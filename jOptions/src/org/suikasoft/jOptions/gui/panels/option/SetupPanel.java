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

import java.awt.LayoutManager;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.KeyPanel;
import org.suikasoft.jOptions.gui.panels.app.BaseSetupPanel;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 *
 * @author Joao Bispo
 */
public class SetupPanel extends KeyPanel<DataStore> {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private JPanel currentOptionsPanel;

    private final BaseSetupPanel setupOptionsPanel;

    public SetupPanel(DataKey<DataStore> key, DataStore data, StoreDefinition definition) {
	super(key, data);

	// Initiallize objects
	// newPanel.add(new javax.swing.JSeparator(),0);
	// newPanel.add(new javax.swing.JSeparator());
	setupOptionsPanel = new BaseSetupPanel(definition, data);

	// Add actions
	/*
	checkBoxShow.addActionListener(new ActionListener() {
	   @Override
	   public void actionPerformed(ActionEvent e) {
	      showButtonActionPerformed(e);
	   }
	
	});
	 *
	 */

	// Build choice panel
	// choicePanel = buildChoicePanel();

	currentOptionsPanel = null;

	// setLayout(new BorderLayout(5, 5));
	// add(choicePanel, BorderLayout.PAGE_START);
	LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
	setLayout(layout);
	// add(choicePanel);
	updateSetupOptions();
    }

    /**
     * Loads the several elements from a DataStore.
     */
    @Override
    public <ET extends DataStore> void setValue(ET value) {
	// Load values
	setupOptionsPanel.loadValues(value);
    }

    private void updateSetupOptions() {
	/*
	boolean show = checkBoxShow.isSelected();
	
	if(!show) {
	   remove(currentOptionsPanel);
	   currentOptionsPanel = null;
	   revalidate();
	   //repaint();
	   return;
	}
	 *
	 */

	if (currentOptionsPanel != null) {
	    remove(currentOptionsPanel);
	    currentOptionsPanel = null;
	}

	currentOptionsPanel = setupOptionsPanel;
	add(currentOptionsPanel);
	currentOptionsPanel.revalidate();

	// TODO: Is it repaint necessary here, or revalidate on panel solves it?
	// repaint();
	// System.out.println("SetupPanel Repainted");
    }

    @Override
    public DataStore getValue() {
	return setupOptionsPanel.getData();
    }

    @Override
    public Collection<KeyPanel<?>> getPanels() {
	return setupOptionsPanel.getPanels().values();
    }

}
