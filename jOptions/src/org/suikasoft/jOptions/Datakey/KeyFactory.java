/**
 * Copyright 2014 SPeCS.
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

package org.suikasoft.jOptions.Datakey;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Supplier;

import javax.swing.JFileChooser;

import org.suikasoft.jOptions.JOptionKeys;
import org.suikasoft.jOptions.Datakey.customkeys.MultipleChoiceListKey;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.Options.FileList;
import org.suikasoft.jOptions.Utils.EnumCodec;
import org.suikasoft.jOptions.Utils.MultipleChoiceListCodec;
import org.suikasoft.jOptions.gui.panels.option.BooleanPanel;
import org.suikasoft.jOptions.gui.panels.option.DoublePanel;
import org.suikasoft.jOptions.gui.panels.option.EnumMultipleChoicePanel;
import org.suikasoft.jOptions.gui.panels.option.FilePanel;
import org.suikasoft.jOptions.gui.panels.option.FilesWithBaseFoldersPanel;
import org.suikasoft.jOptions.gui.panels.option.IntegerPanel;
import org.suikasoft.jOptions.gui.panels.option.MultipleChoiceListPanel;
import org.suikasoft.jOptions.gui.panels.option.SetupListPanel;
import org.suikasoft.jOptions.gui.panels.option.StringListPanel;
import org.suikasoft.jOptions.gui.panels.option.StringPanel;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionProvider;
import org.suikasoft.jOptions.values.SetupList;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.parsing.StringCodec;
import pt.up.fe.specs.util.utilities.StringList;

/**
 * Factory for creating common {@link DataKey} types and utility methods for key construction.
 *
 * <p>This class provides static methods to create DataKey instances for common types such as Boolean, String, Integer, and more.
 */
public class KeyFactory {

    /**
     * Creates a Boolean {@link DataKey} with a default value of 'false'.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for Boolean values
     */
    public static DataKey<Boolean> bool(String id) {
        return new NormalKey<>(id, Boolean.class)
                .setDefault(() -> Boolean.FALSE)
                .setKeyPanelProvider((key, data) -> new BooleanPanel(key, data))
                .setDecoder(s -> Boolean.valueOf(s));
    }

    /**
     * Creates a String {@link DataKey} without a default value.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for String values
     */
    public static DataKey<String> string(String id) {
        return new NormalKey<>(id, String.class)
                .setKeyPanelProvider((key, data) -> new StringPanel(key, data))
                .setDecoder(s -> s)
                .setDefault(() -> "");
    }

    /**
     * Creates a String {@link DataKey} with a specified default value.
     *
     * @param id the identifier for the key
     * @param defaultValue the default value for the key
     * @return a {@link DataKey} for String values
     */
    public static DataKey<String> string(String id, String defaultValue) {
        return string(id).setDefault(() -> defaultValue);
    }

    /**
     * Creates an Integer {@link DataKey} with a specified default value.
     *
     * @param id the identifier for the key
     * @param defaultValue the default value for the key
     * @return a {@link DataKey} for Integer values
     */
    public static DataKey<Integer> integer(String id, int defaultValue) {
        return integer(id).setDefault(() -> defaultValue);
    }

    /**
     * Creates an Integer {@link DataKey} without a default value.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for Integer values
     */
    public static DataKey<Integer> integer(String id) {
        return new NormalKey<>(id, Integer.class)
                .setKeyPanelProvider((key, data) -> new IntegerPanel(key, data))
                .setDecoder(s -> SpecsStrings.decodeInteger(s, () -> 0))
                .setDefault(() -> 0);
    }

    /**
     * Creates a Long {@link DataKey} with a specified default value.
     *
     * @param id the identifier for the key
     * @param defaultValue the default value for the key
     * @return a {@link DataKey} for Long values
     */
    public static DataKey<Long> longInt(String id, long defaultValue) {
        return longInt(id)
                .setDefault(() -> defaultValue);
    }

    /**
     * Creates a Long {@link DataKey} without a default value.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for Long values
     */
    public static DataKey<Long> longInt(String id) {
        return new NormalKey<>(id, Long.class)
                .setDecoder(s -> SpecsStrings.decodeLong(s, () -> 0L));
    }

    /**
     * Creates a Double {@link DataKey} with a specified default value.
     *
     * @param id the identifier for the key
     * @param defaultValue the default value for the key
     * @return a {@link DataKey} for Double values
     */
    public static DataKey<Double> double64(String id, double defaultValue) {
        return double64(id).setDefault(() -> defaultValue);
    }

    /**
     * Creates a Double {@link DataKey} without a default value.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for Double values
     */
    public static DataKey<Double> double64(String id) {
        return new NormalKey<>(id, Double.class)
                .setKeyPanelProvider((key, data) -> new DoublePanel(key, data))
                .setDecoder(s -> Double.valueOf(s));
    }

    /**
     * Creates a BigInteger {@link DataKey}.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for BigInteger values
     */
    public static DataKey<BigInteger> bigInteger(String id) {
        return new NormalKey<>(id, BigInteger.class)
                .setDecoder(s -> new BigInteger(s));
    }

    /**
     * Creates a {@link DataKey} for a file that does not have to exist.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for File values
     */
    public static DataKey<File> file(String id) {
        return file(id, false, false, false, Collections.emptyList());
    }

    /**
     * Creates a {@link DataKey} for a file with specific extensions.
     *
     * @param id the identifier for the key
     * @param extensions the allowed extensions for the file
     * @return a {@link DataKey} for File values
     */
    public static DataKey<File> file(String id, String... extensions) {
        return file(id, false, false, false, Arrays.asList(extensions));
    }

    /**
     * Creates a {@link DataKey} for a file with various options.
     *
     * @param id the identifier for the key
     * @param isFolder whether the file is a folder
     * @param create whether to create the file if it does not exist
     * @param exists whether the file must exist
     * @param extensions the allowed extensions for the file
     * @return a {@link DataKey} for File values
     */
    private static DataKey<File> file(String id, boolean isFolder, boolean create, boolean exists,
            Collection<String> extensions) {
        int fileChooser;
        if (isFolder) {
            fileChooser = JFileChooser.DIRECTORIES_ONLY;
        } else {
            fileChooser = JFileChooser.FILES_ONLY;
        }

        return new NormalKey<>(id, File.class, () -> new File(""))
                .setDecoder(Codecs.file())
                .setKeyPanelProvider((key, data) -> new FilePanel(key, data, fileChooser, extensions))
                .setCustomGetter(customGetterFile(isFolder, !isFolder, create, exists));
    }

    /**
     * Creates a {@link DataKey} for a path that can be either a file or a folder.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for File values
     */
    public static DataKey<File> path(String id) {
        return path(id, false);
    }

    /**
     * Creates a {@link DataKey} for a path that can be either a file or a folder, with an option to check existence.
     *
     * @param id the identifier for the key
     * @param exists whether the path must exist
     * @return a {@link DataKey} for File values
     */
    public static DataKey<File> path(String id, boolean exists) {
        int fileChooser = JFileChooser.FILES_AND_DIRECTORIES;

        return new NormalKey<>(id, File.class, () -> new File(""))
                .setDecoder(Codecs.file())
                .setKeyPanelProvider((key, data) -> new FilePanel(key, data, fileChooser, Collections.emptyList()))
                .setCustomGetter(customGetterFile(true, true, false, exists));
    }

    /**
     * Custom getter for file paths, with options for folders, files, creation, and existence checks.
     *
     * @param canBeFolder whether the path can be a folder
     * @param canBeFile whether the path can be a file
     * @param create whether to create the path if it does not exist
     * @param exists whether the path must exist
     * @return a custom getter for file paths
     */
    public static CustomGetter<File> customGetterFile(boolean canBeFolder, boolean canBeFile, boolean create,
            boolean exists) {
        return (file, dataStore) -> new File(
                SpecsIo.normalizePath(customGetterFile(file, dataStore, canBeFolder, canBeFile, create, exists)));
    }

    /**
     * Processes file paths with options for folders, files, creation, and existence checks.
     *
     * @param file the file to process
     * @param dataStore the data store containing additional information
     * @param isFolder whether the path is a folder
     * @param isFile whether the path is a file
     * @param create whether to create the path if it does not exist
     * @param exists whether the path must exist
     * @return the processed file
     */
    public static File customGetterFile(File file, DataStore dataStore, boolean isFolder, boolean isFile,
            boolean create, boolean exists) {
        if (file.getPath().isEmpty() && !isFolder && isFile && !create) {
            return file;
        }

        File currentFile = file;

        var workingFolder = dataStore.get(JOptionKeys.CURRENT_FOLDER_PATH);
        if (workingFolder.isPresent()) {
            if (!currentFile.isAbsolute()) {
                File parentFolder = new File(workingFolder.get());
                currentFile = new File(parentFolder, currentFile.getPath());
            }
        }

        currentFile = processPath(isFolder, isFile, create, currentFile);

        if (exists && !currentFile.exists()) {
            throw new RuntimeException("Path '" + currentFile + "' does not exist");
        }

        if (workingFolder.isPresent() && dataStore.get(JOptionKeys.USE_RELATIVE_PATHS)) {
            currentFile = new File(SpecsIo.getRelativePath(currentFile, new File(workingFolder.get())));
        }

        if (!dataStore.get(JOptionKeys.USE_RELATIVE_PATHS) && workingFolder.isPresent()) {
            currentFile = SpecsIo.getCanonicalFile(currentFile);
        }

        return currentFile;
    }

    /**
     * Processes the path with options for folders, files, and creation.
     *
     * @param canBeFolder whether the path can be a folder
     * @param canBeFile whether the path can be a file
     * @param create whether to create the path if it does not exist
     * @param currentFile the current file to process
     * @return the processed file
     */
    private static File processPath(boolean canBeFolder, boolean canBeFile, boolean create, File currentFile) {
        var exists = currentFile.exists();

        if (!exists) {
            if (canBeFolder && create) {
                return SpecsIo.mkdir(currentFile);
            }
            return currentFile;
        }

        if (currentFile.isDirectory() && !canBeFolder) {
            throw new RuntimeException("File key has directory as value and key does not allow it: '"
                    + currentFile.getPath() + "')");
        }

        if (currentFile.isFile() && !canBeFile) {
            throw new RuntimeException("File key has file as value and key does not allow it: '"
                    + currentFile.getPath() + "')");
        }

        return currentFile;
    }

    /**
     * Creates a {@link DataKey} for a {@link StringList} with an empty list as the default value.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for StringList values
     */
    public static DataKey<StringList> stringList(String id) {
        return stringList(id, Collections.emptyList());
    }

    /**
     * Creates a generic {@link DataKey} without a default value.
     *
     * @param id the identifier for the key
     * @param aClass the class of the key's value
     * @return a {@link DataKey} for the specified type
     */
    public static <T> DataKey<T> object(String id, Class<T> aClass) {
        return new NormalKey<>(id, aClass);
    }

    /**
     * Creates an optional {@link DataKey} with an empty optional as the default value.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for Optional values
     */
    @SuppressWarnings("unchecked")
    public static <T> DataKey<Optional<T>> optional(String id) {
        return generic(id, (Optional<T>) Optional.empty())
                .setDefault(() -> Optional.empty());
    }

    /**
     * Creates a {@link DataKey} for a {@link StringList} with predefined values.
     *
     * @param id the identifier for the key
     * @param defaultValue the default value for the key
     * @return a {@link DataKey} for StringList values
     */
    public static DataKey<StringList> stringList(String id, List<String> defaultValue) {
        return new NormalKey<>(id, StringList.class)
                .setDefault(() -> new StringList(defaultValue))
                .setDecoder(StringList.getCodec())
                .setKeyPanelProvider(StringListPanel::newInstance);
    }

    /**
     * Creates a {@link DataKey} for a {@link StringList} with predefined values.
     *
     * @param optionName the identifier for the key
     * @param defaultValues the default values for the key
     * @return a {@link DataKey} for StringList values
     */
    public static DataKey<StringList> stringList(String optionName, String... defaultValues) {
        return stringList(optionName, Arrays.asList(defaultValues));
    }

    /**
     * Creates a {@link DataKey} for a {@link FileList}.
     *
     * @param optionName the identifier for the key
     * @return a {@link DataKey} for FileList values
     */
    public static DataKey<FileList> fileList(String optionName) {
        return KeyFactory.object(optionName, FileList.class).setDefault(() -> new FileList())
                .setStoreDefinition(FileList.getStoreDefinition())
                .setDecoder(FileList::decode);
    }

    /**
     * Creates a {@link DataKey} for a folder.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for File values
     */
    public static DataKey<File> folder(String id) {
        return file(id, true, false, false, Collections.emptyList());
    }

    /**
     * Creates a {@link DataKey} for an existing folder.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for File values
     */
    public static DataKey<File> existingFolder(String id) {
        return file(id, true, false, true, Collections.emptyList())
                .setDefault(() -> new File("./"));
    }

    /**
     * Creates a {@link DataKey} for a folder, with an option to create the folder if it does not exist.
     *
     * @param id the identifier for the key
     * @param create whether to create the folder if it does not exist
     * @return a {@link DataKey} for File values
     */
    public static DataKey<File> folder(String id, boolean create) {
        return KeyFactory.file(id, true, create, false, Collections.emptyList())
                .setDefault(() -> new File("./"));
    }

    /**
     * Creates a {@link DataKey} for a {@link SetupList}.
     *
     * @param id the identifier for the key
     * @param definitions the store definitions for the setup list
     * @return a {@link DataKey} for SetupList values
     */
    public static DataKey<SetupList> setupList(String id, List<StoreDefinition> definitions) {
        return object(id, SetupList.class).setDefault(() -> SetupList.newInstance(id, new ArrayList<>(definitions)))
                .setKeyPanelProvider((key, data) -> new SetupListPanel(key, data, definitions));
    }

    /**
     * Creates a {@link DataKey} for a {@link SetupList} using store definition providers.
     *
     * @param id the identifier for the key
     * @param providers the store definition providers for the setup list
     * @return a {@link DataKey} for SetupList values
     */
    public static DataKey<SetupList> setupList(String id, StoreDefinitionProvider... providers) {
        List<StoreDefinition> definitions = new ArrayList<>();

        for (StoreDefinitionProvider provider : providers) {
            definitions.add(provider.getStoreDefinition());
        }

        return setupList(id, definitions);
    }

    /**
     * Creates a {@link DataKey} for a {@link DataStore}.
     *
     * @param id the identifier for the key
     * @param definition the store definition for the data store
     * @return a {@link DataKey} for DataStore values
     */
    public static DataKey<DataStore> dataStore(String id, StoreDefinition definition) {
        return object(id, DataStore.class)
                .setStoreDefinition(definition)
                .setDecoder(string -> KeyFactory.dataStoreDecoder(string, definition));
    }

    /**
     * Decodes a {@link DataStore} from a string representation.
     *
     * @param string the string representation of the data store
     * @param definition the store definition for the data store
     * @return the decoded {@link DataStore}
     */
    private static DataStore dataStoreDecoder(String string, StoreDefinition definition) {
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(string, new TypeToken<Map<String, String>>() {
            private static final long serialVersionUID = 1L;
        }.getType());

        DataStore dataStore = DataStore.newInstance(definition);
        for (Entry<String, String> entry : map.entrySet()) {
            DataKey<?> key = definition.getKey(entry.getKey());
            Object value = key.decode(entry.getValue());
            dataStore.setRaw(key, value);
        }

        return dataStore;
    }

    /**
     * Creates a {@link DataKey} for an enumeration.
     *
     * @param id the identifier for the key
     * @param anEnum the enumeration class
     * @return a {@link DataKey} for enumeration values
     */
    public static <T extends Enum<T>> DataKey<T> enumeration(String id, Class<T> anEnum) {
        return object(id, anEnum)
                .setDefault(() -> anEnum.getEnumConstants()[0])
                .setDecoder(new EnumCodec<>(anEnum))
                .setKeyPanelProvider((key, data) -> new EnumMultipleChoicePanel<>(key, data));
    }

    /**
     * Creates a {@link DataKey} for a list of enumeration values.
     *
     * @param id the identifier for the key
     * @param anEnum the enumeration class
     * @return a {@link DataKey} for a list of enumeration values
     */
    public static <T extends Enum<T>> DataKey<List<T>> enumerationMulti(String id, Class<T> anEnum) {
        return enumerationMulti(id, anEnum.getEnumConstants());
    }

    /**
     * Creates a {@link DataKey} for a list of enumeration values.
     *
     * @param id the identifier for the key
     * @param enums the enumeration values
     * @return a {@link DataKey} for a list of enumeration values
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<T>> DataKey<List<T>> enumerationMulti(String id, T... enums) {
        SpecsCheck.checkArgument(enums.length > 0, () -> "Must give at least one enum");

        return multiplechoiceList(id, new EnumCodec<>((Class<T>) enums[0].getClass()), Arrays.asList(enums));
    }

    /**
     * Creates a generic {@link DataKey} with a specified default value.
     *
     * @param id the identifier for the key
     * @param exampleInstance an example instance of the key's value
     * @return a {@link DataKey} for the specified type
     */
    public static <T, E extends T> DataKey<T> generic(String id, E exampleInstance) {
        return new GenericKey<>(id, exampleInstance);
    }

    /**
     * Creates a generic {@link DataKey} with a default value supplier.
     *
     * @param id the identifier for the key
     * @param defaultSupplier the supplier for the default value
     * @return a {@link DataKey} for the specified type
     */
    public static <T, E extends T> DataKey<T> generic(String id, Supplier<E> defaultSupplier) {
        DataKey<T> datakey = new GenericKey<>(id, defaultSupplier.get());
        datakey.setDefault(defaultSupplier);
        return datakey;
    }

    /**
     * Creates a {@link DataKey} for a list of values.
     *
     * @param id the identifier for the key
     * @param elementClass the class of the list's elements
     * @return a {@link DataKey} for a list of values
     */
    @SuppressWarnings("unchecked")
    public static <T> DataKey<List<T>> list(String id, Class<T> elementClass) {
        return generic(id, () -> (List<T>) new ArrayList<>())
                .setCustomSetter((value, data) -> KeyFactory.listCustomSetter(value, data, elementClass))
                .setCopyFunction(ArrayList::new)
                .setValueClass(List.class);
    }

    /**
     * Custom setter for lists, ensuring the correct element type.
     *
     * @param value the list to set
     * @param data the data store containing additional information
     * @param elementClass the class of the list's elements
     * @return the processed list
     */
    private static <T> List<T> listCustomSetter(List<?> value, DataStore data, Class<T> elementClass) {
        if (value instanceof ArrayList) {
            return SpecsCollections.cast(value, elementClass).toArrayList();
        }

        List<T> arrayList = new ArrayList<>(value.size());
        for (Object element : value) {
            arrayList.add(elementClass.cast(element));
        }

        return arrayList;
    }

    /**
     * Creates a {@link DataKey} for a map of files with corresponding base folders.
     *
     * @param id the identifier for the key
     * @return a {@link DataKey} for a map of files with base folders
     */
    public static DataKey<Map<File, File>> filesWithBaseFolders(String id) {
        return generic(id, (Map<File, File>) new HashMap<File, File>())
                .setKeyPanelProvider((key, data) -> new FilesWithBaseFoldersPanel(key, data))
                .setDecoder(Codecs.filesWithBaseFolders())
                .setCustomGetter(KeyFactory::customGetterFilesWithBaseFolders)
                .setCustomSetter(KeyFactory::customSetterFilesWithBaseFolders)
                .setDefault(() -> new HashMap<File, File>());
    }

    /**
     * Custom getter for files with base folders, processing paths and base folders.
     *
     * @param value the map of files with base folders
     * @param data the data store containing additional information
     * @return the processed map
     */
    public static Map<File, File> customGetterFilesWithBaseFolders(Map<File, File> value, DataStore data) {
        Map<File, File> processedMap = new HashMap<>();

        for (var entry : value.entrySet()) {
            boolean noBaseFolder = entry.getValue() == null || entry.getValue().toString().isEmpty();
            File newPath = customGetterFile(entry.getKey(), data, false, false, false, true);
            File newBase = noBaseFolder ? null
                    : customGetterFile(entry.getValue(), data, true, false, false, true);

            processedMap.put(newPath, newBase);
        }

        return processedMap;
    }

    /**
     * Custom setter for files with base folders, ensuring relative paths.
     *
     * @param value the map of files with base folders
     * @param data the data store containing additional information
     * @return the processed map
     */
    public static Map<File, File> customSetterFilesWithBaseFolders(Map<File, File> value, DataStore data) {
        Optional<String> workingFolderTry = data.get(JOptionKeys.CURRENT_FOLDER_PATH);
        if (!workingFolderTry.isPresent()) {
            return value;
        }

        File workingFolder = new File(workingFolderTry.get());
        Map<File, File> processedMap = new HashMap<>();

        for (var entry : value.entrySet()) {
            File previousPath = entry.getKey();
            File previousBase = entry.getValue();

            String newBase = entry.getValue() == null ? ""
                    : SpecsIo.getRelativePath(previousBase, workingFolder, true).orElse(previousBase.toString());

            String newPath = SpecsIo.getRelativePath(previousPath, workingFolder, true)
                    .orElse(previousPath.toString());

            processedMap.put(new File(newPath), new File(newBase));
        }

        return processedMap;
    }

    /**
     * Creates a {@link DataKey} for a list of multiple-choice values.
     *
     * @param id the identifier for the key
     * @param codec the codec for encoding and decoding values
     * @param availableChoices the available choices for the key
     * @return a {@link DataKey} for a list of multiple-choice values
     */
    public static <T> DataKey<List<T>> multiplechoiceList(String id, StringCodec<T> codec,
            List<T> availableChoices) {
        SpecsCheck.checkArgument(availableChoices.size() > 0, () -> "Must give at least one element");

        return new MultipleChoiceListKey<>(id, availableChoices)
                .setDecoder(new MultipleChoiceListCodec<>(codec))
                .setKeyPanelProvider(
                        (key, data) -> new MultipleChoiceListPanel<>(key, data));
    }

    /**
     * Creates a {@link DataKey} for a list of multiple-choice string values.
     *
     * @param id the identifier for the key
     * @param availableChoices the available choices for the key
     * @return a {@link DataKey} for a list of multiple-choice string values
     */
    public static DataKey<List<String>> multipleStringList(String id, String... availableChoices) {
        return multipleStringList(id, Arrays.asList(availableChoices));
    }

    /**
     * Creates a {@link DataKey} for a list of multiple-choice string values.
     *
     * @param id the identifier for the key
     * @param availableChoices the available choices for the key
     * @return a {@link DataKey} for a list of multiple-choice string values
     */
    public static DataKey<List<String>> multipleStringList(String id, List<String> availableChoices) {
        return multiplechoiceList(id, s -> s, availableChoices);
    }
}
