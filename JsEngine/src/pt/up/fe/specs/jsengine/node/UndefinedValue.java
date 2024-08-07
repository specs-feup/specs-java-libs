/**
 * Copyright 2023 SPeCS.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * under the License.
 */

package pt.up.fe.specs.jsengine.node;

/**
 * Dummy class to represent javascript's "undefined" in Java. Used by NodeJsEngine as a return value.
 */
public class UndefinedValue {

    private static final UndefinedValue UNDEFINED = new UndefinedValue();

    public static UndefinedValue getUndefined() {
        return UNDEFINED;
    }
}
