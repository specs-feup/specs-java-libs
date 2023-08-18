/**
 * Copyright 2023 SPeCS.
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

package org.suikasoft.jOptions.gui.panels.option;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.values.SetupList;

import pt.up.fe.specs.util.SpecsLogs;

public class SetupListPanel extends KeyPanel<SetupList> {

    private static final long serialVersionUID = 1L;

    private final List<StoreDefinition> definitions;

    private final JButton removeButton;
    private final JButton addButton;

    private final JComboBox<String> choicesBox;
    private final List<String> choicesBoxShadow;

    private final JComboBox<String> elementsBox;

    private final JPanel choicePanel;

    /// State

    private JPanel currentOptionsPanel;

    private List<Integer> elementsBoxShadow;
    // private List<DataStore> elementsFiles;
    private List<SetupPanel> elementsOptionPanels;

    public SetupListPanel(DataKey<SetupList> key, DataStore data, Collection<StoreDefinition> definitions) {
        super(key, data);

        this.definitions = new ArrayList<>(definitions);

        removeButton = new JButton("X");
        addButton = new JButton("Add");

        choicesBox = new JComboBox<>();
        choicesBoxShadow = new ArrayList<>();

        // Init elements
        elementsBoxShadow = new ArrayList<>();
        elementsBox = new JComboBox<>();
        // elementsFiles = new ArrayList<>();
        elementsOptionPanels = new ArrayList<>();

        initChoices();

        // Add actions
        addButton.addActionListener(this::addButtonActionPerformed);

        removeButton.addActionListener(this::removeButtonActionPerformed);

        elementsBox.addActionListener(this::elementComboBoxActionPerformed);

        // Build choice panel
        choicePanel = buildChoicePanel();

        currentOptionsPanel = null;

        LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        add(choicePanel);

    }

    private void initChoices() {

        for (var definition : definitions) {
            String setupName = definition.getName();
            choicesBox.addItem(setupName);
            choicesBoxShadow.add(setupName);
        }

    }

    private JPanel buildChoicePanel() {
        JPanel panel = new JPanel();

        panel.add(elementsBox);
        panel.add(removeButton);
        panel.add(choicesBox);
        panel.add(addButton);

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return panel;
    }

    /**
     * Adds the option from the avaliable list to selected list.
     * 
     * @param evt
     */
    private void addButtonActionPerformed(ActionEvent evt) {
        // Determine what element is selected
        int choice = choicesBox.getSelectedIndex();
        if (choice == -1) {
            return;
        }

        addElement(choice);
    }

    /**
     * Adds an element to the elements list, from the choices list.
     * 
     * @return the index of the added element
     */
    public int addElement(int choice) {
        // Add index to elements
        elementsBoxShadow.add(choice);

        // Get setup options and create option file for element
        var definition = definitions.get(choice);
        var dataStore = DataStore.newInstance(definition);

        // elementsFiles.add(dataStore);

        // Not sure if unique data keys are required here
        var dataKey = KeyFactory.dataStore(buildDataStoreLabel(definition.getName(), choice + 1), definition);

        var newPanel = new SetupPanel(dataKey, dataStore, definition);
        // BaseSetupPanel newPanel = new BaseSetupPanel(setupKeys, identationLevel + 1);

        elementsOptionPanels.add(newPanel);

        // Refresh
        updateElementsComboBox();

        int elementIndex = elementsBoxShadow.size() - 1;

        // Select last item
        elementsBox.setSelectedIndex(elementIndex);

        // Update vision of setup options - not needed, when we select, automatically updates
        // updateSetupOptions();

        return elementIndex;
    }

    private void updateElementsComboBox() {

        // Build list of strings to present
        elementsBox.removeAllItems();
        for (int i = 0; i < elementsBoxShadow.size(); i++) {

            // Get choice name
            int choice = elementsBoxShadow.get(i);

            String setupName = definitions.get(choice).getName();

            String boxString = buildDataStoreLabel(setupName, i + 1);
            elementsBox.addItem(boxString);
        }
    }

    private String buildDataStoreLabel(String definitionName, int number) {
        return number + " - " + definitionName;
    }

    /**
     * Removes the option from the selected list to the available list.
     *
     * @param evt
     */
    private void removeButtonActionPerformed(ActionEvent evt) {
        // Determine index of selected element to remove
        int indexToRemove = elementsBox.getSelectedIndex();
        if (indexToRemove == -1) {
            return;
        }

        removeElement(indexToRemove);
    }

    /**
     * Removes an element from the elements list.
     * 
     * @return
     */
    private void removeElement(int index) {

        // Check if the index is valid
        if (elementsBox.getItemCount() <= index) {
            SpecsLogs.getLogger().warning(
                    "Given index ('" + index + "')is too big. Elements size: " + elementsBox.getItemCount());
            return;
        }

        // Remove shadow index, AppOptionFile and panel
        elementsBoxShadow.remove(index);
        // elementsFiles.remove(index);
        elementsOptionPanels.remove(index);

        // Refresh
        updateElementsComboBox();

        // Calculate new index of selected element and select it
        int newIndex = calculateIndexAfterRemoval(index);
        if (newIndex != -1) {
            elementsBox.setSelectedIndex(newIndex);
        }
    }

    private int calculateIndexAfterRemoval(int index) {
        int numElements = elementsBox.getItemCount();

        // If there are no elements, return -1
        if (numElements == 0) {
            return -1;
        }

        // If there are enough elements, the index is the same
        if (numElements > index) {
            return index;
        }

        // If size is the same as index, it means that we removed the last element
        // Return the index of the current last element
        if (numElements == index) {
            return index - 1;
        }

        SpecsLogs.getLogger().warning("Invalid index '" + index + "' for list with '" + numElements + "' elements.");
        return -1;
    }

    /**
     * Updates the options panel.
     *
     * @param e
     */
    private void elementComboBoxActionPerformed(ActionEvent e) {
        updateSetupOptions();
    }

    private void updateSetupOptions() {
        if (currentOptionsPanel != null) {
            remove(currentOptionsPanel);
            currentOptionsPanel = null;
        }

        // Determine what item is selected in the elements combo
        int index = elementsBox.getSelectedIndex();

        if (index != -1) {
            currentOptionsPanel = elementsOptionPanels.get(index);
            add(currentOptionsPanel);
            currentOptionsPanel.revalidate();
        }

        // TODO: Is it repaint necessary here, or revalidate on panel solves it?
        repaint();
        // System.out.println("SetupPanel Repainted");
    }

    @Override
    public SetupList getValue() {
        List<DataStore> dataStores = new ArrayList<>();

        // Go to each panel collect the DataStores
        for (var setupPanel : elementsOptionPanels) {
            var data = setupPanel.getValue();
            dataStores.add(data);
            // System.out.println("DATA: " + data);
        }

        var value = new SetupList(getKey().getName(), dataStores);
        // System.out.println("GET VALUE:" + value);
        return value;
    }

    @Override
    public <ET extends SetupList> void setValue(ET value) {
        // System.out.println("SET VALUE:" + value);

        // Clear previous values
        clearElements();

        for (var dataStore : value.getDataStores()) {
            loadElement(dataStore);
        }

    }

    /**
     * Loads a single DataStore.
     * 
     * @param aClass
     * @param aFile
     */
    private void loadElement(DataStore table) {

        // Build name
        String enumName = table.getName();

        int setupIndex = choicesBoxShadow.indexOf(enumName);

        if (setupIndex == -1) {
            SpecsLogs.getLogger()
                    .warning("Could not find enum '" + enumName + "'. Available enums:" + choicesBoxShadow);
            return;
        }

        // Create element
        int elementsIndex = addElement(setupIndex);

        // Set option file
        // elementsFiles.set(elementsIndex, table);

        // Load values in the file
        elementsOptionPanels.get(elementsIndex).setValue(table);
    }

    private void clearElements() {
        elementsBox.removeAllItems();

        elementsBoxShadow = new ArrayList<>();
        // elementsFiles = new ArrayList<>();
        elementsOptionPanels = new ArrayList<>();
    }

}
