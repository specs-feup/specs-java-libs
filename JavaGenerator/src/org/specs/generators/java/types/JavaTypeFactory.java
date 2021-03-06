/**
 * Copyright 2015 SPeCS.
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

package org.specs.generators.java.types;

import static org.specs.generators.java.types.Primitive.BOOLEAN;
import static org.specs.generators.java.types.Primitive.DOUBLE;
import static org.specs.generators.java.types.Primitive.INT;
import static org.specs.generators.java.types.Primitive.VOID;

import java.util.List;

import org.specs.generators.java.classtypes.JavaClass;

import tdrc.utils.Pair;
import tdrc.utils.StringUtils;

public class JavaTypeFactory {

    static final String JavaLangImport = "java.lang";

    public static final JavaType getWildCardType() {
        return new JavaType("?");
    }

    public static final JavaType getObjectType() {
        return new JavaType(Object.class);
    }

    public static final JavaType getStringType() {
        return new JavaType(String.class);
    }

    public static final JavaType getClassType() {
        return new JavaType(Class.class);
    }

    public static final JavaType getBooleanType() {
        return getPrimitiveType(BOOLEAN);
    }

    public static final JavaType getIntType() {
        return getPrimitiveType(INT);
    }

    public static final JavaType getVoidType() {
        return getPrimitiveType(VOID);
    }

    public static final JavaType getDoubleType() {
        return getPrimitiveType(DOUBLE);
    }

    public static final JavaType getListJavaType(JavaGenericType genericType) {
        final JavaType listTType = new JavaType(List.class);
        listTType.addGeneric(genericType);
        return listTType;
    }

    public static final JavaType getListJavaType(JavaType genericType) {
        return getListJavaType(new JavaGenericType(genericType));
    }

    public static final JavaGenericType getWildExtendsType(JavaType javaType) {
        final JavaType wildType = getWildCardType();
        final JavaGenericType wildExtendsType = new JavaGenericType(wildType);
        wildExtendsType.addType(javaType);
        return wildExtendsType;
    }

    /**
     * Adds the generic javType to the given target javaType
     * 
     * @param targetType
     *            target {@link JavaType}
     * @param genericType
     *            {@link JavaType} to be converted to generic
     */
    public static final void addGenericType(JavaType targetType, JavaType genericType) {
        targetType.addGeneric(new JavaGenericType(genericType));
    }

    public static final JavaType getListStringJavaType() {
        final JavaType listStringType = new JavaType(List.class);
        final JavaGenericType genStrinType = new JavaGenericType(getStringType());
        listStringType.addGeneric(genStrinType);
        return listStringType;
    }

    /**
     * Get the JavaType representation of a given primitive type
     * 
     * @param prim
     *            the {@link Primitive} to convert
     * @return the JavaType of the given primitive
     */
    public static final JavaType getPrimitiveType(Primitive prim) {

        return new JavaType(prim.getType(), JavaTypeFactory.JavaLangImport);
    }

    public static final JavaType getPrimitiveWrapper(Primitive prim) {

        return new JavaType(prim.getPrimitiveWrapper(), JavaTypeFactory.JavaLangImport);
    }

    public static final JavaType getPrimitiveWrapper(String primStr) {
        if (primStr.equals(Integer.class.getSimpleName())) {
            return new JavaType(Integer.class);
        }
        return new JavaType(Primitive.getPrimitive(primStr.toLowerCase()).getPrimitiveWrapper(),
                JavaTypeFactory.JavaLangImport);
    }

    public static final String getDefaultValue(JavaType type) {

        if (type.isPrimitive()) {

            if (type.getName().equals(Primitive.VOID.getType())) {

                return "";
            }

            return type.getName().equals(Primitive.BOOLEAN.getType()) ? "false" : "0";
        }

        return "null";
    }

    public static final boolean isPrimitive(String name) {
        return Primitive.contains(name);
    }

    public static boolean isPrimitiveWrapper(String name) {
        if (name.equals(Integer.class.getSimpleName())) {
            return true;
        }
        return isPrimitive(StringUtils.firstCharToLower(name));
    }

    /**
     * Converts the given {@link JavaClass} into a {@link JavaType}
     * 
     * @param javaClass
     * @return
     */
    public static JavaType convert(JavaClass javaClass) {

        return new JavaType(javaClass.getName(), javaClass.getClassPackage());
    }

    /**
     * Converts the given {@link Class} into a {@link JavaType} <br>
     * <b>WARNING:</b> Do not use this method to convert primitive types, it will throw an exception! If this is the
     * case, please use {@link JavaTypeFactory#getPrimitiveType(Primitive)} instead.
     * 
     * @param javaClass
     * @return
     */
    public static JavaType convert(Class<?> javaClass) {

        return new JavaType(javaClass);
    }

    /**
     * Unwrap the primitive tipe. For instance, for an Integer type an int is returned
     * 
     * @param attrClassType
     * @return
     */
    public static String primitiveUnwrap(String simpleType) {
        if (!isPrimitiveWrapper(simpleType)) {
            return simpleType;
        }
        if (simpleType.equals("Integer")) {
            return "int";
        }
        simpleType = StringUtils.firstCharToLower(simpleType);
        return simpleType;
    }

    public static JavaType primitiveUnwrap(JavaType attrClassType) {
        String simpleType = attrClassType.getSimpleType();
        if (!isPrimitiveWrapper(simpleType)) {
            return attrClassType;
        }
        if (simpleType.equals("Integer")) {
            return getIntType();
        }
        simpleType = StringUtils.firstCharToLower(simpleType);
        return getPrimitiveType(Primitive.getPrimitive(simpleType));
    }

    /**
     * This method process a string to find the array dimension.
     * 
     * @param arrayDimString
     *            The input string, should only contain "[]" multiple times
     * @return
     */
    public static Pair<String, Integer> splitTypeFromArrayDimension(String type) {
        final int arrayDimPos = type.indexOf("[");
        if (arrayDimPos < 0) {
            return new Pair<>(type, 0);
        }
        String arrayDimString = type.substring(arrayDimPos);
        type = type.substring(0, arrayDimPos).trim();
        int arrayDimension = 0;
        do {
            arrayDimString = arrayDimString.replaceFirst("\\[\\]", "");
            arrayDimension++;
        } while (arrayDimString.contains("[]"));

        if (!arrayDimString.trim().isEmpty()) {
            throw new RuntimeException("Bad format for array definition. Bad characters: " + arrayDimString);
        }
        return new Pair<>(type, arrayDimension);
    }
}
