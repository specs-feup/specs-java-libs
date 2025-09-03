/*
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
 * Utility class for managing a set of multiple choices, with support for aliases and current selection.
 *
 * @author Joao Bispo
 */
public class MultipleChoice {

    private final List<String> choices;
    /**
     * Maps choices and aliases to indexes
     */
    private final Map<String, Integer> choicesMap;
    private int currentChoice;

    /**
     * Constructs a MultipleChoice with the given choices and aliases.
     *
     * @param choices the list of valid choices
     * @param alias a map of alias names to choice names
     */
    private MultipleChoice(List<String> choices, Map<String, String> alias) {
        this.choices = choices;
        choicesMap = SpecsFactory.newHashMap();
        for (int i = 0; i < choices.size(); i++) {
            choicesMap.put(choices.get(i), i);
        }
        // Add all alias
        for (String aliasName : alias.keySet()) {
            String choice = alias.get(aliasName);
            Integer index = choicesMap.get(choice);
            if (index == null) {
                SpecsLogs.msgInfo("Could not find choice '" + choice + "' for alias '" + aliasName + "'");
                continue;
            }
            choicesMap.put(aliasName, index);
        }
        currentChoice = 0;
    }

    /**
     * Creates a new MultipleChoice instance with the given choices and no aliases.
     *
     * @param choices the list of valid choices
     * @return a new MultipleChoice instance
     */
    public static MultipleChoice newInstance(List<String> choices) {
        Map<String, String> emptyMap = Collections.emptyMap();
        return newInstance(choices, emptyMap);
    }

    /**
     * Creates a new MultipleChoice instance with the given choices and aliases.
     *
     * @param choices the list of valid choices
     * @param alias a map of alias names to choice names
     * @return a new MultipleChoice instance
     */
    public static MultipleChoice newInstance(List<String> choices, Map<String, String> alias) {
        if (choices.isEmpty()) {
            throw new RuntimeException("MultipleChoice needs at least one choice, passed an empty list.");
        }
        return new MultipleChoice(choices, alias);
    }

    /**
     * Sets the current choice to the specified value.
     *
     * @param choice the choice to set as current
     * @return the updated MultipleChoice instance
     */
    public MultipleChoice setChoice(String choice) {
        Integer index = choicesMap.get(choice);
        if (index == null) {
            SpecsLogs.warn("Choice '" + choice + "' not available. Available choices:" + choices);
            return this;
        }
        currentChoice = index;
        return this;
    }

    /**
     * Gets the current choice.
     *
     * @return the current choice
     */
    public String getChoice() {
        return choices.get(currentChoice);
    }

    /**
     * Returns the string representation of the current choice.
     *
     * @return the current choice as a string
     */
    @Override
    public String toString() {
        return getChoice();
    }
}
