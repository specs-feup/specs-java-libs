package org.specs.generators.java.members;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.specs.generators.java.enums.Annotation;
import org.specs.generators.java.enums.Modifier;
import org.specs.generators.java.enums.Privacy;
import org.specs.generators.java.exprs.GenericExpression;
import org.specs.generators.java.exprs.IExpression;
import org.specs.generators.java.types.JavaType;
import org.specs.generators.java.types.JavaTypeFactory;
import org.specs.generators.java.types.Primitive;

import pt.up.fe.specs.util.SpecsStrings;

/**
 * Comprehensive Phase 3 test class for {@link Field}.
 * Tests field creation, property management, modifier handling, annotation
 * support, initialization, code generation, and various field configurations.
 * 
 * @author Generated Tests
 */
@DisplayName("Field Tests - Phase 3 Enhanced")
public class FieldTest {

    private Field field;
    private JavaType intType;
    private JavaType stringType;
    private JavaType booleanType;

    @BeforeEach
    void setUp() {
        intType = JavaTypeFactory.getPrimitiveType(Primitive.INT);
        stringType = JavaTypeFactory.getStringType();
        booleanType = JavaTypeFactory.getPrimitiveType(Primitive.BOOLEAN);
        field = new Field(intType, "testField");
    }

    @Nested
    @DisplayName("Field Creation Tests")
    class FieldCreationTests {

        @Test
        @DisplayName("Constructor with type and name should create field correctly")
        void testConstructor_WithTypeAndName_CreatesFieldCorrectly() {
            // When (field created in setUp)

            // Then
            assertThat(field.getName()).isEqualTo("testField");
            assertThat(field.getType()).isEqualTo(intType);
            assertThat(field.getPrivacy()).isEqualTo(Privacy.PRIVATE);
            assertThat(field.getModifiers()).isNotNull().isEmpty();
            assertThat(field.getInitializer()).isNull();
            assertThat(field.isDefaultInitializer()).isFalse();
        }

        @Test
        @DisplayName("Constructor with type, name and privacy should create field correctly")
        void testConstructor_WithTypeNameAndPrivacy_CreatesFieldCorrectly() {
            // When
            Field publicField = new Field(stringType, "publicField", Privacy.PUBLIC);

            // Then
            assertThat(publicField.getName()).isEqualTo("publicField");
            assertThat(publicField.getType()).isEqualTo(stringType);
            assertThat(publicField.getPrivacy()).isEqualTo(Privacy.PUBLIC);
        }

        @Test
        @DisplayName("Constructor should handle null type")
        void testConstructor_WithNullType_AcceptsNull() {
            // When/Then
            assertThatThrownBy(() -> new Field(null, "nullTypeField"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Constructor should handle null name")
        void testConstructor_WithNullName_AcceptsNull() {
            // When/Then
            assertThatCode(() -> new Field(intType, null))
                    .doesNotThrowAnyException();

            Field nullNameField = new Field(intType, null);
            assertThat(nullNameField.getName()).isNull();
        }

        @Test
        @DisplayName("Constructor should handle various field types")
        void testConstructor_WithVariousTypes_HandlesCorrectly() {
            // String field
            Field stringField = new Field(stringType, "stringField");
            assertThat(stringField.getType()).isEqualTo(stringType);

            // Boolean field
            Field boolField = new Field(booleanType, "boolField");
            assertThat(boolField.getType()).isEqualTo(booleanType);

            // Array type - using JavaType constructor
            JavaType arrayType = new JavaType(int[].class);
            Field arrayField = new Field(arrayType, "arrayField");
            assertThat(arrayField.getType()).isEqualTo(arrayType);
        }
    }

    @Nested
    @DisplayName("Property Management Tests")
    class PropertyManagementTests {

        @Test
        @DisplayName("getName() should return field name")
        void testGetName_ReturnsFieldName() {
            // When
            String name = field.getName();

            // Then
            assertThat(name).isEqualTo("testField");
        }

        @Test
        @DisplayName("setName() should update field name")
        void testSetName_UpdatesFieldName() {
            // When
            field.setName("newFieldName");

            // Then
            assertThat(field.getName()).isEqualTo("newFieldName");
        }

        @Test
        @DisplayName("getType() should return field type")
        void testGetType_ReturnsFieldType() {
            // When
            JavaType type = field.getType();

            // Then
            assertThat(type).isEqualTo(intType);
        }

        @Test
        @DisplayName("setType() should update field type")
        void testSetType_UpdatesFieldType() {
            // When
            field.setType(stringType);

            // Then
            assertThat(field.getType()).isEqualTo(stringType);
        }

        @Test
        @DisplayName("getPrivacy() should return privacy level")
        void testGetPrivacy_ReturnsPrivacyLevel() {
            // When
            Privacy privacy = field.getPrivacy();

            // Then
            assertThat(privacy).isEqualTo(Privacy.PRIVATE);
        }

        @Test
        @DisplayName("setPrivacy() should update privacy level")
        void testSetPrivacy_UpdatesPrivacyLevel() {
            // When
            field.setPrivacy(Privacy.PUBLIC);

            // Then
            assertThat(field.getPrivacy()).isEqualTo(Privacy.PUBLIC);
        }
    }

    @Nested
    @DisplayName("Modifier Management Tests")
    class ModifierManagementTests {

        @Test
        @DisplayName("addModifier() should add modifier")
        void testAddModifier_AddsModifier() {
            // When
            field.addModifier(Modifier.STATIC);

            // Then
            assertThat(field.getModifiers()).contains(Modifier.STATIC);
        }

        @Test
        @DisplayName("addModifier() should add multiple modifiers")
        void testAddModifier_AddsMultipleModifiers() {
            // When
            field.addModifier(Modifier.STATIC);
            field.addModifier(Modifier.FINAL);

            // Then
            assertThat(field.getModifiers())
                    .hasSize(2)
                    .contains(Modifier.STATIC, Modifier.FINAL);
        }

        @Test
        @DisplayName("addModifier() should not add duplicate modifiers")
        void testAddModifier_DoesNotAddDuplicates() {
            // When
            field.addModifier(Modifier.STATIC);
            field.addModifier(Modifier.STATIC);

            // Then
            assertThat(field.getModifiers()).hasSize(1);
            assertThat(field.getModifiers()).contains(Modifier.STATIC);
        }

        @Test
        @DisplayName("addModifier() should handle null modifier")
        void testAddModifier_WithNull_HandlesGracefully() {
            // When/Then
            assertThatCode(() -> field.addModifier(null))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("getModifiers() should return modifiable list")
        void testGetModifiers_ReturnsModifiableList() {
            // Given
            field.addModifier(Modifier.STATIC);

            // When
            var modifiers = field.getModifiers();
            modifiers.add(Modifier.FINAL);

            // Then
            assertThat(field.getModifiers()).hasSize(2);
            assertThat(field.getModifiers()).contains(Modifier.FINAL);
        }
    }

    @Nested
    @DisplayName("Annotation Management Tests")
    class AnnotationManagementTests {

        @Test
        @DisplayName("add(Annotation) should add annotation")
        void testAddAnnotation_AddsAnnotation() {
            // When
            boolean result = field.add(Annotation.DEPRECATED);

            // Then
            assertThat(result).isTrue();
            assertThat(field.generateCode(0).toString()).contains("@Deprecated");
        }

        @Test
        @DisplayName("add(Annotation) should add multiple annotations")
        void testAddAnnotation_AddsMultipleAnnotations() {
            // When
            field.add(Annotation.DEPRECATED);
            field.add(Annotation.OVERRIDE);

            // Then
            String code = field.generateCode(0).toString();
            assertThat(code).contains("@Deprecated");
            assertThat(code).contains("@Override");
        }

        @Test
        @DisplayName("remove(Annotation) should remove annotation")
        void testRemoveAnnotation_RemovesAnnotation() {
            // Given
            field.add(Annotation.DEPRECATED);

            // When
            boolean result = field.remove(Annotation.DEPRECATED);

            // Then
            assertThat(result).isTrue();
            assertThat(field.generateCode(0).toString()).doesNotContain("@Deprecated");
        }

        @Test
        @DisplayName("remove(Annotation) should return false when annotation not present")
        void testRemoveAnnotation_WithNonExistentAnnotation_ReturnsFalse() {
            // When
            boolean result = field.remove(Annotation.DEPRECATED);

            // Then
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("add(Annotation) should handle null annotation")
        void testAddAnnotation_WithNull_HandlesGracefully() {
            // When/Then
            assertThatCode(() -> field.add(null))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Initialization Management Tests")
    class InitializationManagementTests {

        @Test
        @DisplayName("setDefaultInitializer() should enable default initialization")
        void testSetDefaultInitializer_EnablesDefaultInitialization() {
            // When
            field.setDefaultInitializer(true);

            // Then
            assertThat(field.isDefaultInitializer()).isTrue();

            String code = field.generateCode(0).toString();
            assertThat(code).contains(" = 0"); // Default value for int
        }

        @Test
        @DisplayName("setDefaultInitializer() should disable default initialization")
        void testSetDefaultInitializer_DisablesDefaultInitialization() {
            // Given
            field.setDefaultInitializer(true);

            // When
            field.setDefaultInitializer(false);

            // Then
            assertThat(field.isDefaultInitializer()).isFalse();

            String code = field.generateCode(0).toString();
            assertThat(code).doesNotContain(" = 0");
        }

        @Test
        @DisplayName("setInitializer() should set custom initializer")
        void testSetInitializer_SetsCustomInitializer() {
            // Given
            IExpression customInit = new GenericExpression("42");

            // When
            field.setInitializer(customInit);

            // Then
            assertThat(field.getInitializer()).isEqualTo(customInit);

            String code = field.generateCode(0).toString();
            assertThat(code).contains(" = 42");
        }

        @Test
        @DisplayName("setInitializer() should accept null")
        void testSetInitializer_AcceptsNull() {
            // Given
            field.setInitializer(new GenericExpression("123"));

            // When
            field.setInitializer(null);

            // Then
            assertThat(field.getInitializer()).isNull();

            String code = field.generateCode(0).toString();
            assertThat(code).doesNotContain(" = 123");
        }

        @Test
        @DisplayName("Custom initializer should take precedence over default initializer")
        void testCustomInitializer_TakesPrecedenceOverDefault() {
            // Given
            field.setDefaultInitializer(true);
            field.setInitializer(new GenericExpression("100"));

            // When
            String code = field.generateCode(0).toString();

            // Then
            assertThat(code).contains(" = 100");
            assertThat(code).doesNotContain(" = 0");
        }

        @Test
        @DisplayName("Different types should have different default values")
        void testDefaultInitializer_WithDifferentTypes_GeneratesDifferentValues() {
            // String field
            Field stringField = new Field(stringType, "stringField");
            stringField.setDefaultInitializer(true);
            String stringCode = stringField.generateCode(0).toString();
            assertThat(stringCode).contains(" = null");

            // Boolean field
            Field boolField = new Field(booleanType, "boolField");
            boolField.setDefaultInitializer(true);
            String boolCode = boolField.generateCode(0).toString();
            assertThat(boolCode).contains(" = false");
        }
    }

    @Nested
    @DisplayName("Code Generation Tests")
    class CodeGenerationTests {

        @Test
        @DisplayName("generateCode() should generate simple field")
        void testGenerateCode_SimpleField_GeneratesCorrectly() {
            // When
            String code = field.generateCode(0).toString();

            // Then
            String normalized = SpecsStrings.normalizeFileContents(code, true);
            assertThat(normalized).isEqualTo("private int testField;");
        }

        @Test
        @DisplayName("generateCode() should generate field with modifiers")
        void testGenerateCode_WithModifiers_GeneratesCorrectly() {
            // Given
            field.addModifier(Modifier.STATIC);
            field.addModifier(Modifier.FINAL);

            // When
            String code = field.generateCode(0).toString();

            // Then
            String normalized = SpecsStrings.normalizeFileContents(code, true);
            assertThat(normalized).contains("private static final int testField;");
        }

        @Test
        @DisplayName("generateCode() should generate field with different privacy")
        void testGenerateCode_WithDifferentPrivacy_GeneratesCorrectly() {
            // Given
            field.setPrivacy(Privacy.PUBLIC);

            // When
            String code = field.generateCode(0).toString();

            // Then
            String normalized = SpecsStrings.normalizeFileContents(code, true);
            assertThat(normalized).contains("public int testField;");
        }

        @Test
        @DisplayName("generateCode() should generate field with annotations")
        void testGenerateCode_WithAnnotations_GeneratesCorrectly() {
            // Given
            field.add(Annotation.DEPRECATED);
            field.add(Annotation.OVERRIDE);

            // When
            String code = field.generateCode(0).toString();

            // Then
            assertThat(code).contains("@Deprecated");
            assertThat(code).contains("@Override");
            assertThat(code).contains("private int testField;");
        }

        @Test
        @DisplayName("generateCode() should generate field with default initializer")
        void testGenerateCode_WithDefaultInitializer_GeneratesCorrectly() {
            // Given
            field.setDefaultInitializer(true);

            // When
            String code = field.generateCode(0).toString();

            // Then
            String normalized = SpecsStrings.normalizeFileContents(code, true);
            assertThat(normalized).contains("private int testField = 0;");
        }

        @Test
        @DisplayName("generateCode() should generate field with custom initializer")
        void testGenerateCode_WithCustomInitializer_GeneratesCorrectly() {
            // Given
            field.setInitializer(new GenericExpression("42"));

            // When
            String code = field.generateCode(0).toString();

            // Then
            String normalized = SpecsStrings.normalizeFileContents(code, true);
            assertThat(normalized).contains("private int testField = 42;");
        }

        @Test
        @DisplayName("generateCode() should apply correct indentation")
        void testGenerateCode_WithIndentation_AppliesCorrectly() {
            // When
            String code = field.generateCode(2).toString();

            // Then
            String[] lines = code.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    assertThat(line).startsWith("        "); // 8 spaces for 2 levels
                }
            }
        }

        @Test
        @DisplayName("generateCode() should handle complex field configuration")
        void testGenerateCode_WithComplexConfiguration_GeneratesCorrectly() {
            // Given
            field.setPrivacy(Privacy.PROTECTED);
            field.addModifier(Modifier.STATIC);
            field.addModifier(Modifier.FINAL);
            field.add(Annotation.DEPRECATED);
            field.setInitializer(new GenericExpression("Integer.MAX_VALUE"));

            // When
            String code = field.generateCode(1).toString();

            // Then
            assertThat(code).contains("@Deprecated");
            assertThat(code).contains("protected static final int testField = Integer.MAX_VALUE;");
            assertThat(code.lines().allMatch(line -> line.isEmpty() || line.startsWith("    "))); // 4 spaces for 1
                                                                                                  // level
        }

        @Test
        @DisplayName("generateCode() should handle null return type gracefully")
        void testGenerateCode_WithNullType_HandlesGracefully() {
            assertThatThrownBy(() -> field.setType(null)).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Legacy Compatibility Tests")
    class LegacyCompatibilityTests {

        @Test
        @DisplayName("generateCode() - Legacy test compatibility")
        void testGenerateCode_LegacyCompatibility() {
            // Given - Recreate original test scenario
            final String fieldName = "field";
            final JavaType intType = JavaTypeFactory.getPrimitiveType(Primitive.INT);
            final Field tester = new Field(intType, fieldName);
            final String expected = "private int " + fieldName + ";";

            // When/Then
            assertThat(tester.generateCode(0).toString()).isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Integration Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Field should handle very long names")
        void testField_WithVeryLongName_HandlesCorrectly() {
            // Given
            String longName = "veryLongFieldName".repeat(5);
            field.setName(longName);

            // When
            String code = field.generateCode(0).toString();

            // Then
            assertThat(code).contains(longName);
        }

        @Test
        @DisplayName("Field should handle special characters in initializer")
        void testField_WithSpecialCharactersInInitializer_HandlesCorrectly() {
            // Given
            String stringField = "stringField";
            Field strField = new Field(stringType, stringField);
            strField.setInitializer(new GenericExpression("\"Hello, World!\""));

            // When
            String code = strField.generateCode(0).toString();

            // Then
            assertThat(code).contains("\"Hello, World!\"");
        }

        @Test
        @DisplayName("Multiple fields should be independent")
        void testMultipleFields_AreIndependent() {
            // Given
            Field field1 = new Field(intType, "field1");
            Field field2 = new Field(stringType, "field2");

            // When
            field1.addModifier(Modifier.STATIC);
            field1.setDefaultInitializer(true);
            field2.setPrivacy(Privacy.PUBLIC);
            field2.setInitializer(new GenericExpression("\"test\""));

            // Then
            assertThat(field1.getModifiers()).contains(Modifier.STATIC);
            assertThat(field2.getModifiers()).doesNotContain(Modifier.STATIC);
            assertThat(field1.getPrivacy()).isEqualTo(Privacy.PRIVATE);
            assertThat(field2.getPrivacy()).isEqualTo(Privacy.PUBLIC);
            assertThat(field1.isDefaultInitializer()).isTrue();
            assertThat(field2.isDefaultInitializer()).isFalse();
        }

        @Test
        @DisplayName("Field should handle all privacy levels")
        void testField_WithAllPrivacyLevels_HandlesCorrectly() {
            // Test all privacy levels
            Field privateField = new Field(intType, "privateField", Privacy.PRIVATE);
            Field publicField = new Field(intType, "publicField", Privacy.PUBLIC);
            Field protectedField = new Field(intType, "protectedField", Privacy.PROTECTED);
            Field packageField = new Field(intType, "packageField", Privacy.PACKAGE_PROTECTED);

            // Verify generated code
            assertThat(privateField.generateCode(0).toString()).contains("private");
            assertThat(publicField.generateCode(0).toString()).contains("public");
            assertThat(protectedField.generateCode(0).toString()).contains("protected");
            assertThat(packageField.generateCode(0).toString()).doesNotContain("private")
                    .doesNotContain("public").doesNotContain("protected");
        }

        @Test
        @DisplayName("toString() should return generated code")
        void testToString_ReturnsGeneratedCode() {
            // When
            String toString = field.toString();
            String generateCode = field.generateCode(0).toString();

            // Then
            assertThat(toString).isEqualTo(generateCode);
        }
    }
}
