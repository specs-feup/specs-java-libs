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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import pt.up.fe.specs.eclipse.Classpath.ClasspathFiles;
import pt.up.fe.specs.eclipse.Classpath.ClasspathParser;
import pt.up.fe.specs.eclipse.Tasks.TaskExecutor;
import pt.up.fe.specs.eclipse.Tasks.TaskUtils;
import pt.up.fe.specs.eclipse.Utilities.DeployUtils;
import pt.up.fe.specs.eclipse.builder.BuildUtils;
import pt.up.fe.specs.guihelper.BaseTypes.SetupData;
import pt.up.fe.specs.lang.SpecsPlatforms;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.ProgressCounter;
import pt.up.fe.specs.util.utilities.Replacer;

/**
 * Builds and deploys Eclipse projects.
 *
 * @author Joao Bispo
 */
public class EclipseDeployment {

    private static final String BUILD_FILE = "build.xml";

    private static final Map<String, TaskExecutor> tasks = TaskUtils.getTasksByName();

    private static final Map<JarType, Consumer<EclipseDeploymentData>> DEPLOY_BUILDER;
    static {
        DEPLOY_BUILDER = new HashMap<>();
        EclipseDeployment.DEPLOY_BUILDER.put(JarType.SubfolderZip, EclipseDeployment::buildSubfolderZip);
        EclipseDeployment.DEPLOY_BUILDER.put(JarType.OneJar, EclipseDeployment::buildOneJar);
        EclipseDeployment.DEPLOY_BUILDER.put(JarType.RepackJar, EclipseDeployment::buildJarRepack);
        EclipseDeployment.DEPLOY_BUILDER.put(JarType.UseJarInJar, EclipseDeployment::buildJarInJar);
        EclipseDeployment.DEPLOY_BUILDER.put(JarType.MavenRepository, EclipseDeployment::buildMavenRepository);
    }

    private final EclipseDeploymentData data;
    // private final Map<String, TaskExecutor> taskMap;

    public EclipseDeployment(EclipseDeploymentData data) {
        this.data = data;
        // this.taskMap = buildTasks();
    }

    // private Map<String, TaskExecutor> buildTasks() {
    // // TODO Auto-generated method stub
    // return null;
    // }

    public int execute() {

        // Clear temporary folder
        DeployUtils.clearTempFolder();

        // Resolve Ivy
        resolveIvy();

        // Check if case is defined
        if (!EclipseDeployment.DEPLOY_BUILDER.containsKey(data.jarType)) {
            SpecsLogs.warn("Case not defined:" + data.jarType);
        }

        // Build JAR
        EclipseDeployment.DEPLOY_BUILDER
                .getOrDefault(data.jarType, EclipseDeployment::buildJarRepack)
                .accept(data);

        // Execute tasks
        processTasks();

        return 0;
    }

    private void resolveIvy() {
        ClasspathParser parser = ClasspathParser.newFromWorkspace(data.workspaceFolder);

        Collection<String> dependentProjects = parser.getDependentProjects(data.projetName);
        Collection<String> projectsWithIvy = BuildUtils.filterProjectsWithIvy(parser, dependentProjects);

        // Replace fields in template
        String template = SpecsIo.getResource(DeployResource.IVY_RESOLVE_TEMPLATE);

        // System.out.println("IVY RESOLVE 1:\n" + BuildUtils.getResolveTasks(parser, dependentProjects));
        // System.out.println("IVY RESOLVE 2:\n" + BuildUtils.getResolveTask(classpathFiles));
        template = template.replace("<IVY_RESOLVE>", BuildUtils.getResolveTasks(parser, dependentProjects));
        template = template.replace("<USE_IVY>", BuildUtils.getIvyDependency(parser));
        template = template.replace("<IVY_DEPENDENCIES>", BuildUtils.getIvyDepends(projectsWithIvy));
        // System.out.println("BUILD FILE:\n" + template);

        // Save script
        File buildFile = new File(EclipseDeployment.BUILD_FILE);
        SpecsIo.write(buildFile, template);

        // Launch ant
        Project project = new Project();
        project.init();

        ProjectHelper.configureProject(project, buildFile);

        project.addBuildListener(DeployUtils.newStdoutListener());
        project.executeTarget(project.getDefaultTarget());

    }

    /**
     *
     */
    public void processTasks() {

        ProgressCounter progress = new ProgressCounter(data.tasks.getNumSetups());

        for (SetupData setup : data.tasks.getMapOfSetups()) {
            String setupName = setup.getSetupName();

            System.out.println("Executing task '" + setupName + "' " + progress.next());

            // Get task
            TaskExecutor task = EclipseDeployment.tasks.get(setupName);

            if (task == null) {
                SpecsLogs.warn("Could not find task for setup '" + setupName + "', available names: "
                        + EclipseDeployment.tasks.keySet());
                continue;
            }

            task.execute(setup, data);
        }

    }

    /**
     *
     */
    /*
    private void deployFtp() {
    String script = "open specsuser:SpecS#12345@specs.fe.up.pt\r\n" + "bin\r\n"
    	+ "cd /home/specsuser/tools/gearman_server\r\n" + "put C:\\temp_output\\deploy\\suika.properties\r\n"
    	+ "ls\r\n" + "exit";
    
    IoUtils.write(new File("ftp_script.txt"), script);
    
    ProcessUtils.run(Arrays.asList("WinSCP.com", "/script=ftp_script.txt"), IoUtils.getWorkingDir()
    	.getAbsolutePath());
    
    }
    */

    /**
     * Builds a JAR with additional library JARs inside. Uses a custom class loader.
     */
    private static void buildJarInJar(EclipseDeploymentData data) {
        ClasspathParser parser = ClasspathParser.newFromWorkspace(data.workspaceFolder);

        ClasspathFiles classpathFiles = parser.getClasspath(data.projetName);

        Collection<String> dependentProjects = parser.getDependentProjects(data.projetName);
        Collection<String> projectsWithIvy = BuildUtils.filterProjectsWithIvy(parser, dependentProjects);
        Collection<String> ivyFolders = projectsWithIvy.stream()
                .map(ivyProject -> BuildUtils.getIvyJarFoldername(parser.getClasspath(ivyProject).getProjectFolder()))
                .collect(Collectors.toList());

        String fileset = DeployUtils.buildFileset(parser, data.projetName, ivyFolders, false);

        String jarList = DeployUtils.buildJarList(classpathFiles, ivyFolders);

        // Replace fields in template
        String template = SpecsIo.getResource(DeployResource.DEPLOY_JAR_IN_JAR_TEMPLATE);

        // Output JAR
        File outputJar = DeployUtils.getOutputJar(data);

        template = template.replace("<OUTPUT_JAR_FILE>", outputJar.getAbsolutePath());
        template = template.replace("<MAIN_CLASS>", data.mainClass);
        template = template.replace("<JAR_LIST>", jarList);
        template = template.replace("<FILESET>", fileset);

        // System.out.println("IVY RESOLVE 1:\n" + BuildUtils.getResolveTasks(parser, dependentProjects));
        // System.out.println("IVY RESOLVE 2:\n" + BuildUtils.getResolveTask(classpathFiles));
        template = template.replace("<IVY_RESOLVE>", BuildUtils.getResolveTasks(parser, dependentProjects));
        template = template.replace("<USE_IVY>", BuildUtils.getIvyDependency(parser));
        template = template.replace("<IVY_DEPENDENCIES>", BuildUtils.getIvyDepends(projectsWithIvy));
        template = template.replace("<DELETE_IVY>", DeployUtils.getDeleteIvyFolders(ivyFolders));
        // System.out.println("BUILD FILE:\n" + template);
        // Save script
        File buildFile = new File(EclipseDeployment.BUILD_FILE);
        SpecsIo.write(buildFile, template);

        // Run script
        // ProcessUtils.run(Arrays.asList("ant", "build.xml"), IoUtils.getWorkingDir().getPath());
        // Launch ant
        Project project = new Project();
        project.init();

        ProjectHelper.configureProject(project, buildFile);

        project.addBuildListener(DeployUtils.newStdoutListener());
        project.executeTarget(project.getDefaultTarget());
        // System.out.println("OUTPUT JAR:" + outputJar.getAbsolutePath());
        // Check if jar file exists
        if (!outputJar.isFile()) {
            throw new RuntimeException("Could not create output JAR '" + outputJar.getAbsolutePath() + "'");
        }

    }

    /**
     * Creates a single JAR with additional library JARs extracted into the file.
     */
    private static void buildJarRepack(EclipseDeploymentData data) {
        ClasspathParser parser = ClasspathParser.newFromWorkspace(data.workspaceFolder);

        ClasspathFiles classpathFiles = parser.getClasspath(data.projetName);

        Collection<String> dependentProjects = parser.getDependentProjects(data.projetName);
        Collection<String> projectsWithIvy = BuildUtils.filterProjectsWithIvy(parser, dependentProjects);
        Collection<String> ivyFolders = projectsWithIvy.stream()
                .map(ivyProject -> BuildUtils.getIvyJarFoldername(parser.getClasspath(ivyProject).getProjectFolder()))
                .collect(Collectors.toList());

        String fileset = DeployUtils.buildFileset(parser, data.projetName, ivyFolders, true);
        String jarList = DeployUtils.buildJarList(classpathFiles, ivyFolders);

        // Replace fields in template
        String template = SpecsIo.getResource(DeployResource.DEPLOY_REPACK_TEMPLATE);

        // Output JAR
        File outputJar = DeployUtils.getOutputJar(data);

        template = template.replace("<OUTPUT_JAR_FILE>", outputJar.getAbsolutePath());
        template = template.replace("<MAIN_CLASS>", data.mainClass);
        template = template.replace("<JAR_LIST>", jarList);
        template = template.replace("<FILESET>", fileset);

        // System.out.println("IVY RESOLVE 1:\n" + BuildUtils.getResolveTasks(parser, dependentProjects));
        // System.out.println("IVY RESOLVE 2:\n" + BuildUtils.getResolveTask(classpathFiles));
        template = template.replace("<IVY_RESOLVE>", BuildUtils.getResolveTasks(parser, dependentProjects));
        template = template.replace("<USE_IVY>", BuildUtils.getIvyDependency(parser));
        template = template.replace("<IVY_DEPENDENCIES>", BuildUtils.getIvyDepends(projectsWithIvy));
        template = template.replace("<DELETE_IVY>", DeployUtils.getDeleteIvyFolders(ivyFolders));
        template = template.replace("<BUILD_NUMBER_ATTR>", SpecsSystem.getBuildNumberAttr());
        template = template.replace("<BUILD_NUMBER>", data.getBuildNumber());

        // System.out.println("BUILD FILE:\n" + template);
        // Save script
        File buildFile = new File(EclipseDeployment.BUILD_FILE);
        SpecsIo.write(buildFile, template);

        // Run script
        // ProcessUtils.run(Arrays.asList("ant", "build.xml"), IoUtils.getWorkingDir().getPath());
        // Launch ant
        Project project = new Project();
        project.init();

        ProjectHelper.configureProject(project, buildFile);

        project.addBuildListener(DeployUtils.newStdoutListener());
        project.executeTarget(project.getDefaultTarget());
        // System.out.println("OUTPUT JAR:" + outputJar.getAbsolutePath());
        // Check if jar file exists
        if (!outputJar.isFile()) {
            throw new RuntimeException("Could not create output JAR '" + outputJar.getAbsolutePath() + "'");
        }

    }

    // Creates a JAR file with additional library JARs inside a folder. Optionally, zips the JAR file and folders.
    // private static void buildSubfolderZip(EclipseDeploymentData data, boolean zip) {

    /**
     * Creates a zip file with a JAR and additional library JARs inside a folder.
     */
    private static void buildSubfolderZip(EclipseDeploymentData data) {

        ClasspathParser parser = ClasspathParser.newFromWorkspace(data.workspaceFolder);

        ClasspathFiles classpathFiles = parser.getClasspath(data.projetName);

        Collection<String> dependentProjects = parser.getDependentProjects(data.projetName);
        Collection<String> projectsWithIvy = BuildUtils.filterProjectsWithIvy(parser, dependentProjects);
        Collection<String> ivyFolders = projectsWithIvy.stream()
                .map(ivyProject -> BuildUtils.getIvyJarFoldername(parser.getClasspath(ivyProject).getProjectFolder()))
                .collect(Collectors.toList());

        String mainFileset = DeployUtils.buildMainFileset(parser, data.projetName);
        // String jarList = DeployUtils.buildJarList(classpathFiles, ivyFolders);

        // Replace fields in template
        String template = SpecsIo.getResource(DeployResource.DEPLOY_SUBFOLDER_ZIP_TEMPLATE);

        // Output JAR
        File outputJar = DeployUtils.getOutputJar(data);
        String outputJarname = outputJar.getName();
        String outputJarFoldername = outputJar.getParent();
        String libFoldername = data.projetName + "_lib";

        List<File> jarFileList = DeployUtils.getJarFiles(classpathFiles.getJarFiles(), ivyFolders, false);

        String jarList = jarFileList.stream()
                .map(jarFile -> libFoldername + "/" + jarFile.getName())
                .collect(Collectors.joining(" "));

        String jarZipfileset = DeployUtils.buildJarZipfileset(jarFileList, libFoldername);

        // Output Zip
        File outputZip = new File(outputJar.getParentFile(), SpecsIo.removeExtension(outputJar) + ".zip");

        // Set output file as being the zip
        data.setResultFile(outputZip);

        template = template.replace("<OUTPUT_JAR_FILE>", outputJar.getAbsolutePath());
        template = template.replace("<OUTPUT_JAR_FOLDER>", outputJarFoldername);
        template = template.replace("<OUTPUT_JAR_FILENAME>", outputJarname);
        template = template.replace("<OUTPUT_ZIP_FILE>", outputZip.getAbsolutePath());
        template = template.replace("<MAIN_CLASS>", data.mainClass);
        template = template.replace("<JAR_LIST>", jarList);
        template = template.replace("<MAIN_FILESET>", mainFileset);
        template = template.replace("<JAR_ZIPFILESET>", jarZipfileset);
        template = template.replace("<BUILD_NUMBER_ATTR>", SpecsSystem.getBuildNumberAttr());
        template = template.replace("<BUILD_NUMBER>", data.getBuildNumber());

        // System.out.println("IVY RESOLVE 1:\n" + BuildUtils.getResolveTasks(parser, dependentProjects));
        // System.out.println("IVY RESOLVE 2:\n" + BuildUtils.getResolveTask(classpathFiles));
        template = template.replace("<IVY_RESOLVE>", BuildUtils.getResolveTasks(parser, dependentProjects));
        template = template.replace("<USE_IVY>", BuildUtils.getIvyDependency(parser));
        template = template.replace("<IVY_DEPENDENCIES>", BuildUtils.getIvyDepends(projectsWithIvy));
        template = template.replace("<DELETE_IVY>", DeployUtils.getDeleteIvyFolders(ivyFolders));
        // System.out.println("BUILD FILE:\n" + template);
        // Save script
        File buildFile = new File(EclipseDeployment.BUILD_FILE);
        SpecsIo.write(buildFile, template);

        // Run script
        // ProcessUtils.run(Arrays.asList("ant", "build.xml"), IoUtils.getWorkingDir().getPath());
        // Launch ant
        Project project = new Project();
        project.init();

        ProjectHelper.configureProject(project, buildFile);

        project.addBuildListener(DeployUtils.newStdoutListener());
        project.executeTarget(project.getDefaultTarget());
        // System.out.println("OUTPUT JAR:" + outputJar.getAbsolutePath());
        // Check if jar file exists
        if (!outputJar.isFile()) {
            throw new RuntimeException("Could not create output JAR '" + outputJar.getAbsolutePath() + "'");
        }

    }

    /**
     * Creates the necessary JAR files to deploy to Maven Repository.
     */
    private static void buildMavenRepository(EclipseDeploymentData data) {

        ClasspathParser parser = ClasspathParser.newFromWorkspace(data.workspaceFolder);

        ClasspathFiles classpathFiles = parser.getClasspath(data.projetName);

        Collection<String> dependentProjects = parser.getDependentProjects(data.projetName);
        Collection<String> projectsWithIvy = BuildUtils.filterProjectsWithIvy(parser, dependentProjects);
        Collection<String> ivyFolders = projectsWithIvy.stream()
                .map(ivyProject -> BuildUtils.getIvyJarFoldername(parser.getClasspath(ivyProject).getProjectFolder()))
                .collect(Collectors.toList());

        // String fileset = DeployUtils.buildFileset(parser, data.projetName, ivyFolders, false);

        // String jarList = DeployUtils.buildJarList(classpathFiles, ivyFolders);

        // Output JAR
        File outputJar = DeployUtils.getOutputJar(data);

        // Subfolder name
        String subFoldername = "libs";

        List<File> ivyJars = DeployUtils.getJarFiles(Collections.emptyList(), ivyFolders, true);

        // Subfolder gets Ivy JARs
        String subfolderJars = ivyJars.stream()
                .map(file -> subFoldername + "/" + file.getName())
                .collect(Collectors.joining(" "));

        String fileset = DeployUtils.buildProjectsFileset(parser, data.projetName).stream()
                .collect(Collectors.joining("\n" + DeployUtils.getPrefix(), DeployUtils.getPrefix(), ""));

        // Project JARs are repackaged inside JAR
        List<File> projectsJars = DeployUtils.getJarFiles(classpathFiles.getJarFiles(), Collections.emptyList(), true);

        if (!projectsJars.isEmpty()) {
            SpecsLogs.debug(() -> "Repackaging the following JARs in SubFolder deploy: " + projectsJars);

            for (var jarFile : projectsJars) {
                String line = DeployUtils.getZipfilesetExtracted(jarFile);
                fileset += "\n" + DeployUtils.getPrefix() + line;
            }

        }

        // Subfolder
        File subfolder = new File(DeployUtils.getTempFolder(), subFoldername);

        String copyJars = ivyJars.stream()
                .map(jar -> DeployUtils.getCopyTask(jar, subfolder))
                .collect(Collectors.joining("\n"));

        // Sources

        File sourcesJar = DeployUtils.getJarWithClassifier(data.nameOfOutputJar, "sources");

        String sourcesFileset = DeployUtils.buildSourcesFileset(parser, data.projetName).stream()
                .collect(Collectors.joining("\n" + DeployUtils.getPrefix()));

        // Javadoc
        File javadocFolder = new File(DeployUtils.getTempFolder(), "javadoc");
        File javadocJar = DeployUtils.getJarWithClassifier(data.nameOfOutputJar, "javadoc");

        List<File> allJars = new ArrayList<>();
        allJars.addAll(projectsJars);
        allJars.addAll(ivyJars);
        String javadocClasspath = allJars.stream()
                .map(file -> SpecsIo.normalizePath(file.getAbsoluteFile()))
                .collect(Collectors.joining(":"));
        // String javadocClasspath = ivyJars.stream()
        // .map(file -> SpecsIo.normalizePath(new File(subfolder, file.getName())))
        // .collect(Collectors.joining(":"));

        // POM file
        String mavenPom = DeployUtils.buildMavenRepoPom(data, parser);
        File pomFile = DeployUtils.getFileWithClassifier(data.nameOfOutputJar, null, "pom");
        SpecsIo.write(pomFile, mavenPom);

        // Replace fields in template
        String template = SpecsIo.getResource(DeployResource.DEPLOY_MAVEN_REPOSITORY_TEMPLATE);

        template = template.replace("<OUTPUT_JAR_FILE>", outputJar.getAbsolutePath());
        template = template.replace("<MAIN_CLASS>", data.mainClass);
        template = template.replace("<SUBFOLDER_JARS>", subfolderJars);
        template = template.replace("<FILESET>", fileset);
        template = template.replace("<JAR_SUBFOLDER>", subfolder.getAbsolutePath());
        template = template.replace("<COPY_JARS>", copyJars);
        template = template.replace("<SOURCES_JAR_FILE>", sourcesJar.getAbsolutePath());
        template = template.replace("<SOURCES_FILESET>", sourcesFileset);
        template = template.replace("<JAVADOC_FOLDER>", javadocFolder.getAbsolutePath());
        template = template.replace("<JAVADOC_JAR_FILE>", javadocJar.getAbsolutePath());
        template = template.replace("<JAVADOC_CLASSPATH>", javadocClasspath);

        File buildFile = new File(EclipseDeployment.BUILD_FILE);
        SpecsIo.write(buildFile, template);

        // Launch ant
        Project project = new Project();
        project.init();

        ProjectHelper.configureProject(project, buildFile);

        project.addBuildListener(DeployUtils.newStdoutListener());
        project.executeTarget(project.getDefaultTarget());

        // Check if jar file exists
        if (!outputJar.isFile()) {
            throw new RuntimeException("Could not create output JAR '" + outputJar.getAbsolutePath() + "'");
        }

        // Create script
        Replacer deployScript = new Replacer(DeployResource.DEPLOY_MAVEN_SCRIPT_TEMPLATE);
        deployScript.replace("%POM_FILE%", pomFile.getName());
        deployScript.replace("%JAR_FILE%", outputJar.getName());
        deployScript.replace("%SOURCE_FILE%", sourcesJar.getName());
        deployScript.replace("%JAVADOC_FILE%", javadocJar.getName());

        String scriptExtension = SpecsPlatforms.isWindows() ? ".bat" : ".sh";

        File scriptFile = new File(DeployUtils.getTempFolder(), "deploy_script" + scriptExtension);
        SpecsIo.write(scriptFile, deployScript.toString());

        SpecsLogs.msgInfo(
                "Artifacts generated, to deploy them execute the script '" + scriptFile.getAbsolutePath() + "'");
    }

    /**
     * Builds a JAR with additional library JARs inside, using Simon Tuffs One-JAR.
     */
    private static void buildOneJar(EclipseDeploymentData data) {
        ClasspathParser parser = ClasspathParser.newFromWorkspace(data.workspaceFolder);

        ClasspathFiles classpathFiles = parser.getClasspath(data.projetName);

        Collection<String> dependentProjects = parser.getDependentProjects(data.projetName);
        Collection<String> projectsWithIvy = BuildUtils.filterProjectsWithIvy(parser, dependentProjects);
        Collection<String> ivyFolders = projectsWithIvy.stream()
                .map(ivyProject -> BuildUtils.getIvyJarFoldername(parser.getClasspath(ivyProject).getProjectFolder()))
                .collect(Collectors.toList());

        String mainFileset = DeployUtils.buildMainFileset(parser, data.projetName);
        String libFileset = DeployUtils.buildLibFileset(parser, data.projetName, ivyFolders);

        String jarList = DeployUtils.buildJarList(classpathFiles, ivyFolders);

        // Replace fields in template
        String template = SpecsIo.getResource(DeployResource.DEPLOY_ONE_JAR_TEMPLATE);

        // Output JAR
        File outputJar = DeployUtils.getOutputJar(data);

        template = template.replace("<OUTPUT_JAR_FILE>", outputJar.getAbsolutePath());
        template = template.replace("<MAIN_CLASS>", data.mainClass);
        template = template.replace("<JAR_LIST>", jarList);
        template = template.replace("<MAIN_FILESET>", mainFileset);
        template = template.replace("<LIB_FILESET>", libFileset);

        template = template.replace("<IVY_RESOLVE>", BuildUtils.getResolveTasks(parser, dependentProjects));
        template = template.replace("<USE_IVY>", BuildUtils.getIvyDependency(parser));
        template = template.replace("<IVY_DEPENDENCIES>", BuildUtils.getIvyDepends(projectsWithIvy));
        template = template.replace("<DELETE_IVY>", DeployUtils.getDeleteIvyFolders(ivyFolders));

        // Save script
        File buildFile = new File(EclipseDeployment.BUILD_FILE);
        SpecsIo.write(buildFile, template);
        System.out.println("BUILD.XML:\n" + template);
        // Run script
        // ProcessUtils.run(Arrays.asList("ant", "build.xml"), IoUtils.getWorkingDir().getPath());
        // Launch ant
        Project project = new Project();
        project.init();

        ProjectHelper.configureProject(project, buildFile);

        project.addBuildListener(DeployUtils.newStdoutListener());
        project.executeTarget(project.getDefaultTarget());
        // System.out.println("OUTPUT JAR:" + outputJar.getAbsolutePath());
        // Check if jar file exists
        if (!outputJar.isFile()) {
            throw new RuntimeException("Could not create output JAR '" + outputJar.getAbsolutePath() + "'");
        }

    }
}
