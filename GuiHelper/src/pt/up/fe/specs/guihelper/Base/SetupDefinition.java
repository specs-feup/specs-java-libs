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
import java.util.Collection;
import java.util.List;

import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Represents the definition of a Setup. Contains the Fields and the name of the Setup.
 *
 * @author Joao Bispo
 */
public class SetupDefinition {

    // public SetupDefinition(Collection<SetupFieldEnum> setupKeys, String setupName) {
    private SetupDefinition(Collection<SetupFieldEnum> setupKeys, String setupName) {
	this.setupKeys = setupKeys;
	this.setupName = setupName;
    }

    /**
     * Creates a SetupDefinition object from a class. The given class must represent an enum which implements the
     * SetupFieldDefiner interface.
     *
     * <p>
     * This is a helper method for classes implementing methods which returns predefined setups (ex.: interface App).
     *
     * @param enumClass
     * @return
     */
    /*
    public static SetupDefinition create(Class enumSetupFieldDefiner) {
       //return GuiHelperUtils.getSetupDefinitionFromEnum(enumSetupDefiner);
       return GuiHelperUtils.getSetupDefinitionFromEnum(enumSetupFieldDefiner);
    }
     *
     */

    /**
     * Creates a SetupDefinition object from a class. The given class must represent an enum which implements the
     * SetupFieldEnum interface.
     *
     * <p>
     * This is a helper method for classes implementing the SetupDefiner interface.
     *
     * @param enumSetupField
     * @return
     */
    // public static SetupDefinition create(Class enumSetupField, String setupName) {
    // public static SetupDefinition create(Class setupFieldEnum) {
    // public static <K extends Enum<K>> SetupDefinition create(Class<K> setupFieldEnum) {
    public static <K extends SetupFieldEnum> SetupDefinition create(Class<K> setupFieldEnum) {
	// Collection<SetupFieldEnum> keys = SetupFieldUtils.getSetupFields(enumSetupField);
	// Collection<SetupFieldEnum> keys = SetupFieldUtils.getSetupFields(setupFieldEnum);
	// List<SetupFieldEnum> keys = EnumUtils.extractValues(setupFieldEnum);
	List<K> keys = SpecsEnums.extractValues(setupFieldEnum);
	// List<K> keys = EnumUtils.extractValues(setupFieldEnum);
	if (keys == null) {
	    SpecsLogs.getLogger().
		    // warning("Could not extract the SetupKeys from class '"+enumSetupField+"'");
		    warning("Could not extract the SetupKeys from class '" + setupFieldEnum + "'");

	    return null;
	}

	String setupName = keys.iterator().next().getSetupName();
	// return new SetupDefinition(keys, setupName);
	return new SetupDefinition(new ArrayList<>(keys), setupName);
    }

    public static SetupDefinition createEmpty(String setupName) {
	List<SetupFieldEnum> keys = new ArrayList<>();
	return new SetupDefinition(keys, setupName);
    }

    public Collection<SetupFieldEnum> getSetupKeys() {
	return setupKeys;
    }

    public String getSetupName() {
	return setupName;
    }

    @Override
    public String toString() {
	return setupName + "\n" + setupKeys.toString();
    }

    /**
     * INSTANCE VARIABLES
     */
    private Collection<SetupFieldEnum> setupKeys;
    private String setupName;
}
