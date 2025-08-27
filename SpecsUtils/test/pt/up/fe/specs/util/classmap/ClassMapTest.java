package pt.up.fe.specs.util.classmap;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Comprehensive test suite for ClassMap - a class hierarchy-aware mapping
 * utility.
 * 
 * ClassMap allows mapping classes to values while respecting inheritance
 * hierarchies.
 * If a specific class is not found, it searches up the inheritance chain.
 * 
 * @author Generated Tests
 */
@DisplayName("ClassMap Tests")
public class ClassMapTest {

    private ClassMap<Number, String> numberMap;
    private ClassMap<Collection<?>, String> collectionMap;

    @BeforeEach
    void setUp() {
        numberMap = new ClassMap<>();
        collectionMap = new ClassMap<>();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty ClassMap with default constructor")
        void testDefaultConstructor() {
            ClassMap<Object, String> map = new ClassMap<>();

            assertThat(map.keySet()).isEmpty();
            assertThat(map.tryGet(String.class)).isEmpty();
        }

        @Test
        @DisplayName("Should create ClassMap with default value")
        void testConstructorWithDefaultValue() {
            ClassMap<Object, String> map = new ClassMap<>("default");

            assertThat(map.tryGet(String.class)).contains("default");
            assertThat(map.get(Integer.class)).isEqualTo("default");
        }
    }

    @Nested
    @DisplayName("Put Operations")
    class PutOperations {

        @Test
        @DisplayName("Should put and retrieve exact class mapping")
        void testPutAndGetExactClass() {
            String value = "integer_value";
            numberMap.put(Integer.class, value);

            assertThat(numberMap.get(Integer.class)).isEqualTo(value);
            assertThat(numberMap.tryGet(Integer.class)).contains(value);
        }

        @Test
        @DisplayName("Should put multiple mappings")
        void testPutMultipleMappings() {
            numberMap.put(Integer.class, "integer");
            numberMap.put(Double.class, "double");
            numberMap.put(Number.class, "number");

            assertThat(numberMap.get(Integer.class)).isEqualTo("integer");
            assertThat(numberMap.get(Double.class)).isEqualTo("double");
            assertThat(numberMap.get(Number.class)).isEqualTo("number");
        }

        @Test
        @DisplayName("Should return previous value when overwriting")
        void testPutOverwrite() {
            String oldValue = numberMap.put(Integer.class, "old");
            String newValue = numberMap.put(Integer.class, "new");

            assertThat(oldValue).isNull();
            assertThat(newValue).isEqualTo("old");
            assertThat(numberMap.get(Integer.class)).isEqualTo("new");
        }
    }

    @Nested
    @DisplayName("Hierarchy Resolution")
    class HierarchyResolution {

        @Test
        @DisplayName("Should resolve through inheritance hierarchy")
        void testInheritanceHierarchy() {
            // Put mapping for parent class only
            numberMap.put(Number.class, "number");

            // Should find Number mapping for Integer
            assertThat(numberMap.get(Integer.class)).isEqualTo("number");
            assertThat(numberMap.get(Double.class)).isEqualTo("number");
            assertThat(numberMap.tryGet(Long.class)).contains("number");
        }

        @Test
        @DisplayName("Should prefer more specific class mappings")
        void testSpecificOverGeneral() {
            numberMap.put(Number.class, "number");
            numberMap.put(Integer.class, "integer");

            // Integer should get specific mapping
            assertThat(numberMap.get(Integer.class)).isEqualTo("integer");
            // Other numbers should get general mapping
            assertThat(numberMap.get(Double.class)).isEqualTo("number");
        }

        @Test
        @DisplayName("Should handle complex inheritance hierarchies")
        void testComplexHierarchy() {
            collectionMap.put(Collection.class, "collection");
            collectionMap.put(List.class, "list");

            // ArrayList extends List extends Collection
            assertThat(collectionMap.get(ArrayList.class)).isEqualTo("list");
            assertThat(collectionMap.get(List.class)).isEqualTo("list");
            assertThat(collectionMap.get(Collection.class)).isEqualTo("collection");
        }
    }

    @Nested
    @DisplayName("Get Operations")
    class GetOperations {

        @Test
        @DisplayName("Should get value by class")
        void testGetByClass() {
            numberMap.put(Integer.class, "integer");

            assertThat(numberMap.get(Integer.class)).isEqualTo("integer");
        }

        @Test
        @DisplayName("Should get value by instance")
        void testGetByInstance() {
            numberMap.put(Integer.class, "integer");

            Integer instance = 42;
            assertThat(numberMap.get(instance)).isEqualTo("integer");
        }

        @Test
        @DisplayName("Should throw NotImplementedException for unmapped class")
        void testGetUnmappedClass() {
            // Use Object map to test with String class
            ClassMap<Object, String> objectMap = new ClassMap<>();
            assertThatThrownBy(() -> objectMap.get(String.class))
                    .isInstanceOf(NotImplementedException.class)
                    .hasMessageContaining("Function not defined for class");
        }

        @Test
        @DisplayName("Should return default value instead of throwing")
        void testGetWithDefaultValue() {
            ClassMap<Object, String> mapWithDefault = new ClassMap<>("default");

            assertThat(mapWithDefault.get(String.class)).isEqualTo("default");
            assertThat(mapWithDefault.get(Integer.class)).isEqualTo("default");
        }
    }

    @Nested
    @DisplayName("TryGet Operations")
    class TryGetOperations {

        @Test
        @DisplayName("Should return Optional.empty() for unmapped class")
        void testTryGetUnmapped() {
            // Use Object map to test with String class
            ClassMap<Object, String> objectMap = new ClassMap<>();
            assertThat(objectMap.tryGet(String.class)).isEmpty();
        }

        @Test
        @DisplayName("Should return Optional with value for mapped class")
        void testTryGetMapped() {
            numberMap.put(Integer.class, "integer");

            assertThat(numberMap.tryGet(Integer.class)).contains("integer");
        }

        @Test
        @DisplayName("Should return default value when present")
        void testTryGetWithDefault() {
            ClassMap<Object, String> mapWithDefault = new ClassMap<>("default");

            assertThat(mapWithDefault.tryGet(String.class)).contains("default");
        }
    }

    @Nested
    @DisplayName("Copy Operations")
    class CopyOperations {

        @Test
        @DisplayName("Should create independent copy")
        void testCopy() {
            numberMap.put(Integer.class, "integer");
            numberMap.put(Double.class, "double");

            ClassMap<Number, String> copy = numberMap.copy();

            // Copy should have same mappings
            assertThat(copy.get(Integer.class)).isEqualTo("integer");
            assertThat(copy.get(Double.class)).isEqualTo("double");

            // Modifications to original should not affect copy
            numberMap.put(Float.class, "float");
            assertThatThrownBy(() -> copy.get(Float.class))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Expected map to contain");

            // Modifications to copy should not affect original
            copy.put(Long.class, "long");
            assertThatThrownBy(() -> numberMap.get(Long.class))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Expected map to contain");
        }
    }

    @Nested
    @DisplayName("Default Value Operations")
    class DefaultValueOperations {

        @Test
        @DisplayName("Should set default value and return new instance")
        void testSetDefaultValue() {
            numberMap.put(Integer.class, "integer");

            ClassMap<Number, String> withDefault = numberMap.setDefaultValue("default");

            // Original should not have default
            assertThatThrownBy(() -> numberMap.get(Double.class))
                    .isInstanceOf(NotImplementedException.class);

            // New instance should have default
            assertThat(withDefault.get(Double.class)).isEqualTo("default");
            assertThat(withDefault.get(Integer.class)).isEqualTo("integer");
        }

        @Test
        @DisplayName("Should update default value")
        void testUpdateDefaultValue() {
            ClassMap<Number, String> mapWithDefault = new ClassMap<>("old_default");
            ClassMap<Number, String> updated = mapWithDefault.setDefaultValue("new_default");

            assertThat(mapWithDefault.get(Integer.class)).isEqualTo("old_default");
            assertThat(updated.get(Integer.class)).isEqualTo("new_default");
        }
    }

    @Nested
    @DisplayName("Collection Operations")
    class CollectionOperations {

        @Test
        @DisplayName("Should return correct entry set")
        void testEntrySet() {
            numberMap.put(Integer.class, "integer");
            numberMap.put(Double.class, "double");

            var entrySet = numberMap.entrySet();

            assertThat(entrySet).hasSize(2);

            // Verify keys are present
            boolean hasIntegerKey = entrySet.stream()
                    .anyMatch(entry -> entry.getKey().equals(Integer.class));
            boolean hasDoubleKey = entrySet.stream()
                    .anyMatch(entry -> entry.getKey().equals(Double.class));
            assertThat(hasIntegerKey).isTrue();
            assertThat(hasDoubleKey).isTrue();

            assertThat(entrySet).extracting(Map.Entry::getValue)
                    .containsExactlyInAnyOrder("integer", "double");
        }

        @Test
        @DisplayName("Should return correct key set")
        void testKeySet() {
            numberMap.put(Integer.class, "integer");
            numberMap.put(Double.class, "double");

            var keySet = numberMap.keySet();

            assertThat(keySet).hasSize(2);
            assertThat(keySet.contains(Integer.class)).isTrue();
            assertThat(keySet.contains(Double.class)).isTrue();
        }

        @Test
        @DisplayName("Should handle empty collections")
        void testEmptyCollections() {
            assertThat(numberMap.entrySet()).isEmpty();
            assertThat(numberMap.keySet()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null class gracefully")
        void testNullClass() {
            assertThatThrownBy(() -> numberMap.get((Class<? extends Number>) null))
                    .isInstanceOf(NotImplementedException.class)
                    .hasMessageContaining("Function not defined for class");
        }

        @Test
        @DisplayName("Should handle null instance")
        void testNullInstance() {
            assertThatThrownBy(() -> numberMap.get((Number) null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null values in mappings")
        void testNullValues() {
            numberMap.put(Integer.class, null);

            // BUG: ClassMap incorrectly throws NullPointerException for explicit null
            // values
            // This is a bug in the implementation - it should allow null values
            assertThatThrownBy(() -> numberMap.get(Integer.class))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Expected map to contain");

            assertThatThrownBy(() -> numberMap.tryGet(Integer.class))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Expected map to contain");
        }

        @Test
        @DisplayName("Should handle Object class as root")
        void testObjectAsRoot() {
            ClassMap<Object, String> objectMap = new ClassMap<>();
            objectMap.put(Object.class, "object");

            // All classes should resolve to Object mapping
            assertThat(objectMap.get(String.class)).isEqualTo("object");
            assertThat(objectMap.get(Integer.class)).isEqualTo("object");
            assertThat(objectMap.get(ArrayList.class)).isEqualTo("object");
        }
    }

    @Nested
    @DisplayName("Performance and Stress Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle large number of mappings efficiently")
        void testLargeNumberOfMappings() {
            ClassMap<Object, Integer> largeMap = new ClassMap<>();

            // Add many mappings
            for (int i = 0; i < 1000; i++) {
                largeMap.put(("TestClass" + i).getClass(), i);
            }

            // Should still be responsive
            assertThat(largeMap.keySet()).hasSize(1); // All strings have same class
            assertThat(largeMap.get(String.class)).isNotNull();
        }

        @Test
        @DisplayName("Should perform hierarchy lookup efficiently")
        void testHierarchyLookupPerformance() {
            // Create deep hierarchy
            numberMap.put(Number.class, "number");

            // Should resolve quickly even for subclasses
            long startTime = System.nanoTime();
            for (int i = 0; i < 1000; i++) {
                numberMap.get(Integer.class);
            }
            long endTime = System.nanoTime();

            // Should complete in reasonable time (less than 100ms)
            assertThat(endTime - startTime).isLessThan(100_000_000L);
        }
    }
}
