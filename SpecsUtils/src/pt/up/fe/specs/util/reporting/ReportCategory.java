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
 * Enum for categorizing report messages.
 * <p>
 * Used for organizing and filtering reports.
 * </p>
 */
public enum ReportCategory {
    /**
     * Represents an error message.
     */
    ERROR,

    /**
     * Represents a warning message.
     */
    WARNING,

    /**
     * Represents an informational message.
     */
    INFORMATION
}
