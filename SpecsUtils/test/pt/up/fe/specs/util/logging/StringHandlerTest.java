package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for StringHandler class.
 * 
 * Tests the string-based logging handler that captures log messages in a
 * StringBuilder buffer.
 * 
 * @author Generated Tests
 */
@DisplayName("StringHandler Tests")
class StringHandlerTest {

    private StringHandler handler;

    @BeforeEach
    void setUp() {
        handler = new StringHandler();
    }

    private StringBuilder getBufferFromHandler(StringHandler handler) throws Exception {
        Field bufferField = StringHandler.class.getDeclaredField("buffer");
        bufferField.setAccessible(true);
        return (StringBuilder) bufferField.get(handler);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create handler with empty buffer")
        void testConstructorCreatesEmptyBuffer() {
            // When
            StringHandler newHandler = new StringHandler();

            // Then
            assertThat(newHandler).isNotNull();
            assertThat(newHandler.getString()).isEmpty();
        }

        @Test
        @DisplayName("Should extend StreamHandler")
        void testExtendsStreamHandler() {
            // Then
            assertThat(handler).isInstanceOf(StreamHandler.class);
            assertThat(handler).isInstanceOf(StringHandler.class);
        }

        @Test
        @DisplayName("Should create different instances")
        void testMultipleInstances() {
            // When
            StringHandler handler1 = new StringHandler();
            StringHandler handler2 = new StringHandler();

            // Then
            assertThat(handler1).isNotSameAs(handler2);
            assertThat(handler1.getString()).isEmpty();
            assertThat(handler2.getString()).isEmpty();
        }

        @Test
        @DisplayName("Should initialize buffer properly")
        void testBufferInitialization() throws Exception {
            // When
            StringBuilder buffer = getBufferFromHandler(handler);

            // Then
            assertThat(buffer).isNotNull();
            assertThat(buffer.length()).isZero();
        }
    }

    @Nested
    @DisplayName("getString Method Tests")
    class GetStringMethodTests {

        @Test
        @DisplayName("Should return empty string initially")
        void testGetStringEmpty() {
            // Then
            assertThat(handler.getString()).isEmpty();
        }

        @Test
        @DisplayName("Should return buffer contents")
        void testGetStringWithContent() throws Exception {
            // Given
            StringBuilder buffer = getBufferFromHandler(handler);
            buffer.append("Test content");

            // Then
            assertThat(handler.getString()).isEqualTo("Test content");
        }

        @Test
        @DisplayName("Should return accumulated content")
        void testGetStringAccumulated() throws Exception {
            // Given
            StringBuilder buffer = getBufferFromHandler(handler);
            buffer.append("First ");
            buffer.append("Second ");
            buffer.append("Third");

            // Then
            assertThat(handler.getString()).isEqualTo("First Second Third");
        }

        @Test
        @DisplayName("Should return live view of buffer")
        void testGetStringLiveView() throws Exception {
            // Given
            StringBuilder buffer = getBufferFromHandler(handler);

            // When/Then
            assertThat(handler.getString()).isEmpty();

            buffer.append("Updated");
            assertThat(handler.getString()).isEqualTo("Updated");

            buffer.append(" Content");
            assertThat(handler.getString()).isEqualTo("Updated Content");
        }

        @Test
        @DisplayName("Should handle special characters")
        void testGetStringSpecialCharacters() throws Exception {
            // Given
            StringBuilder buffer = getBufferFromHandler(handler);
            String specialContent = "Unicode: \u00E9\u00F1\u00FC\nNewlines\r\nTabs:\t";
            buffer.append(specialContent);

            // Then
            assertThat(handler.getString()).isEqualTo(specialContent);
        }

        @Test
        @DisplayName("Should handle very long content")
        void testGetStringLongContent() throws Exception {
            // Given
            StringBuilder buffer = getBufferFromHandler(handler);
            StringBuilder longContent = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                longContent.append("LongContent").append(i).append(" ");
            }
            buffer.append(longContent);

            // Then
            String result = handler.getString();
            assertThat(result).contains("LongContent0");
            assertThat(result).contains("LongContent9999");
            assertThat(result.length()).isGreaterThan(100000);
        }
    }

    @Nested
    @DisplayName("Publish Method Tests")
    class PublishMethodTests {

        @Test
        @DisplayName("Should publish log record message")
        void testPublishLogRecord() {
            // Given
            LogRecord record = new LogRecord(Level.INFO, "Test message");

            // When
            handler.publish(record);

            // Then
            assertThat(handler.getString()).isEqualTo("Test message");
        }

        @Test
        @DisplayName("Should append multiple messages")
        void testPublishMultipleMessages() {
            // Given
            LogRecord record1 = new LogRecord(Level.INFO, "First");
            LogRecord record2 = new LogRecord(Level.WARNING, "Second");
            LogRecord record3 = new LogRecord(Level.SEVERE, "Third");

            // When
            handler.publish(record1);
            handler.publish(record2);
            handler.publish(record3);

            // Then
            assertThat(handler.getString()).isEqualTo("FirstSecondThird");
        }

        @Test
        @DisplayName("Should throw NPE for null record - Bug discovered")
        void testPublishNullRecord() {
            // Given
            handler.publish(new LogRecord(Level.INFO, "Before null"));

            // When/Then - Should throw NPE for null record (bug behavior)
            assertThatThrownBy(() -> {
                handler.publish(null);
            }).isInstanceOf(NullPointerException.class);

            // Content should remain unchanged
            assertThat(handler.getString()).isEqualTo("Before null");
        }

        @Test
        @DisplayName("Should handle record with null message")
        void testPublishRecordWithNullMessage() {
            // Given
            LogRecord record = new LogRecord(Level.INFO, null);

            // When/Then - Should handle null message
            assertThatCode(() -> {
                handler.publish(record);
            }).doesNotThrowAnyException();

            // Result depends on how LogRecord handles null message
            String result = handler.getString();
            // Could be "null" string or empty, depending on LogRecord behavior
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should handle empty messages")
        void testPublishEmptyMessage() {
            // Given
            LogRecord record = new LogRecord(Level.INFO, "");

            // When
            handler.publish(record);

            // Then
            assertThat(handler.getString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle messages with special characters")
        void testPublishSpecialCharacters() {
            // Given
            String specialMessage = "Unicode: \u00E9\u00F1\u00FC\nNewlines\r\nTabs:\t";
            LogRecord record = new LogRecord(Level.INFO, specialMessage);

            // When
            handler.publish(record);

            // Then
            assertThat(handler.getString()).isEqualTo(specialMessage);
        }

        @Test
        @DisplayName("Should handle very long messages")
        void testPublishLongMessage() {
            // Given
            StringBuilder longMessage = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longMessage.append("LongMessage").append(i).append(" ");
            }
            LogRecord record = new LogRecord(Level.INFO, longMessage.toString());

            // When
            handler.publish(record);

            // Then
            String result = handler.getString();
            assertThat(result).contains("LongMessage0");
            assertThat(result).contains("LongMessage999");
        }

        @Test
        @DisplayName("Should preserve log level in behavior")
        void testPublishDifferentLevels() {
            // Given
            handler.setLevel(Level.ALL); // Allow all levels
            Level[] levels = { Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG, Level.FINE };

            // When
            for (int i = 0; i < levels.length; i++) {
                LogRecord record = new LogRecord(levels[i], "Level" + i);
                handler.publish(record);
            }

            // Then
            assertThat(handler.getString()).isEqualTo("Level0Level1Level2Level3Level4");
        }

        @Test
        @DisplayName("Should be synchronized for thread safety")
        void testPublishSynchronized() throws InterruptedException {
            // Given
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 10; j++) {
                        LogRecord record = new LogRecord(Level.INFO, "T" + index + "M" + j);
                        handler.publish(record);
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then - All messages should be present (order may vary)
            String result = handler.getString();
            for (int i = 0; i < threadCount; i++) {
                for (int j = 0; j < 10; j++) {
                    assertThat(result).contains("T" + i + "M" + j);
                }
            }
            assertThat(result.length()).isEqualTo(threadCount * 10 * 4); // Each message is 4 chars
        }
    }

    @Nested
    @DisplayName("Close Method Tests")
    class CloseMethodTests {

        @Test
        @DisplayName("Should close without throwing exceptions")
        void testCloseHandler() {
            // When/Then - Should close without issues
            assertThatCode(() -> {
                handler.close();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle multiple close calls")
        void testMultipleCloseCalls() {
            // When/Then - Multiple close calls should not cause issues
            assertThatCode(() -> {
                handler.close();
                handler.close();
                handler.close();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should preserve content after close")
        void testContentPreservedAfterClose() {
            // Given
            LogRecord record = new LogRecord(Level.INFO, "Before close");
            handler.publish(record);

            // When
            handler.close();

            // Then
            assertThat(handler.getString()).isEqualTo("Before close");
        }

        @Test
        @DisplayName("Should allow operations after close")
        void testOperationsAfterClose() {
            // Given
            handler.publish(new LogRecord(Level.INFO, "Before close"));
            handler.close();

            // When/Then - Should allow getString after close
            assertThatCode(() -> {
                String content = handler.getString();
                assertThat(content).isEqualTo("Before close");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should allow publish after close")
        void testPublishAfterClose() {
            // Given
            handler.publish(new LogRecord(Level.INFO, "Before close"));
            handler.close();

            // When/Then - Should allow publish after close
            assertThatCode(() -> {
                handler.publish(new LogRecord(Level.INFO, "After close"));
                assertThat(handler.getString()).isEqualTo("Before closeAfter close");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should be synchronized for thread safety")
        void testCloseSynchronized() throws InterruptedException {
            // Given
            Thread publishThread = new Thread(() -> {
                for (int i = 0; i < 100; i++) {
                    handler.publish(new LogRecord(Level.INFO, "Msg" + i));
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

            // When
            publishThread.start();
            closeThread.start();

            publishThread.join();
            closeThread.join();

            // Then - Should complete without issues
            assertThat(handler.getString()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Buffer Management Tests")
    class BufferManagementTests {

        @Test
        @DisplayName("Should maintain buffer state consistently")
        void testBufferStateConsistency() throws Exception {
            // Given
            StringBuilder buffer = getBufferFromHandler(handler);

            // When
            handler.publish(new LogRecord(Level.INFO, "Test1"));
            assertThat(buffer.toString()).isEqualTo("Test1");
            assertThat(handler.getString()).isEqualTo("Test1");

            handler.publish(new LogRecord(Level.INFO, "Test2"));
            assertThat(buffer.toString()).isEqualTo("Test1Test2");
            assertThat(handler.getString()).isEqualTo("Test1Test2");
        }

        @Test
        @DisplayName("Should handle buffer growth efficiently")
        void testBufferGrowth() throws Exception {
            // Given
            StringBuilder buffer = getBufferFromHandler(handler);

            // When - Add many messages to test buffer expansion
            for (int i = 0; i < 1000; i++) {
                handler.publish(new LogRecord(Level.INFO, "Message" + i + " "));
            }

            // Then
            assertThat(buffer.length()).isGreaterThan(10000);
            assertThat(handler.getString()).contains("Message0");
            assertThat(handler.getString()).contains("Message999");
        }

        @Test
        @DisplayName("Should maintain buffer independence between instances")
        void testBufferIndependence() {
            // Given
            StringHandler handler1 = new StringHandler();
            StringHandler handler2 = new StringHandler();

            // When
            handler1.publish(new LogRecord(Level.INFO, "Handler1"));
            handler2.publish(new LogRecord(Level.INFO, "Handler2"));

            // Then
            assertThat(handler1.getString()).isEqualTo("Handler1");
            assertThat(handler2.getString()).isEqualTo("Handler2");
            assertThat(handler1.getString()).isNotEqualTo(handler2.getString());
        }
    }

    @Nested
    @DisplayName("Integration with Java Logging Tests")
    class JavaLoggingIntegrationTests {

        @Test
        @DisplayName("Should work with java.util.logging framework")
        void testLoggingFrameworkIntegration() {
            // Given
            java.util.logging.Logger logger = java.util.logging.Logger.getLogger("string.integration.test");
            handler.setLevel(Level.ALL);

            // When
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
            logger.info("Integration test message");

            // Then
            assertThat(handler.getString()).contains("Integration test message");

            // Cleanup
            logger.removeHandler(handler);
        }

        @Test
        @DisplayName("Should extend StreamHandler properly")
        void testStreamHandlerInheritance() {
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
            java.util.logging.Handler handlerInterface = handler;
            LogRecord record = new LogRecord(Level.INFO, "Interface test");

            // When/Then - Should work through Handler interface
            assertThatCode(() -> {
                handlerInterface.publish(record);
                handlerInterface.flush();
                handlerInterface.close();
            }).doesNotThrowAnyException();

            assertThat(handler.getString()).isEqualTo("Interface test");
        }

        @Test
        @DisplayName("Should ignore formatter in publish")
        void testIgnoresFormatter() {
            // Given
            handler.setFormatter(new java.util.logging.Formatter() {
                @Override
                public String format(LogRecord record) {
                    return "[FORMATTED] " + record.getMessage() + "\n";
                }
            });
            LogRecord record = new LogRecord(Level.INFO, "Raw message");

            // When
            handler.publish(record);

            // Then - Should ignore formatter and use raw message
            assertThat(handler.getString()).isEqualTo("Raw message");
            assertThat(handler.getString()).doesNotContain("[FORMATTED]");
        }

        @Test
        @DisplayName("Should bypass level filtering - Bug discovered")
        void testLevelFiltering() {
            // Given
            handler.setLevel(Level.WARNING); // Only WARNING and above

            // When
            handler.publish(new LogRecord(Level.INFO, "Info message")); // Below threshold
            handler.publish(new LogRecord(Level.WARNING, "Warning message")); // At threshold
            handler.publish(new LogRecord(Level.SEVERE, "Severe message")); // Above threshold

            // Then - Bug: All messages appear because level filtering is bypassed
            String result = handler.getString();
            assertThat(result).contains("Info message"); // Bug: should be filtered out but isn't
            assertThat(result).contains("Warning message");
            assertThat(result).contains("Severe message");
        }

        @Test
        @DisplayName("Should bypass filters - Bug discovered")
        void testWithFilters() {
            // Given
            handler.setFilter(record -> record.getLevel().intValue() >= Level.WARNING.intValue());

            // When
            handler.publish(new LogRecord(Level.INFO, "Filtered info"));
            handler.publish(new LogRecord(Level.WARNING, "Passed warning"));

            // Then - Bug: Filter is bypassed, all messages appear
            String result = handler.getString();
            assertThat(result).contains("Filtered info"); // Bug: should be filtered out but isn't
            assertThat(result).contains("Passed warning");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle rapid successive operations")
        void testRapidSuccessiveOperations() {
            // When/Then - Should handle rapid operations without issues
            assertThatCode(() -> {
                for (int i = 0; i < 1000; i++) {
                    handler.publish(new LogRecord(Level.INFO, "Rapid" + i));
                    if (i % 100 == 0) {
                        handler.close();
                        String content = handler.getString();
                        assertThat(content).contains("Rapid" + i);
                    }
                }
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle interleaved operations")
        void testInterleavedOperations() {
            // When/Then
            assertThatCode(() -> {
                handler.publish(new LogRecord(Level.INFO, "A"));
                handler.close();
                handler.publish(new LogRecord(Level.INFO, "B"));
                String content1 = handler.getString();
                handler.close();
                handler.publish(new LogRecord(Level.INFO, "C"));
                String content2 = handler.getString();

                assertThat(content1).isEqualTo("AB");
                assertThat(content2).isEqualTo("ABC");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle extremely large accumulated content")
        void testLargeAccumulatedContent() {
            // Given
            String baseMessage = "This is a reasonably long message that will be repeated many times. ";

            // When
            for (int i = 0; i < 10000; i++) {
                handler.publish(new LogRecord(Level.INFO, baseMessage + i + " "));
            }

            // Then
            String result = handler.getString();
            assertThat(result).contains(baseMessage + "0");
            assertThat(result).contains(baseMessage + "9999");
            assertThat(result.length()).isGreaterThan(baseMessage.length() * 10000);
        }

        @Test
        @DisplayName("Should handle mixed content types")
        void testMixedContentTypes() {
            // When
            handler.publish(new LogRecord(Level.INFO, "Text"));
            handler.publish(new LogRecord(Level.INFO, "123"));
            handler.publish(new LogRecord(Level.INFO, "\t\n"));
            handler.publish(new LogRecord(Level.INFO, "\u00E9\u00F1\u00FC"));
            handler.publish(new LogRecord(Level.INFO, ""));
            handler.publish(new LogRecord(Level.INFO, "End"));

            // Then
            String result = handler.getString();
            assertThat(result).isEqualTo("Text123\t\n\u00E9\u00F1\u00FCEnd");
        }
    }
}
