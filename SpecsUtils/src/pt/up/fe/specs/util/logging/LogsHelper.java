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

import java.util.function.BiConsumer;

class LogsHelper {

    // // private final T defaultValue;
    // private final String baseName;
    //
    // // protected ALogs(T defaultValue) {
    // protected ALogs() {
    // // this.defaultValue = defaultValue;
    // this.baseName = getClass().getName();
    // }

    static String getLoggerName(String baseName, Enum<?> tag) {
        String loggerName = baseName + ".";

        loggerName += tag != null ? tag.name() : "$root";

        return loggerName;
    }

    static void logMessage(String loggerName, Enum<?> tag, String message, BiConsumer<LoggerWrapper, String> logging) {
        String prefix = getPrefix(tag);

        // String loggerName = getLoggerName(baseName, tag);
        // System.out.println("LOGGER NAME:" + loggerName);
        LoggerWrapper logger = SpecsLoggers.getLogger(loggerName);
        logging.accept(logger, prefix + message);
    }

    static String getPrefix(Object tag) {
        if (tag == null) {
            return "";
        }

        return "[" + tag.toString() + "] ";
    }

}
