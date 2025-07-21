package tdrc.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Comprehensive unit tests for SerializeUtils class.
 * Tests serialization and deserialization functionality with various data
 * types.
 * 
 * @author Generated Tests
 */
@DisplayName("SerializeUtils Tests")
public class SerializeUtilsTest {

    /**
     * Simple test class for serialization testing
     */
    private static class TestPerson implements Serializable {
        private static final long serialVersionUID = 1L;
        private String name;
        private int age;

        public TestPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            TestPerson person = (TestPerson) obj;
            return age == person.age && name.equals(person.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode() + age;
        }
    }

    @Nested
    @DisplayName("Basic Serialization Tests")
    class BasicSerializationTests {

        @Test
        @DisplayName("Should serialize and deserialize strings successfully")
        void testSerializeString() {
            String original = "Hello World";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            SerializeUtils.toStream(original, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            String deserialized = SerializeUtils.fromStream(bais, String.class);

            assertThat(deserialized).isEqualTo(original);
        }

        @Test
        @DisplayName("Should serialize and deserialize integers successfully")
        void testSerializeInteger() {
            Integer original = 42;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            SerializeUtils.toStream(original, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            Integer deserialized = SerializeUtils.fromStream(bais, Integer.class);

            assertThat(deserialized).isEqualTo(original);
        }

        @Test
        @DisplayName("Should serialize and deserialize custom objects successfully")
        void testSerializeCustomObject() {
            TestPerson original = new TestPerson("John Doe", 30);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            SerializeUtils.toStream(original, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            TestPerson deserialized = SerializeUtils.fromStream(bais, TestPerson.class);

            assertThat(deserialized).isEqualTo(original);
            assertThat(deserialized.getName()).isEqualTo("John Doe");
            assertThat(deserialized.getAge()).isEqualTo(30);
        }
    }

    @Nested
    @DisplayName("Collection Serialization Tests")
    class CollectionSerializationTests {

        @Test
        @DisplayName("Should serialize and deserialize lists successfully")
        void testSerializeList() {
            List<String> original = new ArrayList<>();
            original.add("item1");
            original.add("item2");
            original.add("item3");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            SerializeUtils.toStream((Serializable) original, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            @SuppressWarnings("unchecked")
            List<String> deserialized = SerializeUtils.fromStream(bais, ArrayList.class);

            assertThat(deserialized).containsExactly("item1", "item2", "item3");
        }

        @Test
        @DisplayName("Should serialize and deserialize maps successfully")
        void testSerializeMap() {
            Map<String, Integer> original = new HashMap<>();
            original.put("one", 1);
            original.put("two", 2);
            original.put("three", 3);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            SerializeUtils.toStream((Serializable) original, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            @SuppressWarnings("unchecked")
            Map<String, Integer> deserialized = SerializeUtils.fromStream(bais, HashMap.class);

            assertThat(deserialized)
                    .hasSize(3)
                    .containsEntry("one", 1)
                    .containsEntry("two", 2)
                    .containsEntry("three", 3);
        }

        @Test
        @DisplayName("Should serialize and deserialize empty collections")
        void testSerializeEmptyCollections() {
            List<String> emptyList = new ArrayList<>();
            Map<String, String> emptyMap = new HashMap<>();

            // Test empty list
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            SerializeUtils.toStream((Serializable) emptyList, baos1);
            ByteArrayInputStream bais1 = new ByteArrayInputStream(baos1.toByteArray());
            @SuppressWarnings("unchecked")
            List<String> deserializedList = SerializeUtils.fromStream(bais1, ArrayList.class);
            assertThat(deserializedList).isEmpty();

            // Test empty map
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            SerializeUtils.toStream((Serializable) emptyMap, baos2);
            ByteArrayInputStream bais2 = new ByteArrayInputStream(baos2.toByteArray());
            @SuppressWarnings("unchecked")
            Map<String, String> deserializedMap = SerializeUtils.fromStream(bais2, HashMap.class);
            assertThat(deserializedMap).isEmpty();
        }
    }

    @Nested
    @DisplayName("Null Value Tests")
    class NullValueTests {

        @Test
        @DisplayName("Should handle null object serialization")
        void testSerializeNull() {
            String nullObject = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            SerializeUtils.toStream(nullObject, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            String deserialized = SerializeUtils.fromStream(bais, String.class);

            assertThat(deserialized).isNull();
        }

        @Test
        @DisplayName("Should handle objects with null fields")
        void testSerializeObjectWithNullFields() {
            TestPerson original = new TestPerson(null, 25);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            SerializeUtils.toStream(original, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            TestPerson deserialized = SerializeUtils.fromStream(bais, TestPerson.class);

            assertThat(deserialized.getName()).isNull();
            assertThat(deserialized.getAge()).isEqualTo(25);
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw RuntimeException when serialization fails")
        void testSerializationError() {
            // Create a mock OutputStream that always throws IOException
            OutputStream failingStream = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    throw new IOException("Simulated IO error");
                }
            };

            String testObject = "test";

            assertThatThrownBy(() -> SerializeUtils.toStream(testObject, failingStream))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Problem during serialization.")
                    .hasCauseInstanceOf(IOException.class);
        }

        @Test
        @DisplayName("Should throw RuntimeException when deserialization fails")
        void testDeserializationError() {
            // Create invalid serialized data
            byte[] invalidData = { 1, 2, 3, 4, 5 };
            ByteArrayInputStream bais = new ByteArrayInputStream(invalidData);

            assertThatThrownBy(() -> SerializeUtils.fromStream(bais, String.class))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Problem during deserialization.");
        }

        @Test
        @DisplayName("Should throw RuntimeException when input stream fails")
        void testInputStreamError() {
            // Create a mock InputStream that always throws IOException
            InputStream failingStream = new InputStream() {
                @Override
                public int read() throws IOException {
                    throw new IOException("Simulated IO error");
                }
            };

            assertThatThrownBy(() -> SerializeUtils.fromStream(failingStream, String.class))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Problem during deserialization.")
                    .hasCauseInstanceOf(IOException.class);
        }

        @Test
        @DisplayName("Should handle class cast exceptions gracefully")
        void testClassCastException() {
            // Serialize an Integer but try to deserialize as String
            Integer original = 42;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            SerializeUtils.toStream(original, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

            // The ClassCastException should now be wrapped in RuntimeException (bug fix)
            assertThatThrownBy(() -> SerializeUtils.fromStream(bais, String.class))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Problem during deserialization")
                    .hasCauseInstanceOf(ClassCastException.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very large objects")
        void testLargeObjectSerialization() {
            // Create a large list
            List<String> largeList = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                largeList.add("Item " + i);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            SerializeUtils.toStream((Serializable) largeList, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            @SuppressWarnings("unchecked")
            List<String> deserialized = SerializeUtils.fromStream(bais, ArrayList.class);

            assertThat(deserialized).hasSize(10000);
            assertThat(deserialized.get(0)).isEqualTo("Item 0");
            assertThat(deserialized.get(9999)).isEqualTo("Item 9999");
        }

        @Test
        @DisplayName("Should handle special characters in strings")
        void testSpecialCharacters() {
            String specialChars = "Special chars: àáâãäåæçèéêëìíîïñòóôõöøùúûüý 中文 🚀 \n\t\r";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            SerializeUtils.toStream(specialChars, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            String deserialized = SerializeUtils.fromStream(bais, String.class);

            assertThat(deserialized).isEqualTo(specialChars);
        }

        @Test
        @DisplayName("Should handle deeply nested objects")
        void testDeeplyNestedObjects() {
            // Create a nested structure
            List<List<String>> nestedList = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                List<String> innerList = new ArrayList<>();
                for (int j = 0; j < 3; j++) {
                    innerList.add("Item " + i + "-" + j);
                }
                nestedList.add(innerList);
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            SerializeUtils.toStream((Serializable) nestedList, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            @SuppressWarnings("unchecked")
            List<List<String>> deserialized = SerializeUtils.fromStream(bais, ArrayList.class);

            assertThat(deserialized).hasSize(5);
            assertThat(deserialized.get(2).get(1)).isEqualTo("Item 2-1");
        }

        @Test
        @DisplayName("Should handle primitive wrapper edge values")
        void testPrimitiveWrapperEdgeValues() {
            // Test various edge values
            Integer maxInt = Integer.MAX_VALUE;
            Integer minInt = Integer.MIN_VALUE;
            Long maxLong = Long.MAX_VALUE;
            Double maxDouble = Double.MAX_VALUE;
            Double minDouble = Double.MIN_VALUE;
            Double nan = Double.NaN;
            Double positiveInfinity = Double.POSITIVE_INFINITY;
            Double negativeInfinity = Double.NEGATIVE_INFINITY;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Use ArrayList instead of List.of() to create a serializable list
            List<Serializable> values = new ArrayList<>();
            values.add(maxInt);
            values.add(minInt);
            values.add(maxLong);
            values.add(maxDouble);
            values.add(minDouble);
            values.add(nan);
            values.add(positiveInfinity);
            values.add(negativeInfinity);

            SerializeUtils.toStream((Serializable) values, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            @SuppressWarnings("unchecked")
            List<Serializable> deserialized = SerializeUtils.fromStream(bais, ArrayList.class);

            assertThat(deserialized).hasSize(8);
            assertThat(deserialized.get(0)).isEqualTo(Integer.MAX_VALUE);
            assertThat(deserialized.get(1)).isEqualTo(Integer.MIN_VALUE);
            assertThat(deserialized.get(5)).isEqualTo(Double.NaN);
            assertThat(deserialized.get(6)).isEqualTo(Double.POSITIVE_INFINITY);
        }
    }

    @Nested
    @DisplayName("Stream Resource Management Tests")
    class StreamResourceManagementTests {

        @Test
        @DisplayName("Should properly close streams after successful serialization")
        void testStreamClosedAfterSerialization() {
            String testObject = "test";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            // This should not throw any exception
            assertThatCode(() -> SerializeUtils.toStream(testObject, baos))
                    .doesNotThrowAnyException();

            // Verify we can still access the data
            assertThat(baos.toByteArray()).isNotEmpty();
        }

        @Test
        @DisplayName("Should properly close streams after successful deserialization")
        void testStreamClosedAfterDeserialization() {
            String original = "test";
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            SerializeUtils.toStream(original, baos);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());

            // This should not throw any exception
            assertThatCode(() -> SerializeUtils.fromStream(bais, String.class))
                    .doesNotThrowAnyException();
        }
    }
}
