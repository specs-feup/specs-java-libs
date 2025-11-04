/**
 * Copyright 2015 SPeCS Research Group.
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

package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("StringSlice Tests")
public class StringSliceTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty string slice")
        public void testConstructorEmpty() {
            assertThat(new StringSlice("").toString()).isEqualTo("");
        }

        @Test
        @DisplayName("Should create simple string slice")
        public void testConstructorSimple() {
            assertThat(new StringSlice("abc").toString()).isEqualTo("abc");
        }
    }

    @Nested
    @DisplayName("Basic Properties Tests")
    class BasicPropertiesTests {

        @Test
        @DisplayName("Should return correct length")
        public void testLength() {
            assertThat(new StringSlice("hello").length()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should identify empty slices")
        public void testIsEmpty() {
            assertThat(new StringSlice("").isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should return correct character at index")
        public void testCharAt() {
            assertThat(new StringSlice("abc").charAt(1)).isEqualTo('b');
            assertThat(new StringSlice("abc").substring(1).charAt(0)).isEqualTo('b');
        }
    }

    @Nested
    @DisplayName("Substring Tests")
    class SubstringTests {

        @Test
        @DisplayName("Should create substring from index to end")
        public void testSubstringPrefix() {
            assertThat(new StringSlice("abcdef").substring(2).toString()).isEqualTo("cdef");
            assertThat(new StringSlice("abcdef").substring(2).length()).isEqualTo(4);
        }

        @Test
        @DisplayName("Should create substring with start and end indices")
        public void testSubstring() {
            assertThat(new StringSlice("abcdef").substring(2, 3).toString()).isEqualTo("c");
        }

        @Test
        @DisplayName("Should handle empty substrings")
        public void testEmptySubstring() {
            assertThat(new StringSlice("abcdef").substring(2, 2).toString()).isEqualTo("");
            assertThat(new StringSlice("ab").substring(2).toString()).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("String Operations Tests")
    class StringOperationsTests {

        @Test
        @DisplayName("Should check prefix matching correctly")
        public void testStartsWith() {
            assertThat(new StringSlice("abc").startsWith("abc")).isTrue();
            assertThat(new StringSlice("abc").substring(1).startsWith("bc")).isTrue();
            assertThat(new StringSlice("abcd").startsWith("b")).isFalse();
        }

        @Test
        @DisplayName("Should trim whitespace correctly")
        public void testTrim() {
            assertThat(new StringSlice("  a bc   ").trim().toString()).isEqualTo("a bc");
        }

        @Test
        @DisplayName("Should trim line breaks correctly")
        public void testTrimLineBreak() {
            String base = "\nabc\n";
            assertThat(new StringSlice(base).trim().toString()).isEqualTo(base.trim());
        }
    }
}
