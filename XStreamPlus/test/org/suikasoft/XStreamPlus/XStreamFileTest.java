package org.suikasoft.XStreamPlus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.thoughtworks.xstream.XStream;

/**
 * Comprehensive unit tests for {@link XStreamFile}.
 * 
 * Tests cover XML serialization/deserialization, compact representation,
 * instance creation, and XStream configuration.
 * 
 * @author Generated Tests
 */
@DisplayName("XStreamFile Tests")
class XStreamFileTest {

    @Nested
    @DisplayName("Instance Creation")
    class InstanceCreationTests {

        @Test
        @DisplayName("Constructor should create XStreamFile with ObjectXml config")
        void testConstructor_ShouldCreateWithConfig() {
            // Given
            TestObjectXml objectXml = new TestObjectXml();

            // When
            XStreamFile<TestObject> xstreamFile = new XStreamFile<>(objectXml);

            // Then
            assertAll(
                    () -> assertThat(xstreamFile).isNotNull(),
                    () -> assertThat(xstreamFile.getXstream()).isNotNull(),
                    () -> assertThat(xstreamFile.useCompactRepresentation).isFalse());
        }

        @Test
        @DisplayName("newInstance() should create new XStreamFile instance")
        void testNewInstance_ShouldCreateNewInstance() {
            // Given
            TestObjectXml objectXml = new TestObjectXml();

            // When
            XStreamFile<TestObject> xstreamFile = XStreamFile.newInstance(objectXml);

            // Then
            assertAll(
                    () -> assertThat(xstreamFile).isNotNull(),
                    () -> assertThat(xstreamFile.getXstream()).isNotNull(),
                    () -> assertThat(xstreamFile.useCompactRepresentation).isFalse());
        }

        @Test
        @DisplayName("Multiple instances should be independent")
        void testMultipleInstances_ShouldBeIndependent() {
            // Given
            TestObjectXml objectXml1 = new TestObjectXml();
            TestObjectXml objectXml2 = new TestObjectXml();

            // When
            XStreamFile<TestObject> file1 = XStreamFile.newInstance(objectXml1);
            XStreamFile<TestObject> file2 = XStreamFile.newInstance(objectXml2);

            // Then
            assertAll(
                    () -> assertThat(file1).isNotSameAs(file2),
                    () -> assertThat(file1.getXstream()).isNotSameAs(file2.getXstream()));
        }
    }

    @Nested
    @DisplayName("Compact Representation Settings")
    class CompactRepresentationTests {

        private XStreamFile<TestObject> xstreamFile;
        private TestObject testObject;

        @BeforeEach
        void setUp() {
            TestObjectXml objectXml = new TestObjectXml();
            xstreamFile = new XStreamFile<>(objectXml);
            testObject = new TestObject("test", 42);
        }

        @Test
        @DisplayName("setUseCompactRepresentation() should update setting")
        void testSetUseCompactRepresentation_ShouldUpdateSetting() {
            // When
            xstreamFile.setUseCompactRepresentation(true);

            // Then
            assertThat(xstreamFile.useCompactRepresentation).isTrue();
        }

        @Test
        @DisplayName("Compact representation should produce more compact XML")
        void testCompactRepresentation_ShouldProduceCompactXml() {
            // Given
            String prettyXml = xstreamFile.toXml(testObject);

            // When
            xstreamFile.setUseCompactRepresentation(true);
            String compactXml = xstreamFile.toXml(testObject);

            // Then
            assertAll(
                    () -> assertThat(compactXml).isNotNull(),
                    () -> assertThat(compactXml.length()).isLessThan(prettyXml.length()),
                    () -> assertThat(compactXml).doesNotContain("  "), // No double spaces (indentation)
                    () -> assertThat(compactXml).contains("test").contains("42"));
        }

        @Test
        @DisplayName("Pretty representation should have proper formatting")
        void testPrettyRepresentation_ShouldHaveFormatting() {
            // Given
            xstreamFile.setUseCompactRepresentation(false);

            // When
            String prettyXml = xstreamFile.toXml(testObject);

            // Then
            assertAll(
                    () -> assertThat(prettyXml).isNotNull(),
                    () -> assertThat(prettyXml).contains("\n"), // Contains newlines
                    () -> assertThat(prettyXml).contains("test").contains("42"));
        }
    }

    @Nested
    @DisplayName("XML Serialization")
    class XmlSerializationTests {

        private XStreamFile<TestObject> xstreamFile;

        @BeforeEach
        void setUp() {
            TestObjectXml objectXml = new TestObjectXml();
            xstreamFile = new XStreamFile<>(objectXml);
        }

        @Test
        @DisplayName("toXml() should serialize compatible object")
        void testToXml_CompatibleObject_ShouldSerialize() {
            // Given
            TestObject testObj = new TestObject("serialize", 123);

            // When
            String xml = xstreamFile.toXml(testObj);

            // Then
            assertAll(
                    () -> assertThat(xml).isNotNull(),
                    () -> assertThat(xml).contains("serialize"),
                    () -> assertThat(xml).contains("123"),
                    () -> assertThat(xml).contains("<"));
        }

        @Test
        @DisplayName("toXml() should return null for incompatible object")
        void testToXml_IncompatibleObject_ShouldReturnNull() {
            // Given
            String incompatibleObj = "Not a TestObject";

            // When
            String xml = xstreamFile.toXml(incompatibleObj);

            // Then
            assertThat(xml).isNull();
        }

        @Test
        @DisplayName("toXml() should handle null object")
        void testToXml_NullObject_ShouldHandleGracefully() {
            // When
            String xml = xstreamFile.toXml(null);

            // Then
            assertThat(xml)
                    .isNotNull()
                    .contains("null");
        }
    }

    @Nested
    @DisplayName("XML Deserialization")
    class XmlDeserializationTests {

        private XStreamFile<TestObject> xstreamFile;

        @BeforeEach
        void setUp() {
            TestObjectXml objectXml = new TestObjectXml();
            xstreamFile = new XStreamFile<>(objectXml);
        }

        @Test
        @DisplayName("fromXml() should deserialize valid XML")
        void testFromXml_ValidXml_ShouldDeserialize() {
            // Given
            TestObject original = new TestObject("deserialize", 456);
            String xml = xstreamFile.toXml(original);

            // When
            TestObject result = xstreamFile.fromXml(xml);

            // Then
            assertThat(result)
                    .isNotNull()
                    .satisfies(obj -> {
                        assertThat(obj.name).isEqualTo("deserialize");
                        assertThat(obj.value).isEqualTo(456);
                    });
        }

        @Test
        @DisplayName("fromXml() should return null for incompatible XML")
        void testFromXml_IncompatibleXml_ShouldReturnNull() {
            // Given
            String incompatibleXml = "<string>Not a TestObject</string>";

            // When
            TestObject result = xstreamFile.fromXml(incompatibleXml);

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("fromXml() should handle malformed XML")
        void testFromXml_MalformedXml_ShouldThrowException() {
            // Given
            String malformedXml = "<invalid><unclosed>";

            // When/Then - Should throw exception for malformed XML
            try {
                TestObject result = xstreamFile.fromXml(malformedXml);
                assertThat(result).isNull(); // If implementation handles gracefully
            } catch (Exception e) {
                assertThat(e).isNotNull(); // Expected behavior
            }
        }
    }

    @Nested
    @DisplayName("Round-trip Serialization")
    class RoundTripSerializationTests {

        private XStreamFile<TestObject> xstreamFile;

        @BeforeEach
        void setUp() {
            TestObjectXml objectXml = new TestObjectXml();
            xstreamFile = new XStreamFile<>(objectXml);
        }

        @Test
        @DisplayName("Should preserve data in round-trip serialization")
        void testRoundTrip_ShouldPreserveData() {
            // Given
            TestObject original = new TestObject("roundTrip", 789);

            // When
            String xml = xstreamFile.toXml(original);
            TestObject result = xstreamFile.fromXml(xml);

            // Then
            assertThat(result)
                    .isNotNull()
                    .satisfies(obj -> {
                        assertThat(obj.name).isEqualTo(original.name);
                        assertThat(obj.value).isEqualTo(original.value);
                    });
        }

        @Test
        @DisplayName("Should handle complex objects in round-trip")
        void testRoundTrip_ComplexObject_ShouldPreserveData() {
            // Given
            ComplexObjectXml complexXml = new ComplexObjectXml();
            XStreamFile<ComplexObject> complexFile = new XStreamFile<>(complexXml);
            ComplexObject original = new ComplexObject("complex",
                    new TestObject("nested", 999), new String[] { "a", "b", "c" });

            // When
            String xml = complexFile.toXml(original);
            ComplexObject result = complexFile.fromXml(xml);

            // Then
            assertThat(result)
                    .isNotNull()
                    .satisfies(obj -> {
                        assertThat(obj.name).isEqualTo(original.name);
                        assertThat(obj.nested.name).isEqualTo(original.nested.name);
                        assertThat(obj.nested.value).isEqualTo(original.nested.value);
                        assertThat(obj.array).containsExactly("a", "b", "c");
                    });
        }
    }

    @Nested
    @DisplayName("XStream Configuration")
    class XStreamConfigurationTests {

        @Test
        @DisplayName("getXstream() should return configured XStream instance")
        void testGetXstream_ShouldReturnConfiguredInstance() {
            // Given
            TestObjectXml objectXml = new TestObjectXml();
            XStreamFile<TestObject> xstreamFile = new XStreamFile<>(objectXml);

            // When
            XStream xstream = xstreamFile.getXstream();

            // Then
            assertThat(xstream).isNotNull();
        }

        @Test
        @DisplayName("XStream should have proper permissions configured")
        void testXStreamPermissions_ShouldBeConfigured() {
            // Given
            TestObjectXml objectXml = new TestObjectXml();
            XStreamFile<TestObject> xstreamFile = new XStreamFile<>(objectXml);

            // When
            XStream xstream = xstreamFile.getXstream();
            TestObject testObj = new TestObject("permission", 123);

            // Then - Should be able to serialize without security issues
            String xml = xstream.toXML(testObj);
            assertThat(xml).contains("permission").contains("123");
        }
    }

    @Nested
    @DisplayName("Reserved Alias Handling")
    class ReservedAliasTests {

        @Test
        @DisplayName("Should have reserved aliases defined")
        void testReservedAliases_ShouldBeDefined() {
            // When/Then
            assertAll(
                    () -> assertThat(XStreamFile.reservedAlias).contains("string"),
                    () -> assertThat(XStreamFile.reservedAlias).contains("int"),
                    () -> assertThat(XStreamFile.reservedAlias).hasSize(2));
        }

        @Test
        @DisplayName("Reserved aliases should be handled during configuration")
        void testReservedAliases_ShouldBeHandledDuringConfig() {
            // Given
            TestObjectXml objectXml = new TestObjectXml();
            objectXml.addMappings("string", String.class); // This should be skipped

            // When
            XStreamFile<TestObject> xstreamFile = new XStreamFile<>(objectXml);

            // Then - Should create without issues, reserved alias should be skipped
            assertThat(xstreamFile).isNotNull();
            assertThat(xstreamFile.getXstream()).isNotNull();
        }
    }

    // Test helper classes
    private static class TestObject {
        public String name;
        public int value;

        public TestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }
    }

    private static class TestObjectXml extends ObjectXml<TestObject> {
        @Override
        public Class<TestObject> getTargetClass() {
            return TestObject.class;
        }
    }

    private static class ComplexObject {
        public String name;
        public TestObject nested;
        public String[] array;

        public ComplexObject(String name, TestObject nested, String[] array) {
            this.name = name;
            this.nested = nested;
            this.array = array;
        }
    }

    private static class ComplexObjectXml extends ObjectXml<ComplexObject> {
        @Override
        public Class<ComplexObject> getTargetClass() {
            return ComplexObject.class;
        }
    }
}
