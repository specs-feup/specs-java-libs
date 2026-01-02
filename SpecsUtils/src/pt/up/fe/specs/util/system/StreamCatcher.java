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

package pt.up.fe.specs.util.system;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Can be launched in a thread to catch output streams.
 * 
 * @author Joao Bispo
 * 
 */
public class StreamCatcher implements Runnable {

    private static final String NEW_LINE = System.lineSeparator();

    /**
     * The types of output supported by this class.
     * 
     * @author Joao Bispo
     * 
     */
    public enum OutputType {
        StdErr {
            @Override
            public void print(String stdline) {
                System.err.print(stdline);
            }
        },
        StdOut {
            @Override
            public void print(String stdline) {
                System.out.print(stdline);
            }
        };

        public abstract void print(String stdline);
    }

    private final InputStream inputStream;
    private final OutputType type;
    private final boolean storeOutput;
    private final boolean printOutput;

    private StringBuilder printBuffer;
    private final StringBuilder builder;

    public StreamCatcher(InputStream inputStream, OutputType type, boolean storeOutput,
            boolean printOutput) {
        this.inputStream = inputStream;
        this.type = type;
        this.storeOutput = storeOutput;
        this.printOutput = printOutput;

        this.printBuffer = new StringBuilder();
        this.builder = new StringBuilder();
    }

    @Override
    public void run() {

        BufferedReader reader = new BufferedReader(new InputStreamReader(this.inputStream));

        try {
            // Reading individual characters instead of lines to prevent
            // blocking the execution due to the program filling the buffer before a newline
            // appears int character = -1;
            String stdline;
            while ((stdline = reader.readLine()) != null) {
                if (this.printOutput) {
                    this.type.print(stdline + StreamCatcher.NEW_LINE);
                }

                // Save output
                if (this.storeOutput) {
                    this.builder.append(stdline).append(StreamCatcher.NEW_LINE);
                }

            }

            // Clean any characters left in the buffer
            if (this.printOutput) {
                String line = this.printBuffer.toString();
                this.type.print(line);
                this.printBuffer = new StringBuilder();
            }

        } catch (IOException e) {
            SpecsLogs.warn("IOException during program execution:" + e.getMessage());
        }
    }

    /*
     * private void processCharacter(int character) {
     * char aChar = (char) character;
     * 
     * // Add character to current buffer
     * 
     * if (printOutput) {
     * printBuffer.append(aChar);
     * // type.print(stdline);
     * }
     * 
     * // System.err.println(stdline);
     * 
     * // Save output
     * if (storeOutput) {
     * // builder.append(stdline).append("\n");
     * builder.append(aChar);
     * }
     * 
     * // If character equals new line, print outputs and clean buffer
     * if (aChar == '\n' && printOutput) {
     * String line = printBuffer.toString();
     * type.print(line);
     * printBuffer = new StringBuilder();
     * }
     * 
     * }
     */
    public String getOutput() {
        return this.builder.toString();
    }
}
