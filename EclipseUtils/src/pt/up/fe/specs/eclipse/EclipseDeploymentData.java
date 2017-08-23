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
    public final ListOfSetups tasks;

    public EclipseDeploymentData(File workspaceFolder, String projetName,
	    String nameOfOutputJar, String mainClass, JarType jarType, ListOfSetups tasks) {

	this.workspaceFolder = workspaceFolder;
	this.projetName = projetName;
	this.nameOfOutputJar = nameOfOutputJar;
	this.mainClass = mainClass;
	this.jarType = jarType;
	this.tasks = tasks;
    }

}
