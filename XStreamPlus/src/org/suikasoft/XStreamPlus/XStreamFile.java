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

import java.io.StringWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.CompactWriter;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Utility for serializing and deserializing objects to and from XML using
 * XStream.
 *
 * @param <T> the type handled by this XStreamFile
 * @author Joao Bispo
 */
public class XStreamFile<T> {

    private final ObjectXml<T> config;
    public final static Set<String> reservedAlias;
    public final XStream xstream;
    public boolean useCompactRepresentation;

    static {
        reservedAlias = new HashSet<>();
        reservedAlias.add("string");
        reservedAlias.add("int");
    }

    /**
     * Constructs a new XStreamFile for the given ObjectXml configuration.
     *
     * @param object the ObjectXml configuration
     */
    public XStreamFile(ObjectXml<T> object) {
        this.config = object;
        xstream = newXStream();
        useCompactRepresentation = false;
    }

    /**
     * Creates a new XStreamFile instance for the given ObjectXml configuration.
     *
     * @param object the ObjectXml configuration
     * @param <T>    the type handled
     * @return a new XStreamFile instance
     */
    public static <T> XStreamFile<T> newInstance(ObjectXml<T> object) {
        return new XStreamFile<>(object);
    }

    /**
     * Sets whether to use compact XML representation.
     *
     * @param useCompactRepresentation true for compact, false for pretty
     */
    public void setUseCompactRepresentation(boolean useCompactRepresentation) {
        this.useCompactRepresentation = useCompactRepresentation;
    }

    /**
     * Returns the underlying XStream instance.
     *
     * @return the XStream instance
     */
    public XStream getXstream() {
        return xstream;
    }

    /**
     * Serializes the given object to XML.
     *
     * @param object the object to serialize
     * @return the XML string, or null if the object is not compatible
     */
    public String toXml(Object object) {
        if (object == null) {
            return getXstream().toXML(null);
        }
        if (!(config.getTargetClass().isInstance(object))) {
            SpecsLogs.getLogger().warning(
                    "Given object of class '" + object.getClass() + "' is not "
                            + "compatible with class '" + config.getTargetClass() + "'.");
            return null;
        }
        if (useCompactRepresentation) {
            StringWriter sw = new StringWriter();
            xstream.marshal(object, new CompactWriter(sw));
            return sw.toString();
        }
        return getXstream().toXML(object);
    }

    /**
     * Deserializes the given XML string to an object of type T.
     *
     * @param xmlContents the XML string
     * @return the deserialized object, or null if not compatible
     */
    public T fromXml(String xmlContents) {
        Object dataInstance = xstream.fromXML(xmlContents);
        if (!config.getTargetClass().isInstance(dataInstance)) {
            SpecsLogs.warn(
                    "Given file does not represent a '" + config.getTargetClass() + "' object.");
            return null;
        }
        if (!config.getTargetClass().isInstance(dataInstance)) {
            return null;
        }
        return config.getTargetClass().cast(dataInstance);
    }

    /**
     * Creates a new XStream instance with default configuration.
     *
     * @return a new XStream instance
     */
    private XStream newXStream() {
        MappingsCollector mappingsCollector = new MappingsCollector();
        Map<String, Class<?>> mappings = mappingsCollector.collectMappings(config);

        XStream newSstream = XStreamUtils.newXStream();
        for (String key : mappings.keySet()) {
            // Check if key is not a reserved alias
            if (reservedAlias.contains(key)) {
                SpecsLogs.getLogger().warning(
                        "'" + key + "' is a reserved alias. Skipping this mapping.");
                continue;
            }
            newSstream.alias(key, mappings.get(key));
        }

        return newSstream;
    }

}
