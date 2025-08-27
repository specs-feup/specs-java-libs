package pt.up.fe.specs.util.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link CachedValue} class.
 * Tests soft reference-based value caching with supplier-driven refresh.
 * 
 * @author Generated Tests
 */
class CachedValueTest {

    private Supplier<String> supplier;
    private CachedValue<String> cachedValue;
    private int supplierCallCount;

    @BeforeEach
    void setUp() {
        supplierCallCount = 0;
        supplier = () -> {
            supplierCallCount++;
            return "value_" + supplierCallCount;
        };
        cachedValue = new CachedValue<>(supplier);
    }

    @Nested
    @DisplayName("Constructor and Initial State")
    class ConstructorTests {

        @Test
        @DisplayName("Should create cached value with initial supplier call")
        void testConstructorCallsSupplier() {
            assertThat(supplierCallCount).isEqualTo(1);
            assertThat(cachedValue).isNotNull();
        }

        @Test
        @DisplayName("Should handle null supplier gracefully")
        void testNullSupplier() {
            assertThatThrownBy(() -> new CachedValue<>(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle supplier returning null")
        void testSupplierReturningNull() {
            Supplier<String> nullSupplier = () -> null;
            CachedValue<String> nullCachedValue = new CachedValue<>(nullSupplier);

            assertThat(nullCachedValue.getValue()).isNull();
        }
    }

    @Nested
    @DisplayName("Get Value Operations")
    class GetValueTests {

        @Test
        @DisplayName("Should return cached value without additional supplier calls")
        void testGetValueReturnsCachedValue() {
            String firstCall = cachedValue.getValue();
            String secondCall = cachedValue.getValue();
            String thirdCall = cachedValue.getValue();

            assertThat(firstCall).isEqualTo("value_1");
            assertThat(secondCall).isEqualTo("value_1");
            assertThat(thirdCall).isEqualTo("value_1");
            assertThat(supplierCallCount).isEqualTo(1); // Only constructor call
        }

        @Test
        @DisplayName("Should recreate value when cache is cleared by garbage collector")
        void testValueRecreationAfterGC() {
            // Get initial value
            String initialValue = cachedValue.getValue();
            assertThat(initialValue).isEqualTo("value_1");
            assertThat(supplierCallCount).isEqualTo(1);

            // Force garbage collection multiple times to try to clear soft reference
            for (int i = 0; i < 10; i++) {
                System.gc();
            }

            // Note: We can't guarantee GC will clear soft references in a test,
            // but we can verify the behavior when it does happen
            String valueAfterGC = cachedValue.getValue();
            assertThat(valueAfterGC).isNotNull();
            assertThat(valueAfterGC).startsWith("value_");
        }

        @Test
        @DisplayName("Should handle supplier throwing exceptions")
        void testSupplierException() {
            Supplier<String> exceptionSupplier = () -> {
                throw new RuntimeException("Supplier error");
            };

            assertThatThrownBy(() -> new CachedValue<>(exceptionSupplier))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Supplier error");
        }

        @Test
        @DisplayName("Should handle different value types")
        void testDifferentValueTypes() {
            CachedValue<Integer> intCached = new CachedValue<>(() -> 42);
            CachedValue<Boolean> boolCached = new CachedValue<>(() -> true);

            assertThat(intCached.getValue()).isEqualTo(42);
            assertThat(boolCached.getValue()).isTrue();
        }
    }

    @Nested
    @DisplayName("Stale Operations")
    class StaleOperationTests {

        @Test
        @DisplayName("Should refresh value when marked as stale")
        void testStaleRefreshesValue() {
            String initialValue = cachedValue.getValue();
            assertThat(initialValue).isEqualTo("value_1");
            assertThat(supplierCallCount).isEqualTo(1);

            cachedValue.stale();

            String refreshedValue = cachedValue.getValue();
            assertThat(refreshedValue).isEqualTo("value_2");
            assertThat(supplierCallCount).isEqualTo(2);
        }

        @Test
        @DisplayName("Should call supplier immediately when marked as stale")
        void testStaleCallsSupplierImmediately() {
            assertThat(supplierCallCount).isEqualTo(1);

            cachedValue.stale();

            assertThat(supplierCallCount).isEqualTo(2);
        }

        @Test
        @DisplayName("Should allow multiple stale calls")
        void testMultipleStaleOperations() {
            assertThat(supplierCallCount).isEqualTo(1);

            cachedValue.stale();
            assertThat(supplierCallCount).isEqualTo(2);

            cachedValue.stale();
            assertThat(supplierCallCount).isEqualTo(3);

            cachedValue.stale();
            assertThat(supplierCallCount).isEqualTo(4);

            // Getting value after stale doesn't trigger additional supplier calls
            String value = cachedValue.getValue();
            assertThat(value).isEqualTo("value_4");
            assertThat(supplierCallCount).isEqualTo(4);
        }

        @Test
        @DisplayName("Should handle stale operations with supplier exceptions")
        void testStaleWithSupplierException() {
            int[] callCount = { 0 };
            Supplier<String> conditionalSupplier = () -> {
                callCount[0]++;
                if (callCount[0] > 1) {
                    throw new RuntimeException("Error on refresh");
                }
                return "success";
            };

            CachedValue<String> conditionalCached = new CachedValue<>(conditionalSupplier);
            assertThat(conditionalCached.getValue()).isEqualTo("success");

            assertThatThrownBy(() -> conditionalCached.stale())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Error on refresh");
        }
    }

    @Nested
    @DisplayName("Concurrent Usage")
    class ConcurrentUsageTests {

        @Test
        @DisplayName("Should handle concurrent getValue calls safely")
        void testConcurrentGetValue() throws InterruptedException {
            final int NUM_THREADS = 10;
            final int CALLS_PER_THREAD = 100;
            Thread[] threads = new Thread[NUM_THREADS];
            String[] results = new String[NUM_THREADS];

            for (int i = 0; i < NUM_THREADS; i++) {
                final int threadIndex = i;
                threads[i] = new Thread(() -> {
                    for (int j = 0; j < CALLS_PER_THREAD; j++) {
                        results[threadIndex] = cachedValue.getValue();
                    }
                });
            }

            // Start all threads
            for (Thread thread : threads) {
                thread.start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // All threads should get the same cached value
            for (String result : results) {
                assertThat(result).isEqualTo("value_1");
            }

            // Supplier should only be called once (in constructor)
            assertThat(supplierCallCount).isEqualTo(1);
        }

        @Test
        @DisplayName("Should handle concurrent stale operations")
        void testConcurrentStaleOperations() throws InterruptedException {
            final int NUM_THREADS = 5;
            Thread[] threads = new Thread[NUM_THREADS];

            for (int i = 0; i < NUM_THREADS; i++) {
                threads[i] = new Thread(() -> {
                    cachedValue.stale();
                });
            }

            // Start all threads
            for (Thread thread : threads) {
                thread.start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Each stale call triggers supplier, plus initial constructor call
            assertThat(supplierCallCount).isEqualTo(1 + NUM_THREADS);
        }
    }

    @Nested
    @DisplayName("Memory and Performance")
    class MemoryPerformanceTests {

        @Test
        @DisplayName("Should minimize supplier calls with repeated access")
        void testMinimalSupplierCalls() {
            // Access value many times
            for (int i = 0; i < 1000; i++) {
                cachedValue.getValue();
            }

            // Supplier should only be called once (in constructor)
            assertThat(supplierCallCount).isEqualTo(1);
        }

        @RetryingTest(5)
        @DisplayName("Should handle expensive supplier operations efficiently")
        void testExpensiveSupplierCaching() {
            int[] expensiveCallCount = { 0 };
            Supplier<String> expensiveSupplier = () -> {
                expensiveCallCount[0]++;
                // Simulate expensive operation
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "expensive_result_" + expensiveCallCount[0];
            };

            CachedValue<String> expensiveCached = new CachedValue<>(expensiveSupplier);

            long startTime = System.currentTimeMillis();

            // Multiple accesses should be fast (cached)
            for (int i = 0; i < 10; i++) {
                String result = expensiveCached.getValue();
                assertThat(result).isEqualTo("expensive_result_1");
            }

            long endTime = System.currentTimeMillis();

            // Should complete quickly due to caching
            assertThat(endTime - startTime).isLessThan(100);
            assertThat(expensiveCallCount[0]).isEqualTo(1);
        }

        @Test
        @DisplayName("Should work with complex object types")
        void testComplexObjectCaching() {
            class ComplexObject {
                private final String data;
                private final int number;

                ComplexObject(String data, int number) {
                    this.data = data;
                    this.number = number;
                }

                @Override
                public boolean equals(Object obj) {
                    if (this == obj)
                        return true;
                    if (!(obj instanceof ComplexObject))
                        return false;
                    ComplexObject other = (ComplexObject) obj;
                    return number == other.number && data.equals(other.data);
                }

                @Override
                public int hashCode() {
                    return data.hashCode() * 31 + number;
                }
            }

            int[] creationCount = { 0 };
            Supplier<ComplexObject> complexSupplier = () -> {
                creationCount[0]++;
                return new ComplexObject("data", creationCount[0]);
            };

            CachedValue<ComplexObject> complexCached = new CachedValue<>(complexSupplier);

            ComplexObject first = complexCached.getValue();
            ComplexObject second = complexCached.getValue();

            assertThat(first).isSameAs(second); // Same reference (cached)
            assertThat(creationCount[0]).isEqualTo(1);
        }
    }
}
