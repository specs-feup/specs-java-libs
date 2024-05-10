/**
 * Copyright 2024 SPeCS.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.asmparser.parser32bit;

import pt.up.fe.specs.asmparser.ast.*;
import pt.up.fe.specs.asmparser.parser32bit.rules.Asm32bitConstantRule;
import pt.up.fe.specs.asmparser.parser32bit.rules.Asm32bitFieldRule;
import pt.up.fe.specs.asmparser.parser32bit.rules.Asm32bitIgnoreRule;
import pt.up.fe.specs.asmparser.parser32bit.rules.Asm32bitRule;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

import java.util.List;
import java.util.Map;

public class Asm32bitParser {

    private final int id;
    private final List<Asm32bitRule> rules;
    private final Map<String, Integer> fieldsMap;

    private final int numFields;
    private final int[] fieldIndexes;

    private Asm32bitParser(int id, List<Asm32bitRule> rules, Map<String, Integer> fieldsMap) {
        this.id = id;
        this.rules = rules;
        this.fieldsMap = fieldsMap;
        this.fieldIndexes = buildFieldsIndexes(rules, fieldsMap);
        this.numFields = fieldIndexes.length;
    }

    private int[] buildFieldsIndexes(List<Asm32bitRule> rules, Map<String, Integer> fieldsMap) {
        var fieldRules = rules.stream()
                .filter(rule -> rule instanceof Asm32bitFieldRule)
                .map(Asm32bitFieldRule.class::cast)
                .toList();

        var numFields = fieldsMap != null ? fieldsMap.size() : fieldRules.size();

        var fieldIndexes = new int[numFields];

        // If no map, just fill with indexes from 1 to N
        if (fieldsMap == null) {
            for (int i = 0; i < numFields; i++) {
                fieldIndexes[i] = i + 1;
            }
        } else {
            // Iterate over all rules with fields, store the corresponding index
            for (int i = 0; i < fieldRules.size(); i++) {
                fieldIndexes[i] = fieldsMap.get(fieldRules.get(i).getField());
            }
        }

        return fieldIndexes;
    }

    public int[] parse(long instruction) {

        // First element contains instruction format id
        int[] decoded = new int[1 + numFields];
        decoded[0] = id;

        //int fieldIndex = 1;
        int fieldIndex = 0;
        int startIndex = 0;

        // Iterate over all rules
        for (var rule : rules) {
            var res = rule.parse(instruction, startIndex);

            // If res null, parsing failed
            if (res == null) {
                return null;
            }

            // If rule extracts a value, store it
            if (rule instanceof Asm32bitFieldRule) {
                decoded[fieldIndexes[fieldIndex]] = res.value();
                fieldIndex++;
            }

            // Update start index
            startIndex = res.nextIndex();
        }

        return decoded;
    }

    public static Asm32bitParser build(int id, RuleNode rule) {
        return build(id, rule, null);
    }

    public static Asm32bitParser build(int id, RuleNode rule, Map<String, Integer> fieldsMap) {
        // Check if total bits is 32
        var totalBits = rule.getTotalBits();
        if (totalBits != 32) {
            throw new RuntimeException("Given rule represents " + totalBits + " bits, only 32 bits are supported.");
        }

        var rules = rule.getChildren().stream()
                .map(Asm32bitParser::convert)
                .toList();

        return new Asm32bitParser(id, rules, fieldsMap);
    }

    private static Asm32bitRule convert(InstructionFormatNode node) {
        if (node instanceof ConstantNode constantNode) {
            return new Asm32bitConstantRule(constantNode.getLiteralAsInt(), constantNode.getNumBits());
        }

        if (node instanceof IgnoreNode) {
            return new Asm32bitIgnoreRule(node.getNumBits());
        }

        if (node instanceof FieldNode fieldNode) {
            return new Asm32bitFieldRule(fieldNode.getField(), fieldNode.getNumBits());
        }

        throw new NotImplementedException(node.getClass());
    }
}
