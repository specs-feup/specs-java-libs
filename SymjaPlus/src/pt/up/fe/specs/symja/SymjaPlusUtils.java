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
 * specific language governing permissions and limitations under the License.
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
 * Utility class for SymjaPlus operations, including expression simplification and conversion to C code.
 *
 * @author Joao Bispo
 */
public class SymjaPlusUtils {

    /**
     * Thread-local evaluator for Symja expressions.
     */
    private static final ThreadLocal<ExprEvaluator> EVALUATOR = ThreadLocal
            .withInitial(() -> new ExprEvaluator(false, (short) 30));

    /**
     * Simplifies a mathematical expression using Symja.
     *
     * @param expression the expression to simplify
     * @return the simplified expression as a string
     */
    public static String simplify(String expression) {
        return simplify(expression, new HashMap<>());
    }

    /**
     * Returns the thread-local Symja evaluator.
     *
     * @return the evaluator instance
     */
    private static ExprEvaluator evaluator() {
        return EVALUATOR.get();
    }

    /**
     * Simplifies a mathematical expression using Symja, with support for constant definitions.
     *
     * @param expression the expression to simplify
     * @param constants a map of constant names to values
     * @return the simplified expression as a string
     * @throws NullPointerException if constants is null
     */
    public static String simplify(String expression, Map<String, String> constants) {
        SpecsCheck.checkNotNull(constants, () -> "Argument 'constants' cannot be null");
        
        // Enhanced thread safety: Clear variables before evaluation
        var evaluator = evaluator();
        evaluator.clearVariables();
        
        // Set constants in evaluator
        for (String constantName : constants.keySet()) {
            String constantValue = constants.get(constantName);
            evaluator.defineVariable(constantName, evaluator.eval(constantValue));
        }
        
        var output = evaluator.eval("expand(" + expression + ")").toString();
        
        // Clear variables again after evaluation to ensure clean state for next operation
        evaluator.clearVariables();
        
        return output;
    }

    /**
     * Converts a Symja expression to C code.
     *
     * @param expression the Symja expression
     * @return the equivalent C code as a string
     * @throws IllegalArgumentException if expression is null
     */
    public static String convertToC(String expression) throws IllegalArgumentException {
        // Validate input to prevent NullPointerException
        if (expression == null) {
            throw new IllegalArgumentException("Expression cannot be null");
        }
        
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
