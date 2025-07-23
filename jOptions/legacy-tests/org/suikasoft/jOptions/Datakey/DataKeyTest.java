/**
 * Copyright 2016 SPeCS.
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

package org.suikasoft.jOptions.Datakey;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.persistence.XmlPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import com.google.common.collect.Lists;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.utilities.StringList;

public class DataKeyTest {

    @Test
    public void test() {

        String option1Name = "Option1";
        DataKey<StringList> list1 = KeyFactory.object(option1Name, StringList.class);

        // Test methods of simple DataKey
        assertEquals(option1Name, list1.getName());
        assertEquals(StringList.class, list1.getValueClass());
        assertEquals("StringList", list1.getTypeName());
        assertTrue(!list1.getDecoder().isPresent());
        assertTrue(!list1.getDefault().isPresent());

        List<String> defaultList = Arrays.asList("string1", "string2");

        // Test default value
        list1 = list1.setDefault(() -> new StringList(defaultList));
        assertTrue(list1.getDefault().isPresent());
        assertEquals(defaultList, list1.getDefault().get().getStringList());

        // Test decoder
        list1 = list1.setDecoder(value -> new StringList(value));
        String encodedValue = StringList.encode("string1", "string2");
        assertEquals(defaultList, list1.getDecoder().get().decode(encodedValue).getStringList());

        // Test fluent API for object construction
        DataKey<StringList> listWithDefault = KeyFactory
                .object("optionWithDefault", StringList.class)
                .setDefault(() -> new StringList(defaultList));

        assertEquals(defaultList, listWithDefault.getDefault().get().getStringList());

        DataKey<StringList> listWithDecoder = KeyFactory
                .object("optionWithDecoder", StringList.class)
                .setDecoder(value -> new StringList(value));

        assertEquals(defaultList, listWithDecoder.getDecoder().get().decode(encodedValue).getStringList());

        // Test serialization
        StoreDefinition definition = StoreDefinition.newInstance("test", list1, listWithDefault, listWithDecoder);
        XmlPersistence xmlBuilder = new XmlPersistence(definition);
        DataStore store = DataStore.newInstance(definition);
        store.set(list1, StringList.newInstance("string1", "string2"));
        store.set(listWithDefault, StringList.newInstance("stringDef1", "stringDef2"));
        store.set(listWithDecoder, StringList.newInstance("stringDec1", "stringDec2"));

        File testFile = new File("test_store.xml");
        xmlBuilder.saveData(testFile, store);

        DataStore savedStore = xmlBuilder.loadData(testFile);
        SpecsIo.delete(testFile);

        // Using toString() to remove extra information, such as configuration file
        assertEquals(savedStore.toString(), store.toString());

        /*
        DataKey<StringList> list = KeyFactory.object("Option", StringList.class).setDefaultValueV2(new StringList())
        	.setDecoderV2(value -> new StringList(value));

        assertEquals(String.class, s.getValueClass());

        SetupBuilder data = new SimpleSetup("test_data");

        data.setValue(s, "a value");
        assertEquals("a value", data.getValue(s));

        fail("Not yet implemented");
        */
    }

    @Test
    public void testGeneric() {
        String option1Name = "Option1";
        ArrayList<String> instanceExample = Lists.newArrayList("dummy");
        DataKey<ArrayList<String>> list1 = KeyFactory.generic(option1Name, instanceExample);

        // Test methods of simple DataKey
        assertEquals(option1Name, list1.getName());
        assertEquals(instanceExample.getClass(), list1.getValueClass());
        assertTrue(List.class.isAssignableFrom(list1.getValueClass()));
        assertEquals("ArrayList", list1.getTypeName());
        assertTrue(!list1.getDecoder().isPresent());
        assertTrue(!list1.getDefault().isPresent());

        ArrayList<String> defaultList = Lists.newArrayList("string1", "string2");

        // Test default value
        list1 = list1.setDefault(() -> defaultList);
        assertTrue(list1.getDefault().isPresent());
        assertEquals(defaultList, list1.getDefault().get());

        // Test decoder
        list1 = list1.setDecoder(value -> listStringDecode(value));
        String encodedValue = listStringEncode("string1", "string2");
        assertEquals(defaultList, list1.getDecoder().get().decode(encodedValue));

        // Test fluent API for object construction
        DataKey<ArrayList<String>> listWithDefault = KeyFactory
                .generic("optionWithDefault", instanceExample)
                .setDefault(() -> defaultList);

        assertEquals(defaultList, listWithDefault.getDefault().get());

        DataKey<ArrayList<String>> listWithDecoder = KeyFactory
                .generic("optionWithDecoder", instanceExample)
                .setDecoder(value -> listStringDecode(value));

        assertEquals(defaultList, listWithDecoder.getDecoder().get().decode(encodedValue));

        // Test serialization
        StoreDefinition definition = StoreDefinition.newInstance("test", list1, listWithDefault, listWithDecoder);
        XmlPersistence xmlBuilder = new XmlPersistence(definition);
        DataStore store = DataStore.newInstance(definition);
        store.set(list1, Lists.newArrayList("string1", "string2"));
        store.set(listWithDefault, Lists.newArrayList("stringDef1", "stringDef2"));
        store.set(listWithDecoder, Lists.newArrayList("stringDec1", "stringDec2"));

        File testFile = new File("test_store.xml");
        xmlBuilder.saveData(testFile, store);

        DataStore savedStore = xmlBuilder.loadData(testFile);
        SpecsIo.delete(testFile);

        // Using toString() to remove extra information, such as configuration file
        assertEquals(savedStore.toString(), store.toString());
    }

    // TODO: Make this generic for any type of list. Separator can be one of the parameters
    private final static String DEFAULT_SEPARATOR = ",";

    private static String listStringEncode(String... strings) {

        StringJoiner joiner = new StringJoiner(DataKeyTest.DEFAULT_SEPARATOR);
        for (String string : strings) {
            joiner.add(string);
        }
        return joiner.toString();
    }

    private static ArrayList<String> listStringDecode(String string) {
        return Arrays.stream(string.split(DataKeyTest.DEFAULT_SEPARATOR))
                .collect(() -> new ArrayList<>(), (list, element) -> list.add(element),
                        (list1, list2) -> list1.addAll(list2));

    }

}
