/**
 * Copyright 2026 SPeCS.
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

package org.suikasoft.jOptions.DataStore;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Experimental field memoization in ordinary ListDataStore slots.
 * An unloaded slot contains a shared marker, replaced by the value on first read.
 * Like ListDataStore, concurrent mutation of a store is not supported.
 */
public class MemoizedDataStore extends ListDataStore {
    private static final Object UNLOADED = new Object();
    private static final java.util.concurrent.ConcurrentHashMap<StoreDefinition,List<DataKey<?>>> KEY_LAYOUTS
            = new java.util.concurrent.ConcurrentHashMap<>();
    private final List<DataKey<?>> allKeys;
    private final List<DataKey<?>> deferredKeys;
    private final Function<DataKey<?>, Object> decoder;
    private long materializedCount;
    private boolean strict;

    /** Decoder must not retain parser indexes and must produce independently mutable values per call. */
    public MemoizedDataStore(StoreDefinition definition, Collection<DataKey<?>> deferredKeys,
            Function<DataKey<?>, Object> decoder) {
        super(definition);
        this.allKeys = KEY_LAYOUTS.computeIfAbsent(definition, d -> List.copyOf(d.getKeys()));
        this.deferredKeys = List.copyOf(deferredKeys);
        this.decoder = Objects.requireNonNull(decoder);
        for (DataKey<?> key : this.deferredKeys) {
            if (!definition.hasKey(key.getName())) throw new IllegalArgumentException("Unknown deferred key " + key);
            super.setRaw(key.getName(), UNLOADED);
        }
    }

    public long getMaterializedCount() { return materializedCount; }
    public int getDeferredCount() { return deferredKeys.size(); }

    @Override
    protected Object resolveValue(int index, Object value) {
        if (value != UNLOADED) return value;
        DataKey<?> key = allKeys.get(index);
        Object decoded = decoder.apply(key);
        if (decoded != null && key.verifyValueClass() && !key.getValueClass().isInstance(decoded))
            throw new IllegalArgumentException("Decoded value has wrong type for " + key.getName());
        super.setRaw(key.getName(), decoded);
        materializedCount++;
        return decoded;
    }

    /** Typed writes do not request the old value and therefore do not decode it. */
    @Override
    public <T, E extends T> DataStore set(DataKey<T> key, E value) {
        Objects.requireNonNull(value, "Use remove instead of setting a null typed value");
        if (key.verifyValueClass() && !key.getValueClass().isInstance(value))
            throw new IllegalArgumentException("Wrong value type for " + key.getName());
        super.setRaw(key.getName(), value);
        return this;
    }

    /** Raw writes return the previous raw value, which requires decoding it if still pending. */
    @Override
    public Optional<Object> setRaw(String name, Object value) {
        if (!getStoreDefinition().hasKey(name)) return Optional.empty();
        Object previous = get(name);
        super.setRaw(name, value);
        return Optional.ofNullable(previous);
    }

    @Override
    public void setStrict(boolean strict) {
        this.strict = strict;
        super.setStrict(strict);
    }

    /** Pending payloads stay deferred; realized values use the normal DataKey copy policy. */
    @Override
    public DataStore copy() {
        var copy = new MemoizedDataStore(getStoreDefinition(), deferredKeys, decoder);
        copy.setStrict(strict);
        var keys = allKeys;
        for (int i = 0; i < keys.size(); i++) {
            Object value = getRawValue(i);
            if (value != UNLOADED) copy.setRealized(keys.get(i), value);
        }
        return copy;
    }

    private void setRealized(DataKey<?> key, Object value) {
        super.setRaw(key.getName(), value == null ? null : key.copyRaw(value));
    }

    private void materializeAll() {
        for (DataKey<?> key : deferredKeys) get(key.getName());
    }

    @Override
    public int hashCode() {
        materializeAll();
        return super.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        materializeAll();
        ((MemoizedDataStore) other).materializeAll();
        return super.equals(other);
    }
}
