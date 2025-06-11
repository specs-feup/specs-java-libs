/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.specs.git;

import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Enum representing options that can be specified in a Git repository URL.
 */
public enum GitUrlOption implements StringProvider {
    /**
     * A commit or branch that should be used.
     */
    COMMIT,
    /**
     * A folder inside the repository.
     */
    FOLDER;

    private final String option;

    private GitUrlOption() {
        this.option = name().toLowerCase();
    }

    /**
     * Returns the string representation of the option.
     *
     * @return the option as a string
     */
    @Override
    public String getString() {
        return option;
    }
}
