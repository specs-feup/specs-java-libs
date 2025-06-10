/*
 * SpecsLogs.java
 *
 * Utility class for handling logging operations using the Java Logger API. Provides methods for configuring loggers, building handlers, redirecting output streams, and managing log levels. Used throughout the SPeCS framework for consistent logging.
 *
 * Copyright 2025 SPeCS Research Group.
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

package pt.up.fe.specs.util;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import pt.up.fe.specs.util.logging.ConsoleFormatter;
import pt.up.fe.specs.util.logging.CustomConsoleHandler;
import pt.up.fe.specs.util.logging.EnumLogger;
import pt.up.fe.specs.util.logging.LogLevel;
import pt.up.fe.specs.util.logging.LogSourceInfo;
import pt.up.fe.specs.util.logging.LoggingOutputStream;
import pt.up.fe.specs.util.logging.SimpleFileHandler;
import pt.up.fe.specs.util.logging.SpecsLoggerTag;
import pt.up.fe.specs.util.logging.SpecsLogging;

/**
 * Methods for the Java Logger API.
 * <p>
 * Provides static utility methods for configuring and using Java's logging framework, including custom handlers, log level management, and output redirection.
 * </p>
 *
 * @author Joao Bispo
 */
public class SpecsLogs {

    /**
     * Thread-local logger for SPeCS logging tags.
     */
    private final static ThreadLocal<EnumLogger<SpecsLoggerTag>> SPECS_LOGGER = ThreadLocal
            .withInitial(() -> EnumLogger.newInstance(SpecsLoggerTag.class).addToIgnoreList(SpecsLogs.class));

    /**
     * Logger name for System.out redirection.
     */
    private final static String SYSTEM_OUT_LOGGER = "System.out";
    /**
     * Logger name for System.err redirection.
     */
    private final static String SYSTEM_ERR_LOGGER = "System.err";

    /**
     * Tag for informational log messages.
     */
    public final static String INFO_TAG = "App-Info";

    /**
     * Helper method to get the root Logger.
     *
     * @return the root logger
     */
    public static Logger getRootLogger() {
        return Logger.getLogger("");
    }

    /**
     * Helper method to get the Logger with name defined by LOGGING_TAG.
     *
     * @return logger for the application
     */
    public static Logger getLogger() {
        return SPECS_LOGGER.get().getLogger(null);
    }

    /**
     * Retrieves the thread-local EnumLogger for SPeCS logging tags.
     *
     * @return the EnumLogger instance
     */
    public static EnumLogger<SpecsLoggerTag> getSpecsLogger() {
        return SPECS_LOGGER.get();
    }

    /**
     * Redirects the System.out stream to the logger with name defined by LOGGING_TAG.
     * <p>
     * Anything written to System.out is recorded as a log at info level.
     */
    public static void redirectSystemOut() {
        final Logger logger = Logger.getLogger(SpecsLogs.SYSTEM_OUT_LOGGER);
        final LoggingOutputStream los = new LoggingOutputStream(logger, Level.INFO);
        final PrintStream outPrint = new PrintStream(los, true);
        System.setOut(outPrint);
    }

    /**
     * Redirects the System.err stream to the logger with name defined by LOGGING_TAG.
     * <p>
     * Anything written to System.err is recorded as a log at warning level.
     */
    public static void redirectSystemErr() {
        final Logger logger = Logger.getLogger(SpecsLogs.SYSTEM_ERR_LOGGER);
        final LoggingOutputStream los = new LoggingOutputStream(logger, Level.WARNING);
        final PrintStream outPrint = new PrintStream(los, true);
        System.setErr(outPrint);
    }

    /**
     * Removes current handlers and adds the given Handlers to the root logger.
     *
     * @param handlers the Handlers we want to set as the root Handlers.
     */
    public static void setRootHandlers(Handler[] handlers) {
        final Logger logger = getRootLogger();
        final Handler[] handlersTemp = logger.getHandlers();
        for (final Handler handler : handlersTemp) {
            logger.removeHandler(handler);
        }
        for (final Handler handler : handlers) {
            logger.addHandler(handler);
        }
    }

    /**
     * Adds a single handler to the root logger.
     *
     * @param handler the handler to add
     */
    public static void addHandler(Handler handler) {
        addHandlers(Arrays.asList(handler));
    }

    /**
     * Removes a specific handler from the root logger.
     *
     * @param handler the handler to remove
     */
    public static void removeHandler(Handler handler) {
        final Logger logger = getRootLogger();
        final Handler[] handlersTemp = logger.getHandlers();
        final List<Handler> handlerList = SpecsFactory.newArrayList();
        for (Handler element : handlersTemp) {
            if (element == handler) {
                continue;
            }
            handlerList.add(element);
        }
        setRootHandlers(handlerList.toArray(new Handler[handlerList.size()]));
    }

    /**
     * Adds multiple handlers to the root logger.
     *
     * @param handlers the list of handlers to add
     */
    public static void addHandlers(List<Handler> handlers) {
        final Logger logger = getRootLogger();
        final Handler[] handlersTemp = logger.getHandlers();
        final Handler[] newHandlers = new Handler[handlersTemp.length + handlers.size()];
        for (int i = 0; i < handlersTemp.length; i++) {
            newHandlers[i] = handlersTemp[i];
        }
        for (int i = 0; i < handlers.size(); i++) {
            newHandlers[handlersTemp.length + i] = handlers.get(i);
        }
        setRootHandlers(newHandlers);
    }

    /**
     * Builds a Console Handler which uses ConsoleFormatter for formatting.
     *
     * @return a Console Handler formatted by ConsoleFormatter.
     */
    public static Handler buildStdOutHandler() {
        final StreamHandler cHandler = CustomConsoleHandler.newStdout();
        cHandler.setFormatter(new ConsoleFormatter());
        cHandler.setFilter(record -> {
            if (record.getLevel().intValue() > Level.INFO.intValue()) {
                return false;
            }
            return true;
        });
        cHandler.setLevel(Level.ALL);
        return cHandler;
    }

    /**
     * Builds a Console Handler which uses ConsoleFormatter for formatting.
     *
     * @return a Console Handler formatted by ConsoleFormatter.
     */
    public static Handler buildStdErrHandler() {
        final StreamHandler cHandler = CustomConsoleHandler.newStderr();
        cHandler.setFormatter(new ConsoleFormatter());
        cHandler.setFilter(record -> {
            if (record.getLevel().intValue() <= Level.INFO.intValue()) {
                return false;
            }
            return true;
        });
        cHandler.setLevel(Level.ALL);
        return cHandler;
    }

    /**
     * Builds a File Handler for error logs using ConsoleFormatter for formatting.
     *
     * @param logFilename the name of the log file
     * @return a File Handler formatted by ConsoleFormatter.
     */
    public static Handler buildErrorLogHandler(String logFilename) {
        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(logFilename, false);
        } catch (final SecurityException | IOException e) {
            e.printStackTrace();
        }
        if (fileHandler == null) {
            return null;
        }
        fileHandler.setFormatter(new ConsoleFormatter());
        fileHandler.setFilter(record -> {
            if (record.getLevel().intValue() <= Level.INFO.intValue()) {
                return false;
            }
            return true;
        });
        fileHandler.setLevel(Level.ALL);
        return fileHandler;
    }

    /**
     * Automatically setups the root logger for output to the console. Redirects System.out and System.err to the logger
     * as well.
     */
    public static void setupConsoleOnly() {
        final Handler[] handlers = new Handler[2];
        handlers[0] = buildStdOutHandler();
        handlers[1] = buildStdErrHandler();
        setRootHandlers(handlers);
        redirectSystemOut();
        redirectSystemErr();
    }

    /**
     * Parses a string representation of a logging level.
     *
     * @param levelString the string representation of the level
     * @return the parsed Level, or INFO if parsing fails
     */
    public static Level parseLevel(String levelString) {
        final Level defaultLevel = Level.INFO;
        Level level = defaultLevel;
        try {
            level = Level.parse(levelString);
        } catch (final IllegalArgumentException ex) {
            Logger.getLogger(SpecsLogs.class.getName()).info(
                    "Could not parse logger level '" + levelString + "'. " + "Setting level to '" + defaultLevel + "'");
        }
        return level;
    }

    /**
     * Sets the level of the root Logger.
     *
     * @param level the logging level to set
     */
    public static void setLevel(Level level) {
        SpecsLogs.getRootLogger().setLevel(level);
    }

    /**
     * Writes a warning-level message to the logger.
     *
     * @param msg the message to log
     */
    public static void warn(String msg) {
        SPECS_LOGGER.get().warn(msg);
    }

    /**
     * @deprecated Use {@link #warn(String)} instead.
     * Writes a warning-level message to the logger.
     *
     * @param msg the message to log
     */
    @Deprecated
    public static void msgWarn(String msg) {
        warn(msg);
    }

    /**
     * @deprecated Use {@link #warn(String, Throwable)} instead.
     * Writes a warning-level message to the logger, including a Throwable cause.
     *
     * @param msg      the message to log
     * @param ourCause the Throwable cause
     */
    @Deprecated
    public static void msgWarn(String msg, Throwable ourCause) {
        warn(msg, ourCause);
    }

    /**
     * Writes a warning-level message to the logger, including a Throwable cause.
     *
     * @param msg      the message to log
     * @param ourCause the Throwable cause
     */
    public static void warn(String msg, Throwable ourCause) {
        while (ourCause.getCause() != null) {
            ourCause = ourCause.getCause();
        }
        List<StackTraceElement> catchLocationTrace = SpecsLogging
                .getLogCallLocation(Thread.currentThread().getStackTrace());
        String catchLocation = !catchLocationTrace.isEmpty() ? SpecsLogging.getSourceCode(catchLocationTrace.get(0))
                : "<Could not get catch location trace>";
        String causeString = ourCause.getMessage();
        if (causeString == null) {
            causeString = ourCause.toString();
        }
        causeString = "[" + ourCause.getClass().getSimpleName() + "] " + causeString;
        msg = msg + catchLocation + "\n\nException message: " + causeString;
        SPECS_LOGGER.get().log(Level.WARNING, null, msg, LogSourceInfo.getLogSourceInfo(Level.WARNING),
                ourCause.getStackTrace());
    }

    /**
     * Writes an info-level message to the logger.
     *
     * @param msg the message to log
     */
    public static void info(String msg) {
        SPECS_LOGGER.get().info(msg);
    }

    /**
     * @deprecated Use {@link #info(String)} instead.
     * Writes an info-level message to the logger.
     *
     * @param msg the message to log
     */
    @Deprecated
    public static void msgInfo(String msg) {
        info(msg);
    }

    /**
     * Writes a library-level message to the logger.
     *
     * @param msg the message to log
     */
    public static void msgLib(String msg) {
        SPECS_LOGGER.get().log(LogLevel.LIB, msg);
    }

    /**
     * Writes a severe-level message to the logger.
     *
     * @param msg the message to log
     */
    public static void msgSevere(String msg) {
        SPECS_LOGGER.get().log(Level.SEVERE, msg);
    }

    /**
     * Enables/disables printing of the stack trace for Warning level.
     *
     * @param bool true to enable stack trace printing, false to disable
     */
    public static void setPrintStackTrace(boolean bool) {
        LogSourceInfo sourceInfo = bool ? LogSourceInfo.STACK_TRACE : LogSourceInfo.NONE;
        LogSourceInfo.setLogSourceInfo(Level.WARNING, sourceInfo);
    }

    /**
     * Checks if the given logger name corresponds to System.out or System.err.
     *
     * @param loggerName the name of the logger
     * @return true if the logger is System.out or System.err, false otherwise
     */
    public static boolean isSystemPrint(String loggerName) {
        if (SpecsLogs.SYSTEM_OUT_LOGGER.equals(loggerName)) {
            return true;
        }
        if (SpecsLogs.SYSTEM_ERR_LOGGER.equals(loggerName)) {
            return true;
        }
        return false;
    }

    /**
     * Adds a log stream to the root logger.
     *
     * @param stream the PrintStream to add
     */
    public static void addLog(PrintStream stream) {
        final SimpleFileHandler handler = new SimpleFileHandler(stream);
        handler.setFormatter(new ConsoleFormatter());
        SpecsLogs.addHandler(handler);
    }

    /**
     * Logs a debug-level message if debugging is enabled.
     *
     * @param string the message supplier
     */
    public static void debug(Supplier<String> string) {
        if (SpecsSystem.isDebug()) {
            SPECS_LOGGER.get().debug(string.get());
        }
    }

    /**
     * Logs a debug-level message if debugging is enabled.
     *
     * @param string the message to log
     */
    public static void debug(String string) {
        debug(() -> string);
    }

    /**
     * Logs a warning for untested cases.
     *
     * @param untestedAction the description of the untested action
     */
    public static void untested(String untestedAction) {
        SpecsLogs.warn(
                "Untested:" + untestedAction + ". Please contact the developers in order to add this case as a test.");
    }

}
