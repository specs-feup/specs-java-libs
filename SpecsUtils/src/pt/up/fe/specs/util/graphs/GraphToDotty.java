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

import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.util.SpecsGraphviz;

/**
 *
 * @author Joao Bispo
 */
public class GraphToDotty {

    public static <GN extends GraphNode<GN, N, C>, N, C> String getDotty(Graph<GN, N, C> graph) {
        // Build Declarations and Connections
        List<String> declarations = new ArrayList<>();
        List<String> connections = new ArrayList<>();
        for (GN graphNode : graph.getNodeList()) {
            declarations.add(getDeclaration(graphNode));

            for (int i = 0; i < graphNode.getChildrenConnections().size(); i++) {
                String connection = getConnection(graphNode, i);
                connections.add(connection);
            }
        }

        return SpecsGraphviz.generateGraph(declarations, connections);
    }

    public static <GN extends GraphNode<GN, N, C>, N, C> String getDeclaration(GN node) {
        N nodeInfo = node.getNodeInfo();
        return SpecsGraphviz.declaration(node.getId(), nodeInfo.toString(),
                "box", "white");
    }

    public static <GN extends GraphNode<GN, N, C>, N, C> String getConnection(GN node, int index) {
        String inputId = node.getId();
        String outputId = node.getChildren().get(index).getId();

        String label = node.getChildrenConnections().get(index).toString();

        return SpecsGraphviz.connection(inputId, outputId, label);
    }
}
