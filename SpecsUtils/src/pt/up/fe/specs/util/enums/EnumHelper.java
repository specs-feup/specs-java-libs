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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.lazy.ThreadSafeLazy;
import pt.up.fe.specs.util.providers.StringProvider;

public class EnumHelper<T extends Enum<T> & StringProvider> extends EnumHelperBase<T> {

    private final Class<T> enumClass;
    private final Lazy<Map<String, T>> translationMap;
    private final Lazy<Map<String, T>> namesTranslationMap;
    private final Lazy<T[]> values;

    public EnumHelper(Class<T> enumClass) {
        this(enumClass, Collections.emptyList());
    }

    public EnumHelper(Class<T> enumClass, Collection<T> excludeList) {
        this.enumClass = enumClass;
        this.translationMap = Lazy.newInstance(() -> EnumHelper.buildTranslationMap(enumClass, excludeList));
        values = Lazy.newInstance(() -> enumClass.getEnumConstants());
        namesTranslationMap = Lazy.newInstance(() -> buildNamesTranslationMap(values.get()));
    }

    private static <T extends Enum<T> & StringProvider> Map<String, T> buildTranslationMap(Class<T> enumClass,
            Collection<T> excludeList) {

        Map<String, T> translationMap = SpecsEnums.buildMap(enumClass);

        excludeList.stream()
                .map(exclude -> exclude.getString())
                .forEach(key -> translationMap.remove(key));

        return translationMap;

    }

    private Map<String, T> buildNamesTranslationMap(T[] values) {
        Map<String, T> map = new HashMap<>();

        for (T value : values) {
            map.put(value.name(), value);
        }

        return map;
    }

    public Map<String, T> getTranslationMap() {
        return translationMap.get();
    }

    public T fromValue(String name) {
        return fromValueTry(name)
                .orElseThrow(() -> new IllegalArgumentException(getErrorMessage(name, translationMap.get())));
    }

    public T fromName(String name) {
        return fromNameTry(name)
                .orElseThrow(() -> new IllegalArgumentException(getErrorMessage(name, namesTranslationMap.get())));
    }

    /**
     * Helper method which converts the index of an enum to the enum.
     * 
     * @param index
     * @return
     */
    public T fromValue(int index) {
        T[] array = values.get();
        if (index >= array.length) {
            throw new RuntimeException(
                    "Asked for enum at index " + index + ", but there are only " + array.length + " values");
        }
        return values.get()[index];
    }

    // public T valueOfOrNull(String name) {
    // T value = valueOfTry(name).orElse(null);
    // if(value == null) {
    //
    // }
    // }

    private String getErrorMessage(String name, Map<String, T> translationMap) {
        return "Enum '" + enumClass.getSimpleName() + "' does not contain an enum with the name '" + name
                + "'. Available enums: " + translationMap;
    }

    public Optional<T> fromValueTry(String name) {
        T value = translationMap.get().get(name);

        return Optional.ofNullable(value);
    }

    public Optional<T> fromNameTry(String name) {
        T value = namesTranslationMap.get().get(name);

        return Optional.ofNullable(value);
    }

    public List<T> fromValue(List<String> names) {
        return names.stream()
                .map(name -> fromValue(name))
                .collect(Collectors.toList());
    }

    public String getAvailableOptions() {
        return translationMap.get().keySet().stream()
                .collect(Collectors.joining(", "));
    }

    public EnumHelper<T> addAlias(String alias, T anEnum) {
        translationMap.get().put(alias, anEnum);
        return this;
    }

    public int getSize() {
        return values.get().length;
    }

    public static <T extends Enum<T> & StringProvider> Lazy<EnumHelper<T>> newLazyHelper(Class<T> anEnum) {
        return newLazyHelper(anEnum, Collections.emptyList());
    }

    public static <T extends Enum<T> & StringProvider> Lazy<EnumHelper<T>> newLazyHelper(Class<T> anEnum,
            T exclude) {
        return newLazyHelper(anEnum, Arrays.asList(exclude));
    }

    public static <T extends Enum<T> & StringProvider> Lazy<EnumHelper<T>> newLazyHelper(Class<T> anEnum,
            Collection<T> excludeList) {
        return new ThreadSafeLazy<>(() -> new EnumHelper<>(anEnum, excludeList));
    }

    public T[] values() {
        return values.get();
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
