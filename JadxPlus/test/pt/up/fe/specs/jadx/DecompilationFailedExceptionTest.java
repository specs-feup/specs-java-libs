package pt.up.fe.specs.jadx;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for {@link DecompilationFailedException}.
 * 
 * This test class validates the custom exception behavior, inheritance, and
 * serialization characteristics following modern Java testing practices.
 * 
 * @author Generated Tests
 */
@DisplayName("DecompilationFailedException Tests")
class DecompilationFailedExceptionTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor with message and cause should set both correctly")
        void testConstructor_WithMessageAndCause_SetsBothCorrectly() {
            // Arrange
            String expectedMessage = "Decompilation failed due to invalid APK structure";
            IllegalArgumentException expectedCause = new IllegalArgumentException("Invalid format");

            // Act
            DecompilationFailedException exception = new DecompilationFailedException(expectedMessage, expectedCause);

            // Assert
            assertThat(exception.getMessage()).isEqualTo(expectedMessage);
            assertThat(exception.getCause()).isEqualTo(expectedCause);
        }

        @Test
        @DisplayName("Constructor with null message should accept null")
        void testConstructor_WithNullMessage_AcceptsNull() {
            // Arrange
            RuntimeException cause = new RuntimeException("Test cause");

            // Act
            DecompilationFailedException exception = new DecompilationFailedException(null, cause);

            // Assert
            assertThat(exception.getMessage()).isNull();
            assertThat(exception.getCause()).isEqualTo(cause);
        }

        @Test
        @DisplayName("Constructor with null cause should accept null")
        void testConstructor_WithNullCause_AcceptsNull() {
            // Arrange
            String message = "Test message";

            // Act
            DecompilationFailedException exception = new DecompilationFailedException(message, null);

            // Assert
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isNull();
        }

        @Test
        @DisplayName("Constructor with both null values should accept both nulls")
        void testConstructor_WithBothNullValues_AcceptsBothNulls() {
            // Act
            DecompilationFailedException exception = new DecompilationFailedException(null, null);

            // Assert
            assertThat(exception.getMessage()).isNull();
            assertThat(exception.getCause()).isNull();
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {

        @Test
        @DisplayName("Exception should be an instance of Exception")
        void testException_ShouldBeInstanceOfException() {
            // Arrange & Act
            DecompilationFailedException exception = new DecompilationFailedException("test", null);

            // Assert
            assertThat(exception).isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Exception should be throwable")
        void testException_ShouldBeThrowable() {
            // Arrange
            String message = "Decompilation test failure";
            RuntimeException cause = new RuntimeException("Original cause");

            // Act - Create the exception directly
            DecompilationFailedException exception = new DecompilationFailedException(message, cause);

            // Assert
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isEqualTo(cause);

            // Verify it can be thrown and caught
            try {
                throw exception;
            } catch (DecompilationFailedException e) {
                assertThat(e).isEqualTo(exception);
            }
        }

        @Test
        @DisplayName("Exception should maintain stack trace")
        void testException_ShouldMaintainStackTrace() {
            // Arrange & Act
            DecompilationFailedException exception = new DecompilationFailedException("test", null);

            // Assert
            assertThat(exception.getStackTrace()).isNotEmpty();
            assertThat(exception.getStackTrace()[0].getMethodName())
                    .isEqualTo("testException_ShouldMaintainStackTrace");
        }
    }

    @Nested
    @DisplayName("Serialization Tests")
    class SerializationTests {

        @Test
        @DisplayName("Exception should have serialVersionUID defined")
        void testException_ShouldHaveSerialVersionUID() {
            // This test ensures the serialVersionUID is properly defined
            // by checking the class field exists and has the expected value
            try {
                java.lang.reflect.Field field = DecompilationFailedException.class.getDeclaredField("serialVersionUID");
                assertThat(field.getType()).isEqualTo(long.class);
                assertThat(java.lang.reflect.Modifier.isStatic(field.getModifiers())).isTrue();
                assertThat(java.lang.reflect.Modifier.isFinal(field.getModifiers())).isTrue();
            } catch (NoSuchFieldException e) {
                throw new AssertionError("serialVersionUID field not found", e);
            }
        }

        @Test
        @DisplayName("Exception should be serializable")
        void testException_ShouldBeSerializable() {
            // Arrange
            String message = "Serialization test message";
            RuntimeException cause = new RuntimeException("Serialization cause");
            DecompilationFailedException original = new DecompilationFailedException(message, cause);

            // Act & Assert - Exception should implement Serializable through Exception
            // inheritance
            assertThat(original).isInstanceOf(java.io.Serializable.class);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Exception with empty message should handle empty string")
        void testException_WithEmptyMessage_HandlesEmptyString() {
            // Arrange
            String emptyMessage = "";
            RuntimeException cause = new RuntimeException("Test cause");

            // Act
            DecompilationFailedException exception = new DecompilationFailedException(emptyMessage, cause);

            // Assert
            assertThat(exception.getMessage()).isEmpty();
            assertThat(exception.getCause()).isEqualTo(cause);
        }

        @Test
        @DisplayName("Exception with very long message should handle large strings")
        void testException_WithVeryLongMessage_HandlesLargeStrings() {
            // Arrange
            String longMessage = "A".repeat(10000); // 10KB message
            RuntimeException cause = new RuntimeException("Test cause");

            // Act
            DecompilationFailedException exception = new DecompilationFailedException(longMessage, cause);

            // Assert
            assertThat(exception.getMessage()).hasSize(10000);
            assertThat(exception.getCause()).isEqualTo(cause);
        }

        @Test
        @DisplayName("Exception with nested cause chain should maintain chain")
        void testException_WithNestedCauseChain_MaintainsChain() {
            // Arrange
            IllegalStateException rootCause = new IllegalStateException("Root cause");
            RuntimeException intermediateCause = new RuntimeException("Intermediate", rootCause);
            String message = "Final decompilation failure";

            // Act
            DecompilationFailedException exception = new DecompilationFailedException(message, intermediateCause);

            // Assert
            assertThat(exception.getMessage()).isEqualTo(message);
            assertThat(exception.getCause()).isEqualTo(intermediateCause);
            assertThat(exception.getCause().getCause()).isEqualTo(rootCause);
        }
    }

    @Nested
    @DisplayName("Real-World Usage Tests")
    class RealWorldUsageTests {

        @Test
        @DisplayName("Exception with typical APK decompilation error should be realistic")
        void testException_WithTypicalAPKError_ShouldBeRealistic() {
            // Arrange
            String realisticMessage = "Failed to decompile APK: Invalid DEX file structure in classes.dex";
            IllegalArgumentException realisticCause = new IllegalArgumentException(
                    "DEX file corrupted at offset 0x1234");

            // Act
            DecompilationFailedException exception = new DecompilationFailedException(realisticMessage, realisticCause);

            // Assert
            assertThat(exception.getMessage()).contains("Failed to decompile APK");
            assertThat(exception.getMessage()).contains("DEX file");
            assertThat(exception.getCause()).isInstanceOf(IllegalArgumentException.class);
            assertThat(exception.getCause().getMessage()).contains("corrupted");
        }

        @Test
        @DisplayName("Exception with Jadx library error should wrap appropriately")
        void testException_WithJadxLibraryError_ShouldWrapAppropriately() {
            // Arrange
            String jadxMessage = "Jadx decompiler encountered an internal error during code generation";
            RuntimeException jadxException = new RuntimeException("JadxRuntimeException: Code generation failed");

            // Act
            DecompilationFailedException exception = new DecompilationFailedException(jadxMessage, jadxException);

            // Assert
            assertThat(exception.getMessage()).contains("Jadx");
            assertThat(exception.getMessage()).contains("internal error");
            assertThat(exception.getCause().getMessage()).contains("JadxRuntimeException");
        }

        @Test
        @DisplayName("Exception with file system error should handle IO problems")
        void testException_WithFileSystemError_ShouldHandleIOProblems() {
            // Arrange
            String ioMessage = "Cannot write decompiled output to target directory";
            java.io.IOException ioException = new java.io.IOException("Permission denied: /output/classes/");

            // Act
            DecompilationFailedException exception = new DecompilationFailedException(ioMessage, ioException);

            // Assert
            assertThat(exception.getMessage()).contains("write decompiled output");
            assertThat(exception.getCause()).isInstanceOf(java.io.IOException.class);
            assertThat(exception.getCause().getMessage()).contains("Permission denied");
        }
    }
}
