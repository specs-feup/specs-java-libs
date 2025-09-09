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
import java.util.Objects;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author Joao Bispo
 *
 */
public abstract class ATreeNode<K extends ATreeNode<K>> implements TreeNode<K> {

    private final List<K> children;
    protected K parent;

    public ATreeNode(Collection<? extends K> children) {
        this.children = initChildren(children);

        // In case given list is null
        if (children == null) {
            children = Collections.emptyList();
        }

        // Add children
        for (K child : children) {
            Objects.requireNonNull(child, () -> "Cannot use 'null' as children.");
            addChild(child);
        }

        this.parent = null;
    }

    private void addChildPrivate(K child) {
        Objects.requireNonNull(child, () -> "Cannot use 'null' as children.");
        this.children.add(child);
    }

    private void addChildPrivate(int index, K child) {
        Objects.requireNonNull(child, () -> "Cannot use 'null' as children.");
        this.children.add(index, child);
    }

    private List<K> initChildren(Collection<? extends K> children) {
        if (children == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(children.size());
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.up.fe.specs.util.treenode.TreeNode#getChildren()
     */
    @Override
    public List<K> getChildren() {
        // FIXME: Should be 'Collections.unmodifiableList(this.children);'
        // but the current implementation breaks when you do.
        return this.children;
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.up.fe.specs.util.treenode.TreeNode#setChildren(java.util.Collection)
     */
    @Override
    public void setChildren(Collection<? extends K> children) {
        // Remove previous children in this node
        int numChildren = getNumChildren();
        for (int i = 0; i < numChildren; i++) {
            this.removeChild(0);
        }

        // Add new children (handle null case)
        if (children != null) {
            for (K child : children) {
                addChild(child);
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.up.fe.specs.util.treenode.TreeNode#removeChild(int)
     */
    @Override
    public K removeChild(int index) {
        if (!hasChildren()) {
            throw new RuntimeException("Token does not have children, cannot remove a child.");
        }

        K child = this.children.remove(index);

        // Unlink child from this node
        child.removeParent();

        return child;

    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.up.fe.specs.util.treenode.TreeNode#setChild(int, K)
     */
    @Override
    public K setChild(int index, K token) {
        K sanitizedToken = TreeNodeUtils.sanitizeNode(token);
        setAsParentOf(sanitizedToken);

        if (!hasChildren()) {
            throw new RuntimeException("Token does not have children, cannot set a child.");
        }

        Objects.requireNonNull(sanitizedToken, () -> "Sanitized token is null");

        // Insert child
        K previousChild = this.children.set(index, sanitizedToken);

        // Remove the previous child from the tree
        if (previousChild != null) {
            previousChild.removeParent();
        }

        return previousChild;
    }

    @Override
    public void setAsParentOf(K childToken) {

        if (childToken.getParent() != null) {
            throw new RuntimeException("Parent should be null.");
        }

        childToken.parent = getThis();
    }

    @Override
    public void detach() {
        // Safe detach - do nothing if already detached
        if (!hasParent()) {
            return;
        }

        int indexOfSelf = indexOfSelf();

        // Already removed from parent, just unset parent
        if (indexOfSelf == -1) {
            removeParent();
            return;
        }

        // Remove itself from parent
        getParent().removeChild(indexOfSelf);
    }

    @Override
    public void removeParent() {
        if (this.parent == null) {
            throw new RuntimeException("Should have a parent.");
        }

        this.parent = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.up.fe.specs.util.treenode.TreeNode#addChild(K)
     */
    @Override
    // public boolean addChild(K child) {
    public K addChild(K child) {
        K sanitizedChild = TreeNodeUtils.sanitizeNode(child);
        setAsParentOf(sanitizedChild);

        addChildPrivate(sanitizedChild);

        return sanitizedChild;
    }

    @Override
    public <EK extends K> void addChildren(List<EK> children) {
        // If the same list (reference) create a copy, to avoid problems when
        // adding the list to itself
        if (!children.isEmpty() && children == this.children) {
            SpecsLogs.warn("Adding the list to itself");
            children = new ArrayList<>(children);
        }

        for (K child : children) {
            addChild(child);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see pt.up.fe.specs.util.treenode.TreeNode#addChild(int, K)
     */
    @Override
    public K addChild(int index, K child) {
        K sanitizedToken = TreeNodeUtils.sanitizeNode(child);
        setAsParentOf(sanitizedToken);

        // Insert child
        addChildPrivate(index, sanitizedToken);

        return sanitizedToken;
    }

    /**
     * Returns a new copy of the node with the same content and type, but not
     * children.
     *
     */
    protected abstract K copyPrivate();

    @Override
    public K copyShallow() {
        return copyPrivate();
    }

    /**
     * Creates a deep copy of the node, including children. No guarantees are made
     * regarding the contents of each node, they can be the same object as in the
     * original node, and if mutable, changing the content in one node might be
     * reflected in the copy.
     * 
     * (non-Javadoc)
     * 
     * @see pt.up.fe.specs.util.treenode.TreeNode#copy()
     */
    @Override
    public K copy() {
        K newToken = copyPrivate();
        // Check new token does not have children
        if (newToken.getNumChildren() != 0) {
            throw new RuntimeException("Node '" + newToken.getClass().getSimpleName() + "' of type '"
                    + newToken.getNodeName() + "' still has children after copyPrivate(), check implementation");
        }

        for (K child : this.getChildren()) {
            // Copy children of token
            K newChildToken = child.copy();

            newToken.addChild(newChildToken);
        }

        return newToken;
    }

    /**
     * Returns a reference to the object that implements this interface.
     *
     * <p>
     * This method is needed because of Java generics not having information about
     * K.
     *
     */
    @SuppressWarnings("unchecked")
    protected K getThis() {
        return (K) this;
    }

    /**
     *
     * @return a String with a tree-representation of this node
     */
    public String toTree() {
        return TreeNodeUtils.toString(getThis(), "");
    }

    /**
     * @return the parent of this node, or null if the node does not have a parent
     */
    @Override
    public K getParent() {
        return this.parent;
    }

    /**
     *
     * @return the uppermost parent of this node
     */
    @Override
    public K getRoot() {
        // Get parent
        K parent = getParent();

        // If it has no parents, return self
        if (parent == null) {
            return getThis();
        }

        // Recursively call the funcion on the parent
        return parent.getRoot();
    }

    /**
     * Prints the tree.
     */
    @Override
    public String toString() {
        return TreeNodeUtils.toString(getThis(), "");
    }

    /**
     * Removes the children that are an instance of the given class.
     *
     */
    public void removeChildren(Class<? extends K> type) {

        ChildrenIterator<K> iterator = getChildrenIterator();

        while (iterator.hasNext()) {

            if (type.isInstance(iterator.next())) {
                iterator.remove();
            }
        }

    }

    public List<Integer> indexesOf(Class<? extends K> aClass) {
        return TreeNodeIndexUtils.indexesOf(getChildren(), aClass);
    }

    /**
     * Normalizes the token according to a given bypass set. The nodes in the bypass
     * set can have only one child.
     *
     */
    public K normalize(Collection<Class<? extends K>> bypassSet) {

        // If node is in bypass set, return normalized child
        for (Class<? extends K> bypassNode : bypassSet) {
            if (bypassNode.isInstance(this)) {

                if (getNumChildren() != 1) {
                    throw new RuntimeException("Expected only one child");
                }

                return getChild(0).normalize(bypassSet);
            }
        }

        // Node is not in bypass set, return it
        return getThis();
    }

}
