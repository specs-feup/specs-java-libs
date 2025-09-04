package org.specs.generators.java.classtypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.specs.generators.java.members.Field;
import org.specs.generators.java.members.Method;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.types.JavaTypeFactory;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for the {@link Interface} class.
 * Tests Java interface generation functionality and member management.
 * 
 * @author Generated Tests
 */
@DisplayName("Interface Tests")
class InterfaceTest {

    private Interface javaInterface;

    @BeforeEach
    void setUp() {
        javaInterface = new Interface("TestInterface", "com.example");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create Interface with name and package")
        void shouldCreateInterfaceWithNameAndPackage() {
            Interface newInterface = new Interface("MyInterface", "com.test");

            assertThat(newInterface.getName()).isEqualTo("MyInterface");
            assertThat(newInterface.getClassPackage()).isEqualTo("com.test");
        }

        @Test
        @DisplayName("Should create Interface with empty package")
        void shouldCreateInterfaceWithEmptyPackage() {
            Interface defaultInterface = new Interface("DefaultInterface", "");

            assertThat(defaultInterface.getName()).isEqualTo("DefaultInterface");
            assertThat(defaultInterface.getClassPackage()).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend ClassType")
        void shouldExtendClassType() {
            assertThat(javaInterface).isInstanceOf(ClassType.class);
        }
    }

    @Nested
    @DisplayName("Interface Extension Tests")
    class InterfaceExtensionTests {

        @Test
        @DisplayName("Should add parent interface")
        void shouldAddParentInterface() {
            JavaType parentInterface = new JavaType("Serializable", "java.io");

            boolean added = javaInterface.addInterface(parentInterface);

            assertThat(added).isTrue();
            // Interface class doesn't expose getInterfaces() - verify through generated
            // code
            StringBuilder result = javaInterface.generateCode(0);
            assertThat(result.toString()).contains("extends Serializable");
        }

        @Test
        @DisplayName("Should not add duplicate parent interface")
        void shouldNotAddDuplicateParentInterface() {
            JavaType parentInterface = new JavaType("Serializable", "java.io");
            javaInterface.addInterface(parentInterface);

            boolean addedAgain = javaInterface.addInterface(parentInterface);

            assertThat(addedAgain).isFalse();
        }

        @Test
        @DisplayName("Should remove parent interface by string name")
        void shouldRemoveParentInterfaceByString() {
            JavaType parentInterface = new JavaType("Serializable", "java.io");

            boolean added = javaInterface.addInterface(parentInterface);
            assertThat(added).isTrue();

            boolean removed = javaInterface.removeInterface("Serializable");
            assertThat(removed).isTrue();

            // Verify the interface was removed
            StringBuilder result = javaInterface.generateCode(0);
            assertThat(result.toString()).doesNotContain("extends Serializable");
        }

        @Test
        @DisplayName("Should not remove non-existent parent interface")
        void shouldNotRemoveNonExistentParentInterface() {
            boolean removed = javaInterface.removeInterface("NonExistent");

            assertThat(removed).isFalse();
        }

        @Test
        @DisplayName("Should add multiple parent interfaces")
        void shouldAddMultipleParentInterfaces() {
            JavaType serializable = new JavaType("Serializable", "java.io");
            JavaType cloneable = new JavaType("Cloneable", "java.lang");

            javaInterface.addInterface(serializable);
            javaInterface.addInterface(cloneable);

            // Verify through generated code
            StringBuilder result = javaInterface.generateCode(0);
            String generatedCode = result.toString();
            assertThat(generatedCode).contains("extends");
            assertThat(generatedCode).contains("Serializable");
            assertThat(generatedCode).contains("Cloneable");
        }
    }

    @Nested
    @DisplayName("Field Management Tests")
    class FieldManagementTests {

        @Test
        @DisplayName("Should add constant field")
        void shouldAddConstantField() {
            Field field = new Field(JavaTypeFactory.getStringType(), "CONSTANT");

            boolean added = javaInterface.addField(field);

            assertThat(added).isTrue();
            // Interface class doesn't expose getFields() - verify through generated code
            StringBuilder result = javaInterface.generateCode(0);
            assertThat(result.toString()).contains("CONSTANT");
        }

        @Test
        @DisplayName("Should not add duplicate field")
        void shouldNotAddDuplicateField() {
            Field field = new Field(JavaTypeFactory.getStringType(), "CONSTANT");
            javaInterface.addField(field);

            boolean addedAgain = javaInterface.addField(field);

            assertThat(addedAgain).isFalse();
        }

        @Test
        @DisplayName("Should remove field")
        void shouldRemoveField() {
            Field field = new Field(JavaTypeFactory.getStringType(), "CONSTANT");
            javaInterface.addField(field);

            boolean removed = javaInterface.removeField(field);

            assertThat(removed).isTrue();
            // Verify removal through generated code
            StringBuilder result = javaInterface.generateCode(0);
            assertThat(result.toString()).doesNotContain("CONSTANT");
        }

        @Test
        @DisplayName("Should not remove non-existent field")
        void shouldNotRemoveNonExistentField() {
            Field field = new Field(JavaTypeFactory.getStringType(), "CONSTANT");

            boolean removed = javaInterface.removeField(field);

            assertThat(removed).isFalse();
        }

        @Test
        @DisplayName("Should add multiple fields")
        void shouldAddMultipleFields() {
            Field stringConstant = new Field(JavaTypeFactory.getStringType(), "STRING_CONSTANT");
            Field intConstant = new Field(JavaTypeFactory.getIntType(), "INT_CONSTANT");

            javaInterface.addField(stringConstant);
            javaInterface.addField(intConstant);

            // Verify through generated code
            StringBuilder result = javaInterface.generateCode(0);
            String generatedCode = result.toString();
            assertThat(generatedCode).contains("STRING_CONSTANT");
            assertThat(generatedCode).contains("INT_CONSTANT");
        }

        @Test
        @DisplayName("Should handle empty fields list")
        void shouldHandleEmptyFieldsList() {
            // Empty interface should generate code without fields
            StringBuilder result = javaInterface.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("//Fields");
            assertThat(generatedCode).contains("public interface TestInterface");
        }
    }

    @Nested
    @DisplayName("Method Management Tests")
    class MethodManagementTests {

        @Test
        @DisplayName("Should add abstract method")
        void shouldAddAbstractMethod() {
            Method method = new Method(JavaTypeFactory.getVoidType(), "testMethod");

            boolean added = javaInterface.addMethod(method);

            assertThat(added).isTrue();
            // Interface class doesn't expose getMethods() - verify through generated code
            StringBuilder result = javaInterface.generateCode(0);
            assertThat(result.toString()).contains("testMethod");
        }

        @Test
        @DisplayName("Should not add duplicate method")
        void shouldNotAddDuplicateMethod() {
            Method method = new Method(JavaTypeFactory.getVoidType(), "testMethod");
            javaInterface.addMethod(method);

            boolean addedAgain = javaInterface.addMethod(method);

            assertThat(addedAgain).isFalse();
        }

        @Test
        @DisplayName("Should remove method")
        void shouldRemoveMethod() {
            Method method = new Method(JavaTypeFactory.getVoidType(), "testMethod");
            javaInterface.addMethod(method);

            boolean removed = javaInterface.removeMethod(method);

            assertThat(removed).isTrue();
            // Verify removal through generated code
            StringBuilder result = javaInterface.generateCode(0);
            assertThat(result.toString()).doesNotContain("testMethod");
        }

        @Test
        @DisplayName("Should not remove non-existent method")
        void shouldNotRemoveNonExistentMethod() {
            Method method = new Method(JavaTypeFactory.getVoidType(), "testMethod");

            boolean removed = javaInterface.removeMethod(method);

            assertThat(removed).isFalse();
        }

        @Test
        @DisplayName("Should add multiple methods")
        void shouldAddMultipleMethods() {
            Method voidMethod = new Method(JavaTypeFactory.getVoidType(), "voidMethod");
            Method stringMethod = new Method(JavaTypeFactory.getStringType(), "stringMethod");

            javaInterface.addMethod(voidMethod);
            javaInterface.addMethod(stringMethod);

            // Verify through generated code
            StringBuilder result = javaInterface.generateCode(0);
            String generatedCode = result.toString();
            assertThat(generatedCode).contains("voidMethod");
            assertThat(generatedCode).contains("stringMethod");
        }

        @Test
        @DisplayName("Should handle empty methods list")
        void shouldHandleEmptyMethodsList() {
            // Empty interface should generate code without methods
            StringBuilder result = javaInterface.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("//Methods");
            assertThat(generatedCode).contains("public interface TestInterface");
        }
    }

    @Nested
    @DisplayName("Code Generation Tests")
    class CodeGenerationTests {

        @Test
        @DisplayName("Should generate basic interface code")
        void shouldGenerateBasicInterfaceCode() {
            StringBuilder result = javaInterface.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("package com.example;");
            assertThat(generatedCode).contains("public interface TestInterface");
            assertThat(generatedCode).contains("}");
        }

        @Test
        @DisplayName("Should generate code with indentation")
        void shouldGenerateCodeWithIndentation() {
            StringBuilder result = javaInterface.generateCode(1);
            String generatedCode = result.toString();

            // Should have some indentation
            assertThat(generatedCode).contains("    ");
        }

        @Test
        @DisplayName("Should generate code with fields")
        void shouldGenerateCodeWithFields() {
            Field field = new Field(JavaTypeFactory.getStringType(), "CONSTANT");
            javaInterface.addField(field);

            StringBuilder result = javaInterface.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("CONSTANT");
        }

        @Test
        @DisplayName("Should generate code with methods")
        void shouldGenerateCodeWithMethods() {
            Method method = new Method(JavaTypeFactory.getVoidType(), "testMethod");
            javaInterface.addMethod(method);

            StringBuilder result = javaInterface.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("testMethod");
        }

        @Test
        @DisplayName("Should generate code with parent interfaces")
        void shouldGenerateCodeWithParentInterfaces() {
            JavaType parentInterface = new JavaType("Serializable", "java.io");
            javaInterface.addInterface(parentInterface);

            StringBuilder result = javaInterface.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("extends");
            assertThat(generatedCode).contains("Serializable");
        }

        @Test
        @DisplayName("Should generate method without indentation")
        void shouldGenerateWithoutIndentation() {
            // Interface class doesn't implement IGenerate.generate() - use generateCode(0)
            // instead
            StringBuilder result = javaInterface.generateCode(0);

            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("public interface TestInterface");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Complex Scenarios Tests")
    class EdgeCasesAndComplexScenariosTests {

        @Test
        @DisplayName("Should handle interface with all components")
        void shouldHandleInterfaceWithAllComponents() {
            // Add parent interface
            JavaType parentInterface = new JavaType("Serializable", "java.io");
            javaInterface.addInterface(parentInterface);

            // Add field
            Field field = new Field(JavaTypeFactory.getStringType(), "CONSTANT");
            javaInterface.addField(field);

            // Add method
            Method method = new Method(JavaTypeFactory.getStringType(), "getValue");
            javaInterface.addMethod(method);

            StringBuilder result = javaInterface.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("extends Serializable");
            assertThat(generatedCode).contains("CONSTANT");
            assertThat(generatedCode).contains("getValue");
        }

        @Test
        @DisplayName("Should handle interface with multiple parent interfaces")
        void shouldHandleInterfaceWithMultipleParentInterfaces() {
            JavaType serializable = new JavaType("Serializable", "java.io");
            JavaType cloneable = new JavaType("Cloneable", "java.lang");

            javaInterface.addInterface(serializable);
            javaInterface.addInterface(cloneable);

            StringBuilder result = javaInterface.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("extends");
            assertThat(generatedCode).contains("Serializable");
            assertThat(generatedCode).contains("Cloneable");
        }

        @Test
        @DisplayName("Should handle empty interface")
        void shouldHandleEmptyInterface() {
            StringBuilder result = javaInterface.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("public interface TestInterface");
            assertThat(generatedCode).contains("}");
        }

        @Test
        @DisplayName("Should handle interface in default package")
        void shouldHandleInterfaceInDefaultPackage() {
            Interface defaultInterface = new Interface("DefaultInterface", "");

            StringBuilder result = defaultInterface.generateCode(0);
            String generatedCode = result.toString();

            // Should not contain package statement for empty package
            assertThat(generatedCode).doesNotContain("package ");
            assertThat(generatedCode).contains("public interface DefaultInterface");
        }
    }

    @Nested
    @DisplayName("Inheritance and Polymorphism Tests")
    class InheritanceAndPolymorphismTests {

        @Test
        @DisplayName("Should inherit from ClassType")
        void shouldInheritFromClassType() {
            assertThat(javaInterface).isInstanceOf(ClassType.class);
        }

        @Test
        @DisplayName("Should support polymorphic usage as ClassType")
        void shouldSupportPolymorphicUsageAsClassType() {
            ClassType classType = javaInterface;

            assertThat(classType.getName()).isEqualTo("TestInterface");
            assertThat(classType.getClassPackage()).isEqualTo("com.example");
        }

        @Test
        @DisplayName("Should generate code through ClassType interface")
        void shouldGenerateCodeThroughClassTypeInterface() {
            ClassType classType = javaInterface;

            StringBuilder result = classType.generateCode(0);

            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("public interface TestInterface");
        }
    }
}
