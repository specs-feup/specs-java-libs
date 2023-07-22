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
import java.util.Arrays;
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

    private final Collection<String> astNodesPackages;
    private final Class<T> baseClass;
    // private final CustomClassnameMapper<T> customClassMap;
    private final Map<String, Class<? extends T>> autoClassMap;
    private final Set<String> warnedClasses;

    private Class<? extends T> defaultClass;

    public ClassesService(Class<T> baseClass, Collection<String> astNodesPackages) {

        this.baseClass = baseClass;
        this.astNodesPackages = astNodesPackages;
        // this.customClassMap = customClassMap;
        this.autoClassMap = new HashMap<>();
        this.warnedClasses = new HashSet<>();

        defaultClass = null;
    }

    // public ClassesService(Class<T> baseClass, Collection<String> astNodesPackages) {
    // this(baseClass, astNodesPackages, new CustomClassnameMapper<T>());
    // }

    public ClassesService(Class<T> baseClass, String... astNodesPackages) {
        this(baseClass, Arrays.asList(astNodesPackages));
    }

    public ClassesService<T> setDefaultClass(Class<? extends T> defaultClass) {
        this.defaultClass = defaultClass;
        return this;
    }

    // public CustomClassnameMapper<T> getCustomClassMap() {
    // return customClassMap;
    // }

    public Class<? extends T> getClass(String classname) {

        // // Try custom map
        // Class<? extends T> dataNodeClass = customClassMap.getClass(classname);
        // if (dataNodeClass != null) {
        // return dataNodeClass;
        // }

        // Try cached nodes
        Class<? extends T> dataNodeClass = autoClassMap.get(classname);
        if (dataNodeClass != null) {
            return dataNodeClass;
        }

        // Try discovering the node
        dataNodeClass = discoverClass(classname);
        autoClassMap.put(classname, dataNodeClass);
        return dataNodeClass;

    }

    private Class<? extends T> getClass(String classname, String fullClassname) {
        try {
            // Get class
            Class<?> aClass = Class.forName(fullClassname);

            // Check if class is a subtype of DataNode
            if (!baseClass.isAssignableFrom(aClass)) {
                throw new RuntimeException("Classname '" + classname + "' was converted to a (" + fullClassname
                        + ") that is not a DataNode");
            }

            // Cast class object
            return aClass.asSubclass(baseClass);

        } catch (ClassNotFoundException e) {
            // No class found, return null
            return null;
            // throw new RuntimeException("Could not map classname '" + classname + "' to a node class");
        }
    }

    private Class<? extends T> discoverClass(String classname) {

        // First, try custom name
        var customName = customSimpleNameToFullName(classname);

        if (customName != null) {
            var nodeClass = getClass(classname, customName);
            if (nodeClass != null) {
                return nodeClass;
            }
        }

        // Look for the class in the given node packages
        for (var astNodesPackage : astNodesPackages) {
            // Append nodeClassname to basePackage
            var fullClassname = astNodesPackage + "." + classname;
            // System.out.println("TRYING CLASS " + fullClassname);
            var nodeClass = getClass(classname, fullClassname);

            if (nodeClass != null) {
                return nodeClass;
            }
        }

        // If default node class is defined and no class was found, use that class
        if (defaultClass != null) {
            if (!warnedClasses.contains(classname)) {
                warnedClasses.add(classname);

                SpecsLogs.info("ClassesService: no node class found for name '" + classname
                        + "', using default class '" + defaultClass + "'");

            }

            return defaultClass;
        }

        // Throw exception if nothing works
        throw new RuntimeException("Could not map classname '" + classname + "' to a node class");

        // try {
        // // Get class
        // Class<?> aClass = Class.forName(fullClassname);
        //
        // // Check if class is a subtype of DataNode
        // if (!baseClass.isAssignableFrom(aClass)) {
        // throw new RuntimeException("Classname '" + classname + "' was converted to a (" + fullClassname
        // + ") that is not a DataNode");
        // }
        //
        // // Cast class object
        // return aClass.asSubclass(baseClass);
        //
        // } catch (ClassNotFoundException e) {
        // // If default node class is defined, use that class
        // if (defaultClass != null) {
        // if (!warnedClasses.contains(classname)) {
        // warnedClasses.add(classname);
        //
        // SpecsLogs.info("ClassesService: no node class found for name '" + classname
        // + "', using default class '" + defaultClass + "'");
        //
        // }
        //
        // return defaultClass;
        // }
        //
        // throw new RuntimeException("Could not map classname '" + classname + "' to a node class");
        // }
    }

    // private String simpleNameToFullName(String nodeClassname) {
    // var customName = customSimpleNameToFullName(nodeClassname);
    //
    // if (customName != null) {
    // return customName;
    // }
    //
    // // By default, append nodeClassname to basePackage
    // return basePackage + "." + nodeClassname;
    // }

    /**
     * Override method if you want to define custom rules. Any case that returns null uses the default conversion.
     * 
     * @param nodeClassname
     * @return
     */
    protected String customSimpleNameToFullName(String nodeClassname) {
        return null;
    }

    public BiFunction<DataStore, List<? extends T>, T> getNodeBuilder(
            Class<? extends T> dataNodeClass) {

        // Create builder
        try {
            Constructor<? extends T> constructor = dataNodeClass.getConstructor(
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
