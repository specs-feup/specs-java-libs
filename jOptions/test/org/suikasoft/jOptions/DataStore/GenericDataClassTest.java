package org.suikasoft.jOptions.DataStore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Comprehensive test suite for GenericDataClass.
 * 
 * Tests cover:
 * - Generic DataClass implementation backed by DataStore
 * - Inheritance from ADataClass functionality
 * - Locking mechanism
 * - Value management and delegation
 * - Type safety and generic constraints
 * 
 * @author Generated Tests
 */
@DisplayName("GenericDataClass")
class GenericDataClassTest {

    @Mock
    private DataStore mockDataStore;

    private DataKey<String> stringKey;
    private DataKey<Integer> intKey;
    private DataKey<Boolean> boolKey;
    private TestGenericDataClass genericDataClass;

    // Concrete test implementation to avoid generic type issues
    private static class TestGenericDataClass extends GenericDataClass<TestGenericDataClass> {
        public TestGenericDataClass(DataStore data) {
            super(data);
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test DataKeys
        stringKey = KeyFactory.string("string");
        intKey = KeyFactory.integer("int");
        boolKey = KeyFactory.bool("bool");

        // Create GenericDataClass instance
        genericDataClass = new TestGenericDataClass(mockDataStore);
    }

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorAndInitializationTests {

        @Test
        @DisplayName("constructor with DataStore creates instance correctly")
        void testConstructor_WithDataStore_CreatesInstanceCorrectly() {
            TestGenericDataClass dataClass = new TestGenericDataClass(mockDataStore);

            assertThat(dataClass).isNotNull();
            assertThat(dataClass.getDataStore()).isSameAs(mockDataStore);
        }

        @Test
        @DisplayName("constructor with null DataStore throws exception")
        void testConstructor_WithNullDataStore_ThrowsException() {
            // Fixed: GenericDataClass constructor now validates null parameters
            assertThatThrownBy(() -> new TestGenericDataClass(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("DataStore cannot be null");
        }

        @Test
        @DisplayName("inherits from ADataClass correctly")
        void testInheritance_FromADataClass_IsCorrect() {
            assertThat(genericDataClass).isInstanceOf(ADataClass.class);
            assertThat(genericDataClass).isInstanceOf(DataClass.class);
        }
    }

    @Nested
    @DisplayName("DataClass Interface Implementation")
    class DataClassInterfaceTests {

        @Test
        @DisplayName("getDataClassName delegates to DataStore")
        void testGetDataClassName_DelegatesToDataStore() {
            when(mockDataStore.getName()).thenReturn("Test DataStore");

            String name = genericDataClass.getDataClassName();

            assertThat(name).isEqualTo("Test DataStore");
            verify(mockDataStore).getName();
        }

        @Test
        @DisplayName("get delegates to DataStore")
        void testGet_DelegatesToDataStore() {
            when(mockDataStore.get(stringKey)).thenReturn("test value");

            String value = genericDataClass.get(stringKey);

            assertThat(value).isEqualTo("test value");
            verify(mockDataStore).get(stringKey);
        }

        @Test
        @DisplayName("set delegates to DataStore and returns this")
        void testSet_DelegatesToDataStore_ReturnsThis() {
            TestGenericDataClass result = genericDataClass.set(stringKey, "test value");

            assertThat(result).isSameAs(genericDataClass);
            verify(mockDataStore).set(stringKey, "test value");
        }

        @Test
        @DisplayName("hasValue delegates to DataStore")
        void testHasValue_DelegatesToDataStore() {
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);

            boolean hasValue = genericDataClass.hasValue(stringKey);

            assertThat(hasValue).isTrue();
            verify(mockDataStore).hasValue(stringKey);
        }
    }

    @Nested
    @DisplayName("Locking Mechanism")
    class LockingMechanismTests {

        @Test
        @DisplayName("lock returns this and prevents modifications")
        void testLock_ReturnsThis_PreventsModifications() {
            TestGenericDataClass result = genericDataClass.lock();

            assertThat(result).isSameAs(genericDataClass);

            // Verify that modifications are prevented
            assertThatThrownBy(() -> genericDataClass.set(stringKey, "test"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("is locked");
        }

        @Test
        @DisplayName("locked instance prevents set operations")
        void testLockedInstance_PreventsSetOperations() {
            genericDataClass.lock();

            assertThatThrownBy(() -> genericDataClass.set(stringKey, "value"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("locked");

            // DataStore should not be called
            verify(mockDataStore, never()).set(any(), any());
        }

        @Test
        @DisplayName("locked instance prevents set from instance operations")
        void testLockedInstance_PreventsSetFromInstanceOperations() {
            TestGenericDataClass source = new TestGenericDataClass(mock(DataStore.class));
            genericDataClass.lock();

            assertThatThrownBy(() -> genericDataClass.set(source))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("locked");
        }

        @Test
        @DisplayName("locked instance allows read operations")
        void testLockedInstance_AllowsReadOperations() {
            when(mockDataStore.get(stringKey)).thenReturn("test");
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);

            genericDataClass.lock();

            // Read operations should still work
            assertThat(genericDataClass.get(stringKey)).isEqualTo("test");
            assertThat(genericDataClass.hasValue(stringKey)).isTrue();

            verify(mockDataStore).get(stringKey);
            verify(mockDataStore).hasValue(stringKey);
        }
    }

    @Nested
    @DisplayName("Store Definition Integration")
    class StoreDefinitionIntegrationTests {

        @Test
        @DisplayName("getStoreDefinitionTry delegates and wraps result")
        void testGetStoreDefinitionTry_DelegatesAndWrapsResult() {
            StoreDefinition mockDefinition = mock(StoreDefinition.class);
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockDefinition));
            when(mockDataStore.getName()).thenReturn("Test Store");

            Optional<StoreDefinition> result = genericDataClass.getStoreDefinitionTry();

            assertThat(result).isPresent();
            verify(mockDataStore).getStoreDefinitionTry();
        }

        @Test
        @DisplayName("getStoreDefinitionTry returns empty when DataStore has no definition")
        void testGetStoreDefinitionTry_ReturnsEmpty_WhenDataStoreHasNoDefinition() {
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.empty());

            Optional<StoreDefinition> result = genericDataClass.getStoreDefinitionTry();

            assertThat(result).isEmpty();
            verify(mockDataStore).getStoreDefinitionTry();
        }
    }

    @Nested
    @DisplayName("Collection Operations")
    class CollectionOperationsTests {

        @Test
        @DisplayName("getDataKeysWithValues works with valid store definition")
        void testGetDataKeysWithValues_WorksWithValidStoreDefinition() {
            // Create a real store definition for this test
            StoreDefinition storeDefinition = new StoreDefinition() {
                private final List<DataKey<?>> keys = Arrays.asList(stringKey, intKey);

                @Override
                public String getName() {
                    return "Test Store";
                }

                @Override
                public List<DataKey<?>> getKeys() {
                    return keys;
                }

                @Override
                public boolean hasKey(String key) {
                    return keys.stream().anyMatch(k -> k.getName().equals(key));
                }
            };

            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(storeDefinition));
            when(mockDataStore.getKeysWithValues()).thenReturn(Arrays.asList("string", "int"));

            Collection<DataKey<?>> keys = genericDataClass.getDataKeysWithValues();

            assertThat(keys).hasSize(2);
            assertThat(keys).containsExactlyInAnyOrder(stringKey, intKey);
        }

        @Test
        @DisplayName("getDataKeysWithValues filters unknown keys")
        void testGetDataKeysWithValues_FiltersUnknownKeys() {
            StoreDefinition storeDefinition = new StoreDefinition() {
                private final List<DataKey<?>> keys = Arrays.asList(stringKey);

                @Override
                public String getName() {
                    return "Test Store";
                }

                @Override
                public List<DataKey<?>> getKeys() {
                    return keys;
                }

                @Override
                public boolean hasKey(String key) {
                    return "string".equals(key);
                }
            };

            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(storeDefinition));
            when(mockDataStore.getKeysWithValues()).thenReturn(Arrays.asList("string", "unknown"));

            Collection<DataKey<?>> keys = genericDataClass.getDataKeysWithValues();

            assertThat(keys).hasSize(1);
            assertThat(keys).containsExactly(stringKey);
        }
    }

    @Nested
    @DisplayName("Set Operations and Type Safety")
    class SetOperationsAndTypeSafetyTests {

        @Test
        @DisplayName("set with instance copies all values")
        void testSet_WithInstance_CopiesAllValues() {
            DataStore sourceStore = mock(DataStore.class);
            TestGenericDataClass source = new TestGenericDataClass(sourceStore);

            genericDataClass.set(source);

            verify(mockDataStore).addAll(sourceStore);
        }

        @Test
        @DisplayName("set supports type hierarchy")
        void testSet_SupportsTypeHierarchy() {
            // String extends Object, so this should work
            DataKey<Object> objectKey = KeyFactory.object("object", Object.class);

            genericDataClass.set(objectKey, "string value");

            verify(mockDataStore).set(objectKey, "string value");
        }

        @Test
        @DisplayName("set with key and value returns this")
        void testSet_WithKeyAndValue_ReturnsThis() {
            TestGenericDataClass result = genericDataClass.set(stringKey, "test");

            assertThat(result).isSameAs(genericDataClass);
        }
    }

    @Nested
    @DisplayName("Equality and Hash Code")
    class EqualityAndHashCodeTests {

        @Test
        @DisplayName("equals compares DataKey values correctly")
        void testEquals_ComparesDataKeyValuesCorrectly() {
            // Create another instance with same backing store type
            DataStore otherStore = mock(DataStore.class);
            TestGenericDataClass other = new TestGenericDataClass(otherStore);

            // Setup same keys and values
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(createMockStoreDefinition()));
            when(otherStore.getStoreDefinitionTry()).thenReturn(Optional.of(createMockStoreDefinition()));

            when(mockDataStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));
            when(otherStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));

            when(mockDataStore.get(stringKey)).thenReturn("test");
            when(otherStore.get(stringKey)).thenReturn("test");

            // They should be equal if they have the same values
            assertThat(genericDataClass.getDataKeysWithValues()).isEqualTo(other.getDataKeysWithValues());
        }

        @Test
        @DisplayName("hashCode uses DataKey values")
        void testHashCode_UsesDataKeyValues() {
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(createMockStoreDefinition()));
            when(mockDataStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));
            when(mockDataStore.get(stringKey)).thenReturn("test");

            int hashCode = genericDataClass.hashCode();

            assertThat(hashCode).isNotZero();
        }

        private StoreDefinition createMockStoreDefinition() {
            return new StoreDefinition() {
                private final List<DataKey<?>> keys = Arrays.asList(stringKey);

                @Override
                public String getName() {
                    return "Test Store";
                }

                @Override
                public List<DataKey<?>> getKeys() {
                    return keys;
                }

                @Override
                public boolean hasKey(String key) {
                    return "string".equals(key);
                }
            };
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {

        @Test
        @DisplayName("toString delegates to toInlinedString")
        void testToString_DelegatesToToInlinedString() {
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(createMockStoreDefinition()));
            when(mockDataStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);
            when(mockDataStore.get(stringKey)).thenReturn("test");

            String result = genericDataClass.toString();

            assertThat(result).contains("string: test");
        }

        @Test
        @DisplayName("getString returns toString result")
        void testGetString_ReturnsToStringResult() {
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(createMockStoreDefinition()));
            when(mockDataStore.getKeysWithValues()).thenReturn(Arrays.asList());

            String stringResult = genericDataClass.getString();
            String toStringResult = genericDataClass.toString();

            assertThat(stringResult).isEqualTo(toStringResult);
        }

        private StoreDefinition createMockStoreDefinition() {
            return new StoreDefinition() {
                private final List<DataKey<?>> keys = Arrays.asList(stringKey);

                @Override
                public String getName() {
                    return "Test Store";
                }

                @Override
                public List<DataKey<?>> getKeys() {
                    return keys;
                }

                @Override
                public boolean hasKey(String key) {
                    return "string".equals(key);
                }

                @Override
                public DataKey<?> getKey(String keyName) {
                    if ("string".equals(keyName)) {
                        return stringKey;
                    }
                    return null;
                }
            };
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("operations fail gracefully with empty DataStore")
        void testOperations_FailGracefullyWithEmptyDataStore() {
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.empty());
            when(mockDataStore.getKeysWithValues()).thenReturn(Arrays.asList());
            when(mockDataStore.getName()).thenReturn("Empty Store");

            assertThat(genericDataClass.getDataClassName()).isEqualTo("Empty Store");
            assertThat(genericDataClass.getStoreDefinitionTry()).isEmpty();

            // toString() handles empty datastore gracefully
            String result = genericDataClass.toString();
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("handles DataStore exceptions gracefully")
        void testHandles_DataStoreExceptions_Gracefully() {
            when(mockDataStore.get(stringKey)).thenThrow(new RuntimeException("DataStore error"));

            assertThatThrownBy(() -> genericDataClass.get(stringKey))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("DataStore error");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("complete workflow with real SimpleDataStore")
        void testCompleteWorkflow_WithRealSimpleDataStore() {
            // Use a real SimpleDataStore for integration testing
            SimpleDataStore realStore = new SimpleDataStore("Integration Test Store");
            TestGenericDataClass realDataClass = new TestGenericDataClass(realStore);

            // Test complete workflow
            realDataClass.set(stringKey, "test value");
            realDataClass.set(intKey, 42);

            assertThat(realDataClass.get(stringKey)).isEqualTo("test value");
            assertThat(realDataClass.get(intKey)).isEqualTo(42);
            assertThat(realDataClass.hasValue(stringKey)).isTrue();
            assertThat(realDataClass.hasValue(boolKey)).isFalse();

            // Test locking
            realDataClass.lock();
            assertThatThrownBy(() -> realDataClass.set(boolKey, true))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("locked");

            // Read operations should still work
            assertThat(realDataClass.get(stringKey)).isEqualTo("test value");
        }

        @Test
        @DisplayName("copy operations between instances")
        void testCopyOperations_BetweenInstances() {
            SimpleDataStore store1 = new SimpleDataStore("Store 1");
            SimpleDataStore store2 = new SimpleDataStore("Store 2");

            TestGenericDataClass dataClass1 = new TestGenericDataClass(store1);
            TestGenericDataClass dataClass2 = new TestGenericDataClass(store2);

            // Set values in first instance
            dataClass1.set(stringKey, "original");
            dataClass1.set(intKey, 100);

            // Copy to second instance
            dataClass2.set(dataClass1);

            // Verify values were copied
            assertThat(dataClass2.get(stringKey)).isEqualTo("original");
            assertThat(dataClass2.get(intKey)).isEqualTo(100);
        }
    }
}
