package pt.up.fe.specs.util.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Resources class.
 * 
 * @author Generated Tests
 */
@DisplayName("Resources")
class ResourcesTest {

    @Nested
    @DisplayName("Constructor with Varargs")
    class ConstructorWithVarargs {

        @Test
        @DisplayName("should create Resources with base folder and resource names")
        void shouldCreateResourcesWithBaseFolderAndResourceNames() {
            // Given/When
            Resources resources = new Resources("base", "resource1.txt", "resource2.txt");

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).hasSize(2);

            // Verify the paths are correct
            assertThat(providers.get(0).getResource()).endsWith("base/resource1.txt");
            assertThat(providers.get(1).getResource()).endsWith("base/resource2.txt");
        }

        @Test
        @DisplayName("should handle empty resource array")
        void shouldHandleEmptyResourceArray() {
            // Given/When
            Resources resources = new Resources("base");

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).isEmpty();
        }

        @Test
        @DisplayName("should handle single resource")
        void shouldHandleSingleResource() {
            // Given/When
            Resources resources = new Resources("base", "single.txt");

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).hasSize(1);
            assertThat(providers.get(0).getResource()).endsWith("base/single.txt");
        }

        @Test
        @DisplayName("should automatically add trailing slash to base folder")
        void shouldAutomaticallyAddTrailingSlashToBaseFolder() {
            // Given/When
            Resources resources = new Resources("base", "resource.txt");

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).hasSize(1);
            assertThat(providers.get(0).getResource()).endsWith("base/resource.txt");
        }

        @Test
        @DisplayName("should preserve existing trailing slash")
        void shouldPreserveExistingTrailingSlash() {
            // Given/When
            Resources resources = new Resources("base/", "resource.txt");

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).hasSize(1);
            assertThat(providers.get(0).getResource()).endsWith("base/resource.txt");
        }

        @Test
        @DisplayName("should handle null base folder")
        void shouldHandleNullBaseFolder() {
            // Given/When/Then - NPE occurs when constructor tries to call endsWith() on
            // null
            assertThatThrownBy(() -> new Resources(null, "resource.txt"))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Constructor with List")
    class ConstructorWithList {

        @Test
        @DisplayName("should create Resources with base folder and resource list")
        void shouldCreateResourcesWithBaseFolderAndResourceList() {
            // Given
            List<String> resourceNames = Arrays.asList("file1.txt", "file2.txt", "file3.txt");

            // When
            Resources resources = new Resources("data", resourceNames);

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).hasSize(3);
            assertThat(providers.get(0).getResource()).endsWith("data/file1.txt");
            assertThat(providers.get(1).getResource()).endsWith("data/file2.txt");
            assertThat(providers.get(2).getResource()).endsWith("data/file3.txt");
        }

        @Test
        @DisplayName("should handle empty resource list")
        void shouldHandleEmptyResourceList() {
            // Given
            List<String> resourceNames = Arrays.asList();

            // When
            Resources resources = new Resources("base", resourceNames);

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).isEmpty();
        }

        @Test
        @DisplayName("should reject null resource list")
        void shouldRejectNullResourceList() {
            // Given/When/Then - Constructor should reject null resource list
            assertThatThrownBy(() -> new Resources("base", (List<String>) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Resources list cannot be null");
        }

        @Test
        @DisplayName("should preserve resource order")
        void shouldPreserveResourceOrder() {
            // Given
            List<String> resourceNames = Arrays.asList("z-last.txt", "a-first.txt", "m-middle.txt");

            // When
            Resources resources = new Resources("ordered", resourceNames);

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).hasSize(3);
            assertThat(providers.get(0).getResource()).endsWith("ordered/z-last.txt");
            assertThat(providers.get(1).getResource()).endsWith("ordered/a-first.txt");
            assertThat(providers.get(2).getResource()).endsWith("ordered/m-middle.txt");
        }
    }

    @Nested
    @DisplayName("Path Handling")
    class PathHandling {

        @Test
        @DisplayName("should handle nested folder paths")
        void shouldHandleNestedFolderPaths() {
            // Given/When
            Resources resources = new Resources("base/sub/folder", "resource.txt");

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).hasSize(1);
            assertThat(providers.get(0).getResource()).endsWith("base/sub/folder/resource.txt");
        }

        @Test
        @DisplayName("should handle resources with folder paths")
        void shouldHandleResourcesWithFolderPaths() {
            // Given/When
            Resources resources = new Resources("base", "sub/resource.txt", "another/sub/file.txt");

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).hasSize(2);
            assertThat(providers.get(0).getResource()).endsWith("base/sub/resource.txt");
            assertThat(providers.get(1).getResource()).endsWith("base/another/sub/file.txt");
        }

        @Test
        @DisplayName("should handle absolute-like base paths")
        void shouldHandleAbsoluteLikeBasePaths() {
            // Given/When
            Resources resources = new Resources("/absolute/path", "resource.txt");

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).hasSize(1);
            assertThat(providers.get(0).getResource()).endsWith("/absolute/path/resource.txt");
        }

        @Test
        @DisplayName("should handle empty base folder")
        void shouldHandleEmptyBaseFolder() {
            // Given/When
            Resources resources = new Resources("", "resource.txt");

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).hasSize(1);
            assertThat(providers.get(0).getResource()).endsWith("/resource.txt");
        }

        @Test
        @DisplayName("should handle special characters in paths")
        void shouldHandleSpecialCharactersInPaths() {
            // Given/When
            Resources resources = new Resources("base-with_special.chars", "resource-with_special.txt");

            // Then
            List<ResourceProvider> providers = resources.getResources();
            assertThat(providers).hasSize(1);
            assertThat(providers.get(0).getResource()).endsWith("base-with_special.chars/resource-with_special.txt");
        }
    }

    @Nested
    @DisplayName("Resource Provider Generation")
    class ResourceProviderGeneration {

        @Test
        @DisplayName("should create ResourceProvider instances for each resource")
        void shouldCreateResourceProviderInstancesForEachResource() {
            // Given/When
            Resources resources = new Resources("test", "file1.txt", "file2.txt");
            List<ResourceProvider> providers = resources.getResources();

            // Then
            assertThat(providers).hasSize(2);
            assertThat(providers.get(0)).isInstanceOf(ResourceProvider.class);
            assertThat(providers.get(1)).isInstanceOf(ResourceProvider.class);
        }

        @Test
        @DisplayName("should create different ResourceProvider instances")
        void shouldCreateDifferentResourceProviderInstances() {
            // Given/When
            Resources resources = new Resources("test", "file1.txt", "file2.txt");
            List<ResourceProvider> providers = resources.getResources();

            // Then
            assertThat(providers.get(0)).isNotSameAs(providers.get(1));
        }

        @Test
        @DisplayName("should create new instances on each call")
        void shouldCreateNewInstancesOnEachCall() {
            // Given
            Resources resources = new Resources("test", "file.txt");

            // When
            List<ResourceProvider> providers1 = resources.getResources();
            List<ResourceProvider> providers2 = resources.getResources();

            // Then
            assertThat(providers1).hasSize(1);
            assertThat(providers2).hasSize(1);
            assertThat(providers1.get(0)).isNotSameAs(providers2.get(0));
        }

        @Test
        @DisplayName("should create providers with correct resource paths")
        void shouldCreateProvidersWithCorrectResourcePaths() {
            // Given/When
            Resources resources = new Resources("data", "config.xml", "template.html", "script.js");
            List<ResourceProvider> providers = resources.getResources();

            // Then
            assertThat(providers).hasSize(3);
            assertThat(providers.get(0).getResource()).endsWith("data/config.xml");
            assertThat(providers.get(1).getResource()).endsWith("data/template.html");
            assertThat(providers.get(2).getResource()).endsWith("data/script.js");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle duplicate resource names")
        void shouldHandleDuplicateResourceNames() {
            // Given/When
            Resources resources = new Resources("base", "file.txt", "file.txt", "file.txt");
            List<ResourceProvider> providers = resources.getResources();

            // Then
            assertThat(providers).hasSize(3);
            assertThat(providers.get(0).getResource()).endsWith("base/file.txt");
            assertThat(providers.get(1).getResource()).endsWith("base/file.txt");
            assertThat(providers.get(2).getResource()).endsWith("base/file.txt");
        }

        @Test
        @DisplayName("should handle resources with various file extensions")
        void shouldHandleResourcesWithVariousFileExtensions() {
            // Given/When
            Resources resources = new Resources("files",
                    "document.pdf", "image.jpg", "data.json", "script.py", "archive.zip");
            List<ResourceProvider> providers = resources.getResources();

            // Then
            assertThat(providers).hasSize(5);
            assertThat(providers.get(0).getResource()).endsWith("files/document.pdf");
            assertThat(providers.get(1).getResource()).endsWith("files/image.jpg");
            assertThat(providers.get(2).getResource()).endsWith("files/data.json");
            assertThat(providers.get(3).getResource()).endsWith("files/script.py");
            assertThat(providers.get(4).getResource()).endsWith("files/archive.zip");
        }

        @Test
        @DisplayName("should handle resources without extensions")
        void shouldHandleResourcesWithoutExtensions() {
            // Given/When
            Resources resources = new Resources("base", "README", "LICENSE", "Makefile");
            List<ResourceProvider> providers = resources.getResources();

            // Then
            assertThat(providers).hasSize(3);
            assertThat(providers.get(0).getResource()).endsWith("base/README");
            assertThat(providers.get(1).getResource()).endsWith("base/LICENSE");
            assertThat(providers.get(2).getResource()).endsWith("base/Makefile");
        }

        @Test
        @DisplayName("should handle empty resource names")
        void shouldHandleEmptyResourceNames() {
            // Given/When
            Resources resources = new Resources("base", "", "valid.txt", "");
            List<ResourceProvider> providers = resources.getResources();

            // Then
            assertThat(providers).hasSize(3);
            assertThat(providers.get(0).getResource()).endsWith("base/");
            assertThat(providers.get(1).getResource()).endsWith("base/valid.txt");
            assertThat(providers.get(2).getResource()).endsWith("base/");
        }
    }

    @Nested
    @DisplayName("Integration")
    class Integration {

        @Test
        @DisplayName("should work with real resource paths")
        void shouldWorkWithRealResourcePaths() {
            // Given - using test resources that actually exist
            Resources resources = new Resources("test-resources", "a.txt", "b.txt", "c.txt");

            // When
            List<ResourceProvider> providers = resources.getResources();

            // Then
            assertThat(providers).hasSize(3);

            // Verify that the providers can actually access resources
            for (ResourceProvider provider : providers) {
                assertThat(provider.getResource()).isNotNull();
                assertThat(provider.getResource()).contains("test-resources/");
            }
        }

        @Test
        @DisplayName("should preserve resource relationships")
        void shouldPreserveResourceRelationships() {
            // Given
            List<String> originalNames = Arrays.asList("first.txt", "second.txt", "third.txt");
            Resources resources = new Resources("base", originalNames);

            // When
            List<ResourceProvider> providers = resources.getResources();

            // Then
            assertThat(providers).hasSize(originalNames.size());
            for (int i = 0; i < originalNames.size(); i++) {
                String expectedPath = "base/" + originalNames.get(i);
                assertThat(providers.get(i).getResource()).endsWith(expectedPath);
            }
        }

        @Test
        @DisplayName("should work with different base folder styles")
        void shouldWorkWithDifferentBaseFolderStyles() {
            // Given
            String[] baseFolders = { "base", "base/", "/base", "/base/" };
            String resource = "test.txt";

            // When/Then - all should result in the same resource path format
            for (String baseFolder : baseFolders) {
                Resources resources = new Resources(baseFolder, resource);
                List<ResourceProvider> providers = resources.getResources();

                assertThat(providers).hasSize(1);
                assertThat(providers.get(0).getResource()).contains(resource);
            }
        }
    }
}
