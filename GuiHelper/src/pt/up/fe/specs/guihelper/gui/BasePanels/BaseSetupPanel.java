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

package pt.up.fe.specs.guihelper.gui.BasePanels;

import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pt.up.fe.specs.guihelper.Base.SetupDefinition;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.Base.SetupFieldUtils;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.guihelper.gui.FieldPanel;
import pt.up.fe.specs.guihelper.gui.FieldPanels.PanelUtils;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Panel which will contain the options
 *
 * @author Joao Bispo
 */
public class BaseSetupPanel extends JPanel implements ComponentListener {

    private static final long serialVersionUID = 1L;

    private Map<String, FieldPanel> panels;
    private SetupDefinition setupDefinition;
    // private final int identationLevel;

    public static final int IDENTATION_SIZE = 6;

    public BaseSetupPanel(SetupDefinition keys) {
	this(keys, 0);
    }

    public BaseSetupPanel(SetupDefinition keys, int identationLevel) {
	this.setupDefinition = keys;
	// this.identationLevel = identationLevel;
	panels = new HashMap<>();

	// Extract the enum values
	LayoutManager layout = new BoxLayout(this, BoxLayout.Y_AXIS);
	setLayout(layout);

	if (keys == null) {
	    throw new RuntimeException("SetupDefinition is null.");
	    // LoggingUtils.getLogger().warning("SetupDefinition is null.");
	}

	List<FieldPanel> panelsWithLabels = new ArrayList<>();
	int maxWidth = 0;

	for (SetupFieldEnum key : keys.getSetupKeys()) {
	    // FieldPanel panel = PanelUtils.newPanel(key);
	    FieldPanel panel = PanelUtils.newPanel(key);
	    if (panel == null) {
		SpecsLogs.warn("Could not create panel for option '" + key + "'");
		continue;
	    }

	    // Update values of new panel with default/blank values
	    FieldValue newValue = SetupFieldUtils.newValue(key);
	    panel.updatePanel(newValue.getRawValue());

	    add(panel);
	    String stringKey = key.name();
	    panels.put(stringKey, panel);

	    if (panel.getLabel() != null) {
		panelsWithLabels.add(panel);
		// Add identation to label
		String labelText = panel.getLabel().getText();
		int numberOfSpaces = identationLevel * IDENTATION_SIZE;
		panel.getLabel().setText(SpecsStrings.padLeft(labelText, labelText.length() + numberOfSpaces, ' '));
		// maxWidth = Math.max(maxWidth, panel.getLabel().getWidth());
		// Get max width
		maxWidth = Math.max(maxWidth, panel.getLabel().getPreferredSize().width);
	    }
	}

	// Change panels labels width to the maximum size
	for (FieldPanel panel : panelsWithLabels) {
	    JLabel label = panel.getLabel();
	    Dimension previousSize = label.getPreferredSize();
	    // System.out.println("Label:"+label.getText());
	    // System.out.println("Previous Width:"+label.getPreferredSize().width);
	    // System.out.println("New Width:"+maxWidth);

	    // label.setSize(maxWidth, label.getHeight());
	    label.setPreferredSize(new Dimension(maxWidth, previousSize.height));
	}

	addComponentListener(this);
    }

    public Map<String, FieldPanel> getPanels() {
	return panels;
    }

    public void loadValues(SetupData map) {
	if (map == null) {
	    return;
	}
	for (String key : map.keySet()) {
	    FieldValue value = map.get(key);

	    // Get panel
	    FieldPanel panel = panels.get(key);
	    if (panel == null) {
		SpecsLogs.warn("Could not find panel for option '" + key + "'.");
		continue;
	    }

	    Object rawValue = value.getRawValue();
	    panel.updatePanel(rawValue);
	}

    }

    /**
     * Collects information in all the panels and returns a map with the information.
     *
     * @return
     */
    public SetupData getMapWithValues() {
	SetupData updatedMap = SetupData.create(setupDefinition);

	for (String key : panels.keySet()) {
	    FieldPanel panel = panels.get(key);
	    FieldValue value = panel.getOption();
	    if (value == null) {
		SpecsLogs.warn("value is null.");
		// No valid value for the table
		continue;
	    }
	    updatedMap.put(key, value);
	}

	return updatedMap;
    }

    @Override
    public void componentResized(ComponentEvent e) {
	// Iterate over the panels and call resize
	// Rectangle r = e.getComponent().getBounds();
	// System.out.println("RESIZED! " + r);

	for (FieldPanel panel : panels.values()) {
	    panel.componentResized(e);
	}

    }

    @Override
    public void componentMoved(ComponentEvent e) {
	// System.out.println("MOVED!");

    }

    @Override
    public void componentShown(ComponentEvent e) {
	// System.out.println("SHOWN!");

    }

    @Override
    public void componentHidden(ComponentEvent e) {
	// System.out.println("HIDDEN");

    }
}
