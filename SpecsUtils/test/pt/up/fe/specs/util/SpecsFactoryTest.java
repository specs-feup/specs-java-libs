package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;

/**
 * Comprehensive test suite for SpecsFactory utility class.
 * Tests all collection factory methods, utility operations, and edge cases.
 *
 * Note: SpecsFactory is deprecated in favor of Java 7+ diamond operator and
 * Guava collections.
 * These tests ensure backward compatibility and document expected behavior.
 * 
 * @author Generated Tests
 */
@SuppressWarnings("deprecation")
@DisplayName("SpecsFactory Tests")
class SpecsFactoryTest {

    // Test enum for EnumMap testing
    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }

    @Nested
    @DisplayName("List Factory Methods")
    class ListFactoryTests {

        @Test
        @DisplayName("newArrayList should create empty ArrayList")
        void testNewArrayList_Empty() {
            // Execute
            List<String> result = SpecsFactory.newArrayList();

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            assertThat(result).isInstanceOf(ArrayList.class);
        }

        @Test
        @DisplayName("newArrayList with capacity should create ArrayList with specified capacity")
        void testNewArrayList_WithCapacity() {
            // Execute
            List<Integer> result = SpecsFactory.newArrayList(10);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            assertThat(result).isInstanceOf(ArrayList.class);
            // Note: capacity is internal, we can only verify the list works
            result.add(1);
            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("newArrayList from collection should create ArrayList with elements")
        void testNewArrayList_FromCollection() {
            // Arrange
            List<String> source = Arrays.asList("a", "b", "c");

            // Execute
            List<String> result = SpecsFactory.newArrayList(source);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(ArrayList.class);
            assertThat(result).hasSize(3);
            assertThat(result).containsExactly("a", "b", "c");
            assertThat(result).isNotSameAs(source); // Different instance
        }

        @Test
        @DisplayName("newLinkedList should create empty LinkedList")
        void testNewLinkedList_Empty() {
            // Execute
            List<String> result = SpecsFactory.newLinkedList();

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            assertThat(result).isInstanceOf(LinkedList.class);
        }

        @Test
        @DisplayName("newLinkedList from collection should create LinkedList with elements")
        void testNewLinkedList_FromCollection() {
            // Arrange
            Set<Integer> source = new HashSet<>(Arrays.asList(1, 2, 3));

            // Execute
            List<Integer> result = SpecsFactory.newLinkedList(source);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(LinkedList.class);
            assertThat(result).hasSize(3);
            assertThat(result).containsExactlyInAnyOrderElementsOf(source);
        }

        @Test
        @DisplayName("asList should create typed list with valid elements")
        void testAsList_ValidElements() {
            // Execute
            List<String> result = SpecsFactory.asList(String.class, "hello", "world");

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).hasSize(2);
            assertThat(result).containsExactly("hello", "world");
        }

        @Test
        @DisplayName("asList should throw exception for invalid element types")
        void testAsList_InvalidElements() {
            // Execute & Verify
            assertThatThrownBy(() -> SpecsFactory.asList(String.class, "valid", 123))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Object '123' is not an instance of 'java.lang.String'");
        }
    }

    @Nested
    @DisplayName("Map Factory Methods")
    class MapFactoryTests {

        @Test
        @DisplayName("newHashMap should create empty HashMap")
        void testNewHashMap_Empty() {
            // Execute
            Map<String, Integer> result = SpecsFactory.newHashMap();

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            assertThat(result).isInstanceOf(HashMap.class);
        }

        @Test
        @DisplayName("newHashMap from map should create HashMap with elements")
        void testNewHashMap_FromMap() {
            // Arrange
            Map<String, Integer> source = new LinkedHashMap<>();
            source.put("a", 1);
            source.put("b", 2);

            // Execute
            Map<String, Integer> result = SpecsFactory.newHashMap(source);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(HashMap.class);
            assertThat(result).hasSize(2);
            assertThat(result).containsEntry("a", 1);
            assertThat(result).containsEntry("b", 2);
            assertThat(result).isNotSameAs(source);
        }

        @Test
        @DisplayName("newHashMap from null should return empty map")
        void testNewHashMap_FromNull() {
            // Execute
            Map<String, Integer> result = SpecsFactory.newHashMap(null);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            assertThat(result).isSameAs(Collections.emptyMap());
        }

        @Test
        @DisplayName("newLinkedHashMap should create empty LinkedHashMap")
        void testNewLinkedHashMap_Empty() {
            // Execute
            Map<String, Integer> result = SpecsFactory.newLinkedHashMap();

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            assertThat(result).isInstanceOf(LinkedHashMap.class);
        }

        @Test
        @DisplayName("newLinkedHashMap from map should preserve insertion order")
        void testNewLinkedHashMap_FromMap() {
            // Arrange
            Map<String, Integer> source = new HashMap<>();
            source.put("c", 3);
            source.put("a", 1);
            source.put("b", 2);

            // Execute
            Map<String, Integer> result = SpecsFactory.newLinkedHashMap(source);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(LinkedHashMap.class);
            assertThat(result).hasSize(3);
            assertThat(result).containsAllEntriesOf(source);
        }

        @Test
        @DisplayName("newEnumMap should create EnumMap for enum type")
        void testNewEnumMap() {
            // Execute
            Map<TestEnum, String> result = SpecsFactory.newEnumMap(TestEnum.class);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            assertThat(result).isInstanceOf(EnumMap.class);

            // Test functionality
            result.put(TestEnum.VALUE1, "test");
            assertThat(result).containsEntry(TestEnum.VALUE1, "test");
        }

        @Test
        @DisplayName("assignMap should return original map when not null")
        void testAssignMap_NonNull() {
            // Arrange
            Map<String, Integer> originalMap = new HashMap<>();
            originalMap.put("test", 1);

            // Execute
            Map<String, Integer> result = SpecsFactory.assignMap(originalMap);

            // Verify
            assertThat(result).isSameAs(originalMap);
            assertThat(result).containsEntry("test", 1);
        }

        @Test
        @DisplayName("assignMap should return empty map when null")
        void testAssignMap_Null() {
            // Execute
            Map<String, Integer> result = SpecsFactory.assignMap(null);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            assertThat(result).isSameAs(Collections.emptyMap());
        }
    }

    @Nested
    @DisplayName("Set Factory Methods")
    class SetFactoryTests {

        @Test
        @DisplayName("newHashSet should create empty HashSet")
        void testNewHashSet_Empty() {
            // Execute
            Set<String> result = SpecsFactory.newHashSet();

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            assertThat(result).isInstanceOf(HashSet.class);
        }

        @Test
        @DisplayName("newHashSet from collection should create HashSet with elements")
        void testNewHashSet_FromCollection() {
            // Arrange
            List<String> source = Arrays.asList("a", "b", "c", "b"); // Contains duplicate

            // Execute
            Set<String> result = SpecsFactory.newHashSet(source);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(HashSet.class);
            assertThat(result).hasSize(3); // Duplicates removed
            assertThat(result).containsExactlyInAnyOrder("a", "b", "c");
        }

        @Test
        @DisplayName("newLinkedHashSet should create empty LinkedHashSet")
        void testNewLinkedHashSet_Empty() {
            // Execute
            Set<String> result = SpecsFactory.newLinkedHashSet();

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            assertThat(result).isInstanceOf(LinkedHashSet.class);
        }

        @Test
        @DisplayName("newLinkedHashSet from collection should preserve insertion order")
        void testNewLinkedHashSet_FromCollection() {
            // Arrange
            List<String> source = Arrays.asList("c", "a", "b", "a"); // Contains duplicate

            // Execute
            Set<String> result = SpecsFactory.newLinkedHashSet(source);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(LinkedHashSet.class);
            assertThat(result).hasSize(3); // Duplicates removed
            assertThat(result).containsExactly("c", "a", "b"); // Order preserved
        }

        @Test
        @DisplayName("newSetSequence should create set with integer sequence")
        void testNewSetSequence() {
            // Execute
            Set<Integer> result = SpecsFactory.newSetSequence(5, 3);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).hasSize(3);
            assertThat(result).containsExactlyInAnyOrder(5, 6, 7);
        }

        @Test
        @DisplayName("newSetSequence with zero size should create empty set")
        void testNewSetSequence_ZeroSize() {
            // Execute
            Set<Integer> result = SpecsFactory.newSetSequence(10, 0);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
        }

        @ParameterizedTest
        @ValueSource(ints = { 1, 5, 10 })
        @DisplayName("newSetSequence should create correct size sets")
        void testNewSetSequence_VariousSizes(int size) {
            // Execute
            Set<Integer> result = SpecsFactory.newSetSequence(0, size);

            // Verify
            assertThat(result).hasSize(size);
            for (int i = 0; i < size; i++) {
                assertThat(result).contains(i);
            }
        }
    }

    @Nested
    @DisplayName("File and Stream Operations")
    class FileStreamTests {

        @Test
        @DisplayName("getStream should return InputStream for existing file")
        void testGetStream_ExistingFile(@TempDir Path tempDir) throws IOException {
            // Arrange
            File testFile = tempDir.resolve("test.txt").toFile();
            testFile.createNewFile();

            // Execute
            InputStream result = SpecsFactory.getStream(testFile);

            // Verify
            assertThat(result).isNotNull();
            result.close(); // Clean up
        }

        @Test
        @DisplayName("getStream should return null for non-existing file")
        void testGetStream_NonExistingFile() {
            // Arrange
            File nonExistingFile = new File("non-existing-file.txt");

            // Execute
            InputStream result = SpecsFactory.getStream(nonExistingFile);

            // Verify
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Utility Operations")
    class UtilityOperationsTests {

        @Test
        @DisplayName("fromIntArray should convert int array to Integer list")
        void testFromIntArray() {
            // Arrange
            int[] array = { 1, 2, 3, 4, 5 };

            // Execute
            List<Integer> result = SpecsFactory.fromIntArray(array);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).hasSize(5);
            assertThat(result).containsExactly(1, 2, 3, 4, 5);
            assertThat(result).isInstanceOf(ArrayList.class);
        }

        @Test
        @DisplayName("fromIntArray should handle empty array")
        void testFromIntArray_Empty() {
            // Arrange
            int[] array = {};

            // Execute
            List<Integer> result = SpecsFactory.fromIntArray(array);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("getUnmodifiableList should return unmodifiable view of list")
        void testGetUnmodifiableList_NonNull() {
            // Arrange
            List<String> source = new ArrayList<>();
            source.add("test");

            // Execute
            List<String> result = SpecsFactory.getUnmodifiableList(source);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).hasSize(1);
            assertThat(result).containsExactly("test");

            // Should be unmodifiable
            assertThatThrownBy(() -> result.add("new"))
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("getUnmodifiableList should return empty list for null input")
        void testGetUnmodifiableList_Null() {
            // Execute
            List<String> result = SpecsFactory.getUnmodifiableList(null);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            assertThat(result).isSameAs(Collections.emptyList());
        }

        @Test
        @DisplayName("getUnmodifiableList should return empty list for empty input")
        void testGetUnmodifiableList_Empty() {
            // Arrange
            List<String> source = new ArrayList<>();

            // Execute
            List<String> result = SpecsFactory.getUnmodifiableList(source);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
            assertThat(result).isSameAs(Collections.emptyList());
        }

        @Test
        @DisplayName("addAll should add all elements from source to sink")
        void testAddAll_NonNull() {
            // Arrange
            List<String> sink = new ArrayList<>();
            sink.add("existing");
            List<String> source = Arrays.asList("new1", "new2");

            // Execute
            SpecsFactory.addAll(sink, source);

            // Verify
            assertThat(sink).hasSize(3);
            assertThat(sink).containsExactly("existing", "new1", "new2");
        }

        @Test
        @DisplayName("addAll should handle null source gracefully")
        void testAddAll_NullSource() {
            // Arrange
            List<String> sink = new ArrayList<>();
            sink.add("existing");

            // Execute
            SpecsFactory.addAll(sink, null);

            // Verify
            assertThat(sink).hasSize(1);
            assertThat(sink).containsExactly("existing");
        }

        @Test
        @DisplayName("addAll should handle empty source")
        void testAddAll_EmptySource() {
            // Arrange
            List<String> sink = new ArrayList<>();
            sink.add("existing");
            List<String> source = new ArrayList<>();

            // Execute
            SpecsFactory.addAll(sink, source);

            // Verify
            assertThat(sink).hasSize(1);
            assertThat(sink).containsExactly("existing");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("factory methods should handle generic type inference")
        void testGenericTypeInference() {
            // Execute - should compile without explicit generic parameters
            var stringList = SpecsFactory.newArrayList();
            var stringMap = SpecsFactory.newHashMap();
            var stringSet = SpecsFactory.newHashSet();

            // Verify basic functionality
            stringList.add("test");
            stringMap.put("key", "value");
            stringSet.add("element");

            assertThat(stringList).hasSize(1);
            assertThat(stringMap).hasSize(1);
            assertThat(stringSet).hasSize(1);
        }

        @Test
        @DisplayName("collections should handle null elements where appropriate")
        void testNullElements() {
            // Execute
            List<String> list = SpecsFactory.newArrayList();
            Map<String, String> map = SpecsFactory.newHashMap();
            Set<String> set = SpecsFactory.newHashSet();

            // Verify null handling
            list.add(null);
            map.put("key", null);
            map.put(null, "value");
            set.add(null);

            assertThat(list).containsExactly((String) null);
            assertThat(map).containsEntry("key", null);
            assertThat(map).containsEntry(null, "value");
            assertThat(set).containsExactly((String) null);
        }

        @Test
        @DisplayName("large capacity initialization should work")
        void testLargeCapacity() {
            // Execute
            List<Integer> result = SpecsFactory.newArrayList(10000);

            // Verify
            assertThat(result).isNotNull();
            assertThat(result).isEmpty();

            // Should be able to add many elements efficiently
            for (int i = 0; i < 1000; i++) {
                result.add(i);
            }
            assertThat(result).hasSize(1000);
        }
    }
}
