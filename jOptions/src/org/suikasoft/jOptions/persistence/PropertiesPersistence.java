/**
 * Copyright 2013 SPeCS Research Group.
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

package org.suikasoft.jOptions.persistence;

import java.io.File;
import java.util.Optional;

import org.suikasoft.jOptions.JOptionKeys;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.gui.panels.app.AppKeys;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.properties.SpecsProperties;

/**
 * @author Joao Bispo
 *
 */
public class PropertiesPersistence implements AppPersistence {

    // Used to check values being loaded
    private final StoreDefinition definition;

    public PropertiesPersistence(StoreDefinition storeDefinition) {
        definition = storeDefinition;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.suikasoft.SuikaApp.Utils.AppPersistence#loadData(java.io.File,
     * java.lang.String, java.util.List)
     */
    @Override
    public DataStore loadData(File file) {

        // Decode file as a .properties file
        var properties = SpecsProperties.newInstance(file).getProperties();

        // Create empty DataStore
        var dataStore = DataStore.newInstance(definition);

        for (var key : properties.keySet()) {
            // Key must be in definition
            @SuppressWarnings("unchecked")
            var dataKey = (DataKey<Object>) definition.getKey(key.toString());

            var value = properties.get(key).toString();
            dataStore.add(dataKey, dataKey.decode(value));
        }

        dataStore.set(AppKeys.CONFIG_FILE, file.getAbsoluteFile());
        dataStore.set(JOptionKeys.CURRENT_FOLDER_PATH, Optional.of(file.getAbsoluteFile().getParent()));
        dataStore.set(JOptionKeys.USE_RELATIVE_PATHS, false);

        return dataStore;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.suikasoft.SuikaApp.Utils.AppPersistence#saveData(java.io.File,
     * org.suikasoft.jOptions.OptionSetup, boolean)
     */
    @Override
    public boolean saveData(File file, DataStore data, boolean keepConfigFile) {
        // When saving, set config file and use relative paths
        data.set(AppKeys.CONFIG_FILE, file.getAbsoluteFile());
        data.set(JOptionKeys.CURRENT_FOLDER_PATH, Optional.of(file.getAbsoluteFile().getParent()));
        data.set(JOptionKeys.USE_RELATIVE_PATHS, true);

        // DataStore to write. Use same name to avoid conflicts
        DataStore storeToSave = getDataStoreToSave(data);
        // DataStore storeToSave = DataStore.newInstance(data.getName(), data);

        boolean result = write(file, storeToSave);

        // Remove
        data.remove(AppKeys.CONFIG_FILE);
        data.remove(JOptionKeys.CURRENT_FOLDER_PATH);
        data.remove(JOptionKeys.USE_RELATIVE_PATHS);

        return result;
    }

    private boolean write(File file, DataStore data) {
        var properties = toProperties(data);
        return SpecsIo.write(file, properties);
    }

    private String toProperties(DataStore data) {
        StringBuilder properties = new StringBuilder();

        for (var key : data.getDataKeysWithValues()) {
            @SuppressWarnings("unchecked")
            var objectKey = (DataKey<Object>) key;
            var value = data.get(key);
            properties.append(key.getName()).append(" = ").append(objectKey.encode(value)).append("\n");
        }

        return properties.toString();
    }

    public static DataStore getDataStoreToSave(DataStore data) {
        Optional<StoreDefinition> def = data.getStoreDefinitionTry();

        if (def.isEmpty()) {
            return DataStore.newInstance(data.getName(), data);
        }

        DataStore storeToSave = data.getStoreDefinitionTry().map(DataStore::newInstance)
                .orElse(DataStore.newInstance(data.getName()));

        for (DataKey<?> key : def.get().getKeys()) {
            // Before it was not being check if key existed or not, and added default
            // values. Will it break stuff not putting the default values?
            if (data.hasValue(key)) {
                storeToSave.setRaw(key, data.get(key));
            }
        }

        return storeToSave;
    }
}
