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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * Represents a scope of variables.
 * 
 * @author Joao Bispo
 * 
 */
class ScopeNode<V> {

    private final Map<String, ScopeNode<V>> childScopes;
    private final Map<String, V> symbols;

    /**
     * 
     */
    public ScopeNode() {
        this.childScopes = new LinkedHashMap<>();
        this.symbols = new LinkedHashMap<>();
    }

    /**
     * @return the symbols
     */
    public Map<String, V> getSymbols() {
        return this.symbols;
    }

    public List<String> getScopes() {
        return new ArrayList<>(this.childScopes.keySet());
    }

    public V getSymbol(String... key) {
        return getSymbol(Arrays.asList(key));
    }

    public V getSymbol(List<String> key) {
        if (key.isEmpty()) {
            return null;
        }

        if (key.size() == 1) {
            return this.symbols.get(key.get(0));

        }

        ScopeNode<V> scopeChild = this.childScopes.get(key.get(0));
        if (scopeChild == null) {
            return null;
        }

        return scopeChild.getSymbol(key.subList(1, key.size()));
    }

    public void addSymbol(String name, V symbol) {
        V previousSymbol = this.symbols.put(name, symbol);
        if (previousSymbol != null) {
            SpecsLogs.msgLib("Replacing symbol with name '" + name + "'. Previous content: '" + previousSymbol
                    + "'. Current content: '" + symbol + "'");
        }
    }

    public void addSymbol(List<String> scope, String name, V symbol) {
        if (name == null) {
            throw new RuntimeException("'null' is not allowed as a name");
        }

        if (scope == null) {
            scope = Collections.emptyList();
        }
        List<String> key = new ArrayList<>(scope);
        key.add(name);
        addSymbol(key, symbol);
    }

    public void addSymbol(List<String> key, V symbol) {
        if (key.isEmpty()) {
            SpecsLogs.warn("Empty key, symbol '" + symbol + "' not inserted.");
            return;
        }

        if (key.size() == 1) {
            addSymbol(key.get(0), symbol);
            return;
        }

        String scopeName = key.get(0);
        ScopeNode<V> childScope = this.childScopes.get(scopeName);
        if (childScope == null) {
            childScope = new ScopeNode<>();
            this.childScopes.put(scopeName, childScope);
        }

        childScope.addSymbol(key.subList(1, key.size()), symbol);
    }

    public List<List<String>> getKeys() {
        return getKeys(new ArrayList<>());
    }

    private List<List<String>> getKeys(List<String> currentScope) {
        List<List<String>> keys = new ArrayList<>();

        // Add current node keys
        for (String key : this.symbols.keySet()) {
            List<String> newKey = new ArrayList<>(currentScope);
            newKey.add(key);
            keys.add(newKey);
        }

        // Add node keys from scopes
        for (String scope : this.childScopes.keySet()) {
            List<String> newScope = new ArrayList<>(currentScope);
            newScope.add(scope);
            keys.addAll(this.childScopes.get(scope).getKeys(newScope));
        }

        return keys;
    }

    public ScopeNode<V> getScopeNode(List<String> scope) {
        if (scope.isEmpty()) {
            SpecsLogs.warn("Scope is empty.");
            return null;
        }

        String scopeName = scope.get(0);
        ScopeNode<V> childScope = this.childScopes.get(scopeName);
        if (childScope == null) {
            return null;
        }

        if (scope.size() == 1) {
            return childScope;
        }

        return childScope.getScopeNode(scope.subList(1, scope.size()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return this.symbols + "\n" + this.childScopes;
    }

    public ScopeNode<V> getScope(String scope) {
        return this.childScopes.get(scope);
    }
}
