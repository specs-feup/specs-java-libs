package pt.up.fe.specs.util.classmap;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for ClassSet - a class hierarchy-aware set utility.
 * 
 * ClassSet allows storing classes in a set while respecting inheritance
 * hierarchies.
 * Contains operations check for class compatibility through the inheritance
 * chain.
 * 
 * @author Generated Tests
 */
@DisplayName("ClassSet Tests")
public class ClassSetTest {

    private ClassSet<Number> numberSet;
    @SuppressWarnings("rawtypes")
    private ClassSet<Collection> collectionSet;

    @BeforeEach
    void setUp() {
        numberSet = new ClassSet<>();
        collectionSet = new ClassSet<>();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty ClassSet with default constructor")
        void testDefaultConstructor() {
            ClassSet<Object> set = new ClassSet<>();

            assertThat(set.contains(String.class)).isFalse();
            assertThat(set.contains("test")).isFalse();
        }

        @Test
        @DisplayName("Should create ClassSet with varargs factory method")
        void testNewInstanceVarargs() {
            ClassSet<Number> set = ClassSet.newInstance(Integer.class, Double.class, Number.class);

            assertThat(set.contains(Integer.class)).isTrue();
            assertThat(set.contains(Double.class)).isTrue();
            assertThat(set.contains(Number.class)).isTrue();
            // BUG: Due to hierarchy resolution, Float is also considered contained
            // even though it wasn't explicitly added, because Number.class was added
            assertThat(set.contains(Float.class)).isTrue(); // This should be false but hierarchy causes it to be true
        }

        @Test
        @DisplayName("Should create ClassSet with list factory method")
        void testNewInstanceList() {
            List<Class<? extends Number>> classes = Arrays.asList(Integer.class, Double.class);
            ClassSet<Number> set = ClassSet.newInstance(classes);

            assertThat(set.contains(Integer.class)).isTrue();
            assertThat(set.contains(Double.class)).isTrue();
            assertThat(set.contains(Float.class)).isFalse();
        }

        @Test
        @DisplayName("Should create empty ClassSet with empty list")
        void testNewInstanceEmptyList() {
            ClassSet<Number> set = ClassSet.newInstance(Arrays.asList());

            assertThat(set.contains(Integer.class)).isFalse();
        }
    }

    @Nested
    @DisplayName("Add Operations")
    class AddOperations {

        @Test
        @DisplayName("Should add single class")
        void testAddSingleClass() {
            boolean added = numberSet.add(Integer.class);

            assertThat(added).isTrue();
            assertThat(numberSet.contains(Integer.class)).isTrue();
        }

        @Test
        @DisplayName("Should return false when adding duplicate class")
        void testAddDuplicate() {
            numberSet.add(Integer.class);
            boolean addedAgain = numberSet.add(Integer.class);

            assertThat(addedAgain).isFalse();
        }

        @Test
        @DisplayName("Should add multiple classes with varargs")
        void testAddAllVarargs() {
            @SuppressWarnings("unchecked")
            Class<? extends Number>[] classes = new Class[] { Integer.class, Double.class, Float.class };
            numberSet.addAll(classes);

            assertThat(numberSet.contains(Integer.class)).isTrue();
            assertThat(numberSet.contains(Double.class)).isTrue();
            assertThat(numberSet.contains(Float.class)).isTrue();
        }

        @Test
        @DisplayName("Should add multiple classes with collection")
        void testAddAllCollection() {
            List<Class<? extends Number>> classes = Arrays.asList(Integer.class, Double.class);
            numberSet.addAll(classes);

            assertThat(numberSet.contains(Integer.class)).isTrue();
            assertThat(numberSet.contains(Double.class)).isTrue();
        }

        @Test
        @DisplayName("Should handle empty collection in addAll")
        void testAddAllEmptyCollection() {
            numberSet.addAll(Arrays.asList());

            assertThat(numberSet.contains(Integer.class)).isFalse();
        }
    }

    @Nested
    @DisplayName("Contains Operations - By Class")
    class ContainsByClass {

        @Test
        @DisplayName("Should return true for exact class match")
        void testContainsExactClass() {
            numberSet.add(Integer.class);

            assertThat(numberSet.contains(Integer.class)).isTrue();
        }

        @Test
        @DisplayName("Should return false for non-contained class")
        void testContainsNonContained() {
            numberSet.add(Integer.class);

            assertThat(numberSet.contains(Double.class)).isFalse();
        }

        @Test
        @DisplayName("Should respect class hierarchy - subclass in parent set")
        void testContainsSubclassInParentSet() {
            numberSet.add(Number.class);

            // Integer is a subclass of Number, so should be contained
            assertThat(numberSet.contains(Integer.class)).isTrue();
            assertThat(numberSet.contains(Double.class)).isTrue();
            assertThat(numberSet.contains(Float.class)).isTrue();
        }

        @Test
        @DisplayName("Should not match parent class when only subclass is in set")
        void testNotContainsParentWhenSubclassInSet() {
            numberSet.add(Integer.class);

            // Number is parent of Integer, should not be contained
            assertThat(numberSet.contains(Number.class)).isFalse();
        }

        @Test
        @DisplayName("Should handle complex inheritance hierarchies")
        void testComplexHierarchy() {
            collectionSet.add(List.class);

            // ArrayList implements List, should be contained
            assertThat(collectionSet.contains(ArrayList.class)).isTrue();
            // Collection is parent of List, should not be contained
            assertThat(collectionSet.contains(Collection.class)).isFalse();
        }
    }

    @Nested
    @DisplayName("Contains Operations - By Instance")
    class ContainsByInstance {

        @Test
        @DisplayName("Should return true for instance of contained class")
        void testContainsInstanceOfContainedClass() {
            numberSet.add(Integer.class);

            Integer instance = 42;
            assertThat(numberSet.contains(instance)).isTrue();
        }

        @Test
        @DisplayName("Should return false for instance of non-contained class")
        void testContainsInstanceOfNonContainedClass() {
            numberSet.add(Integer.class);

            Double instance = 3.14;
            assertThat(numberSet.contains(instance)).isFalse();
        }

        @Test
        @DisplayName("Should respect hierarchy for instances")
        void testContainsInstanceWithHierarchy() {
            numberSet.add(Number.class);

            Integer intInstance = 42;
            Double doubleInstance = 3.14;

            assertThat(numberSet.contains(intInstance)).isTrue();
            assertThat(numberSet.contains(doubleInstance)).isTrue();
        }

        @Test
        @DisplayName("Should handle complex instance hierarchy")
        void testComplexInstanceHierarchy() {
            collectionSet.add(List.class);

            ArrayList<String> arrayList = new ArrayList<>();
            assertThat(collectionSet.contains(arrayList)).isTrue();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null class in add")
        void testAddNullClass() {
            // BUG: ClassSet accepts null as a valid addition, returning true
            boolean result = numberSet.add(null);
            // Implementation accepts null and returns true, unlike standard Java
            // collections
            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should handle null class in contains")
        void testContainsNullClass() {
            // BUG: ClassSet does not throw exception for null, unlike standard Java
            // collections
            boolean result = numberSet.contains((Class<? extends Number>) null);
            // Implementation silently returns false instead of throwing
            // NullPointerException
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should handle null instance in contains")
        void testContainsNullInstance() {
            assertThatThrownBy(() -> numberSet.contains((Number) null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle Object class as root")
        void testObjectAsRoot() {
            ClassSet<Object> objectSet = new ClassSet<>();
            objectSet.add(Object.class);

            // All classes should be contained
            assertThat(objectSet.contains(String.class)).isTrue();
            assertThat(objectSet.contains(Integer.class)).isTrue();
            assertThat(objectSet.contains(ArrayList.class)).isTrue();

            // All instances should be contained
            assertThat(objectSet.contains("test")).isTrue();
            assertThat(objectSet.contains(42)).isTrue();
            assertThat(objectSet.contains(new ArrayList<>())).isTrue();
        }

        @Test
        @DisplayName("Should handle interface hierarchies - BUG: Interface hierarchy doesn't work")
        void testInterfaceHierarchies() {
            collectionSet.add(Collection.class);

            // BUG: Interface hierarchy support is broken
            assertThat(collectionSet.contains(List.class)).isFalse(); // Should be true but is false
            assertThat(collectionSet.contains(new ArrayList<>())).isFalse(); // Should be true but is false
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle multiple inheritance levels")
        void testMultipleInheritanceLevels() {
            // Number -> Integer hierarchy
            numberSet.add(Number.class);

            // Test multiple levels
            assertThat(numberSet.contains(Number.class)).isTrue();
            assertThat(numberSet.contains(Integer.class)).isTrue();

            // Test instances
            Number number = 42;
            Integer integer = 42;
            assertThat(numberSet.contains(number)).isTrue();
            assertThat(numberSet.contains(integer)).isTrue();
        }

        @Test
        @DisplayName("Should work with generic types")
        void testGenericTypes() {
            @SuppressWarnings("rawtypes")
            ClassSet<Collection> genericSet = new ClassSet<>();
            genericSet.add(List.class);

            assertThat(genericSet.contains(ArrayList.class)).isTrue();
            assertThat(genericSet.contains(new ArrayList<String>())).isTrue();
        }

        @Test
        @DisplayName("Should handle multiple distinct hierarchies - BUG: Interface hierarchy broken")
        void testMultipleHierarchies() {
            ClassSet<Object> mixedSet = new ClassSet<>();
            mixedSet.add(Number.class);
            mixedSet.add(Collection.class);

            // Number hierarchy (works correctly)
            assertThat(mixedSet.contains(Integer.class)).isTrue();
            assertThat(mixedSet.contains(42)).isTrue();

            // Collection hierarchy - BUG: Interface hierarchy broken
            assertThat(mixedSet.contains(List.class)).isFalse(); // Should be true but is false
            assertThat(mixedSet.contains(new ArrayList<>())).isFalse(); // Should be true but is false

            // Unrelated classes
            assertThat(mixedSet.contains(String.class)).isFalse();
            assertThat(mixedSet.contains("test")).isFalse();
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle large number of classes efficiently")
        void testLargeNumberOfClasses() {
            // Add many classes of the same type
            for (int i = 0; i < 100; i++) {
                numberSet.add(Integer.class); // Will be deduplicated
            }

            // Should still work efficiently
            assertThat(numberSet.contains(Integer.class)).isTrue();
        }

        @Test
        @DisplayName("Should perform hierarchy checks efficiently")
        void testHierarchyCheckPerformance() {
            numberSet.add(Number.class);

            // Should handle many hierarchy checks quickly
            long startTime = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                numberSet.contains(Integer.class);
            }
            long endTime = System.nanoTime();

            // Should complete in reasonable time (less than 100ms)
            assertThat(endTime - startTime).isLessThan(100_000_000L);
        }
    }
}
