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

import java.util.List;

import org.specs.generators.java.utils.UniqueList;

import tdrc.utils.StringUtils;

/**
 * Represents a Java generic type for code generation, including the base type
 * and any extending types.
 */
public class JavaGenericType {

    private JavaType theType;
    private List<JavaType> extendingTypes;

    /**
     * Constructs a JavaGenericType with the specified base type.
     *
     * @param theType the base {@link JavaType}
     */
    public JavaGenericType(JavaType theType) {
        setTheType(theType);
        extendingTypes = new UniqueList<>();
    }

    /**
     * Adds an extending type to this generic type.
     *
     * @param extendedType the {@link JavaType} to add
     * @return true if the type was added, false if it was already present
     */
    public boolean addType(JavaType extendedType) {
        return extendingTypes.add(extendedType);
    }

    /**
     * Returns the base type of this generic type.
     *
     * @return the base {@link JavaType}
     */
    public JavaType getTheType() {
        return theType;
    }

    /**
     * Sets the base type of this generic type.
     *
     * @param theType the base {@link JavaType}
     */
    public void setTheType(JavaType theType) {
        this.theType = theType;
    }

    /**
     * Returns the list of extending types.
     *
     * @return a list of {@link JavaType}
     */
    public List<JavaType> getExtendingTypes() {
        return extendingTypes;
    }

    /**
     * Sets the list of extending types.
     *
     * @param extendingTypes the list of {@link JavaType}
     */
    public void setExtendingTypes(List<JavaType> extendingTypes) {
        this.extendingTypes = extendingTypes;
    }

    /**
     * Returns the string representation of this generic type, including canonical
     * type information.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        return "<" + getCanonicalType() + ">";
    }

    /**
     * Returns a simple representation of this type, excluding package names and
     * angle brackets.
     *
     * @return the simple type string
     */
    public String getSimpleType() {
        String toString = theType.getSimpleType();
        if (!extendingTypes.isEmpty()) {
            toString += " extends ";
            toString += StringUtils.join(extendingTypes, JavaType::getSimpleType, "& ");
        }
        return toString;
    }

    /**
     * Returns the canonical representation of this type, including package names
     * but excluding angle brackets.
     *
     * @return the canonical type string
     */
    public String getCanonicalType() {
        String toString = theType.getCanonicalName();
        if (!extendingTypes.isEmpty()) {
            toString += " extends ";
            toString += StringUtils.join(extendingTypes, JavaType::getCanonicalName, "& ");
        }
        return toString;
    }

    /**
     * Returns a simple representation of this type, including angle brackets.
     *
     * @return the wrapped simple type string
     */
    public String getWrappedSimpleType() {
        String toString = "<" + theType.getSimpleType();

        if (!extendingTypes.isEmpty()) {
            toString += " extends ";
            toString += StringUtils.join(extendingTypes, JavaType::getSimpleType, "& ");
        }
        toString += ">";
        return toString;
    }

    @Override
    public JavaGenericType clone() {
        final JavaGenericType genericType = new JavaGenericType(theType.clone());
        this.extendingTypes.forEach(ext -> genericType.addType(ext.clone()));
        return genericType;
    }
}
