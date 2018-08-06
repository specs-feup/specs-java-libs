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
import java.util.logging.Logger;

public interface TagLogger<T> {

    Collection<T> getTags();

    String getBaseName();

    default void setLevelAll(Level level) {
        for (T tag : getTags()) {
            setLevel(tag, level);
        }

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
        /*        
            // Obtain stack trace
        
            // Log using stack trace
        
        } else {
            logger.log(level, SpecsLogging.parseMessage(tag, message));
        }
        */
    }

    default void log(Level level, String message) {
        log(level, null, message);
    }

    default void info(T tag, String message) {
        log(Level.INFO, tag, message);
        /*
        // LogsHelper.logMessage(getLoggerName(tag), tag, message, (logger, msg) -> logger.info(msg));
        
        // String prefix = SpecsLogging.getPrefix(tag);
        
        Logger logger = SpecsLoggers.getLogger(getLoggerName(tag));
        // System.out.println("LEVEL:" + logger.getJavaLogger().getLevel());
        
        logger.info(SpecsLogging.parseMessage(tag, message));
        // System.out.println("ADASD");
        // logging.accept(logger, prefix + message);
        */

    }

    default void info(String message) {
        info(null, message);
    }

    default void debug(String message) {
        log(Level.INFO, null, "[DEBUG] " + message, LogSourceInfo.SOURCE);
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
     * Adds a class to the ignore list when printing the stack trace, or the source code location.
     * 
     * @param aClass
     */
    default TagLogger<T> addToIgnoreList(Class<?> aClass) {
        SpecsLogging.addClassToIgnore(aClass);
        return this;
    }

    /*
    default void warn(T tag, String msg, List<StackTraceElement> elements, int startIndex, boolean appendCallingClass) {
    
        msg = "[WARNING]: " + msg;
        msg = parseMessage(msg);
        msg = buildErrorMessage(msg, elements.subList(startIndex, elements.size()));
    
        if (appendCallingClass) {
            logger = logger == null ? getLoggerDebug() : logger;
            logger.warning(msg);
            // getLoggerDebug().warning(msg);
        } else {
            logger = logger == null ? getLogger() : logger;
            logger.warning(msg);
            // getLogger().warning(msg);
        }
    }
    */

    /**
     * Writes a message to the logger with name defined by LOGGING_TAG.
     *
     * <p>
     * Messages written with this method are recorded as a log at warning level. Use this level to show a message for
     * cases that are supposed to never happen if the code is well used.
     *
     * @param msg
     */
    /*
    public static void msgWarn(String msg) {
    
        final List<StackTraceElement> elements = Arrays.asList(Thread.currentThread().getStackTrace());
        final int startIndex = 2;
    
        msgWarn(msg, elements, startIndex, true, null);
    }
    
    public static void msgWarn(Logger logger, String msg) {
    
        final List<StackTraceElement> elements = Arrays.asList(Thread.currentThread().getStackTrace());
        final int startIndex = 2;
    
        msgWarn(msg, elements, startIndex, true, logger);
    }
    
    private static void msgWarn(String msg, List<StackTraceElement> elements, int startIndex,
            boolean appendCallingClass, Logger logger) {
    
        msg = "[WARNING]: " + msg;
        msg = parseMessage(msg);
        msg = buildErrorMessage(msg, elements.subList(startIndex, elements.size()));
    
        if (appendCallingClass) {
            logger = logger == null ? getLoggerDebug() : logger;
            logger.warning(msg);
            // getLoggerDebug().warning(msg);
        } else {
            logger = logger == null ? getLogger() : logger;
            logger.warning(msg);
            // getLogger().warning(msg);
        }
    }
    
    public static void msgWarn(String msg, Throwable ourCause) {
    
        // Get the root cause
        while (ourCause.getCause() != null) {
            ourCause = ourCause.getCause();
        }
    
        // Save current place where message is being issued
        final List<StackTraceElement> currentElements = Arrays.asList(Thread.currentThread().getStackTrace());
        final StackTraceElement currentElement = currentElements.get(2);
        final String msgSource = "\n\n[Catch]:\n" + currentElement;
    
        String causeString = ourCause.getMessage();
        if (causeString == null) {
            causeString = ourCause.toString();
        }
    
        final String causeMsg = causeString + msgSource;
    
        // msg = msg + "\nCause: [" + ourCause.getClass().getSimpleName() + "] " + ourCause.getMessage() + msgSource;
        msg = msg + "\nCause: " + causeMsg;
    
        final List<StackTraceElement> elements = Arrays.asList(ourCause.getStackTrace());
        final int startIndex = 0;
    
        msgWarn(msg, elements, startIndex, false, null);
    }
    
    public static void msgWarn(Throwable cause) {
    
        final List<StackTraceElement> elements = Arrays.asList(cause.getStackTrace());
        final int startIndex = 0;
    
        final String msg = cause.getClass().getName() + ": " + cause.getMessage();
    
        msgWarn(msg, elements, startIndex, false, null);
    
    }
    
    */

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
