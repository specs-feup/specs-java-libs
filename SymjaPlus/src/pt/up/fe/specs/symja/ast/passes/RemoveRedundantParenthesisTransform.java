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

import pt.up.fe.specs.symja.ast.SymjaFunction;
import pt.up.fe.specs.symja.ast.SymjaNode;
import pt.up.fe.specs.symja.ast.SymjaOperator;
import pt.up.fe.specs.util.treenode.transform.TransformQueue;
import pt.up.fe.specs.util.treenode.transform.TransformResult;
import pt.up.fe.specs.util.treenode.transform.TransformRule;
import pt.up.fe.specs.util.treenode.transform.util.TraversalStrategy;

public class RemoveRedundantParenthesisTransform implements TransformRule<SymjaNode, TransformResult> {

    @Override
    public TransformResult apply(SymjaNode node, TransformQueue<SymjaNode> queue) {

        if (!(node instanceof SymjaFunction)) {
            return TransformResult.empty();
        }

        if (!node.hasParent()) {
            node.set(SymjaFunction.HAS_PARENTHESIS, false);
            return TransformResult.empty();
        }

        var parent = node.getParent();

        if (!(parent instanceof SymjaFunction)) {
            return TransformResult.empty();
        }

        var operator = (SymjaOperator) node.getChild(0);
        var parentOperator = (SymjaOperator) parent.getChild(0);

        var operatorPriority = operator.get(SymjaOperator.OPERATOR).getPriority();
        var parentPriority = parentOperator.get(SymjaOperator.OPERATOR).getPriority();

        if (operatorPriority > parentPriority) {
            node.set(SymjaFunction.HAS_PARENTHESIS, false);
        } else if (operatorPriority == parentPriority && parent.getChild(1) == node) {
            node.set(SymjaFunction.HAS_PARENTHESIS, false);
        }

        // System.out.println("OPERATOR: " + node.getChild(0));
        // System.out.println("PARENT OPERATOR: " + parent.getChild(0));
        // var operator = node.getChild(SymjaOperator.class, 0);

        return TransformResult.empty();

    }

    @Override
    public TraversalStrategy getTraversalStrategy() {
        return TraversalStrategy.POST_ORDER;
    }
}
