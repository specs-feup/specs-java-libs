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

package pt.up.fe.specs.eclipse.Tasks.SftpTask;

import java.io.File;

import pt.up.fe.specs.guihelper.FieldType;
import pt.up.fe.specs.guihelper.SetupAccess;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.guihelper.BaseTypes.FieldValue;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.guihelper.SetupFieldOptions.DefaultValue;

/**
 * @author Joao Bispo
 * 
 */
public enum SftpSetup implements SetupFieldEnum, DefaultValue {

    UserLogin(FieldType.string),
    UserPass(FieldType.string),
    Host(FieldType.string),
    Port(FieldType.string),
    DestinationFolder(FieldType.string),
    OutputJarFilename(FieldType.string),
    CommandsFile(FieldType.string);

    public static SftpData newData(SetupData setupData) {
        SetupAccess setup = new SetupAccess(setupData);

        String login = setup.getString(UserLogin);
        String pass = setup.getString(UserPass);
        String host = setup.getString(Host);
        String port = setup.getString(Port);
        String destinationFolder = setup.getString(DestinationFolder);

        String outputJarFilename = setup.getString(OutputJarFilename);
        if (outputJarFilename.trim().isEmpty()) {
            outputJarFilename = null;
        }

        File commandsFile = null;
        if (!setup.getString(CommandsFile).isEmpty()) {
            commandsFile = setup.getExistingFile(CommandsFile);
        }

        return new SftpData(login, pass, host, port, destinationFolder, outputJarFilename, commandsFile);
    }

    private SftpSetup(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    /**
     * INSTANCE VARIABLES
     */
    private final FieldType fieldType;

    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.Base.SetupFieldEnum#getType()
     */
    @Override
    public FieldType getType() {
        return fieldType;
    }

    /* (non-Javadoc)
     * @see pt.up.fe.specs.guihelper.Base.SetupFieldEnum#getSetupName()
     */
    @Override
    public String getSetupName() {
        return "SFTP Task";
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {

        if (this == OutputJarFilename) {
            return super.toString() + " (optional)";
        }

        if (this == SftpSetup.CommandsFile) {
            return super.toString() + " (optional)";
        }

        return super.toString();
    }

    @Override
    public FieldValue getDefaultValue() {
        if (this == Port) {
            return FieldValue.create("22", Port);
        }

        return null;
    }

}
