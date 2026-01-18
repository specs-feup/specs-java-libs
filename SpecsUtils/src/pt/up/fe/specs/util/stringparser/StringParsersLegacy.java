/**
 * Copyright 2019 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.stringparser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;

import pt.up.fe.specs.util.Preconditions;
import pt.up.fe.specs.util.enums.EnumHelperWithValue;
import pt.up.fe.specs.util.providers.StringProvider;
import pt.up.fe.specs.util.utilities.StringSlice;

public class StringParsersLegacy {

    private static final Pattern REGEX_WINDOWS_PATH = Pattern.compile("^[a-zA-Z]:.*");

    /**
     * Clears the StringSlice, for debugging/development purposes.
     *
     */
    public static ParserResult<String> clear(StringSlice string) {
        return new ParserResult<>(new StringSlice(""), string.toString());
    }

    public static ParserResult<String> parseParenthesis(StringSlice string) {
        return StringParsers.parseNested(string, '(', ')');
    }

    /**
     * Receives a string starting with "'{element}'({separator}'{element}')*",
     * returns a list with the elements, without the primes.
     * 
     * <p>
     * Trims the string after processing.
     *
     */
    public static ParserResult<List<String>> parsePrimesSeparatedByString(StringSlice string, String separator) {
        List<String> elements = new ArrayList<>();

        if (string.isEmpty()) {
            return new ParserResult<>(string, elements);
        }

        // Check that String starts with a '
        if (!string.startsWith("'")) {
            throw new RuntimeException("Given string does not start with quote ('):" + string);
        }

        // While string starts with a prime (')
        while (string.startsWith("'")) {
            // Get string between primes
            ParserResult<String> primeString = StringParsers.parseNested(string, '\'', '\'');

            // Update string
            string = primeString.modifiedString();
            elements.add(primeString.result());

            // If there is not a separator, with a prime following it, return
            if (!string.startsWith(separator + "'")) {
                // Trim string
                string = string.trim();
                return new ParserResult<>(string, elements);
            }

            // Remove separator from string
            string = string.substring(separator.length());
        }

        throw new RuntimeException("Should not arrive here, current string: '" + string + "'");
    }

    /**
     * Receives a string starting with "(line|col):{number}(:{number})? and ending
     * with a whitespace
     *
     */
    public static ParserResult<String> parseLocation(StringSlice string) {
        String location = "";

        Optional<Integer> pathOffsetIndex = testPath(string);

        // Windows path
        if (pathOffsetIndex.isPresent()) {
            int offset = pathOffsetIndex.get();

            // Fetch the next colon, it should mark line/col specification
            int colonIndex = string.substring(offset).indexOf(':');

            if (colonIndex == -1) {
                throw new RuntimeException("Was expecting a colon:" + string.substring(offset));
            }

            // Use space as separator after colon
            int spaceIndex = string.substring(offset + colonIndex).indexOf(' ');

            if (spaceIndex == -1) {
                throw new RuntimeException("Was expecting a space after the colon:" + string.substring(offset));
            }

            location = string.substring(0, offset + colonIndex + spaceIndex).toString();
            string = string.substring(offset + colonIndex + spaceIndex);

            return new ParserResult<>(string, location);
        }

        if (string.startsWith("col") || string.startsWith("line")) {
            // Get last index
            int endIndex = string.indexOf(' ');
            if (endIndex == -1) {
                // endIndex = string.length() - 1;
                endIndex = string.length();
            }

            // Check if there are colons inside
            if (string.indexOf(':') == -1) {
                throw new RuntimeException("Expected a colon (:) in string '" + string + "'");
            }

            location = string.substring(0, endIndex).toString();

            // Update slice
            string = string.substring(endIndex);
        }

        return new ParserResult<>(string, location);
    }

    /**
     * Returns the index after any possible colons in the path.
     *
     */
    private static Optional<Integer> testPath(StringSlice string) {
        // Linux path
        if (string.startsWith("/")) {
            return Optional.of(0);
        }

        // Windows path
        if (REGEX_WINDOWS_PATH.matcher(string.toString()).matches()) {
            return Optional.of(2);
        }

        return Optional.empty();
    }

    /**
     * 
     * @return the remaining of the string in the parser
     */
    public static ParserResult<String> parseRemaining(StringSlice string) {
        String rem = string.toString();
        string = string.substring(rem.length());

        return new ParserResult<>(string, rem);
    }

    /**
     * Makes sure the string has the given prefix at the beginning.
     *
     */
    public static ParserResult<Boolean> ensurePrefix(StringSlice string, String prefix) {
        // Save the string in case we need to throw an exception
        String originalString = string.toString();

        ParserResult<Boolean> result = checkStringStarts(string, prefix);

        if (result.result()) {
            return result;
        }

        throw new RuntimeException(
                "Expected to find the prefix '" + prefix + "' at the beginning of '" + originalString + "'");
    }

    /**
     * Makes sure the string has the given string at the beginning, separated by a
     * whitespace, or is the complete string if no whitespace is found.
     *
     */
    public static ParserResult<Boolean> ensureWord(StringSlice string, String word) {
        // Save the string in case we need to throw an exception
        String originalString = string.toString();

        ParserResult<Boolean> result = checkWord(string, word);

        if (result.result()) {
            return result;
        }

        throw new RuntimeException(
                "Expected to find the word '" + word + "' at the beginning of '" + originalString + "'");
    }

    /**
     * Checks if starts with the given string, separated by a whitespace or if there
     * is no whitespace, until the end of the string.
     *
     */
    public static ParserResult<Boolean> checkWord(StringSlice string, String word) {
        int endIndex = string.indexOf(' ');
        if (endIndex == -1) {
            endIndex = string.length();
        }

        boolean hasWord = string.substring(0, endIndex).equalsString(word);
        if (!hasWord) {
            return new ParserResult<>(string, false);
        }

        string = string.substring(endIndex);

        return new ParserResult<>(string, true);
    }

    /**
     * Checks if ends with the given string, separated by a whitespace or if there
     * is no whitespace, considers the whole string.
     *
     */
    public static ParserResult<Boolean> checkLastString(StringSlice string, String word) {
        // TODO: Using String because StringSlice.lastIndexOf is not implemented
        String workString = string.toString();
        int startIndex = workString.lastIndexOf(' ');
        if (startIndex == -1) {
            startIndex = 0;
        } else {
            startIndex = startIndex + 1;
        }

        boolean hasWord = workString.substring(startIndex).equals(word);
        if (!hasWord) {
            return new ParserResult<>(string, false);
        }

        string = new StringSlice(workString.substring(0, startIndex));

        return new ParserResult<>(string, true);
    }

    /**
     * Returns true if the string starts with the given prefix, removes it from
     * parsing.
     * 
     * <p>
     * Helper method which enables case-sensitiveness by default.
     *
     */
    public static ParserResult<Boolean> checkStringStarts(StringSlice string, String prefix) {
        return checkStringStarts(string, prefix, true);
    }

    /**
     * Returns true if the string starts with the given prefix, removes it from
     * parsing.
     *
     */
    public static ParserResult<Boolean> checkStringStarts(StringSlice string, String prefix, boolean caseSensitive) {

        boolean startsWith = caseSensitive ? string.startsWith(prefix)
                : string.toString().toLowerCase().startsWith(prefix.toLowerCase());

        if (startsWith) {
            string = string.substring(prefix.length());
            return new ParserResult<>(string, true);
        }

        return new ParserResult<>(string, false);
    }

    public static ParserResult<Boolean> ensureStringStarts(StringSlice string, String prefix) {
        ParserResult<Boolean> result = checkStringStarts(string, prefix);
        if (result.result()) {
            return result;
        }

        throw new RuntimeException("Expected string to start with '" + prefix + "', instead is '" + string + "'");
    }

    public static ParserResult<Boolean> checkStringEnds(StringSlice string, String suffix) {
        return StringParsers.checkStringEnds(string, suffix);
    }

    public static ParserResult<Boolean> checkStringEndsStrict(StringSlice string, String suffix) {
        ParserResult<Boolean> result = checkStringEnds(string, suffix);
        if (result.result()) {
            return result;
        }

        throw new RuntimeException("Expected string to end with '" + suffix + "', instead is '" + string + "'");
    }

    /**
     * 
     * @return true if the string starts with '->', false if it starts with '.',
     *         throws an exception otherwise
     */
    public static ParserResult<Boolean> checkArrow(StringSlice string) {
        if (string.startsWith("->")) {
            string = string.substring("->".length());
            return new ParserResult<>(string, true);
        }

        if (string.startsWith(".")) {
            string = string.substring(".".length());
            return new ParserResult<>(string, false);
        }

        throw new RuntimeException("Expected string to start with either -> or .");
    }

    /**
     * Starts at the end of the string, looking for a delimited by possibly nested
     * symbols 'start' and 'end'.
     * 
     * <p>
     * Example: ("a string <another string>", '<', '>') should return "another
     * string"
     *
     */
    public static ParserResult<String> reverseNested(StringSlice string, char start, char end) {
        Preconditions.checkArgument(!string.isEmpty());

        if (string.charAt(string.length() - 1) != end) {
            return new ParserResult<>(string, "");
        }

        // First character is termination at the end of string
        int counter = 1;
        int startIndex = string.length() - 1;
        while (counter > 0) {
            startIndex--;

            if (string.charAt(startIndex) == start) {
                counter--;
                continue;
            }

            if (string.charAt(startIndex) == end) {
                counter++;
            }
        }

        // Return string without separators
        String result = string.substring(startIndex + 1, string.length() - 1).toString();
        // Cut string from parser
        string = string.substring(0, startIndex);

        return new ParserResult<>(string, result);
    }

    /**
     * Receives a string starting with '0x' and interprets the next characters as an
     * hexadecimal number, until there is a whitespace or the string ends.
     * 
     * @return an Integer representing the decoded hexadecimal, or -1 if no hex was
     *         found
     */
    public static ParserResult<Long> parseHex(StringSlice string) {
        if (!string.startsWith("0x")) {
            return new ParserResult<>(string, -1L);
        }

        ParserResult<String> result = StringParsers.parseWord(string);

        string = result.modifiedString();
        String hexString = result.result();

        // CHECK: Does it ever enter here?
        if (hexString.isEmpty()) {
            return new ParserResult<>(string, 0L);
        }

        Long hexValue = Long.decode(hexString);

        return new ParserResult<>(string, hexValue);
    }

    /**
     * Receives a string ending with a 'word' starting with '0x' and interprets the
     * next characters as an hexadecimal number, until the string ends.
     * 
     * @return an Integer representing the decoded hexadecimal, or -1 if no hex was
     *         found
     */
    public static ParserResult<Long> reverseHex(StringSlice string) {
        int startIndex = string.lastIndexOf(' ');
        if (startIndex == -1) {
            startIndex = 0;
        }

        StringSlice hexString = string.substring(startIndex + 1, string.length());

        if (!hexString.startsWith("0x")) {
            return new ParserResult<>(string, -1L);
        }

        // CHECK: Does it ever enter here?
        if (hexString.isEmpty()) {
            return new ParserResult<>(string.substring(0, startIndex), 0L);
        }

        Long hexValue = Long.decode(hexString.toString());

        return new ParserResult<>(string.substring(0, startIndex), hexValue);
    }

    /**
     * Receives a string and interprets the next characters as an integer number,
     * until there is a whitespace or the string ends.
     * 
     * @return an Integer representing the decoded hexadecimal, or -1 if no hex was
     *         found
     */
    public static ParserResult<Integer> parseInt(StringSlice string) {
        return parseDecodedWord(string, Integer::decode, 0);
    }

    public static <T> ParserResult<T> parseDecodedWord(StringSlice string, Function<String, T> decoder, T emptyValue) {
        ParserResult<String> result = StringParsers.parseWord(string);

        string = result.modifiedString();
        String value = result.result();

        // CHECK: Does it ever enter here?
        if (value.isEmpty()) {
            return new ParserResult<>(string, emptyValue);
        }

        T decodedValue = decoder.apply(value);

        return new ParserResult<>(string, decodedValue);
    }

    public static <K extends Enum<K> & StringProvider> ParserResult<List<K>> parseElements(StringSlice string,
            EnumHelperWithValue<K> enumHelper) {

        List<K> parsedElements = new ArrayList<>();

        ParserResult<Optional<K>> element = StringParsers.checkEnum(string, enumHelper);
        while (element.result().isPresent()) {
            parsedElements.add(element.result().get());

            // Update string
            string = element.modifiedString();

            // Parse again
            element = StringParsers.checkEnum(string, enumHelper);
        }

        return new ParserResult<>(string, parsedElements);
    }

    /**
     * 
     * @return a string with all the contents of the StringSlice
     */
    public static ParserResult<String> getString(StringSlice string) {
        String result = string.toString();

        return new ParserResult<>(string.substring(string.length()), result);
    }

    /**
     * Parses a string between primes (e.g., 'a string').
     *
     */
    public static ParserResult<String> parsePrimes(StringSlice string) {
        return StringParsers.parseNested(string, '\'', '\'');
    }
}
