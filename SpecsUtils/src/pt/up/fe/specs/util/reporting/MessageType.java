/*
 * MessageType.java
 *
 * Interface for defining types of messages in reporting. Used for categorizing and handling different message types in the SPeCS ecosystem.
 *
 * Copyright 2025 SPeCS.
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

/**
 * Interface for defining types of messages in reporting.
 * <p>
 * Used for categorizing and handling different message types.
 * </p>
 *
 * @author Luís Reis
 * @see Reporter
 */
public interface MessageType {
    /**
     * Returns the name of the message type.
     * By default, it returns the string representation of the message type.
     *
     * @return the name of the message type
     */
    public default String getName() {
	return toString();
    }

    /**
     * Returns the category of the message type.
     *
     * @return the category of the message type
     */
    public ReportCategory getMessageCategory();

    /**
     * A default info message type.
     */
    public static final MessageType INFO_TYPE = new DefaultMessageType("Info", ReportCategory.INFORMATION);
    /**
     * A default warning message type.
     */
    public static final MessageType WARNING_TYPE = new DefaultMessageType("Warning", ReportCategory.WARNING);
    /**
     * A default error message type.
     */
    public static final MessageType ERROR_TYPE = new DefaultMessageType("Error", ReportCategory.ERROR);

}
