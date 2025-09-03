/**
 * Copyright 2014 SPeCS Research Group.
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

package pt.up.fe.specs.util.csv;

import java.io.File;
import java.util.List;

import pt.up.fe.specs.util.SpecsIo;

public class BufferedCsvWriter extends CsvWriter {

    private final File bufferFile;

    private boolean headerWritten;
    private int lineCounter;

    public BufferedCsvWriter(File bufferFile, List<String> header) {
        super(header);

        this.bufferFile = bufferFile;
        this.headerWritten = false;
        this.lineCounter = 0;
        // Delete buffer
        SpecsIo.write(bufferFile, "");
    }

    @Override
    public BufferedCsvWriter addLine(List<String> line) {
        // Write header
        if (!this.headerWritten) {
            this.headerWritten = true;
            // Increment line counter
            lineCounter++;

            SpecsIo.append(this.bufferFile, buildHeader());
        }

        // Increment line counter
        lineCounter++;

        // Write line
        SpecsIo.append(this.bufferFile, buildLine(line, lineCounter));

        return this;
    }

    @Override
    public String buildCsv() {
        // Return the contents of the file that was written
        return SpecsIo.read(this.bufferFile);
    }

}
