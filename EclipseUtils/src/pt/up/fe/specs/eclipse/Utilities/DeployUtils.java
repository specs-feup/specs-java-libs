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

package pt.up.fe.specs.eclipse.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import pt.up.fe.specs.eclipse.DeployResource;
import pt.up.fe.specs.eclipse.EclipseDeploymentData;
import pt.up.fe.specs.eclipse.Classpath.ClasspathFiles;
import pt.up.fe.specs.eclipse.Classpath.ClasspathParser;
import pt.up.fe.specs.eclipse.Classpath.Dependency;
import pt.up.fe.specs.eclipse.builder.BuildResource;
import pt.up.fe.specs.eclipse.builder.BuildUtils;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.utilities.Replacer;

/**
 * @author Joao Bispo
 *
 */
public class DeployUtils {

    // private static final String PREFIX_PROP_USER_LIB = "org.eclipse.jdt.core.userLibrary";

    private static final String MACRO_BUILD_NUMBER = "${BUILD_NUMBER}";

    private static final String TEMPORARY_FOLDER = "temp";
    private static final String PREFIX = "           ";

    public static String getPrefix() {
        return PREFIX;
    }

    /**
     * @param projectFolder
     * @return
     */
    public static String getPathElement(File projectBinFolder) {
        // String template = "<fileset dir=\"<FOLDER>\" includes=\"**/*.*\"/>";
        String template = "<pathelement location=\"<FOLDER>\"/>";

        template = template.replace("<FOLDER>", projectBinFolder.getAbsolutePath());

        return template;
    }

    /**
     * @param projectFolder
     * @return
     */
    public static String getFileset(File projectFolder) {
        // String template = "<fileset dir=\"<FOLDER>\" includes=\"**/*.*\"/>";
        String template = "<fileset dir=\"<FOLDER>\" />";

        // String template = "<fileset dir=\"<FOLDER>\">\n"
        // + " <include name=\"**/*.class\"/>\n"
        // + "</fileset>";

        template = template.replace("<FOLDER>", projectFolder.getAbsolutePath());

        return template;
    }

    /**
     * @param projectFolder
     * @return
     */
    public static String getSourcesFileset(File projectFolder) {
        String template = "<fileset dir=\"<FOLDER>\" includes=\"**/*.java\"/>";

        template = template.replace("<FOLDER>", projectFolder.getAbsolutePath());

        return template;
    }

    /**
     * @param jarFile
     * @return
     */
    public static String getZipfileset(File jarFile) {
        // String template = "<zipfileset excludes=\"META-INF/*.SF\" dir=\"<FOLDER>\" includes=\"<FILE>\" />";
        String template = "<zipfileset excludes=\"META-INF/*.SF,**/*.java\" dir=\"<FOLDER>\" includes=\"<FILE>\" />";

        // Get canonical version, to avoid problems with Linux systems
        jarFile = SpecsIo.getCanonicalFile(jarFile);

        template = template.replace("<FOLDER>", SpecsIo.getCanonicalFile(jarFile).getParentFile().getPath());
        template = template.replace("<FILE>", jarFile.getName());

        return template;
    }

    /**
     * @param jarFile
     * @return
     */
    public static String getZipfilesetExtracted(File jarFile) {
        String template = "<zipfileset excludes=\"META-INF/*.SF,**/*.java,**/*.html\" src=\"<JARPATH>\" />";

        // Get canonical version, to avoid problems with Linux systems
        jarFile = SpecsIo.getCanonicalFile(jarFile);

        template = template.replace("<JARPATH>", SpecsIo.getCanonicalFile(jarFile).getPath());
        return template;
    }

    /**
     * Standard listener for ANT project.
     *
     * <p>
     * Outputs a message when an ANT target starts and finishes.
     *
     * @return
     */
    public static BuildListener newStdoutListener() {
        BuildListener outListener = new BuildListener() {

            @Override
            public void taskStarted(BuildEvent arg0) {
                // System.out.println("Task Started: "+arg0.getTask().getTaskName());
                // System.out.println(arg0.getMessage());
            }

            @Override
            public void taskFinished(BuildEvent arg0) {
                // System.out.println(arg0.getMessage());
            }

            @Override
            public void targetStarted(BuildEvent arg0) {
                SpecsLogs.msgInfo("[ANT]:Started target '" + arg0.getTarget() + "'");
                // System.out.println(arg0.getMessage());

            }

            @Override
            public void targetFinished(BuildEvent arg0) {
                SpecsLogs.msgInfo("[ANT]:Finished target '" + arg0.getTarget() + "'");
            }

            @Override
            public void messageLogged(BuildEvent arg0) {
                // So that it can show errors (e.g., javac)
                if (!arg0.getMessage().startsWith("[") && arg0.getPriority() < 3) {
                    SpecsLogs.msgInfo(arg0.getMessage());
                }
                // if (arg0.getPriority() < 3) {
                // System.out.println(arg0.getMessage());
                // }

                // SpecsLogs.msgInfo(arg0.getMessage());
                // System.out.println(arg0.getMessage());

            }

            @Override
            public void buildStarted(BuildEvent arg0) {
                // System.out.println("Build Started");
            }

            @Override
            public void buildFinished(BuildEvent arg0) {
                // System.out.println(arg0.getMessage());

            }
        };

        return outListener;
    }

    public static File getTempFolder() {
        File tempFolder = SpecsIo.mkdir(SpecsIo.getWorkingDir(), DeployUtils.TEMPORARY_FOLDER);

        return tempFolder;
    }

    public static void clearTempFolder() {
        SpecsIo.deleteFolderContents(getTempFolder());
    }

    /**
     * Returns a File representing the output JAR.
     *
     * @param jarFilename
     * @return
     */
    public static File getOutputJar(EclipseDeploymentData data) {
        // Process JAR filename
        var jarFilename = processFilename(data.nameOfOutputJar, data);

        // The output jar will be in a temporary folder
        File tempFolder = getTempFolder();

        return new File(tempFolder, jarFilename);
    }

    public static String processFilename(String filename, EclipseDeploymentData data) {

        // Replace macro for build number
        filename = filename.replace(MACRO_BUILD_NUMBER, data.getBuildNumber());

        return filename;
    }

    /**
     * Returns a File representing the file created from the build.
     *
     * @param jarFilename
     * @return
     */
    public static File getResultFile(EclipseDeploymentData data) {
        if (data.getResultFile() != null) {
            return data.getResultFile();
        }

        String jarFilename = data.nameOfOutputJar;

        // The output jar will be in a temporary folder
        File tempFolder = getTempFolder();

        return new File(tempFolder, jarFilename);
    }

    /*
    public static boolean hasMainMethod(String className) {
    System.err.println("NOT IMPLEMENTED");
    
    Class<?> classWithMain = null;
    try {
        classWithMain = Class.forName("className");
    } catch (ClassNotFoundException e) {
        LoggingUtils.msgInfo("Could not find class with name '" + className + "'");
        return false;
    }
    
    Method mainMethod = null;
    try {
        classWithMain.getMethod("main", String[].class);
    } catch (NoSuchMethodException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (SecurityException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    
    return false;
    }
    */
    /*
    public static String buildFileset(ClasspathFiles classpathFiles, boolean hasIvyDependencies) {
    	final String prefix = "			";
    	StringBuilder fileset = new StringBuilder();
    
    	// Add JAR Files
    	for (File jarFile : classpathFiles.getJarFiles()) {
    	    String line = DeployUtils.getZipfileset(jarFile);
    
    	    fileset.append(prefix);
    	    fileset.append(line);
    	    fileset.append("\n");
    	}
    
    	// Add Filesets
    	for (File projectFolder : classpathFiles.getBinFolders()) {
    	    String line = DeployUtils.getFileset(projectFolder);
    
    	    fileset.append(prefix);
    	    fileset.append(line);
    	    fileset.append("\n");
    	}
    
    	// If classpath has an ivy path, add Ivy jar folder
    	if (hasIvyDependencies) {
    
    	    String ivyJarFolder = BuildUtils.getIvyJarFolder(classpathFiles.getProjectFolder());
    	    // String ivyJarFolder = "${user.home}/.ivy2/cache";
    	    Replacer ivyFileset = new Replacer(BuildResource.JARFOLDER_TEMPLATE);
    	    ivyFileset.replace("<FOLDER>", ivyJarFolder);
    	    fileset.append(ivyFileset.toString()).append("\n");
    	}
    
    	return fileset.toString();
    }
    */

    public static boolean ignoreJar(File jarFile) {
        // Ignore javadoc and source
        if (jarFile.getName().contains("-javadoc") || jarFile.getName().contains("-source-")
                || jarFile.getName().contains("-sources")) {
            return true;
        }

        return false;
    }

    public static List<File> getJarFiles(List<File> classpathJars, Collection<String> ivyFolders,
            boolean filterJars) {
        List<File> jars = new ArrayList<>();

        long bytesSaved = 0l;
        List<String> ignoredJars = new ArrayList<>();
        for (File jarFile : classpathJars) {
            if (filterJars && ignoreJar(jarFile)) {
                bytesSaved += jarFile.length();
                ignoredJars.add(jarFile.getName());
                continue;
            }

            jars.add(jarFile);
        }

        for (String ivyFolder : ivyFolders) {
            List<File> jarFiles = SpecsIo.getFiles(new File(ivyFolder), "jar");

            for (File jarFile : jarFiles) {
                if (filterJars && ignoreJar(jarFile)) {
                    bytesSaved += jarFile.length();
                    ignoredJars.add(jarFile.getName());
                    continue;
                }

                jars.add(jarFile);
            }
        }

        if (!ignoredJars.isEmpty()) {
            SpecsLogs.info(
                    "Ignored the following JARs (~" + SpecsStrings.parseSize(bytesSaved) + "): " + ignoredJars);
        }

        return jars;
    }

    public static String buildJarList(ClasspathFiles classpathFiles, Collection<String> ivyFolders) {
        return getJarFiles(classpathFiles.getJarFiles(), ivyFolders, false).stream()
                .map(File::getName)
                .collect(Collectors.joining(" "));
        /*
        StringBuilder jarList = new StringBuilder();
        for (File jarFile : classpathFiles.getJarFiles()) {
            jarList.append(jarFile.getName());
            jarList.append(" ");
        }
        
        // long bytesSaved = 0l;
        // List<String> ignoredJars = new ArrayList<>();
        
        for (String ivyFolder : ivyFolders) {
            List<File> jarFiles = SpecsIo.getFiles(new File(ivyFolder), "jar");
        
            for (File jarFile : jarFiles) {
                // Ignore javadoc and source
                // if (jarFile.getName().contains("-javadoc") || jarFile.getName().contains("-source")) {
                // bytesSaved += jarFile.length();
                // ignoredJars.add(jarFile.getName());
                // continue;
                // }
        
                jarList.append(jarFile.getName());
                jarList.append(" ");
            }
        }
        
        // Is not having an effect in the final JAR
        // if (!ignoredJars.isEmpty()) {
        // LoggingUtils
        // .msgInfo("Ignored the following JARs (~" + ParseUtils.parseSize(bytesSaved) + "): " + ignoredJars);
        // }
        
        return jarList.toString();
        */
    }

    public static void runAnt(File antScript) {
        runAnt(antScript, null);
    }

    public static void runAnt(File antScript, String target) {

        // Launch ant
        Project project = new Project();
        project.init();

        ProjectHelper.configureProject(project, antScript);

        project.addBuildListener(DeployUtils.newStdoutListener());

        // Run script
        target = target != null ? target : project.getDefaultTarget();
        project.executeTarget(target);
        // project.executeTarget(project.getDefaultTarget());
    }

    public static void runAntOnProcess(File antScript, String target) {
        if (target == null) {
            SpecsSystem.executeOnProcessAndWait(RunAnt.class, antScript.getAbsolutePath());
            return;
        }

        SpecsSystem.executeOnProcessAndWait(RunAnt.class, antScript.getAbsolutePath(), target);
    }

    /**
     * Fileset related project files (Eclipse project and sub-projects).
     *
     * @param parser
     * @param projetName
     * @return
     */
    public static List<String> buildProjectsFileset(ClasspathParser parser, String projetName) {
        List<String> projectsFileset = new ArrayList<>();

        ClasspathFiles classpathFiles = parser.getClasspath(projetName);

        // Add Filesets
        for (File projectFolder : classpathFiles.getBinFolders()) {
            projectsFileset.add(DeployUtils.getFileset(projectFolder));
        }

        return projectsFileset;
    }

    public static String buildFileset(ClasspathParser parser, String projetName, Collection<String> ivyFolders,
            boolean extractJars) {
        ClasspathFiles classpathFiles = parser.getClasspath(projetName);

        final String prefix = "			";
        StringBuilder fileset = new StringBuilder();

        // Add JAR Files
        for (File jarFile : classpathFiles.getJarFiles()) {
            String line = extractJars ? DeployUtils.getZipfilesetExtracted(jarFile)
                    : DeployUtils.getZipfileset(jarFile);

            fileset.append(prefix);
            fileset.append(line);
            fileset.append("\n");
        }

        // Add projects filesets
        buildProjectsFileset(parser, projetName).stream()
                .map(projectFileset -> prefix + projectFileset + "\n")
                .forEach(fileset::append);
        /*
        for (File projectFolder : classpathFiles.getBinFolders()) {
            String line = DeployUtils.getFileset(projectFolder);
        
            fileset.append(prefix);
            fileset.append(line);
            fileset.append("\n");
        }
        */
        // Add Ivy folders
        for (String ivyFolder : ivyFolders) {
            String ivySet = extractJars ? getIvyJarsExtracted(ivyFolder) : getIvyJars(ivyFolder);

            fileset.append(ivySet).append("\n");
        }

        return fileset.toString();
    }

    public static String buildLibFileset(ClasspathParser parser, String projetName, Collection<String> ivyFolders) {
        ClasspathFiles classpathFiles = parser.getClasspath(projetName);

        final String prefix = "         ";
        StringBuilder fileset = new StringBuilder();

        // Add JAR Files
        // System.out.println("JAR FILES: " + classpathFiles.getJarFiles());

        fileset.append(DeployUtils.getJarFileset(classpathFiles.getJarFiles()));
        // for (File jarFile : classpathFiles.getJarFiles()) {
        // String line = DeployUtils.getZipfileset(jarFile);
        //
        // fileset.append(prefix);
        // fileset.append(line);
        // fileset.append("\n");
        // }

        // Add Ivy folders
        for (String ivyFolder : ivyFolders) {
            String ivySet = getIvyJars(ivyFolder);

            fileset.append(ivySet).append("\n");
        }

        return fileset.toString();
    }

    private static String getJarFileset(List<File> jarFiles) {
        StringBuilder jarFileset = new StringBuilder();
        final String prefix = "         ";

        for (File jarFile : jarFiles) {
            jarFileset.append(prefix).append("<fileset dir=\"" + jarFile.getParentFile().getAbsolutePath() + "\">\n");
            jarFileset.append(prefix).append(prefix)
                    .append("<include name=\"")
                    .append(jarFile.getName())
                    .append("\"/>\n");
            jarFileset.append(prefix).append("</fileset>\n");
        }
        /*
        jarFileset.append(prefix).append("<fileset>\n");
        
        for (File jarFile : jarFiles) {
            jarFileset.append(prefix).append(prefix)
                    .append("<include name=\"")
                    .append(jarFile.getAbsolutePath())
                    .append("\"/>\n");
        }
        
        jarFileset.append("</fileset>\n");
        */
        return jarFileset.toString();
    }

    public static String buildMainFileset(ClasspathParser parser, String projetName) {

        final String prefix = "         ";
        StringBuilder fileset = new StringBuilder();

        // Add projects filesets
        buildProjectsFileset(parser, projetName).stream()
                .map(projectFileset -> prefix + projectFileset + "\n")
                .forEach(fileset::append);

        return fileset.toString();
    }

    public static String getIvySet(String ivyFolder, boolean extractJars) {
        return extractJars ? getIvyJarsExtracted(ivyFolder) : getIvyJars(ivyFolder);
    }

    private static String getIvyJarsExtracted(String ivyFolder) {
        // Create a zipfile for every jar inside folder
        return SpecsIo.getFiles(SpecsIo.existingFolder(ivyFolder), "jar").stream()
                .map(jarFile -> getZipfilesetExtracted(jarFile))
                .collect(Collectors.joining("\n"));
    }

    private static String getIvyJars(String ivyFolder) {
        Replacer ivyFileset = new Replacer(BuildResource.JARFOLDER_TEMPLATE);
        ivyFileset.replace("<FOLDER>", ivyFolder);

        return ivyFileset.toString();
    }

    public static CharSequence getDeleteIvyFolders(Collection<String> ivyFolders) {

        return ivyFolders.stream()
                .map(ivyFolder -> "<delete dir=\"" + ivyFolder + "\"/>")
                .collect(Collectors.joining("\n"));
    }

    public static String getCopyTask(File sourceFile, File destinationFolder) {
        return "<copy file=\"" + sourceFile.getAbsolutePath() + "\" todir=\"" + destinationFolder.getAbsolutePath()
                + "\"/>";
    }

    public static File getJarWithClassifier(String nameOfOutputJar, String classifier) {
        return getFileWithClassifier(nameOfOutputJar, classifier, "jar");
    }

    public static File getFileWithClassifier(String nameOfOutputJar, String classifier, String extension) {
        // The javadoc jar will be in a temporary folder
        File tempFolder = getTempFolder();

        if (nameOfOutputJar.endsWith(".jar")) {
            nameOfOutputJar = nameOfOutputJar.substring(0, nameOfOutputJar.length() - ".jar".length());
        }

        if (classifier != null) {
            nameOfOutputJar += "-" + classifier;
        }
        nameOfOutputJar += "." + extension;

        return new File(tempFolder, nameOfOutputJar);
    }

    /**
     * Fileset related project files sources (Eclipse project and sub-projects).
     *
     * @param parser
     * @param projetName
     * @return
     */
    public static List<String> buildSourcesFileset(ClasspathParser parser, String projetName) {
        List<String> projectsFileset = new ArrayList<>();

        ClasspathFiles classpathFiles = parser.getClasspath(projetName);

        classpathFiles.getSources().stream()
                .forEach(src -> projectsFileset.add(DeployUtils.getSourcesFileset(src)));

        // Add dependent project folders
        for (String dependentProject : classpathFiles.getDependentProjects()) {
            parser.getClasspath(dependentProject).getSources().stream()
                    .forEach(src -> projectsFileset.add(DeployUtils.getSourcesFileset(src)));

            // File projectFolder = parser.getClasspath(dependentProject).getProjectFolder();
            // projectsFileset.add(DeployUtils.getSourcesFileset(projectFolder));
        }

        return projectsFileset;
    }

    public static String buildMavenRepoPom(EclipseDeploymentData data, ClasspathParser parser) {
        String groupId = data.pomInfo.get(() -> "groupId");
        String artifactId = data.pomInfo.get(() -> "artifactId");

        Objects.requireNonNull(data.version,
                () -> "No version supplied, use for instance %BUILD% in name of output JAR");

        Set<License> licenses = parser.getLicenses(data.projetName);
        String licensesXml = licenses.stream().map(License::getXmlInfo).collect(Collectors.joining("\n"));

        Objects.requireNonNull(data.developersXml,
                () -> "No developers XML file supplied");
        String developers = SpecsIo.read(data.developersXml);

        // Get Ivy dependencies
        Set<Dependency> dependencies = parser.getIvyDependencies(data.projetName);
        String dependenciesXml = dependencies.stream()
                .map(Dependency::toMaven)
                .collect(Collectors.joining("\n"));

        System.out.println("DEPENDENCIES:\n" + dependenciesXml);
        Replacer template = new Replacer(DeployResource.DEPLOY_POM_TEMPLATE);
        template.replace("%GROUP_ID%", groupId);
        template.replace("%ARTIFACT_ID%", artifactId);
        template.replace("%VERSION%", data.version);
        template.replace("%NAME%", data.pomInfo.get(() -> "name"));
        template.replace("%DESCRIPTION%", data.pomInfo.get(() -> "description"));
        template.replace("%URL%", data.pomInfo.get(() -> "url"));
        template.replace("%LICENSES%", licensesXml);
        template.replace("%DEVELOPERS%", developers);
        template.replace("%GITHUB_PROJECT%", data.pomInfo.get(() -> "githubProject"));
        template.replace("%DEPENDENCIES%", dependenciesXml);

        return template.toString();
    }

    public static String buildJarZipfileset(List<File> jarFiles, String libFoldername) {
        StringBuilder jarZipfileset = new StringBuilder();

        Set<String> addedJars = new HashSet<>();

        int skippedJars = 0;
        for (File jarFile : jarFiles) {
            String jarFilename = jarFile.getName();

            // Skip JARs that have been already added
            if (!addedJars.add(jarFilename)) {
                skippedJars++;
                continue;
            }

            jarZipfileset.append("<zipfileset dir=\"" + jarFile.getParentFile().getAbsolutePath()
                    + "\" includes=\"" + jarFilename + "\" prefix=\"" + libFoldername + "\"/>\n");
        }

        SpecsLogs.debug("Skipped " + skippedJars + " out of " + jarFiles.size());

        return jarZipfileset.toString();
    }

    public static Collection<String> getIvyFolders(ClasspathParser parser, String projectName) {
        Collection<String> dependentProjects = parser.getDependentProjects(projectName);
        Collection<String> projectsWithIvy = BuildUtils.filterProjectsWithIvy(parser, dependentProjects);

        return projectsWithIvy.stream()
                .map(ivyProject -> BuildUtils.getIvyJarFoldername(parser.getClasspath(ivyProject).getProjectFolder()))
                .collect(Collectors.toList());
    }

}
