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

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of several Setups definitions.
 *
 * @author Joao Bispo
 */
public class ListOfSetupDefinitions {

    public ListOfSetupDefinitions() {
	this(new ArrayList<SetupDefinition>());
    }

    public ListOfSetupDefinitions(List<SetupDefinition> setupKeysList) {
	this.setupKeysList = setupKeysList;
    }

    /**
     * @param compilersetups
     * @return
     */
    public static ListOfSetupDefinitions newInstance(List<Class<? extends SetupFieldEnum>> setups) {

	List<SetupDefinition> defs = new ArrayList<>();

	for (Class<? extends SetupFieldEnum> aClass : setups) {
	    defs.add(SetupDefinition.create(aClass));
	}

	return new ListOfSetupDefinitions(defs);
    }

    public List<SetupDefinition> getSetupKeysList() {
	return setupKeysList;
    }

    public void addSetupDefinition(Class<? extends SetupFieldEnum> setupDefinerClass) {
	setupKeysList.add(SetupDefinition.create(setupDefinerClass));
    }

    /**
     * INSTANCE VARIABLES
     */
    List<SetupDefinition> setupKeysList;
}
