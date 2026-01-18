package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import pt.up.fe.specs.util.SpecsSystem;

/**
 * Comprehensive test suite for TagLogger interface.
 * 
 * Tests the tag-based logging interface including tag management,
 * logger creation, level configuration, and logging methods.
 * 
 * @author Generated Tests
 */
@DisplayName("TagLogger Tests")
@ExtendWith(MockitoExtension.class)
class TagLoggerTest {

    // Test enums for testing
    enum TestTag {
        PARSER, ANALYZER, GENERATOR
    }

    enum SimpleTag {
        A, B
    }

    // Test implementation for testing
    static class TestTagLogger implements TagLogger<TestTag> {
        private final String baseName;
        private final Collection<TestTag> tags;

        public TestTagLogger(String baseName, Collection<TestTag> tags) {
            this.baseName = baseName;
            this.tags = tags;
        }

        @Override
        public Collection<TestTag> getTags() {
            return tags;
        }

        @Override
        public String getBaseName() {
            return baseName;
        }
    }

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
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should require implementation of abstract methods")
        void testAbstractMethods() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("test.logger",
                    Arrays.asList(TestTag.PARSER, TestTag.ANALYZER));

            // Then
            assertThat(tagLogger.getTags()).containsExactly(TestTag.PARSER, TestTag.ANALYZER);
            assertThat(tagLogger.getBaseName()).isEqualTo("test.logger");
        }

        @Test
        @DisplayName("Should provide default method implementations")
        void testDefaultMethods() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("test.logger",
                    Arrays.asList(TestTag.PARSER));

            // Then - All default methods should be available
            assertThatCode(() -> {
                tagLogger.getLoggerName(TestTag.PARSER);
                tagLogger.getLogger(TestTag.PARSER);
                tagLogger.getBaseLogger();
                tagLogger.setLevel(TestTag.PARSER, Level.INFO);
                tagLogger.setLevelAll(Level.INFO);
                tagLogger.log(Level.INFO, TestTag.PARSER, "test");
                tagLogger.info(TestTag.PARSER, "test");
                tagLogger.warn(TestTag.PARSER, "test");
                tagLogger.debug("test");
                tagLogger.test("test");
                tagLogger.deprecated("test");
                tagLogger.addToIgnoreList(String.class);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Logger Name Generation Tests")
    class LoggerNameGenerationTests {

        @Test
        @DisplayName("Should generate logger name with tag")
        void testGetLoggerNameWithTag() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("com.example.Test", Collections.emptyList());

            // When
            String loggerName = tagLogger.getLoggerName(TestTag.PARSER);

            // Then
            assertThat(loggerName).isEqualTo("com.example.test.parser");
        }

        @Test
        @DisplayName("Should generate logger name without tag (root)")
        void testGetLoggerNameWithoutTag() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("com.example.Test", Collections.emptyList());

            // When
            String loggerName = tagLogger.getLoggerName(null);

            // Then
            assertThat(loggerName).isEqualTo("com.example.test.$root");
        }

        @Test
        @DisplayName("Should handle various base name formats")
        void testVariousBaseNameFormats() {
            String[] baseNames = {
                    "SimpleLogger",
                    "com.example.Logger",
                    "UPPER_CASE_LOGGER",
                    "mixed.Case.Logger",
                    "logger_with_underscores",
                    "logger-with-hyphens"
            };

            for (String baseName : baseNames) {
                TestTagLogger tagLogger = new TestTagLogger(baseName, Collections.emptyList());
                String loggerName = tagLogger.getLoggerName(TestTag.ANALYZER);

                assertThat(loggerName).startsWith(baseName.toLowerCase());
                assertThat(loggerName).endsWith(".analyzer");
            }
        }

        @Test
        @DisplayName("Should handle various tag formats")
        void testVariousTagFormats() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("test", Collections.emptyList());

            // Then
            for (TestTag tag : TestTag.values()) {
                String loggerName = tagLogger.getLoggerName(tag);
                assertThat(loggerName).isEqualTo("test." + tag.toString().toLowerCase());
            }
        }

        @Test
        @DisplayName("Should maintain consistency across calls")
        void testNameConsistency() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("consistency.test", Collections.emptyList());

            // When
            String name1 = tagLogger.getLoggerName(TestTag.GENERATOR);
            String name2 = tagLogger.getLoggerName(TestTag.GENERATOR);
            String name3 = tagLogger.getLoggerName(TestTag.GENERATOR);

            // Then
            assertThat(name1).isEqualTo(name2);
            assertThat(name2).isEqualTo(name3);
        }
    }

    @Nested
    @DisplayName("Logger Creation Tests")
    class LoggerCreationTests {

        @Test
        @DisplayName("Should create logger instances")
        void testGetLogger() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("logger.creation", Collections.emptyList());

            // When
            Logger logger = tagLogger.getLogger(TestTag.PARSER);

            // Then
            assertThat(logger).isNotNull();
            assertThat(logger.getName()).isEqualTo("logger.creation.parser");
        }

        @Test
        @DisplayName("Should create base logger")
        void testGetBaseLogger() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("base.logger", Collections.emptyList());

            // When
            Logger baseLogger = tagLogger.getBaseLogger();

            // Then
            assertThat(baseLogger).isNotNull();
            assertThat(baseLogger.getName()).isEqualTo("base.logger");
        }

        @Test
        @DisplayName("Should return same logger instance for same tag")
        void testLoggerCaching() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("cached.logger", Collections.emptyList());

            // When
            Logger logger1 = tagLogger.getLogger(TestTag.ANALYZER);
            Logger logger2 = tagLogger.getLogger(TestTag.ANALYZER);

            // Then
            assertThat(logger1).isSameAs(logger2);
        }

        @Test
        @DisplayName("Should create different loggers for different tags")
        void testDifferentLoggers() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("different.loggers", Collections.emptyList());

            // When
            Logger parserLogger = tagLogger.getLogger(TestTag.PARSER);
            Logger analyzerLogger = tagLogger.getLogger(TestTag.ANALYZER);

            // Then
            assertThat(parserLogger).isNotSameAs(analyzerLogger);
            assertThat(parserLogger.getName()).isNotEqualTo(analyzerLogger.getName());
        }
    }

    @Nested
    @DisplayName("Level Configuration Tests")
    class LevelConfigurationTests {

        @Test
        @DisplayName("Should set level for specific tag")
        void testSetLevel() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("level.test", Collections.emptyList());
            Level testLevel = Level.WARNING;

            // When
            tagLogger.setLevel(TestTag.PARSER, testLevel);

            // Then
            Logger logger = tagLogger.getLogger(TestTag.PARSER);
            assertThat(logger.getLevel()).isEqualTo(testLevel);
        }

        @Test
        @DisplayName("Should set level for all tags")
        void testSetLevelAll() {
            // Given
            Collection<TestTag> tags = Arrays.asList(TestTag.PARSER, TestTag.ANALYZER, TestTag.GENERATOR);
            TestTagLogger tagLogger = new TestTagLogger("level.all.test", tags);
            Level testLevel = Level.SEVERE;

            // When
            tagLogger.setLevelAll(testLevel);

            // Then
            for (TestTag tag : tags) {
                Logger logger = tagLogger.getLogger(tag);
                assertThat(logger.getLevel()).isEqualTo(testLevel);
            }

            // Root logger should also be set
            Logger rootLogger = tagLogger.getLogger(null);
            assertThat(rootLogger.getLevel()).isEqualTo(testLevel);
        }

        @Test
        @DisplayName("Should handle empty tag collection")
        void testSetLevelAllEmptyTags() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("empty.tags", Collections.emptyList());
            Level testLevel = Level.FINE;

            // When/Then - Should not throw exception
            assertThatCode(() -> tagLogger.setLevelAll(testLevel))
                    .doesNotThrowAnyException();

            // Root logger should still be set
            Logger rootLogger = tagLogger.getLogger(null);
            assertThat(rootLogger.getLevel()).isEqualTo(testLevel);
        }

        @Test
        @DisplayName("Should handle different log levels")
        void testDifferentLogLevels() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("levels.test", Collections.emptyList());
            Level[] levels = { Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG,
                    Level.FINE, Level.FINER, Level.FINEST, Level.ALL, Level.OFF };

            // When/Then
            for (Level level : levels) {
                assertThatCode(() -> tagLogger.setLevel(TestTag.PARSER, level))
                        .doesNotThrowAnyException();

                Logger logger = tagLogger.getLogger(TestTag.PARSER);
                assertThat(logger.getLevel()).isEqualTo(level);
            }
        }
    }

    @Nested
    @DisplayName("Logging Methods Tests")
    class LoggingMethodsTests {

        @Test
        @DisplayName("Should log with level and tag")
        void testLogWithLevelAndTag() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("log.test", Collections.emptyList());

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                tagLogger.log(Level.INFO, TestTag.PARSER, "Test message");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should log with various parameters")
        void testLogWithVariousParameters() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("log.params", Collections.emptyList());

            // When/Then
            assertThatCode(() -> {
                tagLogger.log(Level.INFO, TestTag.PARSER, "Message with tag");
                tagLogger.log(Level.WARNING, TestTag.PARSER, "Message with source", LogSourceInfo.SOURCE);
                tagLogger.log(Level.SEVERE, TestTag.PARSER, "Message with stack", LogSourceInfo.STACK_TRACE,
                        Thread.currentThread().getStackTrace());
                tagLogger.log(Level.INFO, "Message without tag");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle info logging")
        void testInfoLogging() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("info.test", Collections.emptyList());

            // When/Then
            assertThatCode(() -> {
                tagLogger.info(TestTag.ANALYZER, "Info message with tag");
                tagLogger.info("Info message without tag");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle warning logging")
        void testWarningLogging() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("warn.test", Collections.emptyList());

            // When/Then
            assertThatCode(() -> {
                tagLogger.warn(TestTag.GENERATOR, "Warning message with tag");
                tagLogger.warn("Warning message without tag");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle debug logging")
        void testDebugLogging() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("debug.test", Collections.emptyList());

            // When/Then
            assertThatCode(() -> {
                tagLogger.debug("Debug message");
                tagLogger.debug(() -> "Debug supplier message");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle special logging methods")
        void testSpecialLoggingMethods() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("special.test", Collections.emptyList());

            // When/Then
            assertThatCode(() -> {
                tagLogger.test("Test message");
                tagLogger.deprecated("Deprecated message");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle debug with supplier optimization")
        void testDebugSupplierOptimization() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("supplier.test", Collections.emptyList());
            @SuppressWarnings("unchecked")
            Supplier<String> expensiveSupplier = mock(Supplier.class);
            lenient().when(expensiveSupplier.get()).thenReturn("Expensive computation result");

            // When - Debug should only evaluate supplier if debug is enabled
            tagLogger.debug(expensiveSupplier);

            // Then - Verify supplier behavior based on debug state
            if (SpecsSystem.isDebug()) {
                verify(expensiveSupplier, times(1)).get();
            } else {
                verify(expensiveSupplier, never()).get();
            }
        }
    }

    @Nested
    @DisplayName("Utility Methods Tests")
    class UtilityMethodsTests {

        @Test
        @DisplayName("Should add class to ignore list")
        void testAddToIgnoreList() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("ignore.test", Collections.emptyList());
            Class<?> testClass = TagLoggerTest.class;

            // When
            TagLogger<TestTag> result = tagLogger.addToIgnoreList(testClass);

            // Then
            assertThat(result).isSameAs(tagLogger); // Should return this for fluent interface
        }

        @Test
        @DisplayName("Should support fluent interface")
        void testFluentInterface() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("fluent.test", Collections.emptyList());

            // When/Then - Should support method chaining
            assertThatCode(() -> {
                tagLogger.addToIgnoreList(String.class)
                        .addToIgnoreList(Integer.class)
                        .addToIgnoreList(Boolean.class);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle multiple classes in ignore list")
        void testMultipleIgnoreClasses() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("multiple.ignore", Collections.emptyList());
            Class<?>[] classes = { String.class, Integer.class, Boolean.class, Double.class };

            // When/Then
            for (Class<?> clazz : classes) {
                assertThatCode(() -> tagLogger.addToIgnoreList(clazz))
                        .doesNotThrowAnyException();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null tags gracefully")
        void testNullTags() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("null.tag.test", Collections.emptyList());

            // When/Then
            assertThatCode(() -> {
                tagLogger.getLoggerName(null);
                tagLogger.getLogger(null);
                tagLogger.setLevel(null, Level.INFO);
                tagLogger.log(Level.INFO, null, "Message with null tag");
                tagLogger.info(null, "Info with null tag");
                tagLogger.warn(null, "Warning with null tag");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle empty and null messages")
        void testEmptyAndNullMessages() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("empty.message.test", Collections.emptyList());

            // When/Then
            assertThatCode(() -> {
                tagLogger.log(Level.INFO, TestTag.PARSER, "");
                tagLogger.log(Level.INFO, TestTag.PARSER, null);
                tagLogger.info(TestTag.PARSER, "");
                tagLogger.info(TestTag.PARSER, null);
                tagLogger.warn(TestTag.PARSER, "");
                tagLogger.warn(TestTag.PARSER, null);
                tagLogger.debug("");
                tagLogger.debug((String) null);
                tagLogger.test("");
                tagLogger.test(null);
                tagLogger.deprecated("");
                tagLogger.deprecated(null);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle special characters in messages")
        void testSpecialCharactersInMessages() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("special.chars", Collections.emptyList());
            String specialMessage = "Message with unicode: \u00E9\u00F1\u00FC and symbols: @#$%^&*()";

            // When/Then
            assertThatCode(() -> {
                tagLogger.log(Level.INFO, TestTag.PARSER, specialMessage);
                tagLogger.info(TestTag.PARSER, specialMessage);
                tagLogger.warn(TestTag.PARSER, specialMessage);
                tagLogger.debug(specialMessage);
                tagLogger.test(specialMessage);
                tagLogger.deprecated(specialMessage);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle very long messages")
        void testVeryLongMessages() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("long.message", Collections.emptyList());
            StringBuilder longMessage = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longMessage.append("This is a very long message part ").append(i).append(". ");
            }
            String message = longMessage.toString();

            // When/Then
            assertThatCode(() -> {
                tagLogger.info(TestTag.PARSER, message);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null supplier in debug")
        void testNullSupplierDebug() {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("null.supplier", Collections.emptyList());

            // When/Then
            assertThatCode(() -> {
                tagLogger.debug((Supplier<String>) null);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with different enum types")
        void testDifferentEnumTypes() {
            // Given
            TagLogger<SimpleTag> simpleTagLogger = new TagLogger<SimpleTag>() {
                @Override
                public Collection<SimpleTag> getTags() {
                    return Arrays.asList(SimpleTag.A, SimpleTag.B);
                }

                @Override
                public String getBaseName() {
                    return "simple.enum.test";
                }
            };

            // When/Then
            assertThatCode(() -> {
                simpleTagLogger.setLevelAll(Level.INFO);
                simpleTagLogger.info(SimpleTag.A, "Message A");
                simpleTagLogger.warn(SimpleTag.B, "Message B");
            }).doesNotThrowAnyException();

            assertThat(simpleTagLogger.getLoggerName(SimpleTag.A)).isEqualTo("simple.enum.test.a");
            assertThat(simpleTagLogger.getLoggerName(SimpleTag.B)).isEqualTo("simple.enum.test.b");
        }

        @Test
        @DisplayName("Should handle concurrent logging")
        void testConcurrentLogging() throws InterruptedException {
            // Given
            TestTagLogger tagLogger = new TestTagLogger("concurrent.test",
                    Arrays.asList(TestTag.PARSER, TestTag.ANALYZER, TestTag.GENERATOR));
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    for (TestTag tag : TestTag.values()) {
                        tagLogger.info(tag, "Concurrent message " + index);
                        tagLogger.warn(tag, "Concurrent warning " + index);
                        tagLogger.debug("Concurrent debug " + index);
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then - Should complete without exceptions
            // Logger instances should still be accessible
            for (TestTag tag : TestTag.values()) {
                Logger logger = tagLogger.getLogger(tag);
                assertThat(logger).isNotNull();
            }
        }

        @Test
        @DisplayName("Should maintain consistency across complex operations")
        void testComplexOperationsConsistency() {
            // Given
            Collection<TestTag> tags = Arrays.asList(TestTag.PARSER, TestTag.ANALYZER, TestTag.GENERATOR);
            TestTagLogger tagLogger = new TestTagLogger("complex.test", tags);

            // When - Perform complex sequence of operations
            tagLogger.setLevelAll(Level.INFO);

            for (TestTag tag : tags) {
                tagLogger.setLevel(tag, Level.WARNING);
                tagLogger.log(Level.SEVERE, tag, "Severe message");
                tagLogger.info(tag, "Info message");
                tagLogger.warn(tag, "Warning message");
            }

            tagLogger.debug("Debug message");
            tagLogger.test("Test message");
            tagLogger.deprecated("Deprecated message");

            tagLogger.addToIgnoreList(String.class)
                    .addToIgnoreList(Integer.class);

            // Then - Verify state consistency
            for (TestTag tag : tags) {
                Logger logger = tagLogger.getLogger(tag);
                assertThat(logger).isNotNull();
                assertThat(logger.getLevel()).isEqualTo(Level.WARNING);
                assertThat(logger.getName()).contains(tag.toString().toLowerCase());
            }
        }
    }
}
