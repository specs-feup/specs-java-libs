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

import pt.up.fe.specs.util.providers.WebResourceProvider;

public interface JsEngineWebResources {

    static WebResourceProvider create(String resourceUrl, String version) {
        return WebResourceProvider.newInstance("http://specs.fe.up.pt/resources/jsengine/", resourceUrl, version);
    }

    // Taken from https://unpkg.com/browse/@babel/standalone@7.15.6/
    WebResourceProvider BABEL = create("babel.min.js", "v7.15.6");
    WebResourceProvider BABEL_LATEST = WebResourceProvider.newInstance("https://unpkg.com/@babel/standalone/",
            "babel.js");
    WebResourceProvider ESPRIMA = create("esprima.js", "v4.0.1");

}
