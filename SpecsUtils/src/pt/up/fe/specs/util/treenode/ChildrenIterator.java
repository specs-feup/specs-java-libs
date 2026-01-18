/**
 * Copyright 2015 SPeCS Research Group.
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

package pt.up.fe.specs.util.treenode;

import java.util.ListIterator;
import java.util.Optional;

public class ChildrenIterator<N extends TreeNode<N>> implements ListIterator<N> {

    private final TreeNode<N> parent;
    private final ListIterator<N> iterator;
    private N lastReturned;

    public ChildrenIterator(TreeNode<N> parent) {

        this.parent = parent;
        // Access internal mutable list for modification operations
        this.iterator = parent.getChildrenMutable().listIterator();

        this.lastReturned = null;
    }

    @Override
    public boolean hasNext() {
        return this.iterator.hasNext();
    }

    @Override
    public N next() {
        this.lastReturned = this.iterator.next();
        return this.lastReturned;
    }

    @Override
    public boolean hasPrevious() {
        return this.iterator.hasPrevious();
    }

    @Override
    public N previous() {
        this.lastReturned = this.iterator.previous();
        return this.lastReturned;
    }

    @Override
    public int nextIndex() {
        return this.iterator.nextIndex();
    }

    @Override
    public int previousIndex() {
        return this.iterator.previousIndex();
    }

    @Override
    public void remove() {
        // Remove from list
        this.iterator.remove();

        // Unlink child from this node
        this.lastReturned.removeParent();

        // Reset last returned
        this.lastReturned = null;
    }

    @Override
    public void set(N e) {
        // Sanitize input node
        N sanitizedToken = TreeNodeUtils.sanitizeNode(e);

        // Set parent
        parent.setAsParentOf(sanitizedToken);

        // Insert child
        this.iterator.set(sanitizedToken);

        // Remove the previous child from the tree
        this.lastReturned.removeParent();

        // Update last returned node
        this.lastReturned = sanitizedToken;
    }

    @Override
    public void add(N e) {
        // Sanitize input node
        N sanitizedToken = TreeNodeUtils.sanitizeNode(e);

        // Set parent
        parent.setAsParentOf(sanitizedToken);

        // Add node
        this.iterator.add(sanitizedToken);

        // Reset last returned
        this.lastReturned = null;
    }

    /**
     * Moves the cursor back the given amount of places.
     * 
     * <p>
     * If the given amount is bigger than the number of positions, stops when the
     * cursor is at the beginning.
     *
     */
    public N back(int amount) {
        for (int i = 0; i < amount; i++) {
            if (!hasPrevious()) {
                return this.lastReturned;
            }

            previous();
        }

        return this.lastReturned;

    }

    /**
     * 
     * @return the next node that is an instance of the given class
     */
    public <K extends N> Optional<K> next(Class<K> nodeClass) {
        while (hasNext()) {
            N node = next();
            if (nodeClass.isInstance(node)) {
                return Optional.of(nodeClass.cast(node));
            }
        }

        return Optional.empty();
    }

    /**
     * 
     * @return the next node that is NOT an instance of the given class
     */
    public <K extends N> Optional<N> nextNot(Class<K> nodeClass) {
        while (hasNext()) {
            N node = next();
            if (!nodeClass.isInstance(node)) {
                return Optional.of(node);
            }
        }

        return Optional.empty();
    }

    /**
     * Returns the next element that is in the position specified by the given
     * amount.
     * 
     * <p>
     * If amount is zero, returns the last returned node;<br>
     * If the amount is greater than one, returns the nth node of the amount.
     * next(1) is equivalent to next();<br>
     * If the amount is less than one, returns the -nth node of the amount. next(-1)
     * is equivalent to previous();<br>
     *
     */
    public N move(int amount) {
        if (amount == 0) {
            return this.lastReturned;
        }

        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                next();
            }
            return this.lastReturned;
        }

        // Less than 0
        for (int i = 0; i < Math.abs(amount); i++) {
            previous();
        }

        return this.lastReturned;

    }

    /**
     * Removes a number of previous nodes, and replaces them with the given node.
     * This call can only be made once per call to next or previous.
     * 
     * <p>
     * At the end of the method, the cursor of the iterator is before the inserted
     * node.
     *
     */
    public void replace(N node, int numberOfPreviousNodes) {
        // Delete nodes
        for (int i = 0; i < numberOfPreviousNodes - 1; i++) {
            // Delete current node
            remove();
            // Go back
            previous();
        }

        // Set new node
        set(node);
    }

    /**
     * Advances the cursor, and if it finds a statement of the given class, returns
     * it. The cursor advances event if it returns an empty optional.
     *
     *
     */
    public <K extends N> Optional<K> nextOld(Class<K> nodeClass) {

        // If iterator does not have next node, do nothing.
        if (!hasNext()) {
            return Optional.empty();
        }

        N nextNode = next();

        // If next node is not if the given class, return empty
        if (!nodeClass.isInstance(nextNode)) {
            return Optional.empty();
        }

        return Optional.of(nodeClass.cast(nextNode));
    }

}
