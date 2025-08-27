package pt.up.fe.specs.util.lazy;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import pt.up.fe.specs.util.function.SerializableSupplier;

/**
 * 
 * @author Generated Tests
 */
@DisplayName("Lazy Interface Tests")
class LazyTest {

    private Supplier<String> mockSupplier;
    private SerializableSupplier<String> mockSerializableSupplier;
    private Lazy<String> lazy;

    @BeforeEach
    void setUp() {
        mockSupplier = mock();
        mockSerializableSupplier = mock();
        when(mockSupplier.get()).thenReturn("computed");
        when(mockSerializableSupplier.get()).thenReturn("serializable-computed");
    }

    @Nested
    @DisplayName("Factory Methods")
    class FactoryMethods {

        @Test
        @DisplayName("Should create instance with regular supplier")
        void testNewInstance() {
            lazy = Lazy.newInstance(mockSupplier);

            assertThat(lazy).isNotNull();
            assertThat(lazy).isInstanceOf(ThreadSafeLazy.class);
            assertThat(lazy.isInitialized()).isFalse();
        }

        @Test
        @DisplayName("Should create instance with serializable supplier")
        void testNewInstanceSerializable() {
            lazy = Lazy.newInstanceSerializable(mockSerializableSupplier);

            assertThat(lazy).isNotNull();
            assertThat(lazy).isInstanceOf(ThreadSafeLazy.class);
            assertThat(lazy.isInitialized()).isFalse();
        }

        @Test
        @DisplayName("Should throw exception for null supplier - BUG: No validation implemented")
        void testNullSupplier() {
            // BUG: Factory method doesn't validate null supplier
            assertThatCode(() -> Lazy.newInstance(null))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should throw exception for null serializable supplier - BUG: No validation implemented")
        void testNullSerializableSupplier() {
            // BUG: Factory method doesn't validate null serializable supplier
            assertThatCode(() -> Lazy.newInstanceSerializable(null))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Basic Operations")
    class BasicOperations {

        @BeforeEach
        void setUp() {
            lazy = Lazy.newInstance(mockSupplier);
        }

        @Test
        @DisplayName("Should be uninitialized initially")
        void testInitialState() {
            assertThat(lazy.isInitialized()).isFalse();
            verify(mockSupplier, never()).get();
        }

        @Test
        @DisplayName("Should compute value on first get")
        void testFirstGet() {
            String result = lazy.get();

            assertThat(result).isEqualTo("computed");
            assertThat(lazy.isInitialized()).isTrue();
            verify(mockSupplier, times(1)).get();
        }

        @Test
        @DisplayName("Should not recompute value on subsequent gets")
        void testSubsequentGets() {
            String result1 = lazy.get();
            String result2 = lazy.get();
            String result3 = lazy.get();

            assertThat(result1).isEqualTo("computed");
            assertThat(result2).isEqualTo("computed");
            assertThat(result3).isEqualTo("computed");
            assertThat(lazy.isInitialized()).isTrue();
            verify(mockSupplier, times(1)).get();
        }

        @Test
        @DisplayName("Should work as Supplier")
        void testAsSupplier() {
            Supplier<String> supplier = lazy;

            String result = supplier.get();

            assertThat(result).isEqualTo("computed");
            assertThat(lazy.isInitialized()).isTrue();
        }
    }

    @Nested
    @DisplayName("Value Types")
    class ValueTypes {

        @Test
        @DisplayName("Should handle null values")
        void testNullValue() {
            Lazy<String> nullLazy = Lazy.newInstance(() -> null);

            assertThat(nullLazy.get()).isNull();
            assertThat(nullLazy.isInitialized()).isTrue();
        }

        @Test
        @DisplayName("Should handle primitive wrapper types")
        void testPrimitiveWrappers() {
            Lazy<Integer> intLazy = Lazy.newInstance(() -> 42);
            Lazy<Boolean> boolLazy = Lazy.newInstance(() -> true);
            Lazy<Double> doubleLazy = Lazy.newInstance(() -> 3.14);

            assertThat(intLazy.get()).isEqualTo(42);
            assertThat(boolLazy.get()).isTrue();
            assertThat(doubleLazy.get()).isEqualTo(3.14);
        }

        @Test
        @DisplayName("Should handle complex objects")
        void testComplexObjects() {
            Lazy<StringBuilder> builderLazy = Lazy.newInstance(() -> new StringBuilder("test"));

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
            Lazy<String> errorLazy = Lazy.newInstance(() -> {
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
            Lazy<String> retryLazy = Lazy.newInstance(() -> {
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
            CountDownLatch doneLatch = new CountDownLatch(10);

            Lazy<String> threadSafeLazy = Lazy.newInstance(() -> {
                computationCount.incrementAndGet();
                try {
                    Thread.sleep(100); // Simulate expensive computation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "computed";
            });

            ExecutorService executor = Executors.newFixedThreadPool(10);
            @SuppressWarnings("unchecked")
            Future<String>[] futures = new Future[10];

            // Start all threads simultaneously
            for (int i = 0; i < 10; i++) {
                futures[i] = executor.submit(() -> {
                    try {
                        startLatch.await();
                        String result = threadSafeLazy.get();
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
            assertThat(threadSafeLazy.isInitialized()).isTrue();

            executor.shutdown();
        }

        @Test
        @DisplayName("Should handle concurrent exceptions correctly")
        @Timeout(5)
        void testConcurrentExceptions() throws Exception {
            AtomicInteger attempts = new AtomicInteger(0);
            Lazy<String> exceptionLazy = Lazy.newInstance(() -> {
                int attempt = attempts.incrementAndGet();
                throw new RuntimeException("Attempt " + attempt);
            });

            ExecutorService executor = Executors.newFixedThreadPool(5);
            Future<?>[] futures = new Future[5];

            for (int i = 0; i < 5; i++) {
                futures[i] = executor.submit(() -> {
                    assertThatThrownBy(exceptionLazy::get)
                            .isInstanceOf(RuntimeException.class)
                            .hasMessageStartingWith("Attempt");
                });
            }

            for (Future<?> future : futures) {
                future.get(); // Wait for completion
            }

            // Should remain uninitialized
            assertThat(exceptionLazy.isInitialized()).isFalse();
            // Multiple attempts should have been made
            assertThat(attempts.get()).isGreaterThan(1);

            executor.shutdown();
        }
    }

    @Nested
    @DisplayName("Performance Characteristics")
    class Performance {

        @Test
        @DisplayName("Should avoid expensive recomputation")
        void testExpensiveComputation() {
            AtomicInteger computationCount = new AtomicInteger(0);
            long startTime = System.nanoTime();

            Lazy<String> expensiveLazy = Lazy.newInstance(() -> {
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
            String result2 = expensiveLazy.get();
            String result3 = expensiveLazy.get();
            long subsequentTime = System.nanoTime() - secondStart;

            assertThat(result1).isEqualTo("expensive-result");
            assertThat(result2).isEqualTo("expensive-result");
            assertThat(result3).isEqualTo("expensive-result");
            assertThat(computationCount.get()).isEqualTo(1);

            // Subsequent calls should be much faster
            assertThat(subsequentTime).isLessThan(firstCallTime / 10);
        }

        @Test
        @DisplayName("Should have minimal memory overhead when uninitialized")
        void testMemoryFootprint() {
            Lazy<String> uninitialized = Lazy.newInstance(() -> "value");

            // Should not have computed the value yet
            assertThat(uninitialized.isInitialized()).isFalse();

            // Getting the value should initialize it
            uninitialized.get();
            assertThat(uninitialized.isInitialized()).isTrue();
        }
    }

    @Nested
    @DisplayName("Integration Scenarios")
    class Integration {

        @Test
        @DisplayName("Should work with chained lazy computations")
        void testChainedLazy() {
            Lazy<String> baseLazy = Lazy.newInstance(() -> "base");
            Lazy<String> derivedLazy = Lazy.newInstance(() -> baseLazy.get() + "-derived");
            Lazy<String> finalLazy = Lazy.newInstance(() -> derivedLazy.get() + "-final");

            assertThat(finalLazy.get()).isEqualTo("base-derived-final");
            assertThat(baseLazy.isInitialized()).isTrue();
            assertThat(derivedLazy.isInitialized()).isTrue();
            assertThat(finalLazy.isInitialized()).isTrue();
        }

        @Test
        @DisplayName("Should work as method parameter")
        void testAsMethodParameter() {
            Lazy<String> lazy = Lazy.newInstance(() -> "parameter-value");

            String result = processLazy(lazy);
            assertThat(result).isEqualTo("processed: parameter-value");
        }

        private String processLazy(Supplier<String> supplier) {
            return "processed: " + supplier.get();
        }

        @Test
        @DisplayName("Should work in collections")
        void testInCollections() {
            java.util.List<Lazy<String>> lazyList = java.util.Arrays.asList(
                    Lazy.newInstance(() -> "first"),
                    Lazy.newInstance(() -> "second"),
                    Lazy.newInstance(() -> "third"));

            // None should be initialized initially
            assertThat(lazyList).allMatch(lazy -> !lazy.isInitialized());

            // Process them
            java.util.List<String> results = lazyList.stream()
                    .map(Lazy::get)
                    .collect(java.util.stream.Collectors.toList());

            assertThat(results).containsExactly("first", "second", "third");
            assertThat(lazyList).allMatch(Lazy::isInitialized);
        }
    }
}
