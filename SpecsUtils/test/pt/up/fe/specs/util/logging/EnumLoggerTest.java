package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for EnumLogger interface.
 * 
 * Tests the functional interface that provides enum-based TagLogger
 * implementations.
 * 
 * @author Generated Tests
 */
@DisplayName("EnumLogger Tests")
class EnumLoggerTest {

    // Test enums for testing
    enum TestLogLevel {
        TRACE, DEBUG, INFO, WARN, ERROR, FATAL
    }

    enum SimpleEnum {
        A, B, C
    }

    enum EmptyEnum {
        // Empty enum for edge case testing
    }

    enum SingleValueEnum {
        ONLY_VALUE
    }

    // Test implementations
    static class TestEnumLogger implements EnumLogger<TestLogLevel> {
        @Override
        public Class<TestLogLevel> getEnumClass() {
            return TestLogLevel.class;
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
        @DisplayName("Should be a functional interface")
        void testFunctionalInterface() {
            // When - Use as lambda expression
            EnumLogger<TestLogLevel> lambdaLogger = () -> TestLogLevel.class;

            // Then
            assertThat(lambdaLogger.getEnumClass()).isEqualTo(TestLogLevel.class);
            assertThat(lambdaLogger.getBaseName()).isEqualTo(TestLogLevel.class.getName());
        }

        @Test
        @DisplayName("Should require implementation of getEnumClass method")
        void testAbstractMethod() {
            // Given
            TestEnumLogger enumLogger = new TestEnumLogger();

            // When
            Class<TestLogLevel> result = enumLogger.getEnumClass();

            // Then
            assertThat(result).isEqualTo(TestLogLevel.class);
        }

        @Test
        @DisplayName("Should implement TagLogger interface")
        void testTagLoggerImplementation() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // Then - Should be a TagLogger
            assertThat(enumLogger).isInstanceOf(TagLogger.class);

            // Should have all TagLogger methods available
            assertThatCode(() -> {
                enumLogger.getLoggerName(TestLogLevel.INFO);
                enumLogger.getLogger(TestLogLevel.DEBUG);
                enumLogger.getBaseLogger();
                enumLogger.setLevel(TestLogLevel.WARN, Level.WARNING);
                enumLogger.setLevelAll(Level.INFO);
                enumLogger.log(Level.INFO, TestLogLevel.INFO, "test message");
                enumLogger.info(TestLogLevel.INFO, "info message");
                enumLogger.warn(TestLogLevel.WARN, "warning message");
                enumLogger.debug("debug message");
                enumLogger.test("test message");
                enumLogger.deprecated("deprecated message");
                enumLogger.addToIgnoreList(String.class);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Default Method Implementation Tests")
    class DefaultMethodTests {

        @Test
        @DisplayName("Should generate base name from enum class name")
        void testGetBaseName() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When
            String baseName = enumLogger.getBaseName();

            // Then
            assertThat(baseName).isEqualTo("pt.up.fe.specs.util.logging.EnumLoggerTest$TestLogLevel");
        }

        @Test
        @DisplayName("Should generate tags from enum constants")
        void testGetTags() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When
            Collection<TestLogLevel> tags = enumLogger.getTags();

            // Then
            assertThat(tags).containsExactlyInAnyOrder(TestLogLevel.values());
            assertThat(tags).hasSize(6);
        }

        @Test
        @DisplayName("Should handle different enum types")
        void testDifferentEnumTypes() {
            // Given
            EnumLogger<SimpleEnum> simpleLogger = () -> SimpleEnum.class;
            EnumLogger<SingleValueEnum> singleLogger = () -> SingleValueEnum.class;

            // When/Then
            assertThat(simpleLogger.getBaseName()).contains("SimpleEnum");
            assertThat(simpleLogger.getTags()).containsExactlyInAnyOrder(SimpleEnum.values());

            assertThat(singleLogger.getBaseName()).contains("SingleValueEnum");
            assertThat(singleLogger.getTags()).containsExactly(SingleValueEnum.ONLY_VALUE);
        }

        @Test
        @DisplayName("Should maintain tag consistency")
        void testTagConsistency() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When
            Collection<TestLogLevel> tags1 = enumLogger.getTags();
            Collection<TestLogLevel> tags2 = enumLogger.getTags();

            // Then
            assertThat(tags1).containsExactlyElementsOf(tags2);
            // Note: Arrays.asList returns same content but might not be same instance
        }

        @Test
        @DisplayName("Should return this from addToIgnoreList for fluent interface")
        void testAddToIgnoreListFluent() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When
            EnumLogger<TestLogLevel> result = enumLogger.addToIgnoreList(String.class);

            // Then
            assertThat(result).isSameAs(enumLogger);
        }

        @Test
        @DisplayName("Should support fluent chaining")
        void testFluentChaining() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When/Then - Should support method chaining
            assertThatCode(() -> {
                enumLogger.addToIgnoreList(String.class)
                        .addToIgnoreList(Integer.class)
                        .addToIgnoreList(Boolean.class);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Factory Method Tests")
    class FactoryMethodTests {

        @Test
        @DisplayName("Should create instance via newInstance factory method")
        void testNewInstanceFactory() {
            // When
            EnumLogger<TestLogLevel> enumLogger = EnumLogger.newInstance(TestLogLevel.class);

            // Then
            assertThat(enumLogger).isNotNull();
            assertThat(enumLogger.getEnumClass()).isEqualTo(TestLogLevel.class);
            assertThat(enumLogger.getBaseName()).isEqualTo(TestLogLevel.class.getName());
            assertThat(enumLogger.getTags()).containsExactlyInAnyOrder(TestLogLevel.values());
        }

        @Test
        @DisplayName("Should create different instances for different enum types")
        void testFactoryForDifferentTypes() {
            // When
            EnumLogger<TestLogLevel> testLogger = EnumLogger.newInstance(TestLogLevel.class);
            EnumLogger<SimpleEnum> simpleLogger = EnumLogger.newInstance(SimpleEnum.class);

            // Then
            assertThat(testLogger.getEnumClass()).isEqualTo(TestLogLevel.class);
            assertThat(simpleLogger.getEnumClass()).isEqualTo(SimpleEnum.class);
            assertThat(testLogger.getBaseName()).isNotEqualTo(simpleLogger.getBaseName());
            assertThat(testLogger.getTags()).isNotEqualTo(simpleLogger.getTags());
        }

        @Test
        @DisplayName("Should create consistent instances from factory")
        void testFactoryConsistency() {
            // When
            EnumLogger<TestLogLevel> logger1 = EnumLogger.newInstance(TestLogLevel.class);
            EnumLogger<TestLogLevel> logger2 = EnumLogger.newInstance(TestLogLevel.class);

            // Then - Different instances but same behavior
            assertThat(logger1).isNotSameAs(logger2);
            assertThat(logger1.getEnumClass()).isEqualTo(logger2.getEnumClass());
            assertThat(logger1.getBaseName()).isEqualTo(logger2.getBaseName());
            assertThat(logger1.getTags()).containsExactlyElementsOf(logger2.getTags());
        }
    }

    @Nested
    @DisplayName("Logger Name Generation Tests")
    class LoggerNameGenerationTests {

        @Test
        @DisplayName("Should generate logger names for enum constants")
        void testLoggerNameGeneration() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When/Then
            assertThat(enumLogger.getLoggerName(TestLogLevel.TRACE))
                    .endsWith(".trace");
            assertThat(enumLogger.getLoggerName(TestLogLevel.DEBUG))
                    .endsWith(".debug");
            assertThat(enumLogger.getLoggerName(TestLogLevel.INFO))
                    .endsWith(".info");
            assertThat(enumLogger.getLoggerName(TestLogLevel.WARN))
                    .endsWith(".warn");
            assertThat(enumLogger.getLoggerName(TestLogLevel.ERROR))
                    .endsWith(".error");
            assertThat(enumLogger.getLoggerName(TestLogLevel.FATAL))
                    .endsWith(".fatal");
        }

        @Test
        @DisplayName("Should include full enum class name in logger names (lowercase)")
        void testFullClassNameInLoggerName() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When
            String loggerName = enumLogger.getLoggerName(TestLogLevel.INFO);

            // Then
            // Note: Logger names are converted to lowercase
            assertThat(loggerName).startsWith("pt.up.fe.specs.util.logging.enumloggertest$testloglevel");
            assertThat(loggerName).endsWith(".info");
        }

        @Test
        @DisplayName("Should handle null enum constant")
        void testNullEnumConstant() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When
            String loggerName = enumLogger.getLoggerName(null);

            // Then
            assertThat(loggerName).endsWith(".$root");
        }

        @Test
        @DisplayName("Should maintain name consistency across calls")
        void testNameConsistency() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When
            String name1 = enumLogger.getLoggerName(TestLogLevel.DEBUG);
            String name2 = enumLogger.getLoggerName(TestLogLevel.DEBUG);
            String name3 = enumLogger.getLoggerName(TestLogLevel.DEBUG);

            // Then
            assertThat(name1).isEqualTo(name2);
            assertThat(name2).isEqualTo(name3);
        }
    }

    @Nested
    @DisplayName("Logger Creation Tests")
    class LoggerCreationTests {

        @Test
        @DisplayName("Should create loggers for enum constants")
        void testLoggerCreation() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When
            Logger traceLogger = enumLogger.getLogger(TestLogLevel.TRACE);
            Logger infoLogger = enumLogger.getLogger(TestLogLevel.INFO);
            Logger errorLogger = enumLogger.getLogger(TestLogLevel.ERROR);

            // Then
            assertThat(traceLogger).isNotNull();
            assertThat(infoLogger).isNotNull();
            assertThat(errorLogger).isNotNull();

            assertThat(traceLogger.getName()).contains("trace");
            assertThat(infoLogger.getName()).contains("info");
            assertThat(errorLogger.getName()).contains("error");
        }

        @Test
        @DisplayName("Should cache logger instances")
        void testLoggerCaching() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When
            Logger logger1 = enumLogger.getLogger(TestLogLevel.WARN);
            Logger logger2 = enumLogger.getLogger(TestLogLevel.WARN);

            // Then
            assertThat(logger1).isSameAs(logger2);
        }

        @Test
        @DisplayName("Should create different loggers for different enum constants")
        void testDifferentLoggers() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When
            Logger debugLogger = enumLogger.getLogger(TestLogLevel.DEBUG);
            Logger fatalLogger = enumLogger.getLogger(TestLogLevel.FATAL);

            // Then
            assertThat(debugLogger).isNotSameAs(fatalLogger);
            assertThat(debugLogger.getName()).isNotEqualTo(fatalLogger.getName());
        }

        @Test
        @DisplayName("Should create base logger from enum class")
        void testBaseLogger() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When
            Logger baseLogger = enumLogger.getBaseLogger();

            // Then
            assertThat(baseLogger).isNotNull();
            assertThat(baseLogger.getName()).isEqualTo(TestLogLevel.class.getName());
        }
    }

    @Nested
    @DisplayName("Level Configuration Tests")
    class LevelConfigurationTests {

        @Test
        @DisplayName("Should set level for specific enum constants")
        void testSetLevelForEnumConstant() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;
            Level testLevel = Level.WARNING;

            // When
            enumLogger.setLevel(TestLogLevel.DEBUG, testLevel);

            // Then
            Logger logger = enumLogger.getLogger(TestLogLevel.DEBUG);
            assertThat(logger.getLevel()).isEqualTo(testLevel);
        }

        @Test
        @DisplayName("Should set level for all enum constants")
        void testSetLevelAll() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;
            Level testLevel = Level.SEVERE;

            // When
            enumLogger.setLevelAll(testLevel);

            // Then
            for (TestLogLevel level : TestLogLevel.values()) {
                Logger logger = enumLogger.getLogger(level);
                assertThat(logger.getLevel()).isEqualTo(testLevel);
            }

            // Root logger should also be set
            Logger rootLogger = enumLogger.getLogger(null);
            assertThat(rootLogger.getLevel()).isEqualTo(testLevel);
        }

        @Test
        @DisplayName("Should handle different log levels")
        void testDifferentLogLevels() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;
            Level[] levels = { Level.SEVERE, Level.WARNING, Level.INFO, Level.CONFIG,
                    Level.FINE, Level.FINER, Level.FINEST, Level.ALL, Level.OFF };

            // When/Then
            for (Level level : levels) {
                enumLogger.setLevel(TestLogLevel.TRACE, level);
                Logger logger = enumLogger.getLogger(TestLogLevel.TRACE);
                assertThat(logger.getLevel()).isEqualTo(level);
            }
        }
    }

    @Nested
    @DisplayName("Logging Operations Tests")
    class LoggingOperationsTests {

        @Test
        @DisplayName("Should log with enum constants")
        void testLoggingWithEnumConstants() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When/Then - Should not throw exceptions
            assertThatCode(() -> {
                enumLogger.log(Level.INFO, TestLogLevel.INFO, "Info message");
                enumLogger.info(TestLogLevel.INFO, "Direct info message");
                enumLogger.warn(TestLogLevel.WARN, "Warning message");
                enumLogger.log(Level.SEVERE, TestLogLevel.ERROR, "Error message");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should support various logging methods")
        void testVariousLoggingMethods() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When/Then
            assertThatCode(() -> {
                enumLogger.debug("Debug message");
                enumLogger.test("Test message");
                enumLogger.deprecated("Deprecated message");
                enumLogger.info("General info");
                enumLogger.warn("General warning");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle logging with source info")
        void testLoggingWithSourceInfo() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When/Then
            assertThatCode(() -> {
                enumLogger.log(Level.INFO, TestLogLevel.INFO, "Message with source", LogSourceInfo.SOURCE);
                enumLogger.log(Level.WARNING, TestLogLevel.WARN, "Message with trace", LogSourceInfo.STACK_TRACE,
                        Thread.currentThread().getStackTrace());
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should log all enum constants")
        void testLoggingAllEnumConstants() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When/Then
            for (TestLogLevel level : TestLogLevel.values()) {
                assertThatCode(() -> {
                    enumLogger.info(level, "Message for " + level);
                    enumLogger.warn(level, "Warning for " + level);
                }).doesNotThrowAnyException();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle enums with special names")
        void testSpecialEnumNames() {
            // Given - Enum with special characters in names
            enum SpecialNamesEnum {
                NAME_WITH_UNDERSCORES, ALLUPPERCASE, mixedCaseEnum
            }

            EnumLogger<SpecialNamesEnum> enumLogger = () -> SpecialNamesEnum.class;

            // When/Then
            assertThatCode(() -> {
                for (SpecialNamesEnum value : SpecialNamesEnum.values()) {
                    enumLogger.info(value, "Message for " + value);
                    Logger logger = enumLogger.getLogger(value);
                    assertThat(logger).isNotNull();
                }
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle large enums")
        void testLargeEnum() {
            // Given - Create a large enum dynamically would be complex, so test with
            // existing enum
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When - Set level for all constants
            enumLogger.setLevelAll(Level.INFO);

            // Then - Should handle all constants efficiently
            for (TestLogLevel level : TestLogLevel.values()) {
                Logger logger = enumLogger.getLogger(level);
                assertThat(logger).isNotNull();
                assertThat(logger.getLevel()).isEqualTo(Level.INFO);
            }
        }

        @Test
        @DisplayName("Should handle single value enum")
        void testSingleValueEnum() {
            // Given
            EnumLogger<SingleValueEnum> enumLogger = () -> SingleValueEnum.class;

            // When/Then
            assertThat(enumLogger.getTags()).containsExactly(SingleValueEnum.ONLY_VALUE);
            assertThat(enumLogger.getBaseName()).contains("SingleValueEnum");

            assertThatCode(() -> {
                enumLogger.info(SingleValueEnum.ONLY_VALUE, "Single value message");
                Logger logger = enumLogger.getLogger(SingleValueEnum.ONLY_VALUE);
                assertThat(logger).isNotNull();
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle complex enum hierarchies")
        void testComplexEnumHierarchies() {
            // Given - Enum implementing interface
            interface Categorized {
                String getCategory();
            }

            enum CategorizedEnum implements Categorized {
                TYPE_A("category1"), TYPE_B("category2"), TYPE_C("category1");

                private final String category;

                CategorizedEnum(String category) {
                    this.category = category;
                }

                @Override
                public String getCategory() {
                    return category;
                }
            }

            EnumLogger<CategorizedEnum> enumLogger = () -> CategorizedEnum.class;

            // When/Then
            assertThat(enumLogger.getTags()).containsExactlyInAnyOrder(CategorizedEnum.values());

            for (CategorizedEnum value : CategorizedEnum.values()) {
                assertThatCode(() -> {
                    enumLogger.info(value, "Message for " + value + " in " + value.getCategory());
                }).doesNotThrowAnyException();
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
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When
            Logger baseLogger = enumLogger.getBaseLogger();
            Logger infoLogger = enumLogger.getLogger(TestLogLevel.INFO);

            // Then - Should be accessible through SpecsLoggers (but may be different
            // instances due to timing)
            String expectedBaseName = TestLogLevel.class.getName();
            String expectedInfoName = enumLogger.getLoggerName(TestLogLevel.INFO);

            Logger directBaseLogger = SpecsLoggers.getLogger(expectedBaseName);
            Logger directInfoLogger = SpecsLoggers.getLogger(expectedInfoName);

            // Verify they have the same names (instances may differ due to creation order)
            assertThat(baseLogger.getName()).isEqualTo(directBaseLogger.getName());
            assertThat(infoLogger.getName()).isEqualTo(directInfoLogger.getName());
        }

        @Test
        @DisplayName("Should work with complex logging scenarios")
        void testComplexLoggingScenarios() {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;

            // When - Complex sequence of operations
            enumLogger.setLevelAll(Level.INFO);

            for (TestLogLevel level : TestLogLevel.values()) {
                enumLogger.setLevel(level, Level.WARNING);
                enumLogger.log(Level.SEVERE, level, "Severe message for " + level);
                enumLogger.info(level, "Info message for " + level);
                enumLogger.warn(level, "Warning message for " + level);
            }

            enumLogger.debug("Global debug message");
            enumLogger.test("Global test message");
            enumLogger.deprecated("Global deprecated message");

            enumLogger.addToIgnoreList(String.class)
                    .addToIgnoreList(Integer.class);

            // Then - Verify final state
            for (TestLogLevel level : TestLogLevel.values()) {
                Logger logger = enumLogger.getLogger(level);
                assertThat(logger).isNotNull();
                assertThat(logger.getLevel()).isEqualTo(Level.WARNING);
                assertThat(logger.getName()).contains(level.toString().toLowerCase());
            }

            assertThat(enumLogger.getBaseName()).isEqualTo(TestLogLevel.class.getName());
            assertThat(enumLogger.getTags()).containsExactlyInAnyOrderElementsOf(Arrays.asList(TestLogLevel.values()));
        }

        @Test
        @DisplayName("Should support concurrent access")
        void testConcurrentAccess() throws InterruptedException {
            // Given
            EnumLogger<TestLogLevel> enumLogger = () -> TestLogLevel.class;
            int threadCount = 5;
            Thread[] threads = new Thread[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    for (TestLogLevel level : TestLogLevel.values()) {
                        enumLogger.info(level, "Concurrent message " + index + " for " + level);
                        enumLogger.warn(level, "Concurrent warning " + index + " for " + level);
                    }
                    enumLogger.debug("Concurrent debug " + index);
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then - Should complete without exceptions
            // Verify loggers are still accessible
            for (TestLogLevel level : TestLogLevel.values()) {
                Logger logger = enumLogger.getLogger(level);
                assertThat(logger).isNotNull();
            }
        }
    }
}
