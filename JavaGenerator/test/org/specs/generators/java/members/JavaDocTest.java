package org.specs.generators.java.members;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.specs.generators.java.enums.JDocTag;

/**
 * Test class for {@link JavaDoc} - JavaDoc comment generation functionality.
 * Tests JavaDoc comment creation, tag management, text handling,
 * and code generation for Java documentation comments.
 * 
 * @author Generated Tests
 */
@DisplayName("JavaDoc Tests")
public class JavaDocTest {

    private JavaDoc javaDoc;

    @BeforeEach
    void setUp() {
        javaDoc = new JavaDoc();
    }

    @Nested
    @DisplayName("JavaDoc Creation Tests")
    class JavaDocCreationTests {

        @Test
        @DisplayName("Default constructor should create empty JavaDoc")
        void testDefaultConstructor_CreatesEmptyJavaDoc() {
            // When (javaDoc created in setUp)

            // Then
            assertThat(javaDoc.getComment()).isNotNull();
            assertThat(javaDoc.getComment().toString()).isEmpty();
        }

        @Test
        @DisplayName("StringBuilder constructor should create JavaDoc with comment")
        void testStringBuilderConstructor_CreatesWithComment() {
            // Given
            StringBuilder comment = new StringBuilder("Test comment");

            // When
            JavaDoc doc = new JavaDoc(comment);

            // Then
            assertThat(doc.getComment()).isEqualTo(comment);
            assertThat(doc.getComment().toString()).isEqualTo("Test comment");
        }

        @Test
        @DisplayName("String constructor should create JavaDoc with comment")
        void testStringConstructor_CreatesWithComment() {
            // Given
            String comment = "Test comment";

            // When
            JavaDoc doc = new JavaDoc(comment);

            // Then
            assertThat(doc.getComment().toString()).isEqualTo("Test comment");
        }

        @Test
        @DisplayName("StringBuilder constructor should handle null")
        void testStringBuilderConstructor_WithNull_AcceptsNull() {
            // When/Then
            assertThatCode(() -> new JavaDoc((StringBuilder) null))
                    .doesNotThrowAnyException();

            JavaDoc doc = new JavaDoc((StringBuilder) null);
            assertThat(doc.getComment()).isNull();
        }

        @Test
        @DisplayName("String constructor should handle null")
        void testStringConstructor_WithNull_ThrowsException() {
            // When/Then - String constructor should handle null gracefully
            assertThatCode(() -> new JavaDoc((String) null))
                    .doesNotThrowAnyException();

            // Verify null is converted to empty string
            JavaDoc javaDoc = new JavaDoc((String) null);
            assertThat(javaDoc.getComment().toString()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Comment Management Tests")
    class CommentManagementTests {

        @Test
        @DisplayName("getComment() should return comment StringBuilder")
        void testGetComment_ReturnsComment() {
            // Given
            javaDoc.setComment(new StringBuilder("test"));

            // When
            StringBuilder comment = javaDoc.getComment();

            // Then
            assertThat(comment).isNotNull();
            assertThat(comment.toString()).isEqualTo("test");
        }

        @Test
        @DisplayName("setComment() should replace comment")
        void testSetComment_ReplacesComment() {
            // Given
            StringBuilder newComment = new StringBuilder("new comment");

            // When
            javaDoc.setComment(newComment);

            // Then
            assertThat(javaDoc.getComment()).isEqualTo(newComment);
            assertThat(javaDoc.getComment().toString()).isEqualTo("new comment");
        }

        @Test
        @DisplayName("setComment() should accept null")
        void testSetComment_AcceptsNull() {
            // When
            javaDoc.setComment(null);

            // Then
            assertThat(javaDoc.getComment()).isNull();
        }

        @Test
        @DisplayName("appendComment() should append to existing comment")
        void testAppendComment_AppendsToExisting() {
            // Given
            javaDoc.setComment(new StringBuilder("Initial"));

            // When
            StringBuilder result = javaDoc.appendComment(" appended");

            // Then
            assertThat(result).isEqualTo(javaDoc.getComment());
            assertThat(javaDoc.getComment().toString()).isEqualTo("Initial appended");
        }

        @Test
        @DisplayName("appendComment() should handle empty initial comment")
        void testAppendComment_WithEmptyInitial_Appends() {
            // When
            StringBuilder result = javaDoc.appendComment("First content");

            // Then
            assertThat(result).isEqualTo(javaDoc.getComment());
            assertThat(javaDoc.getComment().toString()).isEqualTo("First content");
        }

        @Test
        @DisplayName("appendComment() should handle null parameter")
        void testAppendComment_WithNull_AppendsNull() {
            // Given
            javaDoc.setComment(new StringBuilder("Initial"));

            // When
            StringBuilder result = javaDoc.appendComment(null);

            // Then
            assertThat(result).isEqualTo(javaDoc.getComment());
            assertThat(javaDoc.getComment().toString()).isEqualTo("Initialnull");
        }

        @Test
        @DisplayName("appendComment() should handle multiline content")
        void testAppendComment_WithMultiline_Appends() {
            // When
            javaDoc.appendComment("Line 1\nLine 2\nLine 3");

            // Then
            assertThat(javaDoc.getComment().toString()).isEqualTo("Line 1\nLine 2\nLine 3");
        }
    }

    @Nested
    @DisplayName("Tag Management Tests")
    class TagManagementTests {

        @Test
        @DisplayName("addTag() with JDocTag only should add tag without description")
        void testAddTag_WithJDocTagOnly_AddsTagWithoutDescription() {
            // When
            javaDoc.addTag(JDocTag.AUTHOR);

            // Then
            JavaDocTag tag = javaDoc.getTag(0);
            assertThat(tag.getTag()).isEqualTo(JDocTag.AUTHOR);
            assertThat(tag.getDescription()).isNotNull();
            assertThat(tag.getDescription().toString()).isEmpty();
        }

        @Test
        @DisplayName("addTag() with String description should add tag with description")
        void testAddTag_WithStringDescription_AddsTagWithDescription() {
            // When
            javaDoc.addTag(JDocTag.PARAM, "paramName description");

            // Then
            JavaDocTag tag = javaDoc.getTag(0);
            assertThat(tag.getTag()).isEqualTo(JDocTag.PARAM);
            assertThat(tag.getDescription().toString()).isEqualTo("paramName description");
        }

        @Test
        @DisplayName("addTag() with StringBuilder description should add tag with description")
        void testAddTag_WithStringBuilderDescription_AddsTagWithDescription() {
            // Given
            StringBuilder description = new StringBuilder("return value description");

            // When
            javaDoc.addTag(JDocTag.RETURN, description);

            // Then
            JavaDocTag tag = javaDoc.getTag(0);
            assertThat(tag.getTag()).isEqualTo(JDocTag.RETURN);
            assertThat(tag.getDescription()).isEqualTo(description);
            assertThat(tag.getDescription().toString()).isEqualTo("return value description");
        }

        @Test
        @DisplayName("addTag() should handle null string description")
        void testAddTag_WithNullStringDescription_ThrowsException() {
            // When/Then - addTag should handle null string description gracefully
            assertThatCode(() -> javaDoc.addTag(JDocTag.AUTHOR, (String) null))
                    .doesNotThrowAnyException();

            // Verify null string is converted to empty string
            javaDoc.addTag(JDocTag.AUTHOR, (String) null);
            JavaDocTag tag = javaDoc.getTag(0);
            assertThat(tag.getTag()).isEqualTo(JDocTag.AUTHOR);
            assertThat(tag.getDescription().toString()).isEmpty();
        }

        @Test
        @DisplayName("addTag() should handle null StringBuilder description")
        void testAddTag_WithNullStringBuilderDescription_AcceptsNull() {
            // When
            javaDoc.addTag(JDocTag.AUTHOR, (StringBuilder) null);

            // Then
            JavaDocTag tag = javaDoc.getTag(0);
            assertThat(tag.getTag()).isEqualTo(JDocTag.AUTHOR);
            assertThat(tag.getDescription()).isNull();
        }

        @Test
        @DisplayName("addTag() should add multiple tags in order")
        void testAddTag_MultipleTagsInOrder() {
            // When
            javaDoc.addTag(JDocTag.AUTHOR, "Author Name");
            javaDoc.addTag(JDocTag.PARAM, "param1 description");
            javaDoc.addTag(JDocTag.RETURN, "return description");

            // Then
            assertThat(javaDoc.getTag(0).getTag()).isEqualTo(JDocTag.AUTHOR);
            assertThat(javaDoc.getTag(1).getTag()).isEqualTo(JDocTag.PARAM);
            assertThat(javaDoc.getTag(2).getTag()).isEqualTo(JDocTag.RETURN);
        }

        @Test
        @DisplayName("getTag() should return correct tag at index")
        void testGetTag_ReturnsCorrectTagAtIndex() {
            // Given
            javaDoc.addTag(JDocTag.PARAM, "first param");
            javaDoc.addTag(JDocTag.PARAM, "second param");

            // When
            JavaDocTag tag = javaDoc.getTag(1);

            // Then
            assertThat(tag.getDescription().toString()).isEqualTo("second param");
        }

        @Test
        @DisplayName("getTag() should throw exception for invalid index")
        void testGetTag_WithInvalidIndex_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> javaDoc.getTag(0))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("removeTag() should remove and return tag at index")
        void testRemoveTag_RemovesAndReturnsTag() {
            // Given
            javaDoc.addTag(JDocTag.AUTHOR, "Author Name");
            javaDoc.addTag(JDocTag.PARAM, "param description");

            // When
            JavaDocTag removed = javaDoc.removeTag(0);

            // Then
            assertThat(removed.getTag()).isEqualTo(JDocTag.AUTHOR);
            assertThat(javaDoc.getTag(0).getTag()).isEqualTo(JDocTag.PARAM);
        }

        @Test
        @DisplayName("removeTag() should throw exception for invalid index")
        void testRemoveTag_WithInvalidIndex_ThrowsException() {
            // When/Then
            assertThatThrownBy(() -> javaDoc.removeTag(0))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("Code Generation Tests")
    class CodeGenerationTests {

        @Test
        @DisplayName("generateCode() should generate empty JavaDoc comment")
        void testGenerateCode_EmptyComment_GeneratesEmptyJavaDoc() {
            // When
            String code = javaDoc.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("/**\n * \n */");
        }

        @Test
        @DisplayName("generateCode() should generate simple comment")
        void testGenerateCode_SimpleComment_GeneratesCorrectly() {
            // Given
            javaDoc.setComment(new StringBuilder("This is a simple comment."));

            // When
            String code = javaDoc.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("/**\n * This is a simple comment.\n */");
        }

        @Test
        @DisplayName("generateCode() should handle multiline comments")
        void testGenerateCode_MultilineComment_GeneratesCorrectly() {
            // Given
            javaDoc.setComment(new StringBuilder("First line.\nSecond line.\nThird line."));

            // When
            String code = javaDoc.generateCode(0).toString();

            // Then
            assertThat(code).isEqualTo("/**\n * First line.\n * Second line.\n * Third line.\n */");
        }

        @Test
        @DisplayName("generateCode() should apply correct indentation")
        void testGenerateCode_WithIndentation_AppliesCorrectly() {
            // Given
            javaDoc.setComment(new StringBuilder("Indented comment."));

            // When
            String code = javaDoc.generateCode(2).toString();

            // Then
            assertThat(code).startsWith("        /**"); // 8 spaces
            assertThat(code).contains("        * Indented comment.");
            assertThat(code).endsWith("        */");
        }

        @Test
        @DisplayName("generateCode() should include tags")
        void testGenerateCode_WithTags_IncludesTags() {
            // Given
            javaDoc.setComment(new StringBuilder("Method description."));
            javaDoc.addTag(JDocTag.PARAM, "paramName parameter description");
            javaDoc.addTag(JDocTag.RETURN, "return value description");

            // When
            String code = javaDoc.generateCode(0).toString();

            // Then
            assertThat(code).contains("Method description.");
            assertThat(code).contains("@param paramName parameter description");
            assertThat(code).contains("@return return value description");
        }

        @Test
        @DisplayName("generateCode() should handle tags with multiline descriptions")
        void testGenerateCode_WithMultilineTags_HandlesCorrectly() {
            // Given
            javaDoc.setComment(new StringBuilder("Method description."));
            javaDoc.addTag(JDocTag.PARAM, "paramName first line\nsecond line");

            // When
            String code = javaDoc.generateCode(0).toString();

            // Then
            assertThat(code).contains("@param paramName first line");
            assertThat(code).contains(" *     second line");
        }

        @Test
        @DisplayName("generateCode() should handle empty tags")
        void testGenerateCode_WithEmptyTags_HandlesCorrectly() {
            // Given
            javaDoc.setComment(new StringBuilder("Method description."));
            javaDoc.addTag(JDocTag.AUTHOR);

            // When
            String code = javaDoc.generateCode(0).toString();

            // Then
            assertThat(code).contains("@author");
        }

        @Test
        @DisplayName("generateCode() should handle null comment")
        void testGenerateCode_WithNullComment_HandlesGracefully() {
            // Given
            javaDoc.setComment(null);

            // When/Then
            assertThatThrownBy(() -> javaDoc.generateCode(0))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("generateCode() should preserve tag order")
        void testGenerateCode_PreservesTagOrder() {
            // Given
            javaDoc.setComment(new StringBuilder("Method description."));
            javaDoc.addTag(JDocTag.PARAM, "first param");
            javaDoc.addTag(JDocTag.PARAM, "second param");
            javaDoc.addTag(JDocTag.RETURN, "return value");
            javaDoc.addTag(JDocTag.AUTHOR, "Author Name");

            // When
            String code = javaDoc.generateCode(0).toString();

            // Then
            int firstParamIndex = code.indexOf("first param");
            int secondParamIndex = code.indexOf("second param");
            int returnIndex = code.indexOf("@return");
            int authorIndex = code.indexOf("@author");

            assertThat(firstParamIndex).isLessThan(secondParamIndex);
            assertThat(secondParamIndex).isLessThan(returnIndex);
            assertThat(returnIndex).isLessThan(authorIndex);
        }
    }

    @Nested
    @DisplayName("Cloning Tests")
    class CloningTests {

        @Test
        @DisplayName("clone() should create independent copy")
        void testClone_CreatesIndependentCopy() {
            // Given
            javaDoc.setComment(new StringBuilder("Original comment"));
            javaDoc.addTag(JDocTag.AUTHOR, "Author Name");
            javaDoc.addTag(JDocTag.PARAM, "param description");

            // When
            JavaDoc cloned = javaDoc.clone();

            // Then
            assertThat(cloned).isNotSameAs(javaDoc);
            assertThat(cloned.getComment()).isNotSameAs(javaDoc.getComment());
            assertThat(cloned.getComment().toString()).isEqualTo("Original comment");
            assertThat(cloned.getTag(0).getTag()).isEqualTo(JDocTag.AUTHOR);
            assertThat(cloned.getTag(1).getTag()).isEqualTo(JDocTag.PARAM);
        }

        @Test
        @DisplayName("clone() should allow independent modifications")
        void testClone_AllowsIndependentModifications() {
            // Given
            javaDoc.setComment(new StringBuilder("Original"));
            javaDoc.addTag(JDocTag.AUTHOR, "Original Author");

            JavaDoc cloned = javaDoc.clone();

            // When
            javaDoc.getComment().append(" Modified");
            javaDoc.addTag(JDocTag.PARAM, "new param");

            // Then
            assertThat(javaDoc.getComment().toString()).isEqualTo("Original Modified");
            assertThat(cloned.getComment().toString()).isEqualTo("Original");

            // Original has 2 tags, cloned still has 1
            assertThatCode(() -> javaDoc.getTag(1)).doesNotThrowAnyException();
            assertThatThrownBy(() -> cloned.getTag(1))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("clone() should handle empty JavaDoc")
        void testClone_WithEmptyJavaDoc_ClonesCorrectly() {
            // When
            JavaDoc cloned = javaDoc.clone();

            // Then
            assertThat(cloned).isNotSameAs(javaDoc);
            assertThat(cloned.getComment().toString()).isEmpty();
        }

        @Test
        @DisplayName("clone() should handle null comment")
        void testClone_WithNullComment_HandlesGracefully() {
            // Given
            javaDoc.setComment(null);

            // When/Then
            assertThatThrownBy(() -> javaDoc.clone())
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Integration Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("JavaDoc should handle very long comments")
        void testJavaDoc_WithVeryLongComment_HandlesCorrectly() {
            // Given
            String longComment = "A".repeat(10000);
            javaDoc.setComment(new StringBuilder(longComment));

            // When
            String code = javaDoc.generateCode(0).toString();

            // Then
            assertThat(code).contains(longComment);
            assertThat(code).startsWith("/**");
            assertThat(code).endsWith("*/");
        }

        @Test
        @DisplayName("JavaDoc should handle many tags")
        void testJavaDoc_WithManyTags_HandlesCorrectly() {
            // Given
            javaDoc.setComment(new StringBuilder("Method with many parameters."));
            for (int i = 0; i < 50; i++) {
                javaDoc.addTag(JDocTag.PARAM, "param" + i + " description");
            }

            // When
            String code = javaDoc.generateCode(0).toString();

            // Then
            assertThat(code).contains("param0 description");
            assertThat(code).contains("param49 description");
        }

        @Test
        @DisplayName("JavaDoc should handle special characters in comment")
        void testJavaDoc_WithSpecialCharacters_HandlesCorrectly() {
            // Given
            javaDoc.setComment(new StringBuilder("Comment with special chars: @#$%^&*()_+{}[]|\\:;\"'<>?,./"));

            // When
            String code = javaDoc.generateCode(0).toString();

            // Then
            assertThat(code).contains("Comment with special chars: @#$%^&*()_+{}[]|\\:;\"'<>?,./");
        }

        @Test
        @DisplayName("JavaDoc should handle HTML tags in comment")
        void testJavaDoc_WithHTMLTags_HandlesCorrectly() {
            // Given
            javaDoc.setComment(new StringBuilder("Comment with <b>bold</b> and <i>italic</i> text."));

            // When
            String code = javaDoc.generateCode(0).toString();

            // Then
            assertThat(code).contains("<b>bold</b>");
            assertThat(code).contains("<i>italic</i>");
        }

        @Test
        @DisplayName("Multiple JavaDocs should be independent")
        void testMultipleJavaDocs_AreIndependent() {
            // Given
            JavaDoc doc1 = new JavaDoc("First comment");
            JavaDoc doc2 = new JavaDoc("Second comment");

            // When
            doc1.addTag(JDocTag.AUTHOR, "Author 1");
            doc2.addTag(JDocTag.PARAM, "param1");

            // Then
            assertThat(doc1.getComment().toString()).isEqualTo("First comment");
            assertThat(doc2.getComment().toString()).isEqualTo("Second comment");
            assertThat(doc1.getTag(0).getTag()).isEqualTo(JDocTag.AUTHOR);
            assertThat(doc2.getTag(0).getTag()).isEqualTo(JDocTag.PARAM);
        }

        @Test
        @DisplayName("JavaDoc should handle complex indentation scenarios")
        void testJavaDoc_WithComplexIndentation_HandlesCorrectly() {
            // Given
            javaDoc.setComment(new StringBuilder("Multi\nline\ncomment"));
            javaDoc.addTag(JDocTag.PARAM, "param multi\nline\ndescription");

            // When
            String code = javaDoc.generateCode(3).toString();

            // Then
            String[] lines = code.split("\n");
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    assertThat(line).startsWith("            "); // 12 spaces for level 3
                }
            }
        }
    }
}
