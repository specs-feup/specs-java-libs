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

package pt.up.fe.specs.util.classmap;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.utilities.ClassMapper;

/**
 * Maps a class to a BiFunction that receives an instance of that class being
 * used as key and other object.
 * 
 * @author JoaoBispo
 *
 * @param <T>
 * @param <U>
 */
public class BiFunctionClassMap<T, U, R> {

    private final Map<Class<? extends T>, BiFunction<? extends T, U, R>> map;
    private final ClassMapper classMapper;

    public BiFunctionClassMap() {
        this.map = new HashMap<>();
        // this.supportInterfaces = true;
        this.classMapper = new ClassMapper();
    }

    public <ER extends R> BiFunctionClassMap(BiFunctionClassMap<T, U, ER> other) {
        this.map = new HashMap<>();
        for (var keyPair : other.map.entrySet()) {
            this.map.put(keyPair.getKey(), (BiFunction<T, U, R>) keyPair.getValue());
        }
        this.classMapper = new ClassMapper(other.classMapper);
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
     */
    public <VS extends T, KS extends VS> void put(Class<KS> aClass,
            BiFunction<VS, U, R> value) {
        this.map.put(aClass, value);
        classMapper.add(aClass);
    }

    @SuppressWarnings("unchecked")
    private <TK extends T> BiFunction<T, U, R> get(Class<TK> key) {
        // Map given class to a class supported by this instance
        var mappedClass = classMapper.map(key);

        if (mappedClass.isEmpty()) {
            return null;
        }

        var function = this.map.get(mappedClass.get());

        Objects.requireNonNull(function, () -> "There should be a mapping for " + mappedClass.get() + ", verify");

        return (BiFunction<T, U, R>) function;
    }

    @SuppressWarnings("unchecked")
    private <TK extends T> BiFunction<T, U, R> get(TK key) {
        return get((Class<TK>) key.getClass());
    }

    /**
     * Calls the BiFunction.accept associated with class of the value t, or throws
     * an Exception if no BiFunction could be found in the map.
     *
     */
    public R apply(T t, U u) {
        BiFunction<T, U, R> result = get(t);

        if (result == null) {
            throw new NotImplementedException("BiConsumer not defined for class '"
                    + t.getClass() + "'");
        }

        return result.apply(t, u);
    }

}
