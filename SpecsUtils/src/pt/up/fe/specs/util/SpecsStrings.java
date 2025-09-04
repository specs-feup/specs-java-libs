/*
 * Copyright 2009 SPeCS Research Group.
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
import java.text.DecimalFormatSymbols;
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
 * Utility methods for parsing of values which, instead of throwing an
 * exception, return a default value if a parsing
 * error occurs.
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
        SpecsStrings.TIME_UNIT_SYMBOL.put(TimeUnit.MICROSECONDS, "µs");
        SpecsStrings.TIME_UNIT_SYMBOL.put(TimeUnit.MILLISECONDS, "ms");
        SpecsStrings.TIME_UNIT_SYMBOL.put(TimeUnit.MINUTES, "m");
        SpecsStrings.TIME_UNIT_SYMBOL.put(TimeUnit.NANOSECONDS, "ns");
        SpecsStrings.TIME_UNIT_SYMBOL.put(TimeUnit.SECONDS, "s");
    }

    public static final Pattern INTEGER_PATTERN = Pattern.compile("^[+-]?[0-9]+$");
    public static final Pattern LINE_COUNTER_PATTERN = Pattern.compile("\r\n|\r|\n");

    public static boolean isPrintableChar(char c) {
        Character.UnicodeBlock block = Character.UnicodeBlock.of(c);
        return (!Character.isISOControl(c)) &&
                block != null &&
                block != Character.UnicodeBlock.SPECIALS;
    }

    private final static char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Tries to parse a String into a integer. If an exception happens, warns the
     * user and returns a 0.
     *
     * @param integer a String representing an integer.
     * @return the intenger represented by the string, or 0 if it couldn't be
     *         parsed.
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
     * Tries to parse a String into a integer. If an exception happens, returns
     * null.
     *
     * @param integer a String representing an integer.
     * @return the integer represented by the string, or null if it couldn't be
     *         parsed.
     */
    public static Integer parseInteger(String integer) {
        try {
            return Integer.parseInt(integer);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Tries to parse a String into a double. If an exception happens, returns null.
     *
     * @param doublefloat a String representing a double.
     * @return the double represented by the string, or null if it couldn't be
     *         parsed.
     */
    public static Optional<Double> valueOfDouble(String doublefloat) {
        try {
            return Optional.of(Double.valueOf(doublefloat));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    public static short parseShort(String s) {
        return Short.parseShort(s);
    }

    /**
     * Overload that sets 'isStrict' to true.
     *
     * @param afloat a String representing a float.
     * @return the float represented by the string, or null if it couldn't be
     *         parsed.
     */
    public static Float parseFloat(String afloat) {
        return parseFloat(afloat, true);
    }

    /**
     * Tries to parse a String into a float. If an exception happens or if it lowers
     * precision, returns null.
     *
     * @param afloat a String representing a float.
     */
    public static Float parseFloat(String afloat, boolean isStrict) {
        try {
            Float floatResult = Float.valueOf(afloat);
            if (isStrict && !afloat.equals(floatResult.toString())) {
                return null;
            }
            return floatResult;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Overload that sets 'isStrict' to true.
     *
     * @param aDouble a String representing a double.
     * @return the double represented by the string, or null if it couldn't be
     *         parsed.
     */
    public static Double parseDouble(String aDouble) {
        return parseDouble(aDouble, true);
    }

    /**
     * Tries to parse a String into a double. If an exception happens or if it
     * lowers precision, returns null.
     *
     * @param aDouble a String representing a double.
     * @return the double represented by the string, or null if it couldn't be
     *         parsed.
     */
    public static Double parseDouble(String aDouble, boolean isStrict) {
        try {
            Double doubleResult = Double.valueOf(aDouble);
            if (isStrict && !aDouble.equals(doubleResult.toString())) {
                return null;
            }
            return doubleResult;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Helper method using radix 10 as default.
     */
    public static Long parseLong(String longNumber) {
        return parseLong(longNumber, 10);
    }

    /**
     * Tries to parse a String into a long. If an exception happens, returns null.
     *
     * @param longNumber a String representing a long
     * @return the long represented by the string, or 0L if it couldn't be parsed
     */
    public static Long parseLong(String longNumber, int radix) {
        try {
            return Long.valueOf(longNumber, radix);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Tries to parse a String into a BigInteger. If an exception happens, returns
     * null.
     *
     * @param intNumber a String representing an integer.
     * @return the long represented by the string, or 0L if it couldn't be parsed.
     */
    public static BigInteger parseBigInteger(String intNumber) {
        try {
            return new BigInteger(intNumber);
        } catch (NumberFormatException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Tries to parse a String into a Boolean. If an exception happens, warns the
     * user and returns null.
     *
     * @param booleanString a String representing a Boolean.
     * @return the Boolean represented by the string, or null if it couldn't be
     *         parsed.
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
     * Removes, from String text, the portion of text after the rightmost occurrence
     * of the specified separator.
     *
     * <p>
     * Ex.: removeSuffix("readme.txt", ".") <br>
     * Returns "readme".
     *
     * @param text      a string
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
     * Transforms the given long in an hexadecimal string with the specified size.
     *
     * <p>
     * Ex.: toHexString(10, 2) <br>
     * Returns 0x0A.
     *
     * @param decimalInt a int
     * @param stringSize the final number of digits in the hexadecimal
     *                   representation
     * @return a string
     */
    public static String toHexString(int decimalInt, int stringSize) {
        String intString = Integer.toHexString(decimalInt).toUpperCase();
        intString = SpecsBits.padHexString(intString, stringSize);
        return intString;
    }

    /**
     * Transforms the given long in an hexadecimal string with the specified size.
     *
     * <p>
     * Ex.: toHexString(10, 2) <br>
     * Returns 0x0A.
     *
     * @param decimalLong a long
     * @param stringSize  the final number of digits in the hexadecimal
     *                    representation
     * @return a string
     */
    public static String toHexString(long decimalLong, int stringSize) {
        String longString = Long.toHexString(decimalLong).toUpperCase();
        longString = SpecsBits.padHexString(longString, stringSize);

        return longString;
    }

    /**
     * @param string a string
     * @return the index of the first whitespace found in the given String, or -1 if
     *         none is found.
     */
    public static int indexOfFirstWhitespace(String string) {
        return indexOf(string, Character::isWhitespace, false);
    }

    public static int indexOf(String string, Predicate<Character> target, boolean reverse) {

        // Test reverse order
        if (reverse) {
            for (int i = string.length() - 1; i >= 0; i--) {
                if (target.test(string.charAt(i))) {
                    return i;
                }
            }
        }
        // Test original order
        else {
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
     * @param string a string
     * @param length the size we want the string to be
     * @return the string, with the desired size
     */
    public static String padRight(String string, int length) {
        return String.format("%1$-" + length + "s", string);
    }

    /**
     * Adds spaces to the beginning of the given string until it has the desired
     * size.
     *
     * @param string a string
     * @param length length the size we want the string to be
     * @return the string, with the desired size
     */
    public static String padLeft(String string, int length) {
        return padLeft(string, length, ' ');
    }

    /**
     * Adds an arbitrary character to the beginning of the given string until it has
     * the desired size.
     *
     * @param string a string
     * @param length length the size we want the string to be
     * @param c      the character to pad with
     * @return the string, with the desired size
     */
    public static String padLeft(String string, int length, char c) {
        if (string.length() >= length) {
            return string;
        }

        StringBuilder returnString = new StringBuilder(string);
        int missingChars = length - string.length();
        for (int i = 0; i < missingChars; i++) {
            returnString.insert(0, c);
        }

        return returnString.toString();
    }

    public static <T extends Comparable<? super T>> List<T> getSortedList(Collection<T> collection) {
        List<T> list = new ArrayList<>(collection);
        // Collections.sort((List)list);
        Collections.sort(list);
        return list;
    }

    /**
     * Reads a Table file and returns a table with the key-value pairs.
     *
     * <p>
     * Any line with one or more parameters, as defined by the object LineParser is
     * put in the table. The first parameters is used as the key, and the second as
     * the value. <br>
     * If a line has more than two parameters, they are ignored. <br>
     * If a line has only a single parameters, the second parameters is assumed to
     * be an empty string.
     *
     * @return a table with key-value pairs.
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
                String value;

                if (!arguments.isEmpty()) {
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
     * Addresses are converted to hex representation.
     *
     */
    public static String instructionRangeHexEncode(int firstAddress, int lastAddress) {
        return SpecsStrings.toHexString(firstAddress, 0) + SpecsStrings.RANGE_SEPARATOR
                + SpecsStrings.toHexString(lastAddress, 0);
    }

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
     * <p>
     * Ex.: org.company.program -> org/company/program
     *
     */
    public static String packageNameToFolderName(String packageName) {

        return packageName.replace('.', '/');
    }

    /**
     * Transforms a package name into a folder.
     *
     * <p>
     * Ex.: E:/folder, org.company.program -> E:/folder/org/company/program/
     *
     */
    public static File packageNameToFolder(File baseFolder, String packageName) {
        String packageFoldername = SpecsStrings.packageNameToFolderName(packageName);
        return SpecsIo.mkdir(baseFolder + "/" + packageFoldername);
    }

    public static String replace(String template, Map<String, String> mappings) {

        // iterate over the map
        for (var entry : mappings.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // replace all occurrences of the key in the template with the value
            template = template.replace(key, value);

        }

        return template;
    }

    /**
     * Interprets the index as a modulo of the list size.
     *
     */
    public static <T> T moduloGet(List<T> list, int index) {
        if (list.isEmpty()) {
            return null;
        }

        index = modulo(index, list.size());
        return list.get(index);
    }

    public static int modulo(int overIndex, int size) {

        overIndex = overIndex % size;
        if (overIndex < 0) {
            overIndex = overIndex + size;
        }

        return overIndex;
    }

    /**
     * Returns the first match of all capturing groups.
     *
     */
    public static List<String> getRegex(String contents, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.MULTILINE);

        return getRegex(contents, pattern);
    }

    public static List<String> getRegex(String contents, Pattern pattern) {
        List<String> matches = new ArrayList<>();
        try {
            Matcher regexMatcher = pattern.matcher(contents);
            while (regexMatcher.find()) {
                matches.add(regexMatcher.group()); // group() returns the full match
            }
        } catch (PatternSyntaxException ex) {
            SpecsLogs.warn(ex.getMessage());
        }
        return matches;
    }

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

    public static String getRegexGroup(String contents, String regex, int capturingGroupIndex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.MULTILINE);

        return getRegexGroup(contents, pattern, capturingGroupIndex);
    }

    public static String getRegexGroup(String contents, Pattern pattern, int capturingGroupIndex) {

        String tester = null;

        try {

            Matcher regexMatcher = pattern.matcher(contents);
            if (regexMatcher.find()) {
                tester = regexMatcher.group(capturingGroupIndex);
            }
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
            SpecsLogs.getLogger().warning(ex.getMessage());
        }

        return tester;
    }

    public static List<String> getRegexGroups(String contents, String regex, int capturingGroupIndex) {
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.MULTILINE);
        return getRegexGroups(contents, pattern, capturingGroupIndex);
    }

    public static List<String> getRegexGroups(String contents, Pattern pattern, int capturingGroupIndex) {

        List<String> results = new ArrayList<>();

        try {

            Matcher regexMatcher = pattern.matcher(contents);
            while (regexMatcher.find()) {

                String result = regexMatcher.group(capturingGroupIndex);
                results.add(result);
            }
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
            SpecsLogs.getLogger().warning(ex.getMessage());
        }

        return results;
    }

    /**
     * Transforms a number into a String.
     *
     * <p>
     * Example: <br>
     * 0 -> A <br>
     * 1 -> B <br>
     * ... <br>
     * 23 -> AA
     *
     * @deprecated replace with toExcelColumn
     */
    @Deprecated
    public static String getAlphaId(int number) {

        // Portuguese alphabet (23 letters, skipping K, W, Y)
        final char[] PT_ALPHABET = {
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'X', 'Z'
        };
        final int ALPHABET_SIZE = PT_ALPHABET.length;
        if (number < 0) {
            throw new IllegalArgumentException("Number must be non-negative");
        }
        StringBuilder sb = new StringBuilder();
        int n = number;
        do {
            int rem = n % ALPHABET_SIZE;
            sb.append(PT_ALPHABET[rem]);
            n = n / ALPHABET_SIZE - 1;
        } while (n >= 0);
        return sb.reverse().toString();
    }

    /**
     * Based on this algorithm:
     * <a href=
     * "https://stackoverflow.com/questions/181596/how-to-convert-a-column-number-eg-127-into-an-excel-column-eg-aa">...</a>
     *
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

    public static String toString(TimeUnit timeUnit) {
        return switch (timeUnit) {
            case NANOSECONDS -> "ns";
            case MICROSECONDS -> "us";
            case MILLISECONDS -> "ms";
            case SECONDS -> "s";
            case MINUTES -> "m";
            case HOURS -> "h";
            case DAYS -> "d";
            default -> {
                SpecsLogs.getLogger().warning("Case not defined:" + timeUnit);
                yield "";
            }
        };
    }

    public static <T> String toString(List<T> list) {
        StringBuilder builder = new StringBuilder();

        for (T element : list) {
            builder.append(element.toString()).append("\n");
        }

        return builder.toString();
    }

    /**
     * Converts a value from a TimeUnit to another TimeUnit.
     *
     */
    public static double convert(double timeValue, TimeUnit currentUnit, TimeUnit destinationUnit) {
        // Convert to nanos since it is the smallest TimeUnit, and will not
        // return zero as result.
        long currentNanos = TimeUnit.NANOSECONDS.convert(1, currentUnit);
        long destinationNanos = TimeUnit.NANOSECONDS.convert(1, destinationUnit);

        double multiplier = (double) currentNanos / (double) destinationNanos;

        return timeValue * multiplier;
    }

    /**
     * Inverts the table for all non-null values.
     *
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
     * Adds all elements of elementsMap to destinationMap. If any element is
     * replaced, the key in put in the return list.
     *
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
     * Checks if a mapping for a key in elementsMap is also present to
     * destinationMap. If a key is present in both maps, it is added to the return
     * list.
     *
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
     * <p>
     * Ex.: element "Sa" and numElements "2" returns "SaSa". <br>
     * If numElements is zero, returns an empty string. If numElements is one,
     * returns the string itself.
     *
     */
    public static String buildLine(String element, int numElements) {
        if (numElements == 0) {
            return "";
        }

        if (numElements == 1) {
            return element;
        }

        // Build line string
        return String.valueOf(element).repeat(Math.max(0, numElements));
    }

    public static final String RANGE_SEPARATOR = "-";

    public static Character charAt(String string, int charIndex) {

        if (string == null || string.isEmpty()) {
            return null;
        }

        try {
            return string.charAt(charIndex);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }

    }

    /**
     * Removes the given range of elements from the list.
     *
     * @param startIndex (inclusive)
     * @param endIndex   (exclusive)
     */
    public static <T> void remove(List<T> aList, int startIndex, int endIndex) {
        if (endIndex > startIndex) {
            aList.subList(startIndex, endIndex).clear();
        }
    }

    /**
     * Removes the elements in the given indexes from the list.
     *
     */
    public static <T> void remove(List<T> aList, List<Integer> indexes) {
        // Sort indexes
        Collections.sort(indexes);

        for (int i = indexes.size() - 1; i >= 0; i--) {
            aList.remove(indexes.get(i));
        }
    }

    /**
     * Inserts a String in between CamelCase.
     *
     * <p>
     * Example <br>
     * aString: CamelCase <br>
     * separator: <empty space>
     * <p>
     * Output: Camel Case
     *
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

        // Insert string in the found indexes
        String newString = aString;
        for (int i = upperCaseLetters.size() - 1; i >= 0; i--) {
            int index = upperCaseLetters.get(i);
            newString = newString.substring(0, index) + separator + newString.substring(index);
        }

        return newString;
    }

    /**
     * Accepts tag-value pairs and replaces the tags in the given template for the
     * specified values.
     *
     */
    public static String parseTemplate(String template, List<String> defaultTagsAndValues, String... tagsAndValues) {
        if (tagsAndValues.length % 2 != 0) {
            throw new RuntimeException("'tagsAndValues' length must be even");
        }

        // Apply given tags
        template = applyTagsAndValues(template, Arrays.asList(tagsAndValues));

        // Apply default values
        defaultTagsAndValues = List.copyOf(defaultTagsAndValues);
        template = applyTagsAndValues(template, defaultTagsAndValues);

        return template;
    }

    private static String applyTagsAndValues(String template, List<String> tagsAndValues) {

        String text = template;

        // Apply tags and values
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
     */
    public static String invertBinaryString(String binaryString) {
        // Invert bits
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

    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Helper method which sets verbose to true.
     *
     */
    public static Number parseNumber(String number) {
        return parseNumber(number, true);
    }

    /**
     * Tries to parse a number according to a number of classes, in the following
     * order:<br>
     * - Integer<br>
     * - Long<br>
     * - Float<br>
     * - Double<br>
     *
     * <p>
     * If all these fail, parses a number according to US locale using NumberFormat.
     *
     */
    public static Number parseNumber(String number, boolean verbose) {
        Number parsed;

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
            return NumberFormat.getNumberInstance(Locale.US).parse(number);
        } catch (ParseException e) {
            if (verbose) {
                SpecsLogs.warn("Could not parse number '" + number + "', returning null");
            }
            return null;
        }

    }

    /**
     * Helper method that accepts a double
     *
     * @see SpecsStrings#parseTime(long)
     */
    public static String parseTime(double nanos) {
        return parseTime((long) nanos);
    }

    /**
     * Transforms a number of nano-seconds into a string, trying to find what should
     * be the best time unit.
     *
     */
    public static String parseTime(long nanos) {
        NumberFormat doubleFormat = NumberFormat.getNumberInstance(Locale.UK);
        doubleFormat.setMaximumFractionDigits(2);

        if (nanos < 1000) {
            return doubleFormat.format(nanos) + "ns";
        }

        double micros = (double) nanos / 1000;
        if (micros < 1000) {
            return doubleFormat.format(micros) + "us";
        }

        double millis = micros / 1000;
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
     * Decodes an integer, returns null if an exception happens.
     *
     */
    public static Integer decodeInteger(String number) {
        try {
            Long longNumber = Long.decode(number.trim());
            return longNumber.intValue();
        } catch (NumberFormatException ex) {
            SpecsLogs.warn("Could not decode '" + number + "' into an integer. Returning null");
            return null;
        }
    }

    /**
     * Returns the default value if there is an exception.
     *
     */
    public static Integer decodeInteger(String number, Supplier<Integer> defaultValue) {
        if (number == null) {
            return defaultValue.get();
        }
        // Trim input
        number = number.trim();

        try {
            return Integer.decode(number);
        } catch (Exception e) {
            SpecsLogs.msgInfo("Could not decode integer: " + e.getMessage());
            return defaultValue.get();
        }
    }

    /**
     * Returns the default value if there is an exception.
     *
     */
    public static Long decodeLong(String number, Supplier<Long> defaultValue) {
        if (number == null) {
            return defaultValue.get();
        }
        // Trim input
        number = number.trim();

        try {
            return Long.decode(number);
        } catch (Exception e) {
            SpecsLogs.msgInfo("Could not decode integer: " + e.getMessage());
            return defaultValue.get();
        }
    }

    public static Double decodeDouble(String number, Supplier<Double> defaultValue) {
        // Trim input
        number = number.trim();

        try {
            return Double.valueOf(number);
        } catch (Exception e) {
            SpecsLogs.msgInfo("Could not decode double: " + e.getMessage());
            return defaultValue.get();
        }
    }

    /**
     * Test if the given object implements the given class. If true, casts the
     * object to the class type. Otherwise, throws an exception.
     *
     */
    public static <T> T cast(Object object, Class<T> aClass) {
        return cast(object, aClass, true);
    }

    /**
     * Casts an object to a given type.
     *
     * <p>
     * If the object could not be cast to the given type and throwException is
     * false, returns null. If throwException is true, throws an exception.
     *
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
     * Casts a list of objects to a List of the given type.
     *
     * <p>
     * If any of the objects in the list could not be cast to the given type and
     * throwException is false, returns null.
     * If throwException is true, throws an exception.
     *
     */
    public static <T> List<T> castList(List<?> objects, Class<T> aClass, boolean throwException) {
        List<T> list = new ArrayList<>();

        for (Object object : objects) {
            T castObject = cast(object, aClass, throwException);

            if (castObject == null) {
                return null;
            }

            list.add(castObject);
        }

        return list;
    }

    public static boolean isInteger(double variable) {
        return (variable == Math.floor(variable)) && !Double.isInfinite(variable);
    }

    /**
     * Gets type from super class's type parameter.
     */
    public static Class<?> getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        return (Class<?>) ((ParameterizedType) superclass).getActualTypeArguments()[0];
    }

    public static boolean isLetter(char aChar) {
        return (aChar >= 'a' && aChar <= 'z')
                || (aChar >= 'A' && aChar <= 'Z');
    }

    public static boolean isDigit(char aChar) {
        return aChar >= '0' && aChar <= '9';
    }

    public static boolean isDigitOrLetter(char aChar) {
        return isDigit(aChar) || isLetter(aChar);
    }

    /**
     * Replaces '.' in the package with '/', and suffixes '/' to the String, if
     * necessary.
     *
     */
    public static String packageNameToResource(String packageName) {
        String resourceName = packageName.replace('.', '/');

        if (!resourceName.endsWith("/")) {
            resourceName += "/";
        }

        return resourceName;
    }

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

    public static String toLowerCase(String string) {
        return string.toLowerCase(Locale.UK);
    }

    /**
     * Transforms a number of bytes into a string.
     *
     */
    public static String parseSize(long bytes) {
        long currentBytes = bytes;
        int counter = 0;

        // Greater or equal because table has an entry for the value 0
        while (currentBytes >= 1024 && counter <= SpecsStrings.SIZE_SUFFIXES.size()) {
            currentBytes = currentBytes / 1024;
            counter++;
        }

        return currentBytes + " " + SpecsStrings.SIZE_SUFFIXES.get(counter);
    }

    /**
     * Transforms a String of characters into a String of bytes.
     *
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
     * Converts a string representing 8-bit bytes into a String.
     *
     */
    public static String fromBytes(String text, String encoding) {
        byte[] bytes = new byte[(text.length() / 2)];

        for (int i = 0; i < text.length(); i += 2) {
            bytes[i / 2] = (byte) Integer.parseInt(text.substring(i, i + 2), 16);
        }

        try {
            return new String(bytes, encoding);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Could not encode bytes", e);
        }
    }

    /**
     * Helper method which uses milliseconds as the target unit.
     *
     *
     */
    public static String parseTime(String message, long nanoDuration) {
        return parseTime(message, TimeUnit.MILLISECONDS, nanoDuration);
    }

    /**
     * Shows a message and the time in the given time unit
     *
     */
    public static String parseTime(String message, TimeUnit timeUnit, long nanoDuration) {
        String unitString = timeUnit.toString();
        if (timeUnit == TimeUnit.MILLISECONDS) {
            unitString = "ms";
        }

        return message + ": " + timeUnit.convert(nanoDuration, TimeUnit.NANOSECONDS) + unitString;
    }

    /**
     * Helper method which uses milliseconds as the target unit.
     *
     */
    public static String takeTime(String message, long nanoStart) {
        return takeTime(message, TimeUnit.MILLISECONDS, nanoStart);
    }

    public static void printTime(String message, long nanoStart) {
        SpecsLogs.msgInfo(takeTime(message, nanoStart));
    }

    /**
     * Measures the take taken from a given start until the call of this function.
     *
     */
    public static String takeTime(String message, TimeUnit timeUnit, long nanoStart) {
        long toc = System.nanoTime();

        String unitString = SpecsStrings.TIME_UNIT_SYMBOL.get(timeUnit);
        if (unitString == null) {
            unitString = timeUnit.toString();
        }

        return message + ": " + timeUnit.convert(toc - nanoStart, TimeUnit.NANOSECONDS) + unitString;
    }

    public static String getTimeUnitSymbol(TimeUnit timeunit) {

        String symbol = SpecsStrings.TIME_UNIT_SYMBOL.get(timeunit);
        if (symbol != null) {
            return symbol;
        }

        SpecsLogs.warn("Case not defined:" + timeunit);
        return timeunit.name();
    }

    /**
     * Counts the number of occurences of the given char in the given String.
     *
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
     * Counts the number of lines in the given String.
     *
     * <p>
     * Taken from here:
     * <a href=
     * "https://stackoverflow.com/questions/2850203/count-the-number-of-lines-in-a-java-string#2850259">...</a>
     *
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
     * Remove all occurrences of 'match' from 'string'.
     *
     */
    public static String remove(String string, String match) {
        String currentString = string;

        int classIndex = -1;
        while ((classIndex = currentString.indexOf(match)) != -1) {
            // Remove match
            currentString = currentString.subSequence(0, classIndex)
                    + currentString.substring(classIndex + match.length());
        }

        return currentString;
    }

    /**
     * Splits command line arguments, minding characters such as \"
     *
     */
    public static List<String> splitArgs(String string) {
        List<String> args = new ArrayList<>();
        StringBuilder currentString = new StringBuilder();
        boolean doubleQuoteActive = false;
        for (int i = 0; i < string.length(); i++) {

            char currentChar = string.charAt(i);

            // If space is found, store current string, if not empty
            if (currentChar == ' ' && !doubleQuoteActive) {
                String arg = currentString.toString();
                if (!arg.isEmpty()) {
                    currentString = new StringBuilder();
                    addArgs(args, arg);
                }

                continue;
            }

            // Arguments that were quoted will loose the quotes.
            // Otherwise, when using the resulting list to execute the program, it would add
            // quotes again
            if (currentChar == '"') {
                doubleQuoteActive = !doubleQuoteActive;
                continue;
            }

            currentString.append(currentChar);
        }

        if (!currentString.isEmpty()) {
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

    public static String escapeJson(String string) {
        return escapeJson(string, false);
    }

    public static String escapeJson(String string, boolean ignoreNewlines) {

        SpecsCheck.checkNotNull(string, () -> "Cannot escape a null string");

        StringBuilder escapedString = new StringBuilder();

        for (int i = 0; i < string.length(); i++) {
            char currentChar = string.charAt(i);

            // \b\f\n\r\t\"\\
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
     * Overload which uses '_' as separator and capitalizes the first letter.
     *
     */
    public static String toCamelCase(String string) {
        return toCamelCase(string, "_", true);
    }

    /**
     * Overload which lets select the used separator and capitalizes the first
     * letter.
     *
     */
    public static String toCamelCase(String string, String separator) {
        return toCamelCase(string, separator, true);
    }

    /**
     * Transforms a string into camelCase.
     *
     * <p>
     * E.g., if separator is '_' and string is 'SOME_STRING', returns 'SomeString'-
     *
     */
    public static String toCamelCase(String string, String separator, boolean capitalizeFirstLetter) {

        // Escape the separator to be used in regex
        String escapedSeparator = Pattern.quote(separator);

        // Split string using provided separator
        String[] words = string.split(escapedSeparator);

        String camelCaseString = Arrays.stream(words)
                // Remove empty words
                .filter(word -> !word.isEmpty())
                // Make word lowerCase
                .map(String::toLowerCase)
                // Capitalize first character
                .map(word -> Character.toUpperCase(word.charAt(0)) + word.substring(1))
                // Concatenate
                .collect(Collectors.joining());

        // If first letter should not be capitalize, make it lower case
        if (!capitalizeFirstLetter) {
            camelCaseString = Character.toLowerCase(camelCaseString.charAt(0)) + camelCaseString.substring(1);
        }

        return camelCaseString;
    }

    /**
     * Normalizes file contents:
     * <p>
     * 1) Replaces \r\n with \n <br>
     * 2) Trims lines and removes empty lines
     *
     */
    public static String normalizeFileContents(String fileContents, boolean ignoreEmptyLines) {

        // Normalize new lines
        String normalizedString = fileContents.replaceAll("\r\n", "\n");

        // Remove empty lines
        if (ignoreEmptyLines) {
            normalizedString = StringLines.getLines(normalizedString).stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .collect(Collectors.joining("\n"));
        }

        return normalizedString;

    }

    /**
     * Helper method which does not ignore empty lines.
     *
     */
    public static String normalizeFileContents(String fileContents) {
        return normalizeFileContents(fileContents, false);
    }

    /**
     * Returns an integer from a decimal string such as "123". If the string does
     * not contain just a decimal integer
     * (such as " 123" or "12x") then this returns empty.
     *
     * @param value The string to convert to int. Must not be null.
     * @return The parsed integer, or empty if the string is not an integer.
     */
    public static Optional<Integer> tryGetDecimalInteger(String value) {
        Preconditions.checkArgument(value != null, "value must not be null");

        if (INTEGER_PATTERN.matcher(value).matches()) {
            try {
                return Optional.of(Integer.parseInt(value, 10));
            } catch (NumberFormatException e) {
                // Since we check the regex, this should never happen
                SpecsLogs.warn("Unexpected NumberFormatException for " + value);
            }
        }

        return Optional.empty();
    }

    /**
     * Basen on
     * <a href=
     * "https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java">...</a>
     *
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

    public static String toPercentage(double fraction) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(',');
        DecimalFormat df = new DecimalFormat("##0.00", symbols);
        return df.format(fraction * 100) + "%";
    }

    /**
     * Taken from here:
     * <a href=
     * "https://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java#3758880">...</a>
     *
     */
    public static String toBytes(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit)
            return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp - 1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

    public static String toBytes(long bytes) {
        return toBytes(bytes, false);
    }

    public static String removeWhitespace(String string) {
        return string.replaceAll("\\s+", "");
    }

    /**
     * Given a string with an open-close parenthesis, returns the closing
     * parenthesis corresponding to the first open parenthesis it finds.
     *
     * <p>
     * If no matching closing parenthesis is found, throws an Exception.
     *
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

    public static String toDecimal(long number) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(number);
    }

    /**
     * Splits the given String according to a separator, and removes blank String
     * that can be created from the splitting.
     *
     * @param strip     if true, strips each splitted String
     */
    public static List<String> splitNonEmpty(String string, String separator, boolean strip) {
        return Arrays.stream(string.split(separator))
                .filter(substring -> !substring.isBlank())
                .map(substring -> strip ? substring.strip() : substring)
                .collect(Collectors.toList());
    }

    /**
     * Parses a list of paths.
     *
     * <p>
     * A sequence of paths may be prefixed with a $PREFIX$, the paths after the
     * second $ will be prefixed with PREFIX, until a new $PREFIX$ appears. PREFIX
     * can be empty.
     *
     * <p>
     * Example (; as separator): path1$prefix/$path2;path3$$path4 returns a Map
     * where "" (empty string) is mapped to path1 and path4, and "prefix" is mapped
     * to path2 and path3
     *
     *
     */
    public static MultiMap<String, String> parsePathList(String pathList, String separator) {

        if (pathList == null || pathList.isBlank()) {
            return new MultiMap<>();
        }

        // Separate into prefixes
        MultiMap<String, String> prefixPaths = new MultiMap<>();

        String currentString = pathList;

        int dollarIndex = -1;
        while ((dollarIndex = currentString.indexOf('$')) != -1) {

            // Add what is before the dollar
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

            // Update string
            currentString = dollarIndex == -1 ? "" : currentString.substring(dollarIndex);
        }

        // Parse remaining string to the empty prefix
        if (!currentString.isEmpty()) {
            prefixPaths.addAll("", SpecsStrings.splitNonEmpty(currentString, separator, true));
        }

        return prefixPaths;

    }

    /**
     * All indexes where the given char appears on the String.
     *
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

    public static int[] toDigits(Number number) {
        return toDigits(number.toString());
    }

    public static int[] toDigits(String number) {
        int[] digits = new int[number.length()];

        for (int i = 0; i < number.length(); i++) {
            digits[i] = Integer.parseInt(number.substring(i, i + 1));
        }

        return digits;
    }

    public static boolean isPalindrome(String string) {
        int length = string.length();

        // If only one character, return true
        if (length == 1) {
            return true;
        }

        int middleIndex = length / 2;

        String firstHalf = string.substring(0, middleIndex);
        String secondHalf = string.substring(length - middleIndex, length);

        return firstHalf.contentEquals(new StringBuilder(secondHalf).reverse());
    }

    /**
     * If the String is blank, returns null. Returns the string otherwise.
     *
     */
    public static String nullIfEmpty(String string) {
        return string.isBlank() ? null : string;
    }

    /**
     * Checks if two strings are identical, not considering empty spaces. Returns
     * false if strings do not match.
     */
    public static boolean check(String expected, String actual) {

        // Normalize both strings
        actual = normalizeFileContents(actual, true);
        expected = normalizeFileContents(expected, true);

        return actual.equals(expected);
    }

    /**
     * Normalizes the given string so that it represents a JSON object.
     *
     * <p>
     * - If the input is a single string that ends in .json, interprets as an
     * existing file whose contents will be returned;<br>
     * - If the string does not start with { or ends with }, introduces those
     * characters;
     *
     */
    public static String normalizeJsonObject(String json) {
        return normalizeJsonObject(json, null);
    }

    /**
     *
     * @param json       JSON string
     * @param baseFolder if json represents a relative path to a json file and
     *                   baseFolder is not null, uses baseFolder as the
     *                   parent of the relative path
     */
    public static String normalizeJsonObject(String json, File baseFolder) {
        // Check if string is an existing JSON file

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

        // Normalize to object
        if (!normalizedJson.startsWith("{")) {
            normalizedJson = "{" + normalizedJson + "}";
        }

        return normalizedJson;
    }

    /**
     *
     * @return the last char in the String or throws exception if String is empty
     */
    public static char lastChar(String string) {
        if (string.isEmpty()) {
            throw new RuntimeException("String is empty");
        }

        return string.charAt(string.length() - 1);
    }

    /**
     * Sanitizes a string representing a single name of a path. Currently replaces '
     * ', '(' and ')' with '_'
     *
     */
    public static String sanitizePath(String pathName) {
        var sanitizedString = pathName;

        sanitizedString = sanitizedString.replace(' ', '_');
        sanitizedString = sanitizedString.replace('(', '_');
        sanitizedString = sanitizedString.replace(')', '_');

        return sanitizedString;
    }
}
