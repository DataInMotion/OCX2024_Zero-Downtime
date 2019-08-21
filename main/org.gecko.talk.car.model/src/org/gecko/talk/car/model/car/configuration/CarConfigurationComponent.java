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
package org.gecko.talk.car.model.car.configuration;

import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.gecko.talk.car.model.car.CarPackage;
import org.gecko.talk.car.model.car.util.CarResourceFactoryImpl;
import org.gecko.emf.osgi.EPackageConfigurator;
import org.gecko.emf.osgi.ResourceFactoryConfigurator;
import org.gecko.emf.osgi.annotation.EMFModel;
import org.gecko.emf.osgi.annotation.provide.ProvideEMFModel;
import org.gecko.emf.osgi.annotation.provide.ProvideEMFResourceConfigurator;
import org.gecko.emf.osgi.annotation.require.RequireEMF;
import org.osgi.service.component.annotations.Component;

/**
 * <!-- begin-user-doc -->
 * The <b>EPackageConfiguration</b> and <b>ResourceFactoryConfigurator</b> for the model.
 * The package will be registered into a OSGi base model registry.
 * <!-- end-user-doc -->
 * @see EPackageConfigurator
 * @see ResourceFactoryConfigurator
 * @generated
 */
@Component(name="CarConfigurator", immediate=true, service=EPackageConfigurator.class)
@EMFModel(emf_model_name = CarPackage.eNAME, emf_model_nsURI = CarPackage.eNS_URI, emf_model_version = "1.0")
@RequireEMF
@ProvideEMFModel(name = CarPackage.eNAME, nsURI = CarPackage.eNS_URI, version = "1.0")
@ProvideEMFResourceConfigurator( name = CarPackage.eNAME,
	contentType = { "" }, 
	fileExtension = {
	"car"
 	},  
	version = "1.0"
)
public class CarConfigurationComponent implements EPackageConfigurator, ResourceFactoryConfigurator {

	@Override
	public void configureResourceFactory(Registry registry) {
		registry.getExtensionToFactoryMap().put("car", new CarResourceFactoryImpl()); 
		 
	}

	@Override
	public void unconfigureResourceFactory(Registry registry) {
		registry.getExtensionToFactoryMap().remove("car"); 
		 
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.osgi.EPackageConfigurator#configureEPackage(org.eclipse.emf.ecore.EPackage.Registry)
	 */
	@Override
	public void configureEPackage(org.eclipse.emf.ecore.EPackage.Registry registry) {
		CarPackage.eINSTANCE.getClass();
		registry.put(CarPackage.eNS_URI, CarPackage.eINSTANCE);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.gecko.emf.osgi.EPackageConfigurator#unconfigureEPackage(org.eclipse.emf.ecore.EPackage.Registry)
	 */
	@Override
	public void unconfigureEPackage(org.eclipse.emf.ecore.EPackage.Registry registry) {
		registry.remove(CarPackage.eNS_URI);
	}

}
