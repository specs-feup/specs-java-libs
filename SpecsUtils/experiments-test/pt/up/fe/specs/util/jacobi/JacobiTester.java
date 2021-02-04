/**
 * Copyright 2020 SPeCS.
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

import org.junit.Test;

import pt.up.fe.specs.util.collections.MultiMap;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.stringsplitter.StringSplitter;
import pt.up.fe.specs.util.stringsplitter.StringSplitterRules;

public class JacobiTester {

    @Test
    public void testJacobiIndexes() {
        ResourceProvider testString = () -> "pt/up/fe/specs/util/jacobi/JacobiStringParser.txt";
        var map = parseJacobiIndexes(testString.read());
        // System.out.println("RESULTS:\n" + map);

        for (var entry : map.entrySet()) {
            System.out.println("KEY: " + entry.getKey());
            System.out.println("VALUE: " + entry.getValue());
        }

    }

    public static MultiMap<String, DwAccess> parseJacobiIndexes(String contents) {
        StringSplitter parser = new StringSplitter(contents);

        parser.setSeparator(
                aChar -> aChar == '(' | aChar == ',' | aChar == ')' | Character.isWhitespace(aChar) | aChar == '&');

        MultiMap<String, DwAccess> results = new MultiMap<>();

        String currentJaccIndex = null;

        while (!parser.isEmpty()) {

            var control = parser.parse(StringSplitterRules::string);

            if (control.equals("JacC")) {
                var ijk = parser.parse(StringSplitterRules::string);
                if (!ijk.equals("ijk")) {
                    throw new RuntimeException("Expected 'ijk', found '" + ijk + "'");
                }

                var index1 = parser.parse(StringSplitterRules::string);
                var index2 = parser.parse(StringSplitterRules::string);

                currentJaccIndex = index1 + "," + index2;

                var equal = parser.parse(StringSplitterRules::string);
                if (!equal.equals("=")) {
                    throw new RuntimeException("Expected =, found " + equal);
                }

            } else if (control.equals("+")) {

                var access = parseDw(parser, "+");
                results.add(currentJaccIndex, access);
            } else if (control.equals("-")) {

                var access = parseDw(parser, "-");
                results.add(currentJaccIndex, access);
            } else if (control.strip().isBlank()) {

            } else {
                throw new RuntimeException("Case not implemented: '" + control + "'");
            }

        }

        return results;

    }

    private static DwAccess parseDw(StringSplitter parser, String signal) {

        var dw = parser.parse(StringSplitterRules::string);

        var constant = dw.endsWith("*dw") ? dw.substring(0, dw.length() - "*dw".length()) : "1";
        var ijk = parser.parse(StringSplitterRules::string);
        if (!ijk.equals("ijk")) {
            throw new RuntimeException("Expected 'ijk', found '" + ijk + "'");
        }

        var x = parser.parse(StringSplitterRules::string);
        var y = parser.parse(StringSplitterRules::string);

        return new DwAccess(x, y, constant, signal);

    }

    public static void main(String[] args) {
        new JacobiTester().testJacobiIndexes();
    }
}
