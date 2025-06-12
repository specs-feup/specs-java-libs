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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.reporting;

/**
 * Class for default message types in reporting.
 * <p>
 * Used for standardizing message types in the SPeCS ecosystem.
 * </p>
 */
class DefaultMessageType implements MessageType {

    /**
     * The name of the message type.
     */
    private final String name;

    /**
     * The category of the message type.
     */
    private final ReportCategory category;

    /**
     * Constructs a DefaultMessageType with the given name and category.
     *
     * @param name     the name of the message type
     * @param category the category of the message type
     */
    public DefaultMessageType(String name, ReportCategory category) {
	this.name = name;
	this.category = category;
    }

    /**
     * Gets the name of the message type.
     *
     * @return the name of the message type
     */
    @Override
    public String getName() {
	return this.name;
    }

    /**
     * Gets the category of the message type.
     *
     * @return the category of the message type
     */
    @Override
    public ReportCategory getMessageCategory() {
	return this.category;
    }

}
