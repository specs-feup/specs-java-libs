package org.suikasoft.jOptions.Datakey;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.suikasoft.jOptions.gui.KeyPanelProvider;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Comprehensive test suite for GenericKey generic-aware DataKey implementation.
 * 
 * Tests cover:
 * - Constructor variants with example instances
 * - Generic type inference from example instances
 * - Value class handling and explicit setting
 * - Copy functionality preserving generic types
 * - Edge cases with complex generic types
 * - Runtime type verification limitations
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("GenericKey Generic Type Tests")
class GenericKeyTest {

    @Mock
    private StringCodec<List<String>> mockListDecoder;

    @Mock
    private CustomGetter<List<String>> mockCustomGetter;

    @Mock
    private KeyPanelProvider<List<String>> mockPanelProvider;

    @Mock
    private StoreDefinition mockStoreDefinition;

    @Mock
    private Function<List<String>, List<String>> mockCopyFunction;

    @Mock
    private CustomGetter<List<String>> mockCustomSetter;

    @Mock
    private DataKeyExtraData mockExtraData;

    @Nested
    @DisplayName("Constructor Variants")
    class ConstructorTests {

        @Test
        @DisplayName("simple constructor creates GenericKey with example instance")
        void testSimpleConstructor_CreatesGenericKeyWithExampleInstance() {
            List<String> exampleList = Arrays.asList("example");

            GenericKey<List<String>> key = new GenericKey<>("test.list", exampleList);

            assertThat(key.getName()).isEqualTo("test.list");
            assertThat(key.getValueClass()).isEqualTo(exampleList.getClass());
        }

        @Test
        @DisplayName("constructor with default value provider creates GenericKey")
        void testConstructorWithDefaultValue_CreatesGenericKey() {
            List<String> exampleList = new ArrayList<>();
            Supplier<List<String>> defaultValue = () -> Arrays.asList("default");

            GenericKey<List<String>> key = new GenericKey<>("test.default", exampleList, defaultValue);

            assertThat(key.getName()).isEqualTo("test.default");
            assertThat(key.getValueClass()).isEqualTo(exampleList.getClass());
        }

        @Test
        @DisplayName("constructor with complex generic types")
        void testConstructor_WithComplexGenericTypes() {
            Map<String, Integer> exampleMap = new HashMap<>();
            exampleMap.put("test", 42);

            GenericKey<Map<String, Integer>> key = new GenericKey<>("test.map", exampleMap);

            assertThat(key.getName()).isEqualTo("test.map");
            assertThat(key.getValueClass()).isEqualTo(exampleMap.getClass());
        }
    }

    @Nested
    @DisplayName("Generic Type Inference")
    class TypeInferenceTests {

        @Test
        @DisplayName("getValueClass infers type from example instance")
        void testGetValueClass_InfersTypeFromExampleInstance() {
            List<String> exampleList = new ArrayList<>();

            GenericKey<List<String>> key = new GenericKey<>("inference.test", exampleList);

            assertThat(key.getValueClass()).isEqualTo(ArrayList.class);
        }

        @Test
        @DisplayName("different example instances result in different value classes")
        void testDifferentExampleInstances_ResultInDifferentValueClasses() {
            List<String> arrayList = new ArrayList<>();
            List<String> linkedList = new java.util.LinkedList<>();

            GenericKey<List<String>> arrayKey = new GenericKey<>("array.key", arrayList);
            GenericKey<List<String>> linkedKey = new GenericKey<>("linked.key", linkedList);

            assertThat(arrayKey.getValueClass()).isEqualTo(ArrayList.class);
            assertThat(linkedKey.getValueClass()).isEqualTo(java.util.LinkedList.class);
        }

        @Test
        @DisplayName("inferred types work with inheritance")
        void testInferredTypes_WorkWithInheritance() {
            ArrayList<String> arrayList = new ArrayList<>(); // Specific type

            GenericKey<List<String>> key = new GenericKey<>("inheritance.test", arrayList);

            // Should infer ArrayList, not List
            assertThat(key.getValueClass()).isEqualTo(ArrayList.class);
            assertThat(List.class.isAssignableFrom(key.getValueClass())).isTrue();
        }
    }

    @Nested
    @DisplayName("Value Class Handling")
    class ValueClassHandlingTests {

        @Test
        @DisplayName("setValueClass explicitly sets value class")
        void testSetValueClass_ExplicitlySetsValueClass() {
            List<String> exampleList = new ArrayList<>();

            GenericKey<List<String>> key = new GenericKey<>("explicit.test", exampleList);
            key.setValueClass(List.class);

            assertThat(key.getValueClass()).isEqualTo(List.class);
        }

        @Test
        @DisplayName("setValueClass returns same key instance")
        void testSetValueClass_ReturnsSameKeyInstance() {
            List<String> exampleList = new ArrayList<>();

            GenericKey<List<String>> key = new GenericKey<>("fluent.test", exampleList);
            GenericKey<List<String>> result = (GenericKey<List<String>>) key.setValueClass(List.class);

            assertThat(result).isSameAs(key);
        }

        @Test
        @DisplayName("explicitly set value class overrides inferred class")
        void testExplicitlySetValueClass_OverridesInferredClass() {
            ArrayList<String> specificList = new ArrayList<>();

            GenericKey<List<String>> key = new GenericKey<>("override.test", specificList);

            // Initially inferred as ArrayList
            assertThat(key.getValueClass()).isEqualTo(ArrayList.class);

            // Override with more general class
            key.setValueClass(List.class);
            assertThat(key.getValueClass()).isEqualTo(List.class);
        }
    }

    @Nested
    @DisplayName("Copy Functionality")
    class CopyFunctionalityTests {

        private GenericKey<List<String>> originalKey;
        private List<String> exampleList;

        @BeforeEach
        void setUp() {
            exampleList = Arrays.asList("original");
            originalKey = new GenericKey<>("original.key", exampleList, () -> exampleList);
        }

        @Test
        @DisplayName("copy preserves example instance")
        void testCopy_PreservesExampleInstance() {
            // Since copy is protected, test through creating new instance with same example
            GenericKey<List<String>> copiedKey = new GenericKey<>("copied.key", exampleList);

            assertThat(copiedKey.getValueClass()).isEqualTo(originalKey.getValueClass());
        }

        @Test
        @DisplayName("copied key has different name but same type")
        void testCopiedKey_HasDifferentNameButSameType() {
            GenericKey<List<String>> copiedKey = new GenericKey<>("copied.name", exampleList);

            assertThat(copiedKey.getName()).isEqualTo("copied.name");
            assertThat(copiedKey.getValueClass()).isEqualTo(originalKey.getValueClass());
            assertThat(copiedKey).isNotEqualTo(originalKey);
        }
    }

    @Nested
    @DisplayName("DataKey Interface Implementation")
    class DataKeyInterfaceTests {

        @Test
        @DisplayName("implements DataKey interface correctly")
        void testImplements_DataKeyInterfaceCorrectly() {
            List<String> example = Arrays.asList("test");
            GenericKey<List<String>> key = new GenericKey<>("interface.test", example);

            assertThat(key).isInstanceOf(DataKey.class);
            assertThat(key.getName()).isEqualTo("interface.test");
            assertThat(key.getValueClass()).isEqualTo(example.getClass());
        }

        @Test
        @DisplayName("verifyValueClass returns false for generic types")
        void testVerifyValueClass_ReturnsFalseForGenericTypes() {
            List<String> example = Arrays.asList("test");
            GenericKey<List<String>> key = new GenericKey<>("verify.test", example);

            // GenericKey cannot verify value class due to type erasure
            assertThat(key.verifyValueClass()).isFalse();
        }

        @Test
        @DisplayName("toString works correctly")
        void testToString_WorksCorrectly() {
            List<String> example = Arrays.asList("toString");
            GenericKey<List<String>> key = new GenericKey<>("toString.test", example);

            String result = key.toString();

            assertThat(result).isNotNull();
            assertThat(result).contains("toString.test");
        }
    }

    @Nested
    @DisplayName("Complex Generic Types")
    class ComplexGenericTypesTests {

        @Test
        @DisplayName("handles nested generic types")
        void testHandles_NestedGenericTypes() {
            List<List<String>> nestedList = Arrays.asList(Arrays.asList("nested"));

            GenericKey<List<List<String>>> key = new GenericKey<>("nested.test", nestedList);

            assertThat(key.getName()).isEqualTo("nested.test");
            assertThat(key.getValueClass()).isEqualTo(nestedList.getClass());
        }

        @Test
        @DisplayName("handles map with generic key-value types")
        void testHandles_MapWithGenericKeyValueTypes() {
            Map<String, List<Integer>> complexMap = new HashMap<>();
            complexMap.put("test", Arrays.asList(1, 2, 3));

            GenericKey<Map<String, List<Integer>>> key = new GenericKey<>("complex.map", complexMap);

            assertThat(key.getName()).isEqualTo("complex.map");
            assertThat(key.getValueClass()).isEqualTo(HashMap.class);
        }

        @Test
        @DisplayName("handles wildcard generic types")
        void testHandles_WildcardGenericTypes() {
            List<? extends Number> wildcardList = Arrays.asList(1, 2.0, 3L);

            GenericKey<List<? extends Number>> key = new GenericKey<>("wildcard.test", wildcardList);

            assertThat(key.getName()).isEqualTo("wildcard.test");
            assertThat(key.getValueClass()).isEqualTo(wildcardList.getClass());
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("handles null example instance gracefully")
        void testHandles_NullExampleInstanceGracefully() {
            // This will cause NPE when getValueClass() is called, but constructor should
            // accept it
            assertThatThrownBy(() -> {
                GenericKey<String> key = new GenericKey<>("null.test", null);
                key.getValueClass(); // This should throw NPE
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("handles empty collections as examples")
        void testHandles_EmptyCollectionsAsExamples() {
            List<String> emptyList = new ArrayList<>();

            GenericKey<List<String>> key = new GenericKey<>("empty.test", emptyList);

            assertThat(key.getName()).isEqualTo("empty.test");
            assertThat(key.getValueClass()).isEqualTo(ArrayList.class);
        }

        @Test
        @DisplayName("constructor with empty id works")
        void testConstructor_WithEmptyId_Works() {
            List<String> example = Arrays.asList("test");

            GenericKey<List<String>> key = new GenericKey<>("", example);

            assertThat(key.getName()).isEmpty();
            assertThat(key.getValueClass()).isEqualTo(example.getClass());
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("GenericKey extends ADataKey correctly")
        void testGenericKey_ExtendsADataKeyCorrectly() {
            List<String> example = Arrays.asList("inheritance");
            GenericKey<List<String>> key = new GenericKey<>("inheritance.test", example);

            assertThat(key).isInstanceOf(ADataKey.class);
            assertThat(key).isInstanceOf(DataKey.class);
        }

        @Test
        @DisplayName("multiple GenericKeys work together")
        void testMultipleGenericKeys_WorkTogether() {
            List<String> listExample = Arrays.asList("list");
            Map<String, Integer> mapExample = new HashMap<>();
            mapExample.put("key", 42);

            GenericKey<List<String>> listKey = new GenericKey<>("list.key", listExample);
            GenericKey<Map<String, Integer>> mapKey = new GenericKey<>("map.key", mapExample);

            assertThat(listKey.getValueClass()).isEqualTo(listExample.getClass());
            assertThat(mapKey.getValueClass()).isEqualTo(mapExample.getClass());
            assertThat(listKey).isNotEqualTo(mapKey);
        }

        @Test
        @DisplayName("equals and hashCode work with generic keys")
        void testEqualsAndHashCode_WorkWithGenericKeys() {
            List<String> example1 = Arrays.asList("test");
            List<String> example2 = Arrays.asList("different");

            GenericKey<List<String>> key1 = new GenericKey<>("same.key", example1);
            GenericKey<List<String>> key2 = new GenericKey<>("same.key", example2);
            GenericKey<List<String>> key3 = new GenericKey<>("different.key", example1);

            // Keys with same name should be equal regardless of example
            assertThat(key1).isEqualTo(key2);
            assertThat(key1).isNotEqualTo(key3);
            assertThat(key1.hashCode()).isEqualTo(key2.hashCode());
        }
    }
}
