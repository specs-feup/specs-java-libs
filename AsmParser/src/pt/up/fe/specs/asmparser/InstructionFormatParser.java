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

package pt.up.fe.specs.asmparser;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

import pt.up.fe.specs.asmparser.ast.ConstantNode;
import pt.up.fe.specs.asmparser.ast.FieldNode;
import pt.up.fe.specs.asmparser.ast.IgnoreNode;
import pt.up.fe.specs.asmparser.ast.InstructionFormatNode;
import pt.up.fe.specs.asmparser.ast.RuleNode;
import pt.up.fe.specs.asmparser.parser32bit.Asm32bitParser;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldData;
import pt.up.fe.specs.binarytranslation.asm.parsing.AsmFieldType;
import pt.up.fe.specs.binarytranslation.asm.parsing.binaryasmparser.BinaryAsmInstructionParser;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;

public class InstructionFormatParser {

    public static void main(String[] args) {
        SpecsSystem.programStandardInit();

        // var rule = new InstructionFormatParser().parse("100101_registerd(5)_1000_opcodea(1)_0_imm(15)");

        var rule2 = new InstructionFormatParser().parse("0110_xx_registerd(5)_1000_opcodea(1)_0_imm(15)");

        System.out.println(rule2.toTree());

        var parser2 = Asm32bitParser.build(0, rule2);
        var res = parser2.parse(Long.parseLong("01100011111100000010101010101101", 2));

        System.out.println("RES: " + Arrays.toString(res));

        test();
    }

    enum TestAsmField implements AsmFieldType {
        INST1,
        INST2;
    }

    private static void test() {
        var addr = "0000";
        var rule = "0110_xx_registerd(5)_1000_opcodea(1)_0_imm(15)";
        var instruction = "01100011111100000010101010101101";
        var instructionHex = Long.toString(Long.parseLong(instruction, 2), 16);
        var instructionLong = Long.parseLong(instruction, 2);
        var iterations = 1_000_000;

        // String-based
        var stringParser = new BinaryAsmInstructionParser(TestAsmField.INST1, rule, null);

        var start1 = System.nanoTime();
        var acc1 = 0l;
        for (int i = 0; i < iterations; i++) {
            var res = stringParser.parse(addr, instructionHex);
            var fieldValueString = res.get().get(AsmFieldData.FIELDS).get("registerd");
            var fieldValue = Integer.parseInt(fieldValueString, 2);
            acc1 += fieldValue;
        }
        SpecsStrings.printTime("String parsing", start1);
        System.out.println("String result: " + acc1);

        // Long based
        var astRule = new InstructionFormatParser().parse(rule);
        var longParser = Asm32bitParser.build(0, astRule);

        var start2 = System.nanoTime();
        var acc2 = 0l;
        for (int i = 0; i < iterations; i++) {
            var res = longParser.parse(instructionLong);
            var fieldValue = res[1];
            acc2 += fieldValue;
        }
        SpecsStrings.printTime("Long parsing", start2);
        System.out.println("Long result: " + acc2);

    }

    public RuleNode parse(String instructionFormatRule) {
        String currentRule = instructionFormatRule;

        var root = newNode(RuleNode.class);

        while (!currentRule.isEmpty()) {
            // Ignore underscore
            if (currentRule.startsWith("_")) {
                currentRule = currentRule.substring(1);
                continue;
            }

            // If 0 or 1, create constant rule
            if (currentRule.startsWith("0") || currentRule.startsWith("1")) {
                String constString = currentRule.substring(0, 1);
                currentRule = currentRule.substring(1);

                // Check if has ()
                var result = extractAmount(currentRule, instructionFormatRule);
                if (result != null) {
                    constString = SpecsStrings.buildLine(constString, result.amount());
                    currentRule = result.currentString();
                }

                var constant = newNode(ConstantNode.class);
                constant.set(InstructionFormatNode.NUM_BITS, constString.length());
                constant.set(ConstantNode.LITERAL, constString);
                root.addChild(constant);
                continue;
            }

            // If x, create ignore rule
            if (currentRule.startsWith("x")) {
                int amount = 1;
                currentRule = currentRule.substring(1);

                // Check if has ()
                var result = extractAmount(currentRule, instructionFormatRule);
                if (result != null) {
                    amount = result.amount();
                    currentRule = result.currentString();
                }

                var ignore = newNode(IgnoreNode.class);
                ignore.set(InstructionFormatNode.NUM_BITS, amount);
                root.addChild(ignore);
                continue;
            }

            // Otherwise, interpret as a field with ()
            int startIndex = currentRule.indexOf('(');
            if (startIndex == -1) {
                throw new RuntimeException("Expected field name to have () associated: " + instructionFormatRule);
            }

            String fieldName = currentRule.substring(0, startIndex);
            currentRule = currentRule.substring(startIndex);
            var result = extractAmount(currentRule, instructionFormatRule);

            var field = newNode(FieldNode.class);
            field.set(InstructionFormatNode.NUM_BITS, result.amount());
            field.set(FieldNode.FIELD, fieldName);
            root.addChild(field);

            currentRule = result.currentString();
        }

        // Rules could be optimized - e.g., fuse constant rules together
        // System.out.println("RULES: " + rules);
        // return rules;

        // Fuse rules that are next to each other
        collapseConstantNodes(root);
        collapseIgnoreNodes(root);

        return root;
    }

    private void collapseConstantNodes(RuleNode root) {
        // Collapse sequential ConstantNodes

        var newChildren = new ArrayList<InstructionFormatNode>();

        ConstantNode currentConstant = null;
        for (var child : root.getChildren()) {

            if (!(child instanceof ConstantNode)) {
                // If current constant not null, store it
                if (currentConstant != null) {
                    newChildren.add(currentConstant);
                    currentConstant = null;
                }

                // Just add node
                newChildren.add(child);
                continue;
            }

            // Is constant node
            var constant = (ConstantNode) child;
            var numBits = constant.get(InstructionFormatNode.NUM_BITS);
            var literal = constant.get(ConstantNode.LITERAL);

            if (currentConstant == null) {
                currentConstant = newNode(ConstantNode.class);
            } else {
                numBits = currentConstant.get(InstructionFormatNode.NUM_BITS) + numBits;
                literal = currentConstant.get(ConstantNode.LITERAL) + literal;
            }

            currentConstant.set(InstructionFormatNode.NUM_BITS, numBits);
            currentConstant.set(ConstantNode.LITERAL, literal);
        }

        // If current constant not null, store it
        if (currentConstant != null) {
            newChildren.add(currentConstant);
            currentConstant = null;
        }

        // Set children
        root.setChildren(newChildren);

    }

    private void collapseIgnoreNodes(RuleNode root) {
        // Collapse sequential IgnoreNodes

        var newChildren = new ArrayList<InstructionFormatNode>();

        IgnoreNode currentIgnore = null;
        for (var child : root.getChildren()) {

            if (!(child instanceof IgnoreNode)) {
                // If current ignore not null, store it
                if (currentIgnore != null) {
                    newChildren.add(currentIgnore);
                    currentIgnore = null;
                }

                // Just add node
                newChildren.add(child);
                continue;
            }

            // Is ignore node
            var ignore = (IgnoreNode) child;
            var numBits = ignore.get(InstructionFormatNode.NUM_BITS);

            if (currentIgnore == null) {
                currentIgnore = newNode(IgnoreNode.class);
            } else {
                numBits = currentIgnore.get(InstructionFormatNode.NUM_BITS) + numBits;
            }

            currentIgnore.set(InstructionFormatNode.NUM_BITS, numBits);
        }

        // If current ignore not null, store it
        if (currentIgnore != null) {
            newChildren.add(currentIgnore);
            currentIgnore = null;
        }

        // Set children
        root.setChildren(newChildren);

    }

    private static ExtractResult extractAmount(String currentRule, String fullRule) {
        if (!currentRule.startsWith("(")) {
            return null;
        }

        int endIndex = currentRule.indexOf(')');
        if (endIndex == -1) {
            throw new RuntimeException("Unbalanced parenthesis on rule: " + fullRule);
        }

        int amount = Integer.parseInt(currentRule.substring(1, endIndex));
        String updatedCurrentRule = currentRule.substring(endIndex + 1);

        return new ExtractResult(updatedCurrentRule, amount);
    }

    private DataStore newDataStore(Class<? extends InstructionFormatNode> nodeClass) {
        return DataStore.newInstance(StoreDefinitions.fromInterface(nodeClass), true);
    }

    private <T extends InstructionFormatNode> T newNode(Class<T> nodeClass) {
        var data = newDataStore(nodeClass);

        try {
            return nodeClass.getConstructor(DataStore.class, Collection.class).newInstance(data, null);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Could not create node", e);
        }
    }
}
