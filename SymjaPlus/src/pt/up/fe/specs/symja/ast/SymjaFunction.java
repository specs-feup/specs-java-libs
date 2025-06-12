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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.symja.ast;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

/**
 * Represents a function node in the Symja AST.
 */
public class SymjaFunction extends SymjaNode {

    /** DataKey indicating if the function has parenthesis. */
    public static final DataKey<Boolean> HAS_PARENTHESIS = KeyFactory.bool("hasParenthesis")
            .setDefault(() -> true);

    /**
     * Constructs a SymjaFunction node.
     *
     * @param data the data store
     * @param children the child nodes
     */
    public SymjaFunction(DataStore data, Collection<? extends SymjaNode> children) {
        super(data, children);
    }

}
