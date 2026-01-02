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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.utilities.PersistenceFormat;

/**
 * Implementation of {@link PersistenceFormat} for XML serialization using
 * XStream.
 * Allows registering custom ObjectXml mappings for specific classes.
 */
public class XmlPersistence extends PersistenceFormat {

    private final Map<Class<?>, ObjectXml<?>> xmlObjects = new HashMap<>();

    /**
     * Adds a list of ObjectXml mappings to this persistence instance.
     * If a mapping for a class already exists, it will be replaced and a warning
     * will be logged.
     *
     * @param objectXmls the list of ObjectXml mappings to add
     */
    public void addObjectXml(List<ObjectXml<?>> objectXmls) {
        List<Class<?>> replacedClasses = new ArrayList<>();
        for (ObjectXml<?> objectXml : objectXmls) {
            if (xmlObjects.containsKey(objectXml.getTargetClass())) {
                replacedClasses.add(objectXml.getTargetClass());
            }
            xmlObjects.put(objectXml.getTargetClass(), objectXml);
        }
        if (!replacedClasses.isEmpty()) {
            SpecsLogs.warn("Overlap in the following key mappings:"
                    + replacedClasses);
        }
    }

    /**
     * Serializes the given object to an XML string.
     *
     * @param anObject the object to serialize
     * @return the XML string
     */
    @Override
    public String to(Object anObject) {
        ObjectXml<?> objectXml = xmlObjects.get(anObject.getClass());
        if (objectXml == null) {
            return XStreamUtils.toString(anObject);
        }
        return objectXml.toXml(anObject);
    }

    /**
     * Deserializes the given XML string to an object of the specified class.
     *
     * @param contents      the XML string
     * @param classOfObject the class to deserialize to
     * @param <T>           the type of the object
     * @return the deserialized object
     */
    @Override
    public <T> T from(String contents, Class<T> classOfObject) {
        ObjectXml<?> objectXml = xmlObjects.get(classOfObject);
        if (objectXml == null) {
            return XStreamUtils.from(contents, classOfObject);
        }
        return classOfObject.cast(objectXml.fromXml(contents));
    }

    /**
     * Returns the file extension for XML files.
     *
     * @return the string "xml"
     */
    @Override
    public String getExtension() {
        return "xml";
    }

}
