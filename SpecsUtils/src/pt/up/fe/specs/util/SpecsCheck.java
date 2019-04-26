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

package pt.up.fe.specs.util;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Utility class with methods for checkers.
 * 
 * @author JoaoBispo
 *
 */
public class SpecsCheck {

    private SpecsCheck() {
    }

    public static void checkArgument(boolean expression, Supplier<String> supplier) {
        if (!expression) {
            throw new IllegalArgumentException(String.valueOf(supplier.get()));
        }
    }

    public static <T> T checkNotNull(T reference, Supplier<String> supplier) {
        if (reference == null) {
            throw new NullPointerException(supplier.get());
        }

        return reference;
    }

    public static void checkSize(Collection<?> collection, int expectedSize) {
        checkSize(expectedSize, collection.size());
    }

    public static void checkSize(Object[] objects, int expectedSize) {
        checkSize(expectedSize, objects.length);
    }

    private static void checkSize(int expectedSize, int actualSize) {
        if (actualSize != expectedSize) {
            throw new IllegalArgumentException("Expected collection to have size '" + expectedSize
                    + "', its current size is '" + actualSize + "'");
        }
    }

    public static void checkSizeRange(Collection<?> collection, int minSize, int maxSize) {
        checkSizeRange(minSize, maxSize, collection.size());
    }

    public static void checkSizeRange(Object[] objects, int minSize, int maxSize) {
        checkSizeRange(minSize, maxSize, objects.length);
    }

    private static void checkSizeRange(int minSize, int maxSize, int actualSize) {
        if (actualSize < minSize || actualSize > maxSize) {
            throw new IllegalArgumentException(
                    "Expected collection to have size between '" + minSize + "' and '" + maxSize + "'"
                            + "', its current size is '" + actualSize + "'");
        }
    }

}
