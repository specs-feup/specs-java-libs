package pt.up.fe.specs.util.system;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for VolatileContainer - a simple thread-safe
 * container that uses the volatile keyword to ensure visibility of changes
 * across threads.
 * 
 * Tests cover:
 * - Basic get/set operations
 * - Constructor variations
 * - Thread safety and visibility guarantees
 * - Generic type support
 * - Null handling
 * - Concurrent access patterns
 * - Memory model behavior
 * 
 * @author Generated Tests
 */
@DisplayName("VolatileContainer Tests")
class VolatileContainerTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("should create with default constructor")
        void testDefaultConstructor() {
            VolatileContainer<String> container = new VolatileContainer<>();

            assertThat(container.getElement()).isNull();
        }

        @Test
        @DisplayName("should create with initial element")
        void testConstructorWithInitialElement() {
            String initial = "initial value";
            VolatileContainer<String> container = new VolatileContainer<>(initial);

            assertThat(container.getElement()).isEqualTo(initial);
        }

        @Test
        @DisplayName("should create with null initial element")
        void testConstructorWithNullInitialElement() {
            VolatileContainer<String> container = new VolatileContainer<>(null);

            assertThat(container.getElement()).isNull();
        }

        @Test
        @DisplayName("should create with different generic types")
        void testConstructorWithDifferentTypes() {
            VolatileContainer<Integer> intContainer = new VolatileContainer<>(42);
            VolatileContainer<Boolean> boolContainer = new VolatileContainer<>(true);
            VolatileContainer<List<String>> listContainer = new VolatileContainer<>(new ArrayList<>());

            assertThat(intContainer.getElement()).isEqualTo(42);
            assertThat(boolContainer.getElement()).isTrue();
            assertThat(listContainer.getElement()).isNotNull().isEmpty();
        }
    }

    @Nested
    @DisplayName("Basic Operations")
    class BasicOperations {

        @Test
        @DisplayName("should get and set element")
        void testGetAndSetElement() {
            VolatileContainer<String> container = new VolatileContainer<>();

            String value = "test value";
            container.setElement(value);

            assertThat(container.getElement()).isEqualTo(value);
        }

        @Test
        @DisplayName("should handle null values")
        void testNullValues() {
            VolatileContainer<String> container = new VolatileContainer<>("initial");

            container.setElement(null);
            assertThat(container.getElement()).isNull();

            container.setElement("not null");
            assertThat(container.getElement()).isEqualTo("not null");

            container.setElement(null);
            assertThat(container.getElement()).isNull();
        }

        @Test
        @DisplayName("should overwrite existing values")
        void testOverwriteValues() {
            VolatileContainer<String> container = new VolatileContainer<>("first");

            assertThat(container.getElement()).isEqualTo("first");

            container.setElement("second");
            assertThat(container.getElement()).isEqualTo("second");

            container.setElement("third");
            assertThat(container.getElement()).isEqualTo("third");
        }

        @Test
        @DisplayName("should maintain reference identity")
        void testReferenceIdentity() {
            List<String> list = new ArrayList<>();
            VolatileContainer<List<String>> container = new VolatileContainer<>(list);

            assertThat(container.getElement()).isSameAs(list);

            List<String> newList = new ArrayList<>();
            container.setElement(newList);

            assertThat(container.getElement()).isSameAs(newList);
            assertThat(container.getElement()).isNotSameAs(list);
        }
    }

    @Nested
    @DisplayName("Generic Type Support")
    class GenericTypeSupport {

        @Test
        @DisplayName("should work with primitive wrapper types")
        void testPrimitiveWrapperTypes() {
            VolatileContainer<Integer> intContainer = new VolatileContainer<>(10);
            VolatileContainer<Double> doubleContainer = new VolatileContainer<>(3.14);
            VolatileContainer<Boolean> boolContainer = new VolatileContainer<>(false);
            VolatileContainer<Character> charContainer = new VolatileContainer<>('A');

            assertThat(intContainer.getElement()).isEqualTo(10);
            assertThat(doubleContainer.getElement()).isEqualTo(3.14);
            assertThat(boolContainer.getElement()).isFalse();
            assertThat(charContainer.getElement()).isEqualTo('A');

            intContainer.setElement(20);
            doubleContainer.setElement(2.71);
            boolContainer.setElement(true);
            charContainer.setElement('Z');

            assertThat(intContainer.getElement()).isEqualTo(20);
            assertThat(doubleContainer.getElement()).isEqualTo(2.71);
            assertThat(boolContainer.getElement()).isTrue();
            assertThat(charContainer.getElement()).isEqualTo('Z');
        }

        @Test
        @DisplayName("should work with collection types")
        void testCollectionTypes() {
            VolatileContainer<List<String>> listContainer = new VolatileContainer<>(new ArrayList<>());
            VolatileContainer<java.util.Set<Integer>> setContainer = new VolatileContainer<>(new java.util.HashSet<>());
            VolatileContainer<java.util.Map<String, Integer>> mapContainer = new VolatileContainer<>(
                    new java.util.HashMap<>());

            assertThat(listContainer.getElement()).isEmpty();
            assertThat(setContainer.getElement()).isEmpty();
            assertThat(mapContainer.getElement()).isEmpty();

            // Test modifications through the container
            List<String> list = listContainer.getElement();
            list.add("item");
            assertThat(listContainer.getElement()).containsExactly("item");

            java.util.Set<Integer> set = setContainer.getElement();
            set.add(42);
            assertThat(setContainer.getElement()).containsExactly(42);
        }

        @Test
        @DisplayName("should work with custom objects")
        void testCustomObjects() {
            Person person = new Person("John", 30);
            VolatileContainer<Person> container = new VolatileContainer<>(person);

            assertThat(container.getElement()).isEqualTo(person);
            assertThat(container.getElement().getName()).isEqualTo("John");
            assertThat(container.getElement().getAge()).isEqualTo(30);

            Person newPerson = new Person("Jane", 25);
            container.setElement(newPerson);

            assertThat(container.getElement()).isEqualTo(newPerson);
            assertThat(container.getElement().getName()).isEqualTo("Jane");
            assertThat(container.getElement().getAge()).isEqualTo(25);
        }

        // Helper class for testing
        static class Person {
            private final String name;
            private final int age;

            public Person(String name, int age) {
                this.name = name;
                this.age = age;
            }

            public String getName() {
                return name;
            }

            public int getAge() {
                return age;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null || getClass() != obj.getClass())
                    return false;
                Person person = (Person) obj;
                return age == person.age && name.equals(person.name);
            }

            @Override
            public int hashCode() {
                return name.hashCode() + age;
            }
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("should be visible across threads")
        void testVisibilityAcrossThreads() throws InterruptedException {
            VolatileContainer<String> container = new VolatileContainer<>("initial");
            CountDownLatch latch = new CountDownLatch(1);
            List<String> observedValues = Collections.synchronizedList(new ArrayList<>());

            // Reader thread
            Thread reader = new Thread(() -> {
                try {
                    latch.await();
                    // Give writer time to potentially update
                    Thread.sleep(50);
                    observedValues.add(container.getElement());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            reader.start();

            // Writer thread
            container.setElement("updated");
            latch.countDown();

            reader.join(1000);

            assertThat(observedValues).containsExactly("updated");
        }

        @Test
        @DisplayName("should handle concurrent writers")
        void testConcurrentWriters() throws InterruptedException {
            VolatileContainer<Integer> container = new VolatileContainer<>(0);
            int numberOfThreads = 10;
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch finishLatch = new CountDownLatch(numberOfThreads);

            // Create multiple writer threads
            for (int i = 0; i < numberOfThreads; i++) {
                final int threadId = i;
                new Thread(() -> {
                    try {
                        startLatch.await();
                        container.setElement(threadId);
                        finishLatch.countDown();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }

            startLatch.countDown();
            assertThat(finishLatch.await(5, TimeUnit.SECONDS)).isTrue();

            // Final value should be one of the thread IDs
            Integer finalValue = container.getElement();
            assertThat(finalValue).isBetween(0, numberOfThreads - 1);
        }

        @Test
        @DisplayName("should handle concurrent readers and writers")
        void testConcurrentReadersAndWriters() throws InterruptedException {
            VolatileContainer<String> container = new VolatileContainer<>("start");
            int numberOfReaders = 5;
            int numberOfWriters = 5;
            CountDownLatch startLatch = new CountDownLatch(1);
            CountDownLatch finishLatch = new CountDownLatch(numberOfReaders + numberOfWriters);
            List<String> observedValues = Collections.synchronizedList(new ArrayList<>());

            // Create reader threads
            for (int i = 0; i < numberOfReaders; i++) {
                new Thread(() -> {
                    try {
                        startLatch.await();
                        for (int j = 0; j < 10; j++) {
                            observedValues.add(container.getElement());
                            Thread.sleep(1);
                        }
                        finishLatch.countDown();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }

            // Create writer threads
            for (int i = 0; i < numberOfWriters; i++) {
                final int writerId = i;
                new Thread(() -> {
                    try {
                        startLatch.await();
                        for (int j = 0; j < 10; j++) {
                            container.setElement("writer-" + writerId + "-" + j);
                            Thread.sleep(1);
                        }
                        finishLatch.countDown();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }).start();
            }

            startLatch.countDown();
            assertThat(finishLatch.await(10, TimeUnit.SECONDS)).isTrue();

            // Should have observed some values
            assertThat(observedValues).isNotEmpty();
            // All values should be either "start" or match writer pattern
            assertThat(observedValues).allMatch(value -> value.equals("start") || value.matches("writer-\\d+-\\d+"));
        }

        @RepeatedTest(5)
        @DisplayName("should maintain volatile semantics under stress")
        void testVolatileSemanticsUnderStress() throws InterruptedException {
            VolatileContainer<AtomicInteger> container = new VolatileContainer<>(new AtomicInteger(0));
            int numberOfThreads = 20;
            int operationsPerThread = 100;
            ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);
            CountDownLatch startLatch = new CountDownLatch(1);

            List<Future<?>> futures = new ArrayList<>();

            // Create threads that continuously read and write
            for (int i = 0; i < numberOfThreads; i++) {
                futures.add(executor.submit(() -> {
                    try {
                        startLatch.await();
                        for (int j = 0; j < operationsPerThread; j++) {
                            // Read current value
                            AtomicInteger current = container.getElement();
                            int value = current.get();

                            // Create new AtomicInteger with incremented value
                            container.setElement(new AtomicInteger(value + 1));

                            // Small delay to increase contention
                            if (j % 10 == 0) {
                                Thread.yield();
                            }
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }));
            }

            startLatch.countDown();

            // Wait for all threads to complete
            for (Future<?> future : futures) {
                try {
                    future.get(10, TimeUnit.SECONDS);
                } catch (Exception e) {
                    fail("Thread execution failed", e);
                }
            }

            executor.shutdown();

            // Final value should be reasonable (may not be exact due to race conditions)
            int finalValue = container.getElement().get();
            assertThat(finalValue).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Memory Model Behavior")
    class MemoryModelBehavior {

        @Test
        @DisplayName("should provide happens-before relationship")
        void testHappensBeforeRelationship() throws InterruptedException {
            VolatileContainer<String> container = new VolatileContainer<>("initial");
            final boolean[] writerFinished = { false };
            List<String> readerResults = Collections.synchronizedList(new ArrayList<>());

            // Writer thread
            Thread writer = new Thread(() -> {
                container.setElement("updated");
                writerFinished[0] = true;
            });

            // Reader thread that waits for writer to finish
            Thread reader = new Thread(() -> {
                while (!writerFinished[0]) {
                    Thread.yield();
                }
                // Due to volatile semantics, we should see the updated value
                readerResults.add(container.getElement());
            });

            reader.start();
            writer.start();

            writer.join(1000);
            reader.join(1000);

            assertThat(readerResults).containsExactly("updated");
        }

        @Test
        @DisplayName("should not cache values across threads")
        void testNoCachingAcrossThreads() throws InterruptedException {
            VolatileContainer<Integer> container = new VolatileContainer<>(0);
            CountDownLatch readerReady = new CountDownLatch(1);
            CountDownLatch writerStarted = new CountDownLatch(1);
            List<Integer> observedValues = Collections.synchronizedList(new ArrayList<>());

            // Writer thread that updates value
            Thread writer = new Thread(() -> {
                try {
                    readerReady.await();
                    writerStarted.countDown();

                    for (int i = 1; i <= 20; i++) {
                        container.setElement(i);
                        Thread.sleep(10); // Longer sleep to ensure reader sees changes
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            // Reader thread that continuously reads
            Thread reader = new Thread(() -> {
                try {
                    readerReady.countDown();
                    writerStarted.await();

                    for (int i = 0; i < 50; i++) {
                        observedValues.add(container.getElement());
                        Thread.sleep(5);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });

            writer.start();
            reader.start();

            writer.join(5000);
            reader.join(5000);

            // Should observe at least the initial value (0) and some updates
            // Due to volatile semantics, reader should see updates eventually
            assertThat(observedValues).isNotEmpty();

            // Check if we observed progression (at least some different values)
            long distinctValues = observedValues.stream().distinct().count();
            assertThat(distinctValues).isGreaterThanOrEqualTo(2); // At least initial 0 and some update
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("should handle rapid successive updates")
        void testRapidSuccessiveUpdates() {
            VolatileContainer<Integer> container = new VolatileContainer<>(0);

            for (int i = 0; i < 1000; i++) {
                container.setElement(i);
                assertThat(container.getElement()).isEqualTo(i);
            }
        }

        @Test
        @DisplayName("should handle alternating null and non-null values")
        void testAlternatingNullAndNonNull() {
            VolatileContainer<String> container = new VolatileContainer<>();

            for (int i = 0; i < 100; i++) {
                if (i % 2 == 0) {
                    container.setElement("value-" + i);
                    assertThat(container.getElement()).isEqualTo("value-" + i);
                } else {
                    container.setElement(null);
                    assertThat(container.getElement()).isNull();
                }
            }
        }

        @Test
        @DisplayName("should handle large objects")
        void testLargeObjects() {
            // Create a large object (large string)
            String largeString = "x".repeat(100000);
            VolatileContainer<String> container = new VolatileContainer<>(largeString);

            assertThat(container.getElement()).hasSize(100000);
            assertThat(container.getElement()).isEqualTo(largeString);

            // Update with another large object
            String anotherLargeString = "y".repeat(50000);
            container.setElement(anotherLargeString);

            assertThat(container.getElement()).hasSize(50000);
            assertThat(container.getElement()).isEqualTo(anotherLargeString);
        }

        @Test
        @DisplayName("should work with immutable objects")
        void testImmutableObjects() {
            String immutableString = "immutable";
            VolatileContainer<String> container = new VolatileContainer<>(immutableString);

            String retrieved = container.getElement();
            assertThat(retrieved).isSameAs(immutableString);

            // Even though strings are immutable, we can still change the reference
            String newString = "new immutable";
            container.setElement(newString);

            assertThat(container.getElement()).isSameAs(newString);
            assertThat(container.getElement()).isNotSameAs(immutableString);
        }
    }
}
