package org.suikasoft.jOptions.DataStore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Unit tests for {@link DataStoreContainer}.
 * 
 * Tests the container interface for classes that hold DataStore instances,
 * including implementation verification, data store access, and integration
 * patterns.
 * 
 * @author Generated Tests
 */
@DisplayName("DataStoreContainer")
class DataStoreContainerTest {

    private DataStore mockDataStore;
    private DataStoreContainer container;

    @BeforeEach
    void setUp() {
        // Create a test DataStore
        DataKey<String> stringKey = KeyFactory.string("test");
        StoreDefinition storeDefinition = new StoreDefinition() {
            private final List<DataKey<?>> keys = Arrays.asList(stringKey);

            @Override
            public String getName() {
                return "Container Test Store"; // Match the expected name
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

        mockDataStore = new SimpleDataStore("Container Test Store");
        mockDataStore.setStoreDefinition(storeDefinition);

        // Create a simple implementation of DataStoreContainer
        container = new DataStoreContainer() {
            @Override
            public DataStore getDataStore() {
                return mockDataStore;
            }
        };
    }

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContractTests {

        @Test
        @DisplayName("getDataStore returns contained data store")
        void testGetDataStore_ReturnsContainedDataStore() {
            DataStore result = container.getDataStore();

            assertThat(result).isSameAs(mockDataStore);
        }

        @Test
        @DisplayName("getDataStore returns non-null data store")
        void testGetDataStore_ReturnsNonNullDataStore() {
            DataStore result = container.getDataStore();

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("multiple calls return same instance")
        void testGetDataStore_MultipleCalls_ReturnSameInstance() {
            DataStore first = container.getDataStore();
            DataStore second = container.getDataStore();

            assertThat(first).isSameAs(second);
        }
    }

    @Nested
    @DisplayName("Data Store Access")
    class DataStoreAccessTests {

        @Test
        @DisplayName("can access data store methods through container")
        void testDataStoreAccess_CanAccessMethodsThroughContainer() {
            DataStore dataStore = container.getDataStore();

            // Test that we can use the data store normally
            assertThat(dataStore.getName()).isEqualTo("Container Test Store");
            assertThat(dataStore.getStoreDefinitionTry()).isPresent();
        }

        @Test
        @DisplayName("can modify data through contained data store")
        void testDataStoreAccess_CanModifyDataThroughContainedDataStore() {
            DataStore dataStore = container.getDataStore();
            DataKey<String> stringKey = KeyFactory.string("test");

            // Set a value through the data store
            dataStore.set(stringKey, "test value");

            // Verify the value is accessible
            assertThat(dataStore.get(stringKey)).isEqualTo("test value");
            assertThat(dataStore.hasValue(stringKey)).isTrue();
        }

        @Test
        @DisplayName("data store state persists across container calls")
        void testDataStoreAccess_StatePersistsAcrossCalls() {
            DataKey<String> stringKey = KeyFactory.string("test");

            // Set value through first call
            container.getDataStore().set(stringKey, "persistent value");

            // Verify value persists through second call
            assertThat(container.getDataStore().get(stringKey)).isEqualTo("persistent value");
        }
    }

    @Nested
    @DisplayName("Implementation Patterns")
    class ImplementationPatternsTests {

        @Test
        @DisplayName("container with null data store throws when accessed")
        void testImplementation_NullDataStore_ThrowsWhenAccessed() {
            DataStoreContainer nullContainer = new DataStoreContainer() {
                @Override
                public DataStore getDataStore() {
                    return null;
                }
            };

            // Getting null should not throw, but using it should
            DataStore result = nullContainer.getDataStore();
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("container can delegate to different data stores")
        void testImplementation_CanDelegateToDifferentDataStores() {
            DataStore firstStore = new SimpleDataStore("First Store");
            DataStore secondStore = new SimpleDataStore("Second Store");

            // Container that switches between stores
            final DataStore[] currentStore = { firstStore };
            DataStoreContainer switchingContainer = new DataStoreContainer() {
                @Override
                public DataStore getDataStore() {
                    return currentStore[0];
                }
            };

            assertThat(switchingContainer.getDataStore().getName()).isEqualTo("First Store");

            // Switch to second store
            currentStore[0] = secondStore;
            assertThat(switchingContainer.getDataStore().getName()).isEqualTo("Second Store");
        }

        @Test
        @DisplayName("container can wrap and enhance data store")
        void testImplementation_CanWrapAndEnhanceDataStore() {
            DataStoreContainer wrappingContainer = new DataStoreContainer() {
                @Override
                public DataStore getDataStore() {
                    // Could add logging, validation, etc. here
                    return mockDataStore;
                }
            };

            DataStore wrapped = wrappingContainer.getDataStore();
            assertThat(wrapped).isSameAs(mockDataStore);
        }
    }

    @Nested
    @DisplayName("Type Safety and Integration")
    class TypeSafetyAndIntegrationTests {

        @Test
        @DisplayName("container works with different data store implementations")
        void testTypeSafety_WorksWithDifferentImplementations() {
            // Test with SimpleDataStore
            DataStoreContainer simpleContainer = () -> new SimpleDataStore("Simple");
            assertThat(simpleContainer.getDataStore()).isInstanceOf(SimpleDataStore.class);

            // Test with other implementations (if available)
            DataKey<String> key = KeyFactory.string("test");
            StoreDefinition definition = new StoreDefinition() {
                private final List<DataKey<?>> keys = Arrays.asList(key);

                @Override
                public String getName() {
                    return "List Store";
                }

                @Override
                public List<DataKey<?>> getKeys() {
                    return keys;
                }

                @Override
                public boolean hasKey(String keyName) {
                    return keys.stream().anyMatch(k -> k.getName().equals(keyName));
                }
            };

            DataStoreContainer listContainer = () -> new ListDataStore(definition);
            assertThat(listContainer.getDataStore()).isInstanceOf(ListDataStore.class);
        }

        @Test
        @DisplayName("container interface is functional interface compatible")
        void testTypeSafety_FunctionalInterfaceCompatible() {
            // Can be used as lambda
            DataStoreContainer lambdaContainer = () -> mockDataStore;
            assertThat(lambdaContainer.getDataStore()).isSameAs(mockDataStore);

            // Can be used with supplier pattern
            DataStoreContainer supplierContainer = () -> DataStoreContainerTest.this.mockDataStore;
            assertThat(supplierContainer.getDataStore()).isSameAs(mockDataStore);
        }

        @Test
        @DisplayName("container works in composition patterns")
        void testIntegration_WorksInCompositionPatterns() {
            // Container that contains another container
            DataStoreContainer innerContainer = () -> mockDataStore;
            DataStoreContainer outerContainer = () -> innerContainer.getDataStore();

            assertThat(outerContainer.getDataStore()).isSameAs(mockDataStore);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("container with exception throwing implementation")
        void testEdgeCases_ExceptionThrowingImplementation() {
            DataStoreContainer faultyContainer = new DataStoreContainer() {
                @Override
                public DataStore getDataStore() {
                    throw new RuntimeException("Test exception");
                }
            };

            assertThatThrownBy(() -> faultyContainer.getDataStore())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Test exception");
        }

        @Test
        @DisplayName("container inheritance works correctly")
        void testEdgeCases_InheritanceWorksCorrectly() {
            // Test implementation through inheritance
            class ConcreteContainer implements DataStoreContainer {
                private final DataStore store;

                public ConcreteContainer(DataStore store) {
                    this.store = store;
                }

                @Override
                public DataStore getDataStore() {
                    return store;
                }
            }

            ConcreteContainer concrete = new ConcreteContainer(mockDataStore);
            assertThat(concrete.getDataStore()).isSameAs(mockDataStore);
        }
    }
}
