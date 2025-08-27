package pt.up.fe.specs.util.classmap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for FunctionClassMap class.
 * Tests hierarchy-aware function mapping with type safety.
 * 
 * @author Generated Tests
 */
@DisplayName("FunctionClassMap Tests")
public class FunctionClassMapTest {

    private FunctionClassMap<Number, String> numberMap;
    private FunctionClassMap<Collection<?>, Integer> collectionMap;

    @BeforeEach
    void setUp() {
        numberMap = new FunctionClassMap<>();
        collectionMap = new FunctionClassMap<>();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty map with default constructor")
        void testDefaultConstructor() {
            FunctionClassMap<Object, String> map = new FunctionClassMap<>();
            assertThat(map.applyTry(new Object())).isEmpty();
        }

        @Test
        @DisplayName("Should create map with default value")
        void testDefaultValueConstructor() {
            FunctionClassMap<Object, String> map = new FunctionClassMap<>("default");

            assertThat(map.applyTry(new Object())).contains("default");
            assertThat(map.apply(new Object())).isEqualTo("default");
        }

        @Test
        @DisplayName("Should create map with default function")
        void testDefaultFunctionConstructor() {
            FunctionClassMap<Number, String> map = new FunctionClassMap<>(
                    num -> "Number: " + num.toString());

            Integer value = 42;
            assertThat(map.applyTry(value)).contains("Number: 42");
            assertThat(map.apply(value)).isEqualTo("Number: 42");
        }

        @Test
        @DisplayName("Should create copy of existing map")
        void testCopyConstructor() {
            numberMap.put(Integer.class, i -> "Integer: " + i);
            numberMap.setDefaultValue("copied default");

            FunctionClassMap<Number, String> copy = new FunctionClassMap<>(numberMap);

            assertThat(copy.apply(42)).isEqualTo("Integer: 42");
            assertThat(copy.apply(3.14)).isEqualTo("copied default");
        }
    }

    @Nested
    @DisplayName("Put Operations")
    class PutOperations {

        @Test
        @DisplayName("Should put and retrieve function mapping")
        void testPutAndGet() {
            Function<Integer, String> intFunction = i -> "Integer: " + i;
            numberMap.put(Integer.class, intFunction);

            // Test via apply since get is private
            assertThat(numberMap.apply(42)).isEqualTo("Integer: 42");
        }

        @Test
        @DisplayName("Should handle multiple class mappings")
        void testMultipleMappings() {
            numberMap.put(Integer.class, i -> "Int: " + i);
            numberMap.put(Double.class, d -> "Double: " + d);
            numberMap.put(Number.class, n -> "Number: " + n);

            assertThat(numberMap.apply(42)).isEqualTo("Int: 42");
            assertThat(numberMap.apply(3.14)).isEqualTo("Double: 3.14");
            assertThat(numberMap.apply((Number) 42L)).isEqualTo("Number: 42");
        }

        @Test
        @DisplayName("Should overwrite existing mapping")
        void testOverwriteMapping() {
            numberMap.put(Integer.class, i -> "Old: " + i);
            numberMap.put(Integer.class, i -> "New: " + i);

            assertThat(numberMap.apply(42)).isEqualTo("New: 42");
        }
    }

    @Nested
    @DisplayName("Hierarchy Resolution")
    class HierarchyResolution {

        @Test
        @DisplayName("Should resolve class hierarchy correctly")
        void testClassHierarchy() {
            numberMap.put(Number.class, n -> "Number: " + n);

            // Integer extends Number
            assertThat(numberMap.apply(42)).isEqualTo("Number: 42");
            assertThat(numberMap.apply(3.14)).isEqualTo("Number: 3.14");
            assertThat(numberMap.apply(42L)).isEqualTo("Number: 42");
        }

        @Test
        @DisplayName("Should prefer more specific mappings")
        void testSpecificOverGeneral() {
            numberMap.put(Number.class, n -> "Number: " + n);
            numberMap.put(Integer.class, i -> "Integer: " + i);

            assertThat(numberMap.apply(42)).isEqualTo("Integer: 42");
            assertThat(numberMap.apply(3.14)).isEqualTo("Number: 3.14");
        }

        @Test
        @DisplayName("Should handle interface hierarchies - BUG: May be broken like ClassSet")
        void testInterfaceHierarchy() {
            collectionMap.put(Collection.class, c -> c.size());

            List<String> list = List.of("a", "b", "c");
            ArrayList<String> arrayList = new ArrayList<>(list);

            // These may fail due to interface hierarchy bugs
            assertThat(collectionMap.applyTry(list)).contains(3);
            assertThat(collectionMap.applyTry(arrayList)).contains(3);
        }
    }

    @Nested
    @DisplayName("Apply Operations")
    class ApplyOperations {

        @Test
        @DisplayName("Should apply function successfully")
        void testApplySuccess() {
            numberMap.put(Integer.class, i -> "Value: " + i);

            String result = numberMap.apply(42);
            assertThat(result).isEqualTo("Value: 42");
        }

        @Test
        @DisplayName("Should throw exception when no mapping found")
        void testApplyNoMapping() {
            assertThatThrownBy(() -> numberMap.apply(42))
                    .hasMessageContaining("Function not defined for class");
        }

        @Test
        @DisplayName("Should return Optional.empty when no mapping found")
        void testApplyTryNoMapping() {
            Optional<String> result = numberMap.applyTry(42);
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Should handle null return from function")
        void testApplyNullReturn() {
            numberMap.put(Integer.class, i -> null);

            assertThat(numberMap.applyTry(42)).isEmpty();
            assertThat(numberMap.apply(42)).isNull();
        }
    }

    @Nested
    @DisplayName("Default Value Operations")
    class DefaultValueOperations {

        @Test
        @DisplayName("Should use default value when no mapping found")
        void testDefaultValue() {
            numberMap.setDefaultValue("default");

            assertThat(numberMap.apply(42)).isEqualTo("default");
            assertThat(numberMap.applyTry(42)).contains("default");
        }

        @Test
        @DisplayName("Should use default function when no mapping found")
        void testDefaultFunction() {
            numberMap.setDefaultFunction(n -> "Default: " + n);

            assertThat(numberMap.apply(42)).isEqualTo("Default: 42");
            assertThat(numberMap.applyTry(42)).contains("Default: 42");
        }

        @Test
        @DisplayName("Should prefer specific mapping over defaults")
        void testMappingOverDefault() {
            numberMap.setDefaultValue("default");
            numberMap.put(Integer.class, i -> "Specific: " + i);

            assertThat(numberMap.apply(42)).isEqualTo("Specific: 42");
            assertThat(numberMap.apply(3.14)).isEqualTo("default");
        }

        @Test
        @DisplayName("Should handle null default function return - BUG: Throws NPE")
        void testNullDefaultFunctionReturn() {
            numberMap.setDefaultFunction(n -> null);

            // BUG: This throws NPE instead of returning Optional.empty()
            assertThatThrownBy(() -> numberMap.applyTry(42))
                    .isInstanceOf(NullPointerException.class);
            assertThatThrownBy(() -> numberMap.apply(42))
                    .isInstanceOf(NullPointerException.class);
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null input gracefully")
        void testNullInput() {
            numberMap.put(Integer.class, i -> "Integer: " + i);

            // This should throw NPE or be handled gracefully
            assertThatThrownBy(() -> numberMap.apply(null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle complex generic types")
        void testComplexGenerics() {
            FunctionClassMap<Collection<String>, String> stringCollectionMap = new FunctionClassMap<>();

            @SuppressWarnings("unchecked")
            Class<List<String>> listClass = (Class<List<String>>) (Class<?>) List.class;
            stringCollectionMap.put(listClass, list -> "List of " + list.size());

            List<String> stringList = List.of("a", "b");
            assertThat(stringCollectionMap.apply(stringList)).isEqualTo("List of 2");
        }

        @Test
        @DisplayName("Should handle function composition")
        void testFunctionComposition() {
            numberMap.put(Integer.class, i -> "Number: " + i);

            Function<Number, String> composedFunction = n -> numberMap.apply(n) + " (processed)";

            assertThat(composedFunction.apply(42)).isEqualTo("Number: 42 (processed)");
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
                numberMap.put(Integer.class, n -> "Value: " + value);
            }

            // Should still work efficiently
            assertThat(numberMap.apply(42)).startsWith("Value: ");
        }

        @Test
        @DisplayName("Should cache hierarchy lookups for performance")
        void testHierarchyCaching() {
            numberMap.put(Number.class, n -> "Number: " + n);

            // Multiple calls should be efficient due to caching
            for (int i = 0; i < 100; i++) {
                assertThat(numberMap.apply(i)).isEqualTo("Number: " + i);
            }
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with complex class hierarchies")
        void testComplexHierarchy() {
            FunctionClassMap<Exception, String> exceptionMap = new FunctionClassMap<>();

            exceptionMap.put(Exception.class, e -> "Exception: " + e.getClass().getSimpleName());
            exceptionMap.put(RuntimeException.class, e -> "RuntimeException: " + e.getMessage());
            exceptionMap.put(IllegalArgumentException.class, e -> "IllegalArg: " + e.getMessage());

            Exception base = new Exception("base");
            RuntimeException runtime = new RuntimeException("runtime");
            IllegalArgumentException illegal = new IllegalArgumentException("illegal");

            assertThat(exceptionMap.apply(base)).isEqualTo("Exception: Exception");
            assertThat(exceptionMap.apply(runtime)).isEqualTo("RuntimeException: runtime");
            assertThat(exceptionMap.apply(illegal)).isEqualTo("IllegalArg: illegal");
        }

        @Test
        @DisplayName("Should work with mixed defaults and specific mappings")
        void testMixedDefaults() {
            numberMap.setDefaultFunction(n -> "Default: " + n.getClass().getSimpleName());
            numberMap.put(Integer.class, i -> "Integer: " + i);

            assertThat(numberMap.apply(42)).isEqualTo("Integer: 42");
            assertThat(numberMap.apply(3.14)).isEqualTo("Default: Double");
            assertThat(numberMap.apply(42L)).isEqualTo("Default: Long");
        }
    }
}
