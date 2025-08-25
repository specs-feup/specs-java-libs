package org.suikasoft.jOptions.storedefinition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link StoreSection}.
 * 
 * Tests the interface for grouping related keys in store definitions.
 * 
 * @author Generated Tests
 */
@DisplayName("StoreSection Tests")
class StoreSectionTest {

    @Mock
    private StoreSection mockSection1;

    @Mock
    private StoreSection mockSection2;

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
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create named section with keys")
        void testNewInstance_WithNameAndKeys_CreatesNamedSection() {
            // Given
            String sectionName = "Configuration";
            List<DataKey<?>> keys = Arrays.asList(stringKey, intKey);

            // When
            StoreSection section = StoreSection.newInstance(sectionName, keys);

            // Then
            assertThat(section).isNotNull();
            assertThat(section.getName()).hasValue(sectionName);
            assertThat(section.getKeys()).containsExactlyElementsOf(keys);
        }

        @Test
        @DisplayName("Should create unnamed section with null name")
        void testNewInstance_WithNullNameAndKeys_CreatesUnnamedSection() {
            // Given
            List<DataKey<?>> keys = Arrays.asList(stringKey, intKey);

            // When
            StoreSection section = StoreSection.newInstance(null, keys);

            // Then
            assertThat(section).isNotNull();
            assertThat(section.getName()).isEmpty();
            assertThat(section.getKeys()).containsExactlyElementsOf(keys);
        }

        @Test
        @DisplayName("Should create unnamed section with keys only")
        void testNewInstance_WithKeysOnly_CreatesUnnamedSection() {
            // Given
            List<DataKey<?>> keys = Arrays.asList(stringKey, intKey);

            // When
            StoreSection section = StoreSection.newInstance(keys);

            // Then
            assertThat(section).isNotNull();
            assertThat(section.getName()).isEmpty();
            assertThat(section.getKeys()).containsExactlyElementsOf(keys);
        }

        @Test
        @DisplayName("Should create section with empty keys")
        void testNewInstance_WithEmptyKeys_CreatesEmptySection() {
            // Given
            String sectionName = "EmptySection";
            List<DataKey<?>> emptyKeys = Collections.emptyList();

            // When
            StoreSection section = StoreSection.newInstance(sectionName, emptyKeys);

            // Then
            assertThat(section).isNotNull();
            assertThat(section.getName()).hasValue(sectionName);
            assertThat(section.getKeys()).isEmpty();
        }

        @Test
        @DisplayName("Should create section with single key")
        void testNewInstance_WithSingleKey_CreatesSectionWithOneKey() {
            // Given
            String sectionName = "SingleKey";
            List<DataKey<?>> singleKey = Arrays.asList(stringKey);

            // When
            StoreSection section = StoreSection.newInstance(sectionName, singleKey);

            // Then
            assertThat(section).isNotNull();
            assertThat(section.getName()).hasValue(sectionName);
            assertThat(section.getKeys()).containsExactly(stringKey);
        }

        @Test
        @DisplayName("Null keys list should throw NPE")
        void testNewInstance_WithNullKeys_ThrowsNpe() {
            // Given
            String sectionName = "NullKeys";

            // When/Then
            assertThatThrownBy(() -> StoreSection.newInstance(sectionName, null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Static Utility Method Tests")
    class StaticUtilityMethodTests {

        @Test
        @DisplayName("Should get all keys from multiple sections")
        void testGetAllKeys_WithMultipleSections_ReturnsAllKeys() {
            // Given
            when(mockSection1.getKeys()).thenReturn(Arrays.asList(stringKey, intKey));
            when(mockSection2.getKeys()).thenReturn(Arrays.asList(boolKey));

            List<StoreSection> sections = Arrays.asList(mockSection1, mockSection2);

            // When
            List<DataKey<?>> allKeys = StoreSection.getAllKeys(sections);

            // Then
            assertThat(allKeys).containsExactly(stringKey, intKey, boolKey);
        }

        @Test
        @DisplayName("Should get all keys from single section")
        void testGetAllKeys_WithSingleSection_ReturnsKeysFromSection() {
            // Given
            when(mockSection1.getKeys()).thenReturn(Arrays.asList(stringKey, intKey));

            List<StoreSection> sections = Arrays.asList(mockSection1);

            // When
            List<DataKey<?>> allKeys = StoreSection.getAllKeys(sections);

            // Then
            assertThat(allKeys).containsExactly(stringKey, intKey);
        }

        @Test
        @DisplayName("Should handle empty sections list")
        void testGetAllKeys_WithEmptySections_ReturnsEmptyList() {
            // Given
            List<StoreSection> emptySections = Collections.emptyList();

            // When
            List<DataKey<?>> allKeys = StoreSection.getAllKeys(emptySections);

            // Then
            assertThat(allKeys).isEmpty();
        }

        @Test
        @DisplayName("Should handle sections with empty keys")
        void testGetAllKeys_WithEmptyKeySections_ReturnsEmptyList() {
            // Given
            when(mockSection1.getKeys()).thenReturn(Collections.emptyList());
            when(mockSection2.getKeys()).thenReturn(Collections.emptyList());

            List<StoreSection> sections = Arrays.asList(mockSection1, mockSection2);

            // When
            List<DataKey<?>> allKeys = StoreSection.getAllKeys(sections);

            // Then
            assertThat(allKeys).isEmpty();
        }

        @Test
        @DisplayName("Should handle mixed empty and non-empty sections")
        void testGetAllKeys_WithMixedSections_ReturnsOnlyNonEmptyKeys() {
            // Given
            when(mockSection1.getKeys()).thenReturn(Arrays.asList(stringKey));
            when(mockSection2.getKeys()).thenReturn(Collections.emptyList());

            StoreSection section3 = mock(StoreSection.class);
            when(section3.getKeys()).thenReturn(Arrays.asList(intKey, boolKey));

            List<StoreSection> sections = Arrays.asList(mockSection1, mockSection2, section3);

            // When
            List<DataKey<?>> allKeys = StoreSection.getAllKeys(sections);

            // Then
            assertThat(allKeys).containsExactly(stringKey, intKey, boolKey);
        }

        @Test
        @DisplayName("Should handle null sections list")
        void testGetAllKeys_WithNullSections_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> StoreSection.getAllKeys(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should preserve key order from sections")
        void testGetAllKeys_WithOrderedKeys_PreservesOrder() {
            // Given
            when(mockSection1.getKeys()).thenReturn(Arrays.asList(boolKey, stringKey));
            when(mockSection2.getKeys()).thenReturn(Arrays.asList(intKey));

            List<StoreSection> sections = Arrays.asList(mockSection1, mockSection2);

            // When
            List<DataKey<?>> allKeys = StoreSection.getAllKeys(sections);

            // Then
            assertThat(allKeys).containsExactly(boolKey, stringKey, intKey);
        }
    }

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should implement interface correctly")
        void testInterface_Implementation_ProvidesRequiredMethods() {
            // Given
            StoreSection section = StoreSection.newInstance("Test", Arrays.asList(stringKey));

            // When/Then - Verify interface methods are available and work
            assertThat(section.getName()).isInstanceOf(Optional.class);
            assertThat(section.getKeys()).isInstanceOf(List.class);
        }

        @Test
        @DisplayName("Should support method chaining in usage")
        void testInterface_MethodChaining_WorksCorrectly() {
            // Given
            StoreSection section = StoreSection.newInstance("Test", Arrays.asList(stringKey, intKey));

            // When/Then - Verify fluent usage patterns work
            assertThat(section.getName().orElse("default")).isEqualTo("Test");
            assertThat(section.getKeys().size()).isEqualTo(2);
            assertThat(section.getKeys().get(0)).isSameAs(stringKey);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty string section name")
        void testNewInstance_WithEmptyStringName_CreatesNamedSection() {
            // Given
            String emptyName = "";
            List<DataKey<?>> keys = Arrays.asList(stringKey);

            // When
            StoreSection section = StoreSection.newInstance(emptyName, keys);

            // Then
            assertThat(section.getName()).hasValue(emptyName);
            assertThat(section.getKeys()).containsExactly(stringKey);
        }

        @Test
        @DisplayName("Should handle large number of keys")
        void testNewInstance_WithManyKeys_HandlesEfficiently() {
            // Given
            List<DataKey<?>> manyKeys = new java.util.ArrayList<>();
            for (int i = 0; i < 100; i++) {
                manyKeys.add(KeyFactory.string("key" + i));
            }

            // When
            StoreSection section = StoreSection.newInstance("ManyKeys", manyKeys);

            // Then
            assertThat(section.getKeys()).hasSize(100);
            assertThat(section.getKeys()).containsExactlyElementsOf(manyKeys);
        }

        @Test
        @DisplayName("Should handle duplicate keys in list")
        void testNewInstance_WithDuplicateKeys_PreservesAllKeys() {
            // Given
            List<DataKey<?>> keysWithDuplicate = Arrays.asList(stringKey, intKey, stringKey);

            // When
            StoreSection section = StoreSection.newInstance("WithDuplicates", keysWithDuplicate);

            // Then
            assertThat(section.getKeys()).hasSize(3);
            assertThat(section.getKeys()).containsExactly(stringKey, intKey, stringKey);
        }

        @Test
        @DisplayName("Should handle special characters in section name")
        void testNewInstance_WithSpecialCharacters_CreatesSection() {
            // Given
            String specialName = "Section/With:Special*Characters@#$%";
            List<DataKey<?>> keys = Arrays.asList(stringKey);

            // When
            StoreSection section = StoreSection.newInstance(specialName, keys);

            // Then
            assertThat(section.getName()).hasValue(specialName);
            assertThat(section.getKeys()).containsExactly(stringKey);
        }
    }

    @Nested
    @DisplayName("Immutability Tests")
    class ImmutabilityTests {

        @Test
        @DisplayName("Returned keys list is defensive copy (modifying it does not affect section)")
        void testGetKeys_Modification_DoesNotAffectSection() {
            // Given
            List<DataKey<?>> originalKeys = new java.util.ArrayList<>(Arrays.asList(stringKey, intKey));
            StoreSection section = StoreSection.newInstance("Test", originalKeys);

            // When
            List<DataKey<?>> retrievedKeys = section.getKeys();
            retrievedKeys.add(boolKey);

            // Then - Section is not affected
            assertThat(retrievedKeys).hasSize(3);
            assertThat(section.getKeys()).containsExactly(stringKey, intKey);
        }

        @Test
        @DisplayName("Not affected by modifications to original key list")
        void testNewInstance_WithModifiableKeyList_NotAffectedByLaterChanges() {
            // Given
            List<DataKey<?>> modifiableKeys = new java.util.ArrayList<>(Arrays.asList(stringKey));
            StoreSection section = StoreSection.newInstance("Test", modifiableKeys);

            // When - Modify the original list
            modifiableKeys.add(intKey);

            // Then - Section is NOT affected because a defensive copy is made
            assertThat(section.getKeys()).containsExactly(stringKey);
        }
    }
}
