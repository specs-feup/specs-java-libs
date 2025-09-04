package pt.up.fe.specs.util.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the KeyStringProvider interface.
 * 
 * @author Generated Tests
 */
@DisplayName("KeyStringProvider")
class KeyStringProviderTest {

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("should extend KeyProvider with String type")
        void shouldExtendKeyProviderWithStringType() {
            // Given/When - creating a lambda implementation
            KeyStringProvider provider = () -> "test";

            // Then - should be assignable to KeyProvider<String>
            KeyProvider<String> keyProvider = provider;
            assertThat(keyProvider.getKey()).isEqualTo("test");
        }

        @Test
        @DisplayName("should implement functional interface correctly")
        void shouldImplementFunctionalInterfaceCorrectly() {
            // Given/When - creating a lambda implementation
            KeyStringProvider provider = () -> "lambda-key";

            // Then
            assertThat(provider.getKey()).isEqualTo("lambda-key");
        }

        @Test
        @DisplayName("should support method reference implementation")
        void shouldSupportMethodReferenceImplementation() {
            // Given
            String value = "method-ref-key";

            // When - using method reference
            KeyStringProvider provider = value::toString;

            // Then
            assertThat(provider.getKey()).isEqualTo("method-ref-key");
        }
    }

    @Nested
    @DisplayName("Static Factory Methods")
    class StaticFactoryMethods {

        @Test
        @DisplayName("toList(varargs) should convert array of providers to list of strings")
        void toListVarargsSholdConvertArrayOfProvidersToListOfStrings() {
            // Given
            KeyStringProvider provider1 = () -> "first";
            KeyStringProvider provider2 = () -> "second";
            KeyStringProvider provider3 = () -> "third";

            // When
            List<String> result = KeyStringProvider.toList(provider1, provider2, provider3);

            // Then
            assertThat(result)
                    .hasSize(3)
                    .containsExactly("first", "second", "third");
        }

        @Test
        @DisplayName("toList(varargs) should handle empty array")
        void toListVarargsSholdHandleEmptyArray() {
            // Given/When
            List<String> result = KeyStringProvider.toList();

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("toList(varargs) should handle single provider")
        void toListVarargsSholdHandleSingleProvider() {
            // Given
            KeyStringProvider provider = () -> "single";

            // When
            List<String> result = KeyStringProvider.toList(provider);

            // Then
            assertThat(result)
                    .hasSize(1)
                    .containsExactly("single");
        }

        @Test
        @DisplayName("toList(list) should convert list of providers to list of strings")
        void toListListSholdConvertListOfProvidersToListOfStrings() {
            // Given
            List<KeyStringProvider> providers = Arrays.asList(
                    () -> "alpha",
                    () -> "beta",
                    () -> "gamma");

            // When
            List<String> result = KeyStringProvider.toList(providers);

            // Then
            assertThat(result)
                    .hasSize(3)
                    .containsExactly("alpha", "beta", "gamma");
        }

        @Test
        @DisplayName("toList(list) should handle empty list")
        void toListListSholdHandleEmptyList() {
            // Given
            List<KeyStringProvider> providers = Arrays.asList();

            // When
            List<String> result = KeyStringProvider.toList(providers);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("toList(list) should preserve order")
        void toListListSholdPreserveOrder() {
            // Given
            List<KeyStringProvider> providers = Arrays.asList(
                    () -> "z-last",
                    () -> "a-first",
                    () -> "m-middle");

            // When
            List<String> result = KeyStringProvider.toList(providers);

            // Then
            assertThat(result)
                    .hasSize(3)
                    .containsExactly("z-last", "a-first", "m-middle");
        }

        @Test
        @DisplayName("toList should handle null strings from providers")
        void toListShouldHandleNullStringsFromProviders() {
            // Given
            KeyStringProvider nullProvider = () -> null;
            KeyStringProvider validProvider = () -> "valid";

            // When
            List<String> result = KeyStringProvider.toList(nullProvider, validProvider);

            // Then
            assertThat(result)
                    .hasSize(2)
                    .containsExactly(null, "valid");
        }

        @Test
        @DisplayName("toList should handle duplicate strings")
        void toListShouldHandleDuplicateStrings() {
            // Given
            KeyStringProvider provider1 = () -> "duplicate";
            KeyStringProvider provider2 = () -> "unique";
            KeyStringProvider provider3 = () -> "duplicate";

            // When
            List<String> result = KeyStringProvider.toList(provider1, provider2, provider3);

            // Then
            assertThat(result)
                    .hasSize(3)
                    .containsExactly("duplicate", "unique", "duplicate");
        }
    }

    @Nested
    @DisplayName("Error Handling")
    class ErrorHandling {

        @Test
        @DisplayName("toList(varargs) should handle null array")
        void toListVarargsSholdHandleNullArray() {
            // Given/When/Then
            assertThatThrownBy(() -> KeyStringProvider.toList((KeyStringProvider[]) null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("toList(list) should handle null list")
        void toListListSholdHandleNullList() {
            // Given/When/Then
            assertThatThrownBy(() -> KeyStringProvider.toList((List<KeyStringProvider>) null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("toList should handle null provider in array")
        void toListShouldHandleNullProviderInArray() {
            // Given/When/Then
            assertThatThrownBy(() -> KeyStringProvider.toList(() -> "valid", null, () -> "another"))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("toList should handle null provider in list")
        void toListShouldHandleNullProviderInList() {
            // Given
            List<KeyStringProvider> providers = Arrays.asList(() -> "valid", null, () -> "another");

            // When/Then
            assertThatThrownBy(() -> KeyStringProvider.toList(providers))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Integration")
    class Integration {

        @Test
        @DisplayName("should work with different string types")
        void shouldWorkWithDifferentStringTypes() {
            // Given
            KeyStringProvider simple = () -> "simple";
            KeyStringProvider empty = () -> "";
            KeyStringProvider multiline = () -> "line1\nline2";
            KeyStringProvider unicode = () -> "café";
            KeyStringProvider special = () -> "!@#$%^&*()";

            // When
            List<String> result = KeyStringProvider.toList(simple, empty, multiline, unicode, special);

            // Then
            assertThat(result)
                    .hasSize(5)
                    .containsExactly("simple", "", "line1\nline2", "café", "!@#$%^&*()");
        }

        @Test
        @DisplayName("should be reusable across multiple calls")
        void shouldBeReusableAcrossMultipleCalls() {
            // Given
            KeyStringProvider provider = () -> "reusable";

            // When
            List<String> result1 = KeyStringProvider.toList(provider);
            List<String> result2 = KeyStringProvider.toList(provider, provider);

            // Then
            assertThat(result1)
                    .hasSize(1)
                    .containsExactly("reusable");
            assertThat(result2)
                    .hasSize(2)
                    .containsExactly("reusable", "reusable");
        }

        @Test
        @DisplayName("should work with stateful providers")
        void shouldWorkWithStatefulProviders() {
            // Given
            class CountingProvider implements KeyStringProvider {
                private int count = 0;

                @Override
                public String getKey() {
                    return "count-" + (++count);
                }
            }

            CountingProvider provider = new CountingProvider();

            // When
            List<String> result = KeyStringProvider.toList(provider, provider, provider);

            // Then
            assertThat(result)
                    .hasSize(3)
                    .containsExactly("count-1", "count-2", "count-3");
        }

        @Test
        @DisplayName("should work correctly with enum implementations")
        void shouldWorkCorrectlyWithEnumImplementations() {
            // Given
            enum TestEnum implements KeyStringProvider {
                FIRST("first-key"),
                SECOND("second-key"),
                THIRD("third-key");

                private final String key;

                TestEnum(String key) {
                    this.key = key;
                }

                @Override
                public String getKey() {
                    return key;
                }
            }

            // When
            List<String> result = KeyStringProvider.toList(TestEnum.FIRST, TestEnum.SECOND, TestEnum.THIRD);

            // Then
            assertThat(result)
                    .hasSize(3)
                    .containsExactly("first-key", "second-key", "third-key");
        }
    }
}
