package pt.up.fe.specs.util.jar;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

/**
 * Unit tests for {@link JarParametersUtils}.
 * 
 * Tests the utility class that provides methods for managing parameters
 * when running applications (.jar, .exe) with focus on help requirement
 * detection.
 * 
 * @author Generated Tests
 */
@DisplayName("JarParametersUtils Tests")
class JarParametersUtilsTest {

    @Nested
    @DisplayName("Help Requirement Detection Tests")
    class HelpRequirementDetectionTests {

        @Test
        @DisplayName("Should recognize standard help arguments")
        void testRecognizeStandardHelpArguments() {
            String[] helpArgs = { "-help", "-h", ".?", "/?", "?" };

            for (String helpArg : helpArgs) {
                assertThat(JarParametersUtils.isHelpRequirement(helpArg))
                        .as("Should recognize '%s' as help requirement", helpArg)
                        .isTrue();
            }
        }

        @Test
        @DisplayName("Should not recognize non-help arguments")
        void testNotRecognizeNonHelpArguments() {
            String[] nonHelpArgs = {
                    "help", // without dash
                    "--help", // double dash
                    "-Help", // capital H
                    "-HELP", // all caps
                    "h", // without dash
                    "/help", // slash with help
                    "?help", // question mark with text
                    "-help-me", // additional text
                    "-h1", // with number
                    "help?", // question at end
                    "", // empty string
                    "   ", // whitespace only
                    "file.txt", // regular filename
                    "-v", // other option
                    "--version", // long option
                    "/?help", // combination
                    ".??", // extra question marks
                    "help.", // help with dot
                    "HELP", // caps without dash
                    "Help" // mixed case
            };

            for (String nonHelpArg : nonHelpArgs) {
                assertThat(JarParametersUtils.isHelpRequirement(nonHelpArg))
                        .as("Should not recognize '%s' as help requirement", nonHelpArg)
                        .isFalse();
            }
        }

        @Test
        @DisplayName("Should handle null input gracefully")
        void testHandleNullInput() {
            assertThatThrownBy(() -> JarParametersUtils.isHelpRequirement(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should be case sensitive")
        void testCaseSensitivity() {
            assertThat(JarParametersUtils.isHelpRequirement("-help")).isTrue();
            assertThat(JarParametersUtils.isHelpRequirement("-Help")).isFalse();
            assertThat(JarParametersUtils.isHelpRequirement("-HELP")).isFalse();
            assertThat(JarParametersUtils.isHelpRequirement("-H")).isFalse();
            assertThat(JarParametersUtils.isHelpRequirement("-h")).isTrue();
        }

        @Test
        @DisplayName("Should handle special characters correctly")
        void testSpecialCharacters() {
            // These should be recognized
            assertThat(JarParametersUtils.isHelpRequirement(".?")).isTrue();
            assertThat(JarParametersUtils.isHelpRequirement("/?")).isTrue();
            assertThat(JarParametersUtils.isHelpRequirement("?")).isTrue();

            // These should not be recognized
            assertThat(JarParametersUtils.isHelpRequirement("??")).isFalse();
            assertThat(JarParametersUtils.isHelpRequirement("./")).isFalse();
            assertThat(JarParametersUtils.isHelpRequirement("/")).isFalse();
            assertThat(JarParametersUtils.isHelpRequirement(".")).isFalse();
        }

        @Test
        @DisplayName("Should handle whitespace variations")
        void testWhitespaceVariations() {
            // Exact matches should work
            assertThat(JarParametersUtils.isHelpRequirement("-help")).isTrue();

            // With whitespace should not work
            assertThat(JarParametersUtils.isHelpRequirement(" -help")).isFalse();
            assertThat(JarParametersUtils.isHelpRequirement("-help ")).isFalse();
            assertThat(JarParametersUtils.isHelpRequirement(" -help ")).isFalse();
            assertThat(JarParametersUtils.isHelpRequirement("\t-help")).isFalse();
            assertThat(JarParametersUtils.isHelpRequirement("-help\n")).isFalse();
        }
    }

    @Nested
    @DisplayName("Ask for Help Message Tests")
    class AskForHelpMessageTests {

        @Test
        @DisplayName("Should generate correct help message format")
        void testGenerateCorrectHelpMessageFormat() {
            String jarName = "myapp.jar";
            String helpMessage = JarParametersUtils.askForHelp(jarName);

            assertThat(helpMessage).isEqualTo("for any help > myapp.jar -help");
        }

        @Test
        @DisplayName("Should handle different jar names")
        void testHandleDifferentJarNames() {
            String[] jarNames = {
                    "simple.jar",
                    "my-application.jar",
                    "app_v1.0.jar",
                    "Very Long Application Name.jar",
                    "123.jar",
                    "app.exe",
                    "tool",
                    "utility.bat"
            };

            for (String jarName : jarNames) {
                String helpMessage = JarParametersUtils.askForHelp(jarName);
                assertThat(helpMessage)
                        .as("Help message for jar '%s'", jarName)
                        .startsWith("for any help > ")
                        .contains(jarName)
                        .endsWith(" -help");
            }
        }

        @Test
        @DisplayName("Should handle empty jar name")
        void testHandleEmptyJarName() {
            String helpMessage = JarParametersUtils.askForHelp("");
            assertThat(helpMessage).isEqualTo("for any help >  -help");
        }

        @Test
        @DisplayName("Should handle null jar name")
        void testHandleNullJarName() {
            String helpMessage = JarParametersUtils.askForHelp(null);
            assertThat(helpMessage).isEqualTo("for any help > null -help");
        }

        @Test
        @DisplayName("Should use primary help argument")
        void testUsePrimaryHelpArgument() {
            String jarName = "test.jar";
            String helpMessage = JarParametersUtils.askForHelp(jarName);

            // Should use "-help" (the first element in HELP_ARG array)
            assertThat(helpMessage).endsWith(" -help");
            assertThat(helpMessage).doesNotEndWith(" -h");
            assertThat(helpMessage).doesNotEndWith(" /?");
            assertThat(helpMessage).doesNotEndWith(" ?");
        }

        @Test
        @DisplayName("Should handle special characters in jar name")
        void testHandleSpecialCharactersInJarName() {
            String[] jarNamesWithSpecialChars = {
                    "my app.jar", // space
                    "app@1.0.jar", // at symbol
                    "app#test.jar", // hash
                    "app$ver.jar", // dollar
                    "app%new.jar", // percent
                    "app&tool.jar", // ampersand
                    "app(old).jar", // parentheses
                    "app[new].jar", // brackets
                    "app{test}.jar", // braces
                    "app+tool.jar", // plus
                    "app=1.jar", // equals
                    "app;test.jar", // semicolon
                    "app'quote.jar", // single quote
                    "app\"quote.jar", // double quote
                    "app,test.jar", // comma
                    "app<old.jar", // less than
                    "app>new.jar" // greater than
            };

            for (String jarName : jarNamesWithSpecialChars) {
                String helpMessage = JarParametersUtils.askForHelp(jarName);
                assertThat(helpMessage)
                        .as("Help message for jar with special chars '%s'", jarName)
                        .startsWith("for any help > ")
                        .contains(jarName)
                        .endsWith(" -help");
            }
        }

        @Test
        @DisplayName("Should handle unicode characters in jar name")
        void testHandleUnicodeCharactersInJarName() {
            String[] unicodeJarNames = {
                    "应用程序.jar", // Chinese characters
                    "アプリ.jar", // Japanese characters
                    "приложение.jar", // Russian characters
                    "app🚀.jar", // emoji
                    "café.jar", // accented characters
                    "naïve.jar", // diaeresis
                    "résumé.jar" // multiple accents
            };

            for (String jarName : unicodeJarNames) {
                String helpMessage = JarParametersUtils.askForHelp(jarName);
                assertThat(helpMessage)
                        .as("Help message for unicode jar name '%s'", jarName)
                        .startsWith("for any help > ")
                        .contains(jarName)
                        .endsWith(" -help");
            }
        }
    }

    @Nested
    @DisplayName("Utility Class Tests")
    class UtilityClassTests {

        @Test
        @DisplayName("Should be a utility class with static methods only")
        void testIsUtilityClass() {
            // Verify that all public methods are static
            java.lang.reflect.Method[] methods = JarParametersUtils.class.getDeclaredMethods();

            for (java.lang.reflect.Method method : methods) {
                if (java.lang.reflect.Modifier.isPublic(method.getModifiers())) {
                    assertThat(java.lang.reflect.Modifier.isStatic(method.getModifiers()))
                            .as("Public method '%s' should be static", method.getName())
                            .isTrue();
                }
            }
        }

        @Test
        @DisplayName("Should have a default constructor")
        void testHasDefaultConstructor() {
            // Should be able to instantiate (even though it's a utility class)
            assertThatCode(() -> new JarParametersUtils()).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should have consistent static method behavior")
        void testConsistentStaticMethodBehavior() {
            // Test that multiple calls return consistent results
            String testArg = "-help";
            String jarName = "test.jar";

            // Multiple calls should return same results
            assertThat(JarParametersUtils.isHelpRequirement(testArg)).isTrue();
            assertThat(JarParametersUtils.isHelpRequirement(testArg)).isTrue();
            assertThat(JarParametersUtils.isHelpRequirement(testArg)).isTrue();

            String helpMessage1 = JarParametersUtils.askForHelp(jarName);
            String helpMessage2 = JarParametersUtils.askForHelp(jarName);
            String helpMessage3 = JarParametersUtils.askForHelp(jarName);

            assertThat(helpMessage1).isEqualTo(helpMessage2);
            assertThat(helpMessage2).isEqualTo(helpMessage3);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should handle complete help workflow")
        void testCompleteHelpWorkflow() {
            String jarName = "myapp.jar";
            String[] args = { "-help", "parameter1", "parameter2" };

            // Check if first argument is help request
            boolean isHelpRequested = JarParametersUtils.isHelpRequirement(args[0]);
            assertThat(isHelpRequested).isTrue();

            // Generate help message
            String helpMessage = JarParametersUtils.askForHelp(jarName);
            assertThat(helpMessage).isEqualTo("for any help > myapp.jar -help");
        }

        @Test
        @DisplayName("Should handle non-help workflow")
        void testNonHelpWorkflow() {
            String[] args = { "--input", "file.txt", "--output", "result.txt" };

            // Check that none of the arguments are help requests
            for (String arg : args) {
                boolean isHelpRequested = JarParametersUtils.isHelpRequirement(arg);
                assertThat(isHelpRequested)
                        .as("Argument '%s' should not be recognized as help request", arg)
                        .isFalse();
            }
        }

        @Test
        @DisplayName("Should handle mixed argument scenarios")
        void testMixedArgumentScenarios() {
            String[] scenarios = {
                    "?", // valid help
                    "/?", // valid help
                    "-h", // valid help
                    ".?", // valid help
                    "-help", // valid help
                    "--help", // invalid (double dash)
                    "help", // invalid (no dash)
                    "-version", // invalid (different option)
                    "file.txt", // invalid (filename)
                    "-config=value", // invalid (config option)
                    "/h", // invalid (wrong format)
                    "?help" // invalid (extra text)
            };

            boolean[] expectedResults = {
                    true, true, true, true, true, // valid help args
                    false, false, false, false, false, false, false // invalid args
            };

            for (int i = 0; i < scenarios.length; i++) {
                boolean result = JarParametersUtils.isHelpRequirement(scenarios[i]);
                assertThat(result)
                        .as("Argument '%s' should be %s", scenarios[i],
                                expectedResults[i] ? "recognized" : "not recognized")
                        .isEqualTo(expectedResults[i]);
            }
        }

        @Test
        @DisplayName("Should work with real-world jar names")
        void testRealWorldJarNames() {
            String[] realWorldJarNames = {
                    "spring-boot-starter-2.5.0.jar",
                    "junit-platform-launcher-1.8.0.jar",
                    "slf4j-api-1.7.32.jar",
                    "commons-lang3-3.12.0.jar",
                    "jackson-core-2.13.0.jar",
                    "gson-2.8.8.jar",
                    "guava-30.1-jre.jar",
                    "log4j-core-2.14.1.jar",
                    "hibernate-core-5.6.0.Final.jar",
                    "mockito-core-4.0.0.jar"
            };

            for (String jarName : realWorldJarNames) {
                String helpMessage = JarParametersUtils.askForHelp(jarName);
                assertThat(helpMessage)
                        .as("Help message for real-world jar '%s'", jarName)
                        .startsWith("for any help > ")
                        .contains(jarName)
                        .endsWith(" -help");
            }
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @RetryingTest(5)
        @DisplayName("Should handle large number of help checks efficiently")
        void testLargeNumberOfHelpChecks() {
            String[] testArgs = { "-help", "-h", "?", "/?", ".?", "nothelp", "--help", "file.txt" };

            long startTime = System.currentTimeMillis();

            // Perform many help checks
            for (int i = 0; i < 10000; i++) {
                for (String arg : testArgs) {
                    JarParametersUtils.isHelpRequirement(arg);
                }
            }

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // Should complete within reasonable time (less than 1 second for 80,000
            // operations)
            assertThat(duration).isLessThan(1000);
        }

        @RetryingTest(5)
        @DisplayName("Should handle large number of help message generations efficiently")
        void testLargeNumberOfHelpMessageGenerations() {
            String[] jarNames = { "app1.jar", "app2.jar", "app3.jar", "app4.jar", "app5.jar" };

            long startTime = System.currentTimeMillis();

            // Generate many help messages
            for (int i = 0; i < 10000; i++) {
                for (String jarName : jarNames) {
                    JarParametersUtils.askForHelp(jarName);
                }
            }

            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;

            // Should complete within reasonable time (less than 1 second for 50,000
            // operations)
            assertThat(duration).isLessThan(1000);
        }
    }
}
