/**
 * Copyright 2013 SPeCS Research Group.
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
import java.util.Iterator;
import java.util.List;

public class IteratorUtils {

    public static <K extends TreeNode<K>> List<K> getTokens(Iterator<K> depthIterator,
            TokenTester loopTest) {

        List<K> tokens = new ArrayList<>();

        while (depthIterator.hasNext()) {
            K token = depthIterator.next();
            if (!loopTest.test(token)) {
                continue;
            }

            tokens.add(token);
        }

        return tokens;
    }

    /**
     * Convenience method with prunning set to false.
     *
     * @param token
     * @param loopTest
     * @return
     */
    public static <K extends TreeNode<K>> Iterator<K> getDepthIterator(K token, TokenTester loopTest) {
        return getDepthIterator(token, loopTest, false);
    }

    /**
     * Returns a depth-first iterator for the children of the given token that
     * passes the given test.
     *
     * @param token
     * @return
     */
    public static <K extends TreeNode<K>> Iterator<K> getDepthIterator(K token, TokenTester loopTest,
            boolean prune) {
        // Build list with nodes in depth-first order
        List<K> depthFirstTokens = new ArrayList<>();

        for (K child : token.getChildren()) {
            getDepthFirstTokens(child, depthFirstTokens, loopTest, prune);
        }

        return depthFirstTokens.iterator();
    }

    private static <K extends TreeNode<K>> void getDepthFirstTokens(K token, List<K> currentTokens,
            TokenTester loopTest, boolean prune) {

        boolean tokenPasses = loopTest.test(token);

        // Add self token if it passes the test
        if (tokenPasses) {
            currentTokens.add(token);
        }

        // If pruning active and token passed the test, do not process children
        if (tokenPasses && prune) {
            return;
        }

        // Add children
        for (K child : token.getChildren()) {
            getDepthFirstTokens(child, currentTokens, loopTest, prune);
        }
    }

    /**
     * Returns an object which tests for the given type
     *
     * @return
     */
    public static <E extends TreeNode<?>> TokenTester newTypeTest(Class<E> type) {
        return token -> {
            return type.isInstance(token);
        };
    }
}
