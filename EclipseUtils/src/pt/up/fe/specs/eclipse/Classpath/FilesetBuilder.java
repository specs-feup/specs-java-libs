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

package pt.up.fe.specs.eclipse.Classpath;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import pt.up.fe.specs.util.SpecsFactory;

/**
 * @author Joao Bispo
 *
 */
public class FilesetBuilder {

    private final Set<String> parsedProjects;

    private final Map<String, File> projectFolders;
    private final Set<File> jarFiles;
    private Optional<String> ivyPath;
    private final Set<String> projectsWithIvy;

    private final String projectName;

    public FilesetBuilder(String projectName) {
        this.projectName = projectName;

        parsedProjects = SpecsFactory.newHashSet();
        projectFolders = SpecsFactory.newLinkedHashMap();
        jarFiles = SpecsFactory.newLinkedHashSet();
        ivyPath = Optional.empty();
        projectsWithIvy = new HashSet<>();
    }

    /**
     * @param projectName
     * @param projectFolder
     * @param sourceFolders
     * @return
     */
    public ClasspathFiles newClasspath(String projectName, File projectFolder, List<String> sourceFolders) {
        // Check if project has a commands file
        File commandsFile = new File(projectFolder, ClasspathFiles.getCommandsFilename());
        commandsFile = commandsFile.isFile() ? commandsFile : null;

        return new ClasspathFiles(projectName, projectFolder, sourceFolders, SpecsFactory.newHashMap(projectFolders),
                SpecsFactory.newArrayList(jarFiles), ivyPath, new ArrayList<>(projectsWithIvy), commandsFile);
    }

    public String getProjectName() {
        return projectName;
    }

    /**
     * @param projectName
     * @return
     */
    public boolean hasParsedProject(String projectName) {
        return parsedProjects.contains(projectName);
    }

    /**
     * @param projectName
     */
    public void markProjectAsParsed(String projectName) {
        parsedProjects.add(projectName);
    }

    /**
     * @param jars
     */
    public void addJars(List<File> jars) {
        jarFiles.addAll(jars);
    }

    /**
     * @param projectName
     * @param projectFolder
     */
    public void addProject(String projectName, File projectFolder) {
        projectFolders.put(projectName, projectFolder);
    }

    public void addIvyPath(String ivyPath) {
        this.ivyPath = Optional.of(ivyPath);
    }

    public void addProjectWithIvy(String projectName) {
        projectsWithIvy.add(projectName);
    }

}
