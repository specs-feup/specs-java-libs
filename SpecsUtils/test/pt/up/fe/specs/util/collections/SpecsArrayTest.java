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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SpecsArray Tests")
public class SpecsArrayTest {

    @Test
    @DisplayName("Should return -1 for non-array objects")
    public void notArray() {
        assertThat(SpecsArray.getLength("hello")).isEqualTo(-1);
    }

    @Test
    @DisplayName("Should return correct length for int arrays")
    public void intArray() {
        assertThat(SpecsArray.getLength(new int[10])).isEqualTo(10);
    }

    @Test
    @DisplayName("Should return correct length for object arrays")
    public void objectArray() {
        assertThat(SpecsArray.getLength(new String[9])).isEqualTo(9);
    }

}
