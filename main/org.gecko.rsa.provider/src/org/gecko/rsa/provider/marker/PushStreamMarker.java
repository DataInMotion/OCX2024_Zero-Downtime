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

import org.osgi.util.pushstream.PushStream;
import org.osgi.util.pushstream.QueuePolicyOption;

/**
 * Wrapper class for EMF serialized content
 * @author Mark Hoffmann
 * @since 09.01.2019
 */
public class PushStreamMarker implements Serializable {
	
	public static enum PSDataType {
		
		OPEN,
		DATA,
		ERROR,
		CLOSE
	}
	
	/** serialVersionUID */
	private static final long serialVersionUID = 1010006075555393177L;
	private int bufferSize = -1;
	private String queuePolicy = QueuePolicyOption.BLOCK.name();
	private String controlChannel = "ctrl";
	private String returnChannel = "";
	private String correlation = "";
	private transient PushStream<Object> pushstream;
	
	/**
	 * Creates a new instance.
	 */
	public PushStreamMarker() {
	}
	
	/**
	 * Sets the correlation.
	 * @param correlation the correlation to set
	 */
	public void setCorrelation(String correlation) {
		this.correlation = correlation;
	}
	
	/**
	 * Returns the correlation.
	 * @return the correlation
	 */
	public String getCorrelation() {
		return correlation;
	}
	
	/**
	 * Sets the returnChannel.
	 * @param returnChannel the returnChannel to set
	 */
	public void setReturnChannel(String returnChannel) {
		this.returnChannel = returnChannel;
	}
	
	/**
	 * Returns the returnChannel.
	 * @return the returnChannel
	 */
	public String getReturnChannel() {
		return returnChannel;
	}
	
	/**
	 * Sets the controlChannel.
	 * @param controlChannel the controlChannel to set
	 */
	public void setControlChannel(String controlChannel) {
		this.controlChannel = controlChannel;
	}
	
	/**
	 * Returns the controlChannel.
	 * @return the controlChannel
	 */
	public String getControlChannel() {
		return controlChannel;
	}
	
	/**
	 * Sets the bufferSize.
	 * @param bufferSize the bufferSize to set
	 */
	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	
	/**
	 * Returns the bufferSize.
	 * @return the bufferSize
	 */
	public int getBufferSize() {
		return bufferSize;
	}
	
	/**
	 * Sets the queuePolicy.
	 * @param queuePolicy the queuePolicy to set
	 */
	public void setQueuePolicy(String queuePolicy) {
		this.queuePolicy = queuePolicy;
	}
	
	/**
	 * Returns the queuePolicy.
	 * @return the queuePolicy
	 */
	public String getQueuePolicy() {
		return queuePolicy;
	}
	
	/**
	 * Sets the pushstream.
	 * @param pushstream the pushstream to set
	 */
	public void setPushstream(PushStream<Object> pushstream) {
		this.pushstream = pushstream;
	}
	
	/**
	 * Returns the pushstream.
	 * @return the pushstream
	 */
	public PushStream<Object> getPushstream() {
		return pushstream;
	}
	
}
