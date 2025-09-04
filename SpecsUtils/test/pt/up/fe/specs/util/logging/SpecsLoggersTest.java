package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

/**
 * Comprehensive test suite for SpecsLoggers class.
 * 
 * Tests the logger factory and management functionality including
 * logger creation, caching, concurrent access, and internal state management.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsLoggers Tests")
class SpecsLoggersTest {

    private Map<String, Logger> originalLoggers;

    @BeforeEach
    void setUp() throws Exception {
        // Save original state of LOGGERS map
        Field loggersField = SpecsLoggers.class.getDeclaredField("LOGGERS");
        loggersField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Logger> loggers = (Map<String, Logger>) loggersField.get(null);
        originalLoggers = new ConcurrentHashMap<>(loggers);

        // Clear the loggers map for clean test state
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
    @DisplayName("Logger Creation Tests")
    class LoggerCreationTests {

        @Test
        @DisplayName("Should create logger with given name")
        void testGetLoggerCreation() throws Exception {
            // Given
            String loggerName = "test.logger.name";

            // When
            Logger logger = getLoggerViaReflection(loggerName);

            // Then
            assertThat(logger).isNotNull();
            assertThat(logger.getName()).isEqualTo(loggerName);
        }

        @Test
        @DisplayName("Should create different loggers for different names")
        void testGetLoggerDifferentNames() throws Exception {
            // Given
            String loggerName1 = "test.logger.one";
            String loggerName2 = "test.logger.two";

            // When
            Logger logger1 = getLoggerViaReflection(loggerName1);
            Logger logger2 = getLoggerViaReflection(loggerName2);

            // Then
            assertThat(logger1).isNotNull();
            assertThat(logger2).isNotNull();
            assertThat(logger1).isNotSameAs(logger2);
            assertThat(logger1.getName()).isEqualTo(loggerName1);
            assertThat(logger2.getName()).isEqualTo(loggerName2);
        }

        @Test
        @DisplayName("Should handle various logger name formats")
        void testLoggerNameFormats() throws Exception {
            String[] loggerNames = {
                    "simple",
                    "dot.separated.name",
                    "hyphen-separated-name",
                    "underscore_separated_name",
                    "mixed.dot-hyphen_underscore.name",
                    "com.example.package.ClassName",
                    "very.long.package.name.with.many.levels.ClassName",
                    "single",
                    "",
                    "123numeric456",
                    "special$characters#in@name"
            };

            for (String loggerName : loggerNames) {
                Logger logger = getLoggerViaReflection(loggerName);
                assertThat(logger).isNotNull();
                assertThat(logger.getName()).isEqualTo(loggerName);
            }
        }
    }

    @Nested
    @DisplayName("Logger Caching Tests")
    class LoggerCachingTests {

        @Test
        @DisplayName("Should return same logger instance for same name")
        void testGetLoggerCaching() throws Exception {
            // Given
            String loggerName = "cached.logger";

            // When
            Logger logger1 = getLoggerViaReflection(loggerName);
            Logger logger2 = getLoggerViaReflection(loggerName);

            // Then
            assertThat(logger1).isSameAs(logger2);
        }

        @Test
        @DisplayName("Should cache multiple loggers independently")
        void testMultipleLoggerCaching() throws Exception {
            // Given
            String[] loggerNames = { "logger.one", "logger.two", "logger.three" };
            Logger[] firstCall = new Logger[loggerNames.length];
            Logger[] secondCall = new Logger[loggerNames.length];

            // When - First call to each logger
            for (int i = 0; i < loggerNames.length; i++) {
                firstCall[i] = getLoggerViaReflection(loggerNames[i]);
            }

            // Second call to each logger
            for (int i = 0; i < loggerNames.length; i++) {
                secondCall[i] = getLoggerViaReflection(loggerNames[i]);
            }

            // Then
            for (int i = 0; i < loggerNames.length; i++) {
                assertThat(firstCall[i]).isSameAs(secondCall[i]);
                assertThat(firstCall[i].getName()).isEqualTo(loggerNames[i]);
            }
        }

        @Test
        @DisplayName("Should verify logger is stored in internal cache")
        void testLoggerStoredInCache() throws Exception {
            // Given
            String loggerName = "cache.verification.logger";

            // When
            Logger logger = getLoggerViaReflection(loggerName);

            // Then
            Map<String, Logger> loggers = getLoggersMap();
            assertThat(loggers).containsKey(loggerName);
            assertThat(loggers.get(loggerName)).isSameAs(logger);
        }

        @Test
        @DisplayName("Should handle concurrent logger creation")
        void testConcurrentLoggerCreation() throws InterruptedException {
            // Given
            String loggerName = "concurrent.logger";
            Thread[] threads = new Thread[10];
            Logger[] results = new Logger[10];

            // When
            for (int i = 0; i < threads.length; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        results[index] = getLoggerViaReflection(loggerName);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            Logger firstLogger = results[0];
            for (int i = 1; i < results.length; i++) {
                assertThat(results[i]).isSameAs(firstLogger);
            }
        }
    }

    @Nested
    @DisplayName("Logger Properties Tests")
    class LoggerPropertiesTests {

        @Test
        @DisplayName("Should create standard Java logger instances")
        void testLoggerInstanceType() throws Exception {
            // Given
            String loggerName = "instance.type.test";

            // When
            Logger logger = getLoggerViaReflection(loggerName);

            // Then
            assertThat(logger).isInstanceOf(Logger.class);
            // Should be standard Java logger, not wrapped
            assertThat(logger.getClass()).isEqualTo(Logger.class);
        }

        @Test
        @DisplayName("Should preserve logger hierarchy")
        void testLoggerHierarchy() throws Exception {
            // Given
            String parentLoggerName = "com.example";
            String childLoggerName = "com.example.child";

            // When
            getLoggerViaReflection(parentLoggerName); // Create parent first
            Logger childLogger = getLoggerViaReflection(childLoggerName);

            // Then
            assertThat(childLogger.getParent().getName()).isEqualTo(parentLoggerName);
        }

        @Test
        @DisplayName("Should handle root logger correctly")
        void testRootLogger() throws Exception {
            // Given
            String rootLoggerName = "";

            // When
            Logger logger = getLoggerViaReflection(rootLoggerName);

            // Then
            assertThat(logger).isNotNull();
            assertThat(logger.getName()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null logger name gracefully")
        void testNullLoggerName() {
            // This test verifies what happens when null is passed
            // The current implementation throws NPE due to ConcurrentHashMap.get(null)
            assertThatThrownBy(() -> {
                getLoggerViaReflection(null);
            }).isInstanceOf(Exception.class);
        }

        @Test
        @DisplayName("Should handle empty string logger name")
        void testEmptyLoggerName() throws Exception {
            // Given
            String loggerName = "";

            // When
            Logger logger = getLoggerViaReflection(loggerName);

            // Then
            assertThat(logger).isNotNull();
            assertThat(logger.getName()).isEmpty();
        }

        @Test
        @DisplayName("Should handle very long logger names")
        void testVeryLongLoggerName() throws Exception {
            // Given
            StringBuilder longName = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longName.append("very.long.name.segment.").append(i).append(".");
            }
            String loggerName = longName.toString();

            // When
            Logger logger = getLoggerViaReflection(loggerName);

            // Then
            assertThat(logger).isNotNull();
            assertThat(logger.getName()).isEqualTo(loggerName);
        }

        @Test
        @DisplayName("Should handle special characters in logger names")
        void testSpecialCharactersInLoggerName() throws Exception {
            // Given
            String loggerName = "logger.with.unicode.ñáéíóú.and.symbols.@#$%";

            // When
            Logger logger = getLoggerViaReflection(loggerName);

            // Then
            assertThat(logger).isNotNull();
            assertThat(logger.getName()).isEqualTo(loggerName);
        }
    }

    @Nested
    @DisplayName("Performance and Memory Tests")
    class PerformanceTests {

        @RetryingTest(5)
        @DisplayName("Should efficiently handle many logger creations")
        void testManyLoggerCreations() throws Exception {
            // Given
            int loggerCount = 1000;
            Logger[] loggers = new Logger[loggerCount];

            // When
            long startTime = System.nanoTime();
            for (int i = 0; i < loggerCount; i++) {
                loggers[i] = getLoggerViaReflection("performance.logger." + i);
            }
            long endTime = System.nanoTime();

            // Then
            assertThat(loggers).hasSize(loggerCount);
            for (Logger logger : loggers) {
                assertThat(logger).isNotNull();
            }

            // Performance should be reasonable (less than 1 second for 1000 loggers)
            long durationMs = (endTime - startTime) / 1_000_000;
            assertThat(durationMs).isLessThan(1000);
        }

        @Test
        @DisplayName("Should maintain consistent cache size")
        void testCacheSize() throws Exception {
            // Given
            String[] loggerNames = { "cache.size.1", "cache.size.2", "cache.size.3" };

            // When
            for (String loggerName : loggerNames) {
                getLoggerViaReflection(loggerName);
                getLoggerViaReflection(loggerName); // Call twice to test caching
            }

            // Then
            Map<String, Logger> loggers = getLoggersMap();
            assertThat(loggers).hasSize(loggerNames.length);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with typical usage patterns")
        void testTypicalUsagePatterns() throws Exception {
            // Given - Typical class-based logger names
            String[] typicalNames = {
                    "com.example.service.UserService",
                    "com.example.dao.UserDao",
                    "com.example.controller.UserController",
                    "com.example.util.StringUtils",
                    "com.example.Main"
            };

            // When
            Logger[] loggers = new Logger[typicalNames.length];
            for (int i = 0; i < typicalNames.length; i++) {
                loggers[i] = getLoggerViaReflection(typicalNames[i]);
            }

            // Then
            for (int i = 0; i < loggers.length; i++) {
                assertThat(loggers[i]).isNotNull();
                assertThat(loggers[i].getName()).isEqualTo(typicalNames[i]);
            }

            // Verify caching
            for (int i = 0; i < typicalNames.length; i++) {
                Logger cachedLogger = getLoggerViaReflection(typicalNames[i]);
                assertThat(cachedLogger).isSameAs(loggers[i]);
            }
        }

        @Test
        @DisplayName("Should maintain thread safety")
        void testThreadSafety() throws InterruptedException {
            // Given
            String loggerName = "thread.safety.test";
            int threadCount = 20;
            Thread[] threads = new Thread[threadCount];
            Logger[] results = new Logger[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        // Multiple calls from same thread
                        Logger logger1 = getLoggerViaReflection(loggerName);
                        Logger logger2 = getLoggerViaReflection(loggerName);
                        assertThat(logger1).isSameAs(logger2);
                        results[index] = logger1;
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            Logger firstResult = results[0];
            for (int i = 1; i < results.length; i++) {
                assertThat(results[i]).isSameAs(firstResult);
            }
        }
    }

    // Helper methods for reflection access

    private Logger getLoggerViaReflection(String loggerName) throws Exception {
        Method getLoggerMethod = SpecsLoggers.class.getDeclaredMethod("getLogger", String.class);
        getLoggerMethod.setAccessible(true);
        return (Logger) getLoggerMethod.invoke(null, loggerName);
    }

    @SuppressWarnings("unchecked")
    private Map<String, Logger> getLoggersMap() throws Exception {
        Field loggersField = SpecsLoggers.class.getDeclaredField("LOGGERS");
        loggersField.setAccessible(true);
        return (Map<String, Logger>) loggersField.get(null);
    }
}
