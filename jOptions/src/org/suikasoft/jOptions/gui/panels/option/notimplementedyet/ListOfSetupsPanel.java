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

package org.suikasoft.jOptions.gui.panels.option.notimplementedyet;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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
 * Panel for editing and managing a list of setup panels.
 *
 * <p>This panel provides controls for adding, removing, and selecting setup elements for a MultipleSetup instance.
 * 
 * @author Joao Bispo
 */
public class ListOfSetupsPanel extends FieldPanel {

    private static final long serialVersionUID = 1L;

    private JPanel currentOptionsPanel;
    private JPanel choicePanel;

    private JLabel label;
    private JComboBox<String> elementsBox;
    private JComboBox<String> choicesBox;
    private JButton removeButton;
    private JButton addButton;

    private List<String> choicesBoxShadow;
    private ListOfSetupDefinitions setups;

    private List<Integer> elementsBoxShadow;
    private List<SetupData> elementsFiles;
    private List<BaseSetupPanel> elementsOptionPanels;

    // Properties
    private static final String ENUM_NAME_SEPARATOR = "-";

    /**
     * Constructs a ListOfSetupsPanel for the given enum option, label, and MultipleSetup.
     *
     * @param enumOption the SetupFieldEnum
     * @param labelName the label for the panel
     * @param setup the MultipleSetup instance
     */
    public ListOfSetupsPanel(SetupFieldEnum enumOption, String labelName, MultipleSetup setup) {
        label = new JLabel(labelName + ":");
        removeButton = new JButton("X");
        addButton = new JButton("Add");

        initChoices(setup);
        initElements();

        // Add actions
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                addButtonActionPerformed(evt);
            }
        });

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                removeButtonActionPerformed(evt);
            }

        });

        elementsBox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                elementComboBoxActionPerformed(e);
            }

        });

        // Build choice panel
        choicePanel = buildChoicePanel();

        currentOptionsPanel = null;

        LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        add(choicePanel);

    }

    /**
     * Initializes the choices available in the panel.
     *
     * @param setupList the MultipleSetup instance containing the setup definitions
     */
    private void initChoices(MultipleSetup setupList) {
        setups = setupList.getSetups();

        choicesBox = new JComboBox<>();
        choicesBoxShadow = new ArrayList<>();

        for (SetupDefinition setup : setups.getSetupKeysList()) {
            String setupName = setup.getSetupName();
            choicesBox.addItem(setupName);
            choicesBoxShadow.add(setupName);
        }

    }

    /**
     * Initializes the elements list and related components.
     */
    private void initElements() {
        elementsBoxShadow = new ArrayList<>();
        elementsBox = new JComboBox<>();
        elementsFiles = new ArrayList<>();
        elementsOptionPanels = new ArrayList<>();
    }

    /**
     * Builds the choice panel containing controls for managing setup elements.
     *
     * @return the constructed JPanel
     */
    private JPanel buildChoicePanel() {
        JPanel panel = new JPanel();

        panel.add(label);
        panel.add(elementsBox);
        panel.add(removeButton);
        panel.add(choicesBox);
        panel.add(addButton);

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return panel;
    }

    /**
     * Adds the option from the available list to the selected list.
     * 
     * @param evt the ActionEvent triggered by the add button
     */
    private void addButtonActionPerformed(ActionEvent evt) {
        int choice = choicesBox.getSelectedIndex();
        if (choice == -1) {
            return;
        }

        addElement(choice);
    }

    /**
     * Removes the option from the selected list to the available list.
     * 
     * @param evt the ActionEvent triggered by the remove button
     */
    private void removeButtonActionPerformed(ActionEvent evt) {
        int indexToRemove = elementsBox.getSelectedIndex();
        if (indexToRemove == -1) {
            return;
        }

        removeElement(indexToRemove);
    }

    /**
     * Updates the options panel based on the selected element.
     * 
     * @param e the ActionEvent triggered by the elements combo box
     */
    private void elementComboBoxActionPerformed(ActionEvent e) {
        updateSetupOptions();
    }

    @Override
    public FieldType getType() {
        return FieldType.setupList;
    }

    /**
     * Adds an element to the elements list, from the choices list.
     * 
     * @param choice the index of the choice to add
     * @return the index of the added element
     */
    public int addElement(int choice) {
        elementsBoxShadow.add(choice);
        SetupDefinition setupKeys = setups.getSetupKeysList().get(choice);

        elementsFiles.add(SetupData.create(setupKeys));

        BaseSetupPanel newPanel = new BaseSetupPanel(setupKeys, identationLevel + 1);

        if (!setupKeys.getSetupKeys().isEmpty()) {
            // newPanel.add(new javax.swing.JSeparator(), 0);
        }

        elementsOptionPanels.add(newPanel);

        updateElementsComboBox();

        int elementIndex = elementsBoxShadow.size() - 1;
        elementsBox.setSelectedIndex(elementIndex);

        return elementIndex;
    }

    /**
     * Loads several elements from a FieldValue.
     * 
     * @param value the FieldValue containing the elements to load
     */
    @Override
    public void updatePanel(Object value) {
        clearElements();

        ListOfSetups maps = (ListOfSetups) value;

        for (SetupData key : maps.getMapOfSetups()) {
            loadElement(key);
        }

    }

    /**
     * Loads a single element from a SetupData instance.
     * 
     * @param table the SetupData instance to load
     */
    private void loadElement(SetupData table) {
        String enumName = table.getSetupName();

        int setupIndex = choicesBoxShadow.indexOf(enumName);

        if (setupIndex == -1) {
            SpecsLogs.getLogger().warning("Could not find enum '" + enumName + "'. Available enums:" + setups);
            return;
        }

        int elementsIndex = addElement(setupIndex);

        elementsFiles.set(elementsIndex, table);
        elementsOptionPanels.get(elementsIndex).loadValues(table);

    }

    /**
     * Updates the elements combo box with the current elements.
     */
    private void updateElementsComboBox() {
        elementsBox.removeAllItems();
        for (int i = 0; i < elementsBoxShadow.size(); i++) {
            int choice = elementsBoxShadow.get(i);
            String setupName = setups.getSetupKeysList().get(choice).getSetupName();

            String boxString = buildSetupString(setupName, i + 1);
            elementsBox.addItem(boxString);
        }
    }

    /**
     * Updates the setup options panel based on the selected element.
     */
    private void updateSetupOptions() {
        if (currentOptionsPanel != null) {
            remove(currentOptionsPanel);
            currentOptionsPanel = null;
        }

        int index = elementsBox.getSelectedIndex();

        if (index != -1) {
            currentOptionsPanel = elementsOptionPanels.get(index);
            add(currentOptionsPanel);
            currentOptionsPanel.revalidate();
        }

        repaint();
    }

    /**
     * Removes an element from the elements list.
     * 
     * @param index the index of the element to remove
     */
    public void removeElement(int index) {
        if (elementsBox.getItemCount() <= index) {
            SpecsLogs.getLogger().warning(
                    "Given index ('" + index + "')is too big. Elements size: " + elementsBox.getItemCount());
            return;
        }

        elementsBoxShadow.remove(index);
        elementsFiles.remove(index);
        elementsOptionPanels.remove(index);

        updateElementsComboBox();

        int newIndex = calculateIndexAfterRemoval(index);
        if (newIndex != -1) {
            elementsBox.setSelectedIndex(newIndex);
        }
    }

    /**
     * Calculates the new index after an element is removed.
     * 
     * @param index the index of the removed element
     * @return the new index
     */
    private int calculateIndexAfterRemoval(int index) {
        int numElements = elementsBox.getItemCount();

        if (numElements == 0) {
            return -1;
        }

        if (numElements > index) {
            return index;
        }

        if (numElements == index) {
            return index - 1;
        }

        SpecsLogs.getLogger().warning("Invalid index '" + index + "' for list with '" + numElements + "' elements.");
        return -1;
    }

    /**
     * Retrieves the packed values of the panel.
     * 
     * @return the ListOfSetups containing the packed values
     */
    public ListOfSetups getPackedValues() {

        List<SetupData> listOfSetups = new ArrayList<>();

        for (int i = 0; i < elementsOptionPanels.size(); i++) {
            listOfSetups.add(elementsOptionPanels.get(i).getMapWithValues());
        }

        return new ListOfSetups(listOfSetups);
    }

    /**
     * Clears all elements from the panel.
     */
    private void clearElements() {
        elementsBox.removeAllItems();

        elementsBoxShadow = new ArrayList<>();
        elementsFiles = new ArrayList<>();
        elementsOptionPanels = new ArrayList<>();
    }

    @Override
    public FieldValue getOption() {
        return FieldValue.create(getPackedValues(), getType());
    }

    /**
     * Builds a string representation of a setup element.
     * 
     * @param enumName the name of the enum
     * @param index the index of the element
     * @return the string representation
     */
    private static String buildSetupString(String enumName, int index) {
        return index + ENUM_NAME_SEPARATOR + enumName;
    }

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
