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

package pt.up.fe.specs.asmparser;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinitions;

import pt.up.fe.specs.asmparser.ast.InstructionFormatNode;
import pt.up.fe.specs.asmparser.ast.RootNode;

public class InstructionFormatParser {

    public static void main(String[] args) {
        new InstructionFormatParser().parse("hello");
    }

    public RootNode parse(String instructionFormatRule) {
        String currentRule = instructionFormatRule;

        var root = newNode(RootNode.class);

        System.out.println("ROOT NODE: " + root.toTree());
        /*
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
                var result = extractAmount(currentRule, rule);
                if (result != null) {
                    constString = SpecsStrings.buildLine(constString, result.getAmount());
                    currentRule = result.getCurrentString();
                }
        
                rules.add(new ConstantRule(constString));
                continue;
            }
        
            // If x, create ignore rule
            if (currentRule.startsWith("x")) {
                int amount = 1;
                currentRule = currentRule.substring(1);
        
                // Check if has ()
                var result = extractAmount(currentRule, rule);
                if (result != null) {
                    amount = result.getAmount();
                    currentRule = result.getCurrentString();
                }
        
                rules.add(new IgnoreRule(amount));
                continue;
            }
        
            // Otherwise, interpret as a field with ()
            int startIndex = currentRule.indexOf('(');
            if (startIndex == -1) {
                throw new RuntimeException("Expected field name to have () associated: " + rule);
            }
        
            String fieldName = currentRule.substring(0, startIndex);
            currentRule = currentRule.substring(startIndex);
            var result = extractAmount(currentRule, rule);
        
            rules.add(new FieldRule(fieldName, result.getAmount()));
            currentRule = result.getCurrentString();
        }
        
        // Rules could be optimized - e.g., fuse constant rules together
        // System.out.println("RULES: " + rules);
        return rules;
        */
        return null;
    }

    private DataStore newDataStore(Class<? extends InstructionFormatNode> nodeClass) {
        return DataStore.newInstance(StoreDefinitions.fromInterface(nodeClass), true);
    }

    @SuppressWarnings("unchecked")
    private <T extends InstructionFormatNode> T newNode(Class<? extends InstructionFormatNode> nodeClass) {
        var data = newDataStore(nodeClass);

        try {
            return (T) nodeClass
                    .cast(nodeClass.getConstructor(DataStore.class, Collection.class).newInstance(data, null));
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new RuntimeException("Could not create node", e);
        }
    }
}
