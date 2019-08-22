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
package org.gecko.talk.custom.car.rest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gecko.emf.osgi.json.annotation.RequireEMFJson;
import org.gecko.emf.osgi.rest.jaxrs.annotation.RequireEMFMessageBodyReaderWriter;
import org.gecko.talk.car.model.car.Car;
import org.gecko.talk.car.model.car.CarFactory;
import org.gecko.talk.car.model.car.CarResponse;
import org.gecko.talk.car.whiteboard.api.CarWhiteboard;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsName;
import org.osgi.service.jaxrs.whiteboard.propertytypes.JaxrsResource;

/**
 * <p>
 * A Customer specific Car Resource 
 * </p>
 * 
 * @since 1.0
 */
@RequireEMFJson
@RequireEMFMessageBodyReaderWriter
@JaxrsResource
@JaxrsName("car")
@Component(service = CarResource.class, enabled = true, scope = ServiceScope.PROTOTYPE)
@Path("/")
public class CarResource {

	@Reference
	private CarWhiteboard whiteboard;
	
	@GET
	@Path("/car")
	@Produces({"application/xml", MediaType.TEXT_PLAIN})
	public Response getCars() {
		try {
			List<Car> cars = new ArrayList<>(whiteboard.getAllKnownCars());
			Collections.sort(cars, (c1, c2) -> {
				return c1.getType().compareTo(c2.getType());
				}
			);
			CarResponse carResponse = CarFactory.eINSTANCE.createCarResponse();
			carResponse.getCars().addAll(cars);
			carResponse.setResultSize(carResponse.getCars().size());
			return Response.ok(carResponse).build();
		} catch(NullPointerException e) {
			return Response.serverError().entity("No CarWhiteboard Available!").build();
		}
		
	}

}
