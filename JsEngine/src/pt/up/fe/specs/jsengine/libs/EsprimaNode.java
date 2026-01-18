/**
 * Copyright 2021 SPeCS.
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

package pt.up.fe.specs.jsengine.libs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Represents a node in an Esprima AST.
 */
public class EsprimaNode {

    private static final Set<String> IGNORE_KEYS = new HashSet<>(Arrays.asList("type", "loc", "comments"));

    private final Map<String, Object> node;

    /**
     * Constructs an EsprimaNode with the given node map.
     * 
     * @param node the map representing the node
     */
    public EsprimaNode(Map<String, Object> node) {
        this.node = node;
    }

    /**
     * Retrieves the immediate children of this node.
     * 
     * @return a list of child nodes
     */
    public List<EsprimaNode> getChildren() {
        var children = new ArrayList<EsprimaNode>();

        for (var entry : node.entrySet()) {

            // Ignore certain keys
            if (IGNORE_KEYS.contains(entry.getKey())) {
                continue;
            }

            var value = entry.getValue();

            // If a Map, consider a child if it has a type
            if (value instanceof Map) {
                @SuppressWarnings("unchecked")
                var child = (Map<String, Object>) value;

                if (child.containsKey("type")) {
                    children.add(new EsprimaNode(child));
                }

                continue;
            }

            // If not a list, ignore
            if (!(value instanceof List)) {
                continue;
            }

            @SuppressWarnings("unchecked")
            var partialChildren = (List<Map<String, Object>>) value;

            // If it has a child, check if Map
            if (!partialChildren.isEmpty() && !(partialChildren.get(0) instanceof Map)) {
                continue;
            }

            // Assume they are children
            partialChildren.stream()
                    .filter(map -> map.containsKey("type"))
                    .map(EsprimaNode::new)
                    .forEach(children::add);
        }

        return children;
    }

    /**
     * Retrieves all descendants of this node.
     * 
     * @return a list of descendant nodes
     */
    public List<EsprimaNode> getDescendants() {
        return getDescendantsStream().collect(Collectors.toList());
    }

    /**
     * Retrieves a stream of immediate children of this node.
     * 
     * @return a stream of child nodes
     */
    public Stream<EsprimaNode> getChildrenStream() {
        return getChildren().stream();
    }

    /**
     * Retrieves a stream of all descendants of this node.
     * 
     * @return a stream of descendant nodes
     */
    public Stream<EsprimaNode> getDescendantsStream() {
        return getChildrenStream()
                .flatMap(c -> c.getDescendantsAndSelfStream());
    }

    /**
     * Retrieves a stream of this node and all its descendants.
     * 
     * @return a stream of this node and its descendants
     */
    public Stream<EsprimaNode> getDescendantsAndSelfStream() {
        return Stream.concat(Stream.of(this), getDescendantsStream());
    }

    /**
     * Retrieves the value associated with the given key.
     * 
     * @param key the key to look up
     * @return the value associated with the key
     */
    public Object get(String key) {
        return node.get(key);
    }

    /**
     * Retrieves the type of this node.
     * 
     * @return the type of the node
     * @throws RuntimeException if the node does not have a type
     */
    public String getType() {
        if (!node.containsKey("type")) {
            throw new RuntimeException("Node does not have type: " + node);
        }

        return (String) node.get("type");
    }

    /**
     * Checks if this node has comments.
     * 
     * @return true if the node has comments, false otherwise
     */
    public boolean hasComment() {
        return node.containsKey("comments");
    }

    /**
     * Retrieves the comments associated with this node.
     * 
     * @return a list of comments
     */
    @SuppressWarnings("unchecked")
    public List<EsprimaComment> getComments() {
        return getAsList("comments", Map.class).stream()
                .map(map -> new EsprimaComment(map))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves the value associated with the given key as a list of the specified type.
     * 
     * @param key the key to look up
     * @param elementType the class of the elements in the list
     * @param <T> the type of the elements in the list
     * @return a list of elements of the specified type
     * @throws RuntimeException if the value is not a list
     */
    public <T> List<T> getAsList(String key, Class<T> elementType) {
        var value = node.get(key);

        if (value == null) {
            return Collections.emptyList();
        }

        if (!(value instanceof List)) {
            throw new RuntimeException("Value is not a list: " + value);
        }

        @SuppressWarnings("unchecked")
        var list = (List<Object>) value;

        return list.stream().map(elementType::cast).collect(Collectors.toList());
    }

    /**
     * Retrieves the value associated with the given key as an EsprimaNode.
     * 
     * @param key the key to look up
     * @return the EsprimaNode associated with the key
     */
    public EsprimaNode getAsNode(String key) {
        var value = getExistingValue(key, Map.class);
        @SuppressWarnings("unchecked")
        var node = new EsprimaNode(value);
        return node;
    }

    /**
     * Retrieves the value associated with the given key as a list of EsprimaNodes.
     * 
     * @param key the key to look up
     * @return a list of EsprimaNodes associated with the key
     */
    @SuppressWarnings("unchecked")
    public List<EsprimaNode> getAsNodes(String key) {
        var values = getExistingValue(key, List.class);

        List<EsprimaNode> nodes = new ArrayList<>();

        values.stream()
                .forEach(value -> nodes.add(new EsprimaNode((Map<String, Object>) value)));

        return nodes;
    }

    /**
     * Retrieves the value associated with the given key, ensuring it exists and is of the specified type.
     * 
     * @param key the key to look up
     * @param valueClass the class of the value
     * @param <T> the type of the value
     * @return the value associated with the key
     * @throws RuntimeException if the value does not exist or is not of the specified type
     */
    private <T> T getExistingValue(String key, Class<T> valueClass) {
        var value = node.get(key);
        Objects.requireNonNull(value, () -> "Expected value with key '" + key + "' to exist");
        return valueClass.cast(value);
    }

    /**
     * Returns a string representation of this node.
     * 
     * @return a string representation of the node
     */
    @Override
    public String toString() {
        return node.toString();
    }

    /**
     * Retrieves the location information of this node.
     * 
     * @return the location information
     * @throws RuntimeException if the location information is null
     */
    public EsprimaLoc getLoc() {
        @SuppressWarnings("unchecked")
        var loc = (Map<String, Object>) node.get("loc");
        Objects.requireNonNull(loc, () -> "Loc is null");
        return EsprimaLoc.newInstance(loc);
    }

    /**
     * Sets the comment for this node.
     * 
     * @param comment the comment to set
     */
    public void setComment(EsprimaComment comment) {
        node.put("comments", comment);
    }

    /**
     * Retrieves the comment associated with this node.
     * 
     * @return the comment associated with the node, or an empty comment if none exists
     */
    public EsprimaComment getComment() {
        if (!node.containsKey("comments")) {
            return EsprimaComment.empty();
        }

        return (EsprimaComment) node.get("comments");
    }

    /**
     * Retrieves the keys of this node.
     * 
     * @return a set of keys
     */
    public Set<String> getKeys() {
        return node.keySet();
    }

    /**
     * Retrieves the value associated with the given key as a string.
     * 
     * @param key the key to look up
     * @return the value associated with the key as a string
     */
    public String getAsString(String key) {
        return getExistingValue(key, String.class);
    }

    /**
     * Retrieves the underlying map of this node.
     * 
     * @return the map representing the node
     */
    public Map<String, Object> getNode() {
        return node;
    }

    /**
     * Retrieves the value associated with the given key as a boolean.
     * 
     * @param key the key to look up
     * @return the value associated with the key as a boolean
     */
    public boolean getAsBool(String key) {
        return getExistingValue(key, Boolean.class);
    }

    /**
     * Checks if this node has a value for the given key.
     * 
     * @param key the key to check
     * @return true if the node has a value for the key, false otherwise
     */
    public boolean hasValueFor(String key) {
        return node.get(key) != null;
    }
}
