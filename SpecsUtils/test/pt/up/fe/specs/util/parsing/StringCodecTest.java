package pt.up.fe.specs.util.parsing;

import static org.assertj.core.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Comprehensive test suite for StringCodec interface and GenericCodec
 * implementation.
 * Tests encoding/decoding functionality, custom implementations, and
 * serialization.
 * 
 * @author Generated Tests
 */
@DisplayName("StringCodec Tests")
class StringCodecTest {

    @Nested
    @DisplayName("Default Interface Methods Tests")
    class DefaultInterfaceMethodsTests {

        @Test
        @DisplayName("default encode should use toString")
        void testDefaultEncode() {
            // Setup - Create a simple codec with only decode implemented
            StringCodec<Integer> codec = new StringCodec<Integer>() {
                @Override
                public Integer decode(String value) {
                    return Integer.parseInt(value);
                }
            };

            // Execute & Verify
            assertThat(codec.encode(42)).isEqualTo("42");
            assertThat(codec.encode(0)).isEqualTo("0");
            assertThat(codec.encode(-123)).isEqualTo("-123");
        }

        @Test
        @DisplayName("default encode should handle toString of complex objects")
        void testDefaultEncodeComplexObjects() {
            // Setup
            StringCodec<List<String>> codec = new StringCodec<List<String>>() {
                @Override
                public List<String> decode(String value) {
                    return Arrays.asList(value.split(","));
                }
            };

            List<String> testList = Arrays.asList("a", "b", "c");

            // Execute
            String encoded = codec.encode(testList);

            // Verify - uses List.toString()
            assertThat(encoded).isEqualTo("[a, b, c]");
        }
    }

    @Nested
    @DisplayName("GenericCodec Creation Tests")
    class GenericCodecCreationTests {

        @Test
        @DisplayName("newInstance should create functional codec")
        void testNewInstance() {
            // Setup
            Function<Integer, String> encoder = Object::toString;
            Function<String, Integer> decoder = Integer::parseInt;

            // Execute
            StringCodec<Integer> codec = StringCodec.newInstance(encoder, decoder);

            // Verify
            assertThat(codec).isNotNull();
            assertThat(codec.encode(42)).isEqualTo("42");
            assertThat(codec.decode("42")).isEqualTo(42);
        }

        @Test
        @DisplayName("should handle null encoder function")
        void testNullEncoder() {
            // Setup
            Function<String, String> decoder = s -> s;

            // Execute - GenericCodec accepts null encoder
            StringCodec<String> codec = StringCodec.newInstance(null, decoder);

            // Verify - codec is created but will fail when encode is called
            assertThat(codec).isNotNull();
            assertThatThrownBy(() -> codec.encode("test"))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should handle null decoder function")
        void testNullDecoder() {
            // Setup
            Function<String, String> encoder = s -> s;

            // Execute - GenericCodec accepts null decoder
            StringCodec<String> codec = StringCodec.newInstance(encoder, null);

            // Verify - codec is created but will fail when decode is called
            assertThat(codec).isNotNull();
            assertThatThrownBy(() -> codec.decode("test"))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("String Codec Implementation Tests")
    class StringCodecImplementationTests {

        @Test
        @DisplayName("should encode and decode integers")
        void testIntegerCodec() {
            // Setup
            StringCodec<Integer> codec = StringCodec.newInstance(
                    Object::toString,
                    Integer::parseInt);

            // Execute & Verify
            assertThat(codec.encode(123)).isEqualTo("123");
            assertThat(codec.decode("123")).isEqualTo(123);

            assertThat(codec.encode(-456)).isEqualTo("-456");
            assertThat(codec.decode("-456")).isEqualTo(-456);

            assertThat(codec.encode(0)).isEqualTo("0");
            assertThat(codec.decode("0")).isEqualTo(0);
        }

        @Test
        @DisplayName("should encode and decode doubles")
        void testDoubleCodec() {
            // Setup
            StringCodec<Double> codec = StringCodec.newInstance(
                    Object::toString,
                    Double::parseDouble);

            // Execute & Verify
            assertThat(codec.encode(3.14159)).isEqualTo("3.14159");
            assertThat(codec.decode("3.14159")).isEqualTo(3.14159);

            assertThat(codec.encode(Double.MAX_VALUE)).isEqualTo(String.valueOf(Double.MAX_VALUE));
            assertThat(codec.decode(String.valueOf(Double.MAX_VALUE))).isEqualTo(Double.MAX_VALUE);
        }

        @Test
        @DisplayName("should encode and decode booleans")
        void testBooleanCodec() {
            // Setup
            StringCodec<Boolean> codec = StringCodec.newInstance(
                    Object::toString,
                    Boolean::parseBoolean);

            // Execute & Verify
            assertThat(codec.encode(true)).isEqualTo("true");
            assertThat(codec.decode("true")).isEqualTo(true);

            assertThat(codec.encode(false)).isEqualTo("false");
            assertThat(codec.decode("false")).isEqualTo(false);

            // Test case insensitive decoding
            assertThat(codec.decode("TRUE")).isEqualTo(true);
            assertThat(codec.decode("FALSE")).isEqualTo(false);
        }
    }

    @Nested
    @DisplayName("Custom Codec Tests")
    class CustomCodecTests {

        @Test
        @DisplayName("should handle custom date codec")
        void testDateCodec() {
            // Setup
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            StringCodec<LocalDate> codec = StringCodec.newInstance(
                    date -> date.format(formatter),
                    dateStr -> LocalDate.parse(dateStr, formatter));

            LocalDate testDate = LocalDate.of(2023, 12, 25);

            // Execute & Verify
            assertThat(codec.encode(testDate)).isEqualTo("2023-12-25");
            assertThat(codec.decode("2023-12-25")).isEqualTo(testDate);
        }

        @Test
        @DisplayName("should handle custom list codec")
        void testListCodec() {
            // Setup
            StringCodec<List<String>> codec = StringCodec.newInstance(
                    list -> String.join(",", list),
                    str -> Arrays.asList(str.split(",")));

            List<String> testList = Arrays.asList("apple", "banana", "cherry");

            // Execute & Verify
            assertThat(codec.encode(testList)).isEqualTo("apple,banana,cherry");
            assertThat(codec.decode("apple,banana,cherry")).isEqualTo(testList);
        }

        @Test
        @DisplayName("should handle custom object codec")
        void testCustomObjectCodec() {
            // Setup - Simple Person class representation
            StringCodec<Person> codec = StringCodec.newInstance(
                    person -> person.name + ":" + person.age,
                    str -> {
                        String[] parts = str.split(":");
                        return new Person(parts[0], Integer.parseInt(parts[1]));
                    });

            Person testPerson = new Person("John", 30);

            // Execute & Verify
            assertThat(codec.encode(testPerson)).isEqualTo("John:30");

            Person decoded = codec.decode("John:30");
            assertThat(decoded.name).isEqualTo("John");
            assertThat(decoded.age).isEqualTo(30);
        }

        // Helper class for testing
        private static class Person {
            final String name;
            final int age;

            Person(String name, int age) {
                this.name = name;
                this.age = age;
            }
        }
    }

    @Nested
    @DisplayName("Null Handling Tests")
    class NullHandlingTests {

        @Test
        @DisplayName("should handle null values in custom encoder")
        void testNullHandlingInEncoder() {
            // Setup - Codec that handles null values
            StringCodec<String> codec = StringCodec.newInstance(
                    value -> value == null ? "NULL" : value,
                    str -> "NULL".equals(str) ? null : str);

            // Execute & Verify
            assertThat(codec.encode(null)).isEqualTo("NULL");
            assertThat(codec.decode("NULL")).isNull();

            assertThat(codec.encode("test")).isEqualTo("test");
            assertThat(codec.decode("test")).isEqualTo("test");
        }

        @Test
        @DisplayName("should handle null input in decoder")
        void testNullInputDecoder() {
            // Setup - Codec that supports null strings as recommended
            StringCodec<String> codec = StringCodec.newInstance(
                    value -> value,
                    str -> str == null ? "default" : str);

            // Execute & Verify
            assertThat(codec.decode(null)).isEqualTo("default");
            assertThat(codec.decode("")).isEqualTo("");
            assertThat(codec.decode("test")).isEqualTo("test");
        }

        @Test
        @DisplayName("default encode should handle null with NPE")
        void testDefaultEncodeWithNull() {
            // Setup
            StringCodec<String> codec = new StringCodec<String>() {
                @Override
                public String decode(String value) {
                    return value;
                }
            };

            // Execute & Verify - default encode calls toString() which will fail on null
            assertThatThrownBy(() -> codec.encode(null))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Roundtrip Tests")
    class RoundtripTests {

        @ParameterizedTest
        @ValueSource(ints = { -1000, -1, 0, 1, 1000, Integer.MAX_VALUE, Integer.MIN_VALUE })
        @DisplayName("should roundtrip integers correctly")
        void testIntegerRoundtrip(int value) {
            // Setup
            StringCodec<Integer> codec = StringCodec.newInstance(
                    Object::toString,
                    Integer::parseInt);

            // Execute & Verify
            String encoded = codec.encode(value);
            Integer decoded = codec.decode(encoded);
            assertThat(decoded).isEqualTo(value);
        }

        @Test
        @DisplayName("should roundtrip complex objects")
        void testComplexObjectRoundtrip() {
            // Setup
            StringCodec<BigDecimal> codec = StringCodec.newInstance(
                    BigDecimal::toString,
                    BigDecimal::new);

            BigDecimal[] testValues = {
                    new BigDecimal("0"),
                    new BigDecimal("3.14159265359"),
                    new BigDecimal("-123.456"),
                    new BigDecimal("1E+10"),
                    new BigDecimal("0.000000001")
            };

            // Execute & Verify
            for (BigDecimal original : testValues) {
                String encoded = codec.encode(original);
                BigDecimal decoded = codec.decode(encoded);
                assertThat(decoded).isEqualTo(original);
            }
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("should propagate decoder exceptions")
        void testDecoderExceptions() {
            // Setup
            StringCodec<Integer> codec = StringCodec.newInstance(
                    Object::toString,
                    Integer::parseInt);

            // Execute & Verify
            assertThatThrownBy(() -> codec.decode("not-a-number"))
                    .isInstanceOf(NumberFormatException.class);

            assertThatThrownBy(() -> codec.decode("3.14"))
                    .isInstanceOf(NumberFormatException.class);
        }

        @Test
        @DisplayName("should propagate encoder exceptions")
        void testEncoderExceptions() {
            // Setup - Encoder that throws for specific values
            StringCodec<String> codec = StringCodec.newInstance(
                    value -> {
                        if ("error".equals(value)) {
                            throw new IllegalArgumentException("Error value not allowed");
                        }
                        return value;
                    },
                    Function.identity());

            // Execute & Verify
            assertThatThrownBy(() -> codec.encode("error"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Error value not allowed");

            // Normal values should work
            assertThat(codec.encode("normal")).isEqualTo("normal");
        }
    }

    @Nested
    @DisplayName("Serialization Tests")
    class SerializationTests {

        @Test
        @DisplayName("should serialize and deserialize GenericCodec with serializable functions")
        void testSerialization() throws Exception {
            // Setup - Create codec with serializable functions
            StringCodec<Integer> originalCodec = createSerializableIntegerCodec();

            // Execute - Serialize
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
                oos.writeObject(originalCodec);
            }

            // Execute - Deserialize
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            StringCodec<Integer> deserializedCodec;
            try (ObjectInputStream ois = new ObjectInputStream(bis)) {
                @SuppressWarnings("unchecked")
                StringCodec<Integer> codec = (StringCodec<Integer>) ois.readObject();
                deserializedCodec = codec;
            }

            // Verify
            assertThat(deserializedCodec).isNotNull();
            assertThat(deserializedCodec.encode(42)).isEqualTo("42");
            assertThat(deserializedCodec.decode("42")).isEqualTo(42);
        }

        private StringCodec<Integer> createSerializableIntegerCodec() {
            return StringCodec.newInstance(
                    new SerializableEncoder(),
                    new SerializableDecoder());
        }

        private static class SerializableEncoder implements Function<Integer, String>, Serializable {
            @Override
            public String apply(Integer value) {
                return value.toString();
            }
        }

        private static class SerializableDecoder implements Function<String, Integer>, Serializable {
            @Override
            public Integer apply(String value) {
                return Integer.parseInt(value);
            }
        }
    }

    @Nested
    @DisplayName("Usage Pattern Tests")
    class UsagePatternTests {

        @Test
        @DisplayName("should work as configuration value codec")
        void testConfigurationCodec() {
            // Setup - Simulate configuration property codec
            StringCodec<Boolean> boolCodec = StringCodec.newInstance(
                    value -> value ? "enabled" : "disabled",
                    str -> "enabled".equals(str));

            // Execute & Verify
            assertThat(boolCodec.encode(true)).isEqualTo("enabled");
            assertThat(boolCodec.encode(false)).isEqualTo("disabled");

            assertThat(boolCodec.decode("enabled")).isTrue();
            assertThat(boolCodec.decode("disabled")).isFalse();
            assertThat(boolCodec.decode("invalid")).isFalse(); // Boolean.parseBoolean behavior
        }

        @Test
        @DisplayName("should work for enum serialization")
        void testEnumCodec() {
            // Setup
            StringCodec<TestEnum> enumCodec = StringCodec.newInstance(
                    Enum::name,
                    TestEnum::valueOf);

            // Execute & Verify
            assertThat(enumCodec.encode(TestEnum.VALUE_ONE)).isEqualTo("VALUE_ONE");
            assertThat(enumCodec.encode(TestEnum.VALUE_TWO)).isEqualTo("VALUE_TWO");

            assertThat(enumCodec.decode("VALUE_ONE")).isEqualTo(TestEnum.VALUE_ONE);
            assertThat(enumCodec.decode("VALUE_TWO")).isEqualTo(TestEnum.VALUE_TWO);

            // Test error case
            assertThatThrownBy(() -> enumCodec.decode("INVALID_VALUE"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        private enum TestEnum {
            VALUE_ONE, VALUE_TWO
        }
    }
}
