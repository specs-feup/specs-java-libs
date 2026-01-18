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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.xml;

import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Can represent the root of the XML document, or an XML element.
 * 
 * @author JoaoBispo
 *
 */
public interface XmlNode {

    /**
     * 
     * @return the underlying XML node that we are wrapping
     */
    public Node getNode();

    /**
     * 
     * @return the parent of this node
     */
    default public XmlNode getParent() {
        var node = getNode();
        if (node == null) {
            return null;
        }
        var parent = node.getParentNode();
        return XmlNodes.create(parent);
    }

    /**
     * 
     * @return all the elements that a direct children of this node.
     */
    default public List<XmlNode> getChildren() {
        var node = getNode();
        if (node == null) {
            return List.of();
        }
        return XmlNodes.toList(node.getChildNodes());
    }

    /**
     * 
     * @return all the elements that are under this node.
     */
    default public List<XmlNode> getDescendants() {
        return XmlNodes.getDescendants(this);
    }

    /**
     * 
     * @return all the elements that have the given name
     */
    default public List<XmlElement> getElementsByName(String name) {
        return getDescendants().stream()
                .filter(node -> node instanceof XmlElement)
                .map(XmlElement.class::cast)
                .filter(element -> element.getName().equals(name))
                .collect(Collectors.toList());
    }

    /**
     * 
     * @return the element that has the given name, null if no element is found, and
     *         exception if more than one element with that name is found
     */
    default public XmlElement getElementByName(String name) {
        var elements = getElementsByName(name);

        if (elements.isEmpty()) {
            return null;
        }

        if (elements.size() > 1) {
            throw new RuntimeException("More than one element with name '" + name + "'");
        }

        return elements.get(0);
    }

    /**
     * 
     * @return the text set for this node, or null if no text is set
     */
    default public String getText() {
        var node = getNode();
        if (node == null) {
            return null;
        }

        // For leaf-like nodes, return their direct value (can be empty string).
        switch (node.getNodeType()) {
            case org.w3c.dom.Node.TEXT_NODE:
            case org.w3c.dom.Node.CDATA_SECTION_NODE:
            case org.w3c.dom.Node.COMMENT_NODE:
            case org.w3c.dom.Node.ATTRIBUTE_NODE:
            case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                return node.getNodeValue();
            case org.w3c.dom.Node.DOCUMENT_NODE:
                // For documents, return the text content from the document element
                org.w3c.dom.Document doc = (org.w3c.dom.Document) node;
                org.w3c.dom.Element docElement = doc.getDocumentElement();
                return docElement != null ? docElement.getTextContent() : null;
            default:
                // For element-like nodes, return null only if there are no text/CDATA nodes in
                // the subtree
                if (!hasTextOrCdata(node)) {
                    return null;
                }
                return node.getTextContent();
        }
    }

    // Helper to detect if a node subtree has any Text or CDATA nodes
    private static boolean hasTextOrCdata(org.w3c.dom.Node node) {
        if (node == null) {
            return false;
        }
        short type = node.getNodeType();
        if (type == org.w3c.dom.Node.TEXT_NODE || type == org.w3c.dom.Node.CDATA_SECTION_NODE) {
            return true;
        }

        var children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (hasTextOrCdata(children.item(i))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 
     * @return the previous text that was set, or null if no text was set
     */
    default public String setText(String text) {
        var previousText = getText();
        var node = getNode();
        if (node == null) {
            return previousText;
        }

        if (text == null) {
            // Remove direct text and CDATA children to represent a null text value
            var children = node.getChildNodes();
            // Collect nodes to remove to avoid concurrent modification issues
            java.util.List<org.w3c.dom.Node> toRemove = new java.util.ArrayList<>();
            for (int i = 0; i < children.getLength(); i++) {
                var child = children.item(i);
                if (child != null && (child.getNodeType() == org.w3c.dom.Node.TEXT_NODE
                        || child.getNodeType() == org.w3c.dom.Node.CDATA_SECTION_NODE)) {
                    toRemove.add(child);
                }
            }
            for (var child : toRemove) {
                node.removeChild(child);
            }
            // Do not call setTextContent(null), as some DOM implementations convert it to
            // empty string
        } else {
            // If empty string, ensure there is an explicit (empty) text node
            if (text.isEmpty()) {
                // First remove existing direct text/CDATA children to avoid duplicates
                var children = node.getChildNodes();
                java.util.List<org.w3c.dom.Node> toRemove = new java.util.ArrayList<>();
                for (int i = 0; i < children.getLength(); i++) {
                    var child = children.item(i);
                    if (child != null && (child.getNodeType() == org.w3c.dom.Node.TEXT_NODE
                            || child.getNodeType() == org.w3c.dom.Node.CDATA_SECTION_NODE)) {
                        toRemove.add(child);
                    }
                }
                for (var child : toRemove) {
                    node.removeChild(child);
                }
                var owner = node.getOwnerDocument();
                if (owner != null) {
                    node.appendChild(owner.createTextNode(""));
                } else {
                    // If node is a Document or has no owner, fallback to setTextContent
                    node.setTextContent("");
                }
            } else {
                node.setTextContent(text);
            }
        }

        return previousText;
    }

    default void write(StreamResult result) {
        try {
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(getNode());
            transformer.transform(source, result);

        } catch (Exception e) {
            throw new RuntimeException("Could not write XML  from document", e);
        }
    }

    default void write(File outputFile) {
        // Make sure folder exists
        SpecsIo.mkdir(outputFile.getParent());
        SpecsLogs.debug(() -> "Writing XML document " + outputFile);
        StreamResult result = new StreamResult(outputFile);
        // Handle permission and IO errors gracefully at the file level
        try {
            write(result);
        } catch (RuntimeException e) {
            // Log and do not rethrow to keep behavior graceful when writing to files
            SpecsLogs.warn("Could not write XML to file '" + outputFile + "': " + e.getMessage());
        }
    }

    default public String getString() {
        var stringWriter = new StringWriter();

        StreamResult result = new StreamResult(stringWriter);
        write(result);

        return stringWriter.toString();
    }
}
