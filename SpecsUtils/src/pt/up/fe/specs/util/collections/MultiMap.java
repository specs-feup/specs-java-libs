/*
 * Copyright 2011 SPeCS Research Group.
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

package pt.up.fe.specs.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Each key is associated to a list of values.
 * 
 * @author Joao Bispo
 */
public class MultiMap<K, V> {

    @FunctionalInterface
    public interface MultiMapProvider<K, V> {
        Map<K, List<V>> newInstance();
    }

    private final Map<K, List<V>> map;

    public MultiMap() {
        this.map = new HashMap<>();
    }

    public MultiMap(MultiMap<K, V> other) {
        this.map = new HashMap<>(other.map);
    }

    public MultiMap(MultiMapProvider<K, V> mapProvider) {
        this.map = mapProvider.newInstance();
    }

    /**
     * Returns the values associated to the parameter key.
     * 
     * @param key The key the user wants the values of.
     * @return the values associated to the parameter key.
     */
    public List<V> get(K key) {
        return this.map.getOrDefault(key, Collections.emptyList());
    }

    /**
     * If key does not exist, creates an entry.
     *
     */
    private List<V> getPrivate(K key) {

        return this.map.computeIfAbsent(key, k -> new ArrayList<>());
    }

    /**
     * Adds the given value to the key.
     *
     * @param key   the key the user wants to attribute a value to.
     * @param value the value the user wants to attribute to the key.
     */
    public void put(K key, V value) {
        List<V> values = getPrivate(key);

        values.add(value);
    }

    public void add(K key, V value) {
        getPrivate(key).add(value);
    }

    /**
     * Replaces the current value mapped to the given key with the given values
     * 
     * @param key    the key the user wants to attribute a values to.
     * @param values a List containing all the values the user wants to attribute to
     *               the key.
     */
    public void put(K key, List<V> values) {
        this.map.put(key, new ArrayList<>(values));
    }

    public void addAll(K key, List<V> values) {
        if (values.isEmpty()) {
            return;
        }

        List<V> previousValues = getPrivate(key);
        previousValues.addAll(values);
    }

    /**
     * TODO
     *
     */
    @Override
    public String toString() {
        return map.toString();
    }

    public Map<K, List<V>> getMap() {
        return this.map;
    }

    public Set<K> keySet() {
        return this.map.keySet();
    }

    public static <K, V> MultiMap<K, V> newInstance() {
        return new MultiMap<>();
    }

    public List<V> remove(K key) {
        return this.map.remove(key);
    }

    public Collection<List<V>> values() {
        return map.values();
    }

    public Collection<V> valuesFlat() {
        return map.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public List<V> flatValues() {
        return map
                .values()
                .stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    public Set<Entry<K, List<V>>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof MultiMap && equals((MultiMap<?, ?>) obj);
    }

    public boolean equals(MultiMap<?, ?> obj) {
        if (obj == null) {
            return false;
        }

        return this.map.equals(obj.map);
    }

    @Override
    public int hashCode() {
        return this.map.hashCode();
    }

    public boolean containsKey(K key) {
        return this.map.containsKey(key);
    }

    public void clear() {
        map.clear();
    }

    public int size() {
        return map.size();
    }
}
