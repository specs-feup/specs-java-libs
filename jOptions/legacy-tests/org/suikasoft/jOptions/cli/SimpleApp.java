/**
 * Copyright 2013 SPeCS Research Group.
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

package org.suikasoft.jOptions.cli;

import org.suikasoft.jOptions.app.App;
import org.suikasoft.jOptions.app.AppKernel;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.persistence.XmlPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.test.storedefinitions.TestConfig;

import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.properties.SpecsProperty;

/**
 * @author Joao Bispo
 * 
 */
public class SimpleApp {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpecsSystem.programStandardInit();

        SpecsProperty.ShowStackTrace.applyProperty("true");

        StoreDefinition setupDef = new TestConfig().getStoreDefinition();
        AppPersistence persistence = new XmlPersistence(setupDef);

        App app = new GenericApp("TestApp", setupDef, persistence, newKernel());

        // AppLauncher launcher = new AppLauncher(newKernel(), "TestApp", TestOption.class,
        // persistence);

        AppLauncher launcher = new AppLauncher(app);

        launcher.launch(args);

        // JOptionsUtils.executeApp(app, Arrays.asList(args));
    }

    private static AppKernel newKernel() {
        return (options) -> {

            System.out.println("OPTIONS:" + options);

            return 0;
        };
    }

}
