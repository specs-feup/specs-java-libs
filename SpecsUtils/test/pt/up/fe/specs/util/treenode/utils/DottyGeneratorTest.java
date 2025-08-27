package pt.up.fe.specs.util.treenode.utils;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;
import pt.up.fe.specs.util.treenode.ATreeNode;

/**
 * Comprehensive test suite for DottyGenerator utility class.
 * Tests DOT file generation for tree visualization.
 * 
 * @author Generated Tests
 */
@DisplayName("DottyGenerator Tests")
class DottyGeneratorTest {

    private TestTreeNode root;
    private TestTreeNode child1;
    private TestTreeNode child2;
    private TestTreeNode grandchild1;

    @BeforeEach
    void setUp() {
        // Create test tree structure:
        //     root
        //    /    \
        //  child1  child2
        //  /
        // grandchild1
        grandchild1 = new TestTreeNode("grandchild1");
        child1 = new TestTreeNode("child1", Collections.singletonList(grandchild1));
        child2 = new TestTreeNode("child2");
        root = new TestTreeNode("root", Arrays.asList(child1, child2));
    }

    @Nested
    @DisplayName("Basic DOT Generation Tests")
    class BasicDotGenerationTests {

        @Test
        @DisplayName("buildDotty() should generate valid DOT format for single node")
        void testBuildDotty_SingleNode_GeneratesValidDot() {
            TestTreeNode singleNode = new TestTreeNode("single");

            String dotty = DottyGenerator.buildDotty(singleNode);

            assertThat(dotty).isNotNull();
            assertThat(dotty).startsWith("digraph D {");
            assertThat(dotty).endsWith("}\n");
            assertThat(dotty).contains("single");
            assertThat(dotty).contains("[shape = box, label = \"single\"]");
        }

        @Test
        @DisplayName("buildDotty() should generate DOT format for tree structure")
        void testBuildDotty_TreeStructure_GeneratesValidDot() {
            String dotty = DottyGenerator.buildDotty(root);

            assertThat(dotty).isNotNull();
            assertThat(dotty).startsWith("digraph D {");
            assertThat(dotty).endsWith("}\n");

            // Should contain all node labels
            assertThat(dotty).contains("root");
            assertThat(dotty).contains("child1");
            assertThat(dotty).contains("child2");
            assertThat(dotty).contains("grandchild1");

            // Should contain shape and label declarations
            assertThat(dotty).contains("[shape = box, label = \"root\"]");
            assertThat(dotty).contains("[shape = box, label = \"child1\"]");
            assertThat(dotty).contains("[shape = box, label = \"child2\"]");
            assertThat(dotty).contains("[shape = box, label = \"grandchild1\"]");
        }

        @Test
        @DisplayName("buildDotty() should generate edges between nodes")
        void testBuildDotty_GeneratesEdges() {
            String dotty = DottyGenerator.buildDotty(root);

            // Should contain arrows indicating parent-child relationships
            // The exact format uses hashcodes, so we check for arrow patterns
            assertThat(dotty).containsPattern("\\d+ -> \\d+");

            // Count the number of edges (should be 3 for our tree: root->child1,
            // root->child2, child1->grandchild1)
            long edgeCount = dotty.lines()
                    .filter(line -> line.contains(" -> "))
                    .count();
            assertThat(edgeCount).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("DOT Content Validation Tests")
    class DotContentValidationTests {

        @Test
        @DisplayName("Generated DOT should have unique node identifiers")
        void testGeneratedDot_HasUniqueNodeIdentifiers() {
            String dotty = DottyGenerator.buildDotty(root);

            // Extract all node identifiers (numbers before [shape)
            List<String> nodeIds = dotty.lines()
                    .filter(line -> line.contains("[shape = box"))
                    .map(line -> line.substring(0, line.indexOf("[")))
                    .collect(java.util.stream.Collectors.toList());

            // All node IDs should be unique
            Set<String> uniqueIds = new HashSet<>(nodeIds);
            assertThat(uniqueIds).hasSize(nodeIds.size());
            assertThat(nodeIds).hasSize(4); // root, child1, child2, grandchild1
        }

        @Test
        @DisplayName("Generated DOT should handle newlines in content")
        void testGeneratedDot_HandlesNewlinesInContent() {
            TestTreeNode nodeWithNewlines = new TestTreeNode("line1\nline2\nline3");

            String dotty = DottyGenerator.buildDotty(nodeWithNewlines);

            // Newlines should be escaped as \\l in DOT format
            assertThat(dotty).contains("line1\\lline2\\lline3");
            assertThat(dotty).doesNotContain("line1\nline2"); // Should not contain actual newlines
        }

        @Test
        @DisplayName("Generated DOT should handle empty content")
        void testGeneratedDot_HandlesEmptyContent() {
            TestTreeNode nodeWithEmptyContent = new TestTreeNode("");

            String dotty = DottyGenerator.buildDotty(nodeWithEmptyContent);

            // Should fall back to node name when content is blank
            assertThat(dotty).contains("TestTreeNode");
        }

        @Test
        @DisplayName("Generated DOT should handle blank content")
        void testGeneratedDot_HandlesBlankContent() {
            TestTreeNode nodeWithBlankContent = new TestTreeNode("   ");

            String dotty = DottyGenerator.buildDotty(nodeWithBlankContent);

            // Should fall back to node name when content is blank
            assertThat(dotty).contains("TestTreeNode");
        }
    }

    @Nested
    @DisplayName("DOT Structure Validation Tests")
    class DotStructureValidationTests {

        @Test
        @DisplayName("Generated DOT should have correct hierarchical structure")
        void testGeneratedDot_HasCorrectHierarchicalStructure() {
            String dotty = DottyGenerator.buildDotty(root);

            // Extract edges to verify structure
            List<String> edges = dotty.lines()
                    .filter(line -> line.contains(" -> "))
                    .map(String::trim)
                    .collect(java.util.stream.Collectors.toList());

            assertThat(edges).hasSize(3);

            // Verify that each edge connects valid nodes
            for (String edge : edges) {
                String[] parts = edge.split(" -> ");
                assertThat(parts).hasSize(2);

                String sourceId = parts[0];
                String targetId = parts[1];

                // Both should be numeric (hashcodes)
                assertThat(sourceId).matches("\\d+");
                assertThat(targetId).matches("\\d+;");
            }
        }

        @Test
        @DisplayName("Generated DOT should be valid Graphviz syntax")
        void testGeneratedDot_IsValidGraphvizSyntax() {
            String dotty = DottyGenerator.buildDotty(root);

            // Basic syntax validation
            assertThat(dotty).startsWith("digraph D {");
            assertThat(dotty).endsWith("}\n");

            // Should not have unmatched braces
            long openBraces = dotty.chars().filter(ch -> ch == '{').count();
            long closeBraces = dotty.chars().filter(ch -> ch == '}').count();
            assertThat(openBraces).isEqualTo(closeBraces);

            // All statements should end properly
            String[] lines = dotty.split("\n");
            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty() && !line.equals("digraph D {") && !line.equals("}")) {
                    assertThat(line).endsWith(";");
                }
            }
        }

        @Test
        @DisplayName("Generated DOT should handle deep tree structures")
        void testGeneratedDot_HandlesDeepTreeStructures() {
            // Create a deeper tree: root -> child -> grandchild -> greatgrandchild
            TestTreeNode greatGrandchild = new TestTreeNode("greatgrandchild");
            TestTreeNode deepGrandchild = new TestTreeNode("deepgrandchild",
                    Collections.singletonList(greatGrandchild));
            TestTreeNode deepChild = new TestTreeNode("deepchild", Collections.singletonList(deepGrandchild));
            TestTreeNode deepRoot = new TestTreeNode("deeproot", Collections.singletonList(deepChild));

            String dotty = DottyGenerator.buildDotty(deepRoot);

            assertThat(dotty).contains("deeproot");
            assertThat(dotty).contains("deepchild");
            assertThat(dotty).contains("deepgrandchild");
            assertThat(dotty).contains("greatgrandchild");

            // Should have 3 edges in the chain
            long edgeCount = dotty.lines()
                    .filter(line -> line.contains(" -> "))
                    .count();
            assertThat(edgeCount).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("DottyGenerator Static Method Tests")
    class DottyGeneratorStaticMethodTests {

        @Test
        @DisplayName("buildDotty() static method should work correctly")
        void testBuildDottyStaticMethod_WorksCorrectly() {
            String dotty = DottyGenerator.buildDotty(root);

            assertThat(dotty).isNotNull();
            assertThat(dotty).startsWith("digraph D {");
            assertThat(dotty).endsWith("}\n");
            assertThat(dotty).contains("root");
        }

        @Test
        @DisplayName("buildDotty() should produce consistent output for same tree")
        void testBuildDotty_ProducesConsistentOutput() {
            String dotty1 = DottyGenerator.buildDotty(root);
            String dotty2 = DottyGenerator.buildDotty(root);

            // Content should be the same (though hashcodes might differ between runs)
            // We check structure rather than exact equality
            assertThat(dotty1).hasLineCount(dotty2.lines().toArray().length);
            assertThat(dotty1).contains("root");
            assertThat(dotty2).contains("root");
            assertThat(dotty1).startsWith("digraph D {");
            assertThat(dotty2).startsWith("digraph D {");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("buildDotty() should handle null content gracefully")
        void testBuildDotty_HandlesNullContentGracefully() {
            TestTreeNode nodeWithNullContent = new TestTreeNode(null);

            assertThatCode(() -> DottyGenerator.buildDotty(nodeWithNullContent))
                    .doesNotThrowAnyException();

            String dotty = DottyGenerator.buildDotty(nodeWithNullContent);
            assertThat(dotty).isNotNull();
            assertThat(dotty).contains("TestTreeNode"); // Should fall back to class name
        }

        @Test
        @DisplayName("buildDotty() should handle special characters in content")
        void testBuildDotty_HandlesSpecialCharacters() {
            TestTreeNode nodeWithSpecialChars = new TestTreeNode("node\"with'special<chars>");

            String dotty = DottyGenerator.buildDotty(nodeWithSpecialChars);

            assertThat(dotty).isNotNull();
            assertThat(dotty).contains("node\"with'special<chars>");
            // Note: The current implementation doesn't escape these characters,
            // which might be a potential improvement area
        }

        @Test
        @DisplayName("buildDotty() should handle large trees efficiently")
        void testBuildDotty_HandlesLargeTreesEfficiently() {
            // Create a tree with many nodes
            TestTreeNode largeRoot = new TestTreeNode("largeRoot");
            for (int i = 0; i < 100; i++) {
                largeRoot.addChild(new TestTreeNode("child" + i));
            }

            long startTime = System.currentTimeMillis();
            String dotty = DottyGenerator.buildDotty(largeRoot);
            long endTime = System.currentTimeMillis();

            assertThat(dotty).isNotNull();
            assertThat(dotty).contains("largeRoot");
            assertThat(dotty).contains("child0");
            assertThat(dotty).contains("child99");

            // Should complete in reasonable time (less than 1 second)
            assertThat(endTime - startTime).isLessThan(1000);
        }
    }

    /**
     * Test implementation of TreeNode for testing DottyGenerator
     */
    private static class TestTreeNode extends ATreeNode<TestTreeNode> {
        private final String content;

        public TestTreeNode(String content) {
            super(Collections.emptyList());
            this.content = content;
        }

        public TestTreeNode(String content, Collection<? extends TestTreeNode> children) {
            super(children);
            this.content = content;
        }

        @Override
        public String toContentString() {
            return content;
        }

        @Override
        public String getNodeName() {
            return "TestTreeNode";
        }

        @Override
        protected TestTreeNode copyPrivate() {
            return new TestTreeNode(content);
        }

        @Override
        public TestTreeNode copy() {
            List<TestTreeNode> childrenCopy = new ArrayList<>();
            for (TestTreeNode child : getChildren()) {
                childrenCopy.add(child.copy());
            }
            return new TestTreeNode(content, childrenCopy);
        }

        @Override
        public String toString() {
            return "TestTreeNode{content='" + content + "', children=" + getNumChildren() + "}";
        }
    }
}
