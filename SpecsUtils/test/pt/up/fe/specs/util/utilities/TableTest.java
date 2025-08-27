package pt.up.fe.specs.util.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link Table} class.
 * Tests 2D table data structure with X and Y keys mapping to values.
 * 
 * @author Generated Tests
 */
class TableTest {

    private Table<String, Integer, String> table;

    @BeforeEach
    void setUp() {
        table = new Table<>();
    }

    @Nested
    @DisplayName("Constructor and Initial State")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty table with empty collections")
        void testEmptyTableCreation() {
            assertThat(table).isNotNull();
            assertThat(table.bimap).isNotNull().isEmpty();
            assertThat(table.yKeys).isNotNull().isEmpty();
            assertThat(table.xSet()).isEmpty();
            assertThat(table.ySet()).isEmpty();
        }

        @Test
        @DisplayName("Should initialize with HashMap and HashSet")
        void testInternalCollectionTypes() {
            assertThat(table.bimap).isInstanceOf(java.util.HashMap.class);
            assertThat(table.yKeys).isInstanceOf(java.util.HashSet.class);
        }
    }

    @Nested
    @DisplayName("Put Operations")
    class PutOperationTests {

        @Test
        @DisplayName("Should store single value correctly")
        void testSinglePut() {
            table.put("row1", 1, "value1");

            assertThat(table.get("row1", 1)).isEqualTo("value1");
            assertThat(table.xSet()).containsExactly("row1");
            assertThat(table.ySet()).containsExactly(1);
        }

        @Test
        @DisplayName("Should handle multiple values in same row")
        void testMultipleValuesInRow() {
            table.put("row1", 1, "value1");
            table.put("row1", 2, "value2");
            table.put("row1", 3, "value3");

            assertThat(table.get("row1", 1)).isEqualTo("value1");
            assertThat(table.get("row1", 2)).isEqualTo("value2");
            assertThat(table.get("row1", 3)).isEqualTo("value3");
            assertThat(table.xSet()).containsExactly("row1");
            assertThat(table.ySet()).containsExactlyInAnyOrder(1, 2, 3);
        }

        @Test
        @DisplayName("Should handle multiple rows")
        void testMultipleRows() {
            table.put("row1", 1, "value1");
            table.put("row2", 1, "value2");
            table.put("row3", 2, "value3");

            assertThat(table.get("row1", 1)).isEqualTo("value1");
            assertThat(table.get("row2", 1)).isEqualTo("value2");
            assertThat(table.get("row3", 2)).isEqualTo("value3");
            assertThat(table.xSet()).containsExactlyInAnyOrder("row1", "row2", "row3");
            assertThat(table.ySet()).containsExactlyInAnyOrder(1, 2);
        }

        @Test
        @DisplayName("Should overwrite existing values")
        void testValueOverwrite() {
            table.put("row1", 1, "original");
            table.put("row1", 1, "updated");

            assertThat(table.get("row1", 1)).isEqualTo("updated");
            assertThat(table.xSet()).containsExactly("row1");
            assertThat(table.ySet()).containsExactly(1);
        }

        @Test
        @DisplayName("Should handle null values")
        void testNullValues() {
            table.put("row1", 1, null);

            assertThat(table.get("row1", 1)).isNull();
            assertThat(table.xSet()).containsExactly("row1");
            assertThat(table.ySet()).containsExactly(1);
        }

        @Test
        @DisplayName("Should handle null keys")
        void testNullKeys() {
            table.put(null, 1, "value1");
            table.put("row1", null, "value2");

            assertThat(table.get(null, 1)).isEqualTo("value1");
            assertThat(table.get("row1", null)).isEqualTo("value2");
            assertThat(table.xSet()).containsExactlyInAnyOrder(null, "row1");
            assertThat(table.ySet()).containsExactlyInAnyOrder(1, null);
        }
    }

    @Nested
    @DisplayName("Get Operations")
    class GetOperationTests {

        @BeforeEach
        void setUpTestData() {
            table.put("row1", 1, "value1");
            table.put("row1", 2, "value2");
            table.put("row2", 1, "value3");
        }

        @Test
        @DisplayName("Should retrieve existing values")
        void testGetExistingValues() {
            assertThat(table.get("row1", 1)).isEqualTo("value1");
            assertThat(table.get("row1", 2)).isEqualTo("value2");
            assertThat(table.get("row2", 1)).isEqualTo("value3");
        }

        @Test
        @DisplayName("Should return null for non-existent X key")
        void testGetNonExistentXKey() {
            assertThat(table.get("nonexistent", 1)).isNull();
        }

        @Test
        @DisplayName("Should return null for non-existent Y key in existing row")
        void testGetNonExistentYKey() {
            assertThat(table.get("row1", 999)).isNull();
        }

        @Test
        @DisplayName("Should return null for completely non-existent coordinates")
        void testGetNonExistentCoordinates() {
            assertThat(table.get("nonexistent", 999)).isNull();
        }
    }

    @Nested
    @DisplayName("Boolean String Operations")
    class BooleanStringTests {

        @BeforeEach
        void setUpTestData() {
            table.put("row1", 1, "value1");
            table.put("row1", 2, "value2");
        }

        @Test
        @DisplayName("Should return 'x' for existing values")
        void testBoolStringForExistingValues() {
            assertThat(table.getBoolString("row1", 1)).isEqualTo("x");
            assertThat(table.getBoolString("row1", 2)).isEqualTo("x");
        }

        @Test
        @DisplayName("Should return '-' for null values")
        void testBoolStringForNullValues() {
            table.put("row1", 3, null);
            assertThat(table.getBoolString("row1", 3)).isEqualTo("-");
        }

        @Test
        @DisplayName("Should return '-' for non-existent coordinates")
        void testBoolStringForNonExistentValues() {
            assertThat(table.getBoolString("nonexistent", 1)).isEqualTo("-");
            assertThat(table.getBoolString("row1", 999)).isEqualTo("-");
        }
    }

    @Nested
    @DisplayName("Key Set Operations")
    class KeySetTests {

        @Test
        @DisplayName("Should start with empty key sets")
        void testEmptyKeySets() {
            assertThat(table.xSet()).isEmpty();
            assertThat(table.ySet()).isEmpty();
        }

        @Test
        @DisplayName("Should track X keys correctly")
        void testXKeyTracking() {
            table.put("row1", 1, "value1");
            table.put("row2", 1, "value2");
            table.put("row3", 2, "value3");

            assertThat(table.xSet()).containsExactlyInAnyOrder("row1", "row2", "row3");
        }

        @Test
        @DisplayName("Should track Y keys correctly")
        void testYKeyTracking() {
            table.put("row1", 1, "value1");
            table.put("row2", 1, "value2");
            table.put("row1", 2, "value3");
            table.put("row1", 3, "value4");

            assertThat(table.ySet()).containsExactlyInAnyOrder(1, 2, 3);
        }

        @Test
        @DisplayName("Should not duplicate keys when overwriting values")
        void testNoDuplicateKeys() {
            table.put("row1", 1, "original");
            table.put("row1", 1, "updated");

            assertThat(table.xSet()).containsExactly("row1");
            assertThat(table.ySet()).containsExactly(1);
        }

        @Test
        @DisplayName("Should handle null keys in sets")
        void testNullKeysInSets() {
            table.put(null, 1, "value1");
            table.put("row1", null, "value2");

            assertThat(table.xSet()).containsExactlyInAnyOrder(null, "row1");
            assertThat(table.ySet()).containsExactlyInAnyOrder(1, null);
        }
    }

    @Nested
    @DisplayName("String Representation")
    class ToStringTests {

        @Test
        @DisplayName("Should handle empty table toString")
        void testEmptyTableToString() {
            String result = table.toString();

            assertThat(result).isNotNull();
            assertThat(result).contains("    \n"); // Header with no Y keys
        }

        @Test
        @DisplayName("Should format table with data correctly")
        void testTableWithDataToString() {
            table.put("row1", 1, "A");
            table.put("row1", 2, "B");
            table.put("row2", 1, "C");
            table.put("row2", 2, "D");

            String result = table.toString();

            assertThat(result).isNotNull();
            assertThat(result).contains("row1");
            assertThat(result).contains("row2");
            assertThat(result).contains("A");
            assertThat(result).contains("B");
            assertThat(result).contains("C");
            assertThat(result).contains("D");
        }

        @Test
        @DisplayName("Should handle null values in toString")
        void testToStringWithNullValues() {
            table.put("row1", 1, null);
            table.put("row1", 2, "value");

            String result = table.toString();

            assertThat(result).isNotNull();
            assertThat(result).contains("null");
            assertThat(result).contains("value");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Type Safety")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should work with different generic types")
        void testDifferentGenericTypes() {
            Table<Integer, String, Boolean> intStringBoolTable = new Table<>();

            intStringBoolTable.put(1, "col1", true);
            intStringBoolTable.put(2, "col2", false);

            assertThat(intStringBoolTable.get(1, "col1")).isTrue();
            assertThat(intStringBoolTable.get(2, "col2")).isFalse();
            assertThat(intStringBoolTable.xSet()).containsExactlyInAnyOrder(1, 2);
            assertThat(intStringBoolTable.ySet()).containsExactlyInAnyOrder("col1", "col2");
        }

        @Test
        @DisplayName("Should maintain data integrity across operations")
        void testDataIntegrity() {
            // Add multiple values
            table.put("A", 1, "value1");
            table.put("A", 2, "value2");
            table.put("B", 1, "value3");
            table.put("B", 2, "value4");

            // Verify all data is accessible
            assertThat(table.get("A", 1)).isEqualTo("value1");
            assertThat(table.get("A", 2)).isEqualTo("value2");
            assertThat(table.get("B", 1)).isEqualTo("value3");
            assertThat(table.get("B", 2)).isEqualTo("value4");

            // Verify sets are complete
            assertThat(table.xSet()).containsExactlyInAnyOrder("A", "B");
            assertThat(table.ySet()).containsExactlyInAnyOrder(1, 2);

            // Overwrite one value
            table.put("A", 1, "updated");
            assertThat(table.get("A", 1)).isEqualTo("updated");

            // Verify other values unchanged
            assertThat(table.get("A", 2)).isEqualTo("value2");
            assertThat(table.get("B", 1)).isEqualTo("value3");
            assertThat(table.get("B", 2)).isEqualTo("value4");
        }

        @Test
        @DisplayName("Should handle large number of entries")
        void testLargeDataSet() {
            // Add many entries
            for (int x = 0; x < 100; x++) {
                for (int y = 0; y < 10; y++) {
                    table.put("row" + x, y, "value_" + x + "_" + y);
                }
            }

            // Verify data integrity
            assertThat(table.xSet()).hasSize(100);
            assertThat(table.ySet()).hasSize(10);

            // Spot check some values
            assertThat(table.get("row0", 0)).isEqualTo("value_0_0");
            assertThat(table.get("row50", 5)).isEqualTo("value_50_5");
            assertThat(table.get("row99", 9)).isEqualTo("value_99_9");
        }
    }
}
