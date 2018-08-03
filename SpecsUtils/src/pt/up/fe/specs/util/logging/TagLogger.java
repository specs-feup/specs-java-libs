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
import java.util.logging.Level;

public interface TagLogger<T> {

    Collection<T> getTags();

    Class<?> getBaseClass();

    default void setLevelAll(Level level) {
        for (T tag : getTags()) {
            setLevel(tag, level);
        }

    }

    default void setLevel(T tag, Level level) {
        getLogger(tag).getJavaLogger().setLevel(level);
    }

    default String getLoggerName(T tag) {
        String loggerName = getBaseClass().getName() + ".";

        loggerName += tag != null ? tag.toString() : "$root";

        return loggerName.toLowerCase();
    }

    default LoggerWrapper getLogger(T tag) {
        return SpecsLoggers.getLogger(getLoggerName(tag));
    }

    default LoggerWrapper getBaseLogger() {
        return SpecsLoggers.getLogger(getBaseClass().getName());
    }

    default void info(T tag, String message) {
        // LogsHelper.logMessage(getLoggerName(tag), tag, message, (logger, msg) -> logger.info(msg));

        String prefix = LogsHelper.getPrefix(tag);

        LoggerWrapper logger = SpecsLoggers.getLogger(getLoggerName(tag));
        // System.out.println("LEVEL:" + logger.getJavaLogger().getLevel());
        logger.info(prefix + message);
        // System.out.println("ADASD");
        // logging.accept(logger, prefix + message);
    }

    default void info(String message) {
        info(null, message);
    }

    // static <T extends Enum<T>> TagLogger<T> newInstance(Class<T> enumClass) {
    // return () -> enumClass;
    // }

    // default void warn(T tag, String message) {
    // LogsHelper.logMessage(getClass().getName(), tag, message, (logger, msg) -> logger.warn(msg));
    // }
    //
    // default void warn(String message) {
    // warn(null, message);
    // }

}
