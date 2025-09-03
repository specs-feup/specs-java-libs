package org.suikasoft.jOptions.storedefinition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

import java.util.Arrays;
import java.util.Collections;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTimeout;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link StoreDefinitionIndexes}.
 * 
 * Tests the mapping of keys to indexes for efficient lookup in store
 * definitions.
 * 
 * @author Generated Tests
 */
@DisplayName("StoreDefinitionIndexes Tests")
class StoreDefinitionIndexesTest {

    @Mock
    private StoreDefinition mockStoreDefinition;

    private DataKey<String> stringKey;
    private DataKey<Integer> intKey;
    private DataKey<Boolean> boolKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        stringKey = KeyFactory.string("test.string");
        intKey = KeyFactory.integer("test.int");
        boolKey = KeyFactory.bool("test.bool");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create indexes for multiple keys")
        void testConstructor_WithMultipleKeys_CreatesCorrectIndexes() {
            // Given
            when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(stringKey, intKey, boolKey));

            // When
            StoreDefinitionIndexes indexes = new StoreDefinitionIndexes(mockStoreDefinition);

            // Then
            assertThat(indexes.getIndex(stringKey)).isEqualTo(0);
            assertThat(indexes.getIndex(intKey)).isEqualTo(1);
            assertThat(indexes.getIndex(boolKey)).isEqualTo(2);
        }

        @Test
        @DisplayName("Should create indexes for single key")
        void testConstructor_WithSingleKey_CreatesCorrectIndex() {
            // Given
            when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(stringKey));

            // When
            StoreDefinitionIndexes indexes = new StoreDefinitionIndexes(mockStoreDefinition);

            // Then
            assertThat(indexes.getIndex(stringKey)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle empty key list")
        void testConstructor_WithEmptyKeys_CreatesEmptyIndexes() {
            // Given
            when(mockStoreDefinition.getKeys()).thenReturn(Collections.emptyList());

            // When
            StoreDefinitionIndexes indexes = new StoreDefinitionIndexes(mockStoreDefinition);

            // Then
            assertThat(indexes.hasIndex(stringKey)).isFalse();
        }

        @Test
        @DisplayName("Should handle null store definition")
        void testConstructor_WithNullDefinition_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> new StoreDefinitionIndexes(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Index Retrieval Tests")
    class IndexRetrievalTests {

        private StoreDefinitionIndexes indexes;

        @BeforeEach
        void setUpIndexes() {
            when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(stringKey, intKey, boolKey));
            indexes = new StoreDefinitionIndexes(mockStoreDefinition);
        }

        @Test
        @DisplayName("Should get index by DataKey")
        void testGetIndex_WithDataKey_ReturnsCorrectIndex() {
            // When/Then
            assertThat(indexes.getIndex(stringKey)).isEqualTo(0);
            assertThat(indexes.getIndex(intKey)).isEqualTo(1);
            assertThat(indexes.getIndex(boolKey)).isEqualTo(2);
        }

        @Test
        @DisplayName("Should get index by key name")
        void testGetIndex_WithKeyName_ReturnsCorrectIndex() {
            // When/Then
            assertThat(indexes.getIndex("test.string")).isEqualTo(0);
            assertThat(indexes.getIndex("test.int")).isEqualTo(1);
            assertThat(indexes.getIndex("test.bool")).isEqualTo(2);
        }

        @Test
        @DisplayName("Should throw exception for missing DataKey")
        void testGetIndex_WithMissingDataKey_ThrowsException() {
            // Given
            DataKey<String> missingKey = KeyFactory.string("missing.key");

            // When/Then
            assertThatThrownBy(() -> indexes.getIndex(missingKey))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Key 'missing.key' not present in this definition");
        }

        @Test
        @DisplayName("Should throw exception for missing key name")
        void testGetIndex_WithMissingKeyName_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> indexes.getIndex("missing.key"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Key 'missing.key' not present in this definition");
        }

        @Test
        @DisplayName("Should throw exception for null key name")
        void testGetIndex_WithNullKeyName_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> indexes.getIndex((String) null))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Key 'null' not present in this definition");
        }

        @Test
        @DisplayName("Should throw exception for null DataKey")
        void testGetIndex_WithNullDataKey_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> indexes.getIndex((DataKey<?>) null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Index Existence Tests")
    class IndexExistenceTests {

        private StoreDefinitionIndexes indexes;

        @BeforeEach
        void setUpIndexes() {
            when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(stringKey, intKey));
            indexes = new StoreDefinitionIndexes(mockStoreDefinition);
        }

        @Test
        @DisplayName("Should confirm existing keys by DataKey")
        void testHasIndex_WithExistingDataKey_ReturnsTrue() {
            // When/Then
            assertThat(indexes.hasIndex(stringKey)).isTrue();
            assertThat(indexes.hasIndex(intKey)).isTrue();
        }

        @Test
        @DisplayName("Should confirm existing keys by name")
        void testHasIndex_WithExistingKeyName_ReturnsTrue() {
            // When/Then
            assertThat(indexes.hasIndex("test.string")).isTrue();
            assertThat(indexes.hasIndex("test.int")).isTrue();
        }

        @Test
        @DisplayName("Should deny missing keys by DataKey")
        void testHasIndex_WithMissingDataKey_ReturnsFalse() {
            // Given
            DataKey<String> missingKey = KeyFactory.string("missing.key");

            // When/Then
            assertThat(indexes.hasIndex(missingKey)).isFalse();
            assertThat(indexes.hasIndex(boolKey)).isFalse();
        }

        @Test
        @DisplayName("Should deny missing keys by name")
        void testHasIndex_WithMissingKeyName_ReturnsFalse() {
            // When/Then
            assertThat(indexes.hasIndex("missing.key")).isFalse();
            assertThat(indexes.hasIndex("test.bool")).isFalse();
        }

        @Test
        @DisplayName("Should handle null key name")
        void testHasIndex_WithNullKeyName_ReturnsFalse() {
            // When/Then
            assertThat(indexes.hasIndex((String) null)).isFalse();
        }

        @Test
        @DisplayName("Should handle null DataKey")
        void testHasIndex_WithNullDataKey_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> indexes.hasIndex((DataKey<?>) null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle duplicate key names in definition")
        void testIndexes_WithDuplicateKeyNames_UsesLastOccurrence() {
            // Given - Create keys with same name (though this shouldn't happen in practice)
            @SuppressWarnings("unchecked")
            DataKey<String> firstKey = mock(DataKey.class);
            @SuppressWarnings("unchecked")
            DataKey<String> secondKey = mock(DataKey.class);
            when(firstKey.getName()).thenReturn("duplicate.key");
            when(secondKey.getName()).thenReturn("duplicate.key");

            when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(firstKey, secondKey));

            // When
            StoreDefinitionIndexes indexes = new StoreDefinitionIndexes(mockStoreDefinition);

            // Then - Should map to the index of the last occurrence
            assertThat(indexes.getIndex("duplicate.key")).isEqualTo(1);
            assertThat(indexes.hasIndex("duplicate.key")).isTrue();
        }

        @Test
        @DisplayName("Should handle keys with similar names")
        void testIndexes_WithSimilarKeyNames_MaintainsDistinctIndexes() {
            // Given
            DataKey<String> key1 = KeyFactory.string("test.key");
            DataKey<String> key2 = KeyFactory.string("test.key.extension");
            DataKey<String> key3 = KeyFactory.string("test");

            when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(key1, key2, key3));

            // When
            StoreDefinitionIndexes indexes = new StoreDefinitionIndexes(mockStoreDefinition);

            // Then
            assertThat(indexes.getIndex("test.key")).isEqualTo(0);
            assertThat(indexes.getIndex("test.key.extension")).isEqualTo(1);
            assertThat(indexes.getIndex("test")).isEqualTo(2);

            // Verify all are present
            assertThat(indexes.hasIndex("test.key")).isTrue();
            assertThat(indexes.hasIndex("test.key.extension")).isTrue();
            assertThat(indexes.hasIndex("test")).isTrue();
        }

        @Test
        @DisplayName("Should handle empty key names")
        void testIndexes_WithEmptyKeyName_HandlesGracefully() {
            // Given
            @SuppressWarnings("unchecked")
            DataKey<String> emptyNameKey = mock(DataKey.class);
            when(emptyNameKey.getName()).thenReturn("");

            when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(emptyNameKey, stringKey));

            // When
            StoreDefinitionIndexes indexes = new StoreDefinitionIndexes(mockStoreDefinition);

            // Then
            assertThat(indexes.getIndex("")).isEqualTo(0);
            assertThat(indexes.getIndex("test.string")).isEqualTo(1);
            assertThat(indexes.hasIndex("")).isTrue();
        }

        @Test
        @DisplayName("Should provide meaningful error messages")
        void testGetIndex_WithMissingKey_ProvidesHelpfulErrorMessage() {
            // Given
            when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(stringKey, intKey));
            StoreDefinitionIndexes indexes = new StoreDefinitionIndexes(mockStoreDefinition);

            // When/Then
            assertThatThrownBy(() -> indexes.getIndex("missing.key"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Key 'missing.key' not present in this definition")
                    .hasMessageContaining("test.string")
                    .hasMessageContaining("test.int");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @RetryingTest(5)
        @DisplayName("Should handle large number of keys efficiently")
        void testIndexes_WithManyKeys_HandlesEfficiently() {
            // Given - Create many keys
            java.util.List<DataKey<?>> manyKeys = new java.util.ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                DataKey<String> key = KeyFactory.string("key" + i);
                manyKeys.add(key);
            }
            when(mockStoreDefinition.getKeys()).thenReturn(manyKeys);

            // When
            StoreDefinitionIndexes indexes = new StoreDefinitionIndexes(mockStoreDefinition);

            // Then - Verify a few indexes
            assertThat(indexes.getIndex("key0")).isEqualTo(0);
            assertThat(indexes.getIndex("key500")).isEqualTo(500);
            assertThat(indexes.getIndex("key999")).isEqualTo(999);

            // Verify existence checks
            assertThat(indexes.hasIndex("key0")).isTrue();
            assertThat(indexes.hasIndex("key1000")).isFalse();

            // Performance measurement: ensure many lookups complete within a reasonable time
            final int iterations = 100_000;
            // This will fail the test if the loop takes longer than the duration
            assertTimeout(Duration.ofMillis(500), () -> {
                for (int i = 0; i < iterations; i++) {
                    String keyName = "key" + (i % 1000);
                    int idx = indexes.getIndex(keyName);
                    if (idx != (i % 1000)) {
                        throw new AssertionError("Unexpected index for " + keyName);
                    }

                    // Occasionally assert a negative existence check to ensure that path is exercised
                    if (i % 1000 == 0) {
                        if (indexes.hasIndex("key1000")) {
                            throw new AssertionError("Unexpected existence for key1000");
                        }
                    }
                }
            });
        }
    }
}
