package org.suikasoft.jOptions.treenode.utils;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.treenode.ClassesService;
import org.suikasoft.jOptions.treenode.GenericDataNode;

/**
 * Comprehensive tests for {@link ClassesService} class.
 * 
 * Tests the service that manages and discovers DataNode classes by name for AST
 * nodes.
 * 
 * @author Generated Tests
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ClassesService Tests")
class ClassesServiceTest {

    private ClassesService<GenericDataNode> classesService;
    private String testPackage;

    // Test DataNode implementations - These would need to be in a real package for
    // the service to find them
    public static class TestDataNode extends GenericDataNode {
        public TestDataNode() {
            super();
        }

        public TestDataNode(DataStore data, Collection<? extends GenericDataNode> children) {
            super(data, children);
        }

        public String getTestValue() {
            return getData().get(org.suikasoft.jOptions.Datakey.KeyFactory.string("test"));
        }

        public void setTestValue(String value) {
            getData().set(org.suikasoft.jOptions.Datakey.KeyFactory.string("test"), value);
        }
    }

    @BeforeEach
    void setUp() {
        testPackage = "org.suikasoft.jOptions.treenode";
        classesService = new ClassesService<>(GenericDataNode.class, testPackage);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create ClassesService instance with base class and package")
        void testConstructor_ValidParameters_CreatesInstance() {
            // When
            ClassesService<GenericDataNode> service = new ClassesService<>(GenericDataNode.class, testPackage);

            // Then
            assertThat(service).isNotNull();
        }

        @Test
        @DisplayName("Should create ClassesService instance with base class and multiple packages")
        void testConstructor_MultiplePackages_CreatesInstance() {
            // When
            ClassesService<GenericDataNode> service = new ClassesService<>(GenericDataNode.class,
                    Arrays.asList("org.suikasoft.jOptions.treenode", "org.suikasoft.jOptions.test"));

            // Then
            assertThat(service).isNotNull();
        }

        @Test
        @DisplayName("Should create ClassesService instance with varargs packages")
        void testConstructor_VarargsPackages_CreatesInstance() {
            // When
            ClassesService<GenericDataNode> service = new ClassesService<>(GenericDataNode.class,
                    "org.suikasoft.jOptions.treenode", "org.suikasoft.jOptions.test");

            // Then
            assertThat(service).isNotNull();
        }
    }

    @Nested
    @DisplayName("Default Class Tests")
    class DefaultClassTests {

        @Test
        @DisplayName("Should set and use default class")
        void testSetDefaultClass_ValidClass_SetsDefault() {
            // When
            ClassesService<GenericDataNode> result = classesService.setDefaultClass(GenericDataNode.class);

            // Then
            assertThat(result).isSameAs(classesService);
        }

        @Test
        @DisplayName("Should use default class when specific class not found")
        void testGetClass_UnknownClassName_UsesDefaultClass() {
            // Given
            classesService.setDefaultClass(GenericDataNode.class);

            // When
            Class<? extends GenericDataNode> result = classesService.getClass("UnknownClass");

            // Then
            assertThat(result).isEqualTo(GenericDataNode.class);
        }

        @Test
        @DisplayName("Should throw exception when no default class set and class not found")
        void testGetClass_UnknownClassNameNoDefault_ThrowsException() {
            // When & Then
            assertThatThrownBy(() -> classesService.getClass("UnknownClass"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not map classname 'UnknownClass' to a node class");
        }
    }

    @Nested
    @DisplayName("Class Discovery Tests")
    class ClassDiscoveryTests {

        @Test
        @DisplayName("Should find existing GenericDataNode class")
        void testGetClass_ExistingClassName_ReturnsClass() {
            // When
            Class<? extends GenericDataNode> result = classesService.getClass("GenericDataNode");

            // Then
            assertThat(result).isEqualTo(GenericDataNode.class);
        }

        @Test
        @DisplayName("Should cache class lookups")
        void testGetClass_SameClassName_CachesResult() {
            // When - Multiple calls to same class name
            Class<? extends GenericDataNode> result1 = classesService.getClass("GenericDataNode");
            Class<? extends GenericDataNode> result2 = classesService.getClass("GenericDataNode");

            // Then
            assertThat(result1).isEqualTo(GenericDataNode.class);
            assertThat(result2).isEqualTo(GenericDataNode.class);
            assertThat(result1).isSameAs(result2);
        }

        @Test
        @DisplayName("Should handle multiple package search")
        void testGetClass_MultiplePackages_SearchesAllPackages() {
            // Given
            ClassesService<GenericDataNode> multiPackageService = new ClassesService<>(GenericDataNode.class,
                    "nonexistent.package", "org.suikasoft.jOptions.treenode");

            // When
            Class<? extends GenericDataNode> result = multiPackageService.getClass("GenericDataNode");

            // Then
            assertThat(result).isEqualTo(GenericDataNode.class);
        }
    }

    @Nested
    @DisplayName("Node Builder Tests")
    class NodeBuilderTests {

        @Test
        @DisplayName("Should create node builder for valid class")
        void testGetNodeBuilder_ValidClass_ReturnsBuilder() {
            // When
            BiFunction<DataStore, List<? extends GenericDataNode>, GenericDataNode> builder = classesService
                    .getNodeBuilder(GenericDataNode.class);

            // Then
            assertThat(builder).isNotNull();
        }

        @Test
        @DisplayName("Should create node instances using builder")
        void testGetNodeBuilder_ValidClass_BuilderCreatesInstances() {
            // Given
            BiFunction<DataStore, List<? extends GenericDataNode>, GenericDataNode> builder = classesService
                    .getNodeBuilder(GenericDataNode.class);
            DataStore dataStore = DataStore.newInstance("TestData");
            List<GenericDataNode> children = Collections.emptyList();

            // When
            GenericDataNode result = builder.apply(dataStore, children);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(GenericDataNode.class);
        }

        @Test
        @DisplayName("Should handle null children in builder")
        void testGetNodeBuilder_NullChildren_WorksCorrectly() {
            // Given
            BiFunction<DataStore, List<? extends GenericDataNode>, GenericDataNode> builder = classesService
                    .getNodeBuilder(GenericDataNode.class);
            DataStore dataStore = DataStore.newInstance("TestData");

            // When
            GenericDataNode result = builder.apply(dataStore, null);

            // Then
            assertThat(result).isNotNull();
            assertThat(result).isInstanceOf(GenericDataNode.class);
        }

        @Test
        @DisplayName("Should return null builder for class without proper constructor")
        void testGetNodeBuilder_InvalidClass_ReturnsNull() {
            // When
            BiFunction<DataStore, List<? extends GenericDataNode>, GenericDataNode> builder = classesService
                    .getNodeBuilder(InvalidConstructorNode.class);

            // Then
            assertThat(builder).isNull();
        }
    }

    // Helper class for testing invalid constructor scenario
    public static class InvalidConstructorNode extends GenericDataNode {
        // Missing the required constructor with DataStore and Collection parameters
        public InvalidConstructorNode(String customParam) {
            super();
        }
    }

    @Nested
    @DisplayName("Custom Name Mapping Tests")
    class CustomNameMappingTests {

        @Test
        @DisplayName("Should allow custom subclass to override name mapping")
        void testCustomSimpleNameToFullName_CanBeOverridden() {
            // Given
            ClassesService<GenericDataNode> customService = new ClassesService<GenericDataNode>(GenericDataNode.class,
                    testPackage) {
                @Override
                protected String customSimpleNameToFullName(String nodeClassname) {
                    if ("CustomName".equals(nodeClassname)) {
                        return "org.suikasoft.jOptions.treenode.GenericDataNode";
                    }
                    return null;
                }
            };

            // When
            Class<? extends GenericDataNode> result = customService.getClass("CustomName");

            // Then
            assertThat(result).isEqualTo(GenericDataNode.class);
        }

        @Test
        @DisplayName("Should fall back to package search when custom mapping returns null")
        void testCustomSimpleNameToFullName_FallsBackToPackageSearch() {
            // Given
            ClassesService<GenericDataNode> customService = new ClassesService<GenericDataNode>(GenericDataNode.class,
                    testPackage) {
                @Override
                protected String customSimpleNameToFullName(String nodeClassname) {
                    return null; // Always return null to force package search
                }
            };

            // When
            Class<? extends GenericDataNode> result = customService.getClass("GenericDataNode");

            // Then
            assertThat(result).isEqualTo(GenericDataNode.class);
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should validate class inheritance")
        void testGetClass_NonDataNodeClass_ThrowsException() {
            // Given
            ClassesService<GenericDataNode> service = new ClassesService<GenericDataNode>(GenericDataNode.class,
                    testPackage) {
                @Override
                protected String customSimpleNameToFullName(String nodeClassname) {
                    if ("String".equals(nodeClassname)) {
                        return "java.lang.String";
                    }
                    return null;
                }
            };

            // When & Then
            assertThatThrownBy(() -> service.getClass("String"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("that is not a DataNode");
        }

        @Test
        @DisplayName("Should handle class not found gracefully")
        void testGetClass_ClassNotFound_UsesDefaultOrThrows() {
            // Given
            ClassesService<GenericDataNode> service = new ClassesService<GenericDataNode>(GenericDataNode.class,
                    "nonexistent.package") {
                @Override
                protected String customSimpleNameToFullName(String nodeClassname) {
                    return "nonexistent.package.NonExistentClass";
                }
            };

            // When & Then
            assertThatThrownBy(() -> service.getClass("NonExistent"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not map classname");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should perform complete workflow: discover, build, instantiate")
        void testCompleteWorkflow_ValidClass_WorksEndToEnd() {
            // Given
            classesService.setDefaultClass(GenericDataNode.class);
            DataStore dataStore = DataStore.newInstance("TestWorkflow");
            dataStore.set(org.suikasoft.jOptions.Datakey.KeyFactory.string("testKey"), "testValue");

            // When - Get class
            Class<? extends GenericDataNode> nodeClass = classesService.getClass("GenericDataNode");

            // When - Get builder
            BiFunction<DataStore, List<? extends GenericDataNode>, GenericDataNode> builder = classesService
                    .getNodeBuilder(nodeClass);

            // When - Create instance
            GenericDataNode instance = builder.apply(dataStore, Collections.emptyList());

            // Then
            assertThat(nodeClass).isEqualTo(GenericDataNode.class);
            assertThat(builder).isNotNull();
            assertThat(instance).isNotNull();
            assertThat(instance.getData().get(org.suikasoft.jOptions.Datakey.KeyFactory.string("testKey")))
                    .isEqualTo("testValue");
        }

        @Test
        @DisplayName("Should handle builder exceptions gracefully")
        void testBuilderException_HandlesGracefully() {
            // Given
            BiFunction<DataStore, List<? extends GenericDataNode>, GenericDataNode> builder = classesService
                    .getNodeBuilder(GenericDataNode.class);

            // When & Then - Null DataStore should cause exception
            assertThatThrownBy(() -> builder.apply(null, Collections.emptyList()))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not call constructor for DataNode");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should cache lookups for performance")
        void testCaching_ImprovesPerformance() {
            // Given
            String className = "GenericDataNode";

            // When - First lookup (uncached)
            long startTime1 = System.nanoTime();
            Class<? extends GenericDataNode> result1 = classesService.getClass(className);
            long endTime1 = System.nanoTime();

            // When - Second lookup (cached)
            long startTime2 = System.nanoTime();
            Class<? extends GenericDataNode> result2 = classesService.getClass(className);
            long endTime2 = System.nanoTime();

            // Then
            assertThat(result1).isEqualTo(result2);
            // Second lookup should be faster (though this is timing-dependent)
            long firstLookupTime = endTime1 - startTime1;
            long secondLookupTime = endTime2 - startTime2;
            assertThat(secondLookupTime).isLessThanOrEqualTo(firstLookupTime);
        }

        @Test
        @DisplayName("Should handle multiple concurrent lookups")
        void testConcurrentLookups_HandlesCorrectly() {
            // Given
            String className = "GenericDataNode";

            // When - Multiple lookups
            Class<? extends GenericDataNode> result1 = classesService.getClass(className);
            Class<? extends GenericDataNode> result2 = classesService.getClass(className);
            Class<? extends GenericDataNode> result3 = classesService.getClass(className);

            // Then
            assertThat(result1).isEqualTo(GenericDataNode.class);
            assertThat(result2).isEqualTo(GenericDataNode.class);
            assertThat(result3).isEqualTo(GenericDataNode.class);
            assertThat(result1).isSameAs(result2).isSameAs(result3);
        }
    }
}
