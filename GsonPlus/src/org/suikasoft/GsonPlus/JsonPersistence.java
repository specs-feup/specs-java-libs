/**
 * Copyright 2012 SPeCS Research Group.
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

import com.google.gson.Gson;

import pt.up.fe.specs.util.utilities.PersistenceFormat;

/**
 * Implementation of {@link PersistenceFormat} for JSON serialization using Gson.
 */
public class JsonPersistence extends PersistenceFormat {

    private final Gson gson;

    /**
     * Constructs a new JsonPersistence instance with a default Gson object.
     */
    public JsonPersistence() {
        gson = new Gson();
    }

    /**
     * Serializes the given object to a JSON string.
     *
     * @param anObject the object to serialize
     * @return the JSON string
     */
    @Override
    public String to(Object anObject) {
        return gson.toJson(anObject);
    }

    /**
     * Deserializes the given JSON string to an object of the specified class.
     *
     * @param contents the JSON string
     * @param classOfObject the class to deserialize to
     * @param <T> the type of the object
     * @return the deserialized object
     */
    @Override
    public <T> T from(String contents, Class<T> classOfObject) {
        return gson.fromJson(contents, classOfObject);
    }

    /**
     * Returns the file extension for JSON files.
     *
     * @return the string "json"
     */
    @Override
    public String getExtension() {
        return "json";
    }

}
