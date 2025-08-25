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

import java.util.List;
import java.util.Optional;

import org.suikasoft.jOptions.Datakey.DataKey;

/**
 * Enum representing the type of property associated with a DataNode key.
 */
public enum PropertyWithNodeType {
    DATA_NODE,
    OPTIONAL,
    LIST,
    NOT_FOUND;
    /**
     * Determines the type of a DataKey for a given DataNode.
     *
     * @param node the DataNode
     * @param key the DataKey
     * @return the property type
     */
    public static PropertyWithNodeType getKeyType(DataNode<?> node, DataKey<?> key) {
        // Handle null node
        if (node == null) {
            return NOT_FOUND;
        }
        
        // DataNode keys
        if (node.getBaseClass().isAssignableFrom(key.getValueClass())) {
            return DATA_NODE;
        }

        // Optional nodes
        if (Optional.class.isAssignableFrom(key.getValueClass())) {
            return OPTIONAL;
        }

        // List of nodes
        if (List.class.isAssignableFrom(key.getValueClass())) {
            return LIST;
        }
        return NOT_FOUND;
    }
}
