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
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.suikasoft.jOptions.Datakey.DataKey;

import pt.up.fe.specs.util.exceptions.CaseNotDefinedException;

/**
 * Manages DataKey properties that return an instance of DataNode.
 * 
 * @author JBispo
 *
 */
public class PropertyWithNodeManager {

    /**
     * Maps Type classes to a List of DataKeys corresponding to the properties of that class that return DataNode
     * instances.
     */
    @SuppressWarnings("rawtypes")
    private static final Map<Class<? extends DataNode>, List<DataKey<?>>> POSSIBLE_KEYS_WITH_NODES = new ConcurrentHashMap<>();

    /**
     * All keys that can potentially have DataNodes.
     * 
     * @return
     */
    private <K extends DataNode<?>> List<DataKey<?>> getPossibleKeysWithNodes(K node) {
        List<DataKey<?>> keys = POSSIBLE_KEYS_WITH_NODES.get(node.getClass());
        if (keys == null) {
            keys = findKeysWithNodes(node);

            // Add to map
            POSSIBLE_KEYS_WITH_NODES.put(node.getClass(), keys);
        }

        return keys;
    }

    private static <K extends DataNode<?>> List<DataKey<?>> findKeysWithNodes(K node) {
        List<DataKey<?>> keysWithNodes = new ArrayList<>();

        // Get all the keys that map this DataNode
        for (DataKey<?> key : node.getStoreDefinition().getKeys()) {

            var keyType = PropertyWithNodeType.getKeyType(node, key);

            if (keyType != PropertyWithNodeType.NOT_FOUND) {
                keysWithNodes.add(key);
            }
        }

        return keysWithNodes;
    }

    /**
     * Keys that currently have nodes assigned.
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public <K extends DataNode<?>> List<DataKey<?>> getKeysWithNodes(K node) {

        List<DataKey<?>> keys = new ArrayList<>();
        var baseClass = node.getBaseClass();

        for (DataKey<?> key : getPossibleKeysWithNodes(node)) {

            if (!node.hasValue(key)) {
                continue;
            }

            var keyType = PropertyWithNodeType.getKeyType(node, key);

            switch (keyType) {
            case DATA_NODE:
                keys.add(key);
                break;
            case OPTIONAL:
                DataKey<Optional<?>> optionalKey = (DataKey<Optional<?>>) key;
                Optional<?> value = node.get(optionalKey);
                if (!value.isPresent()) {
                    break;
                }

                Object possibleNode = value.get();

                if (!(baseClass.isInstance(possibleNode))) {
                    break;
                }

                keys.add(key);
                break;
            case LIST:
                DataKey<List<?>> listKey = (DataKey<List<?>>) key;
                List<?> list = node.get(listKey);
                if (list.isEmpty()) {
                    break;
                }

                // Check if elements of the list are instances of the base class
                boolean dataNodeList = list.stream()
                        .filter(baseClass::isInstance)
                        .count() == list.size();

                if (!dataNodeList) {
                    break;
                }

                keys.add(key);
                break;
            case NOT_FOUND:
                break;
            default:
                throw new CaseNotDefinedException(keyType);
            }

        }

        return keys;
        // ClavaLog.info("Case not supported yet:" + keyWithNode);

    }

}
