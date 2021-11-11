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

import java.util.HashMap;
import java.util.Map;

import org.matheclipse.core.eval.ExprEvaluator;

import pt.up.fe.specs.symja.ast.SymjaAst;
import pt.up.fe.specs.symja.ast.SymjaToC;
import pt.up.fe.specs.symja.ast.passes.RemoveMinusMultTransform;
import pt.up.fe.specs.symja.ast.passes.RemoveRedundantParenthesisTransform;
import pt.up.fe.specs.symja.ast.passes.ReplaceUnaryMinusTransform;
import pt.up.fe.specs.util.SpecsCheck;

/**
 * @author Joao Bispo
 * 
 */
public class SymjaPlusUtils {

    private static final ThreadLocal<ExprEvaluator> EVALUATOR = ThreadLocal
            .withInitial(() -> new ExprEvaluator(false, (short) 30));

    public static String simplify(String expression) {
        return simplify(expression, new HashMap<>());
    }

    private static ExprEvaluator evaluator() {
        return EVALUATOR.get();
    }

    public static String simplify(String expression, Map<String, String> constants) {

        // assert constants != null;
        SpecsCheck.checkNotNull(constants, () -> "Argument 'constants' cannot be null");

        // Clear variables
        evaluator().clearVariables();

        for (String constantName : constants.keySet()) {
            String constantValue = constants.get(constantName);

            // String expr = constantName + " = " + constantValue;

            evaluator().defineVariable(constantName, evaluator().eval(constantValue));
        }

        var output = evaluator().eval("expand(" + expression + ")").toString();

        return output;
    }

    public static String convertToC(String expression) {
        // Convert to Symja AST
        var symjaNode = SymjaAst.parse(expression);

        // Apply transformations
        new RemoveMinusMultTransform().visit(symjaNode);
        new ReplaceUnaryMinusTransform().visit(symjaNode);
        new RemoveRedundantParenthesisTransform().visit(symjaNode);

        // Convert to C
        return SymjaToC.convert(symjaNode);
    }
}
