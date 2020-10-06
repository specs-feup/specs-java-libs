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

package pt.up.fe.specs.util.treenode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import pt.up.fe.specs.util.Preconditions;

/**
 * @author Joao Bispo
 *
 */
public abstract class ATreeNode<K extends ATreeNode<K>> implements TreeNode<K> {

    private K parent;
    private List<K> children;

    public ATreeNode(Collection<? extends K> children) {
        this.children = new ArrayList<>();

        // Safety if given list is null
        if (children == null) {
            // This list is immutable, this means that we will not be able to add children to this node
            // Do we want to keep it like this?
            children = Collections.emptyList();
        }

        // Add children
        for (K child : children) {
            Preconditions.checkNotNull(child, "Cannot use 'null' as children.");
            addChild(child);
        }

        this.parent = null;
    }

    @Override
    public abstract K getThis();

    @Override
    public K getParent() {
        return this.parent;
    }

    @Override
    public void removeParent() {
        if (this.parent == null) {
            throw new RuntimeException("Should have a parent.");
        }
        this.parent = null;
    }

    @Override
    public void setParent(K parent) {
        this.parent = parent;
    }

    @Override
    public List<K> getChildren() {
        return this.children;
    }

    /**
     * Returns a new copy of the node with the same content and type, but not children.
     *
     * @return
     */
    protected abstract K copyPrivate();

    /**
     * 
     */
    @Override
    public K copyShallow() {
        return copyPrivate();
    }

    /**
     * Prints the tree.
     */
    @Override
    public String toString() {
        return TreeNodeUtils.toString(getThis(), "");
    }

    /**
     * 
     * @param aClass
     * @return
     */
    public List<Integer> indexesOf(Class<? extends K> aClass) {
        return TreeNodeIndexUtils.indexesOf(getChildren(), aClass);
    }

}
