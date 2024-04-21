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

package pt.up.fe.specs.asmparser.parser32bit;

import java.util.List;

import pt.up.fe.specs.asmparser.ast.ConstantNode;
import pt.up.fe.specs.asmparser.ast.FieldNode;
import pt.up.fe.specs.asmparser.ast.IgnoreNode;
import pt.up.fe.specs.asmparser.ast.InstructionFormatNode;
import pt.up.fe.specs.asmparser.ast.RuleNode;
import pt.up.fe.specs.asmparser.parser32bit.rules.Asm32bitConstantRule;
import pt.up.fe.specs.asmparser.parser32bit.rules.Asm32bitFieldRule;
import pt.up.fe.specs.asmparser.parser32bit.rules.Asm32bitIgnoreRule;
import pt.up.fe.specs.asmparser.parser32bit.rules.Asm32bitRule;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

public class Asm32bitParser {

    private final int id;
    private final List<Asm32bitRule> rules;
    private final int numFields;

    private Asm32bitParser(int id, List<Asm32bitRule> rules) {
        this.id = id;
        this.rules = rules;
        this.numFields = (int) rules.stream()
                .filter(rule -> rule.hasValue())
                .count();
    }

    public int[] parse(long instruction) {

        // First element contains instruction format id
        int[] decoded = new int[1 + numFields];
        decoded[0] = id;

        int fieldIndex = 1;
        int startIndex = 0;

        // Iterate over all rules
        for (var rule : rules) {
            var res = rule.parse(instruction, startIndex);

            // If res null, parsing failed
            if (res == null) {
                return null;
            }

            // If rule extracts a value, store it
            if (rule.hasValue()) {
                decoded[fieldIndex] = res.value();
                fieldIndex++;
            }

            // Update start index
            startIndex = res.nextIndex();
        }

        return decoded;
    }

    public static Asm32bitParser build(int id, RuleNode rule) {
        // Check if total bits is 32
        var totalBits = rule.getTotalBits();
        if (totalBits != 32) {
            throw new RuntimeException("Given rule represents " + totalBits + " bits, only 32 bits are supported.");
        }

        var rules = rule.getChildren().stream()
                .map(Asm32bitParser::convert)
                .toList();

        return new Asm32bitParser(id, rules);
    }

    private static Asm32bitRule convert(InstructionFormatNode node) {
        if (node instanceof ConstantNode constantNode) {
            return new Asm32bitConstantRule(constantNode.getLiteralAsInt(), constantNode.getNumBits());
        }

        if (node instanceof IgnoreNode) {
            return new Asm32bitIgnoreRule(node.getNumBits());
        }

        if (node instanceof FieldNode) {
            return new Asm32bitFieldRule(node.getNumBits());
        }

        throw new NotImplementedException(node.getClass());
    }
}
