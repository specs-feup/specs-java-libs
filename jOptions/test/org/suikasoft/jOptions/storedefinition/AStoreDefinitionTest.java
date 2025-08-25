package org.suikasoft.jOptions.storedefinition;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Comprehensive test suite for {@link AStoreDefinition}.
 * 
 * Tests the abstract base class implementation for store definitions,
 * including constructor validation, key management, section handling,
 * and default value management.
 * 
 * @author Generated Tests
 */
@DisplayName("AStoreDefinition Tests")
class AStoreDefinitionTest {

    private DataKey<String> testStringKey;
    private DataKey<Integer> testIntKey;
    private DataKey<Boolean> testBoolKey;
    private List<DataKey<?>> testKeys;
    private TestStoreDefinition storeDefinition;

    /**
     * Concrete implementation of AStoreDefinition for testing
     */
    private static class TestStoreDefinition extends AStoreDefinition {
        public TestStoreDefinition(String appName, List<DataKey<?>> options) {
            super(appName, options);
        }

        public TestStoreDefinition(String appName, List<StoreSection> sections, DataStore defaultData) {
            super(appName, sections, defaultData);
        }
    }

    @BeforeEach
    void setUp() {
        testStringKey = KeyFactory.string("testString", "defaultValue");
        testIntKey = KeyFactory.integer("testInt", 42);
        testBoolKey = KeyFactory.bool("testBool");
        testKeys = Arrays.asList(testStringKey, testIntKey, testBoolKey);
        storeDefinition = new TestStoreDefinition("TestStore", testKeys);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create store definition with valid parameters")
        void shouldCreateStoreDefinitionWithValidParameters() {
            var definition = new TestStoreDefinition("ValidStore", testKeys);

            assertThat(definition.getName()).isEqualTo("ValidStore");
            assertThat(definition.getKeys()).containsExactlyElementsOf(testKeys);
            assertThat(definition.getSections()).hasSize(1);
        }

        @Test
        @DisplayName("Should create store definition with sections and default data")
        void shouldCreateStoreDefinitionWithSectionsAndDefaultData() {
            var section = StoreSection.newInstance(testKeys);
            var sections = Arrays.asList(section);
            var defaultData = new SimpleDataStore("TestStore");
            defaultData.set(testStringKey, "customDefault");

            var definition = new TestStoreDefinition("SectionStore", sections, defaultData);

            assertThat(definition.getName()).isEqualTo("SectionStore");
            assertThat(definition.getSections()).containsExactly(section);
            assertThat(definition.getDefaultValues()).isEqualTo(defaultData);
        }

        @Test
        @DisplayName("Should handle empty key list")
        void shouldHandleEmptyKeyList() {
            List<DataKey<?>> emptyKeys = Arrays.asList();
            var definition = new TestStoreDefinition("EmptyStore", emptyKeys);

            assertThat(definition.getName()).isEqualTo("EmptyStore");
            assertThat(definition.getKeys()).isEmpty();
            assertThat(definition.getSections()).hasSize(1);
        }

        @Test
        @DisplayName("Should handle null name")
        void shouldHandleNullName() {
            // Test allows null name - this is the current behavior
            assertThatNoException().isThrownBy(() -> new TestStoreDefinition(null, testKeys));
        }

        @Test
        @DisplayName("Should reject duplicate key names")
        void shouldRejectDuplicateKeyNames() {
            var duplicateKey = KeyFactory.string("testString", "differentDefault");
            List<DataKey<?>> keysWithDuplicate = Arrays.asList(testStringKey, duplicateKey, testIntKey);

            assertThatThrownBy(() -> new TestStoreDefinition("DuplicateStore", keysWithDuplicate))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Duplicate key name: 'testString'");
        }

        @Test
        @DisplayName("Should handle null sections list")
        void shouldHandleNullSectionsList() {
            assertThatThrownBy(() -> new TestStoreDefinition("NullSections", null, null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Name Management Tests")
    class NameManagementTests {

        @Test
        @DisplayName("Should return correct store name")
        void shouldReturnCorrectStoreName() {
            assertThat(storeDefinition.getName()).isEqualTo("TestStore");
        }

        @Test
        @DisplayName("Should handle null name retrieval")
        void shouldHandleNullNameRetrieval() {
            var definition = new TestStoreDefinition(null, testKeys);
            assertThat(definition.getName()).isNull();
        }

        @Test
        @DisplayName("Should handle empty name")
        void shouldHandleEmptyName() {
            var definition = new TestStoreDefinition("", testKeys);
            assertThat(definition.getName()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Key Management Tests")
    class KeyManagementTests {

        @Test
        @DisplayName("Should return all keys")
        void shouldReturnAllKeys() {
            var keys = storeDefinition.getKeys();

            assertThat(keys).containsExactlyElementsOf(testKeys);
            assertThat(keys).hasSize(3);
        }

        @Test
        @DisplayName("Should build key map correctly")
        void shouldBuildKeyMapCorrectly() {
            var keyMap = storeDefinition.getKeyMap();

            assertThat(keyMap).hasSize(3);
            assertThat(keyMap.get("testString")).isEqualTo(testStringKey);
            assertThat(keyMap.get("testInt")).isEqualTo(testIntKey);
            assertThat(keyMap.get("testBool")).isEqualTo(testBoolKey);
        }

        @Test
        @DisplayName("Should cache key map")
        void shouldCacheKeyMap() {
            var keyMap1 = storeDefinition.getKeyMap();
            var keyMap2 = storeDefinition.getKeyMap();

            assertThat(keyMap1).isSameAs(keyMap2);
        }

        @Test
        @DisplayName("Should handle empty key list")
        void shouldHandleEmptyKeyList() {
            List<DataKey<?>> emptyKeys = Arrays.asList();
            var emptyDefinition = new TestStoreDefinition("EmptyStore", emptyKeys);

            assertThat(emptyDefinition.getKeys()).isEmpty();
            assertThat(emptyDefinition.getKeyMap()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Section Management Tests")
    class SectionManagementTests {

        @Test
        @DisplayName("Should return sections list")
        void shouldReturnSectionsList() {
            var sections = storeDefinition.getSections();

            assertThat(sections).hasSize(1);
            assertThat(sections.get(0).getKeys()).containsExactlyElementsOf(testKeys);
        }

        @Test
        @DisplayName("Should handle multiple sections")
        void shouldHandleMultipleSections() {
            var section1 = StoreSection.newInstance(Arrays.asList(testStringKey));
            var section2 = StoreSection.newInstance(Arrays.asList(testIntKey, testBoolKey));
            var sections = Arrays.asList(section1, section2);

            var definition = new TestStoreDefinition("MultiSectionStore", sections, null);

            assertThat(definition.getSections()).hasSize(2);
            assertThat(definition.getSections()).containsExactly(section1, section2);
        }

        @Test
        @DisplayName("Should prevent sections modification")
        void shouldPreventSectionsModification() {
            var sections = storeDefinition.getSections();

            // Modifying returned list must not affect original
            sections.clear();
            assertThat(storeDefinition.getSections()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Default Values Tests")
    class DefaultValuesTests {

        @Test
        @DisplayName("Should generate default values from keys")
        void shouldGenerateDefaultValuesFromKeys() {
            var defaultValues = storeDefinition.getDefaultValues();

            assertThat(defaultValues).isNotNull();
            assertThat(defaultValues.get(testStringKey)).isEqualTo("defaultValue");
            assertThat(defaultValues.get(testIntKey)).isEqualTo(42);
            // Keys without an explicit default value still have default values
            // (implementation behavior)
            assertThat(defaultValues.hasValue(testBoolKey)).isTrue();
        }

        @Test
        @DisplayName("Should use provided default data")
        void shouldUseProvidedDefaultData() {
            var customDefaults = new SimpleDataStore("CustomStore");
            customDefaults.set(testStringKey, "customValue");
            customDefaults.set(testIntKey, 999);

            var sections = Arrays.asList(StoreSection.newInstance(testKeys));
            var definition = new TestStoreDefinition("CustomDefaultStore", sections, customDefaults);

            var defaultValues = definition.getDefaultValues();
            assertThat(defaultValues).isSameAs(customDefaults);
            assertThat(defaultValues.get(testStringKey)).isEqualTo("customValue");
            assertThat(defaultValues.get(testIntKey)).isEqualTo(999);
        }

        @Test
        @DisplayName("Should handle keys without defaults")
        void shouldHandleKeysWithoutDefaults() {
            var keyWithoutDefault = KeyFactory.string("noDefault");
            List<DataKey<?>> keysWithoutDefaults = Arrays.asList(keyWithoutDefault, testStringKey);
            var definition = new TestStoreDefinition("NoDefaultStore", keysWithoutDefaults);

            var defaultValues = definition.getDefaultValues();

            assertThat(defaultValues).isNotNull();
            assertThat(defaultValues.get(testStringKey)).isEqualTo("defaultValue");
            // Keys without defaults still have values (implementation behavior)
            assertThat(defaultValues.hasValue(keyWithoutDefault)).isTrue();
        }

        @Test
        @DisplayName("Should handle null default data")
        void shouldHandleNullDefaultData() {
            var sections = Arrays.asList(StoreSection.newInstance(testKeys));
            var definition = new TestStoreDefinition("NullDefaultStore", sections, null);

            var defaultValues = definition.getDefaultValues();

            assertThat(defaultValues).isNotNull();
            assertThat(defaultValues.get(testStringKey)).isEqualTo("defaultValue");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with complex store structure")
        void shouldWorkWithComplexStoreStructure() {
            var stringSection = StoreSection.newInstance(Arrays.asList(
                    KeyFactory.string("string1", "default1"),
                    KeyFactory.string("string2", "default2")));
            var numberSection = StoreSection.newInstance(Arrays.asList(
                    KeyFactory.integer("int1", 100),
                    KeyFactory.integer("int2", 200)));
            var sections = Arrays.asList(stringSection, numberSection);

            var definition = new TestStoreDefinition("ComplexStore", sections, null);

            assertThat(definition.getName()).isEqualTo("ComplexStore");
            assertThat(definition.getSections()).hasSize(2);
            assertThat(definition.getKeys()).hasSize(4);
            assertThat(definition.getKeyMap()).hasSize(4);

            var defaultValues = definition.getDefaultValues();
            assertThat(defaultValues.get(KeyFactory.string("string1", "default1"))).isEqualTo("default1");
            assertThat(defaultValues.get(KeyFactory.integer("int1", 100))).isEqualTo(100);
        }

        @Test
        @DisplayName("Should maintain consistency across operations")
        void shouldMaintainConsistencyAcrossOperations() {
            // Perform multiple operations
            var name = storeDefinition.getName();
            var keys = storeDefinition.getKeys();
            var sections = storeDefinition.getSections();
            var keyMap = storeDefinition.getKeyMap();
            var defaults = storeDefinition.getDefaultValues();

            // Verify consistency
            assertThat(name).isEqualTo("TestStore");
            assertThat(keys).hasSize(3);
            assertThat(sections).hasSize(1);
            assertThat(keyMap).hasSize(3);
            assertThat(defaults).isNotNull();

            // Verify key consistency
            for (DataKey<?> key : keys) {
                assertThat(keyMap.get(key.getName())).isEqualTo(key);
            }
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle duplicate keys across sections")
        void shouldHandleDuplicateKeysAcrossSections() {
            var section1 = StoreSection.newInstance(Arrays.asList(testStringKey));
            var section2 = StoreSection.newInstance(Arrays.asList(
                    KeyFactory.string("testString", "differentDefault")));
            var sections = Arrays.asList(section1, section2);

            assertThatThrownBy(() -> new TestStoreDefinition("DuplicateAcrossSections", sections, null))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Duplicate key name: 'testString'");
        }

        @Test
        @DisplayName("Should handle null keys in sections")
        void shouldHandleNullKeysInSections() {
            // This depends on StoreSection implementation
            assertThatThrownBy(() -> {
                var section = StoreSection.newInstance(Arrays.asList(testStringKey, null));
                new TestStoreDefinition("NullKeyStore", Arrays.asList(section), null);
            }).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should validate section consistency")
        void shouldValidateSectionConsistency() {
            var validSection = StoreSection.newInstance(testKeys);
            var sections = Arrays.asList(validSection);

            assertThatNoException().isThrownBy(() -> new TestStoreDefinition("ValidSectionStore", sections, null));
        }
    }
}
