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

package pt.up.fe.specs.util;

import pt.up.fe.specs.util.asm.ArithmeticResult32;

/**
 * Utility methods for assembly code operations.
 * <p>
 * Provides static helper methods for parsing, formatting, and manipulating
 * assembly code.
 * </p>
 *
 * @author Joao Bispo
 */
public class SpecsAsm {

    /**
     * Adds two 64-bit integers and a carry value.
     *
     * @param input1 the first operand
     * @param input2 the second operand
     * @param carry  the carry value (0 or 1)
     * @return the result of the addition
     */
    public static long add64(long input1, long input2, long carry) {
        return input1 + input2 + carry;
    }

    /**
     * Performs reverse subtraction on two 64-bit integers and a carry value.
     *
     * @param input1 the first operand
     * @param input2 the second operand
     * @param carry  the carry value (0 or 1)
     * @return the result of the reverse subtraction
     */
    public static long rsub64(long input1, long input2, long carry) {
        return input2 + ~input1 + carry;
    }

    /**
     * Calculates the carryOut of the sum of rA with rB and carry. Operation is rA +
     * rB + carry.
     *
     * @param input1 the first operand
     * @param input2 the second operand
     * @param carry  the carry from the previous operation. Should be 0 or 1.
     * @return an ArithmeticResult32 containing the result and carry out
     */
    public static ArithmeticResult32 add32(int input1, int input2, int carry) {
        if (carry != 0 && carry != 1) {
            SpecsLogs.warn("Carry is different than 0 or 1 (" +
                    carry + ")");
        }

        // Extend operands to long and mask them
        long lRa = input1 & SpecsBits.getMask32Bits();
        long lRb = input2 & SpecsBits.getMask32Bits();
        // Carry must be 0 or 1, it shouldn't need to be masked.
        long lCarry = carry;

        // Do the summation
        long result = add64(lRa, lRb, lCarry);
        int maskedResult = (int) result;

        // Get the carry bit
        int carryOut = (int) ((result & SpecsBits.getMaskBit33()) >>> 32);
        return new ArithmeticResult32(maskedResult, carryOut);
    }

    /**
     * Calculates the carryOut of the reverse subtraction of rA with rB and carry.
     * Operation is rB + ~rA + carry.
     *
     * @param input1 the first operand
     * @param input2 the second operand
     * @param carry  the carry from the previous operation. Should be 0 or 1.
     * @return an ArithmeticResult32 containing the result and carry out
     */
    public static ArithmeticResult32 rsub32(int input1, int input2, int carry) {
        if (carry != 0 && carry != 1) {
            SpecsLogs.warn("Carry is different than 0 or 1 (" +
                    carry + ")");
        }

        // Extend operands to long and mask them
        long lRa = input1 & SpecsBits.getMask32Bits();
        long lRb = input2 & SpecsBits.getMask32Bits();
        // Carry must be 0 or 1, it shouldn't need to be masked.
        long lCarry = carry;

        // Do the summation
        long result = rsub64(lRa, lRb, lCarry);
        int maskedResult = (int) result;

        // Get the carry bit
        int carryOut = (int) ((result & SpecsBits.getMaskBit33()) >>> 32);
        return new ArithmeticResult32(maskedResult, carryOut);
    }

    /**
     * Performs a bitwise AND operation on two 32-bit integers.
     *
     * @param input1 the first operand
     * @param input2 the second operand
     * @return the result of the AND operation
     */
    public static int and32(int input1, int input2) {
        return input1 & input2;
    }

    /**
     * Performs a bitwise AND NOT operation on two 32-bit integers.
     *
     * @param input1 the first operand
     * @param input2 the second operand
     * @return the result of the AND NOT operation
     */
    public static int andNot32(int input1, int input2) {
        return input1 & ~input2;
    }

    /**
     * Performs a bitwise NOT operation on a 32-bit integer.
     *
     * @param input1 the operand
     * @return the result of the NOT operation
     */
    public static int not32(int input1) {
        return ~input1;
    }

    /**
     * Performs a bitwise OR operation on two 32-bit integers.
     *
     * @param input1 the first operand
     * @param input2 the second operand
     * @return the result of the OR operation
     */
    public static int or32(int input1, int input2) {
        return input1 | input2;
    }

    /**
     * Performs a bitwise XOR operation on two 32-bit integers.
     *
     * @param input1 the first operand
     * @param input2 the second operand
     * @return the result of the XOR operation
     */
    public static int xor32(int input1, int input2) {
        return input1 ^ input2;
    }

    /**
     * Compares two signed 32-bit integers and modifies the MSB to reflect the
     * relation.
     *
     * @param input1 the first operand
     * @param input2 the second operand
     * @return the result of the comparison
     */
    public static int mbCompareSigned(int input1, int input2) {
        int result = input2 + ~input1 + 1;
        boolean aBiggerThanB = input1 > input2;

        // Change MSB to reflect relation
        if (aBiggerThanB) {
            return SpecsBits.setBit(31, result);
        }
        return SpecsBits.clearBit(31, result);
    }

    /**
     * Compares two unsigned 32-bit integers and modifies the MSB to reflect the
     * relation.
     *
     * @param input1 the first operand
     * @param input2 the second operand
     * @return the result of the comparison
     */
    public static int mbCompareUnsigned(int input1, int input2) {
        int result = input2 + ~input1 + 1;
        boolean aBiggerThanB = SpecsBits.unsignedComp(input1, input2);

        // Change MSB to reflect relation
        if (aBiggerThanB) {
            return SpecsBits.setBit(31, result);
        }

        return SpecsBits.clearBit(31, result);
    }

    /**
     * Performs a logical left shift on a 32-bit integer.
     *
     * @param input1 the operand to shift
     * @param input2 the number of positions to shift
     * @return the result of the shift
     */
    public static int shiftLeftLogical(int input1, int input2) {
        return input1 << input2;
    }

    /**
     * Performs an arithmetic right shift on a 32-bit integer.
     *
     * @param input1 the operand to shift
     * @param input2 the number of positions to shift
     * @return the result of the shift
     */
    public static int shiftRightArithmetical(int input1, int input2) {
        return input1 >> input2;
    }

    /**
     * Performs a logical right shift on a 32-bit integer.
     *
     * @param input1 the operand to shift
     * @param input2 the number of positions to shift
     * @return the result of the shift
     */
    public static int shiftRightLogical(int input1, int input2) {
        return input1 >>> input2;
    }

    /**
     * Performs a logical left shift on a 32-bit integer, taking into account a
     * mask.
     *
     * @param input1 the operand to shift
     * @param input2 the number of positions to shift
     * @param input3 the number of LSB bits of input2 to take into account
     * @return the result of the shift
     */
    public static int shiftLeftLogical(int input1, int input2, int input3) {
        input2 = SpecsBits.mask(input2, input3);
        return input1 << input2;
    }

    /**
     * Performs an arithmetic right shift on a 32-bit integer, taking into account a
     * mask.
     *
     * @param input1 the operand to shift
     * @param input2 the number of positions to shift
     * @param input3 the number of LSB bits of input2 to take into account
     * @return the result of the shift
     */
    public static int shiftRightArithmetical(int input1, int input2, int input3) {
        input2 = SpecsBits.mask(input2, input3);
        return input1 >> input2;
    }

    /**
     * Performs a logical right shift on a 32-bit integer, taking into account a
     * mask.
     *
     * @param input1 the operand to shift
     * @param input2 the number of positions to shift
     * @param input3 the number of LSB bits of input2 to take into account
     * @return the result of the shift
     */
    public static int shiftRightLogical(int input1, int input2, int input3) {
        input2 = SpecsBits.mask(input2, input3);
        return input1 >>> input2;
    }
}
