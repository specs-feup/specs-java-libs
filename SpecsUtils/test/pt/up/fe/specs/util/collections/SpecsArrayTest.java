/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.specs.util.collections;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SpecsArrayTest {

    @Test
    public void notArray() {
        assertEquals(-1, SpecsArray.getLength("hello"));
    }

    @Test
    public void intArray() {
        assertEquals(10, SpecsArray.getLength(new int[10]));
    }

    @Test
    public void objectArray() {
        assertEquals(9, SpecsArray.getLength(new String[9]));
    }

}
