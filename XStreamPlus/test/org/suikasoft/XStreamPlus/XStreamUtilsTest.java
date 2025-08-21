package org.suikasoft.XStreamPlus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.thoughtworks.xstream.XStream;

/**
 * Comprehensive unit tests for {@link XStreamUtils}.
 * 
 * Tests cover all public methods including XStream instance creation, object
 * serialization/deserialization, file I/O operations, string conversions, and
 * object copying functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("XStreamUtils Tests")
class XStreamUtilsTest {

    @Nested
    @DisplayName("XStream Instance Creation")
    class XStreamInstanceCreationTests {

        @Test
        @DisplayName("newXStream() should create configured XStream instance")
        void testNewXStream_ShouldCreateConfiguredInstance() {
            // When
            XStream xstream = XStreamUtils.newXStream();

            // Then
            assertThat(xstream)
                    .as("XStream instance should not be null")
                    .isNotNull();

            // Verify it's properly configured (permissions and converters are set
            // internally)
            assertThat(xstream.getClassLoader())
                    .as("XStream should have a class loader")
                    .isNotNull();
        }

        @Test
        @DisplayName("newXStream() should create instances with proper permissions")
        void testNewXStream_ShouldHaveProperPermissions() {
            // When
            XStream xstream = XStreamUtils.newXStream();

            // Then - Test by trying to serialize a simple object
            TestObject testObj = new TestObject("test", 42);
            String xml = xstream.toXML(testObj);

            assertThat(xml)
                    .as("Should be able to serialize objects with AnyTypePermission")
                    .contains("test")
                    .contains("42");
        }
    }

    @Nested
    @DisplayName("String Conversion Operations")
    class StringConversionTests {

        @Test
        @DisplayName("toString() should convert object to XML string")
        void testToString_ValidObject_ShouldReturnXmlString() {
            // Given
            TestObject testObj = new TestObject("testName", 123);

            // When
            String xml = XStreamUtils.toString(testObj);

            // Then
            assertAll(
                    () -> assertThat(xml).isNotNull(),
                    () -> assertThat(xml).contains("testName"),
                    () -> assertThat(xml).contains("123"),
                    () -> assertThat(xml).contains("<"));
        }

        @Test
        @DisplayName("toString() should handle null object")
        void testToString_NullObject_ShouldHandleGracefully() {
            // When
            String xml = XStreamUtils.toString(null);

            // Then
            assertThat(xml)
                    .as("Should handle null object")
                    .contains("null");
        }

        @Test
        @DisplayName("from() should deserialize XML string to object")
        void testFrom_ValidXmlString_ShouldReturnObject() {
            // Given
            TestObject original = new TestObject("originalName", 456);
            String xml = XStreamUtils.toString(original);

            // When
            TestObject result = XStreamUtils.from(xml, TestObject.class);

            // Then
            assertThat(result)
                    .as("Should deserialize to equivalent object")
                    .isNotNull()
                    .satisfies(obj -> {
                        assertThat(obj.name).isEqualTo("originalName");
                        assertThat(obj.value).isEqualTo(456);
                    });
        }

        @Test
        @DisplayName("from() should handle empty XML string")
        void testFrom_EmptyXmlString_ShouldHandleGracefully() {
            // Given
            String emptyXml = "";

            // When/Then
            try {
                TestObject result = XStreamUtils.from(emptyXml, TestObject.class);
                assertThat(result).isNull(); // Or should throw exception - depends on implementation
            } catch (Exception e) {
                // Expected behavior for invalid XML
                assertThat(e).as("Should handle empty XML gracefully").isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("File I/O Operations with ObjectXml")
    class FileIOWithObjectXmlTests {

        @TempDir
        Path tempDir;

        @Test
        @DisplayName("write() with ObjectXml should write object to file successfully")
        void testWriteWithObjectXml_ValidInputs_ShouldWriteToFile() throws IOException {
            // Given
            File testFile = tempDir.resolve("test.xml").toFile();
            TestObject testObj = new TestObject("fileTest", 789);
            TestObjectXml objectXml = new TestObjectXml();

            // When
            boolean result = XStreamUtils.write(testFile, testObj, objectXml);

            // Then
            assertAll(
                    () -> assertThat(result).as("Write operation should succeed").isTrue(),
                    () -> assertThat(testFile).exists(),
                    () -> assertThat(Files.readString(testFile.toPath()))
                            .contains("fileTest")
                            .contains("789"));
        }

        @Test
        @DisplayName("write() with ObjectXml should handle null XML generation")
        void testWriteWithObjectXml_NullXmlGeneration_ShouldReturnFalse() {
            // Given
            File testFile = tempDir.resolve("test.xml").toFile();
            TestObject testObj = new TestObject("test", 123);
            ObjectXml<TestObject> failingObjectXml = new ObjectXml<TestObject>() {
                @Override
                public Class<TestObject> getTargetClass() {
                    return TestObject.class;
                }

                @Override
                public String toXml(Object object) {
                    return null; // Simulate failure
                }
            };

            // When
            boolean result = XStreamUtils.write(testFile, testObj, failingObjectXml);

            // Then
            assertThat(result)
                    .as("Should return false when XML generation fails")
                    .isFalse();
        }

        @Test
        @DisplayName("read() with ObjectXml should read object from file successfully")
        void testReadWithObjectXml_ValidFile_ShouldReadObject() throws IOException {
            // Given
            File testFile = tempDir.resolve("test.xml").toFile();
            TestObject original = new TestObject("readTest", 999);
            TestObjectXml objectXml = new TestObjectXml();

            // Write first
            XStreamUtils.write(testFile, original, objectXml);

            // When
            TestObject result = XStreamUtils.read(testFile, objectXml);

            // Then
            assertThat(result)
                    .as("Should read equivalent object")
                    .isNotNull()
                    .satisfies(obj -> {
                        assertThat(obj.name).isEqualTo("readTest");
                        assertThat(obj.value).isEqualTo(999);
                    });
        }

        @Test
        @DisplayName("read() with ObjectXml should handle non-existent file")
        void testReadWithObjectXml_NonExistentFile_ShouldHandleGracefully() {
            // Given
            File nonExistentFile = tempDir.resolve("nonexistent.xml").toFile();
            TestObjectXml objectXml = new TestObjectXml();

            // When/Then
            try {
                TestObject result = XStreamUtils.read(nonExistentFile, objectXml);
                assertThat(result).isNull(); // Expected behavior
            } catch (Exception e) {
                // Also acceptable behavior
                assertThat(e).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("File I/O Operations with Class")
    class FileIOWithClassTests {

        @TempDir
        Path tempDir;

        @Test
        @DisplayName("write() with Class should write object to file successfully")
        void testWriteWithClass_ValidInputs_ShouldWriteToFile() throws IOException {
            // Given
            File testFile = tempDir.resolve("classTest.xml").toFile();
            TestObject testObj = new TestObject("classFileTest", 321);

            // When
            boolean result = XStreamUtils.write(testFile, testObj, TestObject.class);

            // Then
            assertAll(
                    () -> assertThat(result).as("Write operation should succeed").isTrue(),
                    () -> assertThat(testFile).exists(),
                    () -> assertThat(Files.readString(testFile.toPath()))
                            .contains("classFileTest")
                            .contains("321"));
        }

        @Test
        @DisplayName("read() with Class should read object from file successfully")
        void testReadWithClass_ValidFile_ShouldReadObject() throws IOException {
            // Given
            File testFile = tempDir.resolve("classTest.xml").toFile();
            TestObject original = new TestObject("classReadTest", 654);

            // Write first
            XStreamUtils.write(testFile, original, TestObject.class);

            // When
            TestObject result = XStreamUtils.read(testFile, TestObject.class);

            // Then
            assertThat(result)
                    .as("Should read equivalent object")
                    .isNotNull()
                    .satisfies(obj -> {
                        assertThat(obj.name).isEqualTo("classReadTest");
                        assertThat(obj.value).isEqualTo(654);
                    });
        }
    }

    @Nested
    @DisplayName("Generic File Write Operations")
    class GenericFileWriteTests {

        @TempDir
        Path tempDir;

        @Test
        @DisplayName("write() generic method should write object to file")
        void testWriteGeneric_ValidInputs_ShouldWriteToFile() throws IOException {
            // Given
            File testFile = tempDir.resolve("generic.xml").toFile();
            TestObject testObj = new TestObject("genericTest", 111);

            // When
            XStreamUtils.write(testFile, testObj);

            // Then
            assertAll(
                    () -> assertThat(testFile).exists(),
                    () -> assertThat(Files.readString(testFile.toPath()))
                            .contains("genericTest")
                            .contains("111"));
        }
    }

    @Nested
    @DisplayName("Object Copy Operations")
    class ObjectCopyTests {

        @Test
        @DisplayName("copy() should create deep copy of object")
        void testCopy_ValidObject_ShouldCreateDeepCopy() {
            // Given
            TestObject original = new TestObject("copyTest", 777);

            // When
            TestObject copy = XStreamUtils.copy(original);

            // Then
            assertAll(
                    () -> assertThat(copy).as("Copy should not be null").isNotNull(),
                    () -> assertThat(copy).as("Copy should not be same instance").isNotSameAs(original),
                    () -> assertThat(copy.name).as("Name should be equal").isEqualTo(original.name),
                    () -> assertThat(copy.value).as("Value should be equal").isEqualTo(original.value));
        }

        @Test
        @DisplayName("copy() should handle complex objects")
        void testCopy_ComplexObject_ShouldCreateDeepCopy() {
            // Given
            ComplexTestObject original = new ComplexTestObject("complex",
                    Optional.of("optional"), new TestObject("nested", 888));

            // When
            ComplexTestObject copy = XStreamUtils.copy(original);

            // Then
            assertAll(
                    () -> assertThat(copy).as("Copy should not be null").isNotNull(),
                    () -> assertThat(copy).as("Copy should not be same instance").isNotSameAs(original),
                    () -> assertThat(copy.name).as("Name should be equal").isEqualTo(original.name),
                    () -> assertThat(copy.optional).as("Optional should be equal").isEqualTo(original.optional),
                    () -> assertThat(copy.nested).as("Nested should not be same instance").isNotSameAs(original.nested),
                    () -> assertThat(copy.nested.name).as("Nested name should be equal")
                            .isEqualTo(original.nested.name));
        }

        @Test
        @DisplayName("copy() should handle null object")
        void testCopy_NullObject_ShouldReturnNull() {
            // When
            TestObject copy = XStreamUtils.copy((TestObject) null);

            // Then
            assertThat(copy).as("Copy of null should be null").isNull();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @TempDir
        Path tempDir;

        @Test
        @DisplayName("Should handle objects with Optional fields")
        void testOptionalFields_ShouldHandleCorrectly() {
            // Given
            ComplexTestObject original = new ComplexTestObject("optionalTest",
                    Optional.of("present"), new TestObject("nested", 123));

            // When
            String xml = XStreamUtils.toString(original);
            ComplexTestObject result = XStreamUtils.from(xml, ComplexTestObject.class);

            // Then
            assertThat(result.optional)
                    .as("Optional field should be preserved")
                    .isEqualTo(Optional.of("present"));
        }

        @Test
        @DisplayName("Should handle objects with empty Optional fields")
        void testEmptyOptionalFields_ShouldHandleCorrectly() {
            // Given
            ComplexTestObject original = new ComplexTestObject("emptyOptionalTest",
                    Optional.empty(), new TestObject("nested", 456));

            // When
            String xml = XStreamUtils.toString(original);
            ComplexTestObject result = XStreamUtils.from(xml, ComplexTestObject.class);

            // Then
            assertThat(result.optional)
                    .as("Empty optional field should be preserved")
                    .isEqualTo(Optional.empty());
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

    private static class ComplexTestObject {
        public String name;
        public Optional<String> optional;
        public TestObject nested;

        public ComplexTestObject(String name, Optional<String> optional, TestObject nested) {
            this.name = name;
            this.optional = optional;
            this.nested = nested;
        }
    }

    private static class TestObjectXml extends ObjectXml<TestObject> {
        @Override
        public Class<TestObject> getTargetClass() {
            return TestObject.class;
        }
    }
}
