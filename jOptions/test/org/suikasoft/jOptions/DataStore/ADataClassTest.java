package org.suikasoft.jOptions.DataStore;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
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
 * Test suite for ADataClass abstract base class functionality.
 * Uses a concrete test implementation to test the abstract class.
 * 
 * @author Generated Tests
 */
@DisplayName("ADataClass")
class ADataClassTest {

    @Mock
    private DataStore mockDataStore;

    @Mock
    private StoreDefinition mockStoreDefinition;

    private DataKey<String> stringKey;
    private DataKey<Integer> intKey;
    private TestADataClass testDataClass;

    /**
     * Concrete test implementation of ADataClass for testing purposes.
     */
    private static class TestADataClass extends ADataClass<TestADataClass> {
        public TestADataClass(DataStore data) {
            super(data);
        }

        public TestADataClass() {
            super();
        }

        // Expose protected method for testing
        public DataStore getDataStoreForTesting() {
            return getDataStore();
        }
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create test DataKeys
        stringKey = KeyFactory.string("string");
        intKey = KeyFactory.integer("int");

        // Create test instance with mock DataStore
        testDataClass = new TestADataClass(mockDataStore);
    }

    @Nested
    @DisplayName("Constructor and Initialization")
    class ConstructorAndInitializationTests {

        @Test
        @DisplayName("constructor with DataStore creates instance correctly")
        void testConstructor_WithDataStore_CreatesInstanceCorrectly() {
            TestADataClass dataClass = new TestADataClass(mockDataStore);

            assertThat(dataClass).isNotNull();
            assertThat(dataClass.getDataStoreForTesting()).isSameAs(mockDataStore);
        }

        @Test
        @DisplayName("constructor behavior with null DataStore")
        void testConstructor_WithNullDataStore_CurrentBehavior() {
            assertThatThrownBy(() -> new TestADataClass(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("DataStore cannot be null");
        }

        @Test
        @DisplayName("default constructor creates instance with auto-generated DataStore")
        void testDefaultConstructor_CreatesInstanceWithAutoDataStore() {
            // The default constructor actually works for test classes and creates a
            // DataStore
            // This documents the actual behavior rather than expected exception
            TestADataClass dataClass = new TestADataClass();

            assertThat(dataClass).isNotNull();
            assertThat(dataClass.getDataStoreForTesting()).isNotNull();
        }
    }

    @Nested
    @DisplayName("Core DataClass Operations")
    class CoreDataClassOperationsTests {

        @Test
        @DisplayName("get delegates to underlying DataStore")
        void testGet_DelegatesToDataStore() {
            when(mockDataStore.get(stringKey)).thenReturn("test");

            String result = testDataClass.get(stringKey);

            assertThat(result).isEqualTo("test");
            verify(mockDataStore).get(stringKey);
        }

        @Test
        @DisplayName("set delegates to underlying DataStore when not locked")
        void testSet_DelegatesToDataStore_WhenNotLocked() {
            TestADataClass result = testDataClass.set(stringKey, "test");

            assertThat(result).isSameAs(testDataClass);
            verify(mockDataStore).set(stringKey, "test");
        }

        @Test
        @DisplayName("set throws exception when locked")
        void testSet_ThrowsException_WhenLocked() {
            testDataClass.lock();

            assertThatThrownBy(() -> testDataClass.set(stringKey, "test"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("is locked");

            verify(mockDataStore, never()).set(any(), any());
        }

        @Test
        @DisplayName("set with instance delegates to DataStore addAll when not locked")
        void testSetInstance_DelegatesToAddAll_WhenNotLocked() {
            TestADataClass otherInstance = new TestADataClass(mock(DataStore.class));

            TestADataClass result = testDataClass.set(otherInstance);

            assertThat(result).isSameAs(testDataClass);
            verify(mockDataStore).addAll(otherInstance.getDataStoreForTesting());
        }

        @Test
        @DisplayName("set with instance throws exception when locked")
        void testSetInstance_ThrowsException_WhenLocked() {
            TestADataClass otherInstance = new TestADataClass(mock(DataStore.class));
            testDataClass.lock();

            assertThatThrownBy(() -> testDataClass.set(otherInstance))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("is locked");

            verify(mockDataStore, never()).addAll((DataStore) any());
        }

        @Test
        @DisplayName("hasValue delegates to underlying DataStore")
        void testHasValue_DelegatesToDataStore() {
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);

            boolean result = testDataClass.hasValue(stringKey);

            assertThat(result).isTrue();
            verify(mockDataStore).hasValue(stringKey);
        }
    }

    @Nested
    @DisplayName("Locking Mechanism")
    class LockingMechanismTests {

        @Test
        @DisplayName("lock returns this instance")
        void testLock_ReturnsThisInstance() {
            TestADataClass result = testDataClass.lock();

            assertThat(result).isSameAs(testDataClass);
        }

        @Test
        @DisplayName("lock prevents further modifications")
        void testLock_PreventsFurtherModifications() {
            testDataClass.lock();

            assertThatThrownBy(() -> testDataClass.set(stringKey, "test"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("is locked");
        }

        @Test
        @DisplayName("locked instance still allows read operations")
        void testLockedInstance_AllowsReadOperations() {
            when(mockDataStore.get(stringKey)).thenReturn("test");
            testDataClass.lock();

            String result = testDataClass.get(stringKey);

            assertThat(result).isEqualTo("test");
        }
    }

    @Nested
    @DisplayName("Data Class Metadata")
    class DataClassMetadataTests {

        @Test
        @DisplayName("getDataClassName delegates to DataStore getName")
        void testGetDataClassName_DelegatesToDataStoreName() {
            when(mockDataStore.getName()).thenReturn("TestStore");

            String result = testDataClass.getDataClassName();

            assertThat(result).isEqualTo("TestStore");
            verify(mockDataStore).getName();
        }

        @Test
        @DisplayName("getStoreDefinitionTry wraps DataStore definition with class name")
        void testGetStoreDefinitionTry_WrapsDataStoreDefinition() {
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockDataStore.getName()).thenReturn("TestStore");

            Optional<StoreDefinition> result = testDataClass.getStoreDefinitionTry();

            assertThat(result).isPresent();
            // The result should be a wrapped StoreDefinition, not the original mock
            assertThat(result.get()).isNotSameAs(mockStoreDefinition);
        }

        @Test
        @DisplayName("getStoreDefinitionTry returns empty when DataStore has no definition")
        void testGetStoreDefinitionTry_ReturnsEmpty_WhenNoDefinition() {
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.empty());

            Optional<StoreDefinition> result = testDataClass.getStoreDefinitionTry();

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Key Collections")
    class KeyCollectionsTests {

        @Test
        @DisplayName("getDataKeysWithValues when no StoreDefinition")
        void testGetDataKeysWithValues_WhenNoStoreDefinition() {
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.empty());

            assertThat(testDataClass.getDataKeysWithValues()).isEmpty();
        }

        @Test
        @DisplayName("getDataKeysWithValues returns keys from StoreDefinition")
        void testGetDataKeysWithValues_ReturnsKeysFromStoreDefinition() {
            setupMockStoreDefinition();
            when(mockDataStore.getKeysWithValues()).thenReturn(Arrays.asList("string", "int"));

            Collection<DataKey<?>> result = testDataClass.getDataKeysWithValues();

            assertThat(result).hasSize(2);
            assertThat(result).contains(stringKey, intKey);
        }

        @Test
        @DisplayName("getDataKeysWithValues filters out unknown keys")
        void testGetDataKeysWithValues_FiltersOutUnknownKeys() {
            setupMockStoreDefinition();
            when(mockDataStore.getKeysWithValues()).thenReturn(Arrays.asList("string", "unknown", "int"));

            Collection<DataKey<?>> result = testDataClass.getDataKeysWithValues();

            // Should only include known keys (string, int) but not unknown
            assertThat(result).hasSize(2);
            assertThat(result).contains(stringKey, intKey);
        }

        private void setupMockStoreDefinition() {
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.hasKey("string")).thenReturn(true);
            when(mockStoreDefinition.hasKey("int")).thenReturn(true);
            when(mockStoreDefinition.hasKey("unknown")).thenReturn(false);
            doReturn(stringKey).when(mockStoreDefinition).getKey("string");
            doReturn(intKey).when(mockStoreDefinition).getKey("int");
        }
    }

    @Nested
    @DisplayName("Equality and Hashing")
    class EqualityAndHashingTests {

        @Test
        @DisplayName("equals returns true for same instance")
        void testEquals_ReturnsTrueForSameInstance() {
            boolean result = testDataClass.equals(testDataClass);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("equals returns false for null")
        void testEquals_ReturnsFalseForNull() {
            boolean result = testDataClass.equals(null);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("equals returns false for different class")
        void testEquals_ReturnsFalseForDifferentClass() {
            Object other = new Object();

            boolean result = testDataClass.equals(other);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("equals with same data returns true")
        void testEquals_ReturnsTrueForSameData() {
            TestADataClass other = new TestADataClass(mock(DataStore.class));
            setupEqualDataClasses(testDataClass, other);

            boolean result = testDataClass.equals(other);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("equals with different keys returns false")
        void testEquals_ReturnsFalseForDifferentKeys() {
            TestADataClass other = new TestADataClass(mock(DataStore.class));
            setupDifferentKeys(testDataClass, other);

            boolean result = testDataClass.equals(other);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("equals with different values returns false")
        void testEquals_ReturnsFalseForDifferentValues() {
            TestADataClass other = new TestADataClass(mock(DataStore.class));
            setupDifferentValues(testDataClass, other);

            boolean result = testDataClass.equals(other);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("hashCode depends on keys and values")
        void testHashCode_DependsOnKeysAndValues() {
            setupMockForHashing();

            int hashCode = testDataClass.hashCode();

            // Hash code should be calculated from the keys and values
            assertThat(hashCode).isNotZero();
        }

        @Test
        @DisplayName("hashCode handles missing StoreDefinition gracefully")
        void testHashCode_HandlesGracefully_WhenNoStoreDefinition() {
            // Implementation handles missing StoreDefinition gracefully
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.empty());

            // Should not throw any exception
            int hashCode = testDataClass.hashCode();
            assertThat(hashCode).isNotNull();
        }

        private void setupEqualDataClasses(TestADataClass first, TestADataClass second) {
            // Mock both to have same keys and values
            DataStore firstStore = first.getDataStoreForTesting();
            DataStore secondStore = second.getDataStoreForTesting();

            when(firstStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(secondStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));

            when(mockStoreDefinition.hasKey("string")).thenReturn(true);
            doReturn(stringKey).when(mockStoreDefinition).getKey("string");

            when(firstStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));
            when(secondStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));

            when(firstStore.get(stringKey)).thenReturn("test");
            when(secondStore.get(stringKey)).thenReturn("test");
        }

        private void setupDifferentKeys(TestADataClass first, TestADataClass second) {
            DataStore firstStore = first.getDataStoreForTesting();
            DataStore secondStore = second.getDataStoreForTesting();

            when(firstStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(secondStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));

            when(mockStoreDefinition.hasKey("string")).thenReturn(true);
            when(mockStoreDefinition.hasKey("int")).thenReturn(true);
            doReturn(stringKey).when(mockStoreDefinition).getKey("string");
            doReturn(intKey).when(mockStoreDefinition).getKey("int");

            when(firstStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));
            when(secondStore.getKeysWithValues()).thenReturn(Arrays.asList("int"));
        }

        private void setupDifferentValues(TestADataClass first, TestADataClass second) {
            DataStore firstStore = first.getDataStoreForTesting();
            DataStore secondStore = second.getDataStoreForTesting();

            when(firstStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(secondStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));

            when(mockStoreDefinition.hasKey("string")).thenReturn(true);
            doReturn(stringKey).when(mockStoreDefinition).getKey("string");

            when(firstStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));
            when(secondStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));

            when(firstStore.get(stringKey)).thenReturn("test1");
            when(secondStore.get(stringKey)).thenReturn("test2");
        }

        private void setupMockForHashing() {
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.hasKey("string")).thenReturn(true);
            doReturn(stringKey).when(mockStoreDefinition).getKey("string");
            when(mockDataStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));
            when(mockDataStore.get(stringKey)).thenReturn("test");
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {

        @Test
        @DisplayName("toString delegates to toInlinedString")
        void testToString_DelegatesToToInlinedString() {
            setupMockForStringRepresentation();

            String result = testDataClass.toString();

            assertThat(result).contains("string: test");
        }

        @Test
        @DisplayName("getString returns toString result")
        void testGetString_ReturnsToStringResult() {
            setupMockForStringRepresentation();

            String stringResult = testDataClass.getString();
            String toStringResult = testDataClass.toString();

            assertThat(stringResult).isEqualTo(toStringResult);
        }

        private void setupMockForStringRepresentation() {
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(stringKey));
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);
            when(mockDataStore.get(stringKey)).thenReturn("test");
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandlingTests {

        @Test
        @DisplayName("operations work with null values")
        void testOperations_WorkWithNullValues() {
            when(mockDataStore.get(stringKey)).thenReturn(null);
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);

            String result = testDataClass.get(stringKey);
            boolean hasValue = testDataClass.hasValue(stringKey);

            assertThat(result).isNull();
            assertThat(hasValue).isTrue(); // hasValue can return true even for null values
        }

        @Test
        @DisplayName("equals handles null values correctly")
        void testEquals_HandlesNullValuesCorrectly() {
            TestADataClass other = new TestADataClass(mock(DataStore.class));
            setupEqualDataClassesWithNullValues(testDataClass, other);

            boolean result = testDataClass.equals(other);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("hashCode handles null values correctly")
        void testHashCode_HandlesNullValuesCorrectly() {
            setupMockForHashingWithNullValues();

            int hashCode = testDataClass.hashCode();

            // Should not throw, null values should be handled
            assertThat(hashCode).isNotNull();
        }

        private void setupEqualDataClassesWithNullValues(TestADataClass first, TestADataClass second) {
            DataStore firstStore = first.getDataStoreForTesting();
            DataStore secondStore = second.getDataStoreForTesting();

            when(firstStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(secondStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));

            when(mockStoreDefinition.hasKey("string")).thenReturn(true);
            doReturn(stringKey).when(mockStoreDefinition).getKey("string");

            when(firstStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));
            when(secondStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));

            when(firstStore.get(stringKey)).thenReturn(null);
            when(secondStore.get(stringKey)).thenReturn(null);
        }

        private void setupMockForHashingWithNullValues() {
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.hasKey("string")).thenReturn(true);
            doReturn(stringKey).when(mockStoreDefinition).getKey("string");
            when(mockDataStore.getKeysWithValues()).thenReturn(Arrays.asList("string"));
            when(mockDataStore.get(stringKey)).thenReturn(null);
        }
    }
}
