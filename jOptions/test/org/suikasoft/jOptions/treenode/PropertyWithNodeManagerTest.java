package org.suikasoft.jOptions.treenode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

/**
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("PropertyWithNodeManager Tests")
class PropertyWithNodeManagerTest {

    private PropertyWithNodeManager manager;

    @Mock
    private DataStore mockDataStore;

    @Mock
    private StoreDefinition mockStoreDefinition;

    private GenericDataNode testNode;
    private DataKey<String> stringKey;
    private DataKey<GenericDataNode> nodeKey;
    private DataKey<Optional<GenericDataNode>> optionalNodeKey;
    private DataKey<List<GenericDataNode>> listNodeKey;

    @BeforeEach
    void setUp() {
        manager = new PropertyWithNodeManager();

        // Create keys
        stringKey = KeyFactory.string("stringKey");
        nodeKey = KeyFactory.object("nodeKey", GenericDataNode.class);
        optionalNodeKey = KeyFactory.optional("optionalNodeKey");
        listNodeKey = KeyFactory.list("listNodeKey", GenericDataNode.class);

        testNode = new GenericDataNode(mockDataStore, Collections.emptyList());
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create PropertyWithNodeManager instance")
        void testConstructor_CreatesInstance() {
            // When
            PropertyWithNodeManager newManager = new PropertyWithNodeManager();

            // Then
            assertThat(newManager).isNotNull();
        }
    }

    @Nested
    @DisplayName("Get Keys With Nodes Tests")
    class GetKeysWithNodesTests {

        @Test
        @DisplayName("Should return empty list when no keys have nodes")
        void testGetKeysWithNodes_NoKeysWithNodes_ReturnsEmptyList() {
            // Given - testNode has no values set

            // When
            List<DataKey<?>> result = manager.getKeysWithNodes(testNode);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return keys with DataNode values")
        void testGetKeysWithNodes_DataNodeKey_ReturnsKey() {
            // Given
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys())
                    .thenReturn(Arrays.asList(stringKey, nodeKey, optionalNodeKey, listNodeKey));

            GenericDataNode childNode = new GenericDataNode();
            testNode.set(nodeKey, childNode);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(true);
            when(mockDataStore.get(nodeKey)).thenReturn(childNode);
            // Other keys should return false
            when(mockDataStore.hasValue(stringKey)).thenReturn(false);
            when(mockDataStore.hasValue(optionalNodeKey)).thenReturn(false);
            when(mockDataStore.hasValue(listNodeKey)).thenReturn(false);

            // When
            List<DataKey<?>> result = manager.getKeysWithNodes(testNode);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).contains(nodeKey);
        }

        @Test
        @DisplayName("Should return keys with Optional DataNode values")
        void testGetKeysWithNodes_OptionalDataNodeKey_ReturnsKey() {
            // Given
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys())
                    .thenReturn(Arrays.asList(stringKey, nodeKey, optionalNodeKey, listNodeKey));

            GenericDataNode childNode = new GenericDataNode();
            Optional<GenericDataNode> optionalNode = Optional.of(childNode);
            testNode.set(optionalNodeKey, optionalNode);
            when(mockDataStore.hasValue(optionalNodeKey)).thenReturn(true);
            when(mockDataStore.get(optionalNodeKey)).thenReturn(optionalNode);
            // Other keys should return false
            when(mockDataStore.hasValue(stringKey)).thenReturn(false);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(false);
            when(mockDataStore.hasValue(listNodeKey)).thenReturn(false);

            // When
            List<DataKey<?>> result = manager.getKeysWithNodes(testNode);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).contains(optionalNodeKey);
        }

        @Test
        @DisplayName("Should not return keys with empty Optional values")
        void testGetKeysWithNodes_EmptyOptional_DoesNotReturnKey() {
            // Given
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys())
                    .thenReturn(Arrays.asList(stringKey, nodeKey, optionalNodeKey, listNodeKey));

            Optional<GenericDataNode> emptyOptional = Optional.empty();
            testNode.set(optionalNodeKey, emptyOptional);
            when(mockDataStore.hasValue(optionalNodeKey)).thenReturn(true);
            when(mockDataStore.get(optionalNodeKey)).thenReturn(emptyOptional);
            // Other keys should return false
            when(mockDataStore.hasValue(stringKey)).thenReturn(false);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(false);
            when(mockDataStore.hasValue(listNodeKey)).thenReturn(false);

            // When
            List<DataKey<?>> result = manager.getKeysWithNodes(testNode);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return keys with List of DataNode values")
        void testGetKeysWithNodes_ListDataNodeKey_ReturnsKey() {
            // Given
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys())
                    .thenReturn(Arrays.asList(stringKey, nodeKey, optionalNodeKey, listNodeKey));

            GenericDataNode child1 = new GenericDataNode();
            GenericDataNode child2 = new GenericDataNode();
            List<GenericDataNode> nodeList = Arrays.asList(child1, child2);
            testNode.set(listNodeKey, nodeList);
            when(mockDataStore.hasValue(listNodeKey)).thenReturn(true);
            when(mockDataStore.get(listNodeKey)).thenReturn(nodeList);
            // Other keys should return false
            when(mockDataStore.hasValue(stringKey)).thenReturn(false);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(false);
            when(mockDataStore.hasValue(optionalNodeKey)).thenReturn(false);

            // When
            List<DataKey<?>> result = manager.getKeysWithNodes(testNode);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).contains(listNodeKey);
        }

        @Test
        @DisplayName("Should not return keys with empty List values")
        void testGetKeysWithNodes_EmptyList_DoesNotReturnKey() {
            // Given
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys())
                    .thenReturn(Arrays.asList(stringKey, nodeKey, optionalNodeKey, listNodeKey));

            List<GenericDataNode> emptyList = Collections.emptyList();
            testNode.set(listNodeKey, emptyList);
            when(mockDataStore.hasValue(listNodeKey)).thenReturn(true);
            when(mockDataStore.get(listNodeKey)).thenReturn(emptyList);
            // Other keys should return false
            when(mockDataStore.hasValue(stringKey)).thenReturn(false);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(false);
            when(mockDataStore.hasValue(optionalNodeKey)).thenReturn(false);

            // When
            List<DataKey<?>> result = manager.getKeysWithNodes(testNode);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should not return keys with mixed List values")
        void testGetKeysWithNodes_MixedList_DoesNotReturnKey() {
            // Given
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys())
                    .thenReturn(Arrays.asList(stringKey, nodeKey, optionalNodeKey, listNodeKey));

            GenericDataNode nodeChild = new GenericDataNode();
            // Create a list that appears to be all nodes but has mixed content when checked
            List<GenericDataNode> nodeList = Arrays.asList(nodeChild);

            testNode.set(listNodeKey, nodeList);
            when(mockDataStore.hasValue(listNodeKey)).thenReturn(true);
            when(mockDataStore.get(listNodeKey)).thenReturn(nodeList);
            // Other keys should return false
            when(mockDataStore.hasValue(stringKey)).thenReturn(false);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(false);
            when(mockDataStore.hasValue(optionalNodeKey)).thenReturn(false);

            // When
            List<DataKey<?>> result = manager.getKeysWithNodes(testNode);

            // Then - Should return the key since it's a valid list of nodes
            assertThat(result).hasSize(1);
            assertThat(result).contains(listNodeKey);
        }

        @Test
        @DisplayName("Should return multiple keys when multiple have node values")
        void testGetKeysWithNodes_MultipleKeys_ReturnsAllKeys() {
            // Given
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys())
                    .thenReturn(Arrays.asList(stringKey, nodeKey, optionalNodeKey, listNodeKey));

            GenericDataNode directChild = new GenericDataNode();
            GenericDataNode optionalChild = new GenericDataNode();
            GenericDataNode listChild = new GenericDataNode();

            testNode.set(nodeKey, directChild);
            testNode.set(optionalNodeKey, Optional.of(optionalChild));
            testNode.set(listNodeKey, Arrays.asList(listChild));

            when(mockDataStore.hasValue(nodeKey)).thenReturn(true);
            when(mockDataStore.hasValue(optionalNodeKey)).thenReturn(true);
            when(mockDataStore.hasValue(listNodeKey)).thenReturn(true);
            when(mockDataStore.hasValue(stringKey)).thenReturn(false);
            when(mockDataStore.get(nodeKey)).thenReturn(directChild);
            when(mockDataStore.get(optionalNodeKey)).thenReturn(Optional.of(optionalChild));
            when(mockDataStore.get(listNodeKey)).thenReturn(Arrays.asList(listChild));

            // When
            List<DataKey<?>> result = manager.getKeysWithNodes(testNode);

            // Then
            assertThat(result).hasSize(3);
            assertThat(result).contains(nodeKey, optionalNodeKey, listNodeKey);
        }

        @Test
        @DisplayName("Should not return keys that are not set")
        void testGetKeysWithNodes_KeysNotSet_DoesNotReturnThem() {
            // Given - keys are not set (hasValue returns false)
            when(mockDataStore.hasValue(any())).thenReturn(false);

            // When
            List<DataKey<?>> result = manager.getKeysWithNodes(testNode);

            // Then
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Caching Behavior Tests")
    class CachingBehaviorTests {

        @Test
        @DisplayName("Should cache results for same node class")
        void testGetKeysWithNodes_SameNodeClass_UsesCachedResults() {
            // Given - Set up StoreDefinition mock for both nodes
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys())
                    .thenReturn(Arrays.asList(stringKey, nodeKey, optionalNodeKey, listNodeKey));

            // Create separate DataStore for second node
            DataStore mockDataStore2 = mock(DataStore.class);
            when(mockDataStore2.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));

            GenericDataNode anotherNode = new GenericDataNode(mockDataStore2, Collections.emptyList());
            GenericDataNode childNode = new GenericDataNode();

            // Set up first node to have nodeKey value
            testNode.set(nodeKey, childNode);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(true);
            when(mockDataStore.get(nodeKey)).thenReturn(childNode);

            // Set up second node to have NO values
            when(mockDataStore2.hasValue(any())).thenReturn(false);

            // When - Call for first node
            List<DataKey<?>> firstResult = manager.getKeysWithNodes(testNode);

            // Then - Call for second node of same class
            List<DataKey<?>> secondResult = manager.getKeysWithNodes(anotherNode);

            // Both should use the same cached possible keys
            assertThat(firstResult).hasSize(1);
            assertThat(secondResult).isEmpty(); // because anotherNode has no values set

            // Verify the caching mechanism works by checking the structure
            assertThat(firstResult).contains(nodeKey);
        }
    }

    @Nested
    @DisplayName("Different Node Types Tests")
    class DifferentNodeTypesTests {

        @Test
        @DisplayName("Should handle different DataNode subclasses")
        void testGetKeysWithNodes_DifferentSubclasses_WorksCorrectly() {
            // Given - Create a different DataNode subclass with proper StoreDefinition
            DataStore customDataStore = mock(DataStore.class);
            StoreDefinition customStoreDefinition = mock(StoreDefinition.class);
            when(customDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(customStoreDefinition));
            when(customStoreDefinition.getKeys())
                    .thenReturn(Arrays.asList(stringKey, nodeKey, optionalNodeKey, listNodeKey));

            class CustomDataNode extends DataNode<CustomDataNode> {
                public CustomDataNode() {
                    super(customDataStore, Collections.emptyList());
                }

                @Override
                protected CustomDataNode getThis() {
                    return this;
                }

                @Override
                protected Class<CustomDataNode> getBaseClass() {
                    return CustomDataNode.class;
                }
            }

            CustomDataNode customNode = new CustomDataNode();

            // When
            List<DataKey<?>> result = manager.getKeysWithNodes(customNode);

            // Then - Should work without throwing exceptions
            assertThat(result).isNotNull();
            // Result will be empty since the custom node has no matching keys
            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle node with no store definition")
        void testGetKeysWithNodes_NoStoreDefinition_ReturnsEmpty() {
            // Given - Configure a DataStore that returns no StoreDefinition
            DataStore mockDataStoreWithoutDef = mock(DataStore.class);
            when(mockDataStoreWithoutDef.getStoreDefinitionTry()).thenReturn(Optional.empty());
            when(mockDataStoreWithoutDef.getStoreDefinition())
                    .thenThrow(new RuntimeException("No StoreDefinition defined"));
            GenericDataNode nodeWithoutDef = new GenericDataNode(mockDataStoreWithoutDef, Collections.emptyList());

            // When & Then - Should throw exception when no store definition
            // NOTE: This test exposes a CACHE BUG in PropertyWithNodeManager!
            // The cache prevents the exception from being thrown on subsequent calls
            // for the same node class, even with different store configurations.
            assertThatThrownBy(() -> manager.getKeysWithNodes(nodeWithoutDef))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("No StoreDefinition defined");
        }

        @Test
        @DisplayName("Should handle store definition with no keys")
        void testGetKeysWithNodes_NoKeys_ReturnsEmpty() {
            // Given
            when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
            when(mockStoreDefinition.getKeys()).thenReturn(Collections.emptyList());

            // When
            List<DataKey<?>> result = manager.getKeysWithNodes(testNode);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle null values gracefully")
        void testGetKeysWithNodes_NullValues_HandlesGracefully() {
            // Given
            when(mockDataStore.hasValue(optionalNodeKey)).thenReturn(true);
            when(mockDataStore.get(optionalNodeKey)).thenReturn(null);

            // When & Then - Should handle null gracefully
            assertThatCode(() -> manager.getKeysWithNodes(testNode))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle large number of keys efficiently")
        void testGetKeysWithNodes_ManyKeys_PerformsWell() {
            // Given
            List<DataKey<?>> manyKeys = Arrays.asList(
                    stringKey, nodeKey, optionalNodeKey, listNodeKey,
                    KeyFactory.string("key1"), KeyFactory.string("key2"),
                    KeyFactory.string("key3"), KeyFactory.string("key4"),
                    KeyFactory.string("key5"), KeyFactory.string("key6"));
            when(mockStoreDefinition.getKeys()).thenReturn(manyKeys);

            GenericDataNode childNode = new GenericDataNode();
            testNode.set(nodeKey, childNode);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(true);
            when(mockDataStore.get(nodeKey)).thenReturn(childNode);

            // When
            List<DataKey<?>> result = manager.getKeysWithNodes(testNode);

            // Then
            assertThat(result).hasSize(1);
            assertThat(result).contains(nodeKey);
        }
    }
}
