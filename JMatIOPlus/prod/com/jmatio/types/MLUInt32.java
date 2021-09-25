/**
 *  Copyright 2012 SPeCS Research Group.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.jmatio.types;

import java.nio.ByteBuffer;

public class MLUInt32 extends MLNumericArray<Integer> {

    public MLUInt32(String name, int[] dims, int type, int attributes) {
	super(name, dims, type, attributes);
    }

    public MLUInt32(String name, int[] dims) {
	super(name, dims, MLArray.mxUINT32_CLASS, 0);
    }

    public MLUInt32(String name, Integer[] vals, int m) {
	super(name, MLArray.mxUINT32_CLASS, vals, m);
    }

    public MLUInt32(String name, int[][] vals) {
	this(name, int2DToInteger(vals), vals.length);
    }

    public MLUInt32(String name, int[] vals, int m) {
	this(name, castToInteger(vals), m);
    }

    /**
     * Casts int[] to Integer[].
     * 
     * @param vals
     * @return
     */
    private static Integer[] castToInteger(int[] vals) {

	Integer[] dest = new Integer[vals.length];
	for (int i = 0; i < vals.length; i++) {
	    dest[i] = (int) vals[i];
	}
	return dest;
    }

    /**
     * Converts int[][] to Integer[].
     * 
     * @param vals
     * @return
     */
    private static Integer[] int2DToInteger(int[][] vals) {

	Integer[] s = new Integer[vals.length * vals[0].length];

	for (int n = 0; n < vals[0].length; n++) {
	    for (int m = 0; m < vals.length; m++) {
		s[m + n * vals.length] = vals[m][n];
	    }
	}
	return s;
    }

    @Override
    public Integer[] createArray(int m, int n) {
	return new Integer[m * n];
    }

    @Override
    public int getBytesAllocated() {
	return Integer.SIZE >> 3;
    }

    @Override
    public Integer buldFromBytes(byte[] bytes) {

	if (bytes.length != getBytesAllocated()) {

	    throw new IllegalArgumentException();
	}

	return ByteBuffer.wrap(bytes).getInt();
    }

    @Override
    public byte[] getByteArray(Integer value) {

	int byteAllocated = getBytesAllocated();
	ByteBuffer buff = ByteBuffer.allocate(byteAllocated);

	buff.putInt(value);

	return buff.array();
    }

    @Override
    public Class<?> getStorageClazz() {
	return Integer.class;
    }

}
