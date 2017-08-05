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
import java.util.Arrays;
import java.util.List;

import org.suikasoft.XStreamPlus.XStreamUtils;

import pt.up.fe.specs.guihelper.Base.SetupDefinition;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.guihelper.Utils.SetupDataKeyValue;
import pt.up.fe.specs.util.SpecsEnums;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.utilities.StringList;

/**
 * Utility methods for GuiHelper API.
 * 
 * @author Joao Bispo
 */
public class GuiHelperUtils {

    /**
     * Tests if a given setupName is the same of a given setupField class.
     * 
     * @param setupFieldClass
     * @param setupName
     * @return
     */
    // public static boolean compare(Class<?> setupFieldEnum, String setupName) {
    public static <E extends Enum<E>> boolean compare(Class<E> setupFieldEnum, String setupName) {
        Object instance = SpecsEnums.getInterfaceFromEnum(setupFieldEnum, SetupFieldEnum.class);
        if (instance == null) {
            return false;
        }

        SetupFieldEnum setupField = (SetupFieldEnum) instance;

        return setupField.getSetupName().equals(setupName);
    }

    /**
     * Helper method, which resets the setup file to null as default.
     * 
     * @param file
     * @param setupData
     * @return
     */
    public static boolean saveData(File file, SetupData setupData) {
        return saveData(file, setupData, false);
    }

    /**
     * Saves the given SetupData to an XML file.
     * 
     * @param file
     * @param setupData
     * @param keepSetupPath
     * @return
     */
    public static boolean saveData(File file, SetupData setupData, boolean keepSetupFile) {
        if (!keepSetupFile) {
            setupData.resetSetupFile();
        }
        return XStreamUtils.write(file, setupData, new SetupDataXml());
    }

    public static SetupData loadData(String contents) {
        SetupData parsedObject = new SetupDataXml().fromXml(contents);
        if (parsedObject == null) {
            SpecsLogs.msgInfo("Could not parse contents into a SetupData object.");
            return null;
        }

        return parsedObject;
    }

    public static SetupData loadData(File file) {
        SetupData parsedObject = XStreamUtils.read(file, new SetupDataXml());
        if (parsedObject == null) {
            SpecsLogs.msgInfo("Could not parse file '" + file.getPath()
                    + "' into a SetupData object.");
            return null;
        }

        // Set setup file
        parsedObject.setSetupFile(file);

        return parsedObject;
    }

    /**
     * Parse the input arguments looking for a folder in the first argument. If found, assumes every file inside folder
     * is a configuration file and launches one program for each file, returning true upon completion.
     * 
     * If arguments have more than one element, or the first argument is not a folder, returns false.
     * 
     * @param args
     * @param app
     * @return
     */
    public static boolean tryBatchMode(String[] args, AppSource app) {
        // If args diff than 1, stop.
        if (args.length != 1) {
            return false;
        }

        File folder = new File(args[0]);
        // If first argument is not a folder, stop.
        if (!folder.isDirectory()) {
            SpecsLogs.msgInfo("Found one input argument '" + folder
                    + "', which is not a folder. Trying to start batch mode?");
            return false;
        }

        // Collect files
        List<File> setupFiles = SpecsIo.getFilesRecursive(folder);
        // For each found file, try to execute program
        int totalFiles = setupFiles.size();
        int currentFile = 1;
        SpecsLogs.msgInfo("Starting batch mode. Found " + totalFiles + " files");
        for (File setupFile : setupFiles) {
            SpecsLogs.msgInfo("Executing '" + setupFile.getName() + "' (" + currentFile + "/"
                    + totalFiles + ")");
            currentFile++;
            try {
                int result = app.newInstance().execute(setupFile);

                // If execution returned a valued different that 0, consider an error occured
                if (result != 0) {
                    return false;
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }

        return true;
    }

    /**
     * Parse the input arguments looking for a configuration file in the first argument.
     * 
     * <p>
     * If found, parses other arguments as key-value pairs, to be replaced in the given setup file, and launches the
     * program returning true upon completion.
     * 
     * If the first argument is not a configuration file, applies default values to the non-defined parameters.
     * 
     * @param args
     * @param app
     * @return
     */
    public static boolean trySingleConfigMode(String[] args, AppSource app) {
        // try {
        // If args equal 0, return false
        if (args.length == 0) {
            return false;
        }

        if (args[0].equals("--multiple-setups")) {
            boolean result = true;

            for (int i = 1; i < args.length; ++i) {
                result &= trySingleConfigMode(new String[] { args[i] }, app);
            }

            return result;
        }

        // Get first argument, check if it is an option.
        if (args[0].indexOf("=") != -1) {
            return tryCommandLineMode(args, app);
        }

        File setupFile = new File(args[0]);
        SetupData setupData = GuiHelperUtils.loadData(setupFile);
        if (setupData == null) {
            return false;
        }

        List<String> arguments = Arrays.asList(args);
        boolean result = tryCommandLineModeInternal(arguments.subList(1, arguments.size()), app, setupData);

        return result;
        // } catch (Exception e) {
        // LoggingUtils.msgInfo("Program stopped. Cause:\n"+e.getMessage());
        // return false;
        // }
    }

    public static boolean tryCommandLineMode(String[] args, AppSource app) {
        // If args equal 0, return false
        if (args.length == 0) {
            return false;
        }

        // Create empty setup data
        SetupData setupData = SetupData.create(app.newInstance().getEnumKeys());

        // Execute command-line mode
        return tryCommandLineModeInternal(Arrays.asList(args), app, setupData);

        // return true;
    }

    private static boolean tryCommandLineModeInternal(List<String> args, AppSource app,
            SetupData setupData) {

        App appInstance = app.newInstance();

        applyChangesToSetupData(setupData, args, appInstance.getEnumKeys());

        // File tempFile = new File(setupFile.getParentFile(),
        // "__temp_config.dat");
        File tempFile = new File(app.getClass().getSimpleName() + "__temp_config.dat");
        GuiHelperUtils.saveData(tempFile, setupData, true);

        // Execute
        // LoggingUtils.msgInfo("Executing configuration '" +
        // setupFile.getName()
        // + "'.");
        SpecsLogs.msgLib("Executing application '" + app.getClass().getSimpleName() + "'.");
        try {
            int result = appInstance.execute(tempFile);
            if (result != 0) {
                return false;
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        tempFile.delete();

        // Success
        return true;
    }

    /**
     * @param setupData
     * @param args
     * @param setupDefinition
     */
    private static void applyChangesToSetupData(SetupData setupData,
            // String[] args) {
            List<String> args, SetupDefinition setupDefinition) {

        for (String arg : args) {

            int index = arg.indexOf("=");
            if (index == -1) {
                SpecsLogs.msgInfo("Problem in key-value '" + arg
                        + "'. Check if key-value is separated by a '='.");
                continue;
            }

            String keyString = arg.substring(0, index);
            List<String> key = Arrays.asList(keyString.split("/"));

            // Discover type of key
            FieldValue fieldValue = setupData.get(key);
            if (fieldValue == null) {
                SpecsLogs.msgInfo("Could not find option with key '" + key + "'");
                System.out.println("Base Keys:" + setupData.keySet());
                continue;
            }
            FieldType fieldType = fieldValue.getType();

            // Object parameter =
            // GuiHelperUtils.parseParameter(args[i].substring(index+1), fieldType);
            String argumentName = arg.substring(index + 1);
            // Object parameter = GuiHelperUtils.parseParameter(arg.substring(index + 1), fieldType);
            Object parameter = GuiHelperUtils.parseParameter(argumentName, fieldType);
            // If null, ignore the argument

            if (parameter == null) {
                // LoggingUtils.msgWarn("Option not supported at command line: '"+keyString+"'");
                continue;
            }

            // Check if field type is map of setups
            /*
            if(fieldType.getRawType() == RawType.MapOfSetups) {
            System.out.println(setupDefinition.getSetupKeys());
            for(SetupFieldEnum field : setupDefinition.getSetupKeys()) {
                if(field.name().equals(argumentName)) {
            	System.out.println("SUSUS");
                }
            }
            /*
            List<Enum> enumList = new ArrayList<Enum>();
            for(SetupFieldEnum en : setupDefinition.getSetupKeys()) {
                Enum e = (Enum)en;
                enumList.add(e);
            }
            EnumUtils.buildMap(enumList);
            System.out.println("GEG");
            */
            // continue;
            // }

            FieldValue value = FieldValue.create(parameter, fieldType);
            SetupDataKeyValue keyValue = new SetupDataKeyValue(key, value);
            keyValue.replaceValue(setupData);
            // (new SetupDataKeyValue(key, value)).replaceValue(setupData);
        }

    }

    /**
     * Parses a String according to the given FiedlType. If the string is empty, returns null.
     * 
     * @param substring
     * @param fieldType
     * @return
     */
    private static Object parseParameter(String parameterString, FieldType fieldType) {
        // If parameter is an empty string, return null

        if (parameterString.isEmpty()) {
            return null;
        }

        switch (fieldType.getRawType()) {
        case String:
            return parameterString;
        case ListOfStrings:
            // Assume that the delimiter is ','
            String[] parameters = parameterString.split(",");
            List<String> stringList = new ArrayList<>();
            for (String param : parameters) {
                param = param.trim();
                stringList.add(param);
            }
            return new StringList(stringList);
        // case MapOfSetups:
        // return parameterString;
        default:
            throw new UnsupportedOperationException("Operation not defined for '"
                    + fieldType.getRawType() + "'");
            // LoggingUtils.msgWarn("Case not defined:" +
            // fieldType.getRawType());
            // return null;
        }

    }
}
