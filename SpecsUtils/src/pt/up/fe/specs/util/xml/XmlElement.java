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

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class XmlElement extends AXmlNode {

    private final Element element;

    public XmlElement(Element element) {
        if (element == null) {
            throw new NullPointerException("XmlElement requires a non-null Element");
        }
        this.element = element;
    }

    @Override
    public Node getNode() {
        return element;
    }

    public String getName() {
        return element.getNodeName();
    }

    /**
     * 
     * @param name
     * @return the value of the attribute with the given name, or empty string if no
     *         attribute with that name is present
     */
    public String getAttribute(String name) {
        if (name == null) {
            return "";
        }
        return element.getAttribute(name);
    }

    public String getAttribute(String name, String defaultValue) {
        var value = getAttribute(name);
        return value.isEmpty() ? defaultValue : value;
    }

    /**
     * 
     * @param name
     * @return the value of the attribute with the given name, or throws exception
     *         if no attribute with that name is present
     */
    public String getAttributeStrict(String name) {
        var result = getAttribute(name);

        if (result.isEmpty()) {
            throw new RuntimeException("Could not find mandatory attribute '" + name + "' in element " + getName());
        }

        return result;
    }

    /**
     * 
     * @param name
     * @param value
     * @return the previous value set to the given name, of null if no value was set
     *         for that name
     */
    public String setAttribute(String name, String value) {
        if (name == null) {
            throw new NullPointerException("Attribute name cannot be null");
        }
        var previousValue = getAttribute(name);
        if (value == null) {
            element.removeAttribute(name);
        } else {
            element.setAttribute(name, value);
        }
        return previousValue;
    }

    public List<String> getAttributes() {
        var attributes = element.getAttributes();
        var attributesNames = new ArrayList<String>(attributes.getLength());

        for (int i = 0; i < attributes.getLength(); i++) {
            var item = attributes.item(i);

            if (!(item instanceof Attr)) {
                continue;
            }

            attributesNames.add(((Attr) item).getName());
        }

        return attributesNames;
    }

}
