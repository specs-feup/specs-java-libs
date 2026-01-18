/*
 * Copyright 2011 SPeCS Research Group.
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
import java.util.List;

/**
 * Utility methods for mathematical operations.
 * <p>
 * Provides static helper methods for arithmetic, rounding, and other
 * math-related tasks.
 * </p>
 *
 * @author Joao Bispo
 */
public class SpecsMath {

    /**
     * Calculates the ratio of zero values in a collection of numbers.
     *
     * @param values the collection of numbers
     * @return the ratio of zero values
     */
    public static double zeroRatio(Collection<Number> values) {
        return zeroRatio(values, 0.0);
    }

    /**
     * Calculates the ratio of values below a given threshold in a collection of
     * numbers.
     *
     * @param values    the collection of numbers
     * @param threshold the threshold value
     * @return the ratio of values below the threshold
     */
    public static double zeroRatio(Collection<Number> values, double threshold) {
        double numZeros = 0;

        for (Number value : values) {
            if (!(value.doubleValue() > threshold)) {
                numZeros++;
            }
        }

        return numZeros / values.size();
    }

    /**
     * Calculates the arithmetic mean of a collection of numbers.
     *
     * @param values the collection of numbers
     * @return the arithmetic mean
     */
    public static double arithmeticMean(Collection<? extends Number> values) {
        if (values.isEmpty()) {
            return 0;
        }

        double result = 0;

        for (Number value : values) {
            result += value.doubleValue();
        }

        result /= values.size();

        return result;
    }

    /**
     * Calculates the arithmetic mean of a collection of numbers, excluding zero
     * values.
     *
     * @param values the collection of numbers
     * @return the arithmetic mean excluding zeros, or null if the collection is
     *         empty
     */
    public static Double arithmeticMeanWithoutZeros(Collection<Number> values) {
        if (values.isEmpty()) {
            return null;
        }

        double result = 0;

        int numZeros = 0;
        for (Number value : values) {
            if (!(value.doubleValue() > 0.0) && !(value.doubleValue() < 0.0)) {
                numZeros++;
                continue;
            }

            result += value.doubleValue();
        }

        int numElements = values.size() - numZeros;
        if (numElements == 0) {
            return 0d;
        }

        result /= numElements;

        return result;
    }

    /**
     * Calculates the geometric mean of a collection of numbers.
     *
     * @param values       the collection of numbers
     * @param withoutZeros if false, performs geometric mean with zero correction;
     *                     otherwise, ignores zero values
     * @return the geometric mean
     */
    public static double geometricMean(Collection<Number> values, boolean withoutZeros) {
        double result = 1;

        int zeros = 0;
        for (Number value : values) {
            if (!(value.doubleValue() > 0.0) && !(value.doubleValue() < 0.0)) {
                zeros++;
                continue;
            }
            result *= value.doubleValue();
        }

        int numberOfElements;
        if (withoutZeros) {
            numberOfElements = values.size() - zeros;
        } else {
            numberOfElements = values.size();
        }

        double power = (double) 1 / (double) numberOfElements;
        result = Math.pow(result, power);

        return result;
    }

    /**
     * Calculates the harmonic mean of a collection of numbers.
     *
     * @param values            the collection of numbers
     * @param useZeroCorrection if true, applies zero correction to the harmonic
     *                          mean calculation
     * @return the harmonic mean
     */
    public static double harmonicMean(Collection<Number> values, boolean useZeroCorrection) {
        double result = 0;
        int zeros = 0;
        for (Number value : values) {
            if (!(value.doubleValue() > 0.0) && !(value.doubleValue() < 0.0)) {
                zeros++;
                continue;
            }
            result += 1 / value.doubleValue();
        }

        int numberOfElements = values.size() - zeros;

        if (numberOfElements == 0) {
            return 0.0;
        }

        result = numberOfElements / result;

        if (useZeroCorrection) {
            result *= (double) numberOfElements / (double) values.size();
        }

        return result;
    }

    /**
     * Finds the maximum value in a list of numbers.
     *
     * @param values      the list of numbers
     * @param ignoreZeros if true, ignores zero values in the calculation
     * @return the maximum value, or null if the list is null or empty
     */
    public static Number max(List<? extends Number> values, boolean ignoreZeros) {
        if (values == null) {
            return null;
        }

        if (values.isEmpty()) {
            return null;
        }

        Number max = Double.MAX_VALUE * -1;

        int zeros = 0;
        for (Number number : values) {
            if (!(number.doubleValue() > 0.0) && !(number.doubleValue() < 0.0)) {
                zeros++;
                if (ignoreZeros) {
                    continue;
                }
            }

            max = Math.max(max.doubleValue(), number.doubleValue());
        }

        if (zeros == values.size() && ignoreZeros) {
            return 0;
        }

        return max;
    }

    /**
     * Finds the minimum value in a list of numbers.
     *
     * @param values      the list of numbers
     * @param ignoreZeros if true, ignores zero values in the calculation
     * @return the minimum value, or null if the list is null or empty
     */
    public static Number min(List<? extends Number> values, boolean ignoreZeros) {
        if (values == null) {
            return null;
        }

        if (values.isEmpty()) {
            return null;
        }

        Number min = Double.MAX_VALUE;

        int zeros = 0;
        for (Number number : values) {
            if (!(number.doubleValue() > 0.0) && !(number.doubleValue() < 0.0)) {
                zeros++;
                if (ignoreZeros) {
                    continue;
                }
            }

            min = Math.min(min.doubleValue(), number.doubleValue());
        }

        if (zeros == values.size() && ignoreZeros) {
            return 0;
        }

        return min;
    }

    /**
     * Calculates the sum of a list of numbers.
     *
     * @param numbers the list of numbers
     * @return the sum of the numbers
     */
    public static double sum(List<? extends Number> numbers) {
        double acc = 0;
        for (Number number : numbers) {
            acc += number.doubleValue();
        }

        return acc;
    }

    /**
     * Calculates the product of a list of numbers.
     *
     * @param numbers the list of numbers
     * @return the product of the numbers
     */
    public static double multiply(List<? extends Number> numbers) {
        double acc = 1;
        for (Number number : numbers) {
            acc *= number.doubleValue();
        }

        return acc;
    }

    /**
     * Calculates the factorial of a number.
     *
     * @param number the number
     * @return the factorial of the number
     */
    public static long factorial(int number) {
        long result = 1;
        boolean isNegative = number < 0;

        if (isNegative) {
            number = -number; // Convert to positive for factorial calculation
        }

        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }

        return isNegative ? -result : result;
    }
}
