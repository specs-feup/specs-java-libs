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
import java.util.function.Function;

import pt.up.fe.specs.util.Preconditions;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.utilities.ClassMapper;

/**
 * Maps a class T or subtype of T to a Function that accepts one argument T and produces a result R.
 * 
 * <p>
 * Use this class if you want to:<br>
 * 1) Use classes as keys and want the map to respect the hierarchy (e.g., a value mapped to class Number will be
 * returned if the key is the class Integer and there is no explicit mapping for the class Integer).<br>
 * 2) When adding a value, you want to have access to the methods of the subtype of the key (e.g., if T is Number, you
 * can do .put(Integer.class, integer -> integer.compareTo()) ).
 * 
 * @author JoaoBispo
 *
 * @param <T>
 * @param <R>
 */
public class FunctionClassMap<T, R> {

    private final Map<Class<? extends T>, Function<? extends T, R>> map;
    // private final boolean supportInterfaces;
    // Can be null
    private final R defaultValue;
    // Can be null
    private final Function<T, ? extends R> defaultFunction;

    private final ClassMapper classMapper;

    public FunctionClassMap() {
        this(new HashMap<>(), null, null, new ClassMapper());
    }

    public <ER extends R> FunctionClassMap(ER defaultValue) {
        this(new HashMap<>(), defaultValue, null, new ClassMapper());
    }

    public <ER extends R> FunctionClassMap(Function<T, ER> defaultFunction) {
        this(new HashMap<>(), null, defaultFunction, new ClassMapper());
    }

    @SuppressWarnings("unchecked")
    public <ER extends R> FunctionClassMap(FunctionClassMap<T, ER> functionClassMap) {
        // this(functionClassMap.map, functionClassMap.supportInterfaces, functionClassMap.defaultValue,
        // functionClassMap.defaultFunction);

        this.map = new HashMap<>();
        for (var keyPair : functionClassMap.map.entrySet()) {
            this.map.put((Class<? extends T>) keyPair.getKey(), (Function<T, R>) keyPair.getValue());
        }

        // this.supportInterfaces = functionClassMap.supportInterfaces;
        this.defaultValue = functionClassMap.defaultValue;
        this.defaultFunction = functionClassMap.defaultFunction;
        this.classMapper = new ClassMapper(functionClassMap.classMapper);
    }

    private <ER extends R> FunctionClassMap(Map<Class<? extends T>, Function<? extends T, R>> map, R defaultValue,
            Function<T, ER> defaultFunction, ClassMapper classMapper) {

        Preconditions.checkArgument(!(defaultFunction != null && defaultValue != null),
                "Both defaults cannot be different than null at the same time");

        this.map = map;
        // this.supportInterfaces = supportInterfaces;
        this.defaultValue = defaultValue;
        this.defaultFunction = defaultFunction;

        this.classMapper = classMapper;
    }

    /**
     * Associates the specified value with the specified key.
     * 
     * <p>
     * The key is always a class of a type that is a subtype of the type in the value.
     * <p>
     * Example: <br>
     * - put(Subclass.class, usesSuperClass), ok<br>
     * - put(Subclass.class, usesSubClass), ok<br>
     * - put(Superclass.class, usesSubClass), error<br>
     * 
     * @param aClass
     * @param value
     */
    public <ET extends T, K extends ET> void put(Class<K> aClass,
            Function<ET, R> value) {

        // if (!this.supportInterfaces) {
        // if (aClass.isInterface()) {
        // SpecsLogs.msgWarn("Support for interfaces is disabled, map is unchanged");
        // return;
        // }
        // }

        this.map.put(aClass, value);
        this.classMapper.add(aClass);

    }

    @SuppressWarnings("unchecked")
    private <TK extends T> Optional<Function<T, R>> get(Class<TK> key) {
        // Map given class to a class supported by this instance
        var mappedClass = classMapper.map(key);

        if (mappedClass.isEmpty()) {
            return Optional.empty();
        }

        var function = this.map.get(mappedClass.get());
        // var function = mappedClass.map(aClass -> this.map.get(aClass));

        // if (function.isEmpty()) {
        if (function == null) {
            throw new RuntimeException("There should be a mapping for " + mappedClass.get() + ", verify");
        }

        return Optional.of((Function<T, R>) function);

        /*
        Class<?> currentKey = key;
        
        while (currentKey != null) {
            // Test key
            Function<? extends T, R> result = this.map.get(currentKey);
            if (result != null) {
                return Optional.of((Function<T, R>) result);
            }
        
            if (this.supportInterfaces) {
                for (Class<?> interf : currentKey.getInterfaces()) {
                    result = this.map.get(interf);
                    if (result != null) {
                        return Optional.of((Function<T, R>) result);
                    }
                }
            }
        
            currentKey = currentKey.getSuperclass();
        }
        
        return Optional.empty();
        */
    }

    @SuppressWarnings("unchecked")
    private <TK extends T> Optional<Function<T, R>> get(TK key) {
        return get((Class<TK>) key.getClass());
    }

    /**
     * Calls the Function.apply associated with class of the value t, or Optional.empty if no mapping could be found.
     * 
     * @param t
     */
    public Optional<R> applyTry(T t) {
        Optional<Function<T, R>> function = get(t);

        // Found function, apply it
        if (function.isPresent()) {
            return Optional.ofNullable(function.get().apply(t));
        }

        // Try getting a default value
        return defaultValue(t);
    }

    /**
     * Calls the Function.apply associated with class of the value t, or throws an Exception if no mapping could be
     * found.
     * 
     * @param t
     */
    public R apply(T t) {
        Optional<Function<T, R>> function = get(t);

        // Found function, apply it
        if (function.isPresent()) {
            return function.get().apply(t);
        }

        // Try getting a default value
        Optional<R> result = defaultValue(t);
        if (result.isPresent()) {
            return result.get();
        }

        throw new NotImplementedException("Function not defined for class '"
                + t.getClass() + "'");
        /*
        if (function == null) {
            throw new NotImplementedException("BiConsumer not defined for class '"
        	    + t.getClass() + "'");
        }
        
        return function.apply(t);
         */
    }

    private Optional<R> defaultValue(T t) {
        // Both defaults cannot be set at the same time, order does not matter

        if (this.defaultValue != null) {
            return Optional.of(this.defaultValue);
        }

        if (this.defaultFunction != null) {
            return Optional.of(this.defaultFunction.apply(t));
        }

        return Optional.empty();
    }

    /**
     * Sets the default value, backed up by the same map.
     * 
     * @param defaultValue
     * @return
     */
    public FunctionClassMap<T, R> setDefaultValue(R defaultValue) {
        return new FunctionClassMap<>(this.map, defaultValue, null, this.classMapper);
    }

    public <ER extends R> FunctionClassMap<T, R> setDefaultFunction(Function<T, ER> defaultFunction) {
        return new FunctionClassMap<>(this.map, null, defaultFunction, this.classMapper);
    }

}
