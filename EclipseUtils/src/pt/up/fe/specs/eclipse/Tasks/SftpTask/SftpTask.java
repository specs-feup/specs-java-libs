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

import pt.up.fe.specs.eclipse.DeployResource;
import pt.up.fe.specs.eclipse.EclipseDeploymentData;
import pt.up.fe.specs.eclipse.Tasks.TaskExecutor;
import pt.up.fe.specs.eclipse.Tasks.TaskUtils;
import pt.up.fe.specs.eclipse.Utilities.DeployUtils;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.utilities.Replacer;

/**
 * @author Joao Bispo
 *
 */
public class SftpTask implements TaskExecutor {

    /* (non-Javadoc)
     * @see org.suikasoft.EclipseDevelopment.TaskExecutor#execute(pt.up.fe.specs.guihelper.BaseTypes.SetupData, org.suikasoft.EclipseDevelopment.EclipseDeploymentData)
     */
    @Override
    public void execute(SetupData setup, EclipseDeploymentData data) {
        SpecsLogs
                .msgInfo(
                        "Using a secure connection over SSH. Make sure you are inside a network that permits the communication! (e.g., by using a VPN).");

        // Get SftpData
        SftpData sftpData = SftpSetup.newData(setup);

        // File outputJar = DeployUtils.getOutputJar(data.nameOfOutputJar);
        File outputJar = DeployUtils.getResultFile(data);

        // Check if it needs a name change
        outputJar = TaskUtils.updateOutput(outputJar, sftpData.outputJarFilename, data);

        SpecsLogs.msgInfo("Transfering '" + outputJar.getName() + "' to " + sftpData.host + ":"
                + sftpData.destinationFolder);

        /*
        	if(sftpData.outputJarFilename != null) {
        	    // New file in temporary folder
        	    File tempFolder = DeployUtils.getTempFolder();
        	    File newOutputJar = new File(tempFolder, sftpData.outputJarFilename);
        	    
        	    // Copy file
        	    IoUtils.copy(outputJar, newOutputJar);
        	    
        	    // Update reference
        	    outputJar = newOutputJar;
        	}
        */
        // Get ANT script
        String antSftp = buildScript(outputJar, sftpData);

        // Save script
        File sftpScript = new File(DeployUtils.getTempFolder(), "sftp.xml");
        SpecsIo.write(sftpScript, antSftp);

        DeployUtils.runAnt(sftpScript);

    }

    /**
     * @param sftpData
     * @return
     */
    private static String buildScript(File fileToSend, SftpData sftpData) {
        String template = SpecsIo.getResource(DeployResource.SFTP_TEMPLATE);

        template = template.replace("<LOGIN>", sftpData.login);
        template = template.replace("<PASS>", sftpData.pass);
        template = template.replace("<HOST>", sftpData.host);
        template = template.replace("<PORT>", sftpData.port);
        template = template.replace("<DESTINATION_FOLDER>", sftpData.destinationFolder);

        template = template.replace("<FILE>", fileToSend.getAbsolutePath());

        String commands = getCommands(sftpData);
        template = template.replace("<COMMANDS>", commands);

        return template;
    }

    private static String getCommands(SftpData sftpData) {
        if (sftpData.commandsFile == null) {
            return "";
        }

        String commandsPath = SpecsIo.getCanonicalPath(sftpData.commandsFile);

        Replacer template = new Replacer(
                "<sshexec host=\"<HOST>\" trust=\"yes\" username=\"<LOGIN>\" password=\"<PASS>\" commandResource=\"<COMMAND_FILE>\"/>");

        template.replace("<LOGIN>", sftpData.login);
        template.replace("<PASS>", sftpData.pass);
        template.replace("<HOST>", sftpData.host);
        template.replace("<COMMAND_FILE>", commandsPath);

        return template.toString();
    }

}
