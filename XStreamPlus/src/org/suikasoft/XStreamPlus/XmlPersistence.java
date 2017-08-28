/**
 * Copyright 2012 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License. under the License.
 */

package org.suikasoft.XStreamPlus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.utilities.PersistenceFormat;

/**
 * @author Joao Bispo
 * 
 */
public class XmlPersistence extends PersistenceFormat {

   private final Map<Class<?>, ObjectXml<?>> xmlObjects = SpecsFactory.newHashMap();

    /**
     * @param objectXmls
     */
    public void addObjectXml(List<ObjectXml<?>> objectXmls) {
	List<Class<?>> replacedClasses = new ArrayList<>();

	for (ObjectXml<?> objectXml : objectXmls) {
	    // Check if mapping does not overlap with previous mapping
	    if (xmlObjects.containsKey(objectXml.getTargetClass())) {
		replacedClasses.add(objectXml.getTargetClass());
	    }

	    // Add class to map
	    xmlObjects.put(objectXml.getTargetClass(), objectXml);
	}

	// Show warning message
	if (!replacedClasses.isEmpty()) {
	    SpecsLogs.msgWarn("Overlap in the following key mappings:"
		    + replacedClasses);
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.specs.DymaLib.Graphs.Utils.PersistenceFormat.PersistenceFormat#to
     * (java.lang.Object, java.lang.Object[])
     */
    @Override
    public String to(Object anObject) {
	
	// Check if class of given object is in table
	ObjectXml<?> objectXml = xmlObjects.get(anObject.getClass());
	if (objectXml == null) {
	    return XStreamUtils.toString(anObject);
	}

	return objectXml.toXml(anObject);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.specs.DymaLib.Graphs.Utils.PersistenceFormat.PersistenceFormat#from
     * (java.lang.String, java.lang.Class, java.lang.Object[])
     */
    @Override
    public <T> T from(String contents, Class<T> classOfObject) {
	
	// Check if class of given object is in table
	ObjectXml<?> objectXml = xmlObjects.get(classOfObject);

	if (objectXml == null) {
	    return XStreamUtils.from(contents, classOfObject);
	}
	
	return classOfObject.cast(objectXml.fromXml(contents));
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.util.Utilities.PersistenceFormat#getExtension()
     */
    @Override
    public String getExtension() {
	return "xml";
    }

}
