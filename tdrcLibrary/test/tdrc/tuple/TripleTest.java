package tdrc.tuple;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link Triple} class.
 * 
 * @author Generated Tests
 */
@DisplayName("Triple")
class TripleTest {

    @Nested
    @DisplayName("Factory Method")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create triple with all non-null values")
        void testNewInstanceWithNonNullValues() {
            Triple<String, Integer, Double> triple = Triple.newInstance("hello", 42, 3.14);

            assertThat(triple.getX()).isEqualTo("hello");
            assertThat(triple.getY()).isEqualTo(42);
            assertThat(triple.getZ()).isEqualTo(3.14);
        }

        @Test
        @DisplayName("Should create triple with null values")
        void testNewInstanceWithNullValues() {
            Triple<String, Integer, Double> triple = Triple.newInstance(null, null, null);

            assertThat(triple.getX()).isNull();
            assertThat(triple.getY()).isNull();
            assertThat(triple.getZ()).isNull();
        }

        @Test
        @DisplayName("Should create triple with mixed types")
        void testNewInstanceWithMixedTypes() {
            Triple<String, Boolean, Character> triple = Triple.newInstance("test", true, 'A');

            assertThat(triple.getX()).isEqualTo("test");
            assertThat(triple.getY()).isTrue();
            assertThat(triple.getZ()).isEqualTo('A');
        }
    }

    @Nested
    @DisplayName("Getter and Setter Methods")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get and set X value")
        void testGetSetX() {
            Triple<String, Integer, Double> triple = Triple.newInstance("initial", 0, 0.0);

            triple.setX("updated");
            assertThat(triple.getX()).isEqualTo("updated");

            triple.setX(null);
            assertThat(triple.getX()).isNull();
        }

        @Test
        @DisplayName("Should get and set Y value")
        void testGetSetY() {
            Triple<String, Integer, Double> triple = Triple.newInstance("test", 10, 0.0);

            triple.setY(20);
            assertThat(triple.getY()).isEqualTo(20);

            triple.setY(null);
            assertThat(triple.getY()).isNull();
        }

        @Test
        @DisplayName("Should get and set Z value")
        void testGetSetZ() {
            Triple<String, Integer, Double> triple = Triple.newInstance("test", 0, 1.5);

            triple.setZ(2.7);
            assertThat(triple.getZ()).isEqualTo(2.7);

            triple.setZ(null);
            assertThat(triple.getZ()).isNull();
        }

        @Test
        @DisplayName("Should handle independent value changes")
        void testIndependentValueChanges() {
            Triple<String, String, String> triple = Triple.newInstance("a", "b", "c");

            triple.setX("x");
            assertThat(triple.getX()).isEqualTo("x");
            assertThat(triple.getY()).isEqualTo("b");
            assertThat(triple.getZ()).isEqualTo("c");

            triple.setY("y");
            assertThat(triple.getX()).isEqualTo("x");
            assertThat(triple.getY()).isEqualTo("y");
            assertThat(triple.getZ()).isEqualTo("c");

            triple.setZ("z");
            assertThat(triple.getX()).isEqualTo("x");
            assertThat(triple.getY()).isEqualTo("y");
            assertThat(triple.getZ()).isEqualTo("z");
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {

        @Test
        @DisplayName("Should format non-null values correctly")
        void testToStringWithNonNullValues() {
            Triple<String, Integer, Double> triple = Triple.newInstance("hello", 42, 3.14);

            assertThat(triple.toString()).isEqualTo("(hello,42,3.14)");
        }

        @Test
        @DisplayName("Should format null values correctly")
        void testToStringWithNullValues() {
            Triple<String, Integer, Double> triple = Triple.newInstance(null, null, null);

            assertThat(triple.toString()).isEqualTo("(null,null,null)");
        }

        @Test
        @DisplayName("Should format mixed null and non-null values")
        void testToStringWithMixedValues() {
            Triple<String, Integer, String> triple = Triple.newInstance("first", null, "third");

            assertThat(triple.toString()).isEqualTo("(first,null,third)");
        }

        @Test
        @DisplayName("Should format empty strings correctly")
        void testToStringWithEmptyStrings() {
            Triple<String, String, String> triple = Triple.newInstance("", "middle", "");

            assertThat(triple.toString()).isEqualTo("(,middle,)");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Object Behavior")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle generic type changes")
        void testGenericTypeFlexibility() {
            Triple<Number, Number, Number> triple = Triple.newInstance(1, 2.0f, 3.0);

            assertThat(triple.getX()).isEqualTo(1);
            assertThat(triple.getY()).isEqualTo(2.0f);
            assertThat(triple.getZ()).isEqualTo(3.0);
        }

        @Test
        @DisplayName("Should handle same type for all parameters")
        void testSameTypeForAllParameters() {
            Triple<String, String, String> triple = Triple.newInstance("one", "two", "three");

            assertThat(triple.getX()).isEqualTo("one");
            assertThat(triple.getY()).isEqualTo("two");
            assertThat(triple.getZ()).isEqualTo("three");
        }

        @Test
        @DisplayName("Should be different instances from factory method")
        void testDifferentInstances() {
            Triple<String, Integer, Double> triple1 = Triple.newInstance("test", 1, 1.0);
            Triple<String, Integer, Double> triple2 = Triple.newInstance("test", 1, 1.0);

            // Different instances
            assertThat(triple1).isNotSameAs(triple2);
        }

        @Test
        @DisplayName("Should handle complex objects")
        void testComplexObjects() {
            java.util.List<String> list = java.util.Arrays.asList("a", "b");
            java.util.Map<String, Integer> map = java.util.Map.of("key", 42);
            Object obj = new Object();

            Triple<java.util.List<String>, java.util.Map<String, Integer>, Object> triple = Triple.newInstance(list,
                    map, obj);

            assertThat(triple.getX()).isSameAs(list);
            assertThat(triple.getY()).isSameAs(map);
            assertThat(triple.getZ()).isSameAs(obj);
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Implementation")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Should properly implement equals() - identical values are equal")
        void testEqualsImplementation() {
            Triple<String, Integer, Double> triple1 = Triple.newInstance("test", 42, 3.14);
            Triple<String, Integer, Double> triple2 = Triple.newInstance("test", 42, 3.14);

            // Fixed: identical values should be equal
            assertThat(triple1.equals(triple2)).isTrue();
            assertThat(triple1).isEqualTo(triple2);
        }

        @Test
        @DisplayName("Should properly implement hashCode() - same values have same hash codes")
        void testHashCodeImplementation() {
            Triple<String, Integer, Double> triple1 = Triple.newInstance("test", 42, 3.14);
            Triple<String, Integer, Double> triple2 = Triple.newInstance("test", 42, 3.14);

            // Fixed: identical values should have same hash code
            assertThat(triple1.hashCode()).isEqualTo(triple2.hashCode());
        }

        @Test
        @DisplayName("Should handle null values in equals()")
        void testEqualsWithNullValues() {
            Triple<String, Integer, Double> triple1 = Triple.newInstance(null, null, null);
            Triple<String, Integer, Double> triple2 = Triple.newInstance(null, null, null);
            Triple<String, Integer, Double> triple3 = Triple.newInstance("test", null, 3.14);

            assertThat(triple1).isEqualTo(triple2);
            assertThat(triple1).isNotEqualTo(triple3);
        }

        @Test
        @DisplayName("Should handle null values in hashCode()")
        void testHashCodeWithNullValues() {
            Triple<String, Integer, Double> triple1 = Triple.newInstance(null, null, null);
            Triple<String, Integer, Double> triple2 = Triple.newInstance(null, null, null);

            assertThat(triple1.hashCode()).isEqualTo(triple2.hashCode());
        }

        @Test
        @DisplayName("Should at least be equal to itself")
        void testSelfEquality() {
            Triple<String, Integer, Double> triple = Triple.newInstance("test", 42, 3.14);

            // This should always work regardless of equals() implementation
            assertThat(triple.equals(triple)).isTrue();
            assertThat(triple).isEqualTo(triple);
        }
    }
}
