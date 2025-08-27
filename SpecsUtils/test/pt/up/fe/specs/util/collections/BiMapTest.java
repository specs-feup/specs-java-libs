package pt.up.fe.specs.util.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for BiMap 2D coordinate mapping utility.
 * Tests bidimensional key-value mapping functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("BiMap Tests")
class BiMapTest {

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorAndInitialization {

        @Test
        @DisplayName("Should create empty BiMap")
        void testDefaultConstructor() {
            BiMap<String> biMap = new BiMap<>();

            assertThat(biMap).isNotNull();
            assertThat(biMap.bimap).isNotNull();
            assertThat(biMap.bimap).isEmpty();
        }

        @Test
        @DisplayName("Should initialize with zero dimensions")
        void testInitialDimensions() {
            BiMap<String> biMap = new BiMap<>();

            // Should handle queries at (0,0) gracefully
            assertThat(biMap.get(0, 0)).isNull();
        }
    }

    @Nested
    @DisplayName("Basic Operations")
    class BasicOperations {

        @Test
        @DisplayName("Should put and get single value")
        void testPutAndGet() {
            BiMap<String> biMap = new BiMap<>();

            biMap.put(1, 2, "test");

            assertThat(biMap.get(1, 2)).isEqualTo("test");
        }

        @Test
        @DisplayName("Should put values at different coordinates")
        void testPutMultipleValues() {
            BiMap<String> biMap = new BiMap<>();

            biMap.put(0, 0, "origin");
            biMap.put(1, 0, "right");
            biMap.put(0, 1, "up");
            biMap.put(1, 1, "diagonal");

            assertThat(biMap.get(0, 0)).isEqualTo("origin");
            assertThat(biMap.get(1, 0)).isEqualTo("right");
            assertThat(biMap.get(0, 1)).isEqualTo("up");
            assertThat(biMap.get(1, 1)).isEqualTo("diagonal");
        }

        @Test
        @DisplayName("Should return null for non-existent coordinates")
        void testGetNonExistent() {
            BiMap<String> biMap = new BiMap<>();
            biMap.put(1, 1, "test");

            assertThat(biMap.get(0, 0)).isNull();
            assertThat(biMap.get(2, 2)).isNull();
            assertThat(biMap.get(1, 0)).isNull();
            assertThat(biMap.get(0, 1)).isNull();
        }

        @Test
        @DisplayName("Should handle negative coordinates")
        void testNegativeCoordinates() {
            BiMap<String> biMap = new BiMap<>();

            // Note: Implementation uses coordinates as HashMap keys, so negatives should
            // work
            biMap.put(-1, -1, "negative");

            assertThat(biMap.get(-1, -1)).isEqualTo("negative");
            assertThat(biMap.get(0, 0)).isNull();
        }

        @Test
        @DisplayName("Should overwrite existing values")
        void testOverwrite() {
            BiMap<String> biMap = new BiMap<>();

            biMap.put(1, 1, "original");
            biMap.put(1, 1, "updated");

            assertThat(biMap.get(1, 1)).isEqualTo("updated");
        }
    }

    @Nested
    @DisplayName("Boolean String Representation")
    class BooleanStringRepresentation {

        @Test
        @DisplayName("Should return 'x' for existing values")
        void testGetBoolStringExisting() {
            BiMap<String> biMap = new BiMap<>();
            biMap.put(1, 1, "test");

            String result = biMap.getBoolString(1, 1);

            assertThat(result).isEqualTo("x");
        }

        @Test
        @DisplayName("Should return '-' for non-existent values")
        void testGetBoolStringNonExistent() {
            BiMap<String> biMap = new BiMap<>();

            String result = biMap.getBoolString(0, 0);

            assertThat(result).isEqualTo("-");
        }

        @Test
        @DisplayName("Should return 'x' regardless of actual value")
        void testGetBoolStringDifferentValues() {
            BiMap<String> biMap = new BiMap<>();

            biMap.put(0, 0, "string");
            biMap.put(1, 0, "");
            biMap.put(0, 1, "longer string value");

            assertThat(biMap.getBoolString(0, 0)).isEqualTo("x");
            assertThat(biMap.getBoolString(1, 0)).isEqualTo("x");
            assertThat(biMap.getBoolString(0, 1)).isEqualTo("x");
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentation {

        @Test
        @DisplayName("Should create grid representation for empty BiMap")
        void testToStringEmpty() {
            BiMap<String> biMap = new BiMap<>();

            String result = biMap.toString();

            assertThat(result).isEqualTo("");
        }

        @Test
        @DisplayName("Should create grid representation for single value")
        void testToStringSingle() {
            BiMap<String> biMap = new BiMap<>();
            biMap.put(0, 0, "test");

            String result = biMap.toString();

            assertThat(result).isEqualTo("x\n");
        }

        @Test
        @DisplayName("Should create grid representation for multiple values")
        void testToStringMultiple() {
            BiMap<String> biMap = new BiMap<>();
            biMap.put(0, 0, "a");
            biMap.put(1, 0, "b");
            biMap.put(0, 1, "c");
            // (1,1) intentionally left empty

            String result = biMap.toString();

            // Should be a 2x2 grid:
            // Row 0: "xx" (both positions filled)
            // Row 1: "x-" (only first position filled)
            assertThat(result).isEqualTo("xx\nx-\n");
        }

        @Test
        @DisplayName("Should create grid with proper dimensions")
        void testToStringDimensions() {
            BiMap<String> biMap = new BiMap<>();
            biMap.put(2, 1, "test"); // This should create a 3x2 grid

            String result = biMap.toString();

            // Should be 3 columns (x=0,1,2) and 2 rows (y=0,1)
            // Row 0: "---" (no values)
            // Row 1: "--x" (value at x=2, y=1)
            assertThat(result).isEqualTo("---\n--x\n");
        }

        @Test
        @DisplayName("Should handle sparse grids correctly")
        void testToStringSparse() {
            BiMap<String> biMap = new BiMap<>();
            biMap.put(0, 0, "corner");
            biMap.put(3, 2, "far");

            String result = biMap.toString();

            // Should be 4 columns (x=0,1,2,3) and 3 rows (y=0,1,2)
            assertThat(result).isEqualTo("x---\n----\n---x\n");
        }
    }

    @Nested
    @DisplayName("Coordinate Boundary Handling")
    class CoordinateBoundaryHandling {

        @Test
        @DisplayName("Should handle large coordinates")
        void testLargeCoordinates() {
            BiMap<String> biMap = new BiMap<>();

            biMap.put(1000, 500, "large");

            assertThat(biMap.get(1000, 500)).isEqualTo("large");
        }

        @Test
        @DisplayName("Should handle zero coordinates")
        void testZeroCoordinates() {
            BiMap<String> biMap = new BiMap<>();

            biMap.put(0, 0, "origin");

            assertThat(biMap.get(0, 0)).isEqualTo("origin");
            assertThat(biMap.getBoolString(0, 0)).isEqualTo("x");
        }

        @Test
        @DisplayName("Should handle mixed positive and negative coordinates")
        void testMixedCoordinates() {
            BiMap<String> biMap = new BiMap<>();

            biMap.put(-5, 5, "negative-x");
            biMap.put(5, -5, "negative-y");
            biMap.put(-3, -3, "both-negative");
            biMap.put(3, 3, "both-positive");

            assertThat(biMap.get(-5, 5)).isEqualTo("negative-x");
            assertThat(biMap.get(5, -5)).isEqualTo("negative-y");
            assertThat(biMap.get(-3, -3)).isEqualTo("both-negative");
            assertThat(biMap.get(3, 3)).isEqualTo("both-positive");
        }
    }

    @Nested
    @DisplayName("Value Type Handling")
    class ValueTypeHandling {

        @Test
        @DisplayName("Should handle null values")
        void testNullValues() {
            BiMap<String> biMap = new BiMap<>();

            biMap.put(1, 1, null);

            assertThat(biMap.get(1, 1)).isNull();
            // getBoolString should return "-" for null values
            assertThat(biMap.getBoolString(1, 1)).isEqualTo("-");
        }

        @Test
        @DisplayName("Should handle empty strings")
        void testEmptyStrings() {
            BiMap<String> biMap = new BiMap<>();

            biMap.put(1, 1, "");

            assertThat(biMap.get(1, 1)).isEqualTo("");
            // getBoolString should return "x" for empty but non-null strings
            assertThat(biMap.getBoolString(1, 1)).isEqualTo("x");
        }

        @Test
        @DisplayName("Should handle different value types")
        void testDifferentValueTypes() {
            BiMap<Integer> intBiMap = new BiMap<>();
            intBiMap.put(0, 0, 42);
            intBiMap.put(1, 0, 0); // Zero value

            assertThat(intBiMap.get(0, 0)).isEqualTo(42);
            assertThat(intBiMap.get(1, 0)).isEqualTo(0);
            assertThat(intBiMap.getBoolString(0, 0)).isEqualTo("x");
            assertThat(intBiMap.getBoolString(1, 0)).isEqualTo("x"); // Zero is not null
        }

        @Test
        @DisplayName("Should handle complex object types")
        void testComplexObjectTypes() {
            record Point(int x, int y) {
            }

            BiMap<Point> pointBiMap = new BiMap<>();
            Point point1 = new Point(10, 20);
            Point point2 = new Point(30, 40);

            pointBiMap.put(1, 1, point1);
            pointBiMap.put(2, 2, point2);

            assertThat(pointBiMap.get(1, 1)).isEqualTo(point1);
            assertThat(pointBiMap.get(2, 2)).isEqualTo(point2);
        }
    }

    @Nested
    @DisplayName("Grid Behavior and Dimensions")
    class GridBehaviorAndDimensions {

        @Test
        @DisplayName("Should track maximum dimensions correctly")
        void testDimensionTracking() {
            BiMap<String> biMap = new BiMap<>();

            // Start with small grid
            biMap.put(1, 1, "small");
            String result1 = biMap.toString();
            assertThat(result1).isEqualTo("--\n-x\n"); // 2x2 grid

            // Expand grid
            biMap.put(3, 2, "larger");
            String result2 = biMap.toString();
            assertThat(result2).isEqualTo("----\n-x--\n---x\n"); // 4x3 grid
        }

        @Test
        @DisplayName("Should handle non-contiguous coordinates")
        void testNonContiguousCoordinates() {
            BiMap<String> biMap = new BiMap<>();

            biMap.put(0, 0, "first");
            biMap.put(5, 3, "second");

            String result = biMap.toString();

            // Should create 6x4 grid with values only at corners
            assertThat(result.split("\n")).hasSize(4); // 4 rows
            assertThat(result.split("\n")[0]).hasSize(6); // 6 columns
            assertThat(result).startsWith("x-----"); // First row starts with x
            assertThat(result).endsWith("-----x\n"); // Last row ends with x
        }

        @Test
        @DisplayName("Should handle single row grid")
        void testSingleRow() {
            BiMap<String> biMap = new BiMap<>();

            biMap.put(0, 0, "a");
            biMap.put(1, 0, "b");
            biMap.put(2, 0, "c");

            String result = biMap.toString();

            assertThat(result).isEqualTo("xxx\n");
        }

        @Test
        @DisplayName("Should handle single column grid")
        void testSingleColumn() {
            BiMap<String> biMap = new BiMap<>();

            biMap.put(0, 0, "a");
            biMap.put(0, 1, "b");
            biMap.put(0, 2, "c");

            String result = biMap.toString();

            assertThat(result).isEqualTo("x\nx\nx\n");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle very large grids efficiently")
        void testLargeGrid() {
            BiMap<String> biMap = new BiMap<>();

            // Create a sparse large grid
            biMap.put(0, 0, "start");
            biMap.put(100, 50, "end");

            assertThat(biMap.get(0, 0)).isEqualTo("start");
            assertThat(biMap.get(100, 50)).isEqualTo("end");
            assertThat(biMap.get(50, 25)).isNull(); // Middle should be empty
        }

        @Test
        @DisplayName("Should handle coordinate overflows gracefully")
        void testCoordinateOverflow() {
            BiMap<String> biMap = new BiMap<>();

            // Test with maximum integer values
            biMap.put(Integer.MAX_VALUE, Integer.MAX_VALUE, "max");

            assertThat(biMap.get(Integer.MAX_VALUE, Integer.MAX_VALUE)).isEqualTo("max");
        }

        @Test
        @DisplayName("Should handle rapid succession of puts and gets")
        void testRapidOperations() {
            BiMap<String> biMap = new BiMap<>();

            // Perform many operations quickly
            for (int i = 0; i < 100; i++) {
                biMap.put(i % 10, i / 10, "value" + i);
            }

            // Verify some values
            assertThat(biMap.get(0, 0)).isEqualTo("value0");
            assertThat(biMap.get(5, 5)).isEqualTo("value55");
            assertThat(biMap.get(9, 9)).isEqualTo("value99");
        }

        @Test
        @DisplayName("Should handle toString for very sparse grids")
        void testToStringVerySparge() {
            BiMap<String> biMap = new BiMap<>();

            // Create a very sparse grid that could be memory intensive
            biMap.put(0, 0, "start");
            biMap.put(10, 10, "end");

            String result = biMap.toString();

            // Should create 11x11 grid with only two 'x' characters
            assertThat(result.split("\n")).hasSize(11);
            long xCount = result.chars().filter(ch -> ch == 'x').count();
            assertThat(xCount).isEqualTo(2);

            long dashCount = result.chars().filter(ch -> ch == '-').count();
            assertThat(dashCount).isEqualTo(119); // 11*11 - 2 = 119
        }
    }

    @Nested
    @DisplayName("Integration and Workflow Tests")
    class IntegrationAndWorkflowTests {

        @Test
        @DisplayName("Should support typical matrix-like usage")
        void testMatrixUsage() {
            BiMap<Double> matrix = new BiMap<>();

            // Fill a 3x3 matrix
            for (int x = 0; x < 3; x++) {
                for (int y = 0; y < 3; y++) {
                    matrix.put(x, y, (double) (x * 3 + y));
                }
            }

            // Verify values
            assertThat(matrix.get(0, 0)).isEqualTo(0.0);
            assertThat(matrix.get(1, 1)).isEqualTo(4.0);
            assertThat(matrix.get(2, 2)).isEqualTo(8.0);

            // Verify toString creates proper grid
            String result = matrix.toString();
            assertThat(result).isEqualTo("xxx\nxxx\nxxx\n");
        }

        @Test
        @DisplayName("Should support game board-like usage")
        void testGameBoardUsage() {
            BiMap<String> gameBoard = new BiMap<>();

            // Place some game pieces
            gameBoard.put(0, 0, "Rook");
            gameBoard.put(7, 0, "Rook");
            gameBoard.put(3, 0, "Queen");
            gameBoard.put(4, 0, "King");

            // Verify pieces
            assertThat(gameBoard.get(0, 0)).isEqualTo("Rook");
            assertThat(gameBoard.get(7, 0)).isEqualTo("Rook");
            assertThat(gameBoard.get(3, 0)).isEqualTo("Queen");
            assertThat(gameBoard.get(4, 0)).isEqualTo("King");

            // Empty squares
            assertThat(gameBoard.get(1, 0)).isNull();
            assertThat(gameBoard.get(0, 1)).isNull();

            // Boolean representation shows occupied squares
            assertThat(gameBoard.getBoolString(0, 0)).isEqualTo("x");
            assertThat(gameBoard.getBoolString(1, 0)).isEqualTo("-");
        }

        @Test
        @DisplayName("Should support coordinate transformation workflows")
        void testCoordinateTransformation() {
            BiMap<String> biMap = new BiMap<>();

            // Add values in one coordinate system
            biMap.put(1, 1, "center");
            biMap.put(0, 1, "left");
            biMap.put(2, 1, "right");
            biMap.put(1, 0, "up");
            biMap.put(1, 2, "down");

            // Access using transformed coordinates (e.g., offset by -1,-1)
            int offsetX = 1, offsetY = 1;
            assertThat(biMap.get(0 + offsetX, 0 + offsetY)).isEqualTo("center");
            assertThat(biMap.get(-1 + offsetX, 0 + offsetY)).isEqualTo("left");
            assertThat(biMap.get(1 + offsetX, 0 + offsetY)).isEqualTo("right");
            assertThat(biMap.get(0 + offsetX, -1 + offsetY)).isEqualTo("up");
            assertThat(biMap.get(0 + offsetX, 1 + offsetY)).isEqualTo("down");
        }
    }
}
