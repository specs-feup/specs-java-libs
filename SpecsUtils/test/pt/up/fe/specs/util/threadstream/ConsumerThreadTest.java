package pt.up.fe.specs.util.threadstream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * Comprehensive test suite for the ConsumerThread class.
 * Tests consumer thread functionality and stream consumption.
 * 
 * @author Generated Tests
 */
@DisplayName("ConsumerThread Tests")
public class ConsumerThreadTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create consumer thread with consume function")
        void testConstructorWithFunction() {
            // Given
            Function<ObjectStream<String>, Integer> consumeFunction = stream -> 42;

            // When
            var consumerThread = new TestableConsumerThread<>(consumeFunction);

            // Then
            assertThat(consumerThread).isNotNull();
            assertThat(consumerThread).isInstanceOf(Runnable.class);
        }

        @Test
        @DisplayName("Should create consumer thread with lambda function")
        void testConstructorWithLambda() {
            // When
            var consumerThread = new TestableConsumerThread<String, String>(stream -> "result");

            // Then
            assertThat(consumerThread).isNotNull();
        }
    }

    @Nested
    @DisplayName("Stream Provision Tests")
    class StreamProvisionTests {

        @Test
        @DisplayName("Should accept provided stream")
        void testProvideStream() {
            // Given
            var consumerThread = new TestableConsumerThread<String, Integer>(stream -> 0);
            @SuppressWarnings("unchecked")
            ObjectStream<String> mockStream = mock(ObjectStream.class);

            // When
            consumerThread.provide(mockStream);

            // Then
            assertThat(consumerThread.getOstream()).isSameAs(mockStream);
        }

        @Test
        @DisplayName("Should allow stream replacement")
        void testReplaceStream() {
            // Given
            var consumerThread = new TestableConsumerThread<String, Integer>(stream -> 0);
            @SuppressWarnings("unchecked")
            ObjectStream<String> mockStream1 = mock(ObjectStream.class);
            @SuppressWarnings("unchecked")
            ObjectStream<String> mockStream2 = mock(ObjectStream.class);

            // When
            consumerThread.provide(mockStream1);
            consumerThread.provide(mockStream2);

            // Then
            assertThat(consumerThread.getOstream()).isSameAs(mockStream2);
        }
    }

    @Nested
    @DisplayName("Consumption Tests")
    class ConsumptionTests {

        @Test
        @DisplayName("Should consume stream and return result")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testConsumeStream() {
            // Given
            Function<ObjectStream<String>, Integer> consumeFunction = stream -> {
                int count = 0;
                while (stream.next() != null) {
                    count++;
                }
                return count;
            };

            var consumerThread = new TestableConsumerThread<>(consumeFunction);
            var mockStream = createMockStreamWithItems("item1", "item2", "item3");
            consumerThread.provide(mockStream);

            // When
            var thread = new Thread(consumerThread);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Thread was interrupted");
            }

            // Then
            assertThat(consumerThread.getConsumeResult()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should handle empty stream")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testConsumeEmptyStream() {
            // Given
            Function<ObjectStream<String>, String> consumeFunction = stream -> {
                var first = stream.next();
                return first != null ? first : "empty";
            };

            var consumerThread = new TestableConsumerThread<>(consumeFunction);
            var mockStream = createMockStreamWithItems(); // Empty stream
            consumerThread.provide(mockStream);

            // When
            var thread = new Thread(consumerThread);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Thread was interrupted");
            }

            // Then
            assertThat(consumerThread.getConsumeResult()).isEqualTo("empty");
        }

        @Test
        @DisplayName("Should process stream items correctly")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testProcessStreamItems() {
            // Given
            Function<ObjectStream<String>, String> consumeFunction = stream -> {
                var builder = new StringBuilder();
                String item;
                while ((item = stream.next()) != null) {
                    builder.append(item).append(",");
                }
                return builder.toString();
            };

            var consumerThread = new TestableConsumerThread<>(consumeFunction);
            var mockStream = createMockStreamWithItems("a", "b", "c");
            consumerThread.provide(mockStream);

            // When
            var thread = new Thread(consumerThread);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Thread was interrupted");
            }

            // Then
            assertThat(consumerThread.getConsumeResult()).isEqualTo("a,b,c,");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle missing stream without propagating exception")
        void testMissingStream() {
            // Given
            var consumerThread = new TestableConsumerThread<String, Integer>(stream -> 0);

            // When - run without providing stream
            var thread = new Thread(consumerThread);
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Thread was interrupted");
            }

            // Then - thread should terminate (exception doesn't propagate to calling
            // thread)
            assertThat(thread.isAlive()).isFalse();
            // Consumer result should be null since exception occurred
            assertThat(consumerThread.getConsumeResult()).isNull();
        }

        @Test
        @DisplayName("Should handle consume function exceptions without propagation")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testConsumeFunctionException() {
            // Given
            Function<ObjectStream<String>, String> consumeFunction = stream -> {
                throw new RuntimeException("Consumption failed");
            };

            var consumerThread = new TestableConsumerThread<>(consumeFunction);
            var mockStream = createMockStreamWithItems("item");
            consumerThread.provide(mockStream);

            // When
            var thread = new Thread(consumerThread);
            thread.start();

            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                fail("Thread was interrupted");
            }

            // Then - thread should terminate (exception doesn't propagate to calling
            // thread)
            assertThat(thread.isAlive()).isFalse();
            // Consumer result should be null since exception occurred
            assertThat(consumerThread.getConsumeResult()).isNull();
        }
    }

    @Nested
    @DisplayName("Result Access Tests")
    class ResultAccessTests {

        @Test
        @DisplayName("Should return null result before execution")
        void testResultBeforeExecution() {
            // Given
            var consumerThread = new TestableConsumerThread<String, Integer>(stream -> 42);

            // When/Then
            assertThat(consumerThread.getConsumeResult()).isNull();
        }

        @Test
        @DisplayName("Should return correct result after execution")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testResultAfterExecution() throws InterruptedException {
            // Given
            var consumerThread = new TestableConsumerThread<String, String>(stream -> "completed");
            var mockStream = createMockStreamWithItems();
            consumerThread.provide(mockStream);

            // When
            var thread = new Thread(consumerThread);
            thread.start();
            thread.join();

            // Then
            assertThat(consumerThread.getConsumeResult()).isEqualTo("completed");
        }

        @Test
        @DisplayName("Should handle different result types")
        @Timeout(value = 5, unit = TimeUnit.SECONDS)
        void testDifferentResultTypes() throws InterruptedException {
            // Given - Integer result
            var intConsumer = new TestableConsumerThread<String, Integer>(stream -> 123);
            var mockStream1 = createMockStreamWithItems();
            intConsumer.provide(mockStream1);

            // Given - Boolean result
            var boolConsumer = new TestableConsumerThread<String, Boolean>(stream -> true);
            var mockStream2 = createMockStreamWithItems();
            boolConsumer.provide(mockStream2);

            // When
            var thread1 = new Thread(intConsumer);
            var thread2 = new Thread(boolConsumer);
            thread1.start();
            thread2.start();
            thread1.join();
            thread2.join();

            // Then
            assertThat(intConsumer.getConsumeResult()).isEqualTo(123);
            assertThat(boolConsumer.getConsumeResult()).isEqualTo(true);
        }
    }

    // Helper methods
    @SuppressWarnings("unchecked")
    private ObjectStream<String> createMockStreamWithItems(String... items) {
        var mockStream = mock(ObjectStream.class);

        if (items.length == 0) {
            when(mockStream.next()).thenReturn(null);
        } else {
            var firstCall = when(mockStream.next()).thenReturn(items[0]);
            for (int i = 1; i < items.length; i++) {
                firstCall = firstCall.thenReturn(items[i]);
            }
            firstCall.thenReturn(null);
        }

        return mockStream;
    }

    // Testable version that exposes protected methods
    private static class TestableConsumerThread<T, K> extends ConsumerThread<T, K> {

        public TestableConsumerThread(Function<ObjectStream<T>, K> consumeFunction) {
            super(consumeFunction);
        }

        @Override
        public void provide(ObjectStream<T> ostream) {
            super.provide(ostream);
        }

        @Override
        public ObjectStream<T> getOstream() {
            return super.getOstream();
        }

        @Override
        public K getConsumeResult() {
            return super.getConsumeResult();
        }
    }
}
