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
    }
}
