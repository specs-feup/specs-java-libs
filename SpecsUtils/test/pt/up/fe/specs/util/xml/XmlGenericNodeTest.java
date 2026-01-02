package pt.up.fe.specs.util.xml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.assertj.core.api.Assertions.*;

/**
 * Test suite for {@link XmlGenericNode} class - Generic DOM Node wrapper
 * implementation.
 * Tests generic node functionality for various DOM node types.
 * 
 * @author Generated Tests
 */
class XmlGenericNodeTest {

    private Document document;
    private Element element;
    private Text textNode;
    private Comment commentNode;
    private Attr attributeNode;

    @BeforeEach
    void setUp() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();

        // Create various types of DOM nodes for testing
        element = document.createElement("test");
        element.setAttribute("attr", "value");
        document.appendChild(element);

        textNode = document.createTextNode("text content");
        element.appendChild(textNode);

        commentNode = document.createComment("This is a comment");
        element.appendChild(commentNode);

        attributeNode = element.getAttributeNode("attr");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create XmlGenericNode with Element")
        void testConstructorWithElement() {
            XmlGenericNode genericNode = new XmlGenericNode(element);

            assertThat(genericNode).isNotNull();
            assertThat(genericNode.getNode()).isSameAs(element);
        }

        @Test
        @DisplayName("Should create XmlGenericNode with Text node")
        void testConstructorWithTextNode() {
            XmlGenericNode genericNode = new XmlGenericNode(textNode);

            assertThat(genericNode).isNotNull();
            assertThat(genericNode.getNode()).isSameAs(textNode);
        }

        @Test
        @DisplayName("Should create XmlGenericNode with Comment node")
        void testConstructorWithCommentNode() {
            XmlGenericNode genericNode = new XmlGenericNode(commentNode);

            assertThat(genericNode).isNotNull();
            assertThat(genericNode.getNode()).isSameAs(commentNode);
        }

        @Test
        @DisplayName("Should create XmlGenericNode with Document node")
        void testConstructorWithDocumentNode() {
            XmlGenericNode genericNode = new XmlGenericNode(document);

            assertThat(genericNode).isNotNull();
            assertThat(genericNode.getNode()).isSameAs(document);
        }

        @Test
        @DisplayName("Should create XmlGenericNode with Attribute node")
        void testConstructorWithAttributeNode() {
            XmlGenericNode genericNode = new XmlGenericNode(attributeNode);

            assertThat(genericNode).isNotNull();
            assertThat(genericNode.getNode()).isSameAs(attributeNode);
        }

        @Test
        @DisplayName("Should handle null Node in constructor")
        void testNullConstructor() {
            assertThatThrownBy(() -> new XmlGenericNode(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("non-null Node");
        }
    }

    @Nested
    @DisplayName("getNode() Method Tests")
    class GetNodeTests {

        @Test
        @DisplayName("Should return original Element node")
        void testGetNodeElement() {
            XmlGenericNode genericNode = new XmlGenericNode(element);

            Node retrievedNode = genericNode.getNode();

            assertThat(retrievedNode).isSameAs(element);
            assertThat(retrievedNode).isInstanceOf(Element.class);
            assertThat(retrievedNode.getNodeName()).isEqualTo("test");
        }

        @Test
        @DisplayName("Should return original Text node")
        void testGetNodeText() {
            XmlGenericNode genericNode = new XmlGenericNode(textNode);

            Node retrievedNode = genericNode.getNode();

            assertThat(retrievedNode).isSameAs(textNode);
            assertThat(retrievedNode).isInstanceOf(Text.class);
            assertThat(retrievedNode.getNodeValue()).isEqualTo("text content");
        }

        @Test
        @DisplayName("Should return original Comment node")
        void testGetNodeComment() {
            XmlGenericNode genericNode = new XmlGenericNode(commentNode);

            Node retrievedNode = genericNode.getNode();

            assertThat(retrievedNode).isSameAs(commentNode);
            assertThat(retrievedNode).isInstanceOf(Comment.class);
            assertThat(retrievedNode.getNodeValue()).isEqualTo("This is a comment");
        }

        @Test
        @DisplayName("Should return original Document node")
        void testGetNodeDocument() {
            XmlGenericNode genericNode = new XmlGenericNode(document);

            Node retrievedNode = genericNode.getNode();

            assertThat(retrievedNode).isSameAs(document);
            assertThat(retrievedNode).isInstanceOf(Document.class);
        }

        @Test
        @DisplayName("Should return original Attribute node")
        void testGetNodeAttribute() {
            XmlGenericNode genericNode = new XmlGenericNode(attributeNode);

            Node retrievedNode = genericNode.getNode();

            assertThat(retrievedNode).isSameAs(attributeNode);
            assertThat(retrievedNode).isInstanceOf(Attr.class);
            assertThat(retrievedNode.getNodeValue()).isEqualTo("value");
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend AXmlNode")
        void testInheritance() {
            XmlGenericNode genericNode = new XmlGenericNode(element);

            assertThat(genericNode).isInstanceOf(AXmlNode.class);
            assertThat(genericNode).isInstanceOf(XmlNode.class);
        }

        @Test
        @DisplayName("Should delegate toString to getString")
        void testToString() {
            XmlGenericNode genericNode = new XmlGenericNode(element);

            assertThat(genericNode.toString()).isEqualTo(genericNode.getString());
        }

        @Test
        @DisplayName("Should inherit XmlNode interface methods")
        void testXmlNodeMethods() {
            XmlGenericNode genericNode = new XmlGenericNode(element);

            // Test that interface methods are available
            assertThat(genericNode.getText()).isEqualTo("text content");
            assertThat(genericNode.getChildren()).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("Node Type Specific Tests")
    class NodeTypeSpecificTests {

        @Test
        @DisplayName("Should handle Element nodes correctly")
        void testElementNodeHandling() {
            XmlGenericNode genericNode = new XmlGenericNode(element);

            assertThat(genericNode.getNode().getNodeType()).isEqualTo(Node.ELEMENT_NODE);
            assertThat(genericNode.getNode().getNodeName()).isEqualTo("test");
            assertThat(genericNode.getText()).contains("text content");
        }

        @Test
        @DisplayName("Should handle Text nodes correctly")
        void testTextNodeHandling() {
            XmlGenericNode genericNode = new XmlGenericNode(textNode);

            assertThat(genericNode.getNode().getNodeType()).isEqualTo(Node.TEXT_NODE);
            assertThat(genericNode.getNode().getNodeValue()).isEqualTo("text content");
            assertThat(genericNode.getText()).isEqualTo("text content");
        }

        @Test
        @DisplayName("Should handle Comment nodes correctly")
        void testCommentNodeHandling() {
            XmlGenericNode genericNode = new XmlGenericNode(commentNode);

            assertThat(genericNode.getNode().getNodeType()).isEqualTo(Node.COMMENT_NODE);
            assertThat(genericNode.getNode().getNodeValue()).isEqualTo("This is a comment");
            assertThat(genericNode.getText()).isEqualTo("This is a comment");
        }

        @Test
        @DisplayName("Should handle Document nodes correctly")
        void testDocumentNodeHandling() {
            XmlGenericNode genericNode = new XmlGenericNode(document);

            assertThat(genericNode.getNode().getNodeType()).isEqualTo(Node.DOCUMENT_NODE);
            assertThat(genericNode.getChildren()).isNotEmpty();
        }

        @Test
        @DisplayName("Should handle Attribute nodes correctly")
        void testAttributeNodeHandling() {
            XmlGenericNode genericNode = new XmlGenericNode(attributeNode);

            assertThat(genericNode.getNode().getNodeType()).isEqualTo(Node.ATTRIBUTE_NODE);
            assertThat(genericNode.getNode().getNodeName()).isEqualTo("attr");
            assertThat(genericNode.getNode().getNodeValue()).isEqualTo("value");
        }
    }

    @Nested
    @DisplayName("Special Node Types Tests")
    class SpecialNodeTypesTests {

        @Test
        @DisplayName("Should handle Processing Instruction nodes")
        void testProcessingInstructionNode() throws Exception {
            ProcessingInstruction pi = document.createProcessingInstruction("xml-stylesheet",
                    "type=\"text/xsl\" href=\"style.xsl\"");
            document.insertBefore(pi, element);

            XmlGenericNode genericNode = new XmlGenericNode(pi);

            assertThat(genericNode.getNode()).isSameAs(pi);
            assertThat(genericNode.getNode().getNodeType()).isEqualTo(Node.PROCESSING_INSTRUCTION_NODE);
            assertThat(genericNode.getNode().getNodeName()).isEqualTo("xml-stylesheet");
        }

        @Test
        @DisplayName("Should handle CDATA Section nodes")
        void testCDataSectionNode() throws Exception {
            CDATASection cdata = document.createCDATASection("This is CDATA content");
            element.appendChild(cdata);

            XmlGenericNode genericNode = new XmlGenericNode(cdata);

            assertThat(genericNode.getNode()).isSameAs(cdata);
            assertThat(genericNode.getNode().getNodeType()).isEqualTo(Node.CDATA_SECTION_NODE);
            assertThat(genericNode.getNode().getNodeValue()).isEqualTo("This is CDATA content");
        }

        @Test
        @DisplayName("Should handle Document Fragment nodes")
        void testDocumentFragmentNode() throws Exception {
            DocumentFragment fragment = document.createDocumentFragment();
            Element fragmentChild = document.createElement("fragment-child");
            fragment.appendChild(fragmentChild);

            XmlGenericNode genericNode = new XmlGenericNode(fragment);

            assertThat(genericNode.getNode()).isSameAs(fragment);
            assertThat(genericNode.getNode().getNodeType()).isEqualTo(Node.DOCUMENT_FRAGMENT_NODE);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle nodes with very long content")
        void testLongContent() {
            String longContent = "a".repeat(10000);
            Text longTextNode = document.createTextNode(longContent);

            XmlGenericNode genericNode = new XmlGenericNode(longTextNode);

            assertThat(genericNode.getNode().getNodeValue()).hasSize(10000);
            assertThat(genericNode.getText()).isEqualTo(longContent);
        }

        @Test
        @DisplayName("Should handle nodes with Unicode content")
        void testUnicodeContent() {
            String unicodeContent = "Unicode: 中文 🚀 ñ é";
            Text unicodeTextNode = document.createTextNode(unicodeContent);

            XmlGenericNode genericNode = new XmlGenericNode(unicodeTextNode);

            assertThat(genericNode.getNode().getNodeValue()).isEqualTo(unicodeContent);
            assertThat(genericNode.getText()).isEqualTo(unicodeContent);
        }

        @Test
        @DisplayName("Should handle empty nodes")
        void testEmptyNodes() {
            Text emptyTextNode = document.createTextNode("");
            Comment emptyCommentNode = document.createComment("");

            XmlGenericNode textGeneric = new XmlGenericNode(emptyTextNode);
            XmlGenericNode commentGeneric = new XmlGenericNode(emptyCommentNode);

            assertThat(textGeneric.getText()).isEmpty();
            assertThat(commentGeneric.getText()).isEmpty();
        }

        @Test
        @DisplayName("Should handle nodes with special characters")
        void testSpecialCharacters() {
            String specialContent = "Content with \n\t\r special chars & <tags>";
            Text specialTextNode = document.createTextNode(specialContent);

            XmlGenericNode genericNode = new XmlGenericNode(specialTextNode);

            assertThat(genericNode.getText()).isEqualTo(specialContent);
        }
    }

    @Nested
    @DisplayName("Multiple Node Instances Tests")
    class MultipleInstancesTests {

        @Test
        @DisplayName("Should create independent instances for same node")
        void testIndependentInstances() {
            XmlGenericNode instance1 = new XmlGenericNode(element);
            XmlGenericNode instance2 = new XmlGenericNode(element);

            assertThat(instance1).isNotSameAs(instance2);
            assertThat(instance1.getNode()).isSameAs(instance2.getNode());
            assertThat(instance1.toString()).isEqualTo(instance2.toString());
        }

        @Test
        @DisplayName("Should handle multiple different nodes")
        void testMultipleDifferentNodes() {
            XmlGenericNode elementGeneric = new XmlGenericNode(element);
            XmlGenericNode textGeneric = new XmlGenericNode(textNode);
            XmlGenericNode commentGeneric = new XmlGenericNode(commentNode);

            assertThat(elementGeneric.getNode()).isNotSameAs(textGeneric.getNode());
            assertThat(textGeneric.getNode()).isNotSameAs(commentGeneric.getNode());

            assertThat(elementGeneric.getNode().getNodeType()).isEqualTo(Node.ELEMENT_NODE);
            assertThat(textGeneric.getNode().getNodeType()).isEqualTo(Node.TEXT_NODE);
            assertThat(commentGeneric.getNode().getNodeType()).isEqualTo(Node.COMMENT_NODE);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with XmlNodes factory pattern")
        void testXmlNodesIntegration() {
            // Test that XmlGenericNode can be used with XmlNodes.create()
            XmlNode createdNode = XmlNodes.create(textNode);

            // Should be XmlGenericNode since Text is not specifically mapped
            assertThat(createdNode).isInstanceOf(XmlGenericNode.class);
            assertThat(createdNode.getNode()).isSameAs(textNode);
        }

        @Test
        @DisplayName("Should work within document hierarchy navigation")
        void testHierarchyNavigation() {
            XmlGenericNode parentGeneric = new XmlGenericNode(element);

            var children = parentGeneric.getChildren();
            assertThat(children).hasSizeGreaterThanOrEqualTo(2); // text and comment nodes

            // Should be able to navigate to child nodes
            for (XmlNode child : children) {
                assertThat(child.getNode().getParentNode()).isSameAs(element);
            }
        }

        @Test
        @DisplayName("Should handle complex document structures")
        void testComplexDocumentStructure() throws Exception {
            // Create a more complex structure
            Element parent = document.createElement("parent");
            Element child1 = document.createElement("child1");
            Element child2 = document.createElement("child2");
            Text text1 = document.createTextNode("text1");
            Text text2 = document.createTextNode("text2");
            Comment comment = document.createComment("comment");

            parent.appendChild(child1);
            child1.appendChild(text1);
            parent.appendChild(comment);
            parent.appendChild(child2);
            child2.appendChild(text2);

            XmlGenericNode parentGeneric = new XmlGenericNode(parent);

            assertThat(parentGeneric.getChildren()).hasSize(3); // child1, comment, child2
            assertThat(parentGeneric.getDescendants()).hasSize(5); // child1, text1, comment, child2, text2
        }
    }
}
