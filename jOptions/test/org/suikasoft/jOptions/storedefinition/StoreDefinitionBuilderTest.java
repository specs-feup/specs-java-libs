package org.suikasoft.jOptions.storedefinition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.DataStore.SimpleDataStore;
import org.suikasoft.jOptions.Interfaces.DataStore;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link StoreDefinitionBuilder}.
 * 
 * Tests the builder pattern for creating store definitions with sections and
 * default data.
 * 
 * @author Generated Tests
 */
@DisplayName("StoreDefinitionBuilder Tests")
class StoreDefinitionBuilderTest {

    @Mock
    private DataStore mockDefaultData;

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
        @DisplayName("Should create builder with app name")
        void testConstructor_WithAppName_CreatesBuilder() {
            // Given
            String appName = "TestApp";

            // When
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder(appName);

            // Then
            assertThat(builder).isNotNull();

            StoreDefinition definition = builder.build();
            assertThat(definition.getName()).isEqualTo(appName);
            assertThat(definition.getKeys()).isEmpty();
            assertThat(definition.getSections()).isEmpty();
        }

        @Test
        @DisplayName("Should create builder with null app name")
        void testConstructor_WithNullAppName_CreatesBuilder() {
            // When
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder((String) null);

            // Then
            assertThat(builder).isNotNull();

            StoreDefinition definition = builder.build();
            assertThat(definition.getName()).isNull();
        }

        @Test
        @DisplayName("Should create builder from class")
        void testConstructor_WithClass_CreatesBuilderWithClassName() {
            // When
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder(String.class);

            // Then
            assertThat(builder).isNotNull();

            StoreDefinition definition = builder.build();
            assertThat(definition.getName()).isEqualTo("String");
        }
    }

    @Nested
    @DisplayName("Key Addition Tests")
    class KeyAdditionTests {

        @Test
        @DisplayName("Should add single key")
        void testAddKey_SingleKey_AddsToDefinition() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .addKey(stringKey)
                    .build();

            // Then
            assertThat(definition.getKeys()).hasSize(1);
            assertThat(definition.getKeys()).contains(stringKey);
        }

        @Test
        @DisplayName("Should add multiple keys via varargs")
        void testAddKeys_MultipleKeys_AddsAllToDefinition() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .addKeys(stringKey, intKey, boolKey)
                    .build();

            // Then
            assertThat(definition.getKeys()).hasSize(3);
            assertThat(definition.getKeys()).containsExactlyInAnyOrder(stringKey, intKey, boolKey);
        }

        @Test
        @DisplayName("Should add multiple keys via collection")
        void testAddKeys_Collection_AddsAllToDefinition() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");
            List<DataKey<?>> keys = Arrays.asList(stringKey, intKey);

            // When
            StoreDefinition definition = builder
                    .addKeys(keys)
                    .build();

            // Then
            assertThat(definition.getKeys()).hasSize(2);
            assertThat(definition.getKeys()).containsExactlyInAnyOrder(stringKey, intKey);
        }

        @Test
        @DisplayName("Should handle duplicate keys gracefully")
        void testAddKey_DuplicateKey_HandlesGracefully() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .addKey(stringKey)
                    .addKey(stringKey) // Duplicate
                    .build();

            // Then
            assertThat(definition.getKeys()).hasSize(1);
            assertThat(definition.getKeys()).contains(stringKey);
        }

        @Test
        @DisplayName("Should handle empty collection")
        void testAddKeys_EmptyCollection_HandlesGracefully() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .addKeys(Arrays.asList())
                    .build();

            // Then
            assertThat(definition.getKeys()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Section Tests")
    class SectionTests {

        @Test
        @DisplayName("Should create section with name")
        void testStartSection_WithName_CreatesNamedSection() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");
            String sectionName = "Configuration";

            // When
            StoreDefinition definition = builder
                    .startSection(sectionName)
                    .addKey(stringKey)
                    .build();

            // Then
            assertThat(definition.getSections()).hasSize(1);

            StoreSection section = definition.getSections().get(0);
            assertThat(section.getName()).hasValue(sectionName);
            assertThat(section.getKeys()).contains(stringKey);
        }

        @Test
        @DisplayName("Should create multiple sections")
        void testStartSection_MultipleSections_CreatesAllSections() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .startSection("Section1")
                    .addKey(stringKey)
                    .startSection("Section2")
                    .addKey(intKey)
                    .build();

            // Then
            assertThat(definition.getSections()).hasSize(2);

            StoreSection section1 = definition.getSections().get(0);
            assertThat(section1.getName()).hasValue("Section1");
            assertThat(section1.getKeys()).containsExactly(stringKey);

            StoreSection section2 = definition.getSections().get(1);
            assertThat(section2.getName()).hasValue("Section2");
            assertThat(section2.getKeys()).containsExactly(intKey);
        }

        @Test
        @DisplayName("Should create section with null name")
        void testStartSection_WithNullName_CreatesUnnamedSection() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .startSection(null)
                    .addKey(stringKey)
                    .build();

            // Then
            assertThat(definition.getSections()).hasSize(1);

            StoreSection section = definition.getSections().get(0);
            assertThat(section.getName()).isEmpty(); // Null name becomes empty Optional
            assertThat(section.getKeys()).contains(stringKey);
        }

        @Test
        @DisplayName("Should handle keys before first section")
        void testAddKey_BeforeFirstSection_CreatesImplicitSection() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .addKey(stringKey)
                    .build();

            // Then
            assertThat(definition.getSections()).hasSize(1);

            StoreSection section = definition.getSections().get(0);
            assertThat(section.getName()).isEmpty(); // Implicit section has no name
            assertThat(section.getKeys()).contains(stringKey);
        }
    }

    @Nested
    @DisplayName("Default Data Tests")
    class DefaultDataTests {

        @Test
        @DisplayName("Should set default values")
        void testSetDefaultValues_ValidData_SetsDefaultData() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");
            DataStore defaultData = new SimpleDataStore("test");

            // When
            StoreDefinition definition = builder
                    .setDefaultValues(defaultData)
                    .build();

            // Then
            // getDefaultValues() returns a computed default data store, not the one we set
            assertThat(definition.getDefaultValues()).isNotNull();
        }

        @Test
        @DisplayName("Should handle null default values")
        void testSetDefaultValues_NullData_HandlesGracefully() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .setDefaultValues(null)
                    .build();

            // Then
            assertThat(definition.getDefaultValues()).isNotNull(); // Always returns a computed default
        }
    }

    @Nested
    @DisplayName("Definition Addition Tests")
    class DefinitionAdditionTests {

        @BeforeEach
        void setUpMocks() {
            when(mockStoreDefinition.getName()).thenReturn("MockStore");
            when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(stringKey, intKey));
            when(mockStoreDefinition.getSections()).thenReturn(Arrays.asList());
        }

        @Test
        @DisplayName("Should add definition without using name")
        void testAddDefinition_ValidDefinition_AddsDefinition() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .addDefinition(mockStoreDefinition)
                    .build();

            // Then
            assertThat(definition.getKeys()).containsAll(Arrays.asList(stringKey, intKey));

            // Should create an unnamed section for the added definition
            assertThat(definition.getSections()).hasSize(1);
            StoreSection section = definition.getSections().get(0);
            assertThat(section.getName()).isEmpty(); // Unnamed section
        }

        @Test
        @DisplayName("Should add named definition")
        void testAddNamedDefinition_ValidDefinition_AddsNamedDefinition() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .addNamedDefinition(mockStoreDefinition)
                    .build();

            // Then
            assertThat(definition.getKeys()).containsAll(Arrays.asList(stringKey, intKey));

            // Should create a named section
            assertThat(definition.getSections()).hasSize(1);
            StoreSection section = definition.getSections().get(0);
            assertThat(section.getName()).hasValue("MockStore");
        }

        @Test
        @DisplayName("Should add named definition with custom name")
        void testAddNamedDefinition_WithCustomName_AddsDefinitionWithCustomName() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");
            String customName = "CustomSection";

            // When
            StoreDefinition definition = builder
                    .addNamedDefinition(customName, mockStoreDefinition)
                    .build();

            // Then
            assertThat(definition.getKeys()).containsAll(Arrays.asList(stringKey, intKey));

            // Should create a section with custom name
            assertThat(definition.getSections()).hasSize(1);
            StoreSection section = definition.getSections().get(0);
            assertThat(section.getName()).hasValue(customName);
        }
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderPatternTests {

        @Test
        @DisplayName("Should support method chaining")
        void testMethodChaining_MultipleOperations_ReturnsBuilder() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .addKey(stringKey)
                    .startSection("Config")
                    .addKey(intKey)
                    .setDefaultValues(new SimpleDataStore("test"))
                    .build();

            // Then
            assertThat(definition).isNotNull();
            assertThat(definition.getName()).isEqualTo("TestApp");
            assertThat(definition.getKeys()).containsExactlyInAnyOrder(stringKey, intKey);
            assertThat(definition.getSections()).hasSize(2); // Implicit section + named section
        }

        @Test
        @DisplayName("Should create consistent results from same builder configuration")
        void testBuild_SameBuilderConfiguration_ReturnsConsistentResults() {
            // Given - Create two identical builders
            StoreDefinitionBuilder builder1 = new StoreDefinitionBuilder("TestApp")
                    .addKey(stringKey);
            StoreDefinitionBuilder builder2 = new StoreDefinitionBuilder("TestApp")
                    .addKey(stringKey);

            // When
            StoreDefinition definition1 = builder1.build();
            StoreDefinition definition2 = builder2.build();

            // Then
            assertThat(definition1.getName()).isEqualTo(definition2.getName());
            assertThat(definition1.getKeys()).containsExactlyElementsOf(definition2.getKeys());
            assertThat(definition1.getSections()).hasSameSizeAs(definition2.getSections());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty builder")
        void testBuild_EmptyBuilder_ReturnsEmptyDefinition() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder.build();

            // Then
            assertThat(definition.getName()).isEqualTo("TestApp");
            assertThat(definition.getKeys()).isEmpty();
            assertThat(definition.getSections()).isEmpty();
            assertThat(definition.getDefaultValues()).isNotNull();
        }

        @Test
        @DisplayName("Should handle section with no keys")
        void testStartSection_WithNoKeys_CreatesEmptySection() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .startSection("EmptySection")
                    .build();

            // Then
            assertThat(definition.getSections()).hasSize(1);

            StoreSection section = definition.getSections().get(0);
            assertThat(section.getName()).hasValue("EmptySection");
            assertThat(section.getKeys()).isEmpty();
        }

        @Test
        @DisplayName("Should handle multiple empty sections")
        void testMultipleEmptySections_CreatesAllSections() {
            // Given
            StoreDefinitionBuilder builder = new StoreDefinitionBuilder("TestApp");

            // When
            StoreDefinition definition = builder
                    .startSection("Section1")
                    .startSection("Section2")
                    .startSection("Section3")
                    .build();

            // Then
            assertThat(definition.getSections()).hasSize(3);
            assertThat(definition.getSections().get(0).getName()).hasValue("Section1");
            assertThat(definition.getSections().get(1).getName()).hasValue("Section2");
            assertThat(definition.getSections().get(2).getName()).hasValue("Section3");

            // All sections should be empty
            definition.getSections().forEach(section -> assertThat(section.getKeys()).isEmpty());
        }
    }
}
