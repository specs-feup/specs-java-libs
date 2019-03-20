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

    // private static AtomicInteger COUNTER = new AtomicInteger();

    private static final String NEW_LINE = System.getProperty("line.separator");
    // private static final int BUFFER_SIZE = 1024;

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
        // int id = COUNTER.incrementAndGet();
        // System.out.println("Stream to String " + id + " (" + type + ")");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        // InputStreamReader reader = new InputStreamReader(inputStream);
        StringBuilder output = new StringBuilder();

        // char[] buffer = new char[BUFFER_SIZE];

        try {
            /*
            int readChars = -1;
            while ((readChars = reader.read(buffer)) != -1) {
                // String currentLine = stdline;
                // SpecsLogs.debug(() -> "StreamToString: reading line: " + currentLine);
                if (this.printOutput) {
                    type.print(String.valueOf(readChars));
                    // type.print(stdline);
                    // type.print(NEW_LINE);
                }
            
                // System.err.println(stdline);
            
                // Save output
                if (this.storeOutput) {
                    output.append(readChars);
                }
            
            }
            // System.out.println("Finished StreamToString " + id + " (" + type + ")");
            inputStream.close();
            */
            /*
            int readChar = -1;
            
            while ((readChar = reader.read()) != -1) {
                char read = (char) readChar;
                String readString = String.valueOf(read);
            
                // String currentLine = stdline;
                // SpecsLogs.debug(() -> "StreamToString: reading line: " + currentLine);
                if (this.printOutput) {
                    type.print(readString);
                    // type.print(stdline);
                    // type.print(NEW_LINE);
                }
            
                // System.err.println(stdline);
            
                // Save output
                if (this.storeOutput) {
                    // output.append(stdline).append(NEW_LINE);
                    output.append(read);
                }
            
            }
            */

            String stdline = null;

            while ((stdline = reader.readLine()) != null) {
                // String currentLine = stdline;
                // SpecsLogs.debug(() -> "StreamToString: reading line: " + currentLine);
                if (this.printOutput) {
                    type.print(stdline);
                    type.print(NEW_LINE);
                }

                // System.err.println(stdline);

                // Save output
                if (this.storeOutput) {
                    output.append(stdline).append(NEW_LINE);
                }

            }
            inputStream.close();

            // System.out.println("Finished StreamToString " + id + " (" + type + ")");

        } catch (IOException e) {
            SpecsLogs.msgWarn("IOException during program execution:" + e.getMessage());
        }
        // SpecsLogs.debug(() -> "Output class: " + output.getClass());
        // SpecsLogs.debug(() -> "Output string length: " + output.toString().length());
        return output.toString();
    }

}
