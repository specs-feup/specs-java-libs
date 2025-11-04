/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.eclipse.Utilities;

import java.io.File;

import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.utilities.LineStream;

public enum License {

    APACHE_2_0,
    MIT,
    UNKNOWN;

    public static License valueOf(File licenseFile) {
        try (LineStream licenseLines = LineStream.newInstance(licenseFile)) {
            while (licenseLines.hasNextLine()) {
                String line = licenseLines.nextLine().strip();

                if (line.startsWith("Apache License")) {
                    String secondLine = licenseLines.nextLine().strip();
                    if (secondLine.startsWith("Version 2.0")) {
                        return APACHE_2_0;
                    }
                }

            }
        }
        System.out.println("Could not determine license of file: " + licenseFile.getAbsolutePath());
        return UNKNOWN;
    }

    public String getXmlInfo() {
        switch (this) {
        case APACHE_2_0:
            return "    <license>\n" +
                    "      <name>The Apache Software License, Version 2.0</name>\n" +
                    "      <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>\n" +
                    "    </license>";
        case MIT:
            return "  <license>\r\n" +
                    "    <name>MIT License</name>\r\n" +
                    "    <url>http://www.opensource.org/licenses/mit-license.php</url>\r\n" +
                    "  </license>";
        default:
            throw new NotImplementedException(this);
        }
    }
}
