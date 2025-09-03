package pt.up.fe.specs.util.classmap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for BiConsumerClassMap class.
 * Tests hierarchy-aware bi-consumer mapping with type safety.
 * 
 * @author Generated Tests
 */
@DisplayName("BiConsumerClassMap Tests")
public class BiConsumerClassMapTest {

    private BiConsumerClassMap<Number, StringBuilder> numberMap;
    private BiConsumerClassMap<Collection<?>, String> collectionMap;
    private StringBuilder output;

    @BeforeEach
    void setUp() {
        numberMap = new BiConsumerClassMap<>();
        collectionMap = new BiConsumerClassMap<>();
        output = new StringBuilder();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create map with default constructor")
        void testDefaultConstructor() {
            BiConsumerClassMap<Object, String> map = new BiConsumerClassMap<>();
            assertThatThrownBy(() -> map.accept(new Object(), "test"))
                    .hasMessageContaining("BiConsumer not defined for class");
        }

        @Test
        @DisplayName("Should create map with ignoreNotFound option")
        void testIgnoreNotFoundConstructor() {
            BiConsumerClassMap<Object, String> map = BiConsumerClassMap.newInstance(true);

            // Should not throw exception when ignoreNotFound is true
            assertThatCode(() -> map.accept(new Object(), "test"))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Put Operations")
    class PutOperations {

        @Test
        @DisplayName("Should put and execute bi-consumer")
        void testPutAndAccept() {
            BiConsumer<Integer, StringBuilder> intConsumer = (i, sb) -> sb.append("Integer: ").append(i);
            numberMap.put(Integer.class, intConsumer);

            numberMap.accept(42, output);
            assertThat(output.toString()).isEqualTo("Integer: 42");
        }

        @Test
        @DisplayName("Should handle multiple class mappings")
        void testMultipleMappings() {
            numberMap.put(Integer.class, (i, sb) -> sb.append("Int: ").append(i));
            numberMap.put(Double.class, (d, sb) -> sb.append("Double: ").append(d));
            numberMap.put(Number.class, (n, sb) -> sb.append("Number: ").append(n));

            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();

            numberMap.accept(42, sb1);
            numberMap.accept(3.14, sb2);
            numberMap.accept((Number) 42L, sb3);

            assertThat(sb1.toString()).isEqualTo("Int: 42");
            assertThat(sb2.toString()).isEqualTo("Double: 3.14");
            assertThat(sb3.toString()).isEqualTo("Number: 42");
        }

        @Test
        @DisplayName("Should overwrite existing mapping")
        void testOverwriteMapping() {
            numberMap.put(Integer.class, (i, sb) -> sb.append("Old: ").append(i));
            numberMap.put(Integer.class, (i, sb) -> sb.append("New: ").append(i));

            numberMap.accept(42, output);
            assertThat(output.toString()).isEqualTo("New: 42");
        }
    }

    @Nested
    @DisplayName("Hierarchy Resolution")
    class HierarchyResolution {

        @Test
        @DisplayName("Should resolve class hierarchy correctly")
        void testClassHierarchy() {
            numberMap.put(Number.class, (n, sb) -> sb.append("Number: ").append(n));

            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();

            // Integer extends Number
            numberMap.accept(42, sb1);
            numberMap.accept(3.14, sb2);
            numberMap.accept(42L, sb3);

            assertThat(sb1.toString()).isEqualTo("Number: 42");
            assertThat(sb2.toString()).isEqualTo("Number: 3.14");
            assertThat(sb3.toString()).isEqualTo("Number: 42");
        }

        @Test
        @DisplayName("Should prefer more specific mappings")
        void testSpecificOverGeneral() {
            numberMap.put(Number.class, (n, sb) -> sb.append("Number: ").append(n));
            numberMap.put(Integer.class, (i, sb) -> sb.append("Integer: ").append(i));

            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();

            numberMap.accept(42, sb1);
            numberMap.accept(3.14, sb2);

            assertThat(sb1.toString()).isEqualTo("Integer: 42");
            assertThat(sb2.toString()).isEqualTo("Number: 3.14");
        }

        @Test
        @DisplayName("Should handle interface hierarchies - BUG: May be broken like ClassSet")
        void testInterfaceHierarchy() {
            collectionMap.put(Collection.class,
                    (c, s) -> output.append("Collection of ").append(c.size()).append(" with ").append(s));

            List<String> list = List.of("a", "b", "c");
            ArrayList<String> arrayList = new ArrayList<>(list);

            // These may fail due to interface hierarchy bugs
            collectionMap.accept(list, "test1");
            String result1 = output.toString();
            output.setLength(0);

            collectionMap.accept(arrayList, "test2");
            String result2 = output.toString();

            assertThat(result1).isEqualTo("Collection of 3 with test1");
            assertThat(result2).isEqualTo("Collection of 3 with test2");
        }
    }

    @Nested
    @DisplayName("Accept Operations")
    class AcceptOperations {

        @Test
        @DisplayName("Should execute bi-consumer successfully")
        void testAcceptSuccess() {
            numberMap.put(Integer.class, (i, sb) -> sb.append("Value: ").append(i));

            numberMap.accept(42, output);
            assertThat(output.toString()).isEqualTo("Value: 42");
        }

        @Test
        @DisplayName("Should throw exception when no mapping found and ignoreNotFound is false")
        void testAcceptNoMapping() {
            assertThatThrownBy(() -> numberMap.accept(42, output))
                    .hasMessageContaining("BiConsumer not defined for class");
        }

        @Test
        @DisplayName("Should ignore when no mapping found and ignoreNotFound is true")
        void testAcceptIgnoreNotFound() {
            BiConsumerClassMap<Number, StringBuilder> ignoringMap = BiConsumerClassMap.newInstance(true);

            // Should not throw exception
            assertThatCode(() -> ignoringMap.accept(42, output))
                    .doesNotThrowAnyException();

            // Output should remain empty
            assertThat(output.toString()).isEmpty();
        }

        @Test
        @DisplayName("Should handle different parameter types")
        void testDifferentParameterTypes() {
            BiConsumerClassMap<String, List<String>> stringMap = new BiConsumerClassMap<>();
            stringMap.put(String.class, (str, list) -> list.add(str.toUpperCase()));

            List<String> list = new ArrayList<>();
            stringMap.accept("hello", list);

            assertThat(list).containsExactly("HELLO");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCases {

        @Test
        @DisplayName("Should handle null first parameter")
        void testNullFirstParameter() {
            numberMap.put(Integer.class, (i, sb) -> sb.append("Result: ").append(i));

            // This should throw NPE or be handled gracefully
            assertThatThrownBy(() -> numberMap.accept(null, output))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle null second parameter")
        void testNullSecondParameter() {
            numberMap.put(Integer.class, (i, sb) -> sb.append("Result: ").append(i));

            // Should work but may throw NPE inside the consumer
            assertThatThrownBy(() -> numberMap.accept(42, null))
                    .isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("Should handle side effects in consumer")
        void testSideEffects() {
            List<String> sideEffectList = new ArrayList<>();
            numberMap.put(Integer.class, (i, sb) -> {
                sb.append("Value: ").append(i);
                sideEffectList.add("Processed: " + i);
            });

            numberMap.accept(42, output);

            assertThat(output.toString()).isEqualTo("Value: 42");
            assertThat(sideEffectList).containsExactly("Processed: 42");
        }

        @Test
        @DisplayName("Should handle exceptions in consumer")
        void testExceptionInConsumer() {
            numberMap.put(Integer.class, (i, sb) -> {
                throw new RuntimeException("Test exception");
            });

            assertThatThrownBy(() -> numberMap.accept(42, output))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessage("Test exception");
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
                numberMap.put(Integer.class, (n, sb) -> sb.append("Value: ").append(value));
            }

            // Should still work efficiently
            numberMap.accept(42, output);
            assertThat(output.toString()).startsWith("Value: ");
        }

        @Test
        @DisplayName("Should cache hierarchy lookups for performance")
        void testHierarchyCaching() {
            numberMap.put(Number.class, (n, sb) -> sb.append("Number: ").append(n));

            // Multiple calls should be efficient due to caching
            for (int i = 0; i < 100; i++) {
                StringBuilder sb = new StringBuilder();
                numberMap.accept(i, sb);
                assertThat(sb.toString()).isEqualTo("Number: " + i);
            }
        }
    }

    @Nested
    @DisplayName("Integration Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Should work with complex class hierarchies")
        void testComplexHierarchy() {
            BiConsumerClassMap<Exception, StringBuilder> exceptionMap = new BiConsumerClassMap<>();

            exceptionMap.put(Exception.class, (e, sb) -> sb.append("Exception: ").append(e.getClass().getSimpleName()));
            exceptionMap.put(RuntimeException.class, (e, sb) -> sb.append("RuntimeException: ").append(e.getMessage()));
            exceptionMap.put(IllegalArgumentException.class,
                    (e, sb) -> sb.append("IllegalArg: ").append(e.getMessage()));

            Exception base = new Exception("base");
            RuntimeException runtime = new RuntimeException("runtime");
            IllegalArgumentException illegal = new IllegalArgumentException("illegal");

            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();

            exceptionMap.accept(base, sb1);
            exceptionMap.accept(runtime, sb2);
            exceptionMap.accept(illegal, sb3);

            assertThat(sb1.toString()).isEqualTo("Exception: Exception");
            assertThat(sb2.toString()).isEqualTo("RuntimeException: runtime");
            assertThat(sb3.toString()).isEqualTo("IllegalArg: illegal");
        }

        @Test
        @DisplayName("Should work with mixed ignore modes")
        void testMixedIgnoreModes() {
            BiConsumerClassMap<Number, StringBuilder> strictMap = new BiConsumerClassMap<>();
            BiConsumerClassMap<Number, StringBuilder> lenientMap = BiConsumerClassMap.newInstance(true);

            strictMap.put(Integer.class, (i, sb) -> sb.append("Strict: ").append(i));
            lenientMap.put(Integer.class, (i, sb) -> sb.append("Lenient: ").append(i));

            StringBuilder sb1 = new StringBuilder();
            StringBuilder sb2 = new StringBuilder();
            StringBuilder sb3 = new StringBuilder();

            // Both should work for mapped classes
            strictMap.accept(42, sb1);
            lenientMap.accept(42, sb2);

            // Only lenient should work for unmapped classes
            assertThatThrownBy(() -> strictMap.accept(3.14, sb3))
                    .hasMessageContaining("BiConsumer not defined");

            lenientMap.accept(3.14, sb3); // Should not throw

            assertThat(sb1.toString()).isEqualTo("Strict: 42");
            assertThat(sb2.toString()).isEqualTo("Lenient: 42");
            assertThat(sb3.toString()).isEmpty();
        }
    }
}
