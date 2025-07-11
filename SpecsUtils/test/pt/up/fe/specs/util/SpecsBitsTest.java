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
 * specific language governing permissions and limitations under the License. under the License.
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

}
