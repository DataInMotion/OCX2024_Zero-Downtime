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
package org.gecko.rsa.provider.marker;

import java.io.Serializable;

/**
 * Wrapper class for EMF serialized content
 * @author Mark Hoffmann
 * @since 09.01.2019
 */
public class EObjectMarker implements Serializable {
	
	/** serialVersionUID */
	private static final long serialVersionUID = -6363479553213628299L;
	private byte[] eData;
	private String modelName;
	
	/**
	 * Creates a new instance.
	 */
	public EObjectMarker() {
	}

	/**
	 * Returns the eData.
	 * @return the eData
	 */
	public byte[] getEData() {
		return eData;
	}

	/**
	 * Sets the eData.
	 * @param eData the eData to set
	 */
	public void setEData(byte[] eData) {
		this.eData = eData;
	}

	/**
	 * Returns the modelName.
	 * @return the modelName
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * Sets the modelName.
	 * @param modelName the modelName to set
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

}
