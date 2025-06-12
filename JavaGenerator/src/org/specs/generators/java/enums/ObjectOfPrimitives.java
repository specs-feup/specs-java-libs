/*
 * Copyright 2013 SPeCS.
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
package org.specs.generators.java.enums;

/**
 * Enum representing object types for Java primitives for code generation.
 */
public enum ObjectOfPrimitives {
    INTEGER(Integer.class.getSimpleName()), DOUBLE(Double.class.getSimpleName()), FLOAT(
            Float.class.getSimpleName()), LONG(Long.class.getSimpleName()), SHORT(Short.class.getSimpleName()), BYTE(
                    Byte.class.getSimpleName()), BOOLEAN(Boolean.class.getSimpleName());

    ObjectOfPrimitives(String type) {
        this.type = type;
    }

    private String type;

    /**
     * Returns the string representation of the object type.
     *
     * @return the type string
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the primitive type name corresponding to the given object type.
     *
     * @param type the object type
     * @return the primitive type name
     */
    public static String getPrimitive(String type) {
        final ObjectOfPrimitives object = ObjectOfPrimitives.valueOf(type.toUpperCase());
        if (object == INTEGER) {
            return int.class.getName();
        }
        return object.type.toLowerCase();
    }

    @Override
    public String toString() {
        return type;
    }

    /**
     * Checks if the given type is contained in the enum.
     *
     * @param type the type to check
     * @return true if the type is contained, false otherwise
     */
    public static boolean contains(String type) {
        for (final ObjectOfPrimitives nt : values()) {
            if (nt.type.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
