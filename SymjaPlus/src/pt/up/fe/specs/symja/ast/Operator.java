/**
 * Copyright 2021 SPeCS.
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

package pt.up.fe.specs.symja.ast;

/**
 * Enum representing mathematical operators supported by Symja AST.
 */
public enum Operator {
    /** Addition operator. */
    Plus("+", 2),
    /** Subtraction operator. */
    Minus("-", 2),
    /** Multiplication operator. */
    Times("*", 3),
    /** Exponentiation operator. */
    Power("^", 4),
    /** Unary minus operator. */
    UnaryMinus("-", 4); // What priority it should have?

    private final String symbol;
    private final int priority;

    /**
     * Constructs an Operator enum.
     *
     * @param symbol   the operator symbol
     * @param priority the operator precedence
     */
    private Operator(String symbol, int priority) {
        this.symbol = symbol;
        this.priority = priority;
    }

    /**
     * Gets the operator precedence.
     *
     * @return the priority value
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Gets the operator symbol.
     *
     * @return the symbol as a string
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Returns the Operator enum from a Symja symbol string.
     *
     * @param symjaSymbol the symbol string
     * @return the corresponding Operator
     */
    public static Operator fromSymjaSymbol(String symjaSymbol) {
        return Enum.valueOf(Operator.class, symjaSymbol);
    }
}
