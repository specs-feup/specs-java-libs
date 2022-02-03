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
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.suikasoft.jOptions.persistence;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.suikasoft.XStreamPlus.ObjectXml;
import org.suikasoft.XStreamPlus.XStreamUtils;
import org.suikasoft.jOptions.JOptionKeys;
import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.app.AppPersistence;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.guihelper.SetupDataXml;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.utilities.LineStream;

/**
 * @author Joao Bispo
 *
 */
public class XmlPersistence implements AppPersistence {

    private final ObjectXml<DataStore> xmlMappings;
    private final Collection<DataKey<?>> options;

    // Used to check values being loaded
    private final StoreDefinition definition;

    /**
     * @param options
     * @deprecated Can only use constructor that receives storeDefinition
     */
    @Deprecated
    public XmlPersistence(Collection<DataKey<?>> options) {
        // this.options = options;
        // // definition = null;
        // definition = StoreDefinition.newInstance("", options);
        // xmlMappings = getObjectXml(definition);
        this(StoreDefinition.newInstance("DefinitionCreatedByXmlPersistence", options));
    }

    /**
     * @param setupDefinition
     */
    /*
    public XmlPersistence(SetupDefinition setupDefinition) {
    this(setupDefinition.getKeys());
    }
    */

    /**
     * @deprecated Can only use constructor that receives storeDefinition
     */
    @Deprecated
    public XmlPersistence() {
        this(new ArrayList<>());
    }

    public XmlPersistence(StoreDefinition storeDefinition) {
        options = storeDefinition.getKeys();
        xmlMappings = getObjectXml(storeDefinition);
        definition = storeDefinition;
    }

    public void addMappings(List<Class<?>> classes) {
        xmlMappings.addMappings(classes);

    }

    public void addMapping(String name, Class<?> aClass) {
        xmlMappings.addMappings(name, aClass);
    }

    public Map<String, Class<?>> getMappings() {
        return xmlMappings.getMappings();
    }

    /* (non-Javadoc)
     * @see org.suikasoft.SuikaApp.Utils.AppPersistence#loadData(java.io.File, java.lang.String, java.util.List)
     */
    @Override
    public DataStore loadData(File file) {
        // Read first line, to check if it is from previous format
        try (LineStream lines = LineStream.newInstance(file)) {
            while (lines.hasNextLine()) {
                String line = lines.nextLine().trim();

                if (line.isEmpty()) {
                    continue;
                }

                // Previous format, for compatibility
                if (line.equals("<SetupData>")) {
                    return loadSetupData(file);
                }

                // Check if
                if (line.startsWith("$base")) {
                    if (definition != null) {
                        return loadCustomProperties(file);
                    }

                    throw new RuntimeException(
                            "Custom properties not supported when persistence does not define a StoreDefinition");
                }

            }
        }

        DataStore parsedObject = XStreamUtils.read(file, xmlMappings);
        if (parsedObject == null) {
            throw new RuntimeException("Could not parse file '" + file.getPath()
                    + "' as a DataStore .");
            // LoggingUtils.msgInfo("Could not parse file '" + file.getPath()
            // + "' into a OptionSetup object.");
            // return null;
        }

        parsedObject.set(JOptionKeys.CURRENT_FOLDER_PATH, Optional.of(file.getAbsoluteFile().getParent()));
        parsedObject.set(JOptionKeys.USE_RELATIVE_PATHS, false);

        // If no definition defined, show warning and return parsed object
        if (definition == null) {
            // LoggingUtils
            // .msgWarn(
            // "Using XmlPersistence without a StoreDefinition, customizations to the keys (e.g., KeyPanels, custom
            // getters) will be lost");
            // parsedObject.setSetupFile(file);
            // When loading DataStore, use absolute paths

            return parsedObject;
        }

        // Create DataStore to return
        DataStore dataStore = DataStore.newInstance(definition);

        // Check that they refer to the same dataStore
        if (!dataStore.getName().equals(parsedObject.getName())) {
            SpecsLogs.debug("File '" + file + "' refers to the store definition '" + parsedObject.getName()
                    + "', expected '" + dataStore.getName() + "'");
        }

        // ParsedObject is not a properly constructed DataStore, it only has its name and the values
        // Do not use it as a normal DataStore

        // When loading DataStore, use absolute paths
        // parsedObject.set(JOptionKeys.CURRENT_FOLDER_PATH, file.getAbsoluteFile().getParent());
        // parsedObject.set(JOptionKeys.USE_RELATIVE_PATHS, false);

        // System.out.println("PARSED OBJECT FOLDER:" + parsedObject.get(JOptionKeys.CURRENT_FOLDER_PATH));

        // Set values
        for (DataKey<?> dataKey : definition.getKeys()) {
            Optional<?> value = parsedObject.getTry(dataKey);
            // Object value = parsedObject.getValuesMap().get(dataKey.getName());

            if (value.isPresent()) {
                dataStore.setRaw(dataKey, value.get());
            }
        }

        // Set configuration file information
        dataStore.set(JOptionKeys.CURRENT_FOLDER_PATH, Optional.of(file.getAbsoluteFile().getParent()));
        // dataStore.set(JOptionKeys.USE_RELATIVE_PATHS, false);

        // dataStore.set(parsedObject);
        // dataStore.getKeys().stream()
        // .forEach(key -> System.out.println("DATASTORE PANEL2:" + key.getKeyPanelProvider()));
        // Set setup file
        // parsedObject.getSetupFile().setFile(file);
        // dataStore.setSetupFile(file);

        return dataStore;
    }

    private DataStore loadCustomProperties(File file) {

        DataStore baseData = DataStore.newInstance(definition);

        // Read first line, to check if it is from previous format
        try (LineStream lines = LineStream.newInstance(file)) {
            while (lines.hasNextLine()) {

                String line = lines.nextNonEmptyLine();
                if (line == null) {
                    break;
                }

                if (line.startsWith(".base")) {
                    CustomProperty baseProp = CustomProperty.parse(line);

                    // Check if there is a filename
                    if (!baseProp.getValue().isEmpty()) {
                        File baseFile = new File(baseProp.getValue());
                        // If absolute path, just load the file
                        if (baseFile.isAbsolute()) {
                            baseData = loadData(baseFile);
                        }
                        // Otherwise, load relative to the current file
                        else {
                            baseData = loadData(new File(file.getParentFile(), baseProp.getValue()));
                        }

                    }
                    continue;

                }

                parseCustomPropertiesLine(line, baseData);
            }

        }

        return baseData;
    }

    static class CustomProperty {
        private final String name;
        private final String value;

        public CustomProperty(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }

        public static CustomProperty parse(String line) {
            String[] args = line.split("=");
            Preconditions.checkArgument(args.length == 2, "Expected 2 arguments, got " + args.length);

            return new CustomProperty(args[0].trim(), args[1].trim());
        }
    }

    private void parseCustomPropertiesLine(String line, DataStore baseData) {
        if (line.startsWith("//")) {
            return;
        }

        CustomProperty prop = CustomProperty.parse(line);

        DataKey<?> key = definition.getKey(prop.getName());

        baseData.setString(key, prop.getValue());
    }

    private DataStore loadSetupData(File file) {
        SpecsLogs.msgInfo("!Found old version of configuration file, trying to translate it");
        SetupData parsedObject = XStreamUtils.read(file, new SetupDataXml());
        if (parsedObject == null) {
            SpecsLogs.msgInfo("Could not parse file '" + file.getPath()
                    + "' into a SetupData object.");
            return null;
        }

        StoreDefinition definition = StoreDefinition.newInstance(parsedObject.getSetupName(), options);
        DataStore data = DataStore.newInstance(definition);

        // Add values
        for (Entry<String, FieldValue> entry : parsedObject.getDataset().entrySet()) {
            @SuppressWarnings("unchecked")
            DataKey<Object> key = (DataKey<Object>) definition.getKey(entry.getKey());

            // Try to decode raw value, higher changes of compatibility
            String rawValue = entry.getValue().getRawValue().toString();
            data.set(key, key.getDecoder().get().decode(rawValue));
        }

        // Set setup file
        // parsedObject.setSetupFile(file);
        // data.setSetupFile(file);

        return data;
    }

    // public static ObjectXml<DataStore> getObjectXml(Collection<DataKey<?>> keys) {
    public static ObjectXml<DataStore> getObjectXml(StoreDefinition storeDefinition) {
        return new DataStoreXml(storeDefinition);
    }

    /* (non-Javadoc)
     * @see org.suikasoft.SuikaApp.Utils.AppPersistence#saveData(java.io.File, org.suikasoft.jOptions.OptionSetup, boolean)
     */
    @Override
    public boolean saveData(File file, DataStore data, boolean keepConfigFile) {

        // Reset setup file
        // if (!keepConfigFile) {
        // data.setSetupFile((SetupFile) null);
        // }

        // When saving, set config file and use relative paths
        data.set(JOptionKeys.CURRENT_FOLDER_PATH, Optional.of(file.getAbsoluteFile().getParent()));
        data.set(JOptionKeys.USE_RELATIVE_PATHS, true);

        // DataStore to write. Use same name to avoid conflicts
        DataStore storeToSave = getDataStoreToSave(data);
        // DataStore storeToSave = DataStore.newInstance(data.getName(), data);

        boolean result = XStreamUtils.write(file, storeToSave, xmlMappings);

        // Remove
        data.remove(JOptionKeys.CURRENT_FOLDER_PATH);
        data.remove(JOptionKeys.USE_RELATIVE_PATHS);

        return result;

    }

    public static DataStore getDataStoreToSave(DataStore data) {
        Optional<StoreDefinition> def = data.getStoreDefinitionTry();

        if (!def.isPresent()) {
            return DataStore.newInstance(data.getName(), data);
        }

        DataStore storeToSave = DataStore.newInstance(data.getName());

        for (DataKey<?> key : def.get().getKeys()) {
            // Before it was not being check if key existed or not, and added default values.
            // Will it break stuff not putting the default values?
            if (data.hasValue(key)) {
                // Should not encode values before saving, this will be a normal DataStore
                // XStream will try to serialize the contents
                storeToSave.setRaw(key, data.get(key));
            }
        }

        return storeToSave;
    }
}
