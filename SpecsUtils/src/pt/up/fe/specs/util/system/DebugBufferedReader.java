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

public class DebugBufferedReader extends BufferedReader {

    public DebugBufferedReader(BufferedReader reader) {
        super(reader == null ? new java.io.StringReader("") : reader);
    }

    @Override
    public int read() throws IOException {
        int readInt = super.read();
        System.out.println("DebugReader: read() -> " + readInt + " (" + ((char) readInt) + ")");
        return readInt;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {

        int readInt = super.read(cbuf, off, len);
        System.out.println("DebugReader: read(char[], int, int) -> " + readInt + " (" + ((char) readInt) + ")");
        return readInt;
    }

    @Override
    public String readLine() throws IOException {
        String line = super.readLine();
        printReadLineDebug(line);

        return line;
    }

    /**
     * Helper to print a consistent debug representation for readLine().
     * Non-null strings are wrapped in quotes so that the literal string
     * "null" can be distinguished from a null return value.
     */
    private void printReadLineDebug(String line) {
        String displayed = (line == null) ? "null" : ('\"' + line + '\"');
        System.out.println("DebugReader: readLine() -> " + displayed);
    }

}
