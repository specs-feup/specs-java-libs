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
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.eclipse;

import java.io.File;

import pt.up.fe.specs.guihelper.BaseTypes.ListOfSetups;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.properties.SpecsProperties;

/**
 * Data fields for EclipseDeployment.
 *
 * @author Joao Bispo
 */
public class EclipseDeploymentData {

    public final File workspaceFolder;
    public final String projetName;
    public final String nameOfOutputJar;
    public final String mainClass;
    public final JarType jarType;
    public final SpecsProperties pomInfo;
    public final File developersXml;
    public final String version;
    public final boolean processJar; // Removes \r from manifest file
    public final ListOfSetups tasks;

    private File resultFile;
    private final String buildNumber;

    public EclipseDeploymentData(File workspaceFolder, String projetName, String nameOfOutputJar, String mainClass,
            JarType jarType, SpecsProperties pomInfo, File developersXml, String version, boolean processJar,
            ListOfSetups tasks) {

        this.workspaceFolder = workspaceFolder;
        this.projetName = projetName;
        this.nameOfOutputJar = nameOfOutputJar;
        this.mainClass = mainClass;
        this.jarType = jarType;
        this.pomInfo = pomInfo;
        this.developersXml = developersXml;
        this.version = version;
        this.processJar = processJar;
        this.tasks = tasks;
        this.resultFile = null;
        this.buildNumber = SpecsSystem.createBuildNumber();
    }

    public void setResultFile(File resultFile) {
        this.resultFile = resultFile;
    }

    public File getResultFile() {
        return resultFile;
    }

    public String getBuildNumber() {
        return buildNumber;
    }
}
