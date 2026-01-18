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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.asm.processor;

/**
 * Detects jumps from a list of instructions.
 *
 * @author Joao Bispo
 */
public interface JumpDetector {

    /**
     * Feeds an instruction to the detector.
     *
     */
    public void giveInstruction(Object instruction);

    /**
     * Detects if there was a jump between the given instruction and the instruction
     * before the given instruction.
     *
     * <p>
     * Even if a branch is not taken (i.e. the given instruction is the instruction
     * in the next address of the instruction given before), it counts as a jump.
     *
     * <p>
     * If the method returns true, this means that the given instruction is the
     * start of a BasicBlock.
     *
     * @return true, if the given instruction is the first instruction after a jump.
     *         false otherwise
     *
     */
    public boolean wasJumpPoint();

    /**
     *
     * @return true if the last given instruction is a jump point.
     */
    public boolean isJumpPoint();

    /**
     *
     * @return true if the last given instruction is a jump and the jump is
     *         conditional, false if it is a jump but
     *         unconditional. Null if there was no jump.
     */
    public Boolean isConditionalJump();

    /**
     *
     * @return true if there was a jump and the jump was conditional, false if it
     *         was not. Null if there was no jump.
     */
    public Boolean wasConditionalJump();

    /**
     *
     * @return true if there was a jump and the jump direction was forward, false if
     *         it was not. Null if there was no jump.
     */
    public Boolean wasForwardJump();

    /**
     *
     * @return true if there was a conditional jump and the jump was taken. false if
     *         it was not. Null if there was no jump, or if the jump was not
     *         conditional.
     */
    public Boolean wasBranchTaken();

}
