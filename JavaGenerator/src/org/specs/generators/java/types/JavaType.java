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

import java.util.ArrayList;
import java.util.List;

import tdrc.utils.Pair;
import tdrc.utils.StringUtils;

public class JavaType {

    private String name;
    private String _package;
    private boolean array;
    private int arrayDimension;
    private boolean primitive;
    private boolean isEnum;
    private List<JavaGenericType> generics;

    /**
     * A JavaType with name and package
     *
     * @param name
     * @param _package
     * @param arrayDimension
     *            the dimension of the array (&le; 0 means that this javatype is not an array
     */
    public JavaType(String name, String _package, int arrayDimension) {
        init(name, _package, arrayDimension);
    }

    /**
     * Instance of a JavaType based on a loaded class
     *
     * @param thisClass
     */
    public JavaType(Class<?> thisClass) {
        this(thisClass.getSimpleName(), thisClass.getPackage().getName(), thisClass.isArray());
        setEnum(thisClass.isEnum());
    }

    /**
     * A JavaType with name, package and if it is an array (uses dimension of 1 by default)
     *
     * @param name
     * @param _package
     * @param isArray
     *            is this javatype an array?
     */
    public JavaType(String name, String _package, boolean isArray) {
        this(name, _package, isArray ? 1 : 0);
    }

    /**
     * Instance of a JavaType with the given name
     *
     * @param name
     */
    public JavaType(String name) {
        this(name, null, 0);
    }

    /**
     * A JavaType with name and package
     *
     * @param name
     * @param _package
     */
    public JavaType(String name, String _package) {
        this(name, _package, 0);
    }

    public static JavaType enumType(String name, String _package) {
        JavaType jt = new JavaType(name, _package);
        jt.setEnum(true);
        return jt;
    }

    /**
     * A JavaType with name that is/isn't an array
     *
     * @param name
     * @param isArray
     *            is this javatype an array?
     */
    public JavaType(String name, boolean isArray) {
        this(name, null, isArray);
    }

    /**
     * A JavaType with name that is/isn't an array
     *
     * @param name
     * @param arrayDimension
     *            the dimension of the array (&le; 0 means that this javatype is not an array
     */
    public JavaType(String name, int arrayDimension) {
        this(name, null, arrayDimension);
    }

    private void init(String name, String _package, int arrayDimension) {

        // Process name to retrieve package
        if (_package == null || _package.isEmpty()) {
            final int lastDot = name.lastIndexOf('.');
            if (lastDot > -1) {
                _package = name.substring(0, lastDot);
                name = name.substring(lastDot + 1, name.length());
            }
        } else {

            if (name.contains(".")) {
                throw new RuntimeException("If the package is defined the name should only contain the simple name: "
                        + name + " vs " + _package + "." + name);
            }
        }

        // Process name to retrieve array dimension
        if (name.contains("[]")) {

            if (arrayDimension > 0) {

                throw new RuntimeException("Cannot define array dimension both in the name and int the array argument: "
                        + name + " vs dimension of " + arrayDimension);
            }

            final Pair<String, Integer> splittedType = JavaTypeFactory.splitTypeFromArrayDimension(name);
            name = splittedType.getLeft();
            arrayDimension = splittedType.getRight();
        }
        setEnum(false);
        setName(name);
        setPackage(_package);
        setArrayDimension(arrayDimension);
        setGenerics(new ArrayList<>());
    }

    /**
     * Verify if this java type has a package defined
     *
     * @return
     */
    public boolean hasPackage() {
        return _package != null && !_package.isEmpty();
    }

    /**
     * Get the package of this java type
     *
     * @return
     */
    public String getPackage() {
        return _package;
    }

    /**
     * Set the package of this java type
     *
     * @param _package
     */
    public void setPackage(String _package) {
        this._package = _package;
    }

    /**
     * Get the name of this java type
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * Get the complete name of this java type (package+name)
     *
     * @return
     */
    public String getCanonicalName() {
        if (hasPackage()) {
            return _package + "." + name;
        }

        return name;
    }

    /**
     * Set the name of this java type
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        primitive = JavaTypeFactory.isPrimitive(name);
    }

    /**
     * See if this java type is a primitive
     *
     * @return
     */
    public boolean isPrimitive() {
        return primitive;
    }

    /**
     * @return the array
     */
    public boolean isArray() {
        return array;
    }

    /**
     * Define if this is an array. This method updates the dimension size
     *
     * @param array
     *            if true sets the arrayDimension to 1 else sets the arrayDimension to 0
     */
    public void setArray(boolean array) {
        this.array = array;
        // this.arrayDimension = !array ? 0 : (arrayDimension < 1 ? 1 :
        // arrayDimension);
        if (array) {

            if (arrayDimension < 1) {
                arrayDimension = 1;
            }
        } else {

            arrayDimension = 0;
        }
    }

    public int getArrayDimension() {
        return arrayDimension;
    }

    /**
     * Sets the dimension of the array. This method updates the array field
     *
     * @param arrayDimension
     *            if arrayDimension &gt; 0 then array is set to true; otherwise it is set to false
     *
     */
    public void setArrayDimension(int arrayDimension) {
        this.arrayDimension = arrayDimension;
        array = arrayDimension > 0;
    }

    @Override
    public String toString() {
        String toString = (hasPackage() ? _package + "." : "") + name;
        if (isArray()) { // conditions to avoid possible mistakes: &&
                         // arrayDimension > 0
            toString += StringUtils.repeat("[]", arrayDimension);
        }
        return toString;
    }

    /**
     * This method returns the simple representation of this type, i.e., does not include the package
     *
     * @return
     */
    public String getSimpleType() {
        String toString = name + genericsToString();
        if (isArray()) { // conditions to avoid possible mistakes: &&
                         // arrayDimension > 0
            toString += StringUtils.repeat("[]", arrayDimension);
        }
        return toString;
    }

    /**
     * This method returns the canonical representation of this type, i.e., includes the package
     *
     * @return
     */
    public String getCanonicalType() {
        String toString = getCanonicalName() + genericsToCanonicalString();
        if (isArray()) {// conditions to avoid possible mistakes: &&
                        // arrayDimension > 0
            toString += StringUtils.repeat("[]", arrayDimension);
        }
        return toString;
    }

    public String genericsToString() {
        String genericStr = "";
        if (!generics.isEmpty()) {
            genericStr = "<";
            genericStr += StringUtils.join(generics, JavaGenericType::getSimpleType, ", ");
            genericStr += ">";
        }
        return genericStr;
    }

    public String genericsToCanonicalString() {
        String genericStr = "";
        if (!generics.isEmpty()) {
            genericStr = "<";
            genericStr += StringUtils.join(generics, JavaGenericType::getCanonicalType, ", ");
            genericStr += ">";
        }
        return genericStr;
    }

    /**
     * Says if this java type requires import or not
     *
     * @return true if import is required, false otherwise
     */
    public boolean requiresImport() {
        return hasPackage() && !isPrimitive() && !_package.equals(JavaTypeFactory.JavaLangImport);
    }

    public List<JavaGenericType> getGenerics() {
        return generics;
    }

    public boolean addGeneric(JavaGenericType genericType) {
        return generics.add(genericType);
    }

    public boolean addTypeAsGeneric(JavaType genericType) {
        return addGeneric(new JavaGenericType(genericType));
    }

    public void setGenerics(List<JavaGenericType> generics) {
        this.generics = generics;
    }

    @Override
    public JavaType clone() {
        final JavaType clone = new JavaType(name, _package, arrayDimension);
        generics.forEach(gen -> clone.addGeneric(gen.clone()));
        clone.setEnum(isEnum);
        return clone;
    }

    public boolean isEnum() {
        return isEnum;
    }

    public void setEnum(boolean isEnum) {
        this.isEnum = isEnum;
    }

}
