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
import java.io.InputStream;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import pt.up.fe.specs.util.SpecsXml;

public class XmlDocument extends AXmlNode {
    /**
     * The document this element is part of.
     */
    private final Document document;

    public XmlDocument(Document document) {
        if (document == null) {
            throw new NullPointerException("XmlDocument requires a non-null Document");
        }
        this.document = document;
    }

    @Override
    public Node getNode() {
        return document;
    }

    public static XmlDocument newInstance(File file) {
        if (file == null) {
            throw new RuntimeException("XML file cannot be null");
        }
        var doc = SpecsXml.getXmlRoot(file);
        // SpecsXml may throw for schema violations and return null for IO/config errors
        if (doc == null) {
            throw new RuntimeException("Could not parse XML file: " + file);
        }
        return new XmlDocument(doc);
    }

    public static XmlDocument newInstance(String contents) {
        if (contents == null) {
            throw new NullPointerException("XML contents cannot be null");
        }
        if (contents.isEmpty()) {
            throw new RuntimeException("XML document not according to schema");
        }
        var doc = SpecsXml.getXmlRoot(contents);
        if (doc == null) {
            throw new RuntimeException("Could not parse XML contents (string)");
        }
        return new XmlDocument(doc);
    }

    public static XmlDocument newInstance(InputStream inputStream) {
        if (inputStream == null) {
            throw new RuntimeException("XML input stream cannot be null");
        }
        return newInstance(inputStream, null);
    }

    public static XmlDocument newInstance(InputStream inputStream, InputStream schema) {
        if (inputStream == null && schema == null) {
            throw new RuntimeException("XML input stream and schema cannot both be null");
        }
        var doc = SpecsXml.getXmlRoot(inputStream, schema);
        if (doc == null) {
            throw new RuntimeException("Could not parse XML from input streams");
        }
        return new XmlDocument(doc);
    }

    public static XmlDocument newInstanceFromUri(String uri) {
        if (uri == null || uri.isEmpty()) {
            throw new RuntimeException("XML URI cannot be null or empty");
        }
        var doc = SpecsXml.getXmlRootFromUri(uri);
        if (doc == null) {
            throw new RuntimeException("Could not parse XML from URI: " + uri);
        }
        return new XmlDocument(doc);
    }

}
