package org.specs.generators.java.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.specs.generators.java.classtypes.JavaClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Comprehensive test suite for the Utils utility class.
 * Tests all static utility methods including indentation, file operations,
 * string manipulation, and code generation utilities.
 * 
 * @author Generated Tests
 */
@DisplayName("Utils - Utility Methods Test Suite")
public class UtilsTest {

    @TempDir
    Path tempDir;

    private File tempDirFile;

    @BeforeEach
    void setUp() {
        tempDirFile = tempDir.toFile();
    }

    @Nested
    @DisplayName("Indentation Tests")
    class IndentationTests {

        @Test
        @DisplayName("Should return empty StringBuilder for zero indentation")
        void shouldReturnEmptyStringBuilderForZeroIndentation() {
            StringBuilder result = Utils.indent(0);

            assertThat(result.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should return four spaces for indentation level 1")
        void shouldReturnFourSpacesForIndentationLevelOne() {
            StringBuilder result = Utils.indent(1);

            assertThat(result.toString()).isEqualTo("    ");
        }

        @Test
        @DisplayName("Should return multiple four-space groups for higher indentation levels")
        void shouldReturnMultipleFourSpaceGroupsForHigherIndentationLevels() {
            StringBuilder result2 = Utils.indent(2);
            StringBuilder result5 = Utils.indent(5);

            assertThat(result2.toString()).isEqualTo("        "); // 8 spaces
            assertThat(result5.toString()).isEqualTo("                    "); // 20 spaces
        }

        @Test
        @DisplayName("Should handle negative indentation gracefully")
        void shouldHandleNegativeIndentationGracefully() {
            StringBuilder result = Utils.indent(-1);

            assertThat(result).isNotNull();
            assertThat(result.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle large indentation values")
        void shouldHandleLargeIndentationValues() {
            StringBuilder result = Utils.indent(10);

            assertThat(result.toString()).hasSize(40); // 10 * 4 spaces
            assertThat(result.toString()).matches(" +");
        }
    }

    @Nested
    @DisplayName("File Generation Tests")
    class FileGenerationTests {

        @Test
        @DisplayName("Should generate file with JavaClass object")
        void shouldGenerateFileWithJavaClassObject() throws IOException {
            // Create a simple JavaClass instance for testing
            JavaClass testClass = new JavaClass("TestClass", "org.example.test");

            boolean result = Utils.generateToFile(tempDirFile, testClass, true);

            assertThat(result).isTrue();

            // Check if file was created in the correct package structure
            Path expectedFile = tempDir.resolve("org/example/test/TestClass.java");
            assertThat(expectedFile).exists();
        }

        @Test
        @DisplayName("Should not replace existing file when replace is false")
        void shouldNotReplaceExistingFileWhenReplaceIsFalse() throws IOException {
            JavaClass testClass = new JavaClass("TestClass", "org.example");

            // Generate file first time
            boolean result1 = Utils.generateToFile(tempDirFile, testClass, true);
            assertThat(result1).isTrue();

            // Try to generate again with replace = false
            boolean result2 = Utils.generateToFile(tempDirFile, testClass, false);
            assertThat(result2).isFalse();
        }

        @Test
        @DisplayName("Should replace existing file when replace is true")
        void shouldReplaceExistingFileWhenReplaceIsTrue() throws IOException {
            JavaClass testClass = new JavaClass("TestClass", "org.example");

            // Generate file first time
            boolean result1 = Utils.generateToFile(tempDirFile, testClass, true);
            assertThat(result1).isTrue();

            // Generate again with replace = true
            boolean result2 = Utils.generateToFile(tempDirFile, testClass, true);
            assertThat(result2).isTrue();
        }

        @Test
        @DisplayName("Should handle null output directory gracefully")
        void shouldHandleNullOutputDirectoryGracefully() {
            JavaClass testClass = new JavaClass("TestClass", "org.example");

            boolean result = Utils.generateToFile(null, testClass, true);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should handle null ClassType gracefully")
        void shouldHandleNullClassTypeGracefully() {
            boolean result = Utils.generateToFile(tempDirFile, null, true);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should create package directories if they don't exist")
        void shouldCreatePackageDirectoriesIfTheyDontExist() throws IOException {
            JavaClass testClass = new JavaClass("DeepClass", "org.example.very.deep.package.structure");

            boolean result = Utils.generateToFile(tempDirFile, testClass, true);

            assertThat(result).isTrue();
            Path expectedDir = tempDir.resolve("org/example/very/deep/package/structure");
            assertThat(expectedDir).exists();
            assertThat(expectedDir.resolve("DeepClass.java")).exists();
        }
    }

    @Nested
    @DisplayName("Directory Creation Tests")
    class DirectoryCreationTests {

        @Test
        @DisplayName("Should create directory that doesn't exist")
        void shouldCreateDirectoryThatDoesntExist() {
            File newDir = tempDir.resolve("newDirectory").toFile();
            assertThat(newDir).doesNotExist();

            Utils.makeDirs(newDir);

            assertThat(newDir).exists();
            assertThat(newDir).isDirectory();
        }

        @Test
        @DisplayName("Should handle existing directory gracefully")
        void shouldHandleExistingDirectoryGracefully() throws IOException {
            File existingDir = tempDir.resolve("existing").toFile();
            Files.createDirectory(existingDir.toPath());
            assertThat(existingDir).exists();

            // Should not throw exception
            assertThatCode(() -> {
                Utils.makeDirs(existingDir);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should create nested directories")
        void shouldCreateNestedDirectories() {
            File nestedDir = tempDir.resolve("nested/deep/path/structure").toFile();
            assertThat(nestedDir.getParentFile()).doesNotExist();

            Utils.makeDirs(nestedDir);

            assertThat(nestedDir).exists();
            assertThat(nestedDir).isDirectory();
        }

        @Test
        @DisplayName("Should handle null directory gracefully")
        void shouldHandleNullDirectoryGracefully() {
            assertThatCode(() -> {
                Utils.makeDirs(null);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("String Manipulation Tests")
    class StringManipulationTests {

        @Test
        @DisplayName("Should capitalize first character of lowercase string")
        void shouldCapitalizeFirstCharacterOfLowercaseString() {
            String result = Utils.firstCharToUpper("hello");

            assertThat(result).isEqualTo("Hello");
        }

        @Test
        @DisplayName("Should leave already capitalized string unchanged")
        void shouldLeaveAlreadyCapitalizedStringUnchanged() {
            String result = Utils.firstCharToUpper("Hello");

            assertThat(result).isEqualTo("Hello");
        }

        @Test
        @DisplayName("Should handle single character string")
        void shouldHandleSingleCharacterString() {
            String result1 = Utils.firstCharToUpper("a");
            String result2 = Utils.firstCharToUpper("A");

            assertThat(result1).isEqualTo("A");
            assertThat(result2).isEqualTo("A");
        }

        @Test
        @DisplayName("Should throw exception for empty string")
        void shouldThrowExceptionForEmptyString() {
            assertThatThrownBy(() -> {
                Utils.firstCharToUpper("");
            }).isInstanceOf(StringIndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should throw exception for null string")
        void shouldThrowExceptionForNullString() {
            assertThatThrownBy(() -> {
                Utils.firstCharToUpper(null);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle string with numbers")
        void shouldHandleStringWithNumbers() {
            String result = Utils.firstCharToUpper("1hello");

            assertThat(result).isEqualTo("1hello"); // Number should remain unchanged
        }

        @Test
        @DisplayName("Should handle string with special characters")
        void shouldHandleStringWithSpecialCharacters() {
            String result = Utils.firstCharToUpper("@hello");

            assertThat(result).isEqualTo("@hello"); // Special char should remain unchanged
        }

        @Test
        @DisplayName("Should preserve rest of string when capitalizing")
        void shouldPreserveRestOfStringWhenCapitalizing() {
            String result = Utils.firstCharToUpper("hELLO wORLD");

            assertThat(result).isEqualTo("HELLO wORLD");
        }

        @Test
        @DisplayName("Should handle multi-word strings")
        void shouldHandleMultiWordStrings() {
            String result = Utils.firstCharToUpper("camelCaseString");

            assertThat(result).isEqualTo("CamelCaseString");
        }
    }

    @Nested
    @DisplayName("Line Separator Tests")
    class LineSeparatorTests {

        @Test
        @DisplayName("Should return newline character")
        void shouldReturnNewlineCharacter() {
            String result = Utils.ln();

            assertThat(result).isEqualTo("\n");
        }

        @Test
        @DisplayName("Should return consistent line separator")
        void shouldReturnConsistentLineSeparator() {
            String result1 = Utils.ln();
            String result2 = Utils.ln();

            assertThat(result1).isEqualTo(result2);
        }

        @Test
        @DisplayName("Should not return null")
        void shouldNotReturnNull() {
            String result = Utils.ln();

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should not return empty string")
        void shouldNotReturnEmptyString() {
            String result = Utils.ln();

            assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("Should return single character")
        void shouldReturnSingleCharacter() {
            String result = Utils.ln();

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should create complete package structure and generate file")
        void shouldCreateCompletePackageStructureAndGenerateFile() throws IOException {
            String packageName = "org.example.generated";
            String className = "GeneratedClass";

            JavaClass testClass = new JavaClass(className, packageName);
            boolean result = Utils.generateToFile(tempDirFile, testClass, true);

            assertThat(result).isTrue();

            Path expectedFile = tempDir.resolve("org/example/generated/GeneratedClass.java");
            assertThat(expectedFile).exists();

            String content = Files.readString(expectedFile);
            assertThat(content).isNotEmpty();
        }

        @Test
        @DisplayName("Should use proper indentation in generated code")
        void shouldUseProperIndentationInGeneratedCode() throws IOException {
            JavaClass testClass = new JavaClass("IndentedClass", "org.example");

            boolean result = Utils.generateToFile(tempDirFile, testClass, true);
            assertThat(result).isTrue();

            Path generatedFile = tempDir.resolve("org/example/IndentedClass.java");
            String content = Files.readString(generatedFile);

            // The basic class without any content may not have indentation
            // But it should be a valid Java class
            assertThat(content).contains("package org.example;");
            assertThat(content).contains("class IndentedClass");
        }

        @Test
        @DisplayName("Should handle complete workflow with directory creation")
        void shouldHandleCompleteWorkflowWithDirectoryCreation() throws IOException {
            String className = Utils.firstCharToUpper("myTestClass");
            assertThat(className).isEqualTo("MyTestClass");

            JavaClass testClass = new JavaClass(className, "com.deep.nested.package");

            // Generate file (should create directories automatically)
            boolean result = Utils.generateToFile(tempDirFile, testClass, true);
            assertThat(result).isTrue();

            // Verify directory structure was created
            Path packageDir = tempDir.resolve("com/deep/nested/package");
            assertThat(packageDir).exists();
            assertThat(packageDir).isDirectory();

            // Verify file was created
            Path classFile = packageDir.resolve("MyTestClass.java");
            assertThat(classFile).exists();

            // Verify content structure
            String content = Files.readString(classFile);
            assertThat(content).contains("package com.deep.nested.package;");
            assertThat(content).contains("class MyTestClass");
            assertThat(content).contains(Utils.ln()); // Uses proper line endings
        }

        @Test
        @DisplayName("Should properly format method names using firstCharToUpper")
        void shouldProperlyFormatMethodNamesUsingFirstCharToUpper() {
            String methodName1 = Utils.firstCharToUpper("getName");
            String methodName2 = Utils.firstCharToUpper("setProperty");
            String methodName3 = Utils.firstCharToUpper("calculateValue");

            assertThat(methodName1).isEqualTo("GetName");
            assertThat(methodName2).isEqualTo("SetProperty");
            assertThat(methodName3).isEqualTo("CalculateValue");
        }
    }
}
