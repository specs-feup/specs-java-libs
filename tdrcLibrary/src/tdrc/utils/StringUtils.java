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
 * specific language governing permissions and limitations under the License. under the License.
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

public class StringUtils {

    static final String keywordPrefix = "_";
    static final String keywords[] = { "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
	    "class", "const", "continue", "default", "do", "double", "else", "extends", "false", "final", "finally",
	    "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long", "native",
	    "new", "null", "package", "private", "protected", "public", "return", "short", "static", "strictfp",
	    "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try", "void",
	    "volatile", "while" };

    /**
     * Verifies if a given String is a reserved keyword of Java
     * 
     * @param keyword
     * @return
     */
    public static boolean isJavaKeyword(String keyword) {
	return (Arrays.binarySearch(StringUtils.keywords, keyword) >= 0);
    }

    /**
     * Returns a sanitized string for the given name, i.e., insures that the name is not a reserved keyword
     * 
     * @param name
     * @return
     */
    public static String getSanitizedName(String name) {
	if (isJavaKeyword(name)) {
	    name = StringUtils.keywordPrefix + name;
	}
	return name;
    }

    public static String firstCharToUpper(String string) {
	return charToUpperOrLower(string, 0, true);
    }

    public static String firstCharToLower(String string) {
	return charToUpperOrLower(string, 0, false);
    }

    /**
     * Puts the given char to upper (true) or lower (false) case
     * 
     * @param string
     * @param pos
     * @param upper
     *            if set to true, the char is set to upper case; if set to false, the char is set to lower case
     * @return
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

    public static String joinStrings(Collection<String> collection, String separator) {
	return String.join(separator, collection);
    }

    /**
     * Joins the elements of a collection with a given separator. This method requires a mapping function to convert the
     * elements into strings
     * 
     * @param collection
     * @param mapper
     * @param separator
     * @return
     */
    public static <T> String join(Collection<T> collection, Function<T, String> mapper, String separator) {

	return collection.stream().map(mapper).collect(Collectors.joining(separator));

    }

    /**
     * 
     * Joins the elements of a collection with a given separator. This method uses the toString method for each element.
     * 
     * @param collection
     * @param mapper
     * @param separator
     * @return
     * @return
     */
    public static <T> String join(Collection<T> collection, String separator) {

	final String joinedArguments = collection.stream().map(Object::toString).collect(Collectors.joining(separator));
	return joinedArguments;
    }

    /**
     * Compares the package of two classes
     * 
     * @param firstClassName
     * @param secondClassName
     * @return true if in the same package, false otherwise
     */
    public static boolean inSamePackage(String firstClassName, String secondClassName) {
	final String firstPackage = getPackage(firstClassName);
	final String secondPackage = getPackage(secondClassName);
	return firstPackage.equals(secondPackage);
    }

    /**
     * Get the package from a given class name
     * 
     * @param className
     *            the name of the class
     * @return the package if is present in the class name (name contains '.'), empty string otherwise
     */
    public static String getPackage(String className) {
	final int lastDot = className.lastIndexOf('.');
	if (lastDot > -1) {
	    return className.substring(0, lastDot);
	}
	return "";
    }

    /**
     * Repeat a given string 'repeat' times. if the string to repeat.
     * <p>
     * Conditions:<br>
     * - toRepeat == <b>null</b> || repeat < 0 -> <b>null</b><br>
     * - repeat == 0 -> ""<br>
     * - toRepeat.isEmpty || repeat == 1 -> toRepeat<br>
     * - else -> toRepeat * repeat
     * 
     * @param toRepeat
     * @param repeat
     * @return
     */
    public static String repeat(String toRepeat, int repeat) {
	if (toRepeat == null || repeat < 0) {
	    return null;
	}
	if (repeat == 0) {
	    return "";
	}
	if (toRepeat.isEmpty() || repeat == 1) {
	    return toRepeat;
	}
	return new String(new char[repeat]).replace("\0", toRepeat);
    }

    public static StringBuffer xmlToStringBuffer(Document doc, int identAmount)
	    throws TransformerFactoryConfigurationError, TransformerConfigurationException, TransformerException {
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
