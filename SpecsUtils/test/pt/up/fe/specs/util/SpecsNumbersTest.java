package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Comprehensive test suite for SpecsNumbers utility class.
 * 
 * This test class covers number-related operations including:
 * - Zero value retrieval for different number types
 * - Addition operations for typed numbers
 * - Type safety and generic handling
 * - Edge cases and null handling
 * - Support for Integer, Long, Float, and Double types
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsNumbers Tests")
public class SpecsNumbersTest {

    @BeforeAll
    static void init() {
        SpecsSystem.programStandardInit();
    }

    @Nested
    @DisplayName("Zero Value Retrieval")
    class ZeroValueTests {

        @Test
        @DisplayName("zero should return correct zero value for Integer")
        void testZero_Integer() {
            // Execute
            Number result = SpecsNumbers.zero(Integer.class);

            // Verify
            assertThat(result).isInstanceOf(Integer.class);
            assertThat(result.intValue()).isEqualTo(0);
            assertThat(result).isEqualTo(Integer.valueOf(0));
        }

        @Test
        @DisplayName("zero should return correct zero value for Long")
        void testZero_Long() {
            // Execute
            Number result = SpecsNumbers.zero(Long.class);

            // Verify
            assertThat(result).isInstanceOf(Long.class);
            assertThat(result.longValue()).isEqualTo(0L);
            assertThat(result).isEqualTo(Long.valueOf(0L));
        }

        @Test
        @DisplayName("zero should return correct zero value for Float")
        void testZero_Float() {
            // Execute
            Number result = SpecsNumbers.zero(Float.class);

            // Verify
            assertThat(result).isInstanceOf(Float.class);
            assertThat(result.floatValue()).isEqualTo(0.0f);
            assertThat(result).isEqualTo(Float.valueOf(0.0f));
        }

        @Test
        @DisplayName("zero should return correct zero value for Double")
        void testZero_Double() {
            // Execute
            Number result = SpecsNumbers.zero(Double.class);

            // Verify
            assertThat(result).isInstanceOf(Double.class);
            assertThat(result.doubleValue()).isEqualTo(0.0);
            assertThat(result).isEqualTo(Double.valueOf(0.0));
        }

        @Test
        @DisplayName("zero should handle unsupported number types")
        void testZero_UnsupportedType() {
            // Execute & Verify - should throw NotImplementedException for unsupported types
            assertThatThrownBy(() -> SpecsNumbers.zero(java.math.BigInteger.class))
                    .isInstanceOf(pt.up.fe.specs.util.exceptions.NotImplementedException.class);
        }

        @Test
        @DisplayName("zero should handle null input")
        void testZero_NullInput() {
            // Execute & Verify - should throw NullPointerException for null input
            assertThatThrownBy(() -> SpecsNumbers.zero(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("zero values should be immutable and cached")
        void testZero_Caching() {
            // Execute multiple times
            Number zero1 = SpecsNumbers.zero(Integer.class);
            Number zero2 = SpecsNumbers.zero(Integer.class);

            // Verify same instance is returned (caching)
            assertThat(zero1).isSameAs(zero2);
            assertThat(zero1).isEqualTo(zero2);
        }
    }

    @Nested
    @DisplayName("Addition Operations")
    class AdditionTests {

        @Test
        @DisplayName("add should correctly add two Integers")
        void testAdd_Integers() {
            // Arrange
            Integer a = 5;
            Integer b = 3;

            // Execute
            Integer result = SpecsNumbers.add(a, b);

            // Verify
            assertThat(result).isInstanceOf(Integer.class);
            assertThat(result).isEqualTo(8);
        }

        @Test
        @DisplayName("add should correctly add two Longs")
        void testAdd_Longs() {
            // Arrange
            Long a = 1000000000L;
            Long b = 2000000000L;

            // Execute
            Long result = SpecsNumbers.add(a, b);

            // Verify
            assertThat(result).isInstanceOf(Long.class);
            assertThat(result).isEqualTo(3000000000L);
        }

        @Test
        @DisplayName("add should correctly add two Floats")
        void testAdd_Floats() {
            // Arrange
            Float a = 1.5f;
            Float b = 2.3f;

            // Execute
            Float result = SpecsNumbers.add(a, b);

            // Verify
            assertThat(result).isInstanceOf(Float.class);
            assertThat(result).isCloseTo(3.8f, within(0.001f));
        }

        @Test
        @DisplayName("add should correctly add two Doubles")
        void testAdd_Doubles() {
            // Arrange
            Double a = 1.123456789;
            Double b = 2.987654321;

            // Execute
            Double result = SpecsNumbers.add(a, b);

            // Verify
            assertThat(result).isInstanceOf(Double.class);
            assertThat(result).isCloseTo(4.11111111, within(0.00000001));
        }

        @ParameterizedTest
        @CsvSource({
                "0, 0, 0",
                "5, 0, 5",
                "0, 7, 7",
                "-3, 8, 5",
                "10, -4, 6",
                "-5, -3, -8"
        })
        @DisplayName("add should handle various integer combinations")
        void testAdd_IntegerCombinations(int a, int b, int expected) {
            // Execute
            Integer result = SpecsNumbers.add(Integer.valueOf(a), Integer.valueOf(b));

            // Verify
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("add should handle zero addition for all types")
        void testAdd_WithZeros() {
            // Test with Integer
            Integer intZero = (Integer) SpecsNumbers.zero(Integer.class);
            Integer intResult = SpecsNumbers.add(5, intZero);
            assertThat(intResult).isEqualTo(5);

            // Test with Long
            Long longZero = (Long) SpecsNumbers.zero(Long.class);
            Long longResult = SpecsNumbers.add(100L, longZero);
            assertThat(longResult).isEqualTo(100L);

            // Test with Float
            Float floatZero = (Float) SpecsNumbers.zero(Float.class);
            Float floatResult = SpecsNumbers.add(2.5f, floatZero);
            assertThat(floatResult).isEqualTo(2.5f);

            // Test with Double
            Double doubleZero = (Double) SpecsNumbers.zero(Double.class);
            Double doubleResult = SpecsNumbers.add(3.14, doubleZero);
            assertThat(doubleResult).isEqualTo(3.14);
        }

        @Test
        @DisplayName("add should handle large number values")
        void testAdd_LargeValues() {
            // Test with large integers
            Integer largeInt1 = Integer.MAX_VALUE - 10;
            Integer largeInt2 = 5;
            Integer intResult = SpecsNumbers.add(largeInt1, largeInt2);
            assertThat(intResult).isEqualTo(Integer.MAX_VALUE - 5);

            // Test with large longs
            Long largeLong1 = Long.MAX_VALUE - 100;
            Long largeLong2 = 50L;
            Long longResult = SpecsNumbers.add(largeLong1, largeLong2);
            assertThat(longResult).isEqualTo(Long.MAX_VALUE - 50);
        }

        @Test
        @DisplayName("add should handle negative numbers")
        void testAdd_NegativeNumbers() {
            // Integer negatives
            assertThat(SpecsNumbers.add(-5, -3)).isEqualTo(-8);
            assertThat(SpecsNumbers.add(-10, 15)).isEqualTo(5);

            // Long negatives
            assertThat(SpecsNumbers.add(-1000L, -2000L)).isEqualTo(-3000L);

            // Float negatives
            assertThat(SpecsNumbers.add(-1.5f, -2.5f)).isEqualTo(-4.0f);

            // Double negatives
            assertThat(SpecsNumbers.add(-1.23, -4.56)).isCloseTo(-5.79, within(0.001));
        }

        @Test
        @DisplayName("add should handle precision edge cases for floating point")
        void testAdd_FloatingPointPrecision() {
            // Float precision test
            Float f1 = 0.1f;
            Float f2 = 0.2f;
            Float floatResult = SpecsNumbers.add(f1, f2);
            assertThat(floatResult).isCloseTo(0.3f, within(0.0001f));

            // Double precision test
            Double d1 = 0.1;
            Double d2 = 0.2;
            Double doubleResult = SpecsNumbers.add(d1, d2);
            assertThat(doubleResult).isCloseTo(0.3, within(0.0000001));
        }

        @Test
        @DisplayName("add should handle special floating point values")
        void testAdd_SpecialFloatingPointValues() {
            // Test with positive infinity
            Double posInf = Double.POSITIVE_INFINITY;
            Double result1 = SpecsNumbers.add(posInf, 100.0);
            assertThat(result1).isEqualTo(Double.POSITIVE_INFINITY);

            // Test with negative infinity
            Double negInf = Double.NEGATIVE_INFINITY;
            Double result2 = SpecsNumbers.add(negInf, 100.0);
            assertThat(result2).isEqualTo(Double.NEGATIVE_INFINITY);

            // Test with NaN
            Double nan = Double.NaN;
            Double result3 = SpecsNumbers.add(nan, 100.0);
            assertThat(result3).isNaN();

            // Test with very small numbers
            Double verySmall1 = Double.MIN_VALUE;
            Double verySmall2 = Double.MIN_VALUE;
            Double result4 = SpecsNumbers.add(verySmall1, verySmall2);
            assertThat(result4).isEqualTo(Double.MIN_VALUE * 2);
        }
    }

    @Nested
    @DisplayName("Type Safety and Generic Handling")
    class TypeSafetyTests {

        @Test
        @DisplayName("add should maintain generic type safety")
        void testAdd_GenericTypeSafety() {
            // The return type should match the input type
            Integer intResult = SpecsNumbers.add(Integer.valueOf(5), Integer.valueOf(3));
            assertThat(intResult).isInstanceOf(Integer.class);

            Long longResult = SpecsNumbers.add(Long.valueOf(5L), Long.valueOf(3L));
            assertThat(longResult).isInstanceOf(Long.class);

            Float floatResult = SpecsNumbers.add(Float.valueOf(5.0f), Float.valueOf(3.0f));
            assertThat(floatResult).isInstanceOf(Float.class);

            Double doubleResult = SpecsNumbers.add(Double.valueOf(5.0), Double.valueOf(3.0));
            assertThat(doubleResult).isInstanceOf(Double.class);
        }

        @Test
        @DisplayName("operations should handle boxing and unboxing correctly")
        void testBoxingUnboxing() {
            // Test with primitive int (auto-boxed)
            int primitiveInt = 5;
            Integer boxedInt = 3;
            Integer result = SpecsNumbers.add(Integer.valueOf(primitiveInt), boxedInt);
            assertThat(result).isEqualTo(8);

            // Test with primitive long (auto-boxed)
            long primitiveLong = 100L;
            Long boxedLong = 200L;
            Long longResult = SpecsNumbers.add(Long.valueOf(primitiveLong), boxedLong);
            assertThat(longResult).isEqualTo(300L);
        }

        @Test
        @DisplayName("add should handle unsupported number types gracefully")
        void testAdd_UnsupportedTypes() {
            // Create custom Number subclass
            Number customNumber1 = new Number() {
                @Override
                public int intValue() {
                    return 5;
                }

                @Override
                public long longValue() {
                    return 5L;
                }

                @Override
                public float floatValue() {
                    return 5.0f;
                }

                @Override
                public double doubleValue() {
                    return 5.0;
                }
            };

            Number customNumber2 = new Number() {
                @Override
                public int intValue() {
                    return 3;
                }

                @Override
                public long longValue() {
                    return 3L;
                }

                @Override
                public float floatValue() {
                    return 3.0f;
                }

                @Override
                public double doubleValue() {
                    return 3.0;
                }
            };

            // Execute & Verify - should throw exception for unsupported types
            assertThatThrownBy(() -> SpecsNumbers.add(customNumber1, customNumber2))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("add should handle null inputs appropriately")
        void testAdd_NullInputs() {
            // Execute & Verify
            assertThatThrownBy(() -> SpecsNumbers.add(null, Integer.valueOf(5)))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> SpecsNumbers.add(Integer.valueOf(5), null))
                    .isInstanceOf(NullPointerException.class);

            assertThatThrownBy(() -> SpecsNumbers.add(null, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("operations should handle boundary values correctly")
        void testBoundaryValues() {
            // Integer boundaries
            assertThat(SpecsNumbers.add(Integer.MAX_VALUE, 0)).isEqualTo(Integer.MAX_VALUE);
            assertThat(SpecsNumbers.add(Integer.MIN_VALUE, 0)).isEqualTo(Integer.MIN_VALUE);

            // Long boundaries
            assertThat(SpecsNumbers.add(Long.MAX_VALUE, 0L)).isEqualTo(Long.MAX_VALUE);
            assertThat(SpecsNumbers.add(Long.MIN_VALUE, 0L)).isEqualTo(Long.MIN_VALUE);

            // Float boundaries
            assertThat(SpecsNumbers.add(Float.MAX_VALUE, 0.0f)).isEqualTo(Float.MAX_VALUE);
            assertThat(SpecsNumbers.add(Float.MIN_VALUE, 0.0f)).isEqualTo(Float.MIN_VALUE);

            // Double boundaries
            assertThat(SpecsNumbers.add(Double.MAX_VALUE, 0.0)).isEqualTo(Double.MAX_VALUE);
            assertThat(SpecsNumbers.add(Double.MIN_VALUE, 0.0)).isEqualTo(Double.MIN_VALUE);
        }

        @Test
        @DisplayName("add should handle integer overflow gracefully")
        void testAdd_IntegerOverflow() {
            // Integer overflow - should wrap around
            Integer result = SpecsNumbers.add(Integer.MAX_VALUE, 1);
            assertThat(result).isEqualTo(Integer.MIN_VALUE); // Overflow wraps to MIN_VALUE

            // Integer underflow - should wrap around
            Integer underflowResult = SpecsNumbers.add(Integer.MIN_VALUE, -1);
            assertThat(underflowResult).isEqualTo(Integer.MAX_VALUE); // Underflow wraps to MAX_VALUE
        }

        @Test
        @DisplayName("add should handle long overflow gracefully")
        void testAdd_LongOverflow() {
            // Long overflow - should wrap around
            Long result = SpecsNumbers.add(Long.MAX_VALUE, 1L);
            assertThat(result).isEqualTo(Long.MIN_VALUE); // Overflow wraps to MIN_VALUE

            // Long underflow - should wrap around
            Long underflowResult = SpecsNumbers.add(Long.MIN_VALUE, -1L);
            assertThat(underflowResult).isEqualTo(Long.MAX_VALUE); // Underflow wraps to MAX_VALUE
        }

        @Test
        @DisplayName("add should handle float overflow to infinity")
        void testAdd_FloatOverflow() {
            // Float overflow to positive infinity
            Float largeFloat = Float.MAX_VALUE;
            Float result = SpecsNumbers.add(largeFloat, largeFloat);
            assertThat(result).isEqualTo(Float.POSITIVE_INFINITY);
        }

        @Test
        @DisplayName("add should handle double overflow to infinity")
        void testAdd_DoubleOverflow() {
            // Double overflow to positive infinity
            Double largeDouble = Double.MAX_VALUE;
            Double result = SpecsNumbers.add(largeDouble, largeDouble);
            assertThat(result).isEqualTo(Double.POSITIVE_INFINITY);
        }

        @Test
        @DisplayName("zero value should be consistent across multiple calls")
        void testZero_Consistency() {
            // Multiple calls should return consistent values
            for (int i = 0; i < 100; i++) {
                assertThat(SpecsNumbers.zero(Integer.class)).isEqualTo(0);
                assertThat(SpecsNumbers.zero(Long.class)).isEqualTo(0L);
                assertThat(SpecsNumbers.zero(Float.class)).isEqualTo(0.0f);
                assertThat(SpecsNumbers.zero(Double.class)).isEqualTo(0.0);
            }
        }

        @Test
        @DisplayName("operations should be commutative")
        void testAdd_Commutativity() {
            // Addition should be commutative: a + b = b + a
            Integer a = 15;
            Integer b = 25;

            Integer result1 = SpecsNumbers.add(a, b);
            Integer result2 = SpecsNumbers.add(b, a);

            assertThat(result1).isEqualTo(result2);
            assertThat(result1).isEqualTo(40);

            // Test with other types
            assertThat(SpecsNumbers.add(1.5, 2.5)).isEqualTo(SpecsNumbers.add(2.5, 1.5));
            assertThat(SpecsNumbers.add(100L, 200L)).isEqualTo(SpecsNumbers.add(200L, 100L));
            assertThat(SpecsNumbers.add(1.0f, 2.0f)).isEqualTo(SpecsNumbers.add(2.0f, 1.0f));
        }

        @Test
        @DisplayName("operations should have additive identity")
        void testAdd_AdditiveIdentity() {
            // Adding zero should not change the value
            Integer intValue = 42;
            Integer intZero = (Integer) SpecsNumbers.zero(Integer.class);
            assertThat(SpecsNumbers.add(intValue, intZero)).isEqualTo(intValue);

            Long longValue = 123L;
            Long longZero = (Long) SpecsNumbers.zero(Long.class);
            assertThat(SpecsNumbers.add(longValue, longZero)).isEqualTo(longValue);

            Float floatValue = 3.14f;
            Float floatZero = (Float) SpecsNumbers.zero(Float.class);
            assertThat(SpecsNumbers.add(floatValue, floatZero)).isEqualTo(floatValue);

            Double doubleValue = 2.718;
            Double doubleZero = (Double) SpecsNumbers.zero(Double.class);
            assertThat(SpecsNumbers.add(doubleValue, doubleZero)).isEqualTo(doubleValue);
        }
    }
}
