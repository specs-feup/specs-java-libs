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

package pt.up.fe.specs.util;

import static org.junit.Assert.assertEquals;

import java.util.stream.Collectors;

import org.junit.Test;

import pt.up.fe.specs.util.xml.XmlDocument;

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

    @Test
    public void test() {
        XmlDocument document = XmlDocument.newInstance(XML_EXAMPLE);
        var elementAttrs = document.getElementsByName("uses-permission").stream()
                .map(element -> element.getAttribute("android:name"))
                .collect(Collectors.joining(", "));

        assertEquals("android.permission.FOREGROUND_SERVICE, android.permission.ACCESS_FINE_LOCATION", elementAttrs);
    }

}
