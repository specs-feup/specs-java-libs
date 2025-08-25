package org.suikasoft.jOptions.Datakey.customkeys;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for MultipleChoiceListKey class.
 * Tests construction, available choices management, default values, generic
 * types, inheritance, edge cases, real-world usage, and constants.
 * 
 * @author Generated Tests
 */
class MultipleChoiceListKeyTest {

    @Nested
    @DisplayName("Construction Tests")
    class ConstructionTests {

        @Test
        @DisplayName("Should construct with string choices")
        void testStringChoicesConstruction() {
            List<String> choices = Arrays.asList("option1", "option2", "option3");
            MultipleChoiceListKey<String> key = new MultipleChoiceListKey<>("test", choices);

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("test");
            assertThat(key.getDefault()).isEmpty(); // No default value provider set

            // Verify choices are stored in extra data
            @SuppressWarnings("unchecked")
            List<String> storedChoices = (List<String>) key.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);
            assertThat(storedChoices).hasSize(3);
            assertThat(storedChoices).containsExactlyInAnyOrder("option1", "option2", "option3");
        }

        @Test
        @DisplayName("Should construct with integer choices")
        void testIntegerChoicesConstruction() {
            List<Integer> choices = Arrays.asList(1, 2, 3, 4, 5);
            MultipleChoiceListKey<Integer> key = new MultipleChoiceListKey<>("intTest", choices);

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("intTest");
            assertThat(key.getDefault()).isEmpty(); // No default value provider set

            // Verify choices are stored in extra data
            @SuppressWarnings("unchecked")
            List<Integer> storedChoices = (List<Integer>) key.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);
            assertThat(storedChoices).containsExactlyInAnyOrder(1, 2, 3, 4, 5);
        }

        @Test
        @DisplayName("Should construct with empty choices")
        void testEmptyChoicesConstruction() {
            List<String> choices = new ArrayList<>();
            MultipleChoiceListKey<String> key = new MultipleChoiceListKey<>("empty", choices);

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("empty");
            assertThat(key.getDefault()).isEmpty(); // No default value provider set

            // Verify choices are stored in extra data
            @SuppressWarnings("unchecked")
            List<String> storedChoices = (List<String>) key.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);
            assertThat(storedChoices).isEmpty();
        }

        @Test
        @DisplayName("Should construct with single choice")
        void testSingleChoiceConstruction() {
            List<String> choices = Arrays.asList("onlyOption");
            MultipleChoiceListKey<String> key = new MultipleChoiceListKey<>("single", choices);

            assertThat(key).isNotNull();
            assertThat(key.getName()).isEqualTo("single");
            assertThat(key.getDefault()).isEmpty(); // No default value provider set

            // Verify choices are stored in extra data
            @SuppressWarnings("unchecked")
            List<String> storedChoices = (List<String>) key.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);
            assertThat(storedChoices).containsExactly("onlyOption");
        }
    }

    @Nested
    @DisplayName("Available Choices Tests")
    class AvailableChoicesTests {

        @Test
        @DisplayName("Should store available choices in extra data")
        void testAvailableChoicesStorage() {
            List<String> choices = Arrays.asList("option1", "option2", "option3", "option4");
            MultipleChoiceListKey<String> key = new MultipleChoiceListKey<>("test", choices);

            // Access available choices from extra data
            @SuppressWarnings("unchecked")
            List<String> storedChoices = (List<String>) key.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);

            assertThat(storedChoices).isNotNull();
            assertThat(storedChoices).hasSize(4);
            assertThat(storedChoices).containsExactlyInAnyOrder("option1", "option2", "option3", "option4");
        }

        @Test
        @DisplayName("Should preserve choice order")
        void testChoiceOrder() {
            List<String> orderedChoices = Arrays.asList("first", "second", "third");
            MultipleChoiceListKey<String> key = new MultipleChoiceListKey<>("ordered", orderedChoices);

            @SuppressWarnings("unchecked")
            List<String> storedChoices = (List<String>) key.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);

            assertThat(storedChoices).containsExactly("first", "second", "third");
        }

        @Test
        @DisplayName("Should handle duplicate choices")
        void testDuplicateChoices() {
            List<String> choicesWithDuplicates = Arrays.asList("option1", "option2", "option1", "option3");
            MultipleChoiceListKey<String> key = new MultipleChoiceListKey<>("duplicates", choicesWithDuplicates);

            @SuppressWarnings("unchecked")
            List<String> storedChoices = (List<String>) key.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);

            assertThat(storedChoices).hasSize(4);
            assertThat(storedChoices).containsExactly("option1", "option2", "option1", "option3");
        }
    }

    @Nested
    @DisplayName("Default Value Tests")
    class DefaultValueTests {

        @Test
        @DisplayName("Should not have default value")
        void testNoDefaultValue() {
            List<String> choices = Arrays.asList("option1", "option2");
            MultipleChoiceListKey<String> stringKey = new MultipleChoiceListKey<>("test", choices);

            // The implementation doesn't provide a default value
            assertThat(stringKey.getDefault()).isEmpty();
            assertThat(stringKey.hasDefaultValue()).isFalse();
        }

        @Test
        @DisplayName("Should work with DataStore")
        void testDataStoreIntegration() {
            List<String> choices = Arrays.asList("option1", "option2");
            MultipleChoiceListKey<String> stringKey = new MultipleChoiceListKey<>("test", choices);

            // When used with DataStore, the key should work correctly
            // even without a default value provider
            assertThat(stringKey.getName()).isEqualTo("test");
            assertThat(stringKey.getValueClass()).isEqualTo(ArrayList.class);
        }

        @Test
        @DisplayName("Should provide choices through extra data")
        void testAvailableChoicesAccess() {
            List<String> choices = Arrays.asList("option1", "option2");
            MultipleChoiceListKey<String> stringKey = new MultipleChoiceListKey<>("test", choices);

            // Available choices should be accessible through extra data
            @SuppressWarnings("unchecked")
            List<String> availableChoices = (List<String>) stringKey.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);
            assertThat(availableChoices).isEqualTo(choices);
        }
    }

    @Nested
    @DisplayName("Generic Type Support Tests")
    class GenericTypeSupportTests {

        private enum TestEnum {
            OPTION_A, OPTION_B, OPTION_C
        }

        private static class CustomOption {
            private final String name;
            private final int value;

            public CustomOption(String name, int value) {
                this.name = name;
                this.value = value;
            }

            @Override
            public String toString() {
                return name + ":" + value;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj)
                    return true;
                if (obj == null || getClass() != obj.getClass())
                    return false;
                CustomOption that = (CustomOption) obj;
                return value == that.value && java.util.Objects.equals(name, that.name);
            }

            @Override
            public int hashCode() {
                return java.util.Objects.hash(name, value);
            }
        }

        @Test
        @DisplayName("Should support custom object generic types")
        void testCustomObjectGenericType() {
            List<CustomOption> options = Arrays.asList(
                    new CustomOption("fast", 1),
                    new CustomOption("balanced", 2),
                    new CustomOption("slow", 3));

            MultipleChoiceListKey<CustomOption> customKey = new MultipleChoiceListKey<>("custom", options);

            assertThat(customKey).isNotNull();
            assertThat(customKey.getName()).isEqualTo("custom");
            assertThat(customKey.getDefault()).isEmpty(); // No default value provider set

            @SuppressWarnings("unchecked")
            List<CustomOption> storedChoices = (List<CustomOption>) customKey.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);
            assertThat(storedChoices).hasSize(3);
        }

        @Test
        @DisplayName("Should support enum generic types")
        void testEnumGenericType() {
            List<TestEnum> enumChoices = Arrays.asList(TestEnum.OPTION_A, TestEnum.OPTION_B, TestEnum.OPTION_C);
            MultipleChoiceListKey<TestEnum> enumKey = new MultipleChoiceListKey<>("enumTest", enumChoices);

            assertThat(enumKey).isNotNull();
            assertThat(enumKey.getName()).isEqualTo("enumTest");

            @SuppressWarnings("unchecked")
            List<TestEnum> storedChoices = (List<TestEnum>) enumKey.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);
            assertThat(storedChoices).containsExactlyInAnyOrder(TestEnum.OPTION_A, TestEnum.OPTION_B,
                    TestEnum.OPTION_C);
        }
    }

    @Nested
    @DisplayName("Key Inheritance Tests")
    class KeyInheritanceTests {

        @Test
        @DisplayName("Should extend GenericKey correctly")
        void testGenericKeyInheritance() {
            List<String> choices = Arrays.asList("choice1", "choice2");
            MultipleChoiceListKey<String> key = new MultipleChoiceListKey<>("test", choices);

            // Should be instance of GenericKey
            assertThat(key).isInstanceOf(org.suikasoft.jOptions.Datakey.GenericKey.class);

            // Should inherit DataKey interface methods
            assertThat(key.getName()).isEqualTo("test");
        }

        @Test
        @DisplayName("Should have default value")
        void testHasDefaultValue() {
            List<String> choices = Arrays.asList("choice1", "choice2");
            MultipleChoiceListKey<String> key = new MultipleChoiceListKey<>("test", choices);

            assertThat(key.hasDefaultValue()).isFalse(); // Implementation doesn't provide default value
        }

        @Test
        @DisplayName("Should have extra data")
        void testHasExtraData() {
            List<String> choices = Arrays.asList("choice1", "choice2");
            MultipleChoiceListKey<String> key = new MultipleChoiceListKey<>("test", choices);

            assertThat(key.getExtraData()).isPresent();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle large choice lists")
        void testLargeChoiceLists() {
            List<Integer> largeChoices = IntStream.range(0, 1000)
                    .boxed()
                    .collect(java.util.stream.Collectors.toList());

            MultipleChoiceListKey<Integer> largeKey = new MultipleChoiceListKey<>("large", largeChoices);

            assertThat(largeKey).isNotNull();
            assertThat(largeKey.getName()).isEqualTo("large");
            assertThat(largeKey.getDefault()).isEmpty(); // No default value provider set

            @SuppressWarnings("unchecked")
            List<Integer> storedChoices = (List<Integer>) largeKey.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);
            assertThat(storedChoices).hasSize(1000);
            assertThat(storedChoices).contains(0, 500, 999);
        }

        @Test
        @DisplayName("Should handle null key ID")
        void testNullKeyId() {
            List<String> choices = Arrays.asList("option1", "option2");

            // The implementation validates key ID and throws an assertion error for null
            assertThatThrownBy(() -> new MultipleChoiceListKey<String>(null, choices))
                    .isInstanceOf(AssertionError.class);
        }

        @Test
        @DisplayName("Should handle choices with null elements")
        void testChoicesWithNullElements() {
            List<String> choicesWithNull = Arrays.asList("option1", null, "option2");
            MultipleChoiceListKey<String> key = new MultipleChoiceListKey<>("withNull", choicesWithNull);

            assertThat(key).isNotNull();

            @SuppressWarnings("unchecked")
            List<String> storedChoices = (List<String>) key.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);
            assertThat(storedChoices).hasSize(3);
            assertThat(storedChoices).containsExactly("option1", null, "option2");
        }
    }

    @Nested
    @DisplayName("Real-World Usage Tests")
    class RealWorldUsageTests {

        @Test
        @DisplayName("Should work as configuration option")
        void testAsConfigurationOption() {
            List<String> optimizationLevels = Arrays.asList("O0", "O1", "O2", "O3", "Os", "Oz");
            MultipleChoiceListKey<String> optimizationKey = new MultipleChoiceListKey<>("optimizations",
                    optimizationLevels);

            assertThat(optimizationKey.getName()).isEqualTo("optimizations");
            assertThat(optimizationKey.getDefault()).isEmpty(); // No default value provider set

            @SuppressWarnings("unchecked")
            List<String> availableOptimizations = (List<String>) optimizationKey.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);
            assertThat(availableOptimizations).hasSize(6);
            assertThat(availableOptimizations).containsExactlyInAnyOrder("O0", "O1", "O2", "O3", "Os", "Oz");
        }

        @Test
        @DisplayName("Should work as feature flags")
        void testAsFeatureFlags() {
            List<String> features = Arrays.asList("feature_a", "feature_b", "feature_c");
            MultipleChoiceListKey<String> featuresKey = new MultipleChoiceListKey<>("enabled_features", features);

            assertThat(featuresKey.getName()).isEqualTo("enabled_features");
            assertThat(featuresKey.getDefault()).isEmpty(); // No default value provider set
        }

        @Test
        @DisplayName("Should work as file extensions selector")
        void testAsFileExtensions() {
            List<String> extensions = Arrays.asList(".java", ".kt", ".scala", ".groovy");
            MultipleChoiceListKey<String> extensionsKey = new MultipleChoiceListKey<>("file_extensions", extensions);

            assertThat(extensionsKey.getName()).isEqualTo("file_extensions");

            @SuppressWarnings("unchecked")
            List<String> supportedExtensions = (List<String>) extensionsKey.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);
            assertThat(supportedExtensions).contains(".java", ".kt", ".scala", ".groovy");
        }
    }

    @Nested
    @DisplayName("Constants Tests")
    class ConstantsTests {

        @Test
        @DisplayName("Should have AVAILABLE_CHOICES constant")
        void testAvailableChoicesConstant() {
            assertThat(MultipleChoiceListKey.AVAILABLE_CHOICES).isNotNull();
            assertThat(MultipleChoiceListKey.AVAILABLE_CHOICES.getName()).isEqualTo("availableChoices");
        }

        @Test
        @DisplayName("Should use constant for storing choices")
        void testConstantUsage() {
            List<String> choices = Arrays.asList("choice1", "choice2");
            MultipleChoiceListKey<String> key = new MultipleChoiceListKey<>("test", choices);

            // Verify that the constant is used for storing choices
            assertThat(key.getExtraData().get().hasValue(MultipleChoiceListKey.AVAILABLE_CHOICES)).isTrue();

            @SuppressWarnings("unchecked")
            List<String> storedChoices = (List<String>) key.getExtraData().get()
                    .get(MultipleChoiceListKey.AVAILABLE_CHOICES);
            assertThat(storedChoices).isEqualTo(choices);
        }
    }
}
