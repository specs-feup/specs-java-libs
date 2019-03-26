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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(file);

            // optional, but recommended
            // read this -
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            return doc;
        } catch (ParserConfigurationException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        } catch (SAXException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        } catch (IOException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        }

        return null;
    }

    public static Document getXmlRoot(String uri) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(uri);

            // optional, but recommended
            // read this -
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            return doc;
        } catch (ParserConfigurationException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        } catch (SAXException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        } catch (IOException e) {
            SpecsLogs.msgWarn("Error message:\n", e);
        }

        return null;
    }

    /**
     * Returns the value of the attribute inside the given session.
     * 
     * @param doc
     * @param section
     * @param attribute
     * @return
     */
    public static String getAttribute(Document doc, String section, String attribute) {
        NodeList nList = doc.getElementsByTagName(section);

        return getAttribute(nList, section, attribute);
    }

    public static String getAttribute(Element element, String section, String attribute) {
        NodeList nList = element.getElementsByTagName(section);

        return getAttribute(nList, section, attribute);
    }

    private static String getAttribute(NodeList nList, String section, String attribute) {
        // NodeList nList = doc.getElementsByTagName(section);
        /*
        	if (nList == null) {
        	    LoggingUtils.msgInfo("Could not find section '" + section + "'");
        	    return null;
        	}
         */
        if (nList.getLength() == 0) {
            SpecsLogs.msgInfo("Could not find section '" + section + "'");
            return null;
        }

        if (nList.getLength() > 1) {
            SpecsLogs.msgInfo("Found more than one '" + section
                    + "' section, returning the attribute of the first occcurence");
        }

        Node nNode = nList.item(0);

        if (nNode.getNodeType() != Node.ELEMENT_NODE) {
            System.out.println("Node '" + section + "' is not an element");
            return null;
        }

        Element eElement = (Element) nNode;

        return eElement.getAttribute(attribute);

    }

    public static Element getSection(Element element, String section) {
        NodeList nList = element.getElementsByTagName(section);

        if (nList.getLength() == 0) {
            SpecsLogs.msgInfo("Could not find section '" + section + "'");
            return null;
        }

        if (nList.getLength() > 1) {
            SpecsLogs.msgInfo("Found more than one '" + section
                    + "' section, returning the attribute of the first occcurence");
        }

        Node nNode = nList.item(0);

        if (nNode.getNodeType() != Node.ELEMENT_NODE) {
            System.out.println("Node '" + section + "' is not an element");
            return null;
        }

        Element eElement = (Element) nNode;

        return eElement;
    }

    public static String getSectionValue(Element element, String section) {
        Element sectionElement = getSection(element, section);

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

    /*
    public static List<Node> getNodes(NodeList nodeList, String nodeTag) {
    
    List<Node> nodes = new ArrayList<>();
    for (int i = 0; i < nodeList.getLength(); i++) {
        nodes.add(nodeList.item(i));
    }
    
    return nodes;
    }
     */

    public static Optional<String> getAttribute(Node node, String attrName) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            return Optional.of(((Element) node).getAttribute(attrName));
        }

        SpecsLogs.msgWarn("Given node that is not an element (" + node + "). Check if this works");

        Node attribute = node.getAttributes().getNamedItem(attrName);
        if (attribute == null) {
            return Optional.empty();
            // throw new RuntimeException("No attribute with name '"+attrName+"' in node '"+node+"');
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

    /*
    public static String getValue(Document doc, String... tagChain) {
    return getValue(doc.getChildNodes(), tagChain);
    }
     */

    /**
     * The value of the node found by walking the given tag-chain.
     * 
     * @param doc
     * @param tagChain
     * @return
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

    public static List<Element> getElementChildren(Element element) {
        return getElementChildren(element, "*");
    }

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

            // if (!currentNode.getNodeName().equals(tag)) {
            // continue;
            // }
            //
            // children.add(((Element) currentNode).);
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

}
