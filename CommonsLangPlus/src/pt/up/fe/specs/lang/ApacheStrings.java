/**
 * Copyright 2017 SPeCS.
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

package pt.up.fe.specs.lang;

import org.apache.commons.text.StringEscapeUtils;

/**
 * Utility class for Apache Commons Text string operations.
 */
public class ApacheStrings {

    /**
     * Escapes HTML entities in the given string using Apache Commons Text.
     *
     * @param html the input HTML string
     * @return the escaped HTML string
     */
    public static String escapeHtml(String html) {
        return StringEscapeUtils.escapeHtml4(html);
    }
}
