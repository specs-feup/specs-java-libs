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

package org.suikasoft.jOptions.gui.panels.option;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.customkeys.MultipleChoiceListKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

/**
 * Panel for selecting multiple values from a list of choices.
 *
 * <p>
 * This panel provides controls for adding, removing, and managing multiple
 * choices for a DataKey of type List<T>.
 *
 * TODO: Keep order as given by the original elements.
 * 
 * @param <T> the type of value handled by the panel
 */
public class MultipleChoiceListPanel<T> extends KeyPanel<List<T>> {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JComboBox<T> selectedElements;
    private final JComboBox<T> availableElements;
    private final JButton addButton;
    private final JButton removeButton;
    private final JButton addAllButton;
    private final JButton removeAllButton;

    /**
     * Constructs a MultipleChoiceListPanel for the given DataKey and DataStore.
     *
     * @param key the DataKey
     * @param data the DataStore
     */
    public MultipleChoiceListPanel(DataKey<List<T>> key, DataStore data) {
        super(key, data);

        addButton = new JButton("Add");
        removeButton = new JButton("X");
        addAllButton = new JButton("Add All");
        removeAllButton = new JButton("X All");

        selectedElements = new JComboBox<>();
        availableElements = new JComboBox<>();

        // Add actions
        addButton.addActionListener(this::addButtonAction);
        removeButton.addActionListener(this::removeButtonAction);
        addAllButton.addActionListener(this::addAllButtonAction);
        removeAllButton.addActionListener(this::removeAllButtonAction);

        // ExtraData must be defined, otherwise we are not able to populate the available choices
        var extraData = key.getExtraData()
                .orElseThrow(() -> new RuntimeException("Key '" + key.getName() + "' must define extra data"));

        @SuppressWarnings("unchecked")
        List<T> defaultValues = extraData.get(MultipleChoiceListKey.AVAILABLE_CHOICES);

        for (T choice : defaultValues) {
            availableElements.addItem(choice);
        }

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(selectedElements);
        add(availableElements);
        add(addButton);
        add(removeButton);
        add(addAllButton);
        add(removeAllButton);
    }

    /**
     * Retrieves all elements from the given JComboBox.
     *
     * @param comboBox the JComboBox to retrieve elements from
     * @return a list of elements in the JComboBox
     */
    private List<T> getElements(JComboBox<T> comboBox) {
        List<T> elements = new ArrayList<>();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            elements.add(comboBox.getItemAt(i));
        }
        return elements;
    }

    /**
     * Finds the index of the given element in the JComboBox.
     *
     * @param comboBox the JComboBox to search
     * @param element the element to find
     * @return the index of the element, or -1 if not found
     */
    private int indexOf(JComboBox<T> comboBox, T element) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (element.equals(comboBox.getItemAt(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Moves an element from the source JComboBox to the destination JComboBox.
     *
     * @param element the element to move
     * @param source the source JComboBox
     * @param destination the destination JComboBox
     */
    private void moveElement(T element, JComboBox<T> source, JComboBox<T> destination) {
        int elementIndex = indexOf(source, element);
        if (elementIndex == -1) {
            return;
        }

        destination.addItem(element);
        source.removeItemAt(elementIndex);
    }

    /**
     * Adds the selected option from the available list to the selected list.
     *
     * @param evt the ActionEvent triggered by the button
     */
    private void addButtonAction(ActionEvent evt) {
        int choice = availableElements.getSelectedIndex();
        if (choice == -1) {
            return;
        }

        moveElement(availableElements.getItemAt(choice), availableElements, selectedElements);
    }

    /**
     * Removes the selected option from the selected list to the available list.
     *
     * @param evt the ActionEvent triggered by the button
     */
    private void removeButtonAction(ActionEvent evt) {
        int choice = selectedElements.getSelectedIndex();
        if (choice == -1) {
            return;
        }

        moveElement(selectedElements.getItemAt(choice), selectedElements, availableElements);
    }

    /**
     * Moves all options from the available list to the selected list.
     *
     * @param evt the ActionEvent triggered by the button
     */
    private void addAllButtonAction(ActionEvent evt) {
        while (availableElements.getItemCount() > 0) {
            moveElement(availableElements.getItemAt(0), availableElements, selectedElements);
        }
    }

    /**
     * Moves all options from the selected list to the available list.
     *
     * @param evt the ActionEvent triggered by the button
     */
    private void removeAllButtonAction(ActionEvent evt) {
        while (selectedElements.getItemCount() > 0) {
            moveElement(selectedElements.getItemAt(0), selectedElements, availableElements);
        }
    }

    /**
     * Retrieves the current value of the panel.
     *
     * @return a list of selected elements
     */
    @Override
    public List<T> getValue() {
        return getElements(selectedElements);
    }

    /**
     * Sets the value of the panel.
     *
     * @param value the list of elements to set as selected
     */
    @Override
    public <ET extends List<T>> void setValue(ET value) {
        for (var element : value) {
            moveElement(element, availableElements, selectedElements);
        }
    }

}
