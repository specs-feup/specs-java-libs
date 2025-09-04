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

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.test.keys.TestKeys;
import org.suikasoft.jOptions.test.keys.AnotherTestKeys;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.properties.SpecsProperty;

/**
 * @author Joao Bispo
 * 
 */
public class CommandLineTester {

    @BeforeAll
    public static void runBeforeClass() {
        SpecsSystem.programStandardInit();

        SpecsProperty.ShowStackTrace.applyProperty("true");

        // Create test files
        getTestFiles().stream().forEach(file -> SpecsIo.write(file, "dummy"));

    }

    @AfterAll
    public static void runAfterClass() {
        // Delete test files
        getTestFiles().stream().forEach(SpecsIo::delete);
    }

    private static List<File> getTestFiles() {
        File testFolder = SpecsIo.mkdir("testFolder");

        List<File> testFiles = new ArrayList<>();
        testFiles.add(new File(testFolder, "file1.txt"));
        testFiles.add(new File(testFolder, "file2.txt"));

        return testFiles;
    }

    @Test
    public void test() {

        CliTester tester = new CliTester();

        // String
        tester.addTest(() -> TestKeys.A_STRING.getName() + "=test string",
                dataStore -> assertEquals("test string", dataStore.get(TestKeys.A_STRING)));

        // DataStore
        Supplier<String> datastoreSupplier = () -> TestKeys.A_SETUP.getName()
                + "={\"ANOTHER_String\": \"another string\"}";

        Consumer<DataStore> datastoreConsumer = dataStore -> assertEquals("another string",
                dataStore.get(TestKeys.A_SETUP).get(AnotherTestKeys.ANOTHER_STRING));
        tester.addTest(datastoreSupplier, datastoreConsumer);

        tester.test();
    }
}
