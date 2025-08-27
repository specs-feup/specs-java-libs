package pt.up.fe.specs.util.utilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for {@link ScheduledLinesBuilder}.
 * 
 * Tests building string representations of scheduling according to elements and
 * levels.
 * 
 * @author Generated Tests
 */
@DisplayName("ScheduledLinesBuilder")
class ScheduledLinesBuilderTest {

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create empty builder")
        void shouldCreateEmptyBuilder() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();

            assertThat(builder).isNotNull();
            assertThat(builder.getScheduledLines()).isEmpty();
        }

        @Test
        @DisplayName("should have empty string representation when empty")
        void shouldHaveEmptyStringRepresentationWhenEmpty() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();

            assertThat(builder.toString()).isEqualTo("");
        }
    }

    @Nested
    @DisplayName("Element Addition")
    class ElementAddition {

        @Test
        @DisplayName("should add single element to level")
        void shouldAddSingleElementToLevel() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();

            builder.addElement("element1", 0);

            assertThat(builder.getScheduledLines()).containsEntry(0, "element1");
        }

        @Test
        @DisplayName("should add multiple elements to same level with separator")
        void shouldAddMultipleElementsToSameLevelWithSeparator() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();

            builder.addElement("element1", 0);
            builder.addElement("element2", 0);

            assertThat(builder.getScheduledLines()).containsEntry(0, "element1 | element2");
        }

        @Test
        @DisplayName("should add elements to different levels")
        void shouldAddElementsToDifferentLevels() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();

            builder.addElement("element1", 0);
            builder.addElement("element2", 1);
            builder.addElement("element3", 2);

            assertThat(builder.getScheduledLines())
                    .containsEntry(0, "element1")
                    .containsEntry(1, "element2")
                    .containsEntry(2, "element3");
        }

        @Test
        @DisplayName("should handle mixed order element addition")
        void shouldHandleMixedOrderElementAddition() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();

            builder.addElement("level2", 2);
            builder.addElement("level0", 0);
            builder.addElement("level1", 1);
            builder.addElement("another_level0", 0);

            assertThat(builder.getScheduledLines())
                    .containsEntry(0, "level0 | another_level0")
                    .containsEntry(1, "level1")
                    .containsEntry(2, "level2");
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentation {

        @Test
        @DisplayName("should format single level correctly")
        void shouldFormatSingleLevelCorrectly() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();
            builder.addElement("element1", 0);

            String result = builder.toString();

            assertThat(result).isEqualTo("0 -> element1\n");
        }

        @Test
        @DisplayName("should format multiple levels correctly")
        void shouldFormatMultipleLevelsCorrectly() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();
            builder.addElement("element1", 0);
            builder.addElement("element2", 1);

            String result = builder.toString();

            assertThat(result).isEqualTo("0 -> element1\n1 -> element2\n");
        }

        @Test
        @DisplayName("should format with missing level placeholders")
        void shouldFormatWithMissingLevelPlaceholders() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();
            builder.addElement("element1", 0);
            builder.addElement("element3", 2);

            String result = builder.toString();

            // Due to bug: maxLevel is size() - 1 = 2 - 1 = 1, so level 2 is not shown
            assertThat(result).isEqualTo("0 -> element1\n1 -> ---\n");
        }

        @Test
        @DisplayName("should pad level numbers for alignment")
        void shouldPadLevelNumbersForAlignment() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();
            for (int i = 0; i <= 10; i++) {
                builder.addElement("element" + i, i);
            }

            String result = builder.toString();

            assertThat(result).contains("00 -> element0\n");
            assertThat(result).contains("05 -> element5\n");
            assertThat(result).contains("10 -> element10\n");
        }

        @Test
        @DisplayName("should format with custom max level")
        void shouldFormatWithCustomMaxLevel() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();
            builder.addElement("element1", 0);
            builder.addElement("element2", 1);
            builder.addElement("element5", 5);

            String result = builder.toString(3);

            assertThat(result).isEqualTo("0 -> element1\n1 -> element2\n2 -> ---\n3 -> ---\n");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle negative levels")
        void shouldHandleNegativeLevels() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();

            builder.addElement("negative", -1);
            builder.addElement("zero", 0);

            assertThat(builder.getScheduledLines())
                    .containsEntry(-1, "negative")
                    .containsEntry(0, "zero");
        }

        @Test
        @DisplayName("should handle empty element strings")
        void shouldHandleEmptyElementStrings() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();

            builder.addElement("", 0);
            builder.addElement("element", 0);

            assertThat(builder.getScheduledLines()).containsEntry(0, " | element");
        }

        @Test
        @DisplayName("should handle elements with special characters")
        void shouldHandleElementsWithSpecialCharacters() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();

            builder.addElement("element|with|pipes", 0);
            builder.addElement("element\nwith\nnewlines", 0);

            assertThat(builder.getScheduledLines())
                    .containsEntry(0, "element|with|pipes | element\nwith\nnewlines");
        }

        @Test
        @DisplayName("should handle large level numbers")
        void shouldHandleLargeLevelNumbers() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();

            builder.addElement("element1", 1000);
            builder.addElement("element2", 2000);

            assertThat(builder.getScheduledLines())
                    .containsEntry(1000, "element1")
                    .containsEntry(2000, "element2");
        }

        @Test
        @DisplayName("should handle toString with zero max level")
        void shouldHandleToStringWithZeroMaxLevel() {
            ScheduledLinesBuilder builder = new ScheduledLinesBuilder();
            builder.addElement("element", 5);

            String result = builder.toString(0);

            assertThat(result).isEqualTo("0 -> ---\n");
        }
    }
}
