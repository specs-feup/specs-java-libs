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

import java.awt.LayoutManager;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JLabel;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.Base.SetupDefinition;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.guihelper.SetupFieldOptions.SingleSetup;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.guihelper.gui.BasePanels.BaseSetupPanel;
import pt.up.fe.specs.util.SpecsLogs;

/**
 *
 * @author Joao Bispo
 */
public class IntegratedSetupPanel extends FieldPanel {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private BaseSetupPanel setupOptionsPanel;

    public IntegratedSetupPanel(SingleSetup setup) {
	// Initiallize objects
	SetupDefinition setupDefinition = setup.getSetupOptions();
	if (setupDefinition == null) {
	    SpecsLogs.msgWarn("null SetupDefinition inside '" + setup.getClass() + "'");
	} else {
	    initChoices(setupDefinition);
	}

	// initChoices(setup);

	LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
	setLayout(layout);

	add(setupOptionsPanel);
    }

    // private void initChoices(SingleSetup setup) {
    private void initChoices(SetupDefinition setupDefinition) {

	BaseSetupPanel newPanel = new BaseSetupPanel(setupDefinition);
	// BaseSetupPanel newPanel = new BaseSetupPanel(setup.getSetupOptions());

	// String labelName = setup.getSetupOptions().getSetupName();
	String labelName = setupDefinition.getSetupName();
	JLabel label = new JLabel("(" + labelName + ")");

	newPanel.add(label, 0);
	newPanel.add(new javax.swing.JSeparator(), 0);
	newPanel.add(new javax.swing.JSeparator());
	this.setupOptionsPanel = newPanel;

    }

    @Override
    public FieldType getType() {
	return FieldType.setup;
    }

    /**
     * Loads data from the raw Object in the FieldValue.
     * 
     * @param choice
     */
    @Override
    public void updatePanel(Object value) {
	SetupData newSetup = (SetupData) value;
	loadSetup(newSetup);
    }

    private void loadSetup(SetupData newSetup) {
	// Load values in the file
	setupOptionsPanel.loadValues(newSetup);
    }

    @Override
    public FieldValue getOption() {
	SetupData updatedValues = setupOptionsPanel.getMapWithValues();
	return FieldValue.create(updatedValues, getType());
    }

    @Override
    public JLabel getLabel() {
	return null;
    }

    @Override
    public Collection<FieldPanel> getPanels() {
	return setupOptionsPanel.getPanels().values();
    }

}
