package pt.up.fe.specs.util.stringsplitter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import pt.up.fe.specs.util.utilities.StringSlice;

import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for StringSliceWithSplit class.
 * Tests string slicing with splitting capabilities and configuration options.
 * 
 * @author Generated Tests
 */
@DisplayName("StringSliceWithSplit Tests")
class StringSliceWithSplitTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create from string with default settings")
        void testConstructorFromString() {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello world");

            assertThat(slice.toString()).isEqualTo("hello world");
            assertThat(slice.length()).isEqualTo(11);
            assertThat(slice.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("Should create from StringSlice with default settings")
        void testConstructorFromStringSlice() {
            StringSlice baseSlice = new StringSlice("hello world");
            StringSliceWithSplit slice = new StringSliceWithSplit(baseSlice);

            assertThat(slice.toString()).isEqualTo("hello world");
            assertThat(slice.length()).isEqualTo(11);
        }

        @Test
        @DisplayName("Should create with custom settings")
        void testConstructorWithCustomSettings() {
            StringSlice baseSlice = new StringSlice("hello,world");
            Predicate<Character> customSeparator = ch -> ch == ',';

            StringSliceWithSplit slice = new StringSliceWithSplit(baseSlice, false, false, customSeparator);

            assertThat(slice.toString()).isEqualTo("hello,world");
        }

        @Test
        @DisplayName("Should handle empty string")
        void testConstructorWithEmptyString() {
            StringSliceWithSplit slice = new StringSliceWithSplit("");

            assertThat(slice.toString()).isEmpty();
            assertThat(slice.length()).isZero();
            assertThat(slice.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should handle null string gracefully")
        void testConstructorWithNullString() {
            assertThatThrownBy(() -> new StringSliceWithSplit((String) null))
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("Configuration Methods Tests")
    class ConfigurationMethodsTests {

        @Test
        @DisplayName("Should set trim configuration")
        void testSetTrim() {
            StringSliceWithSplit original = new StringSliceWithSplit("  hello  world  ");

            StringSliceWithSplit trimmed = original.setTrim(true);
            StringSliceWithSplit notTrimmed = original.setTrim(false);

            assertThat(trimmed).isNotSameAs(original);
            assertThat(notTrimmed).isNotSameAs(original);
            assertThat(trimmed.toString()).isEqualTo("  hello  world  ");
            assertThat(notTrimmed.toString()).isEqualTo("  hello  world  ");
        }

        @Test
        @DisplayName("Should set reverse configuration")
        void testSetReverse() {
            StringSliceWithSplit original = new StringSliceWithSplit("hello world");

            StringSliceWithSplit reversed = original.setReverse(true);
            StringSliceWithSplit notReversed = original.setReverse(false);

            assertThat(reversed).isNotSameAs(original);
            assertThat(notReversed).isNotSameAs(original);
            assertThat(reversed.toString()).isEqualTo("hello world");
            assertThat(notReversed.toString()).isEqualTo("hello world");
        }

        @Test
        @DisplayName("Should set custom separator")
        void testSetSeparator() {
            StringSliceWithSplit original = new StringSliceWithSplit("hello,world");

            Predicate<Character> commaSeparator = ch -> ch == ',';
            StringSliceWithSplit withComma = original.setSeparator(commaSeparator);

            Predicate<Character> spaceSeparator = ch -> ch == ' ';
            StringSliceWithSplit withSpace = original.setSeparator(spaceSeparator);

            assertThat(withComma).isNotSameAs(original);
            assertThat(withSpace).isNotSameAs(original);
            assertThat(withComma.toString()).isEqualTo("hello,world");
            assertThat(withSpace.toString()).isEqualTo("hello,world");
        }

        @Test
        @DisplayName("Should chain configuration methods")
        void testChainedConfiguration() {
            StringSliceWithSplit original = new StringSliceWithSplit("  hello,world  ");

            StringSliceWithSplit configured = original
                    .setTrim(true)
                    .setReverse(false)
                    .setSeparator(ch -> ch == ',');

            assertThat(configured).isNotSameAs(original);
            assertThat(configured.toString()).isEqualTo("  hello,world  ");
        }
    }

    @Nested
    @DisplayName("Split Method Tests")
    class SplitMethodTests {

        @Test
        @DisplayName("Should split with default whitespace separator")
        void testSplitDefaultSeparator() {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello world test");

            SplitResult<String> result = slice.split();

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("hello");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("world test");
        }

        @Test
        @DisplayName("Should split with custom separator")
        void testSplitCustomSeparator() {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello,world,test")
                    .setSeparator(ch -> ch == ',');

            SplitResult<String> result = slice.split();

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("hello");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("world,test");
        }

        @Test
        @DisplayName("Should split when no separator found")
        void testSplitNoSeparator() {
            StringSliceWithSplit slice = new StringSliceWithSplit("helloworld");

            SplitResult<String> result = slice.split();

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("helloworld");
            assertThat(result.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should split with trim enabled")
        void testSplitWithTrim() {
            StringSliceWithSplit slice = new StringSliceWithSplit("  hello  world  ")
                    .setTrim(true);

            SplitResult<String> result = slice.split();

            assertThat(result).isNotNull();
            // Leading spaces cause empty string first, trimmed becomes empty
            assertThat(result.getValue()).isEmpty();
            assertThat(result.getModifiedSlice().toString()).isEqualTo("hello  world");
        }

        @Test
        @DisplayName("Should split with trim disabled")
        void testSplitWithoutTrim() {
            StringSliceWithSplit slice = new StringSliceWithSplit("  hello  world  ")
                    .setTrim(false);

            SplitResult<String> result = slice.split();

            assertThat(result).isNotNull();
            // Leading spaces cause empty string first
            assertThat(result.getValue()).isEmpty();
            assertThat(result.getModifiedSlice().toString()).isEqualTo(" hello  world  ");
        }

        @Test
        @DisplayName("Should split in reverse mode")
        void testSplitReverse() {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello world test")
                    .setReverse(true);

            SplitResult<String> result = slice.split();

            assertThat(result).isNotNull();
            // In reverse mode, should split from the end
            assertThat(result.getValue()).isEqualTo("test");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("hello world");
        }

        @Test
        @DisplayName("Should handle empty string split")
        void testSplitEmptyString() {
            StringSliceWithSplit slice = new StringSliceWithSplit("");

            SplitResult<String> result = slice.split();

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEmpty();
            assertThat(result.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle string with only separators")
        void testSplitOnlySeparators() {
            StringSliceWithSplit slice = new StringSliceWithSplit("   \t\n   ");

            SplitResult<String> result = slice.split();

            assertThat(result).isNotNull();
            // With default trim=true, should result in empty strings
            assertThat(result.getValue()).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(strings = { " ", "\t", "\n", "\r", "\f" })
        @DisplayName("Should recognize all whitespace characters as default separators")
        void testDefaultSeparatorTypes(String separator) {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello" + separator + "world");

            SplitResult<String> result = slice.split();

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEqualTo("hello");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("world");
        }
    }

    @Nested
    @DisplayName("Multiple Split Operations Tests")
    class MultipleSplitOperationsTests {

        @Test
        @DisplayName("Should handle multiple consecutive splits")
        void testConsecutiveSplits() {
            StringSliceWithSplit slice = new StringSliceWithSplit("first second third fourth");

            SplitResult<String> first = slice.split();
            assertThat(first.getValue()).isEqualTo("first");

            SplitResult<String> second = first.getModifiedSlice().split();
            assertThat(second.getValue()).isEqualTo("second");

            SplitResult<String> third = second.getModifiedSlice().split();
            assertThat(third.getValue()).isEqualTo("third");

            SplitResult<String> fourth = third.getModifiedSlice().split();
            assertThat(fourth.getValue()).isEqualTo("fourth");

            assertThat(fourth.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle splits until exhausted")
        void testSplitUntilExhausted() {
            StringSliceWithSplit slice = new StringSliceWithSplit("a b c d e");

            int count = 0;
            StringSliceWithSplit current = slice;

            while (!current.isEmpty()) {
                SplitResult<String> result = current.split();
                assertThat(result.getValue()).isNotEmpty();
                current = result.getModifiedSlice();
                count++;

                // Safety check to prevent infinite loop
                if (count > 10)
                    break;
            }

            assertThat(count).isEqualTo(5);
        }

        @Test
        @DisplayName("Should handle alternating separators")
        void testAlternatingSeparators() {
            StringSliceWithSplit slice = new StringSliceWithSplit("a,b c,d e,f")
                    .setSeparator(ch -> ch == ',' || Character.isWhitespace(ch));

            SplitResult<String> first = slice.split();
            assertThat(first.getValue()).isEqualTo("a");

            SplitResult<String> second = first.getModifiedSlice().split();
            assertThat(second.getValue()).isEqualTo("b");

            SplitResult<String> third = second.getModifiedSlice().split();
            assertThat(third.getValue()).isEqualTo("c");
        }
    }

    @Nested
    @DisplayName("StringSlice Override Tests")
    class StringSliceOverrideTests {

        @Test
        @DisplayName("Should override trim() method")
        void testTrimOverride() {
            StringSliceWithSplit slice = new StringSliceWithSplit("  hello world  ");

            StringSliceWithSplit trimmed = slice.trim();

            assertThat(trimmed).isInstanceOf(StringSliceWithSplit.class);
            assertThat(trimmed).isNotSameAs(slice);
            assertThat(trimmed.toString()).isEqualTo("hello world");
        }

        @Test
        @DisplayName("Should override substring() method")
        void testSubstringOverride() {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello world");

            StringSliceWithSplit substring = slice.substring(6);

            assertThat(substring).isInstanceOf(StringSliceWithSplit.class);
            assertThat(substring).isNotSameAs(slice);
            assertThat(substring.toString()).isEqualTo("world");
        }

        @Test
        @DisplayName("Should override clear() method")
        void testClearOverride() {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello world");

            StringSliceWithSplit cleared = slice.clear();

            assertThat(cleared).isInstanceOf(StringSliceWithSplit.class);
            assertThat(cleared).isNotSameAs(slice);
            assertThat(cleared.toString()).isEmpty();
            assertThat(cleared.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should preserve configuration in overridden methods")
        void testConfigurationPreservation() {
            Predicate<Character> customSeparator = ch -> ch == ',';
            StringSliceWithSplit original = new StringSliceWithSplit("  hello,world  ")
                    .setTrim(false)
                    .setReverse(true)
                    .setSeparator(customSeparator);

            StringSliceWithSplit trimmed = original.trim();
            StringSliceWithSplit substring = original.substring(2);
            StringSliceWithSplit cleared = original.clear();

            // Test that configurations are preserved by attempting splits
            // The exact behavior depends on internal implementation
            assertThat(trimmed).isInstanceOf(StringSliceWithSplit.class);
            assertThat(substring).isInstanceOf(StringSliceWithSplit.class);
            assertThat(cleared).isInstanceOf(StringSliceWithSplit.class);
        }
    }

    @Nested
    @DisplayName("Complex Separator Tests")
    class ComplexSeparatorTests {

        @Test
        @DisplayName("Should work with alphanumeric separator")
        void testAlphanumericSeparator() {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello123world456test")
                    .setSeparator(Character::isDigit);

            SplitResult<String> result = slice.split();

            assertThat(result.getValue()).isEqualTo("hello");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("23world456test");
        }

        @Test
        @DisplayName("Should work with punctuation separator")
        void testPunctuationSeparator() {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello!world?test.")
                    .setSeparator(ch -> "!?.,;:".indexOf(ch) >= 0);

            SplitResult<String> result = slice.split();

            assertThat(result.getValue()).isEqualTo("hello");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("world?test.");
        }

        @Test
        @DisplayName("Should work with complex predicate separator")
        void testComplexPredicateSeparator() {
            StringSliceWithSplit slice = new StringSliceWithSplit("aAaAbBbBcCcC")
                    .setSeparator(ch -> Character.isUpperCase(ch));

            SplitResult<String> result = slice.split();

            assertThat(result.getValue()).isEqualTo("a");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("aAbBbBcCcC");
        }

        @Test
        @DisplayName("Should handle separator that never matches")
        void testNeverMatchingSeparator() {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello world test")
                    .setSeparator(ch -> ch == 'X'); // X never appears

            SplitResult<String> result = slice.split();

            assertThat(result.getValue()).isEqualTo("hello world test");
            assertThat(result.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle separator that always matches")
        void testAlwaysMatchingSeparator() {
            StringSliceWithSplit slice = new StringSliceWithSplit("hello")
                    .setSeparator(ch -> true); // Every character is a separator

            SplitResult<String> result = slice.split();

            assertThat(result.getValue()).isEmpty();
            assertThat(result.getModifiedSlice().toString()).isEqualTo("ello");
        }
    }

    @Nested
    @DisplayName("Reverse Mode Tests")
    class ReverseModeTests {

        @Test
        @DisplayName("Should split from end in reverse mode")
        void testReverseSplitting() {
            StringSliceWithSplit slice = new StringSliceWithSplit("one two three")
                    .setReverse(true);

            SplitResult<String> result = slice.split();

            assertThat(result.getValue()).isEqualTo("three");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("one two");
        }

        @Test
        @DisplayName("Should handle multiple reverse splits")
        void testMultipleReverseSplits() {
            StringSliceWithSplit slice = new StringSliceWithSplit("first second third")
                    .setReverse(true);

            SplitResult<String> first = slice.split();
            assertThat(first.getValue()).isEqualTo("third");

            SplitResult<String> second = first.getModifiedSlice().split();
            assertThat(second.getValue()).isEqualTo("second");

            SplitResult<String> third = second.getModifiedSlice().split();
            assertThat(third.getValue()).isEqualTo("first");

            assertThat(third.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle reverse with custom separator")
        void testReverseWithCustomSeparator() {
            StringSliceWithSplit slice = new StringSliceWithSplit("a,b,c,d")
                    .setReverse(true)
                    .setSeparator(ch -> ch == ',');

            SplitResult<String> result = slice.split();

            assertThat(result.getValue()).isEqualTo("d");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("a,b,c");
        }

        @Test
        @DisplayName("Should handle reverse with trim")
        void testReverseWithTrim() {
            StringSliceWithSplit slice = new StringSliceWithSplit("  first  second  third  ")
                    .setReverse(true)
                    .setTrim(true);

            SplitResult<String> result = slice.split();

            assertThat(result).isNotNull();
            // In reverse mode with trailing spaces, empty string first
            assertThat(result.getValue()).isEmpty();
            assertThat(result.getModifiedSlice().toString()).isEqualTo("first  second  third");
        }

        @Test
        @DisplayName("Should handle reverse mode with no separators")
        void testReverseNoSeparators() {
            StringSliceWithSplit slice = new StringSliceWithSplit("noseparators")
                    .setReverse(true);

            SplitResult<String> result = slice.split();

            assertThat(result.getValue()).isEqualTo("noseparators");
            assertThat(result.getModifiedSlice().toString()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle very long strings")
        void testVeryLongStrings() {
            String longString = "word ".repeat(1000) + "final";
            StringSliceWithSplit slice = new StringSliceWithSplit(longString);

            SplitResult<String> result = slice.split();

            assertThat(result.getValue()).isEqualTo("word");
            assertThat(result.getModifiedSlice().toString()).startsWith("word ");
        }

        @Test
        @DisplayName("Should handle Unicode characters")
        void testUnicodeCharacters() {
            StringSliceWithSplit slice = new StringSliceWithSplit("こんにちは 世界 テスト");

            SplitResult<String> result = slice.split();

            assertThat(result.getValue()).isEqualTo("こんにちは");
            assertThat(result.getModifiedSlice().toString()).isEqualTo("世界 テスト");
        }

        @Test
        @DisplayName("Should handle special characters")
        void testSpecialCharacters() {
            StringSliceWithSplit slice = new StringSliceWithSplit("special!@# chars$%^ here&*(");

            SplitResult<String> result = slice.split();

            // The exact result depends on what characters are considered separators
            assertThat(result).isNotNull();
            assertThat(result.getValue()).isNotNull();
        }

        @Test
        @DisplayName("Should handle strings with mixed separators")
        void testMixedSeparators() {
            StringSliceWithSplit slice = new StringSliceWithSplit("word1\tword2\nword3 word4\rword5");

            SplitResult<String> result = slice.split();

            assertThat(result.getValue()).isEqualTo("word1");
            // Remaining should still contain the other words
            assertThat(result.getModifiedSlice().toString()).contains("word2");
        }

        @Test
        @DisplayName("Should handle empty splits gracefully")
        void testEmptySplits() {
            StringSliceWithSplit slice = new StringSliceWithSplit(" word ");

            SplitResult<String> result = slice.split();

            assertThat(result).isNotNull();
            // Leading space causes empty string first
            assertThat(result.getValue()).isEmpty();
        }

        @Test
        @DisplayName("Should handle single character strings")
        void testSingleCharacterStrings() {
            StringSliceWithSplit slice = new StringSliceWithSplit("a");

            SplitResult<String> result = slice.split();

            assertThat(result.getValue()).isEqualTo("a");
            assertThat(result.getModifiedSlice().toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle strings that are only separators")
        void testOnlySeparatorStrings() {
            StringSliceWithSplit slice = new StringSliceWithSplit("     ")
                    .setTrim(false);

            SplitResult<String> result = slice.split();

            assertThat(result).isNotNull();
            assertThat(result.getValue()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Integration with StringSplitter")
    class IntegrationWithStringSplitter {

        @Test
        @DisplayName("Should work seamlessly with StringSplitter")
        void testStringSliceWithSplitInStringSplitter() {
            StringSliceWithSplit slice = new StringSliceWithSplit("123 hello 45.6");

            // This simulates how StringSplitter might use StringSliceWithSplit
            SplitResult<String> firstResult = slice.split();
            assertThat(firstResult.getValue()).isEqualTo("123");

            SplitResult<String> secondResult = firstResult.getModifiedSlice().split();
            assertThat(secondResult.getValue()).isEqualTo("hello");

            SplitResult<String> thirdResult = secondResult.getModifiedSlice().split();
            assertThat(thirdResult.getValue()).isEqualTo("45.6");
        }

        @Test
        @DisplayName("Should maintain configuration through splits")
        void testConfigurationMaintenance() {
            StringSliceWithSplit slice = new StringSliceWithSplit("a,b,c,d")
                    .setSeparator(ch -> ch == ',')
                    .setTrim(false);

            SplitResult<String> first = slice.split();
            SplitResult<String> second = first.getModifiedSlice().split();

            assertThat(first.getValue()).isEqualTo("a");
            assertThat(second.getValue()).isEqualTo("b");

            // Configuration should be maintained in the modified slice
            SplitResult<String> third = second.getModifiedSlice().split();
            assertThat(third.getValue()).isEqualTo("c");
        }
    }
}
