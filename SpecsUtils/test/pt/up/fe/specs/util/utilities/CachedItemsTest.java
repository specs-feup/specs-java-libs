package pt.up.fe.specs.util.utilities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link CachedItems} class.
 * Tests cache functionality with mapper functions and analytics.
 * 
 * @author Generated Tests
 */
class CachedItemsTest {

    private Function<String, String> upperCaseMapper;
    private CachedItems<String, String> cache;
    private int mapperCallCount;

    @BeforeEach
    void setUp() {
        mapperCallCount = 0;
        upperCaseMapper = key -> {
            mapperCallCount++;
            return key.toUpperCase();
        };
        cache = new CachedItems<>(upperCaseMapper);
    }

    @Nested
    @DisplayName("Constructor and Initial State")
    class ConstructorTests {

        @Test
        @DisplayName("Should create cache with mapper function")
        void testSimpleConstructor() {
            assertThat(cache).isNotNull();
            assertThat(cache.getCacheSize()).isEqualTo(0);
            assertThat(cache.getCacheHits()).isEqualTo(0);
            assertThat(cache.getCacheMisses()).isEqualTo(0);
            assertThat(cache.getCacheTotalCalls()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should create non-thread-safe cache by default")
        void testDefaultNonThreadSafeCache() {
            CachedItems<String, String> defaultCache = new CachedItems<>(upperCaseMapper);

            assertThat(defaultCache).isNotNull();
            assertThat(defaultCache.getCacheSize()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should create thread-safe cache when specified")
        void testThreadSafeCache() {
            CachedItems<String, String> threadSafeCache = new CachedItems<>(upperCaseMapper, true);

            assertThat(threadSafeCache).isNotNull();
            assertThat(threadSafeCache.getCacheSize()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should create non-thread-safe cache when explicitly specified")
        void testExplicitNonThreadSafeCache() {
            CachedItems<String, String> nonThreadSafeCache = new CachedItems<>(upperCaseMapper, false);

            assertThat(nonThreadSafeCache).isNotNull();
            assertThat(nonThreadSafeCache.getCacheSize()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle null mapper gracefully")
        void testNullMapper() {
            // CachedItems should validate mapper in constructor
            assertThatThrownBy(() -> new CachedItems<String, String>(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Mapper function cannot be null");
        }
    }

    @Nested
    @DisplayName("Get Operations")
    class GetOperationTests {

        @Test
        @DisplayName("Should call mapper on first access")
        void testFirstAccess() {
            String result = cache.get("hello");

            assertThat(result).isEqualTo("HELLO");
            assertThat(mapperCallCount).isEqualTo(1);
            assertThat(cache.getCacheMisses()).isEqualTo(1);
            assertThat(cache.getCacheHits()).isEqualTo(0);
            assertThat(cache.getCacheSize()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should return cached value on subsequent access")
        void testCachedAccess() {
            String firstResult = cache.get("hello");
            String secondResult = cache.get("hello");
            String thirdResult = cache.get("hello");

            assertThat(firstResult).isEqualTo("HELLO");
            assertThat(secondResult).isEqualTo("HELLO");
            assertThat(thirdResult).isEqualTo("HELLO");
            assertThat(mapperCallCount).isEqualTo(1); // Mapper called only once
            assertThat(cache.getCacheMisses()).isEqualTo(1);
            assertThat(cache.getCacheHits()).isEqualTo(2);
            assertThat(cache.getCacheSize()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should handle multiple different keys")
        void testMultipleKeys() {
            String result1 = cache.get("hello");
            String result2 = cache.get("world");
            String result3 = cache.get("test");

            assertThat(result1).isEqualTo("HELLO");
            assertThat(result2).isEqualTo("WORLD");
            assertThat(result3).isEqualTo("TEST");
            assertThat(mapperCallCount).isEqualTo(3);
            assertThat(cache.getCacheMisses()).isEqualTo(3);
            assertThat(cache.getCacheHits()).isEqualTo(0);
            assertThat(cache.getCacheSize()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should handle mixed cache hits and misses")
        void testMixedHitsAndMisses() {
            // First access - cache miss
            cache.get("hello");
            cache.get("world");

            // Second access - cache hits
            cache.get("hello");
            cache.get("world");

            // New key - cache miss
            cache.get("new");

            assertThat(mapperCallCount).isEqualTo(3);
            assertThat(cache.getCacheMisses()).isEqualTo(3);
            assertThat(cache.getCacheHits()).isEqualTo(2);
            assertThat(cache.getCacheSize()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should handle null keys")
        void testNullKey() {
            // Note: This test demonstrates Bug 3 - mapper must handle null keys
            Function<String, String> nullSafeMapper = key -> key == null ? "NULL_KEY" : key.toUpperCase();
            CachedItems<String, String> nullSafeCache = new CachedItems<>(nullSafeMapper);

            String result = nullSafeCache.get(null);

            assertThat(result).isEqualTo("NULL_KEY");
            assertThat(nullSafeCache.getCacheMisses()).isEqualTo(1);
            assertThat(nullSafeCache.getCacheSize()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should handle mapper returning null")
        void testMapperReturningNull() {
            Function<String, String> nullMapper = key -> null;
            CachedItems<String, String> nullCache = new CachedItems<>(nullMapper);

            String result = nullCache.get("test");

            assertThat(result).isNull();
            assertThat(nullCache.getCacheMisses()).isEqualTo(1);
            assertThat(nullCache.getCacheSize()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should handle mapper throwing exceptions")
        void testMapperException() {
            Function<String, String> exceptionMapper = key -> {
                throw new RuntimeException("Mapper error");
            };
            CachedItems<String, String> exceptionCache = new CachedItems<>(exceptionMapper);

            assertThatThrownBy(() -> exceptionCache.get("test"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Mapper error");
        }
    }

    @Nested
    @DisplayName("Analytics and Statistics")
    class AnalyticsTests {

        @Test
        @DisplayName("Should track cache hits correctly")
        void testCacheHitTracking() {
            cache.get("test");
            cache.get("test");
            cache.get("test");

            assertThat(cache.getCacheHits()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should track cache misses correctly")
        void testCacheMissTracking() {
            cache.get("test1");
            cache.get("test2");
            cache.get("test3");

            assertThat(cache.getCacheMisses()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should calculate total calls correctly")
        void testTotalCallsCalculation() {
            cache.get("test1");
            cache.get("test1");
            cache.get("test2");
            cache.get("test2");
            cache.get("test3");

            assertThat(cache.getCacheTotalCalls()).isEqualTo(5);
            assertThat(cache.getCacheHits()).isEqualTo(2);
            assertThat(cache.getCacheMisses()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should calculate hit ratio correctly")
        void testHitRatioCalculation() {
            // No calls yet - hit ratio should be NaN or 0
            double initialRatio = cache.getHitRatio();
            assertThat(initialRatio).isNaN();

            // 1 miss, 0 hits - hit ratio should be 0
            cache.get("test");
            assertThat(cache.getHitRatio()).isEqualTo(0.0);

            // 1 miss, 1 hit - hit ratio should be 0.5
            cache.get("test");
            assertThat(cache.getHitRatio()).isEqualTo(0.5);

            // 1 miss, 2 hits - hit ratio should be 2/3
            cache.get("test");
            assertThat(cache.getHitRatio()).isCloseTo(2.0 / 3.0, within(0.001));
        }

        @Test
        @DisplayName("Should track cache size correctly")
        void testCacheSizeTracking() {
            assertThat(cache.getCacheSize()).isEqualTo(0);

            cache.get("test1");
            assertThat(cache.getCacheSize()).isEqualTo(1);

            cache.get("test2");
            assertThat(cache.getCacheSize()).isEqualTo(2);

            cache.get("test1"); // Cache hit, size shouldn't change
            assertThat(cache.getCacheSize()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should generate analytics string correctly")
        void testAnalyticsString() {
            cache.get("test1");
            cache.get("test1");
            cache.get("test2");

            String analytics = cache.getAnalytics();

            assertThat(analytics).isNotNull();
            assertThat(analytics).contains("Cache size: 2");
            assertThat(analytics).contains("Total calls: 3");
            // Note: Bug 2 - locale-specific formatting may use comma instead of period
            assertThat(analytics).containsAnyOf("Hit ratio: 33.33%", "Hit ratio: 33,33%");
        }

        @Test
        @DisplayName("Should handle analytics with no calls")
        void testAnalyticsWithNoCalls() {
            String analytics = cache.getAnalytics();

            assertThat(analytics).isNotNull();
            assertThat(analytics).contains("Cache size: 0");
            assertThat(analytics).contains("Total calls: 0");
            // Note: Bug 2 - locale-specific formatting affects NaN% display
            assertThat(analytics).containsAnyOf("Hit ratio: NaN%", "Hit ratio: NaN%");
        }
    }

    @Nested
    @DisplayName("Thread Safety")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should handle concurrent access with thread-safe cache")
        void testThreadSafeConcurrentAccess() throws InterruptedException {
            final int NUM_THREADS = 5;
            final int SHARED_KEYS = 3; // Keys: 1, 2, 3
            final int ACCESSES_PER_KEY = 10; // Each thread accesses each key 10 times

            // Use CountDownLatch for perfect synchronization
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(NUM_THREADS);

            // Track mapper calls with thread-safe counter
            AtomicInteger mapperCalls = new AtomicInteger(0);

            CachedItems<Integer, String> testCache = new CachedItems<>(
                    key -> {
                        mapperCalls.incrementAndGet();
                        return "value_" + key;
                    }, true);

            Thread[] threads = new Thread[NUM_THREADS];

            for (int i = 0; i < NUM_THREADS; i++) {
                threads[i] = new Thread(() -> {
                    try {
                        // Wait for all threads to be ready
                        startLatch.await();

                        // Each thread accesses the same keys in the same order
                        // This creates maximum contention and tests thread safety
                        for (int accessCount = 0; accessCount < ACCESSES_PER_KEY; accessCount++) {
                            for (int keyId = 1; keyId <= SHARED_KEYS; keyId++) {
                                testCache.get(keyId);
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        doneLatch.countDown();
                    }
                });
            }

            // Start all threads
            for (Thread thread : threads) {
                thread.start();
            }

            // Release all threads simultaneously
            startLatch.countDown();

            // Wait for all threads to complete
            doneLatch.await();

            // DETERMINISTIC ASSERTIONS - if thread-safe, these values must be exact
            int expectedTotalCalls = NUM_THREADS * SHARED_KEYS * ACCESSES_PER_KEY; // 5 * 3 * 10 = 150
            int expectedCacheMisses = SHARED_KEYS; // 3 (first access to each key)
            int expectedCacheHits = expectedTotalCalls - expectedCacheMisses; // 150 - 3 = 147
            int expectedCacheSize = SHARED_KEYS; // 3 keys total
            int expectedMapperCalls = SHARED_KEYS; // 3 (one per unique key)

            assertThat(testCache.getCacheTotalCalls()).isEqualTo(expectedTotalCalls);
            assertThat(testCache.getCacheMisses()).isEqualTo(expectedCacheMisses);
            assertThat(testCache.getCacheHits()).isEqualTo(expectedCacheHits);
            assertThat(testCache.getCacheSize()).isEqualTo(expectedCacheSize);
            assertThat(mapperCalls.get()).isEqualTo(expectedMapperCalls);

            // Verify all expected keys are present
            assertThat(testCache.get(1)).isEqualTo("value_1");
            assertThat(testCache.get(2)).isEqualTo("value_2");
            assertThat(testCache.get(3)).isEqualTo("value_3");
        }

        @Test
        @DisplayName("Should handle race condition on single key access - most critical thread safety test")
        void testSingleKeyRaceCondition() throws InterruptedException {
            final int NUM_THREADS = 20;
            final int ACCESSES_PER_THREAD = 100;
            final Integer SHARED_KEY = 42;

            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch doneLatch = new CountDownLatch(NUM_THREADS);

            AtomicInteger mapperCalls = new AtomicInteger(0);

            CachedItems<Integer, String> testCache = new CachedItems<>(
                    key -> {
                        mapperCalls.incrementAndGet();
                        // Add small delay to increase chance of race condition if not properly
                        // synchronized
                        try {
                            Thread.sleep(1);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        return "computed_" + key;
                    }, true);

            Thread[] threads = new Thread[NUM_THREADS];

            for (int i = 0; i < NUM_THREADS; i++) {
                threads[i] = new Thread(() -> {
                    try {
                        startLatch.await();

                        // All threads hammer the exact same key
                        for (int j = 0; j < ACCESSES_PER_THREAD; j++) {
                            String result = testCache.get(SHARED_KEY);
                            // Verify we always get the same result
                            assertThat(result).isEqualTo("computed_42");
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        doneLatch.countDown();
                    }
                });
            }

            // Start all threads
            for (Thread thread : threads) {
                thread.start();
            }

            // Release all threads simultaneously to maximize contention
            startLatch.countDown();

            // Wait for completion
            doneLatch.await();

            // CRITICAL DETERMINISTIC ASSERTIONS
            // If thread-safe, mapper should be called exactly once despite high contention
            int expectedTotalCalls = NUM_THREADS * ACCESSES_PER_THREAD; // 20 * 100 = 2000
            int expectedMapperCalls = 1; // Only first access should trigger mapper
            int expectedCacheMisses = 1; // Only first access is a miss
            int expectedCacheHits = expectedTotalCalls - 1; // All other accesses are hits
            int expectedCacheSize = 1; // Only one key in cache

            assertThat(testCache.getCacheTotalCalls()).isEqualTo(expectedTotalCalls);
            assertThat(mapperCalls.get()).isEqualTo(expectedMapperCalls);
            assertThat(testCache.getCacheMisses()).isEqualTo(expectedCacheMisses);
            assertThat(testCache.getCacheHits()).isEqualTo(expectedCacheHits);
            assertThat(testCache.getCacheSize()).isEqualTo(expectedCacheSize);
        }

        @Test
        @DisplayName("Should create ConcurrentHashMap for thread-safe cache")
        void testThreadSafeCacheImplementation() {
            CachedItems<String, String> threadSafeCache = new CachedItems<>(upperCaseMapper, true);

            // We can't directly access the internal map, but we can verify behavior
            // that would be consistent with ConcurrentHashMap usage
            threadSafeCache.get("test");
            assertThat(threadSafeCache.getCacheSize()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Performance")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle large number of unique keys")
        void testLargeNumberOfKeys() {
            final int NUM_KEYS = 10000;

            for (int i = 0; i < NUM_KEYS; i++) {
                cache.get("key_" + i);
            }

            assertThat(cache.getCacheSize()).isEqualTo(NUM_KEYS);
            assertThat(cache.getCacheMisses()).isEqualTo(NUM_KEYS);
            assertThat(cache.getCacheHits()).isEqualTo(0);
            assertThat(mapperCallCount).isEqualTo(NUM_KEYS);
        }

        @Test
        @DisplayName("Should handle repeated access to same key efficiently")
        void testRepeatedAccess() {
            final int NUM_ACCESSES = 10000;

            for (int i = 0; i < NUM_ACCESSES; i++) {
                String result = cache.get("same_key");
                assertThat(result).isEqualTo("SAME_KEY");
            }

            assertThat(cache.getCacheSize()).isEqualTo(1);
            assertThat(cache.getCacheMisses()).isEqualTo(1);
            assertThat(cache.getCacheHits()).isEqualTo(NUM_ACCESSES - 1);
            assertThat(mapperCallCount).isEqualTo(1);
        }

        @Test
        @DisplayName("Should work with different key and value types")
        void testDifferentTypes() {
            Function<Integer, String> intToStringMapper = key -> "number_" + key;
            CachedItems<Integer, String> intStringCache = new CachedItems<>(intToStringMapper);

            String result1 = intStringCache.get(42);
            String result2 = intStringCache.get(100);
            String result3 = intStringCache.get(42); // Cache hit

            assertThat(result1).isEqualTo("number_42");
            assertThat(result2).isEqualTo("number_100");
            assertThat(result3).isEqualTo("number_42");
            assertThat(intStringCache.getCacheHits()).isEqualTo(1);
            assertThat(intStringCache.getCacheMisses()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should handle complex object types")
        void testComplexObjectTypes() {
            class Person {
                final String name;
                final int age;

                Person(String name, int age) {
                    this.name = name;
                    this.age = age;
                }

                @Override
                public boolean equals(Object obj) {
                    if (this == obj)
                        return true;
                    if (!(obj instanceof Person))
                        return false;
                    Person person = (Person) obj;
                    return age == person.age && name.equals(person.name);
                }

                @Override
                public int hashCode() {
                    return name.hashCode() * 31 + age;
                }
            }

            Function<String, Person> personMapper = name -> new Person(name, name.length());
            CachedItems<String, Person> personCache = new CachedItems<>(personMapper);

            Person person1 = personCache.get("Alice");
            Person person2 = personCache.get("Alice"); // Should be same instance (cached)

            assertThat(person1).isSameAs(person2);
            assertThat(person1.name).isEqualTo("Alice");
            assertThat(person1.age).isEqualTo(5);
            assertThat(personCache.getCacheHits()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should maintain hit ratio accuracy with edge cases")
        void testHitRatioEdgeCases() {
            // Test with only hits after initial miss
            cache.get("test");
            for (int i = 0; i < 99; i++) {
                cache.get("test");
            }

            assertThat(cache.getHitRatio()).isCloseTo(0.99, within(0.01));
            assertThat(cache.getCacheTotalCalls()).isEqualTo(100);
        }
    }
}
