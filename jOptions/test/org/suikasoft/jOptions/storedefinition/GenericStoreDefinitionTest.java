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

/**
 * Comprehensive test suite for {@link GenericStoreDefinition}.
 * 
 * Tests the default implementation of StoreDefinition, including
 * constructor behavior, inherited functionality from AStoreDefinition,
 * and the custom toString() implementation.
 * 
 * @author Generated Tests
 */
@DisplayName("GenericStoreDefinition Tests")
class GenericStoreDefinitionTest {

    private DataKey<String> testStringKey;
    private DataKey<Integer> testIntKey;
    private DataKey<Boolean> testBoolKey;
    private List<DataKey<?>> testKeys;
    private GenericStoreDefinition storeDefinition;

    @BeforeEach
    void setUp() {
        testStringKey = KeyFactory.string("testString", "defaultValue");
        testIntKey = KeyFactory.integer("testInt", 42);
        testBoolKey = KeyFactory.bool("testBool");
        testKeys = Arrays.asList(testStringKey, testIntKey, testBoolKey);
        storeDefinition = new GenericStoreDefinition("TestStore", testKeys);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create store definition with valid parameters")
        void shouldCreateStoreDefinitionWithValidParameters() {
            var definition = new GenericStoreDefinition("ValidStore", testKeys);

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

            var definition = new GenericStoreDefinition("SectionStore", sections, defaultData);

            assertThat(definition.getName()).isEqualTo("SectionStore");
            assertThat(definition.getSections()).containsExactly(section);
            assertThat(definition.getDefaultValues()).isEqualTo(defaultData);
        }

        @Test
        @DisplayName("Should handle empty key list")
        void shouldHandleEmptyKeyList() {
            List<DataKey<?>> emptyKeys = Arrays.asList();
            var definition = new GenericStoreDefinition("EmptyStore", emptyKeys);

            assertThat(definition.getName()).isEqualTo("EmptyStore");
            assertThat(definition.getKeys()).isEmpty();
            assertThat(definition.getSections()).hasSize(1);
        }

        @Test
        @DisplayName("Should handle null name")
        void shouldHandleNullName() {
            // Test allows null name - this is the current behavior (inherited from
            // AStoreDefinition)
            assertThatNoException().isThrownBy(() -> new GenericStoreDefinition(null, testKeys));
        }

        @Test
        @DisplayName("Should reject duplicate key names")
        void shouldRejectDuplicateKeyNames() {
            var duplicateKey = KeyFactory.string("testString", "differentDefault");
            List<DataKey<?>> keysWithDuplicate = Arrays.asList(testStringKey, duplicateKey, testIntKey);

            assertThatThrownBy(() -> new GenericStoreDefinition("DuplicateStore", keysWithDuplicate))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Duplicate key name: 'testString'");
        }

        @Test
        @DisplayName("Should handle null sections list")
        void shouldHandleNullSectionsList() {
            assertThatThrownBy(() -> new GenericStoreDefinition("NullSections", null, null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Inherited Functionality Tests")
    class InheritedFunctionalityTests {

        @Test
        @DisplayName("Should inherit name management from AStoreDefinition")
        void shouldInheritNameManagementFromAStoreDefinition() {
            assertThat(storeDefinition.getName()).isEqualTo("TestStore");
        }

        @Test
        @DisplayName("Should inherit key management from AStoreDefinition")
        void shouldInheritKeyManagementFromAStoreDefinition() {
            var keys = storeDefinition.getKeys();
            assertThat(keys).containsExactlyElementsOf(testKeys);
            assertThat(keys).hasSize(3);

            var keyMap = storeDefinition.getKeyMap();
            assertThat(keyMap).hasSize(3);
            assertThat(keyMap.get("testString")).isEqualTo(testStringKey);
            assertThat(keyMap.get("testInt")).isEqualTo(testIntKey);
            assertThat(keyMap.get("testBool")).isEqualTo(testBoolKey);
        }

        @Test
        @DisplayName("Should inherit section management from AStoreDefinition")
        void shouldInheritSectionManagementFromAStoreDefinition() {
            var sections = storeDefinition.getSections();
            assertThat(sections).hasSize(1);
            assertThat(sections.get(0).getKeys()).containsExactlyElementsOf(testKeys);
        }

        @Test
        @DisplayName("Should inherit default value management from AStoreDefinition")
        void shouldInheritDefaultValueManagementFromAStoreDefinition() {
            var defaultValues = storeDefinition.getDefaultValues();

            assertThat(defaultValues).isNotNull();
            assertThat(defaultValues.get(testStringKey)).isEqualTo("defaultValue");
            assertThat(defaultValues.get(testIntKey)).isEqualTo(42);
            // Keys without defaults still have values (implementation behavior)
            assertThat(defaultValues.hasValue(testBoolKey)).isTrue();
        }

        @Test
        @DisplayName("Should inherit defensive copy behavior from AStoreDefinition")
        void shouldInheritDefensiveCopyBehaviorFromAStoreDefinition() {
            var sections = storeDefinition.getSections();

            // Implementation provides defensive copies - modifying returned list doesn't
            // affect original
            sections.clear();
            assertThat(storeDefinition.getSections()).hasSize(1); // Implementation correctly maintains original
        }
    }

    @Nested
    @DisplayName("ToString Implementation Tests")
    class ToStringImplementationTests {

        @Test
        @DisplayName("Should format toString correctly with keys")
        void shouldFormatToStringCorrectlyWithKeys() {
            var result = storeDefinition.toString();

            assertThat(result).startsWith("TestStore -> [");
            assertThat(result).contains("testString");
            assertThat(result).contains("testInt");
            assertThat(result).contains("testBool");
            assertThat(result).endsWith("]");
        }

        @Test
        @DisplayName("Should handle toString with empty keys")
        void shouldHandleToStringWithEmptyKeys() {
            List<DataKey<?>> emptyKeys = Arrays.asList();
            var emptyDefinition = new GenericStoreDefinition("EmptyStore", emptyKeys);

            var result = emptyDefinition.toString();
            assertThat(result).isEqualTo("EmptyStore -> []");
        }

        @Test
        @DisplayName("Should handle toString with null name")
        void shouldHandleToStringWithNullName() {
            var definition = new GenericStoreDefinition(null, testKeys);

            var result = definition.toString();
            assertThat(result).startsWith("null -> [");
            assertThat(result).contains("testString");
        }

        @Test
        @DisplayName("Should format toString consistently")
        void shouldFormatToStringConsistently() {
            var result1 = storeDefinition.toString();
            var result2 = storeDefinition.toString();

            assertThat(result1).isEqualTo(result2);
        }

        @Test
        @DisplayName("Should differentiate toString for different instances")
        void shouldDifferentiateToStringForDifferentInstances() {
            List<DataKey<?>> otherKeys = Arrays.asList(KeyFactory.string("other", "value"));
            var otherDefinition = new GenericStoreDefinition("OtherStore", otherKeys);

            var result1 = storeDefinition.toString();
            var result2 = otherDefinition.toString();

            assertThat(result1).isNotEqualTo(result2);
            assertThat(result1).contains("TestStore");
            assertThat(result2).contains("OtherStore");
        }
    }

    @Nested
    @DisplayName("Multiple Section Tests")
    class MultipleSectionTests {

        @Test
        @DisplayName("Should handle multiple sections in toString")
        void shouldHandleMultipleSectionsInToString() {
            var section1 = StoreSection.newInstance(Arrays.asList(testStringKey));
            var section2 = StoreSection.newInstance(Arrays.asList(testIntKey, testBoolKey));
            var sections = Arrays.asList(section1, section2);

            var definition = new GenericStoreDefinition("MultiSectionStore", sections, null);

            var result = definition.toString();
            assertThat(result).startsWith("MultiSectionStore -> [");
            assertThat(result).contains("testString");
            assertThat(result).contains("testInt");
            assertThat(result).contains("testBool");
        }

        @Test
        @DisplayName("Should work with custom default values")
        void shouldWorkWithCustomDefaultValues() {
            var customDefaults = new SimpleDataStore("CustomStore");
            customDefaults.set(testStringKey, "customValue");
            customDefaults.set(testIntKey, 999);

            var sections = Arrays.asList(StoreSection.newInstance(testKeys));
            var definition = new GenericStoreDefinition("CustomDefaultStore", sections, customDefaults);

            assertThat(definition.getName()).isEqualTo("CustomDefaultStore");
            assertThat(definition.getDefaultValues()).isSameAs(customDefaults);

            var result = definition.toString();
            assertThat(result).startsWith("CustomDefaultStore -> [");
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

            var definition = new GenericStoreDefinition("ComplexStore", sections, null);

            assertThat(definition.getName()).isEqualTo("ComplexStore");
            assertThat(definition.getSections()).hasSize(2);
            assertThat(definition.getKeys()).hasSize(4);
            assertThat(definition.getKeyMap()).hasSize(4);

            var result = definition.toString();
            assertThat(result).startsWith("ComplexStore -> [");
            assertThat(result).contains("string1");
            assertThat(result).contains("int1");
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
            var stringRepresentation = storeDefinition.toString();

            // Verify consistency
            assertThat(name).isEqualTo("TestStore");
            assertThat(keys).hasSize(3);
            assertThat(sections).hasSize(1);
            assertThat(keyMap).hasSize(3);
            assertThat(defaults).isNotNull();
            assertThat(stringRepresentation).contains("TestStore");

            // Verify key consistency
            for (DataKey<?> key : keys) {
                assertThat(keyMap.get(key.getName())).isEqualTo(key);
                assertThat(stringRepresentation).contains(key.getName());
            }
        }

        @Test
        @DisplayName("Should handle edge cases consistently")
        void shouldHandleEdgeCasesConsistently() {
            // Test with single key
            List<DataKey<?>> singleKey = Arrays.asList(testStringKey);
            var singleDefinition = new GenericStoreDefinition("SingleKey", singleKey);

            assertThat(singleDefinition.getKeys()).hasSize(1);
            assertThat(singleDefinition.toString()).contains("testString");

            // Test with empty name
            var emptyNameDefinition = new GenericStoreDefinition("", testKeys);
            assertThat(emptyNameDefinition.getName()).isEmpty();
            assertThat(emptyNameDefinition.toString()).startsWith(" -> [");
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

            assertThatThrownBy(() -> new GenericStoreDefinition("DuplicateAcrossSections", sections, null))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Duplicate key name: 'testString'");
        }

        @Test
        @DisplayName("Should handle null keys in sections")
        void shouldHandleNullKeysInSections() {
            // This depends on StoreSection implementation
            assertThatThrownBy(() -> {
                var section = StoreSection.newInstance(Arrays.asList(testStringKey, null));
                new GenericStoreDefinition("NullKeyStore", Arrays.asList(section), null);
            }).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should validate section consistency")
        void shouldValidateSectionConsistency() {
            var validSection = StoreSection.newInstance(testKeys);
            var sections = Arrays.asList(validSection);

            assertThatNoException().isThrownBy(() -> new GenericStoreDefinition("ValidSectionStore", sections, null));
        }

        @Test
        @DisplayName("Should handle toString with problematic data gracefully")
        void shouldHandleToStringWithProblematicDataGracefully() {
            // Test toString doesn't fail even with edge case data
            var definition = new GenericStoreDefinition(null, testKeys);
            assertThatNoException().isThrownBy(() -> definition.toString());

            List<DataKey<?>> emptyKeys = Arrays.asList();
            var emptyDefinition = new GenericStoreDefinition("", emptyKeys);
            assertThatNoException().isThrownBy(() -> emptyDefinition.toString());
        }
    }
}
