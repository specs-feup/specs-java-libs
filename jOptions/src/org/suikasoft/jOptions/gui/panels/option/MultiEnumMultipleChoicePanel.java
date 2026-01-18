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

package org.suikasoft.jOptions.gui.panels.option;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;

import pt.up.fe.specs.util.SpecsCheck;

/**
 * Panel for selecting multiple enum values using combo boxes.
 *
 * <p>
 * This panel provides controls for selecting and managing multiple enum values
 * for a DataKey of type List<T>.
 *
 * @param <T> the enum type
 */
public class MultiEnumMultipleChoicePanel<T extends Enum<T>> extends KeyPanel<List<T>> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private final JComboBox<T> selectedElements;
    private final JComboBox<T> availableElements;
    private final JButton removeButton;
    private final JButton addButton;

    /**
     * Constructs a MultiEnumMultipleChoicePanel for the given DataKey and
     * DataStore.
     *
     * @param key  the DataKey
     * @param data the DataStore
     */
    public MultiEnumMultipleChoicePanel(DataKey<List<T>> key, DataStore data) {
        super(key, data);

        addButton = new JButton("Add");
        removeButton = new JButton("X");

        selectedElements = new JComboBox<>();
        availableElements = new JComboBox<>();

        // Add actions
        addButton.addActionListener(this::addButtonAction);
        removeButton.addActionListener(this::removeButtonAction);

        // Default must be defined, otherwise we are not able to populate the available
        // choices
        var defaultValues = key.getDefault().orElseThrow(
                () -> new RuntimeException("Must define a default value, otherwise we cannot obtain Enum class"));
        SpecsCheck.checkArgument(!defaultValues.isEmpty(),
                () -> "Default value must not be empty, otherwise we cannot obtain Enum class");

        for (T choice : defaultValues) {
            availableElements.addItem(choice);
        }

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(selectedElements);
        add(availableElements);
        add(addButton);
        add(removeButton);
    }

    /**
     * Returns the elements in the given combo box as a list.
     *
     * @param comboBox the combo box
     * @return the list of elements
     */
    private List<T> getElements(JComboBox<T> comboBox) {
        List<T> elements = new ArrayList<>();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            elements.add(comboBox.getItemAt(i));
        }
        return elements;
    }

    /**
     * Returns the index of the given element in the combo box.
     *
     * @param comboBox the combo box
     * @param element  the element to find
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
     * Moves an element from the source combo box to the destination combo box.
     *
     * @param element     the element to move
     * @param source      the source combo box
     * @param destination the destination combo box
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
     * Adds the option from the available list to the selected list.
     *
     * @param evt the action event
     */
    private void addButtonAction(ActionEvent evt) {
        int choice = availableElements.getSelectedIndex();
        if (choice == -1) {
            return;
        }

        moveElement(availableElements.getItemAt(choice), availableElements, selectedElements);
    }

    /**
     * Removes the option from the selected list to the available list.
     *
     * @param evt the action event
     */
    private void removeButtonAction(ActionEvent evt) {
        int choice = selectedElements.getSelectedIndex();
        if (choice == -1) {
            return;
        }

        moveElement(selectedElements.getItemAt(choice), selectedElements, availableElements);
    }

    /**
     * Returns the current value of the selected elements.
     *
     * @return the list of selected elements
     */
    @Override
    public List<T> getValue() {
        return getElements(selectedElements);
    }

    /**
     * Sets the value of the selected elements.
     *
     * @param value the list of elements to set
     * @param <ET>  the type of the list
     */
    @Override
    public <ET extends List<T>> void setValue(ET value) {
        for (var element : value) {
            moveElement(element, availableElements, selectedElements);
        }
    }
}
