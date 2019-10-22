/**
 * Copyright 2016 SPeCS.
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

package org.suikasoft.jOptions.test;

import javax.swing.JPanel;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.panels.app.BaseSetupPanel;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.test.keys.TestKeys;

import pt.up.fe.specs.util.SpecsSwing;

public class PanelRun {

    public static void main(String[] args) {
        StoreDefinition definition = StoreDefinition.newInstance("test_panel",
                TestKeys.A_BOOLEAN,
                TestKeys.A_STRING,
                TestKeys.A_MULTI_ENUM);

        JPanel panel = new BaseSetupPanel(definition, DataStore.newInstance("dummy_data"));
        SpecsSwing.showPanel(panel, "Test");

        // App.newInstance("Test App", definition, XmlPer)
    }

}
