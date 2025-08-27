package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Comprehensive test suite for SimpleFileHandler class.
 * 
 * Tests the file-based logging handler that extends StreamHandler.
 * 
 * @author Generated Tests
 */
@DisplayName("SimpleFileHandler Tests")
class SimpleFileHandlerTest {

    @TempDir
    Path tempDir;

    private ByteArrayOutputStream memoryStream;
    private PrintStream testPrintStream;

    @BeforeEach
    void setUp() {
        memoryStream = new ByteArrayOutputStream();
        testPrintStream = new PrintStream(memoryStream);
    }

    @AfterEach
    void tearDown() {
        if (testPrintStream != null) {
            testPrintStream.close();
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create handler with PrintStream")
        void testConstructorWithPrintStream() {
            // When
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);

            // Then
            assertThat(handler).isNotNull();
            assertThat(handler).isInstanceOf(StreamHandler.class);
            assertThat(handler).isInstanceOf(SimpleFileHandler.class);
        }

        @Test
        @DisplayName("Should create handler with file-based PrintStream")
        void testConstructorWithFilePrintStream() throws IOException {
            // Given
            File logFile = tempDir.resolve("test.log").toFile();
            PrintStream filePrintStream = new PrintStream(new FileOutputStream(logFile));

            // When
            SimpleFileHandler handler = new SimpleFileHandler(filePrintStream);

            // Then
            assertThat(handler).isNotNull();

            // Cleanup
            filePrintStream.close();
        }

        @Test
        @DisplayName("Should handle System.out PrintStream")
        void testConstructorWithSystemOut() {
            // When
            SimpleFileHandler handler = new SimpleFileHandler(System.out);

            // Then
            assertThat(handler).isNotNull();
        }

        @Test
        @DisplayName("Should handle System.err PrintStream")
        void testConstructorWithSystemErr() {
            // When
            SimpleFileHandler handler = new SimpleFileHandler(System.err);

            // Then
            assertThat(handler).isNotNull();
        }

        @Test
        @DisplayName("Should create different instances with same stream")
        void testMultipleInstancesWithSameStream() {
            // When
            SimpleFileHandler handler1 = new SimpleFileHandler(testPrintStream);
            SimpleFileHandler handler2 = new SimpleFileHandler(testPrintStream);

            // Then
            assertThat(handler1).isNotSameAs(handler2);
        }
    }

    @Nested
    @DisplayName("Publish Method Tests")
    class PublishMethodTests {

        @Test
        @DisplayName("Should publish log record without throwing exceptions")
        void testPublishLogRecord() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
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
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                handler.publish(null);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should write to memory stream")
        void testPublishToMemoryStream() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());
            LogRecord record = new LogRecord(Level.INFO, "Memory stream test");

            // When
            handler.publish(record);

            // Then
            String output = memoryStream.toString();
            assertThat(output).contains("Memory stream test");
        }

        @Test
        @DisplayName("Should write to file stream")
        void testPublishToFileStream() throws IOException {
            // Given
            File logFile = tempDir.resolve("publish-test.log").toFile();
            PrintStream filePrintStream = new PrintStream(new FileOutputStream(logFile));
            SimpleFileHandler handler = new SimpleFileHandler(filePrintStream);
            handler.setFormatter(new ConsoleFormatter());
            LogRecord record = new LogRecord(Level.INFO, "File stream test");

            // When
            handler.publish(record);
            handler.close();
            filePrintStream.close();

            // Then
            String content = Files.readString(logFile.toPath());
            assertThat(content).contains("File stream test");
        }

        @Test
        @DisplayName("Should publish multiple records")
        void testPublishMultipleRecords() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());

            // When
            handler.publish(new LogRecord(Level.INFO, "Message 1"));
            handler.publish(new LogRecord(Level.WARNING, "Message 2"));
            handler.publish(new LogRecord(Level.SEVERE, "Message 3"));

            // Then
            String output = memoryStream.toString();
            assertThat(output).contains("Message 1");
            assertThat(output).contains("Message 2");
            assertThat(output).contains("Message 3");
        }

        @Test
        @DisplayName("Should publish with different log levels")
        void testPublishDifferentLevels() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());
            handler.setLevel(Level.ALL); // Allow all levels
            Level[] levels = { Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG, Level.FINE };

            // When
            for (int i = 0; i < levels.length; i++) {
                LogRecord record = new LogRecord(levels[i], "Level test " + i);
                handler.publish(record);
            }

            // Then
            String output = memoryStream.toString();
            for (int i = 0; i < levels.length; i++) {
                assertThat(output).contains("Level test " + i);
            }
        }

        @Test
        @DisplayName("Should flush immediately after publish")
        void testImmediateFlush() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());
            LogRecord record = new LogRecord(Level.INFO, "Immediate flush test");

            // When
            handler.publish(record);

            // Then - Output should be available immediately due to flush
            String output = memoryStream.toString();
            assertThat(output).contains("Immediate flush test");
        }
    }

    @Nested
    @DisplayName("Close Method Tests")
    class CloseMethodTests {

        @Test
        @DisplayName("Should close handler without throwing exceptions")
        void testCloseHandler() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
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
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);

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
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
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
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());
            LogRecord record = new LogRecord(Level.INFO, "Flush test message");

            // When
            handler.publish(record);
            handler.close();

            // Then - Should flush the record
            String output = memoryStream.toString();
            assertThat(output).contains("Flush test message");
        }

        @Test
        @DisplayName("Should not close underlying PrintStream")
        void testDoesNotClosePrintStream() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());

            // When
            handler.publish(new LogRecord(Level.INFO, "Before close"));
            handler.close();

            // Then - PrintStream should still be usable
            testPrintStream.print("Direct usage after handler close");
            String output = memoryStream.toString();
            assertThat(output).contains("Before close");
            assertThat(output).contains("Direct usage after handler close");
        }
    }

    @Nested
    @DisplayName("Handler Configuration Tests")
    class HandlerConfigurationTests {

        @Test
        @DisplayName("Should accept various formatters")
        void testDifferentFormatters() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
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

                // Simple formatter
                handler.setFormatter(new java.util.logging.SimpleFormatter());
                handler.publish(record);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should respect level filtering")
        void testLevelFiltering() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());
            handler.setLevel(Level.WARNING); // Only WARNING and above

            // When
            handler.publish(new LogRecord(Level.INFO, "Info message")); // Below threshold
            handler.publish(new LogRecord(Level.WARNING, "Warning message")); // At threshold
            handler.publish(new LogRecord(Level.SEVERE, "Severe message")); // Above threshold

            // Then
            String output = memoryStream.toString();
            assertThat(output).doesNotContain("Info message");
            assertThat(output).contains("Warning message");
            assertThat(output).contains("Severe message");

            // Verify level is set
            assertThat(handler.getLevel()).isEqualTo(Level.WARNING);
        }

        @Test
        @DisplayName("Should work with filters")
        void testWithFilters() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());
            handler.setFilter(record -> record.getLevel().intValue() >= Level.WARNING.intValue());

            // When
            handler.publish(new LogRecord(Level.INFO, "Filtered info"));
            handler.publish(new LogRecord(Level.WARNING, "Passed warning"));

            // Then
            String output = memoryStream.toString();
            assertThat(output).doesNotContain("Filtered info");
            assertThat(output).contains("Passed warning");
        }

        @Test
        @DisplayName("Should work without explicit formatter")
        void testWithoutExplicitFormatter() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            LogRecord record = new LogRecord(Level.INFO, "No explicit formatter");

            // When/Then - Should work with default formatter behavior
            assertThatCode(() -> {
                handler.publish(record);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("File Handling Tests")
    class FileHandlingTests {

        @Test
        @DisplayName("Should work with actual files")
        void testWithActualFile() throws IOException {
            // Given
            File logFile = tempDir.resolve("actual-file.log").toFile();
            PrintStream filePrintStream = new PrintStream(new FileOutputStream(logFile));
            SimpleFileHandler handler = new SimpleFileHandler(filePrintStream);
            handler.setFormatter(new ConsoleFormatter());

            // When
            handler.publish(new LogRecord(Level.INFO, "File message 1"));
            handler.publish(new LogRecord(Level.WARNING, "File message 2"));
            handler.close();
            filePrintStream.close();

            // Then
            assertThat(logFile).exists();
            String content = Files.readString(logFile.toPath());
            assertThat(content).contains("File message 1");
            assertThat(content).contains("File message 2");
        }

        @Test
        @DisplayName("Should append to existing files")
        void testAppendToExistingFile() throws IOException {
            // Given
            File logFile = tempDir.resolve("append-test.log").toFile();

            // First handler
            PrintStream firstStream = new PrintStream(new FileOutputStream(logFile));
            SimpleFileHandler firstHandler = new SimpleFileHandler(firstStream);
            firstHandler.setFormatter(new ConsoleFormatter());
            firstHandler.publish(new LogRecord(Level.INFO, "First message"));
            firstHandler.close();
            firstStream.close();

            // Second handler with append mode
            PrintStream secondStream = new PrintStream(new FileOutputStream(logFile, true));
            SimpleFileHandler secondHandler = new SimpleFileHandler(secondStream);
            secondHandler.setFormatter(new ConsoleFormatter());
            secondHandler.publish(new LogRecord(Level.INFO, "Second message"));
            secondHandler.close();
            secondStream.close();

            // Then
            String content = Files.readString(logFile.toPath());
            assertThat(content).contains("First message");
            assertThat(content).contains("Second message");
        }

        @Test
        @DisplayName("Should handle multiple handlers on same file")
        void testMultipleHandlersSameFile() throws IOException {
            // Given
            File logFile = tempDir.resolve("shared-file.log").toFile();
            PrintStream sharedStream = new PrintStream(new FileOutputStream(logFile));

            SimpleFileHandler handler1 = new SimpleFileHandler(sharedStream);
            SimpleFileHandler handler2 = new SimpleFileHandler(sharedStream);

            handler1.setFormatter(new ConsoleFormatter());
            handler2.setFormatter(new ConsoleFormatter());

            // When
            handler1.publish(new LogRecord(Level.INFO, "Handler 1 message"));
            handler2.publish(new LogRecord(Level.WARNING, "Handler 2 message"));

            handler1.close();
            handler2.close();
            sharedStream.close();

            // Then
            String content = Files.readString(logFile.toPath());
            assertThat(content).contains("Handler 1 message");
            assertThat(content).contains("Handler 2 message");
        }
    }

    @Nested
    @DisplayName("Integration with Java Logging Tests")
    class JavaLoggingIntegrationTests {

        @Test
        @DisplayName("Should work with java.util.logging framework")
        void testLoggingFrameworkIntegration() {
            // Given
            java.util.logging.Logger logger = java.util.logging.Logger.getLogger("file.integration.test");
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());

            // When
            logger.addHandler(handler);
            logger.setUseParentHandlers(false); // Avoid default handlers
            logger.info("File integration test message");

            // Then
            String output = memoryStream.toString();
            assertThat(output).contains("File integration test message");

            // Cleanup
            logger.removeHandler(handler);
        }

        @Test
        @DisplayName("Should extend StreamHandler properly")
        void testStreamHandlerInheritance() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);

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
            java.util.logging.Handler handler = new SimpleFileHandler(testPrintStream);
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
    @DisplayName("Concurrency Tests")
    class ConcurrencyTests {

        @Test
        @DisplayName("Should handle concurrent publishing")
        void testConcurrentPublishing() throws InterruptedException {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
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

            // Then - All messages should be present (order may vary due to concurrency)
            String output = memoryStream.toString();
            for (int i = 0; i < threadCount; i++) {
                for (int j = 0; j < 10; j++) {
                    assertThat(output).contains("Thread " + i + " message " + j);
                }
            }
        }

        @Test
        @DisplayName("Should handle concurrent close and publish")
        void testConcurrentCloseAndPublish() throws InterruptedException {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
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
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());
            LogRecord record = new LogRecord(Level.INFO, "");

            // When
            handler.publish(record);

            // Then - Should handle empty messages
            String output = memoryStream.toString();
            assertThat(output).isEmpty(); // ConsoleFormatter returns empty for empty message
        }

        @Test
        @DisplayName("Should handle messages with special characters")
        void testSpecialCharacters() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());
            String specialMessage = "Unicode: \u00E9\u00F1\u00FC, Newlines:\n\r, Tabs:\t";
            LogRecord record = new LogRecord(Level.INFO, specialMessage);

            // When
            handler.publish(record);

            // Then
            String output = memoryStream.toString();
            assertThat(output).contains(specialMessage);
        }

        @Test
        @DisplayName("Should handle very long messages")
        void testVeryLongMessages() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());
            StringBuilder longMessage = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longMessage.append("Long message part ").append(i).append(". ");
            }
            LogRecord record = new LogRecord(Level.INFO, longMessage.toString());

            // When
            handler.publish(record);

            // Then
            String output = memoryStream.toString();
            assertThat(output).contains("Long message part 0");
            assertThat(output).contains("Long message part 999");
        }

        @Test
        @DisplayName("Should handle null messages in LogRecord")
        void testNullMessages() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
            handler.setFormatter(new ConsoleFormatter());
            LogRecord record = new LogRecord(Level.INFO, null);

            // When/Then - Should handle null messages
            assertThatCode(() -> {
                handler.publish(record);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle rapid successive operations")
        void testRapidSuccessiveOperations() {
            // Given
            SimpleFileHandler handler = new SimpleFileHandler(testPrintStream);
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
    }
}
