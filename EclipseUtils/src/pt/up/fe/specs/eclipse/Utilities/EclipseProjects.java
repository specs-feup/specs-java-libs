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

package pt.up.fe.specs.eclipse.Utilities;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

import org.w3c.dom.NodeList;

import pt.up.fe.specs.lang.SpecsPlatforms;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsXml;

public class EclipseProjects {

    private static final String[] CHAIN_PROJECT_NAME = { "projectDescription", "name" };

    private static final String PATH_PROJECTS = ".metadata/.plugins/org.eclipse.core.resources/.projects";
    private static final String LOCATION_FILE = ".location";

    private static final String REGEX = "\\x00.URI//file:/(.+?)\\x00";

    /**
     * Maps the name of a project in the workspace to the corresponding project folder.
     */
    private final Map<String, File> projectFolders;
    private final Map<String, File> projectRepos;
    private final Map<String, File> projectIvySettings;

    private EclipseProjects(Map<String, File> projectFolders, Map<String, File> projectRepos,
            Map<String, File> projectIvySettings) {

        this.projectFolders = projectFolders;
        this.projectRepos = projectRepos;
        this.projectIvySettings = projectIvySettings;
    }

    public static EclipseProjects newEmpty() {
        return new EclipseProjects(new HashMap<>(), new HashMap<>(), new HashMap<>());
    }

    public static EclipseProjects newFromWorkspace(File workspace) {

        // Get projects folder
        File projectsFolder = SpecsIo.existingFolder(workspace, PATH_PROJECTS);

        // Get folders, one for each project
        List<File> projects = SpecsIo.getFolders(projectsFolder);
        Map<String, File> projectsFolders = buildProjectsMap(projects);

        // Repo information not supported for builds from a workspace
        return new EclipseProjects(projectsFolders, new HashMap<>(), new HashMap<>());
    }

    public static EclipseProjects newFromRepository(File repositoryFolder) {
        // Find all folders that contain a .project files in repository folder
        List<File> projectFiles = SpecsIo.getFilesRecursive(repositoryFolder, "project");

        // For testing
        // XStreamUtils.write(new File("temp_files.xml"), projectFiles);
        // List<File> projectFiles = (List<File>) XStreamUtils.read(new File("temp_files.xml"), List.class);

        Map<String, File> projectFolders = new HashMap<>();
        Map<String, File> projectRepos = new HashMap<>();
        Map<String, File> projectIvySettings = new HashMap<>();
        addProjects(projectFiles, repositoryFolder, projectFolders, projectRepos, projectIvySettings);

        // All projects are from the same repo

        for (String projectName : projectFolders.keySet()) {
            projectRepos.put(projectName, repositoryFolder);
        }
        /*
        for (File projectFile : projectFiles) {
            // Get the name of the project
            NodeList nodes = XmlUtils.getNodeList(projectFile);
            String projectName = XmlUtils.getText(nodes, CHAIN_PROJECT_NAME);

            // Get parent folder
            File projectFolder = projectFile.getParentFile();
            if (!projectFolder.isDirectory()) {
                throw new RuntimeException("Parent '" + projectFolder + "' of project file '" + projectFile
                        + "' is not a folder.");
            }

            projectFolders.put(projectName, projectFolder);
        }
        */

        return new EclipseProjects(projectFolders, projectRepos, projectIvySettings);
    }

    private static Map<String, File> buildProjectsMap(List<File> projects) {
        Map<String, File> projectsFolders = new HashMap<>();
        Pattern regex = Pattern.compile(REGEX);

        for (File project : projects) {
            // Get .location file
            File location = new File(project, LOCATION_FILE);

            // If .location does not exist, skip project (might be closed in Eclipse)
            if (!location.isFile()) {
                continue;
            }

            String locationContents = SpecsIo.read(location);
            // System.out.println("LOCATION FILE FOR " + project.getName() + "\n" + locationContents);
            String projectFoldername = SpecsStrings.getRegexGroup(locationContents, regex, 1);

            // Fix path if unix
            if (SpecsPlatforms.isUnix()) {
                projectFoldername = "/" + projectFoldername;
            }

            if (projectFoldername == null) {
                SpecsLogs.msgInfo("Could not decode .location file for project '" + project.getName()
                        + "'. Check if project is inside a repository.");
                continue;
            }

            // Parse name
            try {
                projectFoldername = java.net.URLDecoder.decode(projectFoldername, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                SpecsLogs.warn("Could not parse project folder name '" + projectFoldername + "'", e);
                projectFoldername = null;
            }

            // Skip project
            if (projectFoldername == null) {
                continue;
            }

            // If folder does not exist, skip project
            File projectFolder = new File(projectFoldername);
            if (!projectFolder.isDirectory()) {
                continue;
            }

            // projectFolder = IoUtils.existingFolder(null, projectFoldername);

            projectsFolders.put(project.getName(), projectFolder);
        }

        return projectsFolders;
    }

    public File getProjectFolder(String projectName) {
        projectName = parseProjectName(projectName);

        File projectFolder = projectFolders.get(projectName);

        if (projectFolder == null) {
            throw new RuntimeException("Could not find folder for project '" + projectName + "'");
        }

        return projectFolder;
    }

    public Optional<File> getProjectRepositoryTry(String projectName) {
        projectName = parseProjectName(projectName);

        return Optional.ofNullable(projectRepos.get(projectName));
    }

    public File getProjectRepository(String projectName) {
        return getProjectRepositoryTry(projectName)
                .orElseThrow(() -> new RuntimeException("Could not find repository for project '" + projectName + "'"));
    }

    private static String parseProjectName(String projectName) {
        // If project name starts with '/', remove it
        if (projectName.startsWith("/")) {
            return projectName.substring(1);
        }

        return projectName;

    }

    /*
    public File getWorkspaceFolder() {
    return workspaceFolder;
    }
    */

    /**
     * Creates a new EclipseProjects with paths relative to the given folder.
     *
     * @param rootFolder
     * @return
     */
    /*
    public EclipseProjects makePathsRelative(File rootFolder) {

        Map<String, File> relativeProjectFolders = new HashMap<>();

        for (String key : projectFolders.keySet()) {
            File folder = projectFolders.get(key);

            String relativeFilename = IoUtils.getRelativePath(folder, rootFolder);
            if (relativeFilename == null) {
                throw new RuntimeException("Could not convert path '" + folder + "' to relative path using as base '"
                        + rootFolder + "'");
            }

            // Replace
            // projectFolders.put(key, new File(relativeFilename));
            relativeProjectFolders.put(key, new File(relativeFilename));
        }

        return new EclipseProjects(relativeProjectFolders);
    }
    */

    public Collection<String> getProjectNames() {
        return projectFolders.keySet();
    }

    @Override
    public String toString() {
        return projectFolders.toString();
    }

    public void addRepository(File repositoryFolder, Set<String> projectsToIgnore, File ivySettings) {
        // Find all folders that contain a .project files in repository folder
        List<File> projectFiles = SpecsIo.getFilesRecursive(repositoryFolder, "project");

        // Add project folders
        addProjects(projectFiles, repositoryFolder, projectsToIgnore, ivySettings, projectFolders, projectRepos,
                projectIvySettings);

    }

    private static void addProjects(List<File> projectFiles, File repositoryFolder, Map<String, File> projectFolders,
            Map<String, File> projectRepos, Map<String, File> ivySettingsMap) {

        addProjects(projectFiles, repositoryFolder, new HashSet<>(), null, projectFolders, projectRepos,
                ivySettingsMap);
    }

    private static void addProjects(List<File> projectFiles, File repositoryFolder, Set<String> projectsToIgnore,
            File ivySettings, Map<String, File> projectFolders, Map<String, File> projectRepos,
            Map<String, File> projectIvySettings) {

        for (File projectFile : projectFiles) {
            // Get the name of the project
            NodeList nodes = SpecsXml.getNodeList(projectFile);
            String projectName = SpecsXml.getText(nodes, CHAIN_PROJECT_NAME);

            // Ignore project if in ignore list
            if (projectsToIgnore.contains(projectName)) {
                SpecsLogs.msgInfo("Skipping project '" + projectName + "' (it is in ignore list)");
                continue;
            }

            // Get parent folder
            File projectFolder = projectFile.getParentFile();
            if (!projectFolder.isDirectory()) {
                throw new RuntimeException("Parent '" + projectFolder + "' of project file '" + projectFile
                        + "' is not a folder.");
            }

            File previousFile = projectFolders.put(projectName, projectFolder);
            if (previousFile != null) {
                SpecsLogs.msgInfo("!Duplicate project '" + projectName + "', replace project in location '"
                        + previousFile + "' with '" + projectFolder + "'");
            }

            projectRepos.put(projectName, repositoryFolder);
            if (ivySettings != null) {
                projectIvySettings.put(projectName, ivySettings);
            }
        }
    }

    public File getIvySettings(String projectName) {
        projectName = parseProjectName(projectName);
        return projectIvySettings.get(projectName);
    }
}
