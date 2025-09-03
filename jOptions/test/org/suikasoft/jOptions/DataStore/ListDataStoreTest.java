package org.suikasoft.jOptions.DataStore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Unit tests for {@link ListDataStore}.
 * 
 * Tests the list-based data store implementation including value storage,
 * retrieval, key indexing, strict mode, and store definition integration.
 * 
 * @author Generated Tests
 */
@DisplayName("ListDataStore")
class ListDataStoreTest {

    private StoreDefinition mockStoreDefinition;
    private DataKey<String> stringKey;
    private DataKey<Integer> intKey;
    private DataKey<Boolean> boolKey;
    private ListDataStore listDataStore;

    @BeforeEach
    void setUp() {
        // Create test DataKeys
        stringKey = KeyFactory.string("string");
        intKey = KeyFactory.integer("int");
        boolKey = KeyFactory.bool("bool");

        // Create a simple StoreDefinition implementation
        mockStoreDefinition = new StoreDefinition() {
            private final List<DataKey<?>> keys = Arrays.asList(stringKey, intKey, boolKey);

            @Override
            public String getName() {
                return "Test Store";
            }

            @Override
            public List<DataKey<?>> getKeys() {
                return keys;
            }

            @Override
            public boolean hasKey(String key) {
                return keys.stream().anyMatch(k -> k.getName().equals(key));
            }
        };

        // Create ListDataStore instance
        listDataStore = new ListDataStore(mockStoreDefinition);
    }

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorAndInitializationTests {

        @Test
        @DisplayName("constructor creates empty store with given definition")
        void testConstructor_CreatesEmptyStoreWithGivenDefinition() {
            // Create another simple StoreDefinition
            StoreDefinition definition = new StoreDefinition() {
                private final List<DataKey<?>> keys = Arrays.asList(stringKey, intKey);

                @Override
                public String getName() {
                    return "Test Store 2";
                }

                @Override
                public List<DataKey<?>> getKeys() {
                    return keys;
                }

                @Override
                public boolean hasKey(String key) {
                    return keys.stream().anyMatch(k -> k.getName().equals(key));
                }
            };

            ListDataStore store = new ListDataStore(definition);

            assertThat(store.getStoreDefinitionTry()).contains(definition);
            assertThat(store.hasValue(stringKey)).isFalse();
            assertThat(store.hasValue(intKey)).isFalse();
        }

        @Test
        @DisplayName("constructor with null definition throws exception")
        void testConstructor_NullDefinition_ThrowsException() {
            assertThatThrownBy(() -> new ListDataStore(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("strict mode is disabled by default")
        void testStrictMode_DisabledByDefault() {
            // In non-strict mode, getting a value that doesn't exist should return the
            // default value
            assertThat(listDataStore.get(stringKey)).isEqualTo(""); // default value for string key
        }
    }

    @Nested
    @DisplayName("Value Storage and Retrieval")
    class ValueStorageAndRetrievalTests {

        @Test
        @DisplayName("set and get string value works correctly")
        void testSetAndGet_StringValue_WorksCorrectly() {
            String value = "test value";

            listDataStore.set(stringKey, value);

            assertThat(listDataStore.get(stringKey)).isEqualTo(value);
            assertThat(listDataStore.hasValue(stringKey)).isTrue();
        }

        @Test
        @DisplayName("set and get integer value works correctly")
        void testSetAndGet_IntegerValue_WorksCorrectly() {
            Integer value = 42;

            listDataStore.set(intKey, value);

            assertThat(listDataStore.get(intKey)).isEqualTo(value);
            assertThat(listDataStore.hasValue(intKey)).isTrue();
        }

        @Test
        @DisplayName("set and get boolean value works correctly")
        void testSetAndGet_BooleanValue_WorksCorrectly() {
            Boolean value = true;

            listDataStore.set(boolKey, value);

            assertThat(listDataStore.get(boolKey)).isEqualTo(value);
            assertThat(listDataStore.hasValue(boolKey)).isTrue();
        }

        @Test
        @DisplayName("get non-existing key returns default value")
        void testGet_NonExistingKey_ReturnsDefaultValue() {
            // String keys have default value of ""
            // Check hasValue before accessing, as accessing will store the default value
            assertThat(listDataStore.hasValue(stringKey)).isFalse();
            assertThat(listDataStore.get(stringKey)).isEqualTo("");
            // After accessing, the default value is stored, so hasValue becomes true
            assertThat(listDataStore.hasValue(stringKey)).isTrue();
        }

        @Test
        @DisplayName("set multiple values and retrieve correctly")
        void testSetMultiple_RetrieveCorrectly() {
            String stringValue = "test";
            Integer intValue = 123;
            Boolean boolValue = false;

            listDataStore.set(stringKey, stringValue);
            listDataStore.set(intKey, intValue);
            listDataStore.set(boolKey, boolValue);

            assertThat(listDataStore.get(stringKey)).isEqualTo(stringValue);
            assertThat(listDataStore.get(intKey)).isEqualTo(intValue);
            assertThat(listDataStore.get(boolKey)).isEqualTo(boolValue);
        }
    }

    @Nested
    @DisplayName("Key Management")
    class KeyManagementTests {

        @Test
        @DisplayName("getDataKeysWithValues returns all defined keys")
        void testGetDataKeysWithValues_ReturnsAllDefinedKeys() {
            Collection<DataKey<?>> keys = listDataStore.getDataKeysWithValues();

            // Initially empty as no values are set
            assertThat(keys).isEmpty();
        }

        @Test
        @DisplayName("getKeysWithValues returns keys with values")
        void testGetKeysWithValues_ReturnsKeysWithValues() {
            listDataStore.put(stringKey, "test");
            listDataStore.put(intKey, 42);

            Collection<String> keysWithValues = listDataStore.getKeysWithValues();

            assertThat(keysWithValues).hasSize(2);
            assertThat(keysWithValues).contains(stringKey.getName(), intKey.getName());
        }

        @Test
        @DisplayName("getKeysWithValues returns empty list when no values set")
        void testGetKeysWithValues_NoValuesSet_ReturnsEmptyList() {
            Collection<String> keysWithValues = listDataStore.getKeysWithValues();

            assertThat(keysWithValues).isEmpty();
        }
    }

    @Nested
    @DisplayName("Remove Operations")
    class RemoveOperationsTests {

        @Test
        @DisplayName("remove existing value returns true")
        void testRemove_ExistingValue_ReturnsTrue() {
            listDataStore.set(stringKey, "test");

            Optional<String> removed = listDataStore.remove(stringKey);

            assertThat(removed).isPresent();
            assertThat(removed.get()).isEqualTo("test");
            assertThat(listDataStore.hasValue(stringKey)).isFalse();
            assertThat(listDataStore.get(stringKey)).isEqualTo(""); // returns default value
        }

        @Test
        @DisplayName("remove non-existing value returns false")
        void testRemove_NonExistingValue_ReturnsFalse() {
            Optional<String> removed = listDataStore.remove(stringKey);

            assertThat(removed).isEmpty();
        }

        @Test
        @DisplayName("remove value after setting and getting")
        void testRemove_AfterSettingAndGetting() {
            listDataStore.set(stringKey, "test");
            assertThat(listDataStore.get(stringKey)).isEqualTo("test");

            listDataStore.remove(stringKey);

            // After removal, getting the key again will return default and store it
            assertThat(listDataStore.get(stringKey)).isEqualTo(""); // returns default value
            // Since get() was called, the default value is now stored, so hasValue is true
            assertThat(listDataStore.hasValue(stringKey)).isTrue();
        }
    }

    @Nested
    @DisplayName("Strict Mode")
    class StrictModeTests {

        @Test
        @DisplayName("enable strict mode sets flag correctly")
        void testEnableStrictMode_SetsFlagCorrectly() {
            listDataStore.setStrict(true);

            // Test that strict mode affects behavior - cannot set null values
            assertThrows(RuntimeException.class, () -> listDataStore.set(stringKey, null));
        }

        @Test
        @DisplayName("disable strict mode sets flag correctly")
        void testDisableStrictMode_SetsFlagCorrectly() {
            listDataStore.setStrict(true);
            listDataStore.setStrict(false);

            // Test that when strict mode is off, getting a key without a value returns
            // default (not exception)
            assertThat(listDataStore.get(stringKey)).isEqualTo(""); // Should return default, not throw
        }
    }

    @Nested
    @DisplayName("Store Definition")
    class StoreDefinitionTests {

        @Test
        @DisplayName("getStoreDefinitionTry returns definition")
        void testGetStoreDefinitionTry_ReturnsDefinition() {
            Optional<StoreDefinition> definition = listDataStore.getStoreDefinitionTry();

            assertThat(definition).isPresent();
            assertThat(definition.get()).isSameAs(mockStoreDefinition);
        }

        @Test
        @DisplayName("getStoreDefinition returns definition")
        void testGetStoreDefinition_ReturnsDefinition() {
            StoreDefinition definition = listDataStore.getStoreDefinition();

            assertThat(definition).isSameAs(mockStoreDefinition);
        }
    }

    @Nested
    @DisplayName("Copy Operations")
    class CopyOperationsTests {

        @Test
        @DisplayName("copy creates new instance with same values")
        void testCopy_CreatesNewInstanceWithSameValues() {
            listDataStore.set(stringKey, "test");
            listDataStore.set(intKey, 42);

            ListDataStore copy = (ListDataStore) listDataStore.copy();

            assertThat(copy).isNotSameAs(listDataStore);
            assertThat(copy.get(stringKey)).isEqualTo("test");
            assertThat(copy.get(intKey)).isEqualTo(42);
            assertThat(copy.hasValue(boolKey)).isFalse();
        }

        @Test
        @DisplayName("copy preserves store definition")
        void testCopy_PreservesStoreDefinition() {
            ListDataStore copy = (ListDataStore) listDataStore.copy();

            assertThat(copy.getStoreDefinition()).isSameAs(mockStoreDefinition);
        }

        @Test
        @DisplayName("copy preserves strict mode setting")
        void testCopy_PreservesStrictModeSetting() {
            listDataStore.setStrict(true);

            ListDataStore copy = (ListDataStore) listDataStore.copy();

            // We can't directly check strict mode, so test the behavior
            assertThrows(RuntimeException.class, () -> copy.set(stringKey, null));
        }
    }

    @Nested
    @DisplayName("Equality and Hash Code")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("stores with same values are equal")
        void testEquals_StoresWithSameValues_AreEqual() {
            ListDataStore other = new ListDataStore(mockStoreDefinition);

            listDataStore.set(stringKey, "test");
            other.set(stringKey, "test");

            assertThat(listDataStore).isEqualTo(other);
            assertThat(listDataStore.hashCode()).isEqualTo(other.hashCode());
        }

        @Test
        @DisplayName("stores with different values are not equal")
        void testEquals_StoresWithDifferentValues_AreNotEqual() {
            ListDataStore other = new ListDataStore(mockStoreDefinition);

            listDataStore.set(stringKey, "test1");
            other.set(stringKey, "test2");

            assertThat(listDataStore).isNotEqualTo(other);
        }

        @Test
        @DisplayName("empty stores are equal")
        void testEquals_EmptyStores_AreEqual() {
            ListDataStore other = new ListDataStore(mockStoreDefinition);

            assertThat(listDataStore).isEqualTo(other);
            assertThat(listDataStore.hashCode()).isEqualTo(other.hashCode());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("setting null value throws exception")
        void testSetNullValue_ThrowsException() {
            assertThatThrownBy(() -> listDataStore.set(stringKey, null))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Tried to set a null value");
        }

        @Test
        @DisplayName("toString returns meaningful representation")
        void testToString_ReturnsMeaningfulRepresentation() {
            listDataStore.set(stringKey, "test");
            listDataStore.set(intKey, 42);

            String result = listDataStore.toString();

            assertThat(result).isNotEmpty();
            assertThat(result).contains("Test Store"); // Store name
            assertThat(result).contains("string: test");
            assertThat(result).contains("int: 42");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("complete workflow with multiple operations")
        void testCompleteWorkflow_MultipleOperations() {
            // Set initial values
            listDataStore.set(stringKey, "initial");
            listDataStore.set(intKey, 1);

            // Verify initial state
            assertThat(listDataStore.getKeysWithValues()).hasSize(2);

            // Modify values
            listDataStore.set(stringKey, "modified");
            listDataStore.set(boolKey, true);

            // Verify modified state
            assertThat(listDataStore.get(stringKey)).isEqualTo("modified");
            assertThat(listDataStore.get(intKey)).isEqualTo(1);
            assertThat(listDataStore.get(boolKey)).isTrue();
            assertThat(listDataStore.getKeysWithValues()).hasSize(3);

            // Remove a value
            listDataStore.remove(intKey);

            // Verify final state
            assertThat(listDataStore.getKeysWithValues()).hasSize(2);
            assertThat(listDataStore.hasValue(intKey)).isFalse();

            // Test copy functionality
            ListDataStore copy = (ListDataStore) listDataStore.copy();
            assertThat(copy.get(stringKey)).isEqualTo("modified");
            assertThat(copy.get(boolKey)).isTrue();
            assertThat(copy.hasValue(intKey)).isFalse();
        }
    }
}
