/**
 * Copyright 2018 SPeCS.
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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Represents the warning
 * 
 * @author JoaoBispo
 *
 */
public enum LogSourceInfo {

    NONE,
    SOURCE,
    STACK_TRACE;

    /**
     * Maps levels to the corresponding source info configuration.
     */
    private final static Map<Level, LogSourceInfo> LOGGER_SOURCE_INFO = new ConcurrentHashMap<>();
    /**
     * Warning shows stack trace by default
     */
    static {
        LOGGER_SOURCE_INFO.put(Level.WARNING, STACK_TRACE);
    }

    public static LogSourceInfo getLogSourceInfo(Level level) {
        // Handle null level
        if (level == null) {
            return NONE;
        }
        
        LogSourceInfo info = LOGGER_SOURCE_INFO.get(level);

        return info != null ? info : NONE;
    }

    public static void setLogSourceInfo(Level level, LogSourceInfo info) {
        // Handle null parameters
        if (level == null) {
            throw new NullPointerException("Level cannot be null");
        }
        if (info == null) {
            throw new NullPointerException("LogSourceInfo cannot be null");
        }
        
        LOGGER_SOURCE_INFO.put(level, info);
    }

}
