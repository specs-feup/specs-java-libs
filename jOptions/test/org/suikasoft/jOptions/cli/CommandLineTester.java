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

import static org.junit.Assert.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.suikasoft.jOptions.GenericImplementations.DummyPersistence;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.App;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.test.keys.AnotherTestKeys;
import org.suikasoft.jOptions.test.keys.TestKeys;
import org.suikasoft.jOptions.test.storedefinitions.InnerOptions2;
import org.suikasoft.jOptions.test.storedefinitions.TestConfig;
import org.suikasoft.jOptions.test.values.MultipleChoices;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.properties.SpecsProperty;
import pt.up.fe.specs.util.utilities.StringList;

/**
 * @author Joao Bispo
 * 
 */
public class CommandLineTester {

    @BeforeClass
    public static void runBeforeClass() {
	SpecsSystem.programStandardInit();

	SpecsProperty.ShowStackTrace.applyProperty("true");
    }

    @Test
    public void test() {
	// Create arguments
	List<String> args = SpecsFactory.newArrayList();

	// String arg
	String stringValue = "test_string";
	String stringArg = TestKeys.A_STRING.getName() + "=" + stringValue;
	args.add(stringArg);

	// Boolean arg
	Boolean booleanValue = Boolean.TRUE;
	String booleanArg = TestKeys.A_BOOLEAN.getName() + "=" + booleanValue.toString();
	args.add(booleanArg);

	// StringList arg
	StringList stringListValue = new StringList(Arrays.asList("list1", "list2"));
	String stringListArg = TestKeys.A_STRINGLIST.getName() + "=" + "list1;list2";
	args.add(stringListArg);

	// FileList
	String fileListName = TestKeys.A_FILELIST.getName();

	// FileList values
	String folderName = "testFolder";
	String fileListValues = folderName + ";file1.txt;file2.txt";

	String fileListArg = fileListName + "=" + fileListValues;
	args.add(fileListArg);
	// A folder arg

	// String folderArg = fileListName + "/"
	// + FileList.getFolderOptionName() + "=" + folderName;
	// args.add(folderArg);
	//
	// // Files arg
	// String filenames = "file1.txt;file2.txt";
	// String filesArg = fileListName + "/"
	// + FileList.getFilesOptionName() + "=" + filenames;
	// args.add(filesArg);

	// Inner String arg
	String innerString = "inner_string";
	String innerArg = TestKeys.A_SETUP.getName() + "/" + AnotherTestKeys.ANOTHER_STRING.getName() + "="
		+ innerString;
	args.add(innerArg);

	// Setup list arg
	Boolean setupListBool = Boolean.TRUE;
	String setupListArg = TestKeys.A_SETUP_LIST.getName() + "/"
		+ InnerOptions2.getSetupName() + "/"
		+ AnotherTestKeys.ANOTHER_BOOLEAN.getName() + "="
		+ setupListBool.toString();
	args.add(setupListArg);

	// Set preferred index of setup list
	String preferredIndexArg = TestKeys.A_SETUP_LIST.getName() + "=" + InnerOptions2.getSetupName();
	args.add(preferredIndexArg);

	MultipleChoices choice = MultipleChoices.CHOICE2;
	String choiceArg = TestKeys.A_MULTIPLE_OPTION.getName() + "="
		+ choice.name();
	args.add(choiceArg);

	// System.out.println("ARGS:"+args);

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

	assertEquals(stringValue, setup.get(TestKeys.A_STRING));
	assertEquals(booleanValue, setup.get(TestKeys.A_BOOLEAN));
	assertEquals(stringListValue, setup.get(TestKeys.A_STRINGLIST));

	// File List
	List<File> files = setup.get(TestKeys.A_FILELIST).getFiles();

	// Verify it is two files
	assertTrue(files.size() == 2);

	// Verify if files exist
	for (File file : files) {
	    assertTrue(file.isFile());
	}

	// assertEquals(innerString, setup.get(TestKeys.A_SETUP).get(AnotherTestKeys.ANOTHER_STRING));

	// // Accessing setup list by name
	// assertEquals(setupListBool, setup.get(TestKeys.A_SETUP_LIST).getSetup(InnerOptions2.getSetupName())
	// .get(AnotherTestKeys.ANOTHER_BOOLEAN));
	//
	// // Accessing setup list by preferred index
	// assertEquals(setupListBool, setup.get(TestKeys.A_SETUP_LIST).get(AnotherTestKeys.ANOTHER_BOOLEAN));
	//
	// assertEquals(choice, setup.get(TestKeys.A_MULTIPLE_OPTION));

	// Check that setup files of inner and outer setup are the same

	// assertEquals(setup.getSetupFile().get().getFile(),
	// setup.get(TestKeys.A_SETUP_LIST).getSetup(InnerOptions2.getSetupName()).getSetupFile().get().getFile());
	//
	// assertEquals(setup.getSetupFile().get().getFile(),
	// setup.get(TestKeys.A_SETUP_LIST).getSetup(InnerOptions.getSetupName()).getSetupFile().get().getFile());
	//
	// assertEquals(setup.getSetupFile().get().getFile(),
	// setup.get(TestKeys.A_SETUP_LIST).getSetupFile().get().getFile());

    }
}
