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

package org.suikasoft.jOptions.streamparser;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import org.suikasoft.jOptions.DataStore.ADataClass;
import org.suikasoft.jOptions.DataStore.DataClass;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.utilities.LineStream;

public interface LineStreamParser<T extends DataClass<T>> extends AutoCloseable {

    // static LineStreamParser newInstance(Map<String, LineStreamWorker> workers) {
    // return newInstance(DataStore.newInstance("Empty DataStore"), workers);
    // }

    static <T extends ADataClass<T>> LineStreamParser<T> newInstance(T inputData,
            Map<String, LineStreamWorker<T>> workers) {

        return new GenericLineStreamParser<>(inputData, workers);
    }

    /**
     * Returns a DataStore with the current parsed values.
     * 
     * @return DataStore with parsed data
     */
    public T getData();

    /**
     * Applies a LineStreamWorker to the given LineStream, based on the given id.
     * 
     * @param id
     * @param lineStream
     * @return true if the id was valid, false otherwise. When returning false, the LineStream remains unmodified
     */
    public boolean parse(String id, LineStream lineStream);

    /**
     * Each LineStreamWorker of this parser is associated to an id. This function returns the ids supported by this
     * LineStreamParser.
     * 
     * @return the LineStreamWorker ids supported by this parser
     */
    public Collection<String> getIds();

    default String parse(InputStream inputStream, File dumpFile) {
        return parse(inputStream, dumpFile, true, true);
    }

    /**
     * 
     * @param inputStream
     * @param dumpFile
     * @return lines of the inputStream that were not parsed
     */
    default String parse(InputStream inputStream, File dumpFile, boolean printLinesNotParsed,
            boolean storeLinesNotParsed) {

        StringBuilder linesNotParsed = new StringBuilder();

        try (LineStream lines = LineStream.newInstance(inputStream, null)) {
            lines.setDumpFile(dumpFile);

            while (lines.hasNextLine()) {

                // Line that will be used as id
                String currentLine = lines.nextLine();

                // If parser null, check linestream parsers
                if (getIds().contains(currentLine)) {
                    try {
                        parse(currentLine, lines);
                    } catch (Exception e) {
                        SpecsLogs.msgWarn("Problems while parsing '" + currentLine + "'", e);
                    }

                    continue;
                }

                // Add line to the warnings
                if (storeLinesNotParsed) {
                    linesNotParsed.append(currentLine).append("\n");
                }

                if (printLinesNotParsed) {
                    SpecsLogs.msgInfo(currentLine);
                }

            }
        }

        return linesNotParsed.toString();
    }
}
