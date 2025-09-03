package org.suikasoft.XStreamPlus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Comprehensive unit tests for {@link ObjectXml}.
 * 
 * Tests cover XML serialization/deserialization, mapping management, nested XML
 * handling, and custom converter registration.
 * 
 * @author Generated Tests
 */
@DisplayName("ObjectXml Tests")
class ObjectXmlTest {

    @Nested
    @DisplayName("Basic Functionality")
    class BasicFunctionalityTests {

        private TestObjectXml objectXml;
        private TestObject testObject;

        @BeforeEach
        void setUp() {
            objectXml = new TestObjectXml();
            testObject = new TestObject("test", 42);
        }

        @Test
        @DisplayName("getTargetClass() should return correct class")
        void testGetTargetClass_ShouldReturnCorrectClass() {
            // When
            Class<TestObject> targetClass = objectXml.getTargetClass();

            // Then
            assertThat(targetClass).isEqualTo(TestObject.class);
        }

        @Test
        @DisplayName("toXml() should serialize object to XML string")
        void testToXml_ValidObject_ShouldSerializeToXml() {
            // When
            String xml = objectXml.toXml(testObject);

            // Then
            assertAll(
                    () -> assertThat(xml).isNotNull(),
                    () -> assertThat(xml).contains("test"),
                    () -> assertThat(xml).contains("42"),
                    () -> assertThat(xml).contains("<"));
        }

        @Test
        @DisplayName("fromXml() should deserialize XML string to object")
        void testFromXml_ValidXml_ShouldDeserializeToObject() {
            // Given
            String xml = objectXml.toXml(testObject);

            // When
            TestObject result = objectXml.fromXml(xml);

            // Then
            assertThat(result)
                    .isNotNull()
                    .satisfies(obj -> {
                        assertThat(obj.name).isEqualTo("test");
                        assertThat(obj.value).isEqualTo(42);
                    });
        }

        @Test
        @DisplayName("Should handle round-trip serialization correctly")
        void testRoundTripSerialization_ShouldPreserveData() {
            // Given
            TestObject original = new TestObject("roundTrip", 123);

            // When
            String xml = objectXml.toXml(original);
            TestObject result = objectXml.fromXml(xml);

            // Then
            assertThat(result)
                    .isNotNull()
                    .satisfies(obj -> {
                        assertThat(obj.name).isEqualTo(original.name);
                        assertThat(obj.value).isEqualTo(original.value);
                    });
        }
    }

    @Nested
    @DisplayName("Mapping Management")
    class MappingManagementTests {

        private TestObjectXml objectXml;

        @BeforeEach
        void setUp() {
            objectXml = new TestObjectXml();
        }

        @Test
        @DisplayName("getMappings() should return empty map initially")
        void testGetMappings_Initial_ShouldBeEmpty() {
            // When
            Map<String, Class<?>> mappings = objectXml.getMappings();

            // Then
            assertThat(mappings)
                    .as("Initial mappings should be empty")
                    .isEmpty();
        }

        @Test
        @DisplayName("addMappings() should add single mapping")
        void testAddMappings_SingleMapping_ShouldAddCorrectly() {
            // When
            objectXml.addMappings("testAlias", TestObject.class);

            // Then
            Map<String, Class<?>> mappings = objectXml.getMappings();
            assertThat(mappings)
                    .hasSize(1)
                    .containsEntry("testAlias", TestObject.class);
        }

        @Test
        @DisplayName("addMappings() should throw exception for duplicate mapping")
        void testAddMappings_DuplicateMapping_ShouldThrowException() {
            // Given
            objectXml.addMappings("duplicate", TestObject.class);

            // When/Then
            assertThatThrownBy(() -> objectXml.addMappings("duplicate", String.class))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("already present");
        }

        @Test
        @DisplayName("addMappings() should add multiple mappings from map")
        void testAddMappings_MultipleFromMap_ShouldAddAll() {
            // Given
            Map<String, Class<?>> mappingsToAdd = new HashMap<>();
            mappingsToAdd.put("alias1", TestObject.class);
            mappingsToAdd.put("alias2", String.class);
            mappingsToAdd.put("alias3", Integer.class);

            // When
            objectXml.addMappings(mappingsToAdd);

            // Then
            Map<String, Class<?>> mappings = objectXml.getMappings();
            assertThat(mappings)
                    .hasSize(3)
                    .containsEntry("alias1", TestObject.class)
                    .containsEntry("alias2", String.class)
                    .containsEntry("alias3", Integer.class);
        }

        @Test
        @DisplayName("addMappings() should add mappings from class list using simple names")
        void testAddMappings_FromClassList_ShouldUseSimpleNames() {
            // Given
            List<Class<?>> classes = List.of(TestObject.class, String.class, Integer.class);

            // When
            objectXml.addMappings(classes);

            // Then
            Map<String, Class<?>> mappings = objectXml.getMappings();
            assertThat(mappings)
                    .hasSize(3)
                    .containsEntry("TestObject", TestObject.class)
                    .containsEntry("String", String.class)
                    .containsEntry("Integer", Integer.class);
        }
    }

    @Nested
    @DisplayName("Nested XML Handling")
    class NestedXmlTests {

        private TestObjectXml parentXml;
        private NestedObjectXml nestedXml;

        @BeforeEach
        void setUp() {
            parentXml = new TestObjectXml();
            nestedXml = new NestedObjectXml();
        }

        @Test
        @DisplayName("getNestedXml() should return empty map initially")
        void testGetNestedXml_Initial_ShouldBeEmpty() {
            // When
            Map<Class<?>, ObjectXml<?>> nestedXmlMap = parentXml.getNestedXml();

            // Then
            assertThat(nestedXmlMap)
                    .as("Initial nested XML map should be empty")
                    .isEmpty();
        }

        @Test
        @DisplayName("addNestedXml() should add nested ObjectXml")
        void testAddNestedXml_ShouldAddCorrectly() {
            // When
            parentXml.addNestedXml(nestedXml);

            // Then
            Map<Class<?>, ObjectXml<?>> nestedXmlMap = parentXml.getNestedXml();
            assertThat(nestedXmlMap)
                    .hasSize(1)
                    .containsKey(NestedObject.class)
                    .containsValue(nestedXml);
        }

        @Test
        @DisplayName("addNestedXml() should replace existing nested ObjectXml")
        void testAddNestedXml_Replacement_ShouldReplaceExisting() {
            // Given
            NestedObjectXml firstNested = new NestedObjectXml();
            NestedObjectXml secondNested = new NestedObjectXml();

            parentXml.addNestedXml(firstNested);

            // When
            parentXml.addNestedXml(secondNested);

            // Then
            Map<Class<?>, ObjectXml<?>> nestedXmlMap = parentXml.getNestedXml();
            assertThat(nestedXmlMap)
                    .hasSize(1)
                    .containsValue(secondNested)
                    .doesNotContainValue(firstNested);
        }
    }

    @Nested
    @DisplayName("Custom Converter Registration")
    class CustomConverterTests {

        private TestObjectXml objectXml;

        @BeforeEach
        void setUp() {
            objectXml = new TestObjectXml();
        }

        @Test
        @DisplayName("registerConverter() should register custom converter")
        void testRegisterConverter_ShouldRegisterCorrectly() {
            // Given
            StringCodec<CustomType> codec = new StringCodec<CustomType>() {
                @Override
                public String encode(CustomType object) {
                    return "encoded:" + object.value;
                }

                @Override
                public CustomType decode(String string) {
                    String value = string.substring(8); // Remove "encoded:" prefix
                    return new CustomType(value);
                }
            };

            // When
            objectXml.registerConverter(CustomType.class, codec);

            // Then - Test by serializing/deserializing object with custom type
            ComplexTestObject complexObj = new ComplexTestObject("test", new CustomType("custom"));
            ComplexObjectXml complexXml = new ComplexObjectXml();
            complexXml.registerConverter(CustomType.class, codec);

            String xml = complexXml.toXml(complexObj);
            ComplexTestObject result = complexXml.fromXml(xml);

            assertThat(result.name).isEqualTo("test");
            assertThat(result.customField.value).isEqualTo("custom");
        }
    }

    @Nested
    @DisplayName("XStreamFile Access")
    class XStreamFileAccessTests {

        private TestObjectXml objectXml;

        @BeforeEach
        void setUp() {
            objectXml = new TestObjectXml();
        }

        @Test
        @DisplayName("getXStreamFile() should return non-null XStreamFile")
        void testGetXStreamFile_ShouldReturnNonNull() {
            // When
            XStreamFile<TestObject> xstreamFile = objectXml.getXStreamFile();

            // Then
            assertThat(xstreamFile)
                    .as("XStreamFile should not be null")
                    .isNotNull();
        }

        @Test
        @DisplayName("XStreamFile should be properly configured")
        void testXStreamFile_ShouldBeProperlyConfigured() {
            // When
            XStreamFile<TestObject> xstreamFile = objectXml.getXStreamFile();

            // Then
            assertThat(xstreamFile.getXstream())
                    .as("XStream instance should not be null")
                    .isNotNull();
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        private TestObjectXml objectXml;

        @BeforeEach
        void setUp() {
            objectXml = new TestObjectXml();
        }

        @Test
        @DisplayName("fromXml() should handle invalid XML gracefully")
        void testFromXml_InvalidXml_ShouldHandleGracefully() {
            // Given
            String invalidXml = "<invalid><unclosed>";

            // When/Then
            assertThatThrownBy(() -> objectXml.fromXml(invalidXml))
                    .as("Should throw exception for invalid XML")
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("fromXml() should handle empty string")
        void testFromXml_EmptyString_ShouldHandleGracefully() {
            // Given
            String emptyXml = "";

            // When/Then
            assertThatThrownBy(() -> objectXml.fromXml(emptyXml))
                    .as("Should throw exception for empty XML")
                    .isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("toXml() should handle null object")
        void testToXml_NullObject_ShouldHandleGracefully() {
            // When
            String xml = objectXml.toXml(null);

            // Then
            assertThat(xml)
                    .as("Should handle null object")
                    .isNotNull()
                    .contains("null");
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

    private static class NestedObject {
    }

    private static class NestedObjectXml extends ObjectXml<NestedObject> {
        @Override
        public Class<NestedObject> getTargetClass() {
            return NestedObject.class;
        }
    }

    private static class CustomType {
        public String value;

        public CustomType(String value) {
            this.value = value;
        }
    }

    private static class ComplexTestObject {
        public String name;
        public CustomType customField;

        public ComplexTestObject(String name, CustomType customField) {
            this.name = name;
            this.customField = customField;
        }
    }

    private static class ComplexObjectXml extends ObjectXml<ComplexTestObject> {
        @Override
        public Class<ComplexTestObject> getTargetClass() {
            return ComplexTestObject.class;
        }
    }
}
