/*
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
import org.suikasoft.jOptions.gui.panels.app.AppKeys;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.guihelper.SetupDataXml;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.utilities.LineStream;

/**
 * XML-based implementation of AppPersistence for loading and saving DataStore
 * objects.
 *
 * @author Joao Bispo
 */
public class XmlPersistence implements AppPersistence {

    private final ObjectXml<DataStore> xmlMappings;
    private final Collection<DataKey<?>> options;
    // Used to check values being loaded
    private final StoreDefinition definition;

    /**
     * @deprecated Can only use constructor that receives storeDefinition
     */
    @Deprecated
    public XmlPersistence(Collection<DataKey<?>> options) {
        this(StoreDefinition.newInstance("DefinitionCreatedByXmlPersistence", options));
    }

    /**
     * @deprecated Can only use constructor that receives storeDefinition
     */
    @Deprecated
    public XmlPersistence() {
        this(new ArrayList<>());
    }

    /**
     * Constructs an XmlPersistence with the given StoreDefinition.
     *
     * @param storeDefinition the StoreDefinition to use
     */
    public XmlPersistence(StoreDefinition storeDefinition) {
        options = storeDefinition.getKeys();
        xmlMappings = getObjectXml(storeDefinition);
        definition = storeDefinition;
    }

    /**
     * Adds class mappings to the XML serializer.
     *
     * @param classes the list of classes to add
     */
    public void addMappings(List<Class<?>> classes) {
        xmlMappings.addMappings(classes);
    }

    /**
     * Adds a single class mapping to the XML serializer.
     *
     * @param name   the mapping name
     * @param aClass the class to map
     */
    public void addMapping(String name, Class<?> aClass) {
        xmlMappings.addMappings(name, aClass);
    }

    /**
     * Returns the current XML class mappings.
     *
     * @return a map of mapping names to classes
     */
    public Map<String, Class<?>> getMappings() {
        return xmlMappings.getMappings();
    }

    /**
     * Loads data from the given file into a DataStore object.
     *
     * @param file the file to load
     * @return the loaded DataStore object
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
        }

        // Set AppPersistence
        parsedObject.setPersistence(this);

        parsedObject.set(AppKeys.CONFIG_FILE, file.getAbsoluteFile());
        parsedObject.set(JOptionKeys.CURRENT_FOLDER_PATH, Optional.of(file.getAbsoluteFile().getParent()));
        parsedObject.set(JOptionKeys.USE_RELATIVE_PATHS, false);

        // If no definition defined, show warning and return parsed object
        if (definition == null) {
            return parsedObject;
        }

        // Create DataStore to return
        DataStore dataStore = DataStore.newInstance(definition);

        // Set AppPersistence
        dataStore.setPersistence(this);

        // Check that they refer to the same dataStore
        if (!dataStore.getName().equals(parsedObject.getName())) {
            SpecsLogs.debug("File '" + file + "' refers to the store definition '" + parsedObject.getName()
                    + "', expected '" + dataStore.getName() + "'");
        }

        // ParsedObject is not a properly constructed DataStore, it only has its name
        // and the values
        // Do not use it as a normal DataStore

        // Set values
        for (DataKey<?> dataKey : definition.getKeys()) {
            Optional<?> value = parsedObject.getTry(dataKey);

            value.ifPresent(o -> dataStore.setRaw(dataKey, o));
        }

        // Set configuration file information
        dataStore.set(AppKeys.CONFIG_FILE, file.getAbsoluteFile());
        dataStore.set(JOptionKeys.CURRENT_FOLDER_PATH, Optional.of(file.getAbsoluteFile().getParent()));

        return dataStore;
    }

    /**
     * Loads custom properties from the given file.
     *
     * @param file the file to load
     * @return the loaded DataStore object
     */
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
                    if (!baseProp.value().isEmpty()) {
                        File baseFile = new File(baseProp.value());
                        // If absolute path, just load the file
                        if (baseFile.isAbsolute()) {
                            baseData = loadData(baseFile);
                        }
                        // Otherwise, load relative to the current file
                        else {
                            baseData = loadData(new File(file.getParentFile(), baseProp.value()));
                        }

                    }
                    continue;

                }

                parseCustomPropertiesLine(line, baseData);
            }

        }

        // Set AppPersistence
        baseData.setPersistence(this);

        return baseData;
    }

    /**
     * Represents a custom property with a name and value.
     */
    record CustomProperty(String name, String value) {

        /**
         * Parses a custom property from a line of text.
         *
         * @param line the line to parse
         * @return the parsed CustomProperty
         */
        public static CustomProperty parse(String line) {
            String[] args = line.split("=");
            Preconditions.checkArgument(args.length == 2, "Expected 2 arguments, got " + args.length);

            return new CustomProperty(args[0].trim(), args[1].trim());
        }
    }

    /**
     * Parses a line of custom properties and updates the given DataStore.
     *
     * @param line     the line to parse
     * @param baseData the DataStore to update
     */
    private void parseCustomPropertiesLine(String line, DataStore baseData) {
        if (line.startsWith("//")) {
            return;
        }

        CustomProperty prop = CustomProperty.parse(line);

        DataKey<?> key = definition.getKey(prop.name());

        baseData.setString(key, prop.value());
    }

    /**
     * Loads setup data from the given file.
     *
     * @param file the file to load
     * @return the loaded DataStore object
     */
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

        // Set AppPersistence
        data.setPersistence(this);

        // Add values
        for (Entry<String, FieldValue> entry : parsedObject.getDataset().entrySet()) {
            @SuppressWarnings("unchecked")
            DataKey<Object> key = (DataKey<Object>) definition.getKey(entry.getKey());

            // Try to decode raw value, higher changes of compatibility
            String rawValue = entry.getValue().getRawValue().toString();
            data.set(key, key.getDecoder().get().decode(rawValue));
        }

        return data;
    }

    /**
     * Creates an ObjectXml instance for the given StoreDefinition.
     *
     * @param storeDefinition the StoreDefinition to use
     * @return the created ObjectXml instance
     */
    public static ObjectXml<DataStore> getObjectXml(StoreDefinition storeDefinition) {
        return new DataStoreXml(storeDefinition);
    }

    /**
     * Saves the given DataStore to the specified file.
     *
     * @param file           the file to save to
     * @param data           the DataStore to save
     * @param keepConfigFile whether to keep the configuration file
     * @return true if the save was successful, false otherwise
     */
    @Override
    public boolean saveData(File file, DataStore data, boolean keepConfigFile) {

        // When saving, set config file and use relative paths
        data.set(AppKeys.CONFIG_FILE, file.getAbsoluteFile());
        data.set(JOptionKeys.CURRENT_FOLDER_PATH, Optional.of(file.getAbsoluteFile().getParent()));
        data.set(JOptionKeys.USE_RELATIVE_PATHS, true);

        // DataStore to write. Use same name to avoid conflicts
        DataStore storeToSave = getDataStoreToSave(data);

        boolean result = XStreamUtils.write(file, storeToSave, xmlMappings);

        // Remove
        data.remove(AppKeys.CONFIG_FILE);
        data.remove(JOptionKeys.CURRENT_FOLDER_PATH);
        data.remove(JOptionKeys.USE_RELATIVE_PATHS);

        return result;

    }

    /**
     * Creates a DataStore to save based on the given DataStore.
     *
     * @param data the DataStore to base on
     * @return the created DataStore
     */
    public static DataStore getDataStoreToSave(DataStore data) {
        Optional<StoreDefinition> def = data.getStoreDefinitionTry();

        if (def.isEmpty()) {
            return DataStore.newInstance(data.getName(), data);
        }

        DataStore storeToSave = DataStore.newInstance(data.getName());

        for (DataKey<?> key : def.get().getKeys()) {
            if (data.hasValue(key)) {
                storeToSave.setRaw(key, data.get(key));
            }
        }

        return storeToSave;
    }
}
