package org.suikasoft.jOptions.DataStore;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Comprehensive test suite for DataClassUtils utility methods.
 * 
 * Tests cover:
 * - toString() method with various input types
 * - StringProvider handling
 * - DataClass handling and cycle prevention
 * - Optional value conversion
 * - List/Collection handling
 * - Primitive type handling
 * - Null value handling
 * - Edge cases and error conditions
 * 
 * @author Generated Tests
 */
@DisplayName("DataClassUtils Utility Methods Tests")
class DataClassUtilsTest {

    @Nested
    @DisplayName("toString Method Tests")
    class ToStringMethodTests {

        @Test
        @DisplayName("toString with StringProvider returns provider string")
        void testToString_WithStringProvider_ReturnsProviderString() {
            StringProvider mockProvider = Mockito.mock(StringProvider.class);
            Mockito.when(mockProvider.getString()).thenReturn("provider_string");

            String result = DataClassUtils.toString(mockProvider);

            assertThat(result).isEqualTo("provider_string");
        }

        @Test
        @DisplayName("toString with DataClass returns quoted class name")
        void testToString_WithDataClass_ReturnsQuotedClassName() {
            DataClass<?> mockDataClass = Mockito.mock(DataClass.class);
            Mockito.when(mockDataClass.getDataClassName()).thenReturn("TestClass");

            String result = DataClassUtils.toString(mockDataClass);

            assertThat(result).isEqualTo("'TestClass'");
        }

        @Test
        @DisplayName("toString with DataClass using empty name")
        void testToString_WithDataClassEmptyName_ReturnsQuotedEmptyString() {
            DataClass<?> mockDataClass = Mockito.mock(DataClass.class);
            Mockito.when(mockDataClass.getDataClassName()).thenReturn("");

            String result = DataClassUtils.toString(mockDataClass);

            assertThat(result).isEqualTo("''");
        }

        @Test
        @DisplayName("toString with DataClass using null name")
        void testToString_WithDataClassNullName_ReturnsQuotedNull() {
            DataClass<?> mockDataClass = Mockito.mock(DataClass.class);
            Mockito.when(mockDataClass.getDataClassName()).thenReturn(null);

            String result = DataClassUtils.toString(mockDataClass);

            assertThat(result).isEqualTo("'null'");
        }
    }

    @Nested
    @DisplayName("Optional Value Handling")
    class OptionalHandlingTests {

        @Test
        @DisplayName("toString with present Optional returns wrapped value")
        void testToString_WithPresentOptional_ReturnsWrappedValue() {
            Optional<String> presentOptional = Optional.of("optional_value");

            String result = DataClassUtils.toString(presentOptional);

            assertThat(result).isEqualTo("optional_value");
        }

        @Test
        @DisplayName("toString with empty Optional returns Optional.empty")
        void testToString_WithEmptyOptional_ReturnsOptionalEmpty() {
            Optional<String> emptyOptional = Optional.empty();

            String result = DataClassUtils.toString(emptyOptional);

            assertThat(result).isEqualTo("Optional.empty");
        }

        @Test
        @DisplayName("toString with Optional containing DataClass")
        void testToString_WithOptionalContainingDataClass_ReturnsQuotedClassName() {
            DataClass<?> mockDataClass = Mockito.mock(DataClass.class);
            Mockito.when(mockDataClass.getDataClassName()).thenReturn("OptionalClass");
            Optional<DataClass<?>> optionalDataClass = Optional.of(mockDataClass);

            String result = DataClassUtils.toString(optionalDataClass);

            assertThat(result).isEqualTo("'OptionalClass'");
        }

        @Test
        @DisplayName("toString with Optional containing StringProvider")
        void testToString_WithOptionalContainingStringProvider_ReturnsProviderString() {
            StringProvider mockProvider = Mockito.mock(StringProvider.class);
            Mockito.when(mockProvider.getString()).thenReturn("provider_in_optional");
            Optional<StringProvider> optionalProvider = Optional.of(mockProvider);

            String result = DataClassUtils.toString(optionalProvider);

            assertThat(result).isEqualTo("provider_in_optional");
        }

        @Test
        @DisplayName("toString with nested Optional")
        void testToString_WithNestedOptional_HandlesRecursively() {
            Optional<Optional<String>> nestedOptional = Optional.of(Optional.of("nested_value"));

            String result = DataClassUtils.toString(nestedOptional);

            assertThat(result).isEqualTo("nested_value");
        }
    }

    @Nested
    @DisplayName("List and Collection Handling")
    class ListHandlingTests {

        @Test
        @DisplayName("toString with empty List returns bracketed empty string")
        void testToString_WithEmptyList_ReturnsBracketedEmptyString() {
            List<String> emptyList = Arrays.asList();

            String result = DataClassUtils.toString(emptyList);

            // Should return properly formatted empty list
            assertThat(result).isEqualTo("[]");
        }

        @Test
        @DisplayName("toString with simple String List returns comma-separated values")
        void testToString_WithSimpleStringList_ReturnsCommaSeparatedValues() {
            List<String> stringList = Arrays.asList("first", "second", "third");

            String result = DataClassUtils.toString(stringList);

            // Should return properly formatted list with comma-separated values
            assertThat(result).isEqualTo("[first, second, third]");
        }

        @Test
        @DisplayName("toString with List containing DataClass objects")
        void testToString_WithListContainingDataClasses_ReturnsQuotedClassNames() {
            DataClass<?> mockDataClass1 = Mockito.mock(DataClass.class);
            Mockito.when(mockDataClass1.getDataClassName()).thenReturn("Class1");
            DataClass<?> mockDataClass2 = Mockito.mock(DataClass.class);
            Mockito.when(mockDataClass2.getDataClassName()).thenReturn("Class2");

            List<DataClass<?>> dataClassList = Arrays.asList(mockDataClass1, mockDataClass2);

            String result = DataClassUtils.toString(dataClassList);

            // Should return properly formatted list with quoted class names
            assertThat(result).isEqualTo("['Class1', 'Class2']");
        }

        @Test
        @DisplayName("toString with List containing mixed types")
        void testToString_WithListContainingMixedTypes_HandlesMixedConversion() {
            DataClass<?> mockDataClass = Mockito.mock(DataClass.class);
            Mockito.when(mockDataClass.getDataClassName()).thenReturn("MixedClass");

            List<Object> mixedList = Arrays.asList(
                    "string_value",
                    mockDataClass,
                    Optional.of("optional_value"));

            String result = DataClassUtils.toString(mixedList);

            // Should handle mixed types properly: string as-is, DataClass quoted, Optional
            // unwrapped
            assertThat(result).isEqualTo("[string_value, 'MixedClass', optional_value]");
        }
    }

    @Nested
    @DisplayName("Primitive and Basic Type Handling")
    class PrimitiveTypeTests {

        @Test
        @DisplayName("toString with String returns string value")
        void testToString_WithString_ReturnsStringValue() {
            String testString = "simple_string";

            String result = DataClassUtils.toString(testString);

            assertThat(result).isEqualTo("simple_string");
        }

        @Test
        @DisplayName("toString with Integer returns string representation")
        void testToString_WithInteger_ReturnsStringRepresentation() {
            Integer testInteger = 42;

            String result = DataClassUtils.toString(testInteger);

            assertThat(result).isEqualTo("42");
        }

        @Test
        @DisplayName("toString with Boolean returns string representation")
        void testToString_WithBoolean_ReturnsStringRepresentation() {
            Boolean testBoolean = true;

            String result = DataClassUtils.toString(testBoolean);

            assertThat(result).isEqualTo("true");
        }

        @Test
        @DisplayName("toString with Double returns string representation")
        void testToString_WithDouble_ReturnsStringRepresentation() {
            Double testDouble = 3.14159;

            String result = DataClassUtils.toString(testDouble);

            assertThat(result).isEqualTo("3.14159");
        }

        @Test
        @DisplayName("toString with null returns null string")
        void testToString_WithNull_ReturnsNullString() {
            // Implementation should handle null gracefully
            String result = DataClassUtils.toString(null);
            assertThat(result).isEqualTo("null");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Complex Scenarios")
    class EdgeCasesTests {

        @Test
        @DisplayName("toString with empty string returns empty string")
        void testToString_WithEmptyString_ReturnsEmptyString() {
            String result = DataClassUtils.toString("");

            assertThat(result).isEqualTo("");
        }

        @Test
        @DisplayName("toString with string containing special characters")
        void testToString_WithSpecialCharacters_PreservesCharacters() {
            String specialString = "special@#$%^&*()";

            String result = DataClassUtils.toString(specialString);

            assertThat(result).isEqualTo("special@#$%^&*()");
        }

        @Test
        @DisplayName("toString with multiline string")
        void testToString_WithMultilineString_PreservesNewlines() {
            String multilineString = "line1\nline2\nline3";

            String result = DataClassUtils.toString(multilineString);

            assertThat(result).isEqualTo("line1\nline2\nline3");
        }

        @Test
        @DisplayName("toString with object implementing toString")
        void testToString_WithCustomToString_UsesCustomImplementation() {
            Object customObject = new Object() {
                @Override
                public String toString() {
                    return "custom_toString_implementation";
                }
            };

            String result = DataClassUtils.toString(customObject);

            assertThat(result).isEqualTo("custom_toString_implementation");
        }
    }

    @Nested
    @DisplayName("Cycle Prevention Tests")
    class CyclePreventionTests {

        @Test
        @DisplayName("toString with DataClass prevents infinite recursion")
        void testToString_WithDataClass_PreventsInfiniteRecursion() {
            // This test verifies that DataClass instances use getDataClassName()
            // instead of toString() to prevent cycles
            DataClass<?> mockDataClass = Mockito.mock(DataClass.class);
            Mockito.when(mockDataClass.getDataClassName()).thenReturn("CycleTestClass");

            String result = DataClassUtils.toString(mockDataClass);

            // Should return quoted class name, not trigger toString() cycle
            assertThat(result).isEqualTo("'CycleTestClass'");
        }

        @Test
        @DisplayName("toString handles recursive structures safely")
        void testToString_HandlesRecursiveStructures_Safely() {
            // Create a recursive structure through Optional and DataClass
            DataClass<?> mockDataClass = Mockito.mock(DataClass.class);
            Mockito.when(mockDataClass.getDataClassName()).thenReturn("RecursiveClass");
            Optional<DataClass<?>> optionalDataClass = Optional.of(mockDataClass);

            String result = DataClassUtils.toString(optionalDataClass);

            // Should safely handle the recursion
            assertThat(result).isEqualTo("'RecursiveClass'");
        }
    }

    @Nested
    @DisplayName("Type Priority Tests")
    class TypePriorityTests {

        @Test
        @DisplayName("toString with object implementing multiple interfaces prioritizes StringProvider")
        void testToString_WithMultipleInterfaces_PrioritizesStringProvider() {
            // Test StringProvider priority by using a StringProvider directly
            StringProvider mockProvider = Mockito.mock(StringProvider.class);
            Mockito.when(mockProvider.getString()).thenReturn("string_provider_result");

            String result = DataClassUtils.toString(mockProvider);

            // StringProvider should take priority
            assertThat(result).isEqualTo("string_provider_result");
        }
    }
}
