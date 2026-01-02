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
 * Enum representing privacy levels for classes, fields, and methods in Java.
 *
 * @author Tiago
 */
public enum Privacy {
    PUBLIC("public"),
    PRIVATE("private"),
    PROTECTED("protected"),
    PACKAGE_PROTECTED("");

    /**
     * Constructor for the Privacy enum.
     *
     * @param type the string representation of the privacy level
     */
    Privacy(String type) {
        this.type = type;
    }

    private String type;

    /**
     * Returns the string representation of the privacy level.
     *
     * @return the privacy string
     */
    public String getType() {
        return type;
    }

    /**
     * Returns the string representation of the privacy level.
     *
     * @return the privacy string
     */
    @Override
    public String toString() {
        return type;
    }
}
