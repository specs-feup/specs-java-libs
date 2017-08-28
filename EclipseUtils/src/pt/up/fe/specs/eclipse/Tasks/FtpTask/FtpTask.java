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

package pt.up.fe.specs.eclipse.Tasks.FtpTask;

import java.io.File;

import pt.up.fe.specs.eclipse.DeployResource;
import pt.up.fe.specs.eclipse.EclipseDeploymentData;
import pt.up.fe.specs.eclipse.Tasks.TaskExecutor;
import pt.up.fe.specs.eclipse.Tasks.TaskUtils;
import pt.up.fe.specs.eclipse.Utilities.DeployUtils;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.util.SpecsIo;

/**
 * @author Joao Bispo
 * 
 */
public class FtpTask implements TaskExecutor {

    /* (non-Javadoc)
     * @see org.suikasoft.EclipseDevelopment.TaskExecutor#execute(pt.up.fe.specs.guihelper.BaseTypes.SetupData, org.suikasoft.EclipseDevelopment.EclipseDeploymentData)
     */
    @Override
    public void execute(SetupData setup, EclipseDeploymentData data) {
	// Get SftpData
	FtpData ftpData = FtpSetup.newData(setup);

	File outputJar = DeployUtils.getOutputJar(data.nameOfOutputJar);

	// Check if it needs name a name change
	outputJar = TaskUtils.updateOutput(outputJar, ftpData.outputJarFilename);

	// Get ANT script
	String antftp = buildScript(outputJar, ftpData);

	// Save script
	File ftpScript = new File(DeployUtils.getTempFolder(), "ftp.xml");
	SpecsIo.write(ftpScript, antftp);

	DeployUtils.runAnt(ftpScript);

    }

    /**
     * @param sftpData
     * @return
     */
    private static String buildScript(File fileToSend, FtpData sftpData) {
	String template = SpecsIo.getResource(DeployResource.FTP_TEMPLATE);

	template = template.replace("<LOGIN>", sftpData.login);
	template = template.replace("<PASS>", sftpData.pass);
	template = template.replace("<HOST>", sftpData.host);
	template = template.replace("<DESTINATION_FOLDER>", sftpData.destinationFolder);

	template = template.replace("<FILE>", fileToSend.getAbsolutePath());

	return template;
    }

}
