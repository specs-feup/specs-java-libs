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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.jsengine;

import org.junit.Test;

import pt.up.fe.specs.jsengine.libs.JsBabel;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;

public class BabelTester {

    @Test
    public void test() {
        SpecsSystem.programStandardInit();

        JsBabel.toES6("/** \r\n"
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
                + "");

    }

    @Test
    public void testGraph() {
        SpecsSystem.programStandardInit();

        var es5Code = JsBabel.toES6(SpecsIo.getResource("pt/up/fe/specs/jsengine/test/Graphs.js"));
        System.out.println("ES5: " + es5Code);
    }

}
