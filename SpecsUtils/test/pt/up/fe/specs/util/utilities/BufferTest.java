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

package pt.up.fe.specs.util.utilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class BufferTest {

    @Test
    public void test() {
        try {
            testBuffer(0);
            fail();
        } catch (Exception e) {
            // Expects exception
        }

        testBuffer(1);
        testBuffer(2);
        testBuffer(3);
        testBuffer(4);
    }

    public void testBuffer(int numBuffers) {
        ProgressCounter counter = new ProgressCounter(numBuffers);
        var doubleBuffer = new Buffer<>(numBuffers, () -> counter.next());

        for (int i = 0; i < numBuffers; i++) {
            assertEquals("(" + (i + 1) + "/" + numBuffers + ")", doubleBuffer.next());
        }

        // Second run
        for (int i = 0; i < numBuffers; i++) {
            assertEquals("(" + (i + 1) + "/" + numBuffers + ")", doubleBuffer.next());
        }
    }

}
