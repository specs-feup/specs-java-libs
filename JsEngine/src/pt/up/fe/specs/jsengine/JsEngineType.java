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

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;

import pt.up.fe.specs.jsengine.graal.GraalvmJsEngine;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Enum representing the types of JavaScript engines supported.
 */
public enum JsEngineType {

    /**
     * Represents a GraalVM JavaScript engine with compatibility mode enabled.
     */
    GRAALVM_COMPAT,

    /**
     * Represents a standard GraalVM JavaScript engine.
     */
    GRAALVM;

    /**
     * Creates a new JavaScript engine based on the specified type, forbidden classes, and working directory.
     *
     * @param type The type of JavaScript engine to create.
     * @param forbiddenClasses A collection of classes that should be forbidden in the engine.
     * @param engineWorkingDirectory The working directory for the engine.
     * @return A new instance of the JavaScript engine.
     */
    public JsEngine newEngine(JsEngineType type, Collection<Class<?>> forbiddenClasses, Path engineWorkingDirectory) {
        return newEngine(type, forbiddenClasses, engineWorkingDirectory, null, System.out);
    }

    /**
     * Creates a new JavaScript engine based on the specified type, forbidden classes, working directory, and node modules folder.
     *
     * @param type The type of JavaScript engine to create.
     * @param forbiddenClasses A collection of classes that should be forbidden in the engine.
     * @param engineWorkingDirectory The working directory for the engine.
     * @param nodeModulesFolder The folder containing node modules, or null if not applicable.
     * @return A new instance of the JavaScript engine.
     */
    public JsEngine newEngine(JsEngineType type, Collection<Class<?>> forbiddenClasses, Path engineWorkingDirectory,
            File nodeModulesFolder) {
        return newEngine(type, forbiddenClasses, engineWorkingDirectory, nodeModulesFolder, System.out);
    }

    /**
     * Creates a new JavaScript engine based on the specified type, forbidden classes, working directory, node modules folder, and output stream.
     *
     * @param type The type of JavaScript engine to create.
     * @param forbiddenClasses A collection of classes that should be forbidden in the engine.
     * @param engineWorkingDirectory The working directory for the engine.
     * @param nodeModulesFolder The folder containing node modules, or null if not applicable.
     * @param laraiOutputStream The output stream for the engine, or null if not applicable.
     * @return A new instance of the JavaScript engine.
     */
    public JsEngine newEngine(JsEngineType type, Collection<Class<?>> forbiddenClasses, Path engineWorkingDirectory,
            File nodeModulesFolder, OutputStream laraiOutputStream) {
        switch (this) {
        case GRAALVM_COMPAT:
            return new GraalvmJsEngine(forbiddenClasses, true, engineWorkingDirectory, nodeModulesFolder,
                    laraiOutputStream);
        case GRAALVM:
            return new GraalvmJsEngine(forbiddenClasses, false, engineWorkingDirectory, nodeModulesFolder,
                    laraiOutputStream);
        default:
            throw new NotImplementedException(type);
        }
    }

    /**
     * Creates a new JavaScript engine with default settings.
     *
     * @return A new instance of the JavaScript engine.
     */
    public JsEngine newEngine() {
        return newEngine(this, Collections.emptyList(), null);
    }
}
