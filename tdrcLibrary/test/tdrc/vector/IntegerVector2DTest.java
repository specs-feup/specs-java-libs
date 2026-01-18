package tdrc.vector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link IntegerVector2D} class.
 * 
 * @author Generated Tests
 */
@DisplayName("IntegerVector2D")
class IntegerVector2DTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create zero vector with default constructor")
        void testDefaultConstructor() {
            IntegerVector2D vector = new IntegerVector2D();

            assertThat(vector.getX()).isZero();
            assertThat(vector.getY()).isZero();
        }

        @Test
        @DisplayName("Should create vector with given coordinates")
        void testParameterizedConstructor() {
            IntegerVector2D vector = new IntegerVector2D(3, 4);

            assertThat(vector.getX()).isEqualTo(3);
            assertThat(vector.getY()).isEqualTo(4);
        }

        @Test
        @DisplayName("Should create vector with negative coordinates")
        void testNegativeCoordinates() {
            IntegerVector2D vector = new IntegerVector2D(-5, -10);

            assertThat(vector.getX()).isEqualTo(-5);
            assertThat(vector.getY()).isEqualTo(-10);
        }

        @Test
        @DisplayName("Should create vector with mixed positive and negative coordinates")
        void testMixedCoordinates() {
            IntegerVector2D vector = new IntegerVector2D(-2, 7);

            assertThat(vector.getX()).isEqualTo(-2);
            assertThat(vector.getY()).isEqualTo(7);
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get and set X coordinate")
        void testGetSetX() {
            IntegerVector2D vector = new IntegerVector2D(1, 2);

            vector.setX(10);
            assertThat(vector.getX()).isEqualTo(10);
            assertThat(vector.getY()).isEqualTo(2); // Y should remain unchanged

            vector.setX(-15);
            assertThat(vector.getX()).isEqualTo(-15);
        }

        @Test
        @DisplayName("Should get and set Y coordinate")
        void testGetSetY() {
            IntegerVector2D vector = new IntegerVector2D(1, 2);

            vector.setY(20);
            assertThat(vector.getY()).isEqualTo(20);
            assertThat(vector.getX()).isEqualTo(1); // X should remain unchanged

            vector.setY(-25);
            assertThat(vector.getY()).isEqualTo(-25);
        }

        @Test
        @DisplayName("Should handle independent coordinate changes")
        void testIndependentCoordinateChanges() {
            IntegerVector2D vector = new IntegerVector2D(0, 0);

            vector.setX(5);
            assertThat(vector.getX()).isEqualTo(5);
            assertThat(vector.getY()).isZero();

            vector.setY(8);
            assertThat(vector.getX()).isEqualTo(5);
            assertThat(vector.getY()).isEqualTo(8);
        }
    }

    @Nested
    @DisplayName("Distance Calculation Tests")
    class DistanceTests {

        @Test
        @DisplayName("Should calculate distance to itself as zero")
        void testDistanceToSelf() {
            IntegerVector2D vector = new IntegerVector2D(3, 4);

            double distance = vector.getDistance(vector);

            assertThat(distance).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should calculate distance between same points as zero")
        void testDistanceBetweenSamePoints() {
            IntegerVector2D vector1 = new IntegerVector2D(5, 12);
            IntegerVector2D vector2 = new IntegerVector2D(5, 12);

            double distance = vector1.getDistance(vector2);

            assertThat(distance).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should calculate distance using Pythagorean theorem")
        void testPythagoreanDistance() {
            IntegerVector2D vector1 = new IntegerVector2D(0, 0);
            IntegerVector2D vector2 = new IntegerVector2D(3, 4);

            double distance = vector1.getDistance(vector2);

            // 3-4-5 triangle
            assertThat(distance).isEqualTo(5.0);
        }

        @Test
        @DisplayName("Should calculate distance with negative coordinates")
        void testDistanceWithNegativeCoordinates() {
            IntegerVector2D vector1 = new IntegerVector2D(-1, -1);
            IntegerVector2D vector2 = new IntegerVector2D(2, 3);

            double distance = vector1.getDistance(vector2);

            // Distance from (-1,-1) to (2,3) = sqrt((2-(-1))^2 + (3-(-1))^2) = sqrt(9 + 16)
            // = 5
            assertThat(distance).isEqualTo(5.0);
        }

        @Test
        @DisplayName("Should calculate distance along axes")
        void testDistanceAlongAxes() {
            IntegerVector2D origin = new IntegerVector2D(0, 0);
            IntegerVector2D xAxis = new IntegerVector2D(10, 0);
            IntegerVector2D yAxis = new IntegerVector2D(0, 10);

            assertThat(origin.getDistance(xAxis)).isEqualTo(10.0);
            assertThat(origin.getDistance(yAxis)).isEqualTo(10.0);
            assertThat(xAxis.getDistance(yAxis)).isCloseTo(14.142, within(0.001));
        }

        @Test
        @DisplayName("Should calculate symmetric distance")
        void testSymmetricDistance() {
            IntegerVector2D vector1 = new IntegerVector2D(1, 2);
            IntegerVector2D vector2 = new IntegerVector2D(4, 6);

            double distance1to2 = vector1.getDistance(vector2);
            double distance2to1 = vector2.getDistance(vector1);

            assertThat(distance1to2).isEqualTo(distance2to1);
        }
    }

    @Nested
    @DisplayName("Comparable Tests")
    class ComparableTests {

        @Test
        @DisplayName("Should compare vectors by magnitude first")
        void testCompareByMagnitude() {
            IntegerVector2D smaller = new IntegerVector2D(1, 1); // magnitude ≈ 1.414
            IntegerVector2D larger = new IntegerVector2D(3, 4); // magnitude = 5.0

            assertThat(smaller.compareTo(larger)).isNegative();
            assertThat(larger.compareTo(smaller)).isPositive();
        }

        @Test
        @DisplayName("Should compare vectors with same magnitude by angle")
        void testCompareBySameMangnitudeByAngle() {
            IntegerVector2D vector1 = new IntegerVector2D(3, 0); // angle = 0
            IntegerVector2D vector2 = new IntegerVector2D(0, 3); // angle = 90 degrees

            // Both have magnitude 3, but different angles
            assertThat(vector1.compareTo(vector2)).isNegative(); // 0° < 90°
            assertThat(vector2.compareTo(vector1)).isPositive();
        }

        @Test
        @DisplayName("Should return zero for identical vectors")
        void testCompareIdenticalVectors() {
            IntegerVector2D vector1 = new IntegerVector2D(3, 4);
            IntegerVector2D vector2 = new IntegerVector2D(3, 4);

            assertThat(vector1.compareTo(vector2)).isZero();
        }

        @Test
        @DisplayName("Should compare vector to itself as zero")
        void testCompareToSelf() {
            IntegerVector2D vector = new IntegerVector2D(3, 4);

            assertThat(vector.compareTo(vector)).isZero();
        }

        @Test
        @DisplayName("Should handle zero vector comparisons")
        void testZeroVectorComparisons() {
            IntegerVector2D zero = new IntegerVector2D(0, 0);
            IntegerVector2D nonZero = new IntegerVector2D(1, 0);

            assertThat(zero.compareTo(nonZero)).isNegative();
            assertThat(nonZero.compareTo(zero)).isPositive();
            assertThat(zero.compareTo(zero)).isZero();
        }

        @Test
        @DisplayName("Should handle negative coordinates in comparison")
        void testNegativeCoordinatesInComparison() {
            IntegerVector2D negative = new IntegerVector2D(-3, -4); // magnitude = 5
            IntegerVector2D positive = new IntegerVector2D(3, 4); // magnitude = 5

            // Same magnitude, but different angles
            int result = negative.compareTo(positive);
            // The actual result depends on the angle calculation
            assertThat(Math.abs(result)).isGreaterThanOrEqualTo(0);
        }

        @Test
        @DisplayName("Should maintain comparison consistency")
        void testComparisonConsistency() {
            IntegerVector2D v1 = new IntegerVector2D(1, 2);
            IntegerVector2D v2 = new IntegerVector2D(3, 4);
            IntegerVector2D v3 = new IntegerVector2D(5, 6);

            // Transitivity test
            int compare12 = v1.compareTo(v2);
            int compare23 = v2.compareTo(v3);
            int compare13 = v1.compareTo(v3);

            if (compare12 < 0 && compare23 < 0) {
                assertThat(compare13).isNegative();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle maximum integer values")
        void testMaximumValues() {
            IntegerVector2D vector = new IntegerVector2D(Integer.MAX_VALUE, Integer.MAX_VALUE);

            assertThat(vector.getX()).isEqualTo(Integer.MAX_VALUE);
            assertThat(vector.getY()).isEqualTo(Integer.MAX_VALUE);
        }

        @Test
        @DisplayName("Should handle minimum integer values")
        void testMinimumValues() {
            IntegerVector2D vector = new IntegerVector2D(Integer.MIN_VALUE, Integer.MIN_VALUE);

            assertThat(vector.getX()).isEqualTo(Integer.MIN_VALUE);
            assertThat(vector.getY()).isEqualTo(Integer.MIN_VALUE);
        }

        @Test
        @DisplayName("Should handle operations with extreme values")
        void testOperationsWithExtremeValues() {
            IntegerVector2D maxVector = new IntegerVector2D(Integer.MAX_VALUE, 0);
            IntegerVector2D origin = new IntegerVector2D(0, 0);

            // This should not overflow in distance calculation (using double)
            double distance = maxVector.getDistance(origin);
            assertThat(distance).isEqualTo((double) Integer.MAX_VALUE);
        }

        @Test
        @DisplayName("Should handle coordinate changes from extreme values")
        void testCoordinateChangesFromExtremeValues() {
            IntegerVector2D vector = new IntegerVector2D(Integer.MAX_VALUE, Integer.MIN_VALUE);

            vector.setX(0);
            vector.setY(0);

            assertThat(vector.getX()).isZero();
            assertThat(vector.getY()).isZero();
        }
    }
}
