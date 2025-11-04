/**
 * Copyright 2015 SPeCS.
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

package pt.up.fe.specs.psfbuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.TreeSet;

import pt.up.fe.specs.eclipse.Classpath.ClasspathFiles;
import pt.up.fe.specs.eclipse.Classpath.ClasspathParser;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.ProgressCounter;
import pt.up.fe.specs.util.utilities.Replacer;

public class PsfBuilder {

    // private static final boolean OVERWRITE_EXISTING_PSF = true;
    private static final String PSF_DEPENDENCY_TEMPLATE = "<project reference=\"1.0,<REMOTE>,<BRANCH>,<PROJECT_RELATIVE_PATH>\"/>";

    private final ClasspathParser eclipseProjects;
    private final List<String> compulsoryProjects;

    public PsfBuilder(ClasspathParser eclipseProjects, List<String> compulsoryProjects) {
        this.eclipseProjects = eclipseProjects;
        this.compulsoryProjects = compulsoryProjects;
    }

    public static void main(String[] args) {
        SpecsSystem.programStandardInit();

        // Accepts one argument, a folder which will be search recursively for project folders
        if (args.length == 0) {
            SpecsLogs
                    .msgInfo(
                            "Accepts one argument, a folder which will be search recursively for project folders, and optionally a user.libraries file (to avoid warnings). All remaining arguments will be interpreted as project names that should always be added to the dependencies.");
            return;
        }

        File projectsFolder = SpecsIo.existingFolder(args[0]);

        // Get all projects from folder
        ClasspathParser eclipseProjects;
        if (args.length < 2) {
            eclipseProjects = ClasspathParser.newInstance(projectsFolder);
        } else {
            eclipseProjects = ClasspathParser.newInstance(projectsFolder, new File(args[1]));
        }

        List<String> compulsoryProjects = new ArrayList<>();
        for (int i = 2; i < args.length; i++) {
            compulsoryProjects.add(args[i]);
        }

        PsfBuilder builder = new PsfBuilder(eclipseProjects, compulsoryProjects);
        builder.build();
    }

    private void build() {
        ProgressCounter counter = new ProgressCounter(eclipseProjects.getEclipseProjects().getProjectNames().size());
        // For each project, build a psf file, if not present
        for (String projectName : eclipseProjects.getEclipseProjects().getProjectNames()) {
            SpecsLogs.msgInfo("Writing import file for project '" + projectName + "' " + counter.next());
            File projectFolder = eclipseProjects.getEclipseProjects().getProjectFolder(projectName);
            addPsfFile(projectName, projectFolder);
        }
    }

    private void addPsfFile(String projectName, File projectFolder) {
        // Check if project folder already has a PSF file
        /*
        if (!IoUtils.getFiles(projectFolder, "psf").isEmpty() && !OVERWRITE_EXISTING_PSF) {
        // if (IoUtils.getFiles(projectFolder, "psf").isEmpty()) {
        LoggingUtils.msgInfo("Project '" + projectName + "' already has a .psf file, skipping");
        return;
        }
        */

        // Get list of dependent projects
        ClasspathFiles classpath = eclipseProjects.getClasspath(projectName);
        List<String> projects = classpath.getDependentProjects();

        // If no projects, ignore
        if (projects.isEmpty() && compulsoryProjects.isEmpty()) {
            SpecsLogs.msgInfo(" - Ignoring project, does not have project dependencies");
            return;
        }

        // Add self
        projects.add(projectName);

        // Add compulsory projects
        Set<String> currentProjects = new HashSet<>(projects);
        for (String compulsoryProject : compulsoryProjects) {
            if (currentProjects.contains(compulsoryProject)) {
                continue;
            }

            projects.add(compulsoryProject);
        }

        // Sort dependencies alphabetically
        Set<String> sortedProjects = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        sortedProjects.addAll(projects);

        String psfDependencies = buildPsfDependencies(sortedProjects);

        Replacer psfContents = new Replacer(PsfResource.PROJECT_PSF);

        psfContents.replace("<PROJECTS>", psfDependencies);

        File psfFile = new File(projectFolder, "projectSet.psf");

        SpecsIo.write(psfFile, psfContents.toString());

    }

    private String buildPsfDependencies(Collection<String> projects) {
        StringJoiner joiner = new StringJoiner(SpecsIo.getNewline());
        for (String project : projects) {
            String psfDependency = getPsfDependency(project);
            joiner.add(psfDependency);
        }

        return joiner.toString();
    }

    private String getPsfDependency(String project) {
        File projectFolder = eclipseProjects.getEclipseProjects().getProjectFolder(project);
        // Get GitBranch for project
        GitBranch gitBranch = GitBranch.newInstance(projectFolder);
        String remote = getRemote(gitBranch);
        String branch = gitBranch.getBranch();

        String relativePath = SpecsIo.getRelativePath(projectFolder, gitBranch.getWorkingTree());

        Replacer replacer = new Replacer(PSF_DEPENDENCY_TEMPLATE);
        replacer.replace("<REMOTE>", remote);
        replacer.replace("<BRANCH>", branch);
        replacer.replace("<PROJECT_RELATIVE_PATH>", relativePath);

        return replacer.toString();
    }

    /*
    private static Optional<File> getGitRepo(File startLocation) {
    // Get current folder
    File currentParent = startLocation.getAbsoluteFile().getParentFile();
    System.out.println("CURRENT:" + currentParent);
    while (currentParent != null) {
        // Check if it has a .git folder
        File gitFolder = new File(currentParent, GIT_FOLDER);
        if (gitFolder.isDirectory()) {
    	return Optional.of(gitFolder);
        }
    
        currentParent = currentParent.getParentFile();
    }
    
    return Optional.empty();
    }
    */

    private static String getRemote(GitBranch gitBranch) {
        String originalRemote = gitBranch.getRemote();

        // Remove user information from remote
        int index = originalRemote.indexOf("//");
        int atIndex = originalRemote.indexOf("@");

        if (index == -1 || atIndex == -1) {
            return originalRemote;
        }

        return originalRemote.substring(0, index + 2) + originalRemote.substring(atIndex + 1);
    }

}
