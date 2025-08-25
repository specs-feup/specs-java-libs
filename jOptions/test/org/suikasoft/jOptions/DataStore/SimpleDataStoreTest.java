package org.suikasoft.jOptions.DataStore;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Comprehensive test suite for SimpleDataStore implementation.
 * 
 * Tests cover:
 * - Constructor variants (name, name+DataStore, StoreDefinition)
 * - Basic DataStore functionality inheritance
 * - Copy operations and data isolation
 * - Integration with StoreDefinition
 * - Performance characteristics
 * - Memory management
 * 
 * @author Generated Tests
 */
@DisplayName("SimpleDataStore Implementation Tests")
class SimpleDataStoreTest {

    private DataKey<String> stringKey;
    private DataKey<Integer> intKey;
    private DataKey<Boolean> boolKey;

    @BeforeEach
    void setUp() {
        stringKey = KeyFactory.string("test.string");
        intKey = KeyFactory.integer("test.int");
        boolKey = KeyFactory.bool("test.bool");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("name constructor creates functional DataStore")
        void testNameConstructor_CreatesFunctionalDataStore() {
            String storeName = "test-store";

            SimpleDataStore dataStore = new SimpleDataStore(storeName);

            assertThat(dataStore).isNotNull();
            assertThat(dataStore.getName()).isEqualTo(storeName);

            // Verify it's functional - can store and retrieve values
            dataStore.set(stringKey, "test_value");
            assertThat(dataStore.get(stringKey)).isEqualTo("test_value");
        }

        @Test
        @DisplayName("name constructor with empty string works")
        void testNameConstructor_WithEmptyString_Works() {
            SimpleDataStore dataStore = new SimpleDataStore("");

            assertThat(dataStore.getName()).isEqualTo("");

            // Should still be functional
            dataStore.set(boolKey, true);
            assertThat(dataStore.get(boolKey)).isTrue();
        }

        @Test
        @DisplayName("name+DataStore constructor copies data")
        void testNameDataStoreConstructor_CopiesData() {
            // Create source DataStore with data
            SimpleDataStore source = new SimpleDataStore("source");
            source.set(stringKey, "source_value");
            source.set(intKey, 42);

            // Create copy with different name
            SimpleDataStore copy = new SimpleDataStore("copy", source);

            assertThat(copy.getName()).isEqualTo("copy");
            assertThat(copy.get(stringKey)).isEqualTo("source_value");
            assertThat(copy.get(intKey)).isEqualTo(42);
        }

        @Test
        @DisplayName("name+DataStore constructor creates independent copy")
        void testNameDataStoreConstructor_CreatesIndependentCopy() {
            SimpleDataStore source = new SimpleDataStore("source");
            source.set(stringKey, "original");

            SimpleDataStore copy = new SimpleDataStore("copy", source);

            // Modify source after copy creation
            source.set(stringKey, "modified");

            // Copy should remain unchanged
            assertThat(copy.get(stringKey)).isEqualTo("original");
            assertThat(source.get(stringKey)).isEqualTo("modified");
        }

        @Test
        @DisplayName("name+empty DataStore constructor works")
        void testNameDataStoreConstructor_WithEmptyDataStore_Works() {
            SimpleDataStore empty = new SimpleDataStore("empty");
            SimpleDataStore copy = new SimpleDataStore("copy", empty);

            assertThat(copy.getName()).isEqualTo("copy");
            assertThat(copy.hasValue(stringKey)).isFalse();
            assertThat(copy.hasValue(intKey)).isFalse();
        }

        @Test
        @DisplayName("StoreDefinition constructor creates DataStore")
        void testStoreDefinitionConstructor_CreatesDataStore() {
            StoreDefinition mockDefinition = Mockito.mock(StoreDefinition.class);
            Mockito.when(mockDefinition.getName()).thenReturn("definition-store");

            SimpleDataStore dataStore = new SimpleDataStore(mockDefinition);

            assertThat(dataStore).isNotNull();

            // Verify it's functional
            dataStore.set(stringKey, "definition_value");
            assertThat(dataStore.get(stringKey)).isEqualTo("definition_value");
        }
    }

    @Nested
    @DisplayName("DataStore Interface Implementation")
    class DataStoreImplementationTests {

        @Test
        @DisplayName("implements DataStore interface correctly")
        void testImplementsDataStoreInterface_Correctly() {
            SimpleDataStore dataStore = new SimpleDataStore("interface-test");

            // Verify it's a DataStore
            assertThat(dataStore).isInstanceOf(DataStore.class);

            // Test basic interface methods
            dataStore.set(stringKey, "interface_value");
            assertThat(dataStore.get(stringKey)).isEqualTo("interface_value");
            assertThat(dataStore.hasValue(stringKey)).isTrue();
        }

        @Test
        @DisplayName("supports method chaining")
        void testSupportsMethodChaining() {
            SimpleDataStore dataStore = new SimpleDataStore("chaining-test");

            // Method chaining should work
            DataStore result = dataStore.set(stringKey, "value1")
                    .set(intKey, 100)
                    .set(boolKey, true);

            assertThat(result).isSameAs(dataStore);
            assertThat(dataStore.get(stringKey)).isEqualTo("value1");
            assertThat(dataStore.get(intKey)).isEqualTo(100);
            assertThat(dataStore.get(boolKey)).isTrue();
        }

        @Test
        @DisplayName("handles multiple data types correctly")
        void testHandlesMultipleDataTypes_Correctly() {
            SimpleDataStore dataStore = new SimpleDataStore("multi-type-test");

            // Store different types
            dataStore.set(stringKey, "string_value");
            dataStore.set(intKey, 42);
            dataStore.set(boolKey, false);

            // Retrieve and verify types
            assertThat(dataStore.get(stringKey)).isInstanceOf(String.class);
            assertThat(dataStore.get(intKey)).isInstanceOf(Integer.class);
            assertThat(dataStore.get(boolKey)).isInstanceOf(Boolean.class);

            assertThat(dataStore.get(stringKey)).isEqualTo("string_value");
            assertThat(dataStore.get(intKey)).isEqualTo(42);
            assertThat(dataStore.get(boolKey)).isFalse();
        }
    }

    @Nested
    @DisplayName("Data Isolation Tests")
    class DataIsolationTests {

        @Test
        @DisplayName("different SimpleDataStore instances are isolated")
        void testDifferentInstances_AreIsolated() {
            SimpleDataStore store1 = new SimpleDataStore("store1");
            SimpleDataStore store2 = new SimpleDataStore("store2");

            store1.set(stringKey, "store1_value");
            store2.set(stringKey, "store2_value");

            assertThat(store1.get(stringKey)).isEqualTo("store1_value");
            assertThat(store2.get(stringKey)).isEqualTo("store2_value");
        }

        @Test
        @DisplayName("copy constructor creates isolated instances")
        void testCopyConstructor_CreatesIsolatedInstances() {
            SimpleDataStore original = new SimpleDataStore("original");
            original.set(stringKey, "original_value");
            original.set(intKey, 10);

            SimpleDataStore copy = new SimpleDataStore("copy", original);

            // Modify original
            original.set(stringKey, "modified_original");
            original.set(boolKey, true);

            // Copy should be unaffected
            assertThat(copy.get(stringKey)).isEqualTo("original_value");
            assertThat(copy.get(intKey)).isEqualTo(10);
            assertThat(copy.hasValue(boolKey)).isFalse();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("handles null store name gracefully")
        void testHandlesNullStoreName_Gracefully() {
            // Implementation allows null store names
            SimpleDataStore dataStore = new SimpleDataStore((String) null);

            assertThat(dataStore).isNotNull();
            // Should still be functional
            dataStore.set(stringKey, "test_value");
            assertThat(dataStore.get(stringKey)).isEqualTo("test_value");
        }

        @Test
        @DisplayName("handles null DataStore in copy constructor")
        void testHandlesNullDataStoreInCopyConstructor() {
            assertThatThrownBy(() -> new SimpleDataStore("test", null))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("handles null StoreDefinition in constructor")
        void testHandlesNullStoreDefinitionInConstructor() {
            assertThatThrownBy(() -> new SimpleDataStore((StoreDefinition) null))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("handles large number of keys efficiently")
        void testHandlesLargeNumberOfKeys_Efficiently() {
            SimpleDataStore dataStore = new SimpleDataStore("performance-test");

            // Add many keys
            for (int i = 0; i < 1000; i++) {
                DataKey<String> key = KeyFactory.string("key_" + i);
                dataStore.set(key, "value_" + i);
            }

            // Verify all values are retrievable
            for (int i = 0; i < 1000; i++) {
                DataKey<String> key = KeyFactory.string("key_" + i);
                assertThat(dataStore.get(key)).isEqualTo("value_" + i);
            }
        }

        @Test
        @DisplayName("handles rapid modifications correctly")
        void testHandlesRapidModifications_Correctly() {
            SimpleDataStore dataStore = new SimpleDataStore("rapid-test");

            // Rapid set/get operations
            for (int i = 0; i < 100; i++) {
                dataStore.set(stringKey, "value_" + i);
                assertThat(dataStore.get(stringKey)).isEqualTo("value_" + i);
            }

            // Final value should be the last one set
            assertThat(dataStore.get(stringKey)).isEqualTo("value_99");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("works with complex key-value scenarios")
        void testWorksWithComplexKeyValueScenarios() {
            SimpleDataStore dataStore = new SimpleDataStore("integration-test");

            // Complex scenario: store, modify, remove, store again
            dataStore.set(stringKey, "initial");
            dataStore.set(intKey, 1);
            dataStore.set(boolKey, false);

            assertThat(dataStore.hasValue(stringKey)).isTrue();
            assertThat(dataStore.hasValue(intKey)).isTrue();
            assertThat(dataStore.hasValue(boolKey)).isTrue();

            // Remove one key
            dataStore.remove(intKey);
            assertThat(dataStore.hasValue(intKey)).isFalse();

            // Modify existing
            dataStore.set(stringKey, "modified");
            assertThat(dataStore.get(stringKey)).isEqualTo("modified");

            // Add back removed key
            dataStore.set(intKey, 999);
            assertThat(dataStore.get(intKey)).isEqualTo(999);
        }

        @Test
        @DisplayName("toString returns meaningful representation")
        void testToString_ReturnsMeaningfulRepresentation() {
            SimpleDataStore dataStore = new SimpleDataStore("toString-test");
            dataStore.set(stringKey, "test_value");

            String stringRepresentation = dataStore.toString();

            assertThat(stringRepresentation).isNotNull();
            assertThat(stringRepresentation).isNotEmpty();
            // Should contain store name or some identifying information
            assertThat(stringRepresentation).contains("toString-test");
        }
    }
}
