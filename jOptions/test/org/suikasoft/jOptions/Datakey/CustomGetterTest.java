package org.suikasoft.jOptions.Datakey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Comprehensive test suite for the {@link CustomGetter} functional interface.
 * Tests custom value retrieval logic, lambda expressions, and functional
 * interface behavior.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CustomGetter Tests")
class CustomGetterTest {

    @Mock
    private DataStore mockDataStore;

    private String testInputValue;

    @BeforeEach
    void setUp() {
        testInputValue = "input_value";
    }

    @Nested
    @DisplayName("Functional Interface Tests")
    class FunctionalInterfaceTests {

        @Test
        @DisplayName("Should implement functional interface correctly")
        void testFunctionalInterfaceContract() {
            // given - create a lambda implementation
            CustomGetter<String> getter = (value, dataStore) -> value.toUpperCase();

            // when
            String result = getter.get(testInputValue, mockDataStore);

            // then
            assertThat(result).isEqualTo(testInputValue.toUpperCase());
        }

        @Test
        @DisplayName("Should support static method reference")
        void testStaticMethodReference() {
            // given - create a static method reference
            CustomGetter<String> getter = CustomGetterTest::processValue;

            // when
            String result = getter.get(testInputValue, mockDataStore);

            // then
            assertThat(result).isEqualTo("processed:" + testInputValue);
        }

        @Test
        @DisplayName("Should support instance method as lambda")
        void testInstanceMethodAsLambda() {
            // given - create a lambda that calls instance method
            CustomGetter<String> getter = (value, store) -> value.toUpperCase();

            // when
            String result = getter.get("test", mockDataStore);

            // then
            assertThat(result).isEqualTo("TEST");
        }
    }

    @Nested
    @DisplayName("Lambda Expression Tests")
    class LambdaExpressionTests {

        @Test
        @DisplayName("Should support simple lambda expression")
        void testSimpleLambda() {
            // given
            CustomGetter<String> getter = (value, store) -> value + "_suffix";

            // when
            String result = getter.get(testInputValue, mockDataStore);

            // then
            assertThat(result).isEqualTo(testInputValue + "_suffix");
        }

        @Test
        @DisplayName("Should support complex lambda expression")
        void testComplexLambda() {
            // given
            CustomGetter<String> getter = (value, store) -> {
                if (value == null) {
                    return "default";
                }
                return value.trim().toLowerCase().replace(' ', '_');
            };

            // when
            String result = getter.get("  Test Value  ", mockDataStore);

            // then
            assertThat(result).isEqualTo("test_value");
        }

        @Test
        @DisplayName("Should support lambda with datastore interaction")
        void testLambdaWithDataStore() {
            // given
            DataKey<String> testKey = KeyFactory.string("some_key");
            when(mockDataStore.hasValue(testKey)).thenReturn(true);
            when(mockDataStore.get(testKey)).thenReturn("store_value");

            CustomGetter<String> getter = (value, store) -> {
                if (store.hasValue(testKey)) {
                    return store.get(testKey);
                }
                return value;
            };

            // when
            String result = getter.get(testInputValue, mockDataStore);

            // then
            assertThat(result).isEqualTo("store_value");
            verify(mockDataStore).hasValue(testKey);
            verify(mockDataStore).get(testKey);
        }

        @Test
        @DisplayName("Should handle null input value")
        void testNullInputValue() {
            // given
            CustomGetter<String> getter = (value, store) -> value != null ? value : "null_replacement";

            // when
            String result = getter.get(null, mockDataStore);

            // then
            assertThat(result).isEqualTo("null_replacement");
        }

        @Test
        @DisplayName("Should handle null datastore")
        void testNullDataStore() {
            // given
            CustomGetter<String> getter = (value, store) -> store != null ? value : "no_store";

            // when
            String result = getter.get(testInputValue, null);

            // then
            assertThat(result).isEqualTo("no_store");
        }
    }

    @Nested
    @DisplayName("Type Safety Tests")
    class TypeSafetyTests {

        @Test
        @DisplayName("Should work with different types")
        void testDifferentTypes() {
            // given
            CustomGetter<Integer> intGetter = (value, store) -> value * 2;
            CustomGetter<Boolean> boolGetter = (value, store) -> !value;

            // when
            Integer intResult = intGetter.get(5, mockDataStore);
            Boolean boolResult = boolGetter.get(true, mockDataStore);

            // then
            assertThat(intResult).isEqualTo(10);
            assertThat(boolResult).isFalse();
        }

        @Test
        @DisplayName("Should work with complex types")
        void testComplexTypes() {
            // given
            CustomGetter<StringBuilder> builderGetter = (value, store) -> value.append("_modified");

            // when
            StringBuilder result = builderGetter.get(new StringBuilder("test"), mockDataStore);

            // then
            assertThat(result.toString()).isEqualTo("test_modified");
        }

        @Test
        @DisplayName("Should work with generic types")
        void testGenericTypes() {
            // given
            CustomGetter<java.util.List<String>> listGetter = (value, store) -> {
                value.add("added_item");
                return value;
            };

            java.util.List<String> inputList = new java.util.ArrayList<>();
            inputList.add("original");

            // when
            java.util.List<String> result = listGetter.get(inputList, mockDataStore);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).contains("original", "added_item");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should propagate runtime exceptions")
        void testRuntimeExceptionPropagation() {
            // given
            CustomGetter<String> throwingGetter = (value, store) -> {
                throw new RuntimeException("Test exception");
            };

            // when/then
            assertThatThrownBy(() -> throwingGetter.get(testInputValue, mockDataStore))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Test exception");
        }

        @Test
        @DisplayName("Should handle null pointer exceptions gracefully")
        void testNullPointerExceptionHandling() {
            // given
            CustomGetter<String> nullPointerGetter = (value, store) -> value.toUpperCase(); // NPE if value is null

            // when/then
            assertThatThrownBy(() -> nullPointerGetter.get(null, mockDataStore))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle exceptions in complex logic")
        void testComplexLogicExceptions() {
            // given
            CustomGetter<Integer> divisionGetter = (value, store) -> 100 / value;

            // when/then - division by zero
            assertThatThrownBy(() -> divisionGetter.get(0, mockDataStore))
                    .isInstanceOf(ArithmeticException.class);
        }
    }

    @Nested
    @DisplayName("Composition and Chaining Tests")
    class CompositionTests {

        @Test
        @DisplayName("Should support composition through method chaining")
        void testComposition() {
            // given
            CustomGetter<String> trimmer = (value, store) -> value.trim();
            CustomGetter<String> uppercaser = (value, store) -> value.toUpperCase();

            // Create composed getter
            CustomGetter<String> composedGetter = (value, store) -> {
                String trimmed = trimmer.get(value, store);
                return uppercaser.get(trimmed, store);
            };

            // when
            String result = composedGetter.get("  test  ", mockDataStore);

            // then
            assertThat(result).isEqualTo("TEST");
        }

        @Test
        @DisplayName("Should support conditional composition")
        void testConditionalComposition() {
            // given
            CustomGetter<String> conditionalGetter = (value, store) -> {
                if (value.length() > 5) {
                    return value.substring(0, 5);
                } else {
                    return value.toUpperCase();
                }
            };

            // when
            String shortResult = conditionalGetter.get("test", mockDataStore);
            String longResult = conditionalGetter.get("this_is_a_long_string", mockDataStore);

            // then
            assertThat(shortResult).isEqualTo("TEST");
            assertThat(longResult).isEqualTo("this_");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work in realistic data processing scenario")
        void testRealisticScenario() {
            // given - simulate a configuration value processor
            DataKey<String> prefixKey = KeyFactory.string("prefix_key");
            when(mockDataStore.hasValue(prefixKey)).thenReturn(true);
            when(mockDataStore.get(prefixKey)).thenReturn("global_prefix");

            CustomGetter<String> configProcessor = (value, store) -> {
                String prefix = "";
                if (store.hasValue(prefixKey)) {
                    prefix = store.get(prefixKey) + "_";
                }

                String processed = value.trim().toLowerCase().replace(' ', '_');
                return prefix + processed;
            };

            // when
            String result = configProcessor.get("  Test Configuration  ", mockDataStore);

            // then
            assertThat(result).isEqualTo("global_prefix_test_configuration");
            verify(mockDataStore).hasValue(prefixKey);
            verify(mockDataStore).get(prefixKey);
        }

        @Test
        @DisplayName("Should work with multiple datastore interactions")
        void testMultipleDataStoreInteractions() {
            // given
            DataKey<String> key1 = KeyFactory.string("key1");
            DataKey<String> key2 = KeyFactory.string("key2");

            when(mockDataStore.get(key1)).thenReturn("value1");
            when(mockDataStore.get(key2)).thenReturn("value2");

            CustomGetter<String> multiKeyGetter = (value, store) -> {
                String v1 = store.get(key1);
                String v2 = store.get(key2);
                return value + "_" + v1 + "_" + v2;
            };

            // when
            String result = multiKeyGetter.get("base", mockDataStore);

            // then
            assertThat(result).isEqualTo("base_value1_value2");
            verify(mockDataStore).get(key1);
            verify(mockDataStore).get(key2);
        }

        @Test
        @DisplayName("Should support fallback logic")
        void testFallbackLogic() {
            // given
            DataKey<String> preferredKey = KeyFactory.string("preferred_value");
            when(mockDataStore.hasValue(preferredKey)).thenReturn(false);

            CustomGetter<String> fallbackGetter = (value, store) -> {
                if (store.hasValue(preferredKey)) {
                    return store.get(preferredKey);
                }
                if (value != null && !value.isEmpty()) {
                    return value;
                }
                return "default_fallback";
            };

            // when
            String result = fallbackGetter.get("", mockDataStore);

            // then
            assertThat(result).isEqualTo("default_fallback");
            verify(mockDataStore).hasValue(preferredKey);
        }
    }

    // Helper method for testing static method references
    static String processValue(String value, DataStore dataStore) {
        return "processed:" + value;
    }
}
