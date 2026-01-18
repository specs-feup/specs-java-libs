package tdrc.vector;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the {@link IntegerVector3D} class.
 * 
 * @author Generated Tests
 */
@DisplayName("IntegerVector3D")
class IntegerVector3DTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create zero vector with default constructor")
        void testDefaultConstructor() {
            IntegerVector3D vector = new IntegerVector3D();

            assertThat(vector.getX()).isZero();
            assertThat(vector.getY()).isZero();
            assertThat(vector.getZ()).isZero();
        }

        @Test
        @DisplayName("Should create vector with given coordinates")
        void testParameterizedConstructor() {
            IntegerVector3D vector = new IntegerVector3D(3, 4, 5);

            assertThat(vector.getX()).isEqualTo(3);
            assertThat(vector.getY()).isEqualTo(4);
            assertThat(vector.getZ()).isEqualTo(5);
        }

        @Test
        @DisplayName("Should create vector with negative coordinates")
        void testNegativeCoordinates() {
            IntegerVector3D vector = new IntegerVector3D(-1, -2, -3);

            assertThat(vector.getX()).isEqualTo(-1);
            assertThat(vector.getY()).isEqualTo(-2);
            assertThat(vector.getZ()).isEqualTo(-3);
        }

        @Test
        @DisplayName("Should create vector with mixed positive and negative coordinates")
        void testMixedCoordinates() {
            IntegerVector3D vector = new IntegerVector3D(-5, 10, -15);

            assertThat(vector.getX()).isEqualTo(-5);
            assertThat(vector.getY()).isEqualTo(10);
            assertThat(vector.getZ()).isEqualTo(-15);
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should get and set X coordinate")
        void testGetSetX() {
            IntegerVector3D vector = new IntegerVector3D(1, 2, 3);

            vector.setX(10);
            assertThat(vector.getX()).isEqualTo(10);
            assertThat(vector.getY()).isEqualTo(2);
            assertThat(vector.getZ()).isEqualTo(3);

            vector.setX(-20);
            assertThat(vector.getX()).isEqualTo(-20);
        }

        @Test
        @DisplayName("Should get and set Y coordinate")
        void testGetSetY() {
            IntegerVector3D vector = new IntegerVector3D(1, 2, 3);

            vector.setY(20);
            assertThat(vector.getY()).isEqualTo(20);
            assertThat(vector.getX()).isEqualTo(1);
            assertThat(vector.getZ()).isEqualTo(3);

            vector.setY(-30);
            assertThat(vector.getY()).isEqualTo(-30);
        }

        @Test
        @DisplayName("Should get and set Z coordinate")
        void testGetSetZ() {
            IntegerVector3D vector = new IntegerVector3D(1, 2, 3);

            vector.setZ(30);
            assertThat(vector.getZ()).isEqualTo(30);
            assertThat(vector.getX()).isEqualTo(1);
            assertThat(vector.getY()).isEqualTo(2);

            vector.setZ(-40);
            assertThat(vector.getZ()).isEqualTo(-40);
        }

        @Test
        @DisplayName("Should handle independent coordinate changes")
        void testIndependentCoordinateChanges() {
            IntegerVector3D vector = new IntegerVector3D(0, 0, 0);

            vector.setX(5);
            assertThat(vector.getX()).isEqualTo(5);
            assertThat(vector.getY()).isZero();
            assertThat(vector.getZ()).isZero();

            vector.setY(8);
            assertThat(vector.getX()).isEqualTo(5);
            assertThat(vector.getY()).isEqualTo(8);
            assertThat(vector.getZ()).isZero();

            vector.setZ(12);
            assertThat(vector.getX()).isEqualTo(5);
            assertThat(vector.getY()).isEqualTo(8);
            assertThat(vector.getZ()).isEqualTo(12);
        }
    }

    @Nested
    @DisplayName("3D Distance Calculation Tests")
    class DistanceTests {

        @Test
        @DisplayName("Should calculate distance to itself as zero")
        void testDistanceToSelf() {
            IntegerVector3D vector = new IntegerVector3D(3, 4, 5);

            double distance = vector.getDistance(vector);

            assertThat(distance).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should calculate distance between same points as zero")
        void testDistanceBetweenSamePoints() {
            IntegerVector3D vector1 = new IntegerVector3D(1, 2, 3);
            IntegerVector3D vector2 = new IntegerVector3D(1, 2, 3);

            double distance = vector1.getDistance(vector2);

            assertThat(distance).isEqualTo(0.0);
        }

        @Test
        @DisplayName("Should calculate 3D Euclidean distance")
        void test3DEuclideanDistance() {
            IntegerVector3D origin = new IntegerVector3D(0, 0, 0);
            IntegerVector3D point = new IntegerVector3D(3, 4, 12);

            double distance = origin.getDistance(point);

            // Distance = sqrt(3² + 4² + 12²) = sqrt(9 + 16 + 144) = sqrt(169) = 13
            assertThat(distance).isEqualTo(13.0);
        }

        @Test
        @DisplayName("Should calculate distance with negative coordinates")
        void testDistanceWithNegativeCoordinates() {
            IntegerVector3D vector1 = new IntegerVector3D(-1, -2, -3);
            IntegerVector3D vector2 = new IntegerVector3D(2, 2, 3);

            double distance = vector1.getDistance(vector2);

            // Distance from (-1,-2,-3) to (2,2,3) = sqrt((2-(-1))² + (2-(-2))² + (3-(-3))²)
            // = sqrt(3² + 4² + 6²) = sqrt(9 + 16 + 36) = sqrt(61)
            assertThat(distance).isCloseTo(7.810, within(0.001));
        }

        @Test
        @DisplayName("Should calculate distance along coordinate axes")
        void testDistanceAlongAxes() {
            IntegerVector3D origin = new IntegerVector3D(0, 0, 0);
            IntegerVector3D xAxis = new IntegerVector3D(10, 0, 0);
            IntegerVector3D yAxis = new IntegerVector3D(0, 10, 0);
            IntegerVector3D zAxis = new IntegerVector3D(0, 0, 10);

            assertThat(origin.getDistance(xAxis)).isEqualTo(10.0);
            assertThat(origin.getDistance(yAxis)).isEqualTo(10.0);
            assertThat(origin.getDistance(zAxis)).isEqualTo(10.0);

            // Distance from (10,0,0) to (0,10,0) = sqrt(100 + 100) = sqrt(200) ≈ 14.142
            assertThat(xAxis.getDistance(yAxis)).isCloseTo(14.142, within(0.001));

            // Distance from (10,0,0) to (0,0,10) = sqrt(100 + 100) = sqrt(200) ≈ 14.142
            assertThat(xAxis.getDistance(zAxis)).isCloseTo(14.142, within(0.001));

            // Distance from (0,10,0) to (0,0,10) = sqrt(100 + 100) = sqrt(200) ≈ 14.142
            assertThat(yAxis.getDistance(zAxis)).isCloseTo(14.142, within(0.001));
        }

        @Test
        @DisplayName("Should calculate symmetric distance")
        void testSymmetricDistance() {
            IntegerVector3D vector1 = new IntegerVector3D(1, 2, 3);
            IntegerVector3D vector2 = new IntegerVector3D(4, 6, 8);

            double distance1to2 = vector1.getDistance(vector2);
            double distance2to1 = vector2.getDistance(vector1);

            assertThat(distance1to2).isEqualTo(distance2to1);
        }

        @Test
        @DisplayName("Should properly include Z coordinate in distance calculation")
        void testZCoordinateInDistanceCalculation() {
            IntegerVector3D vector1 = new IntegerVector3D(0, 0, 0);
            IntegerVector3D vector2 = new IntegerVector3D(0, 0, 5);

            double distance = vector1.getDistance(vector2);

            // This should be 5 (pure Z-axis distance)
            // Before the fix, this would have been 0 because Z was ignored
            assertThat(distance).isEqualTo(5.0);
        }
    }

    @Nested
    @DisplayName("Comparable Tests")
    class ComparableTests {

        @Test
        @DisplayName("Should compare vectors by 3D magnitude first")
        void testCompareBy3DMagnitude() {
            IntegerVector3D smaller = new IntegerVector3D(1, 1, 1); // magnitude ≈ 1.732
            IntegerVector3D larger = new IntegerVector3D(3, 4, 5); // magnitude ≈ 7.071

            assertThat(smaller.compareTo(larger)).isNegative();
            assertThat(larger.compareTo(smaller)).isPositive();
        }

        @Test
        @DisplayName("Should compare vectors with same magnitude by XY-plane angle")
        void testCompareBySameMagnitudeByAngle() {
            // Both vectors have same magnitude but different XY-plane angles
            IntegerVector3D vector1 = new IntegerVector3D(3, 0, 0); // angle = 0 in XY plane
            IntegerVector3D vector2 = new IntegerVector3D(0, 3, 0); // angle = 90° in XY plane

            assertThat(vector1.compareTo(vector2)).isNegative(); // 0° < 90°
            assertThat(vector2.compareTo(vector1)).isPositive();
        }

        @Test
        @DisplayName("Should return zero for identical vectors")
        void testCompareIdenticalVectors() {
            IntegerVector3D vector1 = new IntegerVector3D(3, 4, 5);
            IntegerVector3D vector2 = new IntegerVector3D(3, 4, 5);

            assertThat(vector1.compareTo(vector2)).isZero();
        }

        @Test
        @DisplayName("Should compare vector to itself as zero")
        void testCompareToSelf() {
            IntegerVector3D vector = new IntegerVector3D(3, 4, 5);

            assertThat(vector.compareTo(vector)).isZero();
        }

        @Test
        @DisplayName("Should handle zero vector comparisons")
        void testZeroVectorComparisons() {
            IntegerVector3D zero = new IntegerVector3D(0, 0, 0);
            IntegerVector3D nonZero = new IntegerVector3D(1, 0, 0);

            assertThat(zero.compareTo(nonZero)).isNegative();
            assertThat(nonZero.compareTo(zero)).isPositive();
            assertThat(zero.compareTo(zero)).isZero();
        }

        @Test
        @DisplayName("Should handle vectors on YZ-plane (x=0)")
        void testYZPlaneVectors() {
            IntegerVector3D yzPlane1 = new IntegerVector3D(0, 3, 4); // x=0, should not crash
            IntegerVector3D yzPlane2 = new IntegerVector3D(0, 4, 3); // x=0, should not crash

            // This should not throw division by zero exception
            // Before the fix, this would crash with ArithmeticException
            int result = yzPlane1.compareTo(yzPlane2);
            assertThat(Math.abs(result)).isGreaterThanOrEqualTo(0); // Just ensure it doesn't crash
        }

        @Test
        @DisplayName("Should handle 3D magnitude calculation including Z coordinate")
        void test3DMagnitudeInComparison() {
            IntegerVector3D vector1 = new IntegerVector3D(1, 0, 0); // magnitude = 1
            IntegerVector3D vector2 = new IntegerVector3D(0, 0, 2); // magnitude = 2

            assertThat(vector1.compareTo(vector2)).isNegative();
            assertThat(vector2.compareTo(vector1)).isPositive();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle maximum integer values")
        void testMaximumValues() {
            IntegerVector3D vector = new IntegerVector3D(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

            assertThat(vector.getX()).isEqualTo(Integer.MAX_VALUE);
            assertThat(vector.getY()).isEqualTo(Integer.MAX_VALUE);
            assertThat(vector.getZ()).isEqualTo(Integer.MAX_VALUE);
        }

        @Test
        @DisplayName("Should handle minimum integer values")
        void testMinimumValues() {
            IntegerVector3D vector = new IntegerVector3D(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);

            assertThat(vector.getX()).isEqualTo(Integer.MIN_VALUE);
            assertThat(vector.getY()).isEqualTo(Integer.MIN_VALUE);
            assertThat(vector.getZ()).isEqualTo(Integer.MIN_VALUE);
        }

        @Test
        @DisplayName("Should handle operations with extreme values")
        void testOperationsWithExtremeValues() {
            IntegerVector3D maxVector = new IntegerVector3D(Integer.MAX_VALUE, 0, 0);
            IntegerVector3D origin = new IntegerVector3D(0, 0, 0);

            // This should not overflow in distance calculation (using double)
            double distance = maxVector.getDistance(origin);
            assertThat(distance).isEqualTo((double) Integer.MAX_VALUE);
        }

        @Test
        @DisplayName("Should handle coordinate changes from extreme values")
        void testCoordinateChangesFromExtremeValues() {
            IntegerVector3D vector = new IntegerVector3D(Integer.MAX_VALUE, Integer.MIN_VALUE, 0);

            vector.setX(0);
            vector.setY(0);
            vector.setZ(0);

            assertThat(vector.getX()).isZero();
            assertThat(vector.getY()).isZero();
            assertThat(vector.getZ()).isZero();
        }

        @Test
        @DisplayName("Should demonstrate Z coordinate is now properly used")
        void testZCoordinateUsage() {
            // This test demonstrates that Z coordinate is now properly included
            IntegerVector3D vector1 = new IntegerVector3D(0, 0, 0);
            IntegerVector3D vector2 = new IntegerVector3D(0, 0, 1);
            IntegerVector3D vector3 = new IntegerVector3D(0, 0, 2);

            // Distance calculations should include Z
            assertThat(vector1.getDistance(vector2)).isEqualTo(1.0);
            assertThat(vector1.getDistance(vector3)).isEqualTo(2.0);

            // Magnitude calculations should include Z
            assertThat(vector1.compareTo(vector2)).isNegative();
            assertThat(vector2.compareTo(vector3)).isNegative();
        }
    }
}
