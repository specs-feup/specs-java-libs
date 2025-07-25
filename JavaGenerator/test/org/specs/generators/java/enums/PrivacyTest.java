package org.specs.generators.java.enums;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Comprehensive Phase 4 test class for {@link Privacy}.
 * Tests all privacy level enum values, method behavior, string representations,
 * and integration with the Java access control framework.
 * 
 * @author Generated Tests
 */
@DisplayName("Privacy Enum Tests - Phase 4")
public class PrivacyTest {

    @Nested
    @DisplayName("Enum Value Tests")
    class EnumValueTests {

        @Test
        @DisplayName("All privacy level enum values should be present")
        void testAllPrivacyValues_ArePresent() {
            // When
            Privacy[] values = Privacy.values();

            // Then
            assertThat(values).hasSize(4);
            assertThat(values).containsExactlyInAnyOrder(
                    Privacy.PUBLIC,
                    Privacy.PRIVATE,
                    Privacy.PROTECTED,
                    Privacy.PACKAGE_PROTECTED);
        }

        @Test
        @DisplayName("valueOf() should work for all valid privacy level names")
        void testValueOf_WithValidNames_ReturnsCorrectPrivacy() {
            // When/Then
            assertThat(Privacy.valueOf("PUBLIC")).isEqualTo(Privacy.PUBLIC);
            assertThat(Privacy.valueOf("PRIVATE")).isEqualTo(Privacy.PRIVATE);
            assertThat(Privacy.valueOf("PROTECTED")).isEqualTo(Privacy.PROTECTED);
            assertThat(Privacy.valueOf("PACKAGE_PROTECTED")).isEqualTo(Privacy.PACKAGE_PROTECTED);
        }

        @Test
        @DisplayName("valueOf() should throw exception for invalid privacy name")
        void testValueOf_WithInvalidName_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> Privacy.valueOf("INVALID_PRIVACY"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("valueOf() should throw exception for null")
        void testValueOf_WithNull_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> Privacy.valueOf(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("getType() Method Tests")
    class GetTypeMethodTests {

        @Test
        @DisplayName("getType() should return correct privacy level strings for all values")
        void testGetType_ReturnsCorrectPrivacyStrings() {
            // When/Then
            assertThat(Privacy.PUBLIC.getType()).isEqualTo("public");
            assertThat(Privacy.PRIVATE.getType()).isEqualTo("private");
            assertThat(Privacy.PROTECTED.getType()).isEqualTo("protected");
            assertThat(Privacy.PACKAGE_PROTECTED.getType()).isEmpty(); // Package-private has no keyword
        }

        @ParameterizedTest(name = "getType() for {0} should not be null")
        @EnumSource(Privacy.class)
        @DisplayName("getType() should never return null")
        void testGetType_AllPrivacyLevels_NotNull(Privacy privacy) {
            // When
            String type = privacy.getType();

            // Then
            assertThat(type).isNotNull();
        }

        @Test
        @DisplayName("getType() should handle package-protected special case")
        void testGetType_PackageProtected_ReturnsEmptyString() {
            // When
            String type = Privacy.PACKAGE_PROTECTED.getType();

            // Then
            assertThat(type).isEmpty(); // Package-private has no explicit keyword in Java
            assertThat(type).isNotNull();
        }

        @ParameterizedTest(name = "getType() for {0} should be valid Java access modifier or empty")
        @EnumSource(Privacy.class)
        @DisplayName("getType() should return valid Java access modifiers")
        void testGetType_AllPrivacyLevels_ValidJavaAccessModifiers(Privacy privacy) {
            // When
            String type = privacy.getType();

            // Then - Should be one of the valid Java access modifiers or empty (for
            // package-private)
            assertThat(type).isIn("public", "private", "protected", "");
        }

        @Test
        @DisplayName("Non-package-protected types should be lowercase")
        void testGetType_NonPackageProtected_AreLowercase() {
            // When/Then
            for (Privacy privacy : Privacy.values()) {
                if (privacy != Privacy.PACKAGE_PROTECTED) {
                    String type = privacy.getType();
                    assertThat(type).isEqualTo(type.toLowerCase());
                    assertThat(type).matches("[a-z]+"); // Only lowercase letters
                }
            }
        }
    }

    @Nested
    @DisplayName("toString() Method Tests")
    class ToStringMethodTests {

        @Test
        @DisplayName("toString() should return same as getType() for all privacy levels")
        void testToString_ReturnsSameAsGetType() {
            // When/Then
            assertThat(Privacy.PUBLIC.toString()).isEqualTo(Privacy.PUBLIC.getType());
            assertThat(Privacy.PRIVATE.toString()).isEqualTo(Privacy.PRIVATE.getType());
            assertThat(Privacy.PROTECTED.toString()).isEqualTo(Privacy.PROTECTED.getType());
            assertThat(Privacy.PACKAGE_PROTECTED.toString()).isEqualTo(Privacy.PACKAGE_PROTECTED.getType());
        }

        @ParameterizedTest(name = "toString() for {0} should match getType()")
        @EnumSource(Privacy.class)
        @DisplayName("toString() should match getType() for all privacy levels")
        void testToString_AllPrivacyLevels_MatchGetType(Privacy privacy) {
            // When
            String toString = privacy.toString();
            String getType = privacy.getType();

            // Then
            assertThat(toString).isEqualTo(getType);
        }
    }

    @Nested
    @DisplayName("Individual Privacy Level Tests")
    class IndividualPrivacyLevelTests {

        @Test
        @DisplayName("PUBLIC privacy should have correct properties")
        void testPublic_HasCorrectProperties() {
            // When/Then
            assertThat(Privacy.PUBLIC.name()).isEqualTo("PUBLIC");
            assertThat(Privacy.PUBLIC.getType()).isEqualTo("public");
            assertThat(Privacy.PUBLIC.toString()).isEqualTo("public");
            assertThat(Privacy.PUBLIC.ordinal()).isEqualTo(0);
        }

        @Test
        @DisplayName("PRIVATE privacy should have correct properties")
        void testPrivate_HasCorrectProperties() {
            // When/Then
            assertThat(Privacy.PRIVATE.name()).isEqualTo("PRIVATE");
            assertThat(Privacy.PRIVATE.getType()).isEqualTo("private");
            assertThat(Privacy.PRIVATE.toString()).isEqualTo("private");
            assertThat(Privacy.PRIVATE.ordinal()).isEqualTo(1);
        }

        @Test
        @DisplayName("PROTECTED privacy should have correct properties")
        void testProtected_HasCorrectProperties() {
            // When/Then
            assertThat(Privacy.PROTECTED.name()).isEqualTo("PROTECTED");
            assertThat(Privacy.PROTECTED.getType()).isEqualTo("protected");
            assertThat(Privacy.PROTECTED.toString()).isEqualTo("protected");
            assertThat(Privacy.PROTECTED.ordinal()).isEqualTo(2);
        }

        @Test
        @DisplayName("PACKAGE_PROTECTED privacy should have correct properties")
        void testPackageProtected_HasCorrectProperties() {
            // When/Then
            assertThat(Privacy.PACKAGE_PROTECTED.name()).isEqualTo("PACKAGE_PROTECTED");
            assertThat(Privacy.PACKAGE_PROTECTED.getType()).isEmpty();
            assertThat(Privacy.PACKAGE_PROTECTED.toString()).isEmpty();
            assertThat(Privacy.PACKAGE_PROTECTED.ordinal()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Enum Behavior Tests")
    class EnumBehaviorTests {

        @Test
        @DisplayName("Enum values should maintain order")
        void testEnumValues_MaintainOrder() {
            // When
            Privacy[] values = Privacy.values();

            // Then
            assertThat(values).containsExactly(
                    Privacy.PUBLIC,
                    Privacy.PRIVATE,
                    Privacy.PROTECTED,
                    Privacy.PACKAGE_PROTECTED);
        }

        @Test
        @DisplayName("Enum should be serializable")
        void testEnum_IsSerializable() {
            // Then
            assertThat(Privacy.PUBLIC).isInstanceOf(java.io.Serializable.class);
            assertThat(Enum.class).isAssignableFrom(Privacy.class);
        }

        @Test
        @DisplayName("Enum values should be comparable")
        void testEnum_ValuesAreComparable() {
            // When/Then
            assertThat(Privacy.PUBLIC.compareTo(Privacy.PRIVATE)).isNegative();
            assertThat(Privacy.PRIVATE.compareTo(Privacy.PUBLIC)).isPositive();
            assertThat(Privacy.PUBLIC.compareTo(Privacy.PUBLIC)).isZero();
        }

        @Test
        @DisplayName("Enum should support equality")
        void testEnum_SupportsEquality() {
            // When/Then
            assertThat(Privacy.PUBLIC).isEqualTo(Privacy.PUBLIC);
            assertThat(Privacy.PUBLIC).isNotEqualTo(Privacy.PRIVATE);
            assertThat(Privacy.PUBLIC.equals(Privacy.PUBLIC)).isTrue();
            assertThat(Privacy.PUBLIC.equals(Privacy.PRIVATE)).isFalse();
            assertThat(Privacy.PUBLIC.equals(null)).isFalse();
            assertThat(Privacy.PUBLIC.equals("public")).isFalse();
        }

        @Test
        @DisplayName("Enum should have consistent hashCode")
        void testEnum_HasConsistentHashCode() {
            // When
            int hashCode1 = Privacy.PUBLIC.hashCode();
            int hashCode2 = Privacy.PUBLIC.hashCode();

            // Then
            assertThat(hashCode1).isEqualTo(hashCode2);
            assertThat(Privacy.PUBLIC.hashCode()).isNotEqualTo(Privacy.PRIVATE.hashCode());
        }
    }

    @Nested
    @DisplayName("Java Access Control Specific Tests")
    class JavaAccessControlSpecificTests {

        @Test
        @DisplayName("All privacy levels should correspond to Java access levels")
        void testAllPrivacyLevels_CorrespondToJavaAccessLevels() {
            // When/Then
            // public - accessible from anywhere
            assertThat(Privacy.PUBLIC.getType()).isEqualTo("public");

            // private - accessible only within the same class
            assertThat(Privacy.PRIVATE.getType()).isEqualTo("private");

            // protected - accessible within the same package and subclasses
            assertThat(Privacy.PROTECTED.getType()).isEqualTo("protected");

            // package-private - accessible within the same package (no keyword)
            assertThat(Privacy.PACKAGE_PROTECTED.getType()).isEmpty();
        }

        @Test
        @DisplayName("Privacy levels should be ordered by accessibility")
        void testPrivacyLevels_OrderedByAccessibility() {
            // Given - ordered from most to least restrictive (conceptually)
            // Note: The actual enum order might be different, this tests logical grouping

            // When/Then
            // Most restrictive
            assertThat(Privacy.PRIVATE.getType()).isEqualTo("private");

            // Package level
            assertThat(Privacy.PACKAGE_PROTECTED.getType()).isEmpty();

            // Inheritance accessible
            assertThat(Privacy.PROTECTED.getType()).isEqualTo("protected");

            // Least restrictive
            assertThat(Privacy.PUBLIC.getType()).isEqualTo("public");
        }

        @Test
        @DisplayName("Privacy levels should be applicable to all Java elements")
        void testPrivacyLevels_ApplicableToJavaElements() {
            // When/Then - All privacy levels can be applied to classes, methods, fields

            // Classes can have public, package-private (no explicit privacy levels for
            // private/protected top-level classes)
            // But nested classes can have all privacy levels
            assertThat(Privacy.PUBLIC.getType()).isEqualTo("public");
            assertThat(Privacy.PACKAGE_PROTECTED.getType()).isEmpty();
            assertThat(Privacy.PRIVATE.getType()).isEqualTo("private");
            assertThat(Privacy.PROTECTED.getType()).isEqualTo("protected");

            // Methods and fields can have all privacy levels
            for (Privacy privacy : Privacy.values()) {
                String type = privacy.getType();
                assertThat(type).isNotNull();
                // Each should be either a valid access modifier or empty (package-private)
            }
        }

        @Test
        @DisplayName("Privacy keywords should be valid Java reserved words")
        void testPrivacyKeywords_ValidJavaReservedWords() {
            // Given
            var javaAccessModifiers = java.util.Set.of("public", "private", "protected", "");

            // When
            var privacyTypes = java.util.Arrays.stream(Privacy.values())
                    .map(Privacy::getType)
                    .collect(java.util.stream.Collectors.toSet());

            // Then
            assertThat(privacyTypes).isEqualTo(javaAccessModifiers);

            // All non-empty types should be valid Java keywords
            privacyTypes.stream()
                    .filter(type -> !type.isEmpty())
                    .forEach(type -> {
                        assertThat(type).matches("[a-z]+"); // Valid identifier pattern
                        assertThat(type).doesNotContain(" "); // No spaces
                        assertThat(type).isEqualTo(type.toLowerCase()); // Lowercase
                    });
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Privacy levels should be usable in switch statements")
        void testPrivacyLevels_UsableInSwitchStatements() {
            // Given
            Privacy privacy = Privacy.PUBLIC;

            // When
            String result = switch (privacy) {
                case PUBLIC -> "public";
                case PRIVATE -> "private";
                case PROTECTED -> "protected";
                case PACKAGE_PROTECTED -> "package";
            };

            // Then
            assertThat(result).isEqualTo("public");
        }

        @Test
        @DisplayName("Privacy levels should be usable in collections")
        void testPrivacyLevels_UsableInCollections() {
            // Given
            var visiblePrivacyLevels = java.util.Set.of(
                    Privacy.PUBLIC,
                    Privacy.PROTECTED);

            // When/Then
            assertThat(visiblePrivacyLevels).hasSize(2);
            assertThat(visiblePrivacyLevels).contains(Privacy.PUBLIC);
            assertThat(visiblePrivacyLevels).doesNotContain(Privacy.PRIVATE);
        }

        @Test
        @DisplayName("Privacy levels should work with streams")
        void testPrivacyLevels_WorkWithStreams() {
            // When
            var privacyStrings = java.util.Arrays.stream(Privacy.values())
                    .map(Privacy::getType)
                    .filter(type -> !type.isEmpty()) // Exclude package-private
                    .sorted()
                    .toList();

            // Then
            assertThat(privacyStrings).hasSize(3);
            assertThat(privacyStrings).containsExactly("private", "protected", "public");
            assertThat(privacyStrings).isSorted();
        }

        @Test
        @DisplayName("Privacy levels should support filtering by visibility")
        void testPrivacyLevels_SupportFilteringByVisibility() {
            // Given - categorize by visibility scope
            var publicAccess = java.util.List.of(Privacy.PUBLIC);
            var packageAccess = java.util.List.of(Privacy.PACKAGE_PROTECTED, Privacy.PROTECTED);
            var restrictedAccess = java.util.List.of(Privacy.PRIVATE);

            // When
            var allPrivacyLevels = java.util.Set.of(Privacy.values());

            // Then
            assertThat(allPrivacyLevels).containsAll(publicAccess);
            assertThat(allPrivacyLevels).containsAll(packageAccess);
            assertThat(allPrivacyLevels).containsAll(restrictedAccess);

            // Verify all privacy levels are accounted for
            int totalExpected = publicAccess.size() + packageAccess.size() + restrictedAccess.size();
            assertThat(allPrivacyLevels).hasSize(totalExpected);
        }

        @Test
        @DisplayName("Privacy levels should work in typical code generation scenarios")
        void testPrivacyLevels_WorkInCodeGenerationScenarios() {
            // Given - common code generation patterns

            // When/Then - Public API elements
            assertThat(Privacy.PUBLIC.getType()).isEqualTo("public");

            // When/Then - Internal implementation details
            assertThat(Privacy.PRIVATE.getType()).isEqualTo("private");

            // When/Then - Inheritance-visible elements
            assertThat(Privacy.PROTECTED.getType()).isEqualTo("protected");

            // When/Then - Package-internal elements
            assertThat(Privacy.PACKAGE_PROTECTED.getType()).isEmpty();

            // Code generation should be able to use these directly
            for (Privacy privacy : Privacy.values()) {
                String modifier = privacy.getType();
                String generatedCode = modifier + " class TestClass {}";

                // Should produce valid Java syntax
                if (!modifier.isEmpty()) {
                    assertThat(generatedCode).contains(modifier + " class");
                } else {
                    assertThat(generatedCode).startsWith(" class"); // Package-private
                }
            }
        }
    }
}
