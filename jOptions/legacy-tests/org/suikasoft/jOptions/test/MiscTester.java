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

package org.suikasoft.jOptions.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.persistence.XmlPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.test.keys.AnotherTestKeys;
import org.suikasoft.jOptions.test.storedefinitions.InnerOptions;

import pt.up.fe.specs.util.SpecsIo;

public class MiscTester {

    @Test
    public void test() {
        DataStore store = DataStore.newInstance("Test");
        DataKey<File> folderKey = KeyFactory.folder("Folder_option");
        store.add(folderKey, new File("Bin"));

        // store.setSetupFile(new SetupFile().setFile(new File("c:/tom/setup.config")));

        System.out.println(store.get(folderKey).getAbsolutePath());
        // fail("Not yet implemented");

        // Classes
        // SetupList.newKey("setup list", new InnerOptions(), new InnerOptions2());
        // SetupList.newKey("setup list", EnumInnerOptions.values()[0], EnumInnerOptions2.values()[0]);

        // Enums

        StoreDefinition def = new InnerOptions().getStoreDefinition();
        DataStore store2 = DataStore.newInstance(new InnerOptions().getStoreDefinition());

        store2.set(AnotherTestKeys.ANOTHER_STRING, "HEY");

        // Get panel provider for String
        // String expectedProvider = null;
        // for (DataKey<?> key : def.getKeys()) {
        // expectedProvider = key.getKeyPanelProvider().toString();
        // }

        File file = new File("./jtest_config.xml");
        AppPersistence persistence = new XmlPersistence(new InnerOptions().getStoreDefinition());
        persistence.saveData(file, store2);
        DataStore store3 = persistence.loadData(file);
        SpecsIo.delete(file);

        for (DataKey<?> key : def.getKeys()) {
            assertTrue(store2.get(key).equals(store3.get(key)));
        }

    }

}
