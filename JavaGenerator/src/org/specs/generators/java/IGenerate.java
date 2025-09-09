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
package org.specs.generators.java;

import org.specs.generators.java.utils.Utils;

/**
 * Interface for code generation in JavaGenerator. Implementing classes should
 * provide a method to generate Java code with a specified indentation level.
 *
 * @author Tiago
 */
public interface IGenerate {
    /**
     * Generates code for the implementing object with the given indentation.
     *
     * @param indentation the indentation level
     * @return a StringBuilder containing the generated code
     */
    StringBuilder generateCode(int indentation);

    /**
     * Returns the platform-specific line separator.
     *
     * @return the line separator string
     */
    default String ln() {
        return Utils.ln();
    }
}
