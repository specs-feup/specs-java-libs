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

import com.thoughtworks.xstream.XStream;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Utility methods related to XStreamPlus package.
 * 
 * <p>
 * Ex.: reading and writing ObjectXml objects to and from XML files.
 * 
 * @author Joao Bispo
 */
public class XStreamUtils {

    public static XStream newXStream() {

        var xstream = new XStream();

        // XStream.setupDefaultSecurity(xstream);
        // xstream.allowTypesByWildcard(new String[] {
        // "java.**"
        // });

        return xstream;

        // return new XStream();
        // return new XStream(new DomDriver());

        // Taken from here: https://github.com/x-stream/xstream/issues/101#issuecomment-514760040
        // XStream xstream = new XStream(new StaxDriver() {
        // @Override
        // public HierarchicalStreamWriter createWriter(Writer out) {
        // return new PrettyPrintWriter(out, " ");
        // }
        // }) {
        // // only register the converters we need; other converters generate a private access warning in the console
        // // on Java9+...
        // @Override
        // protected void setupConverters() {
        // registerConverter(new NullConverter(), PRIORITY_VERY_HIGH);
        // registerConverter(new IntConverter(), PRIORITY_NORMAL);
        // registerConverter(new FloatConverter(), PRIORITY_NORMAL);
        // registerConverter(new DoubleConverter(), PRIORITY_NORMAL);
        // registerConverter(new LongConverter(), PRIORITY_NORMAL);
        // registerConverter(new ShortConverter(), PRIORITY_NORMAL);
        // registerConverter(new BooleanConverter(), PRIORITY_NORMAL);
        // registerConverter(new ByteConverter(), PRIORITY_NORMAL);
        // registerConverter(new StringConverter(), PRIORITY_NORMAL);
        // registerConverter(new DateConverter(), PRIORITY_NORMAL);
        // registerConverter(new CollectionConverter(getMapper()), PRIORITY_NORMAL);
        // registerConverter(new ReflectionConverter(getMapper(), getReflectionProvider()), PRIORITY_VERY_LOW);
        // }
        // };
        // xstream.autodetectAnnotations(true);
        //
        // // setup proper security by limiting which classes can be loaded by XStream
        // // xstream.addPermission(NoTypePermission.NONE);
        // // xstream.addPermission(new WildcardTypePermission(new String[] { "com.mycompany.**" }));
        //
        // return xstream;

    }

    /*
     * public static boolean write(File file, XmlSerializable object) { return
     * write(file, object, object.getXmlSerializer()); }
     */

    public static <T> boolean write(File file, Object object,
            ObjectXml<T> stream) {
        // public static <T> boolean write(File file, T object, ObjectXml<T>
        // stream) {

        String xmlContents = stream.toXml(object);
        if (xmlContents == null) {
            SpecsLogs.getLogger().warning("Could not generate XML.");
            return false;
        }

        return SpecsIo.write(file, xmlContents);
    }

    /**
     * Generic implementation of write method, without user-defined mappings.
     * 
     * @param file
     * @param object
     * @return
     */
    // public static boolean write(File file, final Object object) {
    public static <T> boolean write(File file, final T object,
            final Class<T> objectClass) {
        // ObjectXml<T> objXml = new ObjectXml<T>() {
        ObjectXml<T> objXml = new ObjectXml<T>() {

            // @SuppressWarnings("unchecked")
            // @Override
            @Override
            public Class<T> getTargetClass() {
                return objectClass;
                // return (Class<?>)object.getClass();
            }
        };

        return write(file, object, objXml);
    }

    /**
     * The XML representation of the object.
     * 
     * TODO: Change name to toXml, after errors are corrected
     * 
     * @param file
     * @param object
     * @return
     */
    public static String toString(final Object object) {
        // public static <T> String toString(final T object) {
        // ObjectXml<T> objXml = new ObjectXml<T>() {
        /*
         * ObjectXml<?> objXml = new ObjectXml<Object>() {
         * 
         * @Override //public Class<?> getTargetClass() { public Class<Object>
         * getTargetClass() { return (Class<Object>) object.getClass(); //
         * Class<?> aClass = object.getClass(); // return aClass; } };
         * 
         * 
         * return objXml.toXml(object);
         */
        XStream xstream = XStreamUtils.newXStream();
        return xstream.toXML(object);
    }

    public static <T> T read(File file, ObjectXml<T> stream) {
        // public static T read(File file, ObjectXml stream) {
        // public static Object read(File file, ObjectXml stream) {
        String xmlContents = SpecsIo.read(file);
        T newObject = stream.fromXml(xmlContents);
        // T newObject = stream.fromXml(xmlContents);
        if (newObject == null) {
            // LoggingUtils.getLogger().
            // warning("Could not get object from XML.");
            return null;
        }

        return newObject;
    }

    /**
     * Generic implementation of read method, without user-defined mappings.
     * 
     * @param file
     * @param objectClass
     * @return
     */
    // public static <T> T read(File file, final Class<T> objectClass) {
    public static <T> T read(File file, final Class<T> objectClass) {
        String contents = SpecsIo.read(file);
        return from(contents, objectClass);
        /*
         * //ObjectXml<T> objXml = new ObjectXml<T>() { ObjectXml objXml = new
         * ObjectXml() {
         * 
         * @Override public Class<?> getTargetClass() { return objectClass; } };
         * 
         * Object anObj = read(file, objXml); return objectClass.cast(anObj);
         * //return read(file, objXml);
         */
    }

    public static <T> T from(String contents, final Class<T> objectClass) {
        ObjectXml<T> objXml = new ObjectXml<T>() {

            @Override
            public Class<T> getTargetClass() {
                return objectClass;
            }
        };

        return objXml.fromXml(contents);
        /*
         * Object anObj = objXml.fromXml(contents);
         * 
         * return objectClass.cast(anObj);
         */
    }

    /**
     * @param aspectDataFile
     * @param aspectData
     */
    public static void write(File file, Object value) {
        String xml = toString(value);
        SpecsIo.write(file, xml);
    }

    /**
     * Copies an object.
     * 
     * @param object
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T copy(T object) {
        XStream xstream = XStreamUtils.newXStream();

        String stringObject = xstream.toXML(object);
        return (T) xstream.fromXML(stringObject);
    }
}
