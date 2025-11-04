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

package pt.up.fe.specs.eclipse.Classpath;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import pt.up.fe.specs.eclipse.builder.BuildUtils;
import pt.up.fe.specs.util.SpecsCheck;

/**
 * @author Joao Bispo
 *
 */
public class ClasspathFiles {

    private static final String COMMANDS_FILENAME = "commands.build";

    public static String getCommandsFilename() {
        return COMMANDS_FILENAME;
    }

    private final String projectName;
    private final File projectFolder;
    private final List<String> sourceFolders;
    private final Map<String, File> projectFolders;
    private final List<File> jarFiles;
    private final List<String> parentProjects;
    private final Optional<String> ivyPath;
    private final List<String> projectsWithIvy;
    private final File commandsFile;

    public ClasspathFiles(String projectName, File projectFolder, List<String> sourceFolders,
            Map<String, File> projectFolders, List<File> jarFiles, Optional<String> ivyPath,
            List<String> projectsWithIvy, File commandsFile) {

        this.projectName = projectName;
        this.projectFolder = projectFolder;
        this.projectFolders = projectFolders;
        this.sourceFolders = sourceFolders;
        this.jarFiles = jarFiles;
        this.ivyPath = ivyPath;
        this.projectsWithIvy = projectsWithIvy;
        this.commandsFile = commandsFile;
        parentProjects = buildParentProjects(projectName, projectFolders);
    }

    private static List<String> buildParentProjects(String projectName, Map<String, File> projectFolders) {
        List<String> parentProjects = new ArrayList<>();

        for (String name : projectFolders.keySet()) {
            // Parse name
            if (name.startsWith("/")) {
                name = name.substring(1);
            }

            // Do not include the project itself
            if (name.equals(projectName)) {
                continue;
            }

            parentProjects.add(name);
        }

        return parentProjects;
    }

    public Optional<File> getCommandsFile() {
        return Optional.ofNullable(commandsFile);
    }

    public boolean usesIvy() {
        return getIvyPath().isPresent();
    }

    public Optional<String> getIvyPath() {
        return ivyPath;
    }

    public Optional<File> getIvyFile() {
        Optional<File> ivyFile = getIvyPath().map(ivyPath -> new File(getProjectFolder(), ivyPath));
        if (ivyFile.isPresent()) {
            SpecsCheck.checkArgument(ivyFile.get().isFile(), () -> "Could not find ivy file '" + ivyFile.get() + "'");
        }

        return ivyFile;
    }

    public Optional<File> getIvyJarFolder() {
        if (!getIvyPath().isPresent()) {
            return Optional.empty();
        }

        return Optional.of(BuildUtils.getIvyJarFolder(getProjectFolder()));
    }

    public String getProjectName() {
        return projectName;
    }

    /**
     * @return the jarFiles
     */
    public List<File> getJarFiles() {
        return jarFiles;
    }

    /**
     * @return the projectFolders
     */
    public Collection<File> getBinFolders() {
        // return projectFolders;
        return projectFolders.values();
    }

    public List<String> getDependentProjects() {
        return parentProjects;
    }

    public File getProjectFolder() {
        return projectFolder;
    }

    public List<String> getSourceFolders() {
        return sourceFolders;
    }

    /**
     *
     * @return the source folders of this project
     */
    public List<File> getSources() {
        return getSourceFolders().stream()
                .map(src -> new File(getProjectFolder(), src))
                .collect(Collectors.toList());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Project Folders:" + projectFolders + "\n" + "Jar Files:" + jarFiles;
    }

    public List<String> getProjectsWithIvy() {
        return projectsWithIvy;
    }
}
