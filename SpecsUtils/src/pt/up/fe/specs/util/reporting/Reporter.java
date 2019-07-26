/**
 * Copyright 2015 SPeCS.
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

import java.io.PrintStream;

import pt.up.fe.specs.util.Preconditions;

/**
 * An interface to unify the error/warning reporting system.
 * 
 * @author Luís Reis
 *
 */
public interface Reporter {
    /**
     * Emits a warning or error.
     * 
     * <p>
     * A warning is a potential problem in the code that does not prevent the generation of valid C code. It usually
     * indicates bugs or performance issues.
     * 
     * <p>
     * An error is an actual problem in the code that prevents the generation of C code and therefore should stop the
     * code generation through an exception.
     * 
     * @param type
     *            The type of message.
     * @param message
     *            The message body. Messages should be formatted as one or more simple sentences. Usually ends in a "."
     *            or "?".
     */
    public void emitMessage(MessageType type, String message);

    public void printStackTrace(PrintStream reportStream);

    public PrintStream getReportStream();

    /**
     * Emits an error.
     * 
     * <p>
     * An error is an actual problem in the code that prevents the generation of C code and therefore should stop the
     * code generation through an exception.
     * 
     * @param type
     *            The type of message.
     * @param message
     *            The message body. Messages should be formatted as one or more simple sentences. Usually ends in a "."
     *            or "?".
     * @return A null RuntimeException. It is merely meant to enable the "throw emitError()" syntax.
     */
    public default RuntimeException emitError(MessageType type, String message) {
	Preconditions.checkArgument(type.getMessageCategory() == ReportCategory.ERROR);

	emitMessage(type, message);

	return new RuntimeException(message);
    }

    /**
     * Emits a default warning message.
     * 
     * @param message
     */
    public default void warn(String message) {
	emitMessage(MessageType.WARNING_TYPE, message);
    }

    /**
     * Emits a default info message.
     * 
     * @param message
     */
    public default void info(String message) {
	emitMessage(MessageType.INFO_TYPE, message);
    }

    /**
     * Emits a default error message.
     * 
     * @param message
     */
    public default RuntimeException error(String message) {
	return emitError(MessageType.ERROR_TYPE, message);
    }
}
