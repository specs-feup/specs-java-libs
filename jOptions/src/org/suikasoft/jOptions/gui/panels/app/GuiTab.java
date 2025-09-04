/**
 * Copyright 2012 SPeCS Research Group.
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

import javax.swing.JPanel;

import org.suikasoft.jOptions.Interfaces.DataStore;

import java.io.Serial;

/**
 * Abstract base class for tabs in the application GUI.
 *
 * <p>This class provides a contract for tabs that interact with a DataStore and require enter/exit lifecycle methods.
 *
 * @author Joao Bispo
 */
public abstract class GuiTab extends JPanel {

    private final DataStore data;

    /**
     * Constructs a GuiTab with the given DataStore.
     *
     * @param data the DataStore for the tab
     */
    public GuiTab(DataStore data) {
        this.data = data;
    }

    /**
     * Returns the DataStore associated with this tab.
     *
     * @return the DataStore
     */
    public DataStore getData() {
        return data;
    }

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Called when entering the tab.
     */
    public abstract void enterTab();

    /**
     * Called when exiting the tab.
     */
    public abstract void exitTab();

    /**
     * Returns the name of the tab.
     *
     * @return the tab name
     */
    public abstract String getTabName();

}
