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

import java.util.Iterator;
import java.util.Optional;

public class InlineCommentRule implements TextParserRule {

    @Override
    public Optional<TextElement> apply(String line, Iterator<String> iterator) {
        // Check if line contains '//'
        int commentIndex = line.indexOf("//");
        if (commentIndex == -1) {
            return Optional.empty();
        }

        String commentText = line.substring(commentIndex + "//".length());

        return Optional.of(TextElement.newInstance(TextElementType.INLINE_COMMENT, commentText));
    }

}
