/**
 * Copyright 2021 SPeCS.
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

package pt.up.fe.specs.jsengine;

import pt.up.fe.specs.util.enums.EnumHelperWithValue;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.providers.StringProvider;

/**
 * Enum representing JavaScript file types (e.g., JS, MJS).
 */
public enum JsFileType implements StringProvider {

    /**
     * Represents a standard JavaScript file with the ".js" extension.
     */
    NORMAL("js"),

    /**
     * Represents a JavaScript module file with the ".mjs" extension.
     */
    MODULE("mjs");

    private static final Lazy<EnumHelperWithValue<JsFileType>> HELPER = EnumHelperWithValue
            .newLazyHelperWithValue(JsFileType.class);

    private final String extension;

    /**
     * Constructor for JsFileType.
     * 
     * @param extension the file extension associated with the JavaScript file type
     */
    private JsFileType(String extension) {
        this.extension = extension;
    }

    /**
     * Gets the file extension associated with the JavaScript file type.
     * 
     * @return the file extension as a string
     */
    public String getExtension() {
        return extension;
    }

    /**
     * Gets the string representation of the file extension.
     * 
     * @return the file extension as a string
     */
    @Override
    public String getString() {
        return extension;
    }

    /**
     * Retrieves the JsFileType based on the given file extension.
     * 
     * @param extension the file extension to match
     * @return the corresponding JsFileType
     */
    public static JsFileType getType(String extension) {
        return HELPER.get().fromValue(extension);
    }

}
