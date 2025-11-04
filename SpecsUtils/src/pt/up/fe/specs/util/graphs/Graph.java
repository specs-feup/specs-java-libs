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
 * specific language governing permissions and limitations under the License.
 */
package pt.up.fe.specs.util.graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * 
 * @author Joao Bispo
 */
public abstract class Graph<GN extends GraphNode<GN, N, C>, N, C> {

    // List of nodes in the graph
    private final List<GN> nodeList;
    // Maps a node Id to the node itself
    private final Map<String, GN> graphNodes;

    public Graph() {
        this.nodeList = new ArrayList<>();
        this.graphNodes = new HashMap<>();
    }

    protected Graph(List<GN> nodeList, Map<String, GN> graphNodes) {
        this.nodeList = nodeList;
        this.graphNodes = graphNodes;
    }

    /**
     * Returns an unmodifiable view of this graph.
     *
     */
    public abstract Graph<GN, N, C> getUnmodifiableGraph();

    protected abstract GN newNode(String operationId, N nodeInfo);

    public synchronized GN addNode(String operationId, N nodeInfo) {
        GN oldNode = getNode(operationId);
        if (oldNode != null) {
            SpecsLogs.getLogger().warning("Node with id '" + operationId + "' already in the graph.");
            return oldNode;
        }

        GN newNode = newNode(operationId, nodeInfo);

        this.graphNodes.put(operationId, newNode);
        this.nodeList.add(newNode);

        return newNode;
    }

    public void addConnection(String sourceId, String sinkId, C connInfo) {

        // Get source node
        GN sourceNode = this.graphNodes.get(sourceId);
        if (sourceNode == null) {
            SpecsLogs.getLogger().warning("Could not find node with id '" + sourceId + "'.");
            return;
        }

        // Get destination node
        GN sinkNode = this.graphNodes.get(sinkId);
        if (sinkNode == null) {
            SpecsLogs.getLogger().warning("Could not find node with id '" + sinkId + "'.");
            return;
        }

        sourceNode.addChild(sinkNode, connInfo);
    }

    public GN getNode(String nodeId) {

        return this.graphNodes.get(nodeId);
    }

    public List<GN> getNodeList() {
        return this.nodeList;
    }

    public Map<String, GN> getGraphNodes() {
        return this.graphNodes;
    }

    @Override
    public String toString() {
        return this.nodeList.toString();
    }

    /**
     * Removes a node from the graph.
     *
     */
    public void remove(String nodeId) {
        GN node = this.graphNodes.get(nodeId);
        if (node == null) {
            SpecsLogs.getLogger().warning("Given node does not belong to the graph:" + node);
            return;
        }

        remove(node);
    }

    /**
     * Removes a node from the graph.
     *
     */
    public void remove(GN node) {
        // Check if node is part of the graph
        if (this.graphNodes.get(node.getId()) != node) {
            SpecsLogs.getLogger().warning("Given node does not belong to the graph:" + node);
            return;
        }

        List<C> childrenConnections = node.getChildrenConnections();
        List<GN> children = node.getChildren();
        // Remove parent connection from children
        for (int i = 0; i < childrenConnections.size(); i++) {
            children.get(i).getParentConnections().remove(childrenConnections.get(i));
            children.get(i).getParents().remove(node);
        }

        List<C> parentConnections = node.getParentConnections();
        List<GN> parents = node.getParents();
        // Remove child connection from parents
        for (int i = 0; i < parentConnections.size(); i++) {
            parents.get(i).getChildrenConnections().remove(parentConnections.get(i));
            parents.get(i).getChildren().remove(node);
        }

        // Remove node
        String id = node.getId();
        this.nodeList.remove(node);
        this.graphNodes.put(id, null);
    }
}
