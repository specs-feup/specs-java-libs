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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.util.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Stream;

import pt.up.fe.specs.util.parsing.StringCodec;

/**
 * Represents a list of several Strings.
 *
 * @author Joao Bispo
 */
public class StringList implements Iterable<String> {

    private static final String DEFAULT_SEPARATOR = ";";

    private final List<String> stringList;

    public StringList() {
        this(new ArrayList<>());
    }

    public StringList(String values) {
        this(decode(values));
    }

    public static StringCodec<StringList> getCodec() {
        return StringCodec.newInstance(StringList::encode, StringList::new);
    }

    private static String encode(StringList value) {
        return String.join(StringList.DEFAULT_SEPARATOR, value.stringList);
    }

    private static List<String> decode(String values) {
        if (values == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(values.split(StringList.DEFAULT_SEPARATOR, -1));
    }

    public StringList(Collection<String> stringList) {
        this.stringList = new ArrayList<>();
        this.stringList.addAll(stringList);
    }

    public static String getDefaultSeparator() {
        return StringList.DEFAULT_SEPARATOR;
    }

    public <E extends Enum<?>> StringList(Class<E> aClass) {
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
     * Creates a StringList with the file names from the files on the list passed as
     * parameter.
     *
     * @param files the list of files
     * @return a new StringList instance
     */
    public static StringList newInstanceFromListOfFiles(List<File> files) {

        List<String> strings = new ArrayList<>();

        for (File file : files) {
            strings.add(file.getAbsolutePath());
        }

        return new StringList(strings);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((stringList == null) ? 0 : stringList.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
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
        StringList other = (StringList) obj;
        if (stringList == null) {
            return other.stringList == null;
        } else {
            return stringList.equals(other.stringList);
        }
    }

    @Override
    public Iterator<String> iterator() {
        return stringList.iterator();
    }

    public static String encode(String... strings) {
        StringJoiner joiner = new StringJoiner(StringList.DEFAULT_SEPARATOR);
        for (String string : strings) {
            joiner.add(string);
        }
        return joiner.toString();
    }

    /**
     * Helper constructor with variadic inputs.
     *
     */
    public static StringList newInstance(String... values) {
        return new StringList(Arrays.asList(values));
    }

    public Stream<String> stream() {
        return getStringList().stream();
    }

}
