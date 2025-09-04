package org.suikasoft.jOptions.arguments;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.AppKernel;

/**
 * Comprehensive test suite for the ArgumentsParser class.
 * Tests argument parsing, flag handling, and application execution.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ArgumentsParser Tests")
class ArgumentsParserTest {

    @Mock
    private AppKernel mockKernel;

    private ArgumentsParser parser;
    private DataKey<Boolean> testBoolKey;
    private DataKey<String> testStringKey;
    private DataKey<Integer> testIntegerKey;

    @BeforeEach
    void setUp() {
        parser = new ArgumentsParser();
        testBoolKey = KeyFactory.bool("test_bool_key").setLabel("Test boolean flag");
        testStringKey = KeyFactory.string("test_string_key").setLabel("Test string option");
        testIntegerKey = KeyFactory.integer("test_integer_key").setLabel("Test integer option");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create parser with default help flags")
        void testConstructor_CreatesParserWithHelpFlags() {
            // given
            ArgumentsParser newParser = new ArgumentsParser();
            List<String> helpArgs = Arrays.asList("--help");

            // when
            DataStore result = newParser.parse(helpArgs);

            // then
            assertThat(result).isNotNull();
            // Help flag should be parsed successfully (no exception thrown)
        }

        @Test
        @DisplayName("Should create parser with empty configuration")
        void testConstructor_CreatesEmptyParser() {
            // given
            ArgumentsParser newParser = new ArgumentsParser();
            List<String> emptyArgs = Collections.emptyList();

            // when
            DataStore result = newParser.parse(emptyArgs);

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("Boolean Flag Tests")
    class BooleanFlagTests {

        @Test
        @DisplayName("Should add and parse boolean flag")
        void testAddBool_WithSingleFlag_ParsesCorrectly() {
            // given
            parser.addBool(testBoolKey, "--verbose");
            List<String> args = Arrays.asList("--verbose");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(testBoolKey)).isTrue();
        }

        @Test
        @DisplayName("Should add and parse multiple boolean flags")
        void testAddBool_WithMultipleFlags_ParsesCorrectly() {
            // given
            parser.addBool(testBoolKey, "--verbose", "-v");
            List<String> args1 = Arrays.asList("--verbose");
            List<String> args2 = Arrays.asList("-v");

            // when
            DataStore result1 = parser.parse(args1);
            DataStore result2 = parser.parse(args2);

            // then
            assertThat(result1.get(testBoolKey)).isTrue();
            assertThat(result2.get(testBoolKey)).isTrue();
        }

        @Test
        @DisplayName("Should not set boolean flag when not present")
        void testAddBool_FlagNotPresent_NotSet() {
            // given
            parser.addBool(testBoolKey, "--verbose");
            List<String> args = Collections.emptyList();

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.hasValue(testBoolKey)).isFalse();
        }

        @Test
        @DisplayName("Should throw exception for duplicate flags")
        void testAddBool_DuplicateFlag_ThrowsException() {
            // given
            parser.addBool(testBoolKey, "--verbose");
            DataKey<Boolean> anotherKey = KeyFactory.bool("another_key");

            // when & then
            assertThatThrownBy(() -> parser.addBool(anotherKey, "--verbose"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("There is already a mapping for flag '--verbose'");
        }
    }

    @Nested
    @DisplayName("String Argument Tests")
    class StringArgumentTests {

        @Test
        @DisplayName("Should add and parse string argument")
        void testAddString_WithArgument_ParsesCorrectly() {
            // given
            parser.addString(testStringKey, "--file");
            List<String> args = Arrays.asList("--file", "test.txt");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(testStringKey)).isEqualTo("test.txt");
        }

        @Test
        @DisplayName("Should add and parse multiple string flags")
        void testAddString_WithMultipleFlags_ParsesCorrectly() {
            // given
            parser.addString(testStringKey, "--file", "-f");
            List<String> args1 = Arrays.asList("--file", "test1.txt");
            List<String> args2 = Arrays.asList("-f", "test2.txt");

            // when
            DataStore result1 = parser.parse(args1);
            DataStore result2 = parser.parse(args2);

            // then
            assertThat(result1.get(testStringKey)).isEqualTo("test1.txt");
            assertThat(result2.get(testStringKey)).isEqualTo("test2.txt");
        }

        @Test
        @DisplayName("Should handle string arguments with spaces")
        void testAddString_WithSpaces_ParsesCorrectly() {
            // given
            parser.addString(testStringKey, "--message");
            List<String> args = Arrays.asList("--message", "hello world");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(testStringKey)).isEqualTo("hello world");
        }

        @Test
        @DisplayName("Should handle empty string arguments")
        void testAddString_WithEmptyString_ParsesCorrectly() {
            // given
            parser.addString(testStringKey, "--empty");
            List<String> args = Arrays.asList("--empty", "");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(testStringKey)).isEmpty();
        }
    }

    @Nested
    @DisplayName("Generic Key Tests")
    class GenericKeyTests {

        @Test
        @DisplayName("Should add and parse integer key with decoder")
        void testAdd_WithIntegerKey_ParsesCorrectly() {
            // given
            parser.add(testIntegerKey, "--number");
            List<String> args = Arrays.asList("--number", "42");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(testIntegerKey)).isEqualTo(42);
        }

        @Test
        @DisplayName("Should handle custom parser with multiple arguments")
        void testAdd_WithCustomParser_ParsesCorrectly() {
            // given
            DataKey<String> customKey = KeyFactory.string("custom_key");
            parser.add(customKey, args -> args.popSingle() + "-" + args.popSingle(), 2, "--combine");
            List<String> args = Arrays.asList("--combine", "hello", "world");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(customKey)).isEqualTo("hello-world");
        }

        @Test
        @DisplayName("Should handle custom parser with no arguments")
        void testAdd_WithNoArgumentParser_ParsesCorrectly() {
            // given
            DataKey<String> noArgKey = KeyFactory.string("no_arg_key");
            parser.add(noArgKey, args -> "constant", 0, "--constant");
            List<String> args = Arrays.asList("--constant");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(noArgKey)).isEqualTo("constant");
        }

        @Test
        @DisplayName("Should automatically detect boolean keys")
        void testAdd_WithBooleanKey_UsesBooleanParser() {
            // given
            DataKey<Boolean> autoBoolKey = KeyFactory.bool("auto_bool");
            parser.add(autoBoolKey, "--auto-bool");
            List<String> args = Arrays.asList("--auto-bool");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(autoBoolKey)).isTrue();
        }

        @Test
        @DisplayName("Should automatically detect string keys")
        void testAdd_WithStringKey_UsesStringParser() {
            // given
            DataKey<String> autoStringKey = KeyFactory.string("auto_string");
            parser.add(autoStringKey, "--auto-string");
            List<String> args = Arrays.asList("--auto-string", "value");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(autoStringKey)).isEqualTo("value");
        }
    }

    @Nested
    @DisplayName("Ignore Flags Tests")
    class IgnoreFlagsTests {

        @Test
        @DisplayName("Should ignore default comment flag")
        void testParse_WithCommentFlag_IgnoresFlag() {
            // given
            List<String> args = Arrays.asList("//", "this is a comment");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result).isNotNull();
            // Should not throw exception and should ignore both arguments
        }

        @Test
        @DisplayName("Should add and ignore custom flags")
        void testAddIgnore_WithCustomFlag_IgnoresFlag() {
            // given
            parser.addIgnore("--ignore-me", "-i");
            List<String> args = Arrays.asList("--ignore-me", "ignored", "-i", "also-ignored");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result).isNotNull();
            // Should not throw exception and should ignore all arguments
        }

        @Test
        @DisplayName("Should ignore multiple custom flags")
        void testAddIgnore_WithMultipleFlags_IgnoresAllFlags() {
            // given
            parser.addIgnore("--skip1", "--skip2", "--skip3");
            List<String> args = Arrays.asList("--skip1", "value1", "--skip2", "value2", "--skip3", "value3");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("Parse Method Tests")
    class ParseMethodTests {

        @Test
        @DisplayName("Should parse empty arguments")
        void testParse_EmptyArguments_ReturnsEmptyDataStore() {
            // given
            List<String> args = Collections.emptyList();

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should parse mixed argument types")
        void testParse_MixedArguments_ParsesAllCorrectly() {
            // given
            parser.addBool(testBoolKey, "--verbose")
                    .addString(testStringKey, "--file")
                    .add(testIntegerKey, "--count");
            List<String> args = Arrays.asList("--verbose", "--file", "test.txt", "--count", "10");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(testBoolKey)).isTrue();
            assertThat(result.get(testStringKey)).isEqualTo("test.txt");
            assertThat(result.get(testIntegerKey)).isEqualTo(10);
        }

        @Test
        @DisplayName("Should handle unsupported options gracefully")
        void testParse_UnsupportedOption_LogsMessage() {
            // given
            List<String> args = Arrays.asList("--unsupported");

            // when & then
            // Should not throw exception, but log warning message
            assertThatCode(() -> parser.parse(args)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle arguments in different order")
        void testParse_DifferentOrder_ParsesCorrectly() {
            // given
            parser.addBool(testBoolKey, "--verbose")
                    .addString(testStringKey, "--file");
            List<String> args = Arrays.asList("--file", "test.txt", "--verbose");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(testBoolKey)).isTrue();
            assertThat(result.get(testStringKey)).isEqualTo("test.txt");
        }
    }

    @Nested
    @DisplayName("Execute Method Tests")
    class ExecuteMethodTests {

        @Test
        @DisplayName("Should execute kernel with parsed arguments")
        void testExecute_WithValidArguments_ExecutesKernel() {
            // given
            when(mockKernel.execute(any(DataStore.class))).thenReturn(0);
            parser.addBool(testBoolKey, "--verbose");
            List<String> args = Arrays.asList("--verbose");

            // when
            int result = parser.execute(mockKernel, args);

            // then
            assertThat(result).isEqualTo(0);
            verify(mockKernel).execute(any(DataStore.class));
        }

        @Test
        @DisplayName("Should return help without executing kernel")
        void testExecute_WithHelpFlag_ShowsHelpWithoutExecuting() {
            // given
            List<String> args = Arrays.asList("--help");

            // when
            int result = parser.execute(mockKernel, args);

            // then
            assertThat(result).isEqualTo(0);
            verify(mockKernel, never()).execute(any(DataStore.class));
        }

        @Test
        @DisplayName("Should return help with short flag")
        void testExecute_WithShortHelpFlag_ShowsHelpWithoutExecuting() {
            // given
            List<String> args = Arrays.asList("-h");

            // when
            int result = parser.execute(mockKernel, args);

            // then
            assertThat(result).isEqualTo(0);
            verify(mockKernel, never()).execute(any(DataStore.class));
        }

        @Test
        @DisplayName("Should propagate kernel exit code")
        void testExecute_KernelReturnsErrorCode_PropagatesErrorCode() {
            // given
            when(mockKernel.execute(any(DataStore.class))).thenReturn(1);
            List<String> args = Collections.emptyList();

            // when
            int result = parser.execute(mockKernel, args);

            // then
            assertThat(result).isEqualTo(1);
            verify(mockKernel).execute(any(DataStore.class));
        }

        @Test
        @DisplayName("Should execute with complex arguments")
        void testExecute_WithComplexArguments_ExecutesCorrectly() {
            // given
            when(mockKernel.execute(any(DataStore.class))).thenReturn(42);
            parser.addBool(testBoolKey, "--verbose")
                    .addString(testStringKey, "--output")
                    .add(testIntegerKey, "--threads");
            List<String> args = Arrays.asList("--verbose", "--output", "result.txt", "--threads", "4");

            // when
            int result = parser.execute(mockKernel, args);

            // then
            assertThat(result).isEqualTo(42);
            verify(mockKernel).execute(argThat(dataStore -> dataStore.get(testBoolKey) == true &&
                    "result.txt".equals(dataStore.get(testStringKey)) &&
                    dataStore.get(testIntegerKey) == 4));
        }
    }

    @Nested
    @DisplayName("Method Chaining Tests")
    class MethodChainingTests {

        @Test
        @DisplayName("Should support method chaining for adding flags")
        void testMethodChaining_AddingFlags_ReturnsParser() {
            // when
            ArgumentsParser result = parser
                    .addBool(testBoolKey, "--verbose")
                    .addString(testStringKey, "--file")
                    .add(testIntegerKey, "--count")
                    .addIgnore("--skip");

            // then
            assertThat(result).isSameAs(parser);
        }

        @Test
        @DisplayName("Should work correctly after method chaining")
        void testMethodChaining_AfterChaining_WorksCorrectly() {
            // given
            parser.addBool(testBoolKey, "--verbose")
                    .addString(testStringKey, "--file")
                    .add(testIntegerKey, "--count");
            List<String> args = Arrays.asList("--verbose", "--file", "test.txt", "--count", "5");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(testBoolKey)).isTrue();
            assertThat(result.get(testStringKey)).isEqualTo("test.txt");
            assertThat(result.get(testIntegerKey)).isEqualTo(5);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesAndErrorConditions {

        @Test
        @DisplayName("Should handle null argument list")
        void testParse_WithNullArguments_ThrowsException() {
            // when & then
            assertThatThrownBy(() -> parser.parse(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle missing argument for string flag")
        void testParse_MissingStringArgument_ThrowsException() {
            // given
            parser.addString(testStringKey, "--file");
            List<String> args = Arrays.asList("--file");

            // when & then
            assertThatThrownBy(() -> parser.parse(args))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should decode invalid integer argument as 0 (default)")
        void testParse_InvalidIntegerArgument_DecodesToDefault() {
            // given
            parser.add(testIntegerKey, "--number");
            List<String> args = Arrays.asList("--number", "not-a-number");

            // when
            DataStore result = parser.parse(args);

            // then
            assertThat(result.get(testIntegerKey)).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle empty flag strings")
        void testAdd_WithEmptyFlag_AcceptsEmptyFlag() {
            // given
            DataKey<Boolean> emptyFlagKey = KeyFactory.bool("empty_flag");

            // when & then
            assertThatCode(() -> parser.addBool(emptyFlagKey, ""))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with complete argument parsing workflow")
        void testCompleteWorkflow_RealScenario_WorksCorrectly() {
            // given
            when(mockKernel.execute(any(DataStore.class))).thenReturn(0);

            DataKey<Boolean> verboseKey = KeyFactory.bool("verbose").setLabel("Enable verbose output");
            DataKey<String> inputKey = KeyFactory.string("input").setLabel("Input file path");
            DataKey<String> outputKey = KeyFactory.string("output").setLabel("Output file path");
            DataKey<Integer> threadsKey = KeyFactory.integer("threads").setLabel("Number of threads");

            parser.addBool(verboseKey, "--verbose", "-v")
                    .addString(inputKey, "--input", "-i")
                    .addString(outputKey, "--output", "-o")
                    .add(threadsKey, "--threads", "-t")
                    .addIgnore("--debug");

            List<String> args = Arrays.asList(
                    "--verbose",
                    "--input", "source.txt",
                    "--output", "result.txt",
                    "--threads", "8",
                    "--debug", "full");

            // when
            int result = parser.execute(mockKernel, args);

            // then
            assertThat(result).isEqualTo(0);
            verify(mockKernel).execute(argThat(dataStore -> dataStore.get(verboseKey) == true &&
                    "source.txt".equals(dataStore.get(inputKey)) &&
                    "result.txt".equals(dataStore.get(outputKey)) &&
                    dataStore.get(threadsKey) == 8));
        }

        @Test
        @DisplayName("Should handle help message generation")
        void testHelpMessage_WithMultipleFlags_GeneratesCorrectly() {
            // given
            DataKey<Boolean> verboseKey = KeyFactory.bool("verbose").setLabel("Enable verbose output");
            DataKey<String> fileKey = KeyFactory.string("file").setLabel("Input file");

            parser.addBool(verboseKey, "--verbose", "-v")
                    .addString(fileKey, "--file", "-f");

            List<String> args = Arrays.asList("--help");

            // when & then
            assertThatCode(() -> parser.execute(mockKernel, args))
                    .doesNotThrowAnyException();
        }
    }
}
