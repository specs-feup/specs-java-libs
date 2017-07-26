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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.parsing.arguments;

import java.util.Optional;
import java.util.function.Function;

import pt.up.fe.specs.util.utilities.StringSlice;

public class Escape {

    private final String escapeStart;
    private final Function<StringSlice, StringSlice> escapeCapturer;

    public Escape(String escapeStart, Function<StringSlice, StringSlice> escapeCapturer) {
        this.escapeStart = escapeStart;
        this.escapeCapturer = escapeCapturer;
    }

    /**
     * An escape that happens when a '\' appears, and escapes the next character.
     * 
     * @return
     */
    public static Escape newSlashChar() {
        String escapeStart = "\\";
        Function<StringSlice, StringSlice> escapeCapturer = slice -> slice.substring(0, 2);

        return new Escape(escapeStart, escapeCapturer);
    }

    public Optional<StringSlice> captureEscape(StringSlice slice) {
        if (!slice.startsWith(escapeStart)) {
            return Optional.empty();
        }

        // Found escape, capture it
        return Optional.of(escapeCapturer.apply(slice));
    }
}
