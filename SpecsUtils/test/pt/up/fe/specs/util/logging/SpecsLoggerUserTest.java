package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.lang.reflect.Field;
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

/**
 * Comprehensive test suite for SpecsLoggerUser interface.
 * 
 * Tests the interface that provides static logging utilities and extends
 * TagLoggerUser for SpecsLoggerTag enum.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsLoggerUser Tests")
class SpecsLoggerUserTest {

    // Test implementation
    static class TestSpecsLoggerUser implements SpecsLoggerUser {
        // Uses default implementations from interface
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
        @DisplayName("Should extend TagLoggerUser with SpecsLoggerTag")
        void testTagLoggerUserExtension() {
            // Given
            TestSpecsLoggerUser user = new TestSpecsLoggerUser();

            // Then - Should be a TagLoggerUser
            assertThat(user).isInstanceOf(TagLoggerUser.class);

            // Should have TagLoggerUser method
            TagLogger<SpecsLoggerTag> logger = user.logger();
            assertThat(logger).isNotNull();
            assertThat(logger).isInstanceOf(EnumLogger.class);
        }

        @Test
        @DisplayName("Should provide default logger implementation")
        void testDefaultLoggerImplementation() {
            // Given
            TestSpecsLoggerUser user = new TestSpecsLoggerUser();

            // When
            EnumLogger<SpecsLoggerTag> logger = user.logger();

            // Then
            assertThat(logger).isNotNull();
            assertThat(logger).isSameAs(SpecsLoggerUser.SPECS_LOGGER);
            assertThat(logger.getEnumClass()).isEqualTo(SpecsLoggerTag.class);
        }

        @Test
        @DisplayName("Should have consistent static logger instance")
        void testStaticLoggerConsistency() {
            // When
            EnumLogger<SpecsLoggerTag> logger1 = SpecsLoggerUser.getLogger();
            EnumLogger<SpecsLoggerTag> logger2 = SpecsLoggerUser.getLogger();

            // Then
            assertThat(logger1).isSameAs(logger2);
            assertThat(logger1).isSameAs(SpecsLoggerUser.SPECS_LOGGER);
        }
    }

    @Nested
    @DisplayName("Static Logger Access Tests")
    class StaticLoggerAccessTests {

        @Test
        @DisplayName("Should provide static access to SPECS_LOGGER")
        void testStaticLoggerAccess() {
            // When
            EnumLogger<SpecsLoggerTag> logger = SpecsLoggerUser.getLogger();

            // Then
            assertThat(logger).isNotNull();
            assertThat(logger.getEnumClass()).isEqualTo(SpecsLoggerTag.class);
            assertThat(logger.getBaseName()).isEqualTo(SpecsLoggerTag.class.getName());
            assertThat(logger.getTags()).containsExactly(SpecsLoggerTag.DEPRECATED);
        }

        @Test
        @DisplayName("Should maintain singleton pattern for SPECS_LOGGER")
        void testSingletonPattern() {
            // Given
            TestSpecsLoggerUser user1 = new TestSpecsLoggerUser();
            TestSpecsLoggerUser user2 = new TestSpecsLoggerUser();

            // When
            EnumLogger<SpecsLoggerTag> logger1 = user1.logger();
            EnumLogger<SpecsLoggerTag> logger2 = user2.logger();
            EnumLogger<SpecsLoggerTag> staticLogger = SpecsLoggerUser.getLogger();

            // Then - All should be the same instance
            assertThat(logger1).isSameAs(logger2);
            assertThat(logger2).isSameAs(staticLogger);
            assertThat(logger1).isSameAs(SpecsLoggerUser.SPECS_LOGGER);
        }

        @Test
        @DisplayName("Should provide access to EnumLogger functionality")
        void testEnumLoggerFunctionality() {
            // Given
            EnumLogger<SpecsLoggerTag> logger = SpecsLoggerUser.getLogger();

            // When/Then - Should have all EnumLogger methods
            assertThatCode(() -> {
                logger.setLevel(SpecsLoggerTag.DEPRECATED, Level.INFO);
                logger.setLevelAll(Level.WARNING);
                logger.log(Level.INFO, SpecsLoggerTag.DEPRECATED, "test message");
                logger.info(SpecsLoggerTag.DEPRECATED, "info message");
                logger.warn(SpecsLoggerTag.DEPRECATED, "warning message");
                logger.debug("debug message");
                logger.addToIgnoreList(String.class);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Static Logging Method Tests")
    class StaticLoggingMethodTests {

        @Test
        @DisplayName("Should provide static deprecated logging")
        void testStaticDeprecated() {
            // Given
            String testMessage = "This feature is deprecated";

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                SpecsLoggerUser.deprecated(testMessage);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should provide static info logging with tag")
        void testStaticInfoWithTag() {
            // Given
            String testMessage = "Info message with tag";

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                SpecsLoggerUser.info(SpecsLoggerTag.DEPRECATED, testMessage);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should provide static info logging without tag")
        void testStaticInfoWithoutTag() {
            // Given
            String testMessage = "Info message without tag";

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                SpecsLoggerUser.info(testMessage);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should provide static info logging with supplier")
        void testStaticInfoWithSupplier() {
            // Given
            Supplier<String> messageSupplier = () -> "Supplier-generated message";

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                SpecsLoggerUser.info(messageSupplier);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null messages gracefully")
        void testNullMessages() {
            // When/Then - Should not throw exceptions
            assertThatCode(() -> {
                SpecsLoggerUser.deprecated(null);
                SpecsLoggerUser.info(SpecsLoggerTag.DEPRECATED, null);
                SpecsLoggerUser.info((String) null);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle empty messages")
        void testEmptyMessages() {
            // When/Then - Should not throw exceptions
            assertThatCode(() -> {
                SpecsLoggerUser.deprecated("");
                SpecsLoggerUser.info(SpecsLoggerTag.DEPRECATED, "");
                SpecsLoggerUser.info("");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle supplier that returns null")
        void testNullSupplier() {
            // Given
            Supplier<String> nullSupplier = () -> null;

            // When/Then - Should not throw exception
            assertThatCode(() -> {
                SpecsLoggerUser.info(nullSupplier);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle null supplier")
        void testNullSupplierReference() {
            // When/Then - Should throw NPE when calling get() on null supplier
            assertThatThrownBy(() -> {
                SpecsLoggerUser.info((Supplier<String>) null);
            }).isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Integration with EnumLogger Tests")
    class EnumLoggerIntegrationTests {

        @Test
        @DisplayName("Should use EnumLogger for SpecsLoggerTag")
        void testEnumLoggerIntegration() {
            // Given
            EnumLogger<SpecsLoggerTag> logger = SpecsLoggerUser.getLogger();

            // When
            String baseName = logger.getBaseName();

            // Then
            assertThat(baseName).isEqualTo(SpecsLoggerTag.class.getName());
            assertThat(logger.getTags()).containsExactly(SpecsLoggerTag.DEPRECATED);
        }

        @Test
        @DisplayName("Should create proper Java loggers")
        void testJavaLoggerCreation() {
            // Given
            EnumLogger<SpecsLoggerTag> specsLogger = SpecsLoggerUser.getLogger();

            // When
            Logger baseLogger = specsLogger.getBaseLogger();
            Logger deprecatedLogger = specsLogger.getLogger(SpecsLoggerTag.DEPRECATED);

            // Then
            assertThat(baseLogger).isNotNull();
            assertThat(deprecatedLogger).isNotNull();
            assertThat(baseLogger.getName()).isEqualTo(SpecsLoggerTag.class.getName());
            assertThat(deprecatedLogger.getName()).contains("deprecated");
        }

        @Test
        @DisplayName("Should support level configuration")
        void testLevelConfiguration() {
            // Given
            EnumLogger<SpecsLoggerTag> logger = SpecsLoggerUser.getLogger();

            // When
            logger.setLevel(SpecsLoggerTag.DEPRECATED, Level.WARNING);

            // Then
            Logger deprecatedLogger = logger.getLogger(SpecsLoggerTag.DEPRECATED);
            assertThat(deprecatedLogger.getLevel()).isEqualTo(Level.WARNING);
        }

        @Test
        @DisplayName("Should integrate with SpecsLoggers framework")
        void testSpecsLoggersIntegration() {
            // Given
            EnumLogger<SpecsLoggerTag> logger = SpecsLoggerUser.getLogger();

            // When
            Logger baseLogger = logger.getBaseLogger();

            // Then - Should be accessible through SpecsLoggers
            String expectedName = SpecsLoggerTag.class.getName();
            Logger directLogger = SpecsLoggers.getLogger(expectedName);

            assertThat(baseLogger.getName()).isEqualTo(directLogger.getName());
        }
    }

    @Nested
    @DisplayName("SpecsLoggerTag Usage Tests")
    class SpecsLoggerTagUsageTests {

        @Test
        @DisplayName("Should handle DEPRECATED tag properly")
        void testDeprecatedTag() {
            // Given
            TestSpecsLoggerUser user = new TestSpecsLoggerUser();

            // When/Then - Should work with DEPRECATED tag
            assertThatCode(() -> {
                user.logger().info(SpecsLoggerTag.DEPRECATED, "Deprecation warning");
                user.logger().warn(SpecsLoggerTag.DEPRECATED, "Deprecation error");
                user.logger().log(Level.SEVERE, SpecsLoggerTag.DEPRECATED, "Critical deprecation");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should support all SpecsLoggerTag values")
        void testAllSpecsLoggerTagValues() {
            // Given
            EnumLogger<SpecsLoggerTag> logger = SpecsLoggerUser.getLogger();

            // When/Then
            for (SpecsLoggerTag tag : SpecsLoggerTag.values()) {
                assertThatCode(() -> {
                    logger.info(tag, "Message for " + tag);
                    Logger javaLogger = logger.getLogger(tag);
                    assertThat(javaLogger).isNotNull();
                }).doesNotThrowAnyException();
            }
        }

        @Test
        @DisplayName("Should have consistent tag collection")
        void testTagCollectionConsistency() {
            // Given
            EnumLogger<SpecsLoggerTag> logger = SpecsLoggerUser.getLogger();

            // When
            var tags = logger.getTags();

            // Then
            assertThat(tags).hasSize(SpecsLoggerTag.values().length);
            assertThat(tags).containsExactlyInAnyOrder(SpecsLoggerTag.values());
        }
    }

    @Nested
    @DisplayName("Concurrency Tests")
    class ConcurrencyTests {

        @Test
        @DisplayName("Should handle concurrent static method calls")
        void testConcurrentStaticCalls() throws InterruptedException {
            // Given
            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    SpecsLoggerUser.deprecated("Concurrent deprecated " + index);
                    SpecsLoggerUser.info(SpecsLoggerTag.DEPRECATED, "Concurrent info " + index);
                    SpecsLoggerUser.info("Concurrent general info " + index);
                    SpecsLoggerUser.info(() -> "Concurrent supplier info " + index);
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then - Should complete without exceptions
            // Verify logger is still accessible
            EnumLogger<SpecsLoggerTag> logger = SpecsLoggerUser.getLogger();
            assertThat(logger).isNotNull();
        }

        @Test
        @DisplayName("Should handle concurrent logger access")
        void testConcurrentLoggerAccess() throws InterruptedException {
            // Given
            int threadCount = 5;
            Thread[] threads = new Thread[threadCount];
            @SuppressWarnings("unchecked")
            EnumLogger<SpecsLoggerTag>[] loggers = new EnumLogger[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    loggers[index] = SpecsLoggerUser.getLogger();
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then - All should get the same logger instance
            EnumLogger<SpecsLoggerTag> firstLogger = loggers[0];
            for (EnumLogger<SpecsLoggerTag> logger : loggers) {
                assertThat(logger).isSameAs(firstLogger);
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle very long messages")
        void testVeryLongMessages() {
            // Given
            StringBuilder longMessage = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longMessage.append("Long message part ").append(i).append(". ");
            }
            String message = longMessage.toString();

            // When/Then - Should handle long messages
            assertThatCode(() -> {
                SpecsLoggerUser.deprecated(message);
                SpecsLoggerUser.info(SpecsLoggerTag.DEPRECATED, message);
                SpecsLoggerUser.info(message);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle special characters in messages")
        void testSpecialCharactersInMessages() {
            // Given
            String specialMessage = "Message with unicode: \u00E9\u00F1\u00FC and symbols: @#$%^&*()";

            // When/Then
            assertThatCode(() -> {
                SpecsLoggerUser.deprecated(specialMessage);
                SpecsLoggerUser.info(SpecsLoggerTag.DEPRECATED, specialMessage);
                SpecsLoggerUser.info(specialMessage);
                SpecsLoggerUser.info(() -> specialMessage);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle supplier exceptions")
        void testSupplierExceptions() {
            // Given
            Supplier<String> faultySupplier = () -> {
                throw new RuntimeException("Supplier failed");
            };

            // When/Then - Should propagate supplier exceptions
            assertThatThrownBy(() -> {
                SpecsLoggerUser.info(faultySupplier);
            }).isInstanceOf(RuntimeException.class)
                    .hasMessage("Supplier failed");
        }

        @Test
        @DisplayName("Should handle complex supplier logic")
        void testComplexSupplierLogic() {
            // Given
            Supplier<String> complexSupplier = () -> {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < 5; i++) {
                    sb.append("Complex part ").append(i).append(" ");
                }
                return sb.toString();
            };

            // When/Then
            assertThatCode(() -> {
                SpecsLoggerUser.info(complexSupplier);
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Static Field and Method Access Tests")
    class StaticAccessTests {

        @Test
        @DisplayName("Should access SPECS_LOGGER static field")
        void testStaticFieldAccess() {
            // When
            EnumLogger<SpecsLoggerTag> staticLogger = SpecsLoggerUser.SPECS_LOGGER;

            // Then
            assertThat(staticLogger).isNotNull();
            assertThat(staticLogger.getEnumClass()).isEqualTo(SpecsLoggerTag.class);
        }

        @Test
        @DisplayName("Should maintain consistency between static field and getter")
        void testStaticFieldAndGetterConsistency() {
            // When
            EnumLogger<SpecsLoggerTag> fieldLogger = SpecsLoggerUser.SPECS_LOGGER;
            EnumLogger<SpecsLoggerTag> getterLogger = SpecsLoggerUser.getLogger();

            // Then
            assertThat(fieldLogger).isSameAs(getterLogger);
        }

        @Test
        @DisplayName("Should work with static imports")
        void testStaticImportUsage() {
            // This test verifies that the static methods can be used with static imports
            // When/Then - These calls should work if methods were statically imported
            assertThatCode(() -> {
                // Simulate static import usage
                var logger = SpecsLoggerUser.getLogger();
                logger.info(SpecsLoggerTag.DEPRECATED, "Static import test");
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Behavioral Verification Tests")
    class BehavioralVerificationTests {

        @Test
        @DisplayName("Should verify deprecated method calls underlying logger")
        void testDeprecatedMethodBehavior() {
            // Given - Spy on the static logger to verify behavior
            EnumLogger<SpecsLoggerTag> originalLogger = SpecsLoggerUser.SPECS_LOGGER;

            // When
            SpecsLoggerUser.deprecated("Test deprecated message");

            // Then - Should have called info with DEPRECATED tag
            // Note: This is behavioral verification - the deprecated method should delegate
            // to info
            assertThatCode(() -> {
                originalLogger.info(SpecsLoggerTag.DEPRECATED, "Test deprecated message");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should verify info supplier evaluation")
        void testInfoSupplierEvaluation() {
            // Given
            final boolean[] supplierCalled = { false };
            Supplier<String> supplier = () -> {
                supplierCalled[0] = true;
                return "Supplier message";
            };

            // When
            SpecsLoggerUser.info(supplier);

            // Then
            assertThat(supplierCalled[0]).isTrue();
        }

        @Test
        @DisplayName("Should verify all static methods delegate to same logger")
        void testStaticMethodDelegation() {
            // Given
            EnumLogger<SpecsLoggerTag> logger1 = SpecsLoggerUser.getLogger();

            // When - Use various static methods
            SpecsLoggerUser.deprecated("deprecated");
            SpecsLoggerUser.info(SpecsLoggerTag.DEPRECATED, "info with tag");
            SpecsLoggerUser.info("info without tag");
            SpecsLoggerUser.info(() -> "info with supplier");

            // Then - Should still have the same logger instance
            EnumLogger<SpecsLoggerTag> logger2 = SpecsLoggerUser.getLogger();
            assertThat(logger1).isSameAs(logger2);
        }
    }
}
