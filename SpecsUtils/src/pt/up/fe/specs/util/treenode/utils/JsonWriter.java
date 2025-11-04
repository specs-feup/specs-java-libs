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

package pt.up.fe.specs.util.treenode.utils;

import java.util.List;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.classmap.FunctionClassMap;
import pt.up.fe.specs.util.treenode.TreeNode;
import pt.up.fe.specs.util.utilities.BuilderWithIndentation;

public class JsonWriter<K extends TreeNode<K>> {

    private final FunctionClassMap<K, String> jsonTranslators;

    public JsonWriter(FunctionClassMap<K, String> jsonTranslator) {
        this.jsonTranslators = jsonTranslator;
    }

    public String toJson(K node) {
        return toJson(node, 0);
    }

    private String toJson(K node, int identationLevel) {
        BuilderWithIndentation builder = new BuilderWithIndentation(identationLevel, "  ");

        builder.addLines("{");
        builder.increaseIndentation();

        // Get JSON for the node
        String nodeJson = jsonTranslators.apply(node);
        builder.addLines(nodeJson);

        // Add children
        List<K> children = node.getChildren();
        if (children.isEmpty()) {
            builder.addLines("\"children\": []");
        } else {
            StringBuilder childrenBuilder = new StringBuilder();
            childrenBuilder.append("\"children\": [\n");

            String childrenString = children.stream()
                    .map(child -> toJson(child, builder.getCurrentIdentation() - 1))
                    .collect(Collectors.joining(",\n"));

            childrenBuilder.append(childrenString);
            childrenBuilder.append("]");

            builder.addLines(childrenBuilder.toString());
        }

        builder.decreaseIndentation();
        builder.add("}");

        return builder.toString();
    }

    public static String escape(String string) {
        String escapedString = string.replace("\\", "\\\\");
        escapedString = escapedString.replace("\"", "\\\"");

        return escapedString;
    }
}
