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

import java.io.File;
import java.io.Serializable;

/**
 * Wrapper class for File serialized content
 * @author ilenia
 * @since May 2, 2019
 */
public class FileMarker implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = -1095654742343103324L;
	private byte[] fileContent;	
	private String fileName;
	private String fileId;
	
	/**
	 * Creates a new instance.
	 */
	public FileMarker() {		
	}
	
	public FileMarker(File file, byte[] fileContent, String fileId) {
		this.fileContent = fileContent;
		this.fileId = fileId;
		this.fileName = fileId + "_" + file.getName();			
	}
	
	/**
	 * Returns the file content
	 * @return fileContent the file content
	 */
	public byte[] getFileContent() {
		return this.fileContent;
	}
	
	/**
	 * Sets the file content
	 * @param fileContent the file content to set
	 */
	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}
	
	/**
	 * Returns the file name on the server side
	 * @return fileName, the file name on the server side
	 */
	public String getFileName() {
		return this.fileName;
	}
	
	/**
	 * Sets the file name on the server side
	 * @param fileName the file name on the server side to be set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	/**
	 * Returns the unique identifier associated with the file
	 * @return fileId 
	 */
	public String getFileId() {
		return this.fileId;		
	}

	/**
	 * Sets the unique identifier associated with the file
	 * @param fileId
	 */
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
}
