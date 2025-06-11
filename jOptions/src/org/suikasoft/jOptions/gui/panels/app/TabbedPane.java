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

package org.suikasoft.jOptions.gui.panels.app;

import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.App;

/**
 * Panel which contains the principal panels of the program and coordinates updates between panels.
 *
 * <p>This panel manages the main tabs of the application, including program and options panels.
 * 
 * @author Joao Bispo
 */
public class TabbedPane extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final DataKey<String> APP_NAME = KeyFactory.string("tabbed pane app name");

    /**
     * Returns the DataKey for the application name.
     *
     * @return the DataKey for the app name
     */
    public static DataKey<String> getAppNameKey() {
        return APP_NAME;
    }

    private final DataStore tabData;
    private final List<GuiTab> tabs;
    private GuiTab currentTab = null;

    private final OptionsPanel optionsPanel;

    /**
     * Constructs a TabbedPane for the given application.
     *
     * @param application the application to display
     */
    public TabbedPane(App application) {
        super(new GridLayout(1, 1));

        tabData = DataStore.newInstance("tab data");

        tabData.add(APP_NAME, application.getName());

        tabs = new ArrayList<>();
        // Preferences.
        JTabbedPane tabbedPane = new JTabbedPane();

        // New program panel
        ProgramPanel programPanel = new ProgramPanel(application, tabData);

        // Add program tab
        tabs.add(programPanel);

        // New options panel
        optionsPanel = new OptionsPanel(application, tabData);
        tabs.add(optionsPanel);

        // Add other panels
        for (TabProvider provider : application.getOtherTabs()) {
            tabs.add(provider.getTab(tabData));
        }

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
                currentTab.exitTab();
                // Update current tab
                currentTab = tabs.get(sel);
                // Enter current tab
                currentTab.enterTab();
            }
        });

        // Set program panel as currentTab
        currentTab = programPanel;
        currentTab.enterTab();

        // Add the tabbed pane to this panel.
        add(tabbedPane);

        // The following line enables to use scrolling tabs.
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

    }

    /**
     * Returns the options panel associated with this TabbedPane.
     *
     * @return the options panel
     */
    public OptionsPanel getOptionsPanel() {
        return optionsPanel;
    }

}
