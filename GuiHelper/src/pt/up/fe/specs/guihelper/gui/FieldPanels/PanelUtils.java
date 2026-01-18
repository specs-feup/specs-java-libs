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

package pt.up.fe.specs.guihelper.gui.FieldPanels;

import java.util.Collection;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.Base.SetupFieldUtils;
import pt.up.fe.specs.guihelper.SetupFieldOptions.MultipleChoice;
import pt.up.fe.specs.guihelper.SetupFieldOptions.MultipleSetup;
import pt.up.fe.specs.guihelper.SetupFieldOptions.SingleSetup;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.utilities.StringList;

/**
 * Utility methods related to handling of options panels.
 * 
 * @author Joao Bispo
 */
public class PanelUtils {

    /**
     * Creates a new AppOptionPanel from the given AppOptionEnum.
     * 
     * @param setupField
     * @return
     */
    public static FieldPanel newPanel(SetupFieldEnum setupField) {
	FieldType type = setupField.getType();
	String panelLabel = setupField.toString();

	// Parse label a bit. Add spaces between camel case
	// panelLabel = insertSpacesOnCamelCase(panelLabel);
	panelLabel = SpecsStrings.camelCaseSeparate(panelLabel, " ");
	if (type == FieldType.string) {
	    return new StringPanel(panelLabel);
	}

	if (type == FieldType.folder) {
	    return new FolderPanel(panelLabel);
	}

	if (type == FieldType.stringList) {
	    return new StringListPanel(panelLabel);
	}

	if (type == FieldType.integer) {
	    return new IntegerPanel(panelLabel);
	}

	if (type == FieldType.doublefloat) {
	    return new DoublePanel(panelLabel);
	}

	if (type == FieldType.bool) {
	    return new BooleanPanel(panelLabel);
	}

	if (type == FieldType.multipleChoice) {
	    MultipleChoice multipleChoice = SetupFieldUtils.getMultipleChoices(setupField);
	    if (multipleChoice == null) {
		SpecsLogs.warn(messageCannotLoadPanel(type));
		return null;
	    }

	    StringList stringChoices = multipleChoice.getChoices();
	    if (stringChoices == null) {
		throw new RuntimeException("No choices defined for field '" + setupField.name()
			+ "'. Check if SetupFieldEnum class has a case for it in " + "the method 'getChoices()'");
	    }

	    Collection<String> choices = multipleChoice.getChoices().getStringList();
	    return new MultipleChoicePanel(panelLabel, choices);
	}

	if (type == FieldType.multipleChoiceStringList) {
	    MultipleChoice multipleChoice = SetupFieldUtils.getMultipleChoices(setupField);
	    if (multipleChoice == null) {
		SpecsLogs.warn(messageCannotLoadPanel(type));
		return null;
	    }

	    StringList stringList = multipleChoice.getChoices();
	    if (stringList == null) {
		SpecsLogs.warn("Choices not defined for option '" + setupField + "'.");
		return null;
	    }

	    /*
	    Collection<String> choices = multipleChoice.getChoices().getStringList();
	    if(choices == null) {
	       LoggingUtils.getLogger().
	               warning("Choices list defined for option '"+setupField+"' is empty");
	       return null;
	    }*/

	    // return new MultipleChoiceListPanel(panelLabel, choices);
	    return new MultipleChoiceListPanel(panelLabel, stringList.getStringList());
	}

	if (type == FieldType.setupList) {
	    MultipleSetup choices = SetupFieldUtils.getMultipleSetup(setupField);
	    if (choices == null) {
		SpecsLogs.warn("Cannot load setup list panel.");
		return null;
	    }

	    return new ListOfSetupsPanel(setupField, panelLabel, choices);
	}

	if (type == FieldType.multipleChoiceSetup) {
	    MultipleSetup choices = SetupFieldUtils.getMultipleSetup(setupField);
	    if (choices == null) {
		SpecsLogs.warn("Cannot load multiple choice setup panel.");
		return null;
	    }

	    return new MultipleChoiceSetup(setupField, panelLabel, choices);
	}

	if (type == FieldType.setup) {
	    SingleSetup choices = SetupFieldUtils.getSingleSetup(setupField);
	    if (choices == null) {
		SpecsLogs.warn("Cannot load single setup panel.");
		return null;
	    }

	    return new SetupPanel(setupField, panelLabel, choices);
	}

	if (type == FieldType.integratedSetup) {
	    SingleSetup choices = SetupFieldUtils.getSingleSetup(setupField);
	    if (choices == null) {
		SpecsLogs.warn("Cannot load integrated setup panel.");
		return null;
	    }

	    return new IntegratedSetupPanel(choices);
	}

	SpecsLogs.warn(
		"Option type '" + type + "' in option '" + setupField.name() + "' not implemented yet.");
	return null;
    }

    /**
     * Inserts spaces inbetween CamelCase.
     * 
     * TODO: use method on ParseUtils
     * 
     * <p>
     * Example <br>
     * InputString: CamelCase <br>
     * OutputString: Camel Case
     * 
     * @param panelLabel
     * @return
     */
    /*
    private static String insertSpacesOnCamelCase(String panelLabel) {
       List<Integer> upperCaseLetters = new ArrayList<Integer>();
       for(int i=1; i<panelLabel.length(); i++) {
          char c = panelLabel.charAt(i);
          if(!Character.isUpperCase(c)) {
             continue;
          }    

          upperCaseLetters.add(i);
       }

       // Insert spaces in the found indexes
       String newString = panelLabel;
       for(int i=upperCaseLetters.size()-1; i>=0; i--) {
          int index = upperCaseLetters.get(i);
          newString = newString.substring(0, index) + " " + newString.substring(index, newString.length());
       }

       return newString;
    }
    */

    private static String messageCannotLoadPanel(FieldType valueType) {
	return "Cannot load " + valueType + " panel";
    }

}
