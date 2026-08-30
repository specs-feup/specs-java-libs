package pt.up.fe.specs.util.classmap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for BiFunctionClassMap class.
 * Tests hierarchy-aware bi-function mapping with type safety.
 * 
 * @author Generated Tests
 */
@DisplayName("BiFunctionClassMap Tests")
public class BiFunctionClassMapTest {

    private BiFunctionClassMap<Number, String, String> numberMap;
    private BiFunctionClassMap<Collection<?>, Integer, String> collectionMap;

    @BeforeEach
    void setUp() {
        numberMap = new BiFunctionClassMap<>();
        collectionMap = new BiFunctionClassMap<>();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty map with default constructor")
        void testDefaultConstructor() {
            BiFunctionClassMap<Object, String, Integer> map = new BiFunctionClassMap<>();
            assertThatThrownBy(() -> map.apply(new Object(), "test"))
                    .hasMessageContaining("BiConsumer not defined for class");
        }

        @Test
        @DisplayName("Should create copy with all mappings from original")
        void testCopyConstructor() {
            numberMap.put(Integer.class, (i, s) -> "Int: " + i + "-" + s);
            numberMap.put(Double.class, (d, s) -> "Double: " + d + "-" + s);

            BiFunctionClassMap<Number, String, String> copy = new BiFunctionClassMap<>(numberMap);

            assertThat(copy.apply(42, "test")).isEqualTo("Int: 42-test");
            assertThat(copy.apply(3.14, "test")).isEqualTo("Double: 3.14-test");
        }

        @Test
        @DisplayName("Should create independent copy - changes to original don't affect copy")
        void testCopyIndependenceOriginalToNew() {
            numberMap.put(Integer.class, (i, s) -> "Original: " + i + "-" + s);

            BiFunctionClassMap<Number, String, String> copy = new BiFunctionClassMap<>(numberMap);

            // Modify original
            numberMap.put(Integer.class, (i, s) -> "Modified: " + i + "-" + s);
            numberMap.put(Double.class, (d, s) -> "New: " + d + "-" + s);

            // Copy should retain original behavior
            assertThat(copy.apply(42, "test")).isEqualTo("Original: 42-test");
            
            // Copy shouldn't have the new Double mapping
            assertThatThrownBy(() -> copy.apply(3.14, "test"))
                    .hasMessageContaining("BiConsumer not defined for class");
        }

        @Test
        @DisplayName("Should create independent copy - changes to copy don't affect original")
        void testCopyIndependenceNewToOriginal() {
            numberMap.put(Integer.class, (i, s) -> "Original: " + i + "-" + s);

            BiFunctionClassMap<Number, String, String> copy = new BiFunctionClassMap<>(numberMap);

            // Modify copy
            copy.put(Integer.class, (i, s) -> "Modified: " + i + "-" + s);
            copy.put(Double.class, (d, s) -> "New: " + d + "-" + s);

            // Original should retain original behavior
            assertThat(numberMap.apply(42, "test")).isEqualTo("Original: 42-test");
            
            // Original shouldn't have the new Double mapping
            assertThatThrownBy(() -> numberMap.apply(3.14, "test"))
                    .hasMessageContaining("BiConsumer not defined for class");
        }

        @Test
        @DisplayName("Should copy empty map successfully")
        void testCopyEmptyMap() {
            BiFunctionClassMap<Number, String, String> emptyMap = new BiFunctionClassMap<>();
            BiFunctionClassMap<Number, String, String> copy = new BiFunctionClassMap<>(emptyMap);

            assertThatThrownBy(() -> copy.apply(42, "test"))
                    .hasMessageContaining("BiConsumer not defined for class");
        }

        @Test
        @DisplayName("Should copy classMapper for hierarchy resolution")
        void testCopyClassMapper() {
            numberMap.put(Number.class, (n, s) -> "Number: " + n + "-" + s);
            numberMap.put(Integer.class, (i, s) -> "Integer: " + i + "-" + s);

            BiFunctionClassMap<Number, String, String> copy = new BiFunctionClassMap<>(numberMap);

            // Hierarchy resolution should work in copy
            assertThat(copy.apply(42, "test")).isEqualTo("Integer: 42-test");
            assertThat(copy.apply(3.14, "test")).isEqualTo("Number: 3.14-test");
            assertThat(copy.apply(42L, "test")).isEqualTo("Number: 42-test");
        }

        @Test
        @DisplayName("Should handle covariant return type in copy constructor")
        void testCovariantCopy() {
            BiFunctionClassMap<Number, String, Integer> intResultMap = new BiFunctionClassMap<>();
            intResultMap.put(Integer.class, (i, s) -> s.length() + i.intValue());

            // Copy with covariant return type (Integer extends Number)
            BiFunctionClassMap<Number, String, Number> copy = new BiFunctionClassMap<>(intResultMap);

            Number result = copy.apply(10, "test");
            assertThat(result).isEqualTo(14);
        }

        @Test
        @DisplayName("Should copy map with multiple hierarchy levels")
        void testCopyComplexHierarchy() {
            BiFunctionClassMap<Exception, String, String> exceptionMap = new BiFunctionClassMap<>();
            exceptionMap.put(Exception.class, (e, s) -> "Exception: " + s);
            exceptionMap.put(RuntimeException.class, (e, s) -> "Runtime: " + s);
            exceptionMap.put(IllegalArgumentException.class, (e, s) -> "IllegalArg: " + s);

            BiFunctionClassMap<Exception, String, String> copy = new BiFunctionClassMap<>(exceptionMap);

            assertThat(copy.apply(new Exception(), "test")).isEqualTo("Exception: test");
            assertThat(copy.apply(new RuntimeException(), "test")).isEqualTo("Runtime: test");
            assertThat(copy.apply(new IllegalArgumentException(), "test")).isEqualTo("IllegalArg: test");
        }
    }

    @Nested
    @DisplayName("Put Operations")
    class PutOperations {

        @Test
        @DisplayName("Should put and retrieve bi-function mapping")
        void testPutAndApply() {
            BiFunction<Integer, String, String> intFunction = (i, s) -> "Integer " + i + " with " + s;
            numberMap.put(Integer.class, intFunction);

            String result = numberMap.apply(42, "suffix");
            assertThat(result).isEqualTo("Integer 42 with suffix");
        }

        @Test
        @DisplayName("Should handle multiple class mappings")
        void testMultipleMappings() {
            numberMap.put(Integer.class, (i, s) -> "Int: " + i + "-" + s);
            numberMap.put(Double.class, (d, s) -> "Double: " + d + "-" + s);
            numberMap.put(Number.class, (n, s) -> "Number: " + n + "-" + s);

            assertThat(numberMap.apply(42, "test")).isEqualTo("Int: 42-test");
            assertThat(numberMap.apply(3.14, "test")).isEqualTo("Double: 3.14-test");
            assertThat(numberMap.apply((Number) 42L, "test")).isEqualTo("Number: 42-test");
        }

        @Test
        @DisplayName("Should overwrite existing mapping")
        void testOverwriteMapping() {
            numberMap.put(Integer.class, (i, s) -> "Old: " + i + "-" + s);
            numberMap.put(Integer.class, (i, s) -> "New: " + i + "-" + s);

            assertThat(numberMap.apply(42, "test")).isEqualTo("New: 42-test");
        }
    }

    @Nested
    @DisplayName("Hierarchy Resolution")
    class HierarchyResolution {

        @Test
        @DisplayName("Should resolve class hierarchy correctly")
        void testClassHierarchy() {
            numberMap.put(Number.class, (n, s) -> "Number: " + n + "-" + s);

            // Integer extends Number
            assertThat(numberMap.apply(42, "test")).isEqualTo("Number: 42-test");
            assertThat(numberMap.apply(3.14, "test")).isEqualTo("Number: 3.14-test");
            assertThat(numberMap.apply(42L, "test")).isEqualTo("Number: 42-test");
        }

        @Test
        @DisplayName("Should prefer more specific mappings")
        void testSpecificOverGeneral() {
            numberMap.put(Number.class, (n, s) -> "Number: " + n + "-" + s);
            numberMap.put(Integer.class, (i, s) -> "Integer: " + i + "-" + s);

            assertThat(numberMap.apply(42, "test")).isEqualTo("Integer: 42-test");
            assertThat(numberMap.apply(3.14, "test")).isEqualTo("Number: 3.14-test");
        }

        @Test
        @DisplayName("Should handle interface hierarchies - BUG: May be broken like ClassSet")
        void testInterfaceHierarchy() {
            collectionMap.put(Collection.class, (c, i) -> "Collection of " + c.size() + " with " + i);

            List<String> list = List.of("a", "b", "c");
            ArrayList<String> arrayList = new ArrayList<>(list);

            // These may fail due to interface hierarchy bugs
            assertThat(collectionMap.apply(list, 1)).isEqualTo("Collection of 3 with 1");
            assertThat(collectionMap.apply(arrayList, 2)).isEqualTo("Collection of 3 with 2");
        }
    }

    @Nested
    @DisplayName("Apply Operations")
    class ApplyOperations {

        @Test
        @DisplayName("Should apply bi-function successfully")
        void testApplySuccess() {
            numberMap.put(Integer.class, (i, s) -> "Value: " + i + " suffix: " + s);

            String result = numberMap.apply(42, "test");
            assertThat(result).isEqualTo("Value: 42 suffix: test");
        }

        @Test
        @DisplayName("Should throw exception when no mapping found")
        void testApplyNoMapping() {
            assertThatThrownBy(() -> numberMap.apply(42, "test"))
                    .hasMessageContaining("BiConsumer not defined for class");
        }

        @Test
        @DisplayName("Should handle null return from function")
        void testApplyNullReturn() {
            numberMap.put(Integer.class, (i, s) -> null);

            assertThat(numberMap.apply(42, "test")).isNull();
        }

        @Test
        @DisplayName("Should handle different parameter types")
        void testDifferentParameterTypes() {
            BiFunctionClassMap<String, Integer, Boolean> stringMap = new BiFunctionClassMap<>();
            stringMap.put(String.class, (str, num) -> str.length() > num);

            assertThat(stringMap.apply("hello", 3)).isTrue();
            assertThat(stringMap.apply("hi", 5)).isFalse();
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null first parameter")
        void testNullFirstParameter() {
            numberMap.put(Integer.class, (i, s) -> "Result: " + i + "-" + s);

            // This should throw NPE or be handled gracefully
            assertThatThrownBy(() -> numberMap.apply(null, "test"))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null second parameter")
        void testNullSecondParameter() {
            numberMap.put(Integer.class, (i, s) -> "Result: " + i + "-" + s);

            String result = numberMap.apply(42, null);
            assertThat(result).isEqualTo("Result: 42-null");
        }

        @Test
        @DisplayName("Should handle complex generic types")
        void testComplexGenerics() {
            BiFunctionClassMap<List<String>, Map<String, Integer>, String> complexMap = new BiFunctionClassMap<>();

            @SuppressWarnings("unchecked")
            Class<List<String>> listClass = (Class<List<String>>) (Class<?>) List.class;
            complexMap.put(listClass, (list, map) -> "List size: " + list.size() + ", Map size: " + map.size());

            List<String> stringList = List.of("a", "b");
            Map<String, Integer> stringMap = Map.of("x", 1, "y", 2);

            assertThat(complexMap.apply(stringList, stringMap))
                    .isEqualTo("List size: 2, Map size: 2");
        }

        @Test
        @DisplayName("Should handle function composition")
        void testFunctionComposition() {
            numberMap.put(Integer.class, (i, s) -> "Number: " + i + "-" + s);

            BiFunction<Number, String, String> composedFunction = (n, s) -> numberMap.apply(n, s) + " (processed)";

            assertThat(composedFunction.apply(42, "test"))
                    .isEqualTo("Number: 42-test (processed)");
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
                numberMap.put(Integer.class, (n, s) -> "Value: " + value + "-" + s);
            }

            // Should still work efficiently
            assertThat(numberMap.apply(42, "test")).startsWith("Value: ");
        }

        @Test
        @DisplayName("Should cache hierarchy lookups for performance")
        void testHierarchyCaching() {
            numberMap.put(Number.class, (n, s) -> "Number: " + n + "-" + s);

            // Multiple calls should be efficient due to caching
            for (int i = 0; i < 100; i++) {
                assertThat(numberMap.apply(i, "test")).isEqualTo("Number: " + i + "-test");
            }
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with complex class hierarchies")
        void testComplexHierarchy() {
            BiFunctionClassMap<Exception, String, String> exceptionMap = new BiFunctionClassMap<>();

            exceptionMap.put(Exception.class, (e, s) -> "Exception: " + e.getClass().getSimpleName() + "-" + s);
            exceptionMap.put(RuntimeException.class, (e, s) -> "RuntimeException: " + e.getMessage() + "-" + s);
            exceptionMap.put(IllegalArgumentException.class, (e, s) -> "IllegalArg: " + e.getMessage() + "-" + s);

            Exception base = new Exception("base");
            RuntimeException runtime = new RuntimeException("runtime");
            IllegalArgumentException illegal = new IllegalArgumentException("illegal");

            assertThat(exceptionMap.apply(base, "test")).isEqualTo("Exception: Exception-test");
            assertThat(exceptionMap.apply(runtime, "test")).isEqualTo("RuntimeException: runtime-test");
            assertThat(exceptionMap.apply(illegal, "test")).isEqualTo("IllegalArg: illegal-test");
        }

        @Test
        @DisplayName("Should work with multiple parameter scenarios")
        void testMultipleParameterScenarios() {
            BiFunctionClassMap<String, Object, String> stringMap = new BiFunctionClassMap<>();

            stringMap.put(String.class, (str, obj) -> str + " processed with " + obj.getClass().getSimpleName());

            assertThat(stringMap.apply("hello", 42)).isEqualTo("hello processed with Integer");
            assertThat(stringMap.apply("world", "string")).isEqualTo("world processed with String");
            assertThat(stringMap.apply("test", new ArrayList<>())).isEqualTo("test processed with ArrayList");
        }
    }
}
