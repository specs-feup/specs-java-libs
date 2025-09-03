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

/**
 * 
 * @author Joao Bispo
 */
public class GraphSerializable<N, C> {

    // Nodes
    public final List<String> operationIds;
    public final List<N> nodeInfos;

    // Connections
    public final List<String> inputIds;
    public final List<String> outputIds;
    public final List<C> connInfos;

    public GraphSerializable(List<String> operationIds, List<N> nodeInfos, List<String> inputIds,
            List<String> outputIds, List<C> connInfos) {
        this.operationIds = operationIds;
        this.nodeInfos = nodeInfos;
        this.inputIds = inputIds;
        this.outputIds = outputIds;
        this.connInfos = connInfos;
    }

    public static <T extends GraphNode<T, N, C>, N, C> GraphSerializable<N, C> toSerializable(
            Graph<T, N, C> graph) {
        List<String> operationIds = new ArrayList<>();
        List<N> nodeInfos = new ArrayList<>();

        for (T node : graph.getNodeList()) {
            operationIds.add(node.getId());
            nodeInfos.add(node.getNodeInfo());
        }

        List<String> inputIds = new ArrayList<>();
        List<String> outputIds = new ArrayList<>();
        List<C> connInfos = new ArrayList<>();

        for (T node : graph.getNodeList()) {

            // Add children connections
            for (int i = 0; i < node.getChildren().size(); i++) {
                inputIds.add(node.getId());
                outputIds.add(node.getChildren().get(i).getId());
                connInfos.add(node.getChildrenConnections().get(i));
            }

        }

        return new GraphSerializable<>(operationIds, nodeInfos, inputIds, outputIds,
                connInfos);
    }

    public static <T extends GraphNode<T, N, C>, N, C> void fromSerializable(
            GraphSerializable<N, C> graph, Graph<T, N, C> newGraph) {

        for (int i = 0; i < graph.operationIds.size(); i++) {
            newGraph.addNode(graph.operationIds.get(i), graph.nodeInfos.get(i));
        }

        for (int i = 0; i < graph.connInfos.size(); i++) {
            newGraph.addConnection(graph.inputIds.get(i), graph.outputIds.get(i),
                    graph.connInfos.get(i));
        }
    }

}
