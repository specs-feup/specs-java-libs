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

public enum JsFileType implements StringProvider {

    NORMAL("js"),
    MODULE("mjs");

    /**
     * CommonJS, supports features not available in strict mode (e.g. with)
     */
    // COMMON("cjs");

    private static final Lazy<EnumHelperWithValue<JsFileType>> HELPER = EnumHelperWithValue
            .newLazyHelperWithValue(JsFileType.class);

    private final String extension;

    private JsFileType(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    @Override
    public String getString() {
        return extension;
    }

    public static JsFileType getType(String extension) {
        return HELPER.get().fromValue(extension);
        /*
        switch (extension.toLowerCase()) {
        case "js":
            return NORMAL;
        case "mjs":
            return MODULE;
        case "cjs":
            return COMMON;
        default:
            throw new NotImplementedException(extension);
        }
        */

    }

}
