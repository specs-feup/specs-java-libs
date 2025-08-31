/*
 * Copyright 2011 SPeCS Research Group.
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
package pt.up.fe.specs.util.utilities;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import pt.up.fe.specs.util.SpecsStrings;

/**
 * Builds a string representation of a scheduling, according to elements and
 * levels.
 * 
 * Ex.: element1 | element2 element3 |
 * 
 * TODO: Instead of building the map iteratively, store the data and build the
 * lines when asked, to use the same space for the elements
 * 
 * @author Joao Bispo
 */
public class ScheduledLinesBuilder {

    public ScheduledLinesBuilder() {
        this.scheduledLines = new HashMap<>();
    }

    public void addElement(String element, int nodeLevel) {
        String line = this.scheduledLines.get(nodeLevel);
        if (line == null) {
            line = "";
        } else {
            line += " | ";
        }

        line += element;

        this.scheduledLines.put(nodeLevel, line);
    }

    @Override
    public String toString() {
        if (this.scheduledLines.isEmpty()) {
            return "";
        }
        int maxLevel = Collections.max(this.scheduledLines.keySet());
        return toString(maxLevel);
    }

    public String toString(int maxLevel) {
        StringBuilder builder = new StringBuilder();
        int numberSize = Integer.toString(maxLevel).length();
        for (int i = 0; i <= maxLevel; i++) {
            String line = this.scheduledLines.get(i);
            if (line == null) {
                line = "---";
            }
            builder.append(SpecsStrings.padLeft(Integer.toString(i), numberSize, '0')).append(" -> ").append(line)
                    .append("\n");
        }

        return builder.toString();
    }

    public Map<Integer, String> getScheduledLines() {
        return this.scheduledLines;
    }

    private final Map<Integer, String> scheduledLines;
}
