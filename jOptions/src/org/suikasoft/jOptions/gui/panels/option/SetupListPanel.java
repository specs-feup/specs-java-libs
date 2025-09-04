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
import java.io.Serial;
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

/**
 * Panel for editing and managing lists of SetupList values.
 *
 * <p>This panel provides controls for adding, removing, and selecting setup elements for a DataKey of type SetupList.
 */
public class SetupListPanel extends KeyPanel<SetupList> {

    @Serial
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
    private List<SetupPanel> elementsOptionPanels;

    /**
     * Constructs a SetupListPanel for the given DataKey, DataStore, and collection of StoreDefinitions.
     *
     * @param key the DataKey
     * @param data the DataStore
     * @param definitions the collection of StoreDefinitions
     */
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

    /**
     * Initializes the choices available in the choicesBox.
     */
    private void initChoices() {

        for (var definition : definitions) {
            String setupName = definition.getName();
            choicesBox.addItem(setupName);
            choicesBoxShadow.add(setupName);
        }

    }

    /**
     * Builds the panel containing the choice controls.
     *
     * @return the constructed JPanel
     */
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
     * Adds the option from the available list to the selected list.
     * 
     * @param evt the ActionEvent triggered by the add button
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
     * @param choice the index of the choice to add
     * @return the index of the added element
     */
    public int addElement(int choice) {
        // Add index to elements
        elementsBoxShadow.add(choice);

        // Get setup options and create option file for element
        var definition = definitions.get(choice);
        var dataStore = DataStore.newInstance(definition);

        // Not sure if unique data keys are required here
        var dataKey = KeyFactory.dataStore(buildDataStoreLabel(definition.getName(), choice + 1), definition);

        var newPanel = new SetupPanel(dataKey, dataStore, definition);

        elementsOptionPanels.add(newPanel);

        // Refresh
        updateElementsComboBox();

        int elementIndex = elementsBoxShadow.size() - 1;

        // Select last item
        elementsBox.setSelectedIndex(elementIndex);

        return elementIndex;
    }

    /**
     * Updates the elements combo box with the current elements.
     */
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

    /**
     * Builds a label for a DataStore based on its definition name and number.
     *
     * @param definitionName the name of the definition
     * @param number the number of the DataStore
     * @return the constructed label
     */
    private String buildDataStoreLabel(String definitionName, int number) {
        return number + " - " + definitionName;
    }

    /**
     * Removes the option from the selected list to the available list.
     *
     * @param evt the ActionEvent triggered by the remove button
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
     * @param index the index of the element to remove
     */
    private void removeElement(int index) {

        // Check if the index is valid
        if (elementsBox.getItemCount() <= index) {
            SpecsLogs.getLogger().warning(
                    "Given index ('" + index + "')is too big. Elements size: " + elementsBox.getItemCount());
            return;
        }

        // Remove shadow index and panel
        elementsBoxShadow.remove(index);
        elementsOptionPanels.remove(index);

        // Refresh
        updateElementsComboBox();

        // Calculate new index of selected element and select it
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
     * @param e the ActionEvent triggered by the elements combo box
     */
    private void elementComboBoxActionPerformed(ActionEvent e) {
        updateSetupOptions();
    }

    /**
     * Updates the setup options panel based on the selected element.
     */
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

        repaint();
    }

    @Override
    public SetupList getValue() {
        List<DataStore> dataStores = new ArrayList<>();

        // Go to each panel collect the DataStores
        for (int i = 0; i < elementsOptionPanels.size(); i++) {
            var setupPanel = elementsOptionPanels.get(i);
            var data = setupPanel.getValue();
            // Adapt name
            var newName = elementsBox.getItemAt(i);
            var adaptedDataStore = DataStore.newInstance(newName, data);

            dataStores.add(adaptedDataStore);
        }

        return new SetupList(getKey().getName(), dataStores);
    }

    @Override
    public <ET extends SetupList> void setValue(ET value) {

        // Clear previous values
        clearElements();

        for (var dataStore : value.getDataStores()) {
            loadElement(dataStore);
        }

    }

    /**
     * Converts a setup name to its original enum representation.
     * 
     * @param setupName the setup name
     * @return the original enum name
     */
    public static String toOriginalEnum(String setupName) {

        var dashIndex = setupName.indexOf("-");
        if (dashIndex == -1) {
            return setupName;
        }

        return setupName.substring(dashIndex + 1).strip();
    }

    /**
     * Loads a single DataStore.
     * 
     * @param table the DataStore to load
     */
    private void loadElement(DataStore table) {

        // Build name
        var enumName = toOriginalEnum(table.getName());

        int setupIndex = choicesBoxShadow.indexOf(enumName);

        if (setupIndex == -1) {
            SpecsLogs.getLogger()
                    .warning("Could not find enum '" + enumName + "'. Available enums:" + choicesBoxShadow);
            return;
        }

        // Create element
        int elementsIndex = addElement(setupIndex);

        // Load values in the file
        elementsOptionPanels.get(elementsIndex).setValue(table);
    }

    /**
     * Clears all elements from the panel.
     */
    private void clearElements() {
        elementsBox.removeAllItems();

        elementsBoxShadow = new ArrayList<>();
        elementsOptionPanels = new ArrayList<>();
    }

}
