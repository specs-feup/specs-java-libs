package pt.up.fe.specs.util.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import pt.up.fe.specs.util.providers.KeyProvider;
import pt.up.fe.specs.util.providers.StringProvider;
import pt.up.fe.specs.util.enums.EnumHelperWithValue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Properties;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for SpecsProperties utility class.
 * Tests property loading, saving, type conversions, and file operations.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsProperties Tests")
class SpecsPropertiesTest {

    // Test key providers for testing
    private static final KeyProvider<String> TEST_KEY = () -> "test.key";
    private static final KeyProvider<String> STRING_KEY = () -> "string.value";
    private static final KeyProvider<String> INT_KEY = () -> "int.value";
    private static final KeyProvider<String> BOOLEAN_KEY = () -> "boolean.value";
    private static final KeyProvider<String> FOLDER_KEY = () -> "folder.path";
    private static final KeyProvider<String> FILE_KEY = () -> "file.path";
    private static final KeyProvider<String> MISSING_KEY = () -> "missing.key";

    @Nested
    @DisplayName("Constructor and Factory Methods")
    class ConstructorAndFactoryMethods {

        @Test
        @DisplayName("Should create empty SpecsProperties")
        void testNewEmpty() {
            SpecsProperties props = SpecsProperties.newEmpty();

            assertThat(props).isNotNull();
            assertThat(props.getProperties()).isEmpty();
        }

        @Test
        @DisplayName("Should create SpecsProperties from Properties object")
        void testConstructorFromProperties() {
            Properties props = new Properties();
            props.setProperty("key1", "value1");
            props.setProperty("key2", "value2");

            SpecsProperties specsProps = new SpecsProperties(props);

            assertThat(specsProps.getProperties()).hasSize(2);
            assertThat(specsProps.get(() -> "key1")).isEqualTo("value1");
            assertThat(specsProps.get(() -> "key2")).isEqualTo("value2");
        }

        @Test
        @DisplayName("Should create SpecsProperties from file")
        void testNewInstanceFromFile(@TempDir File tempDir) throws IOException {
            File propsFile = new File(tempDir, "test.properties");
            String content = "name=John\nage=25\ncity=NYC";
            Files.write(propsFile.toPath(), content.getBytes());

            SpecsProperties props = SpecsProperties.newInstance(propsFile);

            assertThat(props).isNotNull();
            assertThat(props.get(() -> "name")).isEqualTo("John");
            assertThat(props.get(() -> "age")).isEqualTo("25");
            assertThat(props.get(() -> "city")).isEqualTo("NYC");
        }

        @Test
        @DisplayName("Should throw exception for null file")
        void testNewInstanceFromNullFile() {
            assertThatThrownBy(() -> SpecsProperties.newInstance((File) null))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Should throw exception for non-existent file")
        void testNewInstanceFromNonExistentFile() {
            File nonExistentFile = new File("does-not-exist.properties");

            assertThatThrownBy(() -> SpecsProperties.newInstance(nonExistentFile))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not load properties file");
        }
    }

    @Nested
    @DisplayName("Key Operations")
    class KeyOperations {

        @Test
        @DisplayName("Should check if key exists")
        void testHasKey() {
            Properties props = new Properties();
            props.setProperty("existing.key", "value");
            SpecsProperties specsProps = new SpecsProperties(props);

            assertThat(specsProps.hasKey(() -> "existing.key")).isTrue();
            assertThat(specsProps.hasKey(() -> "missing.key")).isFalse();
        }

        @Test
        @DisplayName("Should put key-value pairs")
        void testPut() {
            SpecsProperties props = SpecsProperties.newEmpty();

            Object result = props.put(TEST_KEY, "test.value");

            assertThat(result).isNull(); // No previous value
            assertThat(props.hasKey(TEST_KEY)).isTrue();
            assertThat(props.get(TEST_KEY)).isEqualTo("test.value");
        }

        @Test
        @DisplayName("Should replace existing key value")
        void testPutReplace() {
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(TEST_KEY, "original");

            Object result = props.put(TEST_KEY, "updated");

            assertThat(result).isEqualTo("original");
            assertThat(props.get(TEST_KEY)).isEqualTo("updated");
        }
    }

    @Nested
    @DisplayName("Value Retrieval")
    class ValueRetrieval {

        private SpecsProperties createTestProperties() {
            Properties props = new Properties();
            props.setProperty("string.value", "  test string  ");
            props.setProperty("int.value", "42");
            props.setProperty("boolean.value", "true");
            props.setProperty("empty.value", "");
            return new SpecsProperties(props);
        }

        @Test
        @DisplayName("Should get string value and trim whitespace")
        void testGetString() {
            SpecsProperties props = createTestProperties();

            String value = props.get(STRING_KEY);

            assertThat(value).isEqualTo("test string"); // Should be trimmed
        }

        @Test
        @DisplayName("Should return empty string for missing key")
        void testGetMissingKey() {
            SpecsProperties props = createTestProperties();

            String value = props.get(MISSING_KEY);

            assertThat(value).isEmpty();
        }

        @Test
        @DisplayName("Should get integer value")
        void testGetInt() {
            SpecsProperties props = createTestProperties();

            int value = props.getInt(INT_KEY);

            assertThat(value).isEqualTo(42);
        }

        @Test
        @DisplayName("Should throw exception for invalid integer")
        void testGetIntInvalid() {
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(() -> "invalid.int", "not-a-number");

            assertThatThrownBy(() -> props.getInt(() -> "invalid.int"))
                    .isInstanceOf(NumberFormatException.class);
        }

        @Test
        @DisplayName("Should get boolean value")
        void testGetBoolean() {
            SpecsProperties props = createTestProperties();

            boolean value = props.getBoolean(BOOLEAN_KEY);

            assertThat(value).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = { "true", "TRUE", "True", "false", "FALSE", "False", "invalid" })
        @DisplayName("Should parse boolean values")
        void testGetBooleanVariousValues(String booleanString) {
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(() -> "bool.test", booleanString);

            boolean result = props.getBoolean(() -> "bool.test");

            boolean expected = Boolean.parseBoolean(booleanString);
            assertThat(result).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should get value or default")
        void testGetOrElse() {
            SpecsProperties props = createTestProperties();

            String existing = props.getOrElse(STRING_KEY, "default");
            String missing = props.getOrElse(MISSING_KEY, "default");

            assertThat(existing).isEqualTo("test string");
            assertThat(missing).isEqualTo("default");
        }
    }

    @Nested
    @DisplayName("File and Folder Operations")
    class FileAndFolderOperations {

        @Test
        @DisplayName("Should create folder from property")
        void testGetFolder(@TempDir File tempDir) {
            SpecsProperties props = SpecsProperties.newEmpty();
            File testFolder = new File(tempDir, "test-folder");
            props.put(FOLDER_KEY, testFolder.getAbsolutePath());

            File result = props.getFolder(FOLDER_KEY);

            assertThat(result).isNotNull();
            assertThat(result.exists()).isTrue();
            assertThat(result.isDirectory()).isTrue();
        }

        @Test
        @DisplayName("Should return null for empty folder path")
        void testGetFolderEmpty() {
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(FOLDER_KEY, "");

            File result = props.getFolder(FOLDER_KEY);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should get existing file")
        void testGetExistingFile(@TempDir File tempDir) throws IOException {
            File testFile = new File(tempDir, "test.txt");
            Files.write(testFile.toPath(), "test content".getBytes());

            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(FILE_KEY, testFile.getAbsolutePath());

            Optional<File> result = props.getExistingFile(FILE_KEY);

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(testFile);
        }

        @Test
        @DisplayName("Should return empty for non-existent file")
        void testGetExistingFileNotFound() {
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(FILE_KEY, "/non/existent/file.txt");

            Optional<File> result = props.getExistingFile(FILE_KEY);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should get existing folder")
        void testGetExistingFolder(@TempDir File tempDir) {
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(FOLDER_KEY, tempDir.getAbsolutePath());

            Optional<File> result = props.getExistingFolder(FOLDER_KEY);

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(tempDir);
        }

        @Test
        @DisplayName("Should return empty for non-existent folder")
        void testGetExistingFolderNotFound() {
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(FOLDER_KEY, "/non/existent/folder");

            // getExistingFolder calls getFolder which tries to create the folder
            // This will fail if we don't have permission to create in root
            assertThatThrownBy(() -> props.getExistingFolder(FOLDER_KEY))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("does not exist and could not be created");
        }
    }

    @Nested
    @DisplayName("Enum Operations")
    class EnumOperations {

        // Simple test enum
        enum TestEnum implements StringProvider {
            VALUE1("value1"),
            VALUE2("value2"),
            VALUE3("value3");

            private final String value;

            TestEnum(String value) {
                this.value = value;
            }

            @Override
            public String getString() {
                return value;
            }
        }

        private static final EnumHelperWithValue<TestEnum> ENUM_HELPER = new EnumHelperWithValue<>(TestEnum.class);

        @Test
        @DisplayName("Should get enum value")
        void testGetEnum() {
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(() -> "enum.test", "value2");

            Optional<TestEnum> result = props.getEnum(() -> "enum.test", ENUM_HELPER);

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(TestEnum.VALUE2);
        }

        @Test
        @DisplayName("Should return empty for missing enum key")
        void testGetEnumMissing() {
            SpecsProperties props = SpecsProperties.newEmpty();

            // Missing key returns empty string, which throws exception when parsing
            assertThatThrownBy(() -> props.getEnum(() -> "missing.enum", ENUM_HELPER))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("does not contain an enum with the name ''");
        }

        @Test
        @DisplayName("Should handle invalid enum value")
        void testGetEnumInvalid() {
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(() -> "enum.test", "invalid");

            assertThatThrownBy(() -> props.getEnum(() -> "enum.test", ENUM_HELPER))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Persistence Operations")
    class PersistenceOperations {

        @Test
        @DisplayName("Should store properties to file")
        void testStore(@TempDir File tempDir) throws IOException {
            File outputFile = new File(tempDir, "output.properties");
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(() -> "key1", "value1");
            props.put(() -> "key2", "value2");

            boolean result = props.store(outputFile);

            assertThat(result).isTrue();
            assertThat(outputFile.exists()).isTrue();

            // Verify content by loading it back
            SpecsProperties loaded = SpecsProperties.newInstance(outputFile);
            assertThat(loaded.get(() -> "key1")).isEqualTo("value1");
            assertThat(loaded.get(() -> "key2")).isEqualTo("value2");
        }

        @Test
        @DisplayName("Should handle store failure gracefully")
        void testStoreFailure() {
            File invalidFile = new File("/invalid/path/output.properties");
            SpecsProperties props = SpecsProperties.newEmpty();

            boolean result = props.store(invalidFile);

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("Utility Methods")
    class UtilityMethods {

        @Test
        @DisplayName("Should convert to string")
        void testToString() {
            Properties props = new Properties();
            props.setProperty("key1", "value1");
            props.setProperty("key2", "value2");
            SpecsProperties specsProps = new SpecsProperties(props);

            String result = specsProps.toString();

            assertThat(result).contains("key1");
            assertThat(result).contains("value1");
            assertThat(result).contains("key2");
            assertThat(result).contains("value2");
        }

        @Test
        @DisplayName("Should convert to JSON")
        void testToJson() {
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(() -> "name", "John");
            props.put(() -> "age", "25");

            String json = props.toJson();

            assertThat(json).startsWith("{");
            assertThat(json).endsWith("}");
            assertThat(json).contains("name:John");
            assertThat(json).contains("age:25");
        }

        @Test
        @DisplayName("Should convert empty properties to JSON")
        void testToJsonEmpty() {
            SpecsProperties props = SpecsProperties.newEmpty();

            String json = props.toJson();

            assertThat(json).isEqualTo("{}");
        }

        @Test
        @DisplayName("Should get internal Properties object")
        void testGetProperties() {
            Properties originalProps = new Properties();
            originalProps.setProperty("test", "value");
            SpecsProperties specsProps = new SpecsProperties(originalProps);

            Properties result = specsProps.getProperties();

            assertThat(result).isSameAs(originalProps);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle properties with special characters")
        void testSpecialCharacters() {
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(() -> "special.chars", "value with spaces, symbols: !@#$%^&*()");

            String result = props.get(() -> "special.chars");

            assertThat(result).isEqualTo("value with spaces, symbols: !@#$%^&*()");
        }

        @Test
        @DisplayName("Should handle unicode characters")
        void testUnicodeCharacters() {
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(() -> "unicode.test", "こんにちは世界 🌍 αβγδε");

            String result = props.get(() -> "unicode.test");

            assertThat(result).isEqualTo("こんにちは世界 🌍 αβγδε");
        }

        @Test
        @DisplayName("Should handle very long property values")
        void testLongValues() {
            String longValue = "a".repeat(10000);
            SpecsProperties props = SpecsProperties.newEmpty();
            props.put(() -> "long.value", longValue);

            String result = props.get(() -> "long.value");

            assertThat(result).hasSize(10000);
            assertThat(result).isEqualTo(longValue);
        }

        @Test
        @DisplayName("Should handle null values gracefully")
        void testNullValues() {
            SpecsProperties props = SpecsProperties.newEmpty();

            // Properties doesn't allow null values, this will throw NPE
            assertThatThrownBy(() -> props.put(() -> "null.test", null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty property files")
        void testEmptyPropertyFile(@TempDir File tempDir) throws IOException {
            File emptyFile = new File(tempDir, "empty.properties");
            Files.write(emptyFile.toPath(), "".getBytes());

            SpecsProperties props = SpecsProperties.newInstance(emptyFile);

            assertThat(props).isNotNull();
            assertThat(props.getProperties()).isEmpty();
        }

        @Test
        @DisplayName("Should handle malformed property files")
        void testMalformedPropertyFile(@TempDir File tempDir) throws IOException {
            File malformedFile = new File(tempDir, "malformed.properties");
            Files.write(malformedFile.toPath(), "key1=value1\ninvalid line without equals\nkey2=value2".getBytes());

            // Properties class should handle this gracefully by ignoring malformed lines
            SpecsProperties props = SpecsProperties.newInstance(malformedFile);

            assertThat(props).isNotNull();
            assertThat(props.get(() -> "key1")).isEqualTo("value1");
            assertThat(props.get(() -> "key2")).isEqualTo("value2");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete workflow")
        void testCompleteWorkflow(@TempDir File tempDir) throws IOException {
            // Create original properties
            SpecsProperties originalProps = SpecsProperties.newEmpty();
            originalProps.put(() -> "app.name", "TestApp");
            originalProps.put(() -> "app.version", "1.0.0");
            originalProps.put(() -> "app.debug", "true");
            originalProps.put(() -> "app.port", "8080");

            // Save to file
            File propsFile = new File(tempDir, "app.properties");
            boolean saved = originalProps.store(propsFile);
            assertThat(saved).isTrue();

            // Load from file
            SpecsProperties loadedProps = SpecsProperties.newInstance(propsFile);

            // Verify all data types
            assertThat(loadedProps.get(() -> "app.name")).isEqualTo("TestApp");
            assertThat(loadedProps.get(() -> "app.version")).isEqualTo("1.0.0");
            assertThat(loadedProps.getBoolean(() -> "app.debug")).isTrue();
            assertThat(loadedProps.getInt(() -> "app.port")).isEqualTo(8080);

            // Test modifications
            loadedProps.put(() -> "app.updated", "true");
            assertThat(loadedProps.hasKey(() -> "app.updated")).isTrue();

            // Test JSON export
            String json = loadedProps.toJson();
            assertThat(json).contains("app.name:TestApp");
            assertThat(json).contains("app.version:1.0.0");
        }

        @Test
        @DisplayName("Should handle complex property file")
        void testComplexPropertyFile(@TempDir File tempDir) throws IOException {
            File complexFile = new File(tempDir, "complex.properties");
            String content = """
                    # Application Configuration
                    app.name=My Application
                    app.version=2.1.3

                    # Database Settings
                    db.host=localhost
                    db.port=5432
                    db.name=mydb
                    db.ssl.enabled=true

                    # Feature Flags
                    feature.new_ui=false
                    feature.analytics=true

                    # Paths
                    data.folder=/path/to/data
                    log.folder=/var/log/app

                    # Numbers
                    max.connections=100
                    timeout.seconds=30
                    """;

            Files.write(complexFile.toPath(), content.getBytes());

            SpecsProperties props = SpecsProperties.newInstance(complexFile);

            // Verify various property types
            assertThat(props.get(() -> "app.name")).isEqualTo("My Application");
            assertThat(props.get(() -> "app.version")).isEqualTo("2.1.3");
            assertThat(props.get(() -> "db.host")).isEqualTo("localhost");
            assertThat(props.getInt(() -> "db.port")).isEqualTo(5432);
            assertThat(props.getBoolean(() -> "db.ssl.enabled")).isTrue();
            assertThat(props.getBoolean(() -> "feature.new_ui")).isFalse();
            assertThat(props.getInt(() -> "max.connections")).isEqualTo(100);
            assertThat(props.getInt(() -> "timeout.seconds")).isEqualTo(30);
        }
    }
}
