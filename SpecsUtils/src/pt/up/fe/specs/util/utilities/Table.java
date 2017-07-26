/*
 * Copyright 2011 SPeCS Research Group.
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

package pt.up.fe.specs.util.utilities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Each key is associated to a list of values.
 * 
 * @author Joao Bispo
 */
public class Table<X, Y, V> {

    public final Map<X, Map<Y, V>> bimap;
    public final Set<Y> yKeys;

    // private int maxY;
    // private int maxX;

    public Table() {
	this.bimap = new HashMap<>();
	this.yKeys = new HashSet<>();
	// maxY = 0;
	// maxX = 0;
    }

    public void put(X x, Y y, V value) {
	Map<Y, V> yMap = this.bimap.get(x);
	if (yMap == null) {
	    yMap = new HashMap<>();
	    this.bimap.put(x, yMap);
	}

	yMap.put(y, value);
	this.yKeys.add(y);

	// maxX = Math.max(maxX, x + 1);
	// maxY = Math.max(maxY, y + 1);
    }

    public V get(X x, Y y) {
	Map<Y, V> yMap = this.bimap.get(x);
	if (yMap == null) {
	    return null;
	}

	return yMap.get(y);
    }

    public String getBoolString(X x, Y y) {
	V value = get(x, y);
	if (value == null) {
	    return "-";
	}

	return "x";
    }

    public Set<X> xSet() {
	return this.bimap.keySet();
    }

    public Set<Y> ySet() {
	return this.yKeys;
    }

    /*
        public void put(int x, int y, T value) {
    	// Y is the first list
    	List<T> xList = null;
    	if(y < bimap.size()) {
    	    xList = bimap.get(y);
    	}

    	if(xList == null) {
    	    xList = new ArrayList<T>();
    	    bimap.add(y, xList);
    	}

    	xList.add(value);
        }

        public T get(int x, int y) {
    	// Y is the first list
    	List<T> xList = null;
    	if(y < bimap.size()) {
    	    xList = bimap.get(y);
    	}

    	if(xList == null) {
    	    return null;
    	}

    	if(x >= xList.size()) {
    	    return null;
    	}
    	return xList.get(x);
        }
     */
    /*
    @Override
    public String toString() {
    StringBuilder builder = new StringBuilder();

    for (int y = 0; y < maxY; y++) {
        if (maxX > 0) {
    	builder.append(getBoolString(0, y));
        }
        for (int x = 1; x < maxX; x++) {
    	builder.append(getBoolString(x, y));
        }
        builder.append("\n");
    }
    return builder.toString();

    }
     */
    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();

	builder.append("    ");
	for (Y y : ySet()) {
	    builder.append(y).append(" ");
	}
	builder.append("\n");

	for (X x : xSet()) {
	    builder.append(x).append(" ");
	    for (Y y : ySet()) {
		builder.append(this.bimap.get(x).get(y)).append(" ");
	    }
	    builder.append("\n");
	}

	return builder.toString();
    }

}
