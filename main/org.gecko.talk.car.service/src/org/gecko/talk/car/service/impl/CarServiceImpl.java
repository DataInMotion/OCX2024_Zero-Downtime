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
package org.gecko.talk.car.service.impl;

import org.gecko.talk.car.model.car.Car;
import org.gecko.talk.car.model.car.CarFactory;
import org.gecko.talk.car.model.car.Person;
import org.gecko.talk.car.service.api.CarServiceApi;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component(name="CarService", immediate = true, property = {
		 "service.exported.interfaces=*",
         "gecko.rsa.id=echo"
})
public class CarServiceImpl implements CarServiceApi{

	private Car theCar;
	
	private @interface Config {
		String carType() default "Trabant 601"; 
		String ownerName() default "Max Mustermann";
	}
	
	@Activate
	public void activate(Config config) {
		System.out.println(String.format("starting with config carType [%s] owner [%s]!", config.carType(), config.ownerName()));
		Car car = createCar(config.carType());
		car.setOwner(createPerson(config.ownerName()));
		theCar = car;
		System.out.println("started");
	}
	
	public void deactivate() {
		System.out.println("cleaning up " + theCar.toString());
		theCar = null;
		System.out.println("cleaned up");
	}
	
	private Person createPerson(String name) {
		Person p = CarFactory.eINSTANCE.createPerson();
		p.setName(name);
		return p;
	}

	private Car createCar(String type) {
		Car c = CarFactory.eINSTANCE.createCar();
		c.setType(type);
		return c;
	}
	
	@Override
	public Car getCar() {
		return theCar;
	}
}
