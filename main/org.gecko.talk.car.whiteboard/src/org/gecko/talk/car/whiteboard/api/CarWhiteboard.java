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
package org.gecko.talk.car.whiteboard.api;

import java.util.List;

import org.gecko.talk.car.model.car.Car;
import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface CarWhiteboard{

	/**
	 * @return creates a {@link List} of all registered {@link Car}s. The result will never be <code>null</code>
	 */
	public List<Car> getAllKnownCars();

}