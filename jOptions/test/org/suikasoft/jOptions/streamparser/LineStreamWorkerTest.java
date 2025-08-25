package org.suikasoft.jOptions.streamparser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.suikasoft.jOptions.DataStore.ADataClass;

import pt.up.fe.specs.util.utilities.LineStream;

/**
 * Comprehensive test suite for LineStreamWorker interface.
 * Tests factory methods, interface contract compliance, default
 * implementations, worker lifecycle, error handling, and integration scenarios.
 * 
 * @author Generated Tests
 */
class LineStreamWorkerTest {

    private TestDataClass testData;

    @BeforeEach
    void setUp() {
        testData = new TestDataClass();
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create worker with id, init, and apply")
        void testNewInstanceWithInitAndApply() {
            String workerId = "TEST_WORKER";
            AtomicBoolean initCalled = new AtomicBoolean(false);
            AtomicBoolean applyCalled = new AtomicBoolean(false);

            Consumer<TestDataClass> init = data -> {
                initCalled.set(true);
                data.setValue("initialized");
            };

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                applyCalled.set(true);
                if (stream.hasNextLine()) {
                    data.setValue(data.getValue() + ":" + stream.nextLine());
                }
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance(workerId, init, apply);

            assertThat(worker).isNotNull();
            assertThat(worker.getId()).isEqualTo(workerId);

            // Test init
            worker.init(testData);
            assertThat(initCalled.get()).isTrue();
            assertThat(testData.getValue()).isEqualTo("initialized");

            // Test apply
            LineStream stream = LineStream.newInstance("test");
            worker.apply(stream, testData);
            assertThat(applyCalled.get()).isTrue();
            assertThat(testData.getValue()).isEqualTo("initialized:test");
        }

        @Test
        @DisplayName("Should create worker with id and apply (no init)")
        void testNewInstanceWithApplyOnly() {
            String workerId = "SIMPLE_WORKER";
            AtomicBoolean applyCalled = new AtomicBoolean(false);

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                applyCalled.set(true);
                if (stream.hasNextLine()) {
                    data.setValue(stream.nextLine().toUpperCase());
                }
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance(workerId, apply);

            assertThat(worker).isNotNull();
            assertThat(worker.getId()).isEqualTo(workerId);

            // Test init (should do nothing)
            String originalValue = testData.getValue();
            worker.init(testData);
            assertThat(testData.getValue()).isEqualTo(originalValue);

            // Test apply
            LineStream stream = LineStream.newInstance("hello");
            worker.apply(stream, testData);
            assertThat(applyCalled.get()).isTrue();
            assertThat(testData.getValue()).isEqualTo("HELLO");
        }

        @Test
        @DisplayName("Should handle null parameters in factory methods")
        void testFactoryMethodsWithNullParameters() {
            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
            };

            // Null ID should be handled by implementation
            LineStreamWorker<TestDataClass> worker1 = LineStreamWorker.newInstance(null, apply);
            assertThat(worker1).isNotNull();
            assertThat(worker1.getId()).isNull();

            // Null apply function - should be handled by implementation
            LineStreamWorker<TestDataClass> worker2 = LineStreamWorker.newInstance("ID", null, apply);
            assertThat(worker2).isNotNull();

            // Null init function - should be handled by implementation
            Consumer<TestDataClass> init = data -> {
            };
            LineStreamWorker<TestDataClass> worker3 = LineStreamWorker.newInstance("ID", init, null);
            assertThat(worker3).isNotNull();
        }

        @Test
        @DisplayName("Should create multiple independent workers")
        void testMultipleIndependentWorkers() {
            AtomicInteger counter1 = new AtomicInteger(0);
            AtomicInteger counter2 = new AtomicInteger(0);

            LineStreamWorker<TestDataClass> worker1 = LineStreamWorker.newInstance("WORKER1",
                    (stream, data) -> counter1.incrementAndGet());

            LineStreamWorker<TestDataClass> worker2 = LineStreamWorker.newInstance("WORKER2",
                    (stream, data) -> counter2.incrementAndGet());

            assertThat(worker1.getId()).isEqualTo("WORKER1");
            assertThat(worker2.getId()).isEqualTo("WORKER2");

            LineStream stream = LineStream.newInstance("test");

            worker1.apply(stream, testData);
            assertThat(counter1.get()).isEqualTo(1);
            assertThat(counter2.get()).isEqualTo(0);

            worker2.apply(stream, testData);
            assertThat(counter1.get()).isEqualTo(1);
            assertThat(counter2.get()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Worker Lifecycle Tests")
    class WorkerLifecycleTests {

        @Test
        @DisplayName("Should follow complete lifecycle: init -> apply -> close")
        void testCompleteLifecycle() {
            StringBuilder lifecycle = new StringBuilder();

            Consumer<TestDataClass> init = data -> {
                lifecycle.append("init:");
                data.setValue("init");
            };

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                lifecycle.append("apply:");
                data.setValue(data.getValue() + ":apply");
            };

            LineStreamWorker<TestDataClass> worker = new LineStreamWorker<TestDataClass>() {
                @Override
                public String getId() {
                    return "LIFECYCLE_WORKER";
                }

                @Override
                public void init(TestDataClass data) {
                    init.accept(data);
                }

                @Override
                public void apply(LineStream lineStream, TestDataClass data) {
                    apply.accept(lineStream, data);
                }

                @Override
                public void close(TestDataClass data) {
                    lifecycle.append("close");
                    data.setValue(data.getValue() + ":close");
                }
            };

            // Execute lifecycle
            worker.init(testData);
            LineStream stream = LineStream.newInstance("data");
            worker.apply(stream, testData);
            worker.close(testData);

            assertThat(lifecycle.toString()).isEqualTo("init:apply:close");
            assertThat(testData.getValue()).isEqualTo("init:apply:close");
        }

        @Test
        @DisplayName("Should handle multiple init calls")
        void testMultipleInitCalls() {
            AtomicInteger initCount = new AtomicInteger(0);

            Consumer<TestDataClass> init = data -> {
                initCount.incrementAndGet();
                data.setValue("init-" + initCount.get());
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("MULTI_INIT", init,
                    (stream, data) -> {
                    });

            worker.init(testData);
            assertThat(initCount.get()).isEqualTo(1);
            assertThat(testData.getValue()).isEqualTo("init-1");

            worker.init(testData);
            assertThat(initCount.get()).isEqualTo(2);
            assertThat(testData.getValue()).isEqualTo("init-2");
        }

        @Test
        @DisplayName("Should handle multiple apply calls")
        void testMultipleApplyCalls() {
            AtomicInteger applyCount = new AtomicInteger(0);

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                int count = applyCount.incrementAndGet();
                if (stream.hasNextLine()) {
                    data.setValue(data.getValue() + count + ":" + stream.nextLine() + ";");
                }
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("MULTI_APPLY", apply);

            worker.apply(LineStream.newInstance("first"), testData);
            assertThat(testData.getValue()).isEqualTo("1:first;");

            worker.apply(LineStream.newInstance("second"), testData);
            assertThat(testData.getValue()).isEqualTo("1:first;2:second;");
        }

        @Test
        @DisplayName("Should handle default close implementation")
        void testDefaultCloseImplementation() {
            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("DEFAULT_CLOSE",
                    (stream, data) -> data.setValue("applied"));

            String originalValue = "original";
            testData.setValue(originalValue);

            // Default close should do nothing
            worker.close(testData);
            assertThat(testData.getValue()).isEqualTo(originalValue);
        }
    }

    @Nested
    @DisplayName("Stream Processing Tests")
    class StreamProcessingTests {

        @Test
        @DisplayName("Should process single line from stream")
        void testSingleLineProcessing() {
            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                if (stream.hasNextLine()) {
                    data.setValue(stream.nextLine());
                }
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("SINGLE_LINE", apply);
            LineStream stream = LineStream.newInstance("hello world");

            worker.apply(stream, testData);

            assertThat(testData.getValue()).isEqualTo("hello world");
        }

        @Test
        @DisplayName("Should process multiple lines from stream")
        void testMultiLineProcessing() {
            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                StringBuilder content = new StringBuilder();
                while (stream.hasNextLine()) {
                    if (content.length() > 0)
                        content.append("|");
                    content.append(stream.nextLine());
                }
                data.setValue(content.toString());
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("MULTI_LINE", apply);
            LineStream stream = LineStream.newInstance("line1\nline2\nline3\n");

            worker.apply(stream, testData);

            assertThat(testData.getValue()).isEqualTo("line1|line2|line3");
        }

        @Test
        @DisplayName("Should handle empty stream")
        void testEmptyStreamProcessing() {
            AtomicBoolean applyCalled = new AtomicBoolean(false);

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                applyCalled.set(true);
                if (stream.hasNextLine()) {
                    data.setValue(stream.nextLine());
                } else {
                    data.setValue("empty");
                }
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("EMPTY_STREAM", apply);
            LineStream stream = LineStream.newInstance("");

            worker.apply(stream, testData);

            assertThat(applyCalled.get()).isTrue();
            assertThat(testData.getValue()).isEqualTo("empty");
        }

        @Test
        @DisplayName("Should handle stream consumption patterns")
        void testStreamConsumptionPatterns() {
            // Worker that consumes exactly one line
            BiConsumer<LineStream, TestDataClass> oneLineConsumer = (stream, data) -> {
                if (stream.hasNextLine()) {
                    data.setValue(stream.nextLine());
                }
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("ONE_LINE", oneLineConsumer);
            LineStream stream = LineStream.newInstance("first\nsecond\nthird\n");

            worker.apply(stream, testData);

            assertThat(testData.getValue()).isEqualTo("first");
            // Stream should still have remaining lines
            assertThat(stream.hasNextLine()).isTrue();
            assertThat(stream.nextLine()).isEqualTo("second");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should propagate exceptions from init")
        void testInitExceptionPropagation() {
            Consumer<TestDataClass> failingInit = data -> {
                throw new RuntimeException("Init failed");
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("FAILING_INIT",
                    failingInit, (stream, data) -> {
                    });

            assertThatThrownBy(() -> worker.init(testData))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Init failed");
        }

        @Test
        @DisplayName("Should propagate exceptions from apply")
        void testApplyExceptionPropagation() {
            BiConsumer<LineStream, TestDataClass> failingApply = (stream, data) -> {
                throw new IllegalStateException("Apply failed");
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("FAILING_APPLY", failingApply);
            LineStream stream = LineStream.newInstance("test");

            assertThatThrownBy(() -> worker.apply(stream, testData))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Apply failed");
        }

        @Test
        @DisplayName("Should handle null data gracefully")
        void testNullDataHandling() {
            AtomicBoolean initCalled = new AtomicBoolean(false);
            AtomicBoolean applyCalled = new AtomicBoolean(false);

            Consumer<TestDataClass> init = data -> {
                initCalled.set(true);
                // Should handle null data gracefully in implementation
            };

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                applyCalled.set(true);
                // Should handle null data gracefully in implementation
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("NULL_DATA", init, apply);

            // Test with null data - behavior depends on implementation
            worker.init(null);
            assertThat(initCalled.get()).isTrue();

            LineStream stream = LineStream.newInstance("test");
            worker.apply(stream, null);
            assertThat(applyCalled.get()).isTrue();
        }

        @Test
        @DisplayName("Should handle null stream gracefully")
        void testNullStreamHandling() {
            AtomicBoolean applyCalled = new AtomicBoolean(false);

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                applyCalled.set(true);
                // Implementation should handle null stream
                if (stream != null && stream.hasNextLine()) {
                    data.setValue(stream.nextLine());
                } else {
                    data.setValue("null_stream");
                }
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("NULL_STREAM", apply);

            worker.apply(null, testData);
            assertThat(applyCalled.get()).isTrue();
            assertThat(testData.getValue()).isEqualTo("null_stream");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work as component in larger system")
        void testWorkerAsSystemComponent() {
            // Simulate a worker that parses configuration
            BiConsumer<LineStream, TestDataClass> configParser = (stream, data) -> {
                StringBuilder config = new StringBuilder();
                while (stream.hasNextLine()) {
                    String line = stream.nextLine();
                    if (line.startsWith("config.")) {
                        config.append(line).append(";");
                    }
                }
                data.setValue(config.toString());
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("CONFIG_PARSER", configParser);

            String input = "comment line\n" +
                    "config.name=test\n" +
                    "config.value=123\n" +
                    "another comment\n" +
                    "config.enabled=true\n";

            LineStream stream = LineStream.newInstance(input);
            worker.apply(stream, testData);

            assertThat(testData.getValue()).isEqualTo("config.name=test;config.value=123;config.enabled=true;");
        }

        @Test
        @DisplayName("Should maintain state across multiple operations")
        void testStatefulWorker() {
            // Worker that accumulates data across multiple calls
            AtomicInteger lineCount = new AtomicInteger(0);

            Consumer<TestDataClass> init = data -> {
                data.setValue("Lines processed: ");
                lineCount.set(0);
            };

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                while (stream.hasNextLine()) {
                    stream.nextLine();
                    lineCount.incrementAndGet();
                }
                data.setValue("Lines processed: " + lineCount.get());
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("STATEFUL", init, apply);

            worker.init(testData);
            assertThat(testData.getValue()).isEqualTo("Lines processed: ");

            worker.apply(LineStream.newInstance("line1\nline2\n"), testData);
            assertThat(testData.getValue()).isEqualTo("Lines processed: 2");

            worker.apply(LineStream.newInstance("line3\nline4\nline5\n"), testData);
            assertThat(testData.getValue()).isEqualTo("Lines processed: 5");
        }

        @Test
        @DisplayName("Should support complex data processing workflows")
        void testComplexDataProcessing() {
            // Multi-stage processing worker
            Consumer<TestDataClass> init = data -> data.setValue("");

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                StringBuilder result = new StringBuilder();

                // Stage 1: Read header
                if (stream.hasNextLine()) {
                    String header = stream.nextLine();
                    result.append("Header: ").append(header.toUpperCase()).append("; ");
                }

                // Stage 2: Process data lines
                int dataCount = 0;
                while (stream.hasNextLine()) {
                    String line = stream.nextLine();
                    if (!line.trim().isEmpty()) {
                        dataCount++;
                    }
                }
                result.append("Data lines: ").append(dataCount);

                data.setValue(result.toString());
            };

            LineStreamWorker<TestDataClass> worker = LineStreamWorker.newInstance("COMPLEX", init, apply);

            String input = "My Header\n" +
                    "data line 1\n" +
                    "\n" + // empty line
                    "data line 2\n" +
                    "data line 3\n";

            worker.init(testData);
            worker.apply(LineStream.newInstance(input), testData);

            assertThat(testData.getValue()).isEqualTo("Header: MY HEADER; Data lines: 3");
        }
    }

    // Test helper class
    private static class TestDataClass extends ADataClass<TestDataClass> {
        private String value = "";

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
