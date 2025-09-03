package pt.up.fe.specs.util.exceptions;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link CaseNotDefinedException}.
 * 
 * Tests the exception class that indicates when a case is not defined
 * for a particular class, enum, or object value.
 * 
 * @author Generated Tests
 */
@DisplayName("CaseNotDefinedException Tests")
class CaseNotDefinedExceptionTest {

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend UnsupportedOperationException")
        void testExtendsUnsupportedOperationException() {
            assertThat(UnsupportedOperationException.class).isAssignableFrom(CaseNotDefinedException.class);
        }

        @Test
        @DisplayName("Should be a RuntimeException")
        void testIsRuntimeException() {
            assertThat(RuntimeException.class).isAssignableFrom(CaseNotDefinedException.class);
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with class parameter")
        void testConstructorWithClass() {
            Class<?> testClass = String.class;
            CaseNotDefinedException exception = new CaseNotDefinedException(testClass);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Case not defined for class 'java.lang.String'");
        }

        @Test
        @DisplayName("Should create exception with enum parameter")
        void testConstructorWithEnum() {
            TestEnum enumValue = TestEnum.VALUE1;
            CaseNotDefinedException exception = new CaseNotDefinedException(enumValue);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Case not defined for enum 'VALUE1'");
        }

        @Test
        @DisplayName("Should create exception with object parameter")
        void testConstructorWithObject() {
            String testObject = "test string";
            CaseNotDefinedException exception = new CaseNotDefinedException(testObject);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Case not defined for value 'test string'");
        }

        @Test
        @DisplayName("Should handle null object gracefully")
        void testConstructorWithNullObject() {
            Object nullObject = null;

            assertThatThrownBy(() -> new CaseNotDefinedException(nullObject))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle empty string object")
        void testConstructorWithEmptyString() {
            String emptyString = "";
            CaseNotDefinedException exception = new CaseNotDefinedException(emptyString);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Case not defined for value ''");
        }
    }

    @Nested
    @DisplayName("Message Generation Tests")
    class MessageGenerationTests {

        @Test
        @DisplayName("Should generate correct message for primitive wrapper class")
        void testMessageForPrimitiveWrapperClass() {
            CaseNotDefinedException exception = new CaseNotDefinedException(Integer.class);
            assertThat(exception.getMessage()).isEqualTo("Case not defined for class 'java.lang.Integer'");
        }

        @Test
        @DisplayName("Should generate correct message for array class")
        void testMessageForArrayClass() {
            CaseNotDefinedException exception = new CaseNotDefinedException(String[].class);
            assertThat(exception.getMessage()).isEqualTo("Case not defined for class '[Ljava.lang.String;'");
        }

        @Test
        @DisplayName("Should generate correct message for nested class")
        void testMessageForNestedClass() {
            CaseNotDefinedException exception = new CaseNotDefinedException(NestedTestClass.class);
            assertThat(exception.getMessage()).contains("Case not defined for class");
            assertThat(exception.getMessage()).contains("NestedTestClass");
        }

        @Test
        @DisplayName("Should generate correct message for different enum values")
        void testMessageForDifferentEnumValues() {
            CaseNotDefinedException exception1 = new CaseNotDefinedException(TestEnum.VALUE1);
            CaseNotDefinedException exception2 = new CaseNotDefinedException(TestEnum.VALUE2);

            assertThat(exception1.getMessage()).isEqualTo("Case not defined for enum 'VALUE1'");
            assertThat(exception2.getMessage()).isEqualTo("Case not defined for enum 'VALUE2'");
        }

        @Test
        @DisplayName("Should generate correct message for complex objects")
        void testMessageForComplexObjects() {
            ComplexTestObject obj = new ComplexTestObject("test", 42);
            CaseNotDefinedException exception = new CaseNotDefinedException(obj);

            assertThat(exception.getMessage())
                    .isEqualTo("Case not defined for value 'ComplexTestObject{name=test, value=42}'");
        }

        @Test
        @DisplayName("Should handle object with null toString")
        void testMessageForObjectWithNullToString() {
            ObjectWithNullToString obj = new ObjectWithNullToString();
            CaseNotDefinedException exception = new CaseNotDefinedException(obj);

            assertThat(exception.getMessage()).isEqualTo("Case not defined for value 'null'");
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {

        @Test
        @DisplayName("Should be throwable")
        void testThrowable() {
            assertThatThrownBy(() -> {
                throw new CaseNotDefinedException(String.class);
            }).isInstanceOf(CaseNotDefinedException.class)
                    .hasMessage("Case not defined for class 'java.lang.String'");
        }

        @Test
        @DisplayName("Should preserve stack trace")
        void testStackTracePreservation() {
            try {
                throw new CaseNotDefinedException(TestEnum.VALUE1);
            } catch (CaseNotDefinedException e) {
                assertThat(e.getStackTrace()).isNotEmpty();
                assertThat(e.getStackTrace()[0].getMethodName()).isEqualTo("testStackTracePreservation");
            }
        }

        @Test
        @DisplayName("Should have correct cause handling")
        void testCauseHandling() {
            CaseNotDefinedException exception = new CaseNotDefinedException(String.class);
            assertThat(exception.getCause()).isNull();
        }

        @Test
        @DisplayName("Should be serializable")
        void testSerializable() {
            CaseNotDefinedException exception = new CaseNotDefinedException(TestEnum.VALUE1);

            // Test that it has a serialVersionUID (implicitly tests serializability)
            assertThat(exception).isInstanceOf(java.io.Serializable.class);
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

    // Complex test object with custom toString
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

    // Object that returns null from toString
    private static class ObjectWithNullToString {
        @Override
        public String toString() {
            return null;
        }
    }
}
