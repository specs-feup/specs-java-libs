package org.suikasoft.XStreamPlus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive unit tests for {@link MappingsCollector}.
 * 
 * Tests cover mapping collection from ObjectXml instances, handling nested XML,
 * and avoiding duplicate collection of classes.
 * 
 * @author Generated Tests
 */
@DisplayName("MappingsCollector Tests")
class MappingsCollectorTest {

    @Nested
    @DisplayName("Basic Mapping Collection")
    class BasicMappingCollectionTests {

        private MappingsCollector collector;

        @BeforeEach
        void setUp() {
            collector = new MappingsCollector();
        }

        @Test
        @DisplayName("collectMappings() should return empty map for ObjectXml without mappings")
        void testCollectMappings_NoMappings_ShouldReturnEmpty() {
            // Given
            SimpleObjectXml objectXml = new SimpleObjectXml();

            // When
            Map<String, Class<?>> mappings = collector.collectMappings(objectXml);

            // Then
            assertThat(mappings)
                    .as("Should return empty map for ObjectXml without mappings")
                    .isEmpty();
        }

        @Test
        @DisplayName("collectMappings() should collect single mapping")
        void testCollectMappings_SingleMapping_ShouldCollect() {
            // Given
            SimpleObjectXml objectXml = new SimpleObjectXml();
            objectXml.addMappings("testAlias", TestClass.class);

            // When
            Map<String, Class<?>> mappings = collector.collectMappings(objectXml);

            // Then
            assertThat(mappings)
                    .hasSize(1)
                    .containsEntry("testAlias", TestClass.class);
        }

        @Test
        @DisplayName("collectMappings() should collect multiple mappings")
        void testCollectMappings_MultipleMappings_ShouldCollectAll() {
            // Given
            SimpleObjectXml objectXml = new SimpleObjectXml();
            objectXml.addMappings("alias1", TestClass.class);
            objectXml.addMappings("alias2", String.class);
            objectXml.addMappings("alias3", Integer.class);

            // When
            Map<String, Class<?>> mappings = collector.collectMappings(objectXml);

            // Then
            assertThat(mappings)
                    .hasSize(3)
                    .containsEntry("alias1", TestClass.class)
                    .containsEntry("alias2", String.class)
                    .containsEntry("alias3", Integer.class);
        }
    }

    @Nested
    @DisplayName("Nested XML Mapping Collection")
    class NestedXmlMappingCollectionTests {

        private MappingsCollector collector;

        @BeforeEach
        void setUp() {
            collector = new MappingsCollector();
        }

        @Test
        @DisplayName("collectMappings() should collect mappings from nested ObjectXml")
        void testCollectMappings_NestedXml_ShouldCollectAll() {
            // Given
            SimpleObjectXml parentXml = new SimpleObjectXml();
            parentXml.addMappings("parentAlias", TestClass.class);

            NestedObjectXml nestedXml = new NestedObjectXml();
            nestedXml.addMappings("nestedAlias", NestedClass.class);

            parentXml.addNestedXml(nestedXml);

            // When
            Map<String, Class<?>> mappings = collector.collectMappings(parentXml);

            // Then
            assertThat(mappings)
                    .hasSize(2)
                    .containsEntry("parentAlias", TestClass.class)
                    .containsEntry("nestedAlias", NestedClass.class);
        }

        @Test
        @DisplayName("collectMappings() should handle multiple levels of nesting")
        void testCollectMappings_DeepNesting_ShouldCollectAll() {
            // Given
            SimpleObjectXml parentXml = new SimpleObjectXml();
            parentXml.addMappings("level0", TestClass.class);

            NestedObjectXml level1Xml = new NestedObjectXml();
            level1Xml.addMappings("level1", NestedClass.class);

            DeepNestedObjectXml level2Xml = new DeepNestedObjectXml();
            level2Xml.addMappings("level2", DeepNestedClass.class);

            level1Xml.addNestedXml(level2Xml);
            parentXml.addNestedXml(level1Xml);

            // When
            Map<String, Class<?>> mappings = collector.collectMappings(parentXml);

            // Then
            assertThat(mappings)
                    .hasSize(3)
                    .containsEntry("level0", TestClass.class)
                    .containsEntry("level1", NestedClass.class)
                    .containsEntry("level2", DeepNestedClass.class);
        }

        @Test
        @DisplayName("collectMappings() should handle nested XML without mappings")
        void testCollectMappings_NestedWithoutMappings_ShouldCollectOnlyParent() {
            // Given
            SimpleObjectXml parentXml = new SimpleObjectXml();
            parentXml.addMappings("parentAlias", TestClass.class);

            NestedObjectXml emptyNestedXml = new NestedObjectXml();
            parentXml.addNestedXml(emptyNestedXml);

            // When
            Map<String, Class<?>> mappings = collector.collectMappings(parentXml);

            // Then
            assertThat(mappings)
                    .hasSize(1)
                    .containsEntry("parentAlias", TestClass.class);
        }
    }

    @Nested
    @DisplayName("Duplicate Class Handling")
    class DuplicateClassHandlingTests {

        private MappingsCollector collector;

        @BeforeEach
        void setUp() {
            collector = new MappingsCollector();
        }

        @Test
        @DisplayName("collectMappings() should avoid processing same class multiple times")
        void testCollectMappings_DuplicateClasses_ShouldProcessOnce() {
            // Given
            SimpleObjectXml parentXml = new SimpleObjectXml();
            parentXml.addMappings("parent", TestClass.class);

            NestedObjectXml nested1Xml = new NestedObjectXml();
            nested1Xml.addMappings("nested1", TestClass.class); // Same class

            AnotherNestedObjectXml nested2Xml = new AnotherNestedObjectXml();
            nested2Xml.addMappings("nested2", TestClass.class); // Same class again

            parentXml.addNestedXml(nested1Xml);
            parentXml.addNestedXml(nested2Xml);

            // When
            Map<String, Class<?>> mappings = collector.collectMappings(parentXml);

            // Then
            // Should contain all three mappings even though they reference the same class
            assertThat(mappings)
                    .hasSize(3)
                    .containsEntry("parent", TestClass.class)
                    .containsEntry("nested1", TestClass.class)
                    .containsEntry("nested2", TestClass.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        private MappingsCollector collector;

        @BeforeEach
        void setUp() {
            collector = new MappingsCollector();
        }

        @Test
        @DisplayName("collectMappings() should handle ObjectXml with only nested XML")
        void testCollectMappings_OnlyNestedXml_ShouldCollectNested() {
            // Given
            SimpleObjectXml parentXml = new SimpleObjectXml(); // No own mappings

            NestedObjectXml nestedXml = new NestedObjectXml();
            nestedXml.addMappings("onlyNested", NestedClass.class);

            parentXml.addNestedXml(nestedXml);

            // When
            Map<String, Class<?>> mappings = collector.collectMappings(parentXml);

            // Then
            assertThat(mappings)
                    .hasSize(1)
                    .containsEntry("onlyNested", NestedClass.class);
        }

        @Test
        @DisplayName("collectMappings() should handle mixed mapping sources")
        void testCollectMappings_MixedSources_ShouldCollectAll() {
            // Given
            SimpleObjectXml objectXml = new SimpleObjectXml();

            // Add mappings via different methods
            objectXml.addMappings("single", TestClass.class);

            Map<String, Class<?>> mapMappings = new HashMap<>();
            mapMappings.put("fromMap1", String.class);
            mapMappings.put("fromMap2", Integer.class);
            objectXml.addMappings(mapMappings);

            // When
            Map<String, Class<?>> mappings = collector.collectMappings(objectXml);

            // Then
            assertThat(mappings)
                    .hasSize(3)
                    .containsEntry("single", TestClass.class)
                    .containsEntry("fromMap1", String.class)
                    .containsEntry("fromMap2", Integer.class);
        }

        @Test
        @DisplayName("Multiple collector instances should be independent")
        void testMultipleCollectors_ShouldBeIndependent() {
            // Given
            MappingsCollector collector1 = new MappingsCollector();
            MappingsCollector collector2 = new MappingsCollector();

            SimpleObjectXml objectXml1 = new SimpleObjectXml();
            objectXml1.addMappings("collector1", TestClass.class);

            SimpleObjectXml objectXml2 = new SimpleObjectXml();
            objectXml2.addMappings("collector2", NestedClass.class);

            // When
            Map<String, Class<?>> mappings1 = collector1.collectMappings(objectXml1);
            Map<String, Class<?>> mappings2 = collector2.collectMappings(objectXml2);

            // Then
            assertAll(
                    () -> assertThat(mappings1).hasSize(1).containsEntry("collector1", TestClass.class),
                    () -> assertThat(mappings2).hasSize(1).containsEntry("collector2", NestedClass.class),
                    () -> assertThat(mappings1).doesNotContainKey("collector2"),
                    () -> assertThat(mappings2).doesNotContainKey("collector1"));
        }
    }

    // Test helper classes
    private static class TestClass {
    }

    private static class NestedClass {
    }

    private static class DeepNestedClass {
    }
    
    private static class AnotherTestClass {
    }

    private static class SimpleObjectXml extends ObjectXml<TestClass> {
        @Override
        public Class<TestClass> getTargetClass() {
            return TestClass.class;
        }
    }

    private static class NestedObjectXml extends ObjectXml<NestedClass> {
        @Override
        public Class<NestedClass> getTargetClass() {
            return NestedClass.class;
        }
    }

    private static class DeepNestedObjectXml extends ObjectXml<DeepNestedClass> {
        @Override
        public Class<DeepNestedClass> getTargetClass() {
            return DeepNestedClass.class;
        }
    }

    private static class AnotherNestedObjectXml extends ObjectXml<AnotherTestClass> {
        @Override
        public Class<AnotherTestClass> getTargetClass() {
            return AnotherTestClass.class;
        }
    }
}
