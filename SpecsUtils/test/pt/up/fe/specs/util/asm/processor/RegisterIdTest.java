package pt.up.fe.specs.util.asm.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link RegisterId}.
 * 
 * This interface identifies registers in assembly processor simulators,
 * providing register naming functionality.
 * Tests verify the interface contract and typical register identification
 * patterns.
 * 
 * @author Generated Tests
 */
@DisplayName("RegisterId Tests")
class RegisterIdTest {

    @Nested
    @DisplayName("Name Tests")
    class NameTests {

        @Test
        @DisplayName("Should return register name")
        void testGetName_ValidRegister_ReturnsName() {
            // Given
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn("R0");

            // When
            String name = registerId.getName();

            // Then
            assertThat(name).isNotNull();
            assertThat(name).isEqualTo("R0");
        }

        @Test
        @DisplayName("Should handle null name")
        void testGetName_NullName_ReturnsNull() {
            // Given
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn(null);

            // When
            String name = registerId.getName();

            // Then
            assertThat(name).isNull();
        }

        @Test
        @DisplayName("Should handle empty name")
        void testGetName_EmptyName_ReturnsEmpty() {
            // Given
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn("");

            // When
            String name = registerId.getName();

            // Then
            assertThat(name).isEmpty();
        }

        @Test
        @DisplayName("Should support general purpose register names")
        void testGetName_GeneralPurposeRegisters_ReturnsValidNames() {
            // Given: Common general purpose register patterns
            String[] registerNames = { "R0", "R1", "R15", "EAX", "EBX", "ECX", "EDX" };

            for (String regName : registerNames) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(regName);

                // When
                String name = registerId.getName();

                // Then
                assertThat(name).isEqualTo(regName);
            }
        }

        @Test
        @DisplayName("Should support special register names")
        void testGetName_SpecialRegisters_ReturnsValidNames() {
            // Given: Special register patterns
            String[] specialRegisterNames = { "SP", "LR", "PC", "CPSR", "MSR", "ESP", "EBP", "EIP" };

            for (String regName : specialRegisterNames) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(regName);

                // When
                String name = registerId.getName();

                // Then
                assertThat(name).isEqualTo(regName);
            }
        }
    }

    @Nested
    @DisplayName("Register Pattern Tests")
    class RegisterPatternTests {

        @Test
        @DisplayName("Should support ARM register patterns")
        void testRegisterPatterns_ARMRegisters_ValidNames() {
            // Given: ARM register patterns
            String[] armRegisters = { "R0", "R1", "R2", "R13", "R14", "R15", "SP", "LR", "PC" };

            for (String regName : armRegisters) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(regName);

                // When
                String name = registerId.getName();

                // Then
                assertThat(name).isEqualTo(regName);
                assertThat(name).matches("^(R\\d+|SP|LR|PC)$");
            }
        }

        @Test
        @DisplayName("Should support x86 register patterns")
        void testRegisterPatterns_x86Registers_ValidNames() {
            // Given: x86 register patterns
            String[] x86Registers = { "EAX", "EBX", "ECX", "EDX", "ESI", "EDI", "ESP", "EBP" };

            for (String regName : x86Registers) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(regName);

                // When
                String name = registerId.getName();

                // Then
                assertThat(name).isEqualTo(regName);
                assertThat(name).matches("^E[A-Z]{2}$");
            }
        }

        @Test
        @DisplayName("Should support MIPS register patterns")
        void testRegisterPatterns_MIPSRegisters_ValidNames() {
            // Given: MIPS register patterns
            String[] mipsRegisters = { "$0", "$1", "$31", "$zero", "$at", "$sp", "$ra" };

            for (String regName : mipsRegisters) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(regName);

                // When
                String name = registerId.getName();

                // Then
                assertThat(name).isEqualTo(regName);
                assertThat(name).startsWith("$");
            }
        }

        @Test
        @DisplayName("Should support numbered register patterns")
        void testRegisterPatterns_NumberedRegisters_ValidNames() {
            // Given: Numbered register patterns
            for (int i = 0; i < 32; i++) {
                String regName = "R" + i;
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(regName);

                // When
                String name = registerId.getName();

                // Then
                assertThat(name).isEqualTo(regName);
                assertThat(name).matches("^R\\d+$");
            }
        }
    }

    @Nested
    @DisplayName("Case Sensitivity Tests")
    class CaseSensitivityTests {

        @Test
        @DisplayName("Should preserve case in register names")
        void testCaseSensitivity_VariousCases_PreservesCase() {
            // Given: Register names with different cases
            String[] caseVariations = { "r0", "R0", "eax", "EAX", "sp", "SP", "Pc", "PC" };

            for (String regName : caseVariations) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(regName);

                // When
                String name = registerId.getName();

                // Then
                assertThat(name).isEqualTo(regName);
            }
        }

        @Test
        @DisplayName("Should handle mixed case register names")
        void testCaseSensitivity_MixedCase_ReturnsExactCase() {
            // Given: Mixed case register names
            String[] mixedCaseNames = { "CpSr", "mSr", "FpSr", "SpSr" };

            for (String regName : mixedCaseNames) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(regName);

                // When
                String name = registerId.getName();

                // Then
                assertThat(name).isEqualTo(regName);
            }
        }
    }

    @Nested
    @DisplayName("Special Character Tests")
    class SpecialCharacterTests {

        @Test
        @DisplayName("Should support registers with special characters")
        void testSpecialCharacters_VariousCharacters_ValidNames() {
            // Given: Register names with special characters
            String[] specialCharNames = { "$0", "$zero", "_R0", "R0_bit", "MSR[29]", "CPSR.C" };

            for (String regName : specialCharNames) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(regName);

                // When
                String name = registerId.getName();

                // Then
                assertThat(name).isEqualTo(regName);
            }
        }

        @Test
        @DisplayName("Should handle register flag notation")
        void testSpecialCharacters_FlagNotation_ValidNames() {
            // Given: Register flag notation (from RegisterUtils usage)
            String[] flagNames = { "MSR_29", "CPSR_0", "PSR_31" };

            for (String regName : flagNames) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(regName);

                // When
                String name = registerId.getName();

                // Then
                assertThat(name).isEqualTo(regName);
                assertThat(name).contains("_");
            }
        }
    }

    @Nested
    @DisplayName("Implementation Tests")
    class ImplementationTests {

        @Test
        @DisplayName("Should maintain consistent name across calls")
        void testImplementation_ConsistentName_SameResult() {
            // Given
            RegisterId registerId = new TestRegisterId("R0");

            // When
            String name1 = registerId.getName();
            String name2 = registerId.getName();

            // Then
            assertThat(name1).isEqualTo(name2);
        }

        @Test
        @DisplayName("Should support interface polymorphism")
        void testImplementation_Polymorphism_WorksAsInterface() {
            // Given
            RegisterId[] registers = {
                    new TestRegisterId("R0"),
                    new TestRegisterId("R1"),
                    new TestRegisterId("SP")
            };

            // When
            for (RegisterId reg : registers) {
                String name = reg.getName();

                // Then
                assertThat(name).isNotNull();
                assertThat(name).isNotEmpty();
            }
        }

        @Test
        @DisplayName("Should work with register utilities")
        void testImplementation_WithRegisterUtils_ValidIntegration() {
            // Given
            RegisterId registerId = new TestRegisterId("MSR");

            // When
            String flagBit = RegisterUtils.buildRegisterBit(registerId, 29);

            // Then
            assertThat(flagBit).isEqualTo("MSR_29");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle very long register names")
        void testEdgeCase_LongNames_HandlesCorrectly() {
            // Given
            String longName = "VERY_LONG_REGISTER_NAME_WITH_MANY_CHARACTERS_AND_UNDERSCORES_123";
            RegisterId registerId = mock(RegisterId.class);
            when(registerId.getName()).thenReturn(longName);

            // When
            String name = registerId.getName();

            // Then
            assertThat(name).isEqualTo(longName);
        }

        @Test
        @DisplayName("Should handle single character names")
        void testEdgeCase_SingleCharacter_HandlesCorrectly() {
            // Given
            String[] singleCharNames = { "A", "B", "X", "Y", "Z" };

            for (String charName : singleCharNames) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(charName);

                // When
                String name = registerId.getName();

                // Then
                assertThat(name).isEqualTo(charName);
                assertThat(name).hasSize(1);
            }
        }

        @Test
        @DisplayName("Should handle whitespace in names")
        void testEdgeCase_Whitespace_HandlesCorrectly() {
            // Given: Names with various whitespace patterns
            String[] whitespaceNames = { " R0", "R0 ", " R0 ", "R 0", "R\t0", "R\n0" };

            for (String spaceName : whitespaceNames) {
                RegisterId registerId = mock(RegisterId.class);
                when(registerId.getName()).thenReturn(spaceName);

                // When
                String name = registerId.getName();

                // Then
                assertThat(name).isEqualTo(spaceName);
            }
        }
    }

    // Test implementation of RegisterId interface
    private static class TestRegisterId implements RegisterId {
        private final String name;

        public TestRegisterId(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
