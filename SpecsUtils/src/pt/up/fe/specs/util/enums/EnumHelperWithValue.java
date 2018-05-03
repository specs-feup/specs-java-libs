/**
 * Copyright 2016 SPeCS.
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.lazy.ThreadSafeLazy;
import pt.up.fe.specs.util.providers.StringProvider;

public class EnumHelperWithValue<T extends Enum<T> & StringProvider> extends EnumHelper<T> {

    // private final Class<T> enumClass;
    private final Lazy<Map<String, T>> translationMap;
    // private final Lazy<Map<String, T>> namesTranslationMap;
    // private final Lazy<T[]> values;

    public EnumHelperWithValue(Class<T> enumClass) {
        this(enumClass, Collections.emptyList());
    }

    public EnumHelperWithValue(Class<T> enumClass, Collection<T> excludeList) {
        super(enumClass, excludeList);
        // this.enumClass = enumClass;
        this.translationMap = Lazy.newInstance(() -> EnumHelperWithValue.buildTranslationMap(enumClass, excludeList));
        // values = Lazy.newInstance(() -> enumClass.getEnumConstants());
        // namesTranslationMap = Lazy.newInstance(() -> buildNamesTranslationMap(values.get()));
    }

    private static <T extends Enum<T> & StringProvider> Map<String, T> buildTranslationMap(Class<T> enumClass,
            Collection<T> excludeList) {

        Map<String, T> translationMap = SpecsEnums.buildMap(enumClass);

        excludeList.stream()
                .map(exclude -> exclude.getString())
                .forEach(key -> translationMap.remove(key));

        return translationMap;

    }

    // private Map<String, T> buildNamesTranslationMap(T[] values) {
    // Map<String, T> map = new HashMap<>();
    //
    // for (T value : values) {
    // map.put(value.name(), value);
    // }
    //
    // return map;
    // }

    public Map<String, T> getValuesTranslationMap() {
        return translationMap.get();
    }

    public T fromValue(String name) {
        return fromValueTry(name)
                .orElseThrow(() -> new IllegalArgumentException(getErrorMessage(name, translationMap.get())));
    }

    /**
     * Helper method which converts the index of an enum to the enum.
     * 
     * @param index
     * @return
     */
    public T fromValue(int index) {
        T[] array = values();
        if (index >= array.length) {
            throw new RuntimeException(
                    "Asked for enum at index " + index + ", but there are only " + array.length + " values");
        }
        return values()[index];
    }

    // public T valueOfOrNull(String name) {
    // T value = valueOfTry(name).orElse(null);
    // if(value == null) {
    //
    // }
    // }

    // private String getErrorMessage(String name, Map<String, T> translationMap) {
    // return "Enum '" + getEnumClass().getSimpleName() + "' does not contain an enum with the name '" + name
    // + "'. Available enums: " + translationMap;
    // }

    public Optional<T> fromValueTry(String name) {
        T value = translationMap.get().get(name);

        return Optional.ofNullable(value);
    }

    public List<T> fromValue(List<String> names) {
        return names.stream()
                .map(name -> fromValue(name))
                .collect(Collectors.toList());
    }

    /**
     * 
     * @return
     */
    public String getAvailableValues() {
        return translationMap.get().keySet().stream()
                .collect(Collectors.joining(", "));
    }

    public EnumHelperWithValue<T> addAlias(String alias, T anEnum) {
        translationMap.get().put(alias, anEnum);
        return this;
    }

    public static <T extends Enum<T> & StringProvider> Lazy<EnumHelperWithValue<T>> newLazyHelperWithValue(
            Class<T> anEnum) {
        return newLazyHelperWithValue(anEnum, Collections.emptyList());
    }

    public static <T extends Enum<T> & StringProvider> Lazy<EnumHelperWithValue<T>> newLazyHelperWithValue(
            Class<T> anEnum,
            T exclude) {
        return newLazyHelperWithValue(anEnum, Arrays.asList(exclude));
    }

    public static <T extends Enum<T> & StringProvider> Lazy<EnumHelperWithValue<T>> newLazyHelperWithValue(
            Class<T> anEnum,
            Collection<T> excludeList) {
        return new ThreadSafeLazy<>(() -> new EnumHelperWithValue<>(anEnum, excludeList));
    }

    /**
     * Similar to newLazyHelper, but accepts any enum, even if they do not implement StringProvider (uses the
     * enum.name() instead).
     * 
     * @param anEnum
     * @param excludeList
     * @return
     */
    // public static <T extends Enum<T>> Lazy<EnumHelper<T>> newLazyHelperAdapter(Class<T> anEnum,
    // Collection<T> excludeList) {
    // return new ThreadSafeLazy<>(() -> new EnumHelper<>(anEnum, excludeList));
    // }

}
