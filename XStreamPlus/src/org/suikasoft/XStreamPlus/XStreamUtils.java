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

import java.io.File;

import org.suikasoft.XStreamPlus.converters.OptionalConverter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Utility methods related to XStreamPlus package, such as reading and writing
 * ObjectXml objects to and from XML files.
 */
public class XStreamUtils {

    /**
     * Creates a new XStream instance with default permissions and converters.
     *
     * @return a configured XStream instance
     */
    public static XStream newXStream() {
        var xstream = new XStream();
        xstream.addPermission(new AnyTypePermission());
        xstream.registerConverter(new OptionalConverter());
        return xstream;
    }

    /**
     * Writes an object to a file using the provided ObjectXml stream.
     *
     * @param file   the file to write to
     * @param object the object to write
     * @param stream the ObjectXml stream to use for serialization
     * @param <T>    the type of the object
     * @return true if the write operation was successful, false otherwise
     */
    public static <T> boolean write(File file, Object object, ObjectXml<T> stream) {
        String xmlContents = stream.toXml(object);
        if (xmlContents == null) {
            SpecsLogs.warn("Could not generate XML.");
            return false;
        }

        return SpecsIo.write(file, xmlContents);
    }

    /**
     * Writes an object to a file using a generic implementation without
     * user-defined mappings.
     *
     * @param file        the file to write to
     * @param object      the object to write
     * @param objectClass the class of the object
     * @param <T>         the type of the object
     * @return true if the write operation was successful, false otherwise
     */
    public static <T> boolean write(File file, final T object, final Class<T> objectClass) {
        ObjectXml<T> objXml = new ObjectXml<>() {
            @Override
            public Class<T> getTargetClass() {
                return objectClass;
            }
        };

        return write(file, object, objXml);
    }

    /**
     * Converts an object to its XML representation.
     *
     * @param object the object to convert
     * @return the XML representation of the object
     */
    public static String toString(final Object object) {
        XStream xstream = XStreamUtils.newXStream();
        return xstream.toXML(object);
    }

    /**
     * Reads an object from a file using the provided ObjectXml stream.
     *
     * @param file   the file to read from
     * @param stream the ObjectXml stream to use for deserialization
     * @param <T>    the type of the object
     * @return the deserialized object, or null if the operation failed
     */
    public static <T> T read(File file, ObjectXml<T> stream) {
        String xmlContents = SpecsIo.read(file);
        T newObject = stream.fromXml(xmlContents);

        return newObject;
    }

    /**
     * Reads an object from a file using a generic implementation without
     * user-defined mappings.
     *
     * @param file        the file to read from
     * @param objectClass the class of the object
     * @param <T>         the type of the object
     * @return the deserialized object
     */
    public static <T> T read(File file, final Class<T> objectClass) {
        String contents = SpecsIo.read(file);
        return from(contents, objectClass);
    }

    /**
     * Converts an XML string to an object of the specified class.
     *
     * @param contents    the XML string
     * @param objectClass the class of the object
     * @param <T>         the type of the object
     * @return the deserialized object
     */
    public static <T> T from(String contents, final Class<T> objectClass) {
        ObjectXml<T> objXml = new ObjectXml<>() {
            @Override
            public Class<T> getTargetClass() {
                return objectClass;
            }
        };

        return objXml.fromXml(contents);
    }

    /**
     * Writes an object to a file.
     *
     * @param file  the file to write to
     * @param value the object to write
     */
    public static void write(File file, Object value) {
        String xml = toString(value);
        SpecsIo.write(file, xml);
    }

    /**
     * Copies an object by serializing and deserializing it.
     *
     * @param object the object to copy
     * @param <T>    the type of the object
     * @return a copy of the object
     */
    @SuppressWarnings("unchecked")
    public static <T> T copy(T object) {
        XStream xstream = XStreamUtils.newXStream();

        String stringObject = xstream.toXML(object);
        return (T) xstream.fromXML(stringObject);
    }
}
