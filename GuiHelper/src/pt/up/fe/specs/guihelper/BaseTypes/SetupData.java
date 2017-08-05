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

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.Base.SetupDefinition;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.Base.SetupFieldUtils;
import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Maps fields to values.
 * 
 * @author Joao Bispo
 */
public class SetupData implements Serializable {

    private final Map<String, FieldValue> dataset;
    private final String setupName;
    private File setupFile;

    // Variable needed for serialization.
    private static final long serialVersionUID = 2;

    public SetupData(String setupName) {
	this(new HashMap<>(), setupName);
    }

    public SetupData(Map<String, FieldValue> dataset, String setupName) {
	this.dataset = dataset;
	this.setupName = setupName;
	setupFile = null;
    }

    public SetupData(SetupData setupData, String setupName) {
	this(new HashMap<>(setupData.dataset), setupName);
    }

    /**
     * Creates a SetupData with blank/default values from an enumeration implementing the interface SetupFieldEnum
     * class.
     * 
     * @param setupFieldEnum
     * @return
     */
    // public static SetupData create(Class<?> setupFieldEnum) {
    public static <K extends SetupFieldEnum> SetupData create(Class<K> setupFieldEnum) {
	return create(SetupDefinition.create(setupFieldEnum));
    }

    /**
     * Creates a SetupData with blank values from a SetupDefinition.
     * 
     * @param setupDefinition
     * @return
     */
    public static SetupData create(SetupDefinition setupDefinition) {
	String setupName = setupDefinition.getSetupName();

	// Build map with default/blank values
	Map<String, FieldValue> dataset = new HashMap<>();
	for (SetupFieldEnum setupField : setupDefinition.getSetupKeys()) {
	    FieldValue newValue = SetupFieldUtils.newValue(setupField);
	    String key = setupField.name();
	    dataset.put(key, newValue);
	}

	SetupData setupData = new SetupData(dataset, setupName);
	return setupData;
    }

    public Set<String> keySet() {
	return dataset.keySet();
    }

    public FieldValue get(SetupFieldEnum key) {
	return get(key.name());
    }

    public FieldValue get(String key) {
	return dataset.get(key);
    }

    /**
     * Returns the value associated with the given key.
     * 
     * @param key
     * @return
     */
    public FieldValue get(List<String> key) {
	// Return null if empty
	if (key.isEmpty()) {
	    SpecsLogs.msgInfo("No key specified for setup '" + getSetupName() + "'");
	    return null;
	}

	// If key has more than 3 elements, it is not supported
	/*
	if (key.size() > 3) {
	    LoggingUtils
		    .msgWarn("Keys with more than three elements are not supported. Given key: "
			    + key);
	    return null;
	}
	*/

	// Key with one element represents a simple parameter
	String firstKey = key.get(0);
	if (key.size() == 1) {
	    FieldValue value = get(firstKey);

	    if (value == null) {
		SpecsLogs.msgInfo("Could not find a parameter name '" + firstKey
			+ "' inside setup '" + getSetupName() + "'");
		return null;
	    }

	    return value;
	}

	// Keys with more than one element represent parameters inside setups
	// First element addresses another setup, or a list of setups
	FieldValue aSetup = get(firstKey);

	if (aSetup.getType() == FieldType.multipleChoiceSetup) {
	    return getMultipleSetup(aSetup, SpecsCollections.subList(key, 1));
	}

	if (aSetup.getType() == FieldType.setup || aSetup.getType() == FieldType.integratedSetup) {

	    // Get setup
	    /*
	    if (!SetupData.class.isInstance(aSetup.getRawValue())) {
	    LoggingUtils.msgInfo("Element '" + firstKey + "' from key '" + key
	    	+ "' does not correspond to a setup.");
	    return null;
	    }
	    */

	    // Get SetupData a call the method recursively
	    SetupData aSetupData = (SetupData) aSetup.getRawValue();
	    return aSetupData.get(SpecsCollections.subList(key, 1));

	}

	// Key with two elements represents a parameter inside a setup
	/*
	if (key.size() == 2) {
	    // First element addresses another setup
	    FieldValue aSetup = get(firstKey);
	
	    // Get setup
	    // if(SetupData.class.isInstance(aSetup.getRawValue())) {
	    if (!SetupData.class.isInstance(aSetup.getRawValue())) {
		LoggingUtils.msgInfo("Element '" + firstKey + "' from key '" + key
			+ "' does not correspond to a setup.");
		return null;
	    }
	
	    // Get SetupData a call the method recursively
	    SetupData aSetupData = (SetupData) aSetup.getRawValue();
	    return aSetupData.get(key.subList(1, key.size()));
	}
	*/

	throw new RuntimeException("Case not supported:" + aSetup.getType());
	/*
		// Key with three elements represents a parameter inside a list of setups
		FieldValue setupValue = get(firstKey);
		if (setupValue.getType() != FieldType.multipleChoiceSetup) {
		    LoggingUtils.msgWarn("Expected a field of type " + FieldType.multipleChoiceSetup);
		    return null;
		}
	
		ListOfSetups listOfSetups = (ListOfSetups) setupValue.getRawValue();
		Map<String, SetupData> setupMap = listOfSetups.getMap();
	
		// Get second key
		String setupName = key.get(1);
		SetupData setupData = setupMap.get(setupName);
	
		if (setupData == null) {
		    LoggingUtils.msgInfo("Could not find Setup '" + setupName + "' inside Setup list '"
			    + firstKey + "'");
		    return null;
		}
	
		FieldValue value = setupData.get(key.subList(2, key.size()));
		System.out.println("VALUE:" + value);
		return value;
	*/
    }

    /**
     * Returns a value from a list of setups.
     * 
     * @param setupValue
     * @param key
     * @return
     */
    private static FieldValue getMultipleSetup(FieldValue setupValue, List<String> key) {

	// setupValue and key represents a parameter inside a list of setups
	/*
		if (setupValue.getType() != FieldType.multipleChoiceSetup) {
		    LoggingUtils.msgWarn("Expected a field of type " + FieldType.multipleChoiceSetup);
		    return null;
		}
	*/

	ListOfSetups listOfSetups = (ListOfSetups) setupValue.getRawValue();
	Map<String, SetupData> setupMap = listOfSetups.getMap();

	// Get setup name
	String setupName = key.get(0);
	// String setupName = key.get(1);
	SetupData setupData = setupMap.get(setupName);

	if (setupData == null) {
	    SpecsLogs.msgInfo("Could not find Setup with name '" + setupName
		    + "' inside list of setups. Available setups: '" + setupMap.keySet());
	    return null;
	}

	return setupData.get(SpecsCollections.subList(key, 1));

	// FieldValue value = setupData.get(key.subList(2, key.size()));
	// System.out.println("VALUE:" + value);
	// return value;
    }

    public <E extends Enum<E>> FieldValue put(SetupFieldEnum key, E value) {
	return put(key, FieldValue.create(value.name(), key));
    }

    public FieldValue put(SetupFieldEnum key, Object value) {
	return put(key, FieldValue.create(value, key));
    }

    public FieldValue put(SetupFieldEnum key, FieldValue value) {
	return put(key.name(), value);
    }

    public FieldValue put(String key, FieldValue value) {
	return dataset.put(key, value);
    }

    public String getSetupName() {
	return setupName;
    }

    /**
     * @return the setupFile
     */
    public File getSetupFile() {
	return setupFile;
    }

    /**
     * @param setupFile
     *            the setupFile to set
     */
    public void setSetupFile(File setupFile) {
	if (this.setupFile != null) {
	    SpecsLogs.msgInfo("Keeping previous value of setup file:"
		    + this.setupFile.getAbsolutePath());

	    return;
	}

	this.setupFile = setupFile;

	// Set file in other fields
	for (FieldValue value : dataset.values()) {
	    setSetupFile(value.getRawValue(), setupFile);
	}
    }

    /**
     * @param rawValue
     * @param setupFile
     */
    private static void setSetupFile(Object rawValue, File setupFile) {

	if (rawValue instanceof SetupData) {
	    ((SetupData) rawValue).setSetupFile(setupFile);
	    return;
	}

	if (rawValue instanceof ListOfSetups) {
	    ListOfSetups setups = (ListOfSetups) rawValue;
	    for (SetupData setup : setups.getMapOfSetups()) {
		setup.setSetupFile(setupFile);
	    }
	    return;
	}

    }

    private static void resetSetupFile(Object rawValue) {

	if (rawValue instanceof SetupData) {
	    ((SetupData) rawValue).resetSetupFile();
	    return;
	}

	if (rawValue instanceof ListOfSetups) {
	    ListOfSetups setups = (ListOfSetups) rawValue;
	    for (SetupData setup : setups.getMapOfSetups()) {
		setup.resetSetupFile();
	    }
	    return;
	}

    }

    /**
     * @return the dataset
     */
    public Map<String, FieldValue> getDataset() {
	return dataset;
    }

    @Override
    public String toString() {
	return dataset.toString();
    }

    /**
     * @param generatesinglefile
     * @param singleCFile
     */
    public void putString(SetupFieldEnum key, String value) {
	FieldValue fieldValue = FieldValue.create(value, FieldType.string);
	put(key, fieldValue);
    }

    /**
     * 
     */
    public void resetSetupFile() {
	setupFile = null;

	for (FieldValue value : dataset.values()) {
	    resetSetupFile(value.getRawValue());
	}
    }
}
