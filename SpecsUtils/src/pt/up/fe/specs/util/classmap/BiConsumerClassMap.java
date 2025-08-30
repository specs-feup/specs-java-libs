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
import java.util.function.BiConsumer;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.utilities.ClassMapper;

/**
 * Maps a class to a BiConsumer that receives an instance of that class being
 * used as key and other object.
 * 
 * @author JoaoBispo
 *
 * @param <T>
 * @param <U>
 */
public class BiConsumerClassMap<T, U> {

    private final Map<Class<? extends T>, BiConsumer<? extends T, U>> map;
    private final boolean ignoreNotFound;
    private final ClassMapper classMapper;

    public BiConsumerClassMap() {
        this(false, new ClassMapper());
    }

    private BiConsumerClassMap(boolean ignoreNotFound, ClassMapper classMapper) {
        this.map = new HashMap<>();
        this.ignoreNotFound = ignoreNotFound;
        this.classMapper = classMapper;
    }

    /**
     * 
     * @param ignoreNotFound
     * @return
     */
    public static <T, U> BiConsumerClassMap<T, U> newInstance(boolean ignoreNotFound) {
        return new BiConsumerClassMap<>(ignoreNotFound, new ClassMapper());
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
    public <VS extends T, KS extends VS> void put(Class<KS> aClass,
            BiConsumer<VS, U> value) {
        this.map.put(aClass, value);
        this.classMapper.add(aClass);
    }

    @SuppressWarnings("unchecked")
    private <TK extends T> BiConsumer<T, U> get(Class<TK> key) {
        // Map given class to a class supported by this instance
        var mappedClass = classMapper.map(key);

        if (mappedClass.isEmpty()) {
            return null;
        }

        var function = this.map.get(mappedClass.get());

        SpecsCheck.checkNotNull(function, () -> "There should be a mapping for " + mappedClass.get() + ", verify");

        return (BiConsumer<T, U>) function;
    }

    @SuppressWarnings("unchecked")
    private <TK extends T> BiConsumer<T, U> get(TK key) {
        return get((Class<TK>) key.getClass());
    }

    /**
     * Calls the BiConsumer.accept associated with class of the value t, or throws
     * an Exception if no BiConsumer could be found in the map.
     * 
     * @param t
     * @param u
     */
    public void accept(T t, U u) {
        BiConsumer<T, U> result = get(t);

        if (result != null) {
            result.accept(t, u);
            return;
        }

        // Just return
        if (ignoreNotFound) {
            return;
        }

        throw new NotImplementedException("BiConsumer not defined for class '"
                + t.getClass() + "'");
    }

}
