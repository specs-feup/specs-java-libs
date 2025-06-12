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
package org.specs.generators.java.members;

import org.specs.generators.java.types.JavaType;

/**
 * Represents an argument declaration for a method.
 *
 * @author Tiago
 */
public class Argument {
    private String name;
    private JavaType classType;

    /**
     * Creates an Argument with the specified type and name.
     *
     * @param classType the type of the argument
     * @param name the name of the argument
     */
    public Argument(JavaType classType, String name) {
        setName(name);
        setClassType(classType);
    }

    /**
     * Returns the type of the argument.
     *
     * @return the argument type
     */
    public JavaType getClassType() {
        return classType;
    }

    /**
     * Sets the type of the argument.
     *
     * @param classType the argument type to set
     */
    public void setClassType(JavaType classType) {
        this.classType = classType;
    }

    /**
     * Returns the name of the argument.
     *
     * @return the argument name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the argument.
     *
     * @param name the argument name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string representation of the argument.
     *
     * @return the argument as a string
     */
    @Override
    public String toString() {
        return classType.getSimpleType() + " " + name;
    }

    /**
     * Creates a clone of this argument.
     *
     * @return a new Argument instance with the same type and name
     */
    @Override
    public Argument clone() {
        return new Argument(classType.clone(), name);
    }
}
