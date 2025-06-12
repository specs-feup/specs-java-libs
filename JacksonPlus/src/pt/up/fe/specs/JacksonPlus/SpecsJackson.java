/**
 * Copyright 2020 SPeCS.
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

package pt.up.fe.specs.JacksonPlus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;

/**
 * Wrapper class with utility methods to use Jackson for JSON serialization and deserialization.
 */
public class SpecsJackson {

    /**
     * Reads an object from a JSON file at the given path.
     *
     * @param filePath the path to the JSON file
     * @param clazz the class to deserialize to
     * @param <T> the type of the object
     * @return the deserialized object
     */
    public static <T> T fromFile(String filePath, Class<T> clazz) {
        return fromFile(filePath, clazz, false);
    }

    /**
     * Reads an object from a JSON file at the given path, with optional type info.
     *
     * @param filePath the path to the JSON file
     * @param clazz the class to deserialize to
     * @param hasTypeInfo whether to use type information
     * @param <T> the type of the object
     * @return the deserialized object
     */
    public static <T> T fromFile(String filePath, Class<T> clazz, boolean hasTypeInfo) {
        File file = new File(filePath);
        return fromFile(file, clazz, hasTypeInfo);
    }

    /**
     * Reads an object from a JSON file.
     *
     * @param file the JSON file
     * @param clazz the class to deserialize to
     * @param <T> the type of the object
     * @return the deserialized object
     */
    public static <T> T fromFile(File file, Class<T> clazz) {
        return fromFile(file, clazz, false);
    }

    /**
     * Reads an object from a JSON file, with optional type info.
     *
     * @param file the JSON file
     * @param clazz the class to deserialize to
     * @param hasTypeInfo whether to use type information
     * @param <T> the type of the object
     * @return the deserialized object
     */
    public static <T> T fromFile(File file, Class<T> clazz, boolean hasTypeInfo) {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            ObjectMapper mapper = new ObjectMapper();
            if (hasTypeInfo) {
                PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .build();
                mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
            }
            T object = mapper.readValue(br, clazz);
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads an object from a JSON string.
     *
     * @param string the JSON string
     * @param clazz the class to deserialize to
     * @param <T> the type of the object
     * @return the deserialized object
     */
    public static <T> T fromString(String string, Class<T> clazz) {
        return fromString(string, clazz, false);
    }

    /**
     * Reads an object from a JSON string, with optional type info.
     *
     * @param string the JSON string
     * @param clazz the class to deserialize to
     * @param hasTypeInfo whether to use type information
     * @param <T> the type of the object
     * @return the deserialized object
     */
    public static <T> T fromString(String string, Class<T> clazz, boolean hasTypeInfo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (hasTypeInfo) {
                PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .build();
                mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
            }
            T object = mapper.readValue(string, clazz);
            return object;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes an object to a JSON file.
     *
     * @param object the object to serialize
     * @param file the file to write to
     * @param <T> the type of the object
     */
    public static <T> void toFile(T object, File file) {
        toFile(object, file, false);
    }

    /**
     * Writes an object to a JSON file, with optional type info.
     *
     * @param object the object to serialize
     * @param file the file to write to
     * @param embedTypeInfo whether to embed type information
     * @param <T> the type of the object
     */
    public static <T> void toFile(T object, File file, boolean embedTypeInfo) {
        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            ObjectMapper mapper = new ObjectMapper();
            if (embedTypeInfo) {
                PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .build();
                mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
            }
            mapper.writeValue(bw, object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Serializes an object to a JSON string.
     *
     * @param object the object to serialize
     * @param <T> the type of the object
     * @return the JSON string
     */
    public static <T> String toString(T object) {
        return toString(object, false);
    }

    /**
     * Serializes an object to a JSON string, with optional type info.
     *
     * @param object the object to serialize
     * @param embedTypeInfo whether to embed type information
     * @param <T> the type of the object
     * @return the JSON string
     */
    public static <T> String toString(T object, boolean embedTypeInfo) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            if (embedTypeInfo) {
                PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .build();
                mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
            }
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
