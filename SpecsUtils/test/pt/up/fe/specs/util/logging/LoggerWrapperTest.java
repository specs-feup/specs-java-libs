package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for LoggerWrapper class.
 * 
 * Tests the wrapper around java.util.logging.Logger with convenience methods.
 * 
 * @author Generated Tests
 */
@DisplayName("LoggerWrapper Tests")
class LoggerWrapperTest {

    private static final String NEWLINE = System.getProperty("line.separator");

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create LoggerWrapper with logger name")
        void testConstructorWithName() {
            // Given
            String loggerName = "test.logger.wrapper";

            // When
            LoggerWrapper wrapper = new LoggerWrapper(loggerName);

            // Then
            assertThat(wrapper).isNotNull();
            assertThat(wrapper.getName()).isEqualTo(loggerName);
            assertThat(wrapper.getJavaLogger()).isNotNull();
            assertThat(wrapper.getJavaLogger().getName()).isEqualTo(loggerName);
        }

        @Test
        @DisplayName("Should handle various logger name formats")
        void testVariousLoggerNames() {
            String[] loggerNames = {
                    "simple",
                    "com.example.Logger",
                    "very.long.package.name.Logger",
                    "UPPER_CASE_LOGGER",
                    "mixedCase.Logger",
                    "logger-with-hyphens",
                    "logger_with_underscores",
                    "logger.with.123.numbers"
            };

            for (String name : loggerNames) {
                LoggerWrapper wrapper = new LoggerWrapper(name);
                assertThat(wrapper.getName()).isEqualTo(name);
                assertThat(wrapper.getJavaLogger().getName()).isEqualTo(name);
            }
        }

        @Test
        @DisplayName("Should handle empty logger name")
        void testEmptyLoggerName() {
            // When
            LoggerWrapper wrapper = new LoggerWrapper("");

            // Then
            assertThat(wrapper.getName()).isEmpty();
            assertThat(wrapper.getJavaLogger()).isNotNull();
        }

        @Test
        @DisplayName("Should handle null logger name")
        void testNullLoggerName() {
            // When/Then - Logger.getLogger(null) throws NPE in ConcurrentHashMap
            assertThatThrownBy(() -> {
                new LoggerWrapper(null);
            }).isInstanceOf(NullPointerException.class);
            // Note: NPE message varies between Java versions, so we only check exception
            // type
        }

        @Test
        @DisplayName("Should create unique instances for different names")
        void testUniqueInstances() {
            // Given
            LoggerWrapper wrapper1 = new LoggerWrapper("unique.1");
            LoggerWrapper wrapper2 = new LoggerWrapper("unique.2");

            // When/Then
            assertThat(wrapper1).isNotSameAs(wrapper2);
            assertThat(wrapper1.getName()).isNotEqualTo(wrapper2.getName());
            assertThat(wrapper1.getJavaLogger()).isNotSameAs(wrapper2.getJavaLogger());
        }

        @Test
        @DisplayName("Should cache same-named loggers from Java logging framework")
        void testJavaLoggerCaching() {
            // Given
            String sharedName = "shared.logger.name";
            LoggerWrapper wrapper1 = new LoggerWrapper(sharedName);
            LoggerWrapper wrapper2 = new LoggerWrapper(sharedName);

            // When/Then - Java Logger instances should be the same for same name
            assertThat(wrapper1).isNotSameAs(wrapper2); // Different wrapper instances
            assertThat(wrapper1.getJavaLogger()).isSameAs(wrapper2.getJavaLogger()); // Same Java logger
        }
    }

    @Nested
    @DisplayName("Access Method Tests")
    class AccessMethodTests {

        @Test
        @DisplayName("Should provide access to logger name")
        void testGetName() {
            // Given
            String loggerName = "access.test.logger";
            LoggerWrapper wrapper = new LoggerWrapper(loggerName);

            // When
            String name = wrapper.getName();

            // Then
            assertThat(name).isEqualTo(loggerName);
        }

        @Test
        @DisplayName("Should provide access to underlying Java logger")
        void testGetJavaLogger() {
            // Given
            String loggerName = "java.logger.access";
            LoggerWrapper wrapper = new LoggerWrapper(loggerName);

            // When
            Logger javaLogger = wrapper.getJavaLogger();

            // Then
            assertThat(javaLogger).isNotNull();
            assertThat(javaLogger.getName()).isEqualTo(loggerName);
            assertThat(javaLogger).isInstanceOf(Logger.class);
        }

        @Test
        @DisplayName("Should maintain consistency between name and Java logger")
        void testNameConsistency() {
            // Given
            String loggerName = "consistency.test";
            LoggerWrapper wrapper = new LoggerWrapper(loggerName);

            // When
            String wrapperName = wrapper.getName();
            String javaLoggerName = wrapper.getJavaLogger().getName();

            // Then
            assertThat(wrapperName).isEqualTo(javaLoggerName);
        }

        @Test
        @DisplayName("Should allow configuration through Java logger")
        void testJavaLoggerConfiguration() {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("config.test");
            Logger javaLogger = wrapper.getJavaLogger();

            // When
            javaLogger.setLevel(Level.WARNING);

            // Then
            assertThat(javaLogger.getLevel()).isEqualTo(Level.WARNING);
            // Verify it's the same instance
            assertThat(wrapper.getJavaLogger().getLevel()).isEqualTo(Level.WARNING);
        }
    }

    @Nested
    @DisplayName("Info Logging Tests")
    class InfoLoggingTests {

        @Test
        @DisplayName("Should log info messages")
        void testInfoLogging() {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("info.test");
            String message = "Test info message";

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                wrapper.info(message);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle empty info messages")
        void testEmptyInfoMessage() {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("empty.info.test");

            // When/Then - Should handle empty messages
            assertThatCode(() -> {
                wrapper.info("");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null info messages")
        void testNullInfoMessage() {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("null.info.test");

            // When/Then - Should throw NPE in parseMessage when checking msg.isEmpty()
            assertThatThrownBy(() -> {
                wrapper.info(null);
            }).isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke \"String.isEmpty()\" because \"msg\" is null");
        }

        @Test
        @DisplayName("Should handle very long info messages")
        void testVeryLongInfoMessage() {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("long.info.test");
            StringBuilder longMessage = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longMessage.append("Long message part ").append(i).append(". ");
            }
            String message = longMessage.toString();

            // When/Then
            assertThatCode(() -> {
                wrapper.info(message);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle special characters in info messages")
        void testSpecialCharactersInInfo() {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("special.chars.info");
            String specialMessage = "Message with unicode: \u00E9\u00F1\u00FC and symbols: @#$%^&*()";

            // When/Then
            assertThatCode(() -> {
                wrapper.info(specialMessage);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle messages with existing newlines")
        void testMessagesWithNewlines() {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("newline.test");
            String messageWithNewline = "Message with newline" + NEWLINE;
            String messageWithMultipleNewlines = "Line 1" + NEWLINE + "Line 2" + NEWLINE;

            // When/Then
            assertThatCode(() -> {
                wrapper.info(messageWithNewline);
                wrapper.info(messageWithMultipleNewlines);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Message Parsing Tests")
    class MessageParsingTests {

        @Test
        @DisplayName("Should add newline to non-empty messages")
        void testParseMessageAddsNewline() throws Exception {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("parse.test");
            Method parseMessage = LoggerWrapper.class.getDeclaredMethod("parseMessage", String.class);
            parseMessage.setAccessible(true);

            String message = "Test message without newline";

            // When
            String result = (String) parseMessage.invoke(wrapper, message);

            // Then
            assertThat(result).isEqualTo(message + NEWLINE);
        }

        @Test
        @DisplayName("Should not modify empty messages")
        void testParseMessageEmptyString() throws Exception {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("parse.empty.test");
            Method parseMessage = LoggerWrapper.class.getDeclaredMethod("parseMessage", String.class);
            parseMessage.setAccessible(true);

            String emptyMessage = "";

            // When
            String result = (String) parseMessage.invoke(wrapper, emptyMessage);

            // Then
            assertThat(result).isEqualTo(emptyMessage);
        }

        @Test
        @DisplayName("Should handle messages with existing newlines")
        void testParseMessageWithExistingNewline() throws Exception {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("parse.existing.test");
            Method parseMessage = LoggerWrapper.class.getDeclaredMethod("parseMessage", String.class);
            parseMessage.setAccessible(true);

            String messageWithNewline = "Message with newline" + NEWLINE;

            // When
            String result = (String) parseMessage.invoke(wrapper, messageWithNewline);

            // Then
            assertThat(result).isEqualTo(messageWithNewline + NEWLINE); // Adds another newline
        }

        @Test
        @DisplayName("Should handle whitespace-only messages")
        void testParseMessageWhitespace() throws Exception {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("parse.whitespace.test");
            Method parseMessage = LoggerWrapper.class.getDeclaredMethod("parseMessage", String.class);
            parseMessage.setAccessible(true);

            String whitespaceMessage = "   ";

            // When
            String result = (String) parseMessage.invoke(wrapper, whitespaceMessage);

            // Then
            assertThat(result).isEqualTo(whitespaceMessage + NEWLINE);
        }

        @Test
        @DisplayName("Should handle various newline formats")
        void testParseMessageVariousNewlines() throws Exception {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("parse.various.test");
            Method parseMessage = LoggerWrapper.class.getDeclaredMethod("parseMessage", String.class);
            parseMessage.setAccessible(true);

            String[] messages = {
                    "Message\n",
                    "Message\r",
                    "Message\r\n",
                    "Message" + NEWLINE
            };

            // When/Then
            for (String message : messages) {
                String result = (String) parseMessage.invoke(wrapper, message);
                assertThat(result).isEqualTo(message + NEWLINE);
            }
        }

        @Test
        @DisplayName("Should handle null messages in parsing")
        void testParseMessageNull() throws Exception {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("parse.null.test");
            Method parseMessage = LoggerWrapper.class.getDeclaredMethod("parseMessage", String.class);
            parseMessage.setAccessible(true);

            // When/Then - Should throw NPE when checking msg.isEmpty()
            assertThatThrownBy(() -> {
                parseMessage.invoke(wrapper, (String) null);
            }).hasCauseInstanceOf(NullPointerException.class)
                    .cause().hasMessageContaining("Cannot invoke \"String.isEmpty()\" because \"msg\" is null");
        }
    }

    @Nested
    @DisplayName("Integration with Java Logging Tests")
    class JavaLoggingIntegrationTests {

        @Test
        @DisplayName("Should integrate with Java logging framework")
        void testJavaLoggingIntegration() {
            // Given
            String loggerName = "integration.test";
            LoggerWrapper wrapper = new LoggerWrapper(loggerName);

            // When
            Logger directLogger = Logger.getLogger(loggerName);
            Logger wrapperLogger = wrapper.getJavaLogger();

            // Then - Should be the same logger instance
            assertThat(wrapperLogger).isSameAs(directLogger);
        }

        @Test
        @DisplayName("Should respect Java logger level settings")
        void testJavaLoggerLevelSettings() {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("level.test");
            Logger javaLogger = wrapper.getJavaLogger();

            // When
            javaLogger.setLevel(Level.SEVERE);

            // Then - info should still be called, but may not be logged depending on level
            assertThatCode(() -> {
                wrapper.info("This may not be logged due to level");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should work with Java logger hierarchy")
        void testJavaLoggerHierarchy() {
            // Given
            LoggerWrapper parentWrapper = new LoggerWrapper("parent");
            LoggerWrapper childWrapper = new LoggerWrapper("parent.child");

            // When
            Logger childLogger = childWrapper.getJavaLogger();

            // Then - Child should inherit from parent in Java logging hierarchy
            assertThat(childLogger.getParent()).isNotNull();
            // Note: The exact parent relationship depends on Java logging configuration
            // Both wrappers should provide access to their respective loggers
            assertThat(parentWrapper.getJavaLogger()).isNotNull();
        }

        @Test
        @DisplayName("Should support Java logger configuration")
        void testJavaLoggerConfiguration() {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("config.integration.test");
            Logger javaLogger = wrapper.getJavaLogger();

            // When - Configure various aspects
            javaLogger.setLevel(Level.CONFIG);
            javaLogger.setUseParentHandlers(false);

            // Then - Configuration should be preserved
            assertThat(javaLogger.getLevel()).isEqualTo(Level.CONFIG);
            assertThat(javaLogger.getUseParentHandlers()).isFalse();
        }
    }

    @Nested
    @DisplayName("Concurrency Tests")
    class ConcurrencyTests {

        @Test
        @DisplayName("Should handle concurrent info logging")
        void testConcurrentInfoLogging() throws InterruptedException {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("concurrent.info.test");
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 100; j++) {
                        wrapper.info("Concurrent message " + index + "-" + j);
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then - Should complete without exceptions
            assertThat(wrapper.getName()).isEqualTo("concurrent.info.test");
        }

        @Test
        @DisplayName("Should handle concurrent wrapper creation")
        void testConcurrentWrapperCreation() throws InterruptedException {
            // Given
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];
            LoggerWrapper[] wrappers = new LoggerWrapper[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    wrappers[index] = new LoggerWrapper("concurrent.creation." + index);
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then - All wrappers should be created successfully
            for (int i = 0; i < threadCount; i++) {
                assertThat(wrappers[i]).isNotNull();
                assertThat(wrappers[i].getName()).isEqualTo("concurrent.creation." + i);
            }
        }

        @Test
        @DisplayName("Should handle concurrent access to same-named loggers")
        void testConcurrentSameNamedLoggers() throws InterruptedException {
            // Given
            String sharedName = "shared.concurrent.logger";
            int threadCount = 5;
            Thread[] threads = new Thread[threadCount];
            LoggerWrapper[] wrappers = new LoggerWrapper[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    wrappers[index] = new LoggerWrapper(sharedName);
                    wrappers[index].info("Message from thread " + index);
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then - All should have same logger name and same underlying Java logger
            Logger firstJavaLogger = wrappers[0].getJavaLogger();
            for (LoggerWrapper wrapper : wrappers) {
                assertThat(wrapper.getName()).isEqualTo(sharedName);
                assertThat(wrapper.getJavaLogger()).isSameAs(firstJavaLogger);
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle system property changes")
        void testSystemPropertyChanges() {
            // Given
            String originalNewline = System.getProperty("line.separator");
            LoggerWrapper wrapper = new LoggerWrapper("system.property.test");

            try {
                // When - Change system property (not recommended in practice)
                System.setProperty("line.separator", "\n");

                // Then - Should still work (uses static NEWLINE field)
                assertThatCode(() -> {
                    wrapper.info("Message after property change");
                }).doesNotThrowAnyException();

            } finally {
                // Restore original property
                System.setProperty("line.separator", originalNewline);
            }
        }

        @Test
        @DisplayName("Should handle messages with various line ending styles")
        void testVariousLineEndings() {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("line.endings.test");
            String[] messages = {
                    "Unix style\n",
                    "Windows style\r\n",
                    "Old Mac style\r",
                    "Mixed\n\r\nstyles\r"
            };

            // When/Then
            for (String message : messages) {
                assertThatCode(() -> {
                    wrapper.info(message);
                }).doesNotThrowAnyException();
            }
        }

        @Test
        @DisplayName("Should handle logger names with special characters")
        void testSpecialCharacterLoggerNames() {
            String[] specialNames = {
                    "logger.with.dots",
                    "logger$with$dollars",
                    "logger-with-hyphens",
                    "logger_with_underscores",
                    "logger with spaces",
                    "logger@with@symbols",
                    "logger#with#hash",
                    "logger%with%percent"
            };

            for (String name : specialNames) {
                assertThatCode(() -> {
                    LoggerWrapper wrapper = new LoggerWrapper(name);
                    wrapper.info("Test message for " + name);
                    assertThat(wrapper.getName()).isEqualTo(name);
                }).doesNotThrowAnyException();
            }
        }

        @Test
        @DisplayName("Should handle extremely long logger names")
        void testExtremelyLongLoggerNames() {
            // Given
            StringBuilder longName = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longName.append("very.long.logger.name.segment.").append(i).append(".");
            }
            String loggerName = longName.toString();

            // When/Then
            assertThatCode(() -> {
                LoggerWrapper wrapper = new LoggerWrapper(loggerName);
                wrapper.info("Message for very long logger name");
                assertThat(wrapper.getName()).isEqualTo(loggerName);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle rapid successive calls")
        void testRapidSuccessiveCalls() {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("rapid.calls.test");

            // When/Then - Rapid successive calls should not cause issues
            assertThatCode(() -> {
                for (int i = 0; i < 1000; i++) {
                    wrapper.info("Rapid message " + i);
                }
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Memory and Resource Tests")
    class MemoryAndResourceTests {

        @Test
        @DisplayName("Should maintain logger reference to prevent garbage collection")
        void testLoggerReferencePreservation() {
            // Given
            LoggerWrapper wrapper = new LoggerWrapper("gc.test");
            Logger originalLogger = wrapper.getJavaLogger();

            // When - Multiple accesses should return same instance
            Logger logger1 = wrapper.getJavaLogger();
            Logger logger2 = wrapper.getJavaLogger();
            Logger logger3 = wrapper.getJavaLogger();

            // Then - Should be same instances (preserved by wrapper)
            assertThat(logger1).isSameAs(originalLogger);
            assertThat(logger2).isSameAs(originalLogger);
            assertThat(logger3).isSameAs(originalLogger);
        }

        @Test
        @DisplayName("Should work after multiple wrapper instances")
        void testMultipleWrapperInstances() {
            // Given/When - Create many wrapper instances
            LoggerWrapper[] wrappers = new LoggerWrapper[100];
            for (int i = 0; i < 100; i++) {
                wrappers[i] = new LoggerWrapper("multiple.instance.test." + i);
            }

            // Then - All should work correctly
            for (int i = 0; i < 100; i++) {
                final int index = i;
                assertThatCode(() -> {
                    wrappers[index].info("Message from wrapper " + index);
                    assertThat(wrappers[index].getName()).isEqualTo("multiple.instance.test." + index);
                }).doesNotThrowAnyException();
            }
        }
    }
}
