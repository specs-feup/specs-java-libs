/**
 * Copyright 2020 SPeCS.
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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Test suite for SpecsBits utility class.
 * 
 * This test class covers bit manipulation functionality including:
 * - Sign extension operations
 * - Binary string manipulation
 * - Edge cases for bit operations
 */
@DisplayName("SpecsBits Tests")
public class SpecsBitsTest {

    @Nested
    @DisplayName("Sign Extension Operations")
    class SignExtensionOperations {

        @Test
        @DisplayName("signExtend should handle basic sign extension correctly")
        void testSignExtend_BasicCases_ReturnsCorrectExtension() {
            assertThat(SpecsBits.signExtend("01010101", 2)).isEqualTo("11111101");
            assertThat(SpecsBits.signExtend("01010101", 3)).isEqualTo("00000101");
            assertThat(SpecsBits.signExtend("10101010101010101", 16)).isEqualTo("10101010101010101");
            assertThat(SpecsBits.signExtend("00101010101010101", 16)).isEqualTo("00101010101010101");
        }

        @Test
        @DisplayName("signExtend should handle zero bit positions correctly")
        void testSignExtend_ZeroBitPosition_ReturnsCorrectExtension() {
            assertThat(SpecsBits.signExtend("00101010101010101", 0)).isEqualTo("11111111111111111");
            assertThat(SpecsBits.signExtend("00101010101010100", 0)).isEqualTo("00000000000000000");
            assertThat(SpecsBits.signExtend("01", 0)).isEqualTo("11");
        }

        @Test
        @DisplayName("signExtend should handle edge cases correctly")
        void testSignExtend_EdgeCases_ReturnsCorrectExtension() {
            assertThat(SpecsBits.signExtend("100", 6)).isEqualTo("100");
            assertThat(SpecsBits.signExtend("100", 2)).isEqualTo("100");
            assertThat(SpecsBits.signExtend("0", 1)).isEqualTo("0");
            assertThat(SpecsBits.signExtend("0", 0)).isEqualTo("0");
        }

        @ParameterizedTest
        @CsvSource({
                "01010101, 2, 11111101",
                "01010101, 3, 00000101",
                "100, 6, 100",
                "100, 2, 100",
                "0, 1, 0",
                "0, 0, 0",
                "01, 0, 11"
        })
        @DisplayName("signExtend parameterized test cases")
        void testSignExtend_ParameterizedCases(String input, int position, String expected) {
            assertThat(SpecsBits.signExtend(input, position)).isEqualTo(expected);
        }

        @Test
        @DisplayName("signExtend should throw IllegalArgumentException for null input")
        void testSignExtend_NullInput_ShouldThrowException() {
            assertThatThrownBy(() -> SpecsBits.signExtend(null, 2))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("signExtend should throw IllegalArgumentException for empty string")
        void testSignExtend_EmptyString_ShouldThrowException() {
            assertThatThrownBy(() -> SpecsBits.signExtend("", 2))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("signExtend should throw IllegalArgumentException for negative position")
        void testSignExtend_NegativePosition_ShouldThrowException() {
            assertThatThrownBy(() -> SpecsBits.signExtend("101", -1))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Bit Manipulation Operations")
    class BitManipulationOperations {

        @ParameterizedTest
        @CsvSource({
            "0, 0, 0",      // getBit(position=0, target=0) -> 0
            "0, 1, 1",      // getBit(position=0, target=1) -> 1  
            "1, 2, 1",      // getBit(position=1, target=2) -> 1
            "1, 3, 1",      // getBit(position=1, target=3) -> 1
            "2, 4, 1",      // getBit(position=2, target=4) -> 1
            "3, 15, 1",     // getBit(position=3, target=15) -> 1
            "31, -2147483648, 1"  // getBit(position=31, target=Integer.MIN_VALUE) -> 1
        })
        @DisplayName("getBit should return correct bit values")
        void testGetBit_VariousInputs_ReturnsCorrectBit(int position, int target, int expected) {
            assertThat(SpecsBits.getBit(position, target)).isEqualTo(expected);
        }

        @Test
        @DisplayName("getBit should handle edge cases")
        void testGetBit_EdgeCases_ReturnsCorrectValues() {
            // Test with Integer.MAX_VALUE
            assertThat(SpecsBits.getBit(30, Integer.MAX_VALUE)).isEqualTo(1);
            assertThat(SpecsBits.getBit(31, Integer.MAX_VALUE)).isEqualTo(0);
            
            // Test with -1 (all bits set)
            assertThat(SpecsBits.getBit(0, -1)).isEqualTo(1);
            assertThat(SpecsBits.getBit(15, -1)).isEqualTo(1);
            assertThat(SpecsBits.getBit(31, -1)).isEqualTo(1);
        }

        @ParameterizedTest
        @CsvSource({
            "0, 0, 1",      // setBit(bit=0, target=0) -> 1
            "1, 0, 2",      // setBit(bit=1, target=0) -> 2
            "2, 0, 4",      // setBit(bit=2, target=0) -> 4
            "0, 1, 1",      // setBit(bit=0, target=1) -> 1 (already set)
            "1, 1, 3",      // setBit(bit=1, target=1) -> 3
            "31, 0, -2147483648"  // setBit(bit=31, target=0) -> Integer.MIN_VALUE
        })
        @DisplayName("setBit should set bits correctly")
        void testSetBit_VariousInputs_SetsBitsCorrectly(int bit, int target, int expected) {
            assertThat(SpecsBits.setBit(bit, target)).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({
            "0, 1, 0",      // clearBit(bit=0, target=1) -> 0
            "1, 2, 0",      // clearBit(bit=1, target=2) -> 0
            "1, 3, 1",      // clearBit(bit=1, target=3) -> 1
            "0, 0, 0",      // clearBit(bit=0, target=0) -> 0 (already clear)
            "31, -1, 2147483647"  // clearBit(bit=31, target=-1) -> Integer.MAX_VALUE
        })
        @DisplayName("clearBit should clear bits correctly")
        void testClearBit_VariousInputs_ClearsBitsCorrectly(int bit, int target, int expected) {
            assertThat(SpecsBits.clearBit(bit, target)).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("String Formatting Operations")
    class StringFormattingOperations {

        @ParameterizedTest
        @CsvSource({
            "0, 2, '0x00'",
            "1, 4, '0x0001'", 
            "15, 2, '0x0f'",
            "255, 4, '0x00ff'",
            "4095, 3, '0xfff'"
        })
        @DisplayName("padHexString should pad hex numbers correctly")
        void testPadHexString_VariousInputs_PadsCorrectly(long hexNumber, int size, String expected) {
            assertThat(SpecsBits.padHexString(hexNumber, size)).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({
            "'F', 4, '0x000F'",
            "'FF', 2, '0xFF'",
            "'ABC', 6, '0x000ABC'",
            "'1234', 3, '0x1234'"  // Should not truncate
        })
        @DisplayName("padHexString should pad hex strings correctly")
        void testPadHexString_StringInputs_PadsCorrectly(String hexNumber, int size, String expected) {
            assertThat(SpecsBits.padHexString(hexNumber, size)).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({
            "'1', 4, '0001'",
            "'101', 6, '000101'",
            "'1111', 2, '1111'",  // Should not truncate
            "'0', 3, '000'"
        })
        @DisplayName("padBinaryString should pad binary strings correctly")
        void testPadBinaryString_VariousInputs_PadsCorrectly(String binaryNumber, int size, String expected) {
            assertThat(SpecsBits.padBinaryString(binaryNumber, size)).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Hash and Advanced Operations")
    class HashAndAdvancedOperations {

        @Test
        @DisplayName("superFastHash should produce consistent hash values for long inputs")
        void testSuperFastHash_LongInputs_ProducesConsistentValues() {
            int hash1 = SpecsBits.superFastHash(0x123456789ABCDEFL, 0);
            int hash2 = SpecsBits.superFastHash(0x123456789ABCDEFL, 0);
            assertThat(hash1).isEqualTo(hash2);
            
            // Different data should produce different hashes
            int hash3 = SpecsBits.superFastHash(0x987654321FEDCBAL, 0);
            assertThat(hash1).isNotEqualTo(hash3);
        }

        @Test
        @DisplayName("superFastHash should produce consistent hash values for int inputs")
        void testSuperFastHash_IntInputs_ProducesConsistentValues() {
            int hash1 = SpecsBits.superFastHash(0x12345678, 0);
            int hash2 = SpecsBits.superFastHash(0x12345678, 0);
            assertThat(hash1).isEqualTo(hash2);
            
            // Different data should produce different hashes
            int hash3 = SpecsBits.superFastHash(0x87654321, 0);
            assertThat(hash1).isNotEqualTo(hash3);
        }

        @Test
        @DisplayName("get16BitsAligned should extract 16-bit values correctly")
        void testGet16BitsAligned_VariousInputs_ExtractsCorrectly() {
            long data = 0x123456789ABCDEFL;
            
            // Test different offsets - checking actual behavior
            int result0 = SpecsBits.get16BitsAligned(data, 0);
            int result1 = SpecsBits.get16BitsAligned(data, 1);
            int result2 = SpecsBits.get16BitsAligned(data, 2);
            int result3 = SpecsBits.get16BitsAligned(data, 3);
            
            // Verify results are within 16-bit range
            assertThat(result0).isBetween(0, 65535);
            assertThat(result1).isBetween(0, 65535);
            assertThat(result2).isBetween(0, 65535);
            assertThat(result3).isBetween(0, 65535);
            
            // Test with simple data
            assertThat(SpecsBits.get16BitsAligned(0x12345678L, 0)).isEqualTo(0x5678);
            assertThat(SpecsBits.get16BitsAligned(0x12345678L, 1)).isEqualTo(0x1234);
        }

        @ParameterizedTest
        @CsvSource({
            "1, 0",
            "2, 1", 
            "4, 2",
            "8, 3",
            "16, 4",
            "1024, 10"
        })
        @DisplayName("log2 should calculate logarithm base 2 correctly")
        void testLog2_PowersOfTwo_ReturnsCorrectValues(int input, int expected) {
            assertThat(SpecsBits.log2(input)).isEqualTo(expected);
        }

        @Test
        @DisplayName("log2 should handle edge cases")
        void testLog2_EdgeCases_HandlesCorrectly() {
            // Test with non-power-of-two values (method uses ceiling, not floor)
            assertThat(SpecsBits.log2(3)).isEqualTo(2);  // ceil(log2(3)) = 2
            assertThat(SpecsBits.log2(5)).isEqualTo(3);  // ceil(log2(5)) = 3
            assertThat(SpecsBits.log2(15)).isEqualTo(4); // ceil(log2(15)) = 4
        }
    }

    @Nested
    @DisplayName("Utility and Conversion Operations")
    class UtilityAndConversionOperations {

        @Test
        @DisplayName("unsignedComp should compare unsigned integers correctly")
        void testUnsignedComp_VariousInputs_ComparesCorrectly() {
            // Test positive numbers
            assertThat(SpecsBits.unsignedComp(5, 10)).isFalse(); // 5 < 10
            assertThat(SpecsBits.unsignedComp(10, 5)).isTrue();  // 10 > 5
            assertThat(SpecsBits.unsignedComp(5, 5)).isFalse();  // 5 == 5
            
            // Test with negative numbers (treated as unsigned)
            assertThat(SpecsBits.unsignedComp(-1, 1)).isTrue();  // -1 as unsigned is very large
            assertThat(SpecsBits.unsignedComp(1, -1)).isFalse(); // 1 < (-1 as unsigned)
        }

        @ParameterizedTest
        @CsvSource({
            "4660, 22136, 305419896",      // 0x1234, 0x5678 -> decimal
            "0, 65535, 65535",             // 0x0000, 0xFFFF -> decimal
            "65535, 0, -65536",            // 0xFFFF, 0x0000 -> decimal  
            "43690, 21845, -1431677611"   // 0xAAAA, 0x5555 -> actual result
        })
        @DisplayName("fuseImm should fuse 16-bit values correctly")
        void testFuseImm_VariousInputs_FusesCorrectly(int upper16, int lower16, int expected) {
            assertThat(SpecsBits.fuseImm(upper16, lower16)).isEqualTo(expected);
        }

        @ParameterizedTest
        @CsvSource({
            "-128, 128",
            "-1, 255",
            "0, 0",
            "127, 127"
        })
        @DisplayName("getUnsignedByte should convert bytes to unsigned correctly")
        void testGetUnsignedByte_VariousInputs_ConvertsCorrectly(byte input, int expected) {
            assertThat(SpecsBits.getUnsignedByte(input)).isEqualTo(expected);
        }

        @Test
        @DisplayName("extend should handle short extension correctly")
        void testExtend_ShortValues_ExtendsCorrectly() {
            assertThat(SpecsBits.extend((short) 0x1234)).isEqualTo(0x1234);
            assertThat(SpecsBits.extend((short) -1)).isEqualTo(0xFFFF);
            assertThat(SpecsBits.extend((short) 0)).isEqualTo(0);
        }

        @ParameterizedTest
        @CsvSource({
            "127, 8, 127",        // Positive 8-bit value
            "255, 8, -1",         // Negative 8-bit value (sign extended)
            "32767, 16, 32767",   // Positive 16-bit value
            "65535, 16, -1"       // Negative 16-bit value (sign extended)
        })
        @DisplayName("signExtend should handle integer sign extension correctly")
        void testSignExtend_IntegerInputs_ExtendsCorrectly(int value, int extendSize, int expected) {
            assertThat(SpecsBits.signExtend(value, extendSize)).isEqualTo(expected);
        }

        @Test
        @DisplayName("parseSignedBinary should parse binary strings correctly")
        void testParseSignedBinary_VariousInputs_ParsesCorrectly() {
            assertThat(SpecsBits.parseSignedBinary("0")).isEqualTo(0);
            assertThat(SpecsBits.parseSignedBinary("1")).isEqualTo(1);
            assertThat(SpecsBits.parseSignedBinary("101")).isEqualTo(5);
            assertThat(SpecsBits.parseSignedBinary("1111")).isEqualTo(15);
        }

        @Test
        @DisplayName("fromLsbToStringIndex should convert LSB positions correctly")
        void testFromLsbToStringIndex_VariousInputs_ConvertsCorrectly() {
            // Test basic conversions
            assertThat(SpecsBits.fromLsbToStringIndex(0, 8)).isEqualTo(7);
            assertThat(SpecsBits.fromLsbToStringIndex(7, 8)).isEqualTo(0);
            assertThat(SpecsBits.fromLsbToStringIndex(3, 8)).isEqualTo(4);
        }
    }

    @Nested
    @DisplayName("Constants and Masks")
    class ConstantsAndMasks {

        @Test
        @DisplayName("getMask32Bits should return correct 32-bit mask")
        void testGetMask32Bits_ReturnsCorrectMask() {
            assertThat(SpecsBits.getMask32Bits()).isEqualTo(0xFFFFFFFFL);
        }

        @Test
        @DisplayName("getMaskBit33 should return correct bit 33 mask")
        void testGetMaskBit33_ReturnsCorrectMask() {
            assertThat(SpecsBits.getMaskBit33()).isEqualTo(0x100000000L);
        }
    }

}
