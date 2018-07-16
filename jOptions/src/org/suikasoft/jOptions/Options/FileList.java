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

package org.suikasoft.jOptions.Options;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;
import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.storedefinition.StoreDefinition;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.utilities.StringList;

/**
 * @author Joao Bispo
 * 
 */
public class FileList {

    private static final DataKey<File> KEY_FOLDER = KeyFactory.folder("Folder");
    private static final DataKey<StringList> KEY_FILENAMES = KeyFactory.stringList("Filenames");

    private final DataStore data;
    // private final String extension;

    private static final String DATA_NAME = "FileList DataStore";

    /**
     * 
     */
    public FileList() {
        // super(getStoreDefinition(optionName));
        data = DataStore.newInstance(FileList.DATA_NAME);
        // this.extension = extension;

        // SetupDefinition def = GenericSetupDefinition.newInstance(optionName,
        // getFolderProvider().getOptionDefinition(),
        // getFilenamesProvider().getOptionDefinition());
        //
        // setSetupTable(SimpleSetup.newInstance(def));
    }

    public static StoreDefinition getStoreDefinition() {
        return StoreDefinition.newInstance(FileList.DATA_NAME, FileList.KEY_FOLDER, FileList.KEY_FILENAMES);
    }

    public static String getFolderOptionName() {
        return "Folder";
    }

    public static String getFilesOptionName() {
        return "Filenames";
    }

    public List<File> getFiles() {
        // SetupOptions setupData = getSetupTable().getA();

        // Get base folder

        File baseFolder = data.get(FileList.KEY_FOLDER);

        // // Get Folder object
        // Folder folder = value(option, Folder.class);
        //
        // // Check if has parent folder to pass
        // File parentFolder = null;
        // if (setup.getSetupFile() != null) {
        // parentFolder = setup.getSetupFile().getParentFolder();
        // }
        //
        // return folder.getFolder(parentFolder);

        // File baseFolder = setupData.folder(getFolderProvider());

        // Get filenames
        //
        // StringList filenamesList = setupData.value(getFilenamesProvider(), StringList.class);
        // List<String> filenames = filenamesList.getStringList();
        List<String> filenames = data.get(FileList.KEY_FILENAMES).getStringList();

        // If list of filenames is empty, and base folder is not null, add all
        // files in base folder, by name
        // if (filenames.isEmpty() && (baseFolder != null)) {
        // List<File> files = IoUtils.getFiles(baseFolder, extension);
        // for (File file : files) {
        // filenames.add(file.getName());
        // }
        // }

        // Build files with full path
        List<File> files = SpecsFactory.newArrayList();
        for (String fileName : filenames) {
            File file = new File(baseFolder, fileName);

            // Verify is file exists
            if (!file.isFile()) {
                SpecsLogs.msgInfo("Could not find file '" + file.getAbsolutePath() + "'");
                continue;
            }

            // Add folder + filename
            files.add(file);
        }

        return files;
    }

    public static FileList decode(String string) {
        String[] values = string.split(";");

        if (values.length < 1) {
            throw new RuntimeException(
                    "Could not find a value in string '" + string + "'. Does it have a at least a singel ';'?");
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

    // public static StringCodec<FileList> codec() {
    //
    // return StringCodec.newInstance(encoder, FileList::decode);
    // }

    /*
    public static OptionDefinitionProvider getFolderProvider() {
    return () -> Folder.newOption(getFolderOptionName(), true);
    
    }
    
    public static OptionDefinitionProvider getFilenamesProvider() {
    return () -> KeyFactory.stringListOld(getFilesOptionName());
    }
    */

}
