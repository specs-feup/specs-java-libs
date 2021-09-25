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

package org.suikasoft.MvelPlus;

import org.mvel2.MVEL;

import pt.up.fe.specs.util.SpecsLogs;

public class MvelSolver {

    /**
     * Uses MVEL to evaluate expressions. If there is an problem, returns null.
     * 
     * @param expression
     * @return
     */
    public static Object eval(String expression) {
	try {
	    return MVEL.eval(expression);
	} catch (Exception e) {
	    return null;
	}
    }

    /**
     * Uses MVEL to evaluate expressions. If there is an problem, returns null.
     * 
     * @param expression
     * @return
     */
    public static String evalToString(String expression) {
	try {
	    return MVEL.evalToString(expression);
	} catch (Exception e) {
	    return null;
	}
    }

    /**
     * Uses MVEL to evaluate the given expression to a Integer. If there is an problem, returns null.
     * 
     * @param expression
     * @return
     */
    public static Integer evaltoInteger(String expression) {

	// Get number
	Number number = evaltoNumber(expression);
	if (number == null) {
	    return null;
	}

	if (number instanceof Double) {
	    SpecsLogs.warn("Given expression resolved to a double (" + number + ")");
	    return null;
	}

	int intValue = number.intValue();

	if ((number.longValue() - intValue) != 0) {
	    SpecsLogs.warn("Loss of precision, evaluated value '" + number.longValue()
		    + "' but returning '" + intValue + "'.");
	    return intValue;
	}

	return intValue;
    }

    /*
    public static Integer evaltoInteger(String expression) {

    // Evaluate expression
    Object returnValue = MvelSolver.eval(expression);

    // Check if there is a return value
    if (returnValue == null) {
        return null;
    }

    // Parse the result as a double
    double resultDouble = Double.parseDouble(returnValue.toString());

    // Cast to integer
    int resultInt = (int) resultDouble;

    // Check if value had
    double diff = resultDouble - (double) resultInt;
    if (diff != 0) {
        LoggingUtils.msgWarn("Given expression resolved to a double (" + resultDouble + ")");
        return null;
    }

    return resultInt;
    }
    */

    /**
     * Uses MVEL to evaluate the given expression to a Number. Returns either a Long or a Double. If there is an
     * problem, returns null.
     * 
     * @param expression
     * @return
     */
    public static Number evaltoNumber(String expression) {

	// Evaluate expression
	Object returnValue = MvelSolver.eval(expression);

	// Check if there is a return value
	if (returnValue == null) {
	    return null;
	}

	// Parse the result as a double
	Double resultDouble = Double.valueOf(returnValue.toString());

	// Cast to integer
	Long resultLong = resultDouble.longValue();

	// Check if value had decimal part
	double diff = resultDouble - (double) resultLong;
	if (diff != 0) {
	    return resultDouble;
	    // LoggingUtils.msgWarn("Given expression resolved to a double (" + resultDouble + ")");
	    // return null;
	}

	return resultLong;
    }
}
