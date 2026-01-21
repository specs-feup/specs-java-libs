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

import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

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
     * given separator.
     * 
     * @param collection the collection of strings to join
     * @param separator  the separator to use between elements
     * @return the joined string
     */
    @Deprecated
    public static String joinStrings(Collection<String> collection, String separator) {
        return String.join(separator, collection);
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
     * Compares the package of two classes.
     * 
     * @param firstClassName  the name of the first class
     * @param secondClassName the name of the second class
     * @return true if both classes are in the same package, false otherwise
     */
    public static boolean inSamePackage(String firstClassName, String secondClassName) {
        final String firstPackage = getPackage(firstClassName);
        final String secondPackage = getPackage(secondClassName);
        return firstPackage.equals(secondPackage);
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

    /**
     * Repeats a given string a specified number of times.
     * <p>
     * Conditions:<br>
     * - toRepeat == <b>null</b> || repeat < 0 -> <b>null</b><br>
     * - repeat == 0 -> ""<br>
     * - toRepeat.isEmpty || repeat == 1 -> toRepeat<br>
     * - else -> toRepeat * repeat
     * 
     * @param toRepeat the string to repeat
     * @param repeat   the number of times to repeat the string
     * @return the repeated string
     * 
     * @deprecated Use {@link String#repeat(int)} instead, which is available in
     *             Java 11 and later.
     */
    @Deprecated
    public static String repeat(String toRepeat, int repeat) {
        return toRepeat.repeat(repeat);
    }

    /**
     * Converts an XML Document to a StringBuffer with the specified indentation
     * amount.
     * 
     * @param doc         the XML Document to convert
     * @param identAmount the amount of indentation
     * @return the StringBuffer representation of the XML Document
     * @throws IllegalArgumentException             if doc is null
     * @throws TransformerFactoryConfigurationError if there is a configuration
     *                                              error in the TransformerFactory
     * @throws TransformerConfigurationException    if there is a configuration
     *                                              error in the Transformer
     * @throws TransformerException                 if there is an error during the
     *                                              transformation
     */
    public static StringBuffer xmlToStringBuffer(Document doc, int identAmount)
            throws IllegalArgumentException, TransformerFactoryConfigurationError, TransformerConfigurationException,
            TransformerException {
        if (doc == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        final TransformerFactory transfac = TransformerFactory.newInstance();
        final Transformer trans = transfac.newTransformer();

        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        trans.setOutputProperty(OutputKeys.INDENT, "yes");
        trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(identAmount));
        // create string from xml tree
        final StringWriter sw = new StringWriter();
        final StreamResult result = new StreamResult(sw);
        final DOMSource source = new DOMSource(doc);
        trans.transform(source, result);
        final StringBuffer xmlString = sw.getBuffer();
        return xmlString;
    }
}
