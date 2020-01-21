/**
 * Copyright 2018 SPeCS.
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

package org.suikasoft.jOptions.treenode;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import org.suikasoft.jOptions.Interfaces.DataStore;

import pt.up.fe.specs.util.SpecsLogs;

public class ClassesService<T extends DataNode<T>> {

    private final String basePackage;
    private final Class<T> baseClass;
    private final CustomClassnameMapper<T> customClassMap;
    private final Map<String, Class<? extends T>> autoClassMap;
    private final Set<String> warnedClasses;

    private Class<? extends T> defaultClass;

    public ClassesService(String basePackage, Class<T> baseClass, CustomClassnameMapper<T> customClassMap) {
        this.basePackage = basePackage;
        this.baseClass = baseClass;
        this.customClassMap = customClassMap;
        this.autoClassMap = new HashMap<>();
        this.warnedClasses = new HashSet<>();

        defaultClass = null;
    }

    public ClassesService(String basePackage, Class<T> baseClass) {
        this(basePackage, baseClass, new CustomClassnameMapper<T>());
    }

    public ClassesService<T> setDefaultClass(Class<? extends T> defaultClass) {
        this.defaultClass = defaultClass;
        return this;
    }

    public CustomClassnameMapper<T> getCustomClassMap() {
        return customClassMap;
    }

    public Class<? extends T> getClass(String classname) {

        // Try custom map
        Class<? extends T> dataNodeClass = customClassMap.getClass(classname);
        if (dataNodeClass != null) {
            return dataNodeClass;
        }

        // Try cached nodes
        dataNodeClass = autoClassMap.get(classname);
        if (dataNodeClass != null) {
            return dataNodeClass;
        }

        // Try discovering the node
        dataNodeClass = discoverClass(classname);
        autoClassMap.put(classname, dataNodeClass);
        return dataNodeClass;

    }

    private Class<? extends T> discoverClass(String clangClassname) {

        String fullClassname = simpleNameToFullName(clangClassname);

        try {
            // Get class
            Class<?> aClass = Class.forName(fullClassname);

            // Check if class is a subtype of DataNode
            if (!baseClass.isAssignableFrom(aClass)) {
                throw new RuntimeException("Classname '" + clangClassname + "' was converted to a (" + fullClassname
                        + ") that is not a DataNode");
            }

            // Cast class object
            return aClass.asSubclass(baseClass);

        } catch (ClassNotFoundException e) {
            // If default node class is defined, use that class
            if (defaultClass != null) {
                if (!warnedClasses.contains(clangClassname)) {
                    warnedClasses.add(clangClassname);

                    SpecsLogs.info("No parser defined for attribute '" + clangClassname
                            + "', using default class");

                }

                return defaultClass;
            }

            throw new RuntimeException("Could not map classname '" + clangClassname + "' to a node class");
        }
    }

    private String simpleNameToFullName(String nodeClassname) {
        var customName = customSimpleNameToFullName(nodeClassname);

        if (customName != null) {
            return customName;
        }

        // By default, append nodeClassname to basePackage
        return basePackage + "." + nodeClassname;
    }

    /**
     * Override method if you want to define custom rules. Any case that returns null uses the default conversion.
     * 
     * @param nodeClassname
     * @return
     */
    protected String customSimpleNameToFullName(String nodeClassname) {
        return null;
    }

    public BiFunction<DataStore, List<? extends T>, T> getLaraNodeBuilder(
            Class<? extends T> dataNodeClass) {

        // Create builder
        try {
            Constructor<? extends T> constructor = (Constructor<? extends T>) dataNodeClass.getConstructor(
                    DataStore.class,
                    Collection.class);

            return (data, children) -> {
                try {
                    return constructor.newInstance(data, children);
                } catch (Exception e) {
                    throw new RuntimeException("Could not call constructor for DataNode", e);
                }
            };

        } catch (Exception e) {
            SpecsLogs.msgLib("Could not create constructor for DataNode:" + e.getMessage());
            return null;
        }

    }

}
