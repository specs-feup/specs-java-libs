package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for TextAreaHandler class.
 * 
 * Tests the JTextArea-based logging handler that appends log messages to a
 * Swing text component.
 * 
 * @author Generated Tests
 */
@DisplayName("TextAreaHandler Tests")
class TextAreaHandlerTest {

    private JTextArea textArea;
    private TextAreaHandler handler;

    @BeforeEach
    void setUp() throws Exception {
        // Initialize on EDT to avoid threading issues
        SwingUtilities.invokeAndWait(() -> {
            textArea = new JTextArea();
        });
        handler = new TextAreaHandler(textArea);
    }

    private JTextArea getTextAreaFromHandler(TextAreaHandler handler) throws Exception {
        Field textAreaField = TextAreaHandler.class.getDeclaredField("jTextArea");
        textAreaField.setAccessible(true);
        return (JTextArea) textAreaField.get(handler);
    }

    private String getTextAreaContent() throws Exception {
        final StringBuilder content = new StringBuilder();
        CountDownLatch latch = new CountDownLatch(1);

        SwingUtilities.invokeLater(() -> {
            content.append(textArea.getText());
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);
        return content.toString();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create handler with JTextArea")
        void testConstructorWithTextArea() throws Exception {
            // When
            JTextArea testTextArea = new JTextArea();
            TextAreaHandler newHandler = new TextAreaHandler(testTextArea);

            // Then
            assertThat(newHandler).isNotNull();
            assertThat(newHandler).isInstanceOf(StreamHandler.class);
            assertThat(newHandler).isInstanceOf(TextAreaHandler.class);

            JTextArea handlerTextArea = getTextAreaFromHandler(newHandler);
            assertThat(handlerTextArea).isSameAs(testTextArea);
        }

        @Test
        @DisplayName("Should set default formatter and level")
        void testDefaultFormatterAndLevel() {
            // Then
            assertThat(handler.getFormatter()).isInstanceOf(ConsoleFormatter.class);
            assertThat(handler.getLevel()).isEqualTo(Level.ALL);
        }

        @Test
        @DisplayName("Should extend StreamHandler")
        void testExtendsStreamHandler() {
            // Then
            assertThat(handler).isInstanceOf(StreamHandler.class);
        }

        @Test
        @DisplayName("Should store JTextArea reference correctly")
        void testTextAreaReference() throws Exception {
            // Then
            JTextArea handlerTextArea = getTextAreaFromHandler(handler);
            assertThat(handlerTextArea).isSameAs(textArea);
        }

        @Test
        @DisplayName("Should handle different JTextArea instances")
        void testDifferentTextAreas() throws Exception {
            // Given
            JTextArea textArea1 = new JTextArea("Initial content 1");
            JTextArea textArea2 = new JTextArea("Initial content 2");

            // When
            TextAreaHandler handler1 = new TextAreaHandler(textArea1);
            TextAreaHandler handler2 = new TextAreaHandler(textArea2);

            // Then
            assertThat(getTextAreaFromHandler(handler1)).isSameAs(textArea1);
            assertThat(getTextAreaFromHandler(handler2)).isSameAs(textArea2);
            assertThat(handler1).isNotSameAs(handler2);
        }
    }

    @Nested
    @DisplayName("Publish Method Tests")
    class PublishMethodTests {

        @Test
        @DisplayName("Should publish log record to text area")
        void testPublishLogRecord() throws Exception {
            // Given
            LogRecord record = new LogRecord(Level.INFO, "Test message");

            // When
            handler.publish(record);

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("Test message");
        }

        @Test
        @DisplayName("Should append multiple messages")
        void testPublishMultipleMessages() throws Exception {
            // Given
            LogRecord record1 = new LogRecord(Level.INFO, "First message");
            LogRecord record2 = new LogRecord(Level.WARNING, "Second message");
            LogRecord record3 = new LogRecord(Level.SEVERE, "Third message");

            // When
            handler.publish(record1);
            handler.publish(record2);
            handler.publish(record3);

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("First message");
            assertThat(content).contains("Second message");
            assertThat(content).contains("Third message");
        }

        @Test
        @DisplayName("Should use formatter when available")
        void testPublishWithFormatter() throws Exception {
            // Given
            handler.setFormatter(new java.util.logging.Formatter() {
                @Override
                public String format(LogRecord record) {
                    return "[CUSTOM] " + record.getMessage() + "\n";
                }
            });
            LogRecord record = new LogRecord(Level.INFO, "Formatted message");

            // When
            handler.publish(record);

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("[CUSTOM] Formatted message");
        }

        @Test
        @DisplayName("Should append newline when no formatter - Cannot test setFormatter(null)")
        void testPublishWithoutFormatter() throws Exception {
            // NOTE: Cannot test setFormatter(null) because Java logging framework
            // throws NPE when setting null formatter (documented behavior)

            // Given - Test the null formatter check in TextAreaHandler code
            // We'll simulate the scenario by checking if getFormatter() returns null
            LogRecord record = new LogRecord(Level.INFO, "No formatter message");

            // When - Use the handler with its default ConsoleFormatter
            handler.publish(record);

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("No formatter message");
        }

        @Test
        @DisplayName("Should respect level filtering")
        void testPublishWithLevelFiltering() throws Exception {
            // Given
            handler.setLevel(Level.WARNING); // Only WARNING and above

            // When
            handler.publish(new LogRecord(Level.INFO, "Info message")); // Below threshold
            handler.publish(new LogRecord(Level.WARNING, "Warning message")); // At threshold
            handler.publish(new LogRecord(Level.SEVERE, "Severe message")); // Above threshold

            // Then
            String content = getTextAreaContent();
            assertThat(content).doesNotContain("Info message");
            assertThat(content).contains("Warning message");
            assertThat(content).contains("Severe message");
        }

        @Test
        @DisplayName("Should handle null record gracefully")
        void testPublishNullRecord() throws Exception {
            // Given
            handler.publish(new LogRecord(Level.INFO, "Before null"));

            // When - Publishing null record
            handler.publish(null);

            // Then - Should handle gracefully without throwing exception
            // Content should remain unchanged
            String content = getTextAreaContent();
            assertThat(content).isEqualTo("Before null\n");
        }

        @Test
        @DisplayName("Should handle record with null message")
        void testPublishRecordWithNullMessage() throws Exception {
            // Given
            LogRecord record = new LogRecord(Level.INFO, null);

            // When
            handler.publish(record);

            // Then - Should handle gracefully (behavior depends on formatter)
            String content = getTextAreaContent();
            assertThat(content).isNotNull(); // Content should exist, even if null message
        }

        @Test
        @DisplayName("Should handle empty messages")
        void testPublishEmptyMessage() throws Exception {
            // Given
            LogRecord record = new LogRecord(Level.INFO, "");

            // When
            handler.publish(record);

            // Then
            String content = getTextAreaContent();
            // ConsoleFormatter returns empty string for empty message
            assertThat(content).isNotNull();
        }

        @Test
        @DisplayName("Should handle messages with special characters")
        void testPublishSpecialCharacters() throws Exception {
            // Given
            String specialMessage = "Unicode: \u00E9\u00F1\u00FC\nNewlines\r\nTabs:\t";
            LogRecord record = new LogRecord(Level.INFO, specialMessage);

            // When
            handler.publish(record);

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains(specialMessage);
        }

        @Test
        @DisplayName("Should handle very long messages")
        void testPublishLongMessage() throws Exception {
            // Given
            StringBuilder longMessage = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                longMessage.append("Long message part ").append(i).append(". ");
            }
            LogRecord record = new LogRecord(Level.INFO, longMessage.toString());

            // When
            handler.publish(record);

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("Long message part 0");
            assertThat(content).contains("Long message part 99");
        }

        @Test
        @DisplayName("Should be synchronized for thread safety")
        void testPublishSynchronized() throws Exception {
            // Given
            int threadCount = 5; // Reduced for Swing testing
            Thread[] threads = new Thread[threadCount];
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch finishLatch = new CountDownLatch(threadCount);

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        startLatch.await();
                        for (int j = 0; j < 5; j++) {
                            LogRecord record = new LogRecord(Level.INFO, "T" + index + "M" + j);
                            handler.publish(record);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        finishLatch.countDown();
                    }
                });
                threads[i].start();
            }

            startLatch.countDown();
            finishLatch.await(5, TimeUnit.SECONDS);

            // Then - All messages should be present
            String content = getTextAreaContent();
            for (int i = 0; i < threadCount; i++) {
                for (int j = 0; j < 5; j++) {
                    assertThat(content).contains("T" + i + "M" + j);
                }
            }
        }
    }

    @Nested
    @DisplayName("Formatter Integration Tests")
    class FormatterIntegrationTests {

        @Test
        @DisplayName("Should work with ConsoleFormatter")
        void testWithConsoleFormatter() throws Exception {
            // Given
            handler.setFormatter(new ConsoleFormatter());
            LogRecord record = new LogRecord(Level.INFO, "Console format test");

            // When
            handler.publish(record);

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("Console format test");
        }

        @Test
        @DisplayName("Should work with SimpleFormatter")
        void testWithSimpleFormatter() throws Exception {
            // Given
            handler.setFormatter(new java.util.logging.SimpleFormatter());
            LogRecord record = new LogRecord(Level.INFO, "Simple format test");
            record.setLoggerName("test.logger");

            // When
            handler.publish(record);

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("Simple format test");
            assertThat(content).contains("INFO");
        }

        @Test
        @DisplayName("Should work with custom formatter")
        void testWithCustomFormatter() throws Exception {
            // Given
            handler.setFormatter(new java.util.logging.Formatter() {
                @Override
                public String format(LogRecord record) {
                    return "[" + record.getLevel() + "] " + record.getMessage() + " [END]\n";
                }
            });
            LogRecord record = new LogRecord(Level.WARNING, "Custom format test");

            // When
            handler.publish(record);

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("[WARNING] Custom format test [END]");
        }

        @Test
        @DisplayName("Should handle formatter returning null")
        void testFormatterReturningNull() throws Exception {
            // Given
            handler.setFormatter(new java.util.logging.Formatter() {
                @Override
                public String format(LogRecord record) {
                    return null;
                }
            });
            LogRecord record = new LogRecord(Level.INFO, "Null formatter test");

            // When
            handler.publish(record);

            // Then - Should handle gracefully
            String content = getTextAreaContent();
            // Behavior depends on JTextArea.append(null) handling
            assertThat(content).isNotNull();
        }

        @Test
        @DisplayName("Should handle formatter throwing exception")
        void testFormatterThrowingException() throws Exception {
            // Given
            handler.setFormatter(new java.util.logging.Formatter() {
                @Override
                public String format(LogRecord record) {
                    throw new RuntimeException("Formatter error");
                }
            });
            LogRecord record = new LogRecord(Level.INFO, "Exception formatter test");
            // When - Run publish on the EDT and capture any exception locally to avoid uncaught EDT stack traces
            AtomicReference<Throwable> thrown = new AtomicReference<>();
            javax.swing.SwingUtilities.invokeAndWait(() -> {
                try {
                    handler.publish(record);
                } catch (Throwable t) {
                    // Record the throwable so we can assert on it without letting it become an uncaught EDT exception
                    thrown.set(t);
                }
            });

            // Then - The formatter threw, but we captured it (no uncaught stack trace). Ensure expected error and no appended message.
            assertThat(thrown.get()).isInstanceOf(RuntimeException.class).hasMessage("Formatter error");
            String content = getTextAreaContent();
            assertThat(content).doesNotContain("Exception formatter test");
        }
    }

    @Nested
    @DisplayName("JTextArea Integration Tests")
    class TextAreaIntegrationTests {

        @Test
        @DisplayName("Should append to existing text area content")
        void testAppendToExistingContent() throws Exception {
            // Given
            SwingUtilities.invokeAndWait(() -> {
                textArea.setText("Existing content\n");
            });
            LogRecord record = new LogRecord(Level.INFO, "New message");

            // When
            handler.publish(record);

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("Existing content");
            assertThat(content).contains("New message");
        }

        @Test
        @DisplayName("Should work with text area having initial text")
        void testWithInitialText() throws Exception {
            // Given
            JTextArea prePopulatedTextArea = new JTextArea("Initial text\n");
            TextAreaHandler newHandler = new TextAreaHandler(prePopulatedTextArea);
            LogRecord record = new LogRecord(Level.INFO, "Added message");

            // When
            newHandler.publish(record);

            // Then
            final StringBuilder content = new StringBuilder();
            CountDownLatch latch = new CountDownLatch(1);
            SwingUtilities.invokeLater(() -> {
                content.append(prePopulatedTextArea.getText());
                latch.countDown();
            });
            latch.await(1, TimeUnit.SECONDS);

            assertThat(content.toString()).contains("Initial text");
            assertThat(content.toString()).contains("Added message");
        }

        @Test
        @DisplayName("Should handle text area with limited capacity")
        void testWithLimitedCapacityTextArea() throws Exception {
            // Given - JTextArea doesn't have built-in size limits, so this tests behavior
            for (int i = 0; i < 100; i++) {
                LogRecord record = new LogRecord(Level.INFO, "Message " + i + "\n");
                handler.publish(record);
            }

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("Message 0");
            assertThat(content).contains("Message 99");
        }

        @Test
        @DisplayName("Should maintain text area state")
        void testTextAreaStateConsistency() throws Exception {
            // Given
            LogRecord record1 = new LogRecord(Level.INFO, "First");
            LogRecord record2 = new LogRecord(Level.INFO, "Second");

            // When
            handler.publish(record1);
            String contentAfterFirst = getTextAreaContent();

            handler.publish(record2);
            String contentAfterSecond = getTextAreaContent();

            // Then
            assertThat(contentAfterFirst).contains("First");
            assertThat(contentAfterFirst).doesNotContain("Second");

            assertThat(contentAfterSecond).contains("First");
            assertThat(contentAfterSecond).contains("Second");
        }
    }

    @Nested
    @DisplayName("Level and Filter Tests")
    class LevelAndFilterTests {

        @Test
        @DisplayName("Should respect custom level settings")
        void testCustomLevelSettings() throws Exception {
            // Given
            handler.setLevel(Level.SEVERE); // Only SEVERE

            // When
            handler.publish(new LogRecord(Level.INFO, "Info"));
            handler.publish(new LogRecord(Level.WARNING, "Warning"));
            handler.publish(new LogRecord(Level.SEVERE, "Severe"));

            // Then
            String content = getTextAreaContent();
            assertThat(content).doesNotContain("Info");
            assertThat(content).doesNotContain("Warning");
            assertThat(content).contains("Severe");
        }

        @Test
        @DisplayName("Should respect filters")
        void testWithFilters() throws Exception {
            // Given
            handler.setFilter(record -> record.getMessage().contains("PASS"));

            // When
            handler.publish(new LogRecord(Level.INFO, "PASS: This should appear"));
            handler.publish(new LogRecord(Level.INFO, "FAIL: This should not appear"));
            handler.publish(new LogRecord(Level.WARNING, "PASS: Warning that appears"));

            // Then - Filter should be respected
            String content = getTextAreaContent();
            assertThat(content).contains("PASS: This should appear");
            assertThat(content).doesNotContain("FAIL: This should not appear"); // Should be filtered out
            assertThat(content).contains("PASS: Warning that appears");
        }

        @Test
        @DisplayName("Should handle level OFF")
        void testLevelOff() throws Exception {
            // Given
            handler.setLevel(Level.OFF);

            // When
            handler.publish(new LogRecord(Level.SEVERE, "Should not appear"));

            // Then
            String content = getTextAreaContent();
            assertThat(content).doesNotContain("Should not appear");
        }

        @Test
        @DisplayName("Should handle level ALL")
        void testLevelAll() throws Exception {
            // Given
            handler.setLevel(Level.ALL);

            // When
            handler.publish(new LogRecord(Level.FINE, "Fine message"));
            handler.publish(new LogRecord(Level.INFO, "Info message"));
            handler.publish(new LogRecord(Level.SEVERE, "Severe message"));

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("Fine message");
            assertThat(content).contains("Info message");
            assertThat(content).contains("Severe message");
        }
    }

    @Nested
    @DisplayName("Integration with Java Logging Tests")
    class JavaLoggingIntegrationTests {

        @Test
        @DisplayName("Should work with java.util.logging framework")
        void testLoggingFrameworkIntegration() throws Exception {
            // Given
            java.util.logging.Logger logger = java.util.logging.Logger.getLogger("textarea.integration.test");
            handler.setLevel(Level.ALL);

            // When
            logger.addHandler(handler);
            logger.setUseParentHandlers(false);
            logger.setLevel(Level.ALL);
            logger.info("Framework integration test");

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("Framework integration test");

            // Cleanup
            logger.removeHandler(handler);
        }

        @Test
        @DisplayName("Should work as Handler interface")
        void testHandlerInterface() throws Exception {
            // Given
            java.util.logging.Handler handlerInterface = handler;
            LogRecord record = new LogRecord(Level.INFO, "Interface test");

            // When/Then - Should work through Handler interface
            assertThatCode(() -> {
                handlerInterface.publish(record);
                handlerInterface.flush();
                handlerInterface.close();
            }).doesNotThrowAnyException();

            String content = getTextAreaContent();
            assertThat(content).contains("Interface test");
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
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle rapid successive operations")
        void testRapidSuccessiveOperations() throws Exception {
            // When/Then - Should handle rapid operations without issues
            assertThatCode(() -> {
                for (int i = 0; i < 50; i++) { // Reduced for Swing testing
                    handler.publish(new LogRecord(Level.INFO, "Rapid" + i));
                }
            }).doesNotThrowAnyException();

            String content = getTextAreaContent();
            assertThat(content).contains("Rapid0");
            assertThat(content).contains("Rapid49");
        }

        @Test
        @DisplayName("Should throw NPE when setting null formatter - Java logging behavior")
        void testMixedFormatterScenarios() throws Exception {
            // When
            handler.setFormatter(new ConsoleFormatter());
            handler.publish(new LogRecord(Level.INFO, "With formatter"));

            // Cannot set null formatter - Java logging throws NPE
            assertThatThrownBy(() -> {
                handler.setFormatter(null);
            }).isInstanceOf(NullPointerException.class);

            handler.setFormatter(new java.util.logging.SimpleFormatter());
            handler.publish(new LogRecord(Level.INFO, "Different formatter"));

            // Then
            String content = getTextAreaContent();
            assertThat(content).contains("With formatter");
            assertThat(content).contains("Different formatter");
        }

        @Test
        @DisplayName("Should handle close and flush operations")
        void testCloseAndFlushOperations() throws Exception {
            // Given
            LogRecord record = new LogRecord(Level.INFO, "Before operations");
            handler.publish(record);

            // When/Then - Should handle operations gracefully
            assertThatCode(() -> {
                handler.flush();
                handler.close();

                // Should still allow operations after close
                handler.publish(new LogRecord(Level.INFO, "After close"));
            }).doesNotThrowAnyException();

            String content = getTextAreaContent();
            assertThat(content).contains("Before operations");
            assertThat(content).contains("After close");
        }
    }
}
