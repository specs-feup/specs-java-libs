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

package pt.up.fe.specs.util;

import static org.junit.Assert.*;

import java.util.Optional;

import org.junit.Test;

import pt.up.fe.specs.util.stringparser.StringParser;
import pt.up.fe.specs.util.stringparser.StringParserRules;
import pt.up.fe.specs.util.stringparser.StringParsers;

public class StringParserTest {

    @Test
    public void doubleQuotedString() {

        assertEquals("hello", new StringParser("\"hello\"").apply(StringParsers::parseDoubleQuotedString));
        assertEquals("hel\\\"lo", new StringParser("\"hel\\\"lo\"").apply(StringParsers::parseDoubleQuotedString));
    }

    @Test
    public void testWord() {

        String testString = "word1 word2\tword3  word4";

        // By default, trim after parsing is true
        StringParser parser = new StringParser(testString);

        String word1 = parser.parse(StringParserRules::word);
        assertEquals("word1", word1);
        assertEquals("word2\tword3  word4", parser.toString());

        Optional<String> word2 = parser.check(StringParserRules::word);
        assertEquals("word2", word2.get());
        assertEquals("word3  word4", parser.toString());

        Optional<String> failedCheck = parser.check(StringParserRules::word,
                string -> string.equals("non-existing word"));
        assertFalse(failedCheck.isPresent());
        assertEquals("word3  word4", parser.toString());

        Optional<String> word3 = parser.check(StringParserRules::word,
                string -> string.equals("word3"));
        assertEquals("word3", word3.get());
        assertEquals("word4", parser.toString());

        Optional<String> word4 = parser.peek(StringParserRules::word,
                string -> string.equals("word4"));
        assertEquals("word4", word4.get());
        assertEquals("word4", parser.toString());

        boolean hasWord4 = parser.has(StringParserRules::word, string -> string.equals("word4"));
        assertTrue(hasWord4);
        assertTrue(parser.isEmpty());
    }

    @Test
    public void testNumbers() {
        String testString = "1 2.0 3.0f";
        StringParser parser = new StringParser(testString);

        Integer integer = parser.parse(StringParserRules::integer);
        assertEquals(Integer.valueOf(1), integer);
        assertEquals("2.0 3.0f", parser.toString());

        Double aDouble = parser.parse(StringParserRules::doubleNumber);
        assertEquals(Double.valueOf(2.0), aDouble);
        assertEquals("3.0f", parser.toString());

        Float aFloat = parser.parse(StringParserRules::floatNumber);
        assertEquals(Float.valueOf(3.0f), aFloat);
        assertTrue(parser.isEmpty());

    }

    @Test
    public void testReverseAndSeparator() {
        String testString = "word1 word2,word3, word4";
        StringParser parser = new StringParser(testString);

        String word1 = parser.parse(StringParserRules::word);
        assertEquals("word1", word1);
        assertEquals("word2,word3, word4", parser.toString());

        parser.setSeparator(aChar -> aChar == ',');

        String word2 = parser.parse(StringParserRules::word);
        assertEquals("word2", word2);
        assertEquals("word3, word4", parser.toString());

        parser.setReverse(true);

        String word4 = parser.parse(StringParserRules::word);
        assertEquals("word4", word4);
        assertEquals("word3", parser.toString());

        String word3 = parser.parse(StringParserRules::word);
        assertEquals("word3", word3);
        assertTrue(parser.isEmpty());
    }
}
