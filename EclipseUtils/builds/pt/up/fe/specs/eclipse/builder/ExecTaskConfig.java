/**
 * Copyright 2017 SPeCS.
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

package pt.up.fe.specs.eclipse.builder;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.base.Preconditions;

public class ExecTaskConfig {

    private static final Set<String> SUPPORTED_PROPERTIES = new HashSet<>(Arrays.asList("dir"));

    private final String workingDir;

    public ExecTaskConfig(String workingDir) {
        this.workingDir = workingDir;
    }

    public Optional<String> getWorkingDir() {
        return Optional.ofNullable(workingDir);
    }

    public static ExecTaskConfig parseArguments(List<String> arguments) {
        String workingDir = null;

        while (!arguments.isEmpty() && arguments.get(0).startsWith("[")) {
            String option = arguments.remove(0);
            // Remove square brackets
            Preconditions.checkArgument(option.endsWith("]"), "Expected option to end with ]: " + option);

            String workOption = option.substring(1, option.length() - 1);

            int equalIndex = workOption.indexOf('=');
            Preconditions.checkArgument(equalIndex != -1, "Expected an equals: " + option);

            String key = workOption.substring(0, equalIndex);
            String value = workOption.substring(equalIndex + 1, workOption.length());
            switch (key.toLowerCase()) {
            case "dir":
                workingDir = value;
                break;
            default:
                throw new RuntimeException(
                        "Property '" + key + "' not supported, supported properties: " + SUPPORTED_PROPERTIES);
            }
        }

        return new ExecTaskConfig(workingDir);
    }
}
