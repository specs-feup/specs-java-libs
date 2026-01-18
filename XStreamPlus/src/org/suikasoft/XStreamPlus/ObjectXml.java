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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Base for transforming an object to and from XML.
 *
 * <p>
 * When implementing this class do not let the XStreamFile object escape to
 * outside of the class, or you might not be able to guarantee the correct
 * behavior of custom toXml() and fromXml() implementations.
 *
 * @param <T> the type handled by this ObjectXml
 */
public abstract class ObjectXml<T> {

    private final Map<Class<?>, ObjectXml<?>> nestedXml = new HashMap<>();
    private final Map<String, Class<?>> mappings = new HashMap<>();
    private final XStreamFile<T> xstreamFile;

    /**
     * Constructs a new ObjectXml and initializes its XStreamFile.
     */
    public ObjectXml() {
        xstreamFile = new XStreamFile<>(this);
    }

    /**
     * Alias mappings, for assigning names to classes. Can be null.
     *
     * @return the alias-to-class mappings
     */
    public Map<String, Class<?>> getMappings() {
        return mappings;
    }

    /**
     * Adds a mapping from alias to class.
     *
     * @param name   the alias
     * @param aClass the class
     */
    public void addMappings(String name, Class<?> aClass) {
        if (mappings.containsKey(name)) {
            throw new RuntimeException("Mapping for name '" + name + "' already present");
        }
        mappings.put(name, aClass);
        xstreamFile.getXstream().alias(name, aClass);
    }

    /**
     * Adds multiple mappings from a map.
     *
     * @param mappings the map of alias-to-class
     */
    public void addMappings(Map<String, Class<?>> mappings) {
        for (Entry<String, Class<?>> entry : mappings.entrySet()) {
            addMappings(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Adds mappings for a list of classes, using their simple names as aliases.
     *
     * @param classes the list of classes
     */
    public void addMappings(List<Class<?>> classes) {
        for (Class<?> aClass : classes) {
            addMappings(aClass.getSimpleName(), aClass);
        }
    }

    /**
     * The class that will be transformed to and from XML.
     *
     * @return the target class
     */
    public abstract Class<T> getTargetClass();

    /**
     * Serializes the given object to XML.
     *
     * @param object the object to serialize
     * @return the XML string
     */
    public String toXml(Object object) {
        return getXStreamFile().toXml(object);
    }

    /**
     * Deserializes the given XML string to an object of type T.
     *
     * @param xmlContents the XML string
     * @return the deserialized object
     */
    public T fromXml(String xmlContents) {
        return getXStreamFile().fromXml(xmlContents);
    }

    /**
     * Returns the XStreamFile used by this ObjectXml.
     *
     * @return the XStreamFile
     */
    protected XStreamFile<T> getXStreamFile() {
        return xstreamFile;
    }

    /**
     * Adds a nested ObjectXml for a specific class.
     *
     * @param objectXml the nested ObjectXml
     */
    protected void addNestedXml(ObjectXml<?> objectXml) {
        ObjectXml<?> returnObject = nestedXml.put(objectXml.getTargetClass(), objectXml);
        if (returnObject != null) {
            SpecsLogs.warn("Replacing ObjectXml for class '" + objectXml.getTargetClass()
                    + "'.");
        }
    }

    /**
     * Returns the map of nested ObjectXml instances.
     *
     * @return the nested ObjectXml map
     */
    public Map<Class<?>, ObjectXml<?>> getNestedXml() {
        return nestedXml;
    }

    /**
     * Registers a custom converter for a specific class.
     *
     * @param supportedClass the class supported by the converter
     * @param converter      the converter implementation
     */
    public <V> void registerConverter(Class<V> supportedClass, StringCodec<V> converter) {
        getXStreamFile().getXstream().registerConverter(new StringConverter<>(supportedClass, converter));
    }
}
