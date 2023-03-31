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

import static pt.up.fe.specs.util.SpecsCollections.cast;
import static pt.up.fe.specs.util.SpecsCollections.subList;

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
import pt.up.fe.specs.util.SpecsLogs;

public interface TreeNode<K extends TreeNode<K>> {

    /**
     *
     * @return a mutable view of the children
     */
    // List<K> getChildrenMutable();

    /**
     * 
     * @return
     */
    default Iterator<K> iterator() {
        return getChildren().iterator();
    }

    default boolean hasChildren() {
        if (getChildren().isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Prints the node.
     *
     * @return
     */
    default String toNodeString() {
        String prefix = getNodeName();
        // String prefix = getType().toString();
        String content = toContentString();
        if (content.isEmpty()) {
            return prefix;
        }

        return prefix + ": " + content;
    }

    /**
     * Returns the child token at the specified position.
     *
     * @param index
     * @return
     */
    default K getChild(int index) {
        if (!hasChildren()) {
            SpecsLogs.warn("Tried to get child with index '" + index + "', but children size is " + getNumChildren());
            return null;
        }

        return getChildren().get(index);
    }

    default Stream<K> getChildrenStream() {
        return getChildren().stream();
    }

    default Stream<K> getDescendantsStream() {
        return getChildrenStream().flatMap(c -> c.getDescendantsAndSelfStream());
    }

    @SuppressWarnings("unchecked")
    default Stream<K> getDescendantsAndSelfStream() {
        return Stream.concat(Stream.of((K) this), getDescendantsStream());
    }

    default Stream<K> getAscendantsStream() {
        return getAscendantsAndSelfStream().skip(1);
    }

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
     * TODO: Rename to getChildren when current getChildren gets renamed.
     *
     * @param targetType
     * @return
     */
    default <N extends K> List<N> getChildrenV2(Class<N> targetType) {
        return getChildrenStream().filter(node -> targetType.isInstance(node))
                .map(node -> targetType.cast(node))
                .collect(Collectors.toList());
    }

    default <N extends K> List<N> getDescendantsAndSelf(Class<N> targetType) {
        return getDescendantsAndSelfStream().filter(node -> targetType.isInstance(node))
                .map(node -> targetType.cast(node))
                .collect(Collectors.toList());
    }

    default <N extends K> Optional<N> getFirstDescendantsAndSelf(Class<N> targetType) {
        return getDescendantsAndSelfStream().filter(node -> targetType.isInstance(node))
                .map(node -> targetType.cast(node))
                .findFirst();
    }

    default <N extends K> List<N> getAscendantsAndSelf(Class<N> targetType) {
        return getAscendantsAndSelfStream().filter(targetType::isInstance)
                .map(targetType::cast)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param index1
     * @param index2
     * @param indexes
     * @return the child after travelling the given indexes
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
     * Returns an unmodifiable view of the children of the token.
     *
     * <p>
     * To modify the children of the token use methods such as addChild() or removeChild().
     *
     * @return the children
     */
    List<K> getChildren();

    /**
     * TODO: Rename to castChildren.
     *
     * @param aClass
     * @return
     */
    default <T extends K> List<T> getChildren(Class<T> aClass) {
        return getChildren().stream()
                .map(aClass::cast)
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

    default <T extends K> List<T> getChildren(Class<T> aClass, int startIndex) {
        return cast(subList(getChildren(), startIndex), aClass);
    }

    // Object getContent();

    /**
     *
     * @return a string representing the contents of the node
     */
    String toContentString();

    /**
     * If getContent() returns null, this method returns an empty string.
     *
     * @return
     */
    // default String toContentString() {
    // Object content = getContent();
    // if (content == null) {
    // return "";
    // }
    //
    // return getContent().toString();
    // }

    /**
     * @param children
     *            the children to set
     */
    void setChildren(Collection<? extends K> children);

    /**
     *
     * @return the number of children in the node
     */
    default int getNumChildren() {
        if (!hasChildren()) {
            return 0;
        }

        return getChildren().size();
    }

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
    K removeChild(int index);

    default void removeChildren() {
        while (hasChildren()) {
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

        SpecsLogs.warn("Could not find child '" + child.toContentString() + "'");
        return -1;
    }

    /**
     * Replaces the token at the specified position in this list with the specified token.
     *
     * @param index
     * @param token
     */
    K setChild(int index, K token);

    /**
     *
     * @param child
     * @return the object that was really inserted in the tree (e.g., if child already had a parent, usually a copy is
     *         inserted)
     */
    // boolean addChild(K child);
    K addChild(K child);

    /**
     *
     *
     * @param index
     * @param child
     * @return
     */
    K addChild(int index, K child);

    // default <EK extends K> boolean addChildren(List<EK> children) {
    default <EK extends K> void addChildren(List<EK> children) {
        // boolean changed = false;
        for (EK child : children) {
            addChild(child);
            // changed = true;
        }

        // return changed;
    }

    /**
     * Returns a deep copy of the current token.
     *
     * TODO: This should be abstract; Remove return empty instance
     *
     * @return
     */
    K copy();

    /**
     * Returns a new copy of the node with the same content and type, but not children.
     *
     * @return
     */
    K copyShallow();

    /**
     * @return the first ancestor of the given type
     */
    public K getParent();

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
    public K getRoot();

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
                + "' (children: " + getNumChildren() + "):\n" + this);

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
     * By default, returns the name of the class.
     *
     * @return
     */
    default String getNodeName() {
        return getClass().getSimpleName();
    }

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
     * Unsets the parent of this node.
     */
    public void removeParent();

    /**
     * Detaches this node from the parent. If this node does not have a parent, throws an exception.
     */
    public void detach();

    /**
     * Sets this node as the parent of the given node. If the given node already has a parent, throws an exception.
     *
     * @param childToken
     */
    void setAsParentOf(K childToken);

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
     * @return the depth of this node (e.g., 0 if it has no parent, 1 if it is a child of the root node)
     */
    default int getDepth() {
        if (!hasParent()) {
            return 0;
        }

        return 1 + getParent().getDepth();
    }

    /**
     * 
     * @param child
     *            the child left of which the sibling will be inserted
     * @param sibling
     *            the node to be inserted
     */
    default public void addChildLeftOf(K child, K sibling) {
        var idx = indexOfChild(child);
        addChild(idx, sibling);
    }

    default public void addChildRightOf(K child, K sibling) {
        var idx = indexOfChild(child);
        addChild(idx + 1, sibling);
    }

    default public void replaceChild(K oldChild, K newChild) {
        NodeInsertUtils.replace(oldChild, newChild);
    }

}