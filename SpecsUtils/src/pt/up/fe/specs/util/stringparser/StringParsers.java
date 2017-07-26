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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.stringparser;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import pt.up.fe.specs.util.Preconditions;
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
     * Receives a string starting with generic string separated by a whitespace, or the complete string if no whitespace
     * is found.
     *
     * @param string
     * @return
     */
    public static ParserResult<String> parseWord(StringSlice string) {
        int endIndex = string.indexOf(' ');
        if (endIndex == -1) {
            endIndex = string.length();
            // throw new RuntimeException("Expected a space in string '" + string + "'");
        }

        String element = string.substring(0, endIndex).toString();

        // Update slice
        string = string.substring(endIndex);

        return new ParserResult<>(string, element);
    }

    /**
     * Receives a string starting with generic string separated by a whitespace, or the compslete tring if no whitespace
     * is found.
     *
     * @param string
     * @return
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
        return checkCharacter(string, Arrays.asList(aChar));
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
     * @param string
     * @param prefix
     * @return
     */
    public static ParserResult<Optional<String>> checkStringStarts(StringSlice string, String prefix) {
        return checkStringStarts(string, prefix, true);
    }

    /**
     * Returns an optional with the string if it starts with the given prefix, removes it from parsing.
     *
     * @param string
     * @param prefix
     * @param caseSensitive
     * @return
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
     * Checks if it starts with the given String, but does not change the contents if it is either true of false.
     *
     * @param string
     * @param prefix
     * @param caseSensitive
     * @return
     */
    public static ParserResult<Boolean> peekStartsWith(StringSlice string, String prefix,
            boolean caseSensitive) {

        boolean startsWith = caseSensitive ? string.startsWith(prefix)
                : string.toString().toLowerCase().startsWith(prefix.toLowerCase());

        return new ParserResult<>(string, startsWith);
    }

    /**
     * String must start with double quote, and appends characters until there is an unescaped double quote.
     * 
     * @param string
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
                char escapedChar = (char) currentString.charAt(0);
                currentString = currentString.substring(1);

                contents.append(escapeString).append(escapedChar);
                continue;
            }

            if (currentString.startsWith(endString)) {
                // Drop end string
                currentString = currentString.substring(endString.length());
                return new ParserResult<String>(currentString, contents.toString());
            }

            char aChar = (char) currentString.charAt(0);
            currentString = currentString.substring(1);
            contents.append(aChar);
        }

        throw new RuntimeException(
                "Could not find a valid end delimiter ('" + endString + "') for string '" + string + "'");
    }

    /**
     * Receives a string starting with the given prefix, returns the prefix. Throws exception if the prefix is not
     * found.
     *
     * @param string
     * @return
     */
    public static ParserResult<String> parseString(StringSlice string, String prefix) {
        if (!string.startsWith(prefix)) {
            throw new RuntimeException("Expected string to start with '" + prefix + "': " + string);
        }

        StringSlice modifiedString = string.substring(prefix.length());

        return new ParserResult<>(modifiedString, prefix);
    }

}
