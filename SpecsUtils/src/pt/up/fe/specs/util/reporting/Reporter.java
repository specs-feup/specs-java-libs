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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.reporting;

import java.io.PrintStream;

import pt.up.fe.specs.util.Preconditions;

/**
 * Interface for reporting messages and events.
 * <p>
 * Used for logging, error reporting, and user feedback.
 * </p>
 *
 * @author Luís Reis
 */
public interface Reporter {
    /**
     * Emits a warning or error.
     * 
     * <p>
     * A warning is a potential problem in the code that does not prevent the
     * generation of valid C code. It usually indicates bugs or performance issues.
     * 
     * <p>
     * An error is an actual problem in the code that prevents the generation of C
     * code and therefore should stop the code generation through an exception.
     * 
     * @param type    The type of message.
     * @param message The message body. Messages should be formatted as one or more
     *                simple sentences. Usually ends in a "."
     *                or "?".
     */
    public void emitMessage(MessageType type, String message);

    /**
     * Prints the stack trace to the provided PrintStream.
     * 
     * @param reportStream The stream where the stack trace will be printed.
     */
    public void printStackTrace(PrintStream reportStream);

    /**
     * Retrieves the PrintStream used for reporting.
     * 
     * @return The PrintStream used for reporting.
     */
    public PrintStream getReportStream();

    /**
     * Emits an error.
     * 
     * <p>
     * An error is an actual problem in the code that prevents the generation of C
     * code and therefore should stop the code generation through an exception.
     * 
     * @param type    The type of message.
     * @param message The message body. Messages should be formatted as one or more
     *                simple sentences. Usually ends in a "."
     *                or "?".
     * @return A null RuntimeException. It is merely meant to enable the "throw
     *         emitError()" syntax.
     */
    public default RuntimeException emitError(MessageType type, String message) {
        Preconditions.checkArgument(type.getMessageCategory() == ReportCategory.ERROR);

        // Ensure default emitError is thread-safe for implementations that rely on
        // default methods to serialize access to their internal state.
        synchronized (this) {
            emitMessage(type, message);
        }

        return new RuntimeException(message);
    }

    /**
     * Emits a default warning message.
     * 
     * @param message The warning message to be emitted.
     */
    public default void warn(String message) {
        synchronized (this) {
            emitMessage(MessageType.WARNING_TYPE, message);
        }
    }

    /**
     * Emits a default info message.
     * 
     * @param message The info message to be emitted.
     */
    public default void info(String message) {
        synchronized (this) {
            emitMessage(MessageType.INFO_TYPE, message);
        }
    }

    /**
     * Emits a default error message.
     * 
     * @param message The error message to be emitted.
     * @return A RuntimeException containing the error message.
     */
    public default RuntimeException error(String message) {
        // defer to emitError (which is synchronized)
        return emitError(MessageType.ERROR_TYPE, message);
    }
}
