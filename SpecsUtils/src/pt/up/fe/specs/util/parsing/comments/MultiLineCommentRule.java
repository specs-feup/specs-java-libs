/**
 * Copyright 2016 SPeCS.
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

package pt.up.fe.specs.util.parsing.comments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import pt.up.fe.specs.util.Preconditions;

public class MultiLineCommentRule implements TextParserRule {

    @Override
    public Optional<TextElement> apply(String line, Iterator<String> iterator) {

        // Check if line contains '//'
        int startIndex = line.indexOf("/*");
        if (startIndex == -1) {
            return Optional.empty();
        }

        // Found start of a multi-line comment. Try to find the end
        List<String> lines = new ArrayList<>();

        String currentLine = line.substring(startIndex + "/*".length());

        int endIndex;
        while (true) {

            // Check if current line end the multi-line comment
            endIndex = currentLine.indexOf("*/");

            if (endIndex != -1) {
                // Found end of multi-line comment, add line
                lines.add(currentLine.substring(0, endIndex).trim());
                break;

            }

            Preconditions.checkArgument(iterator.hasNext(),
                    "Could not find end of multi-line comment");

            // Did not find end of comment, add current string to list
            lines.add(currentLine.trim());
            currentLine = iterator.next();
        }

        return Optional.of(TextElement.newInstance(TextElementType.MULTILINE_COMMENT,
                String.join("\n", lines)));
    }

}
