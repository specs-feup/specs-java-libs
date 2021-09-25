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

package com.jmatio.types;

import java.nio.ByteBuffer;

public class MLInt16 extends MLNumericArray<Short> {

    public MLInt16(String name, int[] dims, int type, int attributes) {
	super(name, dims, type, attributes);
    }

    public MLInt16(String name, int[] dims) {
	super(name, dims, MLArray.mxINT16_CLASS, 0);
    }

    public MLInt16(String name, Short[] vals, int m) {
	super(name, MLArray.mxINT16_CLASS, vals, m);
    }

    public MLInt16(String name, short[][] vals) {
	this(name, short2DToShort(vals), vals.length);
    }

    public MLInt16(String name, short[] vals, int m) {
	this(name, castToShort(vals), m);
    }

    /**
     * Casts short[] to Short[].
     * 
     * @param vals
     * @return
     */
    private static Short[] castToShort(short[] vals) {

	Short[] dest = new Short[vals.length];
	for (int i = 0; i < vals.length; i++) {
	    dest[i] = (short) vals[i];
	}
	return dest;
    }

    /**
     * Converts short[][] to Short[].
     * 
     * @param vals
     * @return
     */
    private static Short[] short2DToShort(short[][] vals) {

	Short[] s = new Short[vals.length * vals[0].length];

	for (int n = 0; n < vals[0].length; n++) {
	    for (int m = 0; m < vals.length; m++) {
		s[m + n * vals.length] = vals[m][n];
	    }
	}
	return s;
    }

    @Override
    public Short[] createArray(int m, int n) {
	return new Short[m * n];
    }

    @Override
    public int getBytesAllocated() {
	return Short.SIZE >> 3;
    }

    @Override
    public Short buldFromBytes(byte[] bytes) {

	if (bytes.length != getBytesAllocated()) {

	    throw new IllegalArgumentException();
	}

	return ByteBuffer.wrap(bytes).getShort();
    }

    @Override
    public byte[] getByteArray(Short value) {

	int byteAllocated = getBytesAllocated();
	ByteBuffer buff = ByteBuffer.allocate(byteAllocated);

	buff.putShort(value);

	return buff.array();
    }

    @Override
    public Class<?> getStorageClazz() {
	return Short.class;
    }

}
