/*
 * Copyright 2020 SPeCS Research Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.suikasoft.jOptions.treenode.converter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Applies methods that generate DataStores, based on arbitrary inputs defined by a signature Method.
 *
 * Provides a registry of compatible static methods for parsing node data.
 *
 * @author JoaoBispo
 */
public class NodeDataParser {

    private final Method defaultMethod;
    private final Map<String, Method> dataParsers;
    private final Set<String> warnedNodes;

    /**
     * Constructs a NodeDataParser instance.
     *
     * @param defaultMethod the default method to use when no specific parser is found
     * @param classesWithParsers a collection of classes containing parser methods
     */
    public NodeDataParser(Method defaultMethod, Collection<Class<?>> classesWithParsers) {
        this.defaultMethod = defaultMethod;
        this.dataParsers = new HashMap<>();
        this.warnedNodes = new HashSet<String>();

        // Only supports static methods
        if (!Modifier.isStatic(defaultMethod.getModifiers())) {
            throw new RuntimeException("Compatible methods should be static");
        }

        for (var classWithParsers : classesWithParsers) {
            addParsers(defaultMethod, classWithParsers);
        }
    }

    /**
     * Adds parser methods from the given class to the registry.
     *
     * @param parserMethodSignature the signature method to validate compatibility
     * @param classWithParsers the class containing parser methods
     */
    private void addParsers(Method parserMethodSignature, Class<?> classWithParsers) {
        for (Method method : classWithParsers.getMethods()) {

            // Check if method is compatible with signature
            if (!isValidMethod(method, parserMethodSignature)) {
                continue;
            }

            String methodName = method.getName();

            // Map name of the method to the method class
            dataParsers.put(methodName, method);
        }
    }

    /**
     * Validates if the given method is compatible with the signature method.
     *
     * @param method the method to validate
     * @param signature the signature method to compare against
     * @return true if both methods are considered equivalent
     */
    private boolean isValidMethod(Method method, Method signature) {
        // Check if it is static
        if (!Modifier.isStatic(method.getModifiers())) {
            return false;
        }

        // Check number of parameters
        if (method.getParameterCount() != signature.getParameterCount()) {
            return false;
        }

        // For each parameter, check if they are assignable
        var methodParams = method.getParameterTypes();
        var signatureParams = signature.getParameterTypes();
        for (int i = 0; i < methodParams.length; i++) {
            if (!signatureParams[i].isAssignableFrom(methodParams[i])) {
                return false;
            }
        }

        // Check if return type is compatible
        return signature.getReturnType().isAssignableFrom(method.getReturnType());
    }

    /**
     * Generates the parser method name for the given key.
     *
     * By default, prepends "parse" and appends "Data" to the key.
     *
     * @param key the key to generate the parser name
     * @return the generated parser method name
     */
    public String getParserName(String key) {
        return "parse" + key + "Data";
    }

    /**
     * Parses data using the method associated with the given key.
     *
     * @param key the key identifying the parser method
     * @param args the arguments to pass to the parser method
     * @return the result of the parser method
     */
    public Object parse(String key, Object... args) {
        var methodName = getParserName(key);
        var method = dataParsers.get(methodName);

        if (method == null) {
            if (!warnedNodes.contains(key)) {
                warnedNodes.add(key);
                SpecsLogs.info("Could not find parser for key '" + key + "', that is mapped to the method '"
                        + methodName + "'. Returning default method '" + defaultMethod + "'");
            }

            // Return default method
            method = defaultMethod;
        }

        try {
            return method.invoke(null, args);
        } catch (Exception e) {
            throw new RuntimeException("Problems while invoking method '" + methodName + "'", e);
        }
    }
}
