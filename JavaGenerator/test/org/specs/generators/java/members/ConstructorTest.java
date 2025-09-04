package org.specs.generators.java.members;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.specs.generators.java.classtypes.JavaClass;
import org.specs.generators.java.classtypes.JavaEnum;
import org.specs.generators.java.enums.JDocTag;
import org.specs.generators.java.enums.Privacy;
import org.specs.generators.java.types.JavaType;

/**
 * Test class for {@link Constructor} - Constructor generation functionality.
 * Tests comprehensive constructor creation with various privacy levels,
 * arguments, JavaDoc comments, and code generation patterns.
 * 
 * @author Generated Tests
 */
@DisplayName("Constructor Tests")
public class ConstructorTest {

    private JavaClass mockJavaClass;
    private JavaEnum mockJavaEnum;
    private JavaType mockJavaType;

    @BeforeEach
    void setUp() {
        mockJavaClass = mock(JavaClass.class);
        mockJavaEnum = mock(JavaEnum.class);
        mockJavaType = mock(JavaType.class);

        when(mockJavaClass.getName()).thenReturn("TestClass");
        when(mockJavaEnum.getName()).thenReturn("TestEnum");
        when(mockJavaType.getSimpleType()).thenReturn("String");
    }

    @Nested
    @DisplayName("Constructor Creation Tests")
    class ConstructorCreationTests {

        @Test
        @DisplayName("Constructor(JavaClass) should create public constructor with default settings")
        void testConstructor_WithJavaClass_CreatesPublicConstructor() {
            // When
            Constructor constructor = new Constructor(mockJavaClass);

            // Then
            assertThat(constructor.getPrivacy()).isEqualTo(Privacy.PUBLIC);
            assertThat(constructor.getJavaClass()).isEqualTo(mockJavaClass);
            assertThat(constructor.getArguments()).isNotNull().isEmpty();
            assertThat(constructor.getMethodBody()).isNotNull().hasToString("");

            // Verify the class was notified about the constructor
            verify(mockJavaClass, atLeastOnce()).add(constructor);
        }

        @Test
        @DisplayName("Constructor(JavaEnum) should create private constructor for enum")
        void testConstructor_WithJavaEnum_CreatesPrivateConstructor() {
            // When
            Constructor constructor = new Constructor(mockJavaEnum);

            // Then
            assertThat(constructor.getPrivacy()).isEqualTo(Privacy.PRIVATE);
            assertThat(constructor.getArguments()).isNotNull().isEmpty();
            assertThat(constructor.getMethodBody()).isNotNull().hasToString("");
        }

        @Test
        @DisplayName("Constructor(Privacy, JavaClass) should create constructor with specified privacy")
        void testConstructor_WithPrivacyAndJavaClass_CreatesCorrectPrivacy() {
            // When
            Constructor constructor = new Constructor(Privacy.PROTECTED, mockJavaClass);

            // Then
            assertThat(constructor.getPrivacy()).isEqualTo(Privacy.PROTECTED);
            assertThat(constructor.getJavaClass()).isEqualTo(mockJavaClass);
            verify(mockJavaClass, atLeastOnce()).add(constructor);
        }

        @Test
        @DisplayName("Constructor should throw IAE when JavaClass is null")
        void testConstructor_WithNullJavaClass_ThrowsIAE() {
            // When/Then
            assertThatThrownBy(() -> new Constructor((JavaClass) null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Java class");
        }
    }

    @Nested
    @DisplayName("Argument Management Tests")
    class ArgumentManagementTests {

        private Constructor constructor;

        @BeforeEach
        void setUp() {
            constructor = new Constructor(mockJavaClass);
        }

        @Test
        @DisplayName("addArgument(JavaType, String) should add argument correctly")
        void testAddArgument_WithTypeAndName_AddsArgument() {
            // When
            constructor.addArgument(mockJavaType, "testParam");

            // Then
            assertThat(constructor.getArguments())
                    .hasSize(1)
                    .first()
                    .satisfies(arg -> {
                        assertThat(arg.getClassType()).isEqualTo(mockJavaType);
                        assertThat(arg.getName()).isEqualTo("testParam");
                    });
        }

        @Test
        @DisplayName("addArgument(Field) should add field as argument")
        void testAddArgument_WithField_AddsFieldAsArgument() {
            // Given
            Field mockField = mock(Field.class);
            when(mockField.getType()).thenReturn(mockJavaType);
            when(mockField.getName()).thenReturn("fieldName");

            // When
            constructor.addArgument(mockField);

            // Then
            assertThat(constructor.getArguments())
                    .hasSize(1)
                    .first()
                    .satisfies(arg -> {
                        assertThat(arg.getClassType()).isEqualTo(mockJavaType);
                        assertThat(arg.getName()).isEqualTo("fieldName");
                    });
        }

        @Test
        @DisplayName("addArguments(Collection<Field>) should add multiple fields as arguments")
        void testAddArguments_WithFieldCollection_AddsAllFields() {
            // Given
            Field field1 = mock(Field.class);
            Field field2 = mock(Field.class);
            JavaType type2 = mock(JavaType.class);

            when(field1.getType()).thenReturn(mockJavaType);
            when(field1.getName()).thenReturn("field1");
            when(field2.getType()).thenReturn(type2);
            when(field2.getName()).thenReturn("field2");
            when(type2.getSimpleType()).thenReturn("Integer");

            List<Field> fields = Arrays.asList(field1, field2);

            // When
            constructor.addArguments(fields);

            // Then
            assertThat(constructor.getArguments()).hasSize(2);
            assertThat(constructor.getArguments().get(0).getName()).isEqualTo("field1");
            assertThat(constructor.getArguments().get(1).getName()).isEqualTo("field2");
        }

        @Test
        @DisplayName("setArguments() should replace existing arguments")
        void testSetArguments_ReplacesExistingArguments() {
            // Given
            constructor.addArgument(mockJavaType, "oldParam");

            List<Argument> newArguments = new ArrayList<>();
            newArguments.add(new Argument(mockJavaType, "newParam"));

            // When
            constructor.setArguments(newArguments);

            // Then
            assertThat(constructor.getArguments())
                    .hasSize(1)
                    .first()
                    .satisfies(arg -> assertThat(arg.getName()).isEqualTo("newParam"));
        }
    }

    @Nested
    @DisplayName("Code Generation Tests")
    class CodeGenerationTests {

        private Constructor constructor;

        @BeforeEach
        void setUp() {
            constructor = new Constructor(mockJavaClass);
        }

        @Test
        @DisplayName("generateCode() should generate correct constructor signature")
        void testGenerateCode_GeneratesCorrectSignature() {
            // When
            String code = constructor.generateCode(0).toString();

            // Then
            assertThat(code)
                    .contains("public TestClass()")
                    .contains("{}")
                    .contains("/**")
                    .contains("*/");
        }

        @Test
        @DisplayName("generateCode() should include arguments in signature")
        void testGenerateCode_WithArguments_IncludesInSignature() {
            // Given
            constructor.addArgument(mockJavaType, "param1");
            constructor.addArgument(mockJavaType, "param2");

            // When
            String code = constructor.generateCode(0).toString();

            // Then
            assertThat(code)
                    .contains("public TestClass(String param1, String param2)")
                    .contains("{}");
        }

        @Test
        @DisplayName("generateCode() should apply correct indentation")
        void testGenerateCode_WithIndentation_AppliesCorrectly() {
            // When
            String code = constructor.generateCode(1).toString();

            // Then
            assertThat(code).contains("    public TestClass()");
        }

        @Test
        @DisplayName("generateCode() should include method body when present")
        void testGenerateCode_WithMethodBody_IncludesBody() {
            // Given
            constructor.appendCode("this.field = value;");

            // When
            String code = constructor.generateCode(0).toString();

            // Then
            assertThat(code).contains("this.field = value;");
        }

        @Test
        @DisplayName("generateCode() for enum should use enum name")
        void testGenerateCode_ForEnum_UsesEnumName() {
            // Given
            Constructor enumConstructor = new Constructor(mockJavaEnum);

            // When
            String code = enumConstructor.generateCode(0).toString();

            // Then
            assertThat(code).contains("private TestEnum()");
        }
    }

    @Nested
    @DisplayName("Method Body Management Tests")
    class MethodBodyManagementTests {

        private Constructor constructor;

        @BeforeEach
        void setUp() {
            constructor = new Constructor(mockJavaClass);
        }

        @Test
        @DisplayName("appendCode(String) should add code to method body")
        void testAppendCode_WithString_AddsToBody() {
            // When
            constructor.appendCode("this.field = value;");

            // Then
            assertThat(constructor.getMethodBody().toString()).contains("this.field = value;");
        }

        @Test
        @DisplayName("appendCode(StringBuffer) should add buffer content to method body")
        void testAppendCode_WithStringBuffer_AddsToBody() {
            // Given
            StringBuffer buffer = new StringBuffer("this.field = value;");

            // When
            constructor.appendCode(buffer);

            // Then
            assertThat(constructor.getMethodBody().toString()).contains("this.field = value;");
        }

        @Test
        @DisplayName("appendDefaultCode(false) should generate assignment statements")
        void testAppendDefaultCode_WithoutSetters_GeneratesAssignments() {
            // Given
            constructor.addArgument(mockJavaType, "name");
            constructor.addArgument(mockJavaType, "value");

            // When
            constructor.appendDefaultCode(false);

            // Then
            String body = constructor.getMethodBody().toString();
            assertThat(body)
                    .contains("this.name = name;")
                    .contains("this.value = value;");
        }

        @Test
        @DisplayName("appendDefaultCode(true) should generate setter calls")
        void testAppendDefaultCode_WithSetters_GeneratesSetterCalls() {
            // Given
            constructor.addArgument(mockJavaType, "name");
            constructor.addArgument(mockJavaType, "value");

            // When
            constructor.appendDefaultCode(true);

            // Then
            String body = constructor.getMethodBody().toString();
            assertThat(body)
                    .contains("this.setName(name);")
                    .contains("this.setValue(value);");
        }

        @Test
        @DisplayName("clearCode() should remove all code from method body")
        void testClearCode_RemovesAllCode() {
            // Given
            constructor.appendCode("this.field = value;");
            assertThat(constructor.getMethodBody().toString()).isNotEmpty();

            // When
            constructor.clearCode();

            // Then
            assertThat(constructor.getMethodBody().toString()).isEmpty();
        }

        @Test
        @DisplayName("setMethodBody() should replace existing method body")
        void testSetMethodBody_ReplacesExistingBody() {
            // Given
            constructor.appendCode("old code");
            StringBuffer newBody = new StringBuffer("new code");

            // When
            constructor.setMethodBody(newBody);

            // Then
            assertThat(constructor.getMethodBody()).isSameAs(newBody);
            assertThat(constructor.getMethodBody().toString()).isEqualTo("new code");
        }
    }

    @Nested
    @DisplayName("JavaDoc Tests")
    class JavaDocTests {

        private Constructor constructor;

        @BeforeEach
        void setUp() {
            constructor = new Constructor(mockJavaClass);
        }

        @Test
        @DisplayName("appendComment() should add comment to JavaDoc")
        void testAppendComment_AddsToJavaDoc() {
            // When
            constructor.appendComment("This is a test comment");

            // Then
            String code = constructor.generateCode(0).toString();
            assertThat(code).contains("This is a test comment");
        }

        @Test
        @DisplayName("addJavaDocTag() should add tag without description")
        void testAddJavaDocTag_WithoutDescription_AddsTag() {
            // When
            constructor.addJavaDocTag(JDocTag.AUTHOR);

            // Then
            String code = constructor.generateCode(0).toString();
            assertThat(code).contains("@author");
        }

        @Test
        @DisplayName("addJavaDocTag() should add tag with description")
        void testAddJavaDocTag_WithDescription_AddsTagWithDescription() {
            // When
            constructor.addJavaDocTag(JDocTag.AUTHOR, "Test Author");

            // Then
            String code = constructor.generateCode(0).toString();
            assertThat(code)
                    .contains("@author")
                    .contains("Test Author");
        }
    }

    @Nested
    @DisplayName("Property Management Tests")
    class PropertyManagementTests {

        private Constructor constructor;

        @BeforeEach
        void setUp() {
            constructor = new Constructor(mockJavaClass);
        }

        @Test
        @DisplayName("setPrivacy() should change privacy level")
        void testSetPrivacy_ChangesPrivacyLevel() {
            // When
            constructor.setPrivacy(Privacy.PRIVATE);

            // Then
            assertThat(constructor.getPrivacy()).isEqualTo(Privacy.PRIVATE);
        }

        @Test
        @DisplayName("setJavaClass() should change associated Java class")
        void testSetJavaClass_ChangesJavaClass() {
            // Given
            JavaClass newMockClass = mock(JavaClass.class);

            // When
            constructor.setJavaClass(newMockClass);

            // Then
            assertThat(constructor.getJavaClass()).isEqualTo(newMockClass);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("toString() should return generated code")
        void testToString_ReturnsGeneratedCode() {
            // Given
            Constructor constructor = new Constructor(mockJavaClass);

            // When
            String toString = constructor.toString();
            String generateCode = constructor.generateCode(0).toString();

            // Then
            assertThat(toString).isEqualTo(generateCode);
        }

        @Test
        @DisplayName("Constructor should handle empty method body gracefully")
        void testConstructor_WithEmptyMethodBody_HandlesGracefully() {
            // Given
            Constructor constructor = new Constructor(mockJavaClass);

            // When
            String code = constructor.generateCode(0).toString();

            // Then
            assertThat(code)
                    .contains("public TestClass()")
                    .contains("{}");
        }

        @Test
        @DisplayName("Constructor should handle null arguments in addArgument")
        void testAddArgument_WithNullType_HandlesGracefully() {
            // Given
            Constructor constructor = new Constructor(mockJavaClass);

            // When/Then
            assertThatCode(() -> constructor.addArgument(null, "param"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("appendDefaultCode() should handle empty arguments gracefully")
        void testAppendDefaultCode_WithEmptyArguments_HandlesGracefully() {
            // Given
            Constructor constructor = new Constructor(mockJavaClass);

            // When/Then
            assertThatCode(() -> constructor.appendDefaultCode(false))
                    .doesNotThrowAnyException();

            assertThat(constructor.getMethodBody().toString()).isEmpty();
        }
    }
}
