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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Preconditions;

import pt.up.fe.specs.eclipse.Classpath.ClasspathFiles;
import pt.up.fe.specs.eclipse.Classpath.ClasspathParser;
import pt.up.fe.specs.eclipse.Utilities.DeployUtils;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.parsing.arguments.ArgumentsParser;
import pt.up.fe.specs.util.utilities.LineStream;
import pt.up.fe.specs.util.utilities.Replacer;

public class BuildUtils {

    private static final String REPORTS_FOLDERNAME = "reports-eclipse-build";

    // private static final String RESOURCES_FOLDER = "resources";

    /**
     * The target name, which is "build_<PROJECT_NAME>".
     * 
     * @return
     */
    public static String getCompileTargetName(String projectName) {
        return "compile_" + projectName;
    }

    /**
     * A list with the name of the dependencies of the project.
     * 
     * @param classpathFiles
     * @return
     */
    public static String getDependencies(ClasspathFiles classpathFiles) {

        StringBuilder builder = new StringBuilder();

        // String dependencies = getDependenciesPrivate(classpathFiles.getDependentProjects(), true);
        builder.append(getDependenciesPrivate(classpathFiles.getDependentProjects(), true));

        if (classpathFiles.getIvyPath().isPresent()) {
            // Add comma, if not empty
            if (builder.length() != 0) {
                builder.append(",");
            }
            // dependencies = dependencies + "," + BuildUtils.getIvyTargetName(classpathFiles.getProjectName());
            builder.append(BuildUtils.getIvyTargetName(classpathFiles.getProjectName()));
        }

        // if (dependencies.isEmpty()) {
        if (builder.length() == 0) {
            // return dependencies;
            return "";
        }

        // return "depends=\"" + dependencies + "\"";
        return "depends=\"" + builder.toString() + "\"";
    }

    public static String getIvyTargetName(String projectName) {
        return "resolve_" + projectName;
    }

    public static String getDependenciesSuffix(List<String> projects) {
        return getDependenciesPrivate(projects, false);
    }

    private static String getDependenciesPrivate(List<String> projects, boolean firstDependencies) {

        if (projects.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        // builder.append("depends=\"");

        // Append first
        if (!firstDependencies) {
            builder.append(",");
        }
        builder.append(getCompileTargetName(projects.get(0)));
        // Append remaining
        for (int i = 1; i < projects.size(); i++) {
            // for (String project : projects) {
            builder.append(",");
            builder.append(getCompileTargetName(projects.get(i)));
            // builder.append(getCompileTargetName(project));
        }
        // builder.append("\"");

        return builder.toString();
    }

    /**
     * A list with the name of the junit dependencies of the project, without "depends", just the name of the targets.
     * 
     * @param projects
     * @return
     */
    public static String getJUnitTargetDependencies(Collection<String> projects) {
        return getJUnitTargetDependencies(projects, false);
    }

    /**
     * 
     * @param projects
     * @param isFirst
     *            must be true if the target dependencies are the first elements of the property where they will be
     *            used.
     * @return
     */
    public static String getJUnitTargetDependencies(Collection<String> projects, boolean isFirst) {

        if (projects.isEmpty()) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        boolean skipFirstComma = isFirst;

        // Append all (there is already a "depends" of the junit target)
        for (String projectName : projects) {
            // for (int i = 0; i < projects.size(); i++) {
            if (skipFirstComma) {
                skipFirstComma = false;
            } else {
                builder.append(",");
            }

            builder.append(getJUnitTargetName(projectName));
        }

        return builder.toString();
    }

    public static File getOutputJar(String projectName) {
        File outputFolder = getOutputJarFolder();
        return new File(outputFolder, projectName + ".jar");
    }

    private static File getOutputJarFolder() {
        return SpecsIo.mkdir("./jars");
    }

    // public static String buildFileset(String projectName, ClasspathParser parser) {
    public static String buildFileset(String projectName, ClasspathParser parser) {

        ClasspathFiles classpathFiles = parser.getClasspath(projectName);

        final String prefix = "			";
        StringBuilder fileset = new StringBuilder();

        // Add JAR files
        for (File jarFile : classpathFiles.getJarFiles()) {
            String line = DeployUtils.getZipfileset(jarFile);

            fileset.append(prefix);
            fileset.append(line);
            fileset.append("\n");
        }

        // Add self jar and parent projects JARs
        // List<String> projects = new ArrayList<>();
        // projects.add(classpathFiles.getProjectName());
        // projects.addAll(classpathFiles.getParentProjects());

        for (String parent : classpathFiles.getDependentProjects()) {
            // Get project jar
            // File jarFile = getOutputJar(parent);
            // String line = DeployUtils.getZipfileset(jarFile);
            String line = DeployUtils.getPathElement(new File(getBinFoldername(parser.getClasspath(parent))));

            fileset.append(prefix);
            fileset.append(line);
            fileset.append("\n");
        }

        // If classpath has an ivy path, add Ivy jar folder
        if (classpathFiles.getIvyPath().isPresent()) {
            File projectFolder = classpathFiles.getProjectFolder();
            fileset.append(getIvyFileset(projectFolder)).append("\n");
        }

        // Add Ivy jar folders of other projects
        for (String project : classpathFiles.getProjectsWithIvy()) {
            ClasspathFiles projectFiles = parser.getClasspath(project);
            assert projectFiles.getIvyPath().isPresent() : "Needs to have an Ivy definition";
            File projectFolder = projectFiles.getProjectFolder();
            fileset.append(getIvyFileset(projectFolder)).append("\n");
            // System.out.println("ADDING IVY JARS of '" + project + "' to '" + classpathFiles.getProjectName() + "'");
        }

        return fileset.toString();
    }

    private static String getIvyFileset(File projectNameWithIvy) {
        String ivyJarFolder = BuildUtils.getIvyJarFoldername(projectNameWithIvy);
        Replacer ivyFileset = new Replacer(BuildResource.JARFOLDER_TEMPLATE);
        ivyFileset.replace("<FOLDER>", ivyJarFolder);
        String ivyFilesetString = ivyFileset.toString();
        return ivyFilesetString;
    }

    public static String getIvyJarFoldername(File projectFolder) {
        // return new File(projectFolder, "ivy").getAbsolutePath();
        return getIvyJarFolder(projectFolder).getAbsolutePath();
    }

    public static File getIvyJarFolder(File projectFolder) {
        return new File(projectFolder, "ivy");
    }

    /**
     * Returns <project_folder>/bin
     * 
     * @param classpathFiles
     * @return
     */
    public static String getBinFoldername(ClasspathFiles classpathFiles) {
        // File binFolder = IoUtils.safeFolder(classpathFiles.getProjectFolder(), "bin");
        // return binFolder.getAbsolutePath();
        return getBinFolder(classpathFiles.getProjectFolder()).getAbsolutePath();
    }

    public static File getBinFolder(File projectFolder) {
        return SpecsIo.mkdir(projectFolder, "bin");
    }

    /**
     * 
     * @param ignoreTestFolders
     *            if true, ignores folders that end with '-test'
     */
    public static String getSourcePath(ClasspathFiles classpathFiles, boolean ignoreTestFolders) {
        StringBuilder builder = new StringBuilder();

        String sourceTemplate = "<src path=\"<SOURCE_FOLDER>\"/>";

        for (String source : classpathFiles.getSourceFolders()) {
            File sourceFolder = new File(classpathFiles.getProjectFolder(), source);
            if (ignoreTestFolders && isTestFolder(sourceFolder.getName())) {
                continue;
            }

            builder.append(sourceTemplate.replace("<SOURCE_FOLDER>", sourceFolder.getAbsolutePath()));
            builder.append("\n");
        }

        return builder.toString();
    }

    /**
     * Is test folder if the name ends with '-test'
     */
    private static boolean isTestFolder(String srcFoldername) {
        int dashIndex = srcFoldername.lastIndexOf('-');

        // If no dash, return
        if (dashIndex == -1) {
            return false;
        }

        // If no chars after dash, return
        if (dashIndex == srcFoldername.length() - 1) {
            return false;
        }
        String suffix = srcFoldername.substring(dashIndex + 1);

        return suffix.equals("test");
    }

    public static String getJUnitTargetName(String projectName) {
        return "junit_" + projectName;
    }

    public static String getBenchmarkerTargetName(String projectName) {
        return "bench_" + projectName;
    }

    public static String buildJUnitSources(ClasspathFiles classpathFiles) {
        String template = "	       <fileset dir=\"<SOURCE_FOLDER>\">\n" +
                "	            <include name=\"**/*Test.java\" />\n" +
                "	            <include name=\"**/*Tests.java\" />\n" +
                "	       </fileset>";

        StringBuilder builder = new StringBuilder();

        for (String sourceFolder : classpathFiles.getSourceFolders()) {
            File src = new File(classpathFiles.getProjectFolder(), sourceFolder);
            String parsedTemplate = template.replace("<SOURCE_FOLDER>", src.getAbsolutePath());

            builder.append(parsedTemplate).append("\n");
        }

        return builder.toString();
    }

    public static String buildBenchmarkerSources(ClasspathFiles classpathFiles) {
        String template = "	       <fileset dir=\"<SOURCE_FOLDER>\">\n" +
                "	            <include name=\"**/*Runner.java\" />\n" +
                "	       </fileset>";

        StringBuilder builder = new StringBuilder();

        for (String sourceFolder : classpathFiles.getSourceFolders()) {
            File src = new File(classpathFiles.getProjectFolder(), sourceFolder);
            String parsedTemplate = template.replace("<SOURCE_FOLDER>", src.getAbsolutePath());

            builder.append(parsedTemplate).append("\n");
        }

        return builder.toString();
    }

    /**
     * Creates a copy task for each source folder.
     */
    public static String getCopyTask(ClasspathFiles classpathFiles) {

        // Check if it has 'resources' folder
        List<String> sources = classpathFiles.getSourceFolders();
        if (sources.isEmpty()) {
            return "";
        }

        // String template = "<copy todir=\"<BIN_FOLDER>\">\n" +
        // " <fileset dir=\"<RESOURCE_FOLDER>\" includes=\"**/*\">\n" +
        // " <fileset dir=\"<RESOURCE_FOLDER>\" includes=\"**/*\">\n" +
        // " </copy>";

        StringBuilder builder = new StringBuilder();

        for (String source : sources) {
            // Create copy task
            // Replacer replacer = new Replacer(template);
            Replacer replacer = new Replacer(BuildResource.COPY_TEMPLATE);

            File resourceFolder = new File(classpathFiles.getProjectFolder(), source);

            replacer.replace("<BIN_FOLDER>", getBinFoldername(classpathFiles));
            replacer.replace("<RESOURCE_FOLDER>", resourceFolder.getAbsolutePath());

            builder.append(replacer.toString());
        }

        return builder.toString();
        /*
        boolean hasResources = false;
        for (String source : sources) {
        if (source.equals(RESOURCES_FOLDER)) {
        	hasResources = true;
        	break;
        }
        }
        
        if (!hasResources) {
        return "";
        }
        */
        /*
        	// Create copy task
        	Replacer replacer = new Replacer(template);
        
        	File resourceFolder = new File(classpathFiles.getProjectFolder(), RESOURCES_FOLDER);
        
        	replacer.replace("<BIN_FOLDER>", getBinFolder(classpathFiles));
        	replacer.replace("<RESOURCE_FOLDER>", resourceFolder.getAbsolutePath());
        
        	return replacer.toString();
        	*/
    }

    public static String getResolveTask(ClasspathFiles classpathFiles) {
        // public static String getResolveTask(ClasspathParser parser, String projectName) {
        // ClasspathFiles classpathFiles = parser.getClasspath(projectName);

        if (!classpathFiles.getIvyPath().isPresent()) {
            // if (!parser.usesIvy()) {
            return "";
        }

        String ivyFile = new File(classpathFiles.getProjectFolder(), classpathFiles.getIvyPath().get())
                .getAbsolutePath();

        Replacer replacer = new Replacer(BuildResource.RESOLVE_TEMPLATE);

        replacer.replace("<RESOLVE_TARGET_NAME>", getIvyTargetName(classpathFiles.getProjectName()));
        replacer.replace("<IVY_FILE_LOCATION>", ivyFile);
        replacer.replace("<IVY_FOLDER_LOCATION>", getIvyJarFoldername(classpathFiles.getProjectFolder()));

        return replacer.toString();
    }

    // All resolve targets resolves
    public static String getResolveTasks(ClasspathParser parser, Collection<String> projects) {

        return projects.stream()
                .map(project -> getResolveTask(parser.getClasspath(project)))
                .filter(resolveTask -> !resolveTask.isEmpty())
                .collect(Collectors.joining("\n\n"));
    }

    public static String getIvyDependency(ClasspathParser parser) {
        if (parser.usesIvy()) {
            return "xmlns:ivy=\"antlib:org.apache.ivy.ant\"";
        }

        return "";
    }

    public static String getIvyDepends(Collection<String> ivyProjects) {
        if (ivyProjects.isEmpty()) {
            return "";
        }

        return ivyProjects.stream()
                .map(projectName -> getIvyTargetName(projectName))
                .collect(Collectors.joining(",", "depends=\"", "\""));

        // return "depends=\"" +

        // getIvyTargetName(projectName) + "\"";
    }

    /**
     * Returns a collection of strings with projects that use Ivy to resolve dependencies.
     * 
     * @param parser
     * @param dependentProjects
     * @return
     */
    public static Collection<String> filterProjectsWithIvy(ClasspathParser parser,
            Collection<String> dependentProjects) {

        return dependentProjects.stream()
                .filter(projectName -> parser.getClasspath(projectName).getIvyPath().isPresent())
                .collect(Collectors.toList());

    }

    public static String getCommandsTask(ClasspathFiles classpathFiles) {
        // If no commands file, return empty
        if (!classpathFiles.getCommandsFile().isPresent()) {
            return "";
        }

        StringBuilder tasks = new StringBuilder();

        final String commentPrefix = "#";

        // Read file, each line that is not a comment is a command
        try (LineStream lines = LineStream.newInstance(classpathFiles.getCommandsFile().get())) {
            while (lines.hasNextLine()) {
                String line = lines.nextLine().trim();

                // Ignore empty lines
                if (line.isEmpty()) {
                    continue;
                }

                if (line.startsWith(commentPrefix)) {
                    continue;
                }

                tasks.append(buildExecTask(line, classpathFiles.getProjectFolder())).append("\n");
            }

        }

        return tasks.toString();

    }

    private static String buildExecTask(String line, File projectFolder) {
        List<String> arguments = ArgumentsParser.newCommandLine().parse(line);

        Preconditions.checkArgument(!arguments.isEmpty(), "Expected at least one argument:" + line);

        // Parser arguments for exec options
        ExecTaskConfig execTaskConfig = ExecTaskConfig.parseArguments(arguments);

        File workingDir = execTaskConfig.getWorkingDir()
                .map(dir -> new File(projectFolder, dir))
                .orElse(projectFolder);

        StringBuilder task = new StringBuilder();

        // First argument is the command (unless we use the first arg to indicate OS-specific commands)
        task.append("<exec executable=\"").append(arguments.get(0))
                .append("\" failonerror=\"true\" dir=\"")
                .append(workingDir.getAbsolutePath())
                .append("\">\n");

        // Add arguments
        for (int i = 1; i < arguments.size(); i++) {
            task.append("   <arg value=\"").append(arguments.get(i)).append("\"/>\n");
        }

        // Close task
        task.append("</exec>\n");
        return task.toString();
    }

    /**
     * Helper method which uses the current working directory as the base folder.
     * 
     * @return
     */
    public static String getReportsDir() {
        return getReportsDir(SpecsIo.getWorkingDir());
    }

    public static String getReportsDir(File baseFolder) {
        // public static String getReportsDir(File baseFolder, String foldernameSuffix) {

        // SpecsCheck.checkArgument(!foldernameSuffix.isBlank(), () -> "Foldername suffix cannot be empty.");

        File reportsFolder = SpecsIo.mkdir(baseFolder, REPORTS_FOLDERNAME);

        // Clean reports
        SpecsIo.deleteFolderContents(reportsFolder);

        return SpecsIo.getCanonicalPath(reportsFolder);
    }
}
