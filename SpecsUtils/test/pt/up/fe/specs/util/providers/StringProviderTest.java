package pt.up.fe.specs.util.providers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Unit tests for StringProvider interface and its implementations.
 * 
 * @author Generated Tests
 */
@DisplayName("StringProvider")
class StringProviderTest {

    private TestStringProvider testProvider;
    private String testString;

    @TempDir
    File tempDir;

    @BeforeEach
    void setUp() {
        testString = "test-string-content";
        testProvider = new TestStringProvider(testString);
    }

    @Nested
    @DisplayName("Interface Contract")
    class InterfaceContract {

        @Test
        @DisplayName("should have correct interface methods")
        void shouldHaveCorrectInterfaceMethods() {
            assertThatCode(() -> {
                StringProvider.class.getMethod("getString");
                StringProvider.class.getMethod("getKey");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("should extend KeyProvider")
        void shouldExtendKeyProvider() {
            assertThat(KeyProvider.class.isAssignableFrom(StringProvider.class)).isTrue();
        }

        @Test
        @DisplayName("should be a functional interface")
        void shouldBeAFunctionalInterface() {
            assertThat(StringProvider.class.isInterface()).isTrue();
        }
    }

    @Nested
    @DisplayName("Basic Functionality")
    class BasicFunctionality {

        @Test
        @DisplayName("should return string content")
        void shouldReturnStringContent() {
            assertThat(testProvider.getString()).isEqualTo(testString);
        }

        @Test
        @DisplayName("should return string as key")
        void shouldReturnStringAsKey() {
            assertThat(testProvider.getKey()).isEqualTo(testString);
        }

        @Test
        @DisplayName("should have consistent key and string values")
        void shouldHaveConsistentKeyAndStringValues() {
            assertThat(testProvider.getKey()).isEqualTo(testProvider.getString());
        }

        @Test
        @DisplayName("should handle null strings")
        void shouldHandleNullStrings() {
            TestStringProvider nullProvider = new TestStringProvider(null);

            assertThat(nullProvider.getString()).isNull();
            assertThat(nullProvider.getKey()).isNull();
        }

        @Test
        @DisplayName("should handle empty strings")
        void shouldHandleEmptyStrings() {
            TestStringProvider emptyProvider = new TestStringProvider("");

            assertThat(emptyProvider.getString()).isEmpty();
            assertThat(emptyProvider.getKey()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Static Factory Methods")
    class StaticFactoryMethods {

        @Test
        @DisplayName("should create instance from string")
        void shouldCreateInstanceFromString() {
            String content = "factory-created-string";
            StringProvider provider = StringProvider.newInstance(content);

            assertThat(provider.getString()).isEqualTo(content);
            assertThat(provider.getKey()).isEqualTo(content);
        }

        @Test
        @DisplayName("should create instance from file")
        void shouldCreateInstanceFromFile() throws IOException {
            String fileContent = "file-content-test\nmultiline\ncontent";
            File testFile = new File(tempDir, "test.txt");
            Files.write(testFile.toPath(), fileContent.getBytes());

            StringProvider provider = StringProvider.newInstance(testFile);

            assertThat(provider.getString()).isEqualTo(fileContent);
            assertThat(provider.getKey()).isEqualTo(fileContent);
        }

        @Test
        @DisplayName("should create instance from resource provider")
        void shouldCreateInstanceFromResourceProvider() {
            ResourceProvider resourceProvider = () -> "test/resource.txt";

            StringProvider provider = StringProvider.newInstance(resourceProvider);

            // Resource loading might fail, but provider creation should succeed
            assertThat(provider).isNotNull();
        }

        @Test
        @DisplayName("should reject null file during creation")
        void shouldRejectNullFileDuringCreation() {
            // Factory method should reject null file immediately
            assertThatThrownBy(() -> StringProvider.newInstance((File) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("File cannot be null");
        }

        @Test
        @DisplayName("should reject null resource provider during creation")
        void shouldRejectNullResourceProviderDuringCreation() {
            // Factory method should reject null resource immediately
            assertThatThrownBy(() -> StringProvider.newInstance((ResourceProvider) null))
                    .isInstanceOf(NullPointerException.class)
                    .hasMessageContaining("Resource cannot be null");
        }
    }

    @Nested
    @DisplayName("Lambda Implementation")
    class LambdaImplementation {

        @Test
        @DisplayName("should work with lambda expressions")
        void shouldWorkWithLambdaExpressions() {
            StringProvider lambdaProvider = () -> "lambda-generated-string";

            assertThat(lambdaProvider.getString()).isEqualTo("lambda-generated-string");
            assertThat(lambdaProvider.getKey()).isEqualTo("lambda-generated-string");
        }

        @Test
        @DisplayName("should work with method references")
        void shouldWorkWithMethodReferences() {
            String constantString = "constant-string";
            StringProvider methodRefProvider = () -> constantString;

            assertThat(methodRefProvider.getString()).isEqualTo(constantString);
        }

        @Test
        @DisplayName("should support dynamic string generation")
        void shouldSupportDynamicStringGeneration() {
            StringProvider dynamicProvider = () -> "dynamic-" + System.currentTimeMillis();

            String string1 = dynamicProvider.getString();
            String string2 = dynamicProvider.getString();

            assertThat(string1).startsWith("dynamic-");
            assertThat(string2).startsWith("dynamic-");
        }
    }

    @Nested
    @DisplayName("File Integration")
    class FileIntegration {

        @Test
        @DisplayName("should read single line file")
        void shouldReadSingleLineFile() throws IOException {
            String content = "single line content";
            File testFile = new File(tempDir, "single.txt");
            Files.write(testFile.toPath(), content.getBytes());

            StringProvider provider = StringProvider.newInstance(testFile);

            assertThat(provider.getString()).isEqualTo(content);
        }

        @Test
        @DisplayName("should read multiline file")
        void shouldReadMultilineFile() throws IOException {
            String content = "line1\nline2\nline3";
            File testFile = new File(tempDir, "multiline.txt");
            Files.write(testFile.toPath(), content.getBytes());

            StringProvider provider = StringProvider.newInstance(testFile);

            assertThat(provider.getString()).isEqualTo(content);
        }

        @Test
        @DisplayName("should handle empty file")
        void shouldHandleEmptyFile() throws IOException {
            File testFile = new File(tempDir, "empty.txt");
            Files.write(testFile.toPath(), new byte[0]);

            StringProvider provider = StringProvider.newInstance(testFile);

            assertThat(provider.getString()).isEmpty();
        }

        @Test
        @DisplayName("should handle special characters")
        void shouldHandleSpecialCharacters() throws IOException {
            String content = "Special chars: áéíóú àèìòù âêîôû ãñõ ç ü";
            File testFile = new File(tempDir, "special.txt");
            Files.write(testFile.toPath(), content.getBytes("UTF-8"));

            StringProvider provider = StringProvider.newInstance(testFile);

            assertThat(provider.getString()).isEqualTo(content);
        }
    }

    @Nested
    @DisplayName("Caching Behavior")
    class CachingBehavior {

        @Test
        @DisplayName("should cache file content")
        void shouldCacheFileContent() throws IOException {
            String content = "cached content";
            File testFile = new File(tempDir, "cache.txt");
            Files.write(testFile.toPath(), content.getBytes());

            StringProvider provider = StringProvider.newInstance(testFile);

            // First read
            String firstRead = provider.getString();

            // Modify file
            Files.write(testFile.toPath(), "modified content".getBytes());

            // Second read should return cached content
            String secondRead = provider.getString();

            assertThat(firstRead).isEqualTo(content);
            assertThat(secondRead).isEqualTo(content); // Should be cached
        }

        @Test
        @DisplayName("should handle resource loading failures gracefully")
        void shouldHandleResourceLoadingFailuresGracefully() {
            ResourceProvider resourceProvider = () -> "non/existent/resource.txt";
            StringProvider provider = StringProvider.newInstance(resourceProvider);

            // Resource loading failure should return null gracefully
            String result = provider.getString();
            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("should handle providers that throw exceptions")
        void shouldHandleProvidersThrowExceptions() {
            StringProvider throwingProvider = () -> {
                throw new RuntimeException("String generation failed");
            };

            assertThatThrownBy(() -> throwingProvider.getString())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("String generation failed");
        }

        @Test
        @DisplayName("should handle very large strings")
        void shouldHandleVeryLargeStrings() {
            StringBuilder largeString = new StringBuilder();
            for (int i = 0; i < 10000; i++) {
                largeString.append("Line ").append(i).append("\n");
            }

            StringProvider provider = StringProvider.newInstance(largeString.toString());

            assertThat(provider.getString()).hasSize(largeString.length());
            assertThat(provider.getString()).startsWith("Line 0");
            assertThat(provider.getString()).contains("Line 9999");
        }

        @Test
        @DisplayName("should handle unicode strings")
        void shouldHandleUnicodeStrings() {
            String unicodeString = "Unicode: 😀🎉🔥💯 中文 العربية हिन्दी";
            StringProvider provider = StringProvider.newInstance(unicodeString);

            assertThat(provider.getString()).isEqualTo(unicodeString);
        }

        @Test
        @DisplayName("should handle strings with control characters")
        void shouldHandleStringsWithControlCharacters() {
            String controlString = "Control\t\n\r\0chars";
            StringProvider provider = StringProvider.newInstance(controlString);

            assertThat(provider.getString()).isEqualTo(controlString);
        }
    }

    @Nested
    @DisplayName("Polymorphism")
    class Polymorphism {

        @Test
        @DisplayName("should work with different implementations")
        void shouldWorkWithDifferentImplementations() {
            StringProvider impl1 = new TestStringProvider("impl1");
            StringProvider impl2 = () -> "impl2";
            StringProvider impl3 = StringProvider.newInstance("impl3");

            assertThat(impl1.getString()).isEqualTo("impl1");
            assertThat(impl2.getString()).isEqualTo("impl2");
            assertThat(impl3.getString()).isEqualTo("impl3");
        }

        @Test
        @DisplayName("should support interface-based programming")
        void shouldSupportInterfaceBasedProgramming() {
            java.util.List<StringProvider> providers = java.util.Arrays.asList(
                    new TestStringProvider("provider1"),
                    () -> "provider2",
                    StringProvider.newInstance("provider3"));

            assertThat(providers)
                    .extracting(StringProvider::getString)
                    .containsExactly("provider1", "provider2", "provider3");
        }
    }

    /**
     * Test implementation of StringProvider for testing purposes.
     */
    private static class TestStringProvider implements StringProvider {
        private final String string;

        public TestStringProvider(String string) {
            this.string = string;
        }

        @Override
        public String getString() {
            return string;
        }
    }
}
