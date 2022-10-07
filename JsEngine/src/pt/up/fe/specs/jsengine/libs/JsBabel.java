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

package pt.up.fe.specs.jsengine.libs;

import pt.up.fe.specs.jsengine.JsEngine;
import pt.up.fe.specs.jsengine.JsEngineType;
import pt.up.fe.specs.jsengine.JsEngineWebResources;
import pt.up.fe.specs.util.SpecsIo;

public class JsBabel {

    private static final ThreadLocal<JsEngine> BABEL_ENGINE = ThreadLocal
            .withInitial(JsBabel::newBabelEngine);

    private static JsEngine newBabelEngine() {
        // Create JsEngine
        var engine = JsEngineType.GRAALVM.newEngine();

        // Get Babel source code
        var babelSource = JsEngineWebResources.BABEL.writeVersioned(SpecsIo.getTempFolder("specs_js-engine"),
                JsBabel.class);

        // Load babel
        engine.eval(SpecsIo.read(babelSource.getFile()));

        // Load toES6 function
        // Using Chrome 58 as target due to being the value that appears as example in Babel documentation, and Esprima
        // apparently supporting it
        engine.eval(
                "function toES6(code) {return Babel.transform(code, { presets: [\"env\"], targets: {\"chrome\": \"58\"} }).code;}");

        return engine;
    }

    private static JsEngine getEngine() {
        return BABEL_ENGINE.get();
    }

    public static String toES6(String jsCode) {
        var toEs5Function = getEngine().get("toES6");
        var es5Code = getEngine().call(toEs5Function, jsCode);

        return es5Code.toString();
    }
}
