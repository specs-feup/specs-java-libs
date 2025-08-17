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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.swing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/**
 * @author Joao Bispo
 * 
 */
public class MapModel<K extends Comparable<? super K>, V> extends AbstractTableModel {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private final Map<K, V> map;
    private final boolean rowWise;
    private final Class<V> valueClass;

    private final List<K> keys;

    private List<String> columnNames;

    /**
     * @param map
     */
    public MapModel(Map<K, V> map, boolean rowWise, Class<V> valueClass) {
	this(map, new ArrayList<>(map.keySet()), rowWise, valueClass);
    }

    public MapModel(Map<K, V> map, List<K> keys, boolean rowWise, Class<V> valueClass) {
	this.map = map == null ? Collections.emptyMap() : new HashMap<>(map);

	this.rowWise = rowWise;

	this.keys = keys;
	this.valueClass = valueClass;

	this.columnNames = null;
	// keys.addAll(map.keySet());
	// Collections.sort(keys);
    }

    public static <K extends Comparable<? super K>, V> TableModel newTableModel(Map<K, V> map,
	    boolean rowWise, Class<V> valueClass) {

	return new MapModel<>(map, rowWise, valueClass);

    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
	if (this.rowWise) {
	    return 2;
	}

	return this.map.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
	if (this.rowWise) {
	    return this.map.size();
	}

	return 2;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
	int shortIndex, longIndex;
	if (this.rowWise) {
	    shortIndex = rowIndex;
	    longIndex = columnIndex;
	} else {
	    shortIndex = columnIndex;
	    longIndex = rowIndex;
	}

	// Key
	if (shortIndex == 0) {
	    return this.keys.get(longIndex);
	}

	return this.map.get(this.keys.get(longIndex));
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    /*
    public K getKeyAt(int rowIndex, int columnIndex) {
    int shortIndex, longIndex;
    if (rowWise) {
        shortIndex = rowIndex;
        longIndex = columnIndex;
    } else {
        shortIndex = columnIndex;
        longIndex = rowIndex;
    }

    // Key
    if (shortIndex == 0) {
        return keys.get(longIndex);
    } else {
        return keys.get(longIndex);
    }
    }
     */

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

    // @SuppressWarnings("unchecked") // Method must accept Object, cannot to check
    @SuppressWarnings("unchecked")
    // It is being checked using valueClass
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	if (!this.valueClass.isInstance(aValue)) {
	    throw new RuntimeException("Gave an object to type '" + aValue.getClass().getName() + "', expected type '"
		    + this.valueClass.getName() + "' ");
	}

	updateValue((V) aValue, rowIndex, columnIndex);
	fireTableCellUpdated(rowIndex, columnIndex);
    }

    private void updateValue(V aValue, int rowIndex, int columnIndex) {
	if (!this.rowWise) {
	    // If column index is 0, set key
	    if (columnIndex == 0) {
		throw new UnsupportedOperationException("Not yet implemented");
	    }

	    // If column index is 1, set value
	    if (columnIndex == 1) {

		K key = this.keys.get(rowIndex);
		System.out.println("ROW INDEX:" + rowIndex);
		System.out.println("KEY:" + key);
		this.map.put(key, aValue);
		return;
	    }

	} else {
	    // If row index is 0, set key
	    if (rowIndex == 0) {
		throw new UnsupportedOperationException("Not yet implemented");
	    }

	    // If row index is 1, set value
	    if (rowIndex == 1) {
		throw new UnsupportedOperationException("Not yet implemented");
	    }

	    throw new RuntimeException("Unsupported column index:" + columnIndex);
	}

    }
}
