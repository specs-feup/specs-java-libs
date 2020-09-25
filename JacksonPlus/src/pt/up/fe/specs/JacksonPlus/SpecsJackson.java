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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.JacksonPlus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Wrapper class with utility methods to use Jackson.
 * 
 * @author pedro
 *
 */
public class SpecsJackson {

    public static <T> T fromFile(String filePath, Class<T> clazz) {

        File file = new File(filePath);

        return fromFile(file, clazz);
    }

    public static <T> T fromFile(File file, Class<T> clazz) {

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            ObjectMapper mapper = new ObjectMapper();
            T object = mapper.readValue(br, clazz);
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromString(String string, Class<T> clazz) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            T object = mapper.readValue(string, clazz);
            return object;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public static <T> void toFile(T object, File file) {

        try {
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(bw, object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static <T> String toString(T object) {

        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
