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

package org.suikasoft.jOptions.Options;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.utilities.StringList;

/**
 * Utility class for managing a list of files and their associated folder in a DataStore.
 *
 * @author Joao Bispo
 */
public class FileList {

    private static final DataKey<File> KEY_FOLDER = KeyFactory.folder("Folder");
    private static final DataKey<StringList> KEY_FILENAMES = KeyFactory.stringList("Filenames");

    private final DataStore data;

    private static final String DATA_NAME = "FileList DataStore";

    /**
     * Constructs a new FileList with an empty DataStore.
     */
    public FileList() {
        data = DataStore.newInstance(FileList.DATA_NAME);
    }

    /**
     * Returns the StoreDefinition for FileList.
     *
     * @return the StoreDefinition
     */
    public static StoreDefinition getStoreDefinition() {
        return StoreDefinition.newInstance(FileList.DATA_NAME, FileList.KEY_FOLDER, FileList.KEY_FILENAMES);
    }

    /**
     * Returns the option name for the folder.
     *
     * @return the folder option name
     */
    public static String getFolderOptionName() {
        return "Folder";
    }

    /**
     * Returns the option name for the filenames.
     *
     * @return the filenames option name
     */
    public static String getFilesOptionName() {
        return "Filenames";
    }

    /**
     * Returns the list of files represented by this FileList.
     *
     * @return the list of files
     */
    public List<File> getFiles() {
        File baseFolder = data.get(FileList.KEY_FOLDER);
        List<String> filenames = data.get(FileList.KEY_FILENAMES).getStringList();
        List<File> files = new ArrayList<>();
        for (String fileName : filenames) {
            File file = new File(baseFolder, fileName);

            // Verify if file exists
            if (!file.isFile()) {
                SpecsLogs.msgInfo("Could not find file '" + file.getAbsolutePath() + "'");
                continue;
            }

            files.add(file);
        }

        return files;
    }

    /**
     * Decodes a string representation of a FileList into a FileList object.
     *
     * @param string the string representation
     * @return the FileList object
     */
    public static FileList decode(String string) {
        String[] values = string.split(";");

        if (values.length < 1) {
            throw new RuntimeException(
                    "Could not find a value in string '" + string + "'. Does it have at least a single ';'?");
        }

        FileList fileList = new FileList();

        fileList.data.set(FileList.KEY_FOLDER, new File(values[0]));

        List<String> filenames = new ArrayList<>();
        for (int i = 1; i < values.length; i++) {
            filenames.add(values[i]);
        }
        fileList.data.set(FileList.KEY_FILENAMES, new StringList(filenames));

        return fileList;
    }

    /**
     * Returns a string representation of this FileList.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        builder.append(SpecsIo.normalizePath(data.get(FileList.KEY_FOLDER)));

        String filenames = data.get(FileList.KEY_FILENAMES).stream()
                .map(SpecsIo::normalizePath)
                .collect(Collectors.joining(";", ";", ""));

        builder.append(filenames);

        return builder.toString();
    }
}
