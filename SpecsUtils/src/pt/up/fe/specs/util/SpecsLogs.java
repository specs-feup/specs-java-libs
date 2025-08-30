/*
 * Copyright 2009 SPeCS Research Group.
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
import java.util.ArrayList;
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
 *
 * @author Joao Bispo
 */
public class SpecsLogs {

    private final static ThreadLocal<EnumLogger<SpecsLoggerTag>> SPECS_LOGGER = ThreadLocal
            .withInitial(() -> EnumLogger.newInstance(SpecsLoggerTag.class).addToIgnoreList(SpecsLogs.class));

    private final static String SYSTEM_OUT_LOGGER = "System.out";
    private final static String SYSTEM_ERR_LOGGER = "System.err";

    public final static String INFO_TAG = "App-Info";

    /**
     * Helper method to get the root Logger.
     *
     * @return
     */
    public static Logger getRootLogger() {
        return Logger.getLogger("");
    }

    /**
     * Helper method to get the Logger with name defined by LOGGING_TAG.
     *
     * @return logger for
     */
    public static Logger getLogger() {
        return SPECS_LOGGER.get().getLogger(null);
    }

    public static EnumLogger<SpecsLoggerTag> getSpecsLogger() {
        return SPECS_LOGGER.get();
    }

    /**
     * Redirects the System.out stream to the logger with name defined by
     * LOGGING_TAG.
     *
     * <p>
     * Anything written to System.out is recorded as a log at info level.
     */
    public static void redirectSystemOut() {
        // Get Logger
        final Logger logger = Logger.getLogger(SpecsLogs.SYSTEM_OUT_LOGGER);

        // Build Printstream for System.out
        final LoggingOutputStream los = new LoggingOutputStream(logger, Level.INFO);

        // Creating stream with System.out, it is supposed to remain open
        final PrintStream outPrint = new PrintStream(los, true);

        // Set System.out
        System.setOut(outPrint);
    }

    /**
     * Redirects the System.err stream to the logger with name defined by
     * LOGGING_TAG.
     *
     * <p>
     * Anything written to System.err is recorded as a log at warning level.
     */
    public static void redirectSystemErr() {
        // Get Logger
        final Logger logger = Logger.getLogger(SpecsLogs.SYSTEM_ERR_LOGGER);

        // Build Printstream for System.out
        final LoggingOutputStream los = new LoggingOutputStream(logger, Level.WARNING);

        // Creating stream with System.err, it is supposed to remain open
        final PrintStream outPrint = new PrintStream(los, true);

        // Set System.out
        System.setErr(outPrint);
    }

    /**
     * Removes current handlers and adds the given Handlers to the root logger.
     *
     * @param handlers
     *                 the Handlers we want to set as the root Handlers.
     */
    public static void setRootHandlers(Handler[] handlers) {
        final Logger logger = getRootLogger();

        // Remove all previous handlers
        final Handler[] handlersTemp = logger.getHandlers();
        for (final Handler handler : handlersTemp) {
            logger.removeHandler(handler);
        }

        // Add Handlers
        for (final Handler handler : handlers) {
            logger.addHandler(handler);
        }
    }

    /**
     * Helper method.
     *
     * @param handler
     */
    public static void addHandler(Handler handler) {
        addHandlers(Arrays.asList(handler));
    }

    public static void removeHandler(Handler handler) {
        // Get all handlers
        final Logger logger = getRootLogger();
        final Handler[] handlersTemp = logger.getHandlers();

        // Add handlers to a list, except for the given handler
        final List<Handler> handlerList = new ArrayList<>();
        for (Handler element : handlersTemp) {

            if (element == handler) {
                continue;
            }

            handlerList.add(element);
        }

        // Set new handler list
        setRootHandlers(handlerList.toArray(new Handler[handlerList.size()]));
    }

    /**
     * Removes current handlers and adds the given Handlers to the root logger.
     *
     * @param handlers
     *                 the Handlers we want to set as the root Handlers.
     */
    public static void addHandlers(List<Handler> handlers) {
        // Get all handlers
        final Logger logger = getRootLogger();
        final Handler[] handlersTemp = logger.getHandlers();

        final Handler[] newHandlers = new Handler[handlersTemp.length + handlers.size()];

        // Add previous handlres
        for (int i = 0; i < handlersTemp.length; i++) {
            newHandlers[i] = handlersTemp[i];
        }

        // Add new handlers
        for (int i = 0; i < handlers.size(); i++) {
            newHandlers[handlersTemp.length + i] = handlers.get(i);
        }

        // Set handlers
        setRootHandlers(newHandlers);
    }

    /**
     * builds a Console Handler which uses as formatter, ConsoleFormatter.
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
     * builds a Console Handler which uses as formatter, ConsoleFormatter.
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
     * builds a Console Handler which uses as formatter, ConsoleFormatter.
     *
     * @return a Console Handler formatted by ConsoleFormatter.
     */
    public static Handler buildErrorLogHandler(String logFilename) {

        FileHandler fileHandler = null;
        try {
            fileHandler = new FileHandler(logFilename, false);
        } catch (final SecurityException e) {
            e.printStackTrace();
        } catch (final IOException e) {
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
     * Automatically setups the root logger for output to the console. Redirects
     * System.out and System.err to the logger
     * as well.
     */
    public static void setupConsoleOnly() {
        // Build ConsoleHandler
        final Handler[] handlers = new Handler[2];
        handlers[0] = buildStdOutHandler();
        handlers[1] = buildStdErrHandler();

        setRootHandlers(handlers);

        // Redirect System.out
        redirectSystemOut();

        // Redirect System.err
        redirectSystemErr();
    }

    public static Level parseLevel(String levelString) {
        final Level defaultLevel = Level.INFO;
        Level level = defaultLevel;
        // Parse Logger Level
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
     * @param level
     */
    public static void setLevel(Level level) {
        SpecsLogs.getRootLogger().setLevel(level);
    }

    /**
     * Writes a message to the logger with name defined by LOGGING_TAG.
     *
     * <p>
     * Messages written with this method are recorded as a log at warning level. Use
     * this level to show a message for cases that are supposed to never happen if
     * the code is well used.
     *
     * @param msg
     */
    public static void warn(String msg) {
        SPECS_LOGGER.get().warn(msg);
    }

    /**
     * @deprecated use warn() instead
     * @param msg
     */
    @Deprecated
    public static void msgWarn(String msg) {
        warn(msg);
    }

    /**
     * @deprecated use warn() instead
     * @param msg
     * @param ourCause
     */
    @Deprecated
    public static void msgWarn(String msg, Throwable ourCause) {
        warn(msg, ourCause);
    }

    public static void warn(String msg, Throwable ourCause) {

        // Get the root cause
        while (ourCause.getCause() != null) {
            ourCause = ourCause.getCause();
        }

        // Save current place where message is being issued
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
     * Info-level message.
     *
     * <p>
     * Use this level to show messages to the user of a program.
     *
     * @param msg
     */
    public static void msgInfo(String msg) {
        info(msg);
        // msgInfo(Logger.getLogger(SpecsLogs.INFO_TAG), msg);
    }

    public static void info(String msg) {
        SPECS_LOGGER.get().info(msg);
    }

    /**
     * Lib-level message.
     *
     * <p>
     * This is a logging level between INFO and CONFIG, to be used by libraries to
     * log execution information.
     *
     * @param msg
     */
    public static void msgLib(String msg) {
        SPECS_LOGGER.get().log(LogLevel.LIB, msg);
    }

    /**
     * Writes a message to the logger with name defined by LOGGING_TAG.
     *
     * <p>
     * Messages written with this method are recorded as a log at severe level.
     *
     * @param msg
     */
    public static void msgSevere(String msg) {
        SPECS_LOGGER.get().log(Level.SEVERE, msg);
    }

    /**
     * Enables/disables printing of the stack trace for Warning level.
     *
     * <p>
     * This method is for compatibility with previous code. Please use
     * LogSourceInfo.setLogSourceInfo instead.
     *
     * @param bool
     */
    public static void setPrintStackTrace(boolean bool) {
        LogSourceInfo sourceInfo = bool ? LogSourceInfo.STACK_TRACE : LogSourceInfo.NONE;
        LogSourceInfo.setLogSourceInfo(Level.WARNING, sourceInfo);
    }

    public static boolean isSystemPrint(String loggerName) {
        if (SpecsLogs.SYSTEM_OUT_LOGGER.equals(loggerName)) {
            return true;
        }

        if (SpecsLogs.SYSTEM_ERR_LOGGER.equals(loggerName)) {
            return true;
        }

        return false;
    }

    public static void addLog(PrintStream stream) {
        // Create file handler
        final SimpleFileHandler handler = new SimpleFileHandler(stream);

        // Set formatter
        handler.setFormatter(new ConsoleFormatter());

        // Add handler
        SpecsLogs.addHandler(handler);
    }

    public static void debug(Supplier<String> string) {
        // To avoid resolving the string unnecessarily
        if (SpecsSystem.isDebug()) {
            SPECS_LOGGER.get().debug(string.get());
        }
    }

    /**
     * If this is not a pure string literal, should always prefer overload that
     * receives a lambda, to avoid doing the
     * string computation when debug is not enabled.
     *
     * @param string
     */
    public static void debug(String string) {
        debug(() -> string);
    }

    /**
     * When a certain case has not been yet tested and it can appear on the field.
     *
     * @param untestedAction
     */
    public static void untested(String untestedAction) {
        SpecsLogs.warn(
                "Untested:" + untestedAction + ". Please contact the developers in order to add this case as a test.");
    }
}
