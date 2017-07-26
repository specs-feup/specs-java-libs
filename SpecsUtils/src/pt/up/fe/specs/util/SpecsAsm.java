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

package pt.up.fe.specs.util;

import pt.up.fe.specs.util.asm.ArithmeticResult32;

/**
 * Utility methods related with solving operations.
 *
 * @author Joao Bispo
 */
public class SpecsAsm {

    public static long add64(long input1, long input2, long carry) {
        return input1 + input2 + carry;
    }

    public static long rsub64(long input1, long input2, long carry) {
        return input2 + ~input1 + carry;
    }

    /*
    public static ArithmeticResult32 add32(int firstTerm, int secondTerm) {
      return add32(firstTerm, secondTerm, CARRY_NEUTRAL_ADD);
    }
     *
     */

    /**
     * Calculates the carryOut of the sum of rA with rB and carry. Operation is rA + rB + carry.
     *
     * @param input1
     * @param input2
     * @param carry
     *            the carry from the previous operation. Should be 0 or 1.
     * @return 1 if there is carry out, or 0 if not.
     */
    public static ArithmeticResult32 add32(int input1, int input2, int carry) {
        if (carry != 0 && carry != 1) {
            SpecsLogs.getLogger().warning("Carry is different than 0 or 1 (" +
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
     * Calculates the carryOut of the reverse subtraction of rA with rB and carry. Operation is rB + ~rA + carry.
     *
     * @param input1
     * @param input2
     * @param carry
     *            the carry from the previous operation. Should be 0 or 1.
     * @return 1 if there is carry out, or 0 if not.
     */
    public static ArithmeticResult32 rsub32(int input1, int input2, int carry) {
        if (carry != 0 && carry != 1) {
            SpecsLogs.getLogger().warning("Carry is different than 0 or 1 (" +
                    carry + ")");
        }

        // Extend operands to long and mask them
        long lRa = input1 & SpecsBits.getMask32Bits();
        long lRb = input2 & SpecsBits.getMask32Bits();
        // Carry must be 0 or 1, it shouldn't need to be masked.
        long lCarry = carry;

        // Do the summation
        // long result = lRb + ~lRa + lCarry;
        long result = rsub64(lRa, lRb, lCarry);
        int maskedResult = (int) result;

        // Get the carry bit
        int carryOut = (int) ((result & SpecsBits.getMaskBit33()) >>> 32);
        return new ArithmeticResult32(maskedResult, carryOut);
    }

    /*
    public static ArithmeticResult32 rsub32(int firstTerm, int secondTerm) {
      return rsub32(firstTerm, secondTerm, CARRY_NEUTRAL_SUB);
    }
     * 
     */

    public static int and32(int input1, int input2) {
        return input1 & input2;
    }

    public static int andNot32(int input1, int input2) {
        return input1 & ~input2;
    }

    public static int not32(int input1) {
        return ~input1;
    }

    public static int or32(int input1, int input2) {
        return input1 | input2;
    }

    public static int xor32(int input1, int input2) {
        return input1 ^ input2;
    }

    public static int mbCompareSigned(int input1, int input2) {
        int result = input2 + ~input1 + 1;
        boolean aBiggerThanB = input1 > input2;

        // Change MSB to reflect relation
        if (aBiggerThanB) {
            return SpecsBits.setBit(31, result);
        }
        return SpecsBits.clearBit(31, result);
    }

    public static int mbCompareUnsigned(int input1, int input2) {
        int result = input2 + ~input1 + 1;
        boolean aBiggerThanB = SpecsBits.unsignedComp(input1, input2);

        // Change MSB to reflect relation
        if (aBiggerThanB) {
            return SpecsBits.setBit(31, result);
        }

        return SpecsBits.clearBit(31, result);
    }

    public static int shiftLeftLogical(int input1, int input2) {
        return input1 << input2;
    }

    public static int shiftRightArithmetical(int input1, int input2) {
        return input1 >> input2;
    }

    public static int shiftRightLogical(int input1, int input2) {
        return input1 >>> input2;
    }

    /**
     *
     * @param input1
     * @param input2
     * @param input3
     *            the number of LSB bits of input2 to take into account
     * @return
     */
    public static int shiftLeftLogical(int input1, int input2, int input3) {
        input2 = SpecsBits.mask(input2, input3);
        return input1 << input2;
    }

    public static int shiftRightArithmetical(int input1, int input2, int input3) {
        input2 = SpecsBits.mask(input2, input3);
        return input1 >> input2;
    }

    public static int shiftRightLogical(int input1, int input2, int input3) {
        input2 = SpecsBits.mask(input2, input3);
        return input1 >>> input2;
    }

    public static final int CARRY_NEUTRAL_ADD = 0;
    public static final int CARRY_NEUTRAL_SUB = 1;

}
