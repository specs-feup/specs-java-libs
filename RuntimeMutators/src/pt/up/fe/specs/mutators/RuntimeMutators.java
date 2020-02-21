/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.mutators;

import pt.up.fe.specs.mutators.config.MutatorConfiguration;

/**
 * Utility methods relative to Runtime Mutators.
 * 
 * @author JoaoBispo
 *
 */
public class RuntimeMutators {

    private static MutatorConfiguration CONFIG = null;

    public static void setConfiguration(MutatorConfiguration config) {
        CONFIG = config;
    }

    public static String get(String id, Class<String> valueClass) {
        return valueClass.cast(get(id));
    }

    public static Object get(String id) {
        return CONFIG.get(id);
    }
}
