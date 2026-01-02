/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.util.xml;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pt.up.fe.specs.util.classmap.FunctionClassMap;

public class XmlNodes {

    private final static FunctionClassMap<Node, XmlNode> XML_NODE_MAPPER;
    static {
        XML_NODE_MAPPER = new FunctionClassMap<>(XmlGenericNode::new);
        XML_NODE_MAPPER.put(Document.class, XmlDocument::new);
        XML_NODE_MAPPER.put(Element.class, XmlElement::new);
    }

    public static XmlNode create(Node node) {
        if (node == null) {
            return null;
        }
        return XML_NODE_MAPPER.apply(node);
    }

    public static List<XmlNode> toList(NodeList nodeList) {
        if (nodeList == null) {
            return new ArrayList<>();
        }
        var children = new ArrayList<XmlNode>(nodeList.getLength());

        for (int i = 0; i < nodeList.getLength(); i++) {
            children.add(XmlNodes.create(nodeList.item(i)));
        }

        return children;
    }

    public static List<XmlNode> getDescendants(XmlNode node) {
        if (node == null) {
            return new ArrayList<>();
        }
        List<XmlNode> descendants = new ArrayList<>();

        getDescendants(node, descendants);

        return descendants;
    }

    private static void getDescendants(XmlNode node, List<XmlNode> descendants) {
        for (var child : node.getChildren()) {
            descendants.add(child);
            getDescendants(child, descendants);
        }
    }
}
