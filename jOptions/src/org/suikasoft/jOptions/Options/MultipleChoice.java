/**
 * Copyright 2013 SPeCS Research Group.
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

package org.suikasoft.jOptions.Options;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author Joao Bispo
 * 
 */
public class MultipleChoice {

    private final List<String> choices;
    /**
     * Maps choices to indexes
     */
    private final Map<String, Integer> choicesMap;
    private int currentChoice;

    private MultipleChoice(List<String> choices, Map<String, String> alias) {
        this.choices = choices;
        choicesMap = SpecsFactory.newHashMap();
        for (int i = 0; i < choices.size(); i++) {
            choicesMap.put(choices.get(i), i);
        }

        // Add all alias
        for (String aliasName : alias.keySet()) {
            // Get choice
            String choice = alias.get(aliasName);

            // Get index
            Integer index = choicesMap.get(choice);
            if (index == null) {
                SpecsLogs.msgInfo("Could not find choice '" + choice + "' for alias '"
                        + aliasName + "'");
                continue;
            }

            // Add alias
            choicesMap.put(aliasName, index);
        }

        currentChoice = 0;
    }

    public static MultipleChoice newInstance(List<String> choices) {
        Map<String, String> emptyMap = Collections.emptyMap();
        return newInstance(choices, emptyMap);
    }

    public static MultipleChoice newInstance(List<String> choices, Map<String, String> alias) {
        // Check if number of choices is at least one
        if (choices.isEmpty()) {
            throw new RuntimeException(
                    "MultipleChoice needs at least one choice, passed an empty list.");
        }

        return new MultipleChoice(choices, alias);
    }

    // public static DataKey<Object> newOption(String optionName, List<String> choices,
    // String defaultChoice) {
    //
    // Map<String, String> emptyMap = Collections.emptyMap();
    //
    // return newOption(optionName, choices, defaultChoice, emptyMap);
    // }

    // public static DataKey<Object> newOption(String optionName, List<String> choices,
    // String defaultChoice, Map<String, String> alias) {
    //
    // String defaultString = getDefaultString(choices, defaultChoice);
    // String helpString = OptionUtils.getHelpString(optionName, MultipleChoice.class, defaultString);
    //
    // DataKey<Object> definition = new GenericOptionDefinition(optionName, MultipleChoice.class, helpString)
    // .setDefault(() -> newInstance(choices).setChoice(defaultChoice))
    // .setDecoder(value -> new MultipleChoice(choices, alias).setChoice(value));
    //
    // return definition;
    // }

    // private static String getDefaultString(List<String> choices, String defaultChoice) {
    // StringBuilder builder = new StringBuilder();
    //
    // builder.append(defaultChoice).append(" [");
    // if (!choices.isEmpty()) {
    // builder.append(choices.get(0));
    // }
    //
    // for (int i = 1; i < choices.size(); i++) {
    // builder.append(", ").append(choices.get(i));
    // }
    //
    // builder.append("]");
    //
    // String defaultString = builder.toString();
    // return defaultString;
    // }

    // /**
    // * Define multiple choices through an enumeration.
    // *
    // * <p>
    // * The enumeration class can optionally implement the interface AliasProvider, if the rules for enumeration names
    // * are too limiting, or if several alias for the same choice are needed.
    // *
    // * @param optionName
    // * @param aClass
    // * @param defaultValue
    // * @return
    // */
    // public static <T extends Enum<T>> DataKey<Object> newOption(String optionName,
    // Class<T> aClass, T defaultValue) {
    //
    // List<String> choices = EnumUtils.buildList(aClass.getEnumConstants());
    //
    // Map<String, String> alias = Collections.emptyMap();
    //
    // // Check if class implements AliasProvider
    // T anEnum = EnumUtils.getFirstEnum(aClass);
    // if (anEnum instanceof AliasProvider) {
    // alias = ((AliasProvider) anEnum).getAlias();
    // }
    //
    // return newOption(optionName, choices, defaultValue.name(), alias);
    // }

    public MultipleChoice setChoice(String choice) {
        // Get index
        Integer index = choicesMap.get(choice);

        if (index == null) {
            SpecsLogs.msgWarn("Choice '" + choice + "' not available. Available choices:"
                    + choices);
            return this;
        }

        currentChoice = index;

        return this;
    }

    // private static ValueConverter getConverter(final List<String> choices,
    // final Map<String, String> alias) {
    // return new ValueConverter() {
    //
    // @Override
    // public Object convert(String value) {
    // MultipleChoice newObject = new MultipleChoice(choices, alias);
    // newObject.setChoice(value);
    //
    // return newObject;
    // }
    // };
    //
    // }

    public String getChoice() {
        return choices.get(currentChoice);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getChoice();
    }
}
