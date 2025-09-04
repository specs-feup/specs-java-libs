package pt.up.fe.specs.util.csv;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for CsvField enumeration.
 * Tests CSV field creation, header generation, and formula building
 * functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("CsvField Tests")
class CsvFieldTest {

    @Nested
    @DisplayName("Field Header Operations")
    class FieldHeaderOperations {

        @Test
        @DisplayName("Should return correct header for AVERAGE field")
        void testAverageFieldHeader() {
            String header = CsvField.AVERAGE.getHeader();
            assertThat(header).isEqualTo("Average");
        }

        @Test
        @DisplayName("Should return correct header for STANDARD_DEVIATION_SAMPLE field")
        void testStandardDeviationSampleFieldHeader() {
            String header = CsvField.STANDARD_DEVIATION_SAMPLE.getHeader();
            assertThat(header).isEqualTo("Std. Dev. (Sample)");
        }

        @ParameterizedTest(name = "Field {0} should have non-null header")
        @EnumSource(CsvField.class)
        @DisplayName("All fields should have non-null headers")
        void testAllFieldsHaveNonNullHeaders(CsvField field) {
            String header = field.getHeader();
            assertThat(header).isNotNull();
            assertThat(header).isNotEmpty();
        }

        @ParameterizedTest(name = "Field {0} should have header without semicolons")
        @EnumSource(CsvField.class)
        @DisplayName("All field headers should not contain semicolons")
        void testFieldHeadersDoNotContainSemicolons(CsvField field) {
            String header = field.getHeader();
            assertThat(header).doesNotContain(";");
        }
    }

    @Nested
    @DisplayName("Field Formula Generation")
    class FieldFormulaGeneration {

        @Test
        @DisplayName("Should generate correct AVERAGE formula for simple range")
        void testAverageFormulaGeneration() {
            String formula = CsvField.AVERAGE.getField("B2:C2");
            assertThat(formula).isEqualTo("=AVERAGE(B2:C2)");
        }

        @Test
        @DisplayName("Should generate correct STANDARD_DEVIATION_SAMPLE formula for simple range")
        void testStandardDeviationSampleFormulaGeneration() {
            String formula = CsvField.STANDARD_DEVIATION_SAMPLE.getField("B2:C2");
            assertThat(formula).isEqualTo("=STDEV.S(B2:C2)");
        }

        @ParameterizedTest(name = "Range: {0}")
        @ValueSource(strings = { "A1:Z1", "B2:D2", "A1:A100", "AA1:ZZ100" })
        @DisplayName("AVERAGE field should handle various ranges correctly")
        void testAverageFieldWithVariousRanges(String range) {
            String formula = CsvField.AVERAGE.getField(range);
            assertThat(formula).isEqualTo("=AVERAGE(" + range + ")");
        }

        @ParameterizedTest(name = "Range: {0}")
        @ValueSource(strings = { "A1:Z1", "B2:D2", "A1:A100", "AA1:ZZ100" })
        @DisplayName("STANDARD_DEVIATION_SAMPLE field should handle various ranges correctly")
        void testStandardDeviationSampleFieldWithVariousRanges(String range) {
            String formula = CsvField.STANDARD_DEVIATION_SAMPLE.getField(range);
            assertThat(formula).isEqualTo("=STDEV.S(" + range + ")");
        }

        @Test
        @DisplayName("Should handle empty range gracefully")
        void testEmptyRange() {
            String emptyRange = "";

            String averageFormula = CsvField.AVERAGE.getField(emptyRange);
            assertThat(averageFormula).isEqualTo("=AVERAGE()");

            String stdDevFormula = CsvField.STANDARD_DEVIATION_SAMPLE.getField(emptyRange);
            assertThat(stdDevFormula).isEqualTo("=STDEV.S()");
        }

        @Test
        @DisplayName("Should handle null range gracefully")
        void testNullRange() {
            assertThatCode(() -> {
                CsvField.AVERAGE.getField(null);
            }).doesNotThrowAnyException();

            assertThatCode(() -> {
                CsvField.STANDARD_DEVIATION_SAMPLE.getField(null);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle special characters in range")
        void testSpecialCharactersInRange() {
            String specialRange = "Sheet1!A1:B2";

            String averageFormula = CsvField.AVERAGE.getField(specialRange);
            assertThat(averageFormula).isEqualTo("=AVERAGE(Sheet1!A1:B2)");

            String stdDevFormula = CsvField.STANDARD_DEVIATION_SAMPLE.getField(specialRange);
            assertThat(stdDevFormula).isEqualTo("=STDEV.S(Sheet1!A1:B2)");
        }
    }

    @Nested
    @DisplayName("Enumeration Properties")
    class EnumerationProperties {

        @Test
        @DisplayName("Should have exactly 2 enumeration values")
        void testEnumerationCount() {
            CsvField[] fields = CsvField.values();
            assertThat(fields).hasSize(2);
        }

        @Test
        @DisplayName("Should contain AVERAGE field")
        void testContainsAverageField() {
            assertThat(CsvField.valueOf("AVERAGE")).isEqualTo(CsvField.AVERAGE);
        }

        @Test
        @DisplayName("Should contain STANDARD_DEVIATION_SAMPLE field")
        void testContainsStandardDeviationSampleField() {
            assertThat(CsvField.valueOf("STANDARD_DEVIATION_SAMPLE"))
                    .isEqualTo(CsvField.STANDARD_DEVIATION_SAMPLE);
        }

        @Test
        @DisplayName("Should throw exception for invalid field name")
        void testInvalidFieldName() {
            assertThatThrownBy(() -> {
                CsvField.valueOf("INVALID_FIELD");
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest(name = "Field {0} should have consistent toString")
        @EnumSource(CsvField.class)
        @DisplayName("All fields should have consistent toString representation")
        void testToStringConsistency(CsvField field) {
            String fieldName = field.toString();
            assertThat(CsvField.valueOf(fieldName)).isEqualTo(field);
        }
    }

    @Nested
    @DisplayName("Integration with Excel Formulas")
    class ExcelFormulaIntegration {

        @Test
        @DisplayName("Should generate Excel-compatible formulas")
        void testExcelCompatibleFormulas() {
            // Test with typical Excel range patterns
            String typicalRange = "B2:E2";

            String averageFormula = CsvField.AVERAGE.getField(typicalRange);
            assertThat(averageFormula)
                    .startsWith("=")
                    .contains("AVERAGE")
                    .contains(typicalRange);

            String stdDevFormula = CsvField.STANDARD_DEVIATION_SAMPLE.getField(typicalRange);
            assertThat(stdDevFormula)
                    .startsWith("=")
                    .contains("STDEV.S")
                    .contains(typicalRange);
        }

        @Test
        @DisplayName("Should handle complex Excel ranges")
        void testComplexExcelRanges() {
            String complexRange = "Data!$B$2:$E$100";

            String averageFormula = CsvField.AVERAGE.getField(complexRange);
            assertThat(averageFormula).isEqualTo("=AVERAGE(Data!$B$2:$E$100)");

            String stdDevFormula = CsvField.STANDARD_DEVIATION_SAMPLE.getField(complexRange);
            assertThat(stdDevFormula).isEqualTo("=STDEV.S(Data!$B$2:$E$100)");
        }

        @Test
        @DisplayName("Should maintain formula syntax consistency")
        void testFormulaSyntaxConsistency() {
            String range = "A1:C1";

            // All formulas should start with '='
            assertThat(CsvField.AVERAGE.getField(range)).startsWith("=");
            assertThat(CsvField.STANDARD_DEVIATION_SAMPLE.getField(range)).startsWith("=");

            // All formulas should contain the range in parentheses
            assertThat(CsvField.AVERAGE.getField(range)).contains("(" + range + ")");
            assertThat(CsvField.STANDARD_DEVIATION_SAMPLE.getField(range)).contains("(" + range + ")");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle very long ranges")
        void testVeryLongRanges() {
            StringBuilder longRange = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longRange.append("A").append(i + 1);
                if (i < 999)
                    longRange.append(",");
            }
            String range = longRange.toString();

            assertThatCode(() -> {
                CsvField.AVERAGE.getField(range);
                CsvField.STANDARD_DEVIATION_SAMPLE.getField(range);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle ranges with whitespace")
        void testRangesWithWhitespace() {
            String rangeWithSpaces = " B2:C2 ";

            String averageFormula = CsvField.AVERAGE.getField(rangeWithSpaces);
            assertThat(averageFormula).isEqualTo("=AVERAGE( B2:C2 )");

            String stdDevFormula = CsvField.STANDARD_DEVIATION_SAMPLE.getField(rangeWithSpaces);
            assertThat(stdDevFormula).isEqualTo("=STDEV.S( B2:C2 )");
        }

        @Test
        @DisplayName("Should handle malformed ranges gracefully")
        void testMalformedRanges() {
            String[] malformedRanges = {
                    ":::",
                    "A1::",
                    "::B2",
                    "A1:B2:C3",
                    "InvalidRange"
            };

            for (String range : malformedRanges) {
                assertThatCode(() -> {
                    CsvField.AVERAGE.getField(range);
                    CsvField.STANDARD_DEVIATION_SAMPLE.getField(range);
                }).doesNotThrowAnyException();
            }
        }
    }
}
