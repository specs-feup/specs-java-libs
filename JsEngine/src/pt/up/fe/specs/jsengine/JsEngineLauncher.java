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

package pt.up.fe.specs.jsengine;

/**
 * Utility class for launching JavaScript engines and managing their lifecycle.
 */
public class JsEngineLauncher {

    /**
     * Main method for launching JavaScript engines and evaluating sample scripts.
     * 
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        var engine = JsEngineType.GRAALVM_COMPAT.newEngine();

        System.out.println("Graal Compat: " + engine.eval("var a = 10; a;"));

        var engine2 = JsEngineType.GRAALVM.newEngine();

        System.out.println("Graal: " + engine2.eval("var a = 20; a;"));
    }

}
