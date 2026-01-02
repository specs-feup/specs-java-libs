/**
 * Copyright 2013 SPeCS Research Group.
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

package pt.up.fe.specs.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Utility methods related with XML files.
 *
 * @author Joao Bispo
 *
 */
public class SpecsXml {

    public static NodeList getNodeList(File file) {
        return getXmlRoot(file).getChildNodes();
    }

    public static Document getXmlRoot(File file) {
        InputStream fileInputStream = SpecsIo.toInputStream(file);
        Document xmlDoc = getXmlRoot(fileInputStream);
        try {
            fileInputStream.close();
        } catch (IOException e) {
            SpecsLogs.warn("Could not close file input stream:\n", e);
        }
        return xmlDoc;
    }

    public static Document getXmlRoot(String contents) {
        return getXmlRoot(SpecsIo.toInputStream(contents));
    }

    public static Document getXmlRoot(InputStream xmlDocument) {
        return getXmlRoot(xmlDocument, null);
    }

    /**
     * Parses an XML document from an InputSource, optionally validating against a schema.
     * Supports InputSource from String, InputStream, or Reader.
     */
    public static Document getXmlRoot(InputSource in, InputStream schemaDocument) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(in);

            // If schema present, validate document
            if (schemaDocument != null) {
                var schemaFactory = SchemaFactory.newDefaultInstance();
                var schema = schemaFactory.newSchema(new StreamSource(schemaDocument));
                var validator = schema.newValidator();
                validator.validate(new DOMSource(doc));
            }

            // Normalize the document (recommended)
            doc.getDocumentElement().normalize();

            return doc;
        } catch (SAXParseException e) {
            throw new RuntimeException("XML document not according to schema", e);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            SpecsLogs.warn("Error message:\n", e);
        }

        return null;
    }

    /**
     * Parses an XML document from a String, optionally validating against a schema.
     */
    public static Document getXmlRoot(String xmlContents, InputStream schemaDocument) {
        return getXmlRoot(new InputSource(SpecsIo.toInputStream(xmlContents)), schemaDocument);
    }

    /**
     * Parses an XML document from an InputStream, optionally validating against a schema.
     */
    public static Document getXmlRoot(InputStream xmlDocument, InputStream schemaDocument) {
        return getXmlRoot(new InputSource(xmlDocument), schemaDocument);
    }

    /**
     * Parses an XML document from a file path (URI), optionally validating against a schema.
     */
    public static Document getXmlRootFromUri(String uri, InputStream schemaDocument) {
        return getXmlRoot(new InputSource(uri), schemaDocument);
    }

    public static Document getXmlRootFromUri(String uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI cannot be null");
        }
        return getXmlRoot(new InputSource(uri), null);
    }

    /**
     * Returns the value of the attribute inside the given tag.
     *
     */
    public static String getAttribute(Document doc, String tag, String attribute) {
        NodeList nList = doc.getElementsByTagName(tag);

        return getAttribute(nList, tag, attribute);
    }

    public static String getAttribute(Element element, String tag, String attribute) {
        NodeList nList = element.getElementsByTagName(tag);

        return getAttribute(nList, tag, attribute);
    }

    private static String getAttribute(NodeList nList, String tag, String attribute) {
        if (nList.getLength() == 0) {
            SpecsLogs.msgInfo("Could not find section '" + tag + "'");
            return null;
        }

        if (nList.getLength() > 1) {
            SpecsLogs.msgInfo("Found more than one '" + tag
                    + "' section, returning the attribute of the first occcurence");
        }

        Node nNode = nList.item(0);

        if (nNode.getNodeType() != Node.ELEMENT_NODE) {
            System.out.println("Node '" + tag + "' is not an element");
            return null;
        }

        Element eElement = (Element) nNode;

        return eElement.getAttribute(attribute);

    }

    public static Element getElement(Element element, String tag) {
        NodeList nList = element.getElementsByTagName(tag);

        if (nList.getLength() == 0) {
            SpecsLogs.msgInfo("Could not find element with name '" + tag + "'");
            return null;
        }

        if (nList.getLength() > 1) {
            SpecsLogs.msgInfo("Found more than one '" + tag
                    + "' element, returning the attribute of the first occcurence");
        }

        Node nNode = nList.item(0);

        if (nNode.getNodeType() != Node.ELEMENT_NODE) {
            System.out.println("Node '" + tag + "' is not an element");
            return null;
        }

        return (Element) nNode;
    }

    public static String getElementText(Element element, String tag) {
        Element sectionElement = getElement(element, tag);

        return sectionElement.getTextContent();
    }

    public static Integer getAttributeInt(Document doc, String section, String attribute) {
        String integerValue = getAttribute(doc, section, attribute);

        return SpecsStrings.parseInteger(integerValue);
    }

    public static Node getNode(NodeList nodes, String tag) {
        return getNodeMaybe(nodes, tag)
                .orElseThrow(
                        () -> new RuntimeException("Could not find a node with tag '" + tag + "' in nodes:\n" + nodes));
    }

    public static Optional<Node> getNodeMaybe(NodeList nodes, String tag) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node currentNode = nodes.item(i);
            if (currentNode.getNodeName().equals(tag)) {
                return Optional.of(currentNode);
            }
        }

        return Optional.empty();
    }

    public static Optional<String> getAttribute(Node node, String attrName) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            return Optional.of(((Element) node).getAttribute(attrName));
        }

        SpecsLogs.warn("Given node that is not an element (" + node + "). Check if this works");

        Node attribute = node.getAttributes().getNamedItem(attrName);
        if (attribute == null) {
            return Optional.empty();
            // throw new RuntimeException("No attribute with name '"+attrName+"' in node
            // '"+node+"');
        }

        return Optional.of((attribute.getNodeValue()));

    }

    public static List<Node> getNodes(Node node, String tag) {
        List<Node> children = new ArrayList<>();

        NodeList childrenList = node.getChildNodes();
        for (int i = 0; i < childrenList.getLength(); i++) {
            Node child = childrenList.item(i);
            if (child.getNodeName().equals(tag)) {
                children.add(child);
            }
        }

        return children;
    }

    public static String getText(NodeList nodes, String... tagChain) {
        NodeList currentNodes = nodes;

        for (int i = 0; i < tagChain.length; i++) {
            // Get node of current tag
            Node node = getNode(currentNodes, tagChain[i]);

            // If last node in the chain, return node value.

            if (i == tagChain.length - 1) {
                return node.getTextContent();
            }

            // Otherwise, update current nodes
            currentNodes = node.getChildNodes();
        }

        throw new RuntimeException("Should not arrive here.");
    }

    public static List<Element> getElementChildren(Element element) {
        return getElementChildren(element, "*");
    }

    public static List<Element> getElementChildren(Element element, String tag) {

        NodeList entries = element.getChildNodes();
        List<Element> children = new ArrayList<>();
        for (int i = 0; i < entries.getLength(); i++) {
            Node currentNode = entries.item(i);
            if (!(currentNode instanceof Element childElement)) {
                continue;
            }

            if (tag.equals("*") || tag.equals(childElement.getTagName())) {
                children.add(childElement);
            }
        }

        return children;
    }

    public static List<Element> getElements(Element element) {
        return getElements(element, "*");
    }

    public static List<Element> getElements(Element element, String tag) {
        NodeList entries = element.getElementsByTagName(tag);
        List<Element> children = new ArrayList<>();
        for (int i = 0; i < entries.getLength(); i++) {
            Node currentNode = entries.item(i);
            if (currentNode instanceof Element) {
                children.add((Element) currentNode);
            }

        }

        return children;
    }

    public static void toTree(Node rootNode, String spacer) {
        System.out.println(spacer + rootNode.getNodeName() + " -> " + rootNode.getNodeValue());
        NodeList nl = rootNode.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++)
            toTree(nl.item(i), spacer + "   ");
    }

}
