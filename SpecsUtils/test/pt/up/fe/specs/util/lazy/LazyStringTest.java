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
import org.junitpioneer.jupiter.RetryingTest;

/**
 * 
 * @author Generated Tests
 */
@DisplayName("LazyString Tests")
class LazyStringTest {

    private Supplier<String> mockSupplier;
    private LazyString lazyString;

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
            lazyString = new LazyString(mockSupplier);

            assertThat(lazyString).isNotNull();
            verify(mockSupplier, never()).get();
        }

        @Test
        @DisplayName("Should throw exception for null supplier")
        void testConstructorWithNullSupplier() {
            assertThatThrownBy(() -> new LazyString(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Supplier cannot be null");
        }
    }

    @Nested
    @DisplayName("Basic Operations")
    class BasicOperations {

        @BeforeEach
        void setUp() {
            lazyString = new LazyString(mockSupplier);
        }

        @Test
        @DisplayName("Should compute string on first toString")
        void testFirstToString() {
            String result = lazyString.toString();

            assertThat(result).isEqualTo("computed");
            verify(mockSupplier, times(1)).get();
        }

        @Test
        @DisplayName("Should not recompute on subsequent toString calls")
        void testSubsequentToString() {
            String result1 = lazyString.toString();
            String result2 = lazyString.toString();
            String result3 = lazyString.toString();

            assertThat(result1).isEqualTo("computed");
            assertThat(result2).isEqualTo("computed");
            assertThat(result3).isEqualTo("computed");
            verify(mockSupplier, times(1)).get();
        }

        @Test
        @DisplayName("Should work with string concatenation")
        void testStringConcatenation() {
            String result = "prefix-" + lazyString + "-suffix";

            assertThat(result).isEqualTo("prefix-computed-suffix");
            verify(mockSupplier, times(1)).get();
        }

        @Test
        @DisplayName("Should work in StringBuilder")
        void testStringBuilder() {
            StringBuilder sb = new StringBuilder();
            sb.append("Start: ").append(lazyString).append(" :End");

            assertThat(sb.toString()).isEqualTo("Start: computed :End");
            verify(mockSupplier, times(1)).get();
        }
    }

    @Nested
    @DisplayName("Value Types")
    class ValueTypes {

        @Test
        @DisplayName("Should handle null values")
        void testNullValue() {
            LazyString nullLazy = new LazyString(() -> null);

            assertThat(nullLazy.toString()).isEqualTo("null");
        }

        @Test
        @DisplayName("Should handle empty strings")
        void testEmptyString() {
            LazyString emptyLazy = new LazyString(() -> "");

            assertThat(emptyLazy.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle multiline strings")
        void testMultilineString() {
            String multiline = "Line 1\nLine 2\nLine 3";
            LazyString multilineLazy = new LazyString(() -> multiline);

            assertThat(multilineLazy.toString()).isEqualTo(multiline);
        }

        @Test
        @DisplayName("Should handle special characters")
        void testSpecialCharacters() {
            String special = "!@#$%^&*()_+-=[]{}|;':\",./<>?";
            LazyString specialLazy = new LazyString(() -> special);

            assertThat(specialLazy.toString()).isEqualTo(special);
        }

        @Test
        @DisplayName("Should handle Unicode characters")
        void testUnicodeCharacters() {
            String unicode = "Hello 世界 🌍 🚀";
            LazyString unicodeLazy = new LazyString(() -> unicode);

            assertThat(unicodeLazy.toString()).isEqualTo(unicode);
        }
    }

    @Nested
    @DisplayName("Exception Handling")
    class ExceptionHandling {

        @Test
        @DisplayName("Should propagate supplier exceptions")
        void testSupplierException() {
            RuntimeException exception = new RuntimeException("String computation failed");
            LazyString errorLazy = new LazyString(() -> {
                throw exception;
            });

            assertThatThrownBy(errorLazy::toString)
                    .isSameAs(exception);
        }

        @Test
        @DisplayName("Should retry computation after exception")
        void testRetryAfterException() {
            AtomicInteger attempts = new AtomicInteger(0);
            LazyString retryLazy = new LazyString(() -> {
                int attempt = attempts.incrementAndGet();
                if (attempt == 1) {
                    throw new RuntimeException("First attempt failed");
                }
                return "success-" + attempt;
            });

            // First call should fail
            assertThatThrownBy(retryLazy::toString)
                    .hasMessage("First attempt failed");

            // Second call should succeed
            String result = retryLazy.toString();
            assertThat(result).isEqualTo("success-2");
        }

        @Test
        @DisplayName("Should handle exceptions in string concatenation")
        void testExceptionInConcatenation() {
            LazyString errorLazy = new LazyString(() -> {
                throw new IllegalStateException("Concatenation error");
            });

            assertThatThrownBy(() -> {
                @SuppressWarnings("unused")
                String result = "prefix-" + errorLazy;
            })
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Concatenation error");
        }
    }

    @Nested
    @DisplayName("Thread Safety")
    class ThreadSafety {

        @Test
        @DisplayName("Should be thread-safe with concurrent toString calls")
        @Timeout(5)
        void testConcurrentToString() throws Exception {
            AtomicInteger computationCount = new AtomicInteger(0);
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(15);

            LazyString concurrentLazy = new LazyString(() -> {
                computationCount.incrementAndGet();
                try {
                    Thread.sleep(100); // Simulate expensive computation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "computed";
            });

            ExecutorService executor = Executors.newFixedThreadPool(15);
            @SuppressWarnings("unchecked")
            Future<String>[] futures = new Future[15];

            // Start all threads simultaneously
            for (int i = 0; i < 15; i++) {
                futures[i] = executor.submit(() -> {
                    try {
                        startLatch.await();
                        String result = concurrentLazy.toString();
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

            executor.shutdown();
        }

        @Test
        @DisplayName("Should handle concurrent string operations")
        @Timeout(5)
        void testConcurrentStringOperations() throws Exception {
            AtomicInteger computationCount = new AtomicInteger(0);
            LazyString concurrentLazy = new LazyString(() -> {
                computationCount.incrementAndGet();
                return "value";
            });

            ExecutorService executor = Executors.newFixedThreadPool(10);
            @SuppressWarnings("unchecked")
            Future<String>[] futures = new Future[10];

            for (int i = 0; i < 10; i++) {
                final int index = i;
                futures[i] = executor.submit(() -> {
                    switch (index % 3) {
                        case 0:
                            return concurrentLazy.toString();
                        case 1:
                            return "prefix-" + concurrentLazy;
                        case 2:
                            return new StringBuilder().append(concurrentLazy).toString();
                        default:
                            return concurrentLazy.toString();
                    }
                });
            }

            // Verify all operations succeeded and got consistent results
            for (Future<String> future : futures) {
                String result = future.get();
                assertThat(result).matches("(value|prefix-value)");
            }

            // Verify computation happened only once
            assertThat(computationCount.get()).isEqualTo(1);

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

            LazyString expensiveLazy = new LazyString(() -> {
                computationCount.incrementAndGet();
                try {
                    Thread.sleep(100); // Simulate expensive computation
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return "expensive-result";
            });

            // First call should take time
            String result1 = expensiveLazy.toString();
            long firstCallTime = System.nanoTime() - startTime;

            // Subsequent calls should be fast
            long secondStart = System.nanoTime();
            String result2 = expensiveLazy.toString();
            String result3 = "prefix-" + expensiveLazy + "-suffix";
            long subsequentTime = System.nanoTime() - secondStart;

            assertThat(result1).isEqualTo("expensive-result");
            assertThat(result2).isEqualTo("expensive-result");
            assertThat(result3).isEqualTo("prefix-expensive-result-suffix");
            assertThat(computationCount.get()).isEqualTo(1);

            // Subsequent calls should be much faster
            assertThat(subsequentTime).isLessThan(firstCallTime / 10);
        }

        @RetryingTest(5)
        @DisplayName("Should have fast string access after initialization")
        void testFastStringAccess() {
            LazyString fastLazy = new LazyString(() -> "fast");

            // Initialize
            fastLazy.toString();

            // Measure fast access
            long startTime = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                fastLazy.toString();
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
        @DisplayName("Should work with string formatting")
        void testStringFormatting() {
            LazyString lazy = new LazyString(() -> "World");

            String formatted = String.format("Hello %s!", lazy);
            assertThat(formatted).isEqualTo("Hello World!");
        }

        @Test
        @DisplayName("Should work with regex operations")
        void testRegexOperations() {
            LazyString lazy = new LazyString(() -> "123-456-789");

            String replaced = lazy.toString().replaceAll("-", ".");
            assertThat(replaced).isEqualTo("123.456.789");

            boolean matches = lazy.toString().matches("\\d{3}-\\d{3}-\\d{3}");
            assertThat(matches).isTrue();
        }

        @Test
        @DisplayName("Should work in collections")
        void testInCollections() {
            java.util.List<LazyString> lazyList = java.util.Arrays.asList(
                    new LazyString(() -> "first"),
                    new LazyString(() -> "second"),
                    new LazyString(() -> "third"));

            String joined = lazyList.stream()
                    .map(LazyString::toString)
                    .collect(java.util.stream.Collectors.joining(", "));

            assertThat(joined).isEqualTo("first, second, third");
        }

        @Test
        @DisplayName("Should work with chained lazy operations")
        void testChainedLazy() {
            LazyString baseLazy = new LazyString(() -> "base");
            LazyString derivedLazy = new LazyString(() -> baseLazy + "-derived");
            LazyString finalLazy = new LazyString(() -> derivedLazy + "-final");

            assertThat(finalLazy.toString()).isEqualTo("base-derived-final");
        }

        @Test
        @DisplayName("Should work with toString in various contexts")
        void testToStringContexts() {
            LazyString lazy = new LazyString(() -> "context-value");

            // Direct toString
            assertThat(lazy.toString()).isEqualTo("context-value");

            // In string concatenation
            assertThat("prefix-" + lazy).isEqualTo("prefix-context-value");

            // In StringBuilder
            StringBuilder sb = new StringBuilder();
            sb.append(lazy);
            assertThat(sb.toString()).isEqualTo("context-value");

            // In String.valueOf
            assertThat(String.valueOf(lazy)).isEqualTo("context-value");

            // In printf/format
            String formatted = String.format("Value: %s", lazy);
            assertThat(formatted).isEqualTo("Value: context-value");
        }

        @Test
        @DisplayName("Should handle complex string computations")
        void testComplexComputations() {
            LazyString complex = new LazyString(() -> {
                StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= 5; i++) {
                    sb.append("Item ").append(i);
                    if (i < 5)
                        sb.append(", ");
                }
                return sb.toString();
            });

            assertThat(complex.toString()).isEqualTo("Item 1, Item 2, Item 3, Item 4, Item 5");
        }
    }
}
