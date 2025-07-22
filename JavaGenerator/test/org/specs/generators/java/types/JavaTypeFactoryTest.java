package org.specs.generators.java.types;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.specs.generators.java.classtypes.JavaClass;
import tdrc.utils.Pair;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Comprehensive test suite for the {@link JavaTypeFactory} class.
 * Tests all factory methods for creating JavaType instances and type utilities.
 * 
 * @author Generated Tests
 */
@DisplayName("JavaTypeFactory Tests")
public class JavaTypeFactoryTest {

    @Nested
    @DisplayName("Basic Type Creation Tests")
    class BasicTypeCreationTests {

        @Test
        @DisplayName("getWildCardType() should create wildcard type")
        void testGetWildCardType_CreatesWildcardType() {
            // When
            JavaType wildcardType = JavaTypeFactory.getWildCardType();

            // Then
            assertThat(wildcardType).isNotNull();
            assertThat(wildcardType.getSimpleType()).isEqualTo("?");
            assertThat(wildcardType.getName()).isEqualTo("?");
        }

        @Test
        @DisplayName("getObjectType() should create Object type")
        void testGetObjectType_CreatesObjectType() {
            // When
            JavaType objectType = JavaTypeFactory.getObjectType();

            // Then
            assertThat(objectType).isNotNull();
            assertThat(objectType.getSimpleType()).isEqualTo("Object");
            assertThat(objectType.getCanonicalType()).isEqualTo("java.lang.Object");
        }

        @Test
        @DisplayName("getStringType() should create String type")
        void testGetStringType_CreatesStringType() {
            // When
            JavaType stringType = JavaTypeFactory.getStringType();

            // Then
            assertThat(stringType).isNotNull();
            assertThat(stringType.getSimpleType()).isEqualTo("String");
            assertThat(stringType.getCanonicalType()).isEqualTo("java.lang.String");
        }

        @Test
        @DisplayName("getClassType() should create Class type")
        void testGetClassType_CreatesClassType() {
            // When
            JavaType classType = JavaTypeFactory.getClassType();

            // Then
            assertThat(classType).isNotNull();
            assertThat(classType.getSimpleType()).isEqualTo("Class");
            assertThat(classType.getCanonicalType()).isEqualTo("java.lang.Class");
        }
    }

    @Nested
    @DisplayName("Primitive Type Creation Tests")
    class PrimitiveTypeCreationTests {

        @Test
        @DisplayName("getBooleanType() should create boolean primitive")
        void testGetBooleanType_CreatesBooleanPrimitive() {
            // When
            JavaType booleanType = JavaTypeFactory.getBooleanType();

            // Then
            assertThat(booleanType).isNotNull();
            assertThat(booleanType.getSimpleType()).isEqualTo("boolean");
            assertThat(booleanType.getCanonicalType()).isEqualTo("java.lang.boolean");
            assertThat(booleanType.isPrimitive()).isTrue();
        }

        @Test
        @DisplayName("getIntType() should create int primitive")
        void testGetIntType_CreatesIntPrimitive() {
            // When
            JavaType intType = JavaTypeFactory.getIntType();

            // Then
            assertThat(intType).isNotNull();
            assertThat(intType.getSimpleType()).isEqualTo("int");
            assertThat(intType.getCanonicalType()).isEqualTo("java.lang.int");
            assertThat(intType.isPrimitive()).isTrue();
        }

        @Test
        @DisplayName("getVoidType() should create void primitive")
        void testGetVoidType_CreatesVoidPrimitive() {
            // When
            JavaType voidType = JavaTypeFactory.getVoidType();

            // Then
            assertThat(voidType).isNotNull();
            assertThat(voidType.getSimpleType()).isEqualTo("void");
            assertThat(voidType.getCanonicalType()).isEqualTo("java.lang.void");
            assertThat(voidType.isPrimitive()).isTrue();
        }

        @Test
        @DisplayName("getDoubleType() should create double primitive")
        void testGetDoubleType_CreatesDoublePrimitive() {
            // When
            JavaType doubleType = JavaTypeFactory.getDoubleType();

            // Then
            assertThat(doubleType).isNotNull();
            assertThat(doubleType.getSimpleType()).isEqualTo("double");
            assertThat(doubleType.getCanonicalType()).isEqualTo("java.lang.double");
            assertThat(doubleType.isPrimitive()).isTrue();
        }

        @Test
        @DisplayName("getPrimitiveType() should create correct primitive types")
        void testGetPrimitiveType_CreatesCorrectPrimitives() {
            // When
            JavaType intType = JavaTypeFactory.getPrimitiveType(Primitive.INT);
            JavaType boolType = JavaTypeFactory.getPrimitiveType(Primitive.BOOLEAN);

            // Then
            assertThat(intType.getSimpleType()).isEqualTo("int");
            assertThat(boolType.getSimpleType()).isEqualTo("boolean");
            assertThat(intType.isPrimitive()).isTrue();
            assertThat(boolType.isPrimitive()).isTrue();
        }
    }

    @Nested
    @DisplayName("Primitive Wrapper Tests")
    class PrimitiveWrapperTests {

        @Test
        @DisplayName("getPrimitiveWrapper() should create correct wrapper types")
        void testGetPrimitiveWrapper_CreatesCorrectWrappers() {
            // When
            JavaType integerType = JavaTypeFactory.getPrimitiveWrapper(Primitive.INT);
            JavaType booleanType = JavaTypeFactory.getPrimitiveWrapper(Primitive.BOOLEAN);

            // Then
            assertThat(integerType.getSimpleType()).isEqualTo("Integer");
            assertThat(booleanType.getSimpleType()).isEqualTo("Boolean");
            assertThat(integerType.getCanonicalType()).isEqualTo("java.lang.Integer");
            assertThat(booleanType.getCanonicalType()).isEqualTo("java.lang.Boolean");
        }

        @Test
        @DisplayName("getPrimitiveWrapper(String) should handle special cases")
        void testGetPrimitiveWrapper_StringParam_HandlesSpecialCases() {
            // When
            JavaType integerType = JavaTypeFactory.getPrimitiveWrapper("Integer");
            JavaType booleanType = JavaTypeFactory.getPrimitiveWrapper("boolean");

            // Then
            assertThat(integerType.getSimpleType()).isEqualTo("Integer");
            assertThat(booleanType.getSimpleType()).isEqualTo("Boolean");
        }

        @Test
        @DisplayName("isPrimitiveWrapper() should correctly identify wrapper types")
        void testIsPrimitiveWrapper_CorrectlyIdentifiesWrappers() {
            // When/Then
            assertThat(JavaTypeFactory.isPrimitiveWrapper("Integer")).isTrue();
            assertThat(JavaTypeFactory.isPrimitiveWrapper("Boolean")).isTrue();
            assertThat(JavaTypeFactory.isPrimitiveWrapper("Double")).isTrue();
            assertThat(JavaTypeFactory.isPrimitiveWrapper("String")).isFalse();
            assertThat(JavaTypeFactory.isPrimitiveWrapper("int")).isFalse();
        }
    }

    @Nested
    @DisplayName("Generic Type Creation Tests")
    class GenericTypeCreationTests {

        @Test
        @DisplayName("getListJavaType() with JavaGenericType should create parameterized List")
        void testGetListJavaType_WithJavaGenericType_CreatesParameterizedList() {
            // Given
            JavaGenericType stringGeneric = new JavaGenericType(JavaTypeFactory.getStringType());

            // When
            JavaType listType = JavaTypeFactory.getListJavaType(stringGeneric);

            // Then
            assertThat(listType).isNotNull();
            assertThat(listType.getSimpleType()).isEqualTo("List<String>");
            assertThat(listType.getCanonicalType()).isEqualTo("java.util.List<java.lang.String>");
        }

        @Test
        @DisplayName("getListJavaType() with JavaType should create parameterized List")
        void testGetListJavaType_WithJavaType_CreatesParameterizedList() {
            // Given
            JavaType stringType = JavaTypeFactory.getStringType();

            // When
            JavaType listType = JavaTypeFactory.getListJavaType(stringType);

            // Then
            assertThat(listType).isNotNull();
            assertThat(listType.getSimpleType()).isEqualTo("List<String>");
            assertThat(listType.getCanonicalType()).isEqualTo("java.util.List<java.lang.String>");
        }

        @Test
        @DisplayName("getListStringJavaType() should create List<String>")
        void testGetListStringJavaType_CreatesListString() {
            // When
            JavaType listStringType = JavaTypeFactory.getListStringJavaType();

            // Then
            assertThat(listStringType).isNotNull();
            assertThat(listStringType.getSimpleType()).isEqualTo("List<String>");
            assertThat(listStringType.getCanonicalType()).isEqualTo("java.util.List<java.lang.String>");
        }

        @Test
        @DisplayName("getWildExtendsType() should create wildcard extends type")
        void testGetWildExtendsType_CreatesWildcardExtendsType() {
            // Given
            JavaType stringType = JavaTypeFactory.getStringType();

            // When
            JavaGenericType wildExtendsType = JavaTypeFactory.getWildExtendsType(stringType);

            // Then
            assertThat(wildExtendsType).isNotNull();
            assertThat(wildExtendsType.toString()).contains("? extends java.lang.String");
        }

        @Test
        @DisplayName("addGenericType() should add generic to target type")
        void testAddGenericType_AddsGenericToTarget() {
            // Given
            JavaType listType = new JavaType(List.class);
            JavaType stringType = JavaTypeFactory.getStringType();

            // When
            JavaTypeFactory.addGenericType(listType, stringType);

            // Then
            assertThat(listType.getSimpleType()).isEqualTo("List<String>");
        }
    }

    @Nested
    @DisplayName("Default Value Tests")
    class DefaultValueTests {

        @ParameterizedTest
        @CsvSource({
                "boolean, false",
                "int, 0",
                "double, 0",
                "float, 0",
                "long, 0",
                "byte, 0",
                "short, 0",
                "char, 0"
        })
        @DisplayName("getDefaultValue() should return correct defaults for primitives")
        void testGetDefaultValue_PrimitiveTypes_ReturnsCorrectDefaults(String primitiveType, String expectedDefault) {
            // Given
            JavaType type = JavaTypeFactory.getPrimitiveType(Primitive.getPrimitive(primitiveType));

            // When
            String defaultValue = JavaTypeFactory.getDefaultValue(type);

            // Then
            assertThat(defaultValue).isEqualTo(expectedDefault);
        }

        @Test
        @DisplayName("getDefaultValue() should return empty string for void")
        void testGetDefaultValue_VoidType_ReturnsEmptyString() {
            // Given
            JavaType voidType = JavaTypeFactory.getVoidType();

            // When
            String defaultValue = JavaTypeFactory.getDefaultValue(voidType);

            // Then
            assertThat(defaultValue).isEqualTo("");
        }

        @Test
        @DisplayName("getDefaultValue() should return null for object types")
        void testGetDefaultValue_ObjectTypes_ReturnsNull() {
            // Given
            JavaType stringType = JavaTypeFactory.getStringType();

            // When
            String defaultValue = JavaTypeFactory.getDefaultValue(stringType);

            // Then
            assertThat(defaultValue).isEqualTo("null");
        }
    }

    @Nested
    @DisplayName("Type Validation Tests")
    class TypeValidationTests {

        @ParameterizedTest
        @ValueSource(strings = { "int", "boolean", "double", "float", "long", "byte", "short", "char", "void" })
        @DisplayName("isPrimitive() should return true for primitive types")
        void testIsPrimitive_PrimitiveTypes_ReturnsTrue(String primitiveType) {
            // When
            boolean result = JavaTypeFactory.isPrimitive(primitiveType);

            // Then
            assertThat(result).isTrue();
        }

        @ParameterizedTest
        @ValueSource(strings = { "String", "Object", "Integer", "Boolean" })
        @DisplayName("isPrimitive() should return false for non-primitive types")
        void testIsPrimitive_NonPrimitiveTypes_ReturnsFalse(String nonPrimitiveType) {
            // When
            boolean result = JavaTypeFactory.isPrimitive(nonPrimitiveType);

            // Then
            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("Conversion Tests")
    class ConversionTests {

        @Test
        @DisplayName("convert() should convert JavaClass to JavaType")
        void testConvert_JavaClass_ReturnsJavaType() {
            // Given
            JavaClass javaClass = new JavaClass("TestClass", "com.example");

            // When
            JavaType javaType = JavaTypeFactory.convert(javaClass);

            // Then
            assertThat(javaType).isNotNull();
            assertThat(javaType.getName()).isEqualTo("TestClass");
            assertThat(javaType.getPackage()).isEqualTo("com.example");
        }

        @Test
        @DisplayName("convert() should convert Class to JavaType")
        void testConvert_Class_ReturnsJavaType() {
            // When
            JavaType javaType = JavaTypeFactory.convert(String.class);

            // Then
            assertThat(javaType).isNotNull();
            assertThat(javaType.getSimpleType()).isEqualTo("String");
            assertThat(javaType.getCanonicalType()).isEqualTo("java.lang.String");
        }
    }

    @Nested
    @DisplayName("Primitive Unwrapping Tests")
    class PrimitiveUnwrappingTests {

        @ParameterizedTest
        @CsvSource({
                "Integer, int",
                "Boolean, boolean",
                "Double, double",
                "Float, float",
                "Long, long",
                "Byte, byte",
                "Short, short",
                "Character, char"
        })
        @DisplayName("primitiveUnwrap(String) should unwrap wrapper types correctly")
        void testPrimitiveUnwrap_String_UnwrapsCorrectly(String wrapperType, String expectedPrimitive) {
            // When
            String result = JavaTypeFactory.primitiveUnwrap(wrapperType);

            // Then
            assertThat(result).isEqualTo(expectedPrimitive);
        }

        @Test
        @DisplayName("primitiveUnwrap(String) should handle special Integer case")
        void testPrimitiveUnwrap_String_HandlesIntegerSpecialCase() {
            // When
            String result = JavaTypeFactory.primitiveUnwrap("Integer");

            // Then
            assertThat(result).isEqualTo("int");
        }

        @Test
        @DisplayName("primitiveUnwrap(String) should return unchanged for non-wrappers")
        void testPrimitiveUnwrap_String_ReturnUnchangedForNonWrappers() {
            // When
            String result = JavaTypeFactory.primitiveUnwrap("String");

            // Then
            assertThat(result).isEqualTo("String");
        }

        @Test
        @DisplayName("primitiveUnwrap(JavaType) should unwrap wrapper JavaTypes correctly")
        void testPrimitiveUnwrap_JavaType_UnwrapsCorrectly() {
            // Given
            JavaType integerType = JavaTypeFactory.getPrimitiveWrapper(Primitive.INT);

            // When
            JavaType result = JavaTypeFactory.primitiveUnwrap(integerType);

            // Then
            assertThat(result.getSimpleType()).isEqualTo("int");
            assertThat(result.isPrimitive()).isTrue();
        }

        @Test
        @DisplayName("primitiveUnwrap(JavaType) should return unchanged for non-wrapper types")
        void testPrimitiveUnwrap_JavaType_ReturnUnchangedForNonWrappers() {
            // Given
            JavaType stringType = JavaTypeFactory.getStringType();

            // When
            JavaType result = JavaTypeFactory.primitiveUnwrap(stringType);

            // Then
            assertThat(result).isSameAs(stringType);
            assertThat(result.getSimpleType()).isEqualTo("String");
        }
    }

    @Nested
    @DisplayName("Array Dimension Processing Tests")
    class ArrayDimensionProcessingTests {

        @Test
        @DisplayName("splitTypeFromArrayDimension() should handle simple type without arrays")
        void testSplitTypeFromArrayDimension_SimpleType_ReturnsZeroDimension() {
            // When
            Pair<String, Integer> result = JavaTypeFactory.splitTypeFromArrayDimension("String");

            // Then
            assertThat(result.left()).isEqualTo("String");
            assertThat(result.right()).isEqualTo(0);
        }

        @Test
        @DisplayName("splitTypeFromArrayDimension() should handle single dimension array")
        void testSplitTypeFromArrayDimension_SingleDimension_ReturnsCorrectDimension() {
            // When
            Pair<String, Integer> result = JavaTypeFactory.splitTypeFromArrayDimension("int[]");

            // Then
            assertThat(result.left()).isEqualTo("int");
            assertThat(result.right()).isEqualTo(1);
        }

        @Test
        @DisplayName("splitTypeFromArrayDimension() should handle multi-dimensional arrays")
        void testSplitTypeFromArrayDimension_MultiDimensional_ReturnsCorrectDimension() {
            // When
            Pair<String, Integer> result = JavaTypeFactory.splitTypeFromArrayDimension("String[][][]");

            // Then
            assertThat(result.left()).isEqualTo("String");
            assertThat(result.right()).isEqualTo(3);
        }

        @Test
        @DisplayName("splitTypeFromArrayDimension() should handle type with spaces")
        void testSplitTypeFromArrayDimension_TypeWithSpaces_TrimsCorrectly() {
            // When
            Pair<String, Integer> result = JavaTypeFactory.splitTypeFromArrayDimension("  String  [][]");

            // Then
            assertThat(result.left()).isEqualTo("String");
            assertThat(result.right()).isEqualTo(2);
        }

        @Test
        @DisplayName("splitTypeFromArrayDimension() should throw exception for malformed arrays")
        void testSplitTypeFromArrayDimension_MalformedArray_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> JavaTypeFactory.splitTypeFromArrayDimension("int[]["))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Bad format for array definition. Bad characters: [");
        }

        @Test
        @DisplayName("splitTypeFromArrayDimension() should throw exception for invalid array syntax")
        void testSplitTypeFromArrayDimension_InvalidSyntax_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> JavaTypeFactory.splitTypeFromArrayDimension("int[]abc"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Bad format for array definition. Bad characters: abc");
        }

        @Test
        @DisplayName("splitTypeFromArrayDimension() should handle empty type before array")
        void testSplitTypeFromArrayDimension_EmptyTypeBeforeArray_ReturnsEmptyType() {
            // When
            Pair<String, Integer> result = JavaTypeFactory.splitTypeFromArrayDimension("[]");

            // Then
            assertThat(result.left()).isEqualTo("");
            assertThat(result.right()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Consistency Tests")
    class ConsistencyTests {

        @Test
        @DisplayName("Factory methods should return new instances")
        void testFactoryMethods_ReturnNewInstances() {
            // When
            JavaType string1 = JavaTypeFactory.getStringType();
            JavaType string2 = JavaTypeFactory.getStringType();

            // Then
            assertThat(string1).isNotSameAs(string2);
            assertThat(string1.getSimpleType()).isEqualTo(string2.getSimpleType());
        }

        @Test
        @DisplayName("Primitive types should be consistent between different factory methods")
        void testPrimitiveTypes_ConsistentBetweenMethods() {
            // When
            JavaType intFromSpecific = JavaTypeFactory.getIntType();
            JavaType intFromGeneral = JavaTypeFactory.getPrimitiveType(Primitive.INT);

            // Then
            assertThat(intFromSpecific.getSimpleType()).isEqualTo(intFromGeneral.getSimpleType());
            assertThat(intFromSpecific.getCanonicalType()).isEqualTo(intFromGeneral.getCanonicalType());
            assertThat(intFromSpecific.isPrimitive()).isEqualTo(intFromGeneral.isPrimitive());
        }

        @Test
        @DisplayName("Wrapper and primitive unwrap should be inverse operations")
        void testWrapperAndUnwrap_AreInverseOperations() {
            // Given
            Primitive[] primitives = { Primitive.INT, Primitive.BOOLEAN, Primitive.DOUBLE };

            for (Primitive primitive : primitives) {
                // When
                JavaType wrapper = JavaTypeFactory.getPrimitiveWrapper(primitive);
                JavaType unwrapped = JavaTypeFactory.primitiveUnwrap(wrapper);
                JavaType original = JavaTypeFactory.getPrimitiveType(primitive);

                // Then
                assertThat(unwrapped.getSimpleType()).isEqualTo(original.getSimpleType());
                assertThat(unwrapped.isPrimitive()).isEqualTo(original.isPrimitive());
            }
        }
    }
}
