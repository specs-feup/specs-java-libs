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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.jsengine.libs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import pt.up.fe.specs.util.SpecsCheck;

public class EsprimaNode {

    private static final Set<String> IGNORE_KEYS = new HashSet<>(Arrays.asList("type", "loc", "comments"));

    private final Map<String, Object> node;

    public EsprimaNode(Map<String, Object> node) {
        this.node = node;
    }

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

    public List<EsprimaNode> getDescendants() {
        return getDescendantsStream().collect(Collectors.toList());
    }

    public Stream<EsprimaNode> getChildrenStream() {
        return getChildren().stream();
    }

    public Stream<EsprimaNode> getDescendantsStream() {
        return getChildrenStream()
                .flatMap(c -> c.getDescendantsAndSelfStream());
    }

    public Stream<EsprimaNode> getDescendantsAndSelfStream() {
        return Stream.concat(Stream.of(this), getDescendantsStream());
    }

    public Object get(String key) {
        return node.get(key);
    }

    public String getType() {
        if (!node.containsKey("type")) {
            throw new RuntimeException("Node does not have type: " + node);
        }

        return (String) node.get("type");
    }

    public boolean hasComment() {
        return node.containsKey("comments");
    }

    @SuppressWarnings("unchecked")
    public List<EsprimaComment> getComments() {
        return getAsList("comments", Map.class).stream()
                .map(map -> new EsprimaComment(map))
                .collect(Collectors.toList());
    }

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

    public EsprimaNode getAsNode(String key) {
        var value = getExistingValue(key, Map.class);
        @SuppressWarnings("unchecked")
        var node = new EsprimaNode(value);
        return node;
    }

    @SuppressWarnings("unchecked")
    public List<EsprimaNode> getAsNodes(String key) {
        var values = getExistingValue(key, List.class);

        List<EsprimaNode> nodes = new ArrayList<>();

        values.stream()
                .forEach(value -> nodes.add(new EsprimaNode((Map<String, Object>) value)));

        // var a = values.stream()
        // .map(value -> new EsprimaNode((Map<String, Object>) value))
        // .collect(Collectors.toList());

        return nodes;

        // return values.stream()
        // .map(value -> (Map<String, Object>) value)
        // .map(value -> new EsprimaNode(value))
        // .collect(Collectors.toList());
        // @SuppressWarnings("unchecked")
        // var node = new EsprimaNode(value);
        // return node;
    }

    private <T> T getExistingValue(String key, Class<T> valueClass) {
        var value = node.get(key);
        SpecsCheck.checkNotNull(value, () -> "Expected value with key '" + key + "' to exist");
        return valueClass.cast(value);
    }

    @Override
    public String toString() {
        return node.toString();
    }

    public EsprimaLoc getLoc() {
        @SuppressWarnings("unchecked")
        var loc = (Map<String, Object>) node.get("loc");
        SpecsCheck.checkNotNull(loc, () -> "Loc is null");
        return EsprimaLoc.newInstance(loc);
    }

    public void setComment(EsprimaComment comment) {
        node.put("comments", comment);
    }

    public EsprimaComment getComment() {
        if (!node.containsKey("comments")) {
            return EsprimaComment.empty();
        }

        return (EsprimaComment) node.get("comments");
    }

    public Set<String> getKeys() {
        return node.keySet();
    }

    public String getAsString(String key) {
        return getExistingValue(key, String.class);
    }

    public Map<String, Object> getNode() {
        return node;
    }

    public boolean getAsBool(String key) {
        return getExistingValue(key, Boolean.class);
    }

    public boolean hasValueFor(String key) {
        return node.get(key) != null;
    }
}
