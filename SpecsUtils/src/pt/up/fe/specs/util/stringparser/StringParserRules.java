/**
 * Copyright 2018 SPeCS.
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

package pt.up.fe.specs.util.stringparser;

import pt.up.fe.specs.util.utilities.StringSlice;

public class StringParserRules {

    /**
     * Looks for a string separated by a whitespace, or the complete string if no whitespace is found.
     * 
     * <p>
     * A whitespace is determined by the function {@link java.lang.Character#isWhitespace(char)}.
     * 
     * @param string
     * @return
     */
    public static ParserResult<String> word(StringSlice string) {
        int endIndex = string.indexOfFirstWhiteSpace();
        if (endIndex == -1) {
            endIndex = string.length();
        }

        String element = string.substring(0, endIndex).toString();

        // Update slice
        string = string.substring(endIndex);

        return new ParserResult<>(string, element);
    }

}
