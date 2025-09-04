package pt.up.fe.specs.util.providers.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.providers.WebResourceProvider;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link GenericWebResourceProvider}.
 * Tests the implementation of web resource provider functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("GenericWebResourceProvider")
class GenericWebResourceProviderTest {

    private static final String TEST_ROOT_URL = "https://example.com/api";
    private static final String TEST_RESOURCE_URL = "https://example.com/api/resources/file.txt";
    private static final String TEST_VERSION = "1.2.3";

    @Nested
    @DisplayName("Constructor and Basic Properties")
    class ConstructorAndBasicProperties {

        @Test
        @DisplayName("Should create provider with all parameters")
        void shouldCreateProviderWithAllParameters() {
            // When
            GenericWebResourceProvider provider = new GenericWebResourceProvider(
                    TEST_ROOT_URL, TEST_RESOURCE_URL, TEST_VERSION);

            // Then
            assertThat(provider).isNotNull();
            assertThat(provider.rootUrl()).isEqualTo(TEST_ROOT_URL);
            assertThat(provider.resourceUrl()).isEqualTo(TEST_RESOURCE_URL);
            assertThat(provider.version()).isEqualTo(TEST_VERSION);
        }

        @Test
        @DisplayName("Should create provider with null root URL")
        void shouldCreateProviderWithNullRootUrl() {
            // When
            GenericWebResourceProvider provider = new GenericWebResourceProvider(
                    null, TEST_RESOURCE_URL, TEST_VERSION);

            // Then
            assertThat(provider).isNotNull();
            assertThat(provider.rootUrl()).isNull();
            assertThat(provider.resourceUrl()).isEqualTo(TEST_RESOURCE_URL);
            assertThat(provider.version()).isEqualTo(TEST_VERSION);
        }

        @Test
        @DisplayName("Should create provider with null resource URL")
        void shouldCreateProviderWithNullResourceUrl() {
            // When
            GenericWebResourceProvider provider = new GenericWebResourceProvider(
                    TEST_ROOT_URL, null, TEST_VERSION);

            // Then
            assertThat(provider).isNotNull();
            assertThat(provider.rootUrl()).isEqualTo(TEST_ROOT_URL);
            assertThat(provider.resourceUrl()).isNull();
            assertThat(provider.version()).isEqualTo(TEST_VERSION);
        }

        @Test
        @DisplayName("Should create provider with null version")
        void shouldCreateProviderWithNullVersion() {
            // When
            GenericWebResourceProvider provider = new GenericWebResourceProvider(
                    TEST_ROOT_URL, TEST_RESOURCE_URL, null);

            // Then
            assertThat(provider).isNotNull();
            assertThat(provider.rootUrl()).isEqualTo(TEST_ROOT_URL);
            assertThat(provider.resourceUrl()).isEqualTo(TEST_RESOURCE_URL);
            assertThat(provider.version()).isNull();
        }

        @Test
        @DisplayName("Should create provider with all null parameters")
        void shouldCreateProviderWithAllNullParameters() {
            // When
            GenericWebResourceProvider provider = new GenericWebResourceProvider(null, null, null);

            // Then
            assertThat(provider).isNotNull();
            assertThat(provider.rootUrl()).isNull();
            assertThat(provider.resourceUrl()).isNull();
            assertThat(provider.version()).isNull();
        }
    }

    @Nested
    @DisplayName("URL Handling")
    class UrlHandling {

        @Test
        @DisplayName("Should handle empty string URLs")
        void shouldHandleEmptyStringUrls() {
            // When
            GenericWebResourceProvider provider = new GenericWebResourceProvider("", "", "");

            // Then
            assertThat(provider.rootUrl()).isEmpty();
            assertThat(provider.resourceUrl()).isEmpty();
            assertThat(provider.version()).isEmpty();
        }

        @Test
        @DisplayName("Should preserve URL formatting")
        void shouldPreserveUrlFormatting() {
            // Given
            String rootUrl = "https://api.example.com:8080/v1/";
            String resourceUrl = "https://api.example.com:8080/v1/resources/data.json?param=value";
            String version = "v2.1.0-beta";

            // When
            GenericWebResourceProvider provider = new GenericWebResourceProvider(rootUrl, resourceUrl, version);

            // Then
            assertThat(provider.rootUrl()).isEqualTo(rootUrl);
            assertThat(provider.resourceUrl()).isEqualTo(resourceUrl);
            assertThat(provider.version()).isEqualTo(version);
        }

        @Test
        @DisplayName("Should handle special characters in URLs")
        void shouldHandleSpecialCharactersInUrls() {
            // Given
            String rootUrl = "https://example.com/path with spaces/";
            String resourceUrl = "https://example.com/path with spaces/file-name_123.txt";
            String version = "1.0-SNAPSHOT";

            // When
            GenericWebResourceProvider provider = new GenericWebResourceProvider(rootUrl, resourceUrl, version);

            // Then
            assertThat(provider.rootUrl()).isEqualTo(rootUrl);
            assertThat(provider.resourceUrl()).isEqualTo(resourceUrl);
            assertThat(provider.version()).isEqualTo(version);
        }
    }

    @Nested
    @DisplayName("Interface Implementation")
    class InterfaceImplementation {

        private GenericWebResourceProvider provider;

        @BeforeEach
        void setUp() {
            provider = new GenericWebResourceProvider(TEST_ROOT_URL, TEST_RESOURCE_URL, TEST_VERSION);
        }

        @Test
        @DisplayName("Should implement WebResourceProvider interface")
        void shouldImplementWebResourceProviderInterface() {
            // Then
            assertThat(provider).isInstanceOf(WebResourceProvider.class);
        }

        @Test
        @DisplayName("getRootUrl should return constructor value")
        void getRootUrlShouldReturnConstructorValue() {
            // When/Then
            assertThat(provider.rootUrl()).isEqualTo(TEST_ROOT_URL);
        }

        @Test
        @DisplayName("getResourceUrl should return constructor value")
        void getResourceUrlShouldReturnConstructorValue() {
            // When/Then
            assertThat(provider.resourceUrl()).isEqualTo(TEST_RESOURCE_URL);
        }

        @Test
        @DisplayName("getVersion should return constructor value")
        void getVersionShouldReturnConstructorValue() {
            // When/Then
            assertThat(provider.version()).isEqualTo(TEST_VERSION);
        }
    }

    @Nested
    @DisplayName("Immutability")
    class Immutability {

        @Test
        @DisplayName("Provider should be immutable")
        void providerShouldBeImmutable() {
            // Given
            GenericWebResourceProvider provider = new GenericWebResourceProvider(
                    TEST_ROOT_URL, TEST_RESOURCE_URL, TEST_VERSION);

            // When - Get values multiple times
            String rootUrl1 = provider.rootUrl();
            String rootUrl2 = provider.rootUrl();
            String resourceUrl1 = provider.resourceUrl();
            String resourceUrl2 = provider.resourceUrl();
            String version1 = provider.version();
            String version2 = provider.version();

            // Then - Values should be consistent
            assertThat(rootUrl1).isEqualTo(rootUrl2);
            assertThat(resourceUrl1).isEqualTo(resourceUrl2);
            assertThat(version1).isEqualTo(version2);

            // And original values preserved
            assertThat(rootUrl1).isEqualTo(TEST_ROOT_URL);
            assertThat(resourceUrl1).isEqualTo(TEST_RESOURCE_URL);
            assertThat(version1).isEqualTo(TEST_VERSION);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle very long URLs")
        void shouldHandleVeryLongUrls() {
            // Given
            String longUrl = "https://example.com/" + "a".repeat(1000);

            // When
            GenericWebResourceProvider provider = new GenericWebResourceProvider(longUrl, longUrl, "1.0");

            // Then
            assertThat(provider.rootUrl()).isEqualTo(longUrl);
            assertThat(provider.resourceUrl()).isEqualTo(longUrl);
        }

        @Test
        @DisplayName("Should handle URL with international characters")
        void shouldHandleUrlWithInternationalCharacters() {
            // Given
            String internationalUrl = "https://例え.テスト/リソース";

            // When
            GenericWebResourceProvider provider = new GenericWebResourceProvider(
                    internationalUrl, internationalUrl, "テスト");

            // Then
            assertThat(provider.rootUrl()).isEqualTo(internationalUrl);
            assertThat(provider.resourceUrl()).isEqualTo(internationalUrl);
            assertThat(provider.version()).isEqualTo("テスト");
        }
    }

    @Nested
    @DisplayName("Multiple Instance Comparison")
    class MultipleInstanceComparison {

        @Test
        @DisplayName("Different instances with same parameters should have same values")
        void differentInstancesWithSameParametersShouldHaveSameValues() {
            // Given
            GenericWebResourceProvider provider1 = new GenericWebResourceProvider(
                    TEST_ROOT_URL, TEST_RESOURCE_URL, TEST_VERSION);
            GenericWebResourceProvider provider2 = new GenericWebResourceProvider(
                    TEST_ROOT_URL, TEST_RESOURCE_URL, TEST_VERSION);

            // Then
            assertThat(provider1.rootUrl()).isEqualTo(provider2.rootUrl());
            assertThat(provider1.resourceUrl()).isEqualTo(provider2.resourceUrl());
            assertThat(provider1.version()).isEqualTo(provider2.version());
        }

        @Test
        @DisplayName("Instances with different parameters should have different values")
        void instancesWithDifferentParametersShouldHaveDifferentValues() {
            // Given
            GenericWebResourceProvider provider1 = new GenericWebResourceProvider(
                    "https://example.com", "https://example.com/file1.txt", "1.0");
            GenericWebResourceProvider provider2 = new GenericWebResourceProvider(
                    "https://other.com", "https://other.com/file2.txt", "2.0");

            // Then
            assertThat(provider1.rootUrl()).isNotEqualTo(provider2.rootUrl());
            assertThat(provider1.resourceUrl()).isNotEqualTo(provider2.resourceUrl());
            assertThat(provider1.version()).isNotEqualTo(provider2.version());
        }
    }
}
