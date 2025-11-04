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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.mutators.config;

import java.util.HashMap;
import java.util.Map;

public class MapConfiguration implements MutatorConfiguration {

    /**
     * Stores the current configuration for each mutator.
     */
    private final Map<String, Object> mutationConfig;

    public MapConfiguration(Map<String, Object> mutationConfig) {
        this.mutationConfig = mutationConfig;
    }

    /**
     * Helper constructor when keys and values are both Strings.
     * 
     * @param keyValuePairs
     * @return
     */
    public static MapConfiguration newInstance(String... keyValuePairs) {
        if (keyValuePairs.length % 2 != 0) {
            throw new RuntimeException("Expected an even number of arguments");
        }

        Map<String, Object> config = new HashMap<>();
        for (int i = 0; i < keyValuePairs.length; i += 2) {
            config.put(keyValuePairs[i], keyValuePairs[i + 1]);
        }

        return new MapConfiguration(config);
    }

    @Override
    public Object get(String id) {
        if (!mutationConfig.containsKey(id)) {
            throw new RuntimeException("No configuration for id '" + id + "'");
        }

        return mutationConfig.get(id);
    }

}
