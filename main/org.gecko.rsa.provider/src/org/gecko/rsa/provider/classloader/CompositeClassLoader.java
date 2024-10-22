/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.gecko.rsa.provider.classloader;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

/**
 * A ClassLoader, that tries to find a Class in multiple classloaders
 * @author Juergen Albert
 * @since 18 Jul 2019
 */
public class CompositeClassLoader extends ClassLoader {

    private final List<ClassLoader> classLoaders = Collections.synchronizedList(new LinkedList<>());

    /**
     * Creates a new instance with the given {@link ClassLoader} as parent. 
     * The {@link ClassLoader}s of the given {@link Class}es will be added as well.
     * If a class is loaded, the {@link ClassLoader}s from the given {@link Class}es will be asked first and the parent last.
     * @param parent the parent {@link ClassLoader}
     * @param classes the {@link Classes} to get the {@link ClassLoader}s from.
     */
    public CompositeClassLoader(ClassLoader parent, Class<?>[] classes) {
    	super(parent);
    	Arrays.asList(classes).stream()
    	.map(FrameworkUtil::getBundle)
    	.map(bundle -> bundle.adapt(BundleWiring.class))
    	.map(BundleWiring::getClassLoader)
    	.filter(cl -> !classLoaders.contains(cl))
    	.forEach(classLoaders::add);
    }

    /* 
     * (non-Javadoc)
     * @see java.lang.ClassLoader#loadClass(java.lang.String)
     */
    public Class<?> loadClass(String name) throws ClassNotFoundException {
    	
    	Class<? extends Object> clazz = classLoaders.stream().map(cl -> {
    		try {
                return cl.loadClass(name);
            } catch (ClassNotFoundException notFound) {
                return (Class<?>) null;
            }
    	}).filter(c -> c != null).findFirst().orElseGet(() -> null);
    	if(clazz != null) {
    		return clazz;
    	}
    	return super.loadClass(name);
    }

}