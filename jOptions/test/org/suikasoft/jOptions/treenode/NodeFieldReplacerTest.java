package org.suikasoft.jOptions.treenode;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

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
 * Comprehensive tests for {@link NodeFieldReplacer} utility class.
 * 
 * Tests the utility for replacing fields in DataNode trees based on a
 * replacement detector function.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("NodeFieldReplacer Tests")
class NodeFieldReplacerTest {

    @Mock
    private DataStore mockDataStore;

    @Mock
    private StoreDefinition mockStoreDefinition;

    @Mock
    private Function<GenericDataNode, Optional<GenericDataNode>> mockReplacementDetector;

    private NodeFieldReplacer<GenericDataNode> replacer;
    private GenericDataNode testNode;
    private DataKey<GenericDataNode> nodeKey;
    private DataKey<Optional<GenericDataNode>> optionalNodeKey;
    private DataKey<List<GenericDataNode>> listNodeKey;
    private DataKey<String> stringKey;

    @BeforeEach
    void setUp() {
        // Create keys
        nodeKey = KeyFactory.object("nodeKey", GenericDataNode.class);
        optionalNodeKey = KeyFactory.optional("optionalNodeKey");
        listNodeKey = KeyFactory.list("listNodeKey", GenericDataNode.class);
        stringKey = KeyFactory.string("stringKey");

        // Setup mocks
        when(mockDataStore.getName()).thenReturn("TestNode");
        when(mockDataStore.getStoreDefinitionTry()).thenReturn(Optional.of(mockStoreDefinition));
        when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(nodeKey, optionalNodeKey, listNodeKey, stringKey));

        testNode = new GenericDataNode(mockDataStore, Collections.emptyList());
        replacer = new NodeFieldReplacer<>(mockReplacementDetector);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create NodeFieldReplacer with replacement detector")
        void testConstructor_ValidReplacementDetector_CreatesInstance() {
            // Given
            Function<GenericDataNode, Optional<GenericDataNode>> detector = node -> Optional.empty();

            // When
            NodeFieldReplacer<GenericDataNode> newReplacer = new NodeFieldReplacer<>(detector);

            // Then
            assertThat(newReplacer).isNotNull();
        }

        @Test
        @DisplayName("Should accept null replacement detector")
        void testConstructor_NullReplacementDetector_CreatesInstance() {
            // When & Then - Should not throw exception
            assertThatCode(() -> new NodeFieldReplacer<GenericDataNode>(null))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("DataNode Field Replacement Tests")
    class DataNodeFieldReplacementTests {

        @Test
        @DisplayName("Should replace DataNode field when detector returns replacement")
        void testReplaceFields_DataNodeField_ReplacesWhenDetectorReturns() {
            // Given
            GenericDataNode originalChild = new GenericDataNode();
            GenericDataNode replacementChild = new GenericDataNode();

            testNode.set(nodeKey, originalChild);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(true);
            when(mockDataStore.get(nodeKey)).thenReturn(originalChild);
            when(mockReplacementDetector.apply(originalChild)).thenReturn(Optional.of(replacementChild));

            // When
            replacer.replaceFields(testNode);

            // Then
            verify(mockReplacementDetector).apply(originalChild);
            // The replacement should have been set
            verify(mockDataStore).put(eq(nodeKey), eq(replacementChild));
        }

        @Test
        @DisplayName("Should not replace DataNode field when detector returns empty")
        void testReplaceFields_DataNodeField_DoesNotReplaceWhenDetectorReturnsEmpty() {
            // Given
            GenericDataNode originalChild = new GenericDataNode();

            testNode.set(nodeKey, originalChild);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(true);
            when(mockDataStore.get(nodeKey)).thenReturn(originalChild);
            when(mockReplacementDetector.apply(originalChild)).thenReturn(Optional.empty());

            // Clear interactions from setup
            clearInvocations(mockDataStore);

            // When
            replacer.replaceFields(testNode);

            // Then
            verify(mockReplacementDetector).apply(originalChild);
            // No replacement should occur beyond initial set
            verify(mockDataStore, never()).put(eq(nodeKey), any(GenericDataNode.class));
        }
    }

    @Nested
    @DisplayName("Optional Field Replacement Tests")
    class OptionalFieldReplacementTests {

        @Test
        @DisplayName("Should replace Optional DataNode field when detector returns replacement")
        void testReplaceFields_OptionalField_ReplacesWhenDetectorReturns() {
            // Given
            GenericDataNode originalChild = new GenericDataNode();
            GenericDataNode replacementChild = new GenericDataNode();
            Optional<GenericDataNode> optionalOriginal = Optional.of(originalChild);

            testNode.set(optionalNodeKey, optionalOriginal);
            when(mockDataStore.hasValue(optionalNodeKey)).thenReturn(true);
            when(mockDataStore.get(optionalNodeKey)).thenReturn(optionalOriginal);
            when(mockReplacementDetector.apply(originalChild)).thenReturn(Optional.of(replacementChild));

            // When
            replacer.replaceFields(testNode);

            // Then
            verify(mockReplacementDetector).apply(originalChild);
            // The replacement should have been set as Optional
            verify(mockDataStore).put(eq(optionalNodeKey), eq(Optional.of(replacementChild)));
        }

        @Test
        @DisplayName("Should not replace Optional DataNode field when detector returns empty")
        void testReplaceFields_OptionalField_DoesNotReplaceWhenDetectorReturnsEmpty() {
            // Given
            GenericDataNode originalChild = new GenericDataNode();
            Optional<GenericDataNode> optionalOriginal = Optional.of(originalChild);

            testNode.set(optionalNodeKey, optionalOriginal);
            when(mockDataStore.hasValue(optionalNodeKey)).thenReturn(true);
            when(mockDataStore.get(optionalNodeKey)).thenReturn(optionalOriginal);
            when(mockReplacementDetector.apply(originalChild)).thenReturn(Optional.empty());

            // Clear interactions from setup
            clearInvocations(mockDataStore);

            // When
            replacer.replaceFields(testNode);

            // Then
            verify(mockReplacementDetector).apply(originalChild);
            // No replacement should occur beyond initial set
            verify(mockDataStore, never()).put(eq(optionalNodeKey), any());
        }

        @Test
        @DisplayName("Should handle empty Optional field")
        void testReplaceFields_EmptyOptionalField_SkipsReplacement() {
            // Given
            Optional<GenericDataNode> emptyOptional = Optional.empty();

            testNode.set(optionalNodeKey, emptyOptional);
            when(mockDataStore.hasValue(optionalNodeKey)).thenReturn(true);
            when(mockDataStore.get(optionalNodeKey)).thenReturn(emptyOptional);

            // When
            replacer.replaceFields(testNode);

            // Then
            // Detector should not be called for empty optional
            verify(mockReplacementDetector, never()).apply(any());
        }
    }

    @Nested
    @DisplayName("List Field Replacement Tests")
    class ListFieldReplacementTests {

        @Test
        @DisplayName("Should replace nodes in List field when detector returns replacements")
        void testReplaceFields_ListField_ReplacesWhenDetectorReturns() {
            // Given
            GenericDataNode original1 = new GenericDataNode();
            GenericDataNode original2 = new GenericDataNode();
            GenericDataNode replacement1 = new GenericDataNode();
            GenericDataNode replacement2 = new GenericDataNode();
            List<GenericDataNode> originalList = Arrays.asList(original1, original2);

            testNode.set(listNodeKey, originalList);
            when(mockDataStore.hasValue(listNodeKey)).thenReturn(true);
            when(mockDataStore.get(listNodeKey)).thenReturn(originalList);
            when(mockReplacementDetector.apply(original1)).thenReturn(Optional.of(replacement1));
            when(mockReplacementDetector.apply(original2)).thenReturn(Optional.of(replacement2));

            // When
            replacer.replaceFields(testNode);

            // Then
            verify(mockReplacementDetector).apply(original1);
            verify(mockReplacementDetector).apply(original2);
            // The replacement list should have been set
            verify(mockDataStore).put(eq(listNodeKey), argThat(list -> list instanceof List &&
                    ((List<?>) list).size() == 2 &&
                    ((List<?>) list).contains(replacement1) &&
                    ((List<?>) list).contains(replacement2)));
        }

        @Test
        @DisplayName("Should handle mixed replacements in List field")
        void testReplaceFields_ListField_HandlesMixedReplacements() {
            // Given
            GenericDataNode original1 = new GenericDataNode();
            GenericDataNode original2 = new GenericDataNode();
            GenericDataNode replacement1 = new GenericDataNode();
            List<GenericDataNode> originalList = Arrays.asList(original1, original2);

            testNode.set(listNodeKey, originalList);
            when(mockDataStore.hasValue(listNodeKey)).thenReturn(true);
            when(mockDataStore.get(listNodeKey)).thenReturn(originalList);
            when(mockReplacementDetector.apply(original1)).thenReturn(Optional.of(replacement1));
            when(mockReplacementDetector.apply(original2)).thenReturn(Optional.empty());

            // When
            replacer.replaceFields(testNode);

            // Then
            verify(mockReplacementDetector).apply(original1);
            verify(mockReplacementDetector).apply(original2);
            // The replacement list should have replacement1 and original2
            verify(mockDataStore).put(eq(listNodeKey), argThat(list -> list instanceof List &&
                    ((List<?>) list).size() == 2 &&
                    ((List<?>) list).contains(replacement1) &&
                    ((List<?>) list).contains(original2)));
        }

        @Test
        @DisplayName("Should handle empty List field")
        void testReplaceFields_EmptyListField_SkipsReplacement() {
            // Given
            List<GenericDataNode> emptyList = Collections.emptyList();

            testNode.set(listNodeKey, emptyList);
            when(mockDataStore.hasValue(listNodeKey)).thenReturn(true);
            when(mockDataStore.get(listNodeKey)).thenReturn(emptyList);

            // When
            replacer.replaceFields(testNode);

            // Then
            // Detector should not be called for empty list
            verify(mockReplacementDetector, never()).apply(any());
        }
    }

    @Nested
    @DisplayName("Non-Node Field Tests")
    class NonNodeFieldTests {

        @Test
        @DisplayName("Should skip non-node fields")
        void testReplaceFields_NonNodeFields_SkipsReplacement() {
            // Given
            testNode.set(stringKey, "testValue");
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);

            // When
            replacer.replaceFields(testNode);

            // Then
            // Detector should not be called for string fields
            verify(mockReplacementDetector, never()).apply(any());
        }
    }

    @Nested
    @DisplayName("Multiple Fields Tests")
    class MultipleFieldsTests {

        @Test
        @DisplayName("Should handle multiple field types in same node")
        void testReplaceFields_MultipleFieldTypes_HandlesAll() {
            // Given
            GenericDataNode directChild = new GenericDataNode();
            GenericDataNode optionalChild = new GenericDataNode();
            GenericDataNode listChild = new GenericDataNode();
            GenericDataNode directReplacement = new GenericDataNode();
            GenericDataNode optionalReplacement = new GenericDataNode();
            GenericDataNode listReplacement = new GenericDataNode();

            testNode.set(nodeKey, directChild);
            testNode.set(optionalNodeKey, Optional.of(optionalChild));
            testNode.set(listNodeKey, Arrays.asList(listChild));
            testNode.set(stringKey, "testValue");

            when(mockDataStore.hasValue(nodeKey)).thenReturn(true);
            when(mockDataStore.hasValue(optionalNodeKey)).thenReturn(true);
            when(mockDataStore.hasValue(listNodeKey)).thenReturn(true);
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);
            when(mockDataStore.get(nodeKey)).thenReturn(directChild);
            when(mockDataStore.get(optionalNodeKey)).thenReturn(Optional.of(optionalChild));
            when(mockDataStore.get(listNodeKey)).thenReturn(Arrays.asList(listChild));

            when(mockReplacementDetector.apply(directChild)).thenReturn(Optional.of(directReplacement));
            when(mockReplacementDetector.apply(optionalChild)).thenReturn(Optional.of(optionalReplacement));
            when(mockReplacementDetector.apply(listChild)).thenReturn(Optional.of(listReplacement));

            // When
            replacer.replaceFields(testNode);

            // Then
            verify(mockReplacementDetector).apply(directChild);
            verify(mockReplacementDetector).apply(optionalChild);
            verify(mockReplacementDetector).apply(listChild);

            verify(mockDataStore).put(eq(nodeKey), eq(directReplacement));
            verify(mockDataStore).put(eq(optionalNodeKey), eq(Optional.of(optionalReplacement)));
            verify(mockDataStore).put(eq(listNodeKey), argThat(list -> list instanceof List &&
                    ((List<?>) list).size() == 1 &&
                    ((List<?>) list).contains(listReplacement)));
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should handle exception in replacement detector")
        void testReplaceFields_ReplacementDetectorThrows_PropagatesException() {
            // Given
            GenericDataNode originalChild = new GenericDataNode();

            testNode.set(nodeKey, originalChild);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(true);
            when(mockDataStore.get(nodeKey)).thenReturn(originalChild);
            when(mockReplacementDetector.apply(originalChild)).thenThrow(new RuntimeException("Test exception"));

            // When & Then
            assertThatThrownBy(() -> replacer.replaceFields(testNode))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Test exception");
        }

        @Test
        @DisplayName("Should handle null replacement detector gracefully")
        void testReplaceFields_NullReplacementDetector_ThrowsException() {
            // Given
            NodeFieldReplacer<GenericDataNode> nullReplacer = new NodeFieldReplacer<>(null);
            GenericDataNode originalChild = new GenericDataNode();

            testNode.set(nodeKey, originalChild);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(true);
            when(mockDataStore.get(nodeKey)).thenReturn(originalChild);

            // When & Then
            assertThatThrownBy(() -> nullReplacer.replaceFields(testNode))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Real Replacement Function Tests")
    class RealReplacementFunctionTests {

        @Test
        @DisplayName("Should work with real replacement function")
        void testReplaceFields_RealFunction_WorksCorrectly() {
            // Given
            Function<GenericDataNode, Optional<GenericDataNode>> realFunction = node -> {
                // Replace nodes that have a specific string value
                if (node.hasValue(stringKey) && "replace".equals(node.get(stringKey))) {
                    GenericDataNode replacement = new GenericDataNode();
                    replacement.set(stringKey, "replaced");
                    return Optional.of(replacement);
                }
                return Optional.empty();
            };

            NodeFieldReplacer<GenericDataNode> realReplacer = new NodeFieldReplacer<>(realFunction);

            GenericDataNode originalChild = new GenericDataNode();
            originalChild.set(stringKey, "replace");

            testNode.set(nodeKey, originalChild);
            when(mockDataStore.hasValue(nodeKey)).thenReturn(true);
            when(mockDataStore.get(nodeKey)).thenReturn(originalChild);

            // When
            realReplacer.replaceFields(testNode);

            // Then
            // Verify that the replacement occurred - the child should be replaced
            verify(mockDataStore).put(eq(nodeKey), argThat(replacement -> replacement instanceof GenericDataNode &&
                    "replaced".equals(replacement.get(stringKey))));
        }
    }

    @Nested
    @DisplayName("Node Without Fields Tests")
    class NodeWithoutFieldsTests {

        @Test
        @DisplayName("Should handle node with no fields")
        void testReplaceFields_NodeWithNoFields_DoesNothing() {
            // Given
            when(mockStoreDefinition.getKeys()).thenReturn(Collections.emptyList());
            GenericDataNode emptyNode = new GenericDataNode(mockDataStore, Collections.emptyList());

            // When & Then - Should not throw exception
            assertThatCode(() -> replacer.replaceFields(emptyNode))
                    .doesNotThrowAnyException();

            verify(mockReplacementDetector, never()).apply(any());
        }

        @Test
        @DisplayName("Should handle node with only non-node fields")
        void testReplaceFields_OnlyNonNodeFields_SkipsAll() {
            // Given
            when(mockStoreDefinition.getKeys()).thenReturn(Arrays.asList(stringKey));
            testNode.set(stringKey, "testValue");
            when(mockDataStore.hasValue(stringKey)).thenReturn(true);

            // When
            replacer.replaceFields(testNode);

            // Then
            verify(mockReplacementDetector, never()).apply(any());
        }
    }
}
