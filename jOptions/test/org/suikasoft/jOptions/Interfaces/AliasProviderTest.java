package org.suikasoft.jOptions.Interfaces;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests for {@link AliasProvider} interface.
 * 
 * Tests the functional interface for providing alias mappings,
 * including lambda implementations, method references, anonymous classes,
 * and various edge cases for alias mappings.
 * 
 * @author Generated Tests
 */
@DisplayName("AliasProvider Tests")
class AliasProviderTest {

    @Nested
    @DisplayName("Functional Interface Tests")
    class FunctionalInterfaceTests {

        @Test
        @DisplayName("Lambda implementation returns correct aliases")
        void testLambdaImplementation() {
            Map<String, String> expectedAliases = Map.of(
                    "short", "full.name.short",
                    "s", "full.name.short",
                    "long", "full.name.long");

            AliasProvider provider = () -> expectedAliases;

            Map<String, String> actualAliases = provider.getAlias();

            assertThat(actualAliases).isEqualTo(expectedAliases);
        }

        @Test
        @DisplayName("Method reference implementation works correctly")
        void testMethodReferenceImplementation() {
            AliasProvider provider = this::getTestAliases;

            Map<String, String> actualAliases = provider.getAlias();

            assertThat(actualAliases).containsExactlyInAnyOrderEntriesOf(getTestAliases());
        }

        @Test
        @DisplayName("Anonymous class implementation works correctly")
        void testAnonymousClassImplementation() {
            AliasProvider provider = new AliasProvider() {
                @Override
                public Map<String, String> getAlias() {
                    Map<String, String> aliases = new HashMap<>();
                    aliases.put("v", "verbose");
                    aliases.put("h", "help");
                    aliases.put("q", "quiet");
                    return aliases;
                }
            };

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases)
                    .hasSize(3)
                    .containsEntry("v", "verbose")
                    .containsEntry("h", "help")
                    .containsEntry("q", "quiet");
        }

        private Map<String, String> getTestAliases() {
            return Map.of(
                    "test", "test.full.name",
                    "t", "test.full.name");
        }
    }

    @Nested
    @DisplayName("Empty Alias Tests")
    class EmptyAliasTests {

        @Test
        @DisplayName("Empty map implementation")
        void testEmptyMapImplementation() {
            AliasProvider provider = Collections::emptyMap;

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases).isEmpty();
        }

        @Test
        @DisplayName("Returns new empty map each time")
        void testReturnsNewEmptyMapEachTime() {
            AliasProvider provider = HashMap::new;

            Map<String, String> aliases1 = provider.getAlias();
            Map<String, String> aliases2 = provider.getAlias();

            assertThat(aliases1).isEmpty();
            assertThat(aliases2).isEmpty();
            assertThat(aliases1).isNotSameAs(aliases2); // Different instances
        }
    }

    @Nested
    @DisplayName("Complex Alias Mappings Tests")
    class ComplexAliasMappingsTests {

        @Test
        @DisplayName("Multiple aliases for same original")
        void testMultipleAliasesForSameOriginal() {
            AliasProvider provider = () -> Map.of(
                    "v", "verbose",
                    "verb", "verbose",
                    "detail", "verbose");

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases)
                    .hasSize(3)
                    .containsEntry("v", "verbose")
                    .containsEntry("verb", "verbose")
                    .containsEntry("detail", "verbose");
        }

        @Test
        @DisplayName("Hierarchical alias names")
        void testHierarchicalAliasNames() {
            AliasProvider provider = () -> Map.of(
                    "db.host", "database.hostname",
                    "db.port", "database.port",
                    "db.user", "database.username",
                    "srv.timeout", "server.timeout.milliseconds");

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases).containsAllEntriesOf(Map.of(
                    "db.host", "database.hostname",
                    "db.port", "database.port",
                    "db.user", "database.username",
                    "srv.timeout", "server.timeout.milliseconds"));
        }

        @Test
        @DisplayName("Special characters in aliases")
        void testSpecialCharactersInAliases() {
            AliasProvider provider = () -> Map.of(
                    "config-file", "configuration.file.path",
                    "log_level", "logging.level",
                    "api:key", "api.authentication.key",
                    "temp@dir", "temporary.directory");

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases)
                    .containsEntry("config-file", "configuration.file.path")
                    .containsEntry("log_level", "logging.level")
                    .containsEntry("api:key", "api.authentication.key")
                    .containsEntry("temp@dir", "temporary.directory");
        }
    }

    @Nested
    @DisplayName("Dynamic Alias Generation Tests")
    class DynamicAliasGenerationTests {

        @Test
        @DisplayName("Dynamically generated aliases")
        void testDynamicallyGeneratedAliases() {
            AliasProvider provider = () -> {
                Map<String, String> aliases = new HashMap<>();
                String[] prefixes = { "app", "config", "system" };
                String[] suffixes = { "name", "value", "path" };

                for (String prefix : prefixes) {
                    for (String suffix : suffixes) {
                        String alias = prefix + "." + suffix.charAt(0); // e.g., "app.n"
                        String original = prefix + "." + suffix; // e.g., "app.name"
                        aliases.put(alias, original);
                    }
                }
                return aliases;
            };

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases)
                    .hasSize(9)
                    .containsEntry("app.n", "app.name")
                    .containsEntry("config.v", "config.value")
                    .containsEntry("system.p", "system.path");
        }

        @Test
        @DisplayName("Conditional alias generation")
        void testConditionalAliasGeneration() {
            boolean includeDebugAliases = true;

            AliasProvider provider = () -> {
                Map<String, String> aliases = new HashMap<>();
                aliases.put("h", "help");
                aliases.put("v", "version");

                if (includeDebugAliases) {
                    aliases.put("d", "debug");
                    aliases.put("trace", "debug.trace");
                }

                return aliases;
            };

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases)
                    .hasSize(4)
                    .containsKeys("h", "v", "d", "trace");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Very long alias names")
        void testVeryLongAliasNames() {
            String longAlias = "very.long.alias.name.that.goes.on.and.on.and.on";
            String longOriginal = "very.long.original.name.that.is.even.longer.than.the.alias";

            AliasProvider provider = () -> Map.of(longAlias, longOriginal);

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases).containsEntry(longAlias, longOriginal);
        }

        @Test
        @DisplayName("Empty string aliases")
        void testEmptyStringAliases() {
            AliasProvider provider = () -> Map.of(
                    "", "empty.alias",
                    "empty.original", "");

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases)
                    .containsEntry("", "empty.alias")
                    .containsEntry("empty.original", "");
        }

        @Test
        @DisplayName("Single character aliases")
        void testSingleCharacterAliases() {
            AliasProvider provider = () -> Map.of(
                    "a", "alpha",
                    "b", "beta",
                    "c", "gamma", // Intentionally mismatched
                    "1", "first",
                    "2", "second");

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases)
                    .hasSize(5)
                    .containsEntry("a", "alpha")
                    .containsEntry("c", "gamma");
        }

        @Test
        @DisplayName("Unicode character aliases")
        void testUnicodeCharacterAliases() {
            AliasProvider provider = () -> Map.of(
                    "α", "alpha",
                    "β", "beta",
                    "π", "pi",
                    "∞", "infinity",
                    "🔧", "configuration");

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases)
                    .hasSize(5)
                    .containsEntry("α", "alpha")
                    .containsEntry("🔧", "configuration");
        }
    }

    @Nested
    @DisplayName("Immutability and Consistency Tests")
    class ImmutabilityAndConsistencyTests {

        @Test
        @DisplayName("Multiple calls return same content")
        void testMultipleCallsReturnSameContent() {
            Map<String, String> baseMap = Map.of("alias", "original");
            AliasProvider provider = () -> new HashMap<>(baseMap);

            Map<String, String> aliases1 = provider.getAlias();
            Map<String, String> aliases2 = provider.getAlias();

            assertThat(aliases1).isEqualTo(aliases2);
        }

        @Test
        @DisplayName("Returned map modifications don't affect subsequent calls")
        void testReturnedMapModificationsDontAffectSubsequentCalls() {
            AliasProvider provider = () -> new HashMap<>(Map.of("original", "value"));

            Map<String, String> aliases1 = provider.getAlias();
            aliases1.put("modified", "value");

            Map<String, String> aliases2 = provider.getAlias();

            assertThat(aliases2).doesNotContainKey("modified");
        }
    }

    @Nested
    @DisplayName("Composition and Chaining Tests")
    class CompositionAndChainingTests {

        @Test
        @DisplayName("Combining multiple alias providers")
        void testCombiningMultipleAliasProviders() {
            AliasProvider provider1 = () -> Map.of("a", "alpha", "b", "beta");
            AliasProvider provider2 = () -> Map.of("c", "gamma", "d", "delta");

            AliasProvider combined = () -> {
                Map<String, String> mergedAliases = new HashMap<>();
                mergedAliases.putAll(provider1.getAlias());
                mergedAliases.putAll(provider2.getAlias());
                return mergedAliases;
            };

            Map<String, String> aliases = combined.getAlias();

            assertThat(aliases)
                    .hasSize(4)
                    .containsEntry("a", "alpha")
                    .containsEntry("c", "gamma");
        }

        @Test
        @DisplayName("Alias provider with fallback")
        void testAliasProviderWithFallback() {
            AliasProvider primary = () -> Map.of("primary", "primary.value");
            AliasProvider fallback = () -> Map.of("fallback", "fallback.value", "primary", "fallback.primary");

            AliasProvider withFallback = () -> {
                Map<String, String> result = new HashMap<>(fallback.getAlias());
                result.putAll(primary.getAlias()); // Primary overrides fallback
                return result;
            };

            Map<String, String> aliases = withFallback.getAlias();

            assertThat(aliases)
                    .containsEntry("primary", "primary.value") // Primary wins
                    .containsEntry("fallback", "fallback.value");
        }
    }

    @Nested
    @DisplayName("Real-world Usage Patterns Tests")
    class RealWorldUsagePatternsTests {

        @Test
        @DisplayName("Command line argument aliases")
        void testCommandLineArgumentAliases() {
            AliasProvider provider = () -> Map.of(
                    "-h", "--help",
                    "-v", "--verbose",
                    "-q", "--quiet",
                    "-f", "--file",
                    "-o", "--output",
                    "-c", "--config");

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases)
                    .hasSize(6)
                    .containsEntry("-h", "--help")
                    .containsEntry("-f", "--file");
        }

        @Test
        @DisplayName("Configuration property aliases")
        void testConfigurationPropertyAliases() {
            AliasProvider provider = () -> Map.of(
                    "db.url", "database.connection.url",
                    "db.driver", "database.driver.class",
                    "cache.size", "application.cache.max.size",
                    "log.file", "logging.file.path");

            Map<String, String> aliases = provider.getAlias();

            assertThat(aliases).allSatisfy((alias, original) -> {
                assertThat(alias).isNotEmpty();
                assertThat(original).isNotEmpty();
                assertThat(original.length()).isGreaterThan(alias.length());
            });
        }
    }
}
