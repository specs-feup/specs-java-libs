package pt.up.fe.specs.util.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ResourceProvider interface and its implementations.
 * 
 * @author Generated Tests
 */
@DisplayName("ResourceProvider")
class ResourceProviderTest {

    private TestResourceProvider testProvider;
    private String testResource;

    @BeforeEach
    void setUp() {
        testResource = "test/resource/path.txt";
        testProvider = new TestResourceProvider(testResource);
    }

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("should have correct interface methods")
        void shouldHaveCorrectInterfaceMethods() {
            assertThatCode(() -> {
                ResourceProvider.class.getMethod("getResource");
                ResourceProvider.class.getMethod("getEnumResources");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should extend FileResourceProvider")
        void shouldExtendFileResourceProvider() {
            assertThat(FileResourceProvider.class.isAssignableFrom(ResourceProvider.class)).isTrue();
        }

        @Test
        @DisplayName("should be a functional interface")
        void shouldBeAFunctionalInterface() {
            assertThat(ResourceProvider.class.isInterface()).isTrue();
            assertThat(ResourceProvider.class.getAnnotation(java.lang.FunctionalInterface.class)).isNotNull();
        }
    }

    @Nested
    @DisplayName("Basic Functionality")
    class BasicFunctionality {

        @Test
        @DisplayName("should return resource path")
        void shouldReturnResourcePath() {
            assertThat(testProvider.getResource()).isEqualTo(testResource);
        }

        @Test
        @DisplayName("should handle null resource paths")
        void shouldHandleNullResourcePaths() {
            TestResourceProvider nullProvider = new TestResourceProvider(null);

            assertThat(nullProvider.getResource()).isNull();
        }

        @Test
        @DisplayName("should handle empty resource paths")
        void shouldHandleEmptyResourcePaths() {
            TestResourceProvider emptyProvider = new TestResourceProvider("");

            assertThat(emptyProvider.getResource()).isEmpty();
        }

        @Test
        @DisplayName("should handle forward slash separated paths")
        void shouldHandleForwardSlashSeparatedPaths() {
            String path = "path/to/resource/file.txt";
            TestResourceProvider provider = new TestResourceProvider(path);

            assertThat(provider.getResource()).isEqualTo(path);
        }

        @Test
        @DisplayName("should handle paths without leading slash")
        void shouldHandlePathsWithoutLeadingSlash() {
            String path = "resource/file.txt";
            TestResourceProvider provider = new TestResourceProvider(path);

            assertThat(provider.getResource()).isEqualTo(path);
        }
    }

    @Nested
    @DisplayName("Static Factory Methods")
    class StaticFactoryMethods {

        @Test
        @DisplayName("should create instance from string resource")
        void shouldCreateInstanceFromStringResource() {
            String resource = "factory/created/resource.txt";
            ResourceProvider provider = ResourceProvider.newInstance(resource);

            assertThat(provider.getResource()).isEqualTo(resource);
        }

        @Test
        @DisplayName("should create instance with version")
        void shouldCreateInstanceWithVersion() {
            String resource = "versioned/resource.txt";
            String version = "2.0.1";
            ResourceProvider provider = ResourceProvider.newInstance(resource, version);

            assertThat(provider.getResource()).isEqualTo(resource);
            // The version functionality would need to be tested through the GenericResource
            // implementation
        }

        @Test
        @DisplayName("should provide default version")
        void shouldProvideDefaultVersion() {
            String defaultVersion = ResourceProvider.getDefaultVersion();

            assertThat(defaultVersion).isNotNull();
            assertThat(defaultVersion).isEqualTo("1.0");
        }

        @Test
        @DisplayName("should create instance with null resource")
        void shouldCreateInstanceWithNullResource() {
            ResourceProvider provider = ResourceProvider.newInstance(null);

            assertThat(provider.getResource()).isNull();
        }

        @Test
        @DisplayName("should create instance with empty resource")
        void shouldCreateInstanceWithEmptyResource() {
            ResourceProvider provider = ResourceProvider.newInstance("");

            assertThat(provider.getResource()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Enum Resources")
    class EnumResources {

        @Test
        @DisplayName("should return empty list for non-enum implementations")
        void shouldReturnEmptyListForNonEnumImplementations() {
            List<ResourceProvider> enumResources = testProvider.getEnumResources();

            assertThat(enumResources).isEmpty();
        }

        @Test
        @DisplayName("should return enum constants for enum implementations")
        void shouldReturnEnumConstantsForEnumImplementations() {
            TestResourceEnum provider = TestResourceEnum.RESOURCE_A;
            List<ResourceProvider> enumResources = provider.getEnumResources();

            assertThat(enumResources).hasSize(3);
            assertThat(enumResources).containsExactlyInAnyOrder(
                    TestResourceEnum.RESOURCE_A,
                    TestResourceEnum.RESOURCE_B,
                    TestResourceEnum.RESOURCE_C);
        }

        @Test
        @DisplayName("should handle getResourcesFromEnum with class list")
        void shouldHandleGetResourcesFromEnumWithClassList() {
            List<Class<? extends ResourceProvider>> providers = java.util.Arrays.asList(TestResourceEnum.class);

            List<ResourceProvider> resources = ResourceProvider.getResourcesFromEnum(providers);

            assertThat(resources).hasSize(3);
            assertThat(resources).containsExactlyInAnyOrder(
                    TestResourceEnum.RESOURCE_A,
                    TestResourceEnum.RESOURCE_B,
                    TestResourceEnum.RESOURCE_C);
        }

        @Test
        @DisplayName("should handle getResourcesFromEnum with varargs")
        void shouldHandleGetResourcesFromEnumWithVarargs() {
            List<ResourceProvider> resources = ResourceProvider.getResourcesFromEnum(TestResourceEnum.class);

            assertThat(resources).hasSize(3);
        }

        @Test
        @DisplayName("should handle getResources with enum class")
        void shouldHandleGetResourcesWithEnumClass() {
            List<ResourceProvider> resources = ResourceProvider.getResources(TestResourceEnum.class);

            assertThat(resources).hasSize(3);
        }

        @Test
        @DisplayName("should handle empty provider list")
        void shouldHandleEmptyProviderList() {
            List<Class<? extends ResourceProvider>> emptyList = java.util.Collections.emptyList();

            List<ResourceProvider> resources = ResourceProvider.getResourcesFromEnum(emptyList);

            assertThat(resources).isEmpty();
        }
    }

    @Nested
    @DisplayName("Lambda Implementation")
    class LambdaImplementation {

        @Test
        @DisplayName("should work with lambda expressions")
        void shouldWorkWithLambdaExpressions() {
            ResourceProvider lambdaProvider = () -> "lambda/resource.txt";

            assertThat(lambdaProvider.getResource()).isEqualTo("lambda/resource.txt");
        }

        @Test
        @DisplayName("should work with method references")
        void shouldWorkWithMethodReferences() {
            String constantResource = "method/ref/resource.txt";
            ResourceProvider methodRefProvider = () -> constantResource;

            assertThat(methodRefProvider.getResource()).isEqualTo(constantResource);
        }

        @Test
        @DisplayName("should support dynamic resource generation")
        void shouldSupportDynamicResourceGeneration() {
            ResourceProvider dynamicProvider = () -> "dynamic/" + System.currentTimeMillis() + ".txt";

            String resource1 = dynamicProvider.getResource();
            String resource2 = dynamicProvider.getResource();

            assertThat(resource1).startsWith("dynamic/");
            assertThat(resource1).endsWith(".txt");
            assertThat(resource2).startsWith("dynamic/");
            assertThat(resource2).endsWith(".txt");
        }
    }

    @Nested
    @DisplayName("Resource Path Validation")
    class ResourcePathValidation {

        @Test
        @DisplayName("should handle deep nested paths")
        void shouldHandleDeepNestedPaths() {
            String deepPath = "very/deep/nested/path/to/resource/file.txt";
            ResourceProvider provider = ResourceProvider.newInstance(deepPath);

            assertThat(provider.getResource()).isEqualTo(deepPath);
        }

        @Test
        @DisplayName("should handle different file extensions")
        void shouldHandleDifferentFileExtensions() {
            String[] extensions = { ".txt", ".json", ".xml", ".properties", ".yml", ".cfg" };

            for (String ext : extensions) {
                String resource = "test/resource" + ext;
                ResourceProvider provider = ResourceProvider.newInstance(resource);

                assertThat(provider.getResource()).isEqualTo(resource);
            }
        }

        @Test
        @DisplayName("should handle resources without extensions")
        void shouldHandleResourcesWithoutExtensions() {
            String resource = "test/resource/without/extension";
            ResourceProvider provider = ResourceProvider.newInstance(resource);

            assertThat(provider.getResource()).isEqualTo(resource);
        }

        @Test
        @DisplayName("should handle special characters in paths")
        void shouldHandleSpecialCharactersInPaths() {
            String resource = "test/resource-with_special.chars@123/file.txt";
            ResourceProvider provider = ResourceProvider.newInstance(resource);

            assertThat(provider.getResource()).isEqualTo(resource);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle providers that throw exceptions")
        void shouldHandleProvidersThrowExceptions() {
            ResourceProvider throwingProvider = () -> {
                throw new RuntimeException("Resource generation failed");
            };

            assertThatThrownBy(() -> throwingProvider.getResource())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Resource generation failed");
        }

        @Test
        @DisplayName("should handle very long resource paths")
        void shouldHandleVeryLongResourcePaths() {
            StringBuilder longPath = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                longPath.append("very/long/path/segment/");
            }
            longPath.append("file.txt");

            ResourceProvider provider = ResourceProvider.newInstance(longPath.toString());

            assertThat(provider.getResource()).isEqualTo(longPath.toString());
        }

        @Test
        @DisplayName("should handle unicode in resource paths")
        void shouldHandleUnicodeInResourcePaths() {
            String unicodeResource = "test/资源/файл/αρχείο.txt";
            ResourceProvider provider = ResourceProvider.newInstance(unicodeResource);

            assertThat(provider.getResource()).isEqualTo(unicodeResource);
        }

        @Test
        @DisplayName("should handle single character resources")
        void shouldHandleSingleCharacterResources() {
            ResourceProvider provider = ResourceProvider.newInstance("a");

            assertThat(provider.getResource()).isEqualTo("a");
        }
    }

    @Nested
    @DisplayName("Polymorphism")
    class Polymorphism {

        @Test
        @DisplayName("should work with different implementations")
        void shouldWorkWithDifferentImplementations() {
            ResourceProvider impl1 = new TestResourceProvider("impl1");
            ResourceProvider impl2 = () -> "impl2";
            ResourceProvider impl3 = ResourceProvider.newInstance("impl3");

            assertThat(impl1.getResource()).isEqualTo("impl1");
            assertThat(impl2.getResource()).isEqualTo("impl2");
            assertThat(impl3.getResource()).isEqualTo("impl3");
        }

        @Test
        @DisplayName("should support interface-based programming")
        void shouldSupportInterfaceBasedProgramming() {
            List<ResourceProvider> providers = java.util.Arrays.asList(
                    new TestResourceProvider("provider1"),
                    () -> "provider2",
                    ResourceProvider.newInstance("provider3"));

            assertThat(providers)
                    .extracting(ResourceProvider::getResource)
                    .containsExactly("provider1", "provider2", "provider3");
        }
    }

    @Nested
    @DisplayName("FileResourceProvider Integration")
    class FileResourceProviderIntegration {

        @Test
        @DisplayName("should work as FileResourceProvider")
        void shouldWorkAsFileResourceProvider() {
            ResourceProvider resourceProvider = ResourceProvider.newInstance("test/resource.txt");
            FileResourceProvider fileProvider = resourceProvider; // Can be cast safely

            assertThat(fileProvider).isNotNull();
            assertThat(fileProvider).isInstanceOf(ResourceProvider.class);
        }
    }

    /**
     * Test implementation of ResourceProvider for testing purposes.
     */
    private static class TestResourceProvider implements ResourceProvider {
        private final String resource;

        public TestResourceProvider(String resource) {
            this.resource = resource;
        }

        @Override
        public String getResource() {
            return resource;
        }
    }

    /**
     * Test enum implementation of ResourceProvider for enum testing.
     */
    private enum TestResourceEnum implements ResourceProvider {
        RESOURCE_A("test/resource/a.txt"),
        RESOURCE_B("test/resource/b.txt"),
        RESOURCE_C("test/resource/c.txt");

        private final String resource;

        TestResourceEnum(String resource) {
            this.resource = resource;
        }

        @Override
        public String getResource() {
            return resource;
        }
    }
}
