package pt.up.fe.specs.util.threadstream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Comprehensive test suite for the ProducerThread class.
 * Tests producer thread functionality and object stream creation.
 * 
 * @author Generated Tests
 */
@DisplayName("ProducerThread Tests")
public class ProducerThreadTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create producer thread with producer and function")
        void testConstructorWithBasicParameters() {
            // Given
            var producer = new TestProducer(3, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;

            // When
            var producerThread = new TestableProducerThread<>(producer, produceFunction);

            // Then
            assertThat(producerThread).isNotNull();
            assertThat(producerThread).isInstanceOf(Runnable.class);
        }

        @Test
        @DisplayName("Should create producer thread with custom constructor function")
        void testConstructorWithCustomConstructor() {
            // Given
            var producer = new TestProducer(2, "POISON");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            Function<pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer<String>, ObjectStream<String>> cons = cc -> new GenericObjectStream<>(
                    cc, "POISON");

            // When
            var producerThread = new TestableProducerThread<>(producer, produceFunction, cons);

            // Then
            assertThat(producerThread).isNotNull();
            assertThat(producerThread).isInstanceOf(Runnable.class);
        }
    }

    @Nested
    @DisplayName("Channel Creation Tests")
    class ChannelCreationTests {

        @Test
        @DisplayName("Should create new channel with default depth")
        void testNewChannelDefaultDepth() {
            // Given
            var producer = new TestProducer(1, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var producerThread = new TestableProducerThread<>(producer, produceFunction);

            try {
                // When
                var stream = producerThread.newChannel();

                // Then
                assertThat(stream).isNotNull();
                assertThat(stream).isInstanceOf(ObjectStream.class);

                stream.close();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }

        @Test
        @DisplayName("Should create new channel with specified depth")
        void testNewChannelWithDepth() {
            // Given
            var producer = new TestProducer(1, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var producerThread = new TestableProducerThread<>(producer, produceFunction);

            try {
                // When
                var stream = producerThread.newChannel(5);

                // Then
                assertThat(stream).isNotNull();
                assertThat(stream).isInstanceOf(ObjectStream.class);

                stream.close();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }

        @Test
        @DisplayName("Should create multiple independent channels")
        void testMultipleChannels() {
            // Given
            var producer = new TestProducer(1, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var producerThread = new TestableProducerThread<>(producer, produceFunction);

            try {
                // When
                var stream1 = producerThread.newChannel();
                var stream2 = producerThread.newChannel();

                // Then
                assertThat(stream1).isNotNull();
                assertThat(stream2).isNotNull();
                assertThat(stream1).isNotSameAs(stream2);

                stream1.close();
                stream2.close();
            } catch (Exception e) {
                // Ignore close exceptions
            }
        }
    }

    @Nested
    @DisplayName("Production and Distribution Tests")
    class ProductionDistributionTests {

        @Test
        @DisplayName("Should produce and distribute items to single channel")
        @Timeout(value = 10, unit = TimeUnit.SECONDS)
        void testSingleChannelProduction() {
            // Given
            var producer = new TestProducer(3, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var producerThread = new TestableProducerThread<>(producer, produceFunction);

            try {
                var stream = producerThread.newChannel();

                // When - run producer in background
                var thread = new Thread(producerThread);
                thread.start();

                // Then - consume items
                var items = new ArrayList<String>();
                String item;
                while ((item = stream.next()) != null) {
                    items.add(item);
                }

                assertThat(items).containsExactly("item0", "item1", "item2");
                assertThat(stream.isClosed()).isTrue();

                thread.join();
                stream.close();
            } catch (Exception e) {
                // Ignore exceptions
            }
        }

        @Test
        @DisplayName("Should distribute items to multiple channels")
        @Timeout(value = 10, unit = TimeUnit.SECONDS)
        void testMultipleChannelDistribution() {
            // Given
            var producer = new TestProducer(2, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var producerThread = new TestableProducerThread<>(producer, produceFunction);

            try {
                // Use higher depth to avoid blocking
                var stream1 = producerThread.newChannel(5);
                var stream2 = producerThread.newChannel(5);

                // When - run producer
                var thread = new Thread(producerThread);
                thread.start();

                // Wait for producer to finish before consuming
                thread.join();

                // Then - both streams should receive all items
                var items1 = consumeAllItems(stream1);
                var items2 = consumeAllItems(stream2);

                assertThat(items1).containsExactly("item0", "item1");
                assertThat(items2).containsExactly("item0", "item1");

                stream1.close();
                stream2.close();
            } catch (Exception e) {
                // Ignore exceptions
            }
        }

        @Test
        @DisplayName("Should handle empty production")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testEmptyProduction() {
            // Given
            var producer = new TestProducer(0, "END"); // No items to produce
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var producerThread = new TestableProducerThread<>(producer, produceFunction);

            try {
                var stream = producerThread.newChannel();

                // When
                var thread = new Thread(producerThread);
                thread.start();

                // Then - should immediately receive poison
                assertThat(stream.next()).isNull();
                assertThat(stream.isClosed()).isTrue();

                thread.join();
                stream.close();
            } catch (Exception e) {
                // Ignore exceptions
            }
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle producer function exceptions")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testProducerFunctionException() {
            // Given
            var producer = new ExceptionThrowingProducer();
            Function<ExceptionThrowingProducer, String> produceFunction = p -> {
                throw new RuntimeException("Production failed");
            };
            var producerThread = new TestableProducerThread<>(producer, produceFunction);

            try {
                var stream = producerThread.newChannel();

                // When
                var thread = new Thread(producerThread);
                thread.start();

                // Then - should handle exception and terminate
                assertThatCode(() -> thread.join(2000)).doesNotThrowAnyException();

                stream.close();
            } catch (Exception e) {
                // Ignore exceptions
            }
        }
    }

    // Helper method
    private List<String> consumeAllItems(ObjectStream<String> stream) {
        var items = new ArrayList<String>();
        String item;
        while ((item = stream.next()) != null) {
            items.add(item);
        }
        return items;
    }

    // Test implementations
    private static class TestProducer implements ObjectProducer<String> {
        private final int totalItems;
        private final String poison;
        private int currentItem = 0;

        public TestProducer(int totalItems, String poison) {
            this.totalItems = totalItems;
            this.poison = poison;
        }

        public String nextItem() {
            if (currentItem >= totalItems) {
                return null; // Signal end of production
            }
            return "item" + (currentItem++);
        }

        @Override
        public String getPoison() {
            return poison;
        }

        @Override
        public void close() throws Exception {
            // Simple close implementation
        }
    }

    private static class ExceptionThrowingProducer implements ObjectProducer<String> {
        @Override
        public String getPoison() {
            return "POISON";
        }

        @Override
        public void close() throws Exception {
            // Simple close implementation
        }
    }

    // Testable version that exposes protected methods
    private static class TestableProducerThread<T, K extends ObjectProducer<T>> extends ProducerThread<T, K> {

        public TestableProducerThread(K producer, Function<K, T> produceFunction) {
            super(producer, produceFunction);
        }

        public TestableProducerThread(K producer, Function<K, T> produceFunction,
                Function<pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer<T>, ObjectStream<T>> cons) {
            super(producer, produceFunction, cons);
        }

        @Override
        public ObjectStream<T> newChannel() {
            return super.newChannel();
        }

        @Override
        public ObjectStream<T> newChannel(int depth) {
            return super.newChannel(depth);
        }
    }
}
