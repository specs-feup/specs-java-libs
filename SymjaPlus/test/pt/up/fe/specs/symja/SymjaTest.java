/**
 * Copyright 2013 SPeCS Research Group.
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

package pt.up.fe.specs.symja;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import pt.up.fe.specs.symja.ast.SymjaAst;
import pt.up.fe.specs.symja.ast.SymjaToC;
import pt.up.fe.specs.symja.ast.passes.RemoveMinusMultTransform;
import pt.up.fe.specs.symja.ast.passes.RemoveRedundantParenthesisTransform;
import pt.up.fe.specs.symja.ast.passes.ReplaceUnaryMinusTransform;

/**
 * @author Joao Bispo
 *
 */
public class SymjaTest {

    @Test
    public void test() {
        String expression = "N*M*i - (N*M*(i-1)+1) + 1";
        Map<String, String> constants = new HashMap<>();
        Integer N = 8, M = 16;
        Integer result = N * M;

        constants.put("N", N.toString());
        constants.put("M", M.toString());

        String output = SymjaPlusUtils.simplify(expression, constants);
        assertEquals(result.toString(), output);

        output = SymjaPlusUtils.simplify("a + halfSize - (a - halfSize) + 1", new HashMap<String, String>());
        // System.out.println("Second output:" + output);
        assertEquals("1+2*halfsize", output);

    }

    @Test
    public void parser() {

        // var symjaNode = SymjaAst.parse("N*M*i - (N*M*(i-1)+1) + 1");
        // var symjaNode = SymjaAst.parse("N^2");
        // var symjaNode = SymjaAst.parse("1 + Plus");
        var symjaNode = SymjaAst.parse("(N*M*(i-N))");

        System.out.println(symjaNode.toString());

        System.out.println("C Code: " + SymjaToC.convert(symjaNode));

        new RemoveMinusMultTransform().visit(symjaNode);

        System.out.println("C Code After Remove Minus One transform: " + SymjaToC.convert(symjaNode));

        new ReplaceUnaryMinusTransform().visit(symjaNode);

        System.out.println("C Code After Replace Unary Minus: " + SymjaToC.convert(symjaNode));

        new RemoveRedundantParenthesisTransform().visit(symjaNode);

        System.out.println("C Code After parenthesis: " + SymjaToC.convert(symjaNode));

        // var p = new Parser();
        // var root = (FunctionNode) p.parse("N*M*i - (N*M*(i-1)+1) + 1");
        //
        // for (var child : root) {
        // System.out.println("NODE: " + child.getClass());
        // if (child instanceof SymbolNode) {
        // var symbol = (SymbolNode) child;
        // System.out.println("SYMBOL: " + symbol.getString());
        // continue;
        // }
        //
        // if (child instanceof IntegerNode) {
        // var symbol = (IntegerNode) child;
        // System.out.println("INTEGER: " + symbol.toString());
        // continue;
        // }
        // }
    }

    @Test
    public void adiExpressions() {
        eval("((((5 + 1 + 1) * ((n - 1) - 1) + (1 + 1) * ((n - 2) - 1 + 1) + 3 + 1) *"
                + "              ((n - 1) - 1) +"
                + "          ((5 + 1 + 1) * ((n - 1) - 1) + (1 + 1) * ((n - 2) - 1 + 1) + 3 + 1) *"
                + "              ((n - 1) - 1) +"
                + "          1) *"
                + "         ((tsteps)-1 + 1)) *"
                + "        (1)");

        eval("((((2) * ((n - 1) - 1)) * ((n - 1) - 1) +\r\n"
                + "                ((2) * ((n - 1) - 1)) * ((n - 1) - 1)) *\r\n"
                + "                   ((tsteps)-1 + 1) +\r\n"
                + "               7) *\r\n"
                + "              (1)");

        eval("((((4 + 2) * ((n - 1) - 1) + (1) * ((n - 2) - 1 + 1)) * ((n - 1) - 1) +\r\n"
                + "          ((4 + 2) * ((n - 1) - 1) + (1) * ((n - 2) - 1 + 1)) * ((n - 1) - 1)) *\r\n"
                + "             ((tsteps)-1 + 1) +\r\n"
                + "         2) *\r\n"
                + "        (1)");

        eval("((((7) * ((n - 1) - 1) + (1) * ((n - 2) - 1 + 1)) * ((n - 1) - 1) +\r\n"
                + "          ((7) * ((n - 1) - 1) + (1) * ((n - 2) - 1 + 1)) * ((n - 1) - 1)) *\r\n"
                + "             ((tsteps)-1 + 1) +\r\n"
                + "         4) *\r\n"
                + "        (1)");
    }

    private void eval(String expression) {
        var simplified1 = SymjaPlusUtils
                .simplify(expression);

        var simplified1C = SymjaPlusUtils.convertToC(simplified1);
        System.out.println("Symja: " + simplified1);
        System.out.println("C: " + simplified1C);
    }
}
