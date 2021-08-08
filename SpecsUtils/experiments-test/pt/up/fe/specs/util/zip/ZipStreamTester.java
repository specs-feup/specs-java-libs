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

package pt.up.fe.specs.util.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.Test;

import pt.up.fe.specs.util.SpecsLogs;

public class ZipStreamTester {

    @Test
    public void test() {

        // Output zip file
        File outputFile = new File("C:\\Users\\JoaoBispo\\Desktop\\shared\\antarex\\dse-exploration\\log_test.zip");

        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFile));
                PrintStream zipPrintStream = new PrintStream(out)) {

            out.putNextEntry(new ZipEntry("log_test.txt"));
            String test = "Hello\nWorld";
            zipPrintStream.print(test);

        } catch (IOException e) {
            SpecsLogs.warn("Error message:\n", e);
        }

    }

}
