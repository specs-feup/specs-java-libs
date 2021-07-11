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

public class RemoveMinusMultTransform implements VisitAllTransform {

    @Override
    public void applyAll(SymjaNode node, TransformQueue<SymjaNode> queue) {

        if (!(node instanceof SymjaFunction)) {
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
        // if (!leftOperand.toString().equals("-1")) {

        // }

        var rightOperand = node.getChild(2);

        if (rightOperand instanceof SymjaInteger) {
            var newInteger = SymjaNode.newNode(SymjaInteger.class);
            newInteger.set(SymjaInteger.VALUE_STRING, "-" + rightOperand.get(SymjaInteger.VALUE_STRING));
            queue.replace(node, newInteger);
            return;
        }

        if (rightOperand instanceof SymjaSymbol) {
            // TODO: Replace with SymjaFunction with first child unary minus and second child symbol
            var newSymbol = SymjaNode.newNode(SymjaSymbol.class);
            newSymbol.set(SymjaSymbol.SYMBOL, "-" + rightOperand.get(SymjaSymbol.SYMBOL));
            queue.replace(node, newSymbol);
            return;
        }

    }

    @Override
    public TraversalStrategy getTraversalStrategy() {
        return TraversalStrategy.POST_ORDER;
    }
}
