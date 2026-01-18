package org.specs.generators.java.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Comprehensive test suite for the UniqueList class.
 * Tests unique constraint behavior, ArrayList functionality, and edge cases.
 * 
 * @author Generated Tests
 */
@DisplayName("UniqueList - Unique ArrayList Implementation Test Suite")
public class UniqueListTest {

    private UniqueList<String> uniqueList;

    @BeforeEach
    void setUp() {
        uniqueList = new UniqueList<>();
    }

    @Nested
    @DisplayName("Basic Uniqueness Tests")
    class BasicUniquenessTests {

        @Test
        @DisplayName("Should allow adding unique elements")
        void shouldAllowAddingUniqueElements() {
            boolean result1 = uniqueList.add("element1");
            boolean result2 = uniqueList.add("element2");
            boolean result3 = uniqueList.add("element3");

            assertThat(result1).isTrue();
            assertThat(result2).isTrue();
            assertThat(result3).isTrue();
            assertThat(uniqueList).hasSize(3);
            assertThat(uniqueList).containsExactly("element1", "element2", "element3");
        }

        @Test
        @DisplayName("Should prevent adding duplicate elements")
        void shouldPreventAddingDuplicateElements() {
            uniqueList.add("duplicate");
            boolean result = uniqueList.add("duplicate");

            assertThat(result).isFalse();
            assertThat(uniqueList).hasSize(1);
            assertThat(uniqueList).containsExactly("duplicate");
        }

        @Test
        @DisplayName("Should prevent adding null duplicates")
        void shouldPreventAddingNullDuplicates() {
            uniqueList.add(null);
            boolean result = uniqueList.add(null);

            assertThat(result).isFalse();
            assertThat(uniqueList).hasSize(1);
            assertThat(uniqueList).containsExactly((String) null);
        }

        @Test
        @DisplayName("Should allow adding null once")
        void shouldAllowAddingNullOnce() {
            boolean result = uniqueList.add(null);

            assertThat(result).isTrue();
            assertThat(uniqueList).hasSize(1);
            assertThat(uniqueList.get(0)).isNull();
        }

        @Test
        @DisplayName("Should maintain insertion order for unique elements")
        void shouldMaintainInsertionOrderForUniqueElements() {
            uniqueList.add("third");
            uniqueList.add("first");
            uniqueList.add("second");
            uniqueList.add("first"); // duplicate, should be ignored

            assertThat(uniqueList).hasSize(3);
            assertThat(uniqueList).containsExactly("third", "first", "second");
        }
    }

    @Nested
    @DisplayName("Positional Add Tests")
    class PositionalAddTests {

        @Test
        @DisplayName("Should add unique element at specific position")
        void shouldAddUniqueElementAtSpecificPosition() {
            uniqueList.add("element1");
            uniqueList.add("element3");

            uniqueList.add(1, "element2");

            assertThat(uniqueList).hasSize(3);
            assertThat(uniqueList).containsExactly("element1", "element2", "element3");
        }

        @Test
        @DisplayName("Should not add duplicate element at specific position")
        void shouldNotAddDuplicateElementAtSpecificPosition() {
            uniqueList.add("existing");
            uniqueList.add("element2");

            assertThatCode(() -> {
                uniqueList.add(1, "existing");
            }).doesNotThrowAnyException();

            // Should remain unchanged
            assertThat(uniqueList).hasSize(2);
            assertThat(uniqueList).containsExactly("existing", "element2");
        }

        @Test
        @DisplayName("Should handle positional add at beginning")
        void shouldHandlePositionalAddAtBeginning() {
            uniqueList.add("second");
            uniqueList.add("third");

            uniqueList.add(0, "first");

            assertThat(uniqueList).hasSize(3);
            assertThat(uniqueList).containsExactly("first", "second", "third");
        }

        @Test
        @DisplayName("Should handle positional add at end")
        void shouldHandlePositionalAddAtEnd() {
            uniqueList.add("first");
            uniqueList.add("second");

            uniqueList.add(2, "third");

            assertThat(uniqueList).hasSize(3);
            assertThat(uniqueList).containsExactly("first", "second", "third");
        }

        @Test
        @DisplayName("Should throw IndexOutOfBoundsException for invalid index")
        void shouldThrowIndexOutOfBoundsExceptionForInvalidIndex() {
            uniqueList.add("element");

            assertThatThrownBy(() -> {
                uniqueList.add(5, "newElement");
            }).isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> {
                uniqueList.add(-1, "newElement");
            }).isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should not affect list when adding duplicate at position")
        void shouldNotAffectListWhenAddingDuplicateAtPosition() {
            uniqueList.add("a");
            uniqueList.add("b");
            uniqueList.add("c");

            // Try to insert duplicate "b" at position 1 (where "b" already exists)
            uniqueList.add(1, "b");

            assertThat(uniqueList).hasSize(3);
            assertThat(uniqueList).containsExactly("a", "b", "c");
        }
    }

    @Nested
    @DisplayName("AddAll Tests")
    class AddAllTests {

        @Test
        @DisplayName("Should add all unique elements from collection")
        void shouldAddAllUniqueElementsFromCollection() {
            List<String> elements = Arrays.asList("a", "b", "c");

            boolean result = uniqueList.addAll(elements);

            assertThat(result).isTrue();
            assertThat(uniqueList).hasSize(3);
            assertThat(uniqueList).containsExactlyInAnyOrder("a", "b", "c");
        }

        @Test
        @DisplayName("Should filter out duplicates when adding collection")
        void shouldFilterOutDuplicatesWhenAddingCollection() {
            uniqueList.add("a");
            uniqueList.add("c");

            List<String> elements = Arrays.asList("a", "b", "c", "d");
            boolean result = uniqueList.addAll(elements);

            assertThat(result).isTrue(); // Some elements were added
            assertThat(uniqueList).hasSize(4);
            assertThat(uniqueList).containsExactlyInAnyOrder("a", "c", "b", "d");
        }

        @Test
        @DisplayName("Should return false when no new elements are added")
        void shouldReturnFalseWhenNoNewElementsAreAdded() {
            uniqueList.add("a");
            uniqueList.add("b");

            List<String> elements = Arrays.asList("a", "b");
            boolean result = uniqueList.addAll(elements);

            assertThat(result).isFalse(); // No new elements added
            assertThat(uniqueList).hasSize(2);
            assertThat(uniqueList).containsExactly("a", "b");
        }

        @Test
        @DisplayName("Should handle empty collection in addAll")
        void shouldHandleEmptyCollectionInAddAll() {
            uniqueList.add("existing");

            boolean result = uniqueList.addAll(Arrays.asList());

            assertThat(result).isFalse();
            assertThat(uniqueList).hasSize(1);
            assertThat(uniqueList).containsExactly("existing");
        }

        @Test
        @DisplayName("Should handle null collection in addAll")
        void shouldHandleNullCollectionInAddAll() {
            uniqueList.add("existing");

            assertThatThrownBy(() -> {
                uniqueList.addAll(null);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should add collection with internal duplicates correctly")
        void shouldAddCollectionWithInternalDuplicatesCorrectly() {
            List<String> elements = Arrays.asList("a", "b", "a", "c", "b", "d");

            boolean result = uniqueList.addAll(elements);

            assertThat(result).isTrue();
            assertThat(uniqueList).hasSize(4);
            assertThat(uniqueList).containsExactlyInAnyOrder("a", "b", "c", "d");
        }
    }

    @Nested
    @DisplayName("Positional AddAll Tests")
    class PositionalAddAllTests {

        @Test
        @DisplayName("Should add collection at specific position")
        void shouldAddCollectionAtSpecificPosition() {
            uniqueList.add("first");
            uniqueList.add("last");

            List<String> middle = Arrays.asList("second", "third");
            boolean result = uniqueList.addAll(1, middle);

            assertThat(result).isTrue();
            assertThat(uniqueList).hasSize(4);
            assertThat(uniqueList).containsExactly("first", "second", "third", "last");
        }

        @Test
        @DisplayName("Should filter duplicates when adding collection at position")
        void shouldFilterDuplicatesWhenAddingCollectionAtPosition() {
            uniqueList.add("first");
            uniqueList.add("last");

            List<String> elements = Arrays.asList("first", "middle", "last", "new");
            boolean result = uniqueList.addAll(1, elements);

            // Should only add "middle" and "new" (first and last are duplicates)
            assertThat(result).isTrue();
            assertThat(uniqueList).hasSize(4);
            assertThat(uniqueList).containsExactly("first", "middle", "new", "last");
        }

        @Test
        @DisplayName("Should return false when no elements added at position")
        void shouldReturnFalseWhenNoElementsAddedAtPosition() {
            uniqueList.add("a");
            uniqueList.add("b");

            List<String> duplicates = Arrays.asList("a", "b");
            boolean result = uniqueList.addAll(1, duplicates);

            assertThat(result).isFalse();
            assertThat(uniqueList).hasSize(2);
            assertThat(uniqueList).containsExactly("a", "b");
        }

        @Test
        @DisplayName("Should throw IndexOutOfBoundsException for invalid position")
        void shouldThrowIndexOutOfBoundsExceptionForInvalidPosition() {
            uniqueList.add("element");

            List<String> elements = Arrays.asList("new");

            assertThatThrownBy(() -> {
                uniqueList.addAll(5, elements);
            }).isInstanceOf(IndexOutOfBoundsException.class);

            assertThatThrownBy(() -> {
                uniqueList.addAll(-1, elements);
            }).isInstanceOf(IndexOutOfBoundsException.class);
        }
    }

    @Nested
    @DisplayName("ArrayList Functionality Tests")
    class ArrayListFunctionalityTests {

        @Test
        @DisplayName("Should support get operation")
        void shouldSupportGetOperation() {
            uniqueList.add("first");
            uniqueList.add("second");
            uniqueList.add("third");

            assertThat(uniqueList.get(0)).isEqualTo("first");
            assertThat(uniqueList.get(1)).isEqualTo("second");
            assertThat(uniqueList.get(2)).isEqualTo("third");
        }

        @Test
        @DisplayName("Should support set operation")
        void shouldSupportSetOperation() {
            uniqueList.add("original");

            String oldValue = uniqueList.set(0, "replaced");

            assertThat(oldValue).isEqualTo("original");
            assertThat(uniqueList.get(0)).isEqualTo("replaced");
            assertThat(uniqueList).hasSize(1);
        }

        @Test
        @DisplayName("Should support remove operation")
        void shouldSupportRemoveOperation() {
            uniqueList.add("keep");
            uniqueList.add("remove");
            uniqueList.add("keep2");

            String removed = uniqueList.remove(1);

            assertThat(removed).isEqualTo("remove");
            assertThat(uniqueList).hasSize(2);
            assertThat(uniqueList).containsExactly("keep", "keep2");
        }

        @Test
        @DisplayName("Should support contains operation")
        void shouldSupportContainsOperation() {
            uniqueList.add("present");

            assertThat(uniqueList.contains("present")).isTrue();
            assertThat(uniqueList.contains("absent")).isFalse();
        }

        @Test
        @DisplayName("Should support clear operation")
        void shouldSupportClearOperation() {
            uniqueList.add("a");
            uniqueList.add("b");
            uniqueList.add("c");

            uniqueList.clear();

            assertThat(uniqueList).isEmpty();
            assertThat(uniqueList).hasSize(0);
        }

        @Test
        @DisplayName("Should support isEmpty operation")
        void shouldSupportIsEmptyOperation() {
            assertThat(uniqueList.isEmpty()).isTrue();

            uniqueList.add("element");
            assertThat(uniqueList.isEmpty()).isFalse();

            uniqueList.clear();
            assertThat(uniqueList.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("Should support iterator")
        void shouldSupportIterator() {
            uniqueList.add("a");
            uniqueList.add("b");
            uniqueList.add("c");

            StringBuilder result = new StringBuilder();
            for (String element : uniqueList) {
                result.append(element);
            }

            assertThat(result.toString()).isEqualTo("abc");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling Tests")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("Should handle large numbers of unique elements")
        void shouldHandleLargeNumbersOfUniqueElements() {
            for (int i = 0; i < 1000; i++) {
                uniqueList.add("element" + i);
            }

            assertThat(uniqueList).hasSize(1000);
            assertThat(uniqueList.get(0)).isEqualTo("element0");
            assertThat(uniqueList.get(999)).isEqualTo("element999");
        }

        @Test
        @DisplayName("Should handle many duplicate attempts efficiently")
        void shouldHandleManyDuplicateAttemptsEfficiently() {
            uniqueList.add("constant");

            for (int i = 0; i < 100; i++) {
                boolean result = uniqueList.add("constant");
                assertThat(result).isFalse();
            }

            assertThat(uniqueList).hasSize(1);
            assertThat(uniqueList).containsExactly("constant");
        }

        @Test
        @DisplayName("Should handle mixed null and non-null elements")
        void shouldHandleMixedNullAndNonNullElements() {
            uniqueList.add(null);
            uniqueList.add("notNull");
            uniqueList.add(null); // duplicate null
            uniqueList.add("notNull"); // duplicate string

            assertThat(uniqueList).hasSize(2);
            assertThat(uniqueList).containsExactly(null, "notNull");
        }

        @Test
        @DisplayName("Should maintain uniqueness after set operations")
        void shouldMaintainUniquenessAfterSetOperations() {
            uniqueList.add("a");
            uniqueList.add("b");
            uniqueList.add("c");

            // This might break uniqueness in a poorly implemented version
            uniqueList.set(2, "a"); // Set index 2 to same value as index 0

            // Now we have duplicates, but that's allowed via set
            assertThat(uniqueList).hasSize(3);
            assertThat(uniqueList.get(0)).isEqualTo("a");
            assertThat(uniqueList.get(2)).isEqualTo("a");

            // But add should still prevent new duplicates
            boolean result = uniqueList.add("a");
            assertThat(result).isFalse();
            assertThat(uniqueList).hasSize(3);
        }

        @Test
        @DisplayName("Should handle equals comparison correctly")
        void shouldHandleEqualsComparisonCorrectly() {
            String str1 = new String("duplicate");
            String str2 = new String("duplicate");

            uniqueList.add(str1);
            boolean result = uniqueList.add(str2);

            assertThat(result).isFalse(); // Should recognize as duplicate
            assertThat(uniqueList).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Type Safety Tests")
    class TypeSafetyTests {

        @Test
        @DisplayName("Should work with Integer type")
        void shouldWorkWithIntegerType() {
            UniqueList<Integer> intList = new UniqueList<>();

            intList.add(1);
            intList.add(2);
            intList.add(1); // duplicate

            assertThat(intList).hasSize(2);
            assertThat(intList).containsExactly(1, 2);
        }

        @Test
        @DisplayName("Should work with custom objects")
        void shouldWorkWithCustomObjects() {
            UniqueList<TestObject> objList = new UniqueList<>();
            TestObject obj1 = new TestObject("test");
            TestObject obj2 = new TestObject("test");

            objList.add(obj1);
            objList.add(obj2); // Should be treated as different objects

            assertThat(objList).hasSize(2);
            assertThat(objList).contains(obj1);
            assertThat(objList).contains(obj2);
        }

        @Test
        @DisplayName("Should respect equals/hashCode for custom objects")
        void shouldRespectEqualsHashCodeForCustomObjects() {
            UniqueList<EqualsTestObject> objList = new UniqueList<>();
            EqualsTestObject obj1 = new EqualsTestObject("same");
            EqualsTestObject obj2 = new EqualsTestObject("same");

            objList.add(obj1);
            boolean result = objList.add(obj2); // Should be recognized as duplicate

            assertThat(result).isFalse();
            assertThat(objList).hasSize(1);
        }
    }

    // Helper classes for testing
    private static class TestObject {
        private final String value;

        public TestObject(String value) {
            this.value = value;
        }

        @SuppressWarnings("unused")
        public String getValue() {
            return value;
        }
    }

    private static class EqualsTestObject {
        private final String value;

        public EqualsTestObject(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            EqualsTestObject that = (EqualsTestObject) obj;
            return value != null ? value.equals(that.value) : that.value == null;
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }
    }
}
