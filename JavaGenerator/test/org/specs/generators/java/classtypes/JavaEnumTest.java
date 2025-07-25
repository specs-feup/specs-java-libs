package org.specs.generators.java.classtypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.specs.generators.java.members.Constructor;
import org.specs.generators.java.members.EnumItem;
import org.specs.generators.java.members.Field;
import org.specs.generators.java.members.Method;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.types.JavaTypeFactory;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for the {@link JavaEnum} class.
 * Tests Java enum generation functionality and member management.
 * 
 * @author Generated Tests
 */
@DisplayName("JavaEnum Tests")
class JavaEnumTest {

    private JavaEnum javaEnum;

    @BeforeEach
    void setUp() {
        javaEnum = new JavaEnum("TestEnum", "com.example");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create JavaEnum with name and package")
        void shouldCreateJavaEnumWithNameAndPackage() {
            JavaEnum newEnum = new JavaEnum("MyEnum", "com.test");

            assertThat(newEnum.getName()).isEqualTo("MyEnum");
            assertThat(newEnum.getClassPackage()).isEqualTo("com.test");
        }

        @Test
        @DisplayName("Should create JavaEnum with empty package")
        void shouldCreateJavaEnumWithEmptyPackage() {
            JavaEnum defaultEnum = new JavaEnum("DefaultEnum", "");

            assertThat(defaultEnum.getName()).isEqualTo("DefaultEnum");
            assertThat(defaultEnum.getClassPackage()).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend JavaClass")
        void shouldExtendJavaClass() {
            assertThat(javaEnum).isInstanceOf(JavaClass.class);
        }

        @Test
        @DisplayName("Should extend ClassType")
        void shouldExtendClassType() {
            assertThat(javaEnum).isInstanceOf(ClassType.class);
        }

        @Test
        @DisplayName("Should prevent setting superclass")
        void shouldPreventSettingSuperclass() {
            JavaType customSuperClass = new JavaType("CustomParent", "com.custom");

            assertThatThrownBy(() -> javaEnum.setSuperClass(customSuperClass))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("An enum cannot have a super class.");
        }

        @Test
        @DisplayName("Should prevent getting superclass")
        void shouldPreventGettingSuperclass() {
            assertThatThrownBy(() -> javaEnum.getSuperClass())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("An enum does not have a super class.");
        }
    }

    @Nested
    @DisplayName("Enum Items Management Tests")
    class EnumItemsManagementTests {

        @Test
        @DisplayName("Should add enum item by EnumItem object")
        void shouldAddEnumItemByObject() {
            EnumItem item = new EnumItem("ITEM_ONE");

            javaEnum.add(item);

            // Verify through generated code since JavaEnum doesn't expose getItems()
            StringBuilder result = javaEnum.generateCode(0);
            assertThat(result.toString()).contains("ITEM_ONE");
        }

        @Test
        @DisplayName("Should add enum item by string name")
        void shouldAddEnumItemByStringName() {
            javaEnum.addItem("ITEM_TWO");

            // Verify through generated code
            StringBuilder result = javaEnum.generateCode(0);
            assertThat(result.toString()).contains("ITEM_TWO");
        }

        @Test
        @DisplayName("Should add multiple enum items")
        void shouldAddMultipleEnumItems() {
            javaEnum.addItem("FIRST");
            javaEnum.addItem("SECOND");
            javaEnum.addItem("THIRD");

            // Verify through generated code
            StringBuilder result = javaEnum.generateCode(0);
            String generatedCode = result.toString();
            assertThat(generatedCode).contains("FIRST");
            assertThat(generatedCode).contains("SECOND");
            assertThat(generatedCode).contains("THIRD");
        }

        @Test
        @DisplayName("Should add enum item with parameters")
        void shouldAddEnumItemWithParameters() {
            EnumItem itemWithParams = new EnumItem("COMPLEX_ITEM");
            itemWithParams.addParameter("\"value1\"");
            itemWithParams.addParameter("42");

            javaEnum.add(itemWithParams);

            // Verify through generated code
            StringBuilder result = javaEnum.generateCode(0);
            String generatedCode = result.toString();
            assertThat(generatedCode).contains("COMPLEX_ITEM");
            assertThat(generatedCode).contains("value1");
            assertThat(generatedCode).contains("42");
        }
    }

    @Nested
    @DisplayName("Constructor Management Tests")
    class ConstructorManagementTests {

        @Test
        @DisplayName("Should inherit constructor management from JavaClass")
        void shouldInheritConstructorManagement() {
            // JavaEnum extends JavaClass, so it should inherit constructor functionality
            Constructor constructor = new Constructor(javaEnum);

            javaEnum.add(constructor);

            // Verify constructor is added (working around bug 2.1)
            assertThat(javaEnum.getConstructors()).containsExactly(constructor);
        }
    }

    @Nested
    @DisplayName("Field Management Tests")
    class FieldManagementTests {

        @Test
        @DisplayName("Should inherit field management from JavaClass")
        void shouldInheritFieldManagement() {
            // JavaEnum extends JavaClass, so it should inherit field functionality
            Field field = new Field(JavaTypeFactory.getStringType(), "description");

            boolean added = javaEnum.add(field);

            assertThat(added).isTrue();
            assertThat(javaEnum.getFields()).containsExactly(field);
        }
    }

    @Nested
    @DisplayName("Method Management Tests")
    class MethodManagementTests {

        @Test
        @DisplayName("Should inherit method management from JavaClass")
        void shouldInheritMethodManagement() {
            // JavaEnum extends JavaClass, so it should inherit method functionality
            Method method = new Method(JavaTypeFactory.getStringType(), "getValue");

            boolean added = javaEnum.add(method);

            assertThat(added).isTrue();
            assertThat(javaEnum.getMethods()).containsExactly(method);
        }
    }

    @Nested
    @DisplayName("Interface Implementation Tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should inherit interface management from JavaClass")
        void shouldInheritInterfaceManagement() {
            // JavaEnum extends JavaClass, so it should inherit interface functionality
            JavaType interfaceType = new JavaType("Serializable", "java.io");

            boolean added = javaEnum.addInterface(interfaceType);

            assertThat(added).isTrue();
            // Verify through generated code
            StringBuilder result = javaEnum.generateCode(0);
            assertThat(result.toString()).contains("implements Serializable");
        }
    }

    @Nested
    @DisplayName("Code Generation Tests")
    class CodeGenerationTests {

        @Test
        @DisplayName("Should generate basic enum code")
        void shouldGenerateBasicEnumCode() {
            StringBuilder result = javaEnum.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("package com.example;");
            assertThat(generatedCode).contains("public enum TestEnum");
            assertThat(generatedCode).contains("}");
        }

        @Test
        @DisplayName("Should generate code with indentation")
        void shouldGenerateCodeWithIndentation() {
            StringBuilder result = javaEnum.generateCode(1);
            String generatedCode = result.toString();

            // Should have some indentation
            assertThat(generatedCode).contains("    ");
        }

        @Test
        @DisplayName("Should generate enum code with items")
        void shouldGenerateEnumCodeWithItems() {
            javaEnum.addItem("ITEM1");
            javaEnum.addItem("ITEM2");

            StringBuilder result = javaEnum.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("ITEM1");
            assertThat(generatedCode).contains("ITEM2");
            assertThat(generatedCode).contains(";"); // Items should end with semicolon
        }

        @Test
        @DisplayName("Should generate enum code with fields")
        void shouldGenerateEnumCodeWithFields() {
            Field field = new Field(JavaTypeFactory.getStringType(), "value");
            javaEnum.add(field);

            StringBuilder result = javaEnum.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("value");
        }

        @Test
        @DisplayName("Should generate enum code with methods")
        void shouldGenerateEnumCodeWithMethods() {
            Method method = new Method(JavaTypeFactory.getStringType(), "getValue");
            javaEnum.add(method);

            StringBuilder result = javaEnum.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("getValue");
        }

        @Test
        @DisplayName("Should generate enum code with constructors")
        void shouldGenerateEnumCodeWithConstructors() {
            Constructor constructor = new Constructor(javaEnum);
            javaEnum.add(constructor);

            StringBuilder result = javaEnum.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("TestEnum");
        }

        @Test
        @DisplayName("Should generate enum code with interfaces")
        void shouldGenerateEnumCodeWithInterfaces() {
            JavaType interfaceType = new JavaType("Serializable", "java.io");
            javaEnum.addInterface(interfaceType);

            StringBuilder result = javaEnum.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("implements");
            assertThat(generatedCode).contains("Serializable");
        }

        @Test
        @DisplayName("Should generate method without indentation")
        void shouldGenerateWithoutIndentation() {
            // JavaEnum inherits generate() from parent if available
            StringBuilder result = javaEnum.generateCode(0);

            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("public enum TestEnum");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Complex Scenarios Tests")
    class EdgeCasesAndComplexScenariosTests {

        @Test
        @DisplayName("Should handle enum with all components")
        void shouldHandleEnumWithAllComponents() {
            // Add enum items
            javaEnum.addItem("ITEM1");
            javaEnum.addItem("ITEM2");

            // Add interface
            JavaType interfaceType = new JavaType("Serializable", "java.io");
            javaEnum.addInterface(interfaceType);

            // Add field
            Field field = new Field(JavaTypeFactory.getStringType(), "description");
            javaEnum.add(field);

            // Add method
            Method method = new Method(JavaTypeFactory.getStringType(), "getDescription");
            javaEnum.add(method);

            // Add constructor
            Constructor constructor = new Constructor(javaEnum);
            javaEnum.add(constructor);

            StringBuilder result = javaEnum.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("implements Serializable");
            assertThat(generatedCode).contains("ITEM1");
            assertThat(generatedCode).contains("ITEM2");
            assertThat(generatedCode).contains("description");
            assertThat(generatedCode).contains("getDescription");
            assertThat(generatedCode).contains("TestEnum");
        }

        @Test
        @DisplayName("Should handle empty enum")
        void shouldHandleEmptyEnum() {
            StringBuilder result = javaEnum.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("public enum TestEnum");
            assertThat(generatedCode).contains("}");
        }

        @Test
        @DisplayName("Should handle enum in default package")
        void shouldHandleEnumInDefaultPackage() {
            JavaEnum defaultEnum = new JavaEnum("DefaultEnum", "");

            StringBuilder result = defaultEnum.generateCode(0);
            String generatedCode = result.toString();

            // Should not contain package statement for empty package
            assertThat(generatedCode).doesNotContain("package ");
            assertThat(generatedCode).contains("public enum DefaultEnum");
        }

        @Test
        @DisplayName("Should handle enum items with different complexity")
        void shouldHandleEnumItemsWithDifferentComplexity() {
            // Simple item
            javaEnum.addItem("SIMPLE");

            // Complex item with parameters
            EnumItem complex = new EnumItem("COMPLEX");
            complex.addParameter("\"Complex Value\"");
            complex.addParameter("100");
            javaEnum.add(complex);

            StringBuilder result = javaEnum.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("SIMPLE");
            assertThat(generatedCode).contains("COMPLEX");
            assertThat(generatedCode).contains("Complex Value");
            assertThat(generatedCode).contains("100");
        }
    }

    @Nested
    @DisplayName("Inheritance and Polymorphism Tests")
    class InheritanceAndPolymorphismTests {

        @Test
        @DisplayName("Should inherit from JavaClass")
        void shouldInheritFromJavaClass() {
            assertThat(javaEnum).isInstanceOf(JavaClass.class);
        }

        @Test
        @DisplayName("Should support polymorphic usage as JavaClass")
        void shouldSupportPolymorphicUsageAsJavaClass() {
            JavaClass javaClass = javaEnum;

            assertThat(javaClass.getName()).isEqualTo("TestEnum");
            assertThat(javaClass.getClassPackage()).isEqualTo("com.example");
        }

        @Test
        @DisplayName("Should support polymorphic usage as ClassType")
        void shouldSupportPolymorphicUsageAsClassType() {
            ClassType classType = javaEnum;

            assertThat(classType.getName()).isEqualTo("TestEnum");
            assertThat(classType.getClassPackage()).isEqualTo("com.example");
        }

        @Test
        @DisplayName("Should generate code through ClassType interface")
        void shouldGenerateCodeThroughClassTypeInterface() {
            ClassType classType = javaEnum;

            StringBuilder result = classType.generateCode(0);

            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("public enum TestEnum");
        }

        @Test
        @DisplayName("Should override superclass methods properly")
        void shouldOverrideSuperclassMethodsProperly() {
            // Verify that enum-specific behavior overrides JavaClass behavior
            assertThatThrownBy(() -> javaEnum.getSuperClass())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("An enum does not have a super class.");

            assertThatThrownBy(() -> javaEnum.setSuperClass(new JavaType("Object", "java.lang")))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("An enum cannot have a super class.");
        }
    }
}
