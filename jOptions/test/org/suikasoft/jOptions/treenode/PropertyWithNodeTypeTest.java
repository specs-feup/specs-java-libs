package org.suikasoft.jOptions.treenode;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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

/**
 * Comprehensive tests for {@link PropertyWithNodeType} enum.
 * 
 * Tests the enum representing the type of property associated with a DataNode
 * key.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PropertyWithNodeType Tests")
class PropertyWithNodeTypeTest {

    @Mock
    private DataNode<?> mockNode;

    @Mock
    private DataKey<GenericDataNode> mockDataNodeKey;

    @Mock
    private DataKey<Optional<GenericDataNode>> mockOptionalKey;

    @Mock
    private DataKey<List<GenericDataNode>> mockListKey;

    @Mock
    private DataKey<String> mockStringKey;

    private GenericDataNode testNode;

    @BeforeEach
    void setUp() {
        testNode = new GenericDataNode();
    }

    @Nested
    @DisplayName("Enum Values Tests")
    class EnumValuesTests {

        @Test
        @DisplayName("Should have all expected enum values")
        void testEnumValues_AllExpectedValues() {
            // When
            PropertyWithNodeType[] values = PropertyWithNodeType.values();

            // Then
            assertThat(values).hasSize(4);
            assertThat(values).contains(
                    PropertyWithNodeType.DATA_NODE,
                    PropertyWithNodeType.OPTIONAL,
                    PropertyWithNodeType.LIST,
                    PropertyWithNodeType.NOT_FOUND);
        }

        @Test
        @DisplayName("Should return correct enum from valueOf")
        void testValueOf_ValidNames_ReturnsCorrectEnum() {
            // When & Then
            assertThat(PropertyWithNodeType.valueOf("DATA_NODE")).isEqualTo(PropertyWithNodeType.DATA_NODE);
            assertThat(PropertyWithNodeType.valueOf("OPTIONAL")).isEqualTo(PropertyWithNodeType.OPTIONAL);
            assertThat(PropertyWithNodeType.valueOf("LIST")).isEqualTo(PropertyWithNodeType.LIST);
            assertThat(PropertyWithNodeType.valueOf("NOT_FOUND")).isEqualTo(PropertyWithNodeType.NOT_FOUND);
        }

        @Test
        @DisplayName("Should throw exception for invalid enum name")
        void testValueOf_InvalidName_ThrowsException() {
            // When & Then
            assertThatThrownBy(() -> PropertyWithNodeType.valueOf("INVALID"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("DataNode Key Type Detection Tests")
    class DataNodeKeyTypeDetectionTests {

        @Test
        @DisplayName("Should detect DataNode key type")
        void testGetKeyType_DataNodeKey_ReturnsDataNode() {
            // Given
            DataKey<GenericDataNode> nodeKey = KeyFactory.object("nodeKey", GenericDataNode.class);

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, nodeKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.DATA_NODE);
        }

        @Test
        @DisplayName("Should detect DataNode subclass key type")
        void testGetKeyType_DataNodeSubclassKey_ReturnsDataNode() {
            // Given - Using the same class as the node for assignability test
            when(mockDataNodeKey.getValueClass()).thenReturn(GenericDataNode.class);

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, mockDataNodeKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.DATA_NODE);
        }
    }

    @Nested
    @DisplayName("Optional Key Type Detection Tests")
    class OptionalKeyTypeDetectionTests {

        @Test
        @DisplayName("Should detect Optional key type")
        void testGetKeyType_OptionalKey_ReturnsOptional() {
            // Given
            DataKey<Optional<String>> optionalKey = KeyFactory.optional("optionalKey");

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, optionalKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.OPTIONAL);
        }

        @Test
        @DisplayName("Should detect mocked Optional key type")
        void testGetKeyType_MockedOptionalKey_ReturnsOptional() {
            // Given
            @SuppressWarnings("unchecked")
            Class<Optional<GenericDataNode>> optionalClass = (Class<Optional<GenericDataNode>>) (Class<?>) Optional.class;
            when(mockOptionalKey.getValueClass()).thenReturn(optionalClass);

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, mockOptionalKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.OPTIONAL);
        }
    }

    @Nested
    @DisplayName("List Key Type Detection Tests")
    class ListKeyTypeDetectionTests {

        @Test
        @DisplayName("Should detect List key type")
        void testGetKeyType_ListKey_ReturnsList() {
            // Given
            DataKey<List<String>> listKey = KeyFactory.list("listKey", String.class);

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, listKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.LIST);
        }

        @Test
        @DisplayName("Should detect mocked List key type")
        void testGetKeyType_MockedListKey_ReturnsList() {
            // Given
            @SuppressWarnings("unchecked")
            Class<List<GenericDataNode>> listClass = (Class<List<GenericDataNode>>) (Class<?>) List.class;
            when(mockListKey.getValueClass()).thenReturn(listClass);

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, mockListKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.LIST);
        }
    }

    @Nested
    @DisplayName("Not Found Key Type Detection Tests")
    class NotFoundKeyTypeDetectionTests {

        @Test
        @DisplayName("Should return NOT_FOUND for String key")
        void testGetKeyType_StringKey_ReturnsNotFound() {
            // Given
            DataKey<String> stringKey = KeyFactory.string("stringKey");

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, stringKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.NOT_FOUND);
        }

        @Test
        @DisplayName("Should return NOT_FOUND for Integer key")
        void testGetKeyType_IntegerKey_ReturnsNotFound() {
            // Given
            DataKey<Integer> intKey = KeyFactory.integer("intKey");

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, intKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.NOT_FOUND);
        }

        @Test
        @DisplayName("Should return NOT_FOUND for Boolean key")
        void testGetKeyType_BooleanKey_ReturnsNotFound() {
            // Given
            DataKey<Boolean> boolKey = KeyFactory.bool("boolKey");

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, boolKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.NOT_FOUND);
        }

        @Test
        @DisplayName("Should return NOT_FOUND for mocked String key")
        void testGetKeyType_MockedStringKey_ReturnsNotFound() {
            // Given
            when(mockStringKey.getValueClass()).thenReturn(String.class);

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, mockStringKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.NOT_FOUND);
        }

        @Test
        @DisplayName("Should return NOT_FOUND for unrelated class")
        void testGetKeyType_UnrelatedClass_ReturnsNotFound() {
            // Given
            DataKey<java.util.Date> dateKey = KeyFactory.generic("dateKey", new java.util.Date());

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, dateKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null node gracefully")
        void testGetKeyType_NullNode_HandlesGracefully() {
            // Given
            DataKey<String> stringKey = KeyFactory.string("stringKey");

            // When & Then - This should not throw NPE, but return NOT_FOUND
            // The method doesn't use the node parameter for basic type checking
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(null, stringKey);
            assertThat(result).isEqualTo(PropertyWithNodeType.NOT_FOUND);
        }

        @Test
        @DisplayName("Should handle different DataNode subclasses")
        void testGetKeyType_DifferentDataNodeSubclasses_ReturnsAppropriate() {
            // Given - Create a concrete DataNode subclass
            class CustomDataNode extends DataNode<CustomDataNode> {
                public CustomDataNode() {
                    super(org.suikasoft.jOptions.Interfaces.DataStore.newInstance("Custom"),
                            java.util.Collections.emptyList());
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
            DataKey<GenericDataNode> genericNodeKey = KeyFactory.object("genericKey", GenericDataNode.class);

            // When - Check if GenericDataNode is assignable from
            // CustomDataNode.getBaseClass()
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(customNode, genericNodeKey);

            // Then - Should be NOT_FOUND because GenericDataNode is not assignable from
            // CustomDataNode
            assertThat(result).isEqualTo(PropertyWithNodeType.NOT_FOUND);
        }

        @Test
        @DisplayName("Should handle inheritance correctly")
        void testGetKeyType_InheritanceCheck_WorksCorrectly() {
            // Given - Use Object as the base type which is compatible
            DataKey<Object> objectKey = KeyFactory.object("objectKey", Object.class);

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, objectKey);

            // Then - Object should not be assignable from GenericDataNode's base class
            // perspective
            assertThat(result).isEqualTo(PropertyWithNodeType.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("Complex Type Tests")
    class ComplexTypeTests {

        @Test
        @DisplayName("Should detect parameterized Optional correctly")
        void testGetKeyType_ParameterizedOptional_ReturnsOptional() {
            // Given
            @SuppressWarnings("unchecked")
            DataKey<Optional<GenericDataNode>> optionalNodeKey = mock(DataKey.class);
            @SuppressWarnings("unchecked")
            Class<Optional<GenericDataNode>> optionalClass = (Class<Optional<GenericDataNode>>) (Class<?>) Optional.class;
            when(optionalNodeKey.getValueClass()).thenReturn(optionalClass);

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, optionalNodeKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.OPTIONAL);
        }

        @Test
        @DisplayName("Should detect parameterized List correctly")
        void testGetKeyType_ParameterizedList_ReturnsList() {
            // Given
            @SuppressWarnings("unchecked")
            DataKey<List<GenericDataNode>> listNodeKey = mock(DataKey.class);
            @SuppressWarnings("unchecked")
            Class<List<GenericDataNode>> listClass = (Class<List<GenericDataNode>>) (Class<?>) List.class;
            when(listNodeKey.getValueClass()).thenReturn(listClass);

            // When
            PropertyWithNodeType result = PropertyWithNodeType.getKeyType(testNode, listNodeKey);

            // Then
            assertThat(result).isEqualTo(PropertyWithNodeType.LIST);
        }
    }
}
