package pt.up.fe.specs.util.asm;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link ArithmeticResult32}.
 * 
 * This class represents 32-bit arithmetic operation results including both the
 * result value and carry-out bit.
 * Tests verify constructor behavior, field access, and various arithmetic
 * scenarios.
 * 
 * @author Generated Tests
 */
@DisplayName("ArithmeticResult32 Tests")
class ArithmeticResult32Test {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instance with positive values")
        void testConstructor_PositiveValues_SetsFieldsCorrectly() {
            // Given
            int expectedResult = 42;
            int expectedCarryOut = 1;

            // When
            ArithmeticResult32 result = new ArithmeticResult32(expectedResult, expectedCarryOut);

            // Then
            assertThat(result.result).isEqualTo(expectedResult);
            assertThat(result.carryOut).isEqualTo(expectedCarryOut);
        }

        @Test
        @DisplayName("Should create instance with zero values")
        void testConstructor_ZeroValues_SetsFieldsCorrectly() {
            // Given
            int expectedResult = 0;
            int expectedCarryOut = 0;

            // When
            ArithmeticResult32 result = new ArithmeticResult32(expectedResult, expectedCarryOut);

            // Then
            assertThat(result.result).isEqualTo(expectedResult);
            assertThat(result.carryOut).isEqualTo(expectedCarryOut);
        }

        @Test
        @DisplayName("Should create instance with negative result")
        void testConstructor_NegativeResult_SetsFieldsCorrectly() {
            // Given
            int expectedResult = -123;
            int expectedCarryOut = 1;

            // When
            ArithmeticResult32 result = new ArithmeticResult32(expectedResult, expectedCarryOut);

            // Then
            assertThat(result.result).isEqualTo(expectedResult);
            assertThat(result.carryOut).isEqualTo(expectedCarryOut);
        }

        @Test
        @DisplayName("Should create instance with maximum 32-bit values")
        void testConstructor_MaximumValues_SetsFieldsCorrectly() {
            // Given
            int expectedResult = Integer.MAX_VALUE;
            int expectedCarryOut = Integer.MAX_VALUE;

            // When
            ArithmeticResult32 result = new ArithmeticResult32(expectedResult, expectedCarryOut);

            // Then
            assertThat(result.result).isEqualTo(expectedResult);
            assertThat(result.carryOut).isEqualTo(expectedCarryOut);
        }

        @Test
        @DisplayName("Should create instance with minimum 32-bit values")
        void testConstructor_MinimumValues_SetsFieldsCorrectly() {
            // Given
            int expectedResult = Integer.MIN_VALUE;
            int expectedCarryOut = Integer.MIN_VALUE;

            // When
            ArithmeticResult32 result = new ArithmeticResult32(expectedResult, expectedCarryOut);

            // Then
            assertThat(result.result).isEqualTo(expectedResult);
            assertThat(result.carryOut).isEqualTo(expectedCarryOut);
        }
    }

    @Nested
    @DisplayName("Field Access Tests")
    class FieldAccessTests {

        @Test
        @DisplayName("Should allow direct access to result field")
        void testFieldAccess_Result_IsAccessible() {
            // Given
            int expectedResult = 987;
            ArithmeticResult32 result = new ArithmeticResult32(expectedResult, 0);

            // When & Then
            assertThat(result.result).isEqualTo(expectedResult);
        }

        @Test
        @DisplayName("Should allow direct access to carryOut field")
        void testFieldAccess_CarryOut_IsAccessible() {
            // Given
            int expectedCarryOut = 1;
            ArithmeticResult32 result = new ArithmeticResult32(0, expectedCarryOut);

            // When & Then
            assertThat(result.carryOut).isEqualTo(expectedCarryOut);
        }

        @Test
        @DisplayName("Should maintain field immutability")
        void testFieldAccess_Fields_AreImmutable() {
            // Given
            int originalResult = 100;
            int originalCarryOut = 1;
            ArithmeticResult32 result = new ArithmeticResult32(originalResult, originalCarryOut);

            // When accessing fields multiple times
            int result1 = result.result;
            int carry1 = result.carryOut;
            int result2 = result.result;
            int carry2 = result.carryOut;

            // Then values should remain consistent
            assertThat(result1).isEqualTo(result2).isEqualTo(originalResult);
            assertThat(carry1).isEqualTo(carry2).isEqualTo(originalCarryOut);
        }
    }

    @Nested
    @DisplayName("Arithmetic Scenario Tests")
    class ArithmeticScenarioTests {

        @Test
        @DisplayName("Should represent addition with no carry")
        void testArithmeticScenario_AdditionNoCarry_ValidResult() {
            // Given: Simulating 5 + 3 = 8, no carry
            int sum = 8;
            int carryOut = 0;

            // When
            ArithmeticResult32 result = new ArithmeticResult32(sum, carryOut);

            // Then
            assertThat(result.result).isEqualTo(8);
            assertThat(result.carryOut).isEqualTo(0);
        }

        @Test
        @DisplayName("Should represent addition with carry")
        void testArithmeticScenario_AdditionWithCarry_ValidResult() {
            // Given: Simulating addition that generates carry
            int sum = 0xFFFFFFFF; // Result that would overflow
            int carryOut = 1;

            // When
            ArithmeticResult32 result = new ArithmeticResult32(sum, carryOut);

            // Then
            assertThat(result.result).isEqualTo(0xFFFFFFFF);
            assertThat(result.carryOut).isEqualTo(1);
        }

        @Test
        @DisplayName("Should represent subtraction with borrow")
        void testArithmeticScenario_SubtractionWithBorrow_ValidResult() {
            // Given: Simulating subtraction that requires borrow (represented as carry)
            int difference = -1;
            int carryOut = 1; // Borrow represented as carry

            // When
            ArithmeticResult32 result = new ArithmeticResult32(difference, carryOut);

            // Then
            assertThat(result.result).isEqualTo(-1);
            assertThat(result.carryOut).isEqualTo(1);
        }

        @Test
        @DisplayName("Should handle 32-bit overflow scenarios")
        void testArithmeticScenario_OverflowScenarios_ValidResults() {
            // Given: Various overflow scenarios
            ArithmeticResult32 maxOverflow = new ArithmeticResult32(Integer.MIN_VALUE, 1);
            ArithmeticResult32 minUnderflow = new ArithmeticResult32(Integer.MAX_VALUE, 0);

            // Then
            assertThat(maxOverflow.result).isEqualTo(Integer.MIN_VALUE);
            assertThat(maxOverflow.carryOut).isEqualTo(1);
            assertThat(minUnderflow.result).isEqualTo(Integer.MAX_VALUE);
            assertThat(minUnderflow.carryOut).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Binary Representation Tests")
    class BinaryRepresentationTests {

        @Test
        @DisplayName("Should handle binary operations correctly")
        void testBinaryOperations_VariousValues_ValidRepresentation() {
            // Given: Various binary patterns
            ArithmeticResult32 result1 = new ArithmeticResult32(0b11110000, 1);
            ArithmeticResult32 result2 = new ArithmeticResult32(0b00001111, 0);
            ArithmeticResult32 result3 = new ArithmeticResult32(0b10101010, 1);

            // Then
            assertThat(result1.result).isEqualTo(0b11110000);
            assertThat(result1.carryOut).isEqualTo(1);
            assertThat(result2.result).isEqualTo(0b00001111);
            assertThat(result2.carryOut).isEqualTo(0);
            assertThat(result3.result).isEqualTo(0b10101010);
            assertThat(result3.carryOut).isEqualTo(1);
        }

        @Test
        @DisplayName("Should handle hexadecimal values correctly")
        void testHexadecimalValues_VariousInputs_ValidRepresentation() {
            // Given: Hexadecimal values commonly used in assembly
            ArithmeticResult32 result1 = new ArithmeticResult32(0xDEADBEEF, 1);
            ArithmeticResult32 result2 = new ArithmeticResult32(0xCAFEBABE, 0);
            ArithmeticResult32 result3 = new ArithmeticResult32(0x12345678, 1);

            // Then
            assertThat(result1.result).isEqualTo(0xDEADBEEF);
            assertThat(result1.carryOut).isEqualTo(1);
            assertThat(result2.result).isEqualTo(0xCAFEBABE);
            assertThat(result2.carryOut).isEqualTo(0);
            assertThat(result3.result).isEqualTo(0x12345678);
            assertThat(result3.carryOut).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle all ones pattern")
        void testEdgeCase_AllOnesPattern_ValidResult() {
            // Given: All bits set to 1
            int allOnes = 0xFFFFFFFF;

            // When
            ArithmeticResult32 result = new ArithmeticResult32(allOnes, 1);

            // Then
            assertThat(result.result).isEqualTo(allOnes);
            assertThat(result.carryOut).isEqualTo(1);
        }

        @Test
        @DisplayName("Should handle alternating bit patterns")
        void testEdgeCase_AlternatingBitPatterns_ValidResults() {
            // Given: Alternating bit patterns
            int pattern1 = 0xAAAAAAAA; // 10101010...
            int pattern2 = 0x55555555; // 01010101...

            // When
            ArithmeticResult32 result1 = new ArithmeticResult32(pattern1, 0);
            ArithmeticResult32 result2 = new ArithmeticResult32(pattern2, 1);

            // Then
            assertThat(result1.result).isEqualTo(pattern1);
            assertThat(result1.carryOut).isEqualTo(0);
            assertThat(result2.result).isEqualTo(pattern2);
            assertThat(result2.carryOut).isEqualTo(1);
        }

        @Test
        @DisplayName("Should handle power of two values")
        void testEdgeCase_PowerOfTwoValues_ValidResults() {
            // Given: Powers of 2
            int[] powersOfTwo = { 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768 };

            for (int power : powersOfTwo) {
                // When
                ArithmeticResult32 result = new ArithmeticResult32(power, power & 1);

                // Then
                assertThat(result.result).isEqualTo(power);
                assertThat(result.carryOut).isEqualTo(power & 1);
            }
        }
    }

    @Nested
    @DisplayName("Assembly Context Tests")
    class AssemblyContextTests {

        @Test
        @DisplayName("Should support typical ALU operation results")
        void testAssemblyContext_ALUOperations_ValidResults() {
            // Given: Typical ALU operations in assembly processing
            ArithmeticResult32 addResult = new ArithmeticResult32(0x12345678, 0);
            ArithmeticResult32 subResult = new ArithmeticResult32(0x87654321, 1);
            ArithmeticResult32 mulResult = new ArithmeticResult32(0xABCDEF00, 0);

            // Then: Should store both result and carry/overflow information
            assertThat(addResult.result).isEqualTo(0x12345678);
            assertThat(addResult.carryOut).isEqualTo(0);
            assertThat(subResult.result).isEqualTo(0x87654321);
            assertThat(subResult.carryOut).isEqualTo(1);
            assertThat(mulResult.result).isEqualTo(0xABCDEF00);
            assertThat(mulResult.carryOut).isEqualTo(0);
        }

        @Test
        @DisplayName("Should support processor flag calculations")
        void testAssemblyContext_ProcessorFlags_ValidResults() {
            // Given: Results that would set various processor flags
            ArithmeticResult32 zeroFlag = new ArithmeticResult32(0, 0);
            ArithmeticResult32 carryFlag = new ArithmeticResult32(42, 1);
            ArithmeticResult32 negativeFlag = new ArithmeticResult32(-1, 0);

            // Then: Should properly represent flag states
            assertThat(zeroFlag.result).isEqualTo(0);
            assertThat(zeroFlag.carryOut).isEqualTo(0);
            assertThat(carryFlag.result).isEqualTo(42);
            assertThat(carryFlag.carryOut).isEqualTo(1);
            assertThat(negativeFlag.result).isEqualTo(-1);
            assertThat(negativeFlag.carryOut).isEqualTo(0);
        }
    }
}
