/**
 * Copyright 2012 SPeCS Research Group.
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

package pt.up.fe.specs.util.treenode.transform.util;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.treenode.TreeNode;
import pt.up.fe.specs.util.treenode.transform.TransformQueue;
import pt.up.fe.specs.util.treenode.transform.TransformResult;
import pt.up.fe.specs.util.treenode.transform.TransformRule;

/**
 * @author Joao Bispo
 * 
 */
public enum TraversalStrategy {

    /**
     * Top-down traversal.
     */
    PRE_ORDER,
    /**
     * Bottom-up traversal.
     */
    POST_ORDER;

    public <K extends TreeNode<K>, T extends TransformResult> void apply(K node, TransformRule<K, T> rule) {
        getTransformations(node, rule).apply();
    }

    public <K extends TreeNode<K>, T extends TransformResult> TransformQueue<K> getTransformations(K node,
            TransformRule<K, T> rule) {

        // Create instruction queue
        TransformQueue<K> queue = new TransformQueue<>(rule.getClass().getSimpleName());

        // Traverse tree and collect transformations
        traverseTree(node, rule, queue);

        return queue;
    }

    private <K extends TreeNode<K>, T extends TransformResult> void traverseTree(K node, TransformRule<K, T> rule,
            TransformQueue<K> queue) {
        switch (this) {
            case POST_ORDER:
                bottomUpTraversal(node, rule, queue);
                return;
            case PRE_ORDER:
                topDownTraversal(node, rule, queue);
                return;
            default:
                SpecsLogs.warn("Case not defined:" + this);
                return;
        }
    }

    /**
     * Apply the rule to the given token and all children in the token tree, bottom
     * up.
     *
     */
    private <K extends TreeNode<K>, T extends TransformResult> void bottomUpTraversal(K node, TransformRule<K, T> rule,
            TransformQueue<K> queue) {

        if (node.hasChildren()) {
            for (K child : node.getChildren()) {
                bottomUpTraversal(child, rule, queue);
            }
        }

        rule.apply(node, queue);
    }

    /**
     * Apply the rule to the given token and all children in the token tree, top
     * down.
     *
     */
    private <K extends TreeNode<K>, T extends TransformResult> void topDownTraversal(K node, TransformRule<K, T> rule,
            TransformQueue<K> queue) {

        T result = rule.apply(node, queue);

        // Check if children should be visited
        if (!result.visitChildren()) {
            return;
        }

        if (node.hasChildren()) {
            for (K child : node.getChildren()) {
                topDownTraversal(child, rule, queue);
            }
        }

    }
}
