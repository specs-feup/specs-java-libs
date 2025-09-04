package org.specs.generators.java.enums;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Comprehensive Phase 4 test class for {@link Annotation}.
 * Tests all annotation enum values, method behavior, string representations,
 * and integration with the code generation framework.
 * 
 * @author Generated Tests
 */
@DisplayName("Annotation Enum Tests - Phase 4")
public class AnnotationTest {

    @Nested
    @DisplayName("Enum Value Tests")
    class EnumValueTests {

        @Test
        @DisplayName("All annotation enum values should be present")
        void testAllAnnotationValues_ArePresent() {
            // When
            Annotation[] values = Annotation.values();

            // Then
            assertThat(values).hasSize(6);
            assertThat(values).containsExactlyInAnyOrder(
                    Annotation.OVERRIDE,
                    Annotation.DEPRECATED,
                    Annotation.SUPPRESSWARNINGS,
                    Annotation.SAFEVARARGS,
                    Annotation.FUNCTIONALINTERFACE,
                    Annotation.TARGET);
        }

        @Test
        @DisplayName("valueOf() should work for all valid annotation names")
        void testValueOf_WithValidNames_ReturnsCorrectAnnotation() {
            // When/Then
            assertThat(Annotation.valueOf("OVERRIDE")).isEqualTo(Annotation.OVERRIDE);
            assertThat(Annotation.valueOf("DEPRECATED")).isEqualTo(Annotation.DEPRECATED);
            assertThat(Annotation.valueOf("SUPPRESSWARNINGS")).isEqualTo(Annotation.SUPPRESSWARNINGS);
            assertThat(Annotation.valueOf("SAFEVARARGS")).isEqualTo(Annotation.SAFEVARARGS);
            assertThat(Annotation.valueOf("FUNCTIONALINTERFACE")).isEqualTo(Annotation.FUNCTIONALINTERFACE);
            assertThat(Annotation.valueOf("TARGET")).isEqualTo(Annotation.TARGET);
        }

        @Test
        @DisplayName("valueOf() should throw exception for invalid annotation name")
        void testValueOf_WithInvalidName_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> Annotation.valueOf("INVALID_ANNOTATION"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("valueOf() should throw exception for null")
        void testValueOf_WithNull_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> Annotation.valueOf(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("getTag() Method Tests")
    class GetTagMethodTests {

        @Test
        @DisplayName("getTag() should return annotation with @ prefix for all annotations")
        void testGetTag_ReturnsAnnotationWithAtPrefix() {
            // When/Then
            assertThat(Annotation.OVERRIDE.getTag()).isEqualTo("@Override");
            assertThat(Annotation.DEPRECATED.getTag()).isEqualTo("@Deprecated");
            assertThat(Annotation.SUPPRESSWARNINGS.getTag()).isEqualTo("@SuppressWarnings");
            assertThat(Annotation.SAFEVARARGS.getTag()).isEqualTo("@SafeVarargs");
            assertThat(Annotation.FUNCTIONALINTERFACE.getTag()).isEqualTo("@FunctionalInterface");
            assertThat(Annotation.TARGET.getTag()).isEqualTo("@Target");
        }

        @ParameterizedTest(name = "getTag() for {0} should start with @")
        @EnumSource(Annotation.class)
        @DisplayName("getTag() should start with @ for all annotations")
        void testGetTag_AllAnnotations_StartWithAt(Annotation annotation) {
            // When
            String tag = annotation.getTag();

            // Then
            assertThat(tag).startsWith("@");
            assertThat(tag).hasSizeGreaterThan(1);
        }

        @ParameterizedTest(name = "getTag() for {0} should not be null or empty")
        @EnumSource(Annotation.class)
        @DisplayName("getTag() should never return null or empty string")
        void testGetTag_AllAnnotations_NotNullOrEmpty(Annotation annotation) {
            // When
            String tag = annotation.getTag();

            // Then
            assertThat(tag).isNotNull();
            assertThat(tag).isNotEmpty();
            assertThat(tag).isNotBlank();
        }
    }

    @Nested
    @DisplayName("toString() Method Tests")
    class ToStringMethodTests {

        @Test
        @DisplayName("toString() should return same as getTag() for all annotations")
        void testToString_ReturnsSameAsGetTag() {
            // When/Then
            assertThat(Annotation.OVERRIDE.toString()).isEqualTo(Annotation.OVERRIDE.getTag());
            assertThat(Annotation.DEPRECATED.toString()).isEqualTo(Annotation.DEPRECATED.getTag());
            assertThat(Annotation.SUPPRESSWARNINGS.toString()).isEqualTo(Annotation.SUPPRESSWARNINGS.getTag());
            assertThat(Annotation.SAFEVARARGS.toString()).isEqualTo(Annotation.SAFEVARARGS.getTag());
            assertThat(Annotation.FUNCTIONALINTERFACE.toString()).isEqualTo(Annotation.FUNCTIONALINTERFACE.getTag());
            assertThat(Annotation.TARGET.toString()).isEqualTo(Annotation.TARGET.getTag());
        }

        @ParameterizedTest(name = "toString() for {0} should match getTag()")
        @EnumSource(Annotation.class)
        @DisplayName("toString() should match getTag() for all annotations")
        void testToString_AllAnnotations_MatchGetTag(Annotation annotation) {
            // When
            String toString = annotation.toString();
            String getTag = annotation.getTag();

            // Then
            assertThat(toString).isEqualTo(getTag);
        }
    }

    @Nested
    @DisplayName("Individual Annotation Tests")
    class IndividualAnnotationTests {

        @Test
        @DisplayName("OVERRIDE annotation should have correct properties")
        void testOverride_HasCorrectProperties() {
            // When/Then
            assertThat(Annotation.OVERRIDE.name()).isEqualTo("OVERRIDE");
            assertThat(Annotation.OVERRIDE.getTag()).isEqualTo("@Override");
            assertThat(Annotation.OVERRIDE.toString()).isEqualTo("@Override");
            assertThat(Annotation.OVERRIDE.ordinal()).isEqualTo(0);
        }

        @Test
        @DisplayName("DEPRECATED annotation should have correct properties")
        void testDeprecated_HasCorrectProperties() {
            // When/Then
            assertThat(Annotation.DEPRECATED.name()).isEqualTo("DEPRECATED");
            assertThat(Annotation.DEPRECATED.getTag()).isEqualTo("@Deprecated");
            assertThat(Annotation.DEPRECATED.toString()).isEqualTo("@Deprecated");
            assertThat(Annotation.DEPRECATED.ordinal()).isEqualTo(1);
        }

        @Test
        @DisplayName("SUPPRESSWARNINGS annotation should have correct properties")
        void testSuppressWarnings_HasCorrectProperties() {
            // When/Then
            assertThat(Annotation.SUPPRESSWARNINGS.name()).isEqualTo("SUPPRESSWARNINGS");
            assertThat(Annotation.SUPPRESSWARNINGS.getTag()).isEqualTo("@SuppressWarnings");
            assertThat(Annotation.SUPPRESSWARNINGS.toString()).isEqualTo("@SuppressWarnings");
            assertThat(Annotation.SUPPRESSWARNINGS.ordinal()).isEqualTo(2);
        }

        @Test
        @DisplayName("SAFEVARARGS annotation should have correct properties")
        void testSafeVarargs_HasCorrectProperties() {
            // When/Then
            assertThat(Annotation.SAFEVARARGS.name()).isEqualTo("SAFEVARARGS");
            assertThat(Annotation.SAFEVARARGS.getTag()).isEqualTo("@SafeVarargs");
            assertThat(Annotation.SAFEVARARGS.toString()).isEqualTo("@SafeVarargs");
            assertThat(Annotation.SAFEVARARGS.ordinal()).isEqualTo(3);
        }

        @Test
        @DisplayName("FUNCTIONALINTERFACE annotation should have correct properties")
        void testFunctionalInterface_HasCorrectProperties() {
            // When/Then
            assertThat(Annotation.FUNCTIONALINTERFACE.name()).isEqualTo("FUNCTIONALINTERFACE");
            assertThat(Annotation.FUNCTIONALINTERFACE.getTag()).isEqualTo("@FunctionalInterface");
            assertThat(Annotation.FUNCTIONALINTERFACE.toString()).isEqualTo("@FunctionalInterface");
            assertThat(Annotation.FUNCTIONALINTERFACE.ordinal()).isEqualTo(4);
        }

        @Test
        @DisplayName("TARGET annotation should have correct properties")
        void testTarget_HasCorrectProperties() {
            // When/Then
            assertThat(Annotation.TARGET.name()).isEqualTo("TARGET");
            assertThat(Annotation.TARGET.getTag()).isEqualTo("@Target");
            assertThat(Annotation.TARGET.toString()).isEqualTo("@Target");
            assertThat(Annotation.TARGET.ordinal()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("Enum Behavior Tests")
    class EnumBehaviorTests {

        @Test
        @DisplayName("Enum values should maintain order")
        void testEnumValues_MaintainOrder() {
            // When
            Annotation[] values = Annotation.values();

            // Then
            assertThat(values).containsExactly(
                    Annotation.OVERRIDE,
                    Annotation.DEPRECATED,
                    Annotation.SUPPRESSWARNINGS,
                    Annotation.SAFEVARARGS,
                    Annotation.FUNCTIONALINTERFACE,
                    Annotation.TARGET);
        }

        @Test
        @DisplayName("Enum should be serializable")
        void testEnum_IsSerializable() {
            // Then
            assertThat(Annotation.OVERRIDE).isInstanceOf(java.io.Serializable.class);
            assertThat(Enum.class).isAssignableFrom(Annotation.class);
        }

        @Test
        @DisplayName("Enum values should be comparable")
        void testEnum_ValuesAreComparable() {
            // When/Then
            assertThat(Annotation.OVERRIDE.compareTo(Annotation.DEPRECATED)).isNegative();
            assertThat(Annotation.DEPRECATED.compareTo(Annotation.OVERRIDE)).isPositive();
            assertThat(Annotation.OVERRIDE.compareTo(Annotation.OVERRIDE)).isZero();
        }

        @Test
        @DisplayName("Enum should support equality")
        void testEnum_SupportsEquality() {
            // When/Then
            assertThat(Annotation.OVERRIDE).isEqualTo(Annotation.OVERRIDE);
            assertThat(Annotation.OVERRIDE).isNotEqualTo(Annotation.DEPRECATED);
            assertThat(Annotation.OVERRIDE.equals(Annotation.OVERRIDE)).isTrue();
            assertThat(Annotation.OVERRIDE.equals(Annotation.DEPRECATED)).isFalse();
            assertThat(Annotation.OVERRIDE.equals(null)).isFalse();
            assertThat(Annotation.OVERRIDE.equals("@Override")).isFalse();
        }

        @Test
        @DisplayName("Enum should have consistent hashCode")
        void testEnum_HasConsistentHashCode() {
            // When
            int hashCode1 = Annotation.OVERRIDE.hashCode();
            int hashCode2 = Annotation.OVERRIDE.hashCode();

            // Then
            assertThat(hashCode1).isEqualTo(hashCode2);
            assertThat(Annotation.OVERRIDE.hashCode()).isNotEqualTo(Annotation.DEPRECATED.hashCode());
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Annotations should be usable in switch statements")
        void testAnnotations_UsableInSwitchStatements() {
            // Given
            Annotation annotation = Annotation.OVERRIDE;

            // When
            String result = switch (annotation) {
                case OVERRIDE -> "override";
                case DEPRECATED -> "deprecated";
                case SUPPRESSWARNINGS -> "suppresswarnings";
                case SAFEVARARGS -> "safevarargs";
                case FUNCTIONALINTERFACE -> "functionalinterface";
                case TARGET -> "target";
            };

            // Then
            assertThat(result).isEqualTo("override");
        }

        @Test
        @DisplayName("Annotations should be usable in collections")
        void testAnnotations_UsableInCollections() {
            // Given
            var annotations = java.util.Set.of(
                    Annotation.OVERRIDE,
                    Annotation.DEPRECATED,
                    Annotation.SUPPRESSWARNINGS);

            // When/Then
            assertThat(annotations).hasSize(3);
            assertThat(annotations).contains(Annotation.OVERRIDE);
            assertThat(annotations).doesNotContain(Annotation.TARGET);
        }

        @Test
        @DisplayName("Annotations should work with streams")
        void testAnnotations_WorkWithStreams() {
            // When
            var annotationTags = java.util.Arrays.stream(Annotation.values())
                    .map(Annotation::getTag)
                    .toList();

            // Then
            assertThat(annotationTags).hasSize(6);
            assertThat(annotationTags).allMatch(tag -> tag.startsWith("@"));
        }
    }
}
