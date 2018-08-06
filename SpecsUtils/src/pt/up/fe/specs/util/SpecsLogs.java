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
import java.util.Arrays;
import java.util.Collection;
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
import pt.up.fe.specs.util.logging.LoggingOutputStream;
import pt.up.fe.specs.util.logging.SimpleFileHandler;
import pt.up.fe.specs.util.logging.SpecsLoggerTag;

/**
 * Methods for the Java Logger API.
 *
 * @author Joao Bispo
 */
public class SpecsLogs {

    private final static EnumLogger<SpecsLoggerTag> SPECS_LOGGER = EnumLogger.newInstance(SpecsLoggerTag.class);

    private final static String NEWLINE = System.getProperty("line.separator");

    private final static String SYSTEM_OUT_LOGGER = "System.out";
    private final static String SYSTEM_ERR_LOGGER = "System.err";

    private final static String SEVERE_TAG = "App-Severe";
    private final static String WARNING_TAG = "App-Warn";
    private final static String INFO_TAG = "App-Info";
    private final static String LIB_TAG = "App-Lib";

    private final static String LOGGING_TAG = "CurrentApp";
    // public final static String LOGGING_ANDROID_TAG = "currentApp";
    private final static String LIB_LOGGING_TAG = "[LIB]";
    // Preserving a reference to original stdout/stderr streams,
    // in case they change.
    private final static PrintStream stdout = System.out;
    private final static PrintStream stderr = System.err;

    private static boolean printStackTrace = true;

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
        return Logger.getLogger(SpecsLogs.LOGGING_TAG);
    }

    /**
     * Helper method to automatically get the Logger correspondent to the class which calls this method.
     *
     * <p>
     * Thes method uses the "heavy" StackTrace to determine what object called it. This should only be used for logging
     * "warnings" which are not called often, or in situation such as constructors, were we do not want to leak the
     * object reference before it exists.
     *
     * @param object
     * @return logger specific to the given object
     */

    public static Logger getLoggerDebug() {
        // StackTraceElement stackElement = ProcessUtils.getCallerMethod();
        // If called directly, use index 4
        return getLoggerDebug(5);
        // StackTraceElement stackElement = ProcessUtils.getCallerMethod(4);
        // return Logger.getLogger(stackElement.getClassName());
    }

    /**
     * Helper method to automatically get the Logger correspondent to the class which calls this method.
     *
     * <p>
     * Thes method uses the "heavy" StackTrace to determine what object called it. This should only be used for logging
     * "warnings" which are not called often, or in situation such as constructors, were we do not want to leak the
     * object reference before it exists.
     *
     * @param callerMethodIndex
     *            the index indicating the depth of method calling. This method introduces 3 calls (index 0-2), index 3
     *            is this method, index 4 is the caller index
     * @return logger specific to the given object
     */

    public static Logger getLoggerDebug(int callerMethodIndex) {
        final StackTraceElement stackElement = SpecsSystem.getCallerMethod(callerMethodIndex);
        return Logger.getLogger(stackElement.toString());
    }

    /**
     * Redirects the System.out stream to the logger with name defined by LOGGING_TAG.
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
     * Redirects the System.err stream to the logger with name defined by LOGGING_TAG.
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
     *            the Handlers we want to set as the root Handlers.
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
        final List<Handler> handlerList = SpecsFactory.newArrayList();
        for (Handler element : handlersTemp) {

            if (element == handler) {
                continue;
            }

            handlerList.add(element);
        }

        // Set new handler list
        setRootHandlers(handlerList.toArray(new Handler[handlerList.size()]));
    }

    public static void setHandlers(List<Handler> handlers) {

    }

    /**
     * Removes current handlers and adds the given Handlers to the root logger.
     *
     * @param handlers
     *            the Handlers we want to set as the root Handlers.
     */
    // public static void addHandler(Handler handler) {
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
    /*
    public static Handler buildConsoleHandler() {
    
    StreamHandler cHandler = CustomConsoleHandler.newStderr();
    //ConsoleHandler cHandler = new ConsoleHandler();
    cHandler.setFormatter(new ConsoleFormatter());
    
    /*
    cHandler.setFilter(new Filter() {
    
        @Override
        public boolean isLoggable(LogRecord record) {
    	if(record.getLevel().intValue() > 700) {
    	    return false;
    	}
    
    	return true;
        }
    });
     */
    /*
    cHandler.setLevel(Level.ALL);
    
    return cHandler;
    }
     */

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (fileHandler == null) {
            return null;
        }
        // StreamHandler cHandler = CustomConsoleHandler.newStderr();
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
     * Messages written with this method are recorded as a log at warning level. Use this level to show a message for
     * cases that are supposed to never happen if the code is well used.
     *
     * @param msg
     */
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

    public static String buildErrorMessage(String originalMsg, Collection<StackTraceElement> elements) {

        final StringBuilder builder = new StringBuilder();
        builder.append(originalMsg);

        // Append the stack trace to the msg
        if (SpecsLogs.printStackTrace) {
            builder.append("\n\nStack Trace:");
            builder.append("\n--------------");

            for (final StackTraceElement element : elements) {
                builder.append("\n");
                builder.append(element);
            }

            builder.append("\n--------------");
            builder.append("\n");

        }

        return builder.toString();
    }

    /**
     * Writes a message to the logger with name defined by LOGGING_TAG.
     *
     * <p>
     * Messages written with this method are recorded as a log at info level. Use this level to show messages to the
     * user of a program.
     *
     * @param msg
     */
    public static void msgInfo(String msg) {
        msgInfo(Logger.getLogger(SpecsLogs.INFO_TAG), msg);
    }

    /**
     * Info-level message.
     * 
     * <p>
     * Accepting a Logger, since that should be the common case, keeping a reference to a Logger so that it does not get
     * garbage collected.
     * 
     * @param logger
     * @param msg
     */
    public static void msgInfo(Logger logger, String msg) {
        msg = parseMessage(msg);

        // if(globalLevel) {logger.setLevel(globalLevel);}
        logger.info(msg);

    }

    /**
     * Writes a message to the logger with name defined by LOGGING_TAG.
     *
     * <p>
     * Messages written with this method are recorded as a log at info level.
     *
     * @param msg
     */
    public static void msgLib(String msg) {
        msg = parseMessage(msg);
        // msgLib does not need support for printing the stack-trace, since it is to be used
        // to log information that does not represent programming errors.
        // Although it can be used to log user input errors, they are not to be resolved by looking
        // at the source code, hence not using support for stack-trace.
        Logger.getLogger(SpecsLogs.LIB_TAG).log(LogLevel.LIB, msg);
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
        msg = parseMessage(msg);

        getLoggerDebug().severe(msg);
    }

    /**
     * Adds a newline to the end of the message, if it does not have one.
     *
     * @param msg
     * @return
     */
    private static String parseMessage(String msg) {
        if (msg.isEmpty()) {
            return msg;
        }
        // return msg;
        // return String.format(msg+"%n");
        return msg + SpecsLogs.NEWLINE;
    }

    public static void setPrintStackTrace(boolean bool) {
        SpecsLogs.printStackTrace = bool;
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

    // public static void addLogFile(File file) {
    public static void addLog(PrintStream stream) {
        // Create file handler
        // SimpleFileHandler handler = SimpleFileHandler.newInstance(file);
        final SimpleFileHandler handler = new SimpleFileHandler(stream);

        // Set formatter
        handler.setFormatter(new ConsoleFormatter());

        // Add handler
        SpecsLogs.addHandler(handler);
    }

    public static void debug(Supplier<String> string) {
        // To avoid resolving the string unnecessarily
        if (SpecsSystem.isDebug()) {
            // Prefix
            String message = "[DEBUG] " + string.get();
            msgInfo(message);
            // debug(string.get());
        }
    }

    public static void debug(String string) {
        debug(() -> string);
        // if (SpecsSystem.isDebug()) {
        // // Prefix
        // string = "[DEBUG] " + string;
        // msgInfo(string);
        // }
    }

    /**
     * When a certain case has not been yet tested and it can appear on the field.
     * 
     * @param untestedAction
     */
    public static void untested(String untestedAction) {
        SpecsLogs.msgWarn(
                "Untested:" + untestedAction + ". Please contact the developers in order to add this case as a test.");
    }

}
