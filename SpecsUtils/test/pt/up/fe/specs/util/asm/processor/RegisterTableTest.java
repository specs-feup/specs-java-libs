package pt.up.fe.specs.util.asm.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link RegisterTable}.
 * 
 * This class represents a snapshot of register names and values, providing
 * storage and retrieval
 * of register states including individual bit flag access. Tests verify
 * register management,
 * flag bit operations, and table functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("RegisterTable Tests")
class RegisterTableTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty register table")
        void testConstructor_Default_CreatesEmptyTable() {
            // When
            RegisterTable table = new RegisterTable();

            // Then
            assertThat(table).isNotNull();
            assertThat(table.toString()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Put Operation Tests")
    class PutOperationTests {

        private RegisterTable table;
        private RegisterId mockRegister;

        @BeforeEach
        void setUp() {
            table = new RegisterTable();
            mockRegister = mock(RegisterId.class);
            when(mockRegister.getName()).thenReturn("R0");
        }

        @Test
        @DisplayName("Should store register value successfully")
        void testPut_ValidRegisterValue_StoresSuccessfully() {
            // Given
            Integer value = 42;

            // When
            Integer previousValue = table.put(mockRegister, value);

            // Then
            assertThat(previousValue).isNull();
            assertThat(table.get("R0")).isEqualTo(value);
        }

        @Test
        @DisplayName("Should return previous value when updating register")
        void testPut_UpdateExistingRegister_ReturnsPreviousValue() {
            // Given
            Integer initialValue = 10;
            Integer newValue = 20;
            table.put(mockRegister, initialValue);

            // When
            Integer previousValue = table.put(mockRegister, newValue);

            // Then
            assertThat(previousValue).isEqualTo(initialValue);
            assertThat(table.get("R0")).isEqualTo(newValue);
        }

        @Test
        @DisplayName("Should reject null register value")
        void testPut_NullValue_ReturnsNull() {
            // When
            Integer result = table.put(mockRegister, null);

            // Then
            assertThat(result).isNull();
            assertThat(table.get("R0")).isNull();
        }

        @Test
        @DisplayName("Should handle zero value")
        void testPut_ZeroValue_StoresSuccessfully() {
            // Given
            Integer zeroValue = 0;

            // When
            Integer result = table.put(mockRegister, zeroValue);

            // Then
            assertThat(result).isNull();
            assertThat(table.get("R0")).isEqualTo(zeroValue);
        }

        @Test
        @DisplayName("Should handle negative values")
        void testPut_NegativeValue_StoresSuccessfully() {
            // Given
            Integer negativeValue = -123;

            // When
            table.put(mockRegister, negativeValue);

            // Then
            assertThat(table.get("R0")).isEqualTo(negativeValue);
        }

        @Test
        @DisplayName("Should handle maximum integer value")
        void testPut_MaxValue_StoresSuccessfully() {
            // Given
            Integer maxValue = Integer.MAX_VALUE;

            // When
            table.put(mockRegister, maxValue);

            // Then
            assertThat(table.get("R0")).isEqualTo(maxValue);
        }

        @Test
        @DisplayName("Should handle minimum integer value")
        void testPut_MinValue_StoresSuccessfully() {
            // Given
            Integer minValue = Integer.MIN_VALUE;

            // When
            table.put(mockRegister, minValue);

            // Then
            assertThat(table.get("R0")).isEqualTo(minValue);
        }
    }

    @Nested
    @DisplayName("Get Operation Tests")
    class GetOperationTests {

        private RegisterTable table;

        @BeforeEach
        void setUp() {
            table = new RegisterTable();
            RegisterId mockRegister = mock(RegisterId.class);
            when(mockRegister.getName()).thenReturn("MSR");
            table.put(mockRegister, 0x80000000); // Set bit 31
        }

        @Test
        @DisplayName("Should retrieve existing register value")
        void testGet_ExistingRegister_ReturnsValue() {
            // When
            Integer result = table.get("MSR");

            // Then
            assertThat(result).isEqualTo(0x80000000);
        }

        @Test
        @DisplayName("Should return null for non-existing register")
        void testGet_NonExistingRegister_ReturnsNull() {
            // When
            Integer result = table.get("NON_EXISTING");

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle names with underscores")
        void testGet_RegisterWithUnderscore_ReturnsValue() {
            // When
            RegisterId mockRegister2 = mock(RegisterId.class);
            when(mockRegister2.getName()).thenReturn("WITH_UNDERSCORE");
            table.put(mockRegister2, 0x80000000); // Set bit 31

            // Then
            Integer bit31 = table.get("WITH_UNDERSCORE_31");
            assertThat(bit31).isNotNull();
            assertThat(bit31).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return null for null register name")
        void testGet_NullRegisterName_ReturnsNull() {
            // When
            Integer result = table.get(null);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should return null for empty register name")
        void testGet_EmptyRegisterName_ReturnsNull() {
            // When
            Integer result = table.get("");

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should retrieve flag bit value")
        void testGet_FlagBitNotation_ReturnsBitValue() {
            // When: Get bit 31 of MSR register (should be 1)
            Integer result = table.get("MSR_31");

            // Then
            assertThat(result).isEqualTo(1);
        }

        @Test
        @DisplayName("Should retrieve zero flag bit value")
        void testGet_ZeroFlagBit_ReturnsZero() {
            // When: Get bit 0 of MSR register (should be 0)
            Integer result = table.get("MSR_0");

            // Then
            assertThat(result).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle invalid flag bit notation")
        void testGet_InvalidFlagNotation_ReturnsNull() {
            // When
            Integer result = table.get("MSR_INVALID");

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle flag for non-existing register")
        void testGet_FlagForNonExistingRegister_ReturnsNull() {
            // When
            Integer result = table.get("NON_EXISTING_0");

            // Then
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Flag Bit Operations Tests")
    class FlagBitOperationsTests {

        private RegisterTable table;

        @BeforeEach
        void setUp() {
            table = new RegisterTable();
        }

        @Test
        @DisplayName("Should retrieve individual bits correctly")
        void testFlagBits_VariousBitPositions_ReturnsCorrectValues() {
            // Given: Register with specific bit pattern (0x55555555 = 01010101...)
            RegisterId mockRegister = mock(RegisterId.class);
            when(mockRegister.getName()).thenReturn("TEST");
            table.put(mockRegister, 0x55555555);

            // When & Then: Check alternating bit pattern
            for (int i = 0; i < 32; i++) {
                Integer bitValue = table.get("TEST_" + i);
                if (i % 2 == 0) {
                    assertThat(bitValue).isEqualTo(1); // Even bits should be 1
                } else {
                    assertThat(bitValue).isEqualTo(0); // Odd bits should be 0
                }
            }
        }

        @Test
        @DisplayName("Should handle all bits set")
        void testFlagBits_AllBitsSet_ReturnsOnes() {
            // Given: Register with all bits set
            RegisterId mockRegister = mock(RegisterId.class);
            when(mockRegister.getName()).thenReturn("ALL_ONES");
            table.put(mockRegister, 0xFFFFFFFF);

            // When & Then: All bits should be 1
            for (int i = 0; i < 32; i++) {
                Integer bitValue = table.get("ALL_ONES_" + i);
                assertThat(bitValue).isEqualTo(1);
            }
        }

        @Test
        @DisplayName("Should handle all bits clear")
        void testFlagBits_AllBitsClear_ReturnsZeros() {
            // Given: Register with all bits clear
            RegisterId mockRegister = mock(RegisterId.class);
            when(mockRegister.getName()).thenReturn("ALL_ZEROS");
            table.put(mockRegister, 0);

            // When & Then: All bits should be 0
            for (int i = 0; i < 32; i++) {
                Integer bitValue = table.get("ALL_ZEROS_" + i);
                assertThat(bitValue).isNotNull();
                assertThat(bitValue).isEqualTo(0);
            }
        }

        @Test
        @DisplayName("Should handle negative register values")
        void testFlagBits_NegativeValue_ReturnsCorrectBits() {
            // Given: Negative value (-1 = 0xFFFFFFFF)
            RegisterId mockRegister = mock(RegisterId.class);
            when(mockRegister.getName()).thenReturn("NEGATIVE");
            table.put(mockRegister, -1);

            // When & Then: All bits should be 1 for -1
            assertThat(table.get("NEGATIVE_31")).isEqualTo(1); // Sign bit
            assertThat(table.get("NEGATIVE_0")).isEqualTo(1); // LSB
        }
    }

    @Nested
    @DisplayName("Multiple Registers Tests")
    class MultipleRegistersTests {

        private RegisterTable table;

        @BeforeEach
        void setUp() {
            table = new RegisterTable();
        }

        @Test
        @DisplayName("Should store multiple registers independently")
        void testMultipleRegisters_IndependentStorage_MaintainsSeparateValues() {
            // Given
            RegisterId reg1 = mock(RegisterId.class);
            RegisterId reg2 = mock(RegisterId.class);
            RegisterId reg3 = mock(RegisterId.class);
            when(reg1.getName()).thenReturn("R0");
            when(reg2.getName()).thenReturn("R1");
            when(reg3.getName()).thenReturn("PC");

            // When
            table.put(reg1, 100);
            table.put(reg2, 200);
            table.put(reg3, 0x12345678);

            // Then
            assertThat(table.get("R0")).isEqualTo(100);
            assertThat(table.get("R1")).isEqualTo(200);
            assertThat(table.get("PC")).isEqualTo(0x12345678);
        }

        @Test
        @DisplayName("Should handle many registers efficiently")
        void testMultipleRegisters_ManyRegisters_HandlesEfficiently() {
            // Given: Create many registers
            Map<String, Integer> expectedValues = new HashMap<>();
            for (int i = 0; i < 100; i++) {
                RegisterId reg = mock(RegisterId.class);
                String regName = "R" + i;
                when(reg.getName()).thenReturn(regName);

                Integer value = i * 10;
                expectedValues.put(regName, value);
                table.put(reg, value);
            }

            // When & Then: Verify all values
            for (Map.Entry<String, Integer> entry : expectedValues.entrySet()) {
                assertThat(table.get(entry.getKey())).isEqualTo(entry.getValue());
            }
        }

        @Test
        @DisplayName("Should handle register name collisions correctly")
        void testMultipleRegisters_NameCollisions_HandlesCorrectly() {
            // Given: Two different RegisterId objects with same name
            RegisterId reg1 = mock(RegisterId.class);
            RegisterId reg2 = mock(RegisterId.class);
            when(reg1.getName()).thenReturn("SAME_NAME");
            when(reg2.getName()).thenReturn("SAME_NAME");

            // When
            table.put(reg1, 100);
            table.put(reg2, 200); // Should overwrite

            // Then
            assertThat(table.get("SAME_NAME")).isEqualTo(200);
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        private RegisterTable table;

        @BeforeEach
        void setUp() {
            table = new RegisterTable();
        }

        @Test
        @DisplayName("Should return empty string for empty table")
        void testToString_EmptyTable_ReturnsEmptyString() {
            // When
            String result = table.toString();

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should format single register correctly")
        void testToString_SingleRegister_FormatsCorrectly() {
            // Given
            RegisterId reg = mock(RegisterId.class);
            when(reg.getName()).thenReturn("R0");
            table.put(reg, 42);

            // When
            String result = table.toString();

            // Then
            assertThat(result).isEqualTo("R0: 42\n");
        }

        @Test
        @DisplayName("Should format multiple registers in sorted order")
        void testToString_MultipleRegisters_SortedFormat() {
            // Given
            RegisterId reg1 = mock(RegisterId.class);
            RegisterId reg2 = mock(RegisterId.class);
            RegisterId reg3 = mock(RegisterId.class);
            when(reg1.getName()).thenReturn("R2");
            when(reg2.getName()).thenReturn("R0");
            when(reg3.getName()).thenReturn("R1");

            table.put(reg1, 200);
            table.put(reg2, 100);
            table.put(reg3, 150);

            // When
            String result = table.toString();

            // Then: Should be sorted alphabetically
            assertThat(result).isEqualTo("R0: 100\nR1: 150\nR2: 200\n");
        }

        @Test
        @DisplayName("Should handle negative values in toString")
        void testToString_NegativeValues_FormatsCorrectly() {
            // Given
            RegisterId reg = mock(RegisterId.class);
            when(reg.getName()).thenReturn("NEG");
            table.put(reg, -123);

            // When
            String result = table.toString();

            // Then
            assertThat(result).isEqualTo("NEG: -123\n");
        }

        @Test
        @DisplayName("Should handle zero values in toString")
        void testToString_ZeroValues_FormatsCorrectly() {
            // Given
            RegisterId reg = mock(RegisterId.class);
            when(reg.getName()).thenReturn("ZERO");
            table.put(reg, 0);

            // When
            String result = table.toString();

            // Then
            assertThat(result).isEqualTo("ZERO: 0\n");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        private RegisterTable table;

        @BeforeEach
        void setUp() {
            table = new RegisterTable();
        }

        @Test
        @DisplayName("Should handle very long register names")
        void testEdgeCase_LongRegisterNames_HandlesCorrectly() {
            // Given
            RegisterId reg = mock(RegisterId.class);
            String longName = "VERY_LONG_REGISTER_NAME_WITH_MANY_CHARACTERS";
            when(reg.getName()).thenReturn(longName);
            table.put(reg, 12345);

            // When
            Integer result = table.get(longName);

            // Then
            assertThat(result).isEqualTo(12345);
        }

        @Test
        @DisplayName("Should handle register names with special characters")
        void testEdgeCase_SpecialCharacters_HandlesCorrectly() {
            // Given
            String[] specialNames = { "REG.FLAG", "REG-NAME", "REG[0]", "REG$VAR" };

            for (int i = 0; i < specialNames.length; i++) {
                RegisterId reg = mock(RegisterId.class);
                when(reg.getName()).thenReturn(specialNames[i]);
                table.put(reg, i * 100);

                // When
                Integer result = table.get(specialNames[i]);

                // Then
                assertThat(result).isEqualTo(i * 100);
            }
        }

        @Test
        @DisplayName("Should handle boundary bit positions")
        void testEdgeCase_BoundaryBitPositions_HandlesCorrectly() {
            // Given
            RegisterId reg = mock(RegisterId.class);
            when(reg.getName()).thenReturn("BOUNDARY");
            table.put(reg, 0x80000001); // Bit 31 and bit 0 set

            // When & Then
            assertThat(table.get("BOUNDARY_0")).isEqualTo(1); // LSB
            assertThat(table.get("BOUNDARY_31")).isEqualTo(1); // MSB
            assertThat(table.get("BOUNDARY_15")).isEqualTo(0); // Middle bit
        }

        @Test
        @DisplayName("Should handle out-of-range bit positions gracefully")
        void testEdgeCase_OutOfRangeBitPositions_HandlesGracefully() {
            // Given
            RegisterId reg = mock(RegisterId.class);
            when(reg.getName()).thenReturn("TEST");
            table.put(reg, 0xFFFFFFFF);

            // When & Then: Should handle gracefully (implementation dependent)
            Integer result32 = table.get("TEST_32"); // Beyond 32-bit range
            Integer resultNeg = table.get("TEST_-1"); // Negative bit position

            // These may return null or handle differently based on SpecsBits implementation
            // Just verify they don't throw exceptions
            assertThat(result32).isNotNull().isInstanceOfAny(Integer.class);
            assertThat(resultNeg).isNotNull().isInstanceOfAny(Integer.class);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with RegisterUtils integration")
        void testIntegration_WithRegisterUtils_WorksCorrectly() {
            // Given
            RegisterTable table = new RegisterTable();
            RegisterId reg = mock(RegisterId.class);
            when(reg.getName()).thenReturn("MSR");
            table.put(reg, 0x80000000); // Set bit 31

            // When: Use RegisterUtils to build flag notation
            String flagBit = RegisterUtils.buildRegisterBit(reg, 31);
            Integer flagValue = table.get(flagBit);

            // Then
            assertThat(flagBit).isEqualTo("MSR_31");
            assertThat(flagValue).isEqualTo(1);
        }

        @Test
        @DisplayName("Should support processor state simulation")
        void testIntegration_ProcessorStateSimulation_WorksCorrectly() {
            // Given: Simulate ARM processor state
            RegisterTable table = new RegisterTable();

            // Set up registers
            String[] registers = { "R0", "R1", "R2", "SP", "LR", "PC", "CPSR" };
            Integer[] values = { 0x100, 0x200, 0x300, 0x7FFFFFFF, 0x8000, 0x1000, 0x10000000 };

            for (int i = 0; i < registers.length; i++) {
                RegisterId reg = mock(RegisterId.class);
                when(reg.getName()).thenReturn(registers[i]);
                table.put(reg, values[i]);
            }

            // When & Then: Verify complete processor state
            for (int i = 0; i < registers.length; i++) {
                assertThat(table.get(registers[i])).isEqualTo(values[i]);
            }

            // Check CPSR flags
            assertThat(table.get("CPSR_28")).isEqualTo(1); // V flag (bit 28)
        }
    }
}
