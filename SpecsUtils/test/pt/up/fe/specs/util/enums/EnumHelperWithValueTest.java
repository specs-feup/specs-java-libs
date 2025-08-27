package pt.up.fe.specs.util.enums;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Comprehensive test suite for EnumHelperWithValue class.
 * 
 * Tests the enhanced enum helper that works with StringProvider enums,
 * providing value-based lookup in addition to name-based lookup.
 * 
 * @author Generated Tests
 */
@DisplayName("EnumHelperWithValue Tests")
class EnumHelperWithValueTest {

    // Test enum implementing StringProvider for value-based testing
    private enum TestEnumWithValue implements StringProvider {
        OPTION_A("alpha"),
        OPTION_B("beta"),
        OPTION_C("gamma"),
        SPECIAL_CHAR("special-char"),
        EMPTY_VALUE(""),
        DUPLICATE_VALUE("beta"); // Duplicate value to test behavior

        private final String value;

        TestEnumWithValue(String value) {
            this.value = value;
        }

        @Override
        public String getString() {
            return value;
        }
    }

    private EnumHelperWithValue<TestEnumWithValue> enumHelper;
    private EnumHelperWithValue<TestEnumWithValue> enumHelperWithExcludes;

    @BeforeEach
    void setUp() {
        enumHelper = new EnumHelperWithValue<>(TestEnumWithValue.class);
        enumHelperWithExcludes = new EnumHelperWithValue<>(TestEnumWithValue.class,
                Arrays.asList(TestEnumWithValue.OPTION_C));
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create EnumHelperWithValue with enum class")
        void testBasicConstructor() {
            EnumHelperWithValue<TestEnumWithValue> helper = new EnumHelperWithValue<>(TestEnumWithValue.class);

            assertThat(helper.getEnumClass()).isEqualTo(TestEnumWithValue.class);
            assertThat(helper.getSize()).isEqualTo(6);
            assertThat(helper.values()).containsExactly(TestEnumWithValue.values());
        }

        @Test
        @DisplayName("Should create EnumHelperWithValue with exclude list")
        void testConstructorWithExcludeList() {
            Collection<TestEnumWithValue> excludeList = Arrays.asList(TestEnumWithValue.OPTION_B,
                    TestEnumWithValue.EMPTY_VALUE);
            EnumHelperWithValue<TestEnumWithValue> helper = new EnumHelperWithValue<>(TestEnumWithValue.class,
                    excludeList);

            assertThat(helper.getEnumClass()).isEqualTo(TestEnumWithValue.class);
            assertThat(helper.getSize()).isEqualTo(6); // Size is still total enum count

            // Excluded items should not be in values translation map
            Map<String, TestEnumWithValue> valuesMap = helper.getValuesTranslationMap();
            assertThat(valuesMap).doesNotContainKey("beta");
            assertThat(valuesMap).doesNotContainKey("");
            assertThat(valuesMap).containsKey("alpha");
            assertThat(valuesMap).containsKey("gamma");
        }

        @Test
        @DisplayName("Should create EnumHelperWithValue with empty exclude list")
        void testConstructorWithEmptyExcludeList() {
            EnumHelperWithValue<TestEnumWithValue> helper = new EnumHelperWithValue<>(TestEnumWithValue.class,
                    Collections.emptyList());

            assertThat(helper.getEnumClass()).isEqualTo(TestEnumWithValue.class);
            Map<String, TestEnumWithValue> valuesMap = helper.getValuesTranslationMap();
            assertThat(valuesMap).containsKey("alpha");
            assertThat(valuesMap).containsKey("beta");
            assertThat(valuesMap).containsKey("gamma");
        }

        @Test
        @DisplayName("Should handle null enum class gracefully - fails on first access")
        void testNullEnumClass() {
            // Constructor accepts null but fails on first lazy access
            EnumHelperWithValue<TestEnumWithValue> helper = new EnumHelperWithValue<TestEnumWithValue>(null);
            assertThatThrownBy(() -> helper.fromValue("alpha"))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Value-Based Lookup Tests")
    class ValueBasedLookupTests {

        @Test
        @DisplayName("Should find enum by string value")
        void testFromValueExactMatch() {
            TestEnumWithValue result = enumHelper.fromValue("alpha");

            assertThat(result).isEqualTo(TestEnumWithValue.OPTION_A);
        }

        @Test
        @DisplayName("Should find all enum values by string value")
        void testFromValueAllValues() {
            assertThat(enumHelper.fromValue("alpha")).isEqualTo(TestEnumWithValue.OPTION_A);
            assertThat(enumHelper.fromValue("beta")).isEqualTo(TestEnumWithValue.DUPLICATE_VALUE); // Last one wins for
                                                                                                   // duplicates
            assertThat(enumHelper.fromValue("gamma")).isEqualTo(TestEnumWithValue.OPTION_C);
            assertThat(enumHelper.fromValue("special-char")).isEqualTo(TestEnumWithValue.SPECIAL_CHAR);
            assertThat(enumHelper.fromValue("")).isEqualTo(TestEnumWithValue.EMPTY_VALUE);
        }

        @Test
        @DisplayName("Should handle duplicate values correctly")
        void testFromValueDuplicateValues() {
            // Both OPTION_B and DUPLICATE_VALUE have "beta" as their string value
            // The behavior depends on which one is mapped first
            TestEnumWithValue result = enumHelper.fromValue("beta");

            assertThat(result).isIn(TestEnumWithValue.OPTION_B, TestEnumWithValue.DUPLICATE_VALUE);
        }

        @Test
        @DisplayName("Should throw exception for unknown value")
        void testFromValueUnknown() {
            assertThatThrownBy(() -> {
                enumHelper.fromValue("unknown");
            }).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("does not contain an enum with the name 'unknown'")
                    .hasMessageContaining("Available enums:");
        }

        @Test
        @DisplayName("Should throw exception for null value")
        void testFromValueNull() {
            assertThatThrownBy(() -> {
                enumHelper.fromValue((String) null);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should be case sensitive for values")
        void testFromValueCaseSensitive() {
            assertThatThrownBy(() -> {
                enumHelper.fromValue("ALPHA");
            }).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Optional Value-Based Lookup Tests")
    class OptionalValueBasedLookupTests {

        @Test
        @DisplayName("Should return present Optional for valid value")
        void testFromValueTryValidValue() {
            Optional<TestEnumWithValue> result = enumHelper.fromValueTry("gamma");

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(TestEnumWithValue.OPTION_C);
        }

        @Test
        @DisplayName("Should return empty Optional for invalid value")
        void testFromValueTryInvalidValue() {
            Optional<TestEnumWithValue> result = enumHelper.fromValueTry("invalid");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty Optional for null value")
        void testFromValueTryNullValue() {
            Optional<TestEnumWithValue> result = enumHelper.fromValueTry(null);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle empty string value")
        void testFromValueTryEmptyValue() {
            Optional<TestEnumWithValue> result = enumHelper.fromValueTry("");

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(TestEnumWithValue.EMPTY_VALUE);
        }

        @Test
        @DisplayName("Should handle special characters in values")
        void testFromValueTrySpecialCharacters() {
            Optional<TestEnumWithValue> result = enumHelper.fromValueTry("special-char");

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(TestEnumWithValue.SPECIAL_CHAR);
        }
    }

    @Nested
    @DisplayName("Index-Based Lookup Tests")
    class IndexBasedLookupTests {

        @Test
        @DisplayName("Should find enum by valid index")
        void testFromValueByIndex() {
            assertThat(enumHelper.fromValue(0)).isEqualTo(TestEnumWithValue.OPTION_A);
            assertThat(enumHelper.fromValue(1)).isEqualTo(TestEnumWithValue.OPTION_B);
            assertThat(enumHelper.fromValue(2)).isEqualTo(TestEnumWithValue.OPTION_C);
            assertThat(enumHelper.fromValue(3)).isEqualTo(TestEnumWithValue.SPECIAL_CHAR);
            assertThat(enumHelper.fromValue(4)).isEqualTo(TestEnumWithValue.EMPTY_VALUE);
            assertThat(enumHelper.fromValue(5)).isEqualTo(TestEnumWithValue.DUPLICATE_VALUE);
        }

        @Test
        @DisplayName("Should throw exception for negative index")
        void testFromValueByIndexNegative() {
            assertThatThrownBy(() -> {
                enumHelper.fromValue(-1);
            }).isInstanceOf(ArrayIndexOutOfBoundsException.class)
                    .hasMessageContaining("Index -1 out of bounds for length 6");
        }

        @Test
        @DisplayName("Should throw exception for index too large")
        void testFromValueByIndexTooLarge() {
            assertThatThrownBy(() -> {
                enumHelper.fromValue(6);
            }).isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Asked for enum at index 6")
                    .hasMessageContaining("but there are only 6 values");
        }

        @Test
        @DisplayName("Should throw exception for very large index")
        void testFromValueByIndexVeryLarge() {
            assertThatThrownBy(() -> {
                enumHelper.fromValue(100);
            }).isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Asked for enum at index 100");
        }
    }

    @Nested
    @DisplayName("List Processing Tests")
    class ListProcessingTests {

        @Test
        @DisplayName("Should convert list of string values to enum list")
        void testFromValueList() {
            List<String> values = Arrays.asList("alpha", "gamma", "beta");
            List<TestEnumWithValue> result = enumHelper.fromValue(values);

            assertThat(result).containsExactly(
                    TestEnumWithValue.OPTION_A,
                    TestEnumWithValue.OPTION_C,
                    TestEnumWithValue.DUPLICATE_VALUE // "beta" resolves to DUPLICATE_VALUE (last one wins)
            );
        }

        @Test
        @DisplayName("Should handle empty list")
        void testFromValueEmptyList() {
            List<String> values = Collections.emptyList();
            List<TestEnumWithValue> result = enumHelper.fromValue(values);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle single item list")
        void testFromValueSingleItemList() {
            List<String> values = Arrays.asList("alpha");
            List<TestEnumWithValue> result = enumHelper.fromValue(values);

            assertThat(result).containsExactly(TestEnumWithValue.OPTION_A);
        }

        @Test
        @DisplayName("Should throw exception for invalid value in list")
        void testFromValueListWithInvalidValue() {
            List<String> values = Arrays.asList("alpha", "invalid", "gamma");

            assertThatThrownBy(() -> {
                enumHelper.fromValue(values);
            }).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("invalid");
        }

        @Test
        @DisplayName("Should handle null values in list")
        void testFromValueListWithNullValue() {
            List<String> values = Arrays.asList("alpha", null, "gamma");

            assertThatThrownBy(() -> {
                enumHelper.fromValue(values);
            }).isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Should handle duplicate values in list")
        void testFromValueListWithDuplicates() {
            List<String> values = Arrays.asList("alpha", "alpha", "gamma");
            List<TestEnumWithValue> result = enumHelper.fromValue(values);

            assertThat(result).containsExactly(
                    TestEnumWithValue.OPTION_A,
                    TestEnumWithValue.OPTION_A,
                    TestEnumWithValue.OPTION_C);
        }
    }

    @Nested
    @DisplayName("Exclude List Tests")
    class ExcludeListTests {

        @Test
        @DisplayName("Should exclude specified enums from value lookup")
        void testExcludedEnumsNotFoundByValue() {
            // OPTION_C is excluded, so "gamma" should not be found
            assertThatThrownBy(() -> {
                enumHelperWithExcludes.fromValue("gamma");
            }).isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("gamma");
        }

        @Test
        @DisplayName("Should exclude specified enums from optional value lookup")
        void testExcludedEnumsNotFoundByValueTry() {
            // OPTION_C is excluded
            Optional<TestEnumWithValue> result = enumHelperWithExcludes.fromValueTry("gamma");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should still find non-excluded enums by value")
        void testNonExcludedEnumsStillFoundByValue() {
            assertThat(enumHelperWithExcludes.fromValue("alpha")).isEqualTo(TestEnumWithValue.OPTION_A);
            assertThat(enumHelperWithExcludes.fromValue("beta")).isIn(TestEnumWithValue.OPTION_B,
                    TestEnumWithValue.DUPLICATE_VALUE);
        }

        @Test
        @DisplayName("Should still access excluded enums by ordinal")
        void testExcludedEnumsStillAccessibleByOrdinal() {
            // OPTION_C is excluded from value lookup but should still be accessible by
            // ordinal
            assertThat(enumHelperWithExcludes.fromOrdinal(2)).isEqualTo(TestEnumWithValue.OPTION_C);
        }

        @Test
        @DisplayName("Should not access excluded enums by name")
        void testExcludedEnumsStillAccessibleByName() {
            // OPTION_C is excluded, so "gamma" should not be accessible by name
            assertThatThrownBy(() -> enumHelperWithExcludes.fromName("gamma"))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should not include excluded enums in values translation map")
        void testExcludedEnumsNotInValuesMap() {
            Map<String, TestEnumWithValue> valuesMap = enumHelperWithExcludes.getValuesTranslationMap();

            assertThat(valuesMap).containsKey("alpha");
            assertThat(valuesMap).containsKey("beta");
            assertThat(valuesMap).doesNotContainKey("gamma"); // Excluded
        }
    }

    @Nested
    @DisplayName("Translation Map Tests")
    class TranslationMapTests {

        @Test
        @DisplayName("Should return correct values translation map")
        void testGetValuesTranslationMap() {
            Map<String, TestEnumWithValue> valuesMap = enumHelper.getValuesTranslationMap();

            assertThat(valuesMap).containsEntry("alpha", TestEnumWithValue.OPTION_A);
            assertThat(valuesMap).containsEntry("gamma", TestEnumWithValue.OPTION_C);
            assertThat(valuesMap).containsEntry("special-char", TestEnumWithValue.SPECIAL_CHAR);
            assertThat(valuesMap).containsEntry("", TestEnumWithValue.EMPTY_VALUE);
        }

        @Test
        @DisplayName("Should return available values string")
        void testGetAvailableValues() {
            String availableValues = enumHelper.getAvailableValues();

            assertThat(availableValues).contains("alpha");
            assertThat(availableValues).contains("beta");
            assertThat(availableValues).contains("gamma");
            assertThat(availableValues).contains("special-char");
        }

        @Test
        @DisplayName("Should add alias to translation map")
        void testAddAlias() {
            EnumHelperWithValue<TestEnumWithValue> helper = enumHelper.addAlias("new-alpha",
                    TestEnumWithValue.OPTION_A);

            assertThat(helper).isSameAs(enumHelper); // Should return same instance
            assertThat(enumHelper.fromValue("new-alpha")).isEqualTo(TestEnumWithValue.OPTION_A);
            assertThat(enumHelper.fromValueTry("new-alpha")).contains(TestEnumWithValue.OPTION_A);
        }

        @Test
        @DisplayName("Should handle multiple aliases for same enum")
        void testMultipleAliases() {
            enumHelper.addAlias("alias1", TestEnumWithValue.OPTION_A);
            enumHelper.addAlias("alias2", TestEnumWithValue.OPTION_A);

            assertThat(enumHelper.fromValue("alias1")).isEqualTo(TestEnumWithValue.OPTION_A);
            assertThat(enumHelper.fromValue("alias2")).isEqualTo(TestEnumWithValue.OPTION_A);
            assertThat(enumHelper.fromValue("alpha")).isEqualTo(TestEnumWithValue.OPTION_A);
        }

        @Test
        @DisplayName("Should override existing values with aliases")
        void testAliasOverridesExistingValue() {
            enumHelper.addAlias("alpha", TestEnumWithValue.OPTION_B);

            // "alpha" should now map to OPTION_B instead of OPTION_A
            assertThat(enumHelper.fromValue("alpha")).isEqualTo(TestEnumWithValue.OPTION_B);
        }
    }

    @Nested
    @DisplayName("Static Factory Methods Tests")
    class StaticFactoryMethodsTests {

        @Test
        @DisplayName("Should create lazy helper with value without excludes")
        void testNewLazyHelperWithValue() {
            Lazy<EnumHelperWithValue<TestEnumWithValue>> lazyHelper = EnumHelperWithValue
                    .newLazyHelperWithValue(TestEnumWithValue.class);

            assertThat(lazyHelper).isNotNull();

            EnumHelperWithValue<TestEnumWithValue> helper = lazyHelper.get();
            assertThat(helper.getEnumClass()).isEqualTo(TestEnumWithValue.class);
            assertThat(helper.fromValue("alpha")).isEqualTo(TestEnumWithValue.OPTION_A);
        }

        @Test
        @DisplayName("Should create lazy helper with value with single exclude")
        void testNewLazyHelperWithValueWithSingleExclude() {
            Lazy<EnumHelperWithValue<TestEnumWithValue>> lazyHelper = EnumHelperWithValue
                    .newLazyHelperWithValue(TestEnumWithValue.class, TestEnumWithValue.OPTION_B);

            assertThat(lazyHelper).isNotNull();

            EnumHelperWithValue<TestEnumWithValue> helper = lazyHelper.get();
            assertThat(helper.fromValueTry("beta")).isEmpty(); // OPTION_B excluded
            assertThat(helper.fromValue("alpha")).isEqualTo(TestEnumWithValue.OPTION_A);
        }

        @Test
        @DisplayName("Should create lazy helper with value with multiple excludes")
        void testNewLazyHelperWithValueWithMultipleExcludes() {
            Collection<TestEnumWithValue> excludes = Arrays.asList(TestEnumWithValue.OPTION_A,
                    TestEnumWithValue.OPTION_C);
            Lazy<EnumHelperWithValue<TestEnumWithValue>> lazyHelper = EnumHelperWithValue
                    .newLazyHelperWithValue(TestEnumWithValue.class, excludes);

            assertThat(lazyHelper).isNotNull();

            EnumHelperWithValue<TestEnumWithValue> helper = lazyHelper.get();
            assertThat(helper.fromValueTry("alpha")).isEmpty(); // OPTION_A excluded
            assertThat(helper.fromValueTry("gamma")).isEmpty(); // OPTION_C excluded
            assertThat(helper.fromValue("beta")).isIn(TestEnumWithValue.OPTION_B, TestEnumWithValue.DUPLICATE_VALUE);
        }

        @Test
        @DisplayName("Should create lazy helper with value with empty exclude list")
        void testNewLazyHelperWithValueWithEmptyExcludes() {
            Lazy<EnumHelperWithValue<TestEnumWithValue>> lazyHelper = EnumHelperWithValue
                    .newLazyHelperWithValue(TestEnumWithValue.class, Collections.emptyList());

            assertThat(lazyHelper).isNotNull();

            EnumHelperWithValue<TestEnumWithValue> helper = lazyHelper.get();
            assertThat(helper.fromValue("alpha")).isEqualTo(TestEnumWithValue.OPTION_A);
            assertThat(helper.fromValue("gamma")).isEqualTo(TestEnumWithValue.OPTION_C);
        }

        @Test
        @DisplayName("Should return same instance on multiple gets")
        void testLazyHelperWithValueCaching() {
            Lazy<EnumHelperWithValue<TestEnumWithValue>> lazyHelper = EnumHelperWithValue
                    .newLazyHelperWithValue(TestEnumWithValue.class);

            EnumHelperWithValue<TestEnumWithValue> helper1 = lazyHelper.get();
            EnumHelperWithValue<TestEnumWithValue> helper2 = lazyHelper.get();

            assertThat(helper1).isSameAs(helper2);
        }
    }

    @Nested
    @DisplayName("Inheritance and Integration Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should inherit all EnumHelper functionality")
        void testInheritedEnumHelperFunctionality() {
            // Test that name-based lookup uses getString() values for StringProvider enums
            assertThat(enumHelper.fromName("alpha")).isEqualTo(TestEnumWithValue.OPTION_A);
            assertThat(enumHelper.fromNameTry("beta")).contains(TestEnumWithValue.DUPLICATE_VALUE); // "beta" resolves
                                                                                                    // to last enum with
                                                                                                    // that value

            // Test that ordinal-based lookup still works
            assertThat(enumHelper.fromOrdinal(0)).isEqualTo(TestEnumWithValue.OPTION_A);
            assertThat(enumHelper.fromOrdinalTry(1)).contains(TestEnumWithValue.OPTION_B);

            // Test that names() returns getString() values
            assertThat(enumHelper.names()).contains("alpha", "beta", "gamma");
        }

        @Test
        @DisplayName("Should maintain consistency between value and name lookup")
        void testConsistencyBetweenValueAndNameLookup() {
            TestEnumWithValue byName = enumHelper.fromName("alpha");
            TestEnumWithValue byValue = enumHelper.fromValue("alpha");

            assertThat(byName).isSameAs(byValue);
        }

        @Test
        @DisplayName("Should handle concurrent access properly")
        void testConcurrentAccess() throws InterruptedException {
            EnumHelperWithValue<TestEnumWithValue> helper = new EnumHelperWithValue<>(TestEnumWithValue.class);

            Thread[] threads = new Thread[10];
            boolean[] results = new boolean[threads.length];

            for (int i = 0; i < threads.length; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        // Each thread performs multiple operations
                        TestEnumWithValue byName = helper.fromName("alpha"); // Use getString() value
                        TestEnumWithValue byValue = helper.fromValue("alpha");
                        TestEnumWithValue byOrdinal = helper.fromOrdinal(0);
                        Map<String, TestEnumWithValue> valuesMap = helper.getValuesTranslationMap();

                        results[index] = byName == TestEnumWithValue.OPTION_A &&
                                byValue == TestEnumWithValue.OPTION_A &&
                                byOrdinal == TestEnumWithValue.OPTION_A &&
                                valuesMap.containsKey("alpha");
                    } catch (Exception e) {
                        results[index] = false;
                    }
                });
            }

            for (Thread thread : threads) {
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // All threads should succeed
            for (boolean result : results) {
                assertThat(result).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle enums with identical string values")
        void testIdenticalStringValues() {
            // OPTION_B and DUPLICATE_VALUE both have "beta" value
            Map<String, TestEnumWithValue> valuesMap = enumHelper.getValuesTranslationMap();

            // Map should contain "beta" key, but which enum it maps to depends on
            // implementation
            assertThat(valuesMap).containsKey("beta");
            TestEnumWithValue mappedValue = valuesMap.get("beta");
            assertThat(mappedValue).isIn(TestEnumWithValue.OPTION_B, TestEnumWithValue.DUPLICATE_VALUE);
        }

        @Test
        @DisplayName("Should handle empty string values")
        void testEmptyStringValues() {
            assertThat(enumHelper.fromValue("")).isEqualTo(TestEnumWithValue.EMPTY_VALUE);
            assertThat(enumHelper.fromValueTry("")).contains(TestEnumWithValue.EMPTY_VALUE);
        }

        @Test
        @DisplayName("Should handle special characters in values")
        void testSpecialCharactersInValues() {
            assertThat(enumHelper.fromValue("special-char")).isEqualTo(TestEnumWithValue.SPECIAL_CHAR);
        }

        @Test
        @DisplayName("Should handle whitespace in values")
        void testWhitespaceInValues() {
            assertThatThrownBy(() -> {
                enumHelper.fromValue(" alpha ");
            }).isInstanceOf(IllegalArgumentException.class);

            assertThat(enumHelper.fromValueTry(" alpha ")).isEmpty();
        }

        @Test
        @DisplayName("Should handle case sensitivity correctly")
        void testCaseSensitivity() {
            // Values should be case sensitive
            assertThatThrownBy(() -> {
                enumHelper.fromValue("Alpha");
            }).isInstanceOf(IllegalArgumentException.class);

            // Names should be case sensitive
            assertThatThrownBy(() -> {
                enumHelper.fromName("option_a");
            }).isInstanceOf(RuntimeException.class);
        }
    }
}
