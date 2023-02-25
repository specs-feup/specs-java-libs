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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.jsengine;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

import pt.up.fe.specs.jsengine.graal.GraalJsNodeEngine;
import pt.up.fe.specs.jsengine.graal.GraalvmJsEngine;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

public enum JsEngineType {

    // NASHORN,
    GRAALVM_COMPAT,
    GRAALVM,
    GRAAL_NODE;

    /**
     * Creates a new engine, according to the type. TODO: Move to JsEngineType
     * 
     * @param type
     * @param forbiddenClasses
     * @return
     */
    public JsEngine newEngine(JsEngineType type, Collection<Class<?>> forbiddenClasses, Path engineWorkingDirectory) {
        // System.out.println("TEST CLASSLOADER " + Test.class.getClassLoader());
        // System.out.println("JS ENGINE CLASS LOADER: " + GraalJSScriptEngine.class.getClassLoader());
        // System.out.println("THREAD CLASS LOADER: " + Thread.currentThread().getContextClassLoader());
        // Thread.currentThread().setContextClassLoader(GraalJSScriptEngine.class.getClassLoader());
        switch (this) {
        // case NASHORN:
        // return new NashornEngine(forbiddenClasses);
        case GRAALVM_COMPAT:
            return new GraalvmJsEngine(forbiddenClasses, true, engineWorkingDirectory);
        case GRAALVM:
            return new GraalvmJsEngine(forbiddenClasses, false, engineWorkingDirectory);
        case GRAAL_NODE:
            return new GraalJsNodeEngine();
        default:
            throw new NotImplementedException(type);
        }
    }

    public JsEngine newEngine() {
        return newEngine(this, Collections.emptyList(), null);
    }
}
