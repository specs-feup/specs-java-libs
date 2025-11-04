/**
 * Copyright 2021 SPeCS.
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

package pt.up.fe.specs.util.utilities;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;

import pt.up.fe.specs.util.SpecsLogs;

public class PrintOnce {

    private static final Set<String> PRINTED_MESSAGES = ConcurrentHashMap.newKeySet();

    public static void info(String message) {
        // Handle null messages by using a special marker
        String key = message == null ? "__NULL_MESSAGE__" : message;

        if (PRINTED_MESSAGES.add(key)) {
            SpecsLogs.info(message);
        }
    }

    /**
     * Clears the internal cache of printed messages. This is primarily intended for testing
     * purposes to ensure test isolation. In production code, this should rarely be needed
     * as the whole point of PrintOnce is to maintain state across the application lifecycle.
     */
    public static void clearCache() {
        PRINTED_MESSAGES.clear();
    }
}
