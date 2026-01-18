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

package pt.up.fe.specs.util;

import static org.assertj.core.api.Assertions.*;

import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import pt.up.fe.specs.util.xml.XmlDocument;

/**
 * Test suite for XmlDocument and related XML utility classes.
 * 
 * This test class covers XML functionality including:
 * - XML document parsing
 * - Element retrieval by name
 * - Attribute extraction
 * - XML navigation and querying
 */
@DisplayName("XmlDocument Tests")
public class XmlNodeTest {

    private final String XML_EXAMPLE = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n" +
            "<manifest xmlns:android=\"http://schemas.android.com/apk/res/android\"\r\n" +
            "    package=\"com.tourism.app\">\r\n" +
            "\r\n" +
            "    <uses-permission android:name=\"android.permission.FOREGROUND_SERVICE\"/>\r\n" +
            "    <uses-permission android:name=\"android.permission.ACCESS_FINE_LOCATION\"/>\r\n" +
            "\r\n" +
            "    <application\r\n" +
            "        android:fullBackupContent=\"true\"\r\n" +
            "        android:allowBackup=\"true\"\r\n" +
            "        android:icon=\"@mipmap/ic_launcher\"\r\n" +
            "        android:label=\"@string/app_name\"\r\n" +
            "        android:roundIcon=\"@mipmap/ic_launcher_round\"\r\n" +
            "        android:supportsRtl=\"true\"\r\n" +
            "        android:theme=\"@style/AppTheme\">\r\n" +
            "        <activity\r\n" +
            "            android:name=\".MainActivity\"\r\n" +
            "            android:label=\"@string/app_name\"\r\n" +
            "            android:theme=\"@style/AppTheme.NoActionBar\">\r\n" +
            "            <intent-filter>\r\n" +
            "                <action android:name=\"android.intent.action.MAIN\" />\r\n" +
            "                <action android:name=\"android.intent.action.VIEW\" />\r\n" +
            "                <category android:name=\"android.intent.category.LAUNCHER\" />\r\n" +
            "            </intent-filter>\r\n" +
            "        </activity>\r\n" +
            "\r\n" +
            "        <service\r\n" +
            "            android:name=\".service.Track_Service\"\r\n" +
            "            android:enabled=\"true\"\r\n" +
            "            android:exported=\"false\"/>\r\n" +
            "    </application>\r\n" +
            "\r\n" +
            "</manifest>";

    @Nested
    @DisplayName("XML Document Parsing")
    class XmlDocumentParsing {

        @Test
        @DisplayName("getElementsByName should retrieve elements and their attributes correctly")
        void testGetElementsByName_RetrieveAttributes_ReturnsCorrectValues() {
            XmlDocument document = XmlDocument.newInstance(XML_EXAMPLE);
            var elementAttrs = document.getElementsByName("uses-permission").stream()
                    .map(element -> element.getAttribute("android:name"))
                    .collect(Collectors.joining(", "));

            assertThat(elementAttrs).isEqualTo("android.permission.FOREGROUND_SERVICE, android.permission.ACCESS_FINE_LOCATION");
        }

        @Test
        @DisplayName("getElementsByName should handle non-existent elements gracefully")
        void testGetElementsByName_NonExistentElement_ReturnsEmptyList() {
            XmlDocument document = XmlDocument.newInstance(XML_EXAMPLE);
            var elements = document.getElementsByName("non-existent");
            assertThat(elements).isEmpty();
        }

        @Test
        @DisplayName("XmlDocument should throw exception for empty XML")
        void testXmlDocument_EmptyXml_ShouldThrowException() {
            assertThatThrownBy(() -> {
                XmlDocument.newInstance("");
            }).isInstanceOf(RuntimeException.class)
              .hasMessageContaining("XML document not according to schema");
        }

        @Test
        @DisplayName("XmlDocument should throw exception for null XML")
        void testXmlDocument_NullXml_ShouldThrowException() {
            assertThatThrownBy(() -> {
                XmlDocument.newInstance((String) null);
            }).isInstanceOf(NullPointerException.class);
        }

        @Test
        @DisplayName("getElementsByName should find multiple elements of same type")
        void testGetElementsByName_MultipleElements_ReturnsAllElements() {
            XmlDocument document = XmlDocument.newInstance(XML_EXAMPLE);
            var usesPermissionElements = document.getElementsByName("uses-permission");
            assertThat(usesPermissionElements).hasSize(2);
        }

        @Test
        @DisplayName("getElementsByName should find nested elements correctly")
        void testGetElementsByName_NestedElements_ReturnsCorrectElements() {
            XmlDocument document = XmlDocument.newInstance(XML_EXAMPLE);
            var actionElements = document.getElementsByName("action");
            assertThat(actionElements).hasSize(2);
            
            // Verify the attributes of the action elements
            var actionNames = actionElements.stream()
                    .map(element -> element.getAttribute("android:name"))
                    .collect(Collectors.toList());
            assertThat(actionNames).contains("android.intent.action.MAIN", "android.intent.action.VIEW");
        }

        @Test
        @DisplayName("getAttribute should handle non-existent attributes gracefully")
        void testGetAttribute_NonExistentAttribute_ShouldHandleGracefully() {
            XmlDocument document = XmlDocument.newInstance(XML_EXAMPLE);
            var elements = document.getElementsByName("uses-permission");
            if (!elements.isEmpty()) {
                assertThatCode(() -> {
                    elements.get(0).getAttribute("non-existent-attribute");
                }).doesNotThrowAnyException();
            }
        }
    }

}
