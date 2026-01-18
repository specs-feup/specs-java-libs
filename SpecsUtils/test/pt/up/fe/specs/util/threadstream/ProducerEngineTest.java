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
 * Comprehensive test suite for the ProducerEngine class.
 * Tests the complete producer-consumer orchestration system.
 * 
 * @author Generated Tests
 */
@DisplayName("ProducerEngine Tests")
public class ProducerEngineTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create engine with producer and function")
        void testConstructorWithBasicParameters() {
            // Given
            var producer = new TestProducer(3, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;

            // When
            var engine = new ProducerEngine<>(producer, produceFunction);

            // Then
            assertThat(engine).isNotNull();
        }

        @Test
        @DisplayName("Should create engine with custom consumer constructor")
        void testConstructorWithCustomConsumer() {
            // Given
            var producer = new TestProducer(2, "POISON");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            Function<pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer<String>, ObjectStream<String>> cons = cc -> new GenericObjectStream<>(
                    cc, "POISON");

            // When
            var engine = new ProducerEngine<>(producer, produceFunction, cons);

            // Then
            assertThat(engine).isNotNull();
        }
    }

    @Nested
    @DisplayName("Consumer Subscription Tests")
    class ConsumerSubscriptionTests {

        @Test
        @DisplayName("Should subscribe single consumer")
        void testSubscribeSingleConsumer() {
            // Given
            var producer = new TestProducer(2, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var engine = new ProducerEngine<>(producer, produceFunction);

            // When
            var consumer = engine.subscribe(stream -> countItems(stream));

            // Then
            assertThat(consumer).isNotNull();
            assertThat(engine.getConsumers()).hasSize(1);
            assertThat(engine.getConsumer(0)).isSameAs(consumer);
        }

        @Test
        @DisplayName("Should subscribe multiple consumers")
        void testSubscribeMultipleConsumers() {
            // Given
            var producer = new TestProducer(3, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var engine = new ProducerEngine<>(producer, produceFunction);

            // When
            var consumer1 = engine.subscribe(stream -> countItems(stream));
            var consumer2 = engine.subscribe(stream -> concatenateItems(stream));
            var consumer3 = engine.subscribe(stream -> countItems(stream));

            // Then
            assertThat(engine.getConsumers()).hasSize(3);
            assertThat(engine.getConsumer(0)).isSameAs(consumer1);
            assertThat(engine.getConsumer(1)).isSameAs(consumer2);
            assertThat(engine.getConsumer(2)).isSameAs(consumer3);
        }

        @Test
        @DisplayName("Should handle lambda consumer functions")
        void testLambdaConsumerFunctions() {
            // Given
            var producer = new TestProducer(1, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var engine = new ProducerEngine<>(producer, produceFunction);

            // When
            var consumer = engine.subscribe(stream -> {
                var result = new ArrayList<String>();
                String item;
                while ((item = stream.next()) != null) {
                    result.add(item.toUpperCase());
                }
                return result;
            });

            // Then
            assertThat(consumer).isNotNull();
            assertThat(engine.getConsumers()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Execution Tests")
    class ExecutionTests {

        @Test
        @DisplayName("Should execute single producer-consumer pair")
        @Timeout(value = 10, unit = TimeUnit.SECONDS)
        void testSingleProducerConsumer() {
            // Given
            var producer = new TestProducer(3, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var engine = new ProducerEngine<>(producer, produceFunction);
            var consumer = engine.subscribe(stream -> countItems(stream));

            // When
            engine.launch(); // This blocks until completion

            // Then
            assertThat(consumer.getConsumeResult()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should execute multiple consumers simultaneously")
        @Timeout(value = 10, unit = TimeUnit.SECONDS)
        void testMultipleConsumers() {
            // Given
            var producer = new TestProducer(4, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var engine = new ProducerEngine<>(producer, produceFunction);

            var counter = engine.subscribe(stream -> countItems(stream));
            var concatenator = engine.subscribe(stream -> concatenateItems(stream));

            // When
            engine.launch();

            // Then
            assertThat(counter.getConsumeResult()).isEqualTo(4);
            assertThat(concatenator.getConsumeResult()).isEqualTo("item0,item1,item2,item3,");
        }

        @Test
        @DisplayName("Should handle empty production")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testEmptyProduction() {
            // Given
            var producer = new TestProducer(0, "END"); // No items
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var engine = new ProducerEngine<>(producer, produceFunction);
            var consumer = engine.subscribe(stream -> countItems(stream));

            // When
            engine.launch();

            // Then
            assertThat(consumer.getConsumeResult()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle large production")
        @Timeout(value = 15, unit = TimeUnit.SECONDS)
        void testLargeProduction() {
            // Given
            var producer = new TestProducer(1000, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var engine = new ProducerEngine<>(producer, produceFunction);
            var consumer = engine.subscribe(stream -> countItems(stream));

            // When
            engine.launch();

            // Then
            assertThat(consumer.getConsumeResult()).isEqualTo(1000);
        }
    }

    @Nested
    @DisplayName("Consumer Access Tests")
    class ConsumerAccessTests {

        @Test
        @DisplayName("Should provide access to consumers list")
        void testGetConsumers() {
            // Given
            var producer = new TestProducer(1, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var engine = new ProducerEngine<>(producer, produceFunction);

            // When
            var consumer1 = engine.subscribe(stream -> "result1");
            var consumer2 = engine.subscribe(stream -> "result2");
            var consumers = engine.getConsumers();

            // Then
            assertThat(consumers).hasSize(2);
            assertThat(consumers).containsExactly(consumer1, consumer2);
        }

        @Test
        @DisplayName("Should provide indexed access to consumers")
        void testGetConsumerByIndex() {
            // Given
            var producer = new TestProducer(1, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var engine = new ProducerEngine<>(producer, produceFunction);

            // When
            var consumer0 = engine.subscribe(stream -> "first");
            var consumer1 = engine.subscribe(stream -> "second");

            // Then
            assertThat(engine.getConsumer(0)).isSameAs(consumer0);
            assertThat(engine.getConsumer(1)).isSameAs(consumer1);
        }

        @Test
        @DisplayName("Should handle bounds checking for consumer access")
        void testConsumerIndexBounds() {
            // Given
            var producer = new TestProducer(1, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var engine = new ProducerEngine<>(producer, produceFunction);
            engine.subscribe(stream -> "only");

            // When/Then
            assertThatThrownBy(() -> engine.getConsumer(1))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("Different Consumer Types Tests")
    class DifferentConsumerTypesTests {

        @Test
        @DisplayName("Should handle different consumer result types")
        @Timeout(value = 10, unit = TimeUnit.SECONDS)
        void testDifferentResultTypes() {
            // Given
            var producer = new TestProducer(2, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var engine = new ProducerEngine<>(producer, produceFunction);

            var intConsumer = engine.subscribe(stream -> countItems(stream));
            var stringConsumer = engine.subscribe(stream -> concatenateItems(stream));
            var boolConsumer = engine.subscribe(stream -> countItems(stream) > 0);

            // When
            engine.launch();

            // Then
            assertThat(intConsumer.getConsumeResult()).isEqualTo(2);
            assertThat(stringConsumer.getConsumeResult()).isEqualTo("item0,item1,");
            assertThat(boolConsumer.getConsumeResult()).isEqualTo(true);
        }

        @Test
        @DisplayName("Should handle complex consumer logic")
        @Timeout(value = 10, unit = TimeUnit.SECONDS)
        void testComplexConsumerLogic() {
            // Given
            var producer = new TestProducer(5, "END");
            Function<TestProducer, String> produceFunction = TestProducer::nextItem;
            var engine = new ProducerEngine<>(producer, produceFunction);

            // Complex consumer: filter items and transform
            var filterConsumer = engine.subscribe(stream -> {
                var result = new ArrayList<String>();
                String item;
                while ((item = stream.next()) != null) {
                    if (item.contains("1") || item.contains("3")) {
                        result.add(item.toUpperCase());
                    }
                }
                return result;
            });

            // When
            engine.launch();

            // Then
            @SuppressWarnings("unchecked")
            List<String> result = (List<String>) filterConsumer.getConsumeResult();
            assertThat(result).containsExactly("ITEM1", "ITEM3");
        }
    }

    // Helper methods
    private Integer countItems(ObjectStream<String> stream) {
        int count = 0;
        while (stream.next() != null) {
            count++;
        }
        return count;
    }

    private String concatenateItems(ObjectStream<String> stream) {
        var builder = new StringBuilder();
        String item;
        while ((item = stream.next()) != null) {
            builder.append(item).append(",");
        }
        return builder.toString();
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
}
