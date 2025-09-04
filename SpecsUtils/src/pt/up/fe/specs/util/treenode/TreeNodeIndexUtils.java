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

package pt.up.fe.specs.util.treenode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Utility methods related to retrieving and using indexes from tree Tokens.
 * 
 * @author Joao Bispo
 * 
 */
public class TreeNodeIndexUtils {

    /**
     * Returns all indexes where the MatlabToken of the given type appears. If no
     * token of that type is found returns an empty list.
     * 
     * @param tokenType
     * @param tokens
     * @return
     */
    public static <K extends TreeNode<K>> List<Integer> indexesOf(
            List<K> tokens, Class<? extends K> type) {

        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < tokens.size(); i++) {
            if (type.isInstance(tokens.get(i))) {
                indexes.add(i);
            }
        }

        return indexes;
    }

    /**
     * Helper method with variadic inputs.
     * 
     * @param root
     * @param indexes
     * @return
     */
    public static <K extends TreeNode<K>> K getChild(K root, Integer... indexes) {
        return getChild(root, Arrays.asList(indexes));
    }

    /**
     * Returns a child corresponding to the consecutive accesses indicated by the
     * given indexes.
     * 
     * <p>
     * E.g.: In a structure A -> B -> C, getChild(A, 0, 0) will access the index 0
     * of A, which is B, and then the index 0 of B, returning token C.
     * 
     * <p>
     * If any problem happens (e.g., trying to access a child that does not exist)
     * an exception is thrown.
     * 
     * @param indexes
     * @return
     */
    public static <K extends TreeNode<K>> K getChild(K root,
            List<Integer> indexes) {

        K currentToken = root;
        for (Integer index : indexes) {
            if (!currentToken.hasChildren()) {
                throw new RuntimeException("Trying to access index '" + index
                        + "' of a token without children.\nToken:" + currentToken);
            }

            if (index >= currentToken.getChildren().size()) {
                throw new RuntimeException("Trying to access index '" + index
                        + "' of a token which has size '" + currentToken.getChildren().size()
                        + "'.\nToken:" + currentToken);
            }

            currentToken = currentToken.getChildren().get(index);
        }

        return currentToken;
    }

    /**
     * In the object root, replaces the child got by using the method
     * getChild(Token, int...) by the object childToInsert.
     * 
     * <p>
     * If indexInsertion is empty or null, no modifications are made.
     * 
     * @param root           the MatlabToken object to replace a child in.
     * @param nodeToInsert   the MatlabToken object to insert
     * @param indexInsertion an array representing the indexes of the children to
     *                       select until getting the child to replace.
     * 
     * @return root once the insertion of the object childToInsert has been done. If
     *         indexInsertion is empty or null,
     *         returns root.
     */
    public static <K extends TreeNode<K>> void replaceChild(K root,
            K nodeToInsert, List<Integer> indexInsertion) {

        // control of the indexInsertion parameter
        if (indexInsertion == null) {
            return;
        }

        if (indexInsertion.isEmpty()) {
            return;
        }

        // Node where the child will be replaced
        K parentNode = null;
        if (indexInsertion.size() == 1) {
            parentNode = root;
        } else {
            int lastIdx = indexInsertion.size() - 1;
            parentNode = getChild(root, indexInsertion.subList(0, lastIdx));
        }

        // Index of the child to be replaced
        int lastIdx = indexInsertion.size() - 1;
        int replaceIdx = indexInsertion.get(lastIdx);
        parentNode.setChild(replaceIdx, nodeToInsert);
    }

    /**
     * Returns the last index of the TreeNode of the given type.
     * 
     * 
     * @param tokenType
     * @param tokens
     * @return
     */
    public static <K extends TreeNode<K>> Optional<Integer> lastIndexOf(List<K> nodes, Class<? extends K> type) {
        for (int i = nodes.size() - 1; i >= 0; i--) {
            if (type.isInstance(nodes.get(i))) {
                return Optional.of(i);
            }
        }

        return Optional.empty();
    }

    /**
     * Returns the index of the last token that is not of the given types.
     * 
     * @param currentTokens
     * @param space
     * @return
     */

    public static <K extends TreeNode<K>> Optional<Integer> lastIndexExcept(List<K> nodes,
            Collection<Class<? extends K>> exceptions) {

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
                return Optional.of(currentIndex);
            }

            currentIndex -= 1;
        }

        return Optional.empty();
    }

}
