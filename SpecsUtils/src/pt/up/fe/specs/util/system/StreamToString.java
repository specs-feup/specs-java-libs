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

package pt.up.fe.specs.util.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.function.Function;

import pt.up.fe.specs.util.SpecsLogs;

public class StreamToString implements Function<InputStream, String> {

    private static final String NEW_LINE = System.getProperty("line.separator");

    private final boolean printOutput;
    private final boolean storeOutput;
    private final OutputType type;

    /**
     * Helper construtor that prints and stores the result to the standard output
     */
    public StreamToString() {
        this(true, true, OutputType.StdOut);
    }

    public StreamToString(boolean printOutput, boolean storeOutput, OutputType type) {
        this.printOutput = printOutput;
        this.storeOutput = storeOutput;
        this.type = type;
    }

    @Override
    public String apply(InputStream inputStream) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder output = new StringBuilder();

        try {

            String stdline = null;

            while ((stdline = reader.readLine()) != null) {

                if (this.printOutput) {
                    type.print(stdline);
                    type.print(NEW_LINE);
                }

                // Save output
                if (this.storeOutput) {
                    output.append(stdline).append(NEW_LINE);
                }

            }
            inputStream.close();

        } catch (IOException e) {
            SpecsLogs.msgWarn("IOException during program execution:" + e.getMessage());
        }

        return output.toString();
    }

}
