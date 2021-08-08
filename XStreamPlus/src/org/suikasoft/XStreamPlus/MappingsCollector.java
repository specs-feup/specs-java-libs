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

    public MappingsCollector() {
	collectedClasses = null;
    }

    public Map<String, Class<?>> collectMappings(ObjectXml<?> object) {
	// public Map<String, Class<?>> collectMappings(ObjectXml object) {
	collectedClasses = new HashSet<>();

	return collectMappingsInternal(object);
    }

    private Map<String, Class<?>> collectMappingsInternal(ObjectXml<?> object) {
	// private Map<String, Class<?>> collectMappingsInternal(ObjectXml object) {
	Map<String, Class<?>> mappings = new HashMap<>();

	// Check if object was already collected
	if (collectedClasses.contains(object.getClass())) {
	    return mappings;
	}
	collectedClasses.add(object.getClass());

	// System.out.println("Fields of '"+object.getTargetClass()+"':");
	// TODO: Move this functionality to another method
	for (Field field : object.getTargetClass().getDeclaredFields()) {
	    Class<?> fieldType = field.getType();
	    boolean implementsXmlSerializable = SpecsSystem
		    .implementsInterface(fieldType, XmlSerializable.class);
	    if (!implementsXmlSerializable) {
		continue;
	    }

	    // Check if class was taken in to account
	    ObjectXml<?> nestedXml = object.getNestedXml().get(fieldType);
	    // ObjectXml nestedXml = object.getNestedXml().get(fieldType);
	    if (nestedXml == null) {
		SpecsLogs.warn("Class '" + fieldType.getSimpleName()
			+ "' which implements interface '"
			+ XmlSerializable.class.getSimpleName()
			+ "' was not added to the nested XMLs of '"
			+ object.getTargetClass().getSimpleName() + "'.");
		SpecsLogs
			.msgWarn("Please use protected method 'addNestedXml' in '"
				+ object.getClass().getSimpleName()
				+ "' to add the XmlObject before initiallizing the XStreamFile object.");
		continue;
	    }

	    Map<String, Class<?>> childrenMappings = collectMappingsInternal(nestedXml);
	    addMappings(mappings, childrenMappings);
	}
	// System.out.println(Arrays.asList(object.getTargetClass().getDeclaredFields()));
	// INFO: Checking does not work because XmlObject files do not add the
	// object which implement the XmlSerializable interface, usually they do
	// not have alias problems.

	// Collect added classes to check later if there was any class that
	// implements
	// XmlSerializable that was left out.
	// Set<Class> nestedTargetClasses = new HashSet<Class>();

	// Check XstreamObjects inside first, to follow the hierarchy
	/*
	 * if(NestedXml.class.isInstance(object)) { // Collect all mappings
	 * for(ObjectXml xObj : ((NestedXml) object).getObjects()) { Map<String,
	 * Class> childrenMappings = collectMappingsInternal(xObj);
	 * addMappings(mappings, childrenMappings);
	 * //nestedTargetClasses.add(xObj.getTargetClass()); } }
	 */

	// Add own mappings
	Map<String, Class<?>> ownMappings = object.getMappings();
	if (ownMappings == null) {
	    ownMappings = new HashMap<>();
	}

	// Check if any of the classes in ownMapping implements XmlSerializable,
	// and in that case, check if we are already taking that into account.
	// checkPossbileNestedClasses(ownMappings, nestedTargetClasses);

	addMappings(mappings, ownMappings);
	return mappings;
    }

    private static void addMappings(Map<String, Class<?>> totalMappings,
	    Map<String, Class<?>> newMappings) {
	// Add children mappings
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
    /*
     * private void checkPossbileNestedClasses(Map<String, Class> ownMappings,
     * Set<Class> nestedTargetClasses) { for(String key : ownMappings.keySet())
     * { // Check for each class, if they implement the interface
     * XmlSerializable Class aClass = ownMappings.get(key); //Set<Class>
     * fieldInterfaces = new HashSet<Class>(); //boolean
     * implementsXmlSerializable = implementsInterface(aClass,
     * XmlSerializable.class); boolean implementsXmlSerializable =
     * ProcessUtils.implementsInterface(aClass, XmlSerializable.class);
     * if(!implementsXmlSerializable) { return; }
     * 
     * // Check if class was already taken into account
     * if(nestedTargetClasses.contains(aClass)) { return; }
     * 
     * LoggingUtils.getLogger().
     * warning("One of the mapped classes implements interface '"+
     * XmlSerializable.class+"' and was not taken into account."); } }
     */

}
