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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
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
            .withInitial(() -> new HashMap<>());

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

    public static <T extends Enum<T>> List<T> getValues(Class<T> enumType, List<String> names) {
        List<T> values = new ArrayList<>();

        for (String name : names) {
            T value = valueOf(enumType, name);
            if (value == null) {
                continue;
            }

            values.add(value);
        }

        return values;
    }

    /**
     * Checks if a specific enum contains a constant with the given name.
     *
     * @param <T>      The Enum where the constant is
     * @param enumType the Class object of the enum type from which to return a
     *                 constant
     * @param name     the name of the constant to return
     * @return true if the Enum contains a constant with the same name, false
     *         otherwise
     */
    public static <T extends Enum<T>> boolean containsEnum(Class<T> enumType, String name) {
        T enumeration = valueOf(enumType, name);
        if (enumeration == null) {
            return false;
        }

        return true;

    }

    /**
     * Builds an unmmodifiable table which maps the string representation of the
     * enum to the enum itself.
     *
     * <p>
     * This table can be useful to get the enum correspondent to a particular option
     * in String format which was collected from, for example, a config file.
     *
     * @param <K>
     * @param values
     * @return
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
     * @param <K>
     * @param values
     * @return
     */
    public static <K extends Enum<K>> Map<String, K> buildNamesMap(Class<K> enumClass, Collection<K> excludeList) {
        Map<String, K> aMap = new LinkedHashMap<>();

        Function<K, String> toString = StringProvider.class.isAssignableFrom(enumClass)
                ? anEnum -> ((StringProvider) anEnum).getString()
                : anEnum -> anEnum.name();

        var excludeSet = new HashSet<K>(excludeList);

        for (K enume : enumClass.getEnumConstants()) {
            if (excludeSet.contains(enume)) {
                continue;
            }

            aMap.put(toString.apply(enume), enume);
        }

        return Collections.unmodifiableMap(aMap);
    }

    /**
     *
     * @param <K>
     * @param values
     * @return a list with the names of the enums
     */
    public static <K extends Enum<K>> List<String> buildList(K[] values) {
        List<String> aList = new ArrayList<>();
        for (K anEnum : values) {
            aList.add(anEnum.name());
        }

        return aList;
    }

    public static <K extends Enum<K>> List<String> buildListToString(Class<K> enumClass) {
        return buildListToString(enumClass.getEnumConstants());
    }

    /**
     *
     * @param <K>
     * @param values
     * @return a list with the string representation of the enums
     */
    public static <K extends Enum<K>> List<String> buildListToString(K[] values) {
        List<String> aList = new ArrayList<>();
        for (K anEnum : values) {
            aList.add(anEnum.toString());
        }

        return aList;
    }

    /**
     * Returns the class of the enum correspondent to the values of the given array.
     *
     * @param <K>
     * @param values
     * @return the class correspondent to the given array of enums
     */
    public static <K extends Enum<K>> Class<?> getClass(K[] values) {
        if (values.length == 0) {
            SpecsLogs.getLogger().warning("Given array is empty");
            return null;
        }

        return values[0].getClass();
    }

    public static <T> List<T> extractValues(List<Class<? extends T>> enumClasses) {
        return enumClasses.stream()
                .map(anEnumClass -> extractValues(anEnumClass))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    /**
     * If the class represents an enum, returns a list with the values of that enum.
     * Otherwise, returns null.
     *
     * @param anEnumClass
     * @return
     */
    public static <T> List<T> extractValues(Class<? extends T> anEnumClass) {
        // Check class
        if (!anEnumClass.isEnum()) {
            SpecsLogs.getLogger().warning("Class '" + anEnumClass.getName() + "' does not represent an enum.");
            return null;
        }

        // Get enums
        T[] enumKeys = anEnumClass.getEnumConstants();
        return Arrays.asList(enumKeys);
    }

    public static <T extends Enum<T>> List<T> extractValuesV2(Class<? extends T> anEnumClass) {
        // Get enums
        T[] enumKeys = anEnumClass.getEnumConstants();
        return Arrays.asList(enumKeys);
    }

    /**
     * If the class represents an enum, returns a list of Strings with the names of
     * the values of that enum. Otherwise, returns null.
     *
     * @param anEnumClass
     * @return
     */
    public static <T extends Enum<T>> List<String> extractNames(Class<? extends T> anEnumClass) {
        List<T> values = extractValues(anEnumClass);

        List<String> names = new ArrayList<>();

        for (T value : values) {
            names.add(value.name());
        }

        return names;
    }

    /**
     * Extracts an instance of an interface from a class which represents an Enum
     * which implements such interface.
     *
     * @param enumSetupDefiner
     */
    public static <E extends Enum<E>> Object getInterfaceFromEnum(Class<E> enumImplementingInterface,
            Class<?> interfaceClass) {
        // Build set with interfaces of the given class
        Class<?>[] interfacesArray = enumImplementingInterface.getInterfaces();
        List<Class<?>> interfacesList = Arrays.asList(interfacesArray);
        Set<Class<?>> interfaces = new HashSet<>(interfacesList);

        if (!interfaces.contains(interfaceClass)) {
            SpecsLogs.getLogger().warning(
                    "Class '" + enumImplementingInterface.getName() + "' does not implement " + interfaceClass + "'.");
            return null;
        }

        // Get enums
        Object[] enums = enumImplementingInterface.getEnumConstants();
        return enums[0];
    }

    /**
     *
     * <p>
     * The following code can be used to dump the complement collection into a newly
     * allocated array:
     * <p>
     * AnEnum[] y = EnumUtils.getComplement(new AnEnum[0], anEnum1, anEnum2);
     *
     * @param <K>
     * @param a      a - the array into which the elements of this set are to be
     *               stored, if it is big enough; otherwise, a
     *               new array of the same runtime type is allocated for this
     *               purpose.
     * @param values
     * @return
     */
    public static <K extends Enum<K>> K[] getComplement(K[] a, List<K> values) {
        EnumSet<K> complementSet = SpecsEnums.getComplement(values);
        return complementSet.toArray(a);
    }

    public static <K extends Enum<K>> EnumSet<K> getComplement(List<K> values) {
        EnumSet<K> originalSet = EnumSet.copyOf(values);
        EnumSet<K> complementSet = EnumSet.complementOf(originalSet);

        return complementSet;
    }

    /**
     * Build a map from an enumeration class which implements a KeyProvider.
     *
     * @param enumClass
     * @return
     */
    public static <K extends Enum<K> & KeyProvider<T>, T> Map<T, K> buildMap(Class<K> enumClass) {
        Map<T, K> enumMap = new LinkedHashMap<>();
        for (K enumConstant : enumClass.getEnumConstants()) {
            enumMap.put(enumConstant.getKey(), enumConstant);
        }

        return enumMap;
    }

    /**
     * Returns the first enumeration of a class than extends enum.
     *
     * <p>
     * If the given class has no enums, throws a Runtime Exception.
     *
     * @param anEnumClass
     * @return
     */
    public static <T extends Enum<T>> T getFirstEnum(Class<T> anEnumClass) {

        T enums[] = anEnumClass.getEnumConstants();

        if (enums.length == 0) {
            throw new RuntimeException("Class '" + anEnumClass + "' has no enum values.");
        }

        return enums[0];
    }

    public static <T, K extends Enum<K> & KeyProvider<T>> List<T> getKeys(Class<K> enumClass) {
        K[] enums = enumClass.getEnumConstants();

        List<T> resources = new ArrayList<>(enums.length);

        for (K anEnum : enums) {
            resources.add(anEnum.getKey());
        }

        return resources;
    }

    /**
     * Returns a string representing the enum options using ',' as delimiter and '['
     * and ']' and prefix and suffix, respectively.
     *
     * @param anEnumClass
     * @return
     */
    public static <E extends Enum<E>> String getEnumOptions(Class<E> anEnumClass) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");

        for (E anEnum : anEnumClass.getEnumConstants()) {
            joiner.add(anEnum.name().toLowerCase());
        }

        return joiner.toString();
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

    public static <T extends Enum<T>> T[] values(Class<T> enumClass) {
        return getHelper(enumClass).values();
    }

    /**
     *
     * @param <T>
     * @param anEnum
     * @return the next enum, according to the ordinal order, or the first enum if
     *         this one is the last
     */
    public static <T extends Enum<T>> T nextEnum(T anEnum) {
        @SuppressWarnings("unchecked")
        var enums = ((Class<T>) anEnum.getClass()).getEnumConstants();

        var ordinal = anEnum.ordinal();

        if (ordinal < enums.length - 1) {
            return enums[ordinal + 1];
        }

        return enums[0];
    }

    /**
     * Converts a map with string keys to a map
     *
     * @param <T>
     * @param <R>
     * @param enumClass
     * @param map
     * @return
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
     * @param enumClass
     * @param value
     */
    public static <T extends Enum<T>> Optional<T> toEnumTry(Class<T> enumClass, String name) {
        return getHelper(enumClass).fromNameTry(name);
    }

}
