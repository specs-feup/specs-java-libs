package pt.up.fe.specs.util.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for MultiMap collection utility.
 * Tests multiple values per key mapping functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("MultiMap Tests")
class MultiMapTest {

    @Nested
    @DisplayName("Constructor and Factory Methods")
    class ConstructorAndFactoryMethods {

        @Test
        @DisplayName("Should create empty MultiMap with default constructor")
        void testDefaultConstructor() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            assertThat(multiMap).isNotNull();
            assertThat(multiMap.size()).isZero();
            assertThat(multiMap.keySet()).isEmpty();
        }

        @Test
        @DisplayName("Should create MultiMap from another MultiMap")
        void testCopyConstructor() {
            MultiMap<String, Integer> original = new MultiMap<>();
            original.put("key1", 1);
            original.put("key1", 2);
            original.put("key2", 3);

            MultiMap<String, Integer> copy = new MultiMap<>(original);

            assertThat(copy).isNotNull();
            assertThat(copy.size()).isEqualTo(2);
            assertThat(copy.get("key1")).containsExactly(1, 2);
            assertThat(copy.get("key2")).containsExactly(3);

            // The copy constructor does a shallow copy, so lists are shared
            original.put("key1", 4);
            assertThat(copy.get("key1")).containsExactly(1, 2, 4); // Should be affected
        }

        @Test
        @DisplayName("Should create MultiMap with custom map provider")
        void testCustomMapProvider() {
            MultiMap<String, Integer> multiMap = new MultiMap<>(() -> new ConcurrentHashMap<>());

            assertThat(multiMap).isNotNull();
            assertThat(multiMap.size()).isZero();

            // Verify it works with the custom map
            multiMap.put("test", 1);
            assertThat(multiMap.get("test")).containsExactly(1);
        }

        @Test
        @DisplayName("Should create new instance using static factory method")
        void testStaticFactoryMethod() {
            MultiMap<String, Integer> multiMap = MultiMap.newInstance();

            assertThat(multiMap).isNotNull();
            assertThat(multiMap.size()).isZero();
        }
    }

    @Nested
    @DisplayName("Basic Operations")
    class BasicOperations {

        @Test
        @DisplayName("Should add single value to key")
        void testPutSingleValue() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            multiMap.put("key1", 1);

            assertThat(multiMap.get("key1")).containsExactly(1);
            assertThat(multiMap.size()).isEqualTo(1);
            assertThat(multiMap.containsKey("key1")).isTrue();
        }

        @Test
        @DisplayName("Should add multiple values to same key")
        void testPutMultipleValues() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            multiMap.put("key1", 1);
            multiMap.put("key1", 2);
            multiMap.put("key1", 3);

            assertThat(multiMap.get("key1")).containsExactly(1, 2, 3);
            assertThat(multiMap.size()).isEqualTo(1); // Still one key
        }

        @Test
        @DisplayName("Should add values to different keys")
        void testPutDifferentKeys() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            multiMap.put("key1", 1);
            multiMap.put("key2", 2);
            multiMap.put("key3", 3);

            assertThat(multiMap.get("key1")).containsExactly(1);
            assertThat(multiMap.get("key2")).containsExactly(2);
            assertThat(multiMap.get("key3")).containsExactly(3);
            assertThat(multiMap.size()).isEqualTo(3);
        }

        @Test
        @DisplayName("Should return empty list for non-existent key")
        void testGetNonExistentKey() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            List<Integer> result = multiMap.get("nonexistent");

            assertThat(result).isNotNull();
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should use add method as alias for put")
        void testAddMethod() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            multiMap.add("key1", 1);
            multiMap.add("key1", 2);

            assertThat(multiMap.get("key1")).containsExactly(1, 2);
        }
    }

    @Nested
    @DisplayName("Bulk Operations")
    class BulkOperations {

        @Test
        @DisplayName("Should replace all values for key with put(key, list)")
        void testPutList() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", 1);
            multiMap.put("key1", 2);

            List<Integer> newValues = Arrays.asList(10, 20, 30);
            multiMap.put("key1", newValues);

            assertThat(multiMap.get("key1")).containsExactly(10, 20, 30);
        }

        @Test
        @DisplayName("Should create copy of provided list in put(key, list)")
        void testPutListCreatesDeepCopy() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            List<Integer> originalList = new ArrayList<>(Arrays.asList(1, 2, 3));

            multiMap.put("key1", originalList);
            originalList.add(4); // Modify original

            assertThat(multiMap.get("key1")).containsExactly(1, 2, 3); // Should not be affected
        }

        @Test
        @DisplayName("Should add all values to existing key")
        void testAddAll() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", 1);
            multiMap.put("key1", 2);

            List<Integer> additionalValues = Arrays.asList(3, 4, 5);
            multiMap.addAll("key1", additionalValues);

            assertThat(multiMap.get("key1")).containsExactly(1, 2, 3, 4, 5);
        }

        @Test
        @DisplayName("Should handle addAll with empty list")
        void testAddAllEmptyList() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", 1);

            multiMap.addAll("key1", Collections.emptyList());

            assertThat(multiMap.get("key1")).containsExactly(1);
        }

        @Test
        @DisplayName("Should create new key when addAll to non-existent key")
        void testAddAllNewKey() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            List<Integer> values = Arrays.asList(1, 2, 3);
            multiMap.addAll("newkey", values);

            assertThat(multiMap.get("newkey")).containsExactly(1, 2, 3);
            assertThat(multiMap.size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Removal Operations")
    class RemovalOperations {

        @Test
        @DisplayName("Should remove key and return its values")
        void testRemoveKey() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", 1);
            multiMap.put("key1", 2);
            multiMap.put("key2", 3);

            List<Integer> removed = multiMap.remove("key1");

            assertThat(removed).containsExactly(1, 2);
            assertThat(multiMap.containsKey("key1")).isFalse();
            assertThat(multiMap.size()).isEqualTo(1);
            assertThat(multiMap.get("key1")).isEmpty(); // Should return empty list
        }

        @Test
        @DisplayName("Should return null when removing non-existent key")
        void testRemoveNonExistentKey() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            List<Integer> removed = multiMap.remove("nonexistent");

            assertThat(removed).isNull();
        }

        @Test
        @DisplayName("Should clear all keys and values")
        void testClear() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", 1);
            multiMap.put("key2", 2);
            multiMap.put("key3", 3);

            multiMap.clear();

            assertThat(multiMap.size()).isZero();
            assertThat(multiMap.keySet()).isEmpty();
            assertThat(multiMap.get("key1")).isEmpty();
        }
    }

    @Nested
    @DisplayName("Query Operations")
    class QueryOperations {

        @Test
        @DisplayName("Should check if key exists")
        void testContainsKey() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("existing", 1);

            assertThat(multiMap.containsKey("existing")).isTrue();
            assertThat(multiMap.containsKey("nonexistent")).isFalse();
        }

        @Test
        @DisplayName("Should return correct size")
        void testSize() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            assertThat(multiMap.size()).isZero();

            multiMap.put("key1", 1);
            assertThat(multiMap.size()).isEqualTo(1);

            multiMap.put("key1", 2); // Same key, size should not increase
            assertThat(multiMap.size()).isEqualTo(1);

            multiMap.put("key2", 3); // Different key
            assertThat(multiMap.size()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should return key set")
        void testKeySet() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", 1);
            multiMap.put("key2", 2);
            multiMap.put("key1", 3); // Duplicate key

            Set<String> keySet = multiMap.keySet();

            assertThat(keySet).containsExactlyInAnyOrder("key1", "key2");
        }

        @Test
        @DisplayName("Should return values collection")
        void testValues() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", Arrays.asList(1, 2));
            multiMap.put("key2", Arrays.asList(3, 4));

            Collection<List<Integer>> values = multiMap.values();

            assertThat(values).hasSize(2);
            assertThat(values).anySatisfy(list -> assertThat(list).containsExactly(1, 2));
            assertThat(values).anySatisfy(list -> assertThat(list).containsExactly(3, 4));
        }

        @Test
        @DisplayName("Should return flattened values")
        void testValuesFlat() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", 1);
            multiMap.put("key1", 2);
            multiMap.put("key2", 3);
            multiMap.put("key2", 4);

            Collection<Integer> flatValues = multiMap.valuesFlat();

            assertThat(flatValues).containsExactlyInAnyOrder(1, 2, 3, 4);
        }

        @Test
        @DisplayName("Should return flat values as list")
        void testFlatValues() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", 1);
            multiMap.put("key1", 2);
            multiMap.put("key2", 3);

            List<Integer> flatValues = multiMap.flatValues();

            assertThat(flatValues).containsExactlyInAnyOrder(1, 2, 3);
            assertThat(flatValues).isInstanceOf(List.class);
        }

        @Test
        @DisplayName("Should return entry set")
        void testEntrySet() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", Arrays.asList(1, 2));
            multiMap.put("key2", Arrays.asList(3));

            Set<Map.Entry<String, List<Integer>>> entrySet = multiMap.entrySet();

            assertThat(entrySet).hasSize(2);
            assertThat(entrySet).anySatisfy(entry -> {
                assertThat(entry.getKey()).isEqualTo("key1");
                assertThat(entry.getValue()).containsExactly(1, 2);
            });
        }
    }

    @Nested
    @DisplayName("Map Access")
    class MapAccess {

        @Test
        @DisplayName("Should provide access to underlying map")
        void testGetMap() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", 1);
            multiMap.put("key2", 2);

            Map<String, List<Integer>> underlyingMap = multiMap.getMap();

            assertThat(underlyingMap).isNotNull();
            assertThat(underlyingMap).hasSize(2);
            assertThat(underlyingMap.get("key1")).containsExactly(1);
            assertThat(underlyingMap.get("key2")).containsExactly(2);
        }

        @Test
        @DisplayName("Should return reference to internal map (not copy)")
        void testGetMapReturnsReference() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", 1);

            Map<String, List<Integer>> map1 = multiMap.getMap();
            Map<String, List<Integer>> map2 = multiMap.getMap();

            assertThat(map1).isSameAs(map2); // Should be same reference
        }
    }

    @Nested
    @DisplayName("Equality and Hash Code")
    class EqualityAndHashCode {

        @Test
        @DisplayName("Should be equal to another MultiMap with same content")
        void testEquals() {
            MultiMap<String, Integer> map1 = new MultiMap<>();
            map1.put("key1", 1);
            map1.put("key1", 2);
            map1.put("key2", 3);

            MultiMap<String, Integer> map2 = new MultiMap<>();
            map2.put("key1", 1);
            map2.put("key1", 2);
            map2.put("key2", 3);

            assertThat(map1).isEqualTo(map2);
            assertThat(map1.equals(map2)).isTrue();
        }

        @Test
        @DisplayName("Should not be equal to MultiMap with different content")
        void testNotEquals() {
            MultiMap<String, Integer> map1 = new MultiMap<>();
            map1.put("key1", 1);

            MultiMap<String, Integer> map2 = new MultiMap<>();
            map2.put("key1", 2);

            assertThat(map1).isNotEqualTo(map2);
        }

        @Test
        @DisplayName("Should not be equal to null")
        void testNotEqualsNull() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            assertThat(multiMap.equals(null)).isFalse();
        }

        @Test
        @DisplayName("Should not be equal to non-MultiMap object")
        void testNotEqualsOtherType() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            assertThat(multiMap.equals("not a multimap")).isFalse();
        }

        @Test
        @DisplayName("Should have same hash code for equal MultiMaps")
        void testHashCode() {
            MultiMap<String, Integer> map1 = new MultiMap<>();
            map1.put("key1", 1);
            map1.put("key2", 2);

            MultiMap<String, Integer> map2 = new MultiMap<>();
            map2.put("key1", 1);
            map2.put("key2", 2);

            assertThat(map1.hashCode()).isEqualTo(map2.hashCode());
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentation {

        @Test
        @DisplayName("Should convert to string representation")
        void testToString() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();
            multiMap.put("key1", 1);
            multiMap.put("key1", 2);
            multiMap.put("key2", 3);

            String result = multiMap.toString();

            assertThat(result).isNotNull();
            assertThat(result).contains("key1");
            assertThat(result).contains("key2");
            // String representation should match underlying map's toString
        }

        @Test
        @DisplayName("Should handle empty MultiMap toString")
        void testToStringEmpty() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            String result = multiMap.toString();

            assertThat(result).isEqualTo("{}");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle null keys gracefully")
        void testNullKey() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            multiMap.put(null, 1);
            assertThat(multiMap.get(null)).containsExactly(1);
            assertThat(multiMap.containsKey(null)).isTrue();
        }

        @Test
        @DisplayName("Should handle null values gracefully")
        void testNullValue() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            Integer nullValue = null;
            multiMap.put("key1", nullValue);
            assertThat(multiMap.get("key1")).containsExactly((Integer) null);
        }

        @Test
        @DisplayName("Should handle empty string keys")
        void testEmptyStringKey() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            multiMap.put("", 1);
            assertThat(multiMap.get("")).containsExactly(1);
        }

        @Test
        @DisplayName("Should handle large number of values per key")
        void testLargeValueList() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            for (int i = 0; i < 1000; i++) {
                multiMap.put("key1", i);
            }

            List<Integer> values = multiMap.get("key1");
            assertThat(values).hasSize(1000);
            assertThat(values.get(0)).isEqualTo(0);
            assertThat(values.get(999)).isEqualTo(999);
        }

        @Test
        @DisplayName("Should handle many different keys")
        void testManyKeys() {
            MultiMap<String, Integer> multiMap = new MultiMap<>();

            for (int i = 0; i < 1000; i++) {
                multiMap.put("key" + i, i);
            }

            assertThat(multiMap.size()).isEqualTo(1000);
            assertThat(multiMap.get("key500")).containsExactly(500);
        }
    }

    @Nested
    @DisplayName("Generic Type Handling")
    class GenericTypeHandling {

        @Test
        @DisplayName("Should work with different key types")
        void testDifferentKeyTypes() {
            MultiMap<Integer, String> multiMap = new MultiMap<>();

            multiMap.put(1, "one");
            multiMap.put(2, "two");

            assertThat(multiMap.get(1)).containsExactly("one");
            assertThat(multiMap.get(2)).containsExactly("two");
        }

        @Test
        @DisplayName("Should work with complex value types")
        void testComplexValueTypes() {
            MultiMap<String, List<String>> multiMap = new MultiMap<>();

            List<String> list1 = Arrays.asList("a", "b");
            List<String> list2 = Arrays.asList("c", "d");

            multiMap.put("key1", list1);
            multiMap.put("key1", list2);

            assertThat(multiMap.get("key1")).containsExactly(list1, list2);
        }

        @Test
        @DisplayName("Should work with custom object types")
        void testCustomObjectTypes() {
            record Person(String name, int age) {
            }

            MultiMap<String, Person> multiMap = new MultiMap<>();
            Person person1 = new Person("Alice", 30);
            Person person2 = new Person("Bob", 25);

            multiMap.put("team1", person1);
            multiMap.put("team1", person2);

            assertThat(multiMap.get("team1")).containsExactly(person1, person2);
        }
    }
}
