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

package pt.up.fe.specs.symja.ast;

import java.util.Collection;
import java.util.Collections;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;
import org.suikasoft.jOptions.treenode.DataNode;

import pt.up.fe.specs.util.SpecsSystem;

public class SymjaNode extends DataNode<SymjaNode> {

    public static <T extends SymjaNode> T newNode(Class<T> nodeClass) {
        return newNode(nodeClass, Collections.emptyList());
    }

    public static <T extends SymjaNode> T newNode(Class<T> nodeClass, Collection<? extends SymjaNode> children) {
        DataStore data = DataStore.newInstance(StoreDefinitions.fromInterface(nodeClass), true);
        return SpecsSystem.newInstance(nodeClass, data, children);
        // return (T) new SymjaNode(data, children);
    }

    public SymjaNode(DataStore data, Collection<? extends SymjaNode> children) {
        super(data, children);
    }

    @Override
    public String toContentString() {
        return getData().toInlinedString();
    }

    @Override
    protected SymjaNode getThis() {
        return this;
    }

}
