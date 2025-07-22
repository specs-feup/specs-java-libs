package org.specs.generators.java.enums;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Comprehensive Phase 4 test class for {@link ObjectOfPrimitives}.
 * Tests all primitive wrapper type enum values, method behavior, string
 * representations, type checking functionality, primitive mapping, and
 * integration with the Java type system.
 * 
 * @author Generated Tests
 */
@DisplayName("ObjectOfPrimitives Enum Tests - Phase 4")
public class ObjectOfPrimitivesTest {

    @Nested
    @DisplayName("Enum Value Tests")
    class EnumValueTests {

        @Test
        @DisplayName("All primitive wrapper enum values should be present")
        void testAllObjectOfPrimitivesValues_ArePresent() {
            // When
            ObjectOfPrimitives[] values = ObjectOfPrimitives.values();

            // Then
            assertThat(values).hasSize(7);
            assertThat(values).containsExactlyInAnyOrder(
                    ObjectOfPrimitives.INTEGER,
                    ObjectOfPrimitives.DOUBLE,
                    ObjectOfPrimitives.FLOAT,
                    ObjectOfPrimitives.LONG,
                    ObjectOfPrimitives.SHORT,
                    ObjectOfPrimitives.BYTE,
                    ObjectOfPrimitives.BOOLEAN);
        }

        @Test
        @DisplayName("valueOf() should work for all valid wrapper type names")
        void testValueOf_WithValidNames_ReturnsCorrectType() {
            // When/Then
            assertThat(ObjectOfPrimitives.valueOf("INTEGER")).isEqualTo(ObjectOfPrimitives.INTEGER);
            assertThat(ObjectOfPrimitives.valueOf("DOUBLE")).isEqualTo(ObjectOfPrimitives.DOUBLE);
            assertThat(ObjectOfPrimitives.valueOf("FLOAT")).isEqualTo(ObjectOfPrimitives.FLOAT);
            assertThat(ObjectOfPrimitives.valueOf("LONG")).isEqualTo(ObjectOfPrimitives.LONG);
            assertThat(ObjectOfPrimitives.valueOf("SHORT")).isEqualTo(ObjectOfPrimitives.SHORT);
            assertThat(ObjectOfPrimitives.valueOf("BYTE")).isEqualTo(ObjectOfPrimitives.BYTE);
            assertThat(ObjectOfPrimitives.valueOf("BOOLEAN")).isEqualTo(ObjectOfPrimitives.BOOLEAN);
        }

        @Test
        @DisplayName("valueOf() should throw exception for invalid type name")
        void testValueOf_WithInvalidName_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> ObjectOfPrimitives.valueOf("INVALID_TYPE"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("valueOf() should throw exception for null")
        void testValueOf_WithNull_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> ObjectOfPrimitives.valueOf(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("getType() Method Tests")
    class GetTypeMethodTests {

        @Test
        @DisplayName("getType() should return correct wrapper type names for all values")
        void testGetType_ReturnsCorrectWrapperTypeNames() {
            // When/Then
            assertThat(ObjectOfPrimitives.INTEGER.getType()).isEqualTo("Integer");
            assertThat(ObjectOfPrimitives.DOUBLE.getType()).isEqualTo("Double");
            assertThat(ObjectOfPrimitives.FLOAT.getType()).isEqualTo("Float");
            assertThat(ObjectOfPrimitives.LONG.getType()).isEqualTo("Long");
            assertThat(ObjectOfPrimitives.SHORT.getType()).isEqualTo("Short");
            assertThat(ObjectOfPrimitives.BYTE.getType()).isEqualTo("Byte");
            assertThat(ObjectOfPrimitives.BOOLEAN.getType()).isEqualTo("Boolean");
        }

        @ParameterizedTest(name = "getType() for {0} should not be null or empty")
        @EnumSource(ObjectOfPrimitives.class)
        @DisplayName("getType() should never return null or empty string")
        void testGetType_AllTypes_NotNullOrEmpty(ObjectOfPrimitives objectType) {
            // When
            String type = objectType.getType();

            // Then
            assertThat(type).isNotNull();
            assertThat(type).isNotEmpty();
            assertThat(type).isNotBlank();
        }

        @ParameterizedTest(name = "getType() for {0} should start with uppercase")
        @EnumSource(ObjectOfPrimitives.class)
        @DisplayName("getType() should return capitalized wrapper class names")
        void testGetType_AllTypes_StartWithUppercase(ObjectOfPrimitives objectType) {
            // When
            String type = objectType.getType();

            // Then
            assertThat(type).matches("^[A-Z][a-z]*$"); // Starts with uppercase, rest lowercase
        }

        @ParameterizedTest(name = "getType() for {0} should be valid Java wrapper class")
        @EnumSource(ObjectOfPrimitives.class)
        @DisplayName("getType() should return valid Java wrapper class names")
        void testGetType_AllTypes_ValidJavaWrapperClasses(ObjectOfPrimitives objectType) {
            // When
            String type = objectType.getType();

            // Then - Should be one of the valid Java wrapper classes
            assertThat(type).isIn("Byte", "Short", "Integer", "Long", "Float", "Double", "Boolean", "Character");
        }
    }

    @Nested
    @DisplayName("toString() Method Tests")
    class ToStringMethodTests {

        @Test
        @DisplayName("toString() should return same as getType() for all types")
        void testToString_ReturnsSameAsGetType() {
            // When/Then
            assertThat(ObjectOfPrimitives.INTEGER.toString()).isEqualTo(ObjectOfPrimitives.INTEGER.getType());
            assertThat(ObjectOfPrimitives.DOUBLE.toString()).isEqualTo(ObjectOfPrimitives.DOUBLE.getType());
            assertThat(ObjectOfPrimitives.FLOAT.toString()).isEqualTo(ObjectOfPrimitives.FLOAT.getType());
            assertThat(ObjectOfPrimitives.LONG.toString()).isEqualTo(ObjectOfPrimitives.LONG.getType());
            assertThat(ObjectOfPrimitives.SHORT.toString()).isEqualTo(ObjectOfPrimitives.SHORT.getType());
            assertThat(ObjectOfPrimitives.BYTE.toString()).isEqualTo(ObjectOfPrimitives.BYTE.getType());
            assertThat(ObjectOfPrimitives.BOOLEAN.toString()).isEqualTo(ObjectOfPrimitives.BOOLEAN.getType());
        }

        @ParameterizedTest(name = "toString() for {0} should match getType()")
        @EnumSource(ObjectOfPrimitives.class)
        @DisplayName("toString() should match getType() for all types")
        void testToString_AllTypes_MatchGetType(ObjectOfPrimitives objectType) {
            // When
            String toString = objectType.toString();
            String getType = objectType.getType();

            // Then
            assertThat(toString).isEqualTo(getType);
        }
    }

    @Nested
    @DisplayName("getPrimitive() Static Method Tests")
    class GetPrimitiveMethodTests {

        @Test
        @DisplayName("getPrimitive() should return correct primitive types for all wrapper types")
        void testGetPrimitive_ReturnsCorrectPrimitiveTypes() {
            // When/Then
            assertThat(ObjectOfPrimitives.getPrimitive("INTEGER")).isEqualTo("int");
            assertThat(ObjectOfPrimitives.getPrimitive("DOUBLE")).isEqualTo("double");
            assertThat(ObjectOfPrimitives.getPrimitive("FLOAT")).isEqualTo("float");
            assertThat(ObjectOfPrimitives.getPrimitive("LONG")).isEqualTo("long");
            assertThat(ObjectOfPrimitives.getPrimitive("SHORT")).isEqualTo("short");
            assertThat(ObjectOfPrimitives.getPrimitive("BYTE")).isEqualTo("byte");
            assertThat(ObjectOfPrimitives.getPrimitive("BOOLEAN")).isEqualTo("boolean");
        }

        @Test
        @DisplayName("getPrimitive() should handle case insensitive input")
        void testGetPrimitive_HandlesCaseInsensitiveInput() {
            // When/Then
            assertThat(ObjectOfPrimitives.getPrimitive("integer")).isEqualTo("int");
            assertThat(ObjectOfPrimitives.getPrimitive("Integer")).isEqualTo("int");
            assertThat(ObjectOfPrimitives.getPrimitive("InTeGeR")).isEqualTo("int");
            assertThat(ObjectOfPrimitives.getPrimitive("BOOLEAN")).isEqualTo("boolean");
            assertThat(ObjectOfPrimitives.getPrimitive("boolean")).isEqualTo("boolean");
        }

        @Test
        @DisplayName("getPrimitive() should handle special INTEGER case")
        void testGetPrimitive_HandlesSpecialIntegerCase() {
            // When/Then - INTEGER maps to int.class.getName() which is "int"
            assertThat(ObjectOfPrimitives.getPrimitive("INTEGER")).isEqualTo("int");
            assertThat(ObjectOfPrimitives.getPrimitive("integer")).isEqualTo("int");

            // The method uses a special case for INTEGER
            assertThat(ObjectOfPrimitives.getPrimitive("INTEGER")).isEqualTo(int.class.getName());
        }

        @Test
        @DisplayName("getPrimitive() should throw exception for invalid type")
        void testGetPrimitive_WithInvalidType_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> ObjectOfPrimitives.getPrimitive("INVALID_TYPE"))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> ObjectOfPrimitives.getPrimitive("String"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("getPrimitive() should throw exception for null")
        void testGetPrimitive_WithNull_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> ObjectOfPrimitives.getPrimitive(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @ParameterizedTest(name = "getPrimitive() should work for uppercase enum name: {0}")
        @ValueSource(strings = { "INTEGER", "DOUBLE", "FLOAT", "LONG", "SHORT", "BYTE", "BOOLEAN" })
        @DisplayName("getPrimitive() should work for all uppercase enum names")
        void testGetPrimitive_WithUppercaseEnumNames_Works(String enumName) {
            // When/Then
            assertThatCode(() -> ObjectOfPrimitives.getPrimitive(enumName))
                    .doesNotThrowAnyException();

            String primitive = ObjectOfPrimitives.getPrimitive(enumName);
            assertThat(primitive).isNotNull();
            assertThat(primitive).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("contains() Static Method Tests")
    class ContainsMethodTests {

        @Test
        @DisplayName("contains() should return true for all valid wrapper type names")
        void testContains_WithValidTypes_ReturnsTrue() {
            // When/Then
            assertThat(ObjectOfPrimitives.contains("Integer")).isTrue();
            assertThat(ObjectOfPrimitives.contains("Double")).isTrue();
            assertThat(ObjectOfPrimitives.contains("Float")).isTrue();
            assertThat(ObjectOfPrimitives.contains("Long")).isTrue();
            assertThat(ObjectOfPrimitives.contains("Short")).isTrue();
            assertThat(ObjectOfPrimitives.contains("Byte")).isTrue();
            assertThat(ObjectOfPrimitives.contains("Boolean")).isTrue();
        }

        @Test
        @DisplayName("contains() should return false for invalid type names")
        void testContains_WithInvalidTypes_ReturnsFalse() {
            // When/Then
            assertThat(ObjectOfPrimitives.contains("String")).isFalse();
            assertThat(ObjectOfPrimitives.contains("Object")).isFalse();
            assertThat(ObjectOfPrimitives.contains("Character")).isFalse(); // Not in the enum
            assertThat(ObjectOfPrimitives.contains("int")).isFalse(); // Primitive, not wrapper
            assertThat(ObjectOfPrimitives.contains("invalid")).isFalse();
        }

        @Test
        @DisplayName("contains() should return false for null")
        void testContains_WithNull_ReturnsFalse() {
            // When/Then
            assertThat(ObjectOfPrimitives.contains(null)).isFalse();
        }

        @Test
        @DisplayName("contains() should return false for empty string")
        void testContains_WithEmptyString_ReturnsFalse() {
            // When/Then
            assertThat(ObjectOfPrimitives.contains("")).isFalse();
            assertThat(ObjectOfPrimitives.contains("   ")).isFalse();
        }

        @ParameterizedTest(name = "contains() should return true for wrapper type: {0}")
        @ValueSource(strings = { "Integer", "Double", "Float", "Long", "Short", "Byte", "Boolean" })
        @DisplayName("contains() should return true for all supported wrapper types")
        void testContains_WithAllSupportedTypes_ReturnsTrue(String type) {
            // When/Then
            assertThat(ObjectOfPrimitives.contains(type)).isTrue();
        }

        @ParameterizedTest(name = "contains() should return false for type: {0}")
        @ValueSource(strings = { "int", "double", "float", "long", "short", "byte", "boolean", "String", "Character" })
        @DisplayName("contains() should return false for unsupported types")
        void testContains_WithUnsupportedTypes_ReturnsFalse(String type) {
            // When/Then
            assertThat(ObjectOfPrimitives.contains(type)).isFalse();
        }

        @Test
        @DisplayName("contains() should be case sensitive")
        void testContains_IsCaseSensitive() {
            // When/Then
            assertThat(ObjectOfPrimitives.contains("Integer")).isTrue();
            assertThat(ObjectOfPrimitives.contains("INTEGER")).isFalse();
            assertThat(ObjectOfPrimitives.contains("integer")).isFalse();
            assertThat(ObjectOfPrimitives.contains("iNtEgEr")).isFalse();
        }
    }

    @Nested
    @DisplayName("Individual Type Tests")
    class IndividualTypeTests {

        @Test
        @DisplayName("INTEGER type should have correct properties")
        void testInteger_HasCorrectProperties() {
            // When/Then
            assertThat(ObjectOfPrimitives.INTEGER.name()).isEqualTo("INTEGER");
            assertThat(ObjectOfPrimitives.INTEGER.getType()).isEqualTo("Integer");
            assertThat(ObjectOfPrimitives.INTEGER.toString()).isEqualTo("Integer");
            assertThat(ObjectOfPrimitives.INTEGER.ordinal()).isEqualTo(0);
        }

        @Test
        @DisplayName("DOUBLE type should have correct properties")
        void testDouble_HasCorrectProperties() {
            // When/Then
            assertThat(ObjectOfPrimitives.DOUBLE.name()).isEqualTo("DOUBLE");
            assertThat(ObjectOfPrimitives.DOUBLE.getType()).isEqualTo("Double");
            assertThat(ObjectOfPrimitives.DOUBLE.toString()).isEqualTo("Double");
            assertThat(ObjectOfPrimitives.DOUBLE.ordinal()).isEqualTo(1);
        }

        @Test
        @DisplayName("FLOAT type should have correct properties")
        void testFloat_HasCorrectProperties() {
            // When/Then
            assertThat(ObjectOfPrimitives.FLOAT.name()).isEqualTo("FLOAT");
            assertThat(ObjectOfPrimitives.FLOAT.getType()).isEqualTo("Float");
            assertThat(ObjectOfPrimitives.FLOAT.toString()).isEqualTo("Float");
            assertThat(ObjectOfPrimitives.FLOAT.ordinal()).isEqualTo(2);
        }

        @Test
        @DisplayName("LONG type should have correct properties")
        void testLong_HasCorrectProperties() {
            // When/Then
            assertThat(ObjectOfPrimitives.LONG.name()).isEqualTo("LONG");
            assertThat(ObjectOfPrimitives.LONG.getType()).isEqualTo("Long");
            assertThat(ObjectOfPrimitives.LONG.toString()).isEqualTo("Long");
            assertThat(ObjectOfPrimitives.LONG.ordinal()).isEqualTo(3);
        }

        @Test
        @DisplayName("SHORT type should have correct properties")
        void testShort_HasCorrectProperties() {
            // When/Then
            assertThat(ObjectOfPrimitives.SHORT.name()).isEqualTo("SHORT");
            assertThat(ObjectOfPrimitives.SHORT.getType()).isEqualTo("Short");
            assertThat(ObjectOfPrimitives.SHORT.toString()).isEqualTo("Short");
            assertThat(ObjectOfPrimitives.SHORT.ordinal()).isEqualTo(4);
        }

        @Test
        @DisplayName("BYTE type should have correct properties")
        void testByte_HasCorrectProperties() {
            // When/Then
            assertThat(ObjectOfPrimitives.BYTE.name()).isEqualTo("BYTE");
            assertThat(ObjectOfPrimitives.BYTE.getType()).isEqualTo("Byte");
            assertThat(ObjectOfPrimitives.BYTE.toString()).isEqualTo("Byte");
            assertThat(ObjectOfPrimitives.BYTE.ordinal()).isEqualTo(5);
        }

        @Test
        @DisplayName("BOOLEAN type should have correct properties")
        void testBoolean_HasCorrectProperties() {
            // When/Then
            assertThat(ObjectOfPrimitives.BOOLEAN.name()).isEqualTo("BOOLEAN");
            assertThat(ObjectOfPrimitives.BOOLEAN.getType()).isEqualTo("Boolean");
            assertThat(ObjectOfPrimitives.BOOLEAN.toString()).isEqualTo("Boolean");
            assertThat(ObjectOfPrimitives.BOOLEAN.ordinal()).isEqualTo(6);
        }
    }

    @Nested
    @DisplayName("Type System Integration Tests")
    class TypeSystemIntegrationTests {

        @Test
        @DisplayName("All types should correspond to Java wrapper classes")
        void testAllTypes_CorrespondToJavaWrapperClasses() {
            // When/Then
            assertThat(ObjectOfPrimitives.INTEGER.getType()).isEqualTo(Integer.class.getSimpleName());
            assertThat(ObjectOfPrimitives.DOUBLE.getType()).isEqualTo(Double.class.getSimpleName());
            assertThat(ObjectOfPrimitives.FLOAT.getType()).isEqualTo(Float.class.getSimpleName());
            assertThat(ObjectOfPrimitives.LONG.getType()).isEqualTo(Long.class.getSimpleName());
            assertThat(ObjectOfPrimitives.SHORT.getType()).isEqualTo(Short.class.getSimpleName());
            assertThat(ObjectOfPrimitives.BYTE.getType()).isEqualTo(Byte.class.getSimpleName());
            assertThat(ObjectOfPrimitives.BOOLEAN.getType()).isEqualTo(Boolean.class.getSimpleName());
        }

        @Test
        @DisplayName("getPrimitive() should correctly map wrapper to primitive types")
        void testGetPrimitive_CorrectlyMapsWrapperToPrimitiveTypes() {
            // When/Then - Verify bidirectional mapping
            assertThat(ObjectOfPrimitives.getPrimitive("INTEGER")).isEqualTo(int.class.getName());
            assertThat(ObjectOfPrimitives.getPrimitive("DOUBLE")).isEqualTo(double.class.getName());
            assertThat(ObjectOfPrimitives.getPrimitive("FLOAT")).isEqualTo(float.class.getName());
            assertThat(ObjectOfPrimitives.getPrimitive("LONG")).isEqualTo(long.class.getName());
            assertThat(ObjectOfPrimitives.getPrimitive("SHORT")).isEqualTo(short.class.getName());
            assertThat(ObjectOfPrimitives.getPrimitive("BYTE")).isEqualTo(byte.class.getName());
            assertThat(ObjectOfPrimitives.getPrimitive("BOOLEAN")).isEqualTo(boolean.class.getName());
        }

        @Test
        @DisplayName("Types should cover most common primitive wrapper types")
        void testTypes_CoverCommonPrimitiveWrapperTypes() {
            // Given
            var supportedTypes = java.util.Arrays.stream(ObjectOfPrimitives.values())
                    .map(ObjectOfPrimitives::getType)
                    .collect(java.util.stream.Collectors.toSet());

            // When/Then - Most common wrapper types should be supported
            assertThat(supportedTypes).containsExactlyInAnyOrder(
                    "Byte", "Short", "Integer", "Long", "Float", "Double", "Boolean");

            // Character is not included - this might be intentional for numeric focus
        }

        @Test
        @DisplayName("Types should distinguish numeric from non-numeric wrappers")
        void testTypes_DistinguishNumericFromNonNumericWrappers() {
            // Given
            var numericTypes = java.util.Set.of("Byte", "Short", "Integer", "Long", "Float", "Double");
            var nonNumericTypes = java.util.Set.of("Boolean");

            // When
            var actualNumericTypes = java.util.Arrays.stream(ObjectOfPrimitives.values())
                    .map(ObjectOfPrimitives::getType)
                    .filter(numericTypes::contains)
                    .collect(java.util.stream.Collectors.toSet());

            var actualNonNumericTypes = java.util.Arrays.stream(ObjectOfPrimitives.values())
                    .map(ObjectOfPrimitives::getType)
                    .filter(nonNumericTypes::contains)
                    .collect(java.util.stream.Collectors.toSet());

            // Then
            assertThat(actualNumericTypes).containsExactlyInAnyOrder("Byte", "Short", "Integer", "Long", "Float",
                    "Double");
            assertThat(actualNonNumericTypes).containsExactlyInAnyOrder("Boolean");
            assertThat(actualNumericTypes).hasSize(6);
            assertThat(actualNonNumericTypes).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Wrapper types should be usable in switch statements")
        void testWrapperTypes_UsableInSwitchStatements() {
            // Given
            ObjectOfPrimitives type = ObjectOfPrimitives.INTEGER;

            // When
            String result = switch (type) {
                case INTEGER -> "integer";
                case DOUBLE -> "double";
                case FLOAT -> "float";
                case LONG -> "long";
                case SHORT -> "short";
                case BYTE -> "byte";
                case BOOLEAN -> "boolean";
            };

            // Then
            assertThat(result).isEqualTo("integer");
        }

        @Test
        @DisplayName("Wrapper types should be usable in collections")
        void testWrapperTypes_UsableInCollections() {
            // Given
            var numericTypes = java.util.Set.of(
                    ObjectOfPrimitives.BYTE,
                    ObjectOfPrimitives.SHORT,
                    ObjectOfPrimitives.INTEGER,
                    ObjectOfPrimitives.LONG,
                    ObjectOfPrimitives.FLOAT,
                    ObjectOfPrimitives.DOUBLE);

            // When/Then
            assertThat(numericTypes).hasSize(6);
            assertThat(numericTypes).contains(ObjectOfPrimitives.INTEGER);
            assertThat(numericTypes).doesNotContain(ObjectOfPrimitives.BOOLEAN);
        }

        @Test
        @DisplayName("contains() method should work with generated type strings")
        void testContains_WorksWithGeneratedTypeStrings() {
            // Given
            var typeStrings = java.util.Arrays.stream(ObjectOfPrimitives.values())
                    .map(ObjectOfPrimitives::getType)
                    .toList();

            // When/Then
            for (String typeString : typeStrings) {
                assertThat(ObjectOfPrimitives.contains(typeString)).isTrue();
            }
        }

        @Test
        @DisplayName("getPrimitive() should work for all enum values")
        void testGetPrimitive_WorksForAllEnumValues() {
            // When/Then
            for (ObjectOfPrimitives objType : ObjectOfPrimitives.values()) {
                String enumName = objType.name();

                assertThatCode(() -> ObjectOfPrimitives.getPrimitive(enumName))
                        .doesNotThrowAnyException();

                String primitive = ObjectOfPrimitives.getPrimitive(enumName);
                assertThat(primitive).isNotNull();
                assertThat(primitive).isNotEmpty();
                assertThat(primitive).matches("[a-z]+"); // Should be lowercase primitive type
            }
        }

        @Test
        @DisplayName("Round-trip conversion should work between wrapper and primitive")
        void testRoundTripConversion_WorksBetweenWrapperAndPrimitive() {
            // Given
            var wrapperToPrimitive = java.util.Map.of(
                    "Integer", "int",
                    "Double", "double",
                    "Float", "float",
                    "Long", "long",
                    "Short", "short",
                    "Byte", "byte",
                    "Boolean", "boolean");

            // When/Then
            for (ObjectOfPrimitives objType : ObjectOfPrimitives.values()) {
                String wrapperType = objType.getType();
                String primitiveType = ObjectOfPrimitives.getPrimitive(objType.name());
                String expectedPrimitive = wrapperToPrimitive.get(wrapperType);

                assertThat(primitiveType).isEqualTo(expectedPrimitive);
                assertThat(ObjectOfPrimitives.contains(wrapperType)).isTrue();
            }
        }
    }
}
