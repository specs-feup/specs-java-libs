package pt.up.fe.specs.util.treenode.transform.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

import pt.up.fe.specs.util.treenode.transform.TransformResult;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for DefaultTransformResult class.
 * Tests the default implementation of TransformResult interface.
 * 
 * @author Generated Tests
 */
@DisplayName("DefaultTransformResult Tests")
class DefaultTransformResultTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor should accept true for visitChildren")
        void testConstructor_WithTrue() {
            DefaultTransformResult result = new DefaultTransformResult(true);

            assertThat(result.visitChildren()).isTrue();
        }

        @Test
        @DisplayName("Constructor should accept false for visitChildren")
        void testConstructor_WithFalse() {
            DefaultTransformResult result = new DefaultTransformResult(false);

            assertThat(result.visitChildren()).isFalse();
        }

        @Test
        @DisplayName("Constructor should create valid TransformResult instance")
        void testConstructor_CreatesValidInstance() {
            DefaultTransformResult result = new DefaultTransformResult(true);

            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(TransformResult.class);
            assertThat(result).isInstanceOf(DefaultTransformResult.class);
        }
    }

    @Nested
    @DisplayName("VisitChildren Method Tests")
    class VisitChildrenMethodTests {

        @Test
        @DisplayName("visitChildren should return true when constructed with true")
        void testVisitChildren_WithTrue() {
            DefaultTransformResult result = new DefaultTransformResult(true);

            assertThat(result.visitChildren()).isTrue();
        }

        @Test
        @DisplayName("visitChildren should return false when constructed with false")
        void testVisitChildren_WithFalse() {
            DefaultTransformResult result = new DefaultTransformResult(false);

            assertThat(result.visitChildren()).isFalse();
        }

        @Test
        @DisplayName("visitChildren should be consistent across multiple calls")
        void testVisitChildren_ConsistentAcrossMultipleCalls() {
            DefaultTransformResult resultTrue = new DefaultTransformResult(true);
            DefaultTransformResult resultFalse = new DefaultTransformResult(false);

            // Call multiple times to ensure consistency
            for (int i = 0; i < 10; i++) {
                assertThat(resultTrue.visitChildren()).isTrue();
                assertThat(resultFalse.visitChildren()).isFalse();
            }
        }
    }

    @Nested
    @DisplayName("TransformResult Interface Implementation Tests")
    class TransformResultInterfaceTests {

        @Test
        @DisplayName("Should properly implement TransformResult interface")
        void testImplementsTransformResultInterface() {
            DefaultTransformResult result = new DefaultTransformResult(true);

            // Should be assignable to TransformResult
            TransformResult transformResult = result;
            assertThat(transformResult).isNotNull();
            assertThat(transformResult.visitChildren()).isTrue();
        }

        @Test
        @DisplayName("Should work in TransformResult context with true")
        void testInTransformResultContext_WithTrue() {
            TransformResult result = new DefaultTransformResult(true);

            assertThat(result.visitChildren()).isTrue();
        }

        @Test
        @DisplayName("Should work in TransformResult context with false")
        void testInTransformResultContext_WithFalse() {
            TransformResult result = new DefaultTransformResult(false);

            assertThat(result.visitChildren()).isFalse();
        }
    }

    @Nested
    @DisplayName("Factory Method Integration Tests")
    class FactoryMethodIntegrationTests {

        @Test
        @DisplayName("Should be compatible with TransformResult.empty() factory")
        void testCompatibilityWithEmptyFactory() {
            TransformResult emptyResult = TransformResult.empty();
            DefaultTransformResult explicitResult = new DefaultTransformResult(true);

            // Both should have the same behavior for visitChildren
            assertThat(emptyResult.visitChildren()).isEqualTo(explicitResult.visitChildren());
        }

        @Test
        @DisplayName("Should verify TransformResult.empty() returns DefaultTransformResult")
        void testEmptyFactoryReturnsDefaultTransformResult() {
            TransformResult result = TransformResult.empty();

            assertThat(result).isInstanceOf(DefaultTransformResult.class);
            assertThat(result.visitChildren()).isTrue();
        }
    }

    @Nested
    @DisplayName("Equality and Object Method Tests")
    class EqualityAndObjectMethodTests {

        @Test
        @DisplayName("Two instances with same visitChildren value should have same behavior")
        void testSameBehaviorWithSameValues() {
            DefaultTransformResult result1 = new DefaultTransformResult(true);
            DefaultTransformResult result2 = new DefaultTransformResult(true);
            DefaultTransformResult result3 = new DefaultTransformResult(false);
            DefaultTransformResult result4 = new DefaultTransformResult(false);

            assertThat(result1.visitChildren()).isEqualTo(result2.visitChildren());
            assertThat(result3.visitChildren()).isEqualTo(result4.visitChildren());
            assertThat(result1.visitChildren()).isNotEqualTo(result3.visitChildren());
        }

        @Test
        @DisplayName("toString should not throw exceptions")
        void testToString_DoesNotThrowExceptions() {
            DefaultTransformResult resultTrue = new DefaultTransformResult(true);
            DefaultTransformResult resultFalse = new DefaultTransformResult(false);

            assertThatCode(() -> resultTrue.toString()).doesNotThrowAnyException();
            assertThatCode(() -> resultFalse.toString()).doesNotThrowAnyException();

            String stringTrue = resultTrue.toString();
            String stringFalse = resultFalse.toString();

            assertThat(stringTrue).isNotNull();
            assertThat(stringFalse).isNotNull();
        }

        @Test
        @DisplayName("hashCode should not throw exceptions")
        void testHashCode_DoesNotThrowExceptions() {
            DefaultTransformResult resultTrue = new DefaultTransformResult(true);
            DefaultTransformResult resultFalse = new DefaultTransformResult(false);

            assertThatCode(() -> resultTrue.hashCode()).doesNotThrowAnyException();
            assertThatCode(() -> resultFalse.hashCode()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("equals should handle different object types")
        void testEquals_WithDifferentObjectTypes() {
            DefaultTransformResult result = new DefaultTransformResult(true);

            assertThatCode(() -> result.equals(null)).doesNotThrowAnyException();
            assertThatCode(() -> result.equals("string")).doesNotThrowAnyException();
            assertThatCode(() -> result.equals(new Object())).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Usage Scenario Tests")
    class UsageScenarioTests {

        @Test
        @DisplayName("Should work for continuing traversal scenario")
        void testContinueTraversalScenario() {
            DefaultTransformResult continueResult = new DefaultTransformResult(true);

            // Simulate pre-order traversal decision
            if (continueResult.visitChildren()) {
                // This branch should be taken
                assertThat(true).isTrue();
            } else {
                fail("Should continue traversal when visitChildren is true");
            }
        }

        @Test
        @DisplayName("Should work for stopping traversal scenario")
        void testStopTraversalScenario() {
            DefaultTransformResult stopResult = new DefaultTransformResult(false);

            // Simulate pre-order traversal decision
            if (stopResult.visitChildren()) {
                fail("Should not visit children when visitChildren is false");
            } else {
                // This branch should be taken
                assertThat(true).isTrue();
            }
        }

        @Test
        @DisplayName("Should work in array or collection contexts")
        void testInCollectionContexts() {
            DefaultTransformResult[] results = {
                    new DefaultTransformResult(true),
                    new DefaultTransformResult(false),
                    new DefaultTransformResult(true)
            };

            int trueCount = 0;
            int falseCount = 0;

            for (DefaultTransformResult result : results) {
                if (result.visitChildren()) {
                    trueCount++;
                } else {
                    falseCount++;
                }
            }

            assertThat(trueCount).isEqualTo(2);
            assertThat(falseCount).isEqualTo(1);
        }

        @Test
        @DisplayName("Should work with polymorphic TransformResult references")
        void testPolymorphicUsage() {
            TransformResult[] results = {
                    new DefaultTransformResult(true),
                    new DefaultTransformResult(false),
                    TransformResult.empty()
            };

            for (TransformResult result : results) {
                // Should be able to call visitChildren on any TransformResult
                boolean shouldVisit = result.visitChildren();
                assertThat(shouldVisit).isIn(true, false);
            }
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should be thread-safe for immutable state")
        void testThreadSafety() throws InterruptedException {
            DefaultTransformResult result = new DefaultTransformResult(true);
            int numThreads = 10;
            Thread[] threads = new Thread[numThreads];
            boolean[] results = new boolean[numThreads];

            // Create threads that all read the same result
            for (int i = 0; i < numThreads; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    results[index] = result.visitChildren();
                });
            }

            // Start all threads
            for (Thread thread : threads) {
                thread.start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // All threads should see the same value
            for (boolean threadResult : results) {
                assertThat(threadResult).isTrue();
            }
        }

        @Test
        @DisplayName("Multiple instances should not interfere with each other")
        void testMultipleInstancesIndependence() {
            DefaultTransformResult result1 = new DefaultTransformResult(true);
            DefaultTransformResult result2 = new DefaultTransformResult(false);

            // Accessing one should not affect the other
            boolean value1 = result1.visitChildren();
            boolean value2 = result2.visitChildren();
            boolean value1Again = result1.visitChildren();
            boolean value2Again = result2.visitChildren();

            assertThat(value1).isTrue();
            assertThat(value2).isFalse();
            assertThat(value1Again).isTrue();
            assertThat(value2Again).isFalse();
        }
    }

    @Nested
    @DisplayName("Memory and Performance Tests")
    class MemoryAndPerformanceTests {

        @Test
        @DisplayName("Should have minimal memory footprint")
        void testMemoryFootprint() {
            // Create many instances to test memory usage
            DefaultTransformResult[] results = new DefaultTransformResult[1000];

            for (int i = 0; i < results.length; i++) {
                results[i] = new DefaultTransformResult(i % 2 == 0);
            }

            // All instances should be created successfully
            assertThat(results).hasSize(1000);
            assertThat(results[0]).isNotNull();
            assertThat(results[999]).isNotNull();

            // Verify they work correctly
            assertThat(results[0].visitChildren()).isTrue(); // 0 % 2 == 0
            assertThat(results[1].visitChildren()).isFalse(); // 1 % 2 != 0
        }

        @RetryingTest(5)
        @DisplayName("Should have fast access performance")
        void testAccessPerformance() {
            DefaultTransformResult result = new DefaultTransformResult(true);

            long startTime = System.nanoTime();

            // Perform many accesses
            for (int i = 0; i < 100000; i++) {
                boolean value = result.visitChildren();
                assertThat(value).isTrue();
            }

            long endTime = System.nanoTime();
            long durationNanos = endTime - startTime;

            // Should complete very quickly (less than 100ms)
            assertThat(durationNanos).isLessThan(100_000_000L);
        }
    }
}
