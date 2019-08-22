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
package org.gecko.talk.custom.car.service;

import org.gecko.talk.car.model.car.Car;
import org.gecko.talk.car.model.car.CarFactory;
import org.gecko.talk.car.model.car.Person;
import org.gecko.talk.car.service.api.CarServiceApi;
import org.osgi.service.component.annotations.*;

@Component
public class CustomerCarService implements CarServiceApi{

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.talk.car.service.api.CarServiceApi#getCar()
	 */
	@Override
	public Car getCar() {
		Car car = CarFactory.eINSTANCE.createCar();
		car.setType("Masarati");
		Person customer = CarFactory.eINSTANCE.createPerson();
		customer.setName("The Customer");
		car.setOwner(customer);
		return car;
	}

}
