package pt.up.fe.specs.util.exceptions;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link NotImplementedException}.
 * 
 * Tests the exception class that indicates when functionality is not yet
 * implemented for a particular class, enum, or context.
 * 
 * @author Generated Tests
 */
@DisplayName("NotImplementedException Tests")
class NotImplementedExceptionTest {

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend UnsupportedOperationException")
        void testExtendsUnsupportedOperationException() {
            assertThat(UnsupportedOperationException.class).isAssignableFrom(NotImplementedException.class);
        }

        @Test
        @DisplayName("Should be a RuntimeException")
        void testIsRuntimeException() {
            assertThat(RuntimeException.class).isAssignableFrom(NotImplementedException.class);
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with object parameter")
        void testConstructorWithObject() {
            String testObject = "test";
            NotImplementedException exception = new NotImplementedException(testObject);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Not yet implemented: " + testObject);
        }

        @Test
        @DisplayName("Should create exception with enum parameter")
        void testConstructorWithEnum() {
            TestEnum enumValue = TestEnum.VALUE1;
            NotImplementedException exception = new NotImplementedException(enumValue);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Not yet implemented for enum 'VALUE1'");
        }

        @Test
        @DisplayName("Should create exception with class parameter")
        void testConstructorWithClass() {
            Class<?> testClass = Integer.class;
            NotImplementedException exception = new NotImplementedException(testClass);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Not yet implemented in class 'java.lang.Integer'");
        }

        @Test
        @DisplayName("Should create exception with string message")
        void testConstructorWithString() {
            String message = "custom functionality";
            NotImplementedException exception = new NotImplementedException(message);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Not yet implemented: custom functionality");
        }

        @Test
        @DisplayName("Should create exception with deprecated default constructor")
        void testDeprecatedDefaultConstructor() {
            @SuppressWarnings("deprecation")
            NotImplementedException exception = new NotImplementedException();

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).contains("Not yet implemented in class");
            // The message should contain the test class name since that's where it's called
            // from
            assertThat(exception.getMessage()).contains("NotImplementedExceptionTest");
        }

        @Test
        @DisplayName("Should handle null object gracefully")
        void testConstructorWithNullObject() {
            Object nullObject = null;

            assertThatThrownBy(() -> new NotImplementedException(nullObject))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty string message")
        void testConstructorWithEmptyString() {
            String emptyMessage = "";
            NotImplementedException exception = new NotImplementedException(emptyMessage);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Not yet implemented: ");
        }

        @Test
        @DisplayName("Should handle null string message")
        void testConstructorWithNullString() {
            String nullMessage = null;
            NotImplementedException exception = new NotImplementedException(nullMessage);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Not yet implemented: null");
        }
    }

    @Nested
    @DisplayName("Message Generation Tests")
    class MessageGenerationTests {

        @Test
        @DisplayName("Should generate correct message for primitive wrapper classes")
        void testMessageForPrimitiveWrapperClasses() {
            NotImplementedException intException = new NotImplementedException(Integer.class);
            NotImplementedException boolException = new NotImplementedException(Boolean.class);
            NotImplementedException doubleException = new NotImplementedException(Double.class);

            assertThat(intException.getMessage()).isEqualTo("Not yet implemented in class 'java.lang.Integer'");
            assertThat(boolException.getMessage()).isEqualTo("Not yet implemented in class 'java.lang.Boolean'");
            assertThat(doubleException.getMessage()).isEqualTo("Not yet implemented in class 'java.lang.Double'");
        }

        @Test
        @DisplayName("Should generate correct message for array classes")
        void testMessageForArrayClasses() {
            NotImplementedException exception = new NotImplementedException(String[].class);
            assertThat(exception.getMessage()).isEqualTo("Not yet implemented in class '[Ljava.lang.String;'");
        }

        @Test
        @DisplayName("Should generate correct message for nested classes")
        void testMessageForNestedClasses() {
            NotImplementedException exception = new NotImplementedException(NestedTestClass.class);
            assertThat(exception.getMessage()).contains("Not yet implemented in class");
            assertThat(exception.getMessage()).contains("NestedTestClass");
        }

        @Test
        @DisplayName("Should generate correct message for different enum values")
        void testMessageForDifferentEnumValues() {
            NotImplementedException exception1 = new NotImplementedException(TestEnum.VALUE1);
            NotImplementedException exception2 = new NotImplementedException(TestEnum.VALUE2);
            NotImplementedException exception3 = new NotImplementedException(TestEnum.VALUE3);

            assertThat(exception1.getMessage()).isEqualTo("Not yet implemented for enum 'VALUE1'");
            assertThat(exception2.getMessage()).isEqualTo("Not yet implemented for enum 'VALUE2'");
            assertThat(exception3.getMessage()).isEqualTo("Not yet implemented for enum 'VALUE3'");
        }

        @Test
        @DisplayName("Should generate correct message for objects using their class")
        void testMessageForObjectsUsingClass() {
            ComplexTestObject obj = new ComplexTestObject("test", 42);
            NotImplementedException exception = new NotImplementedException(obj);

            assertThat(exception.getMessage()).contains("Not yet implemented in class");
            assertThat(exception.getMessage()).contains("ComplexTestObject");
        }

        @Test
        @DisplayName("Should generate message with custom text")
        void testMessageWithCustomText() {
            String[] customMessages = {
                    "feature X",
                    "advanced calculation",
                    "database connection",
                    "file processing",
                    ""
            };

            for (String message : customMessages) {
                NotImplementedException exception = new NotImplementedException(message);
                assertThat(exception.getMessage()).isEqualTo("Not yet implemented: " + message);
            }
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {

        @Test
        @DisplayName("Should be throwable")
        void testThrowable() {
            assertThatThrownBy(() -> {
                throw new NotImplementedException("test feature");
            }).isInstanceOf(NotImplementedException.class)
                    .hasMessage("Not yet implemented: test feature");
        }

        @Test
        @DisplayName("Should preserve stack trace")
        void testStackTracePreservation() {
            try {
                throw new NotImplementedException(TestEnum.VALUE1);
            } catch (NotImplementedException e) {
                assertThat(e.getStackTrace()).isNotEmpty();
                assertThat(e.getStackTrace()[0].getMethodName()).isEqualTo("testStackTracePreservation");
            }
        }

        @Test
        @DisplayName("Should have correct cause handling")
        void testCauseHandling() {
            NotImplementedException exception = new NotImplementedException("test");
            assertThat(exception.getCause()).isNull();
        }

        @Test
        @DisplayName("Should be serializable")
        void testSerializable() {
            NotImplementedException exception = new NotImplementedException("test");

            // Test that it has a serialVersionUID (implicitly tests serializability)
            assertThat(exception).isInstanceOf(java.io.Serializable.class);
        }

        @Test
        @DisplayName("Should handle deprecated constructor stack trace correctly")
        void testDeprecatedConstructorStackTrace() {
            @SuppressWarnings("deprecation")
            NotImplementedException exception = new NotImplementedException();

            // The exception should use stack trace information to determine the calling
            // class
            assertThat(exception.getMessage()).contains("Not yet implemented in class");
            // Should contain the test class name
            assertThat(exception.getMessage()).contains("NotImplementedExceptionTest");
        }
    }

    @Nested
    @DisplayName("Deprecation Tests")
    class DeprecationTests {

        @Test
        @DisplayName("Should mark default constructor as deprecated")
        void testDefaultConstructorDeprecation() {
            // This test verifies that the default constructor is marked as deprecated
            // We can't directly test the @Deprecated annotation, but we can test its usage
            @SuppressWarnings("deprecation")
            NotImplementedException exception = new NotImplementedException();

            // The deprecated constructor should still work
            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).contains("Not yet implemented in class");
        }

        @Test
        @DisplayName("Should prefer parameterized constructors over deprecated default")
        void testPreferParameterizedConstructors() {
            // Test that the other constructors provide more specific information
            NotImplementedException classException = new NotImplementedException(String.class);
            NotImplementedException enumException = new NotImplementedException(TestEnum.VALUE1);
            NotImplementedException stringException = new NotImplementedException("specific feature");

            @SuppressWarnings("deprecation")
            NotImplementedException deprecatedException = new NotImplementedException();

            // Parameterized constructors should provide more specific messages
            assertThat(classException.getMessage()).isEqualTo("Not yet implemented in class 'java.lang.String'");
            assertThat(enumException.getMessage()).isEqualTo("Not yet implemented for enum 'VALUE1'");
            assertThat(stringException.getMessage()).isEqualTo("Not yet implemented: specific feature");

            // Deprecated constructor provides generic message based on stack trace
            assertThat(deprecatedException.getMessage()).contains("Not yet implemented in class");
            assertThat(deprecatedException.getMessage()).contains("NotImplementedExceptionTest");
        }
    }

    // Test enum for testing
    private enum TestEnum {
        VALUE1, VALUE2, VALUE3
    }

    // Test nested class
    private static class NestedTestClass {
        // Empty class for testing
    }

    // Complex test object for testing
    private static class ComplexTestObject {
        private final String name;
        private final int value;

        public ComplexTestObject(String name, int value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return "ComplexTestObject{name=" + name + ", value=" + value + "}";
        }
    }
}
