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
 * Enum representing the types of iteration supported by JavaScript 'for...of' loops.
 */
public enum ForOfType {

    /**
     * Represents native support for 'for(var a of...)' loops.
     */
    NATIVE,

    /**
     * Represents support for 'for each (...' as a for-each implementation.
     */
    FOR_EACH;
}
