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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.symja.ast;

public enum Operator {
    Plus("+", 2),
    Minus("-", 2),
    Times("*", 3),
    Power("^", 4),
    UnaryMinus("-", 4); // What priority it should have?

    private final String symbol;
    private final int priority;

    private Operator(String symbol, int priority) {
        this.symbol = symbol;
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

    public String getSymbol() {
        return symbol;
    }

    public static Operator fromSymjaSymbol(String symjaSymbol) {

        return Enum.valueOf(Operator.class, symjaSymbol);

        // switch (symjaSymbol) {
        // case "Plus":
        // return Plus;
        // case "Times":
        // return Times;
        // case "Power":
        // return Power;
        // default:
        // throw new CaseNotDefinedException(symjaSymbol);
        // }
    }

}
