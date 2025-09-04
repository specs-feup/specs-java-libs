package pt.up.fe.specs.util.enums;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.lazy.Lazy;

/**
 * Comprehensive test suite for EnumHelper class.
 * 
 * Tests the generic enum manipulation utility that provides name-based enum
 * lookup, ordinal conversion, lazy initialization, and exclude list
 * functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("EnumHelper Tests")
class EnumHelperTest {

    // Test enum for testing
    private enum TestEnum {
        FIRST, SECOND, THIRD, FOURTH
    }

    // Test enum with string provider for integration testing
    private enum TestEnumWithProvider implements pt.up.fe.specs.util.providers.StringProvider {
        ALPHA("alpha"), BETA("beta"), GAMMA("gamma");

        private final String value;

        TestEnumWithProvider(String value) {
            this.value = value;
        }

        @Override
        public String getString() {
            return value;
        }
    }

    private EnumHelper<TestEnum> enumHelper;
    private EnumHelper<TestEnum> enumHelperWithExcludes;

    @BeforeEach
    void setUp() {
        enumHelper = new EnumHelper<>(TestEnum.class);
        enumHelperWithExcludes = new EnumHelper<>(TestEnum.class, Arrays.asList(TestEnum.THIRD));
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create EnumHelper with enum class")
        void testBasicConstructor() {
            EnumHelper<TestEnum> helper = new EnumHelper<>(TestEnum.class);

            assertThat(helper.getEnumClass()).isEqualTo(TestEnum.class);
            assertThat(helper.getSize()).isEqualTo(4);
            assertThat(helper.values()).containsExactly(TestEnum.FIRST, TestEnum.SECOND, TestEnum.THIRD,
                    TestEnum.FOURTH);
        }

        @Test
        @DisplayName("Should create EnumHelper with exclude list")
        void testConstructorWithExcludeList() {
            Collection<TestEnum> excludeList = Arrays.asList(TestEnum.SECOND, TestEnum.FOURTH);
            EnumHelper<TestEnum> helper = new EnumHelper<>(TestEnum.class, excludeList);

            assertThat(helper.getEnumClass()).isEqualTo(TestEnum.class);
            assertThat(helper.getSize()).isEqualTo(4); // Size is still total enum count

            // Excluded items should not be in names map
            assertThat(helper.names()).doesNotContain("SECOND", "FOURTH");
            assertThat(helper.names()).contains("FIRST", "THIRD");
        }

        @Test
        @DisplayName("Should create EnumHelper with empty exclude list")
        void testConstructorWithEmptyExcludeList() {
            EnumHelper<TestEnum> helper = new EnumHelper<>(TestEnum.class, Collections.emptyList());

            assertThat(helper.getEnumClass()).isEqualTo(TestEnum.class);
            assertThat(helper.names()).contains("FIRST", "SECOND", "THIRD", "FOURTH");
        }

        @Test
        @DisplayName("Should throw exception for null enum class")
        void testNullEnumClass() {
            // Constructor should fail fast with null enum class
            assertThatThrownBy(() -> new EnumHelper<TestEnum>(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("Enum class cannot be null");
        }
    }

    @Nested
    @DisplayName("Name Lookup Tests")
    class NameLookupTests {

        @Test
        @DisplayName("Should find enum by exact name")
        void testFromNameExactMatch() {
            TestEnum result = enumHelper.fromName("FIRST");

            assertThat(result).isEqualTo(TestEnum.FIRST);
        }

        @Test
        @DisplayName("Should find all enum values by name")
        void testFromNameAllValues() {
            assertThat(enumHelper.fromName("FIRST")).isEqualTo(TestEnum.FIRST);
            assertThat(enumHelper.fromName("SECOND")).isEqualTo(TestEnum.SECOND);
            assertThat(enumHelper.fromName("THIRD")).isEqualTo(TestEnum.THIRD);
            assertThat(enumHelper.fromName("FOURTH")).isEqualTo(TestEnum.FOURTH);
        }

        @Test
        @DisplayName("Should throw exception for unknown name")
        void testFromNameUnknown() {
            assertThatThrownBy(() -> {
                enumHelper.fromName("UNKNOWN");
            }).isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum with name 'UNKNOWN'")
                    .hasMessageContaining("available names:");
        }

        @Test
        @DisplayName("Should throw exception for null name")
        void testFromNameNull() {
            assertThatThrownBy(() -> {
                enumHelper.fromName(null);
            }).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should throw exception for empty name")
        void testFromNameEmpty() {
            assertThatThrownBy(() -> {
                enumHelper.fromName("");
            }).isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum with name ''");
        }

        @Test
        @DisplayName("Should be case sensitive")
        void testFromNameCaseSensitive() {
            assertThatThrownBy(() -> {
                enumHelper.fromName("first");
            }).isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Optional Name Lookup Tests")
    class OptionalNameLookupTests {

        @Test
        @DisplayName("Should return present Optional for valid name")
        void testFromNameTryValidName() {
            Optional<TestEnum> result = enumHelper.fromNameTry("SECOND");

            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(TestEnum.SECOND);
        }

        @Test
        @DisplayName("Should return empty Optional for invalid name")
        void testFromNameTryInvalidName() {
            Optional<TestEnum> result = enumHelper.fromNameTry("INVALID");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty Optional for null name")
        void testFromNameTryNullName() {
            Optional<TestEnum> result = enumHelper.fromNameTry(null);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty Optional for empty name")
        void testFromNameTryEmptyName() {
            Optional<TestEnum> result = enumHelper.fromNameTry("");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle case sensitivity in try method")
        void testFromNameTryCaseSensitive() {
            Optional<TestEnum> result = enumHelper.fromNameTry("first");

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Ordinal Lookup Tests")
    class OrdinalLookupTests {

        @Test
        @DisplayName("Should find enum by valid ordinal")
        void testFromOrdinalValid() {
            assertThat(enumHelper.fromOrdinal(0)).isEqualTo(TestEnum.FIRST);
            assertThat(enumHelper.fromOrdinal(1)).isEqualTo(TestEnum.SECOND);
            assertThat(enumHelper.fromOrdinal(2)).isEqualTo(TestEnum.THIRD);
            assertThat(enumHelper.fromOrdinal(3)).isEqualTo(TestEnum.FOURTH);
        }

        @Test
        @DisplayName("Should throw exception for negative ordinal")
        void testFromOrdinalNegative() {
            assertThatThrownBy(() -> {
                enumHelper.fromOrdinal(-1);
            }).isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Given ordinal '-1' is out of range")
                    .hasMessageContaining("enum has 4 values");
        }

        @Test
        @DisplayName("Should throw exception for ordinal too large")
        void testFromOrdinalTooLarge() {
            assertThatThrownBy(() -> {
                enumHelper.fromOrdinal(4);
            }).isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Given ordinal '4' is out of range")
                    .hasMessageContaining("enum has 4 values");
        }

        @Test
        @DisplayName("Should throw exception for very large ordinal")
        void testFromOrdinalVeryLarge() {
            assertThatThrownBy(() -> {
                enumHelper.fromOrdinal(100);
            }).isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Given ordinal '100' is out of range");
        }
    }

    @Nested
    @DisplayName("Optional Ordinal Lookup Tests")
    class OptionalOrdinalLookupTests {

        @Test
        @DisplayName("Should return present Optional for valid ordinal")
        void testFromOrdinalTryValid() {
            assertThat(enumHelper.fromOrdinalTry(0)).contains(TestEnum.FIRST);
            assertThat(enumHelper.fromOrdinalTry(1)).contains(TestEnum.SECOND);
            assertThat(enumHelper.fromOrdinalTry(2)).contains(TestEnum.THIRD);
            assertThat(enumHelper.fromOrdinalTry(3)).contains(TestEnum.FOURTH);
        }

        @Test
        @DisplayName("Should return empty Optional for negative ordinal")
        void testFromOrdinalTryNegative() {
            Optional<TestEnum> result = enumHelper.fromOrdinalTry(-1);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty Optional for ordinal too large")
        void testFromOrdinalTryTooLarge() {
            Optional<TestEnum> result = enumHelper.fromOrdinalTry(4);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty Optional for very large ordinal")
        void testFromOrdinalTryVeryLarge() {
            Optional<TestEnum> result = enumHelper.fromOrdinalTry(100);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return empty Optional for very negative ordinal")
        void testFromOrdinalTryVeryNegative() {
            Optional<TestEnum> result = enumHelper.fromOrdinalTry(-100);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Exclude List Tests")
    class ExcludeListTests {

        @Test
        @DisplayName("Should exclude specified enums from name lookup")
        void testExcludedEnumsNotFoundByName() {
            // THIRD is excluded
            assertThatThrownBy(() -> {
                enumHelperWithExcludes.fromName("THIRD");
            }).isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find enum with name 'THIRD'");
        }

        @Test
        @DisplayName("Should exclude specified enums from optional name lookup")
        void testExcludedEnumsNotFoundByNameTry() {
            // THIRD is excluded
            Optional<TestEnum> result = enumHelperWithExcludes.fromNameTry("THIRD");

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should still find non-excluded enums")
        void testNonExcludedEnumsStillFound() {
            assertThat(enumHelperWithExcludes.fromName("FIRST")).isEqualTo(TestEnum.FIRST);
            assertThat(enumHelperWithExcludes.fromName("SECOND")).isEqualTo(TestEnum.SECOND);
            assertThat(enumHelperWithExcludes.fromName("FOURTH")).isEqualTo(TestEnum.FOURTH);
        }

        @Test
        @DisplayName("Should still access excluded enums by ordinal")
        void testExcludedEnumsStillAccessibleByOrdinal() {
            // THIRD is excluded from name lookup but should still be accessible by ordinal
            assertThat(enumHelperWithExcludes.fromOrdinal(2)).isEqualTo(TestEnum.THIRD);
        }

        @Test
        @DisplayName("Should not include excluded enums in names collection")
        void testExcludedEnumsNotInNames() {
            Collection<String> names = enumHelperWithExcludes.names();

            assertThat(names).contains("FIRST", "SECOND", "FOURTH");
            assertThat(names).doesNotContain("THIRD");
        }
    }

    @Nested
    @DisplayName("Values and Names Tests")
    class ValuesAndNamesTests {

        @Test
        @DisplayName("Should return all enum values")
        void testValues() {
            TestEnum[] values = enumHelper.values();

            assertThat(values).containsExactly(TestEnum.FIRST, TestEnum.SECOND, TestEnum.THIRD, TestEnum.FOURTH);
        }

        @Test
        @DisplayName("Should return correct size")
        void testGetSize() {
            assertThat(enumHelper.getSize()).isEqualTo(4);
        }

        @Test
        @DisplayName("Should return all names")
        void testNames() {
            Collection<String> names = enumHelper.names();

            assertThat(names).contains("FIRST", "SECOND", "THIRD", "FOURTH");
            assertThat(names).hasSize(4);
        }

        @Test
        @DisplayName("Should return names excluding excluded items")
        void testNamesWithExcludes() {
            Collection<String> names = enumHelperWithExcludes.names();

            assertThat(names).contains("FIRST", "SECOND", "FOURTH");
            assertThat(names).doesNotContain("THIRD");
            assertThat(names).hasSize(3);
        }

        @Test
        @DisplayName("Should return correct enum class")
        void testGetEnumClass() {
            assertThat(enumHelper.getEnumClass()).isEqualTo(TestEnum.class);
        }
    }

    @Nested
    @DisplayName("Static Factory Methods Tests")
    class StaticFactoryMethodsTests {

        @Test
        @DisplayName("Should create lazy helper without excludes")
        void testNewLazyHelper() {
            Lazy<EnumHelper<TestEnum>> lazyHelper = EnumHelper.newLazyHelper(TestEnum.class);

            assertThat(lazyHelper).isNotNull();

            EnumHelper<TestEnum> helper = lazyHelper.get();
            assertThat(helper.getEnumClass()).isEqualTo(TestEnum.class);
            assertThat(helper.names()).contains("FIRST", "SECOND", "THIRD", "FOURTH");
        }

        @Test
        @DisplayName("Should create lazy helper with single exclude")
        void testNewLazyHelperWithSingleExclude() {
            Lazy<EnumHelper<TestEnum>> lazyHelper = EnumHelper.newLazyHelper(TestEnum.class, TestEnum.SECOND);

            assertThat(lazyHelper).isNotNull();

            EnumHelper<TestEnum> helper = lazyHelper.get();
            assertThat(helper.names()).contains("FIRST", "THIRD", "FOURTH");
            assertThat(helper.names()).doesNotContain("SECOND");
        }

        @Test
        @DisplayName("Should create lazy helper with multiple excludes")
        void testNewLazyHelperWithMultipleExcludes() {
            Collection<TestEnum> excludes = Arrays.asList(TestEnum.FIRST, TestEnum.THIRD);
            Lazy<EnumHelper<TestEnum>> lazyHelper = EnumHelper.newLazyHelper(TestEnum.class, excludes);

            assertThat(lazyHelper).isNotNull();

            EnumHelper<TestEnum> helper = lazyHelper.get();
            assertThat(helper.names()).contains("SECOND", "FOURTH");
            assertThat(helper.names()).doesNotContain("FIRST", "THIRD");
        }

        @Test
        @DisplayName("Should create lazy helper with empty exclude list")
        void testNewLazyHelperWithEmptyExcludes() {
            Lazy<EnumHelper<TestEnum>> lazyHelper = EnumHelper.newLazyHelper(TestEnum.class, Collections.emptyList());

            assertThat(lazyHelper).isNotNull();

            EnumHelper<TestEnum> helper = lazyHelper.get();
            assertThat(helper.names()).contains("FIRST", "SECOND", "THIRD", "FOURTH");
        }

        @Test
        @DisplayName("Should return same instance on multiple gets")
        void testLazyHelperCaching() {
            Lazy<EnumHelper<TestEnum>> lazyHelper = EnumHelper.newLazyHelper(TestEnum.class);

            EnumHelper<TestEnum> helper1 = lazyHelper.get();
            EnumHelper<TestEnum> helper2 = lazyHelper.get();

            assertThat(helper1).isSameAs(helper2);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle empty enum")
        void testEmptyEnum() {
            // Test with enum that has no values (this is theoretical, as Java doesn't allow
            // truly empty enums)
            // But we can test the behavior with all excluded
            Collection<TestEnum> allValues = Arrays.asList(TestEnum.values());
            EnumHelper<TestEnum> helper = new EnumHelper<>(TestEnum.class, allValues);

            assertThat(helper.names()).isEmpty();
            assertThatThrownBy(() -> helper.fromName("FIRST")).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should handle duplicate excludes gracefully")
        void testDuplicateExcludes() {
            Collection<TestEnum> duplicateExcludes = Arrays.asList(TestEnum.FIRST, TestEnum.FIRST, TestEnum.SECOND);
            EnumHelper<TestEnum> helper = new EnumHelper<>(TestEnum.class, duplicateExcludes);

            assertThat(helper.names()).contains("THIRD", "FOURTH");
            assertThat(helper.names()).doesNotContain("FIRST", "SECOND");
        }

        @Test
        @DisplayName("Should handle whitespace in names")
        void testWhitespaceNames() {
            assertThatThrownBy(() -> {
                enumHelper.fromName(" FIRST ");
            }).isInstanceOf(RuntimeException.class);

            assertThat(enumHelper.fromNameTry(" FIRST ")).isEmpty();
        }

        @Test
        @DisplayName("Should handle maximum integer ordinal gracefully")
        void testMaxIntegerOrdinal() {
            Optional<TestEnum> result = enumHelper.fromOrdinalTry(Integer.MAX_VALUE);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle minimum integer ordinal gracefully")
        void testMinIntegerOrdinal() {
            Optional<TestEnum> result = enumHelper.fromOrdinalTry(Integer.MIN_VALUE);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with StringProvider enums using getString() values")
        void testStringProviderEnumIntegration() {
            EnumHelper<TestEnumWithProvider> helper = new EnumHelper<>(TestEnumWithProvider.class);

            // Should work with getString() values for StringProvider enums
            assertThat(helper.fromName("alpha")).isEqualTo(TestEnumWithProvider.ALPHA);
            assertThat(helper.fromName("beta")).isEqualTo(TestEnumWithProvider.BETA);

            // Should not work with enum names
            assertThatThrownBy(() -> helper.fromName("ALPHA")).isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Should maintain consistency across multiple operations")
        void testConsistencyAcrossOperations() {
            EnumHelper<TestEnum> helper = new EnumHelper<>(TestEnum.class);

            // Same enum should be returned from different lookup methods
            TestEnum first = helper.fromName("FIRST");
            TestEnum firstFromOrdinal = helper.fromOrdinal(0);

            assertThat(first).isSameAs(firstFromOrdinal);
            assertThat(first.ordinal()).isEqualTo(0);
            assertThat(first.name()).isEqualTo("FIRST");
        }

        @Test
        @DisplayName("Should handle concurrent access properly")
        void testConcurrentAccess() throws InterruptedException {
            EnumHelper<TestEnum> helper = new EnumHelper<>(TestEnum.class);

            Thread[] threads = new Thread[10];
            boolean[] results = new boolean[threads.length];

            for (int i = 0; i < threads.length; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        // Each thread performs multiple operations
                        TestEnum first = helper.fromName("FIRST");
                        TestEnum second = helper.fromOrdinal(1);
                        Collection<String> names = helper.names();

                        synchronized (results) {
                            results[index] = first == TestEnum.FIRST &&
                                    second == TestEnum.SECOND &&
                                    names.contains("FIRST");
                        }
                    } catch (Exception e) {
                        synchronized (results) {
                            results[index] = false;
                        }
                    }
                });
            }

            for (Thread thread : threads) {
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join(5000); // Add timeout to prevent hanging
            }

            // All threads should succeed
            for (boolean result : results) {
                assertThat(result).isTrue();
            }
        }
    }
}
