/*
 * Copyright 2019 SPeCS Research Group.
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

import pt.up.fe.specs.util.classmap.BiFunctionClassMap;
import pt.up.fe.specs.util.classmap.ClassMap;

/**
 * Utility methods for number operations.
 * <p>
 * Provides static helper methods for parsing, formatting, and converting
 * numbers.
 * </p>
 *
 * @author Joao Bispo
 */
public class SpecsNumbers {

    /**
     * A map of number classes to their zero values.
     */
    private static final ClassMap<Number, Number> ZEROS;
    static {
        ZEROS = new ClassMap<>();
        ZEROS.put(Integer.class, 0);
        ZEROS.put(Long.class, 0l);
        ZEROS.put(Float.class, 0.0f);
        ZEROS.put(Double.class, 0.0);
    }

    /**
     * A map of number classes to their addition operations.
     */
    private static final BiFunctionClassMap<Number, Number, Number> ADD;
    static {
        ADD = new BiFunctionClassMap<>();
        ADD.put(Integer.class, (number1, number2) -> Integer.valueOf(number1.intValue() + number2.intValue()));
        ADD.put(Long.class, (number1, number2) -> Long.valueOf(number1.longValue() + number2.longValue()));
        ADD.put(Float.class, (number1, number2) -> Float.valueOf(number1.floatValue() + number2.floatValue()));
        ADD.put(Double.class, (number1, number2) -> Double.valueOf(number1.doubleValue() + number2.doubleValue()));
    }

    /**
     * Returns the zero value for the given number class.
     *
     * @param numberClass the class of the number
     * @return the zero value for the given number class
     */
    public static Number zero(Class<? extends Number> numberClass) {
        return ZEROS.get(numberClass);
    }

    /**
     * Adds two numbers of the same type.
     *
     * @param <N>     the type of the numbers
     * @param number1 the first number
     * @param number2 the second number
     * @return the result of adding the two numbers
     */
    @SuppressWarnings("unchecked") // Functions should return correct number
    public static <N extends Number> N add(N number1, N number2) {
        return (N) ADD.apply(number1, number2);
    }

}
