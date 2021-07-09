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

import java.util.ArrayList;

import org.matheclipse.parser.client.Parser;
import org.matheclipse.parser.client.ast.ASTNode;
import org.matheclipse.parser.client.ast.FunctionNode;
import org.matheclipse.parser.client.ast.IntegerNode;
import org.matheclipse.parser.client.ast.SymbolNode;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

public class SymjaAst {

    private static final FunctionClassMap<ASTNode, SymjaNode> CONVERTERS;
    static {
        CONVERTERS = new FunctionClassMap<>();
        CONVERTERS.put(IntegerNode.class, SymjaAst::integerConverter);
        CONVERTERS.put(SymbolNode.class, SymjaAst::symbolConverter);
        CONVERTERS.put(FunctionNode.class, SymjaAst::functionConverter);
        CONVERTERS.put(ASTNode.class, SymjaAst::defaultConverter);
    }

    private static SymjaInteger integerConverter(IntegerNode node) {
        var symbol = SymjaNode.newNode(SymjaInteger.class);
        // System.out.println("IS SIGN: " + node.isSign());
        // System.out.println("INTEGER VALUE : " + node.getIntValue());
        // System.out.println("DOUBLE VALUE : " + node.doubleValue());
        // System.out.println("VALUE STRING: " + node.getString());
        // System.out.println("NUMBER FORMAT: " + node.getNumberFormat());
        // System.out.println("TO STRING: " + node.toString());
        // symbol.set(SymjaInteger.VALUE_STRING, node.getString());
        symbol.set(SymjaInteger.VALUE_STRING, node.toString());

        return symbol;
    }

    private static SymjaNode symbolConverter(SymbolNode node) {
        var symbol = SymjaNode.newNode(SymjaSymbol.class);

        symbol.set(SymjaSymbol.SYMBOL, node.getString());

        return symbol;
    }

    private static SymjaNode operatorConverter(SymbolNode node) {
        var symbol = SymjaNode.newNode(SymjaOperator.class);

        var operator = Operator.fromSymjaSymbol(node.getString());
        symbol.set(SymjaOperator.OPERATOR, operator);

        return symbol;
    }

    private static SymjaNode functionConverter(FunctionNode node) {
        var children = new ArrayList<SymjaNode>();
        // var firstChild = node.get(0);

        // Up until now we only saw symbols
        // SpecsCheck.checkClass(firstChild, SymbolNode.class);

        // children.add(operatorConverter((SymbolNode) firstChild));

        for (int i = 0; i < node.size(); i++) {
            if (i == 0) {
                SpecsCheck.checkClass(node.get(i), SymbolNode.class);
                children.add(operatorConverter((SymbolNode) node.get(i)));
            } else {
                children.add(CONVERTERS.apply(node.get(i)));
            }
        }
        // for (var child : node.subList(1, node.size())) {
        // children.add(CONVERTERS.apply(child));
        // }

        var function = SymjaNode.newNode(SymjaFunction.class, children);

        return function;
    }

    private static SymjaNode defaultConverter(ASTNode node) {
        System.out.println("NOT IMPLEMENTED: " + node.getClass());
        return SymjaNode.newNode(SymjaNode.class);
        // return new SymjaNode(null, null);
    }

    public static SymjaNode parse(String symjaExpression) {
        var p = new Parser();

        var root = p.parse(symjaExpression);

        return toNode(root);
        /*
        for (var child : root) {
            System.out.println("NODE: " + child.getClass());
            if (child instanceof SymbolNode) {
                var symbol = (SymbolNode) child;
                System.out.println("SYMBOL: " + symbol.getString());
                continue;
            }
        
            if (child instanceof IntegerNode) {
                var symbol = (IntegerNode) child;
                System.out.println("INTEGER: " + symbol.toString());
                continue;
            }
        }
        */
    }

    public static SymjaNode toNode(ASTNode astNode) {
        return CONVERTERS.apply(astNode);
    }
}
