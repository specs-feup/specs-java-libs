/**
 * Copyright 2021 SPeCS.
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

package pt.up.fe.specs.util.jacobi;

import java.io.File;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.csv.CsvWriter;

public class JacobiParserLauncher {

    public static void main(String[] args) {

        SpecsSystem.programStandardInit();

        if (args.length < 1) {
            SpecsLogs.info("Expected one argument, the file with the JaccC indexes");
            return;
        }

        var inputFile = SpecsIo.existingFile(args[0]);
        var inputString = SpecsIo.read(inputFile);

        var map = JacobiTester.parseJacobiIndexes(inputString);

        // Output to CSV, one CSV per entry

        for (var entry : map.entrySet()) {
            var csvFilename = "key_" + entry.getKey().replace(',', '_') + ".csv";

            var csv = new CsvWriter("x", "y", "constant", "signal");

            var values = entry.getValue();

            for (var value : values) {
                csv.addLine(value.getX(), value.getY(), value.getConstant(), value.getSignal());
            }

            // Write CSV
            var outputFile = new File(csvFilename);
            SpecsLogs.info("Writing file '" + outputFile.getAbsolutePath() + "'");
            SpecsIo.write(outputFile, csv.buildCsv());

            // System.out.println("KEY: " + entry.getKey());
            // System.out.println("VALUE: " + entry.getValue());
        }

    }

}
