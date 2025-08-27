package pt.up.fe.specs.util.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for ScopedMap hierarchical scoped mapping utility.
 * Tests scope-based value storage and retrieval functionality.
 * 
 * @author Generated Tests
 */
@DisplayName("ScopedMap Tests")
class ScopedMapTest {

    @Nested
    @DisplayName("Constructor and Factory Methods")
    class ConstructorAndFactoryMethods {

        @Test
        @DisplayName("Should create empty ScopedMap")
        void testDefaultConstructor() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            assertThat(scopedMap).isNotNull();
            assertThat(scopedMap.getKeys()).isEmpty();
            assertThat(scopedMap.getSymbols()).isEmpty();
        }

        @Test
        @DisplayName("Should create new instance using static factory method")
        void testStaticFactoryMethod() {
            ScopedMap<String> scopedMap = ScopedMap.newInstance();

            assertThat(scopedMap).isNotNull();
            assertThat(scopedMap.getKeys()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Basic Symbol Operations")
    class BasicSymbolOperations {

        @Test
        @DisplayName("Should add and get symbol with single key")
        void testAddGetSymbolSingle() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            scopedMap.addSymbol("variable", "value");

            assertThat(scopedMap.getSymbol("variable")).isEqualTo("value");
        }

        @Test
        @DisplayName("Should add and get symbol with variadic keys")
        void testAddGetSymbolVariadic() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            scopedMap.addSymbol("value", "scope1", "scope2", "variable");

            assertThat(scopedMap.getSymbol("scope1", "scope2", "variable")).isEqualTo("value");
        }

        @Test
        @DisplayName("Should add and get symbol with list key")
        void testAddGetSymbolList() {
            ScopedMap<String> scopedMap = new ScopedMap<>();
            List<String> key = Arrays.asList("scope1", "scope2", "variable");

            scopedMap.addSymbol(key, "value");

            assertThat(scopedMap.getSymbol(key)).isEqualTo("value");
        }

        @Test
        @DisplayName("Should add and get symbol with separate scope and name")
        void testAddGetSymbolSeparate() {
            ScopedMap<String> scopedMap = new ScopedMap<>();
            List<String> scope = Arrays.asList("package", "class");

            scopedMap.addSymbol(scope, "method", "implementation");

            assertThat(scopedMap.getSymbol(scope, "method")).isEqualTo("implementation");
        }

        @Test
        @DisplayName("Should return null for non-existent symbol")
        void testGetNonExistentSymbol() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            assertThat(scopedMap.getSymbol("nonexistent")).isNull();
            assertThat(scopedMap.getSymbol("scope1", "nonexistent")).isNull();
        }
    }

    @Nested
    @DisplayName("Scope Hierarchy")
    class ScopeHierarchy {

        @Test
        @DisplayName("Should handle nested scopes")
        void testNestedScopes() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            scopedMap.addSymbol(Arrays.asList("global"), "var1", "global_value");
            scopedMap.addSymbol(Arrays.asList("global", "function"), "var1", "function_value");
            scopedMap.addSymbol(Arrays.asList("global", "function", "block"), "var1", "block_value");

            assertThat(scopedMap.getSymbol(Arrays.asList("global", "var1"))).isEqualTo("global_value");
            assertThat(scopedMap.getSymbol(Arrays.asList("global", "function", "var1"))).isEqualTo("function_value");
            assertThat(scopedMap.getSymbol(Arrays.asList("global", "function", "block", "var1")))
                    .isEqualTo("block_value");
        }

        @Test
        @DisplayName("Should handle multiple variables in same scope")
        void testMultipleVariablesInScope() {
            ScopedMap<String> scopedMap = new ScopedMap<>();
            List<String> scope = Arrays.asList("package", "class");

            scopedMap.addSymbol(scope, "field1", "value1");
            scopedMap.addSymbol(scope, "field2", "value2");
            scopedMap.addSymbol(scope, "method1", "impl1");

            assertThat(scopedMap.getSymbol(scope, "field1")).isEqualTo("value1");
            assertThat(scopedMap.getSymbol(scope, "field2")).isEqualTo("value2");
            assertThat(scopedMap.getSymbol(scope, "method1")).isEqualTo("impl1");
        }

        @Test
        @DisplayName("Should handle empty scope")
        void testEmptyScope() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            scopedMap.addSymbol(Collections.emptyList(), "global", "value");

            assertThat(scopedMap.getSymbol(Arrays.asList("global"))).isEqualTo("value");
        }

        @Test
        @DisplayName("Should handle deep scope hierarchies")
        void testDeepScopeHierarchy() {
            ScopedMap<String> scopedMap = new ScopedMap<>();
            List<String> deepScope = Arrays.asList("level1", "level2", "level3", "level4", "level5");

            scopedMap.addSymbol(deepScope, "deepVariable", "deepValue");

            List<String> fullKey = new ArrayList<>(deepScope);
            fullKey.add("deepVariable");
            assertThat(scopedMap.getSymbol(fullKey)).isEqualTo("deepValue");
        }
    }

    @Nested
    @DisplayName("Symbol Querying and Management")
    class SymbolQueryingAndManagement {

        @Test
        @DisplayName("Should get all keys")
        void testGetKeys() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            scopedMap.addSymbol("global", "value1");
            scopedMap.addSymbol(Arrays.asList("scope1"), "var1", "value2");
            scopedMap.addSymbol(Arrays.asList("scope1", "scope2"), "var2", "value3");

            List<List<String>> keys = scopedMap.getKeys();

            assertThat(keys).hasSize(3);
            assertThat(keys).contains(Arrays.asList("global"));
            assertThat(keys).contains(Arrays.asList("scope1", "var1"));
            assertThat(keys).contains(Arrays.asList("scope1", "scope2", "var2"));
        }

        @Test
        @DisplayName("Should get all symbols")
        void testGetAllSymbols() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            scopedMap.addSymbol("var1", "value1");
            scopedMap.addSymbol("var2", "value2");
            scopedMap.addSymbol(Arrays.asList("scope"), "var3", "value3");

            List<String> symbols = scopedMap.getSymbols();

            assertThat(symbols).hasSize(3);
            assertThat(symbols).containsExactlyInAnyOrder("value1", "value2", "value3");
        }

        @Test
        @DisplayName("Should get symbols for specific scope")
        void testGetSymbolsForScope() {
            ScopedMap<String> scopedMap = new ScopedMap<>();
            List<String> targetScope = Arrays.asList("package", "class");

            scopedMap.addSymbol(targetScope, "field1", "value1");
            scopedMap.addSymbol(targetScope, "field2", "value2");
            scopedMap.addSymbol(Arrays.asList("other"), "field3", "value3");

            Map<String, String> scopeSymbols = scopedMap.getSymbols(targetScope);

            assertThat(scopeSymbols).hasSize(2);
            assertThat(scopeSymbols.get("field1")).isEqualTo("value1");
            assertThat(scopeSymbols.get("field2")).isEqualTo("value2");
            assertThat(scopeSymbols).doesNotContainKey("field3");
        }

        @Test
        @DisplayName("Should get symbols for null scope (root)")
        void testGetSymbolsNullScope() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            scopedMap.addSymbol("global1", "value1");
            scopedMap.addSymbol("global2", "value2");
            scopedMap.addSymbol(Arrays.asList("scope"), "local", "value3");

            Map<String, String> rootSymbols = scopedMap.getSymbols(null);

            assertThat(rootSymbols).hasSize(2);
            assertThat(rootSymbols.get("global1")).isEqualTo("value1");
            assertThat(rootSymbols.get("global2")).isEqualTo("value2");
        }

        @Test
        @DisplayName("Should check if scope contains symbol")
        void testContainsSymbol() {
            ScopedMap<String> scopedMap = new ScopedMap<>();
            List<String> scope = Arrays.asList("package", "class");

            scopedMap.addSymbol(scope, "method", "implementation");

            assertThat(scopedMap.containsSymbol(scope, "method")).isTrue();
            assertThat(scopedMap.containsSymbol(scope, "nonexistent")).isFalse();
            assertThat(scopedMap.containsSymbol(Arrays.asList("other"), "method")).isFalse();
        }
    }

    @Nested
    @DisplayName("Scoped Map Operations")
    class ScopedMapOperations {

        @Test
        @DisplayName("Should get symbol map for scope")
        void testGetSymbolMapForScope() {
            ScopedMap<String> original = new ScopedMap<>();

            original.addSymbol(Arrays.asList("outer", "inner"), "var1", "value1");
            original.addSymbol(Arrays.asList("outer", "inner"), "var2", "value2");
            original.addSymbol(Arrays.asList("outer", "inner", "deeper"), "var3", "value3");
            original.addSymbol(Arrays.asList("other"), "var4", "value4");

            ScopedMap<String> innerMap = original.getSymbolMap(Arrays.asList("outer"));

            // The returned map should contain symbols from 'outer' scope and below
            assertThat(innerMap.getSymbol(Arrays.asList("inner", "var1"))).isEqualTo("value1");
            assertThat(innerMap.getSymbol(Arrays.asList("inner", "var2"))).isEqualTo("value2");
            assertThat(innerMap.getSymbol(Arrays.asList("inner", "deeper", "var3"))).isEqualTo("value3");
            assertThat(innerMap.getSymbol(Arrays.asList("var4"))).isNull(); // Not in 'outer' scope
        }

        @Test
        @DisplayName("Should get symbol map with variadic scope")
        void testGetSymbolMapVariadic() {
            ScopedMap<String> original = new ScopedMap<>();

            original.addSymbol(Arrays.asList("a", "b", "c"), "var", "value");

            ScopedMap<String> subMap = original.getSymbolMap("a", "b");

            assertThat(subMap.getSymbol(Arrays.asList("c", "var"))).isEqualTo("value");
        }

        @Test
        @DisplayName("Should return empty map for non-existent scope")
        void testGetSymbolMapNonExistent() {
            ScopedMap<String> original = new ScopedMap<>();
            original.addSymbol("var", "value");

            ScopedMap<String> emptyMap = original.getSymbolMap(Arrays.asList("nonexistent"));

            assertThat(emptyMap.getKeys()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Map Merging and Symbol Addition")
    class MapMergingAndSymbolAddition {

        @Test
        @DisplayName("Should add symbols from another ScopedMap preserving scope")
        void testAddSymbolsPreservingScope() {
            ScopedMap<String> map1 = new ScopedMap<>();
            map1.addSymbol("existing", "value1");

            ScopedMap<String> map2 = new ScopedMap<>();
            map2.addSymbol(Arrays.asList("scope"), "var", "value2");
            map2.addSymbol("global", "value3");

            map1.addSymbols(map2);

            assertThat(map1.getSymbol("existing")).isEqualTo("value1");
            assertThat(map1.getSymbol(Arrays.asList("scope", "var"))).isEqualTo("value2");
            assertThat(map1.getSymbol("global")).isEqualTo("value3");
        }

        @Test
        @DisplayName("Should add symbols to specific scope")
        void testAddSymbolsToScope() {
            ScopedMap<String> target = new ScopedMap<>();

            ScopedMap<String> source = new ScopedMap<>();
            source.addSymbol("var1", "value1");
            source.addSymbol("var2", "value2");

            List<String> targetScope = Arrays.asList("imported");
            target.addSymbols(targetScope, source);

            assertThat(target.getSymbol(Arrays.asList("imported", "var1"))).isEqualTo("value1");
            assertThat(target.getSymbol(Arrays.asList("imported", "var2"))).isEqualTo("value2");
        }

        @Test
        @DisplayName("Should handle empty source map")
        void testAddSymbolsEmpty() {
            ScopedMap<String> target = new ScopedMap<>();
            target.addSymbol("existing", "value");

            ScopedMap<String> empty = new ScopedMap<>();
            target.addSymbols(empty);

            assertThat(target.getSymbol("existing")).isEqualTo("value");
            assertThat(target.getKeys()).hasSize(1);
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentation {

        @Test
        @DisplayName("Should provide string representation")
        void testToString() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            scopedMap.addSymbol("global", "value1");
            scopedMap.addSymbol(Arrays.asList("scope"), "local", "value2");

            String result = scopedMap.toString();

            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
            // The exact format depends on ScopeNode's toString implementation
        }

        @Test
        @DisplayName("Should handle empty ScopedMap toString")
        void testToStringEmpty() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            String result = scopedMap.toString();

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("Edge Cases and Error Handling")
    class EdgeCasesAndErrorHandling {

        @Test
        @DisplayName("Should handle null values")
        void testNullValues() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            scopedMap.addSymbol(Collections.emptyList(), "nullVar", null);

            assertThat(scopedMap.getSymbol("nullVar")).isNull();
            assertThat(scopedMap.getKeys()).contains(Arrays.asList("nullVar"));
        }

        @Test
        @DisplayName("Should handle empty string keys")
        void testEmptyStringKeys() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            scopedMap.addSymbol(Collections.emptyList(), "", "emptyKey");
            scopedMap.addSymbol(Arrays.asList("scope"), "", "emptyName");

            assertThat(scopedMap.getSymbol("")).isEqualTo("emptyKey");
            assertThat(scopedMap.getSymbol(Arrays.asList("scope", ""))).isEqualTo("emptyName");
        }

        @Test
        @DisplayName("Should handle special characters in keys")
        void testSpecialCharactersInKeys() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            scopedMap.addSymbol(Collections.emptyList(), "key with spaces", "value1");
            scopedMap.addSymbol(Collections.emptyList(), "key.with.dots", "value2");
            scopedMap.addSymbol(Collections.emptyList(), "key-with-dashes", "value3");
            scopedMap.addSymbol(Collections.emptyList(), "key_with_underscores", "value4");

            assertThat(scopedMap.getSymbol("key with spaces")).isEqualTo("value1");
            assertThat(scopedMap.getSymbol("key.with.dots")).isEqualTo("value2");
            assertThat(scopedMap.getSymbol("key-with-dashes")).isEqualTo("value3");
            assertThat(scopedMap.getSymbol("key_with_underscores")).isEqualTo("value4");
        }

        @Test
        @DisplayName("Should handle very deep scope hierarchies")
        void testVeryDeepScopes() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            List<String> veryDeepScope = new ArrayList<>();
            for (int i = 0; i < 20; i++) {
                veryDeepScope.add("level" + i);
            }

            scopedMap.addSymbol(veryDeepScope, "deepVar", "deepValue");

            List<String> fullKey = new ArrayList<>(veryDeepScope);
            fullKey.add("deepVar");
            assertThat(scopedMap.getSymbol(fullKey)).isEqualTo("deepValue");
        }

        @Test
        @DisplayName("Should handle large number of symbols")
        void testLargeNumberOfSymbols() {
            ScopedMap<String> scopedMap = new ScopedMap<>();

            // Add 1000 symbols across different scopes
            for (int i = 0; i < 1000; i++) {
                String scope = "scope" + (i % 10);
                String variable = "var" + i;
                String value = "value" + i;

                scopedMap.addSymbol(Arrays.asList(scope), variable, value);
            }

            assertThat(scopedMap.getKeys()).hasSize(1000);
            assertThat(scopedMap.getSymbol(Arrays.asList("scope0", "var0"))).isEqualTo("value0");
            assertThat(scopedMap.getSymbol(Arrays.asList("scope9", "var999"))).isEqualTo("value999");
        }
    }

    @Nested
    @DisplayName("Type Safety and Generics")
    class TypeSafetyAndGenerics {

        @Test
        @DisplayName("Should work with different value types")
        void testDifferentValueTypes() {
            ScopedMap<Integer> intMap = new ScopedMap<>();
            ScopedMap<Boolean> boolMap = new ScopedMap<>();

            intMap.addSymbol(Collections.emptyList(), "number", 42);
            boolMap.addSymbol(Collections.emptyList(), "flag", true);

            assertThat(intMap.getSymbol("number")).isEqualTo(42);
            assertThat(boolMap.getSymbol("flag")).isTrue();
        }

        @Test
        @DisplayName("Should work with complex object types")
        void testComplexObjectTypes() {
            record Person(String name, int age) {
            }

            ScopedMap<Person> personMap = new ScopedMap<>();
            Person alice = new Person("Alice", 30);
            Person bob = new Person("Bob", 25);

            personMap.addSymbol(Arrays.asList("employees"), "alice", alice);
            personMap.addSymbol(Arrays.asList("employees"), "bob", bob);

            assertThat(personMap.getSymbol(Arrays.asList("employees", "alice"))).isEqualTo(alice);
            assertThat(personMap.getSymbol(Arrays.asList("employees", "bob"))).isEqualTo(bob);
        }

        @Test
        @DisplayName("Should work with collection value types")
        void testCollectionValueTypes() {
            ScopedMap<List<String>> listMap = new ScopedMap<>();

            List<String> list1 = Arrays.asList("a", "b", "c");
            List<String> list2 = Arrays.asList("x", "y", "z");

            listMap.addSymbol(Collections.emptyList(), "list1", list1);
            listMap.addSymbol(Collections.emptyList(), "list2", list2);

            assertThat(listMap.getSymbol("list1")).isEqualTo(list1);
            assertThat(listMap.getSymbol("list2")).isEqualTo(list2);
        }
    }

    @Nested
    @DisplayName("Integration and Workflow Tests")
    class IntegrationAndWorkflowTests {

        @Test
        @DisplayName("Should support typical symbol table usage")
        void testSymbolTableUsage() {
            ScopedMap<String> symbolTable = new ScopedMap<>();

            // Global scope
            symbolTable.addSymbol(Collections.emptyList(), "PI", "3.14159");
            symbolTable.addSymbol(Collections.emptyList(), "E", "2.71828");

            // Function scope
            List<String> functionScope = Arrays.asList("main");
            symbolTable.addSymbol(functionScope, "argc", "int");
            symbolTable.addSymbol(functionScope, "argv", "char**");

            // Nested block scope
            List<String> blockScope = Arrays.asList("main", "if_block");
            symbolTable.addSymbol(blockScope, "temp", "int");

            // Verify lookup
            assertThat(symbolTable.getSymbol("PI")).isEqualTo("3.14159");
            assertThat(symbolTable.getSymbol(functionScope, "argc")).isEqualTo("int");
            assertThat(symbolTable.getSymbol(blockScope, "temp")).isEqualTo("int");

            // Verify scope isolation
            assertThat(symbolTable.getSymbol(functionScope, "temp")).isNull();
        }

        @Test
        @DisplayName("Should support namespace-like usage")
        void testNamespaceUsage() {
            ScopedMap<String> namespaceMap = new ScopedMap<>();

            // Different namespaces
            namespaceMap.addSymbol(Arrays.asList("std"), "vector", "container");
            namespaceMap.addSymbol(Arrays.asList("std"), "string", "text");
            namespaceMap.addSymbol(Arrays.asList("boost"), "vector", "math_vector");
            namespaceMap.addSymbol(Arrays.asList("custom", "utils"), "helper", "utility");

            // Verify namespace isolation
            assertThat(namespaceMap.getSymbol(Arrays.asList("std", "vector"))).isEqualTo("container");
            assertThat(namespaceMap.getSymbol(Arrays.asList("boost", "vector"))).isEqualTo("math_vector");
            assertThat(namespaceMap.getSymbol(Arrays.asList("custom", "utils", "helper"))).isEqualTo("utility");
        }

        @Test
        @DisplayName("Should support dynamic scope modification")
        void testDynamicScopeModification() {
            ScopedMap<String> dynamicMap = new ScopedMap<>();

            // Initial state
            dynamicMap.addSymbol(Arrays.asList("function"), "var", "initial");
            assertThat(dynamicMap.getSymbol(Arrays.asList("function", "var"))).isEqualTo("initial");

            // Add to deeper scope
            dynamicMap.addSymbol(Arrays.asList("function", "inner"), "var", "inner_value");
            assertThat(dynamicMap.getSymbol(Arrays.asList("function", "inner", "var"))).isEqualTo("inner_value");

            // Original should be unchanged
            assertThat(dynamicMap.getSymbol(Arrays.asList("function", "var"))).isEqualTo("initial");

            // Add another variable to existing scope
            dynamicMap.addSymbol(Arrays.asList("function"), "newvar", "new_value");
            assertThat(dynamicMap.getSymbol(Arrays.asList("function", "newvar"))).isEqualTo("new_value");
        }
    }
}
