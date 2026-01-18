package org.specs.generators.java.members;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.specs.generators.java.enums.JDocTag;
import org.specs.generators.java.enums.Modifier;
import org.specs.generators.java.enums.Privacy;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.types.JavaTypeFactory;
import org.specs.generators.java.types.Primitive;

import pt.up.fe.specs.util.SpecsStrings;

/**
 * Comprehensive Phase 3 test class for {@link Method}.
 * Tests method creation, signature management, modifier handling, JavaDoc
 * integration, code generation, and various method configurations.
 * 
 * @author Generated Tests
 */
@DisplayName("Method Tests - Phase 3 Enhanced")
public class MethodTest {

    private Method method;
    private JavaType intType;
    private JavaType stringType;
    private JavaType voidType;

    @BeforeEach
    void setUp() {
        intType = JavaTypeFactory.getPrimitiveType(Primitive.INT);
        stringType = JavaTypeFactory.getStringType();
        voidType = JavaTypeFactory.getPrimitiveType(Primitive.VOID);
        method = new Method(intType, "testMethod");
    }

    @Nested
    @DisplayName("Method Creation Tests")
    class MethodCreationTests {

        @Test
        @DisplayName("Constructor should create method with correct type and name")
        void testConstructor_CreatesMethodCorrectly() {
            // When (method created in setUp)

            // Then
            assertThat(method.getName()).isEqualTo("testMethod");
            assertThat(method.getReturnType()).isEqualTo(intType);
            assertThat(method.getParams()).isNotNull().isEmpty();
            assertThat(method.getModifiers()).isNotNull().isEmpty();
            assertThat(method.getPrivacy()).isEqualTo(Privacy.PUBLIC);
        }

        @Test
        @DisplayName("Constructor should handle null return type")
        void testConstructor_WithNullReturnType_RejectsNull() {
            // When/Then
            assertThatThrownBy(() -> new Method(null, "methodName"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Constructor should handle null name")
        void testConstructor_WithNullName_AcceptsNull() {
            // When/Then
            assertThatCode(() -> new Method(intType, null))
                    .doesNotThrowAnyException();

            Method nullNameMethod = new Method(intType, null);
            assertThat(nullNameMethod.getName()).isNull();
        }

        @Test
        @DisplayName("Constructor should handle various return types")
        void testConstructor_WithVariousReturnTypes_HandlesCorrectly() {
            // Void method
            Method voidMethod = new Method(voidType, "voidMethod");
            assertThat(voidMethod.getReturnType()).isEqualTo(voidType);

            // String method
            Method stringMethod = new Method(stringType, "stringMethod");
            assertThat(stringMethod.getReturnType()).isEqualTo(stringType);

            // Array type - using JavaType constructor
            JavaType arrayType = new JavaType(int[][].class);
            Method arrayMethod = new Method(arrayType, "arrayMethod");
            assertThat(arrayMethod.getReturnType()).isEqualTo(arrayType);
        }
    }

    @Nested
    @DisplayName("Property Management Tests")
    class PropertyManagementTests {

        @Test
        @DisplayName("getName() should return method name")
        void testGetName_ReturnsMethodName() {
            // When
            String name = method.getName();

            // Then
            assertThat(name).isEqualTo("testMethod");
        }

        @Test
        @DisplayName("setName() should update method name")
        void testSetName_UpdatesMethodName() {
            // When
            method.setName("newMethodName");

            // Then
            assertThat(method.getName()).isEqualTo("newMethodName");
        }

        @Test
        @DisplayName("getReturnType() should return return type")
        void testGetReturnType_ReturnsReturnType() {
            // When
            JavaType returnType = method.getReturnType();

            // Then
            assertThat(returnType).isEqualTo(intType);
        }

        @Test
        @DisplayName("setReturnType() should update return type")
        void testSetReturnType_UpdatesReturnType() {
            // When
            method.setReturnType(stringType);

            // Then
            assertThat(method.getReturnType()).isEqualTo(stringType);
        }

        @Test
        @DisplayName("getPrivacy() should return privacy level")
        void testGetPrivacy_ReturnsPrivacyLevel() {
            // When
            Privacy privacy = method.getPrivacy();

            // Then
            assertThat(privacy).isEqualTo(Privacy.PUBLIC);
        }

        @Test
        @DisplayName("setPrivacy() should update privacy level")
        void testSetPrivacy_UpdatesPrivacyLevel() {
            // When
            method.setPrivacy(Privacy.PRIVATE);

            // Then
            assertThat(method.getPrivacy()).isEqualTo(Privacy.PRIVATE);
        }
    }

    @Nested
    @DisplayName("Parameter Management Tests")
    class ParameterManagementTests {

        @Test
        @DisplayName("addArgument() should add argument to method")
        void testAddArgument_AddsArgumentToMethod() {
            // When
            method.addArgument(stringType, "arg1");

            // Then
            assertThat(method.getParams()).hasSize(1);
            assertThat(method.getParams().get(0).getClassType()).isEqualTo(stringType);
            assertThat(method.getParams().get(0).getName()).isEqualTo("arg1");
        }

        @Test
        @DisplayName("addArgument() should add multiple arguments in order")
        void testAddArgument_AddsMultipleArgumentsInOrder() {
            // When
            method.addArgument(stringType, "arg1");
            method.addArgument(intType, "arg2");
            method.addArgument(voidType, "arg3");

            // Then
            assertThat(method.getParams()).hasSize(3);
            assertThat(method.getParams().get(0).getName()).isEqualTo("arg1");
            assertThat(method.getParams().get(1).getName()).isEqualTo("arg2");
            assertThat(method.getParams().get(2).getName()).isEqualTo("arg3");
        }

        @Test
        @DisplayName("addArgument() should handle null name")
        void testAddArgument_WithNullName_AcceptsNull() {
            // When/Then
            assertThatCode(() -> method.addArgument(intType, null))
                    .doesNotThrowAnyException();

            assertThat(method.getParams()).hasSize(1);
            assertThat(method.getParams().get(0).getName()).isNull();
        }

        @Test
        @DisplayName("addArgument() should handle null type")
        void testAddArgument_WithNullType_AcceptsNull() {
            // When/Then
            assertThatCode(() -> method.addArgument((JavaType) null, "arg"))
                    .doesNotThrowAnyException();

            assertThat(method.getParams()).hasSize(1);
            assertThat(method.getParams().get(0).getClassType()).isNull();
        }

        @Test
        @DisplayName("getParams() should return modifiable list")
        void testGetParams_ReturnsModifiableList() {
            // Given
            method.addArgument(intType, "arg1");

            // When
            var params = method.getParams();
            params.add(new Argument(stringType, "arg2"));

            // Then
            assertThat(method.getParams()).hasSize(2);
            assertThat(method.getParams().get(1).getName()).isEqualTo("arg2");
        }
    }

    @Nested
    @DisplayName("Modifier Management Tests")
    class ModifierManagementTests {

        @Test
        @DisplayName("add(Modifier) should add modifier")
        void testAddModifier_AddsModifier() {
            // When
            method.add(Modifier.STATIC);

            // Then
            assertThat(method.getModifiers()).contains(Modifier.STATIC);
        }

        @Test
        @DisplayName("add(Modifier) should add multiple modifiers")
        void testAddModifier_AddsMultipleModifiers() {
            // When
            method.add(Modifier.STATIC);
            method.add(Modifier.FINAL);

            // Then
            assertThat(method.getModifiers())
                    .hasSize(2)
                    .contains(Modifier.STATIC, Modifier.FINAL);
        }

        @Test
        @DisplayName("add(Modifier) should handle null modifier")
        void testAddModifier_WithNull_AcceptsNull() {
            // When/Then
            assertThatCode(() -> method.add((Modifier) null))
                    .doesNotThrowAnyException();

            assertThat(method.getModifiers()).contains((Modifier) null);
        }

        @Test
        @DisplayName("add(Modifier) should handle/deduplicate duplicate modifiers")
        void testAddModifier_HandleDuplicates() {
            // When
            method.add(Modifier.STATIC);
            method.add(Modifier.STATIC);

            // Then
            assertThat(method.getModifiers()).hasSize(1);
            assertThat(method.getModifiers()).allMatch(modifier -> modifier == Modifier.STATIC);
        }

        @Test
        @DisplayName("getModifiers() should return modifiable list")
        void testGetModifiers_ReturnsModifiableList() {
            // Given
            method.add(Modifier.STATIC);

            // When
            var modifiers = method.getModifiers();
            modifiers.add(Modifier.FINAL);

            // Then
            assertThat(method.getModifiers()).hasSize(2);
            assertThat(method.getModifiers()).contains(Modifier.FINAL);
        }
    }

    @Nested
    @DisplayName("Method Body Management Tests")
    class MethodBodyManagementTests {

        @Test
        @DisplayName("appendCode() should add code to method body")
        void testAppendCode_AddsCodeToMethodBody() {
            // When
            method.appendCode("int result = a + b;");
            method.appendCode("return result;");

            // Then
            String code = method.generateCode(0).toString();
            assertThat(code).contains("int result = a + b;");
            assertThat(code).contains("return result;");
        }

        @Test
        @DisplayName("setMethodBody() should set custom method body")
        void testSetMethodBody_SetsCustomMethodBody() {
            // When
            method.setMethodBody(new StringBuffer("return 42;"));

            // Then
            String code = method.generateCode(0).toString();
            assertThat(code).contains("return 42;");
            assertThat(code).doesNotContain("// TODO Auto-generated method stub");
        }

        @Test
        @DisplayName("getMethodBody() should return current method body")
        void testGetMethodBody_ReturnsCurrentMethodBody() {
            // Given
            method.appendCode("custom code");

            // When
            StringBuffer body = method.getMethodBody();

            // Then
            assertThat(body.toString()).contains("custom code");
        }
    }

    @Nested
    @DisplayName("JavaDoc Management Tests")
    class JavaDocManagementTests {

        @Test
        @DisplayName("getJavaDocComment() should return default JavaDoc")
        void testGetJavaDocComment_WithoutSetting_ReturnsDefault() {
            // When
            JavaDoc javaDoc = method.getJavaDocComment();

            // Then
            assertThat(javaDoc).isNotNull();
            assertThat(javaDoc.getComment().toString()).isEmpty();
        }

        @Test
        @DisplayName("setJavaDocComment() should accept null")
        void testSetJavaDocComment_AcceptsNull() {
            // When/Then
            assertThatCode(() -> method.setJavaDocComment(null))
                    .doesNotThrowAnyException();

            assertThat(method.getJavaDocComment()).isNull();
        }
    }

    @Nested
    @DisplayName("Code Generation Tests")
    class CodeGenerationTests {

        @Test
        @DisplayName("generateCode() should generate simple method")
        void testGenerateCode_SimpleMethod_GeneratesCorrectly() {
            // When
            String code = method.generateCode(0).toString();

            // Then
            String normalized = SpecsStrings.normalizeFileContents(code, true);
            assertThat(normalized).contains("public int testMethod()");
            assertThat(normalized).contains("// TODO Auto-generated method stub");
            assertThat(normalized).contains("return 0;");
        }

        @Test
        @DisplayName("generateCode() should generate method with arguments")
        void testGenerateCode_WithArguments_GeneratesCorrectly() {
            // Given
            method.addArgument(intType, "a");
            method.addArgument(intType, "b");

            // When
            String code = method.generateCode(0).toString();

            // Then
            String normalized = SpecsStrings.normalizeFileContents(code, true);
            assertThat(normalized).contains("public int testMethod(int a, int b)");
        }

        @Test
        @DisplayName("generateCode() should generate method with modifiers")
        void testGenerateCode_WithModifiers_GeneratesCorrectly() {
            // Given
            method.add(Modifier.STATIC);
            method.add(Modifier.FINAL);

            // When
            String code = method.generateCode(0).toString();

            // Then
            String normalized = SpecsStrings.normalizeFileContents(code, true);
            assertThat(normalized).contains("public static final int testMethod()");
        }

        @Test
        @DisplayName("generateCode() should generate method with different privacy")
        void testGenerateCode_WithDifferentPrivacy_GeneratesCorrectly() {
            // Given
            method.setPrivacy(Privacy.PRIVATE);

            // When
            String code = method.generateCode(0).toString();

            // Then
            String normalized = SpecsStrings.normalizeFileContents(code, true);
            assertThat(normalized).contains("private int testMethod()");
        }

        @Test
        @DisplayName("generateCode() should generate abstract method")
        void testGenerateCode_WithAbstractModifier_GeneratesAbstractMethod() {
            // Given
            method.add(Modifier.ABSTRACT);

            // When
            String code = method.generateCode(0).toString();

            // Then
            String normalized = SpecsStrings.normalizeFileContents(code, true);
            assertThat(normalized).contains("public abstract int testMethod();");
            assertThat(normalized).doesNotContain("// TODO Auto-generated method stub");
        }

        @Test
        @DisplayName("generateCode() should generate static method")
        void testGenerateCode_WithStaticModifier_GeneratesStaticMethod() {
            // Given
            method.add(Modifier.STATIC);

            // When
            String code = method.generateCode(0).toString();

            // Then
            String normalized = SpecsStrings.normalizeFileContents(code, true);
            assertThat(normalized).contains("public static int testMethod()");
        }

        @Test
        @DisplayName("generateCode() should apply correct indentation")
        void testGenerateCode_WithIndentation_AppliesCorrectly() {
            // When
            String code = method.generateCode(2).toString();

            // Then
            String[] lines = code.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    assertThat(line).startsWith("        "); // 8 spaces for 2 levels
                }
            }
        }

        @Test
        @DisplayName("generateCode() should include JavaDoc")
        void testGenerateCode_WithJavaDoc_IncludesJavaDoc() {
            // Given
            JavaDoc javaDoc = new JavaDoc("Test method description");
            javaDoc.addTag(JDocTag.RETURN, "test result");
            method.setJavaDocComment(javaDoc);

            // When
            String code = method.generateCode(0).toString();

            // Then
            assertThat(code).contains("Test method description");
            assertThat(code).contains("@return test result");
        }

        @Test
        @DisplayName("generateCode() should include custom method body")
        void testGenerateCode_WithCustomBody_IncludesCustomBody() {
            // Given
            method.setMethodBody(new StringBuffer("return a * b;"));

            // When
            String code = method.generateCode(0).toString();

            // Then
            assertThat(code).contains("return a * b;");
            assertThat(code).doesNotContain("// TODO Auto-generated method stub");
        }

        @Test
        @DisplayName("generateCode() should handle void return type")
        void testGenerateCode_WithVoidReturnType_GeneratesVoidMethod() {
            // Given
            method.setReturnType(voidType);

            // When
            String code = method.generateCode(0).toString();

            // Then
            String normalized = SpecsStrings.normalizeFileContents(code, true);
            assertThat(normalized).contains("public void testMethod()");
            assertThat(normalized).doesNotContain("return");
        }

        @Test
        @DisplayName("generateCode() should handle null return type")
        void testGenerateCode_WithNullReturnType_HandlesGracefully() {
            assertThatThrownBy(() -> method.setReturnType(null)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Legacy Compatibility Tests")
    class LegacyCompatibilityTests {

        @Test
        @DisplayName("testGenerateCode() - Legacy test compatibility")
        void testGenerateCode_LegacyCompatibility() {
            // Given - Recreate original test scenario
            final JavaType intType = JavaTypeFactory.getPrimitiveType(Primitive.INT);
            final Method m = new Method(intType, "max");
            m.addArgument(intType, "a");
            m.addArgument(intType, "b");

            final String returnStr = "/**\n" +
                    " * \n" +
                    " */\n" +
                    "public int max(int a, int b) {\n" +
                    "    // TODO Auto-generated method stub\n" +
                    "    return 0;\n" +
                    "}";

            // When/Then
            assertThat(SpecsStrings.normalizeFileContents(returnStr, true))
                    .isEqualTo(SpecsStrings.normalizeFileContents(m.generateCode(0).toString(), true));

            // Test abstract modifier
            m.add(Modifier.ABSTRACT);
            assertThat(SpecsStrings
                    .normalizeFileContents("/**\n" + " * \n" + " */\n" + "public abstract int max(int a, int b);",
                            true))
                    .isEqualTo(SpecsStrings.normalizeFileContents(m.generateCode(0).toString(), true));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Integration Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Method should handle very long names")
        void testMethod_WithVeryLongName_HandlesCorrectly() {
            // Given
            String longName = "veryLongMethodName".repeat(5);
            method.setName(longName);

            // When
            String code = method.generateCode(0).toString();

            // Then
            assertThat(code).contains(longName);
        }

        @Test
        @DisplayName("Method should handle many arguments")
        void testMethod_WithManyArguments_HandlesCorrectly() {
            // Given
            for (int i = 0; i < 10; i++) {
                method.addArgument(intType, "arg" + i);
            }

            // When
            String code = method.generateCode(0).toString();

            // Then
            assertThat(code).contains("arg0");
            assertThat(code).contains("arg9");
            assertThat(method.getParams()).hasSize(10);
        }

        @Test
        @DisplayName("Method should handle complex method bodies")
        void testMethod_WithComplexMethodBody_HandlesCorrectly() {
            // Given
            method.appendCode("if (condition) {");
            method.appendCode("    return computeValue();");
            method.appendCode("} else {");
            method.appendCode("    throw new IllegalArgumentException(\"Invalid input\");");
            method.appendCode("}");

            // When
            String code = method.generateCode(0).toString();

            // Then
            assertThat(code).contains("if (condition)");
            assertThat(code).contains("computeValue()");
            assertThat(code).contains("IllegalArgumentException");
        }

        @Test
        @DisplayName("Multiple Methods should be independent")
        void testMultipleMethods_AreIndependent() {
            // Given
            Method method1 = new Method(intType, "method1");
            Method method2 = new Method(stringType, "method2");

            // When
            method1.add(Modifier.STATIC);
            method1.addArgument(intType, "arg1");
            method2.setPrivacy(Privacy.PRIVATE);
            method2.addArgument(stringType, "arg2");

            // Then
            assertThat(method1.getModifiers()).contains(Modifier.STATIC);
            assertThat(method2.getModifiers()).doesNotContain(Modifier.STATIC);
            assertThat(method1.getPrivacy()).isEqualTo(Privacy.PUBLIC);
            assertThat(method2.getPrivacy()).isEqualTo(Privacy.PRIVATE);
        }

        @Test
        @DisplayName("Method should handle all modifier combinations")
        void testMethod_WithAllModifierCombinations_HandlesCorrectly() {
            // Test various modifier combinations
            Method staticFinalMethod = new Method(intType, "staticFinal");
            staticFinalMethod.add(Modifier.STATIC);
            staticFinalMethod.add(Modifier.FINAL);

            Method abstractMethod = new Method(voidType, "abstractMethod");
            abstractMethod.add(Modifier.ABSTRACT);

            // Verify generated code
            String staticFinalCode = staticFinalMethod.generateCode(0).toString();
            String abstractCode = abstractMethod.generateCode(0).toString();

            assertThat(staticFinalCode).contains("static final");
            assertThat(abstractCode).contains("abstract");
            assertThat(abstractCode).endsWith(");"); // Abstract methods end with semicolon
        }

        @Test
        @DisplayName("toString() should return generated code")
        void testToString_ReturnsGeneratedCode() {
            // When
            String toString = method.toString();
            String generateCode = method.generateCode(0).toString();

            // Then
            assertThat(toString).isEqualTo(generateCode);
        }
    }
}
