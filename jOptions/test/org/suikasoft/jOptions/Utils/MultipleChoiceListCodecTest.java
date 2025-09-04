package org.suikasoft.jOptions.Utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Test suite for MultipleChoiceListCodec functionality.
 * Tests generic list encoding/decoding with configurable element codecs.
 * 
 * @author Generated Tests
 */
@DisplayName("MultipleChoiceListCodec")
class MultipleChoiceListCodecTest {

    // Simple string codec for testing
    private final StringCodec<String> stringCodec = new StringCodec<String>() {
        @Override
        public String encode(String value) {
            return value == null ? "NULL" : value;
        }

        @Override
        public String decode(String value) {
            return "NULL".equals(value) ? null : value;
        }
    };

    // Simple integer codec for testing
    private final StringCodec<Integer> integerCodec = new StringCodec<Integer>() {
        @Override
        public String encode(Integer value) {
            return value == null ? "NULL" : value.toString();
        }

        @Override
        public Integer decode(String value) {
            if ("NULL".equals(value)) {
                return null;
            }
            return Integer.parseInt(value);
        }
    };

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorAndInitializationTests {

        @Test
        @DisplayName("constructor creates codec with element codec")
        void testConstructor_CreatesCodecWithElementCodec() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            assertThat(codec).isNotNull();

            // Test that codec works with provided element codec
            List<String> result = codec.decode("hello$$$world");
            assertThat(result).containsExactly("hello", "world");
        }

        @Test
        @DisplayName("constructor accepts null element codec")
        void testConstructor_AcceptsNullElementCodec() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(null);

            assertThat(codec).isNotNull();

            // Should fail when trying to use the null codec
            assertThatThrownBy(() -> codec.decode("test"))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("constructor works with different element codec types")
        void testConstructor_WorksWithDifferentElementCodecTypes() {
            MultipleChoiceListCodec<Integer> intCodec = new MultipleChoiceListCodec<>(integerCodec);

            assertThat(intCodec).isNotNull();

            List<Integer> result = intCodec.decode("1$$$2$$$3");
            assertThat(result).containsExactly(1, 2, 3);
        }
    }

    @Nested
    @DisplayName("Encoding Operations")
    class EncodingOperationsTests {

        @Test
        @DisplayName("encode empty list returns empty string")
        void testEncode_EmptyListReturnsEmptyString() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            String encoded = codec.encode(Collections.emptyList());

            assertThat(encoded).isEmpty();
        }

        @Test
        @DisplayName("encode single element returns single encoded value")
        void testEncode_SingleElementReturnsSingleEncodedValue() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            String encoded = codec.encode(Arrays.asList("hello"));

            assertThat(encoded).isEqualTo("hello");
        }

        @Test
        @DisplayName("encode multiple elements returns separator-joined string")
        void testEncode_MultipleElementsReturnsSeparatorJoinedString() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            String encoded = codec.encode(Arrays.asList("hello", "world", "test"));

            assertThat(encoded).isEqualTo("hello$$$world$$$test");
        }

        @Test
        @DisplayName("encode preserves order of elements")
        void testEncode_PreservesOrderOfElements() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            String encoded1 = codec.encode(Arrays.asList("first", "second"));
            String encoded2 = codec.encode(Arrays.asList("second", "first"));

            assertThat(encoded1).isEqualTo("first$$$second");
            assertThat(encoded2).isEqualTo("second$$$first");
            assertThat(encoded1).isNotEqualTo(encoded2);
        }

        @Test
        @DisplayName("encode handles duplicate elements")
        void testEncode_HandlesDuplicateElements() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            String encoded = codec.encode(Arrays.asList("hello", "hello", "world"));

            assertThat(encoded).isEqualTo("hello$$$hello$$$world");
        }

        @Test
        @DisplayName("encode uses element codec for each element")
        void testEncode_UsesElementCodecForEachElement() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            String encoded = codec.encode(Arrays.asList("hello", null, "world"));

            assertThat(encoded).isEqualTo("hello$$$NULL$$$world");
        }

        @Test
        @DisplayName("encode delegates to element codec")
        void testEncode_DelegatesToElementCodec() {
            @SuppressWarnings("unchecked")
            StringCodec<String> mockCodec = mock(StringCodec.class);
            when(mockCodec.encode("test1")).thenReturn("encoded1");
            when(mockCodec.encode("test2")).thenReturn("encoded2");

            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(mockCodec);

            String result = codec.encode(Arrays.asList("test1", "test2"));

            assertThat(result).isEqualTo("encoded1$$$encoded2");
            verify(mockCodec).encode("test1");
            verify(mockCodec).encode("test2");
        }
    }

    @Nested
    @DisplayName("Decoding Operations")
    class DecodingOperationsTests {

        @Test
        @DisplayName("decode null returns empty list")
        void testDecode_NullReturnsEmptyList() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            List<String> result = codec.decode(null);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("decode empty string returns list with one empty element")
        void testDecode_EmptyStringReturnsListWithOneEmptyElement() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            List<String> result = codec.decode("");

            assertThat(result).containsExactly("");
        }

        @Test
        @DisplayName("decode single value returns single element list")
        void testDecode_SingleValueReturnsSingleElementList() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            List<String> result = codec.decode("hello");

            assertThat(result).containsExactly("hello");
        }

        @Test
        @DisplayName("decode multiple values returns multiple element list")
        void testDecode_MultipleValuesReturnsMultipleElementList() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            List<String> result = codec.decode("hello$$$world$$$test");

            assertThat(result).containsExactly("hello", "world", "test");
        }

        @Test
        @DisplayName("decode preserves order of elements")
        void testDecode_PreservesOrderOfElements() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            List<String> result1 = codec.decode("first$$$second");
            List<String> result2 = codec.decode("second$$$first");

            assertThat(result1).containsExactly("first", "second");
            assertThat(result2).containsExactly("second", "first");
            assertThat(result1).isNotEqualTo(result2);
        }

        @Test
        @DisplayName("decode handles duplicate elements")
        void testDecode_HandlesDuplicateElements() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            List<String> result = codec.decode("hello$$$hello$$$world");

            assertThat(result).containsExactly("hello", "hello", "world");
        }

        @Test
        @DisplayName("decode uses element codec for each element")
        void testDecode_UsesElementCodecForEachElement() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            List<String> result = codec.decode("hello$$$NULL$$$world");

            assertThat(result).containsExactly("hello", null, "world");
        }

        @Test
        @DisplayName("decode delegates to element codec")
        void testDecode_DelegatesToElementCodec() {
            @SuppressWarnings("unchecked")
            StringCodec<String> mockCodec = mock(StringCodec.class);
            when(mockCodec.decode("encoded1")).thenReturn("test1");
            when(mockCodec.decode("encoded2")).thenReturn("test2");

            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(mockCodec);

            List<String> result = codec.decode("encoded1$$$encoded2");

            assertThat(result).containsExactly("test1", "test2");
            verify(mockCodec).decode("encoded1");
            verify(mockCodec).decode("encoded2");
        }

        @Test
        @DisplayName("decode with different element types")
        void testDecode_WithDifferentElementTypes() {
            MultipleChoiceListCodec<Integer> codec = new MultipleChoiceListCodec<>(integerCodec);

            List<Integer> result = codec.decode("1$$$2$$$3");

            assertThat(result).containsExactly(1, 2, 3);
        }
    }

    @Nested
    @DisplayName("Round-trip Consistency")
    class RoundTripConsistencyTests {

        @Test
        @DisplayName("encode and decode round-trip works for empty list")
        void testEncodeAndDecode_RoundTripWorksForEmptyList() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);
            List<String> original = Collections.emptyList();

            String encoded = codec.encode(original);
            List<String> decoded = codec.decode(encoded);

            // Encoding of empty list is empty string, decoding empty string should yield a
            // single empty element
            assertThat(decoded).containsExactly("");
        }

        @Test
        @DisplayName("encode and decode round-trip works for single element")
        void testEncodeAndDecode_RoundTripWorksForSingleElement() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);
            List<String> original = Arrays.asList("test");

            String encoded = codec.encode(original);
            List<String> decoded = codec.decode(encoded);

            assertThat(decoded).isEqualTo(original);
        }

        @Test
        @DisplayName("encode and decode round-trip works for multiple elements")
        void testEncodeAndDecode_RoundTripWorksForMultipleElements() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);
            List<String> original = Arrays.asList("hello", "world", "test");

            String encoded = codec.encode(original);
            List<String> decoded = codec.decode(encoded);

            assertThat(decoded).isEqualTo(original);
        }

        @Test
        @DisplayName("encode and decode round-trip works with null elements")
        void testEncodeAndDecode_RoundTripWorksWithNullElements() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);
            List<String> original = Arrays.asList("hello", null, "world");

            String encoded = codec.encode(original);
            List<String> decoded = codec.decode(encoded);

            assertThat(decoded).isEqualTo(original);
        }

        @Test
        @DisplayName("encode and decode round-trip works with integer elements")
        void testEncodeAndDecode_RoundTripWorksWithIntegerElements() {
            MultipleChoiceListCodec<Integer> codec = new MultipleChoiceListCodec<>(integerCodec);
            List<Integer> original = Arrays.asList(1, 2, 3, null, 5);

            String encoded = codec.encode(original);
            List<Integer> decoded = codec.decode(encoded);

            assertThat(decoded).isEqualTo(original);
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandlingTests {

        @Test
        @DisplayName("decode propagates element codec exceptions")
        void testDecode_PropagatesElementCodecExceptions() {
            @SuppressWarnings("unchecked")
            StringCodec<String> mockCodec = mock(StringCodec.class);
            when(mockCodec.decode("invalid")).thenThrow(new RuntimeException("Invalid value"));

            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(mockCodec);

            assertThatThrownBy(() -> codec.decode("valid$$$invalid"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Invalid value");
        }

        @Test
        @DisplayName("encode propagates element codec exceptions")
        void testEncode_PropagatesElementCodecExceptions() {
            @SuppressWarnings("unchecked")
            StringCodec<String> mockCodec = mock(StringCodec.class);
            when(mockCodec.encode("invalid")).thenThrow(new RuntimeException("Cannot encode"));
            when(mockCodec.encode("valid")).thenReturn("encoded_valid");

            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(mockCodec);

            assertThatThrownBy(() -> codec.encode(Arrays.asList("valid", "invalid")))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Cannot encode");
        }

        @Test
        @DisplayName("decode with null element codec throws exception")
        void testDecode_WithNullElementCodecThrowsException() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(null);

            assertThatThrownBy(() -> codec.decode("test"))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("encode with null element codec throws exception")
        void testEncode_WithNullElementCodecThrowsException() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(null);

            assertThatThrownBy(() -> codec.encode(Arrays.asList("test")))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Separator Handling")
    class EdgeCasesAndSeparatorHandlingTests {

        @Test
        @DisplayName("handles string with separator in element content")
        void testHandles_StringWithSeparatorInElementContent() {
            // Element codec that returns separator in content
            StringCodec<String> codecWithSeparator = new StringCodec<String>() {
                @Override
                public String encode(String value) {
                    return value + "$$$";
                }

                @Override
                public String decode(String value) {
                    return value.endsWith("$$$") ? value.substring(0, value.length() - 3) : value;
                }
            };

            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(codecWithSeparator);

            List<String> original = Arrays.asList("hello", "world");
            String encoded = codec.encode(original);
            List<String> decoded = codec.decode(encoded);

            // This demonstrates the limitation when separator appears in encoded content
            assertThat(encoded).isEqualTo("hello$$$$$$world$$$");
            // With adjacent separators, splitting yields a single empty element between
            assertThat(decoded).containsExactly("hello", "", "world", "");
        }

        @Test
        @DisplayName("handles empty string elements correctly")
        void testHandles_EmptyStringElementsCorrectly() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            List<String> original = Arrays.asList("", "hello", "", "world", "");
            String encoded = codec.encode(original);
            List<String> decoded = codec.decode(encoded);

            assertThat(encoded).isEqualTo("$$$hello$$$$$$world$$$");
            assertThat(decoded).isEqualTo(original);
        }

        @Test
        @DisplayName("handles large number of elements")
        void testHandles_LargeNumberOfElements() {
            MultipleChoiceListCodec<Integer> codec = new MultipleChoiceListCodec<>(integerCodec);

            List<Integer> original = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                original.add(i);
            }

            String encoded = codec.encode(original);
            List<Integer> decoded = codec.decode(encoded);

            assertThat(decoded).isEqualTo(original);
            assertThat(decoded).hasSize(1000);
        }

        @Test
        @DisplayName("optimizes for null input without calling element codec")
        void testOptimizes_ForNullInputWithoutCallingElementCodec() {
            @SuppressWarnings("unchecked")
            StringCodec<String> mockCodec = mock(StringCodec.class);

            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(mockCodec);

            List<String> result = codec.decode(null);

            assertThat(result).isEmpty();
            verify(mockCodec, never()).decode(any());
        }

        @Test
        @DisplayName("uses different separator than MultiEnumCodec")
        void testUses_DifferentSeparatorThanMultiEnumCodec() {
            MultipleChoiceListCodec<String> codec = new MultipleChoiceListCodec<>(stringCodec);

            // Encoding with elements that contain semicolon (MultiEnumCodec separator)
            List<String> original = Arrays.asList("value;with;semicolon", "normal");
            String encoded = codec.encode(original);
            List<String> decoded = codec.decode(encoded);

            assertThat(encoded).isEqualTo("value;with;semicolon$$$normal");
            assertThat(decoded).isEqualTo(original);
        }
    }
}
