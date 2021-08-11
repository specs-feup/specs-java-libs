/**
 * Copyright 2019 SPeCS.
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

public class SpecsSystemTest {

    public static final String STATIC_FIELD = "a_static_field";
    private static final int A_NUMBER = 10;

    public static int getStaticNumber() {
        return A_NUMBER;
    }

    public int getNumber() {
        return 20;
    }

    @Test
    public void testJavaVersion() {
        // Just ensure there is no exception thrown
        System.out.println(SpecsSystem.getJavaVersionNumber());
    }

    @Test
    public void testInvokeAsGetter() {
        // Field
        assertEquals("a_static_field", SpecsSystem.invokeAsGetter(SpecsSystemTest.class, "STATIC_FIELD"));

        // Static Method
        assertEquals(10, SpecsSystem.invokeAsGetter(SpecsSystemTest.class, "staticNumber"));

        // Instance Method
        assertEquals(20, SpecsSystem.invokeAsGetter(new SpecsSystemTest(), "number"));
    }

}
