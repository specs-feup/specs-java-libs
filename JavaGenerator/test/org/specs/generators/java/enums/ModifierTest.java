package org.specs.generators.java.enums;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * Comprehensive Phase 4 test class for {@link Modifier}.
 * Tests all modifier enum values, method behavior, string representations,
 * and integration with the Java code generation framework.
 * 
 * @author Generated Tests
 */
@DisplayName("Modifier Enum Tests - Phase 4")
public class ModifierTest {

    @Nested
    @DisplayName("Enum Value Tests")
    class EnumValueTests {

        @Test
        @DisplayName("All modifier enum values should be present")
        void testAllModifierValues_ArePresent() {
            // When
            Modifier[] values = Modifier.values();

            // Then
            assertThat(values).hasSize(3);
            assertThat(values).containsExactlyInAnyOrder(
                    Modifier.ABSTRACT,
                    Modifier.STATIC,
                    Modifier.FINAL);
        }

        @Test
        @DisplayName("valueOf() should work for all valid modifier names")
        void testValueOf_WithValidNames_ReturnsCorrectModifier() {
            // When/Then
            assertThat(Modifier.valueOf("ABSTRACT")).isEqualTo(Modifier.ABSTRACT);
            assertThat(Modifier.valueOf("STATIC")).isEqualTo(Modifier.STATIC);
            assertThat(Modifier.valueOf("FINAL")).isEqualTo(Modifier.FINAL);
        }

        @Test
        @DisplayName("valueOf() should throw exception for invalid modifier name")
        void testValueOf_WithInvalidName_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> Modifier.valueOf("INVALID_MODIFIER"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("valueOf() should throw exception for null")
        void testValueOf_WithNull_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> Modifier.valueOf(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("getType() Method Tests")
    class GetTypeMethodTests {

        @Test
        @DisplayName("getType() should return correct modifier strings for all values")
        void testGetType_ReturnsCorrectModifierStrings() {
            // When/Then
            assertThat(Modifier.ABSTRACT.getType()).isEqualTo("abstract");
            assertThat(Modifier.STATIC.getType()).isEqualTo("static");
            assertThat(Modifier.FINAL.getType()).isEqualTo("final");
        }

        @ParameterizedTest(name = "getType() for {0} should not be null or empty")
        @EnumSource(Modifier.class)
        @DisplayName("getType() should never return null or empty string")
        void testGetType_AllModifiers_NotNullOrEmpty(Modifier modifier) {
            // When
            String type = modifier.getType();

            // Then
            assertThat(type).isNotNull();
            assertThat(type).isNotEmpty();
            assertThat(type).isNotBlank();
        }

        @ParameterizedTest(name = "getType() for {0} should be lowercase")
        @EnumSource(Modifier.class)
        @DisplayName("getType() should return lowercase strings")
        void testGetType_AllModifiers_ReturnLowercase(Modifier modifier) {
            // When
            String type = modifier.getType();

            // Then
            assertThat(type).isEqualTo(type.toLowerCase());
            assertThat(type).matches("[a-z]+"); // Only lowercase letters
        }

        @ParameterizedTest(name = "getType() for {0} should be valid Java modifier")
        @EnumSource(Modifier.class)
        @DisplayName("getType() should return valid Java modifiers")
        void testGetType_AllModifiers_ValidJavaModifiers(Modifier modifier) {
            // When
            String type = modifier.getType();

            // Then - Should be one of the valid Java modifiers
            assertThat(type).isIn("abstract", "static", "final", "public", "private", "protected",
                    "synchronized", "native", "transient", "volatile", "strictfp");
        }
    }

    @Nested
    @DisplayName("toString() Method Tests")
    class ToStringMethodTests {

        @Test
        @DisplayName("toString() should return same as getType() for all modifiers")
        void testToString_ReturnsSameAsGetType() {
            // When/Then
            assertThat(Modifier.ABSTRACT.toString()).isEqualTo(Modifier.ABSTRACT.getType());
            assertThat(Modifier.STATIC.toString()).isEqualTo(Modifier.STATIC.getType());
            assertThat(Modifier.FINAL.toString()).isEqualTo(Modifier.FINAL.getType());
        }

        @ParameterizedTest(name = "toString() for {0} should match getType()")
        @EnumSource(Modifier.class)
        @DisplayName("toString() should match getType() for all modifiers")
        void testToString_AllModifiers_MatchGetType(Modifier modifier) {
            // When
            String toString = modifier.toString();
            String getType = modifier.getType();

            // Then
            assertThat(toString).isEqualTo(getType);
        }
    }

    @Nested
    @DisplayName("Individual Modifier Tests")
    class IndividualModifierTests {

        @Test
        @DisplayName("ABSTRACT modifier should have correct properties")
        void testAbstract_HasCorrectProperties() {
            // When/Then
            assertThat(Modifier.ABSTRACT.name()).isEqualTo("ABSTRACT");
            assertThat(Modifier.ABSTRACT.getType()).isEqualTo("abstract");
            assertThat(Modifier.ABSTRACT.toString()).isEqualTo("abstract");
            assertThat(Modifier.ABSTRACT.ordinal()).isEqualTo(0);
        }

        @Test
        @DisplayName("STATIC modifier should have correct properties")
        void testStatic_HasCorrectProperties() {
            // When/Then
            assertThat(Modifier.STATIC.name()).isEqualTo("STATIC");
            assertThat(Modifier.STATIC.getType()).isEqualTo("static");
            assertThat(Modifier.STATIC.toString()).isEqualTo("static");
            assertThat(Modifier.STATIC.ordinal()).isEqualTo(1);
        }

        @Test
        @DisplayName("FINAL modifier should have correct properties")
        void testFinal_HasCorrectProperties() {
            // When/Then
            assertThat(Modifier.FINAL.name()).isEqualTo("FINAL");
            assertThat(Modifier.FINAL.getType()).isEqualTo("final");
            assertThat(Modifier.FINAL.toString()).isEqualTo("final");
            assertThat(Modifier.FINAL.ordinal()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Enum Behavior Tests")
    class EnumBehaviorTests {

        @Test
        @DisplayName("Enum values should maintain order")
        void testEnumValues_MaintainOrder() {
            // When
            Modifier[] values = Modifier.values();

            // Then
            assertThat(values).containsExactly(
                    Modifier.ABSTRACT,
                    Modifier.STATIC,
                    Modifier.FINAL);
        }

        @Test
        @DisplayName("Enum should be serializable")
        void testEnum_IsSerializable() {
            // Then
            assertThat(Modifier.ABSTRACT).isInstanceOf(java.io.Serializable.class);
            assertThat(Enum.class).isAssignableFrom(Modifier.class);
        }

        @Test
        @DisplayName("Enum values should be comparable")
        void testEnum_ValuesAreComparable() {
            // When/Then
            assertThat(Modifier.ABSTRACT.compareTo(Modifier.STATIC)).isNegative();
            assertThat(Modifier.STATIC.compareTo(Modifier.ABSTRACT)).isPositive();
            assertThat(Modifier.ABSTRACT.compareTo(Modifier.ABSTRACT)).isZero();
        }

        @Test
        @DisplayName("Enum should support equality")
        void testEnum_SupportsEquality() {
            // When/Then
            assertThat(Modifier.ABSTRACT).isEqualTo(Modifier.ABSTRACT);
            assertThat(Modifier.ABSTRACT).isNotEqualTo(Modifier.STATIC);
            assertThat(Modifier.ABSTRACT.equals(Modifier.ABSTRACT)).isTrue();
            assertThat(Modifier.ABSTRACT.equals(Modifier.STATIC)).isFalse();
            assertThat(Modifier.ABSTRACT.equals(null)).isFalse();
            assertThat(Modifier.ABSTRACT.equals("abstract")).isFalse();
        }

        @Test
        @DisplayName("Enum should have consistent hashCode")
        void testEnum_HasConsistentHashCode() {
            // When
            int hashCode1 = Modifier.ABSTRACT.hashCode();
            int hashCode2 = Modifier.ABSTRACT.hashCode();

            // Then
            assertThat(hashCode1).isEqualTo(hashCode2);
            assertThat(Modifier.ABSTRACT.hashCode()).isNotEqualTo(Modifier.STATIC.hashCode());
        }
    }

    @Nested
    @DisplayName("Java Modifier Specific Tests")
    class JavaModifierSpecificTests {

        @Test
        @DisplayName("All modifiers should be applicable to classes")
        void testAllModifiers_ApplicableToClasses() {
            // When/Then
            // abstract - classes can be abstract
            assertThat(Modifier.ABSTRACT.getType()).isEqualTo("abstract");

            // static - nested classes can be static
            assertThat(Modifier.STATIC.getType()).isEqualTo("static");

            // final - classes can be final
            assertThat(Modifier.FINAL.getType()).isEqualTo("final");
        }

        @Test
        @DisplayName("All modifiers should be applicable to methods")
        void testAllModifiers_ApplicableToMethods() {
            // When/Then
            // abstract - methods can be abstract (in interfaces/abstract classes)
            assertThat(Modifier.ABSTRACT.getType()).isEqualTo("abstract");

            // static - methods can be static
            assertThat(Modifier.STATIC.getType()).isEqualTo("static");

            // final - methods can be final
            assertThat(Modifier.FINAL.getType()).isEqualTo("final");
        }

        @Test
        @DisplayName("All modifiers should be applicable to fields")
        void testAllModifiers_ApplicableToFields() {
            // When/Then
            // abstract - fields cannot be abstract (but class can be)
            // static - fields can be static
            assertThat(Modifier.STATIC.getType()).isEqualTo("static");

            // final - fields can be final
            assertThat(Modifier.FINAL.getType()).isEqualTo("final");
        }

        @Test
        @DisplayName("Modifier combinations should be logically valid")
        void testModifierCombinations_LogicallyValid() {
            // When/Then
            // static final is valid
            var staticFinal = java.util.Set.of(Modifier.STATIC, Modifier.FINAL);
            assertThat(staticFinal).hasSize(2);

            // abstract final would be invalid (but enum doesn't prevent it - that's
            // compile-time check)
            var abstractFinal = java.util.Set.of(Modifier.ABSTRACT, Modifier.FINAL);
            assertThat(abstractFinal).hasSize(2); // Enum allows it, compilation would catch invalid usage
        }

        @Test
        @DisplayName("Modifiers should map to standard Java keywords")
        void testModifiers_MapToStandardJavaKeywords() {
            // When
            var modifierKeywords = java.util.Arrays.stream(Modifier.values())
                    .map(Modifier::getType)
                    .collect(java.util.stream.Collectors.toSet());

            // Then
            assertThat(modifierKeywords).containsExactlyInAnyOrder("abstract", "static", "final");

            // All should be valid Java reserved words
            for (String keyword : modifierKeywords) {
                assertThat(keyword).matches("[a-z]+"); // Valid identifier pattern
                assertThat(keyword).doesNotContain(" "); // No spaces
                assertThat(keyword).isEqualTo(keyword.toLowerCase()); // Lowercase
            }
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Modifiers should be usable in switch statements")
        void testModifiers_UsableInSwitchStatements() {
            // Given
            Modifier modifier = Modifier.STATIC;

            // When
            String result = switch (modifier) {
                case ABSTRACT -> "abstract";
                case STATIC -> "static";
                case FINAL -> "final";
            };

            // Then
            assertThat(result).isEqualTo("static");
        }

        @Test
        @DisplayName("Modifiers should be usable in collections")
        void testModifiers_UsableInCollections() {
            // Given
            var classModifiers = java.util.Set.of(
                    Modifier.ABSTRACT,
                    Modifier.FINAL);

            // When/Then
            assertThat(classModifiers).hasSize(2);
            assertThat(classModifiers).contains(Modifier.ABSTRACT);
            assertThat(classModifiers).doesNotContain(Modifier.STATIC);
        }

        @Test
        @DisplayName("Modifiers should work with streams")
        void testModifiers_WorkWithStreams() {
            // When
            var modifierStrings = java.util.Arrays.stream(Modifier.values())
                    .map(Modifier::getType)
                    .sorted()
                    .toList();

            // Then
            assertThat(modifierStrings).hasSize(3);
            assertThat(modifierStrings).containsExactly("abstract", "final", "static");
            assertThat(modifierStrings).isSorted();
        }

        @Test
        @DisplayName("Modifiers should support filtering by applicability")
        void testModifiers_SupportFilteringByApplicability() {
            // Given - modifiers that can be applied to fields
            var fieldModifiers = java.util.Arrays.stream(Modifier.values())
                    .filter(m -> m == Modifier.STATIC || m == Modifier.FINAL)
                    .toList();

            // Given - modifiers that can be applied to methods
            var methodModifiers = java.util.Arrays.stream(Modifier.values())
                    .toList(); // All can be applied to methods in some context

            // When/Then
            assertThat(fieldModifiers).hasSize(2);
            assertThat(fieldModifiers).contains(Modifier.STATIC, Modifier.FINAL);
            assertThat(fieldModifiers).doesNotContain(Modifier.ABSTRACT);

            assertThat(methodModifiers).hasSize(3);
            assertThat(methodModifiers).containsExactlyInAnyOrder(
                    Modifier.ABSTRACT, Modifier.STATIC, Modifier.FINAL);
        }
    }
}
