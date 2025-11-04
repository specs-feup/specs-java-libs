/**
 * Copyright 2012 SPeCS Research Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.swing;

import java.awt.Color;
import java.awt.Component;
import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

/**
 * @author Joao Bispo
 *
 */
public class MapModelV2 extends AbstractTableModel {

    public static final Color COLOR_DEFAULT = new Color(0, 0, 0, 0);

    @Serial
    private static final long serialVersionUID = 1L;

    private final List<Object> keys;
    private final List<Object> values;
    private final List<Color> rowColors;

    private List<String> columnNames;

    public MapModelV2(Map<?, ?> map) {
        this.keys = new ArrayList<>();
        this.values = new ArrayList<>();
        this.rowColors = new ArrayList<>();

        this.columnNames = null;

        // Initialize keys and values
        for (Object key : map.keySet()) {
            Object value = map.get(key);

            this.keys.add(key);
            this.values.add(value);
        }

        // Set default color to translucent
        for (int i = 0; i < this.keys.size(); i++) {
            this.rowColors.add(MapModelV2.COLOR_DEFAULT);
        }
    }

    public static TableCellRenderer getRenderer() {
        return new DefaultTableCellRenderer() {

            @Serial
            private static final long serialVersionUID = -2074238717877716002L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                MapModelV2 model = (MapModelV2) table.getModel();
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus,
                        row, column);
                c.setBackground(model.getRowColour(row));
                return c;
            }

        };
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
        return this.keys.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
        return 2;
    }

    public Color getRowColour(int row) {
        return this.rowColors.get(row);
    }

    public void setRowColor(int row, Color c) {
        this.rowColors.set(row, c);
        fireTableRowsUpdated(row, row);
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return this.keys.get(rowIndex);
        }

        if (columnIndex == 1) {
            return this.values.get(rowIndex);
        }

        throw new RuntimeException("Column index can only have the values 0 or 1");

    }

    /**
     * Helper method with variadic inputs.
     *
     */
    public void setColumnNames(String... columnNames) {
        setColumnNames(Arrays.asList(columnNames));
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    @Override
    public String getColumnName(int column) {

        if (this.columnNames == null) {
            return super.getColumnName(column);
        }

        if (column >= this.columnNames.size()) {
            return super.getColumnName(column);
        }

        return this.columnNames.get(column);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            updateValue(aValue, rowIndex, columnIndex);
            fireTableCellUpdated(rowIndex, columnIndex);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateValue(Object aValue, int rowIndex, int columnIndex) {

        // If column index is 0, set key
        if (columnIndex == 0) {
            this.keys.set(rowIndex, aValue);
            return;
        }

        // If column index is 1, set value
        if (columnIndex == 1) {
            this.values.set(rowIndex, aValue);
            return;
        }

        throw new RuntimeException("Column index can only have the values 0 or 1");

    }
}
