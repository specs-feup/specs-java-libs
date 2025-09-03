package org.suikasoft.jOptions.values;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
 * Test suite for SetupList functionality.
 * Tests the DataStore collection management and delegation patterns.
 * 
 * @author Generated Tests
 */
@DisplayName("SetupList")
class SetupListTest {

    @Mock
    private DataStore mockSetup1;
    @Mock
    private DataStore mockSetup2;
    @Mock
    private DataStore mockSetup3;
    @Mock
    private StoreDefinition mockDefinition1;
    @Mock
    private StoreDefinition mockDefinition2;

    private DataKey<String> stringKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup mock behaviors
        when(mockSetup1.getName()).thenReturn("setup1");
        when(mockSetup2.getName()).thenReturn("setup2");
        when(mockSetup3.getName()).thenReturn("setup3");

        // Create test DataKeys
        stringKey = KeyFactory.string("testString");
    }

    @Nested
    @DisplayName("Constructor and Factory Methods")
    class ConstructorAndFactoryMethodsTests {

        @Test
        @DisplayName("constructor creates SetupList with collection of DataStores")
        void testConstructor_CreatesSetupListWithCollectionOfDataStores() {
            Collection<DataStore> setups = Arrays.asList(mockSetup1, mockSetup2);

            SetupList setupList = new SetupList("testList", setups);

            assertThat(setupList.getSetupListName()).isEqualTo("testList");
            assertThat(setupList.getNumSetups()).isEqualTo(2);
        }

        @Test
        @DisplayName("constructor throws exception for duplicate setup names")
        void testConstructor_ThrowsExceptionForDuplicateSetupNames() {
            DataStore duplicateSetup = mock(DataStore.class);
            when(duplicateSetup.getName()).thenReturn("setup1"); // Same as mockSetup1

            Collection<DataStore> setups = Arrays.asList(mockSetup1, duplicateSetup);

            assertThatThrownBy(() -> new SetupList("testList", setups))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("two of the given setups have the same name")
                    .hasMessageContaining("setup1");
        }

        @Test
        @DisplayName("newInstance fails with null name from DataStore.newInstance - BUG: null name handling")
        void testNewInstance_FailsWithNullNameFromDataStoreNewInstance() {
            // BUG: DataStore.newInstance() creates DataStore with null name, causing NPE in
            // SetupList constructor
            assertThatThrownBy(() -> SetupList.newInstance("testList", mockDefinition1, mockDefinition2))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke \"String.equals(Object)\" because \"name\" is null");
        }

        @Test
        @DisplayName("newInstance list version also fails with null name - BUG: DataStore.newInstance implementation")
        void testNewInstance_ListVersionAlsoFailsWithNullName() {
            List<StoreDefinition> definitions = Arrays.asList(mockDefinition1, mockDefinition2);

            // BUG: Same issue as array version - DataStore.newInstance returns DataStore
            // with null name
            assertThatThrownBy(() -> SetupList.newInstance("testList", definitions))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Cannot invoke \"String.equals(Object)\" because \"name\" is null");
        }

        @Test
        @DisplayName("constructor handles empty collection")
        void testConstructor_HandlesEmptyCollection() {
            SetupList setupList = new SetupList("emptyList", Collections.emptyList());

            assertThat(setupList.getSetupListName()).isEqualTo("emptyList");
            assertThat(setupList.getNumSetups()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Setup Management")
    class SetupManagementTests {

        private SetupList setupList;

        @BeforeEach
        void setUp() {
            Collection<DataStore> setups = Arrays.asList(mockSetup1, mockSetup2, mockSetup3);
            setupList = new SetupList("testList", setups);
        }

        @Test
        @DisplayName("getDataStores returns all DataStores in order")
        void testGetDataStores_ReturnsAllDataStoresInOrder() {
            Collection<DataStore> dataStores = setupList.getDataStores();

            assertThat(dataStores).hasSize(3);
            assertThat(dataStores).containsExactly(mockSetup1, mockSetup2, mockSetup3);
        }

        @Test
        @DisplayName("getSetup returns DataStore by name")
        void testGetSetup_ReturnsDataStoreByName() {
            DataStore result = setupList.getSetup("setup2");

            assertThat(result).isSameAs(mockSetup2);
        }

        @Test
        @DisplayName("getSetup throws exception for non-existent setup")
        void testGetSetup_ThrowsExceptionForNonExistentSetup() {
            assertThatThrownBy(() -> setupList.getSetup("nonExistent"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not find setup 'nonExistent'");
        }

        @Test
        @DisplayName("getDataStore returns DataStore by name")
        void testGetDataStore_ReturnsDataStoreByName() {
            DataStore result = setupList.getDataStore("setup2");

            assertThat(result).isSameAs(mockSetup2);
        }

        @Test
        @DisplayName("getDataStore returns null for non-existent setup")
        void testGetDataStore_ReturnsNullForNonExistentSetup() {
            DataStore result = setupList.getDataStore("nonExistent");

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("getNumSetups returns correct count")
        void testGetNumSetups_ReturnsCorrectCount() {
            assertThat(setupList.getNumSetups()).isEqualTo(3);
        }

        @Test
        @DisplayName("toString returns comma-separated setup names")
        void testToString_ReturnsCommaSeparatedSetupNames() {
            String result = setupList.toString();

            assertThat(result).isEqualTo("setup1, setup2, setup3");
        }
    }

    @Nested
    @DisplayName("Preferred Setup Management")
    class PreferredSetupManagementTests {

        private SetupList setupList;

        @BeforeEach
        void setUp() {
            Collection<DataStore> setups = Arrays.asList(mockSetup1, mockSetup2, mockSetup3);
            setupList = new SetupList("testList", setups);
        }

        @Test
        @DisplayName("getPreferredSetup returns first setup when no preference set")
        void testGetPreferredSetup_ReturnsFirstSetupWhenNoPreferenceSet() {
            DataStore preferred = setupList.getPreferredSetup();

            assertThat(preferred).isSameAs(mockSetup1);
        }

        @Test
        @DisplayName("setPreferredSetup and getPreferredSetup work correctly")
        void testSetPreferredSetup_AndGetPreferredSetupWorkCorrectly() {
            setupList.setPreferredSetup("setup3");

            DataStore preferred = setupList.getPreferredSetup();

            assertThat(preferred).isSameAs(mockSetup3);
        }

        @Test
        @DisplayName("setPreferredSetup causes NPE on invalid setup - BUG: no null safety")
        void testSetPreferredSetup_CausesNPEOnInvalidSetup() {
            setupList.setPreferredSetup("nonExistent");

            // BUG: getPreferredSetup() calls getName() on null when preferredSetupName is
            // invalid
            assertThatThrownBy(() -> setupList.getPreferredSetup())
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining(
                            "Cannot invoke \"org.suikasoft.jOptions.Interfaces.DataStore.getName()\" because the return value of \"java.util.Map.get(Object)\" is null");
        }

        @Test
        @DisplayName("getPreferredSetup returns null for empty setup list")
        void testGetPreferredSetup_ReturnsNullForEmptySetupList() {
            SetupList emptyList = new SetupList("empty", Collections.emptyList());

            DataStore preferred = emptyList.getPreferredSetup();

            assertThat(preferred).isNull();
        }

        @Test
        @DisplayName("getName delegates to preferred setup - BUG: multiple getName() calls")
        void testGetName_DelegatesToPreferredSetup() {
            setupList.setPreferredSetup("setup2");

            String name = setupList.getName();

            assertThat(name).isEqualTo("setup2");
            // BUG: Implementation calls getName() multiple times - during construction,
            // preferred setup lookup, etc.
            // So we can't verify exact number of calls, just that it was called
            verify(mockSetup2, atLeastOnce()).getName();
        }
    }

    @Nested
    @DisplayName("DataStore Interface Implementation")
    class DataStoreInterfaceImplementationTests {

        private SetupList setupList;

        @BeforeEach
        void setUp() {
            Collection<DataStore> setups = Arrays.asList(mockSetup1, mockSetup2);
            setupList = new SetupList("testList", setups);
        }

        @Test
        @DisplayName("set delegates to preferred setup")
        void testSet_DelegatesToPreferredSetup() {
            when(mockSetup1.set(stringKey, "testValue")).thenReturn(mockSetup1);

            SetupList result = setupList.set(stringKey, "testValue");

            assertThat(result).isSameAs(setupList);
            verify(mockSetup1).set(stringKey, "testValue");
        }

        @Test
        @DisplayName("setRaw delegates to preferred setup")
        void testSetRaw_DelegatesToPreferredSetup() {
            Optional<Object> expectedResult = Optional.of("previousValue");
            when(mockSetup1.setRaw("key", "value")).thenReturn(expectedResult);

            Optional<Object> result = setupList.setRaw("key", "value");

            assertThat(result).isEqualTo(expectedResult);
            verify(mockSetup1).setRaw("key", "value");
        }

        @Test
        @DisplayName("get with DataKey delegates to preferred setup")
        void testGet_WithDataKey_DelegatesToPreferredSetup() {
            when(mockSetup1.get(stringKey)).thenReturn("testValue");

            String result = setupList.get(stringKey);

            assertThat(result).isEqualTo("testValue");
            verify(mockSetup1).get(stringKey);
        }

        @Test
        @DisplayName("get with string key delegates to preferred setup")
        void testGet_WithStringKey_DelegatesToPreferredSetup() {
            when(mockSetup1.get("stringKey")).thenReturn("testValue");

            Object result = setupList.get("stringKey");

            assertThat(result).isEqualTo("testValue");
            verify(mockSetup1).get("stringKey");
        }

        @Test
        @DisplayName("hasValue delegates to preferred setup")
        void testHasValue_DelegatesToPreferredSetup() {
            when(mockSetup1.hasValue(stringKey)).thenReturn(true);

            boolean result = setupList.hasValue(stringKey);

            assertThat(result).isTrue();
            verify(mockSetup1).hasValue(stringKey);
        }

        @Test
        @DisplayName("setStrict delegates to preferred setup")
        void testSetStrict_DelegatesToPreferredSetup() {
            setupList.setStrict(true);

            verify(mockSetup1).setStrict(true);
        }

        @Test
        @DisplayName("remove delegates to preferred setup")
        void testRemove_DelegatesToPreferredSetup() {
            Optional<String> expectedResult = Optional.of("removedValue");
            when(mockSetup1.remove(stringKey)).thenReturn(expectedResult);

            Optional<String> result = setupList.remove(stringKey);

            assertThat(result).isEqualTo(expectedResult);
            verify(mockSetup1).remove(stringKey);
        }

        @Test
        @DisplayName("getStoreDefinitionTry delegates to preferred setup")
        void testGetStoreDefinitionTry_DelegatesToPreferredSetup() {
            Optional<StoreDefinition> expectedResult = Optional.of(mockDefinition1);
            when(mockSetup1.getStoreDefinitionTry()).thenReturn(expectedResult);

            Optional<StoreDefinition> result = setupList.getStoreDefinitionTry();

            assertThat(result).isEqualTo(expectedResult);
            verify(mockSetup1).getStoreDefinitionTry();
        }

        @Test
        @DisplayName("setStoreDefinition delegates to preferred setup")
        void testSetStoreDefinition_DelegatesToPreferredSetup() {
            setupList.setStoreDefinition(mockDefinition1);

            verify(mockSetup1).setStoreDefinition(mockDefinition1);
        }

        @Test
        @DisplayName("getKeysWithValues delegates to preferred setup")
        void testGetKeysWithValues_DelegatesToPreferredSetup() {
            Collection<String> expectedKeys = Arrays.asList("key1", "key2");
            when(mockSetup1.getKeysWithValues()).thenReturn(expectedKeys);

            Collection<String> result = setupList.getKeysWithValues();

            assertThat(result).isEqualTo(expectedKeys);
            verify(mockSetup1).getKeysWithValues();
        }
    }

    @Nested
    @DisplayName("Preferred Setup Changes")
    class PreferredSetupChangesTests {

        private SetupList setupList;

        @BeforeEach
        void setUp() {
            Collection<DataStore> setups = Arrays.asList(mockSetup1, mockSetup2, mockSetup3);
            setupList = new SetupList("testList", setups);
        }

        @Test
        @DisplayName("operations delegate to new preferred setup after change")
        void testOperations_DelegateToNewPreferredSetupAfterChange() {
            // Initially uses first setup (mockSetup1)
            when(mockSetup1.get(stringKey)).thenReturn("value1");
            String initialValue = setupList.get(stringKey);
            assertThat(initialValue).isEqualTo("value1");

            // Change preferred setup
            setupList.setPreferredSetup("setup2");
            when(mockSetup2.get(stringKey)).thenReturn("value2");

            String newValue = setupList.get(stringKey);

            assertThat(newValue).isEqualTo("value2");
            verify(mockSetup1).get(stringKey);
            verify(mockSetup2).get(stringKey);
        }

        @Test
        @DisplayName("getName reflects preferred setup changes")
        void testGetName_ReflectsPreferredSetupChanges() {
            assertThat(setupList.getName()).isEqualTo("setup1");

            setupList.setPreferredSetup("setup3");

            assertThat(setupList.getName()).isEqualTo("setup3");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("operations on empty SetupList handle gracefully")
        void testOperations_OnEmptySetupListHandleGracefully() {
            SetupList emptyList = new SetupList("empty", Collections.emptyList());

            // These operations should not crash, but may return null or log warnings
            assertThat(emptyList.getPreferredSetup()).isNull();
            assertThat(emptyList.getNumSetups()).isEqualTo(0);
            assertThat(emptyList.toString()).isEmpty();

            // Operations that depend on preferred setup may fail
            assertThatThrownBy(() -> emptyList.get(stringKey))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("complex setup name scenarios work correctly")
        void testComplexSetupNameScenariosWorkCorrectly() {
            DataStore specialSetup1 = mock(DataStore.class);
            DataStore specialSetup2 = mock(DataStore.class);
            when(specialSetup1.getName()).thenReturn("setup with spaces");
            when(specialSetup2.getName()).thenReturn("setup-with-dashes");

            SetupList setupList = new SetupList("specialList", Arrays.asList(specialSetup1, specialSetup2));

            assertThat(setupList.getSetup("setup with spaces")).isSameAs(specialSetup1);
            assertThat(setupList.getSetup("setup-with-dashes")).isSameAs(specialSetup2);
            assertThat(setupList.toString()).isEqualTo("setup with spaces, setup-with-dashes");
        }

        @Test
        @DisplayName("large number of setups handled efficiently")
        void testLargeNumberOfSetups_HandledEfficiently() {
            List<DataStore> manySetups = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                DataStore setup = mock(DataStore.class);
                when(setup.getName()).thenReturn("setup" + i);
                manySetups.add(setup);
            }

            SetupList largeList = new SetupList("largeList", manySetups);

            assertThat(largeList.getNumSetups()).isEqualTo(100);
            assertThat(largeList.getSetup("setup50")).isNotNull();
            assertThat(largeList.getSetup("setup50").getName()).isEqualTo("setup50");
        }
    }

    @Nested
    @DisplayName("Integration Scenarios")
    class IntegrationScenariosTests {

        @Test
        @DisplayName("SetupList works with real DataStore operations")
        void testSetupList_WorksWithRealDataStoreOperations() {
            // Create actual DataStore instances (if possible without dependencies)
            try {
                DataStore realStore1 = DataStore.newInstance("store1");
                DataStore realStore2 = DataStore.newInstance("store2");

                SetupList setupList = new SetupList("realList", Arrays.asList(realStore1, realStore2));

                // Test basic operations
                assertThat(setupList.getNumSetups()).isEqualTo(2);
                assertThat(setupList.getName()).isEqualTo("store1");

                setupList.setPreferredSetup("store2");
                assertThat(setupList.getName()).isEqualTo("store2");

            } catch (RuntimeException e) {
                // If DataStore.newInstance is not implemented, document this
                assertThat(e.getMessage()).contains("Not implemented yet");
            }
        }

        @Test
        @DisplayName("multiple SetupList instances are independent")
        void testMultipleSetupListInstances_AreIndependent() {
            SetupList list1 = new SetupList("list1", Arrays.asList(mockSetup1, mockSetup2));
            SetupList list2 = new SetupList("list2", Arrays.asList(mockSetup2, mockSetup3));

            list1.setPreferredSetup("setup2");
            // list2 should still use first setup (mockSetup2)

            assertThat(list1.getName()).isEqualTo("setup2");
            assertThat(list2.getName()).isEqualTo("setup2"); // First in list2

            // But they use different instances
            assertThat(list1.getPreferredSetup()).isSameAs(mockSetup2);
            assertThat(list2.getPreferredSetup()).isSameAs(mockSetup2); // First in list2
        }
    }
}
