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
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.suikasoft.jOptions.Datakey.DataKey;

import pt.up.fe.specs.util.exceptions.CaseNotDefinedException;

/**
 * Manages DataKey properties that return an instance of DataNode.
 *
 * Provides methods to retrieve DataKeys associated with DataNode properties for a given node.
 *
 * @author JBispo
 */
public class PropertyWithNodeManager {

    /**
     * Cache key that includes both the node class and DataStore configuration to ensure
     * correct cache behavior when different DataStore configurations are used with the same node class.
     */
    private static class CacheKey {
        private final Class<?> nodeClass;
        private final String storeDefinitionId; // unique identifier for the DataStore configuration
        
        public CacheKey(DataNode<?> node) {
            this.nodeClass = node.getClass();
            // Create a unique identifier based on StoreDefinition presence and identity
            Optional<org.suikasoft.jOptions.storedefinition.StoreDefinition> storeDefOpt = node.getStoreDefinitionTry();
            if (storeDefOpt.isPresent()) {
                // Use StoreDefinition name and hashCode to create unique identifier
                org.suikasoft.jOptions.storedefinition.StoreDefinition storeDef = storeDefOpt.get();
                this.storeDefinitionId = storeDef.getName() + "_" + Integer.toString(storeDef.hashCode());
            } else {
                // Use a special identifier for nodes without StoreDefinition
                this.storeDefinitionId = "NO_STORE_DEFINITION";
            }
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            CacheKey cacheKey = (CacheKey) obj;
            return Objects.equals(nodeClass, cacheKey.nodeClass) &&
                   Objects.equals(storeDefinitionId, cacheKey.storeDefinitionId);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(nodeClass, storeDefinitionId);
        }
        
        @Override
        public String toString() {
            return "CacheKey{" + nodeClass.getSimpleName() + ":" + storeDefinitionId + "}";
        }
    }

    /**
     * Maps cache keys to a List of DataKeys corresponding to the properties of that class and configuration 
     * that return DataNode instances.
     */
    private static final Map<CacheKey, List<DataKey<?>>> POSSIBLE_KEYS_WITH_NODES = new ConcurrentHashMap<>();

    /**
     * Retrieves all keys that can potentially have DataNodes for a given node.
     *
     * @param node the DataNode instance
     * @return a list of DataKeys that can potentially have DataNodes
     */
    private <K extends DataNode<?>> List<DataKey<?>> getPossibleKeysWithNodes(K node) {
        CacheKey cacheKey = new CacheKey(node);
        List<DataKey<?>> keys = POSSIBLE_KEYS_WITH_NODES.get(cacheKey);
        if (keys == null) {
            keys = findKeysWithNodes(node);

            // Add to map
            POSSIBLE_KEYS_WITH_NODES.put(cacheKey, keys);
        }

        return keys;
    }

    /**
     * Finds keys that map to DataNode instances for a given node.
     *
     * @param node the DataNode instance
     * @return a list of DataKeys that map to DataNode instances
     */
    private static <K extends DataNode<?>> List<DataKey<?>> findKeysWithNodes(K node) {
        List<DataKey<?>> keysWithNodes = new ArrayList<>();

        // Check if node has a StoreDefinition - if not, return empty list
        Optional<org.suikasoft.jOptions.storedefinition.StoreDefinition> storeDefOpt = node.getStoreDefinitionTry();
        if (!storeDefOpt.isPresent()) {
            return keysWithNodes; // Return empty list for nodes without StoreDefinition
        }

        // Get all the keys that map this DataNode
        for (DataKey<?> key : storeDefOpt.get().getKeys()) {

            var keyType = PropertyWithNodeType.getKeyType(node, key);

            if (keyType != PropertyWithNodeType.NOT_FOUND) {
                keysWithNodes.add(key);
            }
        }

        return keysWithNodes;
    }

    /**
     * Retrieves keys that currently have nodes assigned for a given node.
     *
     * @param node the DataNode instance
     * @return a list of DataKeys that currently have nodes assigned
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
                if (value == null || !value.isPresent()) {
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
                if (list == null || list.isEmpty()) {
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
    }

}
