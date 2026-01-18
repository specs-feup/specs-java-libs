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

package org.specs.generators.java.enums;

/**
 * Enum representing common Java annotations for code generation.
 */
public enum Annotation {

    OVERRIDE("Override"),
    DEPRECATED("Deprecated"),
    SUPPRESSWARNINGS("SuppressWarnings"),
    SAFEVARARGS("SafeVarargs"),
    FUNCTIONALINTERFACE("FunctionalInterface"),
    TARGET("Target");

    private String tag;
    private final String AtSign = "@";

    Annotation(String tag) {
        this.tag = tag;
    }

    /**
     * Returns the annotation tag with '@' prefix.
     *
     * @return the annotation tag
     */
    public String getTag() {
        return AtSign + tag;
    }

    @Override
    public String toString() {
        return getTag();
    }
}
