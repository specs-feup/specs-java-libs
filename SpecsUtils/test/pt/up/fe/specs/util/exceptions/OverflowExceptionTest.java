package pt.up.fe.specs.util.exceptions;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link OverflowException}.
 * 
 * Tests the exception class that indicates when an overflow condition occurs
 * in numerical calculations or data structures.
 * 
 * @author Generated Tests
 */
@DisplayName("OverflowException Tests")
class OverflowExceptionTest {

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend RuntimeException")
        void testExtendsRuntimeException() {
            assertThat(RuntimeException.class).isAssignableFrom(OverflowException.class);
        }

        @Test
        @DisplayName("Should be an Exception")
        void testIsException() {
            assertThat(Exception.class).isAssignableFrom(OverflowException.class);
        }

        @Test
        @DisplayName("Should be a Throwable")
        void testIsThrowable() {
            assertThat(Throwable.class).isAssignableFrom(OverflowException.class);
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with message")
        void testConstructorWithMessage() {
            String message = "Integer overflow detected";
            OverflowException exception = new OverflowException(message);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("Should create exception with null message")
        void testConstructorWithNullMessage() {
            String nullMessage = null;
            OverflowException exception = new OverflowException(nullMessage);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isNull();
        }

        @Test
        @DisplayName("Should create exception with empty message")
        void testConstructorWithEmptyMessage() {
            String emptyMessage = "";
            OverflowException exception = new OverflowException(emptyMessage);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("");
        }

        @Test
        @DisplayName("Should create exception with whitespace message")
        void testConstructorWithWhitespaceMessage() {
            String whitespaceMessage = "   \t\n   ";
            OverflowException exception = new OverflowException(whitespaceMessage);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo(whitespaceMessage);
        }
    }

    @Nested
    @DisplayName("Message Handling Tests")
    class MessageHandlingTests {

        @Test
        @DisplayName("Should preserve exact message content")
        void testPreservesExactMessageContent() {
            String[] testMessages = {
                    "Buffer overflow at position 1024",
                    "Stack overflow: maximum depth 10000 exceeded",
                    "Heap overflow: cannot allocate 2GB",
                    "Integer overflow: result exceeds MAX_VALUE",
                    "Array overflow: index 999999 out of bounds",
                    "Counter overflow: value wrapped around",
                    "Special chars: !@#$%^&*()_+-=[]{}|;':\",./<>?"
            };

            for (String message : testMessages) {
                OverflowException exception = new OverflowException(message);
                assertThat(exception.getMessage()).isEqualTo(message);
            }
        }

        @Test
        @DisplayName("Should handle unicode characters in message")
        void testUnicodeCharactersInMessage() {
            String unicodeMessage = "Overflow: 数值溢出 - αβγ - 🚫💥⚠️";
            OverflowException exception = new OverflowException(unicodeMessage);

            assertThat(exception.getMessage()).isEqualTo(unicodeMessage);
        }

        @Test
        @DisplayName("Should handle very long messages")
        void testVeryLongMessage() {
            StringBuilder longMessage = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longMessage.append("Overflow condition ").append(i).append(" ");
            }
            String message = longMessage.toString();

            OverflowException exception = new OverflowException(message);
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getMessage().length()).isGreaterThan(10000);
        }

        @Test
        @DisplayName("Should handle newlines and special formatting")
        void testNewlinesAndFormatting() {
            String formattedMessage = "Overflow Error:\n\tLocation: Buffer.java:42\n\tSize: 1024 bytes\n\tCapacity: 512 bytes";
            OverflowException exception = new OverflowException(formattedMessage);

            assertThat(exception.getMessage()).isEqualTo(formattedMessage);
            assertThat(exception.getMessage()).contains("\n");
            assertThat(exception.getMessage()).contains("\t");
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {

        @Test
        @DisplayName("Should be throwable")
        void testThrowable() {
            String message = "Test overflow condition";

            assertThatThrownBy(() -> {
                throw new OverflowException(message);
            }).isInstanceOf(OverflowException.class)
                    .hasMessage(message);
        }

        @Test
        @DisplayName("Should preserve stack trace")
        void testStackTracePreservation() {
            try {
                throw new OverflowException("Stack trace test");
            } catch (OverflowException e) {
                assertThat(e.getStackTrace()).isNotEmpty();
                assertThat(e.getStackTrace()[0].getMethodName()).isEqualTo("testStackTracePreservation");
                assertThat(e.getStackTrace()[0].getClassName()).contains("OverflowExceptionTest");
            }
        }

        @Test
        @DisplayName("Should have no cause by default")
        void testNoCauseByDefault() {
            OverflowException exception = new OverflowException("test");
            assertThat(exception.getCause()).isNull();
        }

        @Test
        @DisplayName("Should be serializable")
        void testSerializable() {
            OverflowException exception = new OverflowException("serialization test");

            // Test that it implements Serializable (through RuntimeException)
            assertThat(exception).isInstanceOf(java.io.Serializable.class);
        }

        @Test
        @DisplayName("Should support suppressed exceptions")
        void testSuppressedExceptions() {
            OverflowException mainException = new OverflowException("Main overflow");
            OverflowException suppressedException = new OverflowException("Suppressed overflow");

            mainException.addSuppressed(suppressedException);

            assertThat(mainException.getSuppressed()).hasSize(1);
            assertThat(mainException.getSuppressed()[0]).isEqualTo(suppressedException);
        }
    }

    @Nested
    @DisplayName("Use Case Scenarios")
    class UseCaseScenarios {

        @Test
        @DisplayName("Should handle integer overflow scenario")
        void testIntegerOverflowScenario() {
            int maxValue = Integer.MAX_VALUE;

            assertThatThrownBy(() -> {
                // Simulate integer overflow check
                if (maxValue > Integer.MAX_VALUE - 1) {
                    throw new OverflowException("Integer overflow: value " + maxValue + " + 1 exceeds MAX_VALUE");
                }
            }).isInstanceOf(OverflowException.class)
                    .hasMessageContaining("Integer overflow")
                    .hasMessageContaining("MAX_VALUE");
        }

        @Test
        @DisplayName("Should handle buffer overflow scenario")
        void testBufferOverflowScenario() {
            int bufferSize = 1024;
            int dataSize = 2048;

            assertThatThrownBy(() -> {
                if (dataSize > bufferSize) {
                    throw new OverflowException(
                            "Buffer overflow: cannot fit " + dataSize + " bytes into " + bufferSize + " byte buffer");
                }
            }).isInstanceOf(OverflowException.class)
                    .hasMessageContaining("Buffer overflow")
                    .hasMessageContaining("2048")
                    .hasMessageContaining("1024");
        }

        @Test
        @DisplayName("Should handle stack overflow scenario")
        void testStackOverflowScenario() {
            int currentDepth = 10000;
            int maxDepth = 5000;

            assertThatThrownBy(() -> {
                if (currentDepth > maxDepth) {
                    throw new OverflowException(
                            "Stack overflow: recursion depth " + currentDepth + " exceeds maximum " + maxDepth);
                }
            }).isInstanceOf(OverflowException.class)
                    .hasMessageContaining("Stack overflow")
                    .hasMessageContaining("10000")
                    .hasMessageContaining("5000");
        }

        @Test
        @DisplayName("Should handle array index overflow scenario")
        void testArrayIndexOverflowScenario() {
            int index = 1000000;
            int arrayLength = 100;

            assertThatThrownBy(() -> {
                if (index >= arrayLength) {
                    throw new OverflowException("Array index overflow: index " + index
                            + " is out of bounds for array of length " + arrayLength);
                }
            }).isInstanceOf(OverflowException.class)
                    .hasMessageContaining("Array index overflow")
                    .hasMessageContaining("1000000")
                    .hasMessageContaining("100");
        }

        @Test
        @DisplayName("Should handle memory overflow scenario")
        void testMemoryOverflowScenario() {
            long requestedMemory = 8L * 1024 * 1024 * 1024; // 8GB
            long availableMemory = 2L * 1024 * 1024 * 1024; // 2GB

            assertThatThrownBy(() -> {
                if (requestedMemory > availableMemory) {
                    throw new OverflowException("Memory overflow: requested " + (requestedMemory / (1024 * 1024))
                            + "MB exceeds available " + (availableMemory / (1024 * 1024)) + "MB");
                }
            }).isInstanceOf(OverflowException.class)
                    .hasMessageContaining("Memory overflow")
                    .hasMessageContaining("8192MB")
                    .hasMessageContaining("2048MB");
        }
    }

    @Nested
    @DisplayName("Comparison with Other Exceptions")
    class ComparisonTests {

        @Test
        @DisplayName("Should be different from ArithmeticException")
        void testDifferentFromArithmeticException() {
            OverflowException overflowException = new OverflowException("overflow");
            ArithmeticException arithmeticException = new ArithmeticException("arithmetic");

            assertThat(overflowException).isNotInstanceOf(ArithmeticException.class);
            assertThat(arithmeticException).isNotInstanceOf(OverflowException.class);
        }

        @Test
        @DisplayName("Should be different from IndexOutOfBoundsException")
        void testDifferentFromIndexOutOfBoundsException() {
            OverflowException overflowException = new OverflowException("overflow");
            IndexOutOfBoundsException indexException = new IndexOutOfBoundsException("index");

            assertThat(overflowException).isNotInstanceOf(IndexOutOfBoundsException.class);
            assertThat(indexException).isNotInstanceOf(OverflowException.class);
        }

        @Test
        @DisplayName("Should have correct inheritance hierarchy")
        void testInheritanceHierarchy() {
            OverflowException exception = new OverflowException("test");

            // Check the inheritance chain
            assertThat(exception).isInstanceOf(OverflowException.class);
            assertThat(exception).isInstanceOf(RuntimeException.class);
            assertThat(exception).isInstanceOf(Exception.class);
            assertThat(exception).isInstanceOf(Throwable.class);
            assertThat(exception).isInstanceOf(Object.class);
        }
    }
}
