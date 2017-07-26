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

package pt.up.fe.specs.util.parsing;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.up.fe.specs.util.SpecsStrings;

public class ParseUtilsTest {

    @Test
    public void testAlphaId() {
        assertEquals("A", SpecsStrings.toExcelColumn(1));
        assertEquals("Z", SpecsStrings.toExcelColumn(26));
        assertEquals("AA", SpecsStrings.toExcelColumn(27));
        assertEquals("AB", SpecsStrings.toExcelColumn(28));
        assertEquals("AE", SpecsStrings.toExcelColumn(31));
    }

}
