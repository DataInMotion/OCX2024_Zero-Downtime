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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EObject;
import org.gecko.rsa.api.DeSerializationContext;
import org.gecko.rsa.api.DeSerializer;
import org.gecko.rsa.core.EObjectDeSerializationContext;
import org.gecko.rsa.provider.marker.DTOMarker;
import org.gecko.rsa.provider.marker.EObjectMarker;
import org.gecko.rsa.provider.marker.FileMarker;
import org.gecko.rsa.provider.marker.VersionMarker;
import org.osgi.framework.Version;
import org.osgi.util.promise.Promise;

/**
 * Object input stream that can de-serialize {@link EObject}'s 
 * @author Mark Hoffmann
 * @since 09.01.2019
 */
public class EObjectInputStream extends ObjectInputStream {

	private static final Logger logger = Logger.getLogger(EObjectInputStream.class.getName());
    private ClassLoader loader;
	private final DeSerializer<EObject, DeSerializationContext> deserializer;

	/**
	 * Creates a new instance.
	 * @param input the input stream
	 * @param loader the class loader to be used
	 * @param deserializer the {@link EObject} de-serializer
	 * @throws IOException
	 */
	public EObjectInputStream(InputStream input, ClassLoader loader, DeSerializer<EObject, DeSerializationContext> deserializer) throws IOException {
		super(input);
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            public Void run() {
                enableResolveObject(true);
                return null;
            }
        });
        this.loader = loader;
		this.deserializer = deserializer;
	}
	   
	/* 
     * (non-Javadoc)
     * @see java.io.ObjectInputStream#resolveClass(java.io.ObjectStreamClass)
     */
    @Override
    protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
        try {
            String className = desc.getName();
            /* 
             * Must use Class.forName instead of loader.loadClass.
             * This is to handle cases like array of user classes
             */
            return Class.forName(className, false, loader);
        } catch (ClassNotFoundException e) {
            logger.log(Level.FINE, "Error loading class using classloader of user bundle. Trying our own ClassLoader now " + desc.getName());
            return super.resolveClass(desc);
        }
    }

	/* 
	 * (non-Javadoc)
	 * @see java.io.ObjectInputStream#resolveObject(java.lang.Object)
	 */
	@Override
	protected Object resolveObject(Object object) throws IOException {
		if (object instanceof EObjectMarker) {
			logger.fine("Detected a EObjectMarker -> de-serializing EObject");
			EObjectMarker wrapper = (EObjectMarker) object;
			byte[] data = wrapper.getEData();
			Promise<EObject> eObject = deserializer.deserialize(new ByteArrayInputStream(data), EObjectDeSerializationContext.getBinaryDeSerializationContext());
			try {
				EObject eo = eObject.getValue();
				logger.fine("De-serialized EObject from marker " + eo);
				return eo;
			} catch (Exception e) {
				throw new IOException(e);
			}
		} else if (object instanceof VersionMarker) {
			logger.fine("Detected a VersionMarker");
			VersionMarker verionMarker = (VersionMarker)object;
			return Version.parseVersion(verionMarker.getVersion());
		} else if (object instanceof DTOMarker) {
			logger.fine("Detected a DTO");
			DTOMarker dtoMarker = (DTOMarker)object;
			return dtoMarker.getDTO(loader);
		} else if (object instanceof FileMarker) {
			logger.info("Detected a File");
			FileMarker fileMarker = (FileMarker) object;	
			File serverFile = new File(System.getProperty("java.io.tmpdir") + "/RSA/" + fileMarker.getFileName());
			serverFile.getParentFile().mkdirs();
			serverFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(serverFile);
			fos.write(fileMarker.getFileContent());
			fos.close();
			return serverFile;
		} else {
			return super.resolveObject(object);
		}
	}

}
