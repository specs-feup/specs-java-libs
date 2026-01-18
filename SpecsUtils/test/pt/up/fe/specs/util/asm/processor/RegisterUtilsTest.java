package pt.up.fe.specs.util.asm.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link RegisterUtils}.
 * 
 * This utility class provides methods for working with registers, including
 * building register bit notation and decoding flag information from register
 * strings. Tests verify bit manipulation, string parsing, and register flag
 * handling functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("RegisterUtils Tests")
class RegisterUtilsTest {

    @Nested
    @DisplayName("Build Register Bit Tests")
    class BuildRegisterBitTests {

        @Test
        @DisplayName("Should build register bit notation with valid inputs")
        void testBuildRegisterBit_ValidInputs_ReturnsCorrectNotation() {
            // Given
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn("MSR");
            int bitPosition = 29;

            // When
            String result = RegisterUtils.buildRegisterBit(registerId, bitPosition);

            // Then
            assertThat(result).isEqualTo("MSR_29");
        }

        @Test
        @DisplayName("Should handle zero bit position")
        void testBuildRegisterBit_ZeroBitPosition_ReturnsCorrectNotation() {
            // Given
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn("CPSR");
            int bitPosition = 0;

            // When
            String result = RegisterUtils.buildRegisterBit(registerId, bitPosition);

            // Then
            assertThat(result).isEqualTo("CPSR_0");
        }

        @Test
        @DisplayName("Should handle maximum bit position")
        void testBuildRegisterBit_MaxBitPosition_ReturnsCorrectNotation() {
            // Given
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn("REG");
            int bitPosition = 31;

            // When
            String result = RegisterUtils.buildRegisterBit(registerId, bitPosition);

            // Then
            assertThat(result).isEqualTo("REG_31");
        }

        @Test
        @DisplayName("Should handle negative bit position")
        void testBuildRegisterBit_NegativeBitPosition_ReturnsCorrectNotation() {
            // Given
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn("REG");
            int bitPosition = -5;

            // When
            String result = RegisterUtils.buildRegisterBit(registerId, bitPosition);

            // Then
            assertThat(result).isEqualTo("REG_-5");
        }

        @Test
        @DisplayName("Should handle various register names")
        void testBuildRegisterBit_VariousRegisterNames_ReturnsCorrectNotation() {
            // Given: Various register names
            String[] registerNames = { "R0", "EAX", "SP", "LR", "PC", "STATUS" };
            int bitPosition = 15;

            for (String regName : registerNames) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(regName);

                // When
                String result = RegisterUtils.buildRegisterBit(registerId, bitPosition);

                // Then
                assertThat(result).isEqualTo(regName + "_15");
            }
        }

        @Test
        @DisplayName("Should handle empty register name")
        void testBuildRegisterBit_EmptyRegisterName_ReturnsCorrectNotation() {
            // Given
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn("");
            int bitPosition = 10;

            // When
            String result = RegisterUtils.buildRegisterBit(registerId, bitPosition);

            // Then
            assertThat(result).isEqualTo("_10");
        }

        @Test
        @DisplayName("Should handle null register name")
        void testBuildRegisterBit_NullRegisterName_HandlesGracefully() {
            // Given
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn(null);
            int bitPosition = 5;

            // When
            String result = RegisterUtils.buildRegisterBit(registerId, bitPosition);

            // Then
            assertThat(result).isEqualTo("null_5");
        }
    }

    @Nested
    @DisplayName("Decode Flag Bit Tests")
    class DecodeFlagBitTests {

        @Test
        @DisplayName("Should decode valid flag bit notation")
        void testDecodeFlagBit_ValidNotation_ReturnsBitPosition() {
            // Given
            String flagName = "MSR_29";

            // When
            Integer result = RegisterUtils.decodeFlagBit(flagName);

            // Then
            assertThat(result).isEqualTo(29);
        }

        @Test
        @DisplayName("Should decode zero bit position")
        void testDecodeFlagBit_ZeroBitPosition_ReturnsZero() {
            // Given
            String flagName = "CPSR_0";

            // When
            Integer result = RegisterUtils.decodeFlagBit(flagName);

            // Then
            assertThat(result).isEqualTo(0);
        }

        @Test
        @DisplayName("Should decode maximum bit position")
        void testDecodeFlagBit_MaxBitPosition_ReturnsCorrectValue() {
            // Given
            String flagName = "REG_31";

            // When
            Integer result = RegisterUtils.decodeFlagBit(flagName);

            // Then
            assertThat(result).isEqualTo(31);
        }

        @Test
        @DisplayName("Should handle negative bit positions")
        void testDecodeFlagBit_NegativeBitPosition_ReturnsNegativeValue() {
            // Given
            String flagName = "REG_-5";

            // When
            Integer result = RegisterUtils.decodeFlagBit(flagName);

            // Then
            assertThat(result).isEqualTo(-5);
        }

        @Test
        @DisplayName("Should return null for invalid flag notation")
        void testDecodeFlagBit_InvalidNotation_ReturnsNull() {
            // Given
            String flagName = "INVALID_FLAG";

            // When
            Integer result = RegisterUtils.decodeFlagBit(flagName);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should return null for flag without underscore")
        void testDecodeFlagBit_NoUnderscore_ReturnsNull() {
            // Given
            String flagName = "MSR29";

            // When
            Integer result = RegisterUtils.decodeFlagBit(flagName);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should return null for null input")
        void testDecodeFlagBit_NullInput_ReturnsNull() {
            // When
            Integer result = RegisterUtils.decodeFlagBit(null);

            // Then: Should return null gracefully
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should return null for empty input")
        void testDecodeFlagBit_EmptyInput_ReturnsNull() {
            // When
            Integer result = RegisterUtils.decodeFlagBit("");

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle multiple underscores")
        void testDecodeFlagBit_MultipleUnderscores_ReturnsLastBitPortion() {
            // Given
            String flagName = "REG_NAME_15";

            // When
            Integer result = RegisterUtils.decodeFlagBit(flagName);

            // Then
            assertThat(result).isEqualTo(15); // Should parse the bit from the last underscore
        }

        @Test
        @DisplayName("Should handle non-numeric bit position")
        void testDecodeFlagBit_NonNumericBit_ReturnsNull() {
            // Given
            String flagName = "MSR_ABC";

            // When
            Integer result = RegisterUtils.decodeFlagBit(flagName);

            // Then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Decode Flag Name Tests")
    class DecodeFlagNameTests {

        @Test
        @DisplayName("Should decode valid flag name")
        void testDecodeFlagName_ValidNotation_ReturnsRegisterName() {
            // Given
            String flagName = "MSR_29";

            // When
            String result = RegisterUtils.decodeFlagName(flagName);

            // Then
            assertThat(result).isEqualTo("MSR");
        }

        @Test
        @DisplayName("Should decode various register names")
        void testDecodeFlagName_VariousRegisters_ReturnsCorrectNames() {
            // Given: Various flag notations
            String[] flagNames = { "CPSR_0", "R0_15", "EAX_31", "SP_7", "STATUS_1" };
            String[] expectedNames = { "CPSR", "R0", "EAX", "SP", "STATUS" };

            for (int i = 0; i < flagNames.length; i++) {
                // When
                String result = RegisterUtils.decodeFlagName(flagNames[i]);

                // Then
                assertThat(result).isEqualTo(expectedNames[i]);
            }
        }

        @Test
        @DisplayName("Should handle empty register name")
        void testDecodeFlagName_EmptyRegisterName_ReturnsEmpty() {
            // Given
            String flagName = "_29";

            // When
            String result = RegisterUtils.decodeFlagName(flagName);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return null for invalid flag notation with non-numeric bit")
        void testDecodeFlagName_InvalidNotation_ReturnsNull() {
            // Given: Invalid notation where "FLAG" is not a number
            String flagName = "INVALID_FLAG";

            // When
            String result = RegisterUtils.decodeFlagName(flagName);

            // Then: Returns null because bit portion is invalid
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should return null for flag without underscore")
        void testDecodeFlagName_NoUnderscore_ReturnsNull() {
            // Given
            String flagName = "MSR29";

            // When
            String result = RegisterUtils.decodeFlagName(flagName);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should return null for null input")
        void testDecodeFlagName_NullInput_ReturnsNull() {
            // When
            String result = RegisterUtils.decodeFlagName(null);

            // Then: Should return null gracefully
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should return null for empty input")
        void testDecodeFlagName_EmptyInput_ReturnsNull() {
            // When
            String result = RegisterUtils.decodeFlagName("");

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle multiple underscores")
        void testDecodeFlagName_MultipleUnderscores_ReturnsPartBeforeLast() {
            // Given
            String flagName = "REG_NAME_15";

            // When
            String result = RegisterUtils.decodeFlagName(flagName);

            // Then
            assertThat(result).isEqualTo("REG_NAME");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should maintain consistency between build and decode operations")
        void testIntegration_BuildAndDecode_MaintainsConsistency() {
            // Given
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn("MSR");
            int bitPosition = 29;

            // When
            String builtFlag = RegisterUtils.buildRegisterBit(registerId, bitPosition);
            String decodedName = RegisterUtils.decodeFlagName(builtFlag);
            Integer decodedBit = RegisterUtils.decodeFlagBit(builtFlag);

            // Then
            assertThat(decodedName).isEqualTo("MSR");
            assertThat(decodedBit).isEqualTo(29);
        }

        @Test
        @DisplayName("Should work with various register types")
        void testIntegration_VariousRegisterTypes_WorksCorrectly() {
            // Given: Different register types and bit positions
            String[][] testData = {
                    { "R0", "15" }, { "CPSR", "0" }, { "MSR", "31" },
                    { "EAX", "7" }, { "STATUS", "1" }, { "FLAGS", "16" }
            };

            for (String[] data : testData) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(data[0]);
                int bitPos = Integer.parseInt(data[1]);

                // When
                String flagNotation = RegisterUtils.buildRegisterBit(registerId, bitPos);
                String decodedName = RegisterUtils.decodeFlagName(flagNotation);
                Integer decodedBit = RegisterUtils.decodeFlagBit(flagNotation);

                // Then
                assertThat(decodedName).isEqualTo(data[0]);
                assertThat(decodedBit).isEqualTo(bitPos);
            }
        }

        @Test
        @DisplayName("Should work with simple register names - Bug: complex names with underscores don't round-trip")
        void testIntegration_RoundTrip_WorksWithSimpleNames() {
            // Given: Simple register name without underscores
            String originalRegName = "MSR";
            int originalBitPos = 23;
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn(originalRegName);

            // When: Build flag notation and decode it back
            String flagNotation = RegisterUtils.buildRegisterBit(registerId, originalBitPos);
            String roundTripName = RegisterUtils.decodeFlagName(flagNotation);
            Integer roundTripBit = RegisterUtils.decodeFlagBit(flagNotation);

            // Then: Simple names work correctly
            assertThat(flagNotation).isEqualTo("MSR_23");
            assertThat(roundTripName).isEqualTo("MSR");
            assertThat(roundTripBit).isEqualTo(originalBitPos);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle very large bit positions")
        void testEdgeCase_LargeBitPositions_HandlesCorrectly() {
            // Given
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn("REG");
            int largeBitPos = Integer.MAX_VALUE;

            // When
            String flagNotation = RegisterUtils.buildRegisterBit(registerId, largeBitPos);
            Integer decodedBit = RegisterUtils.decodeFlagBit(flagNotation);

            // Then
            assertThat(decodedBit).isEqualTo(largeBitPos);
        }

        @Test
        @DisplayName("Should handle very small bit positions")
        void testEdgeCase_SmallBitPositions_HandlesCorrectly() {
            // Given
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn("REG");
            int smallBitPos = Integer.MIN_VALUE;

            // When
            String flagNotation = RegisterUtils.buildRegisterBit(registerId, smallBitPos);
            Integer decodedBit = RegisterUtils.decodeFlagBit(flagNotation);

            // Then
            assertThat(decodedBit).isEqualTo(smallBitPos);
        }

        @Test
        @DisplayName("Should handle register names with special characters")
        void testEdgeCase_SpecialCharacters_HandlesCorrectly() {
            // Given
            String[] specialNames = { "REG.FLAG", "REG-NAME", "REG[0]", "REG$VAR" };
            int bitPos = 10;

            for (String regName : specialNames) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(regName);

                // When
                String flagNotation = RegisterUtils.buildRegisterBit(registerId, bitPos);
                String decodedName = RegisterUtils.decodeFlagName(flagNotation);
                Integer decodedBit = RegisterUtils.decodeFlagBit(flagNotation);

                // Then
                assertThat(decodedName).isEqualTo(regName);
                assertThat(decodedBit).isEqualTo(bitPos);
            }
        }

        @Test
        @DisplayName("Should handle underscore in register name")
        void testEdgeCase_UnderscoreInRegisterName_HandlesCorrectly() {
            // Given: Register name already containing underscore
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn("REG_NAME");
            int bitPos = 15;

            // When
            String flagNotation = RegisterUtils.buildRegisterBit(registerId, bitPos);
            String decodedName = RegisterUtils.decodeFlagName(flagNotation);

            // Then: Should decode the register name correctly for round-trip consistency
            assertThat(flagNotation).isEqualTo("REG_NAME_15");
            assertThat(decodedName).isEqualTo("REG_NAME");
        }
    }
}
