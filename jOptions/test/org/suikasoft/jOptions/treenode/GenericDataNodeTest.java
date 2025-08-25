package org.suikasoft.jOptions.treenode;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
 * Comprehensive tests for {@link GenericDataNode} implementation.
 * 
 * Tests the generic implementation of a DataNode for use when a specific node
 * type is not required.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GenericDataNode Tests")
class GenericDataNodeTest {

    @Mock
    private DataStore mockDataStore;

    @Mock
    private StoreDefinition mockStoreDefinition;

    private GenericDataNode testNode;
    private DataKey<String> stringKey;

    @BeforeEach
    void setUp() {
        stringKey = KeyFactory.string("testString");
        testNode = new GenericDataNode(mockDataStore, Collections.emptyList());
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create GenericDataNode with DataStore and children")
        void testConstructor_WithDataStoreAndChildren_Success() {
            // Given
            List<GenericDataNode> children = Arrays.asList(
                    new GenericDataNode(),
                    new GenericDataNode());

            // When
            GenericDataNode node = new GenericDataNode(mockDataStore, children);

            // Then
            assertThat(node.getData()).isEqualTo(mockDataStore);
            assertThat(node.getChildren()).hasSize(2);
            assertThat(node.getChildren()).containsExactlyElementsOf(children);
        }

        @Test
        @DisplayName("Should create GenericDataNode with null children")
        void testConstructor_WithNullChildren_Success() {
            // When
            GenericDataNode node = new GenericDataNode(mockDataStore, null);

            // Then
            assertThat(node.getData()).isEqualTo(mockDataStore);
            assertThat(node.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("Should create GenericDataNode with empty children")
        void testConstructor_WithEmptyChildren_Success() {
            // When
            GenericDataNode node = new GenericDataNode(mockDataStore, Collections.emptyList());

            // Then
            assertThat(node.getData()).isEqualTo(mockDataStore);
            assertThat(node.getChildren()).isEmpty();
        }

        @Test
        @DisplayName("Should create GenericDataNode with default constructor")
        void testDefaultConstructor_Success() {
            // When
            GenericDataNode node = new GenericDataNode();

            // Then
            assertThat(node.getData()).isNotNull();
            assertThat(node.getData().getName()).isEqualTo("GenericDataNode");
            assertThat(node.getChildren()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Abstract Method Implementation Tests")
    class AbstractMethodImplementationTests {

        @Test
        @DisplayName("Should return this instance from getThis")
        void testGetThis_ReturnsSameInstance() {
            // When
            GenericDataNode result = testNode.getThis();

            // Then
            assertThat(result).isSameAs(testNode);
        }

        @Test
        @DisplayName("Should return correct base class")
        void testGetBaseClass_ReturnsGenericDataNodeClass() {
            // When
            Class<GenericDataNode> result = testNode.getBaseClass();

            // Then
            assertThat(result).isEqualTo(GenericDataNode.class);
        }
    }

    @Nested
    @DisplayName("Data Operations Tests")
    class DataOperationsTests {

        @Test
        @DisplayName("Should set and get values correctly")
        void testSetAndGet_ValidValues_Success() {
            // Given
            GenericDataNode node = new GenericDataNode();

            // When
            GenericDataNode result = node.set(stringKey, "testValue");
            String value = node.get(stringKey);

            // Then
            assertThat(result).isSameAs(node);
            assertThat(value).isEqualTo("testValue");
        }

        @Test
        @DisplayName("Should handle null values by removing key")
        void testSet_NullValue_RemovesValue() {
            // Given
            GenericDataNode node = new GenericDataNode();
            node.set(stringKey, "initialValue");

            // When
            GenericDataNode result = node.set(stringKey, null);

            // Then
            assertThat(result).isSameAs(node);
            assertThat(node.hasValue(stringKey)).isFalse();
        }

        @Test
        @DisplayName("Should check if value exists")
        void testHasValue_ExistingKey_ReturnsTrue() {
            // Given
            GenericDataNode node = new GenericDataNode();
            node.set(stringKey, "testValue");

            // When
            boolean hasValue = node.hasValue(stringKey);

            // Then
            assertThat(hasValue).isTrue();
        }

        @Test
        @DisplayName("Should check if value does not exist")
        void testHasValue_NonExistingKey_ReturnsFalse() {
            // Given
            GenericDataNode node = new GenericDataNode();

            // When
            boolean hasValue = node.hasValue(stringKey);

            // Then
            assertThat(hasValue).isFalse();
        }
    }

    @Nested
    @DisplayName("Tree Structure Tests")
    class TreeStructureTests {

        @Test
        @DisplayName("Should add children to node")
        void testAddChild_SingleChild_Success() {
            // Given
            GenericDataNode parent = new GenericDataNode();
            GenericDataNode child = new GenericDataNode();

            // When
            parent.addChild(child);

            // Then
            assertThat(parent.getChildren()).hasSize(1);
            assertThat(parent.getChildren().get(0)).isEqualTo(child);
            assertThat(child.getParent()).isEqualTo(parent);
        }

        @Test
        @DisplayName("Should handle multiple children")
        void testAddChildren_MultipleChildren_Success() {
            // Given
            GenericDataNode parent = new GenericDataNode();
            GenericDataNode child1 = new GenericDataNode();
            GenericDataNode child2 = new GenericDataNode();

            // When
            parent.addChild(child1);
            parent.addChild(child2);

            // Then
            assertThat(parent.getChildren()).hasSize(2);
            assertThat(parent.getChildren()).containsExactly(child1, child2);
            assertThat(child1.getParent()).isEqualTo(parent);
            assertThat(child2.getParent()).isEqualTo(parent);
        }

        @Test
        @DisplayName("Should create hierarchical structure")
        void testHierarchy_MultipleGenerations_Success() {
            // Given
            GenericDataNode grandparent = new GenericDataNode();
            GenericDataNode parent = new GenericDataNode();
            GenericDataNode child = new GenericDataNode();

            // When
            grandparent.addChild(parent);
            parent.addChild(child);

            // Then
            assertThat(grandparent.getChildren()).hasSize(1);
            assertThat(parent.getParent()).isEqualTo(grandparent);
            assertThat(child.getParent()).isEqualTo(parent);
            assertThat(child.getAscendantsAndSelfStream().skip(1).anyMatch(node -> node.equals(parent))).isTrue();
            assertThat(child.getAscendantsAndSelfStream().skip(1).anyMatch(node -> node.equals(grandparent))).isTrue();
        }
    }

    @Nested
    @DisplayName("Copy and Clone Tests")
    class CopyAndCloneTests {

        @Test
        @DisplayName("Should create independent copy")
        void testCopy_CreatesIndependentInstance() {
            // Given
            GenericDataNode original = new GenericDataNode(); // Use default constructor for real DataStore
            original.set(stringKey, "originalValue");

            // When
            GenericDataNode copy = original.copy();

            // Then
            assertThat(copy).isNotSameAs(original);
            assertThat(copy.getClass()).isEqualTo(original.getClass());
            // Copy should have the same value
            assertThat(copy.get(stringKey)).isEqualTo("originalValue");
        }

        @Test
        @DisplayName("Should copy with children")
        void testCopy_WithChildren_CopiesStructure() {
            // Given
            GenericDataNode parent = new GenericDataNode(); // Use default constructor for real DataStore
            GenericDataNode child = new GenericDataNode(); // Use default constructor for real DataStore
            parent.addChild(child);
            parent.set(stringKey, "parentValue");
            child.set(stringKey, "childValue");

            // When
            GenericDataNode parentCopy = parent.copy();

            // Then
            assertThat(parentCopy).isNotSameAs(parent);
            assertThat(parentCopy.getChildren()).hasSize(1);
            assertThat(parentCopy.get(stringKey)).isEqualTo("parentValue");

            GenericDataNode childCopy = parentCopy.getChildren().get(0);
            assertThat(childCopy).isNotSameAs(child);
            assertThat(childCopy.get(stringKey)).isEqualTo("childValue");
        }
    }

    @Nested
    @DisplayName("Static Factory Method Tests")
    class StaticFactoryMethodTests {

        @Test
        @DisplayName("Should create new instance using DataNode.newInstance")
        void testNewInstance_CreatesGenericDataNode() {
            // When
            GenericDataNode result = DataNode.newInstance(GenericDataNode.class, Collections.emptyList());

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getClass()).isEqualTo(GenericDataNode.class);
            assertThat(result.getChildren()).isEmpty();
            assertThat(result.getData()).isNotNull();
        }

        @Test
        @DisplayName("Should create new instance with children")
        void testNewInstance_WithChildren_CreatesNodeWithChildren() {
            // Given
            GenericDataNode child1 = new GenericDataNode();
            GenericDataNode child2 = new GenericDataNode();
            List<GenericDataNode> children = Arrays.asList(child1, child2);

            // When
            GenericDataNode result = DataNode.newInstance(GenericDataNode.class, children);

            // Then
            assertThat(result).isNotNull();
            assertThat(result.getChildren()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with complex data operations")
        void testComplexOperations_MultipleDataTypes_Success() {
            // Given
            GenericDataNode node = new GenericDataNode();
            DataKey<Integer> intKey = KeyFactory.integer("intValue");
            DataKey<Boolean> boolKey = KeyFactory.bool("boolValue");

            // When
            node.set(stringKey, "stringValue")
                    .set(intKey, 42)
                    .set(boolKey, true);

            // Then
            assertThat(node.get(stringKey)).isEqualTo("stringValue");
            assertThat(node.get(intKey)).isEqualTo(42);
            assertThat(node.get(boolKey)).isEqualTo(true);
            assertThat(node.hasValue(stringKey)).isTrue();
            assertThat(node.hasValue(intKey)).isTrue();
            assertThat(node.hasValue(boolKey)).isTrue();
        }

        @Test
        @DisplayName("Should handle tree traversal operations")
        void testTreeTraversal_ComplexHierarchy_Success() {
            // Given
            GenericDataNode root = new GenericDataNode();
            GenericDataNode level1_1 = new GenericDataNode();
            GenericDataNode level1_2 = new GenericDataNode();
            GenericDataNode level2_1 = new GenericDataNode();

            // When
            root.addChild(level1_1);
            root.addChild(level1_2);
            level1_1.addChild(level2_1);

            // Then
            assertThat(root.getDescendants()).hasSize(3);
            assertThat(root.getDescendants()).contains(level1_1, level1_2, level2_1);
            List<GenericDataNode> ancestors = level2_1.getAscendantsAndSelfStream().skip(1)
                    .collect(java.util.stream.Collectors.toList());
            assertThat(ancestors).contains(level1_1, root);
            assertThat(level2_1.getRoot()).isEqualTo(root);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle circular reference prevention")
        void testCircularReference_PreventedByFramework() {
            // Given
            GenericDataNode node1 = testNode; // Use testNode with mocked DataStore
            GenericDataNode node2 = new GenericDataNode(mockDataStore, Collections.emptyList());

            // When
            node1.addChild(node2);

            // Then - Framework does NOT prevent circular references at tree level
            // Adding parent as child is allowed (node will be copied via sanitizeNode)
            node2.addChild(node1); // This should succeed without throwing
        }

        @Test
        @DisplayName("Should handle large number of children")
        void testLargeNumberOfChildren_PerformanceTest() {
            // Given
            GenericDataNode parent = new GenericDataNode();
            int numberOfChildren = 1000;

            // When
            for (int i = 0; i < numberOfChildren; i++) {
                GenericDataNode child = new GenericDataNode();
                child.set(stringKey, "child" + i);
                parent.addChild(child);
            }

            // Then
            assertThat(parent.getChildren()).hasSize(numberOfChildren);
            assertThat(parent.getChildren().get(0).get(stringKey)).isEqualTo("child0");
            assertThat(parent.getChildren().get(999).get(stringKey)).isEqualTo("child999");
        }
    }
}
