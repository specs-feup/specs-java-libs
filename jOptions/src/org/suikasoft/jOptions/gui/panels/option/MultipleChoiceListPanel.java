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
 * TODO: Keep order as given by the original elements.
 * 
 * @author Joao Bispo
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

    // private final Collection<T> availableChoices;

    public MultipleChoiceListPanel(DataKey<List<T>> key, DataStore data) {
        super(key, data);

        addButton = new JButton("Add");
        removeButton = new JButton("X");
        addAllButton = new JButton("Add All");
        removeAllButton = new JButton("X All");

        selectedElements = new JComboBox<>();

        // comboBoxValues = new JComboBox<String>();
        availableElements = new JComboBox<>();

        // Add actions
        addButton.addActionListener(this::addButtonAction);
        removeButton.addActionListener(this::removeButtonAction);
        addAllButton.addActionListener(this::addAllButtonAction);
        removeAllButton.addActionListener(this::removeAllButtonAction);

        // ExtraData must be defined, otherwise we are not able to populate the avaliable choices
        var extraData = key.getExtraData()
                .orElseThrow(() -> new RuntimeException("Key '" + key.getName() + "' must define extra data"));

        @SuppressWarnings("unchecked")
        List<T> defaultValues = (List<T>) extraData.get(MultipleChoiceListKey.AVAILABLE_CHOICES);

        // var defaultValues = key.getAvailableChoices();
        // var defaultValues = key.getDefault().orElseThrow(
        // () -> new RuntimeException("Must define a default value, otherwise we cannot obtain Enum class"));
        // SpecsCheck.checkArgument(!defaultValues.isEmpty(),
        // () -> "Default value must not be empty, otherwise we cannot obtain Enum class");

        // @SuppressWarnings("unchecked")
        // T[] enumConstants = ((Class<T>) defaultEnum.getClass()).getEnumConstants();

        // availableChoices = new HashSet<>(Arrays.asList(enumConstants));

        for (T choice : defaultValues) {
            availableElements.addItem(choice);
        }

        // Check if there is a default value
        // if (getKey().getDefault().isPresent()) {
        // for (var defaultElement : getKey().getDefault().get()) {
        // System.out.println("ADDING DEFAULT: " + defaultElement);
        // addElement(defaultElement);
        // }
        // }

        setLayout(new FlowLayout(FlowLayout.LEFT));
        add(selectedElements);
        add(availableElements);
        add(addButton);
        add(removeButton);
        add(addAllButton);
        add(removeAllButton);
    }

    private List<T> getElements(JComboBox<T> comboBox) {
        List<T> elements = new ArrayList<>();
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            elements.add(comboBox.getItemAt(i));
        }
        return elements;
    }

    private int indexOf(JComboBox<T> comboBox, T element) {
        for (int i = 0; i < comboBox.getItemCount(); i++) {
            if (element.equals(comboBox.getItemAt(i))) {
                return i;
            }
        }
        return -1;
    }

    private void moveElement(T element, JComboBox<T> source, JComboBox<T> destination) {
        // Check if element is present is available choices
        // var available = getElements(availableElements);
        // int elementIndex = available.indexOf(element);

        int elementIndex = indexOf(source, element);
        if (elementIndex == -1) {
            // SpecsLogs.warn("Could not find element: " + element);
            return;
        }

        destination.addItem(element);
        source.removeItemAt(elementIndex);
        // availableElements.getIt
    }

    /**
     * Adds the option from the avaliable list to selected list.
     * 
     * @param evt
     */
    private void addButtonAction(ActionEvent evt) {
        // Determine what element is selected
        int choice = availableElements.getSelectedIndex();
        if (choice == -1) {
            return;
        }

        moveElement(availableElements.getItemAt(choice), availableElements, selectedElements);
    }

    /**
     * Removes the option from the selected list to the available list.
     * 
     * @param evt
     */
    private void removeButtonAction(ActionEvent evt) {
        // Determine what element is selected
        int choice = selectedElements.getSelectedIndex();
        if (choice == -1) {
            return;
        }

        moveElement(selectedElements.getItemAt(choice), selectedElements, availableElements);
    }

    /**
     * Moves all options from the avaliable list to selected list.
     * 
     * @param evt
     */
    private void addAllButtonAction(ActionEvent evt) {
        while (availableElements.getItemCount() > 0) {
            moveElement(availableElements.getItemAt(0), availableElements, selectedElements);
        }
    }

    /**
     * Moves all options from the selected list to the available list.
     * 
     * @param evt
     */
    private void removeAllButtonAction(ActionEvent evt) {
        while (selectedElements.getItemCount() > 0) {
            moveElement(selectedElements.getItemAt(0), selectedElements, availableElements);
        }
    }

    @Override
    public List<T> getValue() {
        return getElements(selectedElements);
    }

    @Override
    public <ET extends List<T>> void setValue(ET value) {
        for (var element : value) {
            moveElement(element, availableElements, selectedElements);
        }
    }

}
