package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Comprehensive test suite for SpecsXml utility class.
 * Tests XML parsing, document manipulation, element querying, and attribute
 * handling.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsXml Tests")
class SpecsXmlTest {

    private static final String SAMPLE_XML = """
            <?xml version="1.0" encoding="UTF-8"?>
            <root>
                <book id="1" title="Sample Book">
                    <author>John Doe</author>
                    <year>2023</year>
                    <chapter number="1">Introduction</chapter>
                    <chapter number="2">Content</chapter>
                </book>
                <book id="2" title="Another Book">
                    <author>Jane Smith</author>
                    <year>2024</year>
                </book>
                <config>
                    <setting name="debug">true</setting>
                    <setting name="timeout">30</setting>
                </config>
            </root>
            """;

    private static final String INVALID_XML = """
            <?xml version="1.0" encoding="UTF-8"?>
            <root>
                <unclosed>
            </root>
            """;

    @Nested
    @DisplayName("Document Parsing Tests")
    class DocumentParsingTests {

        @Test
        @DisplayName("getXmlRoot should parse XML string")
        void testGetXmlRootFromString() {
            // Execute
            Document doc = SpecsXml.getXmlRoot(SAMPLE_XML);

            // Verify
            assertThat(doc).isNotNull();
            assertThat(doc.getDocumentElement().getNodeName()).isEqualTo("root");
        }

        @Test
        @DisplayName("getXmlRoot should parse InputStream")
        void testGetXmlRootFromInputStream() {
            // Setup
            InputStream inputStream = new ByteArrayInputStream(SAMPLE_XML.getBytes());

            // Execute
            Document doc = SpecsXml.getXmlRoot(inputStream);

            // Verify
            assertThat(doc).isNotNull();
            assertThat(doc.getDocumentElement().getNodeName()).isEqualTo("root");
        }

        @Test
        @DisplayName("getXmlRoot should parse file")
        void testGetXmlRootFromFile(@TempDir File tempDir) throws Exception {
            // Setup
            File xmlFile = new File(tempDir, "test.xml");
            SpecsIo.write(xmlFile, SAMPLE_XML);

            // Execute
            Document doc = SpecsXml.getXmlRoot(xmlFile);

            // Verify
            assertThat(doc).isNotNull();
            assertThat(doc.getDocumentElement().getNodeName()).isEqualTo("root");
        }

        @Test
        @DisplayName("getXmlRoot should throw exception for invalid XML")
        void testGetXmlRootInvalidXml() {
            // Execute & Verify
            assertThatThrownBy(() -> SpecsXml.getXmlRoot(INVALID_XML))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("XML document not according to schema");
        }

        @Test
        @DisplayName("getXmlRoot should throw exception for empty string")
        void testGetXmlRootEmptyString() {
            // Execute & Verify
            assertThatThrownBy(() -> SpecsXml.getXmlRoot(""))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("XML document not according to schema");
        }

        @Test
        @DisplayName("getNodeList should return root children")
        void testGetNodeList(@TempDir File tempDir) throws Exception {
            // Setup
            File xmlFile = new File(tempDir, "test.xml");
            SpecsIo.write(xmlFile, SAMPLE_XML);

            // Execute
            NodeList nodeList = SpecsXml.getNodeList(xmlFile);

            // Verify
            assertThat(nodeList).isNotNull();
            assertThat(nodeList.getLength()).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Element Query Tests")
    class ElementQueryTests {

        private Document getSampleDocument() {
            return SpecsXml.getXmlRoot(SAMPLE_XML);
        }

        @Test
        @DisplayName("getElement should find element by tag")
        void testGetElement() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement();

            // Execute
            Element bookElement = SpecsXml.getElement(root, "book");

            // Verify
            assertThat(bookElement).isNotNull();
            assertThat(bookElement.getTagName()).isEqualTo("book");
            assertThat(bookElement.getAttribute("id")).isEqualTo("1");
        }

        @Test
        @DisplayName("getElement should return null for non-existent tag")
        void testGetElementNotFound() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement();

            // Execute
            Element element = SpecsXml.getElement(root, "nonexistent");

            // Verify
            assertThat(element).isNull();
        }

        @Test
        @DisplayName("getElementText should return element content")
        void testGetElementText() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement();
            Element book = SpecsXml.getElement(root, "book");

            // Execute
            String authorText = SpecsXml.getElementText(book, "author");

            // Verify
            assertThat(authorText).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("getElementText should throw NPE for non-existent element")
        void testGetElementTextNotFound() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement();
            Element book = SpecsXml.getElement(root, "book");

            // Execute & Verify - getElement returns null, getElementText calls
            // getTextContent() on null
            assertThatThrownBy(() -> SpecsXml.getElementText(book, "nonexistent"))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("getElementChildren should return all children")
        void testGetElementChildren() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement();
            Element book = SpecsXml.getElement(root, "book");

            // Execute
            List<Element> children = SpecsXml.getElementChildren(book);

            // Verify
            assertThat(children).isNotEmpty();
            assertThat(children).extracting(Element::getTagName)
                    .contains("author", "year", "chapter");
        }

        @Test
        @DisplayName("getElementChildren with tag should filter by tag name")
        void testGetElementChildrenByTag() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement();
            Element book = SpecsXml.getElement(root, "book");

            // Execute
            List<Element> chapters = SpecsXml.getElementChildren(book, "chapter");

            // Verify
            assertThat(chapters).hasSize(2);
            assertThat(chapters).allSatisfy(chapter -> assertThat(chapter.getTagName()).isEqualTo("chapter"));
        }

        @Test
        @DisplayName("getElements should return all element children")
        void testGetElements() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement();

            // Execute
            List<Element> elements = SpecsXml.getElements(root);

            // Verify
            assertThat(elements).isNotEmpty();
            assertThat(elements).extracting(Element::getTagName)
                    .contains("book", "config");
        }
    }

    @Nested
    @DisplayName("Attribute Handling Tests")
    class AttributeHandlingTests {

        private Document getSampleDocument() {
            return SpecsXml.getXmlRoot(SAMPLE_XML);
        }

        @Test
        @DisplayName("getAttribute from document should return attribute value")
        void testGetAttributeFromDocument() {
            // Setup
            Document doc = getSampleDocument();

            // Execute
            String title = SpecsXml.getAttribute(doc, "book", "title");

            // Verify
            assertThat(title).isEqualTo("Sample Book");
        }

        @Test
        @DisplayName("getAttribute from element should return attribute value")
        void testGetAttributeFromElement() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement();

            // Execute
            String title = SpecsXml.getAttribute(root, "book", "title");

            // Verify
            assertThat(title).isEqualTo("Sample Book");
        }

        @Test
        @DisplayName("getAttribute should return empty string for non-existent attribute")
        void testGetAttributeNotFound() {
            // Setup
            Document doc = getSampleDocument();

            // Execute
            String attribute = SpecsXml.getAttribute(doc, "book", "nonexistent");

            // Verify
            assertThat(attribute).isEmpty();
        }

        @Test
        @DisplayName("getAttribute should return null for non-existent tag")
        void testGetAttributeTagNotFound() {
            // Setup
            Document doc = getSampleDocument();

            // Execute
            String attribute = SpecsXml.getAttribute(doc, "nonexistent", "title");

            // Verify
            assertThat(attribute).isNull();
        }

        @Test
        @DisplayName("getAttributeInt should parse integer attribute")
        void testGetAttributeInt() {
            // Setup
            Document doc = getSampleDocument();

            // Execute
            Integer id = SpecsXml.getAttributeInt(doc, "book", "id");

            // Verify
            assertThat(id).isEqualTo(1);
        }

        @Test
        @DisplayName("getAttributeInt should return null for non-numeric attribute")
        void testGetAttributeIntNonNumeric() {
            // Setup
            Document doc = getSampleDocument();

            // Execute
            Integer result = SpecsXml.getAttributeInt(doc, "book", "title");

            // Verify
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("getAttribute from node should handle optional")
        void testGetAttributeFromNode() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement();
            NodeList books = root.getElementsByTagName("book");
            Node firstBook = books.item(0);

            // Execute
            Optional<String> id = SpecsXml.getAttribute(firstBook, "id");
            Optional<String> nonExistent = SpecsXml.getAttribute(firstBook, "nonexistent");

            // Verify - Element.getAttribute() returns empty string for non-existent
            // attributes, not Optional.empty()
            assertThat(id).isPresent().contains("1");
            assertThat(nonExistent).isPresent().contains("");
        }
    }

    @Nested
    @DisplayName("Node Navigation Tests")
    class NodeNavigationTests {

        private Document getSampleDocument() {
            return SpecsXml.getXmlRoot(SAMPLE_XML);
        }

        @Test
        @DisplayName("getNode should find node by tag")
        void testGetNode() {
            // Setup
            Document doc = getSampleDocument();
            NodeList children = doc.getDocumentElement().getChildNodes();

            // Execute
            Node bookNode = SpecsXml.getNode(children, "book");

            // Verify
            assertThat(bookNode).isNotNull();
            assertThat(bookNode.getNodeName()).isEqualTo("book");
        }

        @Test
        @DisplayName("getNode should throw RuntimeException for non-existent tag")
        void testGetNodeNotFound() {
            // Setup
            Document doc = getSampleDocument();
            NodeList children = doc.getDocumentElement().getChildNodes();

            // Execute & Verify - getNode throws RuntimeException when not found
            assertThatThrownBy(() -> SpecsXml.getNode(children, "nonexistent"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find a node with tag 'nonexistent'");
        }

        @Test
        @DisplayName("getNodeMaybe should return Optional")
        void testGetNodeMaybe() {
            // Setup
            Document doc = getSampleDocument();
            NodeList children = doc.getDocumentElement().getChildNodes();

            // Execute
            Optional<Node> bookNode = SpecsXml.getNodeMaybe(children, "book");
            Optional<Node> nonExistent = SpecsXml.getNodeMaybe(children, "nonexistent");

            // Verify
            assertThat(bookNode).isPresent();
            assertThat(bookNode.get().getNodeName()).isEqualTo("book");
            assertThat(nonExistent).isEmpty();
        }

        @Test
        @DisplayName("getNodes should return all matching child nodes from node")
        void testGetNodesFromNodeChildren() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement();

            // Execute
            List<Node> books = SpecsXml.getNodes(root, "book");

            // Verify
            assertThat(books).hasSize(2);
            assertThat(books).allSatisfy(book -> assertThat(book.getNodeName()).isEqualTo("book"));
        }

        @Test
        @DisplayName("getNodes from node should return children")
        void testGetNodesFromNode() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement();
            Node firstBook = SpecsXml.getNode(root.getChildNodes(), "book");

            // Execute
            List<Node> chapters = SpecsXml.getNodes(firstBook, "chapter");

            // Verify
            assertThat(chapters).hasSize(2);
            assertThat(chapters).allSatisfy(chapter -> assertThat(chapter.getNodeName()).isEqualTo("chapter"));
        }
    }

    @Nested
    @DisplayName("Value Extraction Tests")
    class ValueExtractionTests {

        private Document getSampleDocument() {
            return SpecsXml.getXmlRoot(SAMPLE_XML);
        }

        @Test
        @DisplayName("getText should extract nested value")
        void testGetTextNested() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement(); // Get actual root element, not document

            // Execute
            String author = SpecsXml.getText(root.getChildNodes(), "book", "author");

            // Verify
            assertThat(author).isEqualTo("John Doe");
        }

        @Test
        @DisplayName("getText should handle multiple levels")
        void testGetTextMultipleLevels() {
            // Setup
            String nestedXml = """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <root>
                        <level1>
                            <level2>
                                <level3>deep value</level3>
                            </level2>
                        </level1>
                    </root>
                    """;
            Document doc = SpecsXml.getXmlRoot(nestedXml);
            Element root = doc.getDocumentElement(); // Get actual root element

            // Execute
            String value = SpecsXml.getText(root.getChildNodes(), "level1", "level2", "level3");

            // Verify
            assertThat(value).isEqualTo("deep value");
        }

        @Test
        @DisplayName("getText should throw exception for non-existent path")
        void testGetTextNotFound() {
            // Setup
            Document doc = getSampleDocument();

            // Execute & Verify
            assertThatThrownBy(() -> SpecsXml.getText(doc.getChildNodes(), "nonexistent"))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("should extract element text content")
        void testElementTextContent() {
            // Setup
            Document doc = getSampleDocument();
            Element root = doc.getDocumentElement();
            Element book = SpecsXml.getElement(root, "book");

            // Execute
            assertThat(book).isNotNull();
            Element author = SpecsXml.getElement(book, "author");

            // Verify
            assertThat(author).isNotNull();
            assertThat(author.getTextContent()).isEqualTo("John Doe");
        }
    }

    @Nested
    @DisplayName("Schema Validation Tests")
    class SchemaValidationTests {

        @Test
        @DisplayName("getXmlRoot should accept schema validation")
        void testGetXmlRootWithSchema() {
            // Setup
            InputStream xmlStream = new ByteArrayInputStream(SAMPLE_XML.getBytes());
            // Using null schema for now since we don't have a schema file
            InputStream schemaStream = null;

            // Execute - should not throw exception
            assertThatCode(() -> {
                Document doc = SpecsXml.getXmlRoot(xmlStream, schemaStream);
                assertThat(doc).isNotNull();
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("URI Parsing Tests")
    class UriParsingTests {

        @Test
        @DisplayName("getXmlRootFromUri should handle invalid URI gracefully")
        void testGetXmlRootFromUriInvalid() {
            // Execute
            Document doc = SpecsXml.getXmlRootFromUri("invalid://uri");

            // Verify
            assertThat(doc).isNull();
        }

        @Test
        @DisplayName("getXmlRootFromUri should throw exception for null URI")
        void testGetXmlRootFromUriNull() {
            // Execute & Verify - null URI should throw IllegalArgumentException
            assertThatThrownBy(() -> SpecsXml.getXmlRootFromUri(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("URI cannot be null");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesErrorHandlingTests {

        @Test
        @DisplayName("methods should throw NPE for null document")
        void testNullDocumentHandling() {
            // Execute & Verify - null document should throw NPE
            assertThatThrownBy(() -> SpecsXml.getAttribute((Document) null, "tag", "attr"))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("methods should throw NPE for null elements")
        void testNullElementHandling() {
            // Execute & Verify - null element should throw NPE
            assertThatThrownBy(() -> SpecsXml.getAttribute((Element) null, "tag", "attr"))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("methods should throw NPE for null NodeList")
        void testNullNodeListHandling() {
            // Execute & Verify - null NodeList should throw NPE
            assertThatThrownBy(() -> SpecsXml.getNode(null, "tag"))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("methods should handle empty tag names by throwing RuntimeException")
        void testEmptyTagNames() {
            // Setup
            Document doc = SpecsXml.getXmlRoot(SAMPLE_XML);
            Element root = doc.getDocumentElement();

            // Execute & Verify - empty tag should cause RuntimeException in getText
            assertThatThrownBy(() -> SpecsXml.getText(root.getChildNodes(), ""))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find a node with tag ''");
        }

        @ParameterizedTest
        @ValueSource(strings = { "", "   ", "\n", "\t" })
        @DisplayName("methods should handle whitespace-only strings")
        void testWhitespaceHandling(String whitespace) {
            // Setup
            Document doc = SpecsXml.getXmlRoot(SAMPLE_XML);

            // Execute - should handle whitespace gracefully
            assertThatCode(() -> {
                SpecsXml.getAttribute(doc, whitespace, "attr");
                SpecsXml.getElement(doc.getDocumentElement(), whitespace);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("utility class has public constructor (not ideal but actual implementation)")
        void testUtilityClassNotInstantiable() {
            // SpecsXml actually has a public constructor, unlike other utility classes
            // This is not ideal but reflects the actual implementation
            assertThat(SpecsXml.class.getConstructors()).hasSize(1);

            // Verify it can be instantiated (even though it shouldn't be)
            assertThatCode(() -> new SpecsXml()).doesNotThrowAnyException();
        }
    }
}
