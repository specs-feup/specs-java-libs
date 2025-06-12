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

import java.util.List;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.classmap.FunctionClassMap;
import pt.up.fe.specs.util.exceptions.CaseNotDefinedException;

/**
 * Utility class for converting Symja AST nodes to C code.
 */
public class SymjaToC {

    /** Function map for node type to converter. */
    private static final FunctionClassMap<SymjaNode, String> CONVERTERS;
    static {
        CONVERTERS = new FunctionClassMap<>();
        CONVERTERS.put(SymjaSymbol.class, SymjaToC::symbolConverter);
        CONVERTERS.put(SymjaInteger.class, SymjaToC::integerConverter);
        CONVERTERS.put(SymjaFunction.class, SymjaToC::functionConverter);
        CONVERTERS.put(SymjaNode.class, SymjaToC::defaultConverter);
    }

    /**
     * Converts a SymjaSymbol node to C code.
     *
     * @param node the symbol node
     * @return the symbol as a string
     */
    private static String symbolConverter(SymjaSymbol node) {
        return node.get(SymjaSymbol.SYMBOL);
    }

    /**
     * Converts a SymjaInteger node to C code.
     *
     * @param node the integer node
     * @return the integer value as a string
     */
    private static String integerConverter(SymjaInteger node) {
        return node.get(SymjaInteger.VALUE_STRING);
    }

    /**
     * Converts a SymjaFunction node to C code.
     *
     * @param node the function node
     * @return the C code as a string
     */
    private static String functionConverter(SymjaFunction node) {
        var firstChild = node.getChild(0);

        if (!(firstChild instanceof SymjaOperator)) {
            throw new CaseNotDefinedException(firstChild);
        }

        var operator = (SymjaOperator) firstChild;

        var operands = node.getChildren().subList(1, node.getNumChildren());

        var code = convertOperator(operator, operands);

        if (node.get(SymjaFunction.HAS_PARENTHESIS)) {
            code = "(" + code + ")";
        }

        return code;
    }

    /**
     * Converts an operator and its operands to C code.
     *
     * @param operator the operator
     * @param operands the operand nodes
     * @return the C code as a string
     */
    private static String convertOperator(SymjaOperator operator, List<SymjaNode> operands) {
        var symbol = operator.get(SymjaOperator.OPERATOR);
        switch (symbol) {
        case Plus:
        case Minus:
        case Times:
            return convertTwoOperandsOperator(symbol, operands);
        case UnaryMinus:
            SpecsCheck.checkSize(operands, 1);
            return convertOneOperandOperator(symbol, operands.get(0), true);
        case Power:
            return "pow(" + CONVERTERS.apply(operands.get(0)) + ", " + CONVERTERS.apply(operands.get(1)) + ")";
        default:
            throw new CaseNotDefinedException(symbol);
        }

    }

    /**
     * Converts a binary operator and its operands to C code.
     *
     * @param operator the operator
     * @param operands the operand nodes
     * @return the C code as a string
     */
    private static String convertTwoOperandsOperator(Operator operator, List<SymjaNode> operands) {
        StringBuilder code = new StringBuilder();

        // Append first operand
        code.append(CONVERTERS.apply(operands.get(0)));
        for (int i = 1; i < operands.size(); i++) {
            code.append(" " + operator.getSymbol() + " " + CONVERTERS.apply(operands.get(i)));
        }

        return code.toString();
    }

    /**
     * Converts a unary operator and its operand to C code.
     *
     * @param operator the operator
     * @param operand the operand node
     * @param isPrefix true if the operator is prefix
     * @return the C code as a string
     */
    private static String convertOneOperandOperator(Operator operator, SymjaNode operand, boolean isPrefix) {
        // TODO: To be more correct, there should be an Operator interface, with UnaryOperator and BinaryOperator
        // subclasses, and the UnaryOperator subclass would have a method .isPrefix()
        if (isPrefix) {
            return operator.getSymbol() + CONVERTERS.apply(operand);
        }

        return CONVERTERS.apply(operand) + operator.getSymbol();
    }

    private static String defaultConverter(SymjaNode node) {
        System.out.println("NOT IMPLEMENTED: " + node.getClass());
        return "<NOP>";
    }

    /**
     * Converts a SymjaNode to C code.
     *
     * @param node the Symja node
     * @return the C code as a string
     */
    public static String convert(SymjaNode node) {
        return CONVERTERS.apply(node);
    }

}
