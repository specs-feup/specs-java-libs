/**
 * Copyright 2019 SPeCS.
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

package pt.up.fe.specs.ant.tasks;

import java.io.File;

import org.suikasoft.jOptions.Datakey.DataKey;
import org.suikasoft.jOptions.Datakey.KeyFactory;

import pt.up.fe.specs.ant.AAntTask;
import pt.up.fe.specs.ant.AntResource;
import pt.up.fe.specs.ant.SpecsAnt;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.utilities.Replacer;

public class Sftp extends AAntTask {

    public static final DataKey<String> LOGIN = KeyFactory.string("login");
    public static final DataKey<String> PASS = KeyFactory.string("pass");
    public static final DataKey<String> HOST = KeyFactory.string("host");
    public static final DataKey<String> PORT = KeyFactory.string("port");
    public static final DataKey<String> DESTINATION_FOLDER = KeyFactory.string("destinatonFolder");
    public static final DataKey<File> FILE_TO_TRANSFER = KeyFactory.file("fileToTransfer");
    public static final DataKey<String> NEW_FILENAME = KeyFactory.string("newFilename");
    public static final DataKey<File> COMMANDS_FILE = KeyFactory.file("commandsFile");

    @Override
    public String getScript() {

        File fileToTransfer = get(FILE_TO_TRANSFER);

        if (!fileToTransfer.isFile()) {
            throw new RuntimeException("Could not find file to transfer '" + fileToTransfer + "'");
        }

        // Check if it needs a name change
        if (hasValue(NEW_FILENAME)) {
            fileToTransfer = SpecsAnt.updateOutput(fileToTransfer, get(NEW_FILENAME));
        }

        Replacer template = new Replacer(AntResource.SFTP_TEMPLATE);

        template.replace("<LOGIN>", get(LOGIN));
        template.replace("<PASS>", get(PASS));
        template.replace("<HOST>", get(HOST));
        template.replace("<PORT>", get(PORT));
        template.replace("<DESTINATION_FOLDER>", get(DESTINATION_FOLDER));

        template = template.replace("<FILE>", fileToTransfer.getAbsolutePath());
        template = template.replace("<FILENAME>", fileToTransfer.getName());

        String commands = getCommands();
        template = template.replace("<COMMANDS>", commands);

        return template.toString();
    }

    private String getCommands() {
        if (!hasValue(COMMANDS_FILE)) {
            return "";
        }

        String commandsPath = SpecsIo.getCanonicalPath(get(COMMANDS_FILE));

        Replacer template = new Replacer(
                "<sshexec host=\"<HOST>\" trust=\"yes\" username=\"<LOGIN>\" password=\"<PASS>\" commandResource=\"<COMMAND_FILE>\"/>");

        template.replace("<LOGIN>", get(LOGIN));
        template.replace("<PASS>", get(PASS));
        template.replace("<HOST>", get(HOST));
        template.replace("<COMMAND_FILE>", commandsPath);

        return template.toString();
    }
}
