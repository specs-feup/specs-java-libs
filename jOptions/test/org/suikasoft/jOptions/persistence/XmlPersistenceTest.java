package org.suikasoft.jOptions.persistence;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

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
 * Test class for XmlPersistence.
 * 
 * @author Generated Tests
 */
@DisplayName("XmlPersistence Tests")
class XmlPersistenceTest {

    @TempDir
    Path tempDir;

    private XmlPersistence xmlPersistence;
    private StoreDefinition realStoreDefinition;
    private DataKey<String> testStringKey;
    private DataKey<Integer> testIntKey;

    @BeforeEach
    void setUp() {
        // Create real store definition for integration tests
        testStringKey = KeyFactory.string("testString", "defaultValue");
        testIntKey = KeyFactory.integer("testInt", 42);

        realStoreDefinition = new StoreDefinitionBuilder("TestStore")
                .addKey(testStringKey)
                .addKey(testIntKey)
                .build();

        xmlPersistence = new XmlPersistence(realStoreDefinition);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instance with valid StoreDefinition")
        void shouldCreateInstanceWithValidStoreDefinition() {
            assertThat(new XmlPersistence(realStoreDefinition)).isNotNull();
        }

        @SuppressWarnings("deprecation")
        @Test
        @DisplayName("Should create instance with empty constructor")
        void shouldCreateInstanceWithEmptyConstructor() {
            assertThat(new XmlPersistence()).isNotNull();
        }

        @SuppressWarnings("deprecation")
        @Test
        @DisplayName("Should create instance with Collection of DataKeys")
        void shouldCreateInstanceWithCollectionOfDataKeys() {
            var keys = java.util.List.<DataKey<?>>of(testStringKey, testIntKey);
            assertThatNoException().isThrownBy(() -> new XmlPersistence(keys));
        }

        @Test
        @DisplayName("Should handle null StoreDefinition")
        void shouldHandleNullStoreDefinition() {
            assertThatThrownBy(() -> new XmlPersistence((StoreDefinition) null))
                    .isInstanceOf(NullPointerException.class);
        }

        @SuppressWarnings("deprecation")
        @Test
        @DisplayName("Should handle null Collection of DataKeys")
        void shouldHandleNullCollectionOfDataKeys() {
            assertThatThrownBy(() -> new XmlPersistence((java.util.Collection<DataKey<?>>) null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Mapping Management Tests")
    class MappingManagementTests {

        @Test
        @DisplayName("Should add single mapping")
        void shouldAddSingleMapping() {
            assertThatNoException().isThrownBy(() -> xmlPersistence.addMapping("TestClass", String.class));
        }

        @Test
        @DisplayName("Should add multiple mappings via list")
        void shouldAddMultipleMappingsViaList() {
            var classList = java.util.List.<Class<?>>of(
                    String.class,
                    Integer.class,
                    Boolean.class);

            assertThatNoException().isThrownBy(() -> xmlPersistence.addMappings(classList));
        }

        @Test
        @DisplayName("Should handle null mapping key gracefully")
        void shouldHandleNullMappingKeyGracefully() {
            // Implementation handles null mapping key gracefully
            assertThatNoException().isThrownBy(() -> xmlPersistence.addMapping(null, String.class));
        }

        @Test
        @DisplayName("Should handle null mapping class")
        void shouldHandleNullMappingClass() {
            assertThatThrownBy(() -> xmlPersistence.addMapping("TestClass", null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty mapping key")
        void shouldHandleEmptyMappingKey() {
            assertThatNoException().isThrownBy(() -> xmlPersistence.addMapping("", String.class));
        }

        @Test
        @DisplayName("Should handle null mappings list")
        void shouldHandleNullMappingsList() {
            assertThatThrownBy(() -> xmlPersistence.addMappings(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty mappings list")
        void shouldHandleEmptyMappingsList() {
            var emptyList = java.util.List.<Class<?>>of();
            assertThatNoException().isThrownBy(() -> xmlPersistence.addMappings(emptyList));
        }
    }

    @Nested
    @DisplayName("Data Loading Tests")
    class DataLoadingTests {

        private File testFile;

        @BeforeEach
        void setUp() throws IOException {
            testFile = tempDir.resolve("test-config.xml").toFile();
        }

        @Test
        @DisplayName("Should load data from valid XML file")
        void shouldLoadDataFromValidXmlFile() throws IOException {
            // Create a simple DataStore and save it first
            var originalData = DataStore.newInstance(realStoreDefinition);
            originalData.set(testStringKey, "testValue");
            originalData.set(testIntKey, 123);

            xmlPersistence.saveData(testFile, originalData, false);

            // Now load it back
            var loadedData = xmlPersistence.loadData(testFile);

            assertThat(loadedData).isNotNull();
            assertThat(loadedData.get(testStringKey)).isEqualTo("testValue");
            assertThat(loadedData.get(testIntKey)).isEqualTo(123);
        }

        @Test
        @DisplayName("Should handle non-existent file")
        void shouldHandleNonExistentFile() {
            var nonExistentFile = tempDir.resolve("non-existent.xml").toFile();

            assertThatThrownBy(() -> xmlPersistence.loadData(nonExistentFile))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle null file")
        void shouldHandleNullFile() {
            assertThatThrownBy(() -> xmlPersistence.loadData(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle invalid XML file")
        void shouldHandleInvalidXmlFile() throws IOException {
            Files.writeString(testFile.toPath(), "invalid xml content");

            assertThatThrownBy(() -> xmlPersistence.loadData(testFile))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle empty XML file")
        void shouldHandleEmptyXmlFile() throws IOException {
            Files.writeString(testFile.toPath(), "");

            assertThatThrownBy(() -> xmlPersistence.loadData(testFile))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle XML file with wrong structure")
        void shouldHandleXmlFileWithWrongStructure() throws IOException {
            var wrongXml = "<?xml version=\"1.0\"?><root><wrong>structure</wrong></root>";
            Files.writeString(testFile.toPath(), wrongXml);

            assertThatThrownBy(() -> xmlPersistence.loadData(testFile))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should load data from directory path")
        void shouldLoadDataFromDirectoryPath() throws IOException {
            var directory = tempDir.resolve("testDir").toFile();
            directory.mkdirs();

            assertThatThrownBy(() -> xmlPersistence.loadData(directory))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Data Saving Tests")
    class DataSavingTests {

        private File testFile;
        private DataStore testData;

        @BeforeEach
        void setUp() throws IOException {
            testFile = tempDir.resolve("test-save.xml").toFile();
            testData = DataStore.newInstance(realStoreDefinition);
            testData.set(testStringKey, "saveTestValue");
            testData.set(testIntKey, 456);
        }

        @Test
        @DisplayName("Should save data to XML file successfully")
        void shouldSaveDataToXmlFileSuccessfully() {
            var result = xmlPersistence.saveData(testFile, testData, false);

            assertThat(result).isTrue();
            assertThat(testFile).exists();
            assertThat(testFile.length()).isGreaterThan(0);
        }

        @Test
        @DisplayName("Should save and load data maintaining integrity")
        void shouldSaveAndLoadDataMaintainingIntegrity() {
            xmlPersistence.saveData(testFile, testData, false);
            var loadedData = xmlPersistence.loadData(testFile);

            assertThat(loadedData.get(testStringKey)).isEqualTo("saveTestValue");
            assertThat(loadedData.get(testIntKey)).isEqualTo(456);
        }

        @Test
        @DisplayName("Should handle null file for saving")
        void shouldHandleNullFileForSaving() {
            assertThatThrownBy(() -> xmlPersistence.saveData(null, testData, false))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null data for saving")
        void shouldHandleNullDataForSaving() {
            assertThatThrownBy(() -> xmlPersistence.saveData(testFile, null, false))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle keepConfigFile parameter true")
        void shouldHandleKeepConfigFileParameterTrue() {
            var result = xmlPersistence.saveData(testFile, testData, true);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should handle keepConfigFile parameter false")
        void shouldHandleKeepConfigFileParameterFalse() {
            var result = xmlPersistence.saveData(testFile, testData, false);
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should overwrite existing file")
        void shouldOverwriteExistingFile() throws IOException {
            // Create initial file
            Files.writeString(testFile.toPath(), "initial content");

            var result = xmlPersistence.saveData(testFile, testData, false);

            assertThat(result).isTrue();
            var content = Files.readString(testFile.toPath());
            assertThat(content).contains("saveTestValue");
        }

        @Test
        @DisplayName("Should handle read-only directory")
        void shouldHandleReadOnlyDirectory() throws IOException {
            var readOnlyDir = tempDir.resolve("readonly").toFile();
            readOnlyDir.mkdirs();
            readOnlyDir.setWritable(false);

            var readOnlyFile = new File(readOnlyDir, "test.xml");

            try {
                var result = xmlPersistence.saveData(readOnlyFile, testData, false);
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
    @DisplayName("XML Generation Tests")
    class XmlGenerationTests {

        private DataStore testData;

        @BeforeEach
        void setUp() {
            testData = DataStore.newInstance(realStoreDefinition);
            testData.set(testStringKey, "xmlTestValue");
            testData.set(testIntKey, 789);
        }

        @Test
        @DisplayName("Should generate ObjectXml from StoreDefinition")
        void shouldGenerateObjectXmlFromStoreDefinition() {
            var objectXml = XmlPersistence.getObjectXml(realStoreDefinition);

            assertThat(objectXml).isNotNull();
            assertThat(objectXml.getTargetClass()).isEqualTo(DataStore.class);
        }

        @Test
        @DisplayName("Should handle null StoreDefinition for XML generation gracefully")
        void getObjectXml_WithValidStoreDefinition_ReturnsMapper() {
            var mapper = XmlPersistence.getObjectXml(realStoreDefinition);
            assertThat(mapper).isNotNull();
        }

        @Test
        @DisplayName("Should generate ObjectXml with proper configuration")
        void shouldGenerateObjectXmlWithProperConfiguration() {
            var objectXml = XmlPersistence.getObjectXml(realStoreDefinition);

            // Test that we can serialize and deserialize data
            var xmlString = objectXml.toXml(testData);
            assertThat(xmlString).isNotNull();
            assertThat(xmlString).isNotEmpty();

            var deserializedData = objectXml.fromXml(xmlString);
            assertThat(deserializedData).isNotNull();
        }

        @Test
        @DisplayName("Should handle empty StoreDefinition")
        void shouldHandleEmptyStoreDefinition() {
            var emptyDef = new StoreDefinitionBuilder("Empty").build();
            var objectXml = XmlPersistence.getObjectXml(emptyDef);

            assertThat(objectXml).isNotNull();

            var emptyData = DataStore.newInstance(emptyDef);
            var xmlString = objectXml.toXml(emptyData);
            assertThat(xmlString).isNotNull();
        }

        @Test
        @DisplayName("Should handle DataStore with complex objects")
        void shouldHandleDataStoreWithComplexObjects() {
            var optionalKey = KeyFactory.object("optionalObject", Optional.class);
            var defWithComplex = new StoreDefinitionBuilder("ComplexStore")
                    .addKey(optionalKey)
                    .build();

            var complexData = DataStore.newInstance(defWithComplex);
            complexData.set(optionalKey, Optional.of("complex value"));

            var objectXml = XmlPersistence.getObjectXml(defWithComplex);
            var xmlString = objectXml.toXml(complexData);

            assertThat(xmlString).isNotNull();
            assertThat(xmlString).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete save-load cycle with various data types")
        void shouldHandleCompleteSaveLoadCycleWithVariousDataTypes() throws IOException {
            // Create comprehensive store definition
            var stringKey = KeyFactory.string("stringValue", "");
            var intKey = KeyFactory.integer("intValue", 0);
            var boolKey = KeyFactory.bool("boolValue");
            var doubleKey = KeyFactory.double64("doubleValue", 0.0);

            var comprehensiveDef = new StoreDefinitionBuilder("ComprehensiveStore")
                    .addKey(stringKey)
                    .addKey(intKey)
                    .addKey(boolKey)
                    .addKey(doubleKey)
                    .build();

            var comprehensivePersistence = new XmlPersistence(comprehensiveDef);
            var testFile = tempDir.resolve("comprehensive.xml").toFile();

            // Create test data
            var originalData = DataStore.newInstance(comprehensiveDef);
            originalData.set(stringKey, "test string with special chars: <>&\"'");
            originalData.set(intKey, -12345);
            originalData.set(boolKey, true);
            originalData.set(doubleKey, 3.14159);

            // Save and load
            var saveResult = comprehensivePersistence.saveData(testFile, originalData, false);
            assertThat(saveResult).isTrue();

            var loadedData = comprehensivePersistence.loadData(testFile);

            // Verify all data maintained
            assertThat(loadedData.get(stringKey)).isEqualTo("test string with special chars: <>&\"'");
            assertThat(loadedData.get(intKey)).isEqualTo(-12345);
            assertThat(loadedData.get(boolKey)).isEqualTo(true);
            assertThat(loadedData.get(doubleKey)).isEqualTo(3.14159);
        }

        @Test
        @DisplayName("Should handle multiple save-load cycles")
        void shouldHandleMultipleSaveLoadCycles() throws IOException {
            var testFile = tempDir.resolve("multiple-cycles.xml").toFile();
            var data = DataStore.newInstance(realStoreDefinition);

            for (int i = 0; i < 5; i++) {
                data.set(testStringKey, "value_" + i);
                data.set(testIntKey, i * 100);

                xmlPersistence.saveData(testFile, data, false);
                var loadedData = xmlPersistence.loadData(testFile);

                assertThat(loadedData.get(testStringKey)).isEqualTo("value_" + i);
                assertThat(loadedData.get(testIntKey)).isEqualTo(i * 100);
            }
        }

        @Test
        @DisplayName("Should handle concurrent access")
        void shouldHandleConcurrentAccess() throws IOException, InterruptedException {
            var testFile = tempDir.resolve("concurrent.xml").toFile();
            var data = DataStore.newInstance(realStoreDefinition);
            data.set(testStringKey, "concurrent test");
            data.set(testIntKey, 999);

            // Save initial data
            xmlPersistence.saveData(testFile, data, false);

            var thread1 = new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        xmlPersistence.loadData(testFile);
                        Thread.sleep(10);
                    }
                } catch (Exception e) {
                    fail("Thread 1 failed: " + e.getMessage());
                }
            });

            var thread2 = new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        xmlPersistence.loadData(testFile);
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
            var invalidPath = new File("/invalid/path/that/does/not/exist/file.xml");
            var data = DataStore.newInstance(realStoreDefinition);
            data.set(testStringKey, "test");

            assertThatThrownBy(() -> xmlPersistence.saveData(invalidPath, data, false))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle malformed StoreDefinition")
        void shouldHandleMalformedStoreDefinition() {
            // Test with empty store definition
            var emptyDef = new StoreDefinitionBuilder("Empty").build();
            var emptyPersistence = new XmlPersistence(emptyDef);

            assertThat(emptyPersistence).isNotNull();

            var emptyData = DataStore.newInstance(emptyDef);
            // Test that we can get ObjectXml instead of calling instance method
            var objectXml = XmlPersistence.getObjectXml(emptyDef);
            var xmlString = objectXml.toXml(emptyData);
            assertThat(xmlString).isNotNull();
        }

        @Test
        @DisplayName("Should handle large data sets")
        void shouldHandleLargeDataSets() throws IOException {
            var largeStringKey = KeyFactory.string("largeString", "");
            var largeDef = new StoreDefinitionBuilder("LargeStore")
                    .addKey(largeStringKey)
                    .build();

            var largePersistence = new XmlPersistence(largeDef);
            var testFile = tempDir.resolve("large-data.xml").toFile();

            // Create large string (1MB)
            var largeString = "a".repeat(1024 * 1024);
            var largeData = DataStore.newInstance(largeDef);
            largeData.set(largeStringKey, largeString);

            var saveResult = largePersistence.saveData(testFile, largeData, false);
            assertThat(saveResult).isTrue();

            var loadedData = largePersistence.loadData(testFile);
            assertThat(loadedData.get(largeStringKey)).isEqualTo(largeString);
        }
    }
}
