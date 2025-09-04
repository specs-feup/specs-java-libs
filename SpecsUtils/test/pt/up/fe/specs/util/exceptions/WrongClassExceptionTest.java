package pt.up.fe.specs.util.exceptions;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link WrongClassException}.
 * 
 * Tests the exception class that indicates when an object is of the wrong class
 * type, typically used in type checking scenarios.
 * 
 * @author Generated Tests
 */
@DisplayName("WrongClassException Tests")
class WrongClassExceptionTest {

    @Nested
    @DisplayName("Inheritance Tests")
    class InheritanceTests {

        @Test
        @DisplayName("Should extend UnsupportedOperationException")
        void testExtendsUnsupportedOperationException() {
            assertThat(UnsupportedOperationException.class).isAssignableFrom(WrongClassException.class);
        }

        @Test
        @DisplayName("Should be a RuntimeException")
        void testIsRuntimeException() {
            assertThat(RuntimeException.class).isAssignableFrom(WrongClassException.class);
        }

        @Test
        @DisplayName("Should be an Exception")
        void testIsException() {
            assertThat(Exception.class).isAssignableFrom(WrongClassException.class);
        }
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create exception with object and expected class")
        void testConstructorWithObjectAndExpectedClass() {
            String testObject = "test string";
            Class<?> expectedClass = Integer.class;

            WrongClassException exception = new WrongClassException(testObject, expectedClass);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Expected class 'Integer', found String");
        }

        @Test
        @DisplayName("Should create exception with found class and expected class")
        void testConstructorWithFoundAndExpectedClass() {
            Class<?> foundClass = String.class;
            Class<?> expectedClass = Integer.class;

            WrongClassException exception = new WrongClassException(foundClass, expectedClass);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Expected class 'Integer', found String");
        }

        @Test
        @DisplayName("Should handle same class for found and expected")
        void testConstructorWithSameClass() {
            Class<?> sameClass = String.class;

            WrongClassException exception = new WrongClassException(sameClass, sameClass);

            assertThat(exception).isNotNull();
            assertThat(exception.getMessage()).isEqualTo("Expected class 'String', found String");
        }

        @Test
        @DisplayName("Should handle null object gracefully")
        void testConstructorWithNullObject() {
            Object nullObject = null;
            Class<?> expectedClass = String.class;

            assertThatThrownBy(() -> new WrongClassException(nullObject, expectedClass))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null expected class")
        void testConstructorWithNullExpectedClass() {
            String testObject = "test";
            Class<?> nullExpectedClass = null;

            assertThatThrownBy(() -> new WrongClassException(testObject, nullExpectedClass))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null found class")
        void testConstructorWithNullFoundClass() {
            Class<?> nullFoundClass = null;
            Class<?> expectedClass = String.class;

            assertThatThrownBy(() -> new WrongClassException(nullFoundClass, expectedClass))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Message Generation Tests")
    class MessageGenerationTests {

        @Test
        @DisplayName("Should use simple class names in message")
        void testUsesSimpleClassNames() {
            WrongClassException exception = new WrongClassException(String.class, java.util.ArrayList.class);

            assertThat(exception.getMessage()).isEqualTo("Expected class 'ArrayList', found String");
            // Should not contain package names
            assertThat(exception.getMessage()).doesNotContain("java.lang");
            assertThat(exception.getMessage()).doesNotContain("java.util");
        }

        @Test
        @DisplayName("Should generate correct messages for primitive wrapper classes")
        void testPrimitiveWrapperClasses() {
            Object integerObject = 42;
            WrongClassException exception = new WrongClassException(integerObject, Boolean.class);

            assertThat(exception.getMessage()).isEqualTo("Expected class 'Boolean', found Integer");
        }

        @Test
        @DisplayName("Should generate correct messages for array classes")
        void testArrayClasses() {
            String[] stringArray = { "test" };
            WrongClassException exception = new WrongClassException(stringArray, Integer[].class);

            assertThat(exception.getMessage()).contains("Expected class");
            assertThat(exception.getMessage()).contains("found");
            // Array class names can be complex, so we just verify the structure
        }

        @Test
        @DisplayName("Should generate correct messages for interface classes")
        void testInterfaceClasses() {
            java.util.ArrayList<String> list = new java.util.ArrayList<>();
            WrongClassException exception = new WrongClassException(list, java.util.Set.class);

            assertThat(exception.getMessage()).isEqualTo("Expected class 'Set', found ArrayList");
        }

        @Test
        @DisplayName("Should generate correct messages for nested classes")
        void testNestedClasses() {
            NestedTestClass nested = new NestedTestClass();
            WrongClassException exception = new WrongClassException(nested, OuterTestClass.class);

            assertThat(exception.getMessage()).contains("Expected class");
            assertThat(exception.getMessage()).contains("found");
            assertThat(exception.getMessage()).contains("NestedTestClass");
        }

        @Test
        @DisplayName("Should generate correct messages for custom classes")
        void testCustomClasses() {
            CustomTestClass1 obj1 = new CustomTestClass1();
            WrongClassException exception = new WrongClassException(obj1, CustomTestClass2.class);

            assertThat(exception.getMessage()).isEqualTo("Expected class 'CustomTestClass2', found CustomTestClass1");
        }
    }

    @Nested
    @DisplayName("Exception Behavior Tests")
    class ExceptionBehaviorTests {

        @Test
        @DisplayName("Should be throwable")
        void testThrowable() {
            assertThatThrownBy(() -> {
                throw new WrongClassException(String.class, Integer.class);
            }).isInstanceOf(WrongClassException.class)
                    .hasMessage("Expected class 'Integer', found String");
        }

        @Test
        @DisplayName("Should preserve stack trace")
        void testStackTracePreservation() {
            try {
                throw new WrongClassException(String.class, Integer.class);
            } catch (WrongClassException e) {
                assertThat(e.getStackTrace()).isNotEmpty();
                assertThat(e.getStackTrace()[0].getMethodName()).isEqualTo("testStackTracePreservation");
            }
        }

        @Test
        @DisplayName("Should have no cause by default")
        void testNoCauseByDefault() {
            WrongClassException exception = new WrongClassException(String.class, Integer.class);
            assertThat(exception.getCause()).isNull();
        }

        @Test
        @DisplayName("Should be serializable")
        void testSerializable() {
            WrongClassException exception = new WrongClassException(String.class, Integer.class);

            // Test that it has a serialVersionUID (implicitly tests serializability)
            assertThat(exception).isInstanceOf(java.io.Serializable.class);
        }
    }

    @Nested
    @DisplayName("Use Case Scenarios")
    class UseCaseScenarios {

        @Test
        @DisplayName("Should handle type casting scenario")
        void testTypeCastingScenario() {
            Object obj = "this is a string";

            assertThatThrownBy(() -> {
                if (!(obj instanceof Integer)) {
                    throw new WrongClassException(obj, Integer.class);
                }
            }).isInstanceOf(WrongClassException.class)
                    .hasMessage("Expected class 'Integer', found String");
        }

        @Test
        @DisplayName("Should handle parameter validation scenario")
        void testParameterValidationScenario() {
            Object parameter = 42.5; // Double instead of expected Integer

            assertThatThrownBy(() -> {
                if (!Integer.class.isAssignableFrom(parameter.getClass())) {
                    throw new WrongClassException(parameter, Integer.class);
                }
            }).isInstanceOf(WrongClassException.class)
                    .hasMessage("Expected class 'Integer', found Double");
        }

        @Test
        @DisplayName("Should handle collection element validation scenario")
        void testCollectionElementValidationScenario() {
            Object element = "string element";
            Class<?> expectedElementType = Integer.class;

            assertThatThrownBy(() -> {
                if (!expectedElementType.isInstance(element)) {
                    throw new WrongClassException(element, expectedElementType);
                }
            }).isInstanceOf(WrongClassException.class)
                    .hasMessage("Expected class 'Integer', found String");
        }

        @Test
        @DisplayName("Should handle inheritance checking scenario")
        void testInheritanceCheckingScenario() {
            CustomTestClass1 obj = new CustomTestClass1();
            Class<?> expectedSuperclass = CustomTestClass2.class;

            assertThatThrownBy(() -> {
                if (!expectedSuperclass.isAssignableFrom(obj.getClass())) {
                    throw new WrongClassException(obj, expectedSuperclass);
                }
            }).isInstanceOf(WrongClassException.class)
                    .hasMessage("Expected class 'CustomTestClass2', found CustomTestClass1");
        }

        @Test
        @DisplayName("Should handle factory method validation scenario")
        void testFactoryMethodValidationScenario() {
            Object createdObject = new java.util.HashMap<>();
            Class<?> expectedType = java.util.List.class;

            assertThatThrownBy(() -> {
                if (!expectedType.isInstance(createdObject)) {
                    throw new WrongClassException(createdObject, expectedType);
                }
            }).isInstanceOf(WrongClassException.class)
                    .hasMessage("Expected class 'List', found HashMap");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle abstract class types")
        void testAbstractClassTypes() {
            java.util.ArrayList<String> concreteList = new java.util.ArrayList<>();
            Class<?> abstractListClass = java.util.AbstractList.class;

            // This should NOT throw since ArrayList extends AbstractList
            // But for testing purposes, let's create the exception anyway
            WrongClassException exception = new WrongClassException(concreteList, abstractListClass);
            assertThat(exception.getMessage()).isEqualTo("Expected class 'AbstractList', found ArrayList");
        }

        @Test
        @DisplayName("Should handle interface types")
        void testInterfaceTypes() {
            java.util.ArrayList<String> list = new java.util.ArrayList<>();
            Class<?> setInterface = java.util.Set.class;

            WrongClassException exception = new WrongClassException(list, setInterface);
            assertThat(exception.getMessage()).isEqualTo("Expected class 'Set', found ArrayList");
        }

        @Test
        @DisplayName("Should handle enum types")
        void testEnumTypes() {
            TestEnum enumValue = TestEnum.VALUE1;
            WrongClassException exception = new WrongClassException(enumValue, String.class);

            assertThat(exception.getMessage()).contains("Expected class 'String', found");
            assertThat(exception.getMessage()).contains("TestEnum");
        }

        @Test
        @DisplayName("Should handle generic class types")
        void testGenericClassTypes() {
            java.util.ArrayList<String> stringList = new java.util.ArrayList<>();
            java.util.ArrayList<Integer> integerList = new java.util.ArrayList<>();

            // Both should have the same class despite different generic types
            WrongClassException exception = new WrongClassException(stringList, integerList.getClass());
            assertThat(exception.getMessage()).isEqualTo("Expected class 'ArrayList', found ArrayList");
        }

        @Test
        @DisplayName("Should handle anonymous class types")
        void testAnonymousClassTypes() {
            Object anonymousObject = new Object() {
                @Override
                public String toString() {
                    return "anonymous";
                }
            };

            WrongClassException exception = new WrongClassException(anonymousObject, String.class);
            assertThat(exception.getMessage()).contains("Expected class 'String', found");
            // Anonymous class names can vary, so we just check the structure
        }
    }

    // Test enum for testing
    private enum TestEnum {
        VALUE1, VALUE2
    }

    // Test classes for testing
    private static class NestedTestClass {
        // Empty class for testing
    }

    private static class OuterTestClass {
        // Empty class for testing
    }

    private static class CustomTestClass1 {
        // Empty class for testing
    }

    private static class CustomTestClass2 {
        // Empty class for testing
    }
}
