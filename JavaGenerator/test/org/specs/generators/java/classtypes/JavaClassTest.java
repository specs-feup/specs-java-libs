package org.specs.generators.java.classtypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.specs.generators.java.enums.Modifier;
import org.specs.generators.java.enums.Privacy;
import org.specs.generators.java.members.Constructor;
import org.specs.generators.java.members.Field;
import org.specs.generators.java.members.Method;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.types.JavaTypeFactory;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for the {@link JavaClass} class.
 * Tests Java class generation functionality and member management.
 * 
 * @author Generated Tests
 */
@DisplayName("JavaClass Tests")
class JavaClassTest {

    private JavaClass javaClass;

    @BeforeEach
    void setUp() {
        javaClass = new JavaClass("TestClass", "com.example");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create JavaClass with JavaType")
        void shouldCreateJavaClassWithJavaType() {
            JavaType javaType = new JavaType("MyClass", "com.test");
            JavaClass newClass = new JavaClass(javaType);

            assertThat(newClass.getName()).isEqualTo("MyClass");
            assertThat(newClass.getClassPackage()).isEqualTo("com.test");
            assertThat(newClass.getSuperClass()).isEqualTo(JavaTypeFactory.getObjectType());
        }

        @Test
        @DisplayName("Should create JavaClass with name and package")
        void shouldCreateJavaClassWithNameAndPackage() {
            JavaClass newClass = new JavaClass("MyClass", "com.test");

            assertThat(newClass.getName()).isEqualTo("MyClass");
            assertThat(newClass.getClassPackage()).isEqualTo("com.test");
            assertThat(newClass.getSuperClass()).isEqualTo(JavaTypeFactory.getObjectType());
            assertThat(newClass.getFields()).isEmpty();
            assertThat(newClass.getMethods()).isEmpty();
            assertThat(newClass.getConstructors()).isEmpty();
            assertThat(newClass.getInterfaces()).isEmpty();
        }

        @Test
        @DisplayName("Should create JavaClass with modifier")
        void shouldCreateJavaClassWithModifier() {
            JavaClass abstractClass = new JavaClass("AbstractClass", "com.test", Modifier.ABSTRACT);

            assertThat(abstractClass.getName()).isEqualTo("AbstractClass");
            assertThat(abstractClass.getClassPackage()).isEqualTo("com.test");
            // Can't directly test modifier since it's inherited functionality
        }

        @Test
        @DisplayName("Should create JavaClass without modifier")
        void shouldCreateJavaClassWithoutModifier() {
            JavaClass regularClass = new JavaClass("RegularClass", "com.test", null);

            assertThat(regularClass.getName()).isEqualTo("RegularClass");
            assertThat(regularClass.getClassPackage()).isEqualTo("com.test");
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend ClassType")
        void shouldExtendClassType() {
            assertThat(javaClass).isInstanceOf(ClassType.class);
        }

        @Test
        @DisplayName("Should have default superclass as Object")
        void shouldHaveDefaultSuperclassAsObject() {
            assertThat(javaClass.getSuperClass()).isEqualTo(JavaTypeFactory.getObjectType());
        }

        @Test
        @DisplayName("Should set custom superclass")
        void shouldSetCustomSuperclass() {
            JavaType customSuperClass = new JavaType("CustomParent", "com.custom");

            javaClass.setSuperClass(customSuperClass);

            assertThat(javaClass.getSuperClass()).isEqualTo(customSuperClass);
        }

        @Test
        @DisplayName("Should handle null superclass")
        void shouldHandleNullSuperclass() {
            javaClass.setSuperClass(null);
            assertThat(javaClass.getSuperClass()).isNull();
        }
    }

    @Nested
    @DisplayName("Interface Management Tests")
    class InterfaceManagementTests {

        @Test
        @DisplayName("Should add interface")
        void shouldAddInterface() {
            JavaType interfaceType = new JavaType("Serializable", "java.io");

            boolean added = javaClass.addInterface(interfaceType);

            assertThat(added).isTrue();
            assertThat(javaClass.getInterfaces()).containsExactly(interfaceType);
        }

        @Test
        @DisplayName("Should not add duplicate interface")
        void shouldNotAddDuplicateInterface() {
            JavaType interfaceType = new JavaType("Serializable", "java.io");
            javaClass.addInterface(interfaceType);

            boolean addedAgain = javaClass.addInterface(interfaceType);

            assertThat(addedAgain).isFalse();
            assertThat(javaClass.getInterfaces()).containsExactly(interfaceType);
        }

        @Test
        @DisplayName("Should remove interface")
        void shouldRemoveInterface() {
            JavaType interfaceType = new JavaType("Serializable", "java.io");
            javaClass.addInterface(interfaceType);

            boolean removed = javaClass.removeInterface(interfaceType);

            assertThat(removed).isTrue();
            assertThat(javaClass.getInterfaces()).isEmpty();
        }

        @Test
        @DisplayName("Should not remove non-existent interface")
        void shouldNotRemoveNonExistentInterface() {
            JavaType interfaceType = new JavaType("Serializable", "java.io");

            boolean removed = javaClass.removeInterface(interfaceType);

            assertThat(removed).isFalse();
        }

        @Test
        @DisplayName("Should add multiple interfaces")
        void shouldAddMultipleInterfaces() {
            JavaType serializable = new JavaType("Serializable", "java.io");
            JavaType cloneable = new JavaType("Cloneable", "java.lang");

            javaClass.addInterface(serializable);
            javaClass.addInterface(cloneable);

            assertThat(javaClass.getInterfaces()).containsExactly(serializable, cloneable);
        }

        @Test
        @DisplayName("Should get interfaces list")
        void shouldGetInterfacesList() {
            assertThat(javaClass.getInterfaces()).isNotNull();
            assertThat(javaClass.getInterfaces()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Constructor Management Tests")
    class ConstructorManagementTests {

        @Test
        @DisplayName("Should add constructor")
        void shouldAddConstructor() {
            Constructor constructor = new Constructor(javaClass);
            assertThat(javaClass.getConstructors()).containsExactly(constructor);
        }

        @Test
        @DisplayName("Should not add duplicate constructor")
        void shouldNotAddDuplicateConstructor() {
            Constructor constructor = new Constructor(javaClass);

            boolean addedAgain = javaClass.add(constructor);

            assertThat(addedAgain).isFalse();
            assertThat(javaClass.getConstructors()).containsExactly(constructor);
        }

        @Test
        @DisplayName("Should remove constructor")
        void shouldRemoveConstructor() {
            Constructor constructor = new Constructor(javaClass);

            boolean removed = javaClass.remove(constructor);

            assertThat(removed).isTrue();
            assertThat(javaClass.getConstructors()).isEmpty();
        }

        @Test
        @DisplayName("Should not remove non-existent constructor")
        void shouldNotRemoveNonExistentConstructor() {
            Constructor constructor = new Constructor(javaClass);
            Constructor constructor2 = new Constructor(
                    new JavaClass("TestClass2", "com.example"));

            boolean removed = javaClass.remove(constructor2);

            assertThat(removed).isFalse();
            assertThat(javaClass.getConstructors()).containsExactly(constructor);
        }

        @Test
        @DisplayName("Should add multiple constructors")
        void shouldAddMultipleConstructors() {
            Constructor defaultConstructor = new Constructor(javaClass);
            Constructor paramConstructor = new Constructor(Privacy.PRIVATE, javaClass);

            assertThat(javaClass.getConstructors()).containsExactly(defaultConstructor, paramConstructor);
        }

        @Test
        @DisplayName("Should get constructors list")
        void shouldGetConstructorsList() {
            assertThat(javaClass.getConstructors()).isNotNull();
            assertThat(javaClass.getConstructors()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Field Management Tests")
    class FieldManagementTests {

        @Test
        @DisplayName("Should add field")
        void shouldAddField() {
            Field field = new Field(JavaTypeFactory.getStringType(), "testField");

            boolean added = javaClass.add(field);

            assertThat(added).isTrue();
            assertThat(javaClass.getFields()).containsExactly(field);
        }

        @Test
        @DisplayName("Should not add duplicate field")
        void shouldNotAddDuplicateField() {
            Field field = new Field(JavaTypeFactory.getStringType(), "testField");
            javaClass.add(field);

            boolean addedAgain = javaClass.add(field);

            assertThat(addedAgain).isFalse();
            assertThat(javaClass.getFields()).containsExactly(field);
        }

        @Test
        @DisplayName("Should remove field")
        void shouldRemoveField() {
            Field field = new Field(JavaTypeFactory.getStringType(), "testField");
            javaClass.add(field);

            boolean removed = javaClass.remove(field);

            assertThat(removed).isTrue();
            assertThat(javaClass.getFields()).isEmpty();
        }

        @Test
        @DisplayName("Should not remove non-existent field")
        void shouldNotRemoveNonExistentField() {
            Field field = new Field(JavaTypeFactory.getStringType(), "testField");

            boolean removed = javaClass.remove(field);

            assertThat(removed).isFalse();
        }

        @Test
        @DisplayName("Should add multiple fields")
        void shouldAddMultipleFields() {
            Field stringField = new Field(JavaTypeFactory.getStringType(), "stringField");
            Field intField = new Field(JavaTypeFactory.getIntType(), "intField");

            javaClass.add(stringField);
            javaClass.add(intField);

            assertThat(javaClass.getFields()).containsExactly(stringField, intField);
        }

        @Test
        @DisplayName("Should get fields list")
        void shouldGetFieldsList() {
            assertThat(javaClass.getFields()).isNotNull();
            assertThat(javaClass.getFields()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Method Management Tests")
    class MethodManagementTests {

        @Test
        @DisplayName("Should add method")
        void shouldAddMethod() {
            Method method = new Method(JavaTypeFactory.getVoidType(), "testMethod");

            boolean added = javaClass.add(method);

            assertThat(added).isTrue();
            assertThat(javaClass.getMethods()).containsExactly(method);
        }

        @Test
        @DisplayName("Should not add duplicate method")
        void shouldNotAddDuplicateMethod() {
            Method method = new Method(JavaTypeFactory.getVoidType(), "testMethod");
            javaClass.add(method);

            boolean addedAgain = javaClass.add(method);

            assertThat(addedAgain).isFalse();
            assertThat(javaClass.getMethods()).containsExactly(method);
        }

        @Test
        @DisplayName("Should remove method")
        void shouldRemoveMethod() {
            Method method = new Method(JavaTypeFactory.getVoidType(), "testMethod");
            javaClass.add(method);

            boolean removed = javaClass.remove(method);

            assertThat(removed).isTrue();
            assertThat(javaClass.getMethods()).isEmpty();
        }

        @Test
        @DisplayName("Should not remove non-existent method")
        void shouldNotRemoveNonExistentMethod() {
            Method method = new Method(JavaTypeFactory.getVoidType(), "testMethod");

            boolean removed = javaClass.remove(method);

            assertThat(removed).isFalse();
        }

        @Test
        @DisplayName("Should add multiple methods")
        void shouldAddMultipleMethods() {
            Method voidMethod = new Method(JavaTypeFactory.getVoidType(), "voidMethod");
            Method stringMethod = new Method(JavaTypeFactory.getStringType(), "stringMethod");

            javaClass.add(voidMethod);
            javaClass.add(stringMethod);

            assertThat(javaClass.getMethods()).containsExactly(voidMethod, stringMethod);
        }

        @Test
        @DisplayName("Should get methods list")
        void shouldGetMethodsList() {
            assertThat(javaClass.getMethods()).isNotNull();
            assertThat(javaClass.getMethods()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Code Generation Tests")
    class CodeGenerationTests {

        @Test
        @DisplayName("Should generate basic class code")
        void shouldGenerateBasicClassCode() {
            StringBuilder result = javaClass.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("package com.example;");
            assertThat(generatedCode).contains("public class TestClass");
            assertThat(generatedCode).contains("}");
        }

        @Test
        @DisplayName("Should generate code with indentation")
        void shouldGenerateCodeWithIndentation() {
            StringBuilder result = javaClass.generateCode(1);
            String generatedCode = result.toString();

            // Should have some indentation
            assertThat(generatedCode).contains("    ");
        }

        @Test
        @DisplayName("Should generate code with fields")
        void shouldGenerateCodeWithFields() {
            Field field = new Field(JavaTypeFactory.getStringType(), "testField");
            javaClass.add(field);

            StringBuilder result = javaClass.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("testField");
        }

        @Test
        @DisplayName("Should generate code with methods")
        void shouldGenerateCodeWithMethods() {
            Method method = new Method(JavaTypeFactory.getVoidType(), "testMethod");
            javaClass.add(method);

            StringBuilder result = javaClass.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("testMethod");
        }

        @Test
        @DisplayName("Should generate code with constructors")
        void shouldGenerateCodeWithConstructors() {
            Constructor constructor = new Constructor(javaClass);
            javaClass.add(constructor);

            StringBuilder result = javaClass.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains(javaClass.getName());
        }

        @Test
        @DisplayName("Should generate code with interfaces")
        void shouldGenerateCodeWithInterfaces() {
            JavaType interfaceType = new JavaType("Serializable", "java.io");
            javaClass.addInterface(interfaceType);

            StringBuilder result = javaClass.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("implements");
            assertThat(generatedCode).contains("Serializable");
        }

        @Test
        @DisplayName("Should generate code with custom superclass")
        void shouldGenerateCodeWithCustomSuperclass() {
            JavaType customSuperClass = new JavaType("CustomParent", "com.custom");
            javaClass.setSuperClass(customSuperClass);

            StringBuilder result = javaClass.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("extends CustomParent");
        }

        @Test
        @DisplayName("Should generate method without indentation")
        void shouldGenerateWithoutIndentation() {
            StringBuilder result = javaClass.generate();

            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("public class TestClass");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Complex Scenarios Tests")
    class EdgeCasesAndComplexScenariosTests {

        @Test
        @DisplayName("Should handle class with all components")
        void shouldHandleClassWithAllComponents() {
            // Add interface
            JavaType interfaceType = new JavaType("Serializable", "java.io");
            javaClass.addInterface(interfaceType);

            // Add field
            Field field = new Field(JavaTypeFactory.getStringType(), "name");
            javaClass.add(field);

            // Add method
            Method method = new Method(JavaTypeFactory.getStringType(), "getName");
            javaClass.add(method);

            // Add constructor
            Constructor constructor = new Constructor(javaClass);
            javaClass.add(constructor);

            StringBuilder result = javaClass.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("implements Serializable");
            assertThat(generatedCode).contains("name");
            assertThat(generatedCode).contains("getName");
            assertThat(generatedCode).contains("TestClass()");
        }

        @Test
        @DisplayName("Should handle class with multiple interfaces")
        void shouldHandleClassWithMultipleInterfaces() {
            JavaType serializable = new JavaType("Serializable", "java.io");
            JavaType cloneable = new JavaType("Cloneable", "java.lang");

            javaClass.addInterface(serializable);
            javaClass.addInterface(cloneable);

            StringBuilder result = javaClass.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("implements");
            assertThat(generatedCode).contains("Serializable");
            assertThat(generatedCode).contains("Cloneable");
        }

        @Test
        @DisplayName("Should handle empty class")
        void shouldHandleEmptyClass() {
            StringBuilder result = javaClass.generateCode(0);
            String generatedCode = result.toString();

            assertThat(generatedCode).contains("public class TestClass");
            assertThat(generatedCode).contains("}");
        }

        @Test
        @DisplayName("Should handle class in default package")
        void shouldHandleClassInDefaultPackage() {
            JavaClass defaultPackageClass = new JavaClass("DefaultClass", "");

            StringBuilder result = defaultPackageClass.generateCode(0);
            String generatedCode = result.toString();

            // Should not contain package statement for empty package
            assertThat(generatedCode).doesNotContain("package ");
            assertThat(generatedCode).contains("public class DefaultClass");
        }

        @Test
        @DisplayName("Should handle class with null package")
        void shouldHandleClassWithNullPackage() {
            JavaClass nullPackageClass = new JavaClass("NullClass", null);
            StringBuilder result = nullPackageClass.generateCode(0);
            String generatedCode = result.toString();
            assertThat(generatedCode).contains("public class NullClass");
        }
    }

    @Nested
    @DisplayName("Inheritance and Polymorphism Tests")
    class InheritanceAndPolymorphismTests {

        @Test
        @DisplayName("Should inherit from ClassType")
        void shouldInheritFromClassType() {
            assertThat(javaClass).isInstanceOf(ClassType.class);
        }

        @Test
        @DisplayName("Should support polymorphic usage as ClassType")
        void shouldSupportPolymorphicUsageAsClassType() {
            ClassType classType = javaClass;

            assertThat(classType.getName()).isEqualTo("TestClass");
            assertThat(classType.getClassPackage()).isEqualTo("com.example");
        }

        @Test
        @DisplayName("Should generate code through ClassType interface")
        void shouldGenerateCodeThroughClassTypeInterface() {
            ClassType classType = javaClass;

            StringBuilder result = classType.generateCode(0);

            assertThat(result).isNotNull();
            assertThat(result.toString()).contains("public class TestClass");
        }
    }
}
