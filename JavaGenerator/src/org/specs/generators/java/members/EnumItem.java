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

import java.util.ArrayList;
import java.util.List;

import org.specs.generators.java.IGenerate;
import org.specs.generators.java.utils.Utils;

/**
 * Represents an item for a Java enum, including its name and parameters.
 *
 * @author Tiago Carvalho
 */
public class EnumItem implements IGenerate {
    private String name;
    private List<String> parameters;

    /**
     * Constructs an EnumItem with the specified name.
     *
     * @param name the name of the enum item
     */
    public EnumItem(String name) {
        this.name = name;
        parameters = new ArrayList<>();
    }

    /**
     * Adds a parameter to the enum item.
     *
     * @param value the value to add
     */
    public void addParameter(String value) {
        parameters.add(value);
    }

    /**
     * Returns the name of the enum item.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the enum item.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the parameters of the enum item.
     *
     * @return the parameters
     */
    public List<String> getParameters() {
        return parameters;
    }

    /**
     * Sets the parameters of the enum item.
     *
     * @param parameters the parameters to set
     */
    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    /**
     * Returns the hash code for this enum item.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * Checks if this enum item is equal to another object.
     *
     * @param obj the object to compare
     * @return true if equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnumItem other = (EnumItem) obj;
        if (name == null) {
            return other.name == null;
        } else {
            return name.equals(other.name);
        }
    }

    /**
     * Generates the code representation of this enum item with the specified
     * indentation.
     *
     * @param indentation the level of indentation
     * @return the generated code as a StringBuilder
     */
    @Override
    public StringBuilder generateCode(int indentation) {
        final StringBuilder itemBuilder = Utils.indent(indentation);
        itemBuilder.append(name);
        if (!parameters.isEmpty()) {
            itemBuilder.append("(");
            for (final String param : parameters) {
                itemBuilder.append(param + ", ");
            }
            itemBuilder.replace(itemBuilder.length() - 2, itemBuilder.length(), ")");
        }

        return itemBuilder;
    }
}
