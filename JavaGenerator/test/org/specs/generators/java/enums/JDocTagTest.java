package org.specs.generators.java.enums;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Comprehensive Phase 4 test class for {@link JDocTag}.
 * Tests all JavaDoc tag enum values, method behavior, string representations,
 * and integration with JavaDoc generation framework.
 * 
 * @author Generated Tests
 */
@DisplayName("JDocTag Enum Tests - Phase 4")
public class JDocTagTest {

    @Nested
    @DisplayName("Enum Value Tests")
    class EnumValueTests {

        @Test
        @DisplayName("All JavaDoc tag enum values should be present")
        void testAllJDocTagValues_ArePresent() {
            // When
            JDocTag[] values = JDocTag.values();

            // Then
            assertThat(values).hasSize(7);
            assertThat(values).containsExactlyInAnyOrder(
                    JDocTag.AUTHOR,
                    JDocTag.CATEGORY,
                    JDocTag.DEPRECATED,
                    JDocTag.SEE,
                    JDocTag.VERSION,
                    JDocTag.PARAM,
                    JDocTag.RETURN);
        }

        @Test
        @DisplayName("valueOf() should work for all valid JavaDoc tag names")
        void testValueOf_WithValidNames_ReturnsCorrectTag() {
            // When/Then
            assertThat(JDocTag.valueOf("AUTHOR")).isEqualTo(JDocTag.AUTHOR);
            assertThat(JDocTag.valueOf("CATEGORY")).isEqualTo(JDocTag.CATEGORY);
            assertThat(JDocTag.valueOf("DEPRECATED")).isEqualTo(JDocTag.DEPRECATED);
            assertThat(JDocTag.valueOf("SEE")).isEqualTo(JDocTag.SEE);
            assertThat(JDocTag.valueOf("VERSION")).isEqualTo(JDocTag.VERSION);
            assertThat(JDocTag.valueOf("PARAM")).isEqualTo(JDocTag.PARAM);
            assertThat(JDocTag.valueOf("RETURN")).isEqualTo(JDocTag.RETURN);
        }

        @Test
        @DisplayName("valueOf() should throw exception for invalid tag name")
        void testValueOf_WithInvalidName_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> JDocTag.valueOf("INVALID_TAG"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("valueOf() should throw exception for null")
        void testValueOf_WithNull_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> JDocTag.valueOf(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("getTag() Method Tests")
    class GetTagMethodTests {

        @Test
        @DisplayName("getTag() should return correct JavaDoc tags for all values")
        void testGetTag_ReturnsCorrectJavaDocTags() {
            // When/Then
            assertThat(JDocTag.AUTHOR.getTag()).isEqualTo("@author");
            assertThat(JDocTag.CATEGORY.getTag()).isEqualTo("@category");
            assertThat(JDocTag.DEPRECATED.getTag()).isEqualTo("@deprecated");
            assertThat(JDocTag.SEE.getTag()).isEqualTo("@see");
            assertThat(JDocTag.VERSION.getTag()).isEqualTo("@version");
            assertThat(JDocTag.PARAM.getTag()).isEqualTo("@param");
            assertThat(JDocTag.RETURN.getTag()).isEqualTo("@return");
        }

        @ParameterizedTest(name = "getTag() for {0} should start with @")
        @EnumSource(JDocTag.class)
        @DisplayName("getTag() should start with @ for all JavaDoc tags")
        void testGetTag_AllTags_StartWithAt(JDocTag jDocTag) {
            // When
            String tag = jDocTag.getTag();

            // Then
            assertThat(tag).startsWith("@");
            assertThat(tag).hasSizeGreaterThan(1);
        }

        @ParameterizedTest(name = "getTag() for {0} should not be null or empty")
        @EnumSource(JDocTag.class)
        @DisplayName("getTag() should never return null or empty string")
        void testGetTag_AllTags_NotNullOrEmpty(JDocTag jDocTag) {
            // When
            String tag = jDocTag.getTag();

            // Then
            assertThat(tag).isNotNull();
            assertThat(tag).isNotEmpty();
            assertThat(tag).isNotBlank();
        }

        @ParameterizedTest(name = "getTag() for {0} should be lowercase after @")
        @EnumSource(JDocTag.class)
        @DisplayName("getTag() should be lowercase after @ prefix")
        void testGetTag_AllTags_LowercaseAfterAt(JDocTag jDocTag) {
            // When
            String tag = jDocTag.getTag();
            String afterAt = tag.substring(1);

            // Then
            assertThat(afterAt).isEqualTo(afterAt.toLowerCase());
        }
    }

    @Nested
    @DisplayName("Individual JavaDoc Tag Tests")
    class IndividualTagTests {

        @Test
        @DisplayName("AUTHOR tag should have correct properties")
        void testAuthor_HasCorrectProperties() {
            // When/Then
            assertThat(JDocTag.AUTHOR.name()).isEqualTo("AUTHOR");
            assertThat(JDocTag.AUTHOR.getTag()).isEqualTo("@author");
            assertThat(JDocTag.AUTHOR.ordinal()).isEqualTo(0);
        }

        @Test
        @DisplayName("CATEGORY tag should have correct properties")
        void testCategory_HasCorrectProperties() {
            // When/Then
            assertThat(JDocTag.CATEGORY.name()).isEqualTo("CATEGORY");
            assertThat(JDocTag.CATEGORY.getTag()).isEqualTo("@category");
            assertThat(JDocTag.CATEGORY.ordinal()).isEqualTo(1);
        }

        @Test
        @DisplayName("DEPRECATED tag should have correct properties")
        void testDeprecated_HasCorrectProperties() {
            // When/Then
            assertThat(JDocTag.DEPRECATED.name()).isEqualTo("DEPRECATED");
            assertThat(JDocTag.DEPRECATED.getTag()).isEqualTo("@deprecated");
            assertThat(JDocTag.DEPRECATED.ordinal()).isEqualTo(2);
        }

        @Test
        @DisplayName("SEE tag should have correct properties")
        void testSee_HasCorrectProperties() {
            // When/Then
            assertThat(JDocTag.SEE.name()).isEqualTo("SEE");
            assertThat(JDocTag.SEE.getTag()).isEqualTo("@see");
            assertThat(JDocTag.SEE.ordinal()).isEqualTo(3);
        }

        @Test
        @DisplayName("VERSION tag should have correct properties")
        void testVersion_HasCorrectProperties() {
            // When/Then
            assertThat(JDocTag.VERSION.name()).isEqualTo("VERSION");
            assertThat(JDocTag.VERSION.getTag()).isEqualTo("@version");
            assertThat(JDocTag.VERSION.ordinal()).isEqualTo(4);
        }

        @Test
        @DisplayName("PARAM tag should have correct properties")
        void testParam_HasCorrectProperties() {
            // When/Then
            assertThat(JDocTag.PARAM.name()).isEqualTo("PARAM");
            assertThat(JDocTag.PARAM.getTag()).isEqualTo("@param");
            assertThat(JDocTag.PARAM.ordinal()).isEqualTo(5);
        }

        @Test
        @DisplayName("RETURN tag should have correct properties")
        void testReturn_HasCorrectProperties() {
            // When/Then
            assertThat(JDocTag.RETURN.name()).isEqualTo("RETURN");
            assertThat(JDocTag.RETURN.getTag()).isEqualTo("@return");
            assertThat(JDocTag.RETURN.ordinal()).isEqualTo(6);
        }
    }

    @Nested
    @DisplayName("Enum Behavior Tests")
    class EnumBehaviorTests {

        @Test
        @DisplayName("Enum values should maintain order")
        void testEnumValues_MaintainOrder() {
            // When
            JDocTag[] values = JDocTag.values();

            // Then
            assertThat(values).containsExactly(
                    JDocTag.AUTHOR,
                    JDocTag.CATEGORY,
                    JDocTag.DEPRECATED,
                    JDocTag.SEE,
                    JDocTag.VERSION,
                    JDocTag.PARAM,
                    JDocTag.RETURN);
        }

        @Test
        @DisplayName("Enum should be serializable")
        void testEnum_IsSerializable() {
            // Then
            assertThat(JDocTag.AUTHOR).isInstanceOf(java.io.Serializable.class);
            assertThat(Enum.class).isAssignableFrom(JDocTag.class);
        }

        @Test
        @DisplayName("Enum values should be comparable")
        void testEnum_ValuesAreComparable() {
            // When/Then
            assertThat(JDocTag.AUTHOR.compareTo(JDocTag.CATEGORY)).isNegative();
            assertThat(JDocTag.CATEGORY.compareTo(JDocTag.AUTHOR)).isPositive();
            assertThat(JDocTag.AUTHOR.compareTo(JDocTag.AUTHOR)).isZero();
        }

        @Test
        @DisplayName("Enum should support equality")
        void testEnum_SupportsEquality() {
            // When/Then
            assertThat(JDocTag.AUTHOR).isEqualTo(JDocTag.AUTHOR);
            assertThat(JDocTag.AUTHOR).isNotEqualTo(JDocTag.CATEGORY);
            assertThat(JDocTag.AUTHOR.equals(JDocTag.AUTHOR)).isTrue();
            assertThat(JDocTag.AUTHOR.equals(JDocTag.CATEGORY)).isFalse();
            assertThat(JDocTag.AUTHOR.equals(null)).isFalse();
            assertThat(JDocTag.AUTHOR.equals("@author")).isFalse();
        }

        @Test
        @DisplayName("Enum should have consistent hashCode")
        void testEnum_HasConsistentHashCode() {
            // When
            int hashCode1 = JDocTag.AUTHOR.hashCode();
            int hashCode2 = JDocTag.AUTHOR.hashCode();

            // Then
            assertThat(hashCode1).isEqualTo(hashCode2);
            assertThat(JDocTag.AUTHOR.hashCode()).isNotEqualTo(JDocTag.CATEGORY.hashCode());
        }
    }

    @Nested
    @DisplayName("JavaDoc Specific Tests")
    class JavaDocSpecificTests {

        @Test
        @DisplayName("Common JavaDoc tags should be represented")
        void testCommonJavaDocTags_AreRepresented() {
            // When/Then - Common JavaDoc tags that should be present
            assertThat(JDocTag.values())
                    .extracting(JDocTag::getTag)
                    .contains("@author", "@param", "@return", "@see", "@deprecated");
        }

        @Test
        @DisplayName("Tags should be suitable for JavaDoc generation")
        void testTags_SuitableForJavaDocGeneration() {
            // When/Then
            for (JDocTag tag : JDocTag.values()) {
                String tagString = tag.getTag();

                // Should start with @
                assertThat(tagString).startsWith("@");

                // Should not contain spaces or special characters (except @)
                assertThat(tagString.substring(1)).matches("[a-z]+");

                // Should be valid JavaDoc tag format
                assertThat(tagString).hasSize(tagString.trim().length()); // No leading/trailing whitespace
            }
        }

        @Test
        @DisplayName("Method documentation tags should be present")
        void testMethodDocumentationTags_ArePresent() {
            // When/Then - Method-specific tags
            assertThat(JDocTag.PARAM.getTag()).isEqualTo("@param");
            assertThat(JDocTag.RETURN.getTag()).isEqualTo("@return");
        }

        @Test
        @DisplayName("Class documentation tags should be present")
        void testClassDocumentationTags_ArePresent() {
            // When/Then - Class-specific tags
            assertThat(JDocTag.AUTHOR.getTag()).isEqualTo("@author");
            assertThat(JDocTag.VERSION.getTag()).isEqualTo("@version");
            assertThat(JDocTag.SEE.getTag()).isEqualTo("@see");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("JavaDoc tags should be usable in switch statements")
        void testJavaDocTags_UsableInSwitchStatements() {
            // Given
            JDocTag tag = JDocTag.PARAM;

            // When
            String result = switch (tag) {
                case AUTHOR -> "author";
                case CATEGORY -> "category";
                case DEPRECATED -> "deprecated";
                case SEE -> "see";
                case VERSION -> "version";
                case PARAM -> "param";
                case RETURN -> "return";
            };

            // Then
            assertThat(result).isEqualTo("param");
        }

        @Test
        @DisplayName("JavaDoc tags should be usable in collections")
        void testJavaDocTags_UsableInCollections() {
            // Given
            var methodTags = java.util.Set.of(
                    JDocTag.PARAM,
                    JDocTag.RETURN);

            // When/Then
            assertThat(methodTags).hasSize(2);
            assertThat(methodTags).contains(JDocTag.PARAM);
            assertThat(methodTags).doesNotContain(JDocTag.AUTHOR);
        }

        @Test
        @DisplayName("JavaDoc tags should work with streams")
        void testJavaDocTags_WorkWithStreams() {
            // When
            var tagStrings = java.util.Arrays.stream(JDocTag.values())
                    .map(JDocTag::getTag)
                    .sorted()
                    .toList();

            // Then
            assertThat(tagStrings).hasSize(7);
            assertThat(tagStrings).allMatch(tag -> tag.startsWith("@"));
            assertThat(tagStrings).isSorted();
        }

        @Test
        @DisplayName("JavaDoc tags should group logically")
        void testJavaDocTags_GroupLogically() {
            // Given - Method-related tags
            var methodTags = java.util.List.of(JDocTag.PARAM, JDocTag.RETURN);

            // Given - Class-related tags
            var classTags = java.util.List.of(JDocTag.AUTHOR, JDocTag.VERSION, JDocTag.SEE);

            // Given - General tags
            var generalTags = java.util.List.of(JDocTag.DEPRECATED, JDocTag.CATEGORY);

            // When
            var allTags = java.util.Set.of(JDocTag.values());

            // Then
            assertThat(allTags).containsAll(methodTags);
            assertThat(allTags).containsAll(classTags);
            assertThat(allTags).containsAll(generalTags);

            // Verify all tags are accounted for
            int totalExpected = methodTags.size() + classTags.size() + generalTags.size();
            assertThat(allTags).hasSize(totalExpected);
        }
    }
}
