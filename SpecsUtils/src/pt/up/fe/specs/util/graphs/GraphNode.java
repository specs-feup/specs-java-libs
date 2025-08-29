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
public abstract class GraphNode<T extends GraphNode<T, N, C>, N, C> {

    /**
     * INSTANCE VARIABLES
     */
    private final String id;
    private N nodeInfo;
    private final List<T> children;
    protected final List<T> parents;
    private final List<C> childrenConnections;
    protected final List<C> parentConnections;

    /**
     * @param id
     * @param nodeInfo
     * @param children
     * @param parents
     * @param childrenConnections
     * @param parentConnections
     */
    private GraphNode(String id, N nodeInfo, List<T> children,
            List<T> parents, List<C> childrenConnections,
            List<C> parentConnections) {

        this.id = id;
        this.nodeInfo = nodeInfo;
        this.children = parseList(children);
        this.parents = parseList(parents);
        this.childrenConnections = parseList(childrenConnections);
        this.parentConnections = parseList(parentConnections);
    }

    public GraphNode(String id, N nodeInfo) {
        this(id, nodeInfo, null, null, null, null);
    }

    private static <K> List<K> parseList(List<K> list) {
        if (list == null) {
            return new ArrayList<>();
        }

        return new ArrayList<K>(list);
    }

    public String getId() {
        return this.id;
    }

    public N getNodeInfo() {
        return this.nodeInfo;
    }

    public void replaceNodeInfo(N nodeInfo) {
        this.nodeInfo = nodeInfo;
    }

    public List<T> getChildren() {
        return this.children;
    }

    public List<T> getParents() {
        return this.parents;
    }

    public T getParent(int index) {
        return this.parents.get(index);
    }

    public T getChild(int index) {
        return this.children.get(index);
    }

    public List<C> getChildrenConnections() {
        return this.childrenConnections;
    }

    public C getChildrenConnection(int index) {
        return this.childrenConnections.get(index);
    }

    public List<C> getParentConnections() {
        return this.parentConnections;
    }

    public C getParentConnection(int index) {
        return this.parentConnections.get(index);
    }

    public synchronized void addChild(T childNode, C connectionInfo) {
        this.children.add(childNode);
        this.childrenConnections.add(connectionInfo);

        // Add parent to child
        childNode.parents.add(getThis());
        childNode.parentConnections.add(connectionInfo);
    }

    protected abstract T getThis();

    @Override
    public String toString() {
        return this.id + "->" + this.nodeInfo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        GraphNode<?, ?, ?> other = (GraphNode<?, ?, ?>) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }
}
