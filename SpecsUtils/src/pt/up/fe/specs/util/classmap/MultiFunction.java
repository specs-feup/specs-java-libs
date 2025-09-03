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
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import pt.up.fe.specs.util.Preconditions;
import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.utilities.ClassMapper;

/**
 * Maps a class T or subtype of T to a Function that accepts one argument T and
 * produces a result R.
 * 
 * <p>
 * Use this class if you want to:<br>
 * 1) Use classes as keys and want the map to respect the hierarchy (e.g., a
 * value mapped to class Number will be returned if the key is the class Integer
 * and there is no explicit mapping for the class Integer).<br>
 * 2) When adding a value, you want to have access to the methods of the subtype
 * of the key (e.g., if T is Number, you can do
 * put(Integer.class, integer -> integer.compareTo()) ).
 * 
 * @author JoaoBispo
 *
 * @param <T>
 * @param <R>
 */
public class MultiFunction<T, R> {

    private final Map<Class<? extends T>, BiFunction<? extends MultiFunction<T, R>, ? extends T, ? extends R>> map;

    // Can be null
    private final R defaultValue;
    private final ClassMapper classMapper;

    // Can be null
    private final BiFunction<MultiFunction<T, R>, T, R> defaultFunction;

    public MultiFunction() {
        this(new HashMap<>(), null, null, new ClassMapper());
    }

    public <ER extends R> MultiFunction(Function<T, ER> defaultFunction) {
        this((bi, in) -> defaultFunction.apply(in));
    }

    public <EM extends MultiFunction<T, R>> MultiFunction(
            BiFunction<EM, T, R> defaultFunction) {
        this(new HashMap<>(), null, defaultFunction, new ClassMapper());
    }

    @SuppressWarnings("unchecked")
    private <EM extends MultiFunction<T, R>> MultiFunction(
            Map<Class<? extends T>, BiFunction<? extends MultiFunction<T, R>, ? extends T, ? extends R>> map,
            R defaultValue, BiFunction<EM, T, R> defaultFunction, ClassMapper classMapper) {

        Preconditions.checkArgument(!(defaultFunction != null && defaultValue != null),
                "Both defaults cannot be different than null at the same time");

        this.map = map;
        this.defaultValue = defaultValue;
        this.defaultFunction = (BiFunction<MultiFunction<T, R>, T, R>) defaultFunction;
        this.classMapper = classMapper;
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
    public <EM extends MultiFunction<T, R>, ET extends T, K extends ET> void put(Class<K> aClass,
            BiFunction<EM, ET, R> value) {
        this.map.put(aClass, value);
        this.classMapper.add(aClass);
    }

    public <ET extends T, K extends ET> void put(Class<K> aClass,
            Function<ET, R> value) {

        BiFunction<MultiFunction<T, R>, ET, R> biFunction = (bi, in) -> value.apply(in);
        put(aClass, biFunction);
    }

    @SuppressWarnings("unchecked")
    private <TK extends T> Optional<BiFunction<MultiFunction<T, R>, T, R>> get(Class<TK> key) {
        // Map given class to a class supported by this instance
        var mappedClass = classMapper.map(key);

        if (mappedClass.isEmpty()) {
            return Optional.empty();
        }

        var function = this.map.get(mappedClass.get());

        SpecsCheck.checkNotNull(function, () -> "There should be a mapping for " + mappedClass.get() + ", verify");

        return Optional.of((BiFunction<MultiFunction<T, R>, T, R>) function);
    }

    @SuppressWarnings("unchecked")
    private <TK extends T> Optional<BiFunction<MultiFunction<T, R>, T, R>> get(TK key) {
        return get((Class<TK>) key.getClass());
    }

    /**
     * Calls the Function.apply associated with class of the value t, or throws an
     * Exception if no mapping could be found.
     * 
     * @param t
     */
    public R apply(T t) {
        Optional<BiFunction<MultiFunction<T, R>, T, R>> function = get(t);

        // Found function, apply it
        if (function.isPresent()) {
            return function.get().apply(this, t);
        }

        // Try getting a default value
        Optional<R> result = defaultValue(t);
        if (result.isPresent()) {
            return result.get();
        }

        throw new NotImplementedException("Function not defined for class '"
                + t.getClass() + "'");
    }

    private Optional<R> defaultValue(T t) {
        // Both defaults cannot be set at the same time, order does not matter

        if (this.defaultValue != null) {
            return Optional.of(this.defaultValue);
        }

        if (this.defaultFunction != null) {
            return Optional.of(this.defaultFunction.apply(this, t));
        }

        return Optional.empty();
    }

    /**
     * Sets the default value, backed up by the same map.
     * 
     * @param defaultValue
     * @return
     */
    public MultiFunction<T, R> setDefaultValue(R defaultValue) {
        return new MultiFunction<>(this.map, defaultValue, null, this.classMapper);
    }

    public <ER extends R> MultiFunction<T, R> setDefaultFunction(Function<T, ER> defaultFunction) {
        return setDefaultFunction((bi, in) -> defaultFunction.apply(in));
    }

    public <EM extends MultiFunction<T, R>> MultiFunction<T, R> setDefaultFunction(
            BiFunction<EM, T, R> defaultFunction) {

        return new MultiFunction<>(this.map, null, defaultFunction, this.classMapper);
    }

}
