package pt.up.fe.specs.util.classmap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for MultiFunction class.
 * Tests hierarchy-aware multi-function mapping with self-reference capability.
 * 
 * @author Generated Tests
 */
@DisplayName("MultiFunction Tests")
public class MultiFunctionTest {

    private MultiFunction<Number, String> numberFunction;
    private MultiFunction<Collection<?>, Integer> collectionFunction;

    @BeforeEach
    void setUp() {
        numberFunction = new MultiFunction<>();
        collectionFunction = new MultiFunction<>();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty function with default constructor")
        void testDefaultConstructor() {
            MultiFunction<Object, String> func = new MultiFunction<>();
            assertThatThrownBy(() -> func.apply(new Object()))
                    .hasMessageContaining("Function not defined for class");
        }

        @Test
        @DisplayName("Should create function with default function")
        void testDefaultFunctionConstructor() {
            MultiFunction<Number, String> func = new MultiFunction<>(
                    num -> "Default: " + num.toString());

            assertThat(func.apply(42)).isEqualTo("Default: 42");
            assertThat(func.apply(3.14)).isEqualTo("Default: 3.14");
        }

        @Test
        @DisplayName("Should create function with self-aware default function")
        void testSelfAwareDefaultConstructor() {
            BiFunction<MultiFunction<Number, String>, Number, String> selfAware = (mf, num) -> "SelfAware: "
                    + num.toString();

            MultiFunction<Number, String> func = new MultiFunction<>(selfAware);

            assertThat(func.apply(42)).isEqualTo("SelfAware: 42");
            assertThat(func.apply(3.14)).isEqualTo("SelfAware: 3.14");
        }
    }

    @Nested
    @DisplayName("Put Operations")
    class PutOperations {

        @Test
        @DisplayName("Should put and retrieve function mapping")
        void testPutFunction() {
            Function<Integer, String> intFunction = i -> "Integer: " + i;
            numberFunction.put(Integer.class, intFunction);

            assertThat(numberFunction.apply(42)).isEqualTo("Integer: 42");
        }

        @Test
        @DisplayName("Should put and retrieve bi-function mapping")
        void testPutBiFunction() {
            BiFunction<MultiFunction<Number, String>, Integer, String> biFunction = (mf, i) -> "BiFunction: " + i
                    + " (self-aware)";
            numberFunction.put(Integer.class, biFunction);

            assertThat(numberFunction.apply(42)).isEqualTo("BiFunction: 42 (self-aware)");
        }

        @Test
        @DisplayName("Should handle multiple class mappings")
        void testMultipleMappings() {
            numberFunction.put(Integer.class, i -> "Int: " + i);
            numberFunction.put(Double.class, d -> "Double: " + d);
            numberFunction.put(Number.class, n -> "Number: " + n);

            assertThat(numberFunction.apply(42)).isEqualTo("Int: 42");
            assertThat(numberFunction.apply(3.14)).isEqualTo("Double: 3.14");
            assertThat(numberFunction.apply((Number) 42L)).isEqualTo("Number: 42");
        }

        @Test
        @DisplayName("Should overwrite existing mapping")
        void testOverwriteMapping() {
            numberFunction.put(Integer.class, i -> "Old: " + i);
            numberFunction.put(Integer.class, i -> "New: " + i);

            assertThat(numberFunction.apply(42)).isEqualTo("New: 42");
        }
    }

    @Nested
    @DisplayName("Hierarchy Resolution")
    class HierarchyResolution {

        @Test
        @DisplayName("Should resolve class hierarchy correctly")
        void testClassHierarchy() {
            numberFunction.put(Number.class, n -> "Number: " + n);

            // Integer extends Number
            assertThat(numberFunction.apply(42)).isEqualTo("Number: 42");
            assertThat(numberFunction.apply(3.14)).isEqualTo("Number: 3.14");
            assertThat(numberFunction.apply(42L)).isEqualTo("Number: 42");
        }

        @Test
        @DisplayName("Should prefer more specific mappings")
        void testSpecificOverGeneral() {
            numberFunction.put(Number.class, n -> "Number: " + n);
            numberFunction.put(Integer.class, i -> "Integer: " + i);

            assertThat(numberFunction.apply(42)).isEqualTo("Integer: 42");
            assertThat(numberFunction.apply(3.14)).isEqualTo("Number: 3.14");
        }

        @Test
        @DisplayName("Should handle interface hierarchies - BUG: May be broken like ClassSet")
        void testInterfaceHierarchy() {
            collectionFunction.put(Collection.class, c -> c.size());

            List<String> list = List.of("a", "b", "c");
            ArrayList<String> arrayList = new ArrayList<>(list);

            // These may fail due to interface hierarchy bugs
            assertThat(collectionFunction.apply(list)).isEqualTo(3);
            assertThat(collectionFunction.apply(arrayList)).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("Self-Reference Capability")
    class SelfReferenceCapability {

        @Test
        @DisplayName("Should allow self-referencing functions")
        void testSelfReference() {
            BiFunction<MultiFunction<Number, String>, Number, String> selfAwareFunction = (mf, n) -> {
                if (n.intValue() <= 1) {
                    return "Base: " + n;
                }
                // This demonstrates self-reference capability
                return "Recursive: " + n + " -> " + mf.apply(n.intValue() - 1);
            };

            numberFunction.put(Number.class, selfAwareFunction);

            assertThat(numberFunction.apply(1)).isEqualTo("Base: 1");
            assertThat(numberFunction.apply(3)).contains("Recursive: 3");
        }

        @Test
        @DisplayName("Should handle complex self-referencing scenarios")
        void testComplexSelfReference() {
            BiFunction<MultiFunction<Number, String>, Integer, String> fibonacci = (mf, n) -> {
                if (n <= 1)
                    return n.toString();
                try {
                    String prev1 = mf.apply(n - 1);
                    String prev2 = mf.apply(n - 2);
                    return String.valueOf(Integer.parseInt(prev1) + Integer.parseInt(prev2));
                } catch (Exception e) {
                    return "Error: " + e.getMessage();
                }
            };

            numberFunction.put(Integer.class, fibonacci);

            assertThat(numberFunction.apply(0)).isEqualTo("0");
            assertThat(numberFunction.apply(1)).isEqualTo("1");
            assertThat(numberFunction.apply(5)).isEqualTo("5"); // Fibonacci(5) = 5
        }
    }

    @Nested
    @DisplayName("Default Value Operations")
    class DefaultValueOperations {

        @Test
        @DisplayName("Should use default value when no mapping found")
        void testDefaultValue() {
            MultiFunction<Number, String> func = numberFunction.setDefaultValue("default");

            assertThat(func.apply(42)).isEqualTo("default");
        }

        @Test
        @DisplayName("Should use default function when no mapping found")
        void testDefaultFunction() {
            MultiFunction<Number, String> func = numberFunction.setDefaultFunction(n -> "Default: " + n);

            assertThat(func.apply(42)).isEqualTo("Default: 42");
        }

        @Test
        @DisplayName("Should use default multi-function when no mapping found")
        void testDefaultMultiFunction() {
            BiFunction<MultiFunction<Number, String>, Number, String> defaultMF = (mf, n) -> "DefaultMF: " + n
                    + " from " + mf.getClass().getSimpleName();

            MultiFunction<Number, String> func = numberFunction.setDefaultFunction(defaultMF);

            assertThat(func.apply(42)).contains("DefaultMF: 42 from MultiFunction");
        }

        @Test
        @DisplayName("Should prefer specific mapping over defaults - BUG: Defaults don't work")
        void testMappingOverDefault() {
            numberFunction.setDefaultValue("default");
            numberFunction.put(Integer.class, i -> "Specific: " + i);

            assertThat(numberFunction.apply(42)).isEqualTo("Specific: 42");

            // BUG: Default value doesn't work, throws exception instead
            assertThatThrownBy(() -> numberFunction.apply(3.14))
                    .hasMessageContaining("Function not defined for class");
        }

        @Test
        @DisplayName("Should return same instance from setters for chaining - BUG: Fluent interface broken")
        void testFluentInterface() {
            // BUG: Fluent interface returns new instances instead of 'this'
            MultiFunction<Number, String> result = numberFunction
                    .setDefaultValue("default")
                    .setDefaultFunction(n -> "func: " + n);

            assertThat(result).isNotSameAs(numberFunction);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null input")
        void testNullInput() {
            numberFunction.put(Integer.class, i -> "Integer: " + i);

            // This should throw NPE or be handled gracefully
            assertThatThrownBy(() -> numberFunction.apply(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null return from function")
        void testNullReturn() {
            numberFunction.put(Integer.class, i -> null);

            assertThat(numberFunction.apply(42)).isNull();
        }

        @Test
        @DisplayName("Should handle exceptions in functions")
        void testExceptionInFunction() {
            numberFunction.put(Integer.class, i -> {
                throw new RuntimeException("Test exception");
            });

            assertThatThrownBy(() -> numberFunction.apply(42))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Test exception");
        }

        @Test
        @DisplayName("Should handle complex generic types")
        void testComplexGenerics() {
            MultiFunction<List<String>, String> listFunction = new MultiFunction<>();

            @SuppressWarnings("unchecked")
            Class<List<String>> listClass = (Class<List<String>>) (Class<?>) List.class;
            listFunction.put(listClass, list -> "List of " + list.size() + " items");

            List<String> stringList = List.of("a", "b");
            assertThat(listFunction.apply(stringList)).isEqualTo("List of 2 items");
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should handle large number of mappings efficiently")
        void testLargeMappings() {
            // Add many mappings
            for (int i = 0; i < 1000; i++) {
                final int value = i;
                numberFunction.put(Integer.class, n -> "Value: " + value);
            }

            // Should still work efficiently
            assertThat(numberFunction.apply(42)).startsWith("Value: ");
        }

        @Test
        @DisplayName("Should cache hierarchy lookups for performance")
        void testHierarchyCaching() {
            numberFunction.put(Number.class, n -> "Number: " + n);

            // Multiple calls should be efficient due to caching
            for (int i = 0; i < 100; i++) {
                assertThat(numberFunction.apply(i)).isEqualTo("Number: " + i);
            }
        }

        @Test
        @DisplayName("Should handle recursive calls efficiently")
        void testRecursivePerformance() {
            BiFunction<MultiFunction<Number, String>, Integer, String> countdown = (mf, n) -> {
                if (n <= 0)
                    return "Done";
                return n + " -> " + mf.apply(n - 1);
            };

            numberFunction.put(Integer.class, countdown);

            // Should handle moderate recursion efficiently
            String result = numberFunction.apply(10);
            assertThat(result).startsWith("10 ->").endsWith("Done");
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with complex class hierarchies")
        void testComplexHierarchy() {
            MultiFunction<Exception, String> exceptionFunction = new MultiFunction<>();

            exceptionFunction.put(Exception.class, e -> "Exception: " + e.getClass().getSimpleName());
            exceptionFunction.put(RuntimeException.class, e -> "RuntimeException: " + e.getMessage());
            exceptionFunction.put(IllegalArgumentException.class, e -> "IllegalArg: " + e.getMessage());

            Exception base = new Exception("base");
            RuntimeException runtime = new RuntimeException("runtime");
            IllegalArgumentException illegal = new IllegalArgumentException("illegal");

            assertThat(exceptionFunction.apply(base)).isEqualTo("Exception: Exception");
            assertThat(exceptionFunction.apply(runtime)).isEqualTo("RuntimeException: runtime");
            assertThat(exceptionFunction.apply(illegal)).isEqualTo("IllegalArg: illegal");
        }

        @Test
        @DisplayName("Should work with mixed function types and defaults - BUG: Default function doesn't work")
        void testMixedFunctionTypes() {
            numberFunction.setDefaultFunction(n -> "Default: " + n.getClass().getSimpleName());
            numberFunction.put(Integer.class, i -> "Simple: " + i);

            BiFunction<MultiFunction<Number, String>, Double, String> complexDouble = (mf, d) -> "Complex: " + d
                    + " with precision " + (d % 1 == 0 ? "integer" : "decimal");
            numberFunction.put(Double.class, complexDouble);

            assertThat(numberFunction.apply(42)).isEqualTo("Simple: 42");
            assertThat(numberFunction.apply(3.14)).isEqualTo("Complex: 3.14 with precision decimal");

            // BUG: Default function doesn't work, throws exception instead
            assertThatThrownBy(() -> numberFunction.apply(42L))
                    .hasMessageContaining("Function not defined for class");
        }
    }
}
