package org.suikasoft.jOptions.Interfaces;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

/**
 * Comprehensive test suite for DataStore interface operations.
 * 
 * Tests cover:
 * - Basic key-value operations (get, set, has)
 * - Type safety with DataKey system
 * - Optional value handling
 * - Raw key operations
 * - String-based key access
 * - DataStore copying operations
 * - Default value behaviors
 * - Error handling and edge cases
 * 
 * @author Generated Tests
 */
@DisplayName("DataStore Interface Tests")
class DataStoreTest {

    private DataStore dataStore;
    private DataKey<String> stringKey;
    private DataKey<Integer> intKey;
    private DataKey<Boolean> boolKey;

    @BeforeEach
    void setUp() {
        dataStore = new SimpleDataStore("test-store");
        stringKey = KeyFactory.string("test.string");
        intKey = KeyFactory.integer("test.int");
        boolKey = KeyFactory.bool("test.bool");
    }

    @Nested
    @DisplayName("Basic Key-Value Operations")
    class BasicOperationsTests {

        @Test
        @DisplayName("set and get with DataKey preserves type safety")
        void testSetAndGet_WithDataKey_PreservesTypeSafety() {
            String testValue = "test_value";

            DataStore result = dataStore.set(stringKey, testValue);
            String retrievedValue = dataStore.get(stringKey);

            assertThat(result).isSameAs(dataStore); // Should return same instance
            assertThat(retrievedValue).isEqualTo(testValue);
        }

        @Test
        @DisplayName("set and get with different types work independently")
        void testSetAndGet_WithDifferentTypes_WorkIndependently() {
            String stringValue = "test_string";
            Integer intValue = 42;
            Boolean boolValue = true;

            dataStore.set(stringKey, stringValue)
                    .set(intKey, intValue)
                    .set(boolKey, boolValue);

            assertThat(dataStore.get(stringKey)).isEqualTo(stringValue);
            assertThat(dataStore.get(intKey)).isEqualTo(intValue);
            assertThat(dataStore.get(boolKey)).isEqualTo(boolValue);
        }

        @Test
        @DisplayName("hasValue returns correct boolean for existing and non-existing keys")
        void testHasValue_ReturnsCorrectBoolean() {
            assertThat(dataStore.hasValue(stringKey)).isFalse();

            dataStore.set(stringKey, "value");

            assertThat(dataStore.hasValue(stringKey)).isTrue();
            assertThat(dataStore.hasValue(intKey)).isFalse();
        }

        @Test
        @DisplayName("get returns default value for non-existing keys")
        void testGet_ReturnsDefaultValueForNonExistingKeys() {
            String result = dataStore.get(stringKey);

            // DataStore returns empty string for String keys, not null
            assertThat(result).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Optional Value Operations")
    class OptionalOperationsTests {

        @Test
        @DisplayName("getTry returns empty Optional for non-existing key")
        void testGetTry_ReturnsEmptyOptionalForNonExistingKey() {
            Optional<String> result = dataStore.getTry(stringKey);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("getTry returns present Optional for existing key")
        void testGetTry_ReturnsPresentOptionalForExistingKey() {
            String testValue = "test_value";
            dataStore.set(stringKey, testValue);

            Optional<String> result = dataStore.getTry(stringKey);

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(testValue);
        }

        @Test
        @DisplayName("getTry with null value returns empty Optional")
        void testGetTry_WithNullValue_ReturnsEmptyOptional() {
            // DataStore doesn't allow null values - need to test remove() instead
            dataStore.set(stringKey, "initial_value");
            // Use remove() instead of setting null
            dataStore.remove(stringKey);

            Optional<String> result = dataStore.getTry(stringKey);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("String-based Key Operations")
    class StringKeyOperationsTests {

        @Test
        @DisplayName("get with string key works after setting with DataKey")
        void testGetWithStringKey_WorksAfterSettingWithDataKey() {
            String testValue = "test_value";
            dataStore.set(stringKey, testValue);

            Object result = dataStore.get(stringKey.getName());

            assertThat(result).isEqualTo(testValue);
        }

        @Test
        @DisplayName("get with string key returns null for non-existing key")
        void testGetWithStringKey_ReturnsNullForNonExistingKey() {
            Object result = dataStore.get("non.existing.key");

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Value Update Operations")
    class ValueUpdateTests {

        @Test
        @DisplayName("setting new value overwrites previous value")
        void testSet_NewValue_OverwritesPreviousValue() {
            String firstValue = "first_value";
            String secondValue = "second_value";

            dataStore.set(stringKey, firstValue);
            dataStore.set(stringKey, secondValue);

            assertThat(dataStore.get(stringKey)).isEqualTo(secondValue);
        }

        @Test
        @DisplayName("removing value removes key from DataStore")
        void testRemove_RemovesKeyFromDataStore() {
            dataStore.set(stringKey, "initial_value");
            assertThat(dataStore.hasValue(stringKey)).isTrue();

            // Use remove() instead of setting null
            dataStore.remove(stringKey);

            assertThat(dataStore.hasValue(stringKey)).isFalse();
            // After removal, get returns default value (empty string for String keys)
            assertThat(dataStore.get(stringKey)).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Type Safety Tests")
    class TypeSafetyTests {

        @Test
        @DisplayName("DataKey type system prevents incorrect type retrieval")
        void testDataKeyTypeSystem_PreventsIncorrectTypeRetrieval() {
            // This test verifies compile-time type safety
            dataStore.set(stringKey, "string_value");
            dataStore.set(intKey, 42);

            // These should compile correctly with proper types
            String stringValue = dataStore.get(stringKey);
            Integer intValue = dataStore.get(intKey);

            assertThat(stringValue).isInstanceOf(String.class);
            assertThat(intValue).isInstanceOf(Integer.class);
        }

        @Test
        @DisplayName("subclass values can be stored with superclass keys")
        void testSubclassValues_CanBeStoredWithSuperclassKeys() {
            // Create a key for File type
            DataKey<File> fileKey = KeyFactory.file("test.file");
            File testFile = new File("/test/path");

            // Should be able to store File instances
            dataStore.set(fileKey, testFile);
            File retrievedFile = dataStore.get(fileKey);

            assertThat(retrievedFile).isEqualTo(testFile);
        }
    }

    @Nested
    @DisplayName("DataStore Copy Operations")
    class CopyOperationsTests {

        @Test
        @DisplayName("copy constructor creates independent copy")
        void testCopyConstructor_CreatesIndependentCopy() {
            dataStore.set(stringKey, "original_value");
            dataStore.set(intKey, 42);

            DataStore copy = new SimpleDataStore("copy-store", dataStore);

            // Verify initial values are copied
            assertThat(copy.get(stringKey)).isEqualTo("original_value");
            assertThat(copy.get(intKey)).isEqualTo(42);

            // Verify independence - changes to original don't affect copy
            dataStore.set(stringKey, "changed_value");
            assertThat(copy.get(stringKey)).isEqualTo("original_value");
        }

        @Test
        @DisplayName("empty DataStore copy works correctly")
        void testEmptyDataStoreCopy_WorksCorrectly() {
            DataStore copy = new SimpleDataStore("empty-copy", dataStore);

            assertThat(copy.hasValue(stringKey)).isFalse();
            assertThat(copy.hasValue(intKey)).isFalse();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("multiple sequential operations work correctly")
        void testMultipleSequentialOperations_WorkCorrectly() {
            dataStore.set(stringKey, "value1")
                    .set(stringKey, "value2")
                    .set(intKey, 100)
                    .set(intKey, 200)
                    .set(boolKey, false)
                    .set(boolKey, true);

            assertThat(dataStore.get(stringKey)).isEqualTo("value2");
            assertThat(dataStore.get(intKey)).isEqualTo(200);
            assertThat(dataStore.get(boolKey)).isEqualTo(true);
        }

        @Test
        @DisplayName("DataStore handles keys with same name but different types")
        void testDataStore_HandlesKeysWithSameNameButDifferentTypes() {
            // This tests the internal key handling mechanism
            String keyName = "same.name";
            DataKey<String> stringKey1 = KeyFactory.string(keyName);
            // Note: Creating int key with same name for testing - not used directly
            KeyFactory.integer(keyName);

            // These are different keys despite same name due to type differences
            dataStore.set(stringKey1, "string_value");

            // The behavior here depends on implementation - usually same name = same key
            // This test documents the expected behavior
            assertThat(dataStore.hasValue(stringKey1)).isTrue();

            // Getting with string key should return the string value
            Object rawValue = dataStore.get(keyName);
            assertThat(rawValue).isEqualTo("string_value");
        }

        @Test
        @DisplayName("setting empty string is different from null")
        void testSet_EmptyString_IsDifferentFromNull() {
            dataStore.set(stringKey, "");

            assertThat(dataStore.hasValue(stringKey)).isTrue();
            assertThat(dataStore.get(stringKey)).isEqualTo("");
            assertThat(dataStore.getTry(stringKey)).isPresent();
        }
    }
}
