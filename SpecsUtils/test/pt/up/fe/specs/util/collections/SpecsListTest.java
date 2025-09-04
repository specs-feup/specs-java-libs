package pt.up.fe.specs.util.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for SpecsList enhanced list wrapper utility.
 * Tests custom list operations and wrapper functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsList Tests")
class SpecsListTest {

    @Nested
    @DisplayName("Constructor and Factory Methods")
    class ConstructorAndFactoryMethods {

        @Test
        @DisplayName("Should convert regular List to SpecsList")
        void testConvertFromList() {
            List<String> regularList = Arrays.asList("A", "B", "C");
            SpecsList<String> specsList = SpecsList.convert(regularList);

            assertThat(specsList).isNotNull();
            assertThat(specsList).containsExactly("A", "B", "C");
            assertThat(specsList.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should return same instance if already SpecsList")
        void testConvertFromSpecsList() {
            List<String> originalList = new ArrayList<>(Arrays.asList("X", "Y", "Z"));
            SpecsList<String> originalSpecsList = SpecsList.convert(originalList);

            SpecsList<String> convertedAgain = SpecsList.convert(originalSpecsList);

            assertThat(convertedAgain).isSameAs(originalSpecsList);
        }

        @Test
        @DisplayName("Should create new instance from class")
        void testNewInstanceFromClass() {
            SpecsList<String> specsList = SpecsList.newInstance(String.class);

            assertThat(specsList).isNotNull();
            assertThat(specsList).isEmpty();
            assertThat(specsList.size()).isZero();
        }

        @Test
        @DisplayName("Should create new instance for different types")
        void testNewInstanceDifferentTypes() {
            SpecsList<Integer> intList = SpecsList.newInstance(Integer.class);
            SpecsList<Double> doubleList = SpecsList.newInstance(Double.class);

            assertThat(intList).isEmpty();
            assertThat(doubleList).isEmpty();

            intList.add(42);
            doubleList.add(3.14);

            assertThat(intList).containsExactly(42);
            assertThat(doubleList).containsExactly(3.14);
        }
    }

    @Nested
    @DisplayName("Custom Utility Methods")
    class CustomUtilityMethods {

        @Test
        @DisplayName("Should convert to ArrayList")
        void testToArrayList() {
            List<String> linkedList = new LinkedList<>(Arrays.asList("A", "B", "C"));
            SpecsList<String> specsList = SpecsList.convert(linkedList);

            ArrayList<String> arrayList = specsList.toArrayList();

            assertThat(arrayList).isInstanceOf(ArrayList.class);
            assertThat(arrayList).containsExactly("A", "B", "C");
        }

        @Test
        @DisplayName("Should return same ArrayList if already ArrayList")
        void testToArrayListWhenAlreadyArrayList() {
            ArrayList<String> originalArrayList = new ArrayList<>(Arrays.asList("X", "Y", "Z"));
            SpecsList<String> specsList = SpecsList.convert(originalArrayList);

            ArrayList<String> result = specsList.toArrayList();

            assertThat(result).isSameAs(originalArrayList);
        }

        @Test
        @DisplayName("Should return underlying list")
        void testList() {
            List<String> originalList = Arrays.asList("A", "B", "C");
            SpecsList<String> specsList = SpecsList.convert(originalList);

            List<String> underlyingList = specsList.list();

            assertThat(underlyingList).isSameAs(originalList);
        }

        @Test
        @DisplayName("Should cast elements to subtype")
        void testCast() {
            // Create a list with only Dog instances to test successful casting
            class Animal {
            }
            class Dog extends Animal {
            }

            Dog dog1 = new Dog();
            Dog dog2 = new Dog();

            List<Animal> animals = new ArrayList<>();
            animals.add(dog1);
            animals.add(dog2);

            SpecsList<Animal> animalList = SpecsList.convert(animals);
            List<Dog> dogs = animalList.cast(Dog.class);

            // Should successfully cast all Dog instances
            assertThat(dogs).hasSize(2);
            assertThat(dogs).containsExactly(dog1, dog2);
        }

        @Test
        @DisplayName("Should throw exception when casting incompatible types")
        void testCastIncompatibleTypes() {
            class Animal {
            }
            class Dog extends Animal {
            }
            class Cat extends Animal {
            }

            Dog dog = new Dog();
            Cat cat = new Cat();

            List<Animal> animals = new ArrayList<>();
            animals.add(dog);
            animals.add(cat);

            SpecsList<Animal> animalList = SpecsList.convert(animals);

            // Should throw exception when trying to cast mixed types
            assertThatThrownBy(() -> animalList.cast(Dog.class))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Cannot cast list");
        }
    }

    @Nested
    @DisplayName("Concatenation Operations")
    class ConcatenationOperations {

        @Test
        @DisplayName("Should concatenate single element")
        void testConcatElement() {
            SpecsList<String> originalList = SpecsList.convert(Arrays.asList("A", "B"));

            SpecsList<String> result = originalList.concat("C");

            assertThat(result).containsExactly("A", "B", "C");
            assertThat(originalList).containsExactly("A", "B"); // Original unchanged
        }

        @Test
        @DisplayName("Should handle null element in concat")
        void testConcatNullElement() {
            SpecsList<String> originalList = SpecsList.convert(Arrays.asList("A", "B"));

            SpecsList<String> result = originalList.concat((String) null);

            assertThat(result).containsExactly("A", "B"); // Null elements are ignored
        }

        @Test
        @DisplayName("Should prepend single element")
        void testPrependElement() {
            SpecsList<String> originalList = SpecsList.convert(Arrays.asList("B", "C"));

            SpecsList<String> result = originalList.prepend("A");

            assertThat(result).containsExactly("A", "B", "C");
            assertThat(originalList).containsExactly("B", "C"); // Original unchanged
        }

        @Test
        @DisplayName("Should handle null element in prepend")
        void testPrependNullElement() {
            SpecsList<String> originalList = SpecsList.convert(Arrays.asList("A", "B"));

            SpecsList<String> result = originalList.prepend((String) null);

            assertThat(result).containsExactly("A", "B"); // Null elements are ignored
        }

        @Test
        @DisplayName("Should concatenate collection")
        void testConcatCollection() {
            SpecsList<String> originalList = SpecsList.convert(Arrays.asList("A", "B"));
            List<String> toConcat = Arrays.asList("C", "D", "E");

            SpecsList<String> result = originalList.concat(toConcat);

            assertThat(result).containsExactly("A", "B", "C", "D", "E");
            assertThat(originalList).containsExactly("A", "B"); // Original unchanged
        }

        @Test
        @DisplayName("Should concatenate empty collection")
        void testConcatEmptyCollection() {
            SpecsList<String> originalList = SpecsList.convert(Arrays.asList("A", "B"));
            List<String> emptyList = Collections.emptyList();

            SpecsList<String> result = originalList.concat(emptyList);

            assertThat(result).containsExactly("A", "B");
        }

        @Test
        @DisplayName("Should concatenate with subtype elements")
        void testConcatSubtypes() {
            class Animal {
            }
            class Dog extends Animal {
            }

            SpecsList<Animal> animalList = SpecsList.convert(Arrays.asList(new Animal()));
            List<Dog> dogs = Arrays.asList(new Dog(), new Dog());

            SpecsList<Animal> result = animalList.concat(dogs);

            assertThat(result).hasSize(3);
        }
    }

    @Nested
    @DisplayName("Fluent Interface Operations")
    class FluentInterfaceOperations {

        @Test
        @DisplayName("Should support fluent addition with andAdd")
        void testAndAdd() {
            SpecsList<String> specsList = SpecsList.newInstance(String.class);

            SpecsList<String> result = specsList.andAdd("A").andAdd("B").andAdd("C");

            assertThat(result).isSameAs(specsList); // Returns same instance
            assertThat(specsList).containsExactly("A", "B", "C");
        }

        @Test
        @DisplayName("Should chain andAdd operations")
        void testAndAddChaining() {
            SpecsList<Integer> numbers = SpecsList.newInstance(Integer.class)
                    .andAdd(1)
                    .andAdd(2)
                    .andAdd(3)
                    .andAdd(4)
                    .andAdd(5);

            assertThat(numbers).containsExactly(1, 2, 3, 4, 5);
        }

        @Test
        @DisplayName("Should combine andAdd with other operations")
        void testAndAddWithOtherOperations() {
            SpecsList<String> specsList = SpecsList.newInstance(String.class)
                    .andAdd("A")
                    .andAdd("B");

            specsList.add("C"); // Regular add
            SpecsList<String> extended = specsList.concat("D"); // Concat

            assertThat(specsList).containsExactly("A", "B", "C");
            assertThat(extended).containsExactly("A", "B", "C", "D");
        }
    }

    @Nested
    @DisplayName("SpecsCollection Interface")
    class SpecsCollectionInterface {

        @Test
        @DisplayName("Should convert to Set using mapper")
        void testToSetWithMapper() {
            SpecsList<String> words = SpecsList.convert(Arrays.asList("hello", "world", "test", "hello"));

            Function<String, Integer> lengthMapper = String::length;
            Set<Integer> lengths = words.toSet(lengthMapper);

            assertThat(lengths).containsExactlyInAnyOrder(5, 4); // "hello"=5, "world"=5, "test"=4
        }

        @Test
        @DisplayName("Should handle empty list in toSet")
        void testToSetEmpty() {
            SpecsList<String> emptyList = SpecsList.newInstance(String.class);

            Set<Integer> lengths = emptyList.toSet(String::length);

            assertThat(lengths).isEmpty();
        }

        @Test
        @DisplayName("Should handle duplicate mapping results in toSet")
        void testToSetDuplicates() {
            SpecsList<String> words = SpecsList.convert(Arrays.asList("cat", "dog", "pig", "cow"));

            Set<Integer> lengths = words.toSet(String::length);

            assertThat(lengths).containsExactly(3); // All words have length 3
        }
    }

    @Nested
    @DisplayName("List Interface Compliance")
    class ListInterfaceCompliance {

        @Test
        @DisplayName("Should implement basic List operations")
        void testBasicListOperations() {
            SpecsList<String> specsList = SpecsList.newInstance(String.class);

            // Test add
            specsList.add("A");
            specsList.add("B");
            assertThat(specsList).hasSize(2);

            // Test get
            assertThat(specsList.get(0)).isEqualTo("A");
            assertThat(specsList.get(1)).isEqualTo("B");

            // Test set
            specsList.set(0, "X");
            assertThat(specsList.get(0)).isEqualTo("X");

            // Test remove
            String removed = specsList.remove(0);
            assertThat(removed).isEqualTo("X");
            assertThat(specsList).hasSize(1);
            assertThat(specsList.get(0)).isEqualTo("B");
        }

        @Test
        @DisplayName("Should implement collection operations")
        void testCollectionOperations() {
            // Use ArrayList instead of Arrays.asList for modifiable list
            List<String> mutableList = new ArrayList<>(Arrays.asList("A", "B", "C"));
            SpecsList<String> specsList = SpecsList.convert(mutableList);

            // Test contains
            assertThat(specsList.contains("B")).isTrue();
            assertThat(specsList.contains("D")).isFalse();

            // Test containsAll
            assertThat(specsList.containsAll(Arrays.asList("A", "C"))).isTrue();
            assertThat(specsList.containsAll(Arrays.asList("A", "D"))).isFalse();

            // Test isEmpty
            assertThat(specsList.isEmpty()).isFalse();

            // Test clear
            specsList.clear();
            assertThat(specsList.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should implement bulk operations")
        void testBulkOperations() {
            SpecsList<String> specsList = SpecsList.newInstance(String.class);
            specsList.addAll(Arrays.asList("A", "B", "C"));

            assertThat(specsList).containsExactly("A", "B", "C");

            // Test addAll at index
            specsList.addAll(1, Arrays.asList("X", "Y"));
            assertThat(specsList).containsExactly("A", "X", "Y", "B", "C");

            // Test removeAll
            specsList.removeAll(Arrays.asList("X", "Y"));
            assertThat(specsList).containsExactly("A", "B", "C");

            // Test retainAll
            specsList.retainAll(Arrays.asList("A", "C", "D"));
            assertThat(specsList).containsExactly("A", "C");
        }

        @Test
        @DisplayName("Should implement index operations")
        void testIndexOperations() {
            // Use ArrayList for modifiable list
            List<String> mutableList = new ArrayList<>(Arrays.asList("A", "B", "A", "C"));
            SpecsList<String> specsList = SpecsList.convert(mutableList);

            // Test indexOf
            assertThat(specsList.indexOf("A")).isEqualTo(0);
            assertThat(specsList.indexOf("B")).isEqualTo(1);
            assertThat(specsList.indexOf("D")).isEqualTo(-1);

            // Test lastIndexOf
            assertThat(specsList.lastIndexOf("A")).isEqualTo(2);
            assertThat(specsList.lastIndexOf("C")).isEqualTo(3);

            // Test add at index
            specsList.add(2, "X");
            assertThat(specsList).containsExactly("A", "B", "X", "A", "C");

            // Test remove at index
            String removed = specsList.remove(2);
            assertThat(removed).isEqualTo("X");
            assertThat(specsList).containsExactly("A", "B", "A", "C");
        }

        @Test
        @DisplayName("Should implement subList operations")
        void testSubListOperations() {
            SpecsList<String> specsList = SpecsList.convert(Arrays.asList("A", "B", "C", "D", "E"));

            List<String> subList = specsList.subList(1, 4);

            assertThat(subList).containsExactly("B", "C", "D");

            // Modifications to sublist should affect original
            subList.set(0, "X");
            assertThat(specsList.get(1)).isEqualTo("X");
        }

        @Test
        @DisplayName("Should implement iterator operations")
        void testIteratorOperations() {
            SpecsList<String> specsList = SpecsList.convert(Arrays.asList("A", "B", "C"));

            // Test iterator
            Iterator<String> iterator = specsList.iterator();
            List<String> iteratedElements = new ArrayList<>();
            while (iterator.hasNext()) {
                iteratedElements.add(iterator.next());
            }
            assertThat(iteratedElements).containsExactly("A", "B", "C");

            // Test listIterator
            ListIterator<String> listIterator = specsList.listIterator();
            assertThat(listIterator.hasNext()).isTrue();
            assertThat(listIterator.next()).isEqualTo("A");

            // Test listIterator from index
            ListIterator<String> listIteratorFromIndex = specsList.listIterator(1);
            assertThat(listIteratorFromIndex.next()).isEqualTo("B");
        }
    }

    @Nested
    @DisplayName("Array Conversion")
    class ArrayConversion {

        @Test
        @DisplayName("Should convert to Object array")
        void testToObjectArray() {
            SpecsList<String> specsList = SpecsList.convert(Arrays.asList("A", "B", "C"));

            Object[] array = specsList.toArray();

            assertThat(array).hasSize(3);
            assertThat(array).containsExactly("A", "B", "C");
        }

        @Test
        @DisplayName("Should convert to typed array")
        void testToTypedArray() {
            SpecsList<String> specsList = SpecsList.convert(Arrays.asList("A", "B", "C"));

            String[] array = specsList.toArray(new String[0]);

            assertThat(array).hasSize(3);
            assertThat(array).containsExactly("A", "B", "C");
        }

        @Test
        @DisplayName("Should convert to pre-sized array")
        void testToPreSizedArray() {
            SpecsList<String> specsList = SpecsList.convert(Arrays.asList("A", "B", "C"));

            String[] array = new String[5];
            String[] result = specsList.toArray(array);

            assertThat(result).isSameAs(array);
            assertThat(result[0]).isEqualTo("A");
            assertThat(result[1]).isEqualTo("B");
            assertThat(result[2]).isEqualTo("C");
            assertThat(result[3]).isNull(); // Remaining elements are null
            assertThat(result[4]).isNull();
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentation {

        @Test
        @DisplayName("Should provide meaningful toString")
        void testToString() {
            SpecsList<String> specsList = SpecsList.convert(Arrays.asList("A", "B", "C"));

            String result = specsList.toString();

            assertThat(result).isEqualTo("[A, B, C]");
        }

        @Test
        @DisplayName("Should handle empty list toString")
        void testToStringEmpty() {
            SpecsList<String> emptyList = SpecsList.newInstance(String.class);

            String result = emptyList.toString();

            assertThat(result).isEqualTo("[]");
        }

        @Test
        @DisplayName("Should handle toString with different types")
        void testToStringDifferentTypes() {
            SpecsList<Integer> numbers = SpecsList.convert(Arrays.asList(1, 2, 3));

            String result = numbers.toString();

            assertThat(result).isEqualTo("[1, 2, 3]");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle null values in list")
        void testNullValues() {
            List<String> listWithNulls = new ArrayList<>();
            listWithNulls.add("A");
            listWithNulls.add(null);
            listWithNulls.add("B");

            SpecsList<String> specsList = SpecsList.convert(listWithNulls);

            assertThat(specsList).hasSize(3);
            assertThat(specsList.get(0)).isEqualTo("A");
            assertThat(specsList.get(1)).isNull();
            assertThat(specsList.get(2)).isEqualTo("B");
        }

        @Test
        @DisplayName("Should throw appropriate exceptions for invalid operations")
        void testExceptionHandling() {
            // Use ArrayList for modifiable list
            List<String> mutableList = new ArrayList<>(Arrays.asList("A", "B", "C"));
            SpecsList<String> specsList = SpecsList.convert(mutableList);

            // Test IndexOutOfBoundsException
            assertThatThrownBy(() -> specsList.get(10))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> specsList.set(10, "X"))
                    .isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> specsList.remove(10))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should handle immutable list exceptions")
        void testImmutableListExceptions() {
            // Use Arrays.asList which creates immutable list
            SpecsList<String> immutableSpecsList = SpecsList.convert(Arrays.asList("A", "B", "C"));

            // Test UnsupportedOperationException for modification operations
            assertThatThrownBy(() -> immutableSpecsList.add("D"))
                    .isInstanceOf(UnsupportedOperationException.class);

            assertThatThrownBy(() -> immutableSpecsList.remove(0))
                    .isInstanceOf(UnsupportedOperationException.class);

            assertThatThrownBy(() -> immutableSpecsList.clear())
                    .isInstanceOf(UnsupportedOperationException.class);
        }

        @Test
        @DisplayName("Should handle concurrent modification scenarios")
        void testConcurrentModification() {
            SpecsList<String> specsList = SpecsList.newInstance(String.class);
            specsList.addAll(Arrays.asList("A", "B", "C", "D", "E"));

            Iterator<String> iterator = specsList.iterator();
            iterator.next(); // Move to first element

            // Modify list during iteration
            specsList.add("F");

            // Next iterator operation should throw ConcurrentModificationException
            assertThatThrownBy(() -> iterator.next())
                    .isInstanceOf(ConcurrentModificationException.class);
        }
    }

    @Nested
    @DisplayName("Type Safety and Generics")
    class TypeSafetyAndGenerics {

        @Test
        @DisplayName("Should maintain type safety with different generic types")
        void testTypeSafety() {
            SpecsList<Integer> intList = SpecsList.newInstance(Integer.class);
            SpecsList<String> stringList = SpecsList.newInstance(String.class);

            intList.add(42);
            stringList.add("Hello");

            Integer intValue = intList.get(0);
            String stringValue = stringList.get(0);

            assertThat(intValue).isEqualTo(42);
            assertThat(stringValue).isEqualTo("Hello");
        }

        @Test
        @DisplayName("Should work with complex generic types")
        void testComplexGenerics() {
            List<List<String>> nestedList = Arrays.asList(
                    Arrays.asList("A", "B"),
                    Arrays.asList("C", "D"),
                    Arrays.asList("E", "F"));

            SpecsList<List<String>> specsNestedList = SpecsList.convert(nestedList);

            assertThat(specsNestedList).hasSize(3);
            assertThat(specsNestedList.get(0)).containsExactly("A", "B");
            assertThat(specsNestedList.get(1)).containsExactly("C", "D");
            assertThat(specsNestedList.get(2)).containsExactly("E", "F");
        }

        @Test
        @DisplayName("Should handle wildcard generics")
        void testWildcardGenerics() {
            class Animal {
            }
            class Dog extends Animal {
            }
            class Cat extends Animal {
            }

            SpecsList<Animal> animals = SpecsList.newInstance(Animal.class);
            animals.add(new Dog());
            animals.add(new Cat());
            animals.add(new Animal());

            assertThat(animals).hasSize(3);
            assertThat(animals.get(0)).isInstanceOf(Dog.class);
            assertThat(animals.get(1)).isInstanceOf(Cat.class);
            assertThat(animals.get(2)).isInstanceOf(Animal.class);
        }
    }

    @Nested
    @DisplayName("Integration and Workflow Tests")
    class IntegrationAndWorkflowTests {

        @Test
        @DisplayName("Should support builder pattern workflow")
        void testBuilderPatternWorkflow() {
            SpecsList<String> result = SpecsList.newInstance(String.class)
                    .andAdd("Start")
                    .concat(Arrays.asList("Middle1", "Middle2"))
                    .prepend("Beginning")
                    .concat("End");

            assertThat(result).containsExactly("Beginning", "Start", "Middle1", "Middle2", "End");
        }

        @Test
        @DisplayName("Should support data processing pipeline")
        void testDataProcessingPipeline() {
            // Start with raw data
            List<String> rawData = Arrays.asList("apple", "banana", "cherry", "date");
            SpecsList<String> specsData = SpecsList.convert(rawData);

            // Transform to lengths
            Set<Integer> lengths = specsData.toSet(String::length);

            // Add more data
            SpecsList<String> moreData = specsData
                    .concat("elderberry")
                    .andAdd("fig");

            assertThat(lengths).containsExactlyInAnyOrder(5, 6, 4); // apple=5, banana=6, cherry=6, date=4
            assertThat(moreData).containsExactly("apple", "banana", "cherry", "date", "elderberry", "fig");
        }

        @Test
        @DisplayName("Should support list composition")
        void testListComposition() {
            SpecsList<Integer> evens = SpecsList.newInstance(Integer.class)
                    .andAdd(2).andAdd(4).andAdd(6);

            SpecsList<Integer> odds = SpecsList.newInstance(Integer.class)
                    .andAdd(1).andAdd(3).andAdd(5);

            SpecsList<Integer> combined = evens.concat(odds);
            SpecsList<Integer> withZero = combined.prepend(0);

            assertThat(combined).containsExactly(2, 4, 6, 1, 3, 5);
            assertThat(withZero).containsExactly(0, 2, 4, 6, 1, 3, 5);
        }
    }
}
