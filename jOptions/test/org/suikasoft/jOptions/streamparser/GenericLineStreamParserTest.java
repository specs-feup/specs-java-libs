package org.suikasoft.jOptions.streamparser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import org.suikasoft.jOptions.DataStore.ADataClass;

import pt.up.fe.specs.util.utilities.LineStream;

/**
 * Comprehensive test suite for GenericLineStreamParser implementation class.
 * Tests the concrete implementation details, constructor behavior, state
 * management, worker initialization, exception handling, and lifecycle
 * management.
 * 
 * @author Generated Tests
 */
class GenericLineStreamParserTest {

    private GenericLineStreamParser<TestDataClass> parser;
    private TestDataClass testData;
    private Map<String, LineStreamWorker<TestDataClass>> workers;
    private TestLineStreamWorker testWorker;

    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() {
        testData = new TestDataClass();
        workers = new HashMap<>();
        testWorker = new TestLineStreamWorker("TEST_WORKER");
        workers.put("TEST_WORKER", testWorker);
        parser = new GenericLineStreamParser<>(testData, workers);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create parser with valid parameters")
        void testConstructorWithValidParameters() {
            TestDataClass data = new TestDataClass();
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            workers.put("WORKER1", new TestLineStreamWorker("WORKER1"));

            GenericLineStreamParser<TestDataClass> parser = new GenericLineStreamParser<>(data, workers);

            assertThat(parser).isNotNull();
            assertThat(parser.getData()).isSameAs(data);
            assertThat(parser.getIds()).hasSize(1);
            assertThat(parser.getIds()).contains("WORKER1");
        }

        @Test
        @DisplayName("Should initialize all workers during construction")
        void testWorkerInitialization() throws Exception {
            TestDataClass data = new TestDataClass();
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            TestLineStreamWorker worker1 = new TestLineStreamWorker("WORKER1");
            TestLineStreamWorker worker2 = new TestLineStreamWorker("WORKER2");
            workers.put("WORKER1", worker1);
            workers.put("WORKER2", worker2);

            try (GenericLineStreamParser<TestDataClass> parser = new GenericLineStreamParser<>(data, workers)) {
                // Workers should have been initialized (init() called)
                assertThat(worker1.wasInitialized()).isTrue();
                assertThat(worker2.wasInitialized()).isTrue();
            }
        }

        @Test
        @DisplayName("Should handle null data parameter")
        void testConstructorWithNullData() {
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();

            GenericLineStreamParser<TestDataClass> parser = new GenericLineStreamParser<>(null, workers);

            assertThat(parser).isNotNull();
            assertThat(parser.getData()).isNull();
        }

        @Test
        @DisplayName("Should handle null workers parameter")
        void testConstructorWithNullWorkers() {
            TestDataClass data = new TestDataClass();

            assertThatThrownBy(() -> new GenericLineStreamParser<>(data, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty workers map")
        void testConstructorWithEmptyWorkers() {
            TestDataClass data = new TestDataClass();
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();

            GenericLineStreamParser<TestDataClass> parser = new GenericLineStreamParser<>(data, workers);

            assertThat(parser).isNotNull();
            assertThat(parser.getIds()).isEmpty();
        }

        @Test
        @DisplayName("Should handle workers map with null values")
        void testConstructorWithNullWorkerValues() {
            TestDataClass data = new TestDataClass();
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            workers.put("VALID_WORKER", new TestLineStreamWorker("VALID"));
            workers.put("NULL_WORKER", null);

            assertThatThrownBy(() -> new GenericLineStreamParser<>(data, workers))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("State Management Tests")
    class StateManagementTests {

        @Test
        @DisplayName("Should maintain initial state correctly")
        void testInitialState() {
            assertThat(parser.getData()).isSameAs(testData);
            assertThat(parser.getNumExceptions()).isEqualTo(0);
            assertThat(parser.getLineIgnore()).isNotNull();
            assertThat(parser.getLineIgnore().test("any line")).isFalse();
        }

        @Test
        @DisplayName("Should track current line stream state")
        void testCurrentLineStreamState() {
            // Initially, accessing statistics should throw NPE
            assertThatThrownBy(() -> parser.getReadLines())
                    .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> parser.getReadChars())
                    .isInstanceOf(NullPointerException.class);

            // After parsing, currentLineStream should be set
            LineStream lineStream = LineStream.newInstance("test line");
            parser.parse("TEST_WORKER", lineStream);

            // Now statistics should be accessible
            assertThat(parser.getReadLines()).isGreaterThanOrEqualTo(0);
            assertThat(parser.getReadChars()).isGreaterThanOrEqualTo(0);
        }

        @Test
        @DisplayName("Should update exception count on worker failures")
        void testExceptionCountTracking() throws Exception {
            TestLineStreamWorker errorWorker = new TestLineStreamWorker("ERROR_WORKER") {
                @Override
                public void apply(LineStream lineStream, TestDataClass data) {
                    throw new RuntimeException("Test exception");
                }
            };

            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            workers.put("ERROR_WORKER", errorWorker);

            try (GenericLineStreamParser<TestDataClass> parser = new GenericLineStreamParser<>(testData, workers)) {
                assertThat(parser.getNumExceptions()).isEqualTo(0);

                LineStream lineStream = LineStream.newInstance("test line");

                assertThatThrownBy(() -> parser.parse("ERROR_WORKER", lineStream))
                        .isInstanceOf(RuntimeException.class);

                assertThat(parser.getNumExceptions()).isEqualTo(1);
            }
        }

        @Test
        @DisplayName("Should maintain data consistency across operations")
        void testDataConsistency() {
            TestDataClass initialData = parser.getData();

            // Perform various operations
            parser.setLineIgnore(line -> line.startsWith("#"));
            LineStream lineStream = LineStream.newInstance("test line");
            parser.parse("TEST_WORKER", lineStream);

            // Data reference should remain the same
            assertThat(parser.getData()).isSameAs(initialData);
        }
    }

    @Nested
    @DisplayName("Line Ignore Predicate Tests")
    class LineIgnorePredicateTests {

        @Test
        @DisplayName("Should return default predicate initially")
        void testDefaultLineIgnorePredicate() {
            Predicate<String> predicate = parser.getLineIgnore();

            assertThat(predicate).isNotNull();
            assertThat(predicate.test("any line")).isFalse();
            assertThat(predicate.test("")).isFalse();
            assertThat(predicate.test(null)).isFalse();
        }

        @Test
        @DisplayName("Should set and return custom predicate")
        void testCustomLineIgnorePredicate() {
            Predicate<String> customPredicate = line -> line != null && line.startsWith("IGNORE:");

            parser.setLineIgnore(customPredicate);

            assertThat(parser.getLineIgnore()).isSameAs(customPredicate);
            assertThat(parser.getLineIgnore().test("IGNORE: this line")).isTrue();
            assertThat(parser.getLineIgnore().test("KEEP: this line")).isFalse();
        }

        @Test
        @DisplayName("Should handle null predicate")
        void testNullLineIgnorePredicate() {
            parser.setLineIgnore(null);

            Predicate<String> predicate = parser.getLineIgnore();
            assertThat(predicate).isNotNull();
            assertThat(predicate.test("any line")).isFalse();
        }

        @Test
        @DisplayName("Should replace existing predicate")
        void testReplaceLineIgnorePredicate() {
            Predicate<String> firstPredicate = line -> line.startsWith("FIRST:");
            Predicate<String> secondPredicate = line -> line.startsWith("SECOND:");

            parser.setLineIgnore(firstPredicate);
            assertThat(parser.getLineIgnore()).isSameAs(firstPredicate);

            parser.setLineIgnore(secondPredicate);
            assertThat(parser.getLineIgnore()).isSameAs(secondPredicate);
            assertThat(parser.getLineIgnore().test("SECOND: test")).isTrue();
            assertThat(parser.getLineIgnore().test("FIRST: test")).isFalse();
        }
    }

    @Nested
    @DisplayName("Worker Management Tests")
    class WorkerManagementTests {

        @Test
        @DisplayName("Should return worker IDs correctly")
        void testGetIds() {
            assertThat(parser.getIds()).hasSize(1);
            assertThat(parser.getIds()).contains("TEST_WORKER");
        }

        @Test
        @DisplayName("Should parse with valid worker")
        void testParseWithValidWorker() {
            LineStream lineStream = LineStream.newInstance("test data");

            boolean result = parser.parse("TEST_WORKER", lineStream);

            assertThat(result).isTrue();
            assertThat(testWorker.wasCalled()).isTrue();
        }

        @Test
        @DisplayName("Should fail to parse with invalid worker")
        void testParseWithInvalidWorker() {
            LineStream lineStream = LineStream.newInstance("test data");

            boolean result = parser.parse("INVALID_WORKER", lineStream);

            assertThat(result).isFalse();
            assertThat(testWorker.wasCalled()).isFalse();
        }

        @Test
        @DisplayName("Should handle null worker ID")
        void testParseWithNullWorkerId() {
            LineStream lineStream = LineStream.newInstance("test data");

            boolean result = parser.parse(null, lineStream);

            assertThat(result).isFalse();
            assertThat(testWorker.wasCalled()).isFalse();
        }

        @Test
        @DisplayName("Should pass line stream and data to worker")
        void testWorkerParameters() {
            LineStream lineStream = LineStream.newInstance("test data");
            TestDataClass originalData = parser.getData();

            parser.parse("TEST_WORKER", lineStream);

            assertThat(testWorker.wasCalled()).isTrue();
            assertThat(testWorker.getLastProcessedData()).isSameAs(originalData);
        }
    }

    @Nested
    @DisplayName("Exception Handling Tests")
    class ExceptionHandlingTests {

        @Test
        @DisplayName("Should propagate worker exceptions")
        void testWorkerExceptionPropagation() throws Exception {
            TestLineStreamWorker errorWorker = new TestLineStreamWorker("ERROR_WORKER") {
                @Override
                public void apply(LineStream lineStream, TestDataClass data) {
                    throw new RuntimeException("Worker failed");
                }
            };

            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            workers.put("ERROR_WORKER", errorWorker);

            try (GenericLineStreamParser<TestDataClass> parser = new GenericLineStreamParser<>(testData, workers)) {
                LineStream lineStream = LineStream.newInstance("test data");

                assertThatThrownBy(() -> parser.parse("ERROR_WORKER", lineStream))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessage("Worker failed");
            }
        }

        @Test
        @DisplayName("Should increment exception count before re-throwing")
        void testExceptionCountIncrement() throws Exception {
            TestLineStreamWorker errorWorker = new TestLineStreamWorker("ERROR_WORKER") {
                @Override
                public void apply(LineStream lineStream, TestDataClass data) {
                    throw new IllegalStateException("Test error");
                }
            };

            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            workers.put("ERROR_WORKER", errorWorker);

            try (GenericLineStreamParser<TestDataClass> parser = new GenericLineStreamParser<>(testData, workers)) {
                LineStream lineStream = LineStream.newInstance("test data");

                assertThat(parser.getNumExceptions()).isEqualTo(0);

                assertThatThrownBy(() -> parser.parse("ERROR_WORKER", lineStream))
                        .isInstanceOf(IllegalStateException.class);

                assertThat(parser.getNumExceptions()).isEqualTo(1);
            }
        }

        @Test
        @DisplayName("Should accumulate exception count across multiple failures")
        void testMultipleExceptionCount() throws Exception {
            TestLineStreamWorker errorWorker = new TestLineStreamWorker("ERROR_WORKER") {
                @Override
                public void apply(LineStream lineStream, TestDataClass data) {
                    throw new RuntimeException("Error");
                }
            };

            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            workers.put("ERROR_WORKER", errorWorker);

            try (GenericLineStreamParser<TestDataClass> parser = new GenericLineStreamParser<>(testData, workers)) {
                LineStream lineStream1 = LineStream.newInstance("test data 1");
                LineStream lineStream2 = LineStream.newInstance("test data 2");

                assertThatThrownBy(() -> parser.parse("ERROR_WORKER", lineStream1))
                        .isInstanceOf(RuntimeException.class);
                assertThatThrownBy(() -> parser.parse("ERROR_WORKER", lineStream2))
                        .isInstanceOf(RuntimeException.class);

                assertThat(parser.getNumExceptions()).isEqualTo(2);
            }
        }
    }

    @Nested
    @DisplayName("Lifecycle Management Tests")
    class LifecycleManagementTests {

        @Test
        @DisplayName("Should close all workers")
        void testCloseAllWorkers() throws Exception {
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            TestLineStreamWorker worker1 = new TestLineStreamWorker("WORKER1");
            TestLineStreamWorker worker2 = new TestLineStreamWorker("WORKER2");
            workers.put("WORKER1", worker1);
            workers.put("WORKER2", worker2);

            GenericLineStreamParser<TestDataClass> parser = new GenericLineStreamParser<>(testData, workers);

            parser.close();

            assertThat(worker1.wasClosed()).isTrue();
            assertThat(worker2.wasClosed()).isTrue();
        }

        @Test
        @DisplayName("Should handle close with no workers")
        void testCloseWithNoWorkers() throws Exception {
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            GenericLineStreamParser<TestDataClass> parser = new GenericLineStreamParser<>(testData, workers);

            // Should not throw exception
            parser.close();
        }

        @Test
        @DisplayName("Should pass data to worker close methods")
        void testCloseWithData() throws Exception {
            TestLineStreamWorker worker = new TestLineStreamWorker("WORKER");
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            workers.put("WORKER", worker);

            GenericLineStreamParser<TestDataClass> parser = new GenericLineStreamParser<>(testData, workers);

            parser.close();

            assertThat(worker.wasClosed()).isTrue();
            assertThat(worker.getCloseData()).isSameAs(testData);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete parsing workflow")
        void testCompleteWorkflow() {
            String input = "TEST_WORKER\ndata line\n";
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            File dumpFile = new File(tempDir, "dump.txt");

            String unparsedLines = parser.parse(inputStream, dumpFile);

            assertThat(unparsedLines).isEmpty();
            assertThat(testWorker.wasCalled()).isTrue();
            assertThat(parser.getNumExceptions()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should maintain state consistency during parsing")
        void testStateDuringParsing() {
            LineStream lineStream = LineStream.newInstance("test data");
            TestDataClass originalData = parser.getData();

            parser.parse("TEST_WORKER", lineStream);

            // State should be consistent
            assertThat(parser.getData()).isSameAs(originalData);
            assertThat(parser.getIds()).contains("TEST_WORKER");
            assertThat(parser.getNumExceptions()).isEqualTo(0);
        }

        @Test
        @DisplayName("Should work with multiple workers in sequence")
        void testMultipleWorkersSequence() throws Exception {
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            TestLineStreamWorker worker1 = new TestLineStreamWorker("WORKER1");
            TestLineStreamWorker worker2 = new TestLineStreamWorker("WORKER2");
            workers.put("WORKER1", worker1);
            workers.put("WORKER2", worker2);

            try (GenericLineStreamParser<TestDataClass> parser = new GenericLineStreamParser<>(testData, workers)) {
                LineStream lineStream1 = LineStream.newInstance("data1");
                LineStream lineStream2 = LineStream.newInstance("data2");

                boolean result1 = parser.parse("WORKER1", lineStream1);
                boolean result2 = parser.parse("WORKER2", lineStream2);

                assertThat(result1).isTrue();
                assertThat(result2).isTrue();
                assertThat(worker1.wasCalled()).isTrue();
                assertThat(worker2.wasCalled()).isTrue();
            }
        }
    }

    // Test helper classes
    private static class TestDataClass extends ADataClass<TestDataClass> {
        private String value = "";

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    private static class TestLineStreamWorker implements LineStreamWorker<TestDataClass> {
        private final String id;
        private boolean called = false;
        private boolean initialized = false;
        private boolean closed = false;
        private TestDataClass lastProcessedData;
        private TestDataClass closeData;

        public TestLineStreamWorker(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void init(TestDataClass data) {
            initialized = true;
            lastProcessedData = data;
        }

        @Override
        public void apply(LineStream lineStream, TestDataClass data) {
            called = true;
            lastProcessedData = data;
            // Consume one line as typical worker behavior
            if (lineStream.hasNextLine()) {
                String line = lineStream.nextLine();
                data.setValue(data.getValue() + line + ";");
            }
        }

        @Override
        public void close(TestDataClass data) {
            closed = true;
            closeData = data;
        }

        public boolean wasCalled() {
            return called;
        }

        public boolean wasInitialized() {
            return initialized;
        }

        public boolean wasClosed() {
            return closed;
        }

        public TestDataClass getLastProcessedData() {
            return lastProcessedData;
        }

        public TestDataClass getCloseData() {
            return closeData;
        }
    }
}
