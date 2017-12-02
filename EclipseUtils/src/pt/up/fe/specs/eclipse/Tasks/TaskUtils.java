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

package pt.up.fe.specs.eclipse.Tasks;

import java.io.File;
import java.util.Map;

import pt.up.fe.specs.eclipse.Tasks.Copy.CopySetup;
import pt.up.fe.specs.eclipse.Tasks.Copy.CopyTask;
import pt.up.fe.specs.eclipse.Tasks.FtpTask.FtpSetup;
import pt.up.fe.specs.eclipse.Tasks.FtpTask.FtpTask;
import pt.up.fe.specs.eclipse.Tasks.SftpTask.SftpSetup;
import pt.up.fe.specs.eclipse.Tasks.SftpTask.SftpTask;
import pt.up.fe.specs.eclipse.Utilities.DeployUtils;
import pt.up.fe.specs.guihelper.Base.SetupFieldEnum;
import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * @author Joao Bispo
 * 
 */
public class TaskUtils {

    // private static final Map<String, TaskExecutor> tasks;
    private static final Map<Class<? extends SetupFieldEnum>, TaskExecutor> tasks;
    static {
        tasks = SpecsFactory.newLinkedHashMap();
        // tasks.put(SftpSetup.DestinationFolder.getSetupName(), new SftpTask());
        // tasks.put(FtpSetup.DestinationFolder.getSetupName(), new FtpTask());
        // tasks.put(CopySetup.DestinationFolder.getSetupName(), new CopyTask());
        tasks.put(SftpSetup.class, new SftpTask());
        tasks.put(FtpSetup.class, new FtpTask());
        tasks.put(CopySetup.class, new CopyTask());
    }

    public static Map<Class<? extends SetupFieldEnum>, TaskExecutor> getTasks() {
        return tasks;
    }

    public static Map<String, TaskExecutor> getTasksByName() {
        Map<String, TaskExecutor> tasksByName = SpecsFactory.newHashMap();

        for (Class<? extends SetupFieldEnum> aClass : tasks.keySet()) {
            // Get executor
            TaskExecutor executor = tasks.get(aClass);

            // Get setup name
            String setupName = aClass.getEnumConstants()[0].getSetupName();

            // Add to table
            tasksByName.put(setupName, executor);
        }

        return tasksByName;
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
            SpecsLogs.msgInfo(" - Output name for jar is the same as the default name ('" + newName + "')");
            return file;
        }

        // New file in temporary folder
        File tempFolder = DeployUtils.getTempFolder();

        // Put renamed JAR in a new folder. If we are in Windows and only the case of the name changes,
        // it will copy the file over itself, producing a JAR with 0-bytes
        File newOutputJarFolder = SpecsIo.mkdir(tempFolder, "renamedJar");

        File newOutputJar = new File(newOutputJarFolder, newName);

        // Copy file
        SpecsIo.copy(file, newOutputJar);

        // Update reference
        file = newOutputJar;

        return file;
    }
}
