/**
 * Copyright 2017 SPeCS.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.stringparser;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiPredicate;

import pt.up.fe.specs.util.Preconditions;
import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.enums.EnumHelperWithValue;
import pt.up.fe.specs.util.providers.StringProvider;
import pt.up.fe.specs.util.utilities.StringSlice;

public class StringParsers {

    private static final Set<Character> DIGITS = new HashSet<>(
            Arrays.asList('0', '1', '2', '3', '4', '5', '6', '7', '8', '9'));

    private static final Set<Character> HEXDIGITS_LOWER;

    static {
        HEXDIGITS_LOWER = new HashSet<>(Arrays.asList('a', 'b', 'c', 'd', 'e', 'f'));
        HEXDIGITS_LOWER.addAll(DIGITS);
    }

    /**
     * Receives a string starting with generic string separated by a whitespace, or
     * the complete string if no whitespace is found.
     *
     */
    public static ParserResult<String> parseWord(StringSlice string) {
        int endIndex = string.indexOf(' ');
        if (endIndex == -1) {
            endIndex = string.length();
        }

        String element = string.substring(0, endIndex).toString();

        // Update slice
        string = string.substring(endIndex);

        return new ParserResult<>(string, element);
    }

    /**
     * Receives a string starting with generic string separated by a whitespace, or
     * the complete string if no whitespace is found.
     *
     */
    public static ParserResult<Boolean> hasWord(StringSlice string, String word) {
        return hasWord(string, word, true);
    }

    public static ParserResult<Boolean> hasWord(StringSlice string, String word, boolean caseSensitive) {
        // Find end of the word
        int endIndex = string.indexOf(' ');
        if (endIndex == -1) {
            endIndex = string.length();
        }

        String testWord = string.substring(0, endIndex).toString();

        // If case sensitive, do not change the strings
        testWord = caseSensitive ? testWord : testWord.toLowerCase();
        word = caseSensitive ? word : word.toLowerCase();

        // Check if testWord is the same as the word
        boolean areEqual = testWord.equals(word);

        // If not equal, do not update slice
        if (!areEqual) {
            return new ParserResult<>(string, false);
        }

        // Update slice
        string = string.substring(endIndex);
        return new ParserResult<>(string, true);
    }

    public static ParserResult<Optional<Character>> checkCharacter(StringSlice string, Character aChar) {
        return checkCharacter(string, Collections.singletonList(aChar));
    }

    public static ParserResult<Optional<Character>> checkCharacter(StringSlice string,
            Collection<Character> characterSet) {

        if (string.isEmpty()) {
            return new ParserResult<>(string, Optional.empty());
        }

        char firstChar = string.charAt(0);

        // Do nothing if not in the given set
        if (!characterSet.contains(firstChar)) {
            return new ParserResult<>(string, Optional.empty());
        }

        // Update slice
        string = string.substring(1);

        return new ParserResult<>(string, Optional.of(firstChar));
    }

    public static ParserResult<Optional<Character>> checkDigit(StringSlice string) {
        return checkCharacter(string, DIGITS);
    }

    public static ParserResult<Optional<Character>> checkHexDigit(StringSlice string) {
        return checkCharacter(string, HEXDIGITS_LOWER);
    }

    /**
     * Helper method which sets case-sensitiveness to true.
     *
     */
    public static ParserResult<Optional<String>> checkStringStarts(StringSlice string, String prefix) {
        return checkStringStarts(string, prefix, true);
    }

    /**
     * Returns an optional with the string if it starts with the given prefix,
     * removes it from parsing.
     *
     */
    public static ParserResult<Optional<String>> checkStringStarts(StringSlice string, String prefix,
            boolean caseSensitive) {

        boolean startsWith = caseSensitive ? string.startsWith(prefix)
                : string.toString().toLowerCase().startsWith(prefix.toLowerCase());

        if (startsWith) {
            // Obtain string
            String result = string.substring(0, prefix.length()).toString();

            string = string.substring(prefix.length());
            return new ParserResult<>(string, Optional.of(result));
        }

        return new ParserResult<>(string, Optional.empty());
    }

    public static ParserResult<Boolean> checkStringEnds(StringSlice string, String suffix) {

        if (string.endsWith(suffix)) {
            string = string.substring(0, string.length() - suffix.length());
            return new ParserResult<>(string, true);
        }

        return new ParserResult<>(string, false);
    }

    public static ParserResult<Boolean> peekStartsWith(StringSlice string, String prefix) {
        return peekStartsWith(string, prefix, true);
    }

    /**
     * Checks if it starts with the given String, but does not change the contents
     * if it is either true of false.
     *
     */
    public static ParserResult<Boolean> peekStartsWith(StringSlice string, String prefix,
            boolean caseSensitive) {

        boolean startsWith = caseSensitive ? string.startsWith(prefix)
                : string.toString().toLowerCase().startsWith(prefix.toLowerCase());

        return new ParserResult<>(string, startsWith);
    }

    /**
     * String must start with double quote, and appends characters until there is an
     * unescaped double quote.
     * 
     * @return the contents inside the double quoted string
     */
    public static ParserResult<String> parseDoubleQuotedString(StringSlice string) {
        String startString = "\"";
        String endString = "\"";
        String escapeString = "\\";

        if (!string.startsWith(startString)) {
            throw new RuntimeException("String does not start with '" + startString + "'");
        }

        // Drop start string
        StringSlice currentString = string.substring(startString.length());

        StringBuilder contents = new StringBuilder();
        while (!currentString.isEmpty()) {
            // Escape string must be tested before end string
            if (currentString.startsWith(escapeString)) {
                currentString = currentString.substring(escapeString.length());

                Preconditions.checkArgument(!currentString.isEmpty());
                char escapedChar = currentString.charAt(0);
                currentString = currentString.substring(1);

                contents.append(escapeString).append(escapedChar);
                continue;
            }

            if (currentString.startsWith(endString)) {
                // Drop end string
                currentString = currentString.substring(endString.length());
                return new ParserResult<>(currentString, contents.toString());
            }

            char aChar = currentString.charAt(0);
            currentString = currentString.substring(1);
            contents.append(aChar);
        }

        throw new RuntimeException(
                "Could not find a valid end delimiter ('" + endString + "') for string '" + string + "'");
    }

    /**
     * Receives a string starting with the given prefix, returns the prefix. Throws
     * exception if the prefix is not found.
     *
     */
    public static ParserResult<String> parseString(StringSlice string, String prefix) {
        if (!string.startsWith(prefix)) {
            throw new RuntimeException("Expected string to start with '" + prefix + "': " + string);
        }

        StringSlice modifiedString = string.substring(prefix.length());

        return new ParserResult<>(modifiedString, prefix);
    }

    /**
     * Parses a string between the given begin and end characters, trims the slice
     * in the end.
     *
     */
    public static ParserResult<String> parseNested(StringSlice string, char begin, char end,
            BiPredicate<StringSlice, Integer> endPredicate) {

        Preconditions.checkArgument(!string.isEmpty());

        if (string.charAt(0) != begin) {
            return new ParserResult<>(string, "");
        }

        int counter = 1;
        int endIndex = 0;
        while (counter > 0) {
            endIndex++;

            // If found end char, decrement
            if (endPredicate.test(string, endIndex)) {
                counter--;
                continue;
            }

            // If found start char, increment
            if (string.charAt(endIndex) == begin) {
                counter++;
                continue;
            }
        }

        // Return string without separators
        String result = string.substring(1, endIndex).toString();

        // Cut string from parser
        if (endIndex < string.length() - 1) {
            string = string.substring(endIndex + 1);
        } else {
            string = new StringSlice("");
        }

        return new ParserResult<>(string, result);
    }

    /**
     * Parses a string inside primes ('), separated by spaces.
     * <p>
     * Receives a string starting with "'{element}' ( '{element}')*", returns a list
     * with the elements, without the primes.
     *
     */
    public static ParserResult<String> parseNested(StringSlice string, char begin, char end) {
        BiPredicate<StringSlice, Integer> endPredicate = (slice, endIndex) -> slice.charAt(endIndex) == end;

        return parseNested(string, begin, end, endPredicate);
    }

    public static String removeSuffix(String string, String suffix) {
        if (!string.endsWith(suffix)) {
            return string;
        }

        return string.substring(0, string.length() - suffix.length());
    }

    /**
     * Helper method which does not use the example value as a default value. Throws
     * exception if the enum is not found.
     *
     */
    public static <K extends Enum<K> & StringProvider> ParserResult<K> parseEnum(
            StringSlice string, EnumHelperWithValue<K> enumHelper) {

        return parseEnum(string, enumHelper, null);
    }

    public static <K extends Enum<K> & StringProvider> ParserResult<K> parseEnum(
            StringSlice string, EnumHelperWithValue<K> enumHelper, K defaultValue) {

        // Try parsing the enum
        ParserResult<Optional<K>> result = checkEnum(string, enumHelper);

        if (result.result().isPresent()) {
            return new ParserResult<>(result.modifiedString(), result.result().get());
        }

        // No value found, check if should use the given example value as default
        if (defaultValue != null) {
            return new ParserResult<>(string, defaultValue);
        }

        throw new RuntimeException(
                "Could not convert string '" + StringParsers.parseWord(new StringSlice(string)).result()
                        + "' to enum '"
                        + enumHelper.getValuesTranslationMap() + "'");

    }

    /**
     * Helper method which converts the word to upper case (enum values by
     * convention should be uppercase).
     *
     */
    public static <K extends Enum<K>> ParserResult<K> parseEnum(StringSlice string, Class<K> enumClass,
            K defaultValue) {

        return parseEnum(string, enumClass, defaultValue, Collections.emptyMap());
    }

    public static <K extends Enum<K>> ParserResult<K> parseEnum(StringSlice string, Class<K> enumClass) {
        return parseEnum(string, enumClass, null, Collections.emptyMap());
    }

    public static <K extends Enum<K>> ParserResult<K> parseEnum(StringSlice string, Class<K> enumClass,
            K defaultValue, Map<String, K> customMappings) {

        // Copy StringSlice, in case the function does not found the enum
        ParserResult<String> word = StringParsers.parseWord(new StringSlice(string));

        // Check if enumeration contains element with the same name as the string
        K anEnum = SpecsEnums.valueOf(enumClass, word.result().toUpperCase());
        if (anEnum != null) {
            return new ParserResult<>(word.modifiedString(), anEnum);
        }

        // Check if there are any custom mappings for the word
        K customMapping = customMappings.get(word.result());
        if (customMapping != null) {
            return new ParserResult<>(word.modifiedString(), customMapping);
        }

        // Check if there is a default value
        // In this case, return the unmodified string
        if (defaultValue != null) {
            return new ParserResult<>(string, defaultValue);
        }

        throw new RuntimeException(
                "Could not convert string '" + StringParsers.parseWord(new StringSlice(string)).result()
                        + "' to enum '"
                        + Arrays.toString(enumClass.getEnumConstants()) + "'");

    }

    /**
     * Helper method which accepts a default value.
     *
     */
    public static <K extends Enum<K> & StringProvider> ParserResult<K> checkEnum(
            StringSlice string, EnumHelperWithValue<K> enumHelper, K defaultValue) {

        ParserResult<Optional<K>> result = checkEnum(string, enumHelper);
        K value = result.result().orElse(defaultValue);
        return new ParserResult<>(result.modifiedString(), value);
    }

    /**
     * Checks if string starts with a word representing an enumeration of the given
     * example value.
     *
     */
    public static <K extends Enum<K> & StringProvider> ParserResult<Optional<K>> checkEnum(
            StringSlice string, EnumHelperWithValue<K> enumHelper) {

        // Copy StringSlice, in case the function does not found the enum
        ParserResult<String> word = StringParsers.parseWord(new StringSlice(string));

        // Check if there are any custom mappings for the word
        Optional<K> result = enumHelper.fromValueTry(word.result());

        // Prepare return value
        StringSlice modifiedString = result.isPresent() ? word.modifiedString() : string;

        return new ParserResult<>(modifiedString, result);
    }

}
