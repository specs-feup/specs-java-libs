package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Comprehensive test suite for StringLoggerUser interface.
 * 
 * Tests the functional interface that provides access to StringLogger
 * instances.
 * 
 * @author Generated Tests
 */
@DisplayName("StringLoggerUser Tests")
@ExtendWith(MockitoExtension.class)
class StringLoggerUserTest {

    // Test implementation
    static class TestStringLoggerUser implements StringLoggerUser {
        private final StringLogger stringLogger;

        public TestStringLoggerUser(StringLogger stringLogger) {
            this.stringLogger = stringLogger;
        }

        @Override
        public StringLogger getLogger() {
            return stringLogger;
        }
    }

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should require implementation of getLogger method")
        void testAbstractMethod() {
            // Given
            StringLogger mockStringLogger = mock(StringLogger.class);
            TestStringLoggerUser user = new TestStringLoggerUser(mockStringLogger);

            // When
            StringLogger result = user.getLogger();

            // Then
            assertThat(result).isSameAs(mockStringLogger);
        }

        @Test
        @DisplayName("Should be a functional interface")
        void testFunctionalInterface() {
            // Given
            StringLogger mockStringLogger = mock(StringLogger.class);

            // When - Use as lambda expression
            StringLoggerUser lambdaUser = () -> mockStringLogger;

            // Then
            assertThat(lambdaUser.getLogger()).isSameAs(mockStringLogger);
        }

        @Test
        @DisplayName("Should support method references")
        void testMethodReference() {
            // Given
            StringLogger mockStringLogger = mock(StringLogger.class);
            TestStringLoggerUser user = new TestStringLoggerUser(mockStringLogger);

            // When - Use method reference
            java.util.function.Supplier<StringLogger> supplier = user::getLogger;

            // Then
            assertThat(supplier.get()).isSameAs(mockStringLogger);
        }
    }

    @Nested
    @DisplayName("StringLogger Access Tests")
    class StringLoggerAccessTests {

        @Test
        @DisplayName("Should provide access to StringLogger instance")
        void testStringLoggerAccess() {
            // Given
            Set<String> tags = new HashSet<>(Arrays.asList("access", "test"));
            StringLogger stringLogger = new StringLogger("access.test", tags);
            TestStringLoggerUser user = new TestStringLoggerUser(stringLogger);

            // When
            StringLogger result = user.getLogger();

            // Then
            assertThat(result).isSameAs(stringLogger);
            assertThat(result.getBaseName()).isEqualTo("access.test");
            assertThat(result.getTags()).containsExactlyInAnyOrder("access", "test");
        }

        @Test
        @DisplayName("Should allow null StringLogger")
        void testNullStringLogger() {
            // Given
            TestStringLoggerUser user = new TestStringLoggerUser(null);

            // When
            StringLogger result = user.getLogger();

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should maintain StringLogger reference consistency")
        void testStringLoggerConsistency() {
            // Given
            StringLogger stringLogger = new StringLogger("consistency.test");
            TestStringLoggerUser user = new TestStringLoggerUser(stringLogger);

            // When
            StringLogger result1 = user.getLogger();
            StringLogger result2 = user.getLogger();
            StringLogger result3 = user.getLogger();

            // Then
            assertThat(result1).isSameAs(result2);
            assertThat(result2).isSameAs(result3);
            assertThat(result1).isSameAs(stringLogger);
        }
    }

    @Nested
    @DisplayName("Usage Pattern Tests")
    class UsagePatternTests {

        @Test
        @DisplayName("Should enable convenient logging through delegation")
        void testLoggingDelegation() {
            // Given
            Set<String> tags = new HashSet<>(Arrays.asList("delegation"));
            StringLogger stringLogger = spy(new StringLogger("delegation.test", tags));
            TestStringLoggerUser user = new TestStringLoggerUser(stringLogger);

            // When - Use through StringLoggerUser
            user.getLogger().info("delegation", "Test message");
            user.getLogger().warn("delegation", "Warning message");
            user.getLogger().debug("Debug message");

            // Then - Verify delegation occurred
            verify(stringLogger).info("delegation", "Test message");
            verify(stringLogger).warn("delegation", "Warning message");
            verify(stringLogger).debug("Debug message");
        }

        @Test
        @DisplayName("Should support fluent interface patterns")
        void testFluentInterface() {
            // Given
            StringLogger stringLogger = spy(new StringLogger("fluent.test"));
            TestStringLoggerUser user = new TestStringLoggerUser(stringLogger);

            // When/Then - Should support method chaining through logger
            assertThatCode(() -> {
                user.getLogger()
                        .addToIgnoreList(String.class)
                        .addToIgnoreList(Integer.class);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should support logger configuration through user")
        void testLoggerConfiguration() {
            // Given
            Set<String> tags = new HashSet<>(Arrays.asList("config1", "config2"));
            StringLogger stringLogger = spy(new StringLogger("config.test", tags));
            TestStringLoggerUser user = new TestStringLoggerUser(stringLogger);

            // When
            user.getLogger().setLevel("config1", Level.WARNING);
            user.getLogger().setLevelAll(Level.INFO);

            // Then
            verify(stringLogger).setLevel("config1", Level.WARNING);
            verify(stringLogger).setLevelAll(Level.INFO);
        }
    }

    @Nested
    @DisplayName("String-Specific Tests")
    class StringSpecificTests {

        @Test
        @DisplayName("Should work with string tags")
        void testStringTags() {
            // Given
            Set<String> stringTags = new HashSet<>(Arrays.asList("string1", "string2", "string3"));
            StringLogger stringLogger = new StringLogger("string.tags", stringTags);
            StringLoggerUser user = () -> stringLogger;

            // When
            StringLogger result = user.getLogger();

            // Then
            assertThat(result.getTags()).containsExactlyInAnyOrderElementsOf(stringTags);

            // Verify string tag usage
            assertThatCode(() -> {
                result.info("string1", "Message for string1");
                result.warn("string2", "Message for string2");
                result.log(Level.SEVERE, "string3", "Message for string3");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle various string formats in tags")
        void testVariousStringFormats() {
            // Given
            Set<String> variousTags = new HashSet<>(Arrays.asList(
                    "simple", "with-hyphens", "with_underscores",
                    "with.dots", "UPPERCASE", "MixedCase",
                    "with spaces", "with@symbols", "123numeric"));
            StringLogger stringLogger = new StringLogger("various.formats", variousTags);
            StringLoggerUser user = () -> stringLogger;

            // When/Then
            StringLogger logger = user.getLogger();
            for (String tag : variousTags) {
                assertThatCode(() -> {
                    logger.info(tag, "Message for tag: " + tag);
                    Logger javaLogger = logger.getLogger(tag);
                    assertThat(javaLogger).isNotNull();
                }).doesNotThrowAnyException();
            }
        }

        @Test
        @DisplayName("Should work with empty string tags")
        void testEmptyStringTags() {
            // Given
            StringLogger stringLogger = new StringLogger("empty.string.tags");
            StringLoggerUser user = () -> stringLogger;

            // When/Then
            assertThatCode(() -> {
                StringLogger logger = user.getLogger();
                logger.info("", "Message with empty tag");
                logger.warn("", "Warning with empty tag");
                logger.getLogger(""); // Should not throw
            }).doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Lambda and Functional Tests")
    class LambdaAndFunctionalTests {

        @Test
        @DisplayName("Should work with complex lambda expressions")
        void testComplexLambda() {
            // Given
            StringLogger primaryLogger = new StringLogger("primary.lambda");
            StringLogger fallbackLogger = new StringLogger("fallback.lambda");

            // When - Complex lambda with conditional logic
            StringLoggerUser conditionalUser = () -> {
                if (System.currentTimeMillis() % 2 == 0) {
                    return primaryLogger;
                } else {
                    return fallbackLogger;
                }
            };

            // Then - Should return a valid logger
            StringLogger result = conditionalUser.getLogger();
            assertThat(result).isIn(primaryLogger, fallbackLogger);
        }

        @Test
        @DisplayName("Should support functional composition")
        void testFunctionalComposition() {
            // Given
            StringLogger baseLogger = new StringLogger("composition.base");

            // When - Compose with function
            StringLoggerUser user = () -> baseLogger;
            java.util.function.Function<StringLoggerUser, String> nameExtractor = slu -> slu.getLogger().getBaseName();

            // Then
            String baseName = nameExtractor.apply(user);
            assertThat(baseName).isEqualTo("composition.base");
        }

        @Test
        @DisplayName("Should work in stream operations")
        void testStreamOperations() {
            // Given
            StringLogger logger1 = new StringLogger("stream.1");
            StringLogger logger2 = new StringLogger("stream.2");
            StringLogger logger3 = new StringLogger("stream.3");

            StringLoggerUser[] users = {
                    () -> logger1,
                    () -> logger2,
                    () -> logger3
            };

            // When
            java.util.List<String> baseNames = Arrays.stream(users)
                    .map(StringLoggerUser::getLogger)
                    .map(StringLogger::getBaseName)
                    .collect(java.util.stream.Collectors.toList());

            // Then
            assertThat(baseNames).containsExactly("stream.1", "stream.2", "stream.3");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should integrate with actual StringLogger implementations")
        void testRealStringLoggerIntegration() {
            // Given - Create a real StringLogger implementation
            Set<String> realTags = new HashSet<>(Arrays.asList("integration", "real", "test"));
            StringLogger realStringLogger = new StringLogger("integration.real.test", realTags);

            StringLoggerUser user = () -> realStringLogger;

            // When - Use all major StringLogger functionality through user
            StringLogger logger = user.getLogger();
            logger.setLevelAll(Level.INFO);

            // Then - Should create real Java loggers
            Logger integrationLogger = logger.getLogger("integration");
            Logger realLogger = logger.getLogger("real");
            Logger testLogger = logger.getLogger("test");

            assertThat(integrationLogger).isNotNull();
            assertThat(realLogger).isNotNull();
            assertThat(testLogger).isNotNull();
            assertThat(integrationLogger.getName()).contains("integration");
            assertThat(realLogger.getName()).contains("real");
            assertThat(testLogger.getName()).contains("test");
        }

        @Test
        @DisplayName("Should work in composite patterns")
        void testCompositePattern() {
            // Given - Multiple StringLoggerUser instances
            StringLogger logger1 = new StringLogger("composite.1");
            StringLogger logger2 = new StringLogger("composite.2");
            StringLogger logger3 = new StringLogger("composite.3");

            StringLoggerUser user1 = () -> logger1;
            StringLoggerUser user2 = () -> logger2;
            StringLoggerUser user3 = () -> logger3;

            StringLoggerUser[] users = { user1, user2, user3 };

            // When/Then - All should be accessible
            for (int i = 0; i < users.length; i++) {
                StringLogger logger = users[i].getLogger();
                assertThat(logger).isNotNull();
                assertThat(logger.getBaseName()).isEqualTo("composite." + (i + 1));
            }
        }

        @Test
        @DisplayName("Should support concurrent access")
        void testConcurrentAccess() throws InterruptedException {
            // Given
            StringLogger sharedLogger = new StringLogger("concurrent.string.test");
            StringLoggerUser user = () -> sharedLogger;

            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];
            boolean[] results = new boolean[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        StringLogger logger = user.getLogger();
                        results[index] = (logger == sharedLogger);

                        // Use the logger
                        logger.info("concurrent", "Concurrent message " + index);
                    } catch (Exception e) {
                        results[index] = false;
                    }
                });
                threads[i].start();
            }

            for (Thread thread : threads) {
                thread.join();
            }

            // Then
            for (boolean result : results) {
                assertThat(result).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("Edge Cases Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle exceptions in getLogger method")
        void testExceptionInGetLogger() {
            // Given
            StringLoggerUser faultyUser = () -> {
                throw new RuntimeException("StringLogger creation failed");
            };

            // When/Then
            assertThatThrownBy(() -> faultyUser.getLogger())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("StringLogger creation failed");
        }

        @Test
        @DisplayName("Should handle changing StringLogger references")
        void testChangingStringLoggerReference() {
            // Given
            StringLogger logger1 = new StringLogger("changing.1");
            StringLogger logger2 = new StringLogger("changing.2");

            java.util.concurrent.atomic.AtomicReference<StringLogger> currentLogger = new java.util.concurrent.atomic.AtomicReference<>(
                    logger1);
            StringLoggerUser user = () -> currentLogger.get();

            // When
            StringLogger result1 = user.getLogger();
            currentLogger.set(logger2); // Change reference
            StringLogger result2 = user.getLogger();

            // Then
            assertThat(result1).isSameAs(logger1);
            assertThat(result2).isSameAs(logger2);
            assertThat(result1).isNotSameAs(result2);
        }

        @Test
        @DisplayName("Should handle recursive StringLogger creation")
        void testRecursiveCreation() {
            // Given - Recursive lambda (should be used carefully)
            final StringLoggerUser[] recursiveUser = new StringLoggerUser[1];
            recursiveUser[0] = () -> {
                // Create logger that could potentially reference itself
                StringLogger base = new StringLogger("recursive.base");
                return base;
            };

            // When/Then - Should work without infinite recursion
            assertThatCode(() -> {
                StringLogger logger = recursiveUser[0].getLogger();
                assertThat(logger).isNotNull();
                assertThat(logger.getBaseName()).isEqualTo("recursive.base");
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should handle complex inheritance scenarios")
        void testInheritanceScenarios() {
            // Given - Abstract implementation
            abstract class AbstractStringLoggerUser implements StringLoggerUser {
                protected final StringLogger stringLogger;

                public AbstractStringLoggerUser(StringLogger stringLogger) {
                    this.stringLogger = stringLogger;
                }

                @Override
                public StringLogger getLogger() {
                    return stringLogger;
                }

                public abstract String getUserType();
            }

            // Concrete implementation
            class ConcreteStringLoggerUser extends AbstractStringLoggerUser {
                public ConcreteStringLoggerUser(StringLogger stringLogger) {
                    super(stringLogger);
                }

                @Override
                public String getUserType() {
                    return "concrete";
                }
            }

            StringLogger stringLogger = new StringLogger("inheritance.test");
            ConcreteStringLoggerUser user = new ConcreteStringLoggerUser(stringLogger);

            // When
            StringLogger result = user.getLogger();

            // Then
            assertThat(result).isSameAs(stringLogger);
            assertThat(user.getUserType()).isEqualTo("concrete");
        }
    }
}
