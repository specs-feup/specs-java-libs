package org.suikasoft.jOptions.Options;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for MultipleChoice functionality.
 * Tests choice management, alias support, and selection behavior.
 * 
 * @author Generated Tests
 */
@DisplayName("MultipleChoice")
class MultipleChoiceTest {

    @Nested
    @DisplayName("Factory Methods")
    class FactoryMethodsTests {

        @Test
        @DisplayName("newInstance creates MultipleChoice with choices")
        void testNewInstance_CreatesMultipleChoiceWithChoices() {
            List<String> choices = Arrays.asList("option1", "option2", "option3");

            MultipleChoice mc = MultipleChoice.newInstance(choices);

            assertThat(mc).isNotNull();
            assertThat(mc.getChoice()).isEqualTo("option1"); // First choice is default
        }

        @Test
        @DisplayName("newInstance with aliases creates MultipleChoice with alias support")
        void testNewInstance_WithAliases_CreatesMultipleChoiceWithAliasSupport() {
            List<String> choices = Arrays.asList("verbose", "quiet", "debug");
            Map<String, String> aliases = new HashMap<>();
            aliases.put("v", "verbose");
            aliases.put("q", "quiet");
            aliases.put("d", "debug");

            MultipleChoice mc = MultipleChoice.newInstance(choices, aliases);

            assertThat(mc).isNotNull();
            assertThat(mc.getChoice()).isEqualTo("verbose"); // First choice is default
        }

        @Test
        @DisplayName("newInstance throws exception for empty choices")
        void testNewInstance_ThrowsExceptionForEmptyChoices() {
            List<String> emptyChoices = Collections.emptyList();

            assertThatThrownBy(() -> MultipleChoice.newInstance(emptyChoices))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("MultipleChoice needs at least one choice, passed an empty list.");
        }

        @Test
        @DisplayName("newInstance with empty aliases works correctly")
        void testNewInstance_WithEmptyAliases_WorksCorrectly() {
            List<String> choices = Arrays.asList("choice1", "choice2");
            Map<String, String> emptyAliases = Collections.emptyMap();

            MultipleChoice mc = MultipleChoice.newInstance(choices, emptyAliases);

            assertThat(mc.getChoice()).isEqualTo("choice1");
        }
    }

    @Nested
    @DisplayName("Choice Selection")
    class ChoiceSelectionTests {

        @Test
        @DisplayName("setChoice changes current choice")
        void testSetChoice_ChangesCurrentChoice() {
            List<String> choices = Arrays.asList("first", "second", "third");
            MultipleChoice mc = MultipleChoice.newInstance(choices);

            MultipleChoice result = mc.setChoice("second");

            assertThat(result).isSameAs(mc); // Returns self for chaining
            assertThat(mc.getChoice()).isEqualTo("second");
        }

        @Test
        @DisplayName("setChoice with invalid choice returns unchanged instance")
        void testSetChoice_WithInvalidChoice_ReturnsUnchangedInstance() {
            List<String> choices = Arrays.asList("valid1", "valid2");
            MultipleChoice mc = MultipleChoice.newInstance(choices);
            String originalChoice = mc.getChoice();

            MultipleChoice result = mc.setChoice("invalid");

            assertThat(result).isSameAs(mc);
            assertThat(mc.getChoice()).isEqualTo(originalChoice); // Should remain unchanged
        }

        @Test
        @DisplayName("getChoice returns current choice")
        void testGetChoice_ReturnsCurrentChoice() {
            List<String> choices = Arrays.asList("alpha", "beta", "gamma");
            MultipleChoice mc = MultipleChoice.newInstance(choices);

            assertThat(mc.getChoice()).isEqualTo("alpha"); // Default first choice

            mc.setChoice("gamma");
            assertThat(mc.getChoice()).isEqualTo("gamma");

            mc.setChoice("beta");
            assertThat(mc.getChoice()).isEqualTo("beta");
        }

        @Test
        @DisplayName("default choice is first in list")
        void testDefaultChoice_IsFirstInList() {
            List<String> choices = Arrays.asList("default", "other1", "other2");

            MultipleChoice mc = MultipleChoice.newInstance(choices);

            assertThat(mc.getChoice()).isEqualTo("default");
        }
    }

    @Nested
    @DisplayName("Alias Support")
    class AliasSupportTests {

        @Test
        @DisplayName("setChoice works with aliases")
        void testSetChoice_WorksWithAliases() {
            List<String> choices = Arrays.asList("enable", "disable");
            Map<String, String> aliases = new HashMap<>();
            aliases.put("on", "enable");
            aliases.put("off", "disable");
            aliases.put("1", "enable");
            aliases.put("0", "disable");

            MultipleChoice mc = MultipleChoice.newInstance(choices, aliases);

            mc.setChoice("on");
            assertThat(mc.getChoice()).isEqualTo("enable");

            mc.setChoice("off");
            assertThat(mc.getChoice()).isEqualTo("disable");

            mc.setChoice("1");
            assertThat(mc.getChoice()).isEqualTo("enable");

            mc.setChoice("0");
            assertThat(mc.getChoice()).isEqualTo("disable");
        }

        @Test
        @DisplayName("both original choices and aliases work")
        void testBothOriginalChoicesAndAliases_Work() {
            List<String> choices = Arrays.asList("red", "green", "blue");
            Map<String, String> aliases = new HashMap<>();
            aliases.put("r", "red");
            aliases.put("g", "green");
            aliases.put("b", "blue");

            MultipleChoice mc = MultipleChoice.newInstance(choices, aliases);

            // Test original choices
            mc.setChoice("red");
            assertThat(mc.getChoice()).isEqualTo("red");

            mc.setChoice("green");
            assertThat(mc.getChoice()).isEqualTo("green");

            // Test aliases
            mc.setChoice("r");
            assertThat(mc.getChoice()).isEqualTo("red");

            mc.setChoice("b");
            assertThat(mc.getChoice()).isEqualTo("blue");
        }

        @Test
        @DisplayName("alias pointing to non-existent choice is ignored")
        void testAlias_PointingToNonExistentChoice_IsIgnored() {
            List<String> choices = Arrays.asList("existing1", "existing2");
            Map<String, String> aliases = new HashMap<>();
            aliases.put("validAlias", "existing1");
            aliases.put("invalidAlias", "nonExistent");

            MultipleChoice mc = MultipleChoice.newInstance(choices, aliases);

            // Valid alias should work
            mc.setChoice("validAlias");
            assertThat(mc.getChoice()).isEqualTo("existing1");

            // Invalid alias should be ignored and not change current choice
            String currentChoice = mc.getChoice();
            mc.setChoice("invalidAlias");
            assertThat(mc.getChoice()).isEqualTo(currentChoice); // Should remain unchanged
        }

        @Test
        @DisplayName("multiple aliases can point to same choice")
        void testMultipleAliases_CanPointToSameChoice() {
            List<String> choices = Arrays.asList("yes", "no");
            Map<String, String> aliases = new HashMap<>();
            aliases.put("y", "yes");
            aliases.put("true", "yes");
            aliases.put("1", "yes");
            aliases.put("n", "no");
            aliases.put("false", "no");
            aliases.put("0", "no");

            MultipleChoice mc = MultipleChoice.newInstance(choices, aliases);

            // All aliases for "yes"
            mc.setChoice("y");
            assertThat(mc.getChoice()).isEqualTo("yes");

            mc.setChoice("true");
            assertThat(mc.getChoice()).isEqualTo("yes");

            mc.setChoice("1");
            assertThat(mc.getChoice()).isEqualTo("yes");

            // All aliases for "no"
            mc.setChoice("n");
            assertThat(mc.getChoice()).isEqualTo("no");

            mc.setChoice("false");
            assertThat(mc.getChoice()).isEqualTo("no");

            mc.setChoice("0");
            assertThat(mc.getChoice()).isEqualTo("no");
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {

        @Test
        @DisplayName("toString returns current choice")
        void testToString_ReturnsCurrentChoice() {
            List<String> choices = Arrays.asList("option1", "option2", "option3");
            MultipleChoice mc = MultipleChoice.newInstance(choices);

            assertThat(mc.toString()).isEqualTo("option1");

            mc.setChoice("option3");
            assertThat(mc.toString()).isEqualTo("option3");
        }

        @Test
        @DisplayName("toString works with aliases")
        void testToString_WorksWithAliases() {
            List<String> choices = Arrays.asList("full", "abbreviated");
            Map<String, String> aliases = new HashMap<>();
            aliases.put("f", "full");
            aliases.put("a", "abbreviated");

            MultipleChoice mc = MultipleChoice.newInstance(choices, aliases);

            mc.setChoice("f"); // Using alias
            assertThat(mc.toString()).isEqualTo("full"); // Returns actual choice, not alias
        }
    }

    @Nested
    @DisplayName("Method Chaining")
    class MethodChainingTests {

        @Test
        @DisplayName("setChoice returns self for method chaining")
        void testSetChoice_ReturnsSelfForMethodChaining() {
            List<String> choices = Arrays.asList("a", "b", "c");
            MultipleChoice mc = MultipleChoice.newInstance(choices);

            MultipleChoice result = mc.setChoice("b").setChoice("c").setChoice("a");

            assertThat(result).isSameAs(mc);
            assertThat(mc.getChoice()).isEqualTo("a");
        }

        @Test
        @DisplayName("invalid setChoice still returns self")
        void testInvalidSetChoice_StillReturnsSelf() {
            List<String> choices = Arrays.asList("valid");
            MultipleChoice mc = MultipleChoice.newInstance(choices);

            MultipleChoice result = mc.setChoice("invalid");

            assertThat(result).isSameAs(mc);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Complex Scenarios")
    class EdgeCasesAndComplexScenariosTests {

        @Test
        @DisplayName("single choice works correctly")
        void testSingleChoice_WorksCorrectly() {
            List<String> singleChoice = Arrays.asList("onlyOption");

            MultipleChoice mc = MultipleChoice.newInstance(singleChoice);

            assertThat(mc.getChoice()).isEqualTo("onlyOption");
            assertThat(mc.toString()).isEqualTo("onlyOption");

            // Setting to same choice should work
            mc.setChoice("onlyOption");
            assertThat(mc.getChoice()).isEqualTo("onlyOption");
        }

        @Test
        @DisplayName("choices with special characters work")
        void testChoicesWithSpecialCharacters_Work() {
            List<String> choices = Arrays.asList("choice-1", "choice_2", "choice.3", "choice with spaces");

            MultipleChoice mc = MultipleChoice.newInstance(choices);

            mc.setChoice("choice-1");
            assertThat(mc.getChoice()).isEqualTo("choice-1");

            mc.setChoice("choice_2");
            assertThat(mc.getChoice()).isEqualTo("choice_2");

            mc.setChoice("choice.3");
            assertThat(mc.getChoice()).isEqualTo("choice.3");

            mc.setChoice("choice with spaces");
            assertThat(mc.getChoice()).isEqualTo("choice with spaces");
        }

        @Test
        @DisplayName("case sensitive choice matching")
        void testCaseSensitiveChoiceMatching() {
            List<String> choices = Arrays.asList("Lower", "UPPER", "MiXeD");

            MultipleChoice mc = MultipleChoice.newInstance(choices);

            mc.setChoice("Lower");
            assertThat(mc.getChoice()).isEqualTo("Lower");

            // Case mismatch should not work
            String currentChoice = mc.getChoice();
            mc.setChoice("lower");
            assertThat(mc.getChoice()).isEqualTo(currentChoice); // Should remain unchanged

            mc.setChoice("UPPER");
            assertThat(mc.getChoice()).isEqualTo("UPPER");
        }

        @Test
        @DisplayName("null choice handling")
        void testNullChoiceHandling() {
            List<String> choices = Arrays.asList("option1", "option2");
            MultipleChoice mc = MultipleChoice.newInstance(choices);
            String originalChoice = mc.getChoice();

            // Setting null choice should not change current choice
            mc.setChoice(null);
            assertThat(mc.getChoice()).isEqualTo(originalChoice);
        }

        @Test
        @DisplayName("empty string choice handling")
        void testEmptyStringChoiceHandling() {
            List<String> choices = Arrays.asList("", "option2");

            MultipleChoice mc = MultipleChoice.newInstance(choices);

            // Empty string is a valid choice if included in choices list
            assertThat(mc.getChoice()).isEqualTo(""); // First choice is empty string

            mc.setChoice("option2");
            assertThat(mc.getChoice()).isEqualTo("option2");

            mc.setChoice(""); // Should work since empty string is in choices
            assertThat(mc.getChoice()).isEqualTo("");
        }

        @Test
        @DisplayName("large number of choices handled efficiently")
        void testLargeNumberOfChoices_HandledEfficiently() {
            List<String> manyChoices = new java.util.ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                manyChoices.add("choice" + i);
            }

            MultipleChoice mc = MultipleChoice.newInstance(manyChoices);

            assertThat(mc.getChoice()).isEqualTo("choice0");

            mc.setChoice("choice500");
            assertThat(mc.getChoice()).isEqualTo("choice500");

            mc.setChoice("choice999");
            assertThat(mc.getChoice()).isEqualTo("choice999");
        }
    }

    @Nested
    @DisplayName("Integration and Usage Patterns")
    class IntegrationAndUsagePatternsTests {

        @Test
        @DisplayName("common configuration pattern works")
        void testCommonConfigurationPattern_Works() {
            // Simulating a typical configuration use case
            List<String> logLevels = Arrays.asList("ERROR", "WARN", "INFO", "DEBUG", "TRACE");
            Map<String, String> logAliases = new HashMap<>();
            logAliases.put("0", "ERROR");
            logAliases.put("1", "WARN");
            logAliases.put("2", "INFO");
            logAliases.put("3", "DEBUG");
            logAliases.put("4", "TRACE");
            logAliases.put("quiet", "ERROR");
            logAliases.put("verbose", "DEBUG");

            MultipleChoice logLevel = MultipleChoice.newInstance(logLevels, logAliases);

            // Default behavior
            assertThat(logLevel.getChoice()).isEqualTo("ERROR");

            // Use numeric aliases
            logLevel.setChoice("2");
            assertThat(logLevel.getChoice()).isEqualTo("INFO");

            // Use named aliases
            logLevel.setChoice("verbose");
            assertThat(logLevel.getChoice()).isEqualTo("DEBUG");

            // Use original choice names
            logLevel.setChoice("TRACE");
            assertThat(logLevel.getChoice()).isEqualTo("TRACE");
        }

        @Test
        @DisplayName("multiple MultipleChoice instances are independent")
        void testMultipleMultipleChoiceInstances_AreIndependent() {
            List<String> choices1 = Arrays.asList("opt1", "opt2");
            List<String> choices2 = Arrays.asList("optA", "optB");

            MultipleChoice mc1 = MultipleChoice.newInstance(choices1);
            MultipleChoice mc2 = MultipleChoice.newInstance(choices2);

            mc1.setChoice("opt2");
            mc2.setChoice("optB");

            assertThat(mc1.getChoice()).isEqualTo("opt2");
            assertThat(mc2.getChoice()).isEqualTo("optB");

            // Changes to one should not affect the other
            mc1.setChoice("opt1");
            assertThat(mc1.getChoice()).isEqualTo("opt1");
            assertThat(mc2.getChoice()).isEqualTo("optB"); // Should remain unchanged
        }
    }
}
