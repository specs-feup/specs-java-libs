package org.suikasoft.jOptions.storedefinition;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

class StoreDefinitionConcurrencyTest {

    @Test
    void readersNeverSeePartiallyInitializedKeyMap() throws InterruptedException {
        int keyCount = 50_000;
        var keys = new ArrayList<DataKey<?>>(keyCount);
        for (int i = 0; i < keyCount; i++) {
            keys.add(KeyFactory.string("key" + i, ""));
        }
        var definition = StoreDefinition.newInstance("concurrent", keys);
        var start = new CountDownLatch(1);
        var writer = new Thread(() -> {
            start.countDown();
            definition.getKeyMap();
        });
        writer.start();
        start.await();

        boolean partial = false;
        while (writer.isAlive()) {
            int size = definition.getKeyMap().size();
            if (size > 0 && size < keyCount) {
                partial = true;
                break;
            }
        }
        writer.join();
        assertFalse(partial, "a reader saw an incomplete key map and could silently drop AST fields");
        assertEquals(keyCount, definition.getKeyMap().size());
    }
}
