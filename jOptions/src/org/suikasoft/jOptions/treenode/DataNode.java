/*
 * Copyright 2018 SPeCS Research Group.
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

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.system.Copyable;
import pt.up.fe.specs.util.treenode.ATreeNode;

/**
 * Abstract base class for tree nodes that hold a DataStore and support DataClass and Copyable interfaces.
 *
 * @param <K> the type of DataNode
 */
public abstract class DataNode<K extends DataNode<K>> extends ATreeNode<K>
        implements DataClass<K>, Copyable<K> {

    private final DataStore data;
    private final DataClass<K> dataClass;

    /**
     * Constructs a DataNode with the given data and children.
     *
     * @param data the DataStore associated with this node
     * @param children the child nodes of this node
     */
    public DataNode(DataStore data, Collection<? extends K> children) {
        super(children);

        this.data = data;

        // To avoid implementing methods again
        this.dataClass = new GenericDataClass<>(this.data);
    }

    /**
     * Retrieves the DataStore associated with this node.
     *
     * @return the DataStore
     */
    public DataStore getData() {
        return data;
    }

    /**
     * Returns the class of the base node class of the tree.
     *
     * @return the base class
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
     * @param key the key to set
     * @param value the value to set
     * @return the current instance
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
        // Create a new DataStore that can hold the same types of keys as the original
        String dataStoreName = data.getName() != null ? data.getName() : "CopiedDataStore";
        DataStore newDataStore = DataStore.newInstance(dataStoreName, data);
        
        return newInstanceWithDataStore((Class<K>) getClass(), newDataStore, Collections.emptyList());

    }

    /**
     * Creates a new node instance with the given DataStore.
     */
    private static <K extends DataNode<K>, T extends K> K newInstanceWithDataStore(Class<T> nodeClass, DataStore dataStore, List<K> children) {
        try {
            Constructor<T> constructorMethod = nodeClass.getConstructor(DataStore.class, Collection.class);
            try {
                return nodeClass.cast(constructorMethod.newInstance(dataStore, children));
            } catch (Exception e) {
                throw new RuntimeException("Could not call constructor for DataNode", e);
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not create constructor for DataNode", e);
        }
    }

    /*** STATIC HELPER METHODS ***/

    /**
     * Creates a new DataStore for the given node class.
     *
     * @param nodeClass the class of the node
     * @return a new DataStore instance
     */
    public static <K extends DataNode<K>, T extends K> DataStore newDataStore(Class<T> nodeClass) {
        return DataStore.newInstance(StoreDefinitions.fromInterface(nodeClass), true);
    }

    /**
     * Creates a new node using the same data as this node.
     *
     * @param nodeClass the class of the node
     * @param children the child nodes
     * @return a new instance of the node
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

    /**
     * Returns the system-specific newline character.
     *
     * @return the newline character
     */
    protected String ln() {
        return SpecsIo.getNewline();
    }
}
