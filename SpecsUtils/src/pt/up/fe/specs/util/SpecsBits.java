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
 * specific language governing permissions and limitations under the License. under the License.
 */
package pt.up.fe.specs.util;

import java.nio.ByteBuffer;

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

    // /
    // CONSTANTS
    // /

    /**
     * String representation of zero.
     */
    private static final String ZERO = "0";

    /**
     * Prefix for hexadecimal numbers.
     */
    private static final String HEX_PREFIX = "0x";

    /**
     * Mask for 16 bits.
     */
    private static final long MASK_16_BITS = 0xFFFFL;

    /**
     * Mask for 32 bits.
     */
    private static final long MASK_32_BITS = 0xFFFFFFFFL;

    /**
     * Mask for the 33rd bit.
     */
    private static final long MASK_BIT_33 = 0x100000000L;

    /**
     * Mask for the least significant bit.
     */
    private static final int MASK_BIT_1 = 0x1;

    /**
     * Mask for unsigned byte.
     */
    private static final int UNSIGNED_BYTE_MASK = 0x000000FF;

    // Floating-point related constants

    /**
     * Mask for denormalized floating-point numbers.
     */
    private static final int DENORMAL_MASK = 0x7F800000;

    /**
     * Mask for non-sign bits in floating-point numbers.
     */
    private static final int NOT_SIGN_MASK = 0x7FFFFFFF;

    /**
     * Mask for zero floating-point numbers.
     */
    private static final int ZERO_MASK = 0x7FFFFFFF;

    /**
     * Mask for the sign bit in floating-point numbers.
     */
    private static final int FLOAT_SIGN_MASK = 0x80000000;

    /**
     * Representation of infinity in floating-point numbers.
     */
    private static final int FLOAT_INFINITY = 0x7F800000;

    /**
     * Number of bits in a byte.
     */
    private static final int BITS_IN_A_BYTE = 8;

    /**
     * Returns the mask for 32 bits.
     * 
     * @return the mask for 32 bits
     */
    public static long getMask32Bits() {
        return MASK_32_BITS;
    }

    /**
     * Returns the mask for the 33rd bit.
     * 
     * @return the mask for the 33rd bit
     */
    public static long getMaskBit33() {
        return MASK_BIT_33;
    }

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
        StringBuilder builder = new StringBuilder(numZeros + SpecsBits.HEX_PREFIX.length());
        builder.append(SpecsBits.HEX_PREFIX);
        for (int i = 0; i < numZeros; i++) {
            builder.append(SpecsBits.ZERO);
        }

        return builder.toString() + hexNumber;
    }

    /**
     * Pads the string with zeros on the left until it has the requested size.
     * 
     * <p>
     * Example: <br>
     * Input - padBinaryString(101, 5) <br>
     * Output - 00101.
     * 
     * @param binaryNumber a binary number in String format.
     * @param size         the pretended number of digits of the binary number.
     * @return a string
     */
    public static String padBinaryString(String binaryNumber, int size) {
        int stringSize = binaryNumber.length();
        if (stringSize >= size) {
            return binaryNumber;
        }

        int numZeros = size - stringSize;
        StringBuilder builder = new StringBuilder(numZeros);
        for (int i = 0; i < numZeros; i++) {
            builder.append(SpecsBits.ZERO);
        }

        return builder.toString() + binaryNumber;
    }

    /**
     * Gets the a single bit of the integer target.
     * 
     * @param position a number between 0 and 31, inclusive, where 0 is the LSB
     * @param target   an integer
     * @return 1 if the bit at the specified position is 1; 0 otherwise
     */
    public static int getBit(int position, int target) {
        return (target >>> position) & SpecsBits.MASK_BIT_1;
    }

    /**
     * Returns an integer representing the 16 bits from the long number from a
     * specified offset.
     * 
     * @param data   a long number
     * @param offset a number between 0 and 3, inclusive
     * @return an int representing the 16 bits of the specified offset
     */
    public static int get16BitsAligned(long data, int offset) {
        // Normalize offset
        offset = offset % 4;

        // Align the mask
        long mask = SpecsBits.MASK_16_BITS << 16 * offset;

        // Get the bits
        long result = data & mask;

        // Put bits in position
        return (int) (result >>> (16 * offset));
    }

    /**
     * Paul Hsieh's Hash Function, for long numbers.
     * 
     * @param data data to hash
     * @param hash previous value of the hash. If this it is the start of the
     *             method, a recomended value to use is the
     *             length of the data. In this case because it is a long use the
     *             number 8 (8 bytes).
     * @return a hash value
     */
    public static int superFastHash(long data, int hash) {
        int tmp;

        // Main Loop
        for (int i = 0; i < 4; i += 2) {
            // Get lower 16 bits
            hash += SpecsBits.get16BitsAligned(data, i);
            // Calculate some random value with second-lower 16 bits
            tmp = (SpecsBits.get16BitsAligned(data, i + 1) << 11) ^ hash;
            hash = (hash << 16) ^ tmp;
            // At this point, it would advance the data, but since it is restricted
            // to longs (64-bit values), it is unnecessary).
            hash += hash >> 11;
        }

        // Handle end cases //
        // There are no end cases, main loop is done in chuncks of 32 bits.

        // Force "avalanching" of final 127 bits //
        hash ^= hash << 3;
        hash += hash >> 5;
        hash ^= hash << 4;
        hash += hash >> 17;
        hash ^= hash << 25;
        hash += hash >> 6;

        return hash;
    }

    /**
     * Paul Hsieh's Hash Function, for int numbers.
     * 
     * @param data data to hash
     * @param hash previous value of the hash. If this it is the start of the
     *             method, a recomended value to use is the
     *             length of the data. In this case because it is an integer use the
     *             number 4 (4 bytes).
     * @return a hash value
     */
    public static int superFastHash(int data, int hash) {
        int tmp;

        // Main Loop
        int i = 0;

        // Get lower 16 bits
        hash += SpecsBits.get16BitsAligned(data, i);
        // Calculate some random value with second-lower 16 bits
        tmp = (SpecsBits.get16BitsAligned(data, i + 1) << 11) ^ hash;
        hash = (hash << 16) ^ tmp;

        // At this point, it would advance the data, but since it is restricted
        // to longs (64-bit values), it is unnecessary).
        hash += hash >> 11;

        // Handle end cases //
        // There are no end cases, main loop is done in chuncks of 32 bits.

        // Force "avalanching" of final 127 bits //
        hash ^= hash << 3;
        hash += hash >> 5;
        hash ^= hash << 4;
        hash += hash >> 17;
        hash ^= hash << 25;
        hash += hash >> 6;

        return hash;
    }

    /**
     * Sets a specific bit of an int.
     * 
     * @param bit    the bit to set. The least significant bit is bit 0
     * @param target the integer where the bit will be set
     * @return the updated value of the target
     */
    public static int setBit(int bit, int target) {
        // Create mask
        int mask = 1 << bit;
        // Set bit
        return target | mask;
    }

    /**
     * Clears a specific bit of an int.
     * 
     * @param bit    the bit to clear. The least significant bit is bit 0
     * @param target the integer where the bit will be cleared
     * @return the updated value of the target
     */
    public static int clearBit(int bit, int target) {
        // Create mask
        int mask = 1 << bit;
        // Clear bit
        return target & ~mask;
    }

    /**
     * Returns true if a is greater than b.
     * 
     * @param a
     * @param b
     * @return
     */
    public static boolean unsignedComp(int a, int b) {
        // Unsigned Comparison
        long longA = a & SpecsBits.MASK_32_BITS;
        long longB = b & SpecsBits.MASK_32_BITS;
        return longA > longB;
    }

    /**
     * Fuses the lower 16 bits of two ints.
     * 
     * TODO: Verify correcteness.
     * <p>
     * Ex.: upper16 = 1001 lower16 = 101 result = 00000000000010010000000000000101
     * 
     * @param upper16
     * @param lower16
     * @return
     */
    public static int fuseImm(int upper16, int lower16) {
        // Mask the 16 bits of each one
        upper16 = upper16 & Integer.parseInt("0000FFFF", 16);
        lower16 = lower16 & Integer.parseInt("0000FFFF", 16);
        // Shift Upper16
        upper16 = upper16 << 16;
        // Merge
        int result = upper16 | lower16;
        return result;
    }

    /**
     * Converts a signed byte to an unsigned integer representation.
     * 
     * @param aByte the byte to convert
     * @return the unsigned integer representation of the byte
     */
    public static int getUnsignedByte(byte aByte) {
        int byteAsInt = aByte;
        // When casting a byte to an int, if the byte is signed the additional
        // bits will be set to 1.
        // We use a mask to reset the additional bits back to zero.
        int unsignedByteAsInt = byteAsInt & SpecsBits.UNSIGNED_BYTE_MASK;
        return unsignedByteAsInt;
    }

    /**
     * Calculates the base-2 logarithm of the given integer, rounding up.
     * 
     * @param i the integer to calculate the logarithm for
     * @return the base-2 logarithm of the integer, rounded up
     */
    public static int log2(int i) {
        double log2 = Math.log(i) / Math.log(2);
        int log2Int = (int) Math.ceil(log2);

        return log2Int;
    }

    /**
     * Unsigned extend. Extends the short to an int, adding 0's to the 16 msb.
     * 
     * <p>
     * Ex.: value: 1011011101011010 return: 00000000000000001011011101011010
     * 
     * @param value
     * @return
     */
    public static Integer extend(short value) {
        int returnValue = value;
        returnValue = returnValue & Integer.parseInt("0000FFFF", 16);
        return returnValue;
    }

    /**
     * Sign-Extends the 'extendSize' LSBs.
     * 
     * <p>
     * Ex.: value: 1011011111011010; extendSize: 8 return: 1111111111011010
     * 
     * @param value
     * @return
     */
    public static int signExtend(int value, int extendSize) {
        // Get signal bit
        int signalBit = getBit(extendSize - 1, value);

        // Append first 32-extendSize bits with the signal bit
        StringBuilder binaryString = new StringBuilder();
        int intBits = 32;
        for (int i = 0; i < intBits - extendSize; i++) {
            binaryString.append(signalBit);
        }

        for (int i = extendSize - 1; i >= 0; i--) {
            binaryString.append(getBit(i, value));
        }

        return parseSignedBinary(binaryString.toString());
    }

    /**
     * Converts a 0-based, LSB-order bit to the corresponding index in a String
     * representation of the number.
     * 
     * @param signalBit
     * @param stringSize
     * @return
     */
    public static int fromLsbToStringIndex(int signalBit, int stringSize) {
        return stringSize - signalBit - 1;
    }

    /**
     * Sign-extends the given String representing a binary value (only 0s and 1s).
     * 
     * @param binaryValue
     * @param signalBit   the 0-based index, counting from the LSB, that represents
     *                    the signal
     * @return a String with the same size but where all values higher than
     *         signalBit are the same as the value at the
     *         signalBit value.
     */
    public static String signExtend(String binaryValue, int signalBit) {
        if (binaryValue == null || binaryValue.isEmpty()) {
            throw new IllegalArgumentException("Binary value cannot be null or empty.");
        }

        if (signalBit < 0) {
            throw new IllegalArgumentException("Signal bit must be a non-negative integer.");
        }

        // If bit is not represented in the binary value, value does not need sign
        // extension
        if (signalBit >= binaryValue.length()) {
            return binaryValue;
        }

        // Convert LSB signalBit to String index
        int lsbSignalIndex = fromLsbToStringIndex(signalBit, binaryValue.length());

        // Get signal bit
        var signalValue = binaryValue.substring(lsbSignalIndex, lsbSignalIndex + 1);

        // Replicate signal value up to signal bit
        return SpecsStrings.buildLine(signalValue, lsbSignalIndex + 1)
                + binaryValue.substring(lsbSignalIndex + 1, binaryValue.length());
    }

    /**
     * Parses a signed binary string into an integer.
     * 
     * @param binaryString the binary string to parse
     * @return the integer representation of the binary string
     */
    public static int parseSignedBinary(String binaryString) {
        if (binaryString.length() > 32) {
            SpecsLogs.warn("Given string has more than 32 bits. Truncating MSB.");
            binaryString = binaryString.substring(0, 32);
        }

        if (binaryString.length() < 32) {
            return Integer.parseInt(binaryString.toString(), 2);
        }

        // BinaryString has size 32. Check MSB if 0
        if (binaryString.charAt(0) == '0') {
            return Integer.parseInt(binaryString.toString(), 2);
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < binaryString.length(); i++) {
            if (binaryString.charAt(i) == '0') {
                builder.append('1');
            } else if (binaryString.charAt(i) == '1') {
                builder.append('0');
            } else {
                SpecsLogs.warn("Binary string has char '" + binaryString.charAt(i) + "'.");
            }
        }
        String invertedString = builder.toString();
        int flippedValue = Integer.parseInt(invertedString, 2);
        return (flippedValue + 1) * -1;
    }

    /**
     * Puts to zero all bits except numBits least significant bits.
     * 
     * Ex.: value: 1011; numBits: 3; return: 0011
     * 
     * @param value
     * @param numBits
     * @return
     */
    public static int mask(int value, int numBits) {
        StringBuilder binaryString = new StringBuilder();
        int intBits = 32;
        for (int i = 0; i < intBits - numBits; i++) {
            binaryString.append(0);
        }
        for (int i = 0; i < numBits; i++) {
            binaryString.append(1);
        }

        return value & Integer.parseInt(binaryString.toString(), 2);
    }

    /**
     * Converts a boolean value to an integer representation.
     * 
     * @param boolResult the boolean value to convert
     * @return 1 if true, or 0 if false
     */
    public static int boolToInt(boolean boolResult) {
        if (boolResult) {
            return 1;
        }

        return 0;
    }

    /**
     * Transforms the given integer value into an unsigned long. If the value is
     * negative, returns the positive long value as if the given value is decoded
     * from an equivalent 32-bit hexadecimal string.
     * 
     * @param value
     * @return
     */
    public static Long getUnsignedLong(int value) {
        String hexValue = Integer.toHexString(value);
        return Long.decode("0x" + hexValue);
    }

    /**
     * Checks if a NaN is quiet. Does not test if number is a NaN.
     * 
     * <p>
     * IEEE 754 NaNs are represented with the exponential field filled with ones and
     * some non-zero number in the significand. A bit-wise example of a IEEE
     * floating-point standard single precision NaN:
     * x111 1111 1axx xxxx xxxx xxxx xxxx xxxx
     * where x means don't care. If a = 1, it is a quiet NaN, otherwise it is a
     * signalling NaN.
     * 
     * @param aNanN
     * @return true if the given NaN is quiet.
     */
    public static boolean isQuietNaN(int aNaN) {
        return getBit(22, aNaN) == 1;
    }

    /**
     * Checks if a float is denormalized.
     * 
     * <p>
     * IEEE 754 denormals are identified by having the exponents bits set to zero
     * (30 to 23).
     * 
     * @param aFloat
     * @return true if the given float is denormal
     */
    public static boolean isDenormal(int aFloat) {
        // Test if zero
        if ((SpecsBits.NOT_SIGN_MASK & aFloat) == 0) {
            return false;
        }
        return (aFloat & SpecsBits.DENORMAL_MASK) == 0;
    }

    /**
     * Checks if a float is zero.
     * 
     * <p>
     * IEEE 754 zeros are identified by having the all bits except the sign set to
     * zero (30 to 0).
     * 
     * @param aFloat
     * @return true if the given float represents zero
     */
    public static boolean isZero(int aFloat) {
        return (aFloat & SpecsBits.ZERO_MASK) == 0;
    }

    /**
     * 
     * @param floatBits
     * @return a float zero with the same sign as the given floating point
     */
    public static int getSignedZero(int floatBits) {
        int signBit = floatBits & SpecsBits.FLOAT_SIGN_MASK; // Sign of the float number
        return signBit | 0; // Signed zero, +0 or -0
    }

    /**
     * 
     * @param floatBits
     * @return a float zero with the same sign as the given floating point
     */
    public static int getSignedInfinity(int floatBits) {
        int signBit = floatBits & SpecsBits.FLOAT_SIGN_MASK; // Sign of the result
        return signBit | SpecsBits.FLOAT_INFINITY; // Signed infinity, +Inf or -Inf
    }

    /**
     * 
     * 
     * @param value
     * @param byteOffset can have value 0 or 1, where 0 is the least significant
     *                   short
     * @return
     */
    public static int getShort(int value, int byteOffset) {
        switch (byteOffset) {
            case 0:
                return value & 0x0000FFFF;
            case 2:
                return (value & 0xFFFF0000) >>> 16;
            default:
                throw new RuntimeException("Invalid case: " + byteOffset);
        }
    }

    /**
     * Extracts a specific byte from an integer.
     * 
     * @param value      the integer to extract the byte from
     * @param byteOffset the offset of the byte to extract (0-based)
     * @return the extracted byte as an integer
     */
    public static int getByte(int value, int byteOffset) {
        switch (byteOffset) {
            case 0:
                return value & 0x000000FF;
            case 1:
                return (value & 0x0000FF00) >>> 8;
            case 2:
                return (value & 0x00FF0000) >>> 16;
            case 3:
                return (value & 0xFF000000) >>> 24;
            default:
                throw new RuntimeException("Invalid case: " + byteOffset);
        }
    }

    /**
     * Reads an unsigned 16-bit number from a byte array. This method reads two
     * bytes from the array, starting at the
     * given offset.
     * 
     * @param byteArray
     * @param offset
     * @param isLittleEndian
     * @return
     */
    public static int readUnsignedShort(byte[] byteArray, int offset,
            boolean isLittleEndian) {

        int numBytes = 2;
        int result = 0;
        for (int i = 0; i < numBytes; i++) {
            result |= SpecsBits.positionByte(byteArray[offset + i], i, numBytes, isLittleEndian);
        }
        return result;
    }

    /**
     * Reads an unsigned 32-bit number from a byte array. This method reads four
     * bytes from the array, starting at the given offset.
     * 
     * @param byteArray      the byte array to read from
     * @param offset         the starting offset in the array
     * @param isLittleEndian whether the bytes are in little-endian order
     * @return the unsigned 32-bit number as a long
     */
    public static long readUnsignedInteger(byte[] byteArray, int offset,
            boolean isLittleEndian) {

        int numBytes = 4;
        long result = 0;
        for (int i = 0; i < numBytes; i++) {
            result |= SpecsBits.positionByte(byteArray[offset + i], i, numBytes, isLittleEndian);
        }
        return result;

    }

    /**
     * Positions a byte inside a bigger unit according to its endianess and the
     * position of the byte. A long is used to support unsigned integers.
     *
     * TODO: Test/check this method so see if it can support longs, not just
     * integers
     *
     * @param aByte
     * @param bytePosition
     * @param totalBytes     the bytes of the unit (short = 2, int = 4).
     * @param isLittleEndian
     * @return
     */
    public static long positionByte(byte aByte, int bytePosition, int totalBytes, boolean isLittleEndian) {
        int multiplier;
        if (isLittleEndian) {
            multiplier = bytePosition;
        } else {
            multiplier = totalBytes - bytePosition - 1;
        }

        int shift = SpecsBits.BITS_IN_A_BYTE * multiplier;
        int shiftedByte = getUnsignedByte(aByte) << shift;

        return shiftedByte;
    }

    /**
     * Reverses the half-words in the given integer.
     * 
     * @param data the integer to reverse the half-words of
     * @return the integer with reversed half-words
     */
    public static int reverseHalfWords(int data) {

        int higherHalf = data << 16;
        int lowerHalf = data >>> 16;

        return higherHalf | lowerHalf;
    }

    /**
     * Reverses the bytes in the given integer.
     * 
     * @param data the integer to reverse the bytes of
     * @return the integer with reversed bytes
     */
    public static int reverse(int data) {
        // Reverse bytes of data
        byte[] bytes = ByteBuffer.allocate(4).putInt(data).array();

        byte[] reversedBytes = SpecsBits.reverse(bytes);

        // Create reversed int
        ByteBuffer wrapped = ByteBuffer.wrap(reversedBytes); // big-endian by default

        return wrapped.getInt();
    }

    /**
     * Reverses the bytes in the given short.
     * 
     * @param data the short to reverse the bytes of
     * @return the short with reversed bytes
     */
    public static short reverse(short data) {
        // Reverse bytes of data
        byte[] bytes = ByteBuffer.allocate(2).putShort(data).array();

        byte[] reversedBytes = SpecsBits.reverse(bytes);

        // Create reversed int
        ByteBuffer wrapped = ByteBuffer.wrap(reversedBytes); // big-endian by default

        return wrapped.getShort();
    }

    /**
     * Reverses the order of bytes in the given array.
     * 
     * @param bytes the array of bytes to reverse
     * @return the array with reversed byte order
     */
    public static byte[] reverse(byte[] bytes) {
        byte[] reversedBytes = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            reversedBytes[bytes.length - 1 - i] = bytes[i];
        }

        return reversedBytes;
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
