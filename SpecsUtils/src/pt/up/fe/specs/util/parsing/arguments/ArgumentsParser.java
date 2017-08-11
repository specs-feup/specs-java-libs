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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.utilities.StringSlice;

public class ArgumentsParser {

    private final List<String> delimiters;
    private final List<Gluer> gluers;
    private final List<Escape> escapes;

    private Gluer currentGluer;

    public ArgumentsParser(List<String> delimiters, List<Gluer> gluers, List<Escape> escapes) {
        this.gluers = gluers;
        this.escapes = escapes;
        this.delimiters = delimiters;
        this.currentGluer = null;
    }

    /**
     * Argument parser that delimits arguments by spaces (' '), glues them with double quotes ('"') and escapes single
     * characters with backslash ('\').
     * 
     * @return
     */
    public static ArgumentsParser newCommandLine() {
        return new ArgumentsParser(Arrays.asList(" "), Arrays.asList(Gluer.newDoubleQuote()),
                Arrays.asList(Escape.newSlashChar()));
    }

    public List<String> parse(String string) {
        List<String> args = new ArrayList<>();
        StringBuilder currentArg = new StringBuilder();
        StringSlice slice = new StringSlice(string);

        while (!slice.isEmpty()) {
            // Escapes
            Optional<StringSlice> escapeSlice = checkEscapes(slice);
            if (escapeSlice.isPresent()) {
                StringSlice argPart = escapeSlice.get();

                // Add to current arg
                currentArg.append(argPart);
                // Update slice
                slice = slice.substring(argPart.length());
                continue;
            }

            // Gluers

            // If no current gluer, check if it is the start of a new one
            if (currentGluer == null) {
                Optional<Gluer> gluer = checkGluerStart(slice);
                if (gluer.isPresent()) {
                    // Discard the gluer characters
                    // Activate gluer
                    currentGluer = gluer.get();
                    // Update slice
                    slice = slice.substring(currentGluer.getGluerStart().length());
                    continue;
                }
            }
            // There is a Gluer active, check if this is the end of the Gluer
            else {
                if (slice.startsWith(currentGluer.getGluerEnd())) {
                    // Update slice
                    slice = slice.substring(currentGluer.getGluerEnd().length());
                    currentGluer = null;
                    continue;
                }
            }

            // Delimiters

            // Delimiters are only enabled if there is no current gluer
            if (currentGluer == null) {
                Optional<String> delimiter = checkDelimiters(slice);
                // If there is a delimiter, store current argument (if not empty) and reset current argument
                if (delimiter.isPresent()) {
                    // Update slice
                    slice = slice.substring(delimiter.get().length());

                    // Add arg if not empty
                    String tentativeArg = currentArg.toString();
                    if (!tentativeArg.isEmpty()) {
                        args.add(tentativeArg);
                    }

                    // Reset current arg
                    currentArg = new StringBuilder();
                    continue;
                }

            }

            // Store character and continue
            currentArg.append(slice.substring(0, 1));
            slice = slice.substring(1);
        }

        // If there is a Gluer active, it was not closed
        if (currentGluer != null) {
            throw new RuntimeException("Started Gluer with '" + currentGluer.getGluerStart()
                    + "' but did not close it ('" + currentGluer.getGluerEnd() + "'): " + string);
        }

        // If current argument is not empty, add it to the list of args
        if (currentArg.length() != 0) {
            args.add(currentArg.toString());
        }

        // Trim args
        return args.stream().map(String::trim).collect(Collectors.toList());
    }

    private Optional<String> checkDelimiters(StringSlice slice) {
        for (String delimiter : delimiters) {
            if (slice.startsWith(delimiter)) {
                return Optional.of(delimiter);
            }
        }

        return Optional.empty();
    }

    private Optional<Gluer> checkGluerStart(StringSlice slice) {
        for (Gluer gluer : gluers) {
            if (slice.startsWith(gluer.getGluerStart())) {
                return Optional.of(gluer);
            }
        }

        return Optional.empty();
    }

    private Optional<StringSlice> checkEscapes(StringSlice slice) {
        // First check if it starts with an escape character
        for (Escape escape : escapes) {
            Optional<StringSlice> capturedEscape = escape.captureEscape(slice);
            if (capturedEscape.isPresent()) {
                return capturedEscape;
            }
        }
        return Optional.empty();
    }

}
