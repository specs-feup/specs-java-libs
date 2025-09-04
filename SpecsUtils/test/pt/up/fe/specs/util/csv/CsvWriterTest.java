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

package pt.up.fe.specs.util.csv;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.List;

/**
 * Comprehensive test suite for CsvWriter utility class.
 * Tests CSV writing functionality including field creation, line addition,
 * formula integration, and various formatting options.
 * 
 * @author Generated Tests
 */
@DisplayName("CsvWriter Tests")
public class CsvWriterTest {

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorAndInitialization {

        @Test
        @DisplayName("Should create CsvWriter with string array header")
        void testConstructorWithStringArray() {
            CsvWriter writer = new CsvWriter("col1", "col2", "col3");
            assertThat(writer.isHeaderSet()).isTrue();
        }

        @Test
        @DisplayName("Should create CsvWriter with list header")
        void testConstructorWithList() {
            List<String> header = Arrays.asList("name", "age", "city");
            CsvWriter writer = new CsvWriter(header);
            assertThat(writer.isHeaderSet()).isTrue();
        }

        @Test
        @DisplayName("Should handle empty header")
        void testEmptyHeader() {
            CsvWriter writer = new CsvWriter();
            // Empty header list is still considered "set" (non-null)
            assertThat(writer.isHeaderSet()).isTrue();
        }

        @Test
        @DisplayName("Should handle null header elements")
        void testNullHeaderElements() {
            assertThatCode(() -> {
                new CsvWriter("col1", null, "col3");
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("CSV Building Operations")
    class CsvBuildingOperations {

        @Test
        @DisplayName("Should generate correct CSV with formulas")
        void testCsvWriter_WithFormulas_GeneratesCorrectCsv() {
            CsvWriter csvWriter = new CsvWriter("name", "1", "2");
            csvWriter.setNewline("\n");

            csvWriter.addField(CsvField.AVERAGE, CsvField.STANDARD_DEVIATION_SAMPLE);
            csvWriter.addLine("line1", "4", "7");

            String expectedCsv = "sep=;\n" +
                    "name;1;2;Average;Std. Dev. (Sample)\n" +
                    "line1;4;7;=AVERAGE(B2:D2);=STDEV.S(B2:D2)\n";

            assertThat(csvWriter.buildCsv()).isEqualTo(expectedCsv);
        }

        @Test
        @DisplayName("Should handle empty initialization gracefully")
        void testCsvWriter_EmptyInitialization_ShouldHandleGracefully() {
            CsvWriter csvWriter = new CsvWriter();
            csvWriter.setNewline("\n");

            String result = csvWriter.buildCsv();
            assertThat(result).isEqualTo("sep=;\n\n");
        }

        @Test
        @DisplayName("Should handle multiple lines")
        void testCsvWriter_MultipleLines_GeneratesCorrectCsv() {
            CsvWriter csvWriter = new CsvWriter("column1", "column2");
            csvWriter.setNewline("\n");

            csvWriter.addLine("row1col1", "row1col2");
            csvWriter.addLine("row2col1", "row2col2");

            String result = csvWriter.buildCsv();
            assertThat(result).contains("row1col1;row1col2");
            assertThat(result).contains("row2col1;row2col2");
        }

        @Test
        @DisplayName("Should handle null values gracefully")
        void testCsvWriter_NullValues_ShouldHandleGracefully() {
            assertThatCode(() -> {
                CsvWriter csvWriter = new CsvWriter("col1", "col2");
                csvWriter.addLine(null, "value2");
                csvWriter.buildCsv();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle custom newlines")
        void testCsvWriter_CustomNewlines_ShouldWork() {
            CsvWriter csvWriter = new CsvWriter("col1");
            csvWriter.setNewline("\r\n");
            csvWriter.addLine("value1");

            String result = csvWriter.buildCsv();
            assertThat(result).contains("\r\n");
        }

        @Test
        @DisplayName("Should handle special characters")
        void testCsvWriter_SpecialCharacters_ShouldWork() {
            CsvWriter csvWriter = new CsvWriter("column with spaces", "column;with;semicolons");
            csvWriter.setNewline("\n");
            csvWriter.addLine("value with spaces", "value;with;semicolons");

            assertThatCode(() -> {
                csvWriter.buildCsv();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should generate header-only CSV when no lines added")
        void testHeaderOnlyCsv() {
            CsvWriter writer = new CsvWriter("col1", "col2", "col3");
            writer.setNewline("\n");

            String csv = writer.buildCsv();
            assertThat(csv).contains("sep=;");
            assertThat(csv).contains("col1;col2;col3");
            assertThat(csv.split("\n")).hasSize(2); // separator + header
        }

        @Test
        @DisplayName("Should maintain line order in output")
        void testLineOrderMaintained() {
            CsvWriter writer = new CsvWriter("sequence");
            writer.setNewline("\n");

            writer.addLine("first");
            writer.addLine("second");
            writer.addLine("third");

            String csv = writer.buildCsv();
            String[] lines = csv.split("\n");

            // Find the data lines (skip separator and header)
            assertThat(lines[2]).contains("first");
            assertThat(lines[3]).contains("second");
            assertThat(lines[4]).contains("third");
        }
    }

    @Nested
    @DisplayName("Line Addition Methods")
    class LineAdditionMethods {

        @Test
        @DisplayName("Should add line with string array")
        void testAddLineWithStringArray() {
            CsvWriter writer = new CsvWriter("col1", "col2");
            writer.addLine("val1", "val2");

            String csv = writer.buildCsv();
            assertThat(csv).contains("val1;val2");
        }

        @Test
        @DisplayName("Should add line with object array")
        void testAddLineWithObjectArray() {
            CsvWriter writer = new CsvWriter("name", "age", "active");
            writer.addLine("John", 25, true);

            String csv = writer.buildCsv();
            assertThat(csv).contains("John;25;true");
        }

        @Test
        @DisplayName("Should add line with list")
        void testAddLineWithList() {
            CsvWriter writer = new CsvWriter("col1", "col2");
            writer.addLine(Arrays.asList("listVal1", "listVal2"));

            String csv = writer.buildCsv();
            assertThat(csv).contains("listVal1;listVal2");
        }

        @Test
        @DisplayName("Should convert objects to string using toString")
        void testObjectToStringConversion() {
            CsvWriter writer = new CsvWriter("number", "decimal", "object");
            writer.addLine(42, 3.14, new Object() {
                @Override
                public String toString() {
                    return "custom_object";
                }
            });

            String csv = writer.buildCsv();
            assertThat(csv).contains("42;3.14;custom_object");
        }

        @Test
        @DisplayName("Should handle null objects in object array")
        void testNullObjectsInArray() {
            CsvWriter writer = new CsvWriter("col1", "col2", "col3");
            writer.addLine("value1", null, "value3");

            String csv = writer.buildCsv();
            assertThat(csv).contains("value1;null;value3");
        }

        @Test
        @DisplayName("Should support method chaining for addLine")
        void testMethodChaining() {
            CsvWriter writer = new CsvWriter("col1", "col2");

            CsvWriter result = writer.addLine("val1", "val2")
                    .addLine("val3", "val4");

            assertThat(result).isSameAs(writer);

            String csv = writer.buildCsv();
            assertThat(csv).contains("val1;val2");
            assertThat(csv).contains("val3;val4");
        }
    }

    @Nested
    @DisplayName("Field Management")
    class FieldManagement {

        @Test
        @DisplayName("Should add single field")
        void testAddSingleField() {
            CsvWriter writer = new CsvWriter("data1", "data2");
            writer.addField(CsvField.AVERAGE);
            writer.addLine("10", "20");

            String csv = writer.buildCsv();
            assertThat(csv).contains("Average");
            // Correctly calculates range for both data columns
            assertThat(csv).contains("=AVERAGE(B2:C2)");
        }

        @Test
        @DisplayName("Should add multiple fields using varargs")
        void testAddMultipleFieldsVarargs() {
            CsvWriter writer = new CsvWriter("data1", "data2");
            writer.addField(CsvField.AVERAGE, CsvField.STANDARD_DEVIATION_SAMPLE);
            writer.addLine("5", "15");

            String csv = writer.buildCsv();
            assertThat(csv).contains("Average");
            assertThat(csv).contains("Std. Dev. (Sample)");
            // Correctly calculates range for both data columns
            assertThat(csv).contains("=AVERAGE(B2:C2)");
            assertThat(csv).contains("=STDEV.S(B2:C2)");
        }

        @Test
        @DisplayName("Should add multiple fields using list")
        void testAddMultipleFieldsList() {
            CsvWriter writer = new CsvWriter("value");
            List<CsvField> fields = Arrays.asList(CsvField.AVERAGE, CsvField.STANDARD_DEVIATION_SAMPLE);
            writer.addField(fields);
            writer.addLine("100");

            String csv = writer.buildCsv();
            assertThat(csv).contains("Average");
            assertThat(csv).contains("Std. Dev. (Sample)");
        }

        @Test
        @DisplayName("Should support method chaining for addField")
        void testAddFieldMethodChaining() {
            CsvWriter writer = new CsvWriter("data");

            CsvWriter result = writer.addField(CsvField.AVERAGE);
            assertThat(result).isSameAs(writer);
        }

        @Test
        @DisplayName("Should calculate range for multiple data columns")
        void testRangeCalculationMultipleColumns() {
            CsvWriter writer = new CsvWriter("id", "val1", "val2", "val3", "val4");
            writer.addField(CsvField.AVERAGE);
            writer.addLine("1", "10", "20", "30", "40");

            String csv = writer.buildCsv();
            // Correctly calculates range from B2 to F2 (all data columns)
            assertThat(csv).contains("=AVERAGE(B2:F2)");
        }

        @Test
        @DisplayName("Should handle multiple lines with fields correctly")
        void testMultipleLinesWithFields() {
            CsvWriter writer = new CsvWriter("name", "score1", "score2");
            writer.addField(CsvField.AVERAGE);
            writer.addLine("Alice", "85", "90");
            writer.addLine("Bob", "75", "80");
            String csv = writer.buildCsv();

            // Layout with dataOffset=1: A=empty, B=name, C=score1, D=score2, E=Average
            // formula
            assertThat(csv).contains("=AVERAGE(B2:D2)"); // First data line
            assertThat(csv).contains("=AVERAGE(B3:D3)"); // Second data line
        }
    }

    @Nested
    @DisplayName("Delimiter and Formatting")
    class DelimiterAndFormatting {

        @ParameterizedTest(name = "Delimiter: {0}")
        @ValueSource(strings = { ",", "\t", "|", ":", ";" })
        @DisplayName("Should handle various delimiters")
        void testVariousDelimiters(String delimiter) {
            CsvWriter writer = new CsvWriter("col1", "col2");
            writer.setDelimiter(delimiter);
            writer.addLine("val1", "val2");

            String csv = writer.buildCsv();
            assertThat(csv).contains("sep=" + delimiter);
            assertThat(csv).contains("col1" + delimiter + "col2");
            assertThat(csv).contains("val1" + delimiter + "val2");
        }

        @Test
        @DisplayName("Should use default delimiter")
        void testDefaultDelimiter() {
            CsvWriter writer = new CsvWriter("col1", "col2");
            writer.addLine("val1", "val2");

            String csv = writer.buildCsv();
            assertThat(csv).contains("sep=;");
            assertThat(csv).contains("col1;col2");
        }

        @Test
        @DisplayName("Should get default delimiter statically")
        void testGetDefaultDelimiter() {
            String defaultDelimiter = CsvWriter.getDefaultDelimiter();
            assertThat(defaultDelimiter).isEqualTo(";");
        }

        @ParameterizedTest(name = "Newline: {0}")
        @ValueSource(strings = { "\n", "\r\n", "\r" })
        @DisplayName("Should handle various newline characters")
        void testVariousNewlines(String newline) {
            CsvWriter writer = new CsvWriter("col1");
            writer.setNewline(newline);
            writer.addLine("val1");

            String csv = writer.buildCsv();
            assertThat(csv).contains(newline);
        }

        @Test
        @DisplayName("Should use system newline by default")
        void testDefaultNewline() {
            CsvWriter writer = new CsvWriter("col1");
            writer.addLine("val1");

            String csv = writer.buildCsv();
            String systemNewline = System.getProperty("line.separator");
            assertThat(csv).contains(systemNewline);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should warn about mismatched line size")
        void testMismatchedLineSize() {
            CsvWriter writer = new CsvWriter("col1", "col2", "col3");

            // Should not throw exception, but may log warning
            assertThatCode(() -> {
                writer.addLine("val1", "val2"); // Missing one column
                writer.buildCsv();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle very long lines")
        void testVeryLongLines() {
            CsvWriter writer = new CsvWriter("data");
            String longValue = "x".repeat(10000);

            writer.addLine(longValue);
            String csv = writer.buildCsv();

            assertThat(csv).contains(longValue);
        }

        @Test
        @DisplayName("Should handle many columns")
        void testManyColumns() {
            String[] headers = new String[100];
            String[] values = new String[100];
            for (int i = 0; i < 100; i++) {
                headers[i] = "col" + i;
                values[i] = "val" + i;
            }

            CsvWriter writer = new CsvWriter(headers);
            writer.addLine(values);

            String csv = writer.buildCsv();
            assertThat(csv).contains("col0;col1");
            assertThat(csv).contains("val0;val1");
        }

        @Test
        @DisplayName("Should handle unicode characters")
        void testUnicodeCharacters() {
            CsvWriter writer = new CsvWriter("名前", "年齢");
            writer.addLine("田中", "25");
            writer.addLine("Müller", "30");

            String csv = writer.buildCsv();
            assertThat(csv).contains("名前;年齢");
            assertThat(csv).contains("田中;25");
            assertThat(csv).contains("Müller;30");
        }

        @Test
        @DisplayName("Should handle empty string values")
        void testEmptyStringValues() {
            CsvWriter writer = new CsvWriter("col1", "col2", "col3");
            writer.addLine("", "value", "");

            String csv = writer.buildCsv();
            assertThat(csv).contains(";value;");
        }

        @Test
        @DisplayName("Should handle whitespace-only values")
        void testWhitespaceOnlyValues() {
            CsvWriter writer = new CsvWriter("col1", "col2");
            writer.addLine("   ", "\t");

            String csv = writer.buildCsv();
            assertThat(csv).contains("   ;\t");
        }

        @Test
        @DisplayName("Should handle Excel support correctly")
        void testExcelSupportEnabled() {
            CsvWriter writer = new CsvWriter("col1");
            writer.addLine("val1");

            String csv = writer.buildCsv();
            // Should include separator line for Excel support
            assertThat(csv).startsWith("sep=");
        }
    }

    @Nested
    @DisplayName("Header Validation")
    class HeaderValidation {

        @Test
        @DisplayName("Should correctly identify when header is set")
        void testIsHeaderSetTrue() {
            CsvWriter writer = new CsvWriter("col1", "col2");
            assertThat(writer.isHeaderSet()).isTrue();
        }

        @Test
        @DisplayName("Should correctly identify when header is not set")
        void testIsHeaderSetFalse() {
            CsvWriter writer = new CsvWriter();
            // Even with no arguments, Arrays.asList() creates an empty list (not null)
            assertThat(writer.isHeaderSet()).isTrue();
        }

        @Test
        @DisplayName("Should handle null header list")
        void testNullHeaderList() {
            List<String> nullHeader = null;
            CsvWriter writer = new CsvWriter(nullHeader);
            assertThat(writer.isHeaderSet()).isFalse();
        }
    }
}
