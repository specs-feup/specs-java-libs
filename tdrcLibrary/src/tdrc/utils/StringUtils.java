/**
 * Copyright 2015 SPeCS Research Group.
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

package tdrc.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility class for string operations in tdrcLibrary.
 * <p>
 * This class provides various utility methods for string manipulation,
 * including sanitization, case conversion, joining strings, package comparison,
 * and XML conversion.
 */
public class StringUtils {

    static final String keywordPrefix = "_";
    static final String keywords[] = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
            "class", "const", "continue", "default", "do", "double", "else", "extends", "false", "final", "finally",
            "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
            "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
            "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void",
            "volatile", "while" };

    /**
     * Verifies if a given String is a reserved keyword of Java.
     * 
     * @param keyword the string to check
     * @return true if the string is a reserved keyword, false otherwise
     */
    public static boolean isJavaKeyword(String keyword) {
        if (keyword == null) {
            return false;
        }
        return (Arrays.binarySearch(StringUtils.keywords, keyword) >= 0);
    }

    /**
     * Returns a sanitized string for the given name, i.e., ensures that the name is
     * not a reserved keyword.
     * 
     * @param name the string to sanitize
     * @return the sanitized string
     */
    public static String getSanitizedName(String name) {
        if (isJavaKeyword(name)) {
            name = StringUtils.keywordPrefix + name;
        }
        return name;
    }

    /**
     * Converts the first character of the given string to uppercase.
     * 
     * @param string the input string
     * @return the string with the first character converted to uppercase
     */
    public static String firstCharToUpper(String string) {
        return charToUpperOrLower(string, 0, true);
    }

    /**
     * Converts the first character of the given string to lowercase.
     * 
     * @param string the input string
     * @return the string with the first character converted to lowercase
     */
    public static String firstCharToLower(String string) {
        return charToUpperOrLower(string, 0, false);
    }

    /**
     * Converts the character at the specified position in the given string to upper
     * or lower case.
     * 
     * @param string the input string
     * @param pos    the position of the character to convert
     * @param upper  if true, converts the character to uppercase; if false,
     *               converts to lowercase
     * @return the string with the character at the specified position converted
     */
    public static String charToUpperOrLower(String string, int pos, boolean upper) {
        if (pos < 0 || pos >= string.length()) {
            throw new StringIndexOutOfBoundsException(pos);
        }
        String ret = string.substring(0, pos);
        if (upper) {
            ret += string.substring(pos, pos + 1).toUpperCase();
        } else {
            ret += string.substring(pos, pos + 1).toLowerCase();
        }
        ret += string.substring(pos + 1);
        return ret;
    }

    /**
     * Joins the elements of a collection into a single string, separated by the
     * given separator. This method requires a mapping function to convert the
     * elements into strings.
     * 
     * @param collection the collection of elements to join
     * @param mapper     the function to map elements to strings
     * @param separator  the separator to use between elements
     * @return the joined string
     */
    public static <T> String join(Collection<T> collection, Function<T, String> mapper, String separator) {
        return collection.stream().map(mapper).collect(Collectors.joining(separator));
    }

    /**
     * Joins the elements of a collection into a single string, separated by the
     * given separator. This method uses the toString method for each element.
     * 
     * @param collection the collection of elements to join
     * @param separator  the separator to use between elements
     * @return the joined string
     */
    public static <T> String join(Collection<T> collection, String separator) {
        return join(collection, obj -> obj == null ? "null" : obj.toString(), separator);
    }

    /**
     * Gets the package from a given class name.
     * 
     * @param className the name of the class
     * @return the package if present in the class name (name contains '.'), empty
     *         string otherwise
     */
    public static String getPackage(String className) {
        final int lastDot = className.lastIndexOf('.');
        if (lastDot > -1) {
            return className.substring(0, lastDot);
        }
        return "";
    }
}
