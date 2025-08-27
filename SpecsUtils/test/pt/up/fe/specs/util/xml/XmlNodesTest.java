package pt.up.fe.specs.util.xml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Test suite for {@link XmlNodes} class - Factory and utility methods for XML
 * node handling.
 * Tests node creation, list conversion, and descendant navigation
 * functionality.
 * 
 * @author Generated Tests
 */
class XmlNodesTest {

    private Document document;
    private Element rootElement;
    private Element childElement;
    private Text textNode;
    private Comment commentNode;

    @BeforeEach
    void setUp() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();

        // Create a structured document for testing
        rootElement = document.createElement("root");
        document.appendChild(rootElement);

        childElement = document.createElement("child");
        childElement.setAttribute("id", "1");
        rootElement.appendChild(childElement);

        textNode = document.createTextNode("text content");
        childElement.appendChild(textNode);

        commentNode = document.createComment("comment");
        rootElement.appendChild(commentNode);
    }

    @Nested
    @DisplayName("create() Method Tests")
    class CreateMethodTests {

        @Test
        @DisplayName("Should create XmlDocument for Document nodes")
        void testCreateDocumentNode() {
            XmlNode xmlNode = XmlNodes.create(document);

            assertThat(xmlNode).isInstanceOf(XmlDocument.class);
            assertThat(xmlNode.getNode()).isSameAs(document);
        }

        @Test
        @DisplayName("Should create XmlElement for Element nodes")
        void testCreateElementNode() {
            XmlNode xmlNode = XmlNodes.create(rootElement);

            assertThat(xmlNode).isInstanceOf(XmlElement.class);
            assertThat(xmlNode.getNode()).isSameAs(rootElement);
        }

        @Test
        @DisplayName("Should create XmlGenericNode for Text nodes")
        void testCreateTextNode() {
            XmlNode xmlNode = XmlNodes.create(textNode);

            assertThat(xmlNode).isInstanceOf(XmlGenericNode.class);
            assertThat(xmlNode.getNode()).isSameAs(textNode);
        }

        @Test
        @DisplayName("Should create XmlGenericNode for Comment nodes")
        void testCreateCommentNode() {
            XmlNode xmlNode = XmlNodes.create(commentNode);

            assertThat(xmlNode).isInstanceOf(XmlGenericNode.class);
            assertThat(xmlNode.getNode()).isSameAs(commentNode);
        }

        @Test
        @DisplayName("Should create XmlGenericNode for Attribute nodes")
        void testCreateAttributeNode() {
            Attr attributeNode = childElement.getAttributeNode("id");

            XmlNode xmlNode = XmlNodes.create(attributeNode);

            assertThat(xmlNode).isInstanceOf(XmlGenericNode.class);
            assertThat(xmlNode.getNode()).isSameAs(attributeNode);
        }

        @Test
        @DisplayName("Should handle null nodes gracefully")
        void testCreateNullNode() {
            assertThatThrownBy(() -> XmlNodes.create(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should create appropriate wrappers for special node types")
        void testCreateSpecialNodeTypes() throws Exception {
            ProcessingInstruction pi = document.createProcessingInstruction("xml-stylesheet", "href=\"style.xsl\"");
            CDATASection cdata = document.createCDATASection("CDATA content");
            DocumentFragment fragment = document.createDocumentFragment();

            XmlNode piNode = XmlNodes.create(pi);
            XmlNode cdataNode = XmlNodes.create(cdata);
            XmlNode fragmentNode = XmlNodes.create(fragment);

            assertThat(piNode).isInstanceOf(XmlGenericNode.class);
            assertThat(cdataNode).isInstanceOf(XmlGenericNode.class);
            assertThat(fragmentNode).isInstanceOf(XmlGenericNode.class);

            assertThat(piNode.getNode()).isSameAs(pi);
            assertThat(cdataNode.getNode()).isSameAs(cdata);
            assertThat(fragmentNode.getNode()).isSameAs(fragment);
        }
    }

    @Nested
    @DisplayName("toList() Method Tests")
    class ToListMethodTests {

        @Test
        @DisplayName("Should convert NodeList to List of XmlNodes")
        void testToListBasic() {
            NodeList nodeList = rootElement.getChildNodes();

            List<XmlNode> xmlNodes = XmlNodes.toList(nodeList);

            assertThat(xmlNodes).hasSize(nodeList.getLength());

            for (int i = 0; i < nodeList.getLength(); i++) {
                assertThat(xmlNodes.get(i).getNode()).isSameAs(nodeList.item(i));
            }
        }

        @Test
        @DisplayName("Should handle empty NodeList")
        void testToListEmpty() {
            Element emptyElement = document.createElement("empty");
            NodeList emptyNodeList = emptyElement.getChildNodes();

            List<XmlNode> xmlNodes = XmlNodes.toList(emptyNodeList);

            assertThat(xmlNodes).isEmpty();
        }

        @Test
        @DisplayName("Should create correct wrapper types in list")
        void testToListWrapperTypes() {
            NodeList nodeList = rootElement.getChildNodes();
            List<XmlNode> xmlNodes = XmlNodes.toList(nodeList);

            for (XmlNode xmlNode : xmlNodes) {
                Node originalNode = xmlNode.getNode();

                if (originalNode instanceof Element) {
                    assertThat(xmlNode).isInstanceOf(XmlElement.class);
                } else if (originalNode instanceof Document) {
                    assertThat(xmlNode).isInstanceOf(XmlDocument.class);
                } else {
                    assertThat(xmlNode).isInstanceOf(XmlGenericNode.class);
                }
            }
        }

        @Test
        @DisplayName("Should handle null NodeList")
        void testToListNull() {
            assertThatThrownBy(() -> XmlNodes.toList(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle large NodeList efficiently")
        void testToListLarge() {
            Element container = document.createElement("container");

            // Add many child elements
            for (int i = 0; i < 100; i++) {
                Element child = document.createElement("item" + i);
                child.setTextContent("content" + i);
                container.appendChild(child);
            }

            NodeList nodeList = container.getChildNodes();
            List<XmlNode> xmlNodes = XmlNodes.toList(nodeList);

            assertThat(xmlNodes).hasSize(100);

            for (int i = 0; i < 100; i++) {
                assertThat(xmlNodes.get(i)).isInstanceOf(XmlElement.class);
                assertThat(xmlNodes.get(i).getText()).isEqualTo("content" + i);
            }
        }
    }

    @Nested
    @DisplayName("getDescendants() Method Tests")
    class GetDescendantsMethodTests {

        @Test
        @DisplayName("Should return all descendants recursively")
        void testGetDescendantsBasic() {
            XmlNode rootXmlNode = XmlNodes.create(rootElement);

            List<XmlNode> descendants = XmlNodes.getDescendants(rootXmlNode);

            // Should include: childElement, textNode, commentNode
            assertThat(descendants).hasSizeGreaterThanOrEqualTo(3);

            // Verify that descendants include child elements and text nodes
            boolean hasChildElement = descendants.stream()
                    .anyMatch(node -> node.getNode() == childElement);
            boolean hasTextNode = descendants.stream()
                    .anyMatch(node -> node.getNode() == textNode);
            boolean hasCommentNode = descendants.stream()
                    .anyMatch(node -> node.getNode() == commentNode);

            assertThat(hasChildElement).isTrue();
            assertThat(hasTextNode).isTrue();
            assertThat(hasCommentNode).isTrue();
        }

        @Test
        @DisplayName("Should handle nested hierarchies correctly")
        void testGetDescendantsNested() {
            // Create a deeper hierarchy
            Element grandchild = document.createElement("grandchild");
            Element greatGrandchild = document.createElement("great-grandchild");
            Text deepText = document.createTextNode("deep text");

            childElement.appendChild(grandchild);
            grandchild.appendChild(greatGrandchild);
            greatGrandchild.appendChild(deepText);

            XmlNode rootXmlNode = XmlNodes.create(rootElement);
            List<XmlNode> descendants = XmlNodes.getDescendants(rootXmlNode);

            // Should include all levels of descendants
            boolean hasGrandchild = descendants.stream()
                    .anyMatch(node -> node.getNode() == grandchild);
            boolean hasGreatGrandchild = descendants.stream()
                    .anyMatch(node -> node.getNode() == greatGrandchild);
            boolean hasDeepText = descendants.stream()
                    .anyMatch(node -> node.getNode() == deepText);

            assertThat(hasGrandchild).isTrue();
            assertThat(hasGreatGrandchild).isTrue();
            assertThat(hasDeepText).isTrue();
        }

        @Test
        @DisplayName("Should return empty list for leaf nodes")
        void testGetDescendantsLeaf() {
            XmlNode textXmlNode = XmlNodes.create(textNode);

            List<XmlNode> descendants = XmlNodes.getDescendants(textXmlNode);

            assertThat(descendants).isEmpty();
        }

        @Test
        @DisplayName("Should handle nodes with no children")
        void testGetDescendantsNoChildren() {
            Element emptyElement = document.createElement("empty");
            XmlNode emptyXmlNode = XmlNodes.create(emptyElement);

            List<XmlNode> descendants = XmlNodes.getDescendants(emptyXmlNode);

            assertThat(descendants).isEmpty();
        }

        @Test
        @DisplayName("Should handle null node gracefully")
        void testGetDescendantsNull() {
            assertThatThrownBy(() -> XmlNodes.getDescendants(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should maintain order of descendants")
        void testGetDescendantsOrder() {
            // Create ordered children
            Element first = document.createElement("first");
            Element second = document.createElement("second");
            Element third = document.createElement("third");

            rootElement.appendChild(first);
            rootElement.appendChild(second);
            rootElement.appendChild(third);

            XmlNode rootXmlNode = XmlNodes.create(rootElement);
            List<XmlNode> descendants = XmlNodes.getDescendants(rootXmlNode);

            // Find positions of our elements in descendants
            int firstPos = -1, secondPos = -1, thirdPos = -1;
            for (int i = 0; i < descendants.size(); i++) {
                Node node = descendants.get(i).getNode();
                if (node == first)
                    firstPos = i;
                else if (node == second)
                    secondPos = i;
                else if (node == third)
                    thirdPos = i;
            }

            assertThat(firstPos).isLessThan(secondPos);
            assertThat(secondPos).isLessThan(thirdPos);
        }
    }

    @Nested
    @DisplayName("ClassMap Integration Tests")
    class ClassMapIntegrationTests {

        @Test
        @DisplayName("Should use FunctionClassMap for node type mapping")
        void testClassMapIntegration() {
            // Test that the static mapper correctly identifies node types
            XmlNode documentNode = XmlNodes.create(document);
            XmlNode elementNode = XmlNodes.create(rootElement);
            XmlNode textGenericNode = XmlNodes.create(textNode);

            assertThat(documentNode).isInstanceOf(XmlDocument.class);
            assertThat(elementNode).isInstanceOf(XmlElement.class);
            assertThat(textGenericNode).isInstanceOf(XmlGenericNode.class);
        }

        @Test
        @DisplayName("Should handle inheritance in node type mapping")
        void testInheritanceMapping() {
            // All created nodes should be XmlNode instances
            XmlNode[] nodes = {
                    XmlNodes.create(document),
                    XmlNodes.create(rootElement),
                    XmlNodes.create(textNode),
                    XmlNodes.create(commentNode)
            };

            for (XmlNode node : nodes) {
                assertThat(node).isInstanceOf(XmlNode.class);
                assertThat(node).isInstanceOf(AXmlNode.class);
            }
        }

        @Test
        @DisplayName("Should fallback to XmlGenericNode for unmapped types")
        void testFallbackMapping() throws Exception {
            ProcessingInstruction pi = document.createProcessingInstruction("test", "data");
            CDATASection cdata = document.createCDATASection("data");

            XmlNode piNode = XmlNodes.create(pi);
            XmlNode cdataNode = XmlNodes.create(cdata);

            // Should fallback to XmlGenericNode for unmapped types
            assertThat(piNode).isInstanceOf(XmlGenericNode.class);
            assertThat(cdataNode).isInstanceOf(XmlGenericNode.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very deep hierarchies")
        void testVeryDeepHierarchy() {
            Element current = rootElement;

            // Create a deep hierarchy (100 levels)
            for (int i = 0; i < 100; i++) {
                Element child = document.createElement("level" + i);
                current.appendChild(child);
                current = child;
            }

            XmlNode rootXmlNode = XmlNodes.create(rootElement);
            List<XmlNode> descendants = XmlNodes.getDescendants(rootXmlNode);

            // Should handle deep hierarchy without stack overflow
            assertThat(descendants).hasSizeGreaterThanOrEqualTo(100);
        }

        @Test
        @DisplayName("Should handle wide hierarchies")
        void testWideHierarchy() {
            // Create a wide hierarchy (100 children)
            for (int i = 0; i < 100; i++) {
                Element child = document.createElement("child" + i);
                rootElement.appendChild(child);
            }

            XmlNode rootXmlNode = XmlNodes.create(rootElement);
            List<XmlNode> descendants = XmlNodes.getDescendants(rootXmlNode);

            // Should handle wide hierarchy efficiently
            assertThat(descendants).hasSizeGreaterThanOrEqualTo(100);
        }

        @Test
        @DisplayName("Should handle mixed node types in hierarchy")
        void testMixedNodeTypes() throws Exception {
            Element mixedParent = document.createElement("mixed");

            mixedParent.appendChild(document.createElement("element"));
            mixedParent.appendChild(document.createTextNode("text"));
            mixedParent.appendChild(document.createComment("comment"));
            mixedParent.appendChild(document.createCDATASection("cdata"));

            rootElement.appendChild(mixedParent);

            XmlNode mixedXmlNode = XmlNodes.create(mixedParent);
            List<XmlNode> descendants = XmlNodes.getDescendants(mixedXmlNode);

            assertThat(descendants).hasSize(4);

            // Should handle all node types correctly
            boolean hasElement = descendants.stream()
                    .anyMatch(node -> node.getNode().getNodeType() == Node.ELEMENT_NODE);
            boolean hasText = descendants.stream()
                    .anyMatch(node -> node.getNode().getNodeType() == Node.TEXT_NODE);
            boolean hasComment = descendants.stream()
                    .anyMatch(node -> node.getNode().getNodeType() == Node.COMMENT_NODE);
            boolean hasCData = descendants.stream()
                    .anyMatch(node -> node.getNode().getNodeType() == Node.CDATA_SECTION_NODE);

            assertThat(hasElement).isTrue();
            assertThat(hasText).isTrue();
            assertThat(hasComment).isTrue();
            assertThat(hasCData).isTrue();
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should perform create operations efficiently")
        void testCreatePerformance() {
            long startTime = System.nanoTime();

            // Create 1000 wrapper nodes
            for (int i = 0; i < 1000; i++) {
                XmlNodes.create(rootElement);
            }

            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000;

            // Should complete quickly (less than 100ms for 1000 operations)
            assertThat(durationMs).isLessThan(100);
        }

        @Test
        @DisplayName("Should perform toList operations efficiently")
        void testToListPerformance() {
            // Create a large NodeList
            Element container = document.createElement("container");
            for (int i = 0; i < 1000; i++) {
                container.appendChild(document.createElement("item" + i));
            }

            NodeList nodeList = container.getChildNodes();

            long startTime = System.nanoTime();
            List<XmlNode> xmlNodes = XmlNodes.toList(nodeList);
            long endTime = System.nanoTime();

            long durationMs = (endTime - startTime) / 1_000_000;

            assertThat(xmlNodes).hasSize(1000);
            assertThat(durationMs).isLessThan(50); // Should be very fast
        }
    }
}
