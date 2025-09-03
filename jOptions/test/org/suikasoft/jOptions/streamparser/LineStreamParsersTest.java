package org.suikasoft.jOptions.streamparser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.collections.MultiMap;
import pt.up.fe.specs.util.enums.EnumHelperWithValue;
import pt.up.fe.specs.util.providers.StringProvider;
import pt.up.fe.specs.util.utilities.LineStream;

/**
 * Comprehensive test suite for LineStreamParsers utility class.
 * Tests all static parsing methods for various data types including primitives,
 * enums, collections, optional values, and validation utilities.
 * 
 * @author Generated Tests
 */
class LineStreamParsersTest {

    @Nested
    @DisplayName("Boolean Parsing Tests")
    class BooleanParsingTests {

        @Test
        @DisplayName("Should parse '1' as true from string")
        void testOneOrZeroStringTrue() {
            boolean result = LineStreamParsers.oneOrZero("1");

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("Should parse '0' as false from string")
        void testOneOrZeroStringFalse() {
            boolean result = LineStreamParsers.oneOrZero("0");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("Should throw exception for invalid string")
        void testOneOrZeroStringInvalid() {
            assertThatThrownBy(() -> LineStreamParsers.oneOrZero("true"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Unexpected value: 'true'");

            assertThatThrownBy(() -> LineStreamParsers.oneOrZero("false"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Unexpected value: 'false'");

            assertThatThrownBy(() -> LineStreamParsers.oneOrZero("2"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Unexpected value: '2'");
        }

        @Test
        @DisplayName("Should handle edge cases for boolean parsing")
        void testOneOrZeroStringEdgeCases() {
            assertThatThrownBy(() -> LineStreamParsers.oneOrZero(""))
                    .isInstanceOf(RuntimeException.class);

            assertThatThrownBy(() -> LineStreamParsers.oneOrZero(" 1 "))
                    .isInstanceOf(RuntimeException.class);

            assertThatThrownBy(() -> LineStreamParsers.oneOrZero("1.0"))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should parse boolean from LineStream")
        void testOneOrZeroLineStream() {
            LineStream lines = LineStream.newInstance("1\n0\n");

            boolean first = LineStreamParsers.oneOrZero(lines);
            boolean second = LineStreamParsers.oneOrZero(lines);

            assertThat(first).isTrue();
            assertThat(second).isFalse();
        }

        @Test
        @DisplayName("Should handle invalid boolean in LineStream")
        void testOneOrZeroLineStreamInvalid() {
            LineStream lines = LineStream.newInstance("invalid");

            assertThatThrownBy(() -> LineStreamParsers.oneOrZero(lines))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Unexpected value: 'invalid'");
        }
    }

    @Nested
    @DisplayName("Numeric Parsing Tests")
    class NumericParsingTests {

        @Test
        @DisplayName("Should parse integer from LineStream")
        void testIntegerParsing() {
            LineStream lines = LineStream.newInstance("42\n-123\n0\n");

            int first = LineStreamParsers.integer(lines);
            int second = LineStreamParsers.integer(lines);
            int third = LineStreamParsers.integer(lines);

            assertThat(first).isEqualTo(42);
            assertThat(second).isEqualTo(-123);
            assertThat(third).isEqualTo(0);
        }

        @Test
        @DisplayName("Should handle invalid integer")
        void testIntegerParsingInvalid() {
            LineStream lines = LineStream.newInstance("not_a_number");

            assertThatThrownBy(() -> LineStreamParsers.integer(lines))
                    .isInstanceOf(NumberFormatException.class);
        }

        @Test
        @DisplayName("Should parse long from LineStream")
        void testLongParsing() {
            LineStream lines = LineStream.newInstance("123456789012345\n-987654321098765\n0\n");

            long first = LineStreamParsers.longInt(lines);
            long second = LineStreamParsers.longInt(lines);
            long third = LineStreamParsers.longInt(lines);

            assertThat(first).isEqualTo(123456789012345L);
            assertThat(second).isEqualTo(-987654321098765L);
            assertThat(third).isEqualTo(0L);
        }

        @Test
        @DisplayName("Should handle invalid long")
        void testLongParsingInvalid() {
            LineStream lines = LineStream.newInstance("not_a_long");

            assertThatThrownBy(() -> LineStreamParsers.longInt(lines))
                    .isInstanceOf(NumberFormatException.class);
        }

        @Test
        @DisplayName("Should handle overflow for integer parsing")
        void testIntegerOverflow() {
            LineStream lines = LineStream.newInstance("999999999999999999999");

            assertThatThrownBy(() -> LineStreamParsers.integer(lines))
                    .isInstanceOf(NumberFormatException.class);
        }
    }

    @Nested
    @DisplayName("Enum Parsing Tests")
    class EnumParsingTests {

        private enum TestEnum {
            FIRST, SECOND, THIRD
        }

        private enum TestEnumWithValue implements StringProvider {
            VALUE_ONE(1, "one"),
            VALUE_TWO(2, "two"),
            VALUE_THREE(3, "three");

            private final int intValue;
            private final String stringValue;

            TestEnumWithValue(int intValue, String stringValue) {
                this.intValue = intValue;
                this.stringValue = stringValue;
            }

            public int getIntValue() {
                return intValue;
            }

            @Override
            public String getString() {
                return stringValue;
            }
        }

        @Test
        @DisplayName("Should parse enum from ordinal")
        void testEnumFromOrdinal() {
            LineStream lines = LineStream.newInstance("0\n1\n2\n");

            TestEnum first = LineStreamParsers.enumFromOrdinal(TestEnum.class, lines);
            TestEnum second = LineStreamParsers.enumFromOrdinal(TestEnum.class, lines);
            TestEnum third = LineStreamParsers.enumFromOrdinal(TestEnum.class, lines);

            assertThat(first).isEqualTo(TestEnum.FIRST);
            assertThat(second).isEqualTo(TestEnum.SECOND);
            assertThat(third).isEqualTo(TestEnum.THIRD);
        }

        @Test
        @DisplayName("Should handle invalid ordinal")
        void testEnumFromOrdinalInvalid() {
            LineStream lines = LineStream.newInstance("5");

            assertThatThrownBy(() -> LineStreamParsers.enumFromOrdinal(TestEnum.class, lines))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Given ordinal '5' is out of range, enum has 3 values");
        }

        @Test
        @DisplayName("Should parse enum from name")
        void testEnumFromName() {
            LineStream lines = LineStream.newInstance("FIRST\nSECOND\nTHIRD\n");

            TestEnum first = LineStreamParsers.enumFromName(TestEnum.class, lines);
            TestEnum second = LineStreamParsers.enumFromName(TestEnum.class, lines);
            TestEnum third = LineStreamParsers.enumFromName(TestEnum.class, lines);

            assertThat(first).isEqualTo(TestEnum.FIRST);
            assertThat(second).isEqualTo(TestEnum.SECOND);
            assertThat(third).isEqualTo(TestEnum.THIRD);
        }

        @Test
        @DisplayName("Should handle invalid enum name by returning first enum value")
        void testEnumFromNameInvalid() {
            LineStream lines = LineStream.newInstance("INVALID_NAME");

            // SpecsEnums.valueOf() catches exceptions and returns the first enum value with
            // a warning
            TestEnum result = LineStreamParsers.enumFromName(TestEnum.class, lines);
            assertThat(result).isEqualTo(TestEnum.FIRST);
        }

        @Test
        @DisplayName("Should parse enum from int value using helper")
        void testEnumFromIntHelper() {
            EnumHelperWithValue<TestEnumWithValue> helper = new EnumHelperWithValue<>(TestEnumWithValue.class) {
                @Override
                public TestEnumWithValue fromValue(int value) {
                    for (TestEnumWithValue e : TestEnumWithValue.values()) {
                        if (e.getIntValue() == value) {
                            return e;
                        }
                    }
                    throw new IllegalArgumentException("No enum with value: " + value);
                }
            };

            LineStream lines = LineStream.newInstance("1\n2\n3\n");

            TestEnumWithValue first = LineStreamParsers.enumFromInt(helper, lines);
            TestEnumWithValue second = LineStreamParsers.enumFromInt(helper, lines);
            TestEnumWithValue third = LineStreamParsers.enumFromInt(helper, lines);

            assertThat(first).isEqualTo(TestEnumWithValue.VALUE_ONE);
            assertThat(second).isEqualTo(TestEnumWithValue.VALUE_TWO);
            assertThat(third).isEqualTo(TestEnumWithValue.VALUE_THREE);
        }

        @Test
        @DisplayName("Should parse enum from name using helper")
        void testEnumFromNameHelper() {
            EnumHelperWithValue<TestEnumWithValue> helper = new EnumHelperWithValue<>(TestEnumWithValue.class);
            // For StringProvider enums, the names are the getString() values, not enum
            // names
            LineStream lines = LineStream.newInstance("one\ntwo\n");

            TestEnumWithValue first = LineStreamParsers.enumFromName(helper, lines);
            TestEnumWithValue second = LineStreamParsers.enumFromName(helper, lines);

            assertThat(first).isEqualTo(TestEnumWithValue.VALUE_ONE);
            assertThat(second).isEqualTo(TestEnumWithValue.VALUE_TWO);
        }

        @Test
        @DisplayName("Should parse enum from string value using helper")
        void testEnumFromValueHelper() {
            EnumHelperWithValue<TestEnumWithValue> helper = new EnumHelperWithValue<>(TestEnumWithValue.class);
            LineStream lines = LineStream.newInstance("one\ntwo\nthree\n");

            TestEnumWithValue first = LineStreamParsers.enumFromValue(helper, lines);
            TestEnumWithValue second = LineStreamParsers.enumFromValue(helper, lines);
            TestEnumWithValue third = LineStreamParsers.enumFromValue(helper, lines);

            assertThat(first).isEqualTo(TestEnumWithValue.VALUE_ONE);
            assertThat(second).isEqualTo(TestEnumWithValue.VALUE_TWO);
            assertThat(third).isEqualTo(TestEnumWithValue.VALUE_THREE);
        }

        @Test
        @DisplayName("Should parse enum list from names")
        void testEnumListFromName() {
            EnumHelperWithValue<TestEnumWithValue> helper = new EnumHelperWithValue<>(TestEnumWithValue.class);
            // For StringProvider enums, use the getString() values as names
            LineStream lines = LineStream.newInstance("3\none\ntwo\nthree\n");

            List<TestEnumWithValue> result = LineStreamParsers.enumListFromName(helper, lines);

            assertThat(result).hasSize(3);
            assertThat(result).containsExactly(
                    TestEnumWithValue.VALUE_ONE,
                    TestEnumWithValue.VALUE_TWO,
                    TestEnumWithValue.VALUE_THREE);
        }

        @Test
        @DisplayName("Should handle empty enum list")
        void testEnumListFromNameEmpty() {
            EnumHelperWithValue<TestEnumWithValue> helper = new EnumHelperWithValue<>(TestEnumWithValue.class);
            LineStream lines = LineStream.newInstance("0\n");

            List<TestEnumWithValue> result = LineStreamParsers.enumListFromName(helper, lines);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Duplicate Checking Tests")
    class DuplicateCheckingTests {

        @Test
        @DisplayName("Should pass for unique key in map")
        void testCheckDuplicateMapUnique() {
            Map<String, String> map = new HashMap<>();
            map.put("existing", "value");

            // Should not throw exception
            LineStreamParsers.checkDuplicate("test", "newkey", "newvalue", map);
        }

        @Test
        @DisplayName("Should throw exception for duplicate key in map")
        void testCheckDuplicateMapDuplicate() {
            Map<String, String> map = new HashMap<>();
            map.put("existing", "oldvalue");

            assertThatThrownBy(() -> LineStreamParsers.checkDuplicate("test", "existing", "newvalue", map))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Duplicate value for id 'test', key 'existing'")
                    .hasMessageContaining("Current value:newvalue")
                    .hasMessageContaining("Previous value:oldvalue");
        }

        @Test
        @DisplayName("Should pass for unique key in set")
        void testCheckDuplicateSetUnique() {
            Set<String> set = new HashSet<>();
            set.add("existing");

            // Should not throw exception
            LineStreamParsers.checkDuplicate("test", "newkey", set);
        }

        @Test
        @DisplayName("Should throw exception for duplicate key in set")
        void testCheckDuplicateSetDuplicate() {
            Set<String> set = new HashSet<>();
            set.add("existing");

            assertThatThrownBy(() -> LineStreamParsers.checkDuplicate("test", "existing", set))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Duplicate value for id 'test', key 'existing'");
        }
    }

    @Nested
    @DisplayName("Map and Set Parsing Tests")
    class MapAndSetParsingTests {

        @Test
        @DisplayName("Should parse string map")
        void testStringMap() {
            LineStream lines = LineStream.newInstance("key1\nvalue1\n");
            Map<String, String> map = new HashMap<>();

            LineStreamParsers.stringMap("test", lines, map);

            assertThat(map).hasSize(1);
            assertThat(map).containsEntry("key1", "value1");
        }

        @Test
        @DisplayName("Should detect duplicate in string map")
        void testStringMapDuplicate() {
            LineStream lines = LineStream.newInstance("key1\nvalue2\n");
            Map<String, String> map = new HashMap<>();
            map.put("key1", "value1");

            assertThatThrownBy(() -> LineStreamParsers.stringMap("test", lines, map))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Duplicate value for id 'test', key 'key1'");
        }

        @Test
        @DisplayName("Should parse string set with duplicate checking")
        void testStringSetWithDuplicateCheck() {
            LineStream lines = LineStream.newInstance("item1\n");
            Set<String> set = new HashSet<>();

            LineStreamParsers.stringSet("test", lines, set);

            assertThat(set).hasSize(1);
            assertThat(set).contains("item1");
        }

        @Test
        @DisplayName("Should detect duplicate in string set")
        void testStringSetDuplicate() {
            LineStream lines = LineStream.newInstance("item1\n");
            Set<String> set = new HashSet<>();
            set.add("item1");

            assertThatThrownBy(() -> LineStreamParsers.stringSet("test", lines, set))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Duplicate value for id 'test', key 'item1'");
        }

        @Test
        @DisplayName("Should parse string set without duplicate checking")
        void testStringSetWithoutDuplicateCheck() {
            LineStream lines = LineStream.newInstance("item1\n");
            Set<String> set = new HashSet<>();
            set.add("item1");

            // Should not throw exception
            LineStreamParsers.stringSet("test", lines, set, false);

            assertThat(set).hasSize(1);
            assertThat(set).contains("item1");
        }

        @Test
        @DisplayName("Should parse multimap with string values")
        void testMultiMapString() {
            LineStream lines = LineStream.newInstance("key1\nvalue1\n");
            MultiMap<String, String> map = new MultiMap<>();

            LineStreamParsers.multiMap(lines, map);

            assertThat(map.get("key1")).contains("value1");
        }

        @Test
        @DisplayName("Should parse multimap with custom decoder")
        void testMultiMapWithDecoder() {
            LineStream lines = LineStream.newInstance("numbers\n42\n");
            MultiMap<String, Integer> map = new MultiMap<>();
            Function<String, Integer> decoder = Integer::parseInt;

            LineStreamParsers.multiMap(lines, map, decoder);

            assertThat(map.get("numbers")).contains(42);
        }

        @Test
        @DisplayName("Should handle multiple values for same key in multimap")
        void testMultiMapMultipleValues() {
            LineStream lines1 = LineStream.newInstance("key1\nvalue1\n");
            LineStream lines2 = LineStream.newInstance("key1\nvalue2\n");
            MultiMap<String, String> map = new MultiMap<>();

            LineStreamParsers.multiMap(lines1, map);
            LineStreamParsers.multiMap(lines2, map);

            assertThat(map.get("key1")).hasSize(2);
            assertThat(map.get("key1")).containsExactly("value1", "value2");
        }
    }

    @Nested
    @DisplayName("List Parsing Tests")
    class ListParsingTests {

        @Test
        @DisplayName("Should parse string list")
        void testStringList() {
            LineStream lines = LineStream.newInstance("3\nfirst\nsecond\nthird\n");

            List<String> result = LineStreamParsers.stringList(lines);

            assertThat(result).hasSize(3);
            assertThat(result).containsExactly("first", "second", "third");
        }

        @Test
        @DisplayName("Should parse empty string list")
        void testStringListEmpty() {
            LineStream lines = LineStream.newInstance("0\n");

            List<String> result = LineStreamParsers.stringList(lines);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should parse string list with custom parser")
        void testStringListWithCustomParser() {
            LineStream lines = LineStream.newInstance("2\nHELLO\nWORLD\n");
            Function<LineStream, String> parser = stream -> stream.nextLine().toLowerCase();

            List<String> result = LineStreamParsers.stringList(lines, parser);

            assertThat(result).hasSize(2);
            assertThat(result).containsExactly("hello", "world");
        }

        @Test
        @DisplayName("Should parse generic list with custom parser")
        void testGenericListWithParser() {
            LineStream lines = LineStream.newInstance("3\n10\n20\n30\n");
            Function<LineStream, Integer> parser = stream -> Integer.parseInt(stream.nextLine());

            List<Integer> result = LineStreamParsers.list(lines, parser);

            assertThat(result).hasSize(3);
            assertThat(result).containsExactly(10, 20, 30);
        }

        @Test
        @DisplayName("Should handle complex objects in list")
        void testGenericListComplexObjects() {
            LineStream lines = LineStream.newInstance("2\nJohn:25\nJane:30\n");
            Function<LineStream, Person> parser = stream -> {
                String line = stream.nextLine();
                String[] parts = line.split(":");
                return new Person(parts[0], Integer.parseInt(parts[1]));
            };

            List<Person> result = LineStreamParsers.list(lines, parser);

            assertThat(result).hasSize(2);
            assertThat(result.get(0).name).isEqualTo("John");
            assertThat(result.get(0).age).isEqualTo(25);
            assertThat(result.get(1).name).isEqualTo("Jane");
            assertThat(result.get(1).age).isEqualTo(30);
        }

        @Test
        @DisplayName("Should handle invalid count in list parsing")
        void testListInvalidCount() {
            LineStream lines = LineStream.newInstance("not_a_number\n");

            assertThatThrownBy(() -> LineStreamParsers.stringList(lines))
                    .isInstanceOf(NumberFormatException.class);
        }
    }

    @Nested
    @DisplayName("Optional String Parsing Tests")
    class OptionalStringParsingTests {

        @Test
        @DisplayName("Should parse non-empty string as Optional.of()")
        void testOptionalStringNonEmpty() {
            LineStream lines = LineStream.newInstance("hello\nworld\n");

            Optional<String> first = LineStreamParsers.optionalString(lines);
            Optional<String> second = LineStreamParsers.optionalString(lines);

            assertThat(first).isPresent();
            assertThat(first.get()).isEqualTo("hello");
            assertThat(second).isPresent();
            assertThat(second.get()).isEqualTo("world");
        }

        @Test
        @DisplayName("Should parse empty string as Optional.empty()")
        void testOptionalStringEmpty() {
            LineStream lines = LineStream.newInstance("\n");

            Optional<String> result = LineStreamParsers.optionalString(lines);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle whitespace strings")
        void testOptionalStringWhitespace() {
            LineStream lines = LineStream.newInstance("   \n\t\n");

            Optional<String> spaces = LineStreamParsers.optionalString(lines);
            Optional<String> tab = LineStreamParsers.optionalString(lines);

            assertThat(spaces).isPresent();
            assertThat(spaces.get()).isEqualTo("   ");
            assertThat(tab).isPresent();
            assertThat(tab.get()).isEqualTo("\t");
        }

        @Test
        @DisplayName("Should handle mixed empty and non-empty strings")
        void testOptionalStringMixed() {
            LineStream lines = LineStream.newInstance("content\n\nmore content\n");

            Optional<String> first = LineStreamParsers.optionalString(lines);
            Optional<String> empty = LineStreamParsers.optionalString(lines);
            Optional<String> third = LineStreamParsers.optionalString(lines);

            assertThat(first).isPresent();
            assertThat(first.get()).isEqualTo("content");
            assertThat(empty).isEmpty();
            assertThat(third).isPresent();
            assertThat(third.get()).isEqualTo("more content");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should parse complex data structure")
        void testComplexDataStructure() {
            // Complex stream: boolean, int, enum list, string map
            String input = "1\n" + // boolean: true
                    "42\n" + // integer: 42
                    "2\n" + // enum list size
                    "one\ntwo\n" + // enum values (getString() values)
                    "config\nvalue\n"; // map entry

            LineStream lines = LineStream.newInstance(input);

            // Parse each component
            boolean boolValue = LineStreamParsers.oneOrZero(lines);
            int intValue = LineStreamParsers.integer(lines);

            // Mock enum helper for integration test
            EnumHelperWithValue<TestEnumWithValue> helper = new EnumHelperWithValue<>(TestEnumWithValue.class);
            // Uses getString() values for StringProvider enums
            List<TestEnumWithValue> enumList = LineStreamParsers.enumListFromName(helper, lines);

            Map<String, String> stringMap = new HashMap<>();
            LineStreamParsers.stringMap("config", lines, stringMap);

            // Verify results
            assertThat(boolValue).isTrue();
            assertThat(intValue).isEqualTo(42);
            assertThat(enumList).hasSize(2);
            assertThat(stringMap).containsEntry("config", "value");
        }

        @Test
        @DisplayName("Should handle error propagation in complex parsing")
        void testErrorPropagationInComplexParsing() {
            String input = "1\n" + // valid boolean
                    "invalid_int\n"; // invalid integer

            LineStream lines = LineStream.newInstance(input);

            // First parse should succeed
            boolean boolValue = LineStreamParsers.oneOrZero(lines);
            assertThat(boolValue).isTrue();

            // Second parse should fail
            assertThatThrownBy(() -> LineStreamParsers.integer(lines))
                    .isInstanceOf(NumberFormatException.class);
        }
    }

    // Helper classes for tests
    private enum TestEnumWithValue implements StringProvider {
        VALUE_ONE(1, "one"),
        VALUE_TWO(2, "two"),
        VALUE_THREE(3, "three");

        private final int intValue;
        private final String stringValue;

        TestEnumWithValue(int intValue, String stringValue) {
            this.intValue = intValue;
            this.stringValue = stringValue;
        }

        @SuppressWarnings("unused")
        public int getIntValue() {
            return intValue;
        }

        @Override
        public String getString() {
            return stringValue;
        }
    }

    private static class Person {
        public final String name;
        public final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }
}
