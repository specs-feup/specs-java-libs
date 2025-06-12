/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.specs.logback;

import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * Enum representing Logback resource files for SPeCS projects.
 */
public enum SpecsLogbackResource implements ResourceProvider {
    /**
     * The default logback.xml resource.
     */
    LOGBACK_XML("logback.xml");

    private final String resource;

    /**
     * Constructs a SpecsLogbackResource with the given resource name.
     *
     * @param resource the resource file name
     */
    private SpecsLogbackResource(String resource) {
        this.resource = resource;
    }

    /**
     * Returns the resource file name.
     *
     * @return the resource file name
     */
    @Override
    public String getResource() {
        return resource;
    }

}
