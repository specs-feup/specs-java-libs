package org.suikasoft.jOptions.Utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test suite for MultiEnumCodec functionality.
 * Tests encoding/decoding of lists of enum values.
 * 
 * Note: MultiEnumCodec is marked as @Deprecated but still needs testing for
 * compatibility.
 * 
 * @author Generated Tests
 */
@DisplayName("MultiEnumCodec")
@SuppressWarnings("deprecation")
class MultiEnumCodecTest {

    // Test enum for standard cases
    enum TestEnum {
        VALUE_ONE,
        VALUE_TWO,
        VALUE_THREE
    }

    // Single value enum for edge case testing
    enum SingleValueEnum {
        ONLY_VALUE
    }

    // Enum with similar names for testing name collision issues
    enum SimilarNamesEnum {
        A,
        AA,
        AAA
    }

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorAndInitializationTests {

        @Test
        @DisplayName("constructor creates codec with proper decode map")
        void testConstructor_CreatesCodecWithProperDecodeMap() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            assertThat(codec).isNotNull();

            // Test that all enum values are decodable by their names
            List<TestEnum> decoded = codec.decode("VALUE_ONE;VALUE_TWO;VALUE_THREE");

            assertThat(decoded).containsExactly(
                    TestEnum.VALUE_ONE,
                    TestEnum.VALUE_TWO,
                    TestEnum.VALUE_THREE);
        }

        @Test
        @DisplayName("constructor works with single value enum")
        void testConstructor_WorksWithSingleValueEnum() {
            MultiEnumCodec<SingleValueEnum> codec = new MultiEnumCodec<>(SingleValueEnum.class);

            assertThat(codec).isNotNull();

            List<SingleValueEnum> decoded = codec.decode("ONLY_VALUE");
            assertThat(decoded).containsExactly(SingleValueEnum.ONLY_VALUE);
        }
    }

    @Nested
    @DisplayName("Encoding Operations")
    class EncodingOperationsTests {

        @Test
        @DisplayName("encode empty list returns empty string")
        void testEncode_EmptyListReturnsEmptyString() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            String encoded = codec.encode(Collections.emptyList());

            assertThat(encoded).isEmpty();
        }

        @Test
        @DisplayName("encode single value returns value name")
        void testEncode_SingleValueReturnsValueName() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            String encoded = codec.encode(Arrays.asList(TestEnum.VALUE_ONE));

            assertThat(encoded).isEqualTo("VALUE_ONE");
        }

        @Test
        @DisplayName("encode multiple values returns semicolon separated names")
        void testEncode_MultipleValuesReturnsSemicolonSeparatedNames() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            String encoded = codec.encode(Arrays.asList(
                    TestEnum.VALUE_ONE,
                    TestEnum.VALUE_TWO,
                    TestEnum.VALUE_THREE));

            assertThat(encoded).isEqualTo("VALUE_ONE;VALUE_TWO;VALUE_THREE");
        }

        @Test
        @DisplayName("encode uses enum name method consistently")
        void testEncode_UsesEnumNameMethodConsistently() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            for (TestEnum value : TestEnum.values()) {
                String encoded = codec.encode(Arrays.asList(value));
                assertThat(encoded).isEqualTo(value.name());
            }
        }

        @Test
        @DisplayName("encode handles duplicate values in list")
        void testEncode_HandlesDuplicateValuesInList() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            String encoded = codec.encode(Arrays.asList(
                    TestEnum.VALUE_ONE,
                    TestEnum.VALUE_ONE,
                    TestEnum.VALUE_TWO));

            assertThat(encoded).isEqualTo("VALUE_ONE;VALUE_ONE;VALUE_TWO");
        }
    }

    @Nested
    @DisplayName("Decoding Operations")
    class DecodingOperationsTests {

        @Test
        @DisplayName("decode null returns empty list")
        void testDecode_NullReturnsEmptyList() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            List<TestEnum> decoded = codec.decode(null);

            assertThat(decoded).isEmpty();
        }

        @Test
        @DisplayName("decode empty string throws exception due to empty element")
        void testDecode_EmptyStringThrowsExceptionDueToEmptyElement() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            // Empty string when split by ";" results in array with one empty string element
            // which cannot be decoded as enum value
            assertThatThrownBy(() -> codec.decode(""))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum ''");
        }

        @Test
        @DisplayName("decode single value returns single element list")
        void testDecode_SingleValueReturnsSingleElementList() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            List<TestEnum> decoded = codec.decode("VALUE_TWO");

            assertThat(decoded).containsExactly(TestEnum.VALUE_TWO);
        }

        @Test
        @DisplayName("decode multiple values returns list in correct order")
        void testDecode_MultipleValuesReturnsListInCorrectOrder() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            List<TestEnum> decoded = codec.decode("VALUE_THREE;VALUE_ONE;VALUE_TWO");

            assertThat(decoded).containsExactly(
                    TestEnum.VALUE_THREE,
                    TestEnum.VALUE_ONE,
                    TestEnum.VALUE_TWO);
        }

        @Test
        @DisplayName("decode throws exception for invalid enum name")
        void testDecode_ThrowsExceptionForInvalidEnumName() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            assertThatThrownBy(() -> codec.decode("INVALID_VALUE"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum 'INVALID_VALUE' in class")
                    .hasMessageContaining("Available values:");
        }

        @Test
        @DisplayName("decode throws exception for partially invalid input")
        void testDecode_ThrowsExceptionForPartiallyInvalidInput() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            assertThatThrownBy(() -> codec.decode("VALUE_ONE;INVALID;VALUE_TWO"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum 'INVALID'");
        }

        @Test
        @DisplayName("decode handles duplicate values in input")
        void testDecode_HandlesDuplicateValuesInInput() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            List<TestEnum> decoded = codec.decode("VALUE_ONE;VALUE_ONE;VALUE_TWO");

            assertThat(decoded).containsExactly(
                    TestEnum.VALUE_ONE,
                    TestEnum.VALUE_ONE,
                    TestEnum.VALUE_TWO);
        }
    }

    @Nested
    @DisplayName("Round-trip Encoding/Decoding")
    class RoundTripEncodingDecodingTests {

        @Test
        @DisplayName("encode and decode round-trip works for single value")
        void testEncodeAndDecode_RoundTripWorksForSingleValue() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);
            List<TestEnum> original = Arrays.asList(TestEnum.VALUE_TWO);

            String encoded = codec.encode(original);
            List<TestEnum> decoded = codec.decode(encoded);

            assertThat(decoded).isEqualTo(original);
        }

        @Test
        @DisplayName("encode and decode round-trip works for multiple values")
        void testEncodeAndDecode_RoundTripWorksForMultipleValues() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);
            List<TestEnum> original = Arrays.asList(
                    TestEnum.VALUE_ONE,
                    TestEnum.VALUE_THREE,
                    TestEnum.VALUE_TWO);

            String encoded = codec.encode(original);
            List<TestEnum> decoded = codec.decode(encoded);

            assertThat(decoded).isEqualTo(original);
        }

        @Test
        @DisplayName("encode and decode round-trip fails for empty list due to empty string handling")
        void testEncodeAndDecode_RoundTripFailsForEmptyListDueToEmptyStringHandling() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);
            List<TestEnum> original = Collections.emptyList();

            String encoded = codec.encode(original);

            // Empty list encodes to empty string, but empty string cannot be decoded back
            assertThat(encoded).isEmpty();

            assertThatThrownBy(() -> codec.decode(encoded))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum ''");
        }

        @Test
        @DisplayName("round-trip preserves order and duplicates")
        void testRoundTrip_PreservesOrderAndDuplicates() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);
            List<TestEnum> original = Arrays.asList(
                    TestEnum.VALUE_TWO,
                    TestEnum.VALUE_ONE,
                    TestEnum.VALUE_ONE,
                    TestEnum.VALUE_THREE);

            String encoded = codec.encode(original);
            List<TestEnum> decoded = codec.decode(encoded);

            assertThat(decoded).isEqualTo(original);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("handles enum with single value")
        void testHandles_EnumWithSingleValue() {
            MultiEnumCodec<SingleValueEnum> codec = new MultiEnumCodec<>(SingleValueEnum.class);

            List<SingleValueEnum> original = Arrays.asList(
                    SingleValueEnum.ONLY_VALUE,
                    SingleValueEnum.ONLY_VALUE);

            String encoded = codec.encode(original);
            List<SingleValueEnum> decoded = codec.decode(encoded);

            assertThat(encoded).isEqualTo("ONLY_VALUE;ONLY_VALUE");
            assertThat(decoded).isEqualTo(original);
        }

        @Test
        @DisplayName("handles enums with similar names")
        void testHandles_EnumsWithSimilarNames() {
            MultiEnumCodec<SimilarNamesEnum> codec = new MultiEnumCodec<>(SimilarNamesEnum.class);

            List<SimilarNamesEnum> original = Arrays.asList(
                    SimilarNamesEnum.A,
                    SimilarNamesEnum.AA,
                    SimilarNamesEnum.AAA);

            String encoded = codec.encode(original);
            List<SimilarNamesEnum> decoded = codec.decode(encoded);

            assertThat(encoded).isEqualTo("A;AA;AAA");
            assertThat(decoded).isEqualTo(original);
        }

        @Test
        @DisplayName("decode error message includes available values")
        void testDecodeErrorMessage_IncludesAvailableValues() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            assertThatThrownBy(() -> codec.decode("INVALID"))
                    .hasMessageContaining("Available values:")
                    .hasMessageContaining("VALUE_ONE")
                    .hasMessageContaining("VALUE_TWO")
                    .hasMessageContaining("VALUE_THREE");
        }

        @Test
        @DisplayName("handles string with trailing semicolon (removes trailing empty strings)")
        void testHandles_StringWithTrailingSemicolon() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            // Trailing semicolon is ignored by split() - trailing empty strings are removed
            List<TestEnum> result = codec.decode("VALUE_ONE;VALUE_TWO;");

            assertThat(result).containsExactly(TestEnum.VALUE_ONE, TestEnum.VALUE_TWO);
        }

        @Test
        @DisplayName("handles string with leading semicolon")
        void testHandles_StringWithLeadingSemicolon() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            // Leading semicolon creates an empty string in split result
            assertThatThrownBy(() -> codec.decode(";VALUE_ONE;VALUE_TWO"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum ''");
        }

        @Test
        @DisplayName("handles string with consecutive semicolons")
        void testHandles_StringWithConsecutiveSemicolons() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            // Consecutive semicolons create empty strings in split result
            assertThatThrownBy(() -> codec.decode("VALUE_ONE;;VALUE_TWO"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum ''");
        }

        @Test
        @DisplayName("decode is case sensitive for enum names")
        void testDecode_IsCaseSensitiveForEnumNames() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            assertThatThrownBy(() -> codec.decode("value_one"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum 'value_one'");
        }
    }

    @Nested
    @DisplayName("Separator Handling")
    class SeparatorHandlingTests {

        @Test
        @DisplayName("uses semicolon as separator consistently")
        void testUses_SemicolonAsSeparatorConsistently() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            List<TestEnum> values = Arrays.asList(
                    TestEnum.VALUE_ONE,
                    TestEnum.VALUE_TWO);

            String encoded = codec.encode(values);

            assertThat(encoded).contains(";");
            assertThat(encoded).isEqualTo("VALUE_ONE;VALUE_TWO");
        }

        @Test
        @DisplayName("decode splits on semicolon correctly")
        void testDecode_SplitsOnSemicolonCorrectly() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            List<TestEnum> decoded = codec.decode("VALUE_ONE;VALUE_TWO;VALUE_THREE");

            assertThat(decoded).hasSize(3);
            assertThat(decoded).containsExactly(
                    TestEnum.VALUE_ONE,
                    TestEnum.VALUE_TWO,
                    TestEnum.VALUE_THREE);
        }

        @Test
        @DisplayName("handles single value without separator")
        void testHandles_SingleValueWithoutSeparator() {
            MultiEnumCodec<TestEnum> codec = new MultiEnumCodec<>(TestEnum.class);

            List<TestEnum> decoded = codec.decode("VALUE_ONE");

            assertThat(decoded).hasSize(1);
            assertThat(decoded).containsExactly(TestEnum.VALUE_ONE);
        }
    }
}
