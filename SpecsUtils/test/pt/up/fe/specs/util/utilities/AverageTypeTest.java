package pt.up.fe.specs.util.utilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link AverageType} enum.
 * Tests different types of average calculations with various data sets.
 * 
 * @author Generated Tests
 */
class AverageTypeTest {

    @Nested
    @DisplayName("Enum Properties and Constants")
    class EnumPropertiesTests {

        @Test
        @DisplayName("Should have all expected enum values")
        void testEnumValues() {
            AverageType[] values = AverageType.values();

            assertThat(values).hasSize(5);
            assertThat(values).containsExactlyInAnyOrder(
                    AverageType.ARITHMETIC_MEAN,
                    AverageType.ARITHMETIC_MEAN_WITHOUT_ZEROS,
                    AverageType.GEOMETRIC_MEAN,
                    AverageType.GEOMETRIC_MEAN_WITHOUT_ZEROS,
                    AverageType.HARMONIC_MEAN);
        }

        @Test
        @DisplayName("Should correctly identify which types ignore zeros")
        void testIgnoresZerosProperty() {
            assertThat(AverageType.ARITHMETIC_MEAN.ignoresZeros()).isFalse();
            assertThat(AverageType.ARITHMETIC_MEAN_WITHOUT_ZEROS.ignoresZeros()).isTrue();
            assertThat(AverageType.GEOMETRIC_MEAN.ignoresZeros()).isFalse();
            assertThat(AverageType.GEOMETRIC_MEAN_WITHOUT_ZEROS.ignoresZeros()).isTrue();
            assertThat(AverageType.HARMONIC_MEAN.ignoresZeros()).isFalse();
        }

        @Test
        @DisplayName("Should maintain enum consistency")
        void testEnumConsistency() {
            for (AverageType type : AverageType.values()) {
                assertThat(type.name()).isNotNull();
                assertThat(type.ordinal()).isGreaterThanOrEqualTo(0);
            }
        }
    }

    @Nested
    @DisplayName("Arithmetic Mean Calculations")
    class ArithmeticMeanTests {

        @Test
        @DisplayName("Should calculate arithmetic mean correctly")
        void testArithmeticMean() {
            List<Number> values = Arrays.asList(1, 2, 3, 4, 5);

            double result = AverageType.ARITHMETIC_MEAN.calcAverage(values);

            assertThat(result).isCloseTo(3.0, within(0.001));
        }

        @Test
        @DisplayName("Should handle zeros in arithmetic mean")
        void testArithmeticMeanWithZeros() {
            List<Number> values = Arrays.asList(0, 2, 4, 0, 6);

            double result = AverageType.ARITHMETIC_MEAN.calcAverage(values);

            assertThat(result).isCloseTo(2.4, within(0.001));
        }

        @Test
        @DisplayName("Should calculate arithmetic mean without zeros")
        void testArithmeticMeanWithoutZeros() {
            List<Number> values = Arrays.asList(0, 2, 4, 0, 6);

            double result = AverageType.ARITHMETIC_MEAN_WITHOUT_ZEROS.calcAverage(values);

            assertThat(result).isCloseTo(4.0, within(0.001)); // (2+4+6)/3 = 4
        }

        @Test
        @DisplayName("Should handle empty collection in arithmetic mean")
        void testArithmeticMeanEmptyCollection() {
            Collection<Number> emptyValues = Collections.emptyList();

            double result = AverageType.ARITHMETIC_MEAN.calcAverage(emptyValues);

            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should handle single value in arithmetic mean")
        void testArithmeticMeanSingleValue() {
            List<Number> values = Arrays.asList(42);

            double result = AverageType.ARITHMETIC_MEAN.calcAverage(values);

            assertThat(result).isCloseTo(42.0, within(0.001));
        }

        @Test
        @DisplayName("Should handle negative values in arithmetic mean")
        void testArithmeticMeanNegativeValues() {
            List<Number> values = Arrays.asList(-2, -1, 0, 1, 2);

            double result = AverageType.ARITHMETIC_MEAN.calcAverage(values);

            assertThat(result).isCloseTo(0.0, within(0.001));
        }

        @Test
        @DisplayName("Should handle decimal values in arithmetic mean")
        void testArithmeticMeanDecimalValues() {
            List<Number> values = Arrays.asList(1.5, 2.5, 3.5);

            double result = AverageType.ARITHMETIC_MEAN.calcAverage(values);

            assertThat(result).isCloseTo(2.5, within(0.001));
        }
    }

    @Nested
    @DisplayName("Geometric Mean Calculations")
    class GeometricMeanTests {

        @Test
        @DisplayName("Should calculate geometric mean correctly")
        void testGeometricMean() {
            List<Number> values = Arrays.asList(1, 2, 8);

            double result = AverageType.GEOMETRIC_MEAN.calcAverage(values);

            // Geometric mean of [1, 2, 8] = cube root of (1 × 2 × 8) = cube root of 16 ≈
            // 2.52
            // The implementation is actually correct
            assertThat(result).isCloseTo(2.5198420997897464, within(0.001));
        }

        @Test
        @DisplayName("Should handle geometric mean with zeros")
        void testGeometricMeanWithZeros() {
            List<Number> values = Arrays.asList(0, 2, 4, 8);

            double result = AverageType.GEOMETRIC_MEAN.calcAverage(values);

            // Geometric mean with zeros should be 0.0
            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should calculate geometric mean without zeros")
        void testGeometricMeanWithoutZeros() {
            List<Number> values = Arrays.asList(0, 2, 4, 8);

            double result = AverageType.GEOMETRIC_MEAN_WITHOUT_ZEROS.calcAverage(values);

            // Geometric mean of 2, 4, 8 = cube root of 64 = 4
            assertThat(result).isCloseTo(4.0, within(0.1));
        }

        @Test
        @DisplayName("Should handle single value in geometric mean")
        void testGeometricMeanSingleValue() {
            List<Number> values = Arrays.asList(16);

            double result = AverageType.GEOMETRIC_MEAN.calcAverage(values);

            assertThat(result).isCloseTo(16.0, within(0.001));
        }

        @Test
        @DisplayName("Should handle geometric mean of equal values")
        void testGeometricMeanEqualValues() {
            List<Number> values = Arrays.asList(5, 5, 5, 5);

            double result = AverageType.GEOMETRIC_MEAN.calcAverage(values);

            assertThat(result).isCloseTo(5.0, within(0.001));
        }
    }

    @Nested
    @DisplayName("Harmonic Mean Calculations")
    class HarmonicMeanTests {

        @Test
        @DisplayName("Should calculate harmonic mean correctly")
        void testHarmonicMean() {
            List<Number> values = Arrays.asList(1, 2, 4);

            double result = AverageType.HARMONIC_MEAN.calcAverage(values);

            // Harmonic mean of 1, 2, 4 = 3 / (1/1 + 1/2 + 1/4) = 3 / 1.75 ≈ 1.714
            assertThat(result).isCloseTo(1.714, within(0.01));
        }

        @Test
        @DisplayName("Should handle harmonic mean with zeros")
        void testHarmonicMeanWithZeros() {
            List<Number> values = Arrays.asList(0, 2, 4);

            double result = AverageType.HARMONIC_MEAN.calcAverage(values);

            // Harmonic mean with zeros should handle gracefully
            assertThat(result).isGreaterThanOrEqualTo(0.0);
        }

        @Test
        @DisplayName("Should handle single value in harmonic mean")
        void testHarmonicMeanSingleValue() {
            List<Number> values = Arrays.asList(10);

            double result = AverageType.HARMONIC_MEAN.calcAverage(values);

            assertThat(result).isCloseTo(10.0, within(0.001));
        }

        @Test
        @DisplayName("Should handle harmonic mean of equal values")
        void testHarmonicMeanEqualValues() {
            List<Number> values = Arrays.asList(6, 6, 6);

            double result = AverageType.HARMONIC_MEAN.calcAverage(values);

            assertThat(result).isCloseTo(6.0, within(0.001));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null collection")
        void testNullCollection() {
            for (AverageType type : AverageType.values()) {
                double result = type.calcAverage(null);
                assertThat(result).isEqualTo(0.0);
            }
        }

        @Test
        @DisplayName("Should handle empty collections")
        void testEmptyCollections() {
            Collection<Number> emptyCollection = Collections.emptyList();

            // Empty collections should consistently return 0.0 for all types
            assertThat(AverageType.ARITHMETIC_MEAN.calcAverage(emptyCollection)).isEqualTo(0.0);
            assertThat(AverageType.ARITHMETIC_MEAN_WITHOUT_ZEROS.calcAverage(emptyCollection)).isEqualTo(0.0);
            assertThat(AverageType.GEOMETRIC_MEAN.calcAverage(emptyCollection)).isEqualTo(0.0);
            assertThat(AverageType.GEOMETRIC_MEAN_WITHOUT_ZEROS.calcAverage(emptyCollection)).isEqualTo(0.0);
            assertThat(AverageType.HARMONIC_MEAN.calcAverage(emptyCollection)).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should handle collection with only zeros")
        void testCollectionWithOnlyZeros() {
            List<Number> zerosOnly = Arrays.asList(0, 0, 0, 0);

            // Zero-only collections should have mathematically correct behavior
            assertThat(AverageType.ARITHMETIC_MEAN.calcAverage(zerosOnly)).isEqualTo(0.0);
            assertThat(AverageType.ARITHMETIC_MEAN_WITHOUT_ZEROS.calcAverage(zerosOnly)).isEqualTo(0.0);
            assertThat(AverageType.GEOMETRIC_MEAN.calcAverage(zerosOnly)).isEqualTo(0.0); // should be 0.0, not 1.0
            assertThat(AverageType.GEOMETRIC_MEAN_WITHOUT_ZEROS.calcAverage(zerosOnly)).isEqualTo(0.0);
            assertThat(AverageType.HARMONIC_MEAN.calcAverage(zerosOnly)).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should handle very large numbers")
        void testVeryLargeNumbers() {
            List<Number> largeNumbers = Arrays.asList(1e6, 2e6, 3e6);

            double arithmeticResult = AverageType.ARITHMETIC_MEAN.calcAverage(largeNumbers);
            assertThat(arithmeticResult).isCloseTo(2e6, within(1e5));
        }

        @Test
        @DisplayName("Should handle very small numbers")
        void testVerySmallNumbers() {
            List<Number> smallNumbers = Arrays.asList(1e-6, 2e-6, 3e-6);

            double arithmeticResult = AverageType.ARITHMETIC_MEAN.calcAverage(smallNumbers);
            assertThat(arithmeticResult).isCloseTo(2e-6, within(1e-7));
        }

        @Test
        @DisplayName("Should handle mixed integer and double types")
        void testMixedNumberTypes() {
            List<Number> mixedNumbers = Arrays.asList(1, 2.5, 3, 4.5);

            double result = AverageType.ARITHMETIC_MEAN.calcAverage(mixedNumbers);

            assertThat(result).isCloseTo(2.75, within(0.001));
        }
    }

    @Nested
    @DisplayName("Comparison Between Average Types")
    class ComparisonTests {

        @Test
        @DisplayName("Should show differences between average types")
        void testAverageTypeComparison() {
            List<Number> values = Arrays.asList(1, 2, 3, 4, 5);

            double arithmetic = AverageType.ARITHMETIC_MEAN.calcAverage(values);
            double geometric = AverageType.GEOMETRIC_MEAN.calcAverage(values);
            double harmonic = AverageType.HARMONIC_MEAN.calcAverage(values);

            // For positive values: harmonic ≤ geometric ≤ arithmetic
            assertThat(harmonic).isLessThanOrEqualTo(geometric);
            assertThat(geometric).isLessThanOrEqualTo(arithmetic);

            // All should be reasonable values for this input
            assertThat(arithmetic).isCloseTo(3.0, within(0.001));
            assertThat(geometric).isCloseTo(2.605, within(0.01));
            assertThat(harmonic).isCloseTo(2.189, within(0.01));
        }

        @Test
        @DisplayName("Should show effect of zero-ignoring variants")
        void testZeroIgnoringVariants() {
            List<Number> valuesWithZeros = Arrays.asList(0, 0, 2, 4, 6);

            double arithmeticWithZeros = AverageType.ARITHMETIC_MEAN.calcAverage(valuesWithZeros);
            double arithmeticWithoutZeros = AverageType.ARITHMETIC_MEAN_WITHOUT_ZEROS.calcAverage(valuesWithZeros);

            double geometricWithZeros = AverageType.GEOMETRIC_MEAN.calcAverage(valuesWithZeros);
            double geometricWithoutZeros = AverageType.GEOMETRIC_MEAN_WITHOUT_ZEROS.calcAverage(valuesWithZeros);

            // Without zeros should generally be higher
            assertThat(arithmeticWithoutZeros).isGreaterThan(arithmeticWithZeros);
            assertThat(geometricWithoutZeros).isGreaterThan(geometricWithZeros);
        }

        @Test
        @DisplayName("Should handle identical values across all types")
        void testIdenticalValuesAllTypes() {
            List<Number> identicalValues = Arrays.asList(7, 7, 7, 7);

            for (AverageType type : AverageType.values()) {
                double result = type.calcAverage(identicalValues);
                assertThat(result).isCloseTo(7.0, within(0.001));
            }
        }
    }

    @Nested
    @DisplayName("Performance and Robustness")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle large datasets efficiently")
        void testLargeDatasets() {
            // Create a larger dataset - should now be stable with fixes
            List<Number> dataset = Collections.nCopies(10000, 5);

            // Large datasets should now have consistent behavior
            for (AverageType type : AverageType.values()) {
                try {
                    double result = type.calcAverage(dataset);
                    assertThat(result)
                            .as("Average type %s should return 5.0 for dataset of all 5s, but got %f", type, result)
                            .isCloseTo(5.0, within(0.001));
                } catch (Exception e) {
                    throw new AssertionError("Type " + type + " threw exception: " + e.getMessage(), e);
                }
            }
        }

        @Test
        @DisplayName("Should be consistent across multiple calls")
        void testConsistencyAcrossMultipleCalls() {
            List<Number> values = Arrays.asList(1.1, 2.2, 3.3, 4.4, 5.5);

            for (AverageType type : AverageType.values()) {
                double firstResult = type.calcAverage(values);
                double secondResult = type.calcAverage(values);
                double thirdResult = type.calcAverage(values);

                assertThat(firstResult).isEqualTo(secondResult);
                assertThat(secondResult).isEqualTo(thirdResult);
            }
        }
    }
}
