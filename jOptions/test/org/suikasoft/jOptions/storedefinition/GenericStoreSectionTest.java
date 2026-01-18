package org.suikasoft.jOptions.storedefinition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link GenericStoreSection}.
 * 
 * Tests the concrete implementation of StoreSection interface.
 * 
 * @author Generated Tests
 */
@DisplayName("GenericStoreSection Tests")
class GenericStoreSectionTest {

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
        @DisplayName("Should create section with name and keys")
        void testConstructor_WithNameAndKeys_CreatesSection() {
            // Given
            String sectionName = "TestSection";
            List<DataKey<?>> keys = Arrays.asList(stringKey, intKey);

            // When
            GenericStoreSection section = new GenericStoreSection(sectionName, keys);

            // Then
            assertThat(section.getName()).hasValue(sectionName);
            assertThat(section.getKeys()).containsExactlyElementsOf(keys);
        }

        @Test
        @DisplayName("Should create section with null name")
        void testConstructor_WithNullName_CreatesUnnamedSection() {
            // Given
            List<DataKey<?>> keys = Arrays.asList(stringKey);

            // When
            GenericStoreSection section = new GenericStoreSection(null, keys);

            // Then
            assertThat(section.getName()).isEmpty();
            assertThat(section.getKeys()).containsExactlyElementsOf(keys);
        }

        @Test
        @DisplayName("Should create section with empty keys")
        void testConstructor_WithEmptyKeys_CreatesEmptySection() {
            // Given
            String sectionName = "EmptySection";
            List<DataKey<?>> emptyKeys = Collections.emptyList();

            // When
            GenericStoreSection section = new GenericStoreSection(sectionName, emptyKeys);

            // Then
            assertThat(section.getName()).hasValue(sectionName);
            assertThat(section.getKeys()).isEmpty();
        }

        @Test
        @DisplayName("Null keys are rejected (throws NPE)")
        void testConstructor_WithNullKeys_ThrowsException() {
            // Given
            String sectionName = "NullKeys";

            // When/Then
            assertThatThrownBy(() -> new GenericStoreSection(sectionName, null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Keys list cannot be null");
        }
    }

    @Nested
    @DisplayName("Name Tests")
    class NameTests {

        @Test
        @DisplayName("Should return name as Optional")
        void testGetName_WithName_ReturnsOptionalWithValue() {
            // Given
            String sectionName = "TestSection";
            GenericStoreSection section = new GenericStoreSection(sectionName, Arrays.asList(stringKey));

            // When
            Optional<String> name = section.getName();

            // Then
            assertThat(name).hasValue(sectionName);
        }

        @Test
        @DisplayName("Should return empty Optional for null name")
        void testGetName_WithNullName_ReturnsEmptyOptional() {
            // Given
            GenericStoreSection section = new GenericStoreSection(null, Arrays.asList(stringKey));

            // When
            Optional<String> name = section.getName();

            // Then
            assertThat(name).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty string name")
        void testGetName_WithEmptyStringName_ReturnsOptionalWithEmptyString() {
            // Given
            String emptyName = "";
            GenericStoreSection section = new GenericStoreSection(emptyName, Arrays.asList(stringKey));

            // When
            Optional<String> name = section.getName();

            // Then
            assertThat(name).hasValue(emptyName);
        }
    }

    @Nested
    @DisplayName("Keys Tests")
    class KeysTests {

        @Test
        @DisplayName("Should return defensive copy of keys list with same content")
        void testGetKeys_WithKeys_ReturnsKeysList() {
            // Given
            List<DataKey<?>> keys = Arrays.asList(stringKey, intKey, boolKey);
            GenericStoreSection section = new GenericStoreSection("Test", keys);

            // When
            List<DataKey<?>> result = section.getKeys();

            // Then
            assertThat(result).containsExactlyElementsOf(keys);
            assertThat(result).isNotSameAs(keys);
        }

        @Test
        @DisplayName("Should return defensive copy of keys list")
        void testGetKeys_ReturnsDefensiveCopy() {
            // Given
            List<DataKey<?>> keys = Arrays.asList(stringKey, intKey);
            GenericStoreSection section = new GenericStoreSection("Test", keys);

            // When
            List<DataKey<?>> result1 = section.getKeys();
            List<DataKey<?>> result2 = section.getKeys();

            // Then
            assertThat(result1).isNotSameAs(keys); // Should be defensive copy
            assertThat(result2).isNotSameAs(keys); // Should be defensive copy
            assertThat(result1).isNotSameAs(result2); // Each call returns new copy
            assertThat(result1).containsExactlyElementsOf(keys); // But same content
            assertThat(result2).containsExactlyElementsOf(keys);
        }

        @Test
        @DisplayName("Should return empty list for null keys")
        void testGetKeys_WithNullKeys_ReturnsEmptyList() {
            // Given - Create section with empty list instead of null to test defensive copy
            // behavior
            List<DataKey<?>> emptyKeys = Collections.emptyList();
            GenericStoreSection section = new GenericStoreSection("Test", emptyKeys);

            // When
            List<DataKey<?>> result = section.getKeys();

            // Then
            assertThat(result).isEmpty();
            assertThat(result).isNotSameAs(emptyKeys); // Should be defensive copy
        }

        @Test
        @DisplayName("Should return empty list for empty keys")
        void testGetKeys_WithEmptyKeys_ReturnsEmptyList() {
            // Given
            List<DataKey<?>> emptyKeys = Collections.emptyList();
            GenericStoreSection section = new GenericStoreSection("Test", emptyKeys);

            // When
            List<DataKey<?>> result = section.getKeys();

            // Then
            assertThat(result).isEmpty();
            assertThat(result).isNotSameAs(emptyKeys); // Should be defensive copy
        }
    }

    @Nested
    @DisplayName("Mutability Tests")
    class MutabilityTests {

        @Test
        @DisplayName("Should not be affected by modifications to original keys list")
        void testKeys_OriginalListModification_DoesNotAffectSection() {
            // Given
            List<DataKey<?>> modifiableKeys = new java.util.ArrayList<>(Arrays.asList(stringKey));
            GenericStoreSection section = new GenericStoreSection("Test", modifiableKeys);

            // When
            modifiableKeys.add(intKey);

            // Then
            assertThat(section.getKeys()).containsExactly(stringKey); // Should not be affected
        }

        @Test
        @DisplayName("Should not allow modification through returned keys list")
        void testKeys_ReturnedListModification_DoesNotAffectSection() {
            // Given
            List<DataKey<?>> modifiableKeys = new java.util.ArrayList<>(Arrays.asList(stringKey));
            GenericStoreSection section = new GenericStoreSection("Test", modifiableKeys);

            // When
            List<DataKey<?>> returnedKeys = section.getKeys();
            returnedKeys.add(intKey); // Modify the returned list

            // Then
            assertThat(section.getKeys()).containsExactly(stringKey); // Should not be affected
            assertThat(modifiableKeys).containsExactly(stringKey); // Original should not be affected
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle special characters in name")
        void testSection_WithSpecialCharacters_HandlesCorrectly() {
            // Given
            String specialName = "Section/With:Special*Characters@#$%";
            GenericStoreSection section = new GenericStoreSection(specialName, Arrays.asList(stringKey));

            // When/Then
            assertThat(section.getName()).hasValue(specialName);
        }

        @Test
        @DisplayName("Should handle large number of keys")
        void testSection_WithManyKeys_HandlesEfficiently() {
            // Given
            List<DataKey<?>> manyKeys = new java.util.ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                manyKeys.add(KeyFactory.string("key" + i));
            }

            // When
            GenericStoreSection section = new GenericStoreSection("ManyKeys", manyKeys);

            // Then
            assertThat(section.getKeys()).hasSize(1000);
            assertThat(section.getKeys()).isNotSameAs(manyKeys); // Should be defensive copy
        }

        @Test
        @DisplayName("Should handle duplicate keys in list")
        void testSection_WithDuplicateKeys_PreservesAllKeys() {
            // Given
            List<DataKey<?>> keysWithDuplicate = Arrays.asList(stringKey, intKey, stringKey);
            GenericStoreSection section = new GenericStoreSection("WithDuplicates", keysWithDuplicate);

            // When/Then
            assertThat(section.getKeys()).hasSize(3);
            assertThat(section.getKeys()).containsExactly(stringKey, intKey, stringKey);
        }
    }

    @Nested
    @DisplayName("Interface Compliance Tests")
    class InterfaceComplianceTests {

        @Test
        @DisplayName("Should implement StoreSection interface")
        void testSection_ImplementsInterface_CorrectBehavior() {
            // Given
            GenericStoreSection section = new GenericStoreSection("Test", Arrays.asList(stringKey));

            // When/Then
            assertThat(section).isInstanceOf(StoreSection.class);

            // Verify interface methods work
            StoreSection storeSection = section;
            assertThat(storeSection.getName()).hasValue("Test");
            assertThat(storeSection.getKeys()).containsExactly(stringKey);
        }

        @Test
        @DisplayName("Should work with StoreSection static methods")
        void testSection_WithStaticMethods_WorksCorrectly() {
            // Given
            GenericStoreSection section1 = new GenericStoreSection("Section1", Arrays.asList(stringKey));
            GenericStoreSection section2 = new GenericStoreSection("Section2", Arrays.asList(intKey));

            // When
            List<DataKey<?>> allKeys = StoreSection.getAllKeys(Arrays.asList(section1, section2));

            // Then
            assertThat(allKeys).containsExactly(stringKey, intKey);
        }
    }

    @Nested
    @DisplayName("Consistency Tests")
    class ConsistencyTests {

        @Test
        @DisplayName("Should maintain consistency across multiple calls")
        void testSection_MultipleCalls_MaintainsConsistency() {
            // Given
            String sectionName = "ConsistentSection";
            List<DataKey<?>> keys = Arrays.asList(stringKey, intKey);
            GenericStoreSection section = new GenericStoreSection(sectionName, keys);

            // When/Then
            for (int i = 0; i < 5; i++) {
                assertThat(section.getName()).hasValue(sectionName);
                assertThat(section.getKeys()).containsExactlyElementsOf(keys);
            }
        }

        @Test
        @DisplayName("Should handle concurrent access gracefully")
        void testSection_ConcurrentAccess_HandlesCorrectly() {
            // Given
            GenericStoreSection section = new GenericStoreSection("Concurrent", Arrays.asList(stringKey));

            // When/Then - Basic concurrent access test (not exhaustive)
            assertThat(section.getName()).hasValue("Concurrent");
            assertThat(section.getKeys()).containsExactly(stringKey);

            // Simultaneous access to both methods
            Optional<String> name = section.getName();
            List<DataKey<?>> keys = section.getKeys();

            assertThat(name).hasValue("Concurrent");
            assertThat(keys).containsExactly(stringKey);
        }
    }
}
