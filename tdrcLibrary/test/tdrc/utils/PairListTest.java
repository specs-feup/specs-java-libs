package tdrc.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive unit tests for PairList class.
 * Tests specialized list functionality for Pair objects.
 * 
 * @author Generated Tests
 */
@DisplayName("PairList Tests")
public class PairListTest {

    private PairList<String, Integer> pairList;

    @BeforeEach
    void setUp() {
        pairList = new PairList<>();
    }

    @Nested
    @DisplayName("Constructor and Basic Operations")
    class ConstructorAndBasicOperationsTests {

        @Test
        @DisplayName("Should create empty PairList")
        void testEmptyPairList() {
            assertThat(pairList).isEmpty();
            assertThat(pairList.size()).isZero();
        }

        @Test
        @DisplayName("Should inherit from ArrayList")
        void testInheritance() {
            assertThat(pairList).isInstanceOf(ArrayList.class);
            assertThat(pairList).isInstanceOf(List.class);
        }
    }

    @Nested
    @DisplayName("Add Operations")
    class AddOperationsTests {

        @Test
        @DisplayName("Should add pair using key-value parameters")
        void testAddKeyValue() {
            Pair<String, Integer> result = pairList.add("key1", 42);

            assertThat(result).isNotNull();
            assertThat(result.left()).isEqualTo("key1");
            assertThat(result.right()).isEqualTo(42);
            assertThat(pairList.size()).isEqualTo(1);
            assertThat(pairList.get(0)).isSameAs(result);
        }

        @Test
        @DisplayName("Should add multiple pairs")
        void testAddMultiplePairs() {
            Pair<String, Integer> pair1 = pairList.add("key1", 10);
            Pair<String, Integer> pair2 = pairList.add("key2", 20);
            Pair<String, Integer> pair3 = pairList.add("key3", 30);

            assertThat(pairList.size()).isEqualTo(3);
            assertThat(pairList.get(0)).isSameAs(pair1);
            assertThat(pairList.get(1)).isSameAs(pair2);
            assertThat(pairList.get(2)).isSameAs(pair3);
        }

        @Test
        @DisplayName("Should handle null values in pairs")
        void testAddNullValues() {
            Pair<String, Integer> pairWithNullKey = pairList.add(null, 42);
            Pair<String, Integer> pairWithNullValue = pairList.add("key", null);
            Pair<String, Integer> pairWithBothNull = pairList.add(null, null);

            assertThat(pairWithNullKey).isNotNull();
            assertThat(pairWithNullKey.left()).isNull();
            assertThat(pairWithNullKey.right()).isEqualTo(42);

            assertThat(pairWithNullValue).isNotNull();
            assertThat(pairWithNullValue.left()).isEqualTo("key");
            assertThat(pairWithNullValue.right()).isNull();

            assertThat(pairWithBothNull).isNotNull();
            assertThat(pairWithBothNull.left()).isNull();
            assertThat(pairWithBothNull.right()).isNull();

            assertThat(pairList.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should also support traditional ArrayList add method")
        void testTraditionalAdd() {
            Pair<String, Integer> pair = new Pair<>("key", 42);
            boolean added = pairList.add(pair);

            assertThat(added).isTrue();
            assertThat(pairList.size()).isEqualTo(1);
            assertThat(pairList.get(0)).isSameAs(pair);
        }
    }

    @Nested
    @DisplayName("First and Last Operations")
    class FirstAndLastOperationsTests {

        @Test
        @DisplayName("Should throw exception when getting first from empty list")
        void testFirstOnEmptyList() {
            assertThatThrownBy(() -> pairList.first())
                    .isInstanceOf(IndexOutOfBoundsException.class)
                    .hasMessage("The list of pairs is empty");
        }

        @Test
        @DisplayName("Should throw exception when getting last from empty list")
        void testLastOnEmptyList() {
            assertThatThrownBy(() -> pairList.last())
                    .isInstanceOf(IndexOutOfBoundsException.class)
                    .hasMessage("The list of pairs is empty");
        }

        @Test
        @DisplayName("Should get first element correctly")
        void testFirst() {
            Pair<String, Integer> first = pairList.add("first", 1);
            pairList.add("second", 2);
            pairList.add("third", 3);

            assertThat(pairList.first()).isSameAs(first);
            assertThat(pairList.first().left()).isEqualTo("first");
            assertThat(pairList.first().right()).isEqualTo(1);
        }

        @Test
        @DisplayName("Should get last element correctly")
        void testLast() {
            pairList.add("first", 1);
            pairList.add("second", 2);
            Pair<String, Integer> last = pairList.add("third", 3);

            assertThat(pairList.last()).isSameAs(last);
            assertThat(pairList.last().left()).isEqualTo("third");
            assertThat(pairList.last().right()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should handle single element for both first and last")
        void testSingleElement() {
            Pair<String, Integer> single = pairList.add("only", 42);

            assertThat(pairList.first()).isSameAs(single);
            assertThat(pairList.last()).isSameAs(single);
            assertThat(pairList.first()).isSameAs(pairList.last());
        }

        @Test
        @DisplayName("Should update first and last after modifications")
        void testFirstLastAfterModifications() {
            pairList.add("first", 1);
            pairList.add("middle", 2);
            pairList.add("last", 3);

            // Remove first element
            pairList.remove(0);
            assertThat(pairList.first().left()).isEqualTo("middle");

            // Remove last element
            pairList.remove(pairList.size() - 1);
            assertThat(pairList.last().left()).isEqualTo("middle");

            // They should be the same now
            assertThat(pairList.first()).isSameAs(pairList.last());
        }
    }

    @Nested
    @DisplayName("Generic Type Tests")
    class GenericTypeTests {

        @Test
        @DisplayName("Should handle different generic types")
        void testDifferentGenericTypes() {
            PairList<Integer, String> intStringList = new PairList<>();
            intStringList.add(1, "one");
            intStringList.add(2, "two");

            assertThat(intStringList.first().left()).isEqualTo(1);
            assertThat(intStringList.first().right()).isEqualTo("one");
            assertThat(intStringList.last().right()).isEqualTo("two");
        }

        @Test
        @DisplayName("Should handle same types for key and value")
        void testSameGenericTypes() {
            PairList<String, String> stringStringList = new PairList<>();
            stringStringList.add("key1", "value1");
            stringStringList.add("key2", "value2");

            assertThat(stringStringList.size()).isEqualTo(2);
            assertThat(stringStringList.first().left()).isEqualTo("key1");
            assertThat(stringStringList.first().right()).isEqualTo("value1");
        }

        @Test
        @DisplayName("Should handle complex object types")
        void testComplexObjectTypes() {
            PairList<List<String>, Integer> complexList = new PairList<>();
            List<String> list1 = List.of("a", "b");
            List<String> list2 = List.of("c", "d", "e");

            complexList.add(list1, list1.size());
            complexList.add(list2, list2.size());

            assertThat(complexList.size()).isEqualTo(2);
            assertThat(complexList.first().left()).containsExactly("a", "b");
            assertThat(complexList.first().right()).isEqualTo(2);
            assertThat(complexList.last().left()).containsExactly("c", "d", "e");
            assertThat(complexList.last().right()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Serialization Tests")
    class SerializationTests {

        @Test
        @DisplayName("Should be serializable")
        void testSerializable() {
            assertThat(pairList).isInstanceOf(java.io.Serializable.class);
        }
    }

    @Nested
    @DisplayName("ArrayList Integration Tests")
    class ArrayListIntegrationTests {

        @Test
        @DisplayName("Should support all ArrayList operations")
        void testArrayListOperations() {
            pairList.add("key1", 10);
            pairList.add("key2", 20);
            pairList.add("key3", 30);

            // Test size, contains, etc.
            assertThat(pairList.size()).isEqualTo(3);
            assertThat(pairList.isEmpty()).isFalse();

            Pair<String, Integer> pair = pairList.get(1);
            assertThat(pairList.contains(pair)).isTrue();
            assertThat(pairList.indexOf(pair)).isEqualTo(1);

            // Test removal
            pairList.remove(1);
            assertThat(pairList.size()).isEqualTo(2);
            assertThat(pairList.contains(pair)).isFalse();
        }

        @Test
        @DisplayName("Should support clear operation")
        void testClear() {
            pairList.add("key1", 10);
            pairList.add("key2", 20);

            pairList.clear();
            assertThat(pairList).isEmpty();
            assertThat(pairList.size()).isZero();

            // first() and last() should throw after clear
            assertThatThrownBy(() -> pairList.first())
                    .isInstanceOf(IndexOutOfBoundsException.class);
            assertThatThrownBy(() -> pairList.last())
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should support iteration")
        void testIteration() {
            pairList.add("a", 1);
            pairList.add("b", 2);
            pairList.add("c", 3);

            int count = 0;
            for (Pair<String, Integer> pair : pairList) {
                assertThat(pair).isNotNull();
                assertThat(pair.left()).isNotNull();
                assertThat(pair.right()).isNotNull();
                count++;
            }
            assertThat(count).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle large number of pairs")
        void testLargePairList() {
            int numPairs = 1000;
            for (int i = 0; i < numPairs; i++) {
                pairList.add("key" + i, i);
            }

            assertThat(pairList.size()).isEqualTo(numPairs);
            assertThat(pairList.first().left()).isEqualTo("key0");
            assertThat(pairList.first().right()).isEqualTo(0);
            assertThat(pairList.last().left()).isEqualTo("key999");
            assertThat(pairList.last().right()).isEqualTo(999);
        }

        @Test
        @DisplayName("Should maintain consistency after mixed operations")
        void testMixedOperations() {
            // Add some pairs using both methods
            pairList.add("key1", 10);
            pairList.add(new Pair<>("key2", 20));
            pairList.add("key3", 30);

            assertThat(pairList.size()).isEqualTo(3);
            assertThat(pairList.first().left()).isEqualTo("key1");
            assertThat(pairList.last().left()).isEqualTo("key3");

            // Remove middle element
            pairList.remove(1);
            assertThat(pairList.size()).isEqualTo(2);
            assertThat(pairList.get(1).left()).isEqualTo("key3");

            // Add more
            pairList.add("key4", 40);
            assertThat(pairList.last().left()).isEqualTo("key4");
        }
    }
}
