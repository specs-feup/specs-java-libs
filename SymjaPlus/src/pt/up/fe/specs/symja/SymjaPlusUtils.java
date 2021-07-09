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

import org.matheclipse.core.eval.EvalUtilities;
import org.matheclipse.core.expression.F;
import org.matheclipse.core.form.output.OutputFormFactory;
import org.matheclipse.core.interfaces.IExpr;

import edu.jas.kern.ComputerThreads;
import pt.up.fe.specs.symja.ast.SymjaAst;
import pt.up.fe.specs.symja.ast.SymjaToC;
import pt.up.fe.specs.symja.ast.passes.RemoveMinusMultTransform;
import pt.up.fe.specs.symja.ast.passes.RemoveRedundantParenthesisTransform;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author Joao Bispo
 * 
 */
public class SymjaPlusUtils {

    public static String simplify(String expression) {
        return simplify(expression, new HashMap<>());
    }

    public static String simplify(String expression, Map<String, String> constants) {

        assert constants != null;

        // Static initialization of the MathEclipse engine
        F.initSymbols();

        EvalUtilities util = new EvalUtilities();

        // Initialize constants
        try {

            for (String constantName : constants.keySet()) {
                String constantValue = constants.get(constantName);

                String expr = constantName + " = " + constantValue;
                util.evaluate(expr);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            // Call terminate() only one time at the end of the program
            ComputerThreads.terminate();
        }

        // Calculate simplified expression
        String output = null;
        try {
            StringBuilder buf = new StringBuilder();
            IExpr result = util.evaluate("ExpandAll[" + expression + "]");

            OutputFormFactory outputFormat = OutputFormFactory.get();
            outputFormat.convert(buf, result);
            output = buf.toString();

        } catch (final Exception e) {
            SpecsLogs.msgLib("Exception in Symja:" + e.getMessage());
        } finally {
            // Call terminate() only one time at the end of the program
            ComputerThreads.terminate();
        }

        // Clear constants
        try {

            for (String constantName : constants.keySet()) {
                String expr = "ClearAll[" + constantName + "]";
                util.evaluate(expr);
            }

        } catch (final Exception e) {
            e.printStackTrace();
        } finally {
            // Call terminate() only one time at the end of the program
            ComputerThreads.terminate();
        }

        return output;
    }

    public static String convertToC(String expression) {
        // Convert to Symja AST
        var symjaNode = SymjaAst.parse(expression);

        // Apply transformations
        new RemoveRedundantParenthesisTransform().visit(symjaNode);
        new RemoveMinusMultTransform().visit(symjaNode);

        // Convert to C
        return SymjaToC.convert(symjaNode);
    }
}
