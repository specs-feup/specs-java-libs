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
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.suikasoft.XStreamPlus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Base for transforming an object to and from XML.
 * 
 * <p>
 * When implementing this class do not let the XStreamFile object escape to outside of the class, or you might not be
 * able to guarantee the correct behavior of custom toXml() and fromXml() implementations.
 * 
 * @author Joao Bispo
 */
public abstract class ObjectXml<T> {

    private final Map<Class<?>, ObjectXml<?>> nestedXml = new HashMap<>();
    private final Map<String, Class<?>> mappings = new HashMap<>();

    /**
     * Alias mappings, for assigning names to classes. Can be null.
     * 
     * @return
     */
    public Map<String, Class<?>> getMappings() {
	return mappings;
    }

    public void addMappings(String name, Class<?> aClass) {
	if (mappings.containsKey(name)) {
	    throw new RuntimeException("Mapping for name '" + name + "' already present");
	}

	mappings.put(name, aClass);
    }

    public void addMappings(Map<String, Class<?>> mappings) {
	for (Entry<String, Class<?>> entry : mappings.entrySet()) {
	    addMappings(entry.getKey(), entry.getValue());
	}
    }

    public void addMappings(List<Class<?>> classes) {
	for (Class<?> aClass : classes) {
	    addMappings(aClass.getSimpleName(), aClass);
	}
    }

    /**
     * The class that will be transformed to and from XML.
     * 
     * @return
     */
    public abstract Class<T> getTargetClass();

    public String toXml(Object object) {
	return getXStreamFile().toXml(object);
    }

    public T fromXml(String xmlContents) {
	return getXStreamFile().fromXml(xmlContents);
    }

    protected XStreamFile<T> getXStreamFile() {
	return new XStreamFile<>(this);
    }

    protected void addNestedXml(ObjectXml<?> objectXml) {
	ObjectXml<?> returnObject = nestedXml.put(objectXml.getTargetClass(), objectXml);
	if (returnObject != null) {
	    SpecsLogs.msgWarn("Replacing ObjectXml for class '" + objectXml.getTargetClass()
		    + "'.");
	}
    }

    public Map<Class<?>, ObjectXml<?>> getNestedXml() {
	return nestedXml;
    }

}
