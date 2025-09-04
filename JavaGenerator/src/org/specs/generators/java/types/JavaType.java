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
 * specific language governing permissions and limitations under the License.
 */

package org.specs.generators.java.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import tdrc.utils.Pair;
import tdrc.utils.StringUtils;

/**
 * Represents a Java type for code generation, including name, package, array information, and generics.
 */
public class JavaType {

    private String name;
    private String _package;
    private boolean array;
    private int arrayDimension;
    private boolean primitive;
    private boolean isEnum;
    private List<JavaGenericType> generics;

    /**
     * Constructs a JavaType with the specified name, package, and array dimension.
     *
     * @param name the type name
     * @param _package the package name
     * @param arrayDimension the array dimension (≤ 0 means not an array)
     */
    public JavaType(String name, String _package, int arrayDimension) {
        init(name, _package, arrayDimension);
    }

    /**
     * Constructs a JavaType based on a loaded class.
     *
     * @param thisClass the {@link Class} to base the type on
     */
    public JavaType(Class<?> thisClass) {
        // For array classes, we need to handle the component type and dimension separately
        if (thisClass.isArray()) {
            // Get the base component type and count array dimensions
            Class<?> componentType = thisClass;
            int dimensions = 0;
            while (componentType.isArray()) {
                componentType = componentType.getComponentType();
                dimensions++;
            }
            init(componentType.getSimpleName(), 
                 componentType.getPackage() != null ? componentType.getPackage().getName() : null, 
                 dimensions);
            setEnum(componentType.isEnum());
        } else {
            init(thisClass.getSimpleName(), 
                 thisClass.getPackage() != null ? thisClass.getPackage().getName() : null, 
                 0);
            setEnum(thisClass.isEnum());
        }
    }

    /**
     * Constructs a JavaType with name, package, and array flag (dimension 1 if true).
     *
     * @param name the type name
     * @param _package the package name
     * @param isArray true if this type is an array
     */
    public JavaType(String name, String _package, boolean isArray) {
        this(name, _package, isArray ? 1 : 0);
    }

    /**
     * Constructs a JavaType with the given name.
     *
     * @param name the type name
     */
    public JavaType(String name) {
        this(name, null, 0);
    }

    /**
     * Constructs a JavaType with name and package.
     *
     * @param name the type name
     * @param _package the package name
     */
    public JavaType(String name, String _package) {
        this(name, _package, 0);
    }

    /**
     * Creates a JavaType representing an enum.
     *
     * @param name the enum name
     * @param _package the package name
     * @return a new JavaType marked as enum
     */
    public static JavaType enumType(String name, String _package) {
        JavaType jt = new JavaType(name, _package);
        jt.setEnum(true);
        return jt;
    }

    /**
     * Constructs a JavaType with name and array flag.
     *
     * @param name the type name
     * @param isArray true if this type is an array
     */
    public JavaType(String name, boolean isArray) {
        this(name, null, isArray);
    }

    /**
     * Constructs a JavaType with name and array dimension.
     *
     * @param name the type name
     * @param arrayDimension the array dimension (≤ 0 means not an array)
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
                name = name.substring(lastDot + 1);
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
            name = splittedType.left();
            arrayDimension = splittedType.right();
        }
        setEnum(false);
        setName(name);
        setPackage(_package);
        setArrayDimension(arrayDimension);
        setGenerics(new ArrayList<>());
    }

    /**
     * Verify if this java type has a package defined.
     *
     * @return true if the package is defined, false otherwise
     */
    public boolean hasPackage() {
        return _package != null && !_package.isEmpty();
    }

    /**
     * Get the package of this java type.
     *
     * @return the package name
     */
    public String getPackage() {
        return _package;
    }

    /**
     * Set the package of this java type.
     *
     * @param _package the package name
     */
    public void setPackage(String _package) {
        this._package = _package;
    }

    /**
     * Get the name of this java type.
     *
     * @return the type name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the complete name of this java type (package+name).
     *
     * @return the canonical name
     */
    public String getCanonicalName() {
        if (hasPackage()) {
            return _package + "." + name;
        }

        return name;
    }

    /**
     * Set the name of this java type.
     *
     * @param name the type name
     */
    public void setName(String name) {
        this.name = name;
        primitive = JavaTypeFactory.isPrimitive(name);
    }

    /**
     * See if this java type is a primitive.
     *
     * @return true if the type is primitive, false otherwise
     */
    public boolean isPrimitive() {
        return primitive;
    }

    /**
     * @return true if this type is an array, false otherwise
     */
    public boolean isArray() {
        return array;
    }

    /**
     * Define if this is an array. This method updates the dimension size.
     *
     * @param array if true sets the arrayDimension to 1 else sets the arrayDimension to 0
     */
    public void setArray(boolean array) {
        this.array = array;
        if (array) {
            if (arrayDimension < 1) {
                arrayDimension = 1;
            }
        } else {
            arrayDimension = 0;
        }
    }

    /**
     * Get the array dimension of this type.
     *
     * @return the array dimension
     */
    public int getArrayDimension() {
        return arrayDimension;
    }

    /**
     * Sets the dimension of the array. This method updates the array field.
     *
     * @param arrayDimension if arrayDimension > 0 then array is set to true; otherwise it is set to false
     */
    public void setArrayDimension(int arrayDimension) {
        this.arrayDimension = arrayDimension;
        array = arrayDimension > 0;
    }

    @Override
    public String toString() {
        String toString = (hasPackage() ? _package + "." : "") + name;
        if (isArray()) {
            toString += "[]".repeat(arrayDimension);
        }
        return toString;
    }

    /**
     * This method returns the simple representation of this type, i.e., does not include the package.
     *
     * @return the simple type representation
     */
    public String getSimpleType() {
        String toString = name + genericsToString();
        if (isArray()) {
            toString += "[]".repeat(arrayDimension);
        }
        return toString;
    }

    /**
     * This method returns the canonical representation of this type, i.e., includes the package.
     *
     * @return the canonical type representation
     */
    public String getCanonicalType() {
        String toString = getCanonicalName() + genericsToCanonicalString();
        if (isArray()) {
            toString += "[]".repeat(arrayDimension);
        }
        return toString;
    }

    /**
     * Converts the generics of this type to a string representation.
     *
     * @return the generics string representation
     */
    public String genericsToString() {
        String genericStr = "";
        if (!generics.isEmpty()) {
            genericStr = "<";
            genericStr += StringUtils.join(generics, JavaGenericType::getSimpleType, ", ");
            genericStr += ">";
        }
        return genericStr;
    }

    /**
     * Converts the generics of this type to a canonical string representation.
     *
     * @return the generics canonical string representation
     */
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
     * Says if this java type requires import or not.
     *
     * @return true if import is required, false otherwise
     */
    public boolean requiresImport() {
        return hasPackage() && !isPrimitive() && !_package.equals(JavaTypeFactory.JavaLangImport);
    }

    /**
     * Get the generics of this type.
     *
     * @return the list of generics
     */
    public List<JavaGenericType> getGenerics() {
        return generics;
    }

    /**
     * Adds a generic type to this JavaType.
     *
     * @param genericType the {@link JavaGenericType} to add
     * @return true if the generic was added, false otherwise
     */
    public boolean addGeneric(JavaGenericType genericType) {
        return generics.add(genericType);
    }

    /**
     * Adds a type as a generic to this JavaType.
     *
     * @param genericType the {@link JavaType} to add as a generic
     * @return true if the generic was added, false otherwise
     */
    public boolean addTypeAsGeneric(JavaType genericType) {
        return addGeneric(new JavaGenericType(genericType));
    }

    /**
     * Sets the generics of this type.
     *
     * @param generics the list of {@link JavaGenericType} to set
     */
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

    /**
     * Checks if this type is an enum.
     *
     * @return true if this type is an enum, false otherwise
     */
    public boolean isEnum() {
        return isEnum;
    }

    /**
     * Sets whether this type is an enum.
     *
     * @param isEnum true if this type is an enum, false otherwise
     */
    public void setEnum(boolean isEnum) {
        this.isEnum = isEnum;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        JavaType javaType = (JavaType) obj;

        return this.hashCode() == javaType.hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, _package, array, arrayDimension, primitive, isEnum, generics);
    }

}
