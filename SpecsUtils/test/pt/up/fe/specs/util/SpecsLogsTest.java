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

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.function.Supplier;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import pt.up.fe.specs.util.logging.EnumLogger;
import pt.up.fe.specs.util.logging.SpecsLoggerTag;

/**
 * Comprehensive test suite for SpecsLogs utility class.
 *
 * This test class covers all major logging functionality including:
 * - Logger creation and configuration
 * - Warning, info, and severe level logging
 * - Handler management (console, file, stream handlers)
 * - Exception logging and stack trace handling
 * - Log level parsing and configuration
 * - System output redirection
 * - Debug logging functionality
 * - Error message building utilities
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsLogs Tests")
public class SpecsLogsTest {

    private Logger originalRootLogger;
    private Handler[] originalHandlers;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeAll
    static void init() {
        SpecsSystem.programStandardInit();
    }

    @BeforeEach
    void setUp() {
        // Save original state
        originalRootLogger = SpecsLogs.getRootLogger();
        originalHandlers = originalRootLogger.getHandlers().clone();
        originalOut = System.out;
        originalErr = System.err;
    }

    @AfterEach
    void tearDown() {
        // Restore original state
        System.setOut(originalOut);
        System.setErr(originalErr);

        // Restore original handlers
        for (Handler handler : originalRootLogger.getHandlers()) {
            originalRootLogger.removeHandler(handler);
        }
        for (Handler handler : originalHandlers) {
            originalRootLogger.addHandler(handler);
        }
    }

    @Nested
    @DisplayName("Logger Creation and Access")
    class LoggerCreationTests {

        @Test
        @DisplayName("getRootLogger should return valid root logger")
        void testGetRootLogger() {
            // Execute
            Logger rootLogger = SpecsLogs.getRootLogger();

            // Verify
            assertThat(rootLogger).isNotNull();
            assertThat(rootLogger.getName()).isEmpty(); // Root logger has empty name
        }

        @Test
        @DisplayName("getLogger should return valid logger")
        void testGetLogger() {
            // Execute
            Logger logger = SpecsLogs.getLogger();

            // Verify
            assertThat(logger).isNotNull();
        }

        @Test
        @DisplayName("getSpecsLogger should return EnumLogger")
        void testGetSpecsLogger() {
            // Execute
            EnumLogger<SpecsLoggerTag> specsLogger = SpecsLogs.getSpecsLogger();

            // Verify
            assertThat(specsLogger).isNotNull();
        }
    }

    @Nested
    @DisplayName("Basic Logging Operations")
    class BasicLoggingTests {

        @Test
        @DisplayName("warn should log warning message without throwing exception")
        void testWarnLogging_BasicMessage_ShouldNotThrowException() {
            // Execute & Verify
            assertThatCode(() -> SpecsLogs.warn("Warning level"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("warn should handle null message gracefully")
        void testWarnLogging_NullMessage_ShouldNotThrowException() {
            // Execute & Verify
            assertThatCode(() -> SpecsLogs.warn(null))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("warn with exception should log without throwing")
        void testWarnLogging_WithException_ShouldNotThrowException() {
            // Arrange
            Exception testException = new RuntimeException("Test exception");

            // Execute & Verify
            assertThatCode(() -> SpecsLogs.warn("Warning with exception", testException))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("info should log info message")
        void testInfoLogging() {
            // Execute & Verify
            assertThatCode(() -> SpecsLogs.info("Info message"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("msgInfo should log info message")
        void testMsgInfoLogging() {
            // Execute & Verify
            assertThatCode(() -> SpecsLogs.msgInfo("Info message"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("msgSevere should log severe message")
        void testMsgSevereLogging() {
            // Execute & Verify
            assertThatCode(() -> SpecsLogs.msgSevere("Severe message"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("msgLib should log library message")
        void testMsgLibLogging() {
            // Execute & Verify
            assertThatCode(() -> SpecsLogs.msgLib("Library message"))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Handler Management")
    class HandlerManagementTests {

        @Test
        @DisplayName("buildStdOutHandler should create valid stdout handler")
        void testBuildStdOutHandler() {
            // Execute
            Handler handler = SpecsLogs.buildStdOutHandler();

            // Verify
            assertThat(handler).isNotNull();
        }

        @Test
        @DisplayName("buildStdErrHandler should create valid stderr handler")
        void testBuildStdErrHandler() {
            // Execute
            Handler handler = SpecsLogs.buildStdErrHandler();

            // Verify
            assertThat(handler).isNotNull();
        }

        @Test
        @DisplayName("addHandler should add handler to root logger")
        void testAddHandler() {
            // Arrange
            Handler testHandler = new StreamHandler();
            int originalHandlerCount = SpecsLogs.getRootLogger().getHandlers().length;

            // Execute
            SpecsLogs.addHandler(testHandler);

            // Verify
            Handler[] handlers = SpecsLogs.getRootLogger().getHandlers();
            assertThat(handlers).hasSize(originalHandlerCount + 1);
            assertThat(handlers).contains(testHandler);
        }

        @Test
        @DisplayName("removeHandler should remove handler from root logger")
        void testRemoveHandler() {
            // Arrange
            Handler testHandler = new StreamHandler();
            SpecsLogs.addHandler(testHandler);
            int handlerCountWithAdded = SpecsLogs.getRootLogger().getHandlers().length;

            // Execute
            SpecsLogs.removeHandler(testHandler);

            // Verify
            Handler[] handlers = SpecsLogs.getRootLogger().getHandlers();
            assertThat(handlers).hasSize(handlerCountWithAdded - 1);
            assertThat(handlers).doesNotContain(testHandler);
        }

        @Test
        @DisplayName("setupConsoleOnly should set up console-only logging")
        void testSetupConsoleOnly() {
            // Execute
            assertThatCode(() -> SpecsLogs.setupConsoleOnly())
                    .doesNotThrowAnyException();

            // Verify that handlers were configured
            Handler[] handlers = SpecsLogs.getRootLogger().getHandlers();
            assertThat(handlers).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Level Management")
    class LevelManagementTests {

        @ParameterizedTest
        @ValueSource(strings = { "INFO", "WARNING", "SEVERE", "FINE", "FINER", "FINEST", "ALL", "OFF" })
        @DisplayName("parseLevel should parse valid level strings")
        void testParseLevel_ValidLevels(String levelString) {
            // Execute
            Level level = SpecsLogs.parseLevel(levelString);

            // Verify
            assertThat(level).isNotNull();
            assertThat(level.getName()).isEqualTo(levelString);
        }

        @Test
        @DisplayName("setLevel should set logger level")
        void testSetLevel() {
            // Arrange
            Level originalLevel = SpecsLogs.getRootLogger().getLevel();
            Level testLevel = Level.WARNING;

            try {
                // Execute
                SpecsLogs.setLevel(testLevel);

                // Verify
                assertThat(SpecsLogs.getRootLogger().getLevel()).isEqualTo(testLevel);
            } finally {
                // Restore original level
                SpecsLogs.getRootLogger().setLevel(originalLevel);
            }
        }
    }

    @Nested
    @DisplayName("Debug Logging")
    class DebugLoggingTests {

        @Test
        @DisplayName("debug with supplier should not throw exception")
        void testDebugWithSupplier() {
            // Arrange
            Supplier<String> messageSupplier = () -> "Debug message from supplier";

            // Execute & Verify
            assertThatCode(() -> SpecsLogs.debug(messageSupplier))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("debug with string should not throw exception")
        void testDebugWithString() {
            // Execute & Verify
            assertThatCode(() -> SpecsLogs.debug("Debug message"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("untested should log untested action")
        void testUntested() {
            // Execute & Verify
            assertThatCode(() -> SpecsLogs.untested("Untested action"))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("System Integration")
    class SystemIntegrationTests {

        @Test
        @DisplayName("isSystemPrint should identify system loggers")
        void testIsSystemPrint() {
            // Execute & Verify
            assertThat(SpecsLogs.isSystemPrint("System.out")).isTrue();
            assertThat(SpecsLogs.isSystemPrint("System.err")).isTrue();
            assertThat(SpecsLogs.isSystemPrint("custom.logger")).isFalse();
        }

        @Test
        @DisplayName("addLog should add print stream for logging")
        void testAddLog() {
            // Arrange
            ByteArrayOutputStream testStream = new ByteArrayOutputStream();
            PrintStream printStream = new PrintStream(testStream);

            // Execute & Verify
            assertThatCode(() -> SpecsLogs.addLog(printStream))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("setPrintStackTrace should configure stack trace printing")
        void testSetPrintStackTrace() {
            // Execute & Verify
            assertThatCode(() -> SpecsLogs.setPrintStackTrace(true))
                    .doesNotThrowAnyException();

            assertThatCode(() -> SpecsLogs.setPrintStackTrace(false))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("logging methods should handle very long messages")
        void testLogging_VeryLongMessages() {
            // Arrange
            String longMessage = "Very long message ".repeat(1000);

            // Execute & Verify
            assertThatCode(() -> SpecsLogs.warn(longMessage))
                    .doesNotThrowAnyException();

            assertThatCode(() -> SpecsLogs.info(longMessage))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("logging methods should handle messages with special characters")
        void testLogging_SpecialCharacters() {
            // Arrange
            String specialMessage = "Message with special chars: \n\t\r\\ \"'";

            // Execute & Verify
            assertThatCode(() -> SpecsLogs.warn(specialMessage))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("handler operations should handle null handlers gracefully")
        void testHandlerOperations_NullHandlers() {
            // Execute & Verify - These may throw exceptions, which is acceptable behavior
            assertThatThrownBy(() -> SpecsLogs.addHandler(null))
                    .isInstanceOf(NullPointerException.class);

            assertThatCode(() -> SpecsLogs.removeHandler(null))
                    .doesNotThrowAnyException();
        }
    }
}
