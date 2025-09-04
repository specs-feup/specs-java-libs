/**
 * Copyright 2019 SPeCS.
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

package org.suikasoft.GsonPlus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * Utility class for working with Gson serialization and deserialization.
 */
public class SpecsGson {

    /**
     * Serializes an object to a pretty-printed JSON string.
     *
     * @param object the object to serialize
     * @return the JSON string
     */
    public static String toJson(Object object) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(object);
    }

    /**
     * Deserializes a JSON string to an object of the given class.
     *
     * @param json the JSON string
     * @param aClass the class to deserialize to
     * @param <T> the type of the object
     * @return the deserialized object
     */
    public static <T> T fromJson(String json, Class<T> aClass) {
        return new Gson().fromJson(json, aClass);
    }

    /**
     * Deserializes a JSON string to a map.
     *
     * @param json the JSON string
     * @return the deserialized map
     */
    @SuppressWarnings("unchecked")
    public static Map<Object, Object> fromJson(String json) {
        return new Gson().fromJson(json, Map.class);
    }

    /**
     * Converts a JsonElement array to a list using the given mapper function.
     *
     * @param element the JsonElement (must be an array)
     * @param mapper function to map each JsonElement to the desired type
     * @param <T> the type of the list elements
     * @return the list of mapped elements
     */
    public static <T> List<T> asList(JsonElement element, Function<JsonElement, T> mapper) {
        if (!element.isJsonArray()) {
            throw new RuntimeException("Can only be applied to arrays: " + element);
        }
        var array = element.getAsJsonArray();
        List<T> list = new ArrayList<>(array.size());
        for (var arrayElemen : array) {
            list.add(mapper.apply(arrayElemen));
        }
        return list;
    }

    /**
     * Converts a JsonElement to an Optional using the given mapper function.
     *
     * @param element the JsonElement
     * @param mapper function to map the JsonElement to the desired type
     * @param <T> the type of the optional value
     * @return an Optional containing the mapped value, or empty if the element is null
     */
    public static <T> Optional<T> asOptional(JsonElement element, Function<JsonElement, T> mapper) {
        if (element == null) {
            return Optional.empty();
        }
        return Optional.of(mapper.apply(element));
    }
}
