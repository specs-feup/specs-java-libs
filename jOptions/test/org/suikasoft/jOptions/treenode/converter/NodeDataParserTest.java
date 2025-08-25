package org.suikasoft.jOptions.treenode.converter;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Comprehensive tests for {@link NodeDataParser} class.
 * 
 * Tests the parser that applies methods that generate DataStores, based on
 * arbitrary inputs defined by a signature Method.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("NodeDataParser Tests")
class NodeDataParserTest {

    private NodeDataParser parser;
    private Method defaultMethod;

    // Test classes with parser methods
    public static class TestParsers {
        public static DataStore parseTestData(String input) {
            DataStore store = DataStore.newInstance("TestData");
            store.put(org.suikasoft.jOptions.Datakey.KeyFactory.string("input"), input);
            return store;
        }

        public static DataStore parseCustomData(String input) {
            DataStore store = DataStore.newInstance("CustomData");
            store.put(org.suikasoft.jOptions.Datakey.KeyFactory.string("custom"), input);
            return store;
        }

        // Non-static method - should be ignored
        public DataStore parseNonStaticData(String input) {
            return DataStore.newInstance("NonStatic");
        }

        // Wrong return type - should be ignored
        public static String parseWrongReturnData(String input) {
            return "wrong";
        }

        // Wrong parameters - should be ignored
        public static DataStore parseWrongParamsData(String input, int extra) {
            return DataStore.newInstance("WrongParams");
        }
    }

    public static class EmptyParsers {
        // No parser methods
        public static void someOtherMethod() {
            // Does nothing
        }
    }

    // Default method for parser
    public static DataStore parseDefaultData(String input) {
        DataStore store = DataStore.newInstance("DefaultData");
        store.put(org.suikasoft.jOptions.Datakey.KeyFactory.string("default"), input);
        return store;
    }

    @BeforeEach
    void setUp() throws Exception {
        defaultMethod = NodeDataParserTest.class.getMethod("parseDefaultData", String.class);
        parser = new NodeDataParser(defaultMethod, Arrays.asList(TestParsers.class));
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create NodeDataParser with valid default method and parser classes")
        void testConstructor_ValidParameters_CreatesInstance() throws Exception {
            // Given
            Method validMethod = NodeDataParserTest.class.getMethod("parseDefaultData", String.class);

            // When
            NodeDataParser newParser = new NodeDataParser(validMethod, Arrays.asList(TestParsers.class));

            // Then
            assertThat(newParser).isNotNull();
        }

        @Test
        @DisplayName("Should throw exception for non-static default method")
        void testConstructor_NonStaticDefaultMethod_ThrowsException() throws Exception {
            // Given
            Method nonStaticMethod = TestParsers.class.getMethod("parseNonStaticData", String.class);

            // When & Then
            assertThatThrownBy(() -> new NodeDataParser(nonStaticMethod, Arrays.asList(TestParsers.class)))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Compatible methods should be static");
        }

        @Test
        @DisplayName("Should handle empty parser classes collection")
        void testConstructor_EmptyParserClasses_CreatesInstance() throws Exception {
            // Given
            Method validMethod = NodeDataParserTest.class.getMethod("parseDefaultData", String.class);

            // When & Then
            assertThatCode(() -> new NodeDataParser(validMethod, Collections.emptyList()))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle parser classes with no valid methods")
        void testConstructor_NoValidMethods_CreatesInstance() throws Exception {
            // Given
            Method validMethod = NodeDataParserTest.class.getMethod("parseDefaultData", String.class);

            // When & Then
            assertThatCode(() -> new NodeDataParser(validMethod, Arrays.asList(EmptyParsers.class)))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Parser Name Generation Tests")
    class ParserNameGenerationTests {

        @Test
        @DisplayName("Should generate correct parser name for simple key")
        void testGetParserName_SimpleKey_ReturnsCorrectName() {
            // When
            String result = parser.getParserName("Test");

            // Then
            assertThat(result).isEqualTo("parseTestData");
        }

        @Test
        @DisplayName("Should generate correct parser name for camelCase key")
        void testGetParserName_CamelCaseKey_ReturnsCorrectName() {
            // When
            String result = parser.getParserName("CustomData");

            // Then
            assertThat(result).isEqualTo("parseCustomDataData");
        }

        @Test
        @DisplayName("Should generate correct parser name for empty key")
        void testGetParserName_EmptyKey_ReturnsParseData() {
            // When
            String result = parser.getParserName("");

            // Then
            assertThat(result).isEqualTo("parseData");
        }

        @Test
        @DisplayName("Should generate correct parser name for key with special characters")
        void testGetParserName_SpecialCharacters_ReturnsCorrectName() {
            // When
            String result = parser.getParserName("Test_123");

            // Then
            assertThat(result).isEqualTo("parseTest_123Data");
        }
    }

    @Nested
    @DisplayName("Parse Method Tests")
    class ParseMethodTests {

        @Test
        @DisplayName("Should parse using specific parser method when available")
        void testParse_SpecificParserExists_UsesSpecificParser() {
            // When
            Object result = parser.parse("Test", "testInput");

            // Then
            assertThat(result).isInstanceOf(DataStore.class);
            DataStore store = (DataStore) result;
            assertThat(store.getName()).isEqualTo("TestData");
            assertThat(store.get(org.suikasoft.jOptions.Datakey.KeyFactory.string("input"))).isEqualTo("testInput");
        }

        @Test
        @DisplayName("Should parse using custom parser method when available")
        void testParse_CustomParserExists_UsesCustomParser() {
            // When
            Object result = parser.parse("Custom", "customInput");

            // Then
            assertThat(result).isInstanceOf(DataStore.class);
            DataStore store = (DataStore) result;
            assertThat(store.getName()).isEqualTo("CustomData");
            assertThat(store.get(org.suikasoft.jOptions.Datakey.KeyFactory.string("custom"))).isEqualTo("customInput");
        }

        @Test
        @DisplayName("Should use default parser when specific parser not found")
        void testParse_NoSpecificParser_UsesDefaultParser() {
            // When
            Object result = parser.parse("NonExistent", "defaultInput");

            // Then
            assertThat(result).isInstanceOf(DataStore.class);
            DataStore store = (DataStore) result;
            assertThat(store.getName()).isEqualTo("DefaultData");
            assertThat(store.get(org.suikasoft.jOptions.Datakey.KeyFactory.string("default")))
                    .isEqualTo("defaultInput");
        }

        @Test
        @DisplayName("Should handle multiple arguments correctly")
        void testParse_MultipleArguments_PassesAllArguments() throws Exception {
            // Given - Create a parser that accepts multiple arguments
            Method multiArgMethod = MultiArgParsers.class.getMethod("parseDefaultData", String.class, Integer.class);
            NodeDataParser multiArgParser = new NodeDataParser(multiArgMethod, Arrays.asList(MultiArgParsers.class));

            // When
            Object result = multiArgParser.parse("MultiArg", "test", 42);

            // Then
            assertThat(result).isInstanceOf(DataStore.class);
            DataStore store = (DataStore) result;
            // With a default method signature of two args, key 'MultiArg' maps to
            // 'parseMultiArgData' if available
            assertThat(store.getName()).isEqualTo("SpecificMultiArg");
        }

        @Test
        @DisplayName("Should warn only once for missing parser")
        void testParse_MissingParser_WarnsOnlyOnce() {
            // When - Call parse multiple times for same missing parser
            parser.parse("Missing1", "input1");
            parser.parse("Missing1", "input2");
            parser.parse("Missing1", "input3");

            // Then - All should return default parser result
            Object result = parser.parse("Missing1", "input4");
            assertThat(result).isInstanceOf(DataStore.class);
            DataStore store = (DataStore) result;
            assertThat(store.getName()).isEqualTo("DefaultData");
        }
    }

    // Helper class for multi-argument tests
    public static class MultiArgParsers {
        public static DataStore parseDefaultData(String input, Integer number) {
            DataStore store = DataStore.newInstance("MultiArgData");
            store.put(org.suikasoft.jOptions.Datakey.KeyFactory.string("input"), input);
            store.put(org.suikasoft.jOptions.Datakey.KeyFactory.integer("number"), number);
            return store;
        }

        public static DataStore parseMultiArgData(String input, Integer number) {
            DataStore store = DataStore.newInstance("SpecificMultiArg");
            store.put(org.suikasoft.jOptions.Datakey.KeyFactory.string("specific"), input);
            store.put(org.suikasoft.jOptions.Datakey.KeyFactory.integer("specificNumber"), number);
            return store;
        }
    }

    @Nested
    @DisplayName("Method Validation Tests")
    class MethodValidationTests {

        @Test
        @DisplayName("Should register valid static methods")
        void testMethodValidation_ValidStaticMethod_IsRegistered() {
            // When
            Object result = parser.parse("Test", "testInput");

            // Then - Should use the specific Test parser, not default
            assertThat(result).isInstanceOf(DataStore.class);
            DataStore store = (DataStore) result;
            assertThat(store.getName()).isEqualTo("TestData");
        }

        @Test
        @DisplayName("Should ignore non-static methods")
        void testMethodValidation_NonStaticMethod_IsIgnored() {
            // When
            Object result = parser.parse("NonStatic", "input");

            // Then - Should use default parser since non-static is ignored
            assertThat(result).isInstanceOf(DataStore.class);
            DataStore store = (DataStore) result;
            assertThat(store.getName()).isEqualTo("DefaultData");
        }

        @Test
        @DisplayName("Should ignore methods with wrong return type")
        void testMethodValidation_WrongReturnType_IsIgnored() {
            // When
            Object result = parser.parse("WrongReturn", "input");

            // Then - Should use default parser since wrong return type is ignored
            assertThat(result).isInstanceOf(DataStore.class);
            DataStore store = (DataStore) result;
            assertThat(store.getName()).isEqualTo("DefaultData");
        }

        @Test
        @DisplayName("Should ignore methods with wrong parameter count")
        void testMethodValidation_WrongParameterCount_IsIgnored() {
            // When
            Object result = parser.parse("WrongParams", "input");

            // Then - Should use default parser since wrong params is ignored
            assertThat(result).isInstanceOf(DataStore.class);
            DataStore store = (DataStore) result;
            assertThat(store.getName()).isEqualTo("DefaultData");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw exception when parser method throws exception")
        void testParse_ParserMethodThrows_PropagatesException() throws Exception {
            // Given
            Method throwingMethod = ThrowingParsers.class.getMethod("parseDefaultData", String.class);
            NodeDataParser throwingParser = new NodeDataParser(throwingMethod, Arrays.asList(ThrowingParsers.class));

            // When & Then
            assertThatThrownBy(() -> throwingParser.parse("Throwing", "input"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Problems while invoking method")
                    .hasMessageContaining("parseThrowingData");
        }

        @Test
        @DisplayName("Should throw exception when default method throws exception")
        void testParse_DefaultMethodThrows_PropagatesException() throws Exception {
            // Given
            Method throwingMethod = ThrowingParsers.class.getMethod("parseDefaultData", String.class);
            NodeDataParser throwingParser = new NodeDataParser(throwingMethod, Collections.emptyList());

            // When & Then
            assertThatThrownBy(() -> throwingParser.parse("NonExistent", "input"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Problems while invoking method");
        }
    }

    // Helper class for throwing tests
    public static class ThrowingParsers {
        public static DataStore parseDefaultData(String input) {
            throw new RuntimeException("Default method throws");
        }

        public static DataStore parseThrowingData(String input) {
            throw new RuntimeException("Specific method throws");
        }
    }

    @Nested
    @DisplayName("Complex Scenarios Tests")
    class ComplexScenariosTests {

        @Test
        @DisplayName("Should handle multiple parser classes")
        void testParse_MultipleParserClasses_CombinesAllParsers() throws Exception {
            // Given
            Method validMethod = NodeDataParserTest.class.getMethod("parseDefaultData", String.class);
            NodeDataParser multiClassParser = new NodeDataParser(validMethod,
                    Arrays.asList(TestParsers.class, AdditionalParsers.class));

            // When - Test parser from first class
            Object result1 = multiClassParser.parse("Test", "input1");
            // When - Test parser from second class
            Object result2 = multiClassParser.parse("Additional", "input2");

            // Then
            assertThat(result1).isInstanceOf(DataStore.class);
            assertThat(((DataStore) result1).getName()).isEqualTo("TestData");

            assertThat(result2).isInstanceOf(DataStore.class);
            assertThat(((DataStore) result2).getName()).isEqualTo("AdditionalData");
        }

        @Test
        @DisplayName("Should handle method name conflicts correctly")
        void testParse_MethodNameConflicts_UsesLastRegistered() throws Exception {
            // Given
            Method validMethod = NodeDataParserTest.class.getMethod("parseDefaultData", String.class);
            NodeDataParser conflictParser = new NodeDataParser(validMethod,
                    Arrays.asList(TestParsers.class, ConflictingParsers.class));

            // When - Both classes have parseTestData method
            Object result = conflictParser.parse("Test", "input");

            // Then - Should use the last registered (from ConflictingParsers)
            assertThat(result).isInstanceOf(DataStore.class);
            DataStore store = (DataStore) result;
            assertThat(store.getName()).isEqualTo("ConflictingTestData");
        }
    }

    // Helper classes for complex scenarios
    public static class AdditionalParsers {
        public static DataStore parseAdditionalData(String input) {
            DataStore store = DataStore.newInstance("AdditionalData");
            store.put(org.suikasoft.jOptions.Datakey.KeyFactory.string("additional"), input);
            return store;
        }
    }

    public static class ConflictingParsers {
        public static DataStore parseTestData(String input) {
            DataStore store = DataStore.newInstance("ConflictingTestData");
            store.put(org.suikasoft.jOptions.Datakey.KeyFactory.string("conflicting"), input);
            return store;
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null arguments gracefully")
        void testParse_NullArguments_HandlesGracefully() {
            // When & Then - Should not throw NPE
            assertThatCode(() -> parser.parse("Test", (String) null))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle empty string key")
        void testParse_EmptyStringKey_UsesDefaultParser() {
            // When
            Object result = parser.parse("", "input");

            // Then
            assertThat(result).isInstanceOf(DataStore.class);
            DataStore store = (DataStore) result;
            assertThat(store.getName()).isEqualTo("DefaultData");
        }

        @Test
        @DisplayName("Should handle very long key names")
        void testParse_VeryLongKey_WorksCorrectly() {
            // Given
            String longKey = "a".repeat(1000);

            // When
            Object result = parser.parse(longKey, "input");

            // Then
            assertThat(result).isInstanceOf(DataStore.class);
            DataStore store = (DataStore) result;
            assertThat(store.getName()).isEqualTo("DefaultData");
        }
    }
}
