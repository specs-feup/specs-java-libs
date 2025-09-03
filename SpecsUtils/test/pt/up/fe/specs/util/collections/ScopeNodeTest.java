package pt.up.fe.specs.util.collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprehensive test suite for {@link ScopeNode}.
 * Tests hierarchical scope management and symbol storage functionality.
 * 
 * @author Generated Tests
 */
class ScopeNodeTest {

    private ScopeNode<String> scopeNode;

    @BeforeEach
    void setUp() {
        scopeNode = new ScopeNode<>();
    }

    @Nested
    @DisplayName("Constructor and Basic Properties")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty scope node")
        void testScopeNodeCreation() {
            assertThat(scopeNode.getSymbols()).isEmpty();
            assertThat(scopeNode.getScopes()).isEmpty();
            assertThat(scopeNode.getKeys()).isEmpty();
        }

        @Test
        @DisplayName("Should have empty symbols map initially")
        void testInitialSymbolsMap() {
            Map<String, String> symbols = scopeNode.getSymbols();
            assertThat(symbols).isNotNull();
            assertThat(symbols).isEmpty();
        }

        @Test
        @DisplayName("Should have empty scopes list initially")
        void testInitialScopesList() {
            List<String> scopes = scopeNode.getScopes();
            assertThat(scopes).isNotNull();
            assertThat(scopes).isEmpty();
        }
    }

    @Nested
    @DisplayName("Symbol Management")
    class SymbolManagementTests {

        @Test
        @DisplayName("Should add symbol successfully")
        void testAddSymbol() {
            scopeNode.addSymbol("var1", "value1");

            assertThat(scopeNode.getSymbols()).hasSize(1);
            assertThat(scopeNode.getSymbols()).containsEntry("var1", "value1");
        }

        @Test
        @DisplayName("Should replace existing symbol")
        void testReplaceSymbol() {
            scopeNode.addSymbol("var1", "value1");
            scopeNode.addSymbol("var1", "value2");

            assertThat(scopeNode.getSymbols()).hasSize(1);
            assertThat(scopeNode.getSymbols()).containsEntry("var1", "value2");
        }

        @Test
        @DisplayName("Should add multiple symbols")
        void testAddMultipleSymbols() {
            scopeNode.addSymbol("var1", "value1");
            scopeNode.addSymbol("var2", "value2");
            scopeNode.addSymbol("var3", "value3");

            assertThat(scopeNode.getSymbols()).hasSize(3);
            assertThat(scopeNode.getSymbols()).containsEntry("var1", "value1");
            assertThat(scopeNode.getSymbols()).containsEntry("var2", "value2");
            assertThat(scopeNode.getSymbols()).containsEntry("var3", "value3");
        }

        @Test
        @DisplayName("Should get symbol by name")
        void testGetSymbol() {
            scopeNode.addSymbol("var1", "value1");

            assertThat(scopeNode.getSymbol("var1")).isEqualTo("value1");
        }

        @Test
        @DisplayName("Should return null for non-existent symbol")
        void testGetNonExistentSymbol() {
            assertThat(scopeNode.getSymbol("nonexistent")).isNull();
        }

        @Test
        @DisplayName("Should get symbol with varargs key")
        void testGetSymbolVarargs() {
            scopeNode.addSymbol("var1", "value1");

            assertThat(scopeNode.getSymbol("var1")).isEqualTo("value1");
            assertThat(scopeNode.getSymbol("nonexistent")).isNull();
        }

        @Test
        @DisplayName("Should get symbol with list key")
        void testGetSymbolWithList() {
            scopeNode.addSymbol("var1", "value1");

            assertThat(scopeNode.getSymbol(Arrays.asList("var1"))).isEqualTo("value1");
            assertThat(scopeNode.getSymbol(Arrays.asList("nonexistent"))).isNull();
        }

        @Test
        @DisplayName("Should return null for empty key list")
        void testGetSymbolEmptyList() {
            assertThat(scopeNode.getSymbol(Collections.emptyList())).isNull();
        }
    }

    @Nested
    @DisplayName("Hierarchical Scope Management")
    class HierarchicalScopeTests {

        @Test
        @DisplayName("Should add symbol with scope path")
        void testAddSymbolWithScope() {
            scopeNode.addSymbol(Arrays.asList("scope1", "var1"), "value1");

            assertThat(scopeNode.getScopes()).contains("scope1");
            assertThat(scopeNode.getSymbol(Arrays.asList("scope1", "var1"))).isEqualTo("value1");
        }

        @Test
        @DisplayName("Should add symbol with multiple scope levels")
        void testAddSymbolMultipleLevels() {
            scopeNode.addSymbol(Arrays.asList("scope1", "scope2", "var1"), "value1");

            assertThat(scopeNode.getScopes()).contains("scope1");
            assertThat(scopeNode.getSymbol(Arrays.asList("scope1", "scope2", "var1"))).isEqualTo("value1");
        }

        @Test
        @DisplayName("Should add symbol with scope and name separately")
        void testAddSymbolScopeAndName() {
            scopeNode.addSymbol(Arrays.asList("scope1"), "var1", "value1");

            assertThat(scopeNode.getSymbol(Arrays.asList("scope1", "var1"))).isEqualTo("value1");
        }

        @Test
        @DisplayName("Should add symbol with null scope")
        void testAddSymbolNullScope() {
            scopeNode.addSymbol(null, "var1", "value1");

            assertThat(scopeNode.getSymbol("var1")).isEqualTo("value1");
        }

        @Test
        @DisplayName("Should throw exception for null name")
        void testAddSymbolNullName() {
            assertThatThrownBy(() -> scopeNode.addSymbol(Arrays.asList("scope1"), null, "value1"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("'null' is not allowed as a name");
        }

        @Test
        @DisplayName("Should handle empty key gracefully")
        void testAddSymbolEmptyKey() {
            scopeNode.addSymbol(Collections.emptyList(), "value1");

            // Should not crash and maintain empty state
            assertThat(scopeNode.getKeys()).isEmpty();
        }

        @Test
        @DisplayName("Should get scope node by path")
        void testGetScopeNode() {
            scopeNode.addSymbol(Arrays.asList("scope1", "var1"), "value1");

            ScopeNode<String> childScope = scopeNode.getScopeNode(Arrays.asList("scope1"));
            assertThat(childScope).isNotNull();
            assertThat(childScope.getSymbol("var1")).isEqualTo("value1");
        }

        @Test
        @DisplayName("Should return null for non-existent scope")
        void testGetNonExistentScope() {
            assertThat(scopeNode.getScopeNode(Arrays.asList("nonexistent"))).isNull();
        }

        @Test
        @DisplayName("Should return null for empty scope path")
        void testGetScopeEmptyPath() {
            assertThat(scopeNode.getScopeNode(Collections.emptyList())).isNull();
        }

        @Test
        @DisplayName("Should get scope by name")
        void testGetScope() {
            scopeNode.addSymbol(Arrays.asList("scope1", "var1"), "value1");

            ScopeNode<String> childScope = scopeNode.getScope("scope1");
            assertThat(childScope).isNotNull();
            assertThat(childScope.getSymbol("var1")).isEqualTo("value1");
        }
    }

    @Nested
    @DisplayName("Key Management")
    class KeyManagementTests {

        @Test
        @DisplayName("Should get all keys from flat structure")
        void testGetKeysFlat() {
            scopeNode.addSymbol("var1", "value1");
            scopeNode.addSymbol("var2", "value2");

            List<List<String>> keys = scopeNode.getKeys();
            assertThat(keys).hasSize(2);
            assertThat(keys).contains(Arrays.asList("var1"));
            assertThat(keys).contains(Arrays.asList("var2"));
        }

        @Test
        @DisplayName("Should get all keys from hierarchical structure")
        void testGetKeysHierarchical() {
            scopeNode.addSymbol("var1", "value1");
            scopeNode.addSymbol(Arrays.asList("scope1", "var2"), "value2");
            scopeNode.addSymbol(Arrays.asList("scope1", "scope2", "var3"), "value3");

            List<List<String>> keys = scopeNode.getKeys();
            assertThat(keys).hasSize(3);
            assertThat(keys).contains(Arrays.asList("var1"));
            assertThat(keys).contains(Arrays.asList("scope1", "var2"));
            assertThat(keys).contains(Arrays.asList("scope1", "scope2", "var3"));
        }

        @Test
        @DisplayName("Should return empty keys for empty scope")
        void testGetKeysEmpty() {
            List<List<String>> keys = scopeNode.getKeys();
            assertThat(keys).isEmpty();
        }
    }

    @Nested
    @DisplayName("String Representation")
    class StringRepresentationTests {

        @Test
        @DisplayName("Should have string representation for empty scope")
        void testToStringEmpty() {
            String result = scopeNode.toString();
            assertThat(result).isNotNull();
            assertThat(result).contains("{}");
        }

        @Test
        @DisplayName("Should have string representation with symbols")
        void testToStringWithSymbols() {
            scopeNode.addSymbol("var1", "value1");

            String result = scopeNode.toString();
            assertThat(result).isNotNull();
            assertThat(result).contains("var1");
            assertThat(result).contains("value1");
        }

        @Test
        @DisplayName("Should have string representation with scopes")
        void testToStringWithScopes() {
            scopeNode.addSymbol(Arrays.asList("scope1", "var1"), "value1");

            String result = scopeNode.toString();
            assertThat(result).isNotNull();
            assertThat(result).contains("scope1");
        }
    }

    @Nested
    @DisplayName("Complex Scenarios")
    class ComplexScenarioTests {

        @Test
        @DisplayName("Should handle mixed flat and hierarchical symbols")
        void testMixedStructure() {
            scopeNode.addSymbol("global", "globalValue");
            scopeNode.addSymbol(Arrays.asList("namespace1", "var1"), "ns1Value1");
            scopeNode.addSymbol(Arrays.asList("namespace1", "var2"), "ns1Value2");
            scopeNode.addSymbol(Arrays.asList("namespace2", "var1"), "ns2Value1");

            assertThat(scopeNode.getSymbol("global")).isEqualTo("globalValue");
            assertThat(scopeNode.getSymbol(Arrays.asList("namespace1", "var1"))).isEqualTo("ns1Value1");
            assertThat(scopeNode.getSymbol(Arrays.asList("namespace1", "var2"))).isEqualTo("ns1Value2");
            assertThat(scopeNode.getSymbol(Arrays.asList("namespace2", "var1"))).isEqualTo("ns2Value1");

            assertThat(scopeNode.getScopes()).containsExactlyInAnyOrder("namespace1", "namespace2");
            assertThat(scopeNode.getKeys()).hasSize(4);
        }

        @Test
        @DisplayName("Should handle deep nesting")
        void testDeepNesting() {
            List<String> deepPath = Arrays.asList("level1", "level2", "level3", "level4", "var");
            scopeNode.addSymbol(deepPath, "deepValue");

            assertThat(scopeNode.getSymbol(deepPath)).isEqualTo("deepValue");

            // Verify intermediate scopes exist
            assertThat(scopeNode.getScopeNode(Arrays.asList("level1"))).isNotNull();
            assertThat(scopeNode.getScopeNode(Arrays.asList("level1", "level2"))).isNotNull();
            assertThat(scopeNode.getScopeNode(Arrays.asList("level1", "level2", "level3"))).isNotNull();
            assertThat(scopeNode.getScopeNode(Arrays.asList("level1", "level2", "level3", "level4"))).isNotNull();
        }

        @Test
        @DisplayName("Should handle symbol shadowing between scopes")
        void testSymbolShadowing() {
            scopeNode.addSymbol("var", "globalValue");
            scopeNode.addSymbol(Arrays.asList("scope1", "var"), "scope1Value");
            scopeNode.addSymbol(Arrays.asList("scope1", "scope2", "var"), "scope2Value");

            assertThat(scopeNode.getSymbol("var")).isEqualTo("globalValue");
            assertThat(scopeNode.getSymbol(Arrays.asList("scope1", "var"))).isEqualTo("scope1Value");
            assertThat(scopeNode.getSymbol(Arrays.asList("scope1", "scope2", "var"))).isEqualTo("scope2Value");
        }
    }

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle numeric type values")
        void testNumericValues() {
            ScopeNode<Integer> intScope = new ScopeNode<>();
            intScope.addSymbol("count", 42);

            assertThat(intScope.getSymbol("count")).isEqualTo(42);
        }

        @Test
        @DisplayName("Should handle null values")
        void testNullValues() {
            scopeNode.addSymbol("nullVar", null);

            assertThat(scopeNode.getSymbols()).containsKey("nullVar");
            assertThat(scopeNode.getSymbol("nullVar")).isNull();
        }

        @Test
        @DisplayName("Should handle empty string keys")
        void testEmptyStringKey() {
            scopeNode.addSymbol("", "emptyKeyValue");

            assertThat(scopeNode.getSymbol("")).isEqualTo("emptyKeyValue");
        }

        @Test
        @DisplayName("Should handle empty string values")
        void testEmptyStringValue() {
            scopeNode.addSymbol("emptyValue", "");

            assertThat(scopeNode.getSymbol("emptyValue")).isEqualTo("");
        }
    }
}
