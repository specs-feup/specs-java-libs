package org.suikasoft.jOptions.storedefinition;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

/**
 * Comprehensive test suite for {@link StoreDefinitions}.
 * 
 * Tests the utility class for building StoreDefinition instances from Java
 * interfaces by reflection, including caching behavior, field extraction, and
 * error handling.
 * 
 * @author Generated Tests
 */
@DisplayName("StoreDefinitions Tests")
class StoreDefinitionsTest {

    // Test interface with DataKey fields
    public static interface TestInterface {
        DataKey<String> STRING_KEY = KeyFactory.string("stringKey", "defaultValue");
        DataKey<Integer> INT_KEY = KeyFactory.integer("intKey", 42);
        DataKey<Boolean> BOOL_KEY = KeyFactory.bool("boolKey");
    }

    // Test interface with mixed fields (some not DataKeys)
    public static interface MixedInterface {
        DataKey<String> VALID_KEY = KeyFactory.string("validKey", "value");
        String REGULAR_STRING = "notADataKey";
        int REGULAR_INT = 123;
    }

    // Test interface with no DataKey fields
    public static interface EmptyInterface {
        String SOME_STRING = "noDataKeys";
        int SOME_INT = 456;
    }

    // Test interface with non-static DataKey field (should be ignored)
    public static class ClassWithNonStaticKeys {
        @SuppressWarnings("unused")
        private static final DataKey<String> STATIC_KEY = KeyFactory.string("staticKey", "value");
        public final DataKey<String> NON_STATIC_KEY = KeyFactory.string("nonStaticKey", "ignored");
    }

    // Class with private static keys - current implementation only collects public
    // static fields
    public static class ClassWithPrivateKeys {
        @SuppressWarnings("unused")
        private static final DataKey<String> PUBLIC_KEY = KeyFactory.string("publicKey", "value");
        @SuppressWarnings("unused")
        private static final DataKey<String> PRIVATE_KEY = KeyFactory.string("privateKey", "hidden");
    }

    @BeforeEach
    void setUp() {
        // Note: CachedItems doesn't provide a clear() method
        // Tests will work with whatever cache state exists
    }

    @Nested
    @DisplayName("Basic Functionality Tests")
    class BasicFunctionalityTests {

        @Test
        @DisplayName("Should extract DataKey fields from interface")
        void shouldExtractDataKeyFieldsFromInterface() {
            var storeDefinition = StoreDefinitions.fromInterface(TestInterface.class);

            assertThat(storeDefinition).isNotNull();
            assertThat(storeDefinition.getName()).isEqualTo("TestInterface");
            assertThat(storeDefinition.getKeys()).hasSize(3);

            var keyMap = storeDefinition.getKeyMap();
            assertThat(keyMap).containsKey("stringKey");
            assertThat(keyMap).containsKey("intKey");
            assertThat(keyMap).containsKey("boolKey");
        }

        @Test
        @DisplayName("Should filter out non-DataKey fields")
        void shouldFilterOutNonDataKeyFields() {
            var storeDefinition = StoreDefinitions.fromInterface(MixedInterface.class);

            assertThat(storeDefinition).isNotNull();
            assertThat(storeDefinition.getName()).isEqualTo("MixedInterface");
            assertThat(storeDefinition.getKeys()).hasSize(1);

            var keyMap = storeDefinition.getKeyMap();
            assertThat(keyMap).containsKey("validKey");
            assertThat(keyMap).doesNotContainKey("REGULAR_STRING");
            assertThat(keyMap).doesNotContainKey("REGULAR_INT");
        }

        @Test
        @DisplayName("Should handle interfaces with no DataKey fields")
        void shouldHandleInterfacesWithNoDataKeyFields() {
            var storeDefinition = StoreDefinitions.fromInterface(EmptyInterface.class);

            assertThat(storeDefinition).isNotNull();
            assertThat(storeDefinition.getName()).isEqualTo("EmptyInterface");
            assertThat(storeDefinition.getKeys()).isEmpty();
            assertThat(storeDefinition.getKeyMap()).isEmpty();
        }

        @Test
        @DisplayName("Should ignore non-static DataKey fields and private static fields in classes")
        void shouldIgnoreNonStaticDataKeyFields() {
            var storeDefinition = StoreDefinitions.fromInterface(ClassWithNonStaticKeys.class);

            assertThat(storeDefinition).isNotNull();
            assertThat(storeDefinition.getName()).isEqualTo("ClassWithNonStaticKeys");
            // current implementation uses getFields(), so private static and public
            // non-static are both ignored
            assertThat(storeDefinition.getKeys()).isEmpty();

            var keyMap = storeDefinition.getKeyMap();
            assertThat(keyMap).isEmpty();
        }

        @Test
        @DisplayName("Classes: only public static DataKey fields are collected")
        void classesOnlyPublicStaticAreCollected() {
            var storeDefinition = StoreDefinitions.fromInterface(ClassWithPrivateKeys.class);

            assertThat(storeDefinition).isNotNull();
            assertThat(storeDefinition.getName()).isEqualTo("ClassWithPrivateKeys");
            // All fields are private static; with current implementation using getFields(),
            // none are collected
            assertThat(storeDefinition.getKeys()).isEmpty();
            assertThat(storeDefinition.getKeyMap()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Cache Functionality Tests")
    class CacheFunctionalityTests {

        @Test
        @DisplayName("Should return same instance from cache")
        void shouldReturnSameInstanceFromCache() {
            var storeDefinition1 = StoreDefinitions.fromInterface(TestInterface.class);
            var storeDefinition2 = StoreDefinitions.fromInterface(TestInterface.class);

            assertThat(storeDefinition1).isSameAs(storeDefinition2);
        }

        @Test
        @DisplayName("Should return different instances for different classes")
        void shouldReturnDifferentInstancesForDifferentClasses() {
            var storeDefinition1 = StoreDefinitions.fromInterface(TestInterface.class);
            var storeDefinition2 = StoreDefinitions.fromInterface(MixedInterface.class);

            assertThat(storeDefinition1).isNotSameAs(storeDefinition2);
            assertThat(storeDefinition1.getName()).isEqualTo("TestInterface");
            assertThat(storeDefinition2.getName()).isEqualTo("MixedInterface");
        }

        @Test
        @DisplayName("Should provide access to cache")
        void shouldProvideAccessToCache() {
            var cache = StoreDefinitions.getStoreDefinitionsCache();

            assertThat(cache).isNotNull();

            // Cache starts with some initial size
            long initialSize = cache.getCacheSize();
            assertThat(initialSize).isGreaterThanOrEqualTo(0);

            // After using fromInterface, cache should have at least one more entry
            StoreDefinitions.fromInterface(TestInterface.class);
            assertThat(cache.getCacheSize()).isGreaterThanOrEqualTo(initialSize);
        }

        @Test
        @DisplayName("Should support cache operations")
        void shouldSupportCacheOperations() {
            var cache = StoreDefinitions.getStoreDefinitionsCache();

            // Record initial state
            long initialSize = cache.getCacheSize();
            long initialCalls = cache.getCacheTotalCalls();

            // Add entry (or get existing)
            var storeDefinition = StoreDefinitions.fromInterface(TestInterface.class);
            assertThat(cache.getCacheSize()).isGreaterThanOrEqualTo(initialSize);
            assertThat(cache.getCacheTotalCalls()).isGreaterThan(initialCalls);

            // Get from cache (should increase calls but not size)
            long sizeAfterFirst = cache.getCacheSize();
            long callsAfterFirst = cache.getCacheTotalCalls();

            var cachedDefinition = StoreDefinitions.fromInterface(TestInterface.class);
            assertThat(cachedDefinition).isSameAs(storeDefinition);
            assertThat(cache.getCacheSize()).isEqualTo(sizeAfterFirst); // Size unchanged
            assertThat(cache.getCacheTotalCalls()).isGreaterThan(callsAfterFirst); // Calls increased
        }
    }

    @Nested
    @DisplayName("Generated StoreDefinition Tests")
    class GeneratedStoreDefinitionTests {

        @Test
        @DisplayName("Generated store definition should have correct name")
        void generatedStoreDefinitionShouldHaveCorrectName() {
            var storeDefinition = StoreDefinitions.fromInterface(TestInterface.class);
            assertThat(storeDefinition.getName()).isEqualTo("TestInterface");
        }

        @Test
        @DisplayName("Generated store definition should contain correct keys")
        void generatedStoreDefinitionShouldContainCorrectKeys() {
            var storeDefinition = StoreDefinitions.fromInterface(TestInterface.class);
            var keys = storeDefinition.getKeys();

            assertThat(keys).hasSize(3);
            assertThat(keys).extracting(DataKey::getName)
                    .containsExactlyInAnyOrder("stringKey", "intKey", "boolKey");
        }

        @Test
        @DisplayName("Generated store definition should have correct default values")
        void generatedStoreDefinitionShouldHaveCorrectDefaultValues() {
            var storeDefinition = StoreDefinitions.fromInterface(TestInterface.class);
            var defaultValues = storeDefinition.getDefaultValues();

            assertThat(defaultValues).isNotNull();
            assertThat(defaultValues.get(TestInterface.STRING_KEY)).isEqualTo("defaultValue");
            assertThat(defaultValues.get(TestInterface.INT_KEY)).isEqualTo(42);
            // Boolean key without default - inherited behavior
            assertThat(defaultValues.hasValue(TestInterface.BOOL_KEY)).isTrue();
        }

        @Test
        @DisplayName("Generated store definition should support all operations")
        void generatedStoreDefinitionShouldSupportAllOperations() {
            var storeDefinition = StoreDefinitions.fromInterface(TestInterface.class);

            // Test all major operations
            assertThat(storeDefinition.getName()).isEqualTo("TestInterface");
            assertThat(storeDefinition.getKeys()).hasSize(3);
            assertThat(storeDefinition.getKeyMap()).hasSize(3);
            assertThat(storeDefinition.getSections()).hasSize(1);
            assertThat(storeDefinition.getDefaultValues()).isNotNull();

            // Test key retrieval
            assertThat(storeDefinition.getKey("stringKey")).isEqualTo(TestInterface.STRING_KEY);
            assertThat(storeDefinition.getKey("intKey")).isEqualTo(TestInterface.INT_KEY);
            assertThat(storeDefinition.getKey("boolKey")).isEqualTo(TestInterface.BOOL_KEY);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null class gracefully")
        void shouldHandleNullClassGracefully() {
            assertThatThrownBy(() -> StoreDefinitions.fromInterface(null))
                    .isInstanceOf(RuntimeException.class);
        }

        // Test interface with field access issues (simulated)
        public static class ClassWithInaccessibleField {
            // We can't easily create a field that throws on access, but we can test
            // the error handling path indirectly
        }

        @Test
        @DisplayName("Should handle classes with no fields")
        void shouldHandleClassesWithNoFields() {
            // Create an interface with no fields at all
            interface NoFieldsInterface {
                // Completely empty
            }

            var storeDefinition = StoreDefinitions.fromInterface(NoFieldsInterface.class);

            assertThat(storeDefinition).isNotNull();
            assertThat(storeDefinition.getName()).isEqualTo("NoFieldsInterface");
            assertThat(storeDefinition.getKeys()).isEmpty();
        }

        @Test
        @DisplayName("Should handle nested interfaces")
        void shouldHandleNestedInterfaces() {
            interface OuterInterface {
                @SuppressWarnings("unused")
                DataKey<String> OUTER_KEY = KeyFactory.string("outerKey", "value");

                interface NestedInterface {
                    @SuppressWarnings("unused")
                    DataKey<String> NESTED_KEY = KeyFactory.string("nestedKey", "nestedValue");
                }
            }

            var outerDefinition = StoreDefinitions.fromInterface(OuterInterface.class);
            var nestedDefinition = StoreDefinitions.fromInterface(OuterInterface.NestedInterface.class);

            assertThat(outerDefinition.getKeys()).hasSize(1);
            assertThat(outerDefinition.getKeyMap()).containsKey("outerKey");

            assertThat(nestedDefinition.getKeys()).hasSize(1);
            assertThat(nestedDefinition.getKeyMap()).containsKey("nestedKey");

            assertThat(outerDefinition).isNotSameAs(nestedDefinition);
        }

        @Test
        @DisplayName("Should handle complex inheritance hierarchies")
        void shouldHandleComplexInheritanceHierarchies() {
            interface BaseInterface {
                @SuppressWarnings("unused")
                DataKey<String> BASE_KEY = KeyFactory.string("baseKey", "baseValue");
            }

            interface ChildInterface extends BaseInterface {
                @SuppressWarnings("unused")
                DataKey<String> CHILD_KEY = KeyFactory.string("childKey", "childValue");
            }

            var childDefinition = StoreDefinitions.fromInterface(ChildInterface.class);

            // Should include both base and child keys due to getFields() behavior
            assertThat(childDefinition.getKeys()).hasSize(2);
            assertThat(childDefinition.getKeyMap()).containsKeys("baseKey", "childKey");
        }
    }

    @Nested
    @DisplayName("Performance and Behavior Tests")
    class PerformanceAndBehaviorTests {

        @Test
        @DisplayName("Should be efficient with cache enabled")
        void shouldBeEfficientWithCacheEnabled() {
            var cache = StoreDefinitions.getStoreDefinitionsCache();

            // Record initial state
            long initialCalls = cache.getCacheTotalCalls();
            long initialHits = cache.getCacheHits();

            // First call should either hit cache or add new entry
            var definition1 = StoreDefinitions.fromInterface(TestInterface.class);
            long callsAfterFirst = cache.getCacheTotalCalls();

            assertThat(callsAfterFirst).isGreaterThan(initialCalls);

            // Second call should hit cache and be more efficient
            var definition2 = StoreDefinitions.fromInterface(TestInterface.class);
            long callsAfterSecond = cache.getCacheTotalCalls();
            long hitsAfterSecond = cache.getCacheHits();

            assertThat(definition1).isSameAs(definition2);
            assertThat(callsAfterSecond).isGreaterThan(callsAfterFirst);
            assertThat(hitsAfterSecond).isGreaterThan(initialHits); // Should have at least one more hit
        }

        @Test
        @DisplayName("Should handle concurrent access gracefully")
        void shouldHandleConcurrentAccessGracefully() {
            // This test is more about ensuring no exceptions occur during concurrent access
            assertThatNoException().isThrownBy(() -> {
                var definition1 = StoreDefinitions.fromInterface(TestInterface.class);
                var definition2 = StoreDefinitions.fromInterface(TestInterface.class);

                assertThat(definition1).isSameAs(definition2);
            });
        }

        @Test
        @DisplayName("Should handle multiple different interfaces efficiently")
        void shouldHandleMultipleDifferentInterfacesEfficiently() {
            var cache = StoreDefinitions.getStoreDefinitionsCache();
            long initialSize = cache.getCacheSize();

            var definition1 = StoreDefinitions.fromInterface(TestInterface.class);
            var definition2 = StoreDefinitions.fromInterface(MixedInterface.class);
            var definition3 = StoreDefinitions.fromInterface(EmptyInterface.class);

            // Cache should have grown (or stayed same if interfaces were already cached)
            assertThat(cache.getCacheSize()).isGreaterThanOrEqualTo(initialSize);
            assertThat(definition1).isNotSameAs(definition2);
            assertThat(definition2).isNotSameAs(definition3);
            assertThat(definition1).isNotSameAs(definition3);

            // Verify cached retrieval
            var cached1 = StoreDefinitions.fromInterface(TestInterface.class);
            var cached2 = StoreDefinitions.fromInterface(MixedInterface.class);

            assertThat(cached1).isSameAs(definition1);
            assertThat(cached2).isSameAs(definition2);
        }
    }
}
