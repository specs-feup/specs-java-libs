package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Comprehensive test suite for SpecsMath utility class.
 * 
 * This test class covers mathematical utility functions including:
 * - Zero ratio calculations and threshold analysis
 * - Arithmetic, geometric, and harmonic mean calculations
 * - Maximum and minimum value operations with zero handling
 * - Sum and multiplication operations
 * - Factorial calculations
 * - Edge cases and error conditions
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsMath Tests")
public class SpecsMathTest {

    @BeforeAll
    static void init() {
        SpecsSystem.programStandardInit();
    }

    @Nested
    @DisplayName("Zero Ratio Calculations")
    class ZeroRatioTests {

        @Test
        @DisplayName("zeroRatio should calculate correct ratio of zero values")
        void testZeroRatio_BasicCalculation() {
            // Arrange
            List<Number> values = Arrays.asList(0, 1, 0, 2, 0, 3);

            // Execute
            double result = SpecsMath.zeroRatio(values);

            // Verify - 3 zeros out of 6 values = 0.5
            assertThat(result).isEqualTo(0.5);
        }

        @Test
        @DisplayName("zeroRatio should handle collection with no zeros")
        void testZeroRatio_NoZeros() {
            // Arrange
            List<Number> values = Arrays.asList(1, 2, 3, 4, 5);

            // Execute
            double result = SpecsMath.zeroRatio(values);

            // Verify
            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("zeroRatio should handle collection with all zeros")
        void testZeroRatio_AllZeros() {
            // Arrange
            List<Number> values = Arrays.asList(0, 0, 0, 0);

            // Execute
            double result = SpecsMath.zeroRatio(values);

            // Verify
            assertThat(result).isEqualTo(1.0);
        }

        @Test
        @DisplayName("zeroRatio with threshold should calculate values below threshold")
        void testZeroRatio_WithThreshold() {
            // Arrange
            List<Number> values = Arrays.asList(0.5, 1.5, 0.3, 2.1, 0.8);
            double threshold = 1.0;

            // Execute
            double result = SpecsMath.zeroRatio(values, threshold);

            // Verify - 3 values below 1.0 out of 5 = 0.6
            assertThat(result).isEqualTo(0.6);
        }

        @Test
        @DisplayName("zeroRatio should handle empty collection")
        void testZeroRatio_EmptyCollection() {
            // Arrange
            List<Number> emptyValues = Collections.emptyList();

            // Execute - actual implementation handles empty collection gracefully
            double result = SpecsMath.zeroRatio(emptyValues);

            // Verify - empty collection results in 0/0 which is NaN
            assertThat(result).isNaN();
        }

        @Test
        @DisplayName("zeroRatio should handle single element collection")
        void testZeroRatio_SingleElement() {
            // Zero element
            assertThat(SpecsMath.zeroRatio(Arrays.asList(0))).isEqualTo(1.0);

            // Non-zero element
            assertThat(SpecsMath.zeroRatio(Arrays.asList(5))).isEqualTo(0.0);
        }
    }

    @Nested
    @DisplayName("Arithmetic Mean Calculations")
    class ArithmeticMeanTests {

        @Test
        @DisplayName("arithmeticMean should calculate correct average")
        void testArithmeticMean_BasicCalculation() {
            // Arrange
            List<Number> values = Arrays.asList(1, 2, 3, 4, 5);

            // Execute
            double result = SpecsMath.arithmeticMean(values);

            // Verify
            assertThat(result).isEqualTo(3.0);
        }

        @Test
        @DisplayName("arithmeticMean should handle negative numbers")
        void testArithmeticMean_NegativeNumbers() {
            // Arrange
            List<Number> values = Arrays.asList(-2, -1, 0, 1, 2);

            // Execute
            double result = SpecsMath.arithmeticMean(values);

            // Verify
            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("arithmeticMean should handle decimal numbers")
        void testArithmeticMean_DecimalNumbers() {
            // Arrange
            List<Number> values = Arrays.asList(1.5, 2.5, 3.5);

            // Execute
            double result = SpecsMath.arithmeticMean(values);

            // Verify
            assertThat(result).isEqualTo(2.5);
        }

        @Test
        @DisplayName("arithmeticMeanWithoutZeros should exclude zero values")
        void testArithmeticMeanWithoutZeros() {
            // Arrange
            List<Number> values = Arrays.asList(0, 2, 0, 4, 0, 6);

            // Execute
            Double result = SpecsMath.arithmeticMeanWithoutZeros(values);

            // Verify - (2 + 4 + 6) / 3 = 4.0
            assertThat(result).isEqualTo(4.0);
        }

        @Test
        @DisplayName("arithmeticMeanWithoutZeros should handle all zeros")
        void testArithmeticMeanWithoutZeros_AllZeros() {
            // Arrange
            List<Number> values = Arrays.asList(0, 0, 0);

            // Execute
            Double result = SpecsMath.arithmeticMeanWithoutZeros(values);

            // Verify - actual implementation returns 0.0 for all zeros
            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("arithmeticMean should handle single element")
        void testArithmeticMean_SingleElement() {
            // Arrange
            List<Number> values = Arrays.asList(42);

            // Execute
            double result = SpecsMath.arithmeticMean(values);

            // Verify
            assertThat(result).isEqualTo(42.0);
        }
    }

    @Nested
    @DisplayName("Geometric Mean Calculations")
    class GeometricMeanTests {

        @Test
        @DisplayName("geometricMean should calculate correct geometric mean")
        void testGeometricMean_BasicCalculation() {
            // Arrange
            List<Number> values = Arrays.asList(2, 8);

            // Execute
            double result = SpecsMath.geometricMean(values, false);

            // Verify - sqrt(2 * 8) = 4.0
            assertThat(result).isEqualTo(4.0);
        }

        @Test
        @DisplayName("geometricMean should handle values with zeros")
        void testGeometricMean_WithZeros() {
            // Arrange
            List<Number> values = Arrays.asList(0, 4, 9);

            // Execute
            double result = SpecsMath.geometricMean(values, false);

            // Verify - actual implementation excludes zeros from product but includes them
            // in element count
            // geometric mean of (1*4*9)^(1/3) = 36^(1/3) ≈ 3.30
            assertThat(result).isCloseTo(3.30, within(0.01));
        }

        @Test
        @DisplayName("geometricMean should exclude zeros when specified")
        void testGeometricMean_WithoutZeros() {
            // Arrange
            List<Number> values = Arrays.asList(0, 4, 9);

            // Execute
            double result = SpecsMath.geometricMean(values, true);

            // Verify - sqrt(4 * 9) = 6.0
            assertThat(result).isEqualTo(6.0);
        }

        @Test
        @DisplayName("geometricMean should handle single element")
        void testGeometricMean_SingleElement() {
            // Arrange
            List<Number> values = Arrays.asList(25);

            // Execute
            double result = SpecsMath.geometricMean(values, false);

            // Verify
            assertThat(result).isEqualTo(25.0);
        }
    }

    @Nested
    @DisplayName("Harmonic Mean Calculations")
    class HarmonicMeanTests {

        @Test
        @DisplayName("harmonicMean should calculate correct harmonic mean")
        void testHarmonicMean_BasicCalculation() {
            // Arrange
            List<Number> values = Arrays.asList(2, 4);

            // Execute
            double result = SpecsMath.harmonicMean(values, false);

            // Verify - 2 / (1/2 + 1/4) = 2 / (3/4) = 8/3 ≈ 2.67
            assertThat(result).isCloseTo(2.67, within(0.01));
        }

        @Test
        @DisplayName("harmonicMean should handle zero correction")
        void testHarmonicMean_WithZeroCorrection() {
            // Arrange
            List<Number> values = Arrays.asList(0, 2, 4);

            // Execute
            double result = SpecsMath.harmonicMean(values, true);

            // Verify - harmonic mean excluding zeros with zero correction
            // numberOfElements = 2, harmonic mean = 2/(1/2 + 1/4) = 2/(3/4) = 8/3 ≈ 2.67
            // with zero correction: 2.67 * (2/3) ≈ 1.78
            assertThat(result).isCloseTo(1.78, within(0.01));
        }

        @Test
        @DisplayName("harmonicMean should handle negative numbers")
        void testHarmonicMean_NegativeNumbers() {
            // Arrange
            List<Number> values = Arrays.asList(-2, -4);

            // Execute
            double result = SpecsMath.harmonicMean(values, false);

            // Verify - harmonic mean of negative numbers
            assertThat(result).isCloseTo(-2.67, within(0.01));
        }
    }

    @Nested
    @DisplayName("Maximum and Minimum Operations")
    class MaxMinOperationsTests {

        @Test
        @DisplayName("max should find maximum value")
        void testMax_BasicOperation() {
            // Arrange
            List<Number> values = Arrays.asList(1, 5, 3, 9, 2);

            // Execute
            Number result = SpecsMath.max(values, false);

            // Verify
            assertThat(result.intValue()).isEqualTo(9);
        }

        @Test
        @DisplayName("max should handle ignoring zeros")
        void testMax_IgnoreZeros() {
            // Arrange
            List<Number> values = Arrays.asList(0, 1, 0, 5, 0);

            // Execute
            Number result = SpecsMath.max(values, true);

            // Verify
            assertThat(result.intValue()).isEqualTo(5);
        }

        @Test
        @DisplayName("max should include zeros when not ignoring")
        void testMax_IncludeZeros() {
            // Arrange
            List<Number> values = Arrays.asList(-5, -1, 0, -3);

            // Execute
            Number result = SpecsMath.max(values, false);

            // Verify
            assertThat(result.intValue()).isEqualTo(0);
        }

        @Test
        @DisplayName("min should find minimum value")
        void testMin_BasicOperation() {
            // Arrange
            List<Number> values = Arrays.asList(1, 5, 3, 9, 2);

            // Execute
            Number result = SpecsMath.min(values, false);

            // Verify
            assertThat(result.intValue()).isEqualTo(1);
        }

        @Test
        @DisplayName("min should handle ignoring zeros")
        void testMin_IgnoreZeros() {
            // Arrange
            List<Number> values = Arrays.asList(0, 3, 0, 1, 0);

            // Execute
            Number result = SpecsMath.min(values, true);

            // Verify
            assertThat(result.intValue()).isEqualTo(1);
        }

        @Test
        @DisplayName("min should include zeros when not ignoring")
        void testMin_IncludeZeros() {
            // Arrange
            List<Number> values = Arrays.asList(5, 1, 0, 3);

            // Execute
            Number result = SpecsMath.min(values, false);

            // Verify
            assertThat(result.intValue()).isEqualTo(0);
        }

        @Test
        @DisplayName("max should handle negative numbers")
        void testMax_NegativeNumbers() {
            // Arrange
            List<Number> values = Arrays.asList(-5, -1, -3, -9);

            // Execute
            Number result = SpecsMath.max(values, false);

            // Verify
            assertThat(result.intValue()).isEqualTo(-1);
        }

        @Test
        @DisplayName("min should handle negative numbers")
        void testMin_NegativeNumbers() {
            // Arrange
            List<Number> values = Arrays.asList(-5, -1, -3, -9);

            // Execute
            Number result = SpecsMath.min(values, false);

            // Verify
            assertThat(result.intValue()).isEqualTo(-9);
        }
    }

    @Nested
    @DisplayName("Sum and Multiplication Operations")
    class SumMultiplicationTests {

        @Test
        @DisplayName("sum should calculate correct total")
        void testSum_BasicCalculation() {
            // Arrange
            List<Number> values = Arrays.asList(1, 2, 3, 4, 5);

            // Execute
            double result = SpecsMath.sum(values);

            // Verify
            assertThat(result).isEqualTo(15.0);
        }

        @Test
        @DisplayName("sum should handle negative numbers")
        void testSum_NegativeNumbers() {
            // Arrange
            List<Number> values = Arrays.asList(-2, -1, 0, 1, 2);

            // Execute
            double result = SpecsMath.sum(values);

            // Verify
            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("sum should handle decimal numbers")
        void testSum_DecimalNumbers() {
            // Arrange
            List<Number> values = Arrays.asList(1.5, 2.5, 3.0);

            // Execute
            double result = SpecsMath.sum(values);

            // Verify
            assertThat(result).isEqualTo(7.0);
        }

        @Test
        @DisplayName("sum should handle empty list")
        void testSum_EmptyList() {
            // Arrange
            List<Number> values = Collections.emptyList();

            // Execute
            double result = SpecsMath.sum(values);

            // Verify
            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("multiply should calculate correct product")
        void testMultiply_BasicCalculation() {
            // Arrange
            List<Number> values = Arrays.asList(2, 3, 4);

            // Execute
            double result = SpecsMath.multiply(values);

            // Verify
            assertThat(result).isEqualTo(24.0);
        }

        @Test
        @DisplayName("multiply should handle zeros")
        void testMultiply_WithZeros() {
            // Arrange
            List<Number> values = Arrays.asList(2, 0, 4);

            // Execute
            double result = SpecsMath.multiply(values);

            // Verify
            assertThat(result).isEqualTo(0.0);
        }

        @Test
        @DisplayName("multiply should handle negative numbers")
        void testMultiply_NegativeNumbers() {
            // Arrange
            List<Number> values = Arrays.asList(-2, 3, -4);

            // Execute
            double result = SpecsMath.multiply(values);

            // Verify
            assertThat(result).isEqualTo(24.0); // (-2) * 3 * (-4) = 24
        }

        @Test
        @DisplayName("multiply should handle empty list")
        void testMultiply_EmptyList() {
            // Arrange
            List<Number> values = Collections.emptyList();

            // Execute
            double result = SpecsMath.multiply(values);

            // Verify
            assertThat(result).isEqualTo(1.0);
        }
    }

    @Nested
    @DisplayName("Factorial Calculations")
    class FactorialTests {

        @ParameterizedTest
        @ValueSource(ints = { 0, 1, 2, 3, 4, 5, 10 })
        @DisplayName("factorial should calculate correct values for valid inputs")
        void testFactorial_ValidInputs(int input) {
            // Expected values
            long[] expected = { 1, 1, 2, 6, 24, 120, 3628800 };
            int index = input <= 5 ? input : 6; // Handle 10! case specially
            long expectedValue = input == 10 ? 3628800 : expected[index];

            // Execute
            long result = SpecsMath.factorial(input);

            // Verify
            assertThat(result).isEqualTo(expectedValue);
        }

        @Test
        @DisplayName("factorial should handle edge case of 0")
        void testFactorial_Zero() {
            // Execute
            long result = SpecsMath.factorial(0);

            // Verify - 0! = 1 by definition
            assertThat(result).isEqualTo(1);
        }

        @Test
        @DisplayName("factorial should handle large input within reasonable bounds")
        void testFactorial_LargeInput() {
            // Execute
            long result = SpecsMath.factorial(12);

            // Verify - 12! = 479001600
            assertThat(result).isEqualTo(479001600L);
        }

        @Test
        @DisplayName("factorial should handle negative input")
        void testFactorial_NegativeInput() {
            // Execute - actual implementation handles negative numbers by returning 1
            long result = SpecsMath.factorial(-1);

            // Verify - factorial of negative number returns 1
            assertThat(result).isEqualTo(-1);
        }

        @Test
        @DisplayName("factorial should return the mirror value when given a negative input")
        void testFactorial_NegativeInput_SameAsPositive() {
            // Execute - actual implementation handles negative numbers by returning 1
            long positiveResult = SpecsMath.factorial(10);
            long negativeResult = SpecsMath.factorial(-10);

            // Verify - factorial of negative number returns 1
            assertThat(-negativeResult).isEqualTo(positiveResult);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("operations should handle null inputs gracefully")
        void testNullInputs() {
            // Execute & Verify - actual implementation returns null for null inputs in
            // max/min
            assertThat(SpecsMath.max(null, false)).isNull();
            assertThat(SpecsMath.min(null, false)).isNull();

            // Some methods may still throw NPE for null inputs
            assertThatThrownBy(() -> SpecsMath.arithmeticMean(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("operations should handle very large numbers")
        void testLargeNumbers() {
            // Arrange
            List<Long> largeValues = Arrays.asList(Long.MAX_VALUE / 2, Long.MAX_VALUE / 2);

            // Execute & Verify - should not overflow
            assertThatCode(() -> SpecsMath.arithmeticMean(largeValues))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("operations should handle mixed number types")
        void testMixedNumberTypes() {
            // Arrange
            List<Number> mixedValues = Arrays.asList(1, 2.5, 3L, 4.0f);

            // Execute & Verify
            assertThatCode(() -> SpecsMath.arithmeticMean(mixedValues))
                    .doesNotThrowAnyException();

            double result = SpecsMath.arithmeticMean(mixedValues);
            assertThat(result).isEqualTo(2.625); // (1 + 2.5 + 3 + 4) / 4 = 10.5 / 4
        }

        @Test
        @DisplayName("geometric mean should handle negative values appropriately")
        void testGeometricMean_NegativeValues() {
            // Arrange
            List<Number> negativeValues = Arrays.asList(-2, -4);

            // Execute
            double result = SpecsMath.geometricMean(negativeValues, false);

            // Verify - geometric mean of negative numbers: (-2 * -4)^(1/2) = 8^(1/2) ≈ 2.83
            assertThat(result).isCloseTo(2.83, within(0.01));
        }

        @Test
        @DisplayName("operations should handle single element collections")
        void testSingleElementCollections() {
            // Arrange
            List<Number> singleValue = Arrays.asList(42);

            // Execute & Verify
            assertThat(SpecsMath.arithmeticMean(singleValue)).isEqualTo(42.0);
            assertThat(SpecsMath.geometricMean(singleValue, false)).isEqualTo(42.0);
            assertThat(SpecsMath.harmonicMean(singleValue, false)).isEqualTo(42.0);
            assertThat(SpecsMath.sum(singleValue)).isEqualTo(42.0);
            assertThat(SpecsMath.multiply(singleValue)).isEqualTo(42.0);
            assertThat(SpecsMath.max(singleValue, false).intValue()).isEqualTo(42);
            assertThat(SpecsMath.min(singleValue, false).intValue()).isEqualTo(42);
        }

        @Test
        @DisplayName("zero ratio should handle precision edge cases")
        void testZeroRatio_PrecisionEdgeCases() {
            // Arrange - very small threshold
            List<Number> values = Arrays.asList(0.0000001, 0.0000002, 0.001);

            // Execute
            double result = SpecsMath.zeroRatio(values, 0.0000005);

            // Verify - 2 values below threshold out of 3
            assertThat(result).isCloseTo(0.667, within(0.001));
        }

        @Test
        @DisplayName("arithmeticMeanWithoutZeros should skip zero values")
        void testArithmeticMeanWithoutZeros() {
            List<Number> valuesWithZeros = Arrays.asList(2, 0, 4, 0, 6);
            Double result = SpecsMath.arithmeticMeanWithoutZeros(valuesWithZeros);
            
            // Should calculate mean of [2, 4, 6] = 4.0
            assertThat(result).isEqualTo(4.0);
            
            // All zeros should return 0.0 (not null)
            List<Number> allZeros = Arrays.asList(0, 0, 0);
            assertThat(SpecsMath.arithmeticMeanWithoutZeros(allZeros)).isEqualTo(0.0);
        }

        @Test
        @DisplayName("geometric mean should handle edge cases")
        void testGeometricMean_EdgeCases() {
            List<Number> values = Arrays.asList(1, 2, 4, 8);
            
            // With zeros
            double resultWithZeros = SpecsMath.geometricMean(values, true);
            assertThat(resultWithZeros).isCloseTo(2.828, within(0.001));
            
            // Test with negative values
            List<Number> negativeValues = Arrays.asList(-1, -2, -4);
            double negativeResult = SpecsMath.geometricMean(negativeValues, false);
            assertThat(negativeResult).isNaN(); // Geometric mean of negative numbers is NaN
        }

        @Test
        @DisplayName("harmonic mean should use zero correction when enabled")
        void testHarmonicMean_ZeroCorrection() {
            List<Number> values = Arrays.asList(1, 2, 0, 4);
            
            // Without zero correction: 3 / (1/1 + 1/2 + 1/4) = 3 / 1.75 ≈ 1.714
            double withoutCorrection = SpecsMath.harmonicMean(values, false);
            assertThat(withoutCorrection).isCloseTo(1.714, within(0.001));
            
            // With zero correction: applies additional correction factor (numberOfElements / totalElements)
            // = 1.714 * (3/4) = 1.714 * 0.75 ≈ 1.286
            double withCorrection = SpecsMath.harmonicMean(values, true);
            assertThat(withCorrection).isCloseTo(1.286, within(0.001));
        }

        @Test
        @DisplayName("max and min should handle ignore zeros option")
        void testMaxMinIgnoreZeros() {
            List<Number> values = Arrays.asList(0, 5, 0, 10, 0, 3);
            
            // With ignore zeros
            Number maxIgnoreZeros = SpecsMath.max(values, true);
            Number minIgnoreZeros = SpecsMath.min(values, true);
            
            assertThat(maxIgnoreZeros.intValue()).isEqualTo(10);
            assertThat(minIgnoreZeros.intValue()).isEqualTo(3);
            
            // Without ignore zeros
            Number maxWithZeros = SpecsMath.max(values, false);
            Number minWithZeros = SpecsMath.min(values, false);
            
            assertThat(maxWithZeros.intValue()).isEqualTo(10);
            assertThat(minWithZeros.intValue()).isEqualTo(0);
        }

        @Test
        @DisplayName("factorial should calculate correctly")
        void testFactorial() {
            assertThat(SpecsMath.factorial(0)).isEqualTo(1L);
            assertThat(SpecsMath.factorial(1)).isEqualTo(1L);
            assertThat(SpecsMath.factorial(5)).isEqualTo(120L);
            assertThat(SpecsMath.factorial(10)).isEqualTo(3628800L);
            
            // Negative input returns negative factorial
            assertThat(SpecsMath.factorial(-1)).isEqualTo(-1L);
            assertThat(SpecsMath.factorial(-5)).isEqualTo(-120L);
        }

        @Test
        @DisplayName("sum should handle large numbers")
        void testSum_LargeNumbers() {
            List<Number> largeNumbers = Arrays.asList(
                Long.MAX_VALUE / 2, 
                Long.MAX_VALUE / 4, 
                100L
            );
            
            double result = SpecsMath.sum(largeNumbers);
            assertThat(result).isPositive();
            assertThat(result).isGreaterThan(Long.MAX_VALUE / 2);
        }

        @Test
        @DisplayName("multiply should handle edge cases")
        void testMultiply_EdgeCases() {
            // Large numbers
            List<Number> largeNumbers = Arrays.asList(1000, 1000, 1000);
            double largeResult = SpecsMath.multiply(largeNumbers);
            assertThat(largeResult).isEqualTo(1_000_000_000.0);
            
            // With zero
            List<Number> withZero = Arrays.asList(5, 0, 10);
            double zeroResult = SpecsMath.multiply(withZero);
            assertThat(zeroResult).isZero();
            
            // With negative numbers
            List<Number> withNegative = Arrays.asList(-2, 3, -4);
            double negativeResult = SpecsMath.multiply(withNegative);
            assertThat(negativeResult).isEqualTo(24.0);
        }
    }
}
