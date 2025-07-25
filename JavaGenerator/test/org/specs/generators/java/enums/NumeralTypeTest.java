package org.specs.generators.java.enums;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Comprehensive Phase 4 test class for {@link NumeralType}.
 * Tests all numeral type enum values, method behavior, string representations,
 * type checking functionality, and integration with the Java type system.
 * 
 * @author Generated Tests
 */
@DisplayName("NumeralType Enum Tests - Phase 4")
public class NumeralTypeTest {

    @Nested
    @DisplayName("Enum Value Tests")
    class EnumValueTests {

        @Test
        @DisplayName("All numeral type enum values should be present")
        void testAllNumeralTypeValues_ArePresent() {
            // When
            NumeralType[] values = NumeralType.values();

            // Then
            assertThat(values).hasSize(6);
            assertThat(values).containsExactlyInAnyOrder(
                    NumeralType.INT,
                    NumeralType.DOUBLE,
                    NumeralType.FLOAT,
                    NumeralType.LONG,
                    NumeralType.SHORT,
                    NumeralType.BYTE);
        }

        @Test
        @DisplayName("valueOf() should work for all valid numeral type names")
        void testValueOf_WithValidNames_ReturnsCorrectType() {
            // When/Then
            assertThat(NumeralType.valueOf("INT")).isEqualTo(NumeralType.INT);
            assertThat(NumeralType.valueOf("DOUBLE")).isEqualTo(NumeralType.DOUBLE);
            assertThat(NumeralType.valueOf("FLOAT")).isEqualTo(NumeralType.FLOAT);
            assertThat(NumeralType.valueOf("LONG")).isEqualTo(NumeralType.LONG);
            assertThat(NumeralType.valueOf("SHORT")).isEqualTo(NumeralType.SHORT);
            assertThat(NumeralType.valueOf("BYTE")).isEqualTo(NumeralType.BYTE);
        }

        @Test
        @DisplayName("valueOf() should throw exception for invalid type name")
        void testValueOf_WithInvalidName_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> NumeralType.valueOf("INVALID_TYPE"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("valueOf() should throw exception for null")
        void testValueOf_WithNull_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> NumeralType.valueOf(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("getType() Method Tests")
    class GetTypeMethodTests {

        @Test
        @DisplayName("getType() should return correct primitive type names for all values")
        void testGetType_ReturnsCorrectPrimitiveTypeNames() {
            // When/Then
            assertThat(NumeralType.INT.getType()).isEqualTo("int");
            assertThat(NumeralType.DOUBLE.getType()).isEqualTo("double");
            assertThat(NumeralType.FLOAT.getType()).isEqualTo("float");
            assertThat(NumeralType.LONG.getType()).isEqualTo("long");
            assertThat(NumeralType.SHORT.getType()).isEqualTo("short");
            assertThat(NumeralType.BYTE.getType()).isEqualTo("byte");
        }

        @ParameterizedTest(name = "getType() for {0} should not be null or empty")
        @EnumSource(NumeralType.class)
        @DisplayName("getType() should never return null or empty string")
        void testGetType_AllTypes_NotNullOrEmpty(NumeralType numeralType) {
            // When
            String type = numeralType.getType();

            // Then
            assertThat(type).isNotNull();
            assertThat(type).isNotEmpty();
            assertThat(type).isNotBlank();
        }

        @ParameterizedTest(name = "getType() for {0} should be lowercase")
        @EnumSource(NumeralType.class)
        @DisplayName("getType() should return lowercase type names")
        void testGetType_AllTypes_ReturnLowercase(NumeralType numeralType) {
            // When
            String type = numeralType.getType();

            // Then
            assertThat(type).isEqualTo(type.toLowerCase());
            assertThat(type).matches("[a-z]+"); // Only lowercase letters
        }

        @ParameterizedTest(name = "getType() for {0} should be valid Java primitive")
        @EnumSource(NumeralType.class)
        @DisplayName("getType() should return valid Java primitive type names")
        void testGetType_AllTypes_ValidJavaPrimitives(NumeralType numeralType) {
            // When
            String type = numeralType.getType();

            // Then - Should be one of the valid Java primitive types
            assertThat(type).isIn("byte", "short", "int", "long", "float", "double", "char", "boolean");
        }
    }

    @Nested
    @DisplayName("toString() Method Tests")
    class ToStringMethodTests {

        @Test
        @DisplayName("toString() should return same as getType() for all types")
        void testToString_ReturnsSameAsGetType() {
            // When/Then
            assertThat(NumeralType.INT.toString()).isEqualTo(NumeralType.INT.getType());
            assertThat(NumeralType.DOUBLE.toString()).isEqualTo(NumeralType.DOUBLE.getType());
            assertThat(NumeralType.FLOAT.toString()).isEqualTo(NumeralType.FLOAT.getType());
            assertThat(NumeralType.LONG.toString()).isEqualTo(NumeralType.LONG.getType());
            assertThat(NumeralType.SHORT.toString()).isEqualTo(NumeralType.SHORT.getType());
            assertThat(NumeralType.BYTE.toString()).isEqualTo(NumeralType.BYTE.getType());
        }

        @ParameterizedTest(name = "toString() for {0} should match getType()")
        @EnumSource(NumeralType.class)
        @DisplayName("toString() should match getType() for all types")
        void testToString_AllTypes_MatchGetType(NumeralType numeralType) {
            // When
            String toString = numeralType.toString();
            String getType = numeralType.getType();

            // Then
            assertThat(toString).isEqualTo(getType);
        }
    }

    @Nested
    @DisplayName("contains() Static Method Tests")
    class ContainsMethodTests {

        @Test
        @DisplayName("contains() should return true for all valid primitive type names")
        void testContains_WithValidTypes_ReturnsTrue() {
            // When/Then
            assertThat(NumeralType.contains("int")).isTrue();
            assertThat(NumeralType.contains("double")).isTrue();
            assertThat(NumeralType.contains("float")).isTrue();
            assertThat(NumeralType.contains("long")).isTrue();
            assertThat(NumeralType.contains("short")).isTrue();
            assertThat(NumeralType.contains("byte")).isTrue();
        }

        @Test
        @DisplayName("contains() should return false for invalid type names")
        void testContains_WithInvalidTypes_ReturnsFalse() {
            // When/Then
            assertThat(NumeralType.contains("String")).isFalse();
            assertThat(NumeralType.contains("Integer")).isFalse();
            assertThat(NumeralType.contains("char")).isFalse();
            assertThat(NumeralType.contains("boolean")).isFalse();
            assertThat(NumeralType.contains("void")).isFalse();
            assertThat(NumeralType.contains("invalid")).isFalse();
        }

        @Test
        @DisplayName("contains() should return false for null")
        void testContains_WithNull_ReturnsFalse() {
            // When/Then
            assertThat(NumeralType.contains(null)).isFalse();
        }

        @Test
        @DisplayName("contains() should return false for empty string")
        void testContains_WithEmptyString_ReturnsFalse() {
            // When/Then
            assertThat(NumeralType.contains("")).isFalse();
            assertThat(NumeralType.contains("   ")).isFalse();
        }

        @ParameterizedTest(name = "contains() should return true for type: {0}")
        @ValueSource(strings = { "int", "double", "float", "long", "short", "byte" })
        @DisplayName("contains() should return true for all supported numeral types")
        void testContains_WithAllSupportedTypes_ReturnsTrue(String type) {
            // When/Then
            assertThat(NumeralType.contains(type)).isTrue();
        }

        @ParameterizedTest(name = "contains() should return false for type: {0}")
        @ValueSource(strings = { "Integer", "Double", "Float", "Long", "Short", "Byte", "String", "char", "boolean" })
        @DisplayName("contains() should return false for unsupported types")
        void testContains_WithUnsupportedTypes_ReturnsFalse(String type) {
            // When/Then
            assertThat(NumeralType.contains(type)).isFalse();
        }

        @Test
        @DisplayName("contains() should be case sensitive")
        void testContains_IsCaseSensitive() {
            // When/Then
            assertThat(NumeralType.contains("int")).isTrue();
            assertThat(NumeralType.contains("INT")).isFalse();
            assertThat(NumeralType.contains("Int")).isFalse();
            assertThat(NumeralType.contains("iNt")).isFalse();
        }
    }

    @Nested
    @DisplayName("Individual Type Tests")
    class IndividualTypeTests {

        @Test
        @DisplayName("INT type should have correct properties")
        void testInt_HasCorrectProperties() {
            // When/Then
            assertThat(NumeralType.INT.name()).isEqualTo("INT");
            assertThat(NumeralType.INT.getType()).isEqualTo("int");
            assertThat(NumeralType.INT.toString()).isEqualTo("int");
            assertThat(NumeralType.INT.ordinal()).isEqualTo(0);
        }

        @Test
        @DisplayName("DOUBLE type should have correct properties")
        void testDouble_HasCorrectProperties() {
            // When/Then
            assertThat(NumeralType.DOUBLE.name()).isEqualTo("DOUBLE");
            assertThat(NumeralType.DOUBLE.getType()).isEqualTo("double");
            assertThat(NumeralType.DOUBLE.toString()).isEqualTo("double");
            assertThat(NumeralType.DOUBLE.ordinal()).isEqualTo(1);
        }

        @Test
        @DisplayName("FLOAT type should have correct properties")
        void testFloat_HasCorrectProperties() {
            // When/Then
            assertThat(NumeralType.FLOAT.name()).isEqualTo("FLOAT");
            assertThat(NumeralType.FLOAT.getType()).isEqualTo("float");
            assertThat(NumeralType.FLOAT.toString()).isEqualTo("float");
            assertThat(NumeralType.FLOAT.ordinal()).isEqualTo(2);
        }

        @Test
        @DisplayName("LONG type should have correct properties")
        void testLong_HasCorrectProperties() {
            // When/Then
            assertThat(NumeralType.LONG.name()).isEqualTo("LONG");
            assertThat(NumeralType.LONG.getType()).isEqualTo("long");
            assertThat(NumeralType.LONG.toString()).isEqualTo("long");
            assertThat(NumeralType.LONG.ordinal()).isEqualTo(3);
        }

        @Test
        @DisplayName("SHORT type should have correct properties")
        void testShort_HasCorrectProperties() {
            // When/Then
            assertThat(NumeralType.SHORT.name()).isEqualTo("SHORT");
            assertThat(NumeralType.SHORT.getType()).isEqualTo("short");
            assertThat(NumeralType.SHORT.toString()).isEqualTo("short");
            assertThat(NumeralType.SHORT.ordinal()).isEqualTo(4);
        }

        @Test
        @DisplayName("BYTE type should have correct properties")
        void testByte_HasCorrectProperties() {
            // When/Then
            assertThat(NumeralType.BYTE.name()).isEqualTo("BYTE");
            assertThat(NumeralType.BYTE.getType()).isEqualTo("byte");
            assertThat(NumeralType.BYTE.toString()).isEqualTo("byte");
            assertThat(NumeralType.BYTE.ordinal()).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("Type System Integration Tests")
    class TypeSystemIntegrationTests {

        @Test
        @DisplayName("All types should correspond to Java primitive classes")
        void testAllTypes_CorrespondToJavaPrimitiveClasses() {
            // When/Then
            assertThat(NumeralType.INT.getType()).isEqualTo(int.class.getName());
            assertThat(NumeralType.DOUBLE.getType()).isEqualTo(double.class.getName());
            assertThat(NumeralType.FLOAT.getType()).isEqualTo(float.class.getName());
            assertThat(NumeralType.LONG.getType()).isEqualTo(long.class.getName());
            assertThat(NumeralType.SHORT.getType()).isEqualTo(short.class.getName());
            assertThat(NumeralType.BYTE.getType()).isEqualTo(byte.class.getName());
        }

        @Test
        @DisplayName("All types should be numeric primitives")
        void testAllTypes_AreNumericPrimitives() {
            // When/Then
            for (NumeralType type : NumeralType.values()) {
                String typeName = type.getType();

                // Should be one of the numeric primitive types (excluding char and boolean)
                assertThat(typeName).isIn("byte", "short", "int", "long", "float", "double");
            }
        }

        @Test
        @DisplayName("Types should cover all numeric primitive types")
        void testTypes_CoverAllNumericPrimitiveTypes() {
            // Given
            var supportedTypes = java.util.Arrays.stream(NumeralType.values())
                    .map(NumeralType::getType)
                    .collect(java.util.stream.Collectors.toSet());

            // When/Then - All Java numeric primitive types should be supported
            assertThat(supportedTypes).containsExactlyInAnyOrder(
                    "byte", "short", "int", "long", "float", "double");
        }

        @Test
        @DisplayName("Types should distinguish integral from floating point")
        void testTypes_DistinguishIntegralFromFloatingPoint() {
            // Given
            var integralTypes = java.util.Set.of("byte", "short", "int", "long");
            var floatingTypes = java.util.Set.of("float", "double");

            // When
            var actualIntegralTypes = java.util.Arrays.stream(NumeralType.values())
                    .map(NumeralType::getType)
                    .filter(integralTypes::contains)
                    .collect(java.util.stream.Collectors.toSet());

            var actualFloatingTypes = java.util.Arrays.stream(NumeralType.values())
                    .map(NumeralType::getType)
                    .filter(floatingTypes::contains)
                    .collect(java.util.stream.Collectors.toSet());

            // Then
            assertThat(actualIntegralTypes).containsExactlyInAnyOrder("byte", "short", "int", "long");
            assertThat(actualFloatingTypes).containsExactlyInAnyOrder("float", "double");
            assertThat(actualIntegralTypes).hasSize(4);
            assertThat(actualFloatingTypes).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Numeral types should be usable in switch statements")
        void testNumeralTypes_UsableInSwitchStatements() {
            // Given
            NumeralType type = NumeralType.INT;

            // When
            String result = switch (type) {
                case INT -> "integer";
                case DOUBLE -> "double";
                case FLOAT -> "float";
                case LONG -> "long";
                case SHORT -> "short";
                case BYTE -> "byte";
            };

            // Then
            assertThat(result).isEqualTo("integer");
        }

        @Test
        @DisplayName("Numeral types should be usable in collections")
        void testNumeralTypes_UsableInCollections() {
            // Given
            var integerTypes = java.util.Set.of(
                    NumeralType.BYTE,
                    NumeralType.SHORT,
                    NumeralType.INT,
                    NumeralType.LONG);

            // When/Then
            assertThat(integerTypes).hasSize(4);
            assertThat(integerTypes).contains(NumeralType.INT);
            assertThat(integerTypes).doesNotContain(NumeralType.DOUBLE);
        }

        @Test
        @DisplayName("contains() method should work with generated type strings")
        void testContains_WorksWithGeneratedTypeStrings() {
            // Given
            var typeStrings = java.util.Arrays.stream(NumeralType.values())
                    .map(NumeralType::getType)
                    .toList();

            // When/Then
            for (String typeString : typeStrings) {
                assertThat(NumeralType.contains(typeString)).isTrue();
            }
        }

        @Test
        @DisplayName("Numeral types should support filtering by bit size")
        void testNumeralTypes_SupportFilteringByBitSize() {
            // Given - categorize by typical bit sizes
            var smallTypes = java.util.List.of(NumeralType.BYTE); // 8-bit
            var mediumTypes = java.util.List.of(NumeralType.SHORT); // 16-bit
            var standardTypes = java.util.List.of(NumeralType.INT, NumeralType.FLOAT); // 32-bit
            var largeTypes = java.util.List.of(NumeralType.LONG, NumeralType.DOUBLE); // 64-bit

            // When
            var allTypes = java.util.Set.of(NumeralType.values());

            // Then
            assertThat(allTypes).containsAll(smallTypes);
            assertThat(allTypes).containsAll(mediumTypes);
            assertThat(allTypes).containsAll(standardTypes);
            assertThat(allTypes).containsAll(largeTypes);

            // Verify all types are accounted for
            int totalExpected = smallTypes.size() + mediumTypes.size() +
                    standardTypes.size() + largeTypes.size();
            assertThat(allTypes).hasSize(totalExpected);
        }
    }
}
