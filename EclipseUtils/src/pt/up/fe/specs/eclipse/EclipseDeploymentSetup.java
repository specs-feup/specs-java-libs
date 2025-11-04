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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.eclipse;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import pt.up.fe.specs.eclipse.Tasks.TaskUtils;
import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.SetupAccess;
import pt.up.fe.specs.guihelper.Base.ListOfSetupDefinitions;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.ListOfSetups;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.guihelper.SetupFieldOptions.DefaultValue;
import pt.up.fe.specs.guihelper.SetupFieldOptions.MultipleChoice;
import pt.up.fe.specs.guihelper.SetupFieldOptions.MultipleSetup;
import pt.up.fe.specs.util.properties.SpecsProperties;
import pt.up.fe.specs.util.utilities.StringList;

/**
 * Setup definition for program EclipseDeployment.
 *
 * @author Joao Bispo
 */
public enum EclipseDeploymentSetup implements SetupFieldEnum, MultipleSetup, MultipleChoice, DefaultValue {

    WorkspaceFolder(FieldType.string),
    ProjectName(FieldType.string),
    NameOfOutputJar(FieldType.string),
    ClassWithMain(FieldType.string),
    OutputJarType(FieldType.multipleChoice),
    PomInfoFile(FieldType.string),
    DevelopersXml(FieldType.string),
    ProcessJar(FieldType.bool),
    Tasks(FieldType.setupList);

    public static EclipseDeploymentData newData(SetupData setupData) {
        SetupAccess setup = new SetupAccess(setupData);

        File workspaceFolder = setup.getFolderV2(null, WorkspaceFolder, true);

        String projetName = setup.getString(ProjectName);
        String nameOfOutputJar = setup.getString(NameOfOutputJar);
        Pair<String, String> nameAndVersion = processOuputJarName(nameOfOutputJar);
        nameOfOutputJar = nameAndVersion.getLeft();
        String version = nameAndVersion.getRight();
        String mainClass = setup.getString(ClassWithMain);

        JarType jarType = setup.getEnum(OutputJarType, JarType.class);

        File pomInfoFile = setup.getString(PomInfoFile).strip().isEmpty() ? null
                : setup.getExistingFile(PomInfoFile);
        SpecsProperties pomInfo = pomInfoFile == null ? null : SpecsProperties.newInstance(pomInfoFile);

        File developersXml = setup.getString(DevelopersXml).strip().isEmpty() ? null
                : setup.getExistingFile(DevelopersXml);

        var processJar = setup.getBoolean(ProcessJar);

        ListOfSetups tasks = setup.getListOfSetups(Tasks);

        return new EclipseDeploymentData(workspaceFolder, projetName, nameOfOutputJar, mainClass, jarType, pomInfo,
                developersXml, version, processJar, tasks);
    }

    private static Pair<String, String> processOuputJarName(String nameOfOutputJar) {
        String version = null;

        if (nameOfOutputJar.contains("%BUILD%")) {
            // Generate build number
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmm");
            Date date = new Date(System.currentTimeMillis());
            version = formatter.format(date);
            nameOfOutputJar = nameOfOutputJar.replace("%BUILD%", version);
        }
        return Pair.of(nameOfOutputJar, version);
        // return nameOfOutputJar;
    }

    private EclipseDeploymentSetup(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    @Override
    public FieldType getType() {
        return fieldType;
    }

    @Override
    public String getSetupName() {
        return "EclipseDeployment";
    }

    /**
     * INSTANCE VARIABLES
     */
    private final FieldType fieldType;

    /*
     * (non-Javadoc)
     *
     * @see pt.up.fe.specs.guihelper.SetupFieldOptions.MultipleSetup#getSetups()
     */
    @Override
    public ListOfSetupDefinitions getSetups() {
        /*
         * List<Class<? extends SetupFieldEnum>> setups =
         * FactoryUtils.newArrayList();
         *
         * setups.addAll(TaskUtils.getTasks().keySet()); //
         * setups.add(FtpSetup.class); // setups.add(SftpSetup.class);
         *
         * return ListOfSetupDefinitions.newInstance(setups);
         */
        return getTasksDefinitions();
    }

    public static ListOfSetupDefinitions getTasksDefinitions() {
        List<Class<? extends SetupFieldEnum>> setups = new ArrayList<>();

        setups.addAll(TaskUtils.getTasks().keySet());
        // setups.add(FtpSetup.class);
        // setups.add(SftpSetup.class);

        return ListOfSetupDefinitions.newInstance(setups);
    }

    @Override
    public StringList getChoices() {
        if (this == OutputJarType) {
            return new StringList(JarType.class);
        }

        return null;
    }

    @Override
    public FieldValue getDefaultValue() {
        if (this == OutputJarType) {
            return FieldValue.create(JarType.RepackJar.name(), OutputJarType);
        }

        if (this == ProcessJar) {
            return FieldValue.create(true, ProcessJar);
        }

        return null;
    }
}
