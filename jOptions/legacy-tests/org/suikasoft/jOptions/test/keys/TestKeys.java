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
 * specific language governing permissions and limitations under the License.
 */

package org.suikasoft.jOptions.test.keys;

import java.io.File;
import java.util.List;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.Options.FileList;
import org.suikasoft.jOptions.test.storedefinitions.InnerOptions;
import org.suikasoft.jOptions.test.storedefinitions.InnerOptions2;
import org.suikasoft.jOptions.test.values.MultipleChoices;
import org.suikasoft.jOptions.values.SetupList;

import pt.up.fe.specs.util.utilities.StringList;

public interface TestKeys {

    DataKey<String> A_STRING = KeyFactory.string("A_String", "default");

    DataKey<Boolean> A_BOOLEAN = KeyFactory.bool("A_bool").setDefault(() -> Boolean.TRUE);

    DataKey<StringList> A_STRINGLIST = KeyFactory.stringList("A_string_list")
            .setDefault(() -> StringList.newInstance("default_string1", "default_string2"));

    DataKey<FileList> A_FILELIST = KeyFactory.fileList("Text_files");

    DataKey<DataStore> A_SETUP = KeyFactory.dataStore("A_setup", new InnerOptions().getStoreDefinition());

    DataKey<SetupList> A_SETUP_LIST = KeyFactory.setupList("A_setup_list", new InnerOptions(),
            new InnerOptions2());

    DataKey<MultipleChoices> A_MULTIPLE_OPTION = KeyFactory
            .enumeration("A_multiple_option", MultipleChoices.class)
            .setDefault(() -> MultipleChoices.CHOICE1);

    DataKey<List<MultipleChoices>> A_MULTI_ENUM = KeyFactory
            .enumerationMulti("A_multi_enumeration", MultipleChoices.class);

    DataKey<List<String>> A_MULTI_STRING = KeyFactory
            .multipleStringList("A_multi_string_list", "option1", "option2", "option3");

    DataKey<File> A_FILE = KeyFactory.file("A_file");
}
