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

package pt.up.fe.specs.util.collections;

import java.util.HashMap;
import java.util.Map;

/**
 * Each key is associated to a list of values.
 * 
 * @author Joao Bispo
 */
public class BiMap<T> {

    public final Map<Integer, Map<Integer, T>> bimap;
    private int maxY;
    private int maxX;

    public BiMap() {
	this.bimap = new HashMap<>();
	this.maxY = 0;
	this.maxX = 0;
    }

    public void put(int x, int y, T value) {
	Map<Integer, T> yMap = this.bimap.get(x);
	if (yMap == null) {
	    yMap = new HashMap<>();
	    this.bimap.put(x, yMap);
	}

	yMap.put(y, value);

	this.maxX = Math.max(this.maxX, x + 1);
	this.maxY = Math.max(this.maxY, y + 1);
    }

    public T get(int x, int y) {
	Map<Integer, T> yMap = this.bimap.get(x);
	if (yMap == null) {
	    return null;
	}

	return yMap.get(y);
    }

    public String getBoolString(int x, int y) {
	T value = get(x, y);
	if (value == null) {
	    return "-";
	}

	return "x";
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
    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();

	for (int y = 0; y < this.maxY; y++) {
	    if (this.maxX > 0) {
		builder.append(getBoolString(0, y));
	    }
	    for (int x = 1; x < this.maxX; x++) {
		builder.append(getBoolString(x, y));
	    }
	    builder.append("\n");
	}
	return builder.toString();

    }

}
