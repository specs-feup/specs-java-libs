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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.NotImplementedException;

import pt.up.fe.specs.util.SpecsCheck;

public class EsprimaComment {

    private static final EsprimaComment EMPTY = new EsprimaComment(new HashMap<>());
    static {
        EMPTY.comment.put("type", "Block");
    }

    private final Map<String, Object> comment;

    public EsprimaComment(Map<String, Object> comments) {
        this.comment = comments;
    }

    public static EsprimaComment empty() {
        return EMPTY;
    }

    @Override
    public String toString() {
        return comment.toString();
    }

    public EsprimaLoc getLoc() {
        @SuppressWarnings("unchecked")
        var loc = (Map<String, Object>) comment.get("loc");

        if (loc == null) {
            return EsprimaLoc.undefined();
        }

        return EsprimaLoc.newInstance(loc);
    }

    public String getContents() {
        var content = (String) comment.get("value");

        return content != null ? content : "";
    }

    public String getType() {
        var type = (String) comment.get("type");
        SpecsCheck.checkNotNull(type, () -> "Comment should have type");
        return type;
    }

    public String getCode() {
        if (this == EMPTY) {
            return "";
        }

        var contents = getContents();
        var type = getType();
        switch (type) {
        case "Block":
            return "/*" + contents + "*/";
        default:
            throw new NotImplementedException("Not implemented for comments of type '" + type + "'");
        }
    }
}
