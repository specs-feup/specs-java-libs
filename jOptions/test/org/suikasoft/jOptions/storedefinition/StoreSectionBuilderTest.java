package org.suikasoft.jOptions.storedefinition;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for {@link StoreSectionBuilder} class.
 * 
 * Tests the builder pattern for creating StoreSection instances, including
 * named and unnamed sections, key addition, duplicate detection, and section
 * building.
 * 
 * @author Generated Tests
 */
@DisplayName("StoreSectionBuilder Tests")
class StoreSectionBuilderTest {

    private DataKey<String> stringKey;
    private DataKey<Integer> intKey;
    private DataKey<Boolean> boolKey;

    @BeforeEach
    void setUp() {
        stringKey = KeyFactory.string("test.string", "default");
        intKey = KeyFactory.integer("test.int", 42);
        boolKey = KeyFactory.bool("test.bool");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor creates unnamed section builder")
        void testDefaultConstructor() {
            StoreSectionBuilder builder = new StoreSectionBuilder();

            StoreSection section = builder.build();

            assertThat(section.getName()).isEmpty();
        }

        @Test
        @DisplayName("Named constructor creates named section builder")
        void testNamedConstructor() {
            String sectionName = "TestSection";
            StoreSectionBuilder builder = new StoreSectionBuilder(sectionName);

            StoreSection section = builder.build();

            assertThat(section.getName()).hasValue(sectionName);
        }

        @Test
        @DisplayName("Null name constructor creates unnamed section")
        void testNullNameConstructor() {
            StoreSectionBuilder builder = new StoreSectionBuilder(null);

            StoreSection section = builder.build();

            assertThat(section.getName()).isEmpty();
        }

        @Test
        @DisplayName("Empty name constructor creates named section with empty name")
        void testEmptyNameConstructor() {
            StoreSectionBuilder builder = new StoreSectionBuilder("");

            StoreSection section = builder.build();

            assertThat(section.getName()).hasValue("");
        }
    }

    @Nested
    @DisplayName("Key Addition Tests")
    class KeyAdditionTests {

        @Test
        @DisplayName("Add single key successfully")
        void testAddSingleKey() {
            StoreSectionBuilder builder = new StoreSectionBuilder("test");

            StoreSectionBuilder result = builder.add(stringKey);

            assertThat(result).isSameAs(builder); // Should return same instance for chaining

            StoreSection section = builder.build();
            assertThat(section.getKeys()).containsExactly(stringKey);
        }

        @Test
        @DisplayName("Add multiple keys successfully")
        void testAddMultipleKeys() {
            StoreSectionBuilder builder = new StoreSectionBuilder("test");

            builder.add(stringKey)
                    .add(intKey)
                    .add(boolKey);

            StoreSection section = builder.build();
            assertThat(section.getKeys()).containsExactly(stringKey, intKey, boolKey);
        }

        @Test
        @DisplayName("Add keys maintains order")
        void testAddKeysOrder() {
            StoreSectionBuilder builder = new StoreSectionBuilder("test");

            builder.add(boolKey)
                    .add(stringKey)
                    .add(intKey);

            StoreSection section = builder.build();
            assertThat(section.getKeys()).containsExactly(boolKey, stringKey, intKey);
        }

        @Test
        @DisplayName("Add null key throws exception")
        void testAddNullKeyThrowsException() {
            StoreSectionBuilder builder = new StoreSectionBuilder("test");

            assertThatThrownBy(() -> builder.add(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Duplicate Key Detection Tests")
    class DuplicateKeyDetectionTests {

        @Test
        @DisplayName("Adding duplicate key by same reference throws exception")
        void testAddDuplicateKeySameReference() {
            StoreSectionBuilder builder = new StoreSectionBuilder("test");
            builder.add(stringKey);

            assertThatThrownBy(() -> builder.add(stringKey))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Datakey clash for name")
                    .hasMessageContaining(stringKey.getName());
        }

        @Test
        @DisplayName("Adding keys with same name throws exception")
        void testAddKeysWithSameName() {
            StoreSectionBuilder builder = new StoreSectionBuilder("test");
            DataKey<String> duplicateNameKey = KeyFactory.string("test.string", "different default");

            builder.add(stringKey);

            assertThatThrownBy(() -> builder.add(duplicateNameKey))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Datakey clash for name")
                    .hasMessageContaining("test.string");
        }

        @Test
        @DisplayName("Keys with different names but same type can be added")
        void testAddKeysWithDifferentNames() {
            StoreSectionBuilder builder = new StoreSectionBuilder("test");
            DataKey<String> otherStringKey = KeyFactory.string("other.string", "other default");

            builder.add(stringKey)
                    .add(otherStringKey);

            StoreSection section = builder.build();
            assertThat(section.getKeys()).containsExactly(stringKey, otherStringKey);
        }
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderPatternTests {

        @Test
        @DisplayName("Builder supports method chaining")
        void testMethodChaining() {
            StoreSectionBuilder builder = new StoreSectionBuilder("test");

            // Test that all add operations return the builder for chaining
            StoreSectionBuilder result = builder
                    .add(stringKey)
                    .add(intKey)
                    .add(boolKey);

            assertThat(result).isSameAs(builder);

            StoreSection section = result.build();
            assertThat(section.getKeys()).hasSize(3);
        }

        @Test
        @DisplayName("Build can be called multiple times")
        void testBuildMultipleTimes() {
            StoreSectionBuilder builder = new StoreSectionBuilder("test");
            builder.add(stringKey).add(intKey);

            StoreSection section1 = builder.build();
            StoreSection section2 = builder.build();

            // Should create separate instances
            assertThat(section1).isNotSameAs(section2);

            assertThat(section1.getName()).isEqualTo(section2.getName());
            assertThat(section1.getKeys()).containsExactlyElementsOf(section2.getKeys());
        }

        @Test
        @DisplayName("Builder uses defensive copy; previously built sections are not mutated")
        void testBuilderReuseAfterBuild_DefensiveCopy() {
            StoreSectionBuilder builder = new StoreSectionBuilder("test");
            builder.add(stringKey);

            StoreSection section1 = builder.build();

            // Add more keys after build - previously built section should not change
            builder.add(intKey);
            StoreSection section2 = builder.build();

            assertThat(section1.getKeys()).containsExactly(stringKey); // not mutated
            assertThat(section2.getKeys()).containsExactly(stringKey, intKey);
        }
    }

    @Nested
    @DisplayName("Empty Section Tests")
    class EmptySectionTests {

        @Test
        @DisplayName("Build empty unnamed section")
        void testBuildEmptyUnnamedSection() {
            StoreSectionBuilder builder = new StoreSectionBuilder();

            StoreSection section = builder.build();

            assertThat(section.getName()).isEmpty();
            assertThat(section.getKeys()).isEmpty();
        }

        @Test
        @DisplayName("Build empty named section")
        void testBuildEmptyNamedSection() {
            StoreSectionBuilder builder = new StoreSectionBuilder("EmptySection");

            StoreSection section = builder.build();

            assertThat(section.getName()).hasValue("EmptySection");
            assertThat(section.getKeys()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Very long section name")
        void testVeryLongSectionName() {
            String longName = "a".repeat(1000);
            StoreSectionBuilder builder = new StoreSectionBuilder(longName);

            StoreSection section = builder.build();

            assertThat(section.getName()).hasValue(longName);
        }

        @Test
        @DisplayName("Section name with special characters")
        void testSectionNameWithSpecialCharacters() {
            String specialName = "test-section_with.special@chars#123!";
            StoreSectionBuilder builder = new StoreSectionBuilder(specialName);

            StoreSection section = builder.build();

            assertThat(section.getName()).hasValue(specialName);
        }

        @Test
        @DisplayName("Large number of keys")
        void testLargeNumberOfKeys() {
            StoreSectionBuilder builder = new StoreSectionBuilder("LargeSection");

            // Add many keys
            for (int i = 0; i < 100; i++) {
                DataKey<Integer> key = KeyFactory.integer("key" + i, i);
                builder.add(key);
            }

            StoreSection section = builder.build();

            assertThat(section.getKeys()).hasSize(100);
        }

        @Test
        @DisplayName("Keys with very long names")
        void testKeysWithVeryLongNames() {
            String longKeyName = "very.long.key.name." + "segment.".repeat(100);
            DataKey<String> longNameKey = KeyFactory.string(longKeyName, "default");

            StoreSectionBuilder builder = new StoreSectionBuilder("test");
            builder.add(longNameKey);

            StoreSection section = builder.build();

            assertThat(section.getKeys()).containsExactly(longNameKey);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Build section and verify it contains expected keys")
        void testSectionWithExpectedKeys() {
            // Build a section
            StoreSectionBuilder sectionBuilder = new StoreSectionBuilder("TestSection");
            sectionBuilder.add(stringKey).add(intKey);
            StoreSection section = sectionBuilder.build();

            // Verify the section contains the expected keys
            assertThat(section.getName()).hasValue("TestSection");
            assertThat(section.getKeys()).containsExactly(stringKey, intKey);
        }

        @Test
        @DisplayName("Multiple sections with same keys from different builders")
        void testMultipleSectionsWithSameKeys() {
            // Create two builders for different sections
            StoreSectionBuilder builder1 = new StoreSectionBuilder("Section1");
            StoreSectionBuilder builder2 = new StoreSectionBuilder("Section2");

            // Add same keys to both (should be allowed in different sections)
            builder1.add(stringKey).add(intKey);
            builder2.add(stringKey).add(intKey);

            StoreSection section1 = builder1.build();
            StoreSection section2 = builder2.build();

            assertThat(section1.getKeys()).containsExactly(stringKey, intKey);
            assertThat(section2.getKeys()).containsExactly(stringKey, intKey);
            assertThat(section1.getName()).hasValue("Section1");
            assertThat(section2.getName()).hasValue("Section2");
        }
    }
}
