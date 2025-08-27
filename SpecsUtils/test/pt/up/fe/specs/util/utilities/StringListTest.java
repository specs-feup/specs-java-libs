package pt.up.fe.specs.util.utilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Unit tests for {@link StringList}.
 * 
 * Tests list wrapper for strings with encoding/decoding capabilities.
 * 
 * @author Generated Tests
 */
@DisplayName("StringList")
class StringListTest {

    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create empty list")
        void shouldCreateEmptyList() {
            StringList list = new StringList();

            assertThat(list.getStringList()).isEmpty();
        }

        @Test
        @DisplayName("should create from encoded string")
        void shouldCreateFromEncodedString() {
            StringList list = new StringList("value1;value2;value3");

            assertThat(list.getStringList()).containsExactly("value1", "value2", "value3");
        }

        @Test
        @DisplayName("should create from null string")
        void shouldCreateFromNullString() {
            StringList list = new StringList((String) null);

            assertThat(list.getStringList()).isEmpty();
        }

        @Test
        @DisplayName("should create from empty string")
        void shouldCreateFromEmptyString() {
            StringList list = new StringList("");

            assertThat(list.getStringList()).containsExactly("");
        }

        @Test
        @DisplayName("should create from collection")
        void shouldCreateFromCollection() {
            List<String> strings = Arrays.asList("a", "b", "c");
            StringList list = new StringList(strings);

            assertThat(list.getStringList()).containsExactly("a", "b", "c");
            // Should be a copy, not the same reference
            assertThat(list.getStringList()).isNotSameAs(strings);
        }

        @Test
        @DisplayName("should create from enum class")
        void shouldCreateFromEnumClass() {
            StringList list = new StringList(TestEnum.class);

            assertThat(list.getStringList()).containsExactly("VALUE1", "VALUE2", "VALUE3");
        }

        @Test
        @DisplayName("should create from file list")
        void shouldCreateFromFileList() {
            List<File> files = Arrays.asList(
                    new File("/path/to/file1.txt"),
                    new File("/path/to/file2.txt"));

            StringList list = StringList.newInstanceFromListOfFiles(files);

            assertThat(list.getStringList()).containsExactly(
                    "/path/to/file1.txt",
                    "/path/to/file2.txt");
        }

        @Test
        @DisplayName("should create from varargs")
        void shouldCreateFromVarargs() {
            StringList list = StringList.newInstance("value1", "value2", "value3");

            assertThat(list.getStringList()).containsExactly("value1", "value2", "value3");
        }

        @Test
        @DisplayName("should create from empty varargs")
        void shouldCreateFromEmptyVarargs() {
            StringList list = StringList.newInstance();

            assertThat(list.getStringList()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Encoding and Decoding")
    class EncodingAndDecoding {

        @Test
        @DisplayName("should encode to semicolon-separated string")
        void shouldEncodeToSemicolonSeparatedString() {
            StringList list = StringList.newInstance("value1", "value2", "value3");

            StringCodec<StringList> codec = StringList.getCodec();
            String encoded = codec.encode(list);

            assertThat(encoded).isEqualTo("value1;value2;value3");
        }

        @Test
        @DisplayName("should decode from semicolon-separated string")
        void shouldDecodeFromSemicolonSeparatedString() {
            StringCodec<StringList> codec = StringList.getCodec();
            StringList decoded = codec.decode("value1;value2;value3");

            assertThat(decoded.getStringList()).containsExactly("value1", "value2", "value3");
        }

        @Test
        @DisplayName("should encode empty list")
        void shouldEncodeEmptyList() {
            StringList list = new StringList();

            StringCodec<StringList> codec = StringList.getCodec();
            String encoded = codec.encode(list);

            assertThat(encoded).isEmpty();
        }

        @Test
        @DisplayName("should encode single value")
        void shouldEncodeSingleValue() {
            StringList list = StringList.newInstance("single");

            StringCodec<StringList> codec = StringList.getCodec();
            String encoded = codec.encode(list);

            assertThat(encoded).isEqualTo("single");
        }

        @Test
        @DisplayName("should encode varargs")
        void shouldEncodeVarargs() {
            String encoded = StringList.encode("value1", "value2", "value3");

            assertThat(encoded).isEqualTo("value1;value2;value3");
        }

        @Test
        @DisplayName("should handle values with separators")
        void shouldHandleValuesWithSeparators() {
            StringList list = StringList.newInstance("value;1", "value;2");

            StringCodec<StringList> codec = StringList.getCodec();
            String encoded = codec.encode(list);

            assertThat(encoded).isEqualTo("value;1;value;2");

            // Note: This is a limitation - decoding will not work correctly
            StringList decoded = codec.decode(encoded);
            assertThat(decoded.getStringList()).containsExactly("value", "1", "value", "2");
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentation {

        @Test
        @DisplayName("should provide list-style toString")
        void shouldProvideListStyleToString() {
            StringList list = StringList.newInstance("a", "b", "c");

            assertThat(list.toString()).isEqualTo("[a, b, c]");
        }

        @Test
        @DisplayName("should provide empty list toString")
        void shouldProvideEmptyListToString() {
            StringList list = new StringList();

            assertThat(list.toString()).isEqualTo("[]");
        }
    }

    @Nested
    @DisplayName("Equality and Hashing")
    class EqualityAndHashing {

        @Test
        @DisplayName("should be equal to itself")
        void shouldBeEqualToItself() {
            StringList list = StringList.newInstance("a", "b", "c");

            assertThat(list).isEqualTo(list);
        }

        @Test
        @DisplayName("should be equal to list with same values")
        void shouldBeEqualToListWithSameValues() {
            StringList list1 = StringList.newInstance("a", "b", "c");
            StringList list2 = StringList.newInstance("a", "b", "c");

            assertThat(list1).isEqualTo(list2);
            assertThat(list1.hashCode()).isEqualTo(list2.hashCode());
        }

        @Test
        @DisplayName("should not be equal to list with different values")
        void shouldNotBeEqualToListWithDifferentValues() {
            StringList list1 = StringList.newInstance("a", "b", "c");
            StringList list2 = StringList.newInstance("a", "b", "d");

            assertThat(list1).isNotEqualTo(list2);
        }

        @Test
        @DisplayName("should not be equal to null")
        void shouldNotBeEqualToNull() {
            StringList list = StringList.newInstance("a", "b", "c");

            assertThat(list).isNotEqualTo(null);
        }

        @Test
        @DisplayName("should not be equal to different type")
        void shouldNotBeEqualToDifferentType() {
            StringList list = StringList.newInstance("a", "b", "c");

            assertThat(list).isNotEqualTo("not a StringList");
        }

        @Test
        @DisplayName("should handle empty lists equality")
        void shouldHandleEmptyListsEquality() {
            StringList list1 = new StringList();
            StringList list2 = new StringList();

            assertThat(list1).isEqualTo(list2);
            assertThat(list1.hashCode()).isEqualTo(list2.hashCode());
        }
    }

    @Nested
    @DisplayName("Iteration")
    class Iteration {

        @Test
        @DisplayName("should be iterable")
        void shouldBeIterable() {
            StringList list = StringList.newInstance("a", "b", "c");

            assertThat(list).containsExactly("a", "b", "c");
        }

        @Test
        @DisplayName("should provide stream")
        void shouldProvideStream() {
            StringList list = StringList.newInstance("a", "b", "c");

            List<String> collected = list.stream()
                    .map(String::toUpperCase)
                    .collect(Collectors.toList());

            assertThat(collected).containsExactly("A", "B", "C");
        }

        @Test
        @DisplayName("should handle empty iteration")
        void shouldHandleEmptyIteration() {
            StringList list = new StringList();

            assertThat(list).isEmpty();
        }
    }

    @Nested
    @DisplayName("Static Methods")
    class StaticMethods {

        @Test
        @DisplayName("should provide default separator")
        void shouldProvideDefaultSeparator() {
            assertThat(StringList.getDefaultSeparator()).isEqualTo(";");
        }

        @Test
        @DisplayName("should create from empty file list")
        void shouldCreateFromEmptyFileList() {
            StringList list = StringList.newInstanceFromListOfFiles(Collections.emptyList());

            assertThat(list.getStringList()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle null values in collection")
        void shouldHandleNullValuesInCollection() {
            StringList list = new StringList(Arrays.asList("a", null, "c"));

            assertThat(list.getStringList()).containsExactly("a", null, "c");
        }

        @Test
        @DisplayName("should handle empty string values")
        void shouldHandleEmptyStringValues() {
            StringList list = StringList.newInstance("", "value", "");

            assertThat(list.getStringList()).containsExactly("", "value", "");
        }

        @Test
        @DisplayName("should handle single semicolon")
        void shouldHandleSingleSemicolon() {
            StringList list = new StringList(";");

            // Due to split() behavior: ";" becomes [] not ["", ""]
            assertThat(list.getStringList()).isEmpty();
        }

        @Test
        @DisplayName("should handle multiple consecutive semicolons")
        void shouldHandleMultipleConsecutiveSemicolons() {
            StringList list = new StringList("a;;b");

            assertThat(list.getStringList()).containsExactly("a", "", "b");
        }

        @Test
        @DisplayName("should handle round-trip encoding with special cases")
        void shouldHandleRoundTripEncodingWithSpecialCases() {
            StringList original = StringList.newInstance("", "a", "", "b", "");

            StringCodec<StringList> codec = StringList.getCodec();
            String encoded = codec.encode(original);
            StringList decoded = codec.decode(encoded);

            // Due to split() bug: trailing empty string is lost
            assertThat(decoded.getStringList()).containsExactly("", "a", "", "b");
            assertThat(decoded).isNotEqualTo(original); // Round-trip fails
        }

        @Test
        @DisplayName("should handle large enum")
        void shouldHandleLargeEnum() {
            StringList list = new StringList(TestEnum.class);

            assertThat(list.getStringList()).hasSize(3);
            assertThat(list.getStringList()).allMatch(s -> s.startsWith("VALUE"));
        }
    }
}
