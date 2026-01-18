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
 * specific language governing permissions and limitations under the License.
 */

package org.suikasoft.jOptions.streamparser;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.DataStore.DataClass;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.LineStream;

/**
 * Interface for parsing data from a
 * {@link pt.up.fe.specs.util.utilities.LineStream} into a {@link DataClass}.
 *
 * @param <T> the type of DataClass
 */
public interface LineStreamParser<T extends DataClass<T>> extends AutoCloseable {

    /**
     * Returns a new parser instance for the given input data and workers.
     *
     * @param inputData the initial data
     * @param workers   the map of worker IDs to workers
     * @return a new LineStreamParser
     */
    static <T extends ADataClass<T>> LineStreamParser<T> newInstance(T inputData,
            Map<String, LineStreamWorker<T>> workers) {
        return new GenericLineStreamParser<>(inputData, workers);
    }

    /**
     * Returns a DataStore with the current parsed values.
     *
     * @return DataStore with parsed data
     */
    T getData();

    /**
     * Applies a LineStreamWorker to the given LineStream, based on the given id.
     *
     * @param id         the worker id
     * @param lineStream the line stream
     * @return true if the id was valid, false otherwise
     */
    boolean parse(String id, LineStream lineStream);

    /**
     * Returns the IDs supported by this parser.
     *
     * @return the supported worker IDs
     */
    Collection<String> getIds();

    /**
     * Parses an input stream and optionally dumps unparsed lines to a file.
     *
     * @param inputStream the input stream
     * @param dumpFile    the file to dump unparsed lines
     * @return lines of the inputStream that were not parsed
     */
    default String parse(InputStream inputStream, File dumpFile) {
        return parse(inputStream, dumpFile, true, true);
    }

    /**
     * Parses an input stream and optionally dumps unparsed lines to a file.
     *
     * @param inputStream         the input stream
     * @param dumpFile            the file to dump unparsed lines
     * @param printLinesNotParsed whether to print unparsed lines
     * @param storeLinesNotParsed whether to store unparsed lines
     * @return lines of the inputStream that were not parsed
     */
    default String parse(InputStream inputStream, File dumpFile, boolean printLinesNotParsed,
            boolean storeLinesNotParsed) {

        StringBuilder linesNotParsed = new StringBuilder();

        try (LineStream lines = LineStream.newInstance(inputStream, null)) {
            lines.setDumpFile(dumpFile);
            if (SpecsSystem.isDebug()) {
                lines.enableLastLines(10);
            }

            while (lines.hasNextLine()) {

                // Line that will be used as id
                String currentLine = lines.nextLine();

                // If parser null, check linestream parsers
                if (getIds().contains(currentLine)) {
                    try {
                        parse(currentLine, lines);
                    } catch (Exception e) {
                        SpecsLogs.warn("Problems while parsing '" + currentLine + "'", e);
                    }

                    continue;
                }

                // If line should not be ignored, add to warnings
                if (!getLineIgnore().test(currentLine)) {
                    // Add line to the warnings
                    if (storeLinesNotParsed) {
                        if (SpecsSystem.isDebug()) {
                            SpecsLogs.debug(() -> "LineStreamParser: line not parsed, '" + currentLine
                                    + "'\nPrevious lines:\n"
                                    + String.join("\n", lines.getLastLines()));

                        }

                        linesNotParsed.append(currentLine).append("\n");
                    }

                    if (printLinesNotParsed) {
                        SpecsLogs.msgInfo(currentLine);
                    }
                } else {
                    SpecsLogs.debug("Ignoring line: " + currentLine);
                }

            }
        }

        return linesNotParsed.toString();
    }

    /**
     * Returns the number of lines read by the parser.
     *
     * @return the number of lines read
     */
    default long getReadLines() {
        SpecsLogs.debug("Not implemented yet, returning 0");
        return 0;
    }

    /**
     * Returns the number of characters read by the parser.
     *
     * @return the number of characters read
     */
    default long getReadChars() {
        SpecsLogs.debug("Not implemented yet, returning 0");
        return 0;
    }

    /**
     * Predicate that in case a line is not parsed, tests if it should be ignored.
     *
     * <p>
     * By default, always returns false (does not ignore lines).
     *
     * @return the predicate for ignoring lines
     */
    default Predicate<String> getLineIgnore() {
        return string -> false;
    }

    /**
     * Sets the predicate for ignoring lines.
     *
     * @param ignorePredicate the predicate for ignoring lines
     */
    void setLineIgnore(Predicate<String> ignorePredicate);

    /**
     * Returns the number of exceptions that occurred during parsing.
     *
     * @return the number of exceptions
     */
    int getNumExceptions();

    /**
     * Returns whether at least one exception has occurred during parsing.
     *
     * @return true if at least one exception has occurred, false otherwise
     */
    default boolean hasExceptions() {
        return getNumExceptions() > 0;
    }
}
