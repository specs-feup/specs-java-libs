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

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.Base.ListOfSetupDefinitions;
import pt.up.fe.specs.guihelper.Base.SetupDefinition;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.ListOfSetups;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.guihelper.SetupFieldOptions.MultipleSetup;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.guihelper.gui.BasePanels.BaseSetupPanel;
import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * 
 * @author Joao Bispo
 */
public class MultipleChoiceSetup extends FieldPanel {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private JPanel currentOptionsPanel;
    private JPanel choicePanel;

    private JLabel label;
    // private JComboBox elementsBox;
    // private JComboBox<String> choicesBox;
    private JComboBox<String> choicesBox;
    // private JButton removeButton;
    // private JButton addButton;

    private List<String> choicesBoxNames;
    private ListOfSetupDefinitions setups;

    private List<Integer> elementsBoxShadow;
    private List<SetupData> elementsFiles;
    private List<BaseSetupPanel> elementsOptionPanels;

    // Properties
    // private static final String ENUM_NAME_SEPARATOR = "-";

    public MultipleChoiceSetup(SetupFieldEnum enumOption, String labelName, MultipleSetup setup) {
	// Initiallize objects
	label = new JLabel(labelName + ":");
	// removeButton = new JButton("X");
	// addButton = new JButton("Add");

	initChoices(setup);
	initElements();
	// Add choices
	for (int i = 0; i < choicesBox.getItemCount(); i++) {
	    // System.out.println("Adding element "+i);
	    addElement(i);
	}

	// Add actions
	/*
	addButton.addActionListener(new ActionListener() {
	   @Override
	   public void actionPerformed(ActionEvent evt) {
	      addButtonActionPerformed(evt);
	   }
	});
	*/
	/*
	removeButton.addActionListener(new ActionListener() {
	   @Override
	   public void actionPerformed(ActionEvent evt) {
	      removeButtonActionPerformed(evt);
	   }

	});
	*/
	// elementsBox.addActionListener(new ActionListener() {
	choicesBox.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		choiceComboBoxActionPerformed(e);
	    }

	});

	// Build choice panel
	choicePanel = buildChoicePanel();

	currentOptionsPanel = null;

	LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
	setLayout(layout);
	add(choicePanel);

    }

    private void initChoices(MultipleSetup setupList) {
	setups = setupList.getSetups();

	// choicesBox = new JComboBox<String>();
	choicesBox = new JComboBox<>();
	choicesBoxNames = new ArrayList<>();

	for (SetupDefinition setup : setups.getSetupKeysList()) {
	    String setupName = setup.getSetupName();
	    choicesBox.addItem(setupName);
	    choicesBoxNames.add(setupName);
	}

    }

    private void initElements() {
	elementsBoxShadow = new ArrayList<>();
	// elementsBox = new JComboBox();
	elementsFiles = new ArrayList<>();
	elementsOptionPanels = new ArrayList<>();
    }

    private JPanel buildChoicePanel() {
	JPanel panel = new JPanel();

	panel.add(label);
	// panel.add(elementsBox);
	// panel.add(removeButton);
	panel.add(choicesBox);
	// panel.add(addButton);

	panel.setLayout(new FlowLayout(FlowLayout.LEFT));

	return panel;
    }

    /**
     * Adds the option from the avaliable list to selected list.
     * 
     * @param evt
     */
    /*
     private void addButtonActionPerformed(ActionEvent evt) {
        // Determine what element is selected
        int choice = choicesBox.getSelectedIndex();
        if (choice == -1) {
           return;
        }

        addElement(choice);
     }
    */
    /**
     * Removes the option from the selected list to the available list.
     * 
     * @param evt
     */
    /*
    private void removeButtonActionPerformed(ActionEvent evt) {
      // Determine index of selected element to remove
      int indexToRemove = elementsBox.getSelectedIndex();
      if(indexToRemove == -1) {
         return;
      }
      
      removeElement(indexToRemove);
    }
    */
    /**
     * Updates the options panel.
     * 
     * @param e
     */
    private void choiceComboBoxActionPerformed(ActionEvent e) {
	updateSetupOptions();
    }

    @Override
    public FieldType getType() {
	return FieldType.setupList;
    }

    /**
     * Adds an element to the elements list, from the choices list.
     * 
     * @return the index of the added element
     */
    private int addElement(int choice) {
	// Add index to elements
	elementsBoxShadow.add(choice);

	// Get setup options and create option file for element
	SetupDefinition setupKeys = setups.getSetupKeysList().get(choice);

	elementsFiles.add(SetupData.create(setupKeys));

	BaseSetupPanel newPanel = new BaseSetupPanel(setupKeys);
	// newPanel.add(new javax.swing.JSeparator(),0);
	// newPanel.add(new javax.swing.JSeparator());
	elementsOptionPanels.add(newPanel);
	// System.out.println("ElementOptionsPanel:"+elementsOptionPanels.size());

	// Refresh
	// updateElementsComboBox();

	int elementIndex = elementsBoxShadow.size() - 1;

	// Select last item
	// elementsBox.setSelectedIndex(elementIndex);
	// Update vision of setup options - not needed, when we select, automatically updates
	// updateSetupOptions();

	return elementIndex;
    }

    /**
     * Loads several elements from an AppValue.
     * 
     * @param choice
     */
    @Override
    public void updatePanel(Object value) {
	// Clear previous values
	// clearElements();

	/*
	SetupData setupData = (SetupData) value;
	loadSetup(setupData);
	*/

	ListOfSetups maps = (ListOfSetups) value;

	for (SetupData key : maps.getMapOfSetups()) {
	    // loadElement(key);
	    loadSetup(key);
	}

	// Show preferred setup
	Integer choice = maps.getPreferredIndex();
	if (choice == null) {
	    choice = 0;
	}

	choicesBox.setSelectedIndex(choice);
	updateSetupOptions();

    }

    /**
     * Loads the given setup.
     * 
     * @param aClass
     * @param aFile
     */
    private void loadSetup(SetupData setupData) {
	// Build name
	String enumName = setupData.getSetupName();

	int setupIndex = choicesBoxNames.indexOf(enumName);

	if (setupIndex == -1) {
	    SpecsLogs.getLogger().warning("Could not find enum '" + enumName + "'. Available enums:" + setups);
	    return;
	}

	// Create element
	// int elementsIndex = addElement(setupIndex);

	// Set option file
	// elementsFiles.set(elementsIndex, setupData);
	elementsFiles.set(setupIndex, setupData);
	// Load values in the file
	// elementsOptionPanels.get(elementsIndex).loadValues(setupData);
	elementsOptionPanels.get(setupIndex).loadValues(setupData);

    }

    /*
       private void updateElementsComboBox() {
          // Build list of strings to present
          elementsBox.removeAllItems();
          for(int i=0; i<elementsBoxShadow.size(); i++) {
             // Get choice name
             int choice = elementsBoxShadow.get(i);
             String setupName = setups.getSetupKeysList().get(choice).getSetupName();

             String boxString = buildSetupString(setupName, i+1);
             elementsBox.addItem(boxString);
          }
       }
    */
    private void updateSetupOptions() {
	if (currentOptionsPanel != null) {
	    remove(currentOptionsPanel);
	    currentOptionsPanel = null;
	}

	// Determine what item is selected in the elements combo
	// int index = elementsBox.getSelectedIndex();
	int index = choicesBox.getSelectedIndex();

	if (index != -1) {
	    currentOptionsPanel = elementsOptionPanels.get(index);
	    add(currentOptionsPanel);
	    currentOptionsPanel.revalidate();
	}

	// TODO: Is it repaint necessary here, or revalidate on panel solves it?
	repaint();
	// System.out.println("SetupPanel Repainted");
    }

    /**
     * Removes an element from the elements list.
     * 
     * @return
     */
    /*
     public void removeElement(int index) {
        // Check if the index is valid
        if(elementsBox.getItemCount() <= index) {
           LoggingUtils.getLogger().
                   warning("Given index ('"+index+"')is too big. Elements size: "+elementsBox.getItemCount());
           return;
        }

        // Remove shadow index, AppOptionFile and panel
        elementsBoxShadow.remove(index);
        elementsFiles.remove(index);
        elementsOptionPanels.remove(index);

        // Refresh
        //updateElementsComboBox();

        // Calculate new index of selected element and select it
        int newIndex = calculateIndexAfterRemoval(index);
        if(newIndex != -1) {
           elementsBox.setSelectedIndex(newIndex);
        }
     }
    */
    /*
    private int calculateIndexAfterRemoval(int index) {
       int numElements = elementsBox.getItemCount();

       // If there are no elements, return -1
       if(numElements == 0) {
          return -1;
       }

       // If there are enough elements, the index is the same
       if(numElements > index) {
          return index;
       }

       // If size is the same as index, it means that we removed the last element
       // Return the index of the current last element
       if(numElements == index) {
          return index-1;
       }

       LoggingUtils.getLogger().
               warning("Invalid index '"+index+"' for list with '"+numElements+"' elements.");
       return -1;
    }
    */

    public ListOfSetups getSetups() {
	// public SetupData getSetups() {
	/*
	      // Get index of selected setup
	      int choice = choicesBox.getSelectedIndex();
	      if (choice == -1) {
	         LoggingUtils.getLogger().
	                 warning("Could not get index of selected setup.");
	         return null;
	      }


	      if (elementsOptionPanels.isEmpty()) {
	         SetupDefinition setupDefinition = setups.getSetupKeysList().get(choice);
	         return SetupData.create(setupDefinition);
	      }

	      return elementsOptionPanels.get(choice).getMapWithValues();
	      */

	List<SetupData> listOfSetups = new ArrayList<>();

	// For each selected panel, add the corresponding options table to the return list
	for (int i = 0; i < elementsOptionPanels.size(); i++) {
	    // int choicesIndex = elementsBoxShadow.get(i);

	    listOfSetups.add(elementsOptionPanels.get(i).getMapWithValues());
	}

	ListOfSetups currentSetups = new ListOfSetups(listOfSetups);

	// Get index of selected setup
	int choice = choicesBox.getSelectedIndex();
	if (choice == -1) {
	    SpecsLogs.getLogger().warning("Could not get index of selected setup.");
	    return null;
	}
	currentSetups.setPreferredIndex(choice);

	// return new ListOfSetups(listOfSetups);
	return currentSetups;

    }

    /*
       private void clearElements() {
          //elementsBox.removeAllItems();

          elementsBoxShadow = new ArrayList<Integer>();
          elementsFiles = new ArrayList<SetupData>();
          elementsOptionPanels = new ArrayList<BaseSetupPanel>();
       }
    */
    @Override
    public FieldValue getOption() {
	return FieldValue.create(getSetups(), getType());
    }

    /*
    private String buildSetupString(String enumName, int index) {
       return index+ ENUM_NAME_SEPARATOR + enumName;
    }
    */
    @Override
    public JLabel getLabel() {
	return label;
    }

    @Override
    public Collection<FieldPanel> getPanels() {
	return elementsOptionPanels.stream()
		.map(setupPanel -> setupPanel.getPanels().values())
		.reduce(new ArrayList<>(), SpecsCollections::add);
    }

}
