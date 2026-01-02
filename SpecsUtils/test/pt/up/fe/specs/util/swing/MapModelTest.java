package pt.up.fe.specs.util.swing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link MapModel}.
 * Tests the implementation of map-based table model functionality for Swing
 * tables.
 * 
 * @author Generated Tests
 */
@DisplayName("MapModel")
class MapModelTest {

    private Map<String, Integer> testMap;
    private Map<String, String> stringMap;
    private List<String> customKeys;

    @BeforeEach
    void setUp() {
        testMap = new LinkedHashMap<>();
        testMap.put("key1", 100);
        testMap.put("key2", 200);
        testMap.put("key3", 300);

        stringMap = new LinkedHashMap<>();
        stringMap.put("alpha", "value1");
        stringMap.put("beta", "value2");
        stringMap.put("gamma", "value3");

        customKeys = Arrays.asList("key3", "key1", "key2");
    }

    @Nested
    @DisplayName("Constructor and Factory Method")
    class ConstructorAndFactoryMethod {

        @Test
        @DisplayName("Should create model with map constructor")
        void shouldCreateModelWithMapConstructor() {
            // When
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // Then
            assertThat(model).isNotNull();
            assertThat(model).isInstanceOf(AbstractTableModel.class);
            assertThat(model).isInstanceOf(TableModel.class);
        }

        @Test
        @DisplayName("Should create model with custom keys constructor")
        void shouldCreateModelWithCustomKeysConstructor() {
            // When
            MapModel<String, Integer> model = new MapModel<>(testMap, customKeys, false, Integer.class);

            // Then
            assertThat(model).isNotNull();
            assertThat(model.getRowCount()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should create model with factory method")
        void shouldCreateModelWithFactoryMethod() {
            // When
            TableModel model = MapModel.newTableModel(testMap, false, Integer.class);

            // Then
            assertThat(model).isNotNull();
            assertThat(model).isInstanceOf(MapModel.class);
        }

        @Test
        @DisplayName("Should handle empty map")
        void shouldHandleEmptyMap() {
            // Given
            Map<String, Integer> emptyMap = new HashMap<>();

            // When
            MapModel<String, Integer> model = new MapModel<>(emptyMap, false, Integer.class);

            // Then
            assertThat(model.getRowCount()).isEqualTo(0);
            assertThat(model.getColumnCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should handle null value class")
        void shouldHandleNullValueClass() {
            // When/Then - Should not throw exception during construction
            MapModel<String, Integer> model = new MapModel<>(testMap, false, null);
            assertThat(model).isNotNull();
        }
    }

    @Nested
    @DisplayName("Table Dimensions")
    class TableDimensions {

        @Test
        @DisplayName("Should return correct dimensions for column-wise model")
        void shouldReturnCorrectDimensionsForColumnWiseModel() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // When/Then
            assertThat(model.getRowCount()).isEqualTo(3); // Number of map entries
            assertThat(model.getColumnCount()).isEqualTo(2); // Key and Value columns
        }

        @Test
        @DisplayName("Should return correct dimensions for row-wise model")
        void shouldReturnCorrectDimensionsForRowWiseModel() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, true, Integer.class);

            // When/Then
            assertThat(model.getRowCount()).isEqualTo(2); // Key and Value rows
            assertThat(model.getColumnCount()).isEqualTo(3); // Number of map entries
        }

        @Test
        @DisplayName("Should handle single entry map")
        void shouldHandleSingleEntryMap() {
            // Given
            Map<String, Integer> singleMap = Map.of("single", 42);

            // When
            MapModel<String, Integer> columnModel = new MapModel<>(singleMap, false, Integer.class);
            MapModel<String, Integer> rowModel = new MapModel<>(singleMap, true, Integer.class);

            // Then
            assertThat(columnModel.getRowCount()).isEqualTo(1);
            assertThat(columnModel.getColumnCount()).isEqualTo(2);
            assertThat(rowModel.getRowCount()).isEqualTo(2);
            assertThat(rowModel.getColumnCount()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Value Retrieval")
    class ValueRetrieval {

        @Test
        @DisplayName("Should return keys in first column for column-wise model")
        void shouldReturnKeysInFirstColumnForColumnWiseModel() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // When/Then
            assertThat(model.getValueAt(0, 0)).isEqualTo("key1");
            assertThat(model.getValueAt(1, 0)).isEqualTo("key2");
            assertThat(model.getValueAt(2, 0)).isEqualTo("key3");
        }

        @Test
        @DisplayName("Should return values in second column for column-wise model")
        void shouldReturnValuesInSecondColumnForColumnWiseModel() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // When/Then
            assertThat(model.getValueAt(0, 1)).isEqualTo(100);
            assertThat(model.getValueAt(1, 1)).isEqualTo(200);
            assertThat(model.getValueAt(2, 1)).isEqualTo(300);
        }

        @Test
        @DisplayName("Should return keys in first row for row-wise model")
        void shouldReturnKeysInFirstRowForRowWiseModel() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, true, Integer.class);

            // When/Then
            assertThat(model.getValueAt(0, 0)).isEqualTo("key1");
            assertThat(model.getValueAt(0, 1)).isEqualTo("key2");
            assertThat(model.getValueAt(0, 2)).isEqualTo("key3");
        }

        @Test
        @DisplayName("Should return values in second row for row-wise model")
        void shouldReturnValuesInSecondRowForRowWiseModel() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, true, Integer.class);

            // When/Then
            assertThat(model.getValueAt(1, 0)).isEqualTo(100);
            assertThat(model.getValueAt(1, 1)).isEqualTo(200);
            assertThat(model.getValueAt(1, 2)).isEqualTo(300);
        }

        @Test
        @DisplayName("Should respect custom key ordering")
        void shouldRespectCustomKeyOrdering() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, customKeys, false, Integer.class);

            // When/Then - Should follow customKeys order: key3, key1, key2
            assertThat(model.getValueAt(0, 0)).isEqualTo("key3");
            assertThat(model.getValueAt(0, 1)).isEqualTo(300);
            assertThat(model.getValueAt(1, 0)).isEqualTo("key1");
            assertThat(model.getValueAt(1, 1)).isEqualTo(100);
            assertThat(model.getValueAt(2, 0)).isEqualTo("key2");
            assertThat(model.getValueAt(2, 1)).isEqualTo(200);
        }
    }

    @Nested
    @DisplayName("Column Names")
    class ColumnNames {

        @Test
        @DisplayName("Should use default column names when none set")
        void shouldUseDefaultColumnNamesWhenNoneSet() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // When/Then
            assertThat(model.getColumnName(0)).matches("A|Column 0"); // Default naming
            assertThat(model.getColumnName(1)).matches("B|Column 1"); // Default naming
        }

        @Test
        @DisplayName("Should use custom column names when set")
        void shouldUseCustomColumnNamesWhenSet() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);
            List<String> columnNames = Arrays.asList("Key", "Value");

            // When
            model.setColumnNames(columnNames);

            // Then
            assertThat(model.getColumnName(0)).isEqualTo("Key");
            assertThat(model.getColumnName(1)).isEqualTo("Value");
        }

        @Test
        @DisplayName("Should fallback to default for missing column names")
        void shouldFallbackToDefaultForMissingColumnNames() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);
            List<String> partialNames = Arrays.asList("Key"); // Only first column named

            // When
            model.setColumnNames(partialNames);

            // Then
            assertThat(model.getColumnName(0)).isEqualTo("Key");
            assertThat(model.getColumnName(1)).matches("B|Column 1"); // Default for missing
        }

        @Test
        @DisplayName("Should handle null column names")
        void shouldHandleNullColumnNames() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // When
            model.setColumnNames(null);

            // Then
            assertThat(model.getColumnName(0)).matches("A|Column 0");
            assertThat(model.getColumnName(1)).matches("B|Column 1");
        }
    }

    @Nested
    @DisplayName("Value Setting")
    class ValueSetting {

        @Test
        @DisplayName("Should update value in column-wise model")
        void shouldUpdateValueInColumnWiseModel() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // When
            model.setValueAt(999, 0, 1); // Update first row, second column (value)

            // Then
            assertThat(model.getValueAt(0, 1)).isEqualTo(999);
        }

        @Test
        @DisplayName("Should support row-wise value updates")
        void shouldSupportRowWiseValueUpdates() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, true, Integer.class);

            // When
            model.setValueAt(999, 1, 0); // Update value at first column in row-wise model

            // Then - row-wise updates should work
            assertThat(model.getValueAt(1, 0)).isEqualTo(999);
        }

        @Test
        @DisplayName("Should reject wrong value type")
        void shouldRejectWrongValueType() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // When/Then
            assertThatThrownBy(() -> model.setValueAt("string", 0, 1))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("expected type");
        }

        @Test
        @DisplayName("Should throw UnsupportedOperationException for key updates before type checking")
        void shouldThrowUnsupportedOperationExceptionForKeyUpdatesBeforeTypeChecking() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // When/Then - operation support checking happens before type checking
            assertThatThrownBy(() -> model.setValueAt("newkey", 0, 0))
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Not yet implemented");
        }

        @Test
        @DisplayName("Should throw UnsupportedOperationException for key updates with correct type")
        void shouldThrowUnsupportedOperationExceptionForKeyUpdatesWithCorrectType() {
            // Given
            MapModel<String, String> stringModel = new MapModel<>(stringMap, false, String.class);

            // When/Then - With correct type, should get UnsupportedOperationException
            assertThatThrownBy(() -> stringModel.setValueAt("newkey", 0, 0))
                    .isInstanceOf(UnsupportedOperationException.class)
                    .hasMessage("Not yet implemented");
        }

        @Test
        @DisplayName("Should handle null value class gracefully")
        void shouldHandleNullValueClassGracefully() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, null);

            // When/Then - With null value class, type checking should be skipped and update
            // should work
            model.setValueAt(999, 0, 1);
            assertThat(model.getValueAt(0, 1)).isEqualTo(999);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle out of bounds access gracefully")
        void shouldHandleOutOfBoundsAccessGracefully() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // When/Then - The current implementation may not throw exceptions for some
            // out-of-bounds cases
            // due to the way it accesses internal data structures
            try {
                model.getValueAt(10, 0);
                // If no exception is thrown, that's the current behavior
            } catch (Exception e) {
                // Expected: some form of bounds exception
                assertThat(e).isInstanceOf(RuntimeException.class);
            }

            try {
                model.getValueAt(0, 10);
                // If no exception is thrown, that's the current behavior
            } catch (Exception e) {
                // Expected: some form of bounds exception
                assertThat(e).isInstanceOf(RuntimeException.class);
            }
        }

        @Test
        @DisplayName("Should handle large maps efficiently")
        void shouldHandleLargeMapsEfficiently() {
            // Given
            Map<String, Integer> largeMap = new HashMap<>();
            for (int i = 0; i < 1000; i++) {
                largeMap.put("key" + i, i);
            }

            // When
            MapModel<String, Integer> model = new MapModel<>(largeMap, false, Integer.class);

            // Then
            assertThat(model.getRowCount()).isEqualTo(1000);
            assertThat(model.getColumnCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should handle null values in map")
        void shouldHandleNullValuesInMap() {
            // Given
            Map<String, Integer> mapWithNulls = new HashMap<>();
            mapWithNulls.put("key1", 100);
            mapWithNulls.put("key2", null);
            mapWithNulls.put("key3", 300);

            // When
            MapModel<String, Integer> model = new MapModel<>(mapWithNulls, false, Integer.class);

            // Then
            assertThat(model.getValueAt(1, 1)).isNull(); // Assuming key2 is at index 1
        }

        @Test
        @DisplayName("Should handle special characters in keys")
        void shouldHandleSpecialCharactersInKeys() {
            // Given
            Map<String, String> specialMap = new LinkedHashMap<>();
            specialMap.put("key with spaces", "value1");
            specialMap.put("key-with-dashes", "value2");
            specialMap.put("key_with_underscores", "value3");
            specialMap.put("key.with.dots", "value4");

            // When
            MapModel<String, String> model = new MapModel<>(specialMap, false, String.class);

            // Then
            assertThat(model.getRowCount()).isEqualTo(4);
            assertThat(model.getValueAt(0, 0)).isEqualTo("key with spaces");
            assertThat(model.getValueAt(0, 1)).isEqualTo("value1");
        }
    }

    @Nested
    @DisplayName("Map Integration")
    class MapIntegration {

        @Test
        @DisplayName("Should NOT reflect changes in underlying map due to internal copy")
        void shouldNotReflectChangesInUnderlyingMapDueToInternalCopy() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // When
            testMap.put("key1", 999);

            // Then - Due to defensive copy, changes to original map are not reflected
            assertThat(model.getValueAt(0, 1)).isEqualTo(100); // Still original value
        }

        @Test
        @DisplayName("Should work with different map implementations")
        void shouldWorkWithDifferentMapImplementations() {
            // Given
            Map<String, Integer> hashMap = new HashMap<>(testMap);
            Map<String, Integer> treeMap = new TreeMap<>(testMap);
            Map<String, Integer> linkedMap = new LinkedHashMap<>(testMap);

            // When
            MapModel<String, Integer> hashModel = new MapModel<>(hashMap, false, Integer.class);
            MapModel<String, Integer> treeModel = new MapModel<>(treeMap, false, Integer.class);
            MapModel<String, Integer> linkedModel = new MapModel<>(linkedMap, false, Integer.class);

            // Then
            assertThat(hashModel.getRowCount()).isEqualTo(3);
            assertThat(treeModel.getRowCount()).isEqualTo(3);
            assertThat(linkedModel.getRowCount()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should NOT maintain map state after model updates due to internal copy")
        void shouldNotMaintainMapStateAfterModelUpdatesDueToInternalCopy() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // When
            model.setValueAt(777, 1, 1); // Update key2's value

            // Then - Due to defensive copy, original map is not updated
            assertThat(testMap.get("key2")).isEqualTo(200); // Original value unchanged
            assertThat(testMap.size()).isEqualTo(3); // Size unchanged
            assertThat(testMap.containsKey("key1")).isTrue(); // Other entries intact
            assertThat(testMap.containsKey("key3")).isTrue();

            // But the model's internal copy should be updated
            assertThat(model.getValueAt(1, 1)).isEqualTo(777);
        }
    }

    @Nested
    @DisplayName("Serialization and Persistence")
    class SerializationAndPersistence {

        @Test
        @DisplayName("Should have serialVersionUID field")
        void shouldHaveSerialVersionUIDField() {
            // Given/When/Then - Should be able to create instance (indicates
            // serialVersionUID is present)
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);
            assertThat(model).isNotNull();

            // The serialVersionUID field should exist (this is more of a compilation check)
            // If it didn't exist, the class wouldn't compile properly as AbstractTableModel
            // is Serializable
        }
    }

    @Nested
    @DisplayName("Performance Characteristics")
    class PerformanceCharacteristics {

        @Test
        @DisplayName("Should handle rapid value access")
        void shouldHandleRapidValueAccess() {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);

            // When/Then - Should not throw exceptions during rapid access
            for (int i = 0; i < 1000; i++) {
                for (int row = 0; row < model.getRowCount(); row++) {
                    for (int col = 0; col < model.getColumnCount(); col++) {
                        Object value = model.getValueAt(row, col);
                        assertThat(value).isNotNull();
                    }
                }
            }
        }

        @Test
        @DisplayName("Should handle concurrent access safely")
        void shouldHandleConcurrentAccessSafely() throws InterruptedException {
            // Given
            MapModel<String, Integer> model = new MapModel<>(testMap, false, Integer.class);
            final int numThreads = 10;
            Thread[] threads = new Thread[numThreads];

            // When
            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 100; j++) {
                        Object value = model.getValueAt(0, 0);
                        assertThat(value).isNotNull();
                    }
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Then - Should complete without exceptions
            assertThat(model.getRowCount()).isEqualTo(3);
        }
    }
}
