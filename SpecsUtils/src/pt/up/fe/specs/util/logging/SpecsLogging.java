/**
 * Copyright 2018 SPeCS.
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

package pt.up.fe.specs.util.logging;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Utility methods used internally by the logging package.
 * 
 * @author JoaoBispo
 *
 */
public class SpecsLogging {

    private final static String NEWLINE = System.getProperty("line.separator");

    private final static Set<String> CLASS_NAME_IGNORE = new HashSet<>();
    static {
        addClassToIgnore(Thread.class); // To avoid Thread.getStackTrace
        addClassToIgnore(SpecsLogging.class);
        addClassToIgnore(TagLogger.class);
    }

    // private final static Set<String> METHOD_NAME_IGNORE = new HashSet<>(
    // Arrays.asList("log", "info", "warn", "debug", "deprecated", "warning"));

    /**
     * Adds a class to the ignore list for determining what should appear when a stack trace or source code location is
     * printed.
     * 
     * @param aClass
     */
    public static void addClassToIgnore(Class<?> aClass) {
        CLASS_NAME_IGNORE.add(aClass.getName());
    }

    public static String getPrefix(Object tag) {
        if (tag == null) {
            return "";
        }

        return "[" + tag.toString() + "] ";
    }

    public static String getLogSuffix(LogSourceInfo logSuffix, StackTraceElement[] stackTrace) {
        switch (logSuffix) {
        case NONE:
            return "";
        case SOURCE:
            return getSourceCodeLocation(stackTrace);
        case STACK_TRACE:
            return getStackTrace(stackTrace);
        default:
            throw new NotImplementedException(logSuffix);
        }
    }

    private static String getSourceCodeLocation(StackTraceElement[] stackTrace) {

        StackTraceElement element = getLogCallLocation(stackTrace).get(0);

        if (element == null) {
            return "";
        }

        return getSourceCode(element);
    }

    public static String getStackTrace(StackTraceElement[] stackTrace) {

        Collection<StackTraceElement> elements = getLogCallLocation(stackTrace);

        final StringBuilder builder = new StringBuilder();

        builder.append("\n\nStack Trace:");
        builder.append("\n--------------");

        for (final StackTraceElement element : elements) {
            builder.append("\n");
            builder.append(element);
        }

        builder.append("\n--------------");
        builder.append("\n");

        return builder.toString();
    }

    public static List<StackTraceElement> getLogCallLocation(StackTraceElement[] stackTrace) {
        if (stackTrace == null) {
            stackTrace = Thread.currentThread().getStackTrace();
        }

        // Discover from which index to cut the trace
        // Discard first index, will be java.lang.Thread.getStackTrace
        int index = 0;
        while (index < stackTrace.length) {
            if (!ignoreStackTraceElement(stackTrace[index])) {
                return Arrays.asList(stackTrace).subList(index, stackTrace.length);
            }

            index++;
        }

        return Collections.emptyList();

    }

    private static boolean ignoreStackTraceElement(StackTraceElement stackTraceElement) {
        // Check if in class name ignore list
        if (CLASS_NAME_IGNORE.contains(stackTraceElement.getClassName())) {
            return true;
        }

        // Check if in method name ignore list
        // if (METHOD_NAME_IGNORE.contains(stackTraceElement.getMethodName())) {
        // return true;
        // }

        // System.out.println("File name:" + stackTraceElement.getFileName());
        // System.out.println("Class name:" + stackTraceElement.getClassName());
        // System.out.println("Method name:" + stackTraceElement.getMethodName());

        return false;
    }

    public static String getSourceCode(StackTraceElement s) {

        StringBuilder builder = new StringBuilder();
        builder.append(" -> ");
        // builder.append("[ ");
        builder.append(s.getClassName());
        builder.append(".");
        builder.append(s.getMethodName());
        builder.append("(");
        builder.append(s.getFileName());
        builder.append(":");
        builder.append(s.getLineNumber());
        builder.append(")");
        // builder.append(" ]");

        return builder.toString();
    }

    /**
     * Prepares the message for logging.
     * 
     * <p>
     * - Adds a prefix according to the tag; <br>
     * - Adds a newline to the end of the message; <br>
     *
     * @param msg
     * @return
     */
    public static String parseMessage(Object tag, String msg, LogSourceInfo logSuffix, StackTraceElement[] stackTrace) {

        // Prefix
        String parsedMessage = getPrefix(tag);

        // Message
        parsedMessage += msg;

        parsedMessage += getLogSuffix(logSuffix, stackTrace);

        // New line
        if (msg != null && !msg.isEmpty()) {
            parsedMessage += NEWLINE;
        }

        return parsedMessage;
    }

    // public static String parseMessage(Object tag, String msg) {
    // return parseMessage(tag, msg, Collections.emptyList());
    // }
}
