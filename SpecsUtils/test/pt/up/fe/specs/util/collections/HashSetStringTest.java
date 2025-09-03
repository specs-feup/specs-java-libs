package pt.up.fe.specs.util.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link HashSetString} class.
 * Tests specialized String HashSet with enum name support.
 * 
 * @author Generated Tests
 */
class HashSetStringTest {

    private HashSetString hashSetString;

    // Test enum for enum-related tests
    private enum TestStatus {
        ACTIVE, INACTIVE, PENDING, COMPLETED
    }

    @BeforeEach
    void setUp() {
        hashSetString = new HashSetString();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty HashSetString with default constructor")
        void testDefaultConstructor() {
            HashSetString set = new HashSetString();

            assertThat(set).isEmpty();
            assertThat(set).isInstanceOf(HashSetString.class);
        }

        @Test
        @DisplayName("Should create HashSetString from collection")
        void testCollectionConstructor() {
            List<String> strings = Arrays.asList("one", "two", "three");
            HashSetString set = new HashSetString(strings);

            assertThat(set).hasSize(3);
            assertThat(set).containsExactlyInAnyOrder("one", "two", "three");
        }

        @Test
        @DisplayName("Should create empty HashSetString from empty collection")
        void testEmptyCollectionConstructor() {
            HashSetString set = new HashSetString(Arrays.asList());

            assertThat(set).isEmpty();
        }

        @Test
        @DisplayName("Should handle null elements in collection constructor")
        void testCollectionConstructorWithNulls() {
            List<String> strings = Arrays.asList("one", null, "three");
            HashSetString set = new HashSetString(strings);

            assertThat(set).hasSize(3);
            assertThat(set).containsExactlyInAnyOrder("one", null, "three");
        }

        @Test
        @DisplayName("Should handle duplicate elements in collection constructor")
        void testCollectionConstructorWithDuplicates() {
            List<String> strings = Arrays.asList("one", "two", "one", "three", "two");
            HashSetString set = new HashSetString(strings);

            assertThat(set).hasSize(3);
            assertThat(set).containsExactlyInAnyOrder("one", "two", "three");
        }
    }

    @Nested
    @DisplayName("Basic HashSet Operations")
    class BasicOperationsTests {

        @Test
        @DisplayName("Should add strings correctly")
        void testAdd() {
            boolean added1 = hashSetString.add("test1");
            boolean added2 = hashSetString.add("test2");
            boolean addedDuplicate = hashSetString.add("test1");

            assertThat(added1).isTrue();
            assertThat(added2).isTrue();
            assertThat(addedDuplicate).isFalse();
            assertThat(hashSetString).hasSize(2);
            assertThat(hashSetString).containsExactlyInAnyOrder("test1", "test2");
        }

        @Test
        @DisplayName("Should contain strings correctly")
        void testContains() {
            hashSetString.add("test");

            assertThat(hashSetString.contains("test")).isTrue();
            assertThat(hashSetString.contains("notPresent")).isFalse();
        }

        @Test
        @DisplayName("Should remove strings correctly")
        void testRemove() {
            hashSetString.add("test");

            boolean removed = hashSetString.remove("test");
            boolean removedNotPresent = hashSetString.remove("notPresent");

            assertThat(removed).isTrue();
            assertThat(removedNotPresent).isFalse();
            assertThat(hashSetString).isEmpty();
        }

        @Test
        @DisplayName("Should handle null values")
        void testNullHandling() {
            boolean addedNull = hashSetString.add(null);
            boolean containsNull = hashSetString.contains(null);
            boolean removedNull = hashSetString.remove(null);

            assertThat(addedNull).isTrue();
            assertThat(containsNull).isTrue();
            assertThat(removedNull).isTrue();
            assertThat(hashSetString).isEmpty();
        }
    }

    @Nested
    @DisplayName("Enum Support Tests")
    class EnumSupportTests {

        @Test
        @DisplayName("Should contain enum by name")
        void testContainsEnum() {
            hashSetString.add("ACTIVE");
            hashSetString.add("PENDING");

            assertThat(hashSetString.contains(TestStatus.ACTIVE)).isTrue();
            assertThat(hashSetString.contains(TestStatus.PENDING)).isTrue();
            assertThat(hashSetString.contains(TestStatus.INACTIVE)).isFalse();
            assertThat(hashSetString.contains(TestStatus.COMPLETED)).isFalse();
        }

        @Test
        @DisplayName("Should work with different enum types")
        void testDifferentEnumTypes() {
            hashSetString.add("MONDAY");
            hashSetString.add("TUESDAY");

            // Using different enum types
            assertThat(hashSetString.contains(java.time.DayOfWeek.MONDAY)).isTrue();
            assertThat(hashSetString.contains(java.time.DayOfWeek.TUESDAY)).isTrue();
            assertThat(hashSetString.contains(java.time.DayOfWeek.WEDNESDAY)).isFalse();
        }

        @Test
        @DisplayName("Should handle enum with complex names")
        void testEnumWithComplexNames() {
            // Create enum names that might have special characters
            hashSetString.add("VALUE_WITH_UNDERSCORE");
            hashSetString.add("VALUE123");

            // Create a temporary enum for testing
            enum ComplexEnum {
                VALUE_WITH_UNDERSCORE, VALUE123, OTHER_VALUE
            }

            assertThat(hashSetString.contains(ComplexEnum.VALUE_WITH_UNDERSCORE)).isTrue();
            assertThat(hashSetString.contains(ComplexEnum.VALUE123)).isTrue();
            assertThat(hashSetString.contains(ComplexEnum.OTHER_VALUE)).isFalse();
        }

        @Test
        @DisplayName("Should maintain case sensitivity for enum names")
        void testEnumCaseSensitivity() {
            hashSetString.add("active"); // lowercase

            assertThat(hashSetString.contains(TestStatus.ACTIVE)).isFalse(); // ACTIVE is uppercase
            assertThat(hashSetString.contains("active")).isTrue();
        }
    }

    @Nested
    @DisplayName("Collection Operations")
    class CollectionOperationsTests {

        @Test
        @DisplayName("Should add all strings from collection")
        void testAddAll() {
            List<String> strings = Arrays.asList("one", "two", "three");

            boolean modified = hashSetString.addAll(strings);

            assertThat(modified).isTrue();
            assertThat(hashSetString).hasSize(3);
            assertThat(hashSetString).containsExactlyInAnyOrder("one", "two", "three");
        }

        @Test
        @DisplayName("Should retain only specified strings")
        void testRetainAll() {
            hashSetString.addAll(Arrays.asList("one", "two", "three", "four"));
            List<String> toRetain = Arrays.asList("two", "four", "five");

            boolean modified = hashSetString.retainAll(toRetain);

            assertThat(modified).isTrue();
            assertThat(hashSetString).hasSize(2);
            assertThat(hashSetString).containsExactlyInAnyOrder("two", "four");
        }

        @Test
        @DisplayName("Should remove all specified strings")
        void testRemoveAll() {
            hashSetString.addAll(Arrays.asList("one", "two", "three", "four"));
            List<String> toRemove = Arrays.asList("two", "four");

            boolean modified = hashSetString.removeAll(toRemove);

            assertThat(modified).isTrue();
            assertThat(hashSetString).hasSize(2);
            assertThat(hashSetString).containsExactlyInAnyOrder("one", "three");
        }

        @Test
        @DisplayName("Should check if contains all strings")
        void testContainsAll() {
            hashSetString.addAll(Arrays.asList("one", "two", "three"));

            assertThat(hashSetString.containsAll(Arrays.asList("one", "two"))).isTrue();
            assertThat(hashSetString.containsAll(Arrays.asList("one", "four"))).isFalse();
            assertThat(hashSetString.containsAll(Arrays.asList())).isTrue();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty strings")
        void testEmptyStrings() {
            hashSetString.add("");

            assertThat(hashSetString.contains("")).isTrue();
            assertThat(hashSetString).hasSize(1);
        }

        @Test
        @DisplayName("Should handle strings with special characters")
        void testSpecialCharacters() {
            String special1 = "test with spaces";
            String special2 = "test\nwith\nnewlines";
            String special3 = "test\twith\ttabs";
            String special4 = "test@#$%^&*()";

            hashSetString.add(special1);
            hashSetString.add(special2);
            hashSetString.add(special3);
            hashSetString.add(special4);

            assertThat(hashSetString).hasSize(4);
            assertThat(hashSetString.contains(special1)).isTrue();
            assertThat(hashSetString.contains(special2)).isTrue();
            assertThat(hashSetString.contains(special3)).isTrue();
            assertThat(hashSetString.contains(special4)).isTrue();
        }

        @Test
        @DisplayName("Should handle Unicode strings")
        void testUnicodeStrings() {
            String unicode1 = "café";
            String unicode2 = "naïve";
            String unicode3 = "🙂😊";

            hashSetString.add(unicode1);
            hashSetString.add(unicode2);
            hashSetString.add(unicode3);

            assertThat(hashSetString).hasSize(3);
            assertThat(hashSetString.contains(unicode1)).isTrue();
            assertThat(hashSetString.contains(unicode2)).isTrue();
            assertThat(hashSetString.contains(unicode3)).isTrue();
        }

        @Test
        @DisplayName("Should handle very long strings")
        void testLongStrings() {
            String longString = "a".repeat(10000);

            hashSetString.add(longString);

            assertThat(hashSetString.contains(longString)).isTrue();
            assertThat(hashSetString).hasSize(1);
        }
    }

    @Nested
    @DisplayName("Performance and Integration Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle large number of strings efficiently")
        void testLargeDataset() {
            // Add 1000 strings
            for (int i = 0; i < 1000; i++) {
                hashSetString.add("string" + i);
            }

            assertThat(hashSetString).hasSize(1000);
            assertThat(hashSetString.contains("string500")).isTrue();
            assertThat(hashSetString.contains("string1000")).isFalse();
        }

        @Test
        @DisplayName("Should work correctly with enum integration")
        void testEnumIntegration() {
            // Add all enum values as strings
            for (TestStatus status : TestStatus.values()) {
                hashSetString.add(status.name());
            }

            // Verify all enums are contained
            for (TestStatus status : TestStatus.values()) {
                assertThat(hashSetString.contains(status)).isTrue();
            }

            assertThat(hashSetString).hasSize(TestStatus.values().length);
        }

        @Test
        @DisplayName("Should maintain set behavior with mixed operations")
        void testMixedOperations() {
            // Mix of string and enum operations
            hashSetString.add("ACTIVE");
            hashSetString.add("custom");
            hashSetString.add("INACTIVE");

            assertThat(hashSetString.contains(TestStatus.ACTIVE)).isTrue();
            assertThat(hashSetString.contains("custom")).isTrue();
            assertThat(hashSetString.contains(TestStatus.INACTIVE)).isTrue();
            assertThat(hashSetString.contains(TestStatus.PENDING)).isFalse();

            // Remove via string
            hashSetString.remove("ACTIVE");
            assertThat(hashSetString.contains(TestStatus.ACTIVE)).isFalse();

            assertThat(hashSetString).hasSize(2);
        }
    }
}
