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
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.eclipse.Tasks.Copy;

import java.io.File;

import pt.up.fe.specs.eclipse.EclipseDeploymentData;
import pt.up.fe.specs.eclipse.Tasks.TaskExecutor;
import pt.up.fe.specs.eclipse.Tasks.TaskUtils;
import pt.up.fe.specs.eclipse.Utilities.DeployUtils;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author Joao Bispo
 * 
 */
public class CopyTask implements TaskExecutor {

    /* (non-Javadoc)
     * @see org.suikasoft.EclipseDevelopment.TaskExecutor#execute(pt.up.fe.specs.guihelper.BaseTypes.SetupData, org.suikasoft.EclipseDevelopment.EclipseDeploymentData)
     */
    @Override
    public void execute(SetupData setup, EclipseDeploymentData data) {

        // Get CopyData
        CopyData copyData = CopySetup.newData(setup);

        // Get output folder
        File outputFolder = SpecsIo.mkdir(copyData.destinationFolder);
        if (outputFolder == null) {
            SpecsLogs.msgInfo("Canceling task, could not open folder '" + outputFolder + "'");
            return;
        }

        // Get file to copy
        File outputJar = DeployUtils.getResultFile(data);

        outputJar = TaskUtils.updateOutput(outputJar, copyData.outputJarFilename, data);

        // Get output file
        File outputFile = new File(outputFolder, outputJar.getName());

        // Only show message if file does not exist. Otherwise, copy will warn on overwriting
        if (!outputFile.isFile()) {
            SpecsLogs.msgInfo("Copying file " + outputFile.getName() + " to "
                    + outputFile.getParent());
        }

        // Copy file
        boolean success = SpecsIo.copy(outputJar, outputFile);
        if (!success) {
            SpecsLogs.msgInfo("Could not copy file to destination: '" + outputFile + "'");
            return;
        }

    }

}
