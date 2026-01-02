package pt.up.fe.specs.util.providers.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.providers.ResourceProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link GenericResource}.
 * Tests the implementation of generic resource provider functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("GenericResource")
class GenericResourceTest {

    private static final String TEST_RESOURCE = "test-resource.txt";
    private static final String TEST_VERSION = "1.2.3";

    @Nested
    @DisplayName("Constructor with Resource Only")
    class ConstructorWithResourceOnly {

        @Test
        @DisplayName("Should create resource with default version")
        void shouldCreateResourceWithDefaultVersion() {
            // When
            GenericResource resource = new GenericResource(TEST_RESOURCE);

            // Then
            assertThat(resource).isNotNull();
            assertThat(resource.getResource()).isEqualTo(TEST_RESOURCE);
            assertThat(resource.version()).isEqualTo(ResourceProvider.getDefaultVersion());
        }

        @Test
        @DisplayName("Should create resource with null resource name")
        void shouldCreateResourceWithNullResourceName() {
            // When
            GenericResource resource = new GenericResource(null);

            // Then
            assertThat(resource).isNotNull();
            assertThat(resource.getResource()).isNull();
            assertThat(resource.version()).isEqualTo(ResourceProvider.getDefaultVersion());
        }

        @Test
        @DisplayName("Should create resource with empty resource name")
        void shouldCreateResourceWithEmptyResourceName() {
            // When
            GenericResource resource = new GenericResource("");

            // Then
            assertThat(resource).isNotNull();
            assertThat(resource.getResource()).isEmpty();
            assertThat(resource.version()).isEqualTo(ResourceProvider.getDefaultVersion());
        }
    }

    @Nested
    @DisplayName("Constructor with Resource and Version")
    class ConstructorWithResourceAndVersion {

        @Test
        @DisplayName("Should create resource with specified version")
        void shouldCreateResourceWithSpecifiedVersion() {
            // When
            GenericResource resource = new GenericResource(TEST_RESOURCE, TEST_VERSION);

            // Then
            assertThat(resource).isNotNull();
            assertThat(resource.getResource()).isEqualTo(TEST_RESOURCE);
            assertThat(resource.version()).isEqualTo(TEST_VERSION);
        }

        @Test
        @DisplayName("Should create resource with null resource and version")
        void shouldCreateResourceWithNullResourceAndVersion() {
            // When
            GenericResource resource = new GenericResource(null, null);

            // Then
            assertThat(resource).isNotNull();
            assertThat(resource.getResource()).isNull();
            assertThat(resource.version()).isNull();
        }

        @Test
        @DisplayName("Should create resource with null version")
        void shouldCreateResourceWithNullVersion() {
            // When
            GenericResource resource = new GenericResource(TEST_RESOURCE, null);

            // Then
            assertThat(resource).isNotNull();
            assertThat(resource.getResource()).isEqualTo(TEST_RESOURCE);
            assertThat(resource.version()).isNull();
        }

        @Test
        @DisplayName("Should create resource with empty strings")
        void shouldCreateResourceWithEmptyStrings() {
            // When
            GenericResource resource = new GenericResource("", "");

            // Then
            assertThat(resource).isNotNull();
            assertThat(resource.getResource()).isEmpty();
            assertThat(resource.version()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Interface Implementation")
    class InterfaceImplementation {

        private GenericResource resource;

        @BeforeEach
        void setUp() {
            resource = new GenericResource(TEST_RESOURCE, TEST_VERSION);
        }

        @Test
        @DisplayName("Should implement ResourceProvider interface")
        void shouldImplementResourceProviderInterface() {
            // Then
            assertThat(resource).isInstanceOf(ResourceProvider.class);
        }

        @Test
        @DisplayName("getResource should return constructor value")
        void getResourceShouldReturnConstructorValue() {
            // When/Then
            assertThat(resource.getResource()).isEqualTo(TEST_RESOURCE);
        }

        @Test
        @DisplayName("getVersion should return constructor value")
        void getVersionShouldReturnConstructorValue() {
            // When/Then
            assertThat(resource.version()).isEqualTo(TEST_VERSION);
        }
    }

    @Nested
    @DisplayName("Default Version Behavior")
    class DefaultVersionBehavior {

        @Test
        @DisplayName("Should use default version from ResourceProvider")
        void shouldUseDefaultVersionFromResourceProvider() {
            // Given
            String defaultVersion = ResourceProvider.getDefaultVersion();

            // When
            GenericResource resource = new GenericResource(TEST_RESOURCE);

            // Then
            assertThat(resource.version()).isEqualTo(defaultVersion);
        }

        @Test
        @DisplayName("Constructor with version should override default")
        void constructorWithVersionShouldOverrideDefault() {
            // Given
            String customVersion = "custom-version";

            // When
            GenericResource resource = new GenericResource(TEST_RESOURCE, customVersion);

            // Then
            assertThat(resource.version()).isEqualTo(customVersion);
            assertThat(resource.version()).isNotEqualTo(ResourceProvider.getDefaultVersion());
        }
    }

    @Nested
    @DisplayName("Resource Name Handling")
    class ResourceNameHandling {

        @Test
        @DisplayName("Should handle resource names with paths")
        void shouldHandleResourceNamesWithPaths() {
            // Given
            String resourceWithPath = "path/to/resource.txt";

            // When
            GenericResource resource = new GenericResource(resourceWithPath);

            // Then
            assertThat(resource.getResource()).isEqualTo(resourceWithPath);
        }

        @Test
        @DisplayName("Should handle resource names with special characters")
        void shouldHandleResourceNamesWithSpecialCharacters() {
            // Given
            String resourceWithSpecialChars = "resource-name_123.file.txt";

            // When
            GenericResource resource = new GenericResource(resourceWithSpecialChars);

            // Then
            assertThat(resource.getResource()).isEqualTo(resourceWithSpecialChars);
        }

        @Test
        @DisplayName("Should handle very long resource names")
        void shouldHandleVeryLongResourceNames() {
            // Given
            String longResourceName = "resource-" + "a".repeat(1000) + ".txt";

            // When
            GenericResource resource = new GenericResource(longResourceName);

            // Then
            assertThat(resource.getResource()).isEqualTo(longResourceName);
        }

        @Test
        @DisplayName("Should handle resource names with international characters")
        void shouldHandleResourceNamesWithInternationalCharacters() {
            // Given
            String internationalResource = "リソース-資源.txt";

            // When
            GenericResource resource = new GenericResource(internationalResource);

            // Then
            assertThat(resource.getResource()).isEqualTo(internationalResource);
        }
    }

    @Nested
    @DisplayName("Version Handling")
    class VersionHandling {

        @Test
        @DisplayName("Should handle semantic versions")
        void shouldHandleSemanticVersions() {
            // Given
            String semanticVersion = "1.2.3-beta.1+build.123";

            // When
            GenericResource resource = new GenericResource(TEST_RESOURCE, semanticVersion);

            // Then
            assertThat(resource.version()).isEqualTo(semanticVersion);
        }

        @Test
        @DisplayName("Should handle timestamp versions")
        void shouldHandleTimestampVersions() {
            // Given
            String timestampVersion = "20231225-143022";

            // When
            GenericResource resource = new GenericResource(TEST_RESOURCE, timestampVersion);

            // Then
            assertThat(resource.version()).isEqualTo(timestampVersion);
        }

        @Test
        @DisplayName("Should handle git hash versions")
        void shouldHandleGitHashVersions() {
            // Given
            String gitHashVersion = "abc123def456";

            // When
            GenericResource resource = new GenericResource(TEST_RESOURCE, gitHashVersion);

            // Then
            assertThat(resource.version()).isEqualTo(gitHashVersion);
        }
    }

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("Resource should be immutable")
        void resourceShouldBeImmutable() {
            // Given
            GenericResource resource = new GenericResource(TEST_RESOURCE, TEST_VERSION);

            // When - Get values multiple times
            String resource1 = resource.getResource();
            String resource2 = resource.getResource();
            String version1 = resource.version();
            String version2 = resource.version();

            // Then - Values should be consistent
            assertThat(resource1).isEqualTo(resource2);
            assertThat(version1).isEqualTo(version2);

            // And original values preserved
            assertThat(resource1).isEqualTo(TEST_RESOURCE);
            assertThat(version1).isEqualTo(TEST_VERSION);
        }
    }

    @Nested
    @DisplayName("Multiple Instance Comparison")
    class MultipleInstanceComparison {

        @Test
        @DisplayName("Different instances with same parameters should have same values")
        void differentInstancesWithSameParametersShouldHaveSameValues() {
            // Given
            GenericResource resource1 = new GenericResource(TEST_RESOURCE, TEST_VERSION);
            GenericResource resource2 = new GenericResource(TEST_RESOURCE, TEST_VERSION);

            // Then
            assertThat(resource1.getResource()).isEqualTo(resource2.getResource());
            assertThat(resource1.version()).isEqualTo(resource2.version());
        }

        @Test
        @DisplayName("Instances with different parameters should have different values")
        void instancesWithDifferentParametersShouldHaveDifferentValues() {
            // Given
            GenericResource resource1 = new GenericResource("resource1.txt", "1.0");
            GenericResource resource2 = new GenericResource("resource2.txt", "2.0");

            // Then
            assertThat(resource1.getResource()).isNotEqualTo(resource2.getResource());
            assertThat(resource1.version()).isNotEqualTo(resource2.version());
        }

        @Test
        @DisplayName("Single param constructor should use default version consistently")
        void singleParamConstructorShouldUseDefaultVersionConsistently() {
            // Given
            GenericResource resource1 = new GenericResource(TEST_RESOURCE);
            GenericResource resource2 = new GenericResource(TEST_RESOURCE);

            // Then
            assertThat(resource1.getResource()).isEqualTo(resource2.getResource());
            assertThat(resource1.version()).isEqualTo(resource2.version());
            assertThat(resource1.version()).isEqualTo(ResourceProvider.getDefaultVersion());
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle whitespace-only resource names")
        void shouldHandleWhitespaceOnlyResourceNames() {
            // Given
            String whitespaceResource = "   ";

            // When
            GenericResource resource = new GenericResource(whitespaceResource);

            // Then
            assertThat(resource.getResource()).isEqualTo(whitespaceResource);
        }

        @Test
        @DisplayName("Should handle whitespace-only versions")
        void shouldHandleWhitespaceOnlyVersions() {
            // Given
            String whitespaceVersion = "   ";

            // When
            GenericResource resource = new GenericResource(TEST_RESOURCE, whitespaceVersion);

            // Then
            assertThat(resource.version()).isEqualTo(whitespaceVersion);
        }

        @Test
        @DisplayName("Should handle resource names with line breaks")
        void shouldHandleResourceNamesWithLineBreaks() {
            // Given
            String resourceWithLineBreaks = "line1\nline2\rline3";

            // When
            GenericResource resource = new GenericResource(resourceWithLineBreaks);

            // Then
            assertThat(resource.getResource()).isEqualTo(resourceWithLineBreaks);
        }
    }
}
