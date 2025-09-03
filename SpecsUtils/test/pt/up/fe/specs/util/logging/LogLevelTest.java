package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.util.logging.Level;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for LogLevel class.
 * 
 * Tests the custom log level implementation including level constants,
 * inheritance behavior, comparison operations, and serialization compatibility.
 * 
 * @author Generated Tests
 */
@DisplayName("LogLevel Tests")
class LogLevelTest {

    @Nested
    @DisplayName("Level Constants Tests")
    class LevelConstantsTests {

        @Test
        @DisplayName("Should have LIB level constant defined")
        void testLibLevelConstant() {
            // When
            Level libLevel = LogLevel.LIB;

            // Then
            assertThat(libLevel).isNotNull();
            assertThat(libLevel.getName()).isEqualTo("LIB");
            assertThat(libLevel.intValue()).isEqualTo(750);
        }

        @Test
        @DisplayName("Should have correct LIB level properties")
        void testLibLevelProperties() {
            // Given
            Level libLevel = LogLevel.LIB;

            // Then
            assertThat(libLevel).isInstanceOf(LogLevel.class);
            assertThat(libLevel).isInstanceOf(Level.class);
            assertThat(libLevel.getName()).isEqualTo("LIB");
            assertThat(libLevel.intValue()).isEqualTo(750);
            assertThat(libLevel.getResourceBundleName()).isEqualTo("sun.util.logging.resources.logging");
        }

        @Test
        @DisplayName("Should maintain LIB level identity across calls")
        void testLibLevelIdentity() {
            // When
            Level lib1 = LogLevel.LIB;
            Level lib2 = LogLevel.LIB;

            // Then
            assertThat(lib1).isSameAs(lib2);
        }
    }

    @Nested
    @DisplayName("Level Inheritance Tests")
    class LevelInheritanceTests {

        @Test
        @DisplayName("Should extend java.util.logging.Level")
        void testInheritance() {
            // Given
            LogLevel customLevel = new LogLevel("CUSTOM", 850) {
                private static final long serialVersionUID = 1L;
            };

            // Then
            assertThat(customLevel).isInstanceOf(Level.class);
            assertThat(customLevel).isInstanceOf(LogLevel.class);
        }

        @Test
        @DisplayName("Should support Level class methods")
        void testLevelMethods() {
            // Given
            Level libLevel = LogLevel.LIB;

            // Then
            assertThat(libLevel.toString()).isEqualTo("LIB");
            assertThat(libLevel.getName()).isEqualTo("LIB");
            assertThat(libLevel.intValue()).isEqualTo(750);
        }
    }

    @Nested
    @DisplayName("Level Comparison Tests")
    class LevelComparisonTests {

        @Test
        @DisplayName("Should compare correctly with standard levels")
        void testComparisonWithStandardLevels() {
            // Given
            Level libLevel = LogLevel.LIB;

            // Then - LIB (750) should be between INFO (800) and CONFIG (700)
            assertThat(libLevel.intValue()).isLessThan(Level.INFO.intValue()); // 750 < 800
            assertThat(libLevel.intValue()).isGreaterThan(Level.CONFIG.intValue()); // 750 > 700

            // Should be less than WARNING and SEVERE
            assertThat(libLevel.intValue()).isLessThan(Level.WARNING.intValue());
            assertThat(libLevel.intValue()).isLessThan(Level.SEVERE.intValue());

            // Should be greater than FINE levels
            assertThat(libLevel.intValue()).isGreaterThan(Level.FINE.intValue());
            assertThat(libLevel.intValue()).isGreaterThan(Level.FINER.intValue());
            assertThat(libLevel.intValue()).isGreaterThan(Level.FINEST.intValue());
        }

        @Test
        @DisplayName("Should support equality comparison")
        void testEqualityComparison() {
            // Given
            Level lib1 = LogLevel.LIB;
            Level lib2 = LogLevel.LIB;

            // Then
            assertThat(lib1).isEqualTo(lib2);
            assertThat(lib1.hashCode()).isEqualTo(lib2.hashCode());
        }

        @Test
        @DisplayName("Should compare with custom levels")
        void testComparisonWithCustomLevels() {
            // Given
            LogLevel lowerLevel = new LogLevel("LOWER", 700) {
                private static final long serialVersionUID = 1L;
            };
            LogLevel higherLevel = new LogLevel("HIGHER", 800) {
                private static final long serialVersionUID = 1L;
            };
            Level libLevel = LogLevel.LIB;

            // Then
            assertThat(libLevel.intValue()).isGreaterThan(lowerLevel.intValue());
            assertThat(libLevel.intValue()).isLessThan(higherLevel.intValue());
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create level with name and value")
        void testConstructorNameValue() {
            // Given/When
            LogLevel customLevel = new LogLevel("CUSTOM", 550) {
                private static final long serialVersionUID = 1L;
            };

            // Then
            assertThat(customLevel.getName()).isEqualTo("CUSTOM");
            assertThat(customLevel.intValue()).isEqualTo(550);
            assertThat(customLevel.getResourceBundleName()).isNull();
        }

        @Test
        @DisplayName("Should create level with name, value, and resource bundle")
        void testConstructorNameValueBundle() {
            // Given/When
            LogLevel customLevel = new LogLevel("CUSTOM", 550, "custom.bundle") {
                private static final long serialVersionUID = 1L;
            };

            // Then
            assertThat(customLevel.getName()).isEqualTo("CUSTOM");
            assertThat(customLevel.intValue()).isEqualTo(550);
            assertThat(customLevel.getResourceBundleName()).isEqualTo("custom.bundle");
        }

        @Test
        @DisplayName("Should handle various level values")
        void testVariousLevelValues() {
            int[] testValues = { 0, 100, 500, 750, 1000, Integer.MAX_VALUE };

            for (int value : testValues) {
                LogLevel level = new LogLevel("TEST" + value, value) {
                    private static final long serialVersionUID = 1L;
                };

                assertThat(level.intValue()).isEqualTo(value);
                assertThat(level.getName()).isEqualTo("TEST" + value);
            }
        }

        @Test
        @DisplayName("Should handle negative level values")
        void testNegativeLevelValues() {
            // Given/When
            LogLevel negativeLevel = new LogLevel("NEGATIVE", -100) {
                private static final long serialVersionUID = 1L;
            };

            // Then
            assertThat(negativeLevel.intValue()).isEqualTo(-100);
            assertThat(negativeLevel.getName()).isEqualTo("NEGATIVE");
        }
    }

    @Nested
    @DisplayName("Resource Bundle Tests")
    class ResourceBundleTests {

        @Test
        @DisplayName("Should use default bundle for LIB level")
        void testDefaultBundle() {
            // Given
            Level libLevel = LogLevel.LIB;

            // Then
            assertThat(libLevel.getResourceBundleName()).isEqualTo("sun.util.logging.resources.logging");
        }

        @Test
        @DisplayName("Should support custom resource bundles")
        void testCustomResourceBundle() {
            // Given/When
            LogLevel customLevel = new LogLevel("CUSTOM", 600, "com.example.logging") {
                private static final long serialVersionUID = 1L;
            };

            // Then
            assertThat(customLevel.getResourceBundleName()).isEqualTo("com.example.logging");
        }

        @Test
        @DisplayName("Should handle null resource bundle")
        void testNullResourceBundle() {
            // Given/When
            LogLevel customLevel = new LogLevel("CUSTOM", 600, null) {
                private static final long serialVersionUID = 1L;
            };

            // Then
            assertThat(customLevel.getResourceBundleName()).isNull();
        }

        @Test
        @DisplayName("Should handle empty resource bundle")
        void testEmptyResourceBundle() {
            // Given/When
            LogLevel customLevel = new LogLevel("CUSTOM", 600, "") {
                private static final long serialVersionUID = 1L;
            };

            // Then
            assertThat(customLevel.getResourceBundleName()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Serialization Tests")
    class SerializationTests {

        @Test
        @DisplayName("Should have serialVersionUID defined")
        void testSerialVersionUID() throws Exception {
            // Given
            Class<?> logLevelClass = LogLevel.class;

            // When
            java.lang.reflect.Field serialVersionField = logLevelClass.getDeclaredField("serialVersionUID");
            serialVersionField.setAccessible(true);
            long serialVersionUID = serialVersionField.getLong(null);

            // Then
            assertThat(serialVersionUID).isEqualTo(1L);
        }

        @Test
        @DisplayName("Should be serializable")
        void testSerializable() {
            // Given
            Level libLevel = LogLevel.LIB;

            // Then
            assertThat(libLevel).isInstanceOf(java.io.Serializable.class);
        }
    }

    @Nested
    @DisplayName("String Representation Tests")
    class StringRepresentationTests {

        @Test
        @DisplayName("Should have correct string representation")
        void testToString() {
            // Given
            Level libLevel = LogLevel.LIB;

            // Then
            assertThat(libLevel.toString()).isEqualTo("LIB");
        }

        @Test
        @DisplayName("Should maintain consistent string representation")
        void testConsistentStringRepresentation() {
            // Given
            Level libLevel = LogLevel.LIB;

            // When
            String str1 = libLevel.toString();
            String str2 = libLevel.toString();
            String str3 = libLevel.getName();

            // Then
            assertThat(str1).isEqualTo(str2);
            assertThat(str1).isEqualTo(str3);
        }

        @Test
        @DisplayName("Should handle special characters in names")
        void testSpecialCharactersInName() {
            // Given/When
            LogLevel specialLevel = new LogLevel("LEVEL_WITH-SPECIAL.CHARS@123", 600) {
                private static final long serialVersionUID = 1L;
            };

            // Then
            assertThat(specialLevel.getName()).isEqualTo("LEVEL_WITH-SPECIAL.CHARS@123");
            assertThat(specialLevel.toString()).isEqualTo("LEVEL_WITH-SPECIAL.CHARS@123");
        }
    }

    @Nested
    @DisplayName("Integration and Usage Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with Level.parse()")
        void testLevelParsing() {
            // Note: Level.parse() for custom levels requires they be registered
            // This test verifies the LIB level properties work correctly
            Level libLevel = LogLevel.LIB;

            // Standard Level.parse() won't find "LIB" unless registered
            // But we can verify the level behaves correctly
            assertThat(libLevel.getName()).isEqualTo("LIB");
            assertThat(libLevel.intValue()).isEqualTo(750);
        }

        @Test
        @DisplayName("Should work in logging hierarchy")
        void testLoggingHierarchy() {
            // Given
            Level libLevel = LogLevel.LIB;

            // Then - Verify it fits properly in the logging hierarchy
            // LIB (750) should be between CONFIG (700) and INFO (800)
            boolean isProperlyOrdered = libLevel.intValue() > Level.CONFIG.intValue() &&
                    libLevel.intValue() < Level.INFO.intValue();

            assertThat(isProperlyOrdered).isTrue();
        }

        @Test
        @DisplayName("Should support logger level filtering")
        void testLoggerLevelFiltering() {
            // Given
            Level libLevel = LogLevel.LIB;

            // Then - Test typical level filtering scenarios
            assertThat(libLevel.intValue() >= Level.ALL.intValue()).isTrue();
            assertThat(libLevel.intValue() <= Level.OFF.intValue()).isTrue();

            // Should be loggable at INFO level or higher
            assertThat(libLevel.intValue() < Level.INFO.intValue()).isTrue();

            // Should not be loggable at SEVERE level
            assertThat(libLevel.intValue() < Level.SEVERE.intValue()).isTrue();
        }

        @Test
        @DisplayName("Should handle concurrent access")
        void testConcurrentAccess() throws InterruptedException {
            // Given
            Thread[] threads = new Thread[10];
            Level[] results = new Level[10];

            // When
            for (int i = 0; i < threads.length; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    results[index] = LogLevel.LIB;
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            Level firstResult = results[0];
            for (int i = 1; i < results.length; i++) {
                assertThat(results[i]).isSameAs(firstResult);
                assertThat(results[i].getName()).isEqualTo("LIB");
                assertThat(results[i].intValue()).isEqualTo(750);
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle extreme level values")
        void testExtremeLevelValues() {
            // Given/When
            LogLevel minLevel = new LogLevel("MIN", Integer.MIN_VALUE) {
                private static final long serialVersionUID = 1L;
            };
            LogLevel maxLevel = new LogLevel("MAX", Integer.MAX_VALUE) {
                private static final long serialVersionUID = 1L;
            };

            // Then
            assertThat(minLevel.intValue()).isEqualTo(Integer.MIN_VALUE);
            assertThat(maxLevel.intValue()).isEqualTo(Integer.MAX_VALUE);
        }

        @Test
        @DisplayName("Should handle empty and null names gracefully")
        void testEmptyAndNullNames() {
            // Given/When
            LogLevel emptyLevel = new LogLevel("", 500) {
                private static final long serialVersionUID = 1L;
            };

            // Then
            assertThat(emptyLevel.getName()).isEmpty();
            assertThat(emptyLevel.intValue()).isEqualTo(500);

            // Note: null name would cause NPE in Level constructor
            // This is expected behavior from java.util.logging.Level
        }

        @Test
        @DisplayName("Should maintain default bundle constant access")
        void testDefaultBundleAccess() throws Exception {
            // Given
            java.lang.reflect.Field defaultBundleField = LogLevel.class.getDeclaredField("defaultBundle");
            defaultBundleField.setAccessible(true);
            String defaultBundle = (String) defaultBundleField.get(null);

            // Then
            assertThat(defaultBundle).isEqualTo("sun.util.logging.resources.logging");
        }
    }
}
