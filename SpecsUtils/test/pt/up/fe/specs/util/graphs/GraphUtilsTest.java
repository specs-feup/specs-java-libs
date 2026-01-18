package pt.up.fe.specs.util.graphs;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for GraphUtils class.
 * 
 * Tests cover:
 * - Parent relationship detection
 * - Edge cases and error conditions
 * - Complex graph structures
 * - Null handling
 * 
 * @author Generated Tests
 */
@DisplayName("GraphUtils Tests")
class GraphUtilsTest {

    // Test implementation of Graph and GraphNode
    private static class TestGraphNode extends GraphNode<TestGraphNode, String, String> {
        public TestGraphNode(String id, String nodeInfo) {
            super(id, nodeInfo);
        }

        @Override
        protected TestGraphNode getThis() {
            return this;
        }
    }

    private static class TestGraph extends Graph<TestGraphNode, String, String> {
        @Override
        protected TestGraphNode newNode(String operationId, String nodeInfo) {
            return new TestGraphNode(operationId, nodeInfo);
        }

        @Override
        public Graph<TestGraphNode, String, String> getUnmodifiableGraph() {
            // For testing purposes, return this
            return this;
        }
    }

    private TestGraph graph;

    @BeforeEach
    void setUp() {
        graph = new TestGraph();
        graph.addNode("parent", "parentInfo");
        graph.addNode("child", "childInfo");
        graph.addNode("grandchild", "grandChildInfo");
        graph.addNode("unrelated", "unrelatedInfo");

        // Set up basic parent-child relationship
        graph.addConnection("parent", "child", "parentToChild");
        graph.addConnection("child", "grandchild", "childToGrandChild");
    }

    @Nested
    @DisplayName("Parent Detection Tests")
    class ParentDetectionTests {

        @Test
        @DisplayName("Should detect direct parent relationship")
        void testDirectParentRelationship() {
            boolean isParent = GraphUtils.isParent(graph, "parent", "child");

            assertThat(isParent).isTrue();
        }

        @Test
        @DisplayName("Should return false for non-parent relationship")
        void testNonParentRelationship() {
            boolean isParent = GraphUtils.isParent(graph, "child", "parent");

            assertThat(isParent).isFalse();
        }

        @Test
        @DisplayName("Should return false for unrelated nodes")
        void testUnrelatedNodes() {
            boolean isParent = GraphUtils.isParent(graph, "unrelated", "child");

            assertThat(isParent).isFalse();
        }

        @Test
        @DisplayName("Should return false for grandparent-grandchild relationship")
        void testGrandparentRelationship() {
            // Grandparent is not considered a direct parent
            boolean isParent = GraphUtils.isParent(graph, "parent", "grandchild");

            assertThat(isParent).isFalse();
        }

        @Test
        @DisplayName("Should return false for self-relationship")
        void testSelfRelationship() {
            boolean isParent = GraphUtils.isParent(graph, "parent", "parent");

            assertThat(isParent).isFalse();
        }

        @Test
        @DisplayName("Should detect parent in multiple parent scenario")
        void testMultipleParents() {
            graph.addNode("secondParent", "secondParentInfo");
            graph.addConnection("secondParent", "child", "secondParentToChild");

            boolean isFirstParent = GraphUtils.isParent(graph, "parent", "child");
            boolean isSecondParent = GraphUtils.isParent(graph, "secondParent", "child");
            boolean isNotParent = GraphUtils.isParent(graph, "unrelated", "child");

            assertThat(isFirstParent).isTrue();
            assertThat(isSecondParent).isTrue();
            assertThat(isNotParent).isFalse();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should throw exception for non-existent child node")
        void testNonExistentChildNode() {
            assertThatThrownBy(() -> GraphUtils.isParent(graph, "parent", "nonExistent"))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle non-existent parent id gracefully")
        void testNonExistentParentId() {
            // Since we only check the parent IDs in the child's parent list,
            // a non-existent parent ID should simply return false
            boolean isParent = GraphUtils.isParent(graph, "nonExistent", "child");

            assertThat(isParent).isFalse();
        }

        @Test
        @DisplayName("Should handle empty strings as node IDs")
        void testEmptyStringIds() {
            graph.addNode("", "emptyParentInfo");
            graph.addNode("emptyChild", "emptyChildInfo");
            graph.addConnection("", "emptyChild", "emptyConnection");

            boolean isParent = GraphUtils.isParent(graph, "", "emptyChild");

            assertThat(isParent).isTrue();
        }

        @Test
        @DisplayName("Should handle null node IDs gracefully")
        void testNullNodeIds() {
            graph.addNode(null, "nullParentInfo");
            graph.addNode("nullChild", "nullChildInfo");
            graph.addConnection(null, "nullChild", "nullConnection");

            // Should return false when checking for null parent ID
            boolean isParent = GraphUtils.isParent(graph, null, "nullChild");
            assertThat(isParent).isFalse();

            // Should return false when checking for non-null parent ID against null parent
            boolean isParent2 = GraphUtils.isParent(graph, "someParent", "nullChild");
            assertThat(isParent2).isFalse();
        }

        @Test
        @DisplayName("Should handle node with no parents")
        void testNodeWithNoParents() {
            graph.addNode("isolated", "isolatedInfo");

            boolean isParent = GraphUtils.isParent(graph, "parent", "isolated");

            assertThat(isParent).isFalse();
        }
    }

    @Nested
    @DisplayName("Complex Graph Structure Tests")
    class ComplexGraphTests {

        @Test
        @DisplayName("Should work with circular relationships")
        void testCircularRelationships() {
            // Create circular relationship: A -> B -> C -> A
            graph.addNode("A", "infoA");
            graph.addNode("B", "infoB");
            graph.addNode("C", "infoC");

            graph.addConnection("A", "B", "AtoB");
            graph.addConnection("B", "C", "BtoC");
            graph.addConnection("C", "A", "CtoA");

            assertThat(GraphUtils.isParent(graph, "A", "B")).isTrue();
            assertThat(GraphUtils.isParent(graph, "B", "C")).isTrue();
            assertThat(GraphUtils.isParent(graph, "C", "A")).isTrue();

            // Verify non-direct relationships are false
            assertThat(GraphUtils.isParent(graph, "A", "C")).isFalse();
            assertThat(GraphUtils.isParent(graph, "B", "A")).isFalse();
            assertThat(GraphUtils.isParent(graph, "C", "B")).isFalse();
        }

        @Test
        @DisplayName("Should work with diamond dependency structure")
        void testDiamondDependency() {
            // Create diamond: root -> A, B -> A, B -> leaf
            graph.addNode("root", "rootInfo");
            graph.addNode("nodeA", "infoA");
            graph.addNode("nodeB", "infoB");
            graph.addNode("leaf", "leafInfo");

            graph.addConnection("root", "nodeA", "rootToA");
            graph.addConnection("root", "nodeB", "rootToB");
            graph.addConnection("nodeA", "leaf", "AtoLeaf");
            graph.addConnection("nodeB", "leaf", "BtoLeaf");

            assertThat(GraphUtils.isParent(graph, "root", "nodeA")).isTrue();
            assertThat(GraphUtils.isParent(graph, "root", "nodeB")).isTrue();
            assertThat(GraphUtils.isParent(graph, "nodeA", "leaf")).isTrue();
            assertThat(GraphUtils.isParent(graph, "nodeB", "leaf")).isTrue();

            // Verify non-direct relationships
            assertThat(GraphUtils.isParent(graph, "root", "leaf")).isFalse();
            assertThat(GraphUtils.isParent(graph, "nodeA", "nodeB")).isFalse();
        }

        @Test
        @DisplayName("Should work with deeply nested hierarchy")
        void testDeeplyNestedHierarchy() {
            // Create a chain: level0 -> level1 -> level2 -> ... -> level5
            for (int i = 0; i < 6; i++) {
                graph.addNode("level" + i, "levelInfo" + i);
                if (i > 0) {
                    graph.addConnection("level" + (i - 1), "level" + i, "connection" + i);
                }
            }

            // Test direct parent relationships
            for (int i = 1; i < 6; i++) {
                assertThat(GraphUtils.isParent(graph, "level" + (i - 1), "level" + i))
                        .as("level%d should be parent of level%d", i - 1, i)
                        .isTrue();
            }

            // Test non-direct relationships (should be false)
            assertThat(GraphUtils.isParent(graph, "level0", "level2")).isFalse();
            assertThat(GraphUtils.isParent(graph, "level1", "level3")).isFalse();
            assertThat(GraphUtils.isParent(graph, "level0", "level5")).isFalse();
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle large number of parents efficiently")
        void testLargeNumberOfParents() {
            graph.addNode("manyParentsChild", "childInfo");

            // Add 1000 parents to one child
            for (int i = 0; i < 1000; i++) {
                graph.addNode("parent" + i, "parentInfo" + i);
                graph.addConnection("parent" + i, "manyParentsChild", "connection" + i);
            }

            // Test finding parent in the middle
            boolean foundMiddle = GraphUtils.isParent(graph, "parent500", "manyParentsChild");
            assertThat(foundMiddle).isTrue();

            // Test finding last parent
            boolean foundLast = GraphUtils.isParent(graph, "parent999", "manyParentsChild");
            assertThat(foundLast).isTrue();

            // Test non-existent parent
            boolean foundNonExistent = GraphUtils.isParent(graph, "parent1000", "manyParentsChild");
            assertThat(foundNonExistent).isFalse();
        }
    }
}
