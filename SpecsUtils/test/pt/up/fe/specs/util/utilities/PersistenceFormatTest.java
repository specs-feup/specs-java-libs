package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Test class for PersistenceFormat utility.
 * 
 * Tests persistence format functionality including:
 * - Abstract class contract and method implementations
 * - File I/O operations with read/write
 * - Serialization and deserialization processes
 * - Error handling for invalid inputs
 * - File system integration and cleanup
 * - Edge cases and null handling
 * 
 * @author Generated Tests
 */
@DisplayName("PersistenceFormat Tests")
class PersistenceFormatTest {

    @TempDir
    File tempDir;

    private TestPersistenceFormat persistenceFormat;

    // Concrete implementation for testing the abstract class
    private static class TestPersistenceFormat extends PersistenceFormat {
        private boolean shouldThrowOnTo = false;
        private boolean shouldThrowOnFrom = false;
        private boolean shouldReturnNull = false;

        @Override
        public String to(Object anObject) {
            if (shouldThrowOnTo) {
                throw new RuntimeException("Test serialization error");
            }
            if (anObject == null) {
                return "null";
            }
            return anObject.toString();
        }

        @Override
        public <T> T from(String contents, Class<T> classOfObject) {
            if (shouldThrowOnFrom) {
                throw new RuntimeException("Test deserialization error");
            }
            if (shouldReturnNull) {
                return null;
            }
            if (contents == null || "null".equals(contents)) {
                return null;
            }

            // Simple string-based deserialization for testing
            if (classOfObject == String.class) {
                return classOfObject.cast(contents);
            } else if (classOfObject == Integer.class) {
                try {
                    return classOfObject.cast(Integer.valueOf(contents));
                } catch (NumberFormatException e) {
                    return null;
                }
            } else if (classOfObject == Boolean.class) {
                return classOfObject.cast(Boolean.valueOf(contents));
            }

            return null;
        }

        @Override
        public String getExtension() {
            return "test";
        }

        // Test helper methods
        public void setShouldThrowOnTo(boolean shouldThrow) {
            this.shouldThrowOnTo = shouldThrow;
        }

        public void setShouldThrowOnFrom(boolean shouldThrow) {
            this.shouldThrowOnFrom = shouldThrow;
        }
    }

    @BeforeEach
    void setUp() {
        persistenceFormat = new TestPersistenceFormat();
    }

    @Nested
    @DisplayName("Abstract Contract Tests")
    class AbstractContractTests {

        @Test
        @DisplayName("Should implement abstract methods correctly")
        void testAbstractMethods() {
            assertThat(persistenceFormat.to("test")).isEqualTo("test");
            assertThat(persistenceFormat.from("test", String.class)).isEqualTo("test");
            assertThat(persistenceFormat.getExtension()).isEqualTo("test");
        }

        @Test
        @DisplayName("Should handle different object types in to() method")
        void testToMethodWithDifferentTypes() {
            assertThat(persistenceFormat.to("hello")).isEqualTo("hello");
            assertThat(persistenceFormat.to(123)).isEqualTo("123");
            assertThat(persistenceFormat.to(true)).isEqualTo("true");
            assertThat(persistenceFormat.to(null)).isEqualTo("null");
        }

        @Test
        @DisplayName("Should handle different class types in from() method")
        void testFromMethodWithDifferentTypes() {
            assertThat(persistenceFormat.from("hello", String.class)).isEqualTo("hello");
            assertThat(persistenceFormat.from("123", Integer.class)).isEqualTo(123);
            assertThat(persistenceFormat.from("true", Boolean.class)).isEqualTo(true);
            assertThat(persistenceFormat.from("false", Boolean.class)).isEqualTo(false);
        }
    }

    @Nested
    @DisplayName("Write Operation Tests")
    class WriteOperationTests {

        @Test
        @DisplayName("Should write object to file successfully")
        void testWriteSuccessful() throws IOException {
            File outputFile = new File(tempDir, "test.txt");

            boolean result = persistenceFormat.write(outputFile, "Hello World");

            assertThat(result).isTrue();
            assertThat(outputFile).exists();
            assertThat(Files.readString(outputFile.toPath())).isEqualTo("Hello World");
        }

        @Test
        @DisplayName("Should write null object")
        void testWriteNullObject() throws IOException {
            File outputFile = new File(tempDir, "null_test.txt");

            boolean result = persistenceFormat.write(outputFile, null);

            assertThat(result).isTrue();
            assertThat(outputFile).exists();
            assertThat(Files.readString(outputFile.toPath())).isEqualTo("null");
        }

        @Test
        @DisplayName("Should handle different object types")
        void testWriteDifferentTypes() throws IOException {
            File stringFile = new File(tempDir, "string.txt");
            File intFile = new File(tempDir, "int.txt");
            File boolFile = new File(tempDir, "bool.txt");

            persistenceFormat.write(stringFile, "test string");
            persistenceFormat.write(intFile, 42);
            persistenceFormat.write(boolFile, true);

            assertThat(Files.readString(stringFile.toPath())).isEqualTo("test string");
            assertThat(Files.readString(intFile.toPath())).isEqualTo("42");
            assertThat(Files.readString(boolFile.toPath())).isEqualTo("true");
        }

        @Test
        @DisplayName("Should handle write to invalid directory")
        void testWriteToInvalidDirectory() {
            File invalidFile = new File("/invalid/path/test.txt");

            assertThatThrownBy(() -> persistenceFormat.write(invalidFile, "test"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("could not be created");
        }

        @Test
        @DisplayName("Should handle serialization errors")
        void testWriteWithSerializationError() {
            persistenceFormat.setShouldThrowOnTo(true);
            File outputFile = new File(tempDir, "error_test.txt");

            assertThatThrownBy(() -> persistenceFormat.write(outputFile, "test"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Test serialization error");
        }

        @Test
        @DisplayName("Should create parent directories when needed")
        void testWriteWithParentCreation() throws IOException {
            File nestedFile = new File(tempDir, "nested/deep/test.txt");

            boolean result = persistenceFormat.write(nestedFile, "nested content");

            assertThat(result).isTrue();
            assertThat(nestedFile).exists();
            assertThat(Files.readString(nestedFile.toPath())).isEqualTo("nested content");
        }
    }

    @Nested
    @DisplayName("Read Operation Tests")
    class ReadOperationTests {

        @Test
        @DisplayName("Should read object from file successfully")
        void testReadSuccessful() throws IOException {
            File inputFile = new File(tempDir, "input.txt");
            Files.writeString(inputFile.toPath(), "Hello World");

            String result = persistenceFormat.read(inputFile, String.class);

            assertThat(result).isEqualTo("Hello World");
        }

        @Test
        @DisplayName("Should read and convert to different types")
        void testReadWithTypeConversion() throws IOException {
            File stringFile = new File(tempDir, "string.txt");
            File intFile = new File(tempDir, "int.txt");
            File boolFile = new File(tempDir, "bool.txt");

            Files.writeString(stringFile.toPath(), "test content");
            Files.writeString(intFile.toPath(), "123");
            Files.writeString(boolFile.toPath(), "true");

            assertThat(persistenceFormat.read(stringFile, String.class)).isEqualTo("test content");
            assertThat(persistenceFormat.read(intFile, Integer.class)).isEqualTo(123);
            assertThat(persistenceFormat.read(boolFile, Boolean.class)).isEqualTo(true);
        }

        @Test
        @DisplayName("Should read null content")
        void testReadNullContent() throws IOException {
            File nullFile = new File(tempDir, "null.txt");
            Files.writeString(nullFile.toPath(), "null");

            String result = persistenceFormat.read(nullFile, String.class);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle empty file")
        void testReadEmptyFile() throws IOException {
            File emptyFile = new File(tempDir, "empty.txt");
            Files.writeString(emptyFile.toPath(), "");

            String result = persistenceFormat.read(emptyFile, String.class);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle non-existent file with logging")
        void testReadNonExistentFile() {
            File nonExistentFile = new File(tempDir, "nonexistent.txt");

            // SpecsIo logs info but returns null content, which our test implementation
            // handles
            String result = persistenceFormat.read(nonExistentFile, String.class);

            // Test implementation returns null for null content
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle deserialization errors")
        void testReadWithDeserializationError() throws IOException {
            persistenceFormat.setShouldThrowOnFrom(true);
            File inputFile = new File(tempDir, "error_test.txt");
            Files.writeString(inputFile.toPath(), "test content");

            assertThatThrownBy(() -> persistenceFormat.read(inputFile, String.class))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Test deserialization error");
        }

        @Test
        @DisplayName("Should handle invalid type conversion")
        void testReadInvalidTypeConversion() throws IOException {
            File inputFile = new File(tempDir, "invalid.txt");
            Files.writeString(inputFile.toPath(), "not_a_number");

            Integer result = persistenceFormat.read(inputFile, Integer.class);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should handle unsupported class types")
        void testReadUnsupportedClass() throws IOException {
            File inputFile = new File(tempDir, "unsupported.txt");
            Files.writeString(inputFile.toPath(), "test content");

            Object result = persistenceFormat.read(inputFile, Object.class);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Round-trip Tests")
    class RoundTripTests {

        @Test
        @DisplayName("Should preserve data through write-read cycle")
        void testRoundTripWithString() throws IOException {
            File testFile = new File(tempDir, "roundtrip.txt");
            String originalData = "Hello, World!";

            persistenceFormat.write(testFile, originalData);
            String restoredData = persistenceFormat.read(testFile, String.class);

            assertThat(restoredData).isEqualTo(originalData);
        }

        @Test
        @DisplayName("Should preserve numbers through round-trip")
        void testRoundTripWithNumbers() throws IOException {
            File testFile = new File(tempDir, "number.txt");
            Integer originalNumber = 42;

            persistenceFormat.write(testFile, originalNumber);
            Integer restoredNumber = persistenceFormat.read(testFile, Integer.class);

            assertThat(restoredNumber).isEqualTo(originalNumber);
        }

        @Test
        @DisplayName("Should preserve booleans through round-trip")
        void testRoundTripWithBooleans() throws IOException {
            File trueFile = new File(tempDir, "true.txt");
            File falseFile = new File(tempDir, "false.txt");

            persistenceFormat.write(trueFile, true);
            persistenceFormat.write(falseFile, false);

            assertThat(persistenceFormat.read(trueFile, Boolean.class)).isTrue();
            assertThat(persistenceFormat.read(falseFile, Boolean.class)).isFalse();
        }

        @Test
        @DisplayName("Should handle null through round-trip")
        void testRoundTripWithNull() throws IOException {
            File nullFile = new File(tempDir, "null.txt");

            persistenceFormat.write(nullFile, null);
            String result = persistenceFormat.read(nullFile, String.class);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle null file parameter in write with logging")
        void testWriteNullFile() {
            // SpecsIo logs warning but doesn't throw exception for null file
            boolean result = persistenceFormat.write(null, "test");

            assertThat(result).isFalse();
            // Note: This might be a bug - null file should be validated
        }

        @Test
        @DisplayName("Should handle null file parameter in read with logging")
        void testReadNullFile() {
            // SpecsIo logs info but doesn't throw exception for null file
            String result = persistenceFormat.read(null, String.class);

            // Test implementation returns null for null content
            assertThat(result).isNull();
            // Note: This might be a bug - null file should be validated
        }

        @Test
        @DisplayName("Should handle null class parameter in read gracefully")
        void testReadNullClass() throws IOException {
            File testFile = new File(tempDir, "test.txt");
            Files.writeString(testFile.toPath(), "test content");

            // Test implementation handles null class parameter without throwing
            Object result = persistenceFormat.read(testFile, null);

            assertThat(result).isNull();
            // Note: This might be a bug - null class should be validated
        }

        @Test
        @DisplayName("Should handle permission denied scenarios")
        void testPermissionDenied() {
            // Skip on systems that don't support file permissions
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                return;
            }

            File readOnlyDir = new File(tempDir, "readonly");
            readOnlyDir.mkdir();
            readOnlyDir.setWritable(false);

            File deniedFile = new File(readOnlyDir, "denied.txt");

            boolean result = persistenceFormat.write(deniedFile, "test");

            assertThat(result).isFalse();

            // Cleanup
            readOnlyDir.setWritable(true);
        }
    }

    @Nested
    @DisplayName("Extension and Metadata Tests")
    class ExtensionTests {

        @Test
        @DisplayName("Should return correct extension")
        void testGetExtension() {
            assertThat(persistenceFormat.getExtension()).isEqualTo("test");
        }

        @Test
        @DisplayName("Should handle extension in file operations")
        void testExtensionConsistency() throws IOException {
            String extension = persistenceFormat.getExtension();
            File testFile = new File(tempDir, "data." + extension);

            persistenceFormat.write(testFile, "test data");
            String result = persistenceFormat.read(testFile, String.class);

            assertThat(result).isEqualTo("test data");
            assertThat(testFile.getName()).endsWith("." + extension);
        }
    }

    @Nested
    @DisplayName("Large Data Tests")
    class LargeDataTests {

        @Test
        @DisplayName("Should handle large strings")
        void testLargeStringPersistence() throws IOException {
            StringBuilder largeString = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                largeString.append("Line ").append(i).append(" with some content\n");
            }

            File largeFile = new File(tempDir, "large.txt");
            String originalData = largeString.toString();

            boolean writeResult = persistenceFormat.write(largeFile, originalData);
            String readData = persistenceFormat.read(largeFile, String.class);

            assertThat(writeResult).isTrue();
            assertThat(readData).isEqualTo(originalData);
            assertThat(readData.length()).isGreaterThan(100000);
        }

        @Test
        @DisplayName("Should handle special characters and Unicode")
        void testSpecialCharactersPersistence() throws IOException {
            String specialChars = "Special: àáâãäåæçèéêëìíîïñòóôõöùúûüýÿ 中文 العربية 日本語 🎉🚀";
            File specialFile = new File(tempDir, "special.txt");

            persistenceFormat.write(specialFile, specialChars);
            String result = persistenceFormat.read(specialFile, String.class);

            assertThat(result).isEqualTo(specialChars);
        }
    }
}
