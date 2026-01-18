/*
 * Copyright 2021 SPeCS Research Group.
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

/**
 * Utility for replacing fields in DataNode trees based on a replacement
 * detector function.
 *
 * @param <B> the type of DataNode
 */
public class NodeFieldReplacer<B extends DataNode<B>> {

    private final PropertyWithNodeManager manager;
    private final Function<B, Optional<B>> replacementDetector;

    // Stats
    private long processedNodes;
    private long processedFields;
    private long replacedNodes;

    /**
     * Constructs a NodeFieldReplacer with the given replacement detector function.
     *
     * @param replacementProvider if the node needs to be replaced, returns a
     *                            different node wrapped by the optional,
     *                            otherwise returns empty
     */
    public NodeFieldReplacer(Function<B, Optional<B>> replacementProvider) {
        manager = new PropertyWithNodeManager();
        this.replacementDetector = replacementProvider;

        this.processedNodes = 0;
        this.processedFields = 0;
        this.replacedNodes = 0;
    }

    /**
     * Replaces fields in the given node based on the replacement detector function.
     *
     * @param node the node whose fields are to be replaced
     * @param <N>  the type of the node
     */
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

    /**
     * Replaces nodes in a list field of the given node.
     *
     * @param node the node containing the list field
     * @param key  the key identifying the list field
     * @param <N>  the type of the node
     */
    private <N extends B> void replaceList(N node, DataKey<?> key) {
        @SuppressWarnings("unchecked")
        var clavaNodes = (List<B>) node.get(key);
        var newClavaNodes = new ArrayList<B>(clavaNodes.size());

        for (B oldNode : clavaNodes) {
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

    /**
     * Replaces a node in an optional field of the given node.
     *
     * @param node the node containing the optional field
     * @param key  the key identifying the optional field
     * @param <N>  the type of the node
     */
    @SuppressWarnings("unchecked")
    private <N extends B> void replaceOptional(N node, DataKey<?> key) {
        var value = ((Optional<B>) node.get(key)).get();

        replacementDetector.apply(value).ifPresent(normalizedNode -> {
            node.set((DataKey<Optional<B>>) key, Optional.of(normalizedNode));
            replacedNodes++;
        });
    }

    /**
     * Replaces a node in a data node field of the given node.
     *
     * @param node the node containing the data node field
     * @param key  the key identifying the data node field
     * @param <N>  the type of the node
     */
    @SuppressWarnings("unchecked")
    private <N extends B> void replaceDataNode(N node, DataKey<?> key) {
        var value = node.getBaseClass().cast(node.get(key));

        replacementDetector.apply(value).ifPresent(replacementNode -> {
            node.set((DataKey<B>) key, replacementNode);
            replacedNodes++;
        });
    }
}
