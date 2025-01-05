/**
 * Copyright 2012 SPeCS Research Group.
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

package pt.up.fe.specs.util.utilities;

import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author Joao Bispo
 */
public class BuilderWithIndentation {

    public final static String DEFAULT_NEWLINE = "\n";
    public final static String DEFAULT_TAB = "\t";

    // The builder used to create the string
    private final StringBuilder builder;

    private int currentIdentation;
    private final String tab;
    private final String newline;

    public BuilderWithIndentation() {
        this(0, BuilderWithIndentation.DEFAULT_TAB);
    }

    public BuilderWithIndentation(int startIdentation) {
        this(startIdentation, BuilderWithIndentation.DEFAULT_TAB);
    }

    public BuilderWithIndentation(int startIdentation, String tab) {
        builder = new StringBuilder();

        currentIdentation = startIdentation;
        this.tab = tab;
        newline = BuilderWithIndentation.DEFAULT_NEWLINE;
    }

    public BuilderWithIndentation increaseIndentation() {
        currentIdentation += 1;

        return this;
    }

    public BuilderWithIndentation decreaseIndentation() {
        if (currentIdentation == 0) {
            SpecsLogs.warn("Decreasing indentation when its value is already zero.");
            return this;
        }

        currentIdentation -= 1;

        return this;
    }

    public int getCurrentIdentation() {
        return currentIdentation;
    }

    /**
     * Appends the current indentation and the string to the current buffer.
     *
     * @param string
     */
    public BuilderWithIndentation add(String string) {
        // Add identation
        addIndentation();
        builder.append(string);

        return this;
    }

    /**
     * Splits the given string around the newlines and a adds each line.
     *
     * @param lines
     */
    public BuilderWithIndentation addLines(String lines) {
        StringLines.newInstance(lines).stream()
                .forEach(line -> addLine(line));

        return this;
        /*
        try (LineStream reader = LineStream.createLineReader(lines)) {
        for (String line : reader.getIterable()) {
        	addLineHelper(line);
        }
        }
         */
    }

    @Override
    public String toString() {
        return builder.toString();
    }

    /**
     * Appends the current indentation, the string and a newline to the current buffer.
     *
     * @param line
     */
    public BuilderWithIndentation addLine(String line) {
        // Add identation
        addIndentation();
        builder.append(line);
        builder.append(newline);

        return this;
    }

    private void addIndentation() {
        for (int i = 0; i < currentIdentation; i++) {
            builder.append(tab);
        }
    }

}
