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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.guihelper.Utils;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.ListOfSetups;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Stores a key-value pair for a SetupData object.
 * 
 * @author Joao Bispo
 * 
 */
public class SetupDataKeyValue {

    private final List<String> key;
    private final FieldValue value;

    private static Set<FieldType> SINGLE_SETUP = EnumSet.of(FieldType.integratedSetup,
	    FieldType.setup);

    /**
     * @param key
     * @param value
     */
    public SetupDataKeyValue(List<String> key, FieldValue value) {
	this.key = key;
	this.value = value;
    }

    public boolean replaceValue(SetupData setupData) {
	String currentKey = "";
	SetupData currentSetup = setupData;

	// for(String keyPart : key) {
	for (int i = 0; i < key.size(); i++) {
	    // System.out.println("IT:"+i+"; Setup:"+currentSetup);
	    String keyPart = key.get(i);
	    if (!currentSetup.getDataset().containsKey(keyPart)) {
		SpecsLogs.msgInfo("Given setup does not contain key '" + currentKey + keyPart
			+ "'");
		return false;
	    }

	    // Check if is the last part of the key and replace value
	    if (i == key.size() - 1) {
		currentSetup.getDataset().put(keyPart, value);
		currentKey += keyPart + "->";
		continue;
	    }

	    // Go down another level
	    // FieldValue value = setupData.getDataset().get(key);
	    FieldValue value = setupData.getDataset().get(keyPart);
	    boolean usedTwoKeys = false;

	    // if (value.getType() == FieldType.integratedSetup || value.getType() == FieldType.setup) {
	    if (SINGLE_SETUP.contains(value.getType())) {
		currentSetup = (SetupData) value.getRawValue();
	    } else if (value.getType() == FieldType.multipleChoiceSetup) {
		currentSetup = getSetupFromList(value, key, i);
		usedTwoKeys = true;
	    } else {
		SpecsLogs.msgInfo("Key '" + key + " with value of type '" + value.getType()
			+ "' does not correspond to a SetupData or ListOfSetups.");
		return false;
	    }
	    /*
	    switch (value.getType()) {
	    
	    case integratedSetup:
	    currentSetup = (SetupData) value.getRawValue();
	    case setup:
	    currentSetup = (SetupData) value.getRawValue();
	    case multipleChoiceSetup:
	    currentSetup = getSetupFromList(value, key, i);
	    usedTwoKeys = true;
	    // ListOfSetups listOfSetups = null;
	    // currentSetup = SetupDataUtils.getSetupFromList((ListOfSetups) value.getRawValue(),
	    // key.subList(i, i + 2));
	    // return ((ListOfSetups) value.getRawValue()).getPreferredSetup();
	    case setupList:
	    currentSetup = getSetupFromList(value, key, i);
	    usedTwoKeys = true;
	    // ListOfSetups listOfSetups = null;
	    // currentSetup = SetupDataUtils.getSetupFromList((ListOfSetups) value.getRawValue(),
	    // key.subList(i, i + 2));
	    // return ((ListOfSetups) value.getRawValue()).getPreferredSetup();
	    default:
	    LoggingUtils.msgInfo("Key '" + key + " with value of type '" + value.getType()
	    	+ "' does not correspond to a SetupData or ListOfSetups.");
	    return false;
	    }
	    */

	    // currentSetup = SetupDataUtils.getSetup(currentSetup, keyPart);
	    currentKey += keyPart + "->";
	    if (usedTwoKeys) {
		currentKey += key.get(i + 1) + "->";
		i += 1;
	    }

	    if (currentSetup == null) {
		return false;
	    }
	}

	return true;
    }

    /**
     * @param value2
     * @param key2
     * @param i
     * @return
     */
    private static SetupData getSetupFromList(FieldValue value, List<String> key, int currentIndex) {
	// Raw value of the given value must be a ListOfSetups
	ListOfSetups setupList = (ListOfSetups) value.getRawValue();

	// Key must have one more element beyond the current index
	if ((currentIndex + 1) >= key.size()) {
	    SpecsLogs.msgInfo("Given key '" + key
		    + "' is incomplete, needs one of the following:" + setupList.getMap().keySet());
	    return null;
	}

	String setupName = key.get(currentIndex + 1);
	SetupData setupData = setupList.getMap().get(setupName);

	if (setupData == null) {
	    SpecsLogs.msgInfo("ListOfSetups '" + key.get(currentIndex)
		    + "' does not contain a setup called '" + setupName + "'. Available setups:"
		    + setupList.getMap().keySet());
	    return null;
	}

	return setupData;
    }
}
