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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.jsengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.google.gson.JsonArray;

import pt.up.fe.specs.util.classmap.FunctionClassMap;

/**
 * Abstract base class for JavaScript engine implementations.
 * Defines the contract for engine execution and resource management.
 */
public abstract class AJsEngine implements JsEngine {

    private final FunctionClassMap<Object, Object> toJsRules;

    /**
     * Constructor for AJsEngine.
     * Initializes the rules for converting Java objects to JavaScript objects.
     */
    public AJsEngine() {
        this.toJsRules = new FunctionClassMap<>();

        // Add default rules
        this.toJsRules.put(List.class, this::listToJs);
        this.toJsRules.put(Set.class, this::setToJs);
        this.toJsRules.put(JsonArray.class, this::jsonArrayToJs);
    }

    /**
     * Converts a List to a JavaScript array.
     * Applies the conversion rule to each element of the list.
     * 
     * @param list the List to be converted
     * @return the JavaScript array representation of the List
     */
    private Object listToJs(List<?> list) {
        return toNativeArray(list.stream().map(this::toJs).toArray());
    }

    /**
     * Converts a Set to a JavaScript array.
     * Applies the conversion rule to each element of the Set.
     * 
     * @param set the Set to be converted
     * @return the JavaScript array representation of the Set
     */
    private Object setToJs(Set<?> set) {
        return toNativeArray(set.stream().map(this::toJs).toArray());
    }

    /**
     * Converts a JsonArray to a JavaScript object.
     * Converts the JsonArray to a List and applies the conversion rule.
     * 
     * @param jsonArray the JsonArray to be converted
     * @return the JavaScript object representation of the JsonArray
     */
    private Object jsonArrayToJs(JsonArray jsonArray) {
        var list = new ArrayList<Object>();
        for (int i = 0; i < jsonArray.size(); i++) {
            list.add(jsonArray.get(i));
        }
        return toJs(list);
    }

    /**
     * Adds a custom rule for converting Java objects to JavaScript objects.
     * 
     * @param key the class type of the Java object
     * @param rule the conversion function
     * @param <VS> the type of the Java object
     * @param <KS> the type of the key, which extends VS
     */
    @Override
    public <VS, KS extends VS> void addToJsRule(Class<KS> key, Function<VS, Object> rule) {
        toJsRules.put(key, rule);
    }

    /**
     * Retrieves the rules for converting Java objects to JavaScript objects.
     * 
     * @return the map of conversion rules
     */
    @Override
    public FunctionClassMap<Object, Object> getToJsRules() {
        return toJsRules;
    }
}
