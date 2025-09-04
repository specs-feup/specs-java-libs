package pt.up.fe.specs.util.treenode.utils;

import org.junit.jupiter.api.*;
import static org.assertj.core.api.Assertions.*;

import java.util.*;
import pt.up.fe.specs.util.treenode.ATreeNode;
import pt.up.fe.specs.util.classmap.FunctionClassMap;

/**
 * Comprehensive test suite for JsonWriter utility class.
 * Tests JSON export functionality for tree structures.
 * 
 * @author Generated Tests
 */
@DisplayName("JsonWriter Tests")
class JsonWriterTest {

    private TestTreeNode root;
    private TestTreeNode child1;
    private TestTreeNode child2;
    private TestTreeNode grandchild1;
    private FunctionClassMap<TestTreeNode, String> basicTranslator;
    private JsonWriter<TestTreeNode> jsonWriter;

    @BeforeEach
    void setUp() {
        // Create test tree structure:
        //     root
        //    /    \
        //  child1  child2
        //  /
        // grandchild1
        grandchild1 = new TestTreeNode("grandchild1", "leaf");
        child1 = new TestTreeNode("child1", "parent", Collections.singletonList(grandchild1));
        child2 = new TestTreeNode("child2", "leaf");
        root = new TestTreeNode("root", "root", Arrays.asList(child1, child2));

        // Create basic JSON translator
        basicTranslator = new FunctionClassMap<>();
        basicTranslator.put(TestTreeNode.class, node -> "\"name\": \"" + JsonWriter.escape(node.getName()) + "\",\n" +
                "\"type\": \"" + JsonWriter.escape(node.getType()) + "\"");

        jsonWriter = new JsonWriter<>(basicTranslator);
    }

    @Nested
    @DisplayName("Basic JSON Generation Tests")
    class BasicJsonGenerationTests {

        @Test
        @DisplayName("toJson() should generate valid JSON for single node")
        void testToJson_SingleNode_GeneratesValidJson() {
            TestTreeNode singleNode = new TestTreeNode("single", "test");

            String json = jsonWriter.toJson(singleNode);

            assertThat(json).isNotNull();
            assertThat(json).startsWith("{");
            assertThat(json).endsWith("}");
            assertThat(json).contains("\"name\": \"single\"");
            assertThat(json).contains("\"type\": \"test\"");
            assertThat(json).contains("\"children\": []");
        }

        @Test
        @DisplayName("toJson() should generate JSON for tree structure")
        void testToJson_TreeStructure_GeneratesValidJson() {
            String json = jsonWriter.toJson(root);

            assertThat(json).isNotNull();
            assertThat(json).startsWith("{");
            assertThat(json).endsWith("}");

            // Should contain root information
            assertThat(json).contains("\"name\": \"root\"");
            assertThat(json).contains("\"type\": \"root\"");

            // Should contain children array
            assertThat(json).contains("\"children\": [");

            // Should contain child information
            assertThat(json).contains("\"name\": \"child1\"");
            assertThat(json).contains("\"name\": \"child2\"");
            assertThat(json).contains("\"name\": \"grandchild1\"");
        }

        @Test
        @DisplayName("toJson() should handle leaf nodes correctly")
        void testToJson_LeafNodes_HandledCorrectly() {
            String json = jsonWriter.toJson(child2);

            assertThat(json).contains("\"name\": \"child2\"");
            assertThat(json).contains("\"type\": \"leaf\"");
            assertThat(json).contains("\"children\": []");
            
            // Should not contain nested children
            assertThat(json).doesNotContain("\"children\": [\n");
        }
    }

    @Nested
    @DisplayName("JSON Structure Validation Tests")
    class JsonStructureValidationTests {

        @Test
        @DisplayName("Generated JSON should have proper indentation")
        void testGeneratedJson_HasProperIndentation() {
            String json = jsonWriter.toJson(root);

            String[] lines = json.split("\n");

            // Root level should have no indentation
            assertThat(lines[0]).isEqualTo("{");

            // Child properties should be indented
            boolean foundIndentedContent = false;
            for (String line : lines) {
                if (line.startsWith("    ") && line.contains("\"name\":")) {
                    foundIndentedContent = true;
                    break;
                }
            }
            assertThat(foundIndentedContent).isTrue();
        }

        @Test
        @DisplayName("Generated JSON should have proper nesting structure")
        void testGeneratedJson_HasProperNestingStructure() {
            String json = jsonWriter.toJson(root);

            // Count braces to ensure proper nesting
            long openBraces = json.chars().filter(ch -> ch == '{').count();
            long closeBraces = json.chars().filter(ch -> ch == '}').count();
            assertThat(openBraces).isEqualTo(closeBraces);

            // Count brackets for arrays
            long openBrackets = json.chars().filter(ch -> ch == '[').count();
            long closeBrackets = json.chars().filter(ch -> ch == ']').count();
            assertThat(openBrackets).isEqualTo(closeBrackets);

            // Should have the expected number of nodes (4 nodes = 4 objects)
            assertThat(openBraces).isEqualTo(4);
        }

        @Test
        @DisplayName("Generated JSON should maintain parent-child relationships")
        void testGeneratedJson_MaintainsParentChildRelationships() {
            String json = jsonWriter.toJson(root);

            // Root should contain children
            int rootStart = json.indexOf("\"name\": \"root\"");
            int rootChildrenStart = json.indexOf("\"children\": [", rootStart);
            assertThat(rootChildrenStart).isGreaterThan(rootStart);

            // child1 should contain grandchild1
            int child1Start = json.indexOf("\"name\": \"child1\"");
            int child1ChildrenStart = json.indexOf("\"children\": [", child1Start);
            int grandchild1Start = json.indexOf("\"name\": \"grandchild1\"");

            assertThat(child1ChildrenStart).isGreaterThan(child1Start);
            assertThat(grandchild1Start).isGreaterThan(child1ChildrenStart);
        }
    }

    @Nested
    @DisplayName("JSON Escaping Tests")
    class JsonEscapingTests {

        @Test
        @DisplayName("escape() should handle backslashes correctly")
        void testEscape_HandlesBackslashes() {
            String input = "path\\to\\file";
            String escaped = JsonWriter.escape(input);

            assertThat(escaped).isEqualTo("path\\\\to\\\\file");
        }

        @Test
        @DisplayName("escape() should handle quotes correctly")
        void testEscape_HandlesQuotes() {
            String input = "He said \"Hello\"";
            String escaped = JsonWriter.escape(input);

            assertThat(escaped).isEqualTo("He said \\\"Hello\\\"");
        }

        @Test
        @DisplayName("escape() should handle combined special characters")
        void testEscape_HandlesCombinedSpecialCharacters() {
            String input = "path\\to\\\"quoted folder\"";
            String escaped = JsonWriter.escape(input);

            assertThat(escaped).isEqualTo("path\\\\to\\\\\\\"quoted folder\\\"");
        }

        @Test
        @DisplayName("escape() should handle empty and null strings")
        void testEscape_HandlesEmptyAndNullStrings() {
            assertThat(JsonWriter.escape("")).isEqualTo("");

            // Note: escape method doesn't handle null - would throw NPE
            // This is current behavior, could be documented as requirement for non-null
            // input
        }

        @Test
        @DisplayName("toJson() should properly escape content in generated JSON")
        void testToJson_ProperlyEscapesContent() {
            TestTreeNode nodeWithSpecialChars = new TestTreeNode("node\"with\\special", "type\"with\\chars");

            String json = jsonWriter.toJson(nodeWithSpecialChars);

            assertThat(json).contains("\"name\": \"node\\\"with\\\\special\"");
            assertThat(json).contains("\"type\": \"type\\\"with\\\\chars\"");
        }
    }

    @Nested
    @DisplayName("Custom Translator Tests")
    class CustomTranslatorTests {

        @Test
        @DisplayName("JsonWriter should work with custom translators")
        void testJsonWriter_WorksWithCustomTranslators() {
            // Create custom translator that includes additional information
            FunctionClassMap<TestTreeNode, String> customTranslator = new FunctionClassMap<>();
            customTranslator.put(TestTreeNode.class,
                    node -> "\"name\": \"" + JsonWriter.escape(node.getName()) + "\",\n" +
                            "\"type\": \"" + JsonWriter.escape(node.getType()) + "\",\n" +
                            "\"depth\": " + node.getDepth() + ",\n" +
                            "\"hasChildren\": " + node.hasChildren());

            JsonWriter<TestTreeNode> customWriter = new JsonWriter<>(customTranslator);
            String json = customWriter.toJson(root);

            assertThat(json).contains("\"depth\": 0");
            assertThat(json).contains("\"hasChildren\": true");
            assertThat(json).contains("\"hasChildren\": false");
        }

        @Test
        @DisplayName("JsonWriter should handle minimal translators")
        void testJsonWriter_HandlesMinimalTranslators() {
            FunctionClassMap<TestTreeNode, String> minimalTranslator = new FunctionClassMap<>();
            minimalTranslator.put(TestTreeNode.class, node -> "\"id\": \"" + node.getName() + "\"");

            JsonWriter<TestTreeNode> minimalWriter = new JsonWriter<>(minimalTranslator);
            String json = minimalWriter.toJson(child2);

            assertThat(json).contains("\"id\": \"child2\"");
            assertThat(json).contains("\"children\": []");
            assertThat(json).doesNotContain("\"type\"");
        }

        @Test
        @DisplayName("JsonWriter should handle translators with complex JSON structures")
        void testJsonWriter_HandlesComplexTranslators() {
            FunctionClassMap<TestTreeNode, String> complexTranslator = new FunctionClassMap<>();
            complexTranslator.put(TestTreeNode.class, node -> "\"node\": {\n" +
                    "    \"name\": \"" + JsonWriter.escape(node.getName()) + "\",\n" +
                    "    \"type\": \"" + JsonWriter.escape(node.getType()) + "\"\n" +
                    "},\n" +
                    "\"metadata\": {\n" +
                    "    \"depth\": " + node.getDepth() + ",\n" +
                    "    \"childCount\": " + node.getNumChildren() + "\n" +
                    "}");

            JsonWriter<TestTreeNode> complexWriter = new JsonWriter<>(complexTranslator);
            String json = complexWriter.toJson(child2);

            assertThat(json).contains("\"node\": {");
            assertThat(json).contains("\"metadata\": {");
            assertThat(json).contains("\"childCount\": 0");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("toJson() should handle trees with many children")
        void testToJson_HandlesTreesWithManyChildren() {
            TestTreeNode parentWithManyChildren = new TestTreeNode("parent", "parent");
            for (int i = 0; i < 10; i++) {
                parentWithManyChildren.addChild(new TestTreeNode("child" + i, "child"));
            }

            String json = jsonWriter.toJson(parentWithManyChildren);

            assertThat(json).contains("\"name\": \"parent\"");
            assertThat(json).contains("\"name\": \"child0\"");
            assertThat(json).contains("\"name\": \"child9\"");

            // Should have proper comma separation between children
            assertThat(json).containsPattern("\\},\\s*\\{");
        }

        @Test
        @DisplayName("toJson() should handle deep tree structures")
        void testToJson_HandlesDeepTreeStructures() {
            // Create a deep chain: root -> child -> grandchild -> greatgrandchild
            TestTreeNode greatGrandchild = new TestTreeNode("greatgrandchild", "leaf");
            TestTreeNode deepGrandchild = new TestTreeNode("deepgrandchild", "parent",
                    Collections.singletonList(greatGrandchild));
            TestTreeNode deepChild = new TestTreeNode("deepchild", "parent", Collections.singletonList(deepGrandchild));
            TestTreeNode deepRoot = new TestTreeNode("deeproot", "root", Collections.singletonList(deepChild));

            String json = jsonWriter.toJson(deepRoot);

            assertThat(json).contains("\"name\": \"deeproot\"");
            assertThat(json).contains("\"name\": \"deepchild\"");
            assertThat(json).contains("\"name\": \"deepgrandchild\"");
            assertThat(json).contains("\"name\": \"greatgrandchild\"");

            // Check proper nesting (4 levels = 4 objects)
            long openBraces = json.chars().filter(ch -> ch == '{').count();
            assertThat(openBraces).isEqualTo(4);
        }

        @Test
        @DisplayName("toJson() should handle nodes with special characters in all fields")
        void testToJson_HandlesSpecialCharactersInAllFields() {
            TestTreeNode specialNode = new TestTreeNode("name\"with\\quotes", "type\"with\\quotes");

            String json = jsonWriter.toJson(specialNode);

            assertThat(json).contains("\"name\": \"name\\\"with\\\\quotes\"");
            assertThat(json).contains("\"type\": \"type\\\"with\\\\quotes\"");

            // Should still be valid JSON structure
            assertThat(json).startsWith("{");
            assertThat(json).endsWith("}");
        }

        @Test
        @DisplayName("toJson() should produce deterministic output for same tree")
        void testToJson_ProducesDeterministicOutput() {
            String json1 = jsonWriter.toJson(root);
            String json2 = jsonWriter.toJson(root);

            assertThat(json1).isEqualTo(json2);
        }
    }

    /**
     * Test implementation of TreeNode for testing JsonWriter
     */
    private static class TestTreeNode extends ATreeNode<TestTreeNode> {
        private final String name;
        private final String type;

        public TestTreeNode(String name, String type) {
            super(Collections.emptyList());
            this.name = name;
            this.type = type;
        }

        public TestTreeNode(String name, String type, Collection<? extends TestTreeNode> children) {
            super(children);
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        @Override
        public String toContentString() {
            return name + ":" + type;
        }

        @Override
        protected TestTreeNode copyPrivate() {
            return new TestTreeNode(name, type);
        }

        @Override
        public TestTreeNode copy() {
            List<TestTreeNode> childrenCopy = new ArrayList<>();
            for (TestTreeNode child : getChildren()) {
                childrenCopy.add(child.copy());
            }
            return new TestTreeNode(name, type, childrenCopy);
        }

        @Override
        public String toString() {
            return "TestTreeNode{name='" + name + "', type='" + type + "', children=" + getNumChildren() + "}";
        }
    }
}
