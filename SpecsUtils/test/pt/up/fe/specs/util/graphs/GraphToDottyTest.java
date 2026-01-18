package pt.up.fe.specs.util.graphs;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for GraphToDotty class.
 * 
 * Tests cover:
 * - DOT format generation for various graph structures
 * - Node declarations with different node info types
 * - Connection generation with various connection labels
 * - Edge cases and error conditions
 * - Complex graph structures (circular, hierarchical, etc.)
 * 
 * @author Generated Tests
 */
@DisplayName("GraphToDotty Tests")
class GraphToDottyTest {

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
            return this;
        }
    }

    private TestGraph graph;

    @BeforeEach
    void setUp() {
        graph = new TestGraph();
    }

    @Nested
    @DisplayName("DOT Format Generation Tests")
    class DotFormatTests {

        @Test
        @DisplayName("Should generate DOT format for empty graph")
        void testEmptyGraph() {
            String dotOutput = GraphToDotty.getDotty(graph);

            assertThat(dotOutput)
                    .isNotNull()
                    .contains("digraph")
                    .contains("{")
                    .contains("}");
        }

        @Test
        @DisplayName("Should generate DOT format for single node")
        void testSingleNode() {
            graph.addNode("singleNode", "Single Node Info");

            String dotOutput = GraphToDotty.getDotty(graph);

            assertThat(dotOutput)
                    .contains("singleNode")
                    .contains("Single Node Info")
                    .contains("digraph")
                    .contains("{")
                    .contains("}");
        }

        @Test
        @DisplayName("Should generate DOT format for simple graph with connection")
        void testSimpleGraphWithConnection() {
            graph.addNode("nodeA", "Node A Info");
            graph.addNode("nodeB", "Node B Info");
            graph.addConnection("nodeA", "nodeB", "A to B");

            String dotOutput = GraphToDotty.getDotty(graph);

            assertThat(dotOutput)
                    .contains("nodeA")
                    .contains("nodeB")
                    .contains("Node A Info")
                    .contains("Node B Info")
                    .contains("A to B")
                    .contains("->"); // DOT connection syntax
        }

        @Test
        @DisplayName("Should generate DOT format for complex graph")
        void testComplexGraph() {
            // Create a more complex graph: A -> B, A -> C, B -> D, C -> D
            graph.addNode("A", "Root Node");
            graph.addNode("B", "Left Branch");
            graph.addNode("C", "Right Branch");
            graph.addNode("D", "Leaf Node");

            graph.addConnection("A", "B", "left");
            graph.addConnection("A", "C", "right");
            graph.addConnection("B", "D", "leftToLeaf");
            graph.addConnection("C", "D", "rightToLeaf");

            String dotOutput = GraphToDotty.getDotty(graph);

            assertThat(dotOutput)
                    .contains("A")
                    .contains("B")
                    .contains("C")
                    .contains("D")
                    .contains("Root Node")
                    .contains("Left Branch")
                    .contains("Right Branch")
                    .contains("Leaf Node")
                    .contains("left")
                    .contains("right")
                    .contains("leftToLeaf")
                    .contains("rightToLeaf");
        }
    }

    @Nested
    @DisplayName("Node Declaration Tests")
    class NodeDeclarationTests {

        @Test
        @DisplayName("Should generate proper node declaration")
        void testNodeDeclaration() {
            TestGraphNode node = graph.addNode("testNode", "Test Node Info");

            String declaration = GraphToDotty.getDeclaration(node);

            assertThat(declaration)
                    .contains("testNode")
                    .contains("Test Node Info")
                    .contains("box")
                    .contains("white");
        }

        @Test
        @DisplayName("Should handle nodes with special characters in info")
        void testNodeWithSpecialCharacters() {
            TestGraphNode node = graph.addNode("specialNode", "Node with \"quotes\" and \\slashes\\");

            String declaration = GraphToDotty.getDeclaration(node);

            assertThat(declaration)
                    .contains("specialNode")
                    .contains("Node with");
        }

        @Test
        @DisplayName("Should handle nodes with empty info")
        void testNodeWithEmptyInfo() {
            TestGraphNode node = graph.addNode("emptyInfoNode", "");

            String declaration = GraphToDotty.getDeclaration(node);

            assertThat(declaration)
                    .contains("emptyInfoNode");
        }

        @Test
        @DisplayName("Should handle nodes with null info")
        void testNodeWithNullInfo() {
            TestGraphNode node = graph.addNode("nullInfoNode", null);

            // This will likely throw NPE due to nodeInfo.toString()
            assertThatThrownBy(() -> GraphToDotty.getDeclaration(node))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should throw exception for nodes with null ID")
        void testNodeWithNullId() {
            TestGraphNode node = graph.addNode(null, "Node with null ID");

            // GraphToDotty has a bug - it throws NPE for null IDs
            assertThatThrownBy(() -> GraphToDotty.getDeclaration(node))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Connection Generation Tests")
    class ConnectionTests {

        @Test
        @DisplayName("Should generate proper connection")
        void testConnectionGeneration() {
            TestGraphNode nodeA = graph.addNode("nodeA", "Node A");
            graph.addNode("nodeB", "Node B");
            graph.addConnection("nodeA", "nodeB", "connection label");

            String connection = GraphToDotty.getConnection(nodeA, 0);

            assertThat(connection)
                    .contains("nodeA")
                    .contains("nodeB")
                    .contains("connection label");
        }

        @Test
        @DisplayName("Should generate connection with empty label")
        void testConnectionWithEmptyLabel() {
            TestGraphNode nodeA = graph.addNode("nodeA", "Node A");
            graph.addNode("nodeB", "Node B");
            graph.addConnection("nodeA", "nodeB", "");

            String connection = GraphToDotty.getConnection(nodeA, 0);

            assertThat(connection)
                    .contains("nodeA")
                    .contains("nodeB");
        }

        @Test
        @DisplayName("Should handle connection with null label")
        void testConnectionWithNullLabel() {
            TestGraphNode nodeA = graph.addNode("nodeA", "Node A");
            graph.addNode("nodeB", "Node B");
            graph.addConnection("nodeA", "nodeB", null);

            // This will likely throw NPE due to toString() on null connection
            assertThatThrownBy(() -> GraphToDotty.getConnection(nodeA, 0))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle multiple connections from same node")
        void testMultipleConnections() {
            TestGraphNode nodeA = graph.addNode("nodeA", "Node A");
            graph.addNode("nodeB", "Node B");
            graph.addNode("nodeC", "Node C");

            graph.addConnection("nodeA", "nodeB", "first connection");
            graph.addConnection("nodeA", "nodeC", "second connection");

            String firstConnection = GraphToDotty.getConnection(nodeA, 0);
            String secondConnection = GraphToDotty.getConnection(nodeA, 1);

            assertThat(firstConnection)
                    .contains("nodeA")
                    .contains("nodeB")
                    .contains("first connection");

            assertThat(secondConnection)
                    .contains("nodeA")
                    .contains("nodeC")
                    .contains("second connection");
        }

        @Test
        @DisplayName("Should throw exception for invalid connection index")
        void testInvalidConnectionIndex() {
            TestGraphNode nodeA = graph.addNode("nodeA", "Node A");

            assertThatThrownBy(() -> GraphToDotty.getConnection(nodeA, 0))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("Complex Graph Structure Tests")
    class ComplexGraphTests {

        @Test
        @DisplayName("Should handle circular graph")
        void testCircularGraph() {
            graph.addNode("A", "Node A");
            graph.addNode("B", "Node B");
            graph.addNode("C", "Node C");

            graph.addConnection("A", "B", "A->B");
            graph.addConnection("B", "C", "B->C");
            graph.addConnection("C", "A", "C->A");

            String dotOutput = GraphToDotty.getDotty(graph);

            assertThat(dotOutput)
                    .contains("A")
                    .contains("B")
                    .contains("C")
                    .contains("A->B")
                    .contains("B->C")
                    .contains("C->A");
        }

        @Test
        @DisplayName("Should handle self-referencing node")
        void testSelfReferencingNode() {
            graph.addNode("selfRef", "Self Referencing Node");
            graph.addConnection("selfRef", "selfRef", "self loop");

            String dotOutput = GraphToDotty.getDotty(graph);

            assertThat(dotOutput)
                    .contains("selfRef")
                    .contains("Self Referencing Node")
                    .contains("self loop");
        }

        @Test
        @DisplayName("Should handle deeply nested hierarchy")
        void testDeeplyNestedHierarchy() {
            // Create chain: level0 -> level1 -> level2 -> level3 -> level4
            for (int i = 0; i < 5; i++) {
                graph.addNode("level" + i, "Level " + i + " Node");
                if (i > 0) {
                    graph.addConnection("level" + (i - 1), "level" + i, "level" + (i - 1) + "->level" + i);
                }
            }

            String dotOutput = GraphToDotty.getDotty(graph);

            for (int i = 0; i < 5; i++) {
                assertThat(dotOutput)
                        .contains("level" + i)
                        .contains("Level " + i + " Node");

                if (i > 0) {
                    assertThat(dotOutput)
                            .contains("level" + (i - 1) + "->level" + i);
                }
            }
        }

        @Test
        @DisplayName("Should handle diamond dependency structure")
        void testDiamondDependency() {
            // Create diamond: root -> left, right -> left, right -> leaf
            graph.addNode("root", "Root Node");
            graph.addNode("left", "Left Node");
            graph.addNode("right", "Right Node");
            graph.addNode("leaf", "Leaf Node");

            graph.addConnection("root", "left", "root->left");
            graph.addConnection("root", "right", "root->right");
            graph.addConnection("left", "leaf", "left->leaf");
            graph.addConnection("right", "leaf", "right->leaf");

            String dotOutput = GraphToDotty.getDotty(graph);

            assertThat(dotOutput)
                    .contains("root")
                    .contains("left")
                    .contains("right")
                    .contains("leaf")
                    .contains("root->left")
                    .contains("root->right")
                    .contains("left->leaf")
                    .contains("right->leaf");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle graph with disconnected components")
        void testDisconnectedComponents() {
            // Create two separate components
            graph.addNode("A1", "Component A Node 1");
            graph.addNode("A2", "Component A Node 2");
            graph.addConnection("A1", "A2", "A1->A2");

            graph.addNode("B1", "Component B Node 1");
            graph.addNode("B2", "Component B Node 2");
            graph.addConnection("B1", "B2", "B1->B2");

            String dotOutput = GraphToDotty.getDotty(graph);

            assertThat(dotOutput)
                    .contains("A1")
                    .contains("A2")
                    .contains("B1")
                    .contains("B2")
                    .contains("A1->A2")
                    .contains("B1->B2");
        }

        @Test
        @DisplayName("Should handle nodes with unusual IDs")
        void testUnusualNodeIds() {
            // Test with various unusual but valid IDs
            graph.addNode("123", "Numeric ID");
            graph.addNode("node-with-dashes", "Dashed ID");
            graph.addNode("node_with_underscores", "Underscored ID");
            graph.addNode("MixedCaseNode", "Mixed Case ID");

            String dotOutput = GraphToDotty.getDotty(graph);

            assertThat(dotOutput)
                    .contains("123")
                    .contains("node-with-dashes")
                    .contains("node_with_underscores")
                    .contains("MixedCaseNode");
        }

        @Test
        @DisplayName("Should handle large graph efficiently")
        void testLargeGraph() {
            // Create a star graph with central hub and many spokes
            graph.addNode("hub", "Central Hub");

            for (int i = 0; i < 100; i++) {
                graph.addNode("spoke" + i, "Spoke " + i);
                graph.addConnection("hub", "spoke" + i, "hub->spoke" + i);
            }

            String dotOutput = GraphToDotty.getDotty(graph);

            assertThat(dotOutput)
                    .contains("hub")
                    .contains("Central Hub");

            // Check a few random spokes
            assertThat(dotOutput)
                    .contains("spoke0")
                    .contains("spoke50")
                    .contains("spoke99");
        }
    }
}
