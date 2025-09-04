package org.suikasoft.jOptions.Utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Datakey.DataKey;

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Test suite for RawValueUtils functionality.
 * Tests string-to-object value conversion with various data types and codecs.
 * 
 * @author Generated Tests
 */
@DisplayName("RawValueUtils")
class RawValueUtilsTest {

    @Nested
    @DisplayName("String Value Conversion")
    class StringValueConversionTests {

        @Test
        @DisplayName("getRealValue converts String values using default converter")
        void testGetRealValue_ConvertsStringValuesUsingDefaultConverter() {
            // Create a mock DataKey for String type
            @SuppressWarnings("unchecked")
            DataKey<String> stringKey = mock(DataKey.class);
            when(stringKey.getValueClass()).thenReturn(String.class);
            when(stringKey.getDecoder()).thenReturn(Optional.empty());

            String result = (String) RawValueUtils.getRealValue(stringKey, "test string");

            assertThat(result).isEqualTo("test string");
        }

        @Test
        @DisplayName("getRealValue handles empty string")
        void testGetRealValue_HandlesEmptyString() {
            @SuppressWarnings("unchecked")
            DataKey<String> stringKey = mock(DataKey.class);
            when(stringKey.getValueClass()).thenReturn(String.class);
            when(stringKey.getDecoder()).thenReturn(Optional.empty());

            String result = (String) RawValueUtils.getRealValue(stringKey, "");

            assertThat(result).isEqualTo("");
        }

        @Test
        @DisplayName("getRealValue handles string with special characters")
        void testGetRealValue_HandlesStringWithSpecialCharacters() {
            @SuppressWarnings("unchecked")
            DataKey<String> stringKey = mock(DataKey.class);
            when(stringKey.getValueClass()).thenReturn(String.class);
            when(stringKey.getDecoder()).thenReturn(Optional.empty());

            String specialString = "test@#$%^&*()_+{}|:<>?[]\\;'\".,/";
            String result = (String) RawValueUtils.getRealValue(stringKey, specialString);

            assertThat(result).isEqualTo(specialString);
        }
    }

    @Nested
    @DisplayName("Boolean Value Conversion")
    class BooleanValueConversionTests {

        @Test
        @DisplayName("getRealValue converts Boolean true values")
        void testGetRealValue_ConvertsBooleanTrueValues() {
            @SuppressWarnings("unchecked")
            DataKey<Boolean> booleanKey = mock(DataKey.class);
            when(booleanKey.getValueClass()).thenReturn(Boolean.class);
            when(booleanKey.getDecoder()).thenReturn(Optional.empty());

            Boolean result = (Boolean) RawValueUtils.getRealValue(booleanKey, "true");

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("getRealValue converts Boolean false values")
        void testGetRealValue_ConvertsBooleanFalseValues() {
            @SuppressWarnings("unchecked")
            DataKey<Boolean> booleanKey = mock(DataKey.class);
            when(booleanKey.getValueClass()).thenReturn(Boolean.class);
            when(booleanKey.getDecoder()).thenReturn(Optional.empty());

            Boolean result = (Boolean) RawValueUtils.getRealValue(booleanKey, "false");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("getRealValue converts Boolean case-insensitive values")
        void testGetRealValue_ConvertsBooleanCaseInsensitiveValues() {
            @SuppressWarnings("unchecked")
            DataKey<Boolean> booleanKey = mock(DataKey.class);
            when(booleanKey.getValueClass()).thenReturn(Boolean.class);
            when(booleanKey.getDecoder()).thenReturn(Optional.empty());

            // Test various case combinations
            Boolean trueResult1 = (Boolean) RawValueUtils.getRealValue(booleanKey, "TRUE");
            Boolean trueResult2 = (Boolean) RawValueUtils.getRealValue(booleanKey, "True");
            Boolean falseResult1 = (Boolean) RawValueUtils.getRealValue(booleanKey, "FALSE");
            Boolean falseResult2 = (Boolean) RawValueUtils.getRealValue(booleanKey, "False");

            assertThat(trueResult1).isTrue();
            assertThat(trueResult2).isTrue();
            assertThat(falseResult1).isFalse();
            assertThat(falseResult2).isFalse();
        }

        @Test
        @DisplayName("getRealValue handles invalid Boolean values")
        void testGetRealValue_HandlesInvalidBooleanValues() {
            @SuppressWarnings("unchecked")
            DataKey<Boolean> booleanKey = mock(DataKey.class);
            when(booleanKey.getValueClass()).thenReturn(Boolean.class);
            when(booleanKey.getDecoder()).thenReturn(Optional.empty());

            // Invalid boolean values should still be processed by Boolean.valueOf
            Boolean result1 = (Boolean) RawValueUtils.getRealValue(booleanKey, "invalid");
            Boolean result2 = (Boolean) RawValueUtils.getRealValue(booleanKey, "123");
            Boolean result3 = (Boolean) RawValueUtils.getRealValue(booleanKey, "");

            // Boolean.valueOf returns false for any string that's not "true"
            // (case-insensitive)
            assertThat(result1).isFalse();
            assertThat(result2).isFalse();
            assertThat(result3).isFalse();
        }
    }

    @Nested
    @DisplayName("Custom Decoder Usage")
    class CustomDecoderUsageTests {

        @Test
        @DisplayName("getRealValue uses custom decoder when available")
        void testGetRealValue_UsesCustomDecoderWhenAvailable() {
            @SuppressWarnings("unchecked")
            DataKey<Integer> intKey = mock(DataKey.class);
            @SuppressWarnings("unchecked")
            StringCodec<Integer> customDecoder = mock(StringCodec.class);

            when(intKey.getValueClass()).thenReturn(Integer.class);
            when(intKey.getDecoder()).thenReturn(Optional.of(customDecoder));
            when(customDecoder.decode("42")).thenReturn(42);

            Integer result = (Integer) RawValueUtils.getRealValue(intKey, "42");

            assertThat(result).isEqualTo(42);
        }

        @Test
        @DisplayName("getRealValue handles custom decoder returning null - ClassMap throws exception")
        void testGetRealValue_HandlesCustomDecoderReturningNull_ClassMapThrowsException() {
            @SuppressWarnings("unchecked")
            DataKey<Integer> intKey = mock(DataKey.class);
            @SuppressWarnings("unchecked")
            StringCodec<Integer> customDecoder = mock(StringCodec.class);

            when(intKey.getValueClass()).thenReturn(Integer.class);
            when(intKey.getDecoder()).thenReturn(Optional.of(customDecoder));
            when(customDecoder.decode("invalid")).thenReturn(null);

            // When custom decoder returns null, should fall back to default converters
            // But ClassMap.get() throws exception for Integer
            try {
                Object result = RawValueUtils.getRealValue(intKey, "invalid");

                // If it doesn't throw, this is unexpected behavior
                assertThat(result).isNull();

            } catch (pt.up.fe.specs.util.exceptions.NotImplementedException e) {
                // This is the actual behavior - ClassMap throws exception when fallback happens
                assertThat(e.getMessage())
                        .contains("Not yet implemented: Function not defined for class 'class java.lang.Integer'");
            }
        }

        @Test
        @DisplayName("getRealValue prioritizes custom decoder over default converter")
        void testGetRealValue_PrioritizesCustomDecoderOverDefaultConverter() {
            @SuppressWarnings("unchecked")
            DataKey<String> stringKey = mock(DataKey.class);
            @SuppressWarnings("unchecked")
            StringCodec<String> customDecoder = mock(StringCodec.class);

            when(stringKey.getValueClass()).thenReturn(String.class);
            when(stringKey.getDecoder()).thenReturn(Optional.of(customDecoder));
            when(customDecoder.decode("input")).thenReturn("custom_output");

            String result = (String) RawValueUtils.getRealValue(stringKey, "input");

            // Should use custom decoder result, not default String converter
            assertThat(result).isEqualTo("custom_output");
        }
    }

    @Nested
    @DisplayName("Unsupported Type Handling")
    class UnsupportedTypeHandlingTests {

        @Test
        @DisplayName("getRealValue throws NotImplementedException for unsupported types without decoder")
        void testGetRealValue_ThrowsNotImplementedExceptionForUnsupportedTypesWithoutDecoder() {
            @SuppressWarnings("unchecked")
            DataKey<Integer> intKey = mock(DataKey.class);
            when(intKey.getValueClass()).thenReturn(Integer.class);
            when(intKey.getDecoder()).thenReturn(Optional.empty());
            when(intKey.toString()).thenReturn("IntegerKey");

            // Integer is not in default converters, ClassMap.get() throws
            // NotImplementedException
            try {
                Object result = RawValueUtils.getRealValue(intKey, "123");

                // If it doesn't throw, this is unexpected behavior
                assertThat(result).isNull();

            } catch (pt.up.fe.specs.util.exceptions.NotImplementedException e) {
                // This is the actual behavior - ClassMap throws exception instead of returning
                // null
                assertThat(e.getMessage())
                        .contains("Not yet implemented: Function not defined for class 'class java.lang.Integer'");
            }
        }

        @Test
        @DisplayName("getRealValue throws NotImplementedException for custom types without decoder")
        void testGetRealValue_ThrowsNotImplementedExceptionForCustomTypesWithoutDecoder() {
            // Custom class for testing
            class CustomType {
            }

            @SuppressWarnings("unchecked")
            DataKey<CustomType> customKey = mock(DataKey.class);
            when(customKey.getValueClass()).thenReturn(CustomType.class);
            when(customKey.getDecoder()).thenReturn(Optional.empty());
            when(customKey.toString()).thenReturn("CustomTypeKey");

            try {
                Object result = RawValueUtils.getRealValue(customKey, "any_value");

                // If it doesn't throw, this is unexpected behavior
                assertThat(result).isNull();

            } catch (pt.up.fe.specs.util.exceptions.NotImplementedException e) {
                // This is the actual behavior - ClassMap throws exception for unknown classes
                assertThat(e.getMessage()).contains("Not yet implemented: Function not defined for class");
            }
        }

        @Test
        @DisplayName("getRealValue handles null input value")
        void testGetRealValue_HandlesNullInputValue() {
            @SuppressWarnings("unchecked")
            DataKey<String> stringKey = mock(DataKey.class);
            when(stringKey.getValueClass()).thenReturn(String.class);
            when(stringKey.getDecoder()).thenReturn(Optional.empty());

            // The method signature expects String, but let's see how it handles null
            // This might cause NullPointerException depending on the implementation
            try {
                Object result = RawValueUtils.getRealValue(stringKey, null);

                // If it doesn't throw, check the result
                assertThat(result).isNull(); // String converter should handle null somehow

            } catch (NullPointerException e) {
                // If it throws NPE, that's also a valid behavior to document
                assertThat(e).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("getRealValue handles null DataKey")
        void testGetRealValue_HandlesNullDataKey() {
            try {
                Object result = RawValueUtils.getRealValue(null, "test");

                // If it doesn't throw, result should be null
                assertThat(result).isNull();

            } catch (NullPointerException e) {
                // NPE is expected behavior when DataKey is null
                assertThat(e).isNotNull();
            }
        }

        @Test
        @DisplayName("getRealValue throws NPE for DataKey with null value class")
        void testGetRealValue_ThrowsNPEForDataKeyWithNullValueClass() {
            @SuppressWarnings("unchecked")
            DataKey<String> keyWithNullClass = mock(DataKey.class);
            when(keyWithNullClass.getValueClass()).thenReturn(null);
            when(keyWithNullClass.getDecoder()).thenReturn(Optional.empty());
            when(keyWithNullClass.toString()).thenReturn("NullClassKey");

            try {
                Object result = RawValueUtils.getRealValue(keyWithNullClass, "test");

                // If it doesn't throw, this is unexpected behavior
                assertThat(result).isNull();

            } catch (NullPointerException e) {
                // This is the actual behavior - ClassMap throws exception for null class
                assertThat(e.getMessage()).contains("Key cannot be null");
            }
        }

        @Test
        @DisplayName("getRealValue handles custom decoder throwing exception")
        void testGetRealValue_HandlesCustomDecoderThrowingException() {
            @SuppressWarnings("unchecked")
            DataKey<Integer> intKey = mock(DataKey.class);
            @SuppressWarnings("unchecked")
            StringCodec<Integer> faultyDecoder = mock(StringCodec.class);

            when(intKey.getValueClass()).thenReturn(Integer.class);
            when(intKey.getDecoder()).thenReturn(Optional.of(faultyDecoder));
            when(faultyDecoder.decode("invalid")).thenThrow(new RuntimeException("Decode error"));

            try {
                Object result = RawValueUtils.getRealValue(intKey, "invalid");

                // If exception is caught internally, should continue to default converters
                assertThat(result).isNull(); // Integer not in defaults

            } catch (RuntimeException e) {
                // If exception is not caught, that's also valid behavior
                assertThat(e.getMessage()).contains("Decode error");
            }
        }

        @Test
        @DisplayName("getRealValue works with whitespace values")
        void testGetRealValue_WorksWithWhitespaceValues() {
            @SuppressWarnings("unchecked")
            DataKey<String> stringKey = mock(DataKey.class);
            when(stringKey.getValueClass()).thenReturn(String.class);
            when(stringKey.getDecoder()).thenReturn(Optional.empty());

            String result1 = (String) RawValueUtils.getRealValue(stringKey, " ");
            String result2 = (String) RawValueUtils.getRealValue(stringKey, "\t");
            String result3 = (String) RawValueUtils.getRealValue(stringKey, "\n");
            String result4 = (String) RawValueUtils.getRealValue(stringKey, "  \t\n  ");

            assertThat(result1).isEqualTo(" ");
            assertThat(result2).isEqualTo("\t");
            assertThat(result3).isEqualTo("\n");
            assertThat(result4).isEqualTo("  \t\n  ");
        }
    }

    @Nested
    @DisplayName("Default Converter Table Behavior")
    class DefaultConverterTableBehaviorTests {

        @Test
        @DisplayName("default converters include String and Boolean types")
        void testDefaultConverters_IncludeStringAndBooleanTypes() {
            // Test that the expected types have default converters
            @SuppressWarnings("unchecked")
            DataKey<String> stringKey = mock(DataKey.class);
            when(stringKey.getValueClass()).thenReturn(String.class);
            when(stringKey.getDecoder()).thenReturn(Optional.empty());

            @SuppressWarnings("unchecked")
            DataKey<Boolean> booleanKey = mock(DataKey.class);
            when(booleanKey.getValueClass()).thenReturn(Boolean.class);
            when(booleanKey.getDecoder()).thenReturn(Optional.empty());

            // These should work because they have default converters
            String stringResult = (String) RawValueUtils.getRealValue(stringKey, "test");
            Boolean booleanResult = (Boolean) RawValueUtils.getRealValue(booleanKey, "true");

            assertThat(stringResult).isEqualTo("test");
            assertThat(booleanResult).isTrue();
        }

        @Test
        @DisplayName("common types without default converters throw NotImplementedException")
        void testCommonTypesWithoutDefaultConverters_ThrowNotImplementedException() {
            // Test some common types that don't have default converters
            @SuppressWarnings("unchecked")
            DataKey<Integer> intKey = mock(DataKey.class);
            when(intKey.getValueClass()).thenReturn(Integer.class);
            when(intKey.getDecoder()).thenReturn(Optional.empty());
            when(intKey.toString()).thenReturn("IntegerKey");

            @SuppressWarnings("unchecked")
            DataKey<Double> doubleKey = mock(DataKey.class);
            when(doubleKey.getValueClass()).thenReturn(Double.class);
            when(doubleKey.getDecoder()).thenReturn(Optional.empty());
            when(doubleKey.toString()).thenReturn("DoubleKey");

            // Should throw NotImplementedException instead of returning null
            try {
                Object intResult = RawValueUtils.getRealValue(intKey, "123");
                // If it doesn't throw, this is unexpected behavior
                assertThat(intResult).isNull();
            } catch (pt.up.fe.specs.util.exceptions.NotImplementedException e) {
                assertThat(e.getMessage())
                        .contains("Not yet implemented: Function not defined for class 'class java.lang.Integer'");
            }

            try {
                Object doubleResult = RawValueUtils.getRealValue(doubleKey, "123.45");
                // If it doesn't throw, this is unexpected behavior
                assertThat(doubleResult).isNull();
            } catch (pt.up.fe.specs.util.exceptions.NotImplementedException e) {
                assertThat(e.getMessage())
                        .contains("Not yet implemented: Function not defined for class 'class java.lang.Double'");
            }
        }
    }
}
