/**
 * Copyright 2014 SPeCS Research Group.
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

package pt.up.fe.specs.util.treenode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.SpecsCollections;

public class TreeNodeUtils {

    /**
     * Ensures that the token has a null parent.
     * 
     * @return the given token if it does not have a parent, or a copy of the token
     *         if it has (a copy of a token does not have a parent)
     */
    public static <K extends TreeNode<K>> K sanitizeNode(K token) {
        if (!token.hasParent()) {
            return token;
        }

        // Copy token
        return token.copy();
    }

    public static <K extends TreeNode<K>> String toString(K token, String prefix) {
        StringBuilder builder = new StringBuilder();

        builder.append(prefix);
        builder.append(token.toNodeString());

        builder.append("\n");

        if (token.hasChildren()) {
            for (K child : token.getChildren()) {
                builder.append(toString(child, prefix + "  "));
            }
        }

        return builder.toString();
    }

    /**
     * Gets all the descendants of a certain type from a collection of nodes.
     *
     */
    public static <I extends K, O extends K, K extends TreeNode<K>> List<O> getDescendants(Class<O> aClass,
            Collection<I> nodes) {

        return nodes.stream()
                .map(st -> st.getDescendants(aClass))
                .reduce(new ArrayList<>(), SpecsCollections::add);
    }

    /**
     * Gets all the descendants of a certain type from a collection of nodes. In
     * addition, if any of the provided nodes are of that class, then they are
     * returned as well.
     *
     */
    public static <I extends K, O extends K, K extends TreeNode<K>> List<O> getDescendantsAndSelves(Class<O> aClass,
            Collection<I> nodes) {

        return nodes.stream()
                .map(st -> st.getDescendantsAndSelf(aClass))
                .reduce(new ArrayList<>(), SpecsCollections::add);
    }

    /**
     * Returns the index of the last token that is not of the given types.
     *
     */
    public static <K extends TreeNode<K>> Optional<K> lastNodeExcept(List<K> nodes,
            Collection<Class<? extends K>> exceptions) {

        Optional<Integer> index = TreeNodeIndexUtils.lastIndexExcept(nodes, exceptions);
        return index.map(nodes::get);
    }

    /**
     * Tests two nodes, to check if one is ancestor of the other. If this is the
     * case, returns the ancestor, otherwise returns Optional.empty().
     *
     */
    public static <K extends TreeNode<K>> Optional<K> getAncestor(K node1, K node2) {
        if (node1.isAncestor(node2)) {
            return Optional.of(node2);
        }

        if (node2.isAncestor(node1)) {
            return Optional.of(node1);
        }

        return Optional.empty();
    }

    @SuppressWarnings("unchecked")
    public static <K extends TreeNode<K>, EK extends K> List<EK> copy(List<EK> nodes) {
        return nodes.stream()
                .map(node -> (EK) node.copy())
                .collect(Collectors.toList());
    }

}
