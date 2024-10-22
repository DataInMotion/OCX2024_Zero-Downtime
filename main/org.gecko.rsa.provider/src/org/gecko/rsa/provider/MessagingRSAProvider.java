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
package org.gecko.rsa.provider;

import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.aries.rsa.spi.DistributionProvider;
import org.apache.aries.rsa.spi.Endpoint;
import org.apache.aries.rsa.util.StringPlus;
import org.gecko.emf.osgi.ResourceSetFactory;
import org.gecko.osgi.messaging.MessagingService;
import org.gecko.rsa.provider.classloader.CompositeClassLoader;
import org.osgi.annotation.bundle.Capability;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.remoteserviceadmin.EndpointDescription;
import org.osgi.service.remoteserviceadmin.RemoteConstants;

/**
 * RSA Distribution provider using the messaging adapter  used by the aries RSA implementation 
 * @author Mark Hoffmann
 * @since 07.07.2018
 */
@Capability(namespace="osgi.remoteserviceadmin.distribution", version="1.1.0", attribute={"configs:List<String>=gecko.rsa.ma"})
@Component(name = "MessagingRSAProvider", configurationPolicy = ConfigurationPolicy.REQUIRE, property = {
		RemoteConstants.REMOTE_CONFIGS_SUPPORTED + "=osgi.basic",
		RemoteConstants.REMOTE_CONFIGS_SUPPORTED + "=osgi.async",
		RemoteConstants.REMOTE_CONFIGS_SUPPORTED + "=gecko.emf",
		RemoteConstants.REMOTE_CONFIGS_SUPPORTED + "=gecko.ma",
		RemoteConstants.REMOTE_INTENTS_SUPPORTED + "=osgi.async",
		RemoteConstants.REMOTE_INTENTS_SUPPORTED + "=osgi.basic"
})
public class MessagingRSAProvider implements DistributionProvider {
	
	public static final String GECKO_RSA_ID = "gecko.rsa.id";
	private static final Logger logger = Logger.getLogger(MessagingRSAProvider.class.getName());
	private static final String[] SUPPORTED_INTENTS = { "osgi.basic", "osgi.async", "gecko.emf", "gecko.ma"};
	static final String MA_CONFIG_TYPE = "gecko.ma";
	
	@Reference(name = "messaging", cardinality = ReferenceCardinality.MANDATORY)
	private MessagingService messaging;

	@Reference(name = "resourceSetFactory", cardinality = ReferenceCardinality.MANDATORY)
	private ResourceSetFactory resourceSetFactory;

	/**
	 * Creates a new instance of an {@link MessagingRSAProvider} with a given message service
	 */
	public MessagingRSAProvider() {
		
	}

	/* 
	 * (non-Javadoc)
	 * @see org.apache.aries.rsa.spi.DistributionProvider#exportService(java.lang.Object, org.osgi.framework.BundleContext, java.util.Map, java.lang.Class[])
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Endpoint exportService(Object service, BundleContext bctx, Map<String, Object> effectiveProperties, Class[] exportedInterfaces) {
		effectiveProperties.put(RemoteConstants.SERVICE_IMPORTED_CONFIGS, getSupportedTypes());
        Set<String> intents = getCombinedIntents(effectiveProperties);
        intents.removeAll(Arrays.asList(SUPPORTED_INTENTS));
        if (!intents.isEmpty()) {
            logger.warning(String.format("Unsupported intents found: %s. Not exporting service", intents));
            return null;
        }
        return new MessagingRSAEndpoint(messaging, resourceSetFactory, service, effectiveProperties);
	}

	/* 
	 * (non-Javadoc)
	 * @see org.apache.aries.rsa.spi.DistributionProvider#importEndpoint(java.lang.ClassLoader, org.osgi.framework.BundleContext, java.lang.Class[], org.osgi.service.remoteserviceadmin.EndpointDescription)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object importEndpoint(ClassLoader cl, BundleContext bctx, Class[] interfaces, EndpointDescription endpoint) {
		try {
			String endpointId = endpoint.getId();
			//We need to create the proxy, with the classloader of the interface Bundle(s). The consuming Bundle only 
			// knows about the packages it really uses. In such a case the proxy would thorw a NoClassDev Exception    
			CompositeClassLoader compositeClassLoader = new CompositeClassLoader(cl, interfaces);
			MessagingClientProxyHandler handler = new MessagingClientProxyHandler(messaging, resourceSetFactory, endpointId, cl, interfaces);
            return Proxy.newProxyInstance(compositeClassLoader, interfaces, handler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
	}

	/* 
	 * (non-Javadoc)
	 * @see org.apache.aries.rsa.spi.DistributionProvider#getSupportedTypes()
	 */
	@Override
	public String[] getSupportedTypes() {
		return SUPPORTED_INTENTS;
	}
	
	/**
	 * Returns the combined intents as {@link Set} extracted from the given properties
	 * @param effectiveProperties the {@link Map} of all properties
	 * @return a {@link Set} of intents or an empty {@link Set}
	 */
	private Set<String> getCombinedIntents(Map<String, Object> effectiveProperties) {
        Set<String> combinedIntents = new HashSet<>();
        List<String> intents = StringPlus.normalize(effectiveProperties.get(RemoteConstants.SERVICE_EXPORTED_INTENTS));
        if (intents != null) {
            combinedIntents.addAll(intents);
        }
        List<String> intentsExtra = StringPlus.normalize(effectiveProperties.get(RemoteConstants.SERVICE_EXPORTED_INTENTS_EXTRA));
        if (intentsExtra != null) {
            combinedIntents.addAll(intentsExtra);
        }
        return combinedIntents;
    }

}
