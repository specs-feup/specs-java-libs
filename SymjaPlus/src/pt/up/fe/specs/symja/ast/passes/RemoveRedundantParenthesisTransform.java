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

import pt.up.fe.specs.symja.ast.SymjaFunction;
import pt.up.fe.specs.symja.ast.SymjaNode;
import pt.up.fe.specs.symja.ast.SymjaOperator;
import pt.up.fe.specs.symja.ast.VisitAllTransform;
import pt.up.fe.specs.util.treenode.transform.TransformQueue;
import pt.up.fe.specs.util.treenode.transform.util.TraversalStrategy;

/**
 * Transform that removes redundant parenthesis from Symja AST function nodes.
 */
public class RemoveRedundantParenthesisTransform implements VisitAllTransform {

    /**
     * Applies the transform to all children of the given node, removing redundant parenthesis where appropriate.
     *
     * @param node the node to transform
     * @param queue the transform queue
     */
    @Override
    public void applyAll(SymjaNode node, TransformQueue<SymjaNode> queue) {
        if (!(node instanceof SymjaFunction)) {
            return;
        }
        if (!node.hasParent()) {
            node.set(SymjaFunction.HAS_PARENTHESIS, false);
            return;
        }
        var parent = node.getParent();
        if (!(parent instanceof SymjaFunction)) {
            return;
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
        return;
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
