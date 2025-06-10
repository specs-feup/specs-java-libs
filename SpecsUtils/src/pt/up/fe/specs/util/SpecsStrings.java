/*
 * SpecsStrings.java
 *
 * Utility class for string-related operations, such as parsing, formatting, and manipulation. Provides static helper methods for working with strings in the SPeCS ecosystem.
 *
 * Copyright 2025 SPeCS Research Group.
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

package pt.up.fe.specs.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.collections.MultiMap;
import pt.up.fe.specs.util.exceptions.OverflowException;
import pt.up.fe.specs.util.parsing.LineParser;
import pt.up.fe.specs.util.utilities.LineStream;
import pt.up.fe.specs.util.utilities.StringLines;

/**
 * Utility methods for string operations.
 * <p>
 * Provides static helper methods for parsing, formatting, and manipulating strings.
 * </p>
 *
 * @author Joao Bispo
 */
public class SpecsStrings {

    private static final Map<Integer, String> SIZE_SUFFIXES;

    static {
        SIZE_SUFFIXES = new HashMap<>();
        SpecsStrings.SIZE_SUFFIXES.put(0, "bytes");
        SpecsStrings.SIZE_SUFFIXES.put(1, "KiB");
        SpecsStrings.SIZE_SUFFIXES.put(2, "MiB");
        SpecsStrings.SIZE_SUFFIXES.put(3, "GiB");
    }

    private static final Map<TimeUnit, String> TIME_UNIT_SYMBOL;
    static {
        TIME_UNIT_SYMBOL = new HashMap<>();
        SpecsStrings.TIME_UNIT_SYMBOL.put(TimeUnit.DAYS, "days");
        SpecsStrings.TIME_UNIT_SYMBOL.put(TimeUnit.HOURS, "h");
        SpecsStrings.TIME_UNIT_SYMBOL.put(TimeUnit.MICROSECONDS, "\u00B5s");
        SpecsStrings.TIME_UNIT_SYMBOL.put(TimeUnit.MILLISECONDS, "ms");
        SpecsStrings.TIME_UNIT_SYMBOL.put(TimeUnit.MINUTES, "m");
        SpecsStrings.TIME_UNIT_SYMBOL.put(TimeUnit.NANOSECONDS, "ns");
        SpecsStrings.TIME_UNIT_SYMBOL.put(TimeUnit.SECONDS, "s");
    }

    public static final Pattern INTEGER_PATTERN = Pattern.compile("^[+-]?[0-9]+$");
    public static final Pattern LINE_COUNTER_PATTERN = Pattern.compile("\r\n|\r|\n");

    /**
     * Checks if a character is printable.
     *
     * @param c the character to check
     * @return true if the character is printable, false otherwise
     */
    public static boolean isPrintableChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c)) &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Tries to parse a String into an integer. If an exception happens, warns the user and returns a 0.
     *
     * @param integer a String representing an integer
     * @return the integer represented by the string, or 0 if it couldn't be parsed
     */
    public static int parseInt(String integer) {
        int intResult = 0;
        try {
            intResult = Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            SpecsLogs.warn("Couldn''t parse '" + integer + "' into an integer. Returning " + intResult + ".");
        }

        return intResult;
    }

    /**
     * Tries to parse a String into an integer. If an exception happens, returns null.
     *
     * @param integer a String representing an integer
     * @return the integer represented by the string, or null if it couldn't be parsed
     */
    public static Integer parseInteger(String integer) {

        Integer intResult = null;
        try {
            intResult = Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            return null;
        }

        return intResult;
    }

    /**
     * Tries to parse a String into a double. If an exception happens, returns null.
     *
     * @param doublefloat a String representing a double
     * @return the double represented by the string, or null if it couldn't be parsed
     */
    public static Optional<Double> valueOfDouble(String doublefloat) {
        try {
            return Optional.of(Double.valueOf(doublefloat));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    /**
     * Parses a String into a short value.
     *
     * @param s the String to parse
     * @return the short value represented by the string
     */
    public static short parseShort(String s) {
        return Short.parseShort(s);
    }

    /**
     * Parses a String into a float value. If an exception happens or if it lowers precision, returns null.
     *
     * @param afloat a String representing a float
     * @param isStrict whether to enforce strict parsing
     * @return the float represented by the string, or null if it couldn't be parsed
     */
    public static Float parseFloat(String afloat, boolean isStrict) {
        Float floatResult = null;

        try {
            floatResult = Float.valueOf(afloat);
            if (isStrict && !afloat.equals(floatResult.toString())) {
                return null;
            }

        } catch (NumberFormatException e) {
            return null;
        }

        return floatResult;
    }

    /**
     * Parses a String into a double value. If an exception happens or if it lowers precision, returns null.
     *
     * @param aDouble a String representing a double
     * @param isStrict whether to enforce strict parsing
     * @return the double represented by the string, or null if it couldn't be parsed
     */
    public static Double parseDouble(String aDouble, boolean isStrict) {
        Double doubleResult = null;
        try {
            doubleResult = Double.valueOf(aDouble);
            if (isStrict && !aDouble.equals(doubleResult.toString())) {
                return null;
            }

        } catch (NumberFormatException e) {
            return null;
        }

        return doubleResult;
    }

    /**
     * Helper method using radix 10 as default.
     *
     * @param longNumber a String representing a long
     * @return the long represented by the string, or null if it couldn't be parsed
     */
    public static Long parseLong(String longNumber) {
        return parseLong(longNumber, 10);
    }

    /**
     * Tries to parse a String into a long. If an exception happens, returns null.
     *
     * @param longNumber a String representing a long
     * @param radix the radix to use for parsing
     * @return the long represented by the string, or null if it couldn't be parsed
     */
    public static Long parseLong(String longNumber, int radix) {

        Long longResult = null;
        try {
            longResult = Long.valueOf(longNumber, radix);
        } catch (NumberFormatException e) {
            return null;
        }

        return longResult;
    }

    /**
     * Tries to parse a String into a BigInteger. If an exception happens, returns null.
     *
     * @param intNumber a String representing an integer
     * @return the BigInteger represented by the string, or null if it couldn't be parsed
     */
    public static BigInteger parseBigInteger(String intNumber) {
        try {
            return new BigInteger(intNumber);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Tries to parse a String into a Boolean. If an exception happens, warns the user and returns null.
     *
     * @param booleanString a String representing a Boolean
     * @return the Boolean represented by the string, or null if it couldn't be parsed
     */
    public static Boolean parseBoolean(String booleanString) {
        booleanString = booleanString.toLowerCase();
        if (booleanString.equals("true")) {
            return true;
        } else if (booleanString.equals("false")) {
            return false;
        } else {
            SpecsLogs.getLogger().warning("Couldn''t parse '" + booleanString + "' into an Boolean.");
            return null;
        }
    }

    /**
     * Removes, from String text, the portion of text after the rightmost occurrence of the specified separator.
     *
     * @param text a string
     * @param separator a string
     * @return a string
     */
    public static String removeSuffix(String text, String separator) {
        int index = text.lastIndexOf(separator);

        if (index == -1) {
            return text;
        }

        return text.substring(0, index);
    }

    /**
     * Transforms the given integer into a hexadecimal string with the specified size.
     *
     * @param decimalInt an integer
     * @param stringSize the final number of digits in the hexadecimal representation
     * @return a string
     */
    public static String toHexString(int decimalInt, int stringSize) {
        String intString = Integer.toHexString(decimalInt).toUpperCase();
        intString = SpecsBits.padHexString(intString, stringSize);
        return intString;
    }

    /**
     * Transforms the given long into a hexadecimal string with the specified size.
     *
     * @param decimalLong a long
     * @param stringSize the final number of digits in the hexadecimal representation
     * @return a string
     */
    public static String toHexString(long decimalLong, int stringSize) {
        String longString = Long.toHexString(decimalLong).toUpperCase();
        longString = SpecsBits.padHexString(longString, stringSize);

        return longString;
    }

    /**
     * Finds the index of the first whitespace in the given string.
     *
     * @param string the string to search
     * @return the index of the first whitespace, or -1 if none is found
     */
    public static int indexOfFirstWhitespace(String string) {
        return indexOf(string, aChar -> Character.isWhitespace(aChar), false);
    }

    /**
     * Finds the index of a character in the given string based on a predicate.
     *
     * @param string the string to search
     * @param target the predicate to match the character
     * @param reverse whether to search in reverse order
     * @return the index of the character, or -1 if none is found
     */
    public static int indexOf(String string, Predicate<Character> target, boolean reverse) {

        if (reverse) {
            for (int i = string.length() - 1; i >= 0; i--) {
                if (target.test(string.charAt(i))) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < string.length(); i++) {
                if (target.test(string.charAt(i))) {
                    return i;
                }
            }
        }

        return -1;
    }

    /**
     * Adds spaces to the end of the given string until it has the desired size.
     *
     * @param string the string to pad
     * @param length the desired length of the string
     * @return the padded string
     */
    public static String padRight(String string, int length) {
        return String.format("%1$-" + length + "s", string);
    }

    /**
     * Adds spaces to the beginning of the given string until it has the desired size.
     *
     * @param string the string to pad
     * @param length the desired length of the string
     * @return the padded string
     */
    public static String padLeft(String string, int length) {
        return String.format("%1$#" + length + "s", string);
    }

    /**
     * Adds an arbitrary character to the beginning of the given string until it has the desired size.
     *
     * @param string the string to pad
     * @param length the desired length of the string
     * @param c the character to pad with
     * @return the padded string
     */
    public static String padLeft(String string, int length, char c) {
        if (string.length() >= length) {
            return string;
        }

        String returnString = string;
        int missingChars = length - string.length();
        for (int i = 0; i < missingChars; i++) {
            returnString = c + returnString;
        }

        return returnString;
    }

    /**
     * Returns a sorted list from the given collection.
     *
     * @param <T> the type of elements in the collection
     * @param collection the collection to sort
     * @return a sorted list
     */
    public static <T extends Comparable<? super T>> List<T> getSortedList(Collection<T> collection) {
        List<T> list = new ArrayList<>(collection);
        Collections.sort(list);
        return list;
    }

    /**
     * Reads a table file and returns a map with key-value pairs.
     *
     * @param tableFile the file to read
     * @param lineParser the parser to split lines
     * @return a map with key-value pairs
     */
    public static Map<String, String> parseTableFromFile(File tableFile, LineParser lineParser) {
        try (LineStream lineReader = LineStream.newInstance(tableFile)) {

            String line;
            Map<String, String> table = new HashMap<>();

            while ((line = lineReader.nextLine()) != null) {
                List<String> arguments = lineParser.splitCommand(line);

                if (arguments.isEmpty()) {
                    continue;
                }

                String key = null;
                String value = null;

                if (arguments.size() > 0) {
                    key = arguments.get(0);
                }

                if (arguments.size() > 1) {
                    value = arguments.get(1);
                } else {
                    value = "";
                }

                table.put(key, value);
            }

            return table;
        }
    }

    /**
     * Encodes a range of instructions into a hexadecimal string.
     *
     * @param firstAddress the first address
     * @param lastAddress the last address
     * @return the encoded range
     */
    public static String instructionRangeHexEncode(int firstAddress, int lastAddress) {
        return SpecsStrings.toHexString(firstAddress, 0) + SpecsStrings.RANGE_SEPARATOR
                + SpecsStrings.toHexString(lastAddress, 0);
    }

    /**
     * Decodes a hexadecimal string into a range of instructions.
     *
     * @param encodedRange the encoded range
     * @return a list of integers representing the range
     */
    public static List<Integer> instructionRangeHexDecode(String encodedRange) {
        String[] nums = encodedRange.split(SpecsStrings.RANGE_SEPARATOR);
        if (nums.length != 2) {
            SpecsLogs.getLogger().warning("Could not decode string '" + encodedRange + "'.");
            return null;
        }

        int start = Integer.decode("0x" + nums[0]);
        int end = Integer.decode("0x" + nums[1]);
        List<Integer> rangeNums = new ArrayList<>(2);
        rangeNums.add(start);
        rangeNums.add(end);
        return rangeNums;
    }

    /**
     * Transforms a package name into a folder name.
     *
     * @param packageName the package name
     * @return the folder name
     */
    public static String packageNameToFolderName(String packageName) {
        String newBasePackage = packageName.replace('.', '/');

        return newBasePackage;
    }

    /**
     * Transforms a package name into a folder.
     *
     * @param baseFolder the base folder
     * @param packageName the package name
     * @return the folder
     */
    public static File packageNameToFolder(File baseFolder, String packageName) {
        String packageFoldername = SpecsStrings.packageNameToFolderName(packageName);
        return SpecsIo.mkdir(baseFolder + "/" + packageFoldername);
    }

    /**
     * Replaces macros in a template with their corresponding values.
     *
     * @param template the template string
     * @param mappings the map of macros and their values
     * @return the replaced template
     */
    public static String replace(String template, Map<String, String> mappings) {

        for (String key : mappings.keySet()) {
            String macro = key;
            String replacement = mappings.get(key);

            template = template.replace(macro, replacement);
        }

        return template;
    }

    /**
     * Gets an element from a list using modulo indexing.
     *
     * @param <T> the type of elements in the list
     * @param list the list
     * @param index the index
     * @return the element at the index
     */
    public static <T> T moduloGet(List<T> list, int index) {
        if (list.isEmpty()) {
            return null;
        }

        index = modulo(index, list.size());
        return list.get(index);
    }

    /**
     * Calculates the modulo of a number.
     *
     * @param overIndex the number
     * @param size the size
     * @return the modulo
     */
    public static int modulo(int overIndex, int size) {

        overIndex = overIndex % size;
        if (overIndex < 0) {
            overIndex = overIndex + size;
        }

        return overIndex;
    }

    /**
     * Returns the first match of all capturing groups in a regex.
     *
     * @param contents the contents to search
     * @param regex the regex pattern
     * @return a list of captured groups
     */
    public static List<String> getRegex(String contents, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.MULTILINE);

        return getRegex(contents, pattern);
    }

    /**
     * Returns the first match of all capturing groups in a regex pattern.
     *
     * @param contents the contents to search
     * @param pattern the regex pattern
     * @return a list of captured groups
     */
    public static List<String> getRegex(String contents, Pattern pattern) {

        try {

            Matcher regexMatcher = pattern.matcher(contents);
            if (regexMatcher.find()) {
                int numGroups = regexMatcher.groupCount();
                List<String> capturedGroups = SpecsFactory.newArrayList();
                for (int i = 0; i < numGroups; i++) {
                    int groupIndex = i + 1;
                    capturedGroups.add(regexMatcher.group(groupIndex));
                }

                return capturedGroups;
            }
        } catch (PatternSyntaxException ex) {
            SpecsLogs.warn(ex.getMessage());
        }

        return Collections.emptyList();
    }

    /**
     * Checks if a regex pattern matches the contents.
     *
     * @param contents the contents to search
     * @param pattern the regex pattern
     * @return true if the pattern matches, false otherwise
     */
    public static boolean matches(String contents, Pattern pattern) {

        try {

            Matcher regexMatcher = pattern.matcher(contents);
            if (regexMatcher.find()) {
                return true;
            }
        } catch (PatternSyntaxException ex) {
            SpecsLogs.warn(ex.getMessage());
        }

        return false;
    }

    /**
     * Returns the specified capturing group from a regex match.
     *
     * @param contents the contents to search
     * @param regex the regex pattern
     * @param capturingGroupIndex the index of the capturing group
     * @return the captured group, or null if not found
     */
    public static String getRegexGroup(String contents, String regex, int capturingGroupIndex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.MULTILINE);

        return getRegexGroup(contents, pattern, capturingGroupIndex);
    }

    /**
     * Returns the specified capturing group from a regex match.
     *
     * @param contents the contents to search
     * @param pattern the regex pattern
     * @param capturingGroupIndex the index of the capturing group
     * @return the captured group, or null if not found
     */
    public static String getRegexGroup(String contents, Pattern pattern, int capturingGroupIndex) {

        String tester = null;

        try {

            Matcher regexMatcher = pattern.matcher(contents);
            if (regexMatcher.find()) {
                tester = regexMatcher.group(capturingGroupIndex);
            }
        } catch (PatternSyntaxException ex) {
            SpecsLogs.getLogger().warning(ex.getMessage());
        }

        return tester;
    }

    /**
     * Returns all matches of the specified capturing group from a regex pattern.
     *
     * @param contents the contents to search
     * @param regex the regex pattern
     * @param capturingGroupIndex the index of the capturing group
     * @return a list of captured groups
     */
    public static List<String> getRegexGroups(String contents, String regex, int capturingGroupIndex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.MULTILINE);
        return getRegexGroups(contents, pattern, capturingGroupIndex);
    }

    /**
     * Returns all matches of the specified capturing group from a regex pattern.
     *
     * @param contents the contents to search
     * @param pattern the regex pattern
     * @param capturingGroupIndex the index of the capturing group
     * @return a list of captured groups
     */
    public static List<String> getRegexGroups(String contents, Pattern pattern, int capturingGroupIndex) {

        List<String> results = SpecsFactory.newArrayList();

        try {

            Matcher regexMatcher = pattern.matcher(contents);
            while (regexMatcher.find()) {

                String result = regexMatcher.group(capturingGroupIndex);
                results.add(result);
            }
        } catch (PatternSyntaxException ex) {
            SpecsLogs.getLogger().warning(ex.getMessage());
        }

        return results;
    }

    /**
     * Transforms a number into a String.
     *
     * @param number the number to transform
     * @return the transformed string
     */
    @Deprecated
    public static String getAlphaId(int number) {

        String numberAsString = Integer.toString(number);
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < numberAsString.length(); i++) {
            char originalChar = numberAsString.charAt(i);
            int singleNumber = Character.getNumericValue(originalChar);
            char newChar = (char) (singleNumber + 65);
            builder.append(newChar);

        }

        return builder.toString();
    }

    /**
     * Converts a column number to an Excel column name.
     *
     * @param columnNumber the column number
     * @return the Excel column name
     */
    public static String toExcelColumn(int columnNumber) {
        int dividend = columnNumber;
        List<Character> reversedColumnName = new ArrayList<>();

        while (dividend > 0) {
            int modulo = (dividend - 1) % 26;
            reversedColumnName.add((char) (65 + modulo));
            dividend = (dividend - modulo) / 26;
        }

        Collections.reverse(reversedColumnName);

        return reversedColumnName.stream()
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    /**
     * Converts a TimeUnit to its string representation.
     *
     * @param timeUnit the TimeUnit to convert
     * @return the string representation
     */
    public static String toString(TimeUnit timeUnit) {
        switch (timeUnit) {
        case MICROSECONDS:
            return "us";
        case MILLISECONDS:
            return "ms";
        case NANOSECONDS:
            return "ns";
        case SECONDS:
            return "s";
        default:
            SpecsLogs.getLogger().warning("Case not defined:" + timeUnit);
            return "";
        }
    }

    /**
     * Converts a list to a string representation.
     *
     * @param <T> the type of elements in the list
     * @param list the list to convert
     * @return the string representation
     */
    public static <T> String toString(List<T> list) {
        StringBuilder builder = new StringBuilder();

        for (T element : list) {
            builder.append(element.toString()).append("\n");
        }

        return builder.toString();
    }

    /**
     * Converts a value from one TimeUnit to another.
     *
     * @param timeValue the value to convert
     * @param currentUnit the current TimeUnit
     * @param destinationUnit the destination TimeUnit
     * @return the converted value
     */
    public static double convert(double timeValue, TimeUnit currentUnit, TimeUnit destinationUnit) {
        long currentNanos = TimeUnit.NANOSECONDS.convert(1, currentUnit);
        long destinationNanos = TimeUnit.NANOSECONDS.convert(1, destinationUnit);

        double multiplier = (double) currentNanos / (double) destinationNanos;

        return timeValue * multiplier;
    }

    /**
     * Inverts a map for all non-null values.
     *
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @param aMap the map to invert
     * @return the inverted map
     */
    public static <K, V> HashMap<V, K> invertMap(Map<K, V> aMap) {
        HashMap<V, K> invertedMap = new HashMap<>();

        for (K key : aMap.keySet()) {
            V value = aMap.get(key);
            if (value == null) {
                continue;
            }

            invertedMap.put(value, key);
        }

        return invertedMap;
    }

    /**
     * Adds all elements of elementsMap to destinationMap. If any element is replaced, the key is added to the return
     * list.
     *
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @param destinationMap the destination map
     * @param elementsMap the elements map
     * @return a list of replaced keys
     */
    public static <K, V> List<K> putAll(Map<K, V> destinationMap, Map<K, V> elementsMap) {
        List<K> replacedKeys = new ArrayList<>();

        for (K key : elementsMap.keySet()) {
            V replacedValue = destinationMap.put(key, elementsMap.get(key));
            if (replacedValue != null) {
                replacedKeys.add(key);
            }
        }

        return replacedKeys;
    }

    /**
     * Checks if a mapping for a key in elementsMap is also present in destinationMap. If a key is present in both maps,
     * it is added to the return list.
     *
     * @param <K> the type of keys in the map
     * @param <V> the type of values in the map
     * @param destinationMap the destination map
     * @param elementsMap the elements map
     * @return a list of common keys
     */
    public static <K, V> List<K> check(Map<K, V> destinationMap, Map<K, V> elementsMap) {
        List<K> commonKeys = new ArrayList<>();

        for (K key : elementsMap.keySet()) {
            if (destinationMap.containsKey(key)) {
                commonKeys.add(key);
            }
        }

        return commonKeys;
    }

    /**
     * Gets the extension of a filename.
     *
     * @param hdlFilename the filename
     * @return the extension, or null if not found
     */
    public static String getExtension(String hdlFilename) {
        int separatorIndex = hdlFilename.lastIndexOf('.');
        if (separatorIndex == -1) {
            return null;
        }

        return hdlFilename.substring(separatorIndex + 1);
    }

    /**
     * Concatenates repetitions of the same element.
     *
     * @param element the element to repeat
     * @param numElements the number of repetitions
     * @return the concatenated string
     */
    public static String buildLine(String element, int numElements) {
        if (numElements == 0) {
            return "";
        }

        if (numElements == 1) {
            return element;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < numElements; i++) {
            builder.append(element);
        }
        return builder.toString();
    }

    public static final String RANGE_SEPARATOR = "-";

    /**
     * Gets the character at the specified index in the string.
     *
     * @param string the string
     * @param charIndex the index
     * @return the character, or null if the index is out of bounds
     */
    public static Character charAt(String string, int charIndex) {

        try {
            char c = string.charAt(charIndex);
            return c;
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

    }

    /**
     * Removes the given range of elements from the list.
     *
     * @param <T> the type of elements in the list
     * @param aList the list
     * @param startIndex the start index (inclusive)
     * @param endIndex the end index (exclusive)
     */
    public static <T> void remove(List<T> aList, int startIndex, int endIndex) {

        for (int i = endIndex - 1; i >= startIndex; i--) {
            aList.remove(i);
        }
    }

    /**
     * Removes the elements in the given indexes from the list.
     *
     * @param <T> the type of elements in the list
     * @param aList the list
     * @param indexes the indexes to remove
     */
    public static <T> void remove(List<T> aList, List<Integer> indexes) {
        Collections.sort(indexes);

        for (int i = indexes.size() - 1; i >= 0; i--) {
            aList.remove(indexes.get(i));
        }
    }

    /**
     * Inserts a separator in between CamelCase words.
     *
     * @param aString the string to separate
     * @param separator the separator to insert
     * @return the separated string
     */
    public static String camelCaseSeparate(String aString, String separator) {
        List<Integer> upperCaseLetters = new ArrayList<>();
        for (int i = 1; i < aString.length(); i++) {
            char c = aString.charAt(i);
            if (!Character.isUpperCase(c)) {
                continue;
            }

            upperCaseLetters.add(i);
        }

        String newString = aString;
        for (int i = upperCaseLetters.size() - 1; i >= 0; i--) {
            int index = upperCaseLetters.get(i);
            newString = newString.substring(0, index) + separator + newString.substring(index, newString.length());
        }

        return newString;
    }

    /**
     * Parses a template with tags and values.
     *
     * @param template the template string
     * @param defaultTagsAndValues the default tags and values
     * @param tagsAndValues the tags and values
     * @return the parsed template
     */
    public static String parseTemplate(String template, List<String> defaultTagsAndValues, String... tagsAndValues) {
        if (tagsAndValues.length % 2 != 0) {
            throw new RuntimeException("'tagsAndValues' length must be even");
        }

        template = applyTagsAndValues(template, Arrays.asList(tagsAndValues));

        defaultTagsAndValues = SpecsFactory.getUnmodifiableList(defaultTagsAndValues);
        template = applyTagsAndValues(template, defaultTagsAndValues);

        return template;
    }

    private static String applyTagsAndValues(String template, List<String> tagsAndValues) {

        String text = template;

        for (int i = 0; i < tagsAndValues.size(); i += 2) {
            String tag = tagsAndValues.get(i);

            String value = tagsAndValues.get(i + 1);
            text = text.replace(tag, value);
        }

        return text;
    }

    /**
     * Inverts the bits of a binary string.
     *
     * @param binaryString the binary string
     * @return the inverted binary string
     */
    public static String invertBinaryString(String binaryString) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < binaryString.length(); i++) {
            if (binaryString.charAt(i) == '0') {
                builder.append("1");
                continue;
            }

            if (binaryString.charAt(i) == '1') {
                builder.append("0");
                continue;
            }

            throw new RuntimeException("Character not supported:" + binaryString.charAt(i));
        }

        return builder.toString();
    }

    /**
     * Checks if a string is empty.
     *
     * @param string the string to check
     * @return true if the string is empty, false otherwise
     */
    public static boolean isEmpty(String string) {
        return string.length() == 0;
    }

    /**
     * Parses a number from a string.
     *
     * @param number the string to parse
     * @return the parsed number, or null if it couldn't be parsed
     */
    public static Number parseNumber(String number) {
        return parseNumber(number, true);
    }

    /**
     * Parses a number from a string.
     *
     * @param number the string to parse
     * @param verbose whether to log warnings
     * @return the parsed number, or null if it couldn't be parsed
     */
    public static Number parseNumber(String number, boolean verbose) {
        Number parsed = null;

        parsed = SpecsStrings.parseInteger(number);
        if (parsed != null) {
            return parsed;
        }

        parsed = SpecsStrings.parseLong(number);
        if (parsed != null) {
            return parsed;
        }

        parsed = SpecsStrings.parseFloat(number);
        if (parsed != null) {
            return parsed;
        }

        Optional<Double> parsedDouble = SpecsStrings.valueOfDouble(number);
        if (parsedDouble.isPresent()) {
            return parsedDouble.get();
        }

        try {
            Number parsedNumber = NumberFormat.getNumberInstance(Locale.US).parse(number);
            return parsedNumber;
        } catch (ParseException e) {
            if (verbose) {
                SpecsLogs.warn("Could not parse number '" + number + "', returning null");
            }
            return null;
        }

    }

    /**
     * Parses a duration in nanoseconds into a human-readable string.
     *
     * @param nanos the duration in nanoseconds
     * @return the human-readable string
     */
    public static String parseTime(double nanos) {
        return parseTime((long) nanos);
    }

    /**
     * Parses a duration in nanoseconds into a human-readable string.
     *
     * @param nanos the duration in nanoseconds
     * @return the human-readable string
     */
    public static String parseTime(long nanos) {
        NumberFormat doubleFormat = NumberFormat.getNumberInstance(Locale.UK);
        doubleFormat.setMaximumFractionDigits(2);

        double millis = (double) nanos / 1000000;

        if (millis < 1000) {
            return doubleFormat.format(millis) + "ms";
        }

        double secs = millis / 1000;
        if (secs < 60) {
            return doubleFormat.format(secs) + "s";
        }

        double mins = secs / 60.0;

        String min = "minute";
        int intMins = (int) mins;
        if (intMins != 1) {
            min = "minutes";
        }

        String sec = "second";
        int intSecs = (int) (secs - (intMins * 60));
        if (intSecs != 1) {
            sec = "seconds";
        }

        return new StringJoiner(" ")
                .add(Integer.toString(intMins))
                .add(min)
                .add(Integer.toString(intSecs))
                .add(sec)
                .toString();
    }

    /**
     * Decodes an integer from a string.
     *
     * @param number the string to decode
     * @return the decoded integer, or null if it couldn't be decoded
     */
    public static Integer decodeInteger(String number) {
        number = number.trim();

        Integer parsedNumber = null;
        try {
            Long longNumber = Long.decode(number);
            parsedNumber = longNumber.intValue();
        } catch (NumberFormatException ex) {
            SpecsLogs.warn("Could not decode '" + number + "' into an integer. Returning null");
            return null;
        }
        return parsedNumber;
    }

    /**
     * Decodes an integer from a string, returning a default value if an exception occurs.
     *
     * @param number the string to decode
     * @param defaultValue the default value supplier
     * @return the decoded integer, or the default value if an exception occurs
     */
    public static Integer decodeInteger(String number, Supplier<Integer> defaultValue) {
        if (number == null) {
            return defaultValue.get();
        }
        number = number.trim();

        try {
            return Integer.decode(number);
        } catch (Exception e) {
            SpecsLogs.msgInfo("Could not decode integer: " + e.getMessage());
            return defaultValue.get();
        }
    }

    /**
     * Decodes a long from a string, returning a default value if an exception occurs.
     *
     * @param number the string to decode
     * @param defaultValue the default value supplier
     * @return the decoded long, or the default value if an exception occurs
     */
    public static Long decodeLong(String number, Supplier<Long> defaultValue) {
        if (number == null) {
            return defaultValue.get();
        }
        number = number.trim();

        try {
            return Long.decode(number);
        } catch (Exception e) {
            SpecsLogs.msgInfo("Could not decode integer: " + e.getMessage());
            return defaultValue.get();
        }
    }

    /**
     * Decodes a double from a string, returning a default value if an exception occurs.
     *
     * @param number the string to decode
     * @param defaultValue the default value supplier
     * @return the decoded double, or the default value if an exception occurs
     */
    public static Double decodeDouble(String number, Supplier<Double> defaultValue) {
        number = number.trim();

        try {
            return Double.valueOf(number);
        } catch (Exception e) {
            SpecsLogs.msgInfo("Could not decode double: " + e.getMessage());
            return defaultValue.get();
        }
    }

    /**
     * Casts an object to a given type.
     *
     * @param <T> the type to cast to
     * @param object the object to cast
     * @param aClass the class to cast to
     * @return the casted object
     */
    public static <T> T cast(Object object, Class<T> aClass) {
        return cast(object, aClass, true);
    }

    /**
     * Casts an object to a given type, optionally throwing an exception if the cast fails.
     *
     * @param <T> the type to cast to
     * @param object the object to cast
     * @param aClass the class to cast to
     * @param throwException whether to throw an exception if the cast fails
     * @return the casted object, or null if the cast fails and throwException is false
     */
    public static <T> T cast(Object object, Class<T> aClass, boolean throwException) {

        if (!aClass.isInstance(object)) {
            if (throwException) {
                throw new RuntimeException("Object '" + object + "' with class '" + object.getClass()
                        + "' could not be cast to '" + aClass.getName() + "'");

            }

            return null;

        }

        return aClass.cast(object);
    }

    /**
     * Casts a list of objects to a list of the given type.
     *
     * @param <T> the type to cast to
     * @param objects the list of objects to cast
     * @param aClass the class to cast to
     * @param throwException whether to throw an exception if the cast fails
     * @return the casted list, or null if the cast fails and throwException is false
     */
    public static <T> List<T> castList(List<?> objects, Class<T> aClass, boolean throwException) {
        List<T> list = SpecsFactory.newArrayList();

        for (Object object : objects) {
            T castObject = cast(object, aClass, throwException);

            if (castObject == null) {
                return null;
            }

            list.add(castObject);
        }

        return list;
    }

    /**
     * Checks if a double value is an integer.
     *
     * @param variable the double value to check
     * @return true if the value is an integer, false otherwise
     */
    public static boolean isInteger(double variable) {
        if ((variable == Math.floor(variable)) && !Double.isInfinite(variable)) {
            return true;
        }

        return false;
    }

    /**
     * Gets the type parameter from a superclass.
     *
     * @param subclass the subclass
     * @return the type parameter
     */
    public static Class<?> getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        return (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    /**
     * Checks if a character is a letter.
     *
     * @param aChar the character to check
     * @return true if the character is a letter, false otherwise
     */
    public static boolean isLetter(char aChar) {
        if ((aChar >= 'a' && aChar <= 'z')
                || (aChar >= 'A' && aChar <= 'Z')) {

            return true;
        }

        return false;
    }

    /**
     * Checks if a character is a digit.
     *
     * @param aChar the character to check
     * @return true if the character is a digit, false otherwise
     */
    public static boolean isDigit(char aChar) {
        if (aChar >= '0' && aChar <= '9') {
            return true;
        }

        return false;
    }

    /**
     * Checks if a character is a digit or a letter.
     *
     * @param aChar the character to check
     * @return true if the character is a digit or a letter, false otherwise
     */
    public static boolean isDigitOrLetter(char aChar) {
        return isDigit(aChar) || isLetter(aChar);
    }

    /**
     * Converts a package name to a resource path.
     *
     * @param packageName the package name
     * @return the resource path
     */
    public static String packageNameToResource(String packageName) {
        String resourceName = packageName.replace('.', '/');

        if (!resourceName.endsWith("/")) {
            resourceName += "/";
        }

        return resourceName;
    }

    /**
     * Parses an integer from a string, allowing relaxed parsing.
     *
     * @param constant the string to parse
     * @return the parsed integer
     */
    public static int parseIntegerRelaxed(String constant) {
        Preconditions.checkArgument(constant != null);

        double doubleConstant = Double.parseDouble(constant);

        if (!(doubleConstant >= Integer.MIN_VALUE && doubleConstant <= Integer.MAX_VALUE)) {
            throw new OverflowException("Number in size is too large");
        }
        if (Math.floor(doubleConstant) != doubleConstant) {
            throw new RuntimeException("Dimension argument must be an integer.");
        }

        return (int) doubleConstant;

    }

    /**
     * Converts a string to lowercase using the UK locale.
     *
     * @param string the string to convert
     * @return the lowercase string
     */
    public static String toLowerCase(String string) {
        return string.toLowerCase(Locale.UK);
    }

    /**
     * Parses a size in bytes into a human-readable string.
     *
     * @param bytes the size in bytes
     * @return the human-readable string
     */
    public static String parseSize(long bytes) {
        long currentBytes = bytes;
        int counter = 0;

        while (currentBytes > 1024 && counter <= SpecsStrings.SIZE_SUFFIXES.size()) {
            currentBytes = currentBytes / 1024;
            counter++;
        }

        return currentBytes + " " + SpecsStrings.SIZE_SUFFIXES.get(counter);
    }

    /**
     * Converts a string to its byte representation.
     *
     * @param string the string to convert
     * @param encoding the encoding to use
     * @return the byte representation
     */
    public static String toBytes(String string, String enconding) {
        try {
            byte[] bytes = string.getBytes(enconding);

            StringBuilder builder = new StringBuilder();
            for (byte aByte : bytes) {
                builder.append(String.format("%02X", aByte));
            }
            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not get the bytes of the string '" + string + "'", e);
        }

    }

    /**
     * Converts a byte representation to a string.
     *
     * @param text the byte representation
     * @param encoding the encoding to use
     * @return the string
     */
    public static String fromBytes(String text, String encoding) {
        byte[] bytes = new byte[(text.length() / 2)];

        for (int i = 0; i < text.length(); i += 2) {
            bytes[i / 2] = Byte.parseByte(text.substring(i, i + 2), 16);
        }

        try {
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not encode bytes", e);
        }
    }

    /**
     * Parses a duration in nanoseconds into a human-readable string with a message.
     *
     * @param message the message
     * @param nanoDuration the duration in nanoseconds
     * @return the human-readable string
     */
    public static String parseTime(String message, long nanoDuration) {
        return parseTime(message, TimeUnit.MILLISECONDS, nanoDuration);
    }

    /**
     * Parses a duration in nanoseconds into a human-readable string with a message and time unit.
     *
     * @param message the message
     * @param timeUnit the time unit
     * @param nanoDuration the duration in nanoseconds
     * @return the human-readable string
     */
    public static String parseTime(String message, TimeUnit timeUnit, long nanoDuration) {
        String unitString = timeUnit.toString();
        if (timeUnit == TimeUnit.MILLISECONDS) {
            unitString = "ms";
        }

        return message + ": " + timeUnit.convert(nanoDuration, TimeUnit.NANOSECONDS) + unitString;
    }

    /**
     * Measures the time taken from a given start until the call of this function.
     *
     * @param message the message
     * @param nanoStart the start time in nanoseconds
     * @return the human-readable string
     */
    public static String takeTime(String message, long nanoStart) {
        return takeTime(message, TimeUnit.MILLISECONDS, nanoStart);
    }

    /**
     * Prints the time taken from a given start until the call of this function.
     *
     * @param message the message
     * @param nanoStart the start time in nanoseconds
     */
    public static void printTime(String message, long nanoStart) {
        SpecsLogs.msgInfo(takeTime(message, nanoStart));
    }

    /**
     * Measures the time taken from a given start until the call of this function with a specific time unit.
     *
     * @param message the message
     * @param timeUnit the time unit
     * @param nanoStart the start time in nanoseconds
     * @return the human-readable string
     */
    public static String takeTime(String message, TimeUnit timeUnit, long nanoStart) {
        long toc = System.nanoTime();

        String unitString = SpecsStrings.TIME_UNIT_SYMBOL.get(timeUnit);
        if (unitString == null) {
            unitString = timeUnit.toString();
        }

        return message + ": " + timeUnit.convert(toc - nanoStart, TimeUnit.NANOSECONDS) + unitString;
    }

    /**
     * Gets the symbol for a time unit.
     *
     * @param timeunit the time unit
     * @return the symbol
     */
    public static String getTimeUnitSymbol(TimeUnit timeunit) {

        String symbol = SpecsStrings.TIME_UNIT_SYMBOL.get(timeunit);
        if (symbol != null) {
            return symbol;
        }

        SpecsLogs.warn("Case not defined:" + timeunit);
        return timeunit.name();
    }

    /**
     * Counts the number of occurrences of a character in a string.
     *
     * @param string the string
     * @param aChar the character
     * @return the number of occurrences
     */
    public static int count(String string, char aChar) {
        int counter = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == aChar) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * Counts the number of lines in a string.
     *
     * @param string the string
     * @param trim whether to trim the string
     * @return the number of lines
     */
    public static int countLines(String string, boolean trim) {

        if (trim) {
            string = string.trim();
        }

        if (string.isEmpty()) {
            return 0;
        }

        Matcher m = LINE_COUNTER_PATTERN.matcher(string);
        int lines = 1;
        while (m.find()) {
            lines++;
        }

        return lines;
    }

    /**
     * Removes all occurrences of a pattern from a string.
     *
     * @param string the string
     * @param pattern the pattern
     * @return the string with the pattern removed
     */
    public static String remove(String string, String pattern) {
        String currentString = string;

        int classIndex = -1;
        while ((classIndex = currentString.indexOf(pattern)) != -1) {
            currentString = currentString.subSequence(0, classIndex)
                    + currentString.substring(classIndex + pattern.length());
        }

        return currentString;
    }

    /**
     * Splits command line arguments, minding characters such as \".
     *
     * @param string the string to split
     * @return the list of arguments
     */
    public static List<String> splitArgs(String string) {
        List<String> args = new ArrayList<>();
        StringBuilder currentString = new StringBuilder();
        boolean doubleQuoteActive = false;
        for (int i = 0; i < string.length(); i++) {

            char currentChar = string.charAt(i);

            if (currentChar == ' ' && !doubleQuoteActive) {
                String arg = currentString.toString();
                if (!arg.isEmpty()) {
                    currentString = new StringBuilder();
                    addArgs(args, arg);
                }

                continue;
            }

            if (currentChar == '"') {
                doubleQuoteActive = !doubleQuoteActive;
                continue;
            }

            currentString.append(currentChar);
        }

        if (currentString.length() > 0) {
            addArgs(args, currentString.toString());
        }

        return args;
    }

    private static void addArgs(List<String> arguments, String argument) {
        String currentArgument = argument;

        if (currentArgument.contains(" ")) {
            currentArgument = "\"" + currentArgument + "\"";
        }

        arguments.add(currentArgument);
    }

    /**
     * Escapes a string for JSON.
     *
     * @param string the string to escape
     * @return the escaped string
     */
    public static String escapeJson(String string) {
        return escapeJson(string, false);
    }

    /**
     * Escapes a string for JSON, optionally ignoring newlines.
     *
     * @param string the string to escape
     * @param ignoreNewlines whether to ignore newlines
     * @return the escaped string
     */
    public static String escapeJson(String string, boolean ignoreNewlines) {

        SpecsCheck.checkNotNull(string, () -> "Cannot escape a null string");

        StringBuilder escapedString = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char currentChar = string.charAt(i);

            if (currentChar == '\b') {
                escapedString.append("\\b");
                continue;
            }

            if (currentChar == '\f') {
                escapedString.append("\\f");
                continue;
            }
            if (currentChar == '\n') {
                if (!ignoreNewlines) {
                    escapedString.append("\\n");
                }

                continue;
            }
            if (currentChar == '\r') {
                if (!ignoreNewlines) {
                    escapedString.append("\\r");
                }

                continue;
            }
            if (currentChar == '\t') {
                escapedString.append("\\t");
                continue;
            }

            if (currentChar == '\"' || currentChar == '\\') {
                escapedString.append("\\");
            }

            escapedString.append(currentChar);
        }

        return escapedString.toString();
    }

    /**
     * Converts a string to camelCase.
     *
     * @param string the string to convert
     * @return the camelCase string
     */
    public static String toCamelCase(String string) {
        return toCamelCase(string, "_", true);
    }

    /**
     * Converts a string to camelCase with a specific separator and capitalization.
     *
     * @param string the string to convert
     * @param separator the separator
     * @param capitalizeFirstLetter whether to capitalize the first letter
     * @return the camelCase string
     */
    public static String toCamelCase(String string, String separator, boolean capitalizeFirstLetter) {

        String[] words = string.split(separator);

        String camelCaseString = Arrays.stream(words)
                .filter(word -> !word.isEmpty())
                .map(word -> word.toLowerCase())
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                .collect(Collectors.joining());

        if (!capitalizeFirstLetter) {
            camelCaseString = Character.toLowerCase(camelCaseString.charAt(0)) + camelCaseString.substring(1);
        }

        return camelCaseString;
    }

    /**
     * Normalizes file contents.
     *
     * @param fileContents the file contents
     * @param ignoreEmptyLines whether to ignore empty lines
     * @return the normalized file contents
     */
    public static String normalizeFileContents(String fileContents, boolean ignoreEmptyLines) {

        String normalizedString = fileContents.replaceAll("\r\n", "\n");

        if (ignoreEmptyLines) {
            normalizedString = StringLines.getLines(normalizedString).stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.joining("\n"));
        }

        return normalizedString;

    }

    /**
     * Normalizes file contents without ignoring empty lines.
     *
     * @param fileContents the file contents
     * @return the normalized file contents
     */
    public static String normalizeFileContents(String fileContents) {
        return normalizeFileContents(fileContents, false);
    }

    /**
     * Tries to parse a decimal integer from a string.
     *
     * @param value the string to parse
     * @return the parsed integer, or empty if the string is not an integer
     */
    public static Optional<Integer> tryGetDecimalInteger(String value) {
        Preconditions.checkArgument(value != null, "value must not be null");

        if (INTEGER_PATTERN.matcher(value).matches()) {
            try {
                return Optional.of(Integer.parseInt(value, 10));
            } catch (NumberFormatException e) {
                SpecsLogs.warn("Unexpected NumberFormatException for " + value);
            }
        }

        return Optional.empty();
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes the byte array
     * @return the hexadecimal string
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Converts a fraction to a percentage string.
     *
     * @param fraction the fraction
     * @return the percentage string
     */
    public static String toPercentage(double fraction) {
        return MessageFormat.format("{0,number,#.##%}", fraction);
    }

    /**
     * Converts a size in bytes to a human-readable string.
     *
     * @param bytes the size in bytes
     * @param si whether to use SI units
     * @return the human-readable string
     */
    public static String toBytes(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    /**
     * Converts a size in bytes to a human-readable string using non-SI units.
     *
     * @param bytes the size in bytes
     * @return the human-readable string
     */
    public static String toBytes(long bytes) {
        return toBytes(bytes, false);
    }

    /**
     * Removes all whitespace from a string.
     *
     * @param string the string
     * @return the string without whitespace
     */
    public static String removeWhitespace(String string) {
        return string.replaceAll("\\s+", "");
    }

    /**
     * Finds the index of the closing parenthesis corresponding to the first open parenthesis in a string.
     *
     * @param string the string
     * @return the index of the closing parenthesis
     */
    public static int findCloseParenthesisIndex(String string) {
        int openParIndex = string.indexOf('(');

        int innerOpenPars = 0;
        for (int i = openParIndex + 1; i < string.length(); i++) {
            if (string.charAt(i) == ')') {
                if (innerOpenPars == 0) {
                    return i;
                } else {
                    innerOpenPars--;
                }

                continue;
            }

            if (string.charAt(i) == '(') {
                innerOpenPars++;
                continue;
            }
        }

        throw new RuntimeException("Could not find a matching closing parenthesis for the open parenthesis at index "
                + openParIndex + " in the string '" + string + "'");
    }

    /**
     * Converts a number to a decimal string with commas.
     *
     * @param number the number
     * @return the decimal string
     */
    public static String toDecimal(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(number);
    }

    /**
     * Splits a string into non-empty substrings using a separator.
     *
     * @param string the string to split
     * @param separator the separator
     * @param strip whether to strip each substring
     * @return the list of non-empty substrings
     */
    public static List<String> splitNonEmpty(String string, String separator, boolean strip) {
        return Arrays.stream(string.split(separator))
                .filter(substring -> !substring.isBlank())
                .map(substring -> strip ? substring.strip() : substring)
                .collect(Collectors.toList());
    }

    /**
     * Parses a list of paths with prefixes.
     *
     * @param pathList the list of paths
     * @param separator the separator
     * @return a map of prefixes and paths
     */
    public static MultiMap<String, String> parsePathList(String pathList, String separator) {
        MultiMap<String, String> prefixPaths = new MultiMap<>();

        String currentString = pathList;

        int dollarIndex = -1;
        while ((dollarIndex = currentString.indexOf('$')) != -1) {

            if (dollarIndex > 0) {
                String beforeDollar = currentString.substring(0, dollarIndex);
                prefixPaths.addAll("", SpecsStrings.splitNonEmpty(beforeDollar, separator, true));
            }

            currentString = currentString.substring(dollarIndex + 1);

            dollarIndex = currentString.indexOf('$');
            if (dollarIndex == -1) {
                throw new RuntimeException("Expected an even number of $");
            }

            String prefix = currentString.substring(0, dollarIndex);
            currentString = currentString.substring(dollarIndex + 1);

            dollarIndex = currentString.indexOf('$');
            String paths = dollarIndex == -1 ? currentString : currentString.substring(0, dollarIndex);

            prefixPaths.addAll(prefix, SpecsStrings.splitNonEmpty(paths, separator, true));

            currentString = dollarIndex == -1 ? "" : currentString.substring(dollarIndex, currentString.length());
        }

        if (!currentString.isEmpty()) {
            prefixPaths.addAll("", SpecsStrings.splitNonEmpty(currentString, separator, true));
        }

        return prefixPaths;

    }

    /**
     * Finds all indexes of a character in a string.
     *
     * @param string the string
     * @param ch the character
     * @return the list of indexes
     */
    public static List<Integer> indexesOf(String string, int ch) {
        List<Integer> indexes = new ArrayList<>();

        int currentIndex = string.indexOf(ch);
        String currentString = string;
        int offset = 0;
        while (currentIndex != -1) {
            indexes.add(currentIndex + offset);
            offset += currentString.subSequence(0, currentIndex + 1).length();
            currentString = currentString.substring(currentIndex + 1);
            currentIndex = currentString.indexOf(ch);

        }

        return indexes;
    }

    /**
     * Converts a number to an array of digits.
     *
     * @param number the number
     * @return the array of digits
     */
    public static int[] toDigits(Number number) {
        return toDigits(number.toString());
    }

    /**
     * Converts a string to an array of digits.
     *
     * @param number the string
     * @return the array of digits
     */
    public static int[] toDigits(String number) {
        int[] digits = new int[number.length()];

        for (int i = 0; i < number.length(); i++) {
            digits[i] = Integer.valueOf(number.substring(i, i + 1));
        }

        return digits;
    }

    /**
     * Checks if a string is a palindrome.
     *
     * @param string the string to check
     * @return true if the string is a palindrome, false otherwise
     */
    public static boolean isPalindrome(String string) {
        int length = string.length();

        if (length == 1) {
            return true;
        }

        int middleIndex = length / 2;

        String firstHalf = string.substring(0, middleIndex);
        String secondHalf = string.substring(length - middleIndex, length);

        return firstHalf.equals(new StringBuilder(secondHalf).reverse().toString());
    }

    /**
     * Returns null if the string is blank, otherwise returns the string.
     *
     * @param string the string
     * @return null if the string is blank, otherwise the string
     */
    public static String nullIfEmpty(String string) {
        return string.isBlank() ? null : string;
    }

    /**
     * Checks if two strings are identical, ignoring empty spaces.
     *
     * @param expected the expected string
     * @param actual the actual string
     * @return true if the strings are identical, false otherwise
     */
    public static boolean check(String expected, String actual) {

        actual = normalizeFileContents(actual, true);
        expected = normalizeFileContents(expected, true);

        return actual.equals(expected);
    }

    /**
     * Normalizes a string to represent a JSON object.
     *
     * @param json the string
     * @return the normalized JSON object
     */
    public static String normalizeJsonObject(String json) {
        return normalizeJsonObject(json, null);
    }

    /**
     * Normalizes a string to represent a JSON object with a base folder.
     *
     * @param json the string
     * @param baseFolder the base folder
     * @return the normalized JSON object
     */
    public static String normalizeJsonObject(String json, File baseFolder) {

        if (json.endsWith(".json")) {
            var jsonFile = new File(json);

            if (baseFolder != null && !jsonFile.isAbsolute()) {
                jsonFile = new File(baseFolder, jsonFile.getPath());
            }

            if (!jsonFile.isFile()) {
                throw new RuntimeException(
                        "Tried to interpret '" + jsonFile + "' as a JSON file, but file does not exist.");
            }

            return SpecsIo.read(jsonFile);
        }

        var normalizedJson = json.trim();

        if (!normalizedJson.startsWith("{")) {
            normalizedJson = "{" + normalizedJson + "}";
        }

        return normalizedJson;
    }

    /**
     * Gets the last character in a string.
     *
     * @param string the string
     * @return the last character
     */
    public static char lastChar(String string) {
        if (string.isEmpty()) {
            throw new RuntimeException("String is empty");
        }

        return string.charAt(string.length() - 1);
    }

    /**
     * Sanitizes a string representing a single name of a path.
     *
     * @param pathName the path name
     * @return the sanitized path name
     */
    public static String sanitizePath(String pathName) {
        var sanitizedString = pathName;

        sanitizedString = sanitizedString.replace(' ', '_');
        sanitizedString = sanitizedString.replace('(', '_');
        sanitizedString = sanitizedString.replace(')', '_');

        return sanitizedString;
    }
}
