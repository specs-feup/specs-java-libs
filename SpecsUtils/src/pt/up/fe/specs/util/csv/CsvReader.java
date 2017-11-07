/**
 * Copyright 2016 SPeCS.
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

package pt.up.fe.specs.util.csv;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pt.up.fe.specs.util.utilities.LineStream;

/**
 * Reads CSV files.
 * 
 * @author JoaoBispo
 *
 */
public class CsvReader implements AutoCloseable {
    private final static String DEFAULT_DELIMITER = ";";

    private final LineStream csvLines;

    private List<String> header;
    private String delimiter;

    public CsvReader(File csvFile) {
        this(csvFile, DEFAULT_DELIMITER);
    }

    public CsvReader(File csvFile, String delimiter) {
        this(LineStream.newInstance(csvFile), delimiter);
    }

    public CsvReader(String csvContents) {
        this(csvContents, DEFAULT_DELIMITER);
    }

    public CsvReader(String csvContents, String delimiter) {
        this(LineStream.newInstance(csvContents), delimiter);
    }

    private CsvReader(LineStream csvLines, String delimiter) {
        this.csvLines = csvLines;
        this.delimiter = delimiter;

        initHeader();
    }

    public List<String> getHeader() {
        return header;
    }

    public boolean hasNext() {
        return csvLines.hasNextLine();
    }

    /**
     * 
     * @return
     */
    public List<String> next() {

        // Header is parsed, return data
        return parseInternal();

    }

    private void initHeader() {
        // If header is not set, parse lines and ignore result until it is parsed
        while (header == null) {
            parseInternal();

            if (!csvLines.hasNextLine()) {
                throw new RuntimeException("Could not find a header in CSV file");
            }
        }

    }

    private List<String> parseInternal() {
        // If no more lines, return empty
        if (!csvLines.hasNextLine()) {
            return Collections.emptyList();
        }

        String csvLine = csvLines.nextLine();

        return parse(csvLine);
    }

    private List<String> parse(String csvLine) {
        // Parse separator ('sep=')
        if (csvLine.startsWith("sep=")) {
            this.delimiter = csvLine.substring("sep=".length()).trim();
            return Collections.emptyList();
        }

        // Parse line
        List<String> elements = Arrays.asList(csvLine.split(delimiter));

        // Check if header needs to be set
        if (header == null) {
            header = elements;
            return elements;
        }

        return elements;
    }

    @Override
    public void close() {
        csvLines.close();
    }

}
