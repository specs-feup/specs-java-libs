package pt.up.fe.specs.util.graphs;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for GraphSerializable class.
 * 
 * Tests cover:
 * - Serialization and deserialization of various graph structures
 * - Round-trip serialization (serialize then deserialize)
 * - Edge cases and error conditions
 * - Data integrity during serialization/deserialization
 * - Complex graph structures
 * 
 * @author Generated Tests
 */
@DisplayName("GraphSerializable Tests")
class GraphSerializableTest {

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
    @DisplayName("Serialization Tests")
    class SerializationTests {

        @Test
        @DisplayName("Should serialize empty graph")
        void testSerializeEmptyGraph() {
            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);

            assertThat(serializable.operationIds).isEmpty();
            assertThat(serializable.nodeInfos).isEmpty();
            assertThat(serializable.inputIds).isEmpty();
            assertThat(serializable.outputIds).isEmpty();
            assertThat(serializable.connInfos).isEmpty();
        }

        @Test
        @DisplayName("Should serialize single node graph")
        void testSerializeSingleNode() {
            graph.addNode("singleNode", "Single Node Info");

            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);

            assertThat(serializable.operationIds).containsExactly("singleNode");
            assertThat(serializable.nodeInfos).containsExactly("Single Node Info");
            assertThat(serializable.inputIds).isEmpty();
            assertThat(serializable.outputIds).isEmpty();
            assertThat(serializable.connInfos).isEmpty();
        }

        @Test
        @DisplayName("Should serialize simple connected graph")
        void testSerializeSimpleConnectedGraph() {
            graph.addNode("nodeA", "Node A Info");
            graph.addNode("nodeB", "Node B Info");
            graph.addConnection("nodeA", "nodeB", "A to B");

            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);

            assertThat(serializable.operationIds).containsExactly("nodeA", "nodeB");
            assertThat(serializable.nodeInfos).containsExactly("Node A Info", "Node B Info");
            assertThat(serializable.inputIds).containsExactly("nodeA");
            assertThat(serializable.outputIds).containsExactly("nodeB");
            assertThat(serializable.connInfos).containsExactly("A to B");
        }

        @Test
        @DisplayName("Should serialize complex graph with multiple connections")
        void testSerializeComplexGraph() {
            graph.addNode("A", "Node A");
            graph.addNode("B", "Node B");
            graph.addNode("C", "Node C");
            graph.addNode("D", "Node D");

            graph.addConnection("A", "B", "A->B");
            graph.addConnection("A", "C", "A->C");
            graph.addConnection("B", "D", "B->D");
            graph.addConnection("C", "D", "C->D");

            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);

            assertThat(serializable.operationIds).containsExactly("A", "B", "C", "D");
            assertThat(serializable.nodeInfos).containsExactly("Node A", "Node B", "Node C", "Node D");
            assertThat(serializable.inputIds).containsExactly("A", "A", "B", "C");
            assertThat(serializable.outputIds).containsExactly("B", "C", "D", "D");
            assertThat(serializable.connInfos).containsExactly("A->B", "A->C", "B->D", "C->D");
        }

        @Test
        @DisplayName("Should handle nodes with null IDs and info")
        void testSerializeWithNullValues() {
            graph.addNode(null, "Null ID Node");
            graph.addNode("nodeWithNullInfo", null);
            graph.addConnection(null, "nodeWithNullInfo", "null connection");

            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);

            assertThat(serializable.operationIds).containsExactly(null, "nodeWithNullInfo");
            assertThat(serializable.nodeInfos).containsExactly("Null ID Node", null);
            assertThat(serializable.inputIds).containsExactly((String) null);
            assertThat(serializable.outputIds).containsExactly("nodeWithNullInfo");
            assertThat(serializable.connInfos).containsExactly("null connection");
        }
    }

    @Nested
    @DisplayName("Deserialization Tests")
    class DeserializationTests {

        @Test
        @DisplayName("Should deserialize empty graph")
        void testDeserializeEmptyGraph() {
            GraphSerializable<String, String> emptySerializable = new GraphSerializable<>(
                    Arrays.asList(),
                    Arrays.asList(),
                    Arrays.asList(),
                    Arrays.asList(),
                    Arrays.asList());

            TestGraph newGraph = new TestGraph();
            GraphSerializable.fromSerializable(emptySerializable, newGraph);

            assertThat(newGraph.getNodeList()).isEmpty();
            assertThat(newGraph.getGraphNodes()).isEmpty();
        }

        @Test
        @DisplayName("Should deserialize single node graph")
        void testDeserializeSingleNode() {
            GraphSerializable<String, String> singleNodeSerializable = new GraphSerializable<>(
                    Arrays.asList("singleNode"),
                    Arrays.asList("Single Node Info"),
                    Arrays.asList(),
                    Arrays.asList(),
                    Arrays.asList());

            TestGraph newGraph = new TestGraph();
            GraphSerializable.fromSerializable(singleNodeSerializable, newGraph);

            assertThat(newGraph.getNodeList()).hasSize(1);
            TestGraphNode node = newGraph.getNode("singleNode");
            assertThat(node).isNotNull();
            assertThat(node.getNodeInfo()).isEqualTo("Single Node Info");
            assertThat(node.getChildren()).isEmpty();
            assertThat(node.getParents()).isEmpty();
        }

        @Test
        @DisplayName("Should deserialize simple connected graph")
        void testDeserializeSimpleConnectedGraph() {
            GraphSerializable<String, String> connectedSerializable = new GraphSerializable<>(
                    Arrays.asList("nodeA", "nodeB"),
                    Arrays.asList("Node A Info", "Node B Info"),
                    Arrays.asList("nodeA"),
                    Arrays.asList("nodeB"),
                    Arrays.asList("A to B"));

            TestGraph newGraph = new TestGraph();
            GraphSerializable.fromSerializable(connectedSerializable, newGraph);

            assertThat(newGraph.getNodeList()).hasSize(2);

            TestGraphNode nodeA = newGraph.getNode("nodeA");
            TestGraphNode nodeB = newGraph.getNode("nodeB");

            assertThat(nodeA).isNotNull();
            assertThat(nodeB).isNotNull();
            assertThat(nodeA.getNodeInfo()).isEqualTo("Node A Info");
            assertThat(nodeB.getNodeInfo()).isEqualTo("Node B Info");
            assertThat(nodeA.getChildren()).containsExactly(nodeB);
            assertThat(nodeB.getParents()).containsExactly(nodeA);
            assertThat(nodeA.getChildrenConnections()).containsExactly("A to B");
            assertThat(nodeB.getParentConnections()).containsExactly("A to B");
        }

        @Test
        @DisplayName("Should deserialize complex graph")
        void testDeserializeComplexGraph() {
            GraphSerializable<String, String> complexSerializable = new GraphSerializable<>(
                    Arrays.asList("A", "B", "C", "D"),
                    Arrays.asList("Node A", "Node B", "Node C", "Node D"),
                    Arrays.asList("A", "A", "B", "C"),
                    Arrays.asList("B", "C", "D", "D"),
                    Arrays.asList("A->B", "A->C", "B->D", "C->D"));

            TestGraph newGraph = new TestGraph();
            GraphSerializable.fromSerializable(complexSerializable, newGraph);

            assertThat(newGraph.getNodeList()).hasSize(4);

            TestGraphNode nodeA = newGraph.getNode("A");
            TestGraphNode nodeB = newGraph.getNode("B");
            TestGraphNode nodeC = newGraph.getNode("C");
            TestGraphNode nodeD = newGraph.getNode("D");

            assertThat(nodeA.getChildren()).containsExactlyInAnyOrder(nodeB, nodeC);
            assertThat(nodeB.getChildren()).containsExactly(nodeD);
            assertThat(nodeC.getChildren()).containsExactly(nodeD);
            assertThat(nodeD.getParents()).containsExactlyInAnyOrder(nodeB, nodeC);
        }
    }

    @Nested
    @DisplayName("Round-Trip Serialization Tests")
    class RoundTripTests {

        @Test
        @DisplayName("Should preserve graph structure in round-trip serialization")
        void testRoundTripPreservesStructure() {
            // Create original graph
            graph.addNode("root", "Root Node");
            graph.addNode("left", "Left Child");
            graph.addNode("right", "Right Child");
            graph.addNode("leaf", "Leaf Node");

            graph.addConnection("root", "left", "root->left");
            graph.addConnection("root", "right", "root->right");
            graph.addConnection("left", "leaf", "left->leaf");
            graph.addConnection("right", "leaf", "right->leaf");

            // Serialize
            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);

            // Deserialize
            TestGraph newGraph = new TestGraph();
            GraphSerializable.fromSerializable(serializable, newGraph);

            // Verify structure is preserved
            assertThat(newGraph.getNodeList()).hasSize(4);

            TestGraphNode root = newGraph.getNode("root");
            TestGraphNode left = newGraph.getNode("left");
            TestGraphNode right = newGraph.getNode("right");
            TestGraphNode leaf = newGraph.getNode("leaf");

            assertThat(root.getChildren()).containsExactlyInAnyOrder(left, right);
            assertThat(left.getChildren()).containsExactly(leaf);
            assertThat(right.getChildren()).containsExactly(leaf);
            assertThat(leaf.getParents()).containsExactlyInAnyOrder(left, right);

            // Verify connection labels
            assertThat(root.getChildrenConnections()).containsExactlyInAnyOrder("root->left", "root->right");
            assertThat(left.getChildrenConnections()).containsExactly("left->leaf");
            assertThat(right.getChildrenConnections()).containsExactly("right->leaf");
        }

        @Test
        @DisplayName("Should preserve node info in round-trip serialization")
        void testRoundTripPreservesNodeInfo() {
            graph.addNode("node1", "First Node Info");
            graph.addNode("node2", "Second Node Info");
            graph.addConnection("node1", "node2", "connection info");

            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);
            TestGraph newGraph = new TestGraph();
            GraphSerializable.fromSerializable(serializable, newGraph);

            assertThat(newGraph.getNode("node1").getNodeInfo()).isEqualTo("First Node Info");
            assertThat(newGraph.getNode("node2").getNodeInfo()).isEqualTo("Second Node Info");
            assertThat(newGraph.getNode("node1").getChildrenConnections()).containsExactly("connection info");
        }

        @Test
        @DisplayName("Should handle circular graphs in round-trip serialization")
        void testRoundTripCircularGraph() {
            graph.addNode("A", "Node A");
            graph.addNode("B", "Node B");
            graph.addNode("C", "Node C");

            graph.addConnection("A", "B", "A->B");
            graph.addConnection("B", "C", "B->C");
            graph.addConnection("C", "A", "C->A");

            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);
            TestGraph newGraph = new TestGraph();
            GraphSerializable.fromSerializable(serializable, newGraph);

            TestGraphNode nodeA = newGraph.getNode("A");
            TestGraphNode nodeB = newGraph.getNode("B");
            TestGraphNode nodeC = newGraph.getNode("C");

            assertThat(nodeA.getChildren()).containsExactly(nodeB);
            assertThat(nodeB.getChildren()).containsExactly(nodeC);
            assertThat(nodeC.getChildren()).containsExactly(nodeA);

            assertThat(nodeA.getParents()).containsExactly(nodeC);
            assertThat(nodeB.getParents()).containsExactly(nodeA);
            assertThat(nodeC.getParents()).containsExactly(nodeB);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle graph with disconnected components")
        void testDisconnectedComponents() {
            // Create two separate components
            graph.addNode("A1", "Component A1");
            graph.addNode("A2", "Component A2");
            graph.addConnection("A1", "A2", "A1->A2");

            graph.addNode("B1", "Component B1");
            graph.addNode("B2", "Component B2");
            graph.addConnection("B1", "B2", "B1->B2");

            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);
            TestGraph newGraph = new TestGraph();
            GraphSerializable.fromSerializable(serializable, newGraph);

            assertThat(newGraph.getNodeList()).hasSize(4);
            assertThat(newGraph.getNode("A1").getChildren()).containsExactly(newGraph.getNode("A2"));
            assertThat(newGraph.getNode("B1").getChildren()).containsExactly(newGraph.getNode("B2"));
        }

        @Test
        @DisplayName("Should handle self-referencing nodes")
        void testSelfReferencingNodes() {
            graph.addNode("selfRef", "Self Referencing Node");
            graph.addConnection("selfRef", "selfRef", "self loop");

            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);
            TestGraph newGraph = new TestGraph();
            GraphSerializable.fromSerializable(serializable, newGraph);

            TestGraphNode selfRefNode = newGraph.getNode("selfRef");
            assertThat(selfRefNode.getChildren()).containsExactly(selfRefNode);
            assertThat(selfRefNode.getParents()).containsExactly(selfRefNode);
            assertThat(selfRefNode.getChildrenConnections()).containsExactly("self loop");
        }

        @Test
        @DisplayName("Should allow mismatched list sizes in constructor")
        void testMismatchedListSizes() {
            // GraphSerializable constructor doesn't validate list sizes
            // This test verifies that mismatched sizes are allowed
            GraphSerializable<String, String> serializable = new GraphSerializable<>(
                    Arrays.asList("node1"),
                    Arrays.asList("info1", "info2"), // mismatched size - no validation
                    Arrays.asList(),
                    Arrays.asList(),
                    Arrays.asList());

            // Constructor succeeds but usage might be problematic
            assertThat(serializable.operationIds).hasSize(1);
            assertThat(serializable.nodeInfos).hasSize(2);
        }

        @Test
        @DisplayName("Should handle large graphs efficiently")
        void testLargeGraph() {
            // Create a star graph with central hub and 100 spokes
            graph.addNode("hub", "Central Hub");

            for (int i = 0; i < 100; i++) {
                graph.addNode("spoke" + i, "Spoke " + i);
                graph.addConnection("hub", "spoke" + i, "hub->spoke" + i);
            }

            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);
            TestGraph newGraph = new TestGraph();
            GraphSerializable.fromSerializable(serializable, newGraph);

            assertThat(newGraph.getNodeList()).hasSize(101);
            TestGraphNode hub = newGraph.getNode("hub");
            assertThat(hub.getChildren()).hasSize(100);

            for (int i = 0; i < 100; i++) {
                TestGraphNode spoke = newGraph.getNode("spoke" + i);
                assertThat(spoke).isNotNull();
                assertThat(spoke.getParents()).containsExactly(hub);
            }
        }
    }

    @Nested
    @DisplayName("Data Integrity Tests")
    class DataIntegrityTests {

        @Test
        @DisplayName("Should maintain connection order during serialization")
        void testConnectionOrder() {
            graph.addNode("parent", "Parent Node");
            graph.addNode("child1", "Child 1");
            graph.addNode("child2", "Child 2");
            graph.addNode("child3", "Child 3");

            graph.addConnection("parent", "child1", "first");
            graph.addConnection("parent", "child2", "second");
            graph.addConnection("parent", "child3", "third");

            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);

            assertThat(serializable.inputIds).containsExactly("parent", "parent", "parent");
            assertThat(serializable.outputIds).containsExactly("child1", "child2", "child3");
            assertThat(serializable.connInfos).containsExactly("first", "second", "third");
        }

        @Test
        @DisplayName("Should maintain node order during serialization")
        void testNodeOrder() {
            graph.addNode("third", "Third Node");
            graph.addNode("first", "First Node");
            graph.addNode("second", "Second Node");

            GraphSerializable<String, String> serializable = GraphSerializable.toSerializable(graph);

            // Order should be preserved based on insertion order in graph
            assertThat(serializable.operationIds).containsExactly("third", "first", "second");
            assertThat(serializable.nodeInfos).containsExactly("Third Node", "First Node", "Second Node");
        }
    }
}
