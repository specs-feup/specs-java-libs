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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.asmparser.parser32bit.rules;

import pt.up.fe.specs.asmparser.parser32bit.Asm32bitResult;

public class Asm32bitIgnoreRule implements Asm32bitRule {

    private final int numBits;

    public Asm32bitIgnoreRule(int numBits) {
        this.numBits = numBits;
    }

    @Override
    public Asm32bitResult parse(long asm, int startIndex) {

        // Just ignore bits
        return new Asm32bitResult(startIndex + numBits, 0);
    }

}
