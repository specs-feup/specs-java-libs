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

package pt.up.fe.specs.util.parsing.arguments;

public class Gluer {

    private final String delimiterStart;
    private final String delimiterEnd;

    public Gluer(String start, String end) {
        this.delimiterStart = start;
        this.delimiterEnd = end;
    }

    /**
     * 
     * @return a new Gluer that start and ends with double quote ('"')
     */
    public static Gluer newDoubleQuote() {
        return new Gluer("\"", "\"");
    }

    public String getGluerStart() {
        return delimiterStart;
    }

    public String getGluerEnd() {
        return delimiterEnd;
    }
}
