/**
 * Copyright 2021 SPeCS.
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

package pt.up.fe.specs.jsengine;

import java.util.stream.Collectors;

import org.junit.Test;

import pt.up.fe.specs.jsengine.libs.JsEsprima;
import pt.up.fe.specs.util.SpecsSystem;

public class EsprimaTester {

    @Test
    public void test() {
        SpecsSystem.programStandardInit();

        // var code = SpecsIo.read(new File("C:\\Temp\\2021-12-02 JsDoc Test\\navier.js"));
        var code = PROGRAM_1;

        var program = JsEsprima.parse(code);

        // System.out.println("P:" + program);
        // System.out.println("Program children:" + JsEsprima.getChildren(program));
        // System.out.println("Program descendants:" + JsEsprima.getDescendants(program));

        var functions = JsEsprima.getDescendantsAndSelfStream(program)
                .filter(node -> ((String) node.get("type")).equals("FunctionDeclaration"))
                .collect(Collectors.toList());

        System.out.println("FUNCTIONS: " + functions.size());

        // functions.stream().map(f -> ((Map) f.get("id")).get("name")).forEach(n -> System.out.println(n));
        functions.stream().map(f -> f.get("comments")).forEach(n -> System.out.println(n));

        var numNodes = JsEsprima.getDescendantsAndSelfStream(program)
                .count();

        System.out.println("NUM NODES: " + numNodes);
    }

    private static final String PROGRAM_1 = "/** \r\n"
            + " * \r\n"
            + " */\r\n"
            + "function runNavierStokes()\r\n"
            + "{\r\n"
            + "    solver.update();\r\n"
            + "    nsFrameCounter++;\r\n"
            + "{\r\n"
            + "    if(nsFrameCounter==15)\r\n"
            + "        checkResult(solver.getDens());\r\n"
            + "}\r\n"
            + "}\r\n"
            + "\r\n"
            + "";

}
