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

package pt.up.fe.specs.util.utilities;

import java.util.Collection;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsMath;

/**
 * Calculates several types of averages.
 *
 * @author Joao Bispo
 */
public enum AverageType {
    ARITHMETIC_MEAN(false),
    ARITHMETIC_MEAN_WITHOUT_ZEROS(true),
    GEOMETRIC_MEAN(false),
    GEOMETRIC_MEAN_WITHOUT_ZEROS(true),
    HARMONIC_MEAN(false);

    AverageType(boolean ignoresZeros) {
        this.ignoresZeros = ignoresZeros;
    }

    public boolean ignoresZeros() {
        return this.ignoresZeros;
    }

    public double calcAverage(Collection<Number> values) {
        if (values == null || values.isEmpty()) {
            return 0.0;
        }

        // Check if all values are zero (for specific handling)
        boolean allZeros = values.stream().allMatch(n -> n.doubleValue() == 0.0);
        // Check if any values are zero (for geometric mean)
        boolean hasZeros = values.stream().anyMatch(n -> n.doubleValue() == 0.0);

        switch (this) {
            case ARITHMETIC_MEAN:
                return SpecsMath.arithmeticMean(values);

            case ARITHMETIC_MEAN_WITHOUT_ZEROS:
                if (allZeros) {
                    return 0.0; // Mathematically correct: mean of zeros is zero
                }
                Double result = SpecsMath.arithmeticMeanWithoutZeros(values);
                return result != null ? result : 0.0; // Handle null returns safely

            case GEOMETRIC_MEAN:
                if (hasZeros) {
                    return 0.0; // Geometric mean is 0 if any value is 0
                }
                // Handle large datasets that could cause overflow
                if (values.size() > 1000) {
                    return calculateGeometricMeanSafe(values, false);
                }
                return SpecsMath.geometricMean(values, false);

            case GEOMETRIC_MEAN_WITHOUT_ZEROS:
                if (allZeros) {
                    return 0.0; // No non-zero values to calculate
                }
                // Handle large datasets that could cause overflow
                if (values.size() > 1000) {
                    return calculateGeometricMeanSafe(values, true);
                }
                return SpecsMath.geometricMean(values, true);

            case HARMONIC_MEAN:
                if (hasZeros) {
                    return 0.0; // Harmonic mean is 0 if any value is 0
                }
                return SpecsMath.harmonicMean(values, true);

            default:
                SpecsLogs.getLogger().warning("Case not implemented: '" + this + "'");
                return 0.0;
        }
    }

    /**
     * Calculates geometric mean using logarithmic approach to avoid overflow
     * with large datasets.
     * 
     * @param values       the collection of numbers
     * @param withoutZeros if true, excludes zeros from calculation
     * @return the geometric mean
     */
    private double calculateGeometricMeanSafe(Collection<Number> values, boolean withoutZeros) {
        double sumOfLogs = 0.0;
        int validCount = 0;

        for (Number value : values) {
            double d = value.doubleValue();

            // Skip zeros if withoutZeros is true
            if (d == 0.0 && withoutZeros) {
                continue;
            }

            // Skip negative values and zeros (geometric mean undefined for negatives, zero
            // for zeros)
            if (d > 0.0) {
                sumOfLogs += Math.log(d);
                validCount++;
            }
        }

        if (validCount == 0) {
            return 0.0;
        }

        // Use appropriate count based on withoutZeros flag (matching SpecsMath
        // behavior)
        int denominator = withoutZeros ? validCount : values.size();
        return Math.exp(sumOfLogs / denominator);
    }

    private final boolean ignoresZeros;
}
