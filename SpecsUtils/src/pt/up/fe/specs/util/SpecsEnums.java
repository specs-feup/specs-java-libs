/*
 * Copyright 2010 SPeCS Research Group.
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

package pt.up.fe.specs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.enums.EnumHelper;
import pt.up.fe.specs.util.providers.KeyProvider;
import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Methods for Enumeration manipulation.
 *
 * @author Joao Bispo
 */
public class SpecsEnums {

    private static final ThreadLocal<Map<Class<Enum<?>>, EnumHelper<?>>> ENUM_HELPERS = ThreadLocal
            .withInitial(HashMap::new);

    /**
     * Transforms a String into a constant of the same name in a specific Enum.
     * Returns null instead of throwing
     * exceptions.
     *
     *
     * @param <T>      The Enum where the constant is
     * @param enumType the Class object of the enum type from which to return a
     *                 constant
     * @param name     the name of the constant to return
     * @return the constant of enum with the same name, or the first element
     *         (ordinal order) if not found, with a
     *         warning
     */
    public static <T extends Enum<T>> T valueOf(Class<T> enumType, String name) {
        try {
            return Enum.valueOf(enumType, name);
        } catch (Exception e) {

            var firstElement = enumType.getEnumConstants()[0];
            SpecsLogs.warn("Exception while retrieving enum with name '" + name + "' from type '" + enumType
                    + "', returning first element '" + firstElement + "'", e);
            return firstElement;
        }
    }

    public static <T extends Enum<T>> Optional<T> valueOfTry(Class<T> enumType, String name) {
        return Optional.ofNullable(valueOf(enumType, name));
    }

    /**
     * Builds an unmmodifiable table which maps the string representation of the
     * enum to the enum itself.
     *
     * <p>
     * This table can be useful to get the enum correspondent to a particular option
     * in String format which was collected from, for example, a config file.
     *
     */
    public static <K extends Enum<K>> Map<String, K> buildMap(K[] values) {
        Map<String, K> aMap = new HashMap<>();

        for (K enume : values) {
            aMap.put(enume.toString(), enume);
        }

        return Collections.unmodifiableMap(aMap);
    }

    /**
     * Builds a n unmodifiable of the enum to the enum itself. If the enum
     * implements StringProvider, .getString() is used instead of .name().
     *
     * <p>
     * This table can be useful to get the enum correspondent to a particular option
     * in String format which was collected from, for example, a config file.
     *
     */
    public static <K extends Enum<K>> Map<String, K> buildNamesMap(Class<K> enumClass, Collection<K> excludeList) {
        Map<String, K> aMap = new LinkedHashMap<>();

        Function<K, String> toString = StringProvider.class.isAssignableFrom(enumClass)
                ? anEnum -> ((StringProvider) anEnum).getString()
                : Enum::name;

        var excludeSet = new HashSet<>(excludeList);

        for (K enume : enumClass.getEnumConstants()) {
            if (excludeSet.contains(enume)) {
                continue;
            }

            aMap.put(toString.apply(enume), enume);
        }

        return Collections.unmodifiableMap(aMap);
    }

    public static <T> List<T> extractValues(List<Class<? extends T>> enumClasses) {
        return enumClasses.stream()
                .map(SpecsEnums::extractValues)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * If the class represents an enum, returns a list with the values of that enum.
     * Otherwise, returns null.
     *
     */
    public static <T> List<T> extractValues(Class<? extends T> anEnumClass) {
        // Check class
        if (!anEnumClass.isEnum()) {
            SpecsLogs.warn("Class '" + anEnumClass.getName() + "' does not represent an enum.");
            return null;
        }

        // Get enums
        T[] enumKeys = anEnumClass.getEnumConstants();
        return Arrays.asList(enumKeys);
    }

    /**
     * Extracts an instance of an interface from a class which represents an Enum
     * which implements such interface.
     *
     */
    public static <E extends Enum<E>> Object getInterfaceFromEnum(Class<E> enumImplementingInterface,
            Class<?> interfaceClass) {
        // Build set with interfaces of the given class
        Class<?>[] interfacesArray = enumImplementingInterface.getInterfaces();
        List<Class<?>> interfacesList = Arrays.asList(interfacesArray);
        Set<Class<?>> interfaces = new HashSet<>(interfacesList);

        if (!interfaces.contains(interfaceClass)) {
            SpecsLogs.warn(
                    "Class '" + enumImplementingInterface.getName() + "' does not implement " + interfaceClass + "'.");
            return null;
        }

        // Get enums
        Object[] enums = enumImplementingInterface.getEnumConstants();
        return enums[0];
    }

    /**
     * Build a map from an enumeration class which implements a KeyProvider.
     *
     */
    public static <K extends Enum<K> & KeyProvider<T>, T> Map<T, K> buildMap(Class<K> enumClass) {
        Map<T, K> enumMap = new LinkedHashMap<>();
        for (K enumConstant : enumClass.getEnumConstants()) {
            enumMap.put(enumConstant.getKey(), enumConstant);
        }

        return enumMap;
    }

    public static <T, K extends Enum<K> & KeyProvider<T>> List<T> getKeys(Class<K> enumClass) {
        K[] enums = enumClass.getEnumConstants();

        List<T> resources = new ArrayList<>(enums.length);

        for (K anEnum : enums) {
            resources.add(anEnum.getKey());
        }

        return resources;
    }

    public static <T extends Enum<T>> T fromName(Class<T> enumType, String name) {
        return SpecsEnums.valueOf(enumType, name);
    }

    public static <T extends Enum<T>> T fromOrdinal(Class<T> enumClass, int ordinal) {
        EnumHelper<T> helper = getHelper(enumClass);
        return helper.fromOrdinal(ordinal);
    }

    @SuppressWarnings("unchecked") // Class<T> is a Class<Enum<?>>
    public static <T extends Enum<T>> EnumHelper<T> getHelper(Class<T> enumClass) {
        EnumHelper<?> helper = ENUM_HELPERS.get().get(enumClass);
        if (helper == null) {
            helper = new EnumHelper<>(enumClass);
            ENUM_HELPERS.get().put((Class<Enum<?>>) enumClass, helper);
        }

        return (EnumHelper<T>) helper;
    }

    /**
     * Converts a map with string keys to a map
     *
     */
    public static <T extends Enum<T>, R> EnumMap<T, R> toEnumMap(Class<T> enumClass,
            Map<String, R> map) {

        var enumMap = new EnumMap<T, R>(enumClass);

        for (var entry : map.entrySet()) {
            var key = entry.getKey();

            // Convert key to enum
            var enumValue = toEnumTry(enumClass, key).orElse(null);

            if (enumValue == null) {
                SpecsLogs.info("Could not map '" + key + "' to a predefined value, available values: "
                        + getHelper(enumClass).names());
                continue;
            }

            enumMap.put(enumValue, entry.getValue());
        }

        return enumMap;
    }

    /**
     * Uses enum helpers, supports interface StringProvider.
     *
     */
    private static <T extends Enum<T>> Optional<T> toEnumTry(Class<T> enumClass, String name) {
        return getHelper(enumClass).fromNameTry(name);
    }

}
