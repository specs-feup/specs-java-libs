package pt.up.fe.specs.util.providers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the KeyEnumNameProvider interface.
 * 
 * @author Generated Tests
 */
@DisplayName("KeyEnumNameProvider")
class KeyEnumNameProviderTest {

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("should extend KeyStringProvider")
        void shouldExtendKeyStringProvider() {
            // Given
            enum TestEnum implements KeyEnumNameProvider {
                FIRST, SECOND
            }

            // When
            KeyEnumNameProvider provider = TestEnum.FIRST;

            // Then - should be assignable to KeyStringProvider
            KeyStringProvider stringProvider = provider;
            KeyProvider<String> keyProvider = provider;

            assertThat(stringProvider.getKey()).isEqualTo("FIRST");
            assertThat(keyProvider.getKey()).isEqualTo("FIRST");
        }

        @Test
        @DisplayName("getKey should return name() by default")
        void getKeyShouldReturnNameByDefault() {
            // Given
            enum TestEnum implements KeyEnumNameProvider {
                ALPHA, BETA, GAMMA
            }

            // When/Then
            assertThat(TestEnum.ALPHA.getKey()).isEqualTo(TestEnum.ALPHA.name());
            assertThat(TestEnum.BETA.getKey()).isEqualTo(TestEnum.BETA.name());
            assertThat(TestEnum.GAMMA.getKey()).isEqualTo(TestEnum.GAMMA.name());
        }

        @Test
        @DisplayName("should provide consistent key values")
        void shouldProvideConsistentKeyValues() {
            // Given
            enum TestEnum implements KeyEnumNameProvider {
                CONSTANT_ONE, CONSTANT_TWO
            }

            KeyEnumNameProvider provider = TestEnum.CONSTANT_ONE;

            // When
            String key1 = provider.getKey();
            String key2 = provider.getKey();

            // Then
            assertThat(key1).isEqualTo(key2);
            assertThat(key1).isEqualTo("CONSTANT_ONE");
        }
    }

    @Nested
    @DisplayName("Enum Implementation")
    class EnumImplementation {

        enum StandardEnum implements KeyEnumNameProvider {
            FIRST, SECOND, THIRD
        }

        enum SpecialNamesEnum implements KeyEnumNameProvider {
            UNDERSCORE_NAME, CAMEL_CASE, ALL_CAPS_WITH_UNDERSCORES, singleLowercase
        }

        @Test
        @DisplayName("should work with standard enum names")
        void shouldWorkWithStandardEnumNames() {
            // Given/When/Then
            assertThat(StandardEnum.FIRST.getKey()).isEqualTo("FIRST");
            assertThat(StandardEnum.SECOND.getKey()).isEqualTo("SECOND");
            assertThat(StandardEnum.THIRD.getKey()).isEqualTo("THIRD");
        }

        @Test
        @DisplayName("should work with special enum names")
        void shouldWorkWithSpecialEnumNames() {
            // Given/When/Then
            assertThat(SpecialNamesEnum.UNDERSCORE_NAME.getKey()).isEqualTo("UNDERSCORE_NAME");
            assertThat(SpecialNamesEnum.CAMEL_CASE.getKey()).isEqualTo("CAMEL_CASE");
            assertThat(SpecialNamesEnum.ALL_CAPS_WITH_UNDERSCORES.getKey()).isEqualTo("ALL_CAPS_WITH_UNDERSCORES");
            assertThat(SpecialNamesEnum.singleLowercase.getKey()).isEqualTo("singleLowercase");
        }

        @Test
        @DisplayName("should preserve enum values across multiple calls")
        void shouldPreserveEnumValuesAcrossMultipleCalls() {
            // Given
            StandardEnum constant = StandardEnum.FIRST;

            // When
            String name1 = constant.name();
            String name2 = constant.name();
            String key1 = constant.getKey();
            String key2 = constant.getKey();

            // Then
            assertThat(name1).isEqualTo(name2);
            assertThat(key1).isEqualTo(key2);
            assertThat(name1).isEqualTo(key1);
        }

        @Test
        @DisplayName("should work with enum.values()")
        void shouldWorkWithEnumValues() {
            // Given/When
            StandardEnum[] values = StandardEnum.values();

            // Then
            assertThat(values).hasSize(3);
            assertThat(values[0].getKey()).isEqualTo("FIRST");
            assertThat(values[1].getKey()).isEqualTo("SECOND");
            assertThat(values[2].getKey()).isEqualTo("THIRD");
        }

        @Test
        @DisplayName("should maintain enum ordinal consistency")
        void shouldMaintainEnumOrdinalConsistency() {
            // Given/When/Then
            assertThat(StandardEnum.FIRST.ordinal()).isEqualTo(0);
            assertThat(StandardEnum.SECOND.ordinal()).isEqualTo(1);
            assertThat(StandardEnum.THIRD.ordinal()).isEqualTo(2);

            // Keys should remain consistent regardless of ordinal
            assertThat(StandardEnum.FIRST.getKey()).isEqualTo("FIRST");
            assertThat(StandardEnum.SECOND.getKey()).isEqualTo("SECOND");
            assertThat(StandardEnum.THIRD.getKey()).isEqualTo("THIRD");
        }
    }

    @Nested
    @DisplayName("Integration with KeyStringProvider")
    class IntegrationWithKeyStringProvider {

        enum IntegrationEnum implements KeyEnumNameProvider {
            INTEGRATION_FIRST, INTEGRATION_SECOND, INTEGRATION_THIRD
        }

        @Test
        @DisplayName("should work with KeyStringProvider.toList(varargs)")
        void shouldWorkWithKeyStringProviderToListVarargs() {
            // Given/When
            List<String> result = KeyStringProvider.toList(
                    IntegrationEnum.INTEGRATION_FIRST,
                    IntegrationEnum.INTEGRATION_SECOND,
                    IntegrationEnum.INTEGRATION_THIRD);

            // Then
            assertThat(result)
                    .hasSize(3)
                    .containsExactly("INTEGRATION_FIRST", "INTEGRATION_SECOND", "INTEGRATION_THIRD");
        }

        @Test
        @DisplayName("should work with KeyStringProvider.toList(list)")
        void shouldWorkWithKeyStringProviderToListList() {
            // Given
            List<KeyStringProvider> providers = List.of(
                    IntegrationEnum.INTEGRATION_FIRST,
                    IntegrationEnum.INTEGRATION_SECOND,
                    IntegrationEnum.INTEGRATION_THIRD);

            // When
            List<String> result = KeyStringProvider.toList(providers);

            // Then
            assertThat(result)
                    .hasSize(3)
                    .containsExactly("INTEGRATION_FIRST", "INTEGRATION_SECOND", "INTEGRATION_THIRD");
        }

        @Test
        @DisplayName("should mix with other KeyStringProvider implementations")
        void shouldMixWithOtherKeyStringProviderImplementations() {
            // Given
            KeyStringProvider lambda = () -> "lambda-key";
            KeyStringProvider enum1 = IntegrationEnum.INTEGRATION_FIRST;
            KeyStringProvider enum2 = IntegrationEnum.INTEGRATION_SECOND;

            // When
            List<String> result = KeyStringProvider.toList(lambda, enum1, enum2);

            // Then
            assertThat(result)
                    .hasSize(3)
                    .containsExactly("lambda-key", "INTEGRATION_FIRST", "INTEGRATION_SECOND");
        }
    }

    @Nested
    @DisplayName("Advanced Enum Features")
    class AdvancedEnumFeatures {

        enum EnumWithConstructor implements KeyEnumNameProvider {
            VALUE_ONE("description one"),
            VALUE_TWO("description two"),
            VALUE_THREE("description three");

            private final String description;

            EnumWithConstructor(String description) {
                this.description = description;
            }

            public String getDescription() {
                return description;
            }
        }

        enum EnumWithOverriddenMethod implements KeyEnumNameProvider {
            OVERRIDE_FIRST("custom-first"),
            OVERRIDE_SECOND("custom-second");

            private final String customKey;

            EnumWithOverriddenMethod(String customKey) {
                this.customKey = customKey;
            }

            @Override
            public String getKey() {
                return customKey; // Override the default behavior
            }
        }

        @Test
        @DisplayName("should work with enum constructors and fields")
        void shouldWorkWithEnumConstructorsAndFields() {
            // Given/When/Then
            assertThat(EnumWithConstructor.VALUE_ONE.getKey()).isEqualTo("VALUE_ONE");
            assertThat(EnumWithConstructor.VALUE_ONE.getDescription()).isEqualTo("description one");

            assertThat(EnumWithConstructor.VALUE_TWO.getKey()).isEqualTo("VALUE_TWO");
            assertThat(EnumWithConstructor.VALUE_TWO.getDescription()).isEqualTo("description two");
        }

        @Test
        @DisplayName("should support method overriding")
        void shouldSupportMethodOverriding() {
            // Given/When/Then
            assertThat(EnumWithOverriddenMethod.OVERRIDE_FIRST.name()).isEqualTo("OVERRIDE_FIRST");
            assertThat(EnumWithOverriddenMethod.OVERRIDE_FIRST.getKey()).isEqualTo("custom-first");

            assertThat(EnumWithOverriddenMethod.OVERRIDE_SECOND.name()).isEqualTo("OVERRIDE_SECOND");
            assertThat(EnumWithOverriddenMethod.OVERRIDE_SECOND.getKey()).isEqualTo("custom-second");
        }

        @Test
        @DisplayName("should handle enum with single value")
        void shouldHandleEnumWithSingleValue() {
            // Given
            enum SingleValueEnum implements KeyEnumNameProvider {
                ONLY_VALUE
            }

            // When/Then
            assertThat(SingleValueEnum.ONLY_VALUE.getKey()).isEqualTo("ONLY_VALUE");
            assertThat(SingleValueEnum.values()).hasSize(1);
        }

        @Test
        @DisplayName("should handle enum with many values")
        void shouldHandleEnumWithManyValues() {
            // Given
            enum ManyValuesEnum implements KeyEnumNameProvider {
                V1, V2, V3, V4, V5, V6, V7, V8, V9, V10
            }

            // When
            List<String> keys = KeyStringProvider.toList(ManyValuesEnum.values());

            // Then
            assertThat(keys)
                    .hasSize(10)
                    .containsExactly("V1", "V2", "V3", "V4", "V5", "V6", "V7", "V8", "V9", "V10");
        }
    }

    @Nested
    @DisplayName("Polymorphism")
    class Polymorphism {

        enum PolymorphicEnum implements KeyEnumNameProvider {
            POLY_FIRST, POLY_SECOND
        }

        @Test
        @DisplayName("should work polymorphically as KeyProvider")
        void shouldWorkPolymorphicallyAsKeyProvider() {
            // Given
            KeyProvider<String> provider = PolymorphicEnum.POLY_FIRST;

            // When
            String key = provider.getKey();

            // Then
            assertThat(key).isEqualTo("POLY_FIRST");
        }

        @Test
        @DisplayName("should work polymorphically as KeyStringProvider")
        void shouldWorkPolymorphicallyAsKeyStringProvider() {
            // Given
            KeyStringProvider provider = PolymorphicEnum.POLY_SECOND;

            // When
            String key = provider.getKey();

            // Then
            assertThat(key).isEqualTo("POLY_SECOND");
        }

        @Test
        @DisplayName("should work in collections with other provider types")
        void shouldWorkInCollectionsWithOtherProviderTypes() {
            // Given
            KeyStringProvider lambda = () -> "lambda";
            KeyEnumNameProvider enum1 = PolymorphicEnum.POLY_FIRST;
            KeyEnumNameProvider enum2 = PolymorphicEnum.POLY_SECOND;

            // When
            List<String> result = KeyStringProvider.toList(lambda, enum1, enum2);

            // Then
            assertThat(result)
                    .hasSize(3)
                    .containsExactly("lambda", "POLY_FIRST", "POLY_SECOND");
        }
    }
}
