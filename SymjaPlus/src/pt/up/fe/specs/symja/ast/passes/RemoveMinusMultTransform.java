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

package pt.up.fe.specs.symja.ast.passes;

import java.util.Arrays;

import pt.up.fe.specs.symja.ast.Operator;
import pt.up.fe.specs.symja.ast.SymjaFunction;
import pt.up.fe.specs.symja.ast.SymjaInteger;
import pt.up.fe.specs.symja.ast.SymjaNode;
import pt.up.fe.specs.symja.ast.SymjaOperator;
import pt.up.fe.specs.symja.ast.SymjaSymbol;
import pt.up.fe.specs.symja.ast.SymjaToC;
import pt.up.fe.specs.symja.ast.VisitAllTransform;
import pt.up.fe.specs.util.treenode.transform.TransformQueue;
import pt.up.fe.specs.util.treenode.transform.util.TraversalStrategy;

/**
 * Transform that replaces multiplication by -1 with a unary minus in the Symja AST.
 */
public class RemoveMinusMultTransform implements VisitAllTransform {

    /**
     * Applies the transform to all children of the given node, replacing multiplication by -1 where appropriate.
     *
     * @param node the node to transform
     * @param queue the transform queue
     */
    @Override
    public void applyAll(SymjaNode node, TransformQueue<SymjaNode> queue) {
        if (!(node instanceof SymjaFunction)) {
            return;
        }
        
        // Check if node has sufficient children
        if (node.getNumChildren() < 3) {
            return;
        }
        
        var operator = node.getChild(SymjaOperator.class, 0);
        var symbol = operator.get(SymjaOperator.OPERATOR);
        if (symbol != Operator.Times) {
            return;
        }
        var leftOperand = node.getChild(1);
        if (!SymjaToC.convert(leftOperand).equals("-1")) {
            return;
        }
        var rightOperand = node.getChild(2);
        if (rightOperand instanceof SymjaInteger) {
            var newInteger = SymjaNode.newNode(SymjaInteger.class);
            newInteger.set(SymjaInteger.VALUE_STRING, rightOperand.get(SymjaInteger.VALUE_STRING));
            var unaryMinus = SymjaNode.newNode(SymjaOperator.class);
            unaryMinus.set(SymjaOperator.OPERATOR, Operator.UnaryMinus);
            var newFunction = SymjaNode.newNode(SymjaFunction.class, Arrays.asList(unaryMinus, newInteger));
            queue.replace(node, newFunction);
            return;
        }
        if (rightOperand instanceof SymjaSymbol) {
            var newSymbol = SymjaNode.newNode(SymjaSymbol.class);
            newSymbol.set(SymjaSymbol.SYMBOL, rightOperand.get(SymjaSymbol.SYMBOL));
            var unaryMinus = SymjaNode.newNode(SymjaOperator.class);
            unaryMinus.set(SymjaOperator.OPERATOR, Operator.UnaryMinus);
            var newFunction = SymjaNode.newNode(SymjaFunction.class, Arrays.asList(unaryMinus, newSymbol));
            queue.replace(node, newFunction);
            return;
        }
    }

    /**
     * Returns the traversal strategy for this transform.
     *
     * @return the traversal strategy
     */
    @Override
    public TraversalStrategy getTraversalStrategy() {
        return TraversalStrategy.POST_ORDER;
    }
}
