/**
 * Copyright 2021 SPeCS.
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

package org.suikasoft.jOptions.treenode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.suikasoft.jOptions.Datakey.DataKey;

import pt.up.fe.specs.util.exceptions.CaseNotDefinedException;

public class NodeFieldReplacer<B extends DataNode<B>> {

    private final PropertyWithNodeManager manager;
    private final Function<B, Optional<B>> replacementDetector;

    // Stats
    private long processedNodes;
    private long processedFields;
    private long replacedNodes;

    /**
     * 
     * @param replacementProvider
     *            if the node needs to be replaced, returns a different node wrapped by the optional, otherwise returns
     *            empty
     */
    public NodeFieldReplacer(Function<B, Optional<B>> replacementProvider) {
        manager = new PropertyWithNodeManager();
        this.replacementDetector = replacementProvider;

        this.processedNodes = 0;
        this.processedFields = 0;
        this.replacedNodes = 0;
    }

    public <N extends B> void replaceFields(N node) {
        processedNodes++;

        for (var key : manager.getKeysWithNodes(node)) {
            processedFields++;

            var propertyType = PropertyWithNodeType.getKeyType(node, key);

            switch (propertyType) {
            case DATA_NODE:
                replaceDataNode(node, key);
                break;
            case OPTIONAL:
                replaceOptional(node, key);
                break;
            case LIST:
                replaceList(node, key);
                break;
            case NOT_FOUND:
                break;
            default:
                throw new CaseNotDefinedException(propertyType);
            }

        }

    }

    private <N extends B> void replaceList(N node, DataKey<?> key) {
        @SuppressWarnings("unchecked")
        var clavaNodes = (List<B>) node.get(key);
        var newClavaNodes = new ArrayList<B>(clavaNodes.size());

        for (int i = 0; i < clavaNodes.size(); i++) {
            var oldNode = clavaNodes.get(i);

            var normalizedNode = replacementDetector.apply(oldNode).orElse(oldNode);
            newClavaNodes.add(normalizedNode);
            if (normalizedNode != oldNode) {
                replacedNodes++;
            }
        }

        @SuppressWarnings("unchecked")
        var objectKey = (DataKey<Object>) key;
        node.set(objectKey, newClavaNodes);
    }

    @SuppressWarnings("unchecked")
    private <N extends B> void replaceOptional(N node, DataKey<?> key) {
        var value = ((Optional<B>) node.get(key)).get();

        replacementDetector.apply(value).ifPresent(normalizedNode -> {
            node.set((DataKey<Optional<B>>) key, Optional.of(normalizedNode));
            replacedNodes++;
        });
    }

    @SuppressWarnings("unchecked")
    private <N extends B> void replaceDataNode(N node, DataKey<?> key) {
        var value = node.getBaseClass().cast(node.get(key));

        replacementDetector.apply(value).ifPresent(replacementNode -> {
            node.set((DataKey<B>) key, replacementNode);
            replacedNodes++;
        });
    }
}
