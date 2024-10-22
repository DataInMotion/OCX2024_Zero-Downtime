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
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.aries.rsa.spi.Endpoint;
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
import org.gecko.rsa.provider.ser.EObjectDeSerializer;
import org.gecko.rsa.provider.stream.EObjectInputStream;
import org.gecko.rsa.provider.stream.EObjectOutputStream;
import org.gecko.util.common.concurrent.NamedThreadFactory;
import org.gecko.util.pushstream.distributed.DistributedConsumer;
import org.osgi.service.remoteserviceadmin.EndpointDescription;
import org.osgi.service.remoteserviceadmin.RemoteConstants;
import org.osgi.util.promise.Promise;
import org.osgi.util.pushstream.PushStream;
import org.osgi.util.pushstream.QueuePolicyOption;

/**
 * An messaging adapter based Aries RSA endpoint. It is the receiver of remote requests from a client
 * 
 * @author Mark Hoffmann
 * @since 07.07.2018
 */
public class MessagingRSAEndpoint implements Endpoint {

	public static final String MA_DATA_TOPIC = "gecko/rsa/data/%s";
	//	public static final String MA_DATA_RESPONSE_TOPIC = "gecko/rsa/data/%s/%s/response";
	public static final String MA_DATA_RESPONSE_TOPIC = "gecko/rsa/data/%s/response";
	public static final String MA_DATA_MANY_RESPONSE_TOPIC = "gecko/rsa/data/%s/%s/psResponse";
	public static final String MA_DATA_MANY_CONTROL_TOPIC = "gecko/rsa/ctrl/%s/%s";
	private static final Logger logger = Logger.getLogger(MessagingRSAEndpoint.class.getName());

	private final MessagingService messaging;
	private EndpointDescription description;
	private PushStream<Message> receiveData = null;
	private String requestResponseAddress;
	private final ClassLoader serviceCL;
	private ServiceMethodInvoker invoker;
	private final ExecutorService publishHandler = Executors.newFixedThreadPool(5, NamedThreadFactory.newNamedFactory("Source-PS-Handler"));
	private final DeSerializer<EObject, DeSerializationContext> deserializer;
	private final Serializer<EObject, SerializationContext> serializer;
	private String endpointId;

	/**
	 * Creates a new instance.
	 */
	public MessagingRSAEndpoint(MessagingService messaging, ResourceSetFactory resourceSetFactory, Object service, Map<String, Object> effectiveProperties) {
		this.messaging = messaging;
		deserializer = new EObjectDeSerializer(resourceSetFactory);
		serializer = new EObjectDeSerializer(resourceSetFactory);
		if (service == null) {
			throw new NullPointerException("Service must not be null");
		}
		this.serviceCL = service.getClass().getClassLoader();
		this.invoker = new ServiceMethodInvoker(service);
		if (effectiveProperties.get(MessagingRSAProvider.MA_CONFIG_TYPE + ".id") != null) {
			throw new IllegalArgumentException("For the tck .. Just to please you!");
		}
		String frameworkId = (String) effectiveProperties.get(RemoteConstants.ENDPOINT_FRAMEWORK_UUID);
		Object serviceId = effectiveProperties.get(RemoteConstants.ENDPOINT_SERVICE_ID);
		endpointId = frameworkId + "_" + serviceId;
		if (endpointId == null) {
			throw new IllegalArgumentException("Remote constant Endpoint_Id is missing");
		}
		initializeMessaging(endpointId);
		effectiveProperties.put(RemoteConstants.ENDPOINT_ID, endpointId);
		effectiveProperties.put(RemoteConstants.SERVICE_EXPORTED_CONFIGS, "");
		effectiveProperties.put(RemoteConstants.SERVICE_INTENTS, Arrays.asList("osgi.basic", "osgi.async"));

		// tck tests for one such property ... so we provide it
		effectiveProperties.put(MessagingRSAProvider.MA_CONFIG_TYPE + ".id", endpointId);
		this.description = new EndpointDescription(effectiveProperties);
	}

	/* 
	 * (non-Javadoc)
	 * @see java.io.Closeable#close()
	 */
	@Override
	public void close() throws IOException {
		if (receiveData != null) {
			receiveData.close();
		}
	}

	/* 
	 * (non-Javadoc)
	 * @see org.apache.aries.rsa.spi.Endpoint#description()
	 */
	@Override
	public EndpointDescription description() {
		return description;
	}

	/**
	 * Initializes the messaging system
	 * @param endpointId the endpointId
	 */
	private void initializeMessaging(String endpointId) {
		this.requestResponseAddress = String.format(MessagingRSAEndpoint.MA_DATA_RESPONSE_TOPIC, endpointId);
		//		this.requestResponseAddress = String.format(MessagingRSAEndpoint.MA_DATA_RESPONSE_TOPIC, endpointId, "%s");
		String requestDataAddress = String.format(MessagingRSAEndpoint.MA_DATA_TOPIC, endpointId);
		try {
			this.receiveData = messaging.subscribe(requestDataAddress);
			this.receiveData.forEach(this::handleRequest);
		} catch (Exception e) {
			logger.log(Level.SEVERE, String.format("Error subscribing to receiver topic '%s'", endpointId));
		}

	}

	/**
	 * Handles the message request
	 * @param message the message object with the payload
	 */
	private void handleRequest(Message message) {
		ByteBuffer buffer = message.payload();
		ByteArrayInputStream input = new ByteArrayInputStream(buffer.array());
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try (ObjectInputStream objectInput = new EObjectInputStream(input, serviceCL, deserializer);
				ObjectOutputStream objectOutput = new EObjectOutputStream(output, serializer)) {
			String id = (String) objectInput.readObject();
			objectOutput.writeObject(id);
			handleCall(objectInput, objectOutput, id);
			//			String responseAddress = String.format(requestResponseAddress, id);
			//			handleSendMessage(output, responseAddress);
			handleSendMessage(output, requestResponseAddress);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Cannot create BasicInputStream from byte array", e);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Cannot find class to read UUID", e);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Cannot handle request because of an error", e);
		}
	}

	/**
	 * Handles the request call
	 * @param requestInput the request input
	 * @param objectResponseOutput the request response data
	 * @throws Exception
	 */
	private void handleCall(ObjectInputStream requestInput, ObjectOutputStream objectResponseOutput, String id) throws Exception {
		String methodName = (String)requestInput.readObject();
		Object[] args = (Object[])requestInput.readObject();
		Object result = invoker.invoke(methodName, args);
		result = resolveAsnyc(result);
		if (result instanceof PushStreamMarker) {
			PushStreamMarker marker = (PushStreamMarker) result;
			marker.setCorrelation(id);
			handlePushstreamMarker(marker);
		} 
		if (result instanceof InvocationTargetException) {
			result = ((InvocationTargetException) result).getCause();
		}
		objectResponseOutput.writeObject(result);
	}

	/**
	 * If there is a async request, we have to handle it this way
	 * @param result the result object
	 * @return the async result
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unchecked")
	private Object resolveAsnyc(Object result) throws InterruptedException {
		if (result instanceof Future) {
			Future<Object> fu = (Future<Object>) result;
			try {
				result = fu.get();
			} catch (ExecutionException e) {
				result = e.getCause();
			}
		} else if (result instanceof CompletionStage) {
			CompletionStage<Object> fu = (CompletionStage<Object>) result;
			try {
				result = fu.toCompletableFuture().get();
			} catch (ExecutionException e) {
				result = e.getCause();
			}
		} else if (result instanceof Promise) {
			Promise<Object> fu = (Promise<Object>) result;  
			try {
				result = fu.getValue();
			} catch (InvocationTargetException e) {
				result = e.getCause();
			}
		} else if (result instanceof PushStream) {
			PushStream<Object> ps = (PushStream<Object>) result;
			PushStreamMarker marker = new PushStreamMarker();
			marker.setBufferSize(100);
			marker.setQueuePolicy(QueuePolicyOption.BLOCK.name());
			marker.setPushstream(ps);
			result = marker;
		}
		return result;
	}

	/**
	 * Handles the send operation
	 * @param output the payload
	 * @param address the publish to address
	 */
	private void handleSendMessage(ByteArrayOutputStream output, String address) {
		ByteBuffer responseData = ByteBuffer.wrap(output.toByteArray());
		try {
			messaging.publish(address, responseData);
		} catch (Exception e) {
			logger.log(Level.SEVERE, String.format("[%s] Cannot send message to response address", address), e);
		}
	}

	/**
	 * Handles a {@link PushStreamMarker}. It prepares everything to enable remote
	 * push-streaming.
	 * @param marker the {@link PushStreamMarker}
	 */
	private void handlePushstreamMarker(PushStreamMarker marker) {
		String id = marker.getCorrelation();
		logger.log(Level.FINE, String.format("[%s] Configure PushStreamMarker", id));

		String responseTopic = String.format(MA_DATA_MANY_RESPONSE_TOPIC, endpointId, id);
		marker.setReturnChannel(responseTopic);
		String controlTopic = String.format(MA_DATA_MANY_CONTROL_TOPIC, endpointId, id);
		marker.setControlChannel(controlTopic);
		publishHandler.submit(()->{
			try {
				configurePushstream(marker);
			} catch (Exception e) {
				logger.log(Level.SEVERE, String.format("[%s] Error configuring PushStream", id), e);
			}
		});
	}

	/**
	 * Configures a pushstream handling for a {@link PushStreamMarker}. This happend when a method
	 * with a push stream return value was called 
	 * @param marker the {@link PushStreamMarker}
	 * @throws Exception
	 */
	private void configurePushstream(PushStreamMarker marker) throws Exception {
		String id = marker.getCorrelation();
		String ctrlChannel = marker.getControlChannel();
		logger.log(Level.FINE, String.format("[%s] Configure PushStream using control stream '%s'", id, ctrlChannel));
		String returnChannel = marker.getReturnChannel();
		logger.log(Level.FINEST, String.format("[%s] Configure PushStream using return stream '%s'", id, returnChannel));
		PushStream<Object> resultStream = marker.getPushstream();
		final DistributedConsumer<Object> consumer = new DistributedConsumer<Object>(resultStream);
		consumer.onAccept(o->sendPushstreamMessage(o, PSDataType.DATA, id, returnChannel));
		consumer.onClose(()->sendPushstreamMessage(null, PSDataType.CLOSE, id, returnChannel));
		consumer.onError(t->sendPushstreamMessage(t, PSDataType.ERROR, id, returnChannel));
		PushStream<Message> ctrlStream = messaging.subscribe(ctrlChannel);
		/*
		 * Listen to the different messages in the return channel.
		 * open connect a remote stream
		 * close close a remote stream
		 * error remote stream sent an error
		 */
		ctrlStream.map(this::getObjectStreamFromMessage).forEach(ois->{
			try {
				ois.readObject();
				int ordinal = ois.readInt();
				PSDataType type = PSDataType.values()[ordinal];
				logger.log(Level.FINE, String.format("[%s] Received control message of type '%s'", id, type));
				switch (type) {
				case OPEN:
					consumer.doConnect();
					break;
				case CLOSE:
					consumer.doExternalClose();
					ctrlStream.close();
					break;
				case ERROR:
					Throwable t = (Throwable) ois.readObject();
					consumer.doExternalError(t);
					ctrlStream.close();
					break;
				default:
					break;
				}
			} catch (Exception e) {
				logger.log(Level.SEVERE, String.format("[%s] Error reading control message", id), e);
			}
		});
	}

	/**
	 * Creates a message data for a remote push stream and sends it
	 * @param o the payload, can be <code>null</code>
	 * @param type the message type
	 * @param id the correlation
	 * @param returnTopic the topic where to publish to
	 */
	private void sendPushstreamMessage(Object o, PSDataType type, String id, String returnTopic) {
		ByteArrayOutputStream data;
		try {
			logger.log(Level.FINE, String.format("[%s] Sending remote push stream message of type %s to return topic %s", id, type, returnTopic));
			data = writePushstreamData(o,type, id);
			handleSendMessage(data, returnTopic);
		} catch (Exception e) {
			logger.log(Level.SEVERE, String.format("[%s] Error mapping message for data %s", id, o), e);
		}
	}

	/**
	 * Writes a data object for remote push streams.
	 * @param object the payload
	 * @param type the data type
	 * @param id the correlation
	 * @return the {@link ByteArrayOutputStream}
	 * @throws Exception
	 */
	private ByteArrayOutputStream writePushstreamData(Object object, PSDataType type, String id) throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try (ObjectOutputStream objectOutput = new EObjectOutputStream(output, serializer)) {
			objectOutput.writeObject(id);
			objectOutput.writeInt(type.ordinal());;
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
	 * Returns the {@link ObjectInputStream} from the message
	 * @param message the message
	 * @return the {@link ObjectInputStream}
	 * @throws Exception
	 */
	private ObjectInputStream getObjectStreamFromMessage(Message message) throws Exception {
		ByteArrayInputStream output = new ByteArrayInputStream(message.payload().array());
		ObjectInputStream objectOutput = new EObjectInputStream(output, serviceCL, deserializer);
		return objectOutput;
	}


}
