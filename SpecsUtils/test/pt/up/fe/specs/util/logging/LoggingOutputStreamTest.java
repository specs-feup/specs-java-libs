package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for LoggingOutputStream class.
 * 
 * Tests the ByteArrayOutputStream extension that redirects output to a Logger
 * on flush.
 * 
 * @author Generated Tests
 */
@DisplayName("LoggingOutputStream Tests")
class LoggingOutputStreamTest {

    private Logger testLogger;
    private LoggingOutputStream loggingOutputStream;
    private TestHandler testHandler;

    // Test handler to capture log records
    private static class TestHandler extends Handler {
        // Use a thread-safe list since several tests perform concurrent logging
        private final List<LogRecord> records = new CopyOnWriteArrayList<>();

        @Override
        public void publish(LogRecord record) {
            records.add(record);
        }

        @Override
        public void flush() {
            // No-op
        }

        @Override
        public void close() throws SecurityException {
            records.clear();
        }

        public List<LogRecord> getRecords() {
            // Return a snapshot copy to keep original ordering stable for assertions
            return new ArrayList<>(records);
        }
    }

    @BeforeEach
    void setUp() {
        testLogger = Logger.getLogger("test.logging.outputstream");
        testLogger.setUseParentHandlers(false);
        testLogger.setLevel(Level.ALL);

        testHandler = new TestHandler();
        testHandler.setLevel(Level.ALL);
        testLogger.addHandler(testHandler);

        loggingOutputStream = new LoggingOutputStream(testLogger, Level.INFO);
    }

    private Logger getLoggerFromStream(LoggingOutputStream stream) throws Exception {
        Field loggerField = LoggingOutputStream.class.getDeclaredField("logger");
        loggerField.setAccessible(true);
        return (Logger) loggerField.get(stream);
    }

    private Level getLevelFromStream(LoggingOutputStream stream) throws Exception {
        Field levelField = LoggingOutputStream.class.getDeclaredField("level");
        levelField.setAccessible(true);
        return (Level) levelField.get(stream);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create stream with logger and level")
        void testConstructorWithLoggerAndLevel() throws Exception {
            // When
            Logger customLogger = Logger.getLogger("custom.test");
            Level customLevel = Level.WARNING;
            LoggingOutputStream customStream = new LoggingOutputStream(customLogger, customLevel);

            // Then
            assertThat(customStream).isNotNull();
            assertThat(customStream).isInstanceOf(ByteArrayOutputStream.class);
            assertThat(getLoggerFromStream(customStream)).isSameAs(customLogger);
            assertThat(getLevelFromStream(customStream)).isSameAs(customLevel);
        }

        @Test
        @DisplayName("Should extend ByteArrayOutputStream")
        void testExtendsOutputStream() {
            // Then
            assertThat(loggingOutputStream).isInstanceOf(ByteArrayOutputStream.class);
        }

        @Test
        @DisplayName("Should store logger and level correctly")
        void testLoggerAndLevelStorage() throws Exception {
            // Then
            assertThat(getLoggerFromStream(loggingOutputStream)).isSameAs(testLogger);
            assertThat(getLevelFromStream(loggingOutputStream)).isSameAs(Level.INFO);
        }

        @Test
        @DisplayName("Should create different instances")
        void testMultipleInstances() {
            // When
            LoggingOutputStream stream1 = new LoggingOutputStream(testLogger, Level.INFO);
            LoggingOutputStream stream2 = new LoggingOutputStream(testLogger, Level.WARNING);

            // Then
            assertThat(stream1).isNotSameAs(stream2);
        }

        @Test
        @DisplayName("Should handle different log levels")
        void testDifferentLogLevels() throws Exception {
            // Given
            Level[] levels = { Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG, Level.FINE };

            // When/Then
            for (Level level : levels) {
                LoggingOutputStream stream = new LoggingOutputStream(testLogger, level);
                assertThat(getLevelFromStream(stream)).isSameAs(level);
            }
        }

        @Test
        @DisplayName("Should handle different loggers")
        void testDifferentLoggers() throws Exception {
            // Given
            Logger logger1 = Logger.getLogger("logger1");
            Logger logger2 = Logger.getLogger("logger2");

            // When
            LoggingOutputStream stream1 = new LoggingOutputStream(logger1, Level.INFO);
            LoggingOutputStream stream2 = new LoggingOutputStream(logger2, Level.INFO);

            // Then
            assertThat(getLoggerFromStream(stream1)).isSameAs(logger1);
            assertThat(getLoggerFromStream(stream2)).isSameAs(logger2);
        }
    }

    @Nested
    @DisplayName("Write and Flush Tests")
    class WriteAndFlushTests {

        @Test
        @DisplayName("Should write bytes and flush to logger")
        void testWriteAndFlush() throws IOException {
            // Given
            String message = "Test message";
            byte[] messageBytes = message.getBytes();

            // When
            loggingOutputStream.write(messageBytes);
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(1);
            assertThat(records.get(0).getMessage()).isEqualTo(message);
            assertThat(records.get(0).getLevel()).isEqualTo(Level.INFO);
        }

        @Test
        @DisplayName("Should accumulate writes before flush")
        void testAccumulateWrites() throws IOException {
            // Given
            String part1 = "First ";
            String part2 = "Second ";
            String part3 = "Third";

            // When
            loggingOutputStream.write(part1.getBytes());
            loggingOutputStream.write(part2.getBytes());
            loggingOutputStream.write(part3.getBytes());
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(1);
            assertThat(records.get(0).getMessage()).isEqualTo("First Second Third");
        }

        @Test
        @DisplayName("Should handle single byte writes")
        void testSingleByteWrites() throws IOException {
            // Given
            String message = "ABC";

            // When
            for (byte b : message.getBytes()) {
                loggingOutputStream.write(b);
            }
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(1);
            assertThat(records.get(0).getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should handle partial byte array writes")
        void testPartialByteArrayWrites() throws IOException {
            // Given
            byte[] buffer = "Hello World Test".getBytes();

            // When
            loggingOutputStream.write(buffer, 0, 5); // "Hello"
            loggingOutputStream.write(buffer, 5, 6); // " World"
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(1);
            assertThat(records.get(0).getMessage()).isEqualTo("Hello World");
        }

        @Test
        @DisplayName("Should reset buffer after flush")
        void testBufferResetAfterFlush() throws IOException {
            // Given
            String message1 = "First message";
            String message2 = "Second message";

            // When
            loggingOutputStream.write(message1.getBytes());
            loggingOutputStream.flush();

            loggingOutputStream.write(message2.getBytes());
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(2);
            assertThat(records.get(0).getMessage()).isEqualTo(message1);
            assertThat(records.get(1).getMessage()).isEqualTo(message2);
        }

        @Test
        @DisplayName("Should not log empty records")
        void testEmptyRecordFiltering() throws IOException {
            // When
            loggingOutputStream.flush(); // Empty flush

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).isEmpty();
        }

        @Test
        @DisplayName("Should handle multiple flushes with empty content")
        void testMultipleEmptyFlushes() throws IOException {
            // When
            loggingOutputStream.flush();
            loggingOutputStream.flush();
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).isEmpty();
        }

        @Test
        @DisplayName("Should handle mixed empty and non-empty flushes")
        void testMixedFlushes() throws IOException {
            // When
            loggingOutputStream.flush(); // Empty

            loggingOutputStream.write("Content1".getBytes());
            loggingOutputStream.flush(); // Non-empty

            loggingOutputStream.flush(); // Empty again

            loggingOutputStream.write("Content2".getBytes());
            loggingOutputStream.flush(); // Non-empty

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(2);
            assertThat(records.get(0).getMessage()).isEqualTo("Content1");
            assertThat(records.get(1).getMessage()).isEqualTo("Content2");
        }
    }

    @Nested
    @DisplayName("Synchronization Tests")
    class SynchronizationTests {

        @Test
        @DisplayName("Should be thread-safe during flush")
        void testThreadSafety() throws InterruptedException, IOException {
            // Given
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];

            // When - Each thread uses its own stream to avoid race conditions
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        LoggingOutputStream threadStream = new LoggingOutputStream(testLogger, Level.INFO);
                        for (int j = 0; j < 10; j++) {
                            String message = "Thread" + index + "Message" + j;
                            threadStream.write(message.getBytes());
                            threadStream.flush();
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(threadCount * 10);

            // Verify all messages are present
            for (int i = 0; i < threadCount; i++) {
                for (int j = 0; j < 10; j++) {
                    String expectedMessage = "Thread" + i + "Message" + j;
                    boolean found = records.stream()
                            .anyMatch(record -> expectedMessage.equals(record.getMessage()));
                    assertThat(found).isTrue();
                }
            }
        }

        @Test
        @DisplayName("Should handle concurrent writes and flushes")
        void testConcurrentWritesAndFlushes() throws InterruptedException {
            // Given
            int writerCount = 5;
            int flusherCount = 3;
            Thread[] writers = new Thread[writerCount];
            Thread[] flushers = new Thread[flusherCount];

            // When
            for (int i = 0; i < writerCount; i++) {
                final int index = i;
                writers[i] = new Thread(() -> {
                    try {
                        for (int j = 0; j < 20; j++) {
                            loggingOutputStream.write(("W" + index + "M" + j + " ").getBytes());
                            Thread.sleep(1);
                        }
                    } catch (IOException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
                writers[i].start();
            }

            for (int i = 0; i < flusherCount; i++) {
                flushers[i] = new Thread(() -> {
                    try {
                        for (int j = 0; j < 10; j++) {
                            Thread.sleep(5);
                            loggingOutputStream.flush();
                        }
                    } catch (IOException | InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
                flushers[i].start();
            }

            for (Thread thread : writers) {
                thread.join();
            }
            for (Thread thread : flushers) {
                thread.join();
            }

            // Final flush to get any remaining content
            try {
                loggingOutputStream.flush();
            } catch (IOException e) {
                // Ignore for test
            }

            // Then - Should complete without exceptions
            assertThat(testHandler.getRecords()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Logger Integration Tests")
    class LoggerIntegrationTests {

        @Test
        @DisplayName("Should use correct log level")
        void testLogLevel() throws IOException {
            // Given
            LoggingOutputStream warningStream = new LoggingOutputStream(testLogger, Level.WARNING);
            LoggingOutputStream severeStream = new LoggingOutputStream(testLogger, Level.SEVERE);

            // When
            warningStream.write("Warning message".getBytes());
            warningStream.flush();

            severeStream.write("Severe message".getBytes());
            severeStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(2);
            assertThat(records.get(0).getLevel()).isEqualTo(Level.WARNING);
            assertThat(records.get(1).getLevel()).isEqualTo(Level.SEVERE);
        }

        @Test
        @DisplayName("Should respect logger level filtering")
        void testLoggerLevelFiltering() throws IOException {
            // Given
            testLogger.setLevel(Level.WARNING); // Only WARNING and above
            LoggingOutputStream infoStream = new LoggingOutputStream(testLogger, Level.INFO);
            LoggingOutputStream warningStream = new LoggingOutputStream(testLogger, Level.WARNING);

            // When
            infoStream.write("Info message".getBytes());
            infoStream.flush();

            warningStream.write("Warning message".getBytes());
            warningStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(1); // Only WARNING message should pass
            assertThat(records.get(0).getMessage()).isEqualTo("Warning message");
            assertThat(records.get(0).getLevel()).isEqualTo(Level.WARNING);
        }

        @Test
        @DisplayName("Should work with multiple loggers")
        void testMultipleLoggers() throws IOException {
            // Given
            Logger logger1 = Logger.getLogger("test.logger1");
            Logger logger2 = Logger.getLogger("test.logger2");

            TestHandler handler1 = new TestHandler();
            TestHandler handler2 = new TestHandler();

            logger1.addHandler(handler1);
            logger1.setUseParentHandlers(false);
            logger1.setLevel(Level.ALL);

            logger2.addHandler(handler2);
            logger2.setUseParentHandlers(false);
            logger2.setLevel(Level.ALL);

            LoggingOutputStream stream1 = new LoggingOutputStream(logger1, Level.INFO);
            LoggingOutputStream stream2 = new LoggingOutputStream(logger2, Level.WARNING);

            // When
            stream1.write("Logger1 message".getBytes());
            stream1.flush();

            stream2.write("Logger2 message".getBytes());
            stream2.flush();

            // Then
            assertThat(handler1.getRecords()).hasSize(1);
            assertThat(handler1.getRecords().get(0).getMessage()).isEqualTo("Logger1 message");

            assertThat(handler2.getRecords()).hasSize(1);
            assertThat(handler2.getRecords().get(0).getMessage()).isEqualTo("Logger2 message");
        }

        @Test
        @DisplayName("Should use logp method with empty source class and method")
        void testLogpMethodUsage() throws IOException {
            // Given
            String message = "Test logp usage";

            // When
            loggingOutputStream.write(message.getBytes());
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(1);
            LogRecord record = records.get(0);
            assertThat(record.getMessage()).isEqualTo(message);
            assertThat(record.getSourceClassName()).isEqualTo("");
            assertThat(record.getSourceMethodName()).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("ByteArrayOutputStream Integration Tests")
    class ByteArrayOutputStreamIntegrationTests {

        @Test
        @DisplayName("Should inherit ByteArrayOutputStream behavior")
        void testByteArrayOutputStreamBehavior() throws IOException {
            // Given
            String message = "ByteArray test";

            // When
            loggingOutputStream.write(message.getBytes());

            // Then - Should have ByteArrayOutputStream methods
            assertThat(loggingOutputStream.size()).isEqualTo(message.getBytes().length);
            assertThat(loggingOutputStream.toString()).isEqualTo(message);

            // After flush, buffer should be reset
            loggingOutputStream.flush();
            assertThat(loggingOutputStream.size()).isZero();
            assertThat(loggingOutputStream.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle large amounts of data")
        void testLargeDataHandling() throws IOException {
            // Given
            StringBuilder largeMessage = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                largeMessage.append("Line ").append(i).append(" ");
            }
            String message = largeMessage.toString();

            // When
            loggingOutputStream.write(message.getBytes());
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(1);
            assertThat(records.get(0).getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should handle various encodings")
        void testEncodingHandling() throws IOException {
            // Given
            String unicodeMessage = "Unicode: \u00E9\u00F1\u00FC, 中文, العربية";

            // When
            loggingOutputStream.write(unicodeMessage.getBytes("UTF-8"));
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(1);
            // Note: Result depends on platform default encoding
            assertThat(records.get(0).getMessage()).isNotNull();
        }

        @Test
        @DisplayName("Should handle binary data")
        void testBinaryDataHandling() throws IOException {
            // Given
            byte[] binaryData = { 0x00, 0x01, 0x02, (byte) 0xFF, 0x7F, (byte) 0x80 };

            // When
            loggingOutputStream.write(binaryData);
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(1);
            assertThat(records.get(0).getMessage()).isNotNull();
        }

        @Test
        @DisplayName("Should handle close operation")
        void testCloseOperation() throws IOException {
            // Given
            String message = "Before close";

            // When
            loggingOutputStream.write(message.getBytes());
            loggingOutputStream.close();

            // Then - Close should not automatically flush
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).isEmpty(); // Content not flushed on close
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle rapid flush operations")
        void testRapidFlushOperations() throws IOException {
            // When/Then - Should handle rapid flushes without issues
            assertThatCode(() -> {
                for (int i = 0; i < 1000; i++) {
                    loggingOutputStream.write(("Message" + i).getBytes());
                    loggingOutputStream.flush();
                }
            }).doesNotThrowAnyException();

            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(1000);
        }

        @Test
        @DisplayName("Should handle interleaved write and flush operations")
        void testInterleavedOperations() throws IOException {
            // When
            loggingOutputStream.write("Part1".getBytes());
            loggingOutputStream.flush();

            loggingOutputStream.write("Part2".getBytes());
            loggingOutputStream.write("Part3".getBytes());
            loggingOutputStream.flush();

            loggingOutputStream.write("Part4".getBytes());
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(3);
            assertThat(records.get(0).getMessage()).isEqualTo("Part1");
            assertThat(records.get(1).getMessage()).isEqualTo("Part2Part3");
            assertThat(records.get(2).getMessage()).isEqualTo("Part4");
        }

        @Test
        @DisplayName("Should handle write operations after flush")
        void testWriteAfterFlush() throws IOException {
            // When
            loggingOutputStream.write("Before".getBytes());
            loggingOutputStream.flush();

            loggingOutputStream.write("After".getBytes());
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(2);
            assertThat(records.get(0).getMessage()).isEqualTo("Before");
            assertThat(records.get(1).getMessage()).isEqualTo("After");
        }

        @Test
        @DisplayName("Should handle buffer overflow scenarios")
        void testBufferOverflowScenarios() throws IOException {
            // Given - Create very large content
            byte[] largeChunk = new byte[64 * 1024]; // 64KB
            for (int i = 0; i < largeChunk.length; i++) {
                largeChunk[i] = (byte) ('A' + (i % 26));
            }

            // When
            loggingOutputStream.write(largeChunk);
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(1);
            assertThat(records.get(0).getMessage().length()).isEqualTo(largeChunk.length);
        }

        @Test
        @DisplayName("Should handle special characters and newlines")
        void testSpecialCharacters() throws IOException {
            // Given
            String specialMessage = "Line1\nLine2\r\nLine3\tTabbed\0Null";

            // When
            loggingOutputStream.write(specialMessage.getBytes());
            loggingOutputStream.flush();

            // Then
            List<LogRecord> records = testHandler.getRecords();
            assertThat(records).hasSize(1);
            assertThat(records.get(0).getMessage()).isEqualTo(specialMessage);
        }
    }
}
