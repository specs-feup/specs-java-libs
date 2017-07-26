/**
 * Copyright 2016 SPeCS.
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

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.util.treenode.TreeNode;
import pt.up.fe.specs.util.treenode.transform.transformations.AddChildTransform;
import pt.up.fe.specs.util.treenode.transform.transformations.DeleteTransform;
import pt.up.fe.specs.util.treenode.transform.transformations.MoveAfterTransform;
import pt.up.fe.specs.util.treenode.transform.transformations.MoveBeforeTransform;
import pt.up.fe.specs.util.treenode.transform.transformations.ReplaceTransform;

public class TransformQueue<K extends TreeNode<K>> {

    // Id that can be used to identify where the transformations come from
    private final String id;
    private final List<NodeTransform<K>> instructions;

    public TransformQueue(String id) {
        this.id = id;
        instructions = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    /**
     * Applies the transformations in the queue, empties the queue.
     */
    public void apply() {
        for (NodeTransform<K> transform : getTransforms()) {
            transform.execute();
        }

        instructions.clear();
    }

    public List<NodeTransform<K>> getTransforms() {
        return instructions;
    }

    @Override
    public String toString() {
        return instructions.toString();
    }

    // public void replace(K originalNode, K newNode) {
    // replace(originalNode, newNode, getClass());
    // }

    /**
     * 
     * 
     * @param originalNode
     * @param newNode
     */
    public void replace(K originalNode, K newNode) {
        instructions.add(new ReplaceTransform<>(originalNode, newNode));
    }

    public void moveBefore(K originalNode, K newNode) {
        instructions.add(new MoveBeforeTransform<>(originalNode, newNode));
    }

    public void moveAfter(K originalNode, K newNode) {
        instructions.add(new MoveAfterTransform<>(originalNode, newNode));
    }

    public void delete(K node) {
        instructions.add(new DeleteTransform<>(node));
    }

    public void addChild(K originalNode, K child) {
        instructions.add(new AddChildTransform<>(originalNode, child));
    }

    public void addChildHead(K originalNode, K child) {
        instructions.add(new AddChildTransform<>(originalNode, child, 0));
    }

}
