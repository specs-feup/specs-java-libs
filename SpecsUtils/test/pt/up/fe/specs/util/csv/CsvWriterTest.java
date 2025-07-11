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

/**
 * Test suite for CsvWriter utility class.
 * 
 * This test class covers CSV writing functionality including:
 * - CSV field creation and management
 * - Line addition and formatting
 * - Formula field integration (averages, standard deviation)
 * - Custom separators and newlines
 */
@DisplayName("CsvWriter Tests")
public class CsvWriterTest {

    @Nested
    @DisplayName("CSV Building Operations")
    class CsvBuildingOperations {

        @Test
        @DisplayName("buildCsv should generate correct CSV with formulas")
        void testCsvWriter_WithFormulas_GeneratesCorrectCsv() {
            CsvWriter csvWriter = new CsvWriter("name", "1", "2");
            csvWriter.setNewline("\n");

            csvWriter.addField(CsvField.AVERAGE, CsvField.STANDARD_DEVIATION_SAMPLE);
            csvWriter.addLine("line1", "4", "7");

            String expectedCsv = "sep=;\n" +
                    "name;1;2;Average;Std. Dev. (Sample)\n" +
                    "line1;4;7;=AVERAGE(B2:C2);=STDEV.S(B2:C2)\n";

            assertThat(csvWriter.buildCsv()).isEqualTo(expectedCsv);
        }

        @Test
        @DisplayName("CsvWriter should throw exception for empty initialization")
        void testCsvWriter_EmptyInitialization_ShouldThrowException() {
            assertThatThrownBy(() -> {
                CsvWriter csvWriter = new CsvWriter();
                csvWriter.buildCsv();
            }).isInstanceOf(ArrayIndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("CsvWriter should handle multiple lines")
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
        @DisplayName("CsvWriter should handle null values gracefully")
        void testCsvWriter_NullValues_ShouldHandleGracefully() {
            assertThatCode(() -> {
                CsvWriter csvWriter = new CsvWriter("col1", "col2");
                csvWriter.addLine(null, "value2");
                csvWriter.buildCsv();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("CsvWriter should handle custom newlines")
        void testCsvWriter_CustomNewlines_ShouldWork() {
            CsvWriter csvWriter = new CsvWriter("col1");
            csvWriter.setNewline("\r\n");
            csvWriter.addLine("value1");

            String result = csvWriter.buildCsv();
            assertThat(result).contains("\r\n");
        }

        @Test
        @DisplayName("CsvWriter should handle special characters")
        void testCsvWriter_SpecialCharacters_ShouldWork() {
            CsvWriter csvWriter = new CsvWriter("column with spaces", "column;with;semicolons");
            csvWriter.setNewline("\n");
            csvWriter.addLine("value with spaces", "value;with;semicolons");

            assertThatCode(() -> {
                csvWriter.buildCsv();
            }).doesNotThrowAnyException();
        }
    }

}
