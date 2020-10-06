/**
 * Copyright 2014 SPeCS Research Group.
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

import static pt.up.fe.specs.util.SpecsCollections.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsLogs;

public interface TreeNode<K extends TreeNode<K>> {

    // Methods implemented by {@link ATreeNode} ///////////////////////////////

    /**
     * Returns a reference to the object that implements this class.
     *
     * <p>
     * This method is needed because of generics not having information about K.
     *
     * @return
     */
    public K getThis();

    /**
     * 
     * @return
     */
    public K getParent();

    /**
     * Unsets the parent of this node.
     * 
     * @return
     */
    public void removeParent();

    /**
     * 
     * @return
     */
    public void setParent(K parent);

    /**
     * Returns an unmodifiable view of the children of the token.
     *
     * <p>
     * To modify the children of the token use methods such as addChild() or removeChild().
     *
     * @return the children
     */
    public List<K> getChildren();

    // Basic Getter/Setter Methods ////////////////////////////////////////////

    /**
     * 
     * @return
     */
    default public boolean hasChildren() {
        if (getChildren().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     *
     * @param child
     * @return the object that was really inserted in the tree (e.g., if child already had a parent, usually a copy is
     *         inserted)
     */
    default public K addChild(K child) {
        K sanitizedChild = TreeNodeUtils.sanitizeNode(child);
        this.setAsParentOf(sanitizedChild);
        SpecsCheck.checkNotNull(sanitizedChild, () -> "Cannot use 'null' as children.");
        this.getChildren().add(sanitizedChild);
        return sanitizedChild;
    }

    /**
     *
     *
     * @param index
     * @param child
     * @return
     */
    default public K addChild(int idx, K child) {
        K sanitizedChild = TreeNodeUtils.sanitizeNode(child);
        this.setAsParentOf(sanitizedChild);
        SpecsCheck.checkNotNull(sanitizedChild, () -> "Cannot use 'null' as children.");
        this.getChildren().add(idx, sanitizedChild);
        return sanitizedChild;
    }

    /**
     * 
     * @param child
     * @param sibling
     * @return
     */
    default public K addChildLeftOf(K child, K sibling) {
        var idx = this.getChildren().indexOf(sibling);
        SpecsCheck.checkNotNull(sibling, () -> "Sibling not found for this parent!");
        return this.addChild(idx, child);
    }

    /**
     * 
     * @param child
     * @param sibling
     * @return
     */
    default public K addChildRightOf(K child, K sibling) {
        var idx = this.getChildren().indexOf(sibling);
        SpecsCheck.checkNotNull(sibling, () -> "Sibling not found for this parent!");
        return this.addChild(idx + 1, child);
    }

    /**
     * Returns the child token at the specified position.
     *
     * @param index
     * @return
     */
    default public K getChild(int idx) {
        if (!this.hasChildren()) {
            SpecsLogs.warn("Tried to get child with index '"
                    + idx + "', but children size is " + getNumChildren());
            return null;
        }
        return this.getChildren().get(idx);
    }

    /**
     * Replaces the token at the specified position in this list with the specified token.
     *
     * @param index
     * @param token
     */
    default public K setChild(int index, K token) {
        if (!this.hasChildren()) {
            throw new RuntimeException("Token does not have children, cannot set a child.");
        }

        K sanitizedToken = TreeNodeUtils.sanitizeNode(token);
        this.setAsParentOf(sanitizedToken);
        SpecsCheck.checkNotNull(sanitizedToken, () -> "Sanitized token is null");

        // Insert child
        K previousChild = this.getChildren().set(index, sanitizedToken);

        // Remove parent from previous child at this index
        if (previousChild != null) {
            previousChild.removeParent();
        }

        return previousChild;
    }

    /**
     * 
     * @param oldChild
     * @param newChild
     */
    default public K replaceChild(K oldChild, K newChild) {
        var idx = this.getChildren().indexOf(oldChild);
        SpecsCheck.checkNotNull(oldChild, () -> "Child not found for this parent!");
        return this.setChild(idx, newChild);
    }

    // Advanced Getter/Setter Methods /////////////////////////////////////////

    /**
     * Sets this node as the parent of the given node. If the given node already has a parent, throws an exception.
     *
     * @param childToken
     */
    default void setAsParentOf(K childToken) {
        if (childToken.getParent() != null) {
            throw new RuntimeException("Parent should be null.");
        }
        childToken.setParent(getThis());
    }

    /**
     * 
     * @param <EK>
     * @param children
     */
    default <EK extends K> void addChildren(List<EK> children) {
        // If the same list (reference) create a copy, to avoid problems when
        // adding the list to itself
        if (!children.isEmpty() && children == this.getChildren()) {
            SpecsLogs.msgWarn("Adding the list to itself");
            children = SpecsFactory.newArrayList(children);
        }

        for (K child : children) {
            addChild(child);
        }
    }

    /**
     * @param children
     *            the children to set
     */
    default void setChildren(Collection<? extends K> children) {

        // Remove previous children in this node
        for (int i = 0; i < this.getNumChildren(); i++) {
            this.removeChild(0);
        }

        // Add new children
        for (K child : children) {
            addChild(child);
        }
    }

    /*
     * TODO: this was a method with the same name in the interface, 
     * which was immediately override by the above method in the ATreeNode
     * 
    default <EK extends K> void addChildren(List<EK> children) {
        for (EK child : children) {
            addChild(child);
        }
    }*/

    /**
     * Returns the nodes on the left of this node.
     *
     * @return
     */
    default List<K> getLeftSiblings() {
        if (!hasParent()) {
            SpecsLogs.warn("Asked for left siblings of a node that does not have a parent");
            return Collections.emptyList();
        }

        List<K> parentChildren = getParent().getChildren();

        return parentChildren.subList(0, indexOfSelf());
    }

    /**
     * Returns the nodes on the right of this node.
     *
     * @return
     */
    default List<K> getRightSiblings() {
        if (!hasParent()) {
            SpecsLogs.warn("Asked for right siblings of a node that does not have a parent");
            return Collections.emptyList();
        }

        List<K> parentChildren = getParent().getChildren();

        return parentChildren.subList(indexOfSelf() + 1, parentChildren.size());
    }

    /**
     *
     * @param targetType
     * @return all descendants that are an instance of the given class
     */
    default <N extends K> List<N> getDescendants(Class<N> targetType) {
        return getDescendantsStream().filter(node -> targetType.isInstance(node))
                .map(node -> targetType.cast(node))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param targetType
     * @return list with all descendants
     */
    default List<K> getDescendants() {
        return getDescendantsStream().collect(Collectors.toList());
    }

    /**
     *
     * @param index1
     * @param index2
     * @param indexes
     * @return the child after traveling the given indexes
     */

    default K getChild(int index1, int index2, int... indexes) {
        K currentChild = getChild(index1);
        currentChild = currentChild.getChild(index2);
        for (int indexe : indexes) {
            currentChild = currentChild.getChild(indexe);
        }

        return currentChild;
    }

    /**
     *
     * @param targetType
     * @return
     */
    default <N extends K> List<N> getChildren(Class<N> targetType) {
        return getChildrenStream().filter(node -> targetType.isInstance(node))
                .map(node -> targetType.cast(node))
                .collect(Collectors.toList());
    }

    /**
     * Returns all children that are an instance of the given class.
     *
     * @param aClass
     * @return
     */
    default <T extends K> List<T> getChildrenOf(Class<T> aClass) {
        return getChildrenStream()
                .filter(child -> aClass.isInstance(child))
                .map(aClass::cast)
                .collect(Collectors.toList());
    }

    /**
     * 
     * @param <T>
     * @param aClass
     * @param startIndex
     * @return
     */
    default <T extends K> List<T> getChildren(Class<T> aClass, int startIndex) {
        return cast(subList(getChildren(), startIndex), aClass);
    }

    /**
     * 
     * @param <N>
     * @param targetType
     * @return
     */
    default <N extends K> List<N> getDescendantsAndSelf(Class<N> targetType) {
        return getDescendantsAndSelfStream().filter(node -> targetType.isInstance(node))
                .map(node -> targetType.cast(node))
                .collect(Collectors.toList());
    }

    /**
     * 
     * @param <N>
     * @param targetType
     * @return
     */
    default <N extends K> Optional<N> getFirstDescendantsAndSelf(Class<N> targetType) {
        return getDescendantsAndSelfStream().filter(node -> targetType.isInstance(node))
                .map(node -> targetType.cast(node))
                .findFirst();
    }

    /**
     * 
     * @param <N>
     * @param targetType
     * @return
     */
    default <N extends K> List<N> getAscendantsAndSelf(Class<N> targetType) {
        return getAscendantsAndSelfStream().filter(targetType::isInstance)
                .map(targetType::cast)
                .collect(Collectors.toList());
    }

    /**
     * Removes the children that are an instance of the given class.
     *
     * @param token
     * @param type
     */
    default void removeChildren(Class<? extends K> type) {

        ChildrenIterator<K> iterator = getChildrenIterator();
        while (iterator.hasNext()) {
            if (type.isInstance(iterator.next())) {
                iterator.remove();
            }
        }
    }

    // Stream Methods /////////////////////////////////////////////////////////

    /**
     * 
     * @return
     */
    default Stream<K> getChildrenStream() {
        return getChildren().stream();
    }

    /**
     * 
     * @return
     */
    default Stream<K> getDescendantsStream() {
        return getChildrenStream().flatMap(c -> c.getDescendantsAndSelfStream());
    }

    /**
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    default Stream<K> getDescendantsAndSelfStream() {
        return Stream.concat(Stream.of((K) this), getDescendantsStream());
    }

    /**
     * 
     * @return
     */
    default Stream<K> getAscendantsStream() {
        return getAscendantsAndSelfStream().skip(1);
    }

    /**
     * 
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    default Stream<K> getAscendantsAndSelfStream() {
        List ascendantsAndSelf = new ArrayList<>();

        TreeNode node = this;
        while (node != null) {
            ascendantsAndSelf.add(node);

            node = node.getParent();
        }

        return ascendantsAndSelf.stream();
    }

    // Output/Printing Methods ///////////////////////////////////////////////

    /**
     *
     * @return a string representing the contents of the node
     */
    default String toContentString() {
        return "";
    }

    /**
     * By default, returns the name of the class.
     *
     * @return
     */
    default String getNodeName() {
        return getClass().getSimpleName();
    }

    /**
     * Prints the node.
     *
     * @return
     */
    default String toNodeString() {
        String prefix = getNodeName();
        String content = toContentString();
        if (content.isEmpty()) {
            return prefix;
        }

        return prefix + ": " + content;
    }

    // Utility Methods ////////////////////////////////////////////////////////

    /**
     * 
     * @return Iterator to start of collection of children nodes
     */
    default Iterator<K> iterator() {
        return getChildren().iterator();
    }

    /**
     *
     * @param aClass
     * @return
     */
    default <T extends K> List<T> castChildren(Class<T> aClass) {
        return getChildren().stream().map(aClass::cast).collect(Collectors.toList());
    }

    /**
     * Normalizes the token according to a given bypass set. The nodes in the bypass set can have only one child.
     *
     * @param bypassSet
     * @return
     */
    default K normalize(Collection<Class<? extends K>> bypassSet) {

        // If node is in bypass set, return normalized child
        for (Class<? extends K> bypassNode : bypassSet) {
            if (bypassNode.isInstance(this)) {

                if (getNumChildren() != 1) {
                    throw new RuntimeException("Expected only one child");
                }

                return this.getChild(0).normalize(bypassSet);
            }
        }

        // Node is not in bypass set, return it
        return getThis();
    }

    /**
     * Returns a new shallow copy of the node with the same content and type, but not children.
     *
     * @return
     */
    public K copyShallow();

    /**
     * Creates a deep copy of the node, including children. No guarantees are made regarding the contents of each node,
     * they can be the same object as in the original node, and if mutable, changing the content in one node might be
     * reflected in the copy.
     * 
     * @return a deep copy of the current token.
     */
    default K copy() {
        K newToken = this.copyShallow();

        // Check new token does not have children
        if (newToken.getNumChildren() != 0) {
            throw new RuntimeException("Node '"
                    + newToken.getClass().getSimpleName() + "' of type '"
                    + newToken.getNodeName() + "' still has children after copyPrivate(), check implementation");
        }

        // Copy children of token
        for (K child : this.getChildren()) {
            K newChildToken = child.copy();
            newToken.addChild(newChildToken);
        }

        return newToken;
    }

    /////////////////////////////////////////////////////////

    /**
     * Removes the child at the specified position.
     *
     * <p>
     * Puts the parent of the child as null.
     *
     * TODO: should remove all it's children recursively?
     *
     * @param index
     * @return
     */
    default K removeChild(int index) {
        if (!hasChildren()) {
            throw new RuntimeException("Token does not have children, cannot remove a child.");
        }

        // get child and remove
        var child = this.getChildren().get(index);
        this.getChildren().remove(index);

        // Unlink child from this node
        child.removeParent();
        return child;
    }

    /**
     *
     * @return the number of children in the node
     */
    default int getNumChildren() {
        if (!this.hasChildren()) {
            return 0;
        }

        return getChildren().size();
    }

    default void removeChildren() {
        while (this.hasChildren()) {
            removeChild(0);
        }
    }

    default int removeChild(K child) {
        // Find index of child
        for (int i = 0; i < getNumChildren(); i++) {
            if (getChild(i) != child) {
                continue;
            }

            // Found child to remove
            removeChild(i);

            // Return index where it was found
            return i;
        }

        SpecsLogs.msgWarn("Could not find child '" + child.toContentString() + "'");
        return -1;
    }

    /**
     * 
     * @param <T>
     * @param type
     * @return
     */
    default <T extends K> T getAncestor(Class<T> type) {
        return getAncestorTry(type)
                .orElseThrow(() -> new RuntimeException("Could not find ancestor of type '" + type + "'"));
    }

    /**
     * Tests whether the given node is an ancestor of this node.
     *
     * @param node
     *            the node to test
     * @return true if it is ancestor, false otherwise
     */
    default boolean isAncestor(K node) {

        K currentAncestor = getParent();

        while (currentAncestor != null) {
            if (node == currentAncestor) {
                return true;
            }

            currentAncestor = currentAncestor.getParent();
        }

        return false;

    }

    default <T extends K> Optional<T> getAncestorTry(Class<T> type) {
        // If no parent, return empty
        if (!hasParent()) {
            return Optional.empty();
        }

        // Check if parent is of the given type
        K parent = getParent();

        if (type.isInstance(parent)) {
            return Optional.of(type.cast(parent));
        }

        return parent.getAncestorTry(type);
    }

    /**
     *
     * @return the uppermost parent of this node
     */
    default K getRoot() {
        K parent = getParent();
        if (parent == null) {
            return getThis();
        }

        // Recursively call the function on the parent
        return parent.getRoot();
    }

    default boolean hasParent() {
        return getParent() != null;
    }

    /**
     * @return the index of this token in its parent token, or -1 if it does not have a parent
     */
    default int indexOfSelf() {
        if (!hasParent()) {
            return -1;
        }

        return getParent().getChildren().indexOf(this);
    }

    /**
     *
     * @param nodeClass
     * @return the index of the first child that is an instance of the given class, or -1 if none is found
     */
    default int getChildIndex(Class<? extends K> nodeClass) {
        for (int i = 0; i < getNumChildren(); i++) {
            if (nodeClass.isInstance(getChild(i))) {
                return i;
            }
        }

        return -1;
    }

    default <T extends K> T getChild(Class<T> nodeClass, int index) {
        return getChildTry(nodeClass, index)
                .orElseThrow(
                        () -> new RuntimeException("Wanted a '" + nodeClass.getSimpleName() + "' at index '" + index
                                + "', but is was a '" + getChild(index).getClass().getSimpleName() + "':\n" + this));
    }

    default <T extends K> Optional<T> getChildTry(Class<T> nodeClass, int index) {
        K childNode = getChild(index);

        SpecsCheck.checkNotNull(childNode, () -> "No child at index " + index + " of node '" + getClass()
                + "' (children: " + getNumChildren() + ")");

        if (!nodeClass.isInstance(childNode)) {
            return Optional.empty();
        }

        return Optional.of(nodeClass.cast(childNode));
    }

    /*
    default boolean is(Class<? extends K> nodeClass) {
    return nodeClass.isInstance(this);
    }
    */

    /**
     * Removes the children in the given index range.
     *
     *
     * @param token
     * @param startIndex
     *            (inclusive)
     * @param endIndex
     *            (exclusive)
     */
    default void removeChildren(int startIndex, int endIndex) {

        // Java doesn't support assertions in default methods.
        // see https://bugs.openjdk.java.net/browse/JDK-8025141
        // So we use exceptions instead.
        if (endIndex < startIndex) {
            throw new IndexOutOfBoundsException();
        }
        if (getNumChildren() < endIndex) {
            throw new IndexOutOfBoundsException();
        }

        // Since we are removing children, start from the last
        // element
        for (int i = endIndex - 1; i >= startIndex; i--) {
            removeChild(i);
        }
    }

    /**
     * Sets 'newChild' in 'token' at the position 'startIndex', and removes tokens from startIndex+1 (inclusive) to
     * endIndex (exclusive).
     *
     * <p>
     * If startIndex+1 is equal to endIndex, no tokens are removed from the list.
     *
     * @param newChild
     * @param tokens
     * @param startIndex
     * @param endIndex
     */
    default void setChildAndRemove(K newChild, int startIndex, int endIndex) {

        // Set the new token at the first position
        setChild(startIndex, newChild);

        // Remove other tokens
        for (int i = endIndex - 1; i >= startIndex + 1; i--) {
            removeChild(i);
        }
    }

    /**
     *
     * @param child
     * @return the index of the given child, or -1 if no child was found
     */
    default int indexOfChild(K child) {
        int index = 0;

        ListIterator<K> iterator = getChildrenIterator();
        // Iterate until it finds the same object
        while (iterator.hasNext()) {
            // If child is the same object, return index
            if (iterator.next() == child) {
                return index;
            }

            // Otherwise, increment index and try again
            index++;
        }

        // Could not find the child
        return -1;
    }

    /**
     * Returns an Iterator of the children of the node.
     *
     * @return a ListIterator over the children of the node. The iterator supports methods that modify the node (set,
     *         remove, insert...)
     */
    default ChildrenIterator<K> getChildrenIterator() {
        return new ChildrenIterator<>(this);
    }

    /**
     * Detaches this node from the parent. If this node does not have a parent, throws an exception.
     */
    default void detach() {
        if (!this.hasParent()) {
            throw new RuntimeException("Does not have a parent");
        }

        int indexOfSelf = this.indexOfSelf();

        // Already removed from parent, just unset parent
        if (indexOfSelf == -1) {
            this.removeParent();
            return;
        }

        // Remove itself from parent
        this.getParent().removeChild(indexOfSelf);
    }

    /**
     * 
     * @return the depth of this node (e.g., 0 if it has no parent, 1 if it is a child of the root node)
     */
    default int getDepth() {
        if (!hasParent()) {
            return 0;
        }

        return 1 + getParent().getDepth();
    }

}