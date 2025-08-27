package pt.up.fe.specs.util.enums;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Comprehensive test suite for EnumHelperProvider class.
 * 
 * Tests the lazy provider wrapper for EnumHelperWithValue that provides
 * simplified access to value-based enum helpers with StringProvider enums.
 * 
 * @author Generated Tests
 */
@DisplayName("EnumHelperProvider Tests")
class EnumHelperProviderTest {

    // Test enum implementing StringProvider for testing
    private enum TestEnumWithValue implements StringProvider {
        FIRST("first"),
        SECOND("second"),
        THIRD("third"),
        SPECIAL("special-value");

        private final String value;

        TestEnumWithValue(String value) {
            this.value = value;
        }

        @Override
        public String getString() {
            return value;
        }
    }

    private EnumHelperProvider<TestEnumWithValue> provider;

    @BeforeEach
    void setUp() {
        provider = new EnumHelperProvider<>(TestEnumWithValue.class);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create EnumHelperProvider with valid StringProvider enum")
        void testValidConstructor() {
            EnumHelperProvider<TestEnumWithValue> newProvider = new EnumHelperProvider<>(TestEnumWithValue.class);

            assertThat(newProvider).isNotNull();
            assertThat(newProvider.get()).isNotNull();
            assertThat(newProvider.get().getEnumClass()).isEqualTo(TestEnumWithValue.class);
        }

        @Test
        @DisplayName("Should handle null enum class gracefully - fails on first access")
        void testNullEnumClass() {
            // Constructor accepts null but fails on first lazy access
            EnumHelperProvider<TestEnumWithValue> provider = new EnumHelperProvider<TestEnumWithValue>(null);
            assertThatThrownBy(() -> provider.get().fromValue("first"))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle enum class properly")
        void testEnumClassHandling() {
            EnumHelperProvider<TestEnumWithValue> newProvider = new EnumHelperProvider<>(TestEnumWithValue.class);

            EnumHelperWithValue<TestEnumWithValue> helper = newProvider.get();
            assertThat(helper.getEnumClass()).isEqualTo(TestEnumWithValue.class);
            assertThat(helper.getSize()).isEqualTo(4);
        }
    }

    @Nested
    @DisplayName("Lazy Initialization Tests")
    class LazyInitializationTests {

        @Test
        @DisplayName("Should return EnumHelperWithValue on get")
        void testGet() {
            EnumHelperWithValue<TestEnumWithValue> helper = provider.get();

            assertThat(helper).isNotNull();
            assertThat(helper).isInstanceOf(EnumHelperWithValue.class);
            assertThat(helper.getEnumClass()).isEqualTo(TestEnumWithValue.class);
        }

        @Test
        @DisplayName("Should return same instance on multiple gets")
        void testLazyCaching() {
            EnumHelperWithValue<TestEnumWithValue> helper1 = provider.get();
            EnumHelperWithValue<TestEnumWithValue> helper2 = provider.get();

            assertThat(helper1).isSameAs(helper2);
        }

        @Test
        @DisplayName("Should initialize helper with correct enum values")
        void testHelperInitialization() {
            EnumHelperWithValue<TestEnumWithValue> helper = provider.get();

            // Test that the helper is properly initialized
            assertThat(helper.fromValue("first")).isEqualTo(TestEnumWithValue.FIRST);
            assertThat(helper.fromValue("second")).isEqualTo(TestEnumWithValue.SECOND);
            assertThat(helper.fromValue("third")).isEqualTo(TestEnumWithValue.THIRD);
            assertThat(helper.fromValue("special-value")).isEqualTo(TestEnumWithValue.SPECIAL);
        }

        @Test
        @DisplayName("Should initialize helper with no excludes by default")
        void testDefaultNoExcludes() {
            EnumHelperWithValue<TestEnumWithValue> helper = provider.get();

            // All enum values should be accessible
            assertThat(helper.getValuesTranslationMap()).hasSize(4);
            assertThat(helper.getValuesTranslationMap()).containsKey("first");
            assertThat(helper.getValuesTranslationMap()).containsKey("second");
            assertThat(helper.getValuesTranslationMap()).containsKey("third");
            assertThat(helper.getValuesTranslationMap()).containsKey("special-value");
        }
    }

    @Nested
    @DisplayName("Functional Integration Tests")
    class FunctionalIntegrationTests {

        @Test
        @DisplayName("Should support all EnumHelperWithValue operations")
        void testAllOperationsSupported() {
            EnumHelperWithValue<TestEnumWithValue> helper = provider.get();

            // Test value-based lookup
            assertThat(helper.fromValue("first")).isEqualTo(TestEnumWithValue.FIRST);
            assertThat(helper.fromValueTry("second")).contains(TestEnumWithValue.SECOND);

            // Test name-based lookup using getString() values for StringProvider enums
            assertThat(helper.fromName("first")).isEqualTo(TestEnumWithValue.FIRST);
            assertThat(helper.fromNameTry("second")).contains(TestEnumWithValue.SECOND);

            // Test ordinal-based lookup (inherited)
            assertThat(helper.fromOrdinal(0)).isEqualTo(TestEnumWithValue.FIRST);
            assertThat(helper.fromOrdinalTry(1)).contains(TestEnumWithValue.SECOND);

            // Test collections - names() returns getString() values for StringProvider
            // enums
            assertThat(helper.names()).contains("first", "second", "third", "special-value");
            assertThat(helper.getValuesTranslationMap()).containsKey("first");
        }

        @Test
        @DisplayName("Should support alias addition")
        void testAliasSupport() {
            EnumHelperWithValue<TestEnumWithValue> helper = provider.get();

            helper.addAlias("alias1", TestEnumWithValue.FIRST);

            assertThat(helper.fromValue("alias1")).isEqualTo(TestEnumWithValue.FIRST);
            assertThat(helper.fromValueTry("alias1")).contains(TestEnumWithValue.FIRST);
        }

        @Test
        @DisplayName("Should support list processing")
        void testListProcessing() {
            EnumHelperWithValue<TestEnumWithValue> helper = provider.get();

            java.util.List<String> values = java.util.Arrays.asList("first", "third", "second");
            java.util.List<TestEnumWithValue> result = helper.fromValue(values);

            assertThat(result).containsExactly(
                    TestEnumWithValue.FIRST,
                    TestEnumWithValue.THIRD,
                    TestEnumWithValue.SECOND);
        }

        @Test
        @DisplayName("Should provide available values")
        void testAvailableValues() {
            EnumHelperWithValue<TestEnumWithValue> helper = provider.get();

            String availableValues = helper.getAvailableValues();
            assertThat(availableValues).contains("first");
            assertThat(availableValues).contains("second");
            assertThat(availableValues).contains("third");
            assertThat(availableValues).contains("special-value");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle invalid values gracefully")
        void testInvalidValueHandling() {
            EnumHelperWithValue<TestEnumWithValue> helper = provider.get();

            assertThatThrownBy(() -> {
                helper.fromValue("invalid");
            }).isInstanceOf(IllegalArgumentException.class);

            assertThat(helper.fromValueTry("invalid")).isEmpty();
        }

        @Test
        @DisplayName("Should handle invalid names gracefully")
        void testInvalidNameHandling() {
            EnumHelperWithValue<TestEnumWithValue> helper = provider.get();

            assertThatThrownBy(() -> {
                helper.fromName("INVALID");
            }).isInstanceOf(RuntimeException.class);

            assertThat(helper.fromNameTry("INVALID")).isEmpty();
        }

        @Test
        @DisplayName("Should handle invalid ordinals gracefully")
        void testInvalidOrdinalHandling() {
            EnumHelperWithValue<TestEnumWithValue> helper = provider.get();

            assertThatThrownBy(() -> {
                helper.fromOrdinal(-1);
            }).isInstanceOf(RuntimeException.class);

            assertThatThrownBy(() -> {
                helper.fromOrdinal(100);
            }).isInstanceOf(RuntimeException.class);

            assertThat(helper.fromOrdinalTry(-1)).isEmpty();
            assertThat(helper.fromOrdinalTry(100)).isEmpty();
        }

        @Test
        @DisplayName("Should handle null inputs gracefully")
        void testNullInputHandling() {
            EnumHelperWithValue<TestEnumWithValue> helper = provider.get();

            assertThatThrownBy(() -> {
                helper.fromValue((String) null);
            }).isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> {
                helper.fromName(null);
            }).isInstanceOf(RuntimeException.class);

            assertThat(helper.fromValueTry(null)).isEmpty();
            assertThat(helper.fromNameTry(null)).isEmpty();
        }
    }

    @Nested
    @DisplayName("Thread Safety Tests")
    class ThreadSafetyTests {

        @Test
        @DisplayName("Should handle concurrent access to provider")
        void testConcurrentProviderAccess() throws InterruptedException {
            EnumHelperProvider<TestEnumWithValue> sharedProvider = new EnumHelperProvider<>(TestEnumWithValue.class);

            Thread[] threads = new Thread[10];
            @SuppressWarnings("unchecked")
            EnumHelperWithValue<TestEnumWithValue>[] helpers = new EnumHelperWithValue[threads.length];

            for (int i = 0; i < threads.length; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    helpers[index] = sharedProvider.get();
                });
            }

            for (Thread thread : threads) {
                thread.start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // All threads should get the same helper instance
            EnumHelperWithValue<TestEnumWithValue> firstHelper = helpers[0];
            for (int i = 1; i < helpers.length; i++) {
                assertThat(helpers[i]).isSameAs(firstHelper);
            }
        }

        @Test
        @DisplayName("Should handle concurrent operations on provided helper")
        void testConcurrentHelperOperations() throws InterruptedException {
            EnumHelperWithValue<TestEnumWithValue> helper = provider.get();

            Thread[] threads = new Thread[10];
            boolean[] results = new boolean[threads.length];

            for (int i = 0; i < threads.length; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        // Each thread performs multiple operations
                        TestEnumWithValue byValue = helper.fromValue("first");
                        TestEnumWithValue byName = helper.fromName("first"); // Use getString() value
                        TestEnumWithValue byOrdinal = helper.fromOrdinal(0);

                        results[index] = byValue == TestEnumWithValue.FIRST &&
                                byName == TestEnumWithValue.FIRST &&
                                byOrdinal == TestEnumWithValue.FIRST;
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
    @DisplayName("Performance and Memory Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should create provider quickly")
        void testProviderCreationPerformance() {
            long startTime = System.nanoTime();

            for (int i = 0; i < 1000; i++) {
                new EnumHelperProvider<>(TestEnumWithValue.class);
            }

            long endTime = System.nanoTime();
            long durationMs = (endTime - startTime) / 1_000_000;

            // Provider creation should be very fast (under 100ms for 1000 instances)
            assertThat(durationMs).isLessThan(100);
        }

        @Test
        @DisplayName("Should initialize helper only once")
        void testLazyInitializationPerformance() {
            EnumHelperProvider<TestEnumWithValue> testProvider = new EnumHelperProvider<>(TestEnumWithValue.class);

            // First call initializes
            long startTime = System.nanoTime();
            EnumHelperWithValue<TestEnumWithValue> helper1 = testProvider.get();
            long firstCallTime = System.nanoTime() - startTime;

            // Subsequent calls should be much faster
            startTime = System.nanoTime();
            EnumHelperWithValue<TestEnumWithValue> helper2 = testProvider.get();
            long secondCallTime = System.nanoTime() - startTime;

            assertThat(helper1).isSameAs(helper2);
            assertThat(secondCallTime).isLessThan(firstCallTime / 10); // Should be at least 10x faster
        }

        @Test
        @DisplayName("Should not create multiple providers unnecessarily")
        void testMemoryEfficiency() {
            // Multiple providers for same enum class should be independent
            EnumHelperProvider<TestEnumWithValue> provider1 = new EnumHelperProvider<>(TestEnumWithValue.class);
            EnumHelperProvider<TestEnumWithValue> provider2 = new EnumHelperProvider<>(TestEnumWithValue.class);

            assertThat(provider1).isNotSameAs(provider2);

            // But their helpers should be functionally equivalent
            EnumHelperWithValue<TestEnumWithValue> helper1 = provider1.get();
            EnumHelperWithValue<TestEnumWithValue> helper2 = provider2.get();

            assertThat(helper1.getEnumClass()).isEqualTo(helper2.getEnumClass());
            assertThat(helper1.fromValue("first")).isEqualTo(helper2.fromValue("first"));
        }
    }

    @Nested
    @DisplayName("Edge Cases and Boundary Conditions")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle enum with single value")
        void testSingleValueEnum() {
            // Create a test enum with single value for this test
            enum SingleValueEnum implements StringProvider {
                ONLY_ONE("single");

                private final String value;

                SingleValueEnum(String value) {
                    this.value = value;
                }

                @Override
                public String getString() {
                    return value;
                }
            }

            EnumHelperProvider<SingleValueEnum> singleProvider = new EnumHelperProvider<>(SingleValueEnum.class);
            EnumHelperWithValue<SingleValueEnum> helper = singleProvider.get();

            assertThat(helper.getSize()).isEqualTo(1);
            assertThat(helper.fromValue("single")).isEqualTo(SingleValueEnum.ONLY_ONE);
            assertThat(helper.fromName("single")).isEqualTo(SingleValueEnum.ONLY_ONE); // Use getString() value
            assertThat(helper.fromOrdinal(0)).isEqualTo(SingleValueEnum.ONLY_ONE);
        }

        @Test
        @DisplayName("Should handle enum with empty string values")
        void testEmptyStringValues() {
            enum EmptyStringEnum implements StringProvider {
                EMPTY(""), NON_EMPTY("value");

                private final String value;

                EmptyStringEnum(String value) {
                    this.value = value;
                }

                @Override
                public String getString() {
                    return value;
                }
            }

            EnumHelperProvider<EmptyStringEnum> emptyProvider = new EnumHelperProvider<>(EmptyStringEnum.class);
            EnumHelperWithValue<EmptyStringEnum> helper = emptyProvider.get();

            assertThat(helper.fromValue("")).isEqualTo(EmptyStringEnum.EMPTY);
            assertThat(helper.fromValue("value")).isEqualTo(EmptyStringEnum.NON_EMPTY);
        }

        @Test
        @DisplayName("Should handle enum with special characters in values")
        void testSpecialCharactersInValues() {
            enum SpecialCharEnum implements StringProvider {
                HYPHEN("test-value"),
                UNDERSCORE("test_value"),
                DOT("test.value"),
                SPACE("test value"),
                UNICODE("test\u00E9value");

                private final String value;

                SpecialCharEnum(String value) {
                    this.value = value;
                }

                @Override
                public String getString() {
                    return value;
                }
            }

            EnumHelperProvider<SpecialCharEnum> specialProvider = new EnumHelperProvider<>(SpecialCharEnum.class);
            EnumHelperWithValue<SpecialCharEnum> helper = specialProvider.get();

            assertThat(helper.fromValue("test-value")).isEqualTo(SpecialCharEnum.HYPHEN);
            assertThat(helper.fromValue("test_value")).isEqualTo(SpecialCharEnum.UNDERSCORE);
            assertThat(helper.fromValue("test.value")).isEqualTo(SpecialCharEnum.DOT);
            assertThat(helper.fromValue("test value")).isEqualTo(SpecialCharEnum.SPACE);
            assertThat(helper.fromValue("test\u00E9value")).isEqualTo(SpecialCharEnum.UNICODE);
        }
    }
}
