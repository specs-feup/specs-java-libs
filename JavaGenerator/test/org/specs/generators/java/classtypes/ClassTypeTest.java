package org.specs.generators.java.classtypes;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.specs.generators.java.IGenerate;
import org.specs.generators.java.enums.Annotation;
import org.specs.generators.java.enums.JDocTag;
import org.specs.generators.java.enums.Modifier;
import org.specs.generators.java.enums.Privacy;
import org.specs.generators.java.members.JavaDoc;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for the {@link ClassType} abstract class.
 * Tests all common functionality for Java class generation.
 * 
 * @author Generated Tests
 */
@DisplayName("ClassType Tests")
class ClassTypeTest {

    /**
     * Concrete implementation of ClassType for testing purposes.
     */
    private static class TestClassType extends ClassType {

        public TestClassType(String name, String classPackage) {
            super(name, classPackage);
        }

        @Override
        public StringBuilder generateCode(int indentation) {
            StringBuilder sb = new StringBuilder();
            // Simple implementation for testing
            for (int i = 0; i < indentation; i++) {
                sb.append("    ");
            }
            sb.append("class ").append(getName()).append(" { }");
            return sb;
        }
    }

    private TestClassType classType;

    @BeforeEach
    void setUp() {
        classType = new TestClassType("TestClass", "com.example");
    }

    @Nested
    @DisplayName("Constructor and Initialization Tests")
    class ConstructorAndInitializationTests {

        @Test
        @DisplayName("Should create class type with name and package")
        void shouldCreateClassTypeWithNameAndPackage() {
            TestClassType newClassType = new TestClassType("MyClass", "com.test");

            assertThat(newClassType.getName()).isEqualTo("MyClass");
            assertThat(newClassType.getClassPackage()).isEqualTo("com.test");
            assertThat(newClassType.getPrivacy()).isEqualTo(Privacy.PUBLIC);
            assertThat(newClassType.getParent()).isEmpty();
            assertThat(newClassType.getImports()).isEmpty();
            assertThat(newClassType.getInnerTypes()).isEmpty();
            assertThat(newClassType.getAnnotations()).isEmpty();
        }

        @Test
        @DisplayName("Should initialize with default JavaDoc")
        void shouldInitializeWithDefaultJavaDoc() {
            assertThat(classType.getJavaDocComment()).isNotNull();
        }

        @Test
        @DisplayName("Should handle null package")
        void shouldHandleNullPackage() {
            TestClassType nullPackageClass = new TestClassType("NullPackageClass", null);

            assertThat(nullPackageClass.getClassPackage()).isEmpty();
            assertThat(nullPackageClass.getQualifiedName()).isEqualTo("NullPackageClass");
        }

        @Test
        @DisplayName("Should handle empty package")
        void shouldHandleEmptyPackage() {
            TestClassType emptyPackageClass = new TestClassType("EmptyPackageClass", "");

            assertThat(emptyPackageClass.getClassPackage()).isEmpty();
            assertThat(emptyPackageClass.getQualifiedName()).isEqualTo("EmptyPackageClass");
        }
    }

    @Nested
    @DisplayName("Interface Implementation Tests")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("Should implement IGenerate interface")
        void shouldImplementIGenerateInterface() {
            assertThat(classType).isInstanceOf(IGenerate.class);
        }

        @Test
        @DisplayName("Should generate code with indentation")
        void shouldGenerateCodeWithIndentation() {
            StringBuilder result = classType.generateCode(2);

            assertThat(result.toString()).startsWith("        class TestClass { }");
        }

        @Test
        @DisplayName("Should generate code without indentation")
        void shouldGenerateCodeWithoutIndentation() {
            StringBuilder result = classType.generateCode(0);

            assertThat(result.toString()).isEqualTo("class TestClass { }");
        }
    }

    @Nested
    @DisplayName("Qualified Name Tests")
    class QualifiedNameTests {

        @Test
        @DisplayName("Should return qualified name with package")
        void shouldReturnQualifiedNameWithPackage() {
            String qualifiedName = classType.getQualifiedName();

            assertThat(qualifiedName).isEqualTo("com.example.TestClass");
        }

        @Test
        @DisplayName("Should return qualified name without package when null")
        void shouldReturnQualifiedNameWithoutPackageWhenNull() {
            classType.setClassPackage(null);

            String qualifiedName = classType.getQualifiedName();

            assertThat(qualifiedName).isEqualTo("TestClass");
        }

        @Test
        @DisplayName("Should return qualified name without package when empty")
        void shouldReturnQualifiedNameWithoutPackageWhenEmpty() {
            classType.setClassPackage("");

            String qualifiedName = classType.getQualifiedName();

            assertThat(qualifiedName).isEqualTo("TestClass");
        }
    }

    @Nested
    @DisplayName("Import Management Tests")
    class ImportManagementTests {

        @Test
        @DisplayName("Should add single import")
        void shouldAddSingleImport() {
            classType.addImport("java.util.List");

            assertThat(classType.getImports()).containsExactly("java.util.List");
        }

        @Test
        @DisplayName("Should add multiple imports")
        void shouldAddMultipleImports() {
            classType.addImport("java.util.List", "java.util.Map", "java.util.Set");

            assertThat(classType.getImports()).containsExactly("java.util.List", "java.util.Map", "java.util.Set");
        }

        @Test
        @DisplayName("Should not add duplicate imports")
        void shouldNotAddDuplicateImports() {
            classType.addImport("java.util.List");
            classType.addImport("java.util.List");

            assertThat(classType.getImports()).containsExactly("java.util.List");
        }

        @Test
        @DisplayName("Should get all imports including inner types")
        void shouldGetAllImportsIncludingInnerTypes() {
            classType.addImport("java.util.List");

            TestClassType innerType = new TestClassType("InnerClass", "com.example.inner");
            innerType.addImport("java.util.Map");
            classType.add(innerType);

            List<String> allImports = classType.getAllImports();

            assertThat(allImports).containsExactly("java.util.List", "java.util.Map");
        }
    }

    @Nested
    @DisplayName("Inner Types Management Tests")
    class InnerTypesManagementTests {

        @Test
        @DisplayName("Should add inner type")
        void shouldAddInnerType() {
            TestClassType innerType = new TestClassType("InnerClass", "com.example.inner");

            boolean added = classType.add(innerType);

            assertThat(added).isTrue();
            assertThat(classType.getInnerTypes()).containsExactly(innerType);
            assertThat(innerType.getParent()).contains(classType);
        }

        @Test
        @DisplayName("Should not add duplicate inner type")
        void shouldNotAddDuplicateInnerType() {
            TestClassType innerType = new TestClassType("InnerClass", "com.example.inner");

            boolean firstAdd = classType.add(innerType);
            boolean secondAdd = classType.add(innerType);

            assertThat(firstAdd).isTrue();
            assertThat(secondAdd).isFalse();
            assertThat(classType.getInnerTypes()).containsExactly(innerType);
        }

        @Test
        @DisplayName("Should set parent when adding inner type")
        void shouldSetParentWhenAddingInnerType() {
            TestClassType innerType = new TestClassType("InnerClass", "com.example.inner");

            classType.add(innerType);

            assertThat(innerType.getParent()).contains(classType);
        }
    }

    @Nested
    @DisplayName("Privacy Management Tests")
    class PrivacyManagementTests {

        @Test
        @DisplayName("Should set and get privacy")
        void shouldSetAndGetPrivacy() {
            classType.setPrivacy(Privacy.PRIVATE);

            assertThat(classType.getPrivacy()).isEqualTo(Privacy.PRIVATE);
        }

        @Test
        @DisplayName("Should default to public privacy")
        void shouldDefaultToPublicPrivacy() {
            assertThat(classType.getPrivacy()).isEqualTo(Privacy.PUBLIC);
        }

        @ParameterizedTest
        @DisplayName("Should handle all privacy levels")
        @ValueSource(strings = { "PUBLIC", "PRIVATE", "PROTECTED", "PACKAGE_PROTECTED" })
        void shouldHandleAllPrivacyLevels(String privacyName) {
            Privacy privacy = Privacy.valueOf(privacyName);

            classType.setPrivacy(privacy);

            assertThat(classType.getPrivacy()).isEqualTo(privacy);
        }
    }

    @Nested
    @DisplayName("Modifier Management Tests")
    class ModifierManagementTests {

        @Test
        @DisplayName("Should add modifier")
        void shouldAddModifier() {
            boolean added = classType.add(Modifier.ABSTRACT);

            assertThat(added).isTrue();
        }

        @Test
        @DisplayName("Should not add duplicate modifier")
        void shouldNotAddDuplicateModifier() {
            classType.add(Modifier.ABSTRACT);
            boolean addedAgain = classType.add(Modifier.ABSTRACT);

            assertThat(addedAgain).isFalse();
        }

        @Test
        @DisplayName("Should remove modifier")
        void shouldRemoveModifier() {
            classType.add(Modifier.ABSTRACT);
            boolean removed = classType.remove(Modifier.ABSTRACT);

            assertThat(removed).isTrue();
        }

        @Test
        @DisplayName("Should not remove non-existent modifier")
        void shouldNotRemoveNonExistentModifier() {
            boolean removed = classType.remove(Modifier.ABSTRACT);

            assertThat(removed).isFalse();
        }

        @Test
        @DisplayName("Should add multiple modifiers")
        void shouldAddMultipleModifiers() {
            boolean abstractAdded = classType.add(org.specs.generators.java.enums.Modifier.ABSTRACT);
            boolean finalAdded = classType.add(org.specs.generators.java.enums.Modifier.FINAL);
            boolean staticAdded = classType.add(org.specs.generators.java.enums.Modifier.STATIC);

            assertThat(abstractAdded).isTrue();
            assertThat(finalAdded).isTrue();
            assertThat(staticAdded).isTrue();

            // Test removal to verify they were added
            assertThat(classType.remove(org.specs.generators.java.enums.Modifier.ABSTRACT)).isTrue();
            assertThat(classType.remove(org.specs.generators.java.enums.Modifier.FINAL)).isTrue();
            assertThat(classType.remove(org.specs.generators.java.enums.Modifier.STATIC)).isTrue();
        }
    }

    @Nested
    @DisplayName("Annotation Management Tests")
    class AnnotationManagementTests {

        @Test
        @DisplayName("Should add annotation")
        void shouldAddAnnotation() {
            boolean added = classType.add(Annotation.DEPRECATED);

            assertThat(added).isTrue();
            assertThat(classType.getAnnotations()).containsExactly(Annotation.DEPRECATED);
        }

        @Test
        @DisplayName("Should not add duplicate annotation")
        void shouldNotAddDuplicateAnnotation() {
            classType.add(Annotation.DEPRECATED);
            boolean addedAgain = classType.add(Annotation.DEPRECATED);

            assertThat(addedAgain).isFalse();
            assertThat(classType.getAnnotations()).containsExactly(Annotation.DEPRECATED);
        }

        @Test
        @DisplayName("Should remove annotation")
        void shouldRemoveAnnotation() {
            classType.add(Annotation.DEPRECATED);
            boolean removed = classType.remove(Annotation.DEPRECATED);

            assertThat(removed).isTrue();
            assertThat(classType.getAnnotations()).isEmpty();
        }

        @Test
        @DisplayName("Should not remove non-existent annotation")
        void shouldNotRemoveNonExistentAnnotation() {
            boolean removed = classType.remove(Annotation.DEPRECATED);

            assertThat(removed).isFalse();
        }

        @Test
        @DisplayName("Should add multiple annotations")
        void shouldAddMultipleAnnotations() {
            classType.add(Annotation.DEPRECATED);
            classType.add(Annotation.OVERRIDE);

            assertThat(classType.getAnnotations()).containsExactly(Annotation.DEPRECATED, Annotation.OVERRIDE);
        }
    }

    @Nested
    @DisplayName("JavaDoc Management Tests")
    class JavaDocManagementTests {

        @Test
        @DisplayName("Should set JavaDoc comment")
        void shouldSetJavaDocComment() {
            JavaDoc newJavaDoc = new JavaDoc("Test documentation");

            classType.setJavaDocComment(newJavaDoc);

            assertThat(classType.getJavaDocComment()).isSameAs(newJavaDoc);
        }

        @Test
        @DisplayName("Should append JavaDoc comment")
        void shouldAppendJavaDocComment() {
            StringBuilder result = classType.appendComment("This is a test class");

            assertThat(result).isNotNull();
            // JavaDoc details are tested in JavaDoc tests, here we just verify the method
            // works
            assertThat(classType.getJavaDocComment()).isNotNull();
        }

        @Test
        @DisplayName("Should add JavaDoc tag with description")
        void shouldAddJavaDocTagWithDescription() {
            classType.add(JDocTag.AUTHOR, "Test Author");

            // JavaDoc details are tested in JavaDoc tests, here we just verify the method
            // works
            assertThat(classType.getJavaDocComment()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Name Management Tests")
    class NameManagementTests {

        @Test
        @DisplayName("Should set and get name")
        void shouldSetAndGetName() {
            classType.setName("NewClassName");

            assertThat(classType.getName()).isEqualTo("NewClassName");
        }

        @Test
        @DisplayName("Should handle empty name")
        void shouldHandleEmptyName() {
            classType.setName("");

            assertThat(classType.getName()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null name")
        void shouldHandleNullName() {
            classType.setName(null);

            assertThat(classType.getName()).isNull();
        }
    }

    @Nested
    @DisplayName("Parent-Child Relationship Tests")
    class ParentChildRelationshipTests {

        @Test
        @DisplayName("Should set parent")
        void shouldSetParent() {
            TestClassType parent = new TestClassType("ParentClass", "com.parent");

            classType.setParent(parent);

            assertThat(classType.getParent()).contains(parent);
        }

        @Test
        @DisplayName("Should default to no parent")
        void shouldDefaultToNoParent() {
            assertThat(classType.getParent()).isEmpty();
        }

        @Test
        @DisplayName("Should override parent")
        void shouldOverrideParent() {
            TestClassType parent1 = new TestClassType("ParentClass1", "com.parent1");
            TestClassType parent2 = new TestClassType("ParentClass2", "com.parent2");

            classType.setParent(parent1);
            classType.setParent(parent2);

            assertThat(classType.getParent()).contains(parent2);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling Tests")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("Should handle large number of imports")
        void shouldHandleLargeNumberOfImports() {
            for (int i = 0; i < 1000; i++) {
                classType.addImport("com.example.package" + i + ".Class" + i);
            }

            assertThat(classType.getImports()).hasSize(1000);
        }

        @Test
        @DisplayName("Should handle complex package names")
        void shouldHandleComplexPackageNames() {
            TestClassType complexClass = new TestClassType("ComplexClass",
                    "com.very.long.package.name.with.many.levels");

            assertThat(complexClass.getQualifiedName())
                    .isEqualTo("com.very.long.package.name.with.many.levels.ComplexClass");
        }

        @Test
        @DisplayName("Should handle special characters in names")
        void shouldHandleSpecialCharactersInNames() {
            TestClassType specialClass = new TestClassType("Class$Inner_123", "com.example");

            assertThat(specialClass.getName()).isEqualTo("Class$Inner_123");
            assertThat(specialClass.getQualifiedName()).isEqualTo("com.example.Class$Inner_123");
        }

        @Test
        @DisplayName("Should handle nested inner types")
        void shouldHandleNestedInnerTypes() {
            TestClassType level1 = new TestClassType("Level1", "com.level1");
            TestClassType level2 = new TestClassType("Level2", "com.level2");
            TestClassType level3 = new TestClassType("Level3", "com.level3");

            level2.add(level3);
            classType.add(level1);
            classType.add(level2);

            assertThat(classType.getInnerTypes()).hasSize(2);
            assertThat(level2.getInnerTypes()).hasSize(1);
            assertThat(level3.getParent()).contains(level2);
        }
    }

    @Nested
    @DisplayName("Inheritance and Polymorphism Tests")
    class InheritanceAndPolymorphismTests {

        @Test
        @DisplayName("Should be abstract class")
        void shouldBeAbstractClass() {
            assertThat(java.lang.reflect.Modifier.isAbstract(ClassType.class.getModifiers())).isTrue();
        }

        @Test
        @DisplayName("Should allow concrete implementations")
        void shouldAllowConcreteImplementations() {
            TestClassType concreteImpl = new TestClassType("ConcreteClass", "com.concrete");

            assertThat(concreteImpl).isInstanceOf(ClassType.class);
            assertThat(concreteImpl.generateCode(0)).isNotNull();
        }

        @Test
        @DisplayName("Should support polymorphic behavior")
        void shouldSupportPolymorphicBehavior() {
            ClassType polymorphicRef = new TestClassType("PolyClass", "com.poly");

            assertThat(polymorphicRef.getName()).isEqualTo("PolyClass");
            assertThat(polymorphicRef.getClassPackage()).isEqualTo("com.poly");
        }
    }
}
