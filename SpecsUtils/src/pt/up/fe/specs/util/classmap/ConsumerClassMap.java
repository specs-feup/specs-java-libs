/**
 * Copyright 2015 SPeCS Research Group.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.classmap;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.utilities.ClassMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Maps a class to a Consumer that receives an instance of that class being used
 * as key and other object.
 *
 * @param <T>
 * @author JoaoBispo
 */
public class ConsumerClassMap<T> {

    private final Map<Class<? extends T>, Consumer<? extends T>> map;
    private final boolean ignoreNotFound;
    private final ClassMapper classMapper;

    public ConsumerClassMap() {
        this(false, new ClassMapper());
    }

    private ConsumerClassMap(boolean ignoreNotFound, ClassMapper classMapper) {
        this.map = new HashMap<>();
        this.ignoreNotFound = ignoreNotFound;
        this.classMapper = classMapper;
    }

    public static <T> ConsumerClassMap<T> newInstance(boolean ignoreNotFound) {
        return new ConsumerClassMap<>(ignoreNotFound, new ClassMapper());
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
            Consumer<VS> value) {
        this.map.put(aClass, value);
        this.classMapper.add(aClass);
    }

    @SuppressWarnings("unchecked")
    private <TK extends T> Consumer<T> get(Class<TK> key) {
        // Map given class to a class supported by this instance
        var mappedClass = classMapper.map(key);

        if (mappedClass.isEmpty()) {
            return null;
        }

        var function = this.map.get(mappedClass.get());

        SpecsCheck.checkNotNull(function, () -> "There should be a mapping for " + mappedClass.get() + ", verify");

        return (Consumer<T>) function;
    }

    @SuppressWarnings("unchecked")
    private <TK extends T> Consumer<T> get(TK key) {
        return get((Class<TK>) key.getClass());
    }

    /**
     * Calls the Consumer.accept associated with class of the value t, or throws an
     * Exception if no Consumer could be found in the map.
     *
     */
    public void accept(T t) {
        Consumer<T> result = get(t);

        if (result != null) {
            result.accept(t);
            return;
        }

        // Just return
        if (ignoreNotFound) {
            return;
        }

        throw new NotImplementedException("Consumer not defined for class '"
                + t.getClass() + "'");
    }

}
