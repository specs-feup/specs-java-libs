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

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Utility methods for TokenWithParent.
 * 
 * @author Tiago
 * 
 */
public class NodeInsertUtils {

    /**
     * Helper method which sets 'move' to false.
     * 
     * @param baseToken
     * @param newToken
     */
    public static <K extends TreeNode<K>> void insertBefore(K baseToken, K newToken) {
        insertBefore(baseToken, newToken, false);
    }

    /**
     * Inserts 'newNode' before the 'baseToken'.
     * 
     * 
     * @param baseToken
     * @param newToken
     * @param move
     *            if true, makes sure parent of newToken is null, removing it from the parent if necessary. Otherwise,
     *            if parent is not null, inserts a copy.
     */
    public static <K extends TreeNode<K>> void insertBefore(K baseToken, K newToken, boolean move) {

        K parent = baseToken.getParent();
        if (parent == null) {
            SpecsLogs.msgWarn("Given 'baseToken' does not have a parent:\n" + baseToken);
            return;
        }

        List<K> children = parent.getChildren();

        int rootTokenIndex = children.indexOf(baseToken);

        // If 'move' is false, just do nothing
        // This means that if parent is not null, it will do a copy; if it is null, will just insert it
        if (move) {
            processNewToken(newToken);
        }

        // System.out.println("CHILDREN BEFORE:\n"+parent.getChildren());
        parent.addChild(rootTokenIndex, newToken);
        // System.out.println("CHILDREN AFTER:\n"+parent.getChildren());
    }

    /**
     * Ensures the node has a null parent.
     * 
     * @param newToken
     */
    private static <K extends TreeNode<K>> void processNewToken(K newToken) {
        if (!newToken.hasParent()) {
            return;
        }

        newToken.getParent().removeChild(newToken.indexOfSelf());
    }

    /**
     * Inserts 'newNode' after the 'baseToken'.
     * 
     * @param baseToken
     * @param newToken
     */
    public static <K extends TreeNode<K>> void insertAfter(K baseToken, K newToken) {
        insertAfter(baseToken, newToken, false);
    }

    public static <K extends TreeNode<K>> void insertAfter(K baseToken, K newToken, boolean move) {

        K parent = baseToken.getParent();
        if (parent == null) {
            SpecsLogs.msgWarn("Given 'baseToken' does not have a parent.");
            return;
        }

        List<K> children = parent.getChildren();

        int rootTokenIndex = children.indexOf(baseToken) + 1;

        // If 'move' is false, just do nothing
        // This means that if parent is not null, it will do a copy; if it is null, will just insert it
        if (move) {
            processNewToken(newToken);
        }

        parent.addChild(rootTokenIndex, newToken);
    }

    /**
     * Replaces 'baseToken' with 'newToken'.
     * 
     * @param baseToken
     * @param newToken
     * @return The new inserted token (same as newToken if newToken.getParent() was null, and a copy of newToken
     *         otherwise).
     */
    public static <K extends TreeNode<K>> K replace(K baseToken, K newToken) {
        return replace(baseToken, newToken, false);
    }

    /**
     * If move is true,
     * 
     * @param baseToken
     * @param newToken
     * @param move
     * @return
     */
    public static <K extends TreeNode<K>> K replace(K baseToken, K newToken, boolean move) {

        K parent = baseToken.getParent();
        if (parent == null) {
            SpecsLogs.msgWarn("Given 'baseToken' does not have a parent. Token:\n" + baseToken);
            return newToken;
        }

        // List<K> children = parent.getChildren();
        // int rootTokenIndex = children.indexOf(baseToken);

        // System.out.println("BEFIRE:" + parent.getChildren());

        int rootTokenIndex = parent.indexOfChild(baseToken);

        // If move is enabled, remove parent before setting
        if (move && newToken.hasParent()) {
            // newToken.removeParent();
            newToken.detach();
        }

        parent.setChild(rootTokenIndex, newToken);

        // parent.setChild(baseToken, newToken);
        // System.out.println("ADFTER:" + parent.getChildren());
        // return parent.getChild(rootTokenIndex);
        return newToken;
    }

    /**
     * Removes 'baseToken'.
     * 
     * @param baseToken
     * @param newToken
     */
    public static <K extends TreeNode<K>> void delete(K baseToken) {

        K parent = baseToken.getParent();
        if (parent == null) {
            SpecsLogs.msgWarn("Given 'baseToken' does not have a parent.");
            return;
        }

        List<K> children = parent.getChildren();

        int rootTokenIndex = children.indexOf(baseToken);
        parent.removeChild(rootTokenIndex);
    }

    /**
     * Replaces 'baseToken' with 'newNode'. Uses the children of 'baseToken' instead of 'newNode'.
     * 
     * @param baseToken
     * @param newToken
     */
    public static <K extends TreeNode<K>> void set(K baseToken, K newToken) {

        if (newToken.hasChildren()) {
            SpecsLogs.msgWarn("New token to replace has children, they will be ignored.");
        }

        if (!baseToken.hasParent()) {
            throw new RuntimeException("Node does not have a parent, should not try to replace it");
        }

        // Exchange children
        newToken.setChildren(baseToken.getChildren());

        K parent = baseToken.getParent();

        // Exchange parent
        parent.setAsParentOf(newToken);

        // Replace in parent's children
        ChildrenIterator<K> iterator = parent.getChildrenIterator();
        while (iterator.hasNext()) {
            K child = iterator.next();
            if (child == baseToken) {
                iterator.set(newToken);
                return;
            }
        }

        throw new RuntimeException("Should have found the base node");

        // List<K> newTokenChildren = newToken.getChildren();

        // baseToken.setChildren(newTokenChildren);
        // baseToken.setType(newToken.getType());
        // baseToken.setContent(newToken.getContent());
    }

    /**
     * Calculates the rank of a given token, according to the provided test.
     * 
     * @param token
     * @param test
     * @return
     */
    public static <K extends TreeNode<K>> List<Integer> getRank(K token, TokenTester test) {

        K currentToken = token;
        K parent = null;

        List<Integer> rank = SpecsFactory.newLinkedList();

        while ((parent = getParent(currentToken, test)) != null) {
            Integer selfRank = getSelfRank(parent, currentToken, test);

            rank.add(0, selfRank);

            currentToken = parent;
        }

        // Get root node
        parent = currentToken.getRoot();
        Integer selfRank = getSelfRank(parent, currentToken, test);
        rank.add(0, selfRank);

        // Integer currentRank = getSelfRank(token, test);
        // System.out.println("SELF RANK:"+currentRank);
        return rank;
    }

    /**
     * Goes to the parent, and checks in which position is the current node.
     * 
     * @param token
     * @param test
     * @return
     */
    private static <K extends TreeNode<K>> Integer getSelfRank(K parent, K token,
            TokenTester test) {

        // Get first parent that passes the test
        // K parent = getParent(token, test);

        // If null, get root node
        // if(parent == null) {
        // parent = token.getRoot();
        // }

        // Get iterator with pruning
        Iterator<K> iterator = IteratorUtils.getDepthIterator(parent, test, true);
        int counter = 1;
        for (K parentChild : SpecsCollections.iterable(iterator)) {
            if (parentChild.equals(token)) {
                return counter;
            }

            counter += 1;
        }

        throw new RuntimeException("Could not find child again inside parent.");
    }

    /**
     * 
     * @param token
     * @param test
     * @return the first parent that passes the test, or null if no parent passes it
     */
    public static <K extends TreeNode<K>> K getParent(K token, TokenTester test) {
        // Check if token as parent
        if (token.getParent() == null) {
            return null;
        }

        // Check if parent passes the test
        if (test.test(token.getParent())) {
            return token.getParent();
        }

        return getParent(token.getParent(), test);
    }

    /**
     * Swaps the positions of node1 and node2.
     * 
     * <p>
     * If 'swapSubtrees' is enabled, this transformation is not allowed if any of the nodes is a part of the subtree of
     * the other.
     * 
     * @param node1
     * @param node2
     * @param swapSubtrees
     *            if true, swaps the complete subtrees. Otherwise, swaps only the nodes, and children are kept in place.
     */
    public static <K extends TreeNode<K>> void swap(K node1, K node2, boolean swapSubtrees) {
        // If swap subtrees is enable, check if a node is an ancestor of the other
        if (swapSubtrees) {
            Optional<K> ancestorTry = TreeNodeUtils.getAncestor(node1, node2);
            if (ancestorTry.isPresent()) {
                K ancestor = ancestorTry.get();
                K descendent = ancestor == node1 ? node2 : node1;
                SpecsLogs.msgInfo("Could not swap nodes, node '" + ancestor.getNodeName() + "' is an ancestor of '"
                        + descendent.getNodeName() + "'");
                return;
            }
        }

        K dummy = node1.copyShallow();

        // Put dummy node in place of node 1
        NodeInsertUtils.replace(node1, dummy);
        // Put node 1 in place of node 2
        NodeInsertUtils.replace(node2, node1);
        // Put node 2 in place of dummy
        NodeInsertUtils.replace(dummy, node2);
    }

}
