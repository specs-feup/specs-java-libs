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

package pt.up.fe.specs.guihelper.gui.BasePanels;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import pt.up.fe.specs.guihelper.GlobalOptionsUtils;
import pt.up.fe.specs.guihelper.Base.SetupDefinition;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.guihelper.gui.AppFrame;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.guihelper.gui.FieldPanels.SetupPanel;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Panel which loads and can edit global options, not tied to a particular file.
 * 
 * @author Joao Bispo
 */
public class GlobalOptionsPanel extends GuiTab {

    private static final long serialVersionUID = 1L;

    private BaseSetupPanel appFilePanel;
    private final Class<? extends SetupFieldEnum> globalSetup;
    private List<SetupPanel> setupPanels;
    // private SetupData optionFile = null;

    public GlobalOptionsPanel(Class<? extends SetupFieldEnum> globalSetup) {
	// public GlobalOptionsPanel(SetupDefinition enumKeys) {
	this.globalSetup = globalSetup;

	SetupDefinition enumKeys = SetupDefinition.create(globalSetup);
	JComponent optionsPanel = initEnumOptions(enumKeys);
	initSetupPanels();

	// SetupData newSetup = SetupData.create(enumKeys);
	updateValues();
	// assignNewOptionFile(newSetup);

	setLayout(new BorderLayout(5, 5));
	add(optionsPanel, BorderLayout.CENTER);

    }

    /**
     * Find setup panels and adds them to the list
     */
    private void initSetupPanels() {
	setupPanels = new ArrayList<>();
	Map<String, FieldPanel> panelMap = appFilePanel.getPanels();
	for (String key : panelMap.keySet()) {
	    SetupPanel setupPanel = getSetupPanel(panelMap.get(key));
	    if (setupPanel == null) {
		continue;
	    }
	    setupPanels.add(setupPanel);
	}
    }

    private static SetupPanel getSetupPanel(FieldPanel panel) {
	Class<?>[] interfaces = panel.getClass().getInterfaces();
	Set<Class<?>> classSet = new HashSet<>(
		Arrays.asList(interfaces));
	if (!classSet.contains(SetupPanel.class)) {
	    return null;
	}

	return (SetupPanel) panel;
    }

    public void updateValues() {
	SetupData newMap = GlobalOptionsUtils.loadData(globalSetup);
	// SetupData newMap = GuiHelperUtils.loadData(file);
	if (newMap == null) {
	    SpecsLogs.getLogger().warning(
		    "Could not load global options from '" + globalSetup.getName() + "'");
	    return;
	}

	// Load file
	// assignNewOptionFile(newMap);

	// appFilePanel.loadValues(optionFile);
	appFilePanel.loadValues(newMap);
	// saveButton.setEnabled(true);
	// updateFileInfoString();
    }

    private JComponent initEnumOptions(SetupDefinition keys) {
	appFilePanel = new BaseSetupPanel(keys);

	JScrollPane scrollPane = new JScrollPane();

	scrollPane.setPreferredSize(new Dimension(
		AppFrame.PREFERRED_WIDTH + 10, AppFrame.PREFERRED_HEIGHT + 10));
	scrollPane.setViewportView(appFilePanel);

	return scrollPane;

    }

    /*
     * Sets the current option file to the given file.
     */
    /*
    private void updateFile() {
    updateInternalMap();
    }
    */
    /*
    private void updateInternalMap() {
    	// Get info from panels
    	SetupData updatedMap = appFilePanel.getMapWithValues();
    	// Update internal optionfile
    	optionFile = updatedMap;
    }
    */
    /**
     * Can only be called after setup panels are initallized.
     * 
     * @param newOptionFile
     */
    /*
    private void assignNewOptionFile(SetupData newOptionFile) {
    optionFile = newOptionFile;
    }
    */

    /**
     * Reads the values from the global options and updates the fields.
     */
    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.gui.BasePanels.GuiTab#enterTab(pt.up.fe.specs.guihelper.Gui.BasePanels.TabData)
     */
    @Override
    public void enterTab(TabData data) {
	updateValues();
	/*
	File outputFile = data.getConfigFile();
	if(outputFile == null) {
	    return;
	}
	
	if (outputFile.exists()) {
	   this.setOutputFile(outputFile);
	}
	if (outputFile.isFile()) {
	   this.updateValues(outputFile.getPath());
	}
	*/
    }

    /**
     * Stores the values in the fields to the global options.
     */
    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.gui.BasePanels.GuiTab#exitTab(pt.up.fe.specs.guihelper.Gui.BasePanels.TabData)
     */
    @Override
    public void exitTab(TabData data) {
	// data.setConfigFile(this.getOutputFile());
	// Get info from panels
	SetupData updatedMap = appFilePanel.getMapWithValues();
	GlobalOptionsUtils.saveData(globalSetup, updatedMap);
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.gui.BasePanels.GuiTab#getTabName()
     */
    @Override
    public String getTabName() {
	return "Global Options";
    }

}
