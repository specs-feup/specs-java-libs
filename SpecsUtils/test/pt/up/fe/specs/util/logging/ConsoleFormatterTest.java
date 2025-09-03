package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for ConsoleFormatter class.
 * 
 * Tests the custom formatter for presenting logging information on a screen.
 * 
 * @author Generated Tests
 */
@DisplayName("ConsoleFormatter Tests")
class ConsoleFormatterTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create ConsoleFormatter instance")
        void testConstructor() {
            // When
            ConsoleFormatter formatter = new ConsoleFormatter();

            // Then
            assertThat(formatter).isNotNull();
            assertThat(formatter).isInstanceOf(java.util.logging.Formatter.class);
        }

        @Test
        @DisplayName("Should create multiple independent instances")
        void testMultipleInstances() {
            // When
            ConsoleFormatter formatter1 = new ConsoleFormatter();
            ConsoleFormatter formatter2 = new ConsoleFormatter();

            // Then
            assertThat(formatter1).isNotSameAs(formatter2);
        }
    }

    @Nested
    @DisplayName("Format Method Tests")
    class FormatMethodTests {

        @Test
        @DisplayName("Should format log record with message")
        void testFormatLogRecord() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.INFO, "Test message");

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo("Test message");
        }

        @Test
        @DisplayName("Should format log record ignoring other fields")
        @SuppressWarnings("deprecation")
        void testFormatIgnoresOtherFields() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.SEVERE, "Critical error");
            record.setLoggerName("test.logger");
            record.setSourceClassName("TestClass");
            record.setSourceMethodName("testMethod");
            record.setThreadID(123);
            record.setMillis(System.currentTimeMillis());

            // When
            String result = formatter.format(record);

            // Then - Only message should be returned, ignoring all other fields
            assertThat(result).isEqualTo("Critical error");
            assertThat(result).doesNotContain("SEVERE");
            assertThat(result).doesNotContain("test.logger");
            assertThat(result).doesNotContain("TestClass");
            assertThat(result).doesNotContain("testMethod");
            assertThat(result).doesNotContain("123");
        }

        @Test
        @DisplayName("Should handle empty messages")
        void testFormatEmptyMessage() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.INFO, "");

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle null messages")
        void testFormatNullMessage() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.INFO, null);

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle whitespace-only messages")
        void testFormatWhitespaceMessage() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.INFO, "   ");

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo("   ");
        }

        @Test
        @DisplayName("Should preserve message formatting")
        void testFormatPreservesFormatting() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            String messageWithFormatting = "Line 1\nLine 2\tTabbed\r\nWindows line ending";
            LogRecord record = new LogRecord(Level.INFO, messageWithFormatting);

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo(messageWithFormatting);
        }

        @Test
        @DisplayName("Should handle messages with special characters")
        void testFormatSpecialCharacters() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            String specialMessage = "Unicode: \u00E9\u00F1\u00FC, Symbols: @#$%^&*()";
            LogRecord record = new LogRecord(Level.INFO, specialMessage);

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo(specialMessage);
        }

        @Test
        @DisplayName("Should handle very long messages")
        void testFormatVeryLongMessage() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            StringBuilder longMessage = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longMessage.append("Very long message part ").append(i).append(". ");
            }
            String message = longMessage.toString();
            LogRecord record = new LogRecord(Level.INFO, message);

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo(message);
        }
    }

    @Nested
    @DisplayName("Different Log Levels Tests")
    class LogLevelTests {

        @Test
        @DisplayName("Should format all standard log levels")
        void testAllStandardLogLevels() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            Level[] levels = {
                    Level.SEVERE, Level.WARNING, Level.INFO,
                    Level.CONFIG, Level.FINE, Level.FINER, Level.FINEST
            };

            // When/Then
            for (Level level : levels) {
                LogRecord record = new LogRecord(level, "Test message");
                String result = formatter.format(record);

                // Only message should be returned, level should not be added by formatter
                assertThat(result).isEqualTo("Test message");
            }
        }

        @Test
        @DisplayName("Should format custom log levels")
        void testCustomLogLevels() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            Level customLevel = Level.parse("850"); // Between INFO (800) and WARNING (900)
            LogRecord record = new LogRecord(customLevel, "Custom level message");

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo("Custom level message");
        }
    }

    @Nested
    @DisplayName("LogRecord Properties Tests")
    class LogRecordPropertiesTests {

        @Test
        @DisplayName("Should ignore logger name")
        void testIgnoreLoggerName() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.INFO, "Test message");
            record.setLoggerName("com.example.TestLogger");

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo("Test message");
            assertThat(result).doesNotContain("com.example.TestLogger");
        }

        @Test
        @DisplayName("Should ignore source class and method")
        void testIgnoreSourceInfo() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.INFO, "Test message");
            record.setSourceClassName("TestClass");
            record.setSourceMethodName("testMethod");

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo("Test message");
            assertThat(result).doesNotContain("TestClass");
            assertThat(result).doesNotContain("testMethod");
        }

        @Test
        @DisplayName("Should ignore timestamp")
        @SuppressWarnings("deprecation")
        void testIgnoreTimestamp() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.INFO, "Test message");
            record.setMillis(1640995200000L); // Fixed timestamp

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo("Test message");
            assertThat(result).doesNotContain("1640995200000");
        }

        @Test
        @DisplayName("Should ignore thread ID")
        @SuppressWarnings("deprecation")
        void testIgnoreThreadId() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.INFO, "Test message");
            record.setThreadID(42);

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo("Test message");
            assertThat(result).doesNotContain("42");
        }

        @Test
        @DisplayName("Should ignore parameters")
        void testIgnoreParameters() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.INFO, "Test message");
            record.setParameters(new Object[] { "param1", "param2", 123 });

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo("Test message");
            assertThat(result).doesNotContain("param1");
            assertThat(result).doesNotContain("param2");
            assertThat(result).doesNotContain("123");
        }

        @Test
        @DisplayName("Should ignore exception information")
        void testIgnoreException() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.SEVERE, "Error occurred");
            record.setThrown(new RuntimeException("Test exception"));

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo("Error occurred");
            assertThat(result).doesNotContain("RuntimeException");
            assertThat(result).doesNotContain("Test exception");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Null Handling Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null LogRecord")
        void testNullLogRecord() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();

            // When/Then - Should throw NPE for null record
            assertThatThrownBy(() -> {
                formatter.format(null);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle LogRecord with all null fields")
        void testLogRecordWithNullFields() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.INFO, null);
            record.setLoggerName(null);
            record.setSourceClassName(null);
            record.setSourceMethodName(null);
            record.setParameters(null);
            record.setThrown(null);

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle rapid successive formatting calls")
        void testRapidSuccessiveFormatting() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.INFO, "Rapid message");

            // When/Then - Should handle many rapid calls
            for (int i = 0; i < 1000; i++) {
                String result = formatter.format(record);
                assertThat(result).isEqualTo("Rapid message");
            }
        }

        @Test
        @DisplayName("Should be stateless across multiple format calls")
        void testStatelessFormatting() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();

            // When
            String result1 = formatter.format(new LogRecord(Level.INFO, "Message 1"));
            String result2 = formatter.format(new LogRecord(Level.WARNING, "Message 2"));
            String result3 = formatter.format(new LogRecord(Level.SEVERE, "Message 3"));

            // Then - Each call should be independent
            assertThat(result1).isEqualTo("Message 1");
            assertThat(result2).isEqualTo("Message 2");
            assertThat(result3).isEqualTo("Message 3");
        }
    }

    @Nested
    @DisplayName("Concurrency Tests")
    class ConcurrencyTests {

        @Test
        @DisplayName("Should handle concurrent formatting")
        void testConcurrentFormatting() throws InterruptedException {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];
            String[] results = new String[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    LogRecord record = new LogRecord(Level.INFO, "Concurrent message " + index);
                    results[index] = formatter.format(record);
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            for (int i = 0; i < threadCount; i++) {
                assertThat(results[i]).isEqualTo("Concurrent message " + i);
            }
        }

        @Test
        @DisplayName("Should handle shared formatter instance")
        void testSharedFormatterInstance() throws InterruptedException {
            // Given
            ConsoleFormatter sharedFormatter = new ConsoleFormatter();
            int operationCount = 100;
            Thread[] threads = new Thread[5];

            // When
            for (int i = 0; i < threads.length; i++) {
                final int threadIndex = i;
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < operationCount; j++) {
                        LogRecord record = new LogRecord(Level.INFO, "Thread " + threadIndex + " message " + j);
                        String result = sharedFormatter.format(record);
                        // Verify result is correct
                        if (!result.equals("Thread " + threadIndex + " message " + j)) {
                            throw new AssertionError("Unexpected result: " + result);
                        }
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then - Should complete without exceptions
            assertThat(sharedFormatter).isNotNull();
        }
    }

    @Nested
    @DisplayName("Inheritance and Interface Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend java.util.logging.Formatter")
        void testExtendsFormatter() {
            // Given
            ConsoleFormatter formatter = new ConsoleFormatter();

            // Then
            assertThat(formatter).isInstanceOf(java.util.logging.Formatter.class);
        }

        @Test
        @DisplayName("Should override format method")
        void testOverridesFormatMethod() throws NoSuchMethodException {
            // Given
            Class<ConsoleFormatter> clazz = ConsoleFormatter.class;

            // When
            java.lang.reflect.Method formatMethod = clazz.getMethod("format", LogRecord.class);

            // Then
            assertThat(formatMethod.getDeclaringClass()).isEqualTo(ConsoleFormatter.class);
            assertThat(formatMethod.getReturnType()).isEqualTo(String.class);
        }

        @Test
        @DisplayName("Should be usable as Formatter interface")
        void testFormatterInterface() {
            // Given
            java.util.logging.Formatter formatter = new ConsoleFormatter();
            LogRecord record = new LogRecord(Level.INFO, "Interface test");

            // When
            String result = formatter.format(record);

            // Then
            assertThat(result).isEqualTo("Interface test");
        }
    }
}
