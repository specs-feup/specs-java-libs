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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.enums;

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
        if (enumClass == null) {
            throw new NullPointerException("Enum class cannot be null");
        }
        this.enumClass = enumClass;
        enumValues = Lazy.newInstance(enumClass::getEnumConstants);
        namesTranslationMap = Lazy.newInstance(() -> SpecsEnums.buildNamesMap(enumClass, excludeList));
    }

    public Class<T> getEnumClass() {
        return enumClass;
    }

    public T fromName(String name) {
        return fromNameTry(name).orElseThrow(() -> new RuntimeException(
                "Could not find enum with name '" + name + "', available names:" + namesTranslationMap.get().keySet()));
    }

    public Optional<T> fromNameTry(String name) {
        var anEnum = namesTranslationMap.get().get(name);
        return Optional.ofNullable(anEnum);
    }

    protected String getErrorMessage(String name, Map<String, T> translationMap) {
        return "Enum '" + enumClass.getSimpleName() + "' does not contain an enum with the name '" + name
                + "'. Available enums: " + translationMap.keySet();
    }

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

    public int getSize() {
        return enumValues.get().length;
    }

    public static <T extends Enum<T>> Lazy<EnumHelper<T>> newLazyHelper(Class<T> anEnum) {
        return newLazyHelper(anEnum, Collections.emptyList());
    }

    public static <T extends Enum<T>> Lazy<EnumHelper<T>> newLazyHelper(Class<T> anEnum,
            T exclude) {
        return newLazyHelper(anEnum, Collections.singletonList(exclude));
    }

    public static <T extends Enum<T>> Lazy<EnumHelper<T>> newLazyHelper(Class<T> anEnum,
            Collection<T> excludeList) {
        return new ThreadSafeLazy<>(() -> new EnumHelper<>(anEnum, excludeList));
    }

    public T[] values() {
        return enumValues.get();
    }

    /**
     * The names used to map Strings to Enums. Might not be the same as the Enum
     * name, if the Enum implements StringProvider.
     *
     */
    public Collection<String> names() {
        return this.namesTranslationMap.get().keySet();
    }

}
