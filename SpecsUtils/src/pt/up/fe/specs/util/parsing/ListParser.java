/**
 * Copyright 2017 SPeCS.
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

package pt.up.fe.specs.util.parsing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.Preconditions;
import pt.up.fe.specs.util.SpecsCollections;

public class ListParser<T> {

    private List<T> currentList;

    public ListParser(List<T> list) {
        this.currentList = list;
    }

    public List<T> getList() {
        return currentList;
    }

    /**
     * 
     * 
     * @param aClass
     * @return a list with the consecutive elements of the given class, starting at the head. These elements are removed
     *         from this list
     */
    public <K extends T> List<K> pop(Class<K> aClass) {
        if (currentList.isEmpty()) {
            return Collections.emptyList();
        }

        // While head is of the specified type, increment counter and add element
        int counter = 0;
        List<K> newList = new ArrayList<>();
        while (counter < currentList.size()) {
            T currentNode = currentList.get(counter);

            // Stop if found node that is not of the expected class
            if (!aClass.isInstance(currentNode)) {
                break;
            }

            // Add node to the list
            newList.add(aClass.cast(currentNode));

            counter++;
        }

        // Update list
        currentList = currentList.subList(counter, currentList.size());

        return newList;
    }

    public <K extends T> List<K> pop(int amount, Function<T, K> mapper) {
        Preconditions.checkArgument(amount <= currentList.size(), "Tried to pop an amount of " + amount
                + " elements, but list only has " + currentList.size());

        List<K> newList = currentList.subList(0, amount).stream()
                .map(element -> mapper.apply(element))
                .collect(Collectors.toList());

        // Update list
        currentList = currentList.subList(amount, currentList.size());

        return newList;
    }

    public T popSingle() {
        Preconditions.checkArgument(!currentList.isEmpty(), "Tried to pop an element from an empty list");

        // Get head of the list
        T head = currentList.get(0);

        // Update list
        currentList = currentList.subList(1, currentList.size());

        return head;
    }

    public Optional<T> popSingleIf(Predicate<T> predicate) {

        // Get head of the list
        T head = peekSingle();

        if (predicate.test(head)) {
            return Optional.of(popSingle());
        }

        return Optional.empty();
    }

    private T peekSingle() {
        Preconditions.checkArgument(!currentList.isEmpty(), "Tried to peek an element from an empty list");

        // Get head of the list
        T head = currentList.get(0);

        return head;
    }

    public <K extends T> K popSingle(Function<T, K> mapper) {
        return pop(1, mapper).get(0);
    }

    public boolean isEmpty() {
        return currentList.isEmpty();
    }

    /**
     * Adds the given elements to the head of the list.
     * 
     * @param elements
     */
    public void add(List<T> elements) {
        currentList = SpecsCollections.concat(elements, currentList);
    }

}
