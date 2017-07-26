/**
 * Copyright 2017 SPeCS.
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

package pt.up.fe.specs.util.lazy;

// import static org.junit.Assert.*;

import org.junit.Test;

import pt.up.fe.specs.util.SpecsStrings;

public class LazyTester {

    @Test
    public void test() {
        Lazy<Integer> threadSafeLazy = new ThreadSafeLazy<>(() -> 1);
        Lazy<Integer> synchronizedLazy = new SynchronizedLazy<>(() -> 1);
        // fail("Not yet implemented");

        int limit = 10000000;

        int safeAcc = 0;
        long safeNano = System.nanoTime();
        for (int i = 0; i < limit; i++) {
            safeAcc += threadSafeLazy.get();
        }
        System.out.println(SpecsStrings.takeTime("SafeLazy (" + safeAcc + "):", safeNano));

        int syncAcc = 0;
        long syncNano = System.nanoTime();
        for (int i = 0; i < limit; i++) {
            syncAcc += synchronizedLazy.get();
        }
        System.out.println(SpecsStrings.takeTime("SyncLazy (" + syncAcc + "):", syncNano));

        safeAcc = 0;
        safeNano = System.nanoTime();
        for (int i = 0; i < limit; i++) {
            safeAcc += threadSafeLazy.get();
        }
        System.out.println(SpecsStrings.takeTime("SafeLazy (" + safeAcc + "):", safeNano));

        syncAcc = 0;
        syncNano = System.nanoTime();
        for (int i = 0; i < limit; i++) {
            syncAcc += synchronizedLazy.get();
        }
        System.out.println(SpecsStrings.takeTime("SyncLazy (" + syncAcc + "):", syncNano));
    }

}
