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
import java.io.Serial;
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
 * Panel for editing and managing multiple choice setup panels.
 *
 * <p>
 * This panel provides controls for adding and managing multiple setup elements
 * for a MultipleSetup instance.
 */
public class MultipleChoiceSetup extends FieldPanel {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private JPanel currentOptionsPanel;
    private JPanel choicePanel;

    private JLabel label;
    private JComboBox<String> choicesBox;

    private List<String> choicesBoxNames;
    private ListOfSetupDefinitions setups;

    private List<Integer> elementsBoxShadow;
    private List<SetupData> elementsFiles;
    private List<BaseSetupPanel> elementsOptionPanels;

    /**
     * Constructs a MultipleChoiceSetup for the given enum option, label, and
     * MultipleSetup.
     *
     * @param enumOption the SetupFieldEnum
     * @param labelName  the label for the panel
     * @param setup      the MultipleSetup instance
     */
    public MultipleChoiceSetup(SetupFieldEnum enumOption, String labelName, MultipleSetup setup) {
        // Initialize objects
        label = new JLabel(labelName + ":");

        initChoices(setup);
        initElements();
        // Add choices
        for (int i = 0; i < choicesBox.getItemCount(); i++) {
            addElement(i);
        }

        // Add actions
        choicesBox.addActionListener(this::choiceComboBoxActionPerformed);

        // Build choice panel
        choicePanel = buildChoicePanel();

        currentOptionsPanel = null;

        LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(layout);
        add(choicePanel);

    }

    /**
     * Initializes the choices for the MultipleSetup.
     *
     * @param setupList the MultipleSetup instance
     */
    private void initChoices(MultipleSetup setupList) {
        setups = setupList.getSetups();

        choicesBox = new JComboBox<>();
        choicesBoxNames = new ArrayList<>();

        for (SetupDefinition setup : setups.getSetupKeysList()) {
            String setupName = setup.getSetupName();
            choicesBox.addItem(setupName);
            choicesBoxNames.add(setupName);
        }

    }

    /**
     * Initializes the elements for the setup panel.
     */
    private void initElements() {
        elementsBoxShadow = new ArrayList<>();
        elementsFiles = new ArrayList<>();
        elementsOptionPanels = new ArrayList<>();
    }

    /**
     * Builds the choice panel for the setup.
     *
     * @return the constructed JPanel
     */
    private JPanel buildChoicePanel() {
        JPanel panel = new JPanel();

        panel.add(label);
        panel.add(choicesBox);

        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

        return panel;
    }

    /**
     * Updates the options panel based on the selected choice.
     *
     * @param e the ActionEvent triggered by the JComboBox
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
     * @param choice the index of the choice to add
     * @return the index of the added element
     */
    private int addElement(int choice) {
        // Add index to elements
        elementsBoxShadow.add(choice);

        // Get setup options and create option file for element
        SetupDefinition setupKeys = setups.getSetupKeysList().get(choice);

        elementsFiles.add(SetupData.create(setupKeys));

        BaseSetupPanel newPanel = new BaseSetupPanel(setupKeys);
        elementsOptionPanels.add(newPanel);

        return elementsBoxShadow.size() - 1;
    }

    /**
     * Updates the panel with the given value.
     *
     * @param value the value to update the panel with
     */
    @Override
    public void updatePanel(Object value) {
        ListOfSetups maps = (ListOfSetups) value;

        for (SetupData key : maps.getMapOfSetups()) {
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
     * @param setupData the SetupData to load
     */
    private void loadSetup(SetupData setupData) {
        // Build name
        String enumName = setupData.getSetupName();

        int setupIndex = choicesBoxNames.indexOf(enumName);

        if (setupIndex == -1) {
            SpecsLogs.warn("Could not find enum '" + enumName + "'. Available enums:" + setups);
            return;
        }

        elementsFiles.set(setupIndex, setupData);
        elementsOptionPanels.get(setupIndex).loadValues(setupData);

    }

    /**
     * Updates the setup options panel.
     */
    private void updateSetupOptions() {
        if (currentOptionsPanel != null) {
            remove(currentOptionsPanel);
            currentOptionsPanel = null;
        }

        int index = choicesBox.getSelectedIndex();

        if (index != -1) {
            currentOptionsPanel = elementsOptionPanels.get(index);
            add(currentOptionsPanel);
            currentOptionsPanel.revalidate();
        }

        repaint();
    }

    /**
     * Retrieves the setups managed by this panel.
     *
     * @return the ListOfSetups instance
     */
    public ListOfSetups getSetups() {
        List<SetupData> listOfSetups = new ArrayList<>();

        for (BaseSetupPanel elementsOptionPanel : elementsOptionPanels) {
            listOfSetups.add(elementsOptionPanel.getMapWithValues());
        }

        ListOfSetups currentSetups = new ListOfSetups(listOfSetups);

        int choice = choicesBox.getSelectedIndex();
        if (choice == -1) {
            SpecsLogs.warn("Could not get index of selected setup.");
            return null;
        }
        currentSetups.setPreferredIndex(choice);

        return currentSetups;

    }

    @Override
    public FieldValue getOption() {
        return FieldValue.create(getSetups(), getType());
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
