package pt.up.fe.specs.util.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for CycleList cycling iteration utility.
 * Tests cycling through elements in a circular manner.
 * 
 * @author Generated Tests
 */
@DisplayName("CycleList Tests")
class CycleListTest {

    @Nested
    @DisplayName("Constructor and Factory Methods")
    class ConstructorAndFactoryMethods {

        @Test
        @DisplayName("Should create CycleList from Collection")
        void testConstructorFromCollection() {
            List<String> source = Arrays.asList("A", "B", "C");
            CycleList<String> cycleList = new CycleList<>(source);

            assertThat(cycleList).isNotNull();
            assertThat(cycleList.toString()).contains("A", "B", "C");
            assertThat(cycleList.toString()).startsWith("0@");
        }

        @Test
        @DisplayName("Should create CycleList from another CycleList")
        void testCopyConstructor() {
            List<String> source = Arrays.asList("X", "Y", "Z");
            CycleList<String> original = new CycleList<>(source);

            // Advance original to different position
            original.next(); // X
            original.next(); // Y

            CycleList<String> copy = new CycleList<>(original);

            // Copy should start from beginning, not from original's current position
            assertThat(copy.next()).isEqualTo("X");
            assertThat(copy.next()).isEqualTo("Y");

            // Original should continue from where it was
            assertThat(original.next()).isEqualTo("Z");
        }

        @Test
        @DisplayName("Should create CycleList from empty collection")
        void testConstructorFromEmpty() {
            CycleList<String> cycleList = new CycleList<>(Collections.emptyList());

            assertThat(cycleList).isNotNull();
            assertThat(cycleList.toString()).contains("[]");
        }

        @Test
        @DisplayName("Should create CycleList from single element")
        void testConstructorFromSingleElement() {
            CycleList<String> cycleList = new CycleList<>(Arrays.asList("ONLY"));

            assertThat(cycleList).isNotNull();
            assertThat(cycleList.toString()).contains("ONLY");
        }
    }

    @Nested
    @DisplayName("Basic Cycling Operations")
    class BasicCyclingOperations {

        @Test
        @DisplayName("Should cycle through elements in order")
        void testBasicCycling() {
            CycleList<String> cycleList = new CycleList<>(Arrays.asList("A", "B", "C"));

            // First cycle
            assertThat(cycleList.next()).isEqualTo("A");
            assertThat(cycleList.next()).isEqualTo("B");
            assertThat(cycleList.next()).isEqualTo("C");

            // Second cycle (should start over)
            assertThat(cycleList.next()).isEqualTo("A");
            assertThat(cycleList.next()).isEqualTo("B");
            assertThat(cycleList.next()).isEqualTo("C");
        }

        @Test
        @DisplayName("Should handle single element cycling")
        void testSingleElementCycling() {
            CycleList<String> cycleList = new CycleList<>(Arrays.asList("SINGLE"));

            // Should always return the same element
            assertThat(cycleList.next()).isEqualTo("SINGLE");
            assertThat(cycleList.next()).isEqualTo("SINGLE");
            assertThat(cycleList.next()).isEqualTo("SINGLE");
            assertThat(cycleList.next()).isEqualTo("SINGLE");
        }

        @Test
        @DisplayName("Should handle two element cycling")
        void testTwoElementCycling() {
            CycleList<String> cycleList = new CycleList<>(Arrays.asList("FIRST", "SECOND"));

            assertThat(cycleList.next()).isEqualTo("FIRST");
            assertThat(cycleList.next()).isEqualTo("SECOND");
            assertThat(cycleList.next()).isEqualTo("FIRST");
            assertThat(cycleList.next()).isEqualTo("SECOND");
            assertThat(cycleList.next()).isEqualTo("FIRST");
        }

        @Test
        @DisplayName("Should maintain internal state between calls")
        void testInternalStateTracking() {
            CycleList<Integer> cycleList = new CycleList<>(Arrays.asList(10, 20, 30));

            // Initial state
            assertThat(cycleList.toString()).startsWith("0@");

            // After first next()
            Integer first = cycleList.next();
            assertThat(first).isEqualTo(10);
            assertThat(cycleList.toString()).startsWith("1@");

            // After second next()
            Integer second = cycleList.next();
            assertThat(second).isEqualTo(20);
            assertThat(cycleList.toString()).startsWith("2@");

            // After third next()
            Integer third = cycleList.next();
            assertThat(third).isEqualTo(30);
            assertThat(cycleList.toString()).startsWith("0@"); // Should wrap around
        }
    }

    @Nested
    @DisplayName("Extended Cycling Scenarios")
    class ExtendedCyclingScenarios {

        @Test
        @DisplayName("Should handle many cycles correctly")
        void testManyCycles() {
            CycleList<String> cycleList = new CycleList<>(Arrays.asList("A", "B", "C"));
            List<String> results = new ArrayList<>();

            // Get 15 elements (5 complete cycles)
            for (int i = 0; i < 15; i++) {
                results.add(cycleList.next());
            }

            List<String> expected = Arrays.asList(
                    "A", "B", "C", // Cycle 1
                    "A", "B", "C", // Cycle 2
                    "A", "B", "C", // Cycle 3
                    "A", "B", "C", // Cycle 4
                    "A", "B", "C" // Cycle 5
            );

            assertThat(results).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle partial cycles")
        void testPartialCycles() {
            CycleList<Integer> cycleList = new CycleList<>(Arrays.asList(1, 2, 3, 4, 5));

            // Take 7 elements (1 full cycle + 2 elements)
            List<Integer> results = new ArrayList<>();
            for (int i = 0; i < 7; i++) {
                results.add(cycleList.next());
            }

            List<Integer> expected = Arrays.asList(1, 2, 3, 4, 5, 1, 2);
            assertThat(results).isEqualTo(expected);
        }

        @Test
        @DisplayName("Should handle large number of elements")
        void testLargeNumberOfElements() {
            List<Integer> source = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                source.add(i);
            }

            CycleList<Integer> cycleList = new CycleList<>(source);

            // Test first cycle
            for (int i = 0; i < 1000; i++) {
                assertThat(cycleList.next()).isEqualTo(i);
            }

            // Test beginning of second cycle
            assertThat(cycleList.next()).isEqualTo(0);
            assertThat(cycleList.next()).isEqualTo(1);
            assertThat(cycleList.next()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should handle cycling with different data types")
        void testDifferentDataTypes() {
            // Test with booleans
            CycleList<Boolean> boolCycle = new CycleList<>(Arrays.asList(true, false));
            assertThat(boolCycle.next()).isTrue();
            assertThat(boolCycle.next()).isFalse();
            assertThat(boolCycle.next()).isTrue();

            // Test with enums
            enum Day {
                MONDAY, TUESDAY, WEDNESDAY
            }
            CycleList<Day> dayCycle = new CycleList<>(Arrays.asList(Day.MONDAY, Day.TUESDAY, Day.WEDNESDAY));
            assertThat(dayCycle.next()).isEqualTo(Day.MONDAY);
            assertThat(dayCycle.next()).isEqualTo(Day.TUESDAY);
            assertThat(dayCycle.next()).isEqualTo(Day.WEDNESDAY);
            assertThat(dayCycle.next()).isEqualTo(Day.MONDAY);
        }
    }

    @Nested
    @DisplayName("Collection Integration")
    class CollectionIntegration {

        @Test
        @DisplayName("Should work with different collection types")
        void testDifferentCollectionTypes() {
            // Test with LinkedList
            LinkedList<String> linkedList = new LinkedList<>(Arrays.asList("L1", "L2", "L3"));
            CycleList<String> fromLinkedList = new CycleList<>(linkedList);
            assertThat(fromLinkedList.next()).isEqualTo("L1");

            // Test with HashSet (order may vary)
            Set<String> hashSet = new HashSet<>(Arrays.asList("S1", "S2", "S3"));
            CycleList<String> fromHashSet = new CycleList<>(hashSet);
            String first = fromHashSet.next();
            assertThat(hashSet).contains(first);

            // Test with TreeSet (maintains order)
            TreeSet<String> treeSet = new TreeSet<>(Arrays.asList("C", "A", "B"));
            CycleList<String> fromTreeSet = new CycleList<>(treeSet);
            assertThat(fromTreeSet.next()).isEqualTo("A"); // TreeSet sorts elements
            assertThat(fromTreeSet.next()).isEqualTo("B");
            assertThat(fromTreeSet.next()).isEqualTo("C");
        }

        @Test
        @DisplayName("Should preserve element order from original collection")
        void testElementOrderPreservation() {
            List<String> original = Arrays.asList("FIRST", "SECOND", "THIRD", "FOURTH");
            CycleList<String> cycleList = new CycleList<>(original);

            // First cycle should match original order
            for (String expected : original) {
                assertThat(cycleList.next()).isEqualTo(expected);
            }

            // Second cycle should also match original order
            for (String expected : original) {
                assertThat(cycleList.next()).isEqualTo(expected);
            }
        }

        @Test
        @DisplayName("Should handle collections with duplicate elements")
        void testDuplicateElements() {
            CycleList<String> cycleList = new CycleList<>(Arrays.asList("A", "B", "A", "C", "B"));

            // Should cycle through all elements, including duplicates
            assertThat(cycleList.next()).isEqualTo("A");
            assertThat(cycleList.next()).isEqualTo("B");
            assertThat(cycleList.next()).isEqualTo("A");
            assertThat(cycleList.next()).isEqualTo("C");
            assertThat(cycleList.next()).isEqualTo("B");

            // Start new cycle
            assertThat(cycleList.next()).isEqualTo("A");
            assertThat(cycleList.next()).isEqualTo("B");
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentation {

        @Test
        @DisplayName("Should provide meaningful string representation")
        void testToString() {
            CycleList<String> cycleList = new CycleList<>(Arrays.asList("X", "Y", "Z"));

            // Initial state
            String initial = cycleList.toString();
            assertThat(initial).contains("0@");
            assertThat(initial).contains("X", "Y", "Z");

            // After one next()
            cycleList.next();
            String afterOne = cycleList.toString();
            assertThat(afterOne).contains("1@");
            assertThat(afterOne).contains("X", "Y", "Z");

            // After cycling back to start
            cycleList.next(); // Y
            cycleList.next(); // Z
            String cycled = cycleList.toString();
            assertThat(cycled).contains("0@");
            assertThat(cycled).contains("X", "Y", "Z");
        }

        @Test
        @DisplayName("Should handle toString with different element types")
        void testToStringDifferentTypes() {
            CycleList<Integer> intCycle = new CycleList<>(Arrays.asList(1, 2, 3));
            assertThat(intCycle.toString()).contains("0@");
            assertThat(intCycle.toString()).contains("1", "2", "3");

            CycleList<Boolean> boolCycle = new CycleList<>(Arrays.asList(true, false));
            assertThat(boolCycle.toString()).contains("0@");
            assertThat(boolCycle.toString()).contains("true", "false");
        }

        @Test
        @DisplayName("Should handle toString with empty list")
        void testToStringEmpty() {
            CycleList<String> emptyCycle = new CycleList<>(Collections.emptyList());

            String result = emptyCycle.toString();
            assertThat(result).contains("0@");
            assertThat(result).contains("[]");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should throw exception when calling next() on empty list")
        void testNextOnEmptyList() {
            CycleList<String> emptyCycle = new CycleList<>(Collections.emptyList());

            assertThatThrownBy(() -> emptyCycle.next())
                    .isInstanceOf(IndexOutOfBoundsException.class);
        }

        @Test
        @DisplayName("Should handle null elements in collection")
        void testNullElements() {
            CycleList<String> cycleWithNulls = new CycleList<>(Arrays.asList("A", null, "B", null));

            assertThat(cycleWithNulls.next()).isEqualTo("A");
            assertThat(cycleWithNulls.next()).isNull();
            assertThat(cycleWithNulls.next()).isEqualTo("B");
            assertThat(cycleWithNulls.next()).isNull();

            // Start new cycle
            assertThat(cycleWithNulls.next()).isEqualTo("A");
        }

        @Test
        @DisplayName("Should handle all null elements")
        void testAllNullElements() {
            CycleList<String> allNulls = new CycleList<>(Arrays.asList(null, null, null));

            assertThat(allNulls.next()).isNull();
            assertThat(allNulls.next()).isNull();
            assertThat(allNulls.next()).isNull();

            // Start new cycle
            assertThat(allNulls.next()).isNull();
        }

        @Test
        @DisplayName("Should handle special string values")
        void testSpecialStringValues() {
            CycleList<String> specialStrings = new CycleList<>(Arrays.asList("", " ", "\n", "\t", "normal"));

            assertThat(specialStrings.next()).isEqualTo("");
            assertThat(specialStrings.next()).isEqualTo(" ");
            assertThat(specialStrings.next()).isEqualTo("\n");
            assertThat(specialStrings.next()).isEqualTo("\t");
            assertThat(specialStrings.next()).isEqualTo("normal");

            // Cycle again
            assertThat(specialStrings.next()).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Type Safety and Generics")
    class TypeSafetyAndGenerics {

        @Test
        @DisplayName("Should work with complex object types")
        void testComplexObjectTypes() {
            record Person(String name, int age) {
            }

            Person alice = new Person("Alice", 30);
            Person bob = new Person("Bob", 25);
            Person charlie = new Person("Charlie", 35);

            CycleList<Person> personCycle = new CycleList<>(Arrays.asList(alice, bob, charlie));

            assertThat(personCycle.next()).isEqualTo(alice);
            assertThat(personCycle.next()).isEqualTo(bob);
            assertThat(personCycle.next()).isEqualTo(charlie);
            assertThat(personCycle.next()).isEqualTo(alice); // Cycle back
        }

        @Test
        @DisplayName("Should work with nested collections")
        void testNestedCollections() {
            List<String> list1 = Arrays.asList("A", "B");
            List<String> list2 = Arrays.asList("C", "D");
            List<String> list3 = Arrays.asList("E", "F");

            CycleList<List<String>> listCycle = new CycleList<>(Arrays.asList(list1, list2, list3));

            assertThat(listCycle.next()).isEqualTo(list1);
            assertThat(listCycle.next()).isEqualTo(list2);
            assertThat(listCycle.next()).isEqualTo(list3);
            assertThat(listCycle.next()).isEqualTo(list1); // Cycle back
        }

        @Test
        @DisplayName("Should maintain type safety")
        void testTypeSafety() {
            CycleList<Integer> intCycle = new CycleList<>(Arrays.asList(1, 2, 3));
            CycleList<String> stringCycle = new CycleList<>(Arrays.asList("A", "B", "C"));

            Integer intResult = intCycle.next();
            String stringResult = stringCycle.next();

            assertThat(intResult).isInstanceOf(Integer.class);
            assertThat(stringResult).isInstanceOf(String.class);

            assertThat(intResult).isEqualTo(1);
            assertThat(stringResult).isEqualTo("A");
        }
    }

    @Nested
    @DisplayName("Performance and Memory")
    class PerformanceAndMemory {

        @Test
        @DisplayName("Should handle high-frequency cycling efficiently")
        void testHighFrequencyCycling() {
            CycleList<Integer> cycleList = new CycleList<>(Arrays.asList(1, 2, 3, 4, 5));

            // Perform many cycles
            int expectedCycles = 10000;
            int totalElements = expectedCycles * 5;

            List<Integer> results = new ArrayList<>();
            for (int i = 0; i < totalElements; i++) {
                results.add(cycleList.next());
            }

            // Verify pattern is maintained
            assertThat(results.get(0)).isEqualTo(1);
            assertThat(results.get(4)).isEqualTo(5);
            assertThat(results.get(5)).isEqualTo(1); // Start of second cycle
            assertThat(results.get(results.size() - 1)).isEqualTo(5); // Last element
        }

        @Test
        @DisplayName("Should not modify original collection")
        void testOriginalCollectionImmutability() {
            List<String> original = new ArrayList<>(Arrays.asList("A", "B", "C"));
            CycleList<String> cycleList = new CycleList<>(original);

            // Cycle through elements
            cycleList.next();
            cycleList.next();
            cycleList.next();
            cycleList.next(); // Start new cycle

            // Original collection should be unchanged
            assertThat(original).containsExactly("A", "B", "C");

            // Modifying original should not affect CycleList
            original.add("D");
            assertThat(cycleList.next()).isEqualTo("B"); // Should still follow original pattern
        }
    }

    @Nested
    @DisplayName("Integration and Workflow Tests")
    class IntegrationAndWorkflowTests {

        @Test
        @DisplayName("Should support round-robin task distribution")
        void testRoundRobinDistribution() {
            CycleList<String> workers = new CycleList<>(Arrays.asList("Worker1", "Worker2", "Worker3"));

            List<String> taskAssignments = new ArrayList<>();

            // Assign 10 tasks
            for (int i = 0; i < 10; i++) {
                String assignedWorker = workers.next();
                taskAssignments.add("Task" + i + " -> " + assignedWorker);
            }

            // Verify round-robin distribution
            assertThat(taskAssignments.get(0)).isEqualTo("Task0 -> Worker1");
            assertThat(taskAssignments.get(1)).isEqualTo("Task1 -> Worker2");
            assertThat(taskAssignments.get(2)).isEqualTo("Task2 -> Worker3");
            assertThat(taskAssignments.get(3)).isEqualTo("Task3 -> Worker1"); // Cycle back
            assertThat(taskAssignments.get(9)).isEqualTo("Task9 -> Worker1");
        }

        @Test
        @DisplayName("Should support color cycling for UI elements")
        void testColorCycling() {
            CycleList<String> colors = new CycleList<>(Arrays.asList("#FF0000", "#00FF00", "#0000FF"));

            List<String> elementColors = new ArrayList<>();

            // Assign colors to 8 UI elements
            for (int i = 0; i < 8; i++) {
                elementColors.add(colors.next());
            }

            // Verify color cycling
            assertThat(elementColors.get(0)).isEqualTo("#FF0000"); // Red
            assertThat(elementColors.get(1)).isEqualTo("#00FF00"); // Green
            assertThat(elementColors.get(2)).isEqualTo("#0000FF"); // Blue
            assertThat(elementColors.get(3)).isEqualTo("#FF0000"); // Red again
            assertThat(elementColors.get(6)).isEqualTo("#FF0000"); // Red
            assertThat(elementColors.get(7)).isEqualTo("#00FF00"); // Green
        }

        @Test
        @DisplayName("Should support pattern generation")
        void testPatternGeneration() {
            CycleList<Character> pattern = new CycleList<>(Arrays.asList('*', '-', '*', '-', '='));

            StringBuilder patternString = new StringBuilder();
            for (int i = 0; i < 25; i++) {
                patternString.append(pattern.next());
            }

            String result = patternString.toString();
            assertThat(result).hasSize(25);
            assertThat(result).startsWith("*-*-=*-*-=*-*-=*-*-=*-*-=");
        }
    }
}
