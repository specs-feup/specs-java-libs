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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import pt.up.fe.specs.util.parsing.comments.InlineCommentRule;
import pt.up.fe.specs.util.parsing.comments.MultiLineCommentRule;
import pt.up.fe.specs.util.parsing.comments.PragmaMacroRule;
import pt.up.fe.specs.util.parsing.comments.PragmaRule;
import pt.up.fe.specs.util.parsing.comments.TextElement;
import pt.up.fe.specs.util.parsing.comments.TextParserRule;
import pt.up.fe.specs.util.utilities.LineStream;
import pt.up.fe.specs.util.utilities.StringLines;

public class CommentParser {

    private static final List<TextParserRule> RULES = Arrays.asList(
            new InlineCommentRule(), new MultiLineCommentRule(), new PragmaRule(), new PragmaMacroRule());

    public List<TextElement> parse(File file) {
        try (LineStream lines = LineStream.newInstance(file)) {
            return parse(lines.getIterable().iterator());
        }
    }

    public List<TextElement> parse(String text) {
        return parse(StringLines.getLines(text).iterator());
    }

    public List<TextElement> parse(Iterator<String> iterator) {

        List<TextElement> elements = new ArrayList<>();

        // Parse each line, looking for text elements
        while (iterator.hasNext()) {
            // Get line
            String currentLine = iterator.next();

            Optional<TextElement> textElement = applyRules(currentLine, iterator);

            if (textElement.isEmpty()) {
                continue;
            }

            // Rule was applied, add element
            elements.add(textElement.get());

        }

        return elements;
    }

    public static Optional<TextElement> applyRules(String currentLine, Iterator<String> iterator) {

        // Apply all rules to the current line
        for (TextParserRule rule : RULES) {
            Optional<TextElement> textElement = rule.apply(currentLine, iterator);

            // If node is present, return
            if (textElement.isPresent()) {
                return textElement;
            }

        }

        return Optional.empty();
    }

}
