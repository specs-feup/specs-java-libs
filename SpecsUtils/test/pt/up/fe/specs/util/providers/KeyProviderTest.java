package pt.up.fe.specs.util.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for KeyProvider interface and its implementations.
 * 
 * @author Generated Tests
 */
@DisplayName("KeyProvider")
class KeyProviderTest {

    private TestKeyProvider testProvider;
    private String testKey;

    @BeforeEach
    void setUp() {
        testKey = "test-key-123";
        testProvider = new TestKeyProvider(testKey);
    }

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("should have correct interface method")
        void shouldHaveCorrectInterfaceMethod() {
            assertThatCode(() -> {
                KeyProvider.class.getMethod("getKey");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should be a functional interface")
        void shouldBeAFunctionalInterface() {
            assertThat(KeyProvider.class.isInterface()).isTrue();
            assertThat(KeyProvider.class.getAnnotation(FunctionalInterface.class)).isNull(); // Not explicitly annotated
                                                                                             // but functionally
                                                                                             // equivalent
        }

        @Test
        @DisplayName("should allow generic type specification")
        void shouldAllowGenericTypeSpecification() {
            KeyProvider<String> stringProvider = () -> "string-key";
            KeyProvider<Integer> intProvider = () -> 42;
            KeyProvider<Long> longProvider = () -> 123L;

            assertThat(stringProvider.getKey()).isEqualTo("string-key");
            assertThat(intProvider.getKey()).isEqualTo(42);
            assertThat(longProvider.getKey()).isEqualTo(123L);
        }
    }

    @Nested
    @DisplayName("Implementation Behavior")
    class ImplementationBehavior {

        @Test
        @DisplayName("should return correct key value")
        void shouldReturnCorrectKeyValue() {
            assertThat(testProvider.getKey()).isEqualTo(testKey);
        }

        @Test
        @DisplayName("should handle null key values")
        void shouldHandleNullKeyValues() {
            TestKeyProvider nullProvider = new TestKeyProvider(null);

            assertThat(nullProvider.getKey()).isNull();
        }

        @Test
        @DisplayName("should handle empty string keys")
        void shouldHandleEmptyStringKeys() {
            TestKeyProvider emptyProvider = new TestKeyProvider("");

            assertThat(emptyProvider.getKey()).isEmpty();
        }

        @Test
        @DisplayName("should support consistent key retrieval")
        void shouldSupportConsistentKeyRetrieval() {
            String key = testProvider.getKey();

            assertThat(testProvider.getKey()).isEqualTo(key);
            assertThat(testProvider.getKey()).isEqualTo(key);
        }
    }

    @Nested
    @DisplayName("Lambda Implementation")
    class LambdaImplementation {

        @Test
        @DisplayName("should work with lambda expressions")
        void shouldWorkWithLambdaExpressions() {
            KeyProvider<String> lambdaProvider = () -> "lambda-key";

            assertThat(lambdaProvider.getKey()).isEqualTo("lambda-key");
        }

        @Test
        @DisplayName("should work with method references")
        void shouldWorkWithMethodReferences() {
            String constantKey = "method-ref-key";
            KeyProvider<String> methodRefProvider = () -> constantKey;

            assertThat(methodRefProvider.getKey()).isEqualTo(constantKey);
        }

        @Test
        @DisplayName("should support dynamic key generation")
        void shouldSupportDynamicKeyGeneration() {
            KeyProvider<String> dynamicProvider = () -> "dynamic-" + System.currentTimeMillis();

            String key1 = dynamicProvider.getKey();
            String key2 = dynamicProvider.getKey();

            // Keys might be different due to timing
            assertThat(key1).startsWith("dynamic-");
            assertThat(key2).startsWith("dynamic-");
        }
    }

    @Nested
    @DisplayName("Different Key Types")
    class DifferentKeyTypes {

        @Test
        @DisplayName("should support Integer keys")
        void shouldSupportIntegerKeys() {
            KeyProvider<Integer> intProvider = () -> 42;

            assertThat(intProvider.getKey()).isEqualTo(42);
        }

        @Test
        @DisplayName("should support Long keys")
        void shouldSupportLongKeys() {
            KeyProvider<Long> longProvider = () -> 123456789L;

            assertThat(longProvider.getKey()).isEqualTo(123456789L);
        }

        @Test
        @DisplayName("should support enum keys")
        void shouldSupportEnumKeys() {
            KeyProvider<TestEnum> enumProvider = () -> TestEnum.VALUE_A;

            assertThat(enumProvider.getKey()).isEqualTo(TestEnum.VALUE_A);
        }

        @Test
        @DisplayName("should support custom object keys")
        void shouldSupportCustomObjectKeys() {
            TestKey customKey = new TestKey("custom", 123);
            KeyProvider<TestKey> customProvider = () -> customKey;

            assertThat(customProvider.getKey()).isEqualTo(customKey);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle providers that throw exceptions")
        void shouldHandleProvidersThrowExceptions() {
            KeyProvider<String> throwingProvider = () -> {
                throw new RuntimeException("Key generation failed");
            };

            assertThatCode(() -> throwingProvider.getKey())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Key generation failed");
        }

        @Test
        @DisplayName("should support providers with side effects")
        void shouldSupportProvidersWithSideEffects() {
            Counter counter = new Counter();
            KeyProvider<Integer> sideEffectProvider = () -> {
                counter.increment();
                return counter.getValue();
            };

            assertThat(sideEffectProvider.getKey()).isEqualTo(1);
            assertThat(sideEffectProvider.getKey()).isEqualTo(2);
            assertThat(sideEffectProvider.getKey()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Polymorphism")
    class Polymorphism {

        @Test
        @DisplayName("should work with different implementations")
        void shouldWorkWithDifferentImplementations() {
            KeyProvider<String> impl1 = new TestKeyProvider("impl1");
            KeyProvider<String> impl2 = () -> "impl2";

            assertThat(impl1.getKey()).isEqualTo("impl1");
            assertThat(impl2.getKey()).isEqualTo("impl2");
        }

        @Test
        @DisplayName("should support interface-based programming")
        void shouldSupportInterfaceBasedProgramming() {
            java.util.List<KeyProvider<String>> providers = java.util.Arrays.asList(
                    new TestKeyProvider("provider1"),
                    () -> "provider2",
                    new TestKeyProvider("provider3"));

            assertThat(providers)
                    .extracting(KeyProvider::getKey)
                    .containsExactly("provider1", "provider2", "provider3");
        }
    }

    /**
     * Test implementation of KeyProvider for testing purposes.
     */
    private static class TestKeyProvider implements KeyProvider<String> {
        private final String key;

        public TestKeyProvider(String key) {
            this.key = key;
        }

        @Override
        public String getKey() {
            return key;
        }
    }

    /**
     * Test enum for enum key testing.
     */
    private enum TestEnum {
        VALUE_A, VALUE_B, VALUE_C
    }

    /**
     * Test custom key class for object key testing.
     */
    private static class TestKey {
        private final String name;
        private final int value;

        public TestKey(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null || getClass() != obj.getClass())
                return false;
            TestKey testKey = (TestKey) obj;
            return value == testKey.value && java.util.Objects.equals(name, testKey.name);
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(name, value);
        }
    }

    /**
     * Counter utility for side effect testing.
     */
    private static class Counter {
        private int value = 0;

        public void increment() {
            value++;
        }

        public int getValue() {
            return value;
        }
    }
}
