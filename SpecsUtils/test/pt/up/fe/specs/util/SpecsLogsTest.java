/**
 * Copyright 2018 SPeCS.
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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test suite for SpecsLogs utility class.
 * 
 * This test class covers logging functionality including:
 * - Warning level logging
 * - Exception logging
 * - Basic logging operations
 */
@DisplayName("SpecsLogs Tests")
public class SpecsLogsTest {

    @BeforeAll
    static void init() {
        SpecsSystem.programStandardInit();
    }

    @Test
    @DisplayName("Should log warning message without throwing exception")
    void testWarnLogging_BasicMessage_ShouldNotThrowException() {
        // This test verifies that logging doesn't throw exceptions
        assertThatCode(() -> SpecsLogs.warn("Warning level"))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should log warning with exception without throwing exception")
    void testWarnLogging_WithException_ShouldNotThrowException() {
        assertThatCode(() -> {
            try {
                throwException();
            } catch (Exception e) {
                SpecsLogs.warn("Catching an exception", e);
            }
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should handle null message gracefully")
    void testWarnLogging_NullMessage_ShouldNotThrowException() {
        assertThatCode(() -> SpecsLogs.warn(null))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Should handle empty message gracefully")
    void testWarnLogging_EmptyMessage_ShouldNotThrowException() {
        assertThatCode(() -> SpecsLogs.warn(""))
                .doesNotThrowAnyException();
    }

    private static void throwException() {
        throw new RuntimeException("Throwing exception");
    }

}
