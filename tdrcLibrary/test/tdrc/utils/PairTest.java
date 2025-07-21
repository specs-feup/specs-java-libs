package tdrc.utils;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive unit tests for Pair record class.
 * Tests record functionality, equals/hashCode, and basic operations.
 * 
 * @author Generated Tests
 */
@DisplayName("Pair Tests")
class PairTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Create pair with non-null values")
        void testConstructor_WithNonNullValues_CreatesCorrectPair() {
            // Arrange & Act
            Pair<String, Integer> pair = new Pair<>("hello", 42);

            // Assert
            assertThat(pair.left()).isEqualTo("hello");
            assertThat(pair.right()).isEqualTo(42);
        }

        @Test
        @DisplayName("Create pair with null left value")
        void testConstructor_WithNullLeft_CreatesCorrectPair() {
            // Arrange & Act
            Pair<String, Integer> pair = new Pair<>(null, 42);

            // Assert
            assertThat(pair.left()).isNull();
            assertThat(pair.right()).isEqualTo(42);
        }

        @Test
        @DisplayName("Create pair with null right value")
        void testConstructor_WithNullRight_CreatesCorrectPair() {
            // Arrange & Act
            Pair<String, Integer> pair = new Pair<>("hello", null);

            // Assert
            assertThat(pair.left()).isEqualTo("hello");
            assertThat(pair.right()).isNull();
        }

        @Test
        @DisplayName("Create pair with both null values")
        void testConstructor_WithBothNull_CreatesCorrectPair() {
            // Arrange & Act
            Pair<String, Integer> pair = new Pair<>(null, null);

            // Assert
            assertThat(pair.left()).isNull();
            assertThat(pair.right()).isNull();
        }
    }

    @Nested
    @DisplayName("Accessor Tests")
    class AccessorTests {

        @Test
        @DisplayName("Access left value")
        void testLeft_ReturnsCorrectValue() {
            // Arrange
            Pair<String, Integer> pair = new Pair<>("test", 100);

            // Act & Assert
            assertThat(pair.left()).isEqualTo("test");
        }

        @Test
        @DisplayName("Access right value")
        void testRight_ReturnsCorrectValue() {
            // Arrange
            Pair<String, Integer> pair = new Pair<>("test", 100);

            // Act & Assert
            assertThat(pair.right()).isEqualTo(100);
        }
    }

    @Nested
    @DisplayName("Equality Tests")
    class EqualityTests {

        @Test
        @DisplayName("Equal pairs with same values")
        void testEquals_SameValues_ReturnsTrue() {
            // Arrange
            Pair<String, Integer> pair1 = new Pair<>("hello", 42);
            Pair<String, Integer> pair2 = new Pair<>("hello", 42);

            // Act & Assert
            assertThat(pair1).isEqualTo(pair2);
            assertThat(pair2).isEqualTo(pair1);
        }

        @Test
        @DisplayName("Equal pairs with null values")
        void testEquals_WithNullValues_ReturnsTrue() {
            // Arrange
            Pair<String, Integer> pair1 = new Pair<>(null, null);
            Pair<String, Integer> pair2 = new Pair<>(null, null);

            // Act & Assert
            assertThat(pair1).isEqualTo(pair2);
        }

        @Test
        @DisplayName("Not equal pairs with different left values")
        void testEquals_DifferentLeftValues_ReturnsFalse() {
            // Arrange
            Pair<String, Integer> pair1 = new Pair<>("hello", 42);
            Pair<String, Integer> pair2 = new Pair<>("world", 42);

            // Act & Assert
            assertThat(pair1).isNotEqualTo(pair2);
        }

        @Test
        @DisplayName("Not equal pairs with different right values")
        void testEquals_DifferentRightValues_ReturnsFalse() {
            // Arrange
            Pair<String, Integer> pair1 = new Pair<>("hello", 42);
            Pair<String, Integer> pair2 = new Pair<>("hello", 100);

            // Act & Assert
            assertThat(pair1).isNotEqualTo(pair2);
        }

        @Test
        @DisplayName("Not equal to null")
        void testEquals_WithNull_ReturnsFalse() {
            // Arrange
            Pair<String, Integer> pair = new Pair<>("hello", 42);

            // Act & Assert
            assertThat(pair).isNotEqualTo(null);
        }

        @Test
        @DisplayName("Not equal to different type")
        void testEquals_WithDifferentType_ReturnsFalse() {
            // Arrange
            Pair<String, Integer> pair = new Pair<>("hello", 42);

            // Act & Assert
            assertThat(pair).isNotEqualTo("not a pair");
        }
    }

    @Nested
    @DisplayName("Hash Code Tests")
    class HashCodeTests {

        @Test
        @DisplayName("Same hash code for equal pairs")
        void testHashCode_EqualPairs_SameHashCode() {
            // Arrange
            Pair<String, Integer> pair1 = new Pair<>("hello", 42);
            Pair<String, Integer> pair2 = new Pair<>("hello", 42);

            // Act & Assert
            assertThat(pair1.hashCode()).isEqualTo(pair2.hashCode());
        }

        @Test
        @DisplayName("Different hash codes for different pairs")
        void testHashCode_DifferentPairs_DifferentHashCodes() {
            // Arrange
            Pair<String, Integer> pair1 = new Pair<>("hello", 42);
            Pair<String, Integer> pair2 = new Pair<>("world", 42);

            // Act & Assert
            // Note: Different objects can have same hash code, but it's unlikely
            // This test checks the most common case
            assertThat(pair1.hashCode()).isNotEqualTo(pair2.hashCode());
        }

        @Test
        @DisplayName("Consistent hash code")
        void testHashCode_ConsistentResults() {
            // Arrange
            Pair<String, Integer> pair = new Pair<>("hello", 42);

            // Act
            int hash1 = pair.hashCode();
            int hash2 = pair.hashCode();

            // Assert
            assertThat(hash1).isEqualTo(hash2);
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("toString returns proper format")
        void testToString_ReturnsCorrectFormat() {
            // Arrange
            Pair<String, Integer> pair = new Pair<>("hello", 42);

            // Act
            String result = pair.toString();

            // Assert
            assertThat(result).contains("hello");
            assertThat(result).contains("42");
            assertThat(result).contains("Pair");
        }

        @Test
        @DisplayName("toString with null values")
        void testToString_WithNullValues_HandlesNulls() {
            // Arrange
            Pair<String, Integer> pair = new Pair<>(null, null);

            // Act
            String result = pair.toString();

            // Assert
            assertThat(result).contains("null");
            assertThat(result).contains("Pair");
        }
    }

    @Nested
    @DisplayName("Generic Type Tests")
    class GenericTypeTests {

        @Test
        @DisplayName("Different generic types")
        void testGenericTypes_DifferentTypes_WorkCorrectly() {
            // Arrange & Act
            Pair<Integer, String> intStringPair = new Pair<>(1, "one");
            Pair<Double, Boolean> doubleBooleanPair = new Pair<>(3.14, true);
            Pair<String, String> stringStringPair = new Pair<>("left", "right");

            // Assert
            assertThat(intStringPair.left()).isEqualTo(1);
            assertThat(intStringPair.right()).isEqualTo("one");

            assertThat(doubleBooleanPair.left()).isEqualTo(3.14);
            assertThat(doubleBooleanPair.right()).isEqualTo(true);

            assertThat(stringStringPair.left()).isEqualTo("left");
            assertThat(stringStringPair.right()).isEqualTo("right");
        }

        @Test
        @DisplayName("Complex object types")
        void testGenericTypes_ComplexObjects_WorkCorrectly() {
            // Arrange & Act
            Pair<java.util.List<String>, java.util.Map<String, Integer>> complexPair = new Pair<>(
                    java.util.Arrays.asList("a", "b", "c"),
                    java.util.Map.of("key1", 1, "key2", 2));

            // Assert
            assertThat(complexPair.left()).hasSize(3);
            assertThat(complexPair.left()).contains("a", "b", "c");
            assertThat(complexPair.right()).hasSize(2);
            assertThat(complexPair.right()).containsEntry("key1", 1);
        }
    }
}
