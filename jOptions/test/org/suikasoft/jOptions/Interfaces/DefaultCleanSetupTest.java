package org.suikasoft.jOptions.Interfaces;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link DefaultCleanSetup} class.
 * 
 * Tests the default implementation of DataView that wraps a DataStore,
 * providing a clean interface for data access and implementing both
 * DataView and DataStoreContainer interfaces.
 * 
 * @author Generated Tests
 */
@DisplayName("DefaultCleanSetup Tests")
class DefaultCleanSetupTest {

    @Mock
    private DataStore mockDataStore;

    private DefaultCleanSetup setup;
    private DataKey<String> stringKey;
    private DataKey<Integer> intKey;
    private DataKey<Boolean> boolKey;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setup = new DefaultCleanSetup(mockDataStore);
        stringKey = KeyFactory.string("test.string", "default");
        intKey = KeyFactory.integer("test.int", 42);
        boolKey = KeyFactory.bool("test.bool");
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Constructor with valid DataStore succeeds")
        void testConstructorWithValidDataStore() {
            DataStore dataStore = mock(DataStore.class);
            DefaultCleanSetup newSetup = new DefaultCleanSetup(dataStore);

            assertThat(newSetup).isNotNull();
            assertThat(newSetup.getDataStore()).isSameAs(dataStore);
        }

        @Test
        @DisplayName("Constructor with null DataStore throws exception")
        void testConstructorWithNullDataStore() {
            assertThatThrownBy(() -> new DefaultCleanSetup(null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessage("DataStore cannot be null");
        }
    }

    @Nested
    @DisplayName("DataView Interface Tests")
    class DataViewInterfaceTests {

        @Test
        @DisplayName("getName delegates to underlying DataStore")
        void testGetName() {
            String expectedName = "TestDataStore";
            when(mockDataStore.getName()).thenReturn(expectedName);

            String actualName = setup.getName();

            assertThat(actualName).isEqualTo(expectedName);
            verify(mockDataStore).getName();
        }

        @Test
        @DisplayName("getValue delegates to underlying DataStore")
        void testGetValue() {
            String expectedValue = "test value";
            when(mockDataStore.get(stringKey)).thenReturn(expectedValue);

            String actualValue = setup.getValue(stringKey);

            assertThat(actualValue).isEqualTo(expectedValue);
            verify(mockDataStore).get(stringKey);
        }

        @Test
        @DisplayName("getValue with different types")
        void testGetValueWithDifferentTypes() {
            when(mockDataStore.get(stringKey)).thenReturn("string value");
            when(mockDataStore.get(intKey)).thenReturn(123);
            when(mockDataStore.get(boolKey)).thenReturn(true);

            assertThat(setup.getValue(stringKey)).isEqualTo("string value");
            assertThat(setup.getValue(intKey)).isEqualTo(123);
            assertThat(setup.getValue(boolKey)).isEqualTo(true);

            verify(mockDataStore).get(stringKey);
            verify(mockDataStore).get(intKey);
            verify(mockDataStore).get(boolKey);
        }

        @Test
        @DisplayName("hasValue delegates to underlying DataStore")
        void testHasValue() {
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);
            when(mockDataStore.hasValue(intKey)).thenReturn(false);

            assertThat(setup.hasValue(stringKey)).isTrue();
            assertThat(setup.hasValue(intKey)).isFalse();

            verify(mockDataStore).hasValue(stringKey);
            verify(mockDataStore).hasValue(intKey);
        }

        @Test
        @DisplayName("getValueRaw delegates to underlying DataStore")
        void testGetValueRaw() {
            Object expectedValue = "raw value";
            String keyId = "test.key";
            when(mockDataStore.get(keyId)).thenReturn(expectedValue);

            Object actualValue = setup.getValueRaw(keyId);

            assertThat(actualValue).isEqualTo(expectedValue);
            verify(mockDataStore).get(keyId);
        }

        @Test
        @DisplayName("getDataKeysWithValues delegates to underlying DataStore")
        void testGetDataKeysWithValues() {
            Collection<DataKey<?>> expectedKeys = Arrays.asList(stringKey, intKey);
            when(mockDataStore.getDataKeysWithValues()).thenReturn(expectedKeys);

            Collection<DataKey<?>> actualKeys = setup.getDataKeysWithValues();

            assertThat(actualKeys).isEqualTo(expectedKeys);
            verify(mockDataStore).getDataKeysWithValues();
        }

        @Test
        @DisplayName("getKeysWithValues delegates to underlying DataStore")
        void testGetKeysWithValues() {
            Collection<String> expectedKeys = Arrays.asList("key1", "key2", "key3");
            when(mockDataStore.getKeysWithValues()).thenReturn(expectedKeys);

            Collection<String> actualKeys = setup.getKeysWithValues();

            assertThat(actualKeys).isEqualTo(expectedKeys);
            verify(mockDataStore).getKeysWithValues();
        }
    }

    @Nested
    @DisplayName("DataStoreContainer Interface Tests")
    class DataStoreContainerInterfaceTests {

        @Test
        @DisplayName("getDataStore returns the underlying DataStore")
        void testGetDataStore() {
            DataStore dataStore = setup.getDataStore();

            assertThat(dataStore).isSameAs(mockDataStore);
        }

        @Test
        @DisplayName("getDataStore returns same instance on multiple calls")
        void testGetDataStoreConsistency() {
            DataStore dataStore1 = setup.getDataStore();
            DataStore dataStore2 = setup.getDataStore();

            assertThat(dataStore1).isSameAs(dataStore2);
            assertThat(dataStore1).isSameAs(mockDataStore);
        }
    }

    @Nested
    @DisplayName("Object Methods Tests")
    class ObjectMethodsTests {

        @Test
        @DisplayName("toString delegates to underlying DataStore")
        void testToString() {
            String expectedString = "DataStore[name=test, keys=5]";
            when(mockDataStore.toString()).thenReturn(expectedString);

            String actualString = setup.toString();

            assertThat(actualString).isEqualTo(expectedString);
            // Note: Cannot verify toString() calls due to Mockito limitations
        }

        @Test
        @DisplayName("toString handles empty DataStore")
        void testToStringEmpty() {
            when(mockDataStore.toString()).thenReturn("EmptyDataStore[]");

            String actualString = setup.toString();

            assertThat(actualString).isEqualTo("EmptyDataStore[]");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Handles null return values from DataStore")
        void testHandlesNullReturnValues() {
            when(mockDataStore.get(stringKey)).thenReturn(null);
            when(mockDataStore.getName()).thenReturn(null);
            when(mockDataStore.get("nonexistent")).thenReturn(null);

            assertThat(setup.getValue(stringKey)).isNull();
            assertThat(setup.getName()).isNull();
            assertThat(setup.getValueRaw("nonexistent")).isNull();
        }

        @Test
        @DisplayName("Handles empty collections from DataStore")
        void testHandlesEmptyCollections() {
            when(mockDataStore.getDataKeysWithValues()).thenReturn(Collections.emptyList());
            when(mockDataStore.getKeysWithValues()).thenReturn(Collections.emptySet());

            assertThat(setup.getDataKeysWithValues()).isEmpty();
            assertThat(setup.getKeysWithValues()).isEmpty();
        }

        @Test
        @DisplayName("Multiple operations on same instance")
        void testMultipleOperations() {
            when(mockDataStore.getName()).thenReturn("TestStore");
            when(mockDataStore.get(stringKey)).thenReturn("value1");
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);
            when(mockDataStore.get("raw.key")).thenReturn("raw value");

            // Perform multiple operations
            assertThat(setup.getName()).isEqualTo("TestStore");
            assertThat(setup.getValue(stringKey)).isEqualTo("value1");
            assertThat(setup.hasValue(stringKey)).isTrue();
            assertThat(setup.getValueRaw("raw.key")).isEqualTo("raw value");
            assertThat(setup.getDataStore()).isSameAs(mockDataStore);

            // Verify all delegations occurred
            verify(mockDataStore).getName();
            verify(mockDataStore).get(stringKey);
            verify(mockDataStore).hasValue(stringKey);
            verify(mockDataStore).get("raw.key");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Propagates exceptions from DataStore.get(DataKey)")
        void testPropagatesGetKeyException() {
            RuntimeException expectedException = new RuntimeException("DataStore error");
            when(mockDataStore.get(stringKey)).thenThrow(expectedException);

            assertThatThrownBy(() -> setup.getValue(stringKey))
                    .isSameAs(expectedException);
        }

        @Test
        @DisplayName("Propagates exceptions from DataStore.get(String)")
        void testPropagatesGetStringException() {
            RuntimeException expectedException = new RuntimeException("DataStore error");
            when(mockDataStore.get("test.key")).thenThrow(expectedException);

            assertThatThrownBy(() -> setup.getValueRaw("test.key"))
                    .isSameAs(expectedException);
        }

        @Test
        @DisplayName("Propagates exceptions from DataStore.hasValue")
        void testPropagatesHasValueException() {
            RuntimeException expectedException = new RuntimeException("DataStore error");
            when(mockDataStore.hasValue(stringKey)).thenThrow(expectedException);

            assertThatThrownBy(() -> setup.hasValue(stringKey))
                    .isSameAs(expectedException);
        }

        @Test
        @DisplayName("Propagates exceptions from DataStore.getName")
        void testPropagatesGetNameException() {
            RuntimeException expectedException = new RuntimeException("DataStore error");
            when(mockDataStore.getName()).thenThrow(expectedException);

            assertThatThrownBy(() -> setup.getName())
                    .isSameAs(expectedException);
        }

        @Test
        @DisplayName("Propagates exceptions from DataStore.getDataKeysWithValues")
        void testPropagatesGetDataKeysWithValuesException() {
            RuntimeException expectedException = new RuntimeException("DataStore error");
            when(mockDataStore.getDataKeysWithValues()).thenThrow(expectedException);

            assertThatThrownBy(() -> setup.getDataKeysWithValues())
                    .isSameAs(expectedException);
        }

        @Test
        @DisplayName("Propagates exceptions from DataStore.getKeysWithValues")
        void testPropagatesGetKeysWithValuesException() {
            RuntimeException expectedException = new RuntimeException("DataStore error");
            when(mockDataStore.getKeysWithValues()).thenThrow(expectedException);

            assertThatThrownBy(() -> setup.getKeysWithValues())
                    .isSameAs(expectedException);
        }

        @Test
        @DisplayName("Propagates exceptions from DataStore.toString")
        void testPropagesToStringException() {
            RuntimeException expectedException = new RuntimeException("DataStore error");
            when(mockDataStore.toString()).thenThrow(expectedException);

            assertThatThrownBy(() -> setup.toString())
                    .isSameAs(expectedException);
            // Note: Cannot verify toString() calls due to Mockito limitations
        }
    }

    @Nested
    @DisplayName("Type Safety Tests")
    class TypeSafetyTests {

        @Test
        @DisplayName("getValue preserves generic type information")
        void testGetValueTypePreservation() {
            when(mockDataStore.get(stringKey)).thenReturn("string value");
            when(mockDataStore.get(intKey)).thenReturn(42);
            when(mockDataStore.get(boolKey)).thenReturn(true);

            // Type information should be preserved
            String stringValue = setup.getValue(stringKey);
            Integer intValue = setup.getValue(intKey);
            Boolean boolValue = setup.getValue(boolKey);

            assertThat(stringValue).isInstanceOf(String.class);
            assertThat(intValue).isInstanceOf(Integer.class);
            assertThat(boolValue).isInstanceOf(Boolean.class);
        }

        @Test
        @DisplayName("hasValue works with different key types")
        void testHasValueWithDifferentTypes() {
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);
            when(mockDataStore.hasValue(intKey)).thenReturn(false);
            when(mockDataStore.hasValue(boolKey)).thenReturn(true);

            assertThat(setup.hasValue(stringKey)).isTrue();
            assertThat(setup.hasValue(intKey)).isFalse();
            assertThat(setup.hasValue(boolKey)).isTrue();
        }
    }
}
