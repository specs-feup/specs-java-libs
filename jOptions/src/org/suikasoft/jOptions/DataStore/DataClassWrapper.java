/**
 * Copyright 2020 SPeCS.
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

package org.suikasoft.jOptions.DataStore;

import java.util.Collection;
import java.util.Optional;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

// public class DataClassWrapper implements DataClass<DataClassWrapper> {
public abstract class DataClassWrapper<T extends DataClass<T>> implements DataClass<T> {

    private final DataClass<?> data;

    public DataClassWrapper(DataClass<?> data) {
        this.data = data;
    }

    protected abstract T getThis();

    @Override
    public String getDataClassName() {
        return data.getDataClassName();
    }

    @Override
    public <K> K get(DataKey<K> key) {
        return data.get(key);
    }

    @Override
    public <K, E extends K> T set(DataKey<K> key, E value) {
        data.set(key, value);
        return getThis();
    }

    @Override
    public T set(T instance) {
        for (var key : instance.getDataKeysWithValues()) {
            var keyString = key.getKey();
            data.setValue(keyString, instance.getValue(keyString));
        }

        return getThis();
    }

    @Override
    public <VT> boolean hasValue(DataKey<VT> key) {
        return data.hasValue(key);
    }

    @Override
    public Collection<DataKey<?>> getDataKeysWithValues() {
        return data.getDataKeysWithValues();
    }

    @Override
    public Optional<StoreDefinition> getStoreDefinitionTry() {
        return data.getStoreDefinitionTry();
    }

    @Override
    public boolean isClosed() {
        return data.isClosed();
    }

    @Override
    public String toString() {
        return toInlinedString();
    }
}
