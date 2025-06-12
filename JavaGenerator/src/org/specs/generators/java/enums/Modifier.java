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
 * Enum representing Java modifiers for classes, fields, and methods.
 *
 * @author Tiago
 */
public enum Modifier {
    ABSTRACT("abstract"), STATIC("static"), FINAL("final");

    /**
     * Constructor for the Modifier enum.
     *
     * @param type the string representation of the modifier
     */
    Modifier(String type) {
        this.type = type;
    }

    private String type;

    /**
     * Returns the string representation of the modifier.
     *
     * @return the modifier string
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the string representation of the modifier.
     *
     * @return the modifier string
     */
    @Override
    public String toString() {
        return type;
    }
}