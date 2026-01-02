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

import tdrc.utils.StringUtils;

/**
 * Enumeration of the existing primitive types in Java for code generation.
 *
 * @author tiago
 */
public enum Primitive {

    VOID("void"),
    BYTE("byte"),
    SHORT("short"),
    INT("int"),
    LONG("long"),
    FLOAT("float"),
    DOUBLE("double"),
    BOOLEAN("boolean"),
    CHAR("char"),;

    private String type;

    Primitive(String type) {
        this.type = type;
    }

    /**
     * Returns the string representation of the primitive type.
     *
     * @return the type string
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the {@link Primitive} corresponding to the given name.
     *
     * @param name the name of the primitive type
     * @return the corresponding Primitive
     * @throws RuntimeException if the type is not a primitive
     */
    public static Primitive getPrimitive(String name) {
        for (final Primitive primitive : values()) {
            if (primitive.type.equals(name)) {
                return primitive;
            }
        }
        throw new RuntimeException("The type '" + name + "' is not a primitive.");
    }

    /**
     * Returns the wrapper class name for this primitive type.
     *
     * @return the wrapper class name
     */
    public String getPrimitiveWrapper() {
        if (equals(Primitive.INT)) {
            return "Integer";
        } else if (equals(Primitive.CHAR)) {
            return "Character";
        }
        return StringUtils.firstCharToUpper(type);
    }

    /**
     * Checks if the given name is a valid primitive type.
     *
     * @param name the name to check
     * @return true if the name is a primitive type, false otherwise
     */
    public static boolean contains(String name) {
        for (final Primitive primitive : values()) {
            if (primitive.type.equals(name)) {
                return true;
            }
        }
        return false;
    }
}
