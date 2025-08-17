/*
 * Copyright 2011 SPeCS Research Group.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.lazy.ThreadSafeLazy;

/**
 * Writes CSV files.
 *
 * @author Joao Bispo
 */
public class CsvWriter {

    private final static String DEFAULT_DELIMITER = ";";

    /**
     * TODO: Check where this is used, probably replace with CsvWriter
     *
     * @return
     */
    public static String getDefaultDelimiter() {
        return DEFAULT_DELIMITER;
    }

    // Configuration
    private String delimiter;
    private String newline;
    private final boolean excelSupport;
    private int dataOffset; // The column where data starts. By default, is 1 (the second column)
    private final List<CsvField> extraFields; // Additional predefined fields that are applied over the data

    /* State */
    private final List<String> header;
    private List<List<String>> lines;

    private final Lazy<String> startColumn;
    private final Lazy<String> endColumn;

    public CsvWriter(String... header) {
        this(Arrays.asList(header));
    }

    public CsvWriter(List<String> header) {
        this.delimiter = CsvWriter.DEFAULT_DELIMITER;
        // newline = System.lineSeparator();
        this.newline = System.getProperty("line.separator");
        this.header = header;
        this.lines = new ArrayList<>();
        this.excelSupport = true;
        this.dataOffset = 1;
        this.extraFields = new ArrayList<>();
        this.startColumn = new ThreadSafeLazy<>(this::getDataStartColumn);
        this.endColumn = new ThreadSafeLazy<>(this::getDataEndColumn);
    }

    private String getDataStartColumn() {
        return SpecsStrings.toExcelColumn(1 + dataOffset);
    }

    private String getDataEndColumn() {
        return SpecsStrings.toExcelColumn(header.size());
    }

    public CsvWriter addField(CsvField... fields) {
        addField(Arrays.asList(fields));
        return this;
    }

    public CsvWriter addField(List<CsvField> fields) {
        this.extraFields.addAll(fields);
        return this;
    }

    public CsvWriter addLine(String... elements) {
        addLine(Arrays.asList(elements));
        return this;
    }

    public CsvWriter addLine(Object... elements) {
        addLineToString(Arrays.asList(elements));
        return this;
    }

    public CsvWriter addLineToString(List<Object> elements) {
        List<String> stringElements = new ArrayList<>(elements.size());
        for (Object object : elements) {
            if (object == null) {
                stringElements.add("null");
            } else {
                stringElements.add(object.toString());
            }
        }

        addLine(stringElements);

        return this;
    }

    public CsvWriter addLine(List<String> elements) {
        if (!isHeaderSet()) {
            SpecsLogs.warn("Header not set yet, cannot verify size of line.");
        } else {
            if (this.header.size() != elements.size()) {
                SpecsLogs.warn("Number of elements (" + elements.size() +
                        ") different than header elements (" + this.header.size() + ").");
            }
        }

        this.lines.add(elements);

        return this;
    }

    protected String buildHeader() {
        StringBuilder csv = new StringBuilder();

        // Separator
        if (excelSupport) {
            csv.append("sep=").append(this.delimiter).append(newline);
        }

        // Header
        csv.append(this.header.get(0));
        for (int i = 1; i < this.header.size(); i++) {
            csv.append(this.delimiter).append(this.header.get(i));
        }

        // Add processing fields
        if (!extraFields.isEmpty()) {
            String extraFieldsHeader = extraFields.stream()
                    .map(CsvField::getHeader)
                    .collect(Collectors.joining(delimiter, delimiter, ""));

            csv.append(extraFieldsHeader);
        }

        csv.append(this.newline);

        return csv.toString();
    }

    protected String buildLine(List<String> line, int lineNumber) {
        StringBuilder csv = new StringBuilder();

        String csvLine = line.stream()
                .collect(Collectors.joining(delimiter));

        csv.append(csvLine);

        // Check if there are fields to compute
        if (!extraFields.isEmpty()) {
            String lineNumberString = Integer.toString(lineNumber);
            String range = startColumn.get() + lineNumberString + ":" + endColumn.get() + lineNumberString;

            String csvExtraFields = extraFields.stream()
                    .map(field -> field.getField(range))
                    .collect(Collectors.joining(delimiter, delimiter, ""));

            csv.append(csvExtraFields);
        }

        csv.append(newline);

        return csv.toString();
        /*
        StringBuilder csvLine = new StringBuilder();

        csvLine.append(line.get(0));
        for (int i = 1; i < line.size(); i++) {
            csvLine.append(this.delimiter).append(line.get(i));
        }
        csvLine.append(this.newline);

        return csvLine.toString();
        */
    }

    public String buildCsv() {
        if (!isHeaderSet()) {
            SpecsLogs.warn("Header was not set.");
            return null;
        }

        StringBuilder builder = new StringBuilder();

        builder.append(buildHeader());
        /*
        // Separator
        builder.append("sep=").append(this.delimiter).append("\n");

        // Header
        builder.append(this.header.get(0));
        for (int i = 1; i < this.header.size(); i++) {
            builder.append(this.delimiter).append(this.header.get(i));
        }

        builder.append(this.newline);
        */

        // First line is the header
        int lineCounter = 2;

        // Lines
        for (List<String> line : this.lines) {
            builder.append(buildLine(line, lineCounter));
            lineCounter++;
            /*
            builder.append(line.get(0));
            for (int i = 1; i < line.size(); i++) {
                builder.append(this.delimiter).append(line.get(i));
            }
            builder.append(this.newline);
            */
        }

        return builder.toString();
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setNewline(String newline) {
        this.newline = newline;
    }

    public boolean isHeaderSet() {
        if (this.header == null) {
            return false;
        }

        return true;
    }

}
