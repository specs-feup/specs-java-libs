/**
 * Copyright 2018 SPeCS.
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

package pt.up.fe.specs.util.utilities;

import pt.up.fe.specs.util.collections.AccumulatorMap;

/**
 * 
 * @author JBispo
 *
 */
public class IdGenerator {

    private final String id;
    private final AccumulatorMap<String> acc;

    public IdGenerator() {
        this((String) null);
    }

    public IdGenerator(String id) {
        this.id = id;
        this.acc = new AccumulatorMap<>();
    }

    public IdGenerator(IdGenerator idGenerator) {
        this.acc = new AccumulatorMap<>(idGenerator.acc);
        this.id = idGenerator.id;
    }

    public String next(String prefix) {
        var actualPrefix = id != null ? id + prefix : prefix;

        Integer suffixValue = acc.add(actualPrefix);

        return actualPrefix + suffixValue;
    }
}
