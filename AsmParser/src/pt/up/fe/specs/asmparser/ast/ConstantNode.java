/**
 * Copyright 2024 SPeCS.
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

package pt.up.fe.specs.asmparser.ast;

import java.util.Collection;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

public class ConstantNode extends InstructionFormatNode {

    public static final DataKey<String> LITERAL = KeyFactory.string("literal");

    public ConstantNode(DataStore data, Collection<? extends InstructionFormatNode> children) {
        super(data, children);
    }

    public int getLiteralAsInt() {
        return Integer.parseInt(get(LITERAL), 2);
    }

}
