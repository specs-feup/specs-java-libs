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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.guihelper.BaseTypes;

import java.io.Serializable;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Represents the value of a field, of a determined FieldType.
 * 
 * <p>
 * When reading a value from this object, it is not needed to check if the type agrees when casting. The objects stored
 * by this class always agree with the type of the FieldValue. If the object given as value when an FieldValue is
 * created does not agree with its type, the FieldValue object is not created.
 * 
 * <p>
 * Stores current value and type of a field.
 * 
 * @author Joao Bispo
 */
public class FieldValue implements Serializable {

    private FieldValue(Object value, FieldType type) {
	this.rawValue = value;
	this.type = type;
    }

    /**
     * Convenience method that accepts a SetupFieldEnum.
     * 
     * @param value
     * @param field
     * @return
     */
    public static FieldValue create(Object value, SetupFieldEnum field) {
	return create(value, field.getType());
    }

    /**
     * Creates a new Value object. Verifies if the given object is compatible with the given type.
     * 
     * @param value
     * @param type
     * @return a new Value object, or null if the object could not be created
     */
    public static FieldValue create(Object value, FieldType type) {
	value = parseValue(value, type);

	if (value == null) {
	    SpecsLogs.msgWarn("Null values not supported.");
	    return null;
	}

	// Verify if object class is compatible with the given type
	boolean success = testFieldValue(value, type);
	if (!success) {
	    SpecsLogs.msgWarn("'" + value.getClass() + "' of given object is not compatible with FieldType '"
		    + type + "'." + " Should be '" + type.getRawType().getRawClass() + "'");
	    // (new Exception()).printStackTrace();
	    return null;
	}

	// Create Value object
	FieldValue newValue = new FieldValue(value, type);
	return newValue;
    }

    /**
     * @param value
     * @param type2
     * @return
     */
    private static Object parseValue(Object value, FieldType type) {
	if (type == FieldType.bool) {
	    return value.toString();
	}

	return value;
    }

    /**
     * @param value
     * @return true if the given object is compatible with the given FieldType.
     */
    public static boolean testFieldValue(Object value, FieldType type) {
	Class<?> typeClass = type.getRawType().getRawClass();
	return typeClass.isInstance(value);
    }

    public FieldType getType() {
	return type;
    }

    @Override
    public String toString() {
	return rawValue.toString() + "(" + type.name() + ")";
    }

    public Object getRawValue() {
	return rawValue;
    }

    private Object rawValue;
    private FieldType type;

    // Variable needed for serialization.
    private static final long serialVersionUID = 1;
}
