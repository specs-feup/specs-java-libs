package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for StringLogger class.
 * 
 * Tests the concrete implementation of TagLogger using String as tag type.
 * 
 * @author Generated Tests
 */
@DisplayName("StringLogger Tests")
class StringLoggerTest {

    private Map<String, Logger> originalLoggers;

    @BeforeEach
    void setUp() throws Exception {
        // Save original state of SpecsLoggers
        Field loggersField = SpecsLoggers.class.getDeclaredField("LOGGERS");
        loggersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Logger> loggers = (Map<String, Logger>) loggersField.get(null);
        originalLoggers = new ConcurrentHashMap<>(loggers);
        loggers.clear();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Restore original state
        Field loggersField = SpecsLoggers.class.getDeclaredField("LOGGERS");
        loggersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Logger> loggers = (Map<String, Logger>) loggersField.get(null);
        loggers.clear();
        loggers.putAll(originalLoggers);
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create StringLogger with base name only")
        void testConstructorWithBaseName() {
            // When
            StringLogger logger = new StringLogger("test.logger");

            // Then
            assertThat(logger.getBaseName()).isEqualTo("test.logger");
            assertThat(logger.getTags()).isEmpty();
        }

        @Test
        @DisplayName("Should create StringLogger with base name and tags")
        void testConstructorWithBaseNameAndTags() {
            // Given
            Set<String> tags = new HashSet<>(Arrays.asList("parser", "analyzer", "generator"));

            // When
            StringLogger logger = new StringLogger("test.logger.with.tags", tags);

            // Then
            assertThat(logger.getBaseName()).isEqualTo("test.logger.with.tags");
            assertThat(logger.getTags()).containsExactlyInAnyOrder("parser", "analyzer", "generator");
        }

        @Test
        @DisplayName("Should handle empty tag set")
        void testConstructorWithEmptyTags() {
            // Given
            Set<String> emptyTags = new HashSet<>();

            // When
            StringLogger logger = new StringLogger("empty.tags", emptyTags);

            // Then
            assertThat(logger.getBaseName()).isEqualTo("empty.tags");
            assertThat(logger.getTags()).isEmpty();
        }

        @Test
        @DisplayName("Should handle null base name")
        void testConstructorWithNullBaseName() {
            // When/Then - Should not throw exception but base name will be null
            assertThatCode(() -> {
                StringLogger logger = new StringLogger(null);
                assertThat(logger.getBaseName()).isNull();
                assertThat(logger.getTags()).isEmpty();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null tags")
        void testConstructorWithNullTags() {
            // When/Then - Should not throw exception and create empty set for null tags
            assertThatCode(() -> {
                StringLogger logger = new StringLogger("null.tags", null);
                assertThat(logger.getBaseName()).isEqualTo("null.tags");
                assertThat(logger.getTags()).isEmpty();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should create defensive copy of tags set")
        void testTagSetReference() {
            // Given
            Set<String> originalTags = new HashSet<>(Arrays.asList("tag1", "tag2"));
            StringLogger logger = new StringLogger("reference.test", originalTags);

            // When - Modify original set
            originalTags.add("tag3");

            // Then - Logger should not be affected because it creates a defensive copy
            assertThat(logger.getTags()).containsExactlyInAnyOrder("tag1", "tag2");
            assertThat(logger.getTags()).isNotSameAs(originalTags); // Different reference
        }
    }

    @Nested
    @DisplayName("TagLogger Interface Implementation Tests")
    class TagLoggerInterfaceTests {

        @Test
        @DisplayName("Should implement getTags correctly")
        void testGetTags() {
            // Given
            Set<String> tags = new HashSet<>(Arrays.asList("feature", "debug", "performance"));
            StringLogger logger = new StringLogger("tags.test", tags);

            // When
            Collection<String> result = logger.getTags();

            // Then
            assertThat(result).containsExactlyInAnyOrder("feature", "debug", "performance");
            assertThat(result).isNotSameAs(tags); // Should return a defensive copy, not the same reference
        }

        @Test
        @DisplayName("Should implement getBaseName correctly")
        void testGetBaseName() {
            // Given
            StringLogger logger = new StringLogger("base.name.test");

            // When
            String result = logger.getBaseName();

            // Then
            assertThat(result).isEqualTo("base.name.test");
        }

        @Test
        @DisplayName("Should support TagLogger default methods")
        void testTagLoggerDefaultMethods() {
            // Given
            Set<String> tags = new HashSet<>(Arrays.asList("method", "test"));
            StringLogger logger = new StringLogger("default.methods", tags);

            // When/Then - All default methods should be available
            assertThatCode(() -> {
                logger.getLoggerName("method");
                logger.getLogger("method");
                logger.getBaseLogger();
                logger.setLevel("method", Level.INFO);
                logger.setLevelAll(Level.INFO);
                logger.log(Level.INFO, "method", "test message");
                logger.info("method", "info message");
                logger.warn("method", "warning message");
                logger.debug("debug message");
                logger.test("test message");
                logger.deprecated("deprecated message");
                logger.addToIgnoreList(String.class);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Logger Name Generation Tests")
    class LoggerNameGenerationTests {

        @Test
        @DisplayName("Should generate logger names for string tags")
        void testLoggerNameGeneration() {
            // Given
            StringLogger logger = new StringLogger("string.name.test");

            // When/Then
            assertThat(logger.getLoggerName("parser")).isEqualTo("string.name.test.parser");
            assertThat(logger.getLoggerName("analyzer")).isEqualTo("string.name.test.analyzer");
            assertThat(logger.getLoggerName("UPPER_CASE")).isEqualTo("string.name.test.upper_case");
            assertThat(logger.getLoggerName("mixed-Case_Tag")).isEqualTo("string.name.test.mixed-case_tag");
        }

        @Test
        @DisplayName("Should handle special characters in tags")
        void testSpecialCharactersInTags() {
            // Given
            StringLogger logger = new StringLogger("special.chars");

            // When/Then
            assertThat(logger.getLoggerName("tag-with-hyphens")).isEqualTo("special.chars.tag-with-hyphens");
            assertThat(logger.getLoggerName("tag_with_underscores")).isEqualTo("special.chars.tag_with_underscores");
            assertThat(logger.getLoggerName("tag.with.dots")).isEqualTo("special.chars.tag.with.dots");
            assertThat(logger.getLoggerName("tag@with$symbols")).isEqualTo("special.chars.tag@with$symbols");
        }

        @Test
        @DisplayName("Should handle null tag")
        void testNullTag() {
            // Given
            StringLogger logger = new StringLogger("null.tag.test");

            // When
            String loggerName = logger.getLoggerName(null);

            // Then
            assertThat(loggerName).isEqualTo("null.tag.test.$root");
        }

        @Test
        @DisplayName("Should handle empty string tag")
        void testEmptyStringTag() {
            // Given
            StringLogger logger = new StringLogger("empty.tag.test");

            // When
            String loggerName = logger.getLoggerName("");

            // Then
            assertThat(loggerName).isEqualTo("empty.tag.test.");
        }

        @Test
        @DisplayName("Should maintain consistency across calls")
        void testNameConsistency() {
            // Given
            StringLogger logger = new StringLogger("consistency.test");
            String tag = "consistent-tag";

            // When
            String name1 = logger.getLoggerName(tag);
            String name2 = logger.getLoggerName(tag);
            String name3 = logger.getLoggerName(tag);

            // Then
            assertThat(name1).isEqualTo(name2);
            assertThat(name2).isEqualTo(name3);
        }
    }

    @Nested
    @DisplayName("Logger Creation and Management Tests")
    class LoggerCreationTests {

        @Test
        @DisplayName("Should create loggers for string tags")
        void testLoggerCreation() {
            // Given
            Set<String> tags = new HashSet<>(Arrays.asList("creation", "test"));
            StringLogger stringLogger = new StringLogger("logger.creation", tags);

            // When
            Logger creationLogger = stringLogger.getLogger("creation");
            Logger testLogger = stringLogger.getLogger("test");

            // Then
            assertThat(creationLogger).isNotNull();
            assertThat(testLogger).isNotNull();
            assertThat(creationLogger.getName()).isEqualTo("logger.creation.creation");
            assertThat(testLogger.getName()).isEqualTo("logger.creation.test");
        }

        @Test
        @DisplayName("Should cache logger instances")
        void testLoggerCaching() {
            // Given
            StringLogger stringLogger = new StringLogger("caching.test");

            // When
            Logger logger1 = stringLogger.getLogger("cached");
            Logger logger2 = stringLogger.getLogger("cached");

            // Then
            assertThat(logger1).isSameAs(logger2);
        }

        @Test
        @DisplayName("Should create different loggers for different tags")
        void testDifferentLoggers() {
            // Given
            StringLogger stringLogger = new StringLogger("different.loggers");

            // When
            Logger logger1 = stringLogger.getLogger("tag1");
            Logger logger2 = stringLogger.getLogger("tag2");

            // Then
            assertThat(logger1).isNotSameAs(logger2);
            assertThat(logger1.getName()).isNotEqualTo(logger2.getName());
        }

        @Test
        @DisplayName("Should create base logger")
        void testBaseLogger() {
            // Given
            StringLogger stringLogger = new StringLogger("base.logger.test");

            // When
            Logger baseLogger = stringLogger.getBaseLogger();

            // Then
            assertThat(baseLogger).isNotNull();
            assertThat(baseLogger.getName()).isEqualTo("base.logger.test");
        }
    }

    @Nested
    @DisplayName("Level Configuration Tests")
    class LevelConfigurationTests {

        @Test
        @DisplayName("Should set level for specific string tags")
        void testSetLevelForTag() {
            // Given
            Set<String> tags = new HashSet<>(Arrays.asList("level", "test"));
            StringLogger stringLogger = new StringLogger("level.config", tags);
            Level testLevel = Level.WARNING;

            // When
            stringLogger.setLevel("level", testLevel);

            // Then
            Logger logger = stringLogger.getLogger("level");
            assertThat(logger.getLevel()).isEqualTo(testLevel);
        }

        @Test
        @DisplayName("Should set level for all tags")
        void testSetLevelAll() {
            // Given
            Set<String> tags = new HashSet<>(Arrays.asList("all1", "all2", "all3"));
            StringLogger stringLogger = new StringLogger("level.all", tags);
            Level testLevel = Level.SEVERE;

            // When
            stringLogger.setLevelAll(testLevel);

            // Then
            for (String tag : tags) {
                Logger logger = stringLogger.getLogger(tag);
                assertThat(logger.getLevel()).isEqualTo(testLevel);
            }

            // Root logger should also be set
            Logger rootLogger = stringLogger.getLogger(null);
            assertThat(rootLogger.getLevel()).isEqualTo(testLevel);
        }

        @Test
        @DisplayName("Should handle different log levels")
        void testDifferentLogLevels() {
            // Given
            StringLogger stringLogger = new StringLogger("levels.test");
            Level[] levels = { Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG,
                    Level.FINE, Level.FINER, Level.FINEST, Level.ALL, Level.OFF };

            // When/Then
            for (Level level : levels) {
                stringLogger.setLevel("test-tag", level);
                Logger logger = stringLogger.getLogger("test-tag");
                assertThat(logger.getLevel()).isEqualTo(level);
            }
        }
    }

    @Nested
    @DisplayName("Logging Operations Tests")
    class LoggingOperationsTests {

        @Test
        @DisplayName("Should log with string tags")
        void testLoggingWithStringTags() {
            // Given
            Set<String> tags = new HashSet<>(Arrays.asList("operation", "log"));
            StringLogger stringLogger = new StringLogger("logging.ops", tags);

            // When/Then - Should not throw exceptions
            assertThatCode(() -> {
                stringLogger.log(Level.INFO, "operation", "Operation message");
                stringLogger.info("operation", "Info message");
                stringLogger.warn("operation", "Warning message");
                stringLogger.log(Level.SEVERE, "log", "Severe message");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should support various logging methods")
        void testVariousLoggingMethods() {
            // Given
            StringLogger stringLogger = new StringLogger("various.methods");

            // When/Then
            assertThatCode(() -> {
                stringLogger.debug("Debug message");
                stringLogger.test("Test message");
                stringLogger.deprecated("Deprecated message");
                stringLogger.info("General info");
                stringLogger.warn("General warning");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle logging with source info")
        void testLoggingWithSourceInfo() {
            // Given
            StringLogger stringLogger = new StringLogger("source.info");

            // When/Then
            assertThatCode(() -> {
                stringLogger.log(Level.INFO, "source", "Message with source", LogSourceInfo.SOURCE);
                stringLogger.log(Level.WARNING, "trace", "Message with trace", LogSourceInfo.STACK_TRACE,
                        Thread.currentThread().getStackTrace());
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Utility Methods Tests")
    class UtilityMethodsTests {

        @Test
        @DisplayName("Should add classes to ignore list")
        void testAddToIgnoreList() {
            // Given
            StringLogger stringLogger = new StringLogger("ignore.list");

            // When
            StringLogger result = (StringLogger) stringLogger.addToIgnoreList(String.class);

            // Then
            assertThat(result).isSameAs(stringLogger); // Should return this for fluent interface
        }

        @Test
        @DisplayName("Should support fluent interface")
        void testFluentInterface() {
            // Given
            StringLogger stringLogger = new StringLogger("fluent.test");

            // When/Then
            assertThatCode(() -> {
                stringLogger.addToIgnoreList(String.class)
                        .addToIgnoreList(Integer.class)
                        .addToIgnoreList(Boolean.class);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long base names")
        void testVeryLongBaseName() {
            // Given
            StringBuilder longName = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                longName.append("very.long.name.segment.").append(i).append(".");
            }
            String baseName = longName.toString();

            // When/Then
            assertThatCode(() -> {
                StringLogger logger = new StringLogger(baseName);
                assertThat(logger.getBaseName()).isEqualTo(baseName);
                logger.info("test", "Message");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle very long tag names")
        void testVeryLongTags() {
            // Given
            StringBuilder longTag = new StringBuilder();
            for (int i = 0; i < 50; i++) {
                longTag.append("very-long-tag-name-segment-").append(i);
            }
            String tag = longTag.toString();

            StringLogger stringLogger = new StringLogger("long.tag.test");

            // When/Then
            assertThatCode(() -> {
                stringLogger.info(tag, "Message with very long tag");
                Logger logger = stringLogger.getLogger(tag);
                assertThat(logger).isNotNull();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle unicode characters in names and tags")
        void testUnicodeCharacters() {
            // Given
            String unicodeBaseName = "测试.logger.ñamé.ü";
            String unicodeTag = "标签.tág.ñ";

            // When/Then
            assertThatCode(() -> {
                StringLogger logger = new StringLogger(unicodeBaseName);
                logger.info(unicodeTag, "Unicode message: こんにちは");
                assertThat(logger.getBaseName()).isEqualTo(unicodeBaseName);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle large tag sets")
        void testLargeTagSets() {
            // Given
            Set<String> largeTags = new HashSet<>();
            for (int i = 0; i < 1000; i++) {
                largeTags.add("tag" + i);
            }

            // When/Then
            assertThatCode(() -> {
                StringLogger logger = new StringLogger("large.tags", largeTags);
                assertThat(logger.getTags()).hasSize(1000);

                // Test a few random tags
                logger.info("tag100", "Message 100");
                logger.warn("tag500", "Message 500");
                logger.debug("Debug message");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle empty and whitespace-only strings")
        void testEmptyAndWhitespaceStrings() {
            // Given/When/Then
            assertThatCode(() -> {
                StringLogger logger1 = new StringLogger("");
                StringLogger logger2 = new StringLogger("   ");
                StringLogger logger3 = new StringLogger("\t\n\r");

                logger1.info("", "Empty base name");
                logger2.info("   ", "Whitespace tag");
                logger3.info("\t", "Tab tag");

                assertThat(logger1.getBaseName()).isEmpty();
                assertThat(logger2.getBaseName()).isEqualTo("   ");
                assertThat(logger3.getBaseName()).isEqualTo("\t\n\r");
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Concurrent Access Tests")
    class ConcurrentAccessTests {

        @Test
        @DisplayName("Should handle concurrent logger creation")
        void testConcurrentLoggerCreation() throws InterruptedException {
            // Given
            StringLogger stringLogger = new StringLogger("concurrent.creation");
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];
            Logger[] results = new Logger[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    results[index] = stringLogger.getLogger("concurrent-tag");
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then - All threads should get the same logger instance
            Logger firstLogger = results[0];
            for (Logger logger : results) {
                assertThat(logger).isSameAs(firstLogger);
            }
        }

        @Test
        @DisplayName("Should handle concurrent logging operations")
        void testConcurrentLogging() throws InterruptedException {
            // Given
            Set<String> tags = new HashSet<>(Arrays.asList("concurrent1", "concurrent2", "concurrent3"));
            StringLogger stringLogger = new StringLogger("concurrent.logging", tags);
            int threadCount = 5;
            Thread[] threads = new Thread[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    for (String tag : tags) {
                        stringLogger.info(tag, "Concurrent message " + index);
                        stringLogger.warn(tag, "Concurrent warning " + index);
                    }
                    stringLogger.debug("Concurrent debug " + index);
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then - Should complete without exceptions
            // Verify loggers are still accessible
            for (String tag : tags) {
                Logger logger = stringLogger.getLogger(tag);
                assertThat(logger).isNotNull();
            }
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should integrate with SpecsLoggers framework")
        void testSpecsLoggersIntegration() {
            // Given
            StringLogger stringLogger = new StringLogger("specs.integration");

            // When
            Logger baseLogger = stringLogger.getBaseLogger();
            Logger tagLogger = stringLogger.getLogger("integration");

            // Then - Should be registered in SpecsLoggers
            Logger directBaseLogger = SpecsLoggers.getLogger("specs.integration");
            Logger directTagLogger = SpecsLoggers.getLogger("specs.integration.integration");

            assertThat(baseLogger).isSameAs(directBaseLogger);
            assertThat(tagLogger).isSameAs(directTagLogger);
        }

        @Test
        @DisplayName("Should work with complex logging scenarios")
        void testComplexLoggingScenarios() {
            // Given
            Set<String> tags = new HashSet<>(Arrays.asList("scenario1", "scenario2", "scenario3"));
            StringLogger stringLogger = new StringLogger("complex.scenarios", tags);

            // When - Complex sequence of operations
            stringLogger.setLevelAll(Level.INFO);

            for (String tag : tags) {
                stringLogger.setLevel(tag, Level.WARNING);
                stringLogger.log(Level.SEVERE, tag, "Severe message for " + tag);
                stringLogger.info(tag, "Info message for " + tag);
                stringLogger.warn(tag, "Warning message for " + tag);
            }

            stringLogger.debug("Global debug message");
            stringLogger.test("Global test message");
            stringLogger.deprecated("Global deprecated message");

            stringLogger.addToIgnoreList(String.class)
                    .addToIgnoreList(Integer.class);

            // Then - Verify final state
            for (String tag : tags) {
                Logger logger = stringLogger.getLogger(tag);
                assertThat(logger).isNotNull();
                assertThat(logger.getLevel()).isEqualTo(Level.WARNING);
                assertThat(logger.getName()).contains(tag);
            }

            assertThat(stringLogger.getBaseName()).isEqualTo("complex.scenarios");
            assertThat(stringLogger.getTags()).containsExactlyInAnyOrderElementsOf(tags);
        }
    }
}
