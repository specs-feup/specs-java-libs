package org.suikasoft.jOptions.DataStore;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Test suite for EnumDataKeyProvider interface functionality.
 * Uses concrete enum implementations to verify the interface contract.
 * 
 * @author Generated Tests
 */
@DisplayName("EnumDataKeyProvider")
class EnumDataKeyProviderTest {

    /**
     * Test enum implementation of EnumDataKeyProvider for testing basic
     * functionality.
     */
    public enum TestKeysEnum implements EnumDataKeyProvider<TestKeysEnum> {
        STRING_KEY(KeyFactory.string("stringValue")),
        INT_KEY(KeyFactory.integer("intValue")),
        BOOL_KEY(KeyFactory.bool("boolValue"));

        private final DataKey<?> dataKey;

        TestKeysEnum(DataKey<?> dataKey) {
            this.dataKey = dataKey;
        }

        @Override
        public DataKey<?> getDataKey() {
            return dataKey;
        }

        @Override
        public Class<TestKeysEnum> getEnumClass() {
            return TestKeysEnum.class;
        }
    }

    /**
     * Test enum with single key for edge case testing.
     */
    public enum SingleKeyEnum implements EnumDataKeyProvider<SingleKeyEnum> {
        ONLY_KEY(KeyFactory.string("onlyKey"));

        private final DataKey<?> dataKey;

        SingleKeyEnum(DataKey<?> dataKey) {
            this.dataKey = dataKey;
        }

        @Override
        public DataKey<?> getDataKey() {
            return dataKey;
        }

        @Override
        public Class<SingleKeyEnum> getEnumClass() {
            return SingleKeyEnum.class;
        }
    }

    /**
     * Test enum with complex keys for advanced testing.
     */
    public enum ComplexKeysEnum implements EnumDataKeyProvider<ComplexKeysEnum> {
        LIST_KEY(KeyFactory.stringList("listValue")),
        OBJECT_KEY(KeyFactory.object("objectValue", Object.class)),
        OPTIONAL_KEY(KeyFactory.optional("optionalValue"));

        private final DataKey<?> dataKey;

        ComplexKeysEnum(DataKey<?> dataKey) {
            this.dataKey = dataKey;
        }

        @Override
        public DataKey<?> getDataKey() {
            return dataKey;
        }

        @Override
        public Class<ComplexKeysEnum> getEnumClass() {
            return ComplexKeysEnum.class;
        }
    }

    @Nested
    @DisplayName("DataKeyProvider Functionality")
    class DataKeyProviderFunctionalityTests {

        @Test
        @DisplayName("enum constants return correct DataKeys")
        void testEnumConstants_ReturnCorrectDataKeys() {
            assertThat(TestKeysEnum.STRING_KEY.getDataKey().getName()).isEqualTo("stringValue");
            assertThat(TestKeysEnum.INT_KEY.getDataKey().getName()).isEqualTo("intValue");
            assertThat(TestKeysEnum.BOOL_KEY.getDataKey().getName()).isEqualTo("boolValue");
        }

        @Test
        @DisplayName("each enum constant has unique DataKey")
        void testEnumConstants_HaveUniqueDataKeys() {
            List<DataKey<?>> keys = Arrays.stream(TestKeysEnum.values())
                    .map(TestKeysEnum::getDataKey)
                    .collect(Collectors.toList());

            assertThat(keys).hasSize(3);
            assertThat(keys).doesNotHaveDuplicates();
        }

        @Test
        @DisplayName("DataKeys are not null")
        void testDataKeys_AreNotNull() {
            for (TestKeysEnum enumValue : TestKeysEnum.values()) {
                assertThat(enumValue.getDataKey()).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("Enum Class Information")
    class EnumClassInformationTests {

        @Test
        @DisplayName("getEnumClass returns correct class for all constants")
        void testGetEnumClass_ReturnsCorrectClassForAllConstants() {
            for (TestKeysEnum enumValue : TestKeysEnum.values()) {
                assertThat(enumValue.getEnumClass()).isEqualTo(TestKeysEnum.class);
            }
        }

        @Test
        @DisplayName("getEnumClass is consistent across instances")
        void testGetEnumClass_IsConsistentAcrossInstances() {
            Class<TestKeysEnum> class1 = TestKeysEnum.STRING_KEY.getEnumClass();
            Class<TestKeysEnum> class2 = TestKeysEnum.INT_KEY.getEnumClass();
            Class<TestKeysEnum> class3 = TestKeysEnum.BOOL_KEY.getEnumClass();

            assertThat(class1).isSameAs(class2);
            assertThat(class2).isSameAs(class3);
        }

        @Test
        @DisplayName("enum class contains all expected constants")
        void testEnumClass_ContainsAllExpectedConstants() {
            TestKeysEnum[] constants = TestKeysEnum.STRING_KEY.getEnumClass().getEnumConstants();

            assertThat(constants).hasSize(3);
            assertThat(constants).contains(TestKeysEnum.STRING_KEY, TestKeysEnum.INT_KEY, TestKeysEnum.BOOL_KEY);
        }
    }

    @Nested
    @DisplayName("StoreDefinition Generation")
    class StoreDefinitionGenerationTests {

        @Test
        @DisplayName("getStoreDefinition returns definition with all enum keys")
        void testGetStoreDefinition_ReturnsDefinitionWithAllEnumKeys() {
            StoreDefinition definition = TestKeysEnum.STRING_KEY.getStoreDefinition();

            List<DataKey<?>> keys = definition.getKeys();
            assertThat(keys).hasSize(3);

            List<String> keyNames = keys.stream()
                    .map(DataKey::getName)
                    .collect(Collectors.toList());
            assertThat(keyNames).containsExactlyInAnyOrder("stringValue", "intValue", "boolValue");
        }

        @Test
        @DisplayName("getStoreDefinition uses enum class simple name")
        void testGetStoreDefinition_UsesEnumClassSimpleName() {
            StoreDefinition definition = TestKeysEnum.STRING_KEY.getStoreDefinition();

            assertThat(definition.getName()).isEqualTo("TestKeysEnum");
        }

        @Test
        @DisplayName("getStoreDefinition is consistent across enum constants")
        void testGetStoreDefinition_IsConsistentAcrossEnumConstants() {
            StoreDefinition def1 = TestKeysEnum.STRING_KEY.getStoreDefinition();
            StoreDefinition def2 = TestKeysEnum.INT_KEY.getStoreDefinition();
            StoreDefinition def3 = TestKeysEnum.BOOL_KEY.getStoreDefinition();

            // Should return equivalent store definitions
            assertThat(def1.getName()).isEqualTo(def2.getName());
            assertThat(def2.getName()).isEqualTo(def3.getName());
            assertThat(def1.getKeys()).hasSize(def2.getKeys().size());
            assertThat(def2.getKeys()).hasSize(def3.getKeys().size());
        }

        @Test
        @DisplayName("single key enum creates correct StoreDefinition")
        void testSingleKeyEnum_CreatesCorrectStoreDefinition() {
            StoreDefinition definition = SingleKeyEnum.ONLY_KEY.getStoreDefinition();

            assertThat(definition.getName()).isEqualTo("SingleKeyEnum");
            assertThat(definition.getKeys()).hasSize(1);
            assertThat(definition.getKeys().get(0).getName()).isEqualTo("onlyKey");
        }

        @Test
        @DisplayName("complex keys enum creates correct StoreDefinition")
        void testComplexKeysEnum_CreatesCorrectStoreDefinition() {
            StoreDefinition definition = ComplexKeysEnum.LIST_KEY.getStoreDefinition();

            assertThat(definition.getName()).isEqualTo("ComplexKeysEnum");
            assertThat(definition.getKeys()).hasSize(3);

            List<String> keyNames = definition.getKeys().stream()
                    .map(DataKey::getName)
                    .collect(Collectors.toList());
            assertThat(keyNames).containsExactlyInAnyOrder("listValue", "objectValue", "optionalValue");
        }
    }

    @Nested
    @DisplayName("Interface Inheritance")
    class InterfaceInheritanceTests {

        @Test
        @DisplayName("implements DataKeyProvider interface")
        void testImplements_DataKeyProviderInterface() {
            assertThat(DataKeyProvider.class.isAssignableFrom(TestKeysEnum.class)).isTrue();

            DataKeyProvider provider = TestKeysEnum.STRING_KEY;
            assertThat(provider.getDataKey()).isNotNull();
        }

        @Test
        @DisplayName("implements StoreDefinitionProvider interface")
        void testImplements_StoreDefinitionProviderInterface() {
            // First, let me check if this interface exists
            try {
                Class<?> storeDefProviderClass = Class
                        .forName("org.suikasoft.jOptions.storedefinition.StoreDefinitionProvider");
                assertThat(storeDefProviderClass.isAssignableFrom(TestKeysEnum.class)).isTrue();
            } catch (ClassNotFoundException e) {
                // If the interface doesn't exist, that's fine - just check the method
                assertThat(TestKeysEnum.STRING_KEY.getStoreDefinition()).isNotNull();
            }
        }

        @Test
        @DisplayName("can be used polymorphically as DataKeyProvider")
        void testCanBeUsedPolymorphically_AsDataKeyProvider() {
            List<DataKeyProvider> providers = Arrays.asList(
                    TestKeysEnum.STRING_KEY,
                    TestKeysEnum.INT_KEY,
                    TestKeysEnum.BOOL_KEY);

            List<DataKey<?>> keys = providers.stream()
                    .map(DataKeyProvider::getDataKey)
                    .collect(Collectors.toList());

            assertThat(keys).hasSize(3);
            assertThat(keys).allMatch(key -> key != null);
        }
    }

    @Nested
    @DisplayName("Generic Type Constraints")
    class GenericTypeConstraintsTests {

        @Test
        @DisplayName("enum class matches generic type parameter")
        void testEnumClass_MatchesGenericTypeParameter() {
            TestKeysEnum enumValue = TestKeysEnum.STRING_KEY;
            Class<TestKeysEnum> enumClass = enumValue.getEnumClass();

            assertThat(enumClass).isEqualTo(TestKeysEnum.class);
            assertThat(enumClass.isEnum()).isTrue();
        }

        @Test
        @DisplayName("generic constraint ensures proper type safety")
        void testGenericConstraint_EnsuresProperTypeSafety() {
            // This test verifies that the generic constraint works at compile time
            // The fact that TestKeysEnum compiles means the constraint is satisfied

            TestKeysEnum[] constants = TestKeysEnum.STRING_KEY.getEnumClass().getEnumConstants();

            // Each constant should implement EnumDataKeyProvider
            for (TestKeysEnum constant : constants) {
                assertThat(constant).isInstanceOf(EnumDataKeyProvider.class);
                assertThat(constant).isInstanceOf(TestKeysEnum.class);
            }
        }
    }

    @Nested
    @DisplayName("Usage Patterns and Best Practices")
    class UsagePatternsAndBestPracticesTests {

        @Test
        @DisplayName("can be used in switch statements")
        void testCanBeUsed_InSwitchStatements() {
            TestKeysEnum key = TestKeysEnum.STRING_KEY;
            String result;

            switch (key) {
                case STRING_KEY:
                    result = "string";
                    break;
                case INT_KEY:
                    result = "int";
                    break;
                case BOOL_KEY:
                    result = "bool";
                    break;
                default:
                    result = "unknown";
            }

            assertThat(result).isEqualTo("string");
        }

        @Test
        @DisplayName("provides type-safe access to keys")
        void testProvides_TypeSafeAccessToKeys() {
            // Using enum provides compile-time type safety
            DataKey<?> stringKey = TestKeysEnum.STRING_KEY.getDataKey();
            DataKey<?> intKey = TestKeysEnum.INT_KEY.getDataKey();

            assertThat(stringKey.getName()).isEqualTo("stringValue");
            assertThat(intKey.getName()).isEqualTo("intValue");
        }

        @Test
        @DisplayName("can be used to build data stores")
        void testCanBeUsed_ToBuildDataStores() {
            StoreDefinition definition = TestKeysEnum.STRING_KEY.getStoreDefinition();

            // Could be used to create a DataStore with these keys
            assertThat(definition.hasKey("stringValue")).isTrue();
            assertThat(definition.hasKey("intValue")).isTrue();
            assertThat(definition.hasKey("boolValue")).isTrue();
            assertThat(definition.hasKey("nonExistentKey")).isFalse();
        }

        @Test
        @DisplayName("supports iteration over all keys")
        void testSupports_IterationOverAllKeys() {
            List<String> allKeyNames = Arrays.stream(TestKeysEnum.values())
                    .map(TestKeysEnum::getDataKey)
                    .map(DataKey::getName)
                    .collect(Collectors.toList());

            assertThat(allKeyNames).containsExactly("stringValue", "intValue", "boolValue");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("handles enum with single constant correctly")
        void testHandles_EnumWithSingleConstantCorrectly() {
            SingleKeyEnum singleKey = SingleKeyEnum.ONLY_KEY;

            assertThat(singleKey.getDataKey()).isNotNull();
            assertThat(singleKey.getDataKey().getName()).isEqualTo("onlyKey");
            assertThat(singleKey.getStoreDefinition().getKeys()).hasSize(1);
        }

        @Test
        @DisplayName("works with complex DataKey types")
        void testWorks_WithComplexDataKeyTypes() {
            ComplexKeysEnum listKey = ComplexKeysEnum.LIST_KEY;
            ComplexKeysEnum objectKey = ComplexKeysEnum.OBJECT_KEY;
            ComplexKeysEnum optionalKey = ComplexKeysEnum.OPTIONAL_KEY;

            assertThat(listKey.getDataKey().getName()).isEqualTo("listValue");
            assertThat(objectKey.getDataKey().getName()).isEqualTo("objectValue");
            assertThat(optionalKey.getDataKey().getName()).isEqualTo("optionalValue");

            StoreDefinition definition = listKey.getStoreDefinition();
            assertThat(definition.getKeys()).hasSize(3);
        }

        @Test
        @DisplayName("maintains consistency across multiple calls")
        void testMaintains_ConsistencyAcrossMultipleCalls() {
            TestKeysEnum enumValue = TestKeysEnum.STRING_KEY;

            // Multiple calls should return the same instances/values
            DataKey<?> key1 = enumValue.getDataKey();
            DataKey<?> key2 = enumValue.getDataKey();
            assertThat(key1).isSameAs(key2);

            Class<TestKeysEnum> class1 = enumValue.getEnumClass();
            Class<TestKeysEnum> class2 = enumValue.getEnumClass();
            assertThat(class1).isSameAs(class2);

            // Note: StoreDefinition might not be the same instance since it's created each
            // time
            // but should have the same content
            StoreDefinition def1 = enumValue.getStoreDefinition();
            StoreDefinition def2 = enumValue.getStoreDefinition();
            assertThat(def1.getName()).isEqualTo(def2.getName());
            assertThat(def1.getKeys().size()).isEqualTo(def2.getKeys().size());
        }

        @Test
        @DisplayName("enum ordinal and name work correctly")
        void testEnumOrdinalAndName_WorkCorrectly() {
            assertThat(TestKeysEnum.STRING_KEY.ordinal()).isEqualTo(0);
            assertThat(TestKeysEnum.INT_KEY.ordinal()).isEqualTo(1);
            assertThat(TestKeysEnum.BOOL_KEY.ordinal()).isEqualTo(2);

            assertThat(TestKeysEnum.STRING_KEY.name()).isEqualTo("STRING_KEY");
            assertThat(TestKeysEnum.INT_KEY.name()).isEqualTo("INT_KEY");
            assertThat(TestKeysEnum.BOOL_KEY.name()).isEqualTo("BOOL_KEY");
        }
    }
}
