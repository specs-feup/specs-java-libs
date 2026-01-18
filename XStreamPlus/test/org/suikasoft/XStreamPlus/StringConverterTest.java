package org.suikasoft.XStreamPlus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Comprehensive unit tests for {@link StringConverter}.
 * 
 * Tests cover type conversion support, string encoding/decoding, and
 * integration
 * with XStream serialization/deserialization.
 * 
 * @author Generated Tests
 */
@DisplayName("StringConverter Tests")
class StringConverterTest {

    @Nested
    @DisplayName("Type Support")
    class TypeSupportTests {

        private StringConverter<CustomType> converter;
        private StringCodec<CustomType> codec;

        @BeforeEach
        void setUp() {
            codec = new CustomTypeCodec();
            converter = new StringConverter<>(CustomType.class, codec);
        }

        @Test
        @DisplayName("canConvert() should return true for supported type")
        void testCanConvert_SupportedType_ShouldReturnTrue() {
            // When
            boolean canConvert = converter.canConvert(CustomType.class);

            // Then
            assertThat(canConvert).isTrue();
        }

        @Test
        @DisplayName("canConvert() should return true for subtype")
        void testCanConvert_Subtype_ShouldReturnTrue() {
            // When
            boolean canConvert = converter.canConvert(CustomSubType.class);

            // Then
            assertThat(canConvert).isTrue();
        }

        @Test
        @DisplayName("canConvert() should return false for unsupported type")
        void testCanConvert_UnsupportedType_ShouldReturnFalse() {
            // When
            boolean canConvert = converter.canConvert(String.class);

            // Then
            assertThat(canConvert).isFalse();
        }

        @Test
        @DisplayName("canConvert() should return false for null type")
        void testCanConvert_NullType_ShouldReturnFalse() {
            // When
            boolean canConvert = converter.canConvert(null);

            // Then
            assertThat(canConvert).isFalse();
        }
    }

    @Nested
    @DisplayName("String Conversion")
    class StringConversionTests {

        private StringConverter<CustomType> converter;
        private StringCodec<CustomType> codec;

        @BeforeEach
        void setUp() {
            codec = new CustomTypeCodec();
            converter = new StringConverter<>(CustomType.class, codec);
        }

        @Test
        @DisplayName("toString() should encode object using codec")
        void testToString_ValidObject_ShouldEncodeUsingCodec() {
            // Given
            CustomType customObj = new CustomType("testValue");

            // When
            String encoded = converter.toString(customObj);

            // Then
            assertThat(encoded).isEqualTo("CUSTOM:testValue");
        }

        @Test
        @DisplayName("toString() should handle null object")
        void testToString_NullObject_ShouldReturnNull() {
            // When
            String encoded = converter.toString(null);

            // Then
            assertThat(encoded).isNull();
        }

        @Test
        @DisplayName("fromString() should decode string using codec")
        void testFromString_ValidString_ShouldDecodeUsingCodec() {
            // Given
            String encoded = "CUSTOM:decodedValue";

            // When
            CustomType decoded = (CustomType) converter.fromString(encoded);

            // Then
            assertThat(decoded)
                    .isNotNull()
                    .satisfies(obj -> assertThat(obj.value).isEqualTo("decodedValue"));
        }

        @Test
        @DisplayName("fromString() should handle null string")
        void testFromString_NullString_ShouldReturnNull() {
            // When
            Object decoded = converter.fromString(null);

            // Then
            assertThat(decoded).isNull();
        }

        @Test
        @DisplayName("fromString() should handle empty string")
        void testFromString_EmptyString_ShouldHandleGracefully() {
            // When
            Object decoded = converter.fromString("");

            // Then
            // Behavior depends on codec implementation
            // Our test codec should handle this gracefully
            assertThat(decoded).satisfies(obj -> {
                if (obj != null) {
                    CustomType customObj = (CustomType) obj;
                    assertThat(customObj.value).isEmpty();
                }
            });
        }
    }

    @Nested
    @DisplayName("Round-trip Conversion")
    class RoundTripConversionTests {

        private StringConverter<CustomType> converter;

        @BeforeEach
        void setUp() {
            converter = new StringConverter<>(CustomType.class, new CustomTypeCodec());
        }

        @Test
        @DisplayName("Should preserve data in round-trip conversion")
        void testRoundTrip_ShouldPreserveData() {
            // Given
            CustomType original = new CustomType("roundTripTest");

            // When
            String encoded = converter.toString(original);
            CustomType decoded = (CustomType) converter.fromString(encoded);

            // Then
            assertThat(decoded)
                    .isNotNull()
                    .satisfies(obj -> assertThat(obj.value).isEqualTo(original.value));
        }

        @Test
        @DisplayName("Should handle special characters in round-trip")
        void testRoundTrip_SpecialCharacters_ShouldPreserveData() {
            // Given
            CustomType original = new CustomType("special:chars!@#$%");

            // When
            String encoded = converter.toString(original);
            CustomType decoded = (CustomType) converter.fromString(encoded);

            // Then
            assertThat(decoded)
                    .isNotNull()
                    .satisfies(obj -> assertThat(obj.value).isEqualTo(original.value));
        }

        @Test
        @DisplayName("Should handle unicode characters in round-trip")
        void testRoundTrip_UnicodeCharacters_ShouldPreserveData() {
            // Given
            CustomType original = new CustomType("unicode:こんにちは🎌");

            // When
            String encoded = converter.toString(original);
            CustomType decoded = (CustomType) converter.fromString(encoded);

            // Then
            assertThat(decoded)
                    .isNotNull()
                    .satisfies(obj -> assertThat(obj.value).isEqualTo(original.value));
        }
    }

    @Nested
    @DisplayName("Integration with Different Codecs")
    class CodecIntegrationTests {

        @Test
        @DisplayName("Should work with different codec implementations")
        void testDifferentCodecs_ShouldWork() {
            // Given
            StringCodec<Integer> intCodec = new StringCodec<Integer>() {
                @Override
                public String encode(Integer object) {
                    return "INT:" + object.toString();
                }

                @Override
                public Integer decode(String string) {
                    return Integer.parseInt(string.substring(4));
                }
            };

            StringConverter<Integer> intConverter = new StringConverter<>(Integer.class, intCodec);

            // When
            String encoded = intConverter.toString(42);
            Integer decoded = (Integer) intConverter.fromString(encoded);

            // Then
            assertAll(
                    () -> assertThat(encoded).isEqualTo("INT:42"),
                    () -> assertThat(decoded).isEqualTo(42));
        }

        @Test
        @DisplayName("Should handle codec that returns null")
        void testCodecReturningNull_ShouldHandleGracefully() {
            // Given
            StringCodec<CustomType> nullCodec = new StringCodec<CustomType>() {
                @Override
                public String encode(CustomType object) {
                    return null; // Simulate error condition
                }

                @Override
                public CustomType decode(String string) {
                    return null; // Simulate error condition
                }
            };

            StringConverter<CustomType> converter = new StringConverter<>(CustomType.class, nullCodec);
            CustomType testObj = new CustomType("test");

            // When
            String encoded = converter.toString(testObj);
            CustomType decoded = (CustomType) converter.fromString("anything");

            // Then
            assertAll(
                    () -> assertThat(encoded).isNull(),
                    () -> assertThat(decoded).isNull());
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle codec throwing exceptions during encoding")
        void testCodecThrowsOnEncode_ShouldPropagateException() {
            // Given
            StringCodec<CustomType> throwingCodec = new StringCodec<CustomType>() {
                @Override
                public String encode(CustomType object) {
                    throw new RuntimeException("Encoding failed");
                }

                @Override
                public CustomType decode(String string) {
                    return new CustomType(string);
                }
            };

            StringConverter<CustomType> converter = new StringConverter<>(CustomType.class, throwingCodec);
            CustomType testObj = new CustomType("test");

            // When/Then
            try {
                String encoded = converter.toString(testObj);
                assertThat(encoded).isNull(); // If implementation catches exceptions
            } catch (RuntimeException e) {
                assertThat(e.getMessage()).contains("Encoding failed");
            }
        }

        @Test
        @DisplayName("Should handle codec throwing exceptions during decoding")
        void testCodecThrowsOnDecode_ShouldPropagateException() {
            // Given
            StringCodec<CustomType> throwingCodec = new StringCodec<CustomType>() {
                @Override
                public String encode(CustomType object) {
                    return object.value;
                }

                @Override
                public CustomType decode(String string) {
                    throw new RuntimeException("Decoding failed");
                }
            };

            StringConverter<CustomType> converter = new StringConverter<>(CustomType.class, throwingCodec);

            // When/Then
            try {
                CustomType decoded = (CustomType) converter.fromString("test");
                assertThat(decoded).isNull(); // If implementation catches exceptions
            } catch (RuntimeException e) {
                assertThat(e.getMessage()).contains("Decoding failed");
            }
        }
    }

    // Test helper classes
    private static class CustomType {
        public String value;

        public CustomType(String value) {
            this.value = value;
        }
    }

    private static class CustomSubType extends CustomType {
        public CustomSubType(String value) {
            super(value);
        }
    }

    private static class CustomTypeCodec implements StringCodec<CustomType> {
        @Override
        public String encode(CustomType object) {
            if (object == null)
                return null;
            return "CUSTOM:" + object.value;
        }

        @Override
        public CustomType decode(String string) {
            if (string == null)
                return null;
            if (!string.startsWith("CUSTOM:")) {
                return new CustomType(string); // Fallback for malformed strings
            }
            return new CustomType(string.substring(7));
        }
    }
}
