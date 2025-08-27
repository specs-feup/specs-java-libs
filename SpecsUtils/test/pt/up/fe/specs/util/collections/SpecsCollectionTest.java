package pt.up.fe.specs.util.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link SpecsCollection} interface.
 * Tests enhanced collection operations with mapping functionality.
 * 
 * @author Generated Tests
 */
class SpecsCollectionTest {

    private TestSpecsCollection<String> specsCollection;
    private TestSpecsCollection<Integer> intCollection;

    @BeforeEach
    void setUp() {
        specsCollection = new TestSpecsCollection<>();
        intCollection = new TestSpecsCollection<>();
    }

    @Nested
    @DisplayName("Basic Collection Operations")
    class BasicOperationsTests {

        @Test
        @DisplayName("Should behave as regular collection")
        void testBasicCollectionBehavior() {
            assertThat(specsCollection).isEmpty();
            assertThat(specsCollection.size()).isZero();

            specsCollection.add("item1");
            specsCollection.add("item2");

            assertThat(specsCollection).hasSize(2);
            assertThat(specsCollection).contains("item1");
            assertThat(specsCollection).contains("item2");
        }

        @Test
        @DisplayName("Should support all collection operations")
        void testAllCollectionOperations() {
            specsCollection.addAll(Arrays.asList("a", "b", "c"));

            assertThat(specsCollection).containsExactlyInAnyOrder("a", "b", "c");

            specsCollection.remove("b");
            assertThat(specsCollection).containsExactlyInAnyOrder("a", "c");

            specsCollection.clear();
            assertThat(specsCollection).isEmpty();
        }
    }

    @Nested
    @DisplayName("ToSet Mapping Operations")
    class ToSetMappingTests {

        @Test
        @DisplayName("Should map strings to their length")
        void testMapToLength() {
            specsCollection.addAll(Arrays.asList("a", "bb", "ccc", "dddd"));

            Set<Integer> lengths = specsCollection.toSet(String::length);

            assertThat(lengths).containsExactlyInAnyOrder(1, 2, 3, 4);
        }

        @Test
        @DisplayName("Should map strings to uppercase")
        void testMapToUppercase() {
            specsCollection.addAll(Arrays.asList("hello", "world", "test"));

            Set<String> uppercase = specsCollection.toSet(String::toUpperCase);

            assertThat(uppercase).containsExactlyInAnyOrder("HELLO", "WORLD", "TEST");
        }

        @Test
        @DisplayName("Should handle duplicate mappings")
        void testDuplicateMappings() {
            specsCollection.addAll(Arrays.asList("a", "b", "aa", "bb"));

            Set<Integer> lengths = specsCollection.toSet(String::length);

            // Should contain only unique lengths (1, 2) despite having 4 elements
            assertThat(lengths).containsExactlyInAnyOrder(1, 2);
        }

        @Test
        @DisplayName("Should handle empty collection")
        void testEmptyCollection() {
            Set<Integer> result = specsCollection.toSet(String::length);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle null elements gracefully")
        void testNullElements() {
            specsCollection.add("test");
            specsCollection.add(null);

            Set<String> result = specsCollection.toSet(s -> s == null ? "NULL" : s.toUpperCase());

            assertThat(result).containsExactlyInAnyOrder("TEST", "NULL");
        }

        @Test
        @DisplayName("Should handle complex mapping functions")
        void testComplexMapping() {
            specsCollection.addAll(Arrays.asList("apple", "banana", "cherry"));

            Set<String> firstChars = specsCollection.toSet(s -> s.substring(0, 1));

            assertThat(firstChars).containsExactlyInAnyOrder("a", "b", "c");
        }
    }

    @Nested
    @DisplayName("Numeric Collection Mapping")
    class NumericMappingTests {

        @Test
        @DisplayName("Should map numbers to their string representation")
        void testNumberToString() {
            intCollection.addAll(Arrays.asList(1, 2, 3, 4, 5));

            Set<String> strings = intCollection.toSet(Object::toString);

            assertThat(strings).containsExactlyInAnyOrder("1", "2", "3", "4", "5");
        }

        @Test
        @DisplayName("Should map numbers to mathematical operations")
        void testMathematicalOperations() {
            intCollection.addAll(Arrays.asList(1, 2, 3, 4, 5));

            Set<Integer> squares = intCollection.toSet(n -> n * n);

            assertThat(squares).containsExactlyInAnyOrder(1, 4, 9, 16, 25);
        }

        @Test
        @DisplayName("Should map numbers to categories")
        void testCategoricalMapping() {
            intCollection.addAll(Arrays.asList(1, 2, 3, 4, 5, 6));

            Set<String> evenOdd = intCollection.toSet(n -> n % 2 == 0 ? "even" : "odd");

            assertThat(evenOdd).containsExactlyInAnyOrder("even", "odd");
        }

        @Test
        @DisplayName("Should handle negative numbers")
        void testNegativeNumbers() {
            intCollection.addAll(Arrays.asList(-2, -1, 0, 1, 2));

            Set<Integer> absolute = intCollection.toSet(Math::abs);

            assertThat(absolute).containsExactlyInAnyOrder(0, 1, 2);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle single element collection")
        void testSingleElement() {
            specsCollection.add("single");

            Set<Integer> result = specsCollection.toSet(String::length);

            assertThat(result).containsExactly(6);
        }

        @Test
        @DisplayName("Should handle mapper that returns null")
        void testMapperReturnsNull() {
            specsCollection.addAll(Arrays.asList("test", "example"));

            Set<String> result = specsCollection.toSet(s -> s.length() > 5 ? null : s);

            assertThat(result).containsExactlyInAnyOrder("test", null);
        }

        @Test
        @DisplayName("Should handle large collections efficiently")
        void testLargeCollection() {
            List<String> largeList = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                largeList.add("item" + i);
            }
            specsCollection.addAll(largeList);

            Set<Integer> lengths = specsCollection.toSet(String::length);

            // All items have format "item" + number
            // "item0" to "item9" = length 5
            // "item10" to "item99" = length 6
            // "item100" to "item999" = length 7
            assertThat(lengths).contains(5, 6, 7);
            assertThat(lengths).hasSizeLessThanOrEqualTo(4);
        }

        @Test
        @DisplayName("Should handle identity mapping")
        void testIdentityMapping() {
            specsCollection.addAll(Arrays.asList("a", "b", "c"));

            Set<String> result = specsCollection.toSet(Function.identity());

            assertThat(result).containsExactlyInAnyOrder("a", "b", "c");
        }

        @Test
        @DisplayName("Should handle constant mapping")
        void testConstantMapping() {
            specsCollection.addAll(Arrays.asList("different", "strings", "here"));

            Set<String> result = specsCollection.toSet(s -> "constant");

            assertThat(result).containsExactly("constant");
        }
    }

    @Nested
    @DisplayName("Type Safety and Generics")
    class TypeSafetyTests {

        @Test
        @DisplayName("Should maintain type safety with different target types")
        void testTypeSafety() {
            specsCollection.addAll(Arrays.asList("1", "2", "3"));

            Set<Integer> integers = specsCollection.toSet(Integer::parseInt);

            assertThat(integers).containsExactlyInAnyOrder(1, 2, 3);
            assertThat(integers).allMatch(item -> item instanceof Integer);
        }

        @Test
        @DisplayName("Should work with custom object mappings")
        void testCustomObjectMapping() {
            specsCollection.addAll(Arrays.asList("john", "jane", "bob"));

            Set<TestPerson> people = specsCollection.toSet(TestPerson::new);

            assertThat(people).hasSize(3);
            assertThat(people).extracting(TestPerson::getName)
                    .containsExactlyInAnyOrder("john", "jane", "bob");
        }

        @Test
        @DisplayName("Should handle nested generic types")
        void testNestedGenerics() {
            specsCollection.addAll(Arrays.asList("a,b", "c,d", "e,f"));

            Set<List<String>> lists = specsCollection.toSet(s -> Arrays.asList(s.split(",")));

            assertThat(lists).hasSize(3);
            assertThat(lists).allMatch(list -> list.size() == 2);
        }
    }

    /**
     * Test implementation of SpecsCollection for testing purposes.
     */
    private static class TestSpecsCollection<T> implements SpecsCollection<T> {
        private final List<T> items = new ArrayList<>();

        @Override
        public int size() {
            return items.size();
        }

        @Override
        public boolean isEmpty() {
            return items.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return items.contains(o);
        }

        @Override
        public Iterator<T> iterator() {
            return items.iterator();
        }

        @Override
        public Object[] toArray() {
            return items.toArray();
        }

        @Override
        public <U> U[] toArray(U[] a) {
            return items.toArray(a);
        }

        @Override
        public boolean add(T t) {
            return items.add(t);
        }

        @Override
        public boolean remove(Object o) {
            return items.remove(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return items.containsAll(c);
        }

        @Override
        public boolean addAll(Collection<? extends T> c) {
            return items.addAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return items.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return items.retainAll(c);
        }

        @Override
        public void clear() {
            items.clear();
        }
    }

    /**
     * Simple test class for custom object mapping tests.
     */
    private static class TestPerson {
        private final String name;

        public TestPerson(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            TestPerson that = (TestPerson) o;
            return Objects.equals(name, that.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
