/**
 * Copyright 2012 SPeCS Research Group.
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

package pt.up.fe.specs.util.logging;

import java.util.logging.Level;

public class LogLevel extends Level {

    protected LogLevel(String name, int value) {
        super(name, value);
    }

    protected LogLevel(String name, int value, String resourceBundleName) {
        super(name, value, resourceBundleName);
    }

    private static final long serialVersionUID = 1L;
    private static final String defaultBundle = "sun.util.logging.resources.logging";

    public static final Level LIB = new LogLevel("LIB", 750, LogLevel.defaultBundle);

}
