package pt.up.fe.specs.util.treenode.transform;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.treenode.ATreeNode;
import pt.up.fe.specs.util.treenode.transform.util.TraversalStrategy;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive test suite for TransformRule interface.
 * Tests the interface contract and default method implementations.
 * 
 * @author Generated Tests
 */
@DisplayName("TransformRule Tests")
class TransformRuleTest {

    private TestTreeNode testNode;
    private TransformQueue<TestTreeNode> mockQueue;
    private TraversalStrategy mockTraversalStrategy;
    private TransformRule<TestTreeNode, TestTransformResult> transformRule;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        testNode = new TestTreeNode("test");
        mockQueue = mock(TransformQueue.class);
        mockTraversalStrategy = mock(TraversalStrategy.class);
        transformRule = createTestTransformRule();
    }

    /**
     * Test tree node implementation for testing purposes.
     */
    private static class TestTreeNode extends ATreeNode<TestTreeNode> {
        private final String value;

        public TestTreeNode(String value) {
            super(null);
            this.value = value;
        }

        @Override
        public String toContentString() {
            return value;
        }

        @Override
        protected TestTreeNode copyPrivate() {
            return new TestTreeNode(value);
        }

        @Override
        public String toString() {
            return "TestTreeNode(" + value + ")";
        }
    }

    /**
     * Test transform result implementation.
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

        // Helper method for tests
        public boolean continueTraversal() {
            return visitChildren;
        }
    }

    /**
     * Creates a test implementation of TransformRule.
     */
    private TransformRule<TestTreeNode, TestTransformResult> createTestTransformRule() {
        return new TransformRule<TestTreeNode, TestTransformResult>() {
            @Override
            public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                // Simple test implementation that returns continue traversal
                return new TestTransformResult(true);
            }

            @Override
            public TraversalStrategy getTraversalStrategy() {
                return mockTraversalStrategy;
            }
        };
    }

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should have apply method that accepts node and queue")
        void testApplyMethod_AcceptsNodeAndQueue() {
            TestTransformResult result = transformRule.apply(testNode, mockQueue);

            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(TestTransformResult.class);
        }

        @Test
        @DisplayName("Should have getTraversalStrategy method")
        void testGetTraversalStrategy_ReturnsStrategy() {
            TraversalStrategy strategy = transformRule.getTraversalStrategy();

            assertThat(strategy).isNotNull();
            assertThat(strategy).isSameAs(mockTraversalStrategy);
        }

        @Test
        @DisplayName("apply should not modify tree directly")
        void testApply_ShouldNotModifyTreeDirectly() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode child = new TestTreeNode("child");
            parent.addChild(child);

            int initialParentChildren = parent.getNumChildren();
            int initialChildrenCount = testNode.getNumChildren();

            // Apply transformation
            transformRule.apply(testNode, mockQueue);

            // Verify tree structure unchanged (transformation should use queue)
            assertThat(parent.getNumChildren()).isEqualTo(initialParentChildren);
            assertThat(testNode.getNumChildren()).isEqualTo(initialChildrenCount);
        }
    }

    @Nested
    @DisplayName("Default Method Tests")
    class DefaultMethodTests {

        @Test
        @DisplayName("visit should call traversal strategy apply method")
        void testVisit_CallsTraversalStrategyApply() {
            // Execute visit method
            transformRule.visit(testNode);

            // Verify that the traversal strategy's apply method was called
            verify(mockTraversalStrategy).apply(testNode, transformRule);
        }

        @Test
        @DisplayName("visit should pass correct parameters to traversal strategy")
        void testVisit_PassesCorrectParameters() {
            TestTreeNode specificNode = new TestTreeNode("specific");

            // Execute visit method with specific node
            transformRule.visit(specificNode);

            // Verify correct parameters were passed
            verify(mockTraversalStrategy).apply(specificNode, transformRule);
            verify(mockTraversalStrategy, never()).apply(testNode, transformRule);
        }

        @Test
        @DisplayName("visit should work with null node")
        void testVisit_WithNullNode() {
            // This should be handled by the traversal strategy
            assertThatCode(() -> transformRule.visit(null))
                    .doesNotThrowAnyException();

            verify(mockTraversalStrategy).apply(null, transformRule);
        }
    }

    @Nested
    @DisplayName("Implementation Variation Tests")
    class ImplementationVariationTests {

        @Test
        @DisplayName("Should work with different transform result types")
        void testWithDifferentTransformResults() {
            TransformRule<TestTreeNode, TestTransformResult> rule1 = createTestTransformRule();

            // Rule that returns stop traversal
            TransformRule<TestTreeNode, TestTransformResult> rule2 = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    return new TestTransformResult(false);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return mockTraversalStrategy;
                }
            };

            TestTransformResult result1 = rule1.apply(testNode, mockQueue);
            TestTransformResult result2 = rule2.apply(testNode, mockQueue);

            assertThat(result1.continueTraversal()).isTrue();
            assertThat(result2.continueTraversal()).isFalse();
        }

        @Test
        @DisplayName("Should work with different traversal strategies")
        void testWithDifferentTraversalStrategies() {
            TraversalStrategy strategy1 = mock(TraversalStrategy.class);
            TraversalStrategy strategy2 = mock(TraversalStrategy.class);

            TransformRule<TestTreeNode, TestTransformResult> rule1 = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return strategy1;
                }
            };

            TransformRule<TestTreeNode, TestTransformResult> rule2 = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return strategy2;
                }
            };

            assertThat(rule1.getTraversalStrategy()).isSameAs(strategy1);
            assertThat(rule2.getTraversalStrategy()).isSameAs(strategy2);

            rule1.visit(testNode);
            rule2.visit(testNode);

            verify(strategy1).apply(testNode, rule1);
            verify(strategy2).apply(testNode, rule2);
        }

        @Test
        @DisplayName("Should support stateful rule implementations")
        void testStatefulRuleImplementations() {
            // Create a stateful rule that counts applications
            TransformRule<TestTreeNode, TestTransformResult> statefulRule = new TransformRule<TestTreeNode, TestTransformResult>() {
                private int applicationCount = 0;

                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    applicationCount++;
                    return new TestTransformResult(applicationCount < 3);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return mockTraversalStrategy;
                }

                @SuppressWarnings("unused")
                public int getApplicationCount() {
                    return applicationCount;
                }
            };

            // Apply multiple times
            TestTransformResult result1 = statefulRule.apply(testNode, mockQueue);
            TestTransformResult result2 = statefulRule.apply(testNode, mockQueue);
            TestTransformResult result3 = statefulRule.apply(testNode, mockQueue);

            assertThat(result1.continueTraversal()).isTrue();
            assertThat(result2.continueTraversal()).isTrue();
            assertThat(result3.continueTraversal()).isFalse();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with real TransformQueue implementation")
        void testWithRealTransformQueue() {
            TransformQueue<TestTreeNode> realQueue = new TransformQueue<>("test");

            TransformRule<TestTreeNode, TestTransformResult> rule = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    // Add a transformation to the queue
                    if (node.toContentString().equals("test")) {
                        TestTreeNode newNode = new TestTreeNode("replaced");
                        queue.replace(node, newNode);
                    }
                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return mockTraversalStrategy;
                }
            };

            TestTransformResult result = rule.apply(testNode, realQueue);

            assertThat(result.continueTraversal()).isTrue();
            assertThat(realQueue.getTransforms()).hasSize(1);
        }

        @Test
        @DisplayName("Should work with tree structure traversal")
        void testWithTreeStructureTraversal() {
            TestTreeNode parent = new TestTreeNode("parent");
            TestTreeNode child1 = new TestTreeNode("child1");
            TestTreeNode child2 = new TestTreeNode("child2");

            parent.addChild(child1);
            parent.addChild(child2);

            TransformRule<TestTreeNode, TestTransformResult> rule = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    // Continue traversal for all nodes
                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return mockTraversalStrategy;
                }
            };

            // Test with different nodes
            assertThat(rule.apply(parent, mockQueue).continueTraversal()).isTrue();
            assertThat(rule.apply(child1, mockQueue).continueTraversal()).isTrue();
            assertThat(rule.apply(child2, mockQueue).continueTraversal()).isTrue();
        }

        @Test
        @DisplayName("Should support complex transformation logic")
        void testComplexTransformationLogic() {
            TransformRule<TestTreeNode, TestTransformResult> complexRule = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    String content = node.toContentString();

                    // Complex logic based on node content and structure
                    if (content.startsWith("remove")) {
                        queue.delete(node);
                        return new TestTransformResult(false); // Stop traversal for removed nodes
                    } else if (content.contains("replace")) {
                        TestTreeNode replacement = new TestTreeNode(content + "_new");
                        queue.replace(node, replacement);
                        return new TestTransformResult(true);
                    } else if (node.getNumChildren() == 0 && content.equals("leaf")) {
                        TestTreeNode newChild = new TestTreeNode("added_child");
                        queue.addChild(node, newChild);
                        return new TestTransformResult(true);
                    }

                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return mockTraversalStrategy;
                }
            };

            TransformQueue<TestTreeNode> queue = new TransformQueue<>("complex");

            TestTreeNode removeNode = new TestTreeNode("remove_this");
            TestTreeNode replaceNode = new TestTreeNode("replace_this");
            TestTreeNode leafNode = new TestTreeNode("leaf");

            TestTransformResult result1 = complexRule.apply(removeNode, queue);
            TestTransformResult result2 = complexRule.apply(replaceNode, queue);
            TestTransformResult result3 = complexRule.apply(leafNode, queue);

            assertThat(result1.continueTraversal()).isFalse();
            assertThat(result2.continueTraversal()).isTrue();
            assertThat(result3.continueTraversal()).isTrue();
            assertThat(queue.getTransforms()).hasSize(3);
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle null parameters gracefully in apply")
        void testApply_WithNullParameters() {
            TransformRule<TestTreeNode, TestTransformResult> rule = createTestTransformRule();

            // Test with null node
            assertThatCode(() -> rule.apply(null, mockQueue))
                    .doesNotThrowAnyException();

            // Test with null queue
            assertThatCode(() -> rule.apply(testNode, null))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle exceptions in apply method gracefully")
        void testApply_WithExceptions() {
            TransformRule<TestTreeNode, TestTransformResult> faultyRule = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    if (node != null && node.toContentString().equals("error")) {
                        throw new RuntimeException("Test exception");
                    }
                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return mockTraversalStrategy;
                }
            };

            TestTreeNode errorNode = new TestTreeNode("error");

            assertThatThrownBy(() -> faultyRule.apply(errorNode, mockQueue))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Test exception");

            // Should work fine with non-error nodes
            assertThatCode(() -> faultyRule.apply(testNode, mockQueue))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null traversal strategy")
        void testWithNullTraversalStrategy() {
            TransformRule<TestTreeNode, TestTransformResult> ruleWithNullStrategy = new TransformRule<TestTreeNode, TestTransformResult>() {
                @Override
                public TestTransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return null;
                }
            };

            assertThat(ruleWithNullStrategy.getTraversalStrategy()).isNull();

            // Visit method should handle null strategy appropriately
            assertThatThrownBy(() -> ruleWithNullStrategy.visit(testNode))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Generic Type Tests")
    class GenericTypeTests {

        @Test
        @DisplayName("Should work with different tree node types")
        void testWithDifferentTreeNodeTypes() {
            // The TransformRule interface should work with any TreeNode implementation
            assertThat(transformRule).isNotNull();
            assertThat(transformRule.apply(testNode, mockQueue)).isInstanceOf(TestTransformResult.class);
        }

        @Test
        @DisplayName("Should work with different transform result types")
        void testWithDifferentTransformResultTypes() {
            // Create a rule with a different result type
            TransformRule<TestTreeNode, TransformResult> genericRule = new TransformRule<TestTreeNode, TransformResult>() {
                @Override
                public TransformResult apply(TestTreeNode node, TransformQueue<TestTreeNode> queue) {
                    return new TestTransformResult(true);
                }

                @Override
                public TraversalStrategy getTraversalStrategy() {
                    return mockTraversalStrategy;
                }
            };

            TransformResult result = genericRule.apply(testNode, mockQueue);
            assertThat(result).isInstanceOf(TransformResult.class);
            assertThat(result.visitChildren()).isTrue();
        }
    }
}
