/*
 * Copyright 2010 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.guihelper.Base;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Represents a single field of a setup. It is characterized by its name (.toString) and its type (.getFieldType).
 *
 * <p>
 * We suggest storing information about fields inside an enumeration file implementing this interface. It gives
 * convenient access to the fields.
 *
 * <p>
 * <b>Example implementation of the methods of a SetupFieldEnum interface:</b>
 * 
 * <pre>
 * {@code
 * 
 *    private Setup(FieldType type) {
 *       this.type = type;
 *    }
 * 
 *    public FieldType getType() {
 *       return type;
 *    }
 * 
 *    public String getSetupName() {
 *       return "Setup Name";
 *    }
 *  
 *    private final FieldType type;
 * 
 * 
 * }
 * </pre>
 *
 * @author Joao Bispo
 */
public interface SetupFieldEnum {

    /**
     *
     * @return the type of this field
     */
    FieldType getType();

    /**
     * Represents the name by which the field will be identified.
     * 
     * <p>
     * This identifier is used to identify the fields in saved files and should not be subject to change, at the risk
     * that saved configurations become incompatible.
     *
     *
     * @return
     */
    String name();

    /**
     * The name of the Setup associated to this SetupField.
     * 
     * @return
     */
    String getSetupName();

    /**
     * Inserts a space before each camel case.
     * 
     * @return
     */
    default String getParsedString() {
	return SpecsStrings.camelCaseSeparate(name(), " ");
    }
}
