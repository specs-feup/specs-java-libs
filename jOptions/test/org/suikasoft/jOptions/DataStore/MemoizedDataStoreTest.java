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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

class MemoizedDataStoreTest {

    @Test
    void deferredValuesArePresenceVisibleAndDecodedOnce() {
        DataKey<String> text = KeyFactory.string("text");
        DataKey<Integer> number = KeyFactory.integer("number");
        StoreDefinition definition = definition(text, number);
        AtomicInteger decodes = new AtomicInteger();

        MemoizedDataStore store = new MemoizedDataStore(definition, List.of(text), key -> {
            decodes.incrementAndGet();
            return "from-dump";
        });

        assertThat(store).isInstanceOf(ListDataStore.class);
        assertThat(store.getDeferredCount()).isEqualTo(1);
        assertThat(store.getMaterializedCount()).isZero();
        assertThat(store.hasValue(text)).isTrue();
        assertThat(store.getKeysWithValues()).containsExactly("text");
        assertThat(decodes).hasValue(0);

        assertThat(store.get(text)).isEqualTo("from-dump");
        assertThat(store.get(text)).isEqualTo("from-dump");
        assertThat(store.get("text")).isEqualTo("from-dump");
        assertThat(decodes).hasValue(1);
        assertThat(store.getMaterializedCount()).isEqualTo(1);
    }

    @Test
    void typedWriteOverridesDeferredValueWithoutDecoding() {
        DataKey<String> text = KeyFactory.string("text");
        StoreDefinition definition = definition(text);
        AtomicInteger decodes = new AtomicInteger();
        MemoizedDataStore store = new MemoizedDataStore(definition, List.of(text), key -> {
            decodes.incrementAndGet();
            return "from-dump";
        });

        store.set(text, "written");

        assertThat(store.get(text)).isEqualTo("written");
        assertThat(store.get("text")).isEqualTo("written");
        assertThat(decodes).hasValue(0);
        assertThat(store.getMaterializedCount()).isZero();
    }

    @Test
    void rawWriteReturnsPreviousDeferredValueAndNullTombstonesIt() {
        DataKey<String> text = KeyFactory.string("text");
        MemoizedDataStore store = new MemoizedDataStore(definition(text), List.of(text), key -> "from-dump");

        assertThat(store.setRaw("text", "written")).contains("from-dump");
        assertThat(store.get(text)).isEqualTo("written");

        assertThat(store.setRaw("text", null)).contains("written");
        assertThat(store.hasValue(text)).isFalse();
        assertThat(store.get(text)).isEmpty();
    }

    @Test
    void copyKeepsPendingValuesLazyAndCopiesMaterializedMutableValues() {
        DataKey<List<String>> values = KeyFactory.list("values", String.class);
        AtomicInteger decodes = new AtomicInteger();
        MemoizedDataStore store = new MemoizedDataStore(definition(values), List.of(values), key -> {
            decodes.incrementAndGet();
            return new ArrayList<>(List.of("dump"));
        });

        MemoizedDataStore pendingCopy = (MemoizedDataStore) store.copy();
        assertThat(decodes).hasValue(0);
        assertThat(pendingCopy.get(values)).containsExactly("dump");
        assertThat(decodes).hasValue(1);

        List<String> originalValues = store.get(values);
        MemoizedDataStore materializedCopy = (MemoizedDataStore) store.copy();
        materializedCopy.get(values).add("copy");

        assertThat(originalValues).containsExactly("dump");
        assertThat(materializedCopy.get(values)).containsExactly("dump", "copy");
        assertThat(decodes).hasValue(2);
    }

    @Test
    void removeUsesDefaultAfterTombstoningDeferredValue() {
        DataKey<String> text = KeyFactory.string("text");
        MemoizedDataStore store = new MemoizedDataStore(definition(text), List.of(text), key -> "from-dump");

        assertThat(store.remove(text)).contains("from-dump");
        assertThat(store.hasValue(text)).isFalse();
        assertThat(store.get(text)).isEmpty();
        assertThat(store.hasValue(text)).isTrue();
    }

    @Test
    void equalityAndHashingObserveDeferredValues() {
        DataKey<String> text = KeyFactory.string("text");
        StoreDefinition definition = definition(text);
        var first = new MemoizedDataStore(definition, List.of(text), key -> "same");
        var second = new MemoizedDataStore(definition, List.of(text), key -> "same");
        var different = new MemoizedDataStore(definition, List.of(text), key -> "different");
        assertThat(first).isEqualTo(second).isNotEqualTo(different);
        assertThat(first.hashCode()).isEqualTo(second.hashCode());
    }

    @Test
    void failedDecodeCanBeRetriedAndStrictReadsStillWork() {
        DataKey<String> text = KeyFactory.string("text");
        AtomicInteger attempts = new AtomicInteger();
        var store = new MemoizedDataStore(definition(text), List.of(text), key -> {
            if (attempts.getAndIncrement() == 0) throw new IllegalStateException("temporary failure");
            return "loaded";
        });
        store.setStrict(true);
        org.assertj.core.api.Assertions.assertThatThrownBy(() -> store.get(text))
                .isInstanceOf(IllegalStateException.class);
        assertThat(store.getMaterializedCount()).isZero();
        assertThat(store.get(text)).isEqualTo("loaded");
        assertThat(store.getMaterializedCount()).isEqualTo(1);
    }

    private static StoreDefinition definition(DataKey<?>... keys) {
        return StoreDefinition.newInstance("Memoized", keys);
    }
}
