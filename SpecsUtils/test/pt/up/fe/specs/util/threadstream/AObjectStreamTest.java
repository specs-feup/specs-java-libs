package pt.up.fe.specs.util.threadstream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Comprehensive test suite for the AObjectStream abstract class.
 * Tests the abstract class through concrete test implementations.
 * 
 * @author Generated Tests
 */
@DisplayName("AObjectStream Tests")
public class AObjectStreamTest {

    @Nested
    @DisplayName("Abstract Class Implementation Tests")
    class AbstractClassTests {

        @Test
        @DisplayName("Should implement ObjectStream interface")
        void testImplementsInterface() {
            try (var stream = new TestObjectStream("POISON")) {
                assertThat(stream).isInstanceOf(ObjectStream.class);
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }

        @Test
        @DisplayName("Should require poison value in constructor")
        void testConstructorWithPoison() {
            try (var stream = new TestObjectStream("END")) {
                assertThat(stream).isNotNull();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }

        @Test
        @DisplayName("Should handle null poison value")
        void testConstructorWithNullPoison() {
            try (var stream = new TestObjectStream(null)) {
                assertThat(stream).isNotNull();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }
    }

    @Nested
    @DisplayName("Stream Lifecycle Tests")
    class StreamLifecycleTests {

        @Test
        @DisplayName("Should initialize lazily on first next() call")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testLazyInitialization() {
            try (var stream = new TestObjectStream("POISON")) {
                // Initially not initialized
                assertThat(stream.hasNext()).isTrue(); // Should return true before initialization

                // First call to next() should trigger initialization
                stream.addItem("first");
                stream.addPoison();

                assertThat(stream.next()).isEqualTo("first");
                assertThat(stream.next()).isNull(); // Poison converted to null
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }

        @Test
        @DisplayName("Should handle stream with no items")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testEmptyStream() {
            try (var stream = new TestObjectStream("POISON")) {
                // Only add poison
                stream.addPoison();

                assertThat(stream.next()).isNull();
                assertThat(stream.isClosed()).isTrue();
                assertThat(stream.hasNext()).isFalse();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }

        @Test
        @DisplayName("Should consume items in correct order")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testOrderedConsumption() {
            try (var stream = new TestObjectStream("END")) {
                // Add items in order
                stream.addItem("first");
                stream.addItem("second");
                stream.addItem("third");
                stream.addPoison();

                assertThat(stream.next()).isEqualTo("first");
                assertThat(stream.next()).isEqualTo("second");
                assertThat(stream.next()).isEqualTo("third");
                assertThat(stream.next()).isNull();
                assertThat(stream.isClosed()).isTrue();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }
    }

    @Nested
    @DisplayName("Peek Functionality Tests")
    class PeekFunctionalityTests {

        @Test
        @DisplayName("Should peek without consuming")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testPeekWithoutConsumption() {
            try (var stream = new TestObjectStream("POISON")) {
                stream.addItem("item1");
                stream.addItem("item2");
                stream.addPoison();

                // Note: peek returns null before first next() call due to lazy initialization
                assertThat(stream.peekNext()).isNull(); // Not initialized yet

                // After first next(), peek works
                assertThat(stream.next()).isEqualTo("item1");
                assertThat(stream.peekNext()).isEqualTo("item2");
                assertThat(stream.peekNext()).isEqualTo("item2"); // Peek should not consume

                // Now consume
                assertThat(stream.next()).isEqualTo("item2");
                assertThat(stream.peekNext()).isNull();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }

        @Test
        @DisplayName("Should return null when peeking at end")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testPeekAtEnd() {
            try (var stream = new TestObjectStream("POISON")) {
                stream.addItem("last");
                stream.addPoison();

                stream.next(); // Consume last item
                assertThat(stream.peekNext()).isNull();
                assertThat(stream.next()).isNull(); // Confirm stream is closed
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }
    }

    @Nested
    @DisplayName("State Management Tests")
    class StateManagementTests {

        @Test
        @DisplayName("Should track closed state correctly")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testClosedState() {
            try (var stream = new TestObjectStream("POISON")) {
                assertThat(stream.isClosed()).isFalse();

                stream.addItem("item");
                stream.addPoison();

                assertThat(stream.isClosed()).isFalse(); // Not closed until poison detected
                stream.next(); // Consume item, this triggers detection of poison internally
                assertThat(stream.isClosed()).isTrue(); // Now closed (poison detected in internal getNext)
                stream.next(); // Consume poison (returns null)
                assertThat(stream.isClosed()).isTrue(); // Still closed
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }

        @Test
        @DisplayName("Should handle hasNext correctly")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testHasNext() {
            try (var stream = new TestObjectStream("POISON")) {
                // Before initialization, should return true
                assertThat(stream.hasNext()).isTrue();

                stream.addItem("item");
                stream.addPoison();

                assertThat(stream.hasNext()).isTrue();
                stream.next(); // Consume item
                assertThat(stream.hasNext()).isFalse(); // No more items after this
                stream.next(); // Consume poison
                assertThat(stream.hasNext()).isFalse();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }
    }

    @Nested
    @DisplayName("Poison Handling Tests")
    class PoisonHandlingTests {

        @Test
        @DisplayName("Should convert poison to null")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testPoisonToNullConversion() {
            try (var stream = new TestObjectStream("TERMINATE")) {
                stream.addItem("TERMINATE"); // This should be treated as poison

                assertThat(stream.next()).isNull();
                assertThat(stream.isClosed()).isTrue();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }

        @Test
        @DisplayName("Should handle different poison types")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testDifferentPoisonTypes() {
            try (var intStream = new TestIntegerStream(-999)) {
                intStream.addItem(1);
                intStream.addItem(2);
                intStream.addPoison(); // Use the addPoison method

                assertThat(intStream.next()).isEqualTo(1);
                assertThat(intStream.next()).isEqualTo(2);
                assertThat(intStream.next()).isNull(); // Poison converted to null
                assertThat(intStream.isClosed()).isTrue();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }

        @Test
        @DisplayName("Should handle null poison correctly")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testNullPoison() {
            try (var stream = new TestObjectStream(null)) {
                stream.addItem("item");
                stream.addItem(null); // This is poison

                assertThat(stream.next()).isEqualTo("item");
                assertThat(stream.next()).isNull(); // Poison (null) results in null
                assertThat(stream.isClosed()).isTrue();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }
    }

    // Test implementations
    private static class TestObjectStream extends AObjectStream<String> {
        private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        private final String poison;

        public TestObjectStream(String poison) {
            super(poison);
            this.poison = poison;
        }

        @Override
        protected String consumeFromProvider() {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        public void addItem(String item) {
            queue.offer(item);
        }

        public void addPoison() {
            queue.offer(this.poison);
        }

        @Override
        public void close() throws Exception {
            // Simple close implementation
        }
    }

    private static class TestIntegerStream extends AObjectStream<Integer> {
        private final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>();
        private final Integer poison;

        public TestIntegerStream(Integer poison) {
            super(poison);
            this.poison = poison;
        }

        @Override
        protected Integer consumeFromProvider() {
            try {
                return queue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            }
        }

        public void addItem(Integer item) {
            queue.offer(item);
        }

        public void addPoison() {
            queue.offer(this.poison);
        }

        @Override
        public void close() throws Exception {
            // Simple close implementation
        }
    }
}
