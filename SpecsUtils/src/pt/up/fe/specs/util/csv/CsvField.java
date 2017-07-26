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

import java.util.function.Function;

public enum CsvField {

    AVERAGE("Average", range -> "=AVERAGE(" + range + ")"),
    STANDARD_DEVIATION_SAMPLE("Std. Dev. (Sample)", range -> "=STDEV.S(" + range + ")");

    private final String header;
    private final Function<String, String> fieldBuilder;

    private CsvField(String header, Function<String, String> fieldBuilder) {
        this.header = header;
        this.fieldBuilder = fieldBuilder;
    }

    String getHeader() {
        return header;
    }

    String getField(String dataRange) {
        return fieldBuilder.apply(dataRange);
    }
}
