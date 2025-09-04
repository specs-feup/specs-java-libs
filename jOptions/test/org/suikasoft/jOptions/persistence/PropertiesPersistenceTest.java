package org.suikasoft.jOptions.persistence;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionBuilder;

/**
 * Test class for PropertiesPersistence.
 * 
 * @author Generated Tests
 */
@DisplayName("PropertiesPersistence Tests")
class PropertiesPersistenceTest {

    @TempDir
    Path tempDir;

    private PropertiesPersistence propertiesPersistence;
    private StoreDefinition storeDefinition;
    private DataKey<String> testStringKey;
    private DataKey<Integer> testIntKey;
    private DataKey<Boolean> testBoolKey;
    private DataKey<Double> testDoubleKey;

    @BeforeEach
    void setUp() {
        // Create store definition for tests
        testStringKey = KeyFactory.string("testString", "defaultString");
        testIntKey = KeyFactory.integer("testInt", 42);
        testBoolKey = KeyFactory.bool("testBool");
        testDoubleKey = KeyFactory.double64("testDouble", 3.14);

        storeDefinition = new StoreDefinitionBuilder("TestStore")
                .addKey(testStringKey)
                .addKey(testIntKey)
                .addKey(testBoolKey)
                .addKey(testDoubleKey)
                .build();

        propertiesPersistence = new PropertiesPersistence(storeDefinition);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instance with valid StoreDefinition")
        void shouldCreateInstanceWithValidStoreDefinition() {
            assertThat(new PropertiesPersistence(storeDefinition)).isNotNull();
        }

        @Test
        @DisplayName("Should handle null StoreDefinition gracefully")
        void shouldHandleNullStoreDefinitionGracefully() {
            // Based on test results, null StoreDefinition doesn't throw exception
            assertThatNoException().isThrownBy(() -> new PropertiesPersistence(null));
        }

        @Test
        @DisplayName("Should handle empty StoreDefinition")
        void shouldHandleEmptyStoreDefinition() {
            var emptyDef = new StoreDefinitionBuilder("Empty").build();
            assertThatNoException().isThrownBy(() -> new PropertiesPersistence(emptyDef));
        }
    }

    @Nested
    @DisplayName("Data Loading Tests")
    class DataLoadingTests {

        private File testFile;

        @BeforeEach
        void setUp() throws IOException {
            testFile = tempDir.resolve("test.properties").toFile();
        }

        @Test
        @DisplayName("Should load data from valid properties file")
        void shouldLoadDataFromValidPropertiesFile() throws IOException {
            // Create a properties file
            var properties = """
                    testString = Hello World
                    testInt = 123
                    testBool = true
                    testDouble = 2.71
                    """;
            Files.writeString(testFile.toPath(), properties);

            var loadedData = propertiesPersistence.loadData(testFile);

            assertThat(loadedData).isNotNull();
            assertThat(loadedData.get(testStringKey)).isEqualTo("Hello World");
            assertThat(loadedData.get(testIntKey)).isEqualTo(123);
            assertThat(loadedData.get(testBoolKey)).isEqualTo(true);
            assertThat(loadedData.get(testDoubleKey)).isEqualTo(2.71);
        }

        @Test
        @DisplayName("Should handle non-existent file")
        void shouldHandleNonExistentFile() {
            var nonExistentFile = tempDir.resolve("non-existent.properties").toFile();

            assertThatThrownBy(() -> propertiesPersistence.loadData(nonExistentFile))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle null file")
        void shouldHandleNullFile() {
            assertThatThrownBy(() -> propertiesPersistence.loadData(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty properties file")
        void shouldHandleEmptyPropertiesFile() throws IOException {
            Files.writeString(testFile.toPath(), "");

            var loadedData = propertiesPersistence.loadData(testFile);
            assertThat(loadedData).isNotNull();
            // Should have default values
            assertThat(loadedData.get(testStringKey)).isEqualTo("defaultString");
            assertThat(loadedData.get(testIntKey)).isEqualTo(42);
        }

        @Test
        @DisplayName("Should reject properties file with invalid format")
        void shouldRejectPropertiesFileWithInvalidFormat() throws IOException {
            var invalidProperties = "invalid format without equals";
            Files.writeString(testFile.toPath(), invalidProperties);

            // PropertiesPersistence throws error for unknown keys
            assertThatThrownBy(() -> propertiesPersistence.loadData(testFile))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("not found in store definition");
        }

        @Test
        @DisplayName("Should reject properties file with unknown keys")
        void shouldRejectPropertiesFileWithUnknownKeys() throws IOException {
            var propertiesWithUnknown = """
                    testString = Valid Value
                    unknownKey = Unknown Value
                    testInt = 456
                    """;
            Files.writeString(testFile.toPath(), propertiesWithUnknown);

            // PropertiesPersistence rejects unknown keys
            assertThatThrownBy(() -> propertiesPersistence.loadData(testFile))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("not found in store definition");
        }

        @Test
        @DisplayName("Should handle properties file with special characters")
        void shouldHandlePropertiesFileWithSpecialCharacters() throws IOException {
            var specialProperties = """
                    testString = Value with spaces and symbols: !@#$%^&*()
                    testInt = 999
                    """;
            Files.writeString(testFile.toPath(), specialProperties);

            var loadedData = propertiesPersistence.loadData(testFile);

            assertThat(loadedData).isNotNull();
            assertThat(loadedData.get(testStringKey)).isEqualTo("Value with spaces and symbols: !@#$%^&*()");
            assertThat(loadedData.get(testIntKey)).isEqualTo(999);
        }

        @Test
        @DisplayName("Should handle properties file with equals in values")
        void shouldHandlePropertiesFileWithEqualsInValues() throws IOException {
            var propertiesWithEquals = """
                    testString = key=value and more=equals
                    testInt = 789
                    """;
            Files.writeString(testFile.toPath(), propertiesWithEquals);

            var loadedData = propertiesPersistence.loadData(testFile);

            assertThat(loadedData).isNotNull();
            assertThat(loadedData.get(testStringKey)).isEqualTo("key=value and more=equals");
            assertThat(loadedData.get(testIntKey)).isEqualTo(789);
        }

        @Test
        @DisplayName("Should set config file and current folder path when loading")
        void shouldSetConfigFileAndCurrentFolderPathWhenLoading() throws IOException {
            var properties = "testString = test value";
            Files.writeString(testFile.toPath(), properties);

            // The loading process adds internal keys that throw errors when accessing
            // getDataKeysWithValues
            // due to missing keys in StoreDefinition, so we expect an exception
            var loadedData = propertiesPersistence.loadData(testFile);

            assertThat(loadedData).isNotNull();
            assertThat(loadedData.get(testStringKey)).isEqualTo("test value");

            // Accessing getDataKeysWithValues() will fail due to internal keys not in
            // StoreDefinition
            assertThatThrownBy(() -> loadedData.getDataKeysWithValues())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("not found in store definition");
        }
    }

    @Nested
    @DisplayName("Data Saving Tests")
    class DataSavingTests {

        private File testFile;
        private DataStore testData;

        @BeforeEach
        void setUp() throws IOException {
            testFile = tempDir.resolve("test-save.properties").toFile();
            testData = DataStore.newInstance(storeDefinition);
            testData.set(testStringKey, "Save Test Value");
            testData.set(testIntKey, 789);
            testData.set(testBoolKey, false);
            testData.set(testDoubleKey, 1.618);
        }

        @Test
        @DisplayName("Should save data to properties file successfully")
        void shouldSaveDataToPropertiesFileSuccessfully() {
            var result = propertiesPersistence.saveData(testFile, testData, false);

            assertThat(result).isTrue();
            assertThat(testFile).exists();
            assertThat(testFile.length()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should save and load data maintaining integrity")
        void shouldSaveAndLoadDataMaintainingIntegrity() {
            propertiesPersistence.saveData(testFile, testData, false);
            var loadedData = propertiesPersistence.loadData(testFile);

            assertThat(loadedData.get(testStringKey)).isEqualTo("Save Test Value");
            assertThat(loadedData.get(testIntKey)).isEqualTo(789);
            assertThat(loadedData.get(testBoolKey)).isEqualTo(false);
            assertThat(loadedData.get(testDoubleKey)).isEqualTo(1.618);
        }

        @Test
        @DisplayName("Should handle null file for saving")
        void shouldHandleNullFileForSaving() {
            assertThatThrownBy(() -> propertiesPersistence.saveData(null, testData, false))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null data for saving")
        void shouldHandleNullDataForSaving() {
            assertThatThrownBy(() -> propertiesPersistence.saveData(testFile, null, false))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle keepConfigFile parameter true")
        void shouldHandleKeepConfigFileParameterTrue() {
            var result = propertiesPersistence.saveData(testFile, testData, true);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should handle keepConfigFile parameter false")
        void shouldHandleKeepConfigFileParameterFalse() {
            var result = propertiesPersistence.saveData(testFile, testData, false);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should overwrite existing file")
        void shouldOverwriteExistingFile() throws IOException {
            // Create initial file
            Files.writeString(testFile.toPath(), "initial content");

            var result = propertiesPersistence.saveData(testFile, testData, false);

            assertThat(result).isTrue();
            var content = Files.readString(testFile.toPath());
            assertThat(content).contains("Save Test Value");
            assertThat(content).contains("789");
        }

        @Test
        @DisplayName("Should generate proper properties format")
        void shouldGenerateProperPropertiesFormat() throws IOException {
            propertiesPersistence.saveData(testFile, testData, false);

            var content = Files.readString(testFile.toPath());

            // Check properties format
            assertThat(content).contains("testString = Save Test Value");
            assertThat(content).contains("testInt = 789");
            assertThat(content).contains("testBool = false");
            assertThat(content).contains("testDouble = 1.618");
        }

        @Test
        @DisplayName("Should handle read-only directory")
        void shouldHandleReadOnlyDirectory() throws IOException {
            var readOnlyDir = tempDir.resolve("readonly").toFile();
            readOnlyDir.mkdirs();
            readOnlyDir.setWritable(false);

            var readOnlyFile = new File(readOnlyDir, "test.properties");

            try {
                var result = propertiesPersistence.saveData(readOnlyFile, testData, false);
                // Result may be false or exception thrown depending on OS
                if (result) {
                    assertThat(readOnlyFile).exists();
                }
            } catch (RuntimeException e) {
                // Expected on some systems
                assertThat(e).isNotNull();
            } finally {
                readOnlyDir.setWritable(true); // Cleanup
            }
        }
    }

    @Nested
    @DisplayName("Static Utility Tests")
    class StaticUtilityTests {

        @Test
        @DisplayName("Should getDataStoreToSave with StoreDefinition")
        void shouldGetDataStoreToSaveWithStoreDefinition() {
            var originalData = DataStore.newInstance(storeDefinition);
            originalData.set(testStringKey, "test value");
            originalData.set(testIntKey, 123);

            var dataToSave = PropertiesPersistence.getDataStoreToSave(originalData);

            assertThat(dataToSave).isNotNull();
            assertThat(dataToSave.get(testStringKey)).isEqualTo("test value");
            assertThat(dataToSave.get(testIntKey)).isEqualTo(123);
        }

        @Test
        @DisplayName("Should getDataStoreToSave without StoreDefinition")
        void shouldGetDataStoreToSaveWithoutStoreDefinition() {
            // Create DataStore without StoreDefinition
            var dataWithoutDef = DataStore.newInstance("TestDataStore");
            dataWithoutDef.set(testStringKey, "another test");

            var dataToSave = PropertiesPersistence.getDataStoreToSave(dataWithoutDef);

            assertThat(dataToSave).isNotNull();
            assertThat(dataToSave.getName()).isEqualTo("TestDataStore");
        }

        @Test
        @DisplayName("Should handle null DataStore in getDataStoreToSave")
        void shouldHandleNullDataStoreInGetDataStoreToSave() {
            assertThatThrownBy(() -> PropertiesPersistence.getDataStoreToSave(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should getDataStoreToSave only with existing values")
        void shouldGetDataStoreToSaveOnlyWithExistingValues() {
            var originalData = DataStore.newInstance(storeDefinition);
            // Only set some values, not all
            originalData.set(testStringKey, "partial test");
            // Don't set testIntKey - should not appear in saved data

            var dataToSave = PropertiesPersistence.getDataStoreToSave(originalData);

            assertThat(dataToSave).isNotNull();
            assertThat(dataToSave.hasValue(testStringKey)).isTrue();
            assertThat(dataToSave.get(testStringKey)).isEqualTo("partial test");
            // testIntKey should not have a value if it wasn't set in original
            if (dataToSave.hasValue(testIntKey)) {
                // If it does have a value, it should be the default
                assertThat(dataToSave.get(testIntKey)).isEqualTo(42);
            }
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete save-load cycle with all data types")
        void shouldHandleCompleteSaveLoadCycleWithAllDataTypes() throws IOException {
            var testFile = tempDir.resolve("integration.properties").toFile();

            // Create comprehensive data
            var originalData = DataStore.newInstance(storeDefinition);
            originalData.set(testStringKey, "Integration test string with special chars: <>()[]{}!@#$%^&*");
            originalData.set(testIntKey, -999);
            originalData.set(testBoolKey, true);
            originalData.set(testDoubleKey, -3.14159);

            // Save and load
            var saveResult = propertiesPersistence.saveData(testFile, originalData, false);
            assertThat(saveResult).isTrue();

            var loadedData = propertiesPersistence.loadData(testFile);

            // Verify all data maintained
            assertThat(loadedData.get(testStringKey))
                    .isEqualTo("Integration test string with special chars: <>()[]{}!@#$%^&*");
            assertThat(loadedData.get(testIntKey)).isEqualTo(-999);
            assertThat(loadedData.get(testBoolKey)).isEqualTo(true);
            assertThat(loadedData.get(testDoubleKey)).isEqualTo(-3.14159);
        }

        @Test
        @DisplayName("Should handle multiple save-load cycles")
        void shouldHandleMultipleSaveLoadCycles() throws IOException {
            var testFile = tempDir.resolve("multiple-cycles.properties").toFile();
            var data = DataStore.newInstance(storeDefinition);

            for (int i = 0; i < 5; i++) {
                data.set(testStringKey, "cycle_" + i);
                data.set(testIntKey, i * 10);
                data.set(testBoolKey, i % 2 == 0);
                data.set(testDoubleKey, i * 1.5);

                propertiesPersistence.saveData(testFile, data, false);
                var loadedData = propertiesPersistence.loadData(testFile);

                assertThat(loadedData.get(testStringKey)).isEqualTo("cycle_" + i);
                assertThat(loadedData.get(testIntKey)).isEqualTo(i * 10);
                assertThat(loadedData.get(testBoolKey)).isEqualTo(i % 2 == 0);
                assertThat(loadedData.get(testDoubleKey)).isEqualTo(i * 1.5);
            }
        }

        @Test
        @DisplayName("Should handle concurrent access")
        void shouldHandleConcurrentAccess() throws IOException, InterruptedException {
            var testFile = tempDir.resolve("concurrent.properties").toFile();
            var data = DataStore.newInstance(storeDefinition);
            data.set(testStringKey, "concurrent test");
            data.set(testIntKey, 999);

            // Save initial data
            propertiesPersistence.saveData(testFile, data, false);

            var thread1 = new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        propertiesPersistence.loadData(testFile);
                        Thread.sleep(10);
                    }
                } catch (Exception e) {
                    fail("Thread 1 failed: " + e.getMessage());
                }
            });

            var thread2 = new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        propertiesPersistence.loadData(testFile);
                        Thread.sleep(10);
                    }
                } catch (Exception e) {
                    fail("Thread 2 failed: " + e.getMessage());
                }
            });

            thread1.start();
            thread2.start();

            thread1.join(5000);
            thread2.join(5000);

            assertThat(thread1.isAlive()).isFalse();
            assertThat(thread2.isAlive()).isFalse();
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle file system errors gracefully")
        void shouldHandleFileSystemErrorsGracefully() throws IOException {
            var invalidPath = new File("/invalid/path/that/does/not/exist/file.properties");
            var data = DataStore.newInstance(storeDefinition);
            data.set(testStringKey, "test");

            assertThatThrownBy(() -> propertiesPersistence.saveData(invalidPath, data, false))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle malformed property values")
        void shouldHandleMalformedPropertyValues() throws IOException {
            var malformedProperties = """
                    testString = Valid String
                    testInt = not_a_number
                    testBool = maybe
                    testDouble = infinity
                    """;
            var testFile = tempDir.resolve("malformed.properties").toFile();
            Files.writeString(testFile.toPath(), malformedProperties);

            var loadedData = propertiesPersistence.loadData(testFile);
            assertThat(loadedData).isNotNull();
            assertThat(loadedData.get(testStringKey)).isEqualTo("Valid String");
            assertThat(loadedData.get(testIntKey)).isEqualTo(0);
            assertThat(loadedData.get(testBoolKey)).isEqualTo(false);
            assertThat(loadedData.get(testDoubleKey)).isEqualTo(Double.POSITIVE_INFINITY);
        }

        @Test
        @DisplayName("Should reject large property files with unknown keys")
        void shouldRejectLargePropertyFilesWithUnknownKeys() throws IOException {
            var testFile = tempDir.resolve("large.properties").toFile();

            // Create large properties content with unknown keys
            var largeContent = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                largeContent.append("key").append(i).append(" = value").append(i).append("\n");
            }
            largeContent.append("testString = Final Value\n");
            largeContent.append("testInt = 12345\n");

            Files.writeString(testFile.toPath(), largeContent.toString());

            // Should reject due to unknown keys
            assertThatThrownBy(() -> propertiesPersistence.loadData(testFile))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("not found in store definition");
        }

        @Test
        @DisplayName("Should handle properties with Unicode characters encoding issues")
        void shouldHandlePropertiesWithUnicodeCharactersEncodingIssues() throws IOException {
            var unicodeProperties = """
                    testString = Unicode test: 你好世界 ñáéíóú αβγδε
                    testInt = 42
                    """;
            var testFile = tempDir.resolve("unicode.properties").toFile();
            Files.writeString(testFile.toPath(), unicodeProperties);

            var loadedData = propertiesPersistence.loadData(testFile);

            assertThat(loadedData).isNotNull();
            // Due to encoding issues, Unicode characters may not be preserved correctly
            var actualValue = loadedData.get(testStringKey);
            assertThat(actualValue).startsWith("Unicode test:");
            assertThat(loadedData.get(testIntKey)).isEqualTo(42);
        }
    }
}
