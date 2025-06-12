/*
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

package pt.up.fe.specs.util.reporting;

import pt.up.fe.specs.util.Preconditions;

/**
 * Utility methods for working with Reporter interfaces and reporting utilities.
 * <p>
 * Provides static helper methods for managing and formatting reports.
 * </p>
 */
public class ReporterUtils {
    private ReporterUtils() {
    }

    /**
     * Formats a message with a given type and content.
     *
     * @param messageType the type of the message (e.g., "Error", "Warning")
     * @param message the content of the message
     * @return a formatted message string
     */
    public static String formatMessage(String messageType,
            String message) {

        Preconditions.checkArgument(messageType != null);
        Preconditions.checkArgument(message != null);

        return messageType + ": " + message;
    }

    /**
     * Formats a stack line for a file, including the file name, line number, and code line.
     *
     * @param fileName the name of the file
     * @param lineNumber the line number in the file
     * @param codeLine the code line at the specified line number
     * @return a formatted stack line string
     */
    public static String formatFileStackLine(String fileName, int lineNumber, String codeLine) {
        Preconditions.checkArgument(fileName != null);
        Preconditions.checkArgument(codeLine != null);

        return "At " + fileName + ":" + lineNumber + ":\n   > " + codeLine.trim();
    }

    /**
     * Formats a stack line for a function, including the function name, file name, line number, and code line.
     *
     * @param functionName the name of the function
     * @param fileName the name of the file
     * @param lineNumber the line number in the file
     * @param codeLine the code line at the specified line number
     * @return a formatted stack line string
     */
    public static String formatFunctionStackLine(String functionName, String fileName, int lineNumber,
            String codeLine) {
        Preconditions.checkArgument(fileName != null);
        Preconditions.checkArgument(codeLine != null);

        return "At function " + functionName + " (" + fileName + ":" + lineNumber + "):\n   > " + codeLine.trim();
    }

    /**
     * Returns a string representing the end of a stack trace.
     *
     * @return a string representing the end of a stack trace
     */
    public static String stackEnd() {
        return "\n";
    }

    /**
     * Retrieves a specific line of code from a given code string.
     *
     * @param code the code string
     * @param line the line number to retrieve
     * @return the code line at the specified line number, or a message if the code is null
     */
    public static String getErrorLine(String code, int line) {
        if (code == null) {
            return "Could not get code.";
        }
        return code.split("\n")[line - 1];
    }
}
