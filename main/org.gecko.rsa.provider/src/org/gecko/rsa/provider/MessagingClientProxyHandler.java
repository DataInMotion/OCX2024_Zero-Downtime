/**
 * Copyright (c) 2012 - 2018 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.rsa.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.osgi.messaging.Message;
import org.gecko.osgi.messaging.MessagingService;
import org.gecko.rsa.api.DeSerializationContext;
import org.gecko.rsa.api.DeSerializer;
import org.gecko.rsa.api.SerializationContext;
import org.gecko.rsa.api.Serializer;
import org.gecko.rsa.provider.marker.PushStreamMarker;
import org.gecko.rsa.provider.marker.PushStreamMarker.PSDataType;
import org.gecko.rsa.provider.marker.VersionMarker;
import org.gecko.rsa.provider.ser.EObjectDeSerializer;
import org.gecko.rsa.provider.stream.EObjectInputStream;
import org.gecko.rsa.provider.stream.EObjectOutputStream;
import org.gecko.util.common.concurrent.NamedThreadFactory;
import org.gecko.util.pushstream.distributed.DistributedEventSource;
import org.osgi.framework.ServiceException;
import org.osgi.framework.Version;
import org.osgi.util.promise.Deferred;
import org.osgi.util.promise.Promise;
import org.osgi.util.promise.PromiseFactory;
import org.osgi.util.pushstream.PushStream;

/**
 * Invocation handler that is used as proxy for the client implementation of a distribution provider.
 * 
 * It delegates client request to be converted into the messaging data that then are sent to the receiver.
 * On the other hand it also handles the response handling, when a server, responds.
 * 
 * @author Mark Hoffmann
 * @since 07.07.2018
 */
public class MessagingClientProxyHandler implements InvocationHandler {

	private static final Logger logger = Logger.getLogger(MessagingClientProxyHandler.class.getName());
	private final MessagingService messaging;
	private final String topicAddress;
	private final ClassLoader cl;
	private PushStream<Message> receiveData = null;
	private Map<String, PushStream<Object>> receiveDataPS = new ConcurrentHashMap<>();
	private final Map<String, Deferred<ObjectInputStream>> deferredMap = new ConcurrentHashMap<>();
	// Serializer and de-serializer for EMF objects
	private final Serializer<EObject, SerializationContext> serializer;
	private final DeSerializer<EObject, DeSerializationContext> deserializer;
	private final PromiseFactory pf;
	private final ExecutorService psHandler = Executors.newFixedThreadPool(5, NamedThreadFactory.newNamedFactory("Remote-PS-Handler"));
	private final Class<?>[] interfaces;
	
	/**
	 * Creates a new instance.
	 * @param interfaces 
	 */
	public MessagingClientProxyHandler(MessagingService messaging, ResourceSetFactory resourceSetFactory, String endpointId, ClassLoader cl, Class<?>[] interfaces) {
		this.messaging = messaging;
		this.interfaces = interfaces;
		this.pf = new PromiseFactory(Executors.newFixedThreadPool(2, NamedThreadFactory.newNamedFactory("messaging-invocation")));
		this.serializer = new EObjectDeSerializer(resourceSetFactory);
		this.deserializer = new EObjectDeSerializer(resourceSetFactory);
		this.topicAddress = String.format(MessagingRSAEndpoint.MA_DATA_TOPIC, endpointId);
		String responseTopicAddress = String.format(MessagingRSAEndpoint.MA_DATA_RESPONSE_TOPIC, endpointId);
		this.cl = cl;
		try {
			System.out.println("Subscribing for response Topic " + responseTopicAddress);
			this.receiveData = messaging.subscribe(responseTopicAddress);
			this.receiveData.forEach(this::handleSingleDataResponse);
		} catch (Exception e) {
			logger.log(Level.SEVERE, String.format("Error subscribing to receiver topic '%s'", endpointId));
		}

	}

	/* 
	 * (non-Javadoc)
	 * @see java.lang.reflect.InvocationHandler#invoke(java.lang.Object, java.lang.reflect.Method, java.lang.Object[])
	 */
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(!isMethodCallFromInterface(method)) {
			if(method.equals(Object.class.getMethod("equals", Object.class))) {
				return proxy == args[0];
			} else {
				//What now?
				return method.invoke(new Object(), args);
			}
		} else if (Future.class.isAssignableFrom(method.getReturnType()) ||
				CompletionStage.class.isAssignableFrom(method.getReturnType())) {
			return createFutureResult(method, args);
		} else if (Promise.class.isAssignableFrom(method.getReturnType())) {
			return createPromiseResult(method, args);
		} else {
			return handleSyncCall(method, args);
		}
	}

	/**
	 * @param method
	 * @return
	 */
	private boolean isMethodCallFromInterface(Method method) {
		Class<?> declaringClass = method.getDeclaringClass();
		for(Class<?> curInterface : interfaces) {
			if(curInterface.equals(declaringClass)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Handles a data response from the messaging system, which is contained in a message 
	 * @param message the message with the payload
	 */
	private void handleSingleDataResponse(Message message) {
		ByteBuffer buffer = message.payload();
		ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
		Deferred<ObjectInputStream> deferred = null;
		try {
			ObjectInputStream in = new EObjectInputStream(bais, cl, deserializer);
			String correlationId = (String) in.readObject();
			System.out.println("Received message for " + correlationId);
			synchronized (deferredMap) {
				deferred = deferredMap.remove(correlationId);
			}
			if (deferred != null) {
				deferred.resolve(in);
			} else {
				logger.severe(String.format("Did not found a count down latch for id '%s'", correlationId));
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Cannot create BasicInputStream from byte array", e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Cannot find class to read UUID", e);
		}
	}
	
	/**
	 * Handles a {@link PushStreamMarker} and prepares everything to receive data
	 * @param marker the {@link PushStreamMarker}
	 * @throws Exception 
	 */
	private PushStream<?> handlePushstreamMarker(PushStreamMarker marker) throws Exception {
		String id = marker.getCorrelation();
		logger.log(Level.FINE, String.format("[%s] Received PushStreamMarker", id));
		String responseTopic = marker.getReturnChannel();
		String ctrlChannel = marker.getControlChannel();
		DistributedEventSource<Object> distSource = new DistributedEventSource<Object>(Object.class);
		Executors.newSingleThreadExecutor(NamedThreadFactory.newNamedFactory("Remote-PES-" + id)).submit(()->{
			distSource.onClose(()->{
				sendPushstreamControlMessage(null, PSDataType.CLOSE, id, ctrlChannel);
				receiveDataPS.remove(marker.getCorrelation());
			});
			distSource.onConnect(()->sendPushstreamControlMessage(null, PSDataType.OPEN, id, ctrlChannel));
			distSource.onError((t)->{
				sendPushstreamControlMessage(t, PSDataType.ERROR, id, ctrlChannel);
				receiveDataPS.remove(marker.getCorrelation());
			});
		});
		logger.log(Level.FINE, String.format("[%s] Subscribe distributed event source to '%s'", id, responseTopic));
		messaging.subscribe(responseTopic).forEach(m->{
			try {
				handlePushstreamDataResponse(m, distSource);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error handling message for remote pushstream", e);
			}
		});
		PushStream<Object> resultStream = distSource.createPushStream(null);
		receiveDataPS.put(id, resultStream);
		return resultStream;
	}

	/**
	 * Handles a data response that arrives over a pushstream
	 * @param message the message with the payload
	 */
	private void handlePushstreamDataResponse(Message message, DistributedEventSource<Object> eventSource) {
		ByteBuffer buffer = message.payload();
		ByteArrayInputStream bais = new ByteArrayInputStream(buffer.array());
		try (ObjectInputStream in = new EObjectInputStream(bais, cl, deserializer)) {
			// read the id
			String id = (String) in.readObject();
			int ordinal = in.readInt();
			PSDataType type = PSDataType.values()[ordinal];
			logger.log(Level.FINE, String.format("[%s] Received remote message of type %s", id, type));
			switch (type) {
			case DATA:
				Object o = in.readObject();
				psHandler.execute(()->eventSource.doExternalPublish(o));
				break;
			case ERROR:
				Throwable t = (Throwable) in.readObject();
				psHandler.execute(()->eventSource.doExternalError(t));
				break;
			case CLOSE:
				psHandler.execute(()->eventSource.doExternalClose());
				break;
			default:
				break;
			}
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Cannot create BasicInputStream from byte array", e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Cannot find class to read UUID", e);
		}
	}

	/**
	 * Handles a synchronous call
	 * @param method the method to be executed
	 * @param args the method arguments
	 * @return the return value
	 * @throws Throwable the exception 
	 */
	private Object handleSyncCall(Method method, Object[] args) throws Throwable {
		Object result;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream out = new EObjectOutputStream(baos, serializer)) {
			// correlation id
			String id = UUID.randomUUID().toString();
			out.writeObject(id);

			out.writeObject(method.getName());
			out.writeObject(args);
			out.flush();
			ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
			// send request
			System.out.println("Sending request to " + topicAddress);
			Promise<ObjectInputStream> resultPromise = resolveResult(id);
			messaging.publish(topicAddress, buffer);
			result = resultPromise.map(this::readResult).getValue();
		} catch (Throwable e) {
			if (e instanceof ServiceException) {
				throw e;
			}
			throw new ServiceException("Error calling '" + topicAddress + "' method: " + method.getName(), ServiceException.REMOTE, e);
		}
		if (result instanceof Throwable) {
			throw (Throwable)result;
		}
		return result;
	}

	/**
	 * @param correlationId
	 * @return
	 */
	private Promise<ObjectInputStream> resolveResult(String correlationId) {
		Deferred<ObjectInputStream> deferred = pf.deferred();
		synchronized (deferredMap) {
			deferredMap.put(correlationId, deferred);
		}
		return deferred.getPromise().timeout(30000l);
	}

	/**
	 * Reads the result data
	 * @param in the input stream
	 * @return the read and replaced object
	 */
	private Object readResult(ObjectInputStream in) {
		Object o;
		try {
			o = in.readObject();
			return readReplaceMarker(o);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException(e);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
	}

	/**
	 * Replaces objects, if a marker object was dee-serialzed
	 * @param readObject the object to replace
	 * @return the replaced object or the original one
	 * @throws Exception
	 */
	private Object readReplaceMarker(Object readObject) {
		if (readObject instanceof VersionMarker) {
			return new Version(((VersionMarker)readObject).getVersion());
		} else if (readObject instanceof PushStreamMarker) {
			PushStreamMarker marker = (PushStreamMarker) readObject;
			try {
				return handlePushstreamMarker(marker);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Error returning pushstream", e);
				return marker;
			}
		} else {
			return readObject;
		}
	}
	
	/**
	 * Sends a push stream upstream to the sending push stream
	 * @param o the object to be sent
	 * @param type the data type of the event
	 * @param correlation the correlation id
	 * @param ctrlChannel the control channel
	 */
	private void sendPushstreamControlMessage(Object o, PSDataType type, String correlation, String ctrlChannel) {
		logger.log(Level.FINE, String.format("[%s] Sending message of type %s to channel '%s'", correlation, type, ctrlChannel));
		try {
			ByteArrayOutputStream data = writeDataMessage(o, type, correlation);
			messaging.publish(ctrlChannel, ByteBuffer.wrap(data.toByteArray()));
		} catch (Exception e) {
			logger.log(Level.SEVERE, String.format("[%s] Error sending control message to upstream", correlation), e);
		}
		
	}
	
    private ByteArrayOutputStream writeDataMessage(Object object, PSDataType type, String id) throws Exception {
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
		try (ObjectOutputStream objectOutput = new EObjectOutputStream(output, serializer)) {
			objectOutput.writeObject(id);
			objectOutput.writeInt(type.ordinal());
			if (object != null) {
				objectOutput.writeObject(object);
			}
			objectOutput.flush();
			return output;
		} catch (Exception e){
			logger.log(Level.SEVERE, String.format("[%s] Error creating message", id));
			throw e;
		}
    }

	/**
	 * Creates a {@link Future} as result from a request. 
	 * @param method the method to be executed
	 * @param args the method parameters
	 * @return the return object
	 */
	private Object createFutureResult(final Method method, final Object[] args) {
		return CompletableFuture.supplyAsync(new Supplier<Object>() {
			public Object get() {
				try {
					return handleSyncCall(method, args);
				} catch (RuntimeException e) {
					throw e;
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			}
		});
	}

	/**
	 * Creates a {@link Promise} as result from a request. 
	 * @param method the method to be executed
	 * @param args the method parameters
	 * @return the return object
	 */
	private Object createPromiseResult(final Method method, final Object[] args) {
		final Deferred<Object> deferred = pf.deferred();
		ExecutorService es = (ExecutorService) pf.executor();
		es.submit(() -> {
			try {
				deferred.resolve(handleSyncCall(method, args));
			} catch (Throwable e) {
				deferred.fail(e);
			}
		});
		return deferred.getPromise();
	}

}
