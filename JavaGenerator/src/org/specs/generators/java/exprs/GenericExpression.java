/**
 * Copyright 2016 SPeCS.
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

package org.specs.generators.java.exprs;

import org.specs.generators.java.utils.Utils;

/**
 * Generic Implementation of an expression which accepts a string and outputs the exactly same string
 * 
 * @author Tiago
 *
 */
public class GenericExpression implements IExpression {

    String expression;

    public GenericExpression(String expression) {
        this.expression = expression;
    }

    public static GenericExpression fromString(String expression) {
        return new GenericExpression(expression);
    }

    @Override
    public StringBuilder generateCode(int indentation) {
        return new StringBuilder(Utils.indent(indentation) + expression);
    }

    @Override
    public String toString() {
        return generateCode(0).toString();
    }
}
