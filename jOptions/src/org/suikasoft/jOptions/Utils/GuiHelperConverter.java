/**
 * Copyright 2023 SPeCS.
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

package org.suikasoft.jOptions.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.gui.panels.option.SetupListPanel;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.values.SetupList;

import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.BaseTypes.ListOfSetups;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.guihelper.SetupFieldOptions.DefaultValue;
import pt.up.fe.specs.util.exceptions.NotImplementedException;

/**
 * Utility class for converting GUI helper objects in jOptions.
 * Converts enums that implement {@link SetupFieldEnum} to StoreDefinition.
 */
public class GuiHelperConverter {

    /**
     * Converts a list of setup classes to a list of StoreDefinitions.
     * 
     * @param setups the setup classes to convert
     * @return a list of StoreDefinitions
     */
    public static <T extends Enum<?> & SetupFieldEnum> List<StoreDefinition> toStoreDefinition(
            @SuppressWarnings("unchecked") Class<T>... setups) {
        return toStoreDefinition(Arrays.asList(setups));
    }

    /**
     * Converts a list of setup classes to a list of StoreDefinitions.
     * 
     * @param setups the setup classes to convert
     * @return a list of StoreDefinitions
     */
    public static <T extends Enum<?> & SetupFieldEnum> List<StoreDefinition> toStoreDefinition(List<Class<T>> setups) {
        var converter = new GuiHelperConverter();

        var definitions = new ArrayList<StoreDefinition>();

        for (var setup : setups) {
            definitions.add(converter.convert(setup));
        }

        return definitions;
    }

    /**
     * Converts a single setup class to a StoreDefinition.
     * 
     * @param setup the setup class to convert
     * @return the StoreDefinition
     */
    public <T extends Enum<?> & SetupFieldEnum> StoreDefinition convert(Class<T> setup) {
        var name = setup.getSimpleName();
        var keys = getDataKeys(setup.getEnumConstants());

        return StoreDefinition.newInstance(name, keys);
    }

    /**
     * Converts an array of setup keys to a list of DataKeys.
     * 
     * @param setupKeys the setup keys to convert
     * @return a list of DataKeys
     */
    public <T extends Enum<?> & SetupFieldEnum> List<DataKey<?>> getDataKeys(
            @SuppressWarnings("unchecked") T... setupKeys) {
        var keys = new ArrayList<DataKey<?>>();

        for (var setupKey : setupKeys) {
            keys.add(getDataKey(setupKey));
        }
        return keys;
    }

    /**
     * Converts a single setup key to a DataKey.
     * 
     * @param setupKey the setup key to convert
     * @return the DataKey
     */
    public <T extends Enum<?> & SetupFieldEnum> DataKey<?> getDataKey(T setupKey) {
        var key = getBaseDataKey(setupKey);

        // Set default value (must be immutable)
        if (setupKey instanceof DefaultValue defaultValueProvider) {
            var defaultValue = defaultValueProvider.getDefaultValue();

            if (defaultValue != null) {
                Supplier<?> defaultSupplier = defaultValue::getRawValue;
                key.setDefaultRaw(defaultSupplier);
            }
        }

        return key;
    }

    /**
     * Gets the base DataKey for a setup key.
     * 
     * @param setupKey the setup key
     * @return the base DataKey
     */
    private <T extends Enum<?> & SetupFieldEnum> DataKey<?> getBaseDataKey(T setupKey) {
        return switch (setupKey.getType()) {
            case string -> KeyFactory.string(setupKey.name());
            default -> throw new NotImplementedException(setupKey.getType());
        };

    }

    /**
     * Converts a SetupList to a ListOfSetups.
     * 
     * @param setupList the SetupList to convert
     * @param tasksList the list of task classes
     * @return the ListOfSetups
     */
    public static <T extends Enum<?> & SetupFieldEnum> ListOfSetups toListOfSetups(SetupList setupList,
            List<Class<T>> tasksList) {

        Map<String, Map<String, SetupFieldEnum>> tasksKeys = new HashMap<>();
        for (var taskList : tasksList) {
            var setupName = taskList.getSimpleName();

            var taskKeys = getSetupFields(taskList);
            tasksKeys.put(setupName, taskKeys);
        }
        // System.out.println("TASK LIST: " + tasksKeys);

        var listOfSetups = new ArrayList<SetupData>();
        for (var dataStore : setupList.getDataStores()) {
            // System.out.println("DATASTORE: " + dataStore);

            // Get setup name
            // String setupName = aClass.getEnumConstants()[0].getSetupName();

            var setupName = SetupListPanel.toOriginalEnum(dataStore.getName());

            var setupDataMapping = tasksKeys.get(setupName);
            Objects.requireNonNull(setupDataMapping,
                    () -> "Could not find setup with name '" + setupName + "', available: " + tasksKeys.keySet());

            var oldSetupName = setupDataMapping.values().stream().findFirst()
                    .orElseThrow(() -> new RuntimeException("Expected to find at least one key in the setup"))
                    .getSetupName();
            var setupData = new SetupData(oldSetupName);
            listOfSetups.add(setupData);

            for (var key : dataStore.getKeysWithValues()) {
                var setupField = setupDataMapping.get(key);
                Objects.requireNonNull(setupField,
                        () -> "Could not find key with name '" + key + "', available: " + setupDataMapping.keySet());
                setupData.put(setupField, dataStore.get(key));
            }
        }

        return new ListOfSetups(listOfSetups);
    }

    /**
     * Gets the setup fields for a task class.
     * 
     * @param taskList the task class
     * @return a map of setup field names to SetupFieldEnum objects
     */
    private static <T extends Enum<?> & SetupFieldEnum> Map<String, SetupFieldEnum> getSetupFields(Class<T> taskList) {

        var taskKeys = new HashMap<String, SetupFieldEnum>();

        for (var key : taskList.getEnumConstants()) {
            var keyName = key.name();
            taskKeys.put(keyName, key);
        }

        return taskKeys;
    }
}
