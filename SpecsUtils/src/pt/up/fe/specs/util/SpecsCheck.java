/*
 * Copyright 2018 SPeCS Research Group.
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

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Supplier;

/**
 * Utility methods for runtime checks and assertions.
 * <p>
 * Provides static helper methods for validating arguments, states, and error conditions at runtime.
 * </p>
 *
 * @author Joao Bispo
 */
public class SpecsCheck {

    private SpecsCheck() {
    }

    /**
     * Validates that a given expression is true. Throws an IllegalArgumentException if the expression is false.
     *
     * @param expression the condition to validate
     * @param supplier   a supplier providing the error message
     */
    public static void checkArgument(boolean expression, Supplier<String> supplier) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(supplier.get()));
        }
    }

    /**
     * Ensures that the given reference is not null. Throws a NullPointerException if the reference is null.
     *
     * @param reference the object to check for nullity
     * @param supplier  a supplier providing the error message
     * @param <T>       the type of the reference
     * @return the non-null reference
     */
    public static <T> T checkNotNull(T reference, Supplier<String> supplier) {
        if (reference == null) {
            throw new NullPointerException(supplier.get());
        }

        return reference;
    }

    /**
     * Validates that the size of the given collection matches the expected size. Throws an IllegalArgumentException if
     * the sizes do not match.
     *
     * @param collection   the collection to check
     * @param expectedSize the expected size of the collection
     */
    public static void checkSize(Collection<?> collection, int expectedSize) {
        checkSize(expectedSize, collection.size(), () -> collection.toString());
    }

    /**
     * Validates that the size of the given array matches the expected size. Throws an IllegalArgumentException if the
     * sizes do not match.
     *
     * @param objects      the array to check
     * @param expectedSize the expected size of the array
     */
    public static void checkSize(Object[] objects, int expectedSize) {
        checkSize(expectedSize, objects.length, () -> Arrays.toString(objects));
    }

    /**
     * Validates that the size of a collection or array matches the expected size. Throws an IllegalArgumentException if
     * the sizes do not match.
     *
     * @param expectedSize      the expected size
     * @param actualSize        the actual size
     * @param collectionContents a supplier providing the contents of the collection or array
     */
    private static void checkSize(int expectedSize, int actualSize, Supplier<String> collectionContents) {
        if (actualSize != expectedSize) {
            throw new IllegalArgumentException("Expected collection to have size '" + expectedSize
                    + "', its current size is '" + actualSize + "'.\nCollection:" + collectionContents.get());
        }
    }

    /**
     * Validates that the size of the given collection is within the specified range. Throws an IllegalArgumentException
     * if the size is outside the range.
     *
     * @param collection the collection to check
     * @param minSize    the minimum size
     * @param maxSize    the maximum size
     */
    public static void checkSizeRange(Collection<?> collection, int minSize, int maxSize) {
        checkSizeRange(minSize, maxSize, collection.size(), () -> collection.toString());
    }

    /**
     * Validates that the size of the given array is within the specified range. Throws an IllegalArgumentException if
     * the size is outside the range.
     *
     * @param objects  the array to check
     * @param minSize  the minimum size
     * @param maxSize  the maximum size
     */
    public static void checkSizeRange(Object[] objects, int minSize, int maxSize) {
        checkSizeRange(minSize, maxSize, objects.length, () -> Arrays.toString(objects));
    }

    /**
     * Validates that the size of a collection or array is within the specified range. Throws an IllegalArgumentException
     * if the size is outside the range.
     *
     * @param minSize           the minimum size
     * @param maxSize           the maximum size
     * @param actualSize        the actual size
     * @param collectionContents a supplier providing the contents of the collection or array
     */
    private static void checkSizeRange(int minSize, int maxSize, int actualSize, Supplier<String> collectionContents) {
        if (actualSize < minSize || actualSize > maxSize) {
            throw new IllegalArgumentException(
                    "Expected collection to have size between '" + minSize + "' and '" + maxSize + "'"
                            + "', its current size is '" + actualSize + "'\nCollection:" + collectionContents.get());
        }
    }

    /**
     * Validates that the given value is an instance of the specified class. Throws an IllegalArgumentException if the
     * value is not an instance of the class.
     *
     * @param value  the object to check
     * @param aClass the class to check against
     */
    public static void checkClass(Object value, Class<?> aClass) {
        SpecsCheck.checkArgument(aClass.isInstance(value),
                () -> "Expected value to be an instance of " + aClass + ", however it is a " + value.getClass());
    }

}
