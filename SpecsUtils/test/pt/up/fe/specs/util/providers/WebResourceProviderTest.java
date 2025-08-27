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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;

import pt.up.fe.specs.util.SpecsIo;

/**
 * Unit tests for WebResourceProvider interface and its implementations.
 * 
 * @author Generated Tests
 */
@DisplayName("WebResourceProvider")
class WebResourceProviderTest {

    @TempDir
    Path tempDir;

    private WebResourceProvider testProvider;
    private String rootUrl;
    private String resourceUrl;
    private String version;

    @BeforeEach
    void setUp() {
        rootUrl = "https://example.com/api";
        resourceUrl = "resources/test.jar";
        version = "2.1.0";
        testProvider = WebResourceProvider.newInstance(rootUrl, resourceUrl, version);
    }

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("should have correct interface methods")
        void shouldHaveCorrectInterfaceMethods() {
            assertThatCode(() -> {
                WebResourceProvider.class.getMethod("getResourceUrl");
                WebResourceProvider.class.getMethod("getRootUrl");
                WebResourceProvider.class.getMethod("getUrlString");
                WebResourceProvider.class.getMethod("getUrlString", String.class);
                WebResourceProvider.class.getMethod("getUrl");
                WebResourceProvider.class.getMethod("getVersion");
                WebResourceProvider.class.getMethod("getFilename");
                WebResourceProvider.class.getMethod("write", File.class);
                WebResourceProvider.class.getMethod("createResourceVersion", String.class);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should extend FileResourceProvider")
        void shouldExtendFileResourceProvider() {
            assertThat(FileResourceProvider.class.isAssignableFrom(WebResourceProvider.class)).isTrue();
        }

        @Test
        @DisplayName("should be an interface")
        void shouldBeAnInterface() {
            assertThat(WebResourceProvider.class.isInterface()).isTrue();
        }
    }

    @Nested
    @DisplayName("Static Factory Methods")
    class StaticFactoryMethods {

        @Test
        @DisplayName("should create instance from root URL and resource URL")
        void shouldCreateInstanceFromRootUrlAndResourceUrl() {
            WebResourceProvider provider = WebResourceProvider.newInstance(rootUrl, resourceUrl);

            assertThat(provider).isNotNull();
            assertThat(provider.getRootUrl()).isEqualTo(rootUrl);
            assertThat(provider.getResourceUrl()).isEqualTo(resourceUrl);
        }

        @Test
        @DisplayName("should create instance with version")
        void shouldCreateInstanceWithVersion() {
            WebResourceProvider provider = WebResourceProvider.newInstance(rootUrl, resourceUrl, version);

            assertThat(provider).isNotNull();
            assertThat(provider.getRootUrl()).isEqualTo(rootUrl);
            assertThat(provider.getResourceUrl()).isEqualTo(resourceUrl);
            assertThat(provider.getVersion()).isEqualTo(version);
        }

        @Test
        @DisplayName("should handle null root URL")
        void shouldHandleNullRootUrl() {
            WebResourceProvider provider = WebResourceProvider.newInstance(null, resourceUrl);

            assertThat(provider.getRootUrl()).isNull();
            assertThat(provider.getResourceUrl()).isEqualTo(resourceUrl);
        }

        @Test
        @DisplayName("should handle null resource URL")
        void shouldHandleNullResourceUrl() {
            WebResourceProvider provider = WebResourceProvider.newInstance(rootUrl, null);

            assertThat(provider.getRootUrl()).isEqualTo(rootUrl);
            assertThat(provider.getResourceUrl()).isNull();
        }

        @Test
        @DisplayName("should handle null version")
        void shouldHandleNullVersion() {
            WebResourceProvider provider = WebResourceProvider.newInstance(rootUrl, resourceUrl, null);

            assertThat(provider.getRootUrl()).isEqualTo(rootUrl);
            assertThat(provider.getResourceUrl()).isEqualTo(resourceUrl);
        }
    }

    @Nested
    @DisplayName("URL Construction")
    class UrlConstruction {

        @Test
        @DisplayName("should construct URL string correctly")
        void shouldConstructUrlStringCorrectly() {
            String expectedUrl = "https://example.com/api/resources/test.jar";

            assertThat(testProvider.getUrlString()).isEqualTo(expectedUrl);
        }

        @Test
        @DisplayName("should add trailing slash to root URL if missing")
        void shouldAddTrailingSlashToRootUrlIfMissing() {
            String rootWithoutSlash = "https://example.com/api";
            WebResourceProvider provider = WebResourceProvider.newInstance(rootWithoutSlash, "file.txt");

            assertThat(provider.getUrlString()).isEqualTo("https://example.com/api/file.txt");
        }

        @Test
        @DisplayName("should not add extra slash if root URL already has trailing slash")
        void shouldNotAddExtraSlashIfRootUrlAlreadyHasTrailingSlash() {
            String rootWithSlash = "https://example.com/api/";
            WebResourceProvider provider = WebResourceProvider.newInstance(rootWithSlash, "file.txt");

            assertThat(provider.getUrlString()).isEqualTo("https://example.com/api/file.txt");
        }

        @Test
        @DisplayName("should construct URL string with custom root URL")
        void shouldConstructUrlStringWithCustomRootUrl() {
            String customRoot = "https://custom.com/api/";
            String expectedUrl = "https://custom.com/api/resources/test.jar";

            assertThat(testProvider.getUrlString(customRoot)).isEqualTo(expectedUrl);
        }

        @Test
        @DisplayName("should handle empty root URL")
        void shouldHandleEmptyRootUrl() {
            WebResourceProvider provider = WebResourceProvider.newInstance("", "file.txt");

            assertThat(provider.getUrlString()).isEqualTo("/file.txt");
        }

        @Test
        @DisplayName("should handle empty resource URL")
        void shouldHandleEmptyResourceUrl() {
            WebResourceProvider provider = WebResourceProvider.newInstance(rootUrl, "");

            assertThat(provider.getUrlString()).isEqualTo("https://example.com/api/");
        }
    }

    @Nested
    @DisplayName("URL Object Creation")
    class UrlObjectCreation {

        @Test
        @DisplayName("should create valid URL object")
        void shouldCreateValidUrlObject() throws MalformedURLException {
            URL url = testProvider.getUrl();

            assertThat(url).isNotNull();
            assertThat(url.toString()).isEqualTo("https://example.com/api/resources/test.jar");
        }

        @Test
        @DisplayName("should throw RuntimeException for malformed URL")
        void shouldThrowRuntimeExceptionForMalformedUrl() {
            WebResourceProvider invalidProvider = WebResourceProvider.newInstance("not-a-valid-url", "file.txt");

            assertThatThrownBy(() -> invalidProvider.getUrl())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Could not transform url String into URL");
        }

        @Test
        @DisplayName("should handle complex URLs with query parameters")
        void shouldHandleComplexUrlsWithQueryParameters() throws MalformedURLException {
            WebResourceProvider provider = WebResourceProvider.newInstance(
                    "https://api.github.com/repos/user/repo/releases/download/v1.0",
                    "artifact.jar?token=abc123");

            URL url = provider.getUrl();
            assertThat(url.toString()).contains("token=abc123");
        }

        @Test
        @DisplayName("should handle URLs with ports")
        void shouldHandleUrlsWithPorts() throws MalformedURLException {
            WebResourceProvider provider = WebResourceProvider.newInstance("http://localhost:8080", "file.txt");

            URL url = provider.getUrl();
            assertThat(url.getPort()).isEqualTo(8080);
            assertThat(url.toString()).isEqualTo("http://localhost:8080/file.txt");
        }
    }

    @Nested
    @DisplayName("Version and Filename")
    class VersionAndFilename {

        @Test
        @DisplayName("should return correct version")
        void shouldReturnCorrectVersion() {
            assertThat(testProvider.getVersion()).isEqualTo(version);
        }

        @Test
        @DisplayName("should return default version when not specified")
        void shouldReturnDefaultVersionWhenNotSpecified() {
            WebResourceProvider defaultProvider = WebResourceProvider.newInstance(rootUrl, resourceUrl);

            assertThat(defaultProvider.getVersion()).isEqualTo("1.0");
        }

        @Test
        @DisplayName("should extract filename from URL")
        void shouldExtractFilenameFromUrl() {
            assertThat(testProvider.getFilename()).isEqualTo("test.jar");
        }

        @Test
        @DisplayName("should handle URL without filename")
        void shouldHandleUrlWithoutFilename() {
            WebResourceProvider provider = WebResourceProvider.newInstance(rootUrl, "");

            assertThat(provider.getFilename()).isEmpty();
        }

        @Test
        @DisplayName("should handle URL with query parameters in filename")
        void shouldHandleUrlWithQueryParametersInFilename() {
            WebResourceProvider provider = WebResourceProvider.newInstance(rootUrl, "file.jar?version=1.0");

            assertThat(provider.getFilename()).isEqualTo("file.jar?version=1.0");
        }

        @Test
        @DisplayName("should handle simple resource name without path")
        void shouldHandleSimpleResourceNameWithoutPath() {
            WebResourceProvider provider = WebResourceProvider.newInstance(rootUrl, "simple.txt");

            assertThat(provider.getFilename()).isEqualTo("simple.txt");
        }

        @Test
        @DisplayName("should handle deep nested paths")
        void shouldHandleDeepNestedPaths() {
            WebResourceProvider provider = WebResourceProvider.newInstance(rootUrl, "very/deep/nested/path/file.txt");

            assertThat(provider.getFilename()).isEqualTo("file.txt");
        }
    }

    @Nested
    @DisplayName("File Download")
    class FileDownload {

        @Test
        @DisplayName("should download file to specified folder")
        void shouldDownloadFileToSpecifiedFolder() {
            File testFile = tempDir.resolve("downloaded.jar").toFile();

            try (MockedStatic<SpecsIo> mockedSpecsIo = mockStatic(SpecsIo.class)) {
                mockedSpecsIo.when(() -> SpecsIo.download(anyString(), any(File.class)))
                        .thenReturn(testFile);

                File result = testProvider.write(tempDir.toFile());

                assertThat(result).isEqualTo(testFile);
            }
        }

        @Test
        @DisplayName("should throw exception when download fails")
        void shouldThrowExceptionWhenDownloadFails() {
            try (MockedStatic<SpecsIo> mockedSpecsIo = mockStatic(SpecsIo.class)) {
                mockedSpecsIo.when(() -> SpecsIo.download(anyString(), any(File.class)))
                        .thenReturn(null);

                assertThatThrownBy(() -> testProvider.write(tempDir.toFile()))
                        .isInstanceOf(RuntimeException.class)
                        .hasMessageContaining("Could not download file from URL");
            }
        }

        @Test
        @DisplayName("should pass correct URL to download method")
        void shouldPassCorrectUrlToDownloadMethod() {
            File testFile = tempDir.resolve("test.jar").toFile();
            String expectedUrl = "https://example.com/api/resources/test.jar";

            try (MockedStatic<SpecsIo> mockedSpecsIo = mockStatic(SpecsIo.class)) {
                mockedSpecsIo.when(() -> SpecsIo.download(expectedUrl, tempDir.toFile()))
                        .thenReturn(testFile);

                File result = testProvider.write(tempDir.toFile());

                assertThat(result).isEqualTo(testFile);
                mockedSpecsIo.verify(() -> SpecsIo.download(expectedUrl, tempDir.toFile()));
            }
        }

        @Test
        @DisplayName("should handle download with special characters in URL")
        void shouldHandleDownloadWithSpecialCharactersInUrl() {
            WebResourceProvider provider = WebResourceProvider.newInstance(
                    "https://example.com/api",
                    "special%20file%20name.jar");
            File testFile = tempDir.resolve("special file name.jar").toFile();

            try (MockedStatic<SpecsIo> mockedSpecsIo = mockStatic(SpecsIo.class)) {
                mockedSpecsIo.when(() -> SpecsIo.download(anyString(), any(File.class)))
                        .thenReturn(testFile);

                File result = provider.write(tempDir.toFile());

                assertThat(result).isEqualTo(testFile);
            }
        }
    }

    @Nested
    @DisplayName("Version Creation")
    class VersionCreation {

        @Test
        @DisplayName("should create resource version with suffix")
        void shouldCreateResourceVersionWithSuffix() {
            WebResourceProvider versionedProvider = testProvider.createResourceVersion("_v3.0");

            assertThat(versionedProvider).isNotNull();
            assertThat(versionedProvider.getRootUrl()).isEqualTo(testProvider.getRootUrl());
            assertThat(versionedProvider.getResourceUrl()).isEqualTo("resources/test_v3.0.jar");
            assertThat(versionedProvider.getVersion()).isEqualTo("_v3.0");
        }

        @Test
        @DisplayName("should handle version creation for files without extension")
        void shouldHandleVersionCreationForFilesWithoutExtension() {
            WebResourceProvider provider = WebResourceProvider.newInstance(rootUrl, "resources/executable");
            WebResourceProvider versionedProvider = provider.createResourceVersion("_v2");

            assertThat(versionedProvider.getResourceUrl()).isEqualTo("resources/executable_v2");
        }

        @Test
        @DisplayName("should handle version creation with null version")
        void shouldHandleVersionCreationWithNullVersion() {
            WebResourceProvider versionedProvider = testProvider.createResourceVersion(null);

            assertThat(versionedProvider).isNotNull();
            assertThat(versionedProvider.getResourceUrl()).isEqualTo("resources/testnull.jar");
        }

        @Test
        @DisplayName("should handle version creation with empty version")
        void shouldHandleVersionCreationWithEmptyVersion() {
            WebResourceProvider versionedProvider = testProvider.createResourceVersion("");

            assertThat(versionedProvider).isNotNull();
            assertThat(versionedProvider.getResourceUrl()).isEqualTo("resources/test.jar");
        }

        @Test
        @DisplayName("should preserve root URL in versioned provider")
        void shouldPreserveRootUrlInVersionedProvider() {
            WebResourceProvider versionedProvider = testProvider.createResourceVersion("_new");

            assertThat(versionedProvider.getRootUrl()).isEqualTo(testProvider.getRootUrl());
        }

        @Test
        @DisplayName("should handle complex extensions in version creation")
        void shouldHandleComplexExtensionsInVersionCreation() {
            WebResourceProvider provider = WebResourceProvider.newInstance(rootUrl, "archive.tar.gz");
            WebResourceProvider versionedProvider = provider.createResourceVersion("_v2");

            // SpecsIo.removeExtension only removes the last extension (.gz), leaving
            // archive.tar
            assertThat(versionedProvider.getResourceUrl()).isEqualTo("archive.tar_v2.gz");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle very long URLs")
        void shouldHandleVeryLongUrls() {
            StringBuilder longPath = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                longPath.append("very/long/path/segment/");
            }
            longPath.append("file.txt");

            WebResourceProvider provider = WebResourceProvider.newInstance(rootUrl, longPath.toString());

            assertThat(provider.getResourceUrl()).isEqualTo(longPath.toString());
            assertThat(provider.getFilename()).isEqualTo("file.txt");
        }

        @Test
        @DisplayName("should handle URLs with unicode characters")
        void shouldHandleUrlsWithUnicodeCharacters() {
            WebResourceProvider provider = WebResourceProvider.newInstance(
                    "https://example.com/资源",
                    "文件.txt");

            assertThat(provider.getRootUrl()).contains("资源");
            assertThat(provider.getResourceUrl()).isEqualTo("文件.txt");
            assertThat(provider.getFilename()).isEqualTo("文件.txt");
        }

        @Test
        @DisplayName("should handle URLs with special protocols")
        void shouldHandleUrlsWithSpecialProtocols() {
            WebResourceProvider ftpProvider = WebResourceProvider.newInstance("ftp://ftp.example.com", "file.txt");
            WebResourceProvider httpsProvider = WebResourceProvider.newInstance("https://secure.example.com",
                    "file.txt");

            assertThat(ftpProvider.getUrlString()).startsWith("ftp://");
            assertThat(httpsProvider.getUrlString()).startsWith("https://");
        }

        @Test
        @DisplayName("should handle multiple consecutive slashes")
        void shouldHandleMultipleConsecutiveSlashes() {
            WebResourceProvider provider = WebResourceProvider.newInstance(
                    "https://example.com//api//",
                    "//resources//file.txt");

            assertThat(provider.getUrlString()).contains("//api//");
            assertThat(provider.getResourceUrl()).contains("//resources//");
        }
    }

    @Nested
    @DisplayName("Polymorphism")
    class Polymorphism {

        @Test
        @DisplayName("should work as FileResourceProvider")
        void shouldWorkAsFileResourceProvider() {
            FileResourceProvider fileProvider = testProvider;

            assertThat(fileProvider).isNotNull();
            assertThat(fileProvider.getFilename()).isEqualTo("test.jar");
            assertThat(fileProvider.getVersion()).isEqualTo(version);
        }

        @Test
        @DisplayName("should support interface-based programming")
        void shouldSupportInterfaceBasedProgramming() {
            java.util.List<WebResourceProvider> providers = java.util.Arrays.asList(
                    WebResourceProvider.newInstance("http://example1.com", "file1.txt"),
                    WebResourceProvider.newInstance("http://example2.com", "file2.txt"),
                    WebResourceProvider.newInstance("http://example3.com", "file3.txt"));

            assertThat(providers)
                    .extracting(WebResourceProvider::getFilename)
                    .containsExactly("file1.txt", "file2.txt", "file3.txt");
        }

        @Test
        @DisplayName("should work with mocked implementations")
        void shouldWorkWithMockedImplementations() {
            WebResourceProvider mockProvider = mock(WebResourceProvider.class);
            when(mockProvider.getRootUrl()).thenReturn("http://mock.com");
            when(mockProvider.getResourceUrl()).thenReturn("mock.jar");
            when(mockProvider.getFilename()).thenReturn("mock.jar");
            when(mockProvider.getVersion()).thenReturn("mock-version");

            assertThat(mockProvider.getRootUrl()).isEqualTo("http://mock.com");
            assertThat(mockProvider.getResourceUrl()).isEqualTo("mock.jar");
            assertThat(mockProvider.getFilename()).isEqualTo("mock.jar");
            assertThat(mockProvider.getVersion()).isEqualTo("mock-version");
        }
    }

    @Nested
    @DisplayName("Integration with FileResourceProvider")
    class IntegrationWithFileResourceProvider {

        @Test
        @DisplayName("should implement all FileResourceProvider methods")
        void shouldImplementAllFileResourceProviderMethods() {
            assertThatCode(() -> {
                testProvider.getFilename();
                testProvider.getVersion();
                testProvider.createResourceVersion("test");
                // write method tested separately due to I/O nature
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should have consistent behavior with FileResourceProvider contract")
        void shouldHaveConsistentBehaviorWithFileResourceProviderContract() {
            FileResourceProvider asFileProvider = testProvider;

            assertThat(asFileProvider.getFilename()).isEqualTo(testProvider.getFilename());
            assertThat(asFileProvider.getVersion()).isEqualTo(testProvider.getVersion());
        }
    }
}
