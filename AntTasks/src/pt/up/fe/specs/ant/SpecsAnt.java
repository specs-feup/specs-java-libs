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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.ant;

import java.io.File;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.lazy.Lazy;

/**
 * Utility methods related with ANT.
 * 
 * @author JoaoBispo
 *
 */
public class SpecsAnt {

    private static final Lazy<File> TEMPORARY_FOLDER = Lazy.newInstance(SpecsAnt::initTempFolder);

    private static File initTempFolder() {
        File tempFolder = SpecsIo.getTempFolder("specs_ant");
        SpecsIo.deleteFolderContents(tempFolder);
        return tempFolder;
    }

    public static void runAnt(File antScript, String target) {

        // Launch ant
        Project project = new Project();
        project.init();

        ProjectHelper.configureProject(project, antScript);

        project.addBuildListener(SpecsAnt.newStdoutListener());

        // Run script
        target = target != null ? target : project.getDefaultTarget();
        project.executeTarget(target);
    }

    /**
     * Standard listener for ANT project.
     * 
     * <p>
     * Outputs a message when an ANT target starts and finishes.
     * 
     * @return
     */
    public static BuildListener newStdoutListener() {
        BuildListener outListener = new BuildListener() {

            @Override
            public void taskStarted(BuildEvent arg0) {
                // System.out.println("Task Started: "+arg0.getTask().getTaskName());
                // System.out.println(arg0.getMessage());
            }

            @Override
            public void taskFinished(BuildEvent arg0) {
                // System.out.println(arg0.getMessage());
            }

            @Override
            public void targetStarted(BuildEvent arg0) {
                SpecsLogs.msgInfo("[ANT]:Started target '" + arg0.getTarget() + "'");
                // System.out.println(arg0.getMessage());

            }

            @Override
            public void targetFinished(BuildEvent arg0) {
                SpecsLogs.msgInfo("[ANT]:Finished target '" + arg0.getTarget() + "'");
            }

            @Override
            public void messageLogged(BuildEvent arg0) {
                // So that it can show errors (e.g., javac)
                if (!arg0.getMessage().startsWith("[") && arg0.getPriority() < 3) {
                    SpecsLogs.msgInfo(arg0.getMessage());
                }
                // if (arg0.getPriority() < 3) {
                // System.out.println(arg0.getMessage());
                // }

                // SpecsLogs.msgInfo(arg0.getMessage());
                // System.out.println(arg0.getMessage());

            }

            @Override
            public void buildStarted(BuildEvent arg0) {
                // System.out.println("Build Started");
            }

            @Override
            public void buildFinished(BuildEvent arg0) {
                // System.out.println(arg0.getMessage());

            }
        };

        return outListener;
    }

    /**
     * Returns a File object pointing to a file equal to the given, but with another name.
     * 
     * @param file
     * @param newName
     * @return
     */
    public static File updateOutput(File file, String newName) {

        // If newName is null, return original file
        if (newName == null) {
            return file;
        }

        // If newName is the same as the current name, return original file
        if (file.getName().equals(newName)) {
            SpecsLogs.info("New name for file is the same as the current name ('" + newName + "')");
            return file;
        }

        // New file in temporary folder
        File tempFolder = getTemporaryFolder();

        // Put renamed file in a new folder. If we are in Windows and only the case of the name changes,
        // it will copy the file over itself, producing a file with 0-bytes
        File newOutputFolder = SpecsIo.mkdir(tempFolder, "renamedFile");

        File newOutputFile = new File(newOutputFolder, newName);

        // Copy file
        SpecsIo.copy(file, newOutputFile);

        // Update reference
        file = newOutputFile;

        return file;
    }

    public static File getTemporaryFolder() {
        return TEMPORARY_FOLDER.get();
    }
}
