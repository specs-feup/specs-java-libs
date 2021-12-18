/**
 * Copyright 2018 SPeCS.
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

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.suikasoft.jOptions.DataStore.DataClass;
import org.suikasoft.jOptions.DataStore.GenericDataClass;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

import pt.up.fe.specs.util.system.Copyable;
import pt.up.fe.specs.util.treenode.ATreeNode;

public abstract class DataNode<K extends DataNode<K>> extends ATreeNode<K>
        implements DataClass<K>, Copyable<K> {

    private final DataStore data;
    private final DataClass<K> dataClass;

    public DataNode(DataStore data, Collection<? extends K> children) {
        super(children);

        // SpecsCheck.checkArgument(dataI instanceof ListDataStore,
        // () -> "Expected ListDataStore, found " + dataI.getClass());

        this.data = data;

        // To avoid implementing methods again
        this.dataClass = new GenericDataClass<>(this.data);
    }

    protected DataStore getData() {
        return data;
    }

    /**
     * 
     * @return the class of the base node class of the tree
     */
    protected abstract Class<K> getBaseClass();

    /*** DataClass IMPLEMENTATION ***/

    @Override
    public String getDataClassName() {
        return data.getName();
    }

    @Override
    public <T> T get(DataKey<T> key) {
        return data.get(key);
    }

    /**
     * Generic method for setting values.
     * 
     * <p>
     * If null is passed as value, removes current value associated with given key.
     * 
     * @param key
     * @param value
     */
    @Override
    public <T, E extends T> K set(DataKey<T> key, E value) {
        // If value is null, remove value, if present
        if (value == null) {
            if (data.hasValue(key)) {
                data.remove(key);
            }

            return getThis();
        }

        data.put(key, value);

        return getThis();
    }

    @Override
    public K set(K instance) {
        return dataClass.set(instance);
    }

    @Override
    public <T> boolean hasValue(DataKey<T> key) {
        return data.hasValue(key);
    }

    @Override
    public Collection<DataKey<?>> getDataKeysWithValues() {
        return dataClass.getDataKeysWithValues();
    }

    @Override
    public boolean isClosed() {
        return true;
    }

    @SuppressWarnings("unchecked") // getClass() will always return a Class<K>
    @Override
    protected K copyPrivate() {
        var newNode = newInstance((Class<K>) getClass(), Collections.emptyList());

        // Copy all data
        for (var key : getDataKeysWithValues()) {
            // var stringValue = key.copy((Object) get(key));
            // var copyValue = key.decode(stringValue);
            // newNode.setValue(key.getName(), copyValue);
            newNode.setValue(key.getName(), key.copyRaw(get(key)));
            // newNode.setValue(key.getName(), get(key));
        }

        return newNode;
    }

    /*** STATIC HELPER METHODS ***/

    public static <K extends DataNode<K>, T extends K> DataStore newDataStore(Class<T> nodeClass) {
        DataStore data = DataStore.newInstance(StoreDefinitions.fromInterface(nodeClass), true);
        return data;
    }

    /**
     * Creates a new node using the same data as this node.
     * 
     * @param nodeClass
     * @param children
     * @return
     */
    public static <K extends DataNode<K>, T extends K> K newInstance(Class<T> nodeClass, List<K> children) {

        DataStore newDataStore = DataNode.newDataStore(nodeClass);

        try {
            Constructor<T> constructorMethod = nodeClass.getConstructor(DataStore.class, Collection.class);
            try {
                return nodeClass.cast(constructorMethod.newInstance(newDataStore, children));
            } catch (Exception e) {
                throw new RuntimeException("Could not call constructor for ClavaNode", e);
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not create constructor for ClavaNode", e);
        }
    }

    @Override
    public Optional<StoreDefinition> getStoreDefinitionTry() {
        return getData().getStoreDefinitionTry();
    }

    @Override
    public String toContentString() {
        return getData().toInlinedString();
    }

}
