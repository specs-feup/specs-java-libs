/*
 * Copyright 2011 SPeCS Research Group.
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

package pt.up.fe.specs.guihelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.Base.SetupFieldUtils;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.ListOfSetups;
import pt.up.fe.specs.guihelper.BaseTypes.RawType;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.io.InputFiles;
import pt.up.fe.specs.util.providers.StringProvider;
import pt.up.fe.specs.util.utilities.StringList;

/**
 * Convenient access to data inside SetupData objects.
 * 
 * <p>
 * Contains getters for the possible types of a SetupData.
 * 
 * @author Joao Bispo
 */
public class SetupAccess {

    /**
     * INSTANCE VARIABLES
     */
    private final SetupData setupData;

    public SetupAccess(SetupData setupData) {
        this.setupData = setupData;
    }

    private static boolean testTypes(FieldType thisType, RawType supposedType) {
        if (thisType.getRawType() != supposedType) {
            SpecsLogs.getLogger().warning(
                    "The given value of type '" + thisType + "' has internal " + "type '" + thisType.getRawType()
                            + "', instead of '" + supposedType + "'");
            return false;
        }

        return true;
    }

    private Object getHelper(SetupFieldEnum setupField, RawType rawType) {

        // if(!SetupFieldUtils.isOfSameType(setupField.getType(), rawType)) {
        if (!testTypes(setupField.getType(), rawType)) {
            // LoggingUtils.getLogger().
            // warning("SetupField '"+setupField+"' is not of same type as "
            SpecsLogs.warn("SetupField '" + setupField + "' is not of same type as "
                    + "the getter internal type '" + rawType + "'.");

            return null;
        }

        // Get value
        FieldValue value = setupData.get(setupField.name());

        // If value is null, get default/empty object
        if (value == null) {
            // value = SetupFieldUtils.newValue(setupField);
            value = SetupFieldUtils.newValue(setupField);
            SpecsLogs.msgLib(SetupData.class.getName() + " did not have a value for field '" + setupField + "'."
                    + " Adding the value '" + value + "' for that field.");
            setupData.put(setupField, value);
        }

        // QUESTION: should we modify setup if value is not found, or generate
        // a new value every time it tries to access it?
        // setupData.put(setupField, value);

        return value.getRawValue();
    }

    public String getString(SetupFieldEnum setupField) {
        Object value = getHelper(setupField, RawType.String);
        // if(value == null) {
        // LoggingUtils.msgWarn("Could not get value.");
        // }
        return (String) value;
    }

    public Boolean getBoolean(SetupFieldEnum setupField) {
        // Object value = getHelper(setupField, RawType.Boolean);
        // Object value = getHelper(setupField, setupField.getType().getRawType());
        Object value = getHelper(setupField, FieldType.bool.getRawType());
        // if(value == null) {
        // LoggingUtils.msgWarn("Could not get value.");
        // }

        Boolean newValue = SpecsStrings.parseBoolean(value.toString());
        if (newValue == null) {
            SpecsLogs.getLogger().info("Could not parse '" + value + "' into an boolean.");
            newValue = SpecsStrings.parseBoolean(RawType.getEmptyValueBoolean());
        }

        return newValue;
    }

    public Integer getInteger(SetupFieldEnum setupField) {
        // Object value = getHelper(setupField, RawType.Integer);
        Object value = getHelper(setupField, FieldType.integer.getRawType());
        // if(value == null) {
        // LoggingUtils.msgWarn("Could not get value.");
        // }

        Integer newValue = SpecsStrings.parseInteger(value.toString());
        if (newValue == null) {
            SpecsLogs.msgInfo("Could not parse '" + value + "' into an integer.");
            newValue = SpecsStrings.parseInteger(RawType.getEmptyValueInteger());
        }

        return newValue;
        // (String) value
        // return (Integer) value;
    }

    public Double getDouble(SetupFieldEnum setupField) {
        Object value = getHelper(setupField, FieldType.doublefloat.getRawType());

        try {
            return Double.valueOf(value.toString());
        } catch (NumberFormatException e) {
            SpecsLogs.msgInfo("Could not parse '" + value.toString() + "' into a double.");
            return Double.valueOf(RawType.getEmptyValueDouble());
        }
        /*
        	Double newValue = ParseUtils.parseDouble(value.toString());
        	if (newValue == null) {
        	    LoggingUtils.msgInfo("Could not parse '" + value.toString() + "' into a double.");
        	    newValue = ParseUtils.parseDouble(RawType.getEmptyValueDouble());
        	}
        
        	return newValue;
        	*/
    }

    /**
     * @param stopaddress
     * @return
     */
    /*
    public Long getLong(SetupFieldEnum setupField) {
    Object value = getHelper(setupField, FieldType.integer.getRawType());
    
    
    Long newValue = ParseUtils.parseLong(value.toString());
    if (newValue == null) {
        LoggingUtils.msgInfo("Could not parse '" + value.toString() + "' into a double.");
        newValue = ParseUtils.parseLong(RawType.EMPTY_VALUE_INTEGER);
    }
    
    return newValue;
    }
    */

    public StringList getStringList(SetupFieldEnum setupField) {
        Object value = getHelper(setupField, RawType.ListOfStrings);
        return (StringList) value;
    }

    public SetupData getSetup(SetupFieldEnum setupField) {
        Object value = getHelper(setupField, RawType.Setup);
        // if(value == null) {
        // LoggingUtils.msgWarn("Could not get value.");
        // }
        return (SetupData) value;
    }

    public ListOfSetups getListOfSetups(SetupFieldEnum setupField) {
        Object value = getHelper(setupField, RawType.MapOfSetups);
        // if(value == null) {
        // LoggingUtils.getLogger().
        // warning("Could not get value.");
        // }
        return (ListOfSetups) value;
    }

    public SetupData getChosenSetupFromList(SetupFieldEnum setupField) {
        Object value = getHelper(setupField, RawType.MapOfSetups);
        // if(value == null) {
        // LoggingUtils.getLogger().
        // warning("Could not get value.");
        // }
        Integer chosenSetup = ((ListOfSetups) value).getPreferredIndex();
        if (chosenSetup == null) {
            SpecsLogs.getLogger().warning("Given " + ListOfSetups.class + " did not have a preferred index.");
            chosenSetup = 0;
        }
        return ((ListOfSetups) value).getMapOfSetups().get(chosenSetup);
    }

    public SetupData getSetupFromList(SetupFieldEnum setupField, int index) {
        Object value = getHelper(setupField, RawType.MapOfSetups);
        // if(value == null) {
        // LoggingUtils.getLogger().
        // warning("Could not get value.");
        // }
        ListOfSetups listOfSetups = (ListOfSetups) value;
        if (!listOfSetups.getMapOfSetups().contains(index)) {
            // Integer chosenSetup = index;
            // if(chosenSetup == null) {
            SpecsLogs.getLogger().warning(
                    "Given " + ListOfSetups.class + " does not have index '" + index + "'. Returning index 0.");
            // chosenSetup = 0;
            index = 0;
        }
        return listOfSetups.getMapOfSetups().get(index);
        // return ((ListOfSetups) value).getMapOfSetups().get(chosenSetup);
    }

    /**
     * 
     * @param map
     *            a table with values
     * @param option
     *            an option
     * @return the folder mapped to the given option. If the folder does not exist and could not be created, returns
     *         null.
     */
    /**
     * 
     * @param setupField
     *            an option
     * @return the folder mapped to the given option. If the folder does not exist and could not be created, returns
     *         null
     */
    public File getFolder(SetupFieldEnum setupField) {
        return getFolder(null, setupField);
    }

    /**
     * Returns a file to the specified folder. The method will try to create the folder, if it does not exist.
     * 
     * @param baseFolder
     * @param setupField
     *            an option
     * @return
     */
    public File getFolder(File baseFolder, SetupFieldEnum setupField) {
        String foldername = getString(setupField);
        // File folder = IoUtils.safeFolder(new File(baseFolder, foldername).getPath());
        File folder = SpecsIo.mkdir(baseFolder, foldername);

        if (folder == null) {
            return null;
        }

        return folder;
    }

    // public static File getExistingFolder(SetupData setup, SetupFieldEnum option) {
    // public static File getExistingFolder(FieldValue option) {
    /**
     * 
     * @param setupField
     *            an option
     * @return the folder mapped to the given option. If the folder does not exist, returns null
     */
    public File getExistingFolder(SetupFieldEnum setupField) {
        File baseFolder = null;
        if (setupData.getSetupFile() != null) {
            baseFolder = setupData.getSetupFile().getParentFile();
        }
        return getExistingFolder(baseFolder, setupField);
    }

    /**
     * Convenience method which throws a RuntimeException if the result is null.
     * 
     * @param setupField
     * @return
     */
    public File getExistingFolder(SetupFieldEnum setupField, String message) {
        File result = getExistingFolder(setupField);

        if (result == null) {
            throw new RuntimeException(message);
        }

        return result;
    }

    public File getExistingFolder(SetupFieldEnum setupField, StringProvider message) {
        return getExistingFolder(setupField, message.getString());
    }

    /**
     * 
     * @param baseFolder
     * @param setupField
     * @return the folder mapped to the given option. If the folder does not exist, returns null
     */
    public File getExistingFolder(File baseFolder, SetupFieldEnum setupField) {

        String foldername = getString(setupField);

        // Check if folder is absolute
        File absfolder = new File(foldername);
        if (absfolder.isAbsolute()) {
            return SpecsIo.existingFolder(foldername);
        }

        File folder = new File(baseFolder, foldername);

        if (!folder.isDirectory()) {
            SpecsLogs.msgInfo("Could not open folder '" + folder.getPath() + "'");
            return null;
        }

        return folder;
    }

    public File getExistingFile(SetupFieldEnum setupField) {
        File baseFolder = null;
        if (setupData.getSetupFile() != null) {
            baseFolder = setupData.getSetupFile().getParentFile();
        }
        return getExistingFile(baseFolder, setupField);
    }

    /**
     * 
     * @param map
     *            a table with values
     * @param option
     *            an option
     * @return the file mapped to the given option. If the file does not exist, returns null.
     */
    // public static File getExistingFile(SetupData setup, SetupFieldEnum option) {
    // public File getExistingFile(SetupFieldEnum setupField) {
    public File getExistingFile(File baseFolder, SetupFieldEnum setupField) {
        // String filename = SetupFieldUtils.getString(setup.get(option));
        // String filename = SetupFieldUtils.getString(option);
        String filename = getString(setupField);

        // First, check if filename is an absolute path, i.e., the filename exists as it is
        File absfile = new File(filename);
        if (absfile.isAbsolute()) {
            return SpecsIo.existingFile(absfile.getPath());
        }

        File file = new File(baseFolder, filename);

        // File newFile = IoUtils.existingFile(filename);
        return SpecsIo.existingFile(file.getPath());

    }

    /**
     * Helper method which assumes the file may not exist yet and the parent folder is the location of the setup file,
     * if available.
     * 
     * @param setupField
     * @return
     */
    // public static File getExistingFile(SetupData setup, SetupFieldEnum option) {
    public File getFile(SetupFieldEnum setupField) {
        return getFile(null, setupField);
    }

    /**
     * Helper method which assumes the file may not exist yet.
     * 
     * @param parentFolder
     * @param setupField
     * @return
     * 
     */
    public File getFile(File parentFolder, SetupFieldEnum setupField) {
        return getFile(parentFolder, setupField, false);
        /*
        	String filename = getString(setupField);
        	File newFile = new File(parentFolder, filename);
        
        	return newFile;
        	*/
    }

    public <T extends Enum<T>> T getEnum(SetupFieldEnum setupField, Class<T> enumType) {

        String enumString = getString(setupField);
        if (enumString == null) {
            SpecsLogs.warn("Given field should be a String.");
            return null;
        }

        if (enumString.isEmpty()) {
            // LoggingUtils.msgLib("Empty string not supported for option '" + setupField + "'.");
            SpecsLogs.msgLib("Empty string, returning the first enum element.");
            return enumType.getEnumConstants()[0];
            // return null;
        }

        T newEnum = SpecsEnums.valueOf(enumType, enumString);

        if (newEnum == null) {
            SpecsLogs.warn("Could not find name '" + enumString + "' in '" + enumType + "'");
            return null;
        }

        // System.out.println("New Enum:"+newEnum);
        // System.out.println("New Enum Class:"+newEnum.getClass());
        return newEnum;
    }

    /**
     * Get enums from a StringList.
     * 
     * @param <T>
     * @param setupField
     * @param enumType
     * @return
     */
    public <T extends Enum<T>> List<T> getEnumList(SetupFieldEnum setupField, Class<T> enumType) {

        StringList stringList = getStringList(setupField);
        if (stringList == null) {
            SpecsLogs.warn("Given field should resolve to StringList.");
            return null;
        }

        List<T> enumList = new ArrayList<>();
        for (String enumString : stringList.getStringList()) {
            T newEnum = SpecsEnums.valueOf(enumType, enumString);

            if (newEnum == null) {
                SpecsLogs.warn("Could not find name '" + enumString + "' in '" + enumType + "'");
                continue;
            }

            enumList.add(newEnum);
        }

        return enumList;
    }

    public InputFiles getInputFilesV2(SetupFieldEnum setupField) {
        File input = getExistingPath(null, setupField);
        return InputFiles.newInstance(SpecsIo.getPath(input));
    }

    /**
     * Convenience method without baseFolder.
     * 
     * @param setupField
     * @return
     */
    public InputFiles getInputFiles(SetupFieldEnum setupField) {
        // String inputPath = getString(setupField);
        // return InputFiles.newInstance(inputPath);
        return getInputFiles(null, setupField);
    }

    /**
     * Parses the given value to see if it is a file or a folder with a list of files.
     * 
     * @param baseFolder
     * @param setupField
     * @return
     */
    public InputFiles getInputFiles(File baseFolder, SetupFieldEnum setupField) {
        // public InputFiles getInputFiles(SetupFieldEnum setupField, String baseFoldername) {
        String inputPath = getString(setupField);
        // String fullInputPath = (new File(baseFoldername+inputPath)).getPath();
        String fullInputPath = (new File(baseFolder, inputPath)).getPath();
        /*
        if(baseFolder == null) {
           fullInputPath = (new File(inputPath)).getPath();
        } else {
           fullInputPath = (new File(baseFolder, inputPath)).getPath();
        }
         * 
         */

        // System.out.println("Full Input Path:"+fullInputPath);
        // System.out.println("Base Foldername:"+baseFoldername);
        // return InputFiles.newInstance(inputPath);
        return InputFiles.newInstance(fullInputPath);
    }

    /**
     * Maps a StringList to a List<String>.
     * 
     * <p>
     * If StringList value is null, returns an empty list.
     * 
     * @param setupField
     * @return
     */
    public List<String> getListOfStrings(SetupFieldEnum setupField) {
        Object value = getHelper(setupField, RawType.ListOfStrings);
        if (value == null) {
            return SpecsFactory.newArrayList();
        }

        return ((StringList) value).getStringList();
    }

    /**
     * - If given path is absolute, uses that path;<br>
     * - Tries to combine global folder with the given path;<br>
     * - Tries to combine the folder of the setup file with the given path;<br>
     * 
     * @param parentFolder
     * @param folder
     * @param existingFolder
     * @return
     */
    public File getFolderV2(File parentFolder, SetupFieldEnum folder, boolean existingFolder) {
        String folderpath = getString(folder);

        return getFolderV2(parentFolder, folderpath, existingFolder);
    }

    public File getFolderV2(SetupFieldEnum folder, boolean existingFolder) {
        return getFolderV2(null, folder, existingFolder);
    }

    private File getFolderV2(File globalFolder, String folderpath, boolean existingFolder) {

        // If empty string, return null
        /*
        if(folderpath.isEmpty()) {
        
        // Warn user if 'existingFolder' is true
        if(existingFolder) {
        	LoggingUtils.msgWarn("Given empty string as path for an existing folder.");
        }
        
        return new File("./");
        //LoggingUtils.msgWarn("What should be done in this case?");
        //folderpath = IoUtils.getWorkingDir().getPath();
        }
        */

        // SetupAccess setup = new SetupAccess(setupData);
        // String folderpath = setup.getString(folder);
        // String folderpath = getString(folder);

        // Try using absolute path
        File absoluteFolder = new File(folderpath);
        if (absoluteFolder.isAbsolute()) {
            return getFolder(null, folderpath, existingFolder);
        }

        // Try using global folder
        if (globalFolder != null) {
            // File matlabInputFilesDirectory = setup.getExistingFolder(globalFolder, existingFolder);
            return getFolder(globalFolder, folderpath, existingFolder);

        }

        /*
        File folder = IoUtils.existingFolder(null, folderpath);
        if (folder != null) {
        if (folder.isAbsolute()) {
        	return folder;
        }
        }
        */

        // Try using setup file location
        if (setupData.getSetupFile() != null) {
            File parent = setupData.getSetupFile().getParentFile();

            return getFolder(parent, folderpath, existingFolder);
        }

        if (existingFolder) {
            return SpecsIo.existingFolder(null, folderpath);
        }

        // return new File(folderpath);
        // Check if it is empty, and return current folder
        // if(folderpath.isEmpty()) {
        // return IoUtils.getWorkingDir();
        // }

        throw new RuntimeException("Could not get folder with path '" + folderpath + "'.");
    }

    /**
     * - If given path is absolute, uses that path;<br>
     * - Tries to combine global folder with the given path;<br>
     * - Tries to combine the folder of the setup file with the given path;<br>
     * - If given path is empty, returns current folder;<br>
     * 
     * TODO: Change name to getPath; Create function that checks if it is a 'file'
     * 
     * @param parentFolder
     * @param file
     * @param existingFile
     * @return
     */
    public File getFile(File parentFolder, SetupFieldEnum file, boolean existingFile) {
        String folderpath = getString(file);

        return getFileV2(parentFolder, folderpath, existingFile);
    }

    private File getFileV2(File parentFolder, String filepath, boolean existingFile) {

        // Try using absolute path
        File absoluteFile = new File(filepath);
        if (absoluteFile.isAbsolute()) {
            return getFileBase(null, filepath, existingFile);
        }

        // Try using global folder
        if (parentFolder != null) {
            return getFileBase(parentFolder, filepath, existingFile);

        }

        // Try using setup file location
        if (setupData.getSetupFile() != null) {
            File parent = setupData.getSetupFile().getParentFile();

            return getFileBase(parent, filepath, existingFile);
        }

        if (existingFile) {
            return SpecsIo.existingFolder(null, filepath);
        }

        // Check if empty
        if (filepath.isEmpty()) {
            return SpecsIo.getWorkingDir();
        }

        if (!existingFile) {
            return new File(filepath);
        }

        throw new RuntimeException("Could not get folder with path '" + filepath + "'.");
    }

    /**
     * @param globalFolder
     * @param filepath
     * @param existingFile
     * @return
     */
    private static File getFileBase(File globalFolder, String filepath, boolean existingFile) {

        File file = new File(globalFolder, filepath);
        if (existingFile) {
            file = SpecsIo.existingFile(file.getPath());
        }

        if (file == null) {
            throw new RuntimeException("Could not get folder '" + file + "'.");
        }

        return file;

    }

    /**
     * @param globalFolder
     * @param folderpath
     * @param b
     * @return
     */
    private static File getFolder(File globalFolder, String folderpath, boolean existingFolder) {

        File folder = null;
        if (existingFolder) {
            folder = SpecsIo.existingFolder(globalFolder, folderpath);
        } else {
            folder = SpecsIo.mkdir(globalFolder, folderpath);
        }

        if (folder == null) {
            throw new RuntimeException("Could not get folder '" + folder + "'.");
        }

        return folder;

    }

    /**
     * @param defaultstackpointervalue
     * @return
     */
    public Integer getIntegerFromString(SetupFieldEnum integer) {
        String integerString = getString(integer);
        if (integerString.isEmpty()) {
            return null;
        }

        return SpecsStrings.decodeInteger(integerString);
    }

    /**
     * If the contents are empty or have only whitespace, returns null.
     * 
     * @param filewithmain
     * @return
     */
    public String getStringOptional(SetupFieldEnum string) {
        String value = getString(string);
        if (value.trim().isEmpty()) {
            return null;
        }

        return value;
    }

    /**
     * @param object
     * @param path
     * @param b
     * @return
     */
    public File getExistingPath(File parentFolder, SetupFieldEnum path) {
        String pathname = getString(path);

        // Try using absolute path
        File absolutePath = new File(pathname);
        if (absolutePath.isAbsolute()) {
            return getPathBase(null, pathname);
        }

        // Try using global folder
        if (parentFolder != null) {
            return getPathBase(parentFolder, pathname);

        }

        // Try using setup file location
        if (setupData.getSetupFile() != null) {
            File parent = setupData.getSetupFile().getParentFile();

            return getPathBase(parent, pathname);
        }

        return SpecsIo.existingPath(pathname);
        /*
        if (existingFile) {
        return IoUtils.existingFolder(null, filepath);
        }
        */

        // throw new RuntimeException("Could not get folder with path '" + filepath + "'.");
    }

    /**
     * @param globalFolder
     * @param filepath
     * @param exists
     * @return
     */
    private static File getPathBase(File globalFolder, String filepath) {

        File file = new File(globalFolder, filepath);

        return SpecsIo.existingPath(file.getPath());

    }

    @Override
    public String toString() {
        return setupData.toString();
    }

}
