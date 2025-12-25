package tdrc.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive unit tests for StringUtils utility class.
 * Tests string manipulation, sanitization, case conversion, and XML operations.
 * 
 * @author Generated Tests
 */
@DisplayName("StringUtils Tests")
class StringUtilsTest {

    @Nested
    @DisplayName("Keyword Tests")
    class KeywordTests {

        @Test
        @DisplayName("Verify Java keyword detection")
        void testIsJavaKeyword_WithKeywords_ReturnsTrue() {
            // Java keywords that should be detected
            String[] keywords = {
                    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
                    "class", "const", "continue", "default", "do", "double", "else", "extends",
                    "false", "final", "finally", "float", "for", "goto", "if", "implements",
                    "import", "instanceof", "int", "interface", "long", "native", "new", "null",
                    "package", "private", "protected", "public", "return", "short", "static",
                    "strictfp", "super", "switch", "synchronized", "this", "throw", "throws",
                    "transient", "true", "try", "void", "volatile", "while"
            };

            for (String keyword : keywords) {
                assertThat(StringUtils.isJavaKeyword(keyword))
                        .as("Keyword '%s' should be detected", keyword)
                        .isTrue();
            }
        }

        @Test
        @DisplayName("Verify non-keywords are not detected")
        void testIsJavaKeyword_WithNonKeywords_ReturnsFalse() {
            // Non-keywords that should not be detected
            String[] nonKeywords = {
                    "hello", "world", "variable", "method", "String", "Integer",
                    "MyClass", "someVariable", "test", "value", "result"
            };

            for (String nonKeyword : nonKeywords) {
                assertThat(StringUtils.isJavaKeyword(nonKeyword))
                        .as("Non-keyword '%s' should not be detected", nonKeyword)
                        .isFalse();
            }
        }

        @Test
        @DisplayName("Handle null and empty strings for keyword detection")
        void testIsJavaKeyword_WithNullOrEmpty_ReturnsFalse() {
            assertThat(StringUtils.isJavaKeyword(null)).isFalse();
            assertThat(StringUtils.isJavaKeyword("")).isFalse();
            assertThat(StringUtils.isJavaKeyword("   ")).isFalse();
        }
    }

    @Nested
    @DisplayName("Sanitization Tests")
    class SanitizationTests {

        @Test
        @DisplayName("Sanitize Java keywords with prefix")
        void testGetSanitizedName_WithKeywords_AddsPrefixCorrectly() {
            assertThat(StringUtils.getSanitizedName("class")).isEqualTo("_class");
            assertThat(StringUtils.getSanitizedName("if")).isEqualTo("_if");
            assertThat(StringUtils.getSanitizedName("public")).isEqualTo("_public");
        }

        @Test
        @DisplayName("Sanitize non-keywords remains unchanged")
        void testGetSanitizedName_WithNonKeywords_RemainsUnchanged() {
            assertThat(StringUtils.getSanitizedName("hello")).isEqualTo("hello");
            assertThat(StringUtils.getSanitizedName("myVariable")).isEqualTo("myVariable");
            assertThat(StringUtils.getSanitizedName("test123")).isEqualTo("test123");
        }

        @Test
        @DisplayName("Sanitize null returns null")
        void testGetSanitizedName_WithNull_ReturnsNull() {
            String result = StringUtils.getSanitizedName(null);
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Sanitize empty string")
        void testGetSanitizedName_WithEmpty_RemainsUnchanged() {
            assertThat(StringUtils.getSanitizedName("")).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Case Conversion Tests")
    class CaseConversionTests {

        @Test
        @DisplayName("Convert first character to uppercase")
        void testFirstCharToUpper_ConvertsCorrectly() {
            assertThat(StringUtils.firstCharToUpper("hello")).isEqualTo("Hello");
            assertThat(StringUtils.firstCharToUpper("world")).isEqualTo("World");
            assertThat(StringUtils.firstCharToUpper("test")).isEqualTo("Test");
        }

        @Test
        @DisplayName("First char uppercase with already uppercase")
        void testFirstCharToUpper_WithAlreadyUppercase_RemainsUnchanged() {
            assertThat(StringUtils.firstCharToUpper("Hello")).isEqualTo("Hello");
            assertThat(StringUtils.firstCharToUpper("World")).isEqualTo("World");
        }

        @Test
        @DisplayName("First char uppercase with edge cases")
        void testFirstCharToUpper_WithEdgeCases_HandlesCorrectly() {
            assertThatThrownBy(() -> StringUtils.firstCharToUpper(""))
                    .isInstanceOf(StringIndexOutOfBoundsException.class);
            assertThat(StringUtils.firstCharToUpper("a")).isEqualTo("A");
            assertThat(StringUtils.firstCharToUpper("1hello")).isEqualTo("1hello"); // Numbers unchanged
        }

        @Test
        @DisplayName("First char uppercase with null throws exception")
        void testFirstCharToUpper_WithNull_ThrowsException() {
            assertThatThrownBy(() -> StringUtils.firstCharToUpper(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Convert first character to lowercase")
        void testFirstCharToLower_ConvertsCorrectly() {
            assertThat(StringUtils.firstCharToLower("Hello")).isEqualTo("hello");
            assertThat(StringUtils.firstCharToLower("World")).isEqualTo("world");
            assertThat(StringUtils.firstCharToLower("Test")).isEqualTo("test");
        }

        @Test
        @DisplayName("First char lowercase with already lowercase")
        void testFirstCharToLower_WithAlreadyLowercase_RemainsUnchanged() {
            assertThat(StringUtils.firstCharToLower("hello")).isEqualTo("hello");
            assertThat(StringUtils.firstCharToLower("world")).isEqualTo("world");
        }

        @Test
        @DisplayName("First char lowercase with edge cases")
        void testFirstCharToLower_WithEdgeCases_HandlesCorrectly() {
            assertThatThrownBy(() -> StringUtils.firstCharToLower(""))
                    .isInstanceOf(StringIndexOutOfBoundsException.class);
            assertThat(StringUtils.firstCharToLower("A")).isEqualTo("a");
            assertThat(StringUtils.firstCharToLower("1Hello")).isEqualTo("1Hello"); // Numbers unchanged
        }

        @Test
        @DisplayName("First char lowercase with null throws exception")
        void testFirstCharToLower_WithNull_ThrowsException() {
            assertThatThrownBy(() -> StringUtils.firstCharToLower(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Convert character at specific position")
        void testCharToUpperOrLower_ConvertsAtPosition() {
            assertThat(StringUtils.charToUpperOrLower("hello", 0, true)).isEqualTo("Hello");
            assertThat(StringUtils.charToUpperOrLower("hello", 1, true)).isEqualTo("hEllo");
            assertThat(StringUtils.charToUpperOrLower("HELLO", 0, false)).isEqualTo("hELLO");
            assertThat(StringUtils.charToUpperOrLower("HELLO", 2, false)).isEqualTo("HElLO");
        }

        @Test
        @DisplayName("Convert character at invalid position throws exception")
        void testCharToUpperOrLower_WithInvalidPosition_ThrowsException() {
            assertThatThrownBy(() -> StringUtils.charToUpperOrLower("hello", -1, true))
                    .isInstanceOf(StringIndexOutOfBoundsException.class);
            assertThatThrownBy(() -> StringUtils.charToUpperOrLower("hello", 5, true))
                    .isInstanceOf(StringIndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("Joining Tests")
    class JoiningTests {

        @Test
        @DisplayName("Join string collection with separator")
        void testJoinStrings_WithSeparator_JoinsCorrectly() {
            Collection<String> items = Arrays.asList("apple", "banana", "cherry");

            assertThat(StringUtils.joinStrings(items, ", ")).isEqualTo("apple, banana, cherry");
            assertThat(StringUtils.joinStrings(items, "-")).isEqualTo("apple-banana-cherry");
            assertThat(StringUtils.joinStrings(items, "")).isEqualTo("applebananacherry");
        }

        @Test
        @DisplayName("Join with function transformation")
        void testJoin_WithFunction_TransformsAndJoins() {
            Collection<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
            Function<Integer, String> squareFunction = n -> String.valueOf(n * n);

            String result = StringUtils.join(numbers, squareFunction, ", ");

            assertThat(result).isEqualTo("1, 4, 9, 16, 25");
        }

        @Test
        @DisplayName("Join collection using toString")
        void testJoin_WithToString_ConvertsAndJoins() {
            Collection<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

            String result = StringUtils.join(numbers, ", ");

            assertThat(result).isEqualTo("1, 2, 3, 4, 5");
        }

        @Test
        @DisplayName("Join empty collection")
        void testJoin_WithEmptyCollection_ReturnsEmptyString() {
            Collection<String> empty = Collections.emptyList();

            assertThat(StringUtils.join(empty, ", ")).isEqualTo("");
            assertThat(StringUtils.joinStrings(empty, ", ")).isEqualTo("");
        }

        @Test
        @DisplayName("Join single item collection")
        void testJoin_WithSingleItem_ReturnsItemAsString() {
            Collection<String> single = List.of("alone");

            assertThat(StringUtils.join(single, ", ")).isEqualTo("alone");
            assertThat(StringUtils.joinStrings(single, ", ")).isEqualTo("alone");
        }

        @Test
        @DisplayName("Join collection with null items")
        void testJoin_WithNullItems_HandlesNulls() {
            Collection<String> withNulls = Arrays.asList("a", null, "c");

            String result = StringUtils.join(withNulls, ", ");

            assertThat(result).isEqualTo("a, null, c");
        }
    }

    @Nested
    @DisplayName("Package Comparison Tests")
    class PackageComparisonTests {

        @Test
        @DisplayName("Compare equal package names")
        void testInSamePackage_EqualPackages_ReturnsTrue() {
            assertThat(StringUtils.inSamePackage("com.example.test.Class1", "com.example.test.Class2")).isTrue();
            assertThat(StringUtils.inSamePackage("java.util.List", "java.util.ArrayList")).isTrue();
            assertThat(StringUtils.inSamePackage("SimpleClass", "AnotherSimpleClass")).isTrue(); // Default package
        }

        @Test
        @DisplayName("Compare different package names")
        void testInSamePackage_DifferentPackages_ReturnsFalse() {
            assertThat(StringUtils.inSamePackage("com.example.test.Class1", "com.example.other.Class2")).isFalse();
            assertThat(StringUtils.inSamePackage("java.util.List", "java.lang.String")).isFalse();
            assertThat(StringUtils.inSamePackage("com.test.Class", "SimpleClass")).isFalse(); // One in package, one
                                                                                              // default
        }

        @Test
        @DisplayName("Get package from class name")
        void testGetPackage_ReturnsCorrectPackage() {
            assertThat(StringUtils.getPackage("com.example.test.MyClass")).isEqualTo("com.example.test");
            assertThat(StringUtils.getPackage("java.util.List")).isEqualTo("java.util");
            assertThat(StringUtils.getPackage("SimpleClass")).isEqualTo(""); // No package
        }

        @Test
        @DisplayName("Get package with edge cases")
        void testGetPackage_WithEdgeCases_HandlesCorrectly() {
            assertThat(StringUtils.getPackage("")).isEqualTo("");
            assertThat(StringUtils.getPackage(".ClassName")).isEqualTo("");
            assertThat(StringUtils.getPackage("a.b.c.d.e.ClassName")).isEqualTo("a.b.c.d.e");
        }
    }

    @Nested
    @DisplayName("String Repeat Tests")
    @SuppressWarnings("deprecation") // Testing deprecated repeat method
    class StringRepeatTests {

        @Test
        @DisplayName("Repeat string multiple times")
        void testRepeat_ValidInputs_RepeatsCorrectly() {
            assertThat(StringUtils.repeat("abc", 3)).isEqualTo("abcabcabc");
            assertThat(StringUtils.repeat("x", 5)).isEqualTo("xxxxx");
            assertThat(StringUtils.repeat("hello", 2)).isEqualTo("hellohello");
        }

        @Test
        @DisplayName("Repeat string zero times")
        void testRepeat_ZeroTimes_ReturnsEmptyString() {
            assertThat(StringUtils.repeat("test", 0)).isEqualTo("");
            assertThat(StringUtils.repeat("hello", 0)).isEqualTo("");
        }

        @Test
        @DisplayName("Repeat string once")
        void testRepeat_OnceTimes_ReturnsOriginal() {
            assertThat(StringUtils.repeat("test", 1)).isEqualTo("test");
            assertThat(StringUtils.repeat("hello", 1)).isEqualTo("hello");
        }

        @Test
        @DisplayName("Repeat empty string")
        void testRepeat_EmptyString_ReturnsEmptyString() {
            assertThat(StringUtils.repeat("", 5)).isEqualTo("");
            assertThat(StringUtils.repeat("", 1)).isEqualTo("");
        }

        @Test
        @DisplayName("Repeat with null and negative inputs")
        void testRepeat_InvalidInputs_ReturnsNull() {
            assertThat(StringUtils.repeat(null, 3)).isNull();
            assertThat(StringUtils.repeat("test", -1)).isNull();
            assertThat(StringUtils.repeat(null, -1)).isNull();
        }
    }

    @Nested
    @DisplayName("XML Conversion Tests")
    class XmlConversionTests {

        @Test
        @DisplayName("XML to string buffer method exists")
        void testXmlToStringBuffer_MethodExists() {
            // Test that the method exists with correct signature
            assertThat(StringUtils.class.getDeclaredMethods())
                    .extracting("name")
                    .contains("xmlToStringBuffer");
        }

        @Test
        @DisplayName("XML to string buffer with null document")
        void testXmlToStringBuffer_WithNullDocument_ThrowsException() {
            // This would throw an exception if called with null
            assertThatThrownBy(() -> StringUtils.xmlToStringBuffer(null, 4))
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Handle very long strings")
        void testLongStrings_HandledCorrectly() {
            StringBuilder longString = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                longString.append("a");
            }
            String testString = longString.toString();

            // Test that methods don't fail with very long strings
            assertThat(StringUtils.getSanitizedName(testString)).isEqualTo(testString);
            assertThat(StringUtils.firstCharToUpper(testString)).startsWith("A");
        }

        @Test
        @DisplayName("Handle special characters")
        void testSpecialCharacters_HandledCorrectly() {
            String specialChars = "äöü@#$%^&*()[]{}|\\:;\"'<>?/.,`~";

            // Test that special characters are handled without errors
            assertThatCode(() -> StringUtils.getSanitizedName(specialChars)).doesNotThrowAnyException();
            assertThatCode(() -> StringUtils.firstCharToUpper(specialChars)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Handle Unicode characters")
        void testUnicodeCharacters_HandledCorrectly() {
            String unicode = "日本語العربية🌟💻🚀";

            // Test that Unicode characters are handled without errors
            assertThatCode(() -> StringUtils.getSanitizedName(unicode)).doesNotThrowAnyException();
            assertThatCode(() -> StringUtils.firstCharToUpper(unicode)).doesNotThrowAnyException();
        }
    }
}
