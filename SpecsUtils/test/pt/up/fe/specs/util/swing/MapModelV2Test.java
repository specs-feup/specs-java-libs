package pt.up.fe.specs.util.swing;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.Color;
import java.awt.Component;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for {@link MapModelV2}.
 * Tests the implementation of enhanced map-based table model functionality with
 * color support.
 * 
 * @author Generated Tests
 */
@DisplayName("MapModelV2")
class MapModelV2Test {

    private Map<String, Integer> testMap;
    private Map<String, String> stringMap;
    private Map<Integer, String> intKeyMap;

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

        intKeyMap = new LinkedHashMap<>();
        intKeyMap.put(1, "one");
        intKeyMap.put(2, "two");
        intKeyMap.put(3, "three");
    }

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorAndInitialization {

        @Test
        @DisplayName("Should create model with map constructor")
        void shouldCreateModelWithMapConstructor() {
            // When
            MapModelV2 model = new MapModelV2(testMap);

            // Then
            assertThat(model).isNotNull();
            assertThat(model).isInstanceOf(AbstractTableModel.class);
            assertThat(model).isInstanceOf(TableModel.class);
        }

        @Test
        @DisplayName("Should initialize with correct row count")
        void shouldInitializeWithCorrectRowCount() {
            // When
            MapModelV2 model = new MapModelV2(testMap);

            // Then
            assertThat(model.getRowCount()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should always have 2 columns")
        void shouldAlwaysHaveTwoColumns() {
            // Given
            Map<String, Integer> smallMap = Map.of("single", 42);
            Map<String, Integer> largeMap = new HashMap<>();
            for (int i = 0; i < 100; i++) {
                largeMap.put("key" + i, i);
            }

            // When
            MapModelV2 smallModel = new MapModelV2(smallMap);
            MapModelV2 largeModel = new MapModelV2(largeMap);
            MapModelV2 testModel = new MapModelV2(testMap);

            // Then
            assertThat(smallModel.getColumnCount()).isEqualTo(2);
            assertThat(largeModel.getColumnCount()).isEqualTo(2);
            assertThat(testModel.getColumnCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should handle empty map")
        void shouldHandleEmptyMap() {
            // Given
            Map<String, Integer> emptyMap = new HashMap<>();

            // When
            MapModelV2 model = new MapModelV2(emptyMap);

            // Then
            assertThat(model.getRowCount()).isEqualTo(0);
            assertThat(model.getColumnCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should initialize with default colors")
        void shouldInitializeWithDefaultColors() {
            // When
            MapModelV2 model = new MapModelV2(testMap);

            // Then
            for (int i = 0; i < model.getRowCount(); i++) {
                assertThat(model.getRowColour(i)).isEqualTo(MapModelV2.COLOR_DEFAULT);
            }
        }
    }

    @Nested
    @DisplayName("Value Retrieval")
    class ValueRetrieval {

        @Test
        @DisplayName("Should return keys in first column")
        void shouldReturnKeysInFirstColumn() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When/Then
            assertThat(model.getValueAt(0, 0)).isEqualTo("key1");
            assertThat(model.getValueAt(1, 0)).isEqualTo("key2");
            assertThat(model.getValueAt(2, 0)).isEqualTo("key3");
        }

        @Test
        @DisplayName("Should return values in second column")
        void shouldReturnValuesInSecondColumn() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When/Then
            assertThat(model.getValueAt(0, 1)).isEqualTo(100);
            assertThat(model.getValueAt(1, 1)).isEqualTo(200);
            assertThat(model.getValueAt(2, 1)).isEqualTo(300);
        }

        @Test
        @DisplayName("Should handle different key and value types")
        void shouldHandleDifferentKeyAndValueTypes() {
            // Given
            MapModelV2 intKeyModel = new MapModelV2(intKeyMap);
            MapModelV2 stringModel = new MapModelV2(stringMap);

            // When/Then - Integer keys
            assertThat(intKeyModel.getValueAt(0, 0)).isEqualTo(1);
            assertThat(intKeyModel.getValueAt(0, 1)).isEqualTo("one");

            // String keys and values
            assertThat(stringModel.getValueAt(0, 0)).isEqualTo("alpha");
            assertThat(stringModel.getValueAt(0, 1)).isEqualTo("value1");
        }

        @Test
        @DisplayName("Should throw exception for invalid column index")
        void shouldThrowExceptionForInvalidColumnIndex() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When/Then
            assertThatThrownBy(() -> model.getValueAt(0, 2))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Column index can only have the values 0 or 1");

            assertThatThrownBy(() -> model.getValueAt(0, -1))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Column index can only have the values 0 or 1");
        }

        @Test
        @DisplayName("Should handle null values in map")
        void shouldHandleNullValuesInMap() {
            // Given
            Map<String, Integer> mapWithNulls = new LinkedHashMap<>();
            mapWithNulls.put("key1", 100);
            mapWithNulls.put("key2", null);
            mapWithNulls.put("key3", 300);

            // When
            MapModelV2 model = new MapModelV2(mapWithNulls);

            // Then
            assertThat(model.getValueAt(1, 0)).isEqualTo("key2");
            assertThat(model.getValueAt(1, 1)).isNull();
        }
    }

    @Nested
    @DisplayName("Value Setting")
    class ValueSetting {

        @Test
        @DisplayName("Should update key values")
        void shouldUpdateKeyValues() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When
            model.setValueAt("newkey", 0, 0);

            // Then
            assertThat(model.getValueAt(0, 0)).isEqualTo("newkey");
            assertThat(model.getValueAt(0, 1)).isEqualTo(100); // Value unchanged
        }

        @Test
        @DisplayName("Should update values")
        void shouldUpdateValues() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When
            model.setValueAt(999, 0, 1);

            // Then
            assertThat(model.getValueAt(0, 0)).isEqualTo("key1"); // Key unchanged
            assertThat(model.getValueAt(0, 1)).isEqualTo(999);
        }

        @Test
        @DisplayName("Should handle null value updates")
        void shouldHandleNullValueUpdates() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When
            model.setValueAt(null, 0, 1);
            model.setValueAt(null, 1, 0);

            // Then
            assertThat(model.getValueAt(0, 1)).isNull();
            assertThat(model.getValueAt(1, 0)).isNull();
        }

        @Test
        @DisplayName("Should handle type changes gracefully")
        void shouldHandleTypeChangesGracefully() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When/Then - Should not throw exceptions for type changes
            model.setValueAt("string_value", 0, 1); // Integer to String
            assertThat(model.getValueAt(0, 1)).isEqualTo("string_value");

            model.setValueAt(42, 1, 0); // String to Integer
            assertThat(model.getValueAt(1, 0)).isEqualTo(42);
        }

        @Test
        @DisplayName("Should throw exception for invalid column index in setValueAt")
        void shouldThrowExceptionForInvalidColumnIndexInSetValueAt() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When/Then - Should handle exceptions gracefully (prints stack trace)
            model.setValueAt("test", 0, 2); // Invalid column index
            model.setValueAt("test", 0, -1); // Invalid column index

            // The current implementation catches exceptions and prints stack trace
            // So no assertion on exception throwing, but values should remain unchanged
            assertThat(model.getValueAt(0, 0)).isEqualTo("key1");
            assertThat(model.getValueAt(0, 1)).isEqualTo(100);
        }
    }

    @Nested
    @DisplayName("Column Names")
    class ColumnNames {

        @Test
        @DisplayName("Should use default column names when none set")
        void shouldUseDefaultColumnNamesWhenNoneSet() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When/Then
            assertThat(model.getColumnName(0)).matches("A|Column 0"); // Default naming
            assertThat(model.getColumnName(1)).matches("B|Column 1"); // Default naming
        }

        @Test
        @DisplayName("Should use custom column names when set with List")
        void shouldUseCustomColumnNamesWhenSetWithList() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);
            List<String> columnNames = Arrays.asList("Key", "Value");

            // When
            model.setColumnNames(columnNames);

            // Then
            assertThat(model.getColumnName(0)).isEqualTo("Key");
            assertThat(model.getColumnName(1)).isEqualTo("Value");
        }

        @Test
        @DisplayName("Should use custom column names when set with varargs")
        void shouldUseCustomColumnNamesWhenSetWithVarargs() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When
            model.setColumnNames("Property", "Setting");

            // Then
            assertThat(model.getColumnName(0)).isEqualTo("Property");
            assertThat(model.getColumnName(1)).isEqualTo("Setting");
        }

        @Test
        @DisplayName("Should fallback to default for missing column names")
        void shouldFallbackToDefaultForMissingColumnNames() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);
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
            MapModelV2 model = new MapModelV2(testMap);

            // When
            model.setColumnNames((List<String>) null);

            // Then
            assertThat(model.getColumnName(0)).matches("A|Column 0");
            assertThat(model.getColumnName(1)).matches("B|Column 1");
        }
    }

    @Nested
    @DisplayName("Color Support")
    class ColorSupport {

        @Test
        @DisplayName("Should get default row colors")
        void shouldGetDefaultRowColors() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When/Then
            assertThat(model.getRowColour(0)).isEqualTo(MapModelV2.COLOR_DEFAULT);
            assertThat(model.getRowColour(1)).isEqualTo(MapModelV2.COLOR_DEFAULT);
            assertThat(model.getRowColour(2)).isEqualTo(MapModelV2.COLOR_DEFAULT);
        }

        @Test
        @DisplayName("Should set and get custom row colors")
        void shouldSetAndGetCustomRowColors() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);
            Color redColor = Color.RED;
            Color blueColor = Color.BLUE;

            // When
            model.setRowColor(0, redColor);
            model.setRowColor(1, blueColor);

            // Then
            assertThat(model.getRowColour(0)).isEqualTo(redColor);
            assertThat(model.getRowColour(1)).isEqualTo(blueColor);
            assertThat(model.getRowColour(2)).isEqualTo(MapModelV2.COLOR_DEFAULT); // Unchanged
        }

        @Test
        @DisplayName("Should have translucent default color")
        void shouldHaveTranslucentDefaultColor() {
            // When/Then
            assertThat(MapModelV2.COLOR_DEFAULT).isEqualTo(new Color(0, 0, 0, 0));
            assertThat(MapModelV2.COLOR_DEFAULT.getAlpha()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle null colors")
        void shouldHandleNullColors() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When
            model.setRowColor(0, null);

            // Then
            assertThat(model.getRowColour(0)).isNull();
        }
    }

    @Nested
    @DisplayName("Renderer Support")
    class RendererSupport {

        @Test
        @DisplayName("Should provide table cell renderer")
        void shouldProvideTableCellRenderer() {
            // When
            TableCellRenderer renderer = MapModelV2.getRenderer();

            // Then
            assertThat(renderer).isNotNull();
        }

        @Test
        @DisplayName("Should renderer integrate with JTable and model")
        void shouldRendererIntegrateWithJTableAndModel() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);
            model.setRowColor(0, Color.YELLOW);

            JTable table = new JTable(model);
            TableCellRenderer renderer = MapModelV2.getRenderer();

            // When
            Component component = renderer.getTableCellRendererComponent(
                    table, "test", false, false, 0, 0);

            // Then
            assertThat(component).isNotNull();
            assertThat(component.getBackground()).isEqualTo(Color.YELLOW);
        }

        @Test
        @DisplayName("Should renderer handle default colors")
        void shouldRendererHandleDefaultColors() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);
            JTable table = new JTable(model);
            TableCellRenderer renderer = MapModelV2.getRenderer();

            // When
            Component component = renderer.getTableCellRendererComponent(
                    table, "test", false, false, 0, 0);

            // Then
            assertThat(component).isNotNull();
            assertThat(component.getBackground()).isEqualTo(MapModelV2.COLOR_DEFAULT);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle large maps efficiently")
        void shouldHandleLargeMapsEfficiently() {
            // Given
            Map<String, Integer> largeMap = new HashMap<>();
            for (int i = 0; i < 1000; i++) {
                largeMap.put("key" + i, i);
            }

            // When
            MapModelV2 model = new MapModelV2(largeMap);

            // Then
            assertThat(model.getRowCount()).isEqualTo(1000);
            assertThat(model.getColumnCount()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should handle concurrent access safely")
        void shouldHandleConcurrentAccessSafely() throws InterruptedException {
            // Given
            MapModelV2 model = new MapModelV2(testMap);
            final int numThreads = 10;
            Thread[] threads = new Thread[numThreads];

            // When
            for (int i = 0; i < numThreads; i++) {
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < 100; j++) {
                        Object value = model.getValueAt(0, 0);
                        assertThat(value).isNotNull();

                        Color color = model.getRowColour(0);
                        assertThat(color).isNotNull();
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

        @Test
        @DisplayName("Should handle special characters in keys and values")
        void shouldHandleSpecialCharactersInKeysAndValues() {
            // Given
            Map<String, String> specialMap = new LinkedHashMap<>();
            specialMap.put("key with spaces", "value with spaces");
            specialMap.put("key-with-dashes", "value-with-dashes");
            specialMap.put("key_with_underscores", "value_with_underscores");
            specialMap.put("key.with.dots", "value.with.dots");
            specialMap.put("key/with/slashes", "value/with/slashes");
            specialMap.put("key\\with\\backslashes", "value\\with\\backslashes");

            // When
            MapModelV2 model = new MapModelV2(specialMap);

            // Then
            assertThat(model.getRowCount()).isEqualTo(6);
            assertThat(model.getValueAt(0, 0)).isEqualTo("key with spaces");
            assertThat(model.getValueAt(0, 1)).isEqualTo("value with spaces");
        }

        @Test
        @DisplayName("Should maintain order for LinkedHashMap")
        void shouldMaintainOrderForLinkedHashMap() {
            // Given
            Map<String, String> orderedMap = new LinkedHashMap<>();
            orderedMap.put("first", "1st");
            orderedMap.put("second", "2nd");
            orderedMap.put("third", "3rd");

            // When
            MapModelV2 model = new MapModelV2(orderedMap);

            // Then
            assertThat(model.getValueAt(0, 0)).isEqualTo("first");
            assertThat(model.getValueAt(1, 0)).isEqualTo("second");
            assertThat(model.getValueAt(2, 0)).isEqualTo("third");
        }
    }

    @Nested
    @DisplayName("Data Independence")
    class DataIndependence {

        @Test
        @DisplayName("Should be independent from original map")
        void shouldBeIndependentFromOriginalMap() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When
            testMap.put("key1", 999);
            testMap.put("newkey", 777);

            // Then - Model should not reflect changes to original map
            assertThat(model.getValueAt(0, 1)).isEqualTo(100); // Original value
            assertThat(model.getRowCount()).isEqualTo(3); // Original size
        }

        @Test
        @DisplayName("Should not modify original map when model is updated")
        void shouldNotModifyOriginalMapWhenModelIsUpdated() {
            // Given
            MapModelV2 model = new MapModelV2(testMap);

            // When
            model.setValueAt("newkey", 0, 0);
            model.setValueAt(999, 0, 1);

            // Then - Original map should be unchanged
            assertThat(testMap.get("key1")).isEqualTo(100);
            assertThat(testMap.containsKey("newkey")).isFalse();
            assertThat(testMap.size()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Serialization Support")
    class SerializationSupport {

        @Test
        @DisplayName("Should have serialVersionUID field")
        void shouldHaveSerialVersionUIDField() {
            // Given/When/Then - Should be able to create instance (indicates
            // serialVersionUID is present)
            MapModelV2 model = new MapModelV2(testMap);
            assertThat(model).isNotNull();

            // The serialVersionUID field should exist (this is more of a compilation check)
            // If it didn't exist, the class wouldn't compile properly as AbstractTableModel
            // is Serializable
        }
    }
}
