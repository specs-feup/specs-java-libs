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

import java.util.ArrayList;

import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.ast.ASTNode;
import org.matheclipse.parser.client.ast.FunctionNode;
import org.matheclipse.parser.client.ast.IntegerNode;
import org.matheclipse.parser.client.ast.SymbolNode;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

/**
 * Utility class for parsing and converting Symja AST nodes.
 */
public class SymjaAst {

    /** Function map for AST node type to SymjaNode converter. */
    private static final FunctionClassMap<ASTNode, SymjaNode> CONVERTERS;
    static {
        CONVERTERS = new FunctionClassMap<>();
        CONVERTERS.put(IntegerNode.class, SymjaAst::integerConverter);
        CONVERTERS.put(SymbolNode.class, SymjaAst::symbolConverter);
        CONVERTERS.put(FunctionNode.class, SymjaAst::functionConverter);
        CONVERTERS.put(ASTNode.class, SymjaAst::defaultConverter);
    }

    /**
     * Converts an IntegerNode to a SymjaInteger node.
     *
     * @param node the integer node
     * @return the corresponding SymjaInteger node
     */
    private static SymjaInteger integerConverter(IntegerNode node) {
        var symbol = SymjaNode.newNode(SymjaInteger.class);
        symbol.set(SymjaInteger.VALUE_STRING, node.toString());
        return symbol;
    }

    /**
     * Converts a SymbolNode to a SymjaSymbol node.
     *
     * @param node the symbol node
     * @return the corresponding SymjaSymbol node
     */
    private static SymjaNode symbolConverter(SymbolNode node) {
        var symbol = SymjaNode.newNode(SymjaSymbol.class);
        symbol.set(SymjaSymbol.SYMBOL, node.getString());
        return symbol;
    }

    /**
     * Converts a SymbolNode to a SymjaOperator node.
     *
     * @param node the symbol node
     * @return the corresponding SymjaOperator node
     */
    private static SymjaNode operatorConverter(SymbolNode node) {
        var symbol = SymjaNode.newNode(SymjaOperator.class);
        var operator = Operator.fromSymjaSymbol(node.getString());
        symbol.set(SymjaOperator.OPERATOR, operator);
        return symbol;
    }

    /**
     * Converts a FunctionNode to a SymjaFunction node.
     *
     * @param node the function node
     * @return the corresponding SymjaFunction node
     */
    private static SymjaNode functionConverter(FunctionNode node) {
        var children = new ArrayList<SymjaNode>();
        for (int i = 0; i < node.size(); i++) {
            if (i == 0) {
                SpecsCheck.checkClass(node.get(i), SymbolNode.class);
                children.add(operatorConverter((SymbolNode) node.get(i)));
            } else {
                children.add(CONVERTERS.apply(node.get(i)));
            }
        }
        var function = SymjaNode.newNode(SymjaFunction.class, children);
        return function;
    }

    /**
     * Default converter for ASTNode types not explicitly handled.
     *
     * @param node the AST node
     * @return a generic SymjaNode
     */
    private static SymjaNode defaultConverter(ASTNode node) {
        System.out.println("NOT IMPLEMENTED: " + node.getClass());
        return SymjaNode.newNode(SymjaNode.class);
    }

    /**
     * Parses a Symja expression into a SymjaNode.
     *
     * @param symjaExpression the Symja expression
     * @return the root SymjaNode
     */
    public static SymjaNode parse(String symjaExpression) {
        var p = new Parser();
        var root = p.parse(symjaExpression);
        return toNode(root);
    }

    /**
     * Converts an ASTNode to a SymjaNode.
     *
     * @param astNode the AST node
     * @return the corresponding SymjaNode
     */
    public static SymjaNode toNode(ASTNode astNode) {
        return CONVERTERS.apply(astNode);
    }
}
