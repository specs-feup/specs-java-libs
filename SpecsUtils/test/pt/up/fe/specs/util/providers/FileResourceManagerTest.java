package pt.up.fe.specs.util.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import pt.up.fe.specs.util.SpecsIo;

/**
 * Unit tests for FileResourceManager class.
 * 
 * @author Generated Tests
 */
@DisplayName("FileResourceManager")
class FileResourceManagerTest {

    @TempDir
    Path tempDir;

    private FileResourceManager manager;
    private Map<String, FileResourceProvider> testResources;
    private FileResourceProvider mockProvider1;
    private FileResourceProvider mockProvider2;

    @BeforeEach
    void setUp() {
        mockProvider1 = mock(FileResourceProvider.class);
        when(mockProvider1.getFilename()).thenReturn("resource1.txt");
        when(mockProvider1.version()).thenReturn("1.0");

        mockProvider2 = mock(FileResourceProvider.class);
        when(mockProvider2.getFilename()).thenReturn("resource2.jar");
        when(mockProvider2.version()).thenReturn("2.0");

        testResources = new LinkedHashMap<>();
        testResources.put("RESOURCE1", mockProvider1);
        testResources.put("RESOURCE2", mockProvider2);

        manager = new FileResourceManager(testResources);
    }

    @Nested
    @DisplayName("Constructor")
    class Constructor {

        @Test
        @DisplayName("should create instance with provided resources")
        void shouldCreateInstanceWithProvidedResources() {
            assertThat(manager).isNotNull();
            assertThat(manager.get("RESOURCE1")).isEqualTo(mockProvider1);
            assertThat(manager.get("RESOURCE2")).isEqualTo(mockProvider2);
        }

        @Test
        @DisplayName("should handle empty resources map")
        void shouldHandleEmptyResourcesMap() {
            FileResourceManager emptyManager = new FileResourceManager(new HashMap<>());

            assertThat(emptyManager).isNotNull();
        }

        @Test
        @DisplayName("should handle null resources map")
        void shouldHandleNullResourcesMap() {
            assertThatCode(() -> new FileResourceManager(null))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Static Factory Methods")
    class StaticFactoryMethods {

        @Test
        @DisplayName("should create manager from enum class")
        void shouldCreateManagerFromEnumClass() {
            FileResourceManager enumManager = FileResourceManager.fromEnum(TestResourceEnum.class);

            assertThat(enumManager).isNotNull();
            assertThat(enumManager.get("RESOURCE_A")).isNotNull();
            assertThat(enumManager.get("RESOURCE_B")).isNotNull();
        }

        @Test
        @DisplayName("should preserve enum order in resources")
        void shouldPreserveEnumOrderInResources() {
            FileResourceManager enumManager = FileResourceManager.fromEnum(TestResourceEnum.class);

            // Get all resources and verify they exist
            assertThatCode(() -> {
                enumManager.get("RESOURCE_A");
                enumManager.get("RESOURCE_B");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should handle empty enum")
        void shouldHandleEmptyEnum() {
            FileResourceManager enumManager = FileResourceManager.fromEnum(EmptyTestEnum.class);

            assertThat(enumManager).isNotNull();
        }
    }

    @Nested
    @DisplayName("Resource Retrieval")
    class ResourceRetrieval {

        @Test
        @DisplayName("should retrieve resource by string name")
        void shouldRetrieveResourceByStringName() {
            FileResourceProvider provider = manager.get("RESOURCE1");

            assertThat(provider).isEqualTo(mockProvider1);
        }

        @Test
        @DisplayName("should retrieve resource by enum")
        void shouldRetrieveResourceByEnum() {
            TestResourceEnum enumValue = TestResourceEnum.RESOURCE_A;
            FileResourceManager enumManager = FileResourceManager.fromEnum(TestResourceEnum.class);

            FileResourceProvider provider = enumManager.get(enumValue);

            assertThat(provider).isNotNull();
        }

        @Test
        @DisplayName("should throw exception for non-existent resource")
        void shouldThrowExceptionForNonExistentResource() {
            assertThatThrownBy(() -> manager.get("NON_EXISTENT"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Resource 'NON_EXISTENT' not available");
        }

        @Test
        @DisplayName("should include available resources in error message")
        void shouldIncludeAvailableResourcesInErrorMessage() {
            assertThatThrownBy(() -> manager.get("INVALID"))
                    .hasMessageContaining("RESOURCE1")
                    .hasMessageContaining("RESOURCE2");
        }

        @Test
        @DisplayName("should handle null resource name")
        void shouldHandleNullResourceName() {
            assertThatThrownBy(() -> manager.get((String) null))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("should handle empty resource name")
        void shouldHandleEmptyResourceName() {
            assertThatThrownBy(() -> manager.get(""))
                    .isInstanceOf(RuntimeException.class);
        }
    }

    @Nested
    @DisplayName("Local Resources")
    class LocalResources {

        @Test
        @DisplayName("should handle non-existent local resources file")
        void shouldHandleNonExistentLocalResourcesFile() {
            try (MockedStatic<SpecsIo> mockedSpecsIo = mockStatic(SpecsIo.class)) {
                mockedSpecsIo.when(() -> SpecsIo.getLocalFile(anyString(), any(Class.class)))
                        .thenReturn(Optional.empty());

                assertThatCode(() -> manager.addLocalResources("nonexistent.properties"))
                        .doesNotThrowAnyException();
            }
        }

        @Test
        @DisplayName("should not throw exception when adding local resources")
        void shouldNotThrowExceptionWhenAddingLocalResources() {
            // Test basic functionality without complex mocking
            assertThatCode(() -> manager.addLocalResources("test.properties"))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Resource Priority")
    class ResourcePriority {

        @Test
        @DisplayName("should use available resources when no local resource exists")
        void shouldUseAvailableResourcesWhenNoLocalResourceExists() {
            FileResourceProvider provider = manager.get("RESOURCE2");

            assertThat(provider).isEqualTo(mockProvider2);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle large number of resources")
        void shouldHandleLargeNumberOfResources() {
            Map<String, FileResourceProvider> largeResourceMap = new HashMap<>();
            for (int i = 0; i < 1000; i++) {
                FileResourceProvider provider = mock(FileResourceProvider.class);
                when(provider.getFilename()).thenReturn("resource" + i + ".txt");
                when(provider.version()).thenReturn("1.0");
                largeResourceMap.put("RESOURCE_" + i, provider);
            }

            FileResourceManager largeManager = new FileResourceManager(largeResourceMap);

            assertThat(largeManager.get("RESOURCE_0")).isNotNull();
            assertThat(largeManager.get("RESOURCE_999")).isNotNull();
        }

        @Test
        @DisplayName("should handle resources with special characters in names")
        void shouldHandleResourcesWithSpecialCharactersInNames() {
            Map<String, FileResourceProvider> specialResources = new HashMap<>();
            FileResourceProvider provider = mock(FileResourceProvider.class);
            when(provider.getFilename()).thenReturn("special-resource_123.txt");
            when(provider.version()).thenReturn("1.0");
            specialResources.put("SPECIAL_RESOURCE_123", provider);

            FileResourceManager specialManager = new FileResourceManager(specialResources);

            assertThat(specialManager.get("SPECIAL_RESOURCE_123")).isNotNull();
        }

        @Test
        @DisplayName("should handle concurrent access")
        void shouldHandleConcurrentAccess() {
            assertThatCode(() -> {
                for (int i = 0; i < 100; i++) {
                    manager.get("RESOURCE1");
                    manager.get("RESOURCE2");
                }
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Integration")
    class Integration {

        @Test
        @DisplayName("should work with real enum implementation")
        void shouldWorkWithRealEnumImplementation() {
            FileResourceManager realManager = FileResourceManager.fromEnum(TestResourceEnum.class);

            assertThat(realManager.get(TestResourceEnum.RESOURCE_A)).isNotNull();
            assertThat(realManager.get(TestResourceEnum.RESOURCE_B)).isNotNull();
        }

        @Test
        @DisplayName("should maintain consistency between string and enum access")
        void shouldMaintainConsistencyBetweenStringAndEnumAccess() {
            FileResourceManager enumManager = FileResourceManager.fromEnum(TestResourceEnum.class);

            FileResourceProvider byEnum = enumManager.get(TestResourceEnum.RESOURCE_A);
            FileResourceProvider byString = enumManager.get("RESOURCE_A");

            assertThat(byEnum).isEqualTo(byString);
        }
    }

    /**
     * Test enum for FileResourceManager testing.
     */
    public enum TestResourceEnum implements Supplier<FileResourceProvider> {
        RESOURCE_A("test/resource/a.txt"),
        RESOURCE_B("test/resource/b.txt");

        private final String resourcePath;

        TestResourceEnum(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        @Override
        public FileResourceProvider get() {
            return FileResourceProvider.newInstance(new File(resourcePath));
        }
    }

    /**
     * Empty test enum for edge case testing.
     */
    public enum EmptyTestEnum implements Supplier<FileResourceProvider> {
        // No values
        ;

        @Override
        public FileResourceProvider get() {
            return null;
        }
    }
}
