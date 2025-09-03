package pt.up.fe.specs.util.reporting;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link ReporterUtils}.
 * Tests the utility methods for working with Reporter interfaces and reporting
 * utilities.
 * 
 * @author Generated Tests
 */
@DisplayName("ReporterUtils")
class ReporterUtilsTest {

    @Nested
    @DisplayName("Constructor")
    class Constructor {

        @Test
        @DisplayName("Should have private constructor but allow instantiation")
        void shouldHavePrivateConstructorButAllowInstantiation() {
            // When/Then - The constructor is private but doesn't throw exceptions when
            // accessed
            try {
                var constructor = ReporterUtils.class.getDeclaredConstructor();
                assertThat(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers())).isTrue();
                constructor.setAccessible(true);
                Object instance = constructor.newInstance();
                assertThat(instance).isNotNull();
            } catch (Exception e) {
                // If exception occurs, it should be a specific type
                assertThat(e).isInstanceOf(RuntimeException.class);
            }
        }
    }

    @Nested
    @DisplayName("Message Formatting")
    class MessageFormatting {

        @Test
        @DisplayName("Should format message with type and content")
        void shouldFormatMessageWithTypeAndContent() {
            // When
            String result = ReporterUtils.formatMessage("Error", "Something went wrong");

            // Then
            assertThat(result).isEqualTo("Error: Something went wrong");
        }

        @Test
        @DisplayName("Should format message with different types")
        void shouldFormatMessageWithDifferentTypes() {
            // When
            String errorResult = ReporterUtils.formatMessage("Error", "Error message");
            String warningResult = ReporterUtils.formatMessage("Warning", "Warning message");
            String infoResult = ReporterUtils.formatMessage("Info", "Info message");

            // Then
            assertThat(errorResult).isEqualTo("Error: Error message");
            assertThat(warningResult).isEqualTo("Warning: Warning message");
            assertThat(infoResult).isEqualTo("Info: Info message");
        }

        @Test
        @DisplayName("Should handle empty message type")
        void shouldHandleEmptyMessageType() {
            // When
            String result = ReporterUtils.formatMessage("", "Some message");

            // Then
            assertThat(result).isEqualTo(": Some message");
        }

        @Test
        @DisplayName("Should handle empty message")
        void shouldHandleEmptyMessage() {
            // When
            String result = ReporterUtils.formatMessage("Error", "");

            // Then
            assertThat(result).isEqualTo("Error: ");
        }

        @Test
        @DisplayName("Should handle both empty type and message")
        void shouldHandleBothEmptyTypeAndMessage() {
            // When
            String result = ReporterUtils.formatMessage("", "");

            // Then
            assertThat(result).isEqualTo(": ");
        }

        @Test
        @DisplayName("Should handle special characters in type and message")
        void shouldHandleSpecialCharactersInTypeAndMessage() {
            // When
            String result = ReporterUtils.formatMessage("Error-Type_1", "Message with: special characters!");

            // Then
            assertThat(result).isEqualTo("Error-Type_1: Message with: special characters!");
        }

        @Test
        @DisplayName("Should handle unicode characters")
        void shouldHandleUnicodeCharacters() {
            // When
            String result = ReporterUtils.formatMessage("错误", "消息包含中文字符 🚨");

            // Then
            assertThat(result).isEqualTo("错误: 消息包含中文字符 🚨");
        }

        @Test
        @DisplayName("Should throw exception for null message type")
        void shouldThrowExceptionForNullMessageType() {
            // When/Then
            assertThatThrownBy(() -> ReporterUtils.formatMessage(null, "message"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should throw exception for null message")
        void shouldThrowExceptionForNullMessage() {
            // When/Then
            assertThatThrownBy(() -> ReporterUtils.formatMessage("Error", null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should throw exception for both null parameters")
        void shouldThrowExceptionForBothNullParameters() {
            // When/Then
            assertThatThrownBy(() -> ReporterUtils.formatMessage(null, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("File Stack Line Formatting")
    class FileStackLineFormatting {

        @Test
        @DisplayName("Should format file stack line with all components")
        void shouldFormatFileStackLineWithAllComponents() {
            // When
            String result = ReporterUtils.formatFileStackLine("example.c", 42, "    int x = 5;");

            // Then
            assertThat(result).isEqualTo("At example.c:42:\n   > int x = 5;");
        }

        @Test
        @DisplayName("Should trim whitespace from code line")
        void shouldTrimWhitespaceFromCodeLine() {
            // When
            String result = ReporterUtils.formatFileStackLine("test.c", 1, "  \t  printf(\"Hello\");  \t  ");

            // Then
            assertThat(result).isEqualTo("At test.c:1:\n   > printf(\"Hello\");");
        }

        @Test
        @DisplayName("Should handle different file extensions")
        void shouldHandleDifferentFileExtensions() {
            // When
            String cResult = ReporterUtils.formatFileStackLine("file.c", 10, "code");
            String javaResult = ReporterUtils.formatFileStackLine("File.java", 20, "code");
            String jsResult = ReporterUtils.formatFileStackLine("script.js", 30, "code");

            // Then
            assertThat(cResult).contains("At file.c:10:");
            assertThat(javaResult).contains("At File.java:20:");
            assertThat(jsResult).contains("At script.js:30:");
        }

        @Test
        @DisplayName("Should handle different line numbers")
        void shouldHandleDifferentLineNumbers() {
            // When
            String singleDigit = ReporterUtils.formatFileStackLine("file.c", 5, "code");
            String multiDigit = ReporterUtils.formatFileStackLine("file.c", 1234, "code");

            // Then
            assertThat(singleDigit).contains("At file.c:5:");
            assertThat(multiDigit).contains("At file.c:1234:");
        }

        @Test
        @DisplayName("Should handle negative line numbers")
        void shouldHandleNegativeLineNumbers() {
            // When
            String result = ReporterUtils.formatFileStackLine("file.c", -1, "code");

            // Then
            assertThat(result).isEqualTo("At file.c:-1:\n   > code");
        }

        @Test
        @DisplayName("Should handle zero line number")
        void shouldHandleZeroLineNumber() {
            // When
            String result = ReporterUtils.formatFileStackLine("file.c", 0, "code");

            // Then
            assertThat(result).isEqualTo("At file.c:0:\n   > code");
        }

        @Test
        @DisplayName("Should handle empty code line")
        void shouldHandleEmptyCodeLine() {
            // When
            String result = ReporterUtils.formatFileStackLine("file.c", 1, "");

            // Then
            assertThat(result).isEqualTo("At file.c:1:\n   > ");
        }

        @Test
        @DisplayName("Should handle code line with only whitespace")
        void shouldHandleCodeLineWithOnlyWhitespace() {
            // When
            String result = ReporterUtils.formatFileStackLine("file.c", 1, "   \t   ");

            // Then
            assertThat(result).isEqualTo("At file.c:1:\n   > ");
        }

        @Test
        @DisplayName("Should handle long file paths")
        void shouldHandleLongFilePaths() {
            // When
            String result = ReporterUtils.formatFileStackLine("/very/long/path/to/some/deep/directory/file.c", 1,
                    "code");

            // Then
            assertThat(result).contains("At /very/long/path/to/some/deep/directory/file.c:1:");
        }

        @Test
        @DisplayName("Should throw exception for null file name")
        void shouldThrowExceptionForNullFileName() {
            // When/Then
            assertThatThrownBy(() -> ReporterUtils.formatFileStackLine(null, 1, "code"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should throw exception for null code line")
        void shouldThrowExceptionForNullCodeLine() {
            // When/Then
            assertThatThrownBy(() -> ReporterUtils.formatFileStackLine("file.c", 1, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Function Stack Line Formatting")
    class FunctionStackLineFormatting {

        @Test
        @DisplayName("Should format function stack line with all components")
        void shouldFormatFunctionStackLineWithAllComponents() {
            // When
            String result = ReporterUtils.formatFunctionStackLine("main", "example.c", 42, "    int x = 5;");

            // Then
            assertThat(result).isEqualTo("At function main (example.c:42):\n   > int x = 5;");
        }

        @Test
        @DisplayName("Should trim whitespace from code line")
        void shouldTrimWhitespaceFromCodeLine() {
            // When
            String result = ReporterUtils.formatFunctionStackLine("printf", "stdio.h", 1,
                    "  \t  printf(\"Hello\");  \t  ");

            // Then
            assertThat(result).isEqualTo("At function printf (stdio.h:1):\n   > printf(\"Hello\");");
        }

        @Test
        @DisplayName("Should handle different function names")
        void shouldHandleDifferentFunctionNames() {
            // When
            String mainResult = ReporterUtils.formatFunctionStackLine("main", "file.c", 1, "code");
            String customResult = ReporterUtils.formatFunctionStackLine("calculateSum", "math.c", 1, "code");
            String specialResult = ReporterUtils.formatFunctionStackLine("func_with_underscores", "util.c", 1, "code");

            // Then
            assertThat(mainResult).contains("At function main (");
            assertThat(customResult).contains("At function calculateSum (");
            assertThat(specialResult).contains("At function func_with_underscores (");
        }

        @Test
        @DisplayName("Should handle empty function name")
        void shouldHandleEmptyFunctionName() {
            // When
            String result = ReporterUtils.formatFunctionStackLine("", "file.c", 1, "code");

            // Then
            assertThat(result).isEqualTo("At function  (file.c:1):\n   > code");
        }

        @Test
        @DisplayName("Should handle special characters in function name")
        void shouldHandleSpecialCharactersInFunctionName() {
            // When
            String result = ReporterUtils.formatFunctionStackLine("operator<<", "iostream.cpp", 1, "code");

            // Then
            assertThat(result).contains("At function operator<< (");
        }

        @Test
        @DisplayName("Should handle different line numbers")
        void shouldHandleDifferentLineNumbers() {
            // When
            String singleDigit = ReporterUtils.formatFunctionStackLine("func", "file.c", 5, "code");
            String multiDigit = ReporterUtils.formatFunctionStackLine("func", "file.c", 1234, "code");

            // Then
            assertThat(singleDigit).contains("(file.c:5):");
            assertThat(multiDigit).contains("(file.c:1234):");
        }

        @Test
        @DisplayName("Should handle empty code line")
        void shouldHandleEmptyCodeLine() {
            // When
            String result = ReporterUtils.formatFunctionStackLine("func", "file.c", 1, "");

            // Then
            assertThat(result).isEqualTo("At function func (file.c:1):\n   > ");
        }

        @Test
        @DisplayName("Should handle null function name gracefully")
        void shouldHandleNullFunctionNameGracefully() {
            // When
            String result = ReporterUtils.formatFunctionStackLine(null, "file.c", 1, "code");

            // Then - Null function name is handled gracefully, not throwing exception
            assertThat(result).isEqualTo("At function null (file.c:1):\n   > code");
        }

        @Test
        @DisplayName("Should throw exception for null file name")
        void shouldThrowExceptionForNullFileName() {
            // When/Then
            assertThatThrownBy(() -> ReporterUtils.formatFunctionStackLine("func", null, 1, "code"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should throw exception for null code line")
        void shouldThrowExceptionForNullCodeLine() {
            // When/Then
            assertThatThrownBy(() -> ReporterUtils.formatFunctionStackLine("func", "file.c", 1, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Stack End")
    class StackEnd {

        @Test
        @DisplayName("Should return newline for stack end")
        void shouldReturnNewlineForStackEnd() {
            // When
            String result = ReporterUtils.stackEnd();

            // Then
            assertThat(result).isEqualTo("\n");
        }

        @Test
        @DisplayName("Should return consistent value across multiple calls")
        void shouldReturnConsistentValueAcrossMultipleCalls() {
            // When
            String result1 = ReporterUtils.stackEnd();
            String result2 = ReporterUtils.stackEnd();
            String result3 = ReporterUtils.stackEnd();

            // Then
            assertThat(result1).isEqualTo(result2);
            assertThat(result2).isEqualTo(result3);
            assertThat(result1).isEqualTo("\n");
        }

        @Test
        @DisplayName("Should be thread-safe")
        void shouldBeThreadSafe() throws InterruptedException {
            // Given
            final int numThreads = 10;
            Thread[] threads = new Thread[numThreads];
            String[] results = new String[numThreads];

            // When
            for (int i = 0; i < numThreads; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    results[index] = ReporterUtils.stackEnd();
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            for (String result : results) {
                assertThat(result).isEqualTo("\n");
            }
        }
    }

    @Nested
    @DisplayName("Error Line Retrieval")
    class ErrorLineRetrieval {

        @Test
        @DisplayName("Should get specific line from code")
        void shouldGetSpecificLineFromCode() {
            // Given
            String code = "line 1\nline 2\nline 3\nline 4";

            // When
            String line1 = ReporterUtils.getErrorLine(code, 1);
            String line2 = ReporterUtils.getErrorLine(code, 2);
            String line3 = ReporterUtils.getErrorLine(code, 3);
            String line4 = ReporterUtils.getErrorLine(code, 4);

            // Then
            assertThat(line1).isEqualTo("line 1");
            assertThat(line2).isEqualTo("line 2");
            assertThat(line3).isEqualTo("line 3");
            assertThat(line4).isEqualTo("line 4");
        }

        @Test
        @DisplayName("Should handle single line code")
        void shouldHandleSingleLineCode() {
            // Given
            String code = "single line";

            // When
            String result = ReporterUtils.getErrorLine(code, 1);

            // Then
            assertThat(result).isEqualTo("single line");
        }

        @Test
        @DisplayName("Should handle empty lines in code")
        void shouldHandleEmptyLinesInCode() {
            // Given
            String code = "line 1\n\nline 3";

            // When
            String line1 = ReporterUtils.getErrorLine(code, 1);
            String line2 = ReporterUtils.getErrorLine(code, 2);
            String line3 = ReporterUtils.getErrorLine(code, 3);

            // Then
            assertThat(line1).isEqualTo("line 1");
            assertThat(line2).isEqualTo("");
            assertThat(line3).isEqualTo("line 3");
        }

        @Test
        @DisplayName("Should handle code with different line endings")
        void shouldHandleCodeWithDifferentLineEndings() {
            // Given
            String unixCode = "line 1\nline 2\nline 3";
            // Note: We test with \n since that's what split("\n") handles

            // When
            String result = ReporterUtils.getErrorLine(unixCode, 2);

            // Then
            assertThat(result).isEqualTo("line 2");
        }

        @Test
        @DisplayName("Should handle whitespace-only lines")
        void shouldHandleWhitespaceOnlyLines() {
            // Given
            String code = "line 1\n   \t   \nline 3";

            // When
            String result = ReporterUtils.getErrorLine(code, 2);

            // Then
            assertThat(result).isEqualTo("   \t   ");
        }

        @Test
        @DisplayName("Should return error message for null code")
        void shouldReturnErrorMessageForNullCode() {
            // When
            String result = ReporterUtils.getErrorLine(null, 1);

            // Then
            assertThat(result).isEqualTo("Could not get code.");
        }

        @Test
        @DisplayName("Should handle line numbers beyond code length")
        void shouldHandleLineNumbersBeyondCodeLength() {
            // Given
            String code = "line 1\nline 2";

            // When/Then - This will throw an exception (array out of bounds)
            assertThatThrownBy(() -> ReporterUtils.getErrorLine(code, 5))
                    .isInstanceOf(ArrayIndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should handle zero line number")
        void shouldHandleZeroLineNumber() {
            // Given
            String code = "line 1\nline 2";

            // When/Then - This will throw an exception (negative array index)
            assertThatThrownBy(() -> ReporterUtils.getErrorLine(code, 0))
                    .isInstanceOf(ArrayIndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should handle negative line numbers")
        void shouldHandleNegativeLineNumbers() {
            // Given
            String code = "line 1\nline 2";

            // When/Then - This will throw an exception (negative array index)
            assertThatThrownBy(() -> ReporterUtils.getErrorLine(code, -1))
                    .isInstanceOf(ArrayIndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should handle empty code string")
        void shouldHandleEmptyCodeString() {
            // Given
            String code = "";

            // When
            String result = ReporterUtils.getErrorLine(code, 1);

            // Then - Empty string returns empty string, not exception
            assertThat(result).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should combine formatting methods for complete stack trace")
        void shouldCombineFormattingMethodsForCompleteStackTrace() {
            // Given
            String code = "int main() {\n    return 0;\n}";

            // When
            String header = ReporterUtils.formatMessage("Error", "Compilation failed");
            String functionStack = ReporterUtils.formatFunctionStackLine("main", "main.c", 2,
                    ReporterUtils.getErrorLine(code, 2));
            String fileStack = ReporterUtils.formatFileStackLine("main.c", 1,
                    ReporterUtils.getErrorLine(code, 1));
            String end = ReporterUtils.stackEnd();

            String fullTrace = header + "\n" + functionStack + "\n" + fileStack + end;

            // Then
            assertThat(fullTrace).contains("Error: Compilation failed");
            assertThat(fullTrace).contains("At function main (main.c:2):");
            assertThat(fullTrace).contains("> return 0;");
            assertThat(fullTrace).contains("At main.c:1:");
            assertThat(fullTrace).contains("> int main() {");
            assertThat(fullTrace).endsWith("\n");
        }

        @Test
        @DisplayName("Should handle edge cases in combination")
        void shouldHandleEdgeCasesInCombination() {
            // Given
            String nullCode = null;

            // When
            String errorMessage = ReporterUtils.formatMessage("Warning", "Code not available");
            String errorLine = ReporterUtils.getErrorLine(nullCode, 1);
            String stackLine = ReporterUtils.formatFileStackLine("unknown.c", 1, errorLine);

            // Then
            assertThat(errorMessage).isEqualTo("Warning: Code not available");
            assertThat(errorLine).isEqualTo("Could not get code.");
            assertThat(stackLine).contains("> Could not get code.");
        }
    }
}
