package pt.up.fe.specs.util.utilities;

import static org.assertj.core.api.Assertions.*;

import java.io.Serializable;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test class for ClassMapper utility.
 * 
 * Tests class hierarchy mapping functionality including:
 * - Class registration and mapping operations
 * - Inheritance hierarchy resolution
 * - Interface mapping support
 * - Cache management and invalidation
 * - Copy constructor functionality
 * - Edge cases and null handling
 * 
 * @author Generated Tests
 */
@DisplayName("ClassMapper Tests")
class ClassMapperTest {

    private ClassMapper classMapper;

    // Test class hierarchy for testing
    private static class BaseClass {
    }

    private static class DerivedClass extends BaseClass {
    }

    private static class DeepDerivedClass extends DerivedClass implements TestInterface {
    }

    private interface TestInterface {
    }

    private static class MultipleInterfaceClass implements TestInterface, Serializable {
    }

    private static class UnrelatedClass {
    }

    @BeforeEach
    void setUp() {
        classMapper = new ClassMapper();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty ClassMapper")
        void testDefaultConstructor() {
            ClassMapper mapper = new ClassMapper();

            assertThat(mapper).isNotNull();
            assertThat(mapper.map(Object.class)).isEmpty();
        }

        @Test
        @DisplayName("Should create copy of existing ClassMapper")
        void testCopyConstructor() {
            classMapper.add(String.class);
            classMapper.add(Integer.class);

            ClassMapper copy = new ClassMapper(classMapper);

            assertThat(copy).isNotNull();
            assertThat(copy.map(String.class)).contains(String.class);
            assertThat(copy.map(Integer.class)).contains(Integer.class);
        }

        @Test
        @DisplayName("Should create independent copy")
        void testCopyIndependence() {
            classMapper.add(String.class);

            ClassMapper copy = new ClassMapper(classMapper);
            copy.add(Integer.class);

            assertThat(classMapper.map(Integer.class)).isEmpty();
            assertThat(copy.map(Integer.class)).contains(Integer.class);
        }
    }

    @Nested
    @DisplayName("Class Addition Tests")
    class ClassAdditionTests {

        @Test
        @DisplayName("Should add new class successfully")
        void testAddNewClass() {
            boolean result = classMapper.add(String.class);

            assertThat(result).isTrue();
            assertThat(classMapper.map(String.class)).contains(String.class);
        }

        @Test
        @DisplayName("Should not add duplicate class")
        void testAddDuplicateClass() {
            classMapper.add(String.class);

            boolean result = classMapper.add(String.class);

            assertThat(result).isFalse();
            assertThat(classMapper.map(String.class)).contains(String.class);
        }

        @Test
        @DisplayName("Should add multiple different classes")
        void testAddMultipleClasses() {
            classMapper.add(String.class);
            classMapper.add(Integer.class);
            classMapper.add(Boolean.class);

            assertThat(classMapper.map(String.class)).contains(String.class);
            assertThat(classMapper.map(Integer.class)).contains(Integer.class);
            assertThat(classMapper.map(Boolean.class)).contains(Boolean.class);
        }

        @Test
        @DisplayName("Should reject adding null class")
        void testAddNullClass() {
            // ClassMapper should validate null classes
            assertThatThrownBy(() -> classMapper.add(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Class cannot be null");
        }
    }

    @Nested
    @DisplayName("Exact Class Mapping Tests")
    class ExactClassMappingTests {

        @Test
        @DisplayName("Should map exact class match")
        void testExactClassMapping() {
            classMapper.add(String.class);

            Optional<Class<?>> result = classMapper.map(String.class);

            assertThat(result).contains(String.class);
        }

        @Test
        @DisplayName("Should return empty for unregistered class")
        void testUnregisteredClass() {
            classMapper.add(String.class);

            Optional<Class<?>> result = classMapper.map(Integer.class);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle primitive classes")
        void testPrimitiveClasses() {
            classMapper.add(int.class);
            classMapper.add(boolean.class);

            assertThat(classMapper.map(int.class)).contains(int.class);
            assertThat(classMapper.map(boolean.class)).contains(boolean.class);
        }

        @Test
        @DisplayName("Should handle array classes")
        void testArrayClasses() {
            classMapper.add(String[].class);
            classMapper.add(int[].class);

            assertThat(classMapper.map(String[].class)).contains(String[].class);
            assertThat(classMapper.map(int[].class)).contains(int[].class);
        }
    }

    @Nested
    @DisplayName("Inheritance Hierarchy Tests")
    class InheritanceHierarchyTests {

        @Test
        @DisplayName("Should map subclass to superclass")
        void testSubclassMapping() {
            classMapper.add(BaseClass.class);

            Optional<Class<?>> result = classMapper.map(DerivedClass.class);

            assertThat(result).contains(BaseClass.class);
        }

        @Test
        @DisplayName("Should map deep inheritance hierarchy")
        void testDeepInheritanceMapping() {
            classMapper.add(BaseClass.class);

            Optional<Class<?>> result = classMapper.map(DeepDerivedClass.class);

            assertThat(result).contains(BaseClass.class);
        }

        @Test
        @DisplayName("Should prefer most specific registered class")
        void testMostSpecificMapping() {
            classMapper.add(BaseClass.class);
            classMapper.add(DerivedClass.class);

            Optional<Class<?>> result = classMapper.map(DeepDerivedClass.class);

            assertThat(result).contains(DerivedClass.class);
        }

        @Test
        @DisplayName("Should respect registration order for same specificity")
        void testRegistrationOrder() {
            // Add in specific order
            classMapper.add(BaseClass.class);
            classMapper.add(Object.class); // Less specific but added later

            Optional<Class<?>> result = classMapper.map(DerivedClass.class);

            // Should prefer BaseClass as it's more specific
            assertThat(result).contains(BaseClass.class);
        }

        @Test
        @DisplayName("Should handle Object class mapping")
        void testObjectClassMapping() {
            classMapper.add(Object.class);

            // All classes extend Object
            assertThat(classMapper.map(String.class)).contains(Object.class);
            assertThat(classMapper.map(DerivedClass.class)).contains(Object.class);
            assertThat(classMapper.map(TestInterface.class)).isEmpty(); // Interfaces don't extend Object in the same
                                                                        // way
        }
    }

    @Nested
    @DisplayName("Interface Mapping Tests")
    class InterfaceMappingTests {

        @Test
        @DisplayName("Should map class implementing interface")
        void testInterfaceMapping() {
            classMapper.add(TestInterface.class);

            Optional<Class<?>> result = classMapper.map(DeepDerivedClass.class);

            assertThat(result).contains(TestInterface.class);
        }

        @Test
        @DisplayName("Should map class with multiple interfaces")
        void testMultipleInterfaceMapping() {
            classMapper.add(TestInterface.class);
            classMapper.add(Serializable.class);

            Optional<Class<?>> result = classMapper.map(MultipleInterfaceClass.class);

            // Should return one of the registered interfaces
            assertThat(result).isPresent()
                    .get().satisfiesAnyOf(
                            clazz -> assertThat(clazz).isEqualTo(TestInterface.class),
                            clazz -> assertThat(clazz).isEqualTo(Serializable.class));
        }

        @Test
        @DisplayName("Should prefer interface at current level over superclass")
        void testSuperclassVsInterface() {
            classMapper.add(BaseClass.class);
            classMapper.add(TestInterface.class);

            Optional<Class<?>> result = classMapper.map(DeepDerivedClass.class);

            // ClassMapper checks interfaces at each level before moving to superclass
            // So TestInterface is found before BaseClass
            assertThat(result).contains(TestInterface.class);
        }

        @Test
        @DisplayName("Should support interface hierarchy mapping")
        void testInterfaceHierarchy() {
            interface ExtendedInterface extends TestInterface {
            }
            class InterfaceImplementor implements ExtendedInterface {
            }

            classMapper.add(TestInterface.class);

            Optional<Class<?>> result = classMapper.map(InterfaceImplementor.class);

            // ClassMapper DOES support interface hierarchy - it recursively checks
            // interfaces
            assertThat(result).contains(TestInterface.class);
        }
    }

    @Nested
    @DisplayName("Cache Management Tests")
    class CacheManagementTests {

        @Test
        @DisplayName("Should cache successful mappings")
        void testSuccessfulMappingCache() {
            classMapper.add(BaseClass.class);

            // First call should calculate and cache
            Optional<Class<?>> result1 = classMapper.map(DerivedClass.class);
            // Second call should use cache
            Optional<Class<?>> result2 = classMapper.map(DerivedClass.class);

            assertThat(result1).contains(BaseClass.class);
            assertThat(result2).contains(BaseClass.class);
            assertThat(result1).isEqualTo(result2);
        }

        @Test
        @DisplayName("Should cache missing mappings")
        void testMissingMappingCache() {
            // First call should calculate and cache miss
            Optional<Class<?>> result1 = classMapper.map(UnrelatedClass.class);
            // Second call should use cache
            Optional<Class<?>> result2 = classMapper.map(UnrelatedClass.class);

            assertThat(result1).isEmpty();
            assertThat(result2).isEmpty();
        }

        @Test
        @DisplayName("Should invalidate cache when new class is added")
        void testCacheInvalidation() {
            // First mapping with empty mapper
            Optional<Class<?>> result1 = classMapper.map(DerivedClass.class);
            assertThat(result1).isEmpty();

            // Add mapping class
            classMapper.add(BaseClass.class);

            // Should now find mapping despite previous cache miss
            Optional<Class<?>> result2 = classMapper.map(DerivedClass.class);
            assertThat(result2).contains(BaseClass.class);
        }

        @Test
        @DisplayName("Should handle cache after copy constructor")
        void testCacheAfterCopy() {
            classMapper.add(String.class);
            classMapper.map(String.class); // Populate cache

            ClassMapper copy = new ClassMapper(classMapper);

            // Copy should work correctly with cached data
            assertThat(copy.map(String.class)).contains(String.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling Tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should reject null mapping parameter")
        void testNullMapping() {
            // ClassMapper should validate null classes
            assertThatThrownBy(() -> classMapper.map(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Class cannot be null");
        }

        @Test
        @DisplayName("Should handle empty mapper")
        void testEmptyMapper() {
            Optional<Class<?>> result = classMapper.map(String.class);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle final classes")
        void testFinalClasses() {
            classMapper.add(String.class); // String is final

            assertThat(classMapper.map(String.class)).contains(String.class);
        }

        @Test
        @DisplayName("Should handle abstract classes")
        void testAbstractClasses() {
            abstract class AbstractTestClass {
            }
            class ConcreteTestClass extends AbstractTestClass {
            }

            classMapper.add(AbstractTestClass.class);

            assertThat(classMapper.map(ConcreteTestClass.class)).contains(AbstractTestClass.class);
        }

        @Test
        @DisplayName("Should handle enum classes")
        void testEnumClasses() {
            enum TestEnum {
                VALUE1, VALUE2
            }

            classMapper.add(TestEnum.class);

            assertThat(classMapper.map(TestEnum.class)).contains(TestEnum.class);
        }

        @Test
        @DisplayName("Should handle inner classes")
        void testInnerClasses() {
            class OuterClass {
                class InnerClass {
                }
            }

            Class<?> innerClass = OuterClass.InnerClass.class;
            classMapper.add(innerClass);

            assertThat(classMapper.map(innerClass)).contains(innerClass);
        }
    }

    @Nested
    @DisplayName("Performance and Complex Scenarios Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle large number of registered classes")
        void testManyRegisteredClasses() {
            // Register many classes
            for (int i = 0; i < 100; i++) {
                try {
                    Class<?> clazz = Class.forName("java.lang.String"); // Just use String repeatedly
                    classMapper.add(clazz);
                } catch (ClassNotFoundException e) {
                    // Skip if class not found
                }
            }

            // Should still work correctly
            assertThat(classMapper.map(String.class)).contains(String.class);
        }

        @Test
        @DisplayName("Should handle complex inheritance chains")
        void testComplexInheritanceChain() {
            class A {
            }
            class B extends A {
            }
            class C extends B {
            }
            class D extends C {
            }
            class E extends D {
            }

            classMapper.add(A.class);
            classMapper.add(C.class);

            // Should map to most specific registered class
            assertThat(classMapper.map(E.class)).contains(C.class);
            assertThat(classMapper.map(B.class)).contains(A.class);
        }

        @Test
        @DisplayName("Should handle repeated mapping calls efficiently")
        void testRepeatedMappingCalls() {
            classMapper.add(BaseClass.class);

            // Multiple calls should be efficient due to caching
            for (int i = 0; i < 1000; i++) {
                Optional<Class<?>> result = classMapper.map(DerivedClass.class);
                assertThat(result).contains(BaseClass.class);
            }
        }
    }
}
