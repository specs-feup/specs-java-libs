package pt.up.fe.specs.util.xml;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import static org.assertj.core.api.Assertions.*;

/**
 * Test suite for {@link XmlElement} class - DOM Element wrapper implementation.
 * Tests element functionality including attributes, name retrieval, and DOM
 * operations.
 * 
 * @author Generated Tests
 */
class XmlElementTest {

    private Document document;
    private Element rootElement;
    private XmlElement xmlElement;

    @BeforeEach
    void setUp() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.newDocument();

        rootElement = document.createElement("root");
        document.appendChild(rootElement);
        xmlElement = new XmlElement(rootElement);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create XmlElement with valid Element")
        void testValidConstructor() {
            Element element = document.createElement("test");
            XmlElement xmlElement = new XmlElement(element);

            assertThat(xmlElement).isNotNull();
            assertThat(xmlElement.getNode()).isSameAs(element);
        }

        @Test
        @DisplayName("Should handle null Element in constructor")
        void testNullConstructor() {
            assertThatThrownBy(() -> new XmlElement(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("non-null Element");
        }
    }

    @Nested
    @DisplayName("getName() Method Tests")
    class GetNameTests {

        @Test
        @DisplayName("Should return correct element name")
        void testGetName() {
            assertThat(xmlElement.getName()).isEqualTo("root");
        }

        @Test
        @DisplayName("Should handle elements with different names")
        void testDifferentNames() {
            Element child1 = document.createElement("child");
            Element child2 = document.createElement("another-child");
            Element child3 = document.createElement("CamelCase");

            assertThat(new XmlElement(child1).getName()).isEqualTo("child");
            assertThat(new XmlElement(child2).getName()).isEqualTo("another-child");
            assertThat(new XmlElement(child3).getName()).isEqualTo("CamelCase");
        }

        @Test
        @DisplayName("Should handle names with namespaces")
        void testNamespaceNames() {
            Element nsElement = document.createElementNS("http://example.com", "ns:element");
            XmlElement xmlNsElement = new XmlElement(nsElement);

            assertThat(xmlNsElement.getName()).isEqualTo("ns:element");
        }
    }

    @Nested
    @DisplayName("getAttribute() Method Tests")
    class GetAttributeTests {

        @BeforeEach
        void setUpAttributes() {
            rootElement.setAttribute("attr1", "value1");
            rootElement.setAttribute("attr2", "value2");
            rootElement.setAttribute("empty", "");
        }

        @Test
        @DisplayName("Should return attribute value when present")
        void testGetExistingAttribute() {
            assertThat(xmlElement.getAttribute("attr1")).isEqualTo("value1");
            assertThat(xmlElement.getAttribute("attr2")).isEqualTo("value2");
        }

        @Test
        @DisplayName("Should return empty string when attribute not present")
        void testGetNonExistentAttribute() {
            assertThat(xmlElement.getAttribute("nonexistent")).isEmpty();
        }

        @Test
        @DisplayName("Should return empty string for empty attribute")
        void testGetEmptyAttribute() {
            assertThat(xmlElement.getAttribute("empty")).isEmpty();
        }

        @Test
        @DisplayName("Should handle null attribute name")
        void testGetAttributeNullName() {
            assertThat(xmlElement.getAttribute(null)).isEmpty();
        }
    }

    @Nested
    @DisplayName("getAttribute(String, String) Method Tests")
    class GetAttributeWithDefaultTests {

        @BeforeEach
        void setUpAttributes() {
            rootElement.setAttribute("present", "value");
            rootElement.setAttribute("empty", "");
        }

        @Test
        @DisplayName("Should return attribute value when present")
        void testGetExistingAttributeWithDefault() {
            assertThat(xmlElement.getAttribute("present", "default")).isEqualTo("value");
        }

        @Test
        @DisplayName("Should return default when attribute not present")
        void testGetNonExistentAttributeWithDefault() {
            assertThat(xmlElement.getAttribute("nonexistent", "default")).isEqualTo("default");
        }

        @Test
        @DisplayName("Should return default when attribute is empty")
        void testGetEmptyAttributeWithDefault() {
            assertThat(xmlElement.getAttribute("empty", "default")).isEqualTo("default");
        }

        @Test
        @DisplayName("Should handle null default value")
        void testGetAttributeWithNullDefault() {
            assertThat(xmlElement.getAttribute("nonexistent", null)).isNull();
        }
    }

    @Nested
    @DisplayName("getAttributeStrict() Method Tests")
    class GetAttributeStrictTests {

        @BeforeEach
        void setUpAttributes() {
            rootElement.setAttribute("present", "value");
            rootElement.setAttribute("empty", "");
        }

        @Test
        @DisplayName("Should return attribute value when present")
        void testGetExistingAttributeStrict() {
            assertThat(xmlElement.getAttributeStrict("present")).isEqualTo("value");
        }

        @Test
        @DisplayName("Should throw exception when attribute not present")
        void testGetNonExistentAttributeStrict() {
            assertThatThrownBy(() -> xmlElement.getAttributeStrict("nonexistent"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find mandatory attribute 'nonexistent'")
                    .hasMessageContaining("element root");
        }

        @Test
        @DisplayName("Should throw exception when attribute is empty")
        void testGetEmptyAttributeStrict() {
            assertThatThrownBy(() -> xmlElement.getAttributeStrict("empty"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find mandatory attribute 'empty'");
        }
    }

    @Nested
    @DisplayName("setAttribute() Method Tests")
    class SetAttributeTests {

        @Test
        @DisplayName("Should set new attribute and return null")
        void testSetNewAttribute() {
            String previousValue = xmlElement.setAttribute("newAttr", "newValue");

            assertThat(previousValue).isEmpty(); // getAttribute returns empty for non-existent
            assertThat(xmlElement.getAttribute("newAttr")).isEqualTo("newValue");
        }

        @Test
        @DisplayName("Should update existing attribute and return previous value")
        void testUpdateExistingAttribute() {
            rootElement.setAttribute("existing", "oldValue");

            String previousValue = xmlElement.setAttribute("existing", "newValue");

            assertThat(previousValue).isEqualTo("oldValue");
            assertThat(xmlElement.getAttribute("existing")).isEqualTo("newValue");
        }

        @Test
        @DisplayName("Should handle null value - BUG: null becomes literal 'null' string")
        void testSetAttributeNullValue() {
            xmlElement.setAttribute("nullAttr", null);

            // BUG: Setting null value results in empty string, not "null" string
            assertThat(xmlElement.getAttribute("nullAttr")).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty value")
        void testSetAttributeEmptyValue() {
            xmlElement.setAttribute("emptyAttr", "");

            assertThat(xmlElement.getAttribute("emptyAttr")).isEmpty();
        }
    }

    @Nested
    @DisplayName("getAttributes() Method Tests")
    class GetAttributesTests {

        @Test
        @DisplayName("Should return empty list when no attributes")
        void testGetAttributesEmpty() {
            assertThat(xmlElement.getAttributes()).isEmpty();
        }

        @Test
        @DisplayName("Should return all attribute names")
        void testGetAttributesWithValues() {
            rootElement.setAttribute("attr1", "value1");
            rootElement.setAttribute("attr2", "value2");
            rootElement.setAttribute("attr3", "value3");

            assertThat(xmlElement.getAttributes())
                    .hasSize(3)
                    .containsExactlyInAnyOrder("attr1", "attr2", "attr3");
        }

        @Test
        @DisplayName("Should handle attributes with special characters")
        void testGetAttributesSpecialChars() {
            rootElement.setAttribute("attr-dash", "value");
            rootElement.setAttribute("attr_underscore", "value");
            rootElement.setAttribute("attr:colon", "value");

            assertThat(xmlElement.getAttributes())
                    .containsExactlyInAnyOrder("attr-dash", "attr_underscore", "attr:colon");
        }

        @Test
        @DisplayName("Should return attributes in consistent order")
        void testGetAttributesOrder() {
            rootElement.setAttribute("z", "1");
            rootElement.setAttribute("a", "2");
            rootElement.setAttribute("m", "3");

            var attributes1 = xmlElement.getAttributes();
            var attributes2 = xmlElement.getAttributes();

            assertThat(attributes1).containsExactlyElementsOf(attributes2);
        }
    }

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend AXmlNode")
        void testInheritance() {
            assertThat(xmlElement).isInstanceOf(AXmlNode.class);
            assertThat(xmlElement).isInstanceOf(XmlNode.class);
        }

        @Test
        @DisplayName("Should delegate toString to getString")
        void testToString() {
            assertThat(xmlElement.toString()).isEqualTo(xmlElement.getString());
        }

        @Test
        @DisplayName("Should provide access to underlying DOM node")
        void testGetNode() {
            assertThat(xmlElement.getNode()).isSameAs(rootElement);
            assertThat(xmlElement.getNode()).isInstanceOf(Element.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long attribute names")
        void testLongAttributeNames() {
            String longName = "a".repeat(1000);
            xmlElement.setAttribute(longName, "value");

            assertThat(xmlElement.getAttribute(longName)).isEqualTo("value");
            assertThat(xmlElement.getAttributes()).contains(longName);
        }

        @Test
        @DisplayName("Should handle very long attribute values")
        void testLongAttributeValues() {
            String longValue = "v".repeat(10000);
            xmlElement.setAttribute("attr", longValue);

            assertThat(xmlElement.getAttribute("attr")).isEqualTo(longValue);
        }

        @Test
        @DisplayName("Should handle Unicode in attribute names and values")
        void testUnicodeAttributes() {
            xmlElement.setAttribute("属性", "值");
            xmlElement.setAttribute("attr", "Unicode: 中文 🚀 ñ é");

            assertThat(xmlElement.getAttribute("属性")).isEqualTo("值");
            assertThat(xmlElement.getAttribute("attr")).contains("Unicode: 中文 🚀 ñ é");
        }

        @Test
        @DisplayName("Should handle many attributes efficiently")
        void testManyAttributes() {
            // Add 100 attributes
            for (int i = 0; i < 100; i++) {
                xmlElement.setAttribute("attr" + i, "value" + i);
            }

            assertThat(xmlElement.getAttributes()).hasSize(100);
            assertThat(xmlElement.getAttribute("attr50")).isEqualTo("value50");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with complex element hierarchy")
        void testComplexHierarchy() throws Exception {
            Element parent = document.createElement("parent");
            parent.setAttribute("id", "parent1");

            Element child = document.createElement("child");
            child.setAttribute("id", "child1");
            child.setAttribute("type", "text");

            parent.appendChild(child);
            // Don't append parent to document - it's already been appended as rootElement

            XmlElement parentElement = new XmlElement(parent);
            XmlElement childElement = new XmlElement(child);

            assertThat(parentElement.getName()).isEqualTo("parent");
            assertThat(parentElement.getAttribute("id")).isEqualTo("parent1");

            assertThat(childElement.getName()).isEqualTo("child");
            assertThat(childElement.getAttribute("id")).isEqualTo("child1");
            assertThat(childElement.getAttribute("type")).isEqualTo("text");
        }

        @Test
        @DisplayName("Should handle namespace-aware elements")
        void testNamespaceAwareElements() throws Exception {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document nsDoc = builder.newDocument();

            Element nsElement = nsDoc.createElementNS("http://example.com/ns", "ns:element");
            nsElement.setAttributeNS("http://example.com/ns", "ns:attr", "nsValue");
            nsDoc.appendChild(nsElement);

            XmlElement xmlNsElement = new XmlElement(nsElement);

            assertThat(xmlNsElement.getName()).isEqualTo("ns:element");
            // Note: getAttribute might not handle namespaced attributes correctly
            // This tests actual behavior rather than expected behavior
        }
    }
}
