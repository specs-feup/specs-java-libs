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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.utilities;

import static org.junit.Assert.*;

import org.junit.Test;

import pt.up.fe.specs.util.utilities.StringSlice;

public class StringSliceTest {

    @Test
    public void testConstructorEmpty() {
	assertEquals("", new StringSlice("").toString());
    }

    @Test
    public void testConstructorSimple() {
	assertEquals("abc", new StringSlice("abc").toString());
    }

    @Test
    public void testLength() {
	assertEquals(5, new StringSlice("hello").length());
    }

    @Test
    public void testIsEmpty() {
	assertTrue(new StringSlice("").isEmpty());
    }

    @Test
    public void testSubstringPrefix() {
	assertEquals("cdef", new StringSlice("abcdef").substring(2).toString());
	assertEquals(4, new StringSlice("abcdef").substring(2).length());
    }

    @Test
    public void testSubstring() {
	assertEquals("c", new StringSlice("abcdef").substring(2, 3).toString());
    }

    @Test
    public void testEmptySubstring() {
	assertEquals("", new StringSlice("abcdef").substring(2, 2).toString());
	assertEquals("", new StringSlice("ab").substring(2).toString());
    }

    @Test
    public void testCharAt() {
	assertEquals('b', new StringSlice("abc").charAt(1));
	assertEquals('b', new StringSlice("abc").substring(1).charAt(0));
    }

    @Test
    public void testStartsWith() {
	assertTrue(new StringSlice("abc").startsWith("abc"));
	assertTrue(new StringSlice("abc").substring(1).startsWith("bc"));
	assertTrue(!new StringSlice("abcd").startsWith("b"));
    }

    @Test
    public void testTrim() {
	assertEquals("a bc", new StringSlice("  a bc   ").trim().toString());
    }

    @Test
    public void testTrimLineBreak() {
	String base = "\nabc\n";
	assertEquals(base.trim(), new StringSlice(base).trim().toString());
    }
}
