package org.specs.generators.java.types;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Comprehensive test suite for the {@link Primitive} enum.
 * Tests all primitive types, their wrappers, and validation methods.
 * 
 * @author Generated Tests
 */
@DisplayName("Primitive Enum Tests")
public class PrimitiveTest {

    @Nested
    @DisplayName("Enum Constants Tests")
    class EnumConstantsTests {

        @Test
        @DisplayName("Should have all expected primitive types")
        void testAllPrimitiveTypes_ArePresent() {
            // When
            Primitive[] primitives = Primitive.values();

            // Then
            assertThat(primitives).hasSize(9);
            assertThat(primitives).containsExactlyInAnyOrder(
                    Primitive.VOID, Primitive.BYTE, Primitive.SHORT, Primitive.INT,
                    Primitive.LONG, Primitive.FLOAT, Primitive.DOUBLE,
                    Primitive.BOOLEAN, Primitive.CHAR);
        }

        @ParameterizedTest
        @EnumSource(Primitive.class)
        @DisplayName("Each primitive should have a non-null type string")
        void testEachPrimitive_HasNonNullType(Primitive primitive) {
            // When
            String type = primitive.getType();

            // Then
            assertThat(type).isNotNull();
            assertThat(type).isNotEmpty();
        }

        @ParameterizedTest
        @CsvSource({
                "VOID, void",
                "BYTE, byte",
                "SHORT, short",
                "INT, int",
                "LONG, long",
                "FLOAT, float",
                "DOUBLE, double",
                "BOOLEAN, boolean",
                "CHAR, char"
        })
        @DisplayName("Each primitive should have correct type string")
        void testEachPrimitive_HasCorrectTypeString(Primitive primitive, String expectedType) {
            // When
            String actualType = primitive.getType();

            // Then
            assertThat(actualType).isEqualTo(expectedType);
        }
    }

    @Nested
    @DisplayName("getType() Method Tests")
    class GetTypeTests {

        @Test
        @DisplayName("getType() should return consistent values")
        void testGetType_ReturnsConsistentValues() {
            // Given
            Primitive intPrimitive = Primitive.INT;

            // When
            String first = intPrimitive.getType();
            String second = intPrimitive.getType();

            // Then
            assertThat(first).isEqualTo(second);
            assertThat(first).isEqualTo("int");
        }

        @Test
        @DisplayName("getType() should return immutable strings")
        void testGetType_ReturnsImmutableStrings() {
            // Given
            Primitive booleanPrimitive = Primitive.BOOLEAN;

            // When
            String type = booleanPrimitive.getType();

            // Then
            assertThat(type).isEqualTo("boolean");
            // Strings are immutable in Java, so this is guaranteed
        }
    }

    @Nested
    @DisplayName("getPrimitive() Method Tests")
    class GetPrimitiveTests {

        @ParameterizedTest
        @ValueSource(strings = { "void", "byte", "short", "int", "long", "float", "double", "boolean", "char" })
        @DisplayName("getPrimitive() should find valid primitive types")
        void testGetPrimitive_ValidTypes_ReturnsCorrectPrimitive(String typeName) {
            // When
            Primitive result = Primitive.getPrimitive(typeName);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getType()).isEqualTo(typeName);
        }

        @Test
        @DisplayName("getPrimitive() should be case sensitive")
        void testGetPrimitive_CaseSensitive() {
            // Given/When/Then
            assertThatThrownBy(() -> Primitive.getPrimitive("INT"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("The type 'INT' is not a primitive.");

            assertThatThrownBy(() -> Primitive.getPrimitive("Boolean"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("The type 'Boolean' is not a primitive.");
        }

        @ParameterizedTest
        @ValueSource(strings = { "String", "Object", "Integer", "invalid", "", " " })
        @DisplayName("getPrimitive() should throw for invalid types")
        void testGetPrimitive_InvalidTypes_ThrowsException(String invalidType) {
            // When/Then
            assertThatThrownBy(() -> Primitive.getPrimitive(invalidType))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("The type '" + invalidType + "' is not a primitive.");
        }

        @Test
        @DisplayName("getPrimitive() should handle null input")
        void testGetPrimitive_NullInput_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> Primitive.getPrimitive(null))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("The type 'null' is not a primitive.");
        }

        @Test
        @DisplayName("getPrimitive() should handle whitespace")
        void testGetPrimitive_Whitespace_ThrowsException() {
            // Given/When/Then
            assertThatThrownBy(() -> Primitive.getPrimitive(" int "))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("The type ' int ' is not a primitive.");

            assertThatThrownBy(() -> Primitive.getPrimitive("int "))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("The type 'int ' is not a primitive.");
        }
    }

    @Nested
    @DisplayName("getPrimitiveWrapper() Method Tests")
    class GetPrimitiveWrapperTests {

        @ParameterizedTest
        @CsvSource({
                "VOID, Void",
                "BYTE, Byte",
                "SHORT, Short",
                "INT, Integer",
                "LONG, Long",
                "FLOAT, Float",
                "DOUBLE, Double",
                "BOOLEAN, Boolean",
                "CHAR, Character"
        })
        @DisplayName("Each primitive should have correct wrapper class name")
        void testGetPrimitiveWrapper_ReturnsCorrectWrapper(Primitive primitive, String expectedWrapper) {
            // When
            String wrapper = primitive.getPrimitiveWrapper();

            // Then
            assertThat(wrapper).isEqualTo(expectedWrapper);
        }

        @Test
        @DisplayName("INT primitive should have special Integer wrapper")
        void testGetPrimitiveWrapper_IntSpecialCase() {
            // Given
            Primitive intPrimitive = Primitive.INT;

            // When
            String wrapper = intPrimitive.getPrimitiveWrapper();

            // Then
            assertThat(wrapper).isEqualTo("Integer");
            assertThat(wrapper).isNotEqualTo("Int"); // Should not be capitalized version
        }

        @Test
        @DisplayName("getPrimitiveWrapper() should be consistent across calls")
        void testGetPrimitiveWrapper_ConsistentResults() {
            // Given
            Primitive doublePrimitive = Primitive.DOUBLE;

            // When
            String first = doublePrimitive.getPrimitiveWrapper();
            String second = doublePrimitive.getPrimitiveWrapper();

            // Then
            assertThat(first).isEqualTo(second);
            assertThat(first).isEqualTo("Double");
        }

        @ParameterizedTest
        @EnumSource(Primitive.class)
        @DisplayName("All wrappers should be valid Java class names")
        void testGetPrimitiveWrapper_ValidJavaClassNames(Primitive primitive) {
            // When
            String wrapper = primitive.getPrimitiveWrapper();

            // Then
            assertThat(wrapper).isNotNull();
            assertThat(wrapper).isNotEmpty();
            assertThat(wrapper).matches("^[A-Z][a-zA-Z]*$"); // Valid Java class name pattern
        }
    }

    @Nested
    @DisplayName("contains() Method Tests")
    class ContainsTests {

        @ParameterizedTest
        @ValueSource(strings = { "void", "byte", "short", "int", "long", "float", "double", "boolean", "char" })
        @DisplayName("contains() should return true for valid primitive types")
        void testContains_ValidTypes_ReturnsTrue(String typeName) {
            // When
            boolean result = Primitive.contains(typeName);

            // Then
            assertThat(result).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = { "String", "Object", "Integer", "Boolean", "Char", "INT", "BOOLEAN", "invalid", "" })
        @DisplayName("contains() should return false for invalid types")
        void testContains_InvalidTypes_ReturnsFalse(String invalidType) {
            // When
            boolean result = Primitive.contains(invalidType);

            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("contains() should return false for null")
        void testContains_Null_ReturnsFalse() {
            // When
            boolean result = Primitive.contains(null);

            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("contains() should be case sensitive")
        void testContains_CaseSensitive() {
            // When/Then
            assertThat(Primitive.contains("int")).isTrue();
            assertThat(Primitive.contains("INT")).isFalse();
            assertThat(Primitive.contains("Int")).isFalse();
            assertThat(Primitive.contains("boolean")).isTrue();
            assertThat(Primitive.contains("Boolean")).isFalse();
        }

        @Test
        @DisplayName("contains() should handle whitespace strictly")
        void testContains_WhitespaceHandling() {
            // When/Then
            assertThat(Primitive.contains("int")).isTrue();
            assertThat(Primitive.contains(" int")).isFalse();
            assertThat(Primitive.contains("int ")).isFalse();
            assertThat(Primitive.contains(" int ")).isFalse();
        }
    }

    @Nested
    @DisplayName("Consistency Tests")
    class ConsistencyTests {

        @ParameterizedTest
        @EnumSource(Primitive.class)
        @DisplayName("getPrimitive() and contains() should be consistent")
        void testConsistency_GetPrimitiveAndContains(Primitive primitive) {
            // Given
            String typeName = primitive.getType();

            // When
            boolean contains = Primitive.contains(typeName);
            Primitive found = Primitive.getPrimitive(typeName);

            // Then
            assertThat(contains).isTrue();
            assertThat(found).isEqualTo(primitive);
        }

        @Test
        @DisplayName("All enum values should have unique type strings")
        void testConsistency_UniqueTypeStrings() {
            // Given
            Primitive[] primitives = Primitive.values();

            // When/Then
            for (int i = 0; i < primitives.length; i++) {
                for (int j = i + 1; j < primitives.length; j++) {
                    assertThat(primitives[i].getType())
                            .describedAs("Primitive types should be unique")
                            .isNotEqualTo(primitives[j].getType());
                }
            }
        }

        @Test
        @DisplayName("All enum values should have unique wrapper names")
        void testConsistency_UniqueWrapperNames() {
            // Given
            Primitive[] primitives = Primitive.values();

            // When/Then
            for (int i = 0; i < primitives.length; i++) {
                for (int j = i + 1; j < primitives.length; j++) {
                    assertThat(primitives[i].getPrimitiveWrapper())
                            .describedAs("Primitive wrapper names should be unique")
                            .isNotEqualTo(primitives[j].getPrimitiveWrapper());
                }
            }
        }
    }

    @Nested
    @DisplayName("toString() and Standard Enum Methods Tests")
    class StandardEnumMethodsTests {

        @Test
        @DisplayName("toString() should return enum name")
        void testToString_ReturnsEnumName() {
            // Given
            Primitive intPrimitive = Primitive.INT;

            // When
            String result = intPrimitive.toString();

            // Then
            assertThat(result).isEqualTo("INT");
        }

        @Test
        @DisplayName("valueOf() should work correctly")
        void testValueOf_WorksCorrectly() {
            // When
            Primitive result = Primitive.valueOf("BOOLEAN");

            // Then
            assertThat(result).isEqualTo(Primitive.BOOLEAN);
            assertThat(result.getType()).isEqualTo("boolean");
        }

        @Test
        @DisplayName("values() should return all primitives")
        void testValues_ReturnsAllPrimitives() {
            // When
            Primitive[] values = Primitive.values();

            // Then
            assertThat(values).hasSize(9);
            assertThat(values).contains(
                    Primitive.VOID, Primitive.BYTE, Primitive.SHORT, Primitive.INT,
                    Primitive.LONG, Primitive.FLOAT, Primitive.DOUBLE,
                    Primitive.BOOLEAN, Primitive.CHAR);
        }
    }
}
