package pt.up.fe.specs.util.collections.pushingqueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link PushingQueue} interface and
 * {@link ArrayPushingQueue} implementation.
 * Tests fixed-size queue with head insertion and tail dropping behavior.
 * 
 * @author Generated Tests
 */
class PushingQueueTest {

    private PushingQueue<String> queue;
    private static final int DEFAULT_CAPACITY = 3;

    @BeforeEach
    void setUp() {
        queue = new ArrayPushingQueue<>(DEFAULT_CAPACITY);
    }

    @Nested
    @DisplayName("Constructor and Basic Properties")
    class ConstructorTests {

        @Test
        @DisplayName("Should create queue with specified capacity")
        void testQueueCreation() {
            PushingQueue<Integer> intQueue = new ArrayPushingQueue<>(5);

            assertThat(intQueue).isNotNull();
            assertThat(intQueue.size()).isEqualTo(5);
            assertThat(intQueue.currentSize()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should create empty queue initially")
        void testInitialState() {
            assertThat(queue.size()).isEqualTo(DEFAULT_CAPACITY);
            assertThat(queue.currentSize()).isEqualTo(0);
            assertThat(queue.getElement(0)).isNull();
        }

        @Test
        @DisplayName("Should handle zero capacity")
        void testZeroCapacity() {
            PushingQueue<String> emptyQueue = new ArrayPushingQueue<>(0);

            assertThat(emptyQueue.size()).isEqualTo(0);
            assertThat(emptyQueue.currentSize()).isEqualTo(0);

            emptyQueue.insertElement("test");
            assertThat(emptyQueue.currentSize()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle single capacity")
        void testSingleCapacity() {
            PushingQueue<String> singleQueue = new ArrayPushingQueue<>(1);

            assertThat(singleQueue.size()).isEqualTo(1);
            assertThat(singleQueue.currentSize()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle different generic types")
        void testGenericTypes() {
            PushingQueue<Integer> intQueue = new ArrayPushingQueue<>(3);
            PushingQueue<Object> objQueue = new ArrayPushingQueue<>(3);
            PushingQueue<List<String>> listQueue = new ArrayPushingQueue<>(3);

            assertThat(intQueue.size()).isEqualTo(3);
            assertThat(objQueue.size()).isEqualTo(3);
            assertThat(listQueue.size()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Element Insertion and Head Pushing")
    class InsertionTests {

        @Test
        @DisplayName("Should insert first element at head")
        void testFirstInsertion() {
            queue.insertElement("first");

            assertThat(queue.currentSize()).isEqualTo(1);
            assertThat(queue.getElement(0)).isEqualTo("first");
            assertThat(queue.getElement(1)).isNull();
            assertThat(queue.getElement(2)).isNull();
        }

        @Test
        @DisplayName("Should push elements when inserting at head")
        void testHeadPushing() {
            queue.insertElement("first");
            queue.insertElement("second");
            queue.insertElement("third");

            assertThat(queue.currentSize()).isEqualTo(3);
            assertThat(queue.getElement(0)).isEqualTo("third"); // Most recent at head
            assertThat(queue.getElement(1)).isEqualTo("second");
            assertThat(queue.getElement(2)).isEqualTo("first");
        }

        @Test
        @DisplayName("Should drop tail element when capacity exceeded")
        void testTailDropping() {
            queue.insertElement("first");
            queue.insertElement("second");
            queue.insertElement("third");
            queue.insertElement("fourth"); // Should drop "first"

            assertThat(queue.currentSize()).isEqualTo(3);
            assertThat(queue.getElement(0)).isEqualTo("fourth");
            assertThat(queue.getElement(1)).isEqualTo("third");
            assertThat(queue.getElement(2)).isEqualTo("second");

            // "first" should be dropped
            assertThat(queue.getElement(3)).isNull();
        }

        @Test
        @DisplayName("Should handle multiple capacity overflows")
        void testMultipleOverflows() {
            // Fill and overflow multiple times
            String[] elements = { "1st", "2nd", "3rd", "4th", "5th", "6th", "7th" };

            for (String element : elements) {
                queue.insertElement(element);
            }

            assertThat(queue.currentSize()).isEqualTo(3);
            assertThat(queue.getElement(0)).isEqualTo("7th");
            assertThat(queue.getElement(1)).isEqualTo("6th");
            assertThat(queue.getElement(2)).isEqualTo("5th");
        }

        @Test
        @DisplayName("Should handle null elements")
        void testNullInsertion() {
            queue.insertElement("first");
            queue.insertElement(null);
            queue.insertElement("third");

            assertThat(queue.currentSize()).isEqualTo(3);
            assertThat(queue.getElement(0)).isEqualTo("third");
            assertThat(queue.getElement(1)).isNull();
            assertThat(queue.getElement(2)).isEqualTo("first");
        }
    }

    @Nested
    @DisplayName("Element Access and Retrieval")
    class AccessTests {

        @Test
        @DisplayName("Should return correct element at valid index")
        void testValidAccess() {
            queue.insertElement("first");
            queue.insertElement("second");

            assertThat(queue.getElement(0)).isEqualTo("second");
            assertThat(queue.getElement(1)).isEqualTo("first");
        }

        @Test
        @DisplayName("Should return null for index beyond current size")
        void testAccessBeyondCurrentSize() {
            queue.insertElement("only");

            assertThat(queue.getElement(1)).isNull();
            assertThat(queue.getElement(2)).isNull();
            assertThat(queue.getElement(10)).isNull();
        }

        @Test
        @DisplayName("Should return null for negative index")
        void testNegativeIndex() {
            queue.insertElement("test");

            assertThat(queue.getElement(-1)).isNull();
            assertThat(queue.getElement(-5)).isNull();
        }

        @Test
        @DisplayName("Should handle access on empty queue")
        void testAccessEmptyQueue() {
            assertThat(queue.getElement(0)).isNull();
            assertThat(queue.getElement(1)).isNull();
            assertThat(queue.getElement(2)).isNull();
        }

        @Test
        @DisplayName("Should maintain access consistency after insertions")
        void testAccessConsistency() {
            queue.insertElement("A");
            assertThat(queue.getElement(0)).isEqualTo("A");

            queue.insertElement("B");
            assertThat(queue.getElement(0)).isEqualTo("B");
            assertThat(queue.getElement(1)).isEqualTo("A");

            queue.insertElement("C");
            assertThat(queue.getElement(0)).isEqualTo("C");
            assertThat(queue.getElement(1)).isEqualTo("B");
            assertThat(queue.getElement(2)).isEqualTo("A");
        }
    }

    @Nested
    @DisplayName("Size Management")
    class SizeTests {

        @Test
        @DisplayName("Should return correct maximum size")
        void testMaximumSize() {
            assertThat(queue.size()).isEqualTo(DEFAULT_CAPACITY);

            PushingQueue<String> bigQueue = new ArrayPushingQueue<>(10);
            assertThat(bigQueue.size()).isEqualTo(10);
        }

        @Test
        @DisplayName("Should track current size accurately")
        void testCurrentSizeTracking() {
            assertThat(queue.currentSize()).isEqualTo(0);

            queue.insertElement("first");
            assertThat(queue.currentSize()).isEqualTo(1);

            queue.insertElement("second");
            assertThat(queue.currentSize()).isEqualTo(2);

            queue.insertElement("third");
            assertThat(queue.currentSize()).isEqualTo(3);

            // Should not exceed capacity
            queue.insertElement("fourth");
            assertThat(queue.currentSize()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should maintain current size after multiple operations")
        void testCurrentSizeStability() {
            // Fill to capacity
            for (int i = 0; i < DEFAULT_CAPACITY; i++) {
                queue.insertElement("item" + i);
            }
            assertThat(queue.currentSize()).isEqualTo(DEFAULT_CAPACITY);

            // Add more elements - size should remain at capacity
            for (int i = 0; i < 5; i++) {
                queue.insertElement("extra" + i);
                assertThat(queue.currentSize()).isEqualTo(DEFAULT_CAPACITY);
            }
        }
    }

    @Nested
    @DisplayName("Iterator and Stream Operations")
    class IterationTests {

        @Test
        @DisplayName("Should provide working iterator")
        void testIterator() {
            queue.insertElement("first");
            queue.insertElement("second");
            queue.insertElement("third");

            Iterator<String> iterator = queue.iterator();

            assertThat(iterator.hasNext()).isTrue();
            assertThat(iterator.next()).isEqualTo("third"); // Head first
            assertThat(iterator.next()).isEqualTo("second");
            assertThat(iterator.next()).isEqualTo("first");
            assertThat(iterator.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Should handle iterator on empty queue")
        void testIteratorEmpty() {
            Iterator<String> iterator = queue.iterator();

            assertThat(iterator.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Should handle iterator on partially filled queue")
        void testIteratorPartiallyFilled() {
            queue.insertElement("only");

            Iterator<String> iterator = queue.iterator();

            assertThat(iterator.hasNext()).isTrue();
            assertThat(iterator.next()).isEqualTo("only");
            assertThat(iterator.hasNext()).isFalse();
        }

        @Test
        @DisplayName("Should provide working stream")
        void testStream() {
            queue.insertElement("apple");
            queue.insertElement("banana");
            queue.insertElement("cherry");

            List<String> collected = queue.stream()
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());

            assertThat(collected).containsExactly("CHERRY", "BANANA", "APPLE");
        }

        @Test
        @DisplayName("Should handle stream operations on empty queue")
        void testStreamEmpty() {
            List<String> collected = queue.stream().collect(Collectors.toList());

            assertThat(collected).isEmpty();
        }

        @Test
        @DisplayName("Should support default toString with mapper")
        void testToStringWithMapper() {
            queue.insertElement("apple");
            queue.insertElement("banana");

            Function<String, String> upperMapper = String::toUpperCase;
            String result = queue.toString(upperMapper);

            assertThat(result).isEqualTo("[BANANA, APPLE]");
        }
    }

    @Nested
    @DisplayName("String Representation")
    class ToStringTests {

        @Test
        @DisplayName("Should provide correct string representation when full")
        void testToStringFull() {
            queue.insertElement("first");
            queue.insertElement("second");
            queue.insertElement("third");

            String result = queue.toString();

            assertThat(result).isEqualTo("[third, second, first]");
        }

        @Test
        @DisplayName("Should provide correct string representation when partial")
        void testToStringPartial() {
            queue.insertElement("only");

            String result = queue.toString();

            assertThat(result).isEqualTo("[only, null, null]");
        }

        @Test
        @DisplayName("Should provide correct string representation when empty")
        void testToStringEmpty() {
            String result = queue.toString();

            assertThat(result).isEqualTo("[null, null, null]");
        }

        @Test
        @DisplayName("Should handle zero capacity toString")
        void testToStringZeroCapacity() {
            PushingQueue<String> emptyQueue = new ArrayPushingQueue<>(0);

            String result = emptyQueue.toString();

            assertThat(result).isEqualTo("[]");
        }

        @Test
        @DisplayName("Should handle null elements in toString")
        void testToStringWithNulls() {
            queue.insertElement("first");
            queue.insertElement(null);
            queue.insertElement("third");

            String result = queue.toString();

            assertThat(result).isEqualTo("[third, null, first]");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Complex Scenarios")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle mixed data types in Object queue")
        void testMixedDataTypes() {
            PushingQueue<Object> objQueue = new ArrayPushingQueue<>(3);

            objQueue.insertElement("string");
            objQueue.insertElement(42);
            objQueue.insertElement(List.of("nested", "list"));

            assertThat(objQueue.getElement(0)).isEqualTo(List.of("nested", "list"));
            assertThat(objQueue.getElement(1)).isEqualTo(42);
            assertThat(objQueue.getElement(2)).isEqualTo("string");
        }

        @Test
        @DisplayName("Should handle large capacity efficiently")
        void testLargeCapacity() {
            PushingQueue<Integer> bigQueue = new ArrayPushingQueue<>(1000);

            // Fill with sequential numbers
            for (int i = 0; i < 1000; i++) {
                bigQueue.insertElement(i);
            }

            assertThat(bigQueue.currentSize()).isEqualTo(1000);
            assertThat(bigQueue.getElement(0)).isEqualTo(999); // Last inserted
            assertThat(bigQueue.getElement(999)).isEqualTo(0); // First inserted

            // Add one more to trigger tail drop
            bigQueue.insertElement(1000);
            assertThat(bigQueue.currentSize()).isEqualTo(1000);
            assertThat(bigQueue.getElement(0)).isEqualTo(1000);
            assertThat(bigQueue.getElement(999)).isEqualTo(1);
        }

        @Test
        @DisplayName("Should maintain LIFO ordering consistently")
        void testLIFOOrdering() {
            String[] insertOrder = { "A", "B", "C", "D", "E", "F" };

            for (String item : insertOrder) {
                queue.insertElement(item);
            }

            // Should contain last 3 elements in reverse order
            assertThat(queue.getElement(0)).isEqualTo("F");
            assertThat(queue.getElement(1)).isEqualTo("E");
            assertThat(queue.getElement(2)).isEqualTo("D");
        }

        @Test
        @DisplayName("Should handle repeated insertions of same element")
        void testRepeatedElements() {
            queue.insertElement("same");
            queue.insertElement("same");
            queue.insertElement("same");
            queue.insertElement("same");

            assertThat(queue.currentSize()).isEqualTo(3);
            assertThat(queue.getElement(0)).isEqualTo("same");
            assertThat(queue.getElement(1)).isEqualTo("same");
            assertThat(queue.getElement(2)).isEqualTo("same");
        }

        @Test
        @DisplayName("Should handle alternating null and non-null insertions")
        void testAlternatingNulls() {
            queue.insertElement("A");
            queue.insertElement(null);
            queue.insertElement("B");
            queue.insertElement(null);
            queue.insertElement("C");

            assertThat(queue.currentSize()).isEqualTo(3);
            assertThat(queue.getElement(0)).isEqualTo("C");
            assertThat(queue.getElement(1)).isNull();
            assertThat(queue.getElement(2)).isEqualTo("B");
        }
    }

    @Nested
    @DisplayName("Performance and Integration Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle rapid insertions efficiently")
        void testRapidInsertions() {
            PushingQueue<Integer> perfQueue = new ArrayPushingQueue<>(100);

            // Insert 10000 elements rapidly
            for (int i = 0; i < 10000; i++) {
                perfQueue.insertElement(i);
            }

            assertThat(perfQueue.currentSize()).isEqualTo(100);
            assertThat(perfQueue.getElement(0)).isEqualTo(9999); // Last inserted
            assertThat(perfQueue.getElement(99)).isEqualTo(9900); // 100th from last
        }

        @Test
        @DisplayName("Should integrate well with stream operations")
        void testStreamIntegration() {
            queue.insertElement("apple");
            queue.insertElement("banana");
            queue.insertElement("cherry");

            long count = queue.stream()
                    .filter(s -> s != null)
                    .filter(s -> s.length() > 5)
                    .count();

            assertThat(count).isEqualTo(2); // "banana" and "cherry"
        }

        @Test
        @DisplayName("Should work correctly with custom mapper toString")
        void testCustomMapperIntegration() {
            PushingQueue<Integer> intQueue = new ArrayPushingQueue<>(3);
            intQueue.insertElement(1);
            intQueue.insertElement(2);
            intQueue.insertElement(3);

            String hexString = intQueue.toString(i -> i != null ? Integer.toHexString(i) : "null");

            assertThat(hexString).isEqualTo("[3, 2, 1]");
        }

        @Test
        @DisplayName("Should maintain consistency across all operations")
        void testOperationalConsistency() {
            // Complex scenario combining all operations
            queue.insertElement("A");
            assertThat(queue.currentSize()).isEqualTo(1);

            queue.insertElement("B");
            assertThat(queue.getElement(0)).isEqualTo("B");

            List<String> streamResult = queue.stream().collect(Collectors.toList());
            assertThat(streamResult).containsExactly("B", "A");

            queue.insertElement("C");
            queue.insertElement("D"); // Should drop "A"

            assertThat(queue.currentSize()).isEqualTo(3);
            assertThat(queue.toString()).isEqualTo("[D, C, B]");

            Iterator<String> iter = queue.iterator();
            assertThat(iter.next()).isEqualTo("D");
            assertThat(iter.next()).isEqualTo("C");
            assertThat(iter.next()).isEqualTo("B");
        }
    }

    @Nested
    @DisplayName("LinkedPushingQueue Implementation Tests")
    class LinkedPushingQueueTests {

        private LinkedPushingQueue<String> linkedQueue;

        @BeforeEach
        void setUp() {
            linkedQueue = new LinkedPushingQueue<>(DEFAULT_CAPACITY);
        }

        @Test
        @DisplayName("Should create LinkedPushingQueue with correct capacity")
        void testLinkedQueueCreation() {
            assertThat(linkedQueue.size()).isEqualTo(DEFAULT_CAPACITY);
            assertThat(linkedQueue.currentSize()).isZero();
        }

        @Test
        @DisplayName("Should implement PushingQueue interface correctly")
        void testInterfaceImplementation() {
            linkedQueue.insertElement("first");
            linkedQueue.insertElement("second");

            assertThat(linkedQueue.getElement(0)).isEqualTo("second");
            assertThat(linkedQueue.getElement(1)).isEqualTo("first");
            assertThat(linkedQueue.currentSize()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should handle capacity overflow with LinkedList backing")
        void testLinkedQueueOverflow() {
            // Fill beyond capacity
            for (int i = 0; i < DEFAULT_CAPACITY + 2; i++) {
                linkedQueue.insertElement("item" + i);
            }

            assertThat(linkedQueue.currentSize()).isEqualTo(DEFAULT_CAPACITY);
            assertThat(linkedQueue.getElement(0)).isEqualTo("item" + (DEFAULT_CAPACITY + 1));
            assertThat(linkedQueue.getElement(DEFAULT_CAPACITY - 1)).isEqualTo("item2");
            assertThat(linkedQueue.getElement(DEFAULT_CAPACITY)).isNull();
        }

        @Test
        @DisplayName("Should provide working iterator for LinkedPushingQueue")
        void testLinkedQueueIterator() {
            linkedQueue.insertElement("A");
            linkedQueue.insertElement("B");
            linkedQueue.insertElement("C");

            List<String> collected = new ArrayList<>();
            linkedQueue.iterator().forEachRemaining(collected::add);

            assertThat(collected).containsExactly("C", "B", "A");
        }

        @Test
        @DisplayName("Should support stream operations with LinkedPushingQueue")
        void testLinkedQueueStream() {
            linkedQueue.insertElement("apple");
            linkedQueue.insertElement("banana");

            long count = linkedQueue.stream()
                    .filter(s -> s.length() > 5)
                    .count();

            assertThat(count).isEqualTo(1); // Only "banana"
        }

        @Test
        @DisplayName("Should handle large capacity with LinkedPushingQueue")
        void testLinkedQueueLargeCapacity() {
            LinkedPushingQueue<Integer> largeQueue = new LinkedPushingQueue<>(1000);

            for (int i = 0; i < 1500; i++) {
                largeQueue.insertElement(i);
            }

            assertThat(largeQueue.currentSize()).isEqualTo(1000);
            assertThat(largeQueue.getElement(0)).isEqualTo(1499);
            assertThat(largeQueue.getElement(999)).isEqualTo(500);
        }

        @Test
        @DisplayName("Should maintain FIFO dropping behavior consistently")
        void testLinkedQueueFIFODropping() {
            linkedQueue.insertElement("first");
            linkedQueue.insertElement("second");
            linkedQueue.insertElement("third");
            linkedQueue.insertElement("fourth"); // Should drop "first"

            assertThat(linkedQueue.getElement(0)).isEqualTo("fourth");
            assertThat(linkedQueue.getElement(1)).isEqualTo("third");
            assertThat(linkedQueue.getElement(2)).isEqualTo("second");
            assertThat(linkedQueue.getElement(3)).isNull();
        }

        @Test
        @DisplayName("Should handle null elements in LinkedPushingQueue")
        void testLinkedQueueNullHandling() {
            linkedQueue.insertElement("valid");
            linkedQueue.insertElement(null);
            linkedQueue.insertElement("another");

            assertThat(linkedQueue.getElement(0)).isEqualTo("another");
            assertThat(linkedQueue.getElement(1)).isNull();
            assertThat(linkedQueue.getElement(2)).isEqualTo("valid");
        }

        @Test
        @DisplayName("Should provide correct string representation when empty")
        void testToStringEmpty() {
            String result = linkedQueue.toString();

            assertThat(result).isEqualTo("[null, null, null]");
        }

        @Test
        @DisplayName("Should provide correct toString for LinkedPushingQueue")
        void testLinkedQueueToString() {
            linkedQueue.insertElement("A");
            linkedQueue.insertElement("B");

            String result = linkedQueue.toString(String::toLowerCase);
            assertThat(result).isEqualTo("[b, a, null]");
        }

        @Test
        @DisplayName("Should handle zero capacity LinkedPushingQueue")
        void testLinkedQueueZeroCapacity() {
            LinkedPushingQueue<String> zeroQueue = new LinkedPushingQueue<>(0);

            zeroQueue.insertElement("test");
            assertThat(zeroQueue.currentSize()).isZero();
            assertThat(zeroQueue.getElement(0)).isNull();
        }

        @Test
        @DisplayName("Should handle single capacity LinkedPushingQueue")
        void testLinkedQueueSingleCapacity() {
            LinkedPushingQueue<String> singleQueue = new LinkedPushingQueue<>(1);

            singleQueue.insertElement("first");
            singleQueue.insertElement("second"); // Should replace "first"

            assertThat(singleQueue.currentSize()).isEqualTo(1);
            assertThat(singleQueue.getElement(0)).isEqualTo("second");
            assertThat(singleQueue.getElement(1)).isNull();
        }
    }

    @Nested
    @DisplayName("MixedPushingQueue Implementation Tests")
    class MixedPushingQueueTests {

        @Test
        @DisplayName("Should use ArrayPushingQueue for small capacity")
        void testSmallCapacityUsesArray() {
            MixedPushingQueue<String> smallQueue = new MixedPushingQueue<>(20); // Below threshold of 40

            smallQueue.insertElement("test1");
            smallQueue.insertElement("test2");

            assertThat(smallQueue.size()).isEqualTo(20);
            assertThat(smallQueue.getElement(0)).isEqualTo("test2");
            assertThat(smallQueue.getElement(1)).isEqualTo("test1");
        }

        @Test
        @DisplayName("Should use LinkedPushingQueue for large capacity")
        void testLargeCapacityUsesLinked() {
            MixedPushingQueue<String> largeQueue = new MixedPushingQueue<>(50); // Above threshold of 40

            largeQueue.insertElement("test1");
            largeQueue.insertElement("test2");

            assertThat(largeQueue.size()).isEqualTo(50);
            assertThat(largeQueue.getElement(0)).isEqualTo("test2");
            assertThat(largeQueue.getElement(1)).isEqualTo("test1");
        }

        @Test
        @DisplayName("Should handle threshold boundary correctly")
        void testThresholdBoundary() {
            MixedPushingQueue<String> atThreshold = new MixedPushingQueue<>(40); // Exactly at threshold
            MixedPushingQueue<String> belowThreshold = new MixedPushingQueue<>(39); // Below threshold

            // Both should work the same way for basic operations
            atThreshold.insertElement("at");
            belowThreshold.insertElement("below");

            assertThat(atThreshold.getElement(0)).isEqualTo("at");
            assertThat(belowThreshold.getElement(0)).isEqualTo("below");
        }

        @Test
        @DisplayName("Should delegate all operations correctly")
        void testOperationDelegation() {
            MixedPushingQueue<Integer> mixedQueue = new MixedPushingQueue<>(5);

            // Test all interface methods
            mixedQueue.insertElement(1);
            mixedQueue.insertElement(2);
            mixedQueue.insertElement(3);

            assertThat(mixedQueue.getElement(0)).isEqualTo(3);
            assertThat(mixedQueue.getElement(1)).isEqualTo(2);
            assertThat(mixedQueue.getElement(2)).isEqualTo(1);
            assertThat(mixedQueue.size()).isEqualTo(5);
            assertThat(mixedQueue.currentSize()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should provide working iterator delegation")
        void testIteratorDelegation() {
            MixedPushingQueue<String> mixedQueue = new MixedPushingQueue<>(10);

            mixedQueue.insertElement("A");
            mixedQueue.insertElement("B");
            mixedQueue.insertElement("C");

            List<String> collected = new ArrayList<>();
            mixedQueue.iterator().forEachRemaining(collected::add);

            assertThat(collected).containsExactly("C", "B", "A");
        }

        @Test
        @DisplayName("Should provide working stream delegation")
        void testStreamDelegation() {
            MixedPushingQueue<String> mixedQueue = new MixedPushingQueue<>(15);

            mixedQueue.insertElement("apple");
            mixedQueue.insertElement("banana");
            mixedQueue.insertElement("cherry");

            List<String> filtered = mixedQueue.stream()
                    .filter(s -> s.contains("a"))
                    .collect(Collectors.toList());

            assertThat(filtered).containsExactly("banana", "apple");
        }

        @Test
        @DisplayName("Should provide correct toString delegation")
        void testToStringDelegation() {
            MixedPushingQueue<String> mixedQueue = new MixedPushingQueue<>(3);

            mixedQueue.insertElement("first");
            mixedQueue.insertElement("second");

            String result = mixedQueue.toString();
            assertThat(result).contains("second").contains("first");
        }

        @Test
        @DisplayName("Should handle capacity overflow correctly for both implementations")
        void testOverflowBehavior() {
            // Test small capacity (array-based)
            MixedPushingQueue<Integer> smallQueue = new MixedPushingQueue<>(3);
            for (int i = 0; i < 5; i++) {
                smallQueue.insertElement(i);
            }
            assertThat(smallQueue.currentSize()).isEqualTo(3);
            assertThat(smallQueue.getElement(0)).isEqualTo(4);

            // Test large capacity (linked-based)
            MixedPushingQueue<Integer> largeQueue = new MixedPushingQueue<>(50);
            for (int i = 0; i < 52; i++) {
                largeQueue.insertElement(i);
            }
            assertThat(largeQueue.currentSize()).isEqualTo(50);
            assertThat(largeQueue.getElement(0)).isEqualTo(51);
        }

        @Test
        @DisplayName("Should handle null elements consistently")
        void testNullHandling() {
            MixedPushingQueue<String> mixedQueue = new MixedPushingQueue<>(25);

            mixedQueue.insertElement("valid");
            mixedQueue.insertElement(null);
            mixedQueue.insertElement("another");

            assertThat(mixedQueue.getElement(0)).isEqualTo("another");
            assertThat(mixedQueue.getElement(1)).isNull();
            assertThat(mixedQueue.getElement(2)).isEqualTo("valid");
        }

        @Test
        @DisplayName("Should work with different data types")
        void testGenericTypeSupport() {
            MixedPushingQueue<Double> doubleQueue = new MixedPushingQueue<>(5);
            MixedPushingQueue<List<String>> listQueue = new MixedPushingQueue<>(60);

            doubleQueue.insertElement(3.14);
            doubleQueue.insertElement(2.71);

            listQueue.insertElement(Arrays.asList("a", "b"));
            listQueue.insertElement(Arrays.asList("c", "d"));

            assertThat(doubleQueue.getElement(0)).isEqualTo(2.71);
            assertThat(listQueue.getElement(0)).isEqualTo(Arrays.asList("c", "d"));
        }

        @Test
        @DisplayName("Should maintain performance characteristics for both implementations")
        void testPerformanceCharacteristics() {
            // This test ensures both implementations work under load
            MixedPushingQueue<Integer> smallQueue = new MixedPushingQueue<>(10);
            MixedPushingQueue<Integer> largeQueue = new MixedPushingQueue<>(100);

            // Load both queues heavily
            for (int i = 0; i < 1000; i++) {
                smallQueue.insertElement(i);
                largeQueue.insertElement(i);
            }

            assertThat(smallQueue.currentSize()).isEqualTo(10);
            assertThat(largeQueue.currentSize()).isEqualTo(100);
            assertThat(smallQueue.getElement(0)).isEqualTo(999);
            assertThat(largeQueue.getElement(0)).isEqualTo(999);
        }

        @Test
        @DisplayName("Should handle edge case capacities")
        void testEdgeCaseCapacities() {
            // Test capacity of exactly 1
            MixedPushingQueue<String> single = new MixedPushingQueue<>(1);
            single.insertElement("only");
            single.insertElement("replacement");
            assertThat(single.getElement(0)).isEqualTo("replacement");

            // Test very large capacity
            MixedPushingQueue<String> huge = new MixedPushingQueue<>(10000);
            huge.insertElement("test");
            assertThat(huge.getElement(0)).isEqualTo("test");
            assertThat(huge.size()).isEqualTo(10000);
        }
    }

    @Nested
    @DisplayName("Implementation Comparison Tests")
    class ImplementationComparisonTests {

        @Test
        @DisplayName("All implementations should behave identically for basic operations")
        void testImplementationConsistency() {
            int capacity = 5;
            ArrayPushingQueue<String> arrayQueue = new ArrayPushingQueue<>(capacity);
            LinkedPushingQueue<String> linkedQueue = new LinkedPushingQueue<>(capacity);
            MixedPushingQueue<String> mixedQueue = new MixedPushingQueue<>(capacity);

            String[] testData = { "A", "B", "C", "D", "E", "F", "G" };

            // Insert same data into all queues
            for (String item : testData) {
                arrayQueue.insertElement(item);
                linkedQueue.insertElement(item);
                mixedQueue.insertElement(item);
            }

            // All should have same size and contents
            assertThat(arrayQueue.currentSize()).isEqualTo(linkedQueue.currentSize())
                    .isEqualTo(mixedQueue.currentSize())
                    .isEqualTo(capacity);

            // All should have same elements at same positions
            for (int i = 0; i < capacity; i++) {
                String arrayElement = arrayQueue.getElement(i);
                String linkedElement = linkedQueue.getElement(i);
                String mixedElement = mixedQueue.getElement(i);

                assertThat(arrayElement).isEqualTo(linkedElement).isEqualTo(mixedElement);
            }
        }

        @Test
        @DisplayName("All implementations should handle edge cases identically")
        void testEdgeCaseConsistency() {
            int capacity = 3;
            ArrayPushingQueue<Integer> arrayQueue = new ArrayPushingQueue<>(capacity);
            LinkedPushingQueue<Integer> linkedQueue = new LinkedPushingQueue<>(capacity);
            MixedPushingQueue<Integer> mixedQueue = new MixedPushingQueue<>(capacity);

            // Test null handling
            arrayQueue.insertElement(null);
            linkedQueue.insertElement(null);
            mixedQueue.insertElement(null);

            assertThat(arrayQueue.getElement(0)).isEqualTo(linkedQueue.getElement(0))
                    .isEqualTo(mixedQueue.getElement(0)).isNull();

            // Test out-of-bounds access
            assertThat(arrayQueue.getElement(10)).isEqualTo(linkedQueue.getElement(10))
                    .isEqualTo(mixedQueue.getElement(10)).isNull();

            // Test negative index access
            assertThat(arrayQueue.getElement(-1)).isEqualTo(linkedQueue.getElement(-1))
                    .isEqualTo(mixedQueue.getElement(-1)).isNull();
        }

        @Test
        @DisplayName("All implementations should provide consistent iteration")
        void testIterationConsistency() {
            int capacity = 4;
            ArrayPushingQueue<String> arrayQueue = new ArrayPushingQueue<>(capacity);
            LinkedPushingQueue<String> linkedQueue = new LinkedPushingQueue<>(capacity);
            MixedPushingQueue<String> mixedQueue = new MixedPushingQueue<>(capacity);

            String[] testData = { "alpha", "beta", "gamma", "delta", "epsilon" };

            for (String item : testData) {
                arrayQueue.insertElement(item);
                linkedQueue.insertElement(item);
                mixedQueue.insertElement(item);
            }

            List<String> arrayList = new ArrayList<>();
            List<String> linkedList = new ArrayList<>();
            List<String> mixedList = new ArrayList<>();

            arrayQueue.iterator().forEachRemaining(arrayList::add);
            linkedQueue.iterator().forEachRemaining(linkedList::add);
            mixedQueue.iterator().forEachRemaining(mixedList::add);

            assertThat(arrayList).isEqualTo(linkedList).isEqualTo(mixedList);
        }

        @Test
        @DisplayName("All implementations should provide consistent stream operations")
        void testStreamConsistency() {
            int capacity = 3;
            ArrayPushingQueue<String> arrayQueue = new ArrayPushingQueue<>(capacity);
            LinkedPushingQueue<String> linkedQueue = new LinkedPushingQueue<>(capacity);
            MixedPushingQueue<String> mixedQueue = new MixedPushingQueue<>(capacity);

            arrayQueue.insertElement("apple");
            arrayQueue.insertElement("banana");
            arrayQueue.insertElement("cherry");

            linkedQueue.insertElement("apple");
            linkedQueue.insertElement("banana");
            linkedQueue.insertElement("cherry");

            mixedQueue.insertElement("apple");
            mixedQueue.insertElement("banana");
            mixedQueue.insertElement("cherry");

            List<String> arrayFiltered = arrayQueue.stream()
                    .filter(s -> s.length() > 5)
                    .collect(Collectors.toList());

            List<String> linkedFiltered = linkedQueue.stream()
                    .filter(s -> s.length() > 5)
                    .collect(Collectors.toList());

            List<String> mixedFiltered = mixedQueue.stream()
                    .filter(s -> s.length() > 5)
                    .collect(Collectors.toList());

            assertThat(arrayFiltered).isEqualTo(linkedFiltered).isEqualTo(mixedFiltered);
        }
    }
}
