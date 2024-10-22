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
package org.gecko.rsa.provider;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that is a helper to invoke a service method reflectively
 * @author Mark Hoffmann
 * @since 31.07.2018
 */
public class ServiceMethodInvoker {

    private HashMap<Object, Object> primTypes;
    private Object service;

    public ServiceMethodInvoker(Object service) {
        this.service = service;
        this.primTypes = new HashMap<>();
        this.primTypes.put(Byte.TYPE, Byte.class);
        this.primTypes.put(Short.TYPE, Short.class);
        this.primTypes.put(Integer.TYPE, Integer.class);
        this.primTypes.put(Long.TYPE, Long.class);
        this.primTypes.put(Float.TYPE, Float.class);
        this.primTypes.put(Double.TYPE, Double.class);
        this.primTypes.put(Boolean.TYPE, Boolean.class);
        this.primTypes.put(Character.TYPE, Character.class);
    }
    
    /**
     * Invokes the method of the given name and arguments
     * @param methodName the method name
     * @param args the arguments
     * @return the return object instance of the invocation
     */
    public Object invoke(String methodName, Object[] args) {
        Class<?>[] parameterTypesAr = getArgumentTypes(args);
        Method method = null;
        try {
            method = getMethod(methodName, parameterTypesAr);
            return method.invoke(service, args);
        } catch (Throwable e) {
            return e;
        }
    }
    
    /**
     * Returns the {@link Method} for the given mothod name and signature
     * @param methodName the name of the method
     * @param parameterTypesArg the parameter signature of the provided arguments
     * @return the method instance or throws an exception
     */
    private Method getMethod(String methodName, Class<?>[] parameterTypesArg) {
        try {
            return service.getClass().getMethod(methodName, parameterTypesArg);
        } catch (NoSuchMethodException e) {
            Method[] methods = service.getClass().getMethods();
            for (Method method : methods) {
                if (!method.getName().equals(methodName)) {
                    continue;
                }
                if (matchAllParamTypes(method.getParameterTypes(), parameterTypesArg)) {
                    return method;
                }
            }
            throw new IllegalArgumentException(String.format("No method found that matches name %s, types %s", 
                                                             methodName, Arrays.toString(parameterTypesArg)));
        }
    }

    /**
     * After checking if the number of parameters is consistent, checks if all parameter matches to the method signature
     * @param methodParamTypes the parameter types
     * @param parameterTypesArg the parameter types, of the provided arguments for the method
     * @return <code>true</code>, if the parameters match
     */
    private boolean matchAllParamTypes(Class<?>[] methodParamTypes, Class<?>[] parameterTypesArg) {
    	
    	if(methodParamTypes.length != parameterTypesArg.length) {
    		return false;
    	}
    	
        int c = 0;
        for (Class<?> type : methodParamTypes) {
            if (!matcheParamType(type, parameterTypesArg[c])) {
                return false;
            }
            c++;
        }
        return true;
    }

    /**
     * Checks a single 
     * @param type the type of the signature
     * @param typeArg the type of the provided argument
     * @return <code>true</code>, if the types match
     */
    private boolean matcheParamType(Class<?> type, Class<?> typeArg) {
        if (type.isPrimitive()) {
            return typeArg == primTypes.get(type);
        }
        return type.isAssignableFrom(typeArg);
    }
    
    /**
     * Returns the types of the provided method arguments
     * @param args the arguments
     * @return the corresponding type array
     */
    private Class<?>[] getArgumentTypes(Object[] args) {
        List<Class<?>> parameterTypes = new ArrayList<>();
        if (args != null) {
        	parameterTypes.addAll(Arrays.asList(args)
        			.stream()
        			.map(Object::getClass)
        			.collect(Collectors.toList()));
        }
        Class<?>[] parameterTypesArg = parameterTypes.toArray(new Class[]{});
        return parameterTypesArg;
    }
    
}
