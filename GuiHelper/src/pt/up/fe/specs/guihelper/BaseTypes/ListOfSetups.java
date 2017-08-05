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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.guihelper.BaseTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.guihelper.Base.ListOfSetupDefinitions;
import pt.up.fe.specs.guihelper.Base.SetupDefinition;
import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Represents a list of several SetupData objects.
 * 
 * @author Joao Bispo
 */
// public class ListOfSetups implements Serializable {
public class ListOfSetups {

    // Consider replace with LinkedHashMap
    private List<SetupData> setupList;
    private Map<String, SetupData> mapOfSetups;
    private Integer preferredIndex;

    public ListOfSetups(List<SetupData> listOfSetups) {
	this.mapOfSetups = SpecsFactory.newLinkedHashMap();
	for (SetupData setup : listOfSetups) {
	    this.mapOfSetups.put(setup.getSetupName(), setup);
	}
	setupList = SpecsFactory.newArrayList(listOfSetups);

	// this.listOfSetups = listOfSetups;
	preferredIndex = null;
    }

    public static ListOfSetups create(ListOfSetupDefinitions listOfSetupDefinitions) {
	List<SetupData> setups = new ArrayList<>();
	for (SetupDefinition setupDefinition : listOfSetupDefinitions.getSetupKeysList()) {
	    SetupData newSetup = SetupData.create(setupDefinition);
	    setups.add(newSetup);
	}

	return new ListOfSetups(setups);
    }

    public List<SetupData> getMapOfSetups() {
	// return listOfSetups;
	return setupList;
    }

    /**
     * @return the listOfSetups
     */
    public Map<String, SetupData> getMap() {
	return mapOfSetups;
    }

    public void setPreferredIndex(Integer preferredIndex) {
	this.preferredIndex = preferredIndex;
    }

    public Integer getPreferredIndex() {
	return preferredIndex;
    }

    public SetupData getPreferredSetup() {
	if (mapOfSetups.isEmpty()) {
	    SpecsLogs.msgWarn("There are no setups.");
	    return null;
	}

	if (preferredIndex == null) {
	    SpecsLogs.msgWarn("Preferred index not set, returning first setup.");
	    return mapOfSetups.get(0);
	}

	String setupName = setupList.get(preferredIndex).getSetupName();

	// return mapOfSetups.get(preferredIndex);
	return mapOfSetups.get(setupName);
    }

    /**
     * @return
     */
    public int getNumSetups() {
	return setupList.size();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return mapOfSetups.toString();
    }

    // Variable needed for serialization.
    // private static final long serialVersionUID = 1;
}
