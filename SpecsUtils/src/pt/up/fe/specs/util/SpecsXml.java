/*
 * SpecsXml.java
 *
 * Utility class for XML file operations, including parsing, validation, and DOM manipulation. Provides static helper methods for reading, validating, and extracting data from XML files in the SPeCS ecosystem.
 *
 * Copyright 2025 SPeCS Research Group.
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
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Utility methods related with XML files.
 * <p>
 * Provides static helper methods for parsing, validating, and extracting data from XML files and strings, using DOM and other Java XML APIs.
 * </p>
 *
 * @author Joao Bispo
 */
public class SpecsXml {

    /**
     * Retrieves the list of child nodes from the root of the XML document represented by the given file.
     *
     * @param file the XML file
     * @return a NodeList containing the child nodes of the root element
     */
    public static NodeList getNodeList(File file) {
        return getXmlRoot(file).getChildNodes();
    }

    /**
     * Parses the XML document from the given file and returns its root element as a Document object.
     *
     * @param file the XML file
     * @return the root element of the XML document
     */
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

    /**
     * Parses the XML document from the given string and returns its root element as a Document object.
     *
     * @param contents the XML content as a string
     * @return the root element of the XML document
     */
    public static Document getXmlRoot(String contents) {
        return getXmlRoot(SpecsIo.toInputStream(contents));
    }

    /**
     * Parses the XML document from the given InputStream and returns its root element as a Document object.
     *
     * @param xmlDocument the InputStream containing the XML document
     * @return the root element of the XML document
     */
    public static Document getXmlRoot(InputStream xmlDocument) {
        return getXmlRoot(xmlDocument, null);
    }

    /**
     * Parses and optionally validates the XML document from the given InputStream and returns its root element as a Document object.
     *
     * @param xmlDocument the InputStream containing the XML document
     * @param schemaDocument the InputStream containing the XML schema for validation (optional)
     * @return the root element of the XML document
     */
    public static Document getXmlRoot(InputStream xmlDocument, InputStream schemaDocument) {

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlDocument);

            // If schema present, validate document
            if (schemaDocument != null) {
                var schemaFactory = SchemaFactory.newDefaultInstance();
                var schema = schemaFactory.newSchema(new StreamSource(schemaDocument));
                var validator = schema.newValidator();
                validator.validate(new DOMSource(doc));
            }

            // Normalize the document
            doc.getDocumentElement().normalize();

            return doc;
        } catch (SAXParseException e) {
            throw new RuntimeException("XML document not according to schema", e);
        } catch (ParserConfigurationException e) {
            SpecsLogs.warn("Error message:\n", e);
        } catch (SAXException e) {
            SpecsLogs.warn("Error message:\n", e);
        } catch (IOException e) {
            SpecsLogs.warn("Error message:\n", e);
        }

        return null;
    }

    /**
     * Parses the XML document from the given URI and returns its root element as a Document object.
     *
     * @param uri the URI of the XML document
     * @return the root element of the XML document
     */
    public static Document getXmlRootFromUri(String uri) {
        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(uri);

            // Normalize the document
            doc.getDocumentElement().normalize();

            return doc;
        } catch (ParserConfigurationException e) {
            SpecsLogs.warn("Error message:\n", e);
        } catch (SAXException e) {
            SpecsLogs.warn("Error message:\n", e);
        } catch (IOException e) {
            SpecsLogs.warn("Error message:\n", e);
        }

        return null;
    }

    /**
     * Returns the value of the attribute inside the given tag.
     *
     * @param doc the XML document
     * @param tag the tag name
     * @param attribute the attribute name
     * @return the value of the attribute, or null if not found
     */
    public static String getAttribute(Document doc, String tag, String attribute) {
        NodeList nList = doc.getElementsByTagName(tag);

        return getAttribute(nList, tag, attribute);
    }

    /**
     * Returns the value of the attribute inside the given tag within the specified element.
     *
     * @param element the parent element
     * @param tag the tag name
     * @param attribute the attribute name
     * @return the value of the attribute, or null if not found
     */
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

    /**
     * Retrieves the first child element with the specified tag name from the given parent element.
     *
     * @param element the parent element
     * @param tag the tag name
     * @return the first matching child element, or null if not found
     */
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

        Element eElement = (Element) nNode;

        return eElement;
    }

    /**
     * Retrieves the text content of the first child element with the specified tag name from the given parent element.
     *
     * @param element the parent element
     * @param tag the tag name
     * @return the text content of the matching child element
     */
    public static String getElementText(Element element, String tag) {
        Element sectionElement = getElement(element, tag);

        return sectionElement.getTextContent();
    }

    /**
     * Retrieves the integer value of the specified attribute from the given tag in the XML document.
     *
     * @param doc the XML document
     * @param section the tag name
     * @param attribute the attribute name
     * @return the integer value of the attribute, or null if not found
     */
    public static Integer getAttributeInt(Document doc, String section, String attribute) {
        String integerValue = getAttribute(doc, section, attribute);

        return SpecsStrings.parseInteger(integerValue);
    }

    /**
     * Retrieves the first node with the specified tag name from the given NodeList.
     *
     * @param nodes the NodeList to search
     * @param tag the tag name
     * @return the matching node
     * @throws RuntimeException if no matching node is found
     */
    public static Node getNode(NodeList nodes, String tag) {
        return getNodeMaybe(nodes, tag)
                .orElseThrow(
                        () -> new RuntimeException("Could not find a node with tag '" + tag + "' in nodes:\n" + nodes));
    }

    /**
     * Retrieves the first node with the specified tag name from the given NodeList, if it exists.
     *
     * @param nodes the NodeList to search
     * @param tag the tag name
     * @return an Optional containing the matching node, or empty if not found
     */
    public static Optional<Node> getNodeMaybe(NodeList nodes, String tag) {
        for (int i = 0; i < nodes.getLength(); i++) {
            Node currentNode = nodes.item(i);
            if (currentNode.getNodeName().equals(tag)) {
                return Optional.of(currentNode);
            }
        }

        return Optional.empty();
    }

    /**
     * Retrieves the list of child nodes with the specified tag name from the given parent node.
     *
     * @param node the parent node
     * @param tag the tag name
     * @return a list of matching child nodes
     */
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

    /**
     * Retrieves the text content of the node found by walking the given tag-chain.
     *
     * @param nodes the NodeList to start the search
     * @param tagChain the sequence of tag names to follow
     * @return the text content of the matching node
     */
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

    /**
     * Retrieves all child elements of the given parent element.
     *
     * @param element the parent element
     * @return a list of child elements
     */
    public static List<Element> getElementChildren(Element element) {
        return getElementChildren(element, "*");
    }

    /**
     * Retrieves all child elements with the specified tag name from the given parent element.
     *
     * @param element the parent element
     * @param tag the tag name
     * @return a list of matching child elements
     */
    public static List<Element> getElementChildren(Element element, String tag) {

        NodeList entries = element.getChildNodes();
        List<Element> children = new ArrayList<>();
        for (int i = 0; i < entries.getLength(); i++) {
            Node currentNode = entries.item(i);
            if (!(currentNode instanceof Element)) {
                continue;
            }

            Element childElement = (Element) currentNode;

            if (tag.equals("*") || tag.equals(childElement.getTagName())) {
                children.add(childElement);
            }
        }

        return children;
    }

    /**
     * Retrieves all elements with the specified tag name from the given parent element.
     *
     * @param element the parent element
     * @return a list of matching elements
     */
    public static List<Element> getElements(Element element) {
        return getElements(element, "*");
    }

    /**
     * Retrieves all elements with the specified tag name from the given parent element.
     *
     * @param element the parent element
     * @param tag the tag name
     * @return a list of matching elements
     */
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

    /**
     * Prints the tree structure of the given root node to the console.
     *
     * @param rootNode the root node
     * @param spacer the spacer string for indentation
     */
    public static void toTree(Node rootNode, String spacer) {
        System.out.println(spacer + rootNode.getNodeName() + " -> " + rootNode.getNodeValue());
        NodeList nl = rootNode.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++)
            toTree(nl.item(i), spacer + "   ");
    }

}
