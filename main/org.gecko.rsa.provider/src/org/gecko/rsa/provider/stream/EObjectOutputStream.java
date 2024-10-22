/**
 * Copyright (c) 2012 - 2019 Data In Motion and others.
 * All rights reserved. 
 * 
 * This program and the accompanying materials are made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.gecko.rsa.provider.stream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.gecko.rsa.api.SerializationContext;
import org.gecko.rsa.api.Serializer;
import org.gecko.rsa.core.EObjectDeSerializationContext;
import org.gecko.rsa.provider.marker.DTOMarker;
import org.gecko.rsa.provider.marker.DTOUtil;
import org.gecko.rsa.provider.marker.EObjectMarker;
import org.gecko.rsa.provider.marker.FileMarker;
import org.gecko.rsa.provider.marker.VersionMarker;
import org.osgi.framework.Version;
import org.osgi.util.promise.Promise;

/**
 * An output stream that can also serialize {@link EObject}'s
 * @author Mark Hoffmann
 * @since 09.01.2019
 */
public class EObjectOutputStream extends ObjectOutputStream {

	private final Serializer<EObject, SerializationContext> serializer;

	/**
	 * Creates a new instance.
	 * @param output the output stream
	 * @param serializer the {@link EObject} serializer
	 * @throws IOException
	 */
	public EObjectOutputStream(OutputStream output, Serializer<EObject, SerializationContext> serializer) throws IOException {
		super(output);
		AccessController.doPrivileged(new PrivilegedAction<Void>() {
			public Void run() {
				enableReplaceObject(true);
				return null;
			}
		});
		this.serializer = serializer;
	}

	/* 
	 * (non-Javadoc)
	 * @see java.io.ObjectOutputStream#replaceObject(java.lang.Object)
	 */
	@Override
	protected Object replaceObject(Object object) throws IOException {
		if (object instanceof Serializable || object.getClass().isArray()) {
			if (object instanceof File) {
				File file = (File) object;
				byte[] fileContent = Files.readAllBytes(file.toPath());
				String uuid = UUID.randomUUID().toString();
				return new FileMarker(file, fileContent, uuid);
			}
			return object;
		} else if (object instanceof Version) {
			return new VersionMarker((Version) object);
		} else if (DTOUtil.isDTOType(object.getClass())){
			return new DTOMarker(object);
		} else if (object instanceof EObject) {
			EObject eObject = (EObject) object;
			String nsUri = eObject.eClass().getEPackage().getNsURI(); 

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Promise<OutputStream> promise = serializer.serialize(eObject, baos, EObjectDeSerializationContext.getBinarySerializationContext());
			try {
				EObjectMarker marker = promise
						.filter(out->out instanceof ByteArrayOutputStream)
						.map(out->(ByteArrayOutputStream)out)
						.map(ByteArrayOutputStream::toByteArray)
						.map(data->createMarker(data, nsUri)).getValue();
				return marker;
			} catch (Exception e) {
				throw new IOException(e);
			}
		} else {
			return super.replaceObject(object);
		}
	}

	/**
	 * Creates a marker object for the given data and model name
	 * @param data the EMF data as byte[]
	 * @param modelName the name-space URI for the model
	 * @return the instance of the marker
	 */
	private EObjectMarker createMarker(byte[] data, String modelName) {
		EObjectMarker marker = new EObjectMarker();
		marker.setModelName(modelName);
		marker.setEData(data);
		return marker;
	}

}
