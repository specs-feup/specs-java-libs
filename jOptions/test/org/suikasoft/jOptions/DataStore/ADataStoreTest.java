package org.suikasoft.jOptions.DataStore;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Comprehensive test suite for ADataStore abstract base class.
 * 
 * Tests cover:
 * - Abstract class functionality through concrete test implementation
 * - Constructor variants with different parameters
 * - Store definition management
 * - Persistence operations
 * - Value storage and retrieval mechanisms
 * - Strict mode behavior
 * - Configuration file handling
 * 
 * @author Generated Tests
 */
@DisplayName("ADataStore Abstract Base Class Tests")
class ADataStoreTest {

    /**
     * Concrete test implementation of ADataStore for testing purposes.
     */
    private static class TestDataStore extends ADataStore {
        public TestDataStore(String name) {
            super(name);
        }

        public TestDataStore(String name, DataStore dataStore) {
            super(name, dataStore);
        }

        public TestDataStore(StoreDefinition storeDefinition) {
            super(storeDefinition);
        }

        public TestDataStore(String name, Map<String, Object> values, StoreDefinition definition) {
            super(name, values, definition);
        }
    }

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

            TestDataStore dataStore = new TestDataStore(storeName);

            assertThat(dataStore).isNotNull();
            assertThat(dataStore.getName()).isEqualTo(storeName);

            // Verify basic functionality
            dataStore.set(stringKey, "test_value");
            assertThat(dataStore.get(stringKey)).isEqualTo("test_value");
        }

        @Test
        @DisplayName("name+DataStore constructor copies values")
        void testNameDataStoreConstructor_CopiesValues() {
            TestDataStore source = new TestDataStore("source");
            source.set(stringKey, "source_value");
            source.set(intKey, 42);

            TestDataStore copy = new TestDataStore("copy", source);

            assertThat(copy.getName()).isEqualTo("copy");
            assertThat(copy.get(stringKey)).isEqualTo("source_value");
            assertThat(copy.get(intKey)).isEqualTo(42);
        }

        @Test
        @DisplayName("StoreDefinition constructor sets up definition")
        void testStoreDefinitionConstructor_SetsUpDefinition() {
            StoreDefinition mockDefinition = Mockito.mock(StoreDefinition.class);
            Mockito.when(mockDefinition.getName()).thenReturn("definition-store");

            TestDataStore dataStore = new TestDataStore(mockDefinition);

            assertThat(dataStore).isNotNull();
            assertThat(dataStore.getStoreDefinitionTry()).isPresent();
        }

        @Test
        @DisplayName("full constructor with name, values, and definition")
        void testFullConstructor_WithNameValuesAndDefinition() {
            Map<String, Object> values = new HashMap<>();
            values.put(stringKey.getName(), "predefined_value");

            StoreDefinition mockDefinition = Mockito.mock(StoreDefinition.class);
            Mockito.when(mockDefinition.getName()).thenReturn("full-store");

            TestDataStore dataStore = new TestDataStore("full-store", values, mockDefinition);

            assertThat(dataStore.getName()).isEqualTo("full-store");
            assertThat(dataStore.get(stringKey.getName())).isEqualTo("predefined_value");
            assertThat(dataStore.getStoreDefinitionTry()).isPresent();
        }
    }

    @Nested
    @DisplayName("Core DataStore Functionality")
    class CoreFunctionalityTests {

        @Test
        @DisplayName("implements DataStore interface methods")
        void testImplementsDataStoreInterface_Methods() {
            TestDataStore dataStore = new TestDataStore("interface-test");

            // Verify it's a DataStore
            assertThat(dataStore).isInstanceOf(DataStore.class);

            // Test core methods
            dataStore.set(stringKey, "interface_value");
            assertThat(dataStore.get(stringKey)).isEqualTo("interface_value");
            assertThat(dataStore.hasValue(stringKey)).isTrue();

            dataStore.remove(stringKey);
            assertThat(dataStore.hasValue(stringKey)).isFalse();
        }

        @Test
        @DisplayName("getName returns correct store name")
        void testGetName_ReturnsCorrectStoreName() {
            String expectedName = "name-test-store";
            TestDataStore dataStore = new TestDataStore(expectedName);

            assertThat(dataStore.getName()).isEqualTo(expectedName);
        }

        @Test
        @DisplayName("handles value overwriting correctly")
        void testHandlesValueOverwriting_Correctly() {
            TestDataStore dataStore = new TestDataStore("overwrite-test");

            dataStore.set(stringKey, "initial_value");
            assertThat(dataStore.get(stringKey)).isEqualTo("initial_value");

            dataStore.set(stringKey, "updated_value");
            assertThat(dataStore.get(stringKey)).isEqualTo("updated_value");
        }

        @Test
        @DisplayName("handles multiple key types correctly")
        void testHandlesMultipleKeyTypes_Correctly() {
            TestDataStore dataStore = new TestDataStore("multi-key-test");

            dataStore.set(stringKey, "string_value");
            dataStore.set(intKey, 123);
            dataStore.set(boolKey, true);

            assertThat(dataStore.get(stringKey)).isEqualTo("string_value");
            assertThat(dataStore.get(intKey)).isEqualTo(123);
            assertThat(dataStore.get(boolKey)).isTrue();
        }
    }

    @Nested
    @DisplayName("Store Definition Management")
    class StoreDefinitionTests {

        @Test
        @DisplayName("getStoreDefinitionTry returns empty when no definition")
        void testGetStoreDefinitionTry_ReturnsEmptyWhenNoDefinition() {
            TestDataStore dataStore = new TestDataStore("no-definition");

            assertThat(dataStore.getStoreDefinitionTry()).isEmpty();
        }

        @Test
        @DisplayName("getStoreDefinitionTry returns definition when set")
        void testGetStoreDefinitionTry_ReturnsDefinitionWhenSet() {
            StoreDefinition mockDefinition = Mockito.mock(StoreDefinition.class);
            Mockito.when(mockDefinition.getName()).thenReturn("mock-definition");
            TestDataStore dataStore = new TestDataStore(mockDefinition);

            assertThat(dataStore.getStoreDefinitionTry()).isPresent();
        }

        @Test
        @DisplayName("setStoreDefinition updates definition")
        void testSetStoreDefinition_UpdatesDefinition() {
            TestDataStore dataStore = new TestDataStore("definition-update");
            assertThat(dataStore.getStoreDefinitionTry()).isEmpty();

            StoreDefinition mockDefinition = Mockito.mock(StoreDefinition.class);
            dataStore.setStoreDefinition(mockDefinition);

            assertThat(dataStore.getStoreDefinitionTry()).isPresent();
            assertThat(dataStore.getStoreDefinitionTry().get()).isSameAs(mockDefinition);
        }
    }

    @Nested
    @DisplayName("Persistence Operations")
    class PersistenceTests {

        @Test
        @DisplayName("getPersistence returns empty when no persistence set")
        void testGetPersistence_ReturnsEmptyWhenNoPersistenceSet() {
            TestDataStore dataStore = new TestDataStore("no-persistence");

            assertThat(dataStore.getPersistence()).isEmpty();
        }

        @Test
        @DisplayName("setPersistence updates persistence")
        void testSetPersistence_UpdatesPersistence() {
            TestDataStore dataStore = new TestDataStore("persistence-test");
            AppPersistence mockPersistence = Mockito.mock(AppPersistence.class);

            dataStore.setPersistence(mockPersistence);

            assertThat(dataStore.getPersistence()).isPresent();
            assertThat(dataStore.getPersistence().get()).isSameAs(mockPersistence);
        }

        @Test
        @DisplayName("getConfigFile returns empty when no config file set")
        void testGetConfigFile_ReturnsEmptyWhenNoConfigFileSet() {
            TestDataStore dataStore = new TestDataStore("no-config");

            assertThat(dataStore.getConfigFile()).isEmpty();
        }

        @Test
        @DisplayName("setConfigFile updates config file")
        void testSetConfigFile_UpdatesConfigFile() {
            TestDataStore dataStore = new TestDataStore("config-test");
            File configFile = new File("/test/config.properties");

            dataStore.setConfigFile(configFile);

            assertThat(dataStore.getConfigFile()).isPresent();
            assertThat(dataStore.getConfigFile().get()).isSameAs(configFile);
        }
    }

    @Nested
    @DisplayName("Strict Mode Tests")
    class StrictModeTests {

        @Test
        @DisplayName("setStrict method exists and can be called")
        void testSetStrict_MethodExistsAndCanBeCalled() {
            TestDataStore dataStore = new TestDataStore("strict-test");

            // Method should exist and not throw exceptions
            assertThatCode(() -> dataStore.setStrict(true)).doesNotThrowAnyException();
            assertThatCode(() -> dataStore.setStrict(false)).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Value Storage Internal Operations")
    class ValueStorageTests {

        @Test
        @DisplayName("stores values internally with correct keys")
        void testStoresValues_InternallyWithCorrectKeys() {
            TestDataStore dataStore = new TestDataStore("storage-test");

            dataStore.set(stringKey, "stored_value");

            // Verify the value is accessible through string key
            assertThat(dataStore.get(stringKey.getName())).isEqualTo("stored_value");
        }

        @Test
        @DisplayName("remove operation clears values correctly")
        void testRemoveOperation_ClearsValuesCorrectly() {
            TestDataStore dataStore = new TestDataStore("remove-test");

            dataStore.set(stringKey, "to_be_removed");
            assertThat(dataStore.hasValue(stringKey)).isTrue();

            dataStore.remove(stringKey);
            assertThat(dataStore.hasValue(stringKey)).isFalse();
        }

        @Test
        @DisplayName("copy constructor creates independent value storage")
        void testCopyConstructor_CreatesIndependentValueStorage() {
            TestDataStore source = new TestDataStore("source");
            source.set(stringKey, "source_value");

            TestDataStore copy = new TestDataStore("copy", source);

            // Modify source
            source.set(stringKey, "modified_source");

            // Copy should remain unchanged
            assertThat(copy.get(stringKey)).isEqualTo("source_value");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Integration")
    class EdgeCasesTests {

        @Test
        @DisplayName("handles empty store name")
        void testHandlesEmptyStoreName() {
            TestDataStore dataStore = new TestDataStore("");

            assertThat(dataStore.getName()).isEqualTo("");

            // Should still be functional
            dataStore.set(stringKey, "empty_name_test");
            assertThat(dataStore.get(stringKey)).isEqualTo("empty_name_test");
        }

        @Test
        @DisplayName("toString returns meaningful representation")
        void testToString_ReturnsMeaningfulRepresentation() {
            TestDataStore dataStore = new TestDataStore("toString-test");
            dataStore.set(stringKey, "test_value");

            String stringRepresentation = dataStore.toString();

            assertThat(stringRepresentation).isNotNull();
            assertThat(stringRepresentation).isNotEmpty();
        }

        @Test
        @DisplayName("copy from empty DataStore works correctly")
        void testCopyFromEmptyDataStore_WorksCorrectly() {
            TestDataStore empty = new TestDataStore("empty");
            TestDataStore copy = new TestDataStore("copy", empty);

            assertThat(copy.getName()).isEqualTo("copy");
            assertThat(copy.hasValue(stringKey)).isFalse();
        }

        @Test
        @DisplayName("multiple property updates work independently")
        void testMultiplePropertyUpdates_WorkIndependently() {
            TestDataStore dataStore = new TestDataStore("properties-test");

            // Set multiple properties
            dataStore.setStrict(true);

            StoreDefinition mockDefinition = Mockito.mock(StoreDefinition.class);
            dataStore.setStoreDefinition(mockDefinition);
            assertThat(dataStore.getStoreDefinitionTry()).isPresent();

            AppPersistence mockPersistence = Mockito.mock(AppPersistence.class);
            dataStore.setPersistence(mockPersistence);
            assertThat(dataStore.getPersistence()).isPresent();

            File configFile = new File("/test/config");
            dataStore.setConfigFile(configFile);
            assertThat(dataStore.getConfigFile()).isPresent();

            // All properties should remain independent
            assertThat(dataStore.getStoreDefinitionTry()).isPresent();
            assertThat(dataStore.getPersistence()).isPresent();
            assertThat(dataStore.getConfigFile()).isPresent();
        }
    }
}
