package org.suikasoft.jOptions.DataStore;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

/**
 * Test suite for DataKeyProvider interface functionality.
 * Uses concrete test implementations to verify the interface contract.
 * 
 * @author Generated Tests
 */
@DisplayName("DataKeyProvider")
class DataKeyProviderTest {

    private DataKey<String> stringKey;
    private DataKey<Integer> intKey;
    private DataKey<Boolean> boolKey;

    @BeforeEach
    void setUp() {
        // Create test DataKeys
        stringKey = KeyFactory.string("string");
        intKey = KeyFactory.integer("int");
        boolKey = KeyFactory.bool("bool");
    }

    @Nested
    @DisplayName("Interface Implementation")
    class InterfaceImplementationTests {

        @Test
        @DisplayName("simple implementation returns correct DataKey")
        void testSimpleImplementation_ReturnsCorrectDataKey() {
            DataKeyProvider provider = () -> stringKey;

            DataKey<?> result = provider.getDataKey();

            assertThat(result).isSameAs(stringKey);
        }

        @Test
        @DisplayName("implementation with different types works correctly")
        void testImplementationWithDifferentTypes_WorksCorrectly() {
            DataKeyProvider stringProvider = () -> stringKey;
            DataKeyProvider intProvider = () -> intKey;
            DataKeyProvider boolProvider = () -> boolKey;

            assertThat(stringProvider.getDataKey()).isSameAs(stringKey);
            assertThat(intProvider.getDataKey()).isSameAs(intKey);
            assertThat(boolProvider.getDataKey()).isSameAs(boolKey);
        }

        @Test
        @DisplayName("implementation can return null")
        void testImplementation_CanReturnNull() {
            DataKeyProvider provider = () -> null;

            DataKey<?> result = provider.getDataKey();

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Concrete Class Implementations")
    class ConcreteClassImplementationsTests {

        /**
         * Test implementation that holds a DataKey.
         */
        private static class SimpleDataKeyProvider implements DataKeyProvider {
            private final DataKey<?> dataKey;

            public SimpleDataKeyProvider(DataKey<?> dataKey) {
                this.dataKey = dataKey;
            }

            @Override
            public DataKey<?> getDataKey() {
                return dataKey;
            }
        }

        /**
         * Test implementation that dynamically creates DataKeys.
         */
        private static class DynamicDataKeyProvider implements DataKeyProvider {
            private final String keyName;
            private final String keyType;

            public DynamicDataKeyProvider(String keyName, String keyType) {
                this.keyName = keyName;
                this.keyType = keyType;
            }

            @Override
            public DataKey<?> getDataKey() {
                switch (keyType.toLowerCase()) {
                    case "string":
                        return KeyFactory.string(keyName);
                    case "int":
                    case "integer":
                        return KeyFactory.integer(keyName);
                    case "bool":
                    case "boolean":
                        return KeyFactory.bool(keyName);
                    default:
                        return KeyFactory.object(keyName, Object.class);
                }
            }
        }

        @Test
        @DisplayName("simple concrete provider works correctly")
        void testSimpleConcreteProvider_WorksCorrectly() {
            SimpleDataKeyProvider provider = new SimpleDataKeyProvider(stringKey);

            DataKey<?> result = provider.getDataKey();

            assertThat(result).isSameAs(stringKey);
        }

        @Test
        @DisplayName("concrete provider with null DataKey works")
        void testConcreteProviderWithNull_Works() {
            SimpleDataKeyProvider provider = new SimpleDataKeyProvider(null);

            DataKey<?> result = provider.getDataKey();

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("dynamic provider creates string keys correctly")
        void testDynamicProvider_CreatesStringKeysCorrectly() {
            DynamicDataKeyProvider provider = new DynamicDataKeyProvider("testString", "string");

            DataKey<?> result = provider.getDataKey();

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("testString");
        }

        @Test
        @DisplayName("dynamic provider creates integer keys correctly")
        void testDynamicProvider_CreatesIntegerKeysCorrectly() {
            DynamicDataKeyProvider provider = new DynamicDataKeyProvider("testInt", "integer");

            DataKey<?> result = provider.getDataKey();

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("testInt");
        }

        @Test
        @DisplayName("dynamic provider creates boolean keys correctly")
        void testDynamicProvider_CreatesBooleanKeysCorrectly() {
            DynamicDataKeyProvider provider = new DynamicDataKeyProvider("testBool", "boolean");

            DataKey<?> result = provider.getDataKey();

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("testBool");
        }

        @Test
        @DisplayName("dynamic provider creates object keys for unknown types")
        void testDynamicProvider_CreatesObjectKeysForUnknownTypes() {
            DynamicDataKeyProvider provider = new DynamicDataKeyProvider("testUnknown", "unknown");

            DataKey<?> result = provider.getDataKey();

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("testUnknown");
        }

        @Test
        @DisplayName("different instances return equal keys for same parameters")
        void testDifferentInstances_ReturnEqualKeysForSameParameters() {
            DynamicDataKeyProvider provider1 = new DynamicDataKeyProvider("sameName", "string");
            DynamicDataKeyProvider provider2 = new DynamicDataKeyProvider("sameName", "string");

            DataKey<?> key1 = provider1.getDataKey();
            DataKey<?> key2 = provider2.getDataKey();

            // Keys should be equal if they have the same name and type
            assertThat(key1.getName()).isEqualTo(key2.getName());
            assertThat(key1.equals(key2)).isTrue();
        }
    }

    @Nested
    @DisplayName("Interface Contract Verification")
    class InterfaceContractVerificationTests {

        @Test
        @DisplayName("interface defines exactly one method")
        void testInterface_DefinesExactlyOneMethod() {
            assertThat(DataKeyProvider.class.getMethods())
                    .hasSize(1); // Only getDataKey() method
        }

        @Test
        @DisplayName("getDataKey method has correct signature")
        void testGetDataKeyMethod_HasCorrectSignature() throws NoSuchMethodException {
            var method = DataKeyProvider.class.getMethod("getDataKey");

            assertThat(method.getName()).isEqualTo("getDataKey");
            assertThat(method.getReturnType()).isEqualTo(DataKey.class);
            assertThat(method.getParameterCount()).isZero();
        }

        @Test
        @DisplayName("interface is public and abstract")
        void testInterface_IsPublicAndAbstract() {
            Class<DataKeyProvider> clazz = DataKeyProvider.class;

            assertThat(clazz.isInterface()).isTrue();
            assertThat(java.lang.reflect.Modifier.isPublic(clazz.getModifiers())).isTrue();
            assertThat(java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())).isTrue();
        }
    }

    @Nested
    @DisplayName("Usage Patterns and Best Practices")
    class UsagePatternsAndBestPracticesTests {

        @Test
        @DisplayName("provider can be used in collections")
        void testProvider_CanBeUsedInCollections() {
            java.util.List<DataKeyProvider> providers = java.util.Arrays.asList(
                    () -> stringKey,
                    () -> intKey,
                    () -> boolKey);

            java.util.List<DataKey<?>> keys = providers.stream()
                    .map(DataKeyProvider::getDataKey)
                    .collect(java.util.stream.Collectors.toList());

            assertThat(keys).hasSize(3);
            assertThat(keys).containsExactly(stringKey, intKey, boolKey);
        }

        @Test
        @DisplayName("provider can be used as method parameter")
        void testProvider_CanBeUsedAsMethodParameter() {
            DataKey<?> result = extractKeyFromProvider(() -> stringKey);

            assertThat(result).isSameAs(stringKey);
        }

        @Test
        @DisplayName("provider supports method references")
        void testProvider_SupportsMethodReferences() {
            SimpleDataKeyHolder holder = new SimpleDataKeyHolder(stringKey);
            DataKeyProvider provider = holder::getKey;

            DataKey<?> result = provider.getDataKey();

            assertThat(result).isSameAs(stringKey);
        }

        // Helper method for testing
        private DataKey<?> extractKeyFromProvider(DataKeyProvider provider) {
            return provider.getDataKey();
        }

        // Helper class for method reference testing
        private static class SimpleDataKeyHolder {
            private final DataKey<?> key;

            public SimpleDataKeyHolder(DataKey<?> key) {
                this.key = key;
            }

            public DataKey<?> getKey() {
                return key;
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("provider can handle rapid successive calls")
        void testProvider_CanHandleRapidSuccessiveCalls() {
            DataKeyProvider provider = () -> stringKey;

            // Call multiple times rapidly
            for (int i = 0; i < 1000; i++) {
                DataKey<?> result = provider.getDataKey();
                assertThat(result).isSameAs(stringKey);
            }
        }

        @Test
        @DisplayName("provider works correctly when key changes")
        void testProvider_WorksCorrectlyWhenKeyChanges() {
            MutableDataKeyProvider provider = new MutableDataKeyProvider(stringKey);

            assertThat(provider.getDataKey()).isSameAs(stringKey);

            provider.setDataKey(intKey);
            assertThat(provider.getDataKey()).isSameAs(intKey);

            provider.setDataKey(null);
            assertThat(provider.getDataKey()).isNull();
        }

        @Test
        @DisplayName("provider interface supports inheritance")
        void testProviderInterface_SupportsInheritance() {
            ExtendedDataKeyProviderImpl provider = new ExtendedDataKeyProviderImpl(stringKey);

            // Should work as DataKeyProvider
            DataKeyProvider baseProvider = provider;
            assertThat(baseProvider.getDataKey()).isSameAs(stringKey);

            // Should work as ExtendedDataKeyProvider
            assertThat(provider.getKeyName()).isEqualTo("string");
        }

        // Helper class for mutable key testing
        private static class MutableDataKeyProvider implements DataKeyProvider {
            private DataKey<?> dataKey;

            public MutableDataKeyProvider(DataKey<?> dataKey) {
                this.dataKey = dataKey;
            }

            public void setDataKey(DataKey<?> dataKey) {
                this.dataKey = dataKey;
            }

            @Override
            public DataKey<?> getDataKey() {
                return dataKey;
            }
        }

        // Helper interface for inheritance testing
        private interface ExtendedDataKeyProvider extends DataKeyProvider {
            default String getKeyName() {
                DataKey<?> key = getDataKey();
                return key != null ? key.getName() : null;
            }
        }

        // Helper class implementing extended interface
        private static class ExtendedDataKeyProviderImpl implements ExtendedDataKeyProvider {
            private final DataKey<?> dataKey;

            public ExtendedDataKeyProviderImpl(DataKey<?> dataKey) {
                this.dataKey = dataKey;
            }

            @Override
            public DataKey<?> getDataKey() {
                return dataKey;
            }
        }
    }
}
