/**
 * Copyright 2017 SPeCS.
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.suikasoft.jOptions.GenericImplementations.DummyPersistence;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.App;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.test.storedefinitions.TestConfig;

public class CliTester {

    private final List<Supplier<String>> cliArgs;
    private final List<Consumer<DataStore>> cliTests;

    public CliTester() {
        this.cliArgs = new ArrayList<>();
        this.cliTests = new ArrayList<>();
    }

    public void addTest(Supplier<String> arg, Consumer<DataStore> test) {
        this.cliArgs.add(arg);
        this.cliTests.add(test);
    }

    public void test() {

        // Collect arguments
        List<String> args = this.cliArgs.stream().map(Supplier::get).collect(Collectors.toList());

        // Create and launch app
        TestKernel kernel = new TestKernel();

        StoreDefinition setupDef = new TestConfig().getStoreDefinition();
        AppPersistence persistence = new DummyPersistence(setupDef);

        App app = new GenericApp("TestApp", setupDef, persistence, kernel);

        AppLauncher launcher = new AppLauncher(app);
        // AppLauncher launcher = new AppLauncher(kernel, "TestApp", TestOption.class, persistence);

        // SimpleApp.main(args.toArray(new String[args.size()]));
        launcher.launch(args);

        // Get options, verify contents
        DataStore setup = kernel.getSetup();

        // Test parsing
        this.cliTests.forEach(test -> test.accept(setup));
    }

}
