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
package pt.up.fe.specs.util.asm.processor;

/**
 * Indicates instructions where the control flow may change in architectures with delay slots.
 * <p>
 * <strong>Semantic model:</strong> This helper tracks at most <em>one</em> pending jump at a time. When a jump
 * instruction with N (&gt;0) delay slots is seen, the next N instructions are treated as delay slot instructions; the
 * last of those delay slot instructions (i.e. when the internal counter reaches 1) is reported as a jump point
 * ( {@link #isJumpPoint()} returns {@code true}). If the jump has zero delay slots it is reported immediately.
 * <p>
 * <strong>Nested / overlapping jumps:</strong> If another jump instruction appears while there is still an outstanding
 * (unresolved) delay slot sequence in progress, the new jump is <em>ignored</em>. No queuing or stacking of multiple
 * future jump events is performed. This mirrors a simplified single in-flight branch model (sufficient for consumers
 * such as basic block detection and consistent with common single delay-slot architectures like MicroBlaze, where a
 * branch in the delay slot does not create a second deferred branch resolution event).
 * <p>
 * Rationale: Supporting multiple overlapping delayed jumps would require a queue of pending delay-slot counts and a
 * richer API (e.g., multiple jump flags or an event list). Current use cases only require identifying basic block
 * boundaries under a single pending jump assumption.
 *
 * @author Joao Bispo
 */
public class DelaySlotBranchCorrector {

    public DelaySlotBranchCorrector() {
        this.currentDelaySlot = 0;
    }

    public void giveInstruction(boolean isJump, int delaySlots) {
        this.wasJump = this.isJump;
        this.isJump = isJump(isJump, delaySlots);
    }

    /**
     * @return true if the control-flow can change after the given instruction
     */
    public boolean isJumpPoint() {
        return this.isJump;
    }

    /**
     *
     * @return true if the control-flow could have changed between the given
     *         instruction and the one before.
     */
    public boolean wasJumpPoint() {
        return this.wasJump;
    }

    /**
     *
     * @return true if the current instruction is a jump
     */
    private boolean isJump(boolean isJump, int delaySlots) {
        // If we are currently in a delay slot that is not the last, just decrement.
        if (this.currentDelaySlot > 1) {
            this.currentDelaySlot--;
            return false;
        }

        // This is the last delay slot. This instruction will jump.
        if (this.currentDelaySlot == 1) {
            this.currentDelaySlot--;
            return true;
        }

        // Check if it is a jump instruction
        if (isJump) {
            return processJump(delaySlots);
        }

        // It is not a jump instruction
        return false;
    }

    private boolean processJump(int delaySlots) {
        // Check if it has delay slots
        if (delaySlots > 0) {
            this.currentDelaySlot = delaySlots;
            return false;
        }

        return true;
    }

    private int currentDelaySlot;
    private boolean isJump;
    private boolean wasJump;
}
