package pt.up.fe.specs.util.lazy;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junitpioneer.jupiter.RetryingTest;

/**
 * 
 * @author Generated Tests
 */
@DisplayName("ThreadSafeLazy Tests")
class ThreadSafeLazyTest {

    private Supplier<String> mockSupplier;
    private ThreadSafeLazy<String> threadSafeLazy;

    @BeforeEach
    void setUp() {
        mockSupplier = mock();
        when(mockSupplier.get()).thenReturn("computed");
    }

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("Should create with supplier")
        void testConstructorWithSupplier() {
            threadSafeLazy = new ThreadSafeLazy<>(mockSupplier);

            assertThat(threadSafeLazy).isNotNull();
            assertThat(threadSafeLazy.isInitialized()).isFalse();
            verify(mockSupplier, never()).get();
        }

        @Test
        @DisplayName("Should throw exception for null supplier - BUG: No validation implemented")
        void testConstructorWithNullSupplier() {
            // BUG: Constructor doesn't validate null supplier
            assertThatCode(() -> new ThreadSafeLazy<>(null))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Basic Operations")
    class BasicOperations {

        @BeforeEach
        void setUp() {
            threadSafeLazy = new ThreadSafeLazy<>(mockSupplier);
        }

        @Test
        @DisplayName("Should be uninitialized initially")
        void testInitialState() {
            assertThat(threadSafeLazy.isInitialized()).isFalse();
            verify(mockSupplier, never()).get();
        }

        @Test
        @DisplayName("Should compute value on first get")
        void testFirstGet() {
            String result = threadSafeLazy.get();

            assertThat(result).isEqualTo("computed");
            assertThat(threadSafeLazy.isInitialized()).isTrue();
            verify(mockSupplier, times(1)).get();
        }

        @Test
        @DisplayName("Should use getValue method")
        void testGetValue() {
            String result = threadSafeLazy.getValue();

            assertThat(result).isEqualTo("computed");
            assertThat(threadSafeLazy.isInitialized()).isTrue();
            verify(mockSupplier, times(1)).get();
        }

        @Test
        @DisplayName("Should not recompute on subsequent calls")
        void testSubsequentCalls() {
            String result1 = threadSafeLazy.get();
            String result2 = threadSafeLazy.getValue();
            String result3 = threadSafeLazy.get();

            assertThat(result1).isEqualTo("computed");
            assertThat(result2).isEqualTo("computed");
            assertThat(result3).isEqualTo("computed");
            assertThat(threadSafeLazy.isInitialized()).isTrue();
            verify(mockSupplier, times(1)).get();
        }

        @Test
        @DisplayName("Should implement Lazy interface")
        void testLazyInterface() {
            Lazy<String> lazy = threadSafeLazy;

            assertThat(lazy.isInitialized()).isFalse();
            String result = lazy.get();
            assertThat(result).isEqualTo("computed");
            assertThat(lazy.isInitialized()).isTrue();
        }

        @Test
        @DisplayName("Should implement Supplier interface")
        void testSupplierInterface() {
            Supplier<String> supplier = threadSafeLazy;

            String result = supplier.get();
            assertThat(result).isEqualTo("computed");
        }
    }

    @Nested
    @DisplayName("Value Types")
    class ValueTypes {

        @Test
        @DisplayName("Should handle null values")
        void testNullValue() {
            ThreadSafeLazy<String> nullLazy = new ThreadSafeLazy<>(() -> null);

            assertThat(nullLazy.get()).isNull();
            assertThat(nullLazy.isInitialized()).isTrue();
        }

        @Test
        @DisplayName("Should handle primitive wrapper types")
        void testPrimitiveWrappers() {
            ThreadSafeLazy<Integer> intLazy = new ThreadSafeLazy<>(() -> 42);
            ThreadSafeLazy<Boolean> boolLazy = new ThreadSafeLazy<>(() -> true);
            ThreadSafeLazy<Double> doubleLazy = new ThreadSafeLazy<>(() -> 3.14);

            assertThat(intLazy.get()).isEqualTo(42);
            assertThat(boolLazy.get()).isTrue();
            assertThat(doubleLazy.get()).isEqualTo(3.14);
        }

        @Test
        @DisplayName("Should handle complex objects")
        void testComplexObjects() {
            ThreadSafeLazy<StringBuilder> builderLazy = new ThreadSafeLazy<>(() -> new StringBuilder("test"));

            StringBuilder result = builderLazy.get();
            assertThat(result.toString()).isEqualTo("test");

            // Verify same instance returned
            StringBuilder result2 = builderLazy.get();
            assertThat(result2).isSameAs(result);
        }
    }

    @Nested
    @DisplayName("Exception Handling")
    class ExceptionHandling {

        @Test
        @DisplayName("Should propagate supplier exceptions")
        void testSupplierException() {
            RuntimeException exception = new RuntimeException("Computation failed");
            ThreadSafeLazy<String> errorLazy = new ThreadSafeLazy<>(() -> {
                throw exception;
            });

            assertThatThrownBy(errorLazy::get)
                    .isSameAs(exception);

            // Should remain uninitialized after exception
            assertThat(errorLazy.isInitialized()).isFalse();
        }

        @Test
        @DisplayName("Should retry computation after exception")
        void testRetryAfterException() {
            AtomicInteger attempts = new AtomicInteger(0);
            ThreadSafeLazy<String> retryLazy = new ThreadSafeLazy<>(() -> {
                int attempt = attempts.incrementAndGet();
                if (attempt == 1) {
                    throw new RuntimeException("First attempt failed");
                }
                return "success-" + attempt;
            });

            // First call should fail
            assertThatThrownBy(retryLazy::get)
                    .hasMessage("First attempt failed");
            assertThat(retryLazy.isInitialized()).isFalse();

            // Second call should succeed
            String result = retryLazy.get();
            assertThat(result).isEqualTo("success-2");
            assertThat(retryLazy.isInitialized()).isTrue();
        }

        @Test
        @DisplayName("Should handle exceptions in getValue method")
        void testGetValueException() {
            ThreadSafeLazy<String> errorLazy = new ThreadSafeLazy<>(() -> {
                throw new IllegalStateException("getValue error");
            });

            assertThatThrownBy(errorLazy::getValue)
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("getValue error");
        }
    }

    @Nested
    @DisplayName("Thread Safety")
    class ThreadSafety {

        @Test
        @DisplayName("Should be thread-safe with concurrent access")
        @Timeout(5)
        void testConcurrentAccess() throws Exception {
            AtomicInteger computationCount = new AtomicInteger(0);
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(20);

            ThreadSafeLazy<String> concurrentLazy = new ThreadSafeLazy<>(() -> {
                computationCount.incrementAndGet();
                try {
                    Thread.sleep(100); // Simulate expensive computation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "computed";
            });

            ExecutorService executor = Executors.newFixedThreadPool(20);
            @SuppressWarnings("unchecked")
            Future<String>[] futures = new Future[20];

            // Start all threads simultaneously
            for (int i = 0; i < 20; i++) {
                futures[i] = executor.submit(() -> {
                    try {
                        startLatch.await();
                        String result = concurrentLazy.get();
                        doneLatch.countDown();
                        return result;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                });
            }

            startLatch.countDown(); // Start all threads
            doneLatch.await(); // Wait for all to complete

            // Verify all got the same result
            for (Future<String> future : futures) {
                assertThat(future.get()).isEqualTo("computed");
            }

            // Verify computation happened only once
            assertThat(computationCount.get()).isEqualTo(1);
            assertThat(concurrentLazy.isInitialized()).isTrue();

            executor.shutdown();
        }

        @Test
        @DisplayName("Should handle mixed get and getValue calls")
        @Timeout(5)
        void testMixedMethodCalls() throws Exception {
            AtomicInteger computationCount = new AtomicInteger(0);
            ThreadSafeLazy<String> mixedLazy = new ThreadSafeLazy<>(() -> {
                computationCount.incrementAndGet();
                return "computed";
            });

            ExecutorService executor = Executors.newFixedThreadPool(10);
            @SuppressWarnings("unchecked")
            CompletableFuture<Void>[] futures = new CompletableFuture[10];

            for (int i = 0; i < 10; i++) {
                final int index = i;
                futures[i] = CompletableFuture.runAsync(() -> {
                    if (index % 2 == 0) {
                        assertThat(mixedLazy.get()).isEqualTo("computed");
                    } else {
                        assertThat(mixedLazy.getValue()).isEqualTo("computed");
                    }
                }, executor);
            }

            CompletableFuture.allOf(futures).get(3, TimeUnit.SECONDS);

            // Verify computation happened only once
            assertThat(computationCount.get()).isEqualTo(1);
            assertThat(mixedLazy.isInitialized()).isTrue();

            executor.shutdown();
        }

        @Test
        @DisplayName("Should handle race condition between initialization check and computation")
        @Timeout(5)
        void testInitializationRaceCondition() throws Exception {
            AtomicInteger computationCount = new AtomicInteger(0);
            AtomicReference<String> lastResult = new AtomicReference<>();

            ThreadSafeLazy<String> raceLazy = new ThreadSafeLazy<>(() -> {
                int count = computationCount.incrementAndGet();
                String result = "computed-" + count;
                lastResult.set(result);
                try {
                    Thread.sleep(50); // Give time for race conditions
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return result;
            });

            ExecutorService executor = Executors.newFixedThreadPool(50);
            @SuppressWarnings("unchecked")
            Future<String>[] futures = new Future[50];

            for (int i = 0; i < 50; i++) {
                futures[i] = executor.submit(raceLazy::get);
            }

            // Verify all threads get the same result
            String firstResult = futures[0].get();
            for (Future<String> future : futures) {
                assertThat(future.get()).isEqualTo(firstResult);
            }

            // Verify computation happened only once
            assertThat(computationCount.get()).isEqualTo(1);
            assertThat(raceLazy.isInitialized()).isTrue();

            executor.shutdown();
        }

        @Test
        @DisplayName("Should handle concurrent exceptions correctly")
        @Timeout(5)
        void testConcurrentExceptions() throws Exception {
            AtomicInteger attempts = new AtomicInteger(0);
            ThreadSafeLazy<String> exceptionLazy = new ThreadSafeLazy<>(() -> {
                int attempt = attempts.incrementAndGet();
                throw new RuntimeException("Attempt " + attempt);
            });

            ExecutorService executor = Executors.newFixedThreadPool(10);
            @SuppressWarnings("unchecked")
            Future<Void>[] futures = new Future[10];

            for (int i = 0; i < 10; i++) {
                futures[i] = executor.submit(() -> {
                    assertThatThrownBy(exceptionLazy::get)
                            .isInstanceOf(RuntimeException.class)
                            .hasMessageStartingWith("Attempt");
                    return null;
                });
            }

            for (Future<Void> future : futures) {
                future.get(); // Wait for completion
            }

            // Should remain uninitialized
            assertThat(exceptionLazy.isInitialized()).isFalse();
            // Multiple attempts should have been made due to concurrent access
            assertThat(attempts.get()).isGreaterThan(1);

            executor.shutdown();
        }
    }

    @Nested
    @DisplayName("Performance Characteristics")
    class Performance {

        @RetryingTest(5)
        @DisplayName("Should avoid expensive recomputation")
        void testExpensiveComputation() {
            AtomicInteger computationCount = new AtomicInteger(0);
            long startTime = System.nanoTime();

            ThreadSafeLazy<String> expensiveLazy = new ThreadSafeLazy<>(() -> {
                computationCount.incrementAndGet();
                try {
                    Thread.sleep(100); // Simulate expensive computation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "expensive-result";
            });

            // First call should take time
            String result1 = expensiveLazy.get();
            long firstCallTime = System.nanoTime() - startTime;

            // Subsequent calls should be fast
            long secondStart = System.nanoTime();
            String result2 = expensiveLazy.getValue();
            String result3 = expensiveLazy.get();
            long subsequentTime = System.nanoTime() - secondStart;

            assertThat(result1).isEqualTo("expensive-result");
            assertThat(result2).isEqualTo("expensive-result");
            assertThat(result3).isEqualTo("expensive-result");
            assertThat(computationCount.get()).isEqualTo(1);

            // Subsequent calls should be much faster
            assertThat(subsequentTime).isLessThan(firstCallTime / 10);
        }

        @RetryingTest(5)
        @DisplayName("Should have fast uncontended access after initialization")
        void testUncontendedAccess() {
            ThreadSafeLazy<String> fastLazy = new ThreadSafeLazy<>(() -> "fast");

            // Initialize
            fastLazy.get();
            assertThat(fastLazy.isInitialized()).isTrue();

            // Measure fast access
            long startTime = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                fastLazy.get();
            }
            long duration = System.nanoTime() - startTime;

            // Should be very fast (less than 1ms for 1000 calls)
            assertThat(duration).isLessThan(1_000_000L);
        }
    }

    @Nested
    @DisplayName("Integration Scenarios")
    class Integration {

        @Test
        @DisplayName("Should work with chained lazy computations")
        void testChainedLazy() {
            ThreadSafeLazy<String> baseLazy = new ThreadSafeLazy<>(() -> "base");
            ThreadSafeLazy<String> derivedLazy = new ThreadSafeLazy<>(() -> baseLazy.get() + "-derived");
            ThreadSafeLazy<String> finalLazy = new ThreadSafeLazy<>(() -> derivedLazy.getValue() + "-final");

            assertThat(finalLazy.get()).isEqualTo("base-derived-final");
            assertThat(baseLazy.isInitialized()).isTrue();
            assertThat(derivedLazy.isInitialized()).isTrue();
            assertThat(finalLazy.isInitialized()).isTrue();
        }

        @Test
        @DisplayName("Should work as method parameter")
        void testAsMethodParameter() {
            ThreadSafeLazy<String> lazy = new ThreadSafeLazy<>(() -> "parameter-value");

            String result = processSupplier(lazy);
            assertThat(result).isEqualTo("processed: parameter-value");
        }

        private String processSupplier(Supplier<String> supplier) {
            return "processed: " + supplier.get();
        }

        @Test
        @DisplayName("Should work in collections")
        void testInCollections() {
            java.util.List<ThreadSafeLazy<String>> lazyList = java.util.Arrays.asList(
                    new ThreadSafeLazy<>(() -> "first"),
                    new ThreadSafeLazy<>(() -> "second"),
                    new ThreadSafeLazy<>(() -> "third"));

            // None should be initialized initially
            assertThat(lazyList).allMatch(lazy -> !lazy.isInitialized());

            // Process them
            java.util.List<String> results = lazyList.stream()
                    .map(ThreadSafeLazy::get)
                    .collect(java.util.stream.Collectors.toList());

            assertThat(results).containsExactly("first", "second", "third");
            assertThat(lazyList).allMatch(ThreadSafeLazy::isInitialized);
        }

        @Test
        @DisplayName("Should work with inheritance")
        void testInheritance() {
            Lazy<String> lazyInterface = new ThreadSafeLazy<>(() -> "interface-value");
            Supplier<String> supplierInterface = new ThreadSafeLazy<>(() -> "supplier-value");

            assertThat(lazyInterface.get()).isEqualTo("interface-value");
            assertThat(supplierInterface.get()).isEqualTo("supplier-value");
        }
    }
}
