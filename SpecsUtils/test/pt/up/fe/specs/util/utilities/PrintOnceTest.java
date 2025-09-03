package pt.up.fe.specs.util.utilities;

import static org.mockito.Mockito.times;

import java.lang.reflect.Field;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Unit tests for {@link PrintOnce}.
 * 
 * Tests one-time printing functionality to prevent duplicate log messages.
 * 
 * @author Generated Tests
 */
@DisplayName("PrintOnce")
class PrintOnceTest {

    private MockedStatic<SpecsLogs> specsLogsMock;

    @BeforeEach
    void setUp() {
        specsLogsMock = Mockito.mockStatic(SpecsLogs.class);
        PrintOnce.clearCache();
    }

    @AfterEach
    void tearDown() {
        specsLogsMock.close();
        PrintOnce.clearCache();
    }

    @Nested
    @DisplayName("Basic Functionality")
    class BasicFunctionality {

        @Test
        @DisplayName("should print message on first call")
        void shouldPrintMessageOnFirstCall() {
            String message = "Test message";

            PrintOnce.info(message);

            specsLogsMock.verify(() -> SpecsLogs.info(message), times(1));
        }

        @Test
        @DisplayName("should not print same message twice")
        void shouldNotPrintSameMessageTwice() {
            String message = "Duplicate message";

            PrintOnce.info(message);
            PrintOnce.info(message);

            specsLogsMock.verify(() -> SpecsLogs.info(message), times(1));
        }

        @Test
        @DisplayName("should print different messages separately")
        void shouldPrintDifferentMessagesSeparately() {
            String message1 = "First message";
            String message2 = "Second message";

            PrintOnce.info(message1);
            PrintOnce.info(message2);

            specsLogsMock.verify(() -> SpecsLogs.info(message1), times(1));
            specsLogsMock.verify(() -> SpecsLogs.info(message2), times(1));
        }

        @Test
        @DisplayName("should handle empty messages")
        void shouldHandleEmptyMessages() {
            String emptyMessage = "";

            PrintOnce.info(emptyMessage);
            PrintOnce.info(emptyMessage);

            specsLogsMock.verify(() -> SpecsLogs.info(emptyMessage), times(1));
        }

        @Test
        @DisplayName("should handle null messages")
        void shouldHandleNullMessages() {
            PrintOnce.info(null);
            PrintOnce.info(null);

            specsLogsMock.verify(() -> SpecsLogs.info(null), times(1));
        }
    }

    @Nested
    @DisplayName("Message Deduplication")
    class MessageDeduplication {

        @Test
        @DisplayName("should deduplicate exact string matches")
        void shouldDeduplicateExactStringMatches() {
            String message = "Exact match test";

            PrintOnce.info(message);
            PrintOnce.info(message);
            PrintOnce.info(message);

            specsLogsMock.verify(() -> SpecsLogs.info(message), times(1));
        }

        @Test
        @DisplayName("should treat different strings as different messages")
        void shouldTreatDifferentStringsAsDifferentMessages() {
            PrintOnce.info("Message A");
            PrintOnce.info("Message B");
            PrintOnce.info("Message A"); // Duplicate of first
            PrintOnce.info("Message C");
            PrintOnce.info("Message B"); // Duplicate of second

            specsLogsMock.verify(() -> SpecsLogs.info("Message A"), times(1));
            specsLogsMock.verify(() -> SpecsLogs.info("Message B"), times(1));
            specsLogsMock.verify(() -> SpecsLogs.info("Message C"), times(1));
        }

        @Test
        @DisplayName("should be case sensitive")
        void shouldBeCaseSensitive() {
            PrintOnce.info("Test Message");
            PrintOnce.info("test message");
            PrintOnce.info("TEST MESSAGE");

            specsLogsMock.verify(() -> SpecsLogs.info("Test Message"), times(1));
            specsLogsMock.verify(() -> SpecsLogs.info("test message"), times(1));
            specsLogsMock.verify(() -> SpecsLogs.info("TEST MESSAGE"), times(1));
        }

        @Test
        @DisplayName("should handle whitespace differences")
        void shouldHandleWhitespaceDifferences() {
            PrintOnce.info("message");
            PrintOnce.info(" message");
            PrintOnce.info("message ");
            PrintOnce.info(" message ");

            specsLogsMock.verify(() -> SpecsLogs.info("message"), times(1));
            specsLogsMock.verify(() -> SpecsLogs.info(" message"), times(1));
            specsLogsMock.verify(() -> SpecsLogs.info("message "), times(1));
            specsLogsMock.verify(() -> SpecsLogs.info(" message "), times(1));
        }
    }

    @Nested
    @DisplayName("Memory Management")
    class MemoryManagement {

        @Test
        @DisplayName("should maintain message history across calls")
        void shouldMaintainMessageHistoryAcrossCalls() {
            String message = "Persistent message";

            PrintOnce.info(message);

            // Simulate some time passing / other operations
            PrintOnce.info("Other message");

            // Original message should still be remembered
            PrintOnce.info(message);

            specsLogsMock.verify(() -> SpecsLogs.info(message), times(1));
            specsLogsMock.verify(() -> SpecsLogs.info("Other message"), times(1));
        }

        @Test
        @DisplayName("should handle many unique messages")
        void shouldHandleManyUniqueMessages() {
            // Force clear the cache at the start to ensure complete isolation from other tests
            PrintOnce.clearCache();
            
            // Test that we can handle many different messages
            for (int i = 0; i < 1000; i++) {
                PrintOnce.info("Message " + i);
            }

            // Verify that each message was logged exactly once by checking the total number of calls
            // This is much more efficient than verifying each message individually
            specsLogsMock.verify(() -> SpecsLogs.info(org.mockito.ArgumentMatchers.anyString()), times(1000));
            
            // Also verify that all messages were tracked in the internal set
            try {
                Field field = PrintOnce.class.getDeclaredField("PRINTED_MESSAGES");
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                Set<String> printedMessages = (Set<String>) field.get(null);
                
                // Should contain exactly 1000 unique messages
                assert printedMessages.size() == 1000 : "Expected 1000 unique messages, but found " + printedMessages.size();
                
            } catch (Exception e) {
                throw new RuntimeException("Failed to verify many unique messages", e);
            }
        }

        @Test
        @DisplayName("should handle many duplicate messages efficiently")
        void shouldHandleManyDuplicateMessagesEfficiently() {
            String message = "Repeated message";

            // Call many times
            for (int i = 0; i < 1000; i++) {
                PrintOnce.info(message);
            }

            // Should only be printed once
            specsLogsMock.verify(() -> SpecsLogs.info(message), times(1));
        }
    }

    @Nested
    @DisplayName("Thread Safety")
    class ThreadSafety {

        @Test
        @DisplayName("should handle concurrent access")
        void shouldHandleConcurrentAccess() throws InterruptedException {
            String message = "Concurrent message";
            int numThreads = 10;
            Thread[] threads = new Thread[numThreads];

            // Create threads that all try to print the same message
            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 100; j++) {
                        PrintOnce.info(message);
                    }
                });
            }

            // Start all threads
            for (Thread thread : threads) {
                thread.start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Verify that the message was processed correctly under concurrent access
            try {
                Field field = PrintOnce.class.getDeclaredField("PRINTED_MESSAGES");
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                Set<String> printedMessages = (Set<String>) field.get(null);

                // Verify the message is tracked in the internal set
                assert printedMessages.contains(message) : "Message should be in printed set";

                // Verify that only one entry was added despite 1000 concurrent calls
                assert printedMessages.size() == 1 : "Expected 1 message in set, but found " + printedMessages.size();

            } catch (Exception e) {
                throw new RuntimeException("Failed to verify printed messages", e);
            }
        }

        @Test
        @DisplayName("should handle concurrent access with different messages")
        void shouldHandleConcurrentAccessWithDifferentMessages() throws InterruptedException {
            int numThreads = 5;
            Thread[] threads = new Thread[numThreads];

            // Each thread prints its own unique message multiple times
            for (int i = 0; i < numThreads; i++) {
                final int threadId = i;
                threads[i] = new Thread(() -> {
                    String message = "Thread " + threadId + " message";
                    for (int j = 0; j < 10; j++) {
                        PrintOnce.info(message);
                    }
                });
            }

            // Start all threads
            for (Thread thread : threads) {
                thread.start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Verify that each thread's unique message was processed correctly
            try {
                Field field = PrintOnce.class.getDeclaredField("PRINTED_MESSAGES");
                field.setAccessible(true);
                @SuppressWarnings("unchecked")
                Set<String> printedMessages = (Set<String>) field.get(null);

                // We expect exactly numThreads unique messages (one per thread)
                assert printedMessages.size() == numThreads
                        : "Expected " + numThreads + " unique messages, but found " + printedMessages.size();

                // Verify each expected message is in the set
                for (int i = 0; i < numThreads; i++) {
                    String expectedMessage = "Thread " + i + " message";
                    assert printedMessages.contains(expectedMessage) : "Missing expected message: " + expectedMessage;
                }

            } catch (Exception e) {
                throw new RuntimeException("Failed to verify concurrent access with different messages", e);
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle very long messages")
        void shouldHandleVeryLongMessages() {
            String longMessage = "a".repeat(10000);

            PrintOnce.info(longMessage);
            PrintOnce.info(longMessage);

            specsLogsMock.verify(() -> SpecsLogs.info(longMessage), times(1));
        }

        @Test
        @DisplayName("should handle special characters")
        void shouldHandleSpecialCharacters() {
            String specialMessage = "Message with special chars: \n\t\r\\\"'{}[]()@#$%^&*";

            PrintOnce.info(specialMessage);
            PrintOnce.info(specialMessage);

            specsLogsMock.verify(() -> SpecsLogs.info(specialMessage), times(1));
        }

        @Test
        @DisplayName("should handle unicode characters")
        void shouldHandleUnicodeCharacters() {
            String unicodeMessage = "Unicode: 🚀 🎉 αβγ δεζ 中文 العربية";

            PrintOnce.info(unicodeMessage);
            PrintOnce.info(unicodeMessage);

            specsLogsMock.verify(() -> SpecsLogs.info(unicodeMessage), times(1));
        }

        @Test
        @DisplayName("should handle numeric strings")
        void shouldHandleNumericStrings() {
            PrintOnce.info("123");
            PrintOnce.info("123.456");
            PrintOnce.info("123"); // Duplicate

            specsLogsMock.verify(() -> SpecsLogs.info("123"), times(1));
            specsLogsMock.verify(() -> SpecsLogs.info("123.456"), times(1));
        }
    }
}
