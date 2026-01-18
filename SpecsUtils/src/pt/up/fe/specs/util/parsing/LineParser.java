/*
 * Copyright 2010 SPeCS Research Group.
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

package pt.up.fe.specs.util.parsing;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Given a string, splits the string into a list of arguments, following some
 * rules.
 *
 * <p>
 * The rules for the default LineParser: <br>
 * - Spliting by a custom string (e.g. space ' '); <br>
 * - One line comments (e.g. //); <br>
 * - 'Joiner', to include characters left out by spliting character (e.g. " ->
 * "something written with spaces")
 *
 * @author Joao Bispo
 */
public class LineParser {

    /**
     * Builds a default LineParser.
     * <p>
     * The rules for the default LineParser: <br>
     * - Spliting -> space ' ' <br>
     * - 'Joiner', -> " (e.g. " -> "something written with spaces") <br>
     * - One line comments -> //
     * 
     * @return a LineParser
     */
    public static LineParser getDefaultLineParser() {
        return new LineParser(" ", "\"", "//");
    }

    public LineParser(String splittingString, String joinerString, String oneLineComment) {
        this.commandSeparator = splittingString;
        this.commandGatherer = joinerString;
        this.commentPrefix = oneLineComment;

        // Make some checks
        if (oneLineComment.isEmpty()) {
            Logger.getLogger(LineParser.class.getName())
                    .warning("OneLineComment is an empty string. This will make all " +
                            "lines in the file appear as comments.");
        }
    }

    public String getOneLineComment() {
        return this.commentPrefix;
    }

    public String getSplittingString() {
        return this.commandSeparator;
    }

    public String getJoinerString() {
        return this.commandGatherer;
    }

    /**
     * Splits the String into arguments, following the rules of the parser.
     *
     * <p>
     * The input string is trimmed before parsing.
     *
     */
    public List<String> splitCommand(String command) {
        // Trim string
        command = command.trim();

        // Check if it starts with comment
        if (command.startsWith(this.commentPrefix)) {
            return new ArrayList<>();
        }

        List<String> commands = new ArrayList<>();

        while (!command.isEmpty()) {
            // Get indexes
            int spaceIndex = command.indexOf(this.commandSeparator);
            int quoteIndex = this.commandGatherer.isEmpty() ? -1 : command.indexOf(this.commandGatherer);

            // Check which comes first
            if (spaceIndex == -1 && quoteIndex == -1) {
                // No more separators or quotes, add remaining command
                commands.add(command);
                command = "";
                continue;
            }

            if (spaceIndex != -1 && (quoteIndex == -1 || spaceIndex < quoteIndex)) {
                // Space/separator comes first
                String argument = command.substring(0, spaceIndex);
                if (!argument.isEmpty()) {
                    commands.add(argument);
                }
                command = command.substring(spaceIndex + this.commandSeparator.length()).trim();
            } else {
                // Quote comes first
                // Find second quote
                int quoteIndex2Increment = command.substring(quoteIndex + 1).indexOf(this.commandGatherer);
                if (quoteIndex2Increment == -1 && spaceIndex == -1) {
                    // Unclosed quote at end - treat everything after first quote as one argument
                    String argument = command.substring(quoteIndex + 1).trim();
                    commands.add(argument);
                    command = "";
                } else if (quoteIndex2Increment == -1 && spaceIndex != -1) {
                    // Unclosed quote but space found - take from quote to space
                    String argument = command.substring(quoteIndex + 1, spaceIndex);
                    commands.add(argument);
                    command = command.substring(spaceIndex + this.commandSeparator.length()).trim();
                } else {
                    // Found closing quote
                    int quote2 = quoteIndex + 1 + quoteIndex2Increment;
                    String argument = command.substring(quoteIndex + 1, quote2);
                    commands.add(argument);
                    command = command.substring(quote2 + 1).trim();
                }
            }
        }

        return commands;
    }

    /**
     * INSTANCE VARIABLES
     */
    private final String commandSeparator;
    private final String commandGatherer;
    private final String commentPrefix;

}
