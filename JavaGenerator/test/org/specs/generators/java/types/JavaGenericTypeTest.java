package org.specs.generators.java.types;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for {@link JavaGenericType} class.
 * Tests generic type parameter representation and manipulation functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("JavaGenericType Tests")
class JavaGenericTypeTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create generic type with base type")
        void shouldCreateGenericTypeWithBaseType() {
            JavaType baseType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(baseType);

            assertThat(genericType.getTheType()).isEqualTo(baseType);
            assertThat(genericType.getExtendingTypes()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null base type")
        void shouldHandleNullBaseType() {
            // Note: Based on the implementation, this might not throw immediately
            // but the setTheType method might handle it differently
            JavaGenericType genericType = new JavaGenericType(null);
            assertThat(genericType.getTheType()).isNull();
        }
    }

    @Nested
    @DisplayName("Type Management Tests")
    class TypeManagementTests {

        @Test
        @DisplayName("Should add extending types")
        void shouldAddExtendingTypes() {
            JavaType baseType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(baseType);

            JavaType numberType = new JavaType("Number");
            JavaType serializableType = new JavaType("Serializable");

            boolean added1 = genericType.addType(numberType);
            boolean added2 = genericType.addType(serializableType);

            assertThat(added1).isTrue();
            assertThat(added2).isTrue();
            assertThat(genericType.getExtendingTypes()).hasSize(2);
            assertThat(genericType.getExtendingTypes()).contains(numberType, serializableType);
        }

        @Test
        @DisplayName("Should prevent duplicate extending types")
        void shouldPreventDuplicateExtendingTypes() {
            JavaType baseType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(baseType);

            JavaType numberType = new JavaType("Number");

            boolean added1 = genericType.addType(numberType);
            boolean added2 = genericType.addType(numberType); // Duplicate

            assertThat(added1).isTrue();
            assertThat(added2).isFalse();
            assertThat(genericType.getExtendingTypes()).hasSize(1);
            assertThat(genericType.getExtendingTypes()).contains(numberType);
        }

        @Test
        @DisplayName("Should update base type")
        void shouldUpdateBaseType() {
            JavaType initialType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(initialType);

            JavaType newType = new JavaType("U");
            genericType.setTheType(newType);

            assertThat(genericType.getTheType()).isEqualTo(newType);
            assertThat(genericType.getTheType()).isNotEqualTo(initialType);
        }

        @Test
        @DisplayName("Should update extending types list")
        void shouldUpdateExtendingTypesList() {
            JavaType baseType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(baseType);

            // Add initial types
            genericType.addType(new JavaType("Number"));
            assertThat(genericType.getExtendingTypes()).hasSize(1);

            // Replace with new list
            List<JavaType> newExtendingTypes = Arrays.asList(
                    new JavaType("Comparable"),
                    new JavaType("Serializable"));
            genericType.setExtendingTypes(newExtendingTypes);

            assertThat(genericType.getExtendingTypes()).hasSize(2);
            assertThat(genericType.getExtendingTypes().get(0).getName()).isEqualTo("Comparable");
            assertThat(genericType.getExtendingTypes().get(1).getName()).isEqualTo("Serializable");
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("Should generate simple type without extends")
        void shouldGenerateSimpleTypeWithoutExtends() {
            JavaType baseType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(baseType);

            assertThat(genericType.getSimpleType()).isEqualTo("T");
        }

        @Test
        @DisplayName("Should generate simple type with single extends")
        void shouldGenerateSimpleTypeWithSingleExtends() {
            JavaType baseType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(baseType);
            genericType.addType(new JavaType("Number"));

            assertThat(genericType.getSimpleType()).isEqualTo("T extends Number");
        }

        @Test
        @DisplayName("Should generate simple type with multiple extends")
        void shouldGenerateSimpleTypeWithMultipleExtends() {
            JavaType baseType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(baseType);
            genericType.addType(new JavaType("Number"));
            genericType.addType(new JavaType("Serializable"));

            String simpleType = genericType.getSimpleType();
            assertThat(simpleType).contains("T extends");
            assertThat(simpleType).contains("Number");
            assertThat(simpleType).contains("Serializable");
            assertThat(simpleType).contains("&");
        }

        @Test
        @DisplayName("Should generate canonical type without extends")
        void shouldGenerateCanonicalTypeWithoutExtends() {
            JavaType baseType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(baseType);

            assertThat(genericType.getCanonicalType()).isEqualTo("T");
        }

        @Test
        @DisplayName("Should generate canonical type with extends")
        void shouldGenerateCanonicalTypeWithExtends() {
            JavaType baseType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(baseType);
            genericType.addType(new JavaType("java.lang.Number"));

            assertThat(genericType.getCanonicalType()).isEqualTo("T extends java.lang.Number");
        }

        @Test
        @DisplayName("Should generate wrapped simple type")
        void shouldGenerateWrappedSimpleType() {
            JavaType baseType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(baseType);

            assertThat(genericType.getWrappedSimpleType()).isEqualTo("<T>");
        }

        @Test
        @DisplayName("Should generate wrapped simple type with extends")
        void shouldGenerateWrappedSimpleTypeWithExtends() {
            JavaType baseType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(baseType);
            genericType.addType(new JavaType("Number"));

            assertThat(genericType.getWrappedSimpleType()).isEqualTo("<T extends Number>");
        }

        @Test
        @DisplayName("Should generate toString representation")
        void shouldGenerateToStringRepresentation() {
            JavaType baseType = new JavaType("T");
            JavaGenericType genericType = new JavaGenericType(baseType);

            String toString = genericType.toString();
            assertThat(toString).startsWith("<");
            assertThat(toString).endsWith(">");
            assertThat(toString).contains("T");
        }
    }

    @Nested
    @DisplayName("Clone Tests")
    class CloneTests {

        @Test
        @DisplayName("Should clone simple generic type")
        void shouldCloneSimpleGenericType() {
            JavaType baseType = new JavaType("T");
            JavaGenericType original = new JavaGenericType(baseType);

            JavaGenericType cloned = original.clone();

            assertThat(cloned).isNotSameAs(original);
            assertThat(cloned.getTheType()).isNotSameAs(original.getTheType());
            assertThat(cloned.getTheType().getName()).isEqualTo(original.getTheType().getName());
            assertThat(cloned.getExtendingTypes()).isEmpty();
        }

        @Test
        @DisplayName("Should clone generic type with extending types")
        void shouldCloneGenericTypeWithExtendingTypes() {
            JavaType baseType = new JavaType("T");
            JavaGenericType original = new JavaGenericType(baseType);
            original.addType(new JavaType("Number"));
            original.addType(new JavaType("Serializable"));

            JavaGenericType cloned = original.clone();

            assertThat(cloned).isNotSameAs(original);
            assertThat(cloned.getTheType()).isNotSameAs(original.getTheType());
            assertThat(cloned.getTheType().getName()).isEqualTo(original.getTheType().getName());
            assertThat(cloned.getExtendingTypes()).hasSize(original.getExtendingTypes().size());

            // Verify independence - changes to original shouldn't affect clone
            original.addType(new JavaType("Comparable"));
            assertThat(cloned.getExtendingTypes()).hasSize(2);
            assertThat(original.getExtendingTypes()).hasSize(3);
        }

        @Test
        @DisplayName("Should deep clone extending types")
        void shouldDeepCloneExtendingTypes() {
            JavaType baseType = new JavaType("T");
            JavaGenericType original = new JavaGenericType(baseType);
            original.addType(new JavaType("Number"));

            JavaGenericType cloned = original.clone();

            assertThat(cloned.getExtendingTypes()).hasSize(1);
            assertThat(original.getExtendingTypes()).hasSize(1);

            // Verify that the extending types are different objects
            assertThat(cloned.getExtendingTypes().get(0))
                    .isNotSameAs(original.getExtendingTypes().get(0));
            assertThat(cloned.getExtendingTypes().get(0).getName())
                    .isEqualTo(original.getExtendingTypes().get(0).getName());
        }
    }

    @Nested
    @DisplayName("Common Generic Type Patterns Tests")
    class CommonGenericTypePatternsTests {

        @Test
        @DisplayName("Should handle unbounded type parameter")
        void shouldHandleUnboundedTypeParameter() {
            // <T>
            JavaGenericType unbounded = new JavaGenericType(new JavaType("T"));

            assertThat(unbounded.getSimpleType()).isEqualTo("T");
            assertThat(unbounded.getWrappedSimpleType()).isEqualTo("<T>");
        }

        @Test
        @DisplayName("Should handle bounded type parameter")
        void shouldHandleBoundedTypeParameter() {
            // <T extends Number>
            JavaGenericType bounded = new JavaGenericType(new JavaType("T"));
            bounded.addType(new JavaType("Number"));

            assertThat(bounded.getSimpleType()).isEqualTo("T extends Number");
            assertThat(bounded.getWrappedSimpleType()).isEqualTo("<T extends Number>");
        }

        @Test
        @DisplayName("Should handle multiple bounds type parameter")
        void shouldHandleMultipleBoundsTypeParameter() {
            // <T extends Number & Serializable>
            JavaGenericType multiBounded = new JavaGenericType(new JavaType("T"));
            multiBounded.addType(new JavaType("Number"));
            multiBounded.addType(new JavaType("Serializable"));

            String simpleType = multiBounded.getSimpleType();
            assertThat(simpleType).contains("T extends");
            assertThat(simpleType).contains("Number");
            assertThat(simpleType).contains("Serializable");
            assertThat(simpleType).contains("&");
        }

        @Test
        @DisplayName("Should handle wildcard-like patterns")
        void shouldHandleWildcardLikePatterns() {
            // Not exactly wildcards, but similar bounded patterns
            JavaGenericType wildcardLike = new JavaGenericType(new JavaType("?"));
            wildcardLike.addType(new JavaType("Number"));

            assertThat(wildcardLike.getSimpleType()).contains("?");
            assertThat(wildcardLike.getSimpleType()).contains("extends");
            assertThat(wildcardLike.getSimpleType()).contains("Number");
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle single letter type parameters")
        void shouldHandleSingleLetterTypeParameters() {
            JavaGenericType singleLetter = new JavaGenericType(new JavaType("T"));
            assertThat(singleLetter.getSimpleType()).isEqualTo("T");

            JavaGenericType otherLetter = new JavaGenericType(new JavaType("E"));
            assertThat(otherLetter.getSimpleType()).isEqualTo("E");
        }

        @Test
        @DisplayName("Should handle multi-letter type parameters")
        void shouldHandleMultiLetterTypeParameters() {
            JavaGenericType multiLetter = new JavaGenericType(new JavaType("TYPE"));
            assertThat(multiLetter.getSimpleType()).isEqualTo("TYPE");

            JavaGenericType descriptive = new JavaGenericType(new JavaType("Element"));
            assertThat(descriptive.getSimpleType()).isEqualTo("Element");
        }

        @Test
        @DisplayName("Should handle fully qualified extending types")
        void shouldHandleFullyQualifiedExtendingTypes() {
            JavaGenericType genericType = new JavaGenericType(new JavaType("T"));
            genericType.addType(new JavaType("java.lang.Number"));
            genericType.addType(new JavaType("java.io.Serializable"));

            String canonicalType = genericType.getCanonicalType();
            assertThat(canonicalType).contains("java.lang.Number");
            assertThat(canonicalType).contains("java.io.Serializable");
        }

        @Test
        @DisplayName("Should handle empty extending types after modification")
        void shouldHandleEmptyExtendingTypesAfterModification() {
            JavaGenericType genericType = new JavaGenericType(new JavaType("T"));

            // Add some extending types
            genericType.addType(new JavaType("Number"));
            assertThat(genericType.getExtendingTypes()).hasSize(1);

            // Clear extending types
            genericType.getExtendingTypes().clear();
            assertThat(genericType.getExtendingTypes()).isEmpty();
            assertThat(genericType.getSimpleType()).isEqualTo("T");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with complex type hierarchies")
        void shouldWorkWithComplexTypeHierarchies() {
            // <T extends Number & Comparable<T> & Serializable>
            JavaGenericType complex = new JavaGenericType(new JavaType("T"));
            complex.addType(new JavaType("Number"));
            complex.addType(new JavaType("Comparable<T>"));
            complex.addType(new JavaType("Serializable"));

            String simpleType = complex.getSimpleType();
            assertThat(simpleType).contains("T extends");
            assertThat(simpleType).contains("Number");
            assertThat(simpleType).contains("Comparable<T>");
            assertThat(simpleType).contains("Serializable");
        }

        @Test
        @DisplayName("Should maintain consistency across different representations")
        void shouldMaintainConsistencyAcrossDifferentRepresentations() {
            JavaGenericType genericType = new JavaGenericType(new JavaType("T"));
            genericType.addType(new JavaType("Number"));

            String simple = genericType.getSimpleType();
            String canonical = genericType.getCanonicalType();
            String wrapped = genericType.getWrappedSimpleType();
            String toString = genericType.toString();

            // All should contain the base type name
            assertThat(simple).contains("T");
            assertThat(canonical).contains("T");
            assertThat(wrapped).contains("T");
            assertThat(toString).contains("T");

            // All should contain extending type
            assertThat(simple).contains("Number");
            assertThat(canonical).contains("Number");
            assertThat(wrapped).contains("Number");
            assertThat(toString).contains("Number");
        }
    }
}
