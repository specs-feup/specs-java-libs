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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.treenode.transform;

import pt.up.fe.specs.util.treenode.TreeNode;
import pt.up.fe.specs.util.treenode.transform.util.TraversalStrategy;

/**
 * Applies a transformation rule over a TreeToken instance.
 * 
 * 
 * @author Joao Bispo
 * 
 */
public interface TransformRule<K extends TreeNode<K>, T extends TransformResult> {

    /**
     * 
     * Applies a transformation over a TreeNode instance.
     * 
     * <p>
     * IMPORTANT: The tree itself should not be modified inside this method, instead
     * the method must queue the changes using methods from the 'queue' object.
     * 
     * @param node
     * @param queue
     * 
     */
    T apply(K node, TransformQueue<K> queue);

    TraversalStrategy getTraversalStrategy();

    default void visit(K node) {
        getTraversalStrategy().apply(node, this);
    }

}
