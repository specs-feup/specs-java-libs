package pt.up.fe.specs.util.asm.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link InstructionName}.
 * 
 * This interface provides instruction naming functionality for assembly
 * processors, including categorization of load/store instructions and
 * instruction enumeration mapping.
 * Tests verify method contracts and typical assembly instruction patterns.
 * 
 * @author Generated Tests
 */
@DisplayName("InstructionName Tests")
class InstructionNameTest {

    @Nested
    @DisplayName("Load Instructions Tests")
    class LoadInstructionsTests {

        @Test
        @DisplayName("Should return collection of load instructions")
        void testGetLoadInstructions_Implementation_ReturnsCollection() {
            // Given
            InstructionName instructionName = new TestInstructionName();

            // When
            Collection<String> loadInstructions = instructionName.getLoadInstructions();

            // Then
            assertThat(loadInstructions).isNotNull();
            assertThat(loadInstructions).containsExactlyInAnyOrder("LDR", "LDRB", "LDRH", "LDM");
        }

        @Test
        @DisplayName("Should handle empty load instructions collection")
        void testGetLoadInstructions_EmptyCollection_ReturnsEmpty() {
            // Given
            InstructionName instructionName = mock(InstructionName.class);
            when(instructionName.getLoadInstructions()).thenReturn(Collections.emptyList());

            // When
            Collection<String> loadInstructions = instructionName.getLoadInstructions();

            // Then
            assertThat(loadInstructions).isNotNull();
            assertThat(loadInstructions).isEmpty();
        }

        @Test
        @DisplayName("Should support various load instruction formats")
        void testGetLoadInstructions_VariousFormats_ReturnsValidInstructions() {
            // Given
            InstructionName instructionName = mock(InstructionName.class);
            List<String> expectedInstructions = Arrays.asList(
                    "LD", "LDW", "LDB", "LDH", "LDM", "LDP", "LDUR", "LDAR");
            when(instructionName.getLoadInstructions()).thenReturn(expectedInstructions);

            // When
            Collection<String> loadInstructions = instructionName.getLoadInstructions();

            // Then
            assertThat(loadInstructions).containsExactlyInAnyOrderElementsOf(expectedInstructions);
        }

        @Test
        @DisplayName("Should maintain collection immutability contract")
        void testGetLoadInstructions_ImmutabilityContract_ConsistentResults() {
            // Given
            InstructionName instructionName = new TestInstructionName();

            // When
            Collection<String> instructions1 = instructionName.getLoadInstructions();
            Collection<String> instructions2 = instructionName.getLoadInstructions();

            // Then
            assertThat(instructions1).containsExactlyInAnyOrderElementsOf(instructions2);
        }
    }

    @Nested
    @DisplayName("Store Instructions Tests")
    class StoreInstructionsTests {

        @Test
        @DisplayName("Should return collection of store instructions")
        void testGetStoreInstructions_Implementation_ReturnsCollection() {
            // Given
            InstructionName instructionName = new TestInstructionName();

            // When
            Collection<String> storeInstructions = instructionName.getStoreInstructions();

            // Then
            assertThat(storeInstructions).isNotNull();
            assertThat(storeInstructions).containsExactlyInAnyOrder("STR", "STRB", "STRH", "STM");
        }

        @Test
        @DisplayName("Should handle empty store instructions collection")
        void testGetStoreInstructions_EmptyCollection_ReturnsEmpty() {
            // Given
            InstructionName instructionName = mock(InstructionName.class);
            when(instructionName.getStoreInstructions()).thenReturn(Collections.emptyList());

            // When
            Collection<String> storeInstructions = instructionName.getStoreInstructions();

            // Then
            assertThat(storeInstructions).isNotNull();
            assertThat(storeInstructions).isEmpty();
        }

        @Test
        @DisplayName("Should support various store instruction formats")
        void testGetStoreInstructions_VariousFormats_ReturnsValidInstructions() {
            // Given
            InstructionName instructionName = mock(InstructionName.class);
            List<String> expectedInstructions = Arrays.asList(
                    "ST", "STW", "STB", "STH", "STM", "STP", "STUR", "STLR");
            when(instructionName.getStoreInstructions()).thenReturn(expectedInstructions);

            // When
            Collection<String> storeInstructions = instructionName.getStoreInstructions();

            // Then
            assertThat(storeInstructions).containsExactlyInAnyOrderElementsOf(expectedInstructions);
        }

        @Test
        @DisplayName("Should maintain collection immutability contract")
        void testGetStoreInstructions_ImmutabilityContract_ConsistentResults() {
            // Given
            InstructionName instructionName = new TestInstructionName();

            // When
            Collection<String> instructions1 = instructionName.getStoreInstructions();
            Collection<String> instructions2 = instructionName.getStoreInstructions();

            // Then
            assertThat(instructions1).containsExactlyInAnyOrderElementsOf(instructions2);
        }
    }

    @Nested
    @DisplayName("Name Tests")
    class NameTests {

        @Test
        @DisplayName("Should return processor name")
        void testGetName_Implementation_ReturnsName() {
            // Given
            InstructionName instructionName = new TestInstructionName();

            // When
            String name = instructionName.getName();

            // Then
            assertThat(name).isNotNull();
            assertThat(name).isEqualTo("ARM");
        }

        @Test
        @DisplayName("Should handle null name gracefully")
        void testGetName_NullName_ReturnsNull() {
            // Given
            InstructionName instructionName = mock(InstructionName.class);
            when(instructionName.getName()).thenReturn(null);

            // When
            String name = instructionName.getName();

            // Then
            assertThat(name).isNull();
        }

        @Test
        @DisplayName("Should handle empty name")
        void testGetName_EmptyName_ReturnsEmpty() {
            // Given
            InstructionName instructionName = mock(InstructionName.class);
            when(instructionName.getName()).thenReturn("");

            // When
            String name = instructionName.getName();

            // Then
            assertThat(name).isEmpty();
        }

        @Test
        @DisplayName("Should support various processor names")
        void testGetName_VariousProcessors_ReturnsValidNames() {
            // Given
            String[] processorNames = { "ARM", "x86", "MIPS", "RISC-V", "PowerPC", "SPARC" };

            for (String processorName : processorNames) {
                InstructionName instructionName = mock(InstructionName.class);
                when(instructionName.getName()).thenReturn(processorName);

                // When
                String name = instructionName.getName();

                // Then
                assertThat(name).isEqualTo(processorName);
            }
        }
    }

    @Nested
    @DisplayName("Enum Mapping Tests")
    class EnumMappingTests {

        @Test
        @DisplayName("Should return enum for valid instruction name")
        void testGetEnum_ValidInstructionName_ReturnsEnum() {
            // Given
            InstructionName instructionName = new TestInstructionName();
            String instructionString = "ADD";

            // When
            Enum<?> result = instructionName.getEnum(instructionString);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.name()).isEqualTo("ADD");
        }

        @Test
        @DisplayName("Should handle invalid instruction name")
        void testGetEnum_InvalidInstructionName_ReturnsNull() {
            // Given
            InstructionName instructionName = new TestInstructionName();
            String invalidInstruction = "INVALID_INSTRUCTION";

            // When
            Enum<?> result = instructionName.getEnum(invalidInstruction);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle null instruction name")
        void testGetEnum_NullInstructionName_ReturnsNull() {
            // Given
            InstructionName instructionName = new TestInstructionName();

            // When
            Enum<?> result = instructionName.getEnum(null);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle empty instruction name")
        void testGetEnum_EmptyInstructionName_ReturnsNull() {
            // Given
            InstructionName instructionName = new TestInstructionName();

            // When
            Enum<?> result = instructionName.getEnum("");

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should support case-sensitive instruction matching")
        void testGetEnum_CaseSensitive_ReturnsCorrectEnum() {
            // Given
            InstructionName instructionName = new TestInstructionName();

            // When
            Enum<?> upperCase = instructionName.getEnum("ADD");
            Enum<?> lowerCase = instructionName.getEnum("add");

            // Then
            assertThat(upperCase).isNotNull();
            assertThat(upperCase.name()).isEqualTo("ADD");
            assertThat(lowerCase).isNull(); // Case sensitive
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should provide complete instruction set interface")
        void testIntegration_CompleteInterface_AllMethodsWork() {
            // Given
            InstructionName instructionName = new TestInstructionName();

            // When
            Collection<String> loadInstructions = instructionName.getLoadInstructions();
            Collection<String> storeInstructions = instructionName.getStoreInstructions();
            String name = instructionName.getName();
            Enum<?> enumValue = instructionName.getEnum("ADD");

            // Then
            assertThat(loadInstructions).isNotEmpty();
            assertThat(storeInstructions).isNotEmpty();
            assertThat(name).isNotNull();
            assertThat(enumValue).isNotNull();
        }

        @Test
        @DisplayName("Should maintain consistency between load and store instructions")
        void testIntegration_LoadStoreConsistency_NoOverlap() {
            // Given
            InstructionName instructionName = new TestInstructionName();

            // When
            Collection<String> loadInstructions = instructionName.getLoadInstructions();
            Collection<String> storeInstructions = instructionName.getStoreInstructions();

            // Then: Load and store instructions should not overlap
            for (String loadInstr : loadInstructions) {
                assertThat(storeInstructions).doesNotContain(loadInstr);
            }
        }

        @Test
        @DisplayName("Should support instruction categorization")
        void testIntegration_InstructionCategorization_ValidCategories() {
            // Given
            InstructionName instructionName = new TestInstructionName();
            Collection<String> loadInstructions = instructionName.getLoadInstructions();
            Collection<String> storeInstructions = instructionName.getStoreInstructions();

            // When checking if specific instructions are correctly categorized
            boolean hasLoadInstruction = loadInstructions.stream()
                    .anyMatch(instr -> instr.contains("LD") || instr.contains("LOAD"));
            boolean hasStoreInstruction = storeInstructions.stream()
                    .anyMatch(instr -> instr.contains("ST") || instr.contains("STORE"));

            // Then
            assertThat(hasLoadInstruction).isTrue();
            assertThat(hasStoreInstruction).isTrue();
        }
    }

    // Test implementation of InstructionName interface
    private static class TestInstructionName implements InstructionName {

        private enum TestInstructionEnum {
            ADD, SUB, MUL, DIV, MOV, CMP
        }

        @Override
        public Collection<String> getLoadInstructions() {
            return Arrays.asList("LDR", "LDRB", "LDRH", "LDM");
        }

        @Override
        public Collection<String> getStoreInstructions() {
            return Arrays.asList("STR", "STRB", "STRH", "STM");
        }

        @Override
        public String getName() {
            return "ARM";
        }

        @Override
        public Enum<?> getEnum(String instructionName) {
            if (instructionName == null || instructionName.isEmpty()) {
                return null;
            }

            try {
                return TestInstructionEnum.valueOf(instructionName);
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}
