package pt.up.fe.specs.git;

import static org.assertj.core.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for {@link SpecsGit} utility class.
 * Tests static utility methods for Git operations, focusing on URL parsing and
 * utility methods.
 * 
 * @author Generated Tests
 */
class SpecsGitTest {

    @TempDir
    File tempDir;

    @Test
    void testGetRepoNameWithGitExtension() {
        // Test repository name extraction from HTTPS URL with .git extension
        String url = "https://github.com/user/myrepo.git";

        String result = SpecsGit.getRepoName(url);

        assertThat(result).isEqualTo("myrepo");
    }

    @Test
    void testGetRepoNameWithoutGitExtension() {
        // Test repository name extraction from HTTPS URL without .git extension
        String url = "https://github.com/user/myrepo";

        String result = SpecsGit.getRepoName(url);

        assertThat(result).isEqualTo("myrepo");
    }

    @Test
    void testGetRepoNameWithSubPath() {
        // Test repository name extraction with organization/user path
        String url = "https://github.com/organization/user/myrepo.git";

        String result = SpecsGit.getRepoName(url);

        assertThat(result).isEqualTo("myrepo");
    }

    @Test
    void testGetRepoNameSSHUrl() {
        // Test repository name extraction from SSH URL - SSH URLs cause
        // URISyntaxException
        String url = "git@github.com:user/myrepo.git";

        assertThatThrownBy(() -> SpecsGit.getRepoName(url))
                .isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(java.net.URISyntaxException.class);
    }

    @Test
    void testGetRepoNameLocalPath() {
        // Test repository name extraction from local file path
        String url = "file:///home/user/repos/myrepo.git";

        String result = SpecsGit.getRepoName(url);

        assertThat(result).isEqualTo("myrepo");
    }

    @Test
    void testGetRepoNameWithQueryParameters() {
        // Test repository name extraction when URL has query parameters
        String url = "https://github.com/user/myrepo.git?branch=main&token=abc";

        String result = SpecsGit.getRepoName(url);

        assertThat(result).isEqualTo("myrepo");
    }

    @Test
    void testGetRepoNameComplexPath() {
        // Test repository name extraction from complex path
        String url = "https://gitlab.example.com/group/subgroup/project/myrepo.git";

        String result = SpecsGit.getRepoName(url);

        assertThat(result).isEqualTo("myrepo");
    }

    @Test
    void testNormalizeTagWithPrefix() {
        // Test tag normalization when tag already has refs/tags/ prefix
        String tag = "refs/tags/v1.0.0";

        String result = SpecsGit.normalizeTag(tag);

        assertThat(result).isEqualTo("refs/tags/v1.0.0");
    }

    @Test
    void testNormalizeTagWithoutPrefix() {
        // Test tag normalization when tag doesn't have refs/tags/ prefix
        String tag = "v1.0.0";

        String result = SpecsGit.normalizeTag(tag);

        assertThat(result).isEqualTo("refs/tags/v1.0.0");
    }

    @Test
    void testNormalizeTagEmpty() {
        // Test tag normalization with empty tag
        String tag = "";

        String result = SpecsGit.normalizeTag(tag);

        assertThat(result).isEqualTo("refs/tags/");
    }

    @Test
    void testNormalizeTagNull() {
        // Test tag normalization with null tag
        assertThatThrownBy(() -> SpecsGit.normalizeTag(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testGetRepositoriesFolder() {
        // Test that repositories folder is created under temp directory
        File result = SpecsGit.getRepositoriesFolder();

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("specs_git_repos");
        assertThat(result.getParentFile()).isNotNull();
    }

    @Test
    void testGetCredentialsNoLogin() {
        // Test credential extraction from URL without login information
        String url = "https://github.com/user/repo.git";

        var result = SpecsGit.getCredentials(url);

        assertThat(result).isNull();
    }

    @Test
    void testGetCredentialsWithLoginAndPassword() {
        // Test credential extraction from URL with login and password
        String url = "https://user:pass@github.com/user/repo.git";

        var result = SpecsGit.getCredentials(url);

        assertThat(result).isNotNull();
        assertThat(result).isInstanceOf(org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider.class);
    }

    @Test
    void testGetCredentialsWithHttpPrefix() {
        // Test credential extraction from URL with http prefix
        String url = "http://user:pass@github.com/user/repo.git";

        var result = SpecsGit.getCredentials(url);

        assertThat(result).isNotNull();
    }

    @Test
    void testGetCredentialsWithOnlyLogin() {
        // Test credential extraction from URL with only login (no password)
        // This should return null because console is not available in tests
        String url = "https://user@github.com/user/repo.git";

        var result = SpecsGit.getCredentials(url);

        assertThat(result).isNull(); // Console not available in test environment
    }

    @Test
    void testGetCredentialsComplexUrl() {
        // Test credential extraction from complex URL
        String url = "https://user:pass@gitlab.example.com:8080/group/repo.git?branch=main";

        var result = SpecsGit.getCredentials(url);

        assertThat(result).isNotNull();
    }

    @Test
    void testGetRepoNameEdgeCases() {
        // Test various edge cases for repository name extraction

        // Single character repo name
        assertThat(SpecsGit.getRepoName("https://github.com/user/a.git")).isEqualTo("a");

        // Repo name with numbers
        assertThat(SpecsGit.getRepoName("https://github.com/user/repo123.git")).isEqualTo("repo123");

        // Repo name with hyphens
        assertThat(SpecsGit.getRepoName("https://github.com/user/my-repo.git")).isEqualTo("my-repo");

        // Repo name with underscores
        assertThat(SpecsGit.getRepoName("https://github.com/user/my_repo.git")).isEqualTo("my_repo");

        // Repo name with dots
        assertThat(SpecsGit.getRepoName("https://github.com/user/my.repo.git")).isEqualTo("my.repo");
    }

    @Test
    void testGetRepoNameInvalidUrl() {
        // Test repository name extraction from invalid URL
        // Based on implementation, it seems invalid URLs might not throw exceptions in
        // all cases
        // Let's test what actually happens
        String invalidUrl = "not-a-valid-url";

        try {
            String result = SpecsGit.getRepoName(invalidUrl);
            // If no exception, the result should be the input or some processing of it
            assertThat(result).isNotNull();
        } catch (RuntimeException e) {
            // If exception is thrown, it should be due to URISyntaxException
            assertThat(e).hasCauseInstanceOf(java.net.URISyntaxException.class);
        }
    }

    @Test
    void testNormalizeTagVariations() {
        // Test various tag normalization scenarios

        assertThat(SpecsGit.normalizeTag("1.0")).isEqualTo("refs/tags/1.0");
        assertThat(SpecsGit.normalizeTag("release-1.0")).isEqualTo("refs/tags/release-1.0");
        assertThat(SpecsGit.normalizeTag("refs/tags/v1.0")).isEqualTo("refs/tags/v1.0");
        assertThat(SpecsGit.normalizeTag("refs/tags/")).isEqualTo("refs/tags/");

        // Test with partial prefix
        assertThat(SpecsGit.normalizeTag("refs/")).isEqualTo("refs/tags/refs/");
        assertThat(SpecsGit.normalizeTag("tags/v1.0")).isEqualTo("refs/tags/tags/v1.0");
    }

    @Test
    void testGetCredentialsVariousFormats() {
        // Test credential extraction from various URL formats

        // No credentials
        assertThat(SpecsGit.getCredentials("https://github.com/user/repo")).isNull();
        assertThat(SpecsGit.getCredentials("git@github.com:user/repo.git")).isNull();

        // With credentials
        assertThat(SpecsGit.getCredentials("https://user:pass@github.com/user/repo")).isNotNull();
        assertThat(SpecsGit.getCredentials("http://user:pass@localhost/repo")).isNotNull();

        // Edge case: multiple @ symbols
        assertThat(SpecsGit.getCredentials("https://user@domain:pass@github.com/repo")).isNotNull();
    }

    @Test
    void testUrlParsingMethods() {
        // Test that URL parsing methods handle typical Git URL formats correctly
        // Note: SSH URLs will fail due to URI parsing limitations

        String[] testUrls = {
                "https://github.com/user/repo.git",
                "https://gitlab.com/group/subgroup/repo.git",
                "file:///local/path/repo.git"
        };

        for (String url : testUrls) {
            String repoName = SpecsGit.getRepoName(url);
            assertThat(repoName).isNotNull();
            assertThat(repoName).isNotEmpty();
            assertThat(repoName).isEqualTo("repo");
        }

        // Test SSH URLs separately as they cause exceptions
        String[] sshUrls = {
                "git@github.com:user/repo.git",
                "ssh://git@server.com/repo.git"
        };

        for (String url : sshUrls) {
            if (url.startsWith("git@")) {
                // git@ format causes URISyntaxException
                assertThatThrownBy(() -> SpecsGit.getRepoName(url))
                        .isInstanceOf(RuntimeException.class);
            } else {
                // ssh:// format should work
                String repoName = SpecsGit.getRepoName(url);
                assertThat(repoName).isEqualTo("repo");
            }
        }
    }

    @Test
    void testTagNormalizationConsistency() {
        // Test that tag normalization is consistent and idempotent

        String[] testTags = { "v1.0", "release", "1.0.0", "beta-1" };

        for (String tag : testTags) {
            String normalized = SpecsGit.normalizeTag(tag);
            String doubleNormalized = SpecsGit.normalizeTag(normalized);

            assertThat(normalized).startsWith("refs/tags/");
            assertThat(doubleNormalized).isEqualTo(normalized);
        }
    }
}
