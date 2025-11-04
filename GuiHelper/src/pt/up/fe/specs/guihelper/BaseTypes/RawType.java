/*
 * Copyright 2010 SPeCS Research Group.
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

package pt.up.fe.specs.guihelper.BaseTypes;

import pt.up.fe.specs.util.utilities.StringList;

/**
 * Types of objects supported internally by SetupData.
 *
 * @author Joao Bispo
 */
public enum RawType {

    String(String.class),
    ListOfStrings(StringList.class),
    Setup(SetupData.class),
    MapOfSetups(ListOfSetups.class);
    // Integer(Integer.class),
    // Integer(String.class),
    // Boolean(Boolean.class);
    // Double(Double.class);
    // Double(String.class);

    private RawType(Class<?> rawClass) {
	this.rawClass = rawClass;
    }

    public Class<?> getRawClass() {
	return rawClass;
    }

    /**
     * INSTANCE VARIABLES
     */
    private final Class<?> rawClass;

    // PROPERTIES
    // public final static Boolean EMPTY_VALUE_BOOLEAN = java.lang.Boolean.FALSE;
    private final static String EMPTY_VALUE_BOOLEAN = java.lang.Boolean.FALSE.toString();
    // public final static Integer EMPTY_VALUE_INTEGER = 0;
    private final static String EMPTY_VALUE_INTEGER = "0";
    private final static String EMPTY_VALUE_LONG = "0";
    private final static String EMPTY_VALUE_STRING = "";
    private final static String EMPTY_VALUE_FILE = "./";
    // public final static Double EMPTY_VALUE_DOUBLE = 0.0;
    private final static String EMPTY_VALUE_DOUBLE = "0.0";

    public static String getEmptyValueFile() {
	return RawType.EMPTY_VALUE_FILE;
    }

    public static String getEmptyValueBoolean() {
	return RawType.EMPTY_VALUE_BOOLEAN;
    }

    public static String getEmptyValueDouble() {
	return RawType.EMPTY_VALUE_DOUBLE;
    }

    public static String getEmptyValueInteger() {
	return RawType.EMPTY_VALUE_INTEGER;
    }

    public static String getEmptyValueLong() {
	return RawType.EMPTY_VALUE_LONG;
    }

    public static String getEmptyValueString() {
	return RawType.EMPTY_VALUE_STRING;
    }
}
