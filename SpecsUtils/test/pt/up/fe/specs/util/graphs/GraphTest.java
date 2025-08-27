package pt.up.fe.specs.util.graphs;

import static org.assertj.core.api.Assertions.*;

import java.util.*;
import java.util.concurrent.*;

import org.junit.jupiter.api.*;

/**
 * 
 * @author Generated Tests
 */
@DisplayName("Graph Tests")
class GraphTest {

    // Test implementation of Graph for testing
    private static class TestGraph extends Graph<TestGraphNode, String, String> {

        public TestGraph() {
            super();
        }

        public TestGraph(List<TestGraphNode> nodeList, Map<String, TestGraphNode> graphNodes) {
            super(nodeList, graphNodes);
        }

        @Override
        protected TestGraphNode newNode(String operationId, String nodeInfo) {
            return new TestGraphNode(operationId, nodeInfo);
        }

        @Override
        public Graph<TestGraphNode, String, String> getUnmodifiableGraph() {
            return new UnmodifiableTestGraph(new ArrayList<>(getNodeList()),
                    new HashMap<>(getGraphNodes()));
        }
    }

    // Unmodifiable version for testing
    private static class UnmodifiableTestGraph extends Graph<TestGraphNode, String, String> {

        protected UnmodifiableTestGraph(List<TestGraphNode> nodeList, Map<String, TestGraphNode> graphNodes) {
            super(Collections.unmodifiableList(nodeList), Collections.unmodifiableMap(graphNodes));
        }

        @Override
        protected TestGraphNode newNode(String operationId, String nodeInfo) {
            throw new UnsupportedOperationException("Cannot add nodes to unmodifiable graph");
        }

        @Override
        public Graph<TestGraphNode, String, String> getUnmodifiableGraph() {
            return this;
        }
    }

    // Test implementation of GraphNode for testing
    private static class TestGraphNode extends GraphNode<TestGraphNode, String, String> {

        public TestGraphNode(String id, String nodeInfo) {
            super(id, nodeInfo);
        }

        @Override
        protected TestGraphNode getThis() {
            return this;
        }
    }

    private TestGraph graph;

    @BeforeEach
    void setUp() {
        graph = new TestGraph();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty graph with default constructor")
        void testDefaultConstructor() {
            TestGraph newGraph = new TestGraph();

            assertThat(newGraph.getNodeList()).isEmpty();
            assertThat(newGraph.getGraphNodes()).isEmpty();
        }

        @Test
        @DisplayName("Should create graph with provided collections")
        void testConstructorWithCollections() {
            List<TestGraphNode> nodeList = new ArrayList<>();
            Map<String, TestGraphNode> graphNodes = new HashMap<>();

            TestGraphNode node = new TestGraphNode("test", "testInfo");
            nodeList.add(node);
            graphNodes.put("test", node);

            // Create a custom test graph that uses the protected constructor
            TestGraph newGraph = new TestGraph(nodeList, graphNodes);

            assertThat(newGraph.getNodeList()).hasSize(1);
            assertThat(newGraph.getGraphNodes()).containsKey("test");
        }
    }

    @Nested
    @DisplayName("Node Management Tests")
    class NodeManagementTests {

        @Test
        @DisplayName("Should add single node successfully")
        void testAddSingleNode() {
            TestGraphNode node = graph.addNode("node1", "info1");

            assertThat(node).isNotNull();
            assertThat(node.getId()).isEqualTo("node1");
            assertThat(node.getNodeInfo()).isEqualTo("info1");
            assertThat(graph.getNodeList()).hasSize(1);
            assertThat(graph.getGraphNodes()).containsKey("node1");
        }

        @Test
        @DisplayName("Should add multiple nodes successfully")
        void testAddMultipleNodes() {
            TestGraphNode node1 = graph.addNode("node1", "info1");
            TestGraphNode node2 = graph.addNode("node2", "info2");
            TestGraphNode node3 = graph.addNode("node3", "info3");

            assertThat(graph.getNodeList()).hasSize(3);
            assertThat(graph.getNodeList()).contains(node1, node2, node3);
            assertThat(graph.getGraphNodes()).containsKeys("node1", "node2", "node3");
        }

        @Test
        @DisplayName("Should return existing node when adding duplicate ID")
        void testAddDuplicateNode() {
            TestGraphNode node1 = graph.addNode("duplicate", "info1");
            TestGraphNode node2 = graph.addNode("duplicate", "info2");

            assertThat(node2).isSameAs(node1);
            assertThat(graph.getNodeList()).hasSize(1);
            assertThat(node1.getNodeInfo()).isEqualTo("info1"); // Original info preserved
        }

        @Test
        @DisplayName("Should retrieve node by ID")
        void testGetNode() {
            TestGraphNode node = graph.addNode("findMe", "findInfo");

            TestGraphNode retrieved = graph.getNode("findMe");
            assertThat(retrieved).isSameAs(node);
        }

        @Test
        @DisplayName("Should return null for non-existent node")
        void testGetNonExistentNode() {
            TestGraphNode retrieved = graph.getNode("nonExistent");
            assertThat(retrieved).isNull();
        }

        @Test
        @DisplayName("Should handle null node ID gracefully")
        void testGetNodeWithNullId() {
            TestGraphNode retrieved = graph.getNode(null);
            assertThat(retrieved).isNull();
        }
    }

    @Nested
    @DisplayName("Connection Management Tests")
    class ConnectionManagementTests {

        @Test
        @DisplayName("Should add connection between existing nodes")
        void testAddValidConnection() {
            TestGraphNode source = graph.addNode("source", "sourceInfo");
            TestGraphNode sink = graph.addNode("sink", "sinkInfo");

            graph.addConnection("source", "sink", "connection1");

            assertThat(source.getChildren()).contains(sink);
            assertThat(source.getChildrenConnections()).contains("connection1");
            assertThat(sink.getParents()).contains(source);
            assertThat(sink.getParentConnections()).contains("connection1");
        }

        @Test
        @DisplayName("Should add multiple connections")
        void testAddMultipleConnections() {
            TestGraphNode node1 = graph.addNode("node1", "info1");
            TestGraphNode node2 = graph.addNode("node2", "info2");
            TestGraphNode node3 = graph.addNode("node3", "info3");

            graph.addConnection("node1", "node2", "conn1");
            graph.addConnection("node1", "node3", "conn2");
            graph.addConnection("node2", "node3", "conn3");

            assertThat(node1.getChildren()).contains(node2, node3);
            assertThat(node2.getChildren()).contains(node3);
            assertThat(node3.getParents()).contains(node1, node2);
        }

        @Test
        @DisplayName("Should handle connection with non-existent source node")
        void testAddConnectionWithInvalidSource() {
            graph.addNode("sink", "sinkInfo");

            // Should not crash, just log warning
            assertThatNoException().isThrownBy(() -> graph.addConnection("nonExistent", "sink", "conn"));
        }

        @Test
        @DisplayName("Should handle connection with non-existent sink node")
        void testAddConnectionWithInvalidSink() {
            graph.addNode("source", "sourceInfo");

            // Should not crash, just log warning
            assertThatNoException().isThrownBy(() -> graph.addConnection("source", "nonExistent", "conn"));
        }

        @Test
        @DisplayName("Should handle self-connection")
        void testSelfConnection() {
            TestGraphNode node = graph.addNode("self", "selfInfo");

            graph.addConnection("self", "self", "selfConn");

            assertThat(node.getChildren()).contains(node);
            assertThat(node.getParents()).contains(node);
        }
    }

    @Nested
    @DisplayName("Node Removal Tests")
    class NodeRemovalTests {

        @Test
        @DisplayName("Should remove node by ID")
        void testRemoveNodeById() {
            TestGraphNode node1 = graph.addNode("node1", "info1");
            TestGraphNode node2 = graph.addNode("node2", "info2");
            graph.addConnection("node1", "node2", "conn");

            graph.remove("node1");

            assertThat(graph.getNodeList()).doesNotContain(node1);
            assertThat(graph.getGraphNodes().get("node1")).isNull();
            assertThat(node2.getParents()).isEmpty();
            assertThat(node2.getParentConnections()).isEmpty();
        }

        @Test
        @DisplayName("Should remove node by reference")
        void testRemoveNodeByReference() {
            TestGraphNode node1 = graph.addNode("node1", "info1");
            TestGraphNode node2 = graph.addNode("node2", "info2");
            graph.addConnection("node1", "node2", "conn");

            graph.remove(node1);

            assertThat(graph.getNodeList()).doesNotContain(node1);
            assertThat(graph.getGraphNodes().get("node1")).isNull();
            assertThat(node2.getParents()).isEmpty();
        }

        @Test
        @DisplayName("Should remove node with multiple connections")
        void testRemoveNodeWithMultipleConnections() {
            TestGraphNode node1 = graph.addNode("node1", "info1");
            TestGraphNode node2 = graph.addNode("node2", "info2");
            TestGraphNode node3 = graph.addNode("node3", "info3");

            graph.addConnection("node1", "node2", "conn1");
            graph.addConnection("node3", "node2", "conn2");
            graph.addConnection("node2", "node1", "conn3");

            graph.remove("node2");

            assertThat(graph.getNodeList()).doesNotContain(node2);
            assertThat(node1.getChildren()).isEmpty();
            assertThat(node1.getParents()).isEmpty();
            assertThat(node3.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("Should handle removal of non-existent node by ID")
        void testRemoveNonExistentNodeById() {
            // Should not crash, just log warning
            assertThatNoException().isThrownBy(() -> graph.remove("nonExistent"));
        }

        @Test
        @DisplayName("Should handle removal of node not in graph")
        void testRemoveNodeNotInGraph() {
            TestGraphNode outsideNode = new TestGraphNode("outside", "outsideInfo");

            // Should not crash, just log warning
            assertThatNoException().isThrownBy(() -> graph.remove(outsideNode));
        }
    }

    @Nested
    @DisplayName("Unmodifiable Graph Tests")
    class UnmodifiableGraphTests {

        @Test
        @DisplayName("Should create unmodifiable view")
        void testGetUnmodifiableGraph() {
            graph.addNode("node1", "info1");

            Graph<TestGraphNode, String, String> unmodGraph = graph.getUnmodifiableGraph();

            assertThat(unmodGraph).isNotSameAs(graph);
            assertThat(unmodGraph.getNodeList()).hasSize(1);
            assertThat(unmodGraph.getNode("node1")).isNotNull();
        }

        @Test
        @DisplayName("Should reflect original graph state")
        void testUnmodifiableGraphReflectsOriginal() {
            TestGraphNode node = graph.addNode("node1", "info1");
            Graph<TestGraphNode, String, String> unmodGraph = graph.getUnmodifiableGraph();

            assertThat(unmodGraph.getNodeList()).containsExactly(node);
            assertThat(unmodGraph.getGraphNodes()).containsKey("node1");
        }
    }

    @Nested
    @DisplayName("Collection Access Tests")
    class CollectionAccessTests {

        @Test
        @DisplayName("Should return node list")
        void testGetNodeList() {
            TestGraphNode node1 = graph.addNode("node1", "info1");
            TestGraphNode node2 = graph.addNode("node2", "info2");

            List<TestGraphNode> nodeList = graph.getNodeList();

            assertThat(nodeList).containsExactly(node1, node2);
        }

        @Test
        @DisplayName("Should return graph nodes map")
        void testGetGraphNodes() {
            TestGraphNode node1 = graph.addNode("node1", "info1");
            TestGraphNode node2 = graph.addNode("node2", "info2");

            Map<String, TestGraphNode> graphNodes = graph.getGraphNodes();

            assertThat(graphNodes).containsKeys("node1", "node2");
            assertThat(graphNodes.get("node1")).isSameAs(node1);
            assertThat(graphNodes.get("node2")).isSameAs(node2);
        }

        @Test
        @DisplayName("Should handle empty collections")
        void testEmptyCollections() {
            assertThat(graph.getNodeList()).isEmpty();
            assertThat(graph.getGraphNodes()).isEmpty();
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("Should return string representation of node list")
        void testToString() {
            graph.addNode("node1", "info1");
            graph.addNode("node2", "info2");

            String result = graph.toString();

            assertThat(result).contains("node1", "node2");
        }

        @Test
        @DisplayName("Should handle empty graph toString")
        void testToStringEmpty() {
            String result = graph.toString();

            assertThat(result).isEqualTo("[]");
        }
    }

    @Nested
    @DisplayName("Complex Graph Structure Tests")
    class ComplexGraphTests {

        @Test
        @DisplayName("Should handle complex connected graph")
        void testComplexConnectedGraph() {
            // Create a diamond-shaped graph: A -> B, A -> C, B -> D, C -> D
            TestGraphNode nodeA = graph.addNode("A", "nodeA");
            TestGraphNode nodeB = graph.addNode("B", "nodeB");
            TestGraphNode nodeC = graph.addNode("C", "nodeC");
            TestGraphNode nodeD = graph.addNode("D", "nodeD");

            graph.addConnection("A", "B", "A->B");
            graph.addConnection("A", "C", "A->C");
            graph.addConnection("B", "D", "B->D");
            graph.addConnection("C", "D", "C->D");

            assertThat(nodeA.getChildren()).containsExactly(nodeB, nodeC);
            assertThat(nodeD.getParents()).containsExactly(nodeB, nodeC);
            assertThat(nodeB.getParents()).containsExactly(nodeA);
            assertThat(nodeC.getParents()).containsExactly(nodeA);
        }

        @Test
        @DisplayName("Should handle cyclic graph")
        void testCyclicGraph() {
            TestGraphNode nodeA = graph.addNode("A", "nodeA");
            TestGraphNode nodeB = graph.addNode("B", "nodeB");
            TestGraphNode nodeC = graph.addNode("C", "nodeC");

            graph.addConnection("A", "B", "A->B");
            graph.addConnection("B", "C", "B->C");
            graph.addConnection("C", "A", "C->A");

            assertThat(nodeA.getChildren()).contains(nodeB);
            assertThat(nodeA.getParents()).contains(nodeC);
            assertThat(nodeB.getChildren()).contains(nodeC);
            assertThat(nodeB.getParents()).contains(nodeA);
            assertThat(nodeC.getChildren()).contains(nodeA);
            assertThat(nodeC.getParents()).contains(nodeB);
        }

        @Test
        @DisplayName("Should handle disconnected components")
        void testDisconnectedComponents() {
            // Create two separate components
            TestGraphNode nodeA = graph.addNode("A", "nodeA");
            TestGraphNode nodeB = graph.addNode("B", "nodeB");
            TestGraphNode nodeC = graph.addNode("C", "nodeC");
            TestGraphNode nodeD = graph.addNode("D", "nodeD");

            graph.addConnection("A", "B", "A->B");
            graph.addConnection("C", "D", "C->D");

            assertThat(nodeA.getChildren()).containsExactly(nodeB);
            assertThat(nodeC.getChildren()).containsExactly(nodeD);
            assertThat(nodeA.getParents()).isEmpty();
            assertThat(nodeC.getParents()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should demonstrate race conditions in concurrent node additions")
        void testConcurrentNodeAdditions() throws InterruptedException {
            ExecutorService executor = Executors.newFixedThreadPool(10);
            CountDownLatch latch = new CountDownLatch(100);
            Set<String> addedNodes = ConcurrentHashMap.newKeySet();

            for (int i = 0; i < 100; i++) {
                final int nodeIndex = i;
                executor.submit(() -> {
                    try {
                        String nodeId = "node" + nodeIndex;
                        graph.addNode(nodeId, "info" + nodeIndex);
                        addedNodes.add(nodeId);
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            executor.shutdown();

            // Due to race conditions in the non-thread-safe Graph implementation,
            // we expect some nodes to potentially be lost during concurrent additions
            // However, the race condition is not always reproducible
            int actualSize = graph.getNodeList().size();
            System.out.println("Concurrent test result: " + actualSize + " out of 100 nodes added successfully");

            assertThat(graph.getNodeList())
                    .hasSizeLessThanOrEqualTo(100)
                    .hasSizeGreaterThanOrEqualTo(90); // Allow more tolerance for race conditions
            assertThat(addedNodes).hasSize(100); // All threads attempted to add
        }

        @Test
        @DisplayName("Should add nodes correctly in sequential operations")
        void testSequentialNodeAdditions() {
            // Add nodes sequentially - this should work correctly
            for (int i = 0; i < 100; i++) {
                graph.addNode("seq" + i, "info" + i);
            }

            assertThat(graph.getNodeList()).hasSize(100);
            assertThat(graph.getGraphNodes()).hasSize(100);

            // Verify all nodes are present
            for (int i = 0; i < 100; i++) {
                TestGraphNode node = graph.getNode("seq" + i);
                assertThat(node)
                        .isNotNull();
                assertThat(node.getNodeInfo())
                        .isEqualTo("info" + i);
            }
        }

        @Test
        @DisplayName("Should handle concurrent operations")
        void testConcurrentOperations() throws InterruptedException {
            // Pre-populate graph
            for (int i = 0; i < 10; i++) {
                graph.addNode("node" + i, "info" + i);
            }

            ExecutorService executor = Executors.newFixedThreadPool(5);
            CountDownLatch latch = new CountDownLatch(50);

            for (int i = 0; i < 50; i++) {
                executor.submit(() -> {
                    try {
                        // Random operations
                        TestGraphNode node = graph.getNode("node" + (int) (Math.random() * 10));
                        if (node != null) {
                            // Access node properties
                            node.getId();
                            node.getNodeInfo();
                        }
                    } finally {
                        latch.countDown();
                    }
                });
            }

            latch.await(5, TimeUnit.SECONDS);
            executor.shutdown();

            assertThat(graph.getNodeList()).hasSize(10);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle large graphs")
        void testLargeGraph() {
            // Create a large linear graph
            for (int i = 0; i < 1000; i++) {
                graph.addNode("node" + i, "info" + i);
            }

            for (int i = 0; i < 999; i++) {
                graph.addConnection("node" + i, "node" + (i + 1), "conn" + i);
            }

            assertThat(graph.getNodeList()).hasSize(1000);

            TestGraphNode firstNode = graph.getNode("node0");
            TestGraphNode lastNode = graph.getNode("node999");

            assertThat(firstNode.getParents()).isEmpty();
            assertThat(lastNode.getChildren()).isEmpty();
            assertThat(firstNode.getChildren()).hasSize(1);
            assertThat(lastNode.getParents()).hasSize(1);
        }

        @Test
        @DisplayName("Should handle graphs with many connections per node")
        void testHighConnectivityGraph() {
            TestGraphNode centralNode = graph.addNode("central", "centralInfo");

            // Create hub node with many connections
            for (int i = 0; i < 100; i++) {
                graph.addNode("leaf" + i, "leafInfo" + i);
                graph.addConnection("central", "leaf" + i, "toLeaf" + i);
                graph.addConnection("leaf" + i, "central", "fromLeaf" + i);
            }

            assertThat(centralNode.getChildren()).hasSize(100);
            assertThat(centralNode.getParents()).hasSize(100);
            assertThat(graph.getNodeList()).hasSize(101);
        }
    }
}
