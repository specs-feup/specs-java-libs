package pt.up.fe.specs.util.parsing;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for ListParser - a utility class that provides
 * convenient operations for parsing and manipulating lists with type-safe
 * popping, peeking, and conditional operations.
 * 
 * Tests cover:
 * - Basic list operations (pop, peek, isEmpty)
 * - Type-safe operations with class casting
 * - Functional operations with mappers and predicates
 * - Edge cases and error conditions
 * - State management and list modification
 * - Performance with large lists
 * 
 * @author Generated Tests
 */
@DisplayName("ListParser Tests")
class ListParserTest {

    // Test classes for polymorphic testing
    static class Animal {
        private final String name;

        public Animal(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" + name + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            Animal animal = (Animal) obj;
            return name.equals(animal.name);
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }
    }

    static class Dog extends Animal {
        public Dog(String name) {
            super(name);
        }
    }

    static class Cat extends Animal {
        public Cat(String name) {
            super(name);
        }
    }

    @Nested
    @DisplayName("Constructor and Basic Operations")
    class ConstructorAndBasicOperations {

        @Test
        @DisplayName("should create parser with given list")
        void testCreateParserWithList() {
            List<String> original = Arrays.asList("a", "b", "c");
            ListParser<String> parser = new ListParser<>(original);

            assertThat(parser.getList())
                    .isNotNull()
                    .isEqualTo(original);
        }

        @Test
        @DisplayName("should create parser with empty list")
        void testCreateParserWithEmptyList() {
            List<String> emptyList = Collections.emptyList();
            ListParser<String> parser = new ListParser<>(emptyList);

            assertThat(parser.getList())
                    .isNotNull()
                    .isEmpty();

            assertThat(parser.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("should create parser with mutable list")
        void testCreateParserWithMutableList() {
            List<String> mutableList = new ArrayList<>(Arrays.asList("a", "b", "c"));
            ListParser<String> parser = new ListParser<>(mutableList);

            assertThat(parser.getList())
                    .isNotNull()
                    .hasSize(3)
                    .containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("should handle null list gracefully")
        void testCreateParserWithNullList() {
            assertThatCode(() -> {
                new ListParser<String>(null);
            }).doesNotThrowAnyException();

            ListParser<String> parser = new ListParser<>(null);
            // getList() should return null in this case
            assertThat(parser.getList()).isNull();
        }
    }

    @Nested
    @DisplayName("isEmpty() Operations")
    class IsEmptyOperations {

        @Test
        @DisplayName("should return true for empty list")
        void testIsEmptyWithEmptyList() {
            ListParser<String> parser = new ListParser<>(Collections.emptyList());
            assertThat(parser.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("should return false for non-empty list")
        void testIsEmptyWithNonEmptyList() {
            ListParser<String> parser = new ListParser<>(Arrays.asList("item"));
            assertThat(parser.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("should return true after all elements are popped")
        void testIsEmptyAfterPoppingAllElements() {
            ListParser<String> parser = new ListParser<>(new ArrayList<>(Arrays.asList("a", "b")));

            assertThat(parser.isEmpty()).isFalse();

            parser.popSingle();
            assertThat(parser.isEmpty()).isFalse();

            parser.popSingle();
            assertThat(parser.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("Pop by Class Operations")
    class PopByClassOperations {

        @Test
        @DisplayName("should pop consecutive elements of same type")
        void testPopConsecutiveElementsOfSameType() {
            List<Animal> animals = new ArrayList<>(Arrays.asList(
                    new Dog("Rex"), new Dog("Buddy"), new Cat("Whiskers"), new Dog("Max")));
            ListParser<Animal> parser = new ListParser<>(animals);

            List<Dog> dogs = parser.pop(Dog.class);

            assertThat(dogs)
                    .hasSize(2)
                    .extracting(Dog::getName)
                    .containsExactly("Rex", "Buddy");

            // Remaining list should start with Cat
            assertThat(parser.getList())
                    .hasSize(2)
                    .first()
                    .isInstanceOf(Cat.class);
        }

        @Test
        @DisplayName("should return empty list when no elements match type")
        void testPopNoElementsOfType() {
            List<Animal> animals = new ArrayList<>(Arrays.asList(
                    new Cat("Whiskers"), new Cat("Fluffy")));
            ListParser<Animal> parser = new ListParser<>(animals);

            List<Dog> dogs = parser.pop(Dog.class);

            assertThat(dogs).isEmpty();

            // Original list should remain unchanged
            assertThat(parser.getList()).hasSize(2);
        }

        @Test
        @DisplayName("should pop all elements when all are of same type")
        void testPopAllElementsOfSameType() {
            List<Animal> animals = new ArrayList<>(Arrays.asList(
                    new Dog("Rex"), new Dog("Buddy"), new Dog("Max")));
            ListParser<Animal> parser = new ListParser<>(animals);

            List<Dog> dogs = parser.pop(Dog.class);

            assertThat(dogs)
                    .hasSize(3)
                    .extracting(Dog::getName)
                    .containsExactly("Rex", "Buddy", "Max");

            assertThat(parser.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("should handle empty list when popping by class")
        void testPopByClassFromEmptyList() {
            ListParser<Animal> parser = new ListParser<>(Collections.emptyList());

            List<Dog> dogs = parser.pop(Dog.class);

            assertThat(dogs).isEmpty();
            assertThat(parser.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("should pop with inheritance hierarchy")
        void testPopWithInheritanceHierarchy() {
            List<Object> objects = new ArrayList<>(Arrays.asList(
                    "string1", "string2", 123, "string3"));
            ListParser<Object> parser = new ListParser<>(objects);

            List<String> strings = parser.pop(String.class);

            assertThat(strings)
                    .hasSize(2)
                    .containsExactly("string1", "string2");

            // Should stop at integer and leave it in the list
            assertThat(parser.getList())
                    .hasSize(2)
                    .containsExactly(123, "string3");
        }
    }

    @Nested
    @DisplayName("Pop with Amount and Mapper Operations")
    class PopWithAmountAndMapperOperations {

        @Test
        @DisplayName("should pop specified amount with mapper")
        void testPopWithAmountAndMapper() {
            List<String> items = new ArrayList<>(Arrays.asList("hello", "world", "test"));
            ListParser<String> parser = new ListParser<>(items);

            List<String> uppercase = parser.pop(2, String::toUpperCase);

            assertThat(uppercase)
                    .hasSize(2)
                    .containsExactly("HELLO", "WORLD");

            assertThat(parser.getList())
                    .hasSize(1)
                    .containsExactly("test");
        }

        @Test
        @DisplayName("should throw when trying to pop more than available")
        void testPopMoreThanAvailable() {
            List<String> items = new ArrayList<>(Arrays.asList("a", "b"));
            ListParser<String> parser = new ListParser<>(items);

            assertThatThrownBy(() -> {
                parser.pop(3, String::toUpperCase);
            }).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Tried to pop an amount of 3")
                    .hasMessageContaining("but list only has 2");
        }

        @Test
        @DisplayName("should pop all elements when amount equals size")
        void testPopExactAmount() {
            List<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
            ListParser<String> parser = new ListParser<>(items);

            List<String> uppercase = parser.pop(3, String::toUpperCase);

            assertThat(uppercase)
                    .hasSize(3)
                    .containsExactly("A", "B", "C");

            assertThat(parser.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("should handle pop with zero amount")
        void testPopZeroAmount() {
            List<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
            ListParser<String> parser = new ListParser<>(items);

            List<String> result = parser.pop(0, String::toUpperCase);

            assertThat(result).isEmpty();
            assertThat(parser.getList()).hasSize(3); // Unchanged
        }

        @Test
        @DisplayName("should work with complex mapper functions")
        void testPopWithComplexMapper() {
            List<Object> objects = new ArrayList<>(Arrays.asList(
                    "hello", "world"));
            ListParser<Object> parser = new ListParser<>(objects);

            Function<Object, String> mapper = Object::toString;

            List<String> descriptions = parser.pop(2, mapper);

            assertThat(descriptions)
                    .hasSize(2)
                    .containsExactly("hello", "world");
        }
    }

    @Nested
    @DisplayName("Single Element Operations")
    class SingleElementOperations {

        @Test
        @DisplayName("should pop single element")
        void testPopSingle() {
            List<String> items = new ArrayList<>(Arrays.asList("first", "second", "third"));
            ListParser<String> parser = new ListParser<>(items);

            String popped = parser.popSingle();

            assertThat(popped).isEqualTo("first");
            assertThat(parser.getList())
                    .hasSize(2)
                    .containsExactly("second", "third");
        }

        @Test
        @DisplayName("should throw when popping from empty list")
        void testPopSingleFromEmptyList() {
            ListParser<String> parser = new ListParser<>(Collections.emptyList());

            assertThatThrownBy(() -> {
                parser.popSingle();
            }).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Tried to pop an element from an empty list");
        }

        @Test
        @DisplayName("should pop single with mapper")
        void testPopSingleWithMapper() {
            List<String> items = new ArrayList<>(Arrays.asList("hello", "world"));
            ListParser<String> parser = new ListParser<>(items);

            String uppercase = parser.popSingle(String::toUpperCase);

            assertThat(uppercase).isEqualTo("HELLO");
            assertThat(parser.getList())
                    .hasSize(1)
                    .containsExactly("world");
        }

        @Test
        @DisplayName("should pop single element making list empty")
        void testPopSingleMakingListEmpty() {
            List<String> items = new ArrayList<>(Arrays.asList("only"));
            ListParser<String> parser = new ListParser<>(items);

            String popped = parser.popSingle();

            assertThat(popped).isEqualTo("only");
            assertThat(parser.isEmpty()).isTrue();
        }
    }

    @Nested
    @DisplayName("Conditional Pop Operations")
    class ConditionalPopOperations {

        @Test
        @DisplayName("should pop single if predicate matches")
        void testPopSingleIfPredicateMatches() {
            List<String> items = new ArrayList<>(Arrays.asList("hello", "world"));
            ListParser<String> parser = new ListParser<>(items);

            Predicate<String> startsWithH = s -> s.startsWith("h");
            Optional<String> result = parser.popSingleIf(startsWithH);

            assertThat(result)
                    .isPresent()
                    .hasValue("hello");

            assertThat(parser.getList())
                    .hasSize(1)
                    .containsExactly("world");
        }

        @Test
        @DisplayName("should not pop if predicate does not match")
        void testPopSingleIfPredicateDoesNotMatch() {
            List<String> items = new ArrayList<>(Arrays.asList("world", "hello"));
            ListParser<String> parser = new ListParser<>(items);

            Predicate<String> startsWithH = s -> s.startsWith("h");
            Optional<String> result = parser.popSingleIf(startsWithH);

            assertThat(result).isEmpty();

            // List should remain unchanged
            assertThat(parser.getList())
                    .hasSize(2)
                    .containsExactly("world", "hello");
        }

        @Test
        @DisplayName("should handle complex predicates")
        void testPopSingleIfWithComplexPredicate() {
            List<Animal> animals = new ArrayList<>(Arrays.asList(
                    new Dog("Rex"), new Cat("Whiskers")));
            ListParser<Animal> parser = new ListParser<>(animals);

            Predicate<Animal> isDogWithShortName = animal -> animal instanceof Dog && animal.getName().length() <= 3;

            Optional<Animal> result = parser.popSingleIf(isDogWithShortName);

            assertThat(result)
                    .isPresent()
                    .hasValueSatisfying(animal -> {
                        assertThat(animal).isInstanceOf(Dog.class);
                        assertThat(animal.getName()).isEqualTo("Rex");
                    });
        }

        @Test
        @DisplayName("should throw when peeking empty list in conditional pop")
        void testPopSingleIfFromEmptyList() {
            ListParser<String> parser = new ListParser<>(Collections.emptyList());

            Predicate<String> anyPredicate = s -> true;

            assertThatThrownBy(() -> {
                parser.popSingleIf(anyPredicate);
            }).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Tried to peek an element from an empty list");
        }
    }

    @Nested
    @DisplayName("List Addition Operations")
    class ListAdditionOperations {

        @Test
        @DisplayName("should add elements to head of list")
        void testAddElementsToHead() {
            List<String> original = new ArrayList<>(Arrays.asList("c", "d"));
            ListParser<String> parser = new ListParser<>(original);

            List<String> toAdd = Arrays.asList("a", "b");
            parser.add(toAdd);

            assertThat(parser.getList())
                    .hasSize(4)
                    .containsExactly("a", "b", "c", "d");
        }

        @Test
        @DisplayName("should add empty list without changing original")
        void testAddEmptyList() {
            List<String> original = new ArrayList<>(Arrays.asList("a", "b"));
            ListParser<String> parser = new ListParser<>(original);

            parser.add(Collections.emptyList());

            assertThat(parser.getList())
                    .hasSize(2)
                    .containsExactly("a", "b");
        }

        @Test
        @DisplayName("should add elements to empty parser")
        void testAddElementsToEmptyParser() {
            ListParser<String> parser = new ListParser<>(Collections.emptyList());

            List<String> toAdd = Arrays.asList("x", "y", "z");
            parser.add(toAdd);

            assertThat(parser.getList())
                    .hasSize(3)
                    .containsExactly("x", "y", "z");

            assertThat(parser.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("should maintain type safety when adding")
        void testAddWithTypeSafety() {
            List<Animal> animals = new ArrayList<>(Arrays.asList(new Cat("Whiskers")));
            ListParser<Animal> parser = new ListParser<>(animals);

            List<Animal> newAnimals = Arrays.asList(new Dog("Rex"), new Dog("Buddy"));
            parser.add(newAnimals);

            assertThat(parser.getList())
                    .hasSize(3);

            // Should be able to pop dogs from head
            List<Dog> dogs = parser.pop(Dog.class);
            assertThat(dogs)
                    .hasSize(2)
                    .extracting(Dog::getName)
                    .containsExactly("Rex", "Buddy");
        }
    }

    @Nested
    @DisplayName("State Management and Modification")
    class StateManagementAndModification {

        @Test
        @DisplayName("should maintain correct state after multiple operations")
        void testMultipleOperationsState() {
            List<String> items = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e"));
            ListParser<String> parser = new ListParser<>(items);

            // Pop single
            String first = parser.popSingle();
            assertThat(first).isEqualTo("a");
            assertThat(parser.getList()).hasSize(4);

            // Pop with mapper
            List<String> upper = parser.pop(2, String::toUpperCase);
            assertThat(upper).containsExactly("B", "C");
            assertThat(parser.getList()).hasSize(2);

            // Add elements
            parser.add(Arrays.asList("x", "y"));
            assertThat(parser.getList()).hasSize(4);
            assertThat(parser.getList()).containsExactly("x", "y", "d", "e");

            // Pop remaining
            while (!parser.isEmpty()) {
                parser.popSingle();
            }
            assertThat(parser.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("should handle mixed type operations correctly")
        void testMixedTypeOperations() {
            List<Number> numbers = new ArrayList<>(Arrays.asList(
                    1, 2.5, 3, 4.7, 5));
            ListParser<Number> parser = new ListParser<>(numbers);

            // Pop integers
            List<Integer> integers = parser.pop(Integer.class);
            assertThat(integers).containsExactly(1);

            // Remaining should start with Double
            assertThat(parser.getList()).hasSize(4);
            assertThat(parser.getList().get(0)).isInstanceOf(Double.class);

            // Pop one with mapper - use identity mapper since K must extend T
            Number numberValue = parser.popSingle(Function.identity());
            assertThat(numberValue).isEqualTo(2.5);
        }

        @Test
        @DisplayName("should preserve list modifications")
        void testListModificationPreservation() {
            List<String> mutableList = new ArrayList<>(Arrays.asList("a", "b", "c"));
            ListParser<String> parser = new ListParser<>(mutableList);

            // Get reference to current list
            List<String> listRef1 = parser.getList();
            assertThat(listRef1).hasSize(3);

            // Pop an element
            parser.popSingle();

            // Get new reference to list
            List<String> listRef2 = parser.getList();
            assertThat(listRef2).hasSize(2);

            // Should be different reference after modification
            assertThat(listRef1).isNotSameAs(listRef2);
        }
    }

    @Nested
    @DisplayName("toString() and Object Methods")
    class ToStringAndObjectMethods {

        @Test
        @DisplayName("should provide meaningful toString representation")
        void testToStringRepresentation() {
            List<String> items = Arrays.asList("a", "b", "c");
            ListParser<String> parser = new ListParser<>(items);

            String toString = parser.toString();

            assertThat(toString)
                    .isNotNull()
                    .contains("a", "b", "c");
        }

        @Test
        @DisplayName("should handle toString for empty list")
        void testToStringForEmptyList() {
            ListParser<String> parser = new ListParser<>(Collections.emptyList());

            String toString = parser.toString();

            assertThat(toString)
                    .isNotNull()
                    .isNotEmpty();
        }

        @Test
        @DisplayName("should update toString after modifications")
        void testToStringAfterModifications() {
            List<String> items = new ArrayList<>(Arrays.asList("a", "b", "c"));
            ListParser<String> parser = new ListParser<>(items);

            String originalToString = parser.toString();

            parser.popSingle();
            String modifiedToString = parser.toString();

            assertThat(modifiedToString)
                    .isNotEqualTo(originalToString)
                    .doesNotContain("a"); // First element should be gone
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("should handle null elements in list")
        void testNullElementsInList() {
            List<String> itemsWithNull = new ArrayList<>(Arrays.asList("a", null, "c"));
            ListParser<String> parser = new ListParser<>(itemsWithNull);

            String first = parser.popSingle();
            assertThat(first).isEqualTo("a");

            String second = parser.popSingle();
            assertThat(second).isNull();

            String third = parser.popSingle();
            assertThat(third).isEqualTo("c");
        }

        @Test
        @DisplayName("should handle predicates with null elements")
        void testPredicateWithNullElements() {
            List<String> itemsWithNull = new ArrayList<>(Arrays.asList(null, "hello"));
            ListParser<String> parser = new ListParser<>(itemsWithNull);

            Predicate<String> isNotNull = s -> s != null;
            Optional<String> result = parser.popSingleIf(isNotNull);

            assertThat(result).isEmpty(); // First element is null, predicate fails
            assertThat(parser.getList()).hasSize(2); // Nothing popped
        }

        @Test
        @DisplayName("should handle class casting with null elements")
        void testClassCastingWithNullElements() {
            List<Object> objectsWithNull = new ArrayList<>(Arrays.asList(null, "string"));
            ListParser<Object> parser = new ListParser<>(objectsWithNull);

            List<String> strings = parser.pop(String.class);

            assertThat(strings).isEmpty(); // null is not instance of String
            assertThat(parser.getList()).hasSize(2); // Nothing popped
        }

        @Test
        @DisplayName("should handle large list operations efficiently")
        void testLargeListOperations() {
            // Create large list
            List<Integer> largeList = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                largeList.add(i);
            }

            ListParser<Integer> parser = new ListParser<>(largeList);

            long startTime = System.currentTimeMillis();

            // Pop first 1000 elements with identity mapper (K extends T)
            List<Integer> numbers = parser.pop(1000, Function.identity());

            long endTime = System.currentTimeMillis();

            assertThat(numbers).hasSize(1000);
            assertThat(parser.getList()).hasSize(9000);

            // Should complete in reasonable time (less than 1 second)
            assertThat(endTime - startTime).isLessThan(1000);
        }
    }

    @Nested
    @DisplayName("Functional Programming Patterns")
    class FunctionalProgrammingPatterns {

        @Test
        @DisplayName("should work with method references")
        void testMethodReferences() {
            List<String> words = new ArrayList<>(Arrays.asList("hello", "world", "java"));
            ListParser<String> parser = new ListParser<>(words);

            List<String> uppercase = parser.pop(2, String::toUpperCase);
            List<String> lowercase = parser.pop(1, String::toLowerCase);

            assertThat(uppercase).containsExactly("HELLO", "WORLD");
            assertThat(lowercase).containsExactly("java");
        }

        @Test
        @DisplayName("should work with lambda expressions")
        void testLambdaExpressions() {
            List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
            ListParser<Integer> parser = new ListParser<>(numbers);

            List<Integer> doubled = parser.pop(3, x -> x * 2);

            assertThat(doubled).containsExactly(2, 4, 6);
            assertThat(parser.getList()).containsExactly(4, 5);
        }

        @Test
        @DisplayName("should work with complex functional transformations")
        void testComplexFunctionalTransformations() {
            List<String> sentences = new ArrayList<>(Arrays.asList(
                    "Hello World", "Java Programming", "Unit Testing"));
            ListParser<String> parser = new ListParser<>(sentences);

            // Transform to trimmed versions (K extends T where T=String)
            Function<String, String> trimmer = String::trim;
            List<String> trimmed = parser.pop(3, trimmer);

            assertThat(trimmed).containsExactly("Hello World", "Java Programming", "Unit Testing");
        }
    }
}
