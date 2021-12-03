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

public class EsprimaLoc {

    private final int startLine;
    private final int startCol;
    private final int endLine;
    private final int endCol;

    public EsprimaLoc(int startLine, int startCol, int endLine, int endCol) {
        this.startLine = startLine;
        this.startCol = startCol;
        this.endLine = endLine;
        this.endCol = endCol;
    }

    public static EsprimaLoc newInstance(Map<String, Object> loc) {
        @SuppressWarnings("unchecked")
        var start = (Map<String, Double>) loc.get("start");
        @SuppressWarnings("unchecked")
        var end = (Map<String, Double>) loc.get("end");

        return new EsprimaLoc(start.get("line").intValue(), start.get("column").intValue(), end.get("line").intValue(),
                end.get("column").intValue());
    }

    /**
     * @return the startLine
     */
    public int getStartLine() {
        return startLine;
    }

    /**
     * @return the startCol
     */
    public int getStartCol() {
        return startCol;
    }

    /**
     * @return the endLine
     */
    public int getEndLine() {
        return endLine;
    }

    /**
     * @return the endCol
     */
    public int getEndCol() {
        return endCol;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "EsprimaLoc [startLine=" + startLine + ", startCol=" + startCol + ", endLine=" + endLine + ", endCol="
                + endCol + "]";
    }

}
