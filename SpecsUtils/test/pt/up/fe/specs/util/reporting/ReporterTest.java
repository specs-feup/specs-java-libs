package pt.up.fe.specs.util.reporting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link Reporter}.
 * Tests the interface contract and default method implementations for reporting
 * functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("Reporter")
class ReporterTest {

    @Mock
    private PrintStream mockPrintStream;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("Default Method Implementation")
    class DefaultMethodImplementation {

        @Test
        @DisplayName("Should provide default emitError implementation")
        void shouldProvideDefaultEmitErrorImplementation() {
            // Given
            TestReporter reporter = new TestReporter();
            MessageType errorType = MessageType.ERROR_TYPE;
            String message = "Test error message";

            // When
            RuntimeException result = reporter.emitError(errorType, message);

            // Then
            assertThat(result).isInstanceOf(RuntimeException.class);
            assertThat(result.getMessage()).isEqualTo(message);
            assertThat(reporter.getLastMessage()).isEqualTo(message);
            assertThat(reporter.getLastMessageType()).isEqualTo(errorType);
        }

        @Test
        @DisplayName("Should validate message type category in emitError")
        void shouldValidateMessageTypeCategoryInEmitError() {
            // Given
            TestReporter reporter = new TestReporter();
            MessageType warningType = MessageType.WARNING_TYPE;

            // When/Then
            assertThatThrownBy(() -> reporter.emitError(warningType, "Invalid error type"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should provide default warn implementation")
        void shouldProvideDefaultWarnImplementation() {
            // Given
            TestReporter reporter = new TestReporter();
            String message = "Warning message";

            // When
            reporter.warn(message);

            // Then
            assertThat(reporter.getLastMessage()).isEqualTo(message);
            assertThat(reporter.getLastMessageType()).isEqualTo(MessageType.WARNING_TYPE);
        }

        @Test
        @DisplayName("Should provide default info implementation")
        void shouldProvideDefaultInfoImplementation() {
            // Given
            TestReporter reporter = new TestReporter();
            String message = "Info message";

            // When
            reporter.info(message);

            // Then
            assertThat(reporter.getLastMessage()).isEqualTo(message);
            assertThat(reporter.getLastMessageType()).isEqualTo(MessageType.INFO_TYPE);
        }

        @Test
        @DisplayName("Should provide default error implementation")
        void shouldProvideDefaultErrorImplementation() {
            // Given
            TestReporter reporter = new TestReporter();
            String message = "Error message";

            // When
            RuntimeException result = reporter.error(message);

            // Then
            assertThat(result).isInstanceOf(RuntimeException.class);
            assertThat(result.getMessage()).isEqualTo(message);
            assertThat(reporter.getLastMessage()).isEqualTo(message);
            assertThat(reporter.getLastMessageType()).isEqualTo(MessageType.ERROR_TYPE);
        }
    }

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("Should require emitMessage implementation")
        void shouldRequireEmitMessageImplementation() {
            // Given
            TestReporter reporter = new TestReporter();
            MessageType type = MessageType.INFO_TYPE;
            String message = "Test message";

            // When
            reporter.emitMessage(type, message);

            // Then
            assertThat(reporter.getLastMessage()).isEqualTo(message);
            assertThat(reporter.getLastMessageType()).isEqualTo(type);
        }

        @Test
        @DisplayName("Should require printStackTrace implementation")
        void shouldRequirePrintStackTraceImplementation() {
            // Given
            TestReporter reporter = new TestReporter();

            // When
            reporter.printStackTrace(mockPrintStream);

            // Then
            assertThat(reporter.isStackTracePrinted()).isTrue();
            assertThat(reporter.getStackTracePrintStream()).isEqualTo(mockPrintStream);
        }

        @Test
        @DisplayName("Should require getReportStream implementation")
        void shouldRequireGetReportStreamImplementation() {
            // Given
            TestReporter reporter = new TestReporter();

            // When
            PrintStream result = reporter.getReportStream();

            // Then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("Error Handling and Validation")
    class ErrorHandlingAndValidation {

        @Test
        @DisplayName("Should reject non-error types in emitError")
        void shouldRejectNonErrorTypesInEmitError() {
            // Given
            TestReporter reporter = new TestReporter();

            // When/Then
            assertThatThrownBy(() -> reporter.emitError(MessageType.WARNING_TYPE, "Warning as error"))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> reporter.emitError(MessageType.INFO_TYPE, "Info as error"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should accept error types in emitError")
        void shouldAcceptErrorTypesInEmitError() {
            // Given
            TestReporter reporter = new TestReporter();
            MessageType customError = new DefaultMessageType("CustomError", ReportCategory.ERROR);

            // When
            RuntimeException result = reporter.emitError(customError, "Custom error message");

            // Then
            assertThat(result).isNotNull();
            assertThat(reporter.getLastMessageType()).isEqualTo(customError);
        }

        @Test
        @DisplayName("Should handle null message gracefully")
        void shouldHandleNullMessageGracefully() {
            // Given
            TestReporter reporter = new TestReporter();

            // When
            reporter.warn(null);
            reporter.info(null);
            RuntimeException errorResult = reporter.error(null);

            // Then
            assertThat(reporter.getMessages()).contains((String) null);
            assertThat(errorResult.getMessage()).isNull();
        }

        @Test
        @DisplayName("Should handle empty message gracefully")
        void shouldHandleEmptyMessageGracefully() {
            // Given
            TestReporter reporter = new TestReporter();

            // When
            reporter.warn("");
            reporter.info("");
            RuntimeException errorResult = reporter.error("");

            // Then
            assertThat(reporter.getMessages()).contains("");
            assertThat(errorResult.getMessage()).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Message Type Interaction")
    class MessageTypeInteraction {

        @Test
        @DisplayName("Should work with all standard message types")
        void shouldWorkWithAllStandardMessageTypes() {
            // Given
            TestReporter reporter = new TestReporter();

            // When
            reporter.emitMessage(MessageType.INFO_TYPE, "Info");
            reporter.emitMessage(MessageType.WARNING_TYPE, "Warning");
            reporter.emitMessage(MessageType.ERROR_TYPE, "Error");

            // Then
            List<String> messages = reporter.getMessages();
            List<MessageType> types = reporter.getMessageTypes();

            assertThat(messages).containsExactly("Info", "Warning", "Error");
            assertThat(types).containsExactly(
                    MessageType.INFO_TYPE,
                    MessageType.WARNING_TYPE,
                    MessageType.ERROR_TYPE);
        }

        @Test
        @DisplayName("Should work with custom message types")
        void shouldWorkWithCustomMessageTypes() {
            // Given
            TestReporter reporter = new TestReporter();
            MessageType customInfo = new DefaultMessageType("CustomInfo", ReportCategory.INFORMATION);
            MessageType customWarning = new DefaultMessageType("CustomWarning", ReportCategory.WARNING);
            MessageType customError = new DefaultMessageType("CustomError", ReportCategory.ERROR);

            // When
            reporter.emitMessage(customInfo, "Custom info");
            reporter.emitMessage(customWarning, "Custom warning");
            reporter.emitMessage(customError, "Custom error");

            // Then
            List<MessageType> types = reporter.getMessageTypes();
            assertThat(types).containsExactly(customInfo, customWarning, customError);
        }
    }

    @Nested
    @DisplayName("PrintStream Integration")
    class PrintStreamIntegration {

        @Test
        @DisplayName("Should support different PrintStream implementations")
        void shouldSupportDifferentPrintStreamImplementations() {
            // Given
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PrintStream customStream = new PrintStream(outputStream);
            TestReporter reporter = new TestReporter(customStream);

            // When
            PrintStream result = reporter.getReportStream();
            reporter.printStackTrace(customStream);

            // Then
            assertThat(result).isEqualTo(customStream);
            assertThat(reporter.getStackTracePrintStream()).isEqualTo(customStream);
        }

        @Test
        @DisplayName("Should handle null PrintStream gracefully")
        void shouldHandleNullPrintStreamGracefully() {
            // Given
            TestReporter reporter = new TestReporter();

            // When/Then - Should not throw exception
            reporter.printStackTrace(null);
            assertThat(reporter.getStackTracePrintStream()).isNull();
        }
    }

    @Nested
    @DisplayName("Polymorphic Behavior")
    class PolymorphicBehavior {

        @Test
        @DisplayName("Should work polymorphically with different implementations")
        void shouldWorkPolymorphicallyWithDifferentImplementations() {
            // Given
            List<Reporter> reporters = List.of(
                    new TestReporter(),
                    new MockReporter(mockPrintStream),
                    new LambdaReporter());

            // When/Then
            for (Reporter reporter : reporters) {
                reporter.warn("Test warning");
                reporter.info("Test info");
                RuntimeException error = reporter.error("Test error");

                assertThat(error).isNotNull();
                assertThat(error.getMessage()).isEqualTo("Test error");
            }
        }

        @Test
        @DisplayName("Should support method references and functional interfaces")
        void shouldSupportMethodReferencesAndFunctionalInterfaces() {
            // Given
            TestReporter reporter = new TestReporter();
            List<String> messages = List.of("Warning 1", "Warning 2", "Warning 3");

            // When
            messages.forEach(reporter::warn);

            // Then
            assertThat(reporter.getMessages()).containsAll(messages);
        }
    }

    @Nested
    @DisplayName("Thread Safety")
    class ThreadSafety {

        @Test
        @DisplayName("Should handle concurrent access to default methods")
        void shouldHandleConcurrentAccessToDefaultMethods() throws InterruptedException {
            // Given
            TestReporter reporter = new TestReporter();
            final int numThreads = 10;
            Thread[] threads = new Thread[numThreads];

            // When
            for (int i = 0; i < numThreads; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    reporter.warn("Warning " + index);
                    reporter.info("Info " + index);
                    reporter.error("Error " + index);
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            assertThat(reporter.getMessages()).hasSize(numThreads * 3);
        }

        @RepeatedTest(50)
        @DisplayName("Stress test concurrent access to default methods")
        void stressTestConcurrentAccessToDefaultMethods() throws InterruptedException {
            // Run the concurrency scenario multiple times to expose flakiness
            TestReporter reporter = new TestReporter();
            final int numThreads = 20;
            Thread[] threads = new Thread[numThreads];

            for (int i = 0; i < numThreads; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    reporter.warn("Warning " + index);
                    reporter.info("Info " + index);
                    reporter.error("Error " + index);
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Expect exactly numThreads * 3 messages
            assertThat(reporter.getMessages()).hasSize(numThreads * 3);
        }
    }

    // Test implementation of Reporter interface
    private static class TestReporter implements Reporter {
        private final List<MessageType> messageTypes = Collections.synchronizedList(new ArrayList<>());
        private final List<String> messages = Collections.synchronizedList(new ArrayList<>());
        private final PrintStream reportStream;
        private boolean stackTracePrinted = false;
        private PrintStream stackTracePrintStream;

        public TestReporter() {
            this(System.out);
        }

        public TestReporter(PrintStream reportStream) {
            this.reportStream = reportStream;
        }

        @Override
        public void emitMessage(MessageType type, String message) {
            messageTypes.add(type);
            messages.add(message);
        }

        @Override
        public void printStackTrace(PrintStream reportStream) {
            this.stackTracePrinted = true;
            this.stackTracePrintStream = reportStream;
        }

        @Override
        public PrintStream getReportStream() {
            return reportStream;
        }

        // Test helper methods
        public MessageType getLastMessageType() {
            return messageTypes.isEmpty() ? null : messageTypes.get(messageTypes.size() - 1);
        }

        public String getLastMessage() {
            return messages.isEmpty() ? null : messages.get(messages.size() - 1);
        }

        public List<MessageType> getMessageTypes() {
            return new ArrayList<>(messageTypes);
        }

        public List<String> getMessages() {
            return new ArrayList<>(messages);
        }

        public boolean isStackTracePrinted() {
            return stackTracePrinted;
        }

        public PrintStream getStackTracePrintStream() {
            return stackTracePrintStream;
        }
    }

    // Mock implementation using mockito
    private static class MockReporter implements Reporter {
        private final PrintStream reportStream;

        public MockReporter(PrintStream reportStream) {
            this.reportStream = reportStream;
        }

        @Override
        public void emitMessage(MessageType type, String message) {
            // Mock implementation - does nothing
        }

        @Override
        public void printStackTrace(PrintStream reportStream) {
            // Mock implementation - does nothing
        }

        @Override
        public PrintStream getReportStream() {
            return reportStream;
        }
    }

    // Lambda-based implementation
    private static class LambdaReporter implements Reporter {
        @Override
        public void emitMessage(MessageType type, String message) {
            // Lambda implementation - does nothing
        }

        @Override
        public void printStackTrace(PrintStream reportStream) {
            // Lambda implementation - does nothing
        }

        @Override
        public PrintStream getReportStream() {
            return System.out;
        }
    }
}
