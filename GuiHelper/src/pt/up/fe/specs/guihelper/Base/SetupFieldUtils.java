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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.guihelper.Base;

import java.util.Arrays;
import java.util.Collection;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.ListOfSetups;
import pt.up.fe.specs.guihelper.BaseTypes.RawType;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.guihelper.SetupFieldOptions.DefaultValue;
import pt.up.fe.specs.guihelper.SetupFieldOptions.MultipleChoice;
import pt.up.fe.specs.guihelper.SetupFieldOptions.MultipleSetup;
import pt.up.fe.specs.guihelper.SetupFieldOptions.SingleSetup;
import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.utilities.StringList;

/**
 * Helper methods related to SetupFields.
 *
 * @author Joao Bispo
 */
public class SetupFieldUtils {

    /**
     * Generates a new Value object. If the given key supports default values, returns the default value. Otherwise,
     * will return what is defined as the empty value.
     *
     * <p>
     * To bypass the default values, use the method newBlankValue.
     *
     * @param key
     * @return
     */
    public static FieldValue newValue(SetupFieldEnum key) {

	// Get default value
	FieldValue newValue = SetupFieldUtils.getDefaultValue(key);
	if (newValue == null) {
	    // Get empty raw object
	    newValue = newBlankValue(key);
	}

	return newValue;
    }

    /**
     * The blank value for a certain SetupFieldEnum.
     *
     * @param key
     * @return
     */
    public static FieldValue newBlankValue(SetupFieldEnum key) {
	FieldType valueType = key.getType();

	Object rawData = null;
	switch (valueType) {
	case bool:
	    rawData = RawType.getEmptyValueBoolean();
	    break;
	case integer:
	    rawData = RawType.getEmptyValueInteger();
	    break;
	case doublefloat:
	    rawData = RawType.getEmptyValueDouble();
	    break;
	case setup:
	    rawData = SetupData.create(SetupFieldUtils.getSingleSetup(key).getSetupOptions());
	    break;
	case integratedSetup:
	    rawData = SetupData.create(SetupFieldUtils.getSingleSetup(key).getSetupOptions());
	    break;
	case string:
	    rawData = RawType.getEmptyValueString();
	    break;
	case folder:
	    rawData = RawType.getEmptyValueFile();
	    break;
	case stringList:
	    rawData = new StringList();
	    break;
	case setupList:
	    rawData = ListOfSetups.create(SetupFieldUtils.getMultipleSetup(key).getSetups());
	    break;
	case multipleChoice:
	    rawData = RawType.getEmptyValueString();
	    break;
	case multipleChoiceSetup:
	    rawData = ListOfSetups.create(SetupFieldUtils.getMultipleSetup(key).getSetups());
	    break;
	case multipleChoiceStringList:
	    rawData = new StringList();
	    break;
	default:
	    SpecsLogs.warn("Case '" + valueType + "' not defined.");
	    return null;
	}

	return FieldValue.create(rawData, valueType);
    }

    /**
     * Extracts a list of SetupFields inside the given class. The class must represent an enum which implements the
     * SetupFieldEnum interface.
     *
     * @param enumKey
     * @return a list with the enumerations (EnumKeys) found inside an SetupFieldEnum class, or null if the given class
     *         does not represent an enum or the interface SetupFieldEnum
     */
    // public static Collection<SetupFieldEnum> getSetupFields(Class<?> enumSetupField) {
    public static <E extends Enum<E>> Collection<SetupFieldEnum> getSetupFields(Class<E> enumSetupField) {
	// Check if implements SetupFieldEnum interface
	Object setupFieldInstance = SpecsEnums.getInterfaceFromEnum(enumSetupField, SetupFieldEnum.class);
	if (setupFieldInstance == null) {
	    SpecsLogs.warn("Could not get SetupField instance.");
	    return null;
	}

	// Get enums
	SetupFieldEnum[] enumKeys = (SetupFieldEnum[]) enumSetupField.getEnumConstants();
	return Arrays.asList(enumKeys);
    }

    /**
     * If the SetupFieldEnum defines a default value, returns that value. Otherwise, returns null.
     *
     * @param key
     * @return
     */
    public static FieldValue getDefaultValue(SetupFieldEnum key) {
	if (!DefaultValue.class.isInstance(key)) {
	    return null;
	}

	DefaultValue defaultValue = (DefaultValue) key;

	return defaultValue.getDefaultValue();
    }

    /**
     * Casts a SetupFieldEnum to a SingleSetup.
     *
     * @param enumOption
     * @return
     */
    public static SingleSetup getSingleSetup(SetupFieldEnum enumOption) {
	// Try casting SetupFieldEnum to AppOptionEnumSetup
	SingleSetup choices = null;
	try {
	    choices = (SingleSetup) enumOption;
	} catch (ClassCastException ex) {
	    SpecsLogs.warn(
		    "Class '" + enumOption.getClass() + "' does not implement " + SingleSetup.class);
	}

	return choices;
    }

    /**
     * Casts a SetupFieldEnum to a MultipleSetup.
     * 
     * @param enumOption
     * @return
     */
    public static MultipleSetup getMultipleSetup(SetupFieldEnum enumOption) {
	// Try casting SetupFieldEnum to AppOptionEnumSetup
	MultipleSetup choices = null;
	try {
	    choices = (MultipleSetup) enumOption;
	} catch (ClassCastException ex) {
	    SpecsLogs.warn(
		    "Class '" + enumOption.getClass() + "' does not implement " + MultipleSetup.class);
	}

	return choices;
    }

    /**
     * Casts a SetupFieldEnum to a MultipleChoice.
     *
     * @param enumOption
     * @return
     */
    public static MultipleChoice getMultipleChoices(SetupFieldEnum enumOption) {
	// Try casting SetupFieldEnum to AppOptionEnumSetup
	MultipleChoice choices = null;
	try {
	    choices = (MultipleChoice) enumOption;
	} catch (ClassCastException ex) {
	    SpecsLogs.warn(
		    "Class '" + enumOption.getClass() + "' does not implement " + MultipleChoice.class);
	}

	return choices;
    }
}
