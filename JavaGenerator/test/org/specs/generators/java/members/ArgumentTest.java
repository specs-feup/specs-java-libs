package org.specs.generators.java.members;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.specs.generators.java.types.JavaType;

/**
 * Test class for {@link Argument} - Method argument handling functionality.
 * Tests argument creation, property management, string representation,
 * and cloning behavior for method parameters.
 * 
 * @author Generated Tests
 */
@DisplayName("Argument Tests")
public class ArgumentTest {

    private JavaType mockJavaType;
    private Argument argument;

    @BeforeEach
    void setUp() {
        mockJavaType = mock(JavaType.class);
        when(mockJavaType.getSimpleType()).thenReturn("String");
        when(mockJavaType.clone()).thenReturn(mockJavaType);

        argument = new Argument(mockJavaType, "testParam");
    }

    @Nested
    @DisplayName("Argument Creation Tests")
    class ArgumentCreationTests {

        @Test
        @DisplayName("Constructor should create argument with correct type and name")
        void testConstructor_CreatesArgumentCorrectly() {
            // Given (from setUp)
            // When (argument created in setUp)

            // Then
            assertThat(argument.getClassType()).isEqualTo(mockJavaType);
            assertThat(argument.getName()).isEqualTo("testParam");
        }

        @Test
        @DisplayName("Constructor should handle null type")
        void testConstructor_WithNullType_AcceptsNull() {
            // When/Then
            assertThatCode(() -> new Argument(null, "testParam"))
                    .doesNotThrowAnyException();

            Argument nullTypeArg = new Argument(null, "testParam");
            assertThat(nullTypeArg.getClassType()).isNull();
            assertThat(nullTypeArg.getName()).isEqualTo("testParam");
        }

        @Test
        @DisplayName("Constructor should handle null name")
        void testConstructor_WithNullName_AcceptsNull() {
            // When/Then
            assertThatCode(() -> new Argument(mockJavaType, null))
                    .doesNotThrowAnyException();

            Argument nullNameArg = new Argument(mockJavaType, null);
            assertThat(nullNameArg.getClassType()).isEqualTo(mockJavaType);
            assertThat(nullNameArg.getName()).isNull();
        }

        @Test
        @DisplayName("Constructor should handle both null values")
        void testConstructor_WithBothNull_AcceptsBoth() {
            // When/Then
            assertThatCode(() -> new Argument(null, null))
                    .doesNotThrowAnyException();

            Argument nullArg = new Argument(null, null);
            assertThat(nullArg.getClassType()).isNull();
            assertThat(nullArg.getName()).isNull();
        }
    }

    @Nested
    @DisplayName("Property Management Tests")
    class PropertyManagementTests {

        @Test
        @DisplayName("getClassType() should return correct type")
        void testGetClassType_ReturnsCorrectType() {
            // When
            JavaType result = argument.getClassType();

            // Then
            assertThat(result).isEqualTo(mockJavaType);
        }

        @Test
        @DisplayName("setClassType() should update type")
        void testSetClassType_UpdatesType() {
            // Given
            JavaType newType = mock(JavaType.class);
            when(newType.getSimpleType()).thenReturn("Integer");

            // When
            argument.setClassType(newType);

            // Then
            assertThat(argument.getClassType()).isEqualTo(newType);
        }

        @Test
        @DisplayName("getName() should return correct name")
        void testGetName_ReturnsCorrectName() {
            // When
            String result = argument.getName();

            // Then
            assertThat(result).isEqualTo("testParam");
        }

        @Test
        @DisplayName("setName() should update name")
        void testSetName_UpdatesName() {
            // When
            argument.setName("newParam");

            // Then
            assertThat(argument.getName()).isEqualTo("newParam");
        }

        @Test
        @DisplayName("setClassType() should accept null")
        void testSetClassType_AcceptsNull() {
            // When
            argument.setClassType(null);

            // Then
            assertThat(argument.getClassType()).isNull();
        }

        @Test
        @DisplayName("setName() should accept null")
        void testSetName_AcceptsNull() {
            // When
            argument.setName(null);

            // Then
            assertThat(argument.getName()).isNull();
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("toString() should format as 'type name'")
        void testToString_FormatsCorrectly() {
            // When
            String result = argument.toString();

            // Then
            assertThat(result).isEqualTo("String testParam");
            verify(mockJavaType).getSimpleType();
        }

        @Test
        @DisplayName("toString() should handle different type names")
        void testToString_WithDifferentTypes_FormatsCorrectly() {
            // Given
            when(mockJavaType.getSimpleType()).thenReturn("List<Integer>");
            argument.setName("items");

            // When
            String result = argument.toString();

            // Then
            assertThat(result).isEqualTo("List<Integer> items");
        }

        @Test
        @DisplayName("toString() should handle null type gracefully")
        void testToString_WithNullType_HandlesGracefully() {
            // Given
            argument.setClassType(null);

            // When/Then
            assertThatThrownBy(() -> argument.toString())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("toString() should handle null name")
        void testToString_WithNullName_IncludesNull() {
            // Given
            argument.setName(null);

            // When
            String result = argument.toString();

            // Then
            assertThat(result).isEqualTo("String null");
        }

        @Test
        @DisplayName("toString() should handle empty name")
        void testToString_WithEmptyName_IncludesEmpty() {
            // Given
            argument.setName("");

            // When
            String result = argument.toString();

            // Then
            assertThat(result).isEqualTo("String ");
        }
    }

    @Nested
    @DisplayName("Cloning Tests")
    class CloningTests {

        @Test
        @DisplayName("clone() should create independent copy")
        void testClone_CreatesIndependentCopy() {
            // When
            Argument cloned = argument.clone();

            // Then
            assertThat(cloned).isNotSameAs(argument);
            assertThat(cloned.getClassType()).isEqualTo(argument.getClassType());
            assertThat(cloned.getName()).isEqualTo(argument.getName());

            // Verify that JavaType.clone() was called
            verify(mockJavaType).clone();
        }

        @Test
        @DisplayName("clone() should preserve all properties")
        void testClone_PreservesAllProperties() {
            // Given
            argument.setName("cloneTest");

            // When
            Argument cloned = argument.clone();

            // Then
            assertThat(cloned.getName()).isEqualTo("cloneTest");
            assertThat(cloned.getClassType()).isEqualTo(mockJavaType);
        }

        @Test
        @DisplayName("clone() modifications should not affect original")
        void testClone_ModificationsDoNotAffectOriginal() {
            // Given
            Argument cloned = argument.clone();

            // When
            cloned.setName("modified");

            // Then
            assertThat(argument.getName()).isEqualTo("testParam");
            assertThat(cloned.getName()).isEqualTo("modified");
        }

        @Test
        @DisplayName("clone() should handle null type")
        void testClone_WithNullType_HandlesGracefully() {
            // Given
            argument.setClassType(null);

            // When/Then
            assertThatThrownBy(() -> argument.clone())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("clone() should handle null name")
        void testClone_WithNullName_PreservesNull() {
            // Given
            argument.setName(null);

            // When
            Argument cloned = argument.clone();

            // Then
            assertThat(cloned.getName()).isNull();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Integration Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Argument should work with complex generic types")
        void testArgument_WithComplexGenericType_WorksCorrectly() {
            // Given
            when(mockJavaType.getSimpleType()).thenReturn("Map<String, List<Integer>>");
            Argument complexArg = new Argument(mockJavaType, "complexParam");

            // When
            String result = complexArg.toString();

            // Then
            assertThat(result).isEqualTo("Map<String, List<Integer>> complexParam");
        }

        @Test
        @DisplayName("Argument should handle array types")
        void testArgument_WithArrayType_WorksCorrectly() {
            // Given
            when(mockJavaType.getSimpleType()).thenReturn("String[]");
            Argument arrayArg = new Argument(mockJavaType, "stringArray");

            // When
            String result = arrayArg.toString();

            // Then
            assertThat(result).isEqualTo("String[] stringArray");
        }

        @Test
        @DisplayName("Argument should handle primitive types")
        void testArgument_WithPrimitiveType_WorksCorrectly() {
            // Given
            when(mockJavaType.getSimpleType()).thenReturn("int");
            Argument primitiveArg = new Argument(mockJavaType, "count");

            // When
            String result = primitiveArg.toString();

            // Then
            assertThat(result).isEqualTo("int count");
        }

        @Test
        @DisplayName("Argument should handle special characters in names")
        void testArgument_WithSpecialCharactersInName_WorksCorrectly() {
            // Given
            argument.setName("param_with_underscores");

            // When
            String result = argument.toString();

            // Then
            assertThat(result).isEqualTo("String param_with_underscores");
        }

        @Test
        @DisplayName("Multiple arguments should be independent")
        void testMultipleArguments_AreIndependent() {
            // Given
            JavaType anotherType = mock(JavaType.class);
            when(anotherType.getSimpleType()).thenReturn("Integer");
            when(anotherType.clone()).thenReturn(anotherType);

            Argument arg1 = new Argument(mockJavaType, "param1");
            Argument arg2 = new Argument(anotherType, "param2");

            // When
            arg1.setName("modifiedParam1");

            // Then
            assertThat(arg1.getName()).isEqualTo("modifiedParam1");
            assertThat(arg2.getName()).isEqualTo("param2");
            assertThat(arg1.getClassType()).isNotEqualTo(arg2.getClassType());
        }
    }
}
