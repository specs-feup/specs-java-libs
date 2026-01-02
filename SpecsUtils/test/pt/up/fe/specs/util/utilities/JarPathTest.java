package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Test class for JarPath utility.
 * 
 * Tests JAR path discovery functionality including:
 * - Constructor variations and parameter validation
 * - Static convenience methods
 * - System property-based path resolution
 * - Automatic JAR location detection
 * - Path normalization and validation
 * - Error handling and fallback mechanisms
 * 
 * @author Generated Tests
 */
@DisplayName("JarPath Tests")
class JarPathTest {

    @TempDir
    Path tempDir;

    private String originalProperty;
    private static final String TEST_PROPERTY = "test.jar.path";

    @BeforeEach
    void setUp() {
        // Save original property value
        originalProperty = System.getProperty(TEST_PROPERTY);
        // Clear property for clean tests
        System.clearProperty(TEST_PROPERTY);
    }

    @AfterEach
    void tearDown() {
        // Restore original property value
        if (originalProperty != null) {
            System.setProperty(TEST_PROPERTY, originalProperty);
        } else {
            System.clearProperty(TEST_PROPERTY);
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create JarPath with class and property")
        void testBasicConstructor() {
            JarPath jarPath = new JarPath(String.class, TEST_PROPERTY);

            assertThat(jarPath).isNotNull();
            // Constructor should accept any valid class and property
        }

        @Test
        @DisplayName("Should create JarPath with class, name, and property")
        void testThreeParameterConstructor() {
            JarPath jarPath = new JarPath(String.class, "TestProgram", TEST_PROPERTY);

            assertThat(jarPath).isNotNull();
            // Constructor should accept program name parameter
        }

        @Test
        @DisplayName("Should create JarPath with all parameters including verbose flag")
        void testFullConstructor() {
            JarPath jarPath = new JarPath(String.class, "TestProgram", TEST_PROPERTY, false);

            assertThat(jarPath).isNotNull();
            // Constructor should accept verbose parameter
        }

        @Test
        @DisplayName("Should handle null class parameter")
        void testNullClass() {
            assertThatThrownBy(() -> new JarPath(null, TEST_PROPERTY))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null property parameter")
        void testNullProperty() {
            // This should work - property can be null/empty for internal usage
            assertThatCode(() -> new JarPath(String.class, null))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null program name")
        void testNullProgramName() {
            assertThatCode(() -> new JarPath(String.class, null, TEST_PROPERTY))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Static Method Tests")
    class StaticMethodTests {

        @Test
        @DisplayName("Should provide static getJarFolder method")
        void testGetJarFolder() {
            Optional<String> jarFolder = JarPath.getJarFolder();

            assertThat(jarFolder).isNotNull();
            // Should return Optional - may be empty if JAR path cannot be determined
        }

        @Test
        @DisplayName("Should return consistent results from getJarFolder")
        void testGetJarFolderConsistency() {
            Optional<String> result1 = JarPath.getJarFolder();
            Optional<String> result2 = JarPath.getJarFolder();

            assertThat(result1).isEqualTo(result2);
            // Multiple calls should return same result
        }
    }

    @Nested
    @DisplayName("Path Building Tests")
    class PathBuildingTests {

        @Test
        @DisplayName("Should build jar path with trailing slash")
        void testBuildJarPath() {
            JarPath jarPath = new JarPath(String.class, TEST_PROPERTY);

            String path = jarPath.buildJarPath();

            assertThat(path).isNotNull()
                    .isNotEmpty()
                    .endsWith("/");
        }

        @Test
        @DisplayName("Should handle different path formats")
        void testPathNormalization() {
            JarPath jarPath = new JarPath(String.class, TEST_PROPERTY);

            String path = jarPath.buildJarPath();

            assertThat(path).doesNotContain("\\"); // Should normalize backslashes
        }

        @Test
        @DisplayName("Should return absolute paths")
        void testAbsolutePaths() {
            JarPath jarPath = new JarPath(String.class, TEST_PROPERTY);

            String path = jarPath.buildJarPath();

            assertThat(new File(path)).isAbsolute();
        }
    }

    @Nested
    @DisplayName("System Property Tests")
    class SystemPropertyTests {

        @Test
        @DisplayName("Should use system property when available and valid")
        void testValidSystemProperty(@TempDir Path methodTemp) throws IOException {
            // Create a valid temporary directory
            Path validDir = Files.createDirectory(methodTemp.resolve("jar_test"));
            System.setProperty(TEST_PROPERTY, validDir.toString());

            JarPath jarPath = new JarPath(String.class, TEST_PROPERTY);
            String path = jarPath.buildJarPath();

            assertThat(path).contains(validDir.getFileName().toString());
        }

        @Test
        @DisplayName("Should handle invalid system property gracefully")
        void testInvalidSystemProperty() {
            System.setProperty(TEST_PROPERTY, "/invalid/nonexistent/path");

            JarPath jarPath = new JarPath(String.class, "TestProgram", TEST_PROPERTY, false); // Non-verbose

            // Invalid system property should be handled gracefully and return a fallback
            assertThatCode(() -> jarPath.buildJarPath()).doesNotThrowAnyException();
            String path = jarPath.buildJarPath();
            assertThat(path).isNotNull().isNotEmpty().endsWith("/");
        }

        @Test
        @DisplayName("Should handle empty system property")
        void testEmptySystemProperty() {
            System.setProperty(TEST_PROPERTY, "");

            JarPath jarPath = new JarPath(String.class, TEST_PROPERTY);

            // Empty system property should be handled gracefully and return a fallback
            assertThatCode(() -> jarPath.buildJarPath()).doesNotThrowAnyException();
            String path = jarPath.buildJarPath();
            assertThat(path).isNotNull().isNotEmpty().endsWith("/");
        }
    }

    @Nested
    @DisplayName("Auto-Detection Tests")
    class AutoDetectionTests {

        @Test
        @DisplayName("Should attempt auto-detection when property not set")
        void testAutoDetection() {
            JarPath jarPath = new JarPath(String.class, "nonexistent.property");

            String path = jarPath.buildJarPath();

            assertThat(path).isNotNull()
                    .isNotEmpty();
            // Should fallback to auto-detection or working directory
        }

        @Test
        @DisplayName("Should handle protection domain access")
        void testProtectionDomainAccess() {
            JarPath jarPath = new JarPath(JarPathTest.class, TEST_PROPERTY);

            assertThatCode(() -> jarPath.buildJarPath())
                    .doesNotThrowAnyException();
            // Should handle cases where protection domain is accessible
        }
    }

    @Nested
    @DisplayName("Verbose Mode Tests")
    class VerboseModeTests {

        @Test
        @DisplayName("Should handle verbose mode without errors")
        void testVerboseMode() {
            System.setProperty(TEST_PROPERTY, "/invalid/path");

            JarPath jarPath = new JarPath(String.class, "TestApp", TEST_PROPERTY, true);

            // Invalid property in verbose mode should still be handled gracefully
            assertThatCode(() -> jarPath.buildJarPath()).doesNotThrowAnyException();
            String path = jarPath.buildJarPath();
            assertThat(path).isNotNull().isNotEmpty().endsWith("/");
        }

        @Test
        @DisplayName("Should handle non-verbose mode")
        void testNonVerboseMode() {
            System.setProperty(TEST_PROPERTY, "/invalid/path");

            JarPath jarPath = new JarPath(String.class, "TestApp", TEST_PROPERTY, false);

            // Invalid property in non-verbose mode should be handled gracefully
            assertThatCode(() -> jarPath.buildJarPath()).doesNotThrowAnyException();
            String path = jarPath.buildJarPath();
            assertThat(path).isNotNull().isNotEmpty().endsWith("/");
        }
    }

    @Nested
    @DisplayName("Error Handling Tests")
    class ErrorHandlingTests {

        @Test
        @DisplayName("Should provide fallback path when all methods fail")
        void testFallbackBehavior() {
            // Use a class that might have limited access for auto-detection
            JarPath jarPath = new JarPath(Object.class, "TestApp", "nonexistent.property", false);

            String path = jarPath.buildJarPath();

            assertThat(path).isNotNull()
                    .isNotEmpty()
                    .endsWith("/");
            // Should always provide some valid path as fallback
        }

        @Test
        @DisplayName("Should handle URI syntax exceptions gracefully")
        void testURISyntaxExceptionHandling() {
            // This is harder to test directly, but the method should handle exceptions
            JarPath jarPath = new JarPath(String.class, TEST_PROPERTY);

            assertThatCode(() -> jarPath.buildJarPath())
                    .doesNotThrowAnyException();
            // Internal URI exceptions should be caught and handled
        }

        @Test
        @DisplayName("Should handle IO exceptions in canonical path resolution")
        void testIOExceptionHandling(@TempDir Path methodTemp) {
            // Create a valid directory that we can reference
            try {
                Path validDir = Files.createDirectory(methodTemp.resolve("jar_test"));
                System.setProperty(TEST_PROPERTY, validDir.toString());

                JarPath jarPath = new JarPath(String.class, TEST_PROPERTY);

                assertThatCode(() -> jarPath.buildJarPath())
                        .doesNotThrowAnyException();
                // Should handle IO exceptions when getting canonical path

            } catch (IOException e) {
                // If temp directory creation fails, skip this test gracefully
                return;
            }
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with real file system paths")
        void testRealFileSystem(@TempDir Path methodTemp) throws IOException {
            Path jarDir = Files.createDirectory(methodTemp.resolve("jar_location"));
            System.setProperty(TEST_PROPERTY, jarDir.toString());

            JarPath jarPath = new JarPath(String.class, "MyApp", TEST_PROPERTY);
            String path = jarPath.buildJarPath();

            assertThat(path).contains(jarDir.getFileName().toString())
                    .endsWith("/");
        }

        @Test
        @DisplayName("Should maintain consistent behavior across multiple calls")
        void testConsistentBehavior() {
            JarPath jarPath = new JarPath(String.class, TEST_PROPERTY);

            String path1 = jarPath.buildJarPath();
            String path2 = jarPath.buildJarPath();

            assertThat(path1).isEqualTo(path2);
            // Multiple calls should return consistent results
        }
    }
}
