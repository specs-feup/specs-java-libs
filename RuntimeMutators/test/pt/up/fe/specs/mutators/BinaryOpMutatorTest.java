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

package pt.up.fe.specs.mutators;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.up.fe.specs.mutators.config.MapConfiguration;

public class BinaryOpMutatorTest {

    @Test
    public void test() {

        // 2 + 3 - 4;
        var configuration = MapConfiguration.newInstance("BinaryOpInt_1", "+", "BinaryOpInt_2", "-");
        RuntimeMutators.setConfiguration(configuration);
        int a1 = twoIntOps();

        assertEquals(1, a1);

        // 2 - 3 + 4;
        configuration = MapConfiguration.newInstance("BinaryOpInt_1", "-", "BinaryOpInt_2", "+");
        RuntimeMutators.setConfiguration(configuration);
        int a2 = twoIntOps();

        assertEquals(3, a2);
    }

    private int twoIntOps() {
        return BinaryOpMutator.intOp(BinaryOpMutator.intOp(2, 3, "BinaryOpInt_1"), 4, "BinaryOpInt_2");
    }

}
