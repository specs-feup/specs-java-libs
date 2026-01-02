/*
 * Copyright 2013 SPeCS.
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
package org.specs.generators.java.utils;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;

/**
 * A list implementation that only allows unique elements. Extends
 * {@link ArrayList} and overrides add methods to prevent duplicates.
 *
 * @param <E> the type of elements in this list
 */
public class UniqueList<E> extends ArrayList<E> {
    /**
     * Auto-Generated serial version UID.
     */
    @Serial
    private static final long serialVersionUID = 8776711618197815102L;

    /**
     * Adds the specified element to the list if it is not already present.
     *
     * @param arg0 the element to be added
     * @return true if the element was added, false otherwise
     */
    @Override
    public boolean add(E arg0) {
        if (!contains(arg0)) {
            return super.add(arg0);
        }
        return false;
    }

    /**
     * Inserts the specified element at the specified position in the list if it is
     * not already present.
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    @Override
    public void add(int index, E element) {
        if (!contains(element)) {
            super.add(index, element);
        }
    }

    /**
     * Adds all of the elements in the specified collection to the list if they are
     * not already present.
     *
     * @param c collection containing elements to be added
     * @return true if the list changed as a result of the call
     */
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean changed = false;
        for (final E element : c) {
            changed |= add(element);
        }
        return changed;
    }

    /**
     * Inserts all of the elements in the specified collection into the list at the
     * specified position, if not already present.
     *
     * @param index index at which to insert the first element from the specified
     *              collection
     * @param c     collection containing elements to be added
     * @return true if the list changed as a result of the call
     */
    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        boolean changed = false;
        int currentIndex = index;
        for (final E element : c) {
            if (!contains(element)) {
                add(currentIndex, element);
                currentIndex++;
                changed = true;
            }
        }
        return changed;
    }

}
