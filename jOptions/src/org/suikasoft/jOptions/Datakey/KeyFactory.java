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
 * specific language governing permissions and limitations under the License. under the License.
 */

package org.suikasoft.jOptions.Datakey;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.swing.JFileChooser;

import org.suikasoft.jOptions.JOptionKeys;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.Options.FileList;
import org.suikasoft.jOptions.Utils.EnumCodec;
import org.suikasoft.jOptions.gui.panels.option.BooleanPanel;
import org.suikasoft.jOptions.gui.panels.option.DoublePanel;
import org.suikasoft.jOptions.gui.panels.option.EnumMultipleChoicePanel;
import org.suikasoft.jOptions.gui.panels.option.FilePanel;
import org.suikasoft.jOptions.gui.panels.option.IntegerPanel;
import org.suikasoft.jOptions.gui.panels.option.StringListPanel;
import org.suikasoft.jOptions.gui.panels.option.StringPanel;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;
import org.suikasoft.jOptions.storedefinition.StoreDefinitionProvider;
import org.suikasoft.jOptions.values.SetupList;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.utilities.StringList;

public class KeyFactory {

    /**
     * A Boolean DataKey, with default value 'false'.
     * 
     * @param id
     * @return
     */
    public static DataKey<Boolean> bool(String id) {
        return new NormalKey<>(id, Boolean.class)
                .setDefault(() -> Boolean.FALSE)
                .setKeyPanelProvider((key, data) -> new BooleanPanel(key, data))
                .setDecoder(s -> Boolean.valueOf(s));
    }

    /**
     * A String DataKey, without default value.
     * 
     * @param id
     * @return
     */
    public static DataKey<String> string(String id) {
        return new NormalKey<>(id, String.class)
                .setKeyPanelProvider((key, data) -> new StringPanel(key, data))
                .setDecoder(s -> s);

    }

    public static DataKey<String> string(String id, String defaultValue) {
        return string(id).setDefault(() -> defaultValue);
    }

    public static DataKey<Integer> integer(String id, int defaultValue) {
        return new NormalKey<>(id, Integer.class)
                .setDefault(() -> defaultValue)
                .setKeyPanelProvider((key, data) -> new IntegerPanel(key, data))
                .setDecoder(s -> SpecsStrings.decodeInteger(s, () -> 0));

    }

    public static DataKey<Double> double64(String id, double defaultValue) {
        return new NormalKey<>(id, Double.class)
                .setDefault(() -> defaultValue)
                .setKeyPanelProvider((key, data) -> new DoublePanel(key, data))
                .setDecoder(s -> Double.valueOf(s));

    }

    /**
     * Helper method which returns a key for a file that does not have to exist.
     * 
     * @param id
     * @return
     */
    public static DataKey<File> file(String id) {
        return file(id, false, false, false, Collections.emptyList());
    }

    public static DataKey<File> file(String id, String... extensions) {
        return file(id, false, false, false, Arrays.asList(extensions));
    }

    /**
     * A File DataKey, with default value file with current path (.).
     * 
     * <p>
     * If 'isFolder' is true, it will try to create the folder when returning the File instance, even if it does not
     * exist.
     * 
     * @param id
     * @param isFolder
     * @param create
     * @return
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
                .setDecoder(s -> {
                    if (s == null) {
                        return new File("");
                    }
                    return new File(s);
                })
                .setKeyPanelProvider((key, data) -> new FilePanel(key, data, fileChooser, extensions))
                .setCustomGetter(customGetterFile(isFolder, !isFolder, create, exists));
    }

    public static CustomGetter<File> customGetterFile(boolean isFolder, boolean isFile, boolean create,
            boolean exists) {

        return (file, dataStore) -> {
            // System.out.println("RECEIVED:" + file);
            // If an empty path, return an empty path
            if (file.getPath().isEmpty() && !isFolder && isFile && !create) {
                // System.out.println("RETURN 0:" + file);
                /*
                if (!dataStore.get(JOptionKeys.USE_RELATIVE_PATHS)) {
                    return file.getAbsoluteFile();
                }
                */
                return file;
            }

            File currentFile = file;

            // System.out.println("CUSTOM GETTER - CURRENT FOLDER:" +
            // dataStore.getTry(JOptionKeys.CURRENT_FOLDER_PATH));
            // System.out.println("CUSTOM GETTER - MAKE RELATIVE:" + dataStore.get(JOptionKeys.USE_RELATIVE_PATHS));

            // If it has a working folder set
            Optional<String> workingFolder = dataStore.getTry(JOptionKeys.CURRENT_FOLDER_PATH);
            if (workingFolder.isPresent()) {
                // If path is not absolute, create new file with working folder as parent

                if (!currentFile.isAbsolute()) {
                    File parentFolder = new File(workingFolder.get());
                    currentFile = new File(parentFolder, currentFile.getPath());
                }

            }
            // System.out.println("CUSTOM GET FOLDER:" + dataStore.getTry(JOptionKeys.CURRENT_FOLDER_PATH));

            currentFile = processPath(isFolder, isFile, create, currentFile);

            // Check if it exists
            if (exists) {
                if (!currentFile.exists()) {
                    throw new RuntimeException("Path '" + currentFile + "' does not exist");
                }
            }

            // If relative paths is enabled, make relative path with working folder.
            if (workingFolder.isPresent() && dataStore.get(JOptionKeys.USE_RELATIVE_PATHS)) {
                currentFile = new File(SpecsIo.getRelativePath(currentFile, new File(workingFolder.get())));
            }

            if (!dataStore.get(JOptionKeys.USE_RELATIVE_PATHS) && workingFolder.isPresent()) {
                currentFile = SpecsIo.getCanonicalFile(currentFile);
            }

            return currentFile;
        };
    }

    private static File processPath(boolean isFolder, boolean isFile, boolean create, File currentFile) {
        if (isFolder) {
            if (create) {
                return SpecsIo.mkdir(currentFile);
            }

            return currentFile;

        }

        // Is a file
        if (isFile) {
            // Test if it is not a folder
            if (currentFile.isDirectory()) {
                throw new RuntimeException("File key has directory as value: '"
                        + currentFile.getPath() + "')");
            }
        }

        return currentFile;
    }

    /**
     * Creates a key of type StringList, with default value being an empty list.
     */
    public static DataKey<StringList> stringList(String id) {
        return stringList(id, Collections.emptyList());
    }

    /**
     * A generic DataKey without default value.
     * 
     * @param id
     * @param aClass
     * @param defaultValue
     * @return
     */
    public static <T> DataKey<T> object(String id, Class<T> aClass) {
        return new NormalKey<>(id, aClass);
    }

    /**
     * A new OptionDefinition, using a converter with the default separator (;)
     * 
     * @param string
     * @return
     */
    public static DataKey<StringList> stringList(String id, List<String> defaultValue) {

        return new NormalKey<>(id, StringList.class)
                .setDefault(() -> new StringList(defaultValue))
                .setDecoder(value -> new StringList(value))
                .setKeyPanelProvider(StringListPanel::newInstance);
    }

    public static DataKey<StringList> stringList(String optionName, String... defaultValues) {
        return stringList(optionName, Arrays.asList(defaultValues));
    }

    /**
     * TODO: Can be an interesting exercise to see if it pays off to use a class such as FileList that inside uses other
     * keys.
     * 
     * @param optionName
     * @param extension
     * @return
     */
    public static DataKey<FileList> fileList(String optionName, String extension) {

        return KeyFactory.object(optionName, FileList.class).setDefault(() -> new FileList())
                .setStoreDefinition(FileList.getStoreDefinition())
                .setDecoder(FileList::decode);
    }

    /**
     * 
     * 
     * @param id
     * @return
     */
    public static DataKey<File> folder(String id) {
        return file(id, true, false, false, Collections.emptyList());
    }

    /**
     * Creates a key for a folder that must exist. If the given folder does not exist when returning the value, throws
     * an exception.
     * 
     * @param id
     * @return
     */
    public static DataKey<File> existingFolder(String id) {
        return file(id, true, false, true, Collections.emptyList())
                .setDefault(() -> new File("./"));
    }

    /**
     * Creates a key for a folder, sets './' as default value.
     * 
     * @param id
     * @param create
     *            if true, creates the path if it does not exist
     * @return
     */
    public static DataKey<File> folder(String id, boolean create) {
        return KeyFactory.file(id, true, create, false, Collections.emptyList())
                .setDefault(() -> new File("./"));

    }

    // public static DataKey<File> folder(String id, boolean create, String defaultValue) {
    //
    // return file(id, true, create, Collections.emptyList())
    // .setDefault(new File(defaultValue));
    //
    // }

    public static DataKey<SetupList> setupList(String id, List<StoreDefinition> definitions) {
        return object(id, SetupList.class).setDefault(() -> SetupList.newInstance(id, definitions));
    }

    public static DataKey<SetupList> setupList(String id, StoreDefinitionProvider... providers) {
        List<StoreDefinition> definitions = new ArrayList<>();

        for (StoreDefinitionProvider provider : providers) {
            definitions.add(provider.getStoreDefinition());
        }

        return setupList(id, definitions);
    }

    // public static DataKey<DataStore> dataStore(String id, StoreDefinitionProvider provider) {
    public static DataKey<DataStore> dataStore(String id, StoreDefinition definition) {

        return object(id, DataStore.class)
                .setStoreDefinition(definition)
                .setDecoder(s -> {
                    throw new RuntimeException("No decoder for DataStore");
                });
    }

    // public static <T extends DataStoreProvider> DataKey<T> dataStoreProvider(String id, Class<T> aClass,
    // StoreDefinition definition) {
    //
    // return object(id, aClass)
    // .setStoreDefinition(definition);
    // }

    public static <T extends Enum<T>> DataKey<T> enumeration(String id, Class<T> anEnum) {
        return object(id, anEnum)
                .setDefault(() -> anEnum.getEnumConstants()[0])
                .setDecoder(new EnumCodec<>(anEnum))
                .setKeyPanelProvider((key, data) -> new EnumMultipleChoicePanel<>(key, data));
    }

    /**
     * A DataKey that supports types with generics.
     * 
     * <p>
     * Can only store instances that have the same concrete type as the given example instance. You should only creating
     * generic keys of concrete base types (e.g., ArrayList<String>).
     * 
     * @param id
     * @param aClass
     * @param defaultValue
     * @return
     */
    public static <T, E extends T> DataKey<T> generic(String id, E exampleInstance) {
        return new GenericKey<>(id, exampleInstance);
    }

    /**
     * *
     * <p>
     * Can only store instances that have the same concrete type as the given example instance. You should only creating
     * generic keys of concrete base types (e.g., ArrayList<String>) and avoid interfaces or abstract classes (e.g.,
     * List<String>).
     * 
     * @param id
     * @param aClass
     * @return
     */
    // public static <T> DataKey<T> generic(String id, Class<T> aClass) {
    // return new NormalKey<>(id, aClass);
    // }

    //
    // public static <E extends Enum<E>> DataKey<EnumList<E>> enumList(String id, Class<E> enumClass) {
    // EnumList<E> enumList = new EnumList<>(enumClass);
    // return new NormalKey<EnumList<E>>(id, enumList.getClass());
    // }

    // public static DataKey<List<String>> stringList2() {
    // List<String> list = Arrays.asList("asd", "asdas");
    // GenericKey<List<String>> key = new GenericKey<>("", list);
    //
    // return key;
    // }
}
