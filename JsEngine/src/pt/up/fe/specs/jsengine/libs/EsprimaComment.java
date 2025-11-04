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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.jsengine.libs;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Represents a comment node in an Esprima AST.
 */
public class EsprimaComment {

    private static final EsprimaComment EMPTY = new EsprimaComment(new HashMap<>());
    static {
        EMPTY.comment.put("type", "Block");
    }

    private final Map<String, Object> comment;

    /**
     * Constructs an EsprimaComment instance with the given comment data.
     * 
     * @param comments a map containing the comment data
     */
    public EsprimaComment(Map<String, Object> comments) {
        this.comment = comments;
    }

    /**
     * Returns an empty EsprimaComment instance.
     * 
     * @return an empty EsprimaComment
     */
    public static EsprimaComment empty() {
        return EMPTY;
    }

    /**
     * Returns a string representation of the comment.
     * 
     * @return the string representation of the comment
     */
    @Override
    public String toString() {
        return comment.toString();
    }

    /**
     * Retrieves the location information of the comment.
     * 
     * @return an EsprimaLoc instance representing the location of the comment
     */
    public EsprimaLoc getLoc() {
        @SuppressWarnings("unchecked")
        var loc = (Map<String, Object>) comment.get("loc");

        if (loc == null) {
            return EsprimaLoc.undefined();
        }

        return EsprimaLoc.newInstance(loc);
    }

    /**
     * Retrieves the contents of the comment.
     * 
     * @return the contents of the comment, or an empty string if not available
     */
    public String getContents() {
        var content = (String) comment.get("value");

        return content != null ? content : "";
    }

    /**
     * Retrieves the type of the comment.
     * 
     * @return the type of the comment
     */
    public String getType() {
        var type = (String) comment.get("type");
        Objects.requireNonNull(type, () -> "Comment should have type");
        return type;
    }

    /**
     * Generates the code representation of the comment based on its type and contents.
     * 
     * @return the code representation of the comment
     */
    public String getCode() {
        if (this == EMPTY) {
            return "";
        }

        var contents = getContents();
        var type = getType();
        switch (type) {
        case "Block":
            return "/*" + contents + "*/";
        case "Line":
            return "//" + contents;
        default:
            throw new NotImplementedException("Not implemented for comments of type '" + type + "'");
        }
    }
}
