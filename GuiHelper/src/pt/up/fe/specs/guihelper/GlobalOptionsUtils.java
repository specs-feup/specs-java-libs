/**
 * Copyright 2012 SPeCS Research Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License. under the License.
 */

package pt.up.fe.specs.guihelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author Joao Bispo
 *
 */
public class GlobalOptionsUtils {

    /**
     * Reads the data corresponding to the given SetupFieldEnum from
     * Preferences. Returns null if any of the fields are not supported.
     *
     * @param setupFieldClass
     * @return
     */
    public static void saveData(Class<? extends SetupFieldEnum> setupFieldClass,
	    SetupData setupData) {
	// Set last used file
	Preferences prefs = Preferences.userNodeForPackage(setupFieldClass);

	// Setup Access helper
	SetupAccess setupAccess = new SetupAccess(setupData);

	// Get values
	List<SetupFieldEnum> fields = SpecsEnums.extractValues(setupFieldClass);

	// For each field, save the value in setup data
	for (SetupFieldEnum field : fields) {
	    boolean success = putPreferencesValue(prefs, setupAccess, field);
	    if (!success) {
		SpecsLogs
			.warn("Given SetupFieldEnum class contains fields not supported by global options.");
		return;
	    }
	}

	return;
    }

    /**
     * Reads the data corresponding to the given SetupFieldEnum from
     * Preferences. Returns null if any of the fields are not supported.
     *
     * @param setupFieldClass
     * @return
     */
    public static SetupData loadData(Class<? extends SetupFieldEnum> setupFieldClass) {
	// Set last used file
	Preferences prefs = Preferences.userNodeForPackage(setupFieldClass);

	// Get values
	List<SetupFieldEnum> fields = SpecsEnums.extractValues(setupFieldClass);

	Map<String, FieldValue> dataSet = new HashMap<>();
	for (SetupFieldEnum field : fields) {
	    Object value = getPreferencesValue(prefs, field);
	    if (value == null) {
		SpecsLogs
			.warn("Given SetupFieldEnum class contains fields not supported by global options.");
		return null;
	    }
	    dataSet.put(field.name(), FieldValue.create(value, field.getType()));
	}
	/*
	String baseInputFolderString = prefs.get(BaseInputFolder.name(), null);
	String baseOutputFolderString = prefs.get(BaseOutputFolder.name(), null);


	dataSet.put(BaseInputFolder.name(), FieldValue.create(baseInputFolderString, BaseInputFolder.getType()));
	dataSet.put(BaseOutputFolder.name(), FieldValue.create(baseOutputFolderString, BaseOutputFolder.getType()));
	*/
	SetupData setupData = new SetupData(dataSet, fields.get(0)
		.getSetupName());

	return setupData;
    }

    /**
     * @param field
     * @return
     */
    public static Object getPreferencesValue(Preferences prefs,
	    SetupFieldEnum field) {
	String name = field.name();

	switch (field.getType()) {
	case bool:
	    return prefs.getBoolean(name, false);
	case doublefloat:
	    return prefs.getDouble(name, 0);
	case integer:
	    return prefs.getInt(name, 0);
	case string:
	    return prefs.get(name, "");
	default:
	    SpecsLogs.warn("Case not defined:" + field.getType());
	    return null;
	}
    }

    /**
     * @param field
     * @return
     */
    public static boolean putPreferencesValue(Preferences prefs,
	    SetupAccess setupAccess, SetupFieldEnum field) {

	String name = field.name();

	switch (field.getType()) {
	case bool:
	    prefs.putBoolean(name, setupAccess.getBoolean(field));
	    return true;
	case doublefloat:
	    prefs.putDouble(name, setupAccess.getDouble(field));
	    return true;
	case integer:
	    prefs.putInt(name, setupAccess.getInteger(field));
	    return true;
	case string:
	    prefs.put(name, setupAccess.getString(field));
	    return true;
	default:
	    SpecsLogs.warn("Case not defined:" + field.getType());
	    return false;
	}
    }
}
