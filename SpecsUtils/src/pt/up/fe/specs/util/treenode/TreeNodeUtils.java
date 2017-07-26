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

import pt.up.fe.specs.util.SpecsCollections;

public class TreeNodeUtils {

    /**
     * Ensures that the token has a null parent.
     * 
     * @param token
     * @return the given token if it does not have a parent, or a copy of the token if it has (a copy of a token does
     *         not have a parent)
     */
    // public static <E extends Enum<E>, K extends TreeNode<E, K>> K sanitizeToken(K token) {
    public static <K extends TreeNode<K>> K sanitizeNode(K token) {
	if (!token.hasParent()) {
	    return token;
	}

	// Copy token
	K tokenCopy = token.copy();
	return tokenCopy;
    }

    // public static <K extends TreeNode<E, K>, E extends Enum<E>> String toString(K token, String prefix) {
    public static <K extends TreeNode<K>> String toString(K token, String prefix) {
	StringBuilder builder = new StringBuilder();

	// builder.append(prefix).append(token.getType());
	builder.append(prefix);
	// builder.append(token.toNodeString() + "(" + token.getClass().getSimpleName() + ")");
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
     * @param aClass
     * @param nodes
     * @return
     */
    public static <I extends K, O extends K, K extends TreeNode<K>> List<O> getDescendants(Class<O> aClass,
	    Collection<I> nodes) {

	return nodes.stream()
		.map(st -> st.getDescendants(aClass))
		.reduce(new ArrayList<>(), SpecsCollections::add);
    }

    /**
     * Gets all the descendants of a certain type from a collection of nodes. In addition, if any of the provided nodes
     * are of that class, then they are returned as well.
     * 
     * @param aClass
     * @param nodes
     * @return
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
     * @param currentTokens
     * @param space
     * @return
     */
    public static <K extends TreeNode<K>> Optional<K> lastNodeExcept(List<K> nodes,
	    Collection<Class<? extends K>> exceptions) {

	Optional<Integer> index = TreeNodeIndexUtils.lastIndexExcept(nodes, exceptions);
	if (!index.isPresent()) {
	    return Optional.empty();
	}

	return Optional.of(nodes.get(index.get()));

	/*
		return currentTokens.get(index);
	
		int currentIndex = nodes.size() - 1;
		while (currentIndex >= 0) {
		    K token = nodes.get(currentIndex);
	
		    boolean isException = false;
		    for (Class<?> exception : exceptions) {
			if (exception.isInstance(token)) {
			    isException = true;
			}
		    }
	
		    if (!isException) {
			return Optional.of(nodes.get(currentIndex));
		    }
	
		    currentIndex -= 1;
		}
	
		return Optional.empty();
		*/
    }

}
