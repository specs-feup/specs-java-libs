package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Comprehensive test suite for TagLoggerUser interface.
 * 
 * Tests the functional interface that provides access to TagLogger instances.
 * 
 * @author Generated Tests
 */
@DisplayName("TagLoggerUser Tests")
@ExtendWith(MockitoExtension.class)
class TagLoggerUserTest {

    // Test enums for testing
    enum TestTag {
        FEATURE_A, FEATURE_B, FEATURE_C
    }

    enum SimpleTag {
        X, Y, Z
    }

    // Test implementations
    static class TestTagLoggerUser implements TagLoggerUser<TestTag> {
        private final TagLogger<TestTag> tagLogger;

        public TestTagLoggerUser(TagLogger<TestTag> tagLogger) {
            this.tagLogger = tagLogger;
        }

        @Override
        public TagLogger<TestTag> logger() {
            return tagLogger;
        }
    }

    static class SimpleTagLogger implements TagLogger<TestTag> {
        private final String baseName;
        private final Collection<TestTag> tags;

        public SimpleTagLogger(String baseName, Collection<TestTag> tags) {
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

    @Nested
    @DisplayName("Interface Contract Tests")
    class InterfaceContractTests {

        @Test
        @DisplayName("Should require implementation of logger method")
        void testAbstractMethod() {
            // Given
            @SuppressWarnings("unchecked")
            TagLogger<TestTag> mockTagLogger = mock(TagLogger.class);
            TestTagLoggerUser user = new TestTagLoggerUser(mockTagLogger);

            // When
            TagLogger<TestTag> result = user.logger();

            // Then
            assertThat(result).isSameAs(mockTagLogger);
        }

        @Test
        @DisplayName("Should be a functional interface")
        void testFunctionalInterface() {
            // Given
            @SuppressWarnings("unchecked")
            TagLogger<TestTag> mockTagLogger = mock(TagLogger.class);

            // When - Use as lambda expression
            TagLoggerUser<TestTag> lambdaUser = () -> mockTagLogger;

            // Then
            assertThat(lambdaUser.logger()).isSameAs(mockTagLogger);
        }

        @Test
        @DisplayName("Should support method references")
        void testMethodReference() {
            // Given
            @SuppressWarnings("unchecked")
            TagLogger<TestTag> mockTagLogger = mock(TagLogger.class);
            TestTagLoggerUser user = new TestTagLoggerUser(mockTagLogger);

            // When - Use method reference
            java.util.function.Supplier<TagLogger<TestTag>> supplier = user::logger;

            // Then
            assertThat(supplier.get()).isSameAs(mockTagLogger);
        }
    }

    @Nested
    @DisplayName("Logger Access Tests")
    class LoggerAccessTests {

        @Test
        @DisplayName("Should provide access to TagLogger instance")
        void testLoggerAccess() {
            // Given
            TagLogger<TestTag> tagLogger = new SimpleTagLogger("test.logger",
                    Arrays.asList(TestTag.FEATURE_A, TestTag.FEATURE_B));
            TestTagLoggerUser user = new TestTagLoggerUser(tagLogger);

            // When
            TagLogger<TestTag> result = user.logger();

            // Then
            assertThat(result).isSameAs(tagLogger);
            assertThat(result.getBaseName()).isEqualTo("test.logger");
            assertThat(result.getTags()).containsExactly(TestTag.FEATURE_A, TestTag.FEATURE_B);
        }

        @Test
        @DisplayName("Should allow null logger")
        void testNullLogger() {
            // Given
            TestTagLoggerUser user = new TestTagLoggerUser(null);

            // When
            TagLogger<TestTag> result = user.logger();

            // Then
            assertThat(result).isNull();
        }

        @Test
        @DisplayName("Should maintain logger reference consistency")
        void testLoggerConsistency() {
            // Given
            TagLogger<TestTag> tagLogger = new SimpleTagLogger("consistent.test",
                    Arrays.asList(TestTag.FEATURE_C));
            TestTagLoggerUser user = new TestTagLoggerUser(tagLogger);

            // When
            TagLogger<TestTag> result1 = user.logger();
            TagLogger<TestTag> result2 = user.logger();
            TagLogger<TestTag> result3 = user.logger();

            // Then
            assertThat(result1).isSameAs(result2);
            assertThat(result2).isSameAs(result3);
            assertThat(result1).isSameAs(tagLogger);
        }
    }

    @Nested
    @DisplayName("Usage Pattern Tests")
    class UsagePatternTests {

        @Test
        @DisplayName("Should enable convenient logging through delegation")
        void testLoggingDelegation() {
            // Given
            TagLogger<TestTag> tagLogger = spy(new SimpleTagLogger("delegation.test",
                    Arrays.asList(TestTag.FEATURE_A)));
            TestTagLoggerUser user = new TestTagLoggerUser(tagLogger);

            // When - Use through TagLoggerUser
            user.logger().info(TestTag.FEATURE_A, "Test message");
            user.logger().warn(TestTag.FEATURE_A, "Warning message");
            user.logger().debug("Debug message");

            // Then - Verify delegation occurred
            verify(tagLogger).info(TestTag.FEATURE_A, "Test message");
            verify(tagLogger).warn(TestTag.FEATURE_A, "Warning message");
            verify(tagLogger).debug("Debug message");
        }

        @Test
        @DisplayName("Should support fluent interface patterns")
        void testFluentInterface() {
            // Given
            TagLogger<TestTag> tagLogger = spy(new SimpleTagLogger("fluent.test",
                    Arrays.asList(TestTag.FEATURE_B)));
            TestTagLoggerUser user = new TestTagLoggerUser(tagLogger);

            // When/Then - Should support method chaining through logger
            assertThatCode(() -> {
                user.logger()
                        .addToIgnoreList(String.class)
                        .addToIgnoreList(Integer.class);
            }).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should support logger configuration through user")
        void testLoggerConfiguration() {
            // Given
            TagLogger<TestTag> tagLogger = spy(new SimpleTagLogger("config.test",
                    Arrays.asList(TestTag.FEATURE_A, TestTag.FEATURE_B)));
            TestTagLoggerUser user = new TestTagLoggerUser(tagLogger);

            // When
            user.logger().setLevel(TestTag.FEATURE_A, Level.WARNING);
            user.logger().setLevelAll(Level.INFO);

            // Then
            verify(tagLogger).setLevel(TestTag.FEATURE_A, Level.WARNING);
            verify(tagLogger).setLevelAll(Level.INFO);
        }
    }

    @Nested
    @DisplayName("Generic Type Tests")
    class GenericTypeTests {

        @Test
        @DisplayName("Should work with different enum types")
        void testDifferentEnumTypes() {
            // Given
            TagLogger<SimpleTag> simpleTagLogger = new TagLogger<SimpleTag>() {
                @Override
                public Collection<SimpleTag> getTags() {
                    return Arrays.asList(SimpleTag.X, SimpleTag.Y);
                }

                @Override
                public String getBaseName() {
                    return "simple.tag.test";
                }
            };

            TagLoggerUser<SimpleTag> user = () -> simpleTagLogger;

            // When
            TagLogger<SimpleTag> result = user.logger();

            // Then
            assertThat(result).isSameAs(simpleTagLogger);
            assertThat(result.getTags()).containsExactly(SimpleTag.X, SimpleTag.Y);
            assertThat(result.getBaseName()).isEqualTo("simple.tag.test");
        }

        @Test
        @DisplayName("Should maintain type safety with generics")
        void testTypeSafety() {
            // Given
            TagLogger<TestTag> testTagLogger = new SimpleTagLogger("type.safe",
                    Arrays.asList(TestTag.FEATURE_A));
            TagLoggerUser<TestTag> testUser = () -> testTagLogger;

            TagLogger<SimpleTag> simpleTagLogger = new TagLogger<SimpleTag>() {
                @Override
                public Collection<SimpleTag> getTags() {
                    return Arrays.asList(SimpleTag.Z);
                }

                @Override
                public String getBaseName() {
                    return "simple.safe";
                }
            };
            TagLoggerUser<SimpleTag> simpleUser = () -> simpleTagLogger;

            // When/Then - Types should be enforced
            TagLogger<TestTag> testResult = testUser.logger();
            TagLogger<SimpleTag> simpleResult = simpleUser.logger();

            assertThat(testResult.getTags()).allMatch(tag -> tag instanceof TestTag);
            assertThat(simpleResult.getTags()).allMatch(tag -> tag instanceof SimpleTag);
        }

        @Test
        @DisplayName("Should support inheritance in generic types")
        void testGenericInheritance() {
            // Given - Abstract implementation
            abstract class AbstractTagLoggerUser<T> implements TagLoggerUser<T> {
                protected final TagLogger<T> tagLogger;

                public AbstractTagLoggerUser(TagLogger<T> tagLogger) {
                    this.tagLogger = tagLogger;
                }

                @Override
                public TagLogger<T> logger() {
                    return tagLogger;
                }
            }

            // Concrete implementation
            class ConcreteTestTagLoggerUser extends AbstractTagLoggerUser<TestTag> {
                public ConcreteTestTagLoggerUser(TagLogger<TestTag> tagLogger) {
                    super(tagLogger);
                }
            }

            TagLogger<TestTag> tagLogger = new SimpleTagLogger("inheritance.test",
                    Arrays.asList(TestTag.FEATURE_C));
            ConcreteTestTagLoggerUser user = new ConcreteTestTagLoggerUser(tagLogger);

            // When
            TagLogger<TestTag> result = user.logger();

            // Then
            assertThat(result).isSameAs(tagLogger);
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should integrate with actual TagLogger implementations")
        void testRealTagLoggerIntegration() {
            // Given - Create a real TagLogger implementation
            TagLogger<TestTag> realTagLogger = new TagLogger<TestTag>() {
                @Override
                public Collection<TestTag> getTags() {
                    return Arrays.asList(TestTag.FEATURE_A, TestTag.FEATURE_B, TestTag.FEATURE_C);
                }

                @Override
                public String getBaseName() {
                    return "integration.test";
                }
            };

            TagLoggerUser<TestTag> user = () -> realTagLogger;

            // When - Use all major TagLogger functionality through user
            TagLogger<TestTag> logger = user.logger();
            logger.setLevelAll(Level.INFO);

            // Then - Should create real loggers
            Logger parserLogger = logger.getLogger(TestTag.FEATURE_A);
            Logger analyzerLogger = logger.getLogger(TestTag.FEATURE_B);

            assertThat(parserLogger).isNotNull();
            assertThat(analyzerLogger).isNotNull();
            assertThat(parserLogger.getName()).contains("feature_a");
            assertThat(analyzerLogger.getName()).contains("feature_b");
        }

        @Test
        @DisplayName("Should work in composite patterns")
        void testCompositePattern() {
            // Given - Multiple TagLoggerUser instances
            TagLogger<TestTag> logger1 = new SimpleTagLogger("composite.1",
                    Arrays.asList(TestTag.FEATURE_A));
            TagLogger<TestTag> logger2 = new SimpleTagLogger("composite.2",
                    Arrays.asList(TestTag.FEATURE_B));
            TagLogger<TestTag> logger3 = new SimpleTagLogger("composite.3",
                    Arrays.asList(TestTag.FEATURE_C));

            TagLoggerUser<TestTag> user1 = () -> logger1;
            TagLoggerUser<TestTag> user2 = () -> logger2;
            TagLoggerUser<TestTag> user3 = () -> logger3;

            @SuppressWarnings("unchecked")
            TagLoggerUser<TestTag>[] users = new TagLoggerUser[] { user1, user2, user3 };

            // When/Then - All should be accessible
            for (int i = 0; i < users.length; i++) {
                TagLogger<TestTag> logger = users[i].logger();
                assertThat(logger).isNotNull();
                assertThat(logger.getBaseName()).isEqualTo("composite." + (i + 1));
            }
        }

        @Test
        @DisplayName("Should support concurrent access")
        void testConcurrentAccess() throws InterruptedException {
            // Given
            TagLogger<TestTag> sharedLogger = new SimpleTagLogger("concurrent.test",
                    Arrays.asList(TestTag.FEATURE_A, TestTag.FEATURE_B, TestTag.FEATURE_C));
            TagLoggerUser<TestTag> user = () -> sharedLogger;

            int threadCount = 10;
            Thread[] threads = new Thread[threadCount];
            boolean[] results = new boolean[threadCount];

            // When
            for (int i = 0; i < threadCount; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        TagLogger<TestTag> logger = user.logger();
                        results[index] = (logger == sharedLogger);

                        // Use the logger
                        logger.info(TestTag.FEATURE_A, "Concurrent message " + index);
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
        @DisplayName("Should handle exceptions in logger method")
        void testExceptionInLogger() {
            // Given
            TagLoggerUser<TestTag> faultyUser = () -> {
                throw new RuntimeException("Logger creation failed");
            };

            // When/Then
            assertThatThrownBy(() -> faultyUser.logger())
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Logger creation failed");
        }

        @Test
        @DisplayName("Should handle changing logger references")
        void testChangingLoggerReference() {
            // Given
            TagLogger<TestTag> logger1 = new SimpleTagLogger("changing.1",
                    Arrays.asList(TestTag.FEATURE_A));
            TagLogger<TestTag> logger2 = new SimpleTagLogger("changing.2",
                    Arrays.asList(TestTag.FEATURE_B));

            java.util.concurrent.atomic.AtomicReference<TagLogger<TestTag>> currentLogger = new java.util.concurrent.atomic.AtomicReference<>(
                    logger1);
            TagLoggerUser<TestTag> user = () -> currentLogger.get();

            // When
            TagLogger<TestTag> result1 = user.logger();
            currentLogger.set(logger2); // Change reference
            TagLogger<TestTag> result2 = user.logger();

            // Then
            assertThat(result1).isSameAs(logger1);
            assertThat(result2).isSameAs(logger2);
            assertThat(result1).isNotSameAs(result2);
        }

        @Test
        @DisplayName("Should handle complex lambda expressions")
        void testComplexLambda() {
            // Given
            TagLogger<TestTag> primaryLogger = new SimpleTagLogger("primary",
                    Arrays.asList(TestTag.FEATURE_A));
            TagLogger<TestTag> fallbackLogger = new SimpleTagLogger("fallback",
                    Arrays.asList(TestTag.FEATURE_B));

            // When - Complex lambda with conditional logic
            TagLoggerUser<TestTag> conditionalUser = () -> {
                if (System.currentTimeMillis() % 2 == 0) {
                    return primaryLogger;
                } else {
                    return fallbackLogger;
                }
            };

            // Then - Should return a valid logger
            TagLogger<TestTag> result = conditionalUser.logger();
            assertThat(result).isIn(primaryLogger, fallbackLogger);
        }
    }
}
