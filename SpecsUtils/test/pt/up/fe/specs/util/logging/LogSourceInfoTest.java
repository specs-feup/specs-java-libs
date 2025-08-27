package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.RetryingTest;

/**
 * Comprehensive test suite for LogSourceInfo enum.
 * 
 * Tests the log source information tracking including enum constants,
 * level mapping, configuration management, and concurrent access patterns.
 * 
 * @author Generated Tests
 */
@DisplayName("LogSourceInfo Tests")
class LogSourceInfoTest {

    private Map<Level, LogSourceInfo> originalMapping;

    @BeforeEach
    void setUp() throws Exception {
        // Save original state of LOGGER_SOURCE_INFO map
        Field mapField = LogSourceInfo.class.getDeclaredField("LOGGER_SOURCE_INFO");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Level, LogSourceInfo> sourceInfoMap = (Map<Level, LogSourceInfo>) mapField.get(null);
        originalMapping = new ConcurrentHashMap<>(sourceInfoMap);
    }

    @AfterEach
    void tearDown() throws Exception {
        // Restore original state
        Field mapField = LogSourceInfo.class.getDeclaredField("LOGGER_SOURCE_INFO");
        mapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<Level, LogSourceInfo> sourceInfoMap = (Map<Level, LogSourceInfo>) mapField.get(null);
        sourceInfoMap.clear();
        sourceInfoMap.putAll(originalMapping);
    }

    @Nested
    @DisplayName("Enum Constants Tests")
    class EnumConstantsTests {

        @Test
        @DisplayName("Should have all expected enum constants")
        void testEnumConstants() {
            // When
            LogSourceInfo[] values = LogSourceInfo.values();

            // Then
            assertThat(values).hasSize(3);
            assertThat(values).containsExactly(
                    LogSourceInfo.NONE,
                    LogSourceInfo.SOURCE,
                    LogSourceInfo.STACK_TRACE);
        }

        @Test
        @DisplayName("Should have correct ordinal values")
        void testOrdinalValues() {
            // Then
            assertThat(LogSourceInfo.NONE.ordinal()).isEqualTo(0);
            assertThat(LogSourceInfo.SOURCE.ordinal()).isEqualTo(1);
            assertThat(LogSourceInfo.STACK_TRACE.ordinal()).isEqualTo(2);
        }

        @Test
        @DisplayName("Should have correct string representations")
        void testStringRepresentations() {
            // Then
            assertThat(LogSourceInfo.NONE.toString()).isEqualTo("NONE");
            assertThat(LogSourceInfo.SOURCE.toString()).isEqualTo("SOURCE");
            assertThat(LogSourceInfo.STACK_TRACE.toString()).isEqualTo("STACK_TRACE");
        }

        @Test
        @DisplayName("Should support valueOf operations")
        void testValueOf() {
            // Then
            assertThat(LogSourceInfo.valueOf("NONE")).isEqualTo(LogSourceInfo.NONE);
            assertThat(LogSourceInfo.valueOf("SOURCE")).isEqualTo(LogSourceInfo.SOURCE);
            assertThat(LogSourceInfo.valueOf("STACK_TRACE")).isEqualTo(LogSourceInfo.STACK_TRACE);
        }

        @Test
        @DisplayName("Should throw exception for invalid valueOf")
        void testInvalidValueOf() {
            // Then
            assertThatThrownBy(() -> LogSourceInfo.valueOf("INVALID"))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("Default Configuration Tests")
    class DefaultConfigurationTests {

        @Test
        @DisplayName("Should have WARNING level configured with STACK_TRACE by default")
        void testDefaultWarningConfiguration() {
            // When
            LogSourceInfo warningInfo = LogSourceInfo.getLogSourceInfo(Level.WARNING);

            // Then
            assertThat(warningInfo).isEqualTo(LogSourceInfo.STACK_TRACE);
        }

        @Test
        @DisplayName("Should return NONE for unconfigured levels")
        void testUnconfiguredLevels() {
            // Given
            Level[] unconfiguredLevels = {
                    Level.SEVERE,
                    Level.INFO,
                    Level.CONFIG,
                    Level.FINE,
                    Level.FINER,
                    Level.FINEST,
                    Level.ALL,
                    Level.OFF
            };

            // When/Then
            for (Level level : unconfiguredLevels) {
                LogSourceInfo info = LogSourceInfo.getLogSourceInfo(level);
                assertThat(info).isEqualTo(LogSourceInfo.NONE);
            }
        }

        @Test
        @DisplayName("Should handle custom LogLevel properly")
        void testCustomLogLevel() {
            // Given
            Level customLevel = LogLevel.LIB;

            // When
            LogSourceInfo info = LogSourceInfo.getLogSourceInfo(customLevel);

            // Then
            assertThat(info).isEqualTo(LogSourceInfo.NONE);
        }

        @Test
        @DisplayName("Should verify default mapping is properly initialized")
        void testDefaultMappingInitialization() throws Exception {
            // Given
            Field mapField = LogSourceInfo.class.getDeclaredField("LOGGER_SOURCE_INFO");
            mapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<Level, LogSourceInfo> sourceInfoMap = (Map<Level, LogSourceInfo>) mapField.get(null);

            // Then
            assertThat(sourceInfoMap).isNotEmpty();
            assertThat(sourceInfoMap).containsEntry(Level.WARNING, LogSourceInfo.STACK_TRACE);
            assertThat(sourceInfoMap).hasSize(1); // Only WARNING should be configured by default
        }
    }

    @Nested
    @DisplayName("Configuration Management Tests")
    class ConfigurationManagementTests {

        @Test
        @DisplayName("Should set and get log source info for levels")
        void testSetAndGetLogSourceInfo() {
            // Given
            Level testLevel = Level.SEVERE;
            LogSourceInfo expectedInfo = LogSourceInfo.SOURCE;

            // When
            LogSourceInfo.setLogSourceInfo(testLevel, expectedInfo);
            LogSourceInfo actualInfo = LogSourceInfo.getLogSourceInfo(testLevel);

            // Then
            assertThat(actualInfo).isEqualTo(expectedInfo);
        }

        @Test
        @DisplayName("Should override existing configuration")
        void testOverrideExistingConfiguration() {
            // Given
            Level level = Level.WARNING;
            LogSourceInfo originalInfo = LogSourceInfo.getLogSourceInfo(level);
            LogSourceInfo newInfo = LogSourceInfo.SOURCE;

            // When
            LogSourceInfo.setLogSourceInfo(level, newInfo);
            LogSourceInfo updatedInfo = LogSourceInfo.getLogSourceInfo(level);

            // Then
            assertThat(originalInfo).isEqualTo(LogSourceInfo.STACK_TRACE);
            assertThat(updatedInfo).isEqualTo(newInfo);
            assertThat(updatedInfo).isNotEqualTo(originalInfo);
        }

        @Test
        @DisplayName("Should configure multiple levels independently")
        void testMultipleLevelsConfiguration() {
            // Given
            Level[] levels = { Level.SEVERE, Level.INFO, Level.FINE };
            LogSourceInfo[] infos = { LogSourceInfo.STACK_TRACE, LogSourceInfo.SOURCE, LogSourceInfo.NONE };

            // When
            for (int i = 0; i < levels.length; i++) {
                LogSourceInfo.setLogSourceInfo(levels[i], infos[i]);
            }

            // Then
            for (int i = 0; i < levels.length; i++) {
                assertThat(LogSourceInfo.getLogSourceInfo(levels[i])).isEqualTo(infos[i]);
            }
        }

        @Test
        @DisplayName("Should handle null level gracefully")
        void testNullLevel() {
            // When/Then - Getting info for null level throws NPE due to ConcurrentHashMap
            assertThatThrownBy(() -> LogSourceInfo.getLogSourceInfo(null))
                    .isInstanceOf(NullPointerException.class);

            // Setting info for null level should also throw NPE
            assertThatThrownBy(() -> LogSourceInfo.setLogSourceInfo(null, LogSourceInfo.SOURCE))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null LogSourceInfo in configuration")
        void testNullLogSourceInfo() {
            // Given
            Level testLevel = Level.CONFIG;

            // When/Then - ConcurrentHashMap does not allow null values either
            assertThatThrownBy(() -> LogSourceInfo.setLogSourceInfo(testLevel, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should support all enum values in configuration")
        void testAllEnumValuesConfiguration() {
            // Given
            Level[] levels = { Level.SEVERE, Level.INFO, Level.FINE };
            LogSourceInfo[] allValues = LogSourceInfo.values();

            // When/Then
            for (int i = 0; i < levels.length && i < allValues.length; i++) {
                LogSourceInfo.setLogSourceInfo(levels[i], allValues[i]);
                assertThat(LogSourceInfo.getLogSourceInfo(levels[i])).isEqualTo(allValues[i]);
            }
        }
    }

    @Nested
    @DisplayName("Concurrent Access Tests")
    class ConcurrentAccessTests {

        @Test
        @DisplayName("Should handle concurrent read access safely")
        void testConcurrentReads() throws InterruptedException {
            // Given
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];
            LogSourceInfo[] results = new LogSourceInfo[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    results[index] = LogSourceInfo.getLogSourceInfo(Level.WARNING);
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            for (LogSourceInfo result : results) {
                assertThat(result).isEqualTo(LogSourceInfo.STACK_TRACE);
            }
        }

        @Test
        @DisplayName("Should handle concurrent write access safely")
        void testConcurrentWrites() throws InterruptedException {
            // Given
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];
            Level[] levels = new Level[threadCount];

            // Initialize unique levels for each thread
            for (int i = 0; i < threadCount; i++) {
                levels[i] = Level.parse(String.valueOf(1000 + i * 10));
            }

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    LogSourceInfo.setLogSourceInfo(levels[index], LogSourceInfo.SOURCE);
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            for (Level level : levels) {
                assertThat(LogSourceInfo.getLogSourceInfo(level)).isEqualTo(LogSourceInfo.SOURCE);
            }
        }

        @Test
        @DisplayName("Should handle concurrent read-write access safely")
        void testConcurrentReadWrite() throws InterruptedException {
            // Given
            Level testLevel = Level.FINE;
            int readerCount = 5;
            int writerCount = 5;
            Thread[] readers = new Thread[readerCount];
            Thread[] writers = new Thread[writerCount];
            LogSourceInfo[] readResults = new LogSourceInfo[readerCount];

            // When
            // Start readers
            for (int i = 0; i < readerCount; i++) {
                final int index = i;
                readers[i] = new Thread(() -> {
                    readResults[index] = LogSourceInfo.getLogSourceInfo(testLevel);
                });
            }

            // Start writers
            for (int i = 0; i < writerCount; i++) {
                final int index = i;
                writers[i] = new Thread(() -> {
                    LogSourceInfo info = (index % 2 == 0) ? LogSourceInfo.SOURCE : LogSourceInfo.STACK_TRACE;
                    LogSourceInfo.setLogSourceInfo(testLevel, info);
                });
            }

            // Execute concurrently
            for (Thread reader : readers)
                reader.start();
            for (Thread writer : writers)
                writer.start();

            for (Thread reader : readers)
                reader.join();
            for (Thread writer : writers)
                writer.join();

            // Then
            for (LogSourceInfo result : readResults) {
                assertThat(result).isIn(LogSourceInfo.NONE, LogSourceInfo.SOURCE, LogSourceInfo.STACK_TRACE);
            }

            // Final state should be consistent
            LogSourceInfo finalInfo = LogSourceInfo.getLogSourceInfo(testLevel);
            assertThat(finalInfo).isIn(LogSourceInfo.SOURCE, LogSourceInfo.STACK_TRACE);
        }
    }

    @Nested
    @DisplayName("Integration and Usage Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with standard logging levels")
        void testStandardLoggingLevels() {
            // Given
            Level[] standardLevels = {
                    Level.SEVERE,
                    Level.WARNING,
                    Level.INFO,
                    Level.CONFIG,
                    Level.FINE,
                    Level.FINER,
                    Level.FINEST
            };

            // When/Then
            for (Level level : standardLevels) {
                // Should not throw exception
                LogSourceInfo info = LogSourceInfo.getLogSourceInfo(level);
                assertThat(info).isNotNull();
                assertThat(info).isIn((Object[]) LogSourceInfo.values());

                // Should be able to configure
                LogSourceInfo.setLogSourceInfo(level, LogSourceInfo.SOURCE);
                assertThat(LogSourceInfo.getLogSourceInfo(level)).isEqualTo(LogSourceInfo.SOURCE);
            }
        }

        @Test
        @DisplayName("Should work with custom log levels")
        void testCustomLogLevels() {
            // Given
            Level customLevel1 = LogLevel.LIB;
            Level customLevel2 = Level.parse("850"); // Custom numeric level

            // When/Then
            LogSourceInfo info1 = LogSourceInfo.getLogSourceInfo(customLevel1);
            LogSourceInfo info2 = LogSourceInfo.getLogSourceInfo(customLevel2);

            assertThat(info1).isEqualTo(LogSourceInfo.NONE);
            assertThat(info2).isEqualTo(LogSourceInfo.NONE);

            // Should be configurable
            LogSourceInfo.setLogSourceInfo(customLevel1, LogSourceInfo.STACK_TRACE);
            LogSourceInfo.setLogSourceInfo(customLevel2, LogSourceInfo.SOURCE);

            assertThat(LogSourceInfo.getLogSourceInfo(customLevel1)).isEqualTo(LogSourceInfo.STACK_TRACE);
            assertThat(LogSourceInfo.getLogSourceInfo(customLevel2)).isEqualTo(LogSourceInfo.SOURCE);
        }

        @Test
        @DisplayName("Should support typical logging configuration scenarios")
        void testTypicalConfigurationScenarios() {
            // Scenario 1: Debug configuration - show source for all levels
            Level[] debugLevels = { Level.FINE, Level.FINER, Level.FINEST };
            for (Level level : debugLevels) {
                LogSourceInfo.setLogSourceInfo(level, LogSourceInfo.SOURCE);
            }

            // Scenario 2: Production configuration - stack trace for errors only
            LogSourceInfo.setLogSourceInfo(Level.SEVERE, LogSourceInfo.STACK_TRACE);
            LogSourceInfo.setLogSourceInfo(Level.WARNING, LogSourceInfo.NONE); // Override default

            // Verify configurations
            for (Level level : debugLevels) {
                assertThat(LogSourceInfo.getLogSourceInfo(level)).isEqualTo(LogSourceInfo.SOURCE);
            }

            assertThat(LogSourceInfo.getLogSourceInfo(Level.SEVERE)).isEqualTo(LogSourceInfo.STACK_TRACE);
            assertThat(LogSourceInfo.getLogSourceInfo(Level.WARNING)).isEqualTo(LogSourceInfo.NONE);
        }

        @RetryingTest(5)
        @DisplayName("Should maintain performance with many configurations")
        void testPerformanceWithManyConfigurations() {
            // Given - Configure many levels
            int levelCount = 1000;
            Level[] levels = new Level[levelCount];
            for (int i = 0; i < levelCount; i++) {
                levels[i] = Level.parse(String.valueOf(i));
                LogSourceInfo.setLogSourceInfo(levels[i], LogSourceInfo.values()[i % 3]);
            }

            // When - Measure lookup performance
            long startTime = System.nanoTime();
            for (Level level : levels) {
                LogSourceInfo.getLogSourceInfo(level);
            }
            long endTime = System.nanoTime();

            // Then - Should complete in reasonable time (less than 10ms for 1000 lookups)
            long durationMs = (endTime - startTime) / 1_000_000;
            assertThat(durationMs).isLessThan(10);
        }
    }

    @Nested
    @DisplayName("Internal State Tests")
    class InternalStateTests {

        @Test
        @DisplayName("Should use ConcurrentHashMap for thread safety")
        void testInternalMapType() throws Exception {
            // Given
            Field mapField = LogSourceInfo.class.getDeclaredField("LOGGER_SOURCE_INFO");
            mapField.setAccessible(true);
            Object map = mapField.get(null);

            // Then
            assertThat(map).isInstanceOf(ConcurrentHashMap.class);
        }

        @Test
        @DisplayName("Should maintain mapping consistency after operations")
        void testMappingConsistency() throws Exception {
            // Given
            Field mapField = LogSourceInfo.class.getDeclaredField("LOGGER_SOURCE_INFO");
            mapField.setAccessible(true);
            @SuppressWarnings("unchecked")
            Map<Level, LogSourceInfo> map = (Map<Level, LogSourceInfo>) mapField.get(null);

            int originalSize = map.size();

            // When
            Level testLevel = Level.CONFIG;
            LogSourceInfo.setLogSourceInfo(testLevel, LogSourceInfo.SOURCE);

            // Then
            assertThat(map).hasSize(originalSize + 1);
            assertThat(map).containsEntry(testLevel, LogSourceInfo.SOURCE);

            // Verify via public API
            assertThat(LogSourceInfo.getLogSourceInfo(testLevel)).isEqualTo(LogSourceInfo.SOURCE);
        }
    }
}
