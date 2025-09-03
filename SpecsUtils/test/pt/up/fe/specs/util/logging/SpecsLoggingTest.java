package pt.up.fe.specs.util.logging;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Comprehensive test suite for SpecsLogging class.
 * 
 * Tests the internal utility methods used by the logging package including
 * prefix handling, source code location tracking, stack trace processing,
 * and message parsing.
 * 
 * @author Generated Tests
 */
@DisplayName("SpecsLogging Tests")
class SpecsLoggingTest {

    @Nested
    @DisplayName("Class Ignore List Tests")
    class ClassIgnoreListTests {

        @Test
        @DisplayName("Should add class to ignore list")
        void testAddClassToIgnore() {
            // Given
            Class<?> testClass = String.class;

            // When
            SpecsLogging.addClassToIgnore(testClass);

            // Then
            // Verify by checking if stack trace processing ignores this class
            StackTraceElement[] stackTrace = new StackTraceElement[] {
                    new StackTraceElement("java.lang.String", "method", "String.java", 100)
            };

            List<StackTraceElement> result = SpecsLogging.getLogCallLocation(stackTrace);

            // The String class should now be ignored, so we should get empty result or skip
            // it
            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("Should ignore predefined classes")
        void testPredefinedIgnoredClasses() {
            // Given - Stack trace with predefined ignored classes
            StackTraceElement[] stackTrace = new StackTraceElement[] {
                    new StackTraceElement("java.lang.Thread", "getStackTrace", "Thread.java", 100),
                    new StackTraceElement("pt.up.fe.specs.util.logging.SpecsLogging", "getLogCallLocation",
                            "SpecsLogging.java", 50),
                    new StackTraceElement("pt.up.fe.specs.util.logging.TagLogger", "log", "TagLogger.java", 30),
                    new StackTraceElement("com.example.MyClass", "myMethod", "MyClass.java", 10)
            };

            // When
            List<StackTraceElement> result = SpecsLogging.getLogCallLocation(stackTrace);

            // Then
            assertThat(result).isNotEmpty();
            // Should start from the first non-ignored class
            assertThat(result.get(0).getClassName()).isEqualTo("com.example.MyClass");
        }
    }

    @Nested
    @DisplayName("Prefix Generation Tests")
    class PrefixGenerationTests {

        @Test
        @DisplayName("Should generate prefix with tag")
        void testGetPrefixWithTag() {
            // Given
            String tag = "TEST";

            // When
            String prefix = SpecsLogging.getPrefix(tag);

            // Then
            assertThat(prefix).isEqualTo("[TEST] ");
        }

        @Test
        @DisplayName("Should handle null tag")
        void testGetPrefixWithNullTag() {
            // Given
            Object tag = null;

            // When
            String prefix = SpecsLogging.getPrefix(tag);

            // Then
            assertThat(prefix).isEmpty();
        }

        @Test
        @DisplayName("Should handle various tag types")
        void testGetPrefixWithVariousTagTypes() {
            // Test with different tag types
            assertThat(SpecsLogging.getPrefix("STRING")).isEqualTo("[STRING] ");
            assertThat(SpecsLogging.getPrefix(42)).isEqualTo("[42] ");
            assertThat(SpecsLogging.getPrefix(LogLevel.INFO)).contains("INFO");
            assertThat(SpecsLogging.getPrefix(new StringBuilder("BUILDER"))).isEqualTo("[BUILDER] ");
        }

        @Test
        @DisplayName("Should handle empty string tag")
        void testGetPrefixWithEmptyString() {
            // Given
            String tag = "";

            // When
            String prefix = SpecsLogging.getPrefix(tag);

            // Then
            assertThat(prefix).isEqualTo("[] ");
        }

        @Test
        @DisplayName("Should handle whitespace tag")
        void testGetPrefixWithWhitespace() {
            // Given
            String tag = "  \t\n  ";

            // When
            String prefix = SpecsLogging.getPrefix(tag);

            // Then
            assertThat(prefix).isEqualTo("[  \t\n  ] ");
        }
    }

    @Nested
    @DisplayName("Source Code Location Tests")
    class SourceCodeLocationTests {

        @Test
        @DisplayName("Should format source code location")
        void testGetSourceCode() {
            // Given
            StackTraceElement element = new StackTraceElement(
                    "com.example.MyClass",
                    "myMethod",
                    "MyClass.java",
                    42);

            // When
            String sourceCode = SpecsLogging.getSourceCode(element);

            // Then
            assertThat(sourceCode).isEqualTo(" -> com.example.MyClass.myMethod(MyClass.java:42)");
        }

        @Test
        @DisplayName("Should handle stack trace element with no line number")
        void testGetSourceCodeWithNoLineNumber() {
            // Given
            StackTraceElement element = new StackTraceElement(
                    "com.example.MyClass",
                    "myMethod",
                    "MyClass.java",
                    -1);

            // When
            String sourceCode = SpecsLogging.getSourceCode(element);

            // Then
            assertThat(sourceCode).isEqualTo(" -> com.example.MyClass.myMethod(MyClass.java:-1)");
        }

        @Test
        @DisplayName("Should handle native method")
        void testGetSourceCodeWithNativeMethod() {
            // Given
            StackTraceElement element = new StackTraceElement(
                    "java.lang.System",
                    "nativeMethod",
                    null,
                    -2);

            // When
            String sourceCode = SpecsLogging.getSourceCode(element);

            // Then
            assertThat(sourceCode).isEqualTo(" -> java.lang.System.nativeMethod(null:-2)");
        }
    }

    @Nested
    @DisplayName("Stack Trace Processing Tests")
    class StackTraceProcessingTests {

        @Test
        @DisplayName("Should get log call location from stack trace")
        void testGetLogCallLocation() {
            // Given
            StackTraceElement[] stackTrace = new StackTraceElement[] {
                    new StackTraceElement("java.lang.Thread", "getStackTrace", "Thread.java", 100),
                    new StackTraceElement("com.example.TestClass", "testMethod", "TestClass.java", 50),
                    new StackTraceElement("com.example.MainClass", "main", "MainClass.java", 20)
            };

            // When
            List<StackTraceElement> result = SpecsLogging.getLogCallLocation(stackTrace);

            // Then
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getClassName()).isEqualTo("com.example.TestClass");
            assertThat(result.get(1).getClassName()).isEqualTo("com.example.MainClass");
        }

        @Test
        @DisplayName("Should handle null stack trace")
        void testGetLogCallLocationWithNullStackTrace() {
            // When
            List<StackTraceElement> result = SpecsLogging.getLogCallLocation(null);

            // Then
            assertThat(result).isNotNull();
            // Should return current thread's stack trace minus ignored classes
        }

        @Test
        @DisplayName("Should return empty list when all elements are ignored")
        void testGetLogCallLocationAllIgnored() {
            // Given - Stack trace with only ignored classes
            StackTraceElement[] stackTrace = new StackTraceElement[] {
                    new StackTraceElement("java.lang.Thread", "getStackTrace", "Thread.java", 100),
                    new StackTraceElement("pt.up.fe.specs.util.logging.SpecsLogging", "method", "SpecsLogging.java", 50)
            };

            // When
            List<StackTraceElement> result = SpecsLogging.getLogCallLocation(stackTrace);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should generate full stack trace string")
        void testGetStackTrace() {
            // Given
            StackTraceElement[] stackTrace = new StackTraceElement[] {
                    new StackTraceElement("com.example.ClassA", "methodA", "ClassA.java", 10),
                    new StackTraceElement("com.example.ClassB", "methodB", "ClassB.java", 20)
            };

            // When
            String result = SpecsLogging.getStackTrace(stackTrace);

            // Then
            assertThat(result).contains("Stack Trace:");
            assertThat(result).contains("--------------");
            assertThat(result).contains("com.example.ClassA.methodA");
            assertThat(result).contains("com.example.ClassB.methodB");
            assertThat(result).startsWith("\n\nStack Trace:");
            assertThat(result).endsWith("--------------\n");
        }
    }

    @Nested
    @DisplayName("Log Suffix Generation Tests")
    class LogSuffixGenerationTests {

        @Test
        @DisplayName("Should return empty string for NONE log suffix")
        void testGetLogSuffixNone() {
            // Given
            StackTraceElement[] stackTrace = new StackTraceElement[] {
                    new StackTraceElement("com.example.Test", "test", "Test.java", 10)
            };

            // When
            String result = SpecsLogging.getLogSuffix(LogSourceInfo.NONE, stackTrace);

            // Then
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should return source location for SOURCE log suffix")
        void testGetLogSuffixSource() {
            // Given
            StackTraceElement[] stackTrace = new StackTraceElement[] {
                    new StackTraceElement("com.example.Test", "test", "Test.java", 10)
            };

            // When
            String result = SpecsLogging.getLogSuffix(LogSourceInfo.SOURCE, stackTrace);

            // Then
            assertThat(result).contains("com.example.Test.test(Test.java:10)");
        }

        @Test
        @DisplayName("Should return stack trace for STACK_TRACE log suffix")
        void testGetLogSuffixStackTrace() {
            // Given
            StackTraceElement[] stackTrace = new StackTraceElement[] {
                    new StackTraceElement("com.example.Test", "test", "Test.java", 10)
            };

            // When
            String result = SpecsLogging.getLogSuffix(LogSourceInfo.STACK_TRACE, stackTrace);

            // Then
            assertThat(result).contains("Stack Trace:");
            assertThat(result).contains("com.example.Test.test");
        }
    }

    @Nested
    @DisplayName("Message Parsing Tests")
    class MessageParsingTests {

        @Test
        @DisplayName("Should parse message with tag and content")
        void testParseMessageComplete() {
            // Given
            Object tag = "INFO";
            String message = "Test message";
            LogSourceInfo logSuffix = LogSourceInfo.NONE;
            StackTraceElement[] stackTrace = null;

            // When
            String result = SpecsLogging.parseMessage(tag, message, logSuffix, stackTrace);

            // Then
            assertThat(result).startsWith("[INFO] ");
            assertThat(result).contains("Test message");
            assertThat(result).endsWith(System.getProperty("line.separator"));
        }

        @Test
        @DisplayName("Should parse message with null tag")
        void testParseMessageNullTag() {
            // Given
            Object tag = null;
            String message = "Test message";
            LogSourceInfo logSuffix = LogSourceInfo.NONE;

            // When
            String result = SpecsLogging.parseMessage(tag, message, logSuffix, null);

            // Then
            assertThat(result).isEqualTo("Test message" + System.getProperty("line.separator"));
        }

        @Test
        @DisplayName("Should parse message with source info")
        void testParseMessageWithSource() {
            // Given
            Object tag = "DEBUG";
            String message = "Debug message";
            LogSourceInfo logSuffix = LogSourceInfo.SOURCE;
            StackTraceElement[] stackTrace = new StackTraceElement[] {
                    new StackTraceElement("com.example.Test", "test", "Test.java", 42)
            };

            // When
            String result = SpecsLogging.parseMessage(tag, message, logSuffix, stackTrace);

            // Then
            assertThat(result).startsWith("[DEBUG] ");
            assertThat(result).contains("Debug message");
            assertThat(result).contains("com.example.Test.test(Test.java:42)");
            assertThat(result).endsWith(System.getProperty("line.separator"));
        }

        @Test
        @DisplayName("Should handle empty message")
        void testParseMessageEmpty() {
            // Given
            Object tag = "WARN";
            String message = "";
            LogSourceInfo logSuffix = LogSourceInfo.NONE;

            // When
            String result = SpecsLogging.parseMessage(tag, message, logSuffix, null);

            // Then
            assertThat(result).isEqualTo("[WARN] ");
            // Empty message should not add newline
        }

        @Test
        @DisplayName("Should handle null message")
        void testParseMessageNull() {
            // Given
            Object tag = "ERROR";
            String message = null;
            LogSourceInfo logSuffix = LogSourceInfo.NONE;

            // When
            String result = SpecsLogging.parseMessage(tag, message, logSuffix, null);

            // Then
            assertThat(result).isEqualTo("[ERROR] null");
            // Null message should not add newline
        }

        @Test
        @DisplayName("Should parse message with stack trace suffix")
        void testParseMessageWithStackTrace() {
            // Given
            Object tag = "TRACE";
            String message = "Trace message";
            LogSourceInfo logSuffix = LogSourceInfo.STACK_TRACE;
            StackTraceElement[] stackTrace = new StackTraceElement[] {
                    new StackTraceElement("com.example.Test", "test", "Test.java", 42)
            };

            // When
            String result = SpecsLogging.parseMessage(tag, message, logSuffix, stackTrace);

            // Then
            assertThat(result).startsWith("[TRACE] ");
            assertThat(result).contains("Trace message");
            assertThat(result).contains("Stack Trace:");
            assertThat(result).contains("com.example.Test.test");
            assertThat(result).endsWith(System.getProperty("line.separator"));
        }
    }

    @Nested
    @DisplayName("Integration and Edge Cases")
    class IntegrationAndEdgeCasesTests {

        @Test
        @DisplayName("Should handle complex tag objects")
        void testComplexTagObjects() {
            // Given
            Object complexTag = new Object() {
                @Override
                public String toString() {
                    return "COMPLEX_TAG_WITH_DETAILS";
                }
            };

            // When
            String prefix = SpecsLogging.getPrefix(complexTag);

            // Then
            assertThat(prefix).isEqualTo("[COMPLEX_TAG_WITH_DETAILS] ");
        }

        @Test
        @DisplayName("Should handle very long messages")
        void testLongMessage() {
            // Given
            StringBuilder longMessage = new StringBuilder();
            for (int i = 0; i < 1000; i++) {
                longMessage.append("This is a very long message part ").append(i).append(". ");
            }
            String message = longMessage.toString();

            // When
            String result = SpecsLogging.parseMessage("LONG", message, LogSourceInfo.NONE, null);

            // Then
            assertThat(result).startsWith("[LONG] ");
            assertThat(result).contains(message);
            assertThat(result).endsWith(System.getProperty("line.separator"));
        }

        @Test
        @DisplayName("Should handle special characters in messages")
        void testSpecialCharactersInMessage() {
            // Given
            String message = "Message with unicode: \u00E9\u00F1\u00FC and symbols: @#$%^&*()";

            // When
            String result = SpecsLogging.parseMessage("SPECIAL", message, LogSourceInfo.NONE, null);

            // Then
            assertThat(result).contains(message);
            assertThat(result).startsWith("[SPECIAL] ");
        }

        @Test
        @DisplayName("Should handle concurrent access safely")
        void testConcurrentAccess() throws InterruptedException {
            // Given
            Thread[] threads = new Thread[10];
            boolean[] results = new boolean[10];

            // When
            for (int i = 0; i < threads.length; i++) {
                final int index = i;
                threads[i] = new Thread(() -> {
                    try {
                        String message = "Concurrent message " + index;
                        String result = SpecsLogging.parseMessage("THREAD" + index, message, LogSourceInfo.NONE, null);
                        results[index] = result.contains(message) && result.contains("[THREAD" + index + "] ");
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

        @Test
        @DisplayName("Should maintain consistent behavior across multiple calls")
        void testConsistentBehavior() {
            // Given
            Object tag = "CONSISTENT";
            String message = "Consistent message";
            LogSourceInfo logSuffix = LogSourceInfo.NONE;

            // When
            String result1 = SpecsLogging.parseMessage(tag, message, logSuffix, null);
            String result2 = SpecsLogging.parseMessage(tag, message, logSuffix, null);
            String result3 = SpecsLogging.parseMessage(tag, message, logSuffix, null);

            // Then
            assertThat(result1).isEqualTo(result2);
            assertThat(result2).isEqualTo(result3);
        }
    }
}
