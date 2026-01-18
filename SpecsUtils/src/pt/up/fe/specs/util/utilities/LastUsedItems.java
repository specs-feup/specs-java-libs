/**
 * Copyright 2015 SPeCS.
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

package pt.up.fe.specs.util.utilities;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Class to help storing a number of last used items.
 * 
 * @author JoaoBispo
 *
 * @param <T>
 */
public class LastUsedItems<T> {

    private final int capacity;
    private final Set<T> currentItemsSet;
    private final LinkedList<T> currentItemsList;

    public LastUsedItems(int capacity) {
        this.capacity = capacity;
        currentItemsSet = new HashSet<>(capacity);
        currentItemsList = new LinkedList<>();
    }

    public LastUsedItems(int capacity, List<T> items) {
        this(capacity);

        for (T item : items) {

            // Do not add more after reaching maximum capacity
            if (currentItemsList.size() == capacity) {
                break;
            }

            currentItemsList.add(item);
            currentItemsSet.add(item);
        }
    }

    /**
     * Indicates that the given item was used.
     * 
     * @return true if there were changes to the list of items
     */
    public boolean used(T item) {
        if (capacity <= 0) {
            return false;
        }

        // Check if item is already in the list
        if (currentItemsSet.contains(item)) {
            // If it is already the first one, return (use Objects.equals to allow nulls)
            if (Objects.equals(currentItemsList.getFirst(), item)) {
                return false;
            }

            // Otherwise, move item to the top
            currentItemsList.remove(item);
            currentItemsList.addFirst(item);
            return true;
        }

        // Check if there is still place to add the item to the head of the list
        if (currentItemsList.size() < capacity) {
            currentItemsList.addFirst(item);
            currentItemsSet.add(item);
            return true;
        }

        // No more space, remove last item and add item to the head of the list
        T lastElement = currentItemsList.removeLast();
        currentItemsSet.remove(lastElement);

        currentItemsList.addFirst(item);
        currentItemsSet.add(item);

        return true;
    }

    /**
     * 
     * @return the current list of items
     */
    public List<T> getItems() {
        return currentItemsList;
    }

    public Optional<T> getHead() {
        if (currentItemsList.isEmpty()) {
            return Optional.empty();
        }

        return Optional.ofNullable(currentItemsList.getFirst());
    }
}
