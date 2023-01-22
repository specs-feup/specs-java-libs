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

package pt.up.fe.specs.jsengine;

import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * @author Joao Bispo
 *
 */
public enum JsEngineResource implements ResourceProvider {
    GET_REGEX_GROUPS("getRegexGroups.js"),
    JAVA_COMPATIBILITY("JavaCompatibility.js");

    private static final String BASE_PATH = "pt/up/fe/specs/jsengine/";

    private final String resource;

    /**
     * @param resource
     */
    private JsEngineResource(String resource) {
        this.resource = JsEngineResource.BASE_PATH + resource;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * pt.up.fe.specs.util.Interfaces.ResourceProvider#getResource()
     */
    @Override
    public String getResource() {
        return resource;
    }

    public static String getRegexGroupsName() {
        return "jsengine_getRegexGroups";
    }
}
