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
package org.gecko.talk.car.whiteboard.impl;

import org.osgi.service.component.annotations.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.gecko.talk.car.model.car.Car;
import org.gecko.talk.car.service.api.CarServiceApi;
import org.gecko.talk.car.whiteboard.api.CarWhiteboard;

@Component
public class CarWhiteboardImpl implements CarWhiteboard{

	List<CarServiceApi> carServices = new LinkedList<CarServiceApi>();
	
	@Override
	public List<Car> getAllKnownCars() {
		return carServices.stream().map(CarServiceApi::getCar).collect(Collectors.toList());
	}

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	public void addCarService(CarServiceApi carService) {
		System.out.println("Adding service for car " + carService.getCar().toString());
		carServices.add(carService);
	}

	public void removeCarService(CarServiceApi carService) {
		carServices.remove(carService);
	}
}
