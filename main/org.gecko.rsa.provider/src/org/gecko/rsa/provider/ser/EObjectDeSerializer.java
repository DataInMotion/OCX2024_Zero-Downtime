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
package org.gecko.rsa.provider.ser;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.rsa.core.EObjectDeAndSerializer;
import org.gecko.rsa.core.EObjectDeSerializationContext;

/**
 * Serializer and De-serializer for the request and response objects
 * @author Mark Hoffmann
 * @since 24.07.2018
 */
public class EObjectDeSerializer extends EObjectDeAndSerializer<EObject, EObject> {
	
	/**
	 * Creates a new instance.
	 */
	public EObjectDeSerializer() {
		super(EObjectDeSerializationContext.getBinaryDeSerializationContext(), EObjectDeSerializationContext.getBinarySerializationContext());
	}
	
	/**
	 * Creates a new instance.
	 * @param rsf the resource set factory
	 */
	public EObjectDeSerializer(ResourceSetFactory rsf) {
		super(rsf, EObjectDeSerializationContext.getBinaryDeSerializationContext(), EObjectDeSerializationContext.getBinarySerializationContext());
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.rsa.core.EObjectDeAndSerializer#doConfigureResourceSet(org.eclipse.emf.ecore.resource.ResourceSet)
	 */
	@Override
	protected void doConfigureResourceSet(ResourceSet resourceSet) {
		resourceSet.getResourceFactoryRegistry().getContentTypeToFactoryMap().put("application/xml", new XMLResourceFactoryImpl());
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("xml", new XMLResourceFactoryImpl());

	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.rsa.core.EObjectDeAndSerializer#getSerializationContent(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected EObject getSerializationContent(EObject serObject) {
		return serObject;
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.rsa.core.EObjectDeAndSerializer#getDeSerializationContent(org.eclipse.emf.ecore.EObject)
	 */
	@Override
	protected EObject getDeSerializationContent(EObject content) {
		return content;
	}

}
