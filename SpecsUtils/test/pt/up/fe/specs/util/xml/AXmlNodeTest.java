package pt.up.fe.specs.util.xml;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;
import org.w3c.dom.Node;

import static org.assertj.core.api.Assertions.*;

/**
 * Test suite for {@link AXmlNode} class - Abstract base implementation of
 * XmlNode interface.
 * Tests abstract node functionality including toString behavior and inheritance
 * patterns.
 * 
 * @author Generated Tests
 */
class AXmlNodeTest {

    /**
     * Concrete implementation of AXmlNode for testing purposes.
     */
    private static class TestXmlNode extends AXmlNode {
        private final String content;

        public TestXmlNode(String content) {
            this.content = content;
        }

        @Override
        public Node getNode() {
            return null; // Simplified for testing
        }

        @Override
        public String getString() {
            return content;
        }
    }

    @Nested
    @DisplayName("toString() Method Tests")
    class ToStringTests {

        @Test
        @DisplayName("Should delegate toString to getString method")
        void testToStringDelegation() {
            TestXmlNode node = new TestXmlNode("test content");

            assertThat(node.toString()).isEqualTo("test content");
            assertThat(node.toString()).isEqualTo(node.getString());
        }

        @Test
        @DisplayName("Should handle null content in toString")
        void testToStringWithNullContent() {
            TestXmlNode node = new TestXmlNode(null);

            assertThat(node.toString()).isNull();
            assertThat(node.toString()).isEqualTo(node.getString());
        }

        @Test
        @DisplayName("Should handle empty content in toString")
        void testToStringWithEmptyContent() {
            TestXmlNode node = new TestXmlNode("");

            assertThat(node.toString()).isEmpty();
            assertThat(node.toString()).isEqualTo(node.getString());
        }

        @Test
        @DisplayName("Should handle complex XML content in toString")
        void testToStringWithComplexContent() {
            String xmlContent = "<root><child>value</child></root>";
            TestXmlNode node = new TestXmlNode(xmlContent);

            assertThat(node.toString()).isEqualTo(xmlContent);
        }
    }

    @Nested
    @DisplayName("Inheritance and Interface Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should implement XmlNode interface")
        void testXmlNodeInterface() {
            TestXmlNode node = new TestXmlNode("test");

            assertThat(node).isInstanceOf(XmlNode.class);
            assertThat(node).isInstanceOf(AXmlNode.class);
        }

        @Test
        @DisplayName("Should provide abstract base for concrete implementations - BUG: Default methods with null node throw NPE")
        void testAbstractBasePattern() {
            TestXmlNode node = new TestXmlNode("test");

            // Test that we can call interface methods that have default implementations
            // BUG: getText() throws NPE when getNode() returns null
            assertThatThrownBy(() -> node.getText())
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke \"org.w3c.dom.Node.getTextContent()\"");

            // BUG: getChildren() also throws NPE when getNode() returns null
            assertThatThrownBy(() -> node.getChildren())
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke \"org.w3c.dom.Node.getChildNodes()\"");
        }

        @Test
        @DisplayName("Should allow multiple concrete implementations")
        void testMultipleImplementations() {
            class AnotherTestNode extends AXmlNode {
                @Override
                public Node getNode() {
                    return null;
                }

                @Override
                public String getString() {
                    return "another";
                }
            }

            TestXmlNode node1 = new TestXmlNode("first");
            AnotherTestNode node2 = new AnotherTestNode();

            assertThat(node1.toString()).isEqualTo("first");
            assertThat(node2.toString()).isEqualTo("another");

            assertThat(node1).isInstanceOf(AXmlNode.class);
            assertThat(node2).isInstanceOf(AXmlNode.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long content strings")
        void testLongContentStrings() {
            String longContent = "a".repeat(10000);
            TestXmlNode node = new TestXmlNode(longContent);

            assertThat(node.toString()).hasSize(10000);
            assertThat(node.toString()).isEqualTo(longContent);
        }

        @Test
        @DisplayName("Should handle content with special characters")
        void testSpecialCharacters() {
            String specialContent = "Content with \n\t\r special chars & <tags>";
            TestXmlNode node = new TestXmlNode(specialContent);

            assertThat(node.toString()).isEqualTo(specialContent);
        }

        @Test
        @DisplayName("Should handle Unicode content")
        void testUnicodeContent() {
            String unicodeContent = "Unicode: 中文 🚀 ñ é";
            TestXmlNode node = new TestXmlNode(unicodeContent);

            assertThat(node.toString()).isEqualTo(unicodeContent);
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @RetryingTest(5)
        @DisplayName("Should perform toString efficiently")
        void testToStringPerformance() {
            TestXmlNode node = new TestXmlNode("test content");

            // Measure time for multiple toString calls
            long startTime = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                node.toString();
            }
            long endTime = System.nanoTime();

            // Should complete quickly (less than 10ms for 1000 calls)
            long durationMs = (endTime - startTime) / 1_000_000;
            assertThat(durationMs).isLessThan(10);
        }

        @Test
        @DisplayName("Should handle repeated toString calls consistently")
        void testRepeatedToString() {
            TestXmlNode node = new TestXmlNode("consistent content");

            String firstCall = node.toString();
            String secondCall = node.toString();
            String thirdCall = node.toString();

            assertThat(firstCall).isEqualTo(secondCall);
            assertThat(secondCall).isEqualTo(thirdCall);
            assertThat(firstCall).isEqualTo("consistent content");
        }
    }
}
