/**
 * Copyright 2014 SPeCS.
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

package pt.up.fe.specs.eclipse.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import pt.up.fe.specs.eclipse.Classpath.ClasspathFiles;
import pt.up.fe.specs.eclipse.Classpath.ClasspathParser;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.properties.SpecsProperty;
import pt.up.fe.specs.util.utilities.LineStream;
import pt.up.fe.specs.util.utilities.Replacer;

/**
 * @author Joao Bispo
 *
 */
public class CreateBuildXml {

    private static final boolean TEMP_IGNORE_TEST_FOLDERS = false;

    private static final String IGNORE_FILE_PROJECTS = "projects.buildignore";
    private static final String BENCHMARKER_FILE_PROJECTS = "projects.benchmarker";
    // private static final String FILE_PROJECTS_CACHE = "eclipse_projects.xml";

    public static final String JTEST_PROJECT_NAME = "projectName";

    public static File getBenchmarkerProjectsFile() {
        return new File(BENCHMARKER_FILE_PROJECTS);
    }

    private final File repFolder;
    private final ClasspathParser parser;
    private final Set<String> projectsToIgnore;
    private final File ivySettingsFile;
    private final boolean ignoreTestFolders;

    public CreateBuildXml(File repFolder, ClasspathParser parser, Collection<String> ignoreList, File ivySettingsFile) {
        this.repFolder = SpecsIo.getCanonicalFile(repFolder);
        this.parser = parser;
        projectsToIgnore = new HashSet<>(ignoreList);
        this.ivySettingsFile = ivySettingsFile;
        this.ignoreTestFolders = TEMP_IGNORE_TEST_FOLDERS;
    }

    public static void main(String args[]) {
        SpecsSystem.programStandardInit();

        SpecsProperty.ShowStackTrace.applyProperty("true");

        if (args.length < 2) {
            SpecsLogs
                    .msgInfo(
                            "Needs at least two arguments, the root of the repository and the user libraries file exported from Eclipse (.userlibraries). Optionally you can pass the ivy settings file");
            return;
        }

        File repFolder = SpecsIo.existingFolder(null, args[0]);
        if (repFolder == null) {
            return;
        }

        // File parsedInfo = IoUtils.existingFile(args[1]);
        // ClasspathParser parser = XStreamUtils.read(parsedInfo, ClasspathParser.class);

        File userLibrariesFile = SpecsIo.existingFile(repFolder, args[1]);

        ClasspathParser parser = ClasspathParser.newInstance(repFolder, userLibrariesFile);

        File ivySettingsFile = null;
        if (args.length > 2) {
            ivySettingsFile = SpecsIo.existingFile(args[2]);
        }

        // Save classparser
        // XStreamUtils.write(new File(repFolder, CreateBuildXml.FILE_PROJECTS_CACHE), parser);

        CreateBuildXml buildXml = new CreateBuildXml(repFolder, parser, getIgnoreList(), ivySettingsFile);
        buildXml.execute();
    }

    /*
    public static Optional<ClasspathParser> loadCachedInfo(File folder) {
        File cachedInfo = new File(folder, CreateBuildXml.FILE_PROJECTS_CACHE);
        if (!cachedInfo.isFile()) {
            return Optional.empty();
        }
    
        return Optional
                .of(XStreamUtils.read(new File(folder, CreateBuildXml.FILE_PROJECTS_CACHE), ClasspathParser.class));
    }
    */

    private static List<String> getIgnoreList() {
        return parseProjectsList(new File(CreateBuildXml.IGNORE_FILE_PROJECTS));
    }

    private static List<String> getBenchmarkerList() {
        return parseProjectsList(new File(CreateBuildXml.BENCHMARKER_FILE_PROJECTS));
    }

    public static List<String> parseProjectsList(File file) {
        // If files does not exists, return empty list
        if (!file.isFile()) {
            return Collections.emptyList();
        }

        // Parse file
        return LineStream.newInstance(file).stream()
                // Remove comments
                .filter(line -> !line.startsWith("#"))
                // Collect project names
                .collect(Collectors.toList());
    }

    // public static Set<String> parseProjectList(File file) {
    // return new LinkedHashSet<>(getProjectsList(file));
    // }

    /**
     * 
     */
    public void execute() {
        // Build all projects
        buildProjects();
    }

    private void buildProjects() {

        List<String> projectNames = getProjectNames();

        // Build clean
        String clean = buildClean(projectNames);

        // Build compilation targets
        StringBuilder compileTargets = new StringBuilder();
        for (String projectName : projectNames) {
            // Check if project is in ignore list

            String compileTarget = buildCompileTarget(projectName);
            compileTargets.append(compileTarget);
            compileTargets.append("\n");
        }

        // Build junit targets
        StringBuilder junitTargets = new StringBuilder();
        for (String projectName : projectNames) {
            String junitTarget = buildJUnitTarget(projectName);
            junitTargets.append(junitTarget);
            junitTargets.append("\n");
        }

        // Build benchmarker target
        String benchmarkerTarget = buildBenchmarkerTarget();

        String ivyImport = BuildUtils.getIvyDependency(parser);

        Replacer antBuild = new Replacer(BuildResource.MAIN_TEMPLATE);

        antBuild.replace("<USE_IVY>", ivyImport);
        antBuild.replace("<IVY_SETTINGS>", getIvySettings());
        antBuild.replace("<CLEAN>", clean);
        antBuild.replace("<ALL_COMPILE_TARGETS>", BuildUtils.getDependenciesSuffix(projectNames));
        antBuild.replace("<COMPILE_TARGETS>", compileTargets.toString());

        antBuild.replace("<ALL_JUNIT_TARGETS>", BuildUtils.getJUnitTargetDependencies(projectNames));
        antBuild.replace("<JUNIT_TARGETS>", junitTargets.toString());
        antBuild.replace("<BENCH_TARGETS>", benchmarkerTarget);

        // Save script
        File buildFile = new File(repFolder, "build_test.xml");

        SpecsIo.write(buildFile, antBuild.toString());
        SpecsLogs.msgInfo("ANT Build file written (" + buildFile + ")");

    }

    private String buildBenchmarkerTarget() {
        List<String> projectNames = getBenchmarkerList();

        StringBuilder benchTargets = new StringBuilder();
        for (String projectName : projectNames) {
            String benchTarget = buildBenchmarkerTarget(projectName);
            benchTargets.append(benchTarget);
            benchTargets.append("\n");
        }

        // Build target benchmarker that call all benchmarker targets

        String benchmarkerTarget = projectNames.stream()
                .map(projectName -> BuildUtils.getBenchmarkerTargetName(projectName))
                .collect(Collectors.joining(",", "<target name=\"benchmarker\" depends=\"", "\"></target>"));

        benchTargets.append(benchmarkerTarget);

        return benchTargets.toString();
    }

    private String getIvySettings() {
        if (ivySettingsFile == null) {
            return "";
        }

        return "<ivy:settings file=\"" + SpecsIo.getCanonicalPath(ivySettingsFile) + "\"/>\n";
    }

    private String buildClean(List<String> projectNames) {
        if (projectNames.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for (String projectName : projectNames) {

            // Add delete for /bin folders
            Replacer template = new Replacer(BuildResource.DELETE_TEMPLATE);
            template.replace("<FOLDER>", BuildUtils.getBinFoldername(parser.getClasspath(projectName)));
            builder.append(template.toString()).append("\n");

            // Add delete for /ivy folders, if project uses it
            if (parser.getClasspath(projectName).getIvyPath().isPresent()) {
                Replacer ivyTemplate = new Replacer(BuildResource.DELETE_TEMPLATE);
                String ivyFolder = BuildUtils.getIvyJarFoldername(parser.getClasspath(projectName).getProjectFolder());
                // Make sure folder exists, to avoid errors during ANT run
                SpecsIo.mkdir(ivyFolder);
                ivyTemplate.replace("<FOLDER>", ivyFolder);
                builder.append(ivyTemplate.toString()).append("\n");
            }

            // parser.getClasspath(projectName).getIvyPath().ifPresent(
            // ivyPath -> System.out.println("Project '" + projectName + "' uses ivy (" + ivyPath + ")"));

        }

        return builder.toString();
    }

    private String buildJUnitTarget(String projectName) {
        ClasspathFiles classpathFiles = parser.getClasspath(projectName);

        String targetName = BuildUtils.getJUnitTargetName(projectName);
        String compileTargetName = BuildUtils.getCompileTargetName(projectName);
        String testsFolder = classpathFiles.getProjectFolder().getAbsolutePath();
        String binFoldername = BuildUtils.getBinFoldername(classpathFiles);
        String fileset = BuildUtils.buildFileset(projectName, parser);
        String junitSourceFolders = BuildUtils.buildJUnitSources(classpathFiles);

        // File reportsFolder = SpecsIo.mkdir(repFolder, "reports");
        //
        // // Clean reports
        // SpecsIo.deleteFolderContents(reportsFolder);
        //
        // String reportsDir = reportsFolder.getAbsolutePath();

        String reportsDir = BuildUtils.getReportsDir(repFolder);

        Replacer projectBuild = new Replacer(BuildResource.JUNIT_TEMPLATE);

        projectBuild.replace("<JUNIT_TARGET_NAME>", targetName);
        projectBuild.replace("<COMPILE_TARGET_NAME>", compileTargetName);
        projectBuild.replace("<PROJECT_NAME>", projectName);
        projectBuild.replace("<TESTS_FOLDER>", testsFolder);
        projectBuild.replace("<FILESET>", fileset);
        projectBuild.replace("<BIN_FOLDER>", binFoldername);
        projectBuild.replace("<SOURCE_FOLDERS>", junitSourceFolders);
        projectBuild.replace("<REPORT_DIR>", reportsDir);

        return projectBuild.toString();
    }

    private String buildBenchmarkerTarget(String projectName) {
        ClasspathFiles classpathFiles = parser.getClasspath(projectName);

        String targetName = BuildUtils.getBenchmarkerTargetName(projectName);
        String testsFolder = classpathFiles.getProjectFolder().getAbsolutePath();
        String binFoldername = BuildUtils.getBinFoldername(classpathFiles);
        String fileset = BuildUtils.buildFileset(projectName, parser);
        String junitSourceFolders = BuildUtils.buildBenchmarkerSources(classpathFiles);

        // File reportsFolder = SpecsIo.mkdir(repFolder, "reports");
        //
        // // Clean reports
        // SpecsIo.deleteFolderContents(reportsFolder);
        //
        // String reportsDir = reportsFolder.getAbsolutePath();

        String reportsDir = BuildUtils.getReportsDir(repFolder);

        Replacer projectBuild = new Replacer(BuildResource.BENCHMARKER_TEMPLATE);

        projectBuild.replace("<BENCHMARKER_TARGET_NAME>", targetName);
        projectBuild.replace("<PROJECT_NAME>", projectName);
        projectBuild.replace("<TESTS_FOLDER>", testsFolder);
        projectBuild.replace("<FILESET>", fileset);
        projectBuild.replace("<BIN_FOLDER>", binFoldername);
        projectBuild.replace("<SOURCE_FOLDERS>", junitSourceFolders);
        projectBuild.replace("<REPORT_DIR>", reportsDir);

        return projectBuild.toString();
    }

    private List<String> getProjectNames() {
        List<String> projectNames = new ArrayList<>();

        // Get all projects
        for (String projectName : parser.getEclipseProjects().getProjectNames()) {

            // If cannot get classpath files for any reason, ignore it
            // (i.e., project is not supposed to be built and does not contain a .classpath file.
            Optional<ClasspathFiles> classpathFiles = getClasspath(projectName);
            if (!classpathFiles.isPresent()) {
                SpecsLogs.msgInfo("Skipping project '" + projectName + "' (could not get classpath information)");
                continue;
            }

            // Ignore project if it does not have sources
            if (classpathFiles.get().getSourceFolders().isEmpty()) {
                SpecsLogs.msgInfo("Skipping project '" + projectName + "' (no source folder found)");
                continue;
            }

            // Ignore project if in ignore list
            if (projectsToIgnore.contains(projectName)) {
                SpecsLogs.msgInfo("Skipping project '" + projectName + "' (it is in ignore list)");
                continue;
            }

            projectNames.add(projectName);
        }

        return projectNames;
    }

    private Optional<ClasspathFiles> getClasspath(String projectName) {
        try {
            return Optional.of(parser.getClasspath(projectName));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private String buildCompileTarget(String projectName) {
        ClasspathFiles classpathFiles = parser.getClasspath(projectName);

        String targetName = BuildUtils.getCompileTargetName(projectName);
        // String projectDependencies = BuildUtils.getDependencies(classpathFiles.getParentProjects());
        String projectDependencies = BuildUtils.getDependencies(classpathFiles);

        String outputJar = BuildUtils.getOutputJar(projectName).getAbsolutePath();
        String fileset = BuildUtils.buildFileset(projectName, parser);
        String binFoldername = BuildUtils.getBinFoldername(classpathFiles);
        String sourcePath = BuildUtils.getSourcePath(classpathFiles, ignoreTestFolders);
        String copyTask = BuildUtils.getCopyTask(classpathFiles);
        String ivyResolve = BuildUtils.getResolveTask(classpathFiles);
        String commands = BuildUtils.getCommandsTask(classpathFiles);
        // String ivyResolve = BuildUtils.getResolveTask(parser, projectName);

        Replacer projectBuild = new Replacer(BuildResource.COMPILE_TEMPLATE);

        projectBuild.replace("<COMPILE_TARGET_NAME>", targetName);
        projectBuild.replace("<PROJECT_DEPENDENCIES>", projectDependencies);
        projectBuild.replace("<COMMANDS>", commands);
        projectBuild.replace("<OUTPUT_JAR_FILE>", outputJar);
        projectBuild.replace("<FILESET>", fileset);
        projectBuild.replace("<PROJECT_NAME>", projectName);
        projectBuild.replace("<BIN_FOLDER>", binFoldername);
        projectBuild.replace("<SOURCE_PATH>", sourcePath);
        projectBuild.replace("<COPY_TASK>", copyTask);
        projectBuild.replace("<IVY_RESOLVE>", ivyResolve);
        projectBuild.replace("<JAVAC_FORK>", ""); // Disabled

        return projectBuild.toString();
    }

}
