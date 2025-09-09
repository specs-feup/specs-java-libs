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
 * Enum representing types requiring numerical return for code generation.
 *
 * @author Tiago @
 */
public enum NumeralType {
    INT(int.class.getName()),
    DOUBLE(double.class.getName()),
    FLOAT(float.class.getName()),
    LONG(long.class.getName()),
    SHORT(short.class.getName()),
    BYTE(byte.class.getName());

    NumeralType(String type) {
        this.type = type;
    }

    private String type;

    /**
     * Returns the string representation of the numeral type.
     *
     * @return the type string
     */
    public String getType() {
        return type;
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
        for (final NumeralType nt : values()) {
            if (nt.type.equals(type)) {
                return true;
            }
        }
        return false;
    }
}