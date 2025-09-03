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

import java.util.Collection;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import pt.up.fe.specs.util.SpecsSystem;

public interface TagLogger<T> {

    Collection<T> getTags();

    String getBaseName();

    default void setLevelAll(Level level) {
        for (T tag : getTags()) {
            setLevel(tag, level);
        }

        // Root level
        setLevel(null, level);

    }

    default void setLevel(T tag, Level level) {
        getLogger(tag).setLevel(level);
    }

    default String getLoggerName(T tag) {
        String loggerName = getBaseName() + ".";

        loggerName += tag != null ? tag.toString() : "$root";

        return loggerName.toLowerCase();
    }

    default Logger getLogger(T tag) {
        return SpecsLoggers.getLogger(getLoggerName(tag));
    }

    default Logger getBaseLogger() {
        return SpecsLoggers.getLogger(getBaseName());
    }

    default void log(Level level, T tag, String message) {
        log(level, tag, message, LogSourceInfo.getLogSourceInfo(level));
    }

    default void log(Level level, T tag, String message, LogSourceInfo logSourceInfo) {
        log(level, tag, message, logSourceInfo, null);
    }

    default void log(Level level, T tag, String message, LogSourceInfo logSourceInfo, StackTraceElement[] stackTrace) {
        // Obtain logger
        Logger logger = SpecsLoggers.getLogger(getLoggerName(tag));
        logger.log(level, SpecsLogging.parseMessage(tag, message, logSourceInfo, stackTrace));
    }

    default void log(Level level, String message) {
        log(level, null, message);
    }

    default void info(T tag, String message) {
        log(Level.INFO, tag, message);
    }

    default void info(String message) {
        info(null, message);
    }

    default void debug(String message) {
        debug(() -> message);
    }

    default void debug(Supplier<String> message) {
        if (SpecsSystem.isDebug()) {
            log(Level.INFO, null, "[DEBUG] " + message.get());
        }
    }

    default void test(String message) {
        log(Level.INFO, null, "[TEST] " + message, LogSourceInfo.SOURCE);
    }

    default void deprecated(String message) {
        log(Level.INFO, null, "[DEPRECATED] " + message, LogSourceInfo.SOURCE);
    }

    default void warn(T tag, String message) {
        log(Level.WARNING, tag, message);
    }

    default void warn(String message) {
        warn(null, message);
    }

    /**
     * Adds a class to the ignore list when printing the stack trace, or the source
     * code location.
     * 
     * @param aClass
     */
    default TagLogger<T> addToIgnoreList(Class<?> aClass) {
        SpecsLogging.addClassToIgnore(aClass);
        return this;
    }
}
