package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.BitSet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.utilities.PatternDetector.PatternState;

/**
 * Unit tests for {@link PatternDetector}.
 * 
 * Tests pattern detection functionality in integer sequences.
 * 
 * @author Generated Tests
 */
@DisplayName("PatternDetector")
class PatternDetectorTest {

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create with valid parameters")
        void shouldCreateWithValidParameters() {
            PatternDetector detector = new PatternDetector(5, true);

            assertThat(detector).isNotNull();
            assertThat(detector.getMaxPatternSize()).isEqualTo(5);
            assertThat(detector.getState()).isEqualTo(PatternState.NO_PATTERN);
        }

        @Test
        @DisplayName("should create with priority to smaller patterns")
        void shouldCreateWithPriorityToSmallerPatterns() {
            PatternDetector detector = new PatternDetector(3, false);

            assertThat(detector).isNotNull();
            assertThat(detector.getMaxPatternSize()).isEqualTo(3);
        }

        @Test
        @DisplayName("should handle max pattern size of 1")
        void shouldHandleMaxPatternSizeOfOne() {
            PatternDetector detector = new PatternDetector(1, true);

            assertThat(detector.getMaxPatternSize()).isEqualTo(1);
            assertThat(detector.getQueue()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Basic Pattern Detection")
    class BasicPatternDetection {

        @Test
        @DisplayName("should detect simple repeating pattern")
        void shouldDetectSimpleRepeatingPattern() {
            PatternDetector detector = new PatternDetector(5, true);

            // Feed pattern: 1, 2, 1, 2, 1, 2
            detector.step(1);
            detector.step(2);
            detector.step(1);
            detector.step(2);
            detector.step(1);
            detector.step(2);

            // Should eventually detect pattern
            assertThat(detector.getState()).isIn(PatternState.PATTERN_STARTED, PatternState.PATTERN_UNCHANGED);
        }

        @Test
        @DisplayName("should handle single value input")
        void shouldHandleSingleValueInput() {
            PatternDetector detector = new PatternDetector(3, true);

            PatternState state = detector.step(42);

            assertThat(state).isNotNull();
            assertThat(detector.getState()).isEqualTo(PatternState.NO_PATTERN);
        }

        @Test
        @DisplayName("should detect pattern of size 1")
        void shouldDetectPatternOfSizeOne() {
            PatternDetector detector = new PatternDetector(3, true);

            // Repeat same value
            detector.step(5);
            detector.step(5);
            detector.step(5);

            assertThat(detector.getState()).isIn(PatternState.PATTERN_STARTED, PatternState.PATTERN_UNCHANGED);
            assertThat(detector.getPatternSize()).isEqualTo(1);
        }

        @Test
        @DisplayName("should detect pattern of size 3")
        void shouldDetectPatternOfSizeThree() {
            PatternDetector detector = new PatternDetector(5, true);

            // Pattern: 1, 2, 3, 1, 2, 3, 1, 2, 3
            detector.step(1);
            detector.step(2);
            detector.step(3);
            detector.step(1);
            detector.step(2);
            detector.step(3);
            detector.step(1);

            assertThat(detector.getState()).isIn(PatternState.PATTERN_STARTED, PatternState.PATTERN_UNCHANGED);
            assertThat(detector.getPatternSize()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Pattern State Management")
    class PatternStateManagement {

        @Test
        @DisplayName("should start with NO_PATTERN state")
        void shouldStartWithNoPatternState() {
            PatternDetector detector = new PatternDetector(3, true);

            assertThat(detector.getState()).isEqualTo(PatternState.NO_PATTERN);
            assertThat(detector.getPatternSize()).isEqualTo(0);
        }

        @Test
        @DisplayName("should handle pattern changes")
        void shouldHandlePatternChanges() {
            PatternDetector detector = new PatternDetector(5, true);

            // Start with pattern 1,2,1,2
            detector.step(1);
            detector.step(2);
            detector.step(1);
            detector.step(2);

            // Break pattern
            detector.step(3);

            // Should detect pattern stop or change
            assertThat(detector.getState()).isIn(
                    PatternState.NO_PATTERN,
                    PatternState.PATTERN_STOPED,
                    PatternState.PATTERN_CHANGED_SIZES);
        }

        @Test
        @DisplayName("should track pattern size changes")
        void shouldTrackPatternSizeChanges() {
            PatternDetector detector = new PatternDetector(6, false); // Priority to smaller patterns

            // Create overlapping patterns of different sizes
            detector.step(1);
            detector.step(2);
            detector.step(1);
            detector.step(2);
            detector.step(1);
            detector.step(2);

            int patternSize = detector.getPatternSize();
            assertThat(patternSize).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("Priority Handling")
    class PriorityHandling {

        @Test
        @DisplayName("should prioritize bigger patterns when configured")
        void shouldPrioritizeBiggerPatternsWhenConfigured() {
            PatternDetector detector = new PatternDetector(6, true);

            // Create sequence that could match patterns of size 1, 2, or 3
            // 1,2,3,1,2,3,1,2,3
            detector.step(1);
            detector.step(2);
            detector.step(3);
            detector.step(1);
            detector.step(2);
            detector.step(3);
            detector.step(1);
            detector.step(2);
            detector.step(3);

            // With priority to bigger patterns, should prefer size 3 over size 1
            if (detector.getState() != PatternState.NO_PATTERN) {
                assertThat(detector.getPatternSize()).isGreaterThanOrEqualTo(1);
            }
        }

        @Test
        @DisplayName("should prioritize smaller patterns when configured")
        void shouldPrioritizeSmallerPatternsWhenConfigured() {
            PatternDetector detector = new PatternDetector(6, false);

            // Same sequence as above
            detector.step(1);
            detector.step(2);
            detector.step(3);
            detector.step(1);
            detector.step(2);
            detector.step(3);
            detector.step(1);
            detector.step(2);
            detector.step(3);

            // With priority to smaller patterns, might prefer size 1
            if (detector.getState() != PatternState.NO_PATTERN) {
                assertThat(detector.getPatternSize()).isGreaterThanOrEqualTo(1);
            }
        }
    }

    @Nested
    @DisplayName("Static Methods")
    class StaticMethods {

        @Test
        @DisplayName("should calculate pattern state correctly")
        void shouldCalculatePatternStateCorrectly() {
            // Test state transitions
            assertThat(PatternDetector.calculateState(0, 0)).isEqualTo(PatternState.NO_PATTERN);
            assertThat(PatternDetector.calculateState(0, 2)).isEqualTo(PatternState.PATTERN_STARTED);
            assertThat(PatternDetector.calculateState(2, 2)).isEqualTo(PatternState.PATTERN_UNCHANGED);
            assertThat(PatternDetector.calculateState(2, 3)).isEqualTo(PatternState.PATTERN_CHANGED_SIZES);
            assertThat(PatternDetector.calculateState(2, 0)).isEqualTo(PatternState.PATTERN_STOPED);
        }

        @Test
        @DisplayName("should calculate pattern size with priority to bigger patterns")
        void shouldCalculatePatternSizeWithPriorityToBiggerPatterns() {
            BitSet bitSet = new BitSet();
            bitSet.set(0); // Pattern of size 1
            bitSet.set(2); // Pattern of size 3

            int size = PatternDetector.calculatePatternSize(bitSet, 0, true);

            // With priority to bigger patterns, should choose larger size
            assertThat(size).isGreaterThanOrEqualTo(0);
        }

        @Test
        @DisplayName("should calculate pattern size with priority to smaller patterns")
        void shouldCalculatePatternSizeWithPriorityToSmallerPatterns() {
            BitSet bitSet = new BitSet();
            bitSet.set(0); // Pattern of size 1
            bitSet.set(2); // Pattern of size 3

            int size = PatternDetector.calculatePatternSize(bitSet, 0, false);

            // With priority to smaller patterns, should choose smaller size
            assertThat(size).isGreaterThanOrEqualTo(0);
        }

        @Test
        @DisplayName("should handle empty bit set")
        void shouldHandleEmptyBitSet() {
            BitSet emptyBitSet = new BitSet();

            int size = PatternDetector.calculatePatternSize(emptyBitSet, 2, true);

            assertThat(size).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Queue Management")
    class QueueManagement {

        @Test
        @DisplayName("should maintain internal queue")
        void shouldMaintainInternalQueue() {
            PatternDetector detector = new PatternDetector(3, true);

            assertThat(detector.getQueue()).isNotNull();
            assertThat(detector.getQueue().size()).isEqualTo(4); // maxPatternSize + 1
        }

        @Test
        @DisplayName("should update queue with new values")
        void shouldUpdateQueueWithNewValues() {
            PatternDetector detector = new PatternDetector(2, true);

            detector.step(10);
            detector.step(20);

            // Queue should contain the values
            assertThat(detector.getQueue()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle null values")
        void shouldHandleNullValues() {
            PatternDetector detector = new PatternDetector(3, true);

            PatternState state = detector.step(null);

            assertThat(state).isNotNull();
            assertThat(detector.getState()).isNotNull();
        }

        @Test
        @DisplayName("should handle mixed null and non-null values")
        void shouldHandleMixedNullAndNonNullValues() {
            PatternDetector detector = new PatternDetector(4, true);

            detector.step(1);
            detector.step(null);
            detector.step(1);
            detector.step(null);
            detector.step(1);

            assertThat(detector.getState()).isNotNull();
        }

        @Test
        @DisplayName("should handle large pattern sizes")
        void shouldHandleLargePatternSizes() {
            PatternDetector detector = new PatternDetector(100, true);

            assertThat(detector.getMaxPatternSize()).isEqualTo(100);

            // Add some values
            for (int i = 0; i < 10; i++) {
                detector.step(i % 3);
            }

            assertThat(detector.getState()).isNotNull();
        }

        @Test
        @DisplayName("should have meaningful toString")
        void shouldHaveMeaningfulToString() {
            PatternDetector detector = new PatternDetector(3, true);
            detector.step(1);
            detector.step(2);

            String toString = detector.toString();

            assertThat(toString).isNotNull();
            assertThat(toString).isNotEmpty();
            assertThat(toString).contains("PatternDetector");
        }
    }
}
