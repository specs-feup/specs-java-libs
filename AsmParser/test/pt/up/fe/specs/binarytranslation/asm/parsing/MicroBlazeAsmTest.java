/**
 * Copyright 2024 SPeCS.
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

package pt.up.fe.specs.binarytranslation.asm.parsing;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import pt.up.fe.specs.binarytranslation.asm.parsing.binaryasmparser.BinaryAsmInstructionParser;

public class MicroBlazeAsmTest {

    enum MicroBlazeAsmField implements AsmFieldType {
        INST1,
        INST2;
    }

    @Test
    public void test1() {

        var parsers = new ArrayList<AsmParser>();

        parsers.add(new BinaryAsmInstructionParser(MicroBlazeAsmField.INST1,
                "100101_registerd(5)_1000_opcodea(1)_0_imm(15)", null));

        parsers.add(new BinaryAsmInstructionParser(MicroBlazeAsmField.INST2,
                "100101_0_opcodea(1)_000_registera(5)_11_registers(14)", null));

        // var insts = List.of("10010100001100000000000000000001");
        var insts = List.of("94300001");

        for (var inst : insts) {

            boolean isSuccess = false;

            for (var parser : parsers) {
                var parsed = parser.parse("0", inst);

                if (parsed.isEmpty()) {
                    continue;
                }

                System.out.println("Parsing success: " + parsed.get());
                isSuccess = true;
                break;
            }

            if (!isSuccess) {
                fail("Could not parse " + inst);
            }
        }

        /*
        newInstance(MSR, ),
        newInstance(MFS, "100101_registerd(5)_0_opcodea(1)_00010_registers(14)"),
        newInstance(MTS, "100101_0_opcodea(1)_000_registera(5)_11_registers(14)"),
          */
    }

}
