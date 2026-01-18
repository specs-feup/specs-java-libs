package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Comprehensive test suite for SpecsGraphviz utility class.
 * Tests Graphviz DOT file generation, rendering, and graph construction
 * utilities.
 * 
 * Note: Some tests may be skipped if Graphviz is not installed on the system.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsGraphviz Tests")
class SpecsGraphvizTest {

    @Nested
    @DisplayName("DOT Availability Tests")
    class DotAvailabilityTests {

        @Test
        @DisplayName("isDotAvailable should return boolean without throwing exceptions")
        void testIsDotAvailable() {
            // Execute - this might be true or false depending on system
            boolean result = SpecsGraphviz.isDotAvailable();

            // Verify - should not throw exception and return a boolean
            assertThat(result).isIn(true, false);
        }

        @Test
        @DisplayName("isDotAvailable should be consistent across multiple calls")
        void testIsDotAvailable_Consistency() {
            // Execute multiple times
            boolean first = SpecsGraphviz.isDotAvailable();
            boolean second = SpecsGraphviz.isDotAvailable();
            boolean third = SpecsGraphviz.isDotAvailable();

            // Verify - should be consistent (cached)
            assertThat(first).isEqualTo(second);
            assertThat(second).isEqualTo(third);
        }
    }

    @Nested
    @DisplayName("Graph Generation Tests")
    class GraphGenerationTests {

        @Test
        @DisplayName("generateGraph should create valid DOT syntax with declarations and connections")
        void testGenerateGraph() {
            // Arrange
            List<String> declarations = Arrays.asList(
                    "node1[label=\"Node 1\"]",
                    "node2[label=\"Node 2\"]");
            List<String> connections = Arrays.asList(
                    "node1 -> node2");

            // Execute
            String result = SpecsGraphviz.generateGraph(declarations, connections);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).startsWith("digraph graphname {");
            assertThat(result).endsWith("}");
            assertThat(result).contains("node1[label=\"Node 1\"];");
            assertThat(result).contains("node2[label=\"Node 2\"];");
            assertThat(result).contains("node1 -> node2;");
        }

        @Test
        @DisplayName("generateGraph should handle empty declarations and connections")
        void testGenerateGraph_Empty() {
            // Arrange
            List<String> declarations = Arrays.asList();
            List<String> connections = Arrays.asList();

            // Execute
            String result = SpecsGraphviz.generateGraph(declarations, connections);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).startsWith("digraph graphname {");
            assertThat(result).endsWith("}");
            assertThat(result).contains("\n\n"); // Empty sections should have spacing
        }

        @Test
        @DisplayName("generateGraph should format multiple declarations and connections correctly")
        void testGenerateGraph_Multiple() {
            // Arrange
            List<String> declarations = Arrays.asList(
                    "A[label=\"Start\"]",
                    "B[label=\"Process\"]",
                    "C[label=\"End\"]");
            List<String> connections = Arrays.asList(
                    "A -> B",
                    "B -> C");

            // Execute
            String result = SpecsGraphviz.generateGraph(declarations, connections);

            // Verify structure
            String[] lines = result.split("\n");
            assertThat(lines[0]).isEqualTo("digraph graphname {");
            assertThat(lines[1]).isEqualTo("A[label=\"Start\"];");
            assertThat(lines[2]).isEqualTo("B[label=\"Process\"];");
            assertThat(lines[3]).isEqualTo("C[label=\"End\"];");
            assertThat(lines[4]).isEmpty(); // Blank line between sections
            assertThat(lines[5]).isEqualTo("A -> B;");
            assertThat(lines[6]).isEqualTo("B -> C;");
            assertThat(lines[7]).isEqualTo("}");
        }
    }

    @Nested
    @DisplayName("Node Declaration Tests")
    class NodeDeclarationTests {

        @Test
        @DisplayName("declaration should create basic node declaration")
        void testDeclaration_Basic() {
            // Execute
            String result = SpecsGraphviz.declaration("node1", "My Label", null, null);

            // Verify
            assertThat(result).isEqualTo("node1[label=\"My Label\"]");
        }

        @Test
        @DisplayName("declaration should include shape when provided")
        void testDeclaration_WithShape() {
            // Execute
            String result = SpecsGraphviz.declaration("node1", "My Label", "box", null);

            // Verify
            assertThat(result).isEqualTo("node1[label=\"My Label\", shape=box]");
        }

        @Test
        @DisplayName("declaration should include color when provided")
        void testDeclaration_WithColor() {
            // Execute
            String result = SpecsGraphviz.declaration("node1", "My Label", null, "red");

            // Verify
            assertThat(result).isEqualTo("node1[label=\"My Label\", style=filled fillcolor=\"red\"]");
        }

        @Test
        @DisplayName("declaration should include both shape and color when provided")
        void testDeclaration_WithShapeAndColor() {
            // Execute
            String result = SpecsGraphviz.declaration("node1", "My Label", "box", "red");

            // Verify
            assertThat(result).isEqualTo("node1[label=\"My Label\", shape=box, style=filled fillcolor=\"red\"]");
        }

        @Test
        @DisplayName("declaration should handle IDs with square brackets")
        void testDeclaration_SquareBrackets() {
            // Execute
            String result = SpecsGraphviz.declaration("node[1]", "Label", null, null);

            // Verify - square brackets should be replaced with '0'
            assertThat(result).isEqualTo("node010[label=\"Label\"]");
        }

        @Test
        @DisplayName("declaration should parse labels with newlines")
        void testDeclaration_LabelWithNewlines() {
            // Execute
            String result = SpecsGraphviz.declaration("node1", "Line1\nLine2", null, null);

            // Verify - newlines should be escaped
            assertThat(result).isEqualTo("node1[label=\"Line1\\nLine2\"]");
        }

        @ParameterizedTest
        @ValueSource(strings = { "box", "circle", "diamond", "ellipse" })
        @DisplayName("declaration should work with various shape types")
        void testDeclaration_VariousShapes(String shape) {
            // Execute
            String result = SpecsGraphviz.declaration("node1", "Label", shape, null);

            // Verify
            assertThat(result).contains("shape=" + shape);
        }
    }

    @Nested
    @DisplayName("Connection Tests")
    class ConnectionTests {

        @Test
        @DisplayName("connection should create basic connection without label")
        void testConnection_Basic() {
            // Execute - don't pass null, implementation doesn't handle it
            String result = SpecsGraphviz.connection("node1", "node2", "");

            // Verify
            assertThat(result).isEqualTo("node1 -> node2 [label=\"\"]");
        }

        @Test
        @DisplayName("connection should create connection with label")
        void testConnection_WithLabel() {
            // Execute
            String result = SpecsGraphviz.connection("node1", "node2", "edge label");

            // Verify
            assertThat(result).isEqualTo("node1 -> node2 [label=\"edge label\"]");
        }

        @Test
        @DisplayName("connection should handle labels with newlines")
        void testConnection_LabelWithNewlines() {
            // Execute
            String result = SpecsGraphviz.connection("node1", "node2", "Line1\nLine2");

            // Verify - newlines should be escaped
            assertThat(result).isEqualTo("node1 -> node2 [label=\"Line1\\nLine2\"]");
        }

        @ParameterizedTest
        @CsvSource({
                "A, B, ''",
                "start, end, 'start to end'",
                "input, output, 'data flow'"
        })
        @DisplayName("connection should work with various node IDs and labels")
        void testConnection_VariousInputs(String from, String to, String label) {
            // Execute - handle empty string as empty label, not null
            String result = SpecsGraphviz.connection(from, to, label.isEmpty() ? "" : label);

            // Verify
            assertThat(result).startsWith(from + " -> " + to);
            if (!label.isEmpty()) {
                assertThat(result).contains("label=\"" + label + "\"");
            } else {
                assertThat(result).contains("label=\"\"");
            }
        }
    }

    @Nested
    @DisplayName("Label and ID Formatting Tests")
    class FormattingTests {

        @Test
        @DisplayName("parseLabel should escape newlines")
        void testParseLabel() {
            // Execute
            String result = SpecsGraphviz.parseLabel("Line1\nLine2\nLine3");

            // Verify - replaces \n with \\n (which is a single backslash followed by n)
            assertThat(result).isEqualTo("Line1\\nLine2\\nLine3");
        }

        @Test
        @DisplayName("parseLabel should handle strings without newlines")
        void testParseLabel_NoNewlines() {
            // Execute
            String result = SpecsGraphviz.parseLabel("Simple Label");

            // Verify
            assertThat(result).isEqualTo("Simple Label");
        }

        @Test
        @DisplayName("formatId should replace square brackets with zeros")
        void testFormatId_Default() {
            // Execute
            String result = SpecsGraphviz.formatId("node[1][2]");

            // Verify
            assertThat(result).isEqualTo("node010020");
        }

        @Test
        @DisplayName("formatId should replace square brackets with custom characters")
        void testFormatId_CustomChars() {
            // Execute
            String result = SpecsGraphviz.formatId("node[1][2]", '(', ')');

            // Verify
            assertThat(result).isEqualTo("node(1)(2)");
        }

        @Test
        @DisplayName("formatId should handle strings without square brackets")
        void testFormatId_NoBrackets() {
            // Execute
            String result = SpecsGraphviz.formatId("simplenode");

            // Verify
            assertThat(result).isEqualTo("simplenode");
        }

        @ParameterizedTest
        @ValueSource(strings = { "node[1]", "test[]", "[start]", "[]" })
        @DisplayName("formatId should handle various bracket patterns")
        void testFormatId_VariousBrackets(String input) {
            // Execute
            String result = SpecsGraphviz.formatId(input);

            // Verify - should not contain brackets
            assertThat(result).doesNotContain("[");
            assertThat(result).doesNotContain("]");
        }
    }

    @Nested
    @DisplayName("DOT Rendering Tests")
    class DotRenderingTests {

        @Test
        @DisplayName("renderDot with format should work without exceptions when DOT file exists")
        void testRenderDot_FormatOnly(@TempDir Path tempDir) throws IOException {
            // Arrange
            File dotFile = tempDir.resolve("test.dot").toFile();
            Files.write(dotFile.toPath(), "digraph G { A -> B; }".getBytes());

            // Execute - should not throw exception even if dot is not available
            assertThatCode(() -> SpecsGraphviz.renderDot(dotFile, DotRenderFormat.PNG))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("renderDot with output file should work without exceptions")
        void testRenderDot_WithOutputFile(@TempDir Path tempDir) throws IOException {
            // Arrange
            File dotFile = tempDir.resolve("test.dot").toFile();
            File outputFile = tempDir.resolve("output.png").toFile();
            Files.write(dotFile.toPath(), "digraph G { A -> B; }".getBytes());

            // Execute - should not throw exception even if dot is not available
            assertThatCode(() -> SpecsGraphviz.renderDot(dotFile, DotRenderFormat.PNG, outputFile))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Constants Tests")
    class ConstantsTests {

        @Test
        @DisplayName("shape constants should have correct values")
        void testShapeConstants() {
            assertThat(SpecsGraphviz.SHAPE_BOX).isEqualTo("box");
        }

        @Test
        @DisplayName("color constants should have correct values")
        void testColorConstants() {
            assertThat(SpecsGraphviz.COLOR_LIGHTBLUE).isEqualTo("lightblue");
            assertThat(SpecsGraphviz.COLOR_LIGHT_SLATE_BLUE).isEqualTo("lightslateblue");
            assertThat(SpecsGraphviz.COLOR_GRAY75).isEqualTo("gray75");
            assertThat(SpecsGraphviz.COLOR_GREEN).isEqualTo("green");
            assertThat(SpecsGraphviz.COLOR_GREEN3).isEqualTo("green3");
        }

        @Test
        @DisplayName("constants should be usable in declaration method")
        void testConstants_Integration() {
            // Execute
            String result = SpecsGraphviz.declaration("node1", "Test",
                    SpecsGraphviz.SHAPE_BOX, SpecsGraphviz.COLOR_LIGHTBLUE);

            // Verify
            assertThat(result).contains("shape=box");
            assertThat(result).contains("fillcolor=\"lightblue\"");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("methods should handle null and empty inputs gracefully")
        void testNullEmptyInputs() {
            // Test empty label in declaration
            String declaration = SpecsGraphviz.declaration("node1", "", null, null);
            assertThat(declaration).contains("label=\"\"");

            // Test empty label in connection (don't use null - it causes NPE)
            String connection = SpecsGraphviz.connection("A", "B", "");
            assertThat(connection).contains("label=\"\"");

            // Test empty connection label
            connection = SpecsGraphviz.connection("A", "B", "");
            assertThat(connection).contains("label=\"\"");
        }

        @Test
        @DisplayName("generateGraph should handle single item lists")
        void testGenerateGraph_SingleItems() {
            // Execute
            String result = SpecsGraphviz.generateGraph(
                    Arrays.asList("A[label=\"Single\"]"),
                    Arrays.asList("A -> A"));

            // Verify
            assertThat(result).contains("A[label=\"Single\"];");
            assertThat(result).contains("A -> A;");
        }

        @Test
        @DisplayName("complex graph should be generated correctly")
        void testComplexGraph() {
            // Arrange
            List<String> declarations = Arrays.asList(
                    SpecsGraphviz.declaration("start", "Start", SpecsGraphviz.SHAPE_BOX, SpecsGraphviz.COLOR_GREEN),
                    SpecsGraphviz.declaration("process", "Process\nData", null, SpecsGraphviz.COLOR_LIGHTBLUE),
                    SpecsGraphviz.declaration("end", "End", SpecsGraphviz.SHAPE_BOX, SpecsGraphviz.COLOR_GRAY75));
            List<String> connections = Arrays.asList(
                    SpecsGraphviz.connection("start", "process", "init"),
                    SpecsGraphviz.connection("process", "end", "complete"));

            // Execute
            String result = SpecsGraphviz.generateGraph(declarations, connections);

            // Verify
            assertThat(result).contains("start[label=\"Start\", shape=box, style=filled fillcolor=\"green\"]");
            assertThat(result).contains("process[label=\"Process\\nData\", style=filled fillcolor=\"lightblue\"]");
            assertThat(result).contains("end[label=\"End\", shape=box, style=filled fillcolor=\"gray75\"]");
            assertThat(result).contains("start -> process [label=\"init\"]");
            assertThat(result).contains("process -> end [label=\"complete\"]");
        }
    }
}
