/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.jsengine.graal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.script.Bindings;

import org.apache.commons.lang3.tuple.Pair;
import org.graalvm.polyglot.Value;

/**
 * Utility class for managing bindings in GraalVM JavaScript engine contexts.
 */
public class GraalvmBindings implements Bindings {

    private final Value bindings;

    /**
     * Constructs a GraalvmBindings instance with the given bindings.
     *
     * @param bindings the GraalVM Value representing the bindings
     */
    public GraalvmBindings(Value bindings) {
        this.bindings = bindings;
    }

    /**
     * Retrieves the underlying GraalVM Value object.
     *
     * @return the GraalVM Value object
     */
    public Value getValue() {
        return bindings;
    }

    /**
     * Returns the number of elements in the bindings.
     *
     * @return the size of the bindings
     */
    @Override
    public int size() {
        if (bindings.hasArrayElements()) {
            return (int) bindings.getArraySize();
        }

        if (bindings.hasMembers()) {
            return bindings.getMemberKeys().size();
        }

        return 0;
    }

    /**
     * Checks if the bindings are empty.
     *
     * @return true if the bindings are empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Checks if the bindings contain the specified value.
     *
     * @param value the value to check
     * @return true if the value is present, false otherwise
     */
    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    /**
     * Clears all elements in the bindings.
     */
    @Override
    public void clear() {
        int arraySize = (int) bindings.getArraySize();
        for (int i = arraySize - 1; i >= 0; i++) {
            bindings.removeArrayElement(i);
        }

        bindings.getMemberKeys().stream()
                .forEach(bindings::removeMember);
    }

    /**
     * Retrieves the set of keys in the bindings.
     *
     * @return a set of keys
     */
    @Override
    public Set<String> keySet() {
        if (bindings.hasArrayElements()) {
            Set<String> indexes = new HashSet<>();

            for (int i = 0; i < bindings.getArraySize(); i++) {
                indexes.add(Integer.toString(i));
            }

            return indexes;
        }

        if (bindings.hasMembers()) {
            return bindings.getMemberKeys();
        }

        return Collections.emptySet();
    }

    /**
     * Retrieves the collection of values in the bindings.
     *
     * @return a collection of values
     */
    @Override
    public Collection<Object> values() {
        if (bindings.hasArrayElements()) {
            List<Object> elements = new ArrayList<>((int) bindings.getArraySize());

            for (int i = 0; i < bindings.getArraySize(); i++) {
                elements.add(bindings.getArrayElement(i));
            }

            return elements;
        }

        if (bindings.hasMembers()) {
            return bindings.getMemberKeys().stream()
                    .map(bindings::getMember)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }

    /**
     * Retrieves the set of entries in the bindings.
     *
     * @return a set of entries
     */
    @Override
    public Set<Entry<String, Object>> entrySet() {
        if (bindings.hasArrayElements()) {
            Set<Entry<String, Object>> set = new HashSet<>();
            for (int i = 0; i < bindings.getArraySize(); i++) {
                set.add(Pair.of(Integer.toString(i), bindings.getArrayElement(i)));
            }

            return set;
        }

        if (bindings.hasMembers()) {
            Set<Entry<String, Object>> set = new HashSet<>();
            for (var key : bindings.getMemberKeys()) {
                set.add(Pair.of(key, bindings.getMember(key)));
            }

            return set;
        }

        return Collections.emptySet();
    }

    /**
     * Adds a new key-value pair to the bindings.
     *
     * @param name  the key
     * @param value the value
     * @return the previous value associated with the key, or null if none
     */
    @Override
    public Object put(String name, Object value) {
        if (bindings.hasArrayElements()) {
            long index = Long.valueOf(name);

            Value previousValue = index < bindings.getArraySize() ? bindings.getArrayElement(index) : null;
            bindings.setArrayElement(index, value);

            return previousValue;
        }

        Value previousValue = bindings.getMember(name);
        bindings.putMember(name, value);
        return previousValue;
    }

    /**
     * Merges the given map into the bindings.
     *
     * @param toMerge the map to merge
     */
    @Override
    public void putAll(Map<? extends String, ? extends Object> toMerge) {
        if (bindings.hasArrayElements()) {
            toMerge.entrySet().stream()
                    .forEach(entry -> bindings.setArrayElement(Integer.valueOf(entry.getKey()), entry.getValue()));
            return;
        }

        toMerge.entrySet().stream()
                .forEach(entry -> bindings.putMember(entry.getKey(), entry.getValue()));
    }

    /**
     * Checks if the bindings contain the specified key.
     *
     * @param key the key to check
     * @return true if the key is present, false otherwise
     */
    @Override
    public boolean containsKey(Object key) {
        if (bindings.hasArrayElements()) {
            return Long.valueOf(key.toString()) < bindings.getArraySize();
        }

        return bindings.hasMember(key.toString());
    }

    /**
     * Retrieves the value associated with the specified key.
     *
     * @param key the key
     * @return the value associated with the key, or null if none
     */
    @Override
    public Object get(Object key) {
        if (bindings.hasArrayElements()) {
            return bindings.getArrayElement(Long.valueOf(key.toString()));
        }

        return bindings.getMember(key.toString());
    }

    /**
     * Removes the value associated with the specified key.
     *
     * @param key the key
     * @return the previous value associated with the key, or null if none
     */
    @Override
    public Object remove(Object key) {
        if (bindings.hasArrayElements()) {
            long index = Long.parseLong(key.toString());
            Value previousValue = index < bindings.getArraySize() ? bindings.getArrayElement(index) : null;
            bindings.removeArrayElement(index);
            return previousValue;
        }

        Value previousValue = bindings.getMember(key.toString());
        bindings.removeMember(key.toString());
        return previousValue;
    }

    /**
     * Returns a string representation of the bindings.
     *
     * @return a string representation of the bindings
     */
    @Override
    public String toString() {
        return bindings.toString();
    }
}
