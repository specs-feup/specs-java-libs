/*
 * Copyright 2011 SPeCS Research Group.
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

import java.io.File;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Represents a list of several Strings.
 * 
 * @author Joao Bispo
 */
public class JsonStringList extends AbstractList<String> {

    // private static final String DEFAULT_SEPARATOR = ";";

    private final List<String> stringList;

    public JsonStringList() {
        this(new ArrayList<String>());
    }

    public JsonStringList(String json) {
        this(decode(json));
    }

    /*
    public static StringCodec<JsonStringList> getCodec() {
        return StringCodec.newInstance(JsonStringList::encode, args -> new JsonStringList(decode(args)));
    }
    
    private static String encode(JsonStringList value) {
        return new Gson().toJson(value.stringList);
    }
    */

    public static StringCodec<List<String>> getCodec() {
        return StringCodec.newInstance(JsonStringList::encode, args -> new JsonStringList(decode(args)));
    }

    private static String encode(List<String> value) {
        if (!(value instanceof JsonStringList)) {
            value = new JsonStringList(value);
        }

        return new Gson().toJson(((JsonStringList) value).stringList);
    }

    private static List<String> decode(String json) {
        // System.out.println("VALUES: " + json);

        List<String> decoded;
        try {
            String[] decodedPrim = new Gson().fromJson(json.strip(), String[].class);
            decoded = Arrays.asList(decodedPrim);
        } catch (JsonSyntaxException e) {
            throw new IllegalArgumentException("Exception while deconding JSON of string list: " + e);
        }

        // System.out.println("LIST: " + decoded);

        return new JsonStringList(decoded);
    }
    //
    // public JsonStringList(Collection<String> JsonStringList) {
    // this(JsonStringList, false);
    // }

    public JsonStringList(Collection<String> JsonStringList) {
        this.stringList = new ArrayList<>();
        this.stringList.addAll(JsonStringList);
        // this.jsonFormat = jsonFormat;
    }

    // TODO: ???
    public <E extends Enum<?>> JsonStringList(Class<E> aClass) {
        this();

        for (E anEnum : aClass.getEnumConstants()) {
            stringList.add(anEnum.name());
        }
    }

    public List<String> getStringList() {
        return stringList;
    }

    @Override
    public String toString() {
        return stringList.toString();
    }

    /**
     * Creates a JsonStringList with the file names from the files on the list passed as parameter.
     * 
     * @param files
     *            - the list of files
     * @return a new JsonStringList instance
     */
    public static JsonStringList newInstanceFromListOfFiles(List<File> files) {

        List<String> strings = SpecsFactory.newArrayList();

        for (File file : files) {

            strings.add(file.getName());
        }

        return new JsonStringList(strings);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((stringList == null) ? 0 : stringList.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JsonStringList other = (JsonStringList) obj;
        if (stringList == null) {
            if (other.stringList != null) {
                return false;
            }
        } else if (!stringList.equals(other.stringList)) {
            return false;
        }
        return true;
    }

    @Override
    public Iterator<String> iterator() {
        return stringList.iterator();
    }

    /**
     * Helper constructor with variadic inputs.
     * 
     * @param string
     * @param string2
     * @return
     */
    public static JsonStringList newInstance(String... values) {
        return new JsonStringList(Arrays.asList(values));
    }

    @Override
    public Stream<String> stream() {
        return getStringList().stream();
    }

    public static DataKey<List<String>> newKey(String id) {
        return newKey(id, new JsonStringList());
    }

    // TODO: ???
    public static DataKey<List<String>> newKey(String id, List<String> defaultValue) {
        return KeyFactory.generic(id, (List<String>) new JsonStringList())
                .setDefault(() -> new JsonStringList(defaultValue))
                // .setDecoder(value -> new StringList(value))
                .setDecoder(JsonStringList.getCodec())
                .setKeyPanelProvider(JsonStringListPanel::newInstance)
                .setCustomSetter(JsonStringList::customSetter);
    }

    private static List<String> customSetter(List<String> value, DataStore data) {
        if (value instanceof JsonStringList) {
            return value;
        }

        return new JsonStringList(value);
    }

    @Override
    public String get(int arg0) {
        return stringList.get(arg0);
    }

    @Override
    public int size() {
        return stringList.size();
    }
}
