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

package pt.up.fe.specs.symja.ast.passes;

import java.util.Arrays;

import pt.up.fe.specs.symja.ast.Operator;
import pt.up.fe.specs.symja.ast.SymjaFunction;
import pt.up.fe.specs.symja.ast.SymjaNode;
import pt.up.fe.specs.symja.ast.SymjaOperator;
import pt.up.fe.specs.symja.ast.VisitAllTransform;
import pt.up.fe.specs.util.treenode.transform.TransformQueue;
import pt.up.fe.specs.util.treenode.transform.util.TraversalStrategy;

public class ReplaceUnaryMinusTransform implements VisitAllTransform {

    @Override
    public void applyAll(SymjaNode node, TransformQueue<SymjaNode> queue) {

        if (!(node instanceof SymjaFunction)) {
            return;
        }

        // Must be Unary Minus
        var unaryMinus = node.getChild(SymjaOperator.class, 0);
        if (unaryMinus.get(SymjaOperator.OPERATOR) != Operator.UnaryMinus) {
            return;
        }

        // Get parent
        var parent = node.getParent();

        // Must be a function
        if (!(parent instanceof SymjaFunction)) {
            return;
        }

        // Curent node must be the second operand
        if (parent.getChild(2) != node) {
            return;
        }

        var parentOperator = (SymjaOperator) parent.getChild(0);

        // Parent must be plus or minus
        var parentSymbol = parentOperator.get(SymjaOperator.OPERATOR);
        if (parentSymbol != Operator.Plus && parentSymbol != Operator.Minus) {
            return;
        }

        // If parent is + use -, otherwise use +
        var newOperatorSymbol = parentSymbol == Operator.Plus ? Operator.Minus : Operator.Plus;

        var newOperator = SymjaNode.newNode(SymjaOperator.class);
        newOperator.set(SymjaOperator.OPERATOR, newOperatorSymbol);

        // System.out.println("FIRST OPERAND: " + parent.getChild(1));
        // System.out.println("Second OPERAND: " + node.getChild(1));

        var newFunction = SymjaNode.newNode(SymjaFunction.class,
                Arrays.asList(newOperator, parent.getChild(1).copy(), node.getChild(1)));

        // System.out.println("1: " + parent.getChild(1));
        // System.out.println("CHILDREN: " + newFunction.getChildren());
        // System.out.println("NEW F: " + SymjaToC.convert(newFunction));
        queue.replace(parent, newFunction);

        // System.out.println("OPERATOR: " + node.getChild(0));
        // System.out.println("PARENT OPERATOR: " + parent.getChild(0));

    }

    @Override
    public TraversalStrategy getTraversalStrategy() {
        return TraversalStrategy.POST_ORDER;
    }
}
