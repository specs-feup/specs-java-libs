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

package pt.up.fe.specs.symja.ast;

import pt.up.fe.specs.util.treenode.transform.TransformQueue;
import pt.up.fe.specs.util.treenode.transform.TransformResult;
import pt.up.fe.specs.util.treenode.transform.TransformRule;

/**
 * Interface for transforms that visit all nodes in a Symja AST.
 */
public interface VisitAllTransform extends TransformRule<SymjaNode, TransformResult> {

    /**
     * Applies the transform to the given node and its children.
     *
     * @param node  the node to transform
     * @param queue the transform queue
     * @return the result of the transformation
     */
    @Override
    default TransformResult apply(SymjaNode node, TransformQueue<SymjaNode> queue) {
        applyAll(node, queue);
        return TransformResult.empty();
    }

    /**
     * Applies the transform to all children of the given node.
     *
     * @param node  the node to transform
     * @param queue the transform queue
     */
    void applyAll(SymjaNode node, TransformQueue<SymjaNode> queue);
}
