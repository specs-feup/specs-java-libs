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
 * Comprehensive test suite for GenericLineStreamWorker implementation class.
 * Tests constructor behavior, method implementations, functional composition,
 * state management, error handling, and lifecycle compliance.
 * 
 * @author Generated Tests
 */
class GenericLineStreamWorkerTest {

    private TestDataClass testData;

    @BeforeEach
    void setUp() {
        testData = new TestDataClass();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create worker with all parameters")
        void testConstructorWithAllParameters() {
            String id = "TEST_WORKER";
            Consumer<TestDataClass> init = data -> data.setValue("init");
            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> data.setValue("apply");

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(id, init, apply);

            assertThat(worker).isNotNull();
            assertThat(worker.getId()).isEqualTo(id);
        }

        @Test
        @DisplayName("Should handle null id")
        void testConstructorWithNullId() {
            Consumer<TestDataClass> init = data -> {
            };
            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(null, init, apply);

            assertThat(worker).isNotNull();
            assertThat(worker.getId()).isNull();
        }

        @Test
        @DisplayName("Should handle null init function")
        void testConstructorWithNullInit() {
            String id = "NULL_INIT_WORKER";
            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> data.setValue("applied");

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(id, null, apply);

            assertThat(worker).isNotNull();
            assertThat(worker.getId()).isEqualTo(id);

            // Calling init with null function should throw NPE
            assertThatThrownBy(() -> worker.init(testData))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null apply function")
        void testConstructorWithNullApply() {
            String id = "NULL_APPLY_WORKER";
            Consumer<TestDataClass> init = data -> data.setValue("initialized");

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(id, init, null);

            assertThat(worker).isNotNull();
            assertThat(worker.getId()).isEqualTo(id);

            // Init should work
            worker.init(testData);
            assertThat(testData.getValue()).isEqualTo("initialized");

            // Calling apply with null function should throw NPE
            LineStream stream = LineStream.newInstance("test");
            assertThatThrownBy(() -> worker.apply(stream, testData))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty id")
        void testConstructorWithEmptyId() {
            String id = "";
            Consumer<TestDataClass> init = data -> {
            };
            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(id, init, apply);

            assertThat(worker).isNotNull();
            assertThat(worker.getId()).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("ID Management Tests")
    class IdManagementTests {

        @Test
        @DisplayName("Should return correct id")
        void testGetIdReturnsCorrectValue() {
            String workerId = "UNIQUE_WORKER_ID";
            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    workerId, data -> {
                    }, (stream, data) -> {
                    });

            assertThat(worker.getId()).isEqualTo(workerId);
        }

        @Test
        @DisplayName("Should handle special characters in id")
        void testIdWithSpecialCharacters() {
            String specialId = "worker-123_$%^&*()";
            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    specialId, data -> {
                    }, (stream, data) -> {
                    });

            assertThat(worker.getId()).isEqualTo(specialId);
        }

        @Test
        @DisplayName("Should handle unicode characters in id")
        void testIdWithUnicodeCharacters() {
            String unicodeId = "worker_测试_🚀";
            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    unicodeId, data -> {
                    }, (stream, data) -> {
                    });

            assertThat(worker.getId()).isEqualTo(unicodeId);
        }

        @Test
        @DisplayName("Should maintain id immutability")
        void testIdImmutability() {
            String originalId = "ORIGINAL_ID";
            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    originalId, data -> {
                    }, (stream, data) -> {
                    });

            String id1 = worker.getId();
            String id2 = worker.getId();

            assertThat(id1).isEqualTo(id2);
            assertThat(id1).isEqualTo(originalId);
        }
    }

    @Nested
    @DisplayName("Init Method Tests")
    class InitMethodTests {

        @Test
        @DisplayName("Should execute init function correctly")
        void testInitFunctionExecution() {
            AtomicBoolean initCalled = new AtomicBoolean(false);
            TestDataClass capturedData = new TestDataClass();

            Consumer<TestDataClass> init = data -> {
                initCalled.set(true);
                capturedData.setValue(data.getValue());
                data.setValue("initialized");
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "INIT_TEST", init, (stream, data) -> {
                    });

            testData.setValue("original");
            worker.init(testData);

            assertThat(initCalled.get()).isTrue();
            assertThat(capturedData.getValue()).isEqualTo("original");
            assertThat(testData.getValue()).isEqualTo("initialized");
        }

        @Test
        @DisplayName("Should handle multiple init calls")
        void testMultipleInitCalls() {
            AtomicInteger initCount = new AtomicInteger(0);

            Consumer<TestDataClass> init = data -> {
                int count = initCount.incrementAndGet();
                data.setValue("init-" + count);
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "MULTI_INIT", init, (stream, data) -> {
                    });

            worker.init(testData);
            assertThat(testData.getValue()).isEqualTo("init-1");

            worker.init(testData);
            assertThat(testData.getValue()).isEqualTo("init-2");

            worker.init(testData);
            assertThat(testData.getValue()).isEqualTo("init-3");
        }

        @Test
        @DisplayName("Should handle init with null data")
        void testInitWithNullData() {
            AtomicBoolean initCalled = new AtomicBoolean(false);

            Consumer<TestDataClass> init = data -> {
                initCalled.set(true);
                // Should handle null data gracefully or throw NPE
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "NULL_DATA_INIT", init, (stream, data) -> {
                    });

            worker.init(null);
            assertThat(initCalled.get()).isTrue();
        }

        @Test
        @DisplayName("Should propagate exceptions from init function")
        void testInitExceptionPropagation() {
            Consumer<TestDataClass> failingInit = data -> {
                throw new RuntimeException("Init failed");
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "FAILING_INIT", failingInit, (stream, data) -> {
                    });

            assertThatThrownBy(() -> worker.init(testData))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Init failed");
        }

        @Test
        @DisplayName("Should handle complex init operations")
        void testComplexInitOperations() {
            Consumer<TestDataClass> complexInit = data -> {
                // Simulate complex initialization
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 5; i++) {
                    sb.append("item-").append(i).append(";");
                }
                data.setValue(sb.toString());
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "COMPLEX_INIT", complexInit, (stream, data) -> {
                    });

            worker.init(testData);
            assertThat(testData.getValue()).isEqualTo("item-0;item-1;item-2;item-3;item-4;");
        }
    }

    @Nested
    @DisplayName("Apply Method Tests")
    class ApplyMethodTests {

        @Test
        @DisplayName("Should execute apply function correctly")
        void testApplyFunctionExecution() {
            AtomicBoolean applyCalled = new AtomicBoolean(false);

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                applyCalled.set(true);
                if (stream.hasNextLine()) {
                    data.setValue(stream.nextLine());
                }
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "APPLY_TEST", data -> {
                    }, apply);

            LineStream stream = LineStream.newInstance("test data");
            worker.apply(stream, testData);

            assertThat(applyCalled.get()).isTrue();
            assertThat(testData.getValue()).isEqualTo("test data");
        }

        @Test
        @DisplayName("Should handle stream processing correctly")
        void testStreamProcessing() {
            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                StringBuilder content = new StringBuilder();
                while (stream.hasNextLine()) {
                    if (content.length() > 0)
                        content.append("|");
                    content.append(stream.nextLine());
                }
                data.setValue(content.toString());
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "STREAM_PROCESSOR", data -> {
                    }, apply);

            LineStream stream = LineStream.newInstance("line1\nline2\nline3\n");
            worker.apply(stream, testData);

            assertThat(testData.getValue()).isEqualTo("line1|line2|line3");
        }

        @Test
        @DisplayName("Should handle empty stream")
        void testEmptyStreamHandling() {
            AtomicBoolean applyCalled = new AtomicBoolean(false);

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                applyCalled.set(true);
                data.setValue(stream.hasNextLine() ? "has_data" : "empty");
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "EMPTY_STREAM", data -> {
                    }, apply);

            LineStream stream = LineStream.newInstance("");
            worker.apply(stream, testData);

            assertThat(applyCalled.get()).isTrue();
            assertThat(testData.getValue()).isEqualTo("empty");
        }

        @Test
        @DisplayName("Should handle null stream")
        void testNullStreamHandling() {
            AtomicBoolean applyCalled = new AtomicBoolean(false);

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                applyCalled.set(true);
                data.setValue(stream == null ? "null_stream" : "valid_stream");
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "NULL_STREAM", data -> {
                    }, apply);

            worker.apply(null, testData);

            assertThat(applyCalled.get()).isTrue();
            assertThat(testData.getValue()).isEqualTo("null_stream");
        }

        @Test
        @DisplayName("Should handle null data in apply")
        void testApplyWithNullData() {
            AtomicBoolean applyCalled = new AtomicBoolean(false);

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                applyCalled.set(true);
                // Implementation should handle null data appropriately
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "NULL_DATA_APPLY", data -> {
                    }, apply);

            LineStream stream = LineStream.newInstance("test");
            worker.apply(stream, null);

            assertThat(applyCalled.get()).isTrue();
        }

        @Test
        @DisplayName("Should propagate exceptions from apply function")
        void testApplyExceptionPropagation() {
            BiConsumer<LineStream, TestDataClass> failingApply = (stream, data) -> {
                throw new IllegalStateException("Apply failed");
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "FAILING_APPLY", data -> {
                    }, failingApply);

            LineStream stream = LineStream.newInstance("test");

            assertThatThrownBy(() -> worker.apply(stream, testData))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessage("Apply failed");
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

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "MULTI_APPLY", data -> {
                    }, apply);

            worker.apply(LineStream.newInstance("first"), testData);
            assertThat(testData.getValue()).isEqualTo("1:first;");

            worker.apply(LineStream.newInstance("second"), testData);
            assertThat(testData.getValue()).isEqualTo("1:first;2:second;");
        }
    }

    @Nested
    @DisplayName("Default Close Method Tests")
    class DefaultCloseMethodTests {

        @Test
        @DisplayName("Should use default close implementation from interface")
        void testDefaultCloseImplementation() {
            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "DEFAULT_CLOSE", data -> {
                    }, (stream, data) -> {
                    });

            String originalValue = "unchanged";
            testData.setValue(originalValue);

            // Default close should do nothing
            worker.close(testData);
            assertThat(testData.getValue()).isEqualTo(originalValue);
        }

        @Test
        @DisplayName("Should handle close with null data")
        void testCloseWithNullData() {
            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "NULL_CLOSE", data -> {
                    }, (stream, data) -> {
                    });

            // Default close with null should not throw exception
            worker.close(null);
        }

        @Test
        @DisplayName("Should call close multiple times without side effects")
        void testMultipleCloseCalls() {
            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "MULTI_CLOSE", data -> {
                    }, (stream, data) -> {
                    });

            String originalValue = "constant";
            testData.setValue(originalValue);

            worker.close(testData);
            worker.close(testData);
            worker.close(testData);

            assertThat(testData.getValue()).isEqualTo(originalValue);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work as complete LineStreamWorker implementation")
        void testCompleteWorkerImplementation() {
            StringBuilder lifecycle = new StringBuilder();

            Consumer<TestDataClass> init = data -> {
                lifecycle.append("init;");
                data.setValue("initialized");
            };

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                lifecycle.append("apply;");
                if (stream.hasNextLine()) {
                    data.setValue(data.getValue() + ":" + stream.nextLine());
                }
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "COMPLETE_WORKER", init, apply);

            // Verify it implements LineStreamWorker interface
            assertThat(worker).isInstanceOf(LineStreamWorker.class);

            // Execute complete lifecycle
            worker.init(testData);
            worker.apply(LineStream.newInstance("data"), testData);
            worker.close(testData);

            assertThat(lifecycle.toString()).isEqualTo("init;apply;");
            assertThat(testData.getValue()).isEqualTo("initialized:data");
        }

        @Test
        @DisplayName("Should work with factory methods from LineStreamWorker")
        void testWithFactoryMethods() {
            Consumer<TestDataClass> init = data -> data.setValue("factory-init");
            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                if (stream.hasNextLine()) {
                    data.setValue(data.getValue() + ":" + stream.nextLine());
                }
            };

            // Use factory method which creates GenericLineStreamWorker internally
            LineStreamWorker<TestDataClass> factoryWorker = LineStreamWorker.newInstance("FACTORY", init, apply);

            assertThat(factoryWorker).isInstanceOf(GenericLineStreamWorker.class);
            assertThat(factoryWorker.getId()).isEqualTo("FACTORY");

            factoryWorker.init(testData);
            factoryWorker.apply(LineStream.newInstance("test"), testData);

            assertThat(testData.getValue()).isEqualTo("factory-init:test");
        }

        @Test
        @DisplayName("Should handle complex data processing scenarios")
        void testComplexDataProcessing() {
            Consumer<TestDataClass> init = data -> data.setValue("");

            BiConsumer<LineStream, TestDataClass> apply = (stream, data) -> {
                int lineCount = 0;
                StringBuilder content = new StringBuilder();

                while (stream.hasNextLine()) {
                    String line = stream.nextLine();
                    lineCount++;

                    // Process only non-empty lines
                    if (!line.trim().isEmpty()) {
                        if (content.length() > 0)
                            content.append("|");
                        content.append(line.toUpperCase());
                    }
                }

                data.setValue("Lines:" + lineCount + ";Content:" + content.toString());
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "COMPLEX", init, apply);

            String input = "hello\n\nworld\n  \ntest\n";

            worker.init(testData);
            worker.apply(LineStream.newInstance(input), testData);

            assertThat(testData.getValue()).isEqualTo("Lines:5;Content:HELLO|WORLD|TEST");
        }

        @Test
        @DisplayName("Should maintain functional composition")
        void testFunctionalComposition() {
            // Test that functions are properly composed
            AtomicInteger initCalls = new AtomicInteger(0);
            AtomicInteger applyCalls = new AtomicInteger(0);

            Consumer<TestDataClass> countingInit = data -> {
                initCalls.incrementAndGet();
                data.setValue("init");
            };

            BiConsumer<LineStream, TestDataClass> countingApply = (stream, data) -> {
                applyCalls.incrementAndGet();
                data.setValue(data.getValue() + ":apply");
            };

            GenericLineStreamWorker<TestDataClass> worker = new GenericLineStreamWorker<>(
                    "COMPOSITION", countingInit, countingApply);

            // Multiple operations
            worker.init(testData);
            worker.apply(LineStream.newInstance("test1"), testData);
            worker.init(testData);
            worker.apply(LineStream.newInstance("test2"), testData);

            assertThat(initCalls.get()).isEqualTo(2);
            assertThat(applyCalls.get()).isEqualTo(2);
            assertThat(testData.getValue()).isEqualTo("init:apply");
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
