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

import java.io.File;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

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
        return XmlNodes.create(getNode().getParentNode());
    }

    /**
     * 
     * @return all the elements that a direct children of this node.
     */
    default public List<XmlNode> getChildren() {
        return XmlNodes.toList(getNode().getChildNodes());
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
     * @param tag
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
     * @return the text set for this node, or null if no text is set
     */
    default public String getText() {
        return getNode().getTextContent();
    }

    /**
     * 
     * @param text
     * @return the previous text that was set, or null if no text was set
     */
    default public String setText(String text) {
        var previousText = getText();
        getNode().setTextContent(text);
        return previousText;
    }

    default void write(StreamResult result) {
        try {
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(getNode());
            // System.out.println("CHILD NODE : " + document.getChildNodes().item(0).getChildNodes().getLength());
            transformer.transform(source, result);

        } catch (Exception e) {
            throw new RuntimeException("Could not write XML  from document", e);
        }
    }

    default void write(File outputFile) {
        SpecsLogs.debug(() -> "Writing XML document " + outputFile);
        StreamResult result = new StreamResult(outputFile);
        write(result);
    }

    default public String getString() {
        var stringWriter = new StringWriter();

        StreamResult result = new StreamResult(stringWriter);
        write(result);
        // stringWriter.flush();
        // try {
        // stringWriter.close();
        // } catch (IOException e) {
        // throw new RuntimeException("Could not ", e);
        // }
        return stringWriter.toString();
    }
}
