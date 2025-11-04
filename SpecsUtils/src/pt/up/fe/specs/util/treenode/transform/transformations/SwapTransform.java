/**
 * Copyright 2017 SPeCS.
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

package pt.up.fe.specs.util.treenode.transform.transformations;

import pt.up.fe.specs.util.treenode.NodeInsertUtils;
import pt.up.fe.specs.util.treenode.TreeNode;
import pt.up.fe.specs.util.treenode.transform.TwoOperandTransform;

public class SwapTransform<K extends TreeNode<K>> extends TwoOperandTransform<K> {

    private final boolean swapSubtrees;

    /**
     * Swaps the positions of node1 and node2.
     * 
     * <p>
     * If 'swapSubtrees' is enabled, this transformation is not allowed if any of
     * the nodes is a part of the subtree of the other.
     *
     */
    public SwapTransform(K node1, K node2, boolean swapSubtrees) {
        super("swap", node1, node2);

        this.swapSubtrees = swapSubtrees;
    }

    @Override
    public void execute() {
        NodeInsertUtils.swap(getNode1(), getNode2(), swapSubtrees);
    }

}
