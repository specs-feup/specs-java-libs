package pt.up.fe.specs.JacksonPlus;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Comprehensive test suite for SpecsJackson utility class.
 * 
 * Tests JSON serialization and deserialization functionality including:
 * - File-based operations (reading from/writing to JSON files)
 * - String-based operations (parsing/generating JSON strings)
 * - Type information handling (polymorphic serialization)
 * - Error handling and edge cases
 * - Performance and memory usage
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsJackson Tests")
class SpecsJacksonTest {

    @TempDir
    Path tempDir;

    /**
     * Test data classes for JSON serialization/deserialization testing.
     */
    static class SimpleObject {
        @JsonProperty("name")
        private String name;

        @JsonProperty("value")
        private int value;

        public SimpleObject() {
        }

        @JsonCreator
        public SimpleObject(@JsonProperty("name") String name, @JsonProperty("value") int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof SimpleObject))
                return false;
            SimpleObject other = (SimpleObject) obj;
            return value == other.value &&
                    (name == null ? other.name == null : name.equals(other.name));
        }

        @Override
        public int hashCode() {
            return (name == null ? 0 : name.hashCode()) + value * 31;
        }
    }

    static class ComplexObject {
        @JsonProperty("simple")
        private SimpleObject simple;

        @JsonProperty("numbers")
        private List<Integer> numbers;

        @JsonProperty("metadata")
        private Map<String, Object> metadata;

        public ComplexObject() {
        }

        @JsonCreator
        public ComplexObject(@JsonProperty("simple") SimpleObject simple,
                @JsonProperty("numbers") List<Integer> numbers,
                @JsonProperty("metadata") Map<String, Object> metadata) {
            this.simple = simple;
            this.numbers = numbers;
            this.metadata = metadata;
        }

        public SimpleObject getSimple() {
            return simple;
        }

        public List<Integer> getNumbers() {
            return numbers;
        }

        public Map<String, Object> getMetadata() {
            return metadata;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (!(obj instanceof ComplexObject))
                return false;
            ComplexObject other = (ComplexObject) obj;
            return (simple == null ? other.simple == null : simple.equals(other.simple)) &&
                    (numbers == null ? other.numbers == null : numbers.equals(other.numbers)) &&
                    (metadata == null ? other.metadata == null : metadata.equals(other.metadata));
        }
    }

    @Nested
    @DisplayName("String-based JSON Operations")
    class StringOperations {

        @Test
        @DisplayName("toString() should serialize simple object to JSON string")
        void testToString_SimpleObject_ReturnsValidJson() {
            // Given
            SimpleObject obj = new SimpleObject("test", 42);

            // When
            String json = SpecsJackson.toString(obj);

            // Then
            assertThat(json)
                    .isNotNull()
                    .contains("\"name\":\"test\"")
                    .contains("\"value\":42");
        }

        @Test
        @DisplayName("toString() should serialize complex object with nested data")
        void testToString_ComplexObject_ReturnsValidJson() {
            // Given
            SimpleObject simple = new SimpleObject("nested", 123);
            ComplexObject complex = new ComplexObject(simple,
                    Arrays.asList(1, 2, 3),
                    Map.of("key1", "value1", "key2", 42));

            // When
            String json = SpecsJackson.toString(complex);

            // Then
            assertThat(json)
                    .isNotNull()
                    .contains("\"simple\":")
                    .contains("\"numbers\":[1,2,3]")
                    .contains("\"metadata\":");
        }

        @Test
        @DisplayName("toString() with type info should include type information")
        void testToString_WithTypeInfo_IncludesTypeInformation() {
            // Given
            SimpleObject obj = new SimpleObject("typed", 999);

            // When
            String json = SpecsJackson.toString(obj, true);

            // Then
            assertThat(json)
                    .isNotNull()
                    .contains("SimpleObject")
                    .startsWith("[");
        }

        @Test
        @DisplayName("fromString() should deserialize JSON string to object")
        void testFromString_ValidJson_ReturnsCorrectObject() {
            // Given
            String json = "{\"name\":\"test\",\"value\":42}";

            // When
            SimpleObject result = SpecsJackson.fromString(json, SimpleObject.class);

            // Then
            assertThat(result)
                    .isNotNull()
                    .satisfies(obj -> {
                        assertThat(obj.getName()).isEqualTo("test");
                        assertThat(obj.getValue()).isEqualTo(42);
                    });
        }

        @Test
        @DisplayName("fromString() should handle complex nested objects")
        void testFromString_ComplexJson_ReturnsCorrectObject() {
            // Given
            String json = "{\"simple\":{\"name\":\"nested\",\"value\":123}," +
                    "\"numbers\":[1,2,3]," +
                    "\"metadata\":{\"key1\":\"value1\",\"key2\":42}}";

            // When
            ComplexObject result = SpecsJackson.fromString(json, ComplexObject.class);

            // Then
            assertThat(result)
                    .isNotNull()
                    .satisfies(obj -> {
                        assertThat(obj.getSimple().getName()).isEqualTo("nested");
                        assertThat(obj.getSimple().getValue()).isEqualTo(123);
                        assertThat(obj.getNumbers()).containsExactly(1, 2, 3);
                        assertThat(obj.getMetadata()).containsKeys("key1", "key2");
                    });
        }

        @Test
        @DisplayName("fromString() with type info should deserialize with polymorphic types")
        void testFromString_WithTypeInfo_HandlesPolymorphicTypes() {
            // Given
            SimpleObject original = new SimpleObject("typed", 999);
            String json = SpecsJackson.toString(original, true);

            // When
            SimpleObject result = SpecsJackson.fromString(json, SimpleObject.class, true);

            // Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(original);
        }

        @ParameterizedTest
        @ValueSource(strings = { "", "   ", "invalid json", "{invalid}" })
        @DisplayName("fromString() should throw RuntimeException for invalid JSON")
        void testFromString_InvalidJson_ThrowsRuntimeException(String invalidJson) {
            // When/Then
            assertThatThrownBy(() -> SpecsJackson.fromString(invalidJson, SimpleObject.class))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("fromString() should handle JSON null string correctly")
        void testFromString_NullJsonString_ReturnsNull() {
            // When
            SimpleObject result = SpecsJackson.fromString("null", SimpleObject.class);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("roundtrip serialization should preserve object equality")
        void testRoundtripSerialization_Object_PreservesEquality() {
            // Given
            SimpleObject original = new SimpleObject("roundtrip", 777);

            // When
            String json = SpecsJackson.toString(original);
            SimpleObject deserialized = SpecsJackson.fromString(json, SimpleObject.class);

            // Then
            assertThat(deserialized).isEqualTo(original);
        }
    }

    @Nested
    @DisplayName("File-based JSON Operations")
    class FileOperations {

        @Test
        @DisplayName("toFile() should write object to JSON file")
        void testToFile_SimpleObject_WritesValidJson() throws IOException {
            // Given
            SimpleObject obj = new SimpleObject("file_test", 123);
            File file = tempDir.resolve("test.json").toFile();

            // When
            SpecsJackson.toFile(obj, file);

            // Then
            assertThat(file).exists();

            String content = Files.readString(file.toPath());
            assertThat(content)
                    .contains("\"name\":\"file_test\"")
                    .contains("\"value\":123");
        }

        @Test
        @DisplayName("toFile() with type info should embed type information in file")
        void testToFile_WithTypeInfo_EmbedsTypeInformation() throws IOException {
            // Given
            SimpleObject obj = new SimpleObject("typed_file", 456);
            File file = tempDir.resolve("typed_test.json").toFile();

            // When
            SpecsJackson.toFile(obj, file, true);

            // Then
            assertThat(file).exists();

            String content = Files.readString(file.toPath());
            assertThat(content)
                    .contains("SimpleObject")
                    .startsWith("[");
        }

        @Test
        @DisplayName("fromFile(String) should read object from JSON file")
        void testFromFile_StringPath_ReadsCorrectObject() throws IOException {
            // Given
            SimpleObject original = new SimpleObject("path_test", 789);
            File file = tempDir.resolve("path_test.json").toFile();
            SpecsJackson.toFile(original, file);

            // When
            SimpleObject result = SpecsJackson.fromFile(file.getAbsolutePath(), SimpleObject.class);

            // Then
            assertThat(result).isEqualTo(original);
        }

        @Test
        @DisplayName("fromFile(File) should read object from JSON file")
        void testFromFile_FileObject_ReadsCorrectObject() throws IOException {
            // Given
            SimpleObject original = new SimpleObject("file_obj_test", 101112);
            File file = tempDir.resolve("file_obj_test.json").toFile();
            SpecsJackson.toFile(original, file);

            // When
            SimpleObject result = SpecsJackson.fromFile(file, SimpleObject.class);

            // Then
            assertThat(result).isEqualTo(original);
        }

        @Test
        @DisplayName("fromFile() with type info should handle polymorphic deserialization")
        void testFromFile_WithTypeInfo_HandlesPolymorphicTypes() throws IOException {
            // Given
            SimpleObject original = new SimpleObject("polymorphic_test", 131415);
            File file = tempDir.resolve("polymorphic_test.json").toFile();
            SpecsJackson.toFile(original, file, true);

            // When
            SimpleObject result = SpecsJackson.fromFile(file, SimpleObject.class, true);

            // Then
            assertThat(result).isEqualTo(original);
        }

        @Test
        @DisplayName("fromFile() should throw RuntimeException for non-existent file")
        void testFromFile_NonExistentFile_ThrowsRuntimeException() {
            // Given
            File nonExistentFile = tempDir.resolve("does_not_exist.json").toFile();

            // When/Then
            assertThatThrownBy(() -> SpecsJackson.fromFile(nonExistentFile, SimpleObject.class))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("fromFile() should throw RuntimeException for invalid JSON file")
        void testFromFile_InvalidJsonFile_ThrowsRuntimeException() throws IOException {
            // Given
            File file = tempDir.resolve("invalid.json").toFile();
            Files.writeString(file.toPath(), "invalid json content");

            // When/Then
            assertThatThrownBy(() -> SpecsJackson.fromFile(file, SimpleObject.class))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("file roundtrip should preserve object equality")
        void testFileRoundtrip_Object_PreservesEquality() throws IOException {
            // Given
            ComplexObject original = new ComplexObject(
                    new SimpleObject("roundtrip_file", 161718),
                    Arrays.asList(10, 20, 30),
                    Map.of("test", "value", "number", 99));
            File file = tempDir.resolve("roundtrip.json").toFile();

            // When
            SpecsJackson.toFile(original, file);
            ComplexObject result = SpecsJackson.fromFile(file, ComplexObject.class);

            // Then
            assertThat(result).isEqualTo(original);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrors {

        @Test
        @DisplayName("toString() should handle null object")
        void testToString_NullObject_ReturnsNullJson() {
            // When
            String result = SpecsJackson.toString(null);

            // Then
            assertThat(result).isEqualTo("null");
        }

        @Test
        @DisplayName("fromString() should handle null JSON string")
        void testFromString_NullString_ThrowsRuntimeException() {
            // When/Then
            assertThatThrownBy(() -> SpecsJackson.fromString(null, SimpleObject.class))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("toFile() should handle null object")
        void testToFile_NullObject_WritesNullJson() throws IOException {
            // Given
            File file = tempDir.resolve("null_test.json").toFile();

            // When
            SpecsJackson.toFile(null, file);

            // Then
            assertThat(file).exists();
            String content = Files.readString(file.toPath());
            assertThat(content.trim()).isEqualTo("null");
        }

        @Test
        @DisplayName("toFile() should throw RuntimeException for read-only file")
        void testToFile_ReadOnlyFile_ThrowsRuntimeException() throws IOException {
            // Given
            SimpleObject obj = new SimpleObject("readonly_test", 192021);
            File file = tempDir.resolve("readonly.json").toFile();
            file.createNewFile();
            file.setReadOnly();

            // When/Then
            assertThatThrownBy(() -> SpecsJackson.toFile(obj, file))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("operations should handle empty objects")
        void testOperations_EmptyObject_HandleCorrectly() throws IOException {
            // Given
            SimpleObject emptyObj = new SimpleObject(null, 0);
            File file = tempDir.resolve("empty.json").toFile();

            // When
            String jsonString = SpecsJackson.toString(emptyObj);
            SpecsJackson.toFile(emptyObj, file);

            SimpleObject fromString = SpecsJackson.fromString(jsonString, SimpleObject.class);
            SimpleObject fromFile = SpecsJackson.fromFile(file, SimpleObject.class);

            // Then
            assertThat(fromString).isEqualTo(emptyObj);
            assertThat(fromFile).isEqualTo(emptyObj);
        }

        @Test
        @DisplayName("operations should handle objects with special characters")
        void testOperations_SpecialCharacters_HandleCorrectly() {
            // Given
            SimpleObject specialObj = new SimpleObject("test\n\t\"special\"", 222324);

            // When
            String json = SpecsJackson.toString(specialObj);
            SimpleObject result = SpecsJackson.fromString(json, SimpleObject.class);

            // Then
            assertThat(result).isEqualTo(specialObj);
            assertThat(json).contains("\\n").contains("\\t").contains("\\\"");
        }

        @Test
        @DisplayName("operations should handle large objects efficiently")
        void testOperations_LargeObject_HandlesEfficiently() {
            // Given
            Map<String, Object> largeMetadata = Map.of(
                    "key1", "value1".repeat(1000),
                    "key2", "value2".repeat(1000),
                    "key3", Arrays.asList(1, 2, 3, 4, 5).toString().repeat(100));
            ComplexObject largeObj = new ComplexObject(
                    new SimpleObject("large_test", 252627),
                    Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10),
                    largeMetadata);

            // When/Then - Should not throw any exceptions or timeout
            String json = SpecsJackson.toString(largeObj);
            ComplexObject result = SpecsJackson.fromString(json, ComplexObject.class);

            assertThat(result).isEqualTo(largeObj);
            assertThat(json.length()).isGreaterThan(1000);
        }
    }

    @Nested
    @DisplayName("Type Information Handling")
    class TypeInformationHandling {

        @Test
        @DisplayName("serialization without type info should not include type information")
        void testSerialization_WithoutTypeInfo_NoClassInfo() {
            // Given
            SimpleObject obj = new SimpleObject("no_type", 282930);

            // When
            String json = SpecsJackson.toString(obj, false);

            // Then
            assertThat(json)
                    .doesNotContain("SimpleObject")
                    .doesNotStartWith("[");
        }

        @Test
        @DisplayName("serialization with type info should include type information")
        void testSerialization_WithTypeInfo_IncludesClassInfo() {
            // Given
            SimpleObject obj = new SimpleObject("with_type", 313233);

            // When
            String json = SpecsJackson.toString(obj, true);

            // Then
            assertThat(json)
                    .contains("SimpleObject")
                    .startsWith("[");
        }

        @Test
        @DisplayName("type info consistency between string and file operations")
        void testTypeInfo_Consistency_BetweenStringAndFile() throws IOException {
            // Given
            SimpleObject obj = new SimpleObject("consistency", 343536);
            File file = tempDir.resolve("consistency.json").toFile();

            // When
            String stringJsonWithType = SpecsJackson.toString(obj, true);
            String stringJsonWithoutType = SpecsJackson.toString(obj, false);

            SpecsJackson.toFile(obj, file, true);
            String fileJsonWithType = Files.readString(file.toPath());

            // Then
            assertThat(stringJsonWithType).contains("SimpleObject").startsWith("[");
            assertThat(stringJsonWithoutType).doesNotContain("SimpleObject").doesNotStartWith("[");
            assertThat(fileJsonWithType).contains("SimpleObject").startsWith("[");
        }

        @Test
        @DisplayName("type info roundtrip should preserve object type and data")
        void testTypeInfoRoundtrip_PreservesObjectTypeAndData() {
            // Given
            SimpleObject original = new SimpleObject("type_roundtrip", 373839);

            // When
            String json = SpecsJackson.toString(original, true);
            SimpleObject result = SpecsJackson.fromString(json, SimpleObject.class, true);

            // Then
            assertThat(result)
                    .isNotNull()
                    .isEqualTo(original)
                    .isInstanceOf(SimpleObject.class);
        }
    }

    @Nested
    @DisplayName("Method Overloading Behavior")
    class MethodOverloadingBehavior {

        @Test
        @DisplayName("fromFile string path should delegate to file path with false hasTypeInfo")
        void testFromFile_StringPathDelegation_UsesDefaultTypeInfo() throws IOException {
            // Given
            SimpleObject original = new SimpleObject("delegation_test", 404142);
            File file = tempDir.resolve("delegation.json").toFile();
            SpecsJackson.toFile(original, file, false);

            // When
            SimpleObject result1 = SpecsJackson.fromFile(file.getAbsolutePath(), SimpleObject.class);
            SimpleObject result2 = SpecsJackson.fromFile(file.getAbsolutePath(), SimpleObject.class, false);

            // Then
            assertThat(result1).isEqualTo(result2).isEqualTo(original);
        }

        @Test
        @DisplayName("fromFile file object should delegate to file object with false hasTypeInfo")
        void testFromFile_FileObjectDelegation_UsesDefaultTypeInfo() throws IOException {
            // Given
            SimpleObject original = new SimpleObject("file_delegation_test", 434445);
            File file = tempDir.resolve("file_delegation.json").toFile();
            SpecsJackson.toFile(original, file, false);

            // When
            SimpleObject result1 = SpecsJackson.fromFile(file, SimpleObject.class);
            SimpleObject result2 = SpecsJackson.fromFile(file, SimpleObject.class, false);

            // Then
            assertThat(result1).isEqualTo(result2).isEqualTo(original);
        }

        @Test
        @DisplayName("fromString should delegate with false hasTypeInfo")
        void testFromString_Delegation_UsesDefaultTypeInfo() {
            // Given
            SimpleObject original = new SimpleObject("string_delegation", 464748);
            String json = SpecsJackson.toString(original, false);

            // When
            SimpleObject result1 = SpecsJackson.fromString(json, SimpleObject.class);
            SimpleObject result2 = SpecsJackson.fromString(json, SimpleObject.class, false);

            // Then
            assertThat(result1).isEqualTo(result2).isEqualTo(original);
        }

        @Test
        @DisplayName("toString should delegate with false embedTypeInfo")
        void testToString_Delegation_UsesDefaultTypeInfo() {
            // Given
            SimpleObject obj = new SimpleObject("to_string_delegation", 495051);

            // When
            String result1 = SpecsJackson.toString(obj);
            String result2 = SpecsJackson.toString(obj, false);

            // Then
            assertThat(result1).isEqualTo(result2);
            assertThat(result1).doesNotContain("SimpleObject").doesNotStartWith("[");
        }

        @Test
        @DisplayName("toFile should delegate with false embedTypeInfo")
        void testToFile_Delegation_UsesDefaultTypeInfo() throws IOException {
            // Given
            SimpleObject obj = new SimpleObject("to_file_delegation", 525354);
            File file1 = tempDir.resolve("to_file_1.json").toFile();
            File file2 = tempDir.resolve("to_file_2.json").toFile();

            // When
            SpecsJackson.toFile(obj, file1);
            SpecsJackson.toFile(obj, file2, false);

            // Then
            String content1 = Files.readString(file1.toPath());
            String content2 = Files.readString(file2.toPath());

            assertThat(content1).isEqualTo(content2);
            assertThat(content1).doesNotContain("SimpleObject").doesNotStartWith("[");
        }
    }
}
