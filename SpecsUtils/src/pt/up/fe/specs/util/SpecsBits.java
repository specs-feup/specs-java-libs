/*
 * Copyright 2009 SPeCS Research Group.
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

/**
 * Utility methods for bitwise operations.
 * <p>
 * Provides static helper methods for manipulating bits and binary
 * representations.
 * </p>
 *
 * @author Joao Bispo
 */
public class SpecsBits {

    /**
     * String representation of zero.
     */
    private static final String ZERO = "0";

    /**
     * Prefix for hexadecimal numbers.
     */
    private static final String HEX_PREFIX = "0x";

    /**
     * Pads the string with zeros on the left until it has the requested size, and
     * prefixes "0x" to the resulting String.
     * 
     * <p>
     * Example: <br>
     * Input - padHexString(166, 4) <br>
     * Output - 0x00A6.
     * 
     * @param hexNumber a long.
     * @param size      the pretended number of digits of the hexadecimal number.
     * @return a string
     */
    public static String padHexString(long hexNumber, int size) {
        return padHexString(Long.toHexString(hexNumber), size);
    }

    /**
     * Pads the string with zeros on the left until it has the requested size, and
     * prefixes "0x" to the resulting String.
     * 
     * <p>
     * Example:2 <br>
     * Input - padHexString(A6, 4) <br>
     * Output - 0x00A6.
     * 
     * @param hexNumber an hexadecimal number in String format.
     * @param size      the pretended number of digits of the hexadecimal number.
     * @return a string
     */
    public static String padHexString(String hexNumber, int size) {
        int stringSize = hexNumber.length();

        if (stringSize >= size) {
            return SpecsBits.HEX_PREFIX + hexNumber;
        }

        int numZeros = size - stringSize;
        String builder = SpecsBits.HEX_PREFIX +
                SpecsBits.ZERO.repeat(numZeros);

        return builder + hexNumber;
    }

    /**
     * Decodes an unsigned byte value from a string representation.
     * 
     * @param unsignedByteValue the string representation of the unsigned byte value
     * @return the decoded unsigned byte value
     */
    public static byte decodeUnsignedByte(String unsignedByteValue) {
        // Bytes in Java are signed, decode as Short
        return Short.valueOf(unsignedByteValue).byteValue();
    }

}
