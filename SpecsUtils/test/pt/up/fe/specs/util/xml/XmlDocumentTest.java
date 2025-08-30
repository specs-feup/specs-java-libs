package pt.up.fe.specs.util.xml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;

/**
 * Test suite for {@link XmlDocument} class - DOM Document wrapper
 * implementation.
 * Tests document creation, factory methods, and document-level operations.
 * 
 * @author Generated Tests
 */
class XmlDocumentTest {

    @TempDir
    Path tempDir;

    private Document document;
    private XmlDocument xmlDocument;

    @BeforeEach
    void setUp() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();

        // Create a simple document structure
        var root = document.createElement("root");
        root.setAttribute("version", "1.0");
        document.appendChild(root);

        xmlDocument = new XmlDocument(document);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create XmlDocument with valid Document")
        void testValidConstructor() {
            XmlDocument xmlDoc = new XmlDocument(document);

            assertThat(xmlDoc).isNotNull();
            assertThat(xmlDoc.getNode()).isSameAs(document);
        }

        @Test
        @DisplayName("Should handle null Document in constructor")
        void testNullConstructor() {
            assertThatThrownBy(() -> new XmlDocument(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("non-null Document");
        }
    }

    @Nested
    @DisplayName("Factory Method Tests - newInstance(String)")
    class NewInstanceStringTests {

        @Test
        @DisplayName("Should create document from valid XML string")
        void testNewInstanceFromValidXml() {
            String xml = "<?xml version=\"1.0\"?><root><child>value</child></root>";

            XmlDocument xmlDoc = XmlDocument.newInstance(xml);

            assertThat(xmlDoc).isNotNull();
            assertThat(xmlDoc.getNode()).isInstanceOf(Document.class);
        }

        @Test
        @DisplayName("Should create document from simple XML string")
        void testNewInstanceFromSimpleXml() {
            String xml = "<simple>content</simple>";

            XmlDocument xmlDoc = XmlDocument.newInstance(xml);

            assertThat(xmlDoc).isNotNull();
            assertThat(xmlDoc.getText()).contains("content");
        }

        @Test
        @DisplayName("Should handle XML with attributes")
        void testNewInstanceWithAttributes() {
            String xml = "<root id=\"123\" type=\"test\">content</root>";

            XmlDocument xmlDoc = XmlDocument.newInstance(xml);

            assertThat(xmlDoc).isNotNull();
        }

        @Test
        @DisplayName("Should handle empty XML elements")
        void testNewInstanceWithEmptyElements() {
            String xml = "<root><empty/></root>";

            XmlDocument xmlDoc = XmlDocument.newInstance(xml);

            assertThat(xmlDoc).isNotNull();
        }

        @Test
        @DisplayName("Should throw exception for invalid XML string")
        void testNewInstanceFromInvalidXml() {
            String invalidXml = "<root><unclosed>";

            assertThatThrownBy(() -> XmlDocument.newInstance(invalidXml))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle null XML string")
        void testNewInstanceFromNullString() {
            assertThatThrownBy(() -> XmlDocument.newInstance((String) null))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle empty XML string")
        void testNewInstanceFromEmptyString() {
            assertThatThrownBy(() -> XmlDocument.newInstance(""))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Factory Method Tests - newInstance(File)")
    class NewInstanceFileTests {

        @Test
        @DisplayName("Should create document from valid XML file")
        void testNewInstanceFromValidFile() throws Exception {
            File xmlFile = tempDir.resolve("test.xml").toFile();
            String xml = "<?xml version=\"1.0\"?><root><child>value</child></root>";
            java.nio.file.Files.write(xmlFile.toPath(), xml.getBytes());

            XmlDocument xmlDoc = XmlDocument.newInstance(xmlFile);

            assertThat(xmlDoc).isNotNull();
            assertThat(xmlDoc.getText()).contains("value");
        }

        @Test
        @DisplayName("Should throw exception for non-existent file")
        void testNewInstanceFromNonExistentFile() {
            File nonExistentFile = tempDir.resolve("nonexistent.xml").toFile();

            assertThatThrownBy(() -> XmlDocument.newInstance(nonExistentFile))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should throw exception for invalid XML file")
        void testNewInstanceFromInvalidFile() throws Exception {
            File invalidFile = tempDir.resolve("invalid.xml").toFile();
            java.nio.file.Files.write(invalidFile.toPath(), "<root><unclosed>".getBytes());

            assertThatThrownBy(() -> XmlDocument.newInstance(invalidFile))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle null file")
        void testNewInstanceFromNullFile() {
            assertThatThrownBy(() -> XmlDocument.newInstance((File) null))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Factory Method Tests - newInstance(InputStream)")
    class NewInstanceInputStreamTests {

        @Test
        @DisplayName("Should create document from valid XML input stream")
        void testNewInstanceFromValidInputStream() {
            String xml = "<?xml version=\"1.0\"?><root><child>value</child></root>";
            InputStream inputStream = new ByteArrayInputStream(xml.getBytes());

            XmlDocument xmlDoc = XmlDocument.newInstance(inputStream);

            assertThat(xmlDoc).isNotNull();
            assertThat(xmlDoc.getText()).contains("value");
        }

        @Test
        @DisplayName("Should handle empty input stream")
        void testNewInstanceFromEmptyInputStream() {
            InputStream emptyStream = new ByteArrayInputStream(new byte[0]);

            assertThatThrownBy(() -> XmlDocument.newInstance(emptyStream))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle null input stream")
        void testNewInstanceFromNullInputStream() {
            assertThatThrownBy(() -> XmlDocument.newInstance((InputStream) null))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Factory Method Tests - newInstance(InputStream, InputStream)")
    class NewInstanceInputStreamWithSchemaTests {

        @Test
        @DisplayName("Should create document with XML and null schema")
        void testNewInstanceWithNullSchema() {
            String xml = "<?xml version=\"1.0\"?><root><child>value</child></root>";
            InputStream xmlStream = new ByteArrayInputStream(xml.getBytes());

            XmlDocument xmlDoc = XmlDocument.newInstance(xmlStream, null);

            assertThat(xmlDoc).isNotNull();
        }

        @Test
        @DisplayName("Should handle both streams being null")
        void testNewInstanceWithBothNull() {
            assertThatThrownBy(() -> XmlDocument.newInstance(null, null))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Factory Method Tests - newInstanceFromUri(String)")
    class NewInstanceFromUriTests {

        @Test
        @DisplayName("Should handle null URI")
        void testNewInstanceFromNullUri() {
            assertThatThrownBy(() -> XmlDocument.newInstanceFromUri(null))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle empty URI")
        void testNewInstanceFromEmptyUri() {
            assertThatThrownBy(() -> XmlDocument.newInstanceFromUri(""))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle invalid URI")
        void testNewInstanceFromInvalidUri() {
            assertThatThrownBy(() -> XmlDocument.newInstanceFromUri("invalid-uri"))
                    .isInstanceOf(RuntimeException.class);
        }

        // Note: Testing with real URIs would require network access
        // which is not suitable for unit tests
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend AXmlNode")
        void testInheritance() {
            assertThat(xmlDocument).isInstanceOf(AXmlNode.class);
            assertThat(xmlDocument).isInstanceOf(XmlNode.class);
        }

        @Test
        @DisplayName("Should delegate toString to getString")
        void testToString() {
            assertThat(xmlDocument.toString()).isEqualTo(xmlDocument.getString());
        }

        @Test
        @DisplayName("Should provide access to underlying DOM document")
        void testGetNode() {
            assertThat(xmlDocument.getNode()).isSameAs(document);
            assertThat(xmlDocument.getNode()).isInstanceOf(Document.class);
        }
    }

    @Nested
    @DisplayName("XML Content Tests")
    class XmlContentTests {

        @Test
        @DisplayName("Should handle complex XML structures")
        void testComplexXmlStructure() {
            String complexXml = """
                    <?xml version="1.0" encoding="UTF-8"?>
                    <catalog>
                        <book id="1">
                            <title>XML Processing</title>
                            <author>John Doe</author>
                            <price currency="USD">29.99</price>
                        </book>
                        <book id="2">
                            <title>Java Programming</title>
                            <author>Jane Smith</author>
                            <price currency="EUR">35.50</price>
                        </book>
                    </catalog>
                    """;

            XmlDocument xmlDoc = XmlDocument.newInstance(complexXml);

            assertThat(xmlDoc).isNotNull();
            assertThat(xmlDoc.getText()).contains("XML Processing");
            assertThat(xmlDoc.getText()).contains("Jane Smith");
        }

        @Test
        @DisplayName("Should handle XML with namespaces")
        void testXmlWithNamespaces() {
            String nsXml = """
                    <?xml version="1.0"?>
                    <root xmlns:ns="http://example.com/namespace">
                        <ns:element>content</ns:element>
                    </root>
                    """;

            XmlDocument xmlDoc = XmlDocument.newInstance(nsXml);

            assertThat(xmlDoc).isNotNull();
        }

        @Test
        @DisplayName("Should handle XML with CDATA sections")
        void testXmlWithCData() {
            String cdataXml = """
                    <?xml version="1.0"?>
                    <root>
                        <content><![CDATA[This is <CDATA> content with special & chars]]></content>
                    </root>
                    """;

            XmlDocument xmlDoc = XmlDocument.newInstance(cdataXml);

            assertThat(xmlDoc).isNotNull();
            assertThat(xmlDoc.getText()).contains("special & chars");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle malformed XML gracefully")
        void testMalformedXmlHandling() {
            String[] malformedXmls = {
                    "<root><child></root>", // Mismatched tags
                    "<root><child><child></root>", // Unclosed nested tag
                    "<root attr=\"unclosed>content</root>", // Unclosed attribute
                    "<?xml version=\"1.0\"?><root>content", // Missing closing tag
                    "<root>>content</root>" // Invalid character
            };

            for (String malformedXml : malformedXmls) {
                assertThatThrownBy(() -> XmlDocument.newInstance(malformedXml))
                        .isInstanceOf(RuntimeException.class);
            }
        }

        @Test
        @DisplayName("Should handle very large XML documents")
        void testLargeXmlDocument() {
            StringBuilder largeXml = new StringBuilder("<?xml version=\"1.0\"?><root>");

            // Create a large XML with many elements
            for (int i = 0; i < 1000; i++) {
                largeXml.append("<item id=\"").append(i).append("\">")
                        .append("Content ").append(i)
                        .append("</item>");
            }
            largeXml.append("</root>");

            XmlDocument xmlDoc = XmlDocument.newInstance(largeXml.toString());

            assertThat(xmlDoc).isNotNull();
            assertThat(xmlDoc.getText()).contains("Content 999");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with XmlElement access")
        void testXmlElementIntegration() {
            String xml = "<root><child attr=\"value\">text</child></root>";
            XmlDocument xmlDoc = XmlDocument.newInstance(xml);

            // Should be able to navigate to elements
            assertThat(xmlDoc.getChildren()).isNotEmpty();

            // Note: Detailed element navigation would require
            // implementing helper methods or using XmlNodes.create()
        }

        @Test
        @DisplayName("Should handle round-trip XML processing")
        void testRoundTripProcessing() throws Exception {
            String originalXml = "<?xml version=\"1.0\"?><root><child>value</child></root>";

            // Create document from string
            XmlDocument xmlDoc = XmlDocument.newInstance(originalXml);

            // Write to file
            File outputFile = tempDir.resolve("output.xml").toFile();
            xmlDoc.write(outputFile);

            // Read back from file
            XmlDocument reloadedDoc = XmlDocument.newInstance(outputFile);

            assertThat(reloadedDoc).isNotNull();
            assertThat(reloadedDoc.getText()).contains("value");
        }
    }
}
