/*
 * Copyright 2011 SPeCS Research Group.
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
package pt.up.fe.specs.util.graphs;

/**
 * Utility methods related with the GraphV2 class.
 * 
 * @author Joao Bispo
 */
public class GraphUtils {

    /**
     * 
     * @param graph
     * @param parentId
     * @param childId
     * @return true if parentId is a parent of childId. False otherwise
     */
    public static <T extends GraphNode<T, N, C>, N, C> boolean isParent(Graph<T, N, C> graph,
	    String parentId, String childId) {

	T childNode = graph.getNode(childId);
	for (T parentNode : childNode.getParents()) {
	    if (parentNode.getId().equals(parentId)) {
		return true;
	    }
	}

	return false;
    }

}
