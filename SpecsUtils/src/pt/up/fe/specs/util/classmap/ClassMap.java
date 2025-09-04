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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.classmap;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.utilities.ClassMapper;

/**
 * Maps a class T or subtype of T to a value V.
 * 
 * <p>
 * Use this class if you want to:<br>
 * 1) Use classes as keys and want the map to respect the hierarchy (e.g., a
 * value mapped to class Number will be returned if the key is the class Integer
 * and there is no explicit mapping for the class Integer).<br>
 * 
 * @author JoaoBispo
 *
 * @param <T>
 * @param <V>
 */
public class ClassMap<T, V> {

    private final Map<Class<? extends T>, V> map;

    // Can be null
    private final V defaultValue;

    private final ClassMapper classMapper;

    public ClassMap() {
        this(new HashMap<>(), null, new ClassMapper());
    }

    public <ER extends V> ClassMap(ER defaultValue) {
        this(new HashMap<>(), defaultValue, new ClassMapper());
    }

    private ClassMap(Map<Class<? extends T>, V> map, V defaultValue,
            ClassMapper classMapper) {

        this.map = map;
        this.defaultValue = defaultValue;
        this.classMapper = classMapper;
    }

    public ClassMap<T, V> copy() {
        return new ClassMap<>(new HashMap<>(this.map), this.defaultValue, this.classMapper);
    }

    /**
     * Associates the specified value with the specified key.
     * 
     * <p>
     * The key is always a class of a type that is a subtype of the type in the
     * value.
     * <p>
     * Example: <br>
     * - put(Subclass.class, usesSuperClass), ok<br>
     * - put(Subclass.class, usesSubClass), ok<br>
     * - put(Superclass.class, usesSubClass), error<br>
     * 
     * @param aClass
     * @param value
     */
    public <ET extends T, K extends ET> V put(Class<K> aClass,
            V value) {
        classMapper.add(aClass);
        return this.map.put(aClass, value);
    }

    public <TK extends T> Optional<V> tryGet(Class<TK> key) {
        // Check for null key
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        // Map given class to a class supported by this instance
        var mappedClass = classMapper.map(key);

        if (mappedClass.isPresent()) {
            var result = this.map.get(mappedClass.get());
            // Allow null values to be stored and retrieved
            return Optional.ofNullable(result);
        }

        // Return default value if present
        if (this.defaultValue != null) {
            return Optional.of(this.defaultValue);
        }

        return Optional.empty();

    }

    public <TK extends T> V get(Class<TK> key) {
        // Null check
        if (key == null) {
            throw new NullPointerException("Key cannot be null");
        }

        // Map given class to a class supported by this instance
        var mappedClass = classMapper.map(key);

        if (mappedClass.isPresent()) {
            var mapped = mappedClass.get();
            // If this instance has an explicit mapping (even if value is null), return it
            if (this.map.containsKey(mapped)) {
                return this.map.get(mapped);
            }
            // Mapping was found by the class mapper, but this map doesn't have the key
            throw new NullPointerException("Expected map to contain " + mapped);
        }

        // Return default value if present
        if (this.defaultValue != null) {
            return this.defaultValue;
        }

        throw new NotImplementedException("Function not defined for class '"
                + key + "'");
    }

    @SuppressWarnings("unchecked")
    public <TK extends T> V get(TK key) {
        return get((Class<TK>) key.getClass());
    }

    /**
     * Sets the default value, backed up by the same map.
     * 
     * @param defaultValue
     * @return
     */
    public ClassMap<T, V> setDefaultValue(V defaultValue) {
        return new ClassMap<>(this.map, defaultValue, this.classMapper);
    }

    public Set<Entry<Class<? extends T>, V>> entrySet() {
        return map.entrySet();
    }

    public Set<Class<? extends T>> keySet() {
        return map.keySet();
    }

}
