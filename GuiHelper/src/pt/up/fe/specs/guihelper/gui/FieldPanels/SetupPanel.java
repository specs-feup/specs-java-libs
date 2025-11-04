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

import java.awt.LayoutManager;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.guihelper.SetupFieldOptions.SingleSetup;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.guihelper.gui.BasePanels.BaseSetupPanel;

/**
 *
 * @author Joao Bispo
 */
public class SetupPanel extends FieldPanel {

    private static final long serialVersionUID = 1L;

    /**
     * INSTANCE VARIABLES
     */
    private JPanel currentOptionsPanel;

    private BaseSetupPanel setupOptionsPanel;

    public SetupPanel(SetupFieldEnum enumOption, String labelName, SingleSetup setup) {
	// Initiallize objects

	initChoices(setup);

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

    private void initChoices(SingleSetup setup) {
	BaseSetupPanel newPanel = new BaseSetupPanel(setup.getSetupOptions());
	// newPanel.add(new javax.swing.JSeparator(),0);
	// newPanel.add(new javax.swing.JSeparator());
	this.setupOptionsPanel = newPanel;

    }

    /*
       private JPanel buildChoicePanel() {
          JPanel panel = new JPanel();

          panel.add(label);
          //panel.add(checkBoxShow);

          panel.setLayout(new FlowLayout(FlowLayout.LEFT));

          return panel;
       }
    */

    /**
     * Toggles between showing or not showing setup options.
     * 
     * @param e
     */
    /*
    private void showButtonActionPerformed(ActionEvent e) {
       updateSetupOptions();
    }
     *
     */

    @Override
    public FieldType getType() {
	return FieldType.setup;
    }

    /**
     * Loads several elements from an AppValue.
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
