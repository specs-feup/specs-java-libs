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
import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author Joao Bispo
 *
 */
public abstract class ATreeNode<K extends ATreeNode<K>> implements TreeNode<K> {

    private List<K> children;
    protected K parent;

    public ATreeNode(Collection<? extends K> children) {
        // this.children = SpecsFactory.newLinkedList();
        this.children = initChildren(children);

        // Safety if given list is null
        // if (children == null) {
        // // This list is immutable, this means that we will not be able to add children to this node
        // // Do we want to keep it like this?
        // children = Collections.emptyList();
        // }

        // Add children
        for (K child : children) {
            Preconditions.checkNotNull(child, "Cannot use 'null' as children.");
            addChild(child);
            // System.out.println("ADDING CHILD:" + child);
        }

        this.parent = null;

        // System.out.println("ATREE GETCHILD:" + getChildren());
    }

    private void addChildPrivate(K child) {
        addChildPrivate(-1, child);
    }

    private void addChildPrivate(int index, K child) {
        // If empty, it most likely is Collections.emptyList(), use new list
        if (!hasChildren()) {
            this.children = new ArrayList<>();
        }

        // If no index, means that will be inserted at the end
        if (index == -1) {
            index = children.size();
        }

        this.children.add(index, child);
    }

    private List<K> initChildren(Collection<? extends K> children) {
        if (children == null || children.isEmpty()) {
            return Collections.emptyList();
        }

        return new ArrayList<>();
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.util.treenode.TreeNode#getChildren()
     */
    @Override
    public List<K> getChildren() {
        return this.children;
    }

    /**
     * TODO: Remove?
     * 
     * @return a mutable view of the children
     */
    @Override
    public List<K> getChildrenMutable() {
        return this.children;
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.util.treenode.TreeNode#setChildren(java.util.Collection)
     */
    @Override
    public void setChildren(Collection<? extends K> children) {
        // Remove previous children in this node

        int numChildren = getNumChildren();
        for (int i = 0; i < numChildren; i++) {
            this.removeChild(0);
        }

        // Add new children
        for (K child : children) {
            addChild(child);
        }

    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.util.treenode.TreeNode#removeChild(int)
     */
    @Override
    public K removeChild(int index) {
        if (!hasChildren()) {
            throw new RuntimeException("Token does not have children, cannot remove a child.");
        }

        K child = this.children.remove(index);

        // If no children, replace list with reference to empty list
        if (this.children.isEmpty()) {
            this.children = Collections.emptyList();
        }

        // Unlink child from this node
        child.removeParent();

        return child;

    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.util.treenode.TreeNode#setChild(int, K)
     */
    @Override
    public K setChild(int index, K token) {
        K sanitizedToken = TreeNodeUtils.sanitizeNode(token);
        setAsParentOf(sanitizedToken);

        if (!hasChildren()) {
            throw new RuntimeException("Token does not have children, cannot set a child.");
        }

        // Insert child
        K previousChild = this.children.set(index, sanitizedToken);

        // Remove the previous child from the tree
        if (previousChild != null) {
            previousChild.removeParent();
        }

        return previousChild;
    }

    /**
     * Fails a test in matlabirtransforms when used in NodeInsertUtils, could not understand why
     */
    /* (non-Javadoc)
     * @see pt.up.fe.specs.util.treenode.TreeNode#setChild(int, K)
     */
    /*
        public K setChild(K oldChild, K newChild) {
    	K sanitizedToken = TreeNodeUtils.sanitizeNode(newChild);
    	setAsParentOf(sanitizedToken);
    
    	if (!hasChildren()) {
    	    throw new RuntimeException("Token does not have children, cannot set a child.");
    	}
    
    	ListIterator<K> iterator = getChildrenIterator();
    	// Iterate until it finds the child
    	boolean foundChild = false;
    	while (iterator.hasNext()) {
    
    	    // If not the child, continue
    	    if (iterator.next() != oldChild) {
    		continue;
    	    }
    
    	    // Found the child, replace it
    	    iterator.set(newChild);
    	    foundChild = true;
    	}
    
    	// If no child found, throw exception
    	Preconditions.checkArgument(foundChild, "Could not find given child.");
    
    	return oldChild;
        }
     */

    // protected void setAsParentOf(K childToken) {
    @Override
    public void setAsParentOf(K childToken) {

        if (childToken.getParent() != null) {
            throw new RuntimeException("Parent should be null.");
        }

        childToken.parent = getThis();
        // childToken.eventParentSet();
    }

    /**
     * This method is called when the parent of this node is set. By default does nothing.
     */
    /*
    protected void eventParentSet() {
    
    }
    */

    /**
     * This method is called when the parent of this node is unset. By default does nothing.
     */
    /*
    protected void eventParentUnset() {
    
    }
    */

    @Override
    public void detach() {
        // Check if it has a parent
        if (!hasParent()) {
            throw new RuntimeException("Does not have a parent");
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
        // eventParentUnset();
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.util.treenode.TreeNode#addChild(K)
     */
    @Override
    // public boolean addChild(K child) {
    public void addChild(K child) {
        K sanitizedChild = TreeNodeUtils.sanitizeNode(child);
        setAsParentOf(sanitizedChild);

        /*
        if (children == null) {
        children = FactoryUtils.newLinkedList();
        // children = FactoryUtils.newArrayList();
        }
         */

        // boolean changed = this.children.add(sanitizedChild);
        addChildPrivate(sanitizedChild);

        // return changed;
    }

    @Override
    // public <EK extends K> boolean addChildren(List<EK> children) {
    public <EK extends K> void addChildren(List<EK> children) {
        // If the same list (reference) create a copy, to avoid problems when
        // adding the list to itself
        if (!children.isEmpty() && children == this.children) {
            SpecsLogs.msgWarn("Adding the list to itself");
            children = SpecsFactory.newArrayList(children);
        }

        // boolean changed = false;

        for (K child : children) {
            // changed = addChild(child);
            addChild(child);
        }

        // return changed;
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.util.treenode.TreeNode#addChild(int, K)
     */
    @Override
    public void addChild(int index, K child) {
        // K sanitizedToken = sanitizeToken(child);
        K sanitizedToken = TreeNodeUtils.sanitizeNode(child);
        setAsParentOf(sanitizedToken);

        /*
        if (children == null) {
        children = FactoryUtils.newLinkedList();
        }
         */

        // Insert child
        addChildPrivate(index, sanitizedToken);
        // this.children.add(index, sanitizedToken);
    }

    /**
     * Returns a new copy of the node with the same content and type, but not children.
     *
     * @return
     */
    protected abstract K copyPrivate();

    @Override
    public K copyShallow() {
        return copyPrivate();
    }

    /**
     * Creates a deep copy of the node, including children. No guarantees are made regarding the contents of each node,
     * they can be the same object as in the original node, and if mutable, changing the content in one node might be
     * reflected in the copy.
     */
    /* (non-Javadoc)
     * @see pt.up.fe.specs.util.treenode.TreeNode#copy()
     */
    @Override
    public K copy() {
        // K newToken = newEmptyInstance();

        // newToken.setType(this.getType());
        // newToken.setContent(this.getContent());

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
     * Returns an empty instance of the token (all values can be initialized to null).
     *
     *
     *
     * @param token
     * @return
     * @deprecated Replace with copy(). However, copy must be made abstract first
     */
    // protected abstract K newEmptyInstance();

    /**
     * Returns a reference to the object that implements this class.
     *
     * <p>
     * This method is needed because of generics not having information about K.
     *
     * @return
     */
    protected abstract K getThis();

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
            // return (K) this;

            return getThis();
        }

        // Recursively call the funcion on the parent
        return parent.getRoot();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    /*
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.children == null) ? 0 : this.children.hashCode());
    
        String contentString = toContentString();
        result = prime * result + ((contentString == null) ? 0 : contentString.hashCode());
    
        return result;
    }
    */

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    /*
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ATreeNode<?> other = (ATreeNode<?>) obj;
        if (this.children == null) {
            if (other.children != null) {
                return false;
            }
        } else if (!this.children.equals(other.children)) {
            return false;
        }
    
        String contentString = toContentString();
        String otherContentString = other.toContentString();
        if (contentString == null) {
            if (otherContentString != null) {
                return false;
            }
        } else if (!contentString.equals(otherContentString)) {
            return false;
        }
    
        return true;
    }
    */

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
     * @param token
     * @param type
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
        /*
        	List<Integer> indexes = new ArrayList<>();
        
        	for (int i = 0; i < numChildren(); i++) {
        	    if (aClass.isInstance(getChild(i))) {
        		indexes.add(i);
        	    }
        	}
        
        	return indexes;
        	*/
    }

    /**
     * Normalizes the token according to a given bypass set. The nodes in the bypass set can have only one child.
     *
     * @param bypassSet
     * @return
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
