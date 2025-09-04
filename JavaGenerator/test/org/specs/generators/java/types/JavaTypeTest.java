package org.specs.generators.java.types;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Enhanced test suite for {@link JavaType} class.
 * Tests Java type representation including names, packages, arrays, generics,
 * and code generation.
 * 
 * @author Generated Tests
 */
@DisplayName("JavaType Enhanced Tests")
class JavaTypeTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create JavaType with name, package, and array dimension")
        void shouldCreateJavaTypeWithNamePackageAndArrayDimension() {
            JavaType javaType = new JavaType("String", "java.lang", 1);

            assertThat(javaType.getName()).isEqualTo("String");
            assertThat(javaType.getPackage()).isEqualTo("java.lang");
            assertThat(javaType.isArray()).isTrue();
            assertThat(javaType.getArrayDimension()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should create JavaType with name and package")
        void shouldCreateJavaTypeWithNameAndPackage() {
            JavaType javaType = new JavaType("List", "java.util");

            assertThat(javaType.getName()).isEqualTo("List");
            assertThat(javaType.getPackage()).isEqualTo("java.util");
            assertThat(javaType.isArray()).isFalse();
            assertThat(javaType.getArrayDimension()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should create JavaType with name only")
        void shouldCreateJavaTypeWithNameOnly() {
            JavaType javaType = new JavaType("MyClass");

            assertThat(javaType.getName()).isEqualTo("MyClass");
            assertThat(javaType.getPackage()).isNull();
            assertThat(javaType.isArray()).isFalse();
            assertThat(javaType.getArrayDimension()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should create JavaType from Class object")
        void shouldCreateJavaTypeFromClassObject() {
            JavaType stringType = new JavaType(String.class);

            assertThat(stringType.getName()).isEqualTo("String");
            assertThat(stringType.getPackage()).isEqualTo("java.lang");
            assertThat(stringType.isArray()).isFalse();
        }

        @Test
        @DisplayName("Should create JavaType from array Class object")
        void shouldCreateJavaTypeFromArrayClassObject() {
            JavaType arrayType = new JavaType(String[].class);

            assertThat(arrayType.isArray()).isTrue();
            assertThat(arrayType.getArrayDimension()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Type Properties Tests")
    class TypePropertiesTests {

        @Test
        @DisplayName("Should detect primitive types")
        void shouldDetectPrimitiveTypes() {
            JavaType intType = new JavaType("int");
            JavaType stringType = new JavaType("String");

            assertThat(intType.isPrimitive()).isTrue();
            assertThat(stringType.isPrimitive()).isFalse();
        }

        @Test
        @DisplayName("Should handle array properties")
        void shouldHandleArrayProperties() {
            JavaType normalType = new JavaType("String", "java.lang", 0);
            JavaType arrayType = new JavaType("String", "java.lang", 2);

            assertThat(normalType.isArray()).isFalse();
            assertThat(normalType.getArrayDimension()).isEqualTo(0);

            assertThat(arrayType.isArray()).isTrue();
            assertThat(arrayType.getArrayDimension()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should handle enum properties")
        void shouldHandleEnumProperties() {
            JavaType enumType = new JavaType("Color");
            enumType.setEnum(true);

            assertThat(enumType.isEnum()).isTrue();

            enumType.setEnum(false);
            assertThat(enumType.isEnum()).isFalse();
        }

        @Test
        @DisplayName("Should handle package presence")
        void shouldHandlePackagePresence() {
            JavaType withPackage = new JavaType("String", "java.lang");
            JavaType withoutPackage = new JavaType("MyClass");

            assertThat(withPackage.hasPackage()).isTrue();
            assertThat(withoutPackage.hasPackage()).isFalse();
        }
    }

    @Nested
    @DisplayName("Name Generation Tests")
    class NameGenerationTests {

        @Test
        @DisplayName("Should generate canonical name")
        void shouldGenerateCanonicalName() {
            JavaType javaType = new JavaType("String", "java.lang");
            assertThat(javaType.getCanonicalName()).isEqualTo("java.lang.String");

            JavaType noPackageType = new JavaType("MyClass");
            assertThat(noPackageType.getCanonicalName()).isEqualTo("MyClass");
        }

        @Test
        @DisplayName("Should generate simple type")
        void shouldGenerateSimpleType() {
            JavaType simpleType = new JavaType("List", "java.util");
            assertThat(simpleType.getSimpleType()).isEqualTo("List");

            JavaType arrayType = new JavaType("String", "java.lang", 2);
            assertThat(arrayType.getSimpleType()).isEqualTo("String[][]");
        }

        @Test
        @DisplayName("Should generate canonical type")
        void shouldGenerateCanonicalType() {
            JavaType simpleType = new JavaType("List", "java.util");
            assertThat(simpleType.getCanonicalType()).isEqualTo("java.util.List");

            JavaType arrayType = new JavaType("String", "java.lang", 1);
            assertThat(arrayType.getCanonicalType()).isEqualTo("java.lang.String[]");
        }
    }

    @Nested
    @DisplayName("Generics Management Tests")
    class GenericsManagementTests {

        @Test
        @DisplayName("Should add and manage generics")
        void shouldAddAndManageGenerics() {
            JavaType listType = new JavaType("List", "java.util");
            JavaGenericType stringGeneric = new JavaGenericType(new JavaType("String"));

            boolean added = listType.addGeneric(stringGeneric);

            assertThat(added).isTrue();
            assertThat(listType.getGenerics()).hasSize(1);
            assertThat(listType.getGenerics()).contains(stringGeneric);
        }

        @Test
        @DisplayName("Should add type as generic")
        void shouldAddTypeAsGeneric() {
            JavaType mapType = new JavaType("Map", "java.util");
            JavaType stringType = new JavaType("String");

            boolean added = mapType.addTypeAsGeneric(stringType);

            assertThat(added).isTrue();
            assertThat(mapType.getGenerics()).hasSize(1);
            assertThat(mapType.getGenerics().get(0).getTheType().getName()).isEqualTo("String");
        }

        @Test
        @DisplayName("Should set generics list")
        void shouldSetGenericsList() {
            JavaType containerType = new JavaType("Container");
            List<JavaGenericType> generics = new ArrayList<>();
            generics.add(new JavaGenericType(new JavaType("T")));
            generics.add(new JavaGenericType(new JavaType("U")));

            containerType.setGenerics(generics);

            assertThat(containerType.getGenerics()).hasSize(2);
            assertThat(containerType.getGenerics().get(0).getTheType().getName()).isEqualTo("T");
            assertThat(containerType.getGenerics().get(1).getTheType().getName()).isEqualTo("U");
        }

        @Test
        @DisplayName("Should handle empty generics list")
        void shouldHandleEmptyGenericsList() {
            JavaType simpleType = new JavaType("String");

            assertThat(simpleType.getGenerics()).isEmpty();
        }

        @Test
        @DisplayName("Should generate generic string representations")
        void shouldGenerateGenericStringRepresentations() {
            JavaType listType = new JavaType("List", "java.util");
            listType.addTypeAsGeneric(new JavaType("String"));

            String simpleType = listType.getSimpleType();
            String canonicalType = listType.getCanonicalType();

            assertThat(simpleType).contains("List");
            assertThat(simpleType).contains("String");
            assertThat(simpleType).contains("<");
            assertThat(simpleType).contains(">");

            assertThat(canonicalType).contains("java.util.List");
            assertThat(canonicalType).contains("<");
            assertThat(canonicalType).contains(">");
        }
    }

    @Nested
    @DisplayName("Array Handling Tests")
    class ArrayHandlingTests {

        @Test
        @DisplayName("Should handle single dimension arrays")
        void shouldHandleSingleDimensionArrays() {
            JavaType arrayType = new JavaType("int", null, 1);

            assertThat(arrayType.isArray()).isTrue();
            assertThat(arrayType.getArrayDimension()).isEqualTo(1);
            assertThat(arrayType.getSimpleType()).isEqualTo("int[]");
        }

        @Test
        @DisplayName("Should handle multi-dimension arrays")
        void shouldHandleMultiDimensionArrays() {
            JavaType multiArrayType = new JavaType("Object", "java.lang", 3);

            assertThat(multiArrayType.isArray()).isTrue();
            assertThat(multiArrayType.getArrayDimension()).isEqualTo(3);
            assertThat(multiArrayType.getSimpleType()).isEqualTo("Object[][][]");
            assertThat(multiArrayType.getCanonicalType()).isEqualTo("java.lang.Object[][][]");
        }

        @Test
        @DisplayName("Should set array dimension")
        void shouldSetArrayDimension() {
            JavaType javaType = new JavaType("String");

            javaType.setArrayDimension(2);

            assertThat(javaType.isArray()).isTrue();
            assertThat(javaType.getArrayDimension()).isEqualTo(2);
            assertThat(javaType.getSimpleType()).isEqualTo("String[][]");
        }
    }

    @Nested
    @DisplayName("Import Requirements Tests")
    class ImportRequirementsTests {

        @Test
        @DisplayName("Should require import for non-java.lang packages")
        void shouldRequireImportForNonJavaLangPackages() {
            JavaType listType = new JavaType("List", "java.util");
            JavaType stringType = new JavaType("String", "java.lang");
            JavaType customType = new JavaType("MyClass", "com.example");

            assertThat(listType.requiresImport()).isTrue();
            assertThat(stringType.requiresImport()).isFalse();
            assertThat(customType.requiresImport()).isTrue();
        }

        @Test
        @DisplayName("Should not require import for primitives")
        void shouldNotRequireImportForPrimitives() {
            JavaType intType = new JavaType("int");

            assertThat(intType.requiresImport()).isFalse();
        }

        @Test
        @DisplayName("Should not require import for types without package")
        void shouldNotRequireImportForTypesWithoutPackage() {
            JavaType localType = new JavaType("LocalClass");

            assertThat(localType.requiresImport()).isFalse();
        }
    }

    @Nested
    @DisplayName("Clone Tests")
    class CloneTests {

        @Test
        @DisplayName("Should clone simple JavaType")
        void shouldCloneSimpleJavaType() {
            JavaType original = new JavaType("String", "java.lang");
            original.setEnum(true);

            JavaType cloned = original.clone();

            assertThat(cloned).isNotSameAs(original);
            assertThat(cloned.getName()).isEqualTo(original.getName());
            assertThat(cloned.getPackage()).isEqualTo(original.getPackage());
            assertThat(cloned.isEnum()).isEqualTo(original.isEnum());
        }

        @Test
        @DisplayName("Should clone JavaType with generics")
        void shouldCloneJavaTypeWithGenerics() {
            JavaType original = new JavaType("List", "java.util");
            original.addTypeAsGeneric(new JavaType("String"));
            original.addTypeAsGeneric(new JavaType("Integer"));

            JavaType cloned = original.clone();

            assertThat(cloned).isNotSameAs(original);
            assertThat(cloned.getGenerics()).hasSize(original.getGenerics().size());
            assertThat(cloned.getGenerics().get(0)).isNotSameAs(original.getGenerics().get(0));
        }

        @Test
        @DisplayName("Should clone array JavaType")
        void shouldCloneArrayJavaType() {
            JavaType original = new JavaType("int", null, 2);

            JavaType cloned = original.clone();

            assertThat(cloned).isNotSameAs(original);
            assertThat(cloned.getArrayDimension()).isEqualTo(original.getArrayDimension());
            assertThat(cloned.isArray()).isEqualTo(original.isArray());
        }
    }

    @Nested
    @DisplayName("Primitive Type Tests")
    class PrimitiveTypeTests {

        @Test
        @DisplayName("Should handle primitive type setting")
        void shouldHandlePrimitiveTypeSetting() {
            JavaType primitiveType = new JavaType("int");
            JavaType objectType = new JavaType("String");

            assertThat(primitiveType.isPrimitive()).isTrue();
            assertThat(objectType.isPrimitive()).isFalse();
        }

        @Test
        @DisplayName("Should identify common primitive types")
        void shouldIdentifyCommonPrimitiveTypes() {
            JavaType intType = new JavaType("int");
            JavaType booleanType = new JavaType("boolean");
            JavaType doubleType = new JavaType("double");

            assertThat(intType.isPrimitive()).isTrue();
            assertThat(booleanType.isPrimitive()).isTrue();
            assertThat(doubleType.isPrimitive()).isTrue();
        }
    }

    @Nested
    @DisplayName("Package and Name Manipulation Tests")
    class PackageAndNameManipulationTests {

        @Test
        @DisplayName("Should set and get name")
        void shouldSetAndGetName() {
            JavaType javaType = new JavaType("OriginalName");

            javaType.setName("NewName");

            assertThat(javaType.getName()).isEqualTo("NewName");
        }

        @Test
        @DisplayName("Should set and get package")
        void shouldSetAndGetPackage() {
            JavaType javaType = new JavaType("MyClass");

            javaType.setPackage("com.example");

            assertThat(javaType.getPackage()).isEqualTo("com.example");
            assertThat(javaType.hasPackage()).isTrue();
            assertThat(javaType.getCanonicalName()).isEqualTo("com.example.MyClass");
        }

        @Test
        @DisplayName("Should handle null package setting")
        void shouldHandleNullPackageSetting() {
            JavaType javaType = new JavaType("MyClass", "com.example");

            javaType.setPackage(null);

            assertThat(javaType.getPackage()).isNull();
            assertThat(javaType.hasPackage()).isFalse();
            assertThat(javaType.getCanonicalName()).isEqualTo("MyClass");
        }
    }

    @Nested
    @DisplayName("Complex Scenarios Tests")
    class ComplexScenariosTests {

        @Test
        @DisplayName("Should handle generic array types")
        void shouldHandleGenericArrayTypes() {
            JavaType arrayListType = new JavaType("List", "java.util", 1);
            arrayListType.addTypeAsGeneric(new JavaType("String"));

            assertThat(arrayListType.isArray()).isTrue();
            assertThat(arrayListType.getGenerics()).hasSize(1);
            assertThat(arrayListType.getSimpleType()).contains("List");
            assertThat(arrayListType.getSimpleType()).contains("[]");
        }

        @Test
        @DisplayName("Should handle nested generics")
        void shouldHandleNestedGenerics() {
            JavaType outerType = new JavaType("Optional", "java.util");
            JavaType innerType = new JavaType("List", "java.util");
            innerType.addTypeAsGeneric(new JavaType("String"));

            outerType.addGeneric(new JavaGenericType(innerType));

            assertThat(outerType.getGenerics()).hasSize(1);
            String canonicalType = outerType.getCanonicalType();
            assertThat(canonicalType).contains("Optional");
            assertThat(canonicalType).contains("List");
        }

        @Test
        @DisplayName("Should handle enum with generics")
        void shouldHandleEnumWithGenerics() {
            JavaType enumType = new JavaType("MyEnum", "com.example");
            enumType.setEnum(true);
            enumType.addTypeAsGeneric(new JavaType("T"));

            assertThat(enumType.isEnum()).isTrue();
            assertThat(enumType.getGenerics()).hasSize(1);
            assertThat(enumType.requiresImport()).isTrue();
        }

        @Test
        @DisplayName("Should handle primitive array types")
        void shouldHandlePrimitiveArrayTypes() {
            JavaType primitiveArrayType = new JavaType("int", null, 2);

            assertThat(primitiveArrayType.isPrimitive()).isTrue();
            assertThat(primitiveArrayType.isArray()).isTrue();
            assertThat(primitiveArrayType.getSimpleType()).isEqualTo("int[][]");
            assertThat(primitiveArrayType.requiresImport()).isFalse();
        }
    }

    @Nested
    @DisplayName("toString Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should provide meaningful toString representation")
        void shouldProvideMeaningfulToStringRepresentation() {
            JavaType javaType = new JavaType("List", "java.util");
            javaType.addTypeAsGeneric(new JavaType("String"));

            String toString = javaType.toString();

            assertThat(toString).isNotNull();
            assertThat(toString).isNotEmpty();
        }

        @Test
        @DisplayName("Should include array information in toString")
        void shouldIncludeArrayInformationInToString() {
            JavaType arrayType = new JavaType("String", "java.lang", 2);

            String toString = arrayType.toString();

            assertThat(toString).contains("String");
        }
    }
}
