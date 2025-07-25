/**
 * Copyright 2021 SPeCS.
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

package pt.up.fe.specs.jsengine.libs;

import java.util.Map;

/**
 * Represents the location information for an Esprima AST node.
 */
public class EsprimaLoc {

    private static final EsprimaLoc UNDEFINED = new EsprimaLoc(-1, -1, -1, -1);

    private final int startLine;
    private final int startCol;
    private final int endLine;
    private final int endCol;

    /**
     * Constructs an EsprimaLoc object with the given start and end line/column information.
     * 
     * @param startLine the starting line number
     * @param startCol the starting column number
     * @param endLine the ending line number
     * @param endCol the ending column number
     */
    public EsprimaLoc(int startLine, int startCol, int endLine, int endCol) {
        this.startLine = startLine;
        this.startCol = startCol;
        this.endLine = endLine;
        this.endCol = endCol;
    }

    /**
     * Creates a new instance of EsprimaLoc from a map containing location information.
     * 
     * @param loc a map with "start" and "end" keys containing line and column information
     * @return a new EsprimaLoc object
     */
    public static EsprimaLoc newInstance(Map<String, Object> loc) {
        @SuppressWarnings("unchecked")
        var start = (Map<String, Double>) loc.get("start");
        @SuppressWarnings("unchecked")
        var end = (Map<String, Double>) loc.get("end");

        return new EsprimaLoc(start.get("line").intValue(), start.get("column").intValue(), end.get("line").intValue(),
                end.get("column").intValue());
    }

    /**
     * @return the starting line number
     */
    public int getStartLine() {
        return startLine;
    }

    /**
     * @return the starting column number
     */
    public int getStartCol() {
        return startCol;
    }

    /**
     * @return the ending line number
     */
    public int getEndLine() {
        return endLine;
    }

    /**
     * @return the ending column number
     */
    public int getEndCol() {
        return endCol;
    }

    /**
     * Returns a string representation of the EsprimaLoc object.
     * 
     * @return a string describing the location information
     */
    @Override
    public String toString() {
        return "EsprimaLoc [startLine=" + startLine + ", startCol=" + startCol + ", endLine=" + endLine + ", endCol="
                + endCol + "]";
    }

    /**
     * Returns an undefined EsprimaLoc object.
     * 
     * @return an EsprimaLoc object representing undefined location
     */
    public static EsprimaLoc undefined() {
        return UNDEFINED;
    }

}
