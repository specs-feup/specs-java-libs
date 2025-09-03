package pt.up.fe.specs.util.utilities;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * Unit tests for {@link TestResources}.
 * 
 * Tests utility for managing test resource paths.
 * 
 * @author Generated Tests
 */
@DisplayName("TestResources")
class TestResourcesTest {

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("should create with base folder")
        void shouldCreateWithBaseFolder() {
            TestResources resources = new TestResources("test/resources");

            assertThat(resources).isNotNull();
        }

        @Test
        @DisplayName("should handle null base folder")
        void shouldHandleNullBaseFolder() {
            assertThatThrownBy(() -> new TestResources(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("should handle empty base folder")
        void shouldHandleEmptyBaseFolder() {
            TestResources resources = new TestResources("");

            assertThat(resources).isNotNull();
        }

        @Test
        @DisplayName("should normalize base folder with trailing slash")
        void shouldNormalizeBaseFolderWithTrailingSlash() {
            TestResources resources1 = new TestResources("test/resources");
            TestResources resources2 = new TestResources("test/resources/");

            // Both should behave the same way - test by checking generated resource paths
            ResourceProvider provider1 = resources1.getResource("test.txt");
            ResourceProvider provider2 = resources2.getResource("test.txt");

            assertThat(provider1.getResource()).isEqualTo(provider2.getResource());
        }
    }

    @Nested
    @DisplayName("Resource Provider Creation")
    class ResourceProviderCreation {

        @Test
        @DisplayName("should create resource provider for file")
        void shouldCreateResourceProviderForFile() {
            TestResources resources = new TestResources("test/resources");

            ResourceProvider provider = resources.getResource("test.txt");

            assertThat(provider).isNotNull();
            assertThat(provider.getResource()).isEqualTo("test/resources/test.txt");
        }

        @Test
        @DisplayName("should handle nested path")
        void shouldHandleNestedPath() {
            TestResources resources = new TestResources("test/resources");

            ResourceProvider provider = resources.getResource("subdir/nested.txt");

            assertThat(provider.getResource()).isEqualTo("test/resources/subdir/nested.txt");
        }

        @Test
        @DisplayName("should handle empty resource file")
        void shouldHandleEmptyResourceFile() {
            TestResources resources = new TestResources("test/resources");

            ResourceProvider provider = resources.getResource("");

            assertThat(provider.getResource()).isEqualTo("test/resources/");
        }

        @Test
        @DisplayName("should handle null resource file")
        void shouldHandleNullResourceFile() {
            TestResources resources = new TestResources("test/resources");

            // The implementation doesn't validate null, it just concatenates
            ResourceProvider provider = resources.getResource(null);
            assertThat(provider.getResource()).isEqualTo("test/resources/null");
        }

        @Test
        @DisplayName("should handle resource file with leading slash")
        void shouldHandleResourceFileWithLeadingSlash() {
            TestResources resources = new TestResources("test/resources");

            ResourceProvider provider = resources.getResource("/test.txt");

            assertThat(provider.getResource()).isEqualTo("test/resources//test.txt");
        }
    }

    @Nested
    @DisplayName("Path Combination")
    class PathCombination {

        @Test
        @DisplayName("should combine simple paths")
        void shouldCombineSimplePaths() {
            TestResources resources = new TestResources("base");

            ResourceProvider provider = resources.getResource("file.txt");

            assertThat(provider.getResource()).isEqualTo("base/file.txt");
        }

        @Test
        @DisplayName("should combine paths with multiple separators")
        void shouldCombinePathsWithMultipleSeparators() {
            TestResources resources = new TestResources("base/path");

            ResourceProvider provider = resources.getResource("sub/file.txt");

            assertThat(provider.getResource()).isEqualTo("base/path/sub/file.txt");
        }

        @Test
        @DisplayName("should handle base folder without slash")
        void shouldHandleBaseFolderWithoutSlash() {
            TestResources resources = new TestResources("base");

            ResourceProvider provider = resources.getResource("file.txt");

            assertThat(provider.getResource()).isEqualTo("base/file.txt");
        }

        @Test
        @DisplayName("should handle base folder with slash")
        void shouldHandleBaseFolderWithSlash() {
            TestResources resources = new TestResources("base/");

            ResourceProvider provider = resources.getResource("file.txt");

            assertThat(provider.getResource()).isEqualTo("base/file.txt");
        }

        @Test
        @DisplayName("should handle multiple consecutive slashes")
        void shouldHandleMultipleConsecutiveSlashes() {
            TestResources resources = new TestResources("base//path//");

            ResourceProvider provider = resources.getResource("//file.txt");

            assertThat(provider.getResource()).isEqualTo("base//path////file.txt");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle root path")
        void shouldHandleRootPath() {
            TestResources resources = new TestResources("/");

            ResourceProvider provider = resources.getResource("file.txt");

            assertThat(provider.getResource()).isEqualTo("/file.txt");
        }

        @Test
        @DisplayName("should handle current directory")
        void shouldHandleCurrentDirectory() {
            TestResources resources = new TestResources(".");

            ResourceProvider provider = resources.getResource("file.txt");

            assertThat(provider.getResource()).isEqualTo("./file.txt");
        }

        @Test
        @DisplayName("should handle parent directory")
        void shouldHandleParentDirectory() {
            TestResources resources = new TestResources("..");

            ResourceProvider provider = resources.getResource("file.txt");

            assertThat(provider.getResource()).isEqualTo("../file.txt");
        }

        @Test
        @DisplayName("should handle special characters in paths")
        void shouldHandleSpecialCharactersInPaths() {
            TestResources resources = new TestResources("test-resources_123");

            ResourceProvider provider = resources.getResource("file@test.txt");

            assertThat(provider.getResource()).isEqualTo("test-resources_123/file@test.txt");
        }

        @Test
        @DisplayName("should handle very long paths")
        void shouldHandleVeryLongPaths() {
            String longBase = "a".repeat(100);
            String longFile = "b".repeat(100) + ".txt";

            TestResources resources = new TestResources(longBase);
            ResourceProvider provider = resources.getResource(longFile);

            assertThat(provider.getResource()).isEqualTo(longBase + "/" + longFile);
        }

        @Test
        @DisplayName("should handle spaces in paths")
        void shouldHandleSpacesInPaths() {
            TestResources resources = new TestResources("test resources");

            ResourceProvider provider = resources.getResource("my file.txt");

            assertThat(provider.getResource()).isEqualTo("test resources/my file.txt");
        }

        @Test
        @DisplayName("should handle unicode characters")
        void shouldHandleUnicodeCharacters() {
            TestResources resources = new TestResources("тест/资源");

            ResourceProvider provider = resources.getResource("файл.txt");

            assertThat(provider.getResource()).isEqualTo("тест/资源/файл.txt");
        }

        @Test
        @DisplayName("should create multiple resource providers")
        void shouldCreateMultipleResourceProviders() {
            TestResources resources = new TestResources("test/resources");

            ResourceProvider provider1 = resources.getResource("file1.txt");
            ResourceProvider provider2 = resources.getResource("file2.txt");
            ResourceProvider provider3 = resources.getResource("subdir/file3.txt");

            assertThat(provider1.getResource()).isEqualTo("test/resources/file1.txt");
            assertThat(provider2.getResource()).isEqualTo("test/resources/file2.txt");
            assertThat(provider3.getResource()).isEqualTo("test/resources/subdir/file3.txt");
        }
    }
}
