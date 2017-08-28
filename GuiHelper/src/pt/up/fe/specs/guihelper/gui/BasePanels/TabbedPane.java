/*
 * Copyright 2010 SPeCS Research Group.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License. under the License.
 */

package pt.up.fe.specs.guihelper.gui.BasePanels;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pt.up.fe.specs.guihelper.App;
import pt.up.fe.specs.guihelper.AppUsesGlobalOptions;

/**
 * Panel which contains the principal panels of the program and coordinates
 * updates between panels.
 * 
 * @author Joao Bispo
 */
public class TabbedPane extends JPanel {

    private static final long serialVersionUID = 1L;

    private TabData tabData;
    private List<GuiTab> tabs;
    private GuiTab currentTab = null;

    public TabbedPane(App application) {
	super(new GridLayout(1, 1));

	tabData = new TabData();
	tabs = new ArrayList<>();

	JTabbedPane tabbedPane = new JTabbedPane();

	// New program panel
	ProgramPanel programPanel = new ProgramPanel(application);

	// Add program tab
	tabs.add(programPanel);

	// New options panel
	OptionsPanel optionsPanel = new OptionsPanel(application.getEnumKeys());
	tabs.add(optionsPanel);

	// Check if program uses global options
	//if(application.getClass().isInstance(AppUsesGlobalOptions.class)) {
	if(AppUsesGlobalOptions.class.isInstance(application)) {
	    GlobalOptionsPanel globalPanel = new GlobalOptionsPanel(
	    ((AppUsesGlobalOptions) application).getGlobalOptions());
	    
	    tabs.add(globalPanel);    
	} 
	/*
	else {
	    System.out.println("Not..."+Arrays.toString(application.getClass().getInterfaces()));
	}
	*/
	
	int baseMnemonic = KeyEvent.VK_1;
	int currentIndex = 0;
	for (GuiTab tab : tabs) {
	    tabbedPane.addTab(tab.getTabName(), tab);
	    tabbedPane.setMnemonicAt(currentIndex, baseMnemonic + currentIndex);
	    currentIndex += 1;
	}

	// Register a change listener
	tabbedPane.addChangeListener(new ChangeListener() {
	    // This method is called whenever the selected tab changes

	    @Override
	    public void stateChanged(ChangeEvent evt) {
		JTabbedPane pane = (JTabbedPane) evt.getSource();

		// Get selected tab
		int sel = pane.getSelectedIndex();

		// Exit current tab
		currentTab.exitTab(tabData);
		// Update current tab
		currentTab = tabs.get(sel);
		// Enter current tab
		currentTab.enterTab(tabData);
	    }
	});

	// Set program panel as currentTab
	currentTab = programPanel;
	currentTab.enterTab(tabData);

	// Add the tabbed pane to this panel.
	add(tabbedPane);

	// The following line enables to use scrolling tabs.
	tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

    }

}
