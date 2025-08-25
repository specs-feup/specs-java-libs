package org.suikasoft.jOptions.Utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for EnumCodec functionality.
 * Tests enum encoding/decoding with default and custom encoders.
 * 
 * @author Generated Tests
 */
@DisplayName("EnumCodec")
class EnumCodecTest {

    // Test enum for standard cases
    enum TestEnum {
        VALUE_ONE,
        VALUE_TWO,
        VALUE_THREE
    }

    // Test enum with custom toString behavior
    enum CustomToStringEnum {
        FIRST("first"),
        SECOND("second"),
        THIRD("third");

        private final String displayName;

        CustomToStringEnum(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    // Empty enum for edge case testing
    enum EmptyEnum {
        // No constants
    }

    // Single value enum
    enum SingleValueEnum {
        ONLY_VALUE
    }

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorAndInitializationTests {

        @Test
        @DisplayName("default constructor creates codec using toString")
        void testDefaultConstructor_CreatesCodecUsingToString() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class);

            assertThat(codec).isNotNull();

            // Test that it uses toString() for encoding
            String encoded = codec.encode(TestEnum.VALUE_ONE);
            assertThat(encoded).isEqualTo("VALUE_ONE");
        }

        @Test
        @DisplayName("custom encoder constructor creates codec with provided encoder")
        void testCustomEncoderConstructor_CreatesCodecWithProvidedEncoder() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class,
                    value -> value.name().toLowerCase());

            assertThat(codec).isNotNull();

            // Test that it uses custom encoder
            String encoded = codec.encode(TestEnum.VALUE_ONE);
            assertThat(encoded).isEqualTo("value_one");
        }

        @Test
        @DisplayName("codec initialization builds decode map correctly")
        void testCodecInitialization_BuildsDecodeMapCorrectly() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class);

            // All enum values should be decodable using their toString values
            TestEnum decoded1 = codec.decode("VALUE_ONE");
            TestEnum decoded2 = codec.decode("VALUE_TWO");
            TestEnum decoded3 = codec.decode("VALUE_THREE");

            assertThat(decoded1).isEqualTo(TestEnum.VALUE_ONE);
            assertThat(decoded2).isEqualTo(TestEnum.VALUE_TWO);
            assertThat(decoded3).isEqualTo(TestEnum.VALUE_THREE);
        }
    }

    @Nested
    @DisplayName("Encoding Operations")
    class EncodingOperationsTests {

        @Test
        @DisplayName("encode uses default toString for standard enum")
        void testEncode_UsesDefaultToStringForStandardEnum() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class);

            String encoded = codec.encode(TestEnum.VALUE_TWO);

            assertThat(encoded).isEqualTo("VALUE_TWO");
        }

        @Test
        @DisplayName("encode uses custom toString when enum overrides it")
        void testEncode_UsesCustomToStringWhenEnumOverridesIt() {
            EnumCodec<CustomToStringEnum> codec = new EnumCodec<>(CustomToStringEnum.class);

            String encoded = codec.encode(CustomToStringEnum.FIRST);

            assertThat(encoded).isEqualTo("first");
        }

        @Test
        @DisplayName("encode uses custom encoder function")
        void testEncode_UsesCustomEncoderFunction() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class,
                    value -> "custom_" + value.ordinal());

            String encoded = codec.encode(TestEnum.VALUE_TWO);

            assertThat(encoded).isEqualTo("custom_1"); // VALUE_TWO has ordinal 1
        }

        @Test
        @DisplayName("encode handles all enum values consistently")
        void testEncode_HandlesAllEnumValuesConsistently() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class);

            for (TestEnum value : TestEnum.values()) {
                String encoded = codec.encode(value);
                assertThat(encoded).isEqualTo(value.toString());
            }
        }
    }

    @Nested
    @DisplayName("Decoding Operations")
    class DecodingOperationsTests {

        @Test
        @DisplayName("decode returns correct enum value for valid string")
        void testDecode_ReturnsCorrectEnumValueForValidString() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class);

            TestEnum decoded = codec.decode("VALUE_TWO");

            assertThat(decoded).isEqualTo(TestEnum.VALUE_TWO);
        }

        @Test
        @DisplayName("decode returns first enum constant for null input")
        void testDecode_ReturnsFirstEnumConstantForNullInput() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class);

            TestEnum decoded = codec.decode(null);

            // Should return the first enum constant (VALUE_ONE)
            assertThat(decoded).isEqualTo(TestEnum.VALUE_ONE);
        }

        @Test
        @DisplayName("decode throws exception for invalid string")
        void testDecode_ThrowsExceptionForInvalidString() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class);

            assertThatThrownBy(() -> codec.decode("INVALID_VALUE"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum 'INVALID_VALUE' in class")
                    .hasMessageContaining("Available values:");
        }

        @Test
        @DisplayName("decode works with custom encoder mapping")
        void testDecode_WorksWithCustomEncoderMapping() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class,
                    value -> value.name().toLowerCase());

            TestEnum decoded = codec.decode("value_two");

            assertThat(decoded).isEqualTo(TestEnum.VALUE_TWO);
        }

        @Test
        @DisplayName("decode works with custom toString enum")
        void testDecode_WorksWithCustomToStringEnum() {
            EnumCodec<CustomToStringEnum> codec = new EnumCodec<>(CustomToStringEnum.class);

            CustomToStringEnum decoded = codec.decode("second");

            assertThat(decoded).isEqualTo(CustomToStringEnum.SECOND);
        }

        @Test
        @DisplayName("decode is case sensitive")
        void testDecode_IsCaseSensitive() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class);

            assertThatThrownBy(() -> codec.decode("value_one"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum 'value_one'");
        }
    }

    @Nested
    @DisplayName("Round-trip Encoding/Decoding")
    class RoundTripEncodingDecodingTests {

        @Test
        @DisplayName("encode and decode round-trip works for all enum values")
        void testEncodeAndDecode_RoundTripWorksForAllEnumValues() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class);

            for (TestEnum original : TestEnum.values()) {
                String encoded = codec.encode(original);
                TestEnum decoded = codec.decode(encoded);

                assertThat(decoded).isEqualTo(original);
            }
        }

        @Test
        @DisplayName("round-trip works with custom encoder")
        void testRoundTrip_WorksWithCustomEncoder() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class,
                    value -> "prefix_" + value.ordinal());

            for (TestEnum original : TestEnum.values()) {
                String encoded = codec.encode(original);
                TestEnum decoded = codec.decode(encoded);

                assertThat(decoded).isEqualTo(original);
            }
        }

        @Test
        @DisplayName("round-trip works with custom toString enum")
        void testRoundTrip_WorksWithCustomToStringEnum() {
            EnumCodec<CustomToStringEnum> codec = new EnumCodec<>(CustomToStringEnum.class);

            for (CustomToStringEnum original : CustomToStringEnum.values()) {
                String encoded = codec.encode(original);
                CustomToStringEnum decoded = codec.decode(encoded);

                assertThat(decoded).isEqualTo(original);
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("handles single value enum")
        void testHandles_SingleValueEnum() {
            EnumCodec<SingleValueEnum> codec = new EnumCodec<>(SingleValueEnum.class);

            String encoded = codec.encode(SingleValueEnum.ONLY_VALUE);
            SingleValueEnum decoded = codec.decode(encoded);

            assertThat(encoded).isEqualTo("ONLY_VALUE");
            assertThat(decoded).isEqualTo(SingleValueEnum.ONLY_VALUE);
        }

        @Test
        @DisplayName("null input returns first enum constant for single value enum")
        void testNullInput_ReturnsFirstEnumConstantForSingleValueEnum() {
            EnumCodec<SingleValueEnum> codec = new EnumCodec<>(SingleValueEnum.class);

            SingleValueEnum decoded = codec.decode(null);

            assertThat(decoded).isEqualTo(SingleValueEnum.ONLY_VALUE);
        }

        @Test
        @DisplayName("custom encoder that returns null causes decode issues")
        void testCustomEncoderThatReturnsNull_CausesDecodeIssues() {
            // This is a pathological case where the custom encoder returns null
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class, value -> null);

            // All values will be mapped to null key in decode map
            // This might cause issues or unexpected behavior
            String encoded = codec.encode(TestEnum.VALUE_ONE);
            assertThat(encoded).isNull();

            // Decoding null should work (returns first constant)
            TestEnum decoded = codec.decode(null);
            assertThat(decoded).isEqualTo(TestEnum.VALUE_ONE);
        }

        @Test
        @DisplayName("custom encoder with duplicate mappings creates decode ambiguity")
        void testCustomEncoderWithDuplicateMappings_CreatesDecodeAmbiguity() {
            // Custom encoder that maps different enum values to the same string
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class, value -> "same");

            // All enum values encode to the same string
            String encoded1 = codec.encode(TestEnum.VALUE_ONE);
            String encoded2 = codec.encode(TestEnum.VALUE_TWO);

            assertThat(encoded1).isEqualTo("same");
            assertThat(encoded2).isEqualTo("same");

            // Decoding will return whichever value was put in the map last
            TestEnum decoded = codec.decode("same");
            assertThat(decoded).isIn((Object[]) TestEnum.values()); // One of the enum values
        }

        @Test
        @DisplayName("decode error message includes available values")
        void testDecodeErrorMessage_IncludesAvailableValues() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class);

            assertThatThrownBy(() -> codec.decode("INVALID"))
                    .hasMessageContaining("Available values:")
                    .hasMessageContaining("VALUE_ONE")
                    .hasMessageContaining("VALUE_TWO")
                    .hasMessageContaining("VALUE_THREE");
        }

        @Test
        @DisplayName("empty string decode throws exception")
        void testEmptyStringDecode_ThrowsException() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class);

            assertThatThrownBy(() -> codec.decode(""))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum ''");
        }
    }

    @Nested
    @DisplayName("Custom Encoder Variations")
    class CustomEncoderVariationsTests {

        @Test
        @DisplayName("ordinal-based encoder works correctly")
        void testOrdinalBasedEncoder_WorksCorrectly() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class,
                    value -> String.valueOf(value.ordinal()));

            String encoded = codec.encode(TestEnum.VALUE_THREE);
            TestEnum decoded = codec.decode(encoded);

            assertThat(encoded).isEqualTo("2"); // VALUE_THREE has ordinal 2
            assertThat(decoded).isEqualTo(TestEnum.VALUE_THREE);
        }

        @Test
        @DisplayName("name-based encoder works correctly")
        void testNameBasedEncoder_WorksCorrectly() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class,
                    Enum::name);

            String encoded = codec.encode(TestEnum.VALUE_TWO);
            TestEnum decoded = codec.decode(encoded);

            assertThat(encoded).isEqualTo("VALUE_TWO");
            assertThat(decoded).isEqualTo(TestEnum.VALUE_TWO);
        }

        @Test
        @DisplayName("complex transformation encoder works correctly")
        void testComplexTransformationEncoder_WorksCorrectly() {
            EnumCodec<TestEnum> codec = new EnumCodec<>(TestEnum.class,
                    value -> value.name().toLowerCase().replace("_", "-"));

            String encoded = codec.encode(TestEnum.VALUE_ONE);
            TestEnum decoded = codec.decode(encoded);

            assertThat(encoded).isEqualTo("value-one");
            assertThat(decoded).isEqualTo(TestEnum.VALUE_ONE);
        }
    }
}
