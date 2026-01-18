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

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for Buffer utility class.
 * 
 * This test class covers buffer functionality including:
 * - Buffer creation with different sizes
 * - Progress counter integration
 * - Exception handling for invalid buffer sizes
 * - Multiple buffer usage cycles
 */
@DisplayName("Buffer Tests")
public class BufferTest {

    @Nested
    @DisplayName("Buffer Creation and Usage")
    class BufferCreationAndUsage {

        @Test
        @DisplayName("Buffer creation should throw exception for zero buffers")
        void testBuffer_ZeroBuffers_ShouldThrowException() {
            assertThatThrownBy(() -> testBuffer(0))
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Buffer should work correctly with single buffer")
        void testBuffer_SingleBuffer_WorksCorrectly() {
            assertThatCode(() -> testBuffer(1))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Buffer should work correctly with multiple buffers")
        void testBuffer_MultipleBuffers_WorksCorrectly() {
            assertThatCode(() -> testBuffer(2))
                    .doesNotThrowAnyException();
            assertThatCode(() -> testBuffer(3))
                    .doesNotThrowAnyException();
            assertThatCode(() -> testBuffer(4))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Buffer should handle large number of buffers")
        void testBuffer_LargeNumberOfBuffers_WorksCorrectly() {
            assertThatCode(() -> testBuffer(10))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Buffer should handle negative number gracefully")
        void testBuffer_NegativeNumber_ShouldThrowException() {
            assertThatThrownBy(() -> testBuffer(-1))
                    .isInstanceOf(Exception.class);
        }
    }

    private void testBuffer(int numBuffers) {
        ProgressCounter counter = new ProgressCounter(numBuffers);
        var doubleBuffer = new Buffer<>(numBuffers, () -> counter.next());

        // First run
        for (int i = 0; i < numBuffers; i++) {
            assertThat(doubleBuffer.next()).isEqualTo("(" + (i + 1) + "/" + numBuffers + ")");
        }

        // Second run - verify buffer can be reused
        for (int i = 0; i < numBuffers; i++) {
            assertThat(doubleBuffer.next()).isEqualTo("(" + (i + 1) + "/" + numBuffers + ")");
        }
    }

}
