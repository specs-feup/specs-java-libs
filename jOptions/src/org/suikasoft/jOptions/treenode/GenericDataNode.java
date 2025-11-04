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
 * specific language governing permissions and limitations under the License.
 */

package org.suikasoft.jOptions.treenode;

import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Generic implementation of a DataNode for use when a specific node type is not
 * required.
 */
public class GenericDataNode extends DataNode<GenericDataNode> {
    /**
     * Constructs a GenericDataNode with the given DataStore and children.
     *
     * @param data     the DataStore for this node
     * @param children the child nodes
     */
    public GenericDataNode(DataStore data, Collection<? extends GenericDataNode> children) {
        super(data, children);
    }

    /**
     * Constructs a GenericDataNode with a default DataStore and no children.
     */
    public GenericDataNode() {
        this(DataStore.newInstance("GenericDataNode"), null);
    }

    /**
     * Returns this node instance.
     *
     * @return this node
     */
    @Override
    protected GenericDataNode getThis() {
        return this;
    }

    /**
     * Returns the base class for this node type.
     *
     * @return the GenericDataNode class
     */
    @Override
    protected Class<GenericDataNode> getBaseClass() {
        return GenericDataNode.class;
    }
}
