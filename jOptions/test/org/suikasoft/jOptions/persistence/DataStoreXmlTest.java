package org.suikasoft.jOptions.persistence;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionBuilder;

import pt.up.fe.specs.util.utilities.StringList;

/**
 * Test class for DataStoreXml.
 * 
 * @author Generated Tests
 */
@DisplayName("DataStoreXml Tests")
class DataStoreXmlTest {

    @TempDir
    Path tempDir;

    private DataStoreXml dataStoreXml;
    private StoreDefinition storeDefinition;
    private DataKey<String> testStringKey;
    private DataKey<Integer> testIntKey;
    private DataKey<StringList> testStringListKey;

    @BeforeEach
    void setUp() {
        // Create store definition for tests
        testStringKey = KeyFactory.string("testString", "defaultString");
        testIntKey = KeyFactory.integer("testInt", 42);
        testStringListKey = KeyFactory.object("testStringList", StringList.class);

        storeDefinition = new StoreDefinitionBuilder("TestStore")
                .addKey(testStringKey)
                .addKey(testIntKey)
                .addKey(testStringListKey)
                .build();

        dataStoreXml = new DataStoreXml(storeDefinition);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create instance with valid StoreDefinition")
        void shouldCreateInstanceWithValidStoreDefinition() {
            assertThat(new DataStoreXml(storeDefinition)).isNotNull();
        }

        @Test
        @DisplayName("Should handle null StoreDefinition")
        void shouldHandleNullStoreDefinition() {
            // DataStoreXml should reject null StoreDefinition with NullPointerException
            assertThatThrownBy(() -> new DataStoreXml(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("StoreDefinition cannot be null");
        }

        @Test
        @DisplayName("Should handle empty StoreDefinition")
        void shouldHandleEmptyStoreDefinition() {
            var emptyDef = new StoreDefinitionBuilder("Empty").build();
            assertThatNoException().isThrownBy(() -> new DataStoreXml(emptyDef));
        }

        @Test
        @DisplayName("Should configure built-in library classes")
        void shouldConfigureBuiltInLibraryClasses() {
            var xml = new DataStoreXml(storeDefinition);
            assertThat(xml).isNotNull();
            // Should have built-in mappings for StringList and SimpleDataStore
            assertThat(xml.getTargetClass()).isEqualTo(DataStore.class);
        }
    }

    @Nested
    @DisplayName("Target Class Tests")
    class TargetClassTests {

        @Test
        @DisplayName("Should return DataStore as target class")
        void shouldReturnDataStoreAsTargetClass() {
            assertThat(dataStoreXml.getTargetClass()).isEqualTo(DataStore.class);
        }

        @Test
        @DisplayName("Should have consistent target class across instances")
        void shouldHaveConsistentTargetClassAcrossInstances() {
            var xml1 = new DataStoreXml(storeDefinition);
            var xml2 = new DataStoreXml(storeDefinition);

            assertThat(xml1.getTargetClass()).isEqualTo(xml2.getTargetClass());
            assertThat(xml1.getTargetClass()).isEqualTo(DataStore.class);
        }
    }

    @Nested
    @DisplayName("XML Serialization Tests")
    class XmlSerializationTests {

        private DataStore testData;

        @BeforeEach
        void setUp() {
            testData = DataStore.newInstance(storeDefinition);
            testData.set(testStringKey, "Test Value");
            testData.set(testIntKey, 123);

            var stringList = new StringList(Arrays.asList("item1", "item2", "item3"));
            testData.set(testStringListKey, stringList);
        }

        @Test
        @DisplayName("Should serialize DataStore to XML")
        void shouldSerializeDataStoreToXml() {
            var xmlString = dataStoreXml.toXml(testData);

            assertThat(xmlString).isNotNull();
            assertThat(xmlString).isNotEmpty();
            // DataStoreXml uses custom format with square brackets, not standard XML
            assertThat(xmlString).contains("SimpleDataStore");
            assertThat(xmlString).contains("Test Value");
            assertThat(xmlString).contains("123");
        }

        @Test
        @DisplayName("Should handle null DataStore for serialization")
        void shouldHandleNullDataStoreForSerialization() {
            // DataStoreXml should reject null DataStore with NullPointerException
            assertThatThrownBy(() -> dataStoreXml.toXml(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("DataStore cannot be null");
        }

        @Test
        @DisplayName("Should serialize empty DataStore")
        void shouldSerializeEmptyDataStore() {
            var emptyData = DataStore.newInstance(storeDefinition);
            var xmlString = dataStoreXml.toXml(emptyData);

            assertThat(xmlString).isNotNull();
            assertThat(xmlString).isNotEmpty();
            // DataStoreXml uses custom format with square brackets, not standard XML
            assertThat(xmlString).contains("SimpleDataStore");
        }

        @Test
        @DisplayName("Should serialize DataStore with StringList")
        void shouldSerializeDataStoreWithStringList() {
            var xmlString = dataStoreXml.toXml(testData);

            assertThat(xmlString).isNotNull();
            assertThat(xmlString).contains("item1");
            assertThat(xmlString).contains("item2");
            assertThat(xmlString).contains("item3");
        }

        @Test
        @DisplayName("Should serialize DataStore with special characters")
        void shouldSerializeDataStoreWithSpecialCharacters() {
            testData.set(testStringKey, "Special <chars> & \"quotes\"");
            var xmlString = dataStoreXml.toXml(testData);

            assertThat(xmlString).isNotNull();
            // Custom XML format should handle special characters
            assertThat(xmlString).isNotEmpty();
            assertThat(xmlString).contains("Special");
        }
    }

    @Nested
    @DisplayName("XML Deserialization Tests")
    class XmlDeserializationTests {

        @Test
        @DisplayName("Should deserialize XML to DataStore")
        void shouldDeserializeXmlToDataStore() {
            var originalData = DataStore.newInstance(storeDefinition);
            originalData.set(testStringKey, "Deserialization Test");
            originalData.set(testIntKey, 456);

            var xmlString = dataStoreXml.toXml(originalData);
            var deserializedData = dataStoreXml.fromXml(xmlString);

            assertThat(deserializedData).isNotNull();
            assertThat(deserializedData.get(testStringKey)).isEqualTo("Deserialization Test");
            assertThat(deserializedData.get(testIntKey)).isEqualTo(456);
        }

        @Test
        @DisplayName("Should handle null XML string for deserialization")
        void shouldHandleNullXmlStringForDeserialization() {
            assertThatThrownBy(() -> dataStoreXml.fromXml(null))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle empty XML string for deserialization")
        void shouldHandleEmptyXmlStringForDeserialization() {
            assertThatThrownBy(() -> dataStoreXml.fromXml(""))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle invalid XML for deserialization")
        void shouldHandleInvalidXmlForDeserialization() {
            assertThatThrownBy(() -> dataStoreXml.fromXml("invalid xml content"))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should deserialize DataStore with StringList")
        void shouldDeserializeDataStoreWithStringList() {
            var originalData = DataStore.newInstance(storeDefinition);
            var originalList = new StringList(Arrays.asList("alpha", "beta", "gamma"));
            originalData.set(testStringListKey, originalList);

            var xmlString = dataStoreXml.toXml(originalData);
            var deserializedData = dataStoreXml.fromXml(xmlString);

            assertThat(deserializedData).isNotNull();
            var deserializedList = deserializedData.get(testStringListKey);
            assertThat(deserializedList).isNotNull();
            assertThat(deserializedList.getStringList().size()).isEqualTo(3);
            assertThat(deserializedList.getStringList().get(0)).isEqualTo("alpha");
            assertThat(deserializedList.getStringList().get(1)).isEqualTo("beta");
            assertThat(deserializedList.getStringList().get(2)).isEqualTo("gamma");
        }

        @Test
        @DisplayName("Should deserialize XML with malformed structure")
        void shouldDeserializeXmlWithMalformedStructure() {
            var malformedXml = "[root][wrong]structure[/wrong][/root]";

            assertThatThrownBy(() -> dataStoreXml.fromXml(malformedXml))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Library Classes Tests")
    class LibraryClassesTests {

        @Test
        @DisplayName("Should handle StringList serialization")
        void shouldHandleStringListSerialization() {
            var data = DataStore.newInstance(storeDefinition);
            var stringList = new StringList();
            stringList.getStringList().add("first");
            stringList.getStringList().add("second");
            stringList.getStringList().add("third");
            data.set(testStringListKey, stringList);

            var xmlString = dataStoreXml.toXml(data);
            var deserializedData = dataStoreXml.fromXml(xmlString);

            var deserializedList = deserializedData.get(testStringListKey);
            assertThat(deserializedList).isNotNull();
            assertThat(deserializedList.getStringList().size()).isEqualTo(3);
            assertThat(deserializedList.getStringList().get(0)).isEqualTo("first");
            assertThat(deserializedList.getStringList().get(1)).isEqualTo("second");
            assertThat(deserializedList.getStringList().get(2)).isEqualTo("third");
        }

        @Test
        @DisplayName("Should handle empty StringList")
        void shouldHandleEmptyStringList() {
            var data = DataStore.newInstance(storeDefinition);
            data.set(testStringListKey, new StringList());

            var xmlString = dataStoreXml.toXml(data);
            var deserializedData = dataStoreXml.fromXml(xmlString);

            var deserializedList = deserializedData.get(testStringListKey);
            assertThat(deserializedList).isNotNull();
            assertThat(deserializedList.getStringList().size()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle SimpleDataStore as nested structure")
        void shouldHandleSimpleDataStoreAsNestedStructure() {
            // Create a nested SimpleDataStore
            var nestedDataKey = KeyFactory.object("nestedData", SimpleDataStore.class);
            var defWithNested = new StoreDefinitionBuilder("NestedStore")
                    .addKey(testStringKey)
                    .addKey(nestedDataKey)
                    .build();

            var xmlWithNested = new DataStoreXml(defWithNested);
            var data = DataStore.newInstance(defWithNested);
            data.set(testStringKey, "Parent Value");

            var nestedData = new SimpleDataStore("NestedSimple");
            nestedData.setRaw("nestedKey", "nestedValue");
            data.set(nestedDataKey, nestedData);

            var xmlString = xmlWithNested.toXml(data);
            var deserializedData = xmlWithNested.fromXml(xmlString);

            assertThat(deserializedData).isNotNull();
            assertThat(deserializedData.get(testStringKey)).isEqualTo("Parent Value");
            var deserializedNested = deserializedData.get(nestedDataKey);
            assertThat(deserializedNested).isNotNull();
            assertThat(deserializedNested.getName()).isEqualTo("NestedSimple");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete round-trip with all supported types")
        void shouldHandleCompleteRoundTripWithAllSupportedTypes() throws IOException {
            var comprehensiveData = DataStore.newInstance(storeDefinition);
            comprehensiveData.set(testStringKey, "Integration test value");
            comprehensiveData.set(testIntKey, 999);

            var stringList = new StringList();
            stringList.getStringList().add("list_item_1");
            stringList.getStringList().add("list_item_2");
            stringList.getStringList().add("list_item_3");
            comprehensiveData.set(testStringListKey, stringList);

            // Test XML round-trip
            var xmlString = dataStoreXml.toXml(comprehensiveData);
            var loadedData = dataStoreXml.fromXml(xmlString);

            assertThat(loadedData.get(testStringKey)).isEqualTo("Integration test value");
            assertThat(loadedData.get(testIntKey)).isEqualTo(999);

            var loadedList = loadedData.get(testStringListKey);
            assertThat(loadedList.getStringList().size()).isEqualTo(3);
            assertThat(loadedList.getStringList().get(0)).isEqualTo("list_item_1");
            assertThat(loadedList.getStringList().get(1)).isEqualTo("list_item_2");
            assertThat(loadedList.getStringList().get(2)).isEqualTo("list_item_3");
        }

        @Test
        @DisplayName("Should handle multiple serialization-deserialization cycles")
        void shouldHandleMultipleSerializationDeserializationCycles() {
            var data = DataStore.newInstance(storeDefinition);

            for (int i = 0; i < 5; i++) {
                data.set(testStringKey, "cycle_" + i);
                data.set(testIntKey, i * 100);

                var xmlString = dataStoreXml.toXml(data);
                var deserializedData = dataStoreXml.fromXml(xmlString);

                assertThat(deserializedData.get(testStringKey)).isEqualTo("cycle_" + i);
                assertThat(deserializedData.get(testIntKey)).isEqualTo(i * 100);

                // Use deserialized data for next cycle
                data = deserializedData;
            }
        }

        @Test
        @DisplayName("Should handle concurrent access")
        void shouldHandleConcurrentAccess() throws InterruptedException {
            var data = DataStore.newInstance(storeDefinition);
            data.set(testStringKey, "concurrent test");
            data.set(testIntKey, 555);

            var thread1 = new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        var xml = dataStoreXml.toXml(data);
                        dataStoreXml.fromXml(xml);
                        Thread.sleep(10);
                    }
                } catch (Exception e) {
                    fail("Thread 1 failed: " + e.getMessage());
                }
            });

            var thread2 = new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        var xml = dataStoreXml.toXml(data);
                        dataStoreXml.fromXml(xml);
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
        @DisplayName("Should handle DataStore with unsupported types gracefully")
        void shouldHandleDataStoreWithUnsupportedTypesGracefully() {
            // Test with a complex object that might not be easily serializable
            var complexKey = KeyFactory.object("complexObject", Object.class);
            var defWithComplex = new StoreDefinitionBuilder("ComplexStore")
                    .addKey(complexKey)
                    .build();

            var xmlWithComplex = new DataStoreXml(defWithComplex);
            var data = DataStore.newInstance(defWithComplex);

            // Set a complex object
            data.set(complexKey, new Object());

            // Should handle serialization attempt
            assertThatNoException().isThrownBy(() -> {
                var xmlString = xmlWithComplex.toXml(data);
                assertThat(xmlString).isNotNull();
            });
        }

        @Test
        @DisplayName("Should handle corrupted XML gracefully")
        void shouldHandleCorruptedXmlGracefully() {
            var corruptedXml = "[data][incomplete>";

            assertThatThrownBy(() -> dataStoreXml.fromXml(corruptedXml))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle very large DataStore")
        void shouldHandleVeryLargeDataStore() {
            var largeStringList = new StringList();
            for (int i = 0; i < 1000; i++) {
                largeStringList.getStringList().add("item_" + i);
            }

            var data = DataStore.newInstance(storeDefinition);
            data.set(testStringKey, "Large test");
            data.set(testStringListKey, largeStringList);

            var xmlString = dataStoreXml.toXml(data);
            assertThat(xmlString).isNotNull();
            assertThat(xmlString.length()).isGreaterThan(10000); // Should be quite large

            var deserializedData = dataStoreXml.fromXml(xmlString);
            var deserializedList = deserializedData.get(testStringListKey);
            assertThat(deserializedList.getStringList().size()).isEqualTo(1000);
            assertThat(deserializedList.getStringList().get(999)).isEqualTo("item_999");
        }
    }
}
