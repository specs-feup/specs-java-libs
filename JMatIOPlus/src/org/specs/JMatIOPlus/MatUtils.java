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

package org.specs.JMatIOPlus;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Optional;

import com.jmatio.io.MatFileFilter;
import com.jmatio.io.MatFileReader;
import com.jmatio.types.MLArray;

import pt.up.fe.specs.util.SpecsStrings;

public class MatUtils {

    /**
     * `
     * 
     * @param value
     * @param bitLength
     * @return
     */
    public static String getUnsignedBinary(String value, int bitLength) {
	if (bitLength >= 64) {
	    throw new RuntimeException("Method only works for bit lengths lower than 64");
	}

	Long l = Long.decode(value);

	String binString = Long.toBinaryString(l);

	if (binString.startsWith("1") && binString.length() == bitLength) {
	    l = ~l;
	    l += 1;
	    String invertedString = Long.toBinaryString(l);

	    String byteString = invertedString.substring(invertedString.length() - 8,
		    invertedString.length());
	    binString = "-" + byteString.substring(1, 8);

	}

	return binString;
    }

    public static String getUnsignedBinaryV2(String value, int bitLength) {
	BigInteger bInt = new BigInteger(value);

	String binString = bInt.toString(2);
	if (binString.startsWith("1") && binString.length() == bitLength) {

	    // Convert from two's complement to original binary representation
	    binString = SpecsStrings.invertBinaryString(binString);

	    // Convert back to BigInteger
	    bInt = new BigInteger(binString, 2);
	    // Add 1
	    bInt = bInt.add(BigInteger.ONE);

	    // Convert to binary string and add a '-'
	    binString = bInt.toString(2);
	    binString = "-" + binString;
	}

	return binString;
    }

    /**
     * Checks whether the variable is a scalar.
     * 
     * @param variable
     *            - the scalar
     * @return true is if it a scalar
     */
    public static boolean isScalar(MLArray variable) {

	// The variable is considered a scalar when it is a 1x1 matrix
	int[] dims = variable.getDimensions();

	if (dims.length == 2) {
	    if (dims[0] == 1 && dims[1] == 1) {
		return true;
	    }
	}

	return false;
    }

    /**
     * Reads a variable from a MAT file.
     * 
     * @param matFile
     * @param variableName
     * @return
     */
    public static Optional<MLArray> read(File matFile, String variableName) {
	MatFileFilter filter = new MatFileFilter(new String[] { variableName });

	try {
	    MatFileReader reader = new MatFileReader(matFile, filter);

	    MLArray variable = reader.getMLArray(variableName);

	    if (variable == null) {
		return Optional.empty();
	    }

	    return Optional.of(variable);
	} catch (IOException e) {
	    throw new RuntimeException("Could not read variable '" + variableName + "' from file '" + matFile + "'", e);
	}
    }

}
