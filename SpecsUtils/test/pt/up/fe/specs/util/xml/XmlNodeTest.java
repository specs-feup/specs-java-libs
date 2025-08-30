package pt.up.fe.specs.util.xml;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.StringWriter;
import java.util.List;

import javax.xml.transform.stream.StreamResult;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * 
 * @author Generated Tests
 */
@DisplayName("XmlNode Interface Tests")
class XmlNodeTest {

    private XmlDocument document;
    private XmlNode rootNode;
    private static final String SAMPLE_XML = """
            <?xml version="1.0" encoding="UTF-8"?>
            <root>
                <parent id="p1">
                    <child name="child1">Text content 1</child>
                    <child name="child2">Text content 2</child>
                    <nested>
                        <grandchild value="gc1">Grandchild text</grandchild>
                    </nested>
                </parent>
                <parent id="p2">
                    <child name="child3">Text content 3</child>
                </parent>
            </root>
            """;

    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() {
        document = XmlDocument.newInstance(SAMPLE_XML);
        rootNode = document.getElementByName("root");
    }

    @Nested
    @DisplayName("Node Access")
    class NodeAccess {

        @Test
        @DisplayName("Should get underlying DOM node")
        void testGetNode() {
            org.w3c.dom.Node domNode = rootNode.getNode();

            assertThat(domNode).isNotNull();
            assertThat(domNode.getNodeName()).isEqualTo("root");
        }

        @Test
        @DisplayName("Should get parent node")
        void testGetParent() {
            List<XmlElement> parents = rootNode.getElementsByName("parent");
            XmlElement parent = parents.get(0); // Get the first parent
            List<XmlElement> children = parent.getElementsByName("child");
            XmlElement child = children.get(0); // Get the first child of this parent

            XmlNode childParent = child.getParent();

            assertThat(childParent).isNotNull();
            assertThat(((XmlElement) childParent).getName()).isEqualTo("parent");
        }

        @Test
        @DisplayName("Should return null for root parent")
        void testRootParent() {
            assertThat(document.getParent()).isNull();
        }
    }

    @Nested
    @DisplayName("Children Navigation")
    class ChildrenNavigation {

        @Test
        @DisplayName("Should get direct children")
        void testGetChildren() {
            List<XmlNode> children = rootNode.getChildren();

            assertThat(children).hasSize(5); // Including text nodes

            // Filter to just elements
            List<XmlElement> elementChildren = children.stream()
                    .filter(XmlElement.class::isInstance)
                    .map(XmlElement.class::cast)
                    .toList();

            assertThat(elementChildren).hasSize(2);
            assertThat(elementChildren).allMatch(e -> e.getName().equals("parent"));
        }

        @Test
        @DisplayName("Should get all descendants")
        void testGetDescendants() {
            List<XmlNode> descendants = rootNode.getDescendants();

            assertThat(descendants).isNotEmpty();

            // Should include all nested elements
            List<XmlElement> elementDescendants = descendants.stream()
                    .filter(XmlElement.class::isInstance)
                    .map(XmlElement.class::cast)
                    .toList();

            List<String> elementNames = elementDescendants.stream()
                    .map(XmlElement::getName)
                    .toList();

            assertThat(elementNames).contains("parent", "child", "nested", "grandchild");
        }

        @Test
        @DisplayName("Should handle nodes with no children")
        void testNoChildren() {
            List<XmlElement> children = rootNode.getElementsByName("child");
            XmlElement child = children.get(0); // Get the first child

            List<XmlNode> childNodes = child.getChildren();

            // May have text node children
            assertThat(childNodes).allMatch(node -> !(node instanceof XmlElement));
        }
    }

    @Nested
    @DisplayName("Element Search")
    class ElementSearch {

        @Test
        @DisplayName("Should find elements by name")
        void testGetElementsByName() {
            List<XmlElement> parents = rootNode.getElementsByName("parent");
            List<XmlElement> children = rootNode.getElementsByName("child");

            assertThat(parents).hasSize(2);
            assertThat(children).hasSize(3);

            assertThat(parents).allMatch(e -> e.getName().equals("parent"));
            assertThat(children).allMatch(e -> e.getName().equals("child"));
        }

        @Test
        @DisplayName("Should return empty list for non-existent elements")
        void testGetElementsByNameNotFound() {
            List<XmlElement> nonExistent = rootNode.getElementsByName("nonexistent");

            assertThat(nonExistent).isEmpty();
        }

        @Test
        @DisplayName("Should find single element by name")
        void testGetElementByName() {
            XmlElement nested = rootNode.getElementByName("nested");
            XmlElement grandchild = rootNode.getElementByName("grandchild");

            assertThat(nested).isNotNull();
            assertThat(nested.getName()).isEqualTo("nested");

            assertThat(grandchild).isNotNull();
            assertThat(grandchild.getName()).isEqualTo("grandchild");
        }

        @Test
        @DisplayName("Should return null for non-existent single element")
        void testGetElementByNameNotFound() {
            XmlElement nonExistent = rootNode.getElementByName("nonexistent");

            assertThat(nonExistent).isNull();
        }

        @Test
        @DisplayName("Should throw exception for multiple elements with same name")
        void testGetElementByNameMultiple() {
            assertThatThrownBy(() -> rootNode.getElementByName("parent"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("More than one element with name 'parent'");
        }
    }

    @Nested
    @DisplayName("Text Content")
    class TextContent {

        @Test
        @DisplayName("Should get text content")
        void testGetText() {
            List<XmlElement> children = rootNode.getElementsByName("child");
            XmlElement child = children.get(0); // Get the first child

            String text = child.getText();

            assertThat(text).isEqualTo("Text content 1");
        }

        @Test
        @DisplayName("Should get text from nested elements")
        void testGetTextNested() {
            XmlElement grandchild = rootNode.getElementByName("grandchild");

            String text = grandchild.getText();

            assertThat(text).isEqualTo("Grandchild text");
        }

        @Test
        @DisplayName("Should set text content")
        void testSetText() {
            List<XmlElement> children = rootNode.getElementsByName("child");
            XmlElement child = children.get(0); // Get the first child
            String originalText = child.getText();

            String previousText = child.setText("New text content");

            assertThat(previousText).isEqualTo(originalText);
            assertThat(child.getText()).isEqualTo("New text content");
        }

        @Test
        @DisplayName("Should handle null and empty text")
        void testNullEmptyText() {
            List<XmlElement> children = rootNode.getElementsByName("child");
            XmlElement child = children.get(0); // Get the first child

            child.setText("");
            assertThat(child.getText()).isEqualTo("");

            child.setText(null);
            assertThat(child.getText()).isNull();
        }

        @Test
        @DisplayName("Should handle elements with no text content")
        void testNoTextContent() {
            List<XmlElement> parents = rootNode.getElementsByName("parent");
            XmlElement parent = parents.get(0); // Get the first parent

            String text = parent.getText();

            // Text content includes whitespace and nested element text
            assertThat(text).isNotNull();
        }
    }

    @Nested
    @DisplayName("XML Output")
    class XmlOutput {

        @Test
        @DisplayName("Should write to StreamResult")
        void testWriteStreamResult() {
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);

            rootNode.write(result);

            String output = writer.toString();
            assertThat(output).contains("<root>");
            assertThat(output).contains("</root>");
            assertThat(output).contains("<parent");
            assertThat(output).contains("<child");
        }

        @Test
        @DisplayName("Should write to file")
        void testWriteToFile() {
            File outputFile = new File(tempDir, "output.xml");

            rootNode.write(outputFile);

            assertThat(outputFile).exists();
            assertThat(outputFile).isFile();

            // Verify content by reading back
            XmlDocument readBack = XmlDocument.newInstance(outputFile);
            assertThat(readBack.getElementByName("root")).isNotNull();
        }

        @Test
        @DisplayName("Should get string representation")
        void testGetString() {
            String xmlString = rootNode.getString();

            assertThat(xmlString).contains("<root>");
            assertThat(xmlString).contains("</root>");
            assertThat(xmlString).contains("<parent");
            assertThat(xmlString).contains("<child");
            assertThat(xmlString).contains("Text content 1");
        }

        @Test
        @DisplayName("Should create directory when writing to file")
        void testWriteCreateDirectory() {
            File subDir = new File(tempDir, "subdir");
            File outputFile = new File(subDir, "nested-output.xml");

            assertThat(subDir).doesNotExist();

            rootNode.write(outputFile);

            assertThat(subDir).exists();
            assertThat(subDir).isDirectory();
            assertThat(outputFile).exists();
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        @Test
        @DisplayName("Should handle write errors gracefully")
        void testWriteError() {
            File readOnlyDir = new File(tempDir, "readonly");
            readOnlyDir.mkdir();
            readOnlyDir.setReadOnly();

            File outputFile = new File(readOnlyDir, "output.xml");

            // Should not throw after fix; errors are logged
            rootNode.write(outputFile);
        }

        @Test
        @DisplayName("Should handle malformed search operations")
        void testMalformedSearch() {
            // Test with null and empty names
            assertThat(rootNode.getElementsByName("")).isEmpty();
            assertThat(rootNode.getElementByName("")).isNull();
        }
    }

    @Nested
    @DisplayName("Integration Scenarios")
    class Integration {

        @Test
        @DisplayName("Should work with complex XML structures")
        void testComplexStructure() {
            String complexXml = """
                    <?xml version="1.0"?>
                    <catalog>
                        <book id="1" category="fiction">
                            <title>Great Gatsby</title>
                            <author>F. Scott Fitzgerald</author>
                            <price currency="USD">10.99</price>
                        </book>
                        <book id="2" category="fiction">
                            <title>1984</title>
                            <author>George Orwell</author>
                            <price currency="USD">8.99</price>
                        </book>
                        <magazine id="3" category="tech">
                            <title>Tech Today</title>
                            <issue>42</issue>
                        </magazine>
                    </catalog>
                    """;

            XmlDocument complexDoc = XmlDocument.newInstance(complexXml);
            XmlElement catalog = complexDoc.getElementByName("catalog");

            List<XmlElement> books = catalog.getElementsByName("book");
            List<XmlElement> titles = catalog.getElementsByName("title");

            assertThat(books).hasSize(2);
            assertThat(titles).hasSize(3); // 2 books + 1 magazine

            XmlElement firstBook = books.get(0);
            XmlElement bookTitle = firstBook.getElementByName("title");
            assertThat(bookTitle.getText()).isEqualTo("Great Gatsby");
        }

        @Test
        @DisplayName("Should handle XML with namespaces")
        void testNamespaces() {
            String namespacedXml = """
                    <?xml version="1.0"?>
                    <root xmlns:custom="http://example.com/custom">
                        <custom:element>Namespaced content</custom:element>
                        <regular>Regular content</regular>
                    </root>
                    """;

            XmlDocument nsDoc = XmlDocument.newInstance(namespacedXml);
            XmlElement root = nsDoc.getElementByName("root");

            assertThat(root).isNotNull();

            // Should be able to find regular elements
            XmlElement regular = root.getElementByName("regular");
            assertThat(regular).isNotNull();
            assertThat(regular.getText()).isEqualTo("Regular content");
        }

        @Test
        @DisplayName("Should preserve XML structure in round-trip")
        void testRoundTrip() {
            String originalXml = rootNode.getString();

            // Parse again
            XmlDocument newDoc = XmlDocument.newInstance(originalXml);
            XmlElement newRoot = newDoc.getElementByName("root");

            // Verify structure is preserved
            assertThat(newRoot.getElementsByName("parent")).hasSize(2);
            assertThat(newRoot.getElementsByName("child")).hasSize(3);
            assertThat(newRoot.getElementByName("grandchild").getText()).isEqualTo("Grandchild text");
        }

        @Test
        @DisplayName("Should work with mixed content")
        void testMixedContent() {
            String mixedXml = """
                    <?xml version="1.0"?>
                    <paragraph>
                        This is <bold>bold text</bold> and this is <italic>italic text</italic>.
                    </paragraph>
                    """;

            XmlDocument mixedDoc = XmlDocument.newInstance(mixedXml);
            XmlElement paragraph = mixedDoc.getElementByName("paragraph");

            assertThat(paragraph).isNotNull();

            XmlElement bold = paragraph.getElementByName("bold");
            XmlElement italic = paragraph.getElementByName("italic");

            assertThat(bold.getText()).isEqualTo("bold text");
            assertThat(italic.getText()).isEqualTo("italic text");
        }
    }
}
