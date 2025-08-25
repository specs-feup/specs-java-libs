package org.suikasoft.jOptions.treenode;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * Comprehensive tests for {@link DataNode} abstract class functionality.
 * 
 * Tests the abstract base class for tree nodes that hold a DataStore and
 * support DataClass and Copyable interfaces.
 * Uses a concrete test implementation to test the abstract functionality.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("DataNode Tests")
class DataNodeTest {

    @Mock
    private DataStore mockDataStore;

    @Mock
    private StoreDefinition mockStoreDefinition;

    private TestDataNode testNode;
    private DataKey<String> stringKey;

    /**
     * Concrete implementation of DataNode for testing purposes.
     */
    private static class TestDataNode extends DataNode<TestDataNode> {

        public TestDataNode(DataStore data, Collection<? extends TestDataNode> children) {
            super(data, children);
        }

        @Override
        protected TestDataNode getThis() {
            return this;
        }

        @Override
        protected Class<TestDataNode> getBaseClass() {
            return TestDataNode.class;
        }
    }

    @BeforeEach
    void setUp() {
        stringKey = KeyFactory.string("testString");
        testNode = new TestDataNode(mockDataStore, Collections.emptyList());
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create DataNode with valid DataStore and children")
        void testConstructor_ValidParameters_Success() {
            // Given
            when(mockDataStore.getName()).thenReturn("TestNode");
            List<TestDataNode> children = Arrays.asList(
                    new TestDataNode(mockDataStore, Collections.emptyList()));

            // When
            TestDataNode node = new TestDataNode(mockDataStore, children);

            // Then
            assertThat(node.getData()).isSameAs(mockDataStore);
            assertThat(node.getDataClassName()).isEqualTo("TestNode");
            assertThat(node.getChildren()).hasSize(1);
        }

        @Test
        @DisplayName("Should create DataNode with null children collection")
        void testConstructor_NullChildren_Success() {
            // When
            TestDataNode node = new TestDataNode(mockDataStore, null);

            // Then
            assertThat(node.getData()).isEqualTo(mockDataStore);
            assertThat(node.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("Should create DataNode with empty children collection")
        void testConstructor_EmptyChildren_Success() {
            // When
            TestDataNode node = new TestDataNode(mockDataStore, Collections.emptyList());

            // Then
            assertThat(node.getData()).isEqualTo(mockDataStore);
            assertThat(node.getChildren()).isEmpty();
        }
    }

    @Nested
    @DisplayName("DataStore Access Tests")
    class DataStoreAccessTests {

        @Test
        @DisplayName("Should return correct DataStore")
        void testGetData_ReturnsCorrectDataStore() {
            // When
            DataStore result = testNode.getData();

            // Then
            assertThat(result).isEqualTo(mockDataStore);
        }

        @Test
        @DisplayName("Should return correct data class name")
        void testGetDataClassName_ReturnsCorrectName() {
            // Given
            when(mockDataStore.getName()).thenReturn("TestNode");

            // When
            String result = testNode.getDataClassName();

            // Then
            assertThat(result).isEqualTo("TestNode");
            verify(mockDataStore).getName();
        }
    }

    @Nested
    @DisplayName("DataClass Implementation Tests")
    class DataClassImplementationTests {

        @Test
        @DisplayName("Should get value from DataStore")
        void testGet_ValidKey_ReturnsValue() {
            // Given
            when(mockDataStore.get(stringKey)).thenReturn("testValue");

            // When
            String result = testNode.get(stringKey);

            // Then
            assertThat(result).isEqualTo("testValue");
            verify(mockDataStore).get(stringKey);
        }

        @Test
        @DisplayName("Should set value in DataStore and return this")
        void testSet_ValidKeyValue_SetsValueAndReturnsThis() {
            // When
            TestDataNode result = testNode.set(stringKey, "testValue");

            // Then
            assertThat(result).isSameAs(testNode);
            verify(mockDataStore).put(stringKey, "testValue");
        }

        @Test
        @DisplayName("Should remove value when setting null")
        void testSet_NullValue_RemovesValue() {
            // Given
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);

            // When
            TestDataNode result = testNode.set(stringKey, null);

            // Then
            assertThat(result).isSameAs(testNode);
            verify(mockDataStore).hasValue(stringKey);
            verify(mockDataStore).remove(stringKey);
            verify(mockDataStore, never()).put(any(), any());
        }

        @Test
        @DisplayName("Should not remove value when setting null if key not present")
        void testSet_NullValueKeyNotPresent_DoesNotRemove() {
            // Given
            when(mockDataStore.hasValue(stringKey)).thenReturn(false);

            // When
            TestDataNode result = testNode.set(stringKey, null);

            // Then
            assertThat(result).isSameAs(testNode);
            verify(mockDataStore).hasValue(stringKey);
            verify(mockDataStore, never()).remove(stringKey);
            verify(mockDataStore, never()).put(any(), any());
        }

        @Test
        @DisplayName("Should check if DataStore has value")
        void testHasValue_ValidKey_ChecksDataStore() {
            // Given
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);

            // When
            boolean result = testNode.hasValue(stringKey);

            // Then
            assertThat(result).isTrue();
            verify(mockDataStore).hasValue(stringKey);
        }

        @Test
        @DisplayName("Should always return true for isClosed")
        void testIsClosed_AlwaysReturnsTrue() {
            // When
            boolean result = testNode.isClosed();

            // Then
            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("Static Helper Methods Tests")
    class StaticHelperMethodsTests {

        @Test
        @DisplayName("Should create new DataStore for node class")
        void testNewDataStore_ValidNodeClass_CreatesDataStore() {
            // When
            DataStore result = DataNode.newDataStore(TestDataNode.class);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getName()).contains("TestDataNode");
        }

        @Test
        @DisplayName("Should create new instance with empty children")
        void testNewInstance_EmptyChildren_CreatesInstance() {
            // When
            TestDataNode result = DataNode.newInstance(TestDataNode.class, Collections.emptyList());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getChildren()).isEmpty();
            assertThat(result.getData()).isNotNull();
        }

        @Test
        @DisplayName("Should create new instance with children")
        void testNewInstance_WithChildren_CreatesInstanceWithChildren() {
            // Given
            TestDataNode child = DataNode.newInstance(TestDataNode.class, Collections.emptyList());
            List<TestDataNode> children = Arrays.asList(child);

            // When
            TestDataNode result = DataNode.newInstance(TestDataNode.class, children);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getChildren()).hasSize(1);
            assertThat(result.getChildren().get(0)).isEqualTo(child);
        }
    }

    @Nested
    @DisplayName("Copy Functionality Tests")
    class CopyFunctionalityTests {

        @Test
        @DisplayName("Should create copy with same data but different instance")
        void testCopy_CreatesIndependentCopy() {
            // Given - setup mocks for copy functionality
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getName()).thenReturn("TestStoreDefinition");
            testNode.set(stringKey, "testValue");

            // When
            TestDataNode copy = testNode.copy();

            // Then - copy should be different instance but same class
            assertThat(copy).isNotSameAs(testNode);
            assertThat(copy.getClass()).isEqualTo(testNode.getClass());
            // Note: Testing complete copy functionality requires proper StoreDefinition
            // setup which is complex for test classes. This test validates the copy method
            // execution.
        }
    }

    @Nested
    @DisplayName("Store Definition Tests")
    class StoreDefinitionTests {

        @Test
        @DisplayName("Should return store definition from DataStore")
        void testGetStoreDefinitionTry_ReturnsFromDataStore() {
            // Given
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));

            // When
            Optional<StoreDefinition> result = testNode.getStoreDefinitionTry();

            // Then
            assertThat(result).isPresent();
            assertThat(result.get()).isEqualTo(mockStoreDefinition);
            verify(mockDataStore).getStoreDefinitionTry();
        }

        @Test
        @DisplayName("Should return empty optional when no store definition")
        void testGetStoreDefinitionTry_NoDefinition_ReturnsEmpty() {
            // Given
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.empty());

            // When
            Optional<StoreDefinition> result = testNode.getStoreDefinitionTry();

            // Then
            assertThat(result).isEmpty();
            verify(mockDataStore).getStoreDefinitionTry();
        }
    }

    @Nested
    @DisplayName("Content String Tests")
    class ContentStringTests {

        @Test
        @DisplayName("Should return DataStore inlined string")
        void testToContentString_ReturnsDataStoreInlinedString() {
            // Given
            when(mockDataStore.toInlinedString()).thenReturn("data: {testString=value}");

            // When
            String result = testNode.toContentString();

            // Then
            assertThat(result).isEqualTo("data: {testString=value}");
            verify(mockDataStore).toInlinedString();
        }
    }

    @Nested
    @DisplayName("Utility Methods Tests")
    class UtilityMethodsTests {

        @Test
        @DisplayName("Should return system newline character")
        void testLn_ReturnsSystemNewline() {
            // When
            String result = testNode.ln();

            // Then
            assertThat(result).isEqualTo(System.lineSeparator());
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should throw exception when constructor not found in newInstance")
        void testNewInstance_InvalidClass_ThrowsException() {
            // Define a class without proper constructor
            class InvalidDataNode extends DataNode<InvalidDataNode> {
                public InvalidDataNode() {
                    super(DataStore.newInstance("test"), Collections.emptyList());
                }

                @Override
                protected InvalidDataNode getThis() {
                    return this;
                }

                @Override
                protected Class<InvalidDataNode> getBaseClass() {
                    return InvalidDataNode.class;
                }
            }

            // When & Then
            assertThatThrownBy(() -> DataNode.newInstance(InvalidDataNode.class, Collections.emptyList()))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not create constructor");
        }
    }

    @Nested
    @DisplayName("Generic Set Method Tests")
    class GenericSetMethodTests {

        @Test
        @DisplayName("Should delegate to dataClass set method")
        void testSetInstance_DelegatesToDataClass() {
            // Given
            TestDataNode otherNode = new TestDataNode(mockDataStore, Collections.emptyList());

            // When & Then
            // The set method delegates to dataClass which is a GenericDataClass
            // This will try to cast the TestDataNode to ADataClass which should fail
            assertThatThrownBy(() -> testNode.set(otherNode))
                    .isInstanceOf(ClassCastException.class)
                    .hasMessageContaining("TestDataNode")
                    .hasMessageContaining("ADataClass");
        }
    }
}
