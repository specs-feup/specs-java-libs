package org.suikasoft.jOptions.storedefinition;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.DataStore.ListDataStore;
import org.suikasoft.jOptions.DataStore.MemoizedDataStore;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

class AStoreDefinitionConcurrencyTest {

    private static final int THREADS = 32;
    private static final int SHARED_KEYS = 2_048;
    private static final int STORE_KEYS = 8;
    private static final int ROUNDS = 8;

    @Test
    void concurrentFirstReadersAndStoreCreationSeeCompleteDefinitions() throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        try {
            for (int round = 0; round < ROUNDS; round++) {
                final int currentRound = round;
                List<DataKey<?>> sharedKeys = keys("shared-" + currentRound + "-", SHARED_KEYS);
                TestStoreDefinition shared = new TestStoreDefinition("shared-" + currentRound, sharedKeys);
                List<TestStoreDefinition> independent = IntStream.range(0, THREADS)
                        .mapToObj(thread -> new TestStoreDefinition("independent-" + currentRound + "-" + thread,
                                keys("independent-" + currentRound + "-" + thread + "-", STORE_KEYS)))
                        .toList();
                CyclicBarrier gate = new CyclicBarrier(THREADS + 1);
                List<Future<Integer>> futures = new ArrayList<>(THREADS);

                for (int thread = 0; thread < THREADS; thread++) {
                    final int threadIndex = thread;
                    futures.add(executor.submit(() -> {
                        gate.await();

                        Map<String, DataKey<?>> map = shared.getKeyMap();
                        TestStoreDefinition definition = independent.get(threadIndex);
                        DataKey<?> firstKey = definition.getKeys().get(0);
                        DataKey<?> secondKey = definition.getKeys().get(1);
                        ListDataStore eager = new ListDataStore(definition);
                        eager.setRaw(firstKey.getName(), "eager");
                        eager.get(firstKey.getName());
                        MemoizedDataStore lazy = new MemoizedDataStore(definition, List.of(secondKey), key -> "lazy");
                        lazy.get(secondKey.getName());

                        return map.size();
                    }));
                }

                gate.await();
                for (Future<Integer> future : futures) {
                    assertThat(future.get()).isEqualTo(SHARED_KEYS);
                }
            }
        } finally {
            executor.shutdownNow();
        }
    }

    private static List<DataKey<?>> keys(String prefix, int count) {
        List<DataKey<?>> keys = new ArrayList<>(count);
        for (int index = 0; index < count; index++) {
            keys.add(KeyFactory.string(prefix + index));
        }
        return keys;
    }

    private static final class TestStoreDefinition extends AStoreDefinition {
        private TestStoreDefinition(String name, List<DataKey<?>> keys) {
            super(name, keys);
        }
    }
}
