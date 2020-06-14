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

package pt.up.fe.specs.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SpecsBitsTest {

    @Test
    public void signExtendString() {
        assertEquals("11111101", SpecsBits.signExtend("01010101", 2));
        assertEquals("00000101", SpecsBits.signExtend("01010101", 3));
        assertEquals("10101010101010101", SpecsBits.signExtend("10101010101010101", 16));
        assertEquals("00101010101010101", SpecsBits.signExtend("00101010101010101", 16));
        assertEquals("11111111111111111", SpecsBits.signExtend("00101010101010101", 0));
        assertEquals("00000000000000000", SpecsBits.signExtend("00101010101010100", 0));
        assertEquals("100", SpecsBits.signExtend("100", 6));
        assertEquals("100", SpecsBits.signExtend("100", 2));
        assertEquals("0", SpecsBits.signExtend("0", 1));
        assertEquals("0", SpecsBits.signExtend("0", 0));
        assertEquals("11", SpecsBits.signExtend("01", 0));
    }

}
