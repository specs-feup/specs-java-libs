/**
 * Copyright 2012 SPeCS Research Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.util.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Map which stores values according to a scope, defined by a list of Strings.
 *
 * @author Joao Bispo
 *
 */
public class ScopedMap<V> {

    private final ScopeNode<V> rootNode;

    private List<String> firstScope;

    /**
     * Creates an empty SymbolMap.
     *
     */
    public ScopedMap() {
        this.rootNode = new ScopeNode<>();

        this.firstScope = null;
    }

    public static <V> ScopedMap<V> newInstance() {
        return new ScopedMap<>();
    }

    /**
     * Helper method with variadic inputs.
     *
     */
    public ScopedMap<V> getSymbolMap(String... scope) {
        return getSymbolMap(Arrays.asList(scope));
    }

    /**
     * Builds a new SymbolMap with the variables of the specified scope, but without
     * preserving the original scope.
     *
     * <p>
     * For instance, if a scope 'x' is asked, the scopes in the returned SymbolMap
     * will start after 'x'.
     *
     */
    public ScopedMap<V> getSymbolMap(List<String> scope) {
        ScopedMap<V> scopedVariables = new ScopedMap<>();

        ScopeNode<V> scopeNode = getScopeNode(scope);
        if (scopeNode == null) {
            return scopedVariables;
        }

        scopedVariables.addSymbols(scopeNode);
        return scopedVariables;
    }

    private void addSymbols(ScopeNode<V> scopeNode) {
        for (List<String> key : scopeNode.getKeys()) {
            V symbol = scopeNode.getSymbol(key);
            List<String> keyScope = key.subList(0, key.size() - 1);
            String name = key.get(key.size() - 1);

            addSymbol(keyScope, name, symbol);
        }
    }

    /**
     * Returns the keys corresponding to all entries in this map.
     *
     */
    public List<List<String>> getKeys() {
        return this.rootNode.getKeys();
    }

    /**
     * Helper method with variadic inputs.
     *
     */
    public V getSymbol(String... key) {
        return this.rootNode.getSymbol(key);
    }

    /**
     * Returns the symbol mapped to the given key. If a symbol cannot be found,
     * returns null.
     *
     * <p>
     * A key is composed by a scope, in the form of a list of Strings, plus a String
     * with the name of the symbol.
     *
     */
    public V getSymbol(List<String> key) {
        return this.rootNode.getSymbol(key);
    }

    /**
     * Helper method, with scope and symbol name given separately.
     *
     */
    public V getSymbol(List<String> scope, String variableName) {
        List<String> key = new ArrayList<>(scope);
        key.add(variableName);
        return getSymbol(key);
    }

    /**
     * Helper method, with scope and symbol name given separately.
     *
     *
     */
    public void addSymbol(List<String> scope, String name, V symbol) {
        this.rootNode.addSymbol(scope, name, symbol);

        // Set default scope
        if (this.firstScope != null) {
            this.firstScope = new ArrayList<>(scope);
        }
    }

    /**
     * Adds a symbol mapped to the given key.
     *
     * <p>
     * A key is composed by a scope, in the form of a list of Strings, plus a String
     * with the name of the symbol.
     *
     */
    public void addSymbol(List<String> key, V symbol) {
        this.rootNode.addSymbol(key, symbol);
    }

    /**
     * Helper method which receives only one key element.
     *
     */
    public void addSymbol(String key, V symbol) {
        this.rootNode.addSymbol(Collections.singletonList(key), symbol);
    }

    /**
     * Helper method which receives several key elements.
     *
     */
    public void addSymbol(V symbol, String... key) {
        this.rootNode.addSymbol(Arrays.asList(key), symbol);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return String.valueOf(this.rootNode);
    }

    /**
     * Adds all the symbols in the given map to the current map, preserving the
     * original scope.
     *
     */
    public void addSymbols(ScopedMap<V> map) {
        for (List<String> key : map.getKeys()) {
            V symbol = map.getSymbol(key);
            if (symbol == null) {
                SpecsLogs.warn("Null symbol for key '" + key + "'. Table:\n" + map.rootNode);
            }

            this.rootNode.addSymbol(key, symbol);
        }
    }

    /**
     * Adds all the symbols in the given map to the current map, mapping them to the
     * given scope.
     *
     */
    public void addSymbols(List<String> scope, ScopedMap<V> symbolMap) {
        Map<String, V> symbols = symbolMap.getSymbols(null);

        // Add each symbol to the given scope
        for (String symbolName : symbols.keySet()) {
            V symbol = symbols.get(symbolName);
            addSymbol(scope, symbolName, symbol);
        }
    }

    private ScopeNode<V> getScopeNode(List<String> scope) {
        return this.rootNode.getScopeNode(scope);
    }

    /**
     * Returns a map with all the symbols for a given scope, mapped to their name.
     *
     */
    public Map<String, V> getSymbols(List<String> scope) {
        if (scope == null) {
            return this.rootNode.getSymbols();
        }

        if (scope.isEmpty()) {
            return this.rootNode.getSymbols();
        }

        ScopeNode<V> scopeNode = getScopeNode(scope);
        if (scopeNode == null) {
            return new HashMap<>();
        }

        return scopeNode.getSymbols();
    }

    /**
     *
     * @return a collection with all the symbols in the map
     */
    public List<V> getSymbols() {
        List<V> symbols = new ArrayList<>();
        for (List<String> key : getKeys()) {
            symbols.add(getSymbol(key));
        }

        return symbols;
    }

    /**
     * Checks if the given scope contains a symbol for the given name.
     *
     */
    public boolean containsSymbol(List<String> scope, String symbolName) {
        Map<String, V> varTable = getSymbols(scope);
        V variable = varTable.get(symbolName);
        return variable != null;
    }

}
