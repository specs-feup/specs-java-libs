/**
 * Copyright 2018 SPeCS.
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

package pt.up.fe.specs.util.enums;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.lazy.ThreadSafeLazy;

public class EnumHelper<T extends Enum<T>> {

    private final Class<T> enumClass;
    private final Lazy<Map<String, T>> namesTranslationMap;
    private final Lazy<T[]> enumValues;

    public EnumHelper(Class<T> enumClass) {
        this(enumClass, Collections.emptyList());
    }

    public EnumHelper(Class<T> enumClass, Collection<T> excludeList) {
        this.enumClass = enumClass;
        enumValues = Lazy.newInstance(() -> enumClass.getEnumConstants());
        namesTranslationMap = Lazy.newInstance(() -> SpecsEnums.buildNamesMap(enumClass, excludeList));
    }

    // private static <T extends Enum<T>> Map<String, T> buildTranslationMap(Class<T> enumClass,
    // Collection<T> excludeList) {
    //
    // Map<String, T> translationMap = SpecsEnums.buildNamesMap(enumClass, excludeList);
    //
    // // excludeList.stream()
    // // .map(exclude -> translationMap.get(exclude))
    // // .forEach(key -> translationMap.remove(key));
    //
    // return translationMap;
    //
    // }

    // private Map<String, T> buildNamesTranslationMap(T[] values) {
    // Map<String, T> map = new HashMap<>();
    //
    // for (T value : values) {
    // map.put(value.name(), value);
    // }
    //
    // return map;
    // }

    public Class<T> getEnumClass() {
        return enumClass;
    }

    /*
    public Map<String, T> getTranslationMap() {
        return translationMap.get();
    }
    
    public T fromValue(String name) {
        return fromValueTry(name)
                .orElseThrow(() -> new IllegalArgumentException(getErrorMessage(name, translationMap.get())));
    }
    */
    public T fromName(String name) {
        return fromNameTry(name).orElseThrow(() -> new RuntimeException(
                "Could not find enum with name '" + name + "', available names:" + namesTranslationMap.get().keySet()));
        // "Could not find enum with name '" + name + "', available names:" + Arrays.toString(values())));
        // return Enum.valueOf(enumClass, name);

        // return fromNameTry(name)
        // .orElseThrow(() -> new IllegalArgumentException(getErrorMessage(name, namesTranslationMap.get())));
    }

    // public String messageNameNotFound(String name) {
    // return "Could not find enum with name '" + name + "', available names:" + namesTranslationMap.get().keySet();
    // }

    public Optional<T> fromNameTry(String name) {
        // try {
        var anEnum = namesTranslationMap.get().get(name);
        return Optional.ofNullable(anEnum);
        // return Optional.of(Enum.valueOf(enumClass, name));
        // } catch (Exception e) {
        // return Optional.empty();
        // }s
        // return fromNameTry(name)
        // .orElseThrow(() -> new IllegalArgumentException(getErrorMessage(name, namesTranslationMap.get())));
    }

    /**
     * Helper method which converts the index of an enum to the enum.
     * 
     * @param index
     * @return
     */
    /*
    public T fromValue(int index) {
        T[] array = values.get();
        if (index >= array.length) {
            throw new RuntimeException(
                    "Asked for enum at index " + index + ", but there are only " + array.length + " values");
        }
        return values.get()[index];
    }
    */

    protected String getErrorMessage(String name, Map<String, T> translationMap) {
        return "Enum '" + enumClass.getSimpleName() + "' does not contain an enum with the name '" + name
                + "'. Available enums: " + translationMap.keySet();
    }

    /*
    public Optional<T> fromValueTry(String name) {
        T value = translationMap.get().get(name);
    
        return Optional.ofNullable(value);
    }
    */

    /*
    public Optional<T> fromNameTry(String name) {
        Enum.valueOf(enumClass, name);
        T value = namesTranslationMap.get().get(name);
    
        return Optional.ofNullable(value);
    }
    */

    public Optional<T> fromOrdinalTry(int ordinal) {
        T[] values = values();

        if (ordinal < 0 || ordinal >= values.length) {
            return Optional.empty();
        }

        return Optional.of(values[ordinal]);
    }

    public T fromOrdinal(int ordinal) {
        return fromOrdinalTry(ordinal)
                .orElseThrow(() -> new RuntimeException(
                        "Given ordinal '" + ordinal + "' is out of range, enum has " + values().length + " values"));
    }

    /*
    public List<T> fromValue(List<String> names) {
        return names.stream()
                .map(name -> fromValue(name))
                .collect(Collectors.toList());
    }
    */
    /*
    public String getAvailableOptions() {
        return translationMap.get().keySet().stream()
                .collect(Collectors.joining(", "));
    }
    
    public EnumHelperWithValue<T> addAlias(String alias, T anEnum) {
        translationMap.get().put(alias, anEnum);
        return this;
    }
    */
    public int getSize() {
        return enumValues.get().length;
    }

    public static <T extends Enum<T>> Lazy<EnumHelper<T>> newLazyHelper(Class<T> anEnum) {
        return newLazyHelper(anEnum, Collections.emptyList());
    }

    public static <T extends Enum<T>> Lazy<EnumHelper<T>> newLazyHelper(Class<T> anEnum,
            T exclude) {
        return newLazyHelper(anEnum, Arrays.asList(exclude));
    }

    public static <T extends Enum<T>> Lazy<EnumHelper<T>> newLazyHelper(Class<T> anEnum,
            Collection<T> excludeList) {
        return new ThreadSafeLazy<>(() -> new EnumHelper<>(anEnum, excludeList));
    }

    public T[] values() {
        return enumValues.get();
    }

    /**
     * The names used to map Strings to Enums. Might not be the same as the Enum name, if the Enum implements
     * StringProvider.
     * 
     * @return
     */
    public Collection<String> names() {
        return this.namesTranslationMap.get().keySet();
    }

}
