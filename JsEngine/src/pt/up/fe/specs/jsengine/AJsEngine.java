/**
 * Copyright 2022 SPeCS.
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

package pt.up.fe.specs.jsengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.google.gson.JsonArray;

import pt.up.fe.specs.util.classmap.FunctionClassMap;

public abstract class AJsEngine implements JsEngine {

    private final FunctionClassMap<Object, Object> toJsRules;

    public AJsEngine() {
        this.toJsRules = new FunctionClassMap<>();

        // Add default rules
        this.toJsRules.put(List.class, this::listToJs);
        this.toJsRules.put(Set.class, this::setToJs);
        this.toJsRules.put(JsonArray.class, this::jsonArrayToJs);
    }

    /**
     * If a List, apply adapt over all elements of the list and convert to array.
     * 
     * @param list
     * @return
     */
    private Object listToJs(List<?> list) {
        return toNativeArray(list.stream().map(this::toJs).toArray());
    }

    /**
     * If a Set, apply adapt over all elements of the Set and convert to array.
     * 
     * @param set
     * @return
     */
    private Object setToJs(Set<?> set) {
        return toNativeArray(set.stream().map(this::toJs).toArray());
    }

    /**
     * If a JsonArray, convert to List and call toJs() again.
     * 
     * @param jsonArray
     * @return
     */
    private Object jsonArrayToJs(JsonArray jsonArray) {
        var list = new ArrayList<Object>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i));
        }
        return toJs(list);
    }

    @Override
    public <VS, KS extends VS> void addToJsRule(Class<KS> key, Function<VS, Object> rule) {
        toJsRules.put(key, rule);
    }

    @Override
    public FunctionClassMap<Object, Object> getToJsRules() {
        return toJsRules;
    }
}
