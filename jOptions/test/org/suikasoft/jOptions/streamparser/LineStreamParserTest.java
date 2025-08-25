package org.suikasoft.jOptions.streamparser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Collection;
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
 * Comprehensive test suite for LineStreamParser interface.
 * Tests static factory methods, default implementations, worker management,
 * parsing functionality, error handling, and interface contracts.
 * 
 * @author Generated Tests
 */
class LineStreamParserTest {

    private LineStreamParser<TestDataClass> parser;
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
        parser = LineStreamParser.newInstance(testData, workers);
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {

        @Test
        @DisplayName("Should create parser instance from newInstance")
        void testNewInstance() {
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            TestDataClass data = new TestDataClass();

            LineStreamParser<TestDataClass> parser = LineStreamParser.newInstance(data, workers);

            assertThat(parser).isNotNull();
            assertThat(parser.getData()).isNotNull();
            assertThat(parser.getIds()).isEmpty();
        }

        @Test
        @DisplayName("Should create parser with workers")
        void testNewInstanceWithWorkers() {
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            TestLineStreamWorker worker = new TestLineStreamWorker("WORKER1");
            workers.put("WORKER1", worker);
            workers.put("WORKER2", new TestLineStreamWorker("WORKER2"));
            TestDataClass data = new TestDataClass();

            LineStreamParser<TestDataClass> parser = LineStreamParser.newInstance(data, workers);

            assertThat(parser).isNotNull();
            assertThat(parser.getIds()).hasSize(2);
            assertThat(parser.getIds()).contains("WORKER1", "WORKER2");
        }

        @Test
        @DisplayName("Should handle null data")
        void testNewInstanceWithNullData() {
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();

            // The implementation doesn't validate null data, so this should work
            LineStreamParser<TestDataClass> parser = LineStreamParser.newInstance(null, workers);
            assertThat(parser).isNotNull();
            assertThat(parser.getData()).isNull();
        }

        @Test
        @DisplayName("Should handle null workers map")
        void testNewInstanceWithNullWorkers() {
            TestDataClass data = new TestDataClass();

            assertThatThrownBy(() -> LineStreamParser.newInstance(data, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty workers map")
        void testNewInstanceWithEmptyWorkers() {
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            TestDataClass data = new TestDataClass();

            LineStreamParser<TestDataClass> parser = LineStreamParser.newInstance(data, workers);

            assertThat(parser).isNotNull();
            assertThat(parser.getIds()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Data Management Tests")
    class DataManagementTests {

        @Test
        @DisplayName("Should return initial data")
        void testGetData() {
            assertThat(parser.getData()).isSameAs(testData);
        }

        @Test
        @DisplayName("Should maintain data consistency")
        void testDataConsistency() {
            TestDataClass data = parser.getData();
            data.setValue("test-value");

            assertThat(parser.getData().getValue()).isEqualTo("test-value");
        }

        @Test
        @DisplayName("Should return same data instance across calls")
        void testDataInstanceConsistency() {
            TestDataClass data1 = parser.getData();
            TestDataClass data2 = parser.getData();

            assertThat(data1).isSameAs(data2);
        }
    }

    @Nested
    @DisplayName("Worker Management Tests")
    class WorkerManagementTests {

        @Test
        @DisplayName("Should return correct worker IDs")
        void testGetIds() {
            Collection<String> ids = parser.getIds();

            assertThat(ids).hasSize(1);
            assertThat(ids).contains("TEST_WORKER");
        }

        @Test
        @DisplayName("Should support multiple workers")
        void testMultipleWorkers() {
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            workers.put("WORKER1", new TestLineStreamWorker("WORKER1"));
            workers.put("WORKER2", new TestLineStreamWorker("WORKER2"));
            workers.put("WORKER3", new TestLineStreamWorker("WORKER3"));

            LineStreamParser<TestDataClass> parser = LineStreamParser.newInstance(testData, workers);
            Collection<String> ids = parser.getIds();

            assertThat(ids).hasSize(3);
            assertThat(ids).contains("WORKER1", "WORKER2", "WORKER3");
        }
    }

    @Nested
    @DisplayName("Parse Method Tests")
    class ParseMethodTests {

        @Test
        @DisplayName("Should parse with valid worker ID")
        void testParseWithValidId() {
            LineStream lineStream = LineStream.newInstance("test line");

            boolean result = parser.parse("TEST_WORKER", lineStream);

            assertThat(result).isTrue();
            assertThat(testWorker.wasCalled()).isTrue();
        }

        @Test
        @DisplayName("Should not parse with invalid worker ID")
        void testParseWithInvalidId() {
            LineStream lineStream = LineStream.newInstance("test line");

            boolean result = parser.parse("INVALID_WORKER", lineStream);

            assertThat(result).isFalse();
            assertThat(testWorker.wasCalled()).isFalse();
        }

        @Test
        @DisplayName("Should handle null worker ID")
        void testParseWithNullId() {
            LineStream lineStream = LineStream.newInstance("test line");

            boolean result = parser.parse(null, lineStream);

            assertThat(result).isFalse();
            assertThat(testWorker.wasCalled()).isFalse();
        }

        @Test
        @DisplayName("Should handle empty worker ID")
        void testParseWithEmptyId() {
            LineStream lineStream = LineStream.newInstance("test line");

            boolean result = parser.parse("", lineStream);

            assertThat(result).isFalse();
            assertThat(testWorker.wasCalled()).isFalse();
        }
    }

    @Nested
    @DisplayName("Stream Parsing Tests")
    class StreamParsingTests {

        @Test
        @DisplayName("Should parse input stream with default settings")
        void testParseInputStream() {
            String input = "TEST_WORKER\ndata line 1\n";
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            File dumpFile = new File(tempDir, "dump.txt");

            String unparsedLines = parser.parse(inputStream, dumpFile);

            assertThat(unparsedLines).isEmpty();
            assertThat(testWorker.wasCalled()).isTrue();
        }

        @Test
        @DisplayName("Should parse input stream with custom settings")
        void testParseInputStreamWithSettings() {
            String input = "TEST_WORKER\ndata line 1\nunknown line\n";
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            File dumpFile = new File(tempDir, "dump.txt");

            String unparsedLines = parser.parse(inputStream, dumpFile, true, true);

            assertThat(unparsedLines).contains("unknown line");
            assertThat(testWorker.wasCalled()).isTrue();
        }

        @Test
        @DisplayName("Should handle empty input stream")
        void testParseEmptyInputStream() {
            String input = "";
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            File dumpFile = new File(tempDir, "dump.txt");

            String unparsedLines = parser.parse(inputStream, dumpFile);

            assertThat(unparsedLines).isEmpty();
            assertThat(testWorker.wasCalled()).isFalse();
        }

        @Test
        @DisplayName("Should handle input stream with only unparsed lines")
        void testParseInputStreamOnlyUnparsed() {
            String input = "unknown line 1\nunknown line 2\n";
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            File dumpFile = new File(tempDir, "dump.txt");

            String unparsedLines = parser.parse(inputStream, dumpFile);

            assertThat(unparsedLines).contains("unknown line 1");
            assertThat(unparsedLines).contains("unknown line 2");
            assertThat(testWorker.wasCalled()).isFalse();
        }

        @Test
        @DisplayName("Should handle mixed input stream")
        void testParseMixedInputStream() {
            String input = "unknown line 1\nTEST_WORKER\ndata line\nunknown line 2\n";
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            File dumpFile = new File(tempDir, "dump.txt");

            String unparsedLines = parser.parse(inputStream, dumpFile);

            assertThat(unparsedLines).contains("unknown line 1");
            assertThat(unparsedLines).contains("unknown line 2");
            assertThat(unparsedLines).doesNotContain("TEST_WORKER");
            assertThat(unparsedLines).doesNotContain("data line"); // consumed by worker
            assertThat(testWorker.wasCalled()).isTrue();
        }
    }

    @Nested
    @DisplayName("Line Ignore Tests")
    class LineIgnoreTests {

        @Test
        @DisplayName("Should use default line ignore predicate")
        void testDefaultLineIgnore() {
            Predicate<String> ignorePredicate = parser.getLineIgnore();

            assertThat(ignorePredicate.test("any line")).isFalse();
            assertThat(ignorePredicate.test("")).isFalse();
            assertThat(ignorePredicate.test(null)).isFalse();
        }

        @Test
        @DisplayName("Should set custom line ignore predicate")
        void testSetLineIgnore() {
            Predicate<String> customPredicate = line -> line.startsWith("IGNORE:");

            parser.setLineIgnore(customPredicate);

            assertThat(parser.getLineIgnore()).isSameAs(customPredicate);
            assertThat(parser.getLineIgnore().test("IGNORE: this line")).isTrue();
            assertThat(parser.getLineIgnore().test("KEEP: this line")).isFalse();
        }

        @Test
        @DisplayName("Should apply ignore predicate during parsing")
        void testIgnorePredicateInParsing() {
            parser.setLineIgnore(line -> line.startsWith("#"));

            String input = "# comment line\nTEST_WORKER\ndata line\n# another comment\n";
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            File dumpFile = new File(tempDir, "dump.txt");

            String unparsedLines = parser.parse(inputStream, dumpFile);

            assertThat(unparsedLines).doesNotContain("# comment line");
            assertThat(unparsedLines).doesNotContain("# another comment");
            assertThat(testWorker.wasCalled()).isTrue();
        }
    }

    @Nested
    @DisplayName("Statistics and Monitoring Tests")
    class StatisticsTests {

        @Test
        @DisplayName("Should return read lines count from current stream")
        void testGetReadLines() {
            // Before any parsing, this should throw NPE since currentLineStream is null
            assertThatThrownBy(() -> parser.getReadLines())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should return read chars count from current stream")
        void testGetReadChars() {
            // Before any parsing, this should throw NPE since currentLineStream is null
            assertThatThrownBy(() -> parser.getReadChars())
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should track exception count")
        void testExceptionCount() {
            int numExceptions = parser.getNumExceptions();

            assertThat(numExceptions).isGreaterThanOrEqualTo(0);
        }

        @Test
        @DisplayName("Should indicate if exceptions occurred")
        void testHasExceptions() {
            boolean hasExceptions = parser.hasExceptions();

            assertThat(hasExceptions).isEqualTo(parser.getNumExceptions() > 0);
        }
    }

    @Nested
    @DisplayName("AutoCloseable Tests")
    class AutoCloseableTests {

        @Test
        @DisplayName("Should implement AutoCloseable")
        void testAutoCloseable() {
            assertThat(parser).isInstanceOf(AutoCloseable.class);
        }

        @Test
        @DisplayName("Should handle close gracefully")
        void testClose() throws Exception {
            // Should not throw exception
            parser.close();
        }

        @Test
        @DisplayName("Should be usable in try-with-resources")
        void testTryWithResources() {
            assertThatCode(() -> {
                try (LineStreamParser<TestDataClass> parser = LineStreamParser.newInstance(testData, workers)) {
                    parser.getData();
                    parser.getIds();
                }
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle worker exceptions gracefully")
        void testWorkerExceptionHandling() {
            // Create a worker that throws an exception
            TestLineStreamWorker errorWorker = new TestLineStreamWorker("ERROR_WORKER") {
                @Override
                public void apply(LineStream lineStream, TestDataClass data) {
                    throw new RuntimeException("Worker error");
                }
            };

            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            workers.put("ERROR_WORKER", errorWorker);
            LineStreamParser<TestDataClass> parser = LineStreamParser.newInstance(testData, workers);

            String input = "ERROR_WORKER\ndata line\n";
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            File dumpFile = new File(tempDir, "dump.txt");

            // The implementation catches the exception and logs it, but continues parsing
            String unparsedLines = parser.parse(inputStream, dumpFile);

            // The line after ERROR_WORKER becomes unparsed since the worker failed
            assertThat(unparsedLines).contains("data line");
            assertThat(parser.getNumExceptions()).isEqualTo(1); // Exception is tracked
        }

        @Test
        @DisplayName("Should handle invalid input stream")
        void testInvalidInputStream() {
            File dumpFile = new File(tempDir, "dump.txt");

            // Create a stream that returns EOF immediately
            InputStream errorStream = new InputStream() {
                @Override
                public int read() {
                    return -1; // EOF
                }
            };

            assertThatCode(() -> parser.parse(errorStream, dumpFile))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complex parsing scenario")
        void testComplexParsingScenario() {
            // Setup multiple workers
            Map<String, LineStreamWorker<TestDataClass>> workers = new HashMap<>();
            TestLineStreamWorker worker1 = new TestLineStreamWorker("WORKER1");
            TestLineStreamWorker worker2 = new TestLineStreamWorker("WORKER2");
            workers.put("WORKER1", worker1);
            workers.put("WORKER2", worker2);

            LineStreamParser<TestDataClass> parser = LineStreamParser.newInstance(testData, workers);
            parser.setLineIgnore(line -> line.startsWith("#"));

            String input = "# comment\nWORKER1\ndata1\n# another comment\nWORKER2\ndata2\nunknown\n";
            InputStream inputStream = new ByteArrayInputStream(input.getBytes());
            File dumpFile = new File(tempDir, "dump.txt");

            String unparsedLines = parser.parse(inputStream, dumpFile);

            assertThat(unparsedLines).contains("unknown");
            assertThat(unparsedLines).doesNotContain("#");
            assertThat(unparsedLines).doesNotContain("data1"); // consumed by worker1
            assertThat(unparsedLines).doesNotContain("data2"); // consumed by worker2
            assertThat(worker1.wasCalled()).isTrue();
            assertThat(worker2.wasCalled()).isTrue();
        }

        @Test
        @DisplayName("Should maintain state consistency across operations")
        void testStateConsistency() {
            TestDataClass initialData = parser.getData();
            Collection<String> initialIds = parser.getIds();

            // Perform various operations
            LineStream lineStream = LineStream.newInstance("test");
            parser.parse("TEST_WORKER", lineStream);
            parser.setLineIgnore(line -> false);
            parser.getReadLines();
            parser.getNumExceptions();

            // State should remain consistent
            assertThat(parser.getData()).isSameAs(initialData);
            assertThat(parser.getIds()).isEqualTo(initialIds);
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

        public TestLineStreamWorker(String id) {
            this.id = id;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public void init(TestDataClass data) {
            // No initialization needed for tests
        }

        @Override
        public void apply(LineStream lineStream, TestDataClass data) {
            called = true;
            // Consume only one line (simulating a worker that processes one data line)
            if (lineStream.hasNextLine()) {
                String line = lineStream.nextLine();
                data.setValue(data.getValue() + line + ";");
            }
        }

        public boolean wasCalled() {
            return called;
        }
    }
}
