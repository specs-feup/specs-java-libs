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
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.suikasoft.GsonPlus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class SpecsGson {

    public static String toJson(Object object) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> aClass) {
        return new Gson().fromJson(json, aClass);
    }

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

    public static <T> Optional<T> asOptional(JsonElement element, Function<JsonElement, T> mapper) {
        if (element == null) {
            return Optional.empty();
        }

        return Optional.of(mapper.apply(element));
    }
}
