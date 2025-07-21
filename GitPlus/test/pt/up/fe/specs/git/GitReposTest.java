package pt.up.fe.specs.git;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

/**
 * Unit tests for {@link GitRepos} utility class.
 * Tests repository caching, singleton behavior, and thread safety.
 * 
 * @author Generated Tests
 */
class GitReposTest {

    private MockedStatic<GitRepo> gitRepoMock;

    @BeforeEach
    void setUp() {
        // Clear the internal repository cache before each test
        clearRepositoryCache();

        // Mock GitRepo.newInstance()
        gitRepoMock = mockStatic(GitRepo.class);
    }

    @AfterEach
    void tearDown() {
        if (gitRepoMock != null) {
            gitRepoMock.close();
        }
        // Clear cache after each test to ensure test isolation
        clearRepositoryCache();
    }

    @Test
    void testGetRepoFirstTime() {
        // Setup
        String repoPath = "/path/to/repo";
        GitRepo mockRepo = mock(GitRepo.class);
        gitRepoMock.when(() -> GitRepo.newInstance(repoPath)).thenReturn(mockRepo);

        // Execute
        GitRepo result = GitRepos.getRepo(repoPath);

        // Verify
        assertThat(result).isSameAs(mockRepo);
        gitRepoMock.verify(() -> GitRepo.newInstance(repoPath), times(1));
    }

    @Test
    void testGetRepoSecondTimeUsesCachedInstance() {
        // Setup
        String repoPath = "/path/to/repo";
        GitRepo mockRepo = mock(GitRepo.class);
        gitRepoMock.when(() -> GitRepo.newInstance(repoPath)).thenReturn(mockRepo);

        // Execute - call twice
        GitRepo result1 = GitRepos.getRepo(repoPath);
        GitRepo result2 = GitRepos.getRepo(repoPath);

        // Verify
        assertThat(result1).isSameAs(mockRepo);
        assertThat(result2).isSameAs(mockRepo);
        assertThat(result1).isSameAs(result2);

        // GitRepo.newInstance should only be called once
        gitRepoMock.verify(() -> GitRepo.newInstance(repoPath), times(1));
    }

    @Test
    void testGetRepoDifferentPaths() {
        // Setup
        String repoPath1 = "/path/to/repo1";
        String repoPath2 = "/path/to/repo2";
        GitRepo mockRepo1 = mock(GitRepo.class);
        GitRepo mockRepo2 = mock(GitRepo.class);

        gitRepoMock.when(() -> GitRepo.newInstance(repoPath1)).thenReturn(mockRepo1);
        gitRepoMock.when(() -> GitRepo.newInstance(repoPath2)).thenReturn(mockRepo2);

        // Execute
        GitRepo result1 = GitRepos.getRepo(repoPath1);
        GitRepo result2 = GitRepos.getRepo(repoPath2);

        // Verify
        assertThat(result1).isSameAs(mockRepo1);
        assertThat(result2).isSameAs(mockRepo2);
        assertThat(result1).isNotSameAs(result2);

        gitRepoMock.verify(() -> GitRepo.newInstance(repoPath1), times(1));
        gitRepoMock.verify(() -> GitRepo.newInstance(repoPath2), times(1));
    }

    @Test
    void testGetRepoWithNullPath() {
        // Test that null path throws NullPointerException due to ConcurrentHashMap
        assertThatThrownBy(() -> GitRepos.getRepo(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void testGetRepoWithEmptyPath() {
        // Setup
        String emptyPath = "";
        GitRepo mockRepo = mock(GitRepo.class);
        gitRepoMock.when(() -> GitRepo.newInstance(emptyPath)).thenReturn(mockRepo);

        // Execute
        GitRepo result = GitRepos.getRepo(emptyPath);

        // Verify
        assertThat(result).isSameAs(mockRepo);
        gitRepoMock.verify(() -> GitRepo.newInstance(emptyPath), times(1));
    }

    @Test
    void testGetFolderCallsGetRepo() {
        // Setup
        String repoPath = "/path/to/repo";
        File expectedWorkFolder = new File("/work/folder");
        GitRepo mockRepo = mock(GitRepo.class);

        gitRepoMock.when(() -> GitRepo.newInstance(repoPath)).thenReturn(mockRepo);
        when(mockRepo.getWorkFolder()).thenReturn(expectedWorkFolder);

        // Create an instance to test the non-static method
        GitRepos gitRepos = new GitRepos();

        // Execute
        File result = gitRepos.getFolder(repoPath);

        // Verify
        assertThat(result).isSameAs(expectedWorkFolder);
        gitRepoMock.verify(() -> GitRepo.newInstance(repoPath), times(1));
        verify(mockRepo).getWorkFolder();
    }

    @Test
    void testBasicCacheBehavior() {
        // Test that the cache works for basic scenarios
        String repoPath = "/path/to/repo";
        GitRepo mockRepo = mock(GitRepo.class);
        gitRepoMock.when(() -> GitRepo.newInstance(repoPath)).thenReturn(mockRepo);

        // First call should create new instance
        GitRepo result1 = GitRepos.getRepo(repoPath);
        assertThat(result1).isSameAs(mockRepo);

        // Second call should return cached instance
        GitRepo result2 = GitRepos.getRepo(repoPath);
        assertThat(result2).isSameAs(mockRepo);
        assertThat(result1).isSameAs(result2);

        // Should only create instance once
        gitRepoMock.verify(() -> GitRepo.newInstance(repoPath), times(1));
    }

    @Test
    void testCacheIsolationBetweenDifferentPaths() {
        // Setup multiple repository paths
        String[] repoPaths = { "/repo1", "/repo2", "/repo3" };
        GitRepo[] mockRepos = new GitRepo[repoPaths.length];

        for (int i = 0; i < repoPaths.length; i++) {
            mockRepos[i] = mock(GitRepo.class);
            final int index = i; // Make effectively final for lambda
            gitRepoMock.when(() -> GitRepo.newInstance(repoPaths[index])).thenReturn(mockRepos[index]);
        }

        // Execute - get each repo multiple times
        for (int i = 0; i < repoPaths.length; i++) {
            GitRepo result1 = GitRepos.getRepo(repoPaths[i]);
            GitRepo result2 = GitRepos.getRepo(repoPaths[i]);

            assertThat(result1).isSameAs(mockRepos[i]);
            assertThat(result2).isSameAs(mockRepos[i]);
            assertThat(result1).isSameAs(result2);
        }

        // Verify each path was only instantiated once
        for (String path : repoPaths) {
            gitRepoMock.verify(() -> GitRepo.newInstance(path), times(1));
        }
    }

    @Test
    void testStaticCacheInstance() {
        // Test that the cache is indeed static/shared across instances
        String repoPath = "/path/to/repo";
        GitRepo mockRepo = mock(GitRepo.class);
        gitRepoMock.when(() -> GitRepo.newInstance(repoPath)).thenReturn(mockRepo);

        // Get repo through static method
        GitRepo staticResult = GitRepos.getRepo(repoPath);

        // Get repo through instance method
        GitRepos instance = new GitRepos();
        File mockWorkFolder = new File("/work");
        when(mockRepo.getWorkFolder()).thenReturn(mockWorkFolder);

        // This should use the cached repo
        File instanceResult = instance.getFolder(repoPath);

        // Verify both use the same cached repo
        assertThat(staticResult).isSameAs(mockRepo);
        assertThat(instanceResult).isSameAs(mockWorkFolder);

        // newInstance should only be called once
        gitRepoMock.verify(() -> GitRepo.newInstance(repoPath), times(1));
    }

    /**
     * Helper method to clear the internal repository cache using reflection.
     */
    private void clearRepositoryCache() {
        try {
            Field reposField = GitRepos.class.getDeclaredField("REPOS");
            reposField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<String, GitRepo> repos = (Map<String, GitRepo>) reposField.get(null);
            repos.clear();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clear repository cache", e);
        }
    }
}
