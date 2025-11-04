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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.parsing;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.parsing.arguments.ArgumentsParser;

@DisplayName("ArgumentParsing Tests")
public class ArgumentParsingTest {

    private void test(ArgumentsParser commandLineParser, String input, String... expected) {
        List<String> args = commandLineParser.parse(input);
        assertThat(args).isEqualTo(Arrays.asList(expected));
    }

    @Test
    @DisplayName("Should parse command line arguments correctly")
    public void commandLine() {
        ArgumentsParser parser = ArgumentsParser.newCommandLine(false);

        test(parser, "\\u\\ ", "\\u\\ ");
        test(parser, "\\u \\ ", "\\u", "\\ ");
        test(parser, "    Hello   World   ", "Hello", "World");
        test(parser, "    \"Hello   World\"   ", "Hello   World");
        test(parser, "    \" Hello   World \"   ", " Hello   World ");
        test(parser, "    \" Hello \\\"  World \"   ", " Hello \\\"  World ");
    }
}
