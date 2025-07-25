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
 * Enum representing tags used in JavaDoc comments.
 *
 * @author Tiago
 */
public enum JDocTag {
    AUTHOR("@author"), 
    CATEGORY("@category"), 
    DEPRECATED("@deprecated"), 
    SEE("@see"), 
    VERSION("@version"), 
    PARAM("@param"), 
    RETURN("@return");

    private String tag;

    /**
     * Constructor for JDocTag.
     *
     * @param tag the JavaDoc tag string
     */
    JDocTag(String tag) {
        this.tag = tag;
    }

    /**
     * Returns the JavaDoc tag string.
     *
     * @return the tag string
     */
    public String getTag() {
        return tag;
    }
}