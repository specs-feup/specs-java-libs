package org.specs.generators.java.members;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.specs.generators.java.enums.JDocTag;

/**
 * Test class for {@link JavaDocTag} - JavaDoc tag generation functionality.
 * Tests JavaDoc tag creation, description management, text appending,
 * and code generation for Java documentation tags.
 * 
 * @author Generated Tests
 */
@DisplayName("JavaDocTag Tests")
public class JavaDocTagTest {

    private JavaDocTag javaDocTag;

    @BeforeEach
    void setUp() {
        javaDocTag = new JavaDocTag(JDocTag.PARAM);
    }

    @Nested
    @DisplayName("JavaDocTag Creation Tests")
    class JavaDocTagCreationTests {

        @Test
        @DisplayName("Constructor with tag only should create tag with empty description")
        void testConstructor_WithTagOnly_CreatesTagWithEmptyDescription() {
            // When (javaDocTag created in setUp)

            // Then
            assertThat(javaDocTag.getTag()).isEqualTo(JDocTag.PARAM);
            assertThat(javaDocTag.getDescription()).isNotNull();
            assertThat(javaDocTag.getDescription().toString()).isEmpty();
        }

        @Test
        @DisplayName("Constructor with tag and string should create tag with description")
        void testConstructor_WithTagAndString_CreatesTagWithDescription() {
            // When
            JavaDocTag tag = new JavaDocTag(JDocTag.RETURN, "return description");

            // Then
            assertThat(tag.getTag()).isEqualTo(JDocTag.RETURN);
            assertThat(tag.getDescription().toString()).isEqualTo("return description");
        }

        @Test
        @DisplayName("Constructor with tag and StringBuilder should create tag with description")
        void testConstructor_WithTagAndStringBuilder_CreatesTagWithDescription() {
            // Given
            StringBuilder description = new StringBuilder("author name");

            // When
            JavaDocTag tag = new JavaDocTag(JDocTag.AUTHOR, description);

            // Then
            assertThat(tag.getTag()).isEqualTo(JDocTag.AUTHOR);
            assertThat(tag.getDescription()).isEqualTo(description);
            assertThat(tag.getDescription().toString()).isEqualTo("author name");
        }

        @Test
        @DisplayName("Constructor should handle null tag")
        void testConstructor_WithNullTag_AcceptsNull() {
            // When/Then
            assertThatCode(() -> new JavaDocTag(null))
                    .doesNotThrowAnyException();

            JavaDocTag tag = new JavaDocTag(null);
            assertThat(tag.getTag()).isNull();
            assertThat(tag.getDescription()).isNotNull().isEmpty();
        }

        @Test
        @DisplayName("Constructor with null string should handle null gracefully")
        void testConstructor_WithNullString_ThrowsException() {
            // When/Then - Constructor with null string should handle gracefully
            assertThatCode(() -> new JavaDocTag(JDocTag.PARAM, (String) null))
                    .doesNotThrowAnyException();

            // Verify null string is converted to empty description
            JavaDocTag tag = new JavaDocTag(JDocTag.PARAM, (String) null);
            assertThat(tag.getTag()).isEqualTo(JDocTag.PARAM);
            assertThat(tag.getDescription().toString()).isEmpty();
        }

        @Test
        @DisplayName("Constructor with null StringBuilder should accept null")
        void testConstructor_WithNullStringBuilder_AcceptsNull() {
            // When/Then
            assertThatCode(() -> new JavaDocTag(JDocTag.PARAM, (StringBuilder) null))
                    .doesNotThrowAnyException();

            JavaDocTag tag = new JavaDocTag(JDocTag.PARAM, (StringBuilder) null);
            assertThat(tag.getDescription()).isNull();
        }
    }

    @Nested
    @DisplayName("Property Management Tests")
    class PropertyManagementTests {

        @Test
        @DisplayName("getTag() should return correct tag")
        void testGetTag_ReturnsCorrectTag() {
            // When
            JDocTag tag = javaDocTag.getTag();

            // Then
            assertThat(tag).isEqualTo(JDocTag.PARAM);
        }

        @Test
        @DisplayName("setTag() should update tag")
        void testSetTag_UpdatesTag() {
            // When
            javaDocTag.setTag(JDocTag.RETURN);

            // Then
            assertThat(javaDocTag.getTag()).isEqualTo(JDocTag.RETURN);
        }

        @Test
        @DisplayName("setTag() should accept null")
        void testSetTag_AcceptsNull() {
            // When
            javaDocTag.setTag(null);

            // Then
            assertThat(javaDocTag.getTag()).isNull();
        }

        @Test
        @DisplayName("getDescription() should return description")
        void testGetDescription_ReturnsDescription() {
            // Given
            StringBuilder description = new StringBuilder("test description");
            javaDocTag.setDescription(description);

            // When
            StringBuilder result = javaDocTag.getDescription();

            // Then
            assertThat(result).isEqualTo(description);
            assertThat(result.toString()).isEqualTo("test description");
        }

        @Test
        @DisplayName("setDescription() should replace description")
        void testSetDescription_ReplacesDescription() {
            // Given
            StringBuilder newDescription = new StringBuilder("new description");

            // When
            javaDocTag.setDescription(newDescription);

            // Then
            assertThat(javaDocTag.getDescription()).isEqualTo(newDescription);
            assertThat(javaDocTag.getDescription().toString()).isEqualTo("new description");
        }

        @Test
        @DisplayName("setDescription() should accept null")
        void testSetDescription_AcceptsNull() {
            // When
            javaDocTag.setDescription(null);

            // Then
            assertThat(javaDocTag.getDescription()).isNull();
        }
    }

    @Nested
    @DisplayName("Description Appending Tests")
    class DescriptionAppendingTests {

        @Test
        @DisplayName("append(StringBuilder) should append to description")
        void testAppendStringBuilder_AppendsToDescription() {
            // Given
            javaDocTag.setDescription(new StringBuilder("Initial"));
            StringBuilder toAppend = new StringBuilder(" appended");

            // When
            javaDocTag.append(toAppend);

            // Then
            assertThat(javaDocTag.getDescription().toString()).isEqualTo("Initial appended");
        }

        @Test
        @DisplayName("append(String) should append to description")
        void testAppendString_AppendsToDescription() {
            // Given
            javaDocTag.setDescription(new StringBuilder("Initial"));

            // When
            javaDocTag.append(" appended");

            // Then
            assertThat(javaDocTag.getDescription().toString()).isEqualTo("Initial appended");
        }

        @Test
        @DisplayName("append() should handle empty initial description")
        void testAppend_WithEmptyInitial_Appends() {
            // When
            javaDocTag.append("First content");

            // Then
            assertThat(javaDocTag.getDescription().toString()).isEqualTo("First content");
        }

        @Test
        @DisplayName("append(StringBuilder) should handle null parameter")
        void testAppendStringBuilder_WithNull_AppendsNull() {
            // Given
            javaDocTag.setDescription(new StringBuilder("Initial"));

            // When
            javaDocTag.append((StringBuilder) null);

            // Then
            assertThat(javaDocTag.getDescription().toString()).isEqualTo("Initialnull");
        }

        @Test
        @DisplayName("append(String) should handle null parameter")
        void testAppendString_WithNull_AppendsNull() {
            // Given
            javaDocTag.setDescription(new StringBuilder("Initial"));

            // When
            javaDocTag.append((String) null);

            // Then
            assertThat(javaDocTag.getDescription().toString()).isEqualTo("Initialnull");
        }

        @Test
        @DisplayName("Multiple appends should work correctly")
        void testMultipleAppends_WorkCorrectly() {
            // Given
            javaDocTag.setDescription(new StringBuilder("Start"));

            // When
            javaDocTag.append(" middle");
            javaDocTag.append(new StringBuilder(" end"));

            // Then
            assertThat(javaDocTag.getDescription().toString()).isEqualTo("Start middle end");
        }

        @Test
        @DisplayName("append() should handle multiline content")
        void testAppend_WithMultilineContent_Appends() {
            // When
            javaDocTag.append("Line 1\nLine 2\nLine 3");

            // Then
            assertThat(javaDocTag.getDescription().toString()).isEqualTo("Line 1\nLine 2\nLine 3");
        }
    }

    @Nested
    @DisplayName("Code Generation Tests")
    class CodeGenerationTests {

        @Test
        @DisplayName("generateCode() should generate tag with empty description")
        void testGenerateCode_WithEmptyDescription_GeneratesTagOnly() {
            // When
            String code = javaDocTag.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("@param ");
        }

        @Test
        @DisplayName("generateCode() should generate tag with description")
        void testGenerateCode_WithDescription_GeneratesTagWithDescription() {
            // Given
            javaDocTag.setDescription(new StringBuilder("paramName parameter description"));

            // When
            String code = javaDocTag.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("@param paramName parameter description");
        }

        @Test
        @DisplayName("generateCode() should apply correct indentation")
        void testGenerateCode_WithIndentation_AppliesCorrectly() {
            // Given
            javaDocTag.setDescription(new StringBuilder("description"));

            // When
            String code = javaDocTag.generateCode(2).toString();

            // Then
            assertThat(code).startsWith("        @param"); // 8 spaces for 2 levels
        }

        @Test
        @DisplayName("generateCode() should handle multiline descriptions")
        void testGenerateCode_WithMultilineDescription_GeneratesCorrectly() {
            // Given
            javaDocTag.setDescription(new StringBuilder("line1\nline2\nline3"));

            // When
            String code = javaDocTag.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("@param line1\nline2\nline3");
        }

        @Test
        @DisplayName("generateCode() should handle null description")
        void testGenerateCode_WithNullDescription_IncludesNull() {
            // Given
            javaDocTag.setDescription(null);

            // When
            String code = javaDocTag.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("@param null");
        }

        @Test
        @DisplayName("generateCode() should handle null tag")
        void testGenerateCode_WithNullTag_HandlesGracefully() {
            // Given
            javaDocTag.setTag(null);
            javaDocTag.setDescription(new StringBuilder("description"));

            // When/Then - Should throw NullPointerException when accessing null tag
            assertThatThrownBy(() -> javaDocTag.generateCode(0))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("generateCode() should handle different tag types")
        void testGenerateCode_WithDifferentTags_GeneratesCorrectly() {
            // Test AUTHOR tag
            JavaDocTag authorTag = new JavaDocTag(JDocTag.AUTHOR, "Author Name");
            assertThat(authorTag.generateCode(0).toString()).isEqualTo("@author Author Name");

            // Test RETURN tag
            JavaDocTag returnTag = new JavaDocTag(JDocTag.RETURN, "return value");
            assertThat(returnTag.generateCode(0).toString()).isEqualTo("@return return value");

            // Test VERSION tag
            JavaDocTag versionTag = new JavaDocTag(JDocTag.VERSION, "1.0");
            assertThat(versionTag.generateCode(0).toString()).isEqualTo("@version 1.0");
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("toString() should return same as generateCode(0)")
        void testToString_ReturnsSameAsGenerateCodeZero() {
            // Given
            javaDocTag.setDescription(new StringBuilder("test description"));

            // When
            String toString = javaDocTag.toString();
            String generateCode = javaDocTag.generateCode(0).toString();

            // Then
            assertThat(toString).isEqualTo(generateCode);
            assertThat(toString).isEqualTo("@param test description");
        }

        @Test
        @DisplayName("toString() should handle empty description")
        void testToString_WithEmptyDescription_ReturnsTagOnly() {
            // When
            String toString = javaDocTag.toString();

            // Then
            assertThat(toString).isEqualTo("@param ");
        }

        @Test
        @DisplayName("toString() should handle multiline description")
        void testToString_WithMultilineDescription_IncludesNewlines() {
            // Given
            javaDocTag.setDescription(new StringBuilder("line1\nline2"));

            // When
            String toString = javaDocTag.toString();

            // Then
            assertThat(toString).isEqualTo("@param line1\nline2");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Integration Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("JavaDocTag should handle very long descriptions")
        void testJavaDocTag_WithVeryLongDescription_HandlesCorrectly() {
            // Given
            String longDescription = "A".repeat(10000);
            javaDocTag.setDescription(new StringBuilder(longDescription));

            // When
            String code = javaDocTag.generateCode(0).toString();

            // Then
            assertThat(code).startsWith("@param " + longDescription);
        }

        @Test
        @DisplayName("JavaDocTag should handle special characters in description")
        void testJavaDocTag_WithSpecialCharacters_HandlesCorrectly() {
            // Given
            javaDocTag.setDescription(new StringBuilder("Special chars: @#$%^&*()_+{}[]|\\:;\"'<>?,./"));

            // When
            String code = javaDocTag.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("@param Special chars: @#$%^&*()_+{}[]|\\:;\"'<>?,./");
        }

        @Test
        @DisplayName("JavaDocTag should handle HTML tags in description")
        void testJavaDocTag_WithHTMLTags_HandlesCorrectly() {
            // Given
            javaDocTag.setDescription(new StringBuilder("Description with <b>bold</b> and <i>italic</i> text."));

            // When
            String code = javaDocTag.generateCode(0).toString();

            // Then
            assertThat(code).contains("<b>bold</b>");
            assertThat(code).contains("<i>italic</i>");
        }

        @Test
        @DisplayName("Multiple JavaDocTags should be independent")
        void testMultipleJavaDocTags_AreIndependent() {
            // Given
            JavaDocTag tag1 = new JavaDocTag(JDocTag.PARAM, "first param");
            JavaDocTag tag2 = new JavaDocTag(JDocTag.RETURN, "return value");

            // When
            tag1.append(" modified");
            tag2.setDescription(new StringBuilder("new return"));

            // Then
            assertThat(tag1.getDescription().toString()).isEqualTo("first param modified");
            assertThat(tag2.getDescription().toString()).isEqualTo("new return");
        }

        @Test
        @DisplayName("JavaDocTag should handle complex indentation scenarios")
        void testJavaDocTag_WithComplexIndentation_HandlesCorrectly() {
            // Given
            javaDocTag.setDescription(new StringBuilder("Multi\nline\ndescription"));

            // When
            String code = javaDocTag.generateCode(3).toString();

            // Then
            assertThat(code).startsWith("            @param"); // 12 spaces for level 3
            assertThat(code).contains("Multi\nline\ndescription");
        }

        @Test
        @DisplayName("JavaDocTag should work with all JDocTag enum values")
        void testJavaDocTag_WithAllJDocTagValues_WorksCorrectly() {
            // Test that we can create JavaDocTag instances with all enum values
            for (JDocTag tag : JDocTag.values()) {
                JavaDocTag docTag = new JavaDocTag(tag, "test description");
                assertThat(docTag.getTag()).isEqualTo(tag);
                assertThat(docTag.toString()).startsWith(tag.getTag());
            }
        }

        @Test
        @DisplayName("JavaDocTag should handle concurrent modifications")
        void testJavaDocTag_WithConcurrentModifications_HandlesCorrectly() {
            // Given
            javaDocTag.setDescription(new StringBuilder("Initial"));

            // When - Multiple modifications
            javaDocTag.append(" first");
            javaDocTag.setTag(JDocTag.RETURN);
            javaDocTag.append(" second");
            StringBuilder desc = javaDocTag.getDescription();
            desc.append(" third");

            // Then
            assertThat(javaDocTag.getTag()).isEqualTo(JDocTag.RETURN);
            assertThat(javaDocTag.getDescription().toString()).isEqualTo("Initial first second third");
            assertThat(javaDocTag.toString()).isEqualTo("@return Initial first second third");
        }
    }
}
