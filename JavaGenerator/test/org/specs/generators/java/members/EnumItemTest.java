package org.specs.generators.java.members;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link EnumItem} - Enum item generation functionality.
 * Tests enum item creation, parameter management, equality, hashing,
 * and code generation for Java enum constants.
 * 
 * @author Generated Tests
 */
@DisplayName("EnumItem Tests")
public class EnumItemTest {

    private EnumItem enumItem;

    @BeforeEach
    void setUp() {
        enumItem = new EnumItem("TEST_ITEM");
    }

    @Nested
    @DisplayName("EnumItem Creation Tests")
    class EnumItemCreationTests {

        @Test
        @DisplayName("Constructor should create enum item with correct name")
        void testConstructor_CreatesEnumItemCorrectly() {
            // When (enumItem created in setUp)

            // Then
            assertThat(enumItem.getName()).isEqualTo("TEST_ITEM");
            assertThat(enumItem.getParameters()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Constructor should handle null name")
        void testConstructor_WithNullName_AcceptsNull() {
            // When/Then
            assertThatCode(() -> new EnumItem(null))
                    .doesNotThrowAnyException();

            EnumItem nullNameItem = new EnumItem(null);
            assertThat(nullNameItem.getName()).isNull();
            assertThat(nullNameItem.getParameters()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Constructor should handle empty name")
        void testConstructor_WithEmptyName_AcceptsEmpty() {
            // When
            EnumItem emptyNameItem = new EnumItem("");

            // Then
            assertThat(emptyNameItem.getName()).isEmpty();
            assertThat(emptyNameItem.getParameters()).isNotNull().isEmpty();
        }
    }

    @Nested
    @DisplayName("Property Management Tests")
    class PropertyManagementTests {

        @Test
        @DisplayName("getName() should return correct name")
        void testGetName_ReturnsCorrectName() {
            // When
            String name = enumItem.getName();

            // Then
            assertThat(name).isEqualTo("TEST_ITEM");
        }

        @Test
        @DisplayName("setName() should update name")
        void testSetName_UpdatesName() {
            // When
            enumItem.setName("NEW_NAME");

            // Then
            assertThat(enumItem.getName()).isEqualTo("NEW_NAME");
        }

        @Test
        @DisplayName("setName() should accept null")
        void testSetName_AcceptsNull() {
            // When
            enumItem.setName(null);

            // Then
            assertThat(enumItem.getName()).isNull();
        }

        @Test
        @DisplayName("getParameters() should return modifiable list")
        void testGetParameters_ReturnsModifiableList() {
            // When
            List<String> parameters = enumItem.getParameters();

            // Then
            assertThat(parameters).isNotNull().isEmpty();

            // Should be modifiable
            assertThatCode(() -> parameters.add("test"))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("setParameters() should replace parameter list")
        void testSetParameters_ReplacesParameterList() {
            // Given
            List<String> newParams = Arrays.asList("param1", "param2");

            // When
            enumItem.setParameters(newParams);

            // Then
            assertThat(enumItem.getParameters())
                    .hasSize(2)
                    .containsExactly("param1", "param2");
        }

        @Test
        @DisplayName("setParameters() should accept null")
        void testSetParameters_AcceptsNull() {
            // When
            enumItem.setParameters(null);

            // Then
            assertThat(enumItem.getParameters()).isNull();
        }
    }

    @Nested
    @DisplayName("Parameter Management Tests")
    class ParameterManagementTests {

        @Test
        @DisplayName("addParameter() should add parameter to list")
        void testAddParameter_AddsParameterToList() {
            // When
            enumItem.addParameter("value1");

            // Then
            assertThat(enumItem.getParameters())
                    .hasSize(1)
                    .containsExactly("value1");
        }

        @Test
        @DisplayName("addParameter() should add multiple parameters in order")
        void testAddParameter_AddsMultipleParameters() {
            // When
            enumItem.addParameter("value1");
            enumItem.addParameter("value2");
            enumItem.addParameter("value3");

            // Then
            assertThat(enumItem.getParameters())
                    .hasSize(3)
                    .containsExactly("value1", "value2", "value3");
        }

        @Test
        @DisplayName("addParameter() should accept null values")
        void testAddParameter_AcceptsNull() {
            // When
            enumItem.addParameter(null);

            // Then
            assertThat(enumItem.getParameters())
                    .hasSize(1)
                    .containsExactly((String) null);
        }

        @Test
        @DisplayName("addParameter() should accept empty strings")
        void testAddParameter_AcceptsEmptyString() {
            // When
            enumItem.addParameter("");

            // Then
            assertThat(enumItem.getParameters())
                    .hasSize(1)
                    .containsExactly("");
        }

        @Test
        @DisplayName("addParameter() should handle complex parameter values")
        void testAddParameter_HandlesComplexValues() {
            // When
            enumItem.addParameter("\"string value\"");
            enumItem.addParameter("123");
            enumItem.addParameter("SomeClass.CONSTANT");

            // Then
            assertThat(enumItem.getParameters())
                    .hasSize(3)
                    .containsExactly("\"string value\"", "123", "SomeClass.CONSTANT");
        }
    }

    @Nested
    @DisplayName("Code Generation Tests")
    class CodeGenerationTests {

        @Test
        @DisplayName("generateCode() should generate simple enum item without parameters")
        void testGenerateCode_WithoutParameters_GeneratesSimpleItem() {
            // When
            String code = enumItem.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("TEST_ITEM");
        }

        @Test
        @DisplayName("generateCode() should generate enum item with single parameter")
        void testGenerateCode_WithSingleParameter_GeneratesWithParentheses() {
            // Given
            enumItem.addParameter("\"value\"");

            // When
            String code = enumItem.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("TEST_ITEM(\"value\")");
        }

        @Test
        @DisplayName("generateCode() should generate enum item with multiple parameters")
        void testGenerateCode_WithMultipleParameters_GeneratesCommaSeparated() {
            // Given
            enumItem.addParameter("\"first\"");
            enumItem.addParameter("123");
            enumItem.addParameter("true");

            // When
            String code = enumItem.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("TEST_ITEM(\"first\", 123, true)");
        }

        @Test
        @DisplayName("generateCode() should apply correct indentation")
        void testGenerateCode_WithIndentation_AppliesCorrectly() {
            // When
            String code = enumItem.generateCode(2).toString();

            // Then
            assertThat(code).startsWith("        TEST_ITEM"); // 8 spaces for 2 levels
        }

        @Test
        @DisplayName("generateCode() should handle null parameter in list")
        void testGenerateCode_WithNullParameter_IncludesNull() {
            // Given
            enumItem.addParameter("\"valid\"");
            enumItem.addParameter(null);
            enumItem.addParameter("123");

            // When
            String code = enumItem.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("TEST_ITEM(\"valid\", null, 123)");
        }

        @Test
        @DisplayName("generateCode() should handle empty parameter in list")
        void testGenerateCode_WithEmptyParameter_IncludesEmpty() {
            // Given
            enumItem.addParameter("\"valid\"");
            enumItem.addParameter("");
            enumItem.addParameter("123");

            // When
            String code = enumItem.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("TEST_ITEM(\"valid\", , 123)");
        }

        @Test
        @DisplayName("generateCode() should handle null name")
        void testGenerateCode_WithNullName_IncludesNull() {
            // Given
            enumItem.setName(null);

            // When
            String code = enumItem.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("null");
        }
    }

    @Nested
    @DisplayName("Equality and Hashing Tests")
    class EqualityAndHashingTests {

        @Test
        @DisplayName("equals() should return true for same instance")
        void testEquals_SameInstance_ReturnsTrue() {
            // When/Then
            assertThat(enumItem.equals(enumItem)).isTrue();
        }

        @Test
        @DisplayName("equals() should return true for same name")
        void testEquals_SameName_ReturnsTrue() {
            // Given
            EnumItem other = new EnumItem("TEST_ITEM");

            // When/Then
            assertThat(enumItem.equals(other)).isTrue();
            assertThat(other.equals(enumItem)).isTrue();
        }

        @Test
        @DisplayName("equals() should return false for different names")
        void testEquals_DifferentNames_ReturnsFalse() {
            // Given
            EnumItem other = new EnumItem("DIFFERENT_ITEM");

            // When/Then
            assertThat(enumItem.equals(other)).isFalse();
            assertThat(other.equals(enumItem)).isFalse();
        }

        @Test
        @DisplayName("equals() should return false for null")
        void testEquals_WithNull_ReturnsFalse() {
            // When/Then
            assertThat(enumItem.equals(null)).isFalse();
        }

        @Test
        @DisplayName("equals() should return false for different class")
        void testEquals_DifferentClass_ReturnsFalse() {
            // When/Then
            assertThat(enumItem.equals("TEST_ITEM")).isFalse();
        }

        @Test
        @DisplayName("equals() should handle null names correctly")
        void testEquals_WithNullNames_HandlesCorrectly() {
            // Given
            EnumItem item1 = new EnumItem(null);
            EnumItem item2 = new EnumItem(null);
            EnumItem item3 = new EnumItem("NOT_NULL");

            // When/Then
            assertThat(item1.equals(item2)).isTrue();
            assertThat(item1.equals(item3)).isFalse();
            assertThat(item3.equals(item1)).isFalse();
        }

        @Test
        @DisplayName("equals() should ignore parameters in comparison")
        void testEquals_IgnoresParameters() {
            // Given
            EnumItem item1 = new EnumItem("TEST_ITEM");
            EnumItem item2 = new EnumItem("TEST_ITEM");
            item1.addParameter("param1");
            item2.addParameter("param2");

            // When/Then
            assertThat(item1.equals(item2)).isTrue();
        }

        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void testHashCode_SameForEqualObjects() {
            // Given
            EnumItem other = new EnumItem("TEST_ITEM");

            // When/Then
            assertThat(enumItem.hashCode()).isEqualTo(other.hashCode());
        }

        @Test
        @DisplayName("hashCode() should handle null name")
        void testHashCode_WithNullName_HandlesGracefully() {
            // Given
            EnumItem nullItem = new EnumItem(null);

            // When/Then
            assertThatCode(() -> nullItem.hashCode())
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("hashCode() should be consistent")
        void testHashCode_IsConsistent() {
            // When
            int hash1 = enumItem.hashCode();
            int hash2 = enumItem.hashCode();

            // Then
            assertThat(hash1).isEqualTo(hash2);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Integration Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("EnumItem should handle very long names")
        void testEnumItem_WithVeryLongName_HandlesCorrectly() {
            // Given
            String longName = "A".repeat(1000);
            EnumItem longNameItem = new EnumItem(longName);

            // When
            String code = longNameItem.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo(longName);
        }

        @Test
        @DisplayName("EnumItem should handle many parameters")
        void testEnumItem_WithManyParameters_HandlesCorrectly() {
            // Given
            for (int i = 0; i < 100; i++) {
                enumItem.addParameter("param" + i);
            }

            // When
            String code = enumItem.generateCode(0).toString();

            // Then
            assertThat(code).startsWith("TEST_ITEM(param0, param1,");
            assertThat(code).contains("param99)");
            assertThat(enumItem.getParameters()).hasSize(100);
        }

        @Test
        @DisplayName("EnumItem should handle special characters in name")
        void testEnumItem_WithSpecialCharacters_HandlesCorrectly() {
            // Given
            enumItem.setName("ITEM_WITH_UNDERSCORES_AND_123");

            // When
            String code = enumItem.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("ITEM_WITH_UNDERSCORES_AND_123");
        }

        @Test
        @DisplayName("EnumItem should handle complex parameter expressions")
        void testEnumItem_WithComplexParameters_HandlesCorrectly() {
            // Given
            enumItem.addParameter("new ArrayList<>()");
            enumItem.addParameter("SomeClass.CONSTANT.getValue()");
            enumItem.addParameter("\"string with \\\"quotes\\\"\"");

            // When
            String code = enumItem.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo(
                    "TEST_ITEM(new ArrayList<>(), SomeClass.CONSTANT.getValue(), \"string with \\\"quotes\\\"\")");
        }

        @Test
        @DisplayName("Multiple EnumItems should be independent")
        void testMultipleEnumItems_AreIndependent() {
            // Given
            EnumItem item1 = new EnumItem("ITEM1");
            EnumItem item2 = new EnumItem("ITEM2");

            // When
            item1.addParameter("param1");
            item2.addParameter("param2");

            // Then
            assertThat(item1.getParameters()).containsExactly("param1");
            assertThat(item2.getParameters()).containsExactly("param2");
        }
    }
}
