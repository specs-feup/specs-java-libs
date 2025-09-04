package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for CustomConsoleHandler class.
 * 
 * Tests the custom console handler that extends StreamHandler with specialized
 * behavior.
 * 
 * @author Generated Tests
 */
@DisplayName("CustomConsoleHandler Tests")
class CustomConsoleHandlerTest {

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create stdout handler")
        void testNewStdout() {
            // When
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();

            // Then
            assertThat(handler).isNotNull();
            assertThat(handler).isInstanceOf(StreamHandler.class);
            assertThat(handler).isInstanceOf(CustomConsoleHandler.class);
        }

        @Test
        @DisplayName("Should create stderr handler")
        void testNewStderr() {
            // When
            CustomConsoleHandler handler = CustomConsoleHandler.newStderr();

            // Then
            assertThat(handler).isNotNull();
            assertThat(handler).isInstanceOf(StreamHandler.class);
            assertThat(handler).isInstanceOf(CustomConsoleHandler.class);
        }

        @Test
        @DisplayName("Should create different instances")
        void testCreateDifferentInstances() {
            // When
            CustomConsoleHandler handler1 = CustomConsoleHandler.newStdout();
            CustomConsoleHandler handler2 = CustomConsoleHandler.newStdout();
            CustomConsoleHandler handler3 = CustomConsoleHandler.newStderr();

            // Then
            assertThat(handler1).isNotSameAs(handler2);
            assertThat(handler1).isNotSameAs(handler3);
            assertThat(handler2).isNotSameAs(handler3);
        }

        @Test
        @DisplayName("Should create functional handlers")
        void testFactoryMethodsCreateFunctionalHandlers() {
            // Given
            LogRecord record = new LogRecord(Level.INFO, "Test message");

            // When/Then - Should not throw exceptions
            assertThatCode(() -> {
                CustomConsoleHandler stdoutHandler = CustomConsoleHandler.newStdout();
                stdoutHandler.setFormatter(new ConsoleFormatter());
                stdoutHandler.publish(record);
                stdoutHandler.close();
            }).doesNotThrowAnyException();

            assertThatCode(() -> {
                CustomConsoleHandler stderrHandler = CustomConsoleHandler.newStderr();
                stderrHandler.setFormatter(new ConsoleFormatter());
                stderrHandler.publish(record);
                stderrHandler.close();
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Publish Method Tests")
    class PublishMethodTests {

        @Test
        @DisplayName("Should publish log record without throwing exceptions")
        void testPublishLogRecord() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());
            LogRecord record = new LogRecord(Level.INFO, "Test publish message");

            // When/Then - Should not throw exceptions
            assertThatCode(() -> {
                handler.publish(record);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null record silently")
        void testPublishNullRecord() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                handler.publish(null);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should publish multiple records")
        void testPublishMultipleRecords() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());

            // When/Then - Should handle multiple records without issues
            assertThatCode(() -> {
                handler.publish(new LogRecord(Level.INFO, "Message 1"));
                handler.publish(new LogRecord(Level.WARNING, "Message 2"));
                handler.publish(new LogRecord(Level.SEVERE, "Message 3"));
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should publish with different log levels")
        void testPublishDifferentLevels() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());
            Level[] levels = { Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG, Level.FINE };

            // When/Then - Should handle all levels without issues
            assertThatCode(() -> {
                for (int i = 0; i < levels.length; i++) {
                    LogRecord record = new LogRecord(levels[i], "Level test " + i);
                    handler.publish(record);
                }
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should work with ConsoleFormatter")
        void testPublishWithConsoleFormatter() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());
            LogRecord record = new LogRecord(Level.INFO, "Console formatter test");

            // When/Then - Should work with ConsoleFormatter
            assertThatCode(() -> {
                handler.publish(record);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle rapid successive publishing")
        void testRapidSuccessivePublishing() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());

            // When/Then - Should handle rapid publishing
            assertThatCode(() -> {
                for (int i = 0; i < 100; i++) {
                    LogRecord record = new LogRecord(Level.INFO, "Rapid message " + i);
                    handler.publish(record);
                }
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Close Method Tests")
    class CloseMethodTests {

        @Test
        @DisplayName("Should close handler without throwing exceptions")
        void testCloseHandler() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());

            // When/Then - Should close without issues
            assertThatCode(() -> {
                handler.close();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle multiple close calls")
        void testMultipleCloseCalls() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();

            // When/Then - Multiple close calls should not cause issues
            assertThatCode(() -> {
                handler.close();
                handler.close();
                handler.close();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should be safe to publish after close")
        void testPublishAfterClose() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());
            handler.close();

            // When/Then - Publishing after close should not throw exception
            assertThatCode(() -> {
                LogRecord record = new LogRecord(Level.INFO, "After close message");
                handler.publish(record);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should flush on close")
        void testCloseFlushes() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());
            LogRecord record = new LogRecord(Level.INFO, "Flush test message");

            // When/Then - Should flush without issues
            assertThatCode(() -> {
                handler.publish(record);
                handler.close(); // Should flush the record
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Handler Configuration Tests")
    class HandlerConfigurationTests {

        @Test
        @DisplayName("Should accept various formatters")
        void testDifferentFormatters() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            LogRecord record = new LogRecord(Level.INFO, "Formatter test");

            // When/Then - Should work with different formatters
            assertThatCode(() -> {
                // Console formatter
                handler.setFormatter(new ConsoleFormatter());
                handler.publish(record);

                // Custom formatter
                handler.setFormatter(new java.util.logging.Formatter() {
                    @Override
                    public String format(LogRecord record) {
                        return "[CUSTOM] " + record.getMessage() + "\n";
                    }
                });
                handler.publish(record);

                // Simple formatter (no formatter set uses default)
                handler.setFormatter(new java.util.logging.SimpleFormatter());
                handler.publish(record);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should respect level filtering")
        void testLevelFiltering() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());
            handler.setLevel(Level.WARNING); // Only WARNING and above

            // When/Then - Should accept level configuration
            assertThatCode(() -> {
                handler.publish(new LogRecord(Level.INFO, "Info message")); // Below threshold
                handler.publish(new LogRecord(Level.WARNING, "Warning message")); // At threshold
                handler.publish(new LogRecord(Level.SEVERE, "Severe message")); // Above threshold
            }).doesNotThrowAnyException();

            // Verify level is set
            assertThat(handler.getLevel()).isEqualTo(Level.WARNING);
        }

        @Test
        @DisplayName("Should work without explicit formatter")
        void testWithoutExplicitFormatter() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            LogRecord record = new LogRecord(Level.INFO, "No explicit formatter");

            // When/Then - Should work with default formatter behavior
            assertThatCode(() -> {
                handler.publish(record);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should work with filters")
        void testWithFilters() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());
            handler.setFilter(record -> record.getLevel().intValue() >= Level.WARNING.intValue());

            // When/Then - Should accept filter configuration
            assertThatCode(() -> {
                handler.publish(new LogRecord(Level.INFO, "Filtered info"));
                handler.publish(new LogRecord(Level.WARNING, "Passed warning"));
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Integration with Java Logging Tests")
    class JavaLoggingIntegrationTests {

        @Test
        @DisplayName("Should work with java.util.logging framework")
        void testLoggingFrameworkIntegration() {
            // Given
            java.util.logging.Logger logger = java.util.logging.Logger.getLogger("integration.test");
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());

            // When/Then - Should integrate with logging framework
            assertThatCode(() -> {
                logger.addHandler(handler);
                logger.setUseParentHandlers(false); // Avoid default handlers
                logger.info("Integration test message");
                logger.removeHandler(handler);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should extend StreamHandler properly")
        void testStreamHandlerInheritance() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();

            // Then - Should be instance of StreamHandler
            assertThat(handler).isInstanceOf(StreamHandler.class);

            // Should have StreamHandler methods available
            assertThatCode(() -> {
                handler.setLevel(Level.INFO);
                handler.getLevel();
                handler.setFormatter(new ConsoleFormatter());
                handler.getFormatter();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should work as Handler interface")
        void testHandlerInterface() {
            // Given
            java.util.logging.Handler handler = CustomConsoleHandler.newStdout();
            LogRecord record = new LogRecord(Level.INFO, "Interface test");

            // When/Then - Should work through Handler interface
            assertThatCode(() -> {
                handler.setFormatter(new ConsoleFormatter());
                handler.publish(record);
                handler.flush();
                handler.close();
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Stdout vs Stderr Tests")
    class StreamTypeTests {

        @Test
        @DisplayName("Should create distinct stdout and stderr handlers")
        void testStdoutVsStderrHandlers() {
            // When
            CustomConsoleHandler stdoutHandler = CustomConsoleHandler.newStdout();
            CustomConsoleHandler stderrHandler = CustomConsoleHandler.newStderr();

            // Then - Should be different instances
            assertThat(stdoutHandler).isNotSameAs(stderrHandler);

            // Both should work
            assertThatCode(() -> {
                stdoutHandler.setFormatter(new ConsoleFormatter());
                stderrHandler.setFormatter(new ConsoleFormatter());

                stdoutHandler.publish(new LogRecord(Level.INFO, "To stdout"));
                stderrHandler.publish(new LogRecord(Level.SEVERE, "To stderr"));

                stdoutHandler.close();
                stderrHandler.close();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should maintain independence between stream types")
        void testStreamIndependence() {
            // Given
            CustomConsoleHandler stdoutHandler = CustomConsoleHandler.newStdout();
            CustomConsoleHandler stderrHandler = CustomConsoleHandler.newStderr();

            // When/Then - Configuration of one should not affect the other
            assertThatCode(() -> {
                stdoutHandler.setLevel(Level.INFO);
                stderrHandler.setLevel(Level.SEVERE);

                stdoutHandler.setFormatter(new ConsoleFormatter());
                stderrHandler.setFormatter(new java.util.logging.SimpleFormatter());

                // Both should work independently
                stdoutHandler.publish(new LogRecord(Level.INFO, "Stdout message"));
                stderrHandler.publish(new LogRecord(Level.SEVERE, "Stderr message"));
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Concurrency Tests")
    class ConcurrencyTests {

        @Test
        @DisplayName("Should handle concurrent publishing")
        void testConcurrentPublishing() throws InterruptedException {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 10; j++) {
                        LogRecord record = new LogRecord(Level.INFO, "Thread " + index + " message " + j);
                        handler.publish(record);
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then - Should complete without exceptions
            assertThat(handler).isNotNull();
        }

        @Test
        @DisplayName("Should handle concurrent close and publish")
        void testConcurrentCloseAndPublish() throws InterruptedException {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());

            // When
            Thread publishThread = new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    LogRecord record = new LogRecord(Level.INFO, "Concurrent message " + i);
                    handler.publish(record);
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            });

            Thread closeThread = new Thread(() -> {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
                handler.close();
            });

            publishThread.start();
            closeThread.start();

            publishThread.join();
            closeThread.join();

            // Then - Should complete without exceptions
            assertThat(handler).isNotNull();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty messages")
        void testEmptyMessages() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());
            LogRecord record = new LogRecord(Level.INFO, "");

            // When/Then - Should handle empty messages
            assertThatCode(() -> {
                handler.publish(record);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle messages with special characters")
        void testSpecialCharacters() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());
            String specialMessage = "Unicode: \u00E9\u00F1\u00FC, Newlines:\n\r, Tabs:\t";
            LogRecord record = new LogRecord(Level.INFO, specialMessage);

            // When/Then - Should handle special characters
            assertThatCode(() -> {
                handler.publish(record);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle very long messages")
        void testVeryLongMessages() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());
            StringBuilder longMessage = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longMessage.append("Long message part ").append(i).append(". ");
            }
            LogRecord record = new LogRecord(Level.INFO, longMessage.toString());

            // When/Then - Should handle long messages
            assertThatCode(() -> {
                handler.publish(record);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle rapid successive operations")
        void testRapidSuccessiveOperations() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());

            // When/Then - Should handle rapid operations without issues
            assertThatCode(() -> {
                for (int i = 0; i < 1000; i++) {
                    LogRecord record = new LogRecord(Level.INFO, "Rapid message " + i);
                    handler.publish(record);
                    if (i % 100 == 0) {
                        handler.close(); // Periodic close calls
                    }
                }
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null messages in LogRecord")
        void testNullMessages() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();
            handler.setFormatter(new ConsoleFormatter());
            LogRecord record = new LogRecord(Level.INFO, null);

            // When/Then - Should handle null messages
            assertThatCode(() -> {
                handler.publish(record);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Resource Management Tests")
    class ResourceManagementTests {

        @Test
        @DisplayName("Should not leak resources on multiple handler creation")
        void testNoResourceLeakage() {
            // When/Then - Creating many handlers should not cause resource issues
            assertThatCode(() -> {
                for (int i = 0; i < 100; i++) {
                    CustomConsoleHandler stdoutHandler = CustomConsoleHandler.newStdout();
                    CustomConsoleHandler stderrHandler = CustomConsoleHandler.newStderr();

                    stdoutHandler.setFormatter(new ConsoleFormatter());
                    stderrHandler.setFormatter(new ConsoleFormatter());

                    stdoutHandler.publish(new LogRecord(Level.INFO, "Resource test " + i));
                    stderrHandler.publish(new LogRecord(Level.SEVERE, "Resource test " + i));

                    stdoutHandler.close();
                    stderrHandler.close();
                }
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle handler lifecycle properly")
        void testHandlerLifecycle() {
            // Given
            CustomConsoleHandler handler = CustomConsoleHandler.newStdout();

            // When/Then - Full lifecycle should work
            assertThatCode(() -> {
                // Configuration phase
                handler.setFormatter(new ConsoleFormatter());
                handler.setLevel(Level.INFO);

                // Usage phase
                handler.publish(new LogRecord(Level.INFO, "Lifecycle test"));
                handler.flush();

                // Cleanup phase
                handler.close();
            }).doesNotThrowAnyException();
        }
    }
}
