package pt.up.fe.specs.util.asm.processor;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link JumpDetector}.
 * 
 * This interface detects jumps and control flow changes in instruction
 * sequences, supporting basic block detection and branch analysis. Tests verify
 * jump detection, state management, and branch condition analysis
 * functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("JumpDetector Tests")
class JumpDetectorTest {

    @Nested
    @DisplayName("Instruction Feeding Tests")
    class InstructionFeedingTests {

        @Test
        @DisplayName("Should accept instruction objects")
        void testGiveInstruction_ValidInstruction_AcceptsSuccessfully() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            Object instruction = "ADD R0, R1, R2";

            // When & Then: Should not throw exception
            detector.giveInstruction(instruction);
        }

        @Test
        @DisplayName("Should handle null instruction")
        void testGiveInstruction_NullInstruction_HandlesGracefully() {
            // Given
            JumpDetector detector = new TestJumpDetector();

            // When & Then: Should not throw exception
            detector.giveInstruction(null);
        }

        @Test
        @DisplayName("Should handle various instruction types")
        void testGiveInstruction_VariousTypes_HandlesCorrectly() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            Object[] instructions = {
                    "BEQ Label1", // String instruction
                    42, // Integer instruction
                    new TestInstruction("JMP"), // Custom object
                    "" // Empty string
            };

            // When & Then: Should handle all types
            for (Object instruction : instructions) {
                detector.giveInstruction(instruction);
            }
        }

        @Test
        @DisplayName("Should process instruction sequence")
        void testGiveInstruction_InstructionSequence_ProcessesSequentially() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            String[] sequence = {
                    "MOV R0, #10",
                    "ADD R1, R0, #5",
                    "BEQ Label1",
                    "SUB R2, R1, #3"
            };

            // When: Feed sequence
            for (String instruction : sequence) {
                detector.giveInstruction(instruction);
            }

            // Then: Should have processed all instructions without errors
            assertThat(detector.isJumpPoint()).isFalse(); // Last instruction is not a jump
        }
    }

    @Nested
    @DisplayName("Jump Point Detection Tests")
    class JumpPointDetectionTests {

        @Test
        @DisplayName("Should detect current instruction as jump point")
        void testIsJumpPoint_JumpInstruction_ReturnsTrue() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("BEQ Label1");

            // When
            boolean isJump = detector.isJumpPoint();

            // Then
            assertThat(isJump).isTrue();
        }

        @Test
        @DisplayName("Should detect non-jump instruction correctly")
        void testIsJumpPoint_NonJumpInstruction_ReturnsFalse() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("ADD R0, R1, R2");

            // When
            boolean isJump = detector.isJumpPoint();

            // Then
            assertThat(isJump).isFalse();
        }

        @Test
        @DisplayName("Should handle initial state correctly")
        void testIsJumpPoint_InitialState_ReturnsFalse() {
            // Given
            JumpDetector detector = new TestJumpDetector();

            // When
            boolean isJump = detector.isJumpPoint();

            // Then
            assertThat(isJump).isFalse();
        }

        @Test
        @DisplayName("Should detect various jump instruction types")
        void testIsJumpPoint_VariousJumpTypes_DetectsCorrectly() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            String[] jumpInstructions = { "BEQ", "BNE", "JMP", "CALL", "RET", "BR" };

            for (String jumpInstr : jumpInstructions) {
                // When
                detector.giveInstruction(jumpInstr);
                boolean isJump = detector.isJumpPoint();

                // Then
                assertThat(isJump).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("Previous Jump Detection Tests")
    class PreviousJumpDetectionTests {

        @Test
        @DisplayName("Should detect previous instruction was jump point")
        void testWasJumpPoint_PreviousJump_ReturnsTrue() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("BEQ Label1"); // Jump instruction
            detector.giveInstruction("ADD R0, R1, R2"); // Non-jump instruction

            // When
            boolean wasJump = detector.wasJumpPoint();

            // Then
            assertThat(wasJump).isTrue();
        }

        @Test
        @DisplayName("Should detect previous instruction was not jump point")
        void testWasJumpPoint_PreviousNonJump_ReturnsFalse() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("MOV R0, #10"); // Non-jump instruction
            detector.giveInstruction("ADD R0, R1, R2"); // Non-jump instruction

            // When
            boolean wasJump = detector.wasJumpPoint();

            // Then
            assertThat(wasJump).isFalse();
        }

        @Test
        @DisplayName("Should handle sequence with multiple jumps")
        void testWasJumpPoint_MultipleJumps_TracksCorrectly() {
            // Given
            JumpDetector detector = new TestJumpDetector();

            // First sequence: non-jump -> jump
            detector.giveInstruction("MOV R0, #10");
            detector.giveInstruction("BEQ Label1");
            assertThat(detector.wasJumpPoint()).isFalse(); // Previous was not jump

            // Second sequence: jump -> non-jump
            detector.giveInstruction("ADD R0, R1, R2");
            assertThat(detector.wasJumpPoint()).isTrue(); // Previous was jump
        }

        @Test
        @DisplayName("Should handle initial state for wasJumpPoint")
        void testWasJumpPoint_InitialState_ReturnsFalse() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("ADD R0, R1, R2"); // First instruction

            // When
            boolean wasJump = detector.wasJumpPoint();

            // Then
            assertThat(wasJump).isFalse(); // No previous instruction
        }
    }

    @Nested
    @DisplayName("Conditional Jump Tests")
    class ConditionalJumpTests {

        @Test
        @DisplayName("Should identify conditional jump correctly")
        void testIsConditionalJump_ConditionalJump_ReturnsTrue() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("BEQ Label1"); // Conditional jump

            // When
            Boolean isConditional = detector.isConditionalJump();

            // Then
            assertThat(isConditional).isTrue();
        }

        @Test
        @DisplayName("Should identify unconditional jump correctly")
        void testIsConditionalJump_UnconditionalJump_ReturnsFalse() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("JMP Label1"); // Unconditional jump

            // When
            Boolean isConditional = detector.isConditionalJump();

            // Then
            assertThat(isConditional).isFalse();
        }

        @Test
        @DisplayName("Should return null for non-jump instruction")
        void testIsConditionalJump_NonJump_ReturnsNull() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("ADD R0, R1, R2"); // Non-jump

            // When
            Boolean isConditional = detector.isConditionalJump();

            // Then
            assertThat(isConditional).isNull();
        }

        @Test
        @DisplayName("Should detect various conditional jump types")
        void testIsConditionalJump_VariousConditionals_DetectsCorrectly() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            String[] conditionalJumps = { "BEQ", "BNE", "BGT", "BLT", "BGE", "BLE" };

            for (String condJump : conditionalJumps) {
                // When
                detector.giveInstruction(condJump);
                Boolean isConditional = detector.isConditionalJump();

                // Then
                assertThat(isConditional).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("Previous Conditional Jump Tests")
    class PreviousConditionalJumpTests {

        @Test
        @DisplayName("Should detect previous conditional jump")
        void testWasConditionalJump_PreviousConditional_ReturnsTrue() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("BEQ Label1"); // Conditional jump
            detector.giveInstruction("ADD R0, R1, R2"); // Non-jump

            // When
            Boolean wasConditional = detector.wasConditionalJump();

            // Then
            assertThat(wasConditional).isTrue();
        }

        @Test
        @DisplayName("Should detect previous unconditional jump")
        void testWasConditionalJump_PreviousUnconditional_ReturnsFalse() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("JMP Label1"); // Unconditional jump
            detector.giveInstruction("ADD R0, R1, R2"); // Non-jump

            // When
            Boolean wasConditional = detector.wasConditionalJump();

            // Then
            assertThat(wasConditional).isFalse();
        }

        @Test
        @DisplayName("Should return null when previous was not jump")
        void testWasConditionalJump_PreviousNonJump_ReturnsNull() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("MOV R0, #10"); // Non-jump
            detector.giveInstruction("ADD R0, R1, R2"); // Non-jump

            // When
            Boolean wasConditional = detector.wasConditionalJump();

            // Then
            assertThat(wasConditional).isNull();
        }
    }

    @Nested
    @DisplayName("Jump Direction Tests")
    class JumpDirectionTests {

        @Test
        @DisplayName("Should detect forward jump")
        void testWasForwardJump_ForwardJump_ReturnsTrue() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("BEQ +100"); // Forward jump
            detector.giveInstruction("ADD R0, R1, R2"); // Next instruction

            // When
            Boolean wasForward = detector.wasForwardJump();

            // Then
            assertThat(wasForward).isTrue();
        }

        @Test
        @DisplayName("Should detect backward jump")
        void testWasForwardJump_BackwardJump_ReturnsFalse() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("BEQ -100"); // Backward jump
            detector.giveInstruction("ADD R0, R1, R2"); // Next instruction

            // When
            Boolean wasForward = detector.wasForwardJump();

            // Then
            assertThat(wasForward).isFalse();
        }

        @Test
        @DisplayName("Should return null when previous was not jump")
        void testWasForwardJump_PreviousNonJump_ReturnsNull() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("MOV R0, #10"); // Non-jump
            detector.giveInstruction("ADD R0, R1, R2"); // Non-jump

            // When
            Boolean wasForward = detector.wasForwardJump();

            // Then
            assertThat(wasForward).isNull();
        }
    }

    @Nested
    @DisplayName("Branch Taken Tests")
    class BranchTakenTests {

        @Test
        @DisplayName("Should detect taken conditional branch")
        void testWasBranchTaken_TakenBranch_ReturnsTrue() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("BEQ_TAKEN"); // Conditional jump taken
            detector.giveInstruction("ADD R0, R1, R2"); // Next instruction

            // When
            Boolean wasTaken = detector.wasBranchTaken();

            // Then
            assertThat(wasTaken).isTrue();
        }

        @Test
        @DisplayName("Should detect not taken conditional branch")
        void testWasBranchTaken_NotTakenBranch_ReturnsFalse() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("BEQ_NOT_TAKEN"); // Conditional jump not taken
            detector.giveInstruction("ADD R0, R1, R2"); // Next instruction

            // When
            Boolean wasTaken = detector.wasBranchTaken();

            // Then
            assertThat(wasTaken).isFalse();
        }

        @Test
        @DisplayName("Should return null for unconditional jump")
        void testWasBranchTaken_UnconditionalJump_ReturnsNull() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("JMP Label1"); // Unconditional jump
            detector.giveInstruction("ADD R0, R1, R2"); // Next instruction

            // When
            Boolean wasTaken = detector.wasBranchTaken();

            // Then
            assertThat(wasTaken).isNull();
        }

        @Test
        @DisplayName("Should return null when previous was not jump")
        void testWasBranchTaken_PreviousNonJump_ReturnsNull() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            detector.giveInstruction("MOV R0, #10"); // Non-jump
            detector.giveInstruction("ADD R0, R1, R2"); // Non-jump

            // When
            Boolean wasTaken = detector.wasBranchTaken();

            // Then
            assertThat(wasTaken).isNull();
        }
    }

    @Nested
    @DisplayName("Basic Block Detection Tests")
    class BasicBlockDetectionTests {

        @Test
        @DisplayName("Should identify basic block starts")
        void testBasicBlockDetection_JumpTargets_IdentifiesBasicBlockStarts() {
            // Given
            JumpDetector detector = new TestJumpDetector();

            // Simulate sequence: normal -> jump -> target (basic block start)
            detector.giveInstruction("MOV R0, #10");
            assertThat(detector.wasJumpPoint()).isFalse(); // Not after jump

            detector.giveInstruction("BEQ Label1");
            assertThat(detector.wasJumpPoint()).isFalse(); // Previous was not jump

            detector.giveInstruction("TARGET_INSTRUCTION"); // This starts new basic block
            boolean startsBasicBlock = detector.wasJumpPoint();

            // Then
            assertThat(startsBasicBlock).isTrue();
        }

        @Test
        @DisplayName("Should handle continuous non-jump instructions")
        void testBasicBlockDetection_ContinuousNonJumps_NoBasicBlockBoundaries() {
            // Given
            JumpDetector detector = new TestJumpDetector();
            String[] normalInstructions = {
                    "MOV R0, #10",
                    "ADD R1, R0, #5",
                    "SUB R2, R1, #3",
                    "MUL R3, R2, #2"
            };

            // When: Process sequence of normal instructions
            for (String instruction : normalInstructions) {
                detector.giveInstruction(instruction);
                boolean wasJump = detector.wasJumpPoint();

                // Then: None should start new basic block
                assertThat(wasJump).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("State Management Tests")
    class StateManagementTests {

        @Test
        @DisplayName("Should maintain consistent state across operations")
        void testStateManagement_ConsistentState_MaintainsCorrectly() {
            // Given
            JumpDetector detector = new TestJumpDetector();

            // Initial state
            assertThat(detector.isJumpPoint()).isFalse();
            assertThat(detector.wasJumpPoint()).isFalse();

            // After non-jump
            detector.giveInstruction("ADD R0, R1, R2");
            assertThat(detector.isJumpPoint()).isFalse();
            assertThat(detector.wasJumpPoint()).isFalse();

            // After jump
            detector.giveInstruction("BEQ Label1");
            assertThat(detector.isJumpPoint()).isTrue();
            assertThat(detector.wasJumpPoint()).isFalse(); // Previous was not jump

            // After another instruction
            detector.giveInstruction("MOV R0, #5");
            assertThat(detector.isJumpPoint()).isFalse();
            assertThat(detector.wasJumpPoint()).isTrue(); // Previous was jump
        }

        @Test
        @DisplayName("Should handle rapid state changes")
        void testStateManagement_RapidChanges_HandlesCorrectly() {
            // Given
            JumpDetector detector = new TestJumpDetector();

            // Rapid sequence of jumps and non-jumps
            String[] sequence = { "BEQ", "ADD", "JMP", "MOV", "BNE", "SUB" };
            boolean[] expectedIsJump = { true, false, true, false, true, false };
            boolean[] expectedWasJump = { false, true, false, true, false, true };

            for (int i = 0; i < sequence.length; i++) {
                detector.giveInstruction(sequence[i]);

                assertThat(detector.isJumpPoint()).isEqualTo(expectedIsJump[i]);
                assertThat(detector.wasJumpPoint()).isEqualTo(expectedWasJump[i]);
            }
        }
    }

    // Test implementation of JumpDetector interface
    private static class TestJumpDetector implements JumpDetector {
        private boolean currentIsJump = false;
        private boolean previousWasJump = false;
        private boolean currentIsConditional = false;
        private boolean previousWasConditional = false;
        private boolean currentIsForward = false;
        private boolean previousWasForward = false;
        private boolean currentBranchTaken = false;
        private boolean previousBranchTaken = false;
        private boolean hasPrevious = false;

        @Override
        public void giveInstruction(Object instruction) {
            // Update previous state
            previousWasJump = currentIsJump;
            previousWasConditional = currentIsConditional;
            previousWasForward = currentIsForward;
            previousBranchTaken = currentBranchTaken;
            hasPrevious = true;

            // Analyze current instruction
            if (instruction != null) {
                String instrStr = instruction.toString();
                currentIsJump = isJumpInstruction(instrStr);
                currentIsConditional = isConditionalInstruction(instrStr);
                currentIsForward = isForwardInstruction(instrStr);
                currentBranchTaken = isBranchTakenInstruction(instrStr);
            } else {
                currentIsJump = false;
                currentIsConditional = false;
                currentIsForward = false;
                currentBranchTaken = false;
            }
        }

        @Override
        public boolean wasJumpPoint() {
            return hasPrevious && previousWasJump;
        }

        @Override
        public boolean isJumpPoint() {
            return currentIsJump;
        }

        @Override
        public Boolean isConditionalJump() {
            return currentIsJump ? currentIsConditional : null;
        }

        @Override
        public Boolean wasConditionalJump() {
            return (hasPrevious && previousWasJump) ? previousWasConditional : null;
        }

        @Override
        public Boolean wasForwardJump() {
            return (hasPrevious && previousWasJump) ? previousWasForward : null;
        }

        @Override
        public Boolean wasBranchTaken() {
            return (hasPrevious && previousWasJump && previousWasConditional) ? previousBranchTaken : null;
        }

        private boolean isJumpInstruction(String instruction) {
            return instruction.matches("^(BEQ|BNE|BGT|BLT|BGE|BLE|JMP|CALL|RET|BR).*");
        }

        private boolean isConditionalInstruction(String instruction) {
            return instruction.matches("^(BEQ|BNE|BGT|BLT|BGE|BLE).*");
        }

        private boolean isForwardInstruction(String instruction) {
            return instruction.contains("+") || (!instruction.contains("-") && isJumpInstruction(instruction));
        }

        private boolean isBranchTakenInstruction(String instruction) {
            // Consider instructions ending with *_TAKEN as taken, but distinguish *_NOT_TAKEN
            // Previous implementation used contains("_TAKEN"), which incorrectly classified
            // "BEQ_NOT_TAKEN" as taken because the substring "_TAKEN" is present.
            if (instruction == null) {
                return false;
            }
            if (instruction.contains("_NOT_TAKEN")) {
                return false;
            }
            return instruction.contains("_TAKEN");
        }
    }

    // Helper class for testing
    private static class TestInstruction {
        private final String mnemonic;

        public TestInstruction(String mnemonic) {
            this.mnemonic = mnemonic;
        }

        @Override
        public String toString() {
            return mnemonic;
        }
    }
}
