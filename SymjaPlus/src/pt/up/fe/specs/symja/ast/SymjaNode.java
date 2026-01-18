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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.symja.ast;

import java.util.Collection;
import java.util.Collections;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;
import org.suikasoft.jOptions.treenode.DataNode;

import pt.up.fe.specs.util.SpecsSystem;

/**
 * Base class for nodes in the Symja AST.
 */
public class SymjaNode extends DataNode<SymjaNode> {

    /**
     * Creates a new node of the given class with no children.
     *
     * @param nodeClass the class of the node
     * @param <T>       the node type
     * @return a new node instance
     */
    public static <T extends SymjaNode> T newNode(Class<T> nodeClass) {
        return newNode(nodeClass, Collections.emptyList());
    }

    /**
     * Creates a new node of the given class with the specified children.
     *
     * @param nodeClass the class of the node
     * @param children  the child nodes
     * @param <T>       the node type
     * @return a new node instance
     */
    public static <T extends SymjaNode> T newNode(Class<T> nodeClass, Collection<? extends SymjaNode> children) {
        DataStore data = DataStore.newInstance(StoreDefinitions.fromInterface(nodeClass), true);
        return SpecsSystem.newInstance(nodeClass, data, children);
    }

    /**
     * Constructs a SymjaNode with the given data and children.
     *
     * @param data     the data store
     * @param children the child nodes
     */
    public SymjaNode(DataStore data, Collection<? extends SymjaNode> children) {
        super(data, children);
    }

    @Override
    protected SymjaNode getThis() {
        return this;
    }

    @Override
    protected Class<SymjaNode> getBaseClass() {
        return SymjaNode.class;
    }
}
