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

import java.util.HashMap;
import java.util.Map;

public class BinaryOpMutator {

    /**
     * Stores the current configuration for each mutator.
     */
    private static final ThreadLocal<Map<String, Object>> MUTATION_CONFIG = ThreadLocal
            .withInitial(() -> new HashMap<>());

    public static void setConfiguration(Map<String, Object> config) {
        MUTATION_CONFIG.set(config);
    }

    public static int intOp(int operand1, int operand2, String id) {

        String op = RuntimeMutators.get(id, String.class);
        switch (op) {
        case "+":
            return operand1 + operand2;
        case "-":
            return operand1 - operand2;
        // ... remaining cases
        default:
            throw new RuntimeException("Case not defined: " + op);
        }

    }

}
