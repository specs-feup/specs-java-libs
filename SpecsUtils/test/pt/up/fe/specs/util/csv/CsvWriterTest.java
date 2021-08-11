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

package pt.up.fe.specs.util.csv;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CsvWriterTest {

    @Test
    public void test() {
        CsvWriter csvWriter = new CsvWriter("name", "1", "2");
        csvWriter.setNewline("\n");

        csvWriter.addField(CsvField.AVERAGE, CsvField.STANDARD_DEVIATION_SAMPLE);
        csvWriter.addLine("line1", "4", "7");

        assertEquals("sep=;\n" +
                "name;1;2;Average;Std. Dev. (Sample)\n" +
                "line1;4;7;=AVERAGE(B2:C2);=STDEV.S(B2:C2)\n",
                csvWriter.buildCsv());
    }

}
