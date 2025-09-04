package pt.up.fe.specs.util.treenode.transform;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.treenode.transform.impl.DefaultTransformResult;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for TransformResult interface.
 * Tests the behavior of transformation results and traversal control.
 * 
 * @author Generated Tests
 */
@DisplayName("TransformResult Tests")
class TransformResultTest {

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("visitChildren() should return boolean value")
        void testVisitChildren_ReturnsBoolean() {
            TestTransformResult trueResult = new TestTransformResult(true);
            TestTransformResult falseResult = new TestTransformResult(false);

            assertThat(trueResult.visitChildren()).isTrue();
            assertThat(falseResult.visitChildren()).isFalse();
        }

        @Test
        @DisplayName("Multiple calls to visitChildren() should return consistent results")
        void testVisitChildren_IsConsistent() {
            TestTransformResult result = new TestTransformResult(true);

            boolean firstCall = result.visitChildren();
            boolean secondCall = result.visitChildren();
            boolean thirdCall = result.visitChildren();

            assertThat(firstCall).isEqualTo(secondCall).isEqualTo(thirdCall).isTrue();
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryTests {

        @Test
        @DisplayName("empty() should return non-null TransformResult")
        void testEmpty_ReturnsNonNull() {
            TransformResult result = TransformResult.empty();

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("empty() should return result with visitChildren() = true")
        void testEmpty_ReturnsVisitChildrenTrue() {
            TransformResult result = TransformResult.empty();

            assertThat(result.visitChildren()).isTrue();
        }

        @Test
        @DisplayName("empty() should return DefaultTransformResult instance")
        void testEmpty_ReturnsDefaultTransformResult() {
            TransformResult result = TransformResult.empty();

            assertThat(result).isInstanceOf(DefaultTransformResult.class);
        }

        @Test
        @DisplayName("Multiple calls to empty() should return equivalent results")
        void testEmpty_ReturnsEquivalentResults() {
            TransformResult result1 = TransformResult.empty();
            TransformResult result2 = TransformResult.empty();

            assertThat(result1.visitChildren()).isEqualTo(result2.visitChildren());
            // Note: They may or may not be the same instance - that's implementation
            // dependent
        }
    }

    @Nested
    @DisplayName("Traversal Control Tests")
    class TraversalControlTests {

        @Test
        @DisplayName("Should support enabling child traversal")
        void testEnableChildTraversal() {
            TestTransformResult enableTraversal = new TestTransformResult(true);

            assertThat(enableTraversal.visitChildren()).isTrue();
        }

        @Test
        @DisplayName("Should support disabling child traversal")
        void testDisableChildTraversal() {
            TestTransformResult disableTraversal = new TestTransformResult(false);

            assertThat(disableTraversal.visitChildren()).isFalse();
        }

        @Test
        @DisplayName("Should handle different traversal configurations")
        void testDifferentTraversalConfigurations() {
            TestTransformResult[] results = {
                    new TestTransformResult(true),
                    new TestTransformResult(false),
                    new TestTransformResult(true),
                    new TestTransformResult(false)
            };

            assertThat(results[0].visitChildren()).isTrue();
            assertThat(results[1].visitChildren()).isFalse();
            assertThat(results[2].visitChildren()).isTrue();
            assertThat(results[3].visitChildren()).isFalse();
        }
    }

    @Nested
    @DisplayName("Implementation Variations")
    class ImplementationVariationsTests {

        @Test
        @DisplayName("Should support custom implementations")
        void testCustomImplementations() {
            // Test different custom implementations
            TransformResult customTrue = new CustomTransformResult(true);
            TransformResult customFalse = new CustomTransformResult(false);
            TransformResult conditionalResult = new ConditionalTransformResult();

            assertThat(customTrue.visitChildren()).isTrue();
            assertThat(customFalse.visitChildren()).isFalse();
            assertThat(conditionalResult.visitChildren()).isTrue(); // Default behavior
        }

        @Test
        @DisplayName("Should handle stateful implementations")
        void testStatefulImplementations() {
            StatefulTransformResult statefulResult = new StatefulTransformResult();

            // First call returns true
            assertThat(statefulResult.visitChildren()).isTrue();
            // Subsequent calls return false
            assertThat(statefulResult.visitChildren()).isFalse();
            assertThat(statefulResult.visitChildren()).isFalse();
        }
    }

    @Nested
    @DisplayName("Integration with Traversal Strategy")
    class TraversalIntegrationTests {

        @Test
        @DisplayName("Should integrate with pre-order traversal strategy")
        void testPreOrderTraversalIntegration() {
            // Test that demonstrates the intended usage in pre-order traversal
            TestTransformResult continueResult = new TestTransformResult(true);
            TestTransformResult stopResult = new TestTransformResult(false);

            // In pre-order traversal, if visitChildren() returns true,
            // the traversal should continue to children
            if (continueResult.visitChildren()) {
                // This branch should be taken
                assertThat(true).isTrue(); // Continue to children
            } else {
                fail("Should continue to children when visitChildren() returns true");
            }

            // If visitChildren() returns false, traversal should skip children
            if (stopResult.visitChildren()) {
                fail("Should not continue to children when visitChildren() returns false");
            } else {
                // This branch should be taken
                assertThat(true).isTrue(); // Skip children
            }
        }
    }

    /**
     * Basic test implementation of TransformResult
     */
    private static class TestTransformResult implements TransformResult {
        private final boolean visitChildren;

        public TestTransformResult(boolean visitChildren) {
            this.visitChildren = visitChildren;
        }

        @Override
        public boolean visitChildren() {
            return visitChildren;
        }
    }

    /**
     * Custom implementation for testing different behaviors
     */
    private static class CustomTransformResult implements TransformResult {
        private final boolean shouldVisitChildren;

        public CustomTransformResult(boolean shouldVisitChildren) {
            this.shouldVisitChildren = shouldVisitChildren;
        }

        @Override
        public boolean visitChildren() {
            return shouldVisitChildren;
        }
    }

    /**
     * Conditional implementation that always returns true
     */
    private static class ConditionalTransformResult implements TransformResult {
        @Override
        public boolean visitChildren() {
            // Always return true for testing
            return true;
        }
    }

    /**
     * Stateful implementation that changes behavior on subsequent calls
     */
    private static class StatefulTransformResult implements TransformResult {
        private boolean firstCall = true;

        @Override
        public boolean visitChildren() {
            if (firstCall) {
                firstCall = false;
                return true;
            }
            return false;
        }
    }
}
