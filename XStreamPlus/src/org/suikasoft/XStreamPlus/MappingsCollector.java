/*
 * Copyright 2011 SPeCS Research Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package org.suikasoft.XStreamPlus;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSystem;

/**
 * Collects the alias mappings for an XstreamObject.
 *
 * @author Joao Bispo
 */
public class MappingsCollector {

    private Set<Class<?>> collectedClasses;

    /**
     * Creates a new MappingsCollector instance.
     */
    public MappingsCollector() {
        collectedClasses = null;
    }

    /**
     * Collects all alias mappings for the given ObjectXml instance.
     *
     * @param object the ObjectXml instance
     * @return a map of alias to class
     */
    public Map<String, Class<?>> collectMappings(ObjectXml<?> object) {
        collectedClasses = new HashSet<>();
        return collectMappingsInternal(object);
    }

    /**
     * Recursively collects alias mappings from the given ObjectXml and its nested ObjectXmls.
     *
     * @param object the ObjectXml to process
     * @return a map of alias to class for the given object and its nested objects
     */
    private Map<String, Class<?>> collectMappingsInternal(ObjectXml<?> object) {
        Map<String, Class<?>> mappings = new HashMap<>();
        if (collectedClasses.contains(object.getClass())) {
            return mappings;
        }
        collectedClasses.add(object.getClass());
        for (Field field : object.getTargetClass().getDeclaredFields()) {
            Class<?> fieldType = field.getType();
            boolean implementsXmlSerializable = SpecsSystem
                    .implementsInterface(fieldType, XmlSerializable.class);
            if (!implementsXmlSerializable) {
                continue;
            }
            ObjectXml<?> nestedXml = object.getNestedXml().get(fieldType);
            if (nestedXml == null) {
                SpecsLogs.warn("Class '" + fieldType.getSimpleName()
                        + "' which implements interface '"
                        + XmlSerializable.class.getSimpleName()
                        + "' was not added to the nested XMLs of '"
                        + object.getTargetClass().getSimpleName() + "'.");
                // TODO: Move this functionality to another method
                SpecsLogs.warn("Please use protected method 'addNestedXml' in '"
                        + object.getClass().getSimpleName()
                        + "' to add the XmlObject before initiallizing the XStreamFile object.");
                continue;
            }
            Map<String, Class<?>> childrenMappings = collectMappingsInternal(nestedXml);
            addMappings(mappings, childrenMappings);
        }

        // Also process any nested XML objects that don't correspond to XmlSerializable fields
        for (ObjectXml<?> nestedXml : object.getNestedXml().values()) {
            if (!collectedClasses.contains(nestedXml.getClass())) {
                Map<String, Class<?>> nestedMappings = collectMappingsInternal(nestedXml);
                addMappings(mappings, nestedMappings);
            }
        }
        
        Map<String, Class<?>> ownMappings = object.getMappings();
        if (ownMappings == null) {
            ownMappings = new HashMap<>();
        }
        addMappings(mappings, ownMappings);
        return mappings;
    }

    /**
     * Adds mappings from newMappings into totalMappings, warning if an alias is already present.
     *
     * @param totalMappings the map to add to
     * @param newMappings the map of new mappings to add
     */
    private static void addMappings(Map<String, Class<?>> totalMappings,
            Map<String, Class<?>> newMappings) {
        for (String key : newMappings.keySet()) {
            Class<?> childClass = newMappings.get(key);
            if (totalMappings.containsKey(key)) {
                Class<?> definedClass = totalMappings.get(key);
                SpecsLogs.getLogger().warning(
                        "Alias '" + key + "' is already defined for class '"
                                + definedClass
                                + "'. Skipping this mapping for class '"
                                + childClass + "'.");
                continue;
            }
            totalMappings.put(key, childClass);
        }
    }
}
