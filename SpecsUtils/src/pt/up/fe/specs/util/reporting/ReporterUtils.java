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

package pt.up.fe.specs.util.reporting;

import pt.up.fe.specs.util.Preconditions;

public class ReporterUtils {
    private ReporterUtils() {
    }

    public static String formatMessage(String messageType,
            String message) {

        Preconditions.checkArgument(messageType != null);
        Preconditions.checkArgument(message != null);

        return messageType + ": " + message;
    }

    public static String formatFileStackLine(String fileName, int lineNumber, String codeLine) {
        Preconditions.checkArgument(fileName != null);
        Preconditions.checkArgument(codeLine != null);

        return "At " + fileName + ":" + lineNumber + ":\n   > " + codeLine.trim();
    }

    public static String formatFunctionStackLine(String functionName, String fileName, int lineNumber,
            String codeLine) {
        Preconditions.checkArgument(fileName != null);
        Preconditions.checkArgument(codeLine != null);

        return "At function " + functionName + " (" + fileName + ":" + lineNumber + "):\n   > " + codeLine.trim();
    }

    public static String stackEnd() {
        return "\n";
    }

    public static String getErrorLine(String code, int line) {
        if (code == null) {
            return "Could not get code.";
        }
        return code.split("\n")[line - 1];
    }
}
