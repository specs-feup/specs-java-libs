package tdrc.tuple;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive unit tests for TupleList class.
 * Tests specialized list functionality for managing collections of Tuple
 * objects.
 * 
 * @author Generated Tests
 */
@DisplayName("TupleList Tests")
public class TupleListTest {

    private TupleList<String> tupleList;

    @BeforeEach
    void setUp() {
        tupleList = TupleList.newInstance();
    }

    @Nested
    @DisplayName("Factory Methods and Constructors")
    class FactoryMethodsTests {

        @Test
        @DisplayName("Should create empty TupleList via newInstance()")
        void testEmptyTupleList() {
            TupleList<Integer> empty = TupleList.newInstance();

            assertThat(empty.size()).isZero();
        }

        @Test
        @DisplayName("Should create TupleList from multiple lists via newInstance(List...)")
        void testFromLists() {
            List<String> list1 = List.of("a", "b");
            List<String> list2 = List.of("c", "d", "e");
            List<String> list3 = List.of("f");

            TupleList<String> tupleList = TupleList.newInstance(list1, list2, list3);

            assertThat(tupleList.size()).isEqualTo(3);

            // Check first tuple
            Tuple<String> tuple1 = tupleList.get(0);
            assertThat((List<String>) tuple1).containsExactly("a", "b");

            // Check second tuple
            Tuple<String> tuple2 = tupleList.get(1);
            assertThat((List<String>) tuple2).containsExactly("c", "d", "e");

            // Check third tuple
            Tuple<String> tuple3 = tupleList.get(2);
            assertThat((List<String>) tuple3).containsExactly("f");
        }

        @Test
        @DisplayName("Should create TupleList from multiple arrays via newInstance(T[]...)")
        void testFromArrays() {
            String[] array1 = { "x", "y" };
            String[] array2 = { "z" };
            String[] array3 = { "a", "b", "c", "d" };

            TupleList<String> tupleList = TupleList.newInstance(array1, array2, array3);

            assertThat(tupleList.size()).isEqualTo(3);

            // Check first tuple
            assertThat((List<String>) tupleList.get(0)).containsExactly("x", "y");

            // Check second tuple
            assertThat((List<String>) tupleList.get(1)).containsExactly("z");

            // Check third tuple
            assertThat((List<String>) tupleList.get(2)).containsExactly("a", "b", "c", "d");
        }

        @Test
        @DisplayName("Should handle empty lists and arrays")
        void testEmptyListsAndArrays() {
            List<Integer> emptyList = List.of();
            Integer[] emptyArray = {};
            List<Integer> nonEmptyList = List.of(1, 2);

            TupleList<Integer> tupleList1 = TupleList.newInstance(emptyList, nonEmptyList);
            assertThat(tupleList1.size()).isEqualTo(2);
            assertThat(tupleList1.get(0).size()).isZero();
            assertThat((List<Integer>) tupleList1.get(1)).containsExactly(1, 2);

            TupleList<Integer> tupleList2 = TupleList.newInstance(emptyArray, new Integer[] { 3, 4 });
            assertThat(tupleList2.size()).isEqualTo(2);
            assertThat(tupleList2.get(0).size()).isZero();
            assertThat((List<Integer>) tupleList2.get(1)).containsExactly(3, 4);
        }

        @Test
        @DisplayName("Should handle null elements in lists and arrays")
        void testNullElements() {
            List<String> listWithNull = new ArrayList<>();
            listWithNull.add("a");
            listWithNull.add(null);
            listWithNull.add("c");

            TupleList<String> tupleList = TupleList.newInstance(listWithNull);

            Tuple<String> tuple = tupleList.get(0);
            assertThat(tuple.get(0)).isEqualTo("a");
            assertThat(tuple.get(1)).isNull();
            assertThat(tuple.get(2)).isEqualTo("c");
        }

        @Test
        @DisplayName("Should handle no arguments")
        void testNoArguments() {
            @SuppressWarnings("unchecked")
            TupleList<String> emptyFromLists = TupleList.newInstance(new List[0]);
            TupleList<String> emptyFromArrays = TupleList.newInstance(new String[0][]);

            assertThat(emptyFromLists.size()).isZero();
            assertThat(emptyFromArrays.size()).isZero();
        }
    }

    @Nested
    @DisplayName("List Operations")
    class ListOperationsTests {

        @Test
        @DisplayName("Should get tuples by index")
        void testGet() {
            tupleList.add(List.of("a", "b"));
            tupleList.add(List.of("c", "d", "e"));

            Tuple<String> first = tupleList.get(0);
            Tuple<String> second = tupleList.get(1);

            assertThat((List<String>) first).containsExactly("a", "b");
            assertThat((List<String>) second).containsExactly("c", "d", "e");
        }

        @Test
        @DisplayName("Should throw IndexOutOfBoundsException for invalid indices")
        void testGetInvalidIndex() {
            tupleList.add(List.of("a"));

            assertThatThrownBy(() -> tupleList.get(-1))
                    .isInstanceOf(IndexOutOfBoundsException.class);
            assertThatThrownBy(() -> tupleList.get(1))
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should return correct size")
        void testSize() {
            assertThat(tupleList.size()).isZero();

            tupleList.add(List.of("a"));
            assertThat(tupleList.size()).isEqualTo(1);

            tupleList.add(List.of("b", "c"));
            assertThat(tupleList.size()).isEqualTo(2);

            tupleList.add(List.of());
            assertThat(tupleList.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should set tuples at index")
        void testSet() {
            tupleList.add(List.of("original"));

            Tuple<String> newTuple = Tuple.newInstance("new", "tuple");
            Tuple<String> old = tupleList.set(0, newTuple);

            assertThat((List<String>) old).containsExactly("original");
            assertThat((Object) tupleList.get(0)).isSameAs(newTuple);
            assertThat((List<String>) tupleList.get(0)).containsExactly("new", "tuple");
        }

        @Test
        @DisplayName("Should add tuples from lists at specific index")
        void testAddListAtIndex() {
            tupleList.add(List.of("first"));
            tupleList.add(List.of("third"));

            tupleList.add(1, List.of("second"));

            assertThat(tupleList.size()).isEqualTo(3);
            assertThat((List<String>) tupleList.get(0)).containsExactly("first");
            assertThat((List<String>) tupleList.get(1)).containsExactly("second");
            assertThat((List<String>) tupleList.get(2)).containsExactly("third");
        }

        @Test
        @DisplayName("Should add tuples from lists at end")
        void testAddList() {
            tupleList.add(List.of("a", "b"));
            tupleList.add(List.of("c"));
            tupleList.add(List.of("d", "e", "f"));

            assertThat(tupleList.size()).isEqualTo(3);
            assertThat((List<String>) tupleList.get(0)).containsExactly("a", "b");
            assertThat((List<String>) tupleList.get(1)).containsExactly("c");
            assertThat((List<String>) tupleList.get(2)).containsExactly("d", "e", "f");
        }

        @Test
        @DisplayName("Should add Tuple objects at specific index")
        void testAddTupleAtIndex() {
            Tuple<String> tuple1 = Tuple.newInstance("first");
            Tuple<String> tuple2 = Tuple.newInstance("third");
            Tuple<String> tuple3 = Tuple.newInstance("second");

            tupleList.add(0, tuple1);
            tupleList.add(1, tuple2);
            tupleList.add(1, tuple3); // Insert between first and third

            assertThat(tupleList.size()).isEqualTo(3);
            assertThat((Object) tupleList.get(0)).isSameAs(tuple1);
            assertThat((Object) tupleList.get(1)).isSameAs(tuple3);
            assertThat((Object) tupleList.get(2)).isSameAs(tuple2);
        }

        @Test
        @DisplayName("Should remove tuples")
        void testRemove() {
            Tuple<String> tuple1 = Tuple.newInstance("a", "b");
            Tuple<String> tuple2 = Tuple.newInstance("c", "d");
            Tuple<String> tuple3 = Tuple.newInstance("e", "f");

            tupleList.add(0, tuple1);
            tupleList.add(1, tuple2);
            tupleList.add(2, tuple3);

            boolean removed = tupleList.remove(tuple2);

            assertThat(removed).isTrue();
            assertThat(tupleList.size()).isEqualTo(2);
            assertThat((Object) tupleList.get(0)).isSameAs(tuple1);
            assertThat((Object) tupleList.get(1)).isSameAs(tuple3);

            // Try to remove non-existent tuple
            boolean notRemoved = tupleList.remove(tuple2);
            assertThat(notRemoved).isFalse();
        }
    }

    @Nested
    @DisplayName("Mixed Length Tuples")
    class MixedLengthTuplesTests {

        @Test
        @DisplayName("Should handle tuples of different lengths")
        void testDifferentLengthTuples() {
            tupleList.add(List.of("single"));
            tupleList.add(List.of("first", "second"));
            tupleList.add(List.of("a", "b", "c", "d"));
            tupleList.add(List.of()); // Empty tuple

            assertThat(tupleList.size()).isEqualTo(4);
            assertThat(tupleList.get(0).size()).isEqualTo(1);
            assertThat(tupleList.get(1).size()).isEqualTo(2);
            assertThat(tupleList.get(2).size()).isEqualTo(4);
            assertThat(tupleList.get(3).size()).isZero();
        }

        @Test
        @DisplayName("Should maintain tuple independence")
        void testTupleIndependence() {
            List<String> originalList = new ArrayList<>();
            originalList.add("original");

            tupleList.add(originalList);

            // Modify original list
            originalList.add("modified");
            originalList.set(0, "changed");

            // Tuple should be unaffected
            Tuple<String> tuple = tupleList.get(0);
            assertThat(tuple.size()).isEqualTo(1);
            assertThat(tuple.get(0)).isEqualTo("original");
        }

        @Test
        @DisplayName("Should allow modification of individual tuples")
        void testTupleModification() {
            tupleList.add(List.of("a", "b"));
            tupleList.add(List.of("c", "d"));

            // Modify first tuple
            Tuple<String> firstTuple = tupleList.get(0);
            firstTuple.set(0, "modified");
            firstTuple.add("added");

            assertThat((List<String>) firstTuple).containsExactly("modified", "b", "added");
            // Second tuple should be unchanged
            assertThat((List<String>) tupleList.get(1)).containsExactly("c", "d");
        }
    }

    @Nested
    @DisplayName("Generic Type Tests")
    class GenericTypeTests {

        @Test
        @DisplayName("Should handle different generic types")
        void testDifferentTypes() {
            TupleList<Integer> intTupleList = TupleList.newInstance();
            TupleList<Double> doubleTupleList = TupleList.newInstance();

            intTupleList.add(List.of(1, 2, 3));
            doubleTupleList.add(List.of(1.1, 2.2));

            assertThat(intTupleList.get(0).get(0)).isInstanceOf(Integer.class);
            assertThat(doubleTupleList.get(0).get(0)).isInstanceOf(Double.class);
        }

        @Test
        @DisplayName("Should handle complex object types")
        void testComplexTypes() {
            TupleList<List<String>> complexTupleList = TupleList.newInstance();

            List<String> list1 = List.of("a", "b");
            List<String> list2 = List.of("c", "d", "e");

            complexTupleList.add(List.of(list1, list2));

            Tuple<List<String>> tuple = complexTupleList.get(0);
            assertThat(tuple.size()).isEqualTo(2);
            assertThat(tuple.get(0)).containsExactly("a", "b");
            assertThat(tuple.get(1)).containsExactly("c", "d", "e");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle large numbers of tuples")
        void testLargeTupleList() {
            for (int i = 0; i < 100; i++) {
                tupleList.add(List.of("tuple" + i, "element" + i));
            }

            assertThat(tupleList.size()).isEqualTo(100);
            assertThat((List<String>) tupleList.get(0)).containsExactly("tuple0", "element0");
            assertThat((List<String>) tupleList.get(99)).containsExactly("tuple99", "element99");
        }

        @Test
        @DisplayName("Should support iteration")
        void testIteration() {
            tupleList.add(List.of("a"));
            tupleList.add(List.of("b", "c"));
            tupleList.add(List.of("d", "e", "f"));

            int count = 0;
            int totalElements = 0;
            for (Tuple<String> tuple : tupleList) {
                assertThat((Object) tuple).isNotNull();
                count++;
                totalElements += tuple.size();
            }

            assertThat(count).isEqualTo(3);
            assertThat(totalElements).isEqualTo(6); // 1 + 2 + 3
        }

        @Test
        @DisplayName("Should handle mixed operations correctly")
        void testMixedOperations() {
            // Start with factory method
            TupleList<String> mixedList = TupleList.newInstance(
                    List.of("a", "b"),
                    List.of("c"));

            // Add via list
            mixedList.add(List.of("d", "e"));

            // Add via tuple
            mixedList.add(Tuple.newInstance("f", "g", "h"));

            // Insert at index
            mixedList.add(1, List.of("inserted"));

            // Replace existing
            mixedList.set(2, Tuple.newInstance("replaced"));

            assertThat(mixedList.size()).isEqualTo(5);
            assertThat((List<String>) mixedList.get(0)).containsExactly("a", "b");
            assertThat((List<String>) mixedList.get(1)).containsExactly("inserted");
            assertThat((List<String>) mixedList.get(2)).containsExactly("replaced");
            assertThat((List<String>) mixedList.get(3)).containsExactly("d", "e");
            assertThat((List<String>) mixedList.get(4)).containsExactly("f", "g", "h");
        }

        @Test
        @DisplayName("Should maintain immutability of creation parameters")
        void testCreationParameterImmutability() {
            List<String> originalList1 = new ArrayList<>();
            originalList1.add("original1");

            List<String> originalList2 = new ArrayList<>();
            originalList2.add("original2");

            TupleList<String> tupleList = TupleList.newInstance(originalList1, originalList2);

            // Modify original lists
            originalList1.add("modified");
            originalList2.set(0, "changed");

            // TupleList should be unaffected
            assertThat((List<String>) tupleList.get(0)).containsExactly("original1");
            assertThat((List<String>) tupleList.get(1)).containsExactly("original2");
        }
    }
}
